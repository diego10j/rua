/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import componentes.DocumentoCxP;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_documentosCxP extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private final Combo com_tipo_documento = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private DocumentoCxP dcp_documento = new DocumentoCxP();
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);

    private Tabla tab_tabla1 = new Tabla();

    public pre_documentosCxP() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();

        com_tipo_documento.setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        com_tipo_documento.setMetodo("filrarTipoDocumento");

        bar_botones.agregarComponente(new Etiqueta("TIPO DE DOCUMENTO :"));
        bar_botones.agregarComponente(com_tipo_documento);

        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);

        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarFacturas");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        dcp_documento.setId("dcp_documento");
        dcp_documento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(dcp_documento);

        mep_menu.setMenuPanel("OPCIONES DOCUMENTOS POR PAGAR", "20%");
        mep_menu.agregarItem("Listado de Documentos CxP ", "dibujarDocumentos", "ui-icon-note");
        mep_menu.agregarItem("Generar Comprobante Contabilidad", "dibujarDocumentosNoContabilizadas", "ui-icon-notice");
        mep_menu.agregarItem("Generar Comprobante Retención", "dibujarDocumentosNoRetencion", "ui-icon-notice");
        mep_menu.agregarItem("Documentos Anulados", "dibujarDocumentosAnulados", "ui-icon-cancel");
        mep_menu.agregarItem("Documentos Por Pagar", "dibujarDocumentosPorPagar", "ui-icon-calculator");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Grafico de Compras", "dibujarGraficoVentas", "ui-icon-clock");
        // mep_menu.agregarItem("Estadística de Ventas", "dibujarEstadisticas", "ui-icon-bookmark");        
        mep_menu.agregarSubMenu("FACTURAS ELECTRONICAS");
        mep_menu.agregarItem("Ingresar Factura ", "dibujarFacturaElectronica", "ui-icon-signal-diag");
        agregarComponente(mep_menu);
        dibujarDocumentos();
    }

    public void dibujarDocumentos() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[2] eq '" + utilitario.getVariable("p_cxc_estado_factura_anulada") + "' ? 'text-red' : fila.campos[13] eq null  ? 'text-green' : null");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "LISTADO DE DOCUMENTOS POR PAGAR", gru);
    }

    public void dibujarDocumentosNoContabilizadas() {

    }

    public void dibujarDocumentosNoRetencion() {

    }

    public void dibujarDocumentosAnulados() {

    }

    public void dibujarDocumentosPorPagar() {

    }

    public void dibujarGraficoVentas() {

    }

    public void dibujarFacturaElectronica() {

    }

    public void filrarTipoDocumento() {
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.ejecutarSql();
    }

    @Override
    public void insertar() {
        dcp_documento.nuevoDocumento();
        dcp_documento.dibujar();
    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {

    }

    public DocumentoCxP getDcp_documento() {
        return dcp_documento;
    }

    public void setDcp_documento(DocumentoCxP dcp_documento) {
        this.dcp_documento = dcp_documento;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

}
