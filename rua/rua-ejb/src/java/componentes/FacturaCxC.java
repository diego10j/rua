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
import framework.componentes.Mensaje;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import java.util.ArrayList;
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
    private final Combo com_pto_emision = new Combo();
    private double tarifaIVA = 0;

    //FORMA DE PAGO
    private Tabla tab_deta_pago = new Tabla();

    //CONTABILIDAD Asiento de Venta
    @EJB
    private final ServicioComprobanteContabilidad ser_comp_contabilidad = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);
    private Tabla tab_cab_conta = new Tabla();
    private Tabla tab_deta_conta = new Tabla();
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
    private Tabla tab_cab_retencion = new Tabla();
    private Tabla tab_det_retencion = new Tabla();

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
        tab_factura.agregarTab("ASIENTO CONTABLE", null);//2       
        tab_factura.agregarTab("RETENCIÓN", null);//3
        this.setDialogo(tab_factura);
        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);
        //Recupera porcentaje iva
        tarifaIVA = ser_configuracion.getPorcentajeIva();
    }

    public void setFacturaCxC(String titulo) {
//        tab_factura.getTab(0).getChildren().clear();
//        tab_factura.getTab(1).getChildren().clear();
//        tab_factura.getTab(2).getChildren().clear();
//        tab_factura.getTab(3).getChildren().clear();
        this.setTitle(titulo);
//        tab_factura.getTab(0).getChildren().add(dibujarFactura());
//        tab_factura.getTab(1).getChildren().add(dibujarDetallePago());
//        tab_factura.getTab(2).getChildren().add(dibujarAsientoVenta());
//        tab_factura.getTab(3).getChildren().add(dibujarRetencion());
        this.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardar");
    }

    /**
     * Configuraciones para crear una factura
     */
    public void nuevaFactura() {

        tab_factura.getTab(0).getChildren().clear();
        tab_factura.getTab(1).getChildren().clear();
        tab_factura.getTab(2).getChildren().clear();
        tab_factura.getTab(3).getChildren().clear();

        tab_factura.getTab(0).getChildren().add(dibujarFactura());

        opcion = 1;  // GENERA FACTURA
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
            tab_factura.getTab(1).getChildren().add(dibujarDetallePago());
            tab_factura.getTab(2).getChildren().add(dibujarAsientoVenta());
            tab_factura.getTab(3).getChildren().add(dibujarRetencion());

            opcion = 2;
            //Carga la Factura
            activarTabs();
            seleccionarTab(0);
            this.getBot_aceptar().setRendered(false);
            tab_cab_factura.setCondicion("ide_cccfa=" + ide_cccfa);
            tab_cab_factura.ejecutarSql();
            com_pto_emision.setValue(tab_cab_factura.getValor("ide_ccdaf"));
            tab_deta_factura.setCondicion("ide_cccfa="+tab_cab_factura.getValorSeleccionado());
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
            tex_subtotal0.setValue(utilitario.getFormatoNumero(dou_subt0 + dou_subtno));
            tex_subtotal12.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("base_grabada_cccfa")));
            tex_total.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("total_cccfa")));
            ate_observacion.setValue(tab_cab_factura.getValor("observacion_cccfa "));
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
             * COMPROBANTE DE VENTA
             */
            verComprobanteVenta(tab_cab_factura.getValor("ide_cnccc"));

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

    /**
     * Visualiza un comprobante de venta , No se puede editar
     *
     * @param ide_cnccc
     */
    public void verComprobanteVenta(String ide_cnccc) {
        tab_cab_conta.setCondicion("ide_cnccc=" + ide_cnccc);
        tab_cab_conta.ejecutarSql();
        if (tab_cab_conta.isEmpty()) {
            tab_cab_conta.insertar();
        }

        tab_deta_conta.setSql(ser_comp_contabilidad.getSqlDetalleAsiento(tab_cab_conta.getValorSeleccionado()));
        tab_deta_conta.ejecutarSql();
        ate_observacion_conta.setValue(tab_cab_conta.getValor("observacion_cnccc "));
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
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        com_pto_emision.setMetodoRuta("pre_index.clase." + getId() + ".cargarMaximoSecuencialFactura");
        com_pto_emision.eliminarVacio();

        Grupo grupo = new Grupo();
        Grid gri_pto = new Grid();
        gri_pto.setColumns(4);
        gri_pto.getChildren().add(new Etiqueta("<strong>PUNTO DE EMISIÓN :</strong>"));
        gri_pto.getChildren().add(com_pto_emision);

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
        tab_cab_factura.getColumna("ide_vgven").setCombo("ven_vendedor", "ide_vgven", "nombre_vgven", "");
        tab_cab_factura.getColumna("ide_vgven").setNombreVisual("VENDEDOR");
        tab_cab_factura.getColumna("ide_vgven").setOrden(5);
        tab_cab_factura.getColumna("ide_vgven").setEstilo("width:140px");
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
        tab_cab_factura.getColumna("total_cccfa").setValorDefecto("0,00");
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
        tab_cab_factura.getColumna("ide_cndfp").setPermitirNullCombo(false);
        tab_cab_factura.getColumna("ide_cndfp").setOrden(4);
        tab_cab_factura.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
        tab_cab_factura.getColumna("ide_cndfp").setEstilo("width:140px");
        tab_cab_factura.getColumna("ide_cndfp").setRequerida(true);
        tab_cab_factura.setCondicionSucursal(false);
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setOrden(6);
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setNombreVisual("DIRECCIÓN");
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setRequerida(true);
        tab_cab_factura.getColumna("OBSERVACION_CCCFA").setVisible(false);
        tab_cab_factura.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
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
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setCombo(lista);
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
        tab_deta_factura.setScrollHeight(150);
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
        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTAL TARIFA 12% :<s/trong>"));
        tex_subtotal12.setDisabled(true);
        tex_subtotal12.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_subtotal12);

        gri_valores.getChildren().add(new Etiqueta("<strong>IVA " + (utilitario.getFormatoNumero(tarifaIVA * 100)) + "% :<s/trong>"));
        tex_iva.setDisabled(true);
        tex_iva.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_iva);

        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTAL TARIFA 0% :<s/trong>"));
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
        tab_cab_conta.setIdCompleto("tab_factura:tab_cab_conta");
        tab_cab_conta.setTabla("con_cab_comp_cont", "ide_cnccc", -1);
        tab_cab_conta.setCondicion("ide_cnccc=-1");
        tab_cab_conta.getColumna("ide_cnccc").setNombreVisual("TRANSACCIÓN");
        tab_cab_conta.getColumna("fecha_siste_cnccc").setVisible(false);
        tab_cab_conta.getColumna("numero_cnccc").setLectura(true);
        tab_cab_conta.getColumna("numero_cnccc").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cab_conta.getColumna("numero_cnccc").setNombreVisual("NUM. COMPROBANTE");
        tab_cab_conta.getColumna("numero_cnccc").setOrden(5);
        tab_cab_conta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_cab_conta.getColumna("fecha_trans_cnccc").setOrden(1);
        tab_cab_conta.getColumna("fecha_trans_cnccc").setRequerida(true);
        tab_cab_conta.getColumna("hora_sistem_cnccc").setVisible(false);
        tab_cab_conta.getColumna("ide_usua").setVisible(false);
        tab_cab_conta.getColumna("ide_modu").setCombo("sis_modulo", "ide_modu", "nom_modu", "");
        tab_cab_conta.getColumna("ide_modu").setLectura(true);
        tab_cab_conta.getColumna("ide_modu").setNombreVisual("MÓDULO");
        tab_cab_conta.getColumna("ide_modu").setOrden(4);
        tab_cab_conta.getColumna("ide_modu").setEstilo("width:150px");
        tab_cab_conta.getColumna("ide_geper").setCombo(tab_cab_factura.getColumna("ide_geper").getListaCombo());
        tab_cab_conta.getColumna("ide_geper").setAutoCompletar();
        tab_cab_conta.getColumna("ide_geper").setLectura(true);
        tab_cab_conta.getColumna("ide_geper").setNombreVisual("BENEFICIARIO");
        tab_cab_conta.getColumna("ide_geper").setOrden(2);
        tab_cab_conta.getColumna("ide_geper").setRequerida(true);
        tab_cab_conta.getColumna("ide_cneco").setVisible(false);
        tab_cab_conta.getColumna("ide_cntcm").setLectura(true);
        tab_cab_conta.getColumna("ide_cntcm").setCombo("con_tipo_comproba", "ide_cntcm", "nombre_cntcm", "");
        tab_cab_conta.getColumna("ide_cntcm").setNombreVisual("TIPO COMPROBANTE");
        tab_cab_conta.getColumna("ide_cntcm").setEstilo("width:100px");
        tab_cab_conta.getColumna("ide_cntcm").setOrden(3);
        tab_cab_conta.getColumna("OBSERVACION_CNCCC").setVisible(false);
        tab_cab_conta.setTipoFormulario(true);
        tab_cab_conta.getGrid().setColumns(6);
        tab_cab_conta.setMostrarNumeroRegistros(false);
        tab_cab_conta.setLectura(true);
        tab_cab_conta.dibujar();
        tab_cab_conta.setLectura(false);

        tab_deta_conta.setRuta("pre_index.clase." + getId());
        tab_deta_conta.setId("tab_deta_conta");
        tab_deta_conta.setIdCompleto("tab_factura:tab_deta_conta");
        tab_deta_conta.setSql(ser_comp_contabilidad.getSqlDetalleAsiento("-1"));
        tab_deta_conta.getColumna("ide_cndcc").setVisible(false);
        tab_deta_conta.getColumna("codig_recur_cndpc").setNombreVisual("CODIGO CUENTA");
        tab_deta_conta.getColumna("nombre_cndpc").setNombreVisual("CUENTA");
        tab_deta_conta.getColumna("observacion_cndcc").setNombreVisual("observacion");
        tab_deta_conta.setColumnaSuma("debe,haber");
        tab_deta_conta.getColumna("debe").alinearDerecha();
        tab_deta_conta.getColumna("haber").alinearDerecha();
        tab_deta_conta.getColumna("OBSERVACION_CNDCC").setNombreVisual("OBSERVACION");
        tab_deta_conta.setScrollable(true);
        tab_deta_conta.setScrollHeight(220);
        tab_deta_conta.setLectura(true);
        tab_deta_conta.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_conta);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");

        grupo.getChildren().add(tab_cab_conta);
        grupo.getChildren().add(pat_panel);

        Grid gri_observa = new Grid();
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong>"));
        ate_observacion_conta.setCols(120);
        ate_observacion_conta.setDisabled(true);
        gri_observa.getChildren().add(ate_observacion_conta);
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
            TablaGenerica tag_cliente = ser_cliente.getCliente(tab_cab_factura.getValor("ide_geper"));
            if (tag_cliente.isEmpty() == false) {
                tab_cab_factura.setValor("direccion_cccfa", tag_cliente.getValor("direccion_geper"));
                utilitario.addUpdateTabla(tab_cab_factura, "direccion_cccfa", "");
            }
        }
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
    private void calcularTotalFactura() {
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
        if (tab_deta_factura.isFocus()) {
            tab_deta_factura.eliminar();
            calcularTotalFactura();
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
                    utilitario.agregarMensajeError("No se puede guardar la Factura", "Debe ingresar un alterno");
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void cerrar() {
        //Activa las tab
        descativarTabs();
        super.cerrar(); //To change body of generated methods, choose Tools | Templates.
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

}
