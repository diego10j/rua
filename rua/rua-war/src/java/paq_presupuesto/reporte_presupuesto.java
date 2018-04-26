/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuesto;

/**
 *
 * @author Andres Redroban
 */

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sistema.aplicacion.Pantalla;

public class reporte_presupuesto extends Pantalla{
    private Reporte rep_reporte=new Reporte();
    private SeleccionFormatoReporte sel_rep=new SeleccionFormatoReporte();  
    private SeleccionCalendario sel_fechas= new SeleccionCalendario();
     private Panel panOpcion = new Panel();
    
    String fechai="";
    String fechaf="";
    
    public reporte_presupuesto(){
        bar_botones.getBot_insertar().setRendered(false);
        bar_botones.getBot_guardar().setRendered(false);
        bar_botones.getBot_eliminar().setRendered(false);
        bar_botones.quitarBotonsNavegacion();
        
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        bar_botones.agregarReporte();
        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        
        sel_fechas.setId("sec_rango_fechas");
        sel_fechas.getBot_aceptar().setMetodo("aceptarReporte");
        sel_fechas.setFechaActual();
        agregarComponente(sel_fechas);
        
        /*PanelTabla pat_imagen = new PanelTabla();
        pat_imagen.setId("pat_imagen");*/
        Imagen ImaReportes = new Imagen();
        ImaReportes.setValue("imagenes/repor_salesiana.png");
        //ImaReportes.setStyle("font-size:9px;color:black;text-align:center;");
        /*pat_imagen.setWidth("100%");
        pat_imagen.setHeader(modalidadIma);*/
        
        /*Grid gri_tipo = new Grid();
        gri_tipo.setId("gri_tipo");
        gri_tipo.getChildren().add(ImaReportes);
        //gri_tipo.setStyle("font-size:9px;color:black;text-align:center;");
        agregarComponente(gri_tipo);*/
        
       /* Panel tabimp = new Panel();
        tabimp.setStyle("font-size:19px;color:black;text-align:center;");
        tabimp.setHeader("REPORTES PRESUPUESTO");
        
        Boton botImprimir = new Boton();
        botImprimir.setValue("Imprimir");
        botImprimir.setExcluirLectura(true);
        botImprimir.setIcon("ui-icon-document-b");
        botImprimir.setMetodo("abrirListaReportes");
        bar_botones.agregarBoton(botImprimir);
        
        tabimp.getChildren().add(ImaReportes);
        tabimp.getChildren().add(botImprimir);

        agregarComponente(tabimp);*/
       
        panOpcion.setId("pan_opcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("REPORTES PRESUPUESTO");
        panOpcion.setStyle("font-size:19px;color:black;text-align:center;");
        
        Grid grid_pant = new Grid();
        grid_pant.setColumns(1);
        grid_pant.setStyle("text-align:center;position:absolute;top:210px;left:515px;");
        Etiqueta eti_encab = new Etiqueta();
        grid_pant.getChildren().add(ImaReportes);
        Boton bot_imprimir = new Boton();
        bot_imprimir.setValue("IMPRIMIR");
        bot_imprimir.setIcon("ui-icon-document-b");
        bot_imprimir.setMetodo("abrirListaReportes");
        bar_botones.agregarBoton(bot_imprimir);
        grid_pant.getChildren().add(bot_imprimir);
        agregarComponente(grid_pant);
        panOpcion.getChildren().add(grid_pant);
        agregarComponente(panOpcion);
        
    }
public void abrirListaReportes() {
 // TODO Auto-generated method stub
 rep_reporte.dibujar();
 }


public void aceptarReporte() {
if (rep_reporte.getReporteSelecionado().equals("Reporte Presupuesto Consolidado")){
                
            if(rep_reporte.isVisible()){
                rep_reporte.cerrar();
                sel_fechas.dibujar();
        }
        else if (sel_fechas.isVisible()){
                
                fechai=sel_fechas.getFecha1String();
                fechaf=sel_fechas.getFecha2String();
                sel_fechas.cerrar();
                Map map_parametros = new HashMap();
                map_parametros.put("fecha_ini",fechai );
                map_parametros.put("fecha_fin", fechaf);
                sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                sel_rep.dibujar();
        }
}
        else{
            utilitario.agregarMensajeInfo("No se puede continuar", "No ha selccionado ningun registro");
        }

    }
    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {
       
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

    public SeleccionCalendario getSel_fechas() {
        return sel_fechas;
    }

    public void setSel_fechas(SeleccionCalendario sel_fechas) {
        this.sel_fechas = sel_fechas;
    }

    public String getFechai() {
        return fechai;
    }

    public void setFechai(String fechai) {
        this.fechai = fechai;
    }

    public String getFechaf() {
        return fechaf;
    }

    public void setFechaf(String fechaf) {
        this.fechaf = fechaf;
    }
    
}
