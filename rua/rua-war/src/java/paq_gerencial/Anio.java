/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Luis Toapanta
 */
public class Anio extends Pantalla {

    Tabla tab_tabla1 = new Tabla();

    public Anio() {


        //Permite crear la tabla          
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("AÑO");
        tab_tabla1.setTabla("gen_anio", "ide_geani", 1);
        tab_tabla1.getColumna("ide_geani").setNombreVisual("CODIGO");
        tab_tabla1.getColumna("nom_geani").setNombreVisual("NOMBRE");
        tab_tabla1.getColumna("activo_geani").setNombreVisual("ACTIVO");

        tab_tabla1.dibujar();

        //Es el contenedor de la tabla
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

        //Permite la dision de la pantalla
        Division div_division1 = new Division();
        div_division1.setId("div_division1");
        div_division1.dividir1(pat_panel1);

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


}
