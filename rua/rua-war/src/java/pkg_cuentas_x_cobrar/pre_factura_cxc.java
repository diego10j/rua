/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
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
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
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
public class pre_factura_cxc extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Division div_division = new Division();
    private Etiqueta eti_total_factura_cxc = new Etiqueta();
    private String cliente_actual;
    private double base_no_objeto = 0;
    private double base_tarifa0 = 0;
    private double base_grabada = 0;
    private double valor_iva = 0;
    private Etiqueta eti_saldo = new Etiqueta();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private SeleccionCalendario sel_cal = new SeleccionCalendario();
    //******Parametros********
    private String p_activos_fijos = utilitario.getVariable("p_inv_articulo_activo_fijo");
    private String p_tipo_transaccion_inv_venta = utilitario.getVariable("p_inv_tipo_transaccion_venta");
    private String p_estado_normal_inv = utilitario.getVariable("p_inv_estado_normal");
    private String p_estado_comprobante_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private String p_tipo_comprobante_diario = utilitario.getVariable("p_con_tipo_comprobante_diario");
    private String p_modulo = "3";
    private String p_tipo_comprobante_ingreso = utilitario.getVariable("p_con_tipo_comprobante_ingreso");
    private String p_bienes = utilitario.getVariable("p_inv_articulo_bien");
    private String p_servicios = utilitario.getVariable("p_inv_articulo_servicio");
    private String p_honorarios_profes = utilitario.getVariable("p_inv_articulo_honorarios");
    private String p_iva30 = utilitario.getVariable("p_con_impuesto_iva30");
    private String p_iva70 = utilitario.getVariable("p_con_impuesto_iva70");
    private String p_iva100 = utilitario.getVariable("p_con_impuesto_iva100");
    List porcen_iva_sql = utilitario.getConexion().consultar("select porcentaje_cnpim from con_porcen_impues where ide_cnpim=" + utilitario.getVariable("p_con_porcentaje_imp_iva"));
    private double p_porcentaje_iva = Double.parseDouble(porcen_iva_sql.get(0).toString());
    //******* Dialogo Comprobante Retencion
    private Dialogo dia_comprobante_rete = new Dialogo();
    private Etiqueta eti_total_retencion = new Etiqueta();
    private double iva30 = 0;
    private double iva70 = 0;
    private double iva100 = 0;
    private List l_casillero = new ArrayList();
    private List l_suma = new ArrayList();
    //  dialogo para pagos al contado
    private Dialogo dia_banco = new Dialogo();
    private Combo com_banco = new Combo();
    private Combo com_cuenta_banco = new Combo();
    private Combo com_serie_factura = new Combo();
    private String banco_actual = "-1";
    private String cuenta_bancaria_asiento = "";
    private Texto tex_num_cheque = new Texto();
    private Texto tex_monto_cheque = new Texto();
    private Etiqueta eti_banc = new Etiqueta();
    private Etiqueta eti_cue_banc = new Etiqueta();
    private Etiqueta eti_num_cheque = new Etiqueta();
    private Etiqueta eti_monto_cheque = new Etiqueta();
    private String ide_cab_libro_banco;
    //Datos del Cliente
    private Boton bot_datos_cliente = new Boton();
    private Dialogo dia_datos_clie = new Dialogo();
    private Tabla tab_tabla9 = new Tabla(); // contiene la tabla de clientes
    private VistaAsiento via_comprobante_conta = new VistaAsiento();
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
// pata boton datos producto
    private Dialogo dia_datos_producto = new Dialogo();
    private Boton bot_datos_producto = new Boton();
    private Tabla tab_tabla10 = new Tabla(); // contiene la tabla de articulos
    //para el boton anular
    private Boton bot_anular = new Boton();
    private Check chk_asiento_costos = new Check();
    private Etiqueta eti_hace_asiento_costos = new Etiqueta();
    private AutoCompletar aut_transferencia_cuentas = new AutoCompletar();
    private Etiqueta eti_transferencia_cuentas = new Etiqueta();
    cls_bancos bancos = new cls_bancos();
    //
    private Boton bot_genera_asiento_costos = new Boton();
    private Check che_solo_guardar = new Check();
    private VistaRetencion vir_comprobante_retencion = new VistaRetencion();

    public Tabla getTab_tabla9() {
        return tab_tabla9;
    }

    public void setTab_tabla9(Tabla tab_tabla9) {
        this.tab_tabla9 = tab_tabla9;
    }

    public Dialogo getDia_datos_clie() {
        return dia_datos_clie;
    }

    public void setDia_datos_clie(Dialogo dia_datos_clie) {
        this.dia_datos_clie = dia_datos_clie;
    }

    public pre_factura_cxc() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {

            bar_botones.getBot_inicio().setMetodo("inicio");
            bar_botones.getBot_fin().setMetodo("fin");
            bar_botones.getBot_siguiente().setMetodo("siguiente");
            bar_botones.getBot_atras().setMetodo("atras");
            bar_botones.agregarReporte();

            // boton anular
            bot_anular.setId("bot_anular");
            bot_anular.setValue("Anular");
            bot_anular.setMetodo("anularFactura");
            bar_botones.agregarBoton(bot_anular);

            //boton datos clientes
            bot_datos_cliente.setId("bot_datos_cliente");
            bot_datos_cliente.setValue("Datos Cliente");
            bot_datos_cliente.setMetodo("abrirDatosClientes");
            bar_botones.agregarBoton(bot_datos_cliente);

            bot_datos_producto.setId("bot_datos_producto");
            bot_datos_producto.setValue("Datos Producto");
            bot_datos_producto.setMetodo("abrirDatosProducto");
            bar_botones.agregarBoton(bot_datos_producto);

            bot_genera_asiento_costos.setId("bot_genera_asiento_costos");
            bot_genera_asiento_costos.setValue("G. Asiento Costos");
            bot_genera_asiento_costos.setMetodo("validarAsientoCostos");
            bar_botones.agregarBoton(bot_genera_asiento_costos);

            che_solo_guardar.setId("che_solo_guardar");
            che_solo_guardar.setValue(false);
            che_solo_guardar.setDisabled(true);
            bar_botones.agregarComponente(new Etiqueta("Solo Guardar"));
            bar_botones.agregarComponente(che_solo_guardar);

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sel_rep.setId("sel_rep");

            sel_cal.setId("sel_cal");
            sel_cal.getBot_aceptar().setMetodo("aceptarReporte");

            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setTabla("cxc_cabece_factura", "ide_cccfa", 1);
            tab_tabla1.onSelect("seleccionar_tabla1");
            tab_tabla1.getColumna("ide_cnccc").setLectura(true);
            tab_tabla1.getColumna("ide_cncre").setLectura(true);
            tab_tabla1.getColumna("ide_vgven").setCombo("ven_vendedor", "ide_vgven", "nombre_vgven", "");
            tab_tabla1.getColumna("ide_cntdo").setCombo("con_tipo_document", "ide_cntdo", "nombre_cntdo", "");
            tab_tabla1.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));
            tab_tabla1.getColumna("ide_cntdo").setLectura(true);
            tab_tabla1.getColumna("ide_ccefa").setCombo("cxc_estado_factura", "ide_ccefa", "nombre_ccefa", "");
            tab_tabla1.getColumna("ide_ccefa").setValorDefecto(utilitario.getVariable("p_cxc_estado_factura_normal"));
            tab_tabla1.getColumna("ide_ccefa").setLectura(true);
            tab_tabla1.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
            tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_tabla1.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
            tab_tabla1.getColumna("ide_usua").setLectura(true);
            tab_tabla1.getColumna("fecha_trans_cccfa").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("fecha_trans_cccfa").setLectura(true);
            tab_tabla1.getColumna("fecha_emisi_cccfa").setValorDefecto(utilitario.getFechaActual());
            tab_tabla1.getColumna("ide_ccdaf").setCombo("cxc_datos_fac", "ide_ccdaf", "serie_ccdaf, autorizacion_ccdaf,observacion_ccdaf", "");
            tab_tabla1.getColumna("ide_ccdaf").setVisible(false);
            tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
            tab_tabla1.getColumna("ide_geper").setAutoCompletar();
            tab_tabla1.getColumna("ide_geper").setMetodoChange("cargar_direccion");
            tab_tabla1.getColumna("pagado_cccfa").setValorDefecto("false");
            tab_tabla1.getColumna("pagado_cccfa").setVisible(false);
            tab_tabla1.getColumna("total_cccfa").setEtiqueta();
            tab_tabla1.getColumna("total_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("total_cccfa").setValorDefecto("0");
            tab_tabla1.getColumna("secuencial_cccfa").setEstilo("font-size: 15px;font-weight: bold");
            tab_tabla1.getColumna("secuencial_cccfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
            tab_tabla1.getColumna("secuencial_cccfa").setMascara("9999999");
            tab_tabla1.getColumna("base_grabada_cccfa").setEtiqueta();
            tab_tabla1.getColumna("base_grabada_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("base_grabada_cccfa").setValorDefecto("0");
            tab_tabla1.getColumna("valor_iva_cccfa").setEtiqueta();
            tab_tabla1.getColumna("valor_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("valor_iva_cccfa").setValorDefecto("0");
            tab_tabla1.getColumna("base_no_objeto_iva_cccfa").setEtiqueta();
            tab_tabla1.getColumna("base_no_objeto_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("base_no_objeto_iva_cccfa").setValorDefecto("0");
            tab_tabla1.getColumna("base_tarifa0_cccfa").setEtiqueta();
            tab_tabla1.getColumna("base_tarifa0_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
            tab_tabla1.getColumna("base_tarifa0_cccfa").setValorDefecto("0");
            tab_tabla1.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp!=" + utilitario.getVariable("p_con_for_pag_reembolso_caja"));
            tab_tabla1.getColumna("ide_cndfp").setPermitirNullCombo(false);
            tab_tabla1.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
            tab_tabla1.getColumna("solo_guardar_cccfa").setValorDefecto("false");
            tab_tabla1.getColumna("solo_guardar_cccfa").setVisible(false);
            tab_tabla1.getColumna("ide_vgven").setVisible(false);
            tab_tabla1.getColumna("ide_geubi").setVisible(false);
            tab_tabla1.getColumna("ide_usua").setVisible(false);
            tab_tabla1.setTipoFormulario(true);
            //tab_tabla1.setRecuperarLectura(true);
            tab_tabla1.getGrid().setColumns(6);
            tab_tabla1.agregarRelacion(tab_tabla2);
            tab_tabla1.agregarRelacion(tab_tabla3);
            tab_tabla1.setCampoOrden("ide_cccfa desc");
            tab_tabla1.setCondicion("ide_cccfa=-1");
            tab_tabla1.setCondicionSucursal(true);

            tab_tabla1.dibujar();
            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setPanelTabla(tab_tabla1);

            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setTabla("cxc_deta_factura", "ide_ccdfa", 2);
            tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
            tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
            tab_tabla2.getColumna("ide_inarti").setMetodoChange("obtener_total_detalle_factura_cxc1");
            tab_tabla2.getColumna("cantidad_ccdfa").setMetodoChange("obtener_total_detalle_factura_cxc");
            tab_tabla2.getColumna("precio_ccdfa").setMetodoChange("obtener_total_detalle_factura_cxc");
            tab_tabla2.getColumna("total_ccdfa").setEtiqueta();
            tab_tabla2.getColumna("total_ccdfa").setEstilo("font-size:13px;font-weight: bold;");
            tab_tabla2.getColumna("precio_promedio_ccdfa").setLectura(true);
            //tab_tabla2.setRecuperarLectura(true);
            tab_tabla2.dibujar();
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla2);

            tab_tabla3.setId("tab_tabla3");
//            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=-1");
            tab_tabla3.setCampoPrimaria("ide_ccdtr");
            tab_tabla3.setCampoForanea("ide_cccfa");
            tab_tabla3.setNumeroTabla(3);
            tab_tabla3.setLectura(true);
            tab_tabla3.dibujar();

            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_tabla3);

// ******** PARA VISTA ASIENTO DE COMPROBANTE DE CONTABILIDAD
            chk_asiento_costos.setId("chk_asiento_costos");
            chk_asiento_costos.setValue("true");
            Grid gri_check = new Grid();
            gri_check.setColumns(2);
            eti_hace_asiento_costos.setValue("Hace Asiento de Costos");
            eti_hace_asiento_costos.setStyle("font-size: 14px;font-weight: bold;text-decoration: underline");
            gri_check.getChildren().add(eti_hace_asiento_costos);
            gri_check.getChildren().add(chk_asiento_costos);
            via_comprobante_conta.setId("via_comprobante_conta");
            via_comprobante_conta.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
            via_comprobante_conta.getBot_cancelar().setMetodo("cancelarDialogo");
            via_comprobante_conta.getGri_cuerpo_vasiento().setHeader(gri_check);
            via_comprobante_conta.setDynamic(false);

            agregarComponente(via_comprobante_conta);

            ///***************Combo para filtar a  la serie de la factura
            Etiqueta eti_serie_factura = new Etiqueta();
            eti_serie_factura.setValue("Serie de Factura");
            bar_botones.agregarComponente(eti_serie_factura);
            com_serie_factura.setId("com_serie_factura");
            bar_botones.agregarComponente(com_serie_factura);
            com_serie_factura.setCombo(tab_tabla1.getColumna("ide_ccdaf").getListaCombo());
            com_serie_factura.setMetodo("seleccionarSerieFactura");//"tab_tabla1,tab_tabla2,tab_tabla3"

//**************  dialogo pagos al contado ************
            tex_num_cheque.setId("tex_num_cheque");
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco.setMetodo("cargar_cuentas");
            com_cuenta_banco.setId("com_cuenta_banco");

            aut_transferencia_cuentas.setId("aut_transferencia_cuentas");
            aut_transferencia_cuentas.setAutoCompletar("select ide_cndpc,codig_recur_cndpc,nombre_cndpc from con_det_plan_cuen where ide_empr=" + utilitario.getVariable("ide_empr") + " and nivel_cndpc = 'HIJO'");
            eti_transferencia_cuentas.setValue("Cuenta Casa: ");

            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Cheque: ");
            eti_monto_cheque.setValue("Monto Cheque: ");
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

            grid_bancos.getChildren().add(eti_transferencia_cuentas);
            grid_bancos.getChildren().add(aut_transferencia_cuentas);

            dia_banco.setId("dia_banco");
            dia_banco.setTitle("Forma de Pago");
            dia_banco.setWidth("30%");
            dia_banco.setHeight("30%");
            dia_banco.setDialogo(grid_bancos);
            grid_bancos.setStyle("width:" + (dia_banco.getAnchoPanel() - 5) + "px;height:" + dia_banco.getAltoPanel() + "px;overflow: auto;display: block;");
            dia_banco.getBot_aceptar().setMetodo("generarComprobanteContabilidadContado");
            dia_banco.getBot_cancelar().setMetodo("cancelarDialogo");
            dia_banco.setDynamic(false);

            agregarComponente(dia_banco);

            Espacio esp = new Espacio();
            esp.setWidth("5");
            esp.setHeight("1");

            eti_total_factura_cxc.setId("eti_total_factura_cxc");
            eti_total_factura_cxc.setStyle("font-size: 16px;color: red;font-weight: bold");
            eti_total_factura_cxc.setValue("TOTAL: 0");

            eti_saldo.setId("eti_saldo");
            eti_saldo.setValue("Saldo: ");
            eti_saldo.setStyle("font-size:16px;font-weight: bold;");

            Grid grid_matriz = new Grid();
            grid_matriz.setId("grid_matriz");
            grid_matriz.setStyle("width: 98%;text-align: right; padding-right: 10%;float: right;overflow: hidden;");
            grid_matriz.getChildren().add(esp);
            grid_matriz.getChildren().add(eti_saldo);

//***************  dialogo datos producto*****************************
            tab_tabla10.setId("tab_tabla10");
            tab_tabla10.setTabla("inv_articulo", "ide_inarti", 10);
            tab_tabla10.setCondicion("ide_inarti=-1");
            tab_tabla10.getColumna("ide_infab").setVisible(false);
            tab_tabla10.getColumna("ide_inmar").setVisible(false);
            tab_tabla10.getColumna("ide_inuni").setVisible(false);
            tab_tabla10.getColumna("ide_intpr").setVisible(false);
            tab_tabla10.getColumna("ide_inepr").setVisible(false);
            tab_tabla10.getColumna("nivel_inarti").setVisible(false);
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
            tab_tabla10.getColumna("iva_inarti").setRadio(lista, "1");
            tab_tabla10.getColumna("iva_inarti").setRadioVertical(true);
            tab_tabla10.getColumna("inv_ide_inarti").setVisible(false);
            tab_tabla10.setTipoFormulario(true);
            tab_tabla10.getGrid().setColumns(2);
            tab_tabla10.setMostrarNumeroRegistros(false);
            utilitario.buscarNombresVisuales(tab_tabla10);
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

            //***************  dialogo datos cliente *****************************
            tab_tabla9.setId("tab_tabla9");
            tab_tabla9.setTabla("gen_persona", "ide_geper", 9);
            tab_tabla9.setCondicion("ide_geper=-1");
            tab_tabla9.getColumna("es_cliente_geper").setValorDefecto("true");
            tab_tabla9.getColumna("es_cliente_geper").setVisible(false);
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
            tab_tabla9.dibujar();

            Grid grid_datos_clie = new Grid();
            grid_datos_clie.getChildren().add(tab_tabla9);

            dia_datos_clie.setId("dia_datos_clie");
            dia_datos_clie.setTitle("Datos Clientes");

            dia_datos_clie.setWidth("70%");
            dia_datos_clie.setHeight("70%");
            dia_datos_clie.setDialogo(grid_datos_clie);
            grid_datos_clie.setStyle("width:" + (dia_datos_clie.getAnchoPanel() - 5) + "px;height:" + dia_datos_clie.getAltoPanel() + "px;overflow: auto;display: block;");
            dia_datos_clie.getBot_aceptar().setMetodo("aceptar_datos_proveedor");
            dia_datos_clie.getBot_cancelar().setMetodo("cancelarDialogo");

            agregarComponente(dia_datos_clie);

            Division div_fot = new Division();
            div_fot.setFooter(pat_panel3, eti_saldo, "80%");

            Division div1 = new Division();
            div1.setId("div1");
            div1.dividir2(pat_panel2, div_fot, "65%", "V");

            div_division.setId("div_division");
            div_division.dividir2(pat_panel1, div1, "55%", "H");

            agregarComponente(div_division);
            agregarComponente(rep_reporte);
            agregarComponente(sel_rep);
            agregarComponente(sel_cal);

// ************* PARA EL VISUALIZADOR DE COMPROBANTE DE RETENCION
            vir_comprobante_retencion.setId("vir_comprobante_retencion");
            vir_comprobante_retencion.getBot_aceptar().setMetodo("aceptarComprobanteRetencion");
//        vir_comprobante_retencion.getBot_cancelar().setMetodo("cancelarDialogo");
            vir_comprobante_retencion.setDynamic(false);
            agregarComponente(vir_comprobante_retencion);
//**********************************************************************************************
            calcula_total_detalles_cxc();
            cargar_saldo();
        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }

    public VistaRetencion getVir_comprobante_retencion() {
        return vir_comprobante_retencion;
    }

    public void setVir_comprobante_retencion(VistaRetencion vir_comprobante_retencion) {
        this.vir_comprobante_retencion = vir_comprobante_retencion;
    }

    public void validarAsientoCostos() {
        if (tab_tabla1.getTotalFilas() > 0) {
            TablaGenerica tab_cab_comp_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_incci in ( "
                    + "select ide_incci from inv_det_comp_inve where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa") + ")");
            if (tab_cab_comp_inv.getTotalFilas() > 0) {
                if (tab_cab_comp_inv.getValor(0, "ide_cnccc") == null || tab_cab_comp_inv.getValor(0, "ide_cnccc").isEmpty()) {
                    generarAsientoCostos();
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el Asiento de Costos", "La factura seleccionada ya tiene generado un asiento de costos");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede generar el Asiento de Costos", "Aun no se ha registrado el comprobante de inventario");
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede generar el Asiento de Costos", "No se encuentran datos para realizar el asiento de costos");
        }
    }

    public void anularFacturaconReversa() {

        if (tab_tabla1.getTotalFilas() > 0) {
            if (tab_tabla3.getTotalFilas() == 0) {
                conta.limpiar();
                TablaGenerica tab_cxc = utilitario.consultar("SELECT * from cxc_cabece_transa where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
                conta.reversar(tab_tabla1.getValor("ide_cnccc"), "reversar pruebas", conta);
                String ide_cnccc_nuevo = conta.getTab_cabecera().getValor("ide_cnccc");
                if (ide_cnccc_nuevo != null && !ide_cnccc_nuevo.isEmpty()) {
                    if (tab_cxc.getTotalFilas() > 0) {
                        utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_anulada") + " where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
                        cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                        //cxc.reversar(ide_cnccc_nuevo, tab_cxc.getValor(0, "ide_cccfa"), "Reversa CxP de fact. num:" + tab_tabla1.getValor(0, "secuencial_cccfa") + " y asiento num:" + tab_tabla1.getValor("ide_cnccc"));
                        TablaGenerica tab_inv_cab_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_incci in ( "
                                + "select ide_incci from inv_det_comp_inve where ide_cccfa=" + tab_cxc.getValor(0, "ide_cccfa") + " GROUP BY ide_incci)");
                        if (tab_inv_cab_inv.getTotalFilas() > 0) {
                            cls_inventario inv = new cls_inventario();
                            inv.reversar_mas(ide_cnccc_nuevo, tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Reversa de comprobante num:" + tab_tabla1.getValor("ide_cnccc"));
                            utilitario.getConexion().guardarPantalla();
                            conta.limpiar();
                            conta.reversar(tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Asiento de reversa de la transaccion num: " + tab_inv_cab_inv.getValor(0, "ide_cnccc"), conta);
                            utilitario.getConexion().guardarPantalla();
                            tab_tabla1.setFilaActual(tab_tabla1.getValor("ide_cccfa"));
                            tab_tabla1.ejecutarSql();
                        } else {
                            utilitario.getConexion().guardarPantalla();
                        }
                    } else {
                        tab_tabla1.setFilaActual(tab_tabla1.getValor("ide_cccfa"));
                        tab_tabla1.ejecutarSql();
                        utilitario.getConexion().guardarPantalla();
                    }
                }

            } else {
                utilitario.agregarMensajeInfo("No se puede Anular la Factura", "La factura ya tiene pagos realizados");
            }
        }
    }

    public void anularFactura() {

        if (tab_tabla1.getTotalFilas() > 0) {
            if (!tab_tabla1.getValor("ide_ccefa").equals(utilitario.getVariable("p_cxc_estado_factura_anulada"))) {
                TablaGenerica tab_cxc_cab_factura = utilitario.consultar("select * from cxc_cabece_factura where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
                if (tab_cxc_cab_factura.getTotalFilas() > 0) {
                    utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_anulada") + " where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));

                    String ide_cnccc = tab_cxc_cab_factura.getValor(0, "ide_cnccc");
                    String ide_cncre = tab_cxc_cab_factura.getValor(0, "ide_cncre");
                    // anulo el comprobante de contabilidad si existe
                    if (ide_cnccc != null && !ide_cnccc.isEmpty()) {
                        conta.anular(ide_cnccc);
                    }
                    // anulo el comprobante de retencion si existe
                    if (ide_cncre != null && !ide_cncre.isEmpty()) {
                        cls_retenciones ret = new cls_retenciones();
                        ret.anularComprobanteRetencion(ide_cncre);
                    }
                    // reverso las transacciones cxc realizadas con la factura seleccionada
                    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                    cxc.reversar(tab_tabla1.getValor("ide_cccfa"), "Reversa CxP de fact. num:" + tab_tabla1.getValor(0, "secuencial_cccfa") + " y asiento num:" + tab_tabla1.getValor("ide_cnccc"));
                    // reverso las transacciones bancarias relacionadas con esta factura
                    TablaGenerica tab_cxc_detalle = utilitario.consultar("select * from cxc_detall_transa where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
                    if (tab_cxc_detalle.getTotalFilas() > 0) {
                        cls_bancos banco = new cls_bancos();
                        banco.getTab_cab_libro_banco().limpiar();
                        for (int i = 0; i < tab_cxc_detalle.getTotalFilas(); i++) {
                            if (tab_cxc_detalle.getValor(i, "ide_teclb") != null && !tab_cxc_detalle.getValor(i, "ide_teclb").isEmpty()) {
                                banco.reversar(tab_cxc_detalle.getValor(i, "ide_teclb"), "anula transaccion cxp con ide numero: " + tab_cxc_detalle.getValor(i, "ide_ccdtr"));
                                TablaGenerica tab_cab = utilitario.consultar("SELECT * from tes_cab_libr_banc where ide_teclb=" + tab_cxc_detalle.getValor(i, "ide_teclb"));
                                if (!tab_cab.getValor(0, "ide_cnccc").equals(tab_tabla1.getValor("ide_cnccc"))) {
                                    conta.anular(tab_cab.getValor(0, "ide_cnccc"));
                                }
                            }
                        }
                        banco.getTab_cab_libro_banco().guardar();
                    }
                    // reverso el comprovante de inventario y el comprobante de contabilidad de costos si existe
                    TablaGenerica tab_inv_cab_inv = utilitario.consultar("select * from inv_cab_comp_inve where ide_incci in ( "
                            + "select ide_incci from inv_det_comp_inve where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa") + " GROUP BY ide_incci)");
                    if (tab_inv_cab_inv.getTotalFilas() > 0) {
                        utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_inepi=0 where ide_incci=" + tab_inv_cab_inv.getValor(0, "ide_incci"));
                        cls_inventario inv = new cls_inventario();
                        inv.reversar_mas(tab_tabla1.getValor("ide_cnccc"), tab_inv_cab_inv.getValor(0, "ide_cnccc"), "Reversa de comprobante num:" + tab_tabla1.getValor("ide_cnccc"));
                        if (tab_inv_cab_inv.getValor(0, "ide_cnccc") != null && !tab_inv_cab_inv.getValor(0, "ide_cnccc").isEmpty()) {
                            conta.anular(tab_inv_cab_inv.getValor(0, "ide_cnccc"));
                        }
                    }
                    String ide_cccfa_anulada = tab_tabla1.getValor("ide_cccfa");
                    utilitario.getConexion().guardarPantalla();
                    tab_tabla1.ejecutarSql();
                    tab_tabla1.setFilaActual(ide_cccfa_anulada);

                }
            } else {
                utilitario.agregarMensajeInfo("No se puede Anula la factura", "La factura seleccionada ya esta anulada");
            }
        }
    }

    public void seleccionarSerieFactura() {
        if (com_serie_factura.getValue() != null) {
            tab_tabla1.setCondicion("ide_ccdaf=" + com_serie_factura.getValue().toString());
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
            tab_tabla3.ejecutarSql();
            che_solo_guardar.setValue(tab_tabla1.getValor("solo_guardar_cccfa"));
            utilitario.addUpdate("che_solo_guardar");
        } else {
            tab_tabla1.limpiar();
            tab_tabla3.limpiar();
        }
        cargar_saldo();
    }

    public void aceptar_datos_cliente() {

        dia_datos_clie.cerrar();
        utilitario.addUpdate("tab_tabla9,dia_datos_clie,tab_tabla1,tab_tabla2,tab_tabla3");
        List<String> respaldo_sql = utilitario.getConexion().getSqlPantalla();
        utilitario.getConexion().getSqlPantalla().clear();
        tab_tabla9.guardar();
        if (!utilitario.getConexion().getSqlPantalla().isEmpty()) {
            utilitario.getConexion().ejecutarSql(utilitario.getConexion().getSqlPantalla().get(0));
            // utilitario.getConexion().commit();*****
        }
        utilitario.getConexion().setSqlPantalla(respaldo_sql);

    }

    public void abrirDatosClientes() {
//        dia_datos_clie.dibujar();
//        utilitario.addUpdate("dia_datos_clie");
        if (tab_tabla1.getValor("ide_geper") != null && !tab_tabla1.getValor("ide_geper").isEmpty()) {
            tab_tabla9.setCondicion("es_cliente_geper=true and ide_geper=" + tab_tabla1.getValor("ide_geper"));
            tab_tabla9.ejecutarSql();
            dia_datos_clie.dibujar();
            utilitario.addUpdate("dia_datos_clie,tab_tabla9");
        } else {
            utilitario.agregarMensajeInfo("Datos Clientes", "Atencion no ha seleccionado ningun proveedor");
        }
    }

    public void aceptar_datos_proveedor() {

        dia_datos_clie.cerrar();
        utilitario.addUpdate("tab_tabla9,dia_datos_clie,tab_tabla2,tab_tabla3");
        List<String> respaldo_sql = utilitario.getConexion().getSqlPantalla();
        utilitario.getConexion().getSqlPantalla().clear();
        tab_tabla9.guardar();
        if (!utilitario.getConexion().getSqlPantalla().isEmpty()) {
            utilitario.getConexion().ejecutarSql(utilitario.getConexion().getSqlPantalla().get(0));
           // utilitario.getConexion().commit(); ****
        }
        utilitario.getConexion().setSqlPantalla(respaldo_sql);

    }

    public void aceptar_datos_producto() {
        dia_datos_producto.cerrar();
        List<String> respaldo_sql = utilitario.getConexion().getSqlPantalla();
        utilitario.getConexion().getSqlPantalla().clear();
        tab_tabla10.guardar();
        if (!utilitario.getConexion().getSqlPantalla().isEmpty()) {
            utilitario.getConexion().ejecutarSql(utilitario.getConexion().getSqlPantalla().get(0));
           // utilitario.getConexion().commit();****
        }
        utilitario.getConexion().setSqlPantalla(respaldo_sql);
        calcula_iva();
        calcula_total_detalles_cxc();
        utilitario.addUpdate("tab_tabla10,dia_datos_producto,tab_tabla1,tab_tabla2,tab_tabla3");
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

    public void cancelarDialogo() {
        if (dia_banco.isVisible()) {
            dia_banco.cerrar();
            utilitario.addUpdate("dia_banco");
        } else if (via_comprobante_conta.isVisible()) {
            via_comprobante_conta.cerrar();
            utilitario.addUpdate("via_comprobante_conta");
        } else if (dia_datos_clie.isVisible()) {
            dia_datos_clie.cerrar();
            utilitario.addUpdate("dia_datos_clie");
        } else if (dia_comprobante_rete.isVisible()) {
            dia_comprobante_rete.cerrar();
            utilitario.addUpdate("dia_comprobante_rete");
        } else if (dia_datos_producto.isVisible()) {
            dia_datos_producto.cerrar();
            utilitario.addUpdate("dia_datos_producto");
        }

        //else if (dia_datos_producto.isVisible()) {
//            dia_datos_producto.cerrar();
//            utilitario.addUpdate("dia_datos_producto");
//        } else if (dia_datos_provee.isVisible()) {
//            dia_datos_provee.cerrar();
//            utilitario.addUpdate("dia_datos_provee");
//        }
//        utilitario.getConexion().rollback(); *****************
        utilitario.getConexion().getSqlPantalla().clear();
    }

    public boolean es_bien(String ide_inarti) {
        String art = obtener_tipo_articulo(ide_inarti);
        if (art.equals(p_bienes)) {
            return true;
        } else {
            // tab_tabla2.setValor(tab_tabla2.getFilaActual(), "precio_ccdfa", "0");
            // utilitario.addUpdateTabla(tab_tabla2, "precio_ccdfa", "");
            return false;
        }
    }

    public void generarComprobanteRetencion() {

        List sql_num_pagos = utilitario.getConexion().consultar("select * from cxc_detall_transa where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa") + " and numero_pago_ccdtr>0");
        if (sql_num_pagos.isEmpty()) {
            vir_comprobante_retencion.setVistaRetencion(tab_tabla1, tab_tabla2, true);
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
            utilitario.agregarMensajeInfo("No puede generar una nueva retencion", "La factura tiene Pagos Realizados");
        }
    }

    public void dibujarDialogoPagoContado() {
        eti_monto_cheque.setValue("Monto: ");
        tex_num_cheque.setValue(null);
        tex_monto_cheque.setDisabled(true);
        com_cuenta_banco.setValue(null);
        if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_efec"))) {
            eti_banc.setValue("Caja: ");
            eti_cue_banc.setValue("Caja (Detalle): ");
            eti_num_cheque.setRendered(false);
            tex_num_cheque.setRendered(false);
            eti_cue_banc.setRendered(true);
            com_cuenta_banco.setRendered(true);
            eti_banc.setRendered(true);
            com_banco.setRendered(true);
            eti_transferencia_cuentas.setRendered(false);
            aut_transferencia_cuentas.setRendered(false);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - tot_retenido), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is TRUE and ide_empr=" + utilitario.getVariable("ide_empr"));
        }
        if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_cheque"))) {
            eti_banc.setValue("Caja: ");
            eti_cue_banc.setValue("Caja (Detalle): ");
            eti_num_cheque.setValue("Numero Cheque: ");

            eti_num_cheque.setRendered(true);
            tex_num_cheque.setRendered(true);
            eti_cue_banc.setRendered(true);
            com_cuenta_banco.setRendered(true);
            eti_banc.setRendered(true);
            com_banco.setRendered(true);
            eti_transferencia_cuentas.setRendered(false);
            aut_transferencia_cuentas.setRendered(false);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - tot_retenido), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is TRUE and ide_empr=" + utilitario.getVariable("ide_empr"));
        }

        if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_transferencia"))) {
            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Tansferencia: ");

            eti_num_cheque.setRendered(true);
            tex_num_cheque.setRendered(true);
            eti_cue_banc.setRendered(true);
            com_cuenta_banco.setRendered(true);
            eti_banc.setRendered(true);
            com_banco.setRendered(true);
            eti_transferencia_cuentas.setRendered(false);
            aut_transferencia_cuentas.setRendered(false);

            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - tot_retenido), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is false and ide_empr=" + utilitario.getVariable("ide_empr"));
        }
        if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_deposito"))) {
            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Deposito: ");

            eti_num_cheque.setRendered(true);
            tex_num_cheque.setRendered(true);
            eti_cue_banc.setRendered(true);
            com_cuenta_banco.setRendered(true);
            eti_banc.setRendered(true);
            com_banco.setRendered(true);
            eti_transferencia_cuentas.setRendered(false);
            aut_transferencia_cuentas.setRendered(false);

            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - tot_retenido), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is false and ide_empr=" + utilitario.getVariable("ide_empr"));
        }

        // transferencia de cuentas de las casas
        if (tab_tabla1.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_transferencia_casas"))) {
            eti_banc.setValue("Banco Casas: ");
            eti_cue_banc.setValue("Cuentas Casas: ");
            eti_num_cheque.setRendered(false);
            tex_num_cheque.setRendered(false);
            eti_cue_banc.setRendered(false);
            com_cuenta_banco.setRendered(false);
            eti_banc.setRendered(false);
            com_banco.setRendered(false);
            eti_transferencia_cuentas.setRendered(true);
            aut_transferencia_cuentas.setRendered(true);
            tex_num_cheque.setValue("");
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - tot_retenido), 2));
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is false and ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco.setValue(utilitario.getVariable("p_tes_banco_casas"));
            com_cuenta_banco.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + utilitario.getVariable("p_tes_banco_casas"));
        }

        dia_banco.dibujar();
        utilitario.addUpdate("dia_banco");
    }
    double tot_retenido = 0;
    boolean boo_hizo_retencion = false;

    public void aceptarComprobanteRetencion() {

        if (boo_hizo_retencion) {
            if (vir_comprobante_retencion.validarComprobanteRetencion()) {
                tot_retenido = vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
                if (vir_comprobante_retencion.getTab_datos_proveedor().isFilaModificada()) {
                    utilitario.getConexion().agregarSqlPantalla("update gen_persona set direccion_geper ='" + vir_comprobante_retencion.getTab_datos_proveedor().getValor("direccion_geper") + "'where ide_geper =" + tab_tabla1.getValor("ide_geper") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                }
                vir_comprobante_retencion.cerrar();
                List sql_forma_pago = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + tab_tabla1.getValor("ide_cndfp"));
                int dias = -1;
                if (!sql_forma_pago.isEmpty()) {
                    dias = Integer.parseInt(sql_forma_pago.get(0).toString());
                }

                if (dias == 0) {
                    dibujarDialogoPagoContado();
                } else if (dias > 0) {
                    generarComprobanteContabilidad();
                }
            }

        } else {

            tot_retenido = 0;
            List sql_forma_pago = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + tab_tabla1.getValor("ide_cndfp"));
            int dias = -1;
            if (!sql_forma_pago.isEmpty()) {
                dias = Integer.parseInt(sql_forma_pago.get(0).toString());
            }

            if (dias == 0) {
                dibujarDialogoPagoContado();
            } else if (dias > 0) {
                generarComprobanteContabilidad();
            }
        }

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
    String ide_cnccc_anterior = "-1";

    public synchronized void aceptarComprobanteContabilidad() {

        if (via_comprobante_conta.validarComprobante()) {
            System.out.println("cerrar dialogo");
            via_comprobante_conta.cerrar();
            utilitario.addUpdate("via_comprobante_conta");
            cab_com_con.setObservacion_cnccc(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_comprobante_conta.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            lista_detalles.clear();
            for (int i = 0; i < via_comprobante_conta.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_comprobante_conta.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            String ide_cnccc = conta.generarAsientoContable(cab_com_con);
            System.out.println("hace asiento de costos? " + boo_asiento_costos);
            if (boo_asiento_costos == false) {
                if (ide_cnccc != null) {
                    tab_tabla1.setValor("ide_cnccc", ide_cnccc);
                    System.out.println("size antes " + utilitario.getConexion().getSqlPantalla().size());
                    tab_tabla1.guardar();
                    tab_tabla2.guardar();
                    System.out.println("size despues " + utilitario.getConexion().getSqlPantalla().size());
                    via_comprobante_conta.cerrar();
                    if (realizarComprobanteInventario()) {
                        boo_asiento_costos = true;
                        generarComprobanteInventarioCXC(ide_cnccc);
                        System.out.println("size 910 " + utilitario.getConexion().getSqlPantalla().size());
                    }

                    // si existe retencion actualizo la cabecera de la factura
                    if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
//                        if (boo_hizo_retencion) {
                        vir_comprobante_retencion.getTab_cab_retencion_vretencion().setValor("ide_cnccc", ide_cnccc);
                        vir_comprobante_retencion.getTab_cab_retencion_vretencion().guardar();
                        vir_comprobante_retencion.getTab_det_retencion_vretencion().guardar();
                        utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_cncre=" + vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("ide_cncre") + " where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
                        System.out.println("size 1136 " + utilitario.getConexion().getSqlPantalla().size());
                        tab_tabla1.setValor("ide_cncre", vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("ide_cncre"));
//                        }
                    }

                    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                    String num_dias = conta.obtenerNumDiasFormaPago(tab_tabla1.getValor("ide_cndfp"));
                    if (!tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_donacion"))) {
//                        try {
                        if (Integer.parseInt(num_dias) == 0) {
                            if (tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_transferencia_casas"))) {
                                tex_num_cheque.setValue(ide_cnccc);
                                cxc.generarTransaccionVentaTransferenciaCasas(tab_tabla1, tot_retenido, ide_cnccc);
                            } else {
                                utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set pagado_cccfa=true where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                                Tabla tab_lib_banco = bancos.generarLibroBancoCxC(tab_tabla1, com_cuenta_banco.getValue() + "", Double.parseDouble(tex_monto_cheque.getValue() + ""), "Pago: " + tab_tabla1.getValor("observacion_cccfa"), tex_num_cheque.getValue() + "");
                                cxc.generarTransaccionVenta(tab_tabla1, tot_retenido, tab_lib_banco);
                            }
                        } else {
                            cxc.generarTransaccionVenta(tab_tabla1, tot_retenido, null);
                        }
//                        } catch (Exception e) {
//                        }
                    } else {
                        cxc.generarTransaccionVentaDonacion(tab_tabla1, tot_retenido);
                    }
                    if (!tab_tabla1.isFilaInsertada()) {
                        if (che_solo_guardar.getValue().equals("true")) {
                            che_solo_guardar.setValue(false);
                            utilitario.getConexion().agregarSqlPantalla("UPDATE cxc_cabece_factura set solo_guardar_cccfa=false where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
                        }
                    }
                    ide_cnccc_anterior = ide_cnccc;
                    System.out.println("size antes de guardar pantalla " + utilitario.getConexion().getSqlPantalla().size());
                    utilitario.getConexion().guardarPantalla();
                    if (boo_asiento_costos) {
                        if (chk_asiento_costos.getValue().equals("true")) {
                            utilitario.agregarMensajeInfo("hacer asiento costos", "");
                            generarAsientoCostos();
                        }
                    }
                }
            } else {
                if (ide_cnccc != null) {
                    System.out.println("asiento de costos generado");
                    utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_cnccc=" + ide_cnccc + " "
                            + "where ide_incci in (select ide_incci from inv_det_comp_inve where "
                            + "ide_cccfa=" + str_ide_cccfa + ")");
                    utilitario.getConexion().guardarPantalla();
                    boo_asiento_costos = false;
                    utilitario.agregarNotificacionInfo("Atencion", "El asiento de costos se ha generado correctamente");
                }
            }
            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
            tab_tabla3.ejecutarSql();
            che_solo_guardar.setDisabled(true);
            utilitario.addUpdate("che_solo_guardar,via_comprobante_conta,tab_tabla1,tab_tabla2,tab_tabla3");
            cargar_saldo();

        }
    }

    public boolean realizarComprobanteInventario() {
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (es_bien1(tab_tabla2.getValor(i, "ide_inarti"))) {
                return true;
            }
        }
        return false;
    }

    public boolean es_bien1(String ide_inarti) {
        String art = obtener_tipo_articulo(ide_inarti);
        if (art.equals(p_bienes)) {
            return true;
        } else {
            return false;
        }
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        utilitario.addUpdate("tab_tabla2,tab_tabla3");
    }

    public void obtener_alterno() {
        String tipo_articulo = "";
        String aplica_iva = "";
        String ide_cntco = conta.obtenerParametroPersona("ide_cntco", tab_tabla1.getValor("ide_geper"));

        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            tipo_articulo = obtener_tipo_articulo(tab_tabla2.getValor(i, "ide_inarti"));
            aplica_iva = obtener_aplica_iva_producto(tab_tabla2.getValor(i, "ide_inarti"));
            System.out.println("tipo articulo " + tipo_articulo);
            System.out.println("aplica iva " + aplica_iva);
            try {
                if (!tipo_articulo.equals(p_activos_fijos) && aplica_iva.equals("si")) {
                    tab_tabla2.setValor(i, "alterno_ccdfa", utilitario.getVariable("p_sri_venta_local_12%_401"));
                } else if (tipo_articulo.equals(p_activos_fijos) && aplica_iva.equals("no") && tab_tabla2.getValor(i, "credito_tributario_ccdfa").equals("true")) {
                    tab_tabla2.setValor(i, "alterno_ccdfa", utilitario.getVariable("p_sri_venta_activos_0%_dertri_406"));
                } else if (tipo_articulo.equals(p_activos_fijos) && aplica_iva.equals("si")) {
                    tab_tabla2.setValor(i, "alterno_ccdfa", utilitario.getVariable("p_sri_venta_activos_12%_402"));
                } else if (tipo_articulo.equals(p_activos_fijos) && aplica_iva.equals("no") && tab_tabla2.getValor(i, "credito_tributario_ccdfa").equals("false")) {
                    tab_tabla2.setValor(i, "alterno_ccdfa", utilitario.getVariable("p_sri_venta_activos_0%_no_dertri_404"));
                } else if (!tipo_articulo.equals(p_activos_fijos) && aplica_iva.equals("no") && tab_tabla2.getValor(i, "credito_tributario_ccdfa").equals("true")) {
                    tab_tabla2.setValor(i, "alterno_ccdfa", utilitario.getVariable("p_sri_venta_local_0%_dertri_405"));
                } else if (!tipo_articulo.equals(p_activos_fijos) && aplica_iva.equals("no") && tab_tabla2.getValor(i, "credito_tributario_ccdfa").equals("false")) {
                    tab_tabla2.setValor(i, "alterno_ccdfa", utilitario.getVariable("p_sri_venta_local_no_dertri_0%_403"));
                } else if (aplica_iva.equals("no objeto")) {
                    tab_tabla2.setValor(i, "alterno_ccdfa", utilitario.getVariable("p_sri_trans_no_obj_iva_431"));
                } else {
                    tab_tabla2.setValor(i, "alterno_ccdfa", "00");
                }
            } catch (Exception e) {
                tab_tabla2.setValor(i, "alterno_ccdfa", "00");
            }

        }
        utilitario.addUpdate("tab_tabla2");
    }

    public String obtener_tipo_articulo(String ide_arti) {
        String ide_art = ide_arti;
        TablaGenerica inv_ide_arti = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_art);
        if (inv_ide_arti.getTotalFilas() > 0) {
            do {
                ide_art = inv_ide_arti.getValor(0, "inv_ide_inarti");
                inv_ide_arti = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_art);
            } while (inv_ide_arti.getValor(0, "inv_ide_inarti") != null && !inv_ide_arti.getValor(0, "inv_ide_inarti").isEmpty());
        }
        return ide_art;
    }

    public String obtener_aplica_iva_producto(String ide_inarti) {

        TablaGenerica sql_iva = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_inarti);
        String aplica_iva = "";
        if (sql_iva.getTotalFilas() > 0) {
            if (sql_iva.getValor(0, "iva_inarti") != null && !sql_iva.getValor(0, "iva_inarti").isEmpty()) {
                if (sql_iva.getValor(0, "iva_inarti").toString().equals("1")) {
                    aplica_iva = "si";
                } else if (sql_iva.getValor(0, "iva_inarti").toString().equals("-1")) {
                    aplica_iva = "no";
                } else if (sql_iva.getValor(0, "iva_inarti").toString().equals("0")) {
                    aplica_iva = "no objeto";
                }
                return aplica_iva;
            }
        }
        return null;

    }

    public void cargar_saldo() {
        double sum_pagos = 0;
        if (tab_tabla1.getTotalFilas() > 0) {
            if (tab_tabla3.getTotalFilas() > 0) {
                for (int i = 0; i < tab_tabla3.getTotalFilas(); i++) {
                    sum_pagos = Double.parseDouble(tab_tabla3.getValor(i, "valor_ccdtr").toString()) + sum_pagos;
                }
                double saldo = Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - sum_pagos;
                eti_saldo.setValue("Saldo x Cobrar: " + utilitario.getFormatoNumero(saldo));
                eti_saldo.setStyle("font-size:16px;font-weight: bold;");
            } else {
                eti_saldo.setValue("Saldo x Cobrar: " + utilitario.getFormatoNumero(tab_tabla1.getValor("total_cccfa")));
                eti_saldo.setStyle("font-size:16px;font-weight: bold;");
            }
        } else {
            eti_saldo.setValue("Saldo x Cobrar: ");
            eti_saldo.setStyle("font-size:16px;font-weight: bold;");
        }
        utilitario.addUpdate("eti_saldo");
    }

    @Override
    public void insertar() {
        if (tab_tabla2.isFocus()) {
            if (tab_tabla1.isFilaInsertada()) {
                tab_tabla2.insertar();
            }
        } else if (tab_tabla1.isFocus()) {
            if (com_serie_factura.getValue() != null) {
                tab_tabla1.insertar();
                che_solo_guardar.setValue(false);
                che_solo_guardar.setDisabled(false);
                utilitario.addUpdate("che_solo_guardar");
                tab_tabla1.setValor("ide_ccdaf", com_serie_factura.getValue().toString());
            } else {
                utilitario.agregarMensajeInfo("No se puede Insertar", "Debe seleccionar una serie de la factura");
            }
//            bot_comp_retencion.setDisabled(true);
        } else if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.insertar();
        } else if (vir_comprobante_retencion.getTab_det_retencion_vretencion().isFocus()) {
            vir_comprobante_retencion.getTab_det_retencion_vretencion().insertar();
        }
        eti_saldo.setValue("Saldo: ");
        utilitario.addUpdate("eti_saldo");
    }

    public boolean validar() {

        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe seleccionar un proveedor");
            return false;
        }
        if (tab_tabla1.getValor("secuencial_cccfa") == null || tab_tabla1.getValor("secuencial_cccfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar el Numero de Factura");
            return false;
        }
        if (tab_tabla1.getValor("observacion_cccfa") == null || tab_tabla1.getValor("observacion_cccfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar una observacion");
            return false;
        }
        if (tab_tabla2.getValor("alterno_ccdfa") == null || tab_tabla2.getValor("alterno_ccdfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar un alterno");
            return false;
        }
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (tab_tabla2.getValor(i, "total_ccdfa").equals("0.00")) {
                utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Error en el detalle no existe stock para producto");
                return false;
            }
        }
        if (tab_tabla2.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar detalles a la factura");
            return false;
        }

        return true;
    }

    @Override
    public void guardar() {
        boolean boo_guardar = false;
        if (!tab_tabla1.isFilaInsertada()) {
            if (che_solo_guardar.getValue().equals("true")) {
                boo_guardar = true;
            }
        } else {
            obtener_alterno();
            if (validar()) {
                boo_guardar = true;
            }
        }
        if (boo_guardar) {
            if (tab_tabla1.isFilaInsertada()) {
                if (che_solo_guardar.getValue().equals("true")) {
                    tab_tabla1.setValor("solo_guardar_cccfa", "true");
                    tab_tabla1.guardar();
                    tab_tabla2.guardar();
                    guardarPantalla();
                    che_solo_guardar.setDisabled(true);
                    utilitario.addUpdate("che_solo_guardar");
                } else {
                    tab_tabla1.setValor("solo_guardar_cccfa", "false");
                    che_solo_guardar.setDisabled(false);
                    utilitario.addUpdate("che_solo_guardar");
                    if (!tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_tarjeta_credito"))
                            && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_donacion"))) {
                        generarComprobanteRetencion();
                    } else {
                        boo_hizo_retencion = false;
                        generarComprobanteContabilidad();
                    }

                }
            } else {
                if (!tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_tarjeta_credito"))
                        && !tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_donacion"))) {
                    generarComprobanteRetencion();
                } else {
                    boo_hizo_retencion = false;
                    generarComprobanteContabilidad();
                }
            }

        }
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

        if (com_cuenta_banco.getValue() != null) {
            cls_bancos banco = new cls_bancos();
            tex_num_cheque.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco.getValue() + "", utilitario.getVariable("p_tes_tran_cheque")));
        } else {
            tex_num_cheque.setValue("");
        }
    }

    @Override
    public void eliminar() {
        if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.eliminar();
        } else {
            utilitario.getTablaisFocus().eliminar();
        }
        calcula_total_detalles_cxc();
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public void obtener_total_detalle_factura_cxc(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        double cantidad = 0;
        double precio = 0;
        double total = 0;

        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti").isEmpty()) {
            if (es_bien(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti"))) {
//                cargarValorVenta(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti"));
                cargarPrecioPromedio(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti"));
            }
        }

        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_ccdfa") != null
                && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_ccdfa").isEmpty()) {
            try {
                cantidad = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_ccdfa"));

            } catch (Exception e) {
                cantidad = 0;
            }
        }
        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_ccdfa") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_ccdfa").isEmpty()) {
            try {
                precio = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_ccdfa"));
            } catch (Exception e) {
                precio = 0;
            }
        }

        total = cantidad * precio;
        tab_tabla2.setValor(tab_tabla2.getFilaActual(), "total_ccdfa", utilitario.getFormatoNumero(total));
        utilitario.addUpdateTabla(tab_tabla2, "total_ccdfa", "");
        calcula_iva();
        calcula_total_detalles_cxc();
    }

    public void cargar_direccion(SelectEvent evt) {
        tab_tabla1.modificar(evt);
        if (!tab_tabla1.getValor("ide_geper").isEmpty()) {
            cliente_actual = tab_tabla1.getValor("ide_geper");
            List direccion_sql = utilitario.getConexion().consultar("SELECT direccion_geper from gen_persona where es_cliente_geper is TRUE and ide_geper=" + cliente_actual);
            if (direccion_sql.get(0) != null) {
                tab_tabla1.setValor(tab_tabla1.getFilaActual(), "direccion_cccfa", direccion_sql.get(0).toString());
                utilitario.addUpdateTabla(tab_tabla1, "direccion_cccfa", "");
            }
            cargar_secuencial_serie(evt);
        }

    }

    public void cargar_secuencial_serie(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor("ide_ccdaf") != null) {
            if (!tab_tabla1.getValor("ide_ccdaf").toString().isEmpty()) {
                TablaGenerica secuencial_sql = utilitario.consultar("select ide_ccdaf,MAX(secuencial_cccfa) as num_max FROM cxc_cabece_factura where ide_ccdaf=" + tab_tabla1.getValor("ide_ccdaf") + " GROUP BY ide_ccdaf ");
                if (secuencial_sql.getTotalFilas() > 0) {
                    Long secuencial = Long.parseLong(secuencial_sql.getValor("num_max"));
                    secuencial = secuencial + 1;
                    String ceros = utilitario.generarCero(7 - secuencial.toString().length());
                    String num_max = ceros.concat(secuencial.toString());

                    tab_tabla1.setValor(tab_tabla1.getFilaActual(), "secuencial_cccfa", num_max + "");
                    utilitario.addUpdateTabla(tab_tabla1, "secuencial_cccfa", "");
                } else {
                    Long secuencial = Long.parseLong("1");
                    String ceros = utilitario.generarCero(7 - secuencial.toString().length());
                    String num_max = ceros.concat(secuencial.toString());

                    tab_tabla1.setValor(tab_tabla1.getFilaActual(), "secuencial_cccfa", num_max + "");
                    utilitario.addUpdateTabla(tab_tabla1, "secuencial_cccfa", "");
                }
            }
        } else {
            System.out.println("aaaa ");
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "secuencial_cccfa", "");
            utilitario.addUpdateTabla(tab_tabla1, "secuencial_cccfa", "");
        }

    }

    public void calcula_iva() {
        base_grabada = 0;
        base_no_objeto = 0;
        base_tarifa0 = 0;
        valor_iva = 0;
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (tab_tabla2.getValor(i, "ide_inarti") != null) {
                List sql_iva = utilitario.getConexion().consultar("select iva_inarti from inv_articulo where ide_inarti=" + tab_tabla2.getValor(i, "ide_inarti"));
                int iva = Integer.parseInt(sql_iva.get(0).toString());
                if (iva == 1) {
                    base_grabada = Double.parseDouble(tab_tabla2.getValor(i, "total_ccdfa")) + base_grabada;
                    valor_iva = base_grabada * 0.12;
                } else {
                    if (iva == 0) {
                        base_no_objeto = Double.parseDouble(tab_tabla2.getValor(i, "total_ccdfa")) + base_no_objeto;
                    } else {
                        base_tarifa0 = Double.parseDouble(tab_tabla2.getValor(i, "total_ccdfa")) + base_tarifa0;
                    }
                }
            }
        }
        tab_tabla1.setValor("base_grabada_cccfa", utilitario.getFormatoNumero(String.valueOf(base_grabada), 2));
        tab_tabla1.setValor("base_no_objeto_iva_cccfa", utilitario.getFormatoNumero(String.valueOf(base_no_objeto), 2));
        tab_tabla1.setValor("valor_iva_cccfa", utilitario.getFormatoNumero(String.valueOf(valor_iva), 2));
        tab_tabla1.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(String.valueOf(base_tarifa0), 2));
        utilitario.addUpdate("tab_tabla1");
    }

    public void calcula_total_detalles_cxc() {

        double total_fac = 0;
        double val_iva = 0;
        double val_base_no_iva = 0;
        double val_base_grabada = 0;
        double val_tarifa0 = 0;
        if (tab_tabla1.getValor("valor_iva_cccfa") != null) {
            val_iva = Double.parseDouble(tab_tabla1.getValor("valor_iva_cccfa"));
        }
        if (tab_tabla1.getValor("base_no_objeto_iva_cccfa") != null) {
            val_base_no_iva = Double.parseDouble(tab_tabla1.getValor("base_no_objeto_iva_cccfa"));
        }
        if (tab_tabla1.getValor("base_grabada_cccfa") != null) {
            val_base_grabada = Double.parseDouble(tab_tabla1.getValor("base_grabada_cccfa"));
        }
        if (tab_tabla1.getValor("base_tarifa0_cccfa") != null) {
            val_tarifa0 = Double.parseDouble(tab_tabla1.getValor("base_tarifa0_cccfa"));
        }

        total_fac = val_iva + val_base_grabada + val_base_no_iva + val_tarifa0;

        if (tab_tabla1.getTotalFilas() > 0) {
            tab_tabla1.setValor("total_cccfa", total_fac + "");
        }

        utilitario.addUpdate("tab_tabla1");
        //cargar_saldo();
    }

    public String obtener_porcen(String cabecera_impuesto, String proveedor, String tipo_documento) {
        String porcentaje = "";
        List sql_porcentaje = utilitario.getConexion().consultar("SELECT porcentaje_cndim from con_detall_impues where ide_cnvim= "
                + "(select ide_cnvim from con_vigenc_impues where ide_cncim =" + cabecera_impuesto + ""
                + "and estado_cnvim is TRUE) "
                + "and ide_cntdo=" + tipo_documento + ""
                + "and ide_cntco=(select ide_cntco from gen_persona where ide_geper=" + proveedor + ""
                + "and es_cliente_geper is TRUE)");
        if (!sql_porcentaje.isEmpty()) {
            porcentaje = sql_porcentaje.get(0).toString();
        }
        return porcentaje;
    }

//
    public void cargarValorVenta(String ide_inarti) {

        cls_inventario inv = new cls_inventario();
        String precio_venta = inv.getValordeVenta(ide_inarti);
        if (precio_venta != null) {
            String precio_uni = utilitario.getFormatoNumero(Double.parseDouble(precio_venta));
            tab_tabla2.setValor(tab_tabla2.getFilaActual(), "precio_ccdfa", precio_uni);
        } else {
            tab_tabla2.setValor(tab_tabla2.getFilaActual(), "precio_ccdfa", "0");
        }
        utilitario.addUpdateTabla(tab_tabla2, "precio_ccdfa", "");

    }

    public void obtener_total_detalle_factura_cxc1(SelectEvent evt) {
        tab_tabla2.modificar(evt);
        double cantidad = 0;
        double precio = 0;
        double total = 0;

        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti").isEmpty()) {
            if (es_bien(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti"))) {
                cargarValorVenta(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti"));
                cargarPrecioPromedio(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "ide_inarti"));
            }
        }
        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_ccdfa") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_ccdfa").isEmpty()) {
            try {
                cantidad = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "cantidad_ccdfa"));
            } catch (Exception e) {
                cantidad = 0;
            }
        }
        if (tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_ccdfa") != null && !tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_ccdfa").isEmpty()) {
            try {
                precio = Double.parseDouble(tab_tabla2.getValor(tab_tabla2.getFilaActual(), "precio_ccdfa"));
            } catch (Exception e) {
                precio = 0;
            }
        }

        total = cantidad * precio;
        tab_tabla2.setValor(tab_tabla2.getFilaActual(), "total_ccdfa", utilitario.getFormatoNumero(total));

        utilitario.addUpdateTabla(tab_tabla2, "total_ccdfa", "");
        calcula_iva();
        calcula_total_detalles_cxc();

    }

    public int validar_stock_existente(String cantidad) {
        cls_inventario inv = new cls_inventario();
        String saldo = inv.cantidad_disponible("1", tab_tabla2.getValor("ide_inarti"), utilitario.getFormatoFecha(utilitario.getFechaActual()));
        if (!saldo.isEmpty()) {
            if ((Double.parseDouble(saldo) - Double.parseDouble(cantidad)) >= 0) {
                return 1;
            } else {
                utilitario.agregarMensajeInfo("Control de Stock", "La cantidad ingresada sobrepasa el stock existente. Cantidad Disponible " + saldo);
                return 0;
            }
        } else {
            return -1;
        }
    }

    public void cargarPrecioPromedio(String ide_inarti) {
        cls_inventario inv = new cls_inventario();
        String precio_promedio = inv.getPrecioPromedioTransaccionNegativa(ide_inarti, "1");
        if (precio_promedio != null) {
            String precio_uni = utilitario.getFormatoNumero(Double.parseDouble(precio_promedio));
            tab_tabla2.setValor(tab_tabla2.getFilaActual(), "precio_promedio_ccdfa", precio_uni);
        } else {

            tab_tabla2.setValor(tab_tabla2.getFilaActual(), "precio_promedio_ccdfa", "0");
        }
        utilitario.addUpdateTabla(tab_tabla2, "precio_promedio_ccdfa", "");
    }

    public boolean haceKardex(String ide_inarti) {
        TablaGenerica tab_articulo = utilitario.consultar("select ide_inarti,hace_kardex_inarti from inv_articulo where ide_inarti=" + ide_inarti);
        if (tab_articulo.getTotalFilas() > 0) {
            if (tab_articulo.getValor(0, "hace_kardex_inarti") != null && !tab_articulo.getValor(0, "hace_kardex_inarti").isEmpty()) {
                if (tab_articulo.getValor(0, "hace_kardex_inarti").equalsIgnoreCase("true")) {
                    return true;
                }
            }
        }
        return false;
    }
    boolean boo_asiento_costos = false;
    String str_ide_cccfa;

    public void generarAsientoCostos() {
        System.out.println("generar asiento de costos");
        conta.limpiar();
        boo_asiento_costos = true;
        if (tab_tabla1.getValor("observacion_cccfa") != null) {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_diario, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cccfa"), tab_tabla1.getValor("observacion_cccfa"));
        } else {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_diario, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cccfa"), "por concepto de factura por cobrar numero " + tab_tabla1.getValor("secuencial_cccfa"));
        }

        lista_detalles.clear();
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (es_bien1(tab_tabla2.getValor(i, "ide_inarti"))) {
                if (haceKardex(tab_tabla2.getValor(i, "ide_inarti"))) {
                    // COSTO EN VENTAS --- DEBE
                    String ide_cuenta_cos_ven = conta.buscarCuentaProducto("COSTO EN VENTAS", tab_tabla2.getValor(i, "ide_inarti"));
                    double valor = Double.parseDouble(tab_tabla2.getValor(i, "cantidad_ccdfa")) * Double.parseDouble(tab_tabla2.getValor(i, "precio_promedio_ccdfa"));
                    if (ide_cuenta_cos_ven != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_cos_ven, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                    }
                    // INVENTARIO GASTO ACTIVO --- HABER
                    String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_tabla2.getValor(i, "ide_inarti"));
                    if (ide_cuenta_inv_gas_act != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                    } else {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), getNombreAriculo(tab_tabla2.getValor(i, "ide_inarti"))));
                    }
                }
            }
        }
        str_ide_cccfa = tab_tabla1.getValor("ide_cccfa");
        cab_com_con.setDetalles(lista_detalles);
        chk_asiento_costos.setRendered(false);
        eti_hace_asiento_costos.setValue("ASIENTO DE COSTOS");
        via_comprobante_conta.setVistaAsiento(cab_com_con);
        via_comprobante_conta.dibujar();
        utilitario.addUpdate("via_comprobante_conta");

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
        boo_asiento_costos = false;
        if (tab_tabla1.getValor("observacion_cccfa") != null) {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_diario, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cccfa"), tab_tabla1.getValor("observacion_cccfa"));
        } else {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_diario, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cccfa"), "por concepto de factura por cobrar numero " + tab_tabla1.getValor("secuencial_cccfa"));
        }

        lista_detalles.clear();

        // CUENTA X COBRAR ----- DEBE
        String ide_cuenta_cuentas_x_cobrar = conta.buscarCuentaPersona("CUENTA POR COBRAR", tab_tabla1.getValor("ide_geper"));
        double valor;
        if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
            valor = Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
        } else {
            valor = Double.parseDouble(tab_tabla1.getValor("total_cccfa"));
        }
        if (ide_cuenta_cuentas_x_cobrar != null) {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_cuentas_x_cobrar, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
        } else {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
        }

        // RETENCION A LA RENTA ----- DEBE
        for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
            if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").equals(p_iva30) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").equals(p_iva70) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").equals(p_iva100)) {
                String ide_cuenta_retencion_iva_x_pagar = conta.buscarCuenta("RETENCION IVA POR COBRAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                if (ide_cuenta_retencion_iva_x_pagar != null) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_retencion_iva_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                }
            } else {
                String ide_cuenta_retencion_renta_x_pagar = conta.buscarCuenta("RETENCION RENTA POR COBRAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                if (ide_cuenta_retencion_renta_x_pagar != null) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_retencion_renta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                }
            }
        }

        // VENTAS ------- HABER
        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("VENTAS", tab_tabla2.getValor(i, "ide_inarti"));
            if (ide_cuenta_inv_gas_act != null) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "total_ccdfa"), 2)), getNombreAriculo(tab_tabla2.getValor(i, "ide_inarti"))));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "total_ccdfa"), 2)), getNombreAriculo(tab_tabla2.getValor(i, "ide_inarti"))));
            }
        }

        // IVA EN VENTAS ------- HABER
        String ide_cuenta_credito_tributario = conta.buscarCuenta("IVA EN VENTAS", null, null, null, utilitario.getVariable("p_con_porcentaje_imp_iva"), null, null);
        if (ide_cuenta_credito_tributario != null) {
            if (Double.parseDouble(tab_tabla1.getValor("valor_iva_cccfa")) != 0) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_credito_tributario, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla1.getValor("valor_iva_cccfa"), 2)), ""));
            }
        }

        cab_com_con.setDetalles(lista_detalles);
        chk_asiento_costos.setValue("true");
        chk_asiento_costos.setRendered(true);
        eti_hace_asiento_costos.setValue("HACE ASIENTO DE COSTOS");
        via_comprobante_conta.setVistaAsiento(cab_com_con);
        via_comprobante_conta.dibujar();
        utilitario.addUpdate("via_comprobante_conta");

    }
    boolean boo_pago_tipo_transferencia_casas = false;

    public boolean validarDialogoFormaPago() {
        if (!tab_tabla1.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_transferencia_casas"))) {
            boo_pago_tipo_transferencia_casas = false;
            if (com_banco.getValue() == null) {
                utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar un Banco");
                return false;
            }
            if (com_cuenta_banco.getValue() == null) {
                utilitario.agregarMensajeInfo("Atencion", "Debe seleccionar un cuenta bancaria");
                return false;
            }
        } else {
            boo_pago_tipo_transferencia_casas = true;
            if (aut_transferencia_cuentas.getValue() == null) {
                utilitario.agregarMensajeInfo("Atencion", "No ha seleccionado un cuenta contable");
                return false;
            }
        }
        return true;
    }

    public void generarComprobanteContabilidadContado() {
        if (validarDialogoFormaPago()) {
            boo_asiento_costos = false;
            dia_banco.cerrar();
            utilitario.addUpdate("dia_banco");
            conta.limpiar();
            if (tab_tabla1.getValor("observacion_cccfa") != null) {
                cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_ingreso, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cccfa"), tab_tabla1.getValor("observacion_cccfa"));
            } else {
                cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_ingreso, p_estado_comprobante_normal, p_modulo, tab_tabla1.getValor("ide_geper"), tab_tabla1.getValor("fecha_emisi_cccfa"), "por concepto de factura por pagar numero " + tab_tabla1.getValor("secuencial_cccfa"));
            }
            lista_detalles.clear();
            // CAJA O BANCOS   ---- DEBE
            if (boo_pago_tipo_transferencia_casas == false) {
                cuenta_bancaria_asiento = com_cuenta_banco.getValue().toString();
                List cuenta_contable_banco_sql = utilitario.getConexion().consultar("select ide_cndpc from tes_cuenta_banco where ide_tecba=" + cuenta_bancaria_asiento);
                double valor;
                if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
                    valor = Double.parseDouble(tab_tabla1.getValor("total_cccfa")) - vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
                } else {
                    valor = Double.parseDouble(tab_tabla1.getValor("total_cccfa"));
                }
                if (cuenta_contable_banco_sql != null) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), cuenta_contable_banco_sql.get(0).toString(), Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                } else {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
                }
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), aut_transferencia_cuentas.getValor(), Double.parseDouble(tex_monto_cheque.getValue() + ""), ""));
            }

            // RETENCIONES RENTA ------ DEBE
            for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
                if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").equals(p_iva30) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").equals(p_iva70) || vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim").equals(p_iva100)) {
                    String ide_cuenta_retencion_iva_x_pagar = conta.buscarCuenta("RETENCION IVA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                    if (ide_cuenta_retencion_iva_x_pagar != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_retencion_iva_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    }
                } else {
                    String ide_cuenta_retencion_renta_x_pagar = conta.buscarCuenta("RETENCION RENTA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                    if (ide_cuenta_retencion_renta_x_pagar != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_retencion_renta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    }
                }
            }

//            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
//                String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_tabla2.getValor(i, "ide_inarti"));
//                if (ide_cuenta_inv_gas_act != null) {
//                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "total_ccdfa"), 2)), ""));
//                } else {
//                    utilitario.agregarMensajeInfo("No se encontro la cuenta de inventario", "");
//                }
//            }
            // VENTAS --- HABER
            for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
                String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("VENTAS", tab_tabla2.getValor(i, "ide_inarti"));
                if (ide_cuenta_inv_gas_act != null) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "total_ccdfa"), 2)), getNombreAriculo(tab_tabla2.getValor(i, "ide_inarti"))));
                } else {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_tabla2.getValor(i, "total_ccdfa"), 2)), getNombreAriculo(tab_tabla2.getValor(i, "ide_inarti"))));
                }
            }

            // IVA EN VENTAS  --------  HABER
            String ide_cuenta_credito_tributario = conta.buscarCuenta("IVA EN VENTAS", null, null, null, utilitario.getVariable("p_con_porcentaje_imp_iva"), null, null);
            if (ide_cuenta_credito_tributario != null) {
                if (Double.parseDouble(tab_tabla1.getValor("valor_iva_cccfa")) != 0) {
                    lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_credito_tributario, Double.parseDouble(utilitario.getFormatoNumero(tab_tabla1.getValor("valor_iva_cccfa"), 2)), ""));
                }
            }

            cab_com_con.setDetalles(lista_detalles);
            chk_asiento_costos.setValue("true");
            chk_asiento_costos.setRendered(true);
            eti_hace_asiento_costos.setValue("HACE ASIENTO DE COSTOS");

            via_comprobante_conta.setVistaAsiento(cab_com_con);
            via_comprobante_conta.dibujar();
            utilitario.addUpdate("via_comprobante_conta");

        }
    }

    public void generarComprobanteInventarioCXC(String ide_cnccc) {

        //Cabecera 
        cls_inventario in = new cls_inventario();

        Tabla tab_cab_comp_inv = new Tabla();
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
        tab_cab_comp_inv.setCondicion("ide_incci=-1");
        tab_cab_comp_inv.ejecutarSql();
        //Detalles
        Tabla tab_det_comp_inv = new Tabla();
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
        tab_det_comp_inv.setCondicion("ide_indci=-1");
        tab_det_comp_inv.ejecutarSql();

        tab_cab_comp_inv.insertar();
        tab_cab_comp_inv.setValor("ide_geper", tab_tabla1.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("ide_inepi", p_estado_normal_inv);  /////variable estado normal de inventario
        tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion_inv_venta);   ////variable titpo transaccion compra 
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_cab_comp_inv.setValor("ide_inbod", "1");   ///variable para bodega por defecto
        tab_cab_comp_inv.setValor("numero_incci", in.generarSecuencialComprobanteInventario(p_tipo_transaccion_inv_venta));  /// calcular numero de comprobante de inventario
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_tabla1.getValor("fecha_emisi_cccfa"));
        tab_cab_comp_inv.setValor("observacion_incci", tab_tabla1.getValor("observacion_cccfa"));
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
//        tab_cab_comp_inv.setValor("ide_cnccc", ide_cnccc);
        tab_cab_comp_inv.guardar();

        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            if (es_bien1(tab_tabla2.getValor(i, "ide_inarti"))) {
                tab_det_comp_inv.insertar();
                tab_det_comp_inv.setValor("ide_inarti", tab_tabla2.getValor(i, "ide_inarti"));
                tab_det_comp_inv.setValor("ide_cccfa", tab_tabla1.getValor("ide_cccfa"));
                tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));
                tab_det_comp_inv.setValor("cantidad_indci", tab_tabla2.getValor(i, "cantidad_ccdfa"));
                String precio_prom = in.getPrecioPromedioTransaccionNegativa(tab_tabla2.getValor(i, "ide_inarti"), "1");
                System.out.println("PRECIO PROMEDIO " + precio_prom);
                if (precio_prom != null) {
                    tab_det_comp_inv.setValor("precio_promedio_indci", precio_prom);
                    tab_det_comp_inv.setValor("precio_indci", precio_prom);
                    tab_det_comp_inv.setValor("valor_indci", (Double.parseDouble(precio_prom) * Double.parseDouble(tab_tabla2.getValor(i, "cantidad_ccdfa"))) + "");
                } else {
                    tab_det_comp_inv.setValor("precio_promedio_indci", tab_tabla2.getValor(i, "precio_ccdfa"));
                    tab_det_comp_inv.setValor("precio_indci", tab_tabla2.getValor(i, "precio_ccdfa"));
                    tab_det_comp_inv.setValor("valor_indci", tab_tabla2.getValor(i, "total_ccdfa"));
                }
                tab_det_comp_inv.setValor("observacion_indci", tab_tabla2.getValor(i, "observacion_ccdfa"));
            }
        }
        System.out.println("size 1747 " + utilitario.getConexion().getSqlPantalla().size());
        System.out.println("size 1748 " + utilitario.getConexion().getSqlPantalla().size());
        tab_det_comp_inv.guardar();
        System.out.println("size 1749 " + utilitario.getConexion().getSqlPantalla().size());

    }

    public String obtener_fecha_vencimiento() {
        List dias_sql = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + tab_tabla1.getValor("ide_cndfp"));
        String fecha_venci = "";
        int dias = 0;
        if (dias_sql.get(0) != null) {
            dias = Integer.parseInt(dias_sql.get(0).toString());
        }
        fecha_venci = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_emisi_cccfa")), dias));
        return fecha_venci;
    }

    public void siguiente() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
            tab_tabla3.ejecutarSql();
            cargar_saldo();
            che_solo_guardar.setValue(tab_tabla1.getValor("solo_guardar_cccfa"));
            if (tab_tabla1.isFilaInsertada()) {
                che_solo_guardar.setDisabled(false);
            } else {
                che_solo_guardar.setDisabled(true);
            }
            utilitario.addUpdate("che_solo_guardar");
        }
    }

    public void atras() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
            tab_tabla3.ejecutarSql();
            cargar_saldo();
            che_solo_guardar.setValue(tab_tabla1.getValor("solo_guardar_cccfa"));
            if (tab_tabla1.isFilaInsertada()) {
                che_solo_guardar.setDisabled(false);
            } else {
                che_solo_guardar.setDisabled(true);
            }
            utilitario.addUpdate("che_solo_guardar");
        }
    }

    public void inicio() {

        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
            tab_tabla3.ejecutarSql();
            che_solo_guardar.setValue(tab_tabla1.getValor("solo_guardar_cccfa"));
            if (tab_tabla1.isFilaInsertada()) {
                che_solo_guardar.setDisabled(false);
            } else {
                che_solo_guardar.setDisabled(true);
            }
            utilitario.addUpdate("che_solo_guardar");
            cargar_saldo();
        }
    }

    public void fin() {
        Tabla tabla_foco = utilitario.getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla3.setSql("SELECT ide_ccdtr,fecha_trans_ccdtr,numero_pago_ccdtr,valor_ccdtr,docum_relac_ccdtr,ide_cccfa FROM cxc_detall_transa where numero_pago_ccdtr > 0 and ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
            tab_tabla3.ejecutarSql();
            cargar_saldo();
            che_solo_guardar.setValue(tab_tabla1.getValor("solo_guardar_cccfa"));
            if (tab_tabla1.isFilaInsertada()) {
                che_solo_guardar.setDisabled(false);
            } else {
                che_solo_guardar.setDisabled(true);
            }
            utilitario.addUpdate("che_solo_guardar");
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
        if (rep_reporte.getReporteSelecionado().equals("Facturas")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cccfa", Long.parseLong(tab_tabla1.getValor("ide_cccfa")));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_rep");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Total Ventas")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_cal.dibujar();
                utilitario.addUpdate("rep_reporte,sel_cal");
            } else if (sel_cal.isVisible()) {

                parametro.put("fecha_inicio", sel_cal.getFecha1());
                parametro.put("fecha_final", sel_cal.getFecha2());
                sel_cal.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_cal,sel_rep");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Ventas")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_cal.dibujar();
                utilitario.addUpdate("rep_reporte,sel_cal");
            } else if (sel_cal.isVisible()) {
                parametro.put("fecha_inicio", sel_cal.getFecha1());
                parametro.put("fecha_fin", sel_cal.getFecha2());
                sel_cal.cerrar();
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("sel_cal,sel_rep");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante de Inventario")) {
            if (rep_reporte.isVisible()) {
                List sql_cab_fac_inv = utilitario.getConexion().consultar("SELECT ide_incci  FROM inv_det_comp_inve WHERE ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
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
        } else if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad de Costos")) {
            if (rep_reporte.isVisible()) {
                TablaGenerica tab_cab_comp_inv = utilitario.consultar("select * from inv_cab_comp_inve "
                        + "where ide_incci in (select ide_incci from inv_det_comp_inve where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa") + ") ");
                if (tab_cab_comp_inv.getTotalFilas() > 0) {
                    if (tab_cab_comp_inv.getValor(0, "ide_cnccc") != null && !tab_cab_comp_inv.getValor(0, "ide_cnccc").isEmpty()) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("ide_cnccc", Long.parseLong(tab_cab_comp_inv.getValor(0, "ide_cnccc")));
                        parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                        parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("rep_reporte,sel_rep");
                    } else {
                        utilitario.agregarMensajeInfo("Comprobante de Contabilidad", "La factura seleccionada no tiene Comprobante de Contabilidad de Costos");
                    }
                } else {
                    utilitario.agregarMensajeInfo("No se puede mostrar el comprobante", "La factura seleccionada no tiene Comprobante de Contabilidad de Costos");
                }
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Facturas A6")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_cccfa", Long.parseLong(tab_tabla1.getValor("ide_cccfa")));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_rep");
            }
        }
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

    public Dialogo getDia_banco() {
        return dia_banco;
    }

    public void setDia_banco(Dialogo dia_banco) {
        this.dia_banco = dia_banco;
    }

    public Dialogo getDia_comprobante_rete() {
        return dia_comprobante_rete;
    }

    public void setDia_comprobante_rete(Dialogo dia_comprobante_rete) {
        this.dia_comprobante_rete = dia_comprobante_rete;
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

    public AutoCompletar getAut_transferencia_cuentas() {
        return aut_transferencia_cuentas;
    }

    public void setAut_transferencia_cuentas(AutoCompletar aut_transferencia_cuentas) {
        this.aut_transferencia_cuentas = aut_transferencia_cuentas;
    }
}
