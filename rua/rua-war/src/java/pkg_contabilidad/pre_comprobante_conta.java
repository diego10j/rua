/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import framework.reportes.ReporteDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import pkg_bancos.cls_bancos;
import pkg_cuentas_x_cobrar.cls_cuentas_x_cobrar;
import pkg_cuentas_x_pagar.cls_cuentas_x_pagar;
import pkg_inventario.cls_inventario;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_comprobante_conta extends Pantalla {

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_presupuesto = new Tabla();
    private final Combo com_tipo_comprobante = new Combo();
    private final Etiqueta eti_suma_debe = new Etiqueta();
    private final Etiqueta eti_suma_haber = new Etiqueta();
    private final Etiqueta eti_suma_diferencia = new Etiqueta();
    private VisualizarPDF vpdf_ver = new VisualizarPDF();
    //Parametros del sistema
    private final String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private final String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionTabla sel_tab_nivel = new SeleccionTabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    cls_contabilidad con = new cls_contabilidad();

    private final Texto tex_num_transaccion = new Texto();

    //Consultas por rango de fechas
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private Map parametro = new HashMap();
    private String fecha_fin;
    private String fecha_inicio;

    public pre_comprobante_conta() {
        //Recuperar el plan de cuentas activo

        if (ser_contabilidad.getPlandeCuentasActivo() != null) {
            bar_botones.agregarReporte();
            com_tipo_comprobante.setId("com_tipo_comprobante");
            com_tipo_comprobante.setTitle("TIPO DE COMPROBANTE");
            com_tipo_comprobante.setCombo(ser_comprobante.getSqlTiposComprobante());
            com_tipo_comprobante.setMetodo("seleccionarTipoComprobante");
            bar_botones.agregarComponente(new Etiqueta("TIPO DE COMPROBANTE :"));
            bar_botones.agregarComponente(com_tipo_comprobante);

            bar_botones.agregarComponente(new Etiqueta("DEL "));
            cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
            cal_fecha_inicio.setSize(8);
            bar_botones.agregarComponente(cal_fecha_inicio);
            bar_botones.agregarComponente(new Etiqueta("AL "));
            cal_fecha_fin.setFechaActual();
            cal_fecha_fin.setSize(8);
            bar_botones.agregarComponente(cal_fecha_fin);
            Boton bot_consultar = new Boton();
            bot_consultar.setTitle("Consultar");
            bot_consultar.setMetodo("actualizarFechaComprobantes");
            bot_consultar.setIcon("ui-icon-search");
            bar_botones.agregarComponente(bot_consultar);
            bar_botones.agregarSeparador();

            Boton bot_ver = new Boton();
            bot_ver.setValue("Ver");
            bot_ver.setTitle("Ver Comprobante");
            bot_ver.setMetodo("verComprobante");
            bot_ver.setUpdate("vpdf_ver");
            bar_botones.agregarBoton(bot_ver);

            Boton bot_anular = new Boton();
            bot_anular.setValue("Anular");
            bot_anular.setTitle("Anular Comprobante");
            bot_anular.setMetodo("anularComprobante");
            bar_botones.agregarBoton(bot_anular);

            bar_botones.agregarSeparador();
            tex_num_transaccion.setId("tex_num_transaccion");
            tex_num_transaccion.setSoloEnteros();
            tex_num_transaccion.setPlaceHolder("Nº COMPROBANTE");
            tex_num_transaccion.setTitle("Nº COMPROBANTE");
            tex_num_transaccion.setSize(7);
            Boton bot_buscar_transaccion = new Boton();
            bot_buscar_transaccion.setTitle("Buscar Comprobante");
            bot_buscar_transaccion.setIcon("ui-icon-search");
            bot_buscar_transaccion.setMetodo("buscarComprobante");

            bar_botones.agregarComponente(tex_num_transaccion);
            bar_botones.agregarBoton(bot_buscar_transaccion);

            //seleccion de las cuentas para reporte libro mayor
            sel_tab.setId("sel_tab");
            sel_tab.setSeleccionTabla(ser_contabilidad.getSqlCuentasHijas(), "ide_cndpc");
            sel_tab.getTab_seleccion().getColumna("nombre_cndpc").setFiltro(true);
            sel_tab.getTab_seleccion().getColumna("codig_recur_cndpc").setFiltro(true);
            sel_tab.getTab_seleccion().onSelectCheck("seleccionaCuentaContable");
            sel_tab.getTab_seleccion().onUnselectCheck("deseleccionaCuentaContable");
            sel_tab.setDynamic(false);
            agregarComponente(sel_tab);

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab.getBot_aceptar().setUpdate("sel_tab ");

            sel_tab_nivel.setId("sel_tab_nivel");
            sel_tab_nivel.setTitle("Seleccione El Nivel");
            sel_tab_nivel.setSeleccionTabla(ser_contabilidad.getSqlNivelesPlandeCuentas(), "ide_cnncu");
            sel_tab_nivel.setRadio();
            agregarComponente(sel_tab_nivel);

            sel_tab_nivel.getBot_aceptar().setMetodo("aceptarReporte");

            agregarComponente(rep_reporte);

            sec_rango_reporte.setId("sec_rango_reporte");
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sec_rango_reporte.setMultiple(false);
            agregarComponente(sec_rango_reporte);

            sel_rep.setId("sel_rep");
            agregarComponente(sel_rep);

            tab_tabla1.setId("tab_tabla1");
            ser_comprobante.configurarTablaCabeceraComprobante(tab_tabla1);
            tab_tabla1.setTabla("con_cab_comp_cont", "ide_cnccc", 1);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.dibujar();

            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            ItemMenu itm_reversar = new ItemMenu();
            itm_reversar.setValue("Reversar");
            itm_reversar.setTitle("Reversar Comprobante");
            itm_reversar.setMetodo("reversarComprobanteContabilidad");
            itm_reversar.setIcon("ui-icon-arrowreturnthick-1-s");
            pat_panel1.getMenuTabla().getChildren().add(new Separator());
            pat_panel1.getMenuTabla().getChildren().add(itm_reversar);

            ItemMenu itm_copiar = new ItemMenu();
            itm_copiar.setValue("Copiar");
            itm_copiar.setTitle("Copiar Comprobante");
            itm_copiar.setMetodo("copiarComprobanteContabilidad");
            itm_copiar.setIcon("ui-icon-copy");
            pat_panel1.getMenuTabla().getChildren().add(itm_copiar);

            tab_tabla2.setId("tab_tabla2");
            ser_comprobante.configurarTablaDetalleComprobante(tab_tabla2);
            tab_tabla2.setTabla("con_det_comp_cont", "ide_cndcc", 2);
            tab_tabla2.agregarRelacion(tab_presupuesto);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            Grid gri_totales = new Grid();
            gri_totales.setId("gri_totales");

            tab_presupuesto.setId("tab_presupuesto");
            tab_presupuesto.setTabla("pre_mensual", "ide_prmen", 3);
            tab_presupuesto.dibujar();
            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_presupuesto);

            gri_totales.setColumns(3);
            eti_suma_debe.setValue("TOTAL DEBE : 0");
            eti_suma_debe.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_haber.setValue("TOTAL HABER : 0");
            eti_suma_haber.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_diferencia.setValue("DIFERENCIA : 0");
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
            gri_totales.setWidth("100%");
            gri_totales.getChildren().add(eti_suma_diferencia);
            gri_totales.getChildren().add(eti_suma_debe);
            gri_totales.getChildren().add(eti_suma_haber);
            Division div_division = new Division();
            div_division.setId("div_division");
            Division div_detalle = new Division();
            div_detalle.setFooter(pat_panel2, gri_totales, "85%");

            div_division.dividir3(pat_panel1, div_detalle, pat_panel3, "40%", "30%", "H");

            agregarComponente(div_division);

            vpdf_ver.setTitle("Comprobante de Contabilidad");
            vpdf_ver.setId("vpdf_ver");
            agregarComponente(vpdf_ver);
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void buscarComprobante() {
        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {
            tab_tabla1.setCondicion("ide_cnccc=" + tex_num_transaccion.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            if (tab_tabla1.getTotalFilas() > 0) {
                if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm") != null && !tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm").isEmpty()) {
                    com_tipo_comprobante.setValue(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm"));
                }
                calcularTotal();
            } else {
                utilitario.agregarMensajeInfo("El Número de Comprobante no existe", "");
                com_tipo_comprobante.setValue(null);
            }
            utilitario.addUpdate("gri_totales,com_tipo_comprobante");
        }
    }

    /**
     * CODIGO ANTERIOR PARA REVERSAR *****************
     */
    public void reversarComprobante() {
        if (tab_tabla1.getTotalFilas() > 0) {
            String ide_cnccc_anular = tab_tabla1.getValor("ide_cnccc");
            // realizo el asiento de reversa
            con.reversar(ide_cnccc_anular, "Asiento de reversa de la transaccion num: " + ide_cnccc_anular, con);
            String ide_cnccc_nuevo = con.getTab_cabecera().getValor("ide_cnccc");
            if (ide_cnccc_nuevo != null && !ide_cnccc_nuevo.isEmpty()) {
                // cambio el estado de libro bancos a anulado
                utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set ide_teelb=1 where ide_cnccc=" + ide_cnccc_anular);
                // consulto si tiene Documentos por Pagar
                TablaGenerica tab_cxp_cab_fact = utilitario.consultar("select * from cxp_cabece_factur where ide_cnccc=" + ide_cnccc_anular);
                if (tab_cxp_cab_fact.getTotalFilas() > 0) {
                    // cambio elestado a anulado de la factura
                    utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set ide_cpefa=1 where ide_cnccc=" + ide_cnccc_anular);
                    cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                    // reverso la transaccion CxP
                    cxp.reversar(ide_cnccc_nuevo, tab_cxp_cab_fact.getValor(0, "ide_cpcfa"), "Reversa CxP de fact. num:" + tab_cxp_cab_fact.getValor(0, "numero_cpcfa") + " y asiento num:" + ide_cnccc_anular, null);
                    // hago reversa de inventario
                    TablaGenerica tab_inv_cab_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_cnccc=" + ide_cnccc_anular);
                    if (tab_inv_cab_inv.getTotalFilas() > 0) {
                        cls_inventario inv = new cls_inventario();
                        inv.reversar_menos(ide_cnccc_nuevo, tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Reversa de comprobante num:" + ide_cnccc_anular);
                    }
                }

                boolean boo_asiento_costos = false;
                String ide_asiento_costos = "-1";
                // consulto si tiene CXC

                TablaGenerica tab_cxc_cab_fact = utilitario.consultar("select * from cxc_cabece_factura where ide_cnccc=" + ide_cnccc_anular);
                if (tab_cxc_cab_fact.getTotalFilas() > 0) {
                    // cambio elestado a anulado de la factura cxc si tiene
                    utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_ccefa=1 where ide_cnccc=" + ide_cnccc_anular);
                    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                    //cxc.reversar(ide_cnccc_nuevo, tab_cxc_cab_fact.getValor(0, "ide_cccfa"), "Reversa CxP de fact. num:" + tab_cxc_cab_fact.getValor(0, "secuencial_cccfa") + " y asiento num:" + ide_cnccc_anular);
                    TablaGenerica tab_inv_cab_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_incci in ( "
                            + "select ide_incci from inv_det_comp_inve where ide_cccfa=" + tab_cxc_cab_fact.getValor(0, "ide_cccfa") + " GROUP BY ide_incci)");
                    if (tab_inv_cab_inv.getTotalFilas() > 0) {
                        cls_inventario inv = new cls_inventario();
                        // reverso inventario
                        inv.reversar_mas(ide_cnccc_nuevo, tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Reversa de comprobante num:" + ide_cnccc_anular);
                        // reverso el comprobante de costos
                        boo_asiento_costos = true;
                        ide_asiento_costos = tab_inv_cab_inv.getValor(0, "ide_cnccc");
                    }
                }

                utilitario.getConexion().guardarPantalla();
                tab_tabla1.setFilaActual(con.getTab_cabecera().getValor("ide_cnccc"));
                tab_tabla1.ejecutarSql();
                if (boo_asiento_costos == true) {
                    con.limpiar();
                    con.reversar(ide_asiento_costos, "Asiento de reversa asiento costos de la transaccion num: " + ide_cnccc_anular, con);
                    utilitario.getConexion().guardarPantalla();
                }
            }
        }
    }

    public void anularComprobante() {
        if (tab_tabla1.getTotalFilas() > 0) {
            if (!tab_tabla1.getValor("ide_cneco").equals(utilitario.getVariable("p_con_estado_comprobante_anulado"))) {
                String ide_cnccc_anular = tab_tabla1.getValor("ide_cnccc");
                // realizo el asiento de reversa
                con.anular(ide_cnccc_anular);
                // cambio el estado de libro bancos a anulado
                TablaGenerica tab_libro_bancos = utilitario.consultar("select * from tes_cab_libr_banc where ide_cnccc=" + tab_tabla1.getValor("ide_cnccc"));
                if (tab_libro_bancos.getTotalFilas() > 0) {
                    utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set ide_teelb=1 where ide_cnccc=" + tab_tabla1.getValor("ide_cnccc"));
                    cls_bancos bancos = new cls_bancos();
                    bancos.reversar(tab_libro_bancos.getValor("ide_teclb"), "anula asiento num " + tab_tabla1.getValor("ide_cnccc"));
                }
                // consulto si tiene Documentos por Pagar
                TablaGenerica tab_cxp_cab_fact = utilitario.consultar("select * from cxp_cabece_factur where ide_cnccc=" + ide_cnccc_anular);
                if (tab_cxp_cab_fact.getTotalFilas() > 0) {
                    // cambio elestado a anulado de la factura
                    utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set ide_cpefa=1 where ide_cnccc=" + ide_cnccc_anular);
                    cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                    // reverso la transaccion CxP
                    cxp.reversar(ide_cnccc_anular, tab_cxp_cab_fact.getValor(0, "ide_cpcfa"), "Reversa CxP de fact. num:" + tab_cxp_cab_fact.getValor(0, "numero_cpcfa") + " y asiento num:" + ide_cnccc_anular, null);
                    // hago reversa de inventario
                    TablaGenerica tab_inv_cab_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_cnccc=" + ide_cnccc_anular);
                    if (tab_inv_cab_inv.getTotalFilas() > 0) {
                        cls_inventario inv = new cls_inventario();
                        inv.reversar_menos(ide_cnccc_anular, tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Reversa de comprobante num:" + ide_cnccc_anular);
                    }
                }

                boolean boo_asiento_costos = false;
                String ide_asiento_costos = "-1";
                // consulto si tiene CXC

                TablaGenerica tab_cxc_cab_fact = utilitario.consultar("select * from cxc_cabece_factura where ide_cnccc=" + ide_cnccc_anular);
                if (tab_cxc_cab_fact.getTotalFilas() > 0) {
                    // cambio elestado a anulado de la factura cxc si tiene
                    utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_ccefa=1 where ide_cnccc=" + ide_cnccc_anular);
                    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                    cxc.reversar(tab_cxc_cab_fact.getValor(0, "ide_cccfa"), "Reversa CxP de fact. num:" + tab_cxc_cab_fact.getValor(0, "secuencial_cccfa") + " y asiento num:" + ide_cnccc_anular);
                    TablaGenerica tab_inv_cab_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_incci in ( "
                            + "select ide_incci from inv_det_comp_inve where ide_cccfa=" + tab_cxc_cab_fact.getValor(0, "ide_cccfa") + " GROUP BY ide_incci)");
                    if (tab_inv_cab_inv.getTotalFilas() > 0) {
                        utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_inepi=0 where ide_incci=" + tab_inv_cab_inv.getValor(0, "ide_incci"));
                        cls_inventario inv = new cls_inventario();
                        // reverso inventario
                        inv.reversar_mas(ide_cnccc_anular, tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Reversa de comprobante num:" + ide_cnccc_anular);
                        // reverso el comprobante de costos
                        if (tab_inv_cab_inv.getValor(0, "ide_cnccc") != null && !tab_inv_cab_inv.getValor(0, "ide_cnccc").isEmpty()) {
                            boo_asiento_costos = true;
                            ide_asiento_costos = tab_inv_cab_inv.getValor(0, "ide_cnccc");
                        }
                    }
                }
                utilitario.getConexion().guardarPantalla();
                tab_tabla1.setFilaActual(con.getTab_cabecera().getValor("ide_cnccc"));
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValor("ide_cnccc"));
                if (boo_asiento_costos == true) {
                    con.limpiar();
                    con.anular(ide_asiento_costos);
                    utilitario.getConexion().guardarPantalla();
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede anular el comprobante", "El comprobante seleccionado ya esta anulado");
            }
        }
    }

    @Override
    public void inicio() {
        super.inicio();
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    @Override
    public void fin() {
        super.fin();
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    @Override
    public void atras() {
        super.atras();
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    @Override
    public void siguiente() {
        super.siguiente();
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    /**
     * Copia el comprobante seleccionado y crea uno nuevo para poder editarlo
     */
    public void copiarComprobanteContabilidad() {
        if (!tab_tabla1.isFilaInsertada()) {
            TablaGenerica tab_cabecera = ser_comprobante.getCabeceraComprobante(tab_tabla1.getValorSeleccionado());
            TablaGenerica tab_detalle = ser_comprobante.getDetallesComprobante(tab_tabla1.getValorSeleccionado());
            tab_tabla1.insertar();
            tab_tabla1.setValor("IDE_MODU", tab_cabecera.getValor("IDE_MODU"));
            tab_tabla1.setValor("IDE_GEPER", tab_cabecera.getValor("IDE_GEPER"));
            tab_tabla1.setValor("OBSERVACION_CNCCC", tab_cabecera.getValor("OBSERVACION_CNCCC"));
            tab_tabla1.setValor("ide_cntcm", tab_cabecera.getValor("ide_cntcm"));
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("IDE_CNLAP", tab_detalle.getValor(i, "IDE_CNLAP"));
                tab_tabla2.setValor("IDE_CNDPC", tab_detalle.getValor(i, "IDE_CNDPC"));
                tab_tabla2.setValor("VALOR_CNDCC", tab_detalle.getValor(i, "VALOR_CNDCC"));
                tab_tabla2.setValor("OBSERVACION_CNDCC", tab_detalle.getValor(i, "OBSERVACION_CNDCC"));
            }
            calcularTotal();
            utilitario.addUpdate("gri_totales");
        } else {
            utilitario.agregarMensajeInfo("No se puede copiar el comprobante de contabilidad", "El comprobante seleccionado no se encuentra grabado");
        }
    }

    /**
     * Reversa el comprobante seleccionado
     */
    public void reversarComprobanteContabilidad() {
        String ide_cnccc = ser_comprobante.reversarComprobante(tab_tabla1.getValorSeleccionado(), null);
        if (guardarPantalla().isEmpty()) {
            utilitario.agregarMensaje("Se genero el Comprobante Num: ", ide_cnccc);
        }
    }

    /**
     * Permite ver el Comprobante en el VisualizadorPDF
     */
    public void verComprobante() {
        if (tab_tabla1.getValorSeleccionado() != null) {
            if (!tab_tabla1.isFilaInsertada()) {
                Map parametros = new HashMap();
                parametros.put("ide_cnccc", Long.parseLong(tab_tabla1.getValorSeleccionado()));
                parametros.put("ide_cnlap_debe", p_con_lugar_debe);
                parametros.put("ide_cnlap_haber", p_con_lugar_haber);
                vpdf_ver.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametros);
                vpdf_ver.dibujar();
            } else {
                utilitario.agregarMensajeInfo("Debe guardar el comprobante", "");
            }

        } else {
            utilitario.agregarMensajeInfo("No hay ningun comprobante seleccionado", "");
        }
    }

    /**
     * Actualiza el rango de fechas para consultar comprobantes
     */
    public void actualizarFechaComprobantes() {
        if (utilitario.isFechasValidas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha())) {
            tab_tabla1.setCondicion("fecha_trans_cnccc between '" + cal_fecha_inicio.getFecha() + "' and '" + cal_fecha_fin.getFecha() + "' and ide_cntcm=" + com_tipo_comprobante.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            utilitario.addUpdate("tab_tabla1,tab_tabla2");
            calcularTotal();
            utilitario.addUpdate("gri_totales");
        } else {
            utilitario.agregarMensajeInfo("Rango de fechas no válidas", "");
        }

    }

    /**
     * Actualiza los tipos de Comprobante cuando se selecciona del Combo de Tipo
     * de Comprobante
     */
    public void seleccionarTipoComprobante() {
        if (com_tipo_comprobante.getValue() != null) {
            tab_tabla1.setCondicion("fecha_trans_cnccc between '" + cal_fecha_inicio.getFecha() + "' and '" + cal_fecha_fin.getFecha() + "' and ide_cntcm=" + com_tipo_comprobante.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } else {
            tab_tabla1.limpiar();
            tab_tabla2.limpiar();
        }
        tex_num_transaccion.setValue(null);
        calcularTotal();
        utilitario.addUpdate("gri_totales,tex_num_transaccion");
    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            if (com_tipo_comprobante.getValue() != null) {
                tab_tabla1.getColumna("ide_cntcm").setValorDefecto(com_tipo_comprobante.getValue() + "");
                tab_tabla1.insertar();
                calcularTotal();
                utilitario.addUpdate("gri_totales");
            } else {
                utilitario.agregarMensajeInfo("No se puede Insertar", "Debe seleccionar un tipo de comprobante");
            }
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        } else if (tab_presupuesto.isFocus()) {
            tab_presupuesto.insertar();
        }
    }

    @Override
    public void guardar() {
        if (validarComprobante()) {
            if (ser_comprobante.isPeriodoValido(tab_tabla1.getValor("fecha_trans_cnccc"))) {
                if (tab_tabla1.isFilaInsertada()) {
                    tab_tabla1.setValor("hora_sistem_cnccc", utilitario.getHoraActual());
                    tab_tabla1.setValor("numero_cnccc", ser_comprobante.getSecuencial(tab_tabla1.getValor("fecha_trans_cnccc"), tab_tabla1.getValor("ide_cntcm")));
                }
                tab_tabla1.guardar();
                tab_tabla2.guardar();
                tab_presupuesto.guardar();
                utilitario.getConexion().guardarPantalla();
            }
        }

    }

    public void cambioLugarAplica(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public boolean validarComprobante() {
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (!ser_contabilidad.isCuentaContableHija(tab_tabla2.getValor(i, "ide_cndpc"))) {
                utilitario.agregarMensajeError("En uno de los detalles del asiento, hay cuentas contables que no tiene nivel HIJO", "");
                return false;
            }
        }
        if (tab_tabla1.getValor("fecha_trans_cnccc") == null || tab_tabla1.getValor("fecha_trans_cnccc").isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar la Fecha de Transaccion", "");
            return false;
        }
        if (tab_tabla1.getValor("observacion_cnccc") == null || tab_tabla1.getValor("observacion_cnccc").isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una Observación", "");
            return false;
        }
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Beneficiario", "");
            return false;
        }
        if (!calcularTotal()) {
            utilitario.agregarMensajeError("La suma de los detalles del DEBE tiene que ser igual al del HABER", "");
            return false;
        }
        return true;

    }

    /**
     * Calcula la sumatoria de debe vs haber
     *
     * @return true =asiento cuadrado ; false = asiento con diferencias
     */
    public boolean calcularTotal() {
        double dou_debe = 0;
        double dou_haber = 0;
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            try {
                if (tab_tabla2.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                    dou_debe += Double.parseDouble(tab_tabla2.getValor(i, "valor_cndcc"));
                } else if (tab_tabla2.getValor(i, "ide_cnlap").equals(p_con_lugar_haber)) {
                    dou_haber += Double.parseDouble(tab_tabla2.getValor(i, "valor_cndcc"));
                }
            } catch (Exception e) {
            }
        }
        eti_suma_debe.setValue("TOTAL DEBE : " + utilitario.getFormatoNumero(dou_debe));
        eti_suma_haber.setValue("TOTAL HABER : " + utilitario.getFormatoNumero(dou_haber));

        double dou_diferencia = Double.parseDouble(utilitario.getFormatoNumero(dou_debe)) - Double.parseDouble(utilitario.getFormatoNumero(dou_haber));
        eti_suma_diferencia.setValue("DIFERENCIA : " + utilitario.getFormatoNumero(dou_diferencia));
        if (dou_diferencia != 0.0) {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold;color:red");
        } else {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
            return true;
        }
        return false;
    }

    public void ingresaCantidad(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    @Override
    public void eliminar() {
        if (utilitario.getTablaisFocus().isFilaInsertada()) {
            utilitario.getTablaisFocus().eliminar();
            calcularTotal();
            utilitario.addUpdate("gri_totales");
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public String getFormatoFecha(String fecha) {
        String mes = utilitario.getNombreMes(utilitario.getMes(fecha));
        String dia = utilitario.getDia(fecha) + "";
        String anio = utilitario.getAnio(fecha) + "";
        String fecha_formato = dia + " DE " + mes + " DEL " + anio;
        return fecha_formato;
    }
    private List lis_ide_cndpc_sel = new ArrayList();
    private int int_count_seleccion = 0;

    public void seleccionaCuentaContable(SelectEvent evt) {
        sel_tab.getTab_seleccion().seleccionarFila(evt);
        for (Fila actual : sel_tab.getTab_seleccion().getSeleccionados()) {
            int band = 0;
            for (int i = 0; i < lis_ide_cndpc_sel.size(); i++) {
                if (actual.getRowKey().equals(lis_ide_cndpc_sel.get(i))) {
                    band = 1;
                    break;
                }
            }
            if (band == 0) {
                lis_ide_cndpc_sel.add(actual.getRowKey());
            }
        }
        if (int_count_seleccion == 0) {
            lis_ide_cndpc_deseleccionados = lis_ide_cndpc_sel;
        }
        int_count_seleccion += 1;

    }
    private List lis_ide_cndpc_deseleccionados = new ArrayList();
    private int int_count_deseleccion = 0;

    public void deseleccionaCuentaContable(UnselectEvent evt) {
        //tab_tabla2.modificar(evt);
        for (Fila actual : sel_tab.getTab_seleccion().getSeleccionados()) {
            int band = 0;
            for (int i = 0; i < lis_ide_cndpc_deseleccionados.size(); i++) {
                if (actual.getRowKey().equals(lis_ide_cndpc_deseleccionados.get(i))) {
                    band = 1;
                    break;
                }
            }
            if (band == 0) {
                lis_ide_cndpc_deseleccionados.add(actual.getRowKey());
            }
        }
        int_count_deseleccion += 1;
    }

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista        
        if (rep_reporte.getReporteSelecionado().equals("Libro Diario")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");

            } else if (sec_rango_reporte.isVisible()) {
                String estado = "" + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comp_final");
                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());

                parametro.put("ide_cneco", estado);
                parametro.put("ide_cnlap_haber", p_con_lugar_haber);
                parametro.put("ide_cnlap_debe", p_con_lugar_debe);
                sec_rango_reporte.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_rep,sec_rango_reporte");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Balance General Consolidado")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        } else {
                            utilitario.agregarMensajeError("Atencion", "El rango de fechas seleccionado no se encuentra en ningun Periodo Contable");
                        }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha final");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicial");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    System.out.println("fecha fin " + fecha_fin);
                    parametro.put("p_activo", utilitario.getVariable("p_con_tipo_cuenta_activo"));
                    parametro.put("p_pasivo", utilitario.getVariable("p_con_tipo_cuenta_pasivo"));
                    parametro.put("p_patrimonio", utilitario.getVariable("p_con_tipo_cuenta_patrimonio"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));

                    }
                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_balance = con.generarBalanceGeneral(true, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    parametro.put("titulo", "BALANCE GENERAL CONSOLIDADO");
                    if (tab_balance.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesBalanceGeneral(true, fecha_inicio, fecha_fin);
                        double tot_activo = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_pasivo = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_patrimonio = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_activo - tot_pasivo - tot_patrimonio;
                        double total = tot_pasivo + tot_patrimonio + utilidad_perdida;
                        parametro.put("p_tot_activo", tot_activo);
                        parametro.put("p_total", total);
                        parametro.put("p_utilidad_perdida", utilidad_perdida);
                        parametro.put("p_tot_pasivo", tot_pasivo);
                        parametro.put("p_tot_patrimonio", (tot_patrimonio));
                    }
                    sel_tab_nivel.cerrar();
                    ReporteDataSource data = new ReporteDataSource(tab_balance);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();

                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Balance General")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        } else {
                            utilitario.agregarMensajeError("Atencion", "El rango de fechas seleccionado no se encuentra en ningun Periodo Contable");
                        }

                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha final");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicial");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    System.out.println("fecha fin " + fecha_fin);
                    parametro.put("p_activo", utilitario.getVariable("p_con_tipo_cuenta_activo"));
                    parametro.put("p_pasivo", utilitario.getVariable("p_con_tipo_cuenta_pasivo"));
                    parametro.put("p_patrimonio", utilitario.getVariable("p_con_tipo_cuenta_patrimonio"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));

                    }
                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_balance = con.generarBalanceGeneral(false, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    parametro.put("titulo", "BALANCE GENERAL");
                    if (tab_balance.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesBalanceGeneral(false, fecha_inicio, fecha_fin);
                        double tot_activo = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_pasivo = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_patrimonio = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_activo - tot_pasivo - tot_patrimonio;
                        double total = tot_pasivo + tot_patrimonio + utilidad_perdida;
                        parametro.put("p_tot_activo", tot_activo);
                        parametro.put("p_total", total);
                        parametro.put("p_utilidad_perdida", utilidad_perdida);
                        parametro.put("p_tot_pasivo", tot_pasivo);
                        parametro.put("p_tot_patrimonio", (tot_patrimonio));
                    }
                    sel_tab_nivel.cerrar();
                    ReporteDataSource data = new ReporteDataSource(tab_balance);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();

                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Estado de Resultados Consolidado")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha fin");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicio");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    parametro.put("p_ingresos", utilitario.getVariable("p_con_tipo_cuenta_ingresos"));
                    parametro.put("p_gastos", utilitario.getVariable("p_con_tipo_cuenta_gastos"));
                    parametro.put("p_costos", utilitario.getVariable("p_con_tipo_cuenta_costos"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                    }

                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_estado = con.generarEstadoResultados(true, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    if (tab_estado.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesEstadoResultados(true, fecha_inicio, fecha_fin);
                        double tot_ingresos = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_gastos = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_costos = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_ingresos - (tot_gastos + tot_costos);
                        parametro.put("p_tot_ingresos", tot_ingresos);
                        parametro.put("p_tot_gastos", tot_gastos);
                        parametro.put("p_tot_costos", tot_costos);
                        parametro.put("p_utilidad", utilidad_perdida);
                    }
                    parametro.put("titulo", "ESTADO DE RESULTADOS CONSOLIDADO");
                    ReporteDataSource data = new ReporteDataSource(tab_estado);
                    sel_tab_nivel.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Estado de Resultados")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                    if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                        fecha_fin = sec_rango_reporte.getFecha2String();
                        fecha_inicio = con.getFechaInicialPeriodo(fecha_fin);
                        if (fecha_inicio != null && !fecha_inicio.isEmpty()) {
                            sec_rango_reporte.cerrar();
                            sel_tab_nivel.dibujar();
                            utilitario.addUpdate("sec_rango_reporte,sel_tab_nivel");
                        }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha fin");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicio");
                }
            } else if (sel_tab_nivel.isVisible()) {
                if (sel_tab_nivel.getValorSeleccionado() != null) {
                    parametro.put("p_ingresos", utilitario.getVariable("p_con_tipo_cuenta_ingresos"));
                    parametro.put("p_gastos", utilitario.getVariable("p_con_tipo_cuenta_gastos"));
                    parametro.put("p_costos", utilitario.getVariable("p_con_tipo_cuenta_costos"));
                    TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                    if (tab_datos.getTotalFilas() > 0) {
                        parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                        parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                        parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                        parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                        parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                        parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                    }

                    parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio));
                    parametro.put("fecha_fin", getFormatoFecha(fecha_fin));
                    TablaGenerica tab_estado = con.generarEstadoResultados(false, fecha_inicio, fecha_fin, Integer.parseInt(sel_tab_nivel.getValorSeleccionado()));
                    if (tab_estado.getTotalFilas() > 0) {
                        List lis_totales = con.obtenerTotalesEstadoResultados(false, fecha_inicio, fecha_fin);
                        double tot_ingresos = Double.parseDouble(lis_totales.get(0) + "");
                        double tot_gastos = Double.parseDouble(lis_totales.get(1) + "");
                        double tot_costos = Double.parseDouble(lis_totales.get(2) + "");
                        double utilidad_perdida = tot_ingresos - (tot_gastos + tot_costos);
                        parametro.put("p_tot_ingresos", tot_ingresos);
                        parametro.put("p_tot_gastos", tot_gastos);
                        parametro.put("p_tot_costos", tot_costos);
                        parametro.put("p_utilidad", utilidad_perdida);
                    }
                    ReporteDataSource data = new ReporteDataSource(tab_estado);
                    parametro.put("titulo", "ESTADO DE RESULTADOS");
                    sel_tab_nivel.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep,sel_tab_nivel");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Libro Mayor")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                lis_ide_cndpc_sel.clear();
                lis_ide_cndpc_deseleccionados.clear();
                int_count_deseleccion = 0;
                int_count_seleccion = 0;
                sel_tab.getTab_seleccion().setSeleccionados(null);
//                utilitario.ejecutarJavaScript(sel_tab.getTab_seleccion().getId() + ".clearFilters();");
                sel_tab.dibujar();
                utilitario.addUpdate("rep_reporte,sel_tab");
            } else {
                if (sel_tab.isVisible()) {

                    if (sel_tab.getSeleccionados() != null && !sel_tab.getSeleccionados().isEmpty()) {
                        System.out.println("nn " + sel_tab.getSeleccionados());
                        parametro.put("ide_cndpc", sel_tab.getSeleccionados());//lista sel                     
                        sel_tab.cerrar();
                        String estado = "" + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comp_final");
                        parametro.put("ide_cneco", estado);
                        sec_rango_reporte.setMultiple(true);
                        sec_rango_reporte.dibujar();
                        utilitario.addUpdate("sel_tab,sec_rango_reporte");

                    } else {
                        utilitario.agregarMensajeInfo("Debe seleccionar al menos una cuenta contable", "");
                    }
//                    if (lis_ide_cndpc_deseleccionados.size() == 0) {
//                        System.out.println("sel tab lis " + sel_tab.getSeleccionados());
//                        parametro.put("ide_cndpc", sel_tab.getSeleccionados());//lista sel                     
//                    } else {
//                        System.out.println("sel tab " + utilitario.generarComillasLista(lis_ide_cndpc_deseleccionados));
//                        parametro.put("ide_cndpc", utilitario.generarComillasLista(lis_ide_cndpc_deseleccionados));//lista sel                     
//                    }
                } else if (sec_rango_reporte.isVisible()) {
                    if (sec_rango_reporte.isFechasValidas()) {
                        parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                        parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                        parametro.put("ide_cnlap_haber", p_con_lugar_haber);
                        parametro.put("ide_cnlap_debe", p_con_lugar_debe);
                        sec_rango_reporte.cerrar();
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep,sec_rango_reporte");
                    } else {
                        utilitario.agregarMensajeInfo("Las fechas seleccionadas no son correctas", "");
                    }
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Balance de Comprobacion")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");

            } else {
                if (sec_rango_reporte.isVisible()) {
                    if (sec_rango_reporte.getFecha1String() != null && !sec_rango_reporte.getFecha1String().isEmpty()) {
                        if (sec_rango_reporte.getFecha2String() != null && !sec_rango_reporte.getFecha2String().isEmpty()) {
                            String fecha_fin1 = sec_rango_reporte.getFecha2String();
                            String fecha_inicio1 = sec_rango_reporte.getFecha1String();
                            System.out.println("fecha fin " + fecha_fin1);
                            sec_rango_reporte.cerrar();

                            TablaGenerica tab_datos = utilitario.consultar("SELECT * FROM sis_empresa e, sis_sucursal s where s.ide_empr=e.ide_empr and s.ide_empr=" + utilitario.getVariable("ide_empr") + " and s.ide_sucu=" + utilitario.getVariable("ide_sucu"));
                            if (tab_datos.getTotalFilas() > 0) {
                                parametro.put("logo", tab_datos.getValor(0, "logo_empr"));
                                parametro.put("empresa", tab_datos.getValor(0, "nom_empr"));
                                parametro.put("sucursal", tab_datos.getValor(0, "nom_sucu"));
                                parametro.put("direccion", tab_datos.getValor(0, "direccion_sucu"));
                                parametro.put("telefono", tab_datos.getValor(0, "telefonos_sucu"));
                                parametro.put("ruc", tab_datos.getValor(0, "identificacion_empr"));
                            }
                            String fechaPeriodoActivo = con.obtenerFechaInicialPeriodoActivo();
//                        if (fechaPeriodoActivo != null && !fechaPeriodoActivo.isEmpty()) {
                            parametro.put("fecha_inicio", getFormatoFecha(fecha_inicio1));
                            parametro.put("fecha_fin", getFormatoFecha(fecha_fin1));
                            TablaGenerica tab_bal = con.generarBalanceComprobacion(fechaPeriodoActivo, fecha_fin1);
                            double suma_debe = 0;
                            double suma_haber = 0;
                            double suma_deudor = 0;
                            double suma_acreedor = 0;
                            for (int i = 0; i < tab_bal.getTotalFilas() - 1; i++) {
                                suma_debe = Double.parseDouble(tab_bal.getValor(i, "debe")) + suma_debe;
                                suma_haber = Double.parseDouble(tab_bal.getValor(i, "haber")) + suma_haber;
                                suma_deudor = Double.parseDouble(tab_bal.getValor(i, "deudor")) + suma_deudor;
                                suma_acreedor = Double.parseDouble(tab_bal.getValor(i, "acreedor")) + suma_acreedor;
                            }
                            parametro.put("tot_debe", suma_debe);
                            parametro.put("tot_haber", suma_haber);
                            parametro.put("tot_deudor", suma_deudor);
                            parametro.put("tot_acreedor", suma_acreedor);
                            ReporteDataSource data = new ReporteDataSource(tab_bal);
                            sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath(), data);
                            sel_rep.dibujar();
                            utilitario.addUpdate("sel_rep,sec_rango_reporte");
                        }
//                    }
                    } else {
                        utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha fin");
                    }
                } else {
                    utilitario.agregarMensajeError("Atencion", "No ha seleccionado la fecha inicio");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", p_con_lugar_haber);
                    parametro.put("ide_cnlap_debe", p_con_lugar_debe);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene compraqbante de contabilidad");
                }

            }
        }
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        sec_rango_reporte.getCal_fecha1().setValue(null);
        sec_rango_reporte.getCal_fecha2().setValue(null);
        rep_reporte.dibujar();

    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
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

    public VisualizarPDF getVpdf_ver() {
        return vpdf_ver;
    }

    public void setVpdf_ver(VisualizarPDF vpdf_ver) {
        this.vpdf_ver = vpdf_ver;
    }

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public SeleccionTabla getSel_tab_nivel() {
        return sel_tab_nivel;
    }

    public void setSel_tab_nivel(SeleccionTabla sel_tab_nivel) {
        this.sel_tab_nivel = sel_tab_nivel;
    }

    public Tabla getTab_presupuesto() {
        return tab_presupuesto;
    }

    public void setTab_presupuesto(Tabla tab_presupuesto) {
        this.tab_presupuesto = tab_presupuesto;
    }

}
