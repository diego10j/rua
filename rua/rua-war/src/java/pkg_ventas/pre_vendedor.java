/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_ventas;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_vendedor extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_vendedor() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("ven_vendedor", "ide_vgven", 1);

        tab_tabla.getColumna("ide_vgtve").setCombo("ven_tipo_vendedor", "ide_vgtve", "nombre_vgtve", "");
        tab_tabla.setTipoFormulario(false);
        tab_tabla.getGrid().setColumns(4);

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
        utilitario.getConexion().guardarPantalla();
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
