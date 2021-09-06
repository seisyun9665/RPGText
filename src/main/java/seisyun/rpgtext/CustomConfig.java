package seisyun.rpgtext;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;

public class CustomConfig {
    private FileConfiguration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    CustomConfig(Plugin plugin){
        this(plugin, "config.yml");
    }

    CustomConfig(Plugin plugin, String fileName,File directory){
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(directory, this.file);
        makeDirectory(directory);
        makeFile(configFile);
    }
    CustomConfig(Plugin plugin,String fileName){
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
        makeFile(configFile);
    }

    CustomConfig(Plugin plugin,File configFile){
        this.plugin = plugin;
        this.file = configFile.getName();
        this.configFile = configFile;
        makeFile(configFile);
    }

    CustomConfig(Plugin plugin,String fileName,File directory,String resource){
        this.plugin = plugin;
        this.file = fileName;
        makeDirectory(directory);
        configFile = new File(directory, this.file);
        // ファイルが既に存在している場合は作成しない
        if(configFile.exists()){
            return;
        }
        try{
            Files.copy(plugin.getResource(resource),configFile.toPath());
            plugin.getLogger().info("Created a \"" +  fileName + "\" file.");
        }catch (IOException e){
            plugin.getLogger().info("Can not make the " + fileName + " from " + resource);
        }
    }

    public void saveDefaultConfig(){
        if(!configFile.exists()){
            plugin.saveResource(file, false);
        }
    }

    void reloadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if(defConfigStream == null){
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    FileConfiguration getConfig(){
        if(config == null){
            reloadConfig();
        }
        return config;
    }

    void saveConfig(){
        if (config == null){
            return;
        }
        try{
            getConfig().save(configFile);
        } catch (IOException ex){
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    private void makeDirectory(File file){
        if (!file.exists()) {
            //フォルダ作成実行
            if (file.mkdir()) {
                //成功
                plugin.getLogger().info("Created a \"" + file.getName() + "\" directory");
            } else {
                //失敗
                plugin.getLogger().info("Could not create \"" + file.getName() + "\" directory");
            }
        }
    }

    private void makeFile(File file){
        try{
            if (!file.exists()) {
                //ファイル作成実行
                if (file.createNewFile()) {
                    //成功
                    plugin.getLogger().info("Created a \"" + file.getName() + "\" file");
                } else {
                    //失敗
                    plugin.getLogger().info("Could not create \"" + file.getName() + "\" file");
                }
            }
        }catch (IOException e){
            plugin.getLogger().info("directory path does not found");
        }
    }

    String getFileName() {
        return file;
    }

}
