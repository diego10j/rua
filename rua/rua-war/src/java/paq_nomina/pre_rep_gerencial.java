/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


import org.apache.commons.digester.SetRootRule;
import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.export.DataExporter;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.row.Row;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imprimir;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelAcordion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

/**
 *
 * @author Diego
 */
public class pre_rep_gerencial extends Pantalla {


	private Grid grid_grafico = new Grid();
	private Division div_division = new Division();
	private Tabla tab_datos=new Tabla();

	public BarChart barchart = new BarChart();
	private PieChart piechart=new PieChart();
	public CartesianChartModel categoryModel;
	private PieChartModel pieModel;

	private SeleccionCalendario sec_rango_fechas=new SeleccionCalendario();
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	private Dialogo dia_tipo_rep=new Dialogo();
	private Radio rad=new Radio();
	private Radio rad_suc_are_dep=new Radio();
	private PanelAcordion pac_acordion = new PanelAcordion();
	private Check che_agrupar = new Check();

	private SeleccionTabla set_tipo_nomina=new SeleccionTabla();
	private SeleccionTabla set_rubros=new SeleccionTabla();
	private Boton bot_exportar=new Boton();
	cls_graficas grafico=new cls_graficas();

	public pre_rep_gerencial() {        

		bar_botones.limpiar();
		Boton bot_ver_nomina_global=new Boton();
		bot_ver_nomina_global.setMetodo("graficarReporte");
		bot_ver_nomina_global.setValue("GRAFICAR REPORTE");




		bar_botones.agregarBoton(bot_ver_nomina_global);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setTitle("Limpiar");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		Boton bot_zoom_in = new Boton();
		bot_zoom_in.setValue("Pastel");
		bot_zoom_in.setTitle("Pastel");
		bot_zoom_in.setMetodo("dibujarPastel");
		bar_botones.agregarBoton(bot_zoom_in);



		dia_tipo_rep.setId("dia_tipo_rep");


		List<Object> lis=new ArrayList<Object>();
		Object obj[]=new Object[2];
		obj[0]=0;
		obj[1]="Tipo Nomina Vs (Sucursal,Areas,Departamentos)";
		lis.add(obj);
		Object obj2[]=new Object[2];
		obj2[0]=1;
		obj2[1]="Tipo Nomina Vs (A�os,Meses)";
		lis.add(obj2);		
		Object obj3[]=new Object[2];
		obj3[0]=2;
		obj3[1]="(Rubros) Vs Tipo Nomina";
		lis.add(obj3);		

		rad.setId("rad");
		rad.setRadio(lis);
		rad.setVertical();

		Grid gri_tip_rep=new Grid();
		gri_tip_rep.setColumns(1);
		gri_tip_rep.setWidth("100%");
		gri_tip_rep.getChildren().add(rad);

		dia_tipo_rep.setWidth("30%");
		dia_tipo_rep.setHeight("30%");
		dia_tipo_rep.setDialogo(gri_tip_rep);
		dia_tipo_rep.getBot_aceptar().setMetodo("aceptarTipoReporte");
		dia_tipo_rep.setDynamic(false);

		agregarComponente(dia_tipo_rep);


		bot_exportar=new Boton();
		bot_exportar.setId("bot_exportar");
		bot_exportar.setValue("Exportar");
		bot_exportar.setType("button");
		bot_exportar.setIcon("ui-icon-extlink");

		bar_botones.agregarBoton(bot_exportar);

		Dialog dia_expor=new Dialog();
		dia_expor.setModal(true);
		dia_expor.setHeader("GRAFICO");
		dia_expor.setShowEffect("fade");
		dia_expor.setWidgetVar("dlg");
		dia_expor.setAppendToBody(true);



		OutputPanel out_panel=new OutputPanel();
		out_panel.setId("output");
		out_panel.setLayout("block");
		out_panel.setStyle("width:100%;height:100%");
		dia_expor.getChildren().add(out_panel);

		agregarComponente(dia_expor);

		che_agrupar=new Check();
		che_agrupar.setId("che_agrupar");
		che_agrupar.setMetodoChange("agruparLabels");

		bar_botones.agregarComponente(new Etiqueta("Agrupar"));
		bar_botones.agregarComponente(che_agrupar);


		div_division.setId("div_division");
		dibujarTabla();
		agregarComponente(div_division);


		//estilo pie
		piechart.setId("sample");
		piechart.setWidgetVar("sample");
		piechart.setFill(true);
		piechart.setShowDataLabels(true);
		piechart.setDiameter(400);
		piechart.setSliceMargin(5);
		piechart.setLegendPosition("e");
		piechart.setStyle("width:900px;height:500px");
		piechart.setTitle("Total x Departamentos");

		Ajax evt1=new Ajax();
		evt1.setMetodo("itemSelectPastel");
		piechart.addClientBehavior("itemSelect", evt1);


		sec_rango_fechas.setId("sec_rango_fechas");
		sec_rango_fechas.getBot_aceptar().setMetodo("aceptarRangoFechas");
		sec_rango_fechas.setTitle("Seleccion Rango de Fechas");
		sec_rango_fechas.setDynamic(false);
		agregarComponente(sec_rango_fechas);

		set_tipo_nomina.setId("set_tipo_nomina");
		set_tipo_nomina.setSeleccionTabla("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN " +
				"FROM NRH_DETALLE_TIPO_NOMINA a " +
				"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
				"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin ","IDE_NRDTN");
		set_tipo_nomina.setDynamic(false);
		agregarComponente(set_tipo_nomina);
		
		set_rubros.setId("set_rubros");
		set_rubros.setSeleccionTabla("SELECT RUB.IDE_NRRUB,DETALLE_NRRUB " +
				"from NRH_RUBRO rub " +
				"inner join NRH_DETALLE_RUBRO der on der.IDE_NRRUB=RUB.IDE_NRRUB " +
				"and IMPRIME_NRDER=true " +
				"group by RUB.IDE_NRRUB,DETALLE_NRRUB","IDE_NRRUB");
		set_rubros.getTab_seleccion().getColumna("DETALLE_NRRUB").setFiltro(true);
		set_rubros.setDynamic(false);
		agregarComponente(set_rubros);


	}

	public void graficarReporte(){
		dia_tipo_rep.dibujar();
	}

	public void aceptarTipoReporte(){
		che_agrupar.setValue(false);
		utilitario.addUpdate("che_agrupar");
		if (rad.getValue().equals("0")){
			dia_tipo_rep.cerrar();
			sec_rango_fechas.setFecha1(null);
			sec_rango_fechas.setFecha2(null);
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarSalariosPorSucursalVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}else if (rad.getValue().equals("1")){
			dia_tipo_rep.cerrar();
			sec_rango_fechas.setFecha1(null);
			sec_rango_fechas.setFecha2(null);
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarMesesVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}else if (rad.getValue().equals("2")){
			dia_tipo_rep.cerrar();
			sec_rango_fechas.setFecha1(null);
			sec_rango_fechas.setFecha2(null);
			
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarRubrosVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}
	}

	//	public void aceptarRangoFechasPorAnios(){
	//		if (sec_rango_fechas.isFechasValidas()){
	//			str_value_rad_anios_meses="0";
	//			String ide_gepro=ser_nomina.getPeriodosRol(sec_rango_fechas.getFecha1String(),sec_rango_fechas.getFecha2String());
	//			graficoAniosVsTipoNomina();
	//		}else{
	//			utilitario.agregarMensajeInfo("Fechas invalidas","");
	//		}
	//	}


	public void construirGrafico(TablaGenerica tab_rep,String titulo){

		categoryModel=new CartesianChartModel();		

		Ajax evt=new Ajax();
		evt.setMetodo("itemSelect");

		barchart=new BarChart();
		barchart=grafico.getBarchar(tab_rep,titulo,500,570);
		
		barchart.addClientBehavior("itemSelect", evt);

		Etiqueta eti = new Etiqueta("GR�FICOS ESTAD�STICOS");
		eti.setStyle("font-size:18px;");
		grid_grafico.getChildren().clear();
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");
		grid_grafico.getChildren().add(barchart);

		pac_acordion=new PanelAcordion();
		pac_acordion.setId("pac_acordion");

		tab_datos=new Tabla();
		tab_datos.setId("tab_datos");
		tab_datos.setIdCompleto("pac_acordion:tab_datos");
		tab_datos.setSql(tab_rep_pantalla.getSql()); 
		tab_datos.setCampoPrimaria("ide_label");
		tab_datos.setNumeroTabla(1);
		tab_datos.setLectura(true);
		tab_datos.setColumnaSuma("MONTO");
		tab_datos.setHeader(barchart.getTitle());
		tab_datos.getColumna("MONTO").setFormatoNumero(2);
		tab_datos.getColumna("MONTO").alinearDerecha();
		tab_datos.getColumna("ide_label").setVisible(false);
		tab_datos.getColumna("ide_col_ejex").setVisible(false);
		tab_datos.getColumna("detalle_label").setNombreVisual("REGIMEN LABORAL");

		tab_datos.dibujar();

		PanelTabla pat_datos=new PanelTabla();
		pat_datos.setPanelTabla(tab_datos);

		pac_acordion.agregarPanel("INFORMACI�N EN TABLA", pat_datos);
		pac_acordion.setRendered(true);

		bot_exportar.setOnclick("$('#formulario\\\\:output').empty().append(basic.exportAsImage());dlg.show(); ");


		rad_suc_are_dep=new Radio();
		Grid gri_cabecera=new Grid();

		gri_cabecera.setWidth("100%");

		Etiqueta eti_titulo=new Etiqueta(titulo);
		eti_titulo.setEstiloCabecera("text-align:center;font-size:10px;font-weight:bold");
		gri_cabecera.getChildren().add(eti_titulo);

		Etiqueta eti_periodo=new Etiqueta("Desde: "+sec_rango_fechas.getFecha1String()+" Hasta: "+sec_rango_fechas.getFecha2String());
		eti_periodo.setEstiloCabecera("text-align:center;font-size:10px;font-weight:bold");
		gri_cabecera.getChildren().add(eti_periodo);


		if (rad.getValue().equals("0")){// grafico (sucursal, area , departamento) Vs Tipo de Nomina
			String str_tipo_nomina="";
			for (int i = 0; i < set_tipo_nomina.getListaSeleccionados().size(); i++) {
				Fila fila=set_tipo_nomina.getListaSeleccionados().get(i);
				str_tipo_nomina+=fila.getCampos()[1]+" - "+fila.getCampos()[2]+",";
			}
			str_tipo_nomina=str_tipo_nomina.substring(0,str_tipo_nomina.length()-1);

			Etiqueta eti_nomina=new Etiqueta(str_tipo_nomina);
			eti_nomina.setEstiloCabecera("text-align:center;font-size:10px;font-weight:bold");
			gri_cabecera.getChildren().add(eti_nomina);

			List<Object> lis=new ArrayList<Object>();
			Object obj[]=new Object[2];
			obj[0]=0;
			obj[1]="Por Sucursal";
			lis.add(obj);
			Object obj0[]=new Object[2];
			obj0[0]=1;
			obj0[1]="Por Areas";
			lis.add(obj0);

			Object obj1[]=new Object[2];
			obj1[0]=2;
			obj1[1]="Por Departamentos";
			lis.add(obj1);

			rad_suc_are_dep.setId("rad_suc_are_dep");
			rad_suc_are_dep.setRadio(lis);
			rad_suc_are_dep.setVertical();
			rad_suc_are_dep.setMetodoChange("cambiaTipoGraficoSucAreDep");
			rad_suc_are_dep.setValue(str_value_rad_sad);
			Grid gri_suc_are_dep=new Grid();
			gri_suc_are_dep.setColumns(1);
			gri_suc_are_dep.setWidth("100%");
			gri_suc_are_dep.getChildren().add(rad_suc_are_dep);

			gri_cabecera.getChildren().add(gri_suc_are_dep);
		}else if (rad.getValue().equals("1")){// grafico (a�os,meses) Vs Tipo de Nomina

			List<Object> lis=new ArrayList<Object>();
			Object obj[]=new Object[2];
			obj[0]=0;
			obj[1]="Por A�os";
			lis.add(obj);
			Object obj0[]=new Object[2];
			obj0[0]=1;
			obj0[1]="Por Meses";
			lis.add(obj0);

			rad_suc_are_dep.setId("rad_suc_are_dep");
			rad_suc_are_dep.setRadio(lis);
			rad_suc_are_dep.setVertical();
			rad_suc_are_dep.setMetodoChange("cambiaTipoGraficoA�osMeses");
			rad_suc_are_dep.setValue(str_value_rad_anios_meses);
			Grid gri_suc_are_dep=new Grid();
			gri_suc_are_dep.setColumns(1);
			gri_suc_are_dep.setWidth("100%");
			gri_suc_are_dep.getChildren().add(rad_suc_are_dep);

			gri_cabecera.getChildren().add(gri_suc_are_dep);
		}

		gri_cabecera.getChildren().add(pac_acordion);

		div_division.getChildren().clear();
		div_division.dividir2(gri_cabecera,grid_grafico,"30%","V");
		div_division.getDivision1().setHeader("MENU DE DATOS");
		div_division.getDivision1().setCollapsible(true);
		utilitario.addUpdate("div_division,bot_exportar");

	}
	String str_value_rad_anios_meses="";
	public void cambiaTipoGraficoAnosMeses(){
		che_agrupar.setValue(false);
		utilitario.addUpdate("che_agrupar");
		if (rad_suc_are_dep.getValue().equals("0")){
			dia_tipo_rep.cerrar();
			set_tipo_nomina.getTab_seleccion().setSql("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN " +
					"FROM NRH_DETALLE_TIPO_NOMINA a " +
					"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
					"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin ");
			set_tipo_nomina.getTab_seleccion().ejecutarSql();
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarAniosVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}else if (rad_suc_are_dep.getValue().equals("1")){
			dia_tipo_rep.cerrar();
			set_tipo_nomina.getTab_seleccion().setSql("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN " +
					"FROM NRH_DETALLE_TIPO_NOMINA a " +
					"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
					"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin ");
			set_tipo_nomina.getTab_seleccion().ejecutarSql();
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarMesesVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}
	}
	public void cambiaTipoGraficoSucAreDep(){
		che_agrupar.setValue(false);
		utilitario.addUpdate("che_agrupar");
		if (rad_suc_are_dep.getValue().equals("0")){
			dia_tipo_rep.cerrar();
			set_tipo_nomina.getTab_seleccion().setSql("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN " +
					"FROM NRH_DETALLE_TIPO_NOMINA a " +
					"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
					"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin ");
			set_tipo_nomina.getTab_seleccion().ejecutarSql();
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarSalariosPorSucursalVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}else if (rad_suc_are_dep.getValue().equals("1")){
			dia_tipo_rep.cerrar();
			set_tipo_nomina.getTab_seleccion().setSql("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN " +
					"FROM NRH_DETALLE_TIPO_NOMINA a " +
					"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
					"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin ");
			set_tipo_nomina.getTab_seleccion().ejecutarSql();
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarSalariosPorAreasVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}else if (rad_suc_are_dep.getValue().equals("2")){
			dia_tipo_rep.cerrar();
			set_tipo_nomina.getTab_seleccion().setSql("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN " +
					"FROM NRH_DETALLE_TIPO_NOMINA a " +
					"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
					"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin ");
			set_tipo_nomina.getTab_seleccion().ejecutarSql();
			set_tipo_nomina.getBot_aceptar().setMetodo("graficarSalariosPorDepartamentosVsTipoNomina");
			set_tipo_nomina.dibujar();
			utilitario.addUpdate("set_tipo_nomina");
		}
	}

	public void agruparLabels(AjaxBehaviorEvent evt){
		System.out.println("si entra "+che_agrupar.getValue());
		if (che_agrupar.getValue().equals(true)){
			String str_sql="select '131' as ide_label, " +
					"'TOTAL SALARIOS' as detalle_label, " +
					"ide_col_ejex,nom_col_ejex,sum (monto) as monto " +
					"from ( ";
			str_sql=str_sql.concat(tab_rep_pantalla.getSql());
			str_sql=str_sql.concat(" ) a GROUP BY ide_col_ejex,nom_col_ejex " +
					"ORDER by nom_col_ejex");
			System.out.println("sql agrupado "+str_sql);
			tab_rep_pantalla=utilitario.consultar(str_sql);

			construirGrafico(tab_rep_pantalla, barchart.getTitle());
		}else{
			if (rad.getValue().equals("0")){
				if (rad_suc_are_dep.getValue().equals("0")){
					graficarSalariosPorSucursalVsTipoNomina();
				}else if (rad_suc_are_dep.getValue().equals("1")){
					graficarSalariosPorAreasVsTipoNomina();
				}else if (rad_suc_are_dep.getValue().equals("2")){
					graficarSalariosPorDepartamentosVsTipoNomina();
				}
			}else if (rad.getValue().equals("1")){
				if (rad_suc_are_dep.getValue().equals("0")){
					graficarAniosVsTipoNomina();
				}else if (rad_suc_are_dep.getValue().equals("1")){
					graficarMesesVsTipoNomina();
				}
			}else if (rad.getValue().equals("2")){
				graficarRubrosVsTipoNomina();
			}
		}

	}

	public void graficarAniosVsTipoNomina(){

		if (set_tipo_nomina.isVisible()){
			if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
				set_tipo_nomina.cerrar();
				sec_rango_fechas.getBot_aceptar().setMetodo("graficarAniosVsTipoNomina");
				sec_rango_fechas.dibujar();
				utilitario.addUpdate("sec_rango_fechas");
				return;
			}else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un tipo de nomina", "");
				return;
			}
		}else if (sec_rango_fechas.isVisible()){
			if (sec_rango_fechas.isFechasValidas()){
				if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
						&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
					sec_rango_fechas.cerrar();
				}else{
					utilitario.agregarMensajeInfo("Fecha no valida", "");
					return;
				}
			}else{
				utilitario.agregarMensajeInfo("Fecha no valida", "");
				return;
			}
		}


		if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
			if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
					&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){

				str_value_rad_anios_meses="0";
				String ide_gepro=ser_nomina.getPeriodosRol(sec_rango_fechas.getFecha1String(),sec_rango_fechas.getFecha2String());

				tab_rep_pantalla=utilitario.consultar("SELECT detatipono.ide_nrdtn as ide_label, " +
						"TIN.DETALLE_NRTIN ||' - '|| TIPOEMP.detalle_gttem as detalle_label, " +
						"anio.ide_geani as ide_col_ejeX, " +
						"detalle_geani as nom_col_ejeX, " +
						"SUM(VALOR_NRDRO) as MONTO " +
						"FROM NRH_DETALLE_ROL DETA LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
						"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
						"INNER JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DETATIPONO.IDE_NRTIN "+
						"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP  " +
						"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
						"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON " +
						"TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM LEFT JOIN SIS_SUCURSAL SUCU ON " +
						"SUCU.IDE_SUCU=PAR.IDE_SUCU LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER LEFT " +
						"JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON " +
						"OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON " +
						"FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN " +
						"GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
						"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON " +
						"PERIODO.IDE_GEPRO=ROL.IDE_GEPRO LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES LEFT JOIN GEN_ANIO " +
						"ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
						"WHERE detatipono.ide_nrdtn IN ("+set_tipo_nomina.getSeleccionados()+") " +
						"AND ROL.IDE_GEPRO IN ("+ide_gepro+") " +
						"AND RUB.IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_valor_recibir")+") " +
						"GROUP BY detatipono.ide_nrdtn,TIN.DETALLE_NRTIN,TIPOEMP.detalle_gttem,detalle_geani,anio.ide_geani " +
						"HAVING sum (DETA.VALOR_NRDRO)>0 " +
						"order by ide_label,nom_col_ejeX");

				if (tab_rep_pantalla.getTotalFilas()>0){

					construirGrafico(tab_rep_pantalla,"SALARIOS POR ANIOS");
				}else{
					utilitario.agregarMensajeInfo("No se puede graficar","No existen datos para el periodo seleccionado");
					return;
				}
			}
		}


	}


	public void graficarRubrosVsTipoNomina(){
		if (set_tipo_nomina.isVisible()){
			if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
				set_tipo_nomina.cerrar();
				set_rubros.getTab_seleccion().setSql("SELECT RUB.IDE_NRRUB,DETALLE_NRRUB " +
						"from NRH_RUBRO rub " +
						"inner join NRH_DETALLE_RUBRO der on der.IDE_NRRUB=RUB.IDE_NRRUB " +
						"and IMPRIME_NRDER=true " +
						"group by RUB.IDE_NRRUB,DETALLE_NRRUB");
				set_rubros.getTab_seleccion().ejecutarSql();
				set_rubros.getBot_aceptar().setMetodo("graficarRubrosVsTipoNomina");
				set_rubros.dibujar();
				utilitario.addUpdate("set_rubros");
				return;
			}else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un tipo de nomina", "");
				return;
			}
		}else if (set_rubros.isVisible()){
			if (set_rubros.getSeleccionados()!=null && !set_rubros.getSeleccionados().isEmpty()){
				set_rubros.cerrar();
				sec_rango_fechas.getBot_aceptar().setMetodo("graficarRubrosVsTipoNomina");
				sec_rango_fechas.dibujar();
				utilitario.addUpdate("sec_rango_fechas");
				return;
			}else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un rubro", "");
				return;
			}
		}
		
		else if (sec_rango_fechas.isVisible()){
			if (sec_rango_fechas.isFechasValidas()){
				if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
						&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
					sec_rango_fechas.cerrar();
				}else{
					utilitario.agregarMensajeInfo("Fecha no valida", "");
					return;
				}
			}else{
				utilitario.agregarMensajeInfo("Fecha no valida", "");
				return;
			}
		}


		if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
			if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
					&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){

				str_value_rad_anios_meses="1";

				String ide_gepro=ser_nomina.getPeriodosRol(sec_rango_fechas.getFecha1String(), 
						sec_rango_fechas.getFecha2String());

				String titulo = "TOTAL RUBROS POR TIPO DE NOMINA";
				tab_rep_pantalla=utilitario.consultar("select " +
						"rub.ide_nrrub as ide_label, " +
						"rub.detalle_nrrub as detalle_label, " +
						"detatipono.ide_nrdtn as ide_col_ejeX, " +
						"TIN.DETALLE_NRTIN ||' - '|| TIPOEMP.detalle_gttem as nom_col_ejeX  , " +
						"SUM(VALOR_NRDRO) as MONTO " +
						"from NRH_DETALLE_ROL deta " +
						"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
						"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
						"INNER JOIN NRH_TIPO_NOMINA tin ON TIN.IDE_NRTIN=DETATIPONO.IDE_NRTIN " +
						"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
						"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
						"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM " +
						"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
						"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
						"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " +
						"LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " +
						"LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " +
						"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP " +
						"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " +
						"LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
						"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR " +
						"LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " +
						"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " +
						"LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
						"WHERE detatipono.ide_nrdtn IN ("+set_tipo_nomina.getSeleccionados()+") " +
						"AND ROL.IDE_GEPRO IN ("+ide_gepro+") " +
						"AND RUB.IDE_NRRUB IN ("+set_rubros.getSeleccionados()+") " +
						"group by rub.ide_nrrub, " +
						"rub.detalle_nrrub , " +
						"detatipono.ide_nrdtn , " +
						"TIN.DETALLE_NRTIN, " +
						"TIPOEMP.detalle_gttem " +
						"HAVING sum (DETA.VALOR_NRDRO)>0 "+
						"order by ide_label,ide_col_ejeX  ");

				if (tab_rep_pantalla.getTotalFilas()>0){
					construirGrafico(tab_rep_pantalla,titulo);
				}else{
					utilitario.agregarMensajeInfo("No se puede graficar","No existen datos para el periodo seleccionado");
					return;
				}
			}
		}
	}
	
	
	TablaGenerica tab_rep_pantalla=new TablaGenerica();

	public void graficarMesesVsTipoNomina(){

		if (set_tipo_nomina.isVisible()){
			if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
				set_tipo_nomina.cerrar();
				sec_rango_fechas.getBot_aceptar().setMetodo("graficarMesesVsTipoNomina");
				sec_rango_fechas.dibujar();
				utilitario.addUpdate("sec_rango_fechas");
				return;
			}else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un tipo de nomina", "");
				return;
			}
		}else if (sec_rango_fechas.isVisible()){
			if (sec_rango_fechas.isFechasValidas()){
				if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
						&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
					sec_rango_fechas.cerrar();
				}else{
					utilitario.agregarMensajeInfo("Fecha no valida", "");
					return;
				}
			}else{
				utilitario.agregarMensajeInfo("Fecha no valida", "");
				return;
			}
		}


		if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
			if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
					&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){

				str_value_rad_anios_meses="1";

				String ide_gepro=ser_nomina.getPeriodosRol(sec_rango_fechas.getFecha1String(), 
						sec_rango_fechas.getFecha2String());

				String titulo = "TOTAL SALARIOS POR MESES";
				tab_rep_pantalla=utilitario.consultar("SELECT detatipono.ide_nrdtn as ide_label," +
						"TIN.DETALLE_NRTIN ||' - '|| TIPOEMP.detalle_gttem as detalle_label,  " +
						"mes.ide_gemes as ide_col_ejeX," +
						"detalle_geani ||' - '|| detalle_gemes as nom_col_ejeX, " +
						"SUM(VALOR_NRDRO) as MONTO, " +
						"detalle_geani "+
						"FROM NRH_DETALLE_ROL DETA LEFT JOIN NRH_ROL ROL ON " +
						"ROL.IDE_NRROL=DETA.IDE_NRROL " +
						"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
						"INNER JOIN NRH_TIPO_NOMINA tin ON TIN.IDE_NRTIN=DETATIPONO.IDE_NRTIN "+
						"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP LEFT JOIN " +
						"GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
						"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM "+ 
						"AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM "+
						"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU LEFT JOIN " +
						"NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER LEFT JOIN GEN_DEPARTAMENTO DEP ON " +
						"DEP.IDE_GEDEP=PAR.IDE_GEDEP LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON " +
						"OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON " +
						"FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN " +
						"GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
						"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON " +
						"PERIODO.IDE_GEPRO=ROL.IDE_GEPRO LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES LEFT JOIN GEN_ANIO " +
						"ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " + 
						"WHERE detatipono.ide_nrdtn IN ("+set_tipo_nomina.getSeleccionados()+") AND ROL.IDE_GEPRO " +
						"IN ("+ide_gepro+") AND RUB.IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_valor_recibir")+") " +
						"GROUP BY detatipono.ide_nrdtn,TIPOEMP.detalle_gttem,detalle_geani,detalle_gemes,mes.ide_gemes,TIN.DETALLE_NRTIN " +
						"HAVING sum (DETA.VALOR_NRDRO)>0 " +
						"order by ide_label,detalle_geani,ide_col_ejeX");

				if (tab_rep_pantalla.getTotalFilas()>0){
					construirGrafico(tab_rep_pantalla,titulo);
				}else{
					utilitario.agregarMensajeInfo("No se puede graficar","No existen datos para el periodo seleccionado");
					return;
				}
			}
		}
	}

	public void itemSelectPastel(ItemSelectEvent event) {  
		System.out.println("si entra al metodo pastel");   

		Map map= pieModel.getData();


		double dou_tot=tab_datos.getSumaColumna("MONTO");

		Iterator it = map.entrySet().iterator();
		int int_indice=0;
		int int_index_sel=event.getItemIndex();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			System.out.println("key "+e.getKey()+" valor "+e.getValue());
			if (int_indice==int_index_sel){
				utilitario.agregarMensaje("Item seleccionado: "+e.getKey(), utilitario.getFormatoNumero(((Double.parseDouble(e.getValue()+"")*100)/dou_tot))+" %   "+"("+e.getValue()+")");
			}
			int_indice=int_indice+1;
		}	     
	}  


	public void dibujarPastel(){

		graficarPastel(tab_rep_pantalla);
	}


	public void graficarPastel(TablaGenerica tab_rep){


		bot_exportar.setOnclick("$('#formulario\\\\:output').empty().append(sample.exportAsImage());dlg.show(); ");

		grid_grafico.getChildren().clear();

		Etiqueta eti = new Etiqueta("GR�FICOS ESTAD�STICOS");
		eti.setStyle("font-size:18px;");
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");

		pieModel=new PieChartModel();


		String str_sql=tab_rep.getSql();
		str_sql="select ide_col_ejex as ide_label,nom_col_ejex as detalle_label,sum (monto) as " +
				"monto from ( ".concat(str_sql);
		str_sql=str_sql.concat(" ) a GROUP BY ide_col_ejex ,nom_col_ejex ORDER BY ide_col_ejex");
		System.out.println("sql pastel "+str_sql);
		TablaGenerica tab_tot_dep=utilitario.consultar(str_sql);

		double dou_tot_salarios=0;
		for (int i = 0; i < tab_tot_dep.getTotalFilas(); i++) {
			pieModel.set(tab_tot_dep.getValor(i,"detalle_label"),Double.parseDouble

					(tab_tot_dep.getValor(i,"MONTO")));
			dou_tot_salarios=dou_tot_salarios+Double.parseDouble(tab_tot_dep.getValor

					(i,"MONTO"));
		}
		piechart.setTitle("Total  ( "+utilitario.getFormatoNumero(dou_tot_salarios)+" )");



		piechart.setValue(pieModel);
		grid_grafico.getChildren().add(piechart);


		tab_datos=new Tabla();
		tab_datos.setId("tab_datos");
		tab_datos.setSql(tab_tot_dep.getSql()); 
		tab_datos.setCampoPrimaria("ide_label");
		tab_datos.setNumeroTabla(1);
		tab_datos.setLectura(true);
		tab_datos.setColumnaSuma("MONTO");
		tab_datos.setHeader(barchart.getTitle());
		tab_datos.getColumna("MONTO").setFormatoNumero(2);
		tab_datos.getColumna("MONTO").alinearDerecha();
		tab_datos.getColumna("ide_label").setVisible(false);
		tab_datos.dibujar();

		PanelTabla pat_datos=new PanelTabla();
		pat_datos.setPanelTabla(tab_datos);

		div_division.getChildren().clear();


		div_division.dividir2(pat_datos,grid_grafico,"30%","V");
		div_division.getDivision1().setHeader("MENU DE DATOS");
		div_division.getDivision1().setCollapsible(true);
		utilitario.addUpdate("div_division,bot_exportar");

	}

	public TablaGenerica getRubroTotalRecibirDepartamentos(String ide_nrdtn,String ide_gepro){
		TablaGenerica tab_tot_dep=utilitario.consultar("SELECT dep.ide_gedep,dep.detalle_gedep as " +
				"departamento,SUM(VALOR_NRDRO) as MONTO " +
				"FROM NRH_DETALLE_ROL DETA " +
				"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = " +
				"DETATIPONO.IDE_NRDTN LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
				"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
				"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " +
				"LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON " +
				"OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON " +
				"FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN " +
				"GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
				"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON " +
				"PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " +
				"LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
				"left join ( SELECT SUCU.IDE_SUCU,area.ide_geare,DEP.IDE_GEDEP, sum " +
				"(DETA.VALOR_NRDRO) AS TOTAL_DEP FROM NRH_DETALLE_ROL DETA LEFT JOIN NRH_ROL ROL ON " +
				"ROL.IDE_NRROL=DETA.IDE_NRROL LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = " +
				"DETATIPONO.IDE_NRDTN LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP LEFT JOIN " +
				"GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO LEFT JOIN GTH_TIPO_EMPLEADO TIPOEMP ON " +
				"TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU LEFT JOIN " +
				"NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER LEFT JOIN GEN_DEPARTAMENTO DEP ON " +
				"DEP.IDE_GEDEP=PAR.IDE_GEDEP LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON " +
				"OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON " +
				"FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP LEFT JOIN " +
				"GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
				"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR LEFT JOIN GEN_PERIDO_ROL PERIODO ON " +
				"PERIODO.IDE_GEPRO=ROL.IDE_GEPRO LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES LEFT JOIN GEN_ANIO " +
				"ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
				"WHERE detatipono.ide_nrdtn IN ("+ide_nrdtn+") AND ROL.IDE_GEPRO IN ("+ide_gepro+") " +
				"AND RUB.IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_valor_recibir")+") " +
				"GROUP BY SUCU.IDE_SUCU,area.ide_geare,DEP.IDE_GEDEP )c on " +
				"c.ide_sucu=sucu.ide_sucu and area.ide_geare=c.ide_geare and dep.ide_gedep=c.ide_gedep left join ( select " +
				"a.ide_gedep,a.total_emp_dep,b.ide_gttco,b.DETALLE_GTTCO,b.tot_emp_tip_con from ( SELECT  A.IDE_GEDEP,COUNT " +
				"(A.IDE_GEEDP) as TOTAL_EMP_DEP FROM ( select EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP from " +
				"NRH_DETALLE_ROL DRO INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON DRO.IDE_GEEDP=EDP.IDE_GEEDP INNER JOIN " +
				"GTH_TIPO_CONTRATO TCO ON TCO.IDE_GTTCO=EDP.IDE_GTTCO WHERE DRO.IDE_NRROL IN (SELECT IDE_NRROL FROM NRH_ROL " +
				"WHERE IDE_NRDTN IN ("+ide_nrdtn+") AND IDE_GEPRO IN ("+ide_gepro+")) GROUP " +
				"BY EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP)A " + 
				"GROUP BY A.IDE_GEDEP) a " +
				"left join ( SELECT a.ide_gedep,a.ide_gttco,a.detalle_gttco,count " +
				"(a.ide_geedp) as tot_emp_tip_con FROM ( select " +
				"EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO,DRO.IDE_GEEDP from NRH_DETALLE_ROL " +
				"DRO INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON DRO.IDE_GEEDP=EDP.IDE_GEEDP INNER JOIN " +
				"GTH_TIPO_CONTRATO TCO ON TCO.IDE_GTTCO=EDP.IDE_GTTCO WHERE DRO.IDE_NRROL IN (SELECT IDE_NRROL FROM NRH_ROL " +
				"WHERE IDE_NRDTN IN ("+ide_nrdtn+") AND IDE_GEPRO IN ("+ide_gepro+")) GROUP " +
				"BY EDP.IDE_SUCU,EDP.IDE_GEARE,EDP.IDE_GEDEP,DRO.IDE_GEEDP,TCO.IDE_GTTCO,TCO.DETALLE_GTTCO) a group by " +
				"a.ide_gttco,a.ide_gedep,a.detalle_gttco )b on a.ide_gedep=b.IDE_GEDEP " +
				"order by a.ide_gedep )d on dep.ide_gedep=d.ide_gedep " +
				"WHERE detatipono.ide_nrdtn IN ("+ide_nrdtn+") " +
				"AND ROL.IDE_GEPRO IN ("+ide_gepro+") " +
				"AND RUB.IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_valor_recibir")+") " +
				"GROUP BY dep.ide_gedep,dep.detalle_gedep " +
				"HAVING sum (DETA.VALOR_NRDRO)>0 " +
				"order by dep.detalle_gedep");
		return tab_tot_dep;
	}



	public void limpiar(){

		barchart.setZoom(false);
		dibujarTabla();
		utilitario.addUpdate("div_division");
	}

	public void graficarSalariosPorDepartamentosVsTipoNomina(){

		if (set_tipo_nomina.isVisible()){
			if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
				set_tipo_nomina.cerrar();
				sec_rango_fechas.getBot_aceptar().setMetodo("graficarSalariosPorDepartamentosVsTipoNomina");
				sec_rango_fechas.dibujar();
				utilitario.addUpdate("sec_rango_fechas");
				return;
			}else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un tipo de nomina", "");
				return;
			}
		}else if (sec_rango_fechas.isVisible()){
			if (sec_rango_fechas.isFechasValidas()){
				if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
						&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
					sec_rango_fechas.cerrar();

				}else{
					utilitario.agregarMensajeInfo("Fecha no valida", "");
					return;
				}
			}else{
				utilitario.agregarMensajeInfo("Fecha no valida", "");
				return;
			}
		}

		if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
			if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
					&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
				String ide_gepro=ser_nomina.getPeriodosRol(sec_rango_fechas.getFecha1String(), sec_rango_fechas.getFecha2String());
				if (ide_gepro==null){
					ide_gepro="-1";
				}
				str_value_rad_sad="2";
				String titulo = "TOTAL SALARIOS POR DEPARTAMENTOS";
				tab_rep_pantalla=getRubrosPorDepartamento(set_tipo_nomina.getSeleccionados(), ide_gepro,utilitario.getVariable("p_nrh_rubro_valor_recibir"));
				if (tab_rep_pantalla.getTotalFilas()>0){
					construirGrafico(tab_rep_pantalla, titulo);
				}else{
					utilitario.agregarMensajeInfo("No se puede graficar","No existen datos");
					return;
				}

			}
		}
	}

	public void graficarSalariosPorAreasVsTipoNomina(){

		if (set_tipo_nomina.isVisible()){
			if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
				set_tipo_nomina.cerrar();
				sec_rango_fechas.getBot_aceptar().setMetodo("graficarSalariosPorAreasVsTipoNomina");
				sec_rango_fechas.dibujar();
				utilitario.addUpdate("sec_rango_fechas");
				return;
			}else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un tipo de nomina", "");
				return;
			}
		}else if (sec_rango_fechas.isVisible()){
			if (sec_rango_fechas.isFechasValidas()){
				if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
						&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
					sec_rango_fechas.cerrar();

				}else{
					utilitario.agregarMensajeInfo("Fecha no valida", "");
					return;
				}
			}else{
				utilitario.agregarMensajeInfo("Fecha no valida", "");
				return;
			}
		}

		if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
			if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
					&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){

				String ide_gepro=ser_nomina.getPeriodosRol(sec_rango_fechas.getFecha1String(), sec_rango_fechas.getFecha2String());
				if (ide_gepro==null){
					ide_gepro="-1";
				}
				str_value_rad_sad="1";				
				String titulo = "TOTAL SALARIOS POR AREAS";
				tab_rep_pantalla=getRubrosPorAreas(set_tipo_nomina.getSeleccionados(), ide_gepro,utilitario.getVariable("p_nrh_rubro_valor_recibir"));
				if (tab_rep_pantalla.getTotalFilas()>0){
					construirGrafico(tab_rep_pantalla, titulo);
				}else{
					utilitario.agregarMensajeInfo("No se puede graficar","No existen datos");
					return;
				}

			}
		}

	}


	public SeleccionTabla getSet_tipo_nomina() {
		return set_tipo_nomina;
	}

	public void setSet_tipo_nomina(SeleccionTabla set_tipo_nomina) {
		this.set_tipo_nomina = set_tipo_nomina;
	}

	String str_value_rad_sad="";
	public void graficarSalariosPorSucursalVsTipoNomina(){
		if (set_tipo_nomina.isVisible()){
			if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
				set_tipo_nomina.cerrar();
				sec_rango_fechas.getBot_aceptar().setMetodo("graficarSalariosPorSucursalVsTipoNomina");
				sec_rango_fechas.dibujar();
				utilitario.addUpdate("sec_rango_fechas");
				return;
			}else{
				utilitario.agregarMensajeInfo("Debe seleccionar al menos un tipo de nomina", "");
				return;
			}
		}else if (sec_rango_fechas.isVisible()){
			if (sec_rango_fechas.isFechasValidas()){
				if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
						&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){
					sec_rango_fechas.cerrar();

				}else{
					utilitario.agregarMensajeInfo("Fecha no valida", "");
					return;
				}
			}else{
				utilitario.agregarMensajeInfo("Fecha no valida", "");
				return;
			}
		}

		if (set_tipo_nomina.getSeleccionados()!=null && !set_tipo_nomina.getSeleccionados().isEmpty()){
			if (sec_rango_fechas.getFecha1String()!=null && !sec_rango_fechas.getFecha1String().isEmpty()
					&& sec_rango_fechas.getFecha2String()!=null && !sec_rango_fechas.getFecha2String().isEmpty()){

				String ide_gepro=ser_nomina.getPeriodosRol(sec_rango_fechas.getFecha1String(), sec_rango_fechas.getFecha2String());
				if (ide_gepro==null){
					ide_gepro="-1";
				}


				str_value_rad_sad="0";


				String titulo = "TOTAL SALARIOS POR SUCURSAL";

				tab_rep_pantalla=utilitario.consultar("select detatipono.ide_nrdtn as ide_label, " +
						"TIPOEMP.detalle_gttem ||' - ' || TIN.DETALLE_NRTIN as detalle_label, " +
						"sucu.ide_sucu as ide_col_ejeX, " +
						"sucu.nom_sucu as nom_col_ejeX, " +
						"SUM(VALOR_NRDRO) as MONTO " +
						"FROM NRH_DETALLE_ROL DETA LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
						"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN=DETATIPONO.IDE_NRDTN " +
						"INNER JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DETATIPONO.IDE_NRTIN "+
						"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
						"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
						"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM " +
						"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
						"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
						"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " +
						"LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " +
						"LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " +
						"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP " +
						"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " +
						"LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
						"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR " +
						"LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " +
						"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " +
						"LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
						"WHERE detatipono.ide_nrdtn IN ("+set_tipo_nomina.getSeleccionados()+") " +
						"AND ROL.IDE_GEPRO IN ("+ide_gepro+") " +
						"AND RUB.IDE_NRRUB IN ("+utilitario.getVariable("p_nrh_rubro_valor_recibir")+") " +
						"GROUP BY sucu.ide_sucu,sucu.nom_sucu,detatipono.ide_nrdtn,TIPOEMP.detalle_gttem,TIN.DETALLE_NRTIN " +
						"HAVING sum (DETA.VALOR_NRDRO)>0 " +
						"order by ide_label,ide_col_ejeX");
				if (tab_rep_pantalla.getTotalFilas()>0){
					construirGrafico(tab_rep_pantalla,titulo);
				}else{
					utilitario.agregarMensajeInfo("No se puede graficar","No existen datos");
					return;
				}

			}
		}

	}


	public TablaGenerica getRubrosPorAreas(String ide_nrdtn,String ide_gepro,String ide_nrrub){
		TablaGenerica tab_rub=utilitario.consultar("select detatipono.ide_nrdtn as ide_label, " +
				"TIPOEMP.detalle_gttem ||' - ' || TIN.DETALLE_NRTIN as detalle_label," +
				"area.ide_geare as ide_col_ejeX," +
				"area.detalle_geare as nom_col_ejeX, " +
				"SUM(VALOR_NRDRO) as MONTO " +
				"FROM NRH_DETALLE_ROL DETA " +
				"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DETATIPONO.IDE_NRTIN "+
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
				"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
				"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM " +
				"AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM "+
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
				"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " +
				"LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " +
				"LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " +
				"LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
				"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR "+
				"LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " +
				"LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
				"WHERE detatipono.ide_nrdtn IN ("+ide_nrdtn+") " +
				"AND ROL.IDE_GEPRO IN ("+ide_gepro+") " +
				"AND RUB.IDE_NRRUB IN ("+ide_nrrub+") " +
				"GROUP BY area.ide_geare,area.detalle_geare,detatipono.ide_nrdtn,TIPOEMP.detalle_gttem,TIN.DETALLE_NRTIN " +
				"HAVING sum (DETA.VALOR_NRDRO)>0 " +
				"order by ide_label,nom_col_ejeX");
		return tab_rub;
	}

	public TablaGenerica getRubrosPorDepartamento(String ide_nrdtn,String ide_gepro,String ide_nrrub){
		TablaGenerica tab_rub=utilitario.consultar("select detatipono.ide_nrdtn as ide_label, " +
				"TIPOEMP.detalle_gttem ||' - ' || TIN.DETALLE_NRTIN as detalle_label, " +
				"dep.ide_gedep as ide_col_ejex," +
				"dep.detalle_gedep ||' - '||area.detalle_geare as nom_col_ejex, " +
				"area.detalle_geare, "+
				"SUM(VALOR_NRDRO) as MONTO " +
				"FROM NRH_DETALLE_ROL DETA " +
				"LEFT JOIN NRH_ROL ROL ON ROL.IDE_NRROL=DETA.IDE_NRROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DETATIPONO ON ROL.IDE_NRDTN = DETATIPONO.IDE_NRDTN " +
				"INNER JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DETATIPONO.IDE_NRTIN "+
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR PAR ON PAR.IDE_GEEDP=DETA.IDE_GEEDP " +
				"LEFT JOIN GTH_TIPO_CONTRATO CONTRATO ON CONTRATO.IDE_GTTCO=PAR.IDE_GTTCO " +
				"INNER JOIN GTH_TIPO_EMPLEADO TIPOEMP ON TIPOEMP.IDE_GTTEM=PAR.IDE_GTTEM " +
				"AND TIPOEMP.IDE_GTTEM=DETATIPONO.IDE_GTTEM "+
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=PAR.IDE_SUCU " +
				"LEFT JOIN NRH_DETALLE_RUBRO DERUBRO ON DERUBRO.IDE_NRDER=DETA.IDE_NRDER " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=PAR.IDE_GEDEP " +
				"LEFT JOIN GEN_GRUPO_OCUPACIONAL OCUPACIONAL ON OCUPACIONAL.IDE_GEGRO=PAR.IDE_GEGRO " +
				"LEFT JOIN GEN_CARGO_FUNCIONAL FUNCIONAL ON FUNCIONAL.IDE_GECAF=PAR.IDE_GECAF " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=PAR.IDE_GTEMP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=PAR.IDE_GEARE " +
				"LEFT JOIN NRH_RUBRO RUB ON DERUBRO.IDE_NRRUB=RUB.IDE_NRRUB " +
				"LEFT JOIN NRH_TIPO_RUBRO TIPORUBRO ON TIPORUBRO.IDE_NRTIR=RUB.IDE_NRTIR "+
				"LEFT JOIN GEN_PERIDO_ROL PERIODO ON PERIODO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES " +
				"LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI " +
				"WHERE detatipono.ide_nrdtn IN ("+ide_nrdtn+") " +
				"AND ROL.IDE_GEPRO IN ("+ide_gepro+") " +
				"AND RUB.IDE_NRRUB IN ("+ide_nrrub+") " +
				"GROUP BY area.ide_geare,area.detalle_geare,dep.ide_gedep,dep.detalle_gedep,detatipono.ide_nrdtn,TIN.DETALLE_NRTIN,TIPOEMP.detalle_gttem  " +
				"HAVING sum (DETA.VALOR_NRDRO)>0 " +
				"order by ide_label,detalle_geare,nom_col_ejeX");
		return tab_rub;
	}


	public void dibujarTabla(){


		Etiqueta eti = new Etiqueta("GR�FICOS ESTAD�STICOS");
		eti.setStyle("font-size:18px;");
		Etiqueta eti1 = new Etiqueta("PAR�METROS RUBROS");
		eti1.setStyle("font-size:10px; aling:left;");
		grid_grafico.setHeader(eti);
		grid_grafico.setId("grid_grafico");

		div_division.getChildren().clear();

		div_division.dividir1(grid_grafico);

	}


	@Override
	public void insertar() {
		//		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		guardarPantalla();
	}

	public void itemSelect(ItemSelectEvent event) {  
		categoryModel=grafico.getCategoryModel(); 

		Map map= categoryModel.getSeries().get(0).getData();
		Iterator it = map.entrySet().iterator();
		List lis_nom_col_eje_x=new ArrayList();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			lis_nom_col_eje_x.add(e.getKey()+"");
		}	     


		String nomina=categoryModel.getSeries().get(event.getSeriesIndex()).getLabel();	     
		map= categoryModel.getSeries().get(event.getSeriesIndex()).getData();

		System.out.println("label "+nomina+" map "+map);
		int int_num_dep=event.getItemIndex();
		String str_nom_col_eje_x="";
		for (int i = 0; i < lis_nom_col_eje_x.size(); i++) {
			if (i==int_num_dep){
				str_nom_col_eje_x=lis_nom_col_eje_x.get(i)+"";
				break;
			}
		}

		it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			if (e.getKey().toString().equalsIgnoreCase(str_nom_col_eje_x)){
				utilitario.agregarMensaje("Item seleccionado: "+nomina, e.getKey() + " " +	e.getValue());
			}
		}	     

	}

	@Override
	public void eliminar() {
	}


	private ValueExpression crearValueExpression(String valueExpression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), "#{" + valueExpression + "}", Object.class);
	}


	private ValueExpression crearValueExpression(String valueExpression,Class c) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), "#{" + valueExpression + "}", c);
	}

	public Tabla getTab_datos() {
		return tab_datos;
	}

	public void setTab_datos(Tabla tab_datos) {
		this.tab_datos = tab_datos;
	}


	public BarChart getBarchart() {
		return barchart;
	}


	public void setBarchart(BarChart barchart) {
		this.barchart = barchart;
	}


	public PieChart getPiechart() {
		return piechart;
	}


	public void setPiechart(PieChart piechart) {
		this.piechart = piechart;
	}


	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}


	public void setCategoryModel(CartesianChartModel categoryModel) {
		this.categoryModel = categoryModel;
	}


	public PieChartModel getPieModel() {
		return pieModel;
	}


	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}
	public SeleccionCalendario getSec_rango_fechas() {
		return sec_rango_fechas;
	}
	public void setSec_rango_fechas(SeleccionCalendario sec_rango_fechas) {
		this.sec_rango_fechas = sec_rango_fechas;
	}
	public Dialogo getDia_tipo_rep() {
		return dia_tipo_rep;
	}
	public void setDia_tipo_rep(Dialogo dia_tipo_rep) {
		this.dia_tipo_rep = dia_tipo_rep;
	}
	public TablaGenerica getTab_rep_pantalla() {
		return tab_rep_pantalla;
	}
	public void setTab_rep_pantalla(TablaGenerica tab_rep_pantalla) {
		this.tab_rep_pantalla = tab_rep_pantalla;
	}

	public SeleccionTabla getSet_rubros() {
		return set_rubros;
	}

	public void setSet_rubros(SeleccionTabla set_rubros) {
		this.set_rubros = set_rubros;
	}




}


