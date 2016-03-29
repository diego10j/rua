/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import framework.aplicacion.Fila;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.AjaxBehaviorEvent;

import org.apache.poi.hssf.record.formula.Ptg;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

public class pre_sucursal_area_departamento extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_area_dep = new Tabla();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	private SeleccionTabla set_sucursal = new SeleccionTabla();

	//
	public pre_sucursal_area_departamento() {
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		bar_botones.agregarReporte();


		tab_area_dep.setId("tab_area_dep");
		tab_area_dep.setSql("select DEP.IDE_GEDEP,ARE.IDE_GEARE,DETALLE_GEARE,DEP.DETALLE_GEDEP " +
				"from GEN_AREA ARE " +
				"INNER JOIN GEN_DEPARTAMENTO DEP ON ARE.IDE_GEARE=DEP.IDE_GEARE " +
				"ORDER BY DETALLE_GEARE ASC, DETALLE_GEDEP ASC");
		tab_area_dep.setCampoPrimaria("IDE_GEDEP");
		tab_area_dep.setNumeroTabla(1);
		tab_area_dep.getColumna("DETALLE_GEARE").setNombreVisual("PROCESO");
		tab_area_dep.getColumna("DETALLE_GEDEP").setNombreVisual("SUB - PROCESO");
		
		tab_area_dep.getColumna("DETALLE_GEARE").setFiltro(true);
		tab_area_dep.getColumna("DETALLE_GEDEP").setFiltro(true);
		tab_area_dep.getColumna("IDE_GEARE").setVisible(false);
		tab_area_dep.getColumna("IDE_GEDEP").setVisible(false);

		tab_area_dep.setLectura(true);
		tab_area_dep.setRows(10);
		tab_area_dep.onSelect("filtrarDepartamentoSucursal");
		tab_area_dep.dibujar();
		PanelTabla pat_panel2 = new PanelTabla(); 
		pat_panel2.getMenuTabla().getItem_formato().setDisabled(true);

		pat_panel2.setPanelTabla(tab_area_dep);
		

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setGenerarPrimaria(false);
		tab_tabla1.setTabla("GEN_DEPARTAMENTO_SUCURSAL", "IDE_SUCU", 2);
		tab_tabla1.getColumna("IDE_GEDEP").setVisible(false);
		tab_tabla1.getColumna("IDE_GEARE").setVisible(false);
		tab_tabla1.getColumna("IDE_GEDEP").setUnico(true);
		tab_tabla1.getColumna("IDE_GEARE").setUnico(true);
		tab_tabla1.getColumna("IDE_SUCU").setUnico(true);
		tab_tabla1.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU","");
		tab_tabla1.getColumna("ACTIVO_GEDES").setCheck();
		tab_tabla1.getColumna("ACTIVO_GEDES").setValorDefecto("true");

		tab_tabla1.setMostrarcampoSucursal(true);
		tab_tabla1.setRows(10);
		tab_tabla1.setCondicion("IDE_GEARE="+tab_area_dep.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_area_dep.getValor("IDE_GEDEP"));
		tab_tabla1.dibujar();
		tab_tabla1.getColumna("IDE_SUCU").setExterna(false);
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("SUB - PROCESO SUCURSAL");
		pat_panel1.setPanelTabla(tab_tabla1);



		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel2,pat_panel1,"60%","H");
		agregarComponente(div_division);


		sef_reporte.setId("sef_reporte");
		agregarComponente(rep_reporte);
		agregarComponente(sef_reporte);
		set_sucursal.setId("set_sucursal");
		set_sucursal.setSeleccionTabla("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU");
		set_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_sucursal);

	}



	public void filtrarDepartamentoSucursal(SelectEvent evt){
		tab_area_dep.seleccionarFila(evt);
		tab_tabla1.setCondicion("IDE_GEARE="+tab_area_dep.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_area_dep.getValor("IDE_GEDEP"));
		tab_tabla1.ejecutarSql();
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}


	Map p_parametros= new HashMap();

	public void aceptarReporte(){
		if (rep_reporte.getReporteSelecionado().equals("Detalle Sucursal Area Departamento")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();				
				p_parametros.put("titulo", "Detalle Sucursal Area Departamento");
				set_sucursal.setTitle("SELECCIONE SUCURSAL");
				set_sucursal.dibujar();
				utilitario.addUpdate("rep_reporte,set_sucursal");
			}else if(set_sucursal.isVisible()) {
				p_parametros.put("SUCURSAL", set_sucursal.getSeleccionados());
				set_sucursal.cerrar();
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
				sef_reporte.dibujar();
				utilitario.addUpdate("sef_reporte,set_sucursal");
			}
		}

	}



	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}

	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}




	@Override
	public void insertar() {
		if (tab_tabla1.isFocus()) {
			tab_tabla1.insertar();
			tab_tabla1.setValor("IDE_GEARE", tab_area_dep.getValor("IDE_GEARE"));
			tab_tabla1.setValor("IDE_GEDEP", tab_area_dep.getValor("IDE_GEDEP"));
		} 	
	}

	@Override
	public void guardar() {		
		if (tab_tabla1.guardar()) {
			guardarPantalla();
		}
	}

	@Override
	public void eliminar() {
		if (tab_tabla1.isFocus()) {
			if (tab_tabla1.eliminar()) {
			}

		}	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		tab_tabla1.setCondicion("IDE_GEARE="+tab_area_dep.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_area_dep.getValor("IDE_GEDEP"));
		tab_tabla1.ejecutarSql();

	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		tab_tabla1.setCondicion("IDE_GEARE="+tab_area_dep.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_area_dep.getValor("IDE_GEDEP"));
		tab_tabla1.ejecutarSql();

	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		tab_tabla1.setCondicion("IDE_GEARE="+tab_area_dep.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_area_dep.getValor("IDE_GEDEP"));
		tab_tabla1.ejecutarSql();

	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		tab_tabla1.setCondicion("IDE_GEARE="+tab_area_dep.getValor("IDE_GEARE")+" and IDE_GEDEP="+tab_area_dep.getValor("IDE_GEDEP"));
		tab_tabla1.ejecutarSql();

	}

	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
	}


	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}


	public SeleccionTabla getSet_sucursal() {
		return set_sucursal;
	}

	public void setSet_sucursal(SeleccionTabla set_sucursal) {
		this.set_sucursal = set_sucursal;
	}




	public Tabla getTab_area_dep() {
		return tab_area_dep;
	}




	public void setTab_area_dep(Tabla tab_area_dep) {
		this.tab_area_dep = tab_area_dep;
	}

}
