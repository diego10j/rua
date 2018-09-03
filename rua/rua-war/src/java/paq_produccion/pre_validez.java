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
public class pre_validez extends Pantalla{
private Tabla tab_validez=new Tabla();
    
    public  pre_validez (){
        tab_validez.setId("tab_validez");
        tab_validez.setTabla("prod_validez_tiempo","ide_prvat",1);
        tab_validez.setHeader("VALIDEZ TIEMPO");
        tab_validez.dibujar();
        
        PanelTabla pat_validez = new PanelTabla();
        pat_validez.setId("pat_validez");
        pat_validez.setPanelTabla(tab_validez);
        
        
        Division div_validez = new Division();
        div_validez.setId("div_validez");
        div_validez.dividir1(pat_validez);
        
        agregarComponente(div_validez);   

    }

@Override
    public void insertar() {
        tab_validez.insertar();
    }

@Override
    public void guardar() {
        tab_validez.guardar();
        guardarPantalla();
    }

@Override
    public void eliminar() {
        tab_validez.eliminar();
        
                
    }

    public Tabla getTab_validez() {
        return tab_validez;
    }

    public void setTab_validez(Tabla tab_color) {
        this.tab_validez = tab_color;
    }

    
    
    
}
