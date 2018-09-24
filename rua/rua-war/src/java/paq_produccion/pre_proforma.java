/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package paq_produccion;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.Map;
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
     
     // Servicios//
   @EJB
    private final ServicioCuentasCxP ser_cuentascxp= (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class); 
       
   @EJB
    private final ServiciosAdquisiones ser_persona= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
    
      @EJB
    private final ServicioEmpleado ser_cargoempleado= (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class); 
     @EJB
    private final ServicioProduccion ser_valtiempo= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
    
       private VisualizarPDF vipdf_proforma = new VisualizarPDF();   

   
    public  pre_proforma (){
        tab_proforma.setId("tab_proforma");
        tab_proforma.setTabla("prod_proforma","ide_prpro",1);
        tab_proforma.getColumna("valor_descuento_prpro").setMetodoChange("CalcularDescuentoValor");
        tab_proforma.getColumna("por_descuento_prpro").setMetodoChange("CalcularDescuentoPorcentaje");
        tab_proforma.getColumna("iva_prpro").setEtiqueta();//etiqueta
        tab_proforma.getColumna("iva_prpro").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_proforma.getColumna("subtotal_prpro").setEtiqueta();//etiqueta
        tab_proforma.getColumna("subtotal_prpro").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_proforma.getColumna("total_prpro").setEtiqueta();//etiqueta
        tab_proforma.getColumna("numero_prpro").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_proforma.getColumna("numero_prpro").setEtiqueta();//etiqueta
        tab_proforma.getColumna("total_prpro").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_proforma.getColumna("ide_cndfp").setCombo(ser_cuentascxp.getFormaPagolista()); // llamada de servicios con get y fin//
        tab_proforma.getColumna("ide_geper").setCombo(ser_persona.getDatosProveedor());
        tab_proforma.getColumna("ide_geper").setAutoCompletar();
        tab_proforma.getColumna("ide_gtemp").setCombo(ser_persona.getDatosEmpleado());
        tab_proforma.getColumna("ide_gtemp").setAutoCompletar(); // El autocompletar se ejecuta cuando un combo ya este realizado y solo lleva esta linea//
        tab_proforma.getColumna("ide_gtcar").setCombo(ser_cargoempleado.getCargoEmpleado());
        tab_proforma.getColumna("ide_prvat").setCombo(ser_valtiempo.getValidezTiempo());
        tab_proforma.getColumna("pro_ide_prvat").setCombo(ser_valtiempo.getValidezTiempo());
        tab_proforma.getColumna("subtotal_prpro").setValorDefecto("0");// dejar valor x defecto cero
        tab_proforma.getColumna("iva_prpro").setValorDefecto("0");
        tab_proforma.getColumna("total_prpro").setValorDefecto("0");
        tab_proforma.getColumna("valor_descuento_prpro").setValorDefecto("0");
        tab_proforma.getColumna("por_descuento_prpro").setValorDefecto("0");
        tab_proforma.getColumna("fecha_prpro").setValorDefecto(utilitario.getFechaActual());// fecha actual x defecto
        //Para visualizacion de datos en la pantalla nombres de etiquetas //
        tab_proforma.getColumna("IDE_PRPRO").setNombreVisual("CODIGO");
        tab_proforma.getColumna("IDE_GTEMP").setNombreVisual("EMPLEADO ENCARGADO");
        tab_proforma.getColumna("IDE_GTCAR").setNombreVisual("CARGO");
        tab_proforma.getColumna("NUMERO_PRPRO ").setNombreVisual("NUMERO MÓDULO");
        tab_proforma.getColumna("SOLITADO_POR_PRPRO").setNombreVisual("SOLICITADO POR");
        tab_proforma.getColumna("POR_DESCUENTO_PRPRO").setNombreVisual("POR DESCUENTO");
        tab_proforma.getColumna("TOTAL_PRPRO").setNombreVisual("TOTAL");
        tab_proforma.getColumna("VALOR_DESCUENTO_PRPRO").setNombreVisual("VALOR DESCUENTO");
        tab_proforma.getColumna("IDE_CNDFP").setNombreVisual("FORMA PAGO");
        tab_proforma.getColumna("IDE_GEPER").setNombreVisual("RAZON SOCIAL");
        tab_proforma.getColumna("IDE_PRVAT").setNombreVisual("VALIDEZ OFERTA ");
        tab_proforma.getColumna("FECHA_PRPRO").setNombreVisual("FECHA");
        tab_proforma.getColumna("SUBTOTAL_PRPRO").setNombreVisual("SUBTOTAL");
        tab_proforma.getColumna("IVA_PRPRO").setNombreVisual("IVA");
        tab_proforma.getColumna("OBSERVACION_PRPRO").setNombreVisual("OBSERVACION");
        tab_proforma.getColumna("PRO_IDE_PRVAT").setNombreVisual("TIEMPO ENTREGA");
        ///Para visualizacion de datos fin//
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
        tab_detalle_proforma.getColumna("ide_inuni").setCombo(ser_persona.getUnidad());
        tab_detalle_proforma.getColumna("cantidad_prdep").setMetodoChange("CalcularSuma");
        tab_detalle_proforma.getColumna("valor_unitario_prdep").setMetodoChange("CalcularSuma");
        tab_detalle_proforma.getColumna("valor_total_prdep").setEtiqueta();
        tab_detalle_proforma.getColumna("valor_total_prdep").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");
        tab_detalle_proforma.getColumna("cantidad_prdep").setValorDefecto("0");
        tab_detalle_proforma.getColumna("valor_unitario_prdep").setValorDefecto("0");
        tab_detalle_proforma.getColumna("ide_inarti").setCombo(ser_persona.getMaterial("", ""));
        //Para visualizacion de datos en la pantalla nombres de etiquetas //
        tab_detalle_proforma.getColumna("IDE_PRDEP").setNombreVisual("CODIGO");
        tab_detalle_proforma.getColumna("IDE_INUNI").setNombreVisual("U/M");
        tab_detalle_proforma.getColumna("IDE_INARTI").setNombreVisual("DETALLE ARTICULO");
        tab_detalle_proforma.getColumna("CANTIDAD_PRDEP").setNombreVisual("CANTIDAD");
        tab_detalle_proforma.getColumna("VALOR_UNITARIO_PRDEP").setNombreVisual("VALOR UNITARIO");
        tab_detalle_proforma.getColumna("VALOR_TOTAL_PRDEP").setNombreVisual("VALOR TOTAL");
        ///Para visualizacion de datos fin//
        tab_detalle_proforma.setHeader("DETALLE PROFORMA");
        tab_detalle_proforma.dibujar();
        
        PanelTabla pat_detalle_proforma = new PanelTabla();
        pat_detalle_proforma.setId("pat_detalle_proforma");
        pat_detalle_proforma.setPanelTabla(tab_detalle_proforma);
        
        
        
        Division div_proforma = new Division();
        div_proforma.setId("div_proforma");
        div_proforma.dividir2(pat_proforma,pat_detalle_proforma,"50%","H" );
        
        agregarComponente(div_proforma);   
        
        
     
        Boton bot_anular = new Boton();
        bot_anular.setIcon("ui-icon-search");
        bot_anular.setValue("IMPRIMIR REPORTE");
        bot_anular.setMetodo("imprimir");
        bar_botones.agregarComponente(bot_anular);
        
         vipdf_proforma.setId("vipdf_proforma");
         vipdf_proforma.setTitle("PROFORMA");
        agregarComponente(vipdf_proforma);
    }//Metodo impresion PDF//
    public void imprimir(){
        if (tab_proforma.getValorSeleccionado() != null) {
                        ///////////AQUI ABRE EL REPORTE
                        Map parametros = new HashMap();
                        parametros.put("ide_prpro", Integer.parseInt(tab_proforma.getValor("ide_prpro")));
                        parametros.put("pide_modulo", Integer.parseInt(utilitario.getVariable("p_prod_numero_proforma")));

                        //System.out.println(" " + str_titulos);
                        vipdf_proforma.setVisualizarPDF("rep_produccion/rep_proforma.jasper", parametros);
                        vipdf_proforma.dibujar();
                        utilitario.addUpdate("vipdf_proforma");
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Solititud de proforma", "");
        }
    }
    public void CalcularSuma(AjaxBehaviorEvent evt){
		tab_detalle_proforma.modificar(evt); // Metodo para calcular la suma 
		double valor_precio=0;
		double valor_unitario=0;
                double subtotal=0;
                
                double suma=0;
              
              
              valor_precio=Double.parseDouble(tab_detalle_proforma.getValor("cantidad_prdep"));
              valor_unitario=Double.parseDouble(tab_detalle_proforma.getValor("valor_unitario_prdep"));
              suma=valor_precio*valor_unitario;
              

              tab_detalle_proforma.setValor("valor_total_prdep", utilitario.getFormatoNumero(suma, 2)+""); 
              utilitario.addUpdateTabla(tab_detalle_proforma, "valor_total_prdep","");
              subtotal=Double.parseDouble(tab_detalle_proforma.getSumaColumna("valor_total_prdep")+"");
              tab_proforma.setValor("subtotal_prpro",utilitario.getFormatoNumero(subtotal, 2)+"");
              utilitario.addUpdateTabla(tab_proforma, "subtotal_prpro","");
              CalcularTotales();
              

             
    }
      
           public void CalcularTotales(){ 
               double subtotal=0;
                double valor_total=0;
                double valor_iva_calculado=0;
                double valor_iva=0;
                double descuento=0;
               descuento=Double.parseDouble(tab_proforma.getValor("valor_descuento_prpro"));
                valor_iva=Double.parseDouble(utilitario.getVariable("p_sri_porcentajeIva_comp_elect")); //tomado de parametro sri
               subtotal=Double.parseDouble(tab_detalle_proforma.getSumaColumna("valor_total_prdep")+"");
              
                            valor_iva_calculado=((subtotal-descuento)*valor_iva)/100;
                            valor_total=(subtotal-descuento)+valor_iva_calculado;
              
              //Para sumar en el padre//
              tab_proforma.setValor("iva_prpro",utilitario.getFormatoNumero(valor_iva_calculado, 2)+"");
              tab_proforma.setValor("total_prpro",utilitario.getFormatoNumero(valor_total, 2)+"");
              tab_proforma.modificar(tab_proforma.getFilaActual());
	      utilitario.addUpdateTabla(tab_proforma, "iva_prpro,total_prpro","");
               
           }
    
           public void CalcularDescuentoPorcentaje(AjaxBehaviorEvent evt){ 
               double subtotal=0;
               double porcentaje=0;
               double descuentop=0;

                subtotal=Double.parseDouble(tab_proforma.getValor("subtotal_prpro"));
                porcentaje=Double.parseDouble(tab_proforma.getValor("por_descuento_prpro"));
                descuentop=(subtotal*porcentaje)/100;
                tab_proforma.setValor("valor_descuento_prpro", descuentop+"");
                utilitario.addUpdateTabla(tab_proforma, "valor_descuento_prpro","");
                
                CalcularTotales();
              
           }
           public void CalcularDescuentoValor(AjaxBehaviorEvent evt){ 
            double subtotal=0;
            double porcentaje=0;
            double porcentajedescuento=0;

            subtotal=Double.parseDouble(tab_proforma.getValor("subtotal_prpro"));
            porcentaje=Double.parseDouble(tab_proforma.getValor("valor_descuento_prpro"));
            porcentajedescuento=(100*porcentaje)/subtotal;
             tab_proforma.setValor("por_descuento_prpro", utilitario.getFormatoNumero(porcentajedescuento, 2)+"");
             utilitario.addUpdateTabla(tab_proforma, "por_descuento_prpro","");
             CalcularTotales();
               
           }
   @Override
    public void insertar() {
        if(tab_proforma.isFocus()){
        tab_proforma.insertar();
            TablaGenerica tab_secuencial=utilitario.consultar(ser_valtiempo.getSecuencialModulo(utilitario.getVariable("p_prod_numero_proforma")));
            tab_proforma.setValor("numero_prpro", tab_secuencial.getValor("nuevo_secuencial"));
                                   

        }
        else if(tab_detalle_proforma.isFocus()){
            tab_detalle_proforma.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_proforma.guardar()){
            
        if (tab_proforma.isFilaInsertada()){
          //  utilitario.getConexion().ejecutarSql(ser_valtiempo.getActualizarSecuencial(utilitario.getVariable("p_prod_numero_secuencial")));
            
        }
            tab_detalle_proforma.guardar();
            guardarPantalla();
        }
        
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

    public VisualizarPDF getVipdf_proforma() {
        return vipdf_proforma;
    }

    public void setVipdf_proforma(VisualizarPDF vipdf_proforma) {
        this.vipdf_proforma = vipdf_proforma;
    }
    
}

