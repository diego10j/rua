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
        tab_tabla1.agregarRelacion(tab_tabla2);
        
        tab_tabla1.dibujar();
        PanelTabla pat_tabla1 = new PanelTabla();
        pat_tabla1.setId("pat_tabla1");
        pat_tabla1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("act_asiento_activo", "ide_acacc", 2);
		tab_tabla2.getColumna("IDE_GETIA").setCombo("GEN_TIPO_ASIENTO", "IDE_GETIA", "DETALLE_GETIA", "");
		tab_tabla2.getColumna("IDE_GETIA").setVisible(false);
                tab_tabla2.getColumna("IDE_GELUA").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
		tab_tabla2.getColumna("IDE_GECUC").setCombo("GEN_CUENTA_CONTABLE", "IDE_GECUC", "CODIGO_CUENTA_GECUC,DETALLE_GECUC", "");
		tab_tabla2.getColumna("IDE_GECUC").setAutoCompletar();
                tab_tabla2.getColumna("IDE_GECUC").setVisible(false);
		//tab_tabla2.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		tab_tabla2.getColumna("ide_cocac").setVisible(false);
                tab_tabla2.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc||' '||nombre_cndpc", "");
                tab_tabla2.getColumna("ide_cndpc").setAutoCompletar();
                tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("RUBROS PARA ASIENTO CONTABLE");		
		pat_panel2.setPanelTabla(tab_tabla2);
       
        
        Division div_tabla1 = new Division();
        div_tabla1.setId("div_tabla1");
        div_tabla1.dividir2(pat_tabla1, pat_panel2, "50%", "H");
        agregarComponente(div_tabla1);
        
    }
    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()){
        tab_tabla1.insertar();
        }
        else if (tab_tabla2.isFocus()){
        tab_tabla2.insertar();
        }
       
    }

    @Override
    public void guardar() {
        if (tab_tabla1.guardar()){
        if (tab_tabla2.guardar()){
            guardarPantalla();
        }
        }
        
        
    }

    @Override
    public void eliminar() {
       if (tab_tabla1.isFocus()){
        tab_tabla1.eliminar();
        }
       if (tab_tabla2.isFocus()){
        tab_tabla2.eliminar();
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
