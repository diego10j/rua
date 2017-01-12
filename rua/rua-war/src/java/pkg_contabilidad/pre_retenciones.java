/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_retenciones extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Boton bot_ver_retencion = new Boton();
    private VisualizarPDF vpdf_ver = new VisualizarPDF();
    private AutoCompletar aut_factura_cxp = new AutoCompletar();
    private Boton bot_clean = new Boton();

    public pre_retenciones() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.agregarReporte();
        bar_botones.agregarBoton(bot_ver_retencion);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("con_cabece_retenc", "ide_cncre", 1);
        tab_tabla1.getColumna("ide_cnere").setCombo("con_estado_retenc", "ide_cnere", "nombre_cnere", "");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.getColumna("numero_cncre").setEstilo("font-size: 16px;font-weight: bold");
        tab_tabla1.getColumna("autorizacion_cncre").setEstilo("font-size: 16px;font-weight: bold");
        tab_tabla1.getColumna("autorizacion_cncre").setEstilo("font-size: 16px;font-weight: bold");
        tab_tabla1.getColumna("es_venta_cncre").setVisible(false);
        tab_tabla1.getColumna("numero_cncre").setNombreVisual("NUMERO DE RETENCION");
        tab_tabla1.getColumna("autorizacion_cncre").setNombreVisual("NUMERO DE AUTORIZACION");
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("TRANSACCION");
        tab_tabla1.getColumna("fecha_emisi_cncre").setNombreVisual("FECHA DE EMISION");
        tab_tabla1.getColumna("observacion_cncre").setNombreVisual("OBSERVACION");
        tab_tabla1.getGrid().setColumns(4);
        //tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("con_detall_retenc", "ide_cndre", 2);
        tab_tabla2.setCampoForanea("ide_cncre");
        // tab_tabla2.setLectura(true);
        tab_tabla2.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
        tab_tabla2.getColumna("ide_cncim").setAutoCompletar();

        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        bot_ver_retencion.setValue("Ver Retención");
        bot_ver_retencion.setMetodo("verComprobante");
        vpdf_ver.setId("vpdf_ver");
        vpdf_ver.setTitle("Comprobante de Retención");

        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "65%", "H");

        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sef_formato.setId("sef_formato");

        aut_factura_cxp.setId("aut_factura_cxp");
        aut_factura_cxp.setAutoCompletar("SELECT cf.ide_cnccc,cf.ide_cnccc,cf.numero_cpcfa,gp.nom_geper FROM cxp_cabece_factur cf "
                + "LEFT JOIN gen_persona gp ON cf.ide_geper=gp.ide_geper "
                + "WHERE cf.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "AND cf.ide_sucu=" + utilitario.getVariable("ide_empr") + "");
        aut_factura_cxp.setMetodoChange("buscarFacturaCxp");
        bar_botones.agregarComponente(new Etiqueta("Factura Proveedor: "));
        bar_botones.agregarComponente(aut_factura_cxp);

        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarComponente(bot_clean);

        agregarComponente(div_division);
        agregarComponente(sef_formato);
        agregarComponente(rep_reporte);
        agregarComponente(vpdf_ver);
    }

    public void buscarFacturaCxp() {
        if (aut_factura_cxp != null) {
            TablaGenerica tab_retencion = utilitario.consultar("SELECT * FROM cxp_cabece_factur WHERE ide_cnccc=" + aut_factura_cxp.getValor());
            if (tab_retencion.getTotalFilas() > 0) {
                if (tab_retencion.getValor(0, "ide_cncre") != null && !tab_retencion.getValor(0, "ide_cncre").isEmpty()) {
                    tab_tabla1.setFilaActual(tab_retencion.getValor(0, "ide_cncre"));
                    tab_tabla2.ejecutarValorForanea(tab_retencion.getValor(0, "ide_cncre"));
                    utilitario.addUpdate("tab_tabla1,tab_tabla2");
                } else {
                    utilitario.agregarMensajeInfo("Atencion ", "No existe Comprobante de retencion de la Factura seleccionada");
                }
            } else {
                utilitario.agregarMensajeInfo("Atencion ", "No existe Comprobante de retencion de la Factura seleccionada");
            }
        }
    }

    public void limpiar() {
        if (aut_factura_cxp.getValue() != null) {
            aut_factura_cxp.setValue(null);
            tab_tabla1.setCondicion("ide_cncre>=0");
            tab_tabla1.ejecutarSql();
            utilitario.addUpdate("tab_tabla1,tab_tabla2,aut_factura_cxp");
        }
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra   
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista
        if (rep_reporte.getReporteSelecionado().equals("Comprobante de Retención")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cncre", Long.parseLong(tab_tabla1.getValor("ide_cncre")));
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("sef_formato,rep_reporte");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sef_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sef_formato");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene compraqbante de contabilidad");
                }

            }
        }
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void verComprobante() {

        System.out.println("Si funciona");
        if (tab_tabla1.getValorSeleccionado() != null) {
            if (!tab_tabla1.isFilaInsertada()) {
                Map parametros = new HashMap();
                parametros.put("ide_cncre", Long.parseLong(tab_tabla1.getValorSeleccionado()));
                vpdf_ver.setVisualizarPDF("rep_contabilidad/rep_comprobante_retencion_1.jasper", parametros);
                vpdf_ver.dibujar();
                utilitario.addUpdate("vpdf_ver");
            } else {
                utilitario.agregarMensajeInfo("Debe guardar el comprobante", "");
            }

        } else {
            utilitario.agregarMensajeInfo("No hay ningun comprobante seleccionado", "");
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public VisualizarPDF getVpdf_ver() {
        return vpdf_ver;
    }

    public void setVpdf_ver(VisualizarPDF vpdf_ver) {
        this.vpdf_ver = vpdf_ver;
    }

    public AutoCompletar getAut_factura_cxp() {
        return aut_factura_cxp;
    }

    public void setAut_factura_cxp(AutoCompletar aut_factura_cxp) {
        this.aut_factura_cxp = aut_factura_cxp;
    }
}
