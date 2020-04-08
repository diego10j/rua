/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Louis
 */
public class Clasificador extends Pantalla {

    Tabla tab_tabla1 = new Tabla();

    private Division div_division1 = new Division();
    private Arbol arb_arbol = new Arbol();

    public Clasificador() {

        
        //Permite crear la tabla          
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("pre_clasificador", "ide_prcla", 1);
        tab_tabla1.setCampoPadre("pre_ide_prcla");
        tab_tabla1.setCampoNombre("descripcion_clasificador_prcla");
        tab_tabla1.getColumna("ide_prcla").setNombreVisual("CODIGO");
        tab_tabla1.getColumna("codigo_clasificador_prcla").setNombreVisual("CODIGO CLASIFICADOR");
        tab_tabla1.getColumna("descripcion_clasificador_prcla").setNombreVisual("DESCRIPCION CLASIFICADOR");
        tab_tabla1.getColumna("tipo_prcla").setNombreVisual("TIPO");
        tab_tabla1.getColumna("nivel_prcla").setNombreVisual("NIVEL");
        tab_tabla1.getColumna("grupo_prcla").setNombreVisual("GRUPO");
        tab_tabla1.getColumna("sigefc_prcla").setNombreVisual("SIGEFC");
        tab_tabla1.getColumna("activo_prcla").setNombreVisual("ACTIVO");

        tab_tabla1.agregarArbol(arb_arbol);

        tab_tabla1.dibujar();

        //Es el contenedor de la tabla
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

        arb_arbol.setId("arb_arbol");        
        arb_arbol.dibujar();

        //Permite la dision de la pantalla             
        div_division1.setId("div_division1");
        div_division1.dividir2(arb_arbol, pat_panel1, "20%", "V");

        agregarComponente(div_division1);

    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        guardarPantalla();
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

    public Division getDiv_division1() {
        return div_division1;
    }

    public void setDiv_division1(Division div_division1) {
        this.div_division1 = div_division1;
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

}
