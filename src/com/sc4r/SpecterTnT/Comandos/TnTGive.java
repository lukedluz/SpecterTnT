package com.sc4r.SpecterTnT.Comandos;

import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

import com.snockmc.tnt.*;

import org.bukkit.entity.*;

public class TnTGive implements CommandExecutor
{
    private SnockTnT plugin;
    
    public TnTGive(final SnockTnT plugin) {
        this.plugin = plugin;
        plugin.getCommand("givetnt").setExecutor((CommandExecutor)this);
    }
    
    public boolean onCommand(final CommandSender Sender, final Command Cmd, final String Label, final String[] Args) {
        if (!Cmd.getName().equalsIgnoreCase("givetnt")) {
            return false;
        }
        if (!Sender.hasPermission("snockmc.commands.tnt.give")) {
            Sender.sendMessage("�cVoc� precisa do grupo Gerente ou superior para executar este comando.");
            return true;
        }
        if (Args.length == 0 || Args.length == 1) {
            Sender.sendMessage("�cUse /givetnt <jogador> <quantidade>");
            return true;
        }
        if (Args.length < 2 || Bukkit.getPlayer(Args[0]) == null) {
            Sender.sendMessage("�cO jogador �f" + Args[0] + " �c est� off-line.");
            return true;
        }
        if (!this.isInt(Args[1])) {
            Sender.sendMessage("�cUtilize apenas n�meros na quantidade.");
            return true;
        }
        if (Integer.parseInt(Args[1]) > 0) {
            final Player player = Bukkit.getPlayer(Args[0]);
            for (int i = 0; i < Integer.parseInt(Args[1]); ++i) {
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), this.plugin.getTntItem().getTnt());
                }
                else {
                    player.getInventory().addItem(new ItemStack[] { this.plugin.getTntItem().getTnt() });
                }
            }
            return true;
        }
        Sender.sendMessage("�cN�o utilize n�meros negativos apenas inteiros.");
        return true;
    }
    
    private boolean isInt(final String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
