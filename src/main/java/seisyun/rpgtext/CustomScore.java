package seisyun.rpgtext;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CustomScore {
    // プラグイン独自のスコアボードを作成する。
    // プラグインフォルダ内にscoreboard.ymlを作成してプレイヤーごとに変数を設定する
    // 別のクラスCustomConfigが必要
    private CustomConfig customConfig;
    private FileConfiguration config;
    private static final String DEFAULT_SECTION_NAME = "score";

    public CustomScore(Plugin plugin){
        customConfig = new CustomConfig(plugin,"scoreboard.yml");
        config = customConfig.getConfig();
    }

    // スコアが既に存在したらtrue
    public boolean contain(String scoreName){
        return config.contains(DEFAULT_SECTION_NAME+"."+scoreName);
    }

    // プレイヤーの特定スコアを取得する
    public int get(String scoreName, Player player){
        return config.getInt(DEFAULT_SECTION_NAME+"."+player.getUniqueId() + "." + scoreName,0);
    }

    // プレイヤーの特定スコアを設定する
    public void set(String scoreName,Player player,int value){
        config.set(DEFAULT_SECTION_NAME+"."+player.getUniqueId() + "." + scoreName,value);
        customConfig.saveConfig();
        reload();
    }

    // 特定プレイヤーの全てのスコアを0にする
    public void resetPlayer(Player player) {
        config.set(DEFAULT_SECTION_NAME + "." + player.getUniqueId(), null);
    }

    // スコアを読み込みなおす（手動で変更した場合など用）
    public void reload(){
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }
}