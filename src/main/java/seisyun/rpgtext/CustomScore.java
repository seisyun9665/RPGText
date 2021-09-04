package seisyun.rpgtext;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class CustomScore {
    private CustomConfig customConfig;
    private FileConfiguration config;

    CustomScore(Plugin plugin){
        customConfig = new CustomConfig(plugin,"scoreboard.yml");
        config = customConfig.getConfig();
    }

    boolean contain(String scoreName){
        return config.contains(scoreName);
    }

    int get(String scoreName, Player player){
        return config.getInt(scoreName + "." + player.getName(),0);
    }

    void set(String scoreName,Player player,int value){
        config.set(scoreName + "." + player.getName(),value);
        customConfig.saveConfig();
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }

    void reload(){
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }

}
