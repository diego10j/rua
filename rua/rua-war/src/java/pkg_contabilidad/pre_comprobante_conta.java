/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
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
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import pkg_bancos.cls_bancos;
import pkg_cuentas_x_cobrar.cls_cuentas_x_cobrar;
import pkg_cuentas_x_pagar.cls_cuentas_x_pagar;
import pkg_inventario.cls_inventario;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 *
 */
public class pre_comprobante_conta extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();
    private Boton bot_copiar = new Boton();
    private Combo com_tipo_comprobante = new Combo();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private Etiqueta eti_suma_debe = new Etiqueta();
    private Etiqueta eti_suma_haber = new Etiqueta();
    private Etiqueta eti_suma_diferencia = new Etiqueta();
    private Boton bot_ver = new Boton();
    private VisualizarPDF vpdf_ver = new VisualizarPDF();
    //Parametros del sistema
    private String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
    private String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionTabla sel_tab_nivel = new SeleccionTabla();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    cls_contabilidad con = new cls_contabilidad();

    private Texto tex_num_transaccion = new Texto();

    public pre_comprobante_conta() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.agregarReporte();
            bar_botones.getBot_inicio().setMetodo("inicio");
            bar_botones.getBot_fin().setMetodo("fin");
            bar_botones.getBot_atras().setMetodo("atras");
            bar_botones.getBot_siguiente().setMetodo("siguiente");

            bar_botones.agregarCalendario();

            tex_num_transaccion.setId("tex_num_transaccion");
            tex_num_transaccion.setSoloEnteros();
            tex_num_transaccion.setSize(10);
            Boton bot_buscar_transaccion = new Boton();
            bot_buscar_transaccion.setTitle("Buscar Transaccion");
            bot_buscar_transaccion.setIcon("ui-icon-search");
            bot_buscar_transaccion.setMetodo("buscarTransaccion");

            bar_botones.agregarComponente(new Etiqueta("Num Transaccion: "));
            bar_botones.agregarComponente(tex_num_transaccion);
            bar_botones.agregarBoton(bot_buscar_transaccion);

            com_tipo_comprobante.setId("com_tipo_comprobante");
            com_tipo_comprobante.setTitle("Tipo de Comprobate");
            com_tipo_comprobante.setCombo("select ide_cntcm,nombre_cntcm from con_tipo_comproba where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_tipo_comprobante.setMetodo("seleccionTipoComprobante");

            bar_botones.agregarComponente(new Etiqueta("Tipo de Comprobante :"));
            bar_botones.agregarComponente(com_tipo_comprobante);

            Espacio esp = new Espacio();
            esp.setHeight("1");
            esp.setWidth("20");
            bar_botones.agregarComponente(esp);

            bot_ver.setValue("Ver Comprobante");
            bot_ver.setMetodo("verComprobante");
            bot_ver.setUpdate("vpdf_ver");
            //       bar_botones.agregarBoton(bot_ver);

            bot_copiar.setValue("Copiar");
            bot_copiar.setMetodo("copiarComprobante");

            bot_copiar.setIcon("ui-icon-copy");
            bar_botones.agregarBoton(bot_copiar);
            Boton bot_anular = new Boton();
            bot_anular.setValue("Anular Comprobante");
            bot_anular.setMetodo("anularComprobante");
            bar_botones.agregarBoton(bot_anular);

            //seleccion de las cuentas para reporte libro mayor
            sel_tab.setId("sel_tab");
            sel_tab.setSeleccionTabla("SELECT ide_cndpc,codig_recur_cndpc,nombre_cndpc FROM con_det_plan_cuen WHERE ide_cncpc=(SELECT ide_cncpc FROM con_cab_plan_cuen WHERE activo_cncpc is TRUE) AND ide_empr=" + utilitario.getVariable("ide_empr") + " AND nivel_cndpc='HIJO' ORDER BY codig_recur_cndpc ASC ", "ide_cndpc");
            sel_tab.getTab_seleccion().getColumna("nombre_cndpc").setFiltro(true);
            sel_tab.getTab_seleccion().getColumna("codig_recur_cndpc").setFiltro(true);
            sel_tab.getTab_seleccion().onSelectCheck("seleccionaCuentaContable");
            sel_tab.getTab_seleccion().onUnselectCheck("deseleccionaCuentaContable");
            sel_tab.setDynamic(false);
            agregarComponente(sel_tab);

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");

            sel_tab.getBot_aceptar().setMetodo("aceptarReporte");
            sel_tab.getBot_aceptar().setUpdate("sel_tab,sec_calendario ");

            sel_tab_nivel.setId("sel_tab_nivel");
            sel_tab_nivel.setTitle("Seleccione El Nivel");
            sel_tab_nivel.setSeleccionTabla("select ide_cnncu,nombre_cnncu from con_nivel_cuenta where ide_empr=" + utilitario.getVariable("ide_empr") + "", "ide_cnncu");
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
            tab_tabla1.setTabla("con_cab_comp_cont", "ide_cnccc", 1);
            tab_tabla1.setCondicionSucursal(true);
            tab_tabla1.getColumna("ide_cneco").setCombo("con_estado_compro", "ide_cneco", "nombre_cneco", "");
            tab_tabla1.getColumna("fecha_siste_cnccc").setVisible(false);
            tab_tabla1.getColumna("numero_cnccc").setEtiqueta();
            tab_tabla1.getColumna("numero_cnccc").setEstilo("font-size:11px;font-weight: bold");
            tab_tabla1.getColumna("fecha_siste_cnccc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_trans_cnccc").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_sistem_cnccc").setVisible(false);
            tab_tabla1.getColumna("hora_sistem_cnccc").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("ide_cntcm").setVisible(false);
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("ide_modu").setCombo("sis_modulo", "ide_modu", "nom_modu", "");
            tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_geper").setRequerida(true);

            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.setCondicion("ide_cntcm=-1");
            tab_tabla1.setValidarInsertar(true);
            tab_tabla1.setRecuperarLectura(true);
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(6);
            tab_tabla1.getColumna("ide_cneco").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_normal"));
            tab_tabla1.getColumna("ide_cneco").setLectura(true);
            tab_tabla1.setCampoOrden("ide_cnccc desc");
            tab_tabla1.dibujar();

            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("con_det_comp_cont", "ide_cndcc", 2);
            tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0));
            tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla2.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla2.getColumna("ide_cnlap").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
            tab_tabla2.getColumna("ide_cnlap").setPermitirNullCombo(false);
            tab_tabla2.getColumna("ide_cnlap").setMetodoChange("cambioLugarAplica");
            tab_tabla2.setCampoOrden("ide_cnlap desc");
            tab_tabla2.getColumna("valor_cndcc").setMetodoChange("ingresaCantidad");
            tab_tabla2.setCampoOrden("ide_cnlap desc");
            tab_tabla2.setRows(10);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            Grid gri_totales = new Grid();
            gri_totales.setId("gri_totales");

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

            div_division.setId("div_division");
            Division div_detalle = new Division();
            div_detalle.setFooter(pat_panel2, gri_totales, "85%");

            div_division.dividir2(pat_panel1, div_detalle, "40%", "H");

            agregarComponente(div_division);

            sec_calendario.setId("sec_calendario");
            //por defecto friltra un mes
            sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getDate(), -31));
            sec_calendario.setFecha2(utilitario.getDate());
            agregarComponente(sec_calendario);
            sec_calendario.getBot_aceptar().setMetodo("aceptarRango");
            vpdf_ver.setTitle("Comprobante de Contabilidad");
            vpdf_ver.setId("vpdf_ver");
            agregarComponente(vpdf_ver);
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public void buscarTransaccion() {
        if (tex_num_transaccion.getValue() != null && !tex_num_transaccion.getValue().toString().isEmpty()) {
            com_tipo_comprobante.setValue(null);
            tab_tabla1.setCondicion("fecha_trans_cnccc >='" + con.obtenerFechaInicialPeriodoActivo() + "' and ide_cnccc=" + tex_num_transaccion.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            if (tab_tabla1.getTotalFilas() > 0) {
                if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm") != null && !tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm").isEmpty()) {
                    com_tipo_comprobante.setValue(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_cntcm"));
                }
                calcularTotal();
            } else {
                utilitario.agregarMensajeInfo("Atencion", "El numero de transaccion no existe");
                com_tipo_comprobante.setValue(null);
            }
            utilitario.addUpdate("tab_tabla1,tab_tabla2,gri_totales,com_tipo_comprobante");
        }
    }

    public void reversarComprobante() {
        if (tab_tabla1.getTotalFilas() > 0) {
            String ide_cnccc_anular = tab_tabla1.getValor("ide_cnccc");
            cls_contabilidad con = new cls_contabilidad();
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
                cls_contabilidad con = new cls_contabilidad();
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

//    public void insertarnuevascuentas(){
//        // desde el 4831
//        Tabla tab_nuevascuentas=utilitario.consultar("select * from plan_cuenta where ide_plan not in ( "
//                + "SELECT ide_plan from plan_cuenta pc, con_det_plan_cuen dpc "
//                + "where dpc.codig_recur_cndpc = pc.codigo) "
//                + "and codigo LIKE '5.%'");
//        System.out.println("lenght" + tab_nuevascuentas.getValor(0, "codigo").length());
//        int ide_cndpc=4921;
//        int ide_cnncu=1;
//        int ide_cntcu=2;
//        String nivel="";
//
//        
//        for (int i = 0; i < tab_nuevascuentas.getTotalFilas(); i++) {
//            if (tab_nuevascuentas.getValor(i, "codigo").length()-6==24){
//                ide_cnncu=7;
//                nivel="HIJO";
//            }
//            if (tab_nuevascuentas.getValor(i, "codigo").length()-6==19){
//                ide_cnncu=6;
//                nivel="PADRE";
//            }
//            if (tab_nuevascuentas.getValor(i, "codigo").length()-6==14){
//                ide_cnncu=5;
//                nivel="PADRE";
//            }
//            utilitario.getConexion().agregarSqlPantalla("INSERT INTO con_det_plan_cuen (ide_cndpc, ide_sucu, ide_cntcu, ide_cncpc, ide_cnncu, ide_empr, con_ide_cndpc, codig_recur_cndpc, nombre_cndpc, nivel_cndpc) VALUES ("+ide_cndpc+", '0', "+ide_cntcu+", '0', "+ide_cnncu+", 0, NULL, '"+tab_nuevascuentas.getValor(i, "codigo")+"', '"+tab_nuevascuentas.getValor(i, "nombre")+"', '"+nivel+"')");
//            ide_cndpc=ide_cndpc+1;
//        }
//        utilitario.getConexion().guardarPantalla();
//        
//    }
//    public void cargarplancuentas() {
//        Tabla tab_cuentas = utilitario.consultar("SELECT dpc.ide_cndpc,pc.nombre from plan_cuenta pc, con_det_plan_cuen dpc "
//                + "where dpc.codig_recur_cndpc = pc.codigo");
//        for (int i = 0; i < tab_cuentas.getTotalFilas(); i++) {
//            utilitario.getConexion().agregarSqlPantalla("update con_det_plan_cuen set nombre_cndpc='" + tab_cuentas.getValor(i, "nombre") + "' where ide_cndpc=" + tab_cuentas.getValor(i, "ide_cndpc"));
//        }
//        utilitario.getConexion().guardarPantalla();
//    }
    public void inicio() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void copiarComprobante() {
        if (!tab_tabla1.isFilaInsertada()) {

            TablaGenerica tab_cabecera = utilitario.consultar("SELECT * From con_cab_comp_cont where ide_cnccc=" + tab_tabla1.getValorSeleccionado());
            TablaGenerica tab_detalle = utilitario.consultar("SELECT * From con_det_comp_cont where ide_cnccc=" + tab_tabla1.getValorSeleccionado());

            tab_tabla1.insertar();
            tab_tabla1.setValor("IDE_MODU", tab_cabecera.getValor("IDE_MODU"));
            tab_tabla1.setValor("IDE_GEPER", tab_cabecera.getValor("IDE_GEPER"));
            tab_tabla1.setValor("OBSERVACION_CNCCC", tab_cabecera.getValor("OBSERVACION_CNCCC"));

            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("IDE_CNLAP", tab_detalle.getValor(i, "IDE_CNLAP"));
                tab_tabla2.setValor("IDE_CNDPC", tab_detalle.getValor(i, "IDE_CNDPC"));
                tab_tabla2.setValor("VALOR_CNDCC", tab_detalle.getValor(i, "VALOR_CNDCC"));
                tab_tabla2.setValor("OBSERVACION_CNDCC", tab_detalle.getValor(i, "OBSERVACION_CNDCC"));
            }
            calcularTotal();
            utilitario.addUpdate("tab_tabla1,tab_tabla2,gri_totales");
        } else {
            utilitario.agregarMensajeInfo("No se puede copiar el comprobante de contabilidad", "El comprobante seleccionado no se encuentra grabado");
        }
    }

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

    public boolean validarFechasConPeriodoActivo(String fecha_inicial) {
        if (utilitario.isFechaMenor(utilitario.getFecha(fecha_inicial), utilitario.getFecha(con.obtenerFechaInicialPeriodoActivo()))) {
            sec_calendario.setFecha1(utilitario.getFecha(con.obtenerFechaInicialPeriodoActivo()));
            utilitario.addUpdate("sec_calendario");
        }
        return true;
    }

    public void aceptarRango() {
        if (validarFechasConPeriodoActivo(sec_calendario.getFecha1String())) {
            if (sec_calendario.isFechasValidas()) {
                sec_calendario.cerrar();
                tab_tabla1.setCondicion("fecha_trans_cnccc between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and ide_cntcm=" + com_tipo_comprobante.getValue());
                tab_tabla1.ejecutarSql();
                tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
                utilitario.addUpdate("sec_calendario,tab_tabla1,tab_tabla2");
                calcularTotal();
                utilitario.addUpdate("gri_totales");
            } else {
                utilitario.agregarMensajeInfo("Fechas no válidas", "");
            }
        }

    }

    @Override
    public void abrirRangoFecha() {
        if (com_tipo_comprobante.getValue() != null) {
            sec_calendario.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tipo de comprobante", "");
        }

    }

    public void seleccionTipoComprobante() {
        if (com_tipo_comprobante.getValue() != null) {
            tex_num_transaccion.setValue(null);
            if (utilitario.isFechaMenor(sec_calendario.getFecha1(), utilitario.getFecha(con.obtenerFechaInicialPeriodoActivo()))) {
                sec_calendario.setFecha1(utilitario.getFecha(con.obtenerFechaInicialPeriodoActivo()));
                sec_calendario.setFecha2(utilitario.getDate());
                utilitario.addUpdate("sec_calendario");
            }
            tab_tabla1.setCondicion("fecha_trans_cnccc between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "' and ide_cntcm=" + com_tipo_comprobante.getValue());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } else {
            tex_num_transaccion.setValue(null);
            tab_tabla1.setCondicion("ide_cnccc=-1");
            tab_tabla1.ejecutarSql();
            tab_tabla2.setCondicion("ide_cndcc=-1");
            tab_tabla2.ejecutarSql();
        }
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
        }
    }

    @Override
    public void guardar() {
        if (validar()) {
            if (con.validarPeriodo(tab_tabla1.getValor("fecha_trans_cnccc"))) {
                if (tab_tabla1.isFilaInsertada()) {
                    tab_tabla1.setValor("hora_sistem_cnccc", utilitario.getHoraActual());
                    tab_tabla1.setValor("numero_cnccc", generarSecuencial());
                }
                tab_tabla1.guardar();
                tab_tabla2.guardar();
                utilitario.getConexion().guardarPantalla();
            }
        }

    }

    public void cambioLugarAplica(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public boolean validar() {
        cls_contabilidad con = new cls_contabilidad();
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (!con.esCuentaContableHija(tab_tabla2.getValor(i, "ide_cndpc"))) {
                utilitario.agregarMensajeError("Error al Guardar el Comprobante", "Una de las filas no tiene nivel HIJO");
                return false;
            }

        }
        if (tab_tabla1.getValor("fecha_trans_cnccc") == null || tab_tabla1.getValor("fecha_trans_cnccc").isEmpty()) {
            utilitario.agregarMensajeInfo("La fecha de transaccion esta vacia", "");
            return false;
        }
        if (tab_tabla1.getValor("observacion_cnccc") == null || tab_tabla1.getValor("observacion_cnccc").isEmpty()) {
            utilitario.agregarMensajeInfo("La observacion esta vacia", "");
            return false;
        }
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("No existe Beneficiario", "");
            return false;
        }

        if (!calcularTotal()) {
            utilitario.agregarMensajeInfo("La suma de los detalles del DEBE tiene que ser igual al del HABER", "");
            return false;
        }
        return true;

    }

    public boolean calcularTotal() {
        double dou_debe = 0;
        double dou_haber = 0;
        if (!com_tipo_comprobante.getValue().equals("5")) {
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
        } else {
            return true;
        }
    }

    public void ingresaCantidad(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    private String generarSecuencial() {
        //GENERA el número secuencial de la cabecera del comprobante
        String str_numero = null;
        String str_fecha = tab_tabla1.getValor("FECHA_TRANS_CNCCC");
        String str_ano = utilitario.getAnio(str_fecha) + "";
        String str_mes = utilitario.getMes(str_fecha) + "";
        String str_ide_sucu = utilitario.getVariable("ide_sucu");
        //SELECCIONA EL MAXIMO SEGUN EL MES Y EL AÑO 
        TablaGenerica tab_max = utilitario.consultar("SELECT count(NUMERO_CNCCC) as cod,max(cast( substr(NUMERO_CNCCC,8) as NUMERIC)) AS MAXIMO FROM CON_CAB_COMP_CONT WHERE extract(year from FECHA_TRANS_CNCCC) ='" + str_ano + "' AND extract(month from FECHA_TRANS_CNCCC) ='" + str_mes + "' AND IDE_SUCU=" + str_ide_sucu);

        String str_maximo = "0";
        if (tab_max.getTotalFilas() > 0) {
            str_maximo = tab_max.getValor("MAXIMO");
            if (str_maximo == null || str_maximo.isEmpty()) {
                str_maximo = "0";
            }
            long lon_siguiente = 0;
            try {
                lon_siguiente = Long.parseLong(str_maximo) + 1;
            } catch (Exception e) {
            }
            str_maximo = lon_siguiente + "";
        }
        str_maximo = utilitario.generarCero(8 - str_maximo.length()) + str_maximo;
        str_numero = str_ano + str_mes + str_ide_sucu + str_maximo;
        return str_numero;
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

    public String retornar_mes_letras(int mes) {
        String mes1 = "";
        if (mes == 1) {
            mes1 = "Enero";
        }
        if (mes == 2) {
            mes1 = "Febrero";
        }
        if (mes == 3) {
            mes1 = "Marzo";
        }
        if (mes == 4) {
            mes1 = "Abril";
        }
        if (mes == 5) {
            mes1 = "Mayo";
        }
        if (mes == 6) {
            mes1 = "Junio";
        }
        if (mes == 7) {
            mes1 = "Julio";
        }
        if (mes == 8) {
            mes1 = "Agosto";
        }
        if (mes == 9) {
            mes1 = "Septiembre";
        }
        if (mes == 10) {
            mes1 = "Octubre";
        }
        if (mes == 11) {
            mes1 = "Noviembre";
        }
        if (mes == 12) {
            mes1 = "Diciembre";
        }
        return mes1;
    }

    public String getFormatoFecha(String fecha) {
        String mes = retornar_mes_letras(utilitario.getMes(fecha));
        String dia = utilitario.getDia(fecha) + "";
        String anio = utilitario.getAnio(fecha) + "";
        String fecha_formato = dia + " de " + mes + " del " + anio;
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
    Map parametro = new HashMap();
    String fecha_fin;
    String fecha_inicio;

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
                parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
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
                        parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                        parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
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
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
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

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
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
}
