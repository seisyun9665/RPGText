package seisyun.rpgtext;

import net.minecraft.server.v1_12_R1.MojangsonParseException;
import net.minecraft.server.v1_12_R1.MojangsonParser;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
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

    public CustomConfig(Plugin plugin){
        this(plugin, "config.yml");
    }

    public CustomConfig(Plugin plugin, String fileName,File directory){
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(directory, this.file);
        makeDirectory(directory);
        makeFile(configFile);
    }
    public CustomConfig(Plugin plugin,String fileName){
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
        makeFile(configFile);
    }

    public CustomConfig(Plugin plugin,File configFile){
        this.plugin = plugin;
        this.file = configFile.getName();
        this.configFile = configFile;
        makeFile(configFile);
    }

    public CustomConfig(Plugin plugin,String fileName,File directory,String resource){
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

    public void reloadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if(defConfigStream == null){
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public FileConfiguration getConfig(){
        if(config == null){
            reloadConfig();
        }
        return config;
    }

    public void saveConfig(){
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

    public String getFileName() {
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

    public void setContents(String path, ItemStack[] contents){
        for (int i = 0; i < contents.length; i++) {
            String tmpPath = path + ".slot" + i;
            ItemStack item = contents[i];

            if(item == null) {
                config.set(tmpPath, null);
                continue;
            }

            config.set(tmpPath + ".material", item.getType().toString());
            config.set(tmpPath + ".amount", item.getAmount());
            config.set(tmpPath + ".durability", (int)item.getDurability());
            config.set(tmpPath + ".NBTTag", itemStackToNMSTagString(item));
        }
        config.set(path + ".size", contents.length);
        saveConfig();
        reloadConfig();
    }
    public ItemStack[] getContents(String path){
        int size = config.getInt(path + ".size", 0);
        if(size == 0 || size % 9 != 0) {
            config.set(path + ".size", 27);
            saveConfig();
            reloadConfig();
        }

        ItemStack[] contents = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            String tmpPath = path + ".slot" + i + ".";
            if(config.getString(tmpPath + "material", "").equals("")){
                contents[i] = null;
                continue;
            }

            String material = config.getString(tmpPath + "material", "APPLE");
            int amount = config.getInt(tmpPath + "amount", 1);
            int durability = config.getInt(tmpPath + "durability", 0);
            String tag = config.getString(tmpPath + "NBTTag", "");

            ItemStack item = new ItemStack(Material.getMaterial(material),amount);
            item.setDurability((short)durability);

            if(tag != null && !tag.equals("")){
                item = NMSTagStringToItemStack(item,tag);
            }

            contents[i] = item;
        }

        return contents;
    }
    private static String itemStackToNMSTagString(ItemStack item){
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsStack.getTag();

        if(tag != null){
            return tag.toString();
        }
        return "";
    }
    public void set(String path, ItemStack item){
        if(item == null) {
            config.set(path, null);
            saveConfig();
            reloadConfig();
            return;
        }

        config.set(path + ".material", item.getType().toString());
        config.set(path + ".amount", item.getAmount());
        config.set(path + ".durability", (int)item.getDurability());
        config.set(path + ".NBTTag", itemStackToNMSTagString(item));
        saveConfig();
        reloadConfig();
    }
    public ItemStack getItemStack(String path){
        if(config.getString(path + "material", "").equals("")){ return null; }

        String material = config.getString(path + "material", "APPLE");
        int amount = config.getInt(path + "amount", 1);
        int durability = config.getInt(path + "durability", 0);
        String tag = config.getString(path + "NBTTag", "");

        ItemStack item = new ItemStack(Material.getMaterial(material),amount);
        item.setDurability((short)durability);

        if(tag != null && !tag.equals("")){
            item = NMSTagStringToItemStack(item,tag);
        }
        return item;
    }
    private static ItemStack NMSTagStringToItemStack(ItemStack item, String tag){
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtBase;
        try {
            nbtBase = MojangsonParser.parse(tag);
        } catch (MojangsonParseException e) {
            e.printStackTrace();
            return item;
        }

        nmsItem.setTag(nbtBase);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
