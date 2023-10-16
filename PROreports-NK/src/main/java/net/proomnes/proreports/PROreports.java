package net.proomnes.proreports;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import net.proomnes.proreports.components.utils.PluginUtils;
import net.proomnes.proreports.dataaccess.IDataAccess;
import net.proomnes.proreports.dataaccess.MongoDBDataAccess;
import net.proomnes.proreports.dataaccess.MySQLDataAccess;
import net.proomnes.proreports.dataaccess.YamlDataAccess;
import net.proomnes.proreports.services.ReportService;
import net.proomnes.proreports.util.messages.MessageLoader;

@Getter
public class PROreports extends PluginBase {

    private IDataAccess dataAccess;

    private ReportService reportService;

    private PluginUtils pluginUtils;

    private MessageLoader messageLoader;

    @Override
    public void onLoad() {
        this.getLogger().info("[PROreports] Loading plugin...");
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        // Initializes the provider and connects it
        this.setUpProvider();

        this.loadPlugin();
    }

    private void setUpProvider() {
        switch (this.getConfig().getString("settings.provider")) {
            case "Yaml":
                this.dataAccess = new YamlDataAccess(this);
                break;
            case "MongoDB":
                this.dataAccess = new MongoDBDataAccess(this);
                break;
            case "MySQL":
                this.dataAccess = new MySQLDataAccess(this);
                break;
            default:
                this.getLogger().error("§4[PROreports] Please specify a valid provider: 'Yaml', 'MySQL', 'MongoDB'.");
                break;
        }
    }

    private void loadPlugin() {
        this.pluginUtils = new PluginUtils();

        this.messageLoader = new MessageLoader(this);

        this.reportService = new ReportService(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
