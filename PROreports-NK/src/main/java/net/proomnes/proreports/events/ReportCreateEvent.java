package net.proomnes.proreports.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.proomnes.proreports.objects.Report;

@AllArgsConstructor
@Getter
public class ReportCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Report report;

    public static HandlerList getHandlers() {
        return handlers;
    }
}
