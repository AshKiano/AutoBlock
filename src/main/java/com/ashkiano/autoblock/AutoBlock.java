package com.ashkiano.autoblock;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class AutoBlock extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        this.getCommand("autoblock").setExecutor(this);
        Metrics metrics = new Metrics(this, 21953);
        this.getLogger().info("Thank you for using the AutoBlock plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            convertToBlocks(player);
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
    }

    private void convertToBlocks(Player player) {
        Map<Material, Material> ingotToBlock = new HashMap<>();
        ingotToBlock.put(Material.IRON_INGOT, Material.IRON_BLOCK);
        ingotToBlock.put(Material.GOLD_INGOT, Material.GOLD_BLOCK);
        ingotToBlock.put(Material.DIAMOND, Material.DIAMOND_BLOCK);
        ingotToBlock.put(Material.REDSTONE, Material.REDSTONE_BLOCK);
        ingotToBlock.put(Material.EMERALD, Material.EMERALD_BLOCK);
        ingotToBlock.put(Material.COAL, Material.COAL_BLOCK);
        ingotToBlock.put(Material.LAPIS_LAZULI, Material.LAPIS_BLOCK);

        for (Map.Entry<Material, Material> entry : ingotToBlock.entrySet()) {
            Material ingot = entry.getKey();
            Material block = entry.getValue();
            int ingotCount = 0;

            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == ingot) {
                    ingotCount += item.getAmount();
                    player.getInventory().remove(item);
                }
            }

            int blockCount = ingotCount / 9;
            int remainingIngots = ingotCount % 9;

            if (blockCount > 0) {
                player.getInventory().addItem(new ItemStack(block, blockCount));
            }
            if (remainingIngots > 0) {
                player.getInventory().addItem(new ItemStack(ingot, remainingIngots));
            }
        }

        player.sendMessage("Items have been converted to blocks!");
    }
}