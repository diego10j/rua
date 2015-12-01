/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_bancos;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_tipo_transaccion_banco extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();

    public pre_tipo_transaccion_banco() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("tes_tip_tran_banc", "ide_tettb", 1);
        tab_tabla.getColumna("signo_tettb").setRadio(getListaSignos(), "0");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);

        agregarComponente(div_division);

    }

    public List getListaSignos() {
        //PARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "+"
        };
        Object fila2[] = {
            "-1", "-"
        };
        lista.add(fila1);
        lista.add(fila2);
        return lista;
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
