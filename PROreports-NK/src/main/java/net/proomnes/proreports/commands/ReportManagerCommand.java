package net.proomnes.proreports.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import net.proomnes.proreports.PROreports;

public class ReportManagerCommand extends PluginCommand<PROreports> {

    public ReportManagerCommand(final PROreports proReports) {
        super(proReports.getConfig().getString("commands.reportmanager.name"), proReports);
        this.setPermission("proreports.role.moderator");
        this.setDescription(proReports.getConfig().getString("commands.reportmanager.description"));
        this.setAliases(proReports.getConfig().getStringList("commands.reportmanager.aliases").toArray(new String[]{}));

        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
        });
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!this.testPermission(sender)) return true;
        if (sender instanceof Player) {
            this.getPlugin().getFormWindows().openReportManagement((Player) sender);
        }
        return false;
    }
}