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

public class pre_maquina extends Pantalla {
     private Tabla tab_maquina=new Tabla();
    
    public  pre_maquina (){
       tab_maquina.setId("tab_maquina");
       tab_maquina.setTabla("prod_maquina","ide_prmaq",1);
       tab_maquina.setHeader("MAQUINA");
       tab_maquina.dibujar();
        
        PanelTabla pat_maquina = new PanelTabla();
        pat_maquina.setId("pat_maquina");
        pat_maquina.setPanelTabla(tab_maquina);
        
        
        Division div_maquina = new Division();
        div_maquina.setId("div_maquina");
        div_maquina.dividir1(pat_maquina);
        
        agregarComponente(div_maquina);   

    }

   @Override
    public void insertar() {
        tab_maquina.insertar();
    }

    @Override
    public void guardar() {
        tab_maquina.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_maquina.eliminar();
        
                
    }

    public Tabla getTab_maquina() {
        return tab_maquina;
    }

    public void setTab_maquina(Tabla tab_maquina) {
        this.tab_maquina = tab_maquina;
    }
    
    
}
