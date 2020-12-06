/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

/**
 *
 * @author ANDRES
 */

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import servicios.activos.ServicioActivosFijos;
import sistema.aplicacion.Pantalla;

public class pre_clase_articulo extends Pantalla{
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
 @EJB
    private final ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);    
    public pre_clase_articulo (){
        tab_tabla1.setId("tab_tabla1");   //identificador
        tab_tabla1.setTabla("ACT_CLASE_ACTIVO", "IDE_ACCLA", 1);
        tab_tabla1.getColumna("ide_accls").setCombo(ser_activos.getSqlClaseActivo());
        
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
       
        
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir1(pat_tabla1);
        agregarComponente(div_tabla1);
        
    }
    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()){
        tab_tabla1.insertar();
        }
       
    }

    @Override
    public void guardar() {
        if (tab_tabla1.isFocus()){
        tab_tabla1.guardar();
        }
        
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       if (tab_tabla1.isFocus()){
        tab_tabla1.eliminar();
        }
    
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}
