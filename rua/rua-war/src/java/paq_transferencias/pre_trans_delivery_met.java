/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transferencias;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author mauricio
 */
public class pre_trans_delivery_met extends Pantalla {
    
    private Tabla tab_delivery = new Tabla();
    public pre_trans_delivery_met(){
        
        tab_delivery.setId("tab_delivery");
        tab_delivery.setTabla("transfer_delivery_met","ide_tradem",1);
        tab_delivery.getColumna("ïde_tadem");
        tab_delivery.getColumna("codigo_tradem");
        tab_delivery.getColumna("detalle_tradem");
        tab_delivery.dibujar();
        
        
       PanelTabla pat_delivery= new PanelTabla();
        pat_delivery.setId("pat_delivery");
        pat_delivery.setPanelTabla(tab_delivery);
        
        Division div_delivery = new Division();
        div_delivery.setId("div_delivery");
        div_delivery.dividir1(pat_delivery);
        agregarComponente(div_delivery);
    }

    public Tabla getTab_delivery() {
        return tab_delivery;
    }

    public void setTab_delivery(Tabla tab_delivery) {
        this.tab_delivery = tab_delivery;
    }
    
    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
