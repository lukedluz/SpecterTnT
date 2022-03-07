package com.sc4r.SpecterTnT.Eventos;

import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.util.*;
import org.bukkit.util.Vector;

import com.snockmc.tnt.*;

import org.bukkit.entity.*;

public class TnTListener implements Listener
{
    public SnockTnT plugin;
    private List<EntityType> entityTypes;
    
    public TnTListener(final SnockTnT plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        (this.entityTypes = new ArrayList<EntityType>()).add(EntityType.ARROW);
        this.entityTypes.add(EntityType.FIREBALL);
        this.entityTypes.add(EntityType.SMALL_FIREBALL);
    }
    
    @EventHandler
    public void breakTnT(final BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.TNT && e.getBlock().hasMetadata("SouTntImpulsao")) {
            e.setCancelled(true);
            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), this.plugin.getTntItem().getTnt());
        }
    }
    
    @EventHandler
    public void explosionTnT(final EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            if (e.getEntity().hasMetadata("SouTntImpulsao")) {
                e.blockList().clear();
            }
            else {
                for (final Block block : e.blockList()) {
                    if (block.hasMetadata("SouTntImpulsao")) {
                        final Location location = block.getLocation();
                        location.getWorld().getBlockAt(location).setType(Material.AIR);
                        final TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
                        tnt.setVelocity(new Vector(0.02, 0.15, 0.02));
                        tnt.setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
                        tnt.setFuseTicks(15);
                        location.getWorld().playSound(location, Sound.FIRE_IGNITE, 10.0f, 10.0f);
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void placeTnT(final BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.TNT && e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getTntItem().getTnt().getItemMeta().getDisplayName())) {
            final Block blk = e.getBlock();
            final ArrayList<Block> tntBlocks = new ArrayList<Block>();
            Boolean tntExists = false;
            if (blk.getRelative(BlockFace.NORTH).getType().equals(Material.REDSTONE_TORCH_ON) || blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_BLOCK)) {
                tntBlocks.add(blk.getRelative(BlockFace.NORTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.EAST).getType().equals(Material.REDSTONE_TORCH_ON) || blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_BLOCK)) {
                tntBlocks.add(blk.getRelative(BlockFace.EAST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.SOUTH).getType().equals(Material.REDSTONE_TORCH_ON) || blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_BLOCK)) {
                tntBlocks.add(blk.getRelative(BlockFace.SOUTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.WEST).getType().equals(Material.REDSTONE_TORCH_ON) || blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_BLOCK)) {
                tntBlocks.add(blk.getRelative(BlockFace.WEST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.DOWN).getType().equals(Material.REDSTONE_TORCH_ON) || blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_BLOCK)) {
                tntBlocks.add(blk.getRelative(BlockFace.DOWN));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_TORCH_ON) || blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_BLOCK)) {
                tntBlocks.add(blk.getRelative(BlockFace.UP));
                tntExists = true;
            }
            if (tntExists) {
                e.getPlayer().getInventory().removeItem(new ItemStack[] { this.plugin.getTntItem().getTnt() });
                e.setCancelled(true);
                final Location location = blk.getLocation();
                location.getWorld().getBlockAt(location).setType(Material.AIR);
                final TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
                tnt.setVelocity(new Vector(0.02, 0.15, 0.02));
                tnt.setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
                location.getWorld().playSound(location, Sound.FIRE_IGNITE, 10.0f, 10.0f);
                return;
            }
            e.getBlock().setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
        }
    }
    
    @EventHandler
    public void activeTnTFlint(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL && e.getClickedBlock().getType() == Material.TNT && e.getClickedBlock().hasMetadata("SouTntImpulsao")) {
            e.setCancelled(true);
            final Location location = e.getClickedBlock().getLocation();
            if (location.getWorld().getBlockAt(location).getType() == Material.TNT) {
                location.getWorld().getBlockAt(location).setType(Material.AIR);
            }
            final TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
            tnt.setVelocity(new Vector(0.02, 0.15, 0.02));
            tnt.setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
            location.getWorld().playSound(location, Sound.FIRE_IGNITE, -4.0f, 12.0f);
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void activeTntDispenser(final BlockDispenseEvent e) {
        if (e.getBlock().getType() == Material.DISPENSER && e.getItem().hasItemMeta() && e.getItem().getType() == Material.TNT && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getTntItem().getTnt().getItemMeta().getDisplayName())) {
            e.setCancelled(true);
            final Dispenser dispenser = (Dispenser)e.getBlock().getState();
            dispenser.getInventory().removeItem(new ItemStack[] { this.plugin.getTntItem().getTnt() });
            final Block block = e.getBlock().getRelative(e.getBlock().getFace(e.getBlock()));
            final Location location = block.getLocation();
            if (e.getBlock().getData() == 8) {
                location.setY(location.getY() - 1.0);
            }
            if (e.getBlock().getData() == 9) {
                location.setY(location.getY() + 1.0);
            }
            if (e.getBlock().getData() == 10) {
                location.setZ(location.getZ() - 1.0);
            }
            if (e.getBlock().getData() == 11) {
                location.setZ(location.getZ() + 1.0);
            }
            if (e.getBlock().getData() == 12) {
                location.setX(location.getX() - 1.0);
            }
            if (e.getBlock().getData() == 13) {
                location.setX(location.getX() + 1.0);
            }
            final TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
            tnt.setVelocity(new Vector(0.02, 0.15, 0.02));
            tnt.setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
            location.getWorld().playSound(location, Sound.FIRE_IGNITE, -4.0f, 12.0f);
        }
    }
    
    @EventHandler
    public void activeTntRedstone(final BlockRedstoneEvent e) {
        if (e.getNewCurrent() >= 1) {
            final Block blk = e.getBlock();
            final ArrayList<Block> tntBlocks = new ArrayList<Block>();
            Boolean tntExists = false;
            if (blk.getRelative(BlockFace.NORTH).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.NORTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.EAST).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.EAST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.SOUTH).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.SOUTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.WEST).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.WEST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.DOWN).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.DOWN));
                tntExists = true;
            }
            if (tntExists) {
                for (final Block tntBlk : tntBlocks) {
                    if (tntBlk.hasMetadata("SouTntImpulsao")) {
                        final Location location = tntBlk.getLocation();
                        location.getWorld().getBlockAt(location).setType(Material.AIR);
                        final TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
                        tnt.setVelocity(new Vector(0.02, 0.15, 0.02));
                        tnt.setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
                        location.getWorld().playSound(location, Sound.FIRE_IGNITE, 10.0f, 10.0f);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void placeRedstoneActiveTnT(final BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.REDSTONE_BLOCK || e.getBlock().getType() == Material.REDSTONE_TORCH_ON) {
            final Block blk = e.getBlock();
            final ArrayList<Block> tntBlocks = new ArrayList<Block>();
            Boolean tntExists = false;
            if (blk.getRelative(BlockFace.NORTH).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.NORTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.EAST).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.EAST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.SOUTH).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.SOUTH));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.WEST).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.WEST));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.DOWN).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.DOWN));
                tntExists = true;
            }
            else if (blk.getRelative(BlockFace.UP).getType().equals(Material.TNT)) {
                tntBlocks.add(blk.getRelative(BlockFace.UP));
                tntExists = true;
            }
            if (tntExists) {
                for (final Block tntBlk : tntBlocks) {
                    if (tntBlk.hasMetadata("SouTntImpulsao")) {
                        final Location location = tntBlk.getLocation();
                        location.getWorld().getBlockAt(location).setType(Material.AIR);
                        final TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
                        tnt.setVelocity(new Vector(0.02, 0.15, 0.02));
                        tnt.setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
                        location.getWorld().playSound(location, Sound.FIRE_IGNITE, 10.0f, 10.0f);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void activeTntProjectileHit(final ProjectileHitEvent e) {
        final Projectile projectile = e.getEntity();
        if (this.entityTypes.contains(projectile.getType())) {
            if (projectile.getFireTicks() == 0) {
                return;
            }
            final BlockIterator blockIterator = new BlockIterator(projectile.getWorld(), projectile.getLocation().toVector(), projectile.getVelocity().normalize(), 0.0, 4);
            Block hitBlock = null;
            while (blockIterator.hasNext()) {
                hitBlock = blockIterator.next();
                if (hitBlock.getType() != Material.AIR) {
                    break;
                }
            }
            if (hitBlock != null && hitBlock.getType() == Material.TNT && hitBlock.hasMetadata("SouTntImpulsao")) {
                projectile.remove();
                final Location location = hitBlock.getLocation();
                location.getWorld().getBlockAt(location).setType(Material.AIR);
                final TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
                tnt.setVelocity(new Vector(0.02, 0.15, 0.02));
                tnt.setMetadata("SouTntImpulsao", new FixedMetadataValue(this.plugin, true));
                location.getWorld().playSound(location, Sound.FIRE_IGNITE, 10.0f, 10.0f);
            }
        }
    }
}
