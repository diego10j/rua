/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;     

import framework.componentes.Boton;
import paq_general.*;
import framework.componentes.Division;
import framework.componentes.PanelTabla;      
import framework.componentes.Tabla;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Louis
 */
public class Casa extends Pantalla {

    private Conexion conPostgres = new Conexion();       
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();

    public Casa() {             

        conPostgres.setUnidad_persistencia("rua_gerencial");
        conPostgres.NOMBRE_MARCA_BASE = "postgres";           
        
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(conPostgres);
        tab_tabla1.setHeader("CASA SALESIANA");
        tab_tabla1.setTabla("ger_casa", "ide_gercas", 0);
        tab_tabla1.getColumna("ide_gercas").setNombreVisual("CÓDIGO");    
        tab_tabla1.getColumna("nombre_gercas").setNombreVisual("NOMBRE");
        tab_tabla1.getColumna("codigo_gercas").setNombreVisual("CÓDIGO CASA");
        tab_tabla1.getColumna("abreviatura_gercas").setNombreVisual("ABREVIATURA");
        tab_tabla1.dibujar();

        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");            
        tab_tabla2.setConexion(conPostgres);             
        tab_tabla2.setHeader("OBRA SALESIANA");           
        tab_tabla2.setTabla("ger_obra", "ide_gerobr", 1);
        tab_tabla2.getColumna("ide_gerobr").setNombreVisual("CÓDIGO");
        tab_tabla2.getColumna("ide_gercas").setNombreVisual("COD. CASA");
        tab_tabla2.getColumna("nombre_gerobr").setNombreVisual("NOMBRE");
        tab_tabla2.getColumna("codigo_gerobr").setNombreVisual("CÓDIGO OBRA");
        tab_tabla2.getColumna("abreviatura_gerobr").setNombreVisual("ABREVIATURA");

        tab_tabla2.dibujar();

        PanelTabla pat_tabla2 = new PanelTabla();
        pat_tabla2.setId("pat_tabla2");
        pat_tabla2.setPanelTabla(tab_tabla2);
                     
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_tabla1, pat_tabla2, "50%", "H");

        agregarComponente(div_division);

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
