package net.proomnes.proreports;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import net.proomnes.formapi.FormHandler;
import net.proomnes.proreports.commands.ReportCommand;
import net.proomnes.proreports.commands.ReportManagerCommand;
import net.proomnes.proreports.dataaccess.IDataAccess;
import net.proomnes.proreports.dataaccess.MongoDBDataAccess;
import net.proomnes.proreports.dataaccess.MySQLDataAccess;
import net.proomnes.proreports.dataaccess.YamlDataAccess;
import net.proomnes.proreports.services.ReportService;
import net.proomnes.proreports.util.forms.FormWindows;
import net.proomnes.proreports.util.messages.MessageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Getter
public class PROreports extends PluginBase {

    private IDataAccess dataAccess;

    private ReportService reportService;

    private FormWindows formWindows;

    private FormHandler formHandler;

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
        // utils
        this.messageLoader = new MessageLoader(this);
        this.formHandler = new FormHandler(this);
        this.formWindows = new FormWindows(this);

        // services
        this.reportService = new ReportService(this);

        // commands
        this.getServer().getCommandMap().register("proreports", new ReportCommand(this));
        this.getServer().getCommandMap().register("proreports", new ReportManagerCommand(this));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public String getRandomId(final int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        final StringBuilder stringBuilder = new StringBuilder();
        final Random rnd = new Random();
        while (stringBuilder.length() < length) {
            final int index = (int) (rnd.nextFloat() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }

    public String getDateWithTime() {
        final Date now = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return dateFormat.format(now);
    }

    public String getDate() {
        final Date now = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(now);
    }

}
