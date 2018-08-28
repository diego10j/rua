/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioEmpleado;
import paq_produccion.ejb.ServicioProduccion;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Nicolas Cajilema
 */
public class pre_proforma extends Pantalla {
     private Tabla tab_proforma=new Tabla();
     private Tabla tab_detalle_proforma=new Tabla();
     
   @EJB
    private final ServicioCuentasCxP ser_cuentascxp= (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class); 
       
   @EJB
    private final ServiciosAdquisiones ser_persona= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
    
    @EJB
    private final ServiciosAdquisiones ser_empleado= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
     @EJB
    private final ServicioEmpleado ser_cargoempleado= (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class); 
     @EJB
    private final ServicioProduccion ser_valtiempo= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
    
    @EJB
    private final ServicioProduccion ser_unidad= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
     @EJB
    private final ServiciosAdquisiones ser_articulo= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
    
   
   
    public  pre_proforma (){
        tab_proforma.setId("tab_proforma");
        tab_proforma.setTabla("prod_proforma","ide_prpro",1);
        tab_proforma.getColumna("iva_prpro").setEtiqueta();//etiqueta
        tab_proforma.getColumna("iva_prpro").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_proforma.getColumna("subtotal_prpro").setEtiqueta();//etiqueta
        tab_proforma.getColumna("subtotal_prpro").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_proforma.getColumna("total_prpro").setEtiqueta();//etiqueta
        tab_proforma.getColumna("total_prpro").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_proforma.getColumna("ide_cndfp").setCombo(ser_cuentascxp.getFormaPagolista());
        tab_proforma.getColumna("ide_geper").setCombo(ser_persona.getDatosProveedor());
        tab_proforma.getColumna("ide_gtemp").setCombo(ser_empleado.getDatosEmpleado());
        tab_proforma.getColumna("gth_ide_gtemp").setCombo(ser_empleado.getDatosEmpleado());
        tab_proforma.getColumna("gth_ide_gtemp3").setCombo(ser_empleado.getDatosEmpleado());
        tab_proforma.getColumna("ide_gtcar").setCombo(ser_cargoempleado.getCargoEmpleado());
        tab_proforma.getColumna("ide_prvat").setCombo(ser_valtiempo.getValidezTiempo());
        tab_proforma.setTipoFormulario(true);
        tab_proforma.getGrid().setColumns(4);// Poner cuatro columnas
        tab_proforma.agregarRelacion(tab_detalle_proforma);  //agrego relacion para las dos tablas tab_proforma padre e hijo and tab_detalle_proforma
        tab_proforma.setHeader("PROFORMA");
        tab_proforma.dibujar();
        
        PanelTabla pat_proforma = new PanelTabla(); // cada tabla debe contener su panel y su divicion 
        pat_proforma.setId("pat_proforma");
        pat_proforma.setPanelTabla(tab_proforma);
        
        
        tab_detalle_proforma.setId("tab_detalle_proforma");
        tab_detalle_proforma.setTabla("prod_detalle_proforma","ide_prdep",2);
        tab_detalle_proforma.getColumna("ide_inuni").setCombo(ser_unidad.getUnidad());
        tab_detalle_proforma.getColumna("cantidad_prdep").setMetodoChange("CalcularSuma");
        tab_detalle_proforma.getColumna("valor_unitario_prdep").setMetodoChange("CalcularSuma");
        tab_detalle_proforma.getColumna("valor_total_prdep").setEtiqueta();
        tab_detalle_proforma.getColumna("valor_total_prdep").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
        tab_detalle_proforma.getColumna("cantidad_prdep").setValorDefecto("0");
        tab_detalle_proforma.getColumna("valor_unitario_prdep").setValorDefecto("0");


        


        tab_detalle_proforma.getColumna("ide_inarti").setCombo(ser_articulo.getMaterial("", ""));
        tab_detalle_proforma.setHeader("DETALLE PROFORMA");
        tab_detalle_proforma.dibujar();
        
        PanelTabla pat_detalle_proforma = new PanelTabla();
        pat_detalle_proforma.setId("pat_detalle_proforma");
        pat_detalle_proforma.setPanelTabla(tab_detalle_proforma);
        
        
        
        Division div_proforma = new Division();
        div_proforma.setId("div_proforma");
        div_proforma.dividir2(pat_proforma,pat_detalle_proforma,"50%","H" );
        
        agregarComponente(div_proforma);   

    }
    public void CalcularSuma(AjaxBehaviorEvent evt){
		tab_detalle_proforma.modificar(evt); // Metodo para calcular la suma 
		double valor_precio=0;
		double valor_unitario=0;
                double valor_iva=0;
                double subtotal=0;
                double valor_total=0;
                double valor_iva_calculado=0;
                        
                
                
                double suma=0;
              
              valor_iva=Double.parseDouble(utilitario.getVariable("p_sri_porcentajeIva_comp_elect")); //tomado de parametro sri
              valor_precio=Double.parseDouble(tab_detalle_proforma.getValor("cantidad_prdep"));
              valor_unitario=Double.parseDouble(tab_detalle_proforma.getValor("valor_unitario_prdep"));
              suma=valor_precio*valor_unitario;
              

              tab_detalle_proforma.setValor("valor_total_prdep", suma+""); 
              utilitario.addUpdateTabla(tab_detalle_proforma, "valor_total_prdep","");
              subtotal=Double.parseDouble(tab_detalle_proforma.getSumaColumna("valor_total_prdep")+"");
              
                            valor_iva_calculado=(subtotal*valor_iva)/100;
                            valor_total=subtotal+valor_iva_calculado;
              
              //Para sumar en el padre//
              tab_proforma.setValor("subtotal_prpro",subtotal+"");
              tab_proforma.setValor("iva_prpro",valor_iva_calculado+"");
              tab_proforma.setValor("total_prpro",valor_total+"");
              tab_proforma.modificar(tab_proforma.getFilaActual());
	      utilitario.addUpdateTabla(tab_proforma, "subtotal_prpro,iva_prpro,total_prpro","");
              
              

             
    }
            

   @Override
    public void insertar() {
        if(tab_proforma.isFocus()){
        tab_proforma.insertar();
        }
        else if(tab_detalle_proforma.isFocus()){
            tab_detalle_proforma.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_proforma.isFocus()){
        tab_proforma.guardar();
        }
        else if(tab_detalle_proforma.isFocus()){
            tab_detalle_proforma.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if(tab_proforma.isFocus()){
        tab_proforma.eliminar();
        }
        else if(tab_detalle_proforma.isFocus()){
            tab_detalle_proforma.eliminar();
           }
        }
                
    

    public Tabla getTab_proforma() {
        return tab_proforma;
    }

    public void setTab_proforma(Tabla tab_proforma) {
        this.tab_proforma = tab_proforma;
    }

    public Tabla getTab_detalle_proforma() {
        return tab_detalle_proforma;
    }

    public void setTab_detalle_proforma(Tabla tab_detalle_proforma) {
        this.tab_detalle_proforma = tab_detalle_proforma;
    }
    
    
}

