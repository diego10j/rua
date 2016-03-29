/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_tipo_periodo extends Pantalla {

    
    private Tabla tab_tabla = new Tabla();

    public pre_tipo_periodo() {        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_TIPO_PERIODO", "IDE_GETPR", 1);
        tab_tabla.getColumna("ACTIVO_GETPR").setCheck();
        tab_tabla.getColumna("ACTIVO_GETPR").setValorDefecto("TRUE");
        tab_tabla.setCampoOrden("DETALLE_GETPR");
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
        tab_tabla.guardar();
        guardarPantalla();
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
