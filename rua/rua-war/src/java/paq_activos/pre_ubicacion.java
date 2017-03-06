/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.NodeSelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_ubicacion extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();

    public pre_ubicacion() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("act_ubicacion_activo", "ide_acuba", 1);

        tab_tabla1.getColumna("act_ide_acuba").setVisible(false);
        tab_tabla1.getColumna("act_ide_acuba").setVisible(false);
        tab_tabla1.getColumna("codigo_acuba").setMascara("99");
        tab_tabla1.getColumna("codigo_acuba").setLongitud(2);
        tab_tabla1.getColumna("codigo_acuba").setAncho(2);

        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        Division div_division = new Division();
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }
}
