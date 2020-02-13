/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import componentes.DocumentoCxP;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.FileUploadEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author ANDRES
 */
public class pre_factura_compras extends Pantalla {

    private SeleccionTabla sel_tab_compras = new SeleccionTabla();
    private SeleccionTabla sel_tab_detalle_compra = new SeleccionTabla();
    private Tabla tab_cab_documento = new Tabla();
    private Tabla tab_det_documento = new Tabla();
    String solicitud = "";
    double dou_total = 0;
    double dou_base_ingresada = 0;
    private double tarifaIVA = 0;
    private Dialogo dia_xml = new Dialogo();
    private Upload upl_xml = new Upload();
    private DocumentoCxP doc_cuenta_pagar = new DocumentoCxP();
    private Map<String, String> parametros;
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

    @EJB
    private final ServicioComprobanteContabilidad ser_comp_contabilidad = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    @EJB
    private final ServicioProduccion ser_produccion = (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class);

    public pre_factura_compras() {

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
                "p_con_tipo_contribuyente_rise", "p_com_sisfinanciero_forma_pago", "p_com_sustento_bodega");
        if (tienePerfilSecretaria() != 0) {

            Boton bt_panel = new Boton();
            bt_panel.setValue("CARGAR XML");
            bt_panel.setIcon("ui-icon-folder-open");
            bt_panel.setTitle("Seleccionar una Factura Electrónica en formato XML");
            bt_panel.setMetodo("dibujarXML");
            bar_botones.agregarBoton(bt_panel);

            // creo dialogo para crear modalidad
            dia_xml.setId("dia_modalidad");
            dia_xml.setTitle("SELECCIONAR FACTURA ELECTRÓNICA XML");
            dia_xml.setWidth("40%");
            dia_xml.setHeight("30%");
            dia_xml.getBot_aceptar().setMetodo("aceptarModalidad");
            dia_xml.setResizable(false);

            Grupo gru_cuerpo = new Grupo();
            upl_xml.setId("upl_cxp_xml");
            upl_xml.setAllowTypes("/(\\.|\\/)(xml)$/");
            gru_cuerpo.getChildren().add(upl_xml);
            upl_xml.setMetodo("seleccionarArchivoXML");
            upl_xml.setUploadLabel("Validar Factura .xml");
            upl_xml.setAuto(false);
            dia_xml.setDialogo(gru_cuerpo);
            agregarComponente(dia_xml);

            tab_cab_documento.setId("tab_cab_documento");
            tab_cab_documento.setTabla("cxp_cabece_factur", "ide_cpcfa", 1);
            tab_cab_documento.setCampoOrden("ide_cpcfa desc limit 200");
            tab_cab_documento.getColumna("ide_cntdo").setVisible(true);
            tab_cab_documento.getColumna("ide_cntdo").setNombreVisual("TIPO DOCUMENTO");
            tab_cab_documento.getColumna("ide_cntdo").setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
            tab_cab_documento.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));
            tab_cab_documento.getColumna("ide_cntdo").setLectura(true);
            tab_cab_documento.getColumna("ide_cpefa").setValorDefecto(parametros.get("p_cxp_estado_factura_normal"));
            tab_cab_documento.getColumna("ide_cpefa").setVisible(false);
            tab_cab_documento.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp=3");
            tab_cab_documento.getColumna("ide_cndfp").setRequerida(false);
            tab_cab_documento.getColumna("TARIFA_IVA_CPCFA").setVisible(false);
            tab_cab_documento.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
            tab_cab_documento.getColumna("ide_cndfp").setValorDefecto(parametros.get("p_com_sisfinanciero_forma_pago"));
            //tab_cab_documento.getColumna("ide_cndfp").setLectura(true);
            tab_cab_documento.getColumna("ide_cndfp").setOrden(5);
            tab_cab_documento.getColumna("dias_credito_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("dias_credito_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("dias_credito_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("ide_cndfp1").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp!=3");
            tab_cab_documento.getColumna("ide_cndfp1").setOrden(50);
            tab_cab_documento.getColumna("ide_cndfp1").setNombreVisual("DÍAS CREDITO");
            tab_cab_documento.getColumna("ide_cndfp1").setEstilo("width:140px");
            tab_cab_documento.getColumna("ide_cndfp1").setVisible(false);
            tab_cab_documento.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
            tab_cab_documento.getColumna("ide_usua").setVisible(false);
            tab_cab_documento.getColumna("MONTO_COM_CPCFA").setVisible(false);
            tab_cab_documento.getColumna("IDE_CNTIC").setVisible(false);
            tab_cab_documento.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
            tab_cab_documento.getColumna("fecha_trans_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
            tab_cab_documento.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA EMISIÓN");
            tab_cab_documento.getColumna("fecha_emisi_cpcfa").setOrden(3);
            tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", ""); //por defecto no carga los clientes
            tab_cab_documento.getColumna("ide_geper").setAutoCompletar();
            tab_cab_documento.getColumna("ide_geper").setRequerida(true);
            tab_cab_documento.getColumna("ide_geper").setNombreVisual("PROVEEDOR");
            tab_cab_documento.getColumna("ide_geper").setOrden(1);
            tab_cab_documento.getColumna("autorizacio_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("autorizacio_cpcfa").setOrden(5);
            tab_cab_documento.getColumna("autorizacio_cpcfa").setNombreVisual("NUM. AUTORIZACIÓN");
            tab_cab_documento.getColumna("autorizacio_cpcfa").setEstilo("font-weight: bold");
            tab_cab_documento.getColumna("observacion_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("observacion_cpcfa").setMayusculas(true);
            tab_cab_documento.getColumna("pagado_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("pagado_cpcfa").setValorDefecto("False");
            tab_cab_documento.getColumna("total_cpcfa").setNombreVisual("TOTAL:");
            tab_cab_documento.getColumna("total_cpcfa").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
            tab_cab_documento.getColumna("total_cpcfa").setVisible(true);
            tab_cab_documento.getColumna("total_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("total_cpcfa").setEtiqueta();
            tab_cab_documento.getColumna("numero_cpcfa").setEstilo("font-size: 12px;font-weight: bold");
            tab_cab_documento.getColumna("numero_cpcfa").setNombreVisual("NÚMERO");
            tab_cab_documento.getColumna("numero_cpcfa").setOrden(4);
            tab_cab_documento.getColumna("numero_cpcfa").setAncho(10);
            tab_cab_documento.getColumna("numero_cpcfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
            tab_cab_documento.getColumna("numero_cpcfa").setMascara("999-999-999999999");
            tab_cab_documento.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
            tab_cab_documento.getColumna("numero_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("base_grabada_cpcfa").setVisible(true);
            tab_cab_documento.getColumna("base_grabada_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("valor_iva_cpcfa").setVisible(true);
            tab_cab_documento.getColumna("valor_iva_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setVisible(true);
            tab_cab_documento.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("base_tarifa0_cpcfa").setVisible(true);
            tab_cab_documento.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("otros_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("ide_srtst").setCombo("sri_tipo_sustento_tributario", "ide_srtst", "alterno_srtst,nombre_srtst", "");
            tab_cab_documento.getColumna("ide_srtst").setValorDefecto(parametros.get("p_com_sustento_bodega"));
            tab_cab_documento.getColumna("ide_srtst").setNombreVisual("SUSTENTO TRIBUTARIO");
            //tab_cab_documento.getColumna("ide_srtst").setLectura(true);
            tab_cab_documento.getColumna("ide_srtst").setOrden(7);
            tab_cab_documento.getColumna("ide_cncre").setVisible(false);
            tab_cab_documento.getColumna("ide_cnccc").setVisible(false);
            tab_cab_documento.getColumna("valor_ice_cpcfa").setValorDefecto("0");
            tab_cab_documento.getColumna("valor_ice_cpcfa").setVisible(true);
            tab_cab_documento.getColumna("OTROS_CPCFA").setVisible(false);
            tab_cab_documento.getColumna("DESCUENTO_CPCFA").setVisible(true);
            tab_cab_documento.getColumna("PORCEN_DESC_CPCFA").setEtiqueta();
            tab_cab_documento.getColumna("PORCEN_DESC_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
            tab_cab_documento.getColumna("DESCUENTO_CPCFA").setEtiqueta();
            tab_cab_documento.getColumna("DESCUENTO_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
            tab_cab_documento.getColumna("valor_ice_cpcfa").setEtiqueta();
            tab_cab_documento.getColumna("valor_ice_cpcfa").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
            tab_cab_documento.getColumna("BASE_NO_OBJETO_IVA_CPCFA").setEtiqueta();
            tab_cab_documento.getColumna("BASE_NO_OBJETO_IVA_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
            tab_cab_documento.getColumna("BASE_GRABADA_CPCFA").setEtiqueta();
            tab_cab_documento.getColumna("BASE_GRABADA_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
            tab_cab_documento.getColumna("VALOR_IVA_CPCFA").setEtiqueta();
            tab_cab_documento.getColumna("VALOR_IVA_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
            tab_cab_documento.getColumna("BASE_TARIFA0_CPCFA").setEtiqueta();
            tab_cab_documento.getColumna("BASE_TARIFA0_CPCFA").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo

            tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setOrden(8);
            tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setNombreVisual("TIPO DOC. MODI.");
            tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
            tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setOrden(9);
            tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setNombreVisual("FECHA EMISIÓN DOC. MODI.");
            tab_cab_documento.getColumna("numero_nc_cpcfa").setOrden(10);
            tab_cab_documento.getColumna("numero_nc_cpcfa").setNombreVisual("NÚMERO DOC. MODI.");
            tab_cab_documento.getColumna("numero_nc_cpcfa").setMascara("999-999-999999999");
            tab_cab_documento.getColumna("numero_nc_cpcfa").setQuitarCaracteresEspeciales(true);
            tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setNombreVisual("AUTORIZACIÓN DOC. MODI.");
            tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setOrden(11);
            tab_cab_documento.getColumna("autorizacio_cpcfa").setMetodoChange("validarAutorizacion");

            tab_cab_documento.getColumna("motivo_nc_cpcfa").setOrden(12);
            tab_cab_documento.getColumna("motivo_nc_cpcfa").setNombreVisual("MOTIVO");
            tab_cab_documento.getColumna("ide_rem_cpcfa").setVisible(false);
            //solo para que dibuje el *
            tab_cab_documento.getColumna("motivo_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("numero_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("ide_adcomp").setVisible(false);
            tab_cab_documento.getColumna("LIQUIDA_NOTA_CPCFA").setVisible(false);
            tab_cab_documento.getColumna("RECIBIDO_COMPRA_CPCFA").setLectura(true);
            tab_cab_documento.getColumna("RECIBIDO_COMPRA_CPCFA").setValorDefecto("FALSE");

            tab_cab_documento.getColumna("motivo_nc_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("numero_nc_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setVisible(false);
            tab_cab_documento.getColumna("motivo_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("numero_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("fecha_emision_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("ide_cntdo_nc_cpcfa").setRequerida(false);
            tab_cab_documento.getColumna("ide_srcom").setVisible(false);
            tab_cab_documento.getColumna("ide_ccdaf").setVisible(false);
            //tab_cab_documento.getColumna("recibido_activo_cpcfa").setVisible(false);

            tab_cab_documento.setTipoFormulario(true);
            tab_cab_documento.getGrid().setColumns(4);
            tab_cab_documento.agregarRelacion(tab_det_documento);
            tab_cab_documento.dibujar();

            PanelTabla pat_panel1 = new PanelTabla();
            pat_panel1.setId("pat_panel1");
            pat_panel1.setPanelTabla(tab_cab_documento);

            tab_det_documento.setId("tab_det_documento");
            tab_det_documento.setTabla("cxp_detall_factur", "ide_cpdfa", 2);
            tab_det_documento.getColumna("ide_cpdfa").setVisible(false);
            tab_det_documento.getColumna("ide_cpcfa").setVisible(false);
            tab_det_documento.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
            tab_det_documento.getColumna("IDE_INUNI").setCombo(ser_produccion.getUnidad());
            tab_det_documento.getColumna("ide_inarti").setAutoCompletar();
            tab_det_documento.getColumna("ide_inarti").setNombreVisual("ARTICULO");
            tab_det_documento.getColumna("ide_inarti").setOrden(1);
            tab_det_documento.getColumna("cantidad_cpdfa").setOrden(2);
            tab_det_documento.getColumna("cantidad_cpdfa").setNombreVisual("CANTIDAD");
            tab_det_documento.getColumna("precio_cpdfa").setOrden(3);
            tab_det_documento.getColumna("precio_cpdfa").setNombreVisual("PRECIO");
            tab_det_documento.getColumna("cantidad_cpdfa").setMetodoChange("calcularDetalle");
            tab_det_documento.getColumna("precio_cpdfa").setMetodoChange("calcularDetalle");
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
            tab_det_documento.getColumna("alter_tribu_cpdfa").setValorDefecto("00");
            tab_det_documento.getColumna("iva_inarti_cpdfa").setCombo(ser_producto.getListaTipoIVA());
            tab_det_documento.getColumna("iva_inarti_cpdfa").setPermitirNullCombo(false);
            tab_det_documento.getColumna("iva_inarti_cpdfa").setValorDefecto("1");
            tab_det_documento.getColumna("iva_inarti_cpdfa").setOrden(4);
            tab_det_documento.getColumna("iva_inarti_cpdfa").setLongitud(-1);
            tab_det_documento.getColumna("iva_inarti_cpdfa").setNombreVisual("APLICA IVA");
            tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChange("calcularDetalle");
            tab_det_documento.getColumna("recibido_compra_cpdfa").setValorDefecto("FALSE");
            tab_det_documento.getColumna("observacion_cpdfa").setVisible(false);
            tab_det_documento.getColumna("alter_tribu_cpdfa").setVisible(false);
            tab_det_documento.getColumna("credi_tribu_cpdfa").setVisible(false);
            tab_det_documento.getColumna("credi_tribu_cpdfa").setValorDefecto("false");
            tab_det_documento.getColumna("devolucion_cpdfa").setVisible(false);
            tab_det_documento.setRows(100);
            tab_det_documento.getColumna("recibido_compra_cpdfa").setLectura(true);
            tab_det_documento.dibujar();

            PanelTabla pat_pane2 = new PanelTabla();
            pat_pane2.setId("pat_pane2");
            pat_pane2.setPanelTabla(tab_det_documento);

            Division div_factura = new Division();
            div_factura.setId("div_factura");
            div_factura.dividir2(pat_panel1, pat_pane2, "50%", "H");
            agregarComponente(div_factura);

            Boton bot_busca_presu = new Boton();
            bot_busca_presu.setValue("BUSCAR SOLICITUD DE COMPRA");
            bot_busca_presu.setIcon("ui-icon-search");
            bot_busca_presu.setMetodo("abrirDialogoSolicitud");

            bar_botones.agregarBoton(bot_busca_presu);

            sel_tab_compras.setId("sel_tab_compras");
            sel_tab_compras.setTitle("SELECCIONA UNA SOLICITUD DE COMPRA");
            sel_tab_compras.setSeleccionTabla(ser_adquisiciones.getSolicitudCompra("1", ""), "ide_adcomp");
            sel_tab_compras.setWidth("80%");
            sel_tab_compras.setHeight("70%");
            sel_tab_compras.setRadio();
            sel_tab_compras.getTab_seleccion().getColumna("numero_orden_adcomp").setFiltroContenido();
            sel_tab_compras.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
            sel_tab_compras.getBot_aceptar().setMetodo("aceptarSolicitud");
            agregarComponente(sel_tab_compras);
            //  sel_tab_compras.getTab_seleccion().getColumna("identificac_geper").setFiltroContenido();
            //  sel_tab_compras.getTab_seleccion().getColumna("nom_geper").setFiltroContenido();
            //  

            sel_tab_detalle_compra.setId("sel_tab_detalle_compra");
            sel_tab_detalle_compra.setTitle("SELECCIONA EL DETALLE DE LA COMPRA");
            sel_tab_detalle_compra.setSeleccionTabla(ser_adquisiciones.getdetalleSolicitudCompra("1", ""), "ide_adcobi");
            sel_tab_detalle_compra.setWidth("80%");
            sel_tab_detalle_compra.setHeight("70%");
            //sel_tab_detalle_compra.setRadio();
            sel_tab_detalle_compra.getBot_aceptar().setMetodo("generarCabecera");
            agregarComponente(sel_tab_detalle_compra);

        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el registro de la orden de gasto de Compras. Consulte con el Administrador");
        }

    }

    public void dibujarXML() {
        dia_xml.dibujar();
    }

    public void seleccionarArchivoXML(FileUploadEvent event) {

        doc_cuenta_pagar.seleccionarArchivoXML(event);
    }

    /*
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
        }
    }

    /**
     * Valida Factura CxP para poder guardar
     *
     * @return
     */
    public boolean validarDocumento() {

        String autorizacion = tab_cab_documento.getValor("autorizacio_cpcfa");
        boolean correcto = false;
        if (autorizacion.length() == 37 || autorizacion.length() == 49 || autorizacion.length() == 10) {
            correcto = true;
        } else {
            correcto = false;
            utilitario.agregarMensajeInfo("La longitud del Número de Autorización no es válido", autorizacion);
            return false;
        }
        return correcto;
    }

    public void calcularTotalDocumento() {
        tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());
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

        try {
            porce_descuento = Double.parseDouble(utilitario.getFormatoNumero(tab_cab_documento.getValor("porcen_desc_cpcfa")));
        } catch (Exception e) {
        }

        double descuento = 0;
        double valor_ice = 0;

        try {
            descuento = Double.parseDouble(utilitario.getFormatoNumero(tab_cab_documento.getValor("descuento_cpcfa")));
        } catch (Exception e) {
        }

        try {
            valor_ice = Double.parseDouble(utilitario.getFormatoNumero(tab_cab_documento.getValor("valor_ice_cpcfa")));
        } catch (Exception e) {
        }

        //base_grabada = base_grabada - descuento;
        valor_iva = (base_grabada - descuento) * tarifaIVA; //0.12
        if (valor_ice > 0) {
            valor_iva += (valor_ice * tarifaIVA); //0.12
        }
        tab_cab_documento.setValor("porcen_desc_cpcfa", utilitario.getFormatoNumero(porce_descuento));
        tab_cab_documento.setValor("descuento_cpcfa", utilitario.getFormatoNumero(descuento));
        tab_cab_documento.setValor("valor_ice_cpcfa", utilitario.getFormatoNumero(valor_ice));

        tab_cab_documento.setValor("base_grabada_cpcfa", utilitario.getFormatoNumero(base_grabada));
        tab_cab_documento.setValor("base_no_objeto_iva_cpcfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_cab_documento.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(valor_iva));
        tab_cab_documento.setValor("base_tarifa0_cpcfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_cab_documento.setValor("total_cpcfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva + valor_ice));
        tab_cab_documento.modificar(tab_cab_documento.getFilaActual());
        utilitario.addUpdateTabla(tab_cab_documento, "porcen_desc_cpcfa,descuento_cpcfa,valor_ice_cpcfa,base_grabada_cpcfa,base_no_objeto_iva_cpcfa,valor_iva_cpcfa,total_cpcfa,base_tarifa0_cpcfa", "");
    }

    public void abrirDialogoSolicitud() {
        sel_tab_compras.dibujar();
    }

    public void aceptarSolicitud() {
        solicitud = sel_tab_compras.getValorSeleccionado();
        sel_tab_compras.cerrar();
        sel_tab_detalle_compra.getTab_seleccion().setSql(ser_adquisiciones.getdetalleSolicitudCompra("2", solicitud));
        sel_tab_detalle_compra.getTab_seleccion().ejecutarSql();
        sel_tab_detalle_compra.dibujar();
    }

    public void generarCabecera() {
        TablaGenerica tab_cabece_fac = utilitario.consultar("select ide_adcomp, numero_orden_adcomp, a.ide_geper, b.identificac_geper, b.nom_geper, fecha_solicitud_adcomp, valor_adcomp, detalle_adcomp\n"
                + "from adq_compra a\n"
                + "left join gen_persona b on a.ide_geper = b.ide_geper\n"
                + "where a.ide_adcomp = " + solicitud + "");
        if (tab_cab_documento.isFilaInsertada() == false) {
            tab_cab_documento.insertar();

        }
        tab_cab_documento.setValor("ide_geper", tab_cabece_fac.getValor("ide_geper"));
        tab_cab_documento.setValor("ide_adcomp", tab_cabece_fac.getValor("ide_adcomp"));

        tab_cab_documento.guardar();
        guardarPantalla();
        ser_cuentas_cxp.generarTransaccionCompra(tab_cab_documento);
        sel_tab_detalle_compra.cerrar();
        utilitario.addUpdate("tab_cab_documento");
        generaDetalle();
    }

    public void generaDetalle() {
        String detalle = sel_tab_detalle_compra.getSeleccionados();
        TablaGenerica tab_deta_fac = utilitario.consultar("select ide_adcobi, ide_adcomp, b.ide_inarti, b.nombre_inarti, cantidad_adcobi, especificaciones_adcobi from adq_compra_bienes a\n"
                + "left join inv_articulo b on a.ide_inarti = b.ide_inarti\n"
                + "where a.ide_adcomp = " + solicitud + "\n"
                + "and ide_adcobi in (" + detalle + ")");
        for (int i = 0; i < tab_deta_fac.getTotalFilas(); i++) {
            tab_det_documento.insertar();
            tab_det_documento.setValor("ide_inarti", tab_deta_fac.getValor(i, "ide_inarti"));
            tab_det_documento.setValor("cantidad_cpdfa", tab_deta_fac.getValor(i, "cantidad_adcobi"));
        }
        tab_det_documento.guardar();
        guardarPantalla();
        utilitario.addUpdate("tab_det_documento");
        utilitario.getConexion().ejecutarSql("update adq_compra  set facturado_adcomp = true where ide_adcomp = " + solicitud + "");
    }

    public void calcularDetalle(AjaxBehaviorEvent evt) {
        tab_det_documento.modificar(evt);
        calcular();
    }

    public void calcular() {
        double dou_cantidad_fac = 0;
        double dou_precio_fac = 0;
        double dou_subtotal_fac = 0;
        try {
            //Obtenemos el valor de la cantidad
            dou_cantidad_fac = Double.parseDouble(tab_det_documento.getValor("cantidad_cpdfa"));
        } catch (Exception e) {
        }

        try {
            //Obtenemos el valor
            dou_precio_fac = Double.parseDouble(tab_det_documento.getValor("precio_cpdfa"));
        } catch (Exception e) {
        }
        //Calculamos el subtotal y ganancias
        dou_subtotal_fac = dou_cantidad_fac * dou_precio_fac;

        //Asignamos el total a la tabla detalle, con 2 decimales
        tab_det_documento.setValor("valor_cpdfa", utilitario.getFormatoNumero(dou_subtotal_fac, 2));

        try {
            //Obtenemos el valor del subtotal
            dou_precio_fac = Double.parseDouble(tab_det_documento.getValor("valor_cpdfa"));
        } catch (Exception e) {
        }
        //Actualizamos el campo de la tabla AJAX
        utilitario.addUpdateTabla(tab_det_documento, "valor_cpdfa", "tab_det_documento");
        utilitario.addUpdate("tab_det_documento");
        calcularTotal();
    }

    public void calcularTotal() {
        dou_total = 0;
        dou_base_ingresada = 0;
        for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
            dou_base_ingresada += Double.parseDouble(tab_det_documento.getValor(i, "valor_cpdfa"));
        }
        tab_cab_documento.setValor("total_cpcfa", utilitario.getFormatoNumero(dou_base_ingresada, 2));
        tab_cab_documento.modificar(tab_cab_documento.getFilaActual());//para que haga el update        
        utilitario.addUpdateTabla(tab_cab_documento, "total_cpcfa", "");
        calcularTotalDocumento();
    }

    String empleado = "";
    String cedula = "";
    String ide_ademple = "";

    private int tienePerfilSecretaria() {
        List sql = utilitario.getConexion().consultar(ser_adquisiciones.getUsuarioSistemaEmpleado(utilitario.getVariable("IDE_USUA")));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);

            empleado = fila[1].toString();
            cedula = fila[2].toString();
            ide_ademple = fila[0].toString();
            return 1;

        } else {
            return 0;

        }
    }

    @Override
    public void insertar() {
        if (tab_cab_documento.isFocus()) {
            tab_cab_documento.insertar();
            //tab_cab_documento.setValor("ide_ademple", ide_ademple); descomnetar y corregir or el vendedor cuando se tenga tiempo
        } else if (tab_det_documento.isFocus()) {
            tab_det_documento.insertar();
        }
    }

    @Override
    public void guardar() {
        if (validarDocumento()) {
            if (tab_cab_documento.guardar()) {
                if (tab_det_documento.guardar()) {
                    guardarPantalla();
                }
            }
        }

    }

    @Override
    public void eliminar() {
        if (tab_cab_documento.isFocus()) {
            tab_cab_documento.eliminar();
        } else if (tab_det_documento.isFocus()) {
            tab_det_documento.eliminar();
        }
    }

    public SeleccionTabla getSel_tab_compras() {
        return sel_tab_compras;
    }

    public void setSel_tab_compras(SeleccionTabla sel_tab_compras) {
        this.sel_tab_compras = sel_tab_compras;
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

    public SeleccionTabla getSel_tab_detalle_compra() {
        return sel_tab_detalle_compra;
    }

    public void setSel_tab_detalle_compra(SeleccionTabla sel_tab_detalle_compra) {
        this.sel_tab_detalle_compra = sel_tab_detalle_compra;
    }

    public DocumentoCxP getDoc_cuenta_pagar() {
        return doc_cuenta_pagar;
    }

    public void setDoc_cuenta_pagar(DocumentoCxP doc_cuenta_pagar) {
        this.doc_cuenta_pagar = doc_cuenta_pagar;
    }

    public Upload getUpl_xml() {
        return upl_xml;
    }

    public void setUpl_xml(Upload upl_xml) {
        this.upl_xml = upl_xml;
    }

    public Dialogo getDia_xml() {
        return dia_xml;
    }

    public void setDia_xml(Dialogo dia_xml) {
        this.dia_xml = dia_xml;
    }

}
