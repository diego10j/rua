/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_produccion;

/**
 *
 * @author ANDRES
 */
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_gestion.ejb.ServicioEmpleado;
import paq_produccion.ejb.ServicioProduccion;
import sistema.aplicacion.Pantalla;

public class pre_recepcion_mercaderia extends Pantalla{
    private Tabla tab_tabla=new Tabla();
    private SeleccionTabla sel_tab_control = new SeleccionTabla();
    private SeleccionTabla sel_tab_detalle_control = new SeleccionTabla();
    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();
    private String ide_control = "";
    
    @EJB
    private final ServicioProduccion ser_produccion= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
    @EJB
    private final ServicioEmpleado ser_cargoempleado= (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class); 
    
      public pre_recepcion_mercaderia(){
          tab_tabla.setId("tab_tabla");
          tab_tabla.setTabla("prod_recepcion_mercaderia","ide_prorem",1);
          tab_tabla.setHeader("RECEPCIÓN DE MERCADERIA");
          tab_tabla.getColumna("IDE_GTEMP").setCombo(ser_cargoempleado.getSQLEmpleadosActivos());
          tab_tabla.setTipoFormulario(true);
          tab_tabla.getGrid().setColumns(4);
          tab_tabla.dibujar();
        
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setId("pat_panel");
        pat_panel.setPanelTabla(tab_tabla);
        
        
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);
        
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-search");
        bot_clean.setValue("Buscar Control de Produccion");
        bot_clean.setTitle("Buscar Control de Produccion");
        bot_clean.setMetodo("dibujarControl");
        bar_botones.agregarComponente(bot_clean);
        
        Boton bot_imprimir = new Boton();
        bot_imprimir.setIcon("ui-icon-print");
        bot_imprimir.setValue("Imprimir Reporte");
        bot_imprimir.setTitle("Imprimir Reporte");
        bot_imprimir.setMetodo("generarPDF");
        bar_botones.agregarComponente(bot_imprimir);
        
        vipdf_comprobante.setId("vipdf_comprobante");
        vipdf_comprobante.setTitle("RECEPCIÓN DE MERCADERÍA");
        agregarComponente(vipdf_comprobante);
        
        sel_tab_control.setId("sel_tab_control");
        sel_tab_control.setTitle("CONTROL DE PRODUCCION");
        sel_tab_control.setSeleccionTabla(ser_produccion.getControlProdRecepcion(), "ide_prcop");
        sel_tab_control.setWidth("80%");
        sel_tab_control.setHeight("70%");
        sel_tab_control.setRadio();
        sel_tab_control.getBot_aceptar().setMetodo("aceptarControl");
        agregarComponente(sel_tab_control);
        
        sel_tab_detalle_control.setId("sel_tab_detalle_control");
        sel_tab_detalle_control.setTitle("DETALLE CONTROL DE PRODUCCION");
        sel_tab_detalle_control.setSeleccionTabla(ser_produccion.getControlProdRecepcionDetalle("1", "", ""), "ide_prdecp");
        sel_tab_detalle_control.setWidth("80%");
        sel_tab_detalle_control.setHeight("70%");
        sel_tab_detalle_control.setRadio();
        sel_tab_detalle_control.getBot_aceptar().setMetodo("aceptarDetalle");
        agregarComponente(sel_tab_detalle_control);
          
      }
      public void dibujarControl(){
          if (tab_tabla.isFilaInsertada() == false){
              tab_tabla.insertar();
          }
          TablaGenerica tab_secuencial=utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_mod_recepcion_mercaderia")));
          TablaGenerica numero_secuencial = utilitario.consultar(ser_produccion.getNumeroSecuencial("num_secuencial_prorem", "prod_recepcion_mercaderia"));
          tab_tabla.setValor("num_secuencial_prorem", numero_secuencial.getValor("numero"));
          tab_tabla.setValor("num_modulo_prorem", tab_secuencial.getValor("nuevo_secuencial"));
          sel_tab_control.dibujar();
      }
      public void aceptarControl(){
          sel_tab_control.cerrar();
          ide_control = sel_tab_control.getValorSeleccionado();
          sel_tab_detalle_control.getTab_seleccion().setSql(ser_produccion.getControlProdRecepcionDetalle("2", ide_control, ""));
          sel_tab_detalle_control.getTab_seleccion().ejecutarSql();
          sel_tab_detalle_control.dibujar();
      }
      public void aceptarDetalle(){
          String detalle = sel_tab_detalle_control.getValorSeleccionado();
          TablaGenerica tab_detalle_control = utilitario.consultar("select a.ide_prcop, detalle_prmaq, numero_prorp, nombre_inarti, fecha_control_prdecp, operario, total\n" +
                                                                   "from prod_control_produccion a \n" +
                                                                   "left join prod_orden_produccion b on a.ide_prorp = b.ide_prorp\n" +
                                                                   "left join inv_articulo c on a.ide_inarti = c.ide_inarti\n" +
                                                                   "left join prod_maquina d on a.ide_prmaq = d.ide_prmaq\n" +
                                                                   "left join (select ide_prcop, ide_prdecp, fecha_control_prdecp, detalle_prtur as turno, apellido_paterno_gtemp||' '||primer_nombre_gtemp as operario, producto_bueno_prdecp as total\n" +
                                                                   "from prod_detalle_control_pro a\n" +
                                                                   "left join gth_empleado b on a.ide_gtemp = b.ide_gtemp\n" +
                                                                   "left join prod_turno c on a.ide_prtur = c.ide_prtur) e on a.ide_prcop = e.ide_prcop\n" +
                                                                   "where a.ide_prcop = "+ide_control+"\n" +
                                                                   "and ide_prdecp = "+detalle+"");
          
          tab_tabla.setValor("IDE_PRCOP", tab_detalle_control.getValor("ide_prcop"));
          tab_tabla.setValor("MAQUINA_PROREM", tab_detalle_control.getValor("detalle_prmaq"));
          tab_tabla.setValor("OPERARIO_PROREM", tab_detalle_control.getValor("operario"));
          tab_tabla.setValor("NUM_ORDEN_PROREM", tab_detalle_control.getValor("numero_prorp"));
          tab_tabla.setValor("PRODUCTO_PROREM", tab_detalle_control.getValor("nombre_inarti"));
          tab_tabla.setValor("TOTAL_PROREM", tab_detalle_control.getValor("total"));
          tab_tabla.modificar(tab_tabla.getFilaActual());
          tab_tabla.guardar();
          guardarPantalla();
          utilitario.addUpdateTabla(tab_tabla, "IDE_PRCOP","");
          utilitario.addUpdate("tab_tabla");
          sel_tab_detalle_control.cerrar();
          
      }
      public void generarPDF() {
        if (tab_tabla.getValorSeleccionado() != null) {
                        ///////////AQUI ABRE EL REPORTE
                        Map parametros = new HashMap();
                        parametros.put("pide_modulo", Integer.parseInt(utilitario.getVariable("p_prod_num_mod_recepcion_mercaderia")));
                        parametros.put("pide_control", Integer.parseInt(tab_tabla.getValor("ide_prcop")));
                        parametros.put("pide_recepcion", Integer.parseInt(tab_tabla.getValorSeleccionado()));

                        //System.out.println(" " + str_titulos);
                        vipdf_comprobante.setVisualizarPDF("rep_produccion/rep_recepcion_mercaderia.jasper", parametros);
                        vipdf_comprobante.dibujar();
                        utilitario.addUpdate("vipdf_comprobante");
        } else {
            utilitario.agregarMensajeInfo("Seleccione una recepcion de mercaderia", "");
        }
    }
      @Override
    public void insertar() {
        tab_tabla.insertar();
        TablaGenerica tab_secuencial=utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_mod_recepcion_mercaderia")));
        TablaGenerica numero_secuencial = utilitario.consultar(ser_produccion.getNumeroSecuencial("num_secuencial_prorem", "prod_recepcion_mercaderia"));
        tab_tabla.setValor("num_secuencial_prorem", numero_secuencial.getValor("numero"));
        tab_tabla.setValor("num_modulo_prorem", tab_secuencial.getValor("nuevo_secuencial"));
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla.eliminar();           
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public SeleccionTabla getSel_tab_control() {
        return sel_tab_control;
    }

    public void setSel_tab_control(SeleccionTabla sel_tab_control) {
        this.sel_tab_control = sel_tab_control;
    }

    public SeleccionTabla getSel_tab_detalle_control() {
        return sel_tab_detalle_control;
    }

    public void setSel_tab_detalle_control(SeleccionTabla sel_tab_detalle_control) {
        this.sel_tab_detalle_control = sel_tab_detalle_control;
    }

    public VisualizarPDF getVipdf_comprobante() {
        return vipdf_comprobante;
    }

    public void setVipdf_comprobante(VisualizarPDF vipdf_comprobante) {
        this.vipdf_comprobante = vipdf_comprobante;
    }
    
}
