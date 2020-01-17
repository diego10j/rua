/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import componentes.AsientoContable;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.Mensaje;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.panel.Panel;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_liquidacion_comp_elec extends Pantalla {

    @EJB
    private final ServicioCuentasCxP ser_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioComprobanteElectronico ser_comprobante_electronico = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);
    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);

    private final Combo com_pto_emision = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();

    private Tabla tab_tabla1;
    private Tabla tab_tabla2;
    private double tarifaIVA = 0;

    private final VisualizarPDF vpd_nota_ride = new VisualizarPDF();
    private final Mensaje men_factura = new Mensaje();
    private Confirmar con_confirma = new Confirmar();

    private String ide_cpcfa = null;

    private final Grid gri_dashboard = new Grid();

    private final Dialogo dia_correo = new Dialogo();
    private final Texto tex_correo = new Texto();

    private AsientoContable asc_asiento = new AsientoContable();

    private final MenuPanel mep_menu = new MenuPanel();

    public pre_liquidacion_comp_elec() {
        tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());
        bar_botones.quitarBotonsNavegacion();
        com_pto_emision.setId("com_pto_emision");
        com_pto_emision.setCombo(ser_cxp.getSqlPuntosEmisionLiquidacionCompras());
        com_pto_emision.setMetodo("actualizarLista");
        com_pto_emision.eliminarVacio();
        bar_botones.agregarComponente(new Etiqueta("PUNTO DE EMISIÓN:"));
        bar_botones.agregarComponente(com_pto_emision);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarLista");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        vpd_nota_ride.setId("vpd_nota_ride");
        vpd_nota_ride.setTitle("RIDE");
        utilitario.getPantalla().getChildren().add(vpd_nota_ride);

        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular la Liquidación de Compra ?");
        con_confirma.setTitle("ANULAR LIQUIDACIÓN DE COMPRA");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        dia_correo.setId("dia_correo");
        dia_correo.setTitle("REENVIAR LIQUIDACIÓN DE COMPRA");
        dia_correo.setWidth("35%");
        dia_correo.setHeight("17%");
        dia_correo.getBot_aceptar().setMetodo("aceptarReenviar");
        tex_correo.setStyle("width:" + (dia_correo.getAnchoPanel() - 35) + "px");
        Grid gri = new Grid();
        gri.setStyle("width:" + (dia_correo.getAnchoPanel() - 5) + "px; height:" + dia_correo.getAltoPanel() + "px;overflow:auto;display:block;vertical-align:middle;");
        gri.getChildren().add(new Etiqueta("<strong> CORREO ELECTRÓNICO: </strong>"));
        gri.getChildren().add(tex_correo);
        dia_correo.setDialogo(gri);
        agregarComponente(dia_correo);

        mep_menu.setMenuPanel("OPCIONES LIQUIDACIÓN DE COMPRAS", "20%");

        mep_menu.agregarItem("Listado de Liquidaciones", "dibujarNotaCredito", "ui-icon-note");  //2
        // mep_menu.agregarItem("Generar Asiento Contable", "dibujarNotaCreditoNoContabilizadas", "ui-icon-notice"); //3
        //mep_menu.agregarItem("Notas de Crédito Anuladas", "dibujarNotaCreditoAnuladas", "ui-icon-cancel"); //4
        //mep_menu.agregarSubMenu("CONTABILIDAD");
        //mep_menu.agregarItem("Generar Asiento Contable", "dibujarNotasNoContabilizadas", "ui-icon-notice");

        agregarComponente(mep_menu);
        dibujarLiquidacionCompra();

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(asc_asiento);
    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla1.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    private void dibujarDashboard() {
        int num_pendientes = 0;
        int num_recibidas = 0;
        int num_rechazadas = 0;
        int num_devueltas = 0;
        int num_autorizadas = 0;
        int num_no_autorizadas = 0;
        TablaGenerica tg = utilitario.consultar(ser_comprobante_electronico.getSqlTotalComprobantesPorEstado(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS, com_pto_emision.getValue() + ""));
        if (tg.isEmpty() == false) {
            for (int i = 0; i < tg.getTotalFilas(); i++) {
                if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()))) {
                    num_pendientes = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.RECIBIDA.getCodigo()))) {
                    num_recibidas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.DEVUELTA.getCodigo()))) {
                    num_devueltas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.RECHAZADO.getCodigo()))) {
                    num_rechazadas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.AUTORIZADO.getCodigo()))) {
                    num_autorizadas = Integer.parseInt(tg.getValor(i, "contador"));
                } else if (tg.getValor(i, "ide_sresc").equals(String.valueOf(EstadoComprobanteEnum.NOAUTORIZADO.getCodigo()))) {
                    num_no_autorizadas = Integer.parseInt(tg.getValor(i, "contador"));
                }
            }
        }

        gri_dashboard.getChildren().clear();
        Panel p1 = new Panel();
        p1.setStyle("margin-left: 2px;");
        Grid g1 = new Grid();
        g1.setWidth("100%");
        g1.setColumns(2);
        g1.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-navy'>PENDIENTES </span>"));
        Link l1 = new Link();
        l1.setMetodo("filtrarPendientes");
        l1.getChildren().add(new Etiqueta("<i class='fa fa-clock-o fa-4x text-navy'></i>"));
        g1.getChildren().add(l1);
        g1.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_pendientes + "</span>"));
        p1.getChildren().add(g1);
        gri_dashboard.getChildren().add(p1);

        Panel p2 = new Panel();
        p2.setStyle("margin-left: 2px;");
        Grid g2 = new Grid();
        g2.setColumns(2);
        g2.setWidth("100%");
        g2.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-blue'>RECIBIDAS </span>"));
        Link l2 = new Link();
        l2.setMetodo("filtrarRecibidas");
        l2.getChildren().add(new Etiqueta("<i class='fa fa-cloud-upload fa-4x text-blue'></i>"));
        g2.getChildren().add(l2);
        g2.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_recibidas + "</span>"));
        p2.getChildren().add(g2);
        gri_dashboard.getChildren().add(p2);

        Panel p3 = new Panel();
        p3.setStyle("margin-left: 2px;");
        Grid g3 = new Grid();
        g3.setWidth("100%");
        g3.setColumns(2);
        g3.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-orange'>DEVUELTAS </span>"));
        Link l3 = new Link();
        l3.setMetodo("filtrarDevueltas");
        l3.getChildren().add(new Etiqueta("<i class='fa fa-arrow-circle-left fa-4x text-orange'></i>"));
        g3.getChildren().add(l3);
        g3.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_devueltas + "</span>"));
        p3.getChildren().add(g3);

        gri_dashboard.getChildren().add(p3);
        Panel p4 = new Panel();
        p4.setStyle("margin-left: 2px;");
        Grid g4 = new Grid();
        g4.setColumns(2);
        g4.setWidth("100%");
        g4.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-red'>RECHAZADAS </span>"));
        Link l4 = new Link();
        l4.setMetodo("filtrarRechazadas");
        l4.getChildren().add(new Etiqueta("<i class='fa fa-times-circle fa-4x text-red'></i>"));
        g4.getChildren().add(l4);
        g4.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_rechazadas + "</span>"));
        p4.getChildren().add(g4);
        gri_dashboard.getChildren().add(p4);

        Panel p5 = new Panel();
        p5.setStyle("margin-left: 2px;");
        Grid g5 = new Grid();
        g5.setColumns(2);
        g5.setWidth("100%");
        g5.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-green'>AUTORIZADAS </span>"));
        Link l5 = new Link();
        l5.setMetodo("filtrarAutorizadas");
        l5.getChildren().add(new Etiqueta("<i class='fa fa-check-circle fa-4x text-green'></i>"));
        g5.getChildren().add(l5);
        g5.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_autorizadas + "</span>"));
        p5.getChildren().add(g5);
        gri_dashboard.getChildren().add(p5);
        Panel p6 = new Panel();
        p6.setStyle("margin-left: 2px;");
        Grid g6 = new Grid();
        g6.setWidth("100%");
        g6.setColumns(2);
        g6.setHeader(new Etiqueta("<span style='font-size:11px;' class='text-red'> NO AUTORIZADAS </span>"));
        Link l6 = new Link();
        l6.setMetodo("filtrarNoAutorizadas");
        l6.getChildren().add(new Etiqueta("<i class='fa fa-minus-circle fa-4x text-red'></i>"));
        g6.getChildren().add(l6);

        g6.getChildren().add(new Etiqueta("<span style='font-size:20px; text-align: left;'>" + num_no_autorizadas + "</span>"));
        p6.getChildren().add(g6);
        gri_dashboard.getChildren().add(p6);
    }

    public void actualizarLista() {
        if (mep_menu.getOpcion() == 2) {
            tab_tabla1.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
            dibujarDashboard();
            tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla1.ejecutarSql();
        }
    }

    public void filtrarPendientes() {
        tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.PENDIENTE));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarRecibidas() {
        tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECIBIDA));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarDevueltas() {
        tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.DEVUELTA));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarRechazadas() {
        tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECHAZADO));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarAutorizadas() {
        tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.AUTORIZADO));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarNoAutorizadas() {
        tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.NOAUTORIZADO));
        tab_tabla1.ejecutarSql();
    }

    public void dibujarNuevaLiquidacion() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla1.setTabla("cxp_cabece_factur", "ide_cpcfa", 1);
        tab_tabla1.setCondicion("ide_cpcfa=" + ide_cpcfa);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(6);

        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cntdo").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setValorDefecto(utilitario.getVariable("p_cxp_estado_factura_normal"));
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp=3");
        tab_tabla1.getColumna("ide_cndfp").setRequerida(true);
        tab_tabla1.getColumna("TARIFA_IVA_CPCFA").setVisible(false);
        tab_tabla1.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");
        tab_tabla1.getColumna("ide_cndfp").setOrden(5);
        tab_tabla1.getColumna("dias_credito_cpcfa").setVisible(false);
        tab_tabla1.getColumna("dias_credito_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("dias_credito_cpcfa").setValorDefecto("0");

        tab_tabla1.getColumna("ide_cndfp1").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp!=3");
        tab_tabla1.getColumna("ide_cndfp1").setOrden(50);
        tab_tabla1.getColumna("ide_cndfp1").setNombreVisual("DÍAS CREDITO");
        tab_tabla1.getColumna("ide_cndfp1").setEstilo("width:140px");
        tab_tabla1.getColumna("ide_cndfp1").setRequerida(true);

        tab_tabla1.getColumna("RECIBIDO_COMPRA_CPCFA").setVisible(false);  //Campo Luis
        tab_tabla1.getColumna("RECIBIDO_COMPRA_CPCFA").setValorDefecto("FALSE");

        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("ide_usua").setVisible(false);
        tab_tabla1.getColumna("MONTO_COM_CPCFA").setVisible(false);
        tab_tabla1.getColumna("IDE_CNTIC").setVisible(false);
        tab_tabla1.getColumna("fecha_trans_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_trans_cpcfa").setVisible(false);
        tab_tabla1.getColumna("fecha_emisi_cpcfa").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA EMISIÓN");
        tab_tabla1.getColumna("fecha_emisi_cpcfa").setOrden(1);
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_proveedo_geper=TRUE AND nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_geper").setRequerida(true);
        tab_tabla1.getColumna("ide_geper").setNombreVisual("PROVEEDOR");
        tab_tabla1.getColumna("ide_geper").setOrden(3);
        tab_tabla1.getColumna("autorizacio_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("ide_srtst").setRequerida(true);
        tab_tabla1.getColumna("autorizacio_cpcfa").setOrden(5);
        tab_tabla1.getColumna("autorizacio_cpcfa").setNombreVisual("NUM. AUTORIZACIÓN");
        tab_tabla1.getColumna("autorizacio_cpcfa").setEstilo("font-weight: bold");
        tab_tabla1.getColumna("observacion_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("observacion_cpcfa").setVisible(false);
        tab_tabla1.getColumna("pagado_cpcfa").setVisible(false);
        tab_tabla1.getColumna("pagado_cpcfa").setValorDefecto("False");
        tab_tabla1.getColumna("total_cpcfa").setVisible(false);
        tab_tabla1.getColumna("IDE_ADCOMP").setVisible(false);  //CAMPO LUIS
        tab_tabla1.getColumna("ide_srcom").setVisible(false);  //30/12/2019 
        tab_tabla1.getColumna("ide_ccdaf").setVisible(false);  //30/12/2019 

        tab_tabla1.getColumna("total_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("numero_cpcfa").setEstilo("font-size: 12px;font-weight: bold");
        tab_tabla1.getColumna("numero_cpcfa").setNombreVisual("NÚMERO");
        tab_tabla1.getColumna("numero_cpcfa").setOrden(4);
        tab_tabla1.getColumna("numero_cpcfa").setAncho(10);
        tab_tabla1.getColumna("numero_cpcfa").setComentario("Debe ingresar el numero de serie - establecimiento y numero secuencial");
        tab_tabla1.getColumna("numero_cpcfa").setMascara("999-999-999999999");
        tab_tabla1.getColumna("numero_cpcfa").setQuitarCaracteresEspeciales(true);
        tab_tabla1.getColumna("numero_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("base_grabada_cpcfa").setVisible(false);
        tab_tabla1.getColumna("base_grabada_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("valor_iva_cpcfa").setVisible(false);
        tab_tabla1.getColumna("valor_iva_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("base_no_objeto_iva_cpcfa").setVisible(false);
        tab_tabla1.getColumna("base_no_objeto_iva_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("base_tarifa0_cpcfa").setVisible(false);
        tab_tabla1.getColumna("base_tarifa0_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("otros_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("ide_srtst").setCombo("sri_tipo_sustento_tributario", "ide_srtst", "alterno_srtst,nombre_srtst", "");
        tab_tabla1.getColumna("ide_srtst").setValorDefecto(utilitario.getVariable("p_sri_tip_sus_tri02"));
        tab_tabla1.getColumna("ide_srtst").setNombreVisual("SUSTENTO TRIBUTARIO");
        tab_tabla1.getColumna("ide_srtst").setOrden(7);
        tab_tabla1.getColumna("ide_cncre").setVisible(false);
        tab_tabla1.getColumna("ide_cnccc").setVisible(false);
        tab_tabla1.getColumna("valor_ice_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("valor_ice_cpcfa").setVisible(false);
        tab_tabla1.getColumna("OTROS_CPCFA").setVisible(false);
        tab_tabla1.getColumna("DESCUENTO_CPCFA").setVisible(false);
        tab_tabla1.getColumna("PORCEN_DESC_CPCFA").setVisible(false);
        tab_tabla1.getColumna("liquida_nota_cpcfa").setValorDefecto("0");
        tab_tabla1.getColumna("motivo_nc_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("autorizacio_nc_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("numero_nc_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("fecha_emision_nc_cpcfa").setRequerida(true);
        tab_tabla1.getColumna("ide_cntdo_nc_cpcfa").setRequerida(true);

        tab_tabla1.getColumna("motivo_nc_cpcfa").setVisible(false);
        tab_tabla1.getColumna("autorizacio_nc_cpcfa").setVisible(false);
        tab_tabla1.getColumna("numero_nc_cpcfa").setVisible(false);
        tab_tabla1.getColumna("fecha_emision_nc_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cntdo_nc_cpcfa").setVisible(false);
        tab_tabla1.getColumna("motivo_nc_cpcfa").setVisible(false);
        tab_tabla1.getColumna("liquida_nota_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cndfp1").setVisible(false);
        tab_tabla1.getColumna("ide_cndfp1").setValorDefecto("3"); //EFECTIVO
        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
        }

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);

        tab_tabla2.setTabla("cxp_detall_factur", "ide_cpdfa", 2);
        if (ide_cpcfa != null) {
            tab_tabla2.setCondicion("ide_cpcfa=" + ide_cpcfa);
        } else {
            tab_tabla2.setCondicionForanea("ide_cpcfa=-1");
        }

        tab_tabla2.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla2.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("credi_tribu_cpdfa").setVisible(false);
        tab_tabla2.getColumna("devolucion_cpdfa").setVisible(false);
        tab_tabla2.getColumna("alter_tribu_cpdfa").setVisible(false);
        tab_tabla2.getColumna("alter_tribu_cpdfa").setValorDefecto("00");
        tab_tabla2.getColumna("cantidad_cpdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("cantidad_cpdfa").setDecimales(ser_factura.getDecimalesCantidad()); //DFJ
        tab_tabla2.getColumna("precio_cpdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("precio_cpdfa").setDecimales(ser_factura.getDecimalesPrecioUnitario()); //DFJ
        tab_tabla2.getColumna("valor_cpdfa").setEtiqueta();
        tab_tabla2.getColumna("valor_cpdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_tabla2.getColumna("valor_cpdfa").alinearDerecha();

        tab_tabla2.setScrollable(true);
        tab_tabla2.getColumna("iva_inarti_cpdfa").setCombo(ser_producto.getListaTipoIVA());
        tab_tabla2.getColumna("iva_inarti_cpdfa").setPermitirNullCombo(false);
        tab_tabla2.getColumna("ide_inarti").setRequerida(true);
        tab_tabla2.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_tabla2.getColumna("ide_inuni").setLongitud(-1);
        tab_tabla2.getColumna("iva_inarti_cpdfa").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("iva_inarti_cpdfa").setLongitud(-1);
        tab_tabla2.setScrollHeight(utilitario.getAltoPantalla() - 350);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel1);
        gru.getChildren().add(pat_panel2);
        mep_menu.dibujar(1, "CONFIGURACIÓN FACTURAS ELECTRÓNICAS", gru);

    }

    /**
     * Se ejecuta cuando cambia el Precio o la Cantidad de un detalle de la
     * factura
     *
     * @param evt
     */
    public void cambioPrecioCantidadIva(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
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
            cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_cpdfa"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_tabla2.getValor("precio_cpdfa"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_tabla2.setValor("valor_cpdfa", utilitario.getFormatoNumero(utilitario.getFormatoNumero(total, 4)));
        utilitario.addUpdateTabla(tab_tabla2, "valor_cpdfa", "");
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

        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            String iva = tab_tabla2.getValor(i, "iva_inarti_cpcfa");
            if (iva.equals("1")) { //SI IVA
                base_grabada = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpcfa")) + base_grabada;
                porcentaje_iva = tarifaIVA;
                valor_iva = base_grabada * porcentaje_iva; //0.12
            } else if (iva.equals("-1")) { // NO IVA
                base_tarifa0 = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpcfa")) + base_tarifa0;
            } else if (iva.equals("0")) { // NO OBJETO
                base_no_objeto = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpcfa")) + base_no_objeto;
            }
        }
        tab_tabla1.setValor("base_grabada_cpcfa", utilitario.getFormatoNumero(base_grabada));
        tab_tabla1.setValor("base_no_objeto_iva_cpcfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_tabla1.setValor("valor_iva_cpcfa", utilitario.getFormatoNumero(valor_iva));
        tab_tabla1.setValor("base_tarifa0_cpcfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_tabla1.setValor("total_cpcfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));
        utilitario.addUpdateTabla(tab_tabla1, "ide_geper,base_grabada_cpcfa,base_no_objeto_iva_cpcfa,valor_iva_cpcfa,base_tarifa0_cpcfa,total_cpcfa", "");
    }

    public void dibujarLiquidacionCompra() {
        Grupo gr_vista = new Grupo();
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver");
        bot_ver.setTitle("Ver Liquidación de Compra");
        bot_ver.setMetodo("abrirVerLiquidacion");
        bot_ver.setIcon("ui-icon-search");
        bar_menu.agregarComponente(bot_ver);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setTitle("Anular Liquidación de Compra");
        bot_anular.setMetodo("abrirAnularLiquidacion");
        bot_anular.setIcon("ui-icon-cancel");
        bar_menu.agregarComponente(bot_anular);
        bar_menu.agregarSeparador();

        bar_menu.agregarSeparador();

        Boton bot_enviar = new Boton();
        bot_enviar.setValue("Enviar al SRI");
        bot_enviar.setMetodo("enviarSRI");
        bot_enviar.setIcon("ui-icon-signal-diag");
        bar_menu.agregarBoton(bot_enviar);

        Boton bot_ride = new Boton();
        bot_ride.setValue("Imprimir");
        bot_ride.setTitle("Imprimir RIDE");
        bot_ride.setMetodo("abrirRIDE");
        bot_ride.setIcon("ui-icon-print");
        bar_menu.agregarBoton(bot_ride);

        Boton bot_reenviar = new Boton();
        bot_reenviar.setValue("Reenviar");
        bot_reenviar.setTitle("Enviar nuevamente al correo");
        bot_reenviar.setMetodo("reenviarLiquidacion");
        bot_reenviar.setIcon("ui-icon-mail-closed");
        bar_menu.agregarBoton(bot_reenviar);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cxp.getSqlLiquidacionesElectronicas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setNumeroTabla(999);
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("ide_cntdo").setVisible(false);
        tab_tabla1.getColumna("fecha_trans_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("fecha_emisi_cpcfa").setNombreVisual("FECHA");
        tab_tabla1.getColumna("nombre_cpefa").setVisible(true);
        tab_tabla1.getColumna("nombre_cpefa").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setNombreVisual("SECUENCIAL");
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("CLIENTE");
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas0").setNombreVisual("VENTAS IVA 0");
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("ventas12").setNombreVisual("VENTAS IVA");
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").setNombreVisual("IVA");
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.getColumna("total_cpcfa").setNombreVisual("TOTAL");
        tab_tabla1.getColumna("ide_srcom").setVisible(false);
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        // ok tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[3] eq '0' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[3] eq '0' ? 'text-red' : null");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        gr_vista.getChildren().clear();
        dibujarDashboard();
        Grupo gr = new Grupo();
        gr.getChildren().add(new Etiqueta("<div align='center'>"));
        gri_dashboard.setId("gri_dashboard");
        gri_dashboard.setWidth("100%");
        gri_dashboard.setColumns(6);
        gr.getChildren().add(gri_dashboard);
        gr.getChildren().add(new Etiqueta("</div>"));
        gr_vista.getChildren().add(gr);
        gr_vista.getChildren().add(bar_menu);
        gr_vista.getChildren().add(pat_panel);

        mep_menu.dibujar(2, "LISTADO DE NOTAS DE CRÉDITO", gr_vista);
        //metodo of mauricio
        utilitario.buscarPermisosObjetos();
    }

    public void reenviarLiquidacion() {
        //Valida que la factura este AUTORIZADA
        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            if (tab_tabla1.getValor("ide_srcom") != null) {
                if (tab_tabla1.getValor("nombre_cpefa") != null && tab_tabla1.getValor("nombre_cpefa").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                    dia_correo.dibujar();
                } else {
                    utilitario.agregarMensajeError("No se puede reenviar la Liquidación de Compra", "La Liquidación de Compra seleccionada debe estar en estado AUTORIZADO");
                }
            } else {
                utilitario.agregarMensajeInfo("La Liquidación de Compra seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Liquidación de Compra", "");
        }
    }

    public void aceptarReenviar() {
        if (utilitario.isCorreoValido(String.valueOf(tex_correo.getValue()))) {
            ser_comprobante_electronico.reenviarComprobante(String.valueOf(tex_correo.getValue()), tab_tabla1.getValor("ide_srcom"));
            dia_correo.cerrar();
            tex_correo.setValue(null);
        } else {
            utilitario.agregarMensajeError("Correo electrónico no válido", "");
        }
    }

    public void abrirAnularLiquidacion() {
        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            //valida que este en estado PENDIENTE o AUTORIZADA  
            if (tab_tabla1.getValor("nombre_cpefa") != null) {
                if (!tab_tabla1.getValor("nombre_cpefa").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) {
                    if (!tab_tabla1.getValor("nombre_cpefa").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                        utilitario.agregarMensajeError("No se puede anular la Liquidación de Compra Electrónica seleccionada", "Solo se pueden anular Liquidación de Compra en estado PENDIENTE y AUTORIZADO");
                        return;
                    }
                }
            }
            con_confirma.getBot_aceptar().setMetodo("anularLiquidacion");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Liquidación de Compra", "");
        }

    }

    public void abrirVerLiquidacion() {
        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            ide_cpcfa = tab_tabla1.getValor("ide_cpcfa");
            dibujarNuevaLiquidacion();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Liquidación de Compra", "");
        }
    }

    public void anularLiquidacion() {
        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            ser_cxp.anularDocumento(tab_tabla1.getValor("ide_cpcfa"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                String aux = tab_tabla1.getValorSeleccionado();
                tab_tabla1.actualizar();
                tab_tabla1.setFilaActual(aux);
                tab_tabla1.calcularPaginaActual();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Liquidación de Compra", "");
        }
    }

    public void abrirRIDE() {
        if (tab_tabla1.getValor("ide_cpcfa") != null) {

            if (tab_tabla1.getValor("nombre_cpefa") != null && !tab_tabla1.getValor("nombre_cpefa").equals(EstadoComprobanteEnum.ANULADO.getDescripcion())) {
                try {
                    ser_comprobante_electronico.getRIDE(tab_tabla1.getValor("ide_srcom"));
                    vpd_nota_ride.setVisualizarPDFUsuario();
                    vpd_nota_ride.dibujar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                utilitario.agregarMensajeError("No se puede Visualizar el Comprobate", "La Liquidación de Compra seleccionada se encuentara ANULADA");
            }

        } else {
            utilitario.agregarMensajeInfo("Seleccione una Liquidación de Compra", "");
        }
    }

    public void enviarSRI() {

        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            //Valida que se encuentre en estado PENDIENTE o RECIBIDA
            if ((tab_tabla1.getValor("nombre_cpefa")) != null && (tab_tabla1.getValor("nombre_cpefa").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) || tab_tabla1.getValor("nombre_cpefa").equals(EstadoComprobanteEnum.RECIBIDA.getDescripcion())) {
                String mensaje = ser_comprobante_electronico.enviarComprobante(tab_tabla1.getValor("clave_acceso"));
                String aux = tab_tabla1.getValorSeleccionado();
                dibujarLiquidacionCompra();
                tab_tabla1.setFilaActual(aux);
                tab_tabla1.calcularPaginaActual();
                if (mensaje.isEmpty()) {
                    String mensje = "<p> LIQUIDACIÓN DE COMPRA NRO. " + tab_tabla1.getValor("nombre_cpefa") + " ";
                    mensje += "</br>AMBIENTE : <strong>" + (utilitario.getVariable("p_sri_ambiente_comp_elect").equals("1") ? "PRUEBAS" : "PRODUCCIÓN") + "</strong></p>";  //********variable ambiente facturacion electronica                    
                    mensje += "<p>ESTADO : <strong>" + tab_tabla1.getValor("nombre_cpefa") + "</strong></p>";
                    mensje += "<p>NÚMERO DE AUTORIZACION : <span style='font-size:12px;font-weight: bold;'>" + tab_tabla1.getValor("CLAVE_ACCESO") + "</span> </p>";
                    men_factura.setMensajeExito("LIQUIDACIÓN DE COMPRA ELECTRÓNICA AUTORIZADA", mensje);
                    men_factura.dibujar();
                    //generar transaccion nota de credito a cliente
                    //Guarda la cuenta por pagar                            
                    ser_cxp.generarTransaccionCompra(tab_tabla1);
                    utilitario.getConexion().ejecutarListaSql();
                } else {
                    utilitario.agregarMensajeError(mensaje, "");
                }

            } else {
                utilitario.agregarMensajeInfo("La Liquidación de Compra seleccionada no se encuentra en estado PENDIENTE o RECIBIDA", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Liquidación de Compra", "");
        }
    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla2.insertar();
        } else {
            ide_cpcfa = null;
            dibujarNuevaLiquidacion();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla1.setValor("TARIFA_IVA_CPCFA", utilitario.getFormatoNumero((tarifaIVA * 100)));
            if (tab_tabla1.guardar()) {
                if (tab_tabla2.guardar()) {
                    if (guardarPantalla().isEmpty()) {
                        ser_comprobante_electronico.generarLiquidacionCompraElectronica(tab_tabla1.getValor("ide_cpcfa"));
                        String aux = tab_tabla1.getValor("ide_cpcfa");
                        dibujarLiquidacionCompra();
                        tab_tabla1.setFilaActual(aux);
                        tab_tabla1.calcularPaginaActual();
                        abrirRIDE();
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla2.eliminar();
            calcularTotalFactura();
        }
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

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

    public Tabla getTab_seleccion() {
        return tab_tabla1;
    }

    public void setTab_seleccion(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

}
