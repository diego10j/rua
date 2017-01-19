/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_modifica_documento extends Pantalla {

    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);

    private double tarifaIVA = 0;
    private Map<String, String> parametros;
    private final AreaTexto ate_observacion = new AreaTexto();
    private final Texto tex_subtotal12 = new Texto();
    private final Texto tex_subtotal0 = new Texto();
    private final Texto tex_iva = new Texto();
    private final Texto tex_total = new Texto();
    private final Texto tex_valor_descuento = new Texto();
    private final Texto tex_porc_descuento = new Texto();
    private final Texto tex_otros_valores = new Texto();

    private Tabla tab_cab_documento;
    private Tabla tab_det_documento;
    private Combo com_tipo_documento;
    //REEMBOLSOS
    private Tabla tab_com_reembolso;

    private final Grid div_division = new Grid();

    private AutoCompletar aut_proveedor = new AutoCompletar();

    private SeleccionTabla sel_factura = new SeleccionTabla();

    public pre_modifica_documento() {

        parametros = utilitario.getVariables("ide_usua", "ide_empr", "ide_sucu",
                "p_cxp_estado_factura_normal",
                "p_con_for_pag_reembolso_caja",
                "p_con_for_pag_anticipo",
                "p_sri_tip_sus_tri02",
                "p_con_tipo_documento_factura",
                "p_con_tipo_documento_nota_credito",
                "p_con_tipo_documento_reembolso",
                "p_con_tipo_documento_factura",
                "p_con_tipo_documento_nota_venta",
                "p_con_tipo_documento_liquidacion_compra",
                "p_gen_tipo_iden_ruc",
                "p_con_tipo_contribuyente_rise");

        aut_proveedor.setId("aut_proveedor");
        aut_proveedor.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_proveedor.setAutocompletarContenido();
        aut_proveedor.setMetodoChange("abrirDocumentos");
        aut_proveedor.setSize(70);
        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarComponente(new Etiqueta("PROVEEDOR"));
        bar_botones.agregarComponente(aut_proveedor);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        //Recupera porcentaje iva
        tarifaIVA = ser_configuracion.getPorcentajeIva();
        div_division.setId("div_division");
        agregarComponente(div_division);

        sel_factura.setId("sel_factura");
        sel_factura.setTitle("DOCUMENTOS POR PAGAR");
        sel_factura.setHeight("50%");
        sel_factura.setWidth("65%");
        sel_factura.setRadio();
        sel_factura.setSeleccionTabla(ser_cuentas_cxp.getSqlDocumentosCliente("-1"), "ide_cpcfa");
        sel_factura.getTab_seleccion().getColumna("numero_cpcfa").setFiltroContenido();
        sel_factura.getBot_aceptar().setMetodo("aceptarDocumento");
        agregarComponente(sel_factura);
    }

    public void limpiar() {
        aut_proveedor.limpiar();
        div_division.getChildren().clear();
        utilitario.addUpdate("div_division");
    }

    public void aceptarDocumento() {
        if (sel_factura.getValorSeleccionado() != null) {
            sel_factura.cerrar();
            div_division.getChildren().clear();
            div_division.getChildren().add(dibujarDocumento(sel_factura.getValorSeleccionado()));
            utilitario.addUpdate("div_division");
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Documento por pagar", "");
        }
    }

    public void abrirDocumentos(SelectEvent evt) {
        aut_proveedor.onSelect(evt);
        if (aut_proveedor.getValor() != null) {
            sel_factura.setSql(ser_cuentas_cxp.getSqlDocumentosCliente(aut_proveedor.getValor()));
            sel_factura.getTab_seleccion().ejecutarSql();
            if (sel_factura.getTab_seleccion().isEmpty() == false) {
                sel_factura.setTitle("DOCUMENTOS POR PAGAR - " + aut_proveedor.getValorArreglo(2));
                sel_factura.dibujar();
            } else {
                utilitario.agregarMensaje("El proveedor seleccionado no tiene Documentos por pagar", "");
            }
        }
    }

    private Grid dibujarDocumento(String ide_cpcfa) {
        Grid grupo = new Grid();
        TablaGenerica tab = utilitario.consultar("Select ide_cpcfa,ide_geper,ide_cntdo from cxp_cabece_factur where ide_cpcfa=" + ide_cpcfa);
        if (tab.isEmpty()) {
            utilitario.agregarMensajeError("El documento por pagar no existe", "");
            return grupo;
        }
        com_tipo_documento = new Combo();
        com_tipo_documento.setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        com_tipo_documento.setMetodoRuta("cambiarTipoDocumento");
        com_tipo_documento.setDisabled(true);
        com_tipo_documento.setValue(tab.getValor("ide_cntdo"));

        Grid gri_pto = new Grid();
        gri_pto.setId("gri_pto");
        gri_pto.setColumns(11);
        gri_pto.getChildren().add(new Etiqueta("<strong>TIPO DE DOCUMENTO :</strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri_pto.getChildren().add(com_tipo_documento);
        grupo.getChildren().add(gri_pto);

        tab_cab_documento = new Tabla();
        tab_det_documento = new Tabla();
        tab_cab_documento.setId("tab_cab_documento");
        tab_cab_documento.agregarRelacion(tab_det_documento);
        tab_cab_documento.setMostrarNumeroRegistros(false);
        tab_cab_documento.setTabla("cxp_cabece_factur", "ide_cpcfa", 1);
        tab_cab_documento.getColumna("ide_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("ide_cntdo").setNombreVisual("TIPO DE DOCUMENTO");
        tab_cab_documento.getColumna("ide_cntdo").setOrden(21);
        tab_cab_documento.getColumna("ide_cntdo").setVisible(false);
        tab_cab_documento.getColumna("ide_cpefa").setValorDefecto(parametros.get("p_cxp_estado_factura_normal"));
        tab_cab_documento.getColumna("ide_cpefa").setVisible(false);
        tab_cab_documento.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "");
        tab_cab_documento.getColumna("ide_cndfp").setRequerida(true);
        tab_cab_documento.getColumna("TARIFA_IVA_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
        tab_cab_documento.getColumna("ide_cndfp").setOrden(0);
        tab_cab_documento.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_documento.getColumna("ide_usua").setVisible(false);
        tab_cab_documento.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_documento.getColumna("fecha_trans_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA EMISIÓN");
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setOrden(1);
        tab_cab_documento.getColumna("ide_geper").setCombo(aut_proveedor.getLista());
        tab_cab_documento.getColumna("ide_geper").setAutoCompletar();
        tab_cab_documento.getColumna("ide_geper").setRequerida(true);
        tab_cab_documento.getColumna("ide_geper").setNombreVisual("PROVEEDOR");
        tab_cab_documento.getColumna("ide_geper").setOrden(3);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setOrden(5);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setNombreVisual("NUM. AUTORIZACIÓN");
        tab_cab_documento.getColumna("autorizacio_cpcfa").setEstilo("font-weight: bold");
        tab_cab_documento.getColumna("autorizacio_cpcfa").setMetodoChange("validarAutorizacion");
        tab_cab_documento.getColumna("observacion_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("observacion_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("pagado_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("pagado_cpcfa").setValorDefecto("False");
        tab_cab_documento.getColumna("total_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("total_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("numero_cpcfa").setEstilo("font-size: 12px;font-weight: bold");
        tab_cab_documento.getColumna("numero_cpcfa").setNombreVisual("NÚMERO");
        tab_cab_documento.getColumna("numero_cpcfa").setOrden(4);
        tab_cab_documento.getColumna("numero_cpcfa").setAncho(10);
        tab_cab_documento.getColumna("numero_cpcfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
        tab_cab_documento.getColumna("numero_cpcfa").setMascara("999-999-99999999");
        tab_cab_documento.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_cab_documento.getColumna("numero_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("base_grabada_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("base_grabada_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("valor_iva_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("valor_iva_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("base_tarifa0_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("otros_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("ide_srtst").setCombo("sri_tipo_sustento_tributario", "ide_srtst", "alterno_srtst,nombre_srtst", "");
        tab_cab_documento.getColumna("ide_srtst").setValorDefecto(parametros.get("p_sri_tip_sus_tri02"));
        tab_cab_documento.getColumna("ide_srtst").setNombreVisual("SUSTENTO TRIBUTARIO");
        tab_cab_documento.getColumna("ide_srtst").setOrden(7);
        tab_cab_documento.getColumna("ide_cncre").setVisible(false);
        tab_cab_documento.getColumna("ide_cnccc").setVisible(true);
        tab_cab_documento.getColumna("ide_cnccc").setNombreVisual("NUM. ASIENTO");
        tab_cab_documento.getColumna("ide_cnccc").setOrden(20);
        tab_cab_documento.getColumna("valor_ice_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("valor_ice_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("OTROS_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("DESCUENTO_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("PORCEN_DESC_CPCFA").setVisible(false);
        tab_cab_documento.setTipoFormulario(true);
        tab_cab_documento.getGrid().setColumns(6);
        tab_cab_documento.setCondicion("ide_cpcfa=" + ide_cpcfa);

        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setOrden(8);
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setNombreVisual("TIPO DOC. MODI.");
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setOrden(9);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setNombreVisual("FECHA EMISIÓN DOC. MODI.");
        tab_cab_documento.getColumna("numero_nc_cpcfa").setOrden(10);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setNombreVisual("NÚMERO DOC. MODI.");
        tab_cab_documento.getColumna("numero_nc_cpcfa").setMascara("999-999-99999999");
        tab_cab_documento.getColumna("numero_nc_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setNombreVisual("AUTORIZACIÓN DOC. MODI.");
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setOrden(11);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setMetodoChange("validarAutorizacion");
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setOrden(12);
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setNombreVisual("MOTIVO");
        tab_cab_documento.getColumna("ide_rem_cpcfa").setVisible(false);
        //solo para que dibuje el *
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setRequerida(true);
        tab_cab_documento.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_cab_documento);
        pat_panel1.getMenuTabla().setRendered(false);
        grupo.getChildren().add(pat_panel1);

        tab_det_documento.setId("tab_det_documento");
        tab_det_documento.setTabla("cxp_detall_factur", "ide_cpdfa", 2);
        tab_det_documento.setCampoForanea("ide_cpdfa");
        tab_det_documento.setCondicionForanea("ide_cpcfa=" + ide_cpcfa);
        tab_det_documento.getColumna("ide_cpdfa").setVisible(false);
        tab_det_documento.getColumna("ide_cpcfa").setVisible(false);
        tab_det_documento.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_det_documento.getColumna("ide_inarti").setAutoCompletar();
        tab_det_documento.getColumna("ide_inarti").setNombreVisual("ARTICULO");
        tab_det_documento.getColumna("ide_inarti").setOrden(1);
        tab_det_documento.getColumna("cantidad_cpdfa").setOrden(2);
        tab_det_documento.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
        tab_det_documento.getColumna("cantidad_cpdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_det_documento.getColumna("precio_cpdfa").setOrden(3);
        tab_det_documento.getColumna("precio_cpdfa").setNombreVisual("PRECIO");
        tab_det_documento.getColumna("precio_cpdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_det_documento.getColumna("valor_cpdfa").setEtiqueta();
        tab_det_documento.getColumna("valor_cpdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_det_documento.getColumna("valor_cpdfa").setOrden(5);
        tab_det_documento.getColumna("valor_cpdfa").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_det_documento.getColumna("valor_cpdfa").setNombreVisual("TOTAL");
        tab_det_documento.getColumna("observacion_cpdfa").setNombreVisual("OBSERVACIÓN");
        tab_det_documento.getColumna("observacion_cpdfa").setOrden(6);
        tab_det_documento.getColumna("secuencial_cpdfa").setNombreVisual("SERIE / SECUENCIAL");
        tab_det_documento.getColumna("secuencial_cpdfa").setOrden(7);
        tab_det_documento.getColumna("devolucion_cpdfa").setValorDefecto("false");
        tab_det_documento.getColumna("alter_tribu_cpdfa").setRequerida(true);
        tab_det_documento.getColumna("alter_tribu_cpdfa").setValorDefecto("00");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setCombo(ser_producto.getListaTipoIVA());
        tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setPermitirNullCombo(false);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setValorDefecto("1");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setOrden(4);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setLongitud(-1);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setNombreVisual("APLICA IVA");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_det_documento.getColumna("credi_tribu_cpdfa").setVisible(false);
        tab_det_documento.getColumna("devolucion_cpdfa").setVisible(false);
        tab_det_documento.setScrollable(true);
        tab_det_documento.setScrollHeight(utilitario.getAltoPantalla() - 335);
        tab_det_documento.setRows(100);
        tab_det_documento.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_det_documento);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setValueExpression("rendered", "true");
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);
        pat_panel.setStyle("width:100%;height:100%;overflow: hidden;display: block;");
        grupo.getChildren().add(pat_panel);

        tab_com_reembolso = new Tabla();
        tab_com_reembolso.setRendered(false);
        tab_com_reembolso.setHeader("DATOS COMPROBANTE DE REEMBOLSO");
        tab_com_reembolso.setId("tab_com_reembolso");
        tab_com_reembolso.setTabla("cxp_cabece_factur", "ide_cpcfa", 3);
        //oculta todos las columnas
        for (int i = 0; i < tab_com_reembolso.getTotalColumnas(); i++) {
            tab_com_reembolso.getColumnas()[i].setVisible(false);
        }
        tab_com_reembolso.getColumna("ide_cntdo").setVisible(true);
        tab_com_reembolso.getColumna("ide_cntdo").setNombreVisual("TIPO DOCUMENTO");
        tab_com_reembolso.getColumna("ide_cntdo").setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        tab_com_reembolso.getColumna("ide_cntdo").setRequerida(true);
        tab_com_reembolso.getColumna("ide_cntdo").setOrden(0);

        tab_com_reembolso.getColumna("motivo_nc_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("motivo_nc_cpcfa").setOrden(1);
        tab_com_reembolso.getColumna("motivo_nc_cpcfa").setLongitud(13);
        tab_com_reembolso.getColumna("motivo_nc_cpcfa").setNombreVisual("IDENTIFICACIÓN");
        tab_com_reembolso.getColumna("motivo_nc_cpcfa").setRequerida(true);

        tab_com_reembolso.getColumna("ide_cntdo").setLongitud(-1);
        tab_com_reembolso.getColumna("numero_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("numero_cpcfa").setNombreVisual("NÚMERO");
        tab_com_reembolso.getColumna("numero_cpcfa").setOrden(2);
        tab_com_reembolso.getColumna("numero_cpcfa").setComentario("Debe ingresar el número de serie - establecimiento y número secuencial");
        tab_com_reembolso.getColumna("numero_cpcfa").setMascara("999-999-99999999");
        tab_com_reembolso.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_com_reembolso.getColumna("numero_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("fecha_emisi_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_com_reembolso.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA EMISIÓN");
        tab_com_reembolso.getColumna("fecha_emisi_cpcfa").setOrden(3);
        tab_com_reembolso.getColumna("fecha_emisi_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("autorizacio_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("autorizacio_cpcfa").setNombreVisual("AUTORIZACIÓN");
        tab_com_reembolso.getColumna("autorizacio_cpcfa").setOrden(4);
        tab_com_reembolso.getColumna("autorizacio_cpcfa").setMetodoChange("validarAutorizacion");
        tab_com_reembolso.getColumna("autorizacio_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("base_no_objeto_iva_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
        tab_com_reembolso.getColumna("base_no_objeto_iva_cpcfa").setOrden(5);
        tab_com_reembolso.getColumna("base_no_objeto_iva_cpcfa").setNombreVisual("BASE NO OBJ. IVA");
        tab_com_reembolso.getColumna("base_no_objeto_iva_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("base_tarifa0_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
        tab_com_reembolso.getColumna("base_tarifa0_cpcfa").setOrden(6);
        tab_com_reembolso.getColumna("base_tarifa0_cpcfa").setNombreVisual("BASE 0%");
        tab_com_reembolso.getColumna("base_tarifa0_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("base_grabada_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("base_grabada_cpcfa").setValorDefecto("0");
        tab_com_reembolso.getColumna("base_grabada_cpcfa").setOrden(7);
        tab_com_reembolso.getColumna("base_grabada_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("base_grabada_cpcfa").setNombreVisual("BASE IVA");
        tab_com_reembolso.getColumna("base_grabada_cpcfa").setMetodoChange("calculaIvaReembolso");
        tab_com_reembolso.getColumna("valor_iva_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("valor_iva_cpcfa").setOrden(8);
        tab_com_reembolso.getColumna("valor_iva_cpcfa").setEtiqueta();
        tab_com_reembolso.getColumna("valor_iva_cpcfa").setValorDefecto("0");
        tab_com_reembolso.getColumna("valor_iva_cpcfa").setNombreVisual("IVA");
        tab_com_reembolso.getColumna("valor_iva_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("valor_ice_cpcfa").setVisible(true);
        tab_com_reembolso.getColumna("valor_ice_cpcfa").setOrden(9);
        tab_com_reembolso.getColumna("valor_ice_cpcfa").setValorDefecto("0");
        tab_com_reembolso.getColumna("valor_ice_cpcfa").setNombreVisual("ICE");
        tab_com_reembolso.getColumna("valor_ice_cpcfa").setRequerida(true);
        tab_com_reembolso.getColumna("ide_cpefa").setValorDefecto(parametros.get("p_cxp_estado_factura_normal"));
        tab_com_reembolso.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_com_reembolso.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_com_reembolso.setScrollable(true);
        tab_com_reembolso.setScrollHeight(55);
        tab_com_reembolso.setRecuperarLectura(true);
        tab_com_reembolso.setCondicion("ide_cpcfa=-1");
        tab_com_reembolso.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_com_reembolso);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel2.getMenuTabla().getItem_eliminar().setValueExpression("rendered", "true");
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);
        // pat_panel2.setStyle("width:100%;height:100%;overflow: hidden;display: block;");
        grupo.getChildren().add(pat_panel2);

        Grid gri_total = new Grid();
        gri_total.setWidth("100%");
        gri_total.setColumns(2);
        Grid gri_observa = new Grid();
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong> <span style='color:red;font-weight: bold;'> *</span>"));
        ate_observacion.setCols(70);
        gri_observa.getChildren().add(ate_observacion);
        gri_total.getChildren().add(gri_observa);
        Grid gri_valores = new Grid();
        gri_valores.setId("gri_valores");
        gri_valores.setColumns(6);

        gri_valores.getChildren().add(new Etiqueta("<strong> OTROS VALORES :<s/trong>"));
        tex_otros_valores.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_otros_valores.setMetodoChange("calcularTotalDocumento");
        tex_otros_valores.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_otros_valores);
        gri_valores.getChildren().add(new Etiqueta("<strong> % DESCUENTO :<s/trong>"));
        tex_porc_descuento.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_porc_descuento.setDisabled(true);
        tex_porc_descuento.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_porc_descuento);

        gri_valores.getChildren().add(new Etiqueta("<strong> VALOR DESCUENTO :<s/trong>"));
        tex_valor_descuento.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_valor_descuento.setValue(utilitario.getFormatoNumero("0"));
        tex_valor_descuento.setMetodoChange("calcularTotalDocumento");
        gri_valores.getChildren().add(tex_valor_descuento);

        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTOTAL TARIFA " + (utilitario.getFormatoNumero(tarifaIVA * 100)) + "% :<s/trong>"));
        tex_subtotal12.setDisabled(true);
        tex_subtotal12.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_subtotal12.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_subtotal12);
        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTOTAL TARIFA 0% :<s/trong>"));
        tex_subtotal0.setDisabled(true);
        tex_subtotal0.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_subtotal0.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_subtotal0);
        gri_valores.getChildren().add(new Etiqueta("<strong>IVA " + (utilitario.getFormatoNumero(tarifaIVA * 100)) + "% :<s/trong>"));
        tex_iva.setDisabled(true);
        tex_iva.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_iva.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_iva);
        gri_valores.getChildren().add(new Etiqueta("<strong>TOTAL :<s/trong>"));
        tex_total.setDisabled(true);
        tex_total.setStyle("font-size: 14px;font-weight: bold;text-align: right;width:110px");
        tex_total.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_total);
        gri_total.getChildren().add(gri_valores);
        grupo.getChildren().add(gri_total);
        grupo.setStyle("overflow:hidden;display:block;");
        cambiarTipoDocumento(); //******        
        ate_observacion.setValue(tab_cab_documento.getValor("observacion_cpcfa"));
        calcularTotalDetalleDocumento();
        return grupo;
    }

    /**
     * Se ejecuta cuando cambia el Precio o la Cantidad de un detalle del
     * documento
     *
     * @param evt
     */
    public void cambioPrecioCantidadIva(AjaxBehaviorEvent evt) {
        tab_det_documento.modificar(evt);
        calcularTotalDetalleDocumento();
    }

    public void calculaIvaReembolso(AjaxBehaviorEvent evt) {
        tab_com_reembolso.modificar(evt);
        double base = 0;
        try {
            base = Double.parseDouble(tab_com_reembolso.getValor("base_grabada_cpcfa"));
        } catch (Exception e) {
        }
        double iva = base * tarifaIVA;
        tab_com_reembolso.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(iva));
        utilitario.addUpdateTabla(tab_com_reembolso, "valor_iva_cpcfa", "");
    }

    /**
     * Calcula el valor total de cada detalle del documento
     */
    private void calcularTotalDetalleDocumento() {
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        try {
            cantidad = Double.parseDouble(tab_det_documento.getValor("cantidad_cpdfa"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_det_documento.getValor("precio_cpdfa"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_det_documento.setValor("valor_cpdfa", utilitario.getFormatoNumero(total));
        utilitario.addUpdateTabla(tab_det_documento, "valor_cpdfa", "");
        calcularTotalDocumento();
    }

    /**
     * Calcula totales de la factura
     */
    public void calcularTotalDocumento() {
        double base_grabada = 0;
        double base_no_objeto = 0;
        double base_tarifa0 = 0;
        double valor_iva = 0;
        // double porcentaje_iva = 0;

        for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
            String iva = tab_det_documento.getValor(i, "iva_inarti_cpdfa");
            switch (iva) {
                case "1":
                    //SI IVA
                    base_grabada = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_grabada;
                    break;
                case "-1":
                    // NO IVA
                    base_tarifa0 = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_tarifa0;
                    break;
                case "0":
                    // NO OBJETO
                    base_no_objeto = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_no_objeto;
                    break;
                default:
                    break;
            }
        }

        double porce_descuento = 0;

        if (tex_porc_descuento.getValue() != null) {
            try {
                porce_descuento = Double.parseDouble(utilitario.getFormatoNumero(tex_porc_descuento.getValue()));
            } catch (Exception e) {
            }

        }

        double descuento = 0;
        double otros = 0;

        if (tex_valor_descuento.getValue() != null) {
            try {
                descuento = Double.parseDouble(utilitario.getFormatoNumero(tex_valor_descuento.getValue()));
            } catch (Exception e) {
            }
        }
        if (tex_otros_valores.getValue() != null) {
            try {
                otros = Double.parseDouble(utilitario.getFormatoNumero(tex_otros_valores.getValue()));
            } catch (Exception e) {
            }
        }
        //base_grabada = base_grabada - descuento;
        valor_iva = (base_grabada - descuento) * tarifaIVA; //0.12

        tab_cab_documento.setValor("porcen_desc_cpcfa", utilitario.getFormatoNumero(porce_descuento));
        tab_cab_documento.setValor("descuento_cpcfa", utilitario.getFormatoNumero(descuento));
        tab_cab_documento.setValor("otros_cpcfa", utilitario.getFormatoNumero(otros));

        tab_cab_documento.setValor("base_grabada_cpcfa", utilitario.getFormatoNumero(base_grabada));
        tab_cab_documento.setValor("base_no_objeto_iva_cpcfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_cab_documento.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(valor_iva));
        tab_cab_documento.setValor("base_tarifa0_cpcfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_cab_documento.setValor("total_cpcfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva + otros));

        tex_subtotal12.setValue(utilitario.getFormatoNumero(base_grabada));
        tex_subtotal0.setValue(utilitario.getFormatoNumero(base_no_objeto + base_tarifa0));
        tex_iva.setValue(utilitario.getFormatoNumero(valor_iva));
        tex_total.setValue(utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva + otros));
        utilitario.addUpdate("gri_valores");
    }

    /**
     * Valida Factura CxP para poder guardar
     *
     * @return
     */
    public boolean validarDocumento() {

        if (tab_cab_documento.getValor("ide_cntdo") == null || tab_cab_documento.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "Debe seleccionar el Tipo de Documento");
            return false;
        }

        if (tab_cab_documento.getValor("ide_geper") == null || tab_cab_documento.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "Debe seleccionar un proveedor");
            return false;
        }
        if (tab_cab_documento.getValor("numero_cpcfa") == null || tab_cab_documento.getValor("numero_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Número del Documento");
            return false;
        }
        if (tab_cab_documento.getValor("observacion_cpcfa") == null || tab_cab_documento.getValor("observacion_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar la observacion del Documento");
            return false;
        }
        if (tab_cab_documento.getValor("autorizacio_cpcfa") == null || tab_cab_documento.getValor("autorizacio_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Número de Autorizacion del Documento");
            return false;
        } else {
            String autorizacion = tab_cab_documento.getValor("autorizacio_cpcfa");
            boolean correcto = false;
            if (autorizacion.length() == 37 || autorizacion.length() == 49 || autorizacion.length() == 10) {
                correcto = true;
            }
            if (correcto == false) {
                utilitario.agregarMensajeInfo("La longitud del Número de Autorización no es válido", autorizacion);
                return false;
            }
        }
        if (tab_det_documento.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("No se puede guardar el Documento", "Debe ingresar detalles al Documento");
            return false;
        } else {
            for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
                if (tab_det_documento.getValor(i, "cantidad_cpdfa") == null || tab_det_documento.getValor("cantidad_cpdfa").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Documento", "Debe ingresar la cantidad en los Detalles del Documento ");
                    return false;
                }
                if (tab_det_documento.getValor(i, "precio_cpdfa") == null || tab_det_documento.getValor("precio_cpdfa").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Documento", "Debe ingresar el precio en los Detalles del Documento ");
                    return false;
                }
                if (tab_det_documento.getValor(i, "alter_tribu_cpdfa") == null || tab_det_documento.getValor("alter_tribu_cpdfa").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Documento", "Debe ingresar un alterno en los Detalles del Documento ");
                    return false;
                }
            }
        }
        double dou_total = 0;
        try {
            dou_total = Double.parseDouble(tab_cab_documento.getValor("total_cpcfa"));
        } catch (Exception e) {
        }
        if (dou_total <= 0) {
            utilitario.agregarMensajeError("No se puede guardar el Documento", "El total del Documento debe ser mayor a 0");
            return false;
        }

        //Validaciones Nota de Crédito
        if (tab_cab_documento.getColumna("autorizacio_nc_cpcfa").isVisible()) {
            String autorizacion = tab_cab_documento.getValor("autorizacio_nc_cpcfa");
            if (autorizacion != null) {
                boolean correcto = false;
                if (autorizacion.length() == 37 || autorizacion.length() == 49 || autorizacion.length() == 10) {
                    correcto = true;
                }
                if (correcto == false) {
                    utilitario.agregarMensajeInfo("Error al guardar el Documento", "La longitud del Número de Autorización del Documento Modificado no es válido " + autorizacion);
                    return false;
                }
            } else {
                utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Número de Autorizacion del Documento Modificado");
                return false;
            }
        }
        if (tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").isVisible()) {
            if (tab_cab_documento.getValor("ide_cntdo_nc_cpcfa") == null || tab_cab_documento.getValor("ide_cntdo_nc_cpcfa").isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar el Documento", "Debe seleccionar el Tipo de Documento Modificado");
                return false;
            }
        }
        if (tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").isVisible()) {
            if (tab_cab_documento.getValor("fecha_emision_nc_cpcfa") == null || tab_cab_documento.getValor("fecha_emision_nc_cpcfa").isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar la Fecha de emisión del Documento Modificado");
                return false;
            }
        }
        if (tab_cab_documento.getColumna("numero_nc_cpcfa").isVisible()) {
            if (tab_cab_documento.getValor("numero_nc_cpcfa") == null || tab_cab_documento.getValor("numero_nc_cpcfa").isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Número del Documento Modificado");
                return false;
            }
        }
        if (tab_cab_documento.getColumna("motivo_nc_cpcfa").isVisible()) {
            if (tab_cab_documento.getValor("motivo_nc_cpcfa") == null || tab_cab_documento.getValor("motivo_nc_cpcfa").isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Motivo de la Nota de Crédito");
                return false;
            }
        }
        //Fin Validaciones Nota de Crédito

        //Validacion Comprobantes de Remmbolso
        if (tab_com_reembolso.isRendered()) {
            for (int i = 0; i < tab_com_reembolso.getTotalFilas(); i++) {
                //autorizacio_cpcfa
                String autorizacion = tab_com_reembolso.getValor(i, "autorizacio_cpcfa");
                if (autorizacion != null) {
                    boolean correcto = false;
                    if (autorizacion.length() == 37 || autorizacion.length() == 49 || autorizacion.length() == 10) {
                        correcto = true;
                    }
                    if (correcto == false) {
                        utilitario.agregarMensajeInfo("La longitud del Número de Autorización del Comprobante de Reembolso no es válido", autorizacion);
                        return false;
                    }
                } else {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Número de Autorizacion del Comprobante de Reembolso");
                    return false;
                }

                if (tab_com_reembolso.getValor(i, "ide_cntdo") == null || tab_com_reembolso.getValor(i, "ide_cntdo").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe seleccionar el Tipo de Documento del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "numero_cpcfa") == null || tab_com_reembolso.getValor(i, "numero_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Número del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "fecha_emisi_cpcfa") == null || tab_com_reembolso.getValor(i, "fecha_emisi_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar la fecha de emisión del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "autorizacio_cpcfa") == null || tab_com_reembolso.getValor(i, "autorizacio_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar la Autorización del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "base_no_objeto_iva_cpcfa") == null || tab_com_reembolso.getValor(i, "base_no_objeto_iva_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el valor de la Base no Objeto  IVA del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "base_tarifa0_cpcfa") == null || tab_com_reembolso.getValor(i, "base_tarifa0_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el valor de la Base 0% Número del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "base_grabada_cpcfa") == null || tab_com_reembolso.getValor(i, "base_grabada_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el valor de la Base IVA del Número del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "valor_iva_cpcfa") == null || tab_com_reembolso.getValor(i, "valor_iva_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el IVA del Número del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "valor_ice_cpcfa") == null || tab_com_reembolso.getValor(i, "valor_ice_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el ICE del Número del Comprobante de Reembolso");
                    return false;
                }
                if (tab_com_reembolso.getValor(i, "motivo_nc_cpcfa") == null || tab_com_reembolso.getValor(i, "motivo_nc_cpcfa").isEmpty()) {
                    utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar la Identificación del Provvedor del Comprobante de Reembolso");
                    return false;
                }
            }
            //valida total factura con total reembolso
            for (int i = 0; i < tab_com_reembolso.getTotalFilas(); i++) {
                //asigna total
                double dou_baseiva = 0;
                double dou_base0 = 0;
                double dou_basenoobjeto = 0;
                double dou_iva = 0;
                try {
                    dou_baseiva = Double.parseDouble(tab_com_reembolso.getValor(i, "base_grabada_cpcfa"));
                } catch (Exception e) {
                }
                try {
                    dou_base0 = Double.parseDouble(tab_com_reembolso.getValor(i, "base_tarifa0_cpcfa"));
                } catch (Exception e) {
                }
                try {
                    dou_basenoobjeto = Double.parseDouble(tab_com_reembolso.getValor(i, "base_no_objeto_iva_cpcfa"));
                } catch (Exception e) {
                }
                try {
                    dou_iva = Double.parseDouble(tab_com_reembolso.getValor(i, "valor_iva_cpcfa"));
                } catch (Exception e) {
                }
                tab_com_reembolso.setValor(i, "total_cpcfa", utilitario.getFormatoNumero(dou_baseiva + dou_base0 + dou_basenoobjeto + dou_iva));
            }
            //valida total factura con total reembolso
            if (tab_cab_documento.getSumaColumna("total_cpcfa") != tab_com_reembolso.getSumaColumna("total_cpcfa")) {
                utilitario.agregarMensajeError("Error al guardar el Documento", "El total del Documento debe ser igual al total del Comprobante de Reembolso");
                return false;
            }

        }
        //Fin validaciones reembolsos
        return true;
    }

    public void cambiarTipoDocumento(AjaxBehaviorEvent evt) {
        tab_cab_documento.seleccionarFila(evt);
        com_tipo_documento.setValue(tab_cab_documento.getValor("ide_cntdo"));
        cambiarTipoDocumento();
    }

    /**
     * Cambia tipo de documento
     */
    public void cambiarTipoDocumento() {
        boolean bol_esnota = false;
        if (com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_nota_credito"))) {
            bol_esnota = true;
            tab_det_documento.setScrollHeight(utilitario.getAltoPantalla() - 385);
        } else {
            tab_det_documento.setScrollHeight(utilitario.getAltoPantalla() - 335);
        }

        for (int i = 0; i < tab_cab_documento.getGrid().getChildren().size(); i++) {
            if (tab_cab_documento.getGrid().getChildren().get(i).getId() != null) {
                if (tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("IDE_CNTDO_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("FECHA_EMISION_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("NUMERO_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("AUTORIZACIO_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("MOTIVO_NC_CPCFA")) {
                    tab_cab_documento.getGrid().getChildren().get(i).setRendered(bol_esnota);
                    tab_cab_documento.getGrid().getChildren().get(i - 1).setRendered(bol_esnota);
                }
            }
        }
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setVisible(bol_esnota);
        tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setRequerida(bol_esnota);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setVisible(bol_esnota);
        tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setRequerida(bol_esnota);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setVisible(bol_esnota);
        tab_cab_documento.getColumna("numero_nc_cpcfa").setRequerida(bol_esnota);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setVisible(bol_esnota);
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setRequerida(bol_esnota);
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setVisible(bol_esnota);
        tab_cab_documento.getColumna("motivo_nc_cpcfa").setRequerida(bol_esnota);

        if (com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_reembolso"))) {
            //Activa tabla  y disminuye tamaño del detalle
            tab_com_reembolso.setRendered(true);
            tab_com_reembolso.limpiar();
            //   tab_det_documento.setScrollHeight(getAltoPanel() - 445);
            try {
                PanelTabla pat_panel = (PanelTabla) tab_com_reembolso.getParent();
                pat_panel.getMenuTabla().getItem_insertar().setDisabled(false);
                pat_panel.getMenuTabla().getItem_eliminar().setDisabled(false);
            } catch (Exception e) {
            }
        } else {
            tab_com_reembolso.setRendered(false);
        }
        utilitario.addUpdate("tab_com_reembolso,tab_cab_documento,tab_det_documento,panel_tab_com_reembolso");
    }

    /**
     * Valida numero de autorizacion
     */
    public void validarAutorizacion() {
        String autorizacion = tab_cab_documento.getValor("autorizacio_cpcfa");
        if (autorizacion != null) {
            boolean correcto = false;
            if (autorizacion.length() == 37 || autorizacion.length() == 49 || autorizacion.length() == 10) {
                correcto = true;
            }
            if (correcto == false) {
                utilitario.agregarMensajeInfo("La longitud del Número de Autorización no es válido", autorizacion);
            }
        }
        if (tab_cab_documento.getColumna("autorizacio_nc_cpcfa").isVisible()) {
            autorizacion = tab_cab_documento.getValor("autorizacio_nc_cpcfa");
            if (autorizacion != null) {
                boolean correcto = false;
                if (autorizacion.length() == 37 || autorizacion.length() == 49 || autorizacion.length() == 10) {
                    correcto = true;
                }
                if (correcto == false) {
                    utilitario.agregarMensajeInfo("La longitud del Número de Autorización del Documento Modificado no es válido", autorizacion);
                }
            }
        } else if (tab_com_reembolso.isRendered()) {
            //autorizacio_cpcfa
            autorizacion = tab_com_reembolso.getValor("autorizacio_cpcfa");
            if (autorizacion != null) {
                boolean correcto = false;
                if (autorizacion.length() == 37 || autorizacion.length() == 49 || autorizacion.length() == 10) {
                    correcto = true;
                }
                if (correcto == false) {
                    utilitario.agregarMensajeInfo("La longitud del Número de Autorización del Comprobante de Reembolso no es válido", autorizacion);
                }
            }
        }
    }

    @Override
    public void insertar() {
        if (tab_det_documento.isFocus()) {
            if (tab_cab_documento.getValor("ide_geper") != null) {
                tab_det_documento.insertar();
            } else {
                utilitario.agregarMensajeInfo("Seleccione Proveedor", "Debe seleccionar un proveedor para realizar el documento");
            }
        }
        if (tab_com_reembolso.isFocus()) {
            if (tab_cab_documento.getValor("ide_geper") != null) {
                tab_com_reembolso.insertar();
            } else {
                utilitario.agregarMensajeInfo("Seleccione Proveedor", "Debe seleccionar un proveedor para realizar el documento");
            }
        }
    }

    @Override
    public void guardar() {
        tab_cab_documento.setValor("ide_cntdo", String.valueOf(com_tipo_documento.getValue()));
        tab_cab_documento.setValor("observacion_cpcfa", String.valueOf(ate_observacion.getValue()));

        tab_cab_documento.setValor("descuento_cpcfa", utilitario.getFormatoNumero(tex_valor_descuento.getValue()));
        tab_cab_documento.setValor("otros_cpcfa", utilitario.getFormatoNumero(tex_otros_valores.getValue()));
        tab_cab_documento.setValor("tarifa_iva_cpcfa", utilitario.getFormatoNumero(tarifaIVA));
        tab_cab_documento.modificar(tab_cab_documento.getFilaActual());
        if (validarDocumento()) {

            if (tab_cab_documento.guardar()) {
                String ide_cccfa = tab_cab_documento.getValor("ide_cpcfa");
                for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
                    tab_det_documento.setValor(i, "ide_cpcfa", ide_cccfa);
                }
                if (tab_det_documento.guardar()) {
                    if (tab_com_reembolso.isRendered()) {
                        for (int i = 0; i < tab_com_reembolso.getTotalFilas(); i++) {
                            tab_com_reembolso.setValor(i, "ide_rem_cpcfa", ide_cccfa);
                        }
                    }
                    if (tab_com_reembolso.guardar()) {
                        //Guarda la cuenta por pagar  
                        ser_cuentas_cxp.generarModificaTransaccionCompra(tab_cab_documento);

                        //Transaccion de Inventario
                        //****FALTA MODIFICAR
                        //  ser_inventario.generarComprobanteTransaccionCompra(tab_cab_documento, tab_det_documento);
                        guardarPantalla();
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_det_documento.isFocus()) {
            tab_det_documento.eliminar();
            calcularTotalDocumento();
        } else if (tab_com_reembolso.isFocus()) {
            tab_com_reembolso.eliminar();
        }
    }

    public Tabla getTab_cab_documento() {
        return tab_cab_documento;
    }

    public void setTab_cab_documento(Tabla tab_cab_documento) {
        this.tab_cab_documento = tab_cab_documento;
    }

    public Tabla getTab_det_documento() {
        return tab_det_documento;
    }

    public void setTab_det_documento(Tabla tab_det_documento) {
        this.tab_det_documento = tab_det_documento;
    }

    public Tabla getTab_com_reembolso() {
        return tab_com_reembolso;
    }

    public void setTab_com_reembolso(Tabla tab_com_reembolso) {
        this.tab_com_reembolso = tab_com_reembolso;
    }

    public AutoCompletar getAut_proveedor() {
        return aut_proveedor;
    }

    public void setAut_proveedor(AutoCompletar aut_proveedor) {
        this.aut_proveedor = aut_proveedor;
    }

    public SeleccionTabla getSel_factura() {
        return sel_factura;
    }

    public void setSel_factura(SeleccionTabla sel_factura) {
        this.sel_factura = sel_factura;
    }

}
