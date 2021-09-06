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
    // 何クリックでメッセージ進むか。（right | left | all）
    private static String DEFAULT_CLICK_TYPE;

    /* 文章情報保存関係 */

    // これから送信する文字列（ここからプレイヤー情報、文字列を取り出して、文字列を加工してから送信する）
    private Set<RPGTextSender> rpgTextSenderList = new HashSet<>();
    // 表示し終えて待機中（最後まで表示されてプレイヤーのクリック待ち）になった文字列のリスト。
    private Map<Player,String> waitingTextMap = new HashMap<>();
    // プレイヤーに送信する予定の文字列のリスト。1行ずつ取り出して送るイメージ。
    private Map<Player,RPGMessages> messageListMap = new HashMap<>();
    // キャラをクリックしたプレイヤーを入れるリスト（２重クリックで１行目の文章を飛ばしてしまうのを防止する）
    private Set<Player> characterClickList = new HashSet<>();
    // 読み込んだメッセージファイルのリスト
    private List<CustomConfig> messageConfigList;
    // config.yml。プラグインの基本情報が保存される。
    private CustomConfig messageConfig;
    // scoreboard.yml。設定した変数の情報が保存される。
    private CustomScore customScore;
    // characters.yml。どのエンティティをクリックしたら会話が発生するか
    private Characters characters;
    // 会話中のプレイヤーを動けなくするためのクラス
    private Freeze freeze;

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
        Command command = new Command(this, this.freeze, this.characters);
        getCommand("rpgtext").setExecutor(command);
    }

    /* ----- 起動時設定終わり ----- */



    // 全部の設定ファイルを読み込み直す（config.yml characters.yml scoreboard.yml messagesの中身）
    public void reloadAllConfig(){
        // messagesの中身を取得（tutorial.ymlも含む全て）
        messageConfigList = getMessageFiles();
        messageConfig.reloadConfig();
        for(CustomConfig config : messageConfigList){
            config.reloadConfig();
        }
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
        DEFAULT_MESSAGE_SPEED =                     config.getInt   ("default.message.speed",           1);
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
        click(e.getPlayer());
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
            click(player);
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
        click(player);
    }

    /* エンティティ検知終わり */

/* ----- クリック検知処理終わり ----- */


    /* メッセージ進行処理 */
    private void click(Player player){
        if(characterClickList.contains(player)){
            characterClickList.remove(player);
            return;
        }
        //選択肢決定
        if(messageListMap.containsKey(player) && messageListMap.get(player).isSelecting()){
            messageListMap.get(player).decisionSelection();
            messageListMap.get(player).finishSelection();
            player.playSound(player.getLocation(),DEFAULT_SELECTION_SELECT_SOUND,SoundCategory.MASTER,DEFAULT_SELECTION_SELECT_VOLUME,DEFAULT_SELECTION_SELECT_PITCH);
            if(!hasWaitingText(player)){
                showMessages(player);
                return;
            }
        }

        if(isContain(player)){ //テキスト表示中に最後まで飛ばす処理
            setFinishActionBar(player);
        }else if(hasWaitingText(player)){ //最後まで表示されて待機状態のテキストを消す処理
            if(isNextSelection(player)){
                //選択肢表示
                RPGMessages message = messageListMap.get(player);
                message.increaseMessageNumber();
                message.showSelection();
            }else {
                removeWaitingText(player);
            }
        }
    }

    // キャラクターをクリックした時に会話を発生させる
    @EventHandler
    public void onCharacterClick(PlayerInteractEntityEvent e){
        Entity entity = e.getRightClicked();
        // 既に会話中でない＆キャラ名が設定ファイルに登録されている
        if(!isTalking(e.getPlayer()) && characters.contain(entity.getName())){
            e.setCancelled(true);
            showMessagesFromConfig(e.getPlayer(),characters.get(entity.getName()));
            // 1文目飛ばすの防止
            characterClickList.add(e.getPlayer());
        }
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


    //アクションバーに動的にテキストを表示する
    public void dynamicActionBar(Player player, String text){
        RPGTextSender rpgTextSender = new RPGTextSender(player,text);
        if(!rpgTextSender.isFinished()){
            putDynamicAction(player,rpgTextSender);
        }
    }
    //速度を設定
    private void dynamicActionBar(Player player,String text,int speed){
        RPGTextSender rpgTextSender = new RPGTextSender(player,text);
        rpgTextSender.setSpeed(speed);
        if(!rpgTextSender.isFinished()){
            putDynamicAction(player,rpgTextSender);
        }
    }
    //音を設定
    private void dynamicActionBar(Player player,String text,String sound,float pitch,float volume){
        RPGTextSender rpgTextSender = new RPGTextSender(player,text);
        if(!rpgTextSender.isFinished()){
            rpgTextSender.setSound(sound,pitch,volume);
            putDynamicAction(player,rpgTextSender);
        }
    }
    //音と速度を設定
    public void dynamicActionBar(Player player, String text, int speed, String sound, float pitch, float volume){
        RPGTextSender rpgTextSender = new RPGTextSender(player,text);
        rpgTextSender.setSpeed(speed);
        if(!rpgTextSender.isFinished()){
            rpgTextSender.setSound(sound,pitch,volume);
            putDynamicAction(player,rpgTextSender);
        }
    }


    private void dynamicActionBarSingle(RPGTextSender rpgTextSender){
        String sendText = getComplementedText(rpgTextSender.getText(),rpgTextSender.getLength() + 1);
        actionbar(rpgTextSender.getPlayer(),sendText);
        rpgTextSender.playSound();
        rpgTextSender.nextLength();
    }

    private void actionbar(Player player,String text){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
    }



    //指定した場所から最後まで、半角全角区別つけてスペースで埋める
    private String getComplementedText(String text,int length){
        if(text.length() - 1 < length){
            return text;
        }
        String complementedText = text.substring(0,length);
        StringBuilder appends = new StringBuilder();
        for(int i = length;i < text.length();i++){
            //記号系は埋めない
            if(i > 0 && text.charAt(i - 1) == '§'){
                continue;
            }
            if(isHalfWidth(text.charAt(i))){
                //半角の時は半角スペースで穴埋め
                appends.append(" ");
            }else if(text.charAt(i) != '§' ){
                //全角で記号じゃない時は半角スペース二つで穴埋め
                appends.append("  ");
            }
        }
        complementedText += appends.toString();
        return complementedText;
    }

    //半角判定
    private boolean isHalfWidth(char chara){
        return String.valueOf(chara).getBytes().length < 2;
    }

    //プレイヤーが話しかけられているか
    private boolean isContain(Player player){
        if(rpgTextSenderList != null && !rpgTextSenderList.isEmpty()){
            for (RPGTextSender rpgTextSender : rpgTextSenderList){
                if(rpgTextSender.getPlayer() == player){
                    return true;
                }
            }
        }
        return false;
    }

    private void actionTextJudge(){
        List<RPGTextSender> removeList = new ArrayList<>();
        if(!rpgTextSenderList.isEmpty()){
            for (RPGTextSender rpgTextSender :rpgTextSenderList){
                if(rpgTextSender.speedManagerJudge()){
                    dynamicActionBarSingle(rpgTextSender);
                    if(rpgTextSender.isFinished()) { //メッセージ送り終わった処理
                        removeList.add(rpgTextSender);
                        //テキストを待機状態にする
                        setWaitingText(rpgTextSender);
                    }
                }
            }
        }
        if(!removeList.isEmpty()){
            for(RPGTextSender rpgTextSender : removeList){
                rpgTextSenderList.remove(rpgTextSender);
            }
        }
    }

    private void putDynamicAction(Player player,RPGTextSender rpgTextSender){
        if(isContain(player)){ //表示中のテキストがあった時の処理（上書き）
            for(RPGTextSender rpgTextSender1 : rpgTextSenderList){
                rpgTextSender1.replace(rpgTextSender);
            }
        }else{
            rpgTextSenderList.add(rpgTextSender);
        }
        if(hasWaitingText(player)){ //待機中のテキストがあった時の処理（上書き）
            waitingTextMap.remove(player);
        }
        //プレイヤーを停止
        freeze.set(player);
    }

    private void setFinishActionBar(Player player){
        RPGTextSender removeRpgTextSender = null;
        for(RPGTextSender rpgTextSender : rpgTextSenderList){
            if(rpgTextSender.getPlayer().equals(player)){
                rpgTextSender.setFinish();
                rpgTextSender.setNoSound();
                dynamicActionBarSingle(rpgTextSender);
                removeRpgTextSender = rpgTextSender;
                //テキストを待機状態にする
                setWaitingText(rpgTextSender);
                break;
            }
        }
        if(removeRpgTextSender != null){
            rpgTextSenderList.remove(removeRpgTextSender);
        }
    }
    //テキスト待機状態判定
    private boolean hasWaitingText(Player player){
        return waitingTextMap.containsKey(player);
    }

    //テキストを待機状態にする
    private void setWaitingText(RPGTextSender rpgTextSender){
        waitingTextMap.put(rpgTextSender.getPlayer(),rpgTextSender.getText());
    }
    //待機中テキストがあったら表示する
    private void waitingTextJudge(){
        if(waitingTextMap != null && !waitingTextMap.isEmpty()){
            toggleWaitingText();//テキスト表示切り替え
            for(Player player :waitingTextMap.keySet()){
                actionbar(player,waitingTextMap.get(player));
            }
        }
    }
    //待機中テキストを削除する
    private void removeWaitingText(Player player){
        waitingTextMap.remove(player);
        actionbar(player,"");
        showMessages(player);
    }
    //待機中のテキスト装飾を切り替える（❙が点滅する感じ）
    private void toggleWaitingText(){
        for(Player player : waitingTextMap.keySet()){
            String waitingText = waitingTextMap.get(player);
            if(waitingText.length() > 5 && waitingText.charAt(0) == ' ' && waitingText.endsWith("§r§n ")){ //待機中テキストの形が" <テキスト>_"の時の処理
                waitingText = waitingText.substring(1,waitingText.length() - 5);
            }else{
                waitingText = " " + waitingText + "§r§n ";
            }
            waitingTextMap.put(player,waitingText);
        }
    }

    //float型か
    public boolean isFloat(String string){
        try{
            Float.parseFloat(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    //int判定
    public boolean isInteger(String string){
        try{
            Integer.parseInt(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    //プレイヤーが次のメッセージを待機させていたらそれを送る
    private void showMessages(Player player){
        if(!messageListMap.containsKey(player)){
            freeze.remove(player);
            return;
        }
        RPGMessages rpgMessages = messageListMap.get(player);
        if(rpgMessages.isFirstSelection()){
            rpgMessages.showSelection();
            rpgMessages.increaseMessageNumber();
            return;
        }
        String message = rpgMessages.getMessage();
        if(message.equals("")){
            freeze.remove(player);
        }
        if(message.equalsIgnoreCase("/?")){
            //選択肢表示中
            return;
        }
        //設定ジャンプ判定
        if(rpgMessages.isJumping()){
            getServer().dispatchCommand(getServer().getConsoleSender(),"rpgtext config " + player.getName() + " " + message);
            return;
        }
        //音が設定されていたら音を追加
        if(rpgMessages.getSound().equals("")){
            dynamicActionBar(player, message, rpgMessages.getSpeed());
        }else{
            dynamicActionBar(player, message, rpgMessages.getSpeed(),rpgMessages.getSound(),rpgMessages.getPitch(),rpgMessages.getVolume());
        }
        judgeFinishMessage(player, rpgMessages);
    }

    //もしメッセージが最後まで送り終えていればメッセージを削除する
    private void judgeFinishMessage(Player player, RPGMessages rpgMessages){
        if(rpgMessages.isFinished()){
            messageListMap.remove(player);
        }
    }

    //選択されたセクションからRPGメッセージが返ってくる
    private RPGMessages getRPGMessagesFromConfig(String section,Player player){
        String[] configPath;
        if(section.contains("/")){
            configPath = section.split("/");
        }else{
            return null;
        }
        CustomConfig customConfig = getMessageConfig(configPath[0]);
        if(customConfig == null){
            return null;
        }else{
            FileConfiguration config = customConfig.getConfig();
            if(!config.contains(configPath[1]) || !config.isList(configPath[1])){
                return null;
            }
            return new RPGMessages(config.getStringList(configPath[1]),player,this,customScore,section);
        }
    }

    //選択したセクションのメッセージをプレイヤーに送信する。成功したらtrueを、失敗したらfalseを送る
    public boolean showMessagesFromConfig(Player player, String section){
        RPGMessages rpgMessages = getRPGMessagesFromConfig(section,player);
        if(rpgMessages == null){
            return false;
        }
        messageListMap.put(player,rpgMessages);
        showMessages(player);
        return true;
    }

    private List<CustomConfig> getMessageFiles(){
        List<CustomConfig> messageFiles = new ArrayList<>();
        File[] files = getFileFromPlugin("messages").listFiles();
        if(files != null) {
            for (File file : files) {
                if(isYaml(file)){
                    messageFiles.add(new CustomConfig(this,file));
                }
            }
        }
        return messageFiles;
    }

    //プラグインのデータファイルから指定した名前のファイルを取得する
    private File getFileFromPlugin(String fileName){
        return getFile(this.getDataFolder(),fileName);
    }

    //指定した名前のファイルが存在したら返す
    //存在しない場合null
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

    private boolean isYaml(File file){
        try{
            YamlConfiguration.loadConfiguration(file);
            return true;
        }catch (Exception e){
            return false;
        }
    }



    private CustomConfig getMessageConfig(String configName){
        for(CustomConfig config : messageConfigList){
            if(config.getFileName().equals(configName)){
                return config;
            }
        }
        return null;
    }
    private boolean isNextSelection(Player player){
        return messageListMap.containsKey(player) && messageListMap.get(player).isNextSelection();
    }

    public String replaceSymbol(String text, Player player){
        if(text.contains(RPGMessages.REPLACED_SYMBOL_COLOR_CODE)){
            text = text.replace(RPGMessages.REPLACED_SYMBOL_COLOR_CODE,"§");
        }
        if(text.contains(RPGMessages.REPLACED_SYMBOL_PLAYER_NAME)){
            text = text.replace(RPGMessages.REPLACED_SYMBOL_PLAYER_NAME,player.getName());
        }
        return text;
    }

    private boolean isTalking(Player player){
        return hasWaitingText(player) || isContain(player);
    }

    private void resetMessage(Player player){
        if(hasWaitingText(player)){
            waitingTextMap.remove(player);
        }
        RPGTextSender remove = null;
        for(RPGTextSender rpgTextSender : rpgTextSenderList){
            if(rpgTextSender.getPlayer() == player){
                remove = rpgTextSender;
            }
        }
        if(remove != null){
            rpgTextSenderList.remove(remove);
        }
        messageListMap.remove(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        freeze.remove(e.getPlayer());
    }
}