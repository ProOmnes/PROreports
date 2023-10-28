package net.proomnes.proreports.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import net.proomnes.proreports.PROreports;

public class MyReportsCommand extends PluginCommand<PROreports> {

    public MyReportsCommand(final PROreports proReports) {
        super(proReports.getConfig().getString("commands.myreports.name"), proReports);
        this.setDescription(proReports.getConfig().getString("commands.myreports.description"));
        this.setAliases(proReports.getConfig().getStringList("commands.myreports.aliases").toArray(new String[]{}));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            this.getPlugin().getFormWindows().openMyReports((Player) sender);
        }
        return true;
    }
}
