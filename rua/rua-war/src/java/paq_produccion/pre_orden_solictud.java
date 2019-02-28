/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_gestion.ejb.ServicioEmpleado;
import paq_produccion.ejb.ServicioProduccion;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;


public class pre_orden_solictud extends Pantalla {

    private Tabla tab_orden_produccion = new Tabla();
    private Tabla tab_detalle_orden = new Tabla();
    private Tabla tab_detalle_orden_control = new Tabla();
    private Tabla tab_control_produccion = new Tabla();
    private Tabla tab_detalle_control_prod = new Tabla();
    private Tabla tab_proforma_orden = new Tabla();
    private Tabla tab_solicitud = new Tabla();
    private Tabla tab_detalle_solicitud = new Tabla();
    private final MenuPanel menup = new MenuPanel();
    private double dou_base_calculada = 0;
    private double dou_total_produccion = 0;
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    private Map parametro = new HashMap();
    private SeleccionTabla sel_tab_proforma = new SeleccionTabla();
    private SeleccionTabla sel_tab_orden_pro = new SeleccionTabla();
    private int producto_malo = 0;
    private int producto_bueno = 0;
    private int cantidad = 0;
    private int cantidad_inicial = 0;
    private int cantidad_calculada = 0;
    private AutoCompletar aut_ord_produ = new AutoCompletar();
    private int int_opcion = 0;
    private SeleccionCalendario sel_fechas = new SeleccionCalendario();
    String fechai = "";
    String fechaf = "";
    double valor_bultos;
    double valor_unidades;
    String valor_producir = "";
    double dou_valor_producir = 0;
    double total_inyectar = 0;
    double peso_x_pieza = 0;
    double num_cavidades = 0;
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);

    @EJB
    private final ServicioEmpleado ser_cargoempleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);

    @EJB
    private final ServicioProduccion ser_produccion = (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class);

    @EJB
    private final ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
           private VisualizarPDF vipdf_orden_produccion = new VisualizarPDF(); 
    
    public pre_orden_solictud() {
        //  bar_botones.limpiar();
        

        
        vipdf_orden_produccion.setId("vipdf_orden_produccion");
        vipdf_orden_produccion.setTitle("SOLICTUD DE MATERIAL");
        agregarComponente(vipdf_orden_produccion);
        
        Boton bot_imprimir_nota = new Boton();
        bot_imprimir_nota.setValue("IMPRIMIR SOLICITUD MATERIAL");
        bot_imprimir_nota.setIcon("ui-icon-print");
        bot_imprimir_nota.setMetodo("generarPDForden");
        bar_botones.agregarBoton(bot_imprimir_nota);
        
        tab_orden_produccion.setId("tab_orden_produccion");
        tab_orden_produccion.setTabla("prod_orden_produccion", "ide_prorp", 1);
        tab_orden_produccion.setCampoOrden("numero_prorp desc");
        tab_orden_produccion.getColumna("ide_gtemp").setCombo(ser_cargoempleado.getSQLEmpleadosActivos());
        tab_orden_produccion.getColumna("ide_gtemp").setAutoCompletar();
        tab_orden_produccion.getColumna("ide_gtemp").setVisible(false);
        tab_orden_produccion.getColumna("ide_gtcar").setVisible(false);
        tab_orden_produccion.getColumna("ide_prtio").setVisible(false);
        tab_orden_produccion.getColumna("fecha_emision_prorp").setVisible(false);
        tab_orden_produccion.getColumna("hora_registro_prorp").setVisible(false);
        tab_orden_produccion.getColumna("LOGO_PRORP").setVisible(false);
        tab_orden_produccion.getColumna("NUMERACION_PRORP").setVisible(false);
        tab_orden_produccion.getColumna("SOLITADO_POR_PRORP").setVisible(false);
        //tab_orden_produccion.getColumna("ide_geper").setEstilo("width: 20px;");
        tab_orden_produccion.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_orden_produccion.getColumna("ide_geper").setAutoCompletar(); // El autocompletar se ejecuta cuando un combo ya este realizado y solo lleva esta linea//
        tab_orden_produccion.getColumna("ide_gtcar").setCombo(ser_cargoempleado.getCargoEmpleado());
        tab_orden_produccion.getColumna("numero_prorp").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_orden_produccion.getColumna("numero_prorp").setEtiqueta();//etiqueta numero_modulo_prorp
        tab_orden_produccion.getColumna("ide_prtio").setCombo(ser_produccion.getTipoOrden());
        tab_orden_produccion.getColumna("fecha_emision_prorp").setValorDefecto(utilitario.getFechaActual());
        tab_orden_produccion.getColumna("hora_registro_prorp").setValorDefecto(utilitario.getHoraActual());
         tab_orden_produccion.setTipoFormulario(true);
        tab_orden_produccion.getGrid().setColumns(8);
        tab_orden_produccion.agregarRelacion(tab_solicitud);
        tab_orden_produccion.setHeader("ORDEN PRODUCCION");
        tab_orden_produccion.getColumna("TOTAL_PRODUCION_PRORP").setEtiqueta();//etiqueta
        tab_orden_produccion.getColumna("TOTAL_PRODUCION_PRORP").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//EstilO
        tab_orden_produccion.getColumna("fecha_terminado_prorp").setVisible(false);
        tab_orden_produccion.getColumna("hora_terminado_prorp").setVisible(false);
        tab_orden_produccion.getColumna("detalle_produccion_prorp").setVisible(false);
        tab_orden_produccion.getColumna("fecha_estimada_entrega_prorp").setVisible(false);
        tab_orden_produccion.getColumna("IDE_GEPER").setLectura(true);
        tab_orden_produccion.getColumna("OBSERVACION_PRORP").setLectura(true);
        tab_orden_produccion.getColumna("OBSERVACION_PRORP").setEtiqueta();
        tab_orden_produccion.getColumna("logo_prorp").setValorDefecto("N/A");
        tab_orden_produccion.getColumna("numeracion_prorp").setValorDefecto("N/A");
        //tab_orden_produccion.setRecuperarLectura(true);
        tab_orden_produccion.dibujar();

        PanelTabla pat_orden_produccion = new PanelTabla();
        pat_orden_produccion.setId("pat_orden_produccion");
        pat_orden_produccion.setPanelTabla(tab_orden_produccion);

        
        
            tab_solicitud.setId("tab_solicitud");
            tab_solicitud.setTabla("prod_solicitud", "ide_prsol", 2);
            tab_solicitud.getColumna("ide_gtemp").setCombo(ser_adquisiciones.getDatosEmpleado());
            tab_solicitud.setCampoOrden("numero_secuencial_prsol desc");
            tab_solicitud.getColumna("numero_secuencial_prsol").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
            tab_solicitud.getColumna("numero_secuencial_prsol").setEtiqueta();//etiqueta
            tab_solicitud.getColumna("fecha_prsol").setValorDefecto(utilitario.getFechaActual());
            tab_solicitud.getColumna("hora_prsol").setValorDefecto(utilitario.getHoraActual());
            tab_solicitud.getColumna("total_inyector_prsol").setValorDefecto("0.00");
            tab_solicitud.getColumna("total_inyector_prsol").setEstilo("font-size:15px;font-weight: bold;color:green");//etiqueta
            tab_solicitud.getColumna("total_inyector_prsol").setEtiqueta();//etiqueta
            tab_solicitud.getColumna("peso_pieza_prsol").setValorDefecto("0");
            tab_solicitud.getColumna("peso_pieza_prsol").setEstilo("font-size:15px;font-weight: bold;color:black");//etiqueta
            tab_solicitud.getColumna("peso_unidad_prsol").setValorDefecto("0");
            tab_solicitud.getColumna("peso_unidad_prsol").setEstilo("font-size:15px;font-weight: bold;color:black");//etiqueta
            tab_solicitud.getColumna("numero_cavidades_prsol").setValorDefecto("0");
            tab_solicitud.getColumna("numero_cavidades_prsol").setEstilo("font-size:15px;font-weight: bold;color:black");//etiqueta
            tab_solicitud.getColumna("hora_prsol").setLectura(true);
            tab_solicitud.getColumna("peso_pieza_prsol").setMetodoChange("calculaInyectar");
            
            tab_solicitud.getColumna("limpia_canion_prsol").setValorDefecto("0.00");
            tab_solicitud.getColumna("limpia_canion_prsol").setEstilo("font-size:15px;font-weight: bold;color:green");//etiqueta
            tab_solicitud.getColumna("limpia_canion_prsol").setEtiqueta();//etiqueta
            tab_solicitud.getColumna("ramal_prsol").setValorDefecto("0.00");
            tab_solicitud.getColumna("ramal_prsol").setEstilo("font-size:15px;font-weight: bold;color:green");//etiqueta
            tab_solicitud.getColumna("ramal_prsol").setEtiqueta();//etiqueta
            tab_solicitud.getColumna("total_inyecta_uni_prsol").setValorDefecto("0.00");
            tab_solicitud.getColumna("total_inyecta_uni_prsol").setEstilo("font-size:15px;font-weight: bold;color:green");//etiqueta
            tab_solicitud.getColumna("total_inyecta_uni_prsol").setEtiqueta();//etiqueta
            //tab_solicitud.getColumna("hora_prsol").setEstilo("font-size:8px;font-weight: bold;color:blue");//etiqueta
            tab_solicitud.setTipoFormulario(true);
            tab_solicitud.getGrid().setColumns(6);
            tab_solicitud.agregarRelacion(tab_detalle_solicitud);
            tab_solicitud.dibujar();
            PanelTabla pat_solicitud = new PanelTabla();
            pat_solicitud.setId("pat_solicitud");
            pat_solicitud.setPanelTabla(tab_solicitud);

         
            tab_detalle_solicitud.setId("tab_detalle_solicitud");
            tab_detalle_solicitud.setTabla("prod_detalle_solicitud", "ide_prdes", 3);
            tab_detalle_solicitud.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaProductos());
            tab_detalle_solicitud.getColumna("ide_inarti").setAutoCompletar();
            tab_detalle_solicitud.getColumna("ide_prcol").setCombo(ser_produccion.getColor());
            tab_detalle_solicitud.getColumna("ide_inuni").setCombo(ser_produccion.getUnidad());
            tab_detalle_solicitud.getColumna("inyectar_prdes").setValorDefecto("true");
            tab_detalle_solicitud.getColumna("cantidad_prdes").setMetodoChange("calculaRemanente");
            tab_detalle_solicitud.getColumna("ide_inuni").setAncho(10);
            tab_detalle_solicitud.dibujar();
            PanelTabla pat_detalle_solicitud = new PanelTabla();
            pat_detalle_solicitud.setId("pat_detalle_solicitud");
            pat_detalle_solicitud.setPanelTabla(tab_detalle_solicitud);
            
            Division div_solcitud = new Division();
            div_solcitud.setId("div_solcitud");
            div_solcitud.dividir3(pat_orden_produccion, pat_solicitud, pat_detalle_solicitud, "20%", "40%", "H");
            agregarComponente(div_solcitud);
      }
     
      public void dibujaProforma(){
          int_opcion=3;
          tab_proforma_orden = new Tabla();
          tab_proforma_orden.setId("tab_proforma_orden");
          tab_proforma_orden.setTabla("prod_proforma_orden","ide_prprof",1);
          //tab_proforma_orden.setCondicion("ide_prprof=" + aut_ord_produ.getValor());
          tab_proforma_orden.getColumna("ide_prorp").setCombo(ser_produccion.getOrdenPro());
          tab_proforma_orden.getColumna("ide_prorp").setAutoCompletar();
          tab_proforma_orden.getColumna("ide_prorp").setVisible(false);
          tab_proforma_orden.getColumna("ide_prpro").setCombo(ser_produccion.getComboProforma());
          tab_proforma_orden.getColumna("ide_prpro").setAutoCompletar();
          tab_proforma_orden.setHeader("PROFORMA ORDEN");
          tab_proforma_orden.dibujar();
          
          Grid gri_proformao = new Grid();
          gri_proformao.setColumns(3);
          Boton bot_busca_proforma = new Boton();
          bot_busca_proforma.setValue("Buscar Proforma");
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
          if (aut_ord_produ.getValor() != null){
              if (tab_proforma_orden.isFilaInsertada() == false){
                  tab_proforma_orden.insertar();
              }
              tab_proforma_orden.setValor("ide_prorp", aut_ord_produ.getValor());
              sel_tab_proforma.dibujar();
          } else {
              utilitario.agregarMensajeError("Debe seleccion una Orden de Produccion", "");
          }
          
      }
      public void aceptarProforma(){
           String ide_proforma = sel_tab_proforma.getValorSeleccionado();
          TablaGenerica tab_proforma_gen = utilitario.consultar("select ide_prpro, numero_prpro, fecha_prpro, b.nom_geper, total_prpro, observacion_prpro\n" +
                                                                "from prod_proforma a\n" +
                                                                "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
                                                                "where ide_prpro = "+ide_proforma+"");
          

    }

   

    public void dibujaControl() {
        int_opcion = 2;
        if (aut_ord_produ.getValor() != null) {

            tab_detalle_orden_control= new Tabla();
            tab_detalle_orden_control.setId("tab_detalle_orden_control");
            tab_detalle_orden_control.setTabla("prod_orden_detalle", "ide_prord", 2);
            tab_detalle_orden_control.setCondicion("ide_prorp=" + aut_ord_produ.getValor());
            tab_detalle_orden_control.getColumna("ide_inuni").setCombo(ser_produccion.getUnidad());
            //tab_detalle_orden_control.getColumna("ide_inarti").setEstilo("width: 20px;");
            tab_detalle_orden_control.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaProductos());
            tab_detalle_orden_control.getColumna("ide_inarti").setAutoCompletar();
            tab_detalle_orden_control.getColumna("ide_prcol").setEstilo("width: 20px;");
            tab_detalle_orden_control.getColumna("ide_prcol").setCombo(ser_produccion.getColor());

            tab_detalle_orden_control.agregarRelacion(tab_control_produccion);
            tab_detalle_orden_control.setLectura(true);
            tab_detalle_orden_control.getColumna("ide_prorp").setVisible(false);
            tab_detalle_orden_control.dibujar();

            PanelTabla pat_detalle_orden_control = new PanelTabla();
            pat_detalle_orden_control.setId("pat_detalle_orden_control");
            pat_detalle_orden_control.setPanelTabla(tab_detalle_orden_control);
            tab_control_produccion= new Tabla();
            tab_control_produccion.setId("tab_control_produccion");
            tab_control_produccion.setTabla("prod_control_produccion", "ide_prcop", 3);
            // tab_control_produccion.setCondicion("ide_prcop=" + aut_ord_produ.getValor());
            tab_control_produccion.getColumna("ide_gtemp").setCombo(ser_adquisiciones.getDatosEmpleado());
            tab_control_produccion.getColumna("gth_ide_gtemp").setCombo(ser_adquisiciones.getDatosEmpleado());
            tab_control_produccion.getColumna("gth_ide_gtemp2").setCombo(ser_adquisiciones.getDatosEmpleado());
            tab_control_produccion.getColumna("ide_prtur").setCombo(ser_produccion.getTurno());
            tab_control_produccion.getColumna("ide_prmaq").setCombo(ser_produccion.getMaquina());
            tab_control_produccion.setTipoFormulario(true);
            tab_control_produccion.getGrid().setColumns(4);
            //tab_control_produccion.setHeader("CONTROL PRODUCCION");
            // tab_control_produccion.agregarRelacion(tab_detalle_control_prod);
            tab_control_produccion.getColumna("PRODUCTO_BUENO_PRCOP").setMetodoChange("calculaTotalProduccion");
            tab_control_produccion.getColumna("PRODUCTO_MALA_CALIDAD_PRCOP").setMetodoChange("calculaTotalProduccion");

            //         tab_control_produccion.getColumna("CALCULO_CANTIDAD_PRCOP").setEtiqueta();
            tab_control_produccion.getColumna("ide_prcop").setOrden(0);
//          tab_control_produccion.getColumna("IDE_PRORP").setOrden(1);   
            tab_control_produccion.getColumna("IDE_PRMAQ").setOrden(1);
            tab_control_produccion.getColumna("FECHA_PRCOP").setOrden(2);
            //   tab_control_produccion.getColumna("CANTIDAD_PRCOP").setOrden(4);
            //   tab_control_produccion.getColumna("CALCULO_CANTIDAD_PRCOP").setOrden(5);
            tab_control_produccion.getColumna("PRODUCTO_BUENO_PRCOP").setOrden(3);
            tab_control_produccion.getColumna("PRODUCTO_MALA_CALIDAD_PRCOP").setOrden(4);
            tab_control_produccion.dibujar();

            PanelTabla pat_control_produccion = new PanelTabla();
            pat_control_produccion.setId("pat_control_produccion");
            pat_control_produccion.setPanelTabla(tab_control_produccion);

            Division div_control_produccion = new Division();
            div_control_produccion.setId("div_control_produccion");
            div_control_produccion.dividir2(pat_detalle_orden_control, pat_control_produccion, "35%", "H");
            agregarComponente(div_control_produccion);

            /* Grupo gru = new Grupo();
         gru.getChildren().add(bar_menu);
         gru.getChildren().add(div_control_produccion);*/
            menup.dibujar(2, "CONTROL PRODUCCION", div_control_produccion);
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Orden de Produccion", "");
        }
    }

    public void asignarCantidad(AjaxBehaviorEvent evt) {
        tab_control_produccion.modificar(evt);
        cantidad = Integer.parseInt(tab_control_produccion.getValor("CANTIDAD_PRCOP"));
        tab_control_produccion.setValor("CALCULO_CANTIDAD_PRCOP", cantidad + "");
        tab_control_produccion.modificar(tab_control_produccion.getFilaActual());
        utilitario.addUpdateTabla(tab_control_produccion, "CALCULO_CANTIDAD_PRCOP", "");
        utilitario.addUpdate("tab_control_produccion");

    }

    public void calculaTotalProduccion(AjaxBehaviorEvent evt) {
        tab_control_produccion.modificar(evt);
        int total = 0;
        double numero_horas = 0;
        double total_horas = 0;
        TablaGenerica tab_horas = utilitario.consultar(utilitario.getDiferenciaHorasCalculo(tab_control_produccion.getValor("hora_inicio_operador_prcop"), tab_control_produccion.getValor("hora_fin_operador_prcop")));
        producto_bueno = Integer.parseInt(tab_control_produccion.getValor("PRODUCTO_BUENO_PRCOP"));
        //producto_malo = Integer.parseInt(tab_control_produccion.getValor("producto_mala_prdecp"));
        cantidad_inicial = Integer.parseInt(tab_detalle_orden_control.getValor("total_entregado_prord"));
        try {
            //total = producto_bueno + producto_malo;
            cantidad_calculada = cantidad_inicial - producto_bueno;
        } catch (Exception e) {

        }
        if (cantidad_calculada > 0) {
            tab_detalle_orden_control.setValor("total_entregado_prord", cantidad_calculada + "");
            tab_detalle_orden_control.modificar(tab_detalle_orden_control.getFilaActual());
            numero_horas = Double.parseDouble(tab_horas.getValor("resultado"));
            total_horas = (numero_horas * 1) / 60;
            tab_control_produccion.setValor("hora_trabajo_prcop", utilitario.getFormatoNumero(total_horas, 2));
            utilitario.addUpdate("tab_detalle_orden_control");
            tab_control_produccion.modificar(tab_control_produccion.getFilaActual());
            utilitario.addUpdateTabla(tab_control_produccion, "hora_trabajo_prcop", "");
            utilitario.addUpdate("tab_control_produccion");
            tab_control_produccion.guardar();
            tab_detalle_orden_control.guardar();
            guardarPantalla();
        } else {
            utilitario.agregarMensajeInfo("El valor ingresado excede de la cantidad faltante", "Por favor revisar valores");
        }
    }

    public void cargarOrden() {
        if (tab_control_produccion.isFilaInsertada() == false) {
            tab_control_produccion.insertar();
        }
        sel_tab_orden_pro.dibujar();
    }

    public void dibujaOrden() {
        Grupo gru_grupo = new Grupo();
        int_opcion = 1;
        tab_orden_produccion= new Tabla();
        tab_detalle_orden = new Tabla();
        tab_orden_produccion.setId("tab_orden_produccion");
        tab_orden_produccion.setTabla("prod_orden_produccion", "ide_prorp", 44);
        tab_orden_produccion.setCondicion("ide_prorp="+aut_ord_produ.getValor());
        tab_orden_produccion.getColumna("ide_gtemp").setCombo(ser_cargoempleado.getSQLEmpleadosActivos());
        tab_orden_produccion.getColumna("ide_gtemp").setAutoCompletar();
        //tab_orden_produccion.getColumna("ide_geper").setEstilo("width: 20px;");
        tab_orden_produccion.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_orden_produccion.getColumna("ide_geper").setAutoCompletar(); // El autocompletar se ejecuta cuando un combo ya este realizado y solo lleva esta linea//
        tab_orden_produccion.getColumna("ide_gtcar").setCombo(ser_cargoempleado.getCargoEmpleado());
        tab_orden_produccion.getColumna("numero_prorp").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        tab_orden_produccion.getColumna("numero_prorp").setEtiqueta();//etiqueta numero_modulo_prorp
        tab_orden_produccion.getColumna("ide_prtio").setCombo(ser_produccion.getTipoOrden());
        tab_orden_produccion.getColumna("fecha_emision_prorp").setValorDefecto(utilitario.getFechaActual());
        tab_orden_produccion.getColumna("hora_registro_prorp").setValorDefecto(utilitario.getHoraActual());
        //tab_orden_produccion.getColumna("numero_modulo_prorp").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//etiqueta
        //tab_orden_produccion.getColumna("numero_modulo_prorp").setEtiqueta();//etiqueta numero_modulo_prorp
        tab_orden_produccion.setTipoFormulario(true);
        tab_orden_produccion.getGrid().setColumns(4);
        tab_orden_produccion.agregarRelacion(tab_detalle_orden);
        tab_orden_produccion.setHeader("ORDEN PRODUCCION");
        tab_orden_produccion.getColumna("TOTAL_PRODUCION_PRORP").setEtiqueta();//etiqueta
        tab_orden_produccion.getColumna("TOTAL_PRODUCION_PRORP").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//EstilO
        tab_orden_produccion.getColumna("fecha_terminado_prorp").setVisible(false);
        tab_orden_produccion.getColumna("hora_terminado_prorp").setVisible(false);
        tab_orden_produccion.getColumna("detalle_produccion_prorp").setVisible(false);
        tab_orden_produccion.getColumna("fecha_estimada_entrega_prorp").setVisible(false);
        tab_orden_produccion.setRecuperarLectura(true);
        tab_orden_produccion.dibujar();

        PanelTabla pat_orden_produccion = new PanelTabla();
        pat_orden_produccion.setId("pat_orden_produccion");
        pat_orden_produccion.setPanelTabla(tab_orden_produccion);
        gru_grupo.getChildren().add(pat_orden_produccion);
        gru_grupo.getChildren().add(new Separator());

        //--- tab_detalle_orden//
        
        tab_detalle_orden.setId("tab_detalle_orden");
        tab_detalle_orden.setTabla("prod_orden_detalle", "ide_prord", 55);
        tab_detalle_orden.setCondicion("ide_prorp="+aut_ord_produ.getValor());
        tab_detalle_orden.getColumna("ide_inuni").setCombo(ser_produccion.getUnidad());
        tab_detalle_orden.getColumna("ide_inarti").setCombo(ser_producto.getSqlListaProductos());
        tab_detalle_orden.getColumna("ide_inarti").setAutoCompletar();
        tab_detalle_orden.getColumna("ide_prcol").setCombo(ser_produccion.getColor());
        tab_detalle_orden.getColumna("BULTO_PAQUETE_PRORD").setValorDefecto("0");
        tab_detalle_orden.getColumna("UNIDADES_PRORD").setValorDefecto("0");
        tab_detalle_orden.getColumna("TOTAL_PRORD").setValorDefecto("0");
        tab_detalle_orden.getColumna("BULTO_PAQUETE_PRORD").setMetodoChange("calculaTotalBultos");
        tab_detalle_orden.getColumna("UNIDADES_PRORD").setMetodoChange("calculaTotalBultos");
        tab_detalle_orden.getColumna("TOTAL_PRORD").setEtiqueta();
        tab_detalle_orden.getColumna("BULTO_PAQUETE_PRORD").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_detalle_orden.getColumna("UNIDADES_PRORD").setEstilo("font-size:15px;font-weight: bold;color:black");//Estilo
        tab_detalle_orden.getColumna("TOTAL_PRORD").setEstilo("font-size:15px;font-weight: bold;color:blue");//Estilo
        tab_detalle_orden.getColumna("total_entregado_prord").setEstilo("font-size:15px;font-weight: bold;color:green");//Estilo
        tab_detalle_orden.getColumna("ide_prmaq").setCombo(ser_produccion.getMaquina());
        tab_detalle_orden.setRecuperarLectura(true);
        // tab_detalle_orden.setTipoFormulario(true);
        //tab_detalle_orden.getGrid().setColumns(4);
        tab_detalle_orden.setHeader("ORDEN  DETALLE");
        tab_detalle_orden.dibujar();

        PanelTabla pat_detalle_orden = new PanelTabla();
        pat_detalle_orden.setId("pat_detalle_orden");
        pat_detalle_orden.setPanelTabla(tab_detalle_orden);
        gru_grupo.getChildren().add(pat_detalle_orden);

        menup.dibujar(1,"fa fa-database", "ORDEN", gru_grupo,true);

        
    }

    public void generarPDForden() {

        String usuario = utilitario.getVariable("NICK");
        if (tab_orden_produccion.getValorSeleccionado() != null) {
            Map parametros = new HashMap();
            parametros.put("pide_solicitud", Integer.parseInt(tab_solicitud.getValorSeleccionado()));
            parametros.put("pide_version", utilitario.getVariable("p_prod_version_documento"));
            parametros.put("pide_fecha", utilitario.getVariable("p_prod_fecha_documento"));
            parametros.put("p_usuario", utilitario.getVariable("NICK"));
            parametros.put("nombre", usuario);
            vipdf_orden_produccion.setVisualizarPDF("rep_produccion/rep_solicitud_material.jasper", parametros);
            vipdf_orden_produccion.dibujar();
            utilitario.addUpdate("vipdf_orden_produccion");
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Orden de Produccion", "");
        }
    }

    
    
    
    public void calculaTotalBultos(AjaxBehaviorEvent evt) {
        tab_detalle_orden.modificar(evt);

        valor_bultos = Float.parseFloat(tab_detalle_orden.getValor("BULTO_PAQUETE_PRORD"));
        valor_unidades = Float.parseFloat(tab_detalle_orden.getValor("UNIDADES_PRORD"));
        //valor_bultos= tab_detalle_orden.getValor("BULTO_PAQUETE_PRORD");
        //valor_unidades = tab_detalle_orden.getValor("UNIDADES_PRORD");

        dou_base_calculada = valor_unidades * valor_bultos;
        tab_detalle_orden.setValor("TOTAL_PRORD", utilitario.getFormatoNumero(dou_base_calculada, 2));
        tab_detalle_orden.setValor("total_entregado_prord", utilitario.getFormatoNumero(dou_base_calculada, 2));
        tab_orden_produccion.setValor("total_producion_prorp", tab_detalle_orden.getSumaColumna("total_prord") + "");//cuado quiera sumar las columnas solo getsumacolum
        tab_orden_produccion.modificar(tab_orden_produccion.getFilaActual());
        utilitario.addUpdateTabla(tab_orden_produccion, "total_producion_prorp", "");
        utilitario.addUpdateTabla(tab_detalle_orden, "TOTAL_PRORD,total_entregado_prord", "");
        aut_ord_produ.actualizar();
    }

    public void dibujaSolicitud() {
        Grupo gru_grupo = new Grupo();
        if (aut_ord_produ != null) {
            TablaGenerica tab_datos_produccion = utilitario.consultar("SELECT ide_prorp, total_producion_prorp FROM prod_orden_produccion WHERE ide_prorp = " + aut_ord_produ.getValor() + "");
            valor_producir = tab_datos_produccion.getValor("total_producion_prorp");
            int_opcion = 4;
            tab_solicitud = new Tabla();
            
            menup.dibujar(4,"fa fa-list-ul", "SOLICITUD DE MATERIAL", gru_grupo, true);

        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Orden de Produccion", "");
        }

    }

    public void calculaRemanente(AjaxBehaviorEvent evt) {
        tab_solicitud.modificar(evt);
        try {
            double total_inyectar_prima=0;
            double cantidad=0;
            double limpia_canion=0;
            double total_m_inyecta=0;
            double total_producir=0;
            double total_ramal=0;
            for(int i=0;i<tab_detalle_solicitud.getTotalFilas();i++){
                if(tab_detalle_solicitud.getValor(i, "inyectar_prdes").equals("true")){
                    cantidad=Double.parseDouble(tab_detalle_solicitud.getValor(i, "cantidad_prdes"));
                    total_inyectar_prima=total_inyectar_prima+cantidad;
                }
            }
            
            total_m_inyecta=Double.parseDouble(tab_solicitud.getValor("total_inyector_prsol"));
            limpia_canion=total_inyectar_prima-total_m_inyecta;
            
            total_producir=Double.parseDouble(tab_orden_produccion.getValor("total_producion_prorp"));
            peso_x_pieza = Double.parseDouble(tab_solicitud.getValor("peso_pieza_prsol"));
            num_cavidades = Double.parseDouble(tab_solicitud.getValor("numero_cavidades_prsol"));
            
            double ramal_previo=0;
            double peso_x_unidad=0;
            
            peso_x_unidad =Double.parseDouble(tab_solicitud.getValor("peso_unidad_prsol"));
            
            ramal_previo=(total_producir*peso_x_unidad)*num_cavidades;
            total_ramal=(peso_x_pieza-(peso_x_unidad*num_cavidades))*total_producir;
            
            tab_solicitud.setValor("total_inyecta_uni_prsol", utilitario.getFormatoNumero(ramal_previo, 4));//cuado quiera sumar las columnas solo getsumacolum
            tab_solicitud.setValor("ramal_prsol", utilitario.getFormatoNumero(total_ramal, 4));//cuado quiera sumar las columnas solo getsumacolum
            tab_solicitud.setValor("limpia_canion_prsol", utilitario.getFormatoNumero(limpia_canion, 4));//cuado quiera sumar las columnas solo getsumacolum
            tab_solicitud.modificar(tab_solicitud.getFilaActual());
            utilitario.addUpdate("tab_solicitud");
        } catch (Exception e) {
        }
    }
public void calculaInyectar(AjaxBehaviorEvent evt) {
        tab_solicitud.modificar(evt);
        double total_mp_inyectar=0;
                double producir=0;
                double peso_x_pieza=0;
                try{
                  
                    
                    producir=Double.parseDouble(tab_orden_produccion.getValor("total_producion_prorp"));
                    peso_x_pieza=Double.parseDouble(tab_solicitud.getValor("peso_pieza_prsol"));
                    total_mp_inyectar=producir*peso_x_pieza;
                }
                catch(Exception e){
                    System.out.println("Eroor calculo "+e);
                }
                tab_solicitud.setValor("total_inyector_prsol",utilitario.getFormatoNumero(total_mp_inyectar, 4));
                utilitario.addUpdateTabla(tab_solicitud, "total_inyector_prsol", "");
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
                        parametro.put("pide_version", utilitario.getVariable("p_prod_version_documento"));
                        parametro.put("pide_fecha", utilitario.getVariable("p_prod_fecha_documento"));
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep");
                    }
                }
                break;
            case 2:
                if (rep_reporte.getReporteSelecionado().equals("Cronograma de Produccion")) {
                    if (rep_reporte.isVisible()) {
                        rep_reporte.cerrar();
                        sel_fechas.dibujar();
                    } else if (sel_fechas.isVisible()) {
                        fechai = sel_fechas.getFecha1String();
                        fechaf = sel_fechas.getFecha2String();
                        sel_fechas.cerrar();
                        parametro = new HashMap();
                        parametro.put("fecha_ini", fechai);
                        parametro.put("fecha_fin", fechaf);
                        parametro.put("pide_version", utilitario.getVariable("p_prod_version_documento"));
                        parametro.put("pide_fecha", utilitario.getVariable("p_prod_fecha_documento"));
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                    }
                }
                break;
            case 3:
                if (rep_reporte.getReporteSelecionado().equals("Control de Produccion")) {
                    if (rep_reporte.isVisible()) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("ide_prcop", Integer.parseInt(tab_control_produccion.getValorSeleccionado()));
                        parametro.put("pide_version", utilitario.getVariable("p_prod_version_documento"));
                        parametro.put("pide_fecha", utilitario.getVariable("p_prod_fecha_documento"));
                        sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                        sel_rep.dibujar();
                        utilitario.addUpdate("sel_rep");
                    }
                }
                break;
            case 4:
                if (rep_reporte.getReporteSelecionado().equals("Solicitud de Material")) {
                    if (rep_reporte.isVisible()) {
                        parametro = new HashMap();
                        rep_reporte.cerrar();
                        parametro.put("pide_solicitud", Integer.parseInt(tab_solicitud.getValorSeleccionado()));
                        parametro.put("pide_version", utilitario.getVariable("p_prod_version_documento"));
                        parametro.put("pide_fecha", utilitario.getVariable("p_prod_fecha_documento"));
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

            if (tab_solicitud.isFocus()) {
                tab_solicitud.insertar();                
                TablaGenerica tab_secuen = utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_mod_solicitud_material")));
                String tipo_aplica = tab_secuen.getValor("aplica_abreviatura_gemos");
                tab_solicitud.setValor("numero_secuencial_prsol", ser_produccion.getSecuencialNumero(tipo_aplica, Integer.parseInt(tab_secuen.getValor("longitud_secuencial_gemos")), Integer.parseInt(tab_secuen.getValor("tamano"))) + tab_secuen.getValor("nuevo_secuencial"));
            } else if (tab_detalle_solicitud.isFocus()) {
                tab_detalle_solicitud.insertar();
            }
        
    }

    @Override
    public void guardar() {
       
            if (tab_solicitud.guardar()){
                                 //    System.out.println("entre al guardar");
                if(tab_solicitud.isFilaInsertada()){
                    // System.out.println("entre al secuencia");
                utilitario.getConexion().ejecutarSql(ser_produccion.getActualizarSecuencial(utilitario.getVariable("p_prod_num_mod_solicitud_material")));
                }
                if(tab_detalle_solicitud.guardar()){
                guardarPantalla();
               
                } 
                 
            }
        
        
    }

    @Override
    public void eliminar() {
       
            if (tab_solicitud.isFocus()) {
                tab_solicitud.eliminar();
            } else if (tab_detalle_solicitud.isFocus()) {
                tab_detalle_solicitud.eliminar();
            }
        
    }

    public void limpiar() {
        aut_ord_produ.limpiar();
        menup.limpiar();
    }

    public void seleccionoAutocompletar(SelectEvent evt) {
        aut_ord_produ.onSelect(evt);
        
        if (aut_ord_produ != null) {
            switch (menup.getOpcion()) {
                case 1:
                    dibujaOrden();
                    break;
                case 2:
                    dibujaControl();
                    break;
                case 3:
                    dibujaProforma();
                    break;
                case 4:
                    
                default:
                    dibujaSolicitud();
            }
        } else {
            limpiar();
        }
        /*
        if (aut_ord_produ.getValor() != null) {
            
            if (menup.getOpcion() == 1) {
                //dibujaOrden();
                tab_orden_produccion.setCondicion("ide_prorp="+aut_ord_produ.getValor());
                tab_orden_produccion.ejecutarSql();
                tab_detalle_orden.setCondicion("ide_prorp="+aut_ord_produ.getValor());
                tab_detalle_orden.ejecutarSql();
            }
            else if (menup.getOpcion() == 2) {
                dibujaControl();
            }
            else if (menup.getOpcion() == 3) {
                dibujaProforma();
            }
            else if (menup.getOpcion() == 4) {
                dibujaSolicitud();
            }
            
            
        } else {
            utilitario.agregarMensajeInfo("Seleccionar una Orden de Producción", "Debe seleccionar una Orden de Producción");
        }
        */
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

    public VisualizarPDF getVipdf_orden_produccion() {
        return vipdf_orden_produccion;
    }

    public void setVipdf_orden_produccion(VisualizarPDF vipdf_orden_produccion) {
        this.vipdf_orden_produccion = vipdf_orden_produccion;
    }

    public void setTab_detalle_orden(Tabla tab_detalle_orden) {
        this.tab_detalle_orden = tab_detalle_orden;
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

    public Tabla getTab_detalle_control_prod() {
        return tab_detalle_control_prod;
    }

    public void setTab_detalle_control_prod(Tabla tab_detalle_control_prod) {
        this.tab_detalle_control_prod = tab_detalle_control_prod;
    }

    public SeleccionTabla getSel_tab_orden_pro() {
        return sel_tab_orden_pro;
    }

    public void setSel_tab_orden_pro(SeleccionTabla sel_tab_orden_pro) {
        this.sel_tab_orden_pro = sel_tab_orden_pro;
    }

    public Tabla getTab_detalle_orden_control() {
        return tab_detalle_orden_control;
    }

    public void setTab_detalle_orden_control(Tabla tab_detalle_orden_control) {
        this.tab_detalle_orden_control = tab_detalle_orden_control;
    }

    public SeleccionCalendario getSel_fechas() {
        return sel_fechas;
    }

    public void setSel_fechas(SeleccionCalendario sel_fechas) {
        this.sel_fechas = sel_fechas;
    }

    public Tabla getTab_solicitud() {
        return tab_solicitud;
    }

    public void setTab_solicitud(Tabla tab_solicitud) {
        this.tab_solicitud = tab_solicitud;
    }

    public Tabla getTab_detalle_solicitud() {
        return tab_detalle_solicitud;
    }

    public void setTab_detalle_solicitud(Tabla tab_detalle_solicitud) {
        this.tab_detalle_solicitud = tab_detalle_solicitud;
    }
    
}   


