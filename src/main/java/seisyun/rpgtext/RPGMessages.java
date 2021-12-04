package seisyun.rpgtext;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/* Configから読み込んだ複数のメッセージを管理するクラス
* それぞれのメッセージに対して、特殊シンボルの変換、コマンド実行、次メッセージの呼び出し等を行う */

public class RPGMessages {

    private List<String> messages;                                  // 読み込んだ文章のリスト
    private int sendTextNumber;                                     // messagesのうち今送信する文章の添字
    private String sound = RPGText.DEFAULT_MESSAGE_SOUND;           // 送信時に鳴らす音（RPGTextSenderに設定する）
    private float volume = RPGText.DEFAULT_MESSAGE_VOLUME;
    private float pitch = RPGText.DEFAULT_MESSAGE_PITCH;
    private int speed = RPGText.DEFAULT_MESSAGE_SPEED;              // 送信速度
    private String color = RPGText.DEFAULT_MESSAGE_COLOR;              // 文字の色
    private String section;                                         // プレイヤーが選んだ選択肢
    private Player player;                                          // 送信するプレイヤー
    private Selections selections = null;                           // 現在表示されている選択肢
    static final String REPLACED_SYMBOL_COLOR_CODE = "\\";          // カラーコードに変換される特殊シンボル
    static final String REPLACED_SYMBOL_PLAYER_NAME = "%player%";   // プレイヤー名に変換される特殊シンボル
    static final String REPLACED_SYMBOL_LEVEL = "%level%";          // プレイヤーのレベルに変換される特殊シンボル
    static final String REPLACED_SYMBOL_HP = "%hp%";                // 体力に変換される特殊シンボル
    static final String REPLACED_SYMBOL_FOOD = "%food%";            // スタミナレベルに変換される特殊シンボル
    static final String REPLACED_SYMBOL_GAMEMODE = "%gamemode%";    // ゲームモード（数字）に変換される特殊シンボル
    static final String REPLACED_SYMBOL_SCORE = "§§";               // スコアの数字に変換される特殊シンボル
    static final String REPLACED_SYMBOL_SELECTION = "?";            // 直前に選んだ選択肢の内容に変換される特殊シンボル
    private static RPGText plugin;
    private String selection = " ";                                 // 直前に選んだ選択肢の名前
    private boolean jump = false;                                   // コマンドでジャンプが起こったかどうか
    private boolean skip = true;                                    // trueで会話を最後まで飛ばせる
    private boolean auto = false;                                   // trueで会話自動進行
    private static CustomScore customScore;

    private static final String LIST_CONFIG_ROOT = "lists";

    RPGMessages(List<String> messages,Player player,RPGText plugin,CustomScore customScore,String section){
        this(messages,player, 0, plugin,customScore,section);
    }

    RPGMessages(List<String> messages,Player player, int sendTextNumber,RPGText plugin,CustomScore customScore,String section){
        this.sendTextNumber = sendTextNumber;
        this.player = player;
        RPGMessages.plugin = plugin;
        RPGMessages.customScore = customScore;
        this.messages = replaceSymbol(messages);
        this.section = section;
    }

    void setSpeed(int speed) {
        this.speed = speed;
    }

    int getSpeed() {
        return speed;
    }

    public void setSkip(boolean skip) { this.skip = skip; }

    public boolean canSkip() { return skip; }

    public void setAuto(boolean auto) { this.auto = auto; }
    public boolean isAuto() { return auto; }

    // 次に表示するメッセージを取得する。コマンド等も実行する
    String getMessage() {
        // メッセージnullもしくは空なら終わり
        if(messages == null || messages.isEmpty()) {
            return "";
        }
        // すべてのメッセージが送信済みだったら終わり
        else {
            if(sendTextNumber >= messages.size()){
                return "";
            }
        }


        /* コマンド処理 */

        while (isCommand(messages.get(sendTextNumber))){
            // \\スコア名\\となっているスコアを数字に置き換える
            messages.set(sendTextNumber,replaceScore(messages.get(sendTextNumber)));

            /* /jump */
            // "/jump 飛ぶ場所"
            // 例：Tutorial.ymlの中のusersを実行する "/jump Tutorial.yml/users"
            // 例：bobフォルダの中のbobTalk.ymlの中のtalk1を実行する "bob/bobTalk.yml/talk1"
            if(messages.get(sendTextNumber).startsWith("/jump ") && messages.get(sendTextNumber).length() > 5){
                //他のメッセージのところに飛ぶ
                jump = true;
                // /jump を消去
                String section = messages.get(sendTextNumber).replace("/jump ","");

                // ジャンプのファイル名を省略していた場合に先頭に追加する
                // 仮に今読み込んでるファイルが Tutorial.yml だったとして /jump users2 と書かれていたら Tutorial.yml/users2 にする
                if(!section.contains(".yml/") && this.section.contains(".yml/")){
                    section = this.section.split(".yml")[0] + ".yml/" + section;
                }
                return section;
            }
            /* /jump終わり */

            /* /? */
            else if(messages.get(sendTextNumber).startsWith("/?" + selection + " ")){
                messages.set(sendTextNumber,messages.get(sendTextNumber).substring(selection.length() + 3));
                continue;
            }
            /* /? 終わり */

            // 条件
            else if(messages.get(sendTextNumber).startsWith("/if ")){
                String ret = branch(messages.get(sendTextNumber));
                // & 記号で条件文繋いでる場合の処理
                while(ret.startsWith("&")){
                    ret = branch(ret);
                }
                if(ret.equals("")){
                    sendTextNumber++;
                    if(sendTextNumber >= messages.size()){
                        return "";
                    }
                }else{
                    messages.set(sendTextNumber,ret);
                }
                continue;
            }
            // アイテム所持
            else if(messages.get(sendTextNumber).startsWith("/has ")){
                String nextCommand = judgeHasItem(messages.get(sendTextNumber));
                if(!nextCommand.equals("")) {
                    messages.set(sendTextNumber, nextCommand);
                }else{
                    sendTextNumber++;
                    if(sendTextNumber >= messages.size()){
                        return "";
                    }
                }
                continue;
            }
            // ディレイ
            else if(messages.get(sendTextNumber).startsWith("/wait ")){
                String message = messages.get(sendTextNumber);
                sendTextNumber++;
                return message;
            }

            // その他のコマンド（sound,speed等）
            determineCommand(messages.get(sendTextNumber));


            sendTextNumber++;
            if(isSelecting()){
                return "/?";
            }
            if(this.isFinished()){
                return "";
            }

            /* コマンド処理終わり */

        }
        messages.set(sendTextNumber,replaceScore(messages.get(sendTextNumber)));
        sendTextNumber++;
        return color + messages.get(sendTextNumber - 1);
    }

    // 送信し終えているか
    boolean isFinished(){
        if(messages == null || messages.isEmpty()){
            return true;
        }
        return sendTextNumber >= messages.size();
    }

    // 別のメッセージを読み込んでいるか
    boolean isJumping(){
        return jump;
    }

    /* セッターゲッター */
    public void setSound(String sound) {
        this.sound = sound;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
    public void setColor(String color){
        this.color = color.replace(REPLACED_SYMBOL_COLOR_CODE,"§");
    }

    String getSound() {
        return sound;
    }
    float getPitch() {
        return pitch;
    }
    float getVolume() {
        return volume;
    }
    /* セッターゲッター終わり */


    // テキストがコマンドならtrue。先頭が"/"だった場合コマンドとみなす
    private boolean isCommand(String text){
        return text.length() > 1 && text.startsWith("/");
    }

    // コマンドを判別して実行する
    private void determineCommand(String text){
        // 引数 ex: /speed 2 -> {"/speed", "2"}   size() == 2
        List<String> args = new ArrayList<>(Arrays.asList(text.split(" ")));

        // "/sound 音" または "/sound 音 ボリューム ピッチ"
        // 例 "/sound block.note.bass" "/sound block.note.bass 1 1.4"
        if(text.startsWith("/sound")){
            //音を設定

            if(args.size() == 2){
                this.sound = args.get(1);
            }else if(args.size() == 4){
                this.sound = args.get(1);
                if(isFloat(args.get(2))){
                    this.volume = Float.parseFloat(args.get(2));
                }
                if(isFloat(args.get(3))){
                    this.pitch = Float.parseFloat(args.get(3));
                }
            }
        }

        // "/speed 数字"
        // 例： １秒間に送る文字数を40に設定する "/speed 40"
        else if(text.startsWith("/speed ")){
            //速度を設定
            if(args.size() == 2 && isInteger(args.get(1))){
                this.speed = Integer.parseInt(args.get(1));
            }
        }

        // "/color カラーコード"
        // 例： 文章を緑の太字にする "/color \a\l"
        else if(text.startsWith("/color ")){
            // 基本の文字色を変更する
            if(args.size() == 2){
                this.color = args.get(1);
            }
        }

        // "/? 選択肢1 選択肢2 選択肢3…"
        // 例：1から3までで好きな数字を選ばせる "/? 1 2 3"
        else if(text.startsWith("/? ")){
            //選択肢
            if(args.size() > 1){
                List<String> selectionList = new ArrayList<>();
                for(String arg : args){
                    if(!arg.equals("/?")){
                        selectionList.add(arg);
                    }
                }
                Selections selections = new Selections(player,selectionList);
                selections.showSelections();
                this.selections = selections;
            }
        }

        // "/command コマンド"
        // 例： プレイヤーにリンゴを一つ与える "/command give %player% apple"
        else if(text.startsWith("/command ")){
            //コマンドを実行
            if(args.size() > 1){
                String command = text.substring(9);
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),command);
            }
        }

        // "/score スコア名 <値|スコア名>" "/score スコア名 <random|+|-|*|/|%> <値|スコア名>"
        // 例： スコア「number」を10にする "/score number 10"
        // 例： スコア「number」を「number2」と同じにする "/score number number2"
        // 例： スコア「number」を二倍にする "/score number * 2"
        // 例： スコア「number」を0から10のどれかにする "/score number random 11"
        else if(text.startsWith("/score ")){
            //スコアを設定
            if(args.size() > 2){
                if(args.size() > 3){
                    /* 値を取得する */
                    // ３番目の引数を取得。スコア名ならcuntomscoreから取得してくる。数字ならそのまま適用
                    int number = 0;
                    if(isInteger(args.get(3))){                     // 数字なのでそのまま適用
                        number = Integer.parseInt(args.get(3));
                    }else{                                          // customscoreから取得
                        if(customScore.contain(args.get(3),player)) {
                           number = customScore.get(args.get(3), player);
                        }
                    }

                    //１番目の引数のスコア
                    int score1 = 0;
                    if(customScore.contain(args.get(1), player)){
                        score1 = customScore.get(args.get(1),player);
                    }

                    /* 比較演算子別に操作する */
                    switch (args.get(2)) {
                        case "random":
                            customScore.set(args.get(1), player, new Random().nextInt(number));
                            break;
                        case "+":
                            customScore.set(args.get(1), player, score1 + number);
                            break;
                        case "-":
                            customScore.set(args.get(1), player, score1 - number);
                            break;
                        case "*":
                            customScore.set(args.get(1), player, score1 * number);
                            break;
                        case "/":
                            customScore.set(args.get(1), player, score1 / number);
                            break;
                        case "%":
                            customScore.set(args.get(1), player, score1 % number);
                            break;
                    }
                }

                // 引数が2つしか無いパターン   例："/socre number 2"
                else if(isInteger(args.get(2))){
                    customScore.set(args.get(1),player,Integer.parseInt(args.get(2)));
                }
            }
        }

        // "/list リスト名 <set | add | remove | replace | clear> [要素] [要素（replace)]"
        // \\リスト名[文字列]\\                :    全要素出力（区切り文字が括弧内文字列になる）
        // \\リスト名[要素番号]\\              :    要素(ない場合は空白)　
        // \\リスト名.size()\\                :  要素数
        // \\リスト名.contain(要素)           :   yes->1 no->0
        // /list list1 set a,b,c            :   \\list[,]\\ -> a,b,c
        // /list list1[0] set b             :   \\list[,]\\ -> b,b,c
        // /list list1[0] set a             :   \\list[,]\\ -> a,b,c
        // /list list1 add d                :   \\list[,]\\ -> a,b,c,d
        // /list list1[1] add e             :   \\list[,]\\ -> a,e,b,c,d
        // /list list1 remove               :   \\list[,]\\ -> a,e,b,c
        // /list list1[1] remove            :   \\list[,]\\ -> a,b,c
        // /list list1 remove a             :   \\list[,]\\ -> b,c
        // /list list1 add ad               :   \\list[,]\\ -> b,c,ad
        // /list list1[2] remove a          :   \\list[,]\\ -> b,c,d
        // /list list1 replace c d          :   \\list[,]\\ -> b,d,d
        // /list list1 replace d ab         :   \\list[,]\\ -> b,ab,ab
        // /list list1[1] replace a d       :   \\list[,]\\ -> b,db,ab
        // /list list1 clear
        else if(text.startsWith("/list ")){
            // リストを操作
            if(args.size() > 2){
                boolean assigned = isAssigning(args.get(1));
                String listName = getListName(args.get(1));
                List<String> list = getList(listName, player);

                String command = args.get(2);

                // 添字取得
                int index = -1;
                if(assigned) {
                    index = getIndex(args.get(1));
                    // 添字がリストより大きい OR　0未満でも-1にする
                    if(index >= list.size() || index < -1){
                        index = -1;
                    }
                }

                // 添字なし　OR　添字正しい
                if(!assigned || index != -1) {
                    String element = "", element2 = "";
                    if(args.size() > 3) element = args.get(3);
                    if(args.size() > 4) element2 = args.get(4);
                    switch (args.size()) {
                        case 3:
                            switch (command) {
                                case "clear":
                                    list.clear();
                                    setList(listName, list, player);
                                    break;
                                case "remove":
                                    if (list.size() > 0) {
                                        if(assigned){
                                            list.remove(index);
                                        }
                                        else {
                                            list.remove(list.size() - 1);
                                        }
                                        setList(listName, list, player);
                                    }
                                    break;
                            }
                            break;
                        case 4:
                            switch (command) {
                                case "set":
                                    if (assigned) {
                                        list.set(index,element);
                                        setList(listName,list, player);
                                    }
                                    else {
                                        if(element.contains(",")){
                                            setList(listName,element.split(","), player);
                                        }
                                    }
                                    break;
                                case "add":
                                    if(assigned){
                                        list.add(index,element);
                                    }
                                    else {
                                        list.add(element);
                                    }
                                    setList(listName, list, player);
                                    break;
                                case "remove":
                                    if(assigned) {
                                        String tmp = list.get(index).replace(element,"");
                                        if(tmp.equals("")){
                                            list.remove(index);
                                        }else{
                                            list.set(index, tmp);
                                        }
                                    }
                                    else {
                                        list.remove(element);
                                    }
                                    setList(listName, list, player);
                            }
                            break;
                        case 5:
                            if ("replace".equals(command)) {
                                if (assigned) {
                                    list.set(index, list.get(index).replace(element, element2));
                                } else {
                                    String finalElement = element;
                                    String finalElement1 = element2;
                                    list.replaceAll(str -> str.replace(finalElement, finalElement1));
                                }
                                list.remove("");
                                setList(listName, list, player);
                            }
                            break;
                    }
                }
            }
        }

        // "/add <スコア名>"
        // 例： numberを+1 "/add number"
        else if(text.startsWith("/add ")){
            //スコアを１増加
            if(args.size() == 2){
                customScore.set(args.get(1),player,customScore.get(args.get(1),player) + 1);
            }
        }

        // "/removeItem アイテムの種類（またはアイテム番号） [個数] [名前]" ※[]は任意
        // 例：リンゴを１個消す "/removeItem apple"
        // 例：石を5個消す "/removeItem 1 5"
        // 例：「強化ガラス」という名前のガラスを１０個消す "/removeItem 20 10 強化ガラス"
        else if(text.startsWith("/removeItem ")){
            /* アイテムを削除 */
            // 例外処理
            if(args.size() < 2) return;

            // 引数に対応して要素を増やす
            int amount = 1;
            String itemName = "";
            String item = args.get(1);  // アイテムの種類は必ず必要

            // 引数２：アイテムの個数
            if(args.size() >= 3 && isInteger(args.get(2))){
                amount = Integer.parseInt(args.get(2));
            }
            // 引数３：アイテムの名前
            if(args.size() >= 4){
                itemName = args.get(3);
            }

            removeItem(item,amount,itemName);
        }

        // "/singlesound 音" または "/singlesound 音 ボリューム ピッチ"
        // 例 "/singlesound block.note.bass" "/singlesound block.note.bass 1 1.4"
        else if(text.startsWith("/singlesound ")){
            //音を鳴らす
            if(args.size() == 2){
                player.playSound(player.getLocation(),args.get(1),1,1);
            }else if(args.size() == 4) {
                if (isFloat(args.get(2)) && isFloat(args.get(3))) {
                    player.playSound(player.getLocation(), args.get(1), Float.parseFloat(args.get(2)),Float.parseFloat(args.get(3)));
                }else{
                    player.playSound(player.getLocation(),args.get(1),1,1);
                }
            }
        }

        // "/freeze <true|false>" 例 "/freeze true"
        else if(text.startsWith("/freeze ")){
            // プレイヤーの停止を設定する
            /* 例外処理 */
            if(args.size() != 2) return;
            /* 例外処理終わり */

            // argをもとにtrue,falseを決める
            String arg = args.get(1);
            if(arg.equals("true")){
                RPGText.setFreeze(player, true);
            }else if(arg.equals("false")){
                RPGText.setFreeze(player, false);
            }
        }

        // falseにするとスキップできなくなる "/skip <true|false>" 例： "/skip false"
        else if(text.startsWith("/skip ")){
            // 途中でスキップ可能か設定する
            /* 例外処理 */
            if(args.size() != 2) return;
            /* 例外処理終わり */

            // argをもとにtrue,falseを決める
            String arg = args.get(1);
            if(arg.equals("true")){
                setSkip(true);
            }else if(arg.equals("false")){
                setSkip(false);
            }
        }

        // "/auto <true|false>" trueにすると自動で進む。デフォルトはfalse
        else if(text.startsWith("/auto ")){
            // 途中でスキップ可能か設定する
            /* 例外処理 */
            if(args.size() != 2) return;
            /* 例外処理終わり */

            // argをもとにtrue,falseを決める
            String arg = args.get(1);
            if(arg.equals("true")){
                setAuto(true);
            }else if(arg.equals("false")){
                setAuto(false);
            }
        }

    }

    void showSelection(){
        reloadSelection();
        selections.showSelections();
    }

    private void reloadSelection(){
        if(selections == null){
            if(sendTextNumber == 0){
                determineCommand(messages.get(sendTextNumber));
            }else {
                determineCommand(messages.get(sendTextNumber - 1));
            }
        }
    }

    void increaseMessageNumber(){
        if(sendTextNumber < messages.size() - 1){
            sendTextNumber++;
        }
    }

    void finishSelection(){
        if(selections != null){
            selections.finishSelection();
            selections = null;
        }
    }

    void selectLeft(){
        if(selections != null){
            selections.moveCursorLeft();
        }
    }
    void selectRight(){
        if(selections != null){
            selections.moveCursorRight();
        }
    }

    private String getSelection(){
        reloadSelection();
        if(selections != null){
            return selections.getSelection();
        }
        return "";
    }

    boolean isSelecting(){
        if(messages.size() > 0) {
            if(sendTextNumber > 0) {
                return messages.get(sendTextNumber - 1).startsWith("/? ");
            }else{
                return messages.get(sendTextNumber).startsWith("/? ");
            }
        }
        return false;
    }

    // 一文目に選択コマンドした時の例外処理
    boolean isFirstSelection(){
        return sendTextNumber == 0 && messages.get(sendTextNumber).startsWith("/? ");
    }

    boolean isNextSelection(){
        if(sendTextNumber < messages.size()) {
            return messages.get(sendTextNumber).startsWith("/? ");
        }
        return false;
    }

    private boolean isFloat(String string){
        try{
            Float.parseFloat(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    private boolean isInteger(String string){
        try{
            Integer.parseInt(string);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    private List<String> replaceSymbol(List<String> list){
        List<String> newList = new ArrayList<>();
        for(String string : list){
            if(string.contains(RPGMessages.REPLACED_SYMBOL_COLOR_CODE))                 string = string.replace(RPGMessages.REPLACED_SYMBOL_COLOR_CODE, "§");
            if(string.contains(RPGMessages.REPLACED_SYMBOL_PLAYER_NAME))                string = string.replace(RPGMessages.REPLACED_SYMBOL_PLAYER_NAME, player.getName());
            if(string.contains(RPGMessages.REPLACED_SYMBOL_LEVEL))          string = string.replace(RPGMessages.REPLACED_SYMBOL_LEVEL, player.getLevel() + "");
            if(string.contains(RPGMessages.REPLACED_SYMBOL_FOOD))           string = string.replace(RPGMessages.REPLACED_SYMBOL_FOOD, player.getFoodLevel() + "");
            if(string.contains(RPGMessages.REPLACED_SYMBOL_HP))             string = string.replace(RPGMessages.REPLACED_SYMBOL_HP, (int)player.getHealth() + "");
            if(string.contains(RPGMessages.REPLACED_SYMBOL_GAMEMODE))       string = string.replace(RPGMessages.REPLACED_SYMBOL_GAMEMODE, player.getGameMode().getValue() + "");
            newList.add(string);
        }
        return newList;
    }

    void decisionSelection(){
        this.selection = getSelection();
    }

    private String branch(String text){
        List<String> args = new ArrayList<>(Arrays.asList(text.split(" ")));
        if(args.size() > 3) {
            String nextCommand = text.substring(args.get(0).length() + 4 + args.get(1).length() + args.get(2).length() + args.get(3).length());
            //条件分岐
            String arg1 = args.get(1);
            String arg2 = args.get(3);
            int var1;
            int var2;
            if(isInteger(arg1)) {
                var1 = Integer.parseInt(arg1);
            }else if(customScore.contain(arg1, player)){
                var1 = customScore.get(arg1, player);
            }else{
                return "";
            }
            if(isInteger(arg2)) {
                var2 = Integer.parseInt(arg2);
            }else if(customScore.contain(arg2, player)){
                var2 = customScore.get(arg2, player);
            }else{
                return "";
            }
            switch (args.get(2)) {
                case "=":
                    if (var1 == var2) {
                        return nextCommand;
                    }
                    break;
                case ">":
                    if (var1 > var2) {
                        return nextCommand;

                    }
                    break;
                case "<":
                    if (var1 < var2) {
                        return nextCommand;
                    }
                    break;
                default:
                    return "";
            }
        }
        return "";
    }

    private String judgeHasItem(String text){
        if(text.contains(" ")){
            String[] args = text.split(" ");
            if(args.length > 3 && isInteger(args[2])){
                if(hasItem(args[1],Integer.parseInt(args[2]),args[3])){
                    return text.substring(8 + args[1].length() + args[2].length() + args[3].length());
                }
            }
        }
        return "";
    }

    private boolean hasItem(String item,int amount,String itemName){
        Material material;
        int durability = 0;
        if(item.contains(":")){
            String[] itemDates = item.split(":");
            if(isInteger(itemDates[0])){
                material = Material.getMaterial(Integer.parseInt(itemDates[0]));
            }else{
                material = Material.getMaterial(itemDates[0]);
            }
            if(material == null){
                return false;
            }
            if(isInteger(itemDates[1])){
                durability = Integer.parseInt(itemDates[1]);
            }
        }else{
            if(isInteger(item)){
                material = Material.getMaterial(Integer.parseInt(item));
            }else{
                material = Material.getMaterial(item);
            }
            if(material == null){
                return false;
            }
        }
        ItemStack itemStack = new ItemStack(material);
        if(durability != 0){
            itemStack.setDurability((short) durability);
        }
        if(itemName.equalsIgnoreCase("none")){
            return player.getInventory().containsAtLeast(itemStack,amount);
        }else{
            return hasItem(player, itemName, amount, material,(short)durability);
        }
    }

    //アイテム削除
    private void removeItem(String item,int amount,String itemName){
        Material material;
        int durability = 0;
        if(item.contains(":")){
            String[] itemDates = item.split(":");
            if(isInteger(itemDates[0])){
                material = Material.getMaterial(Integer.parseInt(itemDates[0]));
            }else{
                material = Material.getMaterial(itemDates[0]);
            }
            if(material == null){
                return;
            }
            if(isInteger(itemDates[1])){
                durability = Integer.parseInt(itemDates[1]);
            }
        }else{
            if(isInteger(item)){
                material = Material.getMaterial(Integer.parseInt(item));
            }else{
                material = Material.getMaterial(item);
            }
            if(material == null){
                return;
            }
        }
        ItemStack itemStack = new ItemStack(material);
        if(durability != 0){
            itemStack.setDurability((short) durability);
        }
        if(itemName.equalsIgnoreCase("none")){
            if(player.getInventory().containsAtLeast(itemStack,amount)){
                itemStack.setAmount(amount);
                player.getInventory().removeItem(itemStack);
            }
        }else{
            if(hasItem(player,itemName,amount,material,(short)durability)){
                removeItem(player,itemName,amount,material,(short) durability);
            }
        }
    }

    private boolean hasItem(Player player,String itemName,int amount,Material itemType){
        int itemNumber = 0;
        for(ItemStack item :player.getInventory().getContents()){
            if(item != null && itemType == item.getType() && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(itemName)){
                itemNumber += item.getAmount();
            }
        }
        return itemNumber >= amount;
    }
    private boolean hasItem(Player player,String itemName,int amount,Material itemType,short durability){
        int itemNumber = 0;
        for(ItemStack item :player.getInventory().getContents()){
            if(item != null && itemType == item.getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(itemName) && item.getDurability() == durability){
                itemNumber += item.getAmount();
            }
        }
        return itemNumber >= amount;
    }
    private void removeItem(Player player,String itemName,int amount,Material itemType,short durability){
        ItemStack[] contents = player.getInventory().getContents();
        for(int i = 0;i < contents.length;i++){
            ItemStack item = player.getInventory().getContents()[i];
            if(item != null && itemType == item.getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(itemName) && item.getDurability() == durability){
                if (amount == item.getAmount()){
                    contents[i].setAmount(0);
                    contents[i].setType(Material.AIR);
                    break;
                }else if(amount > item.getAmount()){
                    amount -= item.getAmount();
                    contents[i].setAmount(0);
                    contents[i].setType(Material.AIR);
                }else {
                    contents[i].setAmount(item.getAmount() - amount);
                    break;
                }
            }
        }
        player.getInventory().setContents(contents);
        player.updateInventory();
    }

    private String replaceScore(String string){
        if(string.contains(REPLACED_SYMBOL_SCORE)){
            // "§§string§§"の形をsplitすると [" ", "string"] になってしまう（最後尾の§§の分割結果が無視される）ので、最後尾が§§の場合にスペースを付け足して分割数が偶数にならないようにする
            if(string.endsWith(REPLACED_SYMBOL_SCORE)){
                string = string + " ";
            }
            String[] args = string.split(REPLACED_SYMBOL_SCORE);
            //split with §§
            if(args.length > 2 && args.length % 2 == 1){    //§§で分割した時に、ちゃんと「§§スコア名§§」と入力されていれば分割数は奇数になる（普通は§§を二つ使ってスコアを挟むため２つで１組になる）。§§の個数が奇数の場合分割数が偶数になる。
                // odd = 偶数
                // even = 奇数
                // args size = odd
                StringBuilder output = new StringBuilder();
                for(int i = 0;i < args.length;i++){
                    if(i % 2 == 0){
                        // odd
                        output.append(args[i]);
                    }else{
                        // even
                        // 各種スコア置き換え処理

                        // 選択肢直前の選択肢
                        if(args[i].equals("?")){
                            output.append(selection);
                        }

                        // list
                        else if(isList(args[i])){
                            String listName = args[i];
                            List<String> list = getList(getListName(listName),player);
                            // サイズ指定
                            if(listName.endsWith(".size()")){
                                output.append(list.size());
                            }
                            // contain
                            else if(listName.contains(".contain(") && listName.endsWith(")") && listName.split(Pattern.quote(".contain(")).length==2) {
                                // contain() のかっこ内に含まれている文字列がリストに存在
                                // -> 1  存在しない -> 0
                                String tmp = listName.split(Pattern.quote(".contain("))[1];
                                output.append(list.contains(tmp.substring(0,tmp.length() - 1)) ? 1 : 0);
                            }
                            // インデックス指定
                            else if(isAssigning(listName)){
                                int index = getIndex(listName);

                                if(index < 0 || index >= list.size()){
                                    // インデックスが不正の場合
                                    String splitIndex = getIndexString(listName);
                                    // 全要素出力
                                    list.forEach(element -> output.append(element).append(splitIndex));
                                    // 最後のはみ出した部分を削除
                                    output.delete(output.length() - splitIndex.length(),output.length());
                                }
                                else {
                                    // インデックスが正しい場合
                                    output.append(list.get(index));
                                }
                            }
                        }

                        // CustomScore
                        else if(customScore.contain(args[i], player)){
                            output.append(customScore.get(args[i],player));
                        }else{
                            output.append(0);
                        }
                    }
                }
                String ret = output.toString();
                //文字列の先頭もしくは末尾がsplitの影響で空白になっている場合の処理
                if(ret.startsWith(" ")) ret = ret.substring(1);
                if(ret.endsWith(" ")) ret = ret.substring(0, ret.length() - 1);
                return ret;
            }
        }
        return string;
    }

    // 文字列がリストの要素を指定しているか判定
    private static boolean isAssigning(String list){
        return list.contains("[") && list.endsWith("]") && !list.startsWith("[");
    }
    // リスト名取得
    private String getListName(String list){
        // リストが要素指定してる場合
        if(isAssigning(list)){
            // [ 以降を空白で置き換え
            String[] args = list.split(Pattern.quote("["));
            return list.replace("[" + args[args.length - 1], "");
        }
        // そうでない場合はそのまま
        return list;
    }
    // 取得
    private static List<String> getList(String list, Player player){
        return RPGText.listConfig.getConfig().getStringList(LIST_CONFIG_ROOT + "." + player.getUniqueId() + "." + list);
    }
    private static void setList(String list, List<String> stringList, Player player){
        RPGText.listConfig.set(LIST_CONFIG_ROOT + "." + player.getUniqueId() + "." +  list, stringList);
    }
    private static void setList(String list, String[] stringList, Player player){
        RPGText.listConfig.set(LIST_CONFIG_ROOT + "." +  player.getUniqueId() + "." + list, Arrays.asList(stringList));
    }
    // not int = -1
    private static int getIndex(String list){
        if(!isAssigning(list)) return -1;

        String[] stringList = list.split(Pattern.quote("["));
        String num = stringList[stringList.length - 1];
        num = num.substring(0,num.length() - 1);

        return plugin.isInteger(num) ? Integer.parseInt(num) : -1;
    }
    private static String getIndexString(String list){
        if(!isAssigning(list)) return "";

        String[] stringList = list.split(Pattern.quote("["));
        String str = stringList[stringList.length - 1];
        str = str.substring(0,str.length() - 1);

        return str;
    }
    // リスト指定判定
    private static boolean isList(String list){
        return isAssigning(list) || list.endsWith(".size()") || (list.contains(".contain(") && list.endsWith(")") && list.split(Pattern.quote(".contain(")).length==2);
    }
    // リストをリセットする
    public static void resetList(Player player){
        RPGText.listConfig.set(LIST_CONFIG_ROOT + "." + player.getUniqueId(), null);
    }
}
