/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_accion_motivo_empleado extends Pantalla {
    
    private Tabla tab_tabla = new Tabla();

    public pre_accion_motivo_empleado() {        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_ACCION_MOTIVO_EMPLEADO", "IDE_GEAME", 1);
        tab_tabla.getColumna("IDE_GEAED").setCombo("GEN_ACCION_EMPLEADO_DEPA", "IDE_GEAED", "DETALLE_GEAED", "");
        tab_tabla.getColumna("IDE_GEMED").setCombo("GEN_MOTIVO_EMPLEADO_DEPA", "IDE_GEMED", "DETALLE_GEMED", "");
        tab_tabla.getColumna("ACTIVO_GEAME").setCheck();
        tab_tabla.getColumna("ACTIVO_GEAME").setValorDefecto("true");
        tab_tabla.getColumna("ANTERIOR_GEAME").setCheck();
        tab_tabla.getColumna("ANTERIOR_GEAME").setValorDefecto("false");
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
