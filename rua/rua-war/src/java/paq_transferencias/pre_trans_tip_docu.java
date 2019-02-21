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
public class pre_trans_tip_docu extends Pantalla {
    
    private Tabla tab_tipo_documento = new Tabla();
    
    public pre_trans_tip_docu (){
        
        tab_tipo_documento.setId("tab_tipo_documento");
        tab_tipo_documento.setTabla("transfer_tipo_documento","ide_trantpd",1);
        tab_tipo_documento.getColumna("ide_trantpd");
        tab_tipo_documento.getColumna("detalle_trantpd");
        tab_tipo_documento.dibujar();
        
        
        PanelTabla pat_tip_docu = new PanelTabla();
        pat_tip_docu.setId("pat_tip_docu");
        pat_tip_docu.setPanelTabla(tab_tipo_documento);
        
        Division div_tip_docu = new Division();
        div_tip_docu.setId("div_tip_docu");
        div_tip_docu.dividir1(pat_tip_docu);
        agregarComponente(div_tip_docu);
           
        
    }

    public Tabla getTab_tipo_documento() {
        return tab_tipo_documento;
    }

    public void setTab_tipo_documento(Tabla tab_tipo_documento) {
        this.tab_tipo_documento = tab_tipo_documento;
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
    
