package seisyun.rpgtext;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import seisyun.rpgtext.command.Command;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class RPGText extends JavaPlugin implements CommandExecutor, Listener {

    /*
    * プレイヤーにRPGゲームのように文字列を表示する
    * 表示したい文章をymlファイルで管理できるようにする
    * */

    /* ----- デフォルト設定変数 ----- */


    /* 音関係 */

    // 選択肢変更音
    private static String DEFAULT_SELECTION_MOVE_SOUND;
    private static float DEFAULT_SELECTION_MOVE_VOLUME;
    private static float DEFAULT_SELECTION_MOVE_PITCH;

    // 選択肢決定音
    private static String DEFAULT_SELECTION_SELECT_SOUND;
    private static float DEFAULT_SELECTION_SELECT_VOLUME;
    private static float DEFAULT_SELECTION_SELECT_PITCH;

    // メッセージ表示音
    static String DEFAULT_MESSAGE_SOUND;
    static float DEFAULT_MESSAGE_VOLUME;
    static float DEFAULT_MESSAGE_PITCH;

    /* 音関係おわり */


    // メッセージ表示速度
    static int DEFAULT_MESSAGE_SPEED;
    // 文字色
    public static String DEFAULT_MESSAGE_COLOR;
    // 何クリックでメッセージ進むか。（right | left | all）
    private static String DEFAULT_CLICK_TYPE;

    /* 情報保存関係 */

    // 文章
    // これから送信する文字列（ここからプレイヤー情報、文字列を取り出して、文字列を加工してから送信する）
    private Set<RPGTextSender> rpgTextSenderSet = new HashSet<>();
    // 表示し終えて待機中（最後まで表示されてプレイヤーのクリック待ち）になった文字列のリスト。
    private Map<Player,String> waitingTextMap = new HashMap<>();
    // プレイヤーに送信する予定の文字列のリスト。1行ずつ取り出して送るイメージ。
    private Map<Player,RPGMessages> messageListMap = new HashMap<>();

    // 例外処理
    // キャラをクリックしたプレイヤーを入れるリスト（２重クリックで１行目の文章を飛ばしてしまうのを防止する）
    private Set<Player> characterClickSet = new HashSet<>();
    // 会話終了後に次に会話できるようになるまでのクールタイムをプレイヤーごとに管理するためのセット（クールタイムがあるプレイヤーをセットに入れる）
    private Set<Player> coolTimeBeforeCanTalkSet = new HashSet<>();
    // クールタイム
    private static final int COOL_TIME_BEFORE_CAN_TALK_TICK = 5;

    // 基本メンバ
    // 読み込んだメッセージファイルのリスト
    private File messages_file;
    // config.yml。プラグインの基本情報が保存される。
    private CustomConfig messageConfig;
    // scoreboard.yml。設定した変数の情報が保存される。
    private CustomScore customScore;
    // characters.yml。どのエンティティをクリックしたら会話が発生するか
    private Characters characters;
    // 会話中のプレイヤーを動けなくするためのクラス
    private static Freeze freeze;

    /* ----- デフォルト設定変数終わり ----- */



    /* ----- 起動時設定 ----- */

    @Override
    public void onEnable() {
        // config.yml と scoreboard.yml と characters.yml と tutorial.yml を作成
        saveDefaultConfig();
        customScore = new CustomScore(this);
        characters = new Characters(this);
        messageConfig = new CustomConfig(this);
        new CustomConfig(this,"Tutorial.yml",new File(getDataFolder(),"messages"),"tutorial.yml");

        // 設定ファイル全読み込み
        reloadAllConfig();

        // イベントリスナ登録
        this.getServer().getPluginManager().registerEvents(this,this);

        // 定期実行メソッド登録（送信済みテキスト判定と待機テキスト判定）
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, this::actionTextJudge,0,1);
        scheduler.scheduleSyncRepeatingTask(this, this::waitingTextJudge,0,10);

        // コマンド登録
        Command command = new Command(this, freeze, this.characters);
        getCommand("rpgtext").setExecutor(command);
    }

    /* ----- 起動時設定終わり ----- */



    // 全部の設定ファイルを読み込み直す（config.yml characters.yml scoreboard.yml messagesの中身）
    public void reloadAllConfig(){
        // messagesの中身を取得（tutorial.ymlも含む全て）
        messages_file = getMessageFile();
        messageConfig.reloadConfig();
        customScore.reload();
        characters.reload();
        // プレイヤー停止システム読み込み
        freeze = new Freeze(this,messageConfig);
        freeze.reload();

        // デフォルト設定の読み込み（操作音やクリックの種類等）
        FileConfiguration config = messageConfig.getConfig();
        DEFAULT_MESSAGE_SOUND =                     config.getString("default.message.sound",           "");
        DEFAULT_MESSAGE_VOLUME =            (float) config.getDouble("default.message.sound",           1);
        DEFAULT_MESSAGE_PITCH =             (float) config.getDouble("default.message.pitch",           1);
        DEFAULT_MESSAGE_SPEED =                     config.getInt   ("default.message.speed",           20);
        DEFAULT_MESSAGE_COLOR =                     config.getString("default.message.color",           "");
        DEFAULT_SELECTION_MOVE_SOUND =              config.getString("default.selection.move.sound",    "");
        DEFAULT_SELECTION_MOVE_VOLUME =     (float) config.getDouble("default.selection.move.volume",   1);
        DEFAULT_SELECTION_MOVE_PITCH =      (float) config.getDouble("default.selection.move.pitch",    1);
        DEFAULT_SELECTION_SELECT_SOUND =            config.getString("default.selection.select.sound",  "");
        DEFAULT_SELECTION_SELECT_VOLUME =   (float) config.getDouble("default.selection.select.volume", 1);
        DEFAULT_SELECTION_SELECT_PITCH =    (float) config.getDouble("default.selection.select.pitch",  1);
        DEFAULT_CLICK_TYPE =                        config.getString("default.click-type",              "all");
        // rightとleftとall以外ならallにする
        if (!(DEFAULT_CLICK_TYPE.equals("right") || DEFAULT_CLICK_TYPE.equals("left") || DEFAULT_CLICK_TYPE.equals("all"))){
            DEFAULT_CLICK_TYPE = "all";
        }
    }



    // 死亡時にアクションバーの内容をリセットする
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        resetMessage(e.getEntity());
        actionbar(e.getEntity(),"");
    }

/* -----クリック検知処理----- */

    /* ブロッククリック検知 */
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Action action = e.getAction();
        // クリック以外の処理を除外
        if(!(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)){
            return;
        }

        // クリックタイプと一致していないなら除外
        if(DEFAULT_CLICK_TYPE.equals("left")){
            if(!(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)){
                return;
            }
        }
        // クリックタイプと一致していないなら除外
        if(DEFAULT_CLICK_TYPE.equals("right")){
            if(!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)){
                return;
            }
        }

        // メインハンドの時のみ処理
        if(e.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        // アドベンチャーモードの時は左クリック判定を onPlayerLeftClickInAdventure に任せるので無効化する
        if(e.getPlayer().getGameMode() == GameMode.ADVENTURE){
            if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK){
                return;
            }
        }

        // メッセージ進行処理
        progressMessage(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeftClickInAdventure(PlayerAnimationEvent e){
        Player player = e.getPlayer();
        // アドベンチャーモードだけ判定
        if(player.getGameMode() != GameMode.ADVENTURE){
            return;
        }

        // 腕を振った時
        if(e.getAnimationType() == PlayerAnimationType.ARM_SWING){
            progressMessage(player);
        }
    }

    /* ブロッククリック検知終わり */


    /* エンティティ検知 */

    // エンティティに対して左クリックしたのを検知する
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){
        // 右クリック検知は除外
        if(DEFAULT_CLICK_TYPE.equals("right")){
            return;
        }
        // プレイヤー以外は除外
        if(!(e.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player) e.getDamager();

        // 打撃ダメージ以外なら除外
        if(!(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)){
            return;
        }

        // メッセージ進行処理
        progressMessage(player);
    }

    /* エンティティ検知終わり */

/* ----- クリック検知処理終わり ----- */


    /* メッセージ進行処理 */
    private void progressMessage(Player player){
        /* 例外処理 */
        // 会話開始と同時にテキスト飛ばすのを無効化（会話開始時に右クリック判定が2重に出て最初の文章が飛ばされるバグ対策）
        if(characterClickSet.contains(player)){
            characterClickSet.remove(player);
            return;
        }
        /* 例外処理終わり */


        // 選択肢決定
        if(messageListMap.containsKey(player) && messageListMap.get(player).isSelecting()){
            messageListMap.get(player).decisionSelection();
            messageListMap.get(player).finishSelection();
            player.playSound(player.getLocation(),DEFAULT_SELECTION_SELECT_SOUND,SoundCategory.MASTER,DEFAULT_SELECTION_SELECT_VOLUME,DEFAULT_SELECTION_SELECT_PITCH);
            if(!hasWaitingText(player)){
                showMessages(player);
                return;
            }
        }

        // テキスト表示中に最後まで飛ばす処理
        if(hasTextInProgress(player)){


            setFinishActionBar(player);
        }
        // 最後まで表示されて待機状態のテキストを消す処理
        else if(hasWaitingText(player)){
            if(hasNextSelectionCommand(player)){
                // 選択肢表示
                RPGMessages message = messageListMap.get(player);
                message.increaseMessageNumber();
                message.showSelection();
            }else {
                removeWaitingText(player);
            }
        }
    }

    // プレイヤーの次の会話文が/?（選択肢）となっているならtrue
    private boolean hasNextSelectionCommand(Player player){
        return messageListMap.containsKey(player) && messageListMap.get(player).isNextSelection();
    }

    // キャラクターをクリックした時に会話を発生させる
    @EventHandler
    public void onCharacterClick(PlayerInteractEntityEvent e){
        /* 例外処理 */
        // 会話終了後のクールタイムがあるプレイヤーのクリックを無効化する
        if(coolTimeBeforeCanTalkSet.contains(e.getPlayer())) return;
        // オフハンドクリックを無効化
        if(e.getHand() == EquipmentSlot.OFF_HAND) return;
        /* 例外処理終わり */

        // 既に会話中なら弾く
        if(isTalking(e.getPlayer())) return;
        // キャラ名が設定ファイルに登録されてないなら弾く
        Entity entity = e.getRightClicked();
        if(!characters.contain(entity.getName())) return;

        // クリックをキャンセルしてメッセージ送信
        e.setCancelled(true);
        showMessagesFromConfig(e.getPlayer(),characters.get(entity.getName()));

        // 1文目飛ばすの防止
        characterClickSet.add(e.getPlayer());
    }


    /* 選択肢操作 */

    // 選択肢を左に移動する
    @EventHandler
    public void onSelectLeft(PlayerToggleSneakEvent e) {
        if (e.isSneaking()){
            if (messageListMap.containsKey(e.getPlayer()) && messageListMap.get(e.getPlayer()).isSelecting()) {
                //移動
                messageListMap.get(e.getPlayer()).selectLeft();
                //表示
                messageListMap.get(e.getPlayer()).showSelection();
                //効果音
                e.getPlayer().playSound(e.getPlayer().getLocation(),DEFAULT_SELECTION_MOVE_SOUND, SoundCategory.MASTER,DEFAULT_SELECTION_MOVE_VOLUME,DEFAULT_SELECTION_MOVE_PITCH);
            }
        }
    }
    // 右に移動する
    @EventHandler
    public void onSelectRight(PlayerSwapHandItemsEvent e){
        if(messageListMap.containsKey(e.getPlayer()) && messageListMap.get(e.getPlayer()).isSelecting()){
            e.setCancelled(true);
            //移動
            messageListMap.get(e.getPlayer()).selectRight();
            //表示
            messageListMap.get(e.getPlayer()).showSelection();
            //効果音
            e.getPlayer().playSound(e.getPlayer().getLocation(),DEFAULT_SELECTION_MOVE_SOUND, SoundCategory.MASTER,DEFAULT_SELECTION_MOVE_VOLUME,DEFAULT_SELECTION_MOVE_PITCH);
        }
    }

    /* 選択肢操作終わり */


    /* 動的テキスト表示 */
 
    // ４種類。テキストのみ、速度指定、音指定、両方指定
    public void dynamicActionBar(Player player, String text){
        dynamicActionBar(player,text,0);
    }
    private void dynamicActionBar(Player player,String text,int speed){
        dynamicActionBar(player,text,speed,"",0,0,true);
    }
    public void dynamicActionBar(Player player, String text, int speed, String sound, float pitch, float volume,boolean skip){
        RPGTextSender rpgTextSender = new RPGTextSender(player,text);
        if(rpgTextSender.isFinished()) return;
        if(speed > 0) rpgTextSender.setSpeed(speed);
        if(!sound.equals("")) rpgTextSender.setSound(sound, pitch, volume);
        rpgTextSender.setSkip(skip);
        putDynamicAction(player,rpgTextSender);
    }

    // RPGMessagesから直接値を取得
    public void dynamicActionBarFromRPGMessages(Player player, String text, RPGMessages rpgMessages){
        dynamicActionBar(player,text, rpgMessages.getSpeed(),rpgMessages.getSound(), rpgMessages.getPitch(), rpgMessages.getVolume(),rpgMessages.canSkip());
    }
    // 文字列を動的に表示する（任意のRPGTextSenderをrpgTextSenderListに追加して、動的に表示する機構に組み込む）
    private void putDynamicAction(Player player,RPGTextSender rpgTextSender){
        // いま表示中のテキストが既にあったら、そこに上書きする形で表示する
        if(hasTextInProgress(player)){
            for(RPGTextSender textForReplace : rpgTextSenderSet){
                if(textForReplace.getPlayer() == player) {
                    textForReplace.replace(rpgTextSender);
                    return;
                }
            }
        }

        // 待機中のテキストを消して新たなヤツを表示
        if(hasWaitingText(player)){
            waitingTextMap.remove(player);
        }
        rpgTextSenderSet.add(rpgTextSender);

        // プレイヤーを動けなくする
        freeze.set(player);
    }

    /* 動的テキスト表示終わり */


    /* アクションバー表示 */

    // rpgTextSenderから文字を取得してアクションバーに表示する
    private void displayInActionbar(RPGTextSender rpgTextSender){
        // 送信するテキストを生成する（元の文字列と、送信する長さをもとに整形する）
        String sendText = rpgTextSender.getLeftAlignedText();

        // 表示と音
        actionbar(rpgTextSender.getPlayer(),sendText);
        rpgTextSender.playSound();

        // 表示する文字の長さを+1する
        rpgTextSender.incrementDisplayLength();
    }
    // プレイヤーのアクションバーに文字列を表示する
    private void actionbar(Player player,String text){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
    }

    /* アクションバー表示終わり */


    /* ----- 文字表示判定 ----- */

    /* 送信中テキスト*/

    // 送信中テキストがある場合、新たな文字の表示を進行させる。
    private void actionTextJudge(){
        // 送信中テキストが無い場合は処理を行わない
        if(rpgTextSenderSet.isEmpty()) return;

        // 削除する予定のテキストを保持するリスト
        List<RPGTextSender> removeList = new ArrayList<>();
        
        for (RPGTextSender rpgTextSender : rpgTextSenderSet){
            // judgeSendingBySpeed()で送信するか判定（最高速度が1tick１文字なので、このtickで送信するかしないか判定して速度管理する）
            if(rpgTextSender.judgeSendingBySpeed()){

                // 1回だけ表示
                displayInActionbar(rpgTextSender);

                // 最後まで表示し終えたテキストを待機状態にする
                if(rpgTextSender.isFinished()) { 
                    removeList.add(rpgTextSender);
                    setWaitingText(rpgTextSender);
                }
            }
        }

        // 送信し終えたテキストをリストから削除する処理
        if(removeList.isEmpty()) return; 
        for(RPGTextSender rpgTextSender : removeList){
            rpgTextSenderSet.remove(rpgTextSender);
        }
    }
    // そのプレイヤーに送信中のテキストがあるか
    private boolean hasTextInProgress(Player player){
        if(rpgTextSenderSet != null && !rpgTextSenderSet.isEmpty()){
            for (RPGTextSender rpgTextSender : rpgTextSenderSet){
                if(rpgTextSender.getPlayer() == player){
                    return true;
                }
            }
        }
        return false;
    }
    /* 送信中テキスト終わり */

    // テキスト表示中に最後まで飛ばす
    private void setFinishActionBar(Player player){
        // 該当のプレイヤーのrpgTextSenderを探す
        RPGTextSender removeRpgTextSender = null;
        for(RPGTextSender rpgTextSender : rpgTextSenderSet){
            if(rpgTextSender.getPlayer().equals(player)){
                /* 例外処理 */
                // スキップ不可能なら処理しない
                if(!rpgTextSender.canSkip()) return;
                /* 例外処理終わり */

                // 最後の文字まで進ませて表示する。うるさいので音は消す
                rpgTextSender.setFinish();
                rpgTextSender.setNoSound();
                displayInActionbar(rpgTextSender);

                // 全体のlistから削除する用にとっておく
                removeRpgTextSender = rpgTextSender;

                //テキストを待機状態にする
                setWaitingText(rpgTextSender);
                break;
            }
        }
        if(removeRpgTextSender != null){
            rpgTextSenderSet.remove(removeRpgTextSender);
        }
    }

    // そのプレイヤーに送信中・待機中のテキストがあるか
    private boolean isTalking(Player player){
        return hasWaitingText(player) || hasTextInProgress(player);
    }

    /* 待機中テキスト */

    // 最後まで表示し終えて待機中テキストがあるか判定し、あったらプレイヤーに表示する。強制進行がtureの時は次へ進ませる
    private void waitingTextJudge(){
        if(waitingTextMap != null && !waitingTextMap.isEmpty()){
            for(Player player :waitingTextMap.keySet()){
                String waitingText = waitingTextMap.get(player);
                /* 自動進行処理終わり */

                /* 終端点滅処理 */
                if(waitingText.length() > 5 && waitingText.charAt(0) == ' ' && waitingText.endsWith("§r§n ")){
                    // 光ってる時に光ってるやつを消す（待機中テキストの形が" <テキスト>_"の時の処理）
                    waitingText = waitingText.substring(1,waitingText.length() - 5);
                }else{
                    // 消えてる時光らせる
                    waitingText = " " + waitingText + "§r§n ";
                }
                waitingTextMap.put(player,waitingText);
                /* 終端点滅処理終わり */

                // アクションバーに待機中文字列を表示する
                actionbar(player,waitingTextMap.get(player));
            }
        }
    }
    // そのプレイヤーに待機状態のテキストがあるか
    private boolean hasWaitingText(Player player){
        return waitingTextMap.containsKey(player);
    }
    // テキストを待機状態にする。自動進行がtrueの時は自動進行させる
    private void setWaitingText(RPGTextSender rpgTextSender){
        waitingTextMap.put(rpgTextSender.getPlayer(),rpgTextSender.getText());

        // 自動進行
        if(rpgTextSender.isAuto()) showMessages(rpgTextSender.getPlayer());
    }
    // あるプレイヤーの待機中テキストを削除して、アクションバーをきれいにする
    private void removeWaitingText(Player player){
        waitingTextMap.remove(player);
        actionbar(player,"");

        // 次に表示する文字列があればプレイヤーに表示する
        showMessages(player);
    }
    //待機中のテキスト装飾を切り替える（❙が点滅する感じ）
    /* 待機中テキスト終わり */


    /* ----- 文字表示判定終わり ------ */


    /* 数字判定 */

    // 文字列がfloat型ならtrue
    public boolean isFloat(String string){
        try{
            Float.parseFloat(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    // 文字列がint型ならtrue
    public boolean isInteger(String string){
        try{
            Integer.parseInt(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    /* 数字判定終わり */


    /* メッセージとコマンド */

    // プレイヤーが次のメッセージを待機させていたらそれを送る
    private void showMessages(Player player){
        // 次のメッセージが無いなら終わり
        if(!messageListMap.containsKey(player)){
            // プレイヤーの停止状態を解除してクールタイム設定
            endTalk(player);
            return;
        }

        // プレイヤーに設定されているメッセージのリストを呼び出す
        RPGMessages rpgMessages = messageListMap.get(player);

        // メッセージの一番最初に/?（選択肢表示のコマンド）があるとmessagesの方で処理できないためこちらで行う
        if(rpgMessages.isFirstSelection()){
            // 選択肢表示してメッセージを一つ次に進める
            rpgMessages.showSelection();
            rpgMessages.increaseMessageNumber();
            return;
        }

        // 取得したメッセージが""の場合はすべてのメッセージの送信終わり
        String message = rpgMessages.getMessage();
        if(message.equals("")){
            endTalk(player);
            return;
        }

        // /?の処理
        else if(message.equalsIgnoreCase("/?")){
            // RPGMessageの方で選択肢を表示しているため何もしない
            return;
        }

        // /wait の処理
        else if(message.startsWith("/wait ")){
            // waitコマンドの引数の数字分だけディレイをかける

            /* 例外処理 */
            String[] args = message.split(" ");
            if(args.length < 2) showMessages(player);       // 引数がない
            if(!isInteger(args[1])) showMessages(player);   // 引数が数字じゃない

            // ディレイかけてshowMessagesを実行
            int tick = Integer.parseInt(args[1]);
            getServer().getScheduler().runTaskLater(this, () -> showMessages(player), tick);
            return;
        }

        // 他のメッセージにジャンプしていたら新たにメッセージを読み込む
        if(rpgMessages.isJumping()){
            getServer().dispatchCommand(getServer().getConsoleSender(),"rpgtext config " + player.getName() + " " + message);
            return;
        }

        // 表示
        if(rpgMessages.getSound().equals("")){
            dynamicActionBarFromRPGMessages(player, message, rpgMessages);
        }

        // もしメッセージが最後まで送り終えていればメッセージを削除する
        judgeFinishMessage(player, rpgMessages);
    }

    // クールタイム設定
    private void setCoolTime(Player player){
        // 次の会話可能までのクールタイム設定（会話したくないのに、間違って右クリックするとすぐに会話再開されてしまうのを防止する）
        coolTimeBeforeCanTalkSet.add(player);
        getServer().getScheduler().runTaskLater(this, () -> {
            // クールタイム経過後にcoolTimeBeforeCanTalkからplayerを削除
            coolTimeBeforeCanTalkSet.remove(player);
        }, COOL_TIME_BEFORE_CAN_TALK_TICK);
    }

    // 会話終了の処理（クールタイムとフリーズ解除）
    private void endTalk(Player player){
        setCoolTime(player);
        freeze.remove(player);
    }

    // もしメッセージを最後まで送り終えていればメッセージを削除する
    private void judgeFinishMessage(Player player, RPGMessages rpgMessages){
        if(rpgMessages.isFinished()){
            messageListMap.remove(player);
            endTalk(player);
        }
    }

    // tutorial.yml/users2 といった感じのパスからRPGMessagesを取得する。取得できなかったらnullを返す
    private RPGMessages getRPGMessagesFromConfig(Player player, String section){
        // コンフィグ取得
        CustomConfig customConfig = getMessageConfig(section);
        if(customConfig == null){
            return null;
        }else{
            // パス生成
            FileConfiguration config = customConfig.getConfig();
            String[] configPaths = section.split("/");
            String messagePath = configPaths[configPaths.length - 1];

            // コンフィグから音、送信速度、文字色の設定を取得。無ければデフォルト
            String sound = DEFAULT_MESSAGE_SOUND, color = DEFAULT_MESSAGE_COLOR;
            float pitch = DEFAULT_MESSAGE_PITCH, volume = DEFAULT_MESSAGE_VOLUME;
            int speed = DEFAULT_MESSAGE_SPEED;
            if(config.contains("sound")) sound = config.getString("sound", DEFAULT_MESSAGE_SOUND);
            if(config.contains("pitch")) pitch = (float)config.getDouble("pitch", DEFAULT_MESSAGE_PITCH);
            if(config.contains("volume")) volume = (float)config.getDouble("volume", DEFAULT_MESSAGE_VOLUME);

            if(config.contains("color")) color = config.getString("color", DEFAULT_MESSAGE_COLOR);
            if(config.contains("speed")) speed = config.getInt("speed", DEFAULT_MESSAGE_SPEED);

            // コンフィグの中身をRPGMessagesへ変換
            if(!config.contains(messagePath) || !config.isList(messagePath)){
                // 存在していないまたはリストではない
                return null;
            }
            RPGMessages messages = new RPGMessages(config.getStringList(messagePath),player,this,customScore,section);
            messages.setSound(sound);
            messages.setPitch(pitch);
            messages.setVolume(volume);
            messages.setSpeed(speed);
            messages.setColor(color);
            return messages;
        }
    }

    // tutorial.yml/users2 といった感じのパスからメッセージを取得してプレイヤーに送信する。成功したらtrueを、失敗したらfalseを送る
    public boolean showMessagesFromConfig(Player player, String section){
        RPGMessages rpgMessages = getRPGMessagesFromConfig(player, section);
        if(rpgMessages == null){
            return false;
        }
        messageListMap.put(player,rpgMessages);
        showMessages(player);
        return true;
    }

    /* メッセージとコマンド終わり */


    /* ファイル */

    // プラグインからmessagesフォルダを取得して返す。無かったらnull
    private File getMessageFile(){
        return getFileFromPlugin("messages");
    }

    // プラグインのデータファイルから指定した名前のファイルを取得する
    private File getFileFromPlugin(String fileName){
        return getFile(this.getDataFolder(),fileName);
    }

    // 指定した名前のファイルが存在したら返す。存在しない場合null
    private File getFile(File dataFile,String fileName){
        File[] Files = dataFile.listFiles();
        if(Files != null) {
            for (File file : Files) {
                if(file.getName().equals(fileName)){
                    return file;
                }
            }
        }
        return null;
    }

    // 引数のファイルがyaml形式ならtrue
    private boolean isYaml(File file){
        try{
            YamlConfiguration.loadConfiguration(file);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // messagesの中から引数のパスをたどってコンフィグファイルを返す。無ければnull
    // パスの例 "Bob/bob.yml/talk1" これを "Bob bob.yml talk1"にして、フォルダとファイルをたどっていく
    private CustomConfig getMessageConfig(String configName){
        /* 例外処理 */
        // "/"が含まれていない場合
        if(!configName.contains("/")) return null;
        /* 例外処理終わり */

        /* パス作成 */
        // パスを分解
        String[] fileNames = configName.split("/");
        // パスの最後の部分(例の"talk1"の部分)と"/"を元テキストから消す
        String pathName = configName.substring(0,configName.length() - fileNames[fileNames.length - 1].length() - 1);
        // 結合
        Path path = Paths.get(messages_file.getAbsolutePath() + "\\" +  pathName.replaceAll("/", "\\"));
        // ファイル存在したらCustomConfigにして返す
        if(path.toFile().exists()) return new CustomConfig(this, path.toFile());
        return null;
    }

    /* ファイル終わり */


    // 一つの文章内の特殊シンボルを置き換える "\\" -> "§"m "%player%"
    public String replaceSymbolInText(String text, Player player){
        if(text.contains(RPGMessages.REPLACED_SYMBOL_COLOR_CODE)){
            text = text.replace(RPGMessages.REPLACED_SYMBOL_COLOR_CODE,"§");
        }
        if(text.contains(RPGMessages.REPLACED_SYMBOL_PLAYER_NAME)){
            text = text.replace(RPGMessages.REPLACED_SYMBOL_PLAYER_NAME,player.getName());
        }
        return text;
    }



    private void resetMessage(Player player){
        if(hasWaitingText(player)){
            waitingTextMap.remove(player);
        }
        RPGTextSender remove = null;
        for(RPGTextSender rpgTextSender : rpgTextSenderSet){
            if(rpgTextSender.getPlayer() == player){
                remove = rpgTextSender;
            }
        }
        if(remove != null){
            rpgTextSenderSet.remove(remove);
        }
        messageListMap.remove(player);
    }

    /* Freeze設定 */
    // 指定したプレイヤーのフリーズのオンオフを設定する
    static public void setFreeze(Player player,boolean enable){ if(enable) freeze.set(player); else freeze.remove(player); }

    /* Freeze設定終わり */


}