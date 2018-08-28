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
public class pre_turno extends Pantalla {
     private Tabla tab_turno=new Tabla();
    
    public  pre_turno (){
        tab_turno.setId("tab_turno");
        tab_turno.setTabla("prod_turno","ide_prtur",1);
        tab_turno.setHeader("TURNO");
        tab_turno.dibujar();
        
        PanelTabla pat_turno = new PanelTabla();
        pat_turno.setId("pat_turno");
        pat_turno.setPanelTabla(tab_turno);
        
        
        Division div_turno = new Division();
        div_turno.setId("div_turno");
        div_turno.dividir1(pat_turno);
        
        agregarComponente(div_turno);   

    }

   @Override
    public void insertar() {
        tab_turno.insertar();
    }

    @Override
    public void guardar() {
        tab_turno.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_turno.eliminar();
        
                
    }

    public Tabla getTab_turno() {
        return tab_turno;
    }

    public void setTab_turno(Tabla tab_turno) {
        this.tab_turno = tab_turno;
    }
    
    
}
