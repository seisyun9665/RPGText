package seisyun.rpgtext;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Characters {
    private CustomConfig customConfig;
    private FileConfiguration config;
    Characters(Plugin plugin){
        customConfig = new CustomConfig(plugin,"characters.yml");
        config = customConfig.getConfig();
    }

    public void set(String entityName, String path){
        config.set(entityName,path);
        customConfig.saveConfig();
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }

    public void reload(){
        customConfig.reloadConfig();
        config = customConfig.getConfig();
    }

    public boolean contain(String entityName){
        return config.contains(entityName);
    }

    public String get(String entityName){
        return config.getString(entityName);
    }

}
