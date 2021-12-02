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


    // resourcesフォルダ内にあるymlファイルをコピーして生成する
    CustomConfig(Plugin plugin,String fileName, String resource){
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(),this.file);
        try{
            Files.copy(plugin.getResource(resource),configFile.toPath());
        }catch (IOException e){
            plugin.getLogger().info("Can not make the default file " + resource);
        }
        makeDirectory(plugin.getDataFolder());
        makeFile(configFile);
    }

    CustomConfig(Plugin plugin,String fileName,File directory,String resource){
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(directory, this.file);
        try{
            Files.copy(plugin.getResource(resource),configFile.toPath());
        }catch (IOException e){
            plugin.getLogger().info("Can not make the default file " + resource);
        }
        makeDirectory(directory);
        makeFile(configFile);
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
            }else{
                plugin.getLogger().info("There is already a \"" + file.getName() + "\" file");
            }
        }catch (IOException e){
            plugin.getLogger().info("directory path does not found");
        }
    }

    String getFileName() {
        return file;
    }
    public void setString(String path, String string){
        config.set(path, string);
        saveConfig();
        reloadConfig();
    }

    public void set(String path, Object value){
        config.set(path, value);
        saveConfig();
        reloadConfig();
    }

    public void setInt(String path, int amount){
        config.set(path, amount);
        saveConfig();
        reloadConfig();
    }

    public void setDouble(String path, double amount){
        config.set(path, amount);
        saveConfig();
        reloadConfig();
    }
    public void setBoolean(String path, boolean amount){
        config.set(path, amount);
        saveConfig();
        reloadConfig();
    }


    public String getString(String path, String def){
        return config.getString(path, def);
    }
    public int getInt(String path, int def){
        return config.getInt(path, def);
    }
    public double getDouble(String path, double def){
        return config.getDouble(path, def);
    }
    public boolean getBoolean(String path, boolean def){
        return config.getBoolean(path, def);
    }

}
