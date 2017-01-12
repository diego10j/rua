/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_cuentas_x_pagar;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Mascara;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import pkg_bancos.cls_bancos;
import pkg_contabilidad.*;
import pkg_inventario.cls_inventario;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_factura_cxp extends Pantalla {

    private Tabla tab_tabla1 = new Tabla(); // contiene la cabecera de la factura cxp
    private Tabla tab_tabla2 = new Tabla(); // contiene el detalle de factura cxp
    private Tabla tab_tabla3 = new Tabla(); // contiene los pagos realizados de la factura cxp
    private Tabla tab_tabla_df = new Tabla();
    private Dialogo dia_datos_factura = new Dialogo();
    private Dialogo dia_datos_liquidacion_compra = new Dialogo();
    private Division div_division = new Division();
    private Boton bot_comp_retencion = new Boton();
    // para retencion iva
    private double iva30;
    private double iva70;
    private double iva100;
    private double base_no_objeto = 0;
    private double base_tarifa0 = 0;
    private double base_grabada = 0;
    private double valor_iva = 0;
    private Etiqueta eti_saldo = new Etiqueta();
    // *********************  PARAMETROS *************
    List porcen_iva_sql = utilitario.getConexion().consultar("select porcentaje_cnpim from con_porcen_impues where ide_cnpim=" + utilitario.getVariable("p_con_porcentaje_imp_iva"));
    private double p_porcentaje_iva = Double.parseDouble(porcen_iva_sql.get(0).toString());
    private String p_bienes = utilitario.getVariable("p_inv_articulo_bien");
    private String p_servicios = utilitario.getVariable("p_inv_articulo_servicio");
    private String p_honorarios_profes = utilitario.getVariable("p_inv_articulo_honorarios");
    private String p_activos_fijos = utilitario.getVariable("p_inv_articulo_activo_fijo");
    private String p_iva30 = utilitario.getVariable("p_con_impuesto_iva30");
    private String p_iva70 = utilitario.getVariable("p_con_impuesto_iva70");
    private String p_iva100 = utilitario.getVariable("p_con_impuesto_iva100");
    private String p_estado_factura = utilitario.getVariable("p_cxp_estado_factura_normal");
    private String p_tipo_comprobante_diario = utilitario.getVariable("p_con_tipo_comprobante_diario");
    private String p_tipo_comprobante_egreso = utilitario.getVariable("p_con_tipo_comprobante_egreso");
    private String p_estado_comprobante_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private String p_modulo = "2";
    private String p_estado_normal_inventario = utilitario.getVariable("p_inv_estado_normal");
    private String p_tipo_transaccion_inv_compra = utilitario.getVariable("p_inv_tipo_transaccion_compra");
    // para rango de fechas CALENDARIO 
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private VisualizarPDF vp = new VisualizarPDF();
    private Confirmar con_guardar = new Confirmar();
    private Confirmar con_confirma_rete = new Confirmar();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionCalendario sel_cal = new SeleccionCalendario();
//  Datos del Proveedor
    private Boton bot_datos_proveedor = new Boton();
    private Dialogo dia_datos_provee = new Dialogo();
    private Tabla tab_tabla9 = new Tabla(); // contiene la tabla de proveedores
//
// Datos de Producto
    private Boton bot_datos_producto = new Boton();
    private Dialogo dia_datos_producto = new Dialogo();
    private Tabla tab_tabla10 = new Tabla(); // contiene la tabla de articulos
//  dialogo para pagos al contado
    private Dialogo dia_banco = new Dialogo();
    private Combo com_banco = new Combo();
    private Combo com_cuenta_banco = new Combo();
    private String banco_actual = "-1";
    private String cuenta_bancaria_asiento = "";
    private Texto tex_num_cheque = new Texto();
    private Texto tex_monto_cheque = new Texto();
    private Etiqueta eti_banc = new Etiqueta();
    private Etiqueta eti_cue_banc = new Etiqueta();
    private Etiqueta eti_num_cheque = new Etiqueta();
    private Etiqueta eti_monto_cheque = new Etiqueta();
    private int bandera_asiento_reversa = 0;
// para anticipo
    private Dialogo dia_ide_anticipo = new Dialogo();
    private Tabla tab_anticipos = new Tabla();
    private Texto tex_ide_anticipo = new Texto();
//
    private VistaAsiento via_comprobante_conta = new VistaAsiento();
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
// 
    private Boton bot_eliminar_factura = new Boton();
    private Mascara mas_num_auto_liquidacion = new Mascara();
    private AutoCompletar aut_num_factura = new AutoCompletar();
    private Calendario cal_fecha_cheque = new Calendario();
    private Etiqueta eti_fecha_cheque = new Etiqueta();
    private VistaRetencion vir_comprobante_retencion = new VistaRetencion();
    private Tabla tab_datos_comprobante_rembolso = new Tabla();
    private PanelAcordion pac_acordion = new PanelAcordion();
    private Etiqueta eti_total_datos_reembolso = new Etiqueta();

    public pre_factura_cxp() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {

            bar_botones.agregarCalendario();
            bar_botones.agregarReporte();

            // rango de fechas para filtro de facturas 
            sec_calendario.setId("sec_calendario");
            sec_calendario.setMultiple(true);
            sec_calendario.getBot_aceptar().setMetodo("filtrar");
            int mes = utilitario.getMes(utilitario.getFechaActual());
            int anio = utilitario.getAnio(utilitario.getFechaActual());
            String fecha_inicio = "" + anio + "-" + mes + "-1";

            sec_calendario.setFecha2(utilitario.getFecha(utilitario.getFechaActual()));
            sec_calendario.setFecha1(utilitario.getFecha(fecha_inicio));

            agregarComponente(sec_calendario);

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            rep_reporte.getBot_aceptar().setUpdate("rep_reporte,sel_cal");

            aut_num_factura.setId("aut_num_factura");
            aut_num_factura.setAutoCompletar("select cf.ide_cpcfa,cf.numero_cpcfa,gp.nom_geper from cxp_cabece_factur cf left join gen_persona gp on cf.ide_geper=gp.ide_geper where cf.ide_empr=" + utilitario.getVariable("ide_empr") + " and cf.ide_sucu=" + utilitario.getVariable("ide_sucu")
                    + " and cf.fecha_emisi_cpcfa between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "'");
            aut_num_factura.setMetodoChange("buscarFactura");

            bar_botones.agregarComponente(new Etiqueta("Num Factura:"));
            bar_botones.agregarComponente(aut_num_factura);

            sel_rep.setId("sel_rep");

            sel_cal.setId("sel_cal");
            sel_cal.setMultiple(true);
            sel_cal.getBot_aceptar().setMetodo("aceptarReporte");
            sel_cal.getBot_aceptar().setUpdate("sel_cal,sel_rep");

            bar_botones.getBot_inicio().setMetodo("inicio");
            bar_botones.getBot_fin().setMetodo("fin");
            bar_botones.getBot_siguiente().setMetodo("siguiente");
            bar_botones.getBot_atras().setMetodo("atras");

            bot_eliminar_factura.setId("bot_eliminar_factura");
            bot_eliminar_factura.setValue("Eliminar");
            bot_eliminar_factura.setMetodo("eliminarFactura");

            bot_comp_retencion.setId("bot_comp_retencion");
            bot_comp_retencion.setValue("Generar Comp. Retencion");
            bot_comp_retencion.setMetodo("generarComprobanteRetencion");
            bot_comp_retencion.setDisabled(false);
            bar_botones.agregarBoton(bot_comp_retencion);

            bot_datos_proveedor.setId("bot_datos_proveedor");
            bot_datos_proveedor.setValue("Datos Proveedor");
            bot_datos_proveedor.setMetodo("abrirDatosProveedor");
            bar_botones.agregarBoton(bot_datos_proveedor);

            bot_datos_producto.setId("bot_datos_producto");
            bot_datos_producto.setValue("Datos Producto");
            bot_datos_producto.setMetodo("abrirDatosProducto");
            bar_botones.agregarBoton(bot_datos_producto);

            pac_acordion.setId("pac_acordion");

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setIdCompleto("pac_acordion:tab_tabla1");
            tab_tabla1.setTabla("cxp_cabece_factur", "ide_cpcfa", 1);
            tab_tabla1.onSelect("seleccionar_tabla1");
            tab_tabla1.getColumna("ide_cntdo").setCombo("con_tipo_document", "ide_cntdo", "nombre_cntdo", "ide_cntdo in (" + utilitario.getVariable("p_con_tipo_documento_reembolso") + "," + utilitario.getVariable("p_con_tipo_documento_factura") + "," + utilitario.getVariable("p_con_tipo_documento_liquidacion_compra") + "," + utilitario.getVariable("p_con_tipo_documento_nota_venta") + ")");
            tab_tabla1.getColumna("ide_cntdo").setMetodoChange("cambioTipoDocumento");
            tab_tabla1.getColumna("ide_cpefa").setCombo("cxp_estado_factur", "ide_cpefa", "nombre_cpefa", "");
            tab_tabla1.getColumna("ide_cpefa").setValorDefecto(p_estado_factura);
            tab_tabla1.getColumna("ide_cpefa").setLectura(true);
            tab_tabla1.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp not in ( " + utilitario.getVariable("p_con_for_pag_reembolso_caja") + "," + utilitario.getVariable("p_con_for_pag_anticipo") + " )");
            tab_tabla1.getColumna("ide_cndfp").setPermitirNullCombo(false);
            tab_tabla1.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setVisible(false);
            tab_tabla1.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_trans_cpcfa").setLectura(true);
            tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper is true");
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_geper").setRequerida(true);
            tab_tabla1.getColumna("autorizacio_cpcfa").setRequerida(true);
            tab_tabla1.getColumna("observacion_cpcfa").setRequerida(true);
            tab_tabla1.getColumna("pagado_cpcfa").setLectura(true);
            tab_tabla1.getColumna("pagado_cpcfa").setValorDefecto("False");
            tab_tabla1.getColumna("total_cpcfa").setEtiqueta();
            tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("total_cpcfa").setValorDefecto("0");
            tab_tabla1.getColumna("numero_cpcfa").setEstilo("font-size: 15px;font-weight: bold");
            tab_tabla1.getColumna("numero_cpcfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
            tab_tabla1.getColumna("numero_cpcfa").setMetodoChange("aceptarDatosFactura");
            tab_tabla1.getColumna("numero_cpcfa").setMascara("999-999-99999999");
            tab_tabla1.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
            tab_tabla1.getColumna("numero_cpcfa").setRequerida(true);
            tab_tabla1.getColumna("autorizacio_cpcfa").setLectura(true);
            tab_tabla1.getColumna("base_grabada_cpcfa").setEtiqueta();
            tab_tabla1.getColumna("base_grabada_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("base_grabada_cpcfa").setValorDefecto("0");
            tab_tabla1.getColumna("valor_iva_cpcfa").setEtiqueta();
            tab_tabla1.getColumna("valor_iva_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("valor_iva_cpcfa").setValorDefecto("0");
            tab_tabla1.getColumna("base_no_objeto_iva_cpcfa").setEtiqueta();
            tab_tabla1.getColumna("base_no_objeto_iva_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
            tab_tabla1.getColumna("base_tarifa0_cpcfa").setEtiqueta();
            tab_tabla1.getColumna("base_tarifa0_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
            tab_tabla1.getColumna("otros_cpcfa").setValorDefecto("0");
            tab_tabla1.getColumna("otros_cpcfa").setMetodoChange("calcula_total_detalles_cxp");
            tab_tabla1.getColumna("porcen_desc_cpcfa").setMetodoChange("calcula_iva1");
            tab_tabla1.getColumna("descuento_cpcfa").setMetodoChange("calcula_total_detalles_cxp");
            tab_tabla1.getColumna("ide_srtst").setCombo("sri_tipo_sustento_tributario", "ide_srtst", "alterno_srtst,nombre_srtst", "");
            tab_tabla1.getColumna("ide_cncre").setLectura(true);
            tab_tabla1.getColumna("ide_cnccc").setLectura(true);
            tab_tabla1.getColumna("ide_srtst").setValorDefecto(utilitario.getVariable("p_sri_tip_sus_tri02"));
            tab_tabla1.getColumna("valor_ice_cpcfa").setValorDefecto("0");
            tab_tabla1.setTipoFormulario(true);
            tab_tabla1.getGrid().setColumns(6);
            tab_tabla1.setCampoOrden("ide_cpcfa desc");
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.agregarRelacion(tab_tabla3);
            tab_tabla1.agregarRelacion(tab_datos_comprobante_rembolso);
            //tab_tabla1.setRecuperarLectura(true);
            utilitario.buscarNombresVisuales(tab_tabla1);
            tab_tabla1.setCondicionSucursal(true);
            tab_tabla1.setCondicion("fecha_emisi_cpcfa between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "'");
            tab_tabla1.dibujar();

            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("cxp_detall_factur", "ide_cpdfa", 2);
            tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
            tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
            tab_tabla2.getColumna("ide_inarti").setMetodoChange("obtener_total_detalle_factura_cxp");
            tab_tabla2.getColumna("cantidad_cpdfa").setMetodoChange("obtener_total_detalle_factura_cxp");
            tab_tabla2.getColumna("precio_cpdfa").setMetodoChange("obtener_total_detalle_factura_cxp");
            tab_tabla2.getColumna("valor_cpdfa").setEtiqueta();
            tab_tabla2.getColumna("valor_cpdfa").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla2.getColumna("devolucion_cpdfa").setValorDefecto("0");
            tab_tabla2.getColumna("alter_tribu_cpdfa").setRequerida(true);

            List lista = new ArrayList();
            Object fila1[] = {
                "1", "SI"
            };
            Object fila2[] = {
                "-1", "NO"
            };
            Object fila3[] = {
                "0", "NO  OBJETO"
            };
            lista.add(fila1);
            lista.add(fila2);
            lista.add(fila3);
            tab_tabla2.getColumna("iva_inarti_cpdfa").setRadio(lista, "");
            tab_tabla2.getColumna("iva_inarti_cpdfa").setMetodoChange("calcula_iva2");
            //tab_tabla2.setRecuperarLectura(true);
            utilitario.buscarNombresVisuales(tab_tabla2);
            tab_tabla2.dibujar();

            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            tab_tabla3.setId("tab_tabla3");
            tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
            tab_tabla3.setCampoPrimaria("ide_cpdtr");
            tab_tabla3.setCampoForanea("ide_cpcfa");
            tab_tabla3.setNumeroTabla(3);
            tab_tabla3.setLectura(true);
            utilitario.buscarNombresVisuales(tab_tabla3);
            tab_tabla3.dibujar();

            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_tabla3);

            tab_datos_comprobante_rembolso.setId("tab_datos_comprobante_rembolso");
            tab_datos_comprobante_rembolso.setIdCompleto("pac_acordion:tab_datos_comprobante_rembolso");
            tab_datos_comprobante_rembolso.setTabla("cxp_datos_com_reembolso", "ide_cpdcr", 4);
            tab_datos_comprobante_rembolso.getColumna("base_tarifa0_cpdcr").setMetodoChange("cambiaValorDatosReembolsos");
            tab_datos_comprobante_rembolso.getColumna("base_no_objeto_cpdcr").setMetodoChange("cambiaValorDatosReembolsos");
            tab_datos_comprobante_rembolso.getColumna("base_imponible_cpdcr").setMetodoChange("cambiaValorDatosReembolsos");
            tab_datos_comprobante_rembolso.getColumna("fecha_cpdcr").setValorDefecto(utilitario.getFechaActual());
            tab_datos_comprobante_rembolso.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
            tab_datos_comprobante_rembolso.getColumna("valor_iva_cpdcr").setLectura(true);
            tab_datos_comprobante_rembolso.dibujar();

            PanelTabla pat_panel4 = new PanelTabla();
            pat_panel4.setPanelTabla(tab_datos_comprobante_rembolso);

            eti_total_datos_reembolso.setId("eti_total_datos_reembolso");
            eti_total_datos_reembolso.setValue("Total: ");
            eti_total_datos_reembolso.setStyle("font-size:16px;font-weight: bold;");

            pat_panel4.getFacets().put("footer", eti_total_datos_reembolso);
            pac_acordion.agregarPanel("COMPRAS ", pat_panel1);
            pac_acordion.agregarPanel("DATOS TIPO COMPROBANTE REEMBOLSO", pat_panel4);

            eti_saldo.setId("eti_saldo");
            eti_saldo.setValue("Saldo: ");
            eti_saldo.setStyle("font-size:16px;font-weight: bold;");

            Division div_fot = new Division();
            div_fot.setFooter(pat_panel3, eti_saldo, "80%");

            Division div1 = new Division();
            div1.setId("div1");
            div1.dividir2(pat_panel2, div_fot, "65%", "V");

            div_division.setId("div_division");
            div_division.dividir2(pac_acordion, div1, "65%", "H");

            agregarComponente(div_division);

// ************* PARA EL VISUALIZADOR DE COMPROBANTE DE CONTABILIDAD
            via_comprobante_conta.setId("via_comprobante_conta");
            via_comprobante_conta.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
            via_comprobante_conta.getBot_cancelar().setMetodo("cancelarDialogo");
            via_comprobante_conta.setDynamic(false);

            agregarComponente(via_comprobante_conta);

//***************  dialogo datos proveedor *****************************
            tab_tabla9.setId("tab_tabla9");
            tab_tabla9.setTabla("gen_persona", "ide_geper", 9);
            tab_tabla9.getColumna("es_proveedo_geper").setValorDefecto("true");
            tab_tabla9.getColumna("es_proveedo_geper").setVisible(false);
            tab_tabla9.getColumna("nivel_geper").setCombo(utilitario.getListaNiveles());
            tab_tabla9.getColumna("ide_vgven").setVisible(false);
            tab_tabla9.getColumna("ide_rhtro").setVisible(false);
            tab_tabla9.getColumna("ide_rhcon").setVisible(false);
            tab_tabla9.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "");
            tab_tabla9.getColumna("ide_getid").setLectura(true);
            tab_tabla9.getColumna("ide_rheem").setVisible(false);
            tab_tabla9.getColumna("ide_rhtem").setVisible(false);
            tab_tabla9.getColumna("ide_rhmse").setVisible(false);
            tab_tabla9.getColumna("ide_vgecl").setVisible(false);
            tab_tabla9.getColumna("ide_rhseg").setVisible(false);
            tab_tabla9.getColumna("ide_rhcsa").setVisible(false);
            tab_tabla9.getColumna("ide_cntco").setCombo("con_tipo_contribu", "ide_cntco", "nombre_cntco", "");
            tab_tabla9.getColumna("ide_rhfpa").setVisible(false);
            tab_tabla9.getColumna("ide_rheor").setVisible(false);
            tab_tabla9.getColumna("ide_rhrtr").setVisible(false);
            tab_tabla9.getColumna("ide_rhtsa").setVisible(false);
            tab_tabla9.getColumna("ide_rhtco").setVisible(false);
            tab_tabla9.getColumna("ide_geeci").setVisible(false);
            tab_tabla9.getColumna("sueldo_geper").setVisible(false);
            tab_tabla9.getColumna("fecha_ingre_geper").setValorDefecto(utilitario.getFechaActual());
            tab_tabla9.getColumna("fecha_ingre_geper").setLectura(true);
            tab_tabla9.getColumna("foto_geper").setVisible(false);
            tab_tabla9.getColumna("fecha_nacim_geper").setVisible(false);
            tab_tabla9.getColumna("fecha_salid_geper").setVisible(false);
            tab_tabla9.getColumna("es_cliente_geper").setVisible(false);
            tab_tabla9.getColumna("es_empleado_geper").setVisible(false);
            tab_tabla9.getColumna("cuent_banco_geper").setVisible(false);
            tab_tabla9.getColumna("ide_coepr").setVisible(false);
            tab_tabla9.getColumna("gen_ide_geper").setVisible(false);
            tab_tabla9.getColumna("contacto_geper").setVisible(false);
            tab_tabla9.getColumna("movil_geper").setVisible(false);
            tab_tabla9.getColumna("pagina_web_geper").setVisible(false);
            tab_tabla9.getColumna("fecha_ingre_geper").setVisible(false);
            tab_tabla9.getColumna("nivel_geper").setVisible(false);
            tab_tabla9.getColumna("tipo_sangre_geper").setVisible(false);
            tab_tabla9.getColumna("jornada_fin_geper").setVisible(false);
            tab_tabla9.getColumna("ide_georg").setVisible(false);
            tab_tabla9.getColumna("ide_cotpr").setVisible(false);
            tab_tabla9.getColumna("nombre_compl_geper").setVisible(false);
            tab_tabla9.getColumna("fax_geper").setVisible(false);
            tab_tabla9.getColumna("correo_geper").setVisible(false);
            tab_tabla9.getColumna("repre_legal_geper").setVisible(false);
            tab_tabla9.getColumna("observacion_geper").setVisible(false);
            tab_tabla9.getColumna("jornada_inicio_geper").setVisible(false);
            tab_tabla9.getColumna("ide_tetcb").setVisible(false);
            tab_tabla9.getColumna("ide_vgtcl").setVisible(false);
            tab_tabla9.getColumna("factu_hasta_geper").setVisible(false);
            tab_tabla9.getColumna("ide_gegen").setCombo("gen_genero", "ide_gegen", "nombre_gegen", "");
            tab_tabla9.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
            tab_tabla9.setTipoFormulario(true);
            tab_tabla9.getGrid().setColumns(2);
            tab_tabla9.setMostrarNumeroRegistros(false);
            utilitario.buscarNombresVisuales(tab_tabla9);
            tab_tabla9.setCondicion("ide_geper=-1");
            tab_tabla9.dibujar();

            Grid grid_datos_provee = new Grid();
            grid_datos_provee.getChildren().add(tab_tabla9);

            dia_datos_provee.setId("dia_datos_provee");
            dia_datos_provee.setTitle("Datos Proveedor");

            dia_datos_provee.setWidth("70%");
            dia_datos_provee.setHeight("70%");
            dia_datos_provee.setDialogo(grid_datos_provee);
            grid_datos_provee.setStyle("width:" + (dia_datos_provee.getAnchoPanel() - 5) + "px;height:" + dia_datos_provee.getAltoPanel() + "px;overflow: auto;display: block;");
            dia_datos_provee.getBot_aceptar().setMetodo("aceptar_datos_proveedor");
            dia_datos_provee.getBot_cancelar().setMetodo("cancelarDialogo");

            agregarComponente(dia_datos_provee);

//***************  dialogo datos producto*****************************
            tab_tabla10.setId("tab_tabla10");
            tab_tabla10.setTabla("inv_articulo", "ide_inarti", 10);
            tab_tabla10.getColumna("ide_infab").setVisible(false);
            tab_tabla10.getColumna("ide_inmar").setVisible(false);
            tab_tabla10.getColumna("ide_inuni").setVisible(false);
            tab_tabla10.getColumna("ide_intpr").setVisible(false);
            tab_tabla10.getColumna("ide_inepr").setVisible(false);
            tab_tabla10.getColumna("nivel_inarti").setVisible(false);
            List lista1 = new ArrayList();
            Object fila10[] = {
                "1", "SI"
            };
            Object fila20[] = {
                "-1", "NO"
            };
            Object fila30[] = {
                "0", "NO  OBJETO"
            };
            lista1.add(fila10);
            lista1.add(fila20);
            lista1.add(fila30);
            tab_tabla10.getColumna("iva_inarti").setRadio(lista1, "1");
            tab_tabla10.getColumna("iva_inarti").setRadioVertical(true);
            tab_tabla10.getColumna("inv_ide_inarti").setVisible(false);
            tab_tabla10.setTipoFormulario(true);
            tab_tabla10.getGrid().setColumns(2);
            tab_tabla10.setMostrarNumeroRegistros(false);
            utilitario.buscarNombresVisuales(tab_tabla10);
            tab_tabla10.setCondicion("ide_inarti=-1");
            tab_tabla10.dibujar();

            Grid grid_datos_produc = new Grid();
            grid_datos_produc.getChildren().add(tab_tabla10);

            dia_datos_producto.setId("dia_datos_producto");
            dia_datos_producto.setTitle("Datos Producto");

            dia_datos_producto.setWidth("50%");
            dia_datos_producto.setHeight("50%");
            dia_datos_producto.setDialogo(grid_datos_produc);
            grid_datos_produc.setStyle("width:" + (dia_datos_producto.getAnchoPanel() - 5) + "px;height:" + dia_datos_producto.getAltoPanel() + "px;overflow: auto;display: block;");
            dia_datos_producto.getBot_aceptar().setMetodo("aceptar_datos_producto");
            dia_datos_producto.getBot_cancelar().setMetodo("cancelarDialogo");

            agregarComponente(dia_datos_producto);

//**************  dialogo pagos con cheque **********
            tex_num_cheque.setId("tex_num_cheque");
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco.setMetodo("cargar_cuentas");
            com_cuenta_banco.setId("com_cuenta_banco");
            com_cuenta_banco.setCombo(new ArrayList());
            com_cuenta_banco.setMetodo("cargar_num_cheque");

            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Cheque: ");
            eti_monto_cheque.setValue("Monto Cheque: ");
            eti_fecha_cheque.setValue("Fecha: ");
            Grid grid_bancos = new Grid();
            grid_bancos.setColumns(2);
            grid_bancos.getChildren().add(eti_banc);
            grid_bancos.getChildren().add(com_banco);
            grid_bancos.getChildren().add(eti_cue_banc);
            grid_bancos.getChildren().add(com_cuenta_banco);
            grid_bancos.getChildren().add(eti_num_cheque);
            grid_bancos.getChildren().add(tex_num_cheque);
            grid_bancos.getChildren().add(eti_monto_cheque);
            grid_bancos.getChildren().add(tex_monto_cheque);
            grid_bancos.getChildren().add(eti_fecha_cheque);
            grid_bancos.getChildren().add(cal_fecha_cheque);

            dia_banco.setId("dia_banco");
            dia_banco.setTitle("Forma de Pago");
            dia_banco.setWidth("30%");
            dia_banco.setHeight("30%");
            dia_banco.setDialogo(grid_bancos);
            dia_banco.setDynamic(false);
            grid_bancos.setStyle("width:" + (dia_banco.getAnchoPanel() - 5) + "px;height:" + dia_banco.getAltoPanel() + "px;overflow: auto;display: block;");
            dia_banco.getBot_aceptar().setMetodo("generarComprobanteContabilidadContado");
            dia_banco.getBot_cancelar().setMetodo("cancelarDialogo");

            agregarComponente(dia_banco);

//**************  dialogo ide de anticipos **********
            Grid grid_ide_cnccc = new Grid();
            grid_ide_cnccc.setColumns(1);

            tab_anticipos.setId("tab_anticipos");
            tab_anticipos.setSql("SELECT ide_cpctr,(select "
                    + "case when sum (dt.valor_cpdtr) is null then 0 else sum (dt.valor_cpdtr) end "
                    + "from cxp_cabece_transa ct "
                    + "left join cxp_detall_transa dt on dt.ide_cpctr=ct.ide_cpctr "
                    + "where ct.ide_geper=-1 "
                    + "and dt.ide_cpttr=7 )- "
                    + "(select "
                    + "case when sum (dt.valor_cpdtr) is null then 0 else sum (dt.valor_cpdtr) end "
                    + "from cxp_cabece_transa ct "
                    + "left join cxp_detall_transa dt on dt.ide_cpctr=ct.ide_cpctr "
                    + "where ct.ide_geper=-1 "
                    + "and dt.ide_cpttr=17 ) AS total_anticipo "
                    + "from cxp_cabece_transa "
                    + "where ide_geper=-1 "
                    + "and ide_cpttr=7 "
                    + "group by ide_cpctr "
                    + "having (select "
                    + "case when sum (dt.valor_cpdtr) is null then 0 else sum (dt.valor_cpdtr) end "
                    + "from cxp_cabece_transa ct "
                    + "left join cxp_detall_transa dt on dt.ide_cpctr=ct.ide_cpctr "
                    + "where ct.ide_geper=-1 "
                    + "and dt.ide_cpttr=7 )- "
                    + "(select "
                    + "case when sum (dt.valor_cpdtr) is null then 0 else sum (dt.valor_cpdtr) end "
                    + "from cxp_cabece_transa ct "
                    + "left join cxp_detall_transa dt on dt.ide_cpctr=ct.ide_cpctr "
                    + "where ct.ide_geper=-1 "
                    + "and dt.ide_cpttr=17 ) > 0 "
                    + "limit 1");
            tab_anticipos.setCampoPrimaria("ide_cpctr");
            tab_anticipos.getColumna("total_anticipo").setEstilo("color:red;font-size:13px;font-weight: bold");
            //tab_anticipos.onSelectCheck("seleccionaFacturaCxP");
            //tab_anticipos.onUnselectCheck("deseleccionaFacturaCxP");
            tab_anticipos.setTipoSeleccion(true);
            tab_anticipos.setLectura(true);
            tab_anticipos.dibujar();

            grid_ide_cnccc.getChildren().add(tab_anticipos);
            dia_ide_anticipo.setId("dia_ide_anticipo");
            dia_ide_anticipo.setTitle("ANTICIPOS");
            dia_ide_anticipo.setWidth("20%");
            dia_ide_anticipo.setHeight("30%");
            dia_ide_anticipo.setDialogo(grid_ide_cnccc);
            dia_ide_anticipo.setDynamic(false);
            grid_ide_cnccc.setStyle("width:" + (dia_ide_anticipo.getAnchoPanel() - 5) + "px;height:" + dia_ide_anticipo.getAltoPanel() + "px;overflow: auto;display: block;");
            dia_ide_anticipo.getBot_aceptar().setMetodo("aceptarDialogoAnticipo");
            dia_ide_anticipo.getBot_cancelar().setMetodo("cancelarDialogo");

            agregarComponente(dia_ide_anticipo);

            // para dialogo datos factura            
            tab_tabla_df.setId("tab_tabla_df");
            tab_tabla_df.setTabla("cxp_datos_factura", "ide_cpdaf", 11);
            tab_tabla_df.setTipoFormulario(true);
            tab_tabla_df.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
            tab_tabla_df.getColumna("ide_geper").setAutoCompletar();
            tab_tabla_df.getColumna("ide_geper").setLectura(true);
            tab_tabla_df.getColumna("autorizacion_cpdaf").setMascara("9999999999");
            tab_tabla_df.getGrid().setColumns(4);
            tab_tabla_df.setMostrarNumeroRegistros(false);
            utilitario.buscarNombresVisuales(tab_tabla_df);
            tab_tabla_df.setCondicion("ide_cpdaf=-1");
            tab_tabla_df.dibujar();

            dia_datos_factura.setId("dia_datos_factura");
            dia_datos_factura.setTitle("Datos Factura");
            dia_datos_factura.setWidth("55%");
            dia_datos_factura.setHeight("40%");
            dia_datos_factura.getBot_aceptar().setMetodo("aceptarDialogoDatosFactura");
            dia_datos_factura.getBot_cancelar().setMetodo("cancelarDialogo");

            Grid gri_datos_factura = new Grid();
            gri_datos_factura.setColumns(2);
            gri_datos_factura.setStyle("width:" + (dia_datos_factura.getAnchoPanel() - 5) + "px;height:" + dia_datos_factura.getAltoPanel() + "px;overflow: auto;display: block;");
            gri_datos_factura.getChildren().add(tab_tabla_df);

            dia_datos_factura.setDialogo(gri_datos_factura);
            agregarComponente(dia_datos_factura);

            // dialogo para datos de liquidacion en compras
            dia_datos_liquidacion_compra.setId("dia_datos_liquidacion_compra");
            dia_datos_liquidacion_compra.setTitle("Datos Liquidacion Factura");
            dia_datos_liquidacion_compra.setWidth("20%");
            dia_datos_liquidacion_compra.setHeight("20%");
            dia_datos_liquidacion_compra.getBot_aceptar().setMetodo("aceptarDialogoDatosLiquidacion");
            dia_datos_liquidacion_compra.getBot_cancelar().setMetodo("cancelarDialogo");

            Grid gri_datos_liquidacion = new Grid();
            gri_datos_liquidacion.setColumns(2);
            gri_datos_liquidacion.setStyle("width:" + (dia_datos_liquidacion_compra.getAnchoPanel() - 5) + "px;height:" + dia_datos_liquidacion_compra.getAltoPanel() + "px;overflow: auto;display: block;");
            gri_datos_liquidacion.getChildren().add(new Etiqueta("Autorizacion: "));
            mas_num_auto_liquidacion.setMask("9999999999");
            gri_datos_liquidacion.getChildren().add(mas_num_auto_liquidacion);

            dia_datos_liquidacion_compra.setDialogo(gri_datos_liquidacion);
            agregarComponente(dia_datos_liquidacion_compra);

            // para el visualizador de pdf
            vp.setId("vp");
            agregarComponente(vp);

            // confirmacion de comprobante de retencion
            con_confirma_rete.setId("con_confirma_rete");
            con_confirma_rete.setWidgetVar("con_confirma_rete");
            con_confirma_rete.setMessage("Desea Generar el Comprobante de Retención");
            con_confirma_rete.getBot_aceptar().setValue("Si");
            con_confirma_rete.getBot_cancelar().setValue("No");
            con_confirma_rete.getBot_aceptar().setMetodo("generarComprobanteRetencion");
            con_confirma_rete.getBot_cancelar().setMetodo("noGeneraRetencion");

            utilitario.getPantalla().getChildren().add(con_confirma_rete);

            con_guardar.setId("con_guardar");
            con_guardar.setMessage("La factura ya tiene retencion, si genera una nueva la retencion actual sera anulada");
            con_guardar.getBot_aceptar().setMetodo("aceptar_nuevo_comprobante_retencion");
            con_guardar.getBot_aceptar().setUpdate("con_guardar");

            agregarComponente(con_guardar);
            agregarComponente(rep_reporte);
            agregarComponente(sel_rep);
            agregarComponente(sel_cal);

            //  PARA EL VISUALIZADOR DE COMPROBANTE DE RETENCION
            vir_comprobante_retencion.setId("vir_comprobante_retencion");
            vir_comprobante_retencion.getBot_aceptar().setMetodo("aceptarComprobanteRetencion");
            vir_comprobante_retencion.setDynamic(false);
            agregarComponente(vir_comprobante_retencion);

            calcula_total_detalles_cxp();

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }

    }

    public double calcularTotalDatosReembolsos() {
        double dou_tot_datos_reembolsos = 0;
        try {
            double tot_base_tarifa0 = tab_datos_comprobante_rembolso.getSumaColumna("base_tarifa0_cpdcr");
            double tot_base_no_objeto = tab_datos_comprobante_rembolso.getSumaColumna("base_no_objeto_cpdcr");
            double tot_base_imponible = tab_datos_comprobante_rembolso.getSumaColumna("base_imponible_cpdcr");
            dou_tot_datos_reembolsos = tot_base_tarifa0 + tot_base_no_objeto + tot_base_imponible;
            eti_total_datos_reembolso.setValue("TOTAL " + dou_tot_datos_reembolsos);
            utilitario.addUpdate("tab_datos_comprobante_rembolso,pac_acordion");
        } catch (Exception e) {
        }
        return dou_tot_datos_reembolsos;
    }

    public double calcularValorDatosReembolso() {
        double dou_val_datos_reembolsos = 0;
        double tot_base_tarifa0 = 0;
        double tot_base_no_objeto = 0;
        double tot_base_imponible = 0;
        try {
            tot_base_tarifa0 = Double.parseDouble(tab_datos_comprobante_rembolso.getValor(tab_datos_comprobante_rembolso.getFilaActual(), "base_tarifa0_cpdcr"));
        } catch (Exception e) {
        }
        try {
            tot_base_no_objeto = Double.parseDouble(tab_datos_comprobante_rembolso.getValor(tab_datos_comprobante_rembolso.getFilaActual(), "base_no_objeto_cpdcr"));
        } catch (Exception e) {
        }
        try {
            tot_base_imponible = Double.parseDouble(tab_datos_comprobante_rembolso.getValor(tab_datos_comprobante_rembolso.getFilaActual(), "base_imponible_cpdcr"));
        } catch (Exception e) {
        }
        dou_val_datos_reembolsos = tot_base_tarifa0 + tot_base_no_objeto + tot_base_imponible;
        return dou_val_datos_reembolsos;
    }

    public void cambiaValorDatosReembolsos(AjaxBehaviorEvent evt) {
        tab_datos_comprobante_rembolso.modificar(evt);
        double dou_val_reembolso = calcularValorDatosReembolso();
        calcularTotalDatosReembolsos();
        tab_datos_comprobante_rembolso.setValor(tab_datos_comprobante_rembolso.getFilaActual(), "valor_iva_cpdcr", dou_val_reembolso + "");
        utilitario.addUpdateTabla(tab_datos_comprobante_rembolso, "valor_iva_cpdcr", "");
    }

    public String binario(int num) {
        String str_binario = "";
        List lis_binarios = new ArrayList();
        do {
            int div = num / 2;
            int mod = num % 2;
            lis_binarios.add(mod);
            num = div;
            if (div == 1) {
                lis_binarios.add(div);
            }
        } while (num >= 2);
        int num_dijitos_binarios = lis_binarios.size();
        do {
            str_binario += lis_binarios.get(num_dijitos_binarios - 1);
            num_dijitos_binarios = num_dijitos_binarios - 1;
        } while (num_dijitos_binarios >= 1);
        return str_binario;
    }

    public void pimi() {
        String test = "HOLA como ESTAS";
        String str_trama_completa = "";
        for (int i = 0; i < test.length(); ++i) {
            char c = test.charAt(i);
            int j = (int) c;
            str_trama_completa += binario(j);
            System.out.println("letra " + c + " ascii " + j + " binario " + binario(j));
        }
        System.out.println("trama de binarios completa " + str_trama_completa);
    }

    public void buscarFactura(SelectEvent evt) {
        aut_num_factura.onSelect(evt);
        if (aut_num_factura.getValue() != null) {
            tab_tabla1.setFilaActual(aut_num_factura.getValor());
            tab_tabla2.ejecutarValorForanea(aut_num_factura.getValor());
            tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + aut_num_factura.getValor());
            tab_tabla3.ejecutarSql();
            cargar_saldo();

            utilitario.addUpdate("pac_acordion,tab_datos_comprobante_rembolso,tab_tabla2,tab_tabla3");
        }
    }

    public boolean validarDatosLiquidacion() {
        if (mas_num_auto_liquidacion.getValue() == null) {
            utilitario.agregarMensajeError("atencion", "Debe ingresar el numero de autorizacion de liquidacion");
            return false;
        } else if (mas_num_auto_liquidacion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeError("atencion", "Debe ingresar el numero de autorizacion de liquidacion");
            return false;
        }
        return true;
    }

    public void aceptarDialogoDatosLiquidacion() {
        if (validarDatosLiquidacion()) {
            dia_datos_liquidacion_compra.cerrar();
            tab_tabla1.setValor("autorizacio_cpcfa", mas_num_auto_liquidacion.getValue() + "");
//            tab_tabla1.setValor("numero_cpcfa", tex_num_liquidacion.getValue() + "");
            utilitario.addUpdate("pac_acordion");
        }
    }

    public String getParametroProducto(String parametro, String ide_inarti) {
        if (parametro != null && ide_inarti != null && !parametro.isEmpty() && !ide_inarti.isEmpty()) {
            TablaGenerica tab_articulo = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_inarti);
            if (tab_articulo.getTotalFilas() > 0) {
                if (tab_articulo.getValor(0, parametro) != null && !tab_articulo.getValor(0, parametro).isEmpty()) {
                    return tab_articulo.getValor(0, parametro);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String obtenerNumDiasFormaPago(String ide_cndfp) {
        List sql_forma_pago = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
        if (sql_forma_pago != null && !sql_forma_pago.isEmpty()) {
            return sql_forma_pago.get(0).toString();
        } else {
            return null;
        }
    }

    public void eliminarFactura() {
        System.out.println("eliminar factura");
        if (tab_tabla1.getTotalFilas() > 0) {
            TablaGenerica tab_cxp_cab_factura = utilitario.consultar("select * from cxp_cabece_factur where ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
            if (tab_cxp_cab_factura.getTotalFilas() > 0) {
                utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_anulada") + " where ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));

                String ide_cnccc = tab_cxp_cab_factura.getValor(0, "ide_cnccc");
                String ide_cncre = tab_cxp_cab_factura.getValor(0, "ide_cncre");
                // anulo el comprobante de contabilidad si existe
                if (ide_cnccc != null && !ide_cnccc.isEmpty()) {
                    conta.anular(ide_cnccc);
                }
                // anulo el comprobante de retencion si existe
                if (ide_cncre != null && !ide_cncre.isEmpty()) {
                    cls_retenciones ret = new cls_retenciones();
                    ret.anularComprobanteRetencion(ide_cncre);
                }
                // reverso las transacciones cxp realizadas con la factura seleccionada
                cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                cxp.reversar(ide_cnccc, tab_tabla1.getValor("ide_cpcfa"), "anulacion de la factura con ide numero " + tab_tabla1.getValor("ide_cpcfa"), null);

                TablaGenerica tab_cxp_detalle = utilitario.consultar("select * from cxp_detall_transa where ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
                if (tab_cxp_detalle.getTotalFilas() > 0) {
                    cls_bancos banco = new cls_bancos();
                    banco.getTab_cab_libro_banco().limpiar();
                    for (int i = 0; i < tab_cxp_detalle.getTotalFilas(); i++) {
                        if (tab_cxp_detalle.getValor(i, "ide_teclb") != null && !tab_cxp_detalle.getValor(i, "ide_teclb").isEmpty()) {
                            banco.reversar(tab_cxp_detalle.getValor(i, "ide_teclb"), "anula transaccion cxp con ide numero: " + tab_cxp_detalle.getValor(i, "ide_cpdtr"));
                            TablaGenerica tab_cab = utilitario.consultar("SELECT * from tes_cab_libr_banc where ide_teclb=" + tab_cxp_detalle.getValor(i, "ide_teclb"));
                            if (!tab_cab.getValor(0, "ide_cnccc").equals(tab_tabla1.getValor("ide_cnccc"))) {
                                conta.anular(tab_cab.getValor(0, "ide_cnccc"));
                            }
                        }
                    }
                    banco.getTab_cab_libro_banco().limpiar();
                }

                // reverso el comprovante de inventario y el comprobante de contabilidad de costos si existe
                TablaGenerica tab_inv_cab_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_incci in ( "
                        + "select ide_incci from inv_det_comp_inve where ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa") + " GROUP BY ide_incci)");
                if (tab_inv_cab_inv.getTotalFilas() > 0) {
                    utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_inepi=0 where ide_incci=" + tab_inv_cab_inv.getValor(0, "ide_incci"));
                    cls_inventario inv = new cls_inventario();
                    inv.reversar_mas(tab_tabla1.getValor("ide_cnccc"), tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Reversa de comprobante num:" + tab_tabla1.getValor("ide_cnccc"));
                    if (tab_inv_cab_inv.getValor(0, "ide_cnccc") != null && !tab_inv_cab_inv.getValor(0, "ide_cnccc").isEmpty()) {
                        conta.anular(tab_inv_cab_inv.getValor(0, "ide_cnccc"));
                    }
                }

                utilitario.getConexion().guardarPantalla();
                tab_tabla1.setFilaActual(tab_tabla1.getValor("ide_cpcfa"));
                tab_tabla1.ejecutarSql();

            }
//            if (obtenerNumDiasFormaPago(tab_tabla1.getValor("ide_cndfp")) != null) {
//                if (Integer.parseInt(obtenerNumDiasFormaPago(tab_tabla1.getValor("ide_cndfp"))) > 0) {
//                    System.out.println("d " + tab_tabla3.getTotalFilas());
//                    if (tab_tabla3.getTotalFilas() == 0) {
//                        utilitario.agregarMensajeError("", "se puede eliminar");
//                    } else {
//                        utilitario.agregarMensajeError("", "No se puede eliminar");
//                    }
//                } else {
//                    utilitario.agregarMensajeError("", "se puede eliminar");
//                }
//            }
        }
    }

    public void noGeneraRetencion() {
        boo_hizo_retencion = false;
        vir_comprobante_retencion.getTab_cab_retencion_vretencion().limpiar();
        vir_comprobante_retencion.getTab_det_retencion_vretencion().limpiar();
        aceptarComprobanteRetencion();
    }

    public boolean validarDialogoAnticipos() {

        if (tab_anticipos.getListaFilasSeleccionadas().size() == 0) {
            utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar un anticipo con el cual cancelar la factura");
            return false;
        }
        return true;
    }

    public void guardarPagoAnticipo(String ide_cpctr, double valor_pago) {
        if (ide_cpctr != null && !ide_cpctr.isEmpty() && valor_pago > 0) {
            Tabla tab_pago_anticipos = new Tabla();
            tab_pago_anticipos.setTabla("cxp_pago_anticipos", "ide_cppan", -1);
            tab_pago_anticipos.setCondicion("ide_cppan=-1");
            tab_pago_anticipos.ejecutarSql();
            tab_pago_anticipos.insertar();
            tab_pago_anticipos.setValor("ide_cpctr", ide_cpctr);
            tab_pago_anticipos.setValor("valor_cppan", valor_pago + "");
            tab_pago_anticipos.setValor("observacion_cppan", "paga la factura num " + tab_tabla1.getValor("numero_cpcfa") + " con un anticipo ");
            tab_pago_anticipos.setValor("fecha_cppan", utilitario.getFechaActual());
            tab_pago_anticipos.guardar();
        }
    }
    double dou_total_anticipo = 0;

    public void aceptarDialogoAnticipo() {
        if (validarDialogoAnticipos()) {
            dou_total_anticipo = 0;
            for (Fila actual : tab_anticipos.getSeleccionados()) {
                dou_total_anticipo = Double.parseDouble(actual.getCampos()[1] + "") + dou_total_anticipo;
                utilitario.getConexion().agregarSqlPantalla("update cxp_detall_transa set anticipo_activo=FALSE where ide_cpctr=" + actual.getRowKey());
            }

            dia_ide_anticipo.cerrar();
            utilitario.addUpdate("dia_ide_anticipo");

            dibujarDialogoPagoContado();
        }
    }

//    public void cambiaImpuesto(AjaxBehaviorEvent evt) {
//        tab_tabla5.modificar(evt);
//        double base = 0;
//        double total = 0;
//
//        String porcentaje = obtener_porcen(tab_tabla5.getValor("ide_cncim"), tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("ide_cntdo"));
//        if (porcentaje != null) {
//            System.out.println("porcentaje " + porcentaje);
//            if (tab_tabla5.getValor(tab_tabla5.getFilaActual(), "base_cndre") != null && !tab_tabla5.getValor(tab_tabla5.getFilaActual(), "base_cndre").isEmpty()) {
//                try {
//                    base = Double.parseDouble(tab_tabla5.getValor(tab_tabla5.getFilaActual(), "base_cndre"));
//                } catch (Exception e) {
//                    base = 0;
//                }
//            }
//            tab_tabla5.setValor(tab_tabla5.getFilaActual(), "porcentaje_cndre", porcentaje);
//            total = (base * Double.parseDouble(porcentaje)) / 100;
//            tab_tabla5.setValor(tab_tabla5.getFilaActual(), "valor_cndre", utilitario.getFormatoNumero(total));
//        }
//        utilitario.addUpdateTabla(tab_tabla5, "porcentaje_cndre,valor_cndre", "");
//        calcular_total_retencion();
//
//    }
//
    public void cancelarDialogo() {
        if (dia_banco.isVisible()) {
            dia_banco.cerrar();
            utilitario.addUpdate("dia_banco");
        } else if (via_comprobante_conta.isVisible()) {
            via_comprobante_conta.cerrar();
            utilitario.addUpdate("via_comprobante_conta");
        } else if (dia_datos_producto.isVisible()) {
            dia_datos_producto.cerrar();
            utilitario.addUpdate("dia_datos_producto");
        } else if (dia_datos_provee.isVisible()) {
            dia_datos_provee.cerrar();
            utilitario.addUpdate("dia_datos_provee");
        } else if (dia_datos_factura.isVisible()) {
            tab_tabla_df.limpiar();
            dia_datos_factura.cerrar();
            utilitario.addUpdate("dia_datos_factura");
        } else if (con_confirma_rete.isVisible()) {
            con_confirma_rete.cerrar();
        } else if (dia_ide_anticipo.isVisible()) {
            dia_ide_anticipo.cerrar();
        } else if (dia_datos_liquidacion_compra.isVisible()) {
            dia_datos_liquidacion_compra.cerrar();
        }

        //utilitario.getConexion().rollback(); **********
        utilitario.getConexion().getSqlPantalla().clear();
    }

    public void cargar_cuentas() {
        if (com_banco.getValue() != null) {
            banco_actual = com_banco.getValue().toString();
            com_cuenta_banco.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
            tex_num_cheque.setValue("");
        } else {
            com_cuenta_banco.setValue("");
            tex_num_cheque.setValue("");
        }
        utilitario.addUpdate("com_cuenta_banco,tex_num_cheque");
    }

    public void cargar_num_cheque() {
        if (p_tipo_tran_bancaria.equals(utilitario.getVariable("p_tes_tran_cheque"))) {
            cls_bancos banco = new cls_bancos();
            tex_num_cheque.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco.getValue() + "", utilitario.getVariable("p_tes_tran_cheque")));
        } else {
            tex_num_cheque.setValue("");
        }
        utilitario.addUpdate("tex_num_cheque");
    }

    public void aceptar_datos_producto() {
        dia_datos_producto.cerrar();
        List<String> respaldo_sql = utilitario.getConexion().getSqlPantalla();
        utilitario.getConexion().getSqlPantalla().clear();
        tab_tabla10.guardar();
        if (!utilitario.getConexion().getSqlPantalla().isEmpty()) {
            utilitario.getConexion().ejecutarSql(utilitario.getConexion().getSqlPantalla().get(0));
           // utilitario.getConexion().commit();*****
        }
        utilitario.getConexion().setSqlPantalla(respaldo_sql);
        calcula_iva();
        //calcula_total_detalles_cxp();
        utilitario.addUpdate("tab_tabla10,dia_datos_producto,pac_acordion,tab_tabla2,tab_tabla3");
    }

    public void cargarProveedores() {
        if (tab_tabla1.getTotalFilas() > 0) {
            // solo ruc 
            if (tab_tabla1.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_factura"))) {
                tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid=" + utilitario.getVariable("p_gen_tipo_iden_ruc"));
                tab_tabla1.setValor("ide_geper", null);
                utilitario.addUpdate("pac_acordion");
            }
            // diferencte de ruc
            if (tab_tabla1.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
                tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid!=" + utilitario.getVariable("p_gen_tipo_iden_ruc"));
                tab_tabla1.setValor("ide_geper", null);
                utilitario.addUpdate("pac_acordion");
            }
            // solo rise
            if (tab_tabla1.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
                tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_cntco = " + utilitario.getVariable("p_con_tipo_contribuyente_rise"));
                tab_tabla1.setValor("ide_geper", null);
                utilitario.addUpdate("pac_acordion");
            }
            tab_datos_comprobante_rembolso.ejecutarSql();
            utilitario.addUpdate("tab_datos_comprobante_rembolso");

        }
    }

    public void cambioTipoDocumento(AjaxBehaviorEvent evt) {

        tab_tabla1.modificar(evt);
        cargarProveedores();
    }

    public void abrirDatosProducto() {
        if (tab_tabla2.getValor("ide_inarti") != null && !tab_tabla2.getValor("ide_inarti").isEmpty()) {
            tab_tabla10.setCondicion("ide_inarti=" + tab_tabla2.getValor("ide_inarti").toString());
            tab_tabla10.ejecutarSql();
            dia_datos_producto.dibujar();
            utilitario.addUpdate("dia_datos_producto,tab_tabla10");
        } else {
            utilitario.agregarMensajeInfo("Datos Producto", "Atencion no ha seleccionado ningun producto");
        }

    }

    public void aceptar_datos_proveedor() {

        tab_tabla9.setAuditoria(false);
        dia_datos_provee.cerrar();
        utilitario.addUpdate("tab_tabla9,dia_datos_provee,pac_acordion,tab_tabla2,tab_tabla3");
        List<String> respaldo_sql = utilitario.getConexion().getSqlPantalla();
        utilitario.getConexion().getSqlPantalla().clear();
        tab_tabla9.guardar();
        if (!utilitario.getConexion().getSqlPantalla().isEmpty()) {
            utilitario.getConexion().ejecutarSql(utilitario.getConexion().getSqlPantalla().get(0));
            //utilitario.getConexion().commit();*****
        }
        utilitario.getConexion().setSqlPantalla(respaldo_sql);

    }

    public void abrirDatosProveedor() {
        if (tab_tabla1.getValor("ide_geper") != null && !tab_tabla1.getValor("ide_geper").isEmpty()) {
            tab_tabla9.setCondicion("ide_geper=" + tab_tabla1.getValor("ide_geper"));
            tab_tabla9.ejecutarSql();
            dia_datos_provee.dibujar();
            utilitario.addUpdate("dia_datos_provee,tab_tabla9");
        } else {
            utilitario.agregarMensajeInfo("Datos Proveedor", "Atencion no ha seleccionado ningun proveedor");
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
        if (rep_reporte.getReporteSelecionado().equals("Total Compras")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_cal.dibujar();
            } else if (sel_cal.isVisible()) {
                parametro = new HashMap();
                parametro.put("fecha_inicio", sel_cal.getFecha1());
                parametro.put("fecha_fin", sel_cal.getFecha2());
                sel_cal.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_cal");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null && !tab_tabla1.getValor("ide_cnccc").isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_tabla1.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("Comprobante de Contabilidad", "La factura seleccionada no tiene Comprobante de Contabilidad");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante de Retención")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cncre", Long.parseLong(tab_tabla1.getValor("ide_cncre")));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_rep");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Facturas de Proveedores")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cpcfa", Long.parseLong(tab_tabla1.getValor("ide_cpcfa")));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_rep");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante de Inventario")) {
            if (rep_reporte.isVisible()) {
                List sql_cab_fac_inv = utilitario.getConexion().consultar("SELECT ide_incci  FROM inv_det_comp_inve WHERE ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
                if (sql_cab_fac_inv != null && !sql_cab_fac_inv.isEmpty()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_incci", Long.parseLong(sql_cab_fac_inv.get(0).toString()));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene cabecera de factura");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Cheque")) {
            System.out.print("tipo::::::::::........  " + rep_reporte.getReporteSelecionado());
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cnccc") != null && !tab_tabla1.getValor("ide_cnccc").isEmpty()) {
                    TablaGenerica tab_lib = utilitario.consultar("select distinct * from tes_cab_libr_banc as tes,con_cab_comp_cont con, cxp_cabece_factur as cxp where con.ide_cnccc=tes.ide_cnccc and con.ide_cnccc=cxp.ide_cnccc and tes.ide_cnccc=" + tab_tabla1.getValor("ide_cnccc") + "");
                    if (tab_lib.getTotalFilas() > 0) {
                        if (tab_lib.getValor(0, "ide_tettb").equalsIgnoreCase(utilitario.getVariable("p_tes_tran_cheque"))) {
                            parametro = new HashMap();
                            rep_reporte.cerrar();
                            cls_bancos banco = new cls_bancos();
                            parametro.put("beneficiario", tab_lib.getValor("beneficiari_teclb") + "");
                            parametro.put("monto", tab_lib.getValor("valor_teclb") + "");
                            parametro.put("anio", utilitario.getAnio(tab_lib.getValor("fecha_trans_teclb")) + "");
                            parametro.put("mes", utilitario.getMes(tab_lib.getValor("fecha_trans_teclb")) + "");
                            parametro.put("dia", utilitario.getDia(tab_lib.getValor("fecha_trans_teclb")) + "");
                            parametro.put("monto_letras", banco.agregarAsteriscosCheque(utilitario.getLetrasDolarNumero(tab_lib.getValor("valor_teclb"))));
                            parametro.put("ide_cnccc", Long.parseLong(tab_lib.getValor("ide_cnccc")));
                            parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                            parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                            parametro.put("p_num_cheque", tab_lib.getValor("numero_teclb") + "");
                            parametro.put("p_num_trans", tab_lib.getValor("ide_teclb") + "");
                            List lis_geper = utilitario.getConexion().consultar("select identificac_geper from gen_persona where ide_geper=(select ide_geper from con_cab_comp_cont where ide_cnccc =" + tab_lib.getValor("ide_cnccc") + ")");
                            if (lis_geper.size() > 0) {
                                parametro.put("p_identificacion", lis_geper.get(0) + "");
                            } else {
                                parametro.put("p_identificacion", "");
                            }
                            sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                            sel_rep.dibujar();
                            utilitario.addUpdate("rep_reporte,sel_rep");
                        } else {
                            utilitario.agregarMensajeInfo("No se puede generar el cheque", "La fila seleccionada no es un cheque");
                        }
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el cheque", "La fila seleccionada no es un cheque");
                }
            }
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        utilitario.addUpdate("tab_tabla2,tab_tabla3");
    }

    @Override
    public void insertar() {

        if (tab_tabla2.isFocus()) {
            if (tab_tabla1.isFilaInsertada()) {
                tab_tabla2.insertar();
                vir_comprobante_retencion.getTab_det_retencion_vretencion().limpiar();
            }
        } else if (tab_tabla1.isFocus()) {
            bot_comp_retencion.setDisabled(true);
            tab_tabla1.insertar();
            cargarProveedores();
        } else if (vir_comprobante_retencion.getTab_det_retencion_vretencion().isFocus()) {
            vir_comprobante_retencion.getTab_det_retencion_vretencion().insertar();
        } else if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.insertar();
        } else if (tab_datos_comprobante_rembolso.isFocus()) {
            if (tab_tabla1.isFilaInsertada()) {
                if (tab_tabla1.getValor("ide_cntdo") != null && !tab_tabla1.getValor("ide_cntdo").isEmpty()) {
                    if (tab_tabla1.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_reembolso"))) {
                        tab_datos_comprobante_rembolso.insertar();
                    } else {
                        utilitario.agregarMensajeInfo("No se puede insertar", "El tipo de documento seleccionado no es Reembolso");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede insertar", "El tipo de documento seleccionado no es Reembolso");
                }
            }
        }
        eti_saldo.setValue("Saldo: ");
        eti_saldo.setStyle("font-size:16px;font-weight: bold;");
        utilitario.addUpdate("eti_saldo,bot_comp_retencion");

    }

    public boolean validarFactura() {

        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe seleccionar un proveedor");
            return false;
        }
        if (tab_tabla1.getValor("numero_cpcfa") == null || tab_tabla1.getValor("numero_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar el Numero de Factura");
            return false;
        }
        if (tab_tabla1.getValor("autorizacio_cpcfa") == null || tab_tabla1.getValor("autorizacio_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar el Numero de Autorizacion");
            return false;
        }
        if (tab_tabla1.getValor("observacion_cpcfa") == null || tab_tabla1.getValor("observacion_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar la observacion de la Factura");
            return false;
        }
        if (Double.parseDouble(tab_tabla1.getValor("total_cpcfa")) == 0) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar Detalles a la Factura ");
            return false;
        }
        if (tab_tabla2.getValor("alter_tribu_cpdfa") == null || tab_tabla2.getValor("alter_tribu_cpdfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe asignar un casillero tributario al producto ");
            return false;
        }

        List lis_numeros_fact = utilitario.getConexion().consultar("select * from cxp_cabece_factur where numero_cpcfa='" + tab_tabla1.getValor("numero_cpcfa") + "' and ide_geper=" + tab_tabla1.getValor("ide_geper") + " and autorizacio_cpcfa='" + tab_tabla1.getValor("autorizacio_cpcfa") + "'");
        if (lis_numeros_fact.size() > 0) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "El numero de factura de este proveedor ya existe ");
            return false;
        }

        if (tab_tabla1.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_reembolso"))) {

            if (Double.parseDouble(utilitario.getFormatoNumero(calcularTotalDatosReembolsos(), 2))
                    != Double.parseDouble(utilitario.getFormatoNumero(tab_tabla1.getValor("total_cpcfa"), 2))) {
                utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "El total de la factura no coincide con el total de datos de reembolso ");
                return false;
            }
            for (int i = 0; i < tab_datos_comprobante_rembolso.getTotalFilas(); i++) {
                if (tab_datos_comprobante_rembolso.getValor(i, "identificacion_cpdcr") == null || tab_datos_comprobante_rembolso.getValor(i, "identificacion_cpdcr").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar los Datos de Reembolso, no se guardaron los datos", "El campo identificacion es requerido ");
                    return false;
                }
                if (tab_datos_comprobante_rembolso.getValor(i, "serie_cpdcr") == null || tab_datos_comprobante_rembolso.getValor(i, "serie_cpdcr").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar los Datos de Reembolso, no se guardaron los datos", "El campo Serie es requerido ");
                    return false;
                }
                if (tab_datos_comprobante_rembolso.getValor(i, "autorizacion_cpdcr") == null || tab_datos_comprobante_rembolso.getValor(i, "autorizacion_cpdcr").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar los Datos de Reembolso, no se guardaron los datos", "El campo Autorizacion es requerido ");
                    return false;
                }
                if (tab_datos_comprobante_rembolso.getValor(i, "fecha_cpdcr") == null || tab_datos_comprobante_rembolso.getValor(i, "fecha_cpdcr").isEmpty()) {
                    tab_datos_comprobante_rembolso.setValor(i, "fecha_cpdcr", utilitario.getFechaActual());
                }
                if (tab_datos_comprobante_rembolso.getValor(i, "base_no_objeto_cpdcr") == null || tab_datos_comprobante_rembolso.getValor(i, "base_no_objeto_cpdcr").isEmpty()) {
                    tab_datos_comprobante_rembolso.setValor(i, "base_no_objeto_cpdcr", "0");
                }
                if (tab_datos_comprobante_rembolso.getValor(i, "base_tarifa0_cpdcr") == null || tab_datos_comprobante_rembolso.getValor(i, "base_tarifa0_cpdcr").isEmpty()) {
                    tab_datos_comprobante_rembolso.setValor(i, "base_tarifa0_cpdcr", "0");
                }
                if (tab_datos_comprobante_rembolso.getValor(i, "base_imponible_cpdcr") == null || tab_datos_comprobante_rembolso.getValor(i, "base_imponible_cpdcr").isEmpty()) {
                    tab_datos_comprobante_rembolso.setValor(i, "base_imponible_cpdcr", "0");
                }

            }
        }

        return true;
    }

    public boolean verificarExistenciaAnticipos() {

        tab_anticipos.setSql("select "
                + "ct.ide_cpctr,dt.valor_cpdtr AS total_anticipo "
                + "from cxp_cabece_transa ct "
                + "INNER join cxp_detall_transa dt on dt.ide_cpctr=ct.ide_cpctr "
                + "where ct.ide_geper=" + tab_tabla1.getValor("ide_geper") + " "
                + "and dt.ide_cpttr=" + utilitario.getVariable("p_cxp_tipo_trans_anticipo") + " "
                + "and dt.anticipo_activo is TRUE");
        tab_anticipos.ejecutarSql();
        System.out.println("anticipos " + tab_anticipos.getSql());
        if (tab_anticipos.getTotalFilas() > 0) {
            return true;
        } else {
            utilitario.agregarMensajeInfo("Atencion", "No tiene registrado ningun anticipo con el proveedor seleccionado");
        }
        return false;
    }

    public void guardar() {

        if (tab_tabla1.isFilaInsertada()) {
            obtener_alterno();
            if (validarFactura()) {
                if (!tab_tabla1.getValor("ide_cntdo").equalsIgnoreCase(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
                    con_confirma_rete.dibujar();
                } else {
                    aceptarComprobanteRetencion();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
            bot_comp_retencion.setDisabled(false);
        } else if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.eliminar();
        } else {

            utilitario.getTablaisFocus().eliminar();
        }
        utilitario.addUpdate("bot_comp_retencion");

    }

    public void aceptarDatosFactura(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
            TablaGenerica tab_cxp_cab = utilitario.consultar("select * from cxp_cabece_factur where ide_empr=" + utilitario.getVariable("ide_empr") + " and  ide_cntdo=" + utilitario.getVariable("p_con_tipo_documento_liquidacion_compra") + " order by numero_cpcfa desc");
            if (tab_cxp_cab.getTotalFilas() > 0) {
                tab_tabla1.setValor("autorizacio_cpcfa", tab_cxp_cab.getValor(0, "autorizacio_cpcfa"));
                String num_liq = tab_cxp_cab.getValor(0, "numero_cpcfa");
                String num = (Integer.parseInt(num_liq.substring(6, num_liq.length())) + 1) + "";
                String num_liquidacion = num_liq.substring(0, 6);
                num_liquidacion = num_liquidacion.concat(utilitario.generarCero(8 - num.length())).concat(num);
                System.out.println("num " + num_liquidacion);
                tab_tabla1.setValor("numero_cpcfa", num_liquidacion);
                utilitario.addUpdate("pac_acordion");
            } else {
                dia_datos_liquidacion_compra.dibujar();
            }
        } else {
            if (tab_tabla1.getValor("ide_geper") != null) {
                if (tab_tabla1.getValor("numero_cpcfa").length() > 8) {
                    tab_tabla1.setValor(tab_tabla1.getFilaActual(), "autorizacio_cpcfa", "");
                    String serie = tab_tabla1.getValor("numero_cpcfa").substring(0, 6);
                    String num_factura = tab_tabla1.getValor("numero_cpcfa").substring(6, tab_tabla1.getValor("numero_cpcfa").length());
                    TablaGenerica tab_datos_fac = utilitario.consultar("SELECT * FROM cxp_datos_factura df WHERE "
                            + "df.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                            + "and df.ide_geper=" + tab_tabla1.getValor("ide_geper") + " "
                            + "and df.serie_cpdaf='" + serie + "' "
                            + "and df.fecha_caducidad >='" + utilitario.getFechaActual() + "'");
                    int numero_factura = Integer.parseInt(num_factura);
                    int serie_factura = Integer.parseInt(serie);

                    if (tab_datos_fac.getTotalFilas() > 0) {
                        TablaGenerica tab_valida_numero = utilitario.consultar("SELECT * FROM cxp_datos_factura df  "
                                + "WHERE df.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                                + "and df.ide_geper=" + tab_tabla1.getValor("ide_geper") + " "
                                + "and df.serie_cpdaf='" + serie + "' "
                                + "and df.fecha_caducidad >'" + utilitario.getFechaActual() + "' "
                                + "and rango_inicial_cpdaf<=" + numero_factura + " "
                                + "and rango_final_cpdaf >=" + numero_factura + " order by df.ide_cpdaf desc ");
                        if (tab_valida_numero.getTotalFilas() > 0) {
                            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "autorizacio_cpcfa", tab_valida_numero.getValor(0, "autorizacion_cpdaf"));
                        } else {
                            //utilitario.agregarMensajeInfo("Numero no valido", "Ingrese un numero dentro del rango " + tab_datos_fac.getValor("rango_inicial_cpdaf") + "hasta: " + tab_datos_fac.getValor("rango_final_cpdaf"));
                            if (serie_factura <= numero_factura || serie_factura >= numero_factura) {
                                tab_tabla_df.limpiar();
                                String seriecaracter = tab_tabla1.getValor("numero_cpcfa").substring(0, 6);
                                tab_tabla_df.getColumna("serie_cpdaf").setValorDefecto(seriecaracter);
                                tab_tabla_df.getColumna("serie_cpdaf").setLectura(true);
                                tab_tabla_df.getColumna("ide_geper").setValorDefecto(tab_tabla1.getValor("ide_geper"));
                                tab_tabla_df.insertar();
                                dia_datos_factura.dibujar();
                                utilitario.addUpdate("dia_datos_factura");
                                dia_datos_factura.getBot_aceptar().setMetodo("aceptarDialogoDatosFactura");
                            }
                        }
                    } else {
                        tab_tabla_df.limpiar();
                        String seriecaracter = tab_tabla1.getValor("numero_cpcfa").substring(0, 6);
                        tab_tabla_df.getColumna("serie_cpdaf").setValorDefecto(seriecaracter);
                        tab_tabla_df.getColumna("serie_cpdaf").setLectura(true);
                        tab_tabla_df.getColumna("ide_geper").setValorDefecto(tab_tabla1.getValor("ide_geper"));
                        tab_tabla_df.insertar();

                        dia_datos_factura.dibujar();
                        utilitario.addUpdate("dia_datos_factura");
                        dia_datos_factura.getBot_aceptar().setMetodo("aceptarDialogoDatosFactura");
                    }
                    utilitario.addUpdateTabla(tab_tabla1, "autorizacio_cpcfa", "");
                } else {
                    utilitario.agregarMensajeError("No se puede abrir los datos de la factura", "Número de factura No Valida");
                }
            } else {
                utilitario.agregarMensajeError("No se puede abrir los datos de la factura", "Debe ingresar un Proveedor");
            }
        }
    }

    public void aceptarDialogoDatosFactura() {

        if (tab_tabla_df.getValor("ide_geper") != null && tab_tabla_df.getValor("serie_cpdaf") != null
                && tab_tabla_df.getValor("autorizacion_cpdaf") != null
                && tab_tabla_df.getValor("rango_final_cpdaf") != null
                && tab_tabla_df.getValor("rango_inicial_cpdaf") != null && tab_tabla_df.getValor("fecha_caducidad") != null) {
            String serie = tab_tabla1.getValor("numero_cpcfa").substring(0, 6);
            boolean boo_datos_correctos = false;
            if (utilitario.isFechaMayor(utilitario.getFecha(tab_tabla_df.getValor("fecha_caducidad")), utilitario.getFecha(tab_tabla1.getValor("fecha_emisi_cpcfa")))) {
                boo_datos_correctos = true;
            } else if (!utilitario.isFechaMenor(utilitario.getFecha(tab_tabla_df.getValor("fecha_caducidad")), utilitario.getFecha(tab_tabla1.getValor("fecha_emisi_cpcfa")))) {
                boo_datos_correctos = true;
            }
            if (boo_datos_correctos) {
                if (serie.equals(tab_tabla_df.getValor("serie_cpdaf"))) {
                    int num_factura = Integer.parseInt(tab_tabla1.getValor("numero_cpcfa").substring(6, tab_tabla1.getValor("numero_cpcfa").length()));
                    int rango_inicial = Integer.parseInt(tab_tabla_df.getValor("rango_inicial_cpdaf"));
                    int rango_final = Integer.parseInt(tab_tabla_df.getValor("rango_final_cpdaf"));
                    if (rango_inicial < rango_final) {

                        if (num_factura >= rango_inicial && num_factura <= rango_final) {
                            tab_tabla1.setValor("autorizacio_cpcfa", tab_tabla_df.getValor("autorizacion_cpdaf"));
                            dia_datos_factura.cerrar();
                            utilitario.addUpdate("dia_datos_factura,pac_acordion");
                        } else {
                            utilitario.agregarMensajeInfo("No se puede guadar", "El número de factura ingresado no se encuentra en el rango de los datos de la factura");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No se puede guadar", "El rango inicial es mayor al rango final");
                    }

                } else {
                    utilitario.agregarMensajeInfo("No se puede guadar", "El número de serie ingresado en la factura no es igual al ingresado");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede guadar", "La fecha de caducidad no es valida");
            }
        } else {
            utilitario.agregarMensajeInfo("No se pudo Guardar", "Debe ingresar todos los datos de la factura");
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public void inicio() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
            tab_tabla3.ejecutarSql();
            cargar_saldo();
        }
    }

    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
            tab_tabla3.ejecutarSql();
            cargar_saldo();

        }
    }

    public void cargar_saldo() {
        double sum_pagos = 0;
        double tot_retenido = 0;
        double tot_fac = 0;
        if (tab_tabla1.getValor("total_cpcfa") != null) {
            tot_fac = Double.parseDouble(tab_tabla1.getValor("total_cpcfa"));
        }
        if (tab_tabla1.getValor("ide_cncre") != null && !tab_tabla1.getValor("ide_cncre").isEmpty()) {
            TablaGenerica tab_tot_retenido = utilitario.consultar("select *from con_detall_retenc where ide_cncre=" + tab_tabla1.getValor("ide_cncre"));
            tot_retenido = Double.parseDouble(utilitario.getFormatoNumero(tab_tot_retenido.getSumaColumna("valor_cndre"), 2));
        }

        if (tab_tabla3.getTotalFilas() > 0) {
            sum_pagos = Double.parseDouble(utilitario.getFormatoNumero(tab_tabla3.getSumaColumna("valor_cpdtr"), 2));
            tot_fac = Double.parseDouble(utilitario.getFormatoNumero(tot_fac, 2));
            double saldo = tot_fac - sum_pagos - tot_retenido;
            eti_saldo.setValue("Saldo: " + utilitario.getFormatoNumero(saldo, 2));
            eti_saldo.setStyle("font-size:16px;font-weight: bold;");
        } else {
            tot_fac = Double.parseDouble(utilitario.getFormatoNumero(tot_fac, 2));
            eti_saldo.setValue("Saldo: " + utilitario.getFormatoNumero(tot_fac - tot_retenido) + "");
            eti_saldo.setStyle("font-size:16px;font-weight: bold;");
        }

        utilitario.addUpdate("eti_saldo");
    }

    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
            tab_tabla3.ejecutarSql();
            cargar_saldo();
        }
    }

    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
            tab_tabla3.ejecutarSql();
            cargar_saldo();

        }
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public void obtener_total_detalle_factura_cxp(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);

        double cantidad = 0;
        double precio = 0;
        double total = 0;

        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_cpdfa") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_cpdfa").isEmpty()) {
            try {
                cantidad = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_cpdfa"));
            } catch (Exception e) {
                cantidad = 0;
            }
        }

        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_cpdfa") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_cpdfa").isEmpty()) {
            try {
                precio = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_cpdfa"));
            } catch (Exception e) {
                precio = 0;
            }
        }

        total = cantidad * precio;
        tab_tabla2.setValor(tab_tabla2.getFilaActual(), "valor_cpdfa", utilitario.getFormatoNumero(total));

        utilitario.addUpdateTabla(tab_tabla2, "valor_cpdfa", "");

        calcula_iva();
        //   calcula_total_detalles_cxp();
    }

    public void obtener_total_detalle_factura_cxp(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_cpdfa") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_cpdfa").isEmpty()) {
            try {
                cantidad = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_cpdfa"));
            } catch (Exception e) {
                cantidad = 0;
            }
        }

        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_cpdfa") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_cpdfa").isEmpty()) {
            try {
                precio = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_cpdfa"));
            } catch (Exception e) {
                precio = 0;
            }
        }

        total = cantidad * precio;
        tab_tabla2.setValor(tab_tabla2.getFilaActual(), "valor_cpdfa", utilitario.getFormatoNumero(total, 2));

        if (getParametroProducto("iva_inarti", tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti")) != null) {
            tab_tabla2.setValor(tab_tabla2.getFilaActual(), "iva_inarti_cpdfa", getParametroProducto("iva_inarti", tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti")));
        }

        utilitario.addUpdateTabla(tab_tabla2, "valor_cpdfa,iva_inarti_cpdfa", "");

        calcula_iva();
        //calcula_total_detalles_cxp();

    }

    public String obtener_aplica_iva_producto(String ide_inarti) {

        List sql_iva = utilitario.getConexion().consultar("select iva_inarti from inv_articulo where ide_inarti=" + ide_inarti);
        String aplica_iva = "";
        if (sql_iva.get(0) != null) {
            if (sql_iva.get(0).toString().equals("1")) {
                aplica_iva = "si";
            } else if (sql_iva.get(0).toString().equals("-1")) {
                aplica_iva = "no";
            } else if (sql_iva.get(0).toString().equals("0")) {
                aplica_iva = "no objeto";
            }
            return aplica_iva;
        } else {
            return null;
        }
    }

    public void obtener_alterno() {
        String tipo_articulo = "";
        String aplica_iva = "";
        String ide_cntco = conta.obtenerParametroPersona("ide_cntco", tab_tabla1.getValor("ide_geper"));
        boolean boo_tipo_rise = false;
        if (ide_cntco != null) {
            if (ide_cntco.equals(utilitario.getVariable("p_con_tipo_contribuyente_rise"))) {
                boo_tipo_rise = true;
            }
        }

        //  1, Si aplica iva
        // -1, No aplica iva
        //  0, No OBJETO iva
        if (boo_tipo_rise == false) {
            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                if (tab_tabla2.getValor(i, "ide_inarti") != null && !tab_tabla2.getValor(i, "ide_inarti").isEmpty()) {
                    tipo_articulo = in.obtener_tipo_articulo(tab_tabla2.getValor(i, "ide_inarti"));
                    //aplica_iva = obtener_aplica_iva_producto(tab_tabla2.getValor(i, "ide_inarti"));
                    if (!tipo_articulo.equals(p_activos_fijos) && tab_tabla2.getValor(i, "iva_inarti_cpdfa").equals("1") && tab_tabla2.getValor(i, "credi_tribu_cpdfa").equals("true")) {
                        tab_tabla2.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_pagos_12%_dertri_501"));
                    } else if (tab_tabla2.getValor(i, "iva_inarti_cpdfa").equals("1") && tab_tabla2.getValor(i, "credi_tribu_cpdfa").equals("true")) {
                        tab_tabla2.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_local_12%_dertri_502"));
                    } else if (tab_tabla2.getValor(i, "iva_inarti_cpdfa").equals("1") && tab_tabla2.getValor(i, "credi_tribu_cpdfa").equals("false")) {
                        tab_tabla2.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_otra_adqui_pago_12%_no_dertri_503"));
                    } else if (tab_tabla2.getValor(i, "iva_inarti_cpdfa").equals("-1")) {
                        tab_tabla2.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_pagos_0%_507"));
                    } else if (tab_tabla2.getValor(i, "iva_inarti_cpdfa").equals("0")) {
                        tab_tabla2.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_no_obj_iva_531"));
                    } else {
                        tab_tabla2.setValor(i, "alter_tribu_cpdfa", "00");
                    }
                }

            }
        } else {
            boo_tipo_rise = false;
            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                tab_tabla2.setValor(i, "alter_tribu_cpdfa", "518");
            }
        }
        utilitario.addUpdate("tab_tabla2");

    }

    public String cargar_nom_proveedor() {
        String provee_actual = tab_tabla1.getValor("ide_geper");
        String nombre_proveedor = "";
        List nom_prove = utilitario.getConexion().consultar("select nom_geper from gen_persona where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_geper=" + provee_actual);
        if (nom_prove.get(0) != null) {
            nombre_proveedor = nom_prove.get(0).toString();
        }
        return nombre_proveedor;
    }

    public void calcula_iva1(AjaxBehaviorEvent evt) {
//  Se ejecuta cuando cambia el iva de un producto en la tabla 2
        tab_tabla1.modificar(evt);
        calcula_iva();
    }

    public void calcula_iva2(AjaxBehaviorEvent evt) {
//  Se ejecuta cuando cambia el iva de un producto en la tabla 2
        tab_tabla2.modificar(evt);
        calcula_iva();
    }

    private void calcula_iva() {

        base_grabada = 0;
        base_no_objeto = 0;
        base_tarifa0 = 0;
        valor_iva = 0;
        iva30 = 0;
        iva70 = 0;
        iva100 = 0;
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (tab_tabla2.getValor(i, "ide_inarti") != null) {
                //List sql_iva = utilitario.getConexion().consultar("select iva_inarti from inv_articulo where ide_inarti=" + tab_tabla2.getValor(i, "ide_inarti"));
                String art = in.obtener_tipo_articulo(tab_tabla2.getValor(i, "ide_inarti"));
                int iva = 0;
                try {
                    iva = Integer.parseInt(tab_tabla2.getValor(i, "iva_inarti_cpdfa"));
                } catch (Exception e) {
                }
                if (iva == 1) {
                    base_grabada = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa")) + base_grabada;
                    if (art.equals(p_bienes)) {
                        iva30 = iva30 + Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa"));
                    }
                    if (art.equals(p_activos_fijos)) {
                        iva30 = iva30 + Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa"));
                    }

                    if (art.equals(p_servicios)) {
                        iva70 = iva70 + Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa"));
                    }
                    if (art.equals(p_honorarios_profes)) {
                        iva100 = iva100 + Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa"));
                    }
                } else {
                    if (iva == 0) {
                        base_no_objeto = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa")) + base_no_objeto;
                    } else {
                        base_tarifa0 = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa")) + base_tarifa0;
                    }
                }
            }
        }

        double porce_descuento = 0;
        if (tab_tabla1.getValor("porcen_desc_cpcfa") != null) {
            try {
                porce_descuento = Double.parseDouble(tab_tabla1.getValor("porcen_desc_cpcfa"));
            } catch (Exception e) {
            }

        }

        if (porce_descuento != 0) {
            //si hay % de descuento
            base_grabada = base_grabada - (base_grabada * (porce_descuento / 100));
            base_no_objeto = base_no_objeto - (base_no_objeto * (porce_descuento / 100));
            base_tarifa0 = base_tarifa0 - (base_tarifa0 * (porce_descuento / 100));
        }

        valor_iva = base_grabada * p_porcentaje_iva;

        tab_tabla1.setValor("base_grabada_cpcfa", utilitario.getFormatoNumero(base_grabada, 2));
        tab_tabla1.setValor("base_no_objeto_iva_cpcfa", utilitario.getFormatoNumero(base_no_objeto, 2));
        tab_tabla1.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(valor_iva, 2));
        tab_tabla1.setValor("base_tarifa0_cpcfa", utilitario.getFormatoNumero(base_tarifa0, 2));
        utilitario.addUpdate("pac_acordion");
        calcula_total_detalles_cxp();
    }

    public void calcula_total_detalles_cxp(AjaxBehaviorEvent evt) {
        //se ejecuta cuando cambia el valor de otros o procentaje de descuento o descuento
        tab_tabla1.modificar(evt);
        calcula_total_detalles_cxp();
    }

    private void calcula_total_detalles_cxp() {

        double total_fac = 0;
        double descuento = 0;
        double val_iva = 0;
        double val_base_no_iva = 0;
        double val_base_grabada = 0;
        double otros = 0;
        double val_tarifa0 = 0;
        if (tab_tabla1.getValor("descuento_cpcfa") != null) {
            try {
                descuento = Double.parseDouble(tab_tabla1.getValor("descuento_cpcfa"));
            } catch (Exception e) {
            }
        }

        if (tab_tabla1.getValor("base_no_objeto_iva_cpcfa") != null) {
            try {
                val_base_no_iva = Double.parseDouble(tab_tabla1.getValor("base_no_objeto_iva_cpcfa"));
            } catch (Exception e) {
            }

        }
        if (tab_tabla1.getValor("base_grabada_cpcfa") != null) {
            try {
                val_base_grabada = Double.parseDouble(tab_tabla1.getValor("base_grabada_cpcfa")) - descuento;
            } catch (Exception e) {
            }

        }

        if (tab_tabla1.getValor("valor_iva_cpcfa") != null) {
            try {
                val_iva = (val_base_grabada * 12) / 100;
            } catch (Exception e) {
            }

        }

        if (tab_tabla1.getValor("otros_cpcfa") != null) {
            try {
                otros = Double.parseDouble(tab_tabla1.getValor("otros_cpcfa"));
            } catch (Exception e) {
            }

        }
        if (tab_tabla1.getValor("base_tarifa0_cpcfa") != null) {
            try {
                val_tarifa0 = Double.parseDouble(tab_tabla1.getValor("base_tarifa0_cpcfa"));
            } catch (Exception e) {
            }

        }
        total_fac = (val_base_grabada + val_base_no_iva + val_tarifa0);
        total_fac += val_iva + otros;

        if (tab_tabla1.getTotalFilas() > 0) {
            tab_tabla1.setValor("total_cpcfa", utilitario.getFormatoNumero(total_fac, 2));
            tab_tabla1.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(val_iva, 2));
        }

        utilitario.addUpdate("pac_acordion");
        cargar_saldo();

    }

    public void aceptar_nuevo_comprobante_retencion() {
        con_guardar.cerrar();
        // cambio el estado de la retencion a anaulado al igual que el comprobante de libro banco
        bandera_asiento_reversa = 1;
        vir_comprobante_retencion.setVistaRetencion(tab_tabla1, tab_tabla2, false);

        if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
            tot_retenido = 0;
            for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
                tot_retenido = Double.parseDouble(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre")) + tot_retenido;
            }
            boo_hizo_retencion = true;
            vir_comprobante_retencion.dibujar();
            utilitario.addUpdate("vir_comprobante_retencion");
        } else {
            // no se genero retencion
            boo_hizo_retencion = false;
            aceptarComprobanteRetencion();
        }

    }

    public void generarComprobanteRetencion() {
        if (con_confirma_rete.isVisible()) {
            con_confirma_rete.cerrar();
        }
        List sql_num_pagos = utilitario.getConexion().consultar("select * from cxp_detall_transa where ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa") + " and numero_pago_cpdtr>0");
        if (sql_num_pagos.isEmpty()) {
            if (tab_tabla1.getValor("ide_cncre") == null || tab_tabla1.getValor("ide_cncre").isEmpty()) {
                vir_comprobante_retencion.setVistaRetencion(this.tab_tabla1, this.tab_tabla2, false);
                if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
                    tot_retenido = 0;
                    for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
                        tot_retenido = Double.parseDouble(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre")) + tot_retenido;
                    }
                    boo_hizo_retencion = true;
                    vir_comprobante_retencion.dibujar();
                    utilitario.addUpdate("vir_comprobante_retencion");
                } else {
                    // no se genero retencion
                    boo_hizo_retencion = false;
                    aceptarComprobanteRetencion();
                }

            } else {
                con_guardar.dibujar();
                utilitario.addUpdate("con_guardar");
            }
        } else {
            utilitario.agregarMensajeInfo("No puede generar una nueva retencion", "La factura tiene Pagos Realizados");
        }

    }
    double tot_retenido = 0;
    boolean boo_tip_tran_cheque = false;
    boolean boo_hizo_retencion = false;
    String p_tipo_tran_bancaria = "";

    public void dibujarDialogoPagoContado() {
        eti_monto_cheque.setValue("Monto: ");
        tex_num_cheque.setValue(null);
        tex_monto_cheque.setDisabled(true);
        com_cuenta_banco.setValue(null);
        cal_fecha_cheque.setValue(utilitario.getFecha(tab_tabla1.getValor("fecha_emisi_cpcfa")));

        if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_efec"))) {
            boo_tip_tran_cheque = false;
            eti_banc.setValue("Caja: ");
            eti_cue_banc.setValue("Caja (Detalle): ");
            eti_num_cheque.setRendered(false);
            tex_num_cheque.setRendered(false);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cpcfa")) - tot_retenido - dou_total_anticipo), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is TRUE and ide_empr=" + utilitario.getVariable("ide_empr"));
            dia_banco.dibujar();
            utilitario.addUpdate("dia_banco");
            p_tipo_tran_bancaria = utilitario.getVariable("p_tes_tran_retiro_caja");
        } else if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_cheque"))) {
            boo_tip_tran_cheque = true;
            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Cheque: ");
            eti_num_cheque.setRendered(true);
            tex_num_cheque.setRendered(true);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cpcfa")) - tot_retenido - dou_total_anticipo), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is false and ide_empr=" + utilitario.getVariable("ide_empr"));
            p_tipo_tran_bancaria = utilitario.getVariable("p_tes_tran_cheque");
            dia_banco.dibujar();
            utilitario.addUpdate("dia_banco");
        } else if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_transferencia"))) {
            boo_tip_tran_cheque = false;
            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Transferencia: ");
            eti_num_cheque.setRendered(true);
            tex_num_cheque.setRendered(true);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cpcfa")) - tot_retenido - dou_total_anticipo), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is false and ide_empr=" + utilitario.getVariable("ide_empr"));
            p_tipo_tran_bancaria = utilitario.getVariable("p_tes_tran_transferencia_menos");
            dia_banco.dibujar();
            utilitario.addUpdate("dia_banco");
        }
//        if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_deposito"))) {
//            eti_banc.setValue("Banco: ");
//            eti_cue_banc.setValue("Cuenta Banco: ");
//            eti_num_cheque.setValue("Numero Deposito: ");
//            eti_num_cheque.setRendered(true);
//            tex_num_cheque.setRendered(true);
//            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - tot_retenido), 2));
//            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is false and ide_empr=" + utilitario.getVariable("ide_empr"));
//        }
    }

    public void dibujarAsientoContable() {
        TablaGenerica sql_forma_pago = utilitario.consultar("select * from con_deta_forma_pago where ide_cndfp=" + tab_tabla1.getValor("ide_cndfp"));
        int dias = -1;
        if (sql_forma_pago.getTotalFilas() > 0) {
            if (sql_forma_pago.getValor(0, "dias_cndfp") != null && !sql_forma_pago.getValor(0, "dias_cndfp").isEmpty()) {
                dias = Integer.parseInt(sql_forma_pago.getValor(0, "dias_cndfp"));
            }
        }
        System.out.println("dias " + dias);
        if (dias == 0) {
            if (!tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_deposito"))
                    && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_tarjeta_credito"))
                    && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_donacion"))
                    && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_reembolso_caja"))
                    && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_transferencia_casas"))) {

                if (verificarExistenciaAnticipos()) {
                    dia_ide_anticipo.dibujar();
                } else {
                    dibujarDialogoPagoContado();
                }
            } else {
                dou_total_anticipo = 0;
                generarComprobanteContabilidad();
            }
        } else if (dias > 0) {
            dou_total_anticipo = 0;
            generarComprobanteContabilidad();
        }
    }

    public void aceptarComprobanteRetencion() {

        if (con_confirma_rete.isVisible()) {
            con_confirma_rete.cerrar();
        }
        bandera_asiento_reversa = 0;
        if (boo_hizo_retencion) {
            if (vir_comprobante_retencion.validarComprobanteRetencion()) {
                //if (validarComprobanteRetencion()) {
                if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
                    tot_retenido = vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
                } else {
                    tot_retenido = 0;
                }
                if (vir_comprobante_retencion.getTab_datos_proveedor().isFilaModificada()) {
                    utilitario.getConexion().agregarSqlPantalla("update gen_persona set direccion_geper ='" + vir_comprobante_retencion.getTab_datos_proveedor().getValor("direccion_geper") + "'where ide_geper =" + tab_tabla1.getValor("ide_geper") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                }
                vir_comprobante_retencion.cerrar();
                dibujarAsientoContable();
            }
        } else {
            dibujarAsientoContable();
        }
    }

    public String getNombreAriculo(String ide_inarti) {
        TablaGenerica tab_articulo = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_inarti);
        if (tab_articulo.getTotalFilas() > 0) {
            return tab_articulo.getValor(0, "nombre_inarti");
        } else {
            return null;
        }
    }

    public void generarComprobanteContabilidad() {

        conta.limpiar();
        if (tab_tabla1.getValor("observacion_cpcfa") != null) {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_diario, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cpcfa"), tab_tabla1.getValor("observacion_cpcfa"));
        } else {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_diario, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cpcfa"), "por concepto de factura por pagar numero " + tab_tabla1.getValor("numero_cpcfa"));
        }

        lista_detalles.clear();

        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_tabla2.getValor(i, "ide_inarti"));
            System.out.println("gasto activo " + ide_cuenta_inv_gas_act);
            if (ide_cuenta_inv_gas_act != null) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "valor_cpdfa"), 2)), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "valor_cpdfa"), 2)), getNombreAriculo(tab_tabla2.getValor(i, "ide_inarti"))));
            }
        }
        if (!tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_deposito"))
                && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_reembolso_caja"))
                && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_tarjeta_credito"))
                && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_donacion"))) {
            String ide_cuenta_cuentas_x_pagar = conta.buscarCuentaPersona("CUENTA POR PAGAR", tab_tabla1.getValor("ide_geper"));
            double valor;
            if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
                valor = Double.parseDouble(tab_tabla1.getValor("total_cpcfa")) - vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
            } else {
                valor = Double.parseDouble(tab_tabla1.getValor("total_cpcfa"));
            }
            if (ide_cuenta_cuentas_x_pagar != null) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_cuentas_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            }
        }

        for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
            if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim") != null) {
                if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").toString().equals(p_iva30) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").toString().equals(p_iva70) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").toString().equals(p_iva100)) {
                    String ide_cuenta_retencion_iva_x_pagar = conta.buscarCuenta("RETENCION IVA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                    if (ide_cuenta_retencion_iva_x_pagar != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_iva_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    }
                } else {
                    String ide_cuenta_retencion_renta_x_pagar = conta.buscarCuenta("RETENCION RENTA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                    if (ide_cuenta_retencion_renta_x_pagar != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_renta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    }
                }
            }
        }
        String ide_cuenta_credito_tributario = conta.buscarCuenta("IVA CREDITO TRIBUTARIO", null, null, null, utilitario.getVariable("p_con_porcentaje_imp_iva"), null, null);
        if (ide_cuenta_credito_tributario != null) {
            if (Double.parseDouble(tab_tabla1.getValor("valor_iva_cpcfa")) != 0) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_credito_tributario, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla1.getValor("valor_iva_cpcfa"), 2)), ""));
            }
        } else {
            utilitario.agregarMensajeInfo("No se encontro la cuenta para iva pagado en compras", "");
        }

        if (dou_total_anticipo > 0) {
            TablaGenerica tab_anticipo = utilitario.consultar("select * from con_det_comp_cont where ide_cnccc in ( "
                    + "select ide_cnccc from cxp_detall_transa where ide_cpctr in (" + tab_anticipos.getFilasSeleccionadas() + ") "
                    + "group by ide_cnccc) "
                    + "and ide_cnlap=" + utilitario.getVariable("p_con_lugar_debe") + "");

            if (dou_total_anticipo > 0) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_anticipo.getValor(0, "ide_cndpc"), Double.parseDouble(utilitario.getFormatoNumero(dou_total_anticipo, 2)), ""));
            }
        }

        System.out.println("lista detalles " + lista_detalles.size());
        cab_com_con.setDetalles(lista_detalles);

        via_comprobante_conta.setVistaAsiento(cab_com_con);

        via_comprobante_conta.dibujar();

        utilitario.addUpdate("via_comprobante_conta");

    }
    String str_num_cheque_cxp;

    public void generarComprobanteContabilidadContado() {
        if (com_banco.getValue() != null && com_cuenta_banco.getValue() != null) {
            cuenta_bancaria_asiento = com_cuenta_banco.getValue().toString();
            dia_banco.cerrar();
            utilitario.addUpdate("dia_banco,tab_tabla5");
            str_num_cheque_cxp = tex_num_cheque.getValue() + "";
            if (str_num_cheque_cxp.isEmpty()) {
                str_num_cheque_cxp = "0";
            }
            conta.limpiar();

            if (tab_tabla1.getValor("observacion_cpcfa") != null) {
                cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_egreso, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cpcfa"), tab_tabla1.getValor("observacion_cpcfa"));
            } else {
                cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_egreso, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cpcfa"), "por concepto de factura por pagar numero " + tab_tabla1.getValor("numero_cpcfa"));
            }

            lista_detalles.clear();
            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_tabla2.getValor(i, "ide_inarti"));
                if (ide_cuenta_inv_gas_act != null) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "valor_cpdfa"), 2)), ""));
                } else {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "valor_cpdfa"), 2)), getNombreAriculo(tab_tabla2.getValor(i, "ide_inarti"))));
                }
            }
            List cuenta_contable_banco_sql = utilitario.getConexion().consultar("select ide_cndpc from tes_cuenta_banco where ide_tecba=" + cuenta_bancaria_asiento);
            double valor = Double.parseDouble(tab_tabla1.getValor("total_cpcfa")) - vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre") - dou_total_anticipo;
            if (cuenta_contable_banco_sql != null) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), cuenta_contable_banco_sql.get(0).toString(), Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            }
            for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
                if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim") != null) {
                    if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").toString().equals(p_iva30) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").toString().equals(p_iva70) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").toString().equals(p_iva100)) {
                        String ide_cuenta_retencion_iva_x_pagar = conta.buscarCuenta("RETENCION IVA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                        if (ide_cuenta_retencion_iva_x_pagar != null) {
                            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_iva_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                        }
                    } else {
                        String ide_cuenta_retencion_renta_x_pagar = conta.buscarCuenta("RETENCION RENTA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                        if (ide_cuenta_retencion_renta_x_pagar != null) {
                            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_renta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                        }
                    }
                }
            }
            String ide_cuenta_credito_tributario = conta.buscarCuenta("IVA CREDITO TRIBUTARIO", null, null, null, utilitario.getVariable("p_con_porcentaje_imp_iva"), null, null);
            if (ide_cuenta_credito_tributario != null) {
                if (Double.parseDouble(tab_tabla1.getValor("valor_iva_cpcfa")) != 0) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_credito_tributario, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla1.getValor("valor_iva_cpcfa"), 2)), ""));
                }
            }

            if (dou_total_anticipo > 0) {
                TablaGenerica tab_anticipo = utilitario.consultar("select * from con_det_comp_cont where ide_cnccc in ( "
                        + "select ide_cnccc from cxp_detall_transa where ide_cpctr in (" + tab_anticipos.getFilasSeleccionadas() + ") "
                        + "group by ide_cnccc) "
                        + "and ide_cnlap=" + utilitario.getVariable("p_con_lugar_debe") + "");

                if (dou_total_anticipo > 0) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), tab_anticipo.getValor(0, "ide_cndpc"), Double.parseDouble(utilitario.getFormatoNumero(dou_total_anticipo, 2)), ""));
                }
            }

            cab_com_con.setDetalles(lista_detalles);
            via_comprobante_conta.setVistaAsiento(cab_com_con);
            via_comprobante_conta.dibujar();
            utilitario.addUpdate("via_comprobante_conta");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un banco y una cuenta bancaria", "");
        }

    }

    public synchronized void aceptarComprobanteContabilidad() {
        if (via_comprobante_conta.validarComprobante()) {
            cab_com_con.setObservacion_cnccc(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            lista_detalles.clear();
            for (int i = 0; i < via_comprobante_conta.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            String ide_cnccc = conta.generarAsientoContable(cab_com_con);

            if (ide_cnccc != null) {
                tab_tabla_df.guardar();
                tab_tabla1.setValor("ide_cnccc", ide_cnccc);
                tab_tabla1.guardar();
                tab_tabla2.guardar();
                tab_datos_comprobante_rembolso.guardar();
                via_comprobante_conta.cerrar();
                if (realizarComprobanteInventario()) {
                    generarComprobanteInventario(ide_cnccc);
                }
                if (boo_hizo_retencion) {
                    vir_comprobante_retencion.getTab_cab_retencion_vretencion().setValor("ide_cnccc", ide_cnccc);
                    vir_comprobante_retencion.getTab_cab_retencion_vretencion().guardar();
                    vir_comprobante_retencion.getTab_det_retencion_vretencion().guardar();
                    tab_tabla1.setValor("ide_cncre", vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("ide_cncre"));
                    utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set ide_cncre=" + vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("ide_cncre") + " where ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
                }
                cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                String num_dias = conta.obtenerNumDiasFormaPago(tab_tabla1.getValor("ide_cndfp"));
                if (!tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_tarjeta_credito"))
                        && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_donacion"))) {
                    try {
                        if (Integer.parseInt(num_dias) == 0) {
                            utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set pagado_cpcfa=true where ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                            cls_bancos bancos = new cls_bancos();
                            bancos.generarLibroBancoCxP(tab_tabla1, com_cuenta_banco.getValue() + "", Double.parseDouble(tex_monto_cheque.getValue() + ""), "Pago: " + tab_tabla1.getValor("observacion_cpcfa"), tex_num_cheque.getValue() + "", cal_fecha_cheque.getFecha());
                            cxp.generarTransaccionCompra(tab_tabla1, tot_retenido, bancos.getTab_cab_libro_banco(), dou_total_anticipo);
                        } else {
                            cxp.generarTransaccionCompra(tab_tabla1, tot_retenido, null, dou_total_anticipo);
                        }
                    } catch (Exception e) {
                    }
                } else {
                    if (tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_anticipo"))) {
                        cxp.generarTransaccionCompra(tab_tabla1, tot_retenido, dou_total_anticipo);
                    } else {
                        cxp.generarTransaccionCompra(tab_tabla1, tot_retenido, true);
                    }
                }

                //cambio los estados de la retencion y el libro bancos a  estado anulado  
                String anterior_ide_cnccc = tab_tabla1.getValor("ide_cnccc");
                if (bandera_asiento_reversa == 1) {
                    if (Integer.parseInt(num_dias) == 0) {
                        utilitario.getConexion().agregarSqlPantalla("UPDATE tes_cab_libr_banc SET ide_teelb=1 where ide_cnccc=" + tab_tabla1.getValor("ide_cnccc") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                    }
                    utilitario.getConexion().agregarSqlPantalla("update con_cabece_retenc set ide_cnere=1 where ide_cncre=" + tab_tabla1.getValor("ide_cncre") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                }
                if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                    if (bandera_asiento_reversa == 1) {
                        bandera_asiento_reversa = 0;
                        utilitario.agregarMensajeInfo("Se ha generado un asiento de reversa", "");
                        conta.limpiar();
                        conta.reversar(anterior_ide_cnccc, "pruebas de reversar", conta);
                        String ide_cnccc_nuevo = conta.getTab_cabecera().getValor("ide_cnccc");
                        cls_inventario inv = new cls_inventario();
                        inv.reversar_menos(ide_cnccc_nuevo, anterior_ide_cnccc, "prueba inve reversar");
                        utilitario.getConexion().guardarPantalla();
                    }
                }
                bot_comp_retencion.setDisabled(false);
                utilitario.addUpdate("via_comprobante_conta,pac_acordion,tab_tabla1,tab_tabla2,tab_tabla3,bot_comp_retencion");

                if (boo_tip_tran_cheque) {
                    cls_bancos banco = new cls_bancos();
                    System.out.println("ide_cnccc_cheque " + ide_cnccc);
                    TablaGenerica tab_lib_banc = utilitario.consultar("select * from tes_cab_libr_banc where ide_cnccc=" + ide_cnccc);
                    parametro = new HashMap();
                    parametro.put("beneficiario", tab_lib_banc.getValor("beneficiari_teclb") + "");
                    parametro.put("monto", tab_lib_banc.getValor("valor_teclb") + "");

                    try {
                        parametro.put("anio", utilitario.getAnio(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                        parametro.put("mes", utilitario.getMes(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                        parametro.put("dia", utilitario.getDia(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                    } catch (Exception e) {
                        parametro.put("anio", "");
                        parametro.put("mes", "");
                        parametro.put("dia", "");

                    }
                    parametro.put("monto_letras", banco.agregarAsteriscosCheque(utilitario.getLetrasDolarNumero(tab_lib_banc.getValor("valor_teclb"))));
                    parametro.put("ide_cnccc", Long.parseLong(tab_lib_banc.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    parametro.put("p_num_cheque", tab_lib_banc.getValor("numero_teclb") + "");
                    parametro.put("p_num_trans", tab_lib_banc.getValor("ide_teclb") + "");
                    List lis_geper = utilitario.getConexion().consultar("select identificac_geper from gen_persona where ide_geper=(select ide_geper from con_cab_comp_cont where ide_cnccc =" + tab_lib_banc.getValor("ide_cnccc") + ")");
                    if (lis_geper.size() > 0) {
                        parametro.put("p_identificacion", lis_geper.get(0) + "");
                    } else {
                        parametro.put("p_identificacion", "");
                    }
                    vp.setVisualizarPDF("rep_bancos/rep_cheque.jasper", parametro);
                    vp.dibujar();
                } else {
                    parametro = new HashMap();
                    parametro.put("ide_cnccc", Long.parseLong(ide_cnccc));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    vp.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametro);
                    vp.dibujar();
                }
                tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
                tab_tabla3.ejecutarSql();
                utilitario.addUpdate("tab_tabla3,vp");
                cargar_saldo();
            }
        }
    }

    public boolean realizarComprobanteInventario() {
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (in.es_bien(tab_tabla2.getValor(i, "ide_inarti"))) {
                return true;
            }
        }
        return false;
    }

    public boolean haceKardex(String ide_inarti) {
        List lis_articulo = utilitario.getConexion().consultar("select hace_kardex_inarti from inv_articulo where ide_inarti=" + ide_inarti);
        if (lis_articulo.size() > 0) {
            if (lis_articulo.get(0) != null) {
                if (lis_articulo.get(0).equals("true")) {
                    return true;
                }
            }
        }
        return false;
    }
    cls_inventario in = new cls_inventario();

    public void generarComprobanteInventario(String ide_cnccc) {
        //Cabecera 
        Tabla tab_cab_comp_inv = new Tabla();
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
        tab_cab_comp_inv.setCondicion("ide_incci=-1");
        tab_cab_comp_inv.ejecutarSql();
        tab_cab_comp_inv.insertar();
        tab_cab_comp_inv.setValor("ide_inepi", p_estado_normal_inventario);  /////variable estado normal de inventario
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_cab_comp_inv.setValor("ide_inbod", "1");   ///variable para bodega por defecto
        tab_cab_comp_inv.setValor("numero_incci", in.generarSecuencialComprobanteInventario(p_tipo_transaccion_inv_compra));  /// calcular numero de comprobante de inventario
        tab_cab_comp_inv.setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_tabla1.getValor("fecha_emisi_cpcfa"));
        tab_cab_comp_inv.setValor("observacion_incci", tab_tabla1.getValor("observacion_cpcfa"));
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
        tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion_inv_compra);   ////variable titpo transaccion compra 
        tab_cab_comp_inv.setValor("ide_cnccc", ide_cnccc);
        //Detalles

        Tabla tab_det_comp_inv = new Tabla();
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
        tab_det_comp_inv.setCondicion("ide_indci=-1");
        tab_det_comp_inv.ejecutarSql();
        tab_cab_comp_inv.guardar();

        double porce_descuento = 0;
        if (tab_tabla1.getValor("porcen_desc_cpcfa") != null) {
            try {
                porce_descuento = Double.parseDouble(tab_tabla1.getValor("porcen_desc_cpcfa"));
            } catch (Exception e) {
            }

        }

        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (in.es_bien(tab_tabla2.getValor(i, "ide_inarti"))) {
//                if (haceKardex(tab_tabla2.getValor(i, "ide_inarti"))) {
                if (in.esTipoCombo(tab_tabla2.getValor(i, "ide_inarti")) == false) {
                    tab_det_comp_inv.insertar();
                    if (tab_tabla2.getValor(i, "iva_inarti_cpdfa") != null
                            && !tab_tabla2.getValor(i, "iva_inarti_cpdfa").isEmpty()
                            && tab_tabla2.getValor(i, "iva_inarti_cpdfa").equals("1")) {
//                    System.out.println(in.aplicaIva(tab_tabla2.getValor(i, "ide_inarti")) + "    " + tab_tabla2.getValor(i, "ide_inarti"));
//                    if (in.aplicaIva(tab_tabla2.getValor(i, "ide_inarti"))) {
                        try {
                            double precio = Double.parseDouble(tab_tabla2.getValor(i, "precio_cpdfa")) + (Double.parseDouble(tab_tabla2.getValor(i, "precio_cpdfa")) * p_porcentaje_iva);
                            if (porce_descuento > 0) {
                                ///aplico &  descuento                                
                                precio = Double.parseDouble(tab_tabla2.getValor(i, "precio_cpdfa"));
                                double valor_descuento = precio * (porce_descuento / 100);
                                precio = precio - valor_descuento;
                                precio = precio + (precio * p_porcentaje_iva);
                            }
                            double valor = (Double.parseDouble(tab_tabla2.getValor(i, "cantidad_cpdfa")) * precio);
                            tab_det_comp_inv.setValor("precio_indci", precio + "");
                            tab_det_comp_inv.setValor("valor_indci", valor + "");
                            String precio_promedio = in.getPrecioPromedioTransaccionPositiva(tab_tabla2.getValor(i, "ide_inarti"), "1", valor, Double.parseDouble(tab_tabla2.getValor(i, "cantidad_cpdfa")));
                            if (precio_promedio != null) {
                                tab_det_comp_inv.setValor("precio_promedio_indci", precio_promedio);
                            }
                        } catch (Exception e) {
                        }
                    } else {

                        double precio = Double.parseDouble(tab_tabla2.getValor(i, "precio_cpdfa"));
                        if (porce_descuento > 0) {
                            ///aplico &  descuento                                
                            precio = Double.parseDouble(tab_tabla2.getValor(i, "precio_cpdfa"));
                            precio = precio - (precio * (porce_descuento / 100));
                        }
                        double valor = Double.parseDouble(tab_tabla2.getValor(i, "cantidad_cpdfa")) * precio;

                        tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(precio));
                        tab_det_comp_inv.setValor("valor_indci", utilitario.getFormatoNumero(valor));
                        String precio_promedio = in.getPrecioPromedioTransaccionPositiva(tab_tabla2.getValor(i, "ide_inarti"), "1", Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa")), Double.parseDouble(tab_tabla2.getValor(i, "cantidad_cpdfa")));
                        if (precio_promedio != null) {
                            tab_det_comp_inv.setValor("precio_promedio_indci", precio_promedio);
                        }
                    }
                    tab_det_comp_inv.setValor("ide_inarti", tab_tabla2.getValor(i, "ide_inarti"));
                    tab_det_comp_inv.setValor("ide_cpcfa", tab_tabla1.getValor("ide_cpcfa"));
                    tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));
                    tab_det_comp_inv.setValor("cantidad_indci", tab_tabla2.getValor(i, "cantidad_cpdfa"));
                    tab_det_comp_inv.setValor("observacion_indci", tab_tabla2.getValor(i, "observacion_cpdfa"));
                }
                //}
            }
        }
        tab_det_comp_inv.guardar();
    }

    public String obtener_fecha_vencimiento() {
        List dias_sql = utilitario.getConexion().consultar("SELECT dias_cndfp from con_deta_forma_pago where ide_cndfp=" + tab_tabla1.getValor("ide_cndfp"));
        String fecha_venci = "";
        int dias = 0;
        if (dias_sql.get(0) != null) {
            dias = Integer.parseInt(dias_sql.get(0).toString());
        }
        fecha_venci = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_emisi_cpcfa")), dias));
        return fecha_venci;
    }

    @Override
    public void abrirRangoFecha() {
        //Se ejecuta cuando da click en el boton Calendario de  la Barra    
        sec_calendario.dibujar();
    }

    public void filtrar() {
        sec_calendario.cerrar();

        aut_num_factura.setAutoCompletar("select cf.ide_cpcfa,cf.numero_cpcfa,gp.nom_geper from cxp_cabece_factur cf left join gen_persona gp on cf.ide_geper=gp.ide_geper where cf.ide_empr=" + utilitario.getVariable("ide_empr") + " and cf.ide_sucu=" + utilitario.getVariable("ide_sucu")
                + " and cf.fecha_emisi_cpcfa between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "'");
        tab_tabla1.setCondicion("fecha_emisi_cpcfa between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "'");
        tab_tabla1.ejecutarSql();
        tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        tab_tabla3.setSql("SELECT ide_cpdtr,fecha_trans_cpdtr,numero_pago_cpdtr,valor_cpdtr,docum_relac_cpdtr,ide_cpcfa FROM cxp_detall_transa where numero_pago_cpdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cpcfa=" + tab_tabla1.getValor("ide_cpcfa"));
        tab_tabla3.ejecutarSql();
        utilitario.addUpdate("aut_num_factura");
        cargar_saldo();
    }

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public VisualizarPDF getVp() {
        return vp;
    }

    public void setVp(VisualizarPDF vp) {
        this.vp = vp;
    }

    public Confirmar getCon_guardar() {
        return con_guardar;
    }

    public void setCon_guardar(Confirmar con_guardar) {
        this.con_guardar = con_guardar;
    }

    public Confirmar getCon_confirma_rete() {
        return con_confirma_rete;
    }

    public void setCon_confirma_rete(Confirmar con_confirma_rete) {
        this.con_confirma_rete = con_confirma_rete;
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

    public SeleccionCalendario getSel_cal() {
        return sel_cal;
    }

    public void setSel_cal(SeleccionCalendario sel_cal) {
        this.sel_cal = sel_cal;
    }

    public VistaAsiento getVia_comprobante_conta() {
        return via_comprobante_conta;
    }

    public void setVia_comprobante_conta(VistaAsiento via_comprobante_conta) {
        this.via_comprobante_conta = via_comprobante_conta;
    }

    public Dialogo getDia_datos_provee() {
        return dia_datos_provee;
    }

    public void setDia_datos_provee(Dialogo dia_datos_provee) {
        this.dia_datos_provee = dia_datos_provee;
    }

    public Tabla getTab_tabla9() {
        return tab_tabla9;
    }

    public void setTab_tabla9(Tabla tab_tabla9) {
        this.tab_tabla9 = tab_tabla9;
    }

    public Dialogo getDia_datos_producto() {
        return dia_datos_producto;
    }

    public void setDia_datos_producto(Dialogo dia_datos_producto) {
        this.dia_datos_producto = dia_datos_producto;
    }

    public Tabla getTab_tabla10() {
        return tab_tabla10;
    }

    public void setTab_tabla10(Tabla tab_tabla10) {
        this.tab_tabla10 = tab_tabla10;
    }

    public Dialogo getDia_banco() {
        return dia_banco;
    }

    public void setDia_banco(Dialogo dia_banco) {
        this.dia_banco = dia_banco;
    }

    public Dialogo getDia_datos_factura() {
        return dia_datos_factura;
    }

    public void setDia_datos_factura(Dialogo dia_datos_factura) {
        this.dia_datos_factura = dia_datos_factura;
    }

    public Tabla getTab_tabla_df() {
        return tab_tabla_df;
    }

    public void setTab_tabla_df(Tabla tab_tabla_df) {
        this.tab_tabla_df = tab_tabla_df;
    }

    public Dialogo getDia_datos_liquidacion_compra() {
        return dia_datos_liquidacion_compra;
    }

    public void setDia_datos_liquidacion_compra(Dialogo dia_datos_liquidacion_compra) {
        this.dia_datos_liquidacion_compra = dia_datos_liquidacion_compra;
    }

    public AutoCompletar getAut_num_factura() {
        return aut_num_factura;
    }

    public void setAut_num_factura(AutoCompletar aut_num_factura) {
        this.aut_num_factura = aut_num_factura;
    }

    public VistaRetencion getVir_comprobante_retencion() {
        return vir_comprobante_retencion;
    }

    public void setVir_comprobante_retencion(VistaRetencion vir_comprobante_retencion) {
        this.vir_comprobante_retencion = vir_comprobante_retencion;
    }

    public Dialogo getDia_ide_anticipo() {
        return dia_ide_anticipo;
    }

    public void setDia_ide_anticipo(Dialogo dia_ide_anticipo) {
        this.dia_ide_anticipo = dia_ide_anticipo;
    }

    public Tabla getTab_anticipos() {
        return tab_anticipos;
    }

    public void setTab_anticipos(Tabla tab_anticipos) {
        this.tab_anticipos = tab_anticipos;
    }

    public Tabla getTab_datos_comprobante_rembolso() {
        return tab_datos_comprobante_rembolso;
    }

    public void setTab_datos_comprobante_rembolso(Tabla tab_datos_comprobante_rembolso) {
        this.tab_datos_comprobante_rembolso = tab_datos_comprobante_rembolso;
    }
}
