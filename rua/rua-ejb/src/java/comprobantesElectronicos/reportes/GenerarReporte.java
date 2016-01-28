/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.reportes;

import framework.reportes.ReporteDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
public class GenerarReporte {

    private ReporteDataSource dataSource;
    private JasperPrint jasperPrint;

    public void generarPDF(Map parametros, String reporte, String nombreReporte) {
        try {
            generarReporte(parametros, reporte);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nombreReporte + ".pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException | JRException ex) {
            System.out.println("Error generarPDF() :" + ex.getMessage());
        }
    }

    public File crearPDF(Map parametros, String reporte, String nombreReporte) {
        try {

            //  generarReporte(parametros, reporte);
            // jasperPrint = JasperFillManager.fillReport("C:/Users/dfjacome/Desktop/SRI/EAR/comprobantesElectronicos/comprobantesElectronicos-war/web"+reporte, parametros, dataSource);
            jasperPrint = JasperFillManager.fillReport("/opt/contingenciaSRI" + reporte, parametros, dataSource);
            dataSource.setIndice(-1);
            //parametros.put("SUBREPORT_DIR", "/opt/contingenciaSRI");
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING.JASPER_PRINT, jasperPrint);
            File fil_reporte = new File(nombreReporte + ".pdf");
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
            exporter.exportReport();
            //  System.out.println(" Reporte Generado " + fil_reporte);  
            return fil_reporte;
        } catch (Exception ex) {
            System.out.println("error" + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private void generarReporte(Map parametros, String reporte) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();

            ExternalContext ec = fc.getExternalContext();
            InputStream fis = ec.getResourceAsStream(reporte);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fis);
            if (parametros == null) {
                parametros = new HashMap();
            }

            parametros.put("SUBREPORT_DIR", getURL());
            if (dataSource == null) {
                Utilitario utilitario = new Utilitario();
                utilitario.getConexion().conectar(false);
                jasperPrint = JasperFillManager.fillReport(
                        jasperReport, parametros, utilitario.getConexion().getConnection());
                utilitario.getConexion().desconectar(false);
            } else {
                jasperPrint = JasperFillManager.fillReport(
                        jasperReport, parametros, dataSource);
                dataSource.setIndice(-1);
            }
        } catch (Exception e) {
            System.out.println("ERROR generarReporte" + e.getMessage());
            e.printStackTrace();
        }
    }

    public ReporteDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ReporteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getURL() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        String path = request.getRequestURL() + "";
        path = path.substring(0, path.lastIndexOf("/"));
        return path;
    }

}
