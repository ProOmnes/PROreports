package net.proomnes.proreports;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import net.proomnes.proreports.provider.MongoDBProvider;
import net.proomnes.proreports.provider.MySQLProvider;
import net.proomnes.proreports.provider.Provider;
import net.proomnes.proreports.provider.YamlProvider;

import java.util.HashMap;
import java.util.Map;

public class PROreports extends PluginBase {

    private final Map<String, Provider> providers = new HashMap<>();

    @Getter
    private Provider provider;

    @Override
    public void onLoad() {
        this.getLogger().info("[PROreports] Loading plugin...");
        this.providers.put("MongoDB", new MongoDBProvider());
        this.providers.put("MySQL", new MySQLProvider());
        this.providers.put("Yaml", new YamlProvider());
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        // connect the provider
        if (!this.providers.containsKey(this.getConfig().getString("settings.provider"))) {
            this.getLogger().error("§4[PROreports] Please specify a valid provider: 'Yaml', 'MySQL', 'MongoDB'.");
            return;
        }
        this.provider = this.providers.get(this.getConfig().getString("settings.provider"));
        this.provider.connectProvider(this);
        this.getLogger().info("§a[PROreports] Successfully loaded provider: " + this.getConfig().getString("settings.provider"));


    }

    private void loadPlugin() {

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
