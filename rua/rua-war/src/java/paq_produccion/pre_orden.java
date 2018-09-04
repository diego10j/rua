/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_gestion.ejb.ServicioEmpleado;
import paq_produccion.ejb.ServicioProduccion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Nicolas Cajilema
 */
public class pre_orden extends Pantalla{
    private Tabla tab_orden_produccion=new Tabla();
    private Tabla tab_detalle_orden=new Tabla();
    private Tabla tab_control_produccion=new Tabla();
    private Tabla tab_proforma_orden=new Tabla();
    private MenuPanel menup = new MenuPanel();
    
    @EJB
    private final ServiciosAdquisiones ser_persona= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
    
    @EJB
    private final ServiciosAdquisiones ser_empleado= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
     @EJB
    private final ServicioEmpleado ser_cargoempleado= (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class); 
     @EJB
    private final ServicioProduccion ser_unidad= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
     @EJB
    private final ServiciosAdquisiones ser_articulo= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
     @EJB
    private final ServicioProduccion ser_turno= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
      @EJB
    private final ServicioProduccion ser_maquina= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
     @EJB
    private final ServicioProduccion ser_orden_produccion= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
     
     
    private int int_opcion=0;
      public  pre_orden (){
          
          menup.setMenuPanel("ORDEN PRODUCION", "22%");
          menup.setTransient(true);
          menup.agregarItem("ORDEN", "dibujaOrden", "ui-icon-cart");
          menup.agregarSubMenu("ORDENES DE PRODUCION");
          
          menup.agregarItem("CONTROL PRODUCCION", "dibujaControl", "ui-icon-home");
          menup.agregarSubMenu("CONTROL PRODUCCION");
          
          menup.agregarItem("PROFORMA ORDEN", "dibujaProforma", "ui-icon-note");
          menup.agregarSubMenu("PROFORMA ORDEN");
        
        agregarComponente(menup);   

    }
      public void dibujaProforma(){
          int_opcion=3;
          tab_proforma_orden.setId("tab_proforma_orden");
          tab_proforma_orden.setTabla("prod_proforma_orden","ide_prprof",4);
          tab_proforma_orden.setHeader("PROFORMA ORDEN");
          tab_proforma_orden.dibujar();
          
          PanelTabla pat_proforma_orden = new PanelTabla();
          pat_proforma_orden.setId("pat_proforma_orden");
          pat_proforma_orden.setPanelTabla( tab_proforma_orden);
          
          
         Division div_proforma_orden = new Division();
         div_proforma_orden.setId("div_proforma_orden");
         div_proforma_orden.dividir1(pat_proforma_orden );
         agregarComponente(div_proforma_orden);
         menup.dibujar(3, "PROFORMA ORDEN",div_proforma_orden );
          
          
          
          
          
      }
      public  void dibujaControl(){
          int_opcion=2;
          tab_control_produccion.setId("tab_control_produccion");
          tab_control_produccion.setTabla("prod_control_produccion","ide_prcop",3);
          tab_control_produccion.getColumna("ide_prorp").setCombo(ser_orden_produccion.getOrdenProduccion());
          tab_control_produccion.getColumna("ide_gtemp").setCombo(ser_empleado.getDatosEmpleado());
          tab_control_produccion.getColumna("gth_ide_gtemp").setCombo(ser_empleado.getDatosEmpleado());
          tab_control_produccion.getColumna("gth_ide_gtemp2").setCombo(ser_empleado.getDatosEmpleado());
          tab_control_produccion.getColumna("ide_prtur").setCombo(ser_turno.getTurno());
          tab_control_produccion.getColumna("ide_prmaq").setCombo(ser_maquina.getMaquina()); 
          tab_control_produccion.setTipoFormulario(true);
          tab_control_produccion.getGrid().setColumns(4);
          tab_control_produccion.setHeader("CONTROL PRODUCCION");
          tab_control_produccion.dibujar();
      
          
          PanelTabla pat_control_produccion = new PanelTabla();
          pat_control_produccion.setId("pat_control_produccion");
          pat_control_produccion.setPanelTabla(tab_control_produccion);
          
          
         Division div_control_produccion = new Division();
         div_control_produccion.setId("div_orden_produccion");
         div_control_produccion.dividir1(pat_control_produccion );
         agregarComponente(div_control_produccion);
         menup.dibujar(2, "CONTROL PRODUCCION",div_control_produccion );

        
      
      }
      public void dibujaOrden(){
          int_opcion=1;
            
        tab_orden_produccion.setId("tab_orden_produccion");
        tab_orden_produccion.setTabla("prod_orden_produccion","ide_prorp",1);
        tab_orden_produccion.getColumna("ide_gtemp").setCombo(ser_empleado.getDatosEmpleado());
        tab_orden_produccion.getColumna("ide_geper").setCombo(ser_persona.getDatosProveedor());
        tab_orden_produccion.getColumna("ide_gtcar").setCombo(ser_cargoempleado.getCargoEmpleado());
        tab_orden_produccion.setTipoFormulario(true);
        tab_orden_produccion.getGrid().setColumns(4);
        tab_orden_produccion.agregarRelacion(tab_detalle_orden);  
        tab_orden_produccion.setHeader("ORDEN PRODUCCION");
        tab_orden_produccion.dibujar();
        
        PanelTabla pat_orden_produccion = new PanelTabla();
        pat_orden_produccion.setId("pat_orden_produccion");
        pat_orden_produccion.setPanelTabla(tab_orden_produccion);
        
        //--- tab_detalle_orden//
        
        tab_detalle_orden.setId("tab_detalle_orden");
        tab_detalle_orden.setTabla("prod_orden_detalle","ide_prord",2);
        tab_detalle_orden.getColumna("ide_inuni").setCombo(ser_unidad.getUnidad());
        tab_detalle_orden.getColumna("ide_inarti").setCombo(ser_articulo.getMaterial("", ""));
        tab_detalle_orden.setTipoFormulario(true);
        //tab_detalle_orden.getGrid().setColumns(4);
        tab_detalle_orden.setHeader("ORDEN  DETALLE");
        tab_detalle_orden.dibujar();
        
        
        PanelTabla pat_detalle_orden = new PanelTabla();
        pat_detalle_orden.setId("pat_detalle_orden");
        pat_detalle_orden.setPanelTabla(tab_detalle_orden);
        
        Division div_orden_produccion = new Division();
        div_orden_produccion.setId("div_orden_produccion");
        div_orden_produccion.dividir2(pat_orden_produccion,pat_detalle_orden,"50%","H" );
          agregarComponente(div_orden_produccion);
          menup.dibujar(1, "ORDEN",div_orden_produccion );
      }

   @Override
   
    public void insertar() {
         if (int_opcion==1) {
        if(tab_orden_produccion.isFocus()){
        tab_orden_produccion.insertar();
        }
        else if(tab_detalle_orden.isFocus()){
            tab_detalle_orden.insertar();
        }
         }
         
      if(int_opcion==2) {
          if(tab_control_produccion.isFocus()){
              tab_control_produccion.insertar();
          }
      }   
      if(int_opcion==3)
          if(tab_proforma_orden.isFocus()){
              tab_proforma_orden.insertar();
          }
    }

    @Override
    public void guardar() {
        if (int_opcion==1) {
        if(tab_orden_produccion.isFocus()){
        tab_orden_produccion.guardar();
        }
        else if(tab_detalle_orden.isFocus()){
            tab_detalle_orden.guardar();
        }
         }
         
      if(int_opcion==2) {
          if(tab_control_produccion.isFocus()){
              tab_control_produccion.guardar();
          }
      }   
      if(int_opcion==3)
          if(tab_proforma_orden.isFocus()){
              tab_proforma_orden.guardar();
          }
      guardarPantalla();
    }

    @Override
    public void eliminar() {
         if (int_opcion==1) {
        if(tab_orden_produccion.isFocus()){
        tab_orden_produccion.eliminar();
        }
        else if(tab_detalle_orden.isFocus()){
            tab_detalle_orden.eliminar();
        }
         }
         
      if(int_opcion==2) {
          if(tab_control_produccion.isFocus()){
              tab_control_produccion.eliminar();
          }
      }   
      if(int_opcion==3)
          if(tab_proforma_orden.isFocus()){
              tab_proforma_orden.eliminar();
          }
    }

    public Tabla getTab_orden_produccion() {
        return tab_orden_produccion;
    }

    public void setTab_orden_produccion(Tabla tab_orden_produccion) {
        this.tab_orden_produccion = tab_orden_produccion;
    }

    public Tabla getTab_detalle_orden() {
        return tab_detalle_orden;
    }

    public void setTab_detalle_orden(Tabla tab_detalle_orden) {
        this.tab_detalle_orden = tab_detalle_orden;
    }

    public MenuPanel getMenup() {
        return menup;
    }

    public void setMenup(MenuPanel menup) {
        this.menup = menup;
    }

    public int getInt_opcion() {
        return int_opcion;
    }

    public void setInt_opcion(int int_opcion) {
        this.int_opcion = int_opcion;
    }

    public Tabla getTab_control_produccion() {
        return tab_control_produccion;
    }

    public void setTab_control_produccion(Tabla tab_control_produccion) {
        this.tab_control_produccion = tab_control_produccion;
    }

    public Tabla getTab_proforma_orden() {
        return tab_proforma_orden;
    }

    public void setTab_proforma_orden(Tabla tab_proforma_orden) {
        this.tab_proforma_orden = tab_proforma_orden;
    }
    
}   

 