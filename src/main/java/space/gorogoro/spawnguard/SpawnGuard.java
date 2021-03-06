package space.gorogoro.spawnguard;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnGuard extends JavaPlugin implements Listener {

  Location spawnPoint = null;

  @Override
  public void onEnable(){
    try{
      getLogger().info("The Plugin Has Been Enabled!");

      // If there is no setting file, it is created
      if(!getDataFolder().exists()){
        getDataFolder().mkdir();
      }

      File configFile = new File(getDataFolder(), "config.yml");
      if(!configFile.exists()){
        saveDefaultConfig();
      }

      final PluginManager pm = getServer().getPluginManager();
      pm.registerEvents(this, this);

    } catch (Exception e){
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    if(baseProc(e.getEntity())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent e) {
    if(baseProc(e.getEntity())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onEntityDamageEvent(EntityDamageEvent e) {
    if(baseProc(e.getEntity())) {
      e.setCancelled(true);
    }
  }

  public boolean baseProc(Entity target) {
    if(!target.getType().equals(EntityType.PLAYER)) {
      return false;
    }
    Player p = (Player)target;

    Location spawnPoint = new Location(
        getServer().getWorld(getConfig().getString("spawn-point.world")),
        getConfig().getDouble("spawn-point.x"),
        getConfig().getDouble("spawn-point.y"),
        getConfig().getDouble("spawn-point.z")
      );
    Location playerPoint = p.getLocation();

    if(!spawnPoint.getWorld().equals(playerPoint.getWorld())) {
      return false;
    }
    if(spawnPoint.distance(playerPoint) > 20.0d) {
      return false;
    }

    return true;
  }

  @Override
  public void onDisable(){
    try{
      getLogger().info("The Plugin Has Been Disabled!");
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
