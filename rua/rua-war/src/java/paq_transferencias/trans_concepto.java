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
 * @author amuricio
 */
public class trans_concepto extends Pantalla{
    
    private Tabla tab_concepto = new Tabla();
    public trans_concepto (){ 
    
    tab_concepto.setId("tab_concepto");
    tab_concepto.setTabla("transfer_concepto","ide_trancon",1);
    tab_concepto.getColumna("ide_trancon");
    tab_concepto.getColumna("detalle_trancon");
    tab_concepto.getColumna("codigo_trancon");
    tab_concepto.dibujar();
    
    PanelTabla pat_codigo = new PanelTabla();
    pat_codigo.setId("pat_codigo");
    pat_codigo.setPanelTabla(tab_concepto);
    
    Division div_codigo = new Division();
    div_codigo.setId("div_codigo");
    div_codigo.dividir1(tab_concepto);
    agregarComponente(div_codigo);
          
            
    }

    public Tabla getTab_concepto() {
        return tab_concepto;
    }

    public void setTab_concepto(Tabla tab_concepto) {
        this.tab_concepto = tab_concepto;
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
