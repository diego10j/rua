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
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import paq_produccion.ejb.ServicioProduccion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Nicolas Cajilema
 */
public class pre_cronograma extends Pantalla{
    
         private Tabla tab_cronograma=new Tabla();
         private SeleccionTabla sel_tab_control = new SeleccionTabla();
         private Reporte rep_reporte = new Reporte();
         private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
         private Map parametro = new HashMap();
         private SeleccionCalendario sel_fechas= new SeleccionCalendario();
         String fechai="";
         String fechaf="";
    @EJB
    private final ServiciosAdquisiones ser_adquisiciones= (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class); 
    
    @EJB
    private final ServicioProduccion ser_produccion= (ServicioProduccion) utilitario.instanciarEJB(ServicioProduccion.class); 
         
         public pre_cronograma(){
        tab_cronograma.setId("tab_cronograma");
        tab_cronograma.setTabla("prod_cronograma_produccion ","ide_prcrp",1);
     //   tab_cronograma.getColumna("ide_inarti").setCombo(ser_adquisiciones.getMaterial("", ""));
        //tab_cronograma.getColumna("ide_geper").setCombo(ser_adquisiciones.getDatosProveedor());
        tab_cronograma.getColumna("FECHA_PRCRP").setRequerida(true);
        tab_cronograma.getColumna("IDE_PRORP").setVisible(false);
        tab_cronograma.getColumna("IDE_PRCOP").setVisible(false);
        tab_cronograma.setTipoFormulario(true);
        tab_cronograma.getGrid().setColumns(4);
        tab_cronograma.setHeader("CRONOGRAMA");
        tab_cronograma.dibujar();
        
        PanelTabla pat_cronograma = new PanelTabla();
        pat_cronograma.setId("pat_cronograma");
        pat_cronograma.setPanelTabla(tab_cronograma);
        
        
        Division div_cronograma = new Division();
        div_cronograma.setId("div_color");
        div_cronograma.dividir1(pat_cronograma);
        
        agregarComponente(pat_cronograma);   
        
        Boton bot_clean = new Boton();
        bot_clean.setIcon("ui-icon-search");
        bot_clean.setValue("Buscar Control de Produccion");
        bot_clean.setTitle("Buscar Control de Produccion");
        bot_clean.setMetodo("dibujarControl");
        bar_botones.agregarComponente(bot_clean);
        
        sel_tab_control.setId("sel_tab_control");
        sel_tab_control.setTitle("CONTROL DE PRODUCCIÓN");
        sel_tab_control.setSeleccionTabla(ser_produccion.getControlProduccion("1", ""), "ide_prcop");
        sel_tab_control.setWidth("80%");
        sel_tab_control.setHeight("70%");
        sel_tab_control.setRadio();
        sel_tab_control.getBot_aceptar().setMetodo("aceptarControlProduccion");
        sel_tab_control.getTab_seleccion().getColumna("ide_prorp").setVisible(false);
        agregarComponente(sel_tab_control);
        
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        bar_botones.agregarReporte();
        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        agregarComponente(rep_reporte);
        
        sel_fechas.setId("sec_rango_fechas");
        sel_fechas.getBot_aceptar().setMetodo("aceptarReporte");
        sel_fechas.setFechaActual();
        agregarComponente(sel_fechas);
        
         }
    public void dibujarControl(){
        if(tab_cronograma.isFilaInsertada() == false){
            tab_cronograma.insertar();
        }
        sel_tab_control.dibujar();
    }
    
    public void aceptarControlProduccion(){
        String valor_control = sel_tab_control.getValorSeleccionado();
        TablaGenerica tab_control_prod = utilitario.consultar(ser_produccion.getControlProduccion("2", valor_control));
        TablaGenerica tab_secuencial=utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_mod_cronograma_produccion")));
        tab_cronograma.setValor("numero_modulo_prcrp", tab_secuencial.getValor("nuevo_secuencial"));
        tab_cronograma.setValor("IDE_PRCOP", tab_control_prod.getValor("IDE_PRCOP"));
        tab_cronograma.setValor("IDE_PRORP", tab_control_prod.getValor("IDE_PRORP"));
        tab_cronograma.setValor("NUMERO_ORDEN_PRCRP", tab_control_prod.getValor("numero_prorp"));
        tab_cronograma.setValor("PRODUCTO_PRCRP", tab_control_prod.getValor("nombre_inarti"));
        tab_cronograma.setValor("PRODUCTO_BUENO_PRCRP", tab_control_prod.getValor("producto_bueno_prcop"));
        tab_cronograma.setValor("PRODUCTO_MALO_PRCRP", tab_control_prod.getValor("producto_mala_calidad_prcop"));
        tab_cronograma.setValor("HORAS_PRCRP", tab_control_prod.getValor("total_horas_prcop"));
        tab_cronograma.setValor("FECHA_TERMINACION_PRCRP", tab_control_prod.getValor("fecha_termina_prcop"));
        tab_cronograma.setValor("CLIENTE_PRCRP", tab_control_prod.getValor("nom_geper"));
        tab_cronograma.modificar(tab_cronograma.getFilaActual());
        utilitario.addUpdateTabla(tab_cronograma, "IDE_PRCOP","");
        utilitario.addUpdateTabla(tab_cronograma, "IDE_PRORP","");
        utilitario.addUpdateTabla(tab_cronograma, "NUMERO_ORDEN_PRCRP","");
        utilitario.addUpdate("tab_cronograma");
        sel_tab_control.cerrar();
    }
    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra 
        rep_reporte.dibujar();

    }
    
    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Cronograma de Produccion")){
         if(rep_reporte.isVisible()){
                rep_reporte.cerrar();
                sel_fechas.dibujar();
        }
         else if (sel_fechas.isVisible()){
                fechai=sel_fechas.getFecha1String();
                fechaf=sel_fechas.getFecha2String();
                sel_fechas.cerrar();
                parametro = new HashMap();
                parametro.put("fecha_ini",fechai );
                parametro.put("fecha_fin", fechaf);
                parametro.put("pide_modulo",Integer.parseInt(utilitario.getVariable("p_prod_num_mod_cronograma_produccion")) );
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar(); 
        }
         else {
                    utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
                }
     }
     else{
            utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro");
        }
        }
    
    
   @Override
    public void insertar() {
        tab_cronograma.insertar();
        TablaGenerica tab_secuencial=utilitario.consultar(ser_produccion.getSecuencialModulo(utilitario.getVariable("p_prod_num_mod_cronograma_produccion")));
        tab_cronograma.setValor("numero_modulo_prcrp", tab_secuencial.getValor("nuevo_secuencial"));
    }

    @Override
    public void guardar() {
        tab_cronograma.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_cronograma.eliminar();
    }

    public Tabla getTab_cronograma() {
        return tab_cronograma;
    }

    public void setTab_cronograma(Tabla tab_cronograma) {
        this.tab_cronograma = tab_cronograma;
    }

    public SeleccionTabla getSel_tab_control() {
        return sel_tab_control;
    }

    public void setSel_tab_control(SeleccionTabla sel_tab_control) {
        this.sel_tab_control = sel_tab_control;
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

    public SeleccionCalendario getSel_fechas() {
        return sel_fechas;
    }

    public void setSel_fechas(SeleccionCalendario sel_fechas) {
        this.sel_fechas = sel_fechas;
    }
    
}
