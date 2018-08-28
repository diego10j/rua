/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_contabilidad.ejb.ServicioContabilidad;
import servicios.cuentas_x_cobrar.ServicioCliente;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Nicolas Cajilema
 */
public class pre_modulo extends Pantalla  {
      private Tabla tab_modulo=new Tabla();
      private Tabla tab_modulo_secuencial=new Tabla();
      private Arbol arb_modulo = new Arbol();
      
 @EJB
    private final ServicioContabilidad ser_contabilidad= (ServicioContabilidad) utilitario.instanciarEJB(ServicioContabilidad.class);     
     
      public  pre_modulo (){
          tab_modulo.setId("tab_modulo");
          tab_modulo.setTabla("gen_modulo","ide_gemod",1);
          //tab_modulo.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true", ""));

          tab_modulo.setCampoPadre("GEN_IDE_GEMOD");//AGREGAMOS  LA RECURSIVIDAD COMO PADRE
          tab_modulo.setCampoNombre("DETALLE_GEMOD");//LO QUE CONTIENE LA TABLA PADRE DEN_IDE_GEMOD
          tab_modulo.agregarRelacion(tab_modulo_secuencial);
          tab_modulo.agregarArbol(arb_modulo);
          tab_modulo.setHeader("MODULO");
          tab_modulo.dibujar();
        
        PanelTabla pat_modulo = new PanelTabla();
        pat_modulo.setId("pat_modulo");
        pat_modulo.setPanelTabla(tab_modulo);
        
        
        tab_modulo_secuencial.setId("tab_modulo_secuencial");
        tab_modulo_secuencial.setTabla("gen_modulo_secuencial","ide_gemos",2);
        tab_modulo_secuencial.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true", ""));
        tab_modulo_secuencial.setHeader("MODULU SECUENCIAL");
        tab_modulo_secuencial.dibujar();
        
        PanelTabla pat_modulo_secuencial = new PanelTabla();
        pat_modulo_secuencial.setId("pat_modulo_secuencial");
        pat_modulo_secuencial.setPanelTabla(tab_modulo_secuencial);
        
      
        arb_modulo.setId("arb_modulo");
        arb_modulo.dibujar();
  
        
      Division div_modulo = new Division();
      div_modulo.setId("div_modulo");
      Division div_arbol= new Division();
      div_arbol.dividir2(pat_modulo,pat_modulo_secuencial,"50%","H");
      div_modulo.dividir2(arb_modulo,div_arbol,"20%","V");
      
        
        agregarComponente(div_modulo);   

     }

   @Override
    public void insertar() {
        if(tab_modulo.isFocus()){
        tab_modulo.insertar();
        }
        else if(tab_modulo_secuencial.isFocus()){
            tab_modulo_secuencial.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_modulo.isFocus()){
        tab_modulo.guardar();
        }
        else if(tab_modulo_secuencial.isFocus()){
            tab_modulo_secuencial.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if(tab_modulo.isFocus()){
        tab_modulo.eliminar();
        }
        else if(tab_modulo_secuencial.isFocus()){
            tab_modulo_secuencial.eliminar();
           }
    }

    public Tabla getTab_modulo() {
        return tab_modulo;
    }

    public void setTab_modulo(Tabla tab_modulo) {
        this.tab_modulo = tab_modulo;
    }

    public Tabla getTab_modulo_secuencial() {
        return tab_modulo_secuencial;
    }

    public void setTab_modulo_secuencial(Tabla tab_modulo_secuencial) {
        this.tab_modulo_secuencial = tab_modulo_secuencial;
    }

    public Arbol getArb_modulo() {
        return arb_modulo;
    }

    public void setArb_modulo(Arbol arb_modulo) {
        this.arb_modulo = arb_modulo;
    }

   
    
}
                
    
