package seisyun.rpgtext;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Freeze implements Listener {
    private List<Player> freezePlayerList = new ArrayList<>();
    private Plugin plugin;
    private boolean HORIZONTAL_FREEZE;
    private boolean VERTICAL_FREEZE;
    private boolean JUMP_FREEZE;
    private boolean FREEZE_PLAYER_INVINCIBLE;
    private boolean CANCEL_LEFT_CLICK;
    private boolean CANCEL_RIGHT_CLICK;
    private CustomConfig customConfig;
    private FileConfiguration config;
    private static final double STILL = -0.0784000015258789; //停止状態

    Freeze(Plugin plugin,CustomConfig customConfig){
        this.plugin = plugin;
        this.customConfig = customConfig;
        this.config = customConfig.getConfig();
        reload();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    void set(Player player){
        if (!freezePlayerList.contains(player)) {
            freezePlayerList.add(player);
        }
    }
    void remove(Player player){
        freezePlayerList.remove(player);
    }
    void toggle(Player player){
        if(freezePlayerList.contains(player)){
            freezePlayerList.remove(player);
        }else{
            freezePlayerList.add(player);
        }
    }
    void clear(){
        freezePlayerList.clear();
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        remove(e.getEntity());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        remove(e.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(!(e.getPlayer().getGameMode() == GameMode.CREATIVE) && freezePlayerList.contains(e.getPlayer())){
            //Horizontal Move
            if (HORIZONTAL_FREEZE && e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()){
                e.setCancelled(true);
                return;
            }
            //Vertical Move
            if (VERTICAL_FREEZE && e.getFrom().getY() != e.getTo().getY()){
                e.setCancelled(true);
                return;
            }
            //Jump
            boolean isJumping = e.getPlayer().getVelocity().getY() > STILL;
            if (isJumping){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if (FREEZE_PLAYER_INVINCIBLE && e.getEntity() instanceof Player){
            Player player = (Player)e.getEntity();
            if (freezePlayerList.contains(player)){
                e.setCancelled(true);
                return;
            }
        }
        if(CANCEL_LEFT_CLICK && e.getDamager() instanceof Player ){
            Player player = (Player)e.getDamager();
            if (!(player.getGameMode() == GameMode.CREATIVE) && freezePlayerList.contains(player)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if (!(e.getPlayer().getGameMode() == GameMode.CREATIVE) &&  CANCEL_RIGHT_CLICK && e.getAction() == Action.RIGHT_CLICK_BLOCK && freezePlayerList.contains(e.getPlayer())){
            e.setCancelled(true);
        }
    }

    void reload(){
        customConfig.reloadConfig();
        HORIZONTAL_FREEZE = config.getBoolean("default.freeze.horizontal",true);
        VERTICAL_FREEZE = config.getBoolean("default.freeze.vertical",false);
        JUMP_FREEZE = config.getBoolean("default.freeze.jump",true);
        FREEZE_PLAYER_INVINCIBLE= config.getBoolean("default.freeze.invincible",true);
        CANCEL_RIGHT_CLICK = config.getBoolean("default.freeze.leftclick-cancel",true);
        CANCEL_LEFT_CLICK= config.getBoolean("default.freeze.rightclick-cancel",true);
    }

}
