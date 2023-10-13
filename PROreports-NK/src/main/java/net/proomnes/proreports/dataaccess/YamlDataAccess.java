package net.proomnes.proreports.dataaccess;

import cn.nukkit.utils.Config;
import net.proomnes.proreports.PROreports;
import net.proomnes.proreports.components.data.Report;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class YamlDataAccess implements IDataAccess {

    private final PROreports proReports;
    private final Config reportsData;

    public YamlDataAccess(final PROreports proReports) {
        this.proReports = proReports;

        proReports.saveResource("/data/reports.yml");
        this.reportsData = new Config(proReports.getDataFolder() + "/data/reports.yml", Config.YAML);
    }

    /**
     * Creates a new report for a player about a target with a reason
     *
     * @param creator Creator of the report
     * @param target  Reported player
     * @param reason  Provided reason
     * @param id      Returns the generated report id after creation
     */
    @Override
    public void createReport(String creator, String target, String reason, Consumer<String> id) {
        final String generatedId = this.proReports.getPluginUtils().getRandomId(7);

        this.reportsData.set("reports." + generatedId + ".creator", creator);
        this.reportsData.set("reports." + generatedId + ".target", target);
        this.reportsData.set("reports." + generatedId + ".reason", reason);
        this.reportsData.set("reports." + generatedId + ".status", "Pending");
        this.reportsData.set("reports." + generatedId + ".moderator", "Unknown");
        this.reportsData.set("reports." + generatedId + ".date", this.proReports.getPluginUtils().getDateWithTime());
        this.reportsData.save();
        this.reportsData.reload();

        id.accept(generatedId);
    }

    /**
     * Deletes a report from the database. The inserted report log is not affected by this method.
     *
     * @param id The unique id of the report
     */
    @Override
    public void deleteReport(String id) {
        final Map<String, Object> objectMap = this.reportsData.getSection("reports." + id).getAllMap();
        objectMap.remove(id);

        this.reportsData.set("reports", objectMap);
        this.reportsData.save();
        this.reportsData.reload();
    }

    /**
     * Checks if a player has already reported another player.
     *
     * @param creator     Creator of the report
     * @param target      Reported player
     * @param hasReported Returns the boolean if the request was successful or not
     */
    @Override
    public void hasReported(String creator, String target, Consumer<Boolean> hasReported) {
        final AtomicBoolean checkIfReported = new AtomicBoolean(false);

        this.reportsData.getSection("reports").getAll().getKeys().forEach(id -> {
            if (this.reportsData.getString("reports." + id + ".creator").equals(creator) &&
                    this.reportsData.getString("reports." + id + ".target").equals(target))
                checkIfReported.set(true);
        });

        hasReported.accept(checkIfReported.get());
    }

    /**
     * Closes the report if the status is 'open'. After closing, the report is saved as 'closed'.
     *
     * @param id The unique id of a report
     */
    @Override
    public void closeReport(String id) {
        this.reportsData.set("reports." + id + ".status", "Closed");
        this.reportsData.save();
        this.reportsData.reload();
    }

    /**
     * Check if a report id exists
     *
     * @param id     The unique id of a report
     * @param exists Returns the boolean if the report exists or not
     */
    @Override
    public void reportIdExists(String id, Consumer<Boolean> exists) {
        exists.accept(this.reportsData.exists("reports." + id));
    }

    /**
     * Sets a new progress status to a report
     *
     * @param id     The unique id of a report
     * @param status Provide the new status of the report
     */
    @Override
    public void updateStatus(String id, Report.Status status) {
        this.reportsData.set("reports." + id + ".status", status.getName());
        this.reportsData.save();
        this.reportsData.reload();
    }

    /**
     * Sets a new moderator to a report
     *
     * @param id        The unique id of a report
     * @param moderator Provide the new moderator of the report
     */
    @Override
    public void updateModerator(String id, String moderator) {
        this.reportsData.set("reports." + id + ".moderator", moderator);
        this.reportsData.save();
        this.reportsData.reload();
    }

    /**
     * Gets a report by the unique id
     *
     * @param id     The unique id of a report
     * @param report Returns the report
     */
    @Override
    public void getReport(String id, Consumer<Report> report) {
        if (!this.reportsData.exists("reports." + id)) {
            report.accept(null);
            return;
        }

        final Report callbackReport = new Report(
                id,
                this.reportsData.getString("reports." + id + ".creator"),
                this.reportsData.getString("reports." + id + ".target"),
                this.reportsData.getString("reports." + id + ".reason"),
                Report.Status.valueOf(this.reportsData.getString("reports." + id + ".status")
                        .replace(" ", "_").toUpperCase()),
                this.reportsData.getString("reports." + id + ".moderator"),
                this.reportsData.getString("reports." + id + ".date")
        );

        report.accept(callbackReport);
    }

    /**
     * Gets a set of reports by a provided status and search type
     *
     * @param status     Only get reports by the provided status
     * @param searchType Get reports by the search type
     * @param value      Provide a value depending on the search type
     * @param reports    Returns a set of reports
     */
    @Override
    public void getReports(Report.Status status, Report.SearchType searchType, String value, Consumer<Set<Report>> reports) {
        final Set<Report> reportSet = new HashSet<>();

        switch (searchType) {
            case BY_CREATOR:
                this.reportsData.getSection("reports").getAll().getKeys().forEach(id -> {
                    if (this.reportsData.getString("reports." + id + ".creator").equals(value)) {
                        reportSet.add(new Report(
                                id,
                                this.reportsData.getString("reports." + id + ".creator"),
                                this.reportsData.getString("reports." + id + ".target"),
                                this.reportsData.getString("reports." + id + ".reason"),
                                Report.Status.valueOf(this.reportsData.getString("reports." + id + ".status")
                                        .replace(" ", "_").toUpperCase()),
                                this.reportsData.getString("reports." + id + ".moderator"),
                                this.reportsData.getString("reports." + id + ".date")
                        ));
                    }
                });
                break;
            case BY_TARGET:
                this.reportsData.getSection("reports").getAll().getKeys().forEach(id -> {
                    if (this.reportsData.getString("reports." + id + ".target").equals(value)) {
                        reportSet.add(new Report(
                                id,
                                this.reportsData.getString("reports." + id + ".creator"),
                                this.reportsData.getString("reports." + id + ".target"),
                                this.reportsData.getString("reports." + id + ".reason"),
                                Report.Status.valueOf(this.reportsData.getString("reports." + id + ".status")
                                        .replace(" ", "_").toUpperCase()),
                                this.reportsData.getString("reports." + id + ".moderator"),
                                this.reportsData.getString("reports." + id + ".date")
                        ));
                    }
                });
                break;
            case BY_MODERATOR:
                this.reportsData.getSection("reports").getAll().getKeys().forEach(id -> {
                    if (this.reportsData.getString("reports." + id + ".moderator").equals(value)) {
                        reportSet.add(new Report(
                                id,
                                this.reportsData.getString("reports." + id + ".creator"),
                                this.reportsData.getString("reports." + id + ".target"),
                                this.reportsData.getString("reports." + id + ".reason"),
                                Report.Status.valueOf(this.reportsData.getString("reports." + id + ".status")
                                        .replace(" ", "_").toUpperCase()),
                                this.reportsData.getString("reports." + id + ".moderator"),
                                this.reportsData.getString("reports." + id + ".date")
                        ));
                    }
                });
                break;
            default:
                reports.accept(null);
                return;
        }

        reports.accept(reportSet);
    }

    /**
     * Gets a set of all reports by a provided status
     *
     * @param status  Only get reports by the provided status
     * @param reports Returns a set of reports
     */
    @Override
    public void getReports(Report.Status status, Consumer<Set<Report>> reports) {
        final Set<Report> reportSet = new HashSet<>();

        this.reportsData.getSection("reports").getAll().getKeys().forEach(id -> {
            if (this.reportsData.getString("reports." + id + ".status").equals(status.getName())) {
                reportSet.add(new Report(
                        id,
                        this.reportsData.getString("reports." + id + ".creator"),
                        this.reportsData.getString("reports." + id + ".target"),
                        this.reportsData.getString("reports." + id + ".reason"),
                        Report.Status.valueOf(this.reportsData.getString("reports." + id + ".status")
                                .replace(" ", "_").toUpperCase()),
                        this.reportsData.getString("reports." + id + ".moderator"),
                        this.reportsData.getString("reports." + id + ".date")
                ));
            }
        });

        reports.accept(reportSet);
    }
}
