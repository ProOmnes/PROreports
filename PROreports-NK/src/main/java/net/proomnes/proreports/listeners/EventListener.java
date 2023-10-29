package net.proomnes.proreports.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.scheduler.Task;
import lombok.AllArgsConstructor;
import net.proomnes.proreports.PROreports;
import net.proomnes.proreports.objects.Report;
import net.proomnes.proreports.util.messages.MessageKeys;

@AllArgsConstructor
public class EventListener implements Listener {

    private final PROreports proReports;

    @EventHandler
    public void on(final PlayerJoinEvent event) {
        final Player joinPlayer = event.getPlayer();
        if (joinPlayer.hasPermission("proreports.role.moderator") || joinPlayer.isOp()) {
            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                @Override
                public void onRun(int i) {
                    final Player player = proReports.getServer().getPlayer(joinPlayer.getName());
                    if (player != null) {
                        proReports.getReportService().getReports(Report.Status.PENDING, reports -> {
                            if (!reports.isEmpty()) {
                                player.sendMessage(proReports.getMessageLoader().get(MessageKeys.REPORT_JOIN_INFO, reports.size()));
                            }
                        });
                    }
                }
            }, 60, true);
        }
    }

}
