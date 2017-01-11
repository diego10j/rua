/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_prestamos;

import componentes.AsientoContable;
import componentes.FacturaCxC;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.contabilidad.TipoAsientoEnum;
import servicios.prestamo.ServicioPrestamo;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacomee
 */
public class pre_prestamos extends Pantalla {

    @EJB
    private final ServicioPrestamo ser_prestamo = (ServicioPrestamo) utilitario.instanciarEJB(ServicioPrestamo.class);
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    private final MenuPanel mep_menu = new MenuPanel();
    private AutoCompletar aut_prestamos = new AutoCompletar();

    private Tabla tab_tabla1;
    private Tabla tab_tabla2;
    private FacturaCxC fcc_factura = new FacturaCxC();
    private Radio rad_hace_factrua = new Radio();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_formato = new SeleccionFormatoReporte();

    private AsientoContable asc_asiento = new AsientoContable();

    public pre_prestamos() {

        bar_botones.quitarBotonsNavegacion();
        bar_botones.agregarReporte();

        bar_botones.agregarComponente(new Etiqueta("PRESTAMO :"));
        aut_prestamos.setId("aut_prestamos");
        aut_prestamos.setAutoCompletar(ser_prestamo.getSqlComboPrestamos());
        aut_prestamos.setSize(100);
        aut_prestamos.setAutocompletarContenido(); // no startWith para la busqueda
        aut_prestamos.setMetodoChangeRuta("pre_index.clase.seleccionarPrestamo");

        bar_botones.agregarComponente(aut_prestamos);
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-cancel");
        bot_clean.setTitle("Limpiar");
        bot_clean.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_clean);

        mep_menu.setMenuPanel("OPCIONES PRESTAMOS", "20%");
        mep_menu.agregarItem("Información Prestamo", "dibujarPrestamo", "ui-icon-contact");
        mep_menu.agregarItem("Tabla de Amortización", "dibujarTabla", "ui-icon-calculator");
        mep_menu.agregarItem("Pagar Prestamo", "dibujarPagar", "ui-icon-check");
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarCuotasNoContabilizadas", "ui-icon-notice");//5        
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Transacciones Realizadas", "dibujarTransacciones", "ui-icon-calculator");
        mep_menu.agregarItem("Listado de Prestamos", "dibujarListaPrestamos", "ui-icon-note");

        agregarComponente(mep_menu);

        fcc_factura.setId("fcc_factura");
        fcc_factura.getBot_aceptar().setMetodo("guardar");
        fcc_factura.setTitle("FACTURA DE PRESTAMOS");
        agregarComponente(fcc_factura);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_formato.setId("sel_formato");
        agregarComponente(rep_reporte);
        agregarComponente(sel_formato);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardarAsiento");
        agregarComponente(asc_asiento);

    }

    public void guardarAsiento() {
        if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {
                utilitario.getConexion().agregarSqlPantalla("update iyp_deta_prestamo set ide_cnccc=" + asc_asiento.getIde_cnccc() + " where ide_ipdpr=" + tab_tabla1.getValor("ide_ipdpr"));
                utilitario.getConexion().agregarSqlPantalla("update cxc_cabece_factura set ide_cnccc =" + asc_asiento.getIde_cnccc() + " where ide_cccfa=" + tab_tabla1.getValor("ide_cccfa"));
                if (guardarPantalla().isEmpty()) {
                    dibujarCuotasNoContabilizadas();
                }
            }
        }
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla1.getValor("ide_cccfa") != null) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.setAsientoPrestamo(tab_tabla1.getValor("ide_cccfa"));
            asc_asiento.getBot_aceptar().setMetodo("guardarAsiento");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Cuota", "");
        }
    }

    public void dibujarCuotasNoContabilizadas() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_prestamo.getSqCuotasNoContabilizadas());
        tab_tabla1.setCampoPrimaria("ide_ipdpr");
        tab_tabla1.getColumna("ide_cccfa").setVisible(false);
        tab_tabla1.getColumna("ide_ipdpr").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
        tab_tabla1.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla1.getColumna("secuencial_cccfa").setNombreVisual("N.FACTURA");
        tab_tabla1.getColumna("num_prestamo_ipcpr").setNombreVisual("N.PRESTAMO");
        tab_tabla1.getColumna("num_ipdpr").setNombreVisual("N.CUOTA");

        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(5, "CUOTAS PAGADAS NO CONTABILIZADAS", gru);
    }

    public void dibujarTransacciones() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_prestamo.getSqMovimientosRealizados());
        tab_tabla1.setCampoPrimaria("ide_ipdpr");
        tab_tabla1.getColumna("ide_cccfa").setVisible(false);
        tab_tabla1.getColumna("ide_ipdpr").setVisible(false);
        tab_tabla1.getColumna("ide_geper").setVisible(false);
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
        tab_tabla1.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_tabla1.getColumna("secuencial_cccfa").setNombreVisual("N.FACTURA");
        tab_tabla1.getColumna("num_prestamo_ipcpr").setNombreVisual("N.PRESTAMO");
        tab_tabla1.getColumna("num_ipdpr").setNombreVisual("N.CUOTA");
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(5, "CUOTAS PAGADAS NO CONTABILIZADAS", pat_panel);
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

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
        Map parametro = new HashMap();
        if (rep_reporte.getReporteSelecionado().equals("Facturas") || rep_reporte.getReporteSelecionado().equals("Facturas Eventos")
                || rep_reporte.getReporteSelecionado().equals("Facturas Nueva") || rep_reporte.getReporteSelecionado().equals("Facturas con Formato")) {
            if (mep_menu.getOpcion() == 2) { //Valida que se seleccione una factura
                if (rep_reporte.isVisible()) {
                    if (tab_tabla2.getValor("ide_cccfa") != null) {
                        rep_reporte.cerrar();
                        parametro.put("ide_cccfa", Long.parseLong(tab_tabla2.getValor("ide_cccfa")));
                        sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_formato.dibujar();
                        utilitario.addUpdate("rep_reporte,sel_rep");
                    } else {
                        utilitario.agregarMensajeInfo("Seleccionar Factura", "El dividendo seleccionado no tiene Factura");
                    }

                } else {
                    utilitario.agregarMensajeInfo("Seleccionar Factura", "Debe seleccionar un dividendo de la Tabla de Amortización");
                }
            }

        } else if (rep_reporte.getReporteSelecionado().equals("Listado de Prestamos por Entidad Bancaria")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                sel_formato.setSeleccionFormatoReporte(null, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        } else if (rep_reporte.getReporteSelecionado().equals("Tabla de Amortizacion")) {
            if (rep_reporte.isVisible()) {
                parametro = new HashMap();
                System.out.println("si entra");
                rep_reporte.cerrar();
                parametro.put("ide_ipcpr", Long.parseLong(tab_tabla1.getValor("ide_ipcpr")));
                parametro.put("es_ingreso_ipcpr", Boolean.parseBoolean(tab_tabla1.getValor("es_ingreso_ipcpr")));
                System.out.println(tab_tabla1.getValor("es_ingreso_ipcpr"));
                sel_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_formato.dibujar();
                utilitario.addUpdate("rep_reporte,sel_formato");
            }
        }
    }

    /**
     * Selecciona un prestamo en el autocompletar
     *
     * @param evt
     */
    public void seleccionarPrestamo(SelectEvent evt) {
        aut_prestamos.onSelect(evt);
        if (aut_prestamos != null) {
            switch (mep_menu.getOpcion()) {
                case 1:
                    dibujarPrestamo();
                    break;
                case 2:
                    dibujarTabla();
                    break;
                case 3:
                    dibujarListaPrestamos();
                    break;
                case 4:
                    dibujarPagar();
                    break;
                default:
                    dibujarPrestamo();
            }
        } else {
            limpiar();
        }
    }

    /**
     * Limpia la pantalla y el autocompletar
     */
    public void limpiar() {
        aut_prestamos.limpiar();
        mep_menu.limpiar();
    }

    public void dibujarPrestamo() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("iyp_cab_prestamo", "ide_ipcpr", 1);
        tab_tabla1.setCondicion("ide_ipcpr=-1");
        tab_tabla1.getColumna("ide_iptpr").setCombo("iyp_tipo_prestamo", "ide_iptpr", "nombre_iptpr", "");
        tab_tabla1.getColumna("ide_iptpr").setValorDefecto(utilitario.getVariable("p_iyp_tipo_prestamo"));
        tab_tabla1.getColumna("ide_ipepr").setCombo("iyp_estado_prestamos", "ide_ipepr", "nombre_ipepr", "");
        tab_tabla1.getColumna("ide_ipepr").setValorDefecto(utilitario.getVariable("p_iyp_estado_normal"));
        tab_tabla1.getColumna("ide_cndpc").setCombo(ser_contabilidad.getSqlCuentasHijas());
        tab_tabla1.getColumna("ide_cndpc").setAutoCompletar();
        tab_tabla1.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_tabla1.getColumna("FECHA_PRESTAMO_IPCPR").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_transaccion_ipcpr").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_transaccion_ipcpr").setVisible(false);
        tab_tabla1.getColumna("ide_tecba").setCombo("select tes_cuenta_banco.ide_tecba,tes_banco.nombre_teban,tes_cuenta_banco.nombre_tecba from  tes_banco,tes_cuenta_banco,sis_empresa where tes_banco.ide_teban=tes_cuenta_banco.ide_teban and sis_empresa.ide_empr=" + utilitario.getVariable("ide_empr") + " and tes_cuenta_banco.ide_sucu=" + utilitario.getVariable("ide_sucu"));
        tab_tabla1.getColumna("ide_usua").setVisible(false);
        tab_tabla1.getColumna("ide_tecba").setRequerida(true);
        tab_tabla1.getColumna("monto_ipcpr").setRequerida(true);
        tab_tabla1.getColumna("ide_cndpc").setRequerida(true);
        tab_tabla1.getColumna("interes_ipcpr").setRequerida(true);
        tab_tabla1.getColumna("num_pagos_ipcpr").setRequerida(true);
        tab_tabla1.getColumna("ide_ipepr").setRequerida(true);
        tab_tabla1.getColumna("ide_iptpr").setRequerida(true);
        tab_tabla1.getColumna("num_dias_ipcpr").setRequerida(true);
        tab_tabla1.getColumna("observacion_ipcpr").setRequerida(true);
        tab_tabla1.getColumna("fecha_sistema_ipcpr").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("hora_sistema_ipcpr").setValorDefecto(utilitario.getHoraActual());
        tab_tabla1.getColumna("fecha_sistema_ipcpr").setVisible(false);
        tab_tabla1.getColumna("hora_sistema_ipcpr").setVisible(false);
        tab_tabla1.getColumna("genera_asiento_ipcpr").setValorDefecto("true");
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.getGrid().setColumns(4);
        List lista = new ArrayList();
        Object fila1[] = {
            true, "INGRESO"
        };
        Object fila2[] = {
            false, "EGRESO"
        };
        lista.add(fila1);
        lista.add(fila2);
        tab_tabla1.getColumna("es_ingreso_ipcpr").setCombo(lista);
        tab_tabla1.getColumna("es_ingreso_ipcpr").setOrden(2);
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_geper").setRequerida(true);
        tab_tabla1.getColumna("ide_geper").setMetodoChange("seleccionaBeneficiario");

        tab_tabla1.getColumna("fecha_sistema_ipcpr").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("hora_sistema_ipcpr").setValorDefecto(utilitario.getHoraActual());
        tab_tabla1.getColumna("fecha_sistema_ipcpr").setVisible(false);
        tab_tabla1.getColumna("hora_sistema_ipcpr").setVisible(false);
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.setCondicion("ide_ipcpr=" + aut_prestamos.getValor());
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(1, "DATOS DEL PRESTAMO", pat_panel);

    }

    public void seleccionaBeneficiario(SelectEvent evt) {
        tab_tabla1.setValor("observacion_ipcpr", tab_tabla1.getValorArreglo("ide_geper", 1));
        utilitario.addUpdateTabla(tab_tabla1, "observacion_ipcpr", "");
    }

    public void dibujarTabla() {
        Grupo gru_grupo = new Grupo();
        if (isPrestamoSeleccionado()) {
            tab_tabla2 = new Tabla();
            tab_tabla2.setId("tab_tabla2");
            tab_tabla2.setNumeroTabla(-1);
            tab_tabla2.setSql(ser_prestamo.getSqlTablaAmortizacion(aut_prestamos.getValor()));
            tab_tabla2.setLectura(true);
            tab_tabla2.getColumna("ide_ipdpr").setVisible(false);
            tab_tabla2.getColumna("ide_cccfa").setVisible(false);
            tab_tabla2.getColumna("fecha_ipdpr").setNombreVisual("fecha");
            tab_tabla2.getColumna("fecha_prestamo_ipcpr").setVisible(false);
            tab_tabla2.getColumna("monto_ipcpr").setVisible(false);
            tab_tabla2.getColumna("num_dias_ipcpr").setVisible(false);
            tab_tabla2.getColumna("interes_ipcpr").setVisible(false);
            tab_tabla2.getColumna("capital").alinearDerecha();
            tab_tabla2.getColumna("interes").alinearDerecha();
            tab_tabla2.getColumna("cuota").alinearDerecha();
            tab_tabla2.getColumna("banco").setLongitud(50);
            tab_tabla2.getColumna("banco").setVisible(false);////!!!!!CAMBIAR
            tab_tabla2.getColumna("documento").setVisible(false);////!!!!CAMBIAR
            tab_tabla2.setColumnaSuma("capital,interes,cuota");
            tab_tabla2.setValueExpression("rowStyleClass", "fila.campos[6] eq 'false' ? 'text-green' :  null");
            tab_tabla2.setScrollable(true);
            tab_tabla2.setOrdenar(false);
            tab_tabla2.setRows(999);
            tab_tabla2.setScrollHeight(400);
            tab_tabla2.getColumna("ide_cnccc").setFiltroContenido();
            tab_tabla2.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
            tab_tabla2.getColumna("IDE_CNCCC").setLink();
            tab_tabla2.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
            tab_tabla2.getColumna("IDE_CNCCC").alinearCentro();
            tab_tabla2.dibujar();
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla2);
            PanelGrid pgrid = new PanelGrid();
            pgrid.setColumns(8);
            pgrid.setStyle("width:100%;");
            pgrid.getFacets().put("header", new Etiqueta(aut_prestamos.getValorArreglo(1)));
            pgrid.getChildren().add(new Etiqueta("<strong>FECHA DEL PRESTAMO :</strong>"));
            pgrid.getChildren().add(new Etiqueta(utilitario.getFormatoFecha(utilitario.getFecha(tab_tabla2.getValor("fecha_prestamo_ipcpr")), "dd-MM-yyyy")));
            pgrid.getChildren().add(new Etiqueta("<strong>MONTO :</strong>"));
            pgrid.getChildren().add(new Etiqueta(tab_tabla2.getValor("monto_ipcpr")));
            pgrid.getChildren().add(new Etiqueta("<strong>NUM DIAS PLAZO :</strong>"));
            pgrid.getChildren().add(new Etiqueta(tab_tabla2.getValor("num_dias_ipcpr")));
            pgrid.getChildren().add(new Etiqueta("<strong>% INTERES :</strong>"));
            pgrid.getChildren().add(new Etiqueta(tab_tabla2.getValor("interes_ipcpr")));
            gru_grupo.getChildren().add(pgrid);
            gru_grupo.getChildren().add(new Separator());
            gru_grupo.getChildren().add(pat_panel);
        }
        mep_menu.dibujar(2, "TABLA DE AMORTIZACION", gru_grupo);
    }

    public void dibujarListaPrestamos() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_prestamo.getSqlPrestamos());
        tab_tabla1.getColumna("ide_ipcpr").setVisible(false);
        tab_tabla1.getColumna("tipo").setLongitud(10);
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
        tab_tabla1.getColumna("fecha_prestamo_ipcpr").setNombreVisual("FECHA");
        tab_tabla1.getColumna("num_prestamo_ipcpr").setNombreVisual("NUM. PRESTAMO");
        tab_tabla1.getColumna("num_prestamo_ipcpr").setFiltroContenido();
        tab_tabla1.getColumna("monto_ipcpr").setNombreVisual("MONTO");
        tab_tabla1.getColumna("monto_ipcpr").alinearDerecha();
        tab_tabla1.getColumna("interes_ipcpr").setNombreVisual("% INTERES");
        tab_tabla1.getColumna("interes_ipcpr").alinearDerecha();
        tab_tabla1.getColumna("saldo").alinearDerecha();
        tab_tabla1.setColumnaSuma("saldo,capital,interes");
        tab_tabla1.getColumna("num_pagos_ipcpr").setNombreVisual("NUM. PAGOS");
        tab_tabla1.getColumna("num_pagos_ipcpr").alinearDerecha();
        tab_tabla1.getColumna("pagos").setNombreVisual("PAGADOS");
        tab_tabla1.getColumna("pagos").alinearDerecha();
        tab_tabla1.getColumna("valor_pagado").setNombreVisual("VALOR PAGADO");
        tab_tabla1.getColumna("valor_pagado").alinearDerecha();
        tab_tabla1.getColumna("capital").alinearDerecha();
        tab_tabla1.getColumna("interes").alinearDerecha();
        tab_tabla1.getColumna("cuota").alinearDerecha();
        tab_tabla1.getColumna("fecha_ultimo_pago").setNombreVisual("FECHA ULT. PAGO");
        tab_tabla1.setOrdenar(false);
        tab_tabla1.setNumeroTabla(-1);
        tab_tabla1.setRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        mep_menu.dibujar(3, "LISTADO PRESTAMOS", pat_panel);

    }

    public void dibujarPagar() {
        Grupo gru_grupo = new Grupo();
        if (isPrestamoSeleccionado()) {
            tab_tabla1 = new Tabla();
            tab_tabla1.setId("tab_tabla1");
            tab_tabla1.setSql(ser_prestamo.getSqlPagosPrestamo(aut_prestamos.getValor()));
            tab_tabla1.setCampoPrimaria("ide_ipdpr");
            tab_tabla1.getColumna("ide_ipdpr").setVisible(false);
            tab_tabla1.getColumna("ide_ipcpr").setVisible(false);
            tab_tabla1.getColumna("ide_ipcpr").setEtiqueta();
            tab_tabla1.getColumna("num_ipdpr").setEtiqueta();
            tab_tabla1.getColumna("fecha_ipdpr").setEtiqueta();
            tab_tabla1.getColumna("capital_ipdpr").setEtiqueta();
            tab_tabla1.getColumna("interes_ipdpr").setEtiqueta();
            tab_tabla1.getColumna("cuota_ipdpr").setEtiqueta();
            tab_tabla1.getColumna("ide_cndpc").setVisible(false);
            tab_tabla1.getColumna("ide_ipcpr").setLectura(false);
            tab_tabla1.getColumna("num_ipdpr").setNombreVisual("NUMERO");
            tab_tabla1.getColumna("fecha_ipdpr").setNombreVisual("FECHA");
            tab_tabla1.getColumna("capital_ipdpr").setNombreVisual("CAPITAL");
            tab_tabla1.getColumna("interes_ipdpr").setNombreVisual("INTERES");
            tab_tabla1.getColumna("cuota_ipdpr").setNombreVisual("CUOTA");
            tab_tabla1.getColumna("pagado_ipdpr").setNombreVisual("PAGAR");
            tab_tabla1.getColumna("fecha_prestamo_ipcpr").setVisible(false);
            tab_tabla1.getColumna("monto_ipcpr").setVisible(false);
            tab_tabla1.getColumna("num_dias_ipcpr").setVisible(false);
            tab_tabla1.getColumna("interes_ipcpr").setVisible(false);
            tab_tabla1.getColumna("ide_geper").setVisible(false);
            tab_tabla1.setNumeroTabla(-1);
            tab_tabla1.setScrollable(true);
            tab_tabla1.setScrollHeight(270);
            tab_tabla1.dibujar();
            tab_tabla1.setRows(999);
            PanelTabla pat_panel = new PanelTabla();
            pat_panel.setPanelTabla(tab_tabla1);
            pat_panel.setMensajeInfo("Seleccione los dividendos que desea pagar");
            pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
            PanelGrid pgrid = new PanelGrid();
            pgrid.setColumns(8);
            pgrid.setStyle("width:100%;");
            pgrid.getFacets().put("header", new Etiqueta(aut_prestamos.getValorArreglo(1)));
            pgrid.getChildren().add(new Etiqueta("<strong>FECHA DEL PRESTAMO :</strong>"));
            pgrid.getChildren().add(new Etiqueta(utilitario.getFormatoFecha(utilitario.getFecha(tab_tabla1.getValor("fecha_prestamo_ipcpr")), "dd-MM-yyyy")));
            pgrid.getChildren().add(new Etiqueta("<strong>MONTO :</strong>"));
            pgrid.getChildren().add(new Etiqueta(tab_tabla1.getValor("monto_ipcpr")));
            pgrid.getChildren().add(new Etiqueta("<strong>NUM DIAS PLAZO :</strong>"));
            pgrid.getChildren().add(new Etiqueta(tab_tabla1.getValor("num_dias_ipcpr")));
            pgrid.getChildren().add(new Etiqueta("<strong>% INTERES :</strong>"));
            pgrid.getChildren().add(new Etiqueta(tab_tabla1.getValor("interes_ipcpr")));
            gru_grupo.getChildren().add(pgrid);
            gru_grupo.getChildren().add(new Separator());
            gru_grupo.getChildren().add(new Etiqueta("<div align='center'>"));
            gru_grupo.getChildren().add(pat_panel);

            Grid gri = new Grid();
            gri.setColumns(2);
            gri.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR FACTURA SOBRE EL INTERES ? </div>"));
            rad_hace_factrua.setRadio(utilitario.getListaPregunta());
            rad_hace_factrua.setValue(true);
            gri.getChildren().add(rad_hace_factrua);
            gru_grupo.getChildren().add(gri);
            Boton bot_pagar = new Boton();
            bot_pagar.setValue("Aceptar");
            bot_pagar.setMetodo("pagarPrestamo");
            bot_pagar.setIcon("ui-icon-check");
            gru_grupo.getChildren().add(bot_pagar);
            gru_grupo.getChildren().add(new Etiqueta("</div>"));
        }
        mep_menu.dibujar(4, "PAGAR PRESTAMO", gru_grupo);
    }

    /**
     * Validacion para que se seleccione un prestamo del Autocompletar
     *
     * @return
     */
    private boolean isPrestamoSeleccionado() {
        if (aut_prestamos.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Préstamo", "Seleccione un préstamo de la lista del Autocompletar");
            return false;
        }
        return true;
    }

    public void pagarPrestamo() {
        double dou_interes = 0;
        String cuotas = "";
        String pagados = "";
        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            if (tab_tabla1.getValor(i, "pagado_ipdpr").equalsIgnoreCase("true")) {
                dou_interes += Double.parseDouble(tab_tabla1.getValor(i, "interes_ipdpr"));
                if (!cuotas.isEmpty()) {
                    cuotas += ", ";
                }
                cuotas += "" + tab_tabla1.getValor(i, "num_ipdpr");
                if (!pagados.isEmpty()) {
                    pagados += ", ";
                }
                pagados += "" + tab_tabla1.getValor(i, "ide_ipdpr");
            }
        }
        if (rad_hace_factrua.getValue().toString().equals("true")) {
            if (dou_interes > 0) {
                fcc_factura.nuevaFactura();
                fcc_factura.setCliente(tab_tabla1.getValor("ide_geper"));
                fcc_factura.setObservacion("Préstamo cuota " + cuotas);
                fcc_factura.getTab_cab_factura().setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(dou_interes));
                fcc_factura.getTab_cab_factura().setValor("total_cccfa", utilitario.getFormatoNumero(dou_interes));
                //Detalle
                fcc_factura.getTab_deta_factura().insertar();
                fcc_factura.getTab_deta_factura().setValor("cantidad_ccdfa", "1");
                fcc_factura.getTab_deta_factura().setValor("precio_ccdfa", utilitario.getFormatoNumero(dou_interes));
                fcc_factura.getTab_deta_factura().setValor("total_ccdfa", utilitario.getFormatoNumero(dou_interes));
                fcc_factura.getTab_deta_factura().setValor("observacion_ccdfa", "Préstamo cuota " + cuotas);
                fcc_factura.getTab_deta_factura().setValor("ide_inarti", utilitario.getVariable("p_iyp_aporte_deta_factura"));
                fcc_factura.getTab_deta_factura().setValor("iva_inarti_ccdfa", "-1");//no iva
                fcc_factura.calcularTotalFactura();
                fcc_factura.dibujar();
            } else {
                utilitario.agregarMensajeError("Seleccionar dividendos", "El valor a facturar debe ser mayor a 0");
            }
        } else {
            //no hace factura solo cambia estado a pagado los seleccionados
            utilitario.getConexion().getSqlPantalla().clear();
            if (pagados.isEmpty() == false) {
                utilitario.getConexion().agregarSql(ser_prestamo.getSqlPagarDividendos(pagados, null));
            }
            if (guardarPantalla().isEmpty()) {
                tab_tabla1.ejecutarSql();
            }
        }

    }

    @Override
    public void insertar() {
        aut_prestamos.limpiar();
        //FORMULARIO PRESTAMO
        dibujarPrestamo();
        tab_tabla1.limpiar();
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            //Generar Tabla de Amortizacion
            if (tab_tabla1.isFilaInsertada()) {
                if (validarPrestamo()) {
                    ser_prestamo.generarTablaAmortizacion(tab_tabla1);
                    utilitario.agregarMensaje("Se generó la Tabla de Amortización", "");
                } else {
                    return;
                }
            } else if (tab_tabla1.isFilaModificada()) {
                if (!validarPrestamo()) {
                    return;
                }
            }
            if (tab_tabla1.guardar()) {
                if (guardarPantalla().isEmpty()) {
                    aut_prestamos.actualizar();
                    aut_prestamos.setSize(100);
                    aut_prestamos.setValor(tab_tabla1.getValor("ide_ipcpr"));
                    utilitario.addUpdate("aut_prestamos");
                }
            }

        }
        if (mep_menu.getOpcion() == 4) {
            if (fcc_factura.isVisible()) {
                //Cambiar de estado a pagado las dividendos seleccionados
                String pagados = "";
                String seleccionado = "";
                for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
                    if (tab_tabla1.getValor(i, "pagado_ipdpr").equalsIgnoreCase("true")) {
                        if (!pagados.isEmpty()) {
                            pagados += ", ";
                        }
                        pagados += "" + tab_tabla1.getValor(i, "ide_ipdpr");
                        seleccionado = tab_tabla1.getValor(i, "ide_ipdpr");
                    }
                }
                fcc_factura.guardar();
                if (fcc_factura.isVisible() == false) {
                    //GUARDO LA FACTURA SIN ERRORES
                    if (pagados.isEmpty() == false) {
                        String ide_cccfa = fcc_factura.getTab_cab_factura().getValor("ide_cccfa");
                        if (ide_cccfa != null) {
                            utilitario.getConexion().ejecutarSql(ser_prestamo.getSqlPagarDividendos(pagados, ide_cccfa));
                        }
                    }
                    if (seleccionado.isEmpty() == false) {
                        dibujarTabla();
                        tab_tabla2.setFilaActual(seleccionado);
                    }
                }
            }
        }
    }

    public boolean validarPrestamo() {
        if (tab_tabla1.getValor("ide_tecba") == null || tab_tabla1.getValor("ide_tecba").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe seleccionar la Entidad Bancaria");
            return false;
        }
        if (tab_tabla1.getValor("monto_ipcpr") == null || tab_tabla1.getValor("monto_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar el Monto del Prestamo");
            return false;
        }
        if (tab_tabla1.getValor("interes_ipcpr") == null || tab_tabla1.getValor("interes_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe ingresar el Interes");
            return false;
        }
        if (tab_tabla1.getValor("ide_cndpc") == null || tab_tabla1.getValor("ide_cndpc").isEmpty()) {
            utilitario.agregarMensajeInfo("No se pudo guardar", "Debe Ingresar  la Cuenta Contable");
            return false;
        }
        if (tab_tabla1.getValor("num_pagos_ipcpr") == null || tab_tabla1.getValor("num_pagos_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el numero de pagos");
            return false;
        }
        if (tab_tabla1.getValor("ide_iptpr") == null || tab_tabla1.getValor("ide_iptpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe seleccionar el Tipo de Prestamo");
            return false;
        }
        if (tab_tabla1.getValor("ide_ipepr") == null || tab_tabla1.getValor("ide_ipepr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe seleccionar el estado del Prestamo");
            return false;
        }
        if (tab_tabla1.getValor("num_dias_ipcpr") == null || tab_tabla1.getValor("num_dias_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el numero de Dias ");
            return false;
        }
        if (tab_tabla1.getValor("observacion_ipcpr") == null || tab_tabla1.getValor("observacion_ipcpr").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la Observación");
            return false;
        }
        if (tab_tabla1.getValor("ide_geper") == null || tab_tabla1.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar el Beneficiario");
            return false;
        }
        return true;
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFilaInsertada()) {
            tab_tabla1.eliminar();
        } else {
            ser_prestamo.eliminarPrestamo(tab_tabla1.getValor("ide_ipcpr"));
            if (guardarPantalla().isEmpty()) {
                aut_prestamos.actualizar();
                aut_prestamos.setSize(100);
                limpiar();
            }
        }
    }

    public AutoCompletar getAut_prestamos() {
        return aut_prestamos;
    }

    public void setAut_prestamos(AutoCompletar aut_prestamos) {
        this.aut_prestamos = aut_prestamos;
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

    public FacturaCxC getFcc_factura() {
        return fcc_factura;
    }

    public void setFcc_factura(FacturaCxC fcc_factura) {
        this.fcc_factura = fcc_factura;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_formato() {
        return sel_formato;
    }

    public void setSel_formato(SeleccionFormatoReporte sel_formato) {
        this.sel_formato = sel_formato;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

}
