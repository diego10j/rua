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
 * @author HP_PRO
 */
public class pre_trans_documento_trans extends Pantalla {
    
    private Tabla tab_docu_trans =new Tabla();
    
    public pre_trans_documento_trans(){
        
        tab_docu_trans.setId("tab_docu_trans");
        tab_docu_trans.setTabla("transfer_documento_trans","ide_tradot",1);
        tab_docu_trans.getColumna("ide_tradot");
        tab_docu_trans.getColumna("detalle_tradot");
        tab_docu_trans.getColumna("codigo_tradot");
        tab_docu_trans.dibujar();
        
        PanelTabla pat_docu_trans= new PanelTabla();
        pat_docu_trans.setId("pat_docu_trans");
        pat_docu_trans.setPanelTabla(tab_docu_trans);
        
        Division div_docu_trans= new Division();
        div_docu_trans.setId("div_docu_trans");
        div_docu_trans.dividir1(pat_docu_trans);
        agregarComponente(div_docu_trans);
    }

    public Tabla getTab_docu_trans() {
        return tab_docu_trans;
    }

    public void setTab_docu_trans(Tabla tab_docu_trans) {
        this.tab_docu_trans = tab_docu_trans;
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
