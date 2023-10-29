package net.proomnes.proreports.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.proomnes.proreports.objects.Report;

@AllArgsConstructor
@Getter
public class ReportCloseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Report report;
    private final CloseType closeType;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public enum CloseType {
        BY_MODERATOR, BY_CREATOR, BY_SYSTEM
    }

}
