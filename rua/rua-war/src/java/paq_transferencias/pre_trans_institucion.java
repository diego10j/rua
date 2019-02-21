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
public class pre_trans_institucion extends Pantalla {
    
    private Tabla tab_institucion = new Tabla();
    
    public  pre_trans_institucion (){
        
        tab_institucion.setId("tab_institucion");
        tab_institucion.setTabla("tranfer_institucion","ide_trani",1);
        tab_institucion.getColumna("ide_trains");
        tab_institucion.getColumna("detalle_trains");
        tab_institucion.getColumna("codigo_trains");
        tab_institucion.getColumna("direccion_trains");
        tab_institucion.dibujar();
       
        
        PanelTabla pat_institucion= new PanelTabla();
        pat_institucion.setId("pat_tip_docu");
        pat_institucion.setPanelTabla(tab_institucion);
        
        Division div_institucion = new Division();
        div_institucion.setId("div_institucion");
        div_institucion.dividir1(pat_institucion);
        agregarComponente(div_institucion);
        
        
    }

    public Tabla getTab_institucion() {
        return tab_institucion;
    }

    public void setTab_institucion(Tabla tab_institucion) {
        this.tab_institucion = tab_institucion;
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
