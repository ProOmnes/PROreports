package net.proomnes.proreports.components.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @implNote Report class
 */
@AllArgsConstructor
@Data
public class Report {

    private final String id;
    private String creator;
    private String target;
    private final String reason;
    private Status status;
    private String moderator;
    private final String date;

    /**
     * Status that a report can assume
     * @see Report
     * @since 1.0.0
     */
    public enum Status {
        PENDING, IN_PROGRESS, ON_HOLD, CLOSED
    }

    /**
     * Search types to search reports by different parameters:
     * <li>BY_CREATOR</li>
     * <li>BY_TARGET</li>
     * <li>BY_MODERATOR</li>
     * @see Report
     */
    public enum SearchType {
        BY_CREATOR, BY_TARGET, BY_MODERATOR
    }

}
