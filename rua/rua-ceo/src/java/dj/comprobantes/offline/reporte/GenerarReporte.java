/*
 *********************************************************************
 Objetivo: Clase con los métodos para generar el archivo pdf
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.reporte;


import dj.comprobantes.offline.enums.ParametrosSistemaEnum;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author diego.jacome
 */
public class GenerarReporte {

    private ReporteDataSource dataSource;
    private JasperPrint jasperPrint;

    /**
     * Crea el archivo pdf con formato RIDE de comprobantes elctronicos Offline
     *
     * @param parametros
     * @param reporte
     * @param nombreReporte
     * @return
     */
    public File crearPDF(Map<String, Object> parametros, String reporte, String nombreReporte) {
        try {
            if (parametros == null) {
                parametros = new HashMap<>();
            }            
            jasperPrint = JasperFillManager.fillReport(ParametrosSistemaEnum.RUTA_SISTEMA.getCodigo() + "/reportes/" + reporte, parametros, dataSource);
            dataSource.setIndice(-1);
            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING.JASPER_PRINT, jasperPrint);
            File fil_reporte = new File(nombreReporte + ".pdf");
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
            exporter.exportReport();
            fil_reporte.deleteOnExit();
            return fil_reporte;
        } catch (Exception ex) {
            System.out.println("error" + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public ReporteDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ReporteDataSource dataSource) {
        this.dataSource = dataSource;
    }

}
