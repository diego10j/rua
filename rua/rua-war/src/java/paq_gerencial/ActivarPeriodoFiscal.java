/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
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
public class ActivarPeriodoFiscal extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Combo com_anio=new Combo(); 

    @EJB
    private final ServicioGerencial ser_gerencial = (ServicioGerencial) utilitario.instanciarEJB(ServicioGerencial.class);

    public ActivarPeriodoFiscal() {

        conPostgres.setUnidad_persistencia("rua_gerencial");
        conPostgres.NOMBRE_MARCA_BASE = "postgres";
        
        com_anio.setConexion(conPostgres);
        com_anio.setCombo(ser_gerencial.getAnio("true,false"));
        com_anio.setMetodo("seleccionaElAnio");
	bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
	bar_botones.agregarComponente(com_anio);

        //Permite crear la tabla1 
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setConexion(conPostgres);
        tab_tabla1.setHeader("CONT BALANCE CABECERA");
        tab_tabla1.setTabla("ger_cont_balance_cabecera", "ide_gecobc", 1);
        tab_tabla1.getColumna("ide_gerest").setCombo(ser_gerencial.getEstado());
        tab_tabla1.getColumna("ide_geani");
        tab_tabla1.getColumna("ide_gerobr").setCombo(ser_gerencial.getObra());
        tab_tabla1.getColumna("responsable_gecobc").setNombreVisual("RESPONSABLE");
        tab_tabla1.getColumna("fecha_apert_gecobc").setNombreVisual("FECHA APERTURA");
        tab_tabla1.getColumna("fecha_cierre_gecobc").setNombreVisual("FECHA CIERRE");
        tab_tabla1.getColumna("observacion_gecobc").setNombreVisual("OBSERVACION");
        tab_tabla1.dibujar(); 

        //Es el contenedor de la tabla
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

       
        //Permite la dision de la pantalla
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel1);
        agregarComponente(div_division);

    }
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_tabla1.setCondicion("not ide_prcla is null and ide_geani="+com_anio.getValue());
			tab_tabla1.ejecutarSql();


		}
		else{
			utilitario.agregarMensajeInfo("Selecione un Año", "");

		}
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
            
    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

}
