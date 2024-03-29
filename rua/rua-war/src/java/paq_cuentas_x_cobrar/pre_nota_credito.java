/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.AsientoContable;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.BotonesCombo;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
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
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_nota_credito extends Pantalla {

    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioComprobanteElectronico ser_comprobante_electronico = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

    private final Combo com_pto_emision = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private final MenuPanel mep_menu = new MenuPanel();

    private Tabla tab_tabla1;
    private Tabla tab_tabla2;
    private double tarifaIVA = 0;

    private final VisualizarPDF vpd_nota_ride = new VisualizarPDF();
    private final Mensaje men_factura = new Mensaje();
    private Confirmar con_confirma = new Confirmar();

    private String ide_cpcno = null;

    private final Grid gri_dashboard = new Grid();

    private final Dialogo dia_correo = new Dialogo();
    private final Texto tex_correo = new Texto();

    private AsientoContable asc_asiento = new AsientoContable();

    private Texto tex_observacion = new Texto();
    private Calendario cal_fecha = new Calendario();

    public pre_nota_credito() {
        tarifaIVA = ser_configuracion.getPorcentajeIva(utilitario.getFechaActual());
        bar_botones.quitarBotonsNavegacion();

        com_pto_emision.setId("com_pto_emision");
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmisionNotasCredito());
        com_pto_emision.setMetodo("actualizarNotas");
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
        bot_consultar.setMetodo("actualizarNotas");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        mep_menu.setMenuPanel("OPCIONES NOTAS DE CRÉDITO", "20%");

        mep_menu.agregarItem("Listado de Notas de Crédito", "dibujarNotaCredito", "ui-icon-note");  //2
        //  mep_menu.agregarItem("Generar Asiento Contable", "dibujarNotaCreditoNoContabilizadas", "ui-icon-notice"); //3
        //mep_menu.agregarItem("Notas de Crédito Anuladas", "dibujarNotaCreditoAnuladas", "ui-icon-cancel"); //4
        mep_menu.agregarItem("Generar Nota de Crédito de Factura", "dibujarNotaCreditoFactura", "ui-icon-calculator");  //5
        mep_menu.agregarSubMenu("CONTABILIDAD");
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarNotasNoContabilizadas", "ui-icon-notice");

        agregarComponente(mep_menu);

        vpd_nota_ride.setId("vpd_nota_ride");
        vpd_nota_ride.setTitle("RIDE");
        utilitario.getPantalla().getChildren().add(vpd_nota_ride);

        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular la Nota de Crédito ?");
        con_confirma.setTitle("ANULAR NOTA DE CRÉDITO");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        dia_correo.setId("dia_correo");
        dia_correo.setTitle("REENVIAR NOTA DE CRÉDITO ELECTRONICA AL CLIENTE");
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

        dibujarNotaCredito();

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(asc_asiento);
    }

    public void dibujarNotaCreditoFactura() {

        Grid gri_op = new Grid();
        gri_op.setColumns(2);
        gri_op.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN :</strong>"));
        tex_observacion.limpiar();
        tex_observacion.setSize(160);
        gri_op.getChildren().add(tex_observacion);
        gri_op.getChildren().add(new Etiqueta("<strong>FECHA :</strong>"));
        cal_fecha.limpiar();
        cal_fecha.setFechaActual();
        cal_fecha.setMaxdate(utilitario.getFecha(utilitario.getFechaActual()));
        gri_op.getChildren().add(cal_fecha);
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        // boton seleccion inversa
        BotonesCombo boc_seleccion_inversa = new BotonesCombo();
        ItemMenu itm_todas = new ItemMenu();
        ItemMenu itm_niguna = new ItemMenu();

        itm_todas.setValue("Seleccion Inversa");
        itm_todas.setIcon("ui-icon-circle-check");
        itm_todas.setMetodo("seleccinarInversa");
        itm_todas.setUpdate("tab_seleccion");
        boc_seleccion_inversa.agregarBoton(itm_todas);

        boc_seleccion_inversa.setValue("Selección Todo");
        boc_seleccion_inversa.setIcon("ui-icon-check");
        boc_seleccion_inversa.setMetodo("seleccionarTodas");
        boc_seleccion_inversa.setUpdate("tab_seleccion");

        itm_niguna.setValue("Seleccionar Ninguna");
        itm_niguna.setIcon("ui-icon-minus");
        itm_niguna.setMetodo("seleccionarNinguna");
        itm_niguna.setUpdate("tab_seleccion");
        bar_menu.agregarComponente(boc_seleccion_inversa);

        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Notas de Crédito");
        bot_asi.setMetodo("generarNC");
        bar_menu.agregarComponente(bot_asi);

        bar_menu.agregarSeparador();
        Boton bot_enviar = new Boton();
        bot_enviar.setValue("Enviar Notas de Credito al SRI");
        bot_enviar.setMetodo("enviarSRILote");
        bot_enviar.setIcon("ui-icon-signal-diag");
        bar_menu.agregarBoton(bot_enviar);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_factura.getSqlFacturasAutorizadasNC(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setCampoPrimaria("ide_cccfa");
        tab_tabla1.getColumna("ide_cccfa").setVisible(false);
        tab_tabla1.getColumna("num_doc_mod_cpcno").setVisible(false);
        tab_tabla1.getColumna("ESTADO").setLongitud(-1);
        tab_tabla1.getColumna("CLIENTE").setFiltroContenido();
        tab_tabla1.getColumna("IDENTIFICACION").setFiltroContenido();
        tab_tabla1.getColumna("SECUENCIAL").setFiltroContenido();
        tab_tabla1.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA");
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("DESCUENTO").alinearDerecha();
        tab_tabla1.getColumna("IVA").alinearDerecha();
        tab_tabla1.getColumna("TOTAL").alinearDerecha();

        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setSeleccionTabla("multiple");

        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(gri_op);
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(5, "GENERAR NOTAS DE CRÉDITO DE FACTURAS AUTORIZADAS", gru);
        utilitario.buscarPermisosObjetos();
    }

    public void generarNC() {

        if (tab_tabla1.getFilasSeleccionadas() != null && tab_tabla1.getFilasSeleccionadas().length() > 0) {
            for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {
                for (int j = 0; j < tab_tabla1.getSeleccionados().length; j++) {

                    if (tab_tabla1.getSeleccionados()[j].equals(tab_tabla1.getFilas().get(i))) {
                        //seleccionada
                        String num_doc = tab_tabla1.getValor(i, "num_doc_mod_cpcno");

                        ser_factura.generarNotaCreditodeFactura(com_pto_emision.getValue() + "", num_doc, tarifaIVA, cal_fecha.getFecha(), tex_observacion.getValue() + "");
                    }

                }
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos una Factura", "");
        }
    }

    public void seleccionarTodas() {
        tab_tabla1.setSeleccionados(null);
        Fila seleccionados[] = new Fila[tab_tabla1.getTotalFilas()];
        for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {
            seleccionados[i] = tab_tabla1.getFilas().get(i);
        }
        tab_tabla1.setSeleccionados(seleccionados);
    }

    public void seleccinarInversa() {
        if (tab_tabla1.getSeleccionados() == null) {
            seleccionarTodas();
        } else if (tab_tabla1.getSeleccionados().length == tab_tabla1.getTotalFilas()) {
            seleccionarNinguna();
        } else {
            Fila seleccionados[] = new Fila[tab_tabla1.getTotalFilas() - tab_tabla1.getSeleccionados().length];
            int cont = 0;
            for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {
                boolean boo_selecionado = false;
                for (int j = 0; j < tab_tabla1.getSeleccionados().length; j++) {
                    if (tab_tabla1.getSeleccionados()[j].equals(tab_tabla1.getFilas().get(i))) {
                        boo_selecionado = true;
                        break;
                    }
                }
                if (boo_selecionado == false) {
                    seleccionados[cont] = tab_tabla1.getFilas().get(i);
                    cont++;
                }
            }
            tab_tabla1.setSeleccionados(seleccionados);
        }

    }

    public void seleccionarNinguna() {
        tab_tabla1.setSeleccionados(null);
    }

    public void  enviarSRILote () {
        try {
            TablaGenerica tag_com = utilitario.consultar(ser_comprobante_electronico.getSqlNotasPendienets());
            for (int i = 0; i < tag_com.getTotalFilas(); i++) {
                ser_comprobante_electronico.enviarComprobante(tag_com.getValor(i, "claveacceso_srcom"));
            }
        } catch (Exception e) {
            utilitario.crearError("Error al enviar al SRI", "enviarSRI()", e);
        }
    }

    public void dibujarNotasNoContabilizadas() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_factura.getSqlNotasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setCampoPrimaria("ide_cpcno");
        tab_tabla1.getColumna("ide_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("num_doc_mod_cpcno").setNombreVisual("DOC. MODIFICADO");
        tab_tabla1.getColumna("fecha_trans_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_cpeno").setVisible(false);
        tab_tabla1.getColumna("nombre_cpeno").setFiltroContenido();
        tab_tabla1.getColumna("fecha_emisi_cpcno").setNombreVisual("FECHA");
        tab_tabla1.getColumna("nombre_cpeno").setVisible(true);
        tab_tabla1.getColumna("nombre_cpeno").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_cpeno").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcno").setNombreVisual("SECUENCIAL");
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("CLIENTE");
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas0").setNombreVisual("VENTAS IVA 0");
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("ventas12").setNombreVisual("VENTAS IVA");
        tab_tabla1.getColumna("valor_iva_cpcno").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcno").setNombreVisual("IVA");
        tab_tabla1.getColumna("total_cpcno").alinearDerecha();
        tab_tabla1.getColumna("total_cpcno").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.getColumna("total_cpcno").setNombreVisual("TOTAL");
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setSeleccionTabla("multiple");

        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(3, "NOTAS DE CRÉDITO NO CONTABILIZADAS", gru);
        // metodo of mauriicio
        utilitario.buscarPermisosObjetos();
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla1.getFilasSeleccionadas() != null && tab_tabla1.getFilasSeleccionadas().length() > 0) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.setAsientoNotasCreditoCxC(tab_tabla1.getFilasSeleccionadas());
            asc_asiento.getBot_aceptar().setMetodo("guardar");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos una Nota de Crédito", "");
        }
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
        TablaGenerica tg = utilitario.consultar(ser_comprobante_electronico.getSqlTotalComprobantesPorEstado(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), TipoComprobanteEnum.NOTA_DE_CREDITO, com_pto_emision.getValue() + ""));
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

    public void actualizarNotas() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla1.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
            dibujarDashboard();
        } else if (mep_menu.getOpcion() == 2) {
            tab_tabla1.setSql(ser_factura.getSqlNotas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla1.ejecutarSql();
        } else if (mep_menu.getOpcion() == 3) {
            tab_tabla1.setSql(ser_factura.getSqlNotasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla1.ejecutarSql();
        } else if (mep_menu.getOpcion() == 5) {
            tab_tabla1.setSql(ser_factura.getSqlFacturasAutorizadasNC(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla1.ejecutarSql();
        }

    }

    public void filtrarPendientes() {
        tab_tabla1.setSql(ser_factura.getSqlNotasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.PENDIENTE));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarRecibidas() {
        tab_tabla1.setSql(ser_factura.getSqlNotasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECIBIDA));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarDevueltas() {
        tab_tabla1.setSql(ser_factura.getSqlNotasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.DEVUELTA));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarRechazadas() {
        tab_tabla1.setSql(ser_factura.getSqlNotasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECHAZADO));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarAutorizadas() {
        tab_tabla1.setSql(ser_factura.getSqlNotasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.AUTORIZADO));
        tab_tabla1.ejecutarSql();
    }

    public void filtrarNoAutorizadas() {
        tab_tabla1.setSql(ser_factura.getSqlNotasElectronicasPorEstado(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.NOAUTORIZADO));
        tab_tabla1.ejecutarSql();
    }

    public void dibujarNuevaNotaCredito() {

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla1.setTabla("cxp_cabecera_nota", "ide_cpcno", 1);
        tab_tabla1.setCondicion("ide_cpcno=" + ide_cpcno);
        tab_tabla1.getGrid().setColumns(6);
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_cpmno").setCombo("cxp_motivo_nota", "ide_cpmno", "nombre_cpmno", "");
        tab_tabla1.getColumna("ide_cpmno").setRequerida(true);
        tab_tabla1.getColumna("ide_cpeno").setValorDefecto("1");//Estado normal por defecto         
        tab_tabla1.getColumna("ide_cpeno").setVisible(false);
        tab_tabla1.getColumna("ide_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_srcom").setVisible(false);
        tab_tabla1.getColumna("TARIFA_IVA_CPCNO").setVisible(false);

        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
            tab_tabla1.getColumna("NUMERO_CPCNO").setLectura(true);
        }
        tab_tabla1.getColumna("ide_cntdo").setVisible(false);
        tab_tabla1.getColumna("ide_cntdo").setValorDefecto("0"); //nota de credito
        tab_tabla1.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp=3");
        tab_tabla1.getColumna("ide_cndfp").setRequerida(true);
        tab_tabla1.getColumna("fecha_trans_cpcno").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_trans_cpcno").setVisible(false);
        tab_tabla1.getColumna("valor_ice_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_cnccc").setVisible(false);
        tab_tabla1.getColumna("total_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_no_objeto_iva_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_tarifa0_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_grabada_cpcno").setLectura(true);
        tab_tabla1.getColumna("valor_iva_cpcno").setLectura(true);
        tab_tabla1.getColumna("total_cpcno").setLectura(true);
        tab_tabla1.getColumna("ide_ccdaf").setVisible(false);
        tab_tabla1.getColumna("fecha_emisi_cpcno").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_emisi_cpcno").setRequerida(true);
        tab_tabla1.getColumna("num_doc_mod_cpcno").setMascara("999-999-999999999");
        tab_tabla1.getColumna("num_doc_mod_cpcno").setMetodoChange("buscaFactura");
        tab_tabla1.getColumna("num_doc_mod_cpcno").setRequerida(true);
        tab_tabla1.getColumna("valor_mod_cpcno").setRequerida(true);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
        }

        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue())) == false) {
            tab_tabla1.setValor("NUMERO_CPCNO", String.valueOf(ser_factura.getSecuencialFactura(String.valueOf(com_pto_emision.getValue()))));
        }
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);

        tab_tabla2.setTabla("cxp_detalle_nota", "ide_cpdno", 2);
        if (ide_cpcno != null) {
            tab_tabla2.setCondicion("ide_cpcno=" + ide_cpcno);
        } else {
            tab_tabla2.setCondicionForanea("ide_cpdno=-1");
        }

        tab_tabla2.getColumna("ide_cpdno").setVisible(false);
        tab_tabla2.getColumna("ide_cpcno").setVisible(false);
        tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("credi_tribu_cpdno").setVisible(false);
        tab_tabla2.getColumna("devolucion_cpdno").setVisible(false);
        tab_tabla2.getColumna("alter_tribu_cpdno").setVisible(false);
        tab_tabla2.getColumna("alter_tribu_cpdno").setValorDefecto("00");
        tab_tabla2.getColumna("cantidad_cpdno").setMetodoChange("cambioPrecioCantidadIva");
        //tab_tabla2.getColumna("cantidad_cpdno").setDecimales(ser_factura.getDecimalesCantidad()); //DFJ
        tab_tabla2.getColumna("precio_cpdno").setMetodoChange("cambioPrecioCantidadIva");
        //tab_tabla2.getColumna("precio_cpdno").setDecimales(ser_factura.getDecimalesPrecioUnitario()); //DFJ
        tab_tabla2.getColumna("valor_cpdno").setEtiqueta();
        tab_tabla2.getColumna("valor_cpdno").setEstilo("font-size:14px;font-weight: bold;");
        tab_tabla2.getColumna("valor_cpdno").alinearDerecha();
        tab_tabla2.getColumna("descuento_cpdno").alinearDerecha();
        tab_tabla2.getColumna("descuento_cpdno").setValorDefecto(utilitario.getFormatoNumero("0"));

        tab_tabla2.setScrollable(true);
        tab_tabla2.getColumna("iva_inarti_cpdno").setCombo(ser_producto.getListaTipoIVA());
        tab_tabla2.getColumna("iva_inarti_cpdno").setPermitirNullCombo(false);
        tab_tabla2.getColumna("ide_inarti").setRequerida(true);
        tab_tabla2.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_tabla2.getColumna("ide_inuni").setLongitud(-1);
        tab_tabla2.getColumna("iva_inarti_cpdno").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("iva_inarti_cpdno").setLongitud(-1);
        tab_tabla2.setScrollHeight(utilitario.getAltoPantalla() - 350);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel1);
        gru.getChildren().add(pat_panel2);
        mep_menu.dibujar(1, "NOTA DE CREDITO", gru);

    }

    public void buscaFactura() {
        tab_tabla2.limpiar();
        if (tab_tabla1.getValor("num_doc_mod_cpcno") != null) {
            TablaGenerica tab_fac = ser_factura.getFacturaPorSecuencial(tab_tabla1.getValor("num_doc_mod_cpcno"));
            tab_tabla1.setValor("ide_geper", tab_fac.getValor("ide_geper"));
            tab_tabla1.setValor("fecha_emision_mod_cpcno", tab_fac.getValor("fecha_emisi_cccfa"));
            tab_tabla1.setValor("valor_mod_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("total_cccfa")));
            tab_tabla1.setValor("total_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("total_cccfa")));
            tab_tabla1.setValor("base_grabada_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("base_grabada_cccfa")));
            tab_tabla1.setValor("base_no_objeto_iva_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("base_no_objeto_iva_cccfa")));
            tab_tabla1.setValor("base_tarifa0_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("base_tarifa0_cccfa")));
            tab_tabla1.setValor("valor_iva_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("valor_iva_cccfa")));
            for (int i = 0; i < tab_fac.getTotalFilas(); i++) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("ide_inarti", tab_fac.getValor(i, "ide_inarti"));
                tab_tabla2.setValor("ide_inuni", tab_fac.getValor(i, "ide_inuni"));
                tab_tabla2.setValor("cantidad_cpdno", tab_fac.getValor(i, "cantidad_ccdfa"));
                tab_tabla2.setValor("precio_cpdno", tab_fac.getValor(i, "precio_ccdfa"));
                tab_tabla2.setValor("valor_cpdno", tab_fac.getValor(i, "total_ccdfa"));
                tab_tabla2.setValor("iva_inarti_cpdno", tab_fac.getValor(i, "iva_inarti_ccdfa"));
                tab_tabla2.setValor("observacion_cpdno", tab_fac.getValor(i, "observacion_ccdfa"));
                tab_tabla2.setValor("descuento_cpdno", tab_fac.getValor(i, "descuento_ccdfa"));
            }
        }
        calcularTotalFactura();
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
            cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_cpdno"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_tabla2.getValor("precio_cpdno"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_tabla2.setValor("valor_cpdno", utilitario.getFormatoNumero(utilitario.getFormatoNumero(total, 4)));
        utilitario.addUpdateTabla(tab_tabla2, "valor_cpdno", "");
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
            String iva = tab_tabla2.getValor(i, "iva_inarti_cpdno");
            if (iva.equals("1")) { //SI IVA
                base_grabada = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdno")) + base_grabada;
                porcentaje_iva = tarifaIVA;
                valor_iva = base_grabada * porcentaje_iva; //0.12
            } else if (iva.equals("-1")) { // NO IVA
                base_tarifa0 = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdno")) + base_tarifa0;
            } else if (iva.equals("0")) { // NO OBJETO
                base_no_objeto = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdno")) + base_no_objeto;
            }
        }
        tab_tabla1.setValor("base_grabada_cpcno", utilitario.getFormatoNumero(base_grabada));
        tab_tabla1.setValor("base_no_objeto_iva_cpcno", utilitario.getFormatoNumero(base_no_objeto));
        tab_tabla1.setValor("valor_iva_cpcno", utilitario.getFormatoNumero(valor_iva));
        tab_tabla1.setValor("base_tarifa0_cpcno", utilitario.getFormatoNumero(base_tarifa0));
        tab_tabla1.setValor("total_cpcno", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));
        utilitario.addUpdateTabla(tab_tabla1, "ide_geper,VALOR_MOD_CPCNO,FECHA_EMISION_MOD_CPCNO,base_grabada_cpcno,base_no_objeto_iva_cpcno,valor_iva_cpcno,base_tarifa0_cpcno,total_cpcno", "");
    }

    public void dibujarNotaCredito() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver");
        bot_ver.setTitle("Ver Nota de Crédito");
        bot_ver.setMetodo("abrirVerNota");
        bot_ver.setIcon("ui-icon-search");
        bar_menu.agregarComponente(bot_ver);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setTitle("Anular Nota de Crédito");
        bot_anular.setMetodo("abrirAnularNotas");
        bot_anular.setIcon("ui-icon-cancel");
        bar_menu.agregarComponente(bot_anular);
        bar_menu.agregarSeparador();

        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
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
            bot_reenviar.setTitle("Enviar nuevamente al correo del cliente");
            bot_reenviar.setMetodo("reenviarNota");
            bot_reenviar.setIcon("ui-icon-mail-closed");
            bar_menu.agregarBoton(bot_reenviar);
        }

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_factura.getSqlNotas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setNumeroTabla(986);
        tab_tabla1.setCampoPrimaria("ide_cpcno");
        tab_tabla1.getColumna("ide_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("num_doc_mod_cpcno").setNombreVisual("DOC. MODIFICADO");
        tab_tabla1.getColumna("fecha_trans_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_cpeno").setVisible(false);
        tab_tabla1.getColumna("nombre_cpeno").setFiltroContenido();
        tab_tabla1.getColumna("fecha_emisi_cpcno").setNombreVisual("FECHA");
        tab_tabla1.getColumna("nombre_cpeno").setVisible(true);
        tab_tabla1.getColumna("nombre_cpeno").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_cpeno").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcno").setNombreVisual("SECUENCIAL");
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
        tab_tabla1.getColumna("valor_iva_cpcno").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcno").setNombreVisual("IVA");
        tab_tabla1.getColumna("total_cpcno").alinearDerecha();
        tab_tabla1.getColumna("total_cpcno").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.getColumna("total_cpcno").setNombreVisual("TOTAL");
        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
            tab_tabla1.getColumna("ide_srcom").setVisible(false);
        }
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        // ok tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[3] eq '0' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[3] eq '0' ? 'text-red' : null");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();

        dibujarDashboard();
        Grupo gr = new Grupo();
        gr.getChildren().add(new Etiqueta("<div align='center'>"));
        gri_dashboard.setId("gri_dashboard");
        gri_dashboard.setWidth("100%");
        gri_dashboard.setColumns(6);
        gr.getChildren().add(gri_dashboard);
        gr.getChildren().add(new Etiqueta("</div>"));
        gru.getChildren().add(gr);

        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "LISTADO DE NOTAS DE CRÉDITO", gru);
        //metodo of mauricio
        utilitario.buscarPermisosObjetos();
    }

    public void reenviarNota() {
        //Valida que la factura este AUTORIZADA
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            if (tab_tabla1.getValor("ide_srcom") != null) {
                if (tab_tabla1.getValor("nombre_cpeno") != null && tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                    dia_correo.dibujar();
                } else {
                    utilitario.agregarMensajeError("No se puede reenviar la Nota de Crédito", "La Nota de Crédito seleccionada debe estar en estado AUTORIZADO");
                }
            } else {
                utilitario.agregarMensajeInfo("La Nota de Crédito seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Nota de Crédito", "");
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

    public void abrirAnularNotas() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
                //valida que este en estado PENDIENTE o AUTORIZADA  
                if (tab_tabla1.getValor("nombre_cpeno") != null) {
                    if (!tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) {
                        if (!tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                            utilitario.agregarMensajeError("No se puede anular la Nota de Crédito Electrónica seleccionada", "Solo se pueden anular Notas de Crédito en estado PENDIENTE y AUTORIZADO");
                            return;
                        }
                    }
                }

            }
            con_confirma.getBot_aceptar().setMetodo("anularNota");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Nota de Crédito", "");
        }

    }

    public void abrirVerNota() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            ide_cpcno = tab_tabla1.getValor("ide_cpcno");
            dibujarNuevaNotaCredito();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Nota de Crédito", "");
        }
    }

    public void anularNota() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            ser_factura.anularNotaCredito(tab_tabla1.getValor("ide_cpcno"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                String aux = tab_tabla1.getValorSeleccionado();
                tab_tabla1.actualizar();
                tab_tabla1.setFilaActual(aux);
                tab_tabla1.calcularPaginaActual();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Nota de Crédito", "");
        }
    }

    public void abrirRIDE() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            if (tab_tabla1.getValor("ide_srcom") != null) {
                if (tab_tabla1.getValor("nombre_cpeno") != null && !tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.ANULADO.getDescripcion())) {
                    try {
                        ser_comprobante_electronico.getRIDE(tab_tabla1.getValor("ide_srcom"));
                        vpd_nota_ride.setVisualizarPDFUsuario();
                        vpd_nota_ride.dibujar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    utilitario.agregarMensajeError("No se puede Visualizar el Comprobate", "La Nota de Crédito seleccionada se encuentara ANULADA");
                }

            } else {
                utilitario.agregarMensajeInfo("La Nota de Crédito seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    public void enviarSRI() {

        if (tab_tabla1.getValor("ide_cpcno") != null) {
            //Valida que se encuentre en estado PENDIENTE o RECIBIDA
            if ((tab_tabla1.getValor("nombre_cpeno")) != null && (tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) || tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.RECIBIDA.getDescripcion())) {
                String mensaje = ser_comprobante_electronico.enviarComprobante(tab_tabla1.getValor("clave_acceso"));

                String aux = tab_tabla1.getValorSeleccionado();
                dibujarNotaCredito();
                tab_tabla1.setFilaActual(aux);
                tab_tabla1.calcularPaginaActual();
                if (mensaje.isEmpty()) {
                    String mensje = "<p> NOTA DE CRÉDITO NRO. " + tab_tabla1.getValor("numero_cpcno") + " ";
                    mensje += "</br>AMBIENTE : <strong>" + (utilitario.getVariable("p_sri_ambiente_comp_elect").equals("1") ? "PRUEBAS" : "PRODUCCIÓN") + "</strong></p>";  //********variable ambiente facturacion electronica                    
                    mensje += "<p>ESTADO : <strong>" + tab_tabla1.getValor("nombre_cpeno") + "</strong></p>";
                    mensje += "<p>NÚMERO DE AUTORIZACION : <span style='font-size:12px;font-weight: bold;'>" + tab_tabla1.getValor("CLAVE_ACCESO") + "</span> </p>";
                    men_factura.setMensajeExito("NOTA DE CRÉDITO ELECTRÓNICA AUTORIZADA", mensje);
                    men_factura.dibujar();
                    //generar transaccion nota de credito a cliente
                    String ide_cccfa = ser_factura.getIdeFacturaPorSecuencial(tab_tabla1.getValor("num_doc_mod_cpcno"));
                    ser_factura.generarTransaccionNotaCredito(ide_cccfa, tab_tabla1);
                    //generar reverso de comprobante de inventario
                    ser_inventario.generarComprobnateTransaccionNotaCredito(tab_tabla1.getValorSeleccionado());
                    utilitario.getConexion().ejecutarListaSql();
                } else {
                    utilitario.agregarMensajeError(mensaje, "");
                }

            } else {
                utilitario.agregarMensajeInfo("La Nota de Crédito seleccionada no se encuentra en estado PENDIENTE o RECIBIDA", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Nota de Crédito", "");
        }
    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla2.insertar();
        } else {
            ide_cpcno = null;
            dibujarNuevaNotaCredito();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla1.setValor("TARIFA_IVA_CPCNO", utilitario.getFormatoNumero((tarifaIVA * 100)));
            if (tab_tabla1.guardar()) {
                if (tab_tabla2.guardar()) {
                    if (guardarPantalla().isEmpty()) {
                        if (ser_factura.isFacturaElectronica(String.valueOf(com_pto_emision.getValue()))) {
                            ser_comprobante_electronico.generarNotaCreditoElectronica(tab_tabla1.getValor("ide_cpcno"));
                            String aux = tab_tabla1.getValor("ide_cpcno");
                            dibujarNotaCredito();
                            tab_tabla1.setFilaActual(aux);
                            tab_tabla1.calcularPaginaActual();
                            abrirRIDE();
                        }
                    }
                }
            }
        } else if (mep_menu.getOpcion() == 3) {
            if (asc_asiento.isVisible()) {
                asc_asiento.guardar();
                if (asc_asiento.isVisible() == false) {
                    dibujarNotasNoContabilizadas();
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
