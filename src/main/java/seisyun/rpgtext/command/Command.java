package seisyun.rpgtext.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import seisyun.rpgtext.Characters;
import seisyun.rpgtext.Freeze;
import seisyun.rpgtext.RPGMessages;
import seisyun.rpgtext.RPGText;

public class Command implements CommandExecutor {
    private static RPGText plugin;
    private static Freeze freeze;
    private static Characters characters;

    public Command(RPGText plugin, Freeze freeze,Characters characters){
        Command.plugin = plugin;
        Command.freeze = freeze;
        Command.characters = characters;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        /* /help */
        if(args.length > 0 && args[0].equals("help")){
            sender.sendMessage("--- RPGText commands ---");
            sender.sendMessage("/rpgtext reload : Reload configs.");
            sender.sendMessage("/rpgtext <text|config> <player> <path> : Send messages in the style an RPG game.");
            sender.sendMessage("/rpgtext <text> <player> <sound> <volume> <pitch> <speed> :  Send messages with custom sound.");
            sender.sendMessage("/rpgtext character <name> <path> : Set the name of the entity that will send a message to the clicked player and set config path used to send messages.");
            sender.sendMessage("example: /rpgtext character Bob Tutorial.yml/users");
            sender.sendMessage("/rpgtext freeze clear : Allows all frozen players to move.");
            sender.sendMessage("/rpgtext freeze toggle <player> : Switch the player's frozen state.");
            sender.sendMessage("/rpgtext reset <player> : Set the player's all score to 0.");
            sender.sendMessage("/rpgtext set <player> <score name> <score> : Set the player's score.");
            sender.sendMessage("/rpgtext list reset <player> : reset the player's list data.");

            if(sender instanceof Player){
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),"rpgtext config " + sender.getName() + " Tutorial.yml/users");
            }
            return true;
        }

        /* /freeze */
        if(args.length > 0 && args[0].equals("freeze")){
            if(args.length > 1){
                if(args[1].equals("clear")){
                    freeze.clear();
                    plugin.getServer().broadcastMessage("§7[Server]: All frozen players be able to move.");
                    return true;
                }else if(args.length > 2 && args[1].equals("toggle")){
                    Player player = plugin.getServer().getPlayer(args[2]);
                    if(player != null){
                        boolean isFrozen = freeze.toggle(player);
                        if(isFrozen){
                            player.sendMessage("§7[Server]: You ware frozen by the Admin commands.");
                            sender.sendMessage("Freezing the " + player.getName() + " was successful.");
                        }else {
                            player.sendMessage("§7[Server]: You ware unfrozen by the Admin commands.");
                            sender.sendMessage("Unfreezing the " + player.getName() + " was successful.");
                        }
                        return true;
                    }else{
                        sender.sendMessage("Not found the player \"" + args[2] + "\"");
                        return false;
                    }
                }
            }
        }

        /* /character */
        if(args.length > 0 && args[0].equals("character")){
            if(args.length != 3){
                sender.sendMessage("/rpgtext character <name> <path>");
                return false;
            }
            if (characters.contain(args[1])) {
                sender.sendMessage("Changed character's path from " + characters.get(args[1]) +" to " + args[2]);
            }
            else {
                sender.sendMessage("New character " + args[1] + " set with path \"" + args[2] + "\"");
            }
            characters.set(args[1],args[2]);
            return true;
        }

        /* /reload */
        if(args.length == 1 && args[0].equals("reload")){
            plugin.reloadAllConfig();
            sender.sendMessage("config reloaded!");
            return true;
        }

        /* /reset */
        if(args.length == 2&& args[0].equals("reset")){
            Player player = plugin.getServer().getPlayer(args[1]);
            if(player == null) {
                sender.sendMessage("Player " + args[1] + " does not exist.");
                return true;
            }
            plugin.resetScore(player);
            sender.sendMessage("reset " + args[1] + "'s score.");
            return true;
        }
        /* /set */
        if(args.length == 4 && args[0].equals("set")){
            Player player = plugin.getServer().getPlayer(args[1]);
            if(player == null) {
                sender.sendMessage("Player " + args[1] + " does not exist.");
                return true;
            }
            if(!plugin.isInteger(args[3])){
                sender.sendMessage(args[3] + " is not a score.");
            }

            plugin.setScore(player, args[2], Integer.parseInt(args[3]));
            sender.sendMessage("set " + args[1] + "'s score \"" + args[2] + "\" to " + args[3] + ".");
            return true;
        }
        /* list reset */
        if(args.length == 3 && args[0].equals("list")){
            if(!args[1].equals("reset")) return false;
            Player player = plugin.getServer().getPlayer(args[2]);
            if(player == null) {
                sender.sendMessage("Player " + args[2] + " does not exist.");
                return true;
            }

            RPGMessages.resetList(player);
            sender.sendMessage("reset the " + args[2] + "'s list data.");
            return true;
        }
        if(!(args.length == 3 || args.length == 6)){
            sender.sendMessage(ChatColor.RED + "/rpgtext reload : Reload configs.");
            sender.sendMessage(ChatColor.RED + "/rpgtext <text|config> <player> <path> : Send messages in the style an RPG game.");
            sender.sendMessage(ChatColor.RED + "/rpgtext <text> <player> <sound> <volume> <pitch> <speed> :  Send messages with custom sound.");
            sender.sendMessage(ChatColor.RED + "/rpgtext character <name> <path> : Set the name of the entity that will send a message to the clicked player and set config path used to send messages.");
            sender.sendMessage(ChatColor.RED + "example: /rpgtext character Bob Tutorial.yml/users");
            sender.sendMessage(ChatColor.RED + "/rpgtext freeze clear : Allows all frozen players to move.");
            sender.sendMessage(ChatColor.RED + "/rpgtext freeze toggle <player> : Switch the player's frozen state.");
            sender.sendMessage(ChatColor.RED + "/rpgtext reset <player> : Set the player's all score to 0.");
            sender.sendMessage(ChatColor.RED + "/rpgtext set <player> <score name> <score> : Set the player's score.");
            sender.sendMessage(ChatColor.RED + "/rpgtext list reset <player> : reset the player's list data.");
            return true;
        }

        /* /rpgtext <player> <text|config> <text> */
        else {
            Player player = plugin.getServer().getPlayer(args[1]);
            if(player != null && player.isOnline()){
                if(args.length == 3) {
                    if (args[0].equals("config") ){
                        if(!plugin.showMessagesFromConfig(player,args[2])) {
                            // コンフィグに任意の項目が存在しなかった場合の処理
                            sender.sendMessage("[RPGSystem]: Message jump error. That item does not exist");
                            return false;
                        }
                    }else if(args[0].equals("text")){
                        plugin.dynamicActionBar(player, plugin.replaceSymbolInText(args[2],player));
                    }else{
                        sender.sendMessage(ChatColor.RED + "/rpgtext <player> <text|config> <text>");
                        return false;
                    }
                }else{
                    if(plugin.isFloat(args[3]) && plugin.isFloat(args[4]) && plugin.isInteger(args[5])) {
                        plugin.dynamicActionBar(player, plugin.replaceSymbolInText(args[0],player), Integer.parseInt(args[5]), args[2], Float.parseFloat(args[4]), Float.parseFloat(args[3]), true, false);
                    }else{
                        sender.sendMessage(ChatColor.RED + "/rpgtext <player> <text> <sound> <volume> <pitch> <speed>");
                        return false;
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED + args[1] + "does not exist or Offline");
                return false;
            }
        }
        return true;
    }
}
