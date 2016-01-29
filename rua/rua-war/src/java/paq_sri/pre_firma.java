/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_firma extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_firma() {
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("sri_firma_digital", "ide_srfid", 1);
        tab_tabla.setTipoFormulario(true);
        tab_tabla.getColumna("archivo_srfid").setUpload();
        tab_tabla.getColumna("password_srfid").setClave();
        tab_tabla.getColumna("fecha_ingreso_srfid").setValorDefecto(utilitario.getFechaActual());
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    @Override

    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

}
