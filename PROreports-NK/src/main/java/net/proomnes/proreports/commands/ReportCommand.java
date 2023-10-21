package net.proomnes.proreports.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import net.proomnes.proreports.PROreports;

public class ReportCommand extends PluginCommand<PROreports> {

    final PROreports proReports;

    public ReportCommand(final PROreports proReports) {
        super(proReports.getConfig().getString("commands.report.name"), proReports);
        this.setDescription(proReports.getConfig().getString("commands.report.description"));
        this.setAliases(proReports.getConfig().getStringList("commands.report.aliases").toArray(new String[]{}));

        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
        });

        this.proReports = proReports;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                this.proReports.getFormWindows().openCreateReport((Player) sender,  args[0]);
            } else {
                this.proReports.getFormWindows().openCreateReport((Player) sender, "");
            }
        }
        return false;
    }
}
