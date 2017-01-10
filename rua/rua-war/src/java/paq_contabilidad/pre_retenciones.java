/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
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

    private Tabla tab_tabla;

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

    public pre_retenciones() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.agregarReporte();

        com_autoriza.setCombo(ser_retencion.getSqlComboAutorizaciones());
        com_autoriza.setMetodo("actualizarConsulta");
        bar_botones.agregarComponente(new Etiqueta("NUM. AUTORIZACIÓN: "));
        bar_botones.agregarComponente(com_autoriza);

        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));

        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));

        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setMetodo("actualizarConsulta");
        bot_consultar.setIcon("ui-icon-search");

        bar_botones.agregarBoton(bot_consultar);

        mep_menu.setMenuPanel("COMPROBANTES DE RETENCIÓN", "23%");
        mep_menu.agregarItem("Listado de Retenciones", "dibujarListado", "ui-icon-bookmark"); //1
        mep_menu.agregarItem("Retenciones Anuladas", "dibujarAnuladas", "ui-icon-bookmark");//2
        mep_menu.agregarItem("Consultas por Impuesto", "dibujarConsulta", "ui-icon-bookmark");//4
        mep_menu.agregarSubMenu("COMPROBANTES RETENCIONES EN VENTAS");
        mep_menu.agregarItem("Listado de RetencionesVentas", "dibujarListadoVentas", "ui-icon-bookmark");//3
        agregarComponente(mep_menu);
        dibujarListado();

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_rep.setId("sel_rep");
        agregarComponente(rep_reporte);
        agregarComponente(sel_rep);
    }

    public void dibujarConsulta() {
        Grupo gru_grupo = new Grupo();
        com_impuesto = new Combo();
        com_impuesto.setCombo("SELECT ide_cncim, nombre_cncim,casillero_cncim FROM con_cabece_impues");

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_retencion.getSqlConsultaImpuestos(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_impuesto.getValue())));
        tab_tabla.setCampoPrimaria("ide_cndre");
        tab_tabla.getColumna("nombre_cncim").setFiltro(true);
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

        mep_menu.dibujar(4, "CONSULTAR POR IMPUESTO", gru_grupo);
    }

    public void actualizarConsultar() {
        tab_tabla.setSql(ser_retencion.getSqlConsultaImpuestos(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_impuesto.getValue())));
        tab_tabla.ejecutarSql();

    }

    public void dibujarListadoVentas() {
        Grupo gru_grupo = new Grupo();
        mep_menu.dibujar(3, "COMPROBANTES DE RETENCIÓN EN VENTAS", gru_grupo);
    }

    public void dibujarListado() {
        Grupo gru_grupo = new Grupo();

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Comprobante de Retención");
        bot_anular.setMetodo("anularRetencion");
        bar_menu.agregarComponente(bot_anular);

        gru_grupo.getChildren().add(bar_menu);

        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_retencion.getSqlRetenciones(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setCampoPrimaria("ide_cncre");
        tab_tabla.setLectura(true);
        tab_tabla.setValueExpression("rowStyleClass", "fila.campos[1] eq '1' ? 'text-red' : null");

        tab_tabla.setRows(25);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "COMPROBANTES DE RETENCIÓN", gru_grupo);
    }

    public void anularRetencion() {
        if (tab_tabla.getValorSeleccionado() != null) {
            ser_retencion.anularComprobanteRetencion(tab_tabla.getValorSeleccionado());
            if (guardarPantalla().isEmpty()) {
                tab_tabla.actualizar();
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Comprobante de Retención", "");
        }
    }

    public void actualizarConsulta() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla.setSql(ser_retencion.getSqlRetenciones(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 2) {
            tab_tabla.setSql(ser_retencion.getSqlRetencionesAnuladas(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 4) {
            actualizarConsultar();
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
        }
    }

    public void dibujarAnuladas() {
        Grupo gru_grupo = new Grupo();
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_retencion.getSqlRetencionesAnuladas(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setLectura(true);
        tab_tabla.setRows(25);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "COMPROBANTES DE RETENCIÓN ANULADOS", gru_grupo);
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

}
