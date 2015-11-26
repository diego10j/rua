/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author Diego
 */
public class pre_prueba extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    Conexion con;

    public pre_prueba() {

        con = new Conexion();        
        con.setUnidad_persistencia("ServidorPreProduccion");
        con.NOMBRE_MARCA_BASE="oracle";

        tab_tabla.setId("tab_tabla");
        tab_tabla.setConexion(con);
        tab_tabla.setTabla("ASI_MOTIVO", "IDE_ASMOT", 1);
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        agregarComponente(pat_panel);

    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        con.guardarPantalla();
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
