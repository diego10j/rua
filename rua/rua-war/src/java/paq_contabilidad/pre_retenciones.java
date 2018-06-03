/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import componentes.Retencion;
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
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.component.panel.Panel;
import servicios.ceo.ServicioComprobanteElectronico;
import servicios.contabilidad.ServicioRetenciones;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_retenciones extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private final Combo com_autoriza = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private Combo com_impuesto;
    @EJB
    private final ServicioRetenciones ser_retencion = (ServicioRetenciones) utilitario.instanciarEJB(ServicioRetenciones.class);
    @EJB
    private final ServicioComprobanteElectronico ser_comElec = (ServicioComprobanteElectronico) utilitario.instanciarEJB(ServicioComprobanteElectronico.class);

    private Tabla tab_tabla;
    private Tabla tab_tabla1;

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private final Confirmar con_confirma = new Confirmar();

    private final Grid gri_dashboard = new Grid();
    private final Mensaje men_factura = new Mensaje();
    private Retencion ret_retencion = new Retencion();
    private final Dialogo dia_correo = new Dialogo();
    private final Texto tex_correo = new Texto();

    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();

    public pre_retenciones() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.agregarReporte();

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sec_rango_reporte.setMultiple(true);
        agregarComponente(sec_rango_reporte);

        com_autoriza.setCombo(ser_retencion.getSqlPuntosEmision());
        com_autoriza.setMetodo("actualizarConsulta");
        com_autoriza.eliminarVacio();
        bar_botones.agregarComponente(new Etiqueta("PUNTO DE EMISIÓN: "));
        bar_botones.agregarComponente(com_autoriza);

        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));

        //cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-" + utilitario.getMes(utilitario.getFechaActual()) + "-01"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));

        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setMetodo("actualizarConsulta");
        bot_consultar.setIcon("ui-icon-search");

        bar_botones.agregarBoton(bot_consultar);

        mep_menu.setMenuPanel("COMPROBANTES DE RETENCIÓN", "20%");
        mep_menu.agregarItem("Listado de Retenciones", "dibujarListado", "ui-icon-note"); //1
        mep_menu.agregarItem("Retenciones Anuladas", "dibujarAnuladas", "ui-icon-cancel");//2
        mep_menu.agregarSubMenu("REPORTES");
        mep_menu.agregarItem("Consulta por Impuesto", "dibujarConsulta", "ui-icon-bookmark");//4
        mep_menu.agregarItem("Consulta Consolidada", "dibujarConsolidado", "ui-icon-calendar");//5
        mep_menu.agregarSubMenu("COMPROBANTES RETENCIONES EN VENTAS");
        mep_menu.agregarItem("Listado de RetencionesVentas", "dibujarListadoVentas", "ui-icon-bookmark");//3
        agregarComponente(mep_menu);
        dibujarListado();

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_rep.setId("sel_rep");
        agregarComponente(rep_reporte);
        agregarComponente(sel_rep);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular el Comprobante de Retención Seleccionado ?");
        con_confirma.setTitle("ANULAR COMPROBANTE DE RETENCIÓN");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);

        ret_retencion.setId("ret_retencion");
        ret_retencion.getBot_aceptar().setMetodo("guardar");
        agregarComponente(ret_retencion);

        dia_correo.setId("dia_correo");
        dia_correo.setTitle("REENVIAR RETENCIÓN ELECTRONICA AL CLIENTE");
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

    }

    public void dibujarConsolidado() {
        Grupo gru_grupo = new Grupo();

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setHeader("RETENCION EN LA FUENTE DE IMPUESTO A LA RENTA");
        tab_tabla.setSql(ser_retencion.getSqlConsolidadoRenta(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setLectura(true);
        tab_tabla.setColumnaSuma("BASE_IMPONIBLE,valor_retenido");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("RETENCION EN LA FUENTE DE IVA");
        tab_tabla1.setSql(ser_retencion.getSqlConsolidadoIva(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("valor_retenido");
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        gru_grupo.getChildren().add(pat_panel);
        gru_grupo.getChildren().add(pat_panel1);

        mep_menu.dibujar(6, "fa fa-list-alt", "Consulta consolidada de retenciones.", gru_grupo, true);
    }

    public void dibujarConsulta() {
        Grupo gru_grupo = new Grupo();
        com_impuesto = new Combo();
        com_impuesto.setCombo(ser_retencion.getSqlComboImpuestos());
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_retencion.getSqlConsultaImpuestos(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_impuesto.getValue())));
        tab_tabla.setCampoPrimaria("ide_cndre");
        tab_tabla.getColumna("ide_cndre").setVisible(false);
        tab_tabla.getColumna("nombre_cncim").setFiltro(true);
        tab_tabla.getColumna("nombre_cncim").setNombreVisual("IMPUESTO");
        tab_tabla.getColumna("valor_cndre").setNombreVisual("VALOR");
        tab_tabla.getColumna("valor_cndre").alinearDerecha();
        tab_tabla.getColumna("base_cndre").setNombreVisual("BASE IMPONIBLE");
        tab_tabla.getColumna("base_cndre").alinearDerecha();
        tab_tabla.getColumna("NUMERO").setFiltroContenido();
        tab_tabla.getColumna("NUMERO").setLongitud(50);
        tab_tabla.getColumna("NUM_FACTURA").setFiltroContenido();
        tab_tabla.setColumnaSuma("valor_cndre,base_cndre");
        tab_tabla.setLectura(true);
        tab_tabla.setRows(25);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();

        Grid g1 = new Grid();
        g1.setColumns(3);
        g1.getChildren().add(new Etiqueta("IMPUESTO"));
        Boton btbus = new Boton();
        btbus.setValue("Consultar");
        btbus.setIcon("ui-icon-search");
        btbus.setMetodo("actualizarConsultar");

        g1.getChildren().add(com_impuesto);
        g1.getChildren().add(btbus);
        pat_panel.getChildren().add(g1);
        pat_panel.setPanelTabla(tab_tabla);

        gru_grupo.getChildren().add(pat_panel);

        mep_menu.dibujar(4, "fa fa-list", "Consulta detalla de retenciones por impuesto.", gru_grupo, true);
    }

    public void actualizarConsultar() {
        tab_tabla.setSql(ser_retencion.getSqlConsultaImpuestos(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_impuesto.getValue())));
        tab_tabla.ejecutarSql();

    }

    public void dibujarListadoVentas() {
        Grupo gru_grupo = new Grupo();

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Comprobante de Retención");
        bot_anular.setMetodo("abrirAnularRetencion");
        bar_menu.agregarComponente(bot_anular);

        gru_grupo.getChildren().add(bar_menu);
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_retencion.getSqlRetencionesVentas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cncre");
        tab_tabla.getColumna("ide_cncre").setVisible(false);
        tab_tabla.getColumna("ide_cnere").setVisible(false);
        tab_tabla.getColumna("ide_cccfa").setVisible(false);
        tab_tabla.getColumna("BASE_RENTA").alinearDerecha();
        tab_tabla.getColumna("BASE_IVA").alinearDerecha();
        tab_tabla.getColumna("NUMERO").setFiltroContenido();
        tab_tabla.getColumna("NUMERO").setLongitud(50);
        tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla.getColumna("NUM_FACTURA").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla.getColumna("fecha_emisi_cncre").setNombreVisual("FECHA");

        tab_tabla.getColumna("ide_cnccc").setVisible(false);
        tab_tabla.getColumna("RET_RENTA").alinearDerecha();
        tab_tabla.getColumna("RET_IVA").alinearDerecha();
        tab_tabla.getColumna("numero").setLongitud(25);
        tab_tabla.setLectura(true);
        tab_tabla.setColumnaSuma("RET_RENTA,RET_IVA,BASE_RENTA,BASE_IVA");

        tab_tabla.setRows(25);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(5, "COMPROBANTES DE RETENCIÓN EN VENTAS", gru_grupo);
    }

    public void dibujarListado() {
        Grupo gru_grupo = new Grupo();

        gri_dashboard.setId("gri_dashboard");
        gri_dashboard.setWidth("100%");
        gri_dashboard.setColumns(6);
        gru_grupo.getChildren().add(gri_dashboard);
        dibujarDashboard();

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular");
        bot_anular.setTitle("Anular Comprobante de Retención");
        bot_anular.setMetodo("abrirAnularRetencion");
        bot_anular.setIcon("ui-icon-cancel");
        bar_menu.agregarComponente(bot_anular);

        if (ser_retencion.isElectronica()) {
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
            bot_reenviar.setMetodo("reenviarComprobante");
            bot_reenviar.setIcon("ui-icon-mail-closed");
            bar_menu.agregarBoton(bot_reenviar);

        }

        gru_grupo.getChildren().add(bar_menu);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        if (ser_retencion.isElectronica()) {
            tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicas(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.getColumna("ide_srcom").setVisible(false);
        } else {
            tab_tabla.setSql(ser_retencion.getSqlRetenciones(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        }

        tab_tabla.setCampoPrimaria("ide_cncre");
        tab_tabla.getColumna("ide_cncre").setVisible(false);
        tab_tabla.getColumna("ide_cnere").setVisible(false);
        tab_tabla.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla.getColumna("BASE_IMPONIBLE").alinearDerecha();
        tab_tabla.getColumna("NUMERO").setFiltroContenido();
        tab_tabla.getColumna("NUMERO").setLongitud(50);
        tab_tabla.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla.getColumna("NUM_FACTURA").setFiltroContenido();
        tab_tabla.getColumna("PROVEEDOR").setFiltroContenido();
        tab_tabla.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla.getColumna("VALOR").alinearDerecha();
        tab_tabla.getColumna("numero").setLongitud(25);
        tab_tabla.setLectura(true);
        tab_tabla.setValueExpression("rowStyleClass", "fila.campos[1] eq '1' ? 'text-red' : null");

        tab_tabla.setRows(20);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "COMPROBANTES DE RETENCIÓN", gru_grupo);
    }

    public void abrirAnularRetencion() {
        if (tab_tabla.getValor("ide_cncre") != null) {
            if (mep_menu.getOpcion() == 1) {
                if (ser_retencion.isElectronica()) {

                    if (tab_tabla.getValor("estado") != null) {
                        if (!tab_tabla.getValor("estado").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) {
                            if (!tab_tabla.getValor("estado").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                                utilitario.agregarMensajeError("No se puede anular el Comprobante de Reteción seleccionado", "Solo se pueden anular  Comprobantes de Reteción en estado PENDIENTE y AUTORIZADO");
                                return;
                            }
                        }

                    }
                }
                con_confirma.getBot_aceptar().setMetodo("anularRetencion");
                con_confirma.dibujar();
            } else {
                con_confirma.getBot_aceptar().setMetodo("anularRetencionVenta");
                con_confirma.dibujar();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Comprobante de Retención", "");
        }

    }

    public void anularRetencionVenta() {
        if (tab_tabla.getValor("ide_cncre") != null) {
            ser_retencion.anularComprobanteRetencionVenta(tab_tabla.getValorSeleccionado());
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                tab_tabla.actualizar();
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Comprobante de Retención", "");
        }
    }

    public void anularRetencion() {
        if (tab_tabla.getValor("ide_cncre") != null) {
            ser_retencion.anularComprobanteRetencion(tab_tabla.getValorSeleccionado());
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                tab_tabla.actualizar();
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Comprobante de Retención", "");
        }
    }

    public void actualizarConsulta() {
        if (mep_menu.getOpcion() == 1) {
            if (ser_retencion.isElectronica()) {
                tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicas(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            } else {
                tab_tabla.setSql(ser_retencion.getSqlRetenciones(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            }
            tab_tabla.ejecutarSql();
            dibujarDashboard();
            utilitario.addUpdate("gri_dashboard");
        } else if (mep_menu.getOpcion() == 2) {
            tab_tabla.setSql(ser_retencion.getSqlRetencionesAnuladas(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 4) {
            actualizarConsultar();
        } else if (mep_menu.getOpcion() == 6) {
            tab_tabla.setSql(ser_retencion.getSqlConsolidadoRenta(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
            tab_tabla1.setSql(ser_retencion.getSqlConsolidadoIva(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla1.ejecutarSql();
        }
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Comprobante de Retención")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla.getValor("ide_cncre") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cncre", Long.parseLong(tab_tabla.getValor("ide_cncre")));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("Seleccione un Comprobante de Retención", "");
                }

            }
        } else if (rep_reporte.getReporteSelecionado().equals("Retenciones en Compras")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sec_rango_reporte.dibujar();
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.isFechasValidas()) {
                    parametro = new HashMap();
                    parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                    parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                    sec_rango_reporte.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                } else {
                    utilitario.agregarMensaje("Rango de Fechas no válido", "");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Retenciones en Ventas")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sec_rango_reporte.dibujar();
            } else if (sec_rango_reporte.isVisible()) {
                if (sec_rango_reporte.isFechasValidas()) {
                    parametro = new HashMap();
                    parametro.put("fecha_inicio", sec_rango_reporte.getFecha1());
                    parametro.put("fecha_fin", sec_rango_reporte.getFecha2());
                    sec_rango_reporte.cerrar();
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                } else {
                    utilitario.agregarMensaje("Rango de Fechas no válido", "");
                }
            }

        }
    }

    private void dibujarDashboard() {
        gri_dashboard.getChildren().clear();
        if (com_autoriza.getValue() != null) {
            if (ser_retencion.isElectronica()) {
                int num_pendientes = 0;
                int num_recibidas = 0;
                int num_rechazadas = 0;
                int num_devueltas = 0;
                int num_autorizadas = 0;
                int num_no_autorizadas = 0;
                TablaGenerica tg = utilitario.consultar(ser_comElec.getSqlTotalComprobantesPorEstado(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), TipoComprobanteEnum.COMPROBANTE_DE_RETENCION,com_autoriza.getValue() + ""));
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
        }
    }

    public void filtrarPendientes() {
        tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicasPorEstado(String.valueOf(com_autoriza.getValue()) + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.PENDIENTE));
        tab_tabla.ejecutarSql();
    }

    public void filtrarRecibidas() {
        tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicasPorEstado(String.valueOf(com_autoriza.getValue()) + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECIBIDA));
        tab_tabla.ejecutarSql();
    }

    public void filtrarDevueltas() {
        tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicasPorEstado(String.valueOf(com_autoriza.getValue()) + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.DEVUELTA));
        tab_tabla.ejecutarSql();
    }

    public void filtrarRechazadas() {
        tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicasPorEstado(String.valueOf(com_autoriza.getValue()) + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.RECHAZADO));
        tab_tabla.ejecutarSql();
    }

    public void filtrarAutorizadas() {
        tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicasPorEstado(String.valueOf(com_autoriza.getValue()) + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.AUTORIZADO));
        tab_tabla.ejecutarSql();
    }

    public void filtrarNoAutorizadas() {
        tab_tabla.setSql(ser_retencion.getSqlRetencionesElectronicasPorEstado(String.valueOf(com_autoriza.getValue()) + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), EstadoComprobanteEnum.NOAUTORIZADO));
        tab_tabla.ejecutarSql();
    }

    public void dibujarAnuladas() {
        Grupo gru_grupo = new Grupo();
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_retencion.getSqlRetencionesAnuladas(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cncre");
        tab_tabla.getColumna("ide_cncre").setVisible(false);
        tab_tabla.getColumna("numero").setLongitud(25);
        tab_tabla.setLectura(true);
        tab_tabla.setRows(25);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        gru_grupo.getChildren().add(pat_panel);

        mep_menu.dibujar(2, "fa fa-file-excel-o", "Listado de Retenciones Anuladas.", gru_grupo, true);
    }

    public void abrirRIDE() {
        if (tab_tabla.getValor("ide_cncre") != null) {
            if (tab_tabla.getValor("ide_srcom") != null) {
                if (tab_tabla.getValor("ESTADO") != null && !tab_tabla.getValor("ESTADO").equals(EstadoComprobanteEnum.ANULADO.getDescripcion())) {
                    ret_retencion.visualizarRide(tab_tabla.getValor("ide_srcom"));
                } else {
                    utilitario.agregarMensajeError("No se puede Visualizar el Comprobate", "El comprobante de Retención seleccionado se encuentara ANULADA");
                }
            } else {
                utilitario.agregarMensajeInfo("El comprobante de Retención seleccionado no es electrónico", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un comprobante de Retención", "");
        }
    }

    public void enviarSRI() {
        if (tab_tabla.getValor("ide_cncre") != null) {
            //Valida que se encuentre en estado PENDIENTE o RECIBIDA
            if ((tab_tabla.getValor("ESTADO")) != null && (tab_tabla.getValor("ESTADO").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) || tab_tabla.getValor("ESTADO").equals(EstadoComprobanteEnum.RECIBIDA.getDescripcion())) {
                String mensaje = ser_comElec.enviarComprobante(tab_tabla.getValor("AUTORIZACION"));

                String aux = tab_tabla.getValorSeleccionado();
                dibujarListado();
                tab_tabla.setFilaActual(aux);
                tab_tabla.calcularPaginaActual();

                if (mensaje.isEmpty()) {
                    String mensje = "<p> COMPROBANTE DE RETENCIÓN NRO. " + tab_tabla.getValor("NUMERO") + " ";
                    mensje += "</br>AMBIENTE : <strong>" + (utilitario.getVariable("p_sri_ambiente_comp_elect").equals("1") ? "PRUEBAS" : "PRODUCCIÓN") + "</strong></p>";  //********variable ambiente facturacion electronica                    
                    mensje += "<p>ESTADO : <strong>" + tab_tabla.getValor("ESTADO") + "</strong></p>";
                    mensje += "<p>NÚMERO DE AUTORIZACION : <span style='font-size:12px;font-weight: bold;'>" + tab_tabla.getValor("AUTORIZACION") + "</span> </p>";
                    men_factura.setMensajeExito("COMPROBANTE DE RETENCIÓN ELECTRÓNICO AUTORIZADO", mensje);
                    men_factura.dibujar();
                } else {
                    utilitario.agregarMensajeError(mensaje, "");
                }
            } else {
                utilitario.agregarMensajeInfo("El comprobante de Retención seleccionado no se encuentra en estado PENDIENTE o RECIBIDA", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un comprobante de Retención", "");
        }
    }

    public void reenviarComprobante() {
        //Valida que la factura este AUTORIZADA
        if (tab_tabla.getValor("ide_cncre") != null) {
            if (tab_tabla.getValor("ide_srcom") != null) {
                if (tab_tabla.getValor("ESTADO") != null && tab_tabla.getValor("ESTADO").equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                    dia_correo.dibujar();
                } else {
                    utilitario.agregarMensajeError("No se puede reenviar el Comprobate de Retención", "El Comprobate de Retención debe estar en estado AUTORIZADO");
                }
            } else {
                utilitario.agregarMensajeInfo("el Comprobate de Retención seleccionado no es electrónico", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Comprobate de Retención", "");
        }
    }

    public void aceptarReenviar() {
        if (utilitario.isCorreoValido(String.valueOf(tex_correo.getValue()))) {
            ser_comElec.reenviarComprobante(String.valueOf(tex_correo.getValue()), tab_tabla.getValor("ide_srcom"));
            dia_correo.cerrar();
            tex_correo.setValue(null);
        } else {
            utilitario.agregarMensajeError("Correo electrónico no válido", "");
        }
    }

    @Override
    public void insertar() {

    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public Retencion getRet_retencion() {
        return ret_retencion;
    }

    public void setRet_retencion(Retencion ret_retencion) {
        this.ret_retencion = ret_retencion;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
    }

}
