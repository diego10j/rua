/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inversiones;

import componentes.AsientoContable;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.DateSelectEvent;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.inversiones.ServicioInversiones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacomee
 */
public class pre_certificados extends Pantalla {

    @EJB
    private final ServicioInversiones ser_inversion = (ServicioInversiones) utilitario.instanciarEJB(ServicioInversiones.class);
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private final Combo com_tipo_inversion = new Combo();

    private Tabla tab_tabla1;

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();
    private String num_certificado_ipcer_busca = null;
    private AsientoContable asc_asiento = new AsientoContable();
    private String ide_incci_busca = null;

    public pre_certificados() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarReporte();

        bar_botones.agregarComponente(new Etiqueta("TIPO DE INVERSIÓN :"));
        com_tipo_inversion.setId("com_tipo_inversion");
        com_tipo_inversion.setCombo(ser_inversion.getSqlTipoInversionesCombo());
        com_tipo_inversion.setMetodo("seleccionarTipoInversion");
        bar_botones.agregarComponente(com_tipo_inversion);

        mep_menu.setMenuPanel("OPCIONES IVERSIONES", "20%");
        mep_menu.agregarItem("Certificado de Inversión", "dibujarCertificado", "ui-icon-contact");
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarAsiento", "ui-icon-notice");
        mep_menu.agregarItem("Generar Asiento de Iteres", "dibujarAsientoInteres", "ui-icon-notice");
        mep_menu.agregarItem("Generar Asiento de Terminación", "dibujarAsientoTermina", "ui-icon-notice");
        mep_menu.agregarItem("Listado de Inversiones", "dibujarListado", "ui-icon-note");
        mep_menu.agregarSubMenu("VENCIDAS");
        mep_menu.agregarItem("Inversiones Vencidas", "dibujarVencidas", "ui-icon-calculator");

        agregarComponente(mep_menu);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_formato.setId("sel_formato");
        agregarComponente(rep_reporte);
        agregarComponente(sel_formato);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(asc_asiento);

        dibujarListado();
    }

    public void seleccionarTipoInversion() {
        if (mep_menu.getOpcion() == 2) {
            tab_tabla1.setSql(ser_inversion.getSqlCertificadosSinAsiento(String.valueOf(com_tipo_inversion.getValue())));
            tab_tabla1.ejecutarSql();
        } else if (mep_menu.getOpcion() == 3) {
            tab_tabla1.setSql(ser_inversion.getSqlCertificadosSinAsientoInteres(String.valueOf(com_tipo_inversion.getValue())));
            tab_tabla1.ejecutarSql();
        } else if (mep_menu.getOpcion() == 4) {
            tab_tabla1.setSql(ser_inversion.getSqlListaCertificados(String.valueOf(com_tipo_inversion.getValue())));
            tab_tabla1.ejecutarSql();
        } else if (mep_menu.getOpcion() == 5) {
            tab_tabla1.setSql(ser_inversion.getSqlListaCertificadosVencidos(String.valueOf(com_tipo_inversion.getValue())));
            tab_tabla1.ejecutarSql();
        } else if (mep_menu.getOpcion() == 6) {
            tab_tabla1.setSql(ser_inversion.getSqlCertificadosSinAsientoTerminacion(String.valueOf(com_tipo_inversion.getValue())));
            tab_tabla1.ejecutarSql();
        }

    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Certificados de Inversiones")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_iptin") != null && !tab_tabla1.getValor("ide_iptin").isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();

                    if (tab_tabla1.getValor("ide_cnccc") != null && !tab_tabla1.getValor("ide_cnccc").isEmpty()) {
                        parametro.put("ide_cnccc", tab_tabla1.getValor("ide_cnccc") + "");
                    } else {
                        parametro.put("ide_cnccc", "-1");
                    }

                    parametro.put("ide_ipcer", Long.parseLong(tab_tabla1.getValor("ide_ipcer")));
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene Certificados de Inversion");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad Interes")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc_interes") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc_interes")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad de interes");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad Termino Inversión")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc_terminacion") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc_terminacion")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_formato.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad de termino de inversión");
                }
            }
        }
    }

    public void dibujarCertificado() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("iyp_certificado", "ide_ipcer", 1);
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_ipcin").setCombo("iyp_clase_inversion", "ide_ipcin", "nombre_ipcin", "");
        tab_tabla1.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cntcu=" + utilitario.getVariable("p_iyp_pasivo"));
        tab_tabla1.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla1.getColumna("es_inver_banco_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_ipein").setCombo("iyp_estado_inversion", "ide_ipein", "nombre_ipein", "");
        tab_tabla1.getColumna("ide_ipein").setValorDefecto(utilitario.getVariable("p_iyp_estado_activo_inversion"));
        tab_tabla1.getColumna("ide_iptin").setCombo("iyp_tipo_inversion", "ide_iptin", "nombre_iptin", "");
        tab_tabla1.getColumna("ide_iptin").setRequerida(true);
        tab_tabla1.getColumna("con_ide_cndpc").setVisible(false);
        tab_tabla1.getColumna("ide_cnmod").setCombo("con_moneda", "ide_cnmod", "nombre_cnmod", "");
        tab_tabla1.getColumna("ide_cnmod").setPermitirNullCombo(false);
        tab_tabla1.getColumna("capital_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("tasa_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("plazo_ipcer").setMetodoChange("calcularInteres");
        tab_tabla1.getColumna("interes_ipcer").setEtiqueta();
        tab_tabla1.getColumna("interes_ipcer").setEstilo("font-size:13px;font-weight: bold;");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setEtiqueta();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setEstilo("font-size:12px;font-weight: bold;");
        tab_tabla1.getColumna("num_certificado_ipcer").setEtiqueta();
        tab_tabla1.getColumna("num_certificado_ipcer").setEstilo("font-size:14px;font-weight: bold;");
        tab_tabla1.getColumna("fecha_emision_ipcer").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_emision_ipcer").setMetodoChange("obtenetFechaVencimiento");
        tab_tabla1.getColumna("fecha_vence_ipcer").setLectura(true);
        tab_tabla1.getColumna("ide_ipcin").setPermitirNullCombo(false);
        tab_tabla1.setMostrarNumeroRegistros(false);
        if (num_certificado_ipcer_busca == null) {
            tab_tabla1.setCondicion("num_certificado_ipcer='-1'");
        } else {
            tab_tabla1.setCondicion("num_certificado_ipcer='" + num_certificado_ipcer_busca + "'");
        }

        tab_tabla1.getColumna("ide_cnccc").setLectura(true);
        tab_tabla1.getColumna("ide_cnccc_interes").setLectura(true);
        tab_tabla1.getColumna("ide_cnccc_terminacion").setLectura(true);
        tab_tabla1.getColumna("ide_teclb").setLectura(true);
        tab_tabla1.getColumna("ide_cpdtr").setLectura(true);
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("ide_usua").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("fecha_sistema_ipcer").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("hora_sistema_ipcer").setValorDefecto(utilitario.getHoraActual());
        tab_tabla1.getColumna("fecha_sistema_ipcer").setVisible(false);
        tab_tabla1.getColumna("hora_sistema_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setRequerida(true);
        tab_tabla1.getColumna("ide_ipein").setRequerida(true);
//            tab_tabla1.getColumna("ide_cndpc").setRequerida(true);
        tab_tabla1.getColumna("observacion_ipcer").setRequerida(true);
        tab_tabla1.getColumna("ide_cnmod").setRequerida(true);
        tab_tabla1.getColumna("ide_ipcin").setRequerida(true);
        tab_tabla1.getColumna("es_renovacion_ipcer").setValorDefecto("false");
        tab_tabla1.getColumna("hace_asiento_ipcer").setValorDefecto("false");
        tab_tabla1.getColumna("hace_asiento_ipcer").setVisible(true);
        tab_tabla1.getColumna("ide_teclb").setVisible(false);
        tab_tabla1.getColumna("ide_cpdtr").setVisible(false);
        tab_tabla1.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));

        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        //tab_tabla1.setRecuperarLectura(true);
        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
        }

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(1, "CERTIFICADO DE INVERSIÓN", pat_panel);
    }

    public void dibujarAsiento() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlCertificadosSinAsiento(String.valueOf(com_tipo_inversion.getValue())));
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_iptin").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("nombre_iptin").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nombre_iptin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipein").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_ipein").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setNombreVisual("NUM. CERTIFICADO");
        tab_tabla1.getColumna("num_certificado_ipcer").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setLink();
        tab_tabla1.getColumna("num_certificado_ipcer").setMetodoChange("cargarCertificado");
        tab_tabla1.getColumna("num_certificado_ipcer").alinearCentro();
        tab_tabla1.getColumna("nombre_ipcin").setNombreVisual("CLASE");
        tab_tabla1.getColumna("nombre_ipcin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipcin").setLongitud(-1);
        tab_tabla1.getColumna("observacion_ipcer").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("fecha_emision_ipcer").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("plazo_ipcer").setNombreVisual("PLAZO");
        tab_tabla1.getColumna("capital_ipcer").setNombreVisual("CAPITAL");
        tab_tabla1.getColumna("interes_ipcer").setNombreVisual("INTERES");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setNombreVisual("VALOR A PAGAR");

        tab_tabla1.getColumna("plazo_ipcer").alinearDerecha();
        tab_tabla1.getColumna("capital_ipcer").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcer").alinearDerecha();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").alinearDerecha();

        tab_tabla1.getColumna("fecha_vence_ipcer").setNombreVisual("FECHA VENCE");
        tab_tabla1.getColumna("cod_captacion_ipcer").setNombreVisual("COD. CAPTACIÓN");

        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "GENERAR ASIENTO CONTABLE", gru);

    }

    public void dibujarAsientoInteres() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable del Interes");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlCertificadosSinAsientoInteres(String.valueOf(com_tipo_inversion.getValue())));
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_iptin").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("nombre_iptin").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nombre_iptin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipein").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_ipein").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setNombreVisual("NUM. CERTIFICADO");
        tab_tabla1.getColumna("num_certificado_ipcer").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setLink();
        tab_tabla1.getColumna("num_certificado_ipcer").setMetodoChange("cargarCertificado");
        tab_tabla1.getColumna("num_certificado_ipcer").alinearCentro();
        tab_tabla1.getColumna("nombre_ipcin").setNombreVisual("CLASE");
        tab_tabla1.getColumna("nombre_ipcin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipcin").setLongitud(-1);
        tab_tabla1.getColumna("observacion_ipcer").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("fecha_emision_ipcer").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("plazo_ipcer").setNombreVisual("PLAZO");
        tab_tabla1.getColumna("capital_ipcer").setNombreVisual("CAPITAL");
        tab_tabla1.getColumna("interes_ipcer").setNombreVisual("INTERES");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setNombreVisual("VALOR A PAGAR");

        tab_tabla1.getColumna("plazo_ipcer").alinearDerecha();
        tab_tabla1.getColumna("capital_ipcer").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcer").alinearDerecha();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").alinearDerecha();

        tab_tabla1.getColumna("fecha_vence_ipcer").setNombreVisual("FECHA VENCE");
        tab_tabla1.getColumna("cod_captacion_ipcer").setNombreVisual("COD. CAPTACIÓN");

        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "GENERAR ASIENTO CONTABLE DEL INTERES", gru);

    }

    public void dibujarAsientoTermina() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable de Termincación");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlCertificadosSinAsientoTerminacion(String.valueOf(com_tipo_inversion.getValue())));
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_iptin").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("nombre_iptin").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nombre_iptin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipein").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_ipein").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setNombreVisual("NUM. CERTIFICADO");
        tab_tabla1.getColumna("num_certificado_ipcer").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setLink();
        tab_tabla1.getColumna("num_certificado_ipcer").setMetodoChange("cargarCertificado");
        tab_tabla1.getColumna("num_certificado_ipcer").alinearCentro();
        tab_tabla1.getColumna("nombre_ipcin").setNombreVisual("CLASE");
        tab_tabla1.getColumna("nombre_ipcin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipcin").setLongitud(-1);
        tab_tabla1.getColumna("observacion_ipcer").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("fecha_emision_ipcer").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("plazo_ipcer").setNombreVisual("PLAZO");
        tab_tabla1.getColumna("capital_ipcer").setNombreVisual("CAPITAL");
        tab_tabla1.getColumna("interes_ipcer").setNombreVisual("INTERES");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setNombreVisual("VALOR A PAGAR");

        tab_tabla1.getColumna("plazo_ipcer").alinearDerecha();
        tab_tabla1.getColumna("capital_ipcer").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcer").alinearDerecha();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").alinearDerecha();

        tab_tabla1.getColumna("fecha_vence_ipcer").setNombreVisual("FECHA VENCE");
        tab_tabla1.getColumna("cod_captacion_ipcer").setNombreVisual("COD. CAPTACIÓN");

        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "GENERAR ASIENTO CONTABLE DE TERMINACIÓN", gru);

    }

    public void dibujarListado() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaCertificados(String.valueOf(com_tipo_inversion.getValue())));
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_iptin").setVisible(false);
        tab_tabla1.getColumna("nombre_iptin").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nombre_iptin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipein").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_ipein").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setNombreVisual("NUM. CERTIFICADO");
        tab_tabla1.getColumna("num_certificado_ipcer").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setLink();
        tab_tabla1.getColumna("num_certificado_ipcer").setMetodoChange("cargarCertificado");
        tab_tabla1.getColumna("num_certificado_ipcer").alinearCentro();
        tab_tabla1.getColumna("nombre_ipcin").setNombreVisual("CLASE");
        tab_tabla1.getColumna("nombre_ipcin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipcin").setLongitud(-1);
        tab_tabla1.getColumna("observacion_ipcer").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("fecha_emision_ipcer").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("plazo_ipcer").setNombreVisual("PLAZO");
        tab_tabla1.getColumna("capital_ipcer").setNombreVisual("CAPITAL");
        tab_tabla1.getColumna("interes_ipcer").setNombreVisual("INTERES");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setNombreVisual("VALOR A PAGAR");

        tab_tabla1.getColumna("plazo_ipcer").alinearDerecha();
        tab_tabla1.getColumna("capital_ipcer").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcer").alinearDerecha();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").alinearDerecha();

        tab_tabla1.getColumna("fecha_vence_ipcer").setNombreVisual("FECHA VENCE");
        tab_tabla1.getColumna("cod_captacion_ipcer").setNombreVisual("COD. CAPTACIÓN");

        tab_tabla1.getColumna("ide_cnccc_terminacion").setNombreVisual("ASIENTO TERM.");
        tab_tabla1.getColumna("ide_cnccc_terminacion").setLink();
        tab_tabla1.getColumna("ide_cnccc_terminacion").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("ide_cnccc_terminacion").alinearCentro();

        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();

        tab_tabla1.getColumna("ide_cnccc_interes").setNombreVisual("ASIENTO INTE.");
        tab_tabla1.getColumna("ide_cnccc_interes").setLink();
        tab_tabla1.getColumna("ide_cnccc_interes").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("ide_cnccc_interes").alinearCentro();

        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(4, "LISTADO DE INVERSIONES", pat_panel);

    }

    public void dibujarVencidas() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inversion.getSqlListaCertificadosVencidos(String.valueOf(com_tipo_inversion.getValue())));
        tab_tabla1.setLectura(true);
        tab_tabla1.setCampoPrimaria("ide_ipcer");
        tab_tabla1.getColumna("ide_ipcer").setVisible(false);
        tab_tabla1.getColumna("ide_iptin").setVisible(false);
        tab_tabla1.getColumna("nombre_iptin").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nombre_iptin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipein").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_ipein").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setNombreVisual("NUM. CERTIFICADO");
        tab_tabla1.getColumna("num_certificado_ipcer").setFiltroContenido();
        tab_tabla1.getColumna("num_certificado_ipcer").setLink();
        tab_tabla1.getColumna("num_certificado_ipcer").setMetodoChange("cargarCertificado");
        tab_tabla1.getColumna("num_certificado_ipcer").alinearCentro();
        tab_tabla1.getColumna("nombre_ipcin").setNombreVisual("CLASE");
        tab_tabla1.getColumna("nombre_ipcin").setFiltroContenido();
        tab_tabla1.getColumna("nombre_ipcin").setLongitud(-1);
        tab_tabla1.getColumna("observacion_ipcer").setNombreVisual("OBSERVACIÓN");
        tab_tabla1.getColumna("fecha_emision_ipcer").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("plazo_ipcer").setNombreVisual("PLAZO");
        tab_tabla1.getColumna("capital_ipcer").setNombreVisual("CAPITAL");
        tab_tabla1.getColumna("interes_ipcer").setNombreVisual("INTERES");
        tab_tabla1.getColumna("valor_a_pagar_ipcer").setNombreVisual("VALOR A PAGAR");
        tab_tabla1.getColumna("plazo_ipcer").alinearDerecha();
        tab_tabla1.getColumna("capital_ipcer").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcer").alinearDerecha();
        tab_tabla1.getColumna("valor_a_pagar_ipcer").alinearDerecha();
        tab_tabla1.getColumna("fecha_vence_ipcer").setNombreVisual("FECHA VENCE");
        tab_tabla1.getColumna("cod_captacion_ipcer").setNombreVisual("COD. CAPTACIÓN");

        tab_tabla1.getColumna("ide_cnccc_terminacion").setNombreVisual("ASIENTO TERM.");
        tab_tabla1.getColumna("ide_cnccc_terminacion").setLink();
        tab_tabla1.getColumna("ide_cnccc_terminacion").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("ide_cnccc_terminacion").alinearCentro();

        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();

        tab_tabla1.getColumna("ide_cnccc_interes").setNombreVisual("ASIENTO INTE.");
        tab_tabla1.getColumna("ide_cnccc_interes").setLink();
        tab_tabla1.getColumna("ide_cnccc_interes").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("ide_cnccc_interes").alinearCentro();

        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(5, "INVERSIONES VENCIDAS AL " + utilitario.getFechaLarga(utilitario.getFechaActual()).toUpperCase(), pat_panel);
    }

    public void cargarCertificado(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        num_certificado_ipcer_busca = lin_ide_cnccc.getValue().toString();
        dibujarCertificado();
    }

    public void calcularInteres(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        calculainteres();
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla1.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    public void calculainteres() {
        double capital = 0;
        double tasa = 0;
        double plazo = 0;
        double interes = 0;
        double valortotal = 0;
        if (tab_tabla1.getValor("capital_ipcer") != null && !tab_tabla1.getValor("capital_ipcer").isEmpty()) {
            try {
                capital = Double.parseDouble(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "capital_ipcer"));
            } catch (Exception e) {
                capital = 0;
            }
        }
        if (tab_tabla1.getValor("tasa_ipcer") != null && !tab_tabla1.getValor("tasa_ipcer").isEmpty()) {
            try {
                tasa = Double.parseDouble(tab_tabla1.getValor("tasa_ipcer"));

            } catch (Exception e) {
                tasa = 0;
            }
        }
        if (tab_tabla1.getValor("plazo_ipcer") != null && !tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
            plazo = Double.parseDouble(tab_tabla1.getValor("plazo_ipcer"));
            obtenetFechaVencimiento();
        } else {
            plazo = 0;
            obtenetFechaVencimiento();
        }
        interes = (capital * tasa * plazo) / 36000;
        valortotal = capital + interes;
        tab_tabla1.setValor("interes_ipcer", utilitario.getFormatoNumero(interes));
        tab_tabla1.setValor("valor_a_pagar_ipcer", utilitario.getFormatoNumero(valortotal));
        utilitario.addUpdateTabla(tab_tabla1, "interes_ipcer,valor_a_pagar_ipcer", "");
    }

    public boolean obtenetFechaVencimiento() {
        if (tab_tabla1.getValor("plazo_ipcer") != null && !tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
            System.out.println("SI ENTRA..." + utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla1.getValor("plazo_ipcer"))));
            System.out.println("SI ENTRA..." + utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla1.getValor("plazo_ipcer")))));
            tab_tabla1.setValor("fecha_vence_ipcer", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_emision_ipcer")), Integer.parseInt(tab_tabla1.getValor("plazo_ipcer")))));
            utilitario.addUpdateTabla(tab_tabla1, "fecha_vence_ipcer", "");
            return true;
        } else {
            tab_tabla1.setValor("fecha_vence_ipcer", "");
            utilitario.addUpdateTabla(tab_tabla1, "fecha_vence_ipcer", "");
            return false;
        }
    }

    public void obtenetFechaVencimiento(DateSelectEvent evt) {
//        tab_renovacion_inversion.modificar(evt);
        if (obtenetFechaVencimiento()) {
            calculainteres();
        }
    }

    @Override
    public void insertar() {
        //FORMULARIO INVERSION
        num_certificado_ipcer_busca = null;
        dibujarCertificado();

        tab_tabla1.setValor("ide_iptin", String.valueOf(com_tipo_inversion.getValue()));
    }

    @Override
    public void guardar() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                if (mep_menu.getOpcion() == 2) {
                    utilitario.getConexion().ejecutarSql("UPDATE iyp_certificado SET ide_cncc=" + asc_asiento.getIde_cnccc() + " where ide_ipcer=" + tab_tabla1.getValor("ide_ipcer"));
                } else if (mep_menu.getOpcion() == 3) {
                    utilitario.getConexion().ejecutarSql("UPDATE iyp_certificado SET ide_cncc_interes=" + asc_asiento.getIde_cnccc() + " where ide_ipcer=" + tab_tabla1.getValor("ide_ipcer"));
                } else if (mep_menu.getOpcion() == 6) {
                    utilitario.getConexion().ejecutarSql("UPDATE iyp_certificado SET ide_cnccc_terminacion=" + asc_asiento.getIde_cnccc() + " where ide_ipcer=" + tab_tabla1.getValor("ide_ipcer"));
                }
                dibujarListado();
            }
        } else {
            if (mep_menu.getOpcion() == 1) {
                if (tab_tabla1.isFilaInsertada() || tab_tabla1.isFilaModificada()) {
                    if (validarCertificado()) {
                        if (tab_tabla1.isFilaInsertada()) {
                            tab_tabla1.setValor("num_certificado_ipcer", ser_inversion.getSecuenciaCertificado(tab_tabla1.getValor("ide_iptin")));
                            tab_tabla1.setValor("hora_sistema_ipcer", utilitario.getHoraActual());
                        }
                    } else {
                        return;
                    }
                }
                if (tab_tabla1.guardar()) {
                    guardarPantalla();
                }
            }

        }
    }

    public boolean validarCertificado() {
        if (tab_tabla1.getValor("capital_ipcer") == null || tab_tabla1.getValor("capital_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el capital");
            return false;
        }
        if (tab_tabla1.getValor("plazo_ipcer") == null || tab_tabla1.getValor("plazo_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar el plazo");
            return false;
        }

        if (tab_tabla1.getValor("tasa_ipcer") == null || tab_tabla1.getValor("tasa_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la tasa de interes");
            return false;
        }

        if (tab_tabla1.getValor("fecha_emision_ipcer") == null || tab_tabla1.getValor("fecha_emision_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la fecha de emisión");
            return false;
        }
        if (tab_tabla1.getValor("observacion_ipcer") == null || tab_tabla1.getValor("observacion_ipcer").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe ingresar la observación");
            return false;
        }
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el Beneficiario");
            return false;
        }
        if (tab_tabla1.getValor("ide_iptin") == null || tab_tabla1.getValor("ide_iptin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el Tipo de Inversión");
            return false;
        }
        if (tab_tabla1.getValor("ide_cnmod") == null || tab_tabla1.getValor("ide_cnmod").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la Moneda");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipein") == null || tab_tabla1.getValor("ide_ipein").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar el estado");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipcin") == null || tab_tabla1.getValor("ide_ipcin").isEmpty()) {
            utilitario.agregarMensajeError("No se pudo guardar", "Debe seleccionar la Clase de Inversión");
            return false;
        }

        return true;
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.eliminar();
            }

        } else {
            utilitario.agregarNotificacionInfo("Seleccione el certificado en vista Formulario", "");
        }
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla1.getValorSeleccionado() != null) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.getTab_cabe_asiento().setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
            asc_asiento.getBot_aceptar().setMetodo("guardar");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos una Factura", "");
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_formato() {
        return sel_formato;
    }

    public void setSel_formato(SeleccionFormatoReporte sel_formato) {
        this.sel_formato = sel_formato;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

}
