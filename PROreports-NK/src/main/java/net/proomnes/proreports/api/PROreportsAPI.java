package net.proomnes.proreports.api;

import lombok.Getter;
import net.proomnes.proreports.dataaccess.IDataAccess;
import net.proomnes.proreports.services.ReportService;
import net.proomnes.proreports.util.forms.FormWindows;

@Getter
public class PROreportsAPI {

    private static IDataAccess iDataAccess = null;
    private static ReportService reportService = null;
    private static FormWindows formWindows = null;

    public PROreportsAPI(final IDataAccess iDataAccess, final ReportService reportService, final FormWindows formWindows) {
        PROreportsAPI.iDataAccess = iDataAccess;
        PROreportsAPI.reportService = reportService;
        PROreportsAPI.formWindows = formWindows;
    }

}
