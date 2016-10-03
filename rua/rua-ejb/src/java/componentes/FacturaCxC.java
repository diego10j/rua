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
import framework.componentes.Mensaje;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import servicios.sri.ServicioComprobatesElectronicos;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class FacturaCxC extends Dialogo {

    private final Utilitario utilitario = new Utilitario();

    /////FACTURA
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);
    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);

    private final Tabulador tab_factura = new Tabulador();
    private Tabla tab_cab_factura = new Tabla();
    private Tabla tab_deta_factura = new Tabla();
    private final AreaTexto ate_observacion = new AreaTexto();
    private final Texto tex_subtotal12 = new Texto();
    private final Texto tex_subtotal0 = new Texto();
    private final Texto tex_iva = new Texto();
    private final Texto tex_total = new Texto();
    private Combo com_pto_emision = new Combo();
    private double tarifaIVA = 0;

    //FORMA DE PAGO
    private Tabla tab_deta_pago;

    //CONTABILIDAD Asiento de Venta
    @EJB
    private final ServicioComprobanteContabilidad ser_comp_contabilidad = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);
    private Tabla tab_cab_conta;
    private Tabla tab_deta_conta;
    private final AreaTexto ate_observacion_conta = new AreaTexto();

    //COMPROBANTES ELECTRONICOS
    @EJB
    private final ServicioComprobatesElectronicos ser_comprobante = (ServicioComprobatesElectronicos) utilitario.instanciarEJB(ServicioComprobatesElectronicos.class);

    /**
     * Opcion que se va a realizar con el componente
     *
     * @opcion == 1 CREAR FACTURA
     * @opcion == 2 VER FACTURA
     */
    private int opcion = 0;
    private int tabActiva = 0;

    private final Mensaje men_factura = new Mensaje();

    //RETENCION
    private Tabla tab_cab_retencion;
    private Tabla tab_det_retencion;

    //CLIENTE
    private Tabla tab_creacion_cliente;
    private Dialogo dia_creacion_cliente;

    //PRODUCTO
    private Tabla tab_creacion_producto;
    private Dialogo dia_creacion_producto;

    public FacturaCxC() {
        this.setWidth("95%");
        this.setHeight("90%");
        this.setTitle("GENERAR FACTURA DE VENTA");
        this.setResizable(false);
        this.setDynamic(false);

        tab_factura.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + (getAltoPanel() - 10) + "px;overflow: auto;display: block;");
        tab_factura.setId("tab_factura");
        tab_factura.setWidgetVar("w_factura");
        tab_factura.agregarTab("FACTURA ", null);//0 
        tab_factura.agregarTab("DETALLE PAGO", null);//1
        tab_factura.agregarTab("COMPROBANTE DE CONTABILIDAD", null);//2       
        tab_factura.agregarTab("COMPROBANTE DE RETENCIÓN", null);//3
        this.setDialogo(tab_factura);
        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);
        //Recupera porcentaje iva
        tarifaIVA = ser_configuracion.getPorcentajeIva();
        dia_creacion_cliente = new Dialogo();
        dia_creacion_cliente.setId("dia_creacion_cliente");
        dia_creacion_cliente.setTitle("CREAR CLIENTE");
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

    /**
     * Configuraciones para crear una factura
     */
    public void nuevaFactura() {
        opcion = 1;  // GENERA FACTURA
        tab_factura.getTab(0).getChildren().clear();
        tab_factura.getTab(1).getChildren().clear();
        tab_factura.getTab(2).getChildren().clear();
        tab_factura.getTab(3).getChildren().clear();

        tab_factura.getTab(0).getChildren().add(dibujarFactura());
        utilitario.getConexion().getSqlPantalla().clear();//LIMPIA SQL EXISTENTES
        ocultarTabs(); //Ocilta todas las tabas
        setActivarFactura(true); //activa solo tab de Fcatura de venta
        seleccionarTab(0);
        this.getBot_aceptar().setRendered(true);
        tab_cab_factura.limpiar();
        tab_cab_factura.insertar();
        tab_deta_factura.limpiar();
        ate_observacion.limpiar();
        ate_observacion.setDisabled(false);
        com_pto_emision.setDisabled(false);
        tex_iva.setValue("0,00");
        tex_subtotal0.setValue("0,00");
        tex_subtotal12.setValue("0,00");
        tex_total.setValue("0,00");
        cargarMaximoSecuencialFactura();

        //Activa click derecho insertar y eliminar
        try {
            PanelTabla pat_panel = (PanelTabla) tab_deta_factura.getParent();
            pat_panel.getMenuTabla().getItem_insertar().setDisabled(false);
            pat_panel.getMenuTabla().getItem_eliminar().setDisabled(false);
        } catch (Exception e) {
        }
    }

    /**
     * Configuraciones para ver una factura
     *
     * @param ide_cccfa
     */
    public void verFactura(String ide_cccfa) {
        if (ide_cccfa != null) {
            tab_factura.getTab(0).getChildren().clear();
            tab_factura.getTab(1).getChildren().clear();
            tab_factura.getTab(2).getChildren().clear();
            tab_factura.getTab(3).getChildren().clear();

            tab_factura.getTab(0).getChildren().add(dibujarFactura());

            opcion = 2;
            //Carga la Factura
            activarTabs();
            seleccionarTab(0);
            this.getBot_aceptar().setRendered(false);
            tab_cab_factura.setCondicion("ide_cccfa=" + ide_cccfa);
            tab_cab_factura.ejecutarSql();

            tab_factura.getTab(1).getChildren().add(dibujarDetallePago());
            if (tab_cab_factura.getValor("ide_cnccc") != null) {
                tab_factura.getTab(2).getChildren().add(dibujarAsientoVenta());
            }
            if (tab_cab_factura.getValor("ide_cncre") != null) {
                tab_factura.getTab(3).getChildren().add(dibujarRetencion());
            }

            com_pto_emision.setValue(tab_cab_factura.getValor("ide_ccdaf"));
            tab_deta_factura.setCondicion("ide_cccfa=" + tab_cab_factura.getValorSeleccionado());
            tab_deta_factura.ejecutarSql();
            tex_iva.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("valor_iva_cccfa")));
            //Carga totales y observacion
            double dou_subt0 = 0;
            double dou_subtno = 0;
            try {
                dou_subt0 = Double.parseDouble(tab_cab_factura.getValor("base_tarifa0_cccfa"));
            } catch (Exception e) {
            }
            try {
                dou_subtno = Double.parseDouble(tab_cab_factura.getValor("base_no_objeto_iva_cccfa"));
            } catch (Exception e) {
            }
            this.setTitle("FACTURA N. " + tab_cab_factura.getValor("secuencial_cccfa"));
            tex_subtotal0.setValue(utilitario.getFormatoNumero(dou_subt0 + dou_subtno));
            tex_subtotal12.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("base_grabada_cccfa")));
            tex_total.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("total_cccfa")));
            ate_observacion.setValue(tab_cab_factura.getValor("observacion_cccfa"));
            if (tab_cab_factura.getFilaSeleccionada() != null) {
                tab_cab_factura.getFilaSeleccionada().setLectura(true);
            }
            ate_observacion.setDisabled(true);
            com_pto_emision.setDisabled(true);
            //Desactiva click derecho insertar y eliminar
            try {
                PanelTabla pat_panel = (PanelTabla) tab_deta_factura.getParent();
                pat_panel.getMenuTabla().getItem_insertar().setDisabled(true);
                pat_panel.getMenuTabla().getItem_eliminar().setDisabled(true);
            } catch (Exception e) {
            }

            /**
             * DETALLE DE PAGO
             */
            verDetallePago(tab_cab_factura.getValorSeleccionado());

            /**
             * RETENCION DE VENTA
             */
        } else {
            utilitario.agregarMensajeInfo("Seleccionar Factura", "Debe seleccionar una factura");
        }
    }

    public void verDetallePago(String ide_cccfa) {
        tab_deta_pago.setSql(ser_factura.getSqlPagosFactura(ide_cccfa));
        tab_deta_pago.ejecutarSql();
    }

    private Grupo dibujarDetallePago() {
        Grupo grupo = new Grupo();
        tab_deta_pago = new Tabla();
        tab_deta_pago.setId("tab_deta_pago");
        tab_deta_pago.setRuta("pre_index.clase." + getId());
        tab_deta_pago.setIdCompleto("tab_factura:tab_deta_pago");
        tab_deta_pago.setSql(ser_factura.getSqlPagosFactura(tab_cab_factura.getValorSeleccionado()));
        tab_deta_pago.setCampoPrimaria("ide_ccdtr");
        tab_deta_pago.getColumna("ide_ccdtr").setVisible(false);
        tab_deta_pago.getColumna("ide_tecba").setVisible(false);
        tab_deta_pago.getColumna("docum_relac_ccdtr").setNombreVisual("N.DOCUMENTO RELACIONADO");
        tab_deta_pago.getColumna("nombre_tettb").setNombreVisual("TIPO TRANSACCION");
        tab_deta_pago.getColumna("valor_ccdtr").setNombreVisual("VALOR");
        tab_deta_pago.getColumna("fecha_trans_ccdtr").setNombreVisual("FECHA");
        tab_deta_pago.setRows(15);
        tab_deta_pago.setColumnaSuma("valor_ccdtr");
        tab_deta_pago.setLectura(true);
        tab_deta_pago.setEmptyMessage("No existen pagos realizados");
        tab_deta_pago.dibujar();

        PanelTabla tab_panel = new PanelTabla();
        tab_panel.setPanelTabla(tab_deta_pago);
        grupo.getChildren().add(tab_panel);
        return grupo;
    }

    /**
     * Configuraciones para Tab de Factura
     *
     * @return
     */
    private Grupo dibujarFactura() {
        //utilitario.getConexion().setImprimirSqlConsola(true);
        com_pto_emision = new Combo();
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        com_pto_emision.setMetodoRuta("pre_index.clase." + getId() + ".cargarMaximoSecuencialFactura");

        Grupo grupo = new Grupo();
        Grid gri_pto = new Grid();
        gri_pto.setColumns(9);
        gri_pto.getChildren().add(new Etiqueta("<strong>PUNTO DE EMISIÓN :</strong>"));
        gri_pto.getChildren().add(com_pto_emision);

        if (opcion == 1) {
            dia_creacion_producto.getGri_cuerpo().getChildren().clear();
            dia_creacion_cliente.getGri_cuerpo().getChildren().clear();
            gri_pto.getChildren().add(new Espacio("10", "1"));
            Boton botCrearCliente = new Boton();
            botCrearCliente.setId("botCrearCliente");
            botCrearCliente.setValue("Crear Cliente");
            botCrearCliente.setIcon("ui-icon-person");
            botCrearCliente.setMetodoRuta("pre_index.clase." + getId() + ".abrirCliente");
            gri_pto.getChildren().add(botCrearCliente);
            gri_pto.getChildren().add(new Espacio("5", "1"));

            Boton botCrearProducto = new Boton();
            botCrearProducto.setId("botCrearProducto");
            botCrearProducto.setValue("Crear Producto");
            botCrearProducto.setIcon("ui-icon-cart");
            botCrearProducto.setMetodoRuta("pre_index.clase." + getId() + ".abrirProducto");
            gri_pto.getChildren().add(botCrearProducto);

            dia_creacion_cliente.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardarCliente");
            dia_creacion_cliente.getBot_cancelar().setMetodoRuta("pre_index.clase." + getId() + ".cerrarDialogos");

            tab_creacion_cliente = new Tabla();
            tab_creacion_cliente.setId("tab_creacion_cliente");
            tab_creacion_cliente.setRuta("pre_index.clase." + getId());
            tab_creacion_cliente.setIdCompleto("tab_factura:tab_creacion_cliente");
            ser_cliente.configurarTablaCliente(tab_creacion_cliente);
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

            tab_creacion_cliente.getColumna("IDE_GETID").setNombreVisual("TIPO DE IDENTIFICACION");
            tab_creacion_cliente.getColumna("IDE_GETID").setOrden(1);
            tab_creacion_cliente.getColumna("IDENTIFICAC_GEPER").setNombreVisual("IDENTIFICACION");
            tab_creacion_cliente.getColumna("IDENTIFICAC_GEPER").setOrden(2);
            tab_creacion_cliente.getColumna("NOM_GEPER").setNombreVisual("NOMBRE");
            tab_creacion_cliente.getColumna("NOM_GEPER").setOrden(3);
            tab_creacion_cliente.getColumna("NOMBRE_COMPL_GEPER").setNombreVisual("NOMBRE COMERCIAL");
            tab_creacion_cliente.getColumna("NOMBRE_COMPL_GEPER").setOrden(4);
            tab_creacion_cliente.getColumna("IDE_CNTCO").setNombreVisual("TIPO DE CONTRIBUYENTE");
            tab_creacion_cliente.getColumna("IDE_CNTCO").setOrden(5);
            tab_creacion_cliente.getColumna("DIRECCION_GEPER").setNombreVisual("DIRECCION");
            tab_creacion_cliente.getColumna("DIRECCION_GEPER").setOrden(6);
            tab_creacion_cliente.getColumna("DIRECCION_GEPER").setRequerida(true);
            tab_creacion_cliente.getColumna("TELEFONO_GEPER").setNombreVisual("TELEFONO");
            tab_creacion_cliente.getColumna("TELEFONO_GEPER").setOrden(7);
            tab_creacion_cliente.getColumna("TELEFONO_GEPER").setRequerida(true);
            tab_creacion_cliente.getColumna("CONTACTO_GEPER").setNombreVisual("CONTACTO");
            tab_creacion_cliente.getColumna("CONTACTO_GEPER").setOrden(8);
            tab_creacion_cliente.getColumna("MOVIL_GEPER").setNombreVisual("CELULAR");
            tab_creacion_cliente.getColumna("MOVIL_GEPER").setOrden(9);
            tab_creacion_cliente.getColumna("CORREO_GEPER").setNombreVisual("E-MAIL");
            tab_creacion_cliente.getColumna("CORREO_GEPER").setOrden(10);
            tab_creacion_cliente.getColumna("OBSERVACION_GEPER").setNombreVisual("OBSERVACION");
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
            tab_creacion_producto.setIdCompleto("tab_factura:tab_creacion_producto");
            ser_producto.configurarTablaProducto(tab_creacion_producto);
            tab_creacion_producto.setTabla("inv_articulo", "ide_inarti", 999);
            tab_creacion_producto.setCondicion("ide_inarti=-1");
            tab_creacion_producto.setMostrarNumeroRegistros(false);
            tab_creacion_producto.getColumna("INV_IDE_INARTI").setRequerida(true);
            tab_creacion_producto.getColumna("INV_IDE_INARTI").setNombreVisual("GRUPO");
            tab_creacion_producto.getColumna("IDE_INARTI").setVisible(false);
            tab_creacion_producto.getColumna("IDE_INFAB").setVisible(false);
            tab_creacion_producto.getColumna("IDE_INEPR").setVisible(false);
            tab_creacion_producto.getColumna("ES_COMBO_INARTI").setVisible(false);
            tab_creacion_producto.getColumna("IDE_GEORG").setVisible(false);
            tab_creacion_producto.getColumna("IDE_GEORG").setVisible(false);
            tab_creacion_producto.getColumna("nivel_inarti").setVisible(false);
            tab_creacion_producto.getColumna("NOMBRE_INARTI").setNombreVisual("NOMBRE");
            tab_creacion_producto.getColumna("NOMBRE_INARTI").setOrden(1);
            tab_creacion_producto.getColumna("CODIGO_INARTI").setNombreVisual("CODIGO");
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
            tab_creacion_producto.getColumna("OBSERVACION_INARTI").setNombreVisual("OBSERVACION");
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

        tab_cab_factura = new Tabla();
        tab_deta_factura = new Tabla();
        tab_cab_factura.setRuta("pre_index.clase." + getId());
        tab_cab_factura.setId("tab_cab_factura");
        tab_cab_factura.setIdCompleto("tab_factura:tab_cab_factura");
        tab_cab_factura.setTabla("cxc_cabece_factura", "ide_cccfa", 999);
        tab_cab_factura.setMostrarNumeroRegistros(false);
        tab_cab_factura.getColumna("ide_cnccc").setVisible(false);
        tab_cab_factura.getColumna("ide_cccfa").setVisible(false);
        tab_cab_factura.getColumna("ide_cncre").setVisible(false);
        tab_cab_factura.getColumna("ide_vgven").setVisible(false);

        tab_cab_factura.getColumna("telefono_cccfa").setNombreVisual("TELEFONO");
        tab_cab_factura.getColumna("telefono_cccfa").setOrden(5);
        tab_cab_factura.getColumna("ide_cntdo").setVisible(false);
        tab_cab_factura.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));
        tab_cab_factura.getColumna("ide_ccefa").setVisible(false);
        tab_cab_factura.getColumna("ide_ccefa").setValorDefecto(utilitario.getVariable("p_cxc_estado_factura_normal"));
        tab_cab_factura.getColumna("ide_geubi").setVisible(false);
        tab_cab_factura.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_factura.getColumna("ide_usua").setVisible(false);
        tab_cab_factura.getColumna("fecha_trans_cccfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_factura.getColumna("fecha_trans_cccfa").setVisible(false);
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setOrden(2);
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA EMISION");
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setRequerida(true);
        tab_cab_factura.getColumna("ide_ccdaf").setVisible(false);
        tab_cab_factura.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        tab_cab_factura.getColumna("ide_geper").setAutoCompletar();
        tab_cab_factura.getColumna("ide_geper").setOrden(3);
        tab_cab_factura.getColumna("ide_geper").setRequerida(true);
        tab_cab_factura.getColumna("ide_geper").setMetodoChangeRuta(tab_cab_factura.getRuta() + ".seleccionarCliente");
        tab_cab_factura.getColumna("ide_geper").setNombreVisual("CLIENTE");
        tab_cab_factura.getColumna("pagado_cccfa").setValorDefecto("false");
        tab_cab_factura.getColumna("pagado_cccfa").setVisible(false);
        tab_cab_factura.getColumna("total_cccfa").setEtiqueta();
        tab_cab_factura.getColumna("total_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura.getColumna("total_cccfa").setValorDefecto("0");
        tab_cab_factura.getColumna("secuencial_cccfa").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cab_factura.getColumna("secuencial_cccfa").setLongitud(10);
        tab_cab_factura.getColumna("secuencial_cccfa").setOrden(1);
        tab_cab_factura.getColumna("secuencial_cccfa").setRequerida(true);
        tab_cab_factura.getColumna("secuencial_cccfa").setNombreVisual("SECUENCIAL");
        tab_cab_factura.getColumna("secuencial_cccfa").setMascara("999999999");
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
        tab_cab_factura.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp!=" + utilitario.getVariable("p_con_for_pag_reembolso_caja"));
        tab_cab_factura.getColumna("ide_cndfp").setOrden(4);
        tab_cab_factura.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
        tab_cab_factura.getColumna("ide_cndfp").setEstilo("width:140px");
        tab_cab_factura.getColumna("ide_cndfp").setRequerida(true);
        tab_cab_factura.getColumna("TARIFA_IVA_CCCFA").setVisible(false);
        tab_cab_factura.setCondicionSucursal(false);
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setOrden(6);
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setNombreVisual("DIRECCIÓN");
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setRequerida(true);
        tab_cab_factura.getColumna("OBSERVACION_CCCFA").setVisible(false);
        //tab_cab_factura.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
        tab_cab_factura.getColumna("solo_guardar_cccfa").setVisible(false);
        tab_cab_factura.getColumna("ide_geubi").setVisible(false);
        tab_cab_factura.getColumna("ide_usua").setVisible(false);
        tab_cab_factura.setTipoFormulario(true);
        tab_cab_factura.getGrid().setColumns(6);
        //tab_cab_factura.agregarRelacion(tab_deta_factura);
        tab_cab_factura.setCondicion("ide_cccfa=-1");
        tab_cab_factura.getColumna("base_grabada_cccfa").setVisible(false);
        tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setVisible(false);
        tab_cab_factura.getColumna("valor_iva_cccfa").setVisible(false);
        tab_cab_factura.getColumna("base_tarifa0_cccfa").setVisible(false);
        tab_cab_factura.getColumna("total_cccfa").setVisible(false);
        tab_cab_factura.setRecuperarLectura(true);
        tab_cab_factura.dibujar();

        tab_deta_factura.setId("tab_deta_factura");
        tab_deta_factura.setIdCompleto("tab_factura:tab_deta_factura");
        tab_deta_factura.setRuta("pre_index.clase." + getId());
        tab_deta_factura.setTabla("cxc_deta_factura", "ide_ccdfa", 999);
        tab_deta_factura.getColumna("ide_ccdfa").setVisible(false);
        tab_deta_factura.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_deta_factura.getColumna("ide_inarti").setAutoCompletar();
        tab_deta_factura.getColumna("ide_inarti").setNombreVisual("ARTICULO");
        tab_deta_factura.getColumna("ide_inarti").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".seleccionarProducto");
        tab_deta_factura.getColumna("ide_inarti").setRequerida(true);
        tab_deta_factura.getColumna("ide_cccfa").setVisible(false);
        tab_deta_factura.getColumna("SECUENCIAL_CCDFA").setVisible(false);
        tab_deta_factura.getColumna("ide_inarti").setOrden(1);
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setNombreVisual("CANTIDAD");
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setOrden(2);
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setRequerida(true);
        tab_deta_factura.getColumna("PRECIO_CCDFA").setNombreVisual("PRECIO");
        tab_deta_factura.getColumna("PRECIO_CCDFA").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("PRECIO_CCDFA").setOrden(3);
        tab_deta_factura.getColumna("PRECIO_CCDFA").setRequerida(true);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setCombo(ser_producto.getListaTipoIVA());
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setPermitirNullCombo(false);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setOrden(4);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setNombreVisual("IVA");
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setLongitud(-1);
        tab_deta_factura.getColumna("total_ccdfa").setNombreVisual("TOTAL");
        tab_deta_factura.getColumna("total_ccdfa").setOrden(5);
        tab_deta_factura.getColumna("OBSERVACION_CCDFA").setNombreVisual("OBSERVACION");
        tab_deta_factura.getColumna("total_ccdfa").setEtiqueta();
        tab_deta_factura.getColumna("total_ccdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_deta_factura.getColumna("total_ccdfa").alinearDerecha();
        tab_deta_factura.getColumna("precio_promedio_ccdfa").setLectura(true);
        tab_deta_factura.getColumna("ALTERNO_CCDFA").setValorDefecto("00");
        tab_deta_factura.setScrollable(true);
        tab_deta_factura.setScrollHeight(getAltoPanel() - 320);
        tab_deta_factura.setRecuperarLectura(true);
        tab_deta_factura.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_factura);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");
        pat_panel.getMenuTabla().getItem_eliminar().setValueExpression("rendered", "true");
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);
        grupo.getChildren().add(tab_cab_factura);
        grupo.getChildren().add(pat_panel);

        Grid gri_total = new Grid();
        gri_total.setWidth("100%");
        gri_total.setStyle("width:" + (getAnchoPanel() - 10) + "px;border:1px");
        gri_total.setColumns(2);

        Grid gri_observa = new Grid();
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong> <span style='color:red;font-weight: bold;'> *</span>"));
        ate_observacion.setCols(80);
        gri_observa.getChildren().add(ate_observacion);
        gri_total.getChildren().add(gri_observa);

        Grid gri_valores = new Grid();
        gri_valores.setId("gri_valores");
        gri_valores.setColumns(4);
        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTOTAL TARIFA " + (utilitario.getFormatoNumero(tarifaIVA * 100)) + "% :<s/trong>"));
        tex_subtotal12.setDisabled(true);
        tex_subtotal12.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_subtotal12);

        gri_valores.getChildren().add(new Etiqueta("<strong>IVA " + (utilitario.getFormatoNumero(tarifaIVA * 100)) + "% :<s/trong>"));
        tex_iva.setDisabled(true);
        tex_iva.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_iva);

        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTOTAL TARIFA 0% :<s/trong>"));
        tex_subtotal0.setDisabled(true);
        tex_subtotal0.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_subtotal0);

        gri_valores.getChildren().add(new Etiqueta("<strong>TOTAL :<s/trong>"));
        tex_total.setDisabled(true);
        tex_total.setStyle("font-size: 14px;font-weight: bold;text-align: right;width:110px");

        gri_valores.getChildren().add(tex_total);

        gri_total.getChildren().add(gri_valores);
        grupo.getChildren().add(gri_total);
        return grupo;
    }

    private Grupo dibujarAsientoVenta() {
        Grupo grupo = new Grupo();
        tab_cab_conta = new Tabla();
        tab_deta_conta = new Tabla();
        tab_cab_conta.setRuta("pre_index.clase." + getId());
        tab_cab_conta.setId("tab_cab_conta");
        tab_cab_conta.setIdCompleto("tab_documenoCxP:tab_cab_conta");
        if (tab_cab_factura.getValor("ide_cnccc") != null) {
            tab_cab_conta.setSql(ser_comp_contabilidad.getSqlCabeceraAsiento(tab_cab_factura.getValor("ide_cnccc")));
        } else {
            tab_cab_conta.setSql(ser_comp_contabilidad.getSqlCabeceraAsiento("-1"));
        }

        tab_cab_conta.getColumna("ide_cnccc").setNombreVisual("TRANSACCIÓN");
        tab_cab_conta.getColumna("ide_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("numero_cnccc").setEtiqueta();
        tab_cab_conta.getColumna("ide_cnccc").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
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
        tab_deta_conta.setColumnaSuma("debe,haber");
        tab_deta_conta.getColumna("debe").alinearDerecha();
        tab_deta_conta.getColumna("debe").setLongitud(25);
        tab_deta_conta.getColumna("haber").alinearDerecha();
        tab_deta_conta.getColumna("haber").setLongitud(25);
        tab_deta_conta.getColumna("OBSERVACION_CNDCC").setNombreVisual("OBSERVACIÓN");
        tab_deta_conta.setScrollable(true);
        tab_deta_conta.setScrollHeight(getAltoPanel() - 240); //300
        tab_deta_conta.setScrollWidth(getAnchoPanel() - 15);
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
        if (tab_cab_factura.getValor("ide_cnccc") != null) {
            gri_observa.getChildren().add(new Etiqueta("<table style='padding-left:10px;'><tr><td><strong>USUARIO CREADOR :</strong></td><td>" + tab_cab_conta.getValor("nom_usua") + " </td></tr><td><strong>FECHA SISTEMA :</strong></td><td>" + utilitario.getFormatoFecha(tab_cab_conta.getValor("fecha_siste_cnccc")) + " </td><tr> </tr><td><strong>HORA SISTEMA :</strong></td><td>" + utilitario.getFormatoHora(tab_cab_conta.getValor("hora_sistem_cnccc")) + " </td><tr> </tr></table>"));
        }
        grupo.getChildren().add(gri_observa);
        return grupo;
    }

    private Grupo dibujarRetencion() {
        Grupo grupo = new Grupo();
        tab_cab_retencion = new Tabla();
        tab_det_retencion = new Tabla();
        tab_cab_retencion.setId("tab_cab_retencion");
        tab_cab_retencion.setRuta("pre_index.clase." + getId());
        tab_cab_retencion.setIdCompleto("tab_factura:tab_cab_retencion");
        tab_cab_retencion.setTabla("con_cabece_retenc", "ide_cncre", -1);
        tab_cab_retencion.agregarRelacion(tab_det_retencion);
        tab_cab_retencion.getColumna("ide_cnccc").setVisible(false);
        tab_cab_retencion.getColumna("ide_cnere").setVisible(false);
        tab_cab_retencion.getColumna("ide_cnere").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_rete_normal"));
        tab_cab_retencion.getColumna("es_venta_cncre").setValorDefecto("true");
        tab_cab_retencion.getColumna("es_venta_cncre").setVisible(false);
        tab_cab_retencion.getColumna("numero_cncre").setMascara("999-999-99999999");
        tab_cab_retencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(false);
        tab_cab_retencion.getColumna("autorizacion_cncre").setMascara("9999999999");
        tab_cab_retencion.getColumna("autorizacion_cncre").setQuitarCaracteresEspeciales(true);
        tab_cab_retencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(true);
        tab_cab_retencion.setCondicion("ide_cncre=-1");
        tab_cab_retencion.setTipoFormulario(true);
        tab_cab_retencion.getGrid().setColumns(6);
        tab_cab_retencion.setMostrarNumeroRegistros(false);
        tab_cab_retencion.dibujar();
        tab_cab_retencion.insertar();

        tab_det_retencion.setId("tab_det_retencion");
        tab_det_retencion.setRuta("pre_index.clase." + getId());
        tab_det_retencion.setIdCompleto("tab_factura:tab_det_retencion");
        tab_det_retencion.setTabla("con_detall_retenc", "ide_cndre", -1);
        tab_det_retencion.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
        tab_det_retencion.getColumna("valor_cndre").setLectura(true);
        tab_det_retencion.setRows(5);
        tab_det_retencion.setColumnaSuma("valor_cndre");
        tab_det_retencion.setCondicion("ide_cndre=-1");
        tab_det_retencion.dibujar();

        grupo.getChildren().add(tab_cab_retencion);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_det_retencion);
        grupo.getChildren().add(tab_cab_retencion);
        grupo.getChildren().add(pat_panel);
        return grupo;
    }

    /**
     * Carga el seccuencial maximo de un punto de emision
     */
    public void cargarMaximoSecuencialFactura() {

        if (com_pto_emision.getValue() != null) {
            String secuencial = ser_factura.getSecuencialFactura(String.valueOf(com_pto_emision.getValue())) + "";
            String ceros = utilitario.generarCero(9 - secuencial.length());
            String num_max = ceros.concat(secuencial);
            tab_cab_factura.setValor("secuencial_cccfa", num_max);
            utilitario.addUpdateTabla(tab_cab_factura, "secuencial_cccfa", "");
        } else {
            tab_cab_factura.setValor("secuencial_cccfa", "");
            utilitario.addUpdateTabla(tab_cab_factura, "secuencial_cccfa", "");
        }
    }

    /**
     * Carga la dirección cuando se selecciona un cliente del Autocompletar
     *
     * @param evt
     */
    public void seleccionarCliente(SelectEvent evt) {
        tab_cab_factura.modificar(evt);
        if (tab_cab_factura.getValor("ide_geper") != null) {
            setCliente(tab_cab_factura.getValor("ide_geper"));
        }
    }

    /**
     * Asigna el cliente de la factura
     *
     * @param ide_geper
     */
    public void setCliente(String ide_geper) {
        if (ide_geper != null) {
            TablaGenerica tag_cliente = ser_cliente.getCliente(ide_geper);
            if (tag_cliente.isEmpty() == false) {
                tab_cab_factura.setValor("ide_geper", ide_geper);
                tab_cab_factura.setValor("direccion_cccfa", tag_cliente.getValor("direccion_geper"));
                tab_cab_factura.setValor("telefono_cccfa", tag_cliente.getValor("telefono_geper"));
                utilitario.addUpdateTabla(tab_cab_factura, "direccion_cccfa,telefono_cccfa", "");
            }
        }
    }

    /**
     * Asigna observacion a la factura
     *
     * @param observacion
     */
    public void setObservacion(String observacion) {
        ate_observacion.setValue(observacion);
    }

    /**
     * Carga el precio de la ultima venta del producto al cliente seleccionado
     *
     * @param evt
     */
    public void seleccionarProducto(SelectEvent evt) {
        tab_deta_factura.modificar(evt);
        if (tab_deta_factura.getValor("ide_inarti") != null) {
            tab_deta_factura.setValor("precio_ccdfa", String.valueOf(ser_producto.getUltimoPrecioProductoCliente(tab_cab_factura.getValor("ide_geper"), tab_deta_factura.getValor("ide_inarti"))));
            //Carga la configuracion de iva del producto seleccionado
            tab_deta_factura.setValor("iva_inarti_ccdfa", String.valueOf(ser_producto.getIvaProducto(tab_deta_factura.getValor("ide_inarti"))));
            //Carga configuracion del producto que compra el cliente 
            String str_aux_iva = ser_producto.getUltimoIvaProductoCliente(tab_cab_factura.getValor("ide_geper"), tab_deta_factura.getValor("ide_inarti"));
            if (str_aux_iva != null && str_aux_iva.isEmpty() == false) {
                //Carga el valor del iva del producto para el cliente seleccionado
                tab_deta_factura.setValor("iva_inarti_ccdfa", str_aux_iva);
            }
        } else {
            tab_deta_factura.setValor("precio_ccdfa", "0,00");
        }
        utilitario.addUpdateTabla(tab_deta_factura, "precio_ccdfa,iva_inarti_ccdfa", "");
        calcularTotalDetalleFactura();

    }

    /**
     * Se ejecuta cuando cambia el Precio o la Cantidad de un detalle de la
     * factura
     *
     * @param evt
     */
    public void cambioPrecioCantidadIva(AjaxBehaviorEvent evt) {
        tab_deta_factura.modificar(evt);
        calcularTotalDetalleFactura();
    }

    /**
     * Calcula el valor total de cada detalle de la factura
     */
    private void calcularTotalDetalleFactura() {
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        try {
            cantidad = Double.parseDouble(tab_deta_factura.getValor("cantidad_ccdfa"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_deta_factura.getValor("precio_ccdfa"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_deta_factura.setValor("total_ccdfa", utilitario.getFormatoNumero(total));
        utilitario.addUpdateTabla(tab_deta_factura, "total_ccdfa", "");
        calcularTotalFactura();
    }

    /**
     * Calcula totales de la factura
     */
    public void calcularTotalFactura() {
        double base_grabada = 0;
        double base_no_objeto = 0;
        double base_tarifa0 = 0;
        double valor_iva = 0;
        double porcentaje_iva = 0;

        for (int i = 0; i < tab_deta_factura.getTotalFilas(); i++) {
            String iva = tab_deta_factura.getValor(i, "iva_inarti_ccdfa");
            if (iva.equals("1")) { //SI IVA
                base_grabada = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_grabada;
                porcentaje_iva = tarifaIVA;
                valor_iva = base_grabada * porcentaje_iva; //0.12
            } else if (iva.equals("-1")) { // NO IVA
                base_tarifa0 = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_tarifa0;
            } else if (iva.equals("0")) { // NO OBJETO
                base_no_objeto = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_no_objeto;
            }
        }
        tab_cab_factura.setValor("base_grabada_cccfa", utilitario.getFormatoNumero(base_grabada));
        tab_cab_factura.setValor("base_no_objeto_iva_cccfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_cab_factura.setValor("valor_iva_cccfa", utilitario.getFormatoNumero(valor_iva));
        tab_cab_factura.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_cab_factura.setValor("total_cccfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));

        tex_subtotal12.setValue(utilitario.getFormatoNumero(base_grabada));
        tex_subtotal0.setValue(utilitario.getFormatoNumero(base_no_objeto + base_tarifa0));
        tex_iva.setValue(utilitario.getFormatoNumero(valor_iva));
        tex_total.setValue(utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));

        utilitario.addUpdate("tab_factura:0:gri_valores");
    }

    /**
     * Inserta en los detalles de Factura y Comprobante de Venta
     */
    public void insertar() {
        if (tab_deta_factura.isFocus()) {
            if (tab_cab_factura.getValor("ide_geper") != null) {
                tab_deta_factura.insertar();
            } else {
                utilitario.agregarMensajeInfo("Seleccione Cliente", "Debe seleccionar un cliente para realizar la factura");
            }
        } else if (tab_deta_conta.isFocus()) {
            //Valida que haya valores en la factura
            if (tab_deta_factura.isEmpty() == false) {
                tab_deta_conta.insertar();
            } else {
                utilitario.agregarMensajeInfo("Crear Factura", "Debe ingresar detalles a la Factura");
            }
        }
    }

    /**
     * Elimina en los detalles de Factura y Comprobante de Venta
     */
    public void eliminar() {
        try {
            if (tab_deta_factura.isFocus()) {
                tab_deta_factura.eliminar();
                calcularTotalFactura();
            }
        } catch (Exception e) {
        }

    }

    /**
     * Acepta la Factura
     */
    public void guardar() {

        if (opcion == 1) {//CREA FACTURA            
            if (tabActiva == 0) {  //TAB ACTIVA  FACTURA
                //Asigna punto de emision seleccionado y si solo guarda la factura
                tab_cab_factura.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
                tab_cab_factura.setValor("OBSERVACION_CCCFA", String.valueOf(ate_observacion.getValue()));
                tab_cab_factura.setValor("tarifa_iva_cccfa", utilitario.getFormatoNumero(tarifaIVA));
                //valida la factura
                if (validarFactura()) {
                    generarFactura();
                }
            }
        }
    }

    /**
     * Guarda la Factura
     */
    public void generarFactura() {
        //Siempre solo guarda la factura       
        tab_cab_factura.setValor("solo_guardar_cccfa", "true");
        //SOLO GUARDA LA FACTURA
        if (tab_cab_factura.guardar()) {
            String ide_cccfa = tab_cab_factura.getValor("ide_cccfa");
            for (int i = 0; i < tab_deta_factura.getTotalFilas(); i++) {
                tab_deta_factura.setValor(i, "ide_cccfa", ide_cccfa);
            }
            if (tab_deta_factura.guardar()) {
                //Guarda la cuenta por cobrar
                ser_factura.generarTransaccionFactura(tab_cab_factura);
                //Transaccion de Inventario
                ser_inventario.generarComprobnateTransaccionVenta(tab_cab_factura, tab_deta_factura);
                String ide_srcom = null; //IDE COMPROBANTE ELECTRONICO
                //SI ESTA ACTIVA FACTURACION ELECTRONICA
                if (utilitario.getVariable("p_sri_activa_comp_elect") != null && utilitario.getVariable("p_sri_activa_comp_elect").equalsIgnoreCase("true")) {
                    ide_srcom = ser_factura.guardarComprobanteElectronicoFactura(tab_cab_factura, tab_deta_factura);
                    if (ide_srcom == null) {
                        return;
                    }
                }
                if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                    if ((utilitario.getVariable("p_sri_activa_comp_elect").equalsIgnoreCase("true")) && ide_srcom != null) { //*******SI ESTA ACTIVADO ONLINE DE COMPROBANTES ELECTRONICOS
                        facturacionElectronica(ide_srcom);
                    }
                    this.cerrar();
                }
            }
        }
    }

    public void guardarProducto() {
        if (true) { //!!!!!!!!******Validar Datos Producto
            if (tab_creacion_producto.guardar()) {
                //Respalda insertadas para que no guarde
                List<String> lis_resp_cab = tab_cab_factura.getInsertadas();
                List<String> lis_resp_deta = tab_deta_factura.getInsertadas();
                if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                    tab_deta_factura.actualizarCombos();
                    tab_deta_factura.insertar();
                    lis_resp_deta.add(tab_deta_factura.getFilaSeleccionada().getRowKey());
                    tab_deta_factura.setValor("ide_inarti", tab_creacion_producto.getValor("ide_inarti"));
                    tab_deta_factura.setValor("iva_inarti_ccdfa", tab_creacion_producto.getValor("IVA_INARTI"));
                    utilitario.addUpdate("tab_factura:tab_deta_factura");
                    tab_creacion_producto.limpiar();
                    //tab_creacion_producto.insertar();
                    dia_creacion_producto.cerrar();
                }
                tab_cab_factura.setInsertadas(lis_resp_cab);
                tab_deta_factura.setInsertadas(lis_resp_deta);
            }
        }
    }

    public void guardarCliente() {
        if (ser_cliente.validarCliente(tab_creacion_cliente)) {
            if (tab_creacion_cliente.guardar()) {
                //Respalda insertadas para que no guarde
                List<String> lis_resp_cab = tab_cab_factura.getInsertadas();
                List<String> lis_resp_deta = tab_deta_factura.getInsertadas();

                if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                    //Se guardo correctamente
                    tab_cab_factura.actualizarCombos();
                    tab_cab_factura.setValor("ide_geper", tab_creacion_cliente.getValor("ide_geper"));
                    tab_cab_factura.setValor("direccion_cccfa", tab_creacion_cliente.getValor("direccion_geper"));
                    tab_cab_factura.setValor("telefono_cccfa", tab_creacion_cliente.getValor("telefono_geper"));
                    utilitario.addUpdateTabla(tab_cab_factura, "direccion_cccfa,telefono_cccfa,ide_geper", "");
                    tab_creacion_cliente.limpiar();
                    //tab_creacion_cliente.insertar();
                    dia_creacion_cliente.cerrar();
                }
                tab_cab_factura.setInsertadas(lis_resp_cab);
                tab_deta_factura.setInsertadas(lis_resp_deta);
            }
        }
    }

    /**
     * Envia la Factura Electronica al SRI
     *
     * @param ide_srcom
     */
    private void facturacionElectronica(String ide_srcom) {
        if (utilitario.getVariable("p_sri_activa_comp_elect").equalsIgnoreCase("true")) {
            ser_comprobante.generarComprobanteElectronico(ide_srcom);
            TablaGenerica tab_comprobante = ser_comprobante.getComprobante(ide_srcom);
            TablaGenerica tab_xml_sri = ser_comprobante.getXmlComprobante(ide_srcom);
            if (tab_xml_sri.isEmpty() == false) {
                String mensje = "<p> FACTURA NRO. " + tab_cab_factura.getValor("secuencial_cccfa");
                mensje += "</br>AMBIENTE : <strong>" + (utilitario.getVariable("p_sri_ambiente_comp_elect").equals("1") ? "PRUEBAS" : "PRODUCCIÓN") + "</strong>";  //********variable ambiente facturacion electronica
                mensje += "</br>CLAVE DE ACCESO : <span style='font-size:12px;'>" + tab_comprobante.getValor("claveacceso_srcom") + "</span></p>";
                mensje += "<p>ESTADO : <strong>" + ser_comprobante.getEstadoComprobante(tab_xml_sri.getValor("ide_sresc")) + "</strong></p>";

                if (tab_xml_sri.getValor("ide_sresc").equals("1")) { //**********RECIBIDA
                    mensje += "<p>Solo se envió al Servicio Web de Recepción de Comprobantes Electrónicos.</p>";
                    men_factura.setMensajeAdvertencia("FACTURACIÓN ELECTRÓNICA", mensje);
                } else if (tab_xml_sri.getValor("ide_sresc").equals("2")) { //**********DEVUELTA
                    mensje += "<p>" + tab_xml_sri.getValor("msg_recepcion_srxmc") + "</p>";
                    men_factura.setMensajeError("FACTURACIÓN ELECTRÓNICA", mensje);
                } else if (tab_xml_sri.getValor("ide_sresc").equals("3")) { //**********AUTORIZADO
                    mensje += "<p>NÚMERO DE AUTORIZACION : " + tab_comprobante.getValor("autorizacion_srcom");
                    mensje += "</br>FECHA DE AUTORIZACION :" + utilitario.getFormatoFechaHora(tab_comprobante.getValor("fechaautoriza_srcom")) + " </p>";

                    men_factura.setMensajeExito("FACTURACIÓN ELECTRÓNICA", mensje);
                } else if (tab_xml_sri.getValor("ide_sresc").equals("4")) { //**********RECHAZADO
                    mensje += "<p>" + tab_xml_sri.getValor("msg_recepcion_srxmc") + "</p>";
                    men_factura.setMensajeError("FACTURACIÓN ELECTRÓNICA", mensje);
                } else if (tab_xml_sri.getValor("ide_sresc").equals("5")) { //**********CONTINGENCIA
                    mensje += "<p>No se pudo enviar el comprobante a los Servicios Web de SRI.</p>";
                    men_factura.setMensajeAdvertencia("FACTURACIÓN ELECTRÓNICA", mensje);
                } else if (tab_xml_sri.getValor("ide_sresc").equals("6")) { //**********NO AUTORIZADO
                    mensje += "<p>" + tab_xml_sri.getValor("msg_autoriza_srxmc") + "</p>";
                    men_factura.setMensajeError("FACTURACIÓN ELECTRÓNICA", mensje);
                }

            } else {
                men_factura.setMensajeError("FACTURACIÓN ELECTRÓNICA", "No existe el comprobante eléctronico Nro. " + ide_srcom);
            }
            men_factura.dibujar();
        }
    }

    /**
     * Selecciona una Tab Mediante Javascript
     *
     * @param index
     */
    public void seleccionarTab(int index) {
        tabActiva = index;
        String str_script_activa = "w_factura.select(" + index + ");";
        tab_factura.setActiveIndex(index);
        for (int i = 0; i < tab_factura.getChildren().size(); i++) {
            str_script_activa += tab_factura.getTab(i).isDisabled() == false ? "w_factura.enable(" + i + ");" : "w_factura.disable(" + i + ");";
        }
        utilitario.ejecutarJavaScript(str_script_activa);
    }

    /**
     * Desactiva todas las tabs
     */
    private void descativarTabs() {
        for (int i = 0; i < tab_factura.getChildren().size(); i++) {
            tab_factura.getTab(i).setDisabled(true);
        }
    }

    /**
     * Oculta todas las tabs
     */
    private void ocultarTabs() {
        for (int i = 0; i < tab_factura.getChildren().size(); i++) {
            tab_factura.getTab(i).setRendered(false);
        }
    }

    /**
     * Activa todas las tabs
     */
    private void activarTabs() {
        for (int i = 0; i < tab_factura.getChildren().size(); i++) {
            tab_factura.getTab(i).setRendered(true);
            tab_factura.getTab(i).setDisabled(false);
        }
    }

    /**
     * Validaciones para guardar o modificar una factura
     *
     * @return
     */
    private boolean validarFactura() {

        if (com_pto_emision.getValue() == null) {
            utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe seleccionar un Punto de Emisión");
            return false;
        }

        if (tab_cab_factura.getValor("ide_geper") == null || tab_cab_factura.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe seleccionar un Cliente");
            return false;
        }
        if (tab_cab_factura.getValor("secuencial_cccfa") == null || tab_cab_factura.getValor("secuencial_cccfa").isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe ingresar el Secuencial de la factura");
            return false;
        }
        if (tab_cab_factura.getValor("observacion_cccfa") == null || tab_cab_factura.getValor("observacion_cccfa").isEmpty()) {
            utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe ingresar una Observacion");
            return false;
        }

        if (tab_deta_factura.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe ingresar detalles a la factura");
            return false;
        } else {
            for (int i = 0; i < tab_deta_factura.getTotalFilas(); i++) {
                if (tab_deta_factura.getValor(i, "cantidad_ccdfa") == null || tab_deta_factura.getValor("cantidad_ccdfa").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe ingresar la cantidad en los Detalles de la factura ");
                    return false;
                }
                if (tab_deta_factura.getValor(i, "precio_ccdfa") == null || tab_deta_factura.getValor("precio_ccdfa").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe ingresar el precio en los Detalles de la factura ");
                    return false;
                }
                if (tab_deta_factura.getValor(i, "alterno_ccdfa") == null || tab_deta_factura.getValor("alterno_ccdfa").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe ingresar un alterno en los Detalles de la factura ");
                    return false;
                }
            }
        }
        double dou_total = 0;
        try {
            dou_total = Double.parseDouble(tab_cab_factura.getValor("total_cccfa"));
        } catch (Exception e) {
        }
        if (dou_total <= 0) {
            utilitario.agregarMensajeError("No se puede guardar la Factura", "El total de la factura debe ser mayor a 0");
            return false;
        }

        return true;
    }

    @Override
    public void cerrar() {
        //Activa las tab
        descativarTabs();
        super.cerrar(); //To change body of generated methods, choose Tools | Templates.
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
        tab_creacion_producto.limpiar();
        tab_creacion_producto.insertar();
        dia_creacion_producto.dibujar();
    }

    public void abrirCliente() {
        tab_creacion_cliente.limpiar();
        tab_creacion_cliente.insertar();
        dia_creacion_cliente.dibujar();
    }

    public Tabla getTab_cab_factura() {
        return tab_cab_factura;
    }

    public void setTab_cab_factura(Tabla tab_cab_factura) {
        this.tab_cab_factura = tab_cab_factura;
    }

    public Tabla getTab_deta_factura() {
        return tab_deta_factura;
    }

    public void setTab_deta_factura(Tabla tab_deta_factura) {
        this.tab_deta_factura = tab_deta_factura;
    }

    public boolean isActivarFactura() {
        return tab_factura.getTab(0).isDisabled();
    }

    public void setActivarFactura(boolean activarAsientoCosto) {
        tab_factura.getTab(0).setRendered(activarAsientoCosto);
        tab_factura.getTab(0).setDisabled(!activarAsientoCosto);
    }

    public boolean isActivarDetallePago() {
        return tab_factura.getTab(1).isDisabled();
    }

    public void setActivarDetallePago(boolean activarAsientoCosto) {
        tab_factura.getTab(1).setDisabled(!activarAsientoCosto);
    }

    public boolean isActivarAsientoVenta() {
        return tab_factura.getTab(2).isDisabled();
    }

    public void setActivarAsientoVenta(boolean activarAsientoVenta) {
        tab_factura.getTab(2).setDisabled(!activarAsientoVenta);
    }

    public boolean isActivarRetencion() {
        return tab_factura.getTab(3).isDisabled();
    }

    public void setActivarRetencion(boolean activarRetencion) {
        tab_factura.getTab(3).setDisabled(!activarRetencion);
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

    public Tabla getTab_deta_pago() {
        return tab_deta_pago;
    }

    public void setTab_deta_pago(Tabla tab_deta_pago) {
        this.tab_deta_pago = tab_deta_pago;
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

    public Combo getComboPuntoEmision() {
        return com_pto_emision;
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

    public Dialogo getDia_creacion_producto() {
        return dia_creacion_producto;
    }

    public void setDia_creacion_producto(Dialogo dia_creacion_producto) {
        this.dia_creacion_producto = dia_creacion_producto;
    }

    public Tabla getTab_creacion_producto() {
        return tab_creacion_producto;
    }

    public void setTab_creacion_producto(Tabla tab_creacion_producto) {
        this.tab_creacion_producto = tab_creacion_producto;
    }

}
