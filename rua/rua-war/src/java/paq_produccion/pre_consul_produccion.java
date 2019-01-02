/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.aplicacion.Columna;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_produccion.ejb.ServicioProduccion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author HP_PRO
 */
public class pre_consul_produccion extends Pantalla {

    private  Tabla tab_tabla1 = new Tabla();

    @EJB
    private final ServicioProduccion ser_produccion = (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class);

    public pre_consul_produccion() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("prod_proforma", "ide_prpro", 1);
        tab_tabla1.getColumna("ide_cndfp").setCombo("con_deta_forma_pago","ide_cndfp","nombre_cndfp","");
        tab_tabla1.getColumna("ide_gtemp").setCombo("gth_empleado","ide_gtemp","apellido_paterno_gtemp","");
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona","ide_geper","nom_geper","");
        tab_tabla1.getColumna("ide_gtcar").setCombo("gth_cargo","ide_gtcar","detalle_gtcar","");
        tab_tabla1.getColumna("ide_prvat").setCombo("prod_validez_tiempo","ide_prvat","detalle_prvat","");
        tab_tabla1.getColumna("ide_prpro").setVisible(false);
        tab_tabla1.setLectura(true);
        
        
        tab_tabla1.dibujar();
        
        PanelTabla pa_tabla1=new PanelTabla();
        pa_tabla1.setId("pa_tabla1");
        pa_tabla1.setPanelTabla(tab_tabla1);
        
        Division div_tabla1=new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pa_tabla1);
        agregarComponente(div_tabla1);
       
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
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
    
}
