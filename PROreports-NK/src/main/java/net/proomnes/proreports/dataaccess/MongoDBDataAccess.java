package net.proomnes.proreports.dataaccess;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.proomnes.proreports.PROreports;
import net.proomnes.proreports.objects.Report;
import org.bson.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MongoDBDataAccess implements IDataAccess {

    private PROreports proReports;
    public MongoClient mongoClient;
    public MongoCollection<Document> reportCollection;

    public MongoDBDataAccess(final PROreports proReports) {
        this.proReports = proReports;
        CompletableFuture.runAsync(() -> {
            // connect MongoDB database with provided information
            final MongoClientURI mongoClientURI = new MongoClientURI(proReports.getConfig().getString("mongodb.uri"));
            this.mongoClient = new MongoClient(mongoClientURI);
            final MongoDatabase reportDatabase = mongoClient.getDatabase(proReports.getConfig().getString("mongodb.database"));

            // report database where all report data will be stored
            this.reportCollection = reportDatabase.getCollection("report_data");

            proReports.getLogger().info("[MongoDB] Connection established.");
        });
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
            final Document document = new Document(
                    "_id", generatedId
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
            );

            this.reportCollection.insertOne(document);

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
        CompletableFuture.runAsync(() -> this.reportCollection.deleteOne(new Document("_id", id)));
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
            final Document document = this.reportCollection.find(
                    new Document(
                            "creator", creator
                    ).append(
                            "target", target
                    ).append(
                            "status", "Pending"
                    )
            ).first();

            hasReported.accept(document != null);
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
            this.reportCollection.updateOne(
                    Objects.requireNonNull(this.reportCollection.find(new Document(
                                    "_id", id
                            )
                    ).first()),
                    new Document("$set", new Document("status", "Closed"))
            );
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
            final Document document = this.reportCollection.find(new Document(
                            "_id", id
                    )
            ).first();

            exists.accept(document != null);
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
            this.reportCollection.updateOne(
                    Objects.requireNonNull(this.reportCollection.find(new Document(
                                    "_id", id
                            )
                    ).first()),
                    new Document("$set", new Document("status", status.getName()))
            );
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
            this.reportCollection.updateOne(
                    Objects.requireNonNull(this.reportCollection.find(new Document(
                                    "_id", id
                            )
                    ).first()),
                    new Document("$set", new Document("moderator", moderator))
            );
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
            final Document document = this.reportCollection.find(new Document(
                            "_id", id
                    )
            ).first();

            if (document == null) {
                report.accept(null);
                return;
            }

            report.accept(new Report(
                    document.getString("_id"),
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
                case BY_CREATOR: {
                    this.reportCollection.find(new Document(
                            "creator", value
                    ).append(
                            "status", status.getName()
                    )).forEach((Consumer<? super Document>) (document) -> {
                        reportSet.add(new Report(
                                document.getString("_id"),
                                document.getString("creator"),
                                document.getString("target"),
                                document.getString("reason"),
                                Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                                document.getString("moderator"),
                                document.getString("date")
                        ));
                    });
                    break;
                }
                case BY_TARGET: {
                    this.reportCollection.find(new Document(
                            "target", value
                    ).append(
                            "status", status.getName()
                    )).forEach((Consumer<? super Document>) (document) -> {
                        reportSet.add(new Report(
                                document.getString("_id"),
                                document.getString("creator"),
                                document.getString("target"),
                                document.getString("reason"),
                                Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                                document.getString("moderator"),
                                document.getString("date")
                        ));
                    });
                    break;
                }
                case BY_MODERATOR: {
                    this.reportCollection.find(new Document(
                            "status", status.getName()
                    ).append(
                            "moderator", value
                    )).forEach((Consumer<? super Document>) (document) -> {
                        reportSet.add(new Report(
                                document.getString("_id"),
                                document.getString("creator"),
                                document.getString("target"),
                                document.getString("reason"),
                                Report.Status.valueOf(document.getString("status").replace(" ", "_").toUpperCase()),
                                document.getString("moderator"),
                                document.getString("date")
                        ));
                    });
                    break;
                }
                default: {
                    reports.accept(null);
                    return;
                }
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

            this.reportCollection.find(new Document(
                    "status", status.getName()
            )).forEach((Consumer<? super Document>) (document) -> {
                reportSet.add(new Report(
                        document.getString("_id"),
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
