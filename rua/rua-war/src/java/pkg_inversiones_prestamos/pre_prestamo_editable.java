/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inversiones_prestamos;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import pkg_bancos.cls_bancos;
import pkg_contabilidad.VistaAsiento;
import pkg_contabilidad.cls_cab_comp_cont;
import pkg_contabilidad.cls_contabilidad;
import pkg_contabilidad.cls_det_comp_cont;
import pkg_cuentas_x_cobrar.cls_cuentas_x_cobrar;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_prestamo_editable extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_detalle_prestamo = new Tabla();//cliente con prestamo
    private Tabla tab_cabecera_prestamo = new Tabla();
    private Division div_division = new Division();
    private Etiqueta eti_total_capital = new Etiqueta();
    private Etiqueta eti_total_interes = new Etiqueta();
    private Etiqueta eti_total_cuota = new Etiqueta();
    private String str_p_con_tipo_comprobante_egreso = utilitario.getVariable("p_con_tipo_comprobante_egreso");
    private String str_p_con_tipo_comprobante_ingreso = utilitario.getVariable("p_con_tipo_comprobante_ingreso");
    private String p_est_com_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();
    private Boton bot_pago_prestamo = new Boton();
////////////////////////////////////////////////////////////////////    private Boton bot_buscar = new Boton();
    private Dialogo dia_pago_prestamo = new Dialogo();
    private AutoCompletar aut_beneficiario = new AutoCompletar();
    private Combo com_cuenta_bancaria = new Combo();
    private Check chk_hace_asiento = new Check();
    private Texto tex_doc_banco = new Texto();
////////////////////////////////////////////////////////////////////////    private Texto tex_buscar = new Texto();
    private Combo com_tipo_prestamo = new Combo();
    private Boton bot_calcular_tabla_amortizacion = new Boton();
    private cls_contabilidad conta = new cls_contabilidad();
    private cls_cab_comp_cont cab_com_con;
    private List<cls_det_comp_cont> lista_detalles = new ArrayList();
    private VistaAsiento via_asiento = new VistaAsiento();
    private Texto tex_num_prestamo = new Texto();
    ////PARA LA FACTURA
    private Dialogo dia_factura = new Dialogo();
    private Tabla tab_persona = new Tabla();
    private Tabla tab_cab_factura = new Tabla();
    private Tabla tab_det_factura = new Tabla();
    private String str_cuenta_contable_banco = "";//Cuenta contable del banco
    private boolean boo_tipo_asiento_pago_prestamo_ingreso; //para saber si el asiento generado es de tipo ingreso
    private String cuentaCliente = ""; //mensajes niña
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private Radio rad_bancos_caja_casas = new Radio();
    private Dialogo dia_bancos_caja_transcasas = new Dialogo();
    private Etiqueta eti_cuenta_banco;
    private Etiqueta eti_doc_banco;

    public pre_prestamo_editable() {
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");
        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.getBot_insertar().setUpdate("tab_tabla1,tab_tabla2,tab_detalle_prestamo");
            bar_botones.getBot_guardar().setUpdate("tab_tabla1,tab_tabla2,tab_detalle_prestamo");
            bar_botones.getBot_eliminar().setUpdate("tab_tabla1,tab_tabla2,tab_detalle_prestamo");
            bar_botones.agregarReporte();
            bar_botones.getBot_inicio().setMetodo("inicio");
            bar_botones.getBot_fin().setMetodo("fin");
            bar_botones.getBot_siguiente().setMetodo("siguiente");
            bar_botones.getBot_atras().setMetodo("atras");
            bar_botones.agregarComponente(new Etiqueta("Tipo Prestamo"));
            bar_botones.agregarComponente(com_tipo_prestamo);
            //////////////////--------------

////////////////////////////////////////////////////////////////////////////////////            tex_buscar.setId("tex_buscar");
////////////////////////////////////////////////////////////////////////////////////            tex_buscar.setSoloEnteros();
////////////////////////////////////////////////////////////////////////////////////            tex_buscar.setSize(10);
////////////////////////////////////////////////////////////////////////////////////            bot_buscar.setTitle("Buscar Transaccion");
////////////////////////////////////////////////////////////////////////////////////            bot_buscar.setIcon("ui-icon-search");
////////////////////////////////////////////////////////////////////////////////////            bot_buscar.setMetodo("cargarPrestamoNumero");
////////////////////////////////////////////////////////////////////////////////////            bot_buscar.setUpdate("tab_cabecera_prestamo,tab_detalle_prestamo,aut_beneficiario,com_cuenta_bancaria");
            //bot_buscar.setMetodo("");
////////////////////////////////////////////////////////////////////////////////////////            Grid gri_busca = new Grid();
////////////////////////////////////////////////////////////////////////////////////////            gri_busca.setWidth("100%");
////////////////////////////////////////////////////////////////////////////////////////            gri_busca.setColumns(3);
////////////////////////////////////////////////////////////////////////////////////////            gri_busca.getChildren().add(new Etiqueta("Num Prestamo: "));
////////////////////////////////////////////////////////////////////////////////////////            gri_busca.getChildren().add(tex_buscar);
////////////////////////////////////////////////////////////////////////////////////////            gri_busca.getChildren().add(bot_buscar);
////////////////////////////////////////////////////////////////////////////////////////            bar_botones.agregarComponente(gri_busca);
//            bot_busca.setIcon("ui-icon-search");
//            bot_busca.setMetodo("cargarPrestamoNumero");
//            bot_busca.setUpdate("tab_cabecera_prestamo,tab_detalle_prestamo,aut_beneficiario,com_cuenta_bancaria");
//            gri_busca_num.getChildren().add(bot_busca);
            ////////---
            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");

            sel_formato.setId("sel_formato");
            sel_formato.getBot_aceptar().setMetodo("aceptarReporte");
            com_tipo_prestamo.setId("com_tipo_prestamo");
            com_tipo_prestamo.setMetodo("cargarTipoPrestamo");
            List lista = new ArrayList();
            Object fila1[] = {
                "true", "INGRESO"
            };
            Object fila2[] = {
                "false", "EGRESO"
            };
            lista.add(fila1);
            lista.add(fila2);
            com_tipo_prestamo.setCombo(lista);
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("iyp_cab_prestamo", "ide_ipcpr", 1);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.setCondicion("ide_ipcpr=-1");
            tab_tabla1.getColumna("ide_iptpr").setCombo("iyp_tipo_prestamo", "ide_iptpr", "nombre_iptpr", "");
            tab_tabla1.getColumna("ide_iptpr").setValorDefecto(utilitario.getVariable("p_iyp_tipo_prestamo"));
            tab_tabla1.getColumna("ide_ipepr").setCombo("iyp_estado_prestamos", "ide_ipepr", "nombre_ipepr", "");
            tab_tabla1.getColumna("ide_ipepr").setValorDefecto(utilitario.getVariable("p_iyp_estado_normal"));
            tab_tabla1.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "nivel_cndpc='HIJO' and ide_cncpc=" + lis_plan.get(0));
            tab_tabla1.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("fecha_transaccion_ipcpr").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            tab_tabla1.getColumna("ide_tecba").setRequerida(true);
            tab_tabla1.getColumna("monto_ipcpr").setRequerida(true);
            tab_tabla1.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla1.getColumna("interes_ipcpr").setRequerida(true);
            tab_tabla1.getColumna("num_pagos_ipcpr").setRequerida(true);
            tab_tabla1.getColumna("ide_ipepr").setRequerida(true);
            tab_tabla1.getColumna("ide_iptpr").setRequerida(true);
            tab_tabla1.getColumna("num_dias_ipcpr").setRequerida(true);
            tab_tabla1.getColumna("observacion_ipcpr").setRequerida(true);
            tab_tabla1.getColumna("fecha_sistema_ipcpr").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_sistema_ipcpr").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("fecha_sistema_ipcpr").setVisible(false);
            tab_tabla1.getColumna("hora_sistema_ipcpr").setVisible(false);
            tab_tabla1.getColumna("genera_asiento_ipcpr").setValorDefecto("true");
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(6);
            tab_tabla1.getColumna("es_ingreso_ipcpr").setVisible(false);
            tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_geper").setRequerida(true);
            tab_tabla1.getColumna("fecha_sistema_ipcpr").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("hora_sistema_ipcpr").setValorDefecto(utilitario.getHoraActual());
            tab_tabla1.getColumna("fecha_sistema_ipcpr").setVisible(false);
            tab_tabla1.getColumna("hora_sistema_ipcpr").setVisible(false);
            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("iyp_deta_prestamo", "ide_ipdpr", 2);
            tab_tabla2.setCampoOrden("ide_ipdpr desc");
            ////
            //tab_tabla2.setCampoPadre("ide_ipcpr");
            ////
            tab_tabla2.getColumna("ide_tecba").setCombo("select tes_banco.ide_teban,nombre_teban,nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
            tab_tabla2.getColumna("pagado_ipdpr").setValorDefecto("false");
            tab_tabla2.setCampoOrden("num_ipdpr asc");
            tab_tabla2.setRows(10);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            tab_cabecera_prestamo.setId("tab_cabecera_prestamo");
            tab_cabecera_prestamo.setNumeroTabla(6);
            tab_cabecera_prestamo.setSql("SELECT cab.ide_ipcpr as ide_pago,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr,cab.es_ingreso_ipcpr,cab.ide_geper,cab.ide_tecba FROM iyp_cab_prestamo cab, iyp_deta_prestamo deta "
                    + " WHERE cab.ide_geper=-1 "
                    + " and cab.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "AND cab.ide_ipcpr=deta.ide_ipcpr AND deta.pagado_ipdpr IS FALSE "
                    + "GROUP BY cab.ide_ipcpr,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr");
            tab_cabecera_prestamo.setCampoPrimaria("ide_pago");
            //  tab_cabecera_prestamo.onSelect("seleccionar_tabla6", "tab_detalle_prestamo,aut_beneficiario,com_cuenta_bancaria,tex_num_prestamo");
            tab_cabecera_prestamo.getColumna("es_ingreso_ipcpr").setCombo(lista);
            tab_cabecera_prestamo.getColumna("num_prestamo_ipcpr").setNombreVisual("PRESTAMO");
            tab_cabecera_prestamo.getColumna("num_pagos_ipcpr").setNombreVisual("PAGOS");
            tab_cabecera_prestamo.getColumna("num_dias_ipcpr").setNombreVisual("DIAS");
            tab_cabecera_prestamo.getColumna("fecha_prestamo_ipcpr").setNombreVisual("FECHA");
            tab_cabecera_prestamo.getColumna("monto_ipcpr").setNombreVisual("MONTO");
            tab_cabecera_prestamo.getColumna("interes_ipcpr").setNombreVisual("INTERES");
            tab_cabecera_prestamo.getColumna("es_ingreso_ipcpr").setNombreVisual("TIPO PRESTAMO");
            tab_cabecera_prestamo.getColumna("ide_tecba").setVisible(false);
            tab_cabecera_prestamo.getColumna("ide_geper").setVisible(false);
            tab_cabecera_prestamo.setScrollable(true);
            tab_cabecera_prestamo.setScrollHeight(60);
            tab_cabecera_prestamo.setLectura(true);
            tab_cabecera_prestamo.dibujar();

            //Cliente con prestamos
            tab_detalle_prestamo.setId("tab_detalle_prestamo");
            tab_detalle_prestamo.setNumeroTabla(3);
            tab_detalle_prestamo.setSql("SELECT dp.ide_ipdpr,dp.ide_ipcpr,dp.num_ipdpr,dp.fecha_ipdpr,dp.capital_ipdpr,dp.interes_ipdpr,dp.cuota_ipdpr,dp.pagado_ipdpr,cp.ide_cndpc "
                    + "FROM iyp_deta_prestamo dp,iyp_cab_prestamo cp WHERE dp.ide_ipcpr=" + tab_cabecera_prestamo.getValor("ide_ipcpr") + " "
                    + "AND dp.pagado_ipdpr is FALSE and dp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and cp.ide_ipcpr=dp.ide_ipcpr "
                    + "ORDER BY dp.num_ipdpr,dp.fecha_ipdpr ASC");
            tab_detalle_prestamo.setCampoPrimaria("ide_ipdpr");
            tab_detalle_prestamo.getColumna("ide_ipcpr").setEtiqueta();
            tab_detalle_prestamo.getColumna("num_ipdpr").setEtiqueta();
            tab_detalle_prestamo.getColumna("fecha_ipdpr").setEtiqueta();
            tab_detalle_prestamo.getColumna("capital_ipdpr").setEtiqueta();
            tab_detalle_prestamo.getColumna("interes_ipdpr").setEtiqueta();
            tab_detalle_prestamo.getColumna("cuota_ipdpr").setEtiqueta();
            tab_detalle_prestamo.getColumna("ide_cndpc").setVisible(false);
            tab_detalle_prestamo.getColumna("ide_ipcpr").setLectura(false);
            tab_detalle_prestamo.getColumna("num_ipdpr").setNombreVisual("NUMERO");
            tab_detalle_prestamo.getColumna("fecha_ipdpr").setNombreVisual("FECHA");
            tab_detalle_prestamo.getColumna("capital_ipdpr").setNombreVisual("CAPITAL");
            tab_detalle_prestamo.getColumna("interes_ipdpr").setNombreVisual("INTERES");
            tab_detalle_prestamo.getColumna("cuota_ipdpr").setNombreVisual("CUOTA");
            tab_detalle_prestamo.getColumna("pagado_ipdpr").setNombreVisual("PAGAR");
            tab_detalle_prestamo.dibujar();

            Espacio esp = new Espacio();
            esp.setWidth("5");
            esp.setHeight("1");
            Espacio esp1 = new Espacio();
            esp1.setWidth("50");
            esp1.setHeight("1");
            Espacio esp2 = new Espacio();
            esp2.setWidth("50");
            esp2.setHeight("1");
            Espacio esp3 = new Espacio();
            esp3.setWidth("50");
            esp3.setHeight("1");

            eti_total_capital.setId("eti_total_capital");
            eti_total_capital.setStyle("font-size: 16px;color: black;font-weight: bold");
            eti_total_interes.setId("eti_total_interes");
            eti_total_interes.setStyle("font-size: 16px;color: black;font-weight: bold");
            eti_total_cuota.setId("eti_total_cuota");
            eti_total_cuota.setStyle("font-size: 16px;color: black;font-weight: bold");

            Grid grid_matriz = new Grid();
            grid_matriz.setId("grid_matriz");
            grid_matriz.setColumns(7);
            grid_matriz.setStyle("width: 98%;text-align: left; padding-right: 10%;float: right;overflow: hidden;");

            grid_matriz.getChildren().add(esp);
            grid_matriz.getChildren().add(eti_total_capital);
            grid_matriz.getChildren().add(esp1);
            grid_matriz.getChildren().add(eti_total_interes);
            grid_matriz.getChildren().add(esp2);
            grid_matriz.getChildren().add(eti_total_cuota);
            grid_matriz.getChildren().add(esp3);

            via_asiento.setId("via_asiento");
            via_asiento.getBot_aceptar().setMetodo("aceptarVistaAsiento");
            via_asiento.getBot_cancelar().setMetodo("cancelarDialogo");
            gru_pantalla.getChildren().add(via_asiento);
            bot_pago_prestamo.setId("bot_pago_prestamo");

////////////////////////////////////////////////////////////////////////////////////            bot_buscar.setId("bot_buscar");
            bot_pago_prestamo.setValue("Pago de Prestamos");
            bot_pago_prestamo.setMetodo("abrirPagoPrestamo");
            bot_calcular_tabla_amortizacion.setId("bot_calcular_tabla_amortizacion");
            bot_calcular_tabla_amortizacion.setValue("Calcular Amortización");
            bot_calcular_tabla_amortizacion.setMetodo("calcularCuotaFija");
            aut_beneficiario.setId("aut_beneficiario");
            aut_beneficiario.setAutoCompletar("select ide_geper,nom_geper from gen_persona where es_cliente_geper is true or es_proveedo_geper is true and upper(nivel_geper)='HIJO'");
            aut_beneficiario.setMetodoChange("cargarPrestamoBeneficiario");
            tex_num_prestamo.setId("tex_num_prestamo");
            tex_num_prestamo.setSoloEnteros();

            Grid gri_busca_num = new Grid();
            gri_busca_num.setColumns(2);
            gri_busca_num.getChildren().add(tex_num_prestamo);
            Boton bot_busca = new Boton();
            bot_busca.setTitle("Buscar por Número de Prestamo");
            bot_busca.setIcon("ui-icon-search");
            bot_busca.setMetodo("cargarPrestamoNumero");
            bot_busca.setUpdate("tab_cabecera_prestamo,tab_detalle_prestamo,aut_beneficiario,com_cuenta_bancaria");
            gri_busca_num.getChildren().add(bot_busca);

            chk_hace_asiento.setId("chk_hace_asiento");
            tex_doc_banco.setId("tex_doc_banco");
            tex_doc_banco.setSoloNumeros();

            com_cuenta_bancaria.setCombo(tab_tabla1.getColumna("ide_tecba").getListaCombo());
            com_cuenta_bancaria.setId("com_cuenta_bancaria");
            tex_num_prestamo.setId("tex_num_prestamo");

            Grid gri_datos_prestamo = new Grid();
            gri_datos_prestamo.setColumns(2);
            gri_datos_prestamo.getChildren().add(new Etiqueta("Número de Prestamo "));
            gri_datos_prestamo.getChildren().add(gri_busca_num);
            gri_datos_prestamo.getChildren().add(new Etiqueta("Beneficiario"));
            gri_datos_prestamo.getChildren().add(aut_beneficiario);
            gri_datos_prestamo.getChildren().add(eti_cuenta_banco = new Etiqueta("Cuenta Bancaria"));
            gri_datos_prestamo.getChildren().add(com_cuenta_bancaria);
            gri_datos_prestamo.getChildren().add(eti_doc_banco = new Etiqueta("Documento Bancario"));
            gri_datos_prestamo.getChildren().add(tex_doc_banco);

            gri_datos_prestamo.getChildren().add(new Etiqueta("Genera Asiento"));
            gri_datos_prestamo.getChildren().add(chk_hace_asiento);

            Grid gri_tabla_cliente = new Grid();
            gri_tabla_cliente.setColumns(1);
            gri_tabla_cliente.getChildren().add(tab_detalle_prestamo);

            Grid gri_pago_prestamo = new Grid();
            gri_pago_prestamo.setColumns(1);
            gri_pago_prestamo.getChildren().add(gri_datos_prestamo);
            gri_pago_prestamo.getChildren().add(tab_cabecera_prestamo);
            gri_pago_prestamo.getChildren().add(gri_tabla_cliente);

            dia_pago_prestamo.setId("dia_pago_prestamo");
            dia_pago_prestamo.setWidth("80%");
            dia_pago_prestamo.setDynamic(false);
            dia_pago_prestamo.setHeight("70%");
            dia_pago_prestamo.setHeader("Pago de Prestamos Realizados");
            dia_pago_prestamo.setDialogo(gri_pago_prestamo);
            dia_pago_prestamo.getBot_aceptar().setMetodo("aceptarPagoPrestamo");
            gri_pago_prestamo.setStyle("width:" + (dia_pago_prestamo.getAnchoPanel() - 5) + "px;height:" + dia_pago_prestamo.getAltoPanel() + "px;overflow: auto;display: block;");

            ///PARA LA FACTURA
            tab_persona.setId("tab_persona");
            tab_persona.setTabla("gen_persona", "ide_geper", -1);
            //oculto todas las columnas
            for (int i = 0; i < tab_persona.getTotalColumnas(); i++) {
                tab_persona.getColumnas()[i].setVisible(false);
            }
            utilitario.buscarNombresVisuales(tab_persona);
            tab_persona.setCondicion("ide_geper=-1");
            tab_persona.setTipoFormulario(true);
            tab_persona.getColumna("nom_geper").setVisible(true);
            tab_persona.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "");
            tab_persona.getColumna("ide_getid").setVisible(true);
            tab_persona.getColumna("identificac_geper").setVisible(true);
            tab_persona.getColumna("direccion_geper").setVisible(true);
            tab_persona.getColumna("telefono_geper").setVisible(true);
            tab_persona.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
            tab_persona.getColumna("ide_geubi").setVisible(true);
            tab_persona.setTipoFormulario(true);
            tab_persona.getGrid().setColumns(4);
            tab_persona.setMostrarNumeroRegistros(false);
            tab_persona.dibujar();

            tab_cab_factura.setId("tab_cab_factura");
            tab_cab_factura.setMostrarNumeroRegistros(false);
            tab_cab_factura.setTabla("cxc_cabece_factura", "ide_cccfa", -1);
            utilitario.buscarNombresVisuales(tab_cab_factura);
            tab_cab_factura.getColumna("ide_vgven").setVisible(false);
            tab_cab_factura.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));
            tab_cab_factura.getColumna("ide_cntdo").setVisible(false);
            tab_cab_factura.getColumna("ide_ccefa").setValorDefecto(utilitario.getVariable("p_cxc_estado_factura_normal"));
            tab_cab_factura.getColumna("ide_ccefa").setVisible(false);
            tab_cab_factura.getColumna("ide_geubi").setVisible(false);
            tab_cab_factura.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_cab_factura.getColumna("ide_usua").setVisible(false);
            tab_cab_factura.getColumna("ide_cnccc").setLectura(false);
            tab_cab_factura.getColumna("secuencial_cccfa").setMascara("9999999");
            tab_cab_factura.getColumna("fecha_trans_cccfa").setValorDefecto(utilitario.getFechaActual());
            tab_cab_factura.getColumna("fecha_trans_cccfa").setVisible(false);
            tab_cab_factura.getColumna("fecha_emisi_cccfa").setValorDefecto(utilitario.getFechaActual());
            tab_cab_factura.getColumna("ide_ccdaf").setCombo("cxc_datos_fac", "ide_ccdaf", "serie_ccdaf, autorizacion_ccdaf,observacion_ccdaf", "");

            tab_cab_factura.getColumna("ide_geper").setVisible(false);
            tab_cab_factura.getColumna("direccion_cccfa").setVisible(false);
            tab_cab_factura.getColumna("pagado_cccfa").setValorDefecto("false");
            tab_cab_factura.getColumna("pagado_cccfa").setVisible(false);
            tab_cab_factura.getColumna("ide_cncre").setVisible(false);
            tab_cab_factura.getColumna("total_cccfa").setEtiqueta();
            tab_cab_factura.getColumna("total_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_cab_factura.getColumna("total_cccfa").setValorDefecto("0");
            tab_cab_factura.getColumna("secuencial_cccfa").setEstilo("font-size: 15px;font-weight: bold");
            tab_cab_factura.getColumna("secuencial_cccfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");

            tab_cab_factura.getColumna("base_grabada_cccfa").setEtiqueta();
            tab_cab_factura.getColumna("base_grabada_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_cab_factura.getColumna("base_grabada_cccfa").setValorDefecto("0");
            tab_cab_factura.getColumna("valor_iva_cccfa").setEtiqueta();
            tab_cab_factura.getColumna("valor_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_cab_factura.getColumna("valor_iva_cccfa").setValorDefecto("0");
            tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setEtiqueta();
            tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setValorDefecto("0");
            tab_cab_factura.getColumna("base_tarifa0_cccfa").setEtiqueta();
            tab_cab_factura.getColumna("base_tarifa0_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_cab_factura.getColumna("base_tarifa0_cccfa").setValorDefecto("0");
            tab_cab_factura.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "");
            tab_cab_factura.getColumna("ide_cndfp").setPermitirNullCombo(false);

            tab_cab_factura.setTipoFormulario(true);
            tab_cab_factura.getGrid().setColumns(4);
            tab_cab_factura.agregarRelacion(tab_det_factura);
            tab_cab_factura.setCondicion("ide_cccfa=-1");
            tab_cab_factura.setCampoOrden("ide_cccfa desc");
            tab_cab_factura.dibujar();

            tab_det_factura.setId("tab_det_factura");
            tab_det_factura.setTabla("cxc_deta_factura", "ide_ccdfa", -1);
            utilitario.buscarNombresVisuales(tab_det_factura);
            tab_det_factura.getColumna("ide_inarti").setValorDefecto(utilitario.getVariable("p_iyp_aporte_deta_factura"));
            tab_det_factura.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "ide_inarti=" + utilitario.getVariable("p_iyp_aporte_deta_factura"));
            tab_det_factura.getColumna("ide_inarti").setAutoCompletar();
            // tab_det_factura.getColumna("ide_inarti").setLectura(true);
            tab_det_factura.getColumna("secuencial_ccdfa").setVisible(false);
            tab_det_factura.getColumna("alterno_ccdfa").setValorDefecto("00");
            tab_det_factura.getColumna("alterno_ccdfa").setVisible(false);
            tab_det_factura.getColumna("credito_tributario_ccdfa").setVisible(false);
            tab_det_factura.getColumna("credito_tributario_ccdfa").setValorDefecto("false");
            tab_det_factura.getColumna("total_ccdfa").setEtiqueta();
            tab_det_factura.getColumna("total_ccdfa").setEstilo("font-size:13px;font-weight: bold;");
            tab_det_factura.getColumna("cantidad_ccdfa").setLectura(true);
            tab_det_factura.getColumna("precio_ccdfa").setLectura(true);
            tab_det_factura.dibujar();

            dia_factura.setId("dia_factura");
            dia_factura.setWidth("80%");
            dia_factura.getBot_aceptar().setMetodo("aceptarFactura");
            dia_factura.setHeight("80%");
            dia_factura.setTitle("FACTURA");
            dia_factura.setDynamic(false);//los valores no se pasan

            Grid gri_fac = new Grid();
            gri_fac.setStyle("width:" + (dia_factura.getAnchoPanel() - 5) + "px;height:" + dia_factura.getAltoPanel() + "px;overflow: auto;display: block;");
            gri_fac.getChildren().add(tab_persona);
            gri_fac.getChildren().add(tab_cab_factura);
            gri_fac.getChildren().add(tab_det_factura);

            dia_factura.setDialogo(gri_fac);

            gru_pantalla.getChildren().add(dia_factura);

            div_division.setId("div_division");
            div_division.dividir3(pat_panel1, pat_panel2, grid_matriz, "57%", "6%", "H");
            gru_pantalla.getChildren().add(bar_botones);
            gru_pantalla.getChildren().add(div_division);
            gru_pantalla.getChildren().add(rep_reporte);
            gru_pantalla.getChildren().add(sel_formato);
            gru_pantalla.getChildren().add(dia_pago_prestamo);

            sec_rango_reporte.setId("sec_rango_reporte");
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sec_rango_reporte.setMultiple(false);
            agregarComponente(sec_rango_reporte);

            dia_bancos_caja_transcasas.setId("dia_bancos_caja_transcasas");
            dia_bancos_caja_transcasas.setTitle("Tipo de pago de prestamos");
            dia_bancos_caja_transcasas.setDynamic(false);
            dia_bancos_caja_transcasas.setWidth("30%");
            dia_bancos_caja_transcasas.setHeight("30%");

            List lista2 = new ArrayList();
            Object fila21[] = {
                "0", "BANCOS - CAJA"
            };
            Object fila31[] = {
                "1", "TRANSFERENCIA ENTRE CASAS"
            };
            lista2.add(fila21);
            lista2.add(fila31);

            rad_bancos_caja_casas.setRadio(lista2);
            rad_bancos_caja_casas.setVertical();

            Grid gri_interes_capital = new Grid();
            gri_interes_capital.getChildren().add(rad_bancos_caja_casas);
            gri_interes_capital.setColumns(2);

            dia_bancos_caja_transcasas.setDialogo(gri_interes_capital);
            dia_bancos_caja_transcasas.getBot_aceptar().setMetodo("aceptarPagoBancoCajaTranferenciaCasas");
            agregarComponente(dia_bancos_caja_transcasas);

            totales();

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }

    }
    boolean boo_banco_caja = false;

    public void aceptarPagoBancoCajaTranferenciaCasas() {
        if (rad_bancos_caja_casas.getValue().toString().equals("0")) {
            System.out.println("ingreso al banco - casas");
            com_cuenta_bancaria.setRendered(true);
            tex_doc_banco.setRendered(true);
            eti_cuenta_banco.setRendered(true);
            eti_doc_banco.setRendered(true);
            boo_banco_caja = true;
        } else {
            System.out.println("ingreso a transferencia casas");
            com_cuenta_bancaria.setRendered(false);
            tex_doc_banco.setRendered(false);
            eti_cuenta_banco.setRendered(false);
            eti_doc_banco.setRendered(false);
            boo_banco_caja = false;
        }
        tab_detalle_prestamo.limpiar();
        tab_cabecera_prestamo.limpiar();
        aut_beneficiario.limpiar();
        tex_doc_banco.limpiar();
        dia_pago_prestamo.dibujar();
        tex_num_prestamo.limpiar();
        chk_hace_asiento.setValue("true");
        utilitario.addUpdate("dia_pago_prestamo");
    }

    public void abrirPagoPrestamo() {
        dia_bancos_caja_transcasas.dibujar();
//        tab_detalle_prestamo.limpiar();
//        tab_cabecera_prestamo.limpiar();
//        aut_beneficiario.limpiar();
//        tex_doc_banco.limpiar();
//        dia_pago_prestamo.dibujar();
//        tex_num_prestamo.limpiar();
//        chk_hace_asiento.setValue("true");
//        utilitario.addUpdate("dia_pago_prestamo");
    }

    public void cargarPrestamoBeneficiario() {
        tab_cabecera_prestamo.setSql("SELECT cab.ide_ipcpr as ide_pago,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr,cab.es_ingreso_ipcpr,cab.ide_geper,cab.ide_tecba FROM iyp_cab_prestamo cab, iyp_deta_prestamo deta "
                + " WHERE cab.ide_geper=" + aut_beneficiario.getValor() + " "
                + "and cab.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + " AND cab.ide_ipcpr=deta.ide_ipcpr AND deta.pagado_ipdpr IS FALSE "
                + "GROUP BY cab.ide_ipcpr,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr");
        tab_cabecera_prestamo.ejecutarSql();
        tex_num_prestamo.setValue(tab_cabecera_prestamo.getValor("ide_pago"));
        com_cuenta_bancaria.setValue(tab_cabecera_prestamo.getValor("ide_tecba"));
        tab_detalle_prestamo.setSql("SELECT dp.ide_ipdpr,dp.ide_ipcpr,dp.num_ipdpr,dp.fecha_ipdpr,dp.capital_ipdpr,dp.interes_ipdpr,dp.cuota_ipdpr,dp.pagado_ipdpr,cp.ide_cndpc "
                + "FROM iyp_deta_prestamo dp,iyp_cab_prestamo cp WHERE dp.ide_ipcpr=" + tab_cabecera_prestamo.getValorSeleccionado() + " "
                + "AND dp.pagado_ipdpr is FALSE and dp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "and cp.ide_ipcpr=dp.ide_ipcpr "
                + "ORDER BY dp.num_ipdpr,dp.fecha_ipdpr ASC");
        tab_detalle_prestamo.ejecutarSql();
//        if (tab_detalle_prestamo.getTotalFilas() > 0) {
//            tab_detalle_prestamo.setValor(0, "pagado_ipdpr", "true");
//        }
        utilitario.addUpdate("tex_num_prestamo,com_cuenta_bancaria");
    }

    public void cargarPrestamoNumero() {
        if (tex_num_prestamo.getValue() != null && !tex_num_prestamo.getValue().toString().isEmpty()) {
            tab_cabecera_prestamo.setSql("SELECT cab.ide_ipcpr as ide_pago,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr,cab.es_ingreso_ipcpr,cab.ide_geper,cab.ide_tecba FROM iyp_cab_prestamo cab, iyp_deta_prestamo deta "
                    + " WHERE cab.num_prestamo_ipcpr=" + tex_num_prestamo.getValue() + " "
                    + "and cab.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + " AND cab.ide_ipcpr=deta.ide_ipcpr AND deta.pagado_ipdpr IS FALSE "
                    + "GROUP BY cab.ide_ipcpr,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr");
            tab_cabecera_prestamo.ejecutarSql();
            if (tab_cabecera_prestamo.getTotalFilas() == 0) {
                tab_cabecera_prestamo.setSql("SELECT cab.ide_ipcpr as ide_pago,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr,cab.es_ingreso_ipcpr,cab.ide_geper,cab.ide_tecba FROM iyp_cab_prestamo cab, iyp_deta_prestamo deta "
                        + " WHERE cab.ide_ipcpr=" + tex_num_prestamo.getValue() + " "
                        + "and cab.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + " AND cab.ide_ipcpr=deta.ide_ipcpr AND deta.pagado_ipdpr IS FALSE "
                        + "GROUP BY cab.ide_ipcpr,cab.num_prestamo_ipcpr,cab.num_pagos_ipcpr,cab.num_dias_ipcpr,cab.fecha_prestamo_ipcpr,cab.monto_ipcpr,cab.interes_ipcpr");
                tab_cabecera_prestamo.ejecutarSql();

            }
            tab_detalle_prestamo.setSql("SELECT dp.ide_ipdpr,dp.ide_ipcpr,dp.num_ipdpr,dp.fecha_ipdpr,dp.capital_ipdpr,dp.interes_ipdpr,dp.cuota_ipdpr,dp.pagado_ipdpr,cp.ide_cndpc "
                    + "FROM iyp_deta_prestamo dp,iyp_cab_prestamo cp WHERE dp.ide_ipcpr=" + tab_cabecera_prestamo.getValorSeleccionado() + " "
                    + "AND dp.pagado_ipdpr is FALSE and dp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and cp.ide_ipcpr=dp.ide_ipcpr "
                    + "ORDER BY dp.num_ipdpr,dp.fecha_ipdpr ASC");
            tab_detalle_prestamo.ejecutarSql();

            System.out.println("ide_geper " + tab_cabecera_prestamo.getValor("ide_geper"));
            aut_beneficiario.setValor(tab_cabecera_prestamo.getValor("ide_geper"));
            com_cuenta_bancaria.setValue(tab_cabecera_prestamo.getValor("ide_tecba"));
            tab_detalle_prestamo.setSql("SELECT dp.ide_ipdpr,dp.ide_ipcpr,dp.num_ipdpr,dp.fecha_ipdpr,dp.capital_ipdpr,dp.interes_ipdpr,dp.cuota_ipdpr,dp.pagado_ipdpr,cp.ide_cndpc "
                    + "FROM iyp_deta_prestamo dp,iyp_cab_prestamo cp WHERE dp.ide_ipcpr=" + tab_cabecera_prestamo.getValorSeleccionado() + " "
                    + "AND dp.pagado_ipdpr is FALSE and dp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and cp.ide_ipcpr=dp.ide_ipcpr "
                    + "ORDER BY dp.num_ipdpr,dp.fecha_ipdpr ASC");
            tab_detalle_prestamo.ejecutarSql();
//            if (tab_detalle_prestamo.getTotalFilas() > 0) {
//                tab_detalle_prestamo.setValor(0, "pagado_ipdpr", "true");
//            }
        }
    }

    public void seleccionar_tabla6(SelectEvent evt) {
        tab_cabecera_prestamo.seleccionarFila(evt);
        tex_num_prestamo.setValue(tab_cabecera_prestamo.getValor("ide_pago"));
        com_cuenta_bancaria.setValue(tab_cabecera_prestamo.getValor("ide_tecba"));
        tab_detalle_prestamo.setSql("SELECT dp.ide_ipdpr,dp.ide_ipcpr,dp.num_ipdpr,dp.fecha_ipdpr,dp.capital_ipdpr,dp.interes_ipdpr,dp.cuota_ipdpr,dp.pagado_ipdpr,cp.ide_cndpc "
                + "FROM iyp_deta_prestamo dp,iyp_cab_prestamo cp WHERE dp.ide_ipcpr=" + tab_cabecera_prestamo.getValorSeleccionado() + " "
                + "AND dp.pagado_ipdpr is FALSE and dp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "and cp.ide_ipcpr=dp.ide_ipcpr "
                + "ORDER BY dp.num_ipdpr,dp.fecha_ipdpr ASC");
        tab_detalle_prestamo.ejecutarSql();
        if (tab_detalle_prestamo.getTotalFilas() > 0) {
            tab_detalle_prestamo.setValor(0, "pagado_ipdpr", "true");
        }
    }

    public void aceptarFactura() {
        if (validarFactura()) {
            if (chk_hace_asiento.getValue().toString().equals("true")) {
                generarAsientoContabilidadPagoPrestamo();
            }
            tab_cab_factura.setValor("ide_geubi", tab_persona.getValor("ide_geubi"));
            tab_cab_factura.setValor("direccion_cccfa", tab_persona.getValor("direccion_geper"));
            dia_factura.cerrar();
            utilitario.addUpdate("dia_factura");

        }
    }

    private boolean validarFactura() {
        if (tab_persona.getValor("direccion_geper") == null || tab_persona.getValor("direccion_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar la Dirección");
            return false;
        }
        if (tab_cab_factura.getValor("ide_ccdaf") == null || tab_cab_factura.getValor("ide_ccdaf").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe seleccionar los Datos Factura");
            return false;
        }
        if (tab_cab_factura.getValor("secuencial_cccfa") == null || tab_cab_factura.getValor("secuencial_cccfa").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe ingresar la Secuencia de la Factura");
            return false;
        }
        return true;
    }

    public void aceptarPagoPrestamo() {
        if (validarPagoPrestamo()) {

            if (tab_cabecera_prestamo.getValor("es_ingreso_ipcpr").equals("false")) {
                if (chk_hace_asiento.getValue().toString().equals("true")) {
                    //generarAsientoContabilidadPagoPrestamo();
                    if (boo_banco_caja) {
                        cls_bancos bancos = new cls_bancos();
                        str_cuenta_contable_banco = bancos.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_bancaria.getValue().toString());
                    } else {
                        str_cuenta_contable_banco = "";
                    }
                    dia_pago_prestamo.cerrar();
                    tab_persona.limpiar();
                    tab_persona.insertar();
                    tab_persona.setCondicion("ide_geper=" + aut_beneficiario.getValor());
                    tab_persona.ejecutarSql();

                    tab_cab_factura.limpiar();
                    tab_cab_factura.insertar();

                    tab_cab_factura.setValor("ide_ccdaf", utilitario.getVariable("p_iyp_serie_factura"));
                    tab_cab_factura.setValor("ide_cndfp", utilitario.getVariable("p_con_for_pag_efec"));
                    tab_cab_factura.setValor("ide_geper", tab_persona.getValor("ide_geper"));
                    tab_cab_factura.setValor("ide_geubi", tab_persona.getValor("ide_geubi"));
                    tab_cab_factura.setValor("direccion_cccfa", tab_persona.getValor("direccion_geper"));
                    tab_cab_factura.setValor("observacion_cccfa", "Préstamo cuota " + numeroCuotasPagadas());
                    tab_cab_factura.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(sumaInteresSeleccionados()));
                    tab_cab_factura.setValor("total_cccfa", utilitario.getFormatoNumero(sumaInteresSeleccionados()));

                    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                    tab_cab_factura.setValor("secuencial_cccfa", cxc.buscarSecuencialFactura(tab_cab_factura.getValor("ide_ccdaf")));
                    tab_det_factura.limpiar();
                    tab_det_factura.insertar();
                    tab_det_factura.setValor("cantidad_ccdfa", "1");
                    tab_det_factura.setValor("precio_ccdfa", utilitario.getFormatoNumero(sumaInteresSeleccionados()));
                    tab_det_factura.setValor("total_ccdfa", utilitario.getFormatoNumero(sumaInteresSeleccionados()));
                    tab_det_factura.setValor("observacion_ccdfa", "Préstamo cuota " + numeroCuotasPagadas());
                    dia_factura.setDynamic(false);
                    dia_factura.dibujar();
                    utilitario.addUpdate("dia_factura");
                    if (com_cuenta_bancaria.getValue() != null) {
                        utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_deta_prestamo SET pagado_ipdpr=TRUE, doc_banco_ipdpr='" + tex_doc_banco.getValue() + "',ide_tecba=" + com_cuenta_bancaria.getValue().toString() + " WHERE ide_ipdpr=" + tab_detalle_prestamo.getValor("ide_ipdpr"));
                    }
                } else {
                    utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_deta_prestamo SET pagado_ipdpr=TRUE, doc_banco_ipdpr='" + tex_doc_banco.getValue() + "',ide_tecba=" + com_cuenta_bancaria.getValue().toString() + " WHERE ide_ipdpr=" + tab_detalle_prestamo.getValor("ide_ipdpr"));
                    dia_pago_prestamo.cerrar();
                    guardarPantalla();
                }
            } else if (tab_cabecera_prestamo.getValor("es_ingreso_ipcpr").equals("true")) {
                if (chk_hace_asiento.getValue().toString().equals("true")) {
                    generarAsientoContabilidadPagoPrestamo();
                }
            }
        }
    }

    private String numeroCuotasPagadas() {
        String cuotas = "";
        for (int i = 0; i < tab_detalle_prestamo.getTotalFilas(); i++) {
            if (tab_detalle_prestamo.getValor(i, "pagado_ipdpr").equalsIgnoreCase("true")) {
                if (!cuotas.isEmpty()) {
                    cuotas += ", ";
                }
                cuotas += "" + tab_detalle_prestamo.getValor(i, "num_ipdpr");
            }

        }
        return cuotas;
    }

    private String comillasPagadas() {
        String cuotas = "";
        for (int i = 0; i < tab_detalle_prestamo.getTotalFilas(); i++) {
            if (tab_detalle_prestamo.getValor(i, "pagado_ipdpr").equalsIgnoreCase("true")) {
                if (!cuotas.isEmpty()) {
                    cuotas += ",";
                }
                cuotas += "'" + tab_detalle_prestamo.getValor(i, "ide_ipdpr") + "'";
            }

        }
        return cuotas;
    }

    private double sumaInteresSeleccionados() {
        double dou_interes = 0;
        for (int i = 0; i < tab_detalle_prestamo.getTotalFilas(); i++) {
            if (tab_detalle_prestamo.getValor(i, "pagado_ipdpr").equalsIgnoreCase("true")) {
                dou_interes += Double.parseDouble(tab_detalle_prestamo.getValor(i, "interes_ipdpr"));
            }

        }
        return dou_interes;
    }

    private double sumaCapitalSeleccionados() {
        double dou_interes = 0;
        for (int i = 0; i < tab_detalle_prestamo.getTotalFilas(); i++) {
            if (tab_detalle_prestamo.getValor(i, "pagado_ipdpr").equalsIgnoreCase("true")) {
                dou_interes += Double.parseDouble(tab_detalle_prestamo.getValor(i, "capital_ipdpr"));
            }

        }
        return dou_interes;
    }

    private double sumaCuotaSeleccionados() {
        double dou_interes = 0;
        for (int i = 0; i < tab_detalle_prestamo.getTotalFilas(); i++) {
            if (tab_detalle_prestamo.getValor(i, "pagado_ipdpr").equalsIgnoreCase("true")) {
                dou_interes += Double.parseDouble(tab_detalle_prestamo.getValor(i, "cuota_ipdpr"));
            }
        }
        return dou_interes;
    }

    public boolean validarPagoPrestamo() {
        if (aut_beneficiario.getValor() == null || aut_beneficiario.getValor().isEmpty()) {
            utilitario.agregarMensajeError("No se pude realizar el pago", "Debe ingresar un beneficiario");
            return false;
        }
        if (boo_banco_caja) {
            if (com_cuenta_bancaria.getValue() == null) {
                utilitario.agregarMensajeError("No se pude realizar el pago", "Debe seleccionar una Cuenta Bancaria");
                return false;
            }
            if (tex_doc_banco.getValue() == null || tex_doc_banco.getValue().toString().isEmpty()) {
                utilitario.agregarMensajeError("No se pude realizar el pago", "Debe ingresar el documento bancario");
                return false;
            }
        }
        if (tab_detalle_prestamo.getValor(0, "pagado_ipdpr").toString().equals("false")) {
            utilitario.agregarMensajeError("No se pude realizar el pago", "Debe seleccionar el primer pago de la lista");
            return false;
        }
        return true;
    }

    public void cargarTipoPrestamo() {
        if (com_tipo_prestamo.getValue() != null) {
            tab_tabla1.setCondicion("es_ingreso_ipcpr=" + com_tipo_prestamo.getValue());
            tab_tabla1.getColumna("es_ingreso_ipcpr").setValorDefecto(com_tipo_prestamo.getValue() + "");
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            totales();
        } else {
            tab_tabla1.limpiar();
        }
    }

    public String cargarNombreGeper(String ide_geper) {
        String provee_actual = ide_geper;
        String nombre_proveedor = "";
        List nom_prove = utilitario.getConexion().consultar("select nom_geper from gen_persona where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_geper=" + provee_actual);
        if (nom_prove != null) {
            if (!nom_prove.isEmpty()) {
                if (nom_prove.get(0) != null) {
                    nombre_proveedor = nom_prove.get(0).toString();
                }
            }
        }
        return nombre_proveedor;
    }

//        public String cargarNumeroPago(String num_ipdpr) {
//        String provee_actual = num_ipdpr;
//        String num_cuota = "";
//        List nom_prove = utilitario.getConexion().consultar("select nom_geper from gen_persona where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_geper=" + provee_actual);
//        if (nom_prove != null) {
//            if (!nom_prove.isEmpty()) {
//                if (nom_prove.get(0) != null) {
//                    nombre_proveedor = nom_prove.get(0).toString();
//                }
//            }
//        }
//        return nombre_proveedor;
//    }
    public List resumirComprobante(List<cls_det_comp_cont> detalles) {
        //Unifica las cuentas
        List<cls_det_comp_cont> resumen = new ArrayList();

        List l_cuenta = new ArrayList();
        List l_cab_conta = new ArrayList();
        List l_observacion = new ArrayList();
        List l_lug_apli = new ArrayList();
        List l_suma = new ArrayList();
        int band = 0;
        String cuen = "";
        double suma = 0;
        for (int i = 0; i < detalles.size(); i++) {
            cuen = detalles.get(i).getIde_cndpc();
            for (int k = 0; k < l_cuenta.size(); k++) {
                if (cuen.equals(l_cuenta.get(k).toString())) {
                    band = 1;
                }
            }
            if (band == 0) {
                l_cuenta.add(cuen);
                l_cab_conta.add(detalles.get(i).getIde_cnccc());
                l_observacion.add(detalles.get(i).getObservacion_cndcc());
                l_lug_apli.add(detalles.get(i).getIde_cnlap());
            }
            band = 0;
        }
        for (int i = 0; i < l_cuenta.size(); i++) {
            cuen = l_cuenta.get(i).toString();
            for (int j = 0; j < detalles.size(); j++) {
                if (cuen.equals(detalles.get(j).getIde_cndpc().toString())) {
                    suma = detalles.get(j).getValor_cndcc() + suma;
                }
            }
            l_suma.add(suma);
            suma = 0;
        }

        for (int i = 0; i < l_cuenta.size(); i++) {
            resumen.add(new cls_det_comp_cont(l_lug_apli.get(i).toString(), l_cuenta.get(i).toString(), Double.parseDouble(l_suma.get(i).toString()), l_observacion.get(i).toString()));
            System.out.println("lugar aplica " + resumen.get(i).getIde_cnlap() + " cuenta " + resumen.get(i).getIde_cndpc() + " total " + resumen.get(i).getValor_cndcc());
        }
        return resumen;
    }

    public void generarAsientoContabilidadPagoPrestamo() {
        if (tab_cabecera_prestamo.getValor("es_ingreso_ipcpr").equalsIgnoreCase("true")) {
//PRESTAMO DE TIPO INGRESO
            boo_tipo_asiento_pago_prestamo_ingreso = true;
            conta.limpiar();
            lista_detalles.clear();
            cab_com_con = new cls_cab_comp_cont(utilitario.getVariable("p_con_tipo_comprobante_egreso"), utilitario.getVariable("p_con_estado_comprobante_normal"), "9", aut_beneficiario.getValor(), utilitario.getFechaActual(), "V/. Pago de cuota de prestamo ");

            cls_bancos bancos = new cls_bancos();
            String ide_cndpc_banco = bancos.getParametroCuentaBanco(com_cuenta_bancaria.getValue() + "", "ide_tecba", "ide_cndpc");

            if (ide_cndpc_banco != null) {
                double capital = sumaCapitalSeleccionados();
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cndpc_banco, Double.parseDouble(utilitario.getFormatoNumero(capital)), ""));
            }

            double interes = sumaInteresSeleccionados();
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), utilitario.getVariable("p_iyp_cuenta_interes_pagado"), Double.parseDouble(utilitario.getFormatoNumero(interes)), ""));

            //String ide_cuenta_banco_cuenta_x_pagar = conta.buscarCuentaPersona("CUENTA POR COBRAR", aut_beneficiario.getValor());
            String ide_cuenta_banco_cuenta_x_pagar = tab_detalle_prestamo.getValor("ide_cndpc");
            System.out.println("Haber........:  " + ide_cuenta_banco_cuenta_x_pagar);
            if (ide_cuenta_banco_cuenta_x_pagar != null) {
                if (!ide_cuenta_banco_cuenta_x_pagar.equals("null")) {
                    double valor = sumaCuotaSeleccionados();
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_banco_cuenta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(valor)), ""));
                } else {
                    cuentaCliente = cargarNombreGeper(aut_beneficiario.getValor());
                }
            }
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        } else {
            //PRESTAMO DE TIPO EGRESO
            boo_tipo_asiento_pago_prestamo_ingreso = false;
            conta.limpiar();
            lista_detalles.clear();
            cab_com_con = new cls_cab_comp_cont(utilitario.getVariable("p_con_tipo_comprobante_ingreso"), utilitario.getVariable("p_con_estado_comprobante_normal"), "9", aut_beneficiario.getValor(), utilitario.getFechaActual(), "V/. Pago de cuota de prestamo ");
            String ide_cuenta_banco_cuenta_x_pagar = tab_detalle_prestamo.getValor("ide_cndpc");
            //String ide_cuenta_banco_cuenta_x_pagar = conta.buscarCuentaPersona("CUENTA POR COBRAR", aut_beneficiario.getValor());
            System.out.println("Haber:  " + ide_cuenta_banco_cuenta_x_pagar);
            if (!str_cuenta_contable_banco.isEmpty()) {
                double valor = sumaCuotaSeleccionados();
                System.out.println("valor:   " + valor);
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), str_cuenta_contable_banco, Double.parseDouble(utilitario.getFormatoNumero(valor)), ""));
            } else {
                double valor = sumaCuotaSeleccionados();
                System.out.println("valor:   " + valor);
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(valor)), ""));

            }
            if (ide_cuenta_banco_cuenta_x_pagar != null) {
                if (!ide_cuenta_banco_cuenta_x_pagar.equals("null")) {
                    double capital = sumaCapitalSeleccionados();
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_banco_cuenta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(capital)), ""));
                    double interes = sumaInteresSeleccionados();
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), utilitario.getVariable("p_iyp_cuenta_interes"), Double.parseDouble(utilitario.getFormatoNumero(interes)), ""));
                } else {
                    cuentaCliente = cargarNombreGeper(aut_beneficiario.getValor());
                }
            }
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }

    public boolean validar() {
        if (tab_tabla1.getValor("ide_tecba") == null || tab_tabla1.getValor("ide_tecba").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe seleccionar la Entidad Bancaria");
            return false;
        }
        if (tab_tabla1.getValor("monto_ipcpr") == null || tab_tabla1.getValor("monto_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe ingresar el Monto del Prestamo");
            return false;
        }
        if (tab_tabla1.getValor("interes_ipcpr") == null || tab_tabla1.getValor("interes_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe ingresar el Interes");
            return false;
        }
        if (tab_tabla1.getValor("ide_cndpc") == null || tab_tabla1.getValor("ide_cndpc").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "debe Ingresar  la Cuenta Contable");
            return false;
        }
        if (tab_tabla1.getValor("num_pagos_ipcpr") == null || tab_tabla1.getValor("num_pagos_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "debe ingresar el numero de pagos");
            return false;
        }
        if (tab_tabla1.getValor("ide_iptpr") == null || tab_tabla1.getValor("ide_iptpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "debe seleccionar el Tipo de Prestamo");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipepr") == null || tab_tabla1.getValor("ide_ipepr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "debe seleccionar el estado del Prestamo");
            return false;
        }
        if (tab_tabla1.getValor("num_dias_ipcpr") == null || tab_tabla1.getValor("num_dias_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "debe ingresar el numero de Dias ");
            return false;
        }
        if (tab_tabla1.getValor("observacion_ipcpr") == null || tab_tabla1.getValor("observacion_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "debe ingresar la Observación");
            return false;
        }
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "debe ingresar el Beneficiario");
            return false;
        }
        if (tab_tabla2.getTotalFilas() == 0) {
            utilitario.agregarMensajeInfo("No se puede guardar", "debe generar la tabla de Amortización");
            return false;
        }
        return true;
    }

    public void calculaTablaAmortizacion() {
        System.out.println("Si ingresa al calcular tabla de amortizacion");
        tab_tabla2.limpiar();
        double monto = 0;
        double tasa_interes_anual = 0;
        double tasa_interes_mensual = 0;
        double num_pagos = 0;
        double num_dias = 0;
        String fecha = "";
        if (tab_tabla1.getValor("monto_ipcpr") != null && !tab_tabla1.getValor("monto_ipcpr").isEmpty()) {
            monto = Double.parseDouble(tab_tabla1.getValor("monto_ipcpr").toString());
        }
        if (tab_tabla1.getValor("interes_ipcpr") != null && !tab_tabla1.getValor("interes_ipcpr").isEmpty()) {
            tasa_interes_anual = Double.parseDouble(tab_tabla1.getValor("interes_ipcpr").toString());
        }
        if (tab_tabla1.getValor("num_pagos_ipcpr") != null && !tab_tabla1.getValor("num_pagos_ipcpr").isEmpty()) {
            num_pagos = Double.parseDouble(tab_tabla1.getValor("num_pagos_ipcpr").toString());
        }
        if (tab_tabla1.getValor("num_dias_ipcpr") != null && !tab_tabla1.getValor("num_dias_ipcpr").isEmpty()) {
            num_dias = Double.parseDouble(tab_tabla1.getValor("num_dias_ipcpr").toString());
        }
        if (tab_tabla1.getValor("fecha_prestamo_ipcpr") != null && !tab_tabla1.getValor("fecha_prestamo_ipcpr").isEmpty()) {
            fecha = tab_tabla1.getValor("fecha_prestamo_ipcpr").toString();
        }

        System.out.println("monto " + monto);
        System.out.println("tasa " + tasa_interes_anual);
        System.out.println("num pag " + num_pagos);
        System.out.println("num dias " + num_dias);
        List lis_capital = new ArrayList();
        List lis_interes = new ArrayList();
        List lis_cuota = new ArrayList();
        List lis_fecha = new ArrayList();
        if (monto != 0) {
            if (tasa_interes_anual != 0) {
                if (num_pagos != 0) {
                    if (num_dias != 0) {
                        if (!fecha.equals("")) {
                            tasa_interes_mensual = (tasa_interes_anual / 12);
                            System.out.println("tasa de nteres mensual " + tasa_interes_mensual);
                            double aux_interes_mesual = tasa_interes_mensual / 100;
                            double aux_interes_n = 0;
                            aux_interes_n = (Math.pow((1 + aux_interes_mesual), num_pagos));
                            double cuotafija = 0;
                            cuotafija = monto * ((aux_interes_mesual * aux_interes_n) / (aux_interes_n - 1));
                            System.out.println("cuota fija " + cuotafija);
                            double plazo = num_dias / 360;
                            System.out.println("plazo " + plazo);
                            double interes100 = tasa_interes_anual / 100;
                            System.out.println("interes100 " + interes100);
                            double capital = 0;
                            double subcapital = monto;
                            double sum_capital = 0;
                            System.out.println("subcapital " + subcapital);
                            double interes = 0;

                            for (int i = 0; i < num_pagos; i++) {
                                interes = interes100 * plazo * subcapital;
                                capital = cuotafija - interes;
                                sum_capital = capital + sum_capital;
                                subcapital = monto - sum_capital;
                                fecha = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha), Integer.parseInt(tab_tabla1.getValor("num_dias_ipcpr").toString())));
                                lis_capital.add(capital);
                                lis_cuota.add(cuotafija);
                                lis_interes.add(interes);
                                lis_fecha.add(fecha);

                                System.out.println("capital:  " + capital + "   interes " + interes + "     cuota " + cuotafija);
                            }
                            for (int i = 0; i < lis_capital.size(); i++) {
                                tab_tabla2.insertar();
                                System.out.println("total de filas" + tab_tabla2.getTotalFilas());
                                tab_tabla2.setValor("capital_ipdpr", utilitario.getFormatoNumero(lis_capital.get(lis_capital.size() - i - 1)));
                                tab_tabla2.setValor("cuota_ipdpr", utilitario.getFormatoNumero(lis_cuota.get(lis_cuota.size() - i - 1)));
                                tab_tabla2.setValor("interes_ipdpr", utilitario.getFormatoNumero(lis_interes.get(lis_interes.size() - i - 1)));
                                tab_tabla2.setValor("fecha_ipdpr", lis_fecha.get(lis_fecha.size() - i - 1) + "");
                                tab_tabla2.setValor("num_ipdpr", lis_capital.size() - i + "");
                            }

                            utilitario.addUpdate("tab_tabla2");
                            totales();
                        } else {
                            utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese la fecha del prestamo");
                        }
                    } else {
                        utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el numero de dias");
                    }
                } else {
                    utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el numero de pagos");
                }

            } else {
                utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el Interes ");
            }

        } else {
            utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el monto");
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
        System.out.println("si entra al aceptar reporte");
//Se ejecuta cuando se selecciona un reporte de la lista                
        if (rep_reporte.getReporteSelecionado().equals("Tabla de Amortizacion")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                System.out.println("si entra");
                rep_reporte.cerrar();
                parametro.put("ide_ipcpr", Long.parseLong(tab_tabla1.getValor("ide_ipcpr")));
                parametro.put("es_ingreso_ipcpr", Boolean.parseBoolean(tab_tabla1.getValor("es_ingreso_ipcpr")));
                System.out.println(tab_tabla1.getValor("es_ingreso_ipcpr"));
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla2.isFocus()) {
                    if (tab_tabla2.getValor("ide_cnccc") != null) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("ide_cnccc", Long.parseLong(tab_tabla2.getValor("ide_cnccc")));
                        parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                        parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                        sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_formato.dibujar();
                        utilitario.addUpdate("rep_reporte,sel_formato");

                    } else {
                        utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene compraqbante de contabilidad");
                    }

                } else {
                    if (tab_tabla1.getValor("ide_cnccc") != null) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                        parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                        parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                        sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_formato.dibujar();
                        utilitario.addUpdate("rep_reporte,sel_formato");
                    } else {
                        utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprobante de contabilidad");
                    }
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Facturas")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla2.isFocus()) {
                    if (tab_tabla2.getValor("ide_cccfa") != null) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("ide_cccfa", Long.parseLong(tab_tabla2.getValor("ide_cccfa")));
                        sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_formato.dibujar();
                        utilitario.addUpdate("rep_reporte,sel_formato");
                    }
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene factura");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Listado de Prestamos Realizados")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("sel_formato,sec_rango_reporte");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Listado de Prestamos por Entidad Bancaria")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();

                sel_formato.setSeleccionFormatoReporte(null, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        } ////*****
        else if (rep_reporte.getReporteSelecionado().equals("Listado de Prestamos Realizados (Saldos)")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
                utilitario.addUpdate("rep_reporte,sec_rango_reporte");
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                sec_rango_reporte.cerrar();
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("sel_formato,sec_rango_reporte");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Listado de Prestamos Realizados (Todos)")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_formato.setSeleccionFormatoReporte(null, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        }

////////****
    }
//    public void calcularCuotaFijaSele(DateSelectEvent evt) {
//        tab_tabla1.modificar(evt);
//        calculaTablaAmortizacion();
//    }

    public void calcularCuotaFija() {
        System.out.println("Si Ingresa al metodo Calcular Cuota Fija");
        calculaTablaAmortizacion();
    }

////    public void calcularSecuenciaPrestamo() {
////        int int_sec_pres = 0;
////        List num_pres = utilitario.getConexion().consultar("SELECT max(num_prestamo_ipcpr) FROM iyp_cab_prestamo WHERE es_ingreso_ipcpr=" + tab_tabla1.getValor(tab_tabla1.getFilaActual(), "es_ingreso_ipcpr"));
////        if (num_pres.get(0) != null) {
////            int_sec_pres = Integer.parseInt(num_pres.get(0).toString()) + 1;
////        } else {
////            int_sec_pres = 1;
////        }
////        tab_tabla1.setValor("num_prestamo_ipcpr", int_sec_pres + "");
////        utilitario.addUpdate("tab_tabla1");
////    }
    @Override
    public void insertar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.insertar();
        } else if (com_tipo_prestamo.getValue() != null) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.insertar();
                totales();
            }
        } else {
            utilitario.agregarMensajeError("Error al Insertar", "Debe Seleccionar un Tipo de Prestamo");
        }
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.eliminar();
        } else {
            utilitario.getTablaisFocus().eliminar();
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }

    public void inicio() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_ipcpr=" + tab_tabla1.getValor("ide_ipcpr"));
            tab_tabla2.ejecutarSql();
            totales();
        }
    }

    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_ipcpr=" + tab_tabla1.getValor("ide_ipcpr"));
            tab_tabla2.ejecutarSql();
            totales();
        }
    }

    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_ipcpr=" + tab_tabla1.getValor("ide_ipcpr"));
            tab_tabla2.ejecutarSql();
            totales();
        }
    }

    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla2.setCondicion("ide_ipcpr=" + tab_tabla1.getValor("ide_ipcpr"));
            tab_tabla2.ejecutarSql();
            totales();
        }
    }

    private void totales() {
        if (tab_tabla2.getTotalFilas() > 0) {
            eti_total_capital.setValue("Total Capital: " + utilitario.getFormatoNumero(tab_tabla2.getSumaColumna("capital_ipdpr"), 2));
            eti_total_interes.setValue(" Total Interes: " + utilitario.getFormatoNumero(tab_tabla2.getSumaColumna("interes_ipdpr"), 2));
            eti_total_cuota.setValue(" Total Cuota: " + utilitario.getFormatoNumero(tab_tabla2.getSumaColumna("cuota_ipdpr"), 2));
        } else {
            eti_total_capital.setValue("Total Capital: 0.0");
            eti_total_interes.setValue(" Total Interes: 0.0 ");
            eti_total_cuota.setValue(" Total Cuota: 0.0");
        }
        utilitario.addUpdate("eti_total_capital,eti_total_interes,eti_total_cuota");
    }

    public void generarAsientoPrestamo() {
        conta.limpiar();
        if (com_tipo_prestamo.getValue().toString().equalsIgnoreCase("true")) {
            System.out.println("INGRESO SI INGRESA AMORTIZACION DEVUELVE:........... " + com_tipo_prestamo.getValue().toString().equalsIgnoreCase("true"));
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_ingreso, p_est_com_normal, "9", tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_transaccion_ipcpr"), tab_tabla1.getValor("observacion_ipcpr"));
            lista_detalles.clear();
            List lis_cuenta = utilitario.getConexion().consultar("SELECT ide_cndpc from tes_cuenta_banco WHERE ide_tecba=" + tab_tabla1.getValor("ide_tecba"));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), lis_cuenta.get(0).toString(), Double.parseDouble(tab_tabla1.getValor("monto_ipcpr")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_tabla1.getValor("ide_cndpc"), Double.parseDouble(tab_tabla1.getValor("monto_ipcpr")), ""));
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            boo_tipo_asiento_pago_prestamo_ingreso = true;
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        } else {
            System.out.println("INGRESO SI INGRESA AMORTIZACION DEVUELVE:................ " + com_tipo_prestamo.getValue().toString().equalsIgnoreCase("true"));
            cab_com_con = new cls_cab_comp_cont(str_p_con_tipo_comprobante_egreso, p_est_com_normal, "9", tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_transaccion_ipcpr"), tab_tabla1.getValor("observacion_ipcpr"));
            lista_detalles.clear();
            List lis_cuenta = utilitario.getConexion().consultar("SELECT ide_cndpc from tes_cuenta_banco WHERE ide_tecba=" + tab_tabla1.getValor("ide_tecba"));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), tab_tabla1.getValor("ide_cndpc"), Double.parseDouble(tab_tabla1.getValor("monto_ipcpr")), ""));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), lis_cuenta.get(0).toString(), Double.parseDouble(tab_tabla1.getValor("monto_ipcpr")), ""));
            cab_com_con.setDetalles(lista_detalles);
            via_asiento.setVistaAsiento(cab_com_con);
            boo_tipo_asiento_pago_prestamo_ingreso = false;
            via_asiento.dibujar();
            utilitario.addUpdate("via_asiento");
        }
    }

    public void aceptarVistaAsiento() {
        System.out.println("Si entra an aceptar");
        if (via_asiento.validarComprobante()) {//siempre if cudra el comp cont
            System.out.println("Si entra al validar comprobante");
            cab_com_con.setObservacion_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            lista_detalles.clear();
            for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            String ide_cnccc = conta.generarAsientoContable(cab_com_con);
            System.out.println("valor del ide_cnccc: " + ide_cnccc);
            if (ide_cnccc != null) {
                if (dia_pago_prestamo.isVisible()) {
                    dia_pago_prestamo.cerrar();
                }
                via_asiento.cerrar();
                dia_bancos_caja_transcasas.cerrar();
                utilitario.addUpdate("via_asiento,dia_bancos_caja_transcasas");
                if (boo_tipo_asiento_pago_prestamo_ingreso == false) {
                    boo_tipo_asiento_pago_prestamo_ingreso = true;
                    tab_persona.guardar();
                    if (tab_cab_factura.getTotalFilas() > 0) {
                        tab_cab_factura.setValor("ide_cnccc", ide_cnccc);
                        tab_cab_factura.guardar();
                    }
                    utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_deta_prestamo SET pagado_ipdpr=TRUE, doc_banco_ipdpr='" + tex_doc_banco.getValue() + "',ide_tecba=" + com_cuenta_bancaria.getValue() + ",ide_cnccc=" + ide_cnccc + ",ide_cccfa=" + tab_cab_factura.getValor("ide_cccfa") + " WHERE ide_ipdpr in(" + comillasPagadas() + ")");
                    cls_bancos banco = new cls_bancos();
                    System.out.println("valor factura " + tab_cab_factura.getValor("total_cccfa"));
                    double valor = sumaCuotaSeleccionados();
                    Tabla tab_cab_lib_banco = banco.generarLibroBancoCxC(tab_cab_factura, com_cuenta_bancaria.getValue() + "", valor, "FACTURA :" + tab_cab_factura.getValor("secuencial_cccfa") + " " + tab_cab_factura.getValor("observacion_cccfa"), tex_doc_banco.getValue() + "");
                    System.out.println("valor del libro bancos...... " + tab_cab_lib_banco.getValor("valor_teclb"));
                    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                    cxc.generarTransaccionVenta(tab_cab_factura, 0, tab_cab_lib_banco);
                    tab_det_factura.guardar();
                    str_cuenta_contable_banco = "";
                } else {
                    boo_tipo_asiento_pago_prestamo_ingreso = false;
                    utilitario.getConexion().agregarSqlPantalla("UPDATE iyp_deta_prestamo SET pagado_ipdpr=TRUE, doc_banco_ipdpr='" + tex_doc_banco.getValue() + "',ide_cnccc=" + ide_cnccc + " WHERE ide_ipdpr in(" + comillasPagadas() + ")");
                    cls_bancos banco = new cls_bancos();
                    banco.generarLibroBanco(aut_beneficiario.getValor(), utilitario.getFechaActual(), utilitario.getVariable("p_tes_tran_cheque"), com_cuenta_bancaria.getValue() + "", ide_cnccc, Double.parseDouble(tab_detalle_prestamo.getValor("cuota_ipdpr")), via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"), tex_doc_banco.getValue() + "");
                }
                utilitario.addUpdate("via_asiento");
                utilitario.getConexion().guardarPantalla();
            }
        }
    }

    public void cancelarDialogo() {

        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        }
        //cancela todo lo que haya tenido hasta ese momento
        //utilitario.getConexion().rollback(); ********
        utilitario.getConexion().getSqlPantalla().clear();
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

    public SeleccionFormatoReporte getSel_formato() {
        return sel_formato;
    }

    public void setSel_formato(SeleccionFormatoReporte sel_formato) {
        this.sel_formato = sel_formato;
    }

    public Dialogo getDia_pago_prestamo() {
        return dia_pago_prestamo;
    }

    public void setDia_pago_prestamo(Dialogo dia_pago_prestamo) {
        this.dia_pago_prestamo = dia_pago_prestamo;
    }

    public AutoCompletar getAut_beneficiario() {
        return aut_beneficiario;
    }

    public void setAut_beneficiario(AutoCompletar aut_beneficiario) {
        this.aut_beneficiario = aut_beneficiario;
    }

    public Check getChk_hace_asiento() {
        return chk_hace_asiento;
    }

    public void setChk_hace_asiento(Check chk_hace_asiento) {
        this.chk_hace_asiento = chk_hace_asiento;
    }

    public Tabla gettab_detalle_prestamo() {
        return tab_detalle_prestamo;
    }

    public void settab_detalle_prestamo(Tabla tab_detalle_prestamo) {
        this.tab_detalle_prestamo = tab_detalle_prestamo;
    }

    public Tabla gettab_cabecera_prestamo() {
        return tab_cabecera_prestamo;
    }

    public void settab_cabecera_prestamo(Tabla tab_cabecera_prestamo) {
        this.tab_cabecera_prestamo = tab_cabecera_prestamo;
    }

    public VistaAsiento getVia_asiento() {
        return via_asiento;
    }

    public void setVia_asiento(VistaAsiento via_asiento) {
        this.via_asiento = via_asiento;
    }

    public Dialogo getDia_factura() {
        return dia_factura;
    }

    public void setDia_factura(Dialogo dia_factura) {
        this.dia_factura = dia_factura;
    }

    public Tabla getTab_persona() {
        return tab_persona;
    }

    public void setTab_persona(Tabla tab_persona) {
        this.tab_persona = tab_persona;
    }

    public Tabla getTab_cab_factura() {
        return tab_cab_factura;
    }

    public void setTab_cab_factura(Tabla tab_cab_factura) {
        this.tab_cab_factura = tab_cab_factura;
    }

    public Tabla getTab_det_factura() {
        return tab_det_factura;
    }

    public void setTab_det_factura(Tabla tab_det_factura) {
        this.tab_det_factura = tab_det_factura;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public Dialogo getDia_bancos_caja_transcasas() {
        return dia_bancos_caja_transcasas;
    }

    public void setDia_bancos_caja_transcasas(Dialogo dia_bancos_caja_transcasas) {
        this.dia_bancos_caja_transcasas = dia_bancos_caja_transcasas;
    }
}
