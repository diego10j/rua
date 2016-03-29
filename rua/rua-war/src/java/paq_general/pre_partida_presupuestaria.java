/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_partida_presupuestaria extends Pantalla {

    
    private Tabla tab_tabla = new Tabla();

    public pre_partida_presupuestaria() {        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_PARTIDA_PRESUPUESTARIA", "IDE_GEPAP", 1);
        tab_tabla.getColumna("ACTIVO_GEPAP").setCheck();
        tab_tabla.getColumna("ACTIVO_GEPAP").setValorDefecto("TRUE");
        tab_tabla.getColumna("ide_prasp").setVisible(false);
        tab_tabla.getColumna("ide_prcla").setCombo("pre_clasificador", "ide_prcla", "codigo_clasificador_prcla,descripcion_clasificador_prcla", "");
        tab_tabla.getColumna("ide_prcla").setAutoCompletar();
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
