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
public class pre_trans_tip_cuen extends Pantalla{
    private Tabla tab_tip_cuen = new Tabla();
    
    public pre_trans_tip_cuen (){
        
        tab_tip_cuen.setId("tab_tip_cuen");
        tab_tip_cuen.setTabla("transfer_tipo_cuenta","ide_tratic",1);
        tab_tip_cuen.getColumna("ide_tratic");
        tab_tip_cuen.getColumna("codigo_tratic");
        tab_tip_cuen.getColumna("detalle_tratic");
        tab_tip_cuen.dibujar();
        
        PanelTabla pat_tip_cuen= new PanelTabla();
        pat_tip_cuen.setId("pat_tip_cuen");
        pat_tip_cuen.setPanelTabla(tab_tip_cuen);
        
        Division div_tip_cuen = new Division();
        div_tip_cuen.setId("div_tip_cuen");
        div_tip_cuen.dividir1(pat_tip_cuen);
        agregarComponente(div_tip_cuen);
    }

    public Tabla getTab_tip_cuen() {
        return tab_tip_cuen;
    }

    public void setTab_tip_cuen(Tabla tab_tip_cuen) {
        this.tab_tip_cuen = tab_tip_cuen;
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
