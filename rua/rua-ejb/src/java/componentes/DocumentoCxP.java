/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class DocumentoCxP extends Dialogo {

    private final Utilitario utilitario = new Utilitario();
    private final Tabulador tab_documenoCxP = new Tabulador();
    private Combo com_tipo_documento;
    private Tabla tab_cab_documento;
    private Tabla tab_det_documento;
    private Tabla tab_datos_documento;
    private Map<String, String> parametros;
    private final AreaTexto ate_observacion = new AreaTexto();
    private final Texto tex_subtotal12 = new Texto();
    private final Texto tex_subtotal0 = new Texto();
    private final Texto tex_iva = new Texto();
    private final Texto tex_total = new Texto();
    private final Texto tex_valor_descuento = new Texto();
    private final Texto tex_porc_descuento = new Texto();
    private final Texto tex_otros_valores = new Texto();

    private int tabActiva = 0;
    private int opcion = 0;
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);

    public DocumentoCxP() {
        //Recupera todos los parametros que se van a utilizar
        parametros = utilitario.getVariables("ide_usua", "ide_empr", "ide_sucu",
                "p_cxp_estado_factura_normal");

        this.setWidth("95%");
        this.setHeight("90%");
        this.setTitle("GENERAR DOCUMENTO POR PAGAR");
        this.setResizable(false);
        this.setDynamic(false);
        tab_documenoCxP.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + (getAltoPanel() - 10) + "px;overflow: auto;display: block;");
        tab_documenoCxP.setId("tab_documenoCxP");
        tab_documenoCxP.setWidgetVar("w_documenoCxP");
        tab_documenoCxP.agregarTab("DOCUMENTO POR PAGAR ", null);//0        
        this.setDialogo(tab_documenoCxP);
    }

    public void setDocumentoCxP(String titulo) {
        this.setTitle(titulo);
        this.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardar");
    }

    public void nuevoDocumento() {
        opcion = 1;  // GENERA FACTURA
        tab_documenoCxP.getTab(0).getChildren().clear();
        tab_documenoCxP.getTab(0).getChildren().add(dibujarDocumento());
        utilitario.getConexion().getSqlPantalla().clear();//LIMPIA SQL EXISTENTES
        //Activa click derecho insertar y eliminar
        try {
            PanelTabla pat_panel = (PanelTabla) tab_det_documento.getParent();
            pat_panel.getMenuTabla().getItem_insertar().setDisabled(false);
            pat_panel.getMenuTabla().getItem_eliminar().setDisabled(false);
        } catch (Exception e) {
        }
    }

    private Grupo dibujarDocumento() {
        Grupo grupo = new Grupo();

        com_tipo_documento = new Combo();
        //com_tipo_documento.setCombo("select ide_cntdo,nombre_cntdo from con_tipo_document where ide_cntdo in (" + utilitario.getVariable("p_con_tipo_documento_reembolso") + "," + utilitario.getVariable("p_con_tipo_documento_factura") + "," + utilitario.getVariable("p_con_tipo_documento_liquidacion_compra") + "," + utilitario.getVariable("p_con_tipo_documento_nota_venta") + ")");
        com_tipo_documento.setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        com_tipo_documento.setMetodoRuta("pre_index.clase." + getId() + ".cambiarTipoDocumento");
        //com_tipo_documento.eliminarVacio();

        Grid gri_pto = new Grid();
        gri_pto.setColumns(4);
        gri_pto.getChildren().add(new Etiqueta("<strong>TIPO DE DOCUMENTO :</strong>"));
        gri_pto.getChildren().add(com_tipo_documento);
        grupo.getChildren().add(gri_pto);

        tab_cab_documento = new Tabla();
        tab_det_documento = new Tabla();
        tab_datos_documento = new Tabla();
        tab_cab_documento.setRuta("pre_index.clase." + getId());
        tab_cab_documento.setId("tab_cab_documento");
        tab_cab_documento.setIdCompleto("tab_documenoCxP:tab_cab_factura");
        tab_cab_documento.setMostrarNumeroRegistros(false);
        tab_cab_documento.setTabla("cxp_cabece_factur", "ide_cpcfa", 999);
        tab_cab_documento.getColumna("ide_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("ide_cntdo").setVisible(false);
        tab_cab_documento.getColumna("ide_cpefa").setValorDefecto(parametros.get("p_cxp_estado_factura_normal"));
        tab_cab_documento.getColumna("ide_cpefa").setVisible(false);
        tab_cab_documento.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp not in ( " + utilitario.getVariable("p_con_for_pag_reembolso_caja") + "," + utilitario.getVariable("p_con_for_pag_anticipo") + " )");
        tab_cab_documento.getColumna("ide_cndfp").setPermitirNullCombo(false);
        tab_cab_documento.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
        tab_cab_documento.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
        tab_cab_documento.getColumna("ide_cndfp").setOrden(0);
        tab_cab_documento.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_documento.getColumna("ide_usua").setVisible(false);
        tab_cab_documento.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_documento.getColumna("fecha_trans_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA EMISION");
        tab_cab_documento.getColumna("fecha_emisi_cpcfa").setOrden(1);
        tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "ide_geper=-1"); //por defecto no carga los clientes
        tab_cab_documento.getColumna("ide_geper").setAutoCompletar();
        tab_cab_documento.getColumna("ide_geper").setRequerida(true);
        tab_cab_documento.getColumna("ide_geper").setNombreVisual("PROVEEDOR");
        tab_cab_documento.getColumna("ide_geper").setOrden(3);
        tab_cab_documento.getColumna("ide_geper").setMetodoChangeRuta(tab_cab_documento.getRuta() + ".seleccionarProveedor");
        tab_cab_documento.getColumna("autorizacio_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setOrden(5);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setNombreVisual("NUM. AUTORIZACION");
        tab_cab_documento.getColumna("autorizacio_cpcfa").setEstilo("font-weight: bold");
        tab_cab_documento.getColumna("observacion_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("observacion_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("pagado_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("pagado_cpcfa").setValorDefecto("False");
        tab_cab_documento.getColumna("total_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("total_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("numero_cpcfa").setEstilo("font-size: 12px;font-weight: bold");
        tab_cab_documento.getColumna("numero_cpcfa").setNombreVisual("NUMERO");
        tab_cab_documento.getColumna("numero_cpcfa").setOrden(4);
        tab_cab_documento.getColumna("numero_cpcfa").setAncho(10);
        tab_cab_documento.getColumna("numero_cpcfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
        tab_cab_documento.getColumna("numero_cpcfa").setMascara("999-999-99999999");
        tab_cab_documento.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_cab_documento.getColumna("numero_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("numero_cpcfa").setMetodoChangeRuta(tab_cab_documento.getRuta() + ".aceptarDatosFactura");
        tab_cab_documento.getColumna("base_grabada_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("base_grabada_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("valor_iva_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("valor_iva_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("base_tarifa0_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("otros_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("otros_cpcfa").setMetodoChange("calcula_total_detalles_cxp");
        tab_cab_documento.getColumna("porcen_desc_cpcfa").setMetodoChange("calcula_iva1");
        tab_cab_documento.getColumna("descuento_cpcfa").setMetodoChange("calcula_total_detalles_cxp");
        tab_cab_documento.getColumna("ide_srtst").setCombo("sri_tipo_sustento_tributario", "ide_srtst", "alterno_srtst,nombre_srtst", "");
        tab_cab_documento.getColumna("ide_srtst").setValorDefecto(utilitario.getVariable("p_sri_tip_sus_tri02"));
        tab_cab_documento.getColumna("ide_srtst").setNombreVisual("SUSTENTO TRIBUTARIO");
        tab_cab_documento.getColumna("ide_srtst").setOrden(7);
        tab_cab_documento.getColumna("ide_cncre").setVisible(false);
        tab_cab_documento.getColumna("ide_cnccc").setVisible(false);
        tab_cab_documento.getColumna("valor_ice_cpcfa").setValorDefecto("0");
        tab_cab_documento.getColumna("valor_ice_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("OTROS_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("DESCUENTO_CPCFA").setVisible(false);
        tab_cab_documento.getColumna("PORCEN_DESC_CPCFA").setVisible(false);
        tab_cab_documento.setTipoFormulario(true);
        tab_cab_documento.getGrid().setColumns(6);
        tab_cab_documento.setCondicion("ide_cpcfa=-1");
        tab_cab_documento.setRecuperarLectura(true);
        tab_cab_documento.dibujar();
        //tab_cab_documento.agregarRelacion(tab_det_documento);
        tab_cab_documento.insertar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_cab_documento);
        pat_panel1.getMenuTabla().setRendered(false);
        grupo.getChildren().add(pat_panel1);

        tab_datos_documento.setRuta("pre_index.clase." + getId());
        tab_datos_documento.setId("tab_datos_documento");
        tab_datos_documento.setIdCompleto("tab_documenoCxP:tab_datos_documento");
        tab_datos_documento.setTabla("cxp_datos_factura", "ide_cpdaf", 999);
        tab_datos_documento.setTipoFormulario(true);
        tab_datos_documento.getColumna("ide_geper").setVisible(false);
        tab_datos_documento.getColumna("ide_cpdaf").setVisible(false);
        tab_datos_documento.getColumna("autorizacion_cpdaf").setVisible(false);
        tab_datos_documento.getColumna("serie_cpdaf").setNombreVisual("SERIE");
        tab_datos_documento.getColumna("serie_cpdaf").setComentario("Punto de Emision y Establecimiento");
        tab_datos_documento.getColumna("serie_cpdaf").setOrden(10);
        tab_datos_documento.getColumna("rango_inicial_cpdaf").setNombreVisual("RANGO INICIAL");
        tab_datos_documento.getColumna("rango_inicial_cpdaf").setOrden(1);
        tab_datos_documento.getColumna("rango_final_cpdaf").setNombreVisual("RANGO FINAL");
        tab_datos_documento.getColumna("rango_final_cpdaf").setOrden(2);
        tab_datos_documento.getColumna("fecha_caducidad").setNombreVisual("FECHA DE CADUCIDAD");
        tab_datos_documento.getColumna("fecha_caducidad").setOrden(3);
        tab_datos_documento.getGrid().setColumns(8);
        tab_datos_documento.setMostrarNumeroRegistros(false);
        tab_datos_documento.setCondicion("ide_cpdaf=-1");
        tab_datos_documento.getColumna("serie_cpdaf").setLectura(true);
        tab_datos_documento.dibujar();
        //tab_datos_documento.insertar(); 
        pat_panel1.setFooter(tab_datos_documento);
        tab_det_documento.setRuta("pre_index.clase." + getId());
        tab_det_documento.setId("tab_det_documento");
        tab_det_documento.setIdCompleto("tab_documenoCxP:tab_det_documento");
        tab_det_documento.setTabla("cxp_detall_factur", "ide_cpdfa", 999);
        tab_det_documento.setCondicion("ide_cpcfa=-1");
        tab_det_documento.getColumna("ide_cpdfa").setVisible(false);
        tab_det_documento.getColumna("ide_cpcfa").setVisible(false);
        tab_det_documento.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_det_documento.getColumna("ide_inarti").setAutoCompletar();
        tab_det_documento.getColumna("ide_inarti").setNombreVisual("ARTICULO");
        tab_det_documento.getColumna("ide_inarti").setOrden(1);
        tab_det_documento.getColumna("cantidad_cpdfa").setOrden(2);
        tab_det_documento.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
        tab_det_documento.getColumna("cantidad_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("precio_cpdfa").setOrden(3);
        tab_det_documento.getColumna("precio_cpdfa").setNombreVisual("PRECIO");
        tab_det_documento.getColumna("precio_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("valor_cpdfa").setEtiqueta();
        tab_det_documento.getColumna("valor_cpdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_det_documento.getColumna("valor_cpdfa").setOrden(5);
        tab_det_documento.getColumna("valor_cpdfa").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_det_documento.getColumna("valor_cpdfa").setNombreVisual("TOTAL");
        tab_det_documento.getColumna("observacion_cpdfa").setNombreVisual("OBSERVACION");
        tab_det_documento.getColumna("observacion_cpdfa").setOrden(6);
        tab_det_documento.getColumna("secuencial_cpdfa").setNombreVisual("SERIE / SECUENCIAL");
        tab_det_documento.getColumna("secuencial_cpdfa").setOrden(7);
        tab_det_documento.getColumna("devolucion_cpdfa").setValorDefecto("false");
        tab_det_documento.getColumna("alter_tribu_cpdfa").setRequerida(true);
        tab_det_documento.getColumna("alter_tribu_cpdfa").setValorDefecto("00");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setCombo(ser_producto.getListaTipoIVA());
        tab_det_documento.getColumna("iva_inarti_cpdfa").setPermitirNullCombo(false);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setOrden(4);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setLongitud(-1);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("credi_tribu_cpdfa").setVisible(false);
        tab_det_documento.getColumna("devolucion_cpdfa").setVisible(false);
        tab_det_documento.setRecuperarLectura(true);
        tab_det_documento.setScrollable(true);
        tab_det_documento.setScrollWidth(getAnchoPanel() - 15);
        tab_det_documento.setScrollHeight(getAltoPanel() - 365); //300
        tab_det_documento.setRows(100);
        tab_det_documento.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_det_documento);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");
        pat_panel.setStyle("width:100%;height:100%;overflow: hidden;display: block;");
        grupo.getChildren().add(pat_panel);

        Grid gri_total = new Grid();
        gri_total.setWidth("100%");
        gri_total.setStyle("width:" + (getAnchoPanel() - 10) + "px;border:1px");
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
        tex_otros_valores.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_otros_valores);
        gri_valores.getChildren().add(new Etiqueta("<strong> % DESCUENTO :<s/trong>"));
        tex_porc_descuento.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_porc_descuento.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_porc_descuento);

        gri_valores.getChildren().add(new Etiqueta("<strong> VALOR DESCUENTO :<s/trong>"));
        tex_valor_descuento.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_valor_descuento.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_valor_descuento);

        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTAL TARIFA 12% :<s/trong>"));
        tex_subtotal12.setDisabled(true);
        tex_subtotal12.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_subtotal12.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_subtotal12);
        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTAL TARIFA 0% :<s/trong>"));
        tex_subtotal0.setDisabled(true);
        tex_subtotal0.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_subtotal0.setValue(utilitario.getFormatoNumero("0"));
        gri_valores.getChildren().add(tex_subtotal0);
        gri_valores.getChildren().add(new Etiqueta("<strong>IVA 12% :<s/trong>"));
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

        return grupo;
    }

    public void insertar() {
        if (tab_det_documento.isFocus()) {
            if (tab_cab_documento.getValor("ide_geper") != null) {
                tab_det_documento.insertar();
            } else {
                utilitario.agregarMensajeInfo("Seleccione Proveedor", "Debe seleccionar un proveedor para realizar el documento");
            }
        }
    }

    public void eliminar() {
        if (tab_det_documento.isFocus()) {
            tab_det_documento.eliminar();
            calcularTotalDocumento();
        }
    }

    public void guardar() {
        utilitario.getConexion().setImprimirSqlConsola(true);
        tab_cab_documento.setValor("ide_cntdo", String.valueOf(com_tipo_documento.getValue()));
        tab_cab_documento.setValor("observacion_cpcfa", String.valueOf(ate_observacion.getValue()));
        if (validarDocumento()) {
            if (tab_datos_documento.isFilaInsertada()) {
                tab_datos_documento.guardar();
            }
            if (tab_cab_documento.guardar()) {
                String ide_cccfa = tab_cab_documento.getValor("ide_cpcfa");
                for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
                    tab_det_documento.setValor(i, "ide_cpcfa", ide_cccfa);
                }
                if (tab_det_documento.guardar()) {
                    //Guarda la cuenta por pagar
                    ser_cuentas_cxp.generarTransaccionCompra(tab_cab_documento);
                    //Transaccion de Inventario
                    ser_inventario.generarComprobanteTransaccionCompra(tab_cab_documento, tab_det_documento);
                    if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                        this.cerrar();
                    }
                }

            }
        }
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
    private void calcularTotalDocumento() {
        double base_grabada = 0;
        double base_no_objeto = 0;
        double base_tarifa0 = 0;
        double valor_iva = 0;
        double porcentaje_iva = 0;

        for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
            String iva = tab_det_documento.getValor(i, "iva_inarti_cpdfa");
            if (iva.equals("1")) { //SI IVA
                base_grabada = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_grabada;
                porcentaje_iva = (Double.parseDouble(utilitario.getVariable("p_sri_porcentajeIva_comp_elect"))) / 100;
                valor_iva = base_grabada * porcentaje_iva; //0.12
            } else if (iva.equals("-1")) { // NO IVA
                base_tarifa0 = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_tarifa0;
            } else if (iva.equals("0")) { // NO OBJETO
                base_no_objeto = Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa")) + base_no_objeto;
            }
        }
        tab_cab_documento.setValor("base_grabada_cpcfa", utilitario.getFormatoNumero(base_grabada));
        tab_cab_documento.setValor("base_no_objeto_iva_cpcfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_cab_documento.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(valor_iva));
        tab_cab_documento.setValor("base_tarifa0_cpcfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_cab_documento.setValor("total_cpcfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));

        tex_subtotal12.setValue(utilitario.getFormatoNumero(base_grabada));
        tex_subtotal0.setValue(utilitario.getFormatoNumero(base_no_objeto + base_tarifa0));
        tex_iva.setValue(utilitario.getFormatoNumero(valor_iva));
        tex_total.setValue(utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));
        utilitario.addUpdate("tab_documenoCxP:0:gri_valores");
    }

    public void aceptarDatosFactura(AjaxBehaviorEvent evt) {
        tab_cab_documento.modificar(evt);
        if (!com_tipo_documento.getValue().equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
            tab_cab_documento.setValor("autorizacio_cpcfa", "");
            String serie = tab_cab_documento.getValor("numero_cpcfa").substring(0, 6);
            String num_factura = tab_cab_documento.getValor("numero_cpcfa").substring(6, tab_cab_documento.getValor("numero_cpcfa").length());
            TablaGenerica tab_datos_fac = utilitario.consultar("SELECT * FROM cxp_datos_factura df WHERE "
                    + "df.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and df.ide_geper=" + tab_cab_documento.getValor("ide_geper") + " "
                    + "and df.serie_cpdaf='" + serie + "' "
                    + "and df.fecha_caducidad >='" + utilitario.getFechaActual() + "'");
            int numero_factura = Integer.parseInt(num_factura);
            int serie_factura = Integer.parseInt(serie);

            if (tab_datos_fac.getTotalFilas() > 0) {
                TablaGenerica tab_valida_numero = utilitario.consultar("SELECT * FROM cxp_datos_factura df  "
                        + "WHERE df.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "and df.ide_geper=" + tab_cab_documento.getValor("ide_geper") + " "
                        + "and df.serie_cpdaf='" + serie + "' "
                        + "and df.fecha_caducidad >'" + utilitario.getFechaActual() + "' "
                        + "and rango_inicial_cpdaf<=" + numero_factura + " "
                        + "and rango_final_cpdaf >=" + numero_factura + " order by df.ide_cpdaf desc ");
                if (tab_valida_numero.getTotalFilas() > 0) {
                    tab_cab_documento.setValor("autorizacio_cpcfa", tab_valida_numero.getValor(0, "autorizacion_cpdaf"));
                    tab_datos_documento.setCondicion("ide_cpdaf=" + tab_valida_numero.getValor("ide_cpdaf"));
                    tab_datos_documento.ejecutarSql();
                    utilitario.addUpdate("tab_documenoCxP:0:tab_cab_documento:AUTORIZACIO_CPCFA_7");
                } else {
                    //otro rango 
                    if (serie_factura <= numero_factura || serie_factura >= numero_factura) {
                        String seriecaracter = tab_cab_documento.getValor("numero_cpcfa").substring(0, 6);
                        tab_datos_documento.setValor("serie_cpdaf", seriecaracter);
                        tab_datos_documento.setValor("ide_geper", tab_cab_documento.getValor("ide_geper"));
                        tab_datos_documento.setValor("autorizacion_cpdaf", "");
                        tab_datos_documento.setValor("rango_inicial_cpdaf", "");
                        tab_datos_documento.setValor("rango_final_cpdaf", "");
                        tab_datos_documento.setValor("fecha_caducidad", "");
                        utilitario.addUpdateTabla(tab_datos_documento, "serie_cpdaf,autorizacion_cpdaf,rango_inicial_cpdaf,rango_final_cpdaf,fecha_caducidad", "");
                    }
                }
            } else {
                //No existe autorizacion de la factura pra el numero ingresado                        
                String seriecaracter = tab_cab_documento.getValor("numero_cpcfa").substring(0, 6);
                tab_datos_documento.setValor("serie_cpdaf", seriecaracter);
                tab_datos_documento.setValor("ide_geper", tab_cab_documento.getValor("ide_geper"));
                tab_datos_documento.setValor("autorizacion_cpdaf", "");
                tab_datos_documento.setValor("rango_inicial_cpdaf", "");
                tab_datos_documento.setValor("rango_final_cpdaf", "");
                tab_datos_documento.setValor("fecha_caducidad", "");
                utilitario.addUpdateTabla(tab_datos_documento, "serie_cpdaf,autorizacion_cpdaf,rango_inicial_cpdaf,rango_final_cpdaf,fecha_caducidad", "");
            }
        }
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
        if (tab_cab_documento.getValor("autorizacio_cpcfa") == null || tab_cab_documento.getValor("autorizacio_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar el Número de Autorizacion del Documento");
            return false;
        }
        if (tab_cab_documento.getValor("observacion_cpcfa") == null || tab_cab_documento.getValor("observacion_cpcfa").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "Debe ingresar la observacion del Documento");
            return false;
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

        //Valida que se hayan ingresado los datos del documento si se a insertado
        if (tab_datos_documento.isFilaInsertada()) {
            if (tab_datos_documento.getValor("rango_inicial_cpdaf") == null || tab_datos_documento.getValor("rango_inicial_cpdaf").isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar la Factura", "Debe ingresar el rango inicial del Documento");
                return false;
            }
            if (tab_datos_documento.getValor("rango_final_cpdaf") == null || tab_datos_documento.getValor("rango_final_cpdaf").isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar la Factura", "Debe ingresar el rango final del Documento");
                return false;
            }
            if (tab_datos_documento.getValor("fecha_caducidad") == null || tab_datos_documento.getValor("fecha_caducidad").isEmpty()) {
                utilitario.agregarMensajeError("Error al guardar la Factura", "Debe ingresar la fecha de caducidad del Documento");
                return false;
            }
        }

        List lis_numeros_fact = utilitario.getConexion().consultar("select * from cxp_cabece_factur where numero_cpcfa='" + tab_cab_documento.getValor("numero_cpcfa") + "' and ide_geper=" + tab_cab_documento.getValor("ide_geper") + " and autorizacio_cpcfa='" + tab_cab_documento.getValor("autorizacio_cpcfa") + "'");
        if (lis_numeros_fact.size() > 0) {
            utilitario.agregarMensajeError("Error al guardar la Factura", "El número de factura del proveedor ya existe ");
            return false;
        }
        return true;
    }

    /**
     * Cambia tipo de documento
     */
    public void cambiarTipoDocumento() {
        if (com_tipo_documento.getValue() != null) {
            cargarProveedores();
        }
    }

    public void cargarProveedores() {
        // solo ruc 
        if (com_tipo_documento.getValue().equals(utilitario.getVariable("p_con_tipo_documento_factura"))) {
            tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid=" + utilitario.getVariable("p_gen_tipo_iden_ruc"));
            tab_cab_documento.setValor("ide_geper", null);
            utilitario.addUpdate("tab_documenoCxP:tab_cab_factura");
            if (!tab_datos_documento.isFilaInsertada()) {
                tab_datos_documento.insertar();
            }
            tab_cab_documento.setValor("autorizacio_cpcfa", "");
            tab_cab_documento.setValor("numero_cpcfa", "");
            utilitario.addUpdate("tab_documenoCxP:0:tab_cab_documento:AUTORIZACIO_CPCFA_7,tab_documenoCxP:0:tab_cab_documento:NUMERO_CPCFA_6,tab_documenoCxP:0:tab_cab_documento:IDE_GEPER_4");
        }
        // diferencte de ruc
        if (com_tipo_documento.getValue().equals(utilitario.getVariable("p_con_tipo_documento_liquidacion_compra"))) {
            tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid!=" + utilitario.getVariable("p_gen_tipo_iden_ruc"));
            tab_cab_documento.setValor("ide_geper", null);
            tab_datos_documento.limpiar();
            //Carga secuencial y autorizacion de liquidaciones en compra
            TablaGenerica tab_cxp_cab = utilitario.consultar("select * from cxp_cabece_factur where ide_empr=" + utilitario.getVariable("ide_empr") + " and  ide_cntdo=" + utilitario.getVariable("p_con_tipo_documento_liquidacion_compra") + " order by numero_cpcfa desc limit 1");
            if (tab_cxp_cab.getTotalFilas() > 0) {
                tab_cab_documento.setValor("autorizacio_cpcfa", tab_cxp_cab.getValor(0, "autorizacio_cpcfa"));
                String num_liq = tab_cxp_cab.getValor(0, "numero_cpcfa");
                String num = (Integer.parseInt(num_liq.substring(6, num_liq.length())) + 1) + "";
                String num_liquidacion = num_liq.substring(0, 6);
                num_liquidacion = num_liquidacion.concat(utilitario.generarCero(8 - num.length())).concat(num);
                System.out.println("num " + num_liquidacion);
                tab_cab_documento.setValor("numero_cpcfa", num_liquidacion);
                utilitario.addUpdate("tab_documenoCxP:0:tab_cab_documento:AUTORIZACIO_CPCFA_7,tab_documenoCxP:0:tab_cab_documento:NUMERO_CPCFA_6,tab_documenoCxP:0:tab_cab_documento:IDE_GEPER_4");
            }
            utilitario.addUpdate("tab_documenoCxP:tab_cab_factura");
        }
        // solo rise
        if (com_tipo_documento.getValue().equals(utilitario.getVariable("p_con_tipo_documento_nota_venta"))) {
            tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_cntco = " + utilitario.getVariable("p_con_tipo_contribuyente_rise"));
            tab_cab_documento.setValor("ide_geper", null);
            utilitario.addUpdate("tab_documenoCxP:tab_cab_factura");
            if (!tab_datos_documento.isFilaInsertada()) {
                tab_datos_documento.insertar();
            }
            tab_cab_documento.setValor("autorizacio_cpcfa", "");
            tab_cab_documento.setValor("numero_cpcfa", "");
            utilitario.addUpdate("tab_documenoCxP:0:tab_cab_documento:AUTORIZACIO_CPCFA_7,tab_documenoCxP:0:tab_cab_documento:NUMERO_CPCFA_6,tab_documenoCxP:0:tab_cab_documento:IDE_GEPER_4");
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

    public Tabla getTab_datos_documento() {
        return tab_datos_documento;
    }

    public void setTab_datos_documento(Tabla tab_datos_documento) {
        this.tab_datos_documento = tab_datos_documento;
    }

    /**
     * Selecciona una Tab Mediante Javascript
     *
     * @param index
     */
    public void seleccionarTab(int index) {
        tabActiva = index;
        String str_script_activa = "w_factura.select(" + index + ");";
        tab_documenoCxP.setActiveIndex(index);
        for (int i = 0; i < tab_documenoCxP.getChildren().size(); i++) {
            str_script_activa += tab_documenoCxP.getTab(i).isDisabled() == false ? "w_factura.enable(" + i + ");" : "w_factura.disable(" + i + ");";
        }
        utilitario.ejecutarJavaScript(str_script_activa);
    }

    /**
     * Selecciona el proveedor
     *
     * @param evt
     */
    public void seleccionarProveedor(SelectEvent evt) {
        if (tab_datos_documento.isFilaInsertada()) {
            tab_datos_documento.setValor("ide_geper", tab_cab_documento.getValor("ide_geper"));
        }
    }

}
