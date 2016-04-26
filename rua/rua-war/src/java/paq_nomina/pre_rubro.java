/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;


public class pre_rubro extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private AutoCompletar aut_cuenta_contable=new AutoCompletar();
	private Combo com_cuenta_contable=new Combo();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private SeleccionTabla  sel_tia = new SeleccionTabla();
	private Map p_parametros=new HashMap();
	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	
	public pre_rubro() {        

		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);


		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		bar_botones.agregarBoton(bot_limpiar);		
		//    	agregarComponente(aut_cuenta_contable);

		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("NRH_RUBRO", "IDE_NRRUB", 1);
		tab_tabla.getColumna("ACTIVO_NRRUB").setCheck();
		tab_tabla.getColumna("ACTIVO_NRRUB").setValorDefecto("true");
		tab_tabla.getColumna("ANTICIPO_NRRUB").setCheck();
		tab_tabla.setCampoOrden("DETALLE_NRRUB");
		tab_tabla.getColumna("IDE_NRFOC").setCombo("NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC", "");
		tab_tabla.getColumna("IDE_NRTIR").setCombo("NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR", "");
		tab_tabla.getColumna("DETALLE_NRRUB").setFiltro(true);
		tab_tabla.getColumna("DETALLE_NRRUB").setUnico(true);
		tab_tabla.getColumna("IDE_NRFOC").setUnico(true);
		tab_tabla.getColumna("DECIMO_NRRUB").setCheck();
		tab_tabla.getColumna("DECIMO_NRRUB").setValorDefecto("false");

		tab_tabla.agregarRelacion(tab_tabla2);
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setMensajeWarn("RUBROS");
		pat_panel.setPanelTabla(tab_tabla);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("NRH_RUBRO_ASIENTO", "IDE_NRRUA", 2);
		tab_tabla2.getColumna("ACTIVO_NRRUA").setCheck();
		tab_tabla2.getColumna("ACTIVO_NRRUA").setValorDefecto("TRUE");
		tab_tabla2.getColumna("TODOS_NRRUA").setCheck();
		tab_tabla2.getColumna("TODOS_NRRUA").setValorDefecto("TRUE");

		tab_tabla2.getColumna("IDE_GETIA").setCombo("GEN_TIPO_ASIENTO", "IDE_GETIA", "DETALLE_GETIA", "");
		tab_tabla2.getColumna("IDE_GELUA").setCombo("GEN_LUGAR_APLICA", "IDE_GELUA", "DETALLE_GELUA", "");
		tab_tabla2.getColumna("IDE_GECUC").setCombo("GEN_CUENTA_CONTABLE", "IDE_GECUC", "CODIGO_CUENTA_GECUC,DETALLE_GECUC", "");
		tab_tabla2.getColumna("IDE_GECUC").setAutoCompletar();
		//tab_tabla2.getColumna("ide_cocac").setCombo(ser_contabilidad.servicioCatalogoCuentaCombo());
		//tab_tabla2.getColumna("ide_cocac").setAutoCompletar();		tab_tabla2.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE","DETALLE_GEARE","");		
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("RUBROS PARA ASIENTO CONTABLE");		
		pat_panel2.setPanelTabla(tab_tabla2);

		sel_tia.setId("sel_tia");	
		sel_tia.setTitle("TIPO ASIENTO");
		sel_tia.setSeleccionTabla("select IDE_GETIA,DETALLE_GETIA from GEN_TIPO_ASIENTO","IDE_GETIA");
		sel_tia.getTab_seleccion().getColumna("DETALLE_GETIA").setFiltro(true);
		sel_tia.setRadio();
		sel_tia.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tia);


		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel, pat_panel2, "50%", "H");
		agregarComponente(div_division);      

	}    

	public void limpiar(){
		tab_tabla.setCondicion("IDE_NRRUB IN (select ide_nrrub from NRH_RUBRO)");
		tab_tabla.ejecutarSql();
		com_cuenta_contable.setValue(null);
		utilitario.addUpdate("com_cuenta_contable");
	}

	//    public void seleccionaCuentaContable(SelectEvent evt){
	public void seleccionaCuentaContable(){
		//    	aut_cuenta_contable.onSelect(evt);
		TablaGenerica tab_rubros=utilitario.consultar("select * from NRH_RUBRO where IDE_NRRUB in ( " +
				"select IDE_NRRUB from NRH_RUBRO_ASIENTO where IDE_GECUC="+com_cuenta_contable.getValue()+")");
		if (tab_rubros.getTotalFilas()>0){

			//    		tab_tabla.setFilaActual(tab_rubros.getValor("IDE_NRRUB"));
			//    		utilitario.addUpdate("tab_tabla");
			//    		tab_tabla.calcularPaginaActual();

			tab_tabla.setCondicion("IDE_NRRUB IN (select ide_nrrub from NRH_RUBRO where IDE_NRRUB in ( " +
					"select IDE_NRRUB from NRH_RUBRO_ASIENTO where IDE_GECUC="+com_cuenta_contable.getValue()+") )");
			tab_tabla.ejecutarSql();

			//    		
		}else{
			utilitario.agregarMensajeInfo("No existe rubro configurado para la cuenta contable "+aut_cuenta_contable.getValorArreglo(1),"");
		}
	}

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		tab_tabla.guardar();
		tab_tabla2.guardar();
		utilitario.getConexion().guardarPantalla();
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}
	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}

	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}

	public AutoCompletar getAut_cuenta_contable() {
		return aut_cuenta_contable;
	}

	public void setAut_cuenta_contable(AutoCompletar aut_cuenta_contable) {
		this.aut_cuenta_contable = aut_cuenta_contable;
	}

	public Combo getCom_cuenta_contable() {
		return com_cuenta_contable;
	}

	public void setCom_cuenta_contable(Combo com_cuenta_contable) {
		this.com_cuenta_contable = com_cuenta_contable;
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

	public SeleccionTabla getSel_tia() {
		return sel_tia;
	}

	public void setSel_tia(SeleccionTabla sel_tia) {
		this.sel_tia = sel_tia;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Plantilla Contable")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				sel_tia.setSql("select IDE_GETIA,DETALLE_GETIA from GEN_TIPO_ASIENTO");				//utilitario.ejecutarJavaScript(set_empleado.getTab_seleccion().getId()+".clearFilters();");				
				sel_tia.getTab_seleccion().ejecutarSql();
				sel_tia.getBot_aceptar().setMetodo("aceptarReporte");
				sel_tia.dibujar();
			}else if (sel_tia.isVisible()){
					if (sel_tia.getValorSeleccionado()!=null && !sel_tia.getValorSeleccionado().isEmpty()) {
						
						p_parametros.put("IDE_GETIA",Long.parseLong(sel_tia.getValorSeleccionado()));
						System.out.println(""+p_parametros);
						p_parametros.put("titulo","PLANTILLA CONTABLE");
						sel_tia.cerrar();
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						sef_reporte.dibujar();					
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
					}
				}
			
		}
	}



}
