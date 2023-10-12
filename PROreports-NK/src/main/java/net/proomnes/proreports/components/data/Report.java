package net.proomnes.proreports.components.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

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
     */
    @Getter
    public enum Status {
        PENDING("Pending"),
        IN_PROGRESS("In Progress"),
        ON_HOLD("On Hold"),
        CLOSED("Closed");

        private final String name;

        Status(final String name) {
            this.name = name;
        }

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
