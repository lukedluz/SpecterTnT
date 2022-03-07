package com.sc4r.SpecterTnT;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Main extends JavaPlugin
{
    private TnTItem tntItem;

    public void onEnable() {
        this.saveDefaultConfig();
        try {
            final File file = new File(this.getDataFolder() + File.separator, "config.yml");
            final String allText = Resources.toString(file.toURI().toURL(), Charset.forName("UTF-8"));
            this.getConfig().loadFromString(allText);
        }
        catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        this.tntItem = new TnTItem(this);
        new TnTListener(this);
        new TnTGive(this);
        Bukkit.getConsoleSender().sendMessage("�a[SnockTnT] plugin iniciado, versao " + this.getDescription().getVersion());
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("�c[SnockTnT] plugin desabilitado.");
    }

    public TnTItem getTntItem() {
        return this.tntItem;
    }
}