/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_bloqueos extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    public pre_bloqueos() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();

        Boton bot_borrar = new Boton();
        bot_borrar.setValue("Borrar Bloqueos");
        bot_borrar.setMetodo("borrar");
        bar_botones.agregarBoton(bot_borrar);

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("SIS_BLOQUEO", "IDE_BLOQ", 1);
        tab_tabla.setRows(20);
        tab_tabla.getColumna("TABLA_BLOQ").setFiltro(true);
        tab_tabla.getColumna("IDE_USUA").setVisible(false);
        tab_tabla.setLectura(true);
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        Division div_division = new Division();
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
    }

    public void borrar() {
        utilitario.getConexion().ejecutarSql("DELETE FROM SIS_BLOQUEO");
        utilitario.agregarMensaje("Se borro la tabla de bloqueos", "");
        tab_tabla.ejecutarSql();
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
