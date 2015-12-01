/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_bancos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import pkg_contabilidad.*;
import pkg_cuentas_x_pagar.cls_cuentas_x_pagar;
import pkg_inventario.cls_inventario;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_reembolso_cajas extends Pantalla {
    
    private Division div_division = new Division();
    private Boton bot_generar_facturas = new Boton();
    private Texto tex_num_facturas = new Texto();
    private Dialogo dia_facturas = new Dialogo();
    cls_contabilidad conta = new cls_contabilidad();
    private Confirmar con_confirma_rete = new Confirmar();
    private VistaRetencion vir_comprobante_retencion = new VistaRetencion();
    private Tabla tab_factura_generadas = new Tabla();
    private Tabla tab_cab_factura_visual = new Tabla();
    private Tabla tab_det_factura_visual = new Tabla();
    private Tabla tab_cab_fact = new Tabla();
    private Tabla tab_det_fact = new Tabla();
    private Tabla tab_cab_tran_cxp = new Tabla();
    private Tabla tab_det_tran_cxp = new Tabla();
    private Tabla tab_cab_retencion = new Tabla();
    private Tabla tab_det_retencion = new Tabla();
    private Tabla tab_cab_comp_inv = new Tabla();
    private Tabla tab_det_comp_inv = new Tabla();
// COMPONENTES PARA PAGOS AL CONTADO     
    private Dialogo dia_banco = new Dialogo();
    private Combo com_banco = new Combo();
    private Combo com_cuenta_banco = new Combo();
    private String banco_actual = "-1";
    private Texto tex_num_cheque = new Texto();
    private Texto tex_monto_cheque = new Texto();
    private Etiqueta eti_banc = new Etiqueta();
    private Etiqueta eti_cue_banc = new Etiqueta();
    private Etiqueta eti_num_cheque = new Etiqueta();
    private Etiqueta eti_monto_cheque = new Etiqueta();
    private Calendario cal_fecha_cheque = new Calendario();
    private Etiqueta eti_fecha_cheque = new Etiqueta();
// VISTA ASIENTO
    private VistaAsiento via_comprobante_conta = new VistaAsiento();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();
    private VisualizarPDF vp = new VisualizarPDF();
    private Dialogo dia_datos_factura = new Dialogo();
    private Tabla tab_tabla_df = new Tabla();
    
    public pre_reembolso_cajas() {
        tex_num_facturas.setSoloEnteros();
        bar_botones.agregarComponente(new Etiqueta("Numero de Facturas"));
        bar_botones.agregarComponente(tex_num_facturas);
        bar_botones.agregarBoton(bot_generar_facturas);
        bot_generar_facturas.setValue("Generar Facturas");
        
        bot_generar_facturas.setTitle("Generar Facturas");
        bot_generar_facturas.setMetodo("generarFacturas");
        
        tab_cab_factura_visual.setId("tab_cab_factura_visual");
        tab_cab_factura_visual.setTabla("cxp_cabece_factur", "ide_cpcfa", 1);
        tab_cab_factura_visual.getColumna("ide_cpcfa").setLectura(true);
        tab_cab_factura_visual.getColumna("ide_cpcfa").setVisible(false);
        tab_cab_factura_visual.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid=" + utilitario.getVariable("p_gen_tipo_iden_ruc"));
        tab_cab_factura_visual.getColumna("ide_geper").setAutoCompletar();
        tab_cab_factura_visual.getColumna("ide_geper").setOrden(3);
        tab_cab_factura_visual.getColumna("ide_usua").setVisible(false);
        tab_cab_factura_visual.getColumna("ide_cntdo").setCombo("con_tipo_document", "ide_cntdo", "nombre_cntdo", "ide_cntdo in (" + utilitario.getVariable("p_con_tipo_documento_factura") + "," + utilitario.getVariable("p_con_tipo_documento_liquidacion_compra") + "," + utilitario.getVariable("p_con_tipo_documento_nota_venta") + ")");
        tab_cab_factura_visual.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));
        tab_cab_factura_visual.getColumna("ide_cntdo").setMetodoChange("cambioTipoDocumento");
        tab_cab_factura_visual.getColumna("ide_cntdo").setOrden(2);
        tab_cab_factura_visual.getColumna("ide_cpefa").setCombo("cxp_estado_factur", "ide_cpefa", "nombre_cpefa", "");
        tab_cab_factura_visual.getColumna("ide_cpefa").setValorDefecto(utilitario.getVariable("p_cxp_estado_factura_normal"));
        tab_cab_factura_visual.getColumna("ide_cpefa").setLectura(true);
        tab_cab_factura_visual.getColumna("valor_ice_cpcfa").setVisible(false);
        tab_cab_factura_visual.getColumna("otros_cpcfa").setVisible(false);
        tab_cab_factura_visual.getColumna("otros_cpcfa").setValorDefecto("0");
        tab_cab_factura_visual.getColumna("otros_cpcfa").setMetodoChange("calcula_total_detalles_cxp");
        tab_cab_factura_visual.getColumna("pagado_cpcfa").setValorDefecto("False");
        tab_cab_factura_visual.getColumna("pagado_cpcfa").setVisible(false);
        tab_cab_factura_visual.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_factura_visual.getColumna("fecha_trans_cpcfa").setVisible(false);
        tab_cab_factura_visual.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_factura_visual.getColumna("ide_srtst").setValorDefecto(utilitario.getVariable("p_sri_tip_sus_tri02"));
        tab_cab_factura_visual.getColumna("ide_srtst").setOrden(6);
        tab_cab_factura_visual.getColumna("ide_srtst").setCombo("sri_tipo_sustento_tributario", "ide_srtst", "alterno_srtst,nombre_srtst", "");
//        tab_cab_factura_visual.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp in (" + utilitario.getVariable("p_con_for_pag_efec") + ", " + utilitario.getVariable("p_con_for_pag_reembolso_caja") + "," + utilitario.getVariable("p_con_for_pag_cheque") + ")");
        tab_cab_factura_visual.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp in (" + utilitario.getVariable("p_con_for_pag_efec") + ", " + utilitario.getVariable("p_con_for_pag_cheque") + ")");
        tab_cab_factura_visual.getColumna("ide_cndfp").setPermitirNullCombo(false);
        tab_cab_factura_visual.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
        tab_cab_factura_visual.getColumna("total_cpcfa").setEtiqueta();
        tab_cab_factura_visual.getColumna("total_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura_visual.getColumna("total_cpcfa").setValorDefecto("0");
        tab_cab_factura_visual.getColumna("numero_cpcfa").setEstilo("font-size: 15px;font-weight: bold");
        tab_cab_factura_visual.getColumna("numero_cpcfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
        tab_cab_factura_visual.getColumna("numero_cpcfa").setMetodoChange("aceptarDatosFactura");
        tab_cab_factura_visual.getColumna("numero_cpcfa").setMascara("999-999-99999999");
        tab_cab_factura_visual.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_cab_factura_visual.getColumna("autorizacio_cpcfa").setLectura(false);
        tab_cab_factura_visual.getColumna("base_grabada_cpcfa").setEtiqueta();
        tab_cab_factura_visual.getColumna("base_grabada_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura_visual.getColumna("base_grabada_cpcfa").setValorDefecto("0");
        tab_cab_factura_visual.getColumna("valor_iva_cpcfa").setEtiqueta();
        tab_cab_factura_visual.getColumna("valor_iva_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura_visual.getColumna("valor_iva_cpcfa").setValorDefecto("0");
        tab_cab_factura_visual.getColumna("base_no_objeto_iva_cpcfa").setEtiqueta();
        tab_cab_factura_visual.getColumna("base_no_objeto_iva_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura_visual.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
        tab_cab_factura_visual.getColumna("base_tarifa0_cpcfa").setEtiqueta();
        tab_cab_factura_visual.getColumna("base_tarifa0_cpcfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura_visual.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
        tab_cab_factura_visual.getColumna("ide_cncre").setLectura(true);
        tab_cab_factura_visual.getColumna("ide_cnccc").setLectura(true);
        tab_cab_factura_visual.getColumna("valor_ice_cpcfa").setValorDefecto("0");
        
        tab_cab_factura_visual.setCondicion("ide_cpcfa=-1");
        tab_cab_factura_visual.setTipoFormulario(true);
        tab_cab_factura_visual.getGrid().setColumns(6);
        tab_cab_factura_visual.agregarRelacion(tab_det_factura_visual);
        utilitario.buscarNombresVisuales(tab_cab_factura_visual);
        tab_cab_factura_visual.setGenerarPrimaria(false);
        tab_cab_factura_visual.getColumna("ide_cpcfa").setExterna(false);
        tab_cab_factura_visual.dibujar();
        
        tab_det_factura_visual.setId("tab_det_factura_visual");
        tab_det_factura_visual.setTabla("cxp_detall_factur", "ide_cpdfa", 2);
        tab_det_factura_visual.setGenerarPrimaria(false);
        tab_det_factura_visual.getColumna("ide_cpdfa").setLectura(true);
        tab_det_factura_visual.getColumna("ide_cpdfa").setVisible(false);
        tab_det_factura_visual.getColumna("ide_cpdfa").setExterna(false);
        tab_det_factura_visual.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_det_factura_visual.getColumna("ide_inarti").setAutoCompletar();
        tab_det_factura_visual.getColumna("ide_inarti").setMetodoChange("obtener_total_detalle_factura_cxp1");
        tab_det_factura_visual.getColumna("cantidad_cpdfa").setMetodoChange("obtener_total_detalle_factura_cxp");
        tab_det_factura_visual.getColumna("precio_cpdfa").setMetodoChange("obtener_total_detalle_factura_cxp");
        tab_det_factura_visual.getColumna("valor_cpdfa").setEtiqueta();
        tab_det_factura_visual.getColumna("valor_cpdfa").setEstilo("font-size:13px;font-weight: bold;");
        tab_det_factura_visual.getColumna("devolucion_cpdfa").setValorDefecto("0");
        tab_det_factura_visual.getColumna("alter_tribu_cpdfa").setRequerida(true);
        tab_det_factura_visual.getColumna("secuencial_cpdfa").setVisible(false);
//        tab_det_factura_visual.getColumna("alter_tribu_cpdfa").setCombo("select casillero_cncim,casillero_cncim,nombre_cncim from con_cabece_impues where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_cnimp=" + p_impuesto_renta);

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
        tab_det_factura_visual.getColumna("iva_inarti_cpdfa").setRadio(lista, "");
        tab_det_factura_visual.getColumna("iva_inarti_cpdfa").setMetodoChange("calcula_iva");
        utilitario.buscarNombresVisuales(tab_det_factura_visual);
        tab_det_factura_visual.setCondicion("ide_cpdfa=-1");
        tab_det_factura_visual.dibujar();
        
        PanelTabla pat_cab_fac = new PanelTabla();
        pat_cab_fac.setPanelTabla(tab_cab_factura_visual);
        
        PanelTabla pat_det_fac = new PanelTabla();
        pat_det_fac.setPanelTabla(tab_det_factura_visual);
        
        dia_facturas.setId("dia_facturas");
        dia_facturas.setWidth("90%");
        dia_facturas.setHeight("80%");
        dia_facturas.setDynamic(false);
        dia_facturas.setTitle("FACTURACION");
        
        Grid gri_facturacion = new Grid();
        gri_facturacion.setColumns(1);
        gri_facturacion.getChildren().add(pat_cab_fac);
        gri_facturacion.getChildren().add(pat_det_fac);
        dia_facturas.setDialogo(gri_facturacion);
        gri_facturacion.setStyle("width:" + (dia_facturas.getAnchoPanel() - 5) + "px;height:" + dia_facturas.getAltoPanel() + "px;overflow: auto;display: block;");
        dia_facturas.getBot_aceptar().setMetodo("aceptar_datos_factura");
//        dia_facturas.getBot_cancelar().setMetodo("cancelarDialogo");
        agregarComponente(dia_facturas);
        
        con_confirma_rete.setId("con_confirma_rete");
        con_confirma_rete.setWidgetVar("con_confirma_rete");
        con_confirma_rete.setMessage("Desea Generar el Comprobante de Retención");
        con_confirma_rete.getBot_aceptar().setValue("Si");
        con_confirma_rete.getBot_cancelar().setValue("No");
        con_confirma_rete.getBot_aceptar().setMetodo("generarComprobanteRetencion");
        con_confirma_rete.getBot_cancelar().setMetodo("noGeneraRetencion");
        
        utilitario.getPantalla().getChildren().add(con_confirma_rete);
        
        tab_cab_retencion.setId("tab_cab_retencion");
        tab_cab_retencion.setTabla("con_cabece_retenc", "ide_cncre", -1);
        tab_cab_retencion.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_cab_retencion.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_cab_retencion.getColumna("ide_cnere").setValorDefecto("0");// estado normal
        tab_cab_retencion.setCondicion("ide_cncre=-1");
        tab_cab_retencion.setGenerarPrimaria(false);
        tab_cab_retencion.getColumna("ide_cncre").setExterna(false);
        tab_cab_retencion.dibujar();
        
        tab_det_retencion.setId("tab_det_retencion");
        tab_det_retencion.setTabla("con_detall_retenc", "ide_cndre", -1);
        tab_det_retencion.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_det_retencion.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_det_retencion.setCondicion("ide_cndre=-1");
        tab_det_retencion.setGenerarPrimaria(false);
        tab_det_retencion.getColumna("ide_cndre").setExterna(false);
        tab_det_retencion.dibujar();
//*************** COMPROBANTE DE INVENTARIO ***************        
        tab_cab_comp_inv.setId("tab_cab_comp_inv");
        
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", -1);
        tab_cab_comp_inv.setCondicion("ide_incci=-1");
        tab_cab_comp_inv.setGenerarPrimaria(false);
        tab_cab_comp_inv.getColumna("ide_incci").setExterna(false);
        tab_cab_comp_inv.ejecutarSql();
        
        tab_det_comp_inv.setId("tab_det_comp_inv");
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", -1);
        tab_det_comp_inv.setCondicion("ide_indci=-1");
        tab_det_comp_inv.setGenerarPrimaria(false);
        tab_det_comp_inv.getColumna("ide_indci").setExterna(false);
        tab_det_comp_inv.ejecutarSql();

//        
// ************* PARA EL VISUALIZADOR DE COMPROBANTE DE RETENCION
        vir_comprobante_retencion.setId("vir_comprobante_retencion");
        vir_comprobante_retencion.getBot_aceptar().setMetodo("aceptarComprobanteRetencion");
        //vir_comprobante_retencion.getBot_cancelar().setMetodo("cancelarDialogo");
        vir_comprobante_retencion.setDynamic(false);
        
        agregarComponente(vir_comprobante_retencion);

        // CONFIGURACION DE LA TABLA 1 DE LA PANTALLA
        tab_factura_generadas.setId("tab_factura_generadas");
        tab_factura_generadas.setSql("select cf.ide_cpcfa as fact,cf.ide_cnccc,cre.ide_cncre,per.nom_geper, "
                + "cf.fecha_emisi_cpcfa, "
                + "cf.numero_cpcfa, "
                + "cf.autorizacio_cpcfa, "
                + "cf.total_cpcfa "
                + "from con_cabece_retenc cre "
                + "left join cxp_cabece_factur cf on cf.ide_cncre=cre.ide_cncre "
                + "left join gen_persona per on per.ide_geper=cf.ide_geper "
                + "left join con_detall_retenc dr on dr.ide_cncre=cre.ide_cncre "
                + "where cf.ide_cpcfa in (-1)");
        tab_factura_generadas.setCampoPrimaria("fact");
        tab_factura_generadas.setLectura(true);
        tab_factura_generadas.dibujar();
        
        div_division.setId("div_division");
        div_division.dividir1(tab_factura_generadas);
        
        agregarComponente(div_division);

// ******** PARA INSERTAR VARIAS CABECERAS DE FACTURAS A LA VES
        tab_cab_fact.setTabla("cxp_cabece_factur", "ide_cpcfa", -1);
        tab_cab_fact.setGenerarPrimaria(false);
        tab_cab_fact.getColumna("ide_cpcfa").setExterna(false);
        tab_cab_fact.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_cab_fact.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_cab_fact.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_fact.setCondicion("ide_cpcfa=-1");
        tab_cab_fact.ejecutarSql();
        
        tab_det_fact.setTabla("cxp_detall_factur", "ide_cpdfa", -1);
        tab_det_fact.setGenerarPrimaria(false);
        tab_det_fact.getColumna("ide_cpdfa").setExterna(false);
        tab_det_fact.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_det_fact.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_det_fact.setCondicion("ide_cpdfa=-1");
        tab_det_fact.ejecutarSql();
// ************ PARA INSERTAR VARIAS TRANSACCIONES CXP A LA VES
        tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr", -1);
        tab_cab_tran_cxp.setGenerarPrimaria(false);
        tab_cab_tran_cxp.getColumna("ide_cpctr").setExterna(false);
        tab_cab_tran_cxp.setCondicion("ide_cpctr=-1");
        tab_cab_tran_cxp.ejecutarSql();
        
        tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr", -1);
        tab_det_tran_cxp.setGenerarPrimaria(false);
        tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
        tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
        tab_det_tran_cxp.ejecutarSql();

//**************  dialogo pagos AL CONTADO **********
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
        dia_banco.getBot_aceptar().setMetodo("generarComprobanteContabilidad");

        //dia_banco.getBot_cancelar().setMetodo("cancelarDialogo");
        agregarComponente(dia_banco);

// ************* PARA EL VISUALIZADOR DE COMPROBANTE DE CONTABILIDAD
        via_comprobante_conta.setId("via_comprobante_conta");
        via_comprobante_conta.getBot_aceptar().setMetodo("aceptarComprobanteContabilidad");
        //via_comprobante_conta.getBot_cancelar().setMetodo("cancelarDialogo");
        via_comprobante_conta.setDynamic(false);
        
        agregarComponente(via_comprobante_conta);
        
        vp.setId("vp");
        agregarComponente(vp);

//***************** para dialogo datos factura            
        tab_tabla_df.setId("tab_tabla_df");
        tab_tabla_df.setTabla("cxp_datos_factura", "ide_cpdaf", 11);
        tab_tabla_df.setTipoFormulario(true);
        tab_tabla_df.getColumna("ide_geper").setCombo(tab_cab_factura_visual.getColumna("ide_geper").getListaCombo());
        tab_tabla_df.getColumna("ide_geper").setAutoCompletar();
        tab_tabla_df.getColumna("ide_geper").setLectura(true);
        tab_tabla_df.getColumna("autorizacion_cpdaf").setMascara("9999999999");
        tab_tabla_df.getGrid().setColumns(4);
        tab_tabla_df.setMostrarNumeroRegistros(false);
        utilitario.buscarNombresVisuales(tab_tabla_df);
        
        tab_tabla_df.dibujar();
        
        dia_datos_factura.setId("dia_datos_factura");
        dia_datos_factura.setTitle("Datos Factura");
        dia_datos_factura.setWidth("55%");
        dia_datos_factura.setHeight("40%");
        dia_datos_factura.getBot_aceptar().setMetodo("aceptarDialogoDatosFactura");
        //dia_datos_factura.getBot_cancelar().setMetodo("cancelarDialogo");

        Grid gri_datos_factura = new Grid();
        gri_datos_factura.setColumns(2);
        gri_datos_factura.setStyle("width:" + (dia_datos_factura.getAnchoPanel() - 5) + "px;height:" + dia_datos_factura.getAltoPanel() + "px;overflow: auto;display: block;");
        gri_datos_factura.getChildren().add(tab_tabla_df);
        
        dia_datos_factura.setDialogo(gri_datos_factura);
        agregarComponente(dia_datos_factura);
//*******************************************************************        

    }
    
    public void aceptarDialogoDatosFactura() {
        
        if (tab_tabla_df.getValor("ide_geper") != null && tab_tabla_df.getValor("serie_cpdaf") != null && tab_tabla_df.getValor("autorizacion_cpdaf") != null && tab_tabla_df.getValor("rango_final_cpdaf") != null && tab_tabla_df.getValor("rango_inicial_cpdaf") != null && tab_tabla_df.getValor("fecha_caducidad") != null) {
            String serie = tab_cab_factura_visual.getValor("numero_cpcfa").substring(0, 6);
            if (utilitario.isFechaMayor(utilitario.getFecha(tab_tabla_df.getValor("fecha_caducidad")), utilitario.getFecha(utilitario.getFechaActual()))) {
                if (serie.equals(tab_tabla_df.getValor("serie_cpdaf"))) {
                    int num_factura = Integer.parseInt(tab_cab_factura_visual.getValor("numero_cpcfa").substring(6, tab_cab_factura_visual.getValor("numero_cpcfa").length()));
                    int rango_inicial = Integer.parseInt(tab_tabla_df.getValor("rango_inicial_cpdaf"));
                    int rango_final = Integer.parseInt(tab_tabla_df.getValor("rango_final_cpdaf"));
                    if (rango_inicial < rango_final) {
                        
                        if (num_factura >= rango_inicial && num_factura <= rango_final) {
                            tab_cab_factura_visual.setValor("autorizacio_cpcfa", tab_tabla_df.getValor("autorizacion_cpdaf"));
                            dia_datos_factura.cerrar();
                            utilitario.addUpdate("dia_datos_factura,tab_cab_factura_visual");
                        } else {
                            utilitario.agregarMensajeInfo("No se puede guadar", "El número de factura ingresado no se encuentra en el rango de los datos de la factura");
                        }
                    } else {
                        utilitario.agregarMensajeInfo("No se puede guadar", "El rango inicial es mayor al rango final");
                    }
                    
                } else {
                    utilitario.agregarMensajeInfo("No se puede guadar", "El número de serie ingresado en la factura no es igual al ingresado");
                }
            }
        } else {
            utilitario.agregarMensajeInfo("No se pudo Guardar", "Debe ingresar todos los datos de la factura");
        }
    }
    
    public void aceptarDatosFactura(AjaxBehaviorEvent evt) {
        tab_cab_factura_visual.modificar(evt);
        
        if (tab_cab_factura_visual.getValor("ide_geper") != null) {
            if (tab_cab_factura_visual.getValor("numero_cpcfa").length() > 8) {
                tab_cab_factura_visual.setValor(tab_cab_factura_visual.getFilaActual(), "autorizacio_cpcfa", "");
                String serie = tab_cab_factura_visual.getValor("numero_cpcfa").substring(0, 6);
                String num_factura = tab_cab_factura_visual.getValor("numero_cpcfa").substring(6, tab_cab_factura_visual.getValor("numero_cpcfa").length());
                TablaGenerica tab_datos_fac = utilitario.consultar("SELECT * FROM cxp_datos_factura df WHERE "
                        + "df.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "and df.ide_geper=" + tab_cab_factura_visual.getValor("ide_geper") + " "
                        + "and df.serie_cpdaf='" + serie + "' "
                        + "and df.fecha_caducidad >='" + utilitario.getFechaActual() + "'");
                int numero_factura = Integer.parseInt(num_factura);
                int serie_factura = Integer.parseInt(serie);
                
                if (tab_datos_fac.getTotalFilas() > 0) {
                    TablaGenerica tab_valida_numero = utilitario.consultar("SELECT * FROM cxp_datos_factura df  "
                            + "WHERE df.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                            + "and df.ide_geper=" + tab_cab_factura_visual.getValor("ide_geper") + " "
                            + "and df.serie_cpdaf='" + serie + "' "
                            + "and df.fecha_caducidad >'" + utilitario.getFechaActual() + "' "
                            + "and rango_inicial_cpdaf<=" + numero_factura + " "
                            + "and rango_final_cpdaf >=" + numero_factura + " order by df.ide_cpdaf desc ");
                    if (tab_valida_numero.getTotalFilas() > 0) {
                        tab_cab_factura_visual.setValor(tab_cab_factura_visual.getFilaActual(), "autorizacio_cpcfa", tab_valida_numero.getValor(0, "autorizacion_cpdaf"));
                    } else {
                        //utilitario.agregarMensajeInfo("Numero no valido", "Ingrese un numero dentro del rango " + tab_datos_fac.getValor("rango_inicial_cpdaf") + "hasta: " + tab_datos_fac.getValor("rango_final_cpdaf"));
                        if (serie_factura <= numero_factura || serie_factura >= numero_factura) {
                            tab_tabla_df.limpiar();
                            String seriecaracter = tab_cab_factura_visual.getValor("numero_cpcfa").substring(0, 6);
                            tab_tabla_df.getColumna("serie_cpdaf").setValorDefecto(seriecaracter);
                            tab_tabla_df.getColumna("serie_cpdaf").setLectura(true);
                            tab_tabla_df.getColumna("ide_geper").setValorDefecto(tab_cab_factura_visual.getValor("ide_geper"));
                            tab_tabla_df.insertar();
                            dia_datos_factura.dibujar();
                            utilitario.addUpdate("dia_datos_factura");
                            dia_datos_factura.getBot_aceptar().setMetodo("aceptarDialogoDatosFactura");
                        }
                    }
                } else {
                    tab_tabla_df.limpiar();
                    String seriecaracter = tab_cab_factura_visual.getValor("numero_cpcfa").substring(0, 6);
                    tab_tabla_df.getColumna("serie_cpdaf").setValorDefecto(seriecaracter);
                    tab_tabla_df.getColumna("serie_cpdaf").setLectura(true);
                    tab_tabla_df.getColumna("ide_geper").setValorDefecto(tab_cab_factura_visual.getValor("ide_geper"));
                    tab_tabla_df.insertar();
                    
                    dia_datos_factura.dibujar();
                    utilitario.addUpdate("dia_datos_factura");
                    dia_datos_factura.getBot_aceptar().setMetodo("aceptarDialogoDatosFactura");
                }
                utilitario.addUpdateTabla(tab_cab_factura_visual, "autorizacio_cpcfa", "");
            } else {
                utilitario.agregarMensajeError("No se puede abrir los datos de la factura", "Número de factura No Valida");
            }
        } else {
            utilitario.agregarMensajeError("No se puede abrir los datos de la factura", "Debe ingresar un Proveedor");
        }
    }
    Map parametro = new HashMap();
    
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
                tab_cab_retencion.guardar();
                tab_det_retencion.guardar();
                utilitario.getConexion().guardarPantalla();
                tab_cab_fact.guardar();
                tab_det_fact.guardar();
                String ides_fact = "";
                for (int i = 0; i < lis_ide_fact_generadas.size(); i++) {
                    ides_fact += lis_ide_fact_generadas.get(i).toString().concat(",");
                }
                if (ides_fact.length() > 1) {
                    ides_fact = ides_fact.substring(0, ides_fact.length() - 1);
                }
                
                utilitario.getConexion().guardarPantalla();
                tab_cab_comp_inv.guardar();
                tab_det_comp_inv.guardar();
                bancos.getTab_cab_libro_banco().guardar();
                utilitario.getConexion().guardarPantalla();
                cxp.getTab_cab_tran_cxp().guardar();
                cxp.getTab_det_tran_cxp().guardar();
                utilitario.getConexion().guardarPantalla();
                via_comprobante_conta.cerrar();
// actualizo los ide_cnccc en todas las tablas que intervienen en el proceso
                String ides_bancos = "";
                for (int i = 0; i < lis_ide_bancos.size(); i++) {
                    ides_bancos += lis_ide_bancos.get(i).toString().concat(",");
                }
                if (ides_bancos.length() > 1) {
                    ides_bancos = ides_bancos.substring(0, ides_bancos.length() - 1);
                }
                if (!ides_bancos.isEmpty()) {
                    double sum = 0;
                    for (int i = 0; i < lis_ide_bancos_update.size(); i++) {
                        sum = sum + Double.parseDouble(lis_valor_ide_bancos_update.get(i) + "");
                    }
                    utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set ide_cnccc=" + ide_cnccc + " ,valor_teclb=valor_teclb+" + sum + " where ide_teclb in (" + ides_bancos + ")");
                }
                if (!ides_fact.isEmpty()) {
                    utilitario.getConexion().agregarSqlPantalla("update cxp_cabece_factur set ide_cnccc=" + ide_cnccc + " where ide_cpcfa in (" + ides_fact + ")");
                    utilitario.getConexion().agregarSqlPantalla("UPDATE con_cabece_retenc set ide_cnccc=" + ide_cnccc + " where ide_cncre in ( "
                            + "select ide_cncre from cxp_cabece_factur where ide_cpcfa in (" + ides_fact + "))");
                    utilitario.getConexion().agregarSqlPantalla("update cxp_detall_transa set ide_cnccc=" + ide_cnccc + " where ide_cpcfa in (" + ides_fact + ")");
                }
                utilitario.getConexion().guardarPantalla();
                via_comprobante_conta.cerrar();
                tab_factura_generadas.setSql("select cf.ide_cpcfa as fact,cf.ide_cnccc,cre.ide_cncre,per.nom_geper, "
                        + "cf.fecha_emisi_cpcfa, "
                        + "cf.numero_cpcfa, "
                        + "cf.autorizacio_cpcfa, "
                        + "cf.total_cpcfa "
                        + "from con_cabece_retenc cre "
                        + "left join cxp_cabece_factur cf on cf.ide_cncre=cre.ide_cncre "
                        + "left join gen_persona per on per.ide_geper=cf.ide_geper "
                        + "left join con_detall_retenc dr on dr.ide_cncre=cre.ide_cncre "
                        + "where cf.ide_cpcfa in (" + ides_fact + ")");
                tab_factura_generadas.ejecutarSql();
                utilitario.addUpdate("tab_factura_generadas");
                if (tab_cab_factura_visual.getValor("ide_cndfp").equals(utilitario.getVariable("p_con_for_pag_cheque"))) {
                    cls_bancos banco = new cls_bancos();
                    TablaGenerica tab_lib_banc = utilitario.consultar("select * from tes_cab_libr_banc where ide_cnccc=" + ide_cnccc);
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
            }
        }
        
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
    String p_tipo_tran_bancaria = "";
    double tot_retenido = 0;
    String tot_pagar = "";
    
    public void dibujarDialogoPagoContado() {
        eti_monto_cheque.setValue("Monto: ");
        tex_num_cheque.setValue(null);
        tex_monto_cheque.setDisabled(true);
        com_banco.setValue(null);
        com_cuenta_banco.setValue(null);
        cal_fecha_cheque.setValue(utilitario.getFecha(tab_cab_factura_visual.getValor("fecha_emisi_cpcfa")));
        
        if (tab_cab_factura_visual.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_efec"))) {
            eti_banc.setValue("Caja: ");
            eti_cue_banc.setValue("Caja (Detalle): ");
            eti_num_cheque.setRendered(false);
            tex_num_cheque.setRendered(false);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - tot_retenido), 2));
            tot_pagar = utilitario.getFormatoNumero((Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - tot_retenido), 2);
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is TRUE and ide_empr=" + utilitario.getVariable("ide_empr"));
            dia_banco.dibujar();
            utilitario.addUpdate("dia_banco");
            p_tipo_tran_bancaria = utilitario.getVariable("p_tes_tran_retiro_caja");
        } else if (tab_cab_factura_visual.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_cheque"))) {
            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Cheque: ");
            eti_num_cheque.setRendered(true);
            tex_num_cheque.setRendered(true);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - tot_retenido), 2));
            tot_pagar = utilitario.getFormatoNumero((Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - tot_retenido), 2);
            com_banco.setCombo("select ide_teban,nombre_teban from tes_banco where es_caja_teban is false and ide_empr=" + utilitario.getVariable("ide_empr"));
            p_tipo_tran_bancaria = utilitario.getVariable("p_tes_tran_cheque");
            dia_banco.dibujar();
            utilitario.addUpdate("dia_banco");
        } else if (tab_cab_factura_visual.getValor("ide_cndfp").toString().equals(utilitario.getVariable("p_con_for_pag_transferencia"))) {
            eti_banc.setValue("Banco: ");
            eti_cue_banc.setValue("Cuenta Banco: ");
            eti_num_cheque.setValue("Numero Transferencia: ");
            eti_num_cheque.setRendered(true);
            tex_num_cheque.setRendered(true);
            tex_monto_cheque.setValue(utilitario.getFormatoNumero((Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - tot_retenido), 2));
            tot_pagar = utilitario.getFormatoNumero((Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - tot_retenido), 2);
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
    private List lis_ide_fact_generadas = new ArrayList();
    
    public void insertarCabeceraFactura() {
        
        tab_cab_fact.insertar();
        tab_cab_fact.setValor("ide_cpcfa", ide_cpcfa + "");
        lis_ide_fact_generadas.add(ide_cpcfa + "");
        tab_cab_fact.setValor("ide_cntdo", tab_cab_factura_visual.getValor("ide_cntdo"));
        tab_cab_fact.setValor("ide_geper", tab_cab_factura_visual.getValor("ide_geper"));
        tab_cab_fact.setValor("ide_srtst", tab_cab_factura_visual.getValor("ide_srtst"));
        tab_cab_fact.setValor("ide_cpefa", tab_cab_factura_visual.getValor("ide_cpefa"));
        tab_cab_fact.setValor("ide_cndfp", tab_cab_factura_visual.getValor("ide_cndfp"));
        if (boo_hizo_retencion) {
            tab_cab_fact.setValor("ide_cncre", tab_cab_factura_visual.getValor("ide_cncre"));
        }
        tab_cab_fact.setValor("fecha_trans_cpcfa", tab_cab_factura_visual.getValor("fecha_trans_cpcfa"));
        tab_cab_fact.setValor("fecha_emisi_cpcfa", tab_cab_factura_visual.getValor("fecha_emisi_cpcfa"));
        tab_cab_fact.setValor("numero_cpcfa", tab_cab_factura_visual.getValor("numero_cpcfa"));
        tab_cab_fact.setValor("autorizacio_cpcfa", tab_cab_factura_visual.getValor("autorizacio_cpcfa"));
        tab_cab_fact.setValor("observacion_cpcfa", tab_cab_factura_visual.getValor("observacion_cpcfa"));
        tab_cab_fact.setValor("base_no_objeto_iva_cpcfa", tab_cab_factura_visual.getValor("base_no_objeto_iva_cpcfa"));
        tab_cab_fact.setValor("base_tarifa0_cpcfa", tab_cab_factura_visual.getValor("base_tarifa0_cpcfa"));
        tab_cab_fact.setValor("base_grabada_cpcfa", tab_cab_factura_visual.getValor("base_grabada_cpcfa"));
        tab_cab_fact.setValor("valor_iva_cpcfa", tab_cab_factura_visual.getValor("valor_iva_cpcfa"));
        tab_cab_fact.setValor("valor_ice_cpcfa", tab_cab_factura_visual.getValor("valor_ice_cpcfa"));
        tab_cab_fact.setValor("otros_cpcfa", tab_cab_factura_visual.getValor("otros_cpcfa"));
        tab_cab_fact.setValor("total_cpcfa", tab_cab_factura_visual.getValor("total_cpcfa"));
        tab_cab_fact.setValor("pagado_cpcfa", tab_cab_factura_visual.getValor("pagado_cpcfa"));
        
    }
    
    public void insertarDetalleFactura() {
        
        for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
            tab_det_fact.insertar();
            tab_det_fact.setValor("ide_cpdfa", ide_cpdfa + "");
            tab_det_fact.setValor("ide_cpcfa", ide_cpcfa + "");
            tab_det_fact.setValor("ide_inarti", tab_det_factura_visual.getValor(i, "ide_inarti"));
            tab_det_fact.setValor("secuencial_cpdfa", tab_det_factura_visual.getValor(i, "secuencial_cpdfa"));
            tab_det_fact.setValor("cantidad_cpdfa", tab_det_factura_visual.getValor(i, "cantidad_cpdfa"));
            tab_det_fact.setValor("precio_cpdfa", tab_det_factura_visual.getValor(i, "precio_cpdfa"));
            tab_det_fact.setValor("valor_cpdfa", tab_det_factura_visual.getValor(i, "valor_cpdfa"));
            tab_det_fact.setValor("credi_tribu_cpdfa", tab_det_factura_visual.getValor(i, "credi_tribu_cpdfa"));
            tab_det_fact.setValor("devolucion_cpdfa", tab_det_factura_visual.getValor(i, "devolucion_cpdfa"));
            tab_det_fact.setValor("alter_tribu_cpdfa", tab_det_factura_visual.getValor(i, "alter_tribu_cpdfa"));
            tab_det_fact.setValor("observacion_cpdfa", tab_det_factura_visual.getValor(i, "observacion_cpdfa"));
            tab_det_fact.setValor("iva_inarti_cpdfa", tab_det_factura_visual.getValor(i, "iva_inarti_cpdfa"));
            ide_cpdfa = ide_cpdfa + 1;
        }
    }
    
    public boolean realizarComprobanteInventario() {
        for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
            if (in.es_bien(tab_det_factura_visual.getValor(i, "ide_inarti"))) {
                return true;
            }
        }
        return false;
    }
    
    public void generarComprobanteInventario(String ide_cnccc) {
        if (realizarComprobanteInventario()) {
            //Cabecera 
            insertarCabeceraComprobanteInventario();
            //Detalles
            insertarDetalleComprobanteInventario();
        }
    }
    cls_inventario in = new cls_inventario();
    
    public void insertarCabeceraComprobanteInventario() {
        tab_cab_comp_inv.insertar();
        tab_cab_comp_inv.setValor("ide_incci", ide_incci + "");
        tab_cab_comp_inv.setValor("ide_geper", tab_cab_factura_visual.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("ide_inepi", utilitario.getVariable("p_inv_estado_normal"));
        tab_cab_comp_inv.setValor("ide_empr", utilitario.getVariable("ide_empr"));
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_cab_comp_inv.setValor("ide_intti", utilitario.getVariable("p_inv_tipo_transaccion_compra"));
        //tab_cab_comp_inv.setValor("ide_cnccc", "");
        tab_cab_comp_inv.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
        tab_cab_comp_inv.setValor("ide_inbod", "1");
        tab_cab_comp_inv.setValor("numero_incci", in.generarSecuencialComprobanteInventario(utilitario.getVariable("p_inv_tipo_transaccion_compra")));
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_cab_factura_visual.getValor("fecha_emisi_cpcfa"));
        tab_cab_comp_inv.setValor("observacion_incci", tab_cab_factura_visual.getValor("observacion_cpcfa"));
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
    }
    
    public void insertarDetalleComprobanteInventario() {
        for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
            if (in.es_bien(tab_det_factura_visual.getValor(i, "ide_inarti"))) {
                if (in.esTipoCombo(tab_det_factura_visual.getValor(i, "ide_inarti")) == false) {
                    tab_det_comp_inv.insertar();
                    tab_det_comp_inv.setValor("ide_indci", ide_indci + "");
                    ide_indci = ide_indci + 1;                    
                    if (in.aplicaIva(tab_det_factura_visual.getValor(i, "ide_inarti"))) {
                        try {
                            double precio = Double.parseDouble(tab_det_factura_visual.getValor(i, "precio_cpdfa")) * p_porcentaje_iva;
                            double valor = Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa")) * p_porcentaje_iva;
                            tab_det_comp_inv.setValor("precio_indci", precio + "");
                            tab_det_comp_inv.setValor("valor_indci", valor + "");
                            String precio_promedio = in.getPrecioPromedioTransaccionPositiva(tab_det_factura_visual.getValor(i, "ide_inarti"), "1", valor, Double.parseDouble(tab_det_factura_visual.getValor(i, "cantidad_cpdfa")));
                            if (precio_promedio != null) {
                                tab_det_comp_inv.setValor("precio_promedio_indci", precio_promedio);
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        tab_det_comp_inv.setValor("precio_indci", tab_det_factura_visual.getValor(i, "precio_cpdfa"));
                        tab_det_comp_inv.setValor("valor_indci", tab_det_factura_visual.getValor(i, "valor_cpdfa"));
                        String precio_promedio = in.getPrecioPromedioTransaccionPositiva(tab_det_factura_visual.getValor(i, "ide_inarti"), "1", Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa")), Double.parseDouble(tab_det_factura_visual.getValor(i, "cantidad_cpdfa")));
                        if (precio_promedio != null) {
                            tab_det_comp_inv.setValor("precio_promedio_indci", precio_promedio);
                        }
                    }
                    tab_det_comp_inv.setValor("ide_inarti", tab_det_factura_visual.getValor(i, "ide_inarti"));
                    tab_det_comp_inv.setValor("ide_cpcfa", ide_cpcfa + "");
                    tab_det_comp_inv.setValor("ide_incci", ide_incci + "");
                    tab_det_comp_inv.setValor("cantidad_indci", tab_det_factura_visual.getValor(i, "cantidad_cpdfa"));
                    tab_det_comp_inv.setValor("observacion_indci", tab_det_factura_visual.getValor(i, "observacion_cpdfa"));
                }
            }
        }
    }
    private int num_fac_registradas = 0;
    
    public void validaNumFact() {
        
        if (num_fac_registradas < Integer.parseInt(tex_num_facturas.getValue() + "")) {
            tab_cab_factura_visual.setCondicion("ide_cpcfa=-1");
            tab_cab_factura_visual.ejecutarSql();
            utilitario.addUpdate("tab_cab_factura_visual");
            tab_cab_factura_visual.insertar();
            dia_facturas.dibujar();
        } else {
            cab_com_con.setDetalles(lista_detalles);
            via_comprobante_conta.setVistaAsiento(cab_com_con);
            via_comprobante_conta.dibujar();
            
            utilitario.addUpdate("via_comprobante_conta");

//            tab_cab_retencion.guardar();
//            tab_det_retencion.guardar();
//            utilitario.getConexion().guardarPantalla();
//            tab_cab_fact.guardar();
//            tab_det_fact.guardar();
////            cxp.getTab_cab_fact().guardar();
////            cxp.getTab_det_fact().guardar();
//            String ides_fact = "";
////            List lis_fact_gen = cxp.getLis_ide_fact_generadas();
//            for (int i = 0; i < lis_ide_fact_generadas.size(); i++) {
//                ides_fact += lis_ide_fact_generadas.get(i).toString().concat(",");
//            }
//            if (ides_fact.length() > 1) {
//                ides_fact = ides_fact.substring(0, ides_fact.length() - 1);
//            }
//
//            utilitario.getConexion().guardarPantalla();
//            tab_cab_comp_inv.guardar();
//            tab_det_comp_inv.guardar();
//            bancos.getTab_cab_libro_banco().guardar();
//            utilitario.getConexion().guardarPantalla();
//            cxp.getTab_cab_tran_cxp().guardar();
//            cxp.getTab_det_tran_cxp().guardar();
//            utilitario.getConexion().guardarPantalla();
//            tab_factura_generadas.setSql("select cf.ide_cpcfa as fact,cf.ide_cnccc,cre.ide_cncre,per.nom_geper, "
//                    + "cf.fecha_emisi_cpcfa, "
//                    + "cf.numero_cpcfa, "
//                    + "cf.autorizacio_cpcfa, "
//                    + "cf.total_cpcfa "
//                    + "from con_cabece_retenc cre "
//                    + "left join cxp_cabece_factur cf on cf.ide_cncre=cre.ide_cncre "
//                    + "left join gen_persona per on per.ide_geper=cf.ide_geper "
//                    + "left join con_detall_retenc dr on dr.ide_cncre=cre.ide_cncre "
//                    + "where cf.ide_cpcfa in (" + ides_fact + ")");
//            tab_factura_generadas.ejecutarSql();
//            utilitario.addUpdate("tab_factura_generadas");
        }
        
    }
    private long ide_cpcfa = -1;
    private long ide_cpdfa = -1;
    private long ide_cncre = -1;
    private long ide_cndre = -1;
    private long ide_incci = -1;
    private long ide_indci = -1;
    private long ide_cpctr = -1;
    private long ide_cpdtr = -1;
    private long ide_teclb = -1;
    private List lis_ide_bancos = new ArrayList();
    private List lis_ide_cuenta_bancos = new ArrayList();
    private List lis_ide_bancos_update = new ArrayList();
    private List lis_valor_ide_bancos_update = new ArrayList();
    
    public synchronized void generarComprobanteContabilidad() {
        
        for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
            String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_det_factura_visual.getValor(i, "ide_inarti"));
            if (ide_cuenta_inv_gas_act != null) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(tab_det_factura_visual.getValor(i, "valor_cpdfa"), 2)), ""));
            } else {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_det_factura_visual.getValor(i, "valor_cpdfa"), 2)), getNombreAriculo(tab_det_factura_visual.getValor(i, "ide_inarti"))));
            }
        }
        
        if (boo_pago_efectivo) {
            String ide_cndpc = bancos.getParametroCuentaBanco(com_cuenta_banco.getValue() + "", "ide_tecba", "ide_cndpc");
            System.out.println("ide_cndpc banco " + ide_cndpc);
            if (ide_cndpc != null) {
                double valor = Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cndpc, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            } else {
                double valor = Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
            }
        }
        
        for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
            if (lis_cab_imp_ret.get(i) != null) {
                if (lis_cab_imp_ret.get(i).toString().equals(p_iva30) || lis_cab_imp_ret.get(i).toString().equals(p_iva70) || lis_cab_imp_ret.get(i).toString().equals(p_iva100)) {
                    String ide_cuenta_retencion_iva_x_pagar = conta.buscarCuenta("RETENCION IVA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                    if (ide_cuenta_retencion_iva_x_pagar != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_iva_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    } else {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    }
                } else {
                    String ide_cuenta_retencion_renta_x_pagar = conta.buscarCuenta("RETENCION RENTA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
                    if (ide_cuenta_retencion_renta_x_pagar != null) {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_renta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    } else {
                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
                    }
                }
            }
        }
        String ide_cuenta_credito_tributario = conta.buscarCuenta("IVA CREDITO TRIBUTARIO", null, null, null, utilitario.getVariable("p_con_porcentaje_imp_iva"), null, null);
        if (ide_cuenta_credito_tributario != null) {
            if (Double.parseDouble(tab_cab_factura_visual.getValor("valor_iva_cpcfa")) != 0) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_credito_tributario, Double.parseDouble(utilitario.getFormatoNumero(tab_cab_factura_visual.getValor("valor_iva_cpcfa"), 2)), ""));
            }
        } else {
            if (Double.parseDouble(tab_cab_factura_visual.getValor("valor_iva_cpcfa")) != 0) {
                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_cab_factura_visual.getValor("valor_iva_cpcfa"), 2)), ""));
            }
        }
        System.out.println("lista detalles " + lista_detalles.size());
        vir_comprobante_retencion.cerrar();
        dia_facturas.cerrar();
        
        if (num_fac_registradas == 0) {
            ide_cpcfa = utilitario.getConexion().getMaximo("cxp_cabece_factur", "ide_cpcfa",1);
            ide_cpdfa = utilitario.getConexion().getMaximo("cxp_detall_factur", "ide_cpdfa",1);
            ide_cpctr = utilitario.getConexion().getMaximo("cxp_cabece_transa", "ide_cpctr",1);
            ide_cpdtr = utilitario.getConexion().getMaximo("cxp_detall_transa", "ide_cpdtr",1);
            ide_cncre = utilitario.getConexion().getMaximo("con_cabece_retenc", "ide_cncre",1);
            ide_cndre = utilitario.getConexion().getMaximo("con_detall_retenc", "ide_cndre",1);
            ide_incci = utilitario.getConexion().getMaximo("inv_cab_comp_inve", "ide_incci",1);
            ide_indci = utilitario.getConexion().getMaximo("inv_det_comp_inve", "ide_indci",1);
            ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb",1);
        } else {
            ide_cpcfa = ide_cpcfa + 1;
            ide_cpdfa = ide_cpdfa + 1;
            ide_cpctr = ide_cpctr + 1;
            ide_cpdtr = ide_cpdtr + 1;
            ide_cncre = ide_cncre + 1;
            ide_cndre = ide_cndre + 1;
            ide_incci = ide_incci + 1;
            ide_indci = ide_indci + 1;
            ide_teclb = ide_teclb + 1;
        }
        
        tab_cab_factura_visual.setValor("ide_cpcfa", ide_cpcfa + "");
        
        if (boo_hizo_retencion) {
            tab_cab_factura_visual.setValor("ide_cncre", ide_cncre + "");
        }
        
        tab_det_factura_visual.setValor("ide_cpdfa", ide_cpdfa + "");
        tab_det_factura_visual.setValor("ide_cpcfa", ide_cpcfa + "");
        
        if (boo_hizo_retencion) {
            vir_comprobante_retencion.getTab_cab_retencion_vretencion().setValor("ide_cncre", ide_cncre + "");
            vir_comprobante_retencion.getTab_det_retencion_vretencion().setValor("ide_cncre", ide_cncre + "");
            vir_comprobante_retencion.getTab_det_retencion_vretencion().setValor("ide_cndre", ide_cndre + "");
        }
        num_fac_registradas = num_fac_registradas + 1;
        if (boo_hizo_retencion) {
            insertarCabeceraRetencion();
            insertarDetalleRetencion();
        }
        insertarCabeceraFactura();
        insertarDetalleFactura();
        if (boo_pago_efectivo) {
            int band = 0;
            for (int i = 0; i < lis_ide_cuenta_bancos.size(); i++) {
                if (com_cuenta_banco.getValue().equals(lis_ide_cuenta_bancos.get(i))) {
                    band = 1;
                    lis_ide_bancos_update.add(ide_teclb - 1);
                    lis_valor_ide_bancos_update.add(tot_pagar);
                    break;
                }
            }
            if (band == 0) {
                bancos.generarVariosLibrosBancos(tab_cab_factura_visual.getValor("ide_geper"), tab_cab_factura_visual.getValor("fecha_emisi_cpcfa"), bancos.buscarTransaccionCXP(tab_cab_factura_visual.getValor("ide_cndfp")), com_cuenta_banco.getValue() + "", "", Double.parseDouble(tot_pagar), tab_cab_factura_visual.getValor("observacion_cpcfa"), tex_num_cheque.getValue() + "");
                cxp.generarVariasTransaccionesCompra(tab_cab_factura_visual, tot_retenido, bancos.getTab_cab_libro_banco(), ide_cpctr + "", ide_cpdtr + "", false);
                bancos.getTab_cab_libro_banco().setValor("ide_teclb", ide_teclb + "");
                lis_ide_bancos.add(ide_teclb + "");
            } else {
                cxp.generarVariasTransaccionesCompra(tab_cab_factura_visual, tot_retenido, null, ide_cpctr + "", ide_cpdtr + "", true);
            }
            lis_ide_cuenta_bancos.add(com_cuenta_banco.getValue() + "");
            dia_banco.cerrar();
        }
        generarComprobanteInventario("");
        validaNumFact();
        
    }
    
    public String getNombreAriculo(String ide_inarti) {
        TablaGenerica tab_articulo = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_inarti);
        if (tab_articulo.getTotalFilas() > 0) {
            return tab_articulo.getValor(0, "nombre_inarti");
        } else {
            return null;
        }
    }
    private String p_tipo_comprobante_diario = utilitario.getVariable("p_con_tipo_comprobante_diario");
    private String p_tipo_comprobante_egreso = utilitario.getVariable("p_con_tipo_comprobante_egreso");
    private String p_estado_comprobante_normal = utilitario.getVariable("p_con_estado_comprobante_normal");
    private String p_modulo = "7";
//    public void generarComprobanteContabilidad1() {
//
//        //     conta.limpiar();
////        cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_egreso, p_estado_comprobante_normal, p_modulo, tab_cab_factura_visual.getValor("ide_geper"), tab_cab_factura_visual.getValor("fecha_emisi_cpcfa"), tab_cab_factura_visual.getValor("observacion_cpcfa"));
//
/////        lista_detalles.clear();
//
//        for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
//            String ide_cuenta_inv_gas_act = conta.buscarCuentaProducto("INVENTARIO-GASTO-ACTIVO", tab_det_factura_visual.getValor(i, "ide_inarti"));
//            System.out.println("gasto activo " + ide_cuenta_inv_gas_act);
//            if (ide_cuenta_inv_gas_act != null) {
//                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_inv_gas_act, Double.parseDouble(utilitario.getFormatoNumero(tab_det_factura_visual.getValor(i, "valor_cpdfa"), 2)), ""));
//            } else {
//                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), "", Double.parseDouble(utilitario.getFormatoNumero(tab_det_factura_visual.getValor(i, "valor_cpdfa"), 2)), getNombreAriculo(tab_det_factura_visual.getValor(i, "ide_inarti"))));
//            }
//        }
//
//
//        if (!tab_cab_factura_visual.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_anticipo"))) {
//            String ide_cuenta_cuentas_x_pagar = conta.buscarCuentaPersona("CUENTA POR PAGAR", tab_cab_factura_visual.getValor("ide_geper"));
//            double valor;
//            if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
//                valor = Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) - vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
//            } else {
//                valor = Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa"));
//            }
//            if (ide_cuenta_cuentas_x_pagar != null) {
//                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_cuentas_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
//            } else {
//                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), "", Double.parseDouble(utilitario.getFormatoNumero(valor, 2)), ""));
//            }
//        }
//
//        for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
//            if (lis_cab_imp_ret.get(i) != null) {
//                if (lis_cab_imp_ret.get(i).toString().equals(p_iva30) || lis_cab_imp_ret.get(i).toString().equals(p_iva70) || lis_cab_imp_ret.get(i).toString().equals(p_iva100)) {
//                    String ide_cuenta_retencion_iva_x_pagar = conta.buscarCuenta("RETENCION IVA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
//                    if (ide_cuenta_retencion_iva_x_pagar != null) {
//                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_iva_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
//                    }
//                } else {
//                    String ide_cuenta_retencion_renta_x_pagar = conta.buscarCuenta("RETENCION RENTA POR PAGAR", null, null, vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"), null, null, null);
//                    if (ide_cuenta_retencion_renta_x_pagar != null) {
//                        lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cuenta_retencion_renta_x_pagar, Double.parseDouble(utilitario.getFormatoNumero(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"), 2)), ""));
//                    }
//                }
//            }
//        }
//        String ide_cuenta_credito_tributario = conta.buscarCuenta("IVA CREDITO TRIBUTARIO", null, null, null, utilitario.getVariable("p_con_porcentaje_imp_iva"), null, null);
//        if (ide_cuenta_credito_tributario != null) {
//            if (Double.parseDouble(tab_cab_factura_visual.getValor("valor_iva_cpcfa")) != 0) {
//                lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cuenta_credito_tributario, Double.parseDouble(utilitario.getFormatoNumero(tab_cab_factura_visual.getValor("valor_iva_cpcfa"), 2)), ""));
//            }
//        }
//        System.out.println("lista detalles " + lista_detalles.size());
////        cab_com_con.setDetalles(lista_detalles);
//
////        via_comprobante_conta.setVistaAsiento(cab_com_con);
//
////        via_comprobante_conta.dibujar();
//
////        utilitario.addUpdate("via_comprobante_conta");
//
//    }
    private boolean boo_hizo_retencion = false;
    
    public void noGeneraRetencion() {
        vir_comprobante_retencion.getTab_cab_retencion_vretencion().limpiar();
        vir_comprobante_retencion.getTab_det_retencion_vretencion().limpiar();
        con_confirma_rete.cerrar();
        boo_hizo_retencion = false;
        aceptarComprobanteRetencion();
    }
    
    boolean boo_pago_efectivo = false;
    List lis_cab_imp_ret = new ArrayList();
    
    public synchronized void aceptarComprobanteRetencion() {
        
        if (vir_comprobante_retencion.getTab_datos_proveedor().isFilaModificada()) {
            utilitario.getConexion().agregarSqlPantalla("update gen_persona set direccion_geper ='" + vir_comprobante_retencion.getTab_datos_proveedor().getValor("direccion_geper") + "'where ide_geper =" + vir_comprobante_retencion.getTab_datos_proveedor().getValor("ide_geper") + " and ide_empr=" + utilitario.getVariable("ide_empr"));
        }
        if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() > 0) {
            tot_retenido = vir_comprobante_retencion.getTab_det_retencion_vretencion().getSumaColumna("valor_cndre");
        } else {
            tot_retenido = 0;
        }
        
        if (vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas() == 0) {
            boo_hizo_retencion = false;
        } else {
            boo_hizo_retencion = true;
        }
        
        lis_cab_imp_ret.clear();
        for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
            lis_cab_imp_ret.add(vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"));
        }
        
        List sql_forma_pago = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + tab_cab_factura_visual.getValor("ide_cndfp"));
        int dias = -1;
        if (!sql_forma_pago.isEmpty()) {
            dias = Integer.parseInt(sql_forma_pago.get(0).toString());
        }
        if (dias == 0) {
            if (!tab_cab_factura_visual.getValor("ide_cndfp").equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_reembolso_caja"))) {
                boo_pago_efectivo = true;
                dibujarDialogoPagoContado();
            } else {
                boo_pago_efectivo = false;
                generarComprobanteContabilidad();
            }
        }
        
    }
    
    public void insertarCabeceraRetencion() {
        tab_cab_retencion.insertar();
        tab_cab_retencion.setValor("ide_cncre", ide_cncre + "");
        tab_cab_retencion.setValor("ide_cnccc", "");
        tab_cab_retencion.setValor("fecha_emisi_cncre", vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("fecha_emisi_cncre"));
        tab_cab_retencion.setValor("observacion_cncre", vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("observacion_cncre"));
        tab_cab_retencion.setValor("numero_cncre", vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("numero_cncre"));
        tab_cab_retencion.setValor("autorizacion_cncre", vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("autorizacion_cncre"));
        tab_cab_retencion.setValor("es_venta_cncre", vir_comprobante_retencion.getTab_cab_retencion_vretencion().getValor("es_venta_cncre"));
    }
    
    public void insertarDetalleRetencion() {
        for (int i = 0; i < vir_comprobante_retencion.getTab_det_retencion_vretencion().getTotalFilas(); i++) {
            
            tab_det_retencion.insertar();
            tab_det_retencion.setValor("ide_cndre", ide_cndre + "");
            tab_det_retencion.setValor("ide_cncre", ide_cncre + "");
            tab_det_retencion.setValor("ide_cncim", vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "ide_cncim"));
            tab_det_retencion.setValor("porcentaje_cndre", vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "porcentaje_cndre"));
            tab_det_retencion.setValor("base_cndre", vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "base_cndre"));
            tab_det_retencion.setValor("valor_cndre", vir_comprobante_retencion.getTab_det_retencion_vretencion().getValor(i, "valor_cndre"));
            ide_cndre = ide_cndre + 1;
        }
    }
//    

    public void obtener_total_detalle_factura_cxp(AjaxBehaviorEvent evt) {
        tab_det_factura_visual.modificar(evt);
        
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        
        if (tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "cantidad_cpdfa") != null && !tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "cantidad_cpdfa").isEmpty()) {
            try {
                cantidad = Double.parseDouble(tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "cantidad_cpdfa"));
            } catch (Exception e) {
                cantidad = 0;
            }
        }
        
        if (tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "precio_cpdfa") != null && !tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "precio_cpdfa").isEmpty()) {
            try {
                precio = Double.parseDouble(tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "precio_cpdfa"));
            } catch (Exception e) {
                precio = 0;
            }
        }
        
        total = cantidad * precio;
        tab_det_factura_visual.setValor(tab_det_factura_visual.getFilaActual(), "valor_cpdfa", utilitario.getFormatoNumero(total));
        
        utilitario.addUpdateTabla(tab_det_factura_visual, "valor_cpdfa", "");
        
        calcula_iva();
        //   calcula_total_detalles_cxp();
    }
    
    public void cargarProveedores() {
        if (tab_cab_factura_visual.getTotalFilas() > 0) {
            // solo ruc 
            if (tab_cab_factura_visual.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_factura"))) {
                tab_cab_factura_visual.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid=" + utilitario.getVariable("p_gen_tipo_iden_ruc"));
                tab_cab_factura_visual.setValor("ide_geper", null);
                utilitario.addUpdate("tab_cab_factura_visual");
            }
            // diferencte de ruc
            if (tab_cab_factura_visual.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
                tab_cab_factura_visual.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid!=" + utilitario.getVariable("p_gen_tipo_iden_ruc"));
                tab_cab_factura_visual.setValor("ide_geper", null);
                utilitario.addUpdate("tab_cab_factura_visual");
            }
            // solo rise
            if (tab_cab_factura_visual.getValor("ide_cntdo").equals(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
                tab_cab_factura_visual.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_cntco = " + utilitario.getVariable("p_con_tipo_contribuyente_rise"));
                tab_cab_factura_visual.setValor("ide_geper", null);
                utilitario.addUpdate("tab_cab_factura_visual");
            }
        }
    }
    
    public void cambioTipoDocumento(AjaxBehaviorEvent evt) {
        
        tab_cab_factura_visual.modificar(evt);
        
        cargarProveedores();
    }
    
    public void obtener_total_detalle_factura_cxp1(SelectEvent evt) {
        
        tab_det_factura_visual.modificar(evt);
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        if (tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "cantidad_cpdfa") != null && !tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "cantidad_cpdfa").isEmpty()) {
            try {
                cantidad = Double.parseDouble(tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "cantidad_cpdfa"));
            } catch (Exception e) {
                cantidad = 0;
            }
        }
        
        if (tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "precio_cpdfa") != null && !tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "precio_cpdfa").isEmpty()) {
            try {
                precio = Double.parseDouble(tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "precio_cpdfa"));
            } catch (Exception e) {
                precio = 0;
            }
        }
        
        total = cantidad * precio;
        tab_det_factura_visual.setValor(tab_det_factura_visual.getFilaActual(), "valor_cpdfa", utilitario.getFormatoNumero(total, 2));
        
        if (getParametroProducto("iva_inarti", tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "ide_inarti")) != null) {
            tab_det_factura_visual.setValor(tab_det_factura_visual.getFilaActual(), "iva_inarti_cpdfa", getParametroProducto("iva_inarti", tab_det_factura_visual.getValor(tab_det_factura_visual.getFilaActual(), "ide_inarti")));
        }
        
        utilitario.addUpdateTabla(tab_det_factura_visual, "valor_cpdfa,iva_inarti_cpdfa", "");
        
        calcula_iva();
        //calcula_total_detalles_cxp();

    }
    
    public String obtener_tipo_articulo(String ide_arti) {
        // devuelve el tipo de articulo ya sea activo fijo,bien, o otro
        String ide_art = ide_arti;
        List inv_ide_arti = utilitario.getConexion().consultar("select inv_ide_inarti from inv_articulo where ide_inarti=" + ide_art);
        try {
            if (inv_ide_arti.get(0) != null) {
                do {
                    ide_art = inv_ide_arti.get(0).toString();
                    inv_ide_arti = utilitario.getConexion().consultar("select inv_ide_inarti from inv_articulo where ide_inarti=" + ide_art);
                } while (inv_ide_arti.get(0) != null);
            }
            
        } catch (Exception e) {
        }
        return ide_art;
    }
    private double base_no_objeto = 0;
    private double base_tarifa0 = 0;
    private double base_grabada = 0;
    private double valor_iva = 0;
    private double iva30;
    private double iva70;
    private double iva100;
    List porcen_iva_sql = utilitario.getConexion().consultar("select porcentaje_cnpim from con_porcen_impues where ide_cnpim=" + utilitario.getVariable("p_con_porcentaje_imp_iva"));
    private double p_porcentaje_iva = Double.parseDouble(porcen_iva_sql.get(0).toString());
    private String p_bienes = utilitario.getVariable("p_inv_articulo_bien");
    private String p_servicios = utilitario.getVariable("p_inv_articulo_servicio");
    private String p_honorarios_profes = utilitario.getVariable("p_inv_articulo_honorarios");
    private String p_activos_fijos = utilitario.getVariable("p_inv_articulo_activo_fijo");
    private String p_iva30 = utilitario.getVariable("p_con_impuesto_iva30");
    private String p_iva70 = utilitario.getVariable("p_con_impuesto_iva70");
    private String p_iva100 = utilitario.getVariable("p_con_impuesto_iva100");
    
    public void calcula_iva() {
        base_grabada = 0;
        base_no_objeto = 0;
        base_tarifa0 = 0;
        valor_iva = 0;
        iva30 = 0;
        iva70 = 0;
        iva100 = 0;
        for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
            if (tab_det_factura_visual.getValor(i, "ide_inarti") != null) {
                //List sql_iva = utilitario.getConexion().consultar("select iva_inarti from inv_articulo where ide_inarti=" + tab_det_factura_visual.getValor(i, "ide_inarti"));
                String art = obtener_tipo_articulo(tab_det_factura_visual.getValor(i, "ide_inarti"));
                int iva = Integer.parseInt(tab_det_factura_visual.getValor(i, "iva_inarti_cpdfa"));
                if (iva == 1) {
                    base_grabada = Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa")) + base_grabada;
                    valor_iva = base_grabada * p_porcentaje_iva;
                    if (art.equals(p_bienes)) {
                        iva30 = iva30 + Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa"));
                    }
                    if (art.equals(p_activos_fijos)) {
                        iva30 = iva30 + Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa"));
                    }
                    
                    if (art.equals(p_servicios)) {
                        iva70 = iva70 + Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa"));
                    }
                    if (art.equals(p_honorarios_profes)) {
                        iva100 = iva100 + Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa"));
                    }
                } else {
                    if (iva == 0) {
                        base_no_objeto = Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa")) + base_no_objeto;
                    } else {
                        base_tarifa0 = Double.parseDouble(tab_det_factura_visual.getValor(i, "valor_cpdfa")) + base_tarifa0;
                    }
                }
            }
        }
        tab_cab_factura_visual.setValor("base_grabada_cpcfa", utilitario.getFormatoNumero(base_grabada, 2));
        tab_cab_factura_visual.setValor("base_no_objeto_iva_cpcfa", utilitario.getFormatoNumero(base_no_objeto, 2));
        tab_cab_factura_visual.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(valor_iva, 2));
        tab_cab_factura_visual.setValor("base_tarifa0_cpcfa", utilitario.getFormatoNumero(base_tarifa0, 2));
        utilitario.addUpdate("tab_cab_factura_visual");
        calcula_total_detalles_cxp();
    }
    
    public void calcula_total_detalles_cxp() {
        
        double total_fac = 0;
        double val_iva = 0;
        double val_base_no_iva = 0;
        double val_base_grabada = 0;
        double otros = 0;
        double val_tarifa0 = 0;
        if (tab_cab_factura_visual.getValor("valor_iva_cpcfa") != null) {
            val_iva = Double.parseDouble(tab_cab_factura_visual.getValor("valor_iva_cpcfa"));
        }
        if (tab_cab_factura_visual.getValor("base_no_objeto_iva_cpcfa") != null) {
            val_base_no_iva = Double.parseDouble(tab_cab_factura_visual.getValor("base_no_objeto_iva_cpcfa"));
        }
        if (tab_cab_factura_visual.getValor("base_grabada_cpcfa") != null) {
            val_base_grabada = Double.parseDouble(tab_cab_factura_visual.getValor("base_grabada_cpcfa"));
        }
        if (tab_cab_factura_visual.getValor("otros_cpcfa") != null) {
            otros = Double.parseDouble(tab_cab_factura_visual.getValor("otros_cpcfa"));
        }
        if (tab_cab_factura_visual.getValor("base_tarifa0_cpcfa") != null) {
            val_tarifa0 = Double.parseDouble(tab_cab_factura_visual.getValor("base_tarifa0_cpcfa"));
        }
        
        total_fac = val_iva + val_base_grabada + val_base_no_iva + val_tarifa0 + otros;
        
        if (tab_cab_factura_visual.getTotalFilas() > 0) {
            tab_cab_factura_visual.setValor("total_cpcfa", utilitario.getFormatoNumero(total_fac, 2));
        }
        
        utilitario.addUpdate("tab_cab_factura_visual");
        
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
    
    public boolean validarRegistroFactura() {
        if (tab_cab_factura_visual.getValor("ide_geper") == null || tab_cab_factura_visual.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe seleccionar un proveedor");
            return false;
        }
        if (tab_cab_factura_visual.getValor("numero_cpcfa") == null || tab_cab_factura_visual.getValor("numero_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar el Numero de Factura");
            return false;
        }
        if (tab_cab_factura_visual.getValor("autorizacio_cpcfa") == null || tab_cab_factura_visual.getValor("autorizacio_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar el Numero de Autorizacion");
            return false;
        }
        if (tab_cab_factura_visual.getValor("observacion_cpcfa") == null || tab_cab_factura_visual.getValor("observacion_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar la observacion de la Factura");
            return false;
        }
        if (Double.parseDouble(tab_cab_factura_visual.getValor("total_cpcfa")) == 0) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe ingresar Detalles a la Factura ");
            return false;
        }
        if (tab_det_factura_visual.getValor("alter_tribu_cpdfa") == null || tab_det_factura_visual.getValor("alter_tribu_cpdfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "Debe asignar un casillero tributario al producto ");
            return false;
        }
        
        List lis_numeros_fact = utilitario.getConexion().consultar("select * from cxp_cabece_factur where numero_cpcfa='" + tab_cab_factura_visual.getValor("numero_cpcfa") + "' and ide_geper=" + tab_cab_factura_visual.getValor("ide_geper") + " and autorizacio_cpcfa='" + tab_cab_factura_visual.getValor("autorizacio_cpcfa") + "'");
        if (lis_numeros_fact.size() > 0) {
            utilitario.agregarMensajeError("Error al guardar la factura, no se guardaron los datos", "El numero de factura de este proveedor ya existe ");
            return false;
        }
        
        return true;
        
    }
    
    public void generarComprobanteRetencion() {
        if (con_confirma_rete.isVisible()) {
            con_confirma_rete.cerrar();
        }
        vir_comprobante_retencion.setVistaRetencion(tab_cab_factura_visual, tab_det_factura_visual, false);
        vir_comprobante_retencion.dibujar();
        utilitario.addUpdate("vir_comprobante_retencion");
    }
    
    public void aceptar_datos_factura() {
        obtener_alterno();
        if (validarRegistroFactura()) {
            if (!tab_cab_factura_visual.getValor("ide_cntdo").equalsIgnoreCase(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
                con_confirma_rete.dibujar();
            } else {
//                boo_hizo_retencion = false;
//                aceptar_comprobante_reten();
            }
            
        }
    }
    
    public void obtener_alterno() {
        String tipo_articulo = "";
        String aplica_iva = "";
        String ide_cntco = conta.obtenerParametroPersona("ide_cntco", tab_cab_factura_visual.getValor("ide_geper"));
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
            for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
                if (tab_det_factura_visual.getValor(i, "ide_inarti") != null && !tab_det_factura_visual.getValor(i, "ide_inarti").isEmpty()) {
                    tipo_articulo = obtener_tipo_articulo(tab_det_factura_visual.getValor(i, "ide_inarti"));
                    //aplica_iva = obtener_aplica_iva_producto(tab_det_factura_visual.getValor(i, "ide_inarti"));
                    if (!tipo_articulo.equals(p_activos_fijos) && tab_det_factura_visual.getValor(i, "iva_inarti_cpdfa").equals("1") && tab_det_factura_visual.getValor(i, "credi_tribu_cpdfa").equals("true")) {
                        tab_det_factura_visual.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_pagos_12%_dertri_501"));
                    } else if (tab_det_factura_visual.getValor(i, "iva_inarti_cpdfa").equals("1") && tab_det_factura_visual.getValor(i, "credi_tribu_cpdfa").equals("true")) {
                        tab_det_factura_visual.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_local_12%_dertri_502"));
                    } else if (tab_det_factura_visual.getValor(i, "iva_inarti_cpdfa").equals("1") && tab_det_factura_visual.getValor(i, "credi_tribu_cpdfa").equals("false")) {
                        tab_det_factura_visual.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_otra_adqui_pago_12%_no_dertri_503"));
                    } else if (tab_det_factura_visual.getValor(i, "iva_inarti_cpdfa").equals("-1")) {
                        tab_det_factura_visual.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_pagos_0%_507"));
                    } else if (tab_det_factura_visual.getValor(i, "iva_inarti_cpdfa").equals("0")) {
                        tab_det_factura_visual.setValor(i, "alter_tribu_cpdfa", utilitario.getVariable("p_sri_adqui_no_obj_iva_531"));
                    } else {
                        tab_det_factura_visual.setValor(i, "alter_tribu_cpdfa", "00");
                    }
                }
                
            }
        } else {
            boo_tipo_rise = false;
            for (int i = 0; i < tab_det_factura_visual.getTotalFilas(); i++) {
                tab_det_factura_visual.setValor(i, "alter_tribu_cpdfa", "518");
            }
        }
        utilitario.addUpdate("tab_det_factura_visual");
        
    }
    cls_bancos bancos = new cls_bancos();
    cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
    
    public void generarFacturas() {
        if (tex_num_facturas.getValue() != null && !tex_num_facturas.getValue().toString().isEmpty()) {
            if (Integer.parseInt(tex_num_facturas.getValue() + "") > 0) {
                tab_cab_fact.limpiar();
                tab_det_fact.limpiar();
                lis_ide_fact_generadas.clear();
                lis_ide_bancos.clear();
                lis_ide_cuenta_bancos.clear();
                lis_ide_bancos_update.clear();
                lis_valor_ide_bancos_update.clear();
                cxp.getTab_cab_tran_cxp().limpiar();
                cxp.getTab_det_tran_cxp().limpiar();
                bancos.getTab_cab_libro_banco().limpiar();
                tab_cab_retencion.limpiar();
                tab_det_retencion.limpiar();
                tab_cab_comp_inv.limpiar();
                tab_det_comp_inv.limpiar();
                tab_cab_factura_visual.limpiar();
                tab_cab_factura_visual.insertar();
                tab_det_factura_visual.limpiar();
                conta.limpiar();
                //1294 SOCIEDAD SALESIANA EN ECUADOR
                cab_com_con = new cls_cab_comp_cont(p_tipo_comprobante_egreso, p_estado_comprobante_normal, p_modulo, "1294", utilitario.getFechaActual(), "");
                lista_detalles.clear();
                dia_facturas.dibujar();
            } else {
                utilitario.agregarMensajeInfo("No se puede generar las facturas", "el numero de facturas debe ser mayor a cero");
            }
        } else {
            utilitario.agregarMensajeInfo("No se puede generar las facturas", "No ha ingresado el numero de facturas que va a registrar");
        }
    }
    
    @Override
    public void insertar() {
        if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.getTab_det_comp_cont_vasiento().insertar();
        } else {
            utilitario.getTablaisFocus().insertar();
        }
        
    }
    
    @Override
    public void guardar() {
    }
    
    @Override
    public void eliminar() {
        if (vir_comprobante_retencion.getTab_det_retencion_vretencion().isFocus()) {
            vir_comprobante_retencion.getTab_det_retencion_vretencion().eliminar();
        }
        if (via_comprobante_conta.getTab_det_comp_cont_vasiento().isFocus()) {
            via_comprobante_conta.getTab_det_comp_cont_vasiento().eliminar();
        }
    }
    
    public Tabla getTab_cab_factura_visual() {
        return tab_cab_factura_visual;
    }
    
    public void setTab_cab_factura_visual(Tabla tab_cab_factura_visual) {
        this.tab_cab_factura_visual = tab_cab_factura_visual;
    }
    
    public Tabla getTab_det_factura_visual() {
        return tab_det_factura_visual;
    }
    
    public void setTab_det_factura_visual(Tabla tab_det_factura_visual) {
        this.tab_det_factura_visual = tab_det_factura_visual;
    }
    
    public Dialogo getDia_facturas() {
        return dia_facturas;
    }
    
    public void setDia_facturas(Dialogo dia_facturas) {
        this.dia_facturas = dia_facturas;
    }
    
    public Confirmar getCon_confirma_rete() {
        return con_confirma_rete;
    }
    
    public void setCon_confirma_rete(Confirmar con_confirma_rete) {
        this.con_confirma_rete = con_confirma_rete;
    }
    
    public VistaRetencion getVir_comprobante_retencion() {
        return vir_comprobante_retencion;
    }
    
    public void setVir_comprobante_retencion(VistaRetencion vir_comprobante_retencion) {
        this.vir_comprobante_retencion = vir_comprobante_retencion;
    }
    
    public Tabla getTab_cab_retencion() {
        return tab_cab_retencion;
    }
    
    public void setTab_cab_retencion(Tabla tab_cab_retencion) {
        this.tab_cab_retencion = tab_cab_retencion;
    }
    
    public Tabla getTab_det_retencion() {
        return tab_det_retencion;
    }
    
    public void setTab_det_retencion(Tabla tab_det_retencion) {
        this.tab_det_retencion = tab_det_retencion;
    }
    
    public Tabla getTab_factura_generadas() {
        return tab_factura_generadas;
    }
    
    public void setTab_factura_generadas(Tabla tab_factura_generadas) {
        this.tab_factura_generadas = tab_factura_generadas;
    }
    
    public Tabla getTab_cab_comp_inv() {
        return tab_cab_comp_inv;
    }
    
    public void setTab_cab_comp_inv(Tabla tab_cab_comp_inv) {
        this.tab_cab_comp_inv = tab_cab_comp_inv;
    }
    
    public Tabla getTab_det_comp_inv() {
        return tab_det_comp_inv;
    }
    
    public void setTab_det_comp_inv(Tabla tab_det_comp_inv) {
        this.tab_det_comp_inv = tab_det_comp_inv;
    }
    
    public Tabla getTab_cab_fact() {
        return tab_cab_fact;
    }
    
    public void setTab_cab_fact(Tabla tab_cab_fact) {
        this.tab_cab_fact = tab_cab_fact;
    }
    
    public Tabla getTab_det_fact() {
        return tab_det_fact;
    }
    
    public void setTab_det_fact(Tabla tab_det_fact) {
        this.tab_det_fact = tab_det_fact;
    }
    
    public Tabla getTab_cab_tran_cxp() {
        return tab_cab_tran_cxp;
    }
    
    public void setTab_cab_tran_cxp(Tabla tab_cab_tran_cxp) {
        this.tab_cab_tran_cxp = tab_cab_tran_cxp;
    }
    
    public Tabla getTab_det_tran_cxp() {
        return tab_det_tran_cxp;
    }
    
    public void setTab_det_tran_cxp(Tabla tab_det_tran_cxp) {
        this.tab_det_tran_cxp = tab_det_tran_cxp;
    }
    
    public Dialogo getDia_banco() {
        return dia_banco;
    }
    
    public void setDia_banco(Dialogo dia_banco) {
        this.dia_banco = dia_banco;
    }
    
    public VistaAsiento getVia_comprobante_conta() {
        return via_comprobante_conta;
    }
    
    public void setVia_comprobante_conta(VistaAsiento via_comprobante_conta) {
        this.via_comprobante_conta = via_comprobante_conta;
    }
    
    public VisualizarPDF getVp() {
        return vp;
    }
    
    public void setVp(VisualizarPDF vp) {
        this.vp = vp;
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
}
