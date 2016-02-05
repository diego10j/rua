/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.FacturaCxC;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;

import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.graficos.GraficoCartesiano;
import framework.componentes.graficos.GraficoPastel;
import javax.ejb.EJB;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_facturasCxC extends Pantalla {
    
    private final MenuPanel mep_menu = new MenuPanel();
    
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);
    
    private final Combo com_pto_emision = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    
    private FacturaCxC fcc_factura = new FacturaCxC();
    
    private Tabla tab_facturas;
    private Tabla tab_facturas_no_conta;
    private Tabla tab_facturas_anuladas;
    private Tabla tab_facturas_x_cobrar;
    
    private GraficoCartesiano gca_facturas;
    private GraficoPastel gpa_facturas;
    private Tabla tab_datos_grafico;
    private Combo com_periodo;
    
    private Tabla tab_rep_ventas;
    private Combo com_mes;
    
    public pre_facturasCxC() {
        
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        com_pto_emision.setMetodo("actualizarFacturas");
        com_pto_emision.eliminarVacio();
        bar_botones.agregarComponente(new Etiqueta("FACTURACIÓN:"));
        bar_botones.agregarComponente(com_pto_emision);
        
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);
        
        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarFacturas");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);
        
        fcc_factura.setId("fcc_factura");
        fcc_factura.setFacturaCxC("GENERAR FACTURA DE VENTA");
        agregarComponente(fcc_factura);
        
        mep_menu.setMenuPanel("OPCIONES FACTURA", "20%");
        mep_menu.agregarItem("Listado de Facturas", "dibujarFacturas", "ui-icon-note");
        mep_menu.agregarItem("Facturas No Contabilizadas", "dibujarFacturasNoContabilizadas", "ui-icon-notice");
        mep_menu.agregarItem("Facturas Anuladas", "dibujarFacturasAnuladas", "ui-icon-cancel");
        mep_menu.agregarItem("Facturas Por Cobrar", "dibujarFacturasPorCobrar", "ui-icon-calculator");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Grafico de Ventas", "dibujarGraficoVentas", "ui-icon-clock");
        mep_menu.agregarItem("Estadística de Ventas", "dibujarEstadisticas", "ui-icon-bookmark");
        mep_menu.agregarItem("Reporte de Ventas", "dibujarReporteVentas", "ui-icon-calendar");
        mep_menu.agregarSubMenu("FACTURACIÓN ELECTRÓNICA");
        mep_menu.agregarItem("Facturas Eléctrónicas", "dibujarFacturas", "ui-icon-signal-diag");
        
        agregarComponente(mep_menu);
        dibujarFacturas();
    }
    
    public void dibujarFacturas() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Factura");
        bot_ver.setMetodo("abrirVerFactura");
        bar_menu.agregarComponente(bot_ver);
        
        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Factura");
        bar_menu.agregarComponente(bot_anular);
        
        Boton bot_asiento = new Boton();
        bot_asiento.setValue("Generar Asiento");
        bar_menu.agregarBoton(bot_asiento);
        
        Boton bot_retención = new Boton();
        bot_retención.setValue("Generar Retención");
        bar_menu.agregarBoton(bot_retención);
        
        tab_facturas = new Tabla();
        tab_facturas.setId("tab_facturas");
        tab_facturas.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas.setCampoPrimaria("ide_cccfa");
        tab_facturas.getColumna("ide_cccfa").setVisible(false);
        tab_facturas.getColumna("ide_ccefa").setVisible(false);
        tab_facturas.getColumna("nombre_ccefa").setFiltroContenido();
        tab_facturas.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas.getColumna("nom_geper").setFiltroContenido();
        tab_facturas.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas.getColumna("ide_cnccc").setFiltroContenido();
        tab_facturas.getColumna("ventas0").alinearDerecha();
        tab_facturas.getColumna("ventas12").alinearDerecha();
        tab_facturas.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas.getColumna("total_cccfa").alinearDerecha();
        tab_facturas.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas.setRows(15);
        tab_facturas.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_facturas.setValueExpression("rowStyleClass", "fila.campos[2] eq '" + utilitario.getVariable("p_cxc_estado_factura_anulada") + "' ? 'text-red' : fila.campos[13] eq null  ? 'text-green' : null");
        tab_facturas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "LISTADO DE FACTURAS", gru);
    }
    
    public void dibujarFacturasNoContabilizadas() {
        tab_facturas_no_conta = new Tabla();
        tab_facturas_no_conta.setId("tab_facturas_no_conta");
        tab_facturas_no_conta.setSql(ser_factura.getSqlFacturasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas_no_conta.setCampoPrimaria("ide_cccfa");
        tab_facturas_no_conta.getColumna("ide_cccfa").setVisible(false);
        tab_facturas_no_conta.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas_no_conta.getColumna("nom_geper").setFiltroContenido();
        tab_facturas_no_conta.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas_no_conta.getColumna("ventas0").alinearDerecha();
        tab_facturas_no_conta.getColumna("ventas12").alinearDerecha();
        tab_facturas_no_conta.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas_no_conta.getColumna("total_cccfa").alinearDerecha();
        tab_facturas_no_conta.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas_no_conta.setRows(15);
        tab_facturas_no_conta.setLectura(true);
        tab_facturas_no_conta.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas_no_conta);
        
        mep_menu.dibujar(2, "FACTURAS NO CONTABILIZADAS", pat_panel);
    }
    
    public void dibujarFacturasAnuladas() {
        tab_facturas_anuladas = new Tabla();
        tab_facturas_anuladas.setId("tab_facturas_anuladas");
        tab_facturas_anuladas.setSql(ser_factura.getSqlFacturasAnuladas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas_anuladas.setCampoPrimaria("ide_cccfa");
        tab_facturas_anuladas.getColumna("ide_cccfa").setVisible(false);
        tab_facturas_anuladas.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas_anuladas.getColumna("nom_geper").setFiltroContenido();
        tab_facturas_anuladas.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas_anuladas.getColumna("ventas0").alinearDerecha();
        tab_facturas_anuladas.getColumna("ventas12").alinearDerecha();
        tab_facturas_anuladas.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas_anuladas.getColumna("total_cccfa").alinearDerecha();
        tab_facturas_anuladas.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas_anuladas.getColumna("ide_cnccc").setFiltroContenido();
        tab_facturas_anuladas.setRows(15);
        tab_facturas_anuladas.setLectura(true);
        tab_facturas_anuladas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas_anuladas);
        
        mep_menu.dibujar(3, "FACTURAS ANULADAS", pat_panel);
    }
    
    public void dibujarFacturasPorCobrar() {
        tab_facturas_x_cobrar = new Tabla();
        tab_facturas_x_cobrar.setId("tab_facturas_x_cobrar");
        tab_facturas_x_cobrar.setSql(ser_factura.getSqlFacturasPorCobrar(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_facturas_x_cobrar.setCampoPrimaria("ide_cccfa");
        tab_facturas_x_cobrar.getColumna("ide_cccfa").setVisible(false);
        tab_facturas_x_cobrar.getColumna("secuencial_cccfa").setFiltroContenido();
        tab_facturas_x_cobrar.getColumna("nom_geper").setFiltroContenido();
        tab_facturas_x_cobrar.getColumna("identificac_geper").setFiltroContenido();
        tab_facturas_x_cobrar.getColumna("ventas0").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("ventas12").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("total_cccfa").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("total_cccfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_facturas_x_cobrar.getColumna("saldo_x_pagar").alinearDerecha();
        tab_facturas_x_cobrar.getColumna("saldo_x_pagar").setEstilo("font-size: 12px;font-weight: bold;color:red");
        tab_facturas_x_cobrar.getColumna("ide_cnccc").setFiltroContenido();
        tab_facturas_x_cobrar.setLectura(true);
        tab_facturas_x_cobrar.setRows(15);
        tab_facturas_x_cobrar.setColumnaSuma("total_cccfa,saldo_x_pagar");
        tab_facturas_x_cobrar.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas_x_cobrar);
        
        mep_menu.dibujar(4, "FACTURAS POR COBRAR", pat_panel);
    }
    
    public void dibujarGraficoVentas() {
        Grupo grupo = new Grupo();
        gca_facturas = new GraficoCartesiano();
        gca_facturas.setId("gca_facturas");
        
        gpa_facturas = new GraficoPastel();
        gpa_facturas.setId("gpa_facturas");
        gpa_facturas.setShowDataLabels(true);
        gpa_facturas.setStyle("width:300px;");
        
        com_periodo = new Combo();
        com_periodo.setMetodo("actualizarFacturas");
        com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
        com_periodo.eliminarVacio();
        com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        
        tab_datos_grafico = new Tabla();
        tab_datos_grafico.setId("tab_datos_grafico");
        tab_datos_grafico.setSql(ser_factura.getSqlTotalVentasMensuales(String.valueOf(com_pto_emision.getValue()), String.valueOf(com_periodo.getValue())));
        tab_datos_grafico.setLectura(true);
        tab_datos_grafico.setColumnaSuma("num_facturas,ventas12,ventas0,iva,total");
        tab_datos_grafico.getColumna("num_facturas").alinearDerecha();
        tab_datos_grafico.getColumna("ventas12").alinearDerecha();
        tab_datos_grafico.getColumna("ventas0").alinearDerecha();
        tab_datos_grafico.getColumna("iva").alinearDerecha();
        tab_datos_grafico.getColumna("total").alinearDerecha();
        tab_datos_grafico.dibujar();
        
        Grid gri_opciones = new Grid();
        gri_opciones.setColumns(2);
        gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
        gri_opciones.getChildren().add(com_periodo);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(gri_opciones);
        pat_panel.setPanelTabla(tab_datos_grafico);
        
        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        gpa_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "num_facturas");
        gri.getChildren().add(pat_panel);
        gri.getChildren().add(gpa_facturas);
        grupo.getChildren().add(gri);
        
        gca_facturas.setTitulo("VENTAS MENSUALES");
        gca_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
        grupo.getChildren().add(gca_facturas);
        mep_menu.dibujar(5, "GRAFICOS DE VENTAS", grupo);
    }
    
    public void dibujarReporteVentas() {
        
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        
        com_mes = new Combo();
        com_mes.setMetodo("actualizarFacturas");
        com_mes.setCombo(ser_factura.getSqlMeses());
        com_mes.eliminarVacio();
        com_mes.setValue(String.valueOf(utilitario.getMes(utilitario.getFechaActual())));
        
        com_periodo = new Combo();
        com_periodo.setMetodo("actualizarFacturas");
        com_periodo.setCombo(ser_factura.getSqlAniosFacturacion());
        com_periodo.eliminarVacio();
        com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        
        bar_menu.agregarComponente(new Etiqueta("<strong>PERÍODO :</strong>"));
        bar_menu.agregarComponente(com_periodo);
        bar_menu.agregarComponente(new Etiqueta("<strong>MES :</strong>"));
        bar_menu.agregarComponente(com_mes);
        
        tab_rep_ventas = new Tabla();
        tab_rep_ventas.setId("tab_rep_ventas");
        tab_rep_ventas.setSql(ser_factura.getSqlVentasMensuales(com_pto_emision.getValue() + "", com_mes.getValue() + "", com_periodo.getValue() + ""));
        tab_rep_ventas.getColumna("ide_cccfa").setVisible(false);
        tab_rep_ventas.getColumna("observacion_cccfa").setVisible(false);
        tab_rep_ventas.setRows(15);
        
        tab_rep_ventas.setLectura(true);
        tab_rep_ventas.getColumna("NOM_GEPER").setLongitud(100);
        tab_rep_ventas.setColumnaSuma("ventas12,ventas0,valor_iva_cccfa,total_cccfa");
        tab_rep_ventas.getColumna("ventas12").alinearDerecha();
        tab_rep_ventas.getColumna("ventas0").alinearDerecha();
        tab_rep_ventas.getColumna("valor_iva_cccfa").alinearDerecha();
        tab_rep_ventas.getColumna("total_cccfa").alinearDerecha();
        tab_rep_ventas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_rep_ventas);
        
        Grupo grupo = new Grupo();
        grupo.getChildren().add(bar_menu);
        grupo.getChildren().add(pat_panel);
        
        mep_menu.dibujar(7, "REPORTE DE VENTAS POR MES Y PERÍODO", grupo);
        
    }
    
    public void abrirVerFactura() {
        if (tab_facturas.getValorSeleccionado() != null) {
            fcc_factura.verFactura(tab_facturas.getValorSeleccionado());
            fcc_factura.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccionar Factura", "Debe seleccionar una factura de la tabla");
        }
        
    }
    
    public void actualizarFacturas() {
        if (mep_menu.getOpcion() == 1) {
            tab_facturas.setSql(ser_factura.getSqlFacturas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas.ejecutarSql();
        } else if (mep_menu.getOpcion() == 2) {
            tab_facturas_no_conta.setSql(ser_factura.getSqlFacturasNoContabilizadas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas_no_conta.ejecutarSql();
        } else if (mep_menu.getOpcion() == 3) {
            tab_facturas_anuladas.setSql(ser_factura.getSqlFacturasAnuladas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas_anuladas.ejecutarSql();
        } else if (mep_menu.getOpcion() == 4) {
            tab_facturas_x_cobrar.setSql(ser_factura.getSqlFacturasPorCobrar(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_facturas_x_cobrar.ejecutarSql();
        } else if (mep_menu.getOpcion() == 5) {
            tab_datos_grafico.setSql(ser_factura.getSqlTotalVentasMensuales(com_pto_emision.getValue() + "", String.valueOf(com_periodo.getValue())));
            tab_datos_grafico.ejecutarSql();
            gca_facturas.limpiar();
            gca_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "total", "VENTAS " + String.valueOf(com_periodo.getValue()));
            gpa_facturas.limpiar();
            gpa_facturas.agregarSerie(tab_datos_grafico, "nombre_gemes", "num_facturas");
            utilitario.addUpdate("gca_facturas,gpa_facturas");
        } else if (mep_menu.getOpcion() == 7) {
            tab_rep_ventas.setSql(ser_factura.getSqlVentasMensuales(com_pto_emision.getValue() + "", com_mes.getValue() + "", com_periodo.getValue() + ""));
            tab_rep_ventas.ejecutarSql();
        }
        
    }
    
    @Override
    public void insertar() {
        fcc_factura.nuevaFactura();
        fcc_factura.dibujar();
    }
    
    @Override
    public void guardar() {
        
    }
    
    @Override
    public void eliminar() {
        
    }
    
    public Tabla getTab_facturas() {
        return tab_facturas;
    }
    
    public void setTab_facturas(Tabla tab_facturas) {
        this.tab_facturas = tab_facturas;
    }
    
    public FacturaCxC getFcc_factura() {
        return fcc_factura;
    }
    
    public void setFcc_factura(FacturaCxC fcc_factura) {
        this.fcc_factura = fcc_factura;
    }
    
    public Tabla getTab_facturas_no_conta() {
        return tab_facturas_no_conta;
    }
    
    public void setTab_facturas_no_conta(Tabla tab_facturas_no_conta) {
        this.tab_facturas_no_conta = tab_facturas_no_conta;
    }
    
    public Tabla getTab_facturas_anuladas() {
        return tab_facturas_anuladas;
    }
    
    public void setTab_facturas_anuladas(Tabla tab_facturas_anuladas) {
        this.tab_facturas_anuladas = tab_facturas_anuladas;
    }
    
    public Tabla getTab_facturas_x_cobrar() {
        return tab_facturas_x_cobrar;
    }
    
    public void setTab_facturas_x_cobrar(Tabla tab_facturas_x_cobrar) {
        this.tab_facturas_x_cobrar = tab_facturas_x_cobrar;
    }
    
    public GraficoCartesiano getGca_facturas() {
        return gca_facturas;
    }
    
    public void setGca_facturas(GraficoCartesiano gca_facturas) {
        this.gca_facturas = gca_facturas;
    }
    
    public Tabla getTab_datos_grafico() {
        return tab_datos_grafico;
    }
    
    public void setTab_datos_grafico(Tabla tab_datos_grafico) {
        this.tab_datos_grafico = tab_datos_grafico;
    }
    
    public Tabla getTab_rep_ventas() {
        return tab_rep_ventas;
    }
    
    public void setTab_rep_ventas(Tabla tab_rep_ventas) {
        this.tab_rep_ventas = tab_rep_ventas;
    }
    
}
