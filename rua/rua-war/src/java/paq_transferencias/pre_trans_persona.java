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
public class pre_trans_persona extends Pantalla{
    
    private Tabla tab_persona = new Tabla();
    
    public pre_trans_persona (){
        
        tab_persona.setId("tab_persona");
        tab_persona.setTabla("tranfer_persona","ide_traper",1);
        tab_persona.getColumna("ide_traper");
        tab_persona.getColumna("ide_trains");
        tab_persona.getColumna("ide_tratic");
        tab_persona.getColumna("ide_tratpd");
        tab_persona.getColumna("apellido_traper"); 
        tab_persona.getColumna("nombre_traper");
        tab_persona.getColumna("documento_traper");
        tab_persona.getColumna("nro_cuenta_traper");
        tab_persona.getColumna("correo_traper");
        tab_persona.getColumna("celular_traper");
        tab_persona.getColumna("convencional_traper");
        tab_persona.getColumna("direccion_traper");
        tab_persona.getColumna("ciudad_traper");
        tab_persona.getColumna("es_emp_traper");
        tab_persona.getColumna("es_provee_traper");
        tab_persona.getColumna("es_cytibank_traper");  
        tab_persona.setTipoFormulario(true);
        tab_persona.dibujar();
        
        PanelTabla pat_persona =new PanelTabla();      
        pat_persona.setId("pat_persona");
        pat_persona.setPanelTabla(tab_persona);
        
        Division div_persona = new Division();
        div_persona.setId("div_perosna");
        div_persona.dividir1(div_persona);
        agregarComponente(div_persona);
        
    }

    public Tabla getTab_persona() {
        return tab_persona;
    }

    public void setTab_persona(Tabla tab_persona) {
        this.tab_persona = tab_persona;
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
