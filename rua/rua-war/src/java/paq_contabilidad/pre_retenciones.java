/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
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
    @EJB
    private final ServicioRetenciones ser_retencion = (ServicioRetenciones) utilitario.instanciarEJB(ServicioRetenciones.class);

    private Tabla tab_tabla;

    public pre_retenciones() {
        bar_botones.limpiar();
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
        mep_menu.agregarSubMenu("COMPROBANTES RETENCIONES EN VENTAS");
        mep_menu.agregarItem("Listado de RetencionesVentas", "dibujarListadoVentas", "ui-icon-bookmark");//3
        agregarComponente(mep_menu);
        dibujarListado();
    }

    public void dibujarListado() {
        Grupo gru_grupo = new Grupo();
        tab_tabla = new Tabla();
        tab_tabla.setId("tab_tabla");
        tab_tabla.setSql(ser_retencion.getSqlRetenciones(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla.setLectura(true);
        tab_tabla.setRows(25);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        gru_grupo.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "COMPROBANTES DE RETENCIÓN", gru_grupo);
    }

    public void actualizarConsulta() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla.setSql(ser_retencion.getSqlRetenciones(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
        } else if (mep_menu.getOpcion() == 2) {
            tab_tabla.setSql(ser_retencion.getSqlRetencionesAnuladas(String.valueOf(com_autoriza.getValue()), cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
            tab_tabla.ejecutarSql();
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

}
