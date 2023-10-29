package net.proomnes.proreports.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.proomnes.proreports.objects.Report;

@AllArgsConstructor
@Getter
public class ReportUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UpdateType updateType;
    private final Report oldData;
    private final Report report;


    public static HandlerList getHandlers() {
        return handlers;
    }

    public enum UpdateType {
        STATUS, MODERATOR, MESSAGE
    }
}
