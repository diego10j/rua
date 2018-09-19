/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Nicolas Cajilema
 */
public class pre_cronograma extends Pantalla{
    
         private Tabla tab_cronograma=new Tabla();
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
    
         
         public pre_cronograma(){
        tab_cronograma.setId("tab_cronograma");
        tab_cronograma.setTabla("prod_cronograma_produccion ","ide_prcrp",1);
     //   tab_cronograma.getColumna("ide_inarti").setCombo(ser_adquisiciones.getMaterial("", ""));
        //tab_cronograma.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_cronograma.setTipoFormulario(true);
        tab_cronograma.getGrid().setColumns(4);
        tab_cronograma.setHeader("CRONOGRAMA");
        tab_cronograma.dibujar();
        
        PanelTabla pat_cronograma = new PanelTabla();
        pat_cronograma.setId("pat_cronograma");
        pat_cronograma.setPanelTabla(tab_cronograma);
        
        
        Division div_cronograma = new Division();
        div_cronograma.setId("div_color");
        div_cronograma.dividir1(pat_cronograma);
        
        agregarComponente(pat_cronograma);   
         }

    
   @Override
    public void insertar() {
        tab_cronograma.insertar();
    }

    @Override
    public void guardar() {
        tab_cronograma.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_cronograma.eliminar();
    }

    public Tabla getTab_cronograma() {
        return tab_cronograma;
    }

    public void setTab_cronograma(Tabla tab_cronograma) {
        this.tab_cronograma = tab_cronograma;
    }
    
}
