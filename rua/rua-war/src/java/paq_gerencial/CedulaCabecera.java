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
public class CedulaCabecera extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();

    public CedulaCabecera() {

        conPostgres.setUnidad_persistencia("rua_gerencial");
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(conPostgres);
        tab_tabla1.setHeader("CEDULA CABECERA");
        tab_tabla1.setTabla("ger_cedula_cabecera", "ide_gececa", 0);
        tab_tabla1.getColumna("ide_gececa").setNombreVisual("CODIGO");
        tab_tabla1.getColumna("ide_gerobr");
        tab_tabla1.getColumna("ide_gerest");
        tab_tabla1.getColumna("ide_geani");
        tab_tabla1.getColumna("responsable_gececa").setNombreVisual("RESPONSABLE");
        tab_tabla1.getColumna("fecha_apertura_gececa").setNombreVisual("FECHA APERTURA");
        tab_tabla1.getColumna("fecha_cierre_gececa").setNombreVisual("FECHA CIERRE");
        tab_tabla1.getColumna("observacion_gececa").setNombreVisual("OBSERVACION");
        tab_tabla1.agregarRelacion(tab_tabla2);

        tab_tabla1.dibujar();      

        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setConexion(conPostgres);
        tab_tabla2.setHeader("CEDULA MENSUAL");
        tab_tabla2.setTabla("ger_cedula_mensual", "ide_geceme", 1);
        tab_tabla2.getColumna("ide_geceme").setNombreVisual("CÓDIGO");
        tab_tabla2.getColumna("ide_gececa");
        tab_tabla2.getColumna("ide_getiba");
        tab_tabla2.getColumna("ide_gerest");
        tab_tabla2.getColumna("ide_gemes");
        tab_tabla2.getColumna("responsable_geceme").setNombreVisual("RESPONSABLE");
        tab_tabla2.getColumna("fecha_apertura_geceme").setNombreVisual("FECHA APERTURA");
        tab_tabla2.getColumna("fecha_cierre_geceme").setNombreVisual("FECHA CIERRE");
        tab_tabla2.getColumna("observacion_geceme").setNombreVisual("OBSERVACION");
        tab_tabla2.agregarRelacion(tab_tabla3);

        tab_tabla2.dibujar();

        PanelTabla pat_tabla2 = new PanelTabla();
        pat_tabla2.setId("pat_tabla2");         
        pat_tabla2.setPanelTabla(tab_tabla2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setConexion(conPostgres);
        tab_tabla3.setHeader("CEDULA");
        tab_tabla3.setTabla("ger_cedula", "ide_gerced", 2);
        tab_tabla3.getColumna("ide_gerced").setNombreVisual("CÓDIGO");
        tab_tabla3.getColumna("ide_geceme");
        tab_tabla3.getColumna("ide_prcla");
        tab_tabla3.getColumna("tipo_gerced").setNombreVisual("TIPO");
        tab_tabla3.getColumna("inicial_gerced").setNombreVisual("INICIAL");
        tab_tabla3.getColumna("reforma_gerced").setNombreVisual("REFORMA");
        tab_tabla3.getColumna("devengado_gerced").setNombreVisual("DEVENGADO");
        tab_tabla3.getColumna("comprometido_gerced").setNombreVisual("COMPROMETIDO");

        tab_tabla3.dibujar();

        PanelTabla pat_tabla3 = new PanelTabla();
        pat_tabla3.setId("pat_tabla3");
        pat_tabla3.setPanelTabla(tab_tabla3);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_tabla1, pat_tabla2, pat_tabla3, "30%", "50%", "H");

        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
        }
        if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        } else if (tab_tabla3.isFocus()) {
            tab_tabla3.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()) {
            if (tab_tabla2.guardar()) {
                if (tab_tabla3.guardar()) {
                    conPostgres.guardarPantalla();
                    //guardarPantalla();

                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
        }
        if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        } else if (tab_tabla3.isFocus()) {
            tab_tabla3.eliminar();
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }        

}
