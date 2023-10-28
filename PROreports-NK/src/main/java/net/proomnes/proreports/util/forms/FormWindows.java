package net.proomnes.proreports.util.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import net.proomnes.formapi.form.CustomForm;
import net.proomnes.formapi.form.SimpleForm;
import net.proomnes.proreports.PROreports;
import net.proomnes.proreports.objects.Report;
import net.proomnes.proreports.util.messages.MessageKeys;
import net.proomnes.proreports.util.messages.MessageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormWindows {

    private final PROreports proReports;
    private final MessageLoader messageLoader;

    public FormWindows(final PROreports proReports) {
        this.proReports = proReports;
        this.messageLoader = proReports.getMessageLoader();
    }

    public void openCreateReport(final Player requester, final String target) {
        final List<String> onlinePlayers = new ArrayList<>();
        onlinePlayers.add(this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_SELECT_PLAYER));
        onlinePlayers.remove(requester.getName());
        this.proReports.getServer().getOnlinePlayers().values().forEach(onlinePlayer -> onlinePlayers.add(onlinePlayer.getName()));

        final CustomForm form = new CustomForm.Builder(this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_TITLE))
                .addElement(new ElementDropdown(
                        this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_SELECT_PLAYER_INFO),
                        onlinePlayers,
                        0
                ))
                .addElement(new ElementInput(
                        this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_TARGET_INFO),
                        this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_TARGET_PLACEHOLDER),
                        target
                ))
                .addElement(new ElementInput(
                        this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_REASON_INFO),
                        this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_REASON_PLACEHOLDER)
                ))
                .onSubmit(((submitter, response) -> {
                    final String dropDownResponse = response.getDropdownResponse(0).getElementContent();
                    String targetResponse = response.getInputResponse(1);
                    final String reasonResponse = response.getInputResponse(2);

                    if (!dropDownResponse.equals(this.messageLoader.get(MessageKeys.UI_CREATE_REPORT_SELECT_PLAYER))) {
                        targetResponse = dropDownResponse;
                    }

                    if (targetResponse.isEmpty()) {
                        requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_ERROR_TARGET));
                        return;
                    }

                    if (reasonResponse.isEmpty()) {
                        requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_ERROR_REASON));
                        return;
                    }

                    if (targetResponse.equals(requester.getName())) {
                        requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_ERROR_SELF));
                        return;
                    }

                    final String finalTargetResponse1 = targetResponse;
                    this.proReports.getReportService().hasReported(requester.getName(), targetResponse, has -> {
                        if (!has) {
                            this.proReports.getReportService().createReport(requester.getName(), finalTargetResponse1, reasonResponse, id -> {
                                requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_SEND, finalTargetResponse1, reasonResponse, id));
                            });
                        } else {
                            requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_ERROR_ALREADY_REPORTED));
                        }
                    });
                }))
                .build();
        form.show(requester);
    }

    public void openReportManagement(final Player requester) {
        final SimpleForm form = new SimpleForm.Builder(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_TITLE),
                this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_CONTENT))
                .addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_OPEN_REPORTS),
                                new ElementButtonImageData("url", "https://form-images-nk.proomnes.net/img/proreports/d0d8d346-efcf-48d8-9b94-39a532d32ed2.png")),
                        this::openPendingReports
                )
                .addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_MANAGE_REPORTS),
                                new ElementButtonImageData("url", "https://form-images-nk.proomnes.net/img/proreports/107eef76-a9c7-4b60-988f-07fe90c9b5b7.png")),
                        this::openMyManagementReports
                )
                .addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_REPORTS),
                                new ElementButtonImageData("url", "https://form-images-nk.proomnes.net/img/proreports/4bac3329-2d97-435d-8780-e07009f269e9.png")),
                        this::openSearchReports
                )
                .build();
        form.show(requester);
    }

    public void openPendingReports(final Player requester) {
        final SimpleForm.Builder form = new SimpleForm.Builder(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_PENDING_TITLE), this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_PENDING_CONTENT));

        this.proReports.getReportService().getReports(Report.Status.PENDING, reports -> {
            /*if (reports.isEmpty()) {
                requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_MANAGEMENT_ERROR_PENDING));
                return;
            }*/
            reports.forEach(report -> {
                form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_PENDING_REPORT, report.getId(), report.getCreator(), report.getTarget(), report.getReason())),
                        submitter -> {
                            this.openReport(submitter, report);
                        });
            });
        });
        form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_BUTTON_BACK), new ElementButtonImageData("url", "")), this::openReportManagement);

        form.build().show(requester);
    }

    public void openMyManagementReports(final Player requester) {
        final SimpleForm.Builder form = new SimpleForm.Builder(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_MODERATOR_TITLE), this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_MODERATOR_CONTENT));

        this.proReports.getReportService().getReports(Report.Status.IN_PROGRESS, Report.SearchType.BY_MODERATOR, requester.getName(), reports -> {
            /*if (reports.isEmpty()) {
                requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_MANAGEMENT_ERROR_TAKE));
                return;
            }*/
            reports.forEach(report -> {
                form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_MODERATOR_REPORT, report.getId(), report.getCreator(), report.getTarget(), report.getReason())),
                        submitter -> {
                            this.openReport(submitter, report);
                        });
            });
        });
        form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_BUTTON_BACK), new ElementButtonImageData("url", "")), this::openReportManagement);

        form.build().show(requester);
    }

    public void openSearchReports(final Player requester) {
        final CustomForm form = new CustomForm.Builder(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_TITLE))
                .addElement(new ElementDropdown(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_SELECT_PROGRESS), Arrays.asList("Pending", "In Progress", "Closed"), 1))
                .addElement(new ElementDropdown(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_SELECT_TYPE), Arrays.asList("Creator", "Target", "Moderator"), 0))
                .addElement(new ElementInput(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_VALUE), this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_VALUE_PLACEHOLDER)))
                .onSubmit((submitter, response) -> {
                    final Report.Status status = Report.Status.valueOf(response.getDropdownResponse(0).getElementContent().replace(" ", "_").toUpperCase());
                    final Report.SearchType type = Report.SearchType.valueOf("BY_" + response.getDropdownResponse(1).getElementContent().toUpperCase());
                    final String value = response.getInputResponse(2);

                    if (value.isEmpty()) {
                        submitter.sendMessage(this.messageLoader.get(MessageKeys.REPORT_MANAGEMENT_ERROR_SEARCH));
                        return;
                    }

                    this.openResultSearchReports(submitter, status, type, value);
                })
                .build();
        form.show(requester);
    }

    public void openResultSearchReports(final Player requester, final Report.Status status, final Report.SearchType searchType, final String value) {
        final SimpleForm.Builder form = new SimpleForm.Builder(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_RESULT_TITLE), this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_RESULT_CONTENT));

        this.proReports.getReportService().getReports(status, searchType, value, reports -> {
            /*if (reports.isEmpty()) {
                requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_MANAGEMENT_ERROR_RESULT));
                return;
            }*/
            reports.forEach(report -> {
                form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_MANAGEMENT_SEARCH_RESULT_REPORT, report.getId(), report.getCreator(), report.getTarget(), report.getReason())),
                        submitter -> {
                            this.openReport(submitter, report);
                        });
            });
        });
        form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_BUTTON_BACK), new ElementButtonImageData("url", "")), this::openReportManagement);

        form.build().show(requester);
    }

    public void openReport(final Player requester, final Report report) {
        final SimpleForm.Builder form = new SimpleForm.Builder(this.messageLoader.get(MessageKeys.UI_REPORT_TITLE, report.getId()),
                this.messageLoader.get(MessageKeys.UI_REPORT_CONTENT, report.getId(), report.getCreator(), report.getTarget(), report.getReason(),
                        report.getStatus().getName(), report.getModerator(), report.getDate()));

        if (report.getModerator().equals("Unknown") && requester.hasPermission("proreports.role.moderator")) {
            if (report.getStatus() == Report.Status.PENDING) {
                form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_BUTTON_TAKE), new ElementButtonImageData("url", "")), submitter -> {
                    this.proReports.getReportService().updateModerator(report.getId(), submitter.getName());
                    this.proReports.getReportService().updateStatus(report.getId(), Report.Status.IN_PROGRESS);
                    submitter.sendMessage(this.messageLoader.get(MessageKeys.REPORT_MANAGEMENT_TAKE_REPORT, report.getId()));
                });
            }
        }

        if (report.getModerator().equals(requester.getName()) && requester.hasPermission("proreports.role.moderator")) {
            if (report.getStatus() == Report.Status.IN_PROGRESS) {
                form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_BUTTON_SOLVE), new ElementButtonImageData("url", "")), submitter -> {
                    this.proReports.getReportService().closeReport(report.getId());
                    submitter.sendMessage(this.messageLoader.get(MessageKeys.REPORT_MANAGEMENT_SOLVE_REPORT, report.getId()));
                });
            }
        }

        if (report.getCreator().equals(requester.getName())) {
            if (report.getStatus() != Report.Status.CLOSED) {
                form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_REPORT_BUTTON_CLOSE), new ElementButtonImageData("url", "")), submitter -> {
                    this.proReports.getReportService().closeReport(report.getId());
                    submitter.sendMessage(this.messageLoader.get(MessageKeys.REPORT_CLOSED, report.getId()));
                });
            }
        }

        if (requester.hasPermission("proreports.role.moderator") && !report.getTarget().equals(requester.getName())) {
            form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_BUTTON_BACK), new ElementButtonImageData("url", "")), this::openReportManagement);
        } else {
            form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_BUTTON_BACK), new ElementButtonImageData("url", "")), this::openMyReports);
        }

        form.build().show(requester);
    }

    public void openMyReports(final Player requester) {
        final CustomForm form = new CustomForm.Builder(this.messageLoader.get(MessageKeys.UI_MY_REPORTS_TITLE))
                .addElement(new ElementDropdown(this.messageLoader.get(MessageKeys.UI_MY_REPORTS_INFO), Arrays.asList("Pending", "In Progress", "Closed"), 0))
                .onSubmit((submitter, response) -> {
                    final Report.Status status = Report.Status.valueOf(response.getDropdownResponse(0).getElementContent().replace(" ", "_").toUpperCase());
                    this.openResultMyReports(submitter, status);
                })
                .build();
        form.show(requester);
    }

    public void openResultMyReports(final Player requester, final Report.Status status) {
        final SimpleForm.Builder form = new SimpleForm.Builder(this.messageLoader.get(MessageKeys.UI_MY_REPORTS_RESULT_TITLE), this.messageLoader.get(MessageKeys.UI_MY_REPORTS_RESULT_CONTENT));

        this.proReports.getReportService().getReports(status, Report.SearchType.BY_CREATOR, requester.getName(), reports -> {
            /*if (reports.isEmpty()) {
                requester.sendMessage(this.messageLoader.get(MessageKeys.REPORT_MANAGEMENT_ERROR_RESULT));
                return;
            }*/
            reports.forEach(report -> {
                form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_MY_REPORTS_RESULT_REPORT, report.getId(), report.getCreator(), report.getTarget(), report.getReason())),
                        submitter -> {
                            this.openReport(submitter, report);
                        });
            });
        });
        form.addElement(new ElementButton(this.messageLoader.get(MessageKeys.UI_BUTTON_BACK), new ElementButtonImageData("url", "")), this::openMyReports);

        form.build().show(requester);
    }

}
