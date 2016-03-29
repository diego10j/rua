/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Imprimir;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_organigrama extends Pantalla {
    
    private Organigrama org_organigrama = new Organigrama();
    private Dialogo dia_dialogo = new Dialogo();
    
    public pre_organigrama() {
        org_organigrama.setId("org_organigrama");
        org_organigrama.setMetodo("seleccionarNodo");
        bar_botones.limpiar();
        Imprimir bot_imprimr = new Imprimir();
        bot_imprimr.setNombreComponente("org_organigrama");       
        
        bar_botones.agregarComponente(bot_imprimr);
        
        Boton bot_actualizar = new Boton();
        bot_actualizar.setValue("Actualizar");
        bot_actualizar.setUpdate("org_organigrama");
        bot_actualizar.setIcon("ui-icon-refresh");
        bar_botones.agregarBoton(bot_actualizar);
        agregarComponente(org_organigrama);
        
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setWidth("50%");
        dia_dialogo.setHeight("50%");
        agregarComponente(dia_dialogo);
        
    }
    
    public void seleccionarNodo() {
        dia_dialogo.setHeader(org_organigrama.getSeleccionado());
        dia_dialogo.dibujar();
    }
    
    public void imprimir() {
        System.out.println("dxxxxx " + org_organigrama.getSeleccionado());
    }
    
    @Override
    public void insertar() {
    }
    
    @Override
    public void guardar() {
    }
    
    @Override
    public void eliminar() {
    }
    
    public Organigrama getOrg_organigrama() {
        return org_organigrama;
    }
    
    public void setOrg_organigrama(Organigrama org_organigrama) {
        this.org_organigrama = org_organigrama;
    }
    
    public Dialogo getDia_dialogo() {
        return dia_dialogo;
    }
    
    public void setDia_dialogo(Dialogo dia_dialogo) {
        this.dia_dialogo = dia_dialogo;
    }
}
