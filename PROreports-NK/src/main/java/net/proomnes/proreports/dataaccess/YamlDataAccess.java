package net.proomnes.proreports.dataaccess;

import net.proomnes.proreports.PROreports;
import net.proomnes.proreports.components.data.Report;

import java.util.Set;
import java.util.function.Consumer;

public class YamlDataAccess implements IDataAccess {

    public YamlDataAccess(final PROreports proReports) {

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

    }

    /**
     * Deletes a report from the database. The inserted report log is not affected by this method.
     *
     * @param id The unique id of the report
     */
    @Override
    public void deleteReport(String id) {

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

    }

    /**
     * Closes the report if the status is 'open'. After closing, the report is saved as 'closed'.
     *
     * @param id The unique id of a report
     */
    @Override
    public void closeReport(String id) {

    }

    /**
     * Check if a report id exists
     *
     * @param id     The unique id of a report
     * @param exists Returns the boolean if the report exists or not
     */
    @Override
    public void reportIdExists(String id, Consumer<Boolean> exists) {

    }

    /**
     * Sets a new progress status to a report
     *
     * @param id     The unique id of a report
     * @param status Provide the new status of the report
     */
    @Override
    public void updateStatus(String id, Report.Status status) {

    }

    /**
     * Sets a new moderator to a report
     *
     * @param id        The unique id of a report
     * @param moderator Provide the new moderator of the report
     */
    @Override
    public void updateModerator(String id, String moderator) {

    }

    /**
     * Gets a report by the unique id
     *
     * @param id The unique id of a report
     */
    @Override
    public void getReport(String id) {

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

    }

    /**
     * Gets a set of all reports by a provided status
     *
     * @param status  Only get reports by the provided status
     * @param reports Returns a set of reports
     */
    @Override
    public void getReports(Report.Status status, Consumer<Set<Report>> reports) {

    }
}
