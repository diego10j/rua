/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Mascara;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_modifica_factura extends Pantalla {

    private final AreaTexto ate_observacion = new AreaTexto();
    private final Texto tex_subtotal12 = new Texto();
    private final Texto tex_subtotal0 = new Texto();
    private final Texto tex_iva = new Texto();
    private final Texto tex_total = new Texto();
    private final Combo com_pto_emision = new Combo();
    private double tarifaIVA = 0;
    private final Mascara mas_num_factua = new Mascara();

    private Tabla tab_cab_factura = new Tabla();
    private Tabla tab_deta_factura = new Tabla();

    private Tabla tab_inf_adi = new Tabla();

    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);
    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioComprobanteElectronico ser_comp_electronico = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);

    private boolean haceKardex = false;

    private String ide_sresc;
    private String fecha_emision_original;
    private boolean cambioFecha = false;

    public pre_modifica_factura() {

        tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        //bar_botones.agregarReporte();         
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmisionFacturas());
        com_pto_emision.eliminarVacio();
        bar_botones.agregarComponente(new Etiqueta("FACTURACIÓN:"));
        bar_botones.agregarComponente(com_pto_emision);
        bar_botones.agregarComponente(new Etiqueta("SECUENCIAL:"));

        mas_num_factua.setMask("999999999");
        bar_botones.agregarComponente(mas_num_factua);
        Boton bot_consultar = new Boton();
        bot_consultar.setMetodo("buscarFactura");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        Grid grupo = new Grid();
        tab_cab_factura = new Tabla();
        tab_deta_factura = new Tabla();
        tab_cab_factura.setId("tab_cab_factura");
        tab_cab_factura.agregarRelacion(tab_deta_factura);
        tab_cab_factura.setTabla("cxc_cabece_factura", "ide_cccfa", 1);
        tab_cab_factura.setMostrarNumeroRegistros(false);
        tab_cab_factura.getColumna("ide_cnccc").setVisible(true);
        tab_cab_factura.getColumna("ide_cccfa").setVisible(false);
        tab_cab_factura.getColumna("ide_cncre").setVisible(false);

        tab_cab_factura.getColumna("ret_fuente_cccfa").setVisible(false);
        tab_cab_factura.getColumna("ret_iva_cccfa").setVisible(false);

        tab_cab_factura.getColumna("dias_credito_cccfa").setVisible(false);
        tab_cab_factura.getColumna("ide_vgven").setVisible(true);
        tab_cab_factura.getColumna("ide_vgven").setCombo("ven_vendedor", "ide_vgven", "nombre_vgven", "");
        tab_cab_factura.getColumna("ide_cndfp1").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp!=3");
        tab_cab_factura.getColumna("ide_cndfp1").setEstilo("width:140px");
        tab_cab_factura.getColumna("ide_cndfp1").setRequerida(true);

        tab_cab_factura.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_factura.getColumna("ide_usua").setVisible(false);
        tab_cab_factura.getColumna("fecha_trans_cccfa").setVisible(false);
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setRequerida(true);
        tab_cab_factura.getColumna("ide_ccdaf").setVisible(false);
        tab_cab_factura.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        tab_cab_factura.getColumna("ide_geper").setAutoCompletar();
        tab_cab_factura.getColumna("ide_geper").setRequerida(true);
        tab_cab_factura.getColumna("ide_geper").setMetodoChange("seleccionarCliente");
        tab_cab_factura.getColumna("pagado_cccfa").setVisible(false);
        tab_cab_factura.getColumna("total_cccfa").setEtiqueta();
        tab_cab_factura.getColumna("total_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
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
        tab_cab_factura.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp=3");
        tab_cab_factura.getColumna("ide_cndfp").setEstilo("width:140px");
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setMetodoChange("cambioFecha");
        tab_cab_factura.getColumna("ide_cndfp").setRequerida(true);
        tab_cab_factura.getColumna("TARIFA_IVA_CCCFA").setVisible(false);
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setRequerida(true);
        tab_cab_factura.getColumna("OBSERVACION_CCCFA").setVisible(false);
        tab_cab_factura.getColumna("solo_guardar_cccfa").setVisible(false);
        tab_cab_factura.getColumna("ide_geubi").setVisible(false);
        tab_cab_factura.getColumna("ide_usua").setVisible(false);
        tab_cab_factura.setTipoFormulario(true);
        tab_cab_factura.getGrid().setColumns(6);
        tab_cab_factura.setCondicion("ide_cccfa=-1");
        tab_cab_factura.getColumna("base_grabada_cccfa").setVisible(false);
        tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setVisible(false);
        tab_cab_factura.getColumna("valor_iva_cccfa").setVisible(false);
        tab_cab_factura.getColumna("base_tarifa0_cccfa").setVisible(false);
        tab_cab_factura.getColumna("total_cccfa").setVisible(false);
        tab_cab_factura.dibujar();
        tab_cab_factura.insertar();
        tab_cab_factura.getFilaSeleccionada().setLectura(true);

        tab_deta_factura.setId("tab_deta_factura");
        tab_deta_factura.setTabla("cxc_deta_factura", "ide_ccdfa", 2);
        tab_deta_factura.setCampoForanea("ide_cccfa");
        tab_deta_factura.getColumna("ide_ccdfa").setVisible(false);
        tab_deta_factura.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_deta_factura.getColumna("ide_inarti").setAutoCompletar();
        tab_deta_factura.getColumna("ide_inarti").setMetodoChange("seleccionarProducto");
        tab_deta_factura.getColumna("ide_inarti").setRequerida(true);
        tab_deta_factura.getColumna("ide_cccfa").setVisible(false);
        tab_deta_factura.getColumna("SECUENCIAL_CCDFA").setVisible(false);
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setMetodoChange("cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setRequerida(true);
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setDecimales(ser_factura.getDecimalesCantidad()); //DFJ
        tab_deta_factura.getColumna("PRECIO_CCDFA").setMetodoChange("cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("PRECIO_CCDFA").setRequerida(true);
        tab_deta_factura.getColumna("PRECIO_CCDFA").setDecimales(ser_factura.getDecimalesPrecioUnitario()); //DFJ
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setCombo(ser_producto.getListaTipoIVA());
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setPermitirNullCombo(false);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setLongitud(-1);
        tab_deta_factura.getColumna("ide_inarti").setRequerida(true);
        tab_deta_factura.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_deta_factura.getColumna("ide_inuni").setOrden(1);
        tab_deta_factura.getColumna("ide_inuni").setNombreVisual("UNIDAD");
        tab_deta_factura.getColumna("ide_inuni").setLongitud(-1);
        tab_deta_factura.getColumna("total_ccdfa").setNombreVisual("TOTAL");
        tab_deta_factura.getColumna("OBSERVACION_CCDFA").setNombreVisual("OBSERVACION");
        tab_deta_factura.getColumna("total_ccdfa").setEtiqueta();
        tab_deta_factura.getColumna("total_ccdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_deta_factura.getColumna("total_ccdfa").alinearDerecha();
        tab_deta_factura.getColumna("precio_promedio_ccdfa").setLectura(true);
        tab_deta_factura.getColumna("ALTERNO_CCDFA").setValorDefecto("00");
        tab_deta_factura.getColumna("ALTERNO_CCDFA").setVisible(false);

        tab_deta_factura.getColumna("descuento_ccdfa").setNombreVisual("DESCUENTO");
        tab_deta_factura.getColumna("descuento_ccdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("descuento_ccdfa").setRequerida(true);
        tab_deta_factura.getColumna("descuento_ccdfa").setValorDefecto(utilitario.getFormatoNumero("0"));

        tab_deta_factura.setScrollable(true);
        tab_deta_factura.setScrollHeight(utilitario.getAltoPantalla() - 350);
        tab_deta_factura.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_cab_factura);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(true);

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_factura);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(true);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(true);
        grupo.getChildren().add(pat_panel1);
        grupo.getChildren().add(pat_panel);

        Grid gri_total = new Grid();
        gri_total.setWidth("100%");
        gri_total.setColumns(2);

        Grid gri_observa = new Grid();
        gri_observa.setId("gri_observa");
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
        Division d = new Division();
        d.dividir1(grupo);
        agregarComponente(d);

        tab_inf_adi.setId("tab_inf_adi");
        tab_inf_adi.setHeader("INFORMACIÓN ADICIONAL DE LA FACTURA");
        tab_inf_adi.setTabla("sri_info_adicional", "ide_srina", 3);
        tab_inf_adi.setCondicion("ide_cccfa=-1");
        tab_inf_adi.getColumna("ide_cccfa").setVisible(false);
        tab_inf_adi.getColumna("ide_srina").setVisible(false);
        tab_inf_adi.getColumna("ide_srcom").setVisible(false);
        tab_inf_adi.getColumna("nombre_srina").setNombreVisual("NOMBRE");
        tab_inf_adi.getColumna("valor_srina").setNombreVisual("VALOR");
        tab_inf_adi.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_inf_adi);
        pat_panel2.getMenuTabla().getItem_insertar().setRendered(true);
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(true);
        grupo.getChildren().add(pat_panel2);
    }

    public void cambioFecha(AjaxBehaviorEvent evt) {
        cambioFecha();
    }

    public void cambioFecha(DateSelectEvent evt) {
        cambioFecha();
    }

    public void cambioFecha() {
        cambioFecha = false;
        if (!ide_sresc.equals(String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()))) {
            utilitario.agregarMensajeError("No se puede Cambiar la fecha de emisión", "El comprobante debe setar en estado PENDIENTE");
            tab_cab_factura.setValor("fecha_emisi_cccfa", fecha_emision_original);
            utilitario.addUpdateTabla(tab_cab_factura, "fecha_emisi_cccfa", "");
        } else {
            cambioFecha = true;
        }
    }

    public void buscarFactura() {

        if (mas_num_factua.getValue() != null || mas_num_factua.getValue().toString().isEmpty() == false) {
            tab_cab_factura.setCondicion("ide_ccdaf=" + com_pto_emision.getValue() + " and secuencial_cccfa='" + mas_num_factua.getValue() + "' and ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal"));//no anuladas
            tab_cab_factura.ejecutarSql();

            if (tab_cab_factura.isEmpty()) {
                tab_cab_factura.insertar();
                tab_cab_factura.getFilaSeleccionada().setLectura(true);
                utilitario.agregarMensajeInfo("No existe la Factura N. " + mas_num_factua.getValue(), "");
                return;
            }
            setObservacion(tab_cab_factura.getValor("OBSERVACION_CCCFA"));
            tab_deta_factura.ejecutarValorForanea(tab_cab_factura.getValorSeleccionado());

            tab_inf_adi.setCondicion("ide_cccfa=" + tab_cab_factura.getValorSeleccionado());
            tab_inf_adi.ejecutarSql();

            haceKardex = false;
            if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
                //si es factura electronica valida que este en pendiente
                String ide_srcom = tab_cab_factura.getValor("ide_srcom");
                if (ide_srcom != null) {
                    TablaGenerica tag_e = utilitario.consultar(ser_comp_electronico.getSqlComprobanteElectronico(ide_srcom));
                    if (tag_e.isEmpty() == false) {
                        //solo puede modificar si no esta RECIBIDA O AUTORIZADA
                        ide_sresc = tag_e.getValor("ide_sresc");
                        fecha_emision_original = tag_e.getValor("fechaemision_srcom");

                        if (ide_sresc == null) {
                            tab_cab_factura.setValor("ide_sresc", String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()));
                            ide_sresc = String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo());
                        }
                        if (ide_sresc.equals(String.valueOf(EstadoComprobanteEnum.RECIBIDA.getCodigo())) || ide_sresc.equals(String.valueOf(EstadoComprobanteEnum.AUTORIZADO.getCodigo()))) {
                            utilitario.agregarMensajeError("No se puede modificar la Factura", "La Factura N. " + mas_num_factua.getValue() + " ya se a enviado al SRI");
                            //Hace de lectura
                            tab_cab_factura.getFilaSeleccionada().setLectura(true);
                            for (int i = 0; i < tab_deta_factura.getTotalFilas(); i++) {
                                tab_deta_factura.getFila(i).setLectura(true);
                            }
                        }
                    }
                }
            }

            tarifaIVA = ser_configuracion.getPorcentajeIva(tab_cab_factura.getValor("fecha_emisi_cccfa"));
            calcularTotalFactura();
        } else {
            utilitario.agregarMensajeInfo("Debe ingresar el número secuencial de la Factura", "");
            tab_cab_factura.insertar();
            tab_cab_factura.getFilaSeleccionada().setLectura(true);
            setObservacion("");
            calcularTotalFactura();
        }
    }

    @Override
    public void insertar() {
        if (tab_deta_factura.isFocus()) {
            if (tab_cab_factura.getValor("ide_geper") != null) {
                tab_deta_factura.insertar();
            } else {
                utilitario.agregarMensajeInfo("Seleccione Cliente", "Debe seleccionar un cliente para realizar la factura");
            }
        } else if (tab_inf_adi.isFocus()) {
            if (tab_cab_factura.getValor("ide_cccfa") != null) {
                tab_inf_adi.insertar();
                tab_inf_adi.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            }
        }
    }

    @Override
    public void guardar() {
        //Asigna punto de emision seleccionado y si solo guarda la factura
        tab_cab_factura.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
        tab_cab_factura.setValor("OBSERVACION_CCCFA", String.valueOf(ate_observacion.getValue()));
        tab_cab_factura.setValor("tarifa_iva_cccfa", utilitario.getFormatoNumero((tarifaIVA * 100)));
        tab_cab_factura.setValor("dias_credito_cccfa", String.valueOf(ser_factura.getDiasCreditoFormaPago(tab_cab_factura.getValor("ide_cndfp1"))));
        //valida la factura
        if (validarFactura()) {
            //SOLO GUARDA LA FACTURA
            tab_cab_factura.modificar(tab_cab_factura.getFilaActual());
            if (tab_cab_factura.guardar()) {
                if (tab_deta_factura.guardar()) {
                    tab_inf_adi.setValor("ide_srcom", tab_cab_factura.getValor("ide_srcom"));
                    tab_inf_adi.guardar();
                    //Guarda la cuenta por cobrar
                    ser_factura.generarModificarTransaccionFactura(tab_cab_factura);
                    //Transaccion de Inventario
                    for (int i = 0; i < tab_deta_factura.getTotalFilas(); i++) {
                        if (haceKardex == false) {
                            if (ser_inventario.isHaceKardex(tab_deta_factura.getValor(i, "ide_inarti"))) {
                                haceKardex = true;
                            }
                        }
                    }
                    if (haceKardex) {
                        ser_inventario.generarModificarComprobnateTransaccionVenta(tab_cab_factura, tab_deta_factura);
                    }
                    if (cambioFecha && ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
                        //Se debe generar nueva clave de acceso
                        ser_factura.generarNuevaClaveAcceso(tab_cab_factura.getValor("ide_srcom"));
                    }
                    //Modifica los datos del cliente
                    TablaGenerica tag_clie = new TablaGenerica();
                    tag_clie.setTabla("gen_persona", "ide_geper");
                    tag_clie.setCondicion("ide_geper=" + tab_cab_factura.getValor("ide_geper"));
                    tag_clie.ejecutarSql();
                    tag_clie.modificar(tag_clie.getFilaActual());
                    tag_clie.setValor("direccion_geper", tab_cab_factura.getValor("direccion_cccfa"));
                    tag_clie.setValor("telefono_geper", tab_cab_factura.getValor("telefono_cccfa"));
                    tag_clie.setValor("correo_geper", tab_cab_factura.getValor("correo_cccfa"));
                    tag_clie.guardar();

                    if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
                            String ide_cccfa = tab_cab_factura.getValor("ide_cccfa");
                            ser_comp_electronico.generarFacturaElectronica(ide_cccfa);
                        }

                        fecha_emision_original = null;
                        cambioFecha = false;
                    }
                }
            }
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
    public void eliminar() {
        if (tab_deta_factura.isFocus()) {
            tab_deta_factura.eliminar();
            calcularTotalFactura();
        } else if (tab_inf_adi.isFocus()) {
            tab_inf_adi.eliminar();
        }
    }

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
                tab_cab_factura.setValor("correo_cccfa", tag_cliente.getValor("correo_geper"));
                utilitario.addUpdateTabla(tab_cab_factura, "direccion_cccfa,telefono_cccfa,correo_cccfa", "");
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
        utilitario.addUpdate("gri_observa");
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
        double descuento = 0;
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
        try {
            descuento = Double.parseDouble(tab_deta_factura.getValor("descuento_ccdfa"));
        } catch (Exception e) {
            descuento = 0;
        }

        total = (cantidad * precio) - descuento;
        tab_deta_factura.setValor("total_ccdfa", utilitario.getFormatoNumero(utilitario.getFormatoNumero(total, 4)));
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
        double descuento = 0;
        for (int i = 0; i < tab_deta_factura.getTotalFilas(); i++) {
            String iva = tab_deta_factura.getValor(i, "iva_inarti_ccdfa");
            double des = 0;
            if (iva.equals("1")) { //SI IVA
                base_grabada = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_grabada;
                porcentaje_iva = tarifaIVA;
                valor_iva = base_grabada * porcentaje_iva; //0.12
            } else if (iva.equals("-1")) { // NO IVA
                base_tarifa0 = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_tarifa0;
            } else if (iva.equals("0")) { // NO OBJETO
                base_no_objeto = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_no_objeto;
            }
            try {
                des = Double.parseDouble(tab_deta_factura.getValor(i, "descuento_ccdfa"));
            } catch (Exception e) {
            }
            descuento += des;
        }

        tab_cab_factura.setValor("descuento_cccfa", utilitario.getFormatoNumero(descuento));
        utilitario.addUpdateTabla(tab_cab_factura, "descuento_cccfa", null);
        tab_cab_factura.setValor("base_grabada_cccfa", utilitario.getFormatoNumero(base_grabada));
        tab_cab_factura.setValor("base_no_objeto_iva_cccfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_cab_factura.setValor("valor_iva_cccfa", utilitario.getFormatoNumero(valor_iva));
        tab_cab_factura.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_cab_factura.setValor("total_cccfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));

        tex_subtotal12.setValue(utilitario.getFormatoNumero(base_grabada));
        tex_subtotal0.setValue(utilitario.getFormatoNumero(base_no_objeto + base_tarifa0));
        tex_iva.setValue(utilitario.getFormatoNumero(valor_iva));
        tex_total.setValue(utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));

        utilitario.addUpdate("gri_valores");
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

    public Tabla getTab_inf_adi() {
        return tab_inf_adi;
    }

    public void setTab_inf_adi(Tabla tab_inf_adi) {
        this.tab_inf_adi = tab_inf_adi;
    }

}
