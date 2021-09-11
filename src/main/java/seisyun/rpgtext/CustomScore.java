package seisyun.rpgtext;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CustomScore {
    private CustomConfig customConfig;
    private FileConfiguration config;
    private static final String DEFAULT_SECTION_NAME = "score";

    public CustomScore(Plugin plugin){
        customConfig = new CustomConfig(plugin,"scoreboard.yml");
        config = customConfig.getConfig();
    }

    public boolean contain(String scoreName){
        return config.contains(DEFAULT_SECTION_NAME+"."+scoreName);
    }

    public int get(String scoreName, Player player){
        return config.getInt(DEFAULT_SECTION_NAME+"."+scoreName + "." + player.getName(),0);
    }

    public void set(String scoreName,Player player,int value){
        config.set(DEFAULT_SECTION_NAME+"."+scoreName + "." + player.getName(),value);
        customConfig.saveConfig();
        reload();
    }

    public void resetPlayer(Player player) {
        for (String key : config.getConfigurationSection(DEFAULT_SECTION_NAME).getKeys(false)) {
            config.set(DEFAULT_SECTION_NAME+"."+key+"."+player.getName(),0);
        }
        customConfig.saveConfig();
        reload();
    }

    void reload(){
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }

}
