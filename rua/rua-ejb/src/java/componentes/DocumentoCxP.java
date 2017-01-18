/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Espacio;
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
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
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
    private Combo com_anticipo;
    private Etiqueta eti_anticipo = new Etiqueta();
    private Tabla tab_cab_documento;
    private Tabla tab_det_documento;
    private Map<String, String> parametros;
    private final AreaTexto ate_observacion = new AreaTexto();
    private final Texto tex_subtotal12 = new Texto();
    private final Texto tex_subtotal0 = new Texto();
    private final Texto tex_iva = new Texto();
    private final Texto tex_total = new Texto();
    private final Texto tex_valor_descuento = new Texto();
    private final Texto tex_porc_descuento = new Texto();
    private final Texto tex_otros_valores = new Texto();
    private boolean haceKardex = false;

    private int tabActiva = 0;
    private int opcion = 0;
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
    //CLIENTE
    private Tabla tab_creacion_cliente;
    private Dialogo dia_creacion_cliente;

    //PRODUCTO
    private Tabla tab_creacion_producto;
    private Dialogo dia_creacion_producto;

    //RETENCION
    private Tabla tab_dto_prove;
    private Tabla tab_cb_rete;
    private Tabla tab_dt_rete;

    //PAGOS
    private Tabla tab_dt_pago;

    //CONTABILIDAD Asiento de Venta
    @EJB
    private final ServicioComprobanteContabilidad ser_comp_contabilidad = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);
    private Tabla tab_cab_conta;
    private Tabla tab_deta_conta;
    private final AreaTexto ate_observacion_conta = new AreaTexto();

    //REEMBOLSOS
    private Tabla tab_com_reembolso;

    public DocumentoCxP() {
        //utilitario.getConexion().setImprimirSqlConsola(true);
        //Recupera todos los parametros que se van a utilizar
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

        this.setWidth("95%");
        this.setHeight("90%");
        this.setTitle("GENERAR DOCUMENTO POR PAGAR");
        this.setResizable(false);
        this.setDynamic(false);
        tab_documenoCxP.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + (getAltoPanel() - 10) + "px;overflow: auto;display: block;");
        tab_documenoCxP.setId("tab_documenoCxP");
        tab_documenoCxP.setWidgetVar("w_documenoCxP");
        tab_documenoCxP.agregarTab("DOCUMENTO POR PAGAR ", null);//0    
        tab_documenoCxP.agregarTab("COMPROBANTE DE RETENCIÓN", null);//1
        tab_documenoCxP.agregarTab("COMPROBANTE DE CONTABILIDAD", null);//2
        tab_documenoCxP.agregarTab("DETALLE DE PAGOS", null);//3       
        this.setDialogo(tab_documenoCxP);

        //Recupera porcentaje iva
        tarifaIVA = ser_configuracion.getPorcentajeIva();
        dia_creacion_cliente = new Dialogo();
        dia_creacion_cliente.setId("dia_creacion_cliente");
        dia_creacion_cliente.setTitle("CREAR PROVEEDOR");
        dia_creacion_cliente.setHeight("65%");
        dia_creacion_cliente.setWidth("55%");
        utilitario.getPantalla().getChildren().add(dia_creacion_cliente);

        dia_creacion_producto = new Dialogo();
        dia_creacion_producto.setId("dia_creacion_producto");
        dia_creacion_producto.setTitle("CREAR PRODUCTO");
        dia_creacion_producto.setHeight("65%");
        dia_creacion_producto.setWidth("40%");
        utilitario.getPantalla().getChildren().add(dia_creacion_producto);
    }

    public void setDocumentoCxP(String titulo) {
        this.setTitle(titulo);
        this.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardar");
    }

    public void nuevoDocumento() {
        opcion = 1;  // GENERA FACTURA
        haceKardex = false;
        ocultarTabs(); //Ocilta todas las tabas
        setActivarDocumento(true); //activa solo tab de Fcatura de venta
        seleccionarTab(0);
        this.getBot_aceptar().setRendered(true);
        this.setTitle("NUEVO DOCUMENTO POR PAGAR");
        ate_observacion.setDisabled(false);
        tex_otros_valores.setDisabled(false);
        tex_valor_descuento.setDisabled(false);
        ate_observacion.setValue("");
        tab_documenoCxP.getTab(0).getChildren().clear();
        tab_documenoCxP.getTab(1).getChildren().clear();
        tab_documenoCxP.getTab(2).getChildren().clear();
        tab_documenoCxP.getTab(3).getChildren().clear();

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

    public void verDocumento(String ide_cpcfa) {
        opcion = 2;  // GENERA FACTURA
        tab_documenoCxP.getTab(0).getChildren().clear();
        tab_documenoCxP.getTab(1).getChildren().clear();
        tab_documenoCxP.getTab(2).getChildren().clear();
        tab_documenoCxP.getTab(3).getChildren().clear();

        tab_documenoCxP.getTab(0).getChildren().add(dibujarDocumento());
        activarTabs();
        seleccionarTab(0);
        this.getBot_aceptar().setRendered(false);
        this.setTitle("DOCUMENTO POR PAGAR");
        TablaGenerica tab_cab_factura = utilitario.consultar("SELECT * FROM cxp_cabece_factur WHERE ide_cpcfa=" + ide_cpcfa);
        tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "ide_geper=" + tab_cab_factura.getValor("ide_geper"));
        tab_cab_documento.setCondicion("ide_cpcfa=" + ide_cpcfa);
        tab_cab_documento.ejecutarSql();
        com_tipo_documento.setValue(tab_cab_factura.getValor("ide_cntdo"));
        tab_det_documento.setCondicion("ide_cpcfa=" + ide_cpcfa);
        tab_det_documento.ejecutarSql();
        if (tab_cab_documento.getValor("ide_cncre") != null) {
            tab_documenoCxP.getTab(1).getChildren().add(dibujarComprobanteRetencion());
        }
        if (tab_cab_documento.getValor("ide_cnccc") != null) {
            tab_documenoCxP.getTab(2).getChildren().add(dibujarAsiento());
        }
        tab_documenoCxP.getTab(3).getChildren().add(dibujarDetallePago());

        tex_iva.setValue(utilitario.getFormatoNumero(tab_cab_documento.getValor("valor_iva_cpcfa")));
        //Carga totales y observacion
        double dou_subt0 = 0;
        double dou_subtno = 0;
        try {
            dou_subt0 = Double.parseDouble(tab_cab_documento.getValor("base_tarifa0_cpcfa"));
        } catch (Exception e) {
        }
        try {
            dou_subtno = Double.parseDouble(tab_cab_documento.getValor("base_no_objeto_iva_cpcfa"));
        } catch (Exception e) {
        }
        tex_subtotal0.setValue(utilitario.getFormatoNumero(dou_subt0 + dou_subtno));
        tex_subtotal12.setValue(utilitario.getFormatoNumero(tab_cab_documento.getValor("base_grabada_cpcfa")));
        tex_total.setValue(utilitario.getFormatoNumero(tab_cab_documento.getValor("total_cpcfa")));
        ate_observacion.setValue(tab_cab_documento.getValor("observacion_cpcfa"));
        if (tab_cab_documento.getFilaSeleccionada() != null) {
            tab_cab_documento.getFilaSeleccionada().setLectura(true);
        }
        ate_observacion.setDisabled(true);
        tex_otros_valores.setDisabled(true);
        tex_valor_descuento.setDisabled(true);
        com_tipo_documento.setDisabled(true);

        //ver reembolso
        if (tab_cab_documento.getValor("ide_cntdo").equals(parametros.get("p_con_tipo_documento_reembolso"))) {
            tab_com_reembolso.setRendered(true);
            tab_com_reembolso.setCondicion("ide_rem_cpcfa=" + tab_cab_documento.getValor("ide_cpcfa"));
            tab_com_reembolso.ejecutarSql();
            //Activa click derecho insertar y eliminar
            try {
                PanelTabla pat_panel = (PanelTabla) tab_com_reembolso.getParent();
                pat_panel.getMenuTabla().getItem_insertar().setDisabled(true);
                pat_panel.getMenuTabla().getItem_eliminar().setDisabled(true);
            } catch (Exception e) {
            }
            tab_det_documento.setScrollHeight(getAltoPanel() - 445);
        } else {
            tab_com_reembolso.setRendered(false);
            tab_det_documento.setScrollHeight(getAltoPanel() - 335);
        }

        boolean bol_esnota = false;
        if (tab_cab_documento.getValor("ide_cntdo").equals(parametros.get("p_con_tipo_documento_nota_credito"))) {
            bol_esnota = true;
            tab_det_documento.setScrollHeight(getAltoPanel() - 385);
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
        //Desactiva click derecho insertar y eliminar
        try {
            PanelTabla pat_panel = (PanelTabla) tab_det_documento.getParent();
            pat_panel.getMenuTabla().getItem_insertar().setDisabled(true);
            pat_panel.getMenuTabla().getItem_eliminar().setDisabled(true);
        } catch (Exception e) {
        }
    }

    private Grupo dibujarDetallePago() {
        Grupo grupo = new Grupo();
        tab_dt_pago = new Tabla();
        tab_dt_pago.setId("tab_dt_pago");
        tab_dt_pago.setRuta("pre_index.clase." + getId());
        tab_dt_pago.setIdCompleto("tab_documenoCxP:tab_dt_pago");
        tab_dt_pago.setSql(ser_cuentas_cxp.getSqlPagosDocumento(tab_cab_documento.getValor("ide_cpcfa")));
        tab_dt_pago.setCampoPrimaria("ide_cpdtr");
        tab_dt_pago.getColumna("ide_cpdtr").setVisible(false);
        tab_dt_pago.getColumna("ide_tecba").setVisible(false);
        tab_dt_pago.getColumna("docum_relac_cpdtr").setNombreVisual("N.DOCUMENTO RELACIONADO");
        tab_dt_pago.getColumna("nombre_tettb").setNombreVisual("TIPO TRANSACCION");
        tab_dt_pago.getColumna("valor_cpdtr").setNombreVisual("VALOR");
        tab_dt_pago.getColumna("fecha_trans_cpdtr").setNombreVisual("FECHA");
        tab_dt_pago.setRows(15);
        tab_dt_pago.setColumnaSuma("valor_cpdtr");
        tab_dt_pago.setLectura(true);
        tab_dt_pago.setEmptyMessage("No existen pagos realizados");
        tab_dt_pago.dibujar();

        PanelTabla tab_panel = new PanelTabla();
        tab_panel.setPanelTabla(tab_dt_pago);
        grupo.getChildren().add(tab_panel);
        return grupo;
    }

    private Grupo dibujarComprobanteRetencion() {
        Grupo grupo = new Grupo();
        tab_dto_prove = new Tabla();
        tab_dto_prove.setRuta("pre_index.clase." + getId());
        tab_dto_prove.setId("tab_dto_prove");
        tab_dto_prove.setIdCompleto("tab_documenoCxP:tab_dto_prove");
        tab_dto_prove.setSql("select ide_geper,nom_geper,direccion_geper,ti.nombre_getid,identificac_geper "
                + "from gen_persona gp,gen_tipo_identifi ti "
                + "where ti.ide_getid=gp.ide_getid and ide_geper=" + tab_cab_documento.getValor("ide_geper"));
        tab_dto_prove.setNumeroTabla(999);
        tab_dto_prove.setCampoPrimaria("ide_geper");
        tab_dto_prove.getColumna("nombre_getid").setEtiqueta();
        tab_dto_prove.getColumna("nombre_getid").setNombreVisual("TIPO DE IDENTIFICACIÓN");
        tab_dto_prove.getColumna("nombre_getid").setEtiqueta();
        tab_dto_prove.getColumna("nombre_getid").setOrden(3);
        tab_dto_prove.getColumna("identificac_geper").setEtiqueta();
        tab_dto_prove.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_dto_prove.getColumna("identificac_geper").setOrden(4);
        tab_dto_prove.getColumna("nom_geper").setOrden(1);
        tab_dto_prove.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
        tab_dto_prove.getColumna("nom_geper").setEtiqueta();
        tab_dto_prove.getColumna("direccion_geper").setNombreVisual("DIRECCIÓN");
        tab_dto_prove.getColumna("direccion_geper").setEtiqueta();
        tab_dto_prove.getColumna("direccion_geper").setOrden(2);
        tab_dto_prove.getColumna("ide_geper").setVisible(false);

        tab_dto_prove.setNumeroTabla(-1);
        tab_dto_prove.setTipoFormulario(true);
        tab_dto_prove.getGrid().setColumns(4);
        tab_dto_prove.setMostrarNumeroRegistros(false);
        tab_dto_prove.dibujar();

        tab_cb_rete = new Tabla();
        tab_dt_rete = new Tabla();
        tab_cb_rete.setId("tab_cb_rete");
        tab_cb_rete.setIdCompleto("tab_documenoCxP:tab_cb_rete");
        tab_cb_rete.setRuta("pre_index.clase." + getId());
        tab_cb_rete.setTabla("con_cabece_retenc", "ide_cncre", 999);
        tab_cb_rete.setLectura(true);
        if (tab_cab_documento.getValor("ide_cncre") != null) {
            tab_cb_rete.setCondicion("ide_cncre=" + tab_cab_documento.getValor("ide_cncre"));
        } else {
            tab_cb_rete.setCondicion("ide_cncre=-1");
        }
        tab_cb_rete.getColumna("ide_cncre").setVisible(false);
        tab_cb_rete.getColumna("ide_cnccc").setVisible(false);
        tab_cb_rete.getColumna("ide_cnere").setVisible(false);
        tab_cb_rete.getColumna("es_venta_cncre").setVisible(false);
        tab_cb_rete.getColumna("numero_cncre").setOrden(1);
        tab_cb_rete.getColumna("numero_cncre").setNombreVisual("NÚMERO");
        tab_cb_rete.getColumna("numero_cncre").setEtiqueta();
        tab_cb_rete.getColumna("numero_cncre").setEstilo("font-size: 12px;font-weight: bold");
        tab_cb_rete.getColumna("autorizacion_cncre").setOrden(2);
        tab_cb_rete.getColumna("autorizacion_cncre").setNombreVisual("NUM. AUTORIZACIÓN");
        tab_cb_rete.getColumna("autorizacion_cncre").setEtiqueta();
        tab_cb_rete.getColumna("autorizacion_cncre").setEstilo("font-size: 12px;font-weight: bold");
        tab_cb_rete.getColumna("OBSERVACION_CNCRE").setVisible(false);
        tab_cb_rete.getColumna("FECHA_EMISI_CNCRE").setNombreVisual("FECHA EMISIÓN");
        tab_cb_rete.getColumna("FECHA_EMISI_CNCRE").setEtiqueta();
        tab_cb_rete.setTipoFormulario(true);
        tab_cb_rete.getGrid().setColumns(6);
        tab_cb_rete.setMostrarNumeroRegistros(false);
        tab_cb_rete.dibujar();

        tab_dt_rete.setId("tab_dt_rete");
        tab_dt_rete.setRuta("pre_index.clase." + getId());
        tab_dt_rete.setIdCompleto("tab_documenoCxP:tab_dt_rete");
        tab_dt_rete.setTabla("con_detall_retenc", "ide_cndre", 999);
        if (tab_cab_documento.getValor("ide_cncre") != null) {
            tab_dt_rete.setCondicion("ide_cncre=" + tab_cab_documento.getValor("ide_cncre"));
        } else {
            tab_dt_rete.setCondicion("ide_cncre=-1");
        }
        tab_dt_rete.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
        tab_dt_rete.getColumna("ide_cncim").setNombreVisual("IMPUESTO");
        tab_dt_rete.getColumna("ide_cncim").setLongitud(200);
        tab_dt_rete.getColumna("valor_cndre").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_dt_rete.getColumna("valor_cndre").setNombreVisual("VALOR");
        tab_dt_rete.getColumna("valor_cndre").alinearDerecha();
        tab_dt_rete.getColumna("valor_cndre").setEstilo("font-size: 15px;font-weight: bold;");
        tab_dt_rete.setColumnaSuma("valor_cndre");
        tab_dt_rete.getColumna("porcentaje_cndre").setNombreVisual("PORCENTAJE RETENCIÓN");
        tab_dt_rete.getColumna("porcentaje_cndre").setLongitud(50);
        tab_dt_rete.getColumna("base_cndre").setNombreVisual("BASE IMPONIBLE");
        tab_dt_rete.getColumna("base_cndre").setLongitud(50);
        tab_dt_rete.getColumna("ide_cndre").setVisible(false);
        tab_dt_rete.getColumna("ide_cncre").setVisible(false);
        tab_dt_rete.setScrollable(true);
        tab_dt_rete.setScrollHeight(getAltoPanel() - 240);
        tab_dt_rete.setRows(100);
        tab_dt_rete.setLectura(true);
        tab_dt_rete.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_dt_rete);

        grupo.getChildren().add(tab_cb_rete);
        grupo.getChildren().add(tab_dto_prove);
        grupo.getChildren().add(new Separator());

        Grid gri_td = new Grid();
        gri_td.setWidth("60%");
        gri_td.setColumns(4);
        gri_td.getChildren().add(new Etiqueta("<strong>TIPO DE COMPROBANTE :</strong>"));
        gri_td.getChildren().add(new Etiqueta("<span style='font-size: 14px;font-weight: bold'>" + ser_cuentas_cxp.getNombreTipoDocumento(tab_cab_documento.getValor("ide_cntdo")) + "</span>"));
        gri_td.getChildren().add(new Etiqueta("<strong>NÚMERO DE COMPROBANTE :</strong>"));
        gri_td.getChildren().add(new Etiqueta("<span style='font-size: 14px;font-weight: bold'>" + tab_cab_documento.getValor("numero_cpcfa") + "</span>"));
        grupo.getChildren().add(gri_td);
        grupo.getChildren().add(new Separator());
        grupo.getChildren().add(pat_panel);
        return grupo;

    }

    private Grupo dibujarDocumento() {
        Grupo grupo = new Grupo();

        com_tipo_documento = new Combo();
        com_tipo_documento.setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        com_tipo_documento.setMetodoRuta("pre_index.clase." + getId() + ".cambiarTipoDocumento");
        //com_tipo_documento.eliminarVacio();

        Grid gri_pto = new Grid();
        gri_pto.setId("gri_pto");
        gri_pto.setColumns(11);
        gri_pto.getChildren().add(new Etiqueta("<strong>TIPO DE DOCUMENTO :</strong> <span style='color:red;font-weight: bold;'>*</span>"));
        gri_pto.getChildren().add(com_tipo_documento);

        if (opcion == 1) {
            dia_creacion_producto.getGri_cuerpo().getChildren().clear();
            dia_creacion_cliente.getGri_cuerpo().getChildren().clear();
            gri_pto.getChildren().add(new Espacio("10", "1"));
            Boton botCrearCliente = new Boton();
            botCrearCliente.setId("botCrearCliente");
            botCrearCliente.setValue("Crear Proveedor");
            botCrearCliente.setIcon("ui-icon-person");
            botCrearCliente.setMetodoRuta("pre_index.clase." + getId() + ".abrirProveedor");
            gri_pto.getChildren().add(botCrearCliente);
            gri_pto.getChildren().add(new Espacio("5", "1"));

            Boton botCrearProducto = new Boton();
            botCrearProducto.setId("botCrearProducto");
            botCrearProducto.setValue("Crear Producto");
            botCrearProducto.setIcon("ui-icon-cart");
            botCrearProducto.setMetodoRuta("pre_index.clase." + getId() + ".abrirProducto");
            gri_pto.getChildren().add(botCrearProducto);

            dia_creacion_cliente.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardarProveedor");
            dia_creacion_cliente.getBot_cancelar().setMetodoRuta("pre_index.clase." + getId() + ".cerrarDialogos");

            tab_creacion_cliente = new Tabla();
            tab_creacion_cliente.setId("tab_creacion_cliente");
            tab_creacion_cliente.setRuta("pre_index.clase." + getId());
            tab_creacion_cliente.setIdCompleto("tab_documenoCxP:tab_creacion_cliente");
            ser_proveedor.configurarTablaProveedor(tab_creacion_cliente);
            tab_creacion_cliente.setTabla("gen_persona", "ide_geper", -1);
            tab_creacion_cliente.setCondicion("ide_geper=-1");
            tab_creacion_cliente.getGrid().setColumns(2);
            tab_creacion_cliente.getColumna("ide_geper").setVisible(false);
            tab_creacion_cliente.getColumna("IDE_GEGEN").setVisible(false);
            tab_creacion_cliente.getColumna("IDE_VGECL").setVisible(false);
            tab_creacion_cliente.getColumna("IDE_GEUBI").setVisible(false);
            tab_creacion_cliente.getColumna("IDE_VGTCL").setVisible(false);
            tab_creacion_cliente.getColumna("FAX_GEPER").setVisible(false);
            tab_creacion_cliente.getColumna("PAGINA_WEB_GEPER").setVisible(false);
            tab_creacion_cliente.getColumna("REPRE_LEGAL_GEPER").setVisible(false);

            tab_creacion_cliente.getColumna("IDE_GETID").setNombreVisual("TIPO DE IDENTIFICACIÓN");
            tab_creacion_cliente.getColumna("IDE_GETID").setOrden(1);
            tab_creacion_cliente.getColumna("IDENTIFICAC_GEPER").setNombreVisual("IDENTIFICACIÓN");
            tab_creacion_cliente.getColumna("IDENTIFICAC_GEPER").setOrden(2);
            tab_creacion_cliente.getColumna("NOM_GEPER").setNombreVisual("NOMBRE");
            tab_creacion_cliente.getColumna("NOM_GEPER").setOrden(3);
            tab_creacion_cliente.getColumna("NOMBRE_COMPL_GEPER").setNombreVisual("NOMBRE COMERCIAL");
            tab_creacion_cliente.getColumna("NOMBRE_COMPL_GEPER").setOrden(4);
            tab_creacion_cliente.getColumna("IDE_CNTCO").setNombreVisual("TIPO DE CONTRIBUYENTE");
            tab_creacion_cliente.getColumna("IDE_CNTCO").setOrden(5);
            tab_creacion_cliente.getColumna("DIRECCION_GEPER").setNombreVisual("DIRECCIÓN");
            tab_creacion_cliente.getColumna("DIRECCION_GEPER").setOrden(6);
            tab_creacion_cliente.getColumna("DIRECCION_GEPER").setRequerida(true);
            tab_creacion_cliente.getColumna("TELEFONO_GEPER").setNombreVisual("TELÉFONO");
            tab_creacion_cliente.getColumna("TELEFONO_GEPER").setOrden(7);
            tab_creacion_cliente.getColumna("TELEFONO_GEPER").setRequerida(true);
            tab_creacion_cliente.getColumna("CONTACTO_GEPER").setNombreVisual("CONTACTO");
            tab_creacion_cliente.getColumna("CONTACTO_GEPER").setOrden(8);
            tab_creacion_cliente.getColumna("MOVIL_GEPER").setNombreVisual("CELULAR");
            tab_creacion_cliente.getColumna("MOVIL_GEPER").setOrden(9);
            tab_creacion_cliente.getColumna("CORREO_GEPER").setNombreVisual("E-MAIL");
            tab_creacion_cliente.getColumna("CORREO_GEPER").setOrden(10);
            tab_creacion_cliente.getColumna("OBSERVACION_GEPER").setNombreVisual("OBSERVACIÓN");
            tab_creacion_cliente.getColumna("OBSERVACION_GEPER").setOrden(11);

            tab_creacion_cliente.setMostrarNumeroRegistros(false);
            tab_creacion_cliente.dibujar();
            tab_creacion_cliente.insertar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_creacion_cliente);
            pat_panel.getMenuTabla().setRendered(false);
            pat_panel.setStyle("overflow:hiden");
            dia_creacion_cliente.setDialogo(pat_panel);

            ///PRODUCTO 
            dia_creacion_producto.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardarProducto");
            dia_creacion_producto.getBot_cancelar().setMetodoRuta("pre_index.clase." + getId() + ".cerrarDialogos");

            tab_creacion_producto = new Tabla();
            tab_creacion_producto.setId("tab_creacion_producto");
            tab_creacion_producto.setRuta("pre_index.clase." + getId());
            tab_creacion_producto.setIdCompleto("tab_documenoCxP:tab_creacion_producto");
            ser_producto.configurarTablaProducto(tab_creacion_producto);
            tab_creacion_producto.setTabla("inv_articulo", "ide_inarti", 999);
            tab_creacion_producto.setCondicion("ide_inarti=-1");
            tab_creacion_producto.getColumna("INV_IDE_INARTI").setRequerida(true);
            tab_creacion_producto.getColumna("INV_IDE_INARTI").setNombreVisual("GRUPO");
            tab_creacion_producto.setMostrarNumeroRegistros(false);
            tab_creacion_producto.getColumna("IDE_INARTI").setVisible(false);
            tab_creacion_producto.getColumna("IDE_INFAB").setVisible(false);
            tab_creacion_producto.getColumna("IDE_INEPR").setVisible(false);
            tab_creacion_producto.getColumna("ES_COMBO_INARTI").setVisible(false);
            tab_creacion_producto.getColumna("IDE_GEORG").setVisible(false);
            tab_creacion_producto.getColumna("IDE_GEORG").setVisible(false);
            tab_creacion_producto.getColumna("nivel_inarti").setVisible(false);
            tab_creacion_producto.getColumna("NOMBRE_INARTI").setNombreVisual("NOMBRE");
            tab_creacion_producto.getColumna("NOMBRE_INARTI").setOrden(1);
            tab_creacion_producto.getColumna("CODIGO_INARTI").setNombreVisual("CÓDIGO");
            tab_creacion_producto.getColumna("CODIGO_INARTI").setOrden(2);
            tab_creacion_producto.getColumna("IDE_INMAR").setNombreVisual("MARCA");
            tab_creacion_producto.getColumna("IDE_INMAR").setOrden(3);
            tab_creacion_producto.getColumna("IDE_INUNI").setNombreVisual("UNIDAD");
            tab_creacion_producto.getColumna("IDE_INUNI").setOrden(4);
            tab_creacion_producto.getColumna("IDE_INTPR").setNombreVisual("TIPO PRODUCTO");
            tab_creacion_producto.getColumna("IDE_INTPR").setOrden(5);
            tab_creacion_producto.getColumna("IVA_INARTI").setNombreVisual("IVA ?");
            tab_creacion_producto.getColumna("IVA_INARTI").setRequerida(true);
            tab_creacion_producto.getColumna("IVA_INARTI").setOrden(6);
            tab_creacion_producto.getColumna("ICE_INARTI").setNombreVisual("APLICA ICE ?");
            tab_creacion_producto.getColumna("ICE_INARTI").setOrden(7);
            tab_creacion_producto.getColumna("HACE_KARDEX_INARTI").setNombreVisual("HACE KARDEX ?");
            tab_creacion_producto.getColumna("HACE_KARDEX_INARTI").setOrden(8);
            tab_creacion_producto.getColumna("OBSERVACION_INARTI").setNombreVisual("OBSERVACIÓN");
            tab_creacion_producto.getColumna("OBSERVACION_INARTI").setOrden(9);
            tab_creacion_producto.getGrid().setColumns(2);
            tab_creacion_producto.dibujar();
            tab_creacion_producto.insertar();
            tab_creacion_producto.setValor("nivel_inarti", "HIJO");
            PanelTabla pat_panel2 = new PanelTabla();
            pat_panel2.setPanelTabla(tab_creacion_producto);
            pat_panel2.getMenuTabla().setRendered(false);
            pat_panel2.setStyle("overflow:hiden");
            dia_creacion_producto.setDialogo(pat_panel2);

        }

        grupo.getChildren().add(gri_pto);

        //Anticipo 
        eti_anticipo.setValue("ANTICIPO :");
        eti_anticipo.setRendered(false);
        eti_anticipo.setStyle("font-weight: bold;");
        gri_pto.getChildren().add(eti_anticipo);
        com_anticipo = new Combo();
        com_anticipo.setRendered(false);

        gri_pto.getChildren().add(com_anticipo);

        tab_cab_documento = new Tabla();
        tab_det_documento = new Tabla();
        tab_cab_documento.setRuta("pre_index.clase." + getId());
        tab_cab_documento.setId("tab_cab_documento");
        tab_cab_documento.setIdCompleto("tab_documenoCxP:tab_cab_documento");
        tab_cab_documento.setMostrarNumeroRegistros(false);
        tab_cab_documento.setTabla("cxp_cabece_factur", "ide_cpcfa", 999);
        tab_cab_documento.getColumna("ide_cpcfa").setVisible(false);
        tab_cab_documento.getColumna("ide_cntdo").setVisible(false);
        tab_cab_documento.getColumna("ide_cpefa").setValorDefecto(parametros.get("p_cxp_estado_factura_normal"));
        tab_cab_documento.getColumna("ide_cpefa").setVisible(false);
        tab_cab_documento.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp not in ( " + parametros.get("p_con_for_pag_reembolso_caja") + "," + parametros.get("p_con_for_pag_anticipo") + " )");
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
        tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "ide_geper=-1"); //por defecto no carga los clientes
        tab_cab_documento.getColumna("ide_geper").setAutoCompletar();
        tab_cab_documento.getColumna("ide_geper").setRequerida(true);
        tab_cab_documento.getColumna("ide_geper").setNombreVisual("PROVEEDOR");
        tab_cab_documento.getColumna("ide_geper").setMetodoChangeRuta("pre_index.clase." + getId() + ".seleccionarProveedor");
        tab_cab_documento.getColumna("ide_geper").setOrden(3);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setRequerida(true);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setOrden(5);
        tab_cab_documento.getColumna("autorizacio_cpcfa").setNombreVisual("NUM. AUTORIZACIÓN");
        tab_cab_documento.getColumna("autorizacio_cpcfa").setEstilo("font-weight: bold");
        tab_cab_documento.getColumna("autorizacio_cpcfa").setMetodoChangeRuta(tab_cab_documento.getRuta() + ".validarAutorizacion");
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
        tab_cab_documento.getColumna("autorizacio_nc_cpcfa").setMetodoChangeRuta(tab_cab_documento.getRuta() + ".validarAutorizacion");
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
        //tab_cab_documento.agregarRelacion(tab_det_documento);
        tab_cab_documento.insertar();

        for (int i = 0; i < tab_cab_documento.getGrid().getChildren().size(); i++) {
            if (tab_cab_documento.getGrid().getChildren().get(i).getId() != null) {
                if (tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("IDE_CNTDO_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("FECHA_EMISION_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("NUMERO_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("AUTORIZACIO_NC_CPCFA")
                        || tab_cab_documento.getGrid().getChildren().get(i).getId().startsWith("MOTIVO_NC_CPCFA")) {
                    tab_cab_documento.getGrid().getChildren().get(i).setRendered(false);
                    tab_cab_documento.getGrid().getChildren().get(i - 1).setRendered(false);
                }
            }
        }
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

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_cab_documento);
        pat_panel1.getMenuTabla().setRendered(false);
        grupo.getChildren().add(pat_panel1);

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
        tab_det_documento.getColumna("observacion_cpdfa").setNombreVisual("OBSERVACIÓN");
        tab_det_documento.getColumna("observacion_cpdfa").setOrden(6);
        tab_det_documento.getColumna("secuencial_cpdfa").setNombreVisual("SERIE / SECUENCIAL");
        tab_det_documento.getColumna("secuencial_cpdfa").setOrden(7);
        tab_det_documento.getColumna("devolucion_cpdfa").setValorDefecto("false");
        tab_det_documento.getColumna("alter_tribu_cpdfa").setRequerida(true);
        tab_det_documento.getColumna("alter_tribu_cpdfa").setValorDefecto("00");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setCombo(ser_producto.getListaTipoIVA());
        tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setPermitirNullCombo(false);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setValorDefecto("1");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setOrden(4);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setLongitud(-1);
        tab_det_documento.getColumna("iva_inarti_cpdfa").setNombreVisual("APLICA IVA");
        tab_det_documento.getColumna("iva_inarti_cpdfa").setMetodoChangeRuta(tab_det_documento.getRuta() + ".cambioPrecioCantidadIva");
        tab_det_documento.getColumna("credi_tribu_cpdfa").setVisible(false);
        tab_det_documento.getColumna("devolucion_cpdfa").setVisible(false);
        tab_det_documento.setRecuperarLectura(true);
        tab_det_documento.setScrollable(true);
        tab_det_documento.setScrollWidth(getAnchoPanel() - 15);
        tab_det_documento.setScrollHeight(getAltoPanel() - 335); //300
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
        pat_panel.getMenuTabla().getItem_eliminar().setValueExpression("rendered", "true");
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);
        pat_panel.setStyle("width:100%;height:100%;overflow: hidden;display: block;");
        grupo.getChildren().add(pat_panel);

        tab_com_reembolso = new Tabla();
        tab_com_reembolso.setRuta("pre_index.clase." + getId());
        tab_com_reembolso.setRendered(false);
        tab_com_reembolso.setHeader("DATOS COMPROBANTE DE REEMBOLSO");
        tab_com_reembolso.setId("tab_com_reembolso");
        tab_com_reembolso.setTabla("cxp_cabece_factur", "ide_cpcfa", 999);
        tab_com_reembolso.setIdCompleto("tab_documenoCxP:tab_com_reembolso");
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
        tab_com_reembolso.getColumna("autorizacio_cpcfa").setMetodoChangeRuta(tab_cab_documento.getRuta() + ".validarAutorizacion");
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
        tab_com_reembolso.getColumna("base_grabada_cpcfa").setMetodoChangeRuta(tab_cab_documento.getRuta() + ".calculaIvaReembolso");
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
        pat_panel2.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel2.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");
        pat_panel2.getMenuTabla().getItem_eliminar().setValueExpression("rendered", "true");
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);
        // pat_panel2.setStyle("width:100%;height:100%;overflow: hidden;display: block;");
        grupo.getChildren().add(pat_panel2);

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
        tex_otros_valores.setMetodoChangeRuta(tab_det_documento.getRuta() + ".calcularTotalDocumento");
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
        tex_valor_descuento.setMetodoChangeRuta(tab_det_documento.getRuta() + ".calcularTotalDocumento");
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

        return grupo;
    }

    private Grupo dibujarAsiento() {
        Grupo grupo = new Grupo();
        tab_cab_conta = new Tabla();
        tab_deta_conta = new Tabla();
        tab_cab_conta.setRuta("pre_index.clase." + getId());
        tab_cab_conta.setId("tab_cab_conta");
        tab_cab_conta.setIdCompleto("tab_documenoCxP:tab_cab_conta");
        if (tab_cab_documento.getValor("ide_cnccc") != null) {
            tab_cab_conta.setSql(ser_comp_contabilidad.getSqlCabeceraAsiento(tab_cab_documento.getValor("ide_cnccc")));
        } else {
            tab_cab_conta.setSql(ser_comp_contabilidad.getSqlCabeceraAsiento("-1"));
        }

        tab_cab_conta.getColumna("ide_cnccc").setNombreVisual("TRANSACCIÓN");
        tab_cab_conta.getColumna("ide_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("numero_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("numero_cnccc").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cab_conta.getColumna("numero_cnccc").setNombreVisual("NUM. COMPROBANTE");
        tab_cab_conta.getColumna("numero_cnccc").setOrden(5);
        tab_cab_conta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_cab_conta.getColumna("fecha_trans_cnccc").setOrden(1);
        tab_cab_conta.getColumna("fecha_trans_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("nom_usua").setVisible(false);
        tab_cab_conta.getColumna("fecha_siste_cnccc").setVisible(false);
        tab_cab_conta.getColumna("hora_sistem_cnccc").setVisible(false);
        tab_cab_conta.getColumna("nom_modu").setEtiqueta();
        tab_cab_conta.getColumna("nom_modu").setNombreVisual("MÓDULO");
        tab_cab_conta.getColumna("nom_modu").setOrden(4);
        tab_cab_conta.getColumna("nom_modu").setEstilo("width:150px");
        tab_cab_conta.getColumna("nom_geper").setEtiqueta();
        tab_cab_conta.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
        tab_cab_conta.getColumna("nom_geper").setOrden(2);
        tab_cab_conta.getColumna("nombre_cntcm").setEtiqueta();
        tab_cab_conta.getColumna("nombre_cntcm").setNombreVisual("TIPO COMPROBANTE");
        tab_cab_conta.getColumna("nombre_cntcm").setEstilo("width:100px");
        tab_cab_conta.getColumna("nombre_cntcm").setOrden(3);
        tab_cab_conta.getColumna("OBSERVACION_CNCCC").setVisible(false);
        tab_cab_conta.setTipoFormulario(true);
        tab_cab_conta.getGrid().setColumns(6);
        tab_cab_conta.setMostrarNumeroRegistros(false);
        tab_cab_conta.setLectura(true);
        tab_cab_conta.dibujar();
        tab_cab_conta.setLectura(false);

        tab_deta_conta.setRuta("pre_index.clase." + getId());
        tab_deta_conta.setId("tab_deta_conta");
        tab_deta_conta.setIdCompleto("tab_documenoCxP:tab_deta_conta");
        tab_deta_conta.setSql(ser_comp_contabilidad.getSqlDetalleAsiento(tab_cab_conta.getValorSeleccionado()));
        tab_deta_conta.getColumna("ide_cndcc").setVisible(false);
        tab_deta_conta.getColumna("codig_recur_cndpc").setNombreVisual("CÓDIGO CUENTA");
        tab_deta_conta.getColumna("nombre_cndpc").setNombreVisual("CUENTA");
        tab_deta_conta.getColumna("observacion_cndcc").setNombreVisual("observacion");
        tab_deta_conta.setColumnaSuma("debe,haber");
        tab_deta_conta.getColumna("debe").alinearDerecha();
        tab_deta_conta.getColumna("debe").setLongitud(25);
        tab_deta_conta.getColumna("haber").alinearDerecha();
        tab_deta_conta.getColumna("haber").setLongitud(25);
        tab_deta_conta.getColumna("OBSERVACION_CNDCC").setNombreVisual("OBSERVACIÓN");
        tab_deta_conta.setScrollable(true);
        tab_deta_conta.setScrollHeight(getAltoPanel() - 240); //240
        tab_deta_conta.setLectura(true);
        tab_deta_conta.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_conta);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);

        grupo.getChildren().add(tab_cab_conta);
        grupo.getChildren().add(pat_panel);

        Grid gri_observa = new Grid();
        gri_observa.setColumns(2);
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong>"));
        gri_observa.getChildren().add(new Etiqueta(""));
        ate_observacion_conta.setValue(tab_cab_conta.getValor("observacion_cnccc"));
        ate_observacion_conta.setCols(120);
        ate_observacion_conta.setDisabled(true);
        gri_observa.getChildren().add(ate_observacion_conta);
        if (tab_cab_documento.getValor("ide_cnccc") != null) {
            gri_observa.getChildren().add(new Etiqueta("<table style='padding-left:10px;'><tr><td><strong>USUARIO CREADOR :</strong></td><td>" + tab_cab_conta.getValor("nom_usua") + " </td></tr><td><strong>FECHA SISTEMA :</strong></td><td>" + utilitario.getFormatoFecha(tab_cab_conta.getValor("fecha_siste_cnccc")) + " </td><tr> </tr><td><strong>HORA SISTEMA :</strong></td><td>" + utilitario.getFormatoHora(tab_cab_conta.getValor("hora_sistem_cnccc")) + " </td><tr> </tr></table>"));
        }
        grupo.getChildren().add(gri_observa);
        return grupo;
    }

    public void seleccionarProveedor(SelectEvent evt) {
        com_anticipo.setCombo(ser_cuentas_cxp.getSqlAnticiposCliente(tab_cab_documento.getValor("ide_geper")));
        if (com_anticipo.getChildCount() > 1) {
            com_anticipo.setRendered(true);
            eti_anticipo.setRendered(true);
            utilitario.agregarMensaje("El Proveedor: " + tab_cab_documento.getValorArreglo("ide_geper", 1), "Tiene Anticipos registrados");
        } else {
            com_anticipo.setRendered(false);
            eti_anticipo.setRendered(false);
        }
        utilitario.addUpdate("tab_documenoCxP:0:gri_pto");
    }

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

    public void eliminar() {
        if (tab_det_documento.isFocus()) {
            tab_det_documento.eliminar();
            calcularTotalDocumento();
        } else if (tab_com_reembolso.isFocus()) {
            tab_com_reembolso.eliminar();
        }
    }

    public void guardar() {
        if (opcion == 1) {
            tab_cab_documento.setValor("ide_cntdo", String.valueOf(com_tipo_documento.getValue()));
            tab_cab_documento.setValor("observacion_cpcfa", String.valueOf(ate_observacion.getValue()));

            tab_cab_documento.setValor("descuento_cpcfa", utilitario.getFormatoNumero(tex_valor_descuento.getValue()));
            tab_cab_documento.setValor("otros_cpcfa", utilitario.getFormatoNumero(tex_otros_valores.getValue()));
            tab_cab_documento.setValor("tarifa_iva_cpcfa", utilitario.getFormatoNumero(tarifaIVA));

            if (validarDocumento()) {

                if (tab_cab_documento.guardar()) {
                    String ide_cccfa = tab_cab_documento.getValor("ide_cpcfa");
                    for (int i = 0; i < tab_det_documento.getTotalFilas(); i++) {
                        tab_det_documento.setValor(i, "ide_cpcfa", ide_cccfa);
                        if (haceKardex == false) {
                            if (ser_inventario.isHaceKardex(tab_det_documento.getValor(i, "ide_inarti"))) {
                                haceKardex = true;
                            }
                        }
                    }
                    if (tab_det_documento.guardar()) {
                        if (tab_com_reembolso.isRendered()) {
                            for (int i = 0; i < tab_com_reembolso.getTotalFilas(); i++) {
                                tab_com_reembolso.setValor(i, "ide_rem_cpcfa", ide_cccfa);
                            }
                        }
                        if (tab_com_reembolso.guardar()) {
                            //Guarda la cuenta por pagar                            
                            if (com_anticipo.getValue() == null) {
                                ser_cuentas_cxp.generarTransaccionCompra(tab_cab_documento);
                            } else {
                                ser_cuentas_cxp.generarTransaccionCompraAnticipo(tab_cab_documento, String.valueOf(com_anticipo.getValue()));
                            }

                            //Transaccion de Inventario
                            if (haceKardex) {
                                ser_inventario.generarComprobanteTransaccionCompra(tab_cab_documento, tab_det_documento);
                            }
                            if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                                this.cerrar();
                            }
                        }
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
        utilitario.addUpdate("tab_documenoCxP:0:gri_valores");
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
        //Valida que no se haya ingresado ya el documento
        List lis_numeros_fact = utilitario.getConexion().consultar("select * from cxp_cabece_factur where numero_cpcfa='" + tab_cab_documento.getValor("numero_cpcfa") + "' and ide_geper=" + tab_cab_documento.getValor("ide_geper") + " and autorizacio_cpcfa='" + tab_cab_documento.getValor("autorizacio_cpcfa") + "'");
        if (lis_numeros_fact.size() > 0) {
            utilitario.agregarMensajeError("Error al guardar el Documento", "El número de del documento ingresado ya existe ");
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

    /**
     * Cambia tipo de documento
     */
    public void cambiarTipoDocumento() {
        boolean bol_esnota = false;
        if (com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_nota_credito"))) {
            bol_esnota = true;
            tab_det_documento.setScrollHeight(getAltoPanel() - 385);
        } else {
            tab_det_documento.setScrollHeight(getAltoPanel() - 335);
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
            tab_det_documento.setScrollHeight(getAltoPanel() - 445);
            try {
                PanelTabla pat_panel = (PanelTabla) tab_com_reembolso.getParent();
                pat_panel.getMenuTabla().getItem_insertar().setDisabled(false);
                pat_panel.getMenuTabla().getItem_eliminar().setDisabled(false);
            } catch (Exception e) {
            }
        } else {
            tab_com_reembolso.setRendered(false);
        }
        utilitario.addUpdate("tab_documenoCxP:0:tab_com_reembolso,tab_documenoCxP:0:tab_cab_documento,tab_documenoCxP:0:tab_det_documento,tab_documenoCxP:0:panel_tab_com_reembolso");
        if (com_tipo_documento.getValue() != null) {
            cargarProveedores();
        }
    }

    public void cargarProveedores() {
        // solo ruc 
        if (com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_factura")) || com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_reembolso")) || com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_nota_credito")) || com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_nota_venta"))) {
            tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid=" + parametros.get("p_gen_tipo_iden_ruc"));
            tab_cab_documento.setValor("ide_geper", null);
            tab_cab_documento.setValor("autorizacio_cpcfa", "");
            tab_cab_documento.setValor("numero_cpcfa", "");
            utilitario.addUpdate("tab_documenoCxP:0:tab_cab_documento:AUTORIZACIO_CPCFA_7,tab_documenoCxP:0:tab_cab_documento:NUMERO_CPCFA_6,tab_documenoCxP:0:tab_cab_documento:IDE_GEPER_4");
        }
        // diferencte de ruc
        if (com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_liquidacion_compra"))) {
            tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_getid!=" + parametros.get("p_gen_tipo_iden_ruc"));
            tab_cab_documento.setValor("ide_geper", null);
            //Carga secuencial y autorizacion de liquidaciones en compra
            TablaGenerica tab_cxp_cab = utilitario.consultar("select * from cxp_cabece_factur where ide_empr=" + utilitario.getVariable("ide_empr") + " and  ide_cntdo=" + parametros.get("p_con_tipo_documento_liquidacion_compra") + " order by numero_cpcfa desc limit 1");
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
        }
//        // solo rise
//        if (com_tipo_documento.getValue().equals(parametros.get("p_con_tipo_documento_nota_venta"))) {
//            tab_cab_documento.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO' and ide_cntco = " + parametros.get("p_con_tipo_contribuyente_rise"));
//            tab_cab_documento.setValor("ide_geper", null);
//            tab_cab_documento.setValor("autorizacio_cpcfa", "");
//            tab_cab_documento.setValor("numero_cpcfa", "");
//            utilitario.addUpdate("tab_documenoCxP:0:tab_cab_documento:AUTORIZACIO_CPCFA_7,tab_documenoCxP:0:tab_cab_documento:NUMERO_CPCFA_6,tab_documenoCxP:0:tab_cab_documento:IDE_GEPER_4");
//        }
    }

    public void guardarProducto() {
        if (true) { //!!!!!!!!******Validar Datos Producto
            if (tab_creacion_producto.guardar()) {
                //Respalda insertadas para que no guarde
                List<String> lis_resp_cab = tab_cab_documento.getInsertadas();
                List<String> lis_resp_deta = tab_det_documento.getInsertadas();
                if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                    tab_det_documento.actualizarCombos();
                    //tab_det_documento.insertar();
                    //lis_resp_deta.add(tab_det_documento.getFilaSeleccionada().getRowKey());
                    //tab_det_documento.setValor("ide_inarti", tab_creacion_producto.getValor("ide_inarti"));
                    //tab_det_documento.setValor("iva_inarti_cpdfa", tab_creacion_producto.getValor("IVA_INARTI"));                    
                    utilitario.addUpdate("tab_documenoCxP:tab_det_documento");
                    tab_creacion_producto.limpiar();
                    //tab_creacion_producto.insertar();
                    dia_creacion_producto.cerrar();
                }
                tab_cab_documento.setInsertadas(lis_resp_cab);
                tab_det_documento.setInsertadas(lis_resp_deta);
            }
        }
    }

    public void guardarProveedor() {
        if (ser_proveedor.validarProveedor(tab_creacion_cliente)) {
            if (tab_creacion_cliente.guardar()) {
                //Respalda insertadas para que no guarde
                List<String> lis_resp_cab = tab_cab_documento.getInsertadas();
                List<String> lis_resp_deta = tab_det_documento.getInsertadas();

                if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                    //Se guardo correctamente
                    tab_cab_documento.actualizarCombos();
                    tab_cab_documento.setValor("ide_geper", tab_creacion_cliente.getValor("ide_geper"));
                    //tab_cab_documento.setValor("direccion_cccfa", tab_creacion_cliente.getValor("direccion_geper"));
                    //tab_cab_documento.setValor("telefono_cccfa", tab_creacion_cliente.getValor("telefono_geper"));
                    // utilitario.addUpdateTabla(tab_cab_documento, "direccion_cccfa,telefono_cccfa,ide_geper", "");
                    utilitario.addUpdate("tab_documenoCxP:0:tab_cab_documento:IDE_GEPER_4");
                    //utilitario.addUpdateTabla(tab_cab_documento, "ide_geper", "");
                    tab_creacion_cliente.limpiar();
                    //tab_creacion_cliente.insertar();
                    dia_creacion_cliente.cerrar();
                }
                tab_cab_documento.setInsertadas(lis_resp_cab);
                tab_det_documento.setInsertadas(lis_resp_deta);
            }
        }
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

    /**
     * Selecciona una Tab Mediante Javascript
     *
     * @param index
     */
    public void seleccionarTab(int index) {
        tabActiva = index;
        String str_script_activa = "w_documenoCxP.select(" + index + ");";
        tab_documenoCxP.setActiveIndex(index);
        for (int i = 0; i < tab_documenoCxP.getChildren().size(); i++) {
            str_script_activa += tab_documenoCxP.getTab(i).isDisabled() == false ? "w_documenoCxP.enable(" + i + ");" : "w_documenoCxP.disable(" + i + ");";
        }
        utilitario.ejecutarJavaScript(str_script_activa);
    }

    public void cerrarDialogos() {
        if (dia_creacion_cliente != null && dia_creacion_cliente.isVisible()) {
            dia_creacion_cliente.cerrar();
        }
        if (dia_creacion_producto != null && dia_creacion_producto.isVisible()) {
            dia_creacion_producto.cerrar();
        }
    }

    public void abrirProducto() {
        if (tab_cab_documento.getValor("ide_geper") != null) {
            tab_creacion_producto.limpiar();
            tab_creacion_producto.insertar();
            dia_creacion_producto.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Proveedor", "");
        }
    }

    public void abrirProveedor() {
        if (com_tipo_documento.getValue() != null) {
            tab_creacion_cliente.limpiar();
            tab_creacion_cliente.insertar();
            dia_creacion_cliente.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Tipo de Documento", "");
        }

    }

    /**
     * Oculta todas las tabs
     */
    private void ocultarTabs() {
        for (int i = 0; i < tab_documenoCxP.getChildren().size(); i++) {
            tab_documenoCxP.getTab(i).setRendered(false);
        }
    }

    /**
     * Activa todas las tabs
     */
    private void activarTabs() {
        for (int i = 0; i < tab_documenoCxP.getChildren().size(); i++) {
            tab_documenoCxP.getTab(i).setRendered(true);
            tab_documenoCxP.getTab(i).setDisabled(false);
        }
    }

    public void setActivarDocumento(boolean activarAsientoCosto) {
        tab_documenoCxP.getTab(0).setRendered(activarAsientoCosto);
        tab_documenoCxP.getTab(0).setDisabled(!activarAsientoCosto);
    }

    public Tabla getTab_creacion_cliente() {
        return tab_creacion_cliente;
    }

    public void setTab_creacion_cliente(Tabla tab_creacion_cliente) {
        this.tab_creacion_cliente = tab_creacion_cliente;
    }

    public Dialogo getDia_creacion_cliente() {
        return dia_creacion_cliente;
    }

    public void setDia_creacion_cliente(Dialogo dia_creacion_cliente) {
        this.dia_creacion_cliente = dia_creacion_cliente;
    }

    public Tabla getTab_creacion_producto() {
        return tab_creacion_producto;
    }

    public void setTab_creacion_producto(Tabla tab_creacion_producto) {
        this.tab_creacion_producto = tab_creacion_producto;
    }

    public Dialogo getDia_creacion_producto() {
        return dia_creacion_producto;
    }

    public void setDia_creacion_producto(Dialogo dia_creacion_producto) {
        this.dia_creacion_producto = dia_creacion_producto;
    }

    public Tabla getTab_dto_prove() {
        return tab_dto_prove;
    }

    public void setTab_dto_prove(Tabla tab_dto_prove) {
        this.tab_dto_prove = tab_dto_prove;
    }

    public Tabla getTab_cb_rete() {
        return tab_cb_rete;
    }

    public void setTab_cb_rete(Tabla tab_cb_rete) {
        this.tab_cb_rete = tab_cb_rete;
    }

    public Tabla getTab_dt_rete() {
        return tab_dt_rete;
    }

    public void setTab_dt_rete(Tabla tab_dt_rete) {
        this.tab_dt_rete = tab_dt_rete;
    }

    public Tabla getTab_dt_pago() {
        return tab_dt_pago;
    }

    public void setTab_dt_pago(Tabla tab_dt_pago) {
        this.tab_dt_pago = tab_dt_pago;
    }

    public Tabla getTab_cab_conta() {
        return tab_cab_conta;
    }

    public void setTab_cab_conta(Tabla tab_cab_conta) {
        this.tab_cab_conta = tab_cab_conta;
    }

    public Tabla getTab_deta_conta() {
        return tab_deta_conta;
    }

    public void setTab_deta_conta(Tabla tab_deta_conta) {
        this.tab_deta_conta = tab_deta_conta;
    }

    public Tabla getTab_com_reembolso() {
        return tab_com_reembolso;
    }

    public void setTab_com_reembolso(Tabla tab_com_reembolso) {
        this.tab_com_reembolso = tab_com_reembolso;
    }

}
