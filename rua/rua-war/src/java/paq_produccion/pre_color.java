/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Nicolas Cajilema
 */
public class pre_color extends Pantalla {
     private Tabla tab_color=new Tabla();
    
    public  pre_color (){
        tab_color.setId("tab_color");
        tab_color.setTabla("prod_color","ide_prcol",1);
        tab_color.setHeader("COLOR");
        tab_color.dibujar();
        
        PanelTabla pat_color = new PanelTabla();
        pat_color.setId("pat_color");
        pat_color.setPanelTabla(tab_color);
        
        
        Division div_color = new Division();
        div_color.setId("div_color");
        div_color.dividir1(pat_color);
        
        agregarComponente(div_color);   

    }

   @Override
    public void insertar() {
        tab_color.insertar();
    }

    @Override
    public void guardar() {
        tab_color.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_color.eliminar();
        
                
    }

    public Tabla getTab_color() {
        return tab_color;
    }

    public void setTab_color(Tabla tab_color) {
        this.tab_color = tab_color;
    }
    
    
}
