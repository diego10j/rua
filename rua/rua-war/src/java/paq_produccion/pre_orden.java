/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_gestion.ejb.ServicioEmpleado;
import paq_produccion.ejb.ServicioProduccion;
import pkg_contabilidad.cls_contabilidad;
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
    private double dou_base_calculada = 0;
    private double dou_total_produccion = 0;
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Map parametro = new HashMap();
    private SeleccionTabla sel_tab_proforma = new SeleccionTabla();
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
  
     @EJB
    private final ServicioEmpleado ser_cargoempleado= (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class); 
     @EJB
    private final ServicioProduccion ser_produccion= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
 
    private AutoCompletar aut_ord_produ = new AutoCompletar();

     private int int_opcion=0;
      public  pre_orden (){
        //  bar_botones.limpiar();
        //  bar_botones.agregarReporte();
            bar_botones.agregarComponente(new Etiqueta("ING.ORDEN PRODUCCIÓN"));
            aut_ord_produ.setId("aut_ord_produ");
            aut_ord_produ.setAutoCompletar(ser_produccion.getOrdenPro());
            aut_ord_produ.setSize(35);

            bar_botones.agregarComponente(aut_ord_produ);



          menup.setMenuPanel("MENÚ PRODUCCIÓN", "22%");
          menup.setTransient(true);
          menup.agregarItem("Orden de Producción", "dibujaOrden", "ui-icon-cart");        
          menup.agregarItem("Control de Producción", "dibujaControl", "ui-icon-home");
          menup.agregarItem("Agregar Proforma / Orden", "dibujaProforma", "ui-icon-note");
         
        
        agregarComponente(menup);   
        
        Boton bot_anular = new Boton();
        bot_anular.setIcon("ui-icon-search");
        bot_anular.setValue("IMPRIMIR REPORTES");
        bot_anular.setMetodo("abrirListaReportes");
        
        bar_botones.agregarBoton(bot_anular);

        rep_reporte.setId("rep_reporte");
       rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
       agregarComponente(rep_reporte);
       bar_botones.agregarReporte();
       sel_rep.setId("sel_rep");
       agregarComponente(sel_rep);
          agregarComponente(rep_reporte);
          
          sel_tab_proforma.setId("sel_tab_proforma");
          sel_tab_proforma.setTitle("PROFORMA");
          sel_tab_proforma.setSeleccionTabla(ser_produccion.getProforma(), "ide_prpro");
          sel_tab_proforma.setWidth("80%");
          sel_tab_proforma.setHeight("70%");
          sel_tab_proforma.setRadio();
          sel_tab_proforma.getBot_aceptar().setMetodo("aceptarProforma");
          agregarComponente(sel_tab_proforma);
    }
      public void dibujaProforma(){
          int_opcion=3;
          tab_proforma_orden.setId("tab_proforma_orden");
          tab_proforma_orden.setTabla("prod_proforma_orden","ide_prprof",4);
          tab_proforma_orden.setCondicion("ide_prprof=" + aut_ord_produ.getValor());
          tab_proforma_orden.setHeader("PROFORMA ORDEN");
          tab_proforma_orden.dibujar();
          
          Grid gri_proformao = new Grid();
          gri_proformao.setColumns(3);
          Boton bot_busca_proforma = new Boton();
          bot_busca_proforma.setValue("BUSCAR PROFORMA");
          bot_busca_proforma.setIcon("ui-icon-search");
          bot_busca_proforma.setMetodo("abrirProforma");
          gri_proformao.getChildren().add(bot_busca_proforma);
          
          PanelTabla pat_proforma_orden = new PanelTabla();
          pat_proforma_orden.setId("pat_proforma_orden");
          pat_proforma_orden.getChildren().add(gri_proformao);
          pat_proforma_orden.setPanelTabla( tab_proforma_orden);
          
          
         Division div_proforma_orden = new Division();
         div_proforma_orden.setId("div_proforma_orden");
         div_proforma_orden.dividir1(pat_proforma_orden );
         agregarComponente(div_proforma_orden);
         menup.dibujar(3, "PROFORMA ORDEN",div_proforma_orden );
        
         
       
      }
      public void abrirProforma(){
          if (tab_proforma_orden.isFilaInsertada() == false){
              utilitario.agregarMensajeError("Debe seleccion una Orden de Produccion", "");
          } else {
              sel_tab_proforma.dibujar();
          }
          
      }
      public void aceptarProforma(){
           String ide_proforma = sel_tab_proforma.getValorSeleccionado();
          TablaGenerica tab_proforma_gen = utilitario.consultar("select ide_prpro, numero_prpro, fecha_prpro, b.nom_geper, total_prpro, observacion_prpro\n" +
                                                                "from prod_proforma a\n" +
                                                                "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
                                                                "where ide_prpro = "+ide_proforma+"");
          

          
              tab_proforma_orden.setValor("ide_prpro", tab_proforma_gen.getValor("ide_prpro"));
              tab_proforma_orden.modificar(tab_proforma_orden.getFilaActual());
//              utilitario.addUpdateTabla(tab_proforma_orden, "prod_proforma_orden", "ide_prpro");
              utilitario.addUpdate("tab_proforma_orden");
              sel_tab_proforma.cerrar();
          
      }
      public  void dibujaControl(){
          int_opcion=2;
          tab_control_produccion.setId("tab_control_produccion");
          tab_control_produccion.setTabla("prod_control_produccion","ide_prcop",3);
          tab_control_produccion.setCondicion("ide_prcop=" + aut_ord_produ.getValor());
          tab_control_produccion.getColumna("ide_prorp").setCombo(ser_produccion.getOrdenProduccion());
          tab_control_produccion.getColumna("ide_gtemp").setCombo(ser_adquisiciones.getDatosEmpleado());
          tab_control_produccion.getColumna("gth_ide_gtemp").setCombo(ser_adquisiciones.getDatosEmpleado());
          tab_control_produccion.getColumna("gth_ide_gtemp2").setCombo(ser_adquisiciones.getDatosEmpleado());
          tab_control_produccion.getColumna("ide_prtur").setCombo(ser_produccion.getTurno());
          tab_control_produccion.getColumna("ide_prmaq").setCombo(ser_produccion.getMaquina()); 
          tab_control_produccion.setTipoFormulario(true);
          tab_control_produccion.getGrid().setColumns(4);
          tab_control_produccion.setHeader("CONTROL PRODUCCION");
          tab_control_produccion.getColumna("numero_prcop").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
          tab_control_produccion.getColumna("numero_prcop").setEtiqueta();;
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
        tab_orden_produccion.setCondicion("ide_prorp=" + aut_ord_produ.getValor());
        tab_orden_produccion.getColumna("ide_gtemp").setCombo(ser_adquisiciones.getDatosEmpleado());
        tab_orden_produccion.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_orden_produccion.getColumna("ide_geper").setAutoCompletar(); // El autocompletar se ejecuta cuando un combo ya este realizado y solo lleva esta linea//
        tab_orden_produccion.getColumna("ide_gtcar").setCombo(ser_cargoempleado.getCargoEmpleado()); 
        tab_orden_produccion.getColumna("numero_prorp").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_orden_produccion.getColumna("numero_prorp").setEtiqueta();//etiqueta numero_modulo_prorp
        tab_orden_produccion.getColumna("numero_modulo_prorp").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_orden_produccion.getColumna("numero_modulo_prorp").setEtiqueta();//etiqueta numero_modulo_prorp
        tab_orden_produccion.setTipoFormulario(true);
        tab_orden_produccion.getGrid().setColumns(4);
        tab_orden_produccion.agregarRelacion(tab_detalle_orden);  
        tab_orden_produccion.setHeader("ORDEN PRODUCCION");
        tab_orden_produccion.getColumna("TOTAL_PRODUCION_PRORP").setEtiqueta();//etiqueta
        tab_orden_produccion.getColumna("TOTAL_PRODUCION_PRORP").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
        tab_orden_produccion.dibujar();
        
        PanelTabla pat_orden_produccion = new PanelTabla();
        pat_orden_produccion.setId("pat_orden_produccion");
        pat_orden_produccion.setPanelTabla(tab_orden_produccion);
        
        //--- tab_detalle_orden//
        
        tab_detalle_orden.setId("tab_detalle_orden");
        tab_detalle_orden.setTabla("prod_orden_detalle","ide_prord",2);
        tab_detalle_orden.getColumna("ide_inuni").setCombo(ser_produccion.getUnidad());
        tab_detalle_orden.getColumna("ide_inarti").setCombo(ser_adquisiciones.getMaterial("", ""));
        tab_detalle_orden.getColumna("ide_prcol").setCombo(ser_produccion.getColor());
        tab_detalle_orden.getColumna("BULTO_PAQUETE_PRORD").setMetodoChange("calculaTotalBultos");
        tab_detalle_orden.getColumna("UNIDADES_PRORD").setMetodoChange("calculaTotalBultos");
        tab_detalle_orden.getColumna("TOTAL_PRORD").setMetodoChange("calculaTotalBultos");
        tab_detalle_orden.getColumna("TOTAL_PRORD").setEtiqueta();
        tab_detalle_orden.getColumna("BULTO_PAQUETE_PRORD").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_detalle_orden.getColumna("UNIDADES_PRORD").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_detalle_orden.getColumna("TOTAL_PRORD").setEstilo("font-size:15px;font-weight: bold;color:blue");//Estilo
       // tab_detalle_orden.setTipoFormulario(true);
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
   
   public void calculaTotalBultos(AjaxBehaviorEvent evt){
       tab_detalle_orden.modificar(evt); 
        double valor_bultos=0;
        double valor_unidades=0;
        valor_bultos= Double.parseDouble(tab_detalle_orden.getValor("BULTO_PAQUETE_PRORD"));
        valor_unidades=Double.parseDouble(tab_detalle_orden.getValor("UNIDADES_PRORD"));
        dou_base_calculada = valor_bultos * valor_unidades;
        tab_detalle_orden.setValor("TOTAL_PRORD", utilitario.getFormatoNumero(dou_base_calculada, 2));
        tab_detalle_orden.modificar(tab_detalle_orden.getFilaActual());       
        tab_orden_produccion.setValor("TOTAL_PRODUCION_PRORP", tab_detalle_orden.getSumaColumna("total_prord")+"");//cuado quiera sumar las columnas solo getsumacolum
        tab_orden_produccion.modificar(tab_orden_produccion.getFilaActual());    
        utilitario.addUpdate("tab_detalle_orden");
        utilitario.addUpdate("tab_orden_produccion");
        /*
        tab_detalle_orden.guardar();
        tab_orden_produccion.guardar();
       guardarPantalla();
        */
   }
@Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista
        switch (int_opcion) {
            case 1:

                if (rep_reporte.getReporteSelecionado().equals("Orden de Produccion")) {
                    if (rep_reporte.isVisible()) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("pide_orden", Integer.parseInt(tab_orden_produccion.getValorSeleccionado()));
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep");
                        
                    }
                }                
              break;
            case 2:
                if (rep_reporte.getReporteSelecionado().equals("Control de Produccion")) {
                    if (rep_reporte.isVisible()) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("ide_prcop", Integer.parseInt(tab_control_produccion.getValorSeleccionado()));
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep");
                    }
                }  
                break;
            default:
                 utilitario.agregarMensajeError("Debe seleccionar el menú Control Produccion para imprimir este reporte", "");
                break;
        }
    
    
    
    }
      
   @Override
   
    public void insertar() {
         if (int_opcion==1) {
        if(tab_orden_produccion.isFocus()){
        tab_orden_produccion.insertar();
        TablaGenerica tab_secuencial=utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_sec_orden_pro")));
        TablaGenerica numero_secuencial = utilitario.consultar(ser_produccion.getNumeroSecuencial("numero_prorp", "prod_orden_produccion"));
        tab_orden_produccion.setValor("numero_prorp", numero_secuencial.getValor("numero"));
        tab_orden_produccion.setValor("numero_modulo_prorp", tab_secuencial.getValor("nuevo_secuencial"));
        }
        else if(tab_detalle_orden.isFocus()){
            tab_detalle_orden.insertar();
            
        }
                               
    }
         
      if(int_opcion==2) {
          if(tab_control_produccion.isFocus()){
              tab_control_produccion.insertar();
              TablaGenerica tab_secuencial=utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_mod_control_pro")));
              tab_control_produccion.setValor("numero_prcop", tab_secuencial.getValor("nuevo_secuencial"));
          }
      }   
      if(int_opcion==3)
          if(tab_proforma_orden.isFocus()){
              tab_proforma_orden.insertar();
              tab_proforma_orden.setValor("ide_prorp", aut_ord_produ.getValor());
              utilitario.addUpdate("tab_proforma_orden");
          }
    }

    @Override
    public void guardar() {
        if (int_opcion==1) {
       if (tab_orden_produccion.guardar()){
      // utilitario.getConexion().ejecutarSql(ser_produccion.getActualizarSecuencial(utilitario.getVariable("p_prod_num_sec_orden_pro")));
 
          tab_detalle_orden.guardar();
           guardarPantalla();
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

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public Map getParametro() {
        return parametro;
    }

    public void setParametro(Map parametro) {
        this.parametro = parametro;
    }

    public AutoCompletar getAut_ord_produ() {
        return aut_ord_produ;
    }

    public void setAut_ord_produ(AutoCompletar aut_ord_produ) {
        this.aut_ord_produ = aut_ord_produ;
    }

    public SeleccionTabla getSel_tab_proforma() {
        return sel_tab_proforma;
    }

    public void setSel_tab_proforma(SeleccionTabla sel_tab_proforma) {
        this.sel_tab_proforma = sel_tab_proforma;
    }
    
}   

 