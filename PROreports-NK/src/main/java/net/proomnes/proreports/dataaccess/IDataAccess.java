package net.proomnes.proreports.dataaccess;

import net.proomnes.proreports.components.data.Report;

import java.util.Set;
import java.util.function.Consumer;

public interface IDataAccess {

    /**
     * Creates a new report for a player about a target with a reason
     *
     * @param creator Creator of the report
     * @param target  Reported player
     * @param reason  Provided reason
     * @param id      Returns the generated report id after creation
     */
    void createReport(final String creator, final String target, final String reason, final Consumer<String> id);

    /**
     * Deletes a report from the database. The inserted report log is not affected by this method.
     *
     * @param id The unique id of the report
     */
    void deleteReport(final String id);

    /**
     * Checks if a player has already reported another player.
     *
     * @param creator     Creator of the report
     * @param target      Reported player
     * @param hasReported Returns the boolean if the request was successful or not
     */
    void hasReported(final String creator, final String target, final Consumer<Boolean> hasReported);

    /**
     * Closes the report if the status is 'open'. After closing, the report is saved as 'closed'.
     *
     * @param id The unique id of a report
     */
    void closeReport(final String id);

    /**
     * Check if a report id exists
     *
     * @param id     The unique id of a report
     * @param exists Returns the boolean if the report exists or not
     */
    void reportIdExists(final String id, final Consumer<Boolean> exists);

    /**
     * Sets a new progress status to a report
     *
     * @param id     The unique id of a report
     * @param status Provide the new status of the report
     */
    void updateStatus(final String id, final Report.Status status);

    /**
     * Sets a new moderator to a report
     *
     * @param id        The unique id of a report
     * @param moderator Provide the new moderator of the report
     */
    void updateModerator(final String id, final String moderator);

    /**
     * Gets a report by the unique id
     *
     * @param id The unique id of a report
     */
    void getReport(final String id);

    /**
     * Gets a set of reports by a provided status and search type
     *
     * @param status     Only get reports by the provided status
     * @param searchType Get reports by the search type
     * @param value      Provide a value depending on the search type
     * @param reports    Returns a set of reports
     */
    void getReports(final Report.Status status, final Report.SearchType searchType, final String value, final Consumer<Set<Report>> reports);

    /**
     * Gets a set of all reports by a provided status
     *
     * @param status  Only get reports by the provided status
     * @param reports Returns a set of reports
     */
    void getReports(final Report.Status status, final Consumer<Set<Report>> reports);

}
