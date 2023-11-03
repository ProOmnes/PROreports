package net.proomnes.proreports.util.messages;

import lombok.Getter;

@Getter
public enum MessageKeys {

    /*
      SYSTEM MESSAGES
     */
    SYSTEM_PREFIX("system.prefix", "§8» §cReports §8| §7", false),

    /*
      PLUGIN MESSAGES
     */
    REPORT_SEND("report.send", "§aYou have successfully reported player §e{0} §afor §e{1}§a. §7[§e{2}§7]", true),
    REPORT_CLOSED("report.closed", "§aYou successfully closed the report. §7[§e{0}§7]", true),
    REPORT_JOIN_INFO("report.join.info", "§7There are currently §e{0} reports §7open.", true),
    REPORT_ERROR_TARGET("report.error.target", "§cPlease enter a valid player name.", true),
    REPORT_ERROR_REASON("report.error.reason", "§cPlease provide a valid reason.", true),
    REPORT_ERROR_ALREADY_REPORTED("report.error.already.reported", "§cYou have already reported this player. Please wait while your report is being processed.", true),
    REPORT_ERROR_SELF("report.error.self", "§cYou are not allowed to report yourself!", true),

    REPORT_MANAGEMENT_ERROR_SEARCH("report.management.error.search", "§cPlease specify a valid player name to look up for reports.", true),
    REPORT_MANAGEMENT_ERROR_RESULT("report.management.error.result", "§cNo reports were found.", true),
    REPORT_MANAGEMENT_ERROR_PENDING("report.management.error.pending", "§cThere are no pending reports at the moment.", true),
    REPORT_MANAGEMENT_ERROR_TAKE("report.management.error.take", "§cYou do not have any reports to manage.", true),
    REPORT_MANAGEMENT_TAKE_REPORT("report.management.take.report", "§aYou have successfully taken the report. You can now edit it under your reports. §7[§e{0}§7]", true),
    REPORT_MANAGEMENT_SOLVE_REPORT("report.management.solve.report", "§aYou have successfully finished the report. The status is now §eClosed§a. §7[§e{0}§7]", true),

    /*
      UI DISPLAYS
     */
    UI_BUTTON_BACK("ui.button.back", "§8» §cBack", false),

    UI_CREATE_REPORT_TITLE("ui.create.report.title", "§8» §fCreate new report", false),
    UI_CREATE_REPORT_SELECT_PLAYER("ui.create.report.select.player", "> Select a player... <", false),
    UI_CREATE_REPORT_SELECT_PLAYER_INFO("ui.create.report.select.player.info", "§8» §7Select a player who is currently online.", false),
    UI_CREATE_REPORT_TARGET_INFO("ui.create.report.target.info", "§8» §7Specify a player to be reported. §6If you already selected a player, this field can be ignored.", false),
    UI_CREATE_REPORT_TARGET_PLACEHOLDER("ui.create.report.target.placeholder", "SomeName123", false),
    UI_CREATE_REPORT_REASON_INFO("ui.create.report.reason.info", "§8» §7Please provide a reason for the report.", false),
    UI_CREATE_REPORT_REASON_PLACEHOLDER("ui.create.report.reason.placeholder", "Hacking", false),

    UI_REPORT_MANAGEMENT_TITLE("ui.report.management.title", "§8» §fReport Management", false),
    UI_REPORT_MANAGEMENT_CONTENT("ui.report.management.content", "§8» §7Here you can take open reports and process your own reports as a moderator.", false),
    UI_REPORT_MANAGEMENT_OPEN_REPORTS("ui.report.management.button.open", "§8» §fSee pending reports", false),
    UI_REPORT_MANAGEMENT_MANAGE_REPORTS("ui.report.management.button.manage", "§8» §fSee my reports", false),
    UI_REPORT_MANAGEMENT_SEARCH_REPORTS("ui.report.management.button.search", "§8» §fLook up for reports", false),

    UI_REPORT_MANAGEMENT_PENDING_TITLE("ui.report.management.pending.title", "§8» §fPending reports", false),
    UI_REPORT_MANAGEMENT_PENDING_CONTENT("ui.report.management.pending.content", "§8» §7All open reports are listed here. Click on a report to get more information.", false),
    UI_REPORT_MANAGEMENT_PENDING_REPORT("ui.report.management.pending.report", "§8» §f{0} §8| §e{1}\n§f{3}", false),

    UI_REPORT_MANAGEMENT_MODERATOR_TITLE("ui.report.management.moderator.title", "§8» §fMy managed reports", false),
    UI_REPORT_MANAGEMENT_MODERATOR_CONTENT("ui.report.management.moderator.content", "§8» §7Here are all the pending reports you are currently processing as a moderator. Click on a report to process it.", false),
    UI_REPORT_MANAGEMENT_MODERATOR_REPORT("ui.report.management.moderator.report", "§8» §f{0} §8| §e{1}\n§f{3}", false),

    UI_REPORT_MANAGEMENT_SEARCH_TITLE("ui.report.management.search.title", "§8» §fLook up for reports", false),
    UI_REPORT_MANAGEMENT_SEARCH_SELECT_PROGRESS("ui.report.management.search.progress", "§8» §7Please select a status.", false),
    UI_REPORT_MANAGEMENT_SEARCH_SELECT_TYPE("ui.report.management.search.type", "§8» §7Please select a search type.", false),
    UI_REPORT_MANAGEMENT_SEARCH_VALUE("ui.report.management.search.value", "§8» §7Please specify a valid player name.", false),
    UI_REPORT_MANAGEMENT_SEARCH_VALUE_PLACEHOLDER("ui.report.management.search.value.placeholder", "Player007", false),

    UI_REPORT_MANAGEMENT_SEARCH_RESULT_TITLE("ui.report.management.search.result.title", "§8» §fResults", false),
    UI_REPORT_MANAGEMENT_SEARCH_RESULT_CONTENT("ui.report.management.search.result.content", "§8» §7These are all reports that were found. Click on a report to get more information.", false),
    UI_REPORT_MANAGEMENT_SEARCH_RESULT_REPORT("ui.report.management.search.result.report", "§8» §f{0} §8| §e{1}\n§f{3}", false),

    UI_REPORT_TITLE("ui.report.title", "§8» §fReport {0}", false),
    UI_REPORT_CONTENT("ui.report.content", "§eReport ID: §7{0}\n\n§bCreator: §7{1}\n§bTarget: §7{2}\n§bReason: §7{3}\n§bStatus: §7{4}\n§bModerator: §7{5}\n§bDate: §7{6}\n", false),
    UI_REPORT_BUTTON_TAKE("ui.report.button.take", "§8» §9Take report", false),
    UI_REPORT_BUTTON_SOLVE("ui.report.button.solve", "§8» §2Solve report", false),
    UI_REPORT_BUTTON_CLOSE("ui.report.button.close", "§8» §4Close report", false),

    UI_MY_REPORTS_TITLE("ui.myreports.title", "§8» §fMy reports", false),
    UI_MY_REPORTS_INFO("ui.myreports.info", "§8» §7Please select a report status.", false),

    UI_MY_REPORTS_RESULT_TITLE("ui.myreports.result.title", "§8» §fResults", false),
    UI_MY_REPORTS_RESULT_CONTENT("ui.myreports.result.content", "§8» §7These are all reports that were found. Click on a report to get more information.", false),
    UI_MY_REPORTS_RESULT_REPORT("ui.myreports.result.report", "§8» §f{0} §8| §e{1}\n§f{3}", false),

    ;

    private final String key;
    private final String defaultMessage;
    private final boolean prefix;

    MessageKeys(final String key, final String defaultMessage, final boolean prefix) {
        this.key = key;
        this.defaultMessage = defaultMessage;
        this.prefix = prefix;
    }

}
