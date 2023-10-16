package net.proomnes.proreports.dataaccess;

import net.proomnes.easysql.Column;
import net.proomnes.easysql.Document;
import net.proomnes.easysql.EasySQL;
import net.proomnes.proreports.PROreports;
import net.proomnes.proreports.objects.Report;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MySQLDataAccess implements IDataAccess {

    private final PROreports proReports;
    private final EasySQL client;

    public MySQLDataAccess(final PROreports proReports) {
        this.proReports = proReports;

        this.client = new EasySQL(
                proReports.getConfig().getString("mysql.host"),
                proReports.getConfig().getString("mysql.port"),
                proReports.getConfig().getString("mysql.user"),
                proReports.getConfig().getString("mysql.password"),
                proReports.getConfig().getString("mysql.database")
        );

        this.client.createTable("reports", "id",
                new Column("id", Column.Type.VARCHAR, 10)
                        .append("creator", Column.Type.VARCHAR, 50)
                        .append("target", Column.Type.VARCHAR, 50)
                        .append("reason", Column.Type.VARCHAR, 200)
                        .append("status", Column.Type.VARCHAR, 50)
                        .append("moderator", Column.Type.VARCHAR, 50)
                        .append("date", Column.Type.VARCHAR, 20)
        );

        proReports.getLogger().info("[MySQLClient] Connection established.");
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
        CompletableFuture.runAsync(() -> {
            final String generatedId = this.proReports.getRandomId(7);
            this.client.insert("reports",
                    new Document(
                            "id", generatedId
                    ).append(
                            "creator", creator
                    ).append(
                            "target", target
                    ).append(
                            "reason", reason
                    ).append(
                            "status", "Pending"
                    ).append(
                            "moderator", "Unknown"
                    ).append(
                            "date", this.proReports.getDateWithTime()
                    )
            );

            id.accept(generatedId);
        });
    }

    /**
     * Deletes a report from the database. The inserted report log is not affected by this method.
     *
     * @param id The unique id of the report
     */
    @Override
    public void deleteReport(String id) {
        CompletableFuture.runAsync(() -> this.client.delete("reports", new Document("id", id)));
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
        CompletableFuture.runAsync(() -> {
            final Document document = this.client.find("reports", new Document("target", target)).first();

            boolean checkReported =
                    document != null &&
                            document.getString("creator").equals(creator) &&
                            document.getString("status").equals("Pending");

            hasReported.accept(checkReported);
        });
    }

    /**
     * Closes the report if the status is 'open'. After closing, the report is saved as 'closed'.
     *
     * @param id The unique id of a report
     */
    @Override
    public void closeReport(String id) {
        CompletableFuture.runAsync(() -> {
            this.client.update("reports", new Document("id", id), new Document("status", "Closed"));
        });
    }

    /**
     * Check if a report id exists
     *
     * @param id     The unique id of a report
     * @param exists Returns the boolean if the report exists or not
     */
    @Override
    public void reportIdExists(String id, Consumer<Boolean> exists) {
        CompletableFuture.runAsync(() -> {
            exists.accept(this.client.find("reports", new Document("id", id)).first() != null);
        });
    }

    /**
     * Sets a new progress status to a report
     *
     * @param id     The unique id of a report
     * @param status Provide the new status of the report
     */
    @Override
    public void updateStatus(String id, Report.Status status) {
        CompletableFuture.runAsync(() -> {
            this.client.update("reports", new Document("id", id), new Document("status", status.getName()));
        });
    }

    /**
     * Sets a new moderator to a report
     *
     * @param id        The unique id of a report
     * @param moderator Provide the new moderator of the report
     */
    @Override
    public void updateModerator(String id, String moderator) {
        CompletableFuture.runAsync(() -> {
            this.client.update("reports", new Document("id", id), new Document("moderator", moderator));
        });
    }

    /**
     * Gets a report by the unique id
     *
     * @param id     The unique id of a report
     * @param report Returns the report
     */
    @Override
    public void getReport(String id, Consumer<Report> report) {
        CompletableFuture.runAsync(() -> {
            final Document document = this.client.find("reports", new Document("id", id)).first();

            if (document == null) {
                report.accept(null);
                return;
            }

            report.accept(new Report(
                    document.getString("id"),
                    document.getString("creator"),
                    document.getString("target"),
                    document.getString("reason"),
                    Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                    document.getString("moderator"),
                    document.getString("date")
            ));
        });
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
        CompletableFuture.runAsync(() -> {
            final Set<Report> reportSet = new HashSet<>();
            switch (searchType) {
                case BY_CREATOR:
                    this.client.find("reports", new Document("creator", value)).results().forEach(document -> {
                        if (document.getString("status").equals(status.getName())) {
                            reportSet.add(new Report(
                                    document.getString("id"),
                                    document.getString("creator"),
                                    document.getString("target"),
                                    document.getString("reason"),
                                    Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                                    document.getString("moderator"),
                                    document.getString("date")
                            ));
                        }
                    });
                    break;
                case BY_MODERATOR:
                    this.client.find("reports", new Document("moderator", value)).results().forEach(document -> {
                        if (document.getString("status").equals(status.getName())) {
                            reportSet.add(new Report(
                                    document.getString("id"),
                                    document.getString("creator"),
                                    document.getString("target"),
                                    document.getString("reason"),
                                    Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                                    document.getString("moderator"),
                                    document.getString("date")
                            ));
                        }
                    });
                    break;
                case BY_TARGET:
                    this.client.find("reports", new Document("target", value)).results().forEach(document -> {
                        if (document.getString("status").equals(status.getName())) {
                            reportSet.add(new Report(
                                    document.getString("id"),
                                    document.getString("creator"),
                                    document.getString("target"),
                                    document.getString("reason"),
                                    Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                                    document.getString("moderator"),
                                    document.getString("date")
                            ));
                        }
                    });
                    break;
                default:
                    reports.accept(null);
                    return;
            }

            reports.accept(reportSet);
        });
    }

    /**
     * Gets a set of all reports by a provided status
     *
     * @param status  Only get reports by the provided status
     * @param reports Returns a set of reports
     */
    @Override
    public void getReports(Report.Status status, Consumer<Set<Report>> reports) {
        CompletableFuture.runAsync(() -> {
            final Set<Report> reportSet = new HashSet<>();

            this.client.find("reports", new Document("status", status.getName())).results().forEach(document -> {
                reportSet.add(new Report(
                        document.getString("id"),
                        document.getString("creator"),
                        document.getString("target"),
                        document.getString("reason"),
                        Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                        document.getString("moderator"),
                        document.getString("date")
                ));
            });

            reports.accept(reportSet);
        });
    }
}
