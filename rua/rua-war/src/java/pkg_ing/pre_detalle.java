/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_ing;

import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_detalle extends Pantalla {
    
    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();
    private Combo com_anio = new Combo();
    private Combo com_mes = new Combo();
    private Etiqueta eti_total = new Etiqueta();
    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();
    
    public pre_detalle() {
        
        com_anio.setCombo("select ide_geani,nom_geani from gen_anio order by ide_geani");
        com_anio.eliminarVacio();
        com_anio.setMetodo("filtrarCombo");
        bar_botones.agregarComponente(new Etiqueta("Periodo"));
        bar_botones.agregarComponente(com_anio);
        bar_botones.agregarReporte();
        com_mes.setCombo("select ide_gemes,nombre_gemes from gen_mes ORDER BY alterno_gemes");
        com_mes.setMetodo("filtrarCombo");
        bar_botones.agregarComponente(com_mes);
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("ing_cabecera", "ide_incab", 1);
        tab_tabla1.getColumna("ide_geani").setVisible(false);
        tab_tabla1.getColumna("ide_gemes").setVisible(false);
        tab_tabla1.getColumna("ide_intip").setCombo("select ide_intip,nombre_intip from ing_tipo");
        tab_tabla1.getColumna("fecha_incab").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.onSelect("seleccionarTabla1");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.setCondicion("ide_gemes=-1");
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("ing_detalle", "ide_indet", 2);
        tab_tabla2.getColumna("ide_inper").setCombo("select ide_inper,nombres_inper from ing_persona where  activo_inper=true");
        tab_tabla2.getColumna("ide_inper").setAutoCompletar();
        tab_tabla2.getColumna("numero_indet").setMascara("9999999");
        tab_tabla2.getColumna("valor_indet").setMetodoChange("cambioValor");
        tab_tabla2.setRows(20);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);
        
        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);
        eti_total.setId("eti_total");
        eti_total.setStyle("font-size: 14px;font-weight: bold");
        eti_total.setValue("TOTAL : 0.00");
        Division div_total = new Division();
        div_total.setFooter(pat_panel2, eti_total, "90%");
        
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, div_total, "50%", "H");
        agregarComponente(div_division);
    }
    
    public void seleccionarTabla1(SelectEvent evt){
        tab_tabla1.seleccionarFila(evt);
        calcularTotal();
    }
    
    public void cambioValor(AjaxBehaviorEvent evt){
        tab_tabla2.modificar(evt);
        calcularTotal();
    }
    
    private void calcularTotal() {
        eti_total.setValue("TOTAL : " + utilitario.getFormatoNumero(tab_tabla2.getSumaColumna("valor_indet")));
        utilitario.addUpdate("eti_total");
    }
    
    public void filtrarCombo() {
        if (com_anio.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
            return;
        }
        
        if (com_mes.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Mes", "");
            return;
        }
        tab_tabla1.setCondicion("ide_geani=" + com_anio.getValue() + " AND ide_gemes=" + com_mes.getValue());
        tab_tabla1.ejecutarSql();
        calcularTotal();
        //tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
    }
    
    @Override
    public void insertar() {
        if (com_anio.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
            return;
        }
        
        if (com_mes.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Mes", "");
            return;
        }
        tab_tabla1.getColumna("ide_geani").setVisible(false);
        tab_tabla1.getColumna("ide_gemes").setVisible(false);
        
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_geani", com_anio.getValue().toString());
            tab_tabla1.setValor("ide_gemes", com_mes.getValue().toString());
            calcularTotal();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
        }
        
    }
    
    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        guardarPantalla();
    }
    
    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
        calcularTotal();
    }
    Map parametro = new HashMap();
    
    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista        
        if (rep_reporte.getReporteSelecionado().equals("Resumen Ingresos y Gastos")) {
            if (rep_reporte.isVisible()) {                
                parametro = new HashMap();
                rep_reporte.cerrar();
                parametro.put("ide_geani", Long.parseLong(com_anio.getValue().toString()));
                sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sel_rep.dibujar();
                utilitario.addUpdate("rep_reporte,sel_rep");                
            }
        }
    }
    
    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra      
        rep_reporte.dibujar();
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
    
    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }
    
    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }
    
    public Reporte getRep_reporte() {
        return rep_reporte;
    }
    
    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }
}
