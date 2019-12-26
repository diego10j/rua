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
public class Mes extends Pantalla {

    private Conexion conPostgres = new Conexion();
    Tabla tab_tabla1 = new Tabla();
    Tabla tab_tabla2 = new Tabla();

    public Mes() {

        conPostgres.setUnidad_persistencia("rua_gerencial");
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        //Permite crear la tabla 
        tab_tabla1.setId("tab_tabla2");
        tab_tabla1.setConexion(conPostgres);
        tab_tabla1.setHeader("MES");
        tab_tabla1.setTabla("gen_mes", "ide_gemes", 1);
        tab_tabla1.getColumna("ide_gemes").setNombreVisual("CODIGO");
        tab_tabla1.getColumna("nombre_gemes").setNombreVisual("NOMBRE");
        tab_tabla1.getColumna("alterno_gemes").setNombreVisual("ALTERNO");
        tab_tabla1.getColumna("activo_gemes").setNombreVisual("ACTIVO");
        tab_tabla1.agregarRelacion(tab_tabla2);
          
        tab_tabla1.dibujar();           

        //Es el contenedor de la tabla
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

        //Permite crear la tabla            
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setConexion(conPostgres);
        tab_tabla2.setHeader("TIPO BALANCE MES");
        tab_tabla2.setTabla("ger_tipo_balance_mes", "ide_getibm", 2);
        tab_tabla2.getColumna("ide_getibm").setNombreVisual("CODIGO");
        tab_tabla2.getColumna("ide_getiba");
        tab_tabla2.getColumna("ide_gemes");

        tab_tabla2.dibujar();

        //Es el contenedor de la tabla
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setId("pat_panel2");
        pat_panel2.setPanelTabla(tab_tabla2);

        //Permite la dision de la pantalla
        Division div_division1 = new Division();
        div_division1.setId("div_division1");
        div_division1.dividir2(pat_panel1, pat_panel2, "50%", "H");

        agregarComponente(div_division1);

    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                conPostgres.guardarPantalla();
                //guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

}
