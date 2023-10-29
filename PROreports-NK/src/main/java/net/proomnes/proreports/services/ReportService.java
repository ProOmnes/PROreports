package net.proomnes.proreports.services;

import lombok.Getter;
import net.proomnes.proreports.PROreports;
import net.proomnes.proreports.events.ReportCloseEvent;
import net.proomnes.proreports.events.ReportCreateEvent;
import net.proomnes.proreports.events.ReportUpdateEvent;
import net.proomnes.proreports.objects.Report;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;

@Getter
public class ReportService {

    private final PROreports proReports;
    private final HashMap<String, Report> cachedReports = new HashMap<>();

    public ReportService(final PROreports proReports) {
        this.proReports = proReports;
    }

    public void createReport(final String creator, final String target, final String reason, final Consumer<String> callbackId) {
        this.proReports.getDataAccess().createReport(creator, target, reason, (id) -> {
            this.cachedReports.put(id, new Report(
                    id, creator, target, reason, Report.Status.PENDING, "Unknown",
                    this.proReports.getDateWithTime()
            ));
            this.proReports.getServer().getPluginManager().callEvent(new ReportCreateEvent(this.cachedReports.get(id)));
            callbackId.accept(id);
        });
    }

    public void deleteReport(final String id) {
        this.proReports.getDataAccess().deleteReport(id);
        this.cachedReports.remove(id);
    }

    public void hasReported(final String creator, final String target, final Consumer<Boolean> hasReported) {
        this.proReports.getDataAccess().hasReported(creator, target, hasReported);
    }

    public void closeReport(final String id, final ReportCloseEvent.CloseType closeType) {
        this.proReports.getDataAccess().closeReport(id);
        this.cachedReports.get(id).setStatus(Report.Status.CLOSED);
        this.proReports.getServer().getPluginManager().callEvent(new ReportCloseEvent(this.cachedReports.get(id), closeType));
    }

    public void reportIdExists(final String id, final Consumer<Boolean> exists) {
        this.proReports.getDataAccess().reportIdExists(id, exists);
    }

    public void updateStatus(final String id, final Report.Status status) {
        final Report oldData = this.cachedReports.get(id);
        this.proReports.getDataAccess().updateStatus(id, status);
        this.cachedReports.get(id).setStatus(status);
        this.proReports.getServer().getPluginManager().callEvent(new ReportUpdateEvent(ReportUpdateEvent.UpdateType.STATUS, oldData, this.cachedReports.get(id)));
    }

    public void updateModerator(final String id, final String moderator) {
        final Report oldData = this.cachedReports.get(id);
        this.proReports.getDataAccess().updateModerator(id, moderator);
        this.cachedReports.get(id).setModerator(moderator);
        this.proReports.getServer().getPluginManager().callEvent(new ReportUpdateEvent(ReportUpdateEvent.UpdateType.MODERATOR, oldData, this.cachedReports.get(id)));
    }

    public void getReport(final String id, final Consumer<Report> callbackReport) {
        if (this.cachedReports.containsKey(id)) callbackReport.accept(this.cachedReports.get(id));
        else {
            this.proReports.getDataAccess().getReport(id, callbackReport::accept);
        }
    }

    public void getReports(final Report.Status status, final Report.SearchType searchType, final String value, final Consumer<Set<Report>> reports) {
        this.proReports.getDataAccess().getReports(status, searchType, value, reports);
    }

    public void getReports(final Report.Status status, final Consumer<Set<Report>> reports) {
        this.proReports.getDataAccess().getReports(status, reports);
    }

}
