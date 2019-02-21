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
public class pre_trans_codigo_trans extends Pantalla {
    
    private Tabla tab_codigo  = new Tabla();
    
    public pre_trans_codigo_trans (){
        
        tab_codigo.setId("tab_codigo");
        tab_codigo.setTabla("transfer_codigo_trans","",1);
        tab_codigo.getColumna("ide_trancot");
        tab_codigo.getColumna("detalle_trancot");
        tab_codigo.getColumna("codigo_trancot");
        tab_codigo.getColumna("ban_agency_trancot");
        tab_codigo.dibujar();
        
        PanelTabla pat_codigo = new PanelTabla();
        pat_codigo.setId("pat_codigo");
        pat_codigo.setPanelTabla(tab_codigo);
        
        Division div_codigo = new Division();
        div_codigo.setId("div_codigo");
        div_codigo.dividir1(pat_codigo);
        agregarComponente(div_codigo);
                  
    }

    public Tabla getTab_codigo() {
        return tab_codigo;
    }

    public void setTab_codigo(Tabla tab_codigo) {
        this.tab_codigo = tab_codigo;
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
