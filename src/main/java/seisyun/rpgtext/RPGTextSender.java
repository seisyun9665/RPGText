package seisyun.rpgtext;
import org.bukkit.entity.Player;

/* RPGTextで動的文章表示を行うための文字列管理クラス
/* ある文章を、左から順番に出現していくように見せかける
/* 表示する速度、サウンドを管理する
/* 文章最後までスキップ処理を行う */

class RPGTextSender {
    private Player player;                                  // 文章を送るプレイヤー
    private String text;                                    // 送信する文章
    private String sound = RPGText.DEFAULT_MESSAGE_SOUND;   // 送信時の音
    private int sendTime = 0;                               // 送信してから何tick経過したか。
    private int sendCount = 0;                              // RPGTextの方で実際に表示した回数
    private int speed = RPGText.DEFAULT_MESSAGE_SPEED;      // 一秒間に送る文字数
    private float pitch = RPGText.DEFAULT_MESSAGE_PITCH;    // 音の高さ
    private float volume = RPGText.DEFAULT_MESSAGE_VOLUME;  // 音の大きさ
    private int length = 0;                                 // 表示する文章の長さ（文字列の先頭から何文字目まで表示するか。配列方式なので0スタート。
                                                            // text.lengthは1スタートなので、比較で使うときは text.length() - 1 とする）
    private boolean skip = true;                            // 表示途中でスキップ可能か
    private boolean auto = true;                            // 会話が自動進行するか
    private String color = "";                              // 基本の色
    // 会話文左揃え用の空白。100マス。
    private static final String COMPLETION_SPACE = "                                                                                                              ";

    // 送信するプレイヤーPlayer、送信したい文章sendText
    RPGTextSender(Player p,String sendText){
        player = p;
        text = sendText;
        skipColorCodeAndSpace();
    }

    public void setColor(String color) { this.color = color; }

    /* 速度 */

    // 文章を送信する速度を指定する。1以上。
    void setSpeed(int speed) {
        if(speed < 1){
            this.speed = RPGText.DEFAULT_MESSAGE_SPEED;
            return;
        }
        this.speed = speed;
    }
    // 1秒間にspeed文字を表示する
    // sendTime++
    // まず20の倍数ごとに必ず表示する文字数を算出する ex:20以上なら1文字、40以上なら2文字
    // speedから20ずつ引いて残った数字sendCountPerSecondsを使い、sendCountと比較して1文字送信する文字を増やすか検討する。
    // sendTTime >= 20/sendCountPerSeconds*sendCount なら１文字増やす
    // 最終的に0文字ならfalseを返す。1文字以上ならsendCount++してtrueを返す
    // 20の倍数文字ならすべてのtickで送信するので、最初に例外処理を加える
    boolean judgeSendingBySpeed(){
        /* 例外処理 */
        // 速度がぴったり20の時は1文字ずつ送るだけなので処理しない（文字数とか表示するtickを考える必要がない）
        if(speed == 20) return true;

        // 送信speedが20より大きい分だけ表示数を増やす
        int sendCountPerSeconds = speed % 20;
        int incrementCount = 0; // 最低限、同時に何文字表示するか
        if(speed >= 20){
            for(int i = speed; i > 20; i -= 20){
                incrementCount++;
            }
        }
        if(incrementCount > 1) addDisplayLength(incrementCount - 1);    // このtickで表示するのかまだわからないので1減らしておく

        // speedが20の倍数の時は処理しない（毎回送る文字数が同じなのでsendTimeを気にしなくていい）
        if(sendCountPerSeconds == 0) {
            incrementDisplayLength();   // -2した分の調整。incrementCountが1のケースはspeed==20で弾かれるため考えなくていい
            return true;
        }

        /* 例外処理終わり */

        // sendCountが0の時は必ず送信
        if(sendCount == 0){
            sendCount++;
            return true;
        }

        /* このtickで文字を表示する（増やす）かを判定する */

        // 20 / 20tick内で表示する文字数 = tick毎表示文字数、sendCountをかけて、sendTimeと比較
        sendTime++;
        double tickCountPerCharacter = 20 / (double)sendCountPerSeconds;
        boolean isSend = sendTime >= tickCountPerCharacter * sendCount;     // このtickで文字を表示（または表示する文字数を+1）するか

        // 送る場合はsendCount++する
        if(isSend) sendCount++;

        // speedが20以上なら必ず送信、isSendがtrueなら更にinclementする
        if(incrementCount >= 1){
            if(isSend) incrementDisplayLength();

            return true;
        }
        // speedが20未満ならisSendを返す
        return isSend;
    }

    /* 速度終わり */

    
    Player getPlayer() {
        return player;
    }

    // 今何文字目まで表示しているか。配列方式なので0スタート。
    int getLength() {
        return length;
    }

    // 送信している元の文章
    String getText() {
        return text;
    }

    // カラーコードと半角スペースの表示を飛ばすための処理。カラーコードが無い場所までlengthを移動する。
    private void skipColorCodeAndSpace(){
        while(true) {
            if(length > text.length() - 1){
                break;
            }
            if (text.charAt(length) == '§') {
                length += 2;
            }else if(text.charAt(length) == ' '){
                length++;
            }else{
                break;
            }
        }
    }

    // 表示する文字数を1つ増やす
    void incrementDisplayLength(){
        if(isFinished()) return;
        length++;
        skipColorCodeAndSpace();
    }

    // 引数の数だけ表示する文字数を増やす
    void addDisplayLength(int amount){
        for (int i = 0; i < amount; i++) {
            if(isFinished()) return;
            length++;
            skipColorCodeAndSpace();
        }
    }

    // 最後まで表示し終えていたらtrue
    boolean isFinished(){
        return text.length() - 1 < length;
    }

    // 他のRPGTextSenderの内容を現在のものに移す。表示途中で他の文章を表示したい時に使う。
    void replace(RPGTextSender rpgTextSender){
        player = rpgTextSender.getPlayer();
        text = rpgTextSender.getText();
        length = 0;
    }

    // 全部の文字を表示した状態にする
    void setFinish(){
        length = text.length() - 1;
    }


    /* サウンド決定系 */

    // 会話中のプレイヤーに向けてサウンドを再生する
    void playSound(){
        if(sound != null) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
    void setSound(String sound,float pitch,float volume) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }
    void setNoSound(){
        this.sound = null;
    }

    /* サウンド決定系終わり */


    /* 文字列整形（左揃え） */

    // 中央揃えとなっているマイクラのアクションバーに、左揃えで表示できるように加工する。画面外に同じテキストを並べることで、無理やり中央揃えにする
    public String getLeftAlignedText(){
        /* 例外処理 */
        // 既に全て送信済みの場合はそのまま返す（元の文の長さと現在の添字の位置が同じか添字の方が大きい場合は処理できない）
        if(text.length() - 1 <= length){
            return text;
        }
        /* 例外処理終わり */

        // StringBuilderに空白、本体、空白、右側の順番で追加していく（文の長さを変えないことで左揃えにする）
        String mainText = text.substring(0,length + 1);
        String rightText = text.substring(length + 1);

        // 文字色＋文字の太さ変更で幅が変わるため、メインテキスト後に§rを入れる
        return color + COMPLETION_SPACE + mainText + "§r" + COMPLETION_SPACE + rightText;
    }

    /* 文字列整形（左揃え）終わり */

    /* スキップ */
    public void setSkip(boolean skip)   { this.skip = skip; }
    public boolean canSkip()            { return skip; }
    /* 自動進行 */
    public void setAuto(boolean auto) { this.auto = auto; }
    public boolean isAuto() { return auto; }
}
