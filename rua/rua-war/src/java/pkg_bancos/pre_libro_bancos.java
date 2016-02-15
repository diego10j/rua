package pkg_bancos;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;
import framework.componentes.VisualizarPDF;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import pkg_contabilidad.VistaAsiento;
import pkg_contabilidad.cls_cab_comp_cont;
import pkg_contabilidad.cls_contabilidad;
import pkg_contabilidad.cls_det_comp_cont;
import pkg_cuentas_x_cobrar.cls_cuentas_x_cobrar;
import pkg_cuentas_x_pagar.cls_cuentas_x_pagar;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_libro_bancos extends Pantalla {

    private Tabla tab_libro_banco = new Tabla();
    private SeleccionTabla set_datos_conciliados = new SeleccionTabla();
    private Tabla tab_cuentas_x_pagar = new Tabla();
    private Tabla tab_cuentas_x_cobrar = new Tabla();
    private Tabla tab_tabla4 = new Tabla(); // detalle comprobante contabilidad
    private Tabla tab_parametros_conciliacion = new Tabla();
    private Tabla tab_beneficiario = new Tabla();
    private Division div_division = new Division();
    private Combo com_bancos = new Combo();
    private String banco_actual = "-1";
    private Combo com_cue_ban = new Combo();
    private SeleccionCalendario sec_calendario = new SeleccionCalendario();
    private Boton bot_cuentas_x_pagar = new Boton();
    private Dialogo dia_cuentas_x_pagar = new Dialogo();
    private Texto tex_num_cxp = new Texto();
    private Combo com_banco_cxp = new Combo();
    private Combo com_tip_tran_cxp = new Combo();
    private Combo com_cuenta_banco_cxp = new Combo();
    private AutoCompletar aut_proveedor = new AutoCompletar();
    private Calendario cal_fecha_pago_cxp = new Calendario();
    private Texto tex_num_cxc = new Texto();
    private Combo com_banco_cxc = new Combo();
    private Combo com_banco_otros = new Combo();
    private Combo com_tip_tran_cxc = new Combo();
    private Combo com_tip_tran_otros = new Combo();
    private Combo com_cuenta_banco_cxc = new Combo();
    private Combo com_cuenta_banco_otros = new Combo();
    private AutoCompletar aut_cliente_cxc = new AutoCompletar();
    private AutoCompletar aut_cliente_otros = new AutoCompletar();
    private Calendario cal_fecha_pago_cxc = new Calendario();
    private Calendario cal_fecha_pago_otros = new Calendario();
    private Texto tex_diferencia = new Texto();
    private Texto tex_diferencia_cxc = new Texto();
    private Texto tex_valor_pagar_cxp = new Texto();
    private Texto tex_valor_pagar_cxc = new Texto();
    private Texto tex_valor_pagar_otros = new Texto();
    private AreaTexto ate_observacion_cxp = new AreaTexto();
    private AreaTexto ate_observacion_cxc = new AreaTexto();
// ************* PARAMETROS **********     
    private String p_tipo_comprobante_contabilidad;
    private String p_tipo_comprobante_egreso = utilitario.getVariable("p_con_tipo_comprobante_egreso");
    private String p_tipo_comprobante_ingreso = utilitario.getVariable("p_con_tipo_comprobante_ingreso");
    private String p_tipo_comprobante_diario = utilitario.getVariable("p_con_tipo_comprobante_diario");
    private String p_estado_comprobante_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private String p_modulo = "7";
    //
    private String nombre_proveedor;
    private String nombre_cliente;
    private Boton bot_cuentas_x_cobrar = new Boton();
    private Dialogo dia_cuentas_x_cobrar = new Dialogo();
    private String provee_actual = "-1";
    private String cliente_actual = "-1";
    private Etiqueta eti_diferencia = new Etiqueta();
    private Etiqueta eti_diferencia_cxc = new Etiqueta();
    private Confirmar con_guardar = new Confirmar();
    private VisualizarPDF vis_pdf = new VisualizarPDF();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Boton bot_otros = new Boton();
    private Dialogo dia_otros = new Dialogo();
//
    private Texto tex_num_otros = new Texto();
    private AreaTexto ate_observacion_otros = new AreaTexto();
    private Etiqueta eti_suma_debe = new Etiqueta();
    private Etiqueta eti_suma_haber = new Etiqueta();
    private Etiqueta eti_suma_diferencia = new Etiqueta();
    private Etiqueta eti_saldo = new Etiqueta();
    // boton transferir
    private Boton bot_transferir = new Boton();
    private Dialogo dia_transferir = new Dialogo();
    private Texto tex_valor_transf = new Texto();
    private Texto tex_num_transf = new Texto();
    private Calendario cal_fecha_transf = new Calendario();
    private Texto tex_observacion_tranf = new Texto();
    private Combo com_banco_transf = new Combo();
    private Combo com_cuenta_banco_transf = new Combo();
    private Combo com_tip_tran_transf = new Combo();
    private Combo com_banco_transf_2 = new Combo();
    private Combo com_cuenta_banco_transf_2 = new Combo();
    private VistaAsiento via_asiento = new VistaAsiento();
    // totales dialogo otros
    private Grid gri_totales_otros = new Grid();
    private Etiqueta eti_suma_debe_otros = new Etiqueta();
    private Etiqueta eti_suma_haber_otros = new Etiqueta();
    private Etiqueta eti_suma_diferencia_otros = new Etiqueta();
    //
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
    cls_bancos banco = new cls_bancos();
    // conciliacion
    private Boton bot_conciliar = new Boton();
    //private Upload uplo = new Upload();
    private Dialogo dia_conciliar = new Dialogo();
    private Radio rad_tipo_conciliacion = new Radio();
    private Upload upl_archivo = new Upload();
    //
    cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
    cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
    private Dialogo dia_cambio_documento = new Dialogo();
    private Texto tex_num_documento = new Texto();
    private Texto tex_num_documento_actual = new Texto();
    private Etiqueta eti_num_doc_actual = new Etiqueta();
    private Etiqueta eti_num_doc = new Etiqueta();
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private Dialogo dia_filtro_activo = new Dialogo();
    private Check che_conciliados = new Check();
    private Check che_no_conciliados = new Check();

//
    public pre_libro_bancos() {
        //Recuperar el plan de cuentas activo
        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        if (lis_plan != null && !lis_plan.isEmpty()) {
            bar_botones.agregarCalendario();

            bar_botones.agregarComponente(new Etiqueta("Banco: "));
            bar_botones.agregarComponente(com_bancos);
            bar_botones.agregarComponente(new Etiqueta("Cuenta Banco: "));
            com_cue_ban.setId("com_cue_ban"); 
            bar_botones.agregarComponente(com_cue_ban);

            bot_cuentas_x_pagar.setId("bot_cuentas_x_pagar");
            bot_cuentas_x_pagar.setValue("Cuenta x Pagar");
            bot_cuentas_x_pagar.setMetodo("abrirDialogoCxP");
            bot_cuentas_x_pagar.setUpdate("dia_cuentas_x_pagar");

            bot_cuentas_x_cobrar.setId("bot_cuentas_x_cobrar");
            bot_cuentas_x_cobrar.setValue("Cuentas x Cobrar");
            bot_cuentas_x_cobrar.setMetodo("abrirDialogoCxC");
            bot_cuentas_x_cobrar.setUpdate("dia_cuentas_x_cobrar");

            bot_transferir.setId("bot_transferir");
            bot_transferir.setValue("Transferir");
            bot_transferir.setMetodo("abrirDialogoTransferir");

//            bot_pago_salud.setId("bot_pago_salud");
//            bot_pago_salud.setValue("Pago Salud");
//            bot_pago_salud.setMetodo("abrirDialogoPagoSalud");
            bar_botones.agregarBoton(bot_cuentas_x_pagar);
            bar_botones.agregarBoton(bot_cuentas_x_cobrar);
            bar_botones.agregarBoton(bot_otros);
            bar_botones.agregarBoton(bot_transferir);
//            bar_botones.agregarBoton(bot_pago_salud);
            bar_botones.agregarReporte();

// ************  CONFIGURACION DEL BOTON CUENTAS POR PAGAR
            dia_cuentas_x_pagar.setId("dia_cuentas_x_pagar");
            dia_cuentas_x_pagar.setTitle("Pagos a Proveedores");
            dia_cuentas_x_pagar.setWidth("70%");
            dia_cuentas_x_pagar.setHeight("65%");
            dia_cuentas_x_pagar.setDynamic(false);

            Etiqueta eti_banco = new Etiqueta();
            Etiqueta eti_cuenta_banco = new Etiqueta();
            Etiqueta eti_num_cheque = new Etiqueta();
            Etiqueta eti_observacion = new Etiqueta();
            Etiqueta eti_proveedor = new Etiqueta();
            Etiqueta eti_tipo_trans = new Etiqueta();
            Etiqueta eti_fecha_pago = new Etiqueta();

            eti_banco.setValue("Banco ");
            eti_cuenta_banco.setValue("Cuenta Bancaria ");
            eti_num_cheque.setValue("Num ");
            eti_observacion.setValue("Observacion ");
            eti_proveedor.setValue("Proveedor ");
            eti_tipo_trans.setValue("Tipo transaccion ");
            eti_fecha_pago.setValue("Fecha Pago ");

            tex_num_cxp.setId("tex_num_cxp");
            com_banco_cxp.setId("com_banco_cxp");
            com_banco_cxp.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco_cxp.setMetodo("cargar_cuentas_cxp");
            com_tip_tran_cxp.setCombo("select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "and signo_tettb=-1");
            //com_tip_tran_cxp.eliminarVacio();
            com_tip_tran_cxp.setMetodo("cambioTipoTransBancCxP");

            com_cuenta_banco_cxp.setId("com_cuenta_banco_cxp");
            com_cuenta_banco_cxp.setMetodo("obtenerNumeroChequeCxP");

            aut_proveedor.setId("aut_proveedor");
            aut_proveedor.setAutoCompletar("select ide_geper,nom_geper from gen_persona where es_proveedo_geper is TRUE and ide_empr=" + utilitario.getVariable("ide_empr"));
            aut_proveedor.setMetodoChange("cargarCuentasporPagar");

            cal_fecha_pago_cxp.setValue(utilitario.getDate());

            bot_otros.setId("bot_otros");
            bot_otros.setValue("Otros");
            bot_otros.setMetodo("abrirDialogoOtros");
            bot_otros.setUpdate("dia_otros");

            tab_cuentas_x_pagar.setId("tab_cuentas_x_pagar");
            tab_cuentas_x_pagar.setSql(cxp.getSqlCuentasPorPagar("-1"));
            tab_cuentas_x_pagar.getColumna("saldo_x_pagar").setEstilo("color:red; font-size: 17px;font-weight: bold;text-decoration: underline");
            tab_cuentas_x_pagar.setColumnaSuma("saldo_x_pagar");
            tab_cuentas_x_pagar.setCampoPrimaria("ide_cpctr");
            tab_cuentas_x_pagar.onSelectCheck("seleccionaFacturaCxP");
            tab_cuentas_x_pagar.onUnselectCheck("deseleccionaFacturaCxP");
            tab_cuentas_x_pagar.setTipoSeleccion(true);
            tab_cuentas_x_pagar.setLectura(true);

            tab_cuentas_x_pagar.dibujar();

            PanelTabla pat_panel_cxp = new PanelTabla();
            pat_panel_cxp.setPanelTabla(tab_cuentas_x_pagar);
            pat_panel_cxp.getMenuTabla().getItem_insertar().setRendered(false);
            pat_panel_cxp.getMenuTabla().getItem_eliminar().setRendered(false);
            pat_panel_cxp.getMenuTabla().getItem_guardar().setRendered(false);

            ate_observacion_cxp.setCols(80);
            Grid grid_matriz = new Grid();
            grid_matriz.setColumns(4);

            grid_matriz.getChildren().add(eti_proveedor);
            grid_matriz.getChildren().add(aut_proveedor);
            grid_matriz.getChildren().add(eti_tipo_trans);
            grid_matriz.getChildren().add(com_tip_tran_cxp);
            grid_matriz.getChildren().add(eti_banco);
            grid_matriz.getChildren().add(com_banco_cxp);
            grid_matriz.getChildren().add(eti_cuenta_banco);
            grid_matriz.getChildren().add(com_cuenta_banco_cxp);
            grid_matriz.getChildren().add(eti_num_cheque);
            grid_matriz.getChildren().add(tex_num_cxp);
            grid_matriz.getChildren().add(eti_fecha_pago);
            grid_matriz.getChildren().add(cal_fecha_pago_cxp);
            grid_matriz.getChildren().add(eti_observacion);
            grid_matriz.getChildren().add(ate_observacion_cxp);

            Grid grid_pago = new Grid();
            grid_pago.setColumns(4);
            Etiqueta eti_valor_pagar = new Etiqueta();
            eti_valor_pagar.setValue("Valor a Pagar");
            tex_diferencia.setId("tex_diferencia");
            eti_diferencia.setStyle("font-size: 15px;font-weight: bold;text-decoration: underline");
            tex_diferencia.setStyle("font-size: 15px;font-weight: bold");
            tex_diferencia.setDisabled(true);
            tex_diferencia.setSoloNumeros();
            eti_diferencia.setValue("Diferencia ");
            tex_valor_pagar_cxp.setId("tex_valor_pagar_cxp");
            tex_valor_pagar_cxp.setMetodoKeyPress("calcular_diferencia_cxp");
            eti_valor_pagar.setStyle("font-size: 15px;font-weight: bold;text-decoration: underline");
            tex_valor_pagar_cxp.setStyle("font-size: 15px;font-weight: bold");
            grid_pago.getChildren().add(eti_valor_pagar);
            grid_pago.getChildren().add(tex_valor_pagar_cxp);
            grid_pago.getChildren().add(eti_diferencia);
            grid_pago.getChildren().add(tex_diferencia);

            Grid grid_dialogo = new Grid();
            grid_dialogo.setStyle("width:" + (dia_cuentas_x_pagar.getAnchoPanel() - 5) + "px; height:" + dia_cuentas_x_pagar.getAltoPanel() + "px;overflow:auto;display:block;");
            grid_dialogo.getChildren().add(grid_matriz);
            grid_dialogo.getChildren().add(grid_pago);
            grid_dialogo.getChildren().add(pat_panel_cxp);

            dia_cuentas_x_pagar.setDialogo(grid_dialogo);
            dia_cuentas_x_pagar.getBot_aceptar().setMetodo("aceptarDialogoCxP");
            dia_cuentas_x_pagar.getBot_cancelar().setMetodo("cancelarDialogo");

// **********************  Configuracion del boton cuentas x cobrar
            dia_cuentas_x_cobrar.setId("dia_cuentas_x_cobrar");
            dia_cuentas_x_cobrar.setTitle("Cobro a Clientes");
            dia_cuentas_x_cobrar.setWidth("70%");
            dia_cuentas_x_cobrar.setHeight("65%");
            dia_cuentas_x_cobrar.setDynamic(false);

            Etiqueta eti_banco_cxc = new Etiqueta();
            Etiqueta eti_cuenta_banco_cxc = new Etiqueta();
            Etiqueta eti_observacion_cxc = new Etiqueta();
            Etiqueta eti_cliente_cxc = new Etiqueta();
            Etiqueta eti_tipo_trans_cxc = new Etiqueta();
            Etiqueta eti_fecha_pago_cxc = new Etiqueta();
            Etiqueta eti_num_cxc = new Etiqueta();
            tex_num_cxc.setId("tex_num_cxc");

            eti_banco_cxc.setValue("Banco ");
            eti_cuenta_banco_cxc.setValue("Cuenta Bancaria ");
            eti_observacion_cxc.setValue("Observacion ");
            eti_cliente_cxc.setValue("Cliente ");
            eti_tipo_trans_cxc.setValue("Tipo transaccion ");
            eti_fecha_pago_cxc.setValue("Fecha Pago ");
            eti_num_cxc.setValue("Numero ");
            eti_fecha_pago_cxc.setValue("Fecha Pago ");
            com_banco_cxc.setId("com_banco_cxc");
            com_banco_cxc.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco_cxc.setMetodo("cargar_cuentas_cxc");
            com_cuenta_banco_cxc.setMetodo("obtenerNumeroChequeCxC");
            com_tip_tran_cxc.setCombo("select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "and signo_tettb=1");
            com_tip_tran_cxc.setMetodo("cambioTipoTransBancCxC");

            com_cuenta_banco_cxc.setId("com_cuenta_banco_cxc");
            aut_cliente_cxc.setId("aut_cliente_cxc");
            aut_cliente_cxc.setAutoCompletar("select ide_geper,nom_geper from gen_persona where es_cliente_geper is TRUE and ide_empr=" + utilitario.getVariable("ide_empr"));
            aut_cliente_cxc.setMetodoChange("cargarCuentasporCobrar");
            cal_fecha_pago_cxc.setValue(utilitario.getDate());
            ate_observacion_cxc.setCols(80);
            tab_cuentas_x_cobrar.setId("tab_cuentas_x_cobrar");

            tab_cuentas_x_cobrar.setSql(cxc.getSqlCuentasPorCobrar("-1"));
            tab_cuentas_x_cobrar.getColumna("saldo_x_pagar").setEstilo("color:red; font-size: 17px;font-weight: bold;text-decoration: underline");
            tab_cuentas_x_cobrar.setCampoPrimaria("ide_ccctr");
            tab_cuentas_x_cobrar.setLectura(true);
            tab_cuentas_x_cobrar.setTipoSeleccion(true);
            tab_cuentas_x_cobrar.setCondicion("ide_ccctr=-1");
            tab_cuentas_x_cobrar.setColumnaSuma("saldo_x_pagar");
            tab_cuentas_x_cobrar.onSelectCheck("seleccionaFacturaCxC");
            tab_cuentas_x_cobrar.onUnselectCheck("deseleccionaFacturaCxC");
            tab_cuentas_x_cobrar.dibujar();

            Grid grid_matriz_ingreso = new Grid();
            grid_matriz_ingreso.setColumns(4);

            grid_matriz_ingreso.getChildren().add(eti_cliente_cxc);
            grid_matriz_ingreso.getChildren().add(aut_cliente_cxc);
            grid_matriz_ingreso.getChildren().add(eti_tipo_trans_cxc);
            grid_matriz_ingreso.getChildren().add(com_tip_tran_cxc);
            grid_matriz_ingreso.getChildren().add(eti_banco_cxc);
            grid_matriz_ingreso.getChildren().add(com_banco_cxc);
            grid_matriz_ingreso.getChildren().add(eti_cuenta_banco_cxc);
            grid_matriz_ingreso.getChildren().add(com_cuenta_banco_cxc);
            grid_matriz_ingreso.getChildren().add(eti_num_cxc);
            grid_matriz_ingreso.getChildren().add(tex_num_cxc);
            grid_matriz_ingreso.getChildren().add(eti_fecha_pago_cxc);
            grid_matriz_ingreso.getChildren().add(cal_fecha_pago_cxc);
            grid_matriz_ingreso.getChildren().add(eti_observacion_cxc);
            grid_matriz_ingreso.getChildren().add(ate_observacion_cxc);

            Grid grid_cobro = new Grid();
            grid_cobro.setColumns(4);
            Etiqueta eti_valor_cobrar = new Etiqueta();
            eti_valor_cobrar.setValue("Valor a Cobrar");
            tex_diferencia_cxc.setId("tex_diferencia_cxc");
            eti_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold;text-decoration: underline");
            tex_diferencia_cxc.setStyle("font-size: 14px;font-weight: bold");
            tex_diferencia_cxc.setDisabled(true);
            tex_diferencia_cxc.setSoloNumeros();
            eti_diferencia_cxc.setValue("Diferencia ");
            tex_valor_pagar_cxc.setId("tex_valor_pagar_cxc");
            tex_valor_pagar_cxc.setMetodoKeyPress("calcular_diferencia_cxc");
            eti_valor_cobrar.setStyle("font-size: 14px;font-weight: bold;text-decoration: underline");
            tex_valor_pagar_cxc.setStyle("font-size: 14px;font-weight: bold");
            grid_cobro.getChildren().add(eti_valor_cobrar);
            grid_cobro.getChildren().add(tex_valor_pagar_cxc);
            grid_cobro.getChildren().add(eti_diferencia_cxc);
            grid_cobro.getChildren().add(tex_diferencia_cxc);

            Grid grid_dialogo1 = new Grid();
            grid_dialogo1.setStyle("width:" + (dia_cuentas_x_cobrar.getAnchoPanel() - 5) + "px; height:" + dia_cuentas_x_cobrar.getAltoPanel() + "px;overflow:auto;display:block;");
            grid_dialogo1.getChildren().add(grid_matriz_ingreso);
            grid_dialogo1.getChildren().add(grid_cobro);
            grid_dialogo1.getChildren().add(tab_cuentas_x_cobrar);

            dia_cuentas_x_cobrar.setDialogo(grid_dialogo1);
            dia_cuentas_x_cobrar.getBot_aceptar().setMetodo("aceptarDialogoCxC");
            dia_cuentas_x_cobrar.getBot_cancelar().setMetodo("cancelarDialogo");

// **********************  Configuracion del boton OTROS
            dia_otros.setId("dia_otros");
            dia_otros.setTitle("Otros");
            dia_otros.setWidth("70%");
            dia_otros.setHeight("65%");
            dia_otros.setDynamic(false);

            Etiqueta eti_banco_ings = new Etiqueta();
            Etiqueta eti_cuenta_banco_ings = new Etiqueta();
            Etiqueta eti_observacion_ings = new Etiqueta();
            Etiqueta eti_cliente_ings = new Etiqueta();
            Etiqueta eti_tipo_trans_ings = new Etiqueta();
            Etiqueta eti_fecha_pago_ings = new Etiqueta();
            Etiqueta eti_num = new Etiqueta();
            Etiqueta eti_obs = new Etiqueta();

            Etiqueta eti_valor_cobrars = new Etiqueta();
            eti_valor_cobrars.setValue("Valor ");
            tex_valor_pagar_otros.setSoloNumeros();
            tex_valor_pagar_otros.setId("tex_valor_pagar_otros");

            eti_banco_ings.setValue("Banco ");
            eti_cuenta_banco_ings.setValue("Cuenta Bancaria ");
            eti_observacion_ings.setValue("Observacion ");
            eti_cliente_ings.setValue("Beneficiario ");
            eti_tipo_trans_ings.setValue("Tipo transaccion ");
            eti_fecha_pago_ings.setValue("Fecha Pago ");
            eti_num.setValue("Numero ");
            eti_obs.setValue("Observacion ");
            com_banco_otros.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco_otros.setMetodo("cargar_cuentas_otros");
            com_tip_tran_otros.setCombo("select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "");

            com_tip_tran_otros.setMetodo("cambioTipoTransBancOtros");
            com_cuenta_banco_otros.setId("com_cuenta_banco_otros");
            com_cuenta_banco_otros.setMetodo("obtenerNumeroChequeOtros");
            aut_cliente_otros.setId("aut_cliente_otros");
            aut_cliente_otros.setAutoCompletar("select ide_geper,nom_geper from gen_persona where ide_empr=" + utilitario.getVariable("ide_empr"));
            cal_fecha_pago_otros.setValue(utilitario.getDate());
            tex_valor_pagar_otros.setMetodoChange("cargar_valor");
            tex_num_otros.setId("tex_num_otros");

            ate_observacion_otros.setCols(60);
            Grid grid_matriz_otros = new Grid();
            grid_matriz_otros.setColumns(4);

            grid_matriz_otros.getChildren().add(eti_cliente_ings);
            grid_matriz_otros.getChildren().add(aut_cliente_otros);
            grid_matriz_otros.getChildren().add(eti_fecha_pago_ings);
            grid_matriz_otros.getChildren().add(cal_fecha_pago_otros);
            grid_matriz_otros.getChildren().add(eti_banco_ings);
            grid_matriz_otros.getChildren().add(com_banco_otros);
            grid_matriz_otros.getChildren().add(eti_tipo_trans_ings);
            grid_matriz_otros.getChildren().add(com_tip_tran_otros);
            grid_matriz_otros.getChildren().add(eti_cuenta_banco_ings);
            grid_matriz_otros.getChildren().add(com_cuenta_banco_otros);
            grid_matriz_otros.getChildren().add(eti_num);
            grid_matriz_otros.getChildren().add(tex_num_otros);
            grid_matriz_otros.getChildren().add(eti_obs);
            grid_matriz_otros.getChildren().add(ate_observacion_otros);
            grid_matriz_otros.getChildren().add(eti_valor_cobrars);
            grid_matriz_otros.getChildren().add(tex_valor_pagar_otros);
//
            tab_tabla4.setId("tab_tabla4");
            tab_tabla4.setTabla("con_det_comp_cont", "ide_cndcc", 3);
            tab_tabla4.setCondicion("ide_cndcc=-1");
            tab_tabla4.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=" + lis_plan.get(0));
            tab_tabla4.getColumna("ide_cndpc").setAutoCompletar();
            tab_tabla4.getColumna("ide_cnlap").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
            tab_tabla4.getColumna("ide_cnlap").setRequerida(true);
            tab_tabla4.getColumna("ide_cnlap").setPermitirNullCombo(false);
            tab_tabla4.getColumna("ide_cnccc").setVisible(false);
            tab_tabla4.getColumna("ide_cndcc").setVisible(false);
            tab_tabla4.getColumna("valor_cndcc").setMetodoChange("ingresaCantidad");
            tab_tabla4.getColumna("ide_cnlap").setMetodoChange("cambioLugarAplica");
            tab_tabla4.setCampoOrden("ide_cnlap desc");
            tab_tabla4.getColumna("ide_cndpc").setRequerida(true);
            tab_tabla4.getColumna("valor_cndcc").setRequerida(true);
            tab_tabla4.setCondicion("ide_cndcc=-1");
            tab_tabla4.dibujar();

            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_tabla4);

            gri_totales_otros.setId("gri_totales_otros");
            gri_totales_otros.setColumns(3);
            eti_suma_debe_otros.setValue("TOTAL DEBE : 0");
            eti_suma_debe_otros.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_haber_otros.setValue("TOTAL HABER : 0");
            eti_suma_haber_otros.setStyle("font-size: 14px;font-weight: bold");
            eti_suma_diferencia_otros.setValue("DIFERENCIA : 0");
            eti_suma_diferencia_otros.setStyle("font-size: 14px;font-weight: bold");
            gri_totales_otros.getChildren().add(eti_suma_debe_otros);
            gri_totales_otros.getChildren().add(eti_suma_haber_otros);
            gri_totales_otros.getChildren().add(eti_suma_diferencia_otros);

            Grid grid_dialogo2 = new Grid();
            grid_dialogo2.setStyle("width:" + (dia_otros.getAnchoPanel() - 5) + "px; height:" + dia_otros.getAltoPanel() + "px;overflow:auto;display:block;");
            grid_dialogo2.getChildren().add(grid_matriz_otros);
            grid_dialogo2.getChildren().add(pat_panel2);
            grid_dialogo2.getChildren().add(gri_totales_otros);

            dia_otros.setDialogo(grid_dialogo2);
            dia_otros.getBot_aceptar().setMetodo("aceptarDialogoOtros");
            dia_otros.getBot_cancelar().setMetodo("cancelarDialogo");

//  CONFIGURACION COMPROBANTE CONTABILIDAD
            via_asiento.setId("via_asiento");
            via_asiento.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
            via_asiento.getBot_cancelar().setMetodo("cancelarDialogo");
            via_asiento.setDynamic(false);

            agregarComponente(via_asiento);

// *********  CONFIGURACION BOTON TRANSFERIR
            dia_transferir.setId("dia_transferir");
            dia_transferir.setTitle("Transferir");
            dia_transferir.setWidth("45%");
            dia_transferir.setHeight("55%");
            dia_transferir.setDynamic(false);

            Etiqueta eti_banco_transf = new Etiqueta();
            Etiqueta eti_cuenta_banco_transf = new Etiqueta();
            Etiqueta eti_banco_transf_2 = new Etiqueta();
            Etiqueta eti_cuenta_banco_transf_2 = new Etiqueta();
            Etiqueta eti_observacion_transf = new Etiqueta();
            Etiqueta eti_tipo_trans_transf = new Etiqueta();
            Etiqueta eti_fecha_transf = new Etiqueta();
            Etiqueta eti_valor_transf = new Etiqueta();
            Etiqueta eti_num_transf = new Etiqueta();

            tex_valor_transf.setSoloNumeros();
            tex_valor_transf.setId("tex_valor_transf");
            tex_valor_transf.setSoloNumeros();
            eti_banco_transf.setValue("Banco Origen ");
            eti_cuenta_banco_transf.setValue("Cta Banco Origen ");
            eti_banco_transf_2.setValue("Banco Destino ");
            eti_cuenta_banco_transf_2.setValue("Cta Banco Destino ");
            eti_observacion_transf.setValue("Observacion ");
            eti_tipo_trans_transf.setValue("Tipo transaccion ");
            eti_fecha_transf.setValue("Fecha ");
            eti_valor_transf.setValue("Valor ");
            eti_num_transf.setValue("Numero ");
            com_banco_transf.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco_transf.setMetodo("cargar_cuentas_transferencia");
            com_tip_tran_transf.setCombo("select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "");
            com_tip_tran_transf.setValue(utilitario.getVariable("p_tes_tran_transferencia_menos"));
            com_tip_tran_transf.eliminarVacio();
            com_tip_tran_transf.setDisabled(true);
            com_cuenta_banco_transf.setId("com_cuenta_banco_transf");
            com_cuenta_banco_transf.setMetodo("cargar_cuenta_escogida");
            com_banco_transf_2.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_banco_transf_2.setMetodo("cargar_cuentas_transferencia_2");
            com_cuenta_banco_transf_2.setId("com_cuenta_banco_transf_2");
            com_banco_transf_2.setId("com_banco_transf_2");
            com_banco_transf.setId("com_banco_transf");
            com_cuenta_banco_transf_2.setMetodo("cargar_cuenta_escogida_2");

            cal_fecha_transf.setValue(utilitario.getDate());

            tex_observacion_tranf.setSize(70);

            Grid grid_matriz_origen = new Grid();
            grid_matriz_origen.setColumns(2);

            grid_matriz_origen.getChildren().add(eti_banco_transf);
            grid_matriz_origen.getChildren().add(com_banco_transf);
            grid_matriz_origen.getChildren().add(eti_cuenta_banco_transf);
            grid_matriz_origen.getChildren().add(com_cuenta_banco_transf);

            Grid grid_matriz_destino = new Grid();
            grid_matriz_destino.setColumns(2);

            grid_matriz_destino.getChildren().add(eti_banco_transf_2);
            grid_matriz_destino.getChildren().add(com_banco_transf_2);
            grid_matriz_destino.getChildren().add(eti_cuenta_banco_transf_2);
            grid_matriz_destino.getChildren().add(com_cuenta_banco_transf_2);

            Grid grid_matriz_union = new Grid();
            grid_matriz_union.setColumns(2);
            grid_matriz_union.getChildren().add(grid_matriz_origen);
            grid_matriz_union.getChildren().add(grid_matriz_destino);

            Grid grid_matriz_extras = new Grid();
            grid_matriz_extras.setColumns(2);
            grid_matriz_extras.getChildren().add(eti_tipo_trans_transf);
            grid_matriz_extras.getChildren().add(com_tip_tran_transf);
            grid_matriz_extras.getChildren().add(eti_num_transf);
            grid_matriz_extras.getChildren().add(tex_num_transf);
            grid_matriz_extras.getChildren().add(eti_valor_transf);
            grid_matriz_extras.getChildren().add(tex_valor_transf);
            grid_matriz_extras.getChildren().add(eti_fecha_transf);
            grid_matriz_extras.getChildren().add(cal_fecha_transf);
            grid_matriz_extras.getChildren().add(eti_observacion_transf);
            grid_matriz_extras.getChildren().add(tex_observacion_tranf);

            Grid grid_matriz_transferir = new Grid();
            grid_matriz_transferir.setColumns(1);

            grid_matriz_transferir.getChildren().add(grid_matriz_union);
            grid_matriz_transferir.getChildren().add(grid_matriz_extras);

            Grid grid_dialogo_transf = new Grid();
            grid_dialogo_transf.setStyle("width:" + (dia_transferir.getAnchoPanel() - 5) + "px; height:" + dia_transferir.getAltoPanel() + "px;overflow:auto;display:block;");
            grid_dialogo_transf.getChildren().add(grid_matriz_transferir);

            dia_transferir.setDialogo(grid_dialogo_transf);
            dia_transferir.getBot_aceptar().setMetodo("aceptarDialogoTransferir");
            dia_transferir.getBot_cancelar().setMetodo("cancelarDialogo");

            // configuracion boton conciliar  
            bot_conciliar.setId("bot_conciliar");
            bot_conciliar.setValue("Conciliar");
            bot_conciliar.setMetodo("abrirDialogoConciliar");

            bar_botones.agregarBoton(bot_conciliar);

            tab_parametros_conciliacion.setId("tab_parametros_conciliacion");
            tab_parametros_conciliacion.setTabla("tes_configura_conciliacion", "ide_tecco", 4);
            tab_parametros_conciliacion.getColumna("num_colum_doc_tecco").setNombreVisual("N.Col. Documento");
            tab_parametros_conciliacion.getColumna("num_colum_monto_tecco").setNombreVisual("N.Col. Monto");
            tab_parametros_conciliacion.getColumna("ide_teban").setVisible(false);
            tab_parametros_conciliacion.setCondicion("ide_tecco=-1");
            tab_parametros_conciliacion.setLectura(true);
            tab_parametros_conciliacion.dibujar();

            List lis_tipo_conciliacion = new ArrayList();
            Object fila1[] = {
                "2", "Por Num. Documento y Valor"
            };
            Object fila2[] = {
                "1", "Solo por Valor"
            };
            lis_tipo_conciliacion.add(fila1);
            lis_tipo_conciliacion.add(fila2);

            rad_tipo_conciliacion.setRadio(lis_tipo_conciliacion);
            rad_tipo_conciliacion.setValue("1");

            dia_conciliar.setId("dia_conciliar");
            dia_conciliar.setTitle("Conciliacion");
            dia_conciliar.setWidth("45%");
            dia_conciliar.setHeight("55%");
            dia_conciliar.setDynamic(false);

            Etiqueta eti_sube_archivo = new Etiqueta();
            eti_sube_archivo.setValue("Seleccione el Archivo .txt del banco a conciliar: ");
            eti_sube_archivo.setStyle("font-size: 14px;font-weight: bold");

            Etiqueta eti_para_conci = new Etiqueta();
            eti_para_conci.setValue("Configuracion Parametros de Conciliacion");
            eti_para_conci.setStyle("font-size: 14px;font-weight: bold");

            Etiqueta eti_tipo_conci = new Etiqueta();
            eti_tipo_conci.setValue("Tipo de Busqueda: ");
            eti_tipo_conci.setStyle("font-size: 14px;font-weight: bold");

            upl_archivo.setId("upl_archivo");

            Grid grid_dialogo_conciliar = new Grid();
            grid_dialogo_conciliar.setStyle("width:" + (dia_conciliar.getAnchoPanel() - 5) + "px; height:" + dia_conciliar.getAltoPanel() + "px;overflow:auto;display:block;");
            grid_dialogo_conciliar.getChildren().add(eti_sube_archivo);
            grid_dialogo_conciliar.getChildren().add(upl_archivo);
            grid_dialogo_conciliar.getChildren().add(eti_tipo_conci);
            grid_dialogo_conciliar.getChildren().add(rad_tipo_conciliacion);
            grid_dialogo_conciliar.getChildren().add(eti_para_conci);
            grid_dialogo_conciliar.getChildren().add(tab_parametros_conciliacion);

            dia_conciliar.setDialogo(grid_dialogo_conciliar);
            dia_conciliar.getBot_aceptar().setMetodo("aceptarDialogoConciliar");
            dia_conciliar.getBot_cancelar().setMetodo("cancelarDialogo");
            agregarComponente(dia_conciliar);

            set_datos_conciliados.setId("set_datos_conciliados");
            set_datos_conciliados.setSeleccionTabla("select ide_teclb,fecha_trans_teclb,nombre_tettb,numero_teclb,valor_teclb,beneficiari_teclb,observacion_teclb from tes_cab_libr_banc clb "
                    + "left join tes_cuenta_banco tcb on clb.ide_tecba=tcb.ide_tecba "
                    + "left join tes_tip_tran_banc ttb on clb.ide_tettb=ttb.ide_tettb "
                    + "left join tes_estado_libro_banco elb on clb.ide_teelb=elb.ide_teelb "
                    + "where clb.ide_teclb in (-1) ", "ide_teclb");
            set_datos_conciliados.getBot_aceptar().setMetodo("aceptarDatosConciliados");
            set_datos_conciliados.getBot_cancelar().setMetodo("cancelarDialogo");
            set_datos_conciliados.setTitle("Valores encontrados para la Conciliacion");
            set_datos_conciliados.setWidth("80%");
            set_datos_conciliados.setHeight("80%");
            agregarComponente(set_datos_conciliados);

            // **************     configuracion de libro bancos 
            com_cue_ban.setId("com_cue_ban");
            com_cue_ban.setCombo(new ArrayList());
            com_cue_ban.setMetodo("filtrar");

            com_bancos.setId("com_bancos");
            com_bancos.setCombo("select ide_teban,nombre_teban from tes_banco where ide_empr=" + utilitario.getVariable("ide_empr"));
            com_bancos.setMetodo("cargar_cuentas_bancarias");

            sec_calendario.setFecha2(utilitario.getFecha(utilitario.getFechaActual()));
            sec_calendario.setFecha1(utilitario.sumarDiasFecha(utilitario.getFecha(utilitario.getFechaActual()), -(Integer.parseInt(utilitario.getVariable("p_tes_num_dias_filtro")))));

            tab_libro_banco.setId("tab_libro_banco");
            tab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", 1);
            tab_libro_banco.getColumna("ide_tecba").setVisible(false);
            tab_libro_banco.getColumna("ide_tettb").setCombo("tes_tip_tran_banc", "ide_tettb", "nombre_tettb", "");
            tab_libro_banco.getColumna("ide_tettb").setValorDefecto("5");
            tab_libro_banco.getColumna("ide_teelb").setCombo("tes_estado_libro_banco", "ide_teelb", "nombre_teelb", "");
            tab_libro_banco.getColumna("ide_teelb").setValorDefecto("0");
            tab_libro_banco.getColumna("fecha_trans_teclb").setValorDefecto(utilitario.getFechaActual());
            tab_libro_banco.getColumna("fecha_venci_teclb").setValorDefecto(utilitario.getFechaActual());
            tab_libro_banco.getColumna("ide_cnccc").setLectura(true);
            tab_libro_banco.getColumna("fec_cam_est_teclb").setVisible(false);
            tab_libro_banco.setLectura(true);
            tab_libro_banco.setCampoOrden("ide_teclb desc");
            tab_libro_banco.setCondicionSucursal(false);
            tab_libro_banco.setCondicion("ide_teclb=-1 and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal"));
            tab_libro_banco.setRows(15);
            tab_libro_banco.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_libro_banco);

            ItemMenu itm_cambiar_valor = new ItemMenu();
            itm_cambiar_valor.setValue("Cambiar Valor");
            itm_cambiar_valor.setMetodo("modificarValorTransaccion");
            itm_cambiar_valor.setIcon("ui-icon-contact");
            pat_panel.getMenuTabla().getChildren().add(itm_cambiar_valor);

            ItemMenu itm_cambiar_numero_doc = new ItemMenu();
            itm_cambiar_numero_doc.setValue("Cambiar Num Doc");
            itm_cambiar_numero_doc.setIcon("ui-icon-contact");
            itm_cambiar_numero_doc.setMetodo("modificarNumeroDocumento");
            pat_panel.getMenuTabla().getChildren().add(itm_cambiar_numero_doc);

            ItemMenu itm_cambiar_estado_conciliado = new ItemMenu();
            itm_cambiar_estado_conciliado.setValue("Cambiar Estado Conciliado");
            itm_cambiar_estado_conciliado.setIcon("ui-icon-contact");
            itm_cambiar_estado_conciliado.setMetodo("modificarEstadoConciliado");
            pat_panel.getMenuTabla().getChildren().add(itm_cambiar_estado_conciliado);

            dia_cambio_documento.setId("dia_cambio_documento");
            dia_cambio_documento.setTitle("CAMBIO NUMERO DE DOCUMENTO");
            dia_cambio_documento.setWidth("30%");
            dia_cambio_documento.setHeight("30%");
            dia_cambio_documento.setDynamic(false);

            eti_num_doc_actual = new Etiqueta();
            eti_num_doc_actual.setStyle("font-size: 14px;font-weight: bold");

            eti_num_doc = new Etiqueta();
            eti_num_doc.setStyle("font-size: 14px;font-weight: bold");

            tex_num_documento_actual.setDisabled(true);
            Grid grid_dialogo_cambio_doc = new Grid();
            grid_dialogo_cambio_doc.setColumns(2);
            grid_dialogo_cambio_doc.setStyle("width:" + (dia_cambio_documento.getAnchoPanel() - 5) + "px; height:" + dia_cambio_documento.getAltoPanel() + "px;overflow:auto;display:block;");
            grid_dialogo_cambio_doc.getChildren().add(eti_num_doc_actual);
            grid_dialogo_cambio_doc.getChildren().add(tex_num_documento_actual);
            grid_dialogo_cambio_doc.getChildren().add(eti_num_doc);
            grid_dialogo_cambio_doc.getChildren().add(tex_num_documento);

            dia_cambio_documento.setDialogo(grid_dialogo_cambio_doc);
            dia_cambio_documento.getBot_aceptar().setMetodo("aceptarDialogoCambioDocumento");
            agregarComponente(dia_cambio_documento);

            eti_saldo.setId("eti_saldo");
            eti_saldo.setStyle("font-size: 16px;color: black;font-weight: bold");
            div_division.setFooter(pat_panel, eti_saldo, "90%");

            sec_calendario.setId("sec_calendario");
            sec_calendario.setMultiple(true);
            sec_calendario.getBot_aceptar().setMetodo("filtrar");
            sec_calendario.getBot_aceptar().setUpdate("sec_calendario,tab_libro_banco");

            rep_reporte.setId("rep_reporte");
            rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sel_rep.setId("sel_rep");
            agregarComponente(sel_rep);
            agregarComponente(rep_reporte);

            agregarComponente(div_division);
            agregarComponente(sec_calendario);
            agregarComponente(dia_cuentas_x_pagar);
            agregarComponente(dia_cuentas_x_cobrar);
            agregarComponente(dia_otros);

            agregarComponente(dia_transferir);

            con_guardar.setId("con_guardar");
            con_guardar.setHeader("PAGO A PROVEEDOR CON CHEQUE");
            con_guardar.setMessage("Esta Seguro de Realizar el Pago y Guardar los Datos");
            con_guardar.getBot_aceptar().setMetodo("generar_cheque");
            con_guardar.getBot_cancelar().setMetodo("cancelar_pago");
            con_guardar.getBot_aceptar().setUpdate("con_guardar,tab_libro_banco");
            con_guardar.getBot_cancelar().setUpdate("con_guardar,tab_libro_banco");
            agregarComponente(con_guardar);
            vis_pdf.setId("vis_pdf");
            agregarComponente(vis_pdf);

            sec_rango_reporte.setId("sec_rango_reporte");
            sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
            sec_rango_reporte.setMultiple(true);
            agregarComponente(sec_rango_reporte);

            che_conciliados.setValue("false");
            che_no_conciliados.setValue("false");

            Grid gri_filatro_conciliados = new Grid();
            gri_filatro_conciliados.setColumns(2);
            gri_filatro_conciliados.getChildren().add(che_conciliados);
            gri_filatro_conciliados.getChildren().add(new Etiqueta("CONCILIADOS"));
            gri_filatro_conciliados.getChildren().add(che_no_conciliados);
            gri_filatro_conciliados.getChildren().add(new Etiqueta("NO CONCILIADOS"));
            gri_filatro_conciliados.setWidth("100%");

            dia_filtro_activo.setId("dia_filtro_activo");
            dia_filtro_activo.setTitle("SELECCIONE ESTADO PARA CONCILIACION");
            dia_filtro_activo.getBot_aceptar().setMetodo("aceptarReporte");
            dia_filtro_activo.setDialogo(gri_filatro_conciliados);
            agregarComponente(dia_filtro_activo);

        } else {
            utilitario.agregarNotificacionInfo("No existe un plan de cuentas activo", "Para poder ingresar a esta pantalla debe estar activo un plan de cuentas, contactese con el administrador del sistema");
        }
    }
    boolean boo_cambio_valor = false;

    public void modificarValorTransaccion() {
        if (tab_libro_banco.getTotalFilas() > 0) {
            if (tab_libro_banco.getValor("conciliado_teclb") == null || tab_libro_banco.getValor("conciliado_teclb").isEmpty()
                    || tab_libro_banco.getValor("conciliado_teclb").equals("false")) {
                boo_cambio_valor = true;
                tex_num_documento_actual.setValue(tab_libro_banco.getValor("valor_teclb"));
                tex_num_documento.setValue(null);
                eti_num_doc_actual.setValue("Valor Actual: ");
                eti_num_doc.setValue("Valor: ");
                dia_cambio_documento.setTitle("CAMBIO DE VALOR DE LA TRANSACCION BANCARIA");
                dia_cambio_documento.dibujar();
            } else {
                utilitario.agregarMensajeInfo("No se puede modificsr el Vslor ", "la transaccion ya se encuentra conciliada");
            }
        }
    }

    public void modificarEstadoConciliado() {
        System.out.println("si entra");
        if (tab_libro_banco.getTotalFilas() > 0) {
            if (tab_libro_banco.getValor("conciliado_teclb") != null && !tab_libro_banco.getValor("conciliado_teclb").isEmpty()) {
                if (tab_libro_banco.getValor("conciliado_teclb").equals("true")) {
                    utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set conciliado_teclb=false where ide_teclb=" + tab_libro_banco.getValor("ide_teclb"));
                } else {
                    utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set conciliado_teclb=true where ide_teclb=" + tab_libro_banco.getValor("ide_teclb"));
                }
            } else {
                utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set conciliado_teclb=true where ide_teclb=" + tab_libro_banco.getValor("ide_teclb"));
            }
            guardarPantalla();
            tab_libro_banco.setCondicion("(fecha_trans_teclb between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "') and ide_tecba=" + com_cue_ban.getValue() + " and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal"));
            tab_libro_banco.ejecutarSql();
        }
    }

    public Dialogo getDia_cambio_documento() {
        return dia_cambio_documento;
    }

    public void setDia_cambio_documento(Dialogo dia_cambio_documento) {
        this.dia_cambio_documento = dia_cambio_documento;
    }

    public void aceptarDialogoCambioDocumento() {
        if (tex_num_documento.getValue() != null && !tex_num_documento.getValue().toString().isEmpty()) {
            dia_cambio_documento.cerrar();
            if (boo_cambio_valor) {
                utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set valor_teclb='" + tex_num_documento.getValue() + "' where ide_teclb=" + tab_libro_banco.getValor("ide_teclb"));
            } else {
                utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set numero_teclb='" + tex_num_documento.getValue() + "' where ide_teclb=" + tab_libro_banco.getValor("ide_teclb"));
            }
            guardarPantalla();
            tab_libro_banco.setCondicion("(fecha_trans_teclb between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "') and ide_tecba=" + com_cue_ban.getValue() + " and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal"));
            tab_libro_banco.ejecutarSql();
        } else {
            utilitario.agregarMensaje("No se puede guardar", "No ha ingresado ningun numero de documento");
        }

    }

    public void modificarNumeroDocumento() {
        if (tab_libro_banco.getTotalFilas() > 0) {
            tex_num_documento_actual.setValue(tab_libro_banco.getValor("numero_teclb"));
            tex_num_documento.setValue(null);
            boo_cambio_valor = false;
            dia_cambio_documento.setTitle("CAMBIO DE NUMERO DE DOCUMENTO BANCARIO");
            eti_num_doc_actual.setValue("Numero Documento Actual: ");
            eti_num_doc.setValue("Numero Documento: ");
            dia_cambio_documento.dibujar();
        }
    }

    public void deseleccionaFacturaCxC(UnselectEvent evt) {
        double total = 0;
        for (Fila actual : tab_cuentas_x_cobrar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxc.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxc");
        calcular_diferencia_cxc();
    }

    public void seleccionaFacturaCxC(SelectEvent evt) {
        tab_cuentas_x_cobrar.seleccionarFila(evt);
        double total = 0;
        for (Fila actual : tab_cuentas_x_cobrar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxc.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxc");
        calcular_diferencia_cxc();
    }

    public void deseleccionaFacturaCxP(UnselectEvent evt) {
        double total = 0;
        for (Fila actual : tab_cuentas_x_pagar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxp.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxp");
        calcular_diferencia_cxp();
    }

    public void seleccionaFacturaCxP(SelectEvent evt) {
        tab_cuentas_x_pagar.seleccionarFila(evt);
        double total = 0;
        for (Fila actual : tab_cuentas_x_pagar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        tex_valor_pagar_cxp.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_valor_pagar_cxp");
        calcular_diferencia_cxp();
    }

    public void aceptarDatosConciliados() {

        utilitario.getConexion().agregarSqlPantalla("UPDATE tes_cab_libr_banc SET conciliado_teclb=TRUE where ide_teclb in (" + set_datos_conciliados.getSeleccionados() + ")");
        set_datos_conciliados.cerrar();
        utilitario.getConexion().guardarPantalla();
        tab_libro_banco.setCondicion("(fecha_trans_teclb between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "') and ide_tecba=" + com_cue_ban.getValue() + " and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_libro_banco.ejecutarSql();
        utilitario.addUpdate("tab_libro_banco");
        calcularSaldo();

    }

    public String agregarComillasCondicion(List lis_Ides) {
        String str_ides = "'";
        for (int i = 0; i < lis_Ides.size(); i++) {
            str_ides += lis_Ides.get(i) + "','";
        }
        System.out.println("ide= " + str_ides.substring(0, str_ides.length() - 2));
        return str_ides.substring(0, str_ides.length() - 2);

    }

    public String getParametroConciliacion(String parametro, String ide_teban) {
        if (parametro != null && !parametro.isEmpty() && ide_teban != null && !ide_teban.isEmpty()) {
            TablaGenerica tab_conf_conc = utilitario.consultar("select * from tes_configura_conciliacion where ide_teban=" + ide_teban);
            if (tab_conf_conc.getTotalFilas() > 0) {
                return tab_conf_conc.getValor(0, parametro);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getValorColumnaArchivoTxt(String fila, int num_columna_retorno) {
        int int_num_columnas_recorridas = 0;
        boolean boo_buscar = false;
        if (num_columna_retorno > 0) {
            if (fila != null && !fila.isEmpty() && num_columna_retorno > 0) {
                String str_valor_columna_retorno = "";
                do {
                    if (fila.indexOf("\t") != -1) {
                        boo_buscar = true;
                        if (num_columna_retorno == 1) {
                            str_valor_columna_retorno = fila.substring(0, fila.indexOf("\t"));
                            boo_buscar = false;
                        } else {
                            int_num_columnas_recorridas = int_num_columnas_recorridas + 1;
                            fila = fila.substring(fila.indexOf("\t") + 1, fila.length());
                            if (int_num_columnas_recorridas == (num_columna_retorno - 1)) {
                                if (fila.indexOf("\t") != -1) {
                                    str_valor_columna_retorno = fila.substring(0, fila.indexOf("\t"));
                                } else {
                                    str_valor_columna_retorno = fila;
                                }
                                boo_buscar = false;
                            }
                        }
                    } else {
                        boo_buscar = false;
                    }
                } while (boo_buscar);
                return str_valor_columna_retorno;
            }
        }
        return null;
    }

    public void conciliarDocumentoBancario(Upload upl_archivo, int columna_documento, int columna_valor, int columna_fecha, int columna_concepto, int columna_saldo, boolean solo_valor, String ide_tecba, String fecha_ini, String fecha_fin) {
        List lis_ide_teclb_conciliados = new ArrayList();
        try {
            FileReader fr = new FileReader(upl_archivo.getArchivos().get(0));
            BufferedReader br = new BufferedReader(fr);
            TablaGenerica tab_cab_lib_banco = utilitario.consultar("select * from tes_cab_libr_banc "
                    + "where ide_tecba=" + ide_tecba + " "
                    + "and fecha_trans_teclb BETWEEN '" + fecha_ini + "' and '" + fecha_fin + "' "
                    + "and conciliado_teclb is not TRUE "
                    + "order by fecha_trans_teclb DESC ");

//            utilitario.getConexion().ejecutar("DELETE from tes_conciliacion_banco where ide_tecba="+com_cue_ban.getValue());
            utilitario.getConexion().agregarSqlPantalla("DELETE from tes_conciliacion_banco where ide_tecba=" + com_cue_ban.getValue());

            Tabla tab_conciliacion_banco = new Tabla();
            tab_conciliacion_banco.setTabla("tes_conciliacion_banco", "ide_tecob", -1);
            tab_conciliacion_banco.setCondicion("ide_tecob=-1");
            tab_conciliacion_banco.getColumna("conciliado_tecob").setValorDefecto("false");
            tab_conciliacion_banco.ejecutarSql();

            String e;
            int int_num_fila = 0;
            while ((e = br.readLine()) != null) {
                if (int_num_fila > 0) {
                    String str_documento = getValorColumnaArchivoTxt(e, columna_documento);
                    String str_monto = getValorColumnaArchivoTxt(e, columna_valor);
                    str_monto = str_monto.replaceAll(",", "");
                    String str_fecha = getValorColumnaArchivoTxt(e, columna_fecha);
                    str_fecha = str_fecha.replaceAll("/", "-");
                    String str_concepto = getValorColumnaArchivoTxt(e, columna_concepto);
                    String str_saldo = getValorColumnaArchivoTxt(e, columna_saldo);
                    str_saldo = str_saldo.replaceAll(",", "");
                    System.out.println("(.txt) fecha " + str_fecha + " num doc " + str_documento + " monto " + str_monto + " saldo " + str_saldo);
                    tab_conciliacion_banco.insertar();
                    tab_conciliacion_banco.setValor("ide_tecba", com_cue_ban.getValue() + "");
                    tab_conciliacion_banco.setValor("documento_tecob", str_documento);
                    tab_conciliacion_banco.setValor("monto_tecob", str_monto);
                    tab_conciliacion_banco.setValor("fecha_tecob", str_fecha);
                    tab_conciliacion_banco.setValor("concepto_tecob", str_concepto);
                    tab_conciliacion_banco.setValor("saldo_tecob", str_saldo);

                    for (int i = 0; i < tab_cab_lib_banco.getTotalFilas(); i++) {
                        if (solo_valor) {
                            // conciliacion solo por monto
                            if (str_monto.equalsIgnoreCase(tab_cab_lib_banco.getValor(i, "valor_teclb"))) {
                                lis_ide_teclb_conciliados.add(tab_cab_lib_banco.getValor(i, "ide_teclb"));
                                System.out.println("(conciliado) num doc " + str_documento + " monto " + str_monto);
                                tab_conciliacion_banco.setValor("conciliado_tecob", "true");
                                break;
                            }
                        } else {
                            // conciliacion por monto y numero de documento
                            if (str_monto.equalsIgnoreCase(tab_cab_lib_banco.getValor(i, "valor_teclb"))
                                    && str_documento.equalsIgnoreCase(tab_cab_lib_banco.getValor(i, "numero_teclb"))) {
                                lis_ide_teclb_conciliados.add(tab_cab_lib_banco.getValor(i, "ide_teclb"));
                                System.out.println("(conciliado) num doc " + str_documento + " monto " + str_monto);
                                tab_conciliacion_banco.setValor("conciliado_tecob", "true");
                                break;
                            }
                        }
                    }
                }
                int_num_fila = int_num_fila + 1;
            }
            br.close();
            tab_conciliacion_banco.guardar();
            if (lis_ide_teclb_conciliados.size() > 0) {
                dia_conciliar.cerrar();
                set_datos_conciliados.getTab_seleccion().setSql("select ide_teclb,fecha_trans_teclb,nombre_tettb,numero_teclb,valor_teclb,beneficiari_teclb,observacion_teclb from tes_cab_libr_banc clb "
                        + "left join tes_cuenta_banco tcb on clb.ide_tecba=tcb.ide_tecba "
                        + "left join tes_tip_tran_banc ttb on clb.ide_tettb=ttb.ide_tettb "
                        + "left join tes_estado_libro_banco elb on clb.ide_teelb=elb.ide_teelb "
                        + "where clb.ide_teclb in (" + agregarComillasCondicion(lis_ide_teclb_conciliados) + ") ");
                set_datos_conciliados.getTab_seleccion().ejecutarSql();
                set_datos_conciliados.dibujar();
                set_datos_conciliados.seleccionarTodas();
            } else {
                utilitario.agregarMensajeInfo("Atencion No se concilio ningun dato", "no se encontraron coincidencias");
            }

        } catch (Exception e1) {
            utilitario.agregarMensajeError("No se puede conciliar ", "el numero de columna configurado para el monto no tiene tipo de dato numerico");
        }
    }

    public void aceptarDialogoConciliar() {
        if (upl_archivo.getArchivos().size() > 0) {
            String num_col_documento = getParametroConciliacion("num_colum_doc_tecco", com_bancos.getValue() + "");
            String num_col_monto = getParametroConciliacion("num_colum_monto_tecco", com_bancos.getValue() + "");
            String num_col_fecha = getParametroConciliacion("num_colum_fecha_tecco", com_bancos.getValue() + "");
            String num_col_concepto = getParametroConciliacion("num_colum_concepto_tecco", com_bancos.getValue() + "");
            String num_col_saldo = getParametroConciliacion("num_colum_saldo_tecco", com_bancos.getValue() + "");

            if (num_col_documento != null && num_col_monto != null
                    && num_col_fecha != null && num_col_saldo != null && num_col_concepto != null) {
                if (rad_tipo_conciliacion.getValue().equals("2")) {
                    // conciliacion solo por valor y numero de documento
                    conciliarDocumentoBancario(upl_archivo, Integer.parseInt(num_col_documento), Integer.parseInt(num_col_monto), Integer.parseInt(num_col_fecha), Integer.parseInt(num_col_concepto), Integer.parseInt(num_col_saldo), false, com_cue_ban.getValue() + "", sec_calendario.getFecha1String(), sec_calendario.getFecha2String());
                } else if (rad_tipo_conciliacion.getValue().equals("1")) {
                    // conciliacion solo por valor
                    conciliarDocumentoBancario(upl_archivo, Integer.parseInt(num_col_documento), Integer.parseInt(num_col_monto), Integer.parseInt(num_col_fecha), Integer.parseInt(num_col_concepto), Integer.parseInt(num_col_saldo), true, com_cue_ban.getValue() + "", sec_calendario.getFecha1String(), sec_calendario.getFecha2String());
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede conciliar", "No existe configuracion de archivo de conciliacion para el banco seleccionado");
            }
        } else {
            utilitario.agregarMensajeInfo("Atencion", "No ha seleccionado ningun archivo .txt para conciliar");
        }
    }

    public void abrirDialogoConciliar() {
        if (tab_libro_banco.getTotalFilas() > 0) {
            TablaGenerica tab_conf_conc = utilitario.consultar("select * from tes_configura_conciliacion where ide_teban=" + com_bancos.getValue());
            if (tab_conf_conc.getTotalFilas() > 0) {
                upl_archivo.getArchivos().clear();
                upl_archivo.setUpload("upload/otros");
                tab_parametros_conciliacion.setCondicion("ide_teban=" + com_bancos.getValue());
                tab_parametros_conciliacion.ejecutarSql();
                dia_conciliar.dibujar();
            } else {
                utilitario.agregarMensajeInfo("No se puede realizar la conciliacion", "La cuenta bancaria seleccionada no tiene configuracion de conciliacion");
            }
        }
    }

    public void calculaTotales() {
        double tot_debe = 0;
        double tot_haber = 0;
        for (int i = 0; i < tab_tabla4.getTotalFilas(); i++) {
            if (tab_tabla4.getValor(i, "valor_cndcc") != null && !tab_tabla4.getValor(i, "valor_cndcc").isEmpty()) {
                if (tab_tabla4.getValor(i, "ide_cnlap") != null && !tab_tabla4.getValor(i, "ide_cnlap").isEmpty()) {
                    if (tab_tabla4.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                        tot_debe = tot_debe + Double.parseDouble(tab_tabla4.getValor(i, "valor_cndcc"));
                    }
                    if (tab_tabla4.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_haber"))) {
                        tot_haber = tot_haber + Double.parseDouble(tab_tabla4.getValor(i, "valor_cndcc"));
                    }
                }
            }

        }

        double valor = 0;
        if (tex_valor_pagar_otros.getValue() != null && !tex_valor_pagar_otros.getValue().toString().isEmpty()) {
            valor = Double.parseDouble(tex_valor_pagar_otros.getValue().toString());
        }

        String signo_tran = recuper_signo_transaccion_bancaria(com_tip_tran_otros.getValue().toString());
        if (Integer.parseInt(signo_tran) == 1) {
            tot_debe = tot_debe + valor;
        } else if (Integer.parseInt(signo_tran) == -1) {
            tot_haber = tot_haber + valor;
        }
        tot_debe = Double.parseDouble(utilitario.getFormatoNumero(tot_debe));
        tot_haber = Double.parseDouble(utilitario.getFormatoNumero(tot_haber));
        eti_suma_debe_otros.setValue("Total Debe: " + utilitario.getFormatoNumero(tot_debe, 2));
        eti_suma_haber_otros.setValue("Total Haber: " + utilitario.getFormatoNumero(tot_haber, 2));
        eti_suma_diferencia_otros.setValue("Total Haber: " + utilitario.getFormatoNumero((tot_debe - tot_haber), 2));

        if (tot_debe != tot_haber) {
            eti_suma_diferencia_otros.setStyle("font-size: 14px;font-weight: bold;color:red");
        } else {
            eti_suma_diferencia_otros.setStyle("font-size: 14px;font-weight: bold");
        }

    }

    public void obtenerNumeroChequeOtros() {
        if (com_tip_tran_otros.getValue() != null) {
            tex_num_otros.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco_otros.getValue() + "", com_tip_tran_otros.getValue() + ""));
        } else {
            tex_num_otros.setValue("");
        }
        utilitario.addUpdate("tex_num_otros");
    }

    public void obtenerNumeroChequeCxC() {
        tex_num_cxc.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco_cxc.getValue() + "", com_tip_tran_cxc.getValue() + ""));
        utilitario.addUpdate("tex_num_cxc");
    }

    public void obtenerNumeroChequeCxP() {
        tex_num_cxp.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco_cxp.getValue() + "", com_tip_tran_cxp.getValue() + ""));
        utilitario.addUpdate("tex_num_cxp");
    }

    public boolean validar_transferencia() {

        if (com_banco_transf.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe seleccionar un Banco");
            return false;
        }
        if (com_banco_transf_2.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe seleccionar un Banco");
            return false;
        }
        if (com_cuenta_banco_transf.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe seleccionar una Cuenta bancaria");
            return false;
        }
        if (com_cuenta_banco_transf_2.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe seleccionar una Cuenta Bncaria");
            return false;
        }
        if (tex_valor_transf.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe ingresar el monto a transferir");
            return false;
        } else {
            if (tex_valor_transf.getValue().toString().isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe ingresar el monto a transferir");
                return false;
            }
        }
        if (cal_fecha_transf.getFecha() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe seleccionar una fecha");
            return false;
        } else {
            if (cal_fecha_transf.getFecha().isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe seleccionar una fecha");
                return false;
            }
        }
        if (tex_observacion_tranf.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe ingresar la observacion");
            return false;
        } else {
            if (tex_observacion_tranf.getValue().toString().isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe una observacion de la transferencia");
                return false;
            }
        }

        if (tex_num_transf.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe ingresar un numero");
            return false;
        } else {
            if (tex_num_transf.getValue().toString().isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar la Transferencia, no se guardaron los datos", "Debe ingresar un numero");
                return false;
            }
        }
        return true;
    }

    public void aceptarDialogoTransferir() {
        if (validar_transferencia()) {
            generarComprobanteContabilidadTransferencia();
        }
    }

    public void abrirDialogoTransferir() {
        System.out.println("realizar transferencia");
        tex_valor_transf.setValue("");
        tex_num_transf.setValue("");
        tex_observacion_tranf.setValue("");
        com_tip_tran_transf.setValue(utilitario.getVariable("p_tes_tran_transferencia_menos"));
        com_banco_transf.setValue(null);
        com_banco_transf_2.setValue(null);
        com_cuenta_banco_transf.setValue(null);
        com_cuenta_banco_transf_2.setValue(null);
        cal_fecha_transf.setFechaActual();
        tex_valor_transf.setValue("");
        dia_transferir.dibujar();

    }

    public void cambioTipoTransBancCxC() {
//        CAMBIE
        if (com_tip_tran_cxc.getValue() != null) {
            if (com_cuenta_banco_cxc.getValue() != null) {
                tex_num_cxc.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco_cxc.getValue() + "", com_tip_tran_cxc.getValue() + ""));
            }
        } else {
            com_banco_cxc.setValue(null);
            com_cuenta_banco_cxc.setValue(null);
            tex_num_cxc.setValue("");
        }
        utilitario.addUpdate("tex_num_cxc,com_banco_cxc,com_cuenta_banco_cxc");
    }

    public void cambioTipoTransBancCxP() {
//        CAMBIE
        if (com_tip_tran_cxp.getValue() != null) {
            if (com_cuenta_banco_cxp.getValue() != null) {
                tex_num_cxp.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco_cxp.getValue() + "", com_tip_tran_cxp.getValue() + ""));
            }
        } else {
            com_banco_cxp.setValue(null);
            com_cuenta_banco_cxp.setValue(null);
            tex_num_cxp.setValue("");
        }
        utilitario.addUpdate("tex_num_cxp,com_banco_cxp,com_cuenta_banco_cxp");

    }

    public void cambioTipoTransBancOtros() {
        if (com_tip_tran_otros.getValue() != null) {
            String signo_tran = recuper_signo_transaccion_bancaria(com_tip_tran_otros.getValue().toString());
            if (Integer.parseInt(signo_tran) == 1) {
                for (int i = 0; i < tab_tabla4.getTotalFilas(); i++) {
                    tab_tabla4.setValor(i, "ide_cnlap", utilitario.getVariable("p_con_lugar_haber"));
                }
            } else if (Integer.parseInt(signo_tran) == -1) {
                for (int i = 0; i < tab_tabla4.getTotalFilas(); i++) {
                    tab_tabla4.setValor(i, "ide_cnlap", utilitario.getVariable("p_con_lugar_debe"));
                }
            }
            if (com_banco_otros.getValue() != null) {
                if (com_cuenta_banco_otros.getValue() != null) {
                    tex_num_otros.setValue(banco.obtenerNumMaximoTran(com_cuenta_banco_otros.getValue() + "", com_tip_tran_otros.getValue() + ""));
                } else {
                    tex_num_otros.setValue("");
                }
            } else {
                tex_num_otros.setValue("");
            }
            //        CAMBIE
            utilitario.addUpdate("tex_num_otros,tab_tabla4");
            calculaTotales();
        }
    }

    public void cancelarDialogo() {
        if (via_asiento.isVisible()) {
            via_asiento.cerrar();
            utilitario.addUpdate("via_asiento");
        } else if (dia_cuentas_x_cobrar.isVisible()) {
            dia_cuentas_x_cobrar.cerrar();
            utilitario.addUpdate("dia_cuentas_x_cobrar");
        } else if (dia_cuentas_x_pagar.isVisible()) {
            dia_cuentas_x_pagar.cerrar();
            utilitario.addUpdate("dia_cuentas_x_pagar");
        } else if (dia_otros.isVisible()) {
            dia_otros.cerrar();
        } else if (dia_transferir.isVisible()) {
            dia_transferir.cerrar();

        } else if (dia_conciliar.isVisible()) {
            dia_conciliar.cerrar();

        } else if (set_datos_conciliados.isVisible()) {
            set_datos_conciliados.cerrar();

        }
        // utilitario.getConexion().rollback(); *********
        utilitario.getConexion().getSqlPantalla().clear();
    }

    public String getValorTransaccion(String ide_tecba, Tabla tab_det_com_con) {
        String ide_cndpc_cuenta_banco = banco.getParametroCuentaBanco("ide_tecba", ide_tecba, "ide_cndpc");
        if (ide_cndpc_cuenta_banco != null) {
            for (int i = 0; i < tab_det_com_con.getTotalFilas(); i++) {
                if (ide_cndpc_cuenta_banco.equalsIgnoreCase(tab_det_com_con.getValor(i, "ide_cndpc"))) {
                    return tab_det_com_con.getValor(i, "valor_cndcc");
                }
            }
        }
        return null;
    }

    public synchronized void aceptarComprobanteContabilidad() {

        String str_banco_seleccionado = "";
        String str_cuenta_banco_seleccionado = "";
        if (via_asiento.validarComprobante()) {
            cab_com_con.setObservacion_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc"));
            cab_com_con.setIde_geper(via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_geper"));
            cab_com_con.setFecha_trans_cnccc(via_asiento.getTab_cab_comp_cont_vasiento().getValor("fecha_trans_cnccc"));
            lista_detalles.clear();
            for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                lista_detalles.add(new cls_det_comp_cont(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cnlap"), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc")), via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "observacion_cndcc")));
            }
            cab_com_con.setDetalles(lista_detalles);
            String ide_cnccc = conta.generarAsientoContable(cab_com_con);
            System.out.println("ide cnccc " + ide_cnccc);
            if (ide_cnccc != null && !ide_cnccc.isEmpty()) {
                if (tipo_cuenta_x_pagar) {
                    tipo_cuenta_x_pagar = false;
                    dia_cuentas_x_pagar.cerrar();
                    str_banco_seleccionado = com_banco_cxp.getValue() + "";
                    str_cuenta_banco_seleccionado = com_cuenta_banco_cxp.getValue() + "";
                    if (realizo_pago_sin_bancos_cxp) {
                        cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
                        cxp.generarPagoFacturaSinBancos(lis_fact_pagadas, ide_cnccc, cal_fecha_pago_cxp.getFecha(), tex_num_cxp.getValue() + "", via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc") + "");
                    } else {
                        String valor_cxp = getValorTransaccion(com_cuenta_banco_cxp.getValue() + "", via_asiento.getTab_det_comp_cont_vasiento());
                        if (valor_cxp.isEmpty()) {
                            valor_cxp = tex_valor_pagar_cxp.getValue() + "";
                        }
                        String str_num_doc = tex_num_cxp.getValue() + "";
                        if (tex_num_cxp.getValue() == null || tex_num_cxp.getValue().toString().isEmpty()) {
                            str_num_doc = str_num_doc_factura_cxp;
                        }

                        banco.generarLibroBanco(aut_proveedor.getValor(), cal_fecha_pago_cxp.getFecha(), com_tip_tran_cxp.getValue() + "", com_cuenta_banco_cxp.getValue() + "", ide_cnccc, Double.parseDouble(valor_cxp), ate_observacion_cxp.getValue() + "", str_num_doc);
                        cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();

                        cxp.generarPagoFacturaConBancos(lis_fact_pagadas, banco.getTab_cab_libro_banco());
                    }
                } else if (tipo_cuenta_x_cobrar) {
                    tipo_cuenta_x_cobrar = false;
                    dia_cuentas_x_cobrar.cerrar();
                    str_banco_seleccionado = com_banco_cxc.getValue() + "";
                    str_cuenta_banco_seleccionado = com_cuenta_banco_cxc.getValue() + "";
                    if (realizo_pago_sin_bancos_cxc) {
                        cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                        cxc.generarPagoTransaccionVentaSinBancos(lis_fact_pagadas, ide_cnccc, cal_fecha_pago_cxc.getFecha(), tex_num_cxc.getValue() + "", via_asiento.getTab_cab_comp_cont_vasiento().getValor("observacion_cnccc") + "");
                    } else {
                        String valor_cxc = getValorTransaccion(com_cuenta_banco_cxc.getValue() + "", via_asiento.getTab_det_comp_cont_vasiento());
                        if (valor_cxc.isEmpty()) {
                            valor_cxc = tex_valor_pagar_cxc.getValue() + "";
                        }
                        String str_num_doc = tex_num_cxc.getValue() + "";
                        if (tex_num_cxc.getValue() == null || tex_num_cxc.getValue().toString().isEmpty()) {
                            str_num_doc = str_num_doc_factura_cxc;
                        }
                        banco.generarLibroBanco(aut_cliente_cxc.getValor(), cal_fecha_pago_cxc.getFecha(), com_tip_tran_cxc.getValue() + "", com_cuenta_banco_cxc.getValue() + "", ide_cnccc, Double.parseDouble(valor_cxc), ate_observacion_cxc.getValue() + "", str_num_doc);
                        cls_cuentas_x_cobrar cxc = new cls_cuentas_x_cobrar();
                        cxc.generarPagoTransaccionVentaConBancos(lis_fact_pagadas, banco.getTab_cab_libro_banco());
                    }
                } else if (tipo_otros) {
                    tipo_otros = false;
                    dia_otros.cerrar();
                    str_banco_seleccionado = com_banco_otros.getValue() + "";
                    str_cuenta_banco_seleccionado = com_cuenta_banco_otros.getValue() + "";
                    banco.getTab_cab_libro_banco().limpiar();
                    banco.generarVariosLibrosBancos(aut_cliente_otros.getValor(), cal_fecha_pago_otros.getFecha(), com_tip_tran_otros.getValue() + "", com_cuenta_banco_otros.getValue() + "", ide_cnccc, Double.parseDouble(tex_valor_pagar_otros.getValue() + ""), ate_observacion_otros.getValue() + "", tex_num_otros.getValue() + "");
                    for (int i = 0; i < via_asiento.getTab_det_comp_cont_vasiento().getTotalFilas(); i++) {
                        String ide_tecba = banco.getParametroCuentaBanco(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "ide_cndpc"), "ide_cndpc", "ide_tecba");
                        System.out.println("ide tecba " + ide_tecba);
                        if (ide_tecba != null) {
                            if (!ide_tecba.equalsIgnoreCase(str_cuenta_banco_seleccionado)) {
                                banco.generarVariosLibrosBancos(aut_cliente_otros.getValor(), cal_fecha_pago_otros.getFecha(), utilitario.getVariable("p_tes_tran_retiro_caja"), ide_tecba, ide_cnccc, Double.parseDouble(via_asiento.getTab_det_comp_cont_vasiento().getValor(i, "valor_cndcc") + ""), ate_observacion_otros.getValue() + "", banco.obtenerNumMaximoTran(ide_tecba, utilitario.getVariable("p_tes_tran_retiro_caja")));
                            }
                        }

                    }
                    banco.getTab_cab_libro_banco().guardar();
                } else {
                    if (tipo_tranferencia) {
                        tipo_tranferencia = false;
                        dia_transferir.cerrar();
                        str_banco_seleccionado = com_banco_transf_2.getValue() + "";
                        str_cuenta_banco_seleccionado = com_cuenta_banco_transf_2.getValue() + "";
                        banco.generarLibroBancoTransferir(utilitario.getVariable("p_con_beneficiario_empresa"), cal_fecha_transf.getFecha(), com_tip_tran_transf.getValue() + "", com_cuenta_banco_transf.getValue() + "", com_cuenta_banco_transf_2.getValue() + "", ide_cnccc, Double.parseDouble(tex_valor_transf.getValue() + ""), tex_observacion_tranf.getValue() + "", tex_num_transf.getValue() + "");
                    }
                }
                via_asiento.cerrar();
                utilitario.addUpdate("via_asiento");
                System.out.println("size: " + utilitario.getConexion().getSqlPantalla().size());
                if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                    TablaGenerica tab_lib_banc = utilitario.consultar("select * from tes_cab_libr_banc where ide_cnccc=" + ide_cnccc + " and ide_tecba=" + str_cuenta_banco_seleccionado);
                    if (tab_lib_banc.getTotalFilas() > 0) {
                        if (tab_lib_banc.getValor("ide_tettb").equals(utilitario.getVariable("p_tes_tran_cheque"))) {
                            parametro = new HashMap();
                            parametro.put("beneficiario", tab_lib_banc.getValor("beneficiari_teclb") + "");
                            parametro.put("monto", tab_lib_banc.getValor("valor_teclb") + "");
                            parametro.put("anio", utilitario.getAnio(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                            parametro.put("mes", utilitario.getMes(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
                            parametro.put("dia", utilitario.getDia(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
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
                            vis_pdf.setVisualizarPDF("rep_bancos/rep_cheque.jasper", parametro);
                            vis_pdf.dibujar();
                            utilitario.addUpdate("vis_pdf");
                        } else {
                            parametro = new HashMap();
                            parametro.put("ide_cnccc", Long.parseLong(ide_cnccc));
                            parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                            parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                            vis_pdf.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametro);
                            vis_pdf.dibujar();
                            utilitario.addUpdate("vis_pdf");
                        }
                    } else {
                        parametro = new HashMap();
                        parametro.put("ide_cnccc", Long.parseLong(ide_cnccc));
                        parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                        parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                        vis_pdf.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametro);
                        vis_pdf.dibujar();
                        utilitario.addUpdate("vis_pdf");
                    }

                    if (!str_banco_seleccionado.isEmpty()) {
                        com_bancos.setValue(str_banco_seleccionado);
                        List lis_cuentas = utilitario.getConexion().consultar("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_teban=" + str_banco_seleccionado + " and ide_sucu=" + utilitario.getVariable("ide_sucu"));
                        com_cue_ban.setCombo(lis_cuentas);
                        com_cue_ban.setValue(str_cuenta_banco_seleccionado);
                    }
                    tab_libro_banco.setCondicion("(fecha_trans_teclb between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "') and ide_tecba=" + str_cuenta_banco_seleccionado + " and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal"));
                    tab_libro_banco.ejecutarSql();
                    utilitario.addUpdate("tab_libro_banco,com_cue_ban,com_bancos");
                    calcularSaldo();
                } else {
                    utilitario.agregarMensajeError("Error no se guardaron los datos", "Uno de los campos sobrepasa la dimension");
                }
            }

        }
    }

    public void ingresaCantidad(AjaxBehaviorEvent evt) {
        tab_tabla4.modificar(evt);
        calculaTotales();
        utilitario.addUpdate("gri_totales_otros");
    }

    public void cambioLugarAplica(AjaxBehaviorEvent evt) {
        tab_tabla4.modificar(evt);
        calculaTotales();
        utilitario.addUpdate("gri_totales_otros");

    }

    public void generarComprobanteContabilidadTransferencia() {
        tipo_tranferencia = true;
        conta.limpiar();
        cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_diario, p_estado_comprobante_normal, p_modulo, conta.obtenerParametroPersona("ide_geper", utilitario.getVariable("p_con_beneficiario_empresa")), cal_fecha_transf.getFecha(), tex_observacion_tranf.getValue() + "");
        lista_detalles.clear();
        String cuenta1 = banco.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_banco_transf.getValue().toString());
        if (cuenta1 != null) {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), cuenta1, Double.parseDouble(tex_valor_transf.getValue().toString()), ""));
        }
        String cuenta2 = banco.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_banco_transf_2.getValue().toString());
        if (cuenta2 != null) {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), cuenta2, Double.parseDouble(tex_valor_transf.getValue().toString()), ""));
        }
        cab_com_con.setDetalles(lista_detalles);
        via_asiento.setVistaAsiento(cab_com_con);
        via_asiento.dibujar();
    }
    boolean tipo_otros;

    public void generarComprobanteContabilidadOtros() {
        tipo_otros = true;
        conta.limpiar();
        lista_detalles.clear();
        String cuenta_banco = com_cuenta_banco_otros.getValue().toString();
        if (ate_observacion_otros.getValue() != null) {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_contabilidad, p_estado_comprobante_normal, p_modulo, aut_cliente_otros.getValor(), cal_fecha_pago_otros.getFecha(), ate_observacion_otros.getValue() + "");
        } else {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_contabilidad, p_estado_comprobante_normal, p_modulo, aut_cliente_otros.getValor(), cal_fecha_pago_otros.getFecha(), "");
        }
        System.out.println("cuenta banco " + cuenta_banco);
        String cuenta_contable_banco = banco.obtenerParametroCuentaBanco("ide_cndpc", cuenta_banco);
        if (cuenta_contable_banco != null) {
            double valor = Double.parseDouble(tex_valor_pagar_otros.getValue().toString());
            if (p_tipo_comprobante_contabilidad.equals(p_tipo_comprobante_ingreso)) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), cuenta_contable_banco, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            } else if (p_tipo_comprobante_contabilidad.equals(p_tipo_comprobante_egreso)) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), cuenta_contable_banco, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            }
        }
        for (int i = 0; i < tab_tabla4.getTotalFilas(); i++) {
            lista_detalles.add(new cls_det_comp_cont(tab_tabla4.getValor(i, "ide_cnlap") + "", tab_tabla4.getValor(i, "ide_cndpc") + "", Double.parseDouble(tab_tabla4.getValor(i, "valor_cndcc")), tab_tabla4.getValor(i, "observacion_cndcc") + ""));
        }
        cab_com_con.setDetalles(lista_detalles);
        via_asiento.setVistaAsiento(cab_com_con);
        via_asiento.dibujar();
    }

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

    public void cancelar_pago() {
        con_guardar.cerrar();
      //  utilitario.getConexion().rollback(); *****

    }

    public void generar_cheque() {
//        con_guardar.cerrar();
//        System.out.println("provee " + aut_proveedor.getValor());
//        System.out.println("nom provee " + nombre_proveedor);
//        System.out.println("monto " + tex_valor_pagar.getValue().toString());
//        System.out.println("fecha " + tex_fecha_pago.getValue().toString());
//        System.out.println("monto en letras " + utilitario.getLetrasDolarNumero(tex_valor_pagar.getValue()));
//        System.out.println("anio " + utilitario.getAnio(tex_fecha_pago.getValue().toString()));
//        System.out.println("mes " + utilitario.getMes(tex_fecha_pago.getValue().toString()));
//        System.out.println("dia " + utilitario.getDia(tex_fecha_pago.getValue().toString()));
//
//        utilitario.getConexion().guardarPantalla();
//
//        Map parametros = new HashMap();
//        parametros.put("beneficiario", nombre_proveedor);
//        parametros.put("monto", tex_valor_pagar.getValue() + "");
//        parametros.put("anio", utilitario.getAnio(tex_fecha_pago.getValue().toString()) + "");
//        parametros.put("mes", utilitario.getMes(tex_fecha_pago.getValue().toString()));
//        parametros.put("dia", utilitario.getDia(tex_fecha_pago.getValue().toString()) + "");
//        parametros.put("monto_letras", utilitario.getLetrasDolarNumero(tex_valor_pagar.getValue()));

//utilitario.getConexion().guardarPantalla();
        Map parametros = new HashMap();
        parametros.put("beneficiario", tab_libro_banco.getValor("beneficiari_teclb"));
        parametros.put("monto", tab_libro_banco.getValor("valor_teclb") + "");
        parametros.put("anio", utilitario.getAnio(tab_libro_banco.getValor("fecha_trans_teclb").toString()) + "");
        parametros.put("mes", utilitario.getMes(tab_libro_banco.getValor("fecha_trans_teclb").toString()));
        parametros.put("dia", utilitario.getDia(tab_libro_banco.getValor("fecha_trans_teclb").toString()) + "");
        parametros.put("monto_letras", utilitario.getLetrasDolarNumero(tab_libro_banco.getValor("valor_teclb")));

//        vp.setVisualizarPDF("rep_bancos/rep_cheque.jasper", parametros);
//        vp.dibujar();
//        utilitario.addUpdate("vp");
//        tab_cuentas_x_pagar.limpiar();
//        aut_proveedor.limpiar();
//        tex_num_cxp.limpiar();
//        tex_observacion.limpiar();
//        tex_valor_pagar.limpiar();
//        tex_diferencia.limpiar();
    }

    public void cargarCuentasporCobrar() {
        cliente_actual = aut_cliente_cxc.getValor();
        tab_cuentas_x_cobrar.setSql(cxc.getSqlCuentasPorCobrar(cliente_actual));
        tab_cuentas_x_cobrar.ejecutarSql();
        tex_diferencia_cxc.setValue("");
        tex_valor_pagar_cxc.setValue("");
        if (tab_cuentas_x_cobrar.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("Atencion ", "El cliente seleccionado no tiene deuda");
        }
        nombre_cliente = conta.obtenerParametroPersona("nom_geper", aut_cliente_cxc.getValor() + "");
        utilitario.addUpdate("tab_cuentas_x_cobrar");

    }

    public void cargarCuentasporPagar() {
        provee_actual = aut_proveedor.getValor();
        tab_cuentas_x_pagar.setSql(cxp.getSqlCuentasPorPagar(provee_actual));
        tab_cuentas_x_pagar.ejecutarSql();
        System.out.println("cxp " + tab_cuentas_x_pagar.getSql());
        tex_diferencia.setValue("");
        tex_valor_pagar_cxp.setValue("");
        if (tab_cuentas_x_pagar.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("Atencion ", "No tiene deuda con el Proveedor seleccionado");
        }
        nombre_proveedor = conta.obtenerParametroPersona("nom_geper", aut_proveedor.getValor());
        utilitario.addUpdate("tab_cuentas_x_pagar,tex_valor_pagar_cxp,tex_diferencia");
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_cuentas_x_pagar.seleccionarFila(evt);
        tex_diferencia.setValue("");
        tex_valor_pagar_cxp.setValue("");
        utilitario.addUpdate("tex_diferencia,tex_valor_pagar_cxp");
    }

    public void seleccionar_tabla2(SelectEvent evt) {
        tab_cuentas_x_cobrar.seleccionarFila(evt);
        tex_diferencia_cxc.setValue("");
        tex_valor_pagar_cxc.setValue("");
        utilitario.addUpdate("tex_valor_pagar_cxc,tex_diferencia_cxc");
    }

    public void abrirDialogoCxP() {
        tex_num_cxp.setValue("");
        ate_observacion_cxp.setValue("");
        aut_proveedor.setValor("");
        tab_cuentas_x_pagar.limpiar();
        com_banco_cxp.setValue(null);
        com_tip_tran_cxp.setValue(null);
        com_cuenta_banco_cxp.setValue(null);
        tex_valor_pagar_cxp.setValue("");
        tex_diferencia.setValue("");
        cal_fecha_pago_cxp.setValue(utilitario.getDate());
        dia_cuentas_x_pagar.dibujar();

    }

    public void cargar_valor() {
        calculaTotales();
        utilitario.addUpdate("gri_totales_otros");
    }

    public void abrirDialogoOtros() {
        tex_num_otros.setValue("");
        ate_observacion_otros.setValue("");
        tex_valor_pagar_otros.setValue("");
        com_banco_otros.setValue(null);
        com_tip_tran_otros.setValue(null);
        com_cuenta_banco_otros.setValue(null);
        aut_cliente_otros.limpiar();
        tab_tabla4.limpiar();
        eti_suma_debe_otros.setValue("Total Debe: 0.0");
        eti_suma_haber_otros.setValue("Total Haber: 0.0");
        eti_suma_diferencia_otros.setValue("Total Haber: 0.0");

        dia_otros.dibujar();
    }

    public void abrirDialogoCxC() {
        tex_num_cxc.setValue("");
        ate_observacion_cxc.setValue("");
        aut_cliente_cxc.setValor("");
        tab_cuentas_x_cobrar.limpiar();
        com_banco_cxc.setValue(null);
        com_tip_tran_cxc.setValue(null);
        tex_valor_pagar_cxc.setValue("");
        tex_diferencia_cxc.setValue("");
        cal_fecha_pago_cxc.setValue(utilitario.getDate());
        com_cuenta_banco_cxc.setValue(null);
        dia_cuentas_x_cobrar.dibujar();
    }

    public void calcular_diferencia_cxp() {
        double diferencia;
        if (tex_valor_pagar_cxp.getValue() != null) {
            if (!tex_valor_pagar_cxp.getValue().toString().isEmpty()) {
                if (tab_cuentas_x_pagar.getTotalFilas() > 0) {
                    diferencia = tab_cuentas_x_pagar.getSumaColumna("saldo_x_pagar") - Double.parseDouble(tex_valor_pagar_cxp.getValue().toString());
                    tex_diferencia.setValue(utilitario.getFormatoNumero(diferencia));
                }
            }
        } else {
            if (tab_cuentas_x_pagar.getTotalFilas() > 0) {
                diferencia = tab_cuentas_x_pagar.getSumaColumna("saldo_x_pagar");
                tex_diferencia.setValue(utilitario.getFormatoNumero(diferencia));
            }
        }
        utilitario.addUpdate("tex_diferencia");
    }

    public void calcular_diferencia_cxc() {
        double diferencia;
        if (tex_valor_pagar_cxc.getValue() != null) {
            if (!tex_valor_pagar_cxc.getValue().toString().isEmpty()) {
                if (tab_cuentas_x_cobrar.getTotalFilas() > 0) {
                    diferencia = tab_cuentas_x_cobrar.getSumaColumna("saldo_x_pagar") - Double.parseDouble(tex_valor_pagar_cxc.getValue().toString());
                    tex_diferencia_cxc.setValue("" + utilitario.getFormatoNumero(diferencia));
                }
            }
        } else {
            if (tab_cuentas_x_cobrar.getTotalFilas() > 0) {
                diferencia = tab_cuentas_x_cobrar.getSumaColumna("saldo_x_pagar");
                tex_diferencia_cxc.setValue("" + utilitario.getFormatoNumero(diferencia));
            }
        }
        utilitario.addUpdate("tex_diferencia_cxc");

    }

    public boolean validar_otros() {
        if (aut_cliente_otros.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "Debe seleccionar un Beneficiario");
            return false;
        }
        if (com_tip_tran_otros.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "Debe seleccionar el Tipo de Transaccion");
            return false;
        }

        if (com_banco_otros.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "Debe seleccionar un Banco");
            return false;
        }
        if (com_cuenta_banco_otros.getValue() == null) {
            utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "Debe seleccionar una cuenta bancaria");
            return false;
        }
        double valor = 0;
        if (tex_valor_pagar_otros.getValue() == null || tex_valor_pagar_otros.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Ingreso, no se guardaron los datos", "Debe ingresar el valor a cobrar");
            return false;
        } else {
            valor = Double.parseDouble(tex_valor_pagar_otros.getValue().toString());
        }
        if (tex_num_otros.getValue() == null || tex_num_otros.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "Debe ingresar el numero de cheque o de deposito");
            return false;
        } else {
        }
//        for (int i = 0; i < tab_tabla4.getTotalFilas(); i++) {
//            if (tab_tabla4.getValor("ide_cndpc") == null || tab_tabla4.getValor("ide_cndpc").isEmpty()) {
//                utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "Debe seleccionar una cuenta contable");
//                return false;
//            }
//            if (tab_tabla4.getValor("valor_cndcc") == null || tab_tabla4.getValor("valor_cndcc").isEmpty()) {
//                utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "Debe ingresar un valor para la cuenta seleccionada");
//                return false;
//            }
//        }

        if (tab_tabla4.getTotalFilas() > 0) {
            if (tex_valor_pagar_otros.getValue() != null || !tex_valor_pagar_otros.getValue().toString().isEmpty()) {
                double tot_debe = 0;
                double tot_haber = 0;
                for (int i = 0; i < tab_tabla4.getTotalFilas(); i++) {
                    try {
                        if (tab_tabla4.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_debe"))) {
                            tot_debe = tot_debe + Double.parseDouble(tab_tabla4.getValor(i, "valor_cndcc"));
                        }
                        if (tab_tabla4.getValor(i, "ide_cnlap").equals(utilitario.getVariable("p_con_lugar_haber"))) {
                            tot_haber = tot_haber + Double.parseDouble(tab_tabla4.getValor(i, "valor_cndcc"));
                        }
                    } catch (Exception e) {
                    }
                }

                String signo_tran = recuper_signo_transaccion_bancaria(com_tip_tran_otros.getValue().toString());
                if (Integer.parseInt(signo_tran) == 1) {
                    tot_debe = tot_debe + valor;
                } else if (Integer.parseInt(signo_tran) == -1) {
                    tot_haber = tot_haber + valor;
                }
                tot_debe = Double.parseDouble(utilitario.getFormatoNumero(tot_debe));
                tot_haber = Double.parseDouble(utilitario.getFormatoNumero(tot_haber));

                if (tot_debe != tot_haber) {
                    utilitario.agregarMensajeError("Error al guardar la Ingreso, no se guardaron los datos", "los valores no cuadran ");
                    return false;

                }
            }
        }

        return true;
    }

    public String recuper_signo_transaccion_bancaria(String ide_tettb) {
        List lis_sig_tra_ban = utilitario.getConexion().consultar("select signo_tettb from tes_tip_tran_banc where ide_tettb=" + ide_tettb);
        if (lis_sig_tra_ban != null) {
            return lis_sig_tra_ban.get(0).toString();
        } else {
            return "";
        }
    }

    public void aceptarDialogoOtros() {
        if (validar_otros()) {
            String signo_tran = recuper_signo_transaccion_bancaria(com_tip_tran_otros.getValue().toString());
            if (Integer.parseInt(signo_tran) == 1) {
                p_tipo_comprobante_contabilidad = p_tipo_comprobante_ingreso;
            } else if (Integer.parseInt(signo_tran) == -1) {
                p_tipo_comprobante_contabilidad = p_tipo_comprobante_egreso;
            }
            generarComprobanteContabilidadOtros();
        }
    }
    boolean realizo_pago_sin_bancos_cxc = false;

    public boolean validarDialogoCxC() {
        if (com_tip_tran_cxc.getValue() != null) {
            realizo_pago_sin_bancos_cxc = false;
            if (com_tip_tran_cxc.getValue() == null) {
                utilitario.agregarMensajeError("Atencion ", "Debe seleccionar el tipo de transaccion bancaria");
                return false;
            }

            if (com_banco_cxc.getValue() == null) {
                utilitario.agregarMensajeError("Atencion ", "No ha seleccionado un Banco");
                return false;
            }
            if (com_cuenta_banco_cxc.getValue() == null) {
                utilitario.agregarMensajeError("Atencion ", "No ha seleccionado una Cuenta Bancaria");
                return false;
            }
        } else {
            realizo_pago_sin_bancos_cxc = true;
        }

        if (aut_cliente_cxc.getValue() == null) {
            utilitario.agregarMensajeError("Atencion ", "No ha seleccionado un Cliente");
            return false;
        }
        if (tab_cuentas_x_cobrar.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("Atencion ", "No tiene deuda con el Cliente seleccionado");
            return false;
        }
        if (tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size() == 0) {
            utilitario.agregarMensajeInfo("No se puede realizar el cobro", "Debe seleccionar al menos una factura");
            return false;
        }

        if (tex_valor_pagar_cxc.getValue() == null || tex_valor_pagar_cxc.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion no ha ingresado el monto a cancelar");
            return false;
        }
        if (tex_diferencia_cxc.getValue() == null || tex_diferencia_cxc.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion no realizo ningun pago");
            return false;
        } else {
            if (Double.parseDouble(tex_diferencia_cxc.getValue() + "") < 0) {
                utilitario.agregarMensajeInfo("No se puede realizar el cobro", "Atencion  no puede cobrar mas de lo que deben");
                return false;
            }
        }

        double suma = 0;
        for (int i = 0; i < tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size(); i++) {
            suma = Double.parseDouble(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5] + "") + suma;
        }

        double valor_a_pagar = Double.parseDouble(tex_valor_pagar_cxc.getValue() + "");
        if (suma < valor_a_pagar) {
            utilitario.agregarMensajeInfo("No se puede cancelar", "El valor a cancelar es mayor que el valor de la deuda");
            return false;
        }

        return true;
    }
    boolean realizo_pago_sin_bancos_cxp = false;

    public boolean validarDialogoCxP() {
        if (com_tip_tran_cxp.getValue() != null) {
            realizo_pago_sin_bancos_cxp = false;
            if (com_banco_cxp.getValue() == null) {
                utilitario.agregarMensajeError("Atencion ", "No ha seleccionado un Banco");
                return false;
            }
            if (com_cuenta_banco_cxp.getValue() == null) {
                utilitario.agregarMensajeError("Atencion ", "No ha seleccionado una Cuenta Bancaria");
                return false;
            }
        } else {
            realizo_pago_sin_bancos_cxp = true;
        }

        if (aut_proveedor.getValue() == null) {
            utilitario.agregarMensajeError("Atencion ", "No ha seleccionado un Proveedor");
            return false;
        }

        if (tab_cuentas_x_pagar.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("Atencion ", "No tiene deuda con el Proveedor seleccionado");
            return false;
        }

        if (tab_cuentas_x_pagar.getListaFilasSeleccionadas().size() == 0) {
            utilitario.agregarMensajeInfo("No se puede realizar el pago", "Debe seleccionar al menos una factura");
            return false;
        }

        if (tex_valor_pagar_cxp.getValue() == null || tex_valor_pagar_cxp.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion no ha ingresado el monto a cancelar");
            return false;
        }

        if (tex_diferencia.getValue() == null || tex_diferencia.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion no realizo ningun pago");
            return false;
        } else {
            if (Double.parseDouble(tex_diferencia.getValue() + "") < 0) {
                utilitario.agregarMensajeInfo("Pago a Proveedores", "Atencion  no puede pagar mas de lo que debe");
                return false;
            }
        }

        double total = 0;
        for (Fila actual : tab_cuentas_x_pagar.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[5] + "") + total;
        }
        total = Double.parseDouble(utilitario.getFormatoNumero(total));
        double valor_a_pagar = Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tex_valor_pagar_cxp.getValue() + "")));
        System.out.println("total " + total + " valor a pagar " + valor_a_pagar);
        if (total < valor_a_pagar) {
            utilitario.agregarMensajeInfo("No se puede cancelar", "El valor a cancelar es mayor que el valor de la deuda");
            return false;
        }
        return true;
    }
    String str_num_doc_factura_cxp = "";
    String str_num_doc_factura_cxc = "";

    public void cargarPagoCxP(double total_a_pagar) {
        lis_fact_pagadas.clear();
        for (int i = 0; i < tab_cuentas_x_pagar.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;
            str_num_doc_factura_cxp = tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[3] + "";
            str_num_doc_factura_cxc = "";
            double valor_x_pagar = Double.parseDouble(tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[5] + "");
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getRowKey(), tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[5]};
                    lis_fact_pagadas.add(fila);
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    long ide_cpctr = Long.parseLong(tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getRowKey());
                    TablaGenerica tab_tiene_factura = utilitario.consultar("select * from cxp_detall_transa where ide_cpctr=" + ide_cpctr + " and ide_empr=" + utilitario.getVariable("ide_empr") + " "
                            + "group by ide_cpcfa,ide_cpdtr having ide_cpcfa is not null");
                    if (tab_tiene_factura.getTotalFilas() > 0) {
                        if (tab_tiene_factura.getValor(0, "ide_cpcfa") != null) {
                            utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set pagado_cpcfa=true where ide_cpcfa=" + tab_tiene_factura.getValor(0, "ide_cpcfa") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                        }
                    }
                } else {
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    Object fila[] = {tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(i).getRowKey(), (total_a_pagar)};
                    lis_fact_pagadas.add(fila);
                }
                total_a_pagar = monto_sobrante;
            } else {
                break;
            }
        }
        for (int i = 0; i < lis_fact_pagadas.size(); i++) {
            Object obj_fila[] = (Object[]) lis_fact_pagadas.get(i);
            System.out.println("ide_cpcfa " + obj_fila[0] + " ide_cpctr " + obj_fila[1] + " valor " + obj_fila[2]);
        }

    }

    public void cargarPagoCxC(double total_a_pagar) {
        lis_fact_pagadas.clear();
        for (int i = 0; i < tab_cuentas_x_cobrar.getListaFilasSeleccionadas().size(); i++) {
            double monto_sobrante = 0;
            str_num_doc_factura_cxc = tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[3] + "";
            str_num_doc_factura_cxp = "";
            double valor_x_pagar = Double.parseDouble(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5] + "");
            if (valor_x_pagar > 0) {
                if (total_a_pagar >= valor_x_pagar) {
                    Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[5]};
                    lis_fact_pagadas.add(fila);
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    long ide_cpctr = Long.parseLong(tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey());
                    TablaGenerica tab_tiene_factura = utilitario.consultar("select * from cxc_detall_transa where ide_ccctr=" + ide_cpctr + " and ide_empr=" + utilitario.getVariable("ide_empr") + " "
                            + "group by ide_cccfa,ide_ccdtr having ide_cccfa is not null");
                    if (tab_tiene_factura.getTotalFilas() > 0) {
                        if (tab_tiene_factura.getValor(0, "ide_cccfa") != null) {
                            utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set pagado_cccfa=true where ide_cccfa=" + tab_tiene_factura.getValor(0, "ide_cccfa") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                        }
                    }
                } else {
                    monto_sobrante = total_a_pagar - valor_x_pagar;
                    Object fila[] = {tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getCampos()[1], tab_cuentas_x_cobrar.getListaFilasSeleccionadas().get(i).getRowKey(), (total_a_pagar)};
                    lis_fact_pagadas.add(fila);
                }
                total_a_pagar = monto_sobrante;
            } else {
                break;
            }
        }
        for (int i = 0; i < lis_fact_pagadas.size(); i++) {
            Object obj_fila[] = (Object[]) lis_fact_pagadas.get(i);
            System.out.println("ide_cpcfa " + obj_fila[0] + " ide_cpctr " + obj_fila[1] + " valor " + obj_fila[2]);
        }

    }
    List lis_fact_pagadas = new ArrayList();

    public void cargarPagosMultiples(Tabla tabla, Double monto, boolean es_cuenta_x_pagar) {
        lis_fact_pagadas.clear();
        for (int i = 0; i < tabla.getTotalFilas(); i++) {
            double dif_acu = monto - Double.parseDouble(tabla.getValor(i, "saldo_x_pagar"));
            if (dif_acu >= 0) {
                if (monto != 0) {
                    if (es_cuenta_x_pagar) {
                        Object fila[] = {tabla.getValor(i, "ide_cpcfa"), tabla.getValor(i, "ide_cpctr"), tabla.getValor(i, "saldo_x_pagar")};
                        lis_fact_pagadas.add(fila);
                        List lis_tiene_factura = utilitario.getConexion().consultar("select ide_cpcfa from cxp_detall_transa where ide_cpctr=" + tabla.getValor(i, "ide_cpctr") + " and ide_empr=" + utilitario.getVariable("ide_empr") + " "
                                + "group by ide_cpcfa having ide_cpcfa is not null");
                        if (lis_tiene_factura.size() > 0) {
                            if (lis_tiene_factura.get(0) != null) {
                                utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set pagado_cpcfa=true where ide_cpcfa=" + lis_tiene_factura.get(0) + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                            }
                        }
                    } else {
                        Object fila[] = {tabla.getValor(i, "ide_cccfa"), tabla.getValor(i, "ide_ccctr"), tabla.getValor(i, "saldo_x_pagar")};
                        lis_fact_pagadas.add(fila);
                        List lis_tiene_factura = utilitario.getConexion().consultar("select ide_cccfa from cxc_detall_transa where ide_ccctr=" + tabla.getValor(i, "ide_ccctr") + " and ide_empr=" + utilitario.getVariable("ide_empr") + " "
                                + "group by ide_cccfa having ide_cccfa is not null");
                        if (lis_tiene_factura.size() > 0) {
                            if (lis_tiene_factura.get(0) != null) {
                                utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set pagado_cccfa=true where ide_cccfa=" + lis_tiene_factura.get(0) + " and ide_empr=" + utilitario.getVariable("ide_empr"));
                            }
                        }
                    }
                }
            } else {
                System.out.println("monto " + monto);
                if (monto != 0) {
                    if (es_cuenta_x_pagar) {
                        Object fila[] = {tabla.getValor(i, "ide_cpcfa"), tabla.getValor(i, "ide_cpctr"), utilitario.getFormatoNumero(monto)};
                        lis_fact_pagadas.add(fila);
                    } else {
                        Object fila[] = {tabla.getValor(i, "ide_cccfa"), tabla.getValor(i, "ide_ccctr"), utilitario.getFormatoNumero(monto)};
                        lis_fact_pagadas.add(fila);
                    }
                }
                break;
            }
            monto = dif_acu;
        }
    }

    public void aceptarDialogoCxC() {
        if (validarDialogoCxC()) {
            if (Double.parseDouble(tex_diferencia_cxc.getValue().toString()) >= 0) {
                cargarPagoCxC(Double.parseDouble(tex_valor_pagar_cxc.getValue() + ""));
                //cargarPagosMultiples(tab_cuentas_x_cobrar, Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()), false);
            } else {
                utilitario.agregarMensajeError("Atencion ", "No puede pagar mas dinero de lo que debe");
            }
            generarComprobanteContabilidadCxC();

        }
    }

    public void aceptarDialogoCxP() {
        if (validarDialogoCxP()) {
            System.out.println("lis seleccionadas " + tab_cuentas_x_pagar.getListaFilasSeleccionadas());
            System.out.println("lis seleccionadas (0) " + tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(0).getRowKey());
            System.out.println("lis seleccionadas (1) " + tab_cuentas_x_pagar.getListaFilasSeleccionadas().get(0).getCampos()[3]);
            if (Double.parseDouble(tex_diferencia.getValue().toString()) >= 0) {
                cargarPagoCxP(Double.parseDouble(tex_valor_pagar_cxp.getValue() + ""));
                //                cargarPagosMultiples(tab_cuentas_x_pagar, Double.parseDouble(tex_valor_pagar.getValue().toString()), true);
            } else {
                utilitario.agregarMensajeError("Atencion ", "No puede pagar mas dinero de lo que debe");
            }
//            dia_cuentas_x_pagar.cerrar();
//            utilitario.addUpdate("dia_cuentas_x_pagar,con_guardar");
            generarComprobanteContabilidadCxP();
        }
    }
    boolean tipo_cuenta_x_cobrar;

    public void generarComprobanteContabilidadCxC() {
        conta.limpiar();
        tipo_cuenta_x_cobrar = true;
        cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_ingreso, p_estado_comprobante_normal, p_modulo, aut_cliente_cxc.getValor(), cal_fecha_pago_cxc.getFecha(), ate_observacion_cxc.getValue() + "");
        lista_detalles.clear();
        String ide_cuenta_banco_cuenta_x_pagar = conta.buscarCuentaPersona("CUENTA POR COBRAR", aut_cliente_cxc.getValor());
        if (ide_cuenta_banco_cuenta_x_pagar != null) {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_banco_cuenta_x_pagar, Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()), ""));
        } else {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()), ""));
        }

        if (realizo_pago_sin_bancos_cxc == false) {

            String cuenta_contable_banco = banco.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_banco_cxc.getValue().toString());
            if (cuenta_contable_banco != null) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), cuenta_contable_banco, Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()), ""));
            }
        } else {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(tex_valor_pagar_cxc.getValue().toString()), ""));
        }

        cab_com_con.setDetalles(lista_detalles);
        via_asiento.setVistaAsiento(cab_com_con);
        via_asiento.dibujar();
        utilitario.addUpdate("via_asiento");

    }
    boolean tipo_cuenta_x_pagar;

    public void generarComprobanteContabilidadCxP() {
        conta.limpiar();
        tipo_cuenta_x_pagar = true;

        if (!ate_observacion_cxp.getValue().toString().isEmpty()) {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_egreso, p_estado_comprobante_normal, p_modulo, aut_proveedor.getValor(), cal_fecha_pago_cxp.getFecha(), ate_observacion_cxp.getValue().toString());
        } else {
            cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_egreso, p_estado_comprobante_normal, p_modulo, aut_proveedor.getValor(), cal_fecha_pago_cxp.getFecha(), "Pago a Proveedor: " + nombre_proveedor);
        }
        lista_detalles.clear();
        String ide_cuenta_banco_cuenta_x_pagar = conta.buscarCuentaPersona("CUENTA POR PAGAR", aut_proveedor.getValor());
        if (ide_cuenta_banco_cuenta_x_pagar != null && !ide_cuenta_banco_cuenta_x_pagar.isEmpty()) {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_banco_cuenta_x_pagar, Double.parseDouble(tex_valor_pagar_cxp.getValue() + ""), ""));
        } else {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(tex_valor_pagar_cxp.getValue().toString()), ""));
        }

        if (realizo_pago_sin_bancos_cxp == false) {
            String cuenta_contable_banco = banco.obtenerParametroCuentaBanco("ide_cndpc", com_cuenta_banco_cxp.getValue().toString());
            if (cuenta_contable_banco != null) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), cuenta_contable_banco, Double.parseDouble(tex_valor_pagar_cxp.getValue().toString()), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(tex_valor_pagar_cxp.getValue().toString()), ""));
            }
        } else {
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(tex_valor_pagar_cxp.getValue().toString()), ""));
        }

        cab_com_con.setDetalles(lista_detalles);
        via_asiento.setVistaAsiento(cab_com_con);
        via_asiento.dibujar();
        utilitario.addUpdate("via_asiento");

    }
    boolean tipo_tranferencia;

    public Combo getcom_cuenta_banco_cxp() {
        return com_cuenta_banco_cxp;
    }

    public void setcom_cuenta_banco_cxp(Combo com_cuenta_banco_cxp) {
        this.com_cuenta_banco_cxp = com_cuenta_banco_cxp;
    }

    public Texto getTex_valor_pagar() {
        return tex_valor_pagar_cxp;
    }

    public void setTex_valor_pagar(Texto tex_valor_pagar) {
        this.tex_valor_pagar_cxp = tex_valor_pagar;
    }

    public void abrirRangoFecha() {
        //Se ejecuta cuando da click en el boton Calendario de  la Barra    
        if (com_cue_ban.getValue() == null) {
            utilitario.agregarMensajeInfo("Atencion No se puede Filtrar ", "Debe seleccionar una cuenta bancaria ");
        } else {
            sec_calendario.dibujar();
        }
    }

    public void filtrar() {
        sec_calendario.cerrar();
        if (com_cue_ban.getValue() == null) {
        } else {
            tab_libro_banco.setCondicion("(fecha_trans_teclb between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "') and ide_tecba=" + com_cue_ban.getValue().toString() + " and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal"));
            tab_libro_banco.ejecutarSql();
        }
        calcularSaldo();
        utilitario.addUpdate("sec_calendario");
    }

    public void cargar_cuentas_cxp() {
        if (com_banco_cxp.getValue() != null) {
            banco_actual = com_banco_cxp.getValue().toString();
            com_cuenta_banco_cxp.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
            tex_num_cxp.setValue("");
        } else {
            com_cuenta_banco_cxp.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=-1");
            tex_num_cxp.setValue("");
        }
        utilitario.addUpdate("com_cuenta_banco_cxp,tex_num_cxp");
    }

    public void cargar_cuentas_cxc() {
        if (com_banco_cxc.getValue() != null) {
            banco_actual = com_banco_cxc.getValue().toString();
            com_cuenta_banco_cxc.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
        } else {
            com_cuenta_banco_cxc.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=-1");
        }
        utilitario.addUpdate("com_cuenta_banco_cxc");
    }

    public void cargar_cuentas_otros() {
        if (com_banco_otros.getValue() != null) {
            banco_actual = com_banco_otros.getValue().toString();
            com_cuenta_banco_otros.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
        } else {
            com_cuenta_banco_otros.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=-1");
        }
        tex_num_otros.setValue(null);

        utilitario.addUpdate("tex_num_otros,com_cuenta_banco_otros");
    }

    public void cargar_cuentas_transferencia() {
        if (com_banco_transf.getValue() != null) {
            if (!ide_cuenta_banco2.isEmpty()) {
                if (com_banco_transf.getValue().equals(com_banco_transf_2.getValue())) {
                    banco_actual = com_banco_transf.getValue().toString();
                    com_cuenta_banco_transf.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual + " and ide_tecba !=" + ide_cuenta_banco2);
                    utilitario.addUpdate("com_cuenta_banco_transf");
                } else {
                    if (com_banco_transf.getValue() != null) {
                        banco_actual = com_banco_transf.getValue().toString();
                        com_cuenta_banco_transf.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
                        utilitario.addUpdate("com_cuenta_banco_transf");
                    }
                    if (com_banco_transf_2.getValue() != null) {
                        banco_actual = com_banco_transf_2.getValue().toString();
                        com_cuenta_banco_transf_2.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
                        utilitario.addUpdate("com_cuenta_banco_transf_2");
                    }
                }
            } else {
                banco_actual = com_banco_transf.getValue().toString();
                com_cuenta_banco_transf.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
                utilitario.addUpdate("com_cuenta_banco_transf");
            }

        } else {
            com_cuenta_banco_transf.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=-1");
            utilitario.addUpdate("com_cuenta_banco_transf");
        }

    }
    private String ide_cuenta_banco2 = "";

    public void cargar_cuenta_escogida_2() {
        ide_cuenta_banco2 = "";
        if (com_cuenta_banco_transf_2.getValue() != null) {
            ide_cuenta_banco2 = com_cuenta_banco_transf_2.getValue().toString();
            if (com_banco_transf.getValue() != null) {
                if (com_banco_transf.getValue().equals(com_banco_transf_2.getValue())) {
                    banco_actual = com_banco_transf.getValue().toString();
                    com_cuenta_banco_transf.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual + " and ide_tecba !=" + com_cuenta_banco_transf_2.getValue().toString());
                    utilitario.addUpdate("com_cuenta_banco_transf");
                }
            }
        } else {
            ide_cuenta_banco2 = "";

            if (com_banco_transf.getValue() != null) {
                if (com_banco_transf.getValue().equals(com_banco_transf_2.getValue())) {
                    banco_actual = com_banco_transf.getValue().toString();
                    com_cuenta_banco_transf.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
                    utilitario.addUpdate("com_cuenta_banco_transf_2");
                }
            }
        }
        utilitario.addUpdate("com_cuenta_banco_transf,com_banco_transf_2,com_banco_transf");
    }

    public void cargar_cuenta_escogida() {
        if (com_cuenta_banco_transf.getValue() != null && !com_cuenta_banco_transf.getValue().toString().isEmpty()) {
            if (com_banco_transf_2.getValue() != null && !com_banco_transf_2.getValue().toString().isEmpty()) {
                if (com_banco_transf.getValue().equals(com_banco_transf_2.getValue())) {
                    banco_actual = com_banco_transf_2.getValue().toString();
                    com_cuenta_banco_transf_2.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual + " and ide_tecba !=" + com_cuenta_banco_transf.getValue().toString());
                    utilitario.addUpdate("com_cuenta_banco_transf_2");
                }
            }
        } else {
            if (com_banco_transf_2.getValue() != null && !com_banco_transf_2.getValue().toString().isEmpty()) {
                if (com_banco_transf.getValue().equals(com_banco_transf_2.getValue())) {
                    banco_actual = com_banco_transf_2.getValue().toString();
                    com_cuenta_banco_transf_2.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
                    utilitario.addUpdate("com_cuenta_banco_transf_2");
                }
            }
        }
    }

    public void cargar_cuentas_transferencia_2() {
        ide_cuenta_banco2 = "";
        if (com_banco_transf_2.getValue() != null) {
            if (com_cuenta_banco_transf.getValue() != null) {
                if (com_banco_transf.getValue().equals(com_banco_transf_2.getValue())) {
                    banco_actual = com_banco_transf_2.getValue().toString();
                    com_cuenta_banco_transf_2.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual + " and ide_tecba !=" + com_cuenta_banco_transf.getValue());

                } else {
                    if (com_banco_transf.getValue() != null) {
                        banco_actual = com_banco_transf.getValue().toString();
                        com_cuenta_banco_transf.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
                        utilitario.addUpdate("com_cuenta_banco_transf");
                    }
                    if (com_banco_transf_2.getValue() != null) {
                        banco_actual = com_banco_transf_2.getValue().toString();
                        com_cuenta_banco_transf_2.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
                        utilitario.addUpdate("com_cuenta_banco_transf_2");
                    }
                }
            } else {
                banco_actual = com_banco_transf_2.getValue().toString();
                com_cuenta_banco_transf_2.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
            }
        } else {
            com_cuenta_banco_transf_2.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=-1");
        }
        utilitario.addUpdate("com_cuenta_banco_transf_2,com_cuenta_banco_transf,com_banco_transf,com_banco_transf_2");
    }

    public void cargar_cuentas_bancarias() {
        tab_libro_banco.limpiar();
        if (com_bancos.getValue() != null) {
            banco_actual = com_bancos.getValue().toString();
            com_cue_ban.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=" + banco_actual);
        } else {
            com_cue_ban.setCombo("select ide_tecba,nombre_tecba from tes_cuenta_banco where ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_teban=-1");
            tab_libro_banco.setCondicion("ide_teclb=-1 and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal"));
            tab_libro_banco.ejecutarSql();
        }
        utilitario.addUpdate("tab_libro_banco,com_cue_ban");
    }

    @Override
    public void insertar() {
        if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.insertar();
        } else if (tab_tabla4.isFocus()) {
            if (com_tip_tran_otros.getValue() != null) {
                tab_tabla4.insertar();
                tab_tabla4.setValor("ide_cnccc", via_asiento.getTab_cab_comp_cont_vasiento().getValor("ide_cnccc"));
                String signo_tran = recuper_signo_transaccion_bancaria(com_tip_tran_otros.getValue().toString());
                if (Integer.parseInt(signo_tran) == 1) {
                    tab_tabla4.setValor("ide_cnlap", utilitario.getVariable("p_con_lugar_haber"));
                } else if (Integer.parseInt(signo_tran) == -1) {
                    tab_tabla4.setValor("ide_cnlap", utilitario.getVariable("p_con_lugar_debe"));
                }
            } else {
                utilitario.agregarMensajeError("No se puede insertar", "Debe seleccionar el tipo de Transaccion");
            }
        } else if (tab_libro_banco.isFocus()) {
            if (com_cue_ban.getValue() == null) {
                utilitario.agregarMensajeInfo("No se puede Insertar ", "Atencion debe seleccionar una cuenta bancaria ");
            } else {
                tab_libro_banco.insertar();
                tab_libro_banco.setValor("ide_tecba", com_cue_ban.getValue().toString());
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
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        //Se ejecuta cuando se selecciona un reporte de la lista
        if (rep_reporte.getReporteSelecionado().equals("Comprobante Contabilidad")) {
            if (rep_reporte.isVisible()) {
                if (tab_libro_banco.getValor("ide_cnccc") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cnccc", Long.parseLong(tab_libro_banco.getValor("ide_cnccc")));
                    parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                    parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("No se puede generar el reporte", "La fila seleccionada no tiene comprombante de contabilidad");
                }

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Cheque")) {
            if (rep_reporte.isVisible()) {
                if (tab_libro_banco.getTotalFilas() > 0) {
                    if (tab_libro_banco.getValor("ide_tettb").equals(utilitario.getVariable("p_tes_tran_cheque"))) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("beneficiario", tab_libro_banco.getValor("beneficiari_teclb") + "");
                        parametro.put("monto", tab_libro_banco.getValor("valor_teclb") + "");
                        parametro.put("anio", utilitario.getAnio(tab_libro_banco.getValor("fecha_trans_teclb")) + "");
                        parametro.put("mes", utilitario.getMes(tab_libro_banco.getValor("fecha_trans_teclb")) + "");
                        parametro.put("dia", utilitario.getDia(tab_libro_banco.getValor("fecha_trans_teclb")) + "");
                        parametro.put("monto_letras", banco.agregarAsteriscosCheque(utilitario.getLetrasDolarNumero(tab_libro_banco.getValor("valor_teclb"))));
                        parametro.put("ide_cnccc", Long.parseLong(tab_libro_banco.getValor("ide_cnccc")));
                        parametro.put("ide_cnlap_haber", utilitario.getVariable("p_con_lugar_haber"));
                        parametro.put("ide_cnlap_debe", utilitario.getVariable("p_con_lugar_debe"));
                        parametro.put("p_num_cheque", tab_libro_banco.getValor("numero_teclb") + "");
                        parametro.put("p_num_trans", tab_libro_banco.getValor("ide_teclb") + "");
                        List lis_geper = utilitario.getConexion().consultar("select identificac_geper from gen_persona where ide_geper=(select ide_geper from con_cab_comp_cont where ide_cnccc =" + tab_libro_banco.getValor("ide_cnccc") + ")");
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
                } else {
                    utilitario.agregarMensajeInfo("No se puede mostrar el Cheque ", "Debe seleccionar un Banco y una Cuenta");
                }

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Reporte Conciliados y No Conciliados(Sistema)")) {
            if (com_cue_ban.getValue() != null) {
                if (rep_reporte.isVisible()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    sec_rango_reporte.getCal_fecha1().setValue(null);
                    sec_rango_reporte.getCal_fecha2().setValue(null);
                    sec_rango_reporte.dibujar();
                    utilitario.addUpdate("rep_reporte,sec_rango_reporte");
                } else if (sec_rango_reporte.isVisible()) {
                    parametro.put("fecha_ini", sec_rango_reporte.getFecha1());
                    parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                    parametro.put("ide_teelb", Long.parseLong("0"));
                    parametro.put("ide_tecba", Long.parseLong(com_cue_ban.getValue() + ""));
                    parametro.put("ide_tettb", "0,2,3,4");
                    sec_rango_reporte.cerrar();
                    dia_filtro_activo.dibujar();
                } else if (dia_filtro_activo.isVisible()) {
                    dia_filtro_activo.cerrar();
                    String str_con = "";
                    if (che_conciliados.getValue().equals("true")) {
                        str_con += "true";
                    }
                    if (che_no_conciliados.getValue().equals("true")) {
                        if (!str_con.isEmpty()) {
                            str_con += ",";
                        }
                        str_con += "false";
                    }
                    System.out.println("str con  " + str_con);
                    parametro.put("conciliado", str_con);
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep");
                }
            } else {
                utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una cuenta bancaria en la barra de botones de la pantalla");
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Reporte Conciliados y No Conciliados(Banco)")) {
            if (com_cue_ban.getValue() != null) {
                if (rep_reporte.isVisible()) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    sec_rango_reporte.getCal_fecha1().setValue(null);
                    sec_rango_reporte.getCal_fecha2().setValue(null);
                    sec_rango_reporte.dibujar();
                    utilitario.addUpdate("rep_reporte,sec_rango_reporte");
                } else if (sec_rango_reporte.isVisible()) {
                    parametro.put("fecha_ini", sec_rango_reporte.getFecha1());
                    parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                    sec_rango_reporte.cerrar();

                    dia_filtro_activo.dibujar();
                } else if (dia_filtro_activo.isVisible()) {
                    dia_filtro_activo.cerrar();
                    String str_con = "";
                    if (che_conciliados.getValue().equals("true")) {
                        str_con += "true";
                    }
                    if (che_no_conciliados.getValue().equals("true")) {
                        if (!str_con.isEmpty()) {
                            str_con += ",";
                        }
                        str_con += "false";
                    }
                    System.out.println("str con  " + str_con);
                    parametro.put("conciliados", str_con);
                    parametro.put("ide_tecba", Long.parseLong(com_cue_ban.getValue() + ""));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("sel_rep");

                }
            } else {
                utilitario.agregarMensajeInfo("No se puede generar el reporte", "Debe seleccionar una cuenta bancaria en la barra de botones de la pantalla");
            }

        }
    }

    public void verCheque() {
        System.out.println("Si ingresa al metodo");
        if (tab_libro_banco.getTotalFilas() > 0) {
            generar_cheque();
        } else {
            utilitario.agregarMensajeInfo("Nose puede mostrar el Cheque", "Debe seleccionar Un Banco y una Numero de Cuenta");
        }

    }

    public void calcularSaldo() {
        double saldo = 0;
        for (int i = 0; i < tab_libro_banco.getTotalFilas(); i++) {
            try {
                String signo = banco.obtener_signo_transaccion(tab_libro_banco.getValor(i, "ide_tettb"));

                if (signo != null) {
                    saldo += Double.parseDouble(tab_libro_banco.getValor(i, "valor_teclb")) * Integer.parseInt(signo);
                }

            } catch (Exception e) {
            }
        }
        eti_saldo.setValue("Saldo: " + utilitario.getFormatoNumero(saldo));

        utilitario.addUpdate("eti_saldo");
    }

    @Override
    public void guardar() {
        //con_guardar.dibujar();
        if (tab_libro_banco.isFocus()) {
            tab_libro_banco.guardar();
            utilitario.getConexion().guardarPantalla();
//            tab_libro_banco.setCondicion("(fecha_trans_teclb between '" + sec_calendario.getFecha1String() + "' and '" + sec_calendario.getFecha2String() + "') and ide_tecba=" + com_cue_ban.getValue().toString());
//            tab_libro_banco.ejecutarSql();
        }

    }

    @Override
    public void eliminar() {
        if (tab_tabla4.isFocus()) {
            tab_tabla4.eliminar();
        } else if (via_asiento.getTab_det_comp_cont_vasiento().isFocus()) {
            via_asiento.eliminar();
        } else if (tab_libro_banco.isFocus()) {
            tab_libro_banco.eliminar();

        }
    }

    public Tabla getTab_libro_banco() {
        return tab_libro_banco;
    }

    public void setTab_libro_banco(Tabla tab_libro_banco) {
        this.tab_libro_banco = tab_libro_banco;
    }

    public SeleccionCalendario getSec_calendario() {
        return sec_calendario;
    }

    public void setSec_calendario(SeleccionCalendario sec_calendario) {
        this.sec_calendario = sec_calendario;
    }

    public Dialogo getDia_cuentas_x_pagar() {
        return dia_cuentas_x_pagar;
    }

    public void setDia_cuentas_x_pagar(Dialogo dia_cuentas_x_pagar) {
        this.dia_cuentas_x_pagar = dia_cuentas_x_pagar;
    }

    public Dialogo getdia_cuentas_x_cobrar() {
        return dia_cuentas_x_cobrar;
    }

    public void setdia_cuentas_x_cobrar(Dialogo dia_cuentas_x_cobrar) {
        this.dia_cuentas_x_cobrar = dia_cuentas_x_cobrar;
    }

    public Tabla getTab_cuentas_x_pagar() {
        return tab_cuentas_x_pagar;
    }

    public void setTab_cuentas_x_pagar(Tabla tab_cuentas_x_pagar) {
        this.tab_cuentas_x_pagar = tab_cuentas_x_pagar;
    }

    public Tabla getTab_cuentas_x_cobrar() {
        return tab_cuentas_x_cobrar;
    }

    public void setTab_cuentas_x_cobrar(Tabla tab_cuentas_x_cobrar) {
        this.tab_cuentas_x_cobrar = tab_cuentas_x_cobrar;
    }

    public AutoCompletar getAut_cliente_cxc() {
        return aut_cliente_cxc;
    }

    public void setAut_cliente_cxc(AutoCompletar aut_cliente_cxc) {
        this.aut_cliente_cxc = aut_cliente_cxc;
    }

    public Confirmar getCon_guardar() {
        return con_guardar;
    }

    public void setCon_guardar(Confirmar con_guardar) {
        this.con_guardar = con_guardar;
    }

    public VisualizarPDF getVis_pdf() {
        return vis_pdf;
    }

    public void setVis_pdf(VisualizarPDF vis_pdf) {
        this.vis_pdf = vis_pdf;
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

    public Dialogo getDia_cuentas_x_cobrar() {
        return dia_cuentas_x_cobrar;
    }

    public void setDia_cuentas_x_cobrar(Dialogo dia_cuentas_x_cobrar) {
        this.dia_cuentas_x_cobrar = dia_cuentas_x_cobrar;
    }

    public Dialogo getDia_otros() {
        return dia_otros;
    }

    public void setDia_otros(Dialogo dia_otros) {
        this.dia_otros = dia_otros;
    }

    public AutoCompletar getAut_cliente_otros() {
        return aut_cliente_otros;
    }

    public void setAut_cliente_otros(AutoCompletar aut_cliente_otros) {
        this.aut_cliente_otros = aut_cliente_otros;
    }

    public Tabla getTab_tabla4() {
        return tab_tabla4;
    }

    public void setTab_tabla4(Tabla tab_tabla4) {
        this.tab_tabla4 = tab_tabla4;
    }

    public AutoCompletar getAut_proveedor() {
        return aut_proveedor;
    }

    public void setAut_proveedor(AutoCompletar aut_proveedor) {
        this.aut_proveedor = aut_proveedor;
    }

    public Etiqueta getEti_saldo() {
        return eti_saldo;
    }

    public void setEti_saldo(Etiqueta eti_saldo) {
        this.eti_saldo = eti_saldo;
    }

    public Dialogo getDia_transferir() {
        return dia_transferir;
    }

    public void setDia_transferir(Dialogo dia_transferir) {
        this.dia_transferir = dia_transferir;
    }

    public Tabla getTab_beneficiario() {
        return tab_beneficiario;
    }

    public void setTab_beneficiario(Tabla tab_beneficiario) {
        this.tab_beneficiario = tab_beneficiario;
    }

    public VistaAsiento getVia_asiento() {
        return via_asiento;
    }

    public void setVia_asiento(VistaAsiento via_asiento) {
        this.via_asiento = via_asiento;
    }

    public Dialogo getDia_conciliar() {
        return dia_conciliar;
    }

    public void setDia_conciliar(Dialogo dia_conciliar) {
        this.dia_conciliar = dia_conciliar;
    }

    public Upload getUpl_archivo() {
        return upl_archivo;
    }

    public void setUpl_archivo(Upload upl_archivo) {
        this.upl_archivo = upl_archivo;
    }

    public Tabla getTab_parametros_conciliacion() {
        return tab_parametros_conciliacion;
    }

    public void setTab_parametros_conciliacion(Tabla tab_parametros_conciliacion) {
        this.tab_parametros_conciliacion = tab_parametros_conciliacion;
    }

    public SeleccionTabla getSet_datos_conciliados() {
        return set_datos_conciliados;
    }

    public void setSet_datos_conciliados(SeleccionTabla set_datos_conciliados) {
        this.set_datos_conciliados = set_datos_conciliados;
    }

//    public Dialogo getDia_pago_salud() {
//        return dia_pago_salud;
//    }
//
//    public void setDia_pago_salud(Dialogo dia_pago_salud) {
//        this.dia_pago_salud = dia_pago_salud;
//    }
//
//    public Tabla getTab_salud_x_pagar() {
//        return tab_salud_x_pagar;
//    }
//
//    public void setTab_salud_x_pagar(Tabla tab_salud_x_pagar) {
//        this.tab_salud_x_pagar = tab_salud_x_pagar;
//    }
//    public AutoCompletar getAut_proveedor_ps() {
//        return aut_proveedor_ps;
//    }
//
//    public void setAut_proveedor_ps(AutoCompletar aut_proveedor_ps) {
//        this.aut_proveedor_ps = aut_proveedor_ps;
//    }
    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

    public Dialogo getDia_filtro_activo() {
        return dia_filtro_activo;
    }

    public void setDia_filtro_activo(Dialogo dia_filtro_activo) {
        this.dia_filtro_activo = dia_filtro_activo;
    }
}
