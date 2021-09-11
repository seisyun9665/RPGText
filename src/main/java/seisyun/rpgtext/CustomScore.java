package seisyun.rpgtext;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CustomScore {
    private CustomConfig customConfig;
    private FileConfiguration config;

    public CustomScore(Plugin plugin){
        customConfig = new CustomConfig(plugin,"scoreboard.yml");
        config = customConfig.getConfig();
    }

    public boolean contain(String scoreName){
        return config.contains(scoreName);
    }

    public int get(String scoreName, Player player){
        return config.getInt(scoreName + "." + player.getName(),0);
    }

    public void set(String scoreName,Player player,int value){
        config.set(scoreName + "." + player.getName(),value);
        customConfig.saveConfig();
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }

    public void resetPlayer(Player player){
        config.getDefaultSection().getKeys(true).forEach( path -> config.set(path, 0));
    }

    void reload(){
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }

}
