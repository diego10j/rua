/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_gerencial.ejb.ServicioGerencial;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author LUIS TOAPANTA
 */    
public class BalanceCabecera extends Pantalla {

    Tabla tab_tabla1 = new Tabla();
    Tabla tab_tabla2 = new Tabla();
    Tabla tab_tabla3 = new Tabla();

    @EJB
    private final ServicioGerencial ser_gerencial = (ServicioGerencial) utilitario.instanciarEJB(ServicioGerencial.class);

    public BalanceCabecera() {



        //Permite crear la tabla1 
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setHeader("CONT BALANCE CABECERA");
        tab_tabla1.setTabla("ger_cont_balance_cabecera", "ide_gecobc", 1);
        tab_tabla1.getColumna("ide_gerest").setCombo(ser_gerencial.getEstado());
        tab_tabla1.getColumna("ide_geani");
        tab_tabla1.getColumna("ide_gerobr").setCombo(ser_gerencial.getObra());
        tab_tabla1.getColumna("responsable_gecobc").setNombreVisual("RESPONSABLE");
        tab_tabla1.getColumna("fecha_apert_gecobc").setNombreVisual("FECHA APERTURA");
        tab_tabla1.getColumna("fecha_cierre_gecobc").setNombreVisual("FECHA CIERRE");
        tab_tabla1.getColumna("observacion_gecobc").setNombreVisual("OBSERVACION");
        tab_tabla1.agregarRelacion(tab_tabla2);
      
        tab_tabla1.dibujar(); 

        //Es el contenedor de la tabla
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

        //Permite crear la tabla2 
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setHeader("BALANCE MENSUAL");
        tab_tabla2.setTabla("ger_balance_mensual", "ide_gebame", 2);
        tab_tabla2.getColumna("ide_gecobc").setNombreVisual("COD.CAB.BAL");
        tab_tabla2.getColumna("ide_getiba").setCombo(ser_gerencial.getTipoBalance("-1",""));         
        tab_tabla2.getColumna("responsable_gebame").setNombreVisual("RESPONSABLE");
        tab_tabla2.getColumna("fecha_apert_gebame").setNombreVisual("FEHCA APERTURA");
        tab_tabla2.getColumna("fecha_cierre_gebame").setNombreVisual("FECHA CIERRE");
        tab_tabla2.getColumna("observacion_gebame").setNombreVisual("OBSERVACION");
        tab_tabla2.agregarRelacion(tab_tabla3);
        tab_tabla2.dibujar();

        //Es el contenedor de la tabla
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setId("pat_panel2");
        pat_panel2.setPanelTabla(tab_tabla2);

        //Permite crear la tabla3      
        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setHeader("BALANCE");
        tab_tabla3.setTabla("ger_balance", "ide_gerbal", 3);
        tab_tabla3.getColumna("ide_gebame").setNombreVisual("BALANCE MENSUAL");
        tab_tabla3.getColumna("ide_cndpc");
        tab_tabla3.getColumna("valor_debe_gerbal").setNombreVisual("VALOR DEBE");
        tab_tabla3.getColumna("valor_haber_gerbal").setNombreVisual("VALOR HABER");

        tab_tabla3.dibujar();

        //Es el contenedor de la tabla
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setId("pat_panel3");
        pat_panel3.setPanelTabla(tab_tabla3);

        //Permite la dision de la pantalla
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel3, "20%", "50%", "H");
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
        } else if (tab_tabla2.isFocus()) {
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
                    guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.eliminar();
        } else if (tab_tabla2.isFocus()) {
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

}
