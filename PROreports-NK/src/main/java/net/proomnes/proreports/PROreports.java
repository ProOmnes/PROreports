package net.proomnes.proreports;

import cn.nukkit.plugin.PluginBase;

public class PROreports extends PluginBase {

    private PROreports proReports;

    @Override
    public void onLoad() {
        this.getLogger().info("PROreports - Loading plugin...");
    }

    @Override
    public void onEnable() {
        this.proReports = this;
        this.loadPlugin();
    }

    private void loadPlugin() {

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
