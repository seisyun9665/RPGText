package seisyun.rpgtext;
import org.bukkit.entity.Player;

/* RPGTextで動的文章表示を行うための文字列管理クラス
/* ある文章を、左から順番に出現していくように見せかける
/* 表示する速度、サウンドを管理する
/* 文章最後までスキップ処理を行う */

/* TODO: スピードを変更できるようにする。speedManagerの仕様を変更。RPGTextの方でも速度設定のための変更を行う */

class RPGTextSender {
    private Player player;                                  // 文章を送るプレイヤー
    private String text;                                    // 送信する文章
    private String sound = RPGText.DEFAULT_MESSAGE_SOUND;   // 送信時の音
    private int speedManager = 1;                           // 送信スピードを管理するための変数。speedManagerを+1していって、speedを超えたら新たな文字を表示する
    private int speed = RPGText.DEFAULT_MESSAGE_SPEED;      // 送信スピード
    private float pitch = RPGText.DEFAULT_MESSAGE_PITCH;    // 音の高さ
    private float volume = RPGText.DEFAULT_MESSAGE_VOLUME;  // 音の大きさ
    private int length = 0;                                 // 表示する文章の長さ（文字列の先頭から何文字目まで表示するか。配列方式なので0スタート。
                                                            // text.lengthは1スタートなので、比較で使うときは text.length() - 1 とする）

    // 送信するプレイヤーPlayer、送信したい文章sendText
    RPGTextSender(Player p,String sendText){
        player = p;
        text = sendText;
        skipColorCodeAndSpace();
    }



    /* 速度 */

    // 文章を送信する速度を指定する。1以上。
    void setSpeed(int speed) {
        if(speed < 1){
            return;
        }
        this.speed = speed;
    }
    // speedManagerがspeedを超えたら新たな文字を表示して、speedManagerを1に戻す
    // これによって送信速度を管理する
    boolean speedManagerJudge(){
        speedManager++;
        if(speedManager > speed){
            speedManager = 1;
            return true;
        }
        return false;
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
        length++;
        skipColorCodeAndSpace();
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
}
