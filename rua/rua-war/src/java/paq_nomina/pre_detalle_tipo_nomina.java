package paq_nomina;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import javax.activation.DataSource;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.SelectEvent;

import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
import framework.reportes.ReporteDataSource;

import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;

 
public class pre_detalle_tipo_nomina extends Pantalla {

	private Tabla tab_tabla = new Tabla();
	private Tabla tab_tabla1 = new Tabla();    
	private Dialogo dia_formula = new Dialogo();
	private Etiqueta eti_formula = new Etiqueta();
	private Etiqueta eti_mensaje = new Etiqueta();
	@EJB 
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	private boolean boo_iniciar_copiar=false;
	private List<Fila> lis_copiar;
	private int int_inicio_copiar=-1;
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();

	private SeleccionTabla set_det_tip_nom =new SeleccionTabla();
	private SeleccionTabla set_periodo_rol =new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();

	private Combo com_tipo_nomina=new Combo();
	
	public pre_detalle_tipo_nomina(){

		bar_botones.agregarReporte();

		com_tipo_nomina.setCombo("select ide_nrtin,detalle_nrtin from nrh_tipo_nomina order by detalle_nrtin");
		com_tipo_nomina.setMetodo("seleccionaTipoNomina");
		bar_botones.agregarComponente(new Etiqueta("Tipo de Nomina:"));
		bar_botones.agregarComponente(com_tipo_nomina);
		
		Boton bot_validar=new Boton();
		bot_validar.setMetodo("validarFormulas");
		bot_validar.setValue("Validar Formulas");
		bar_botones.agregarBoton(bot_validar);

		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("NRH_DETALLE_TIPO_NOMINA", "IDE_NRDTN", 1);
		tab_tabla.getColumna("IDE_NRTIN").setCombo("NRH_TIPO_NOMINA","IDE_NRTIN", "DETALLE_NRTIN", "");
		tab_tabla.getColumna("IDE_NRTIN").setRequerida(true);
		tab_tabla.getColumna("IDE_GTTEM").setCombo("GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_tabla.getColumna("IDE_GTTEM").setRequerida(true);

		tab_tabla.getColumna("IDE_GTTCO").setCombo("GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO", "");
		tab_tabla.getColumna("IDE_GTTCO").setVisible(false);
		//		tab_tabla.getColumna("IDE_GTTCO").setRequerida(true);
		tab_tabla.getColumna("IDE_NRTIT").setCombo("NRH_TIPO_ROL", "IDE_NRTIT", "DETALLE_NRTIT", "");
		tab_tabla.getColumna("IDE_NRTIT").setRequerida(true);
		//		tab_tabla.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
		//		tab_tabla.getColumna("IDE_SUCU").setRequerida(true);
		//		tab_tabla.setMostrarcampoSucursal(true);
		tab_tabla.getColumna("ACTIVO_NRDTN").setCheck();
		tab_tabla.getColumna("ACTIVO_NRDTN").setValorDefecto("true");
		tab_tabla.getColumna("IDE_NRTIN").setVisible(false);
		tab_tabla.setValidarInsertar(true);
		tab_tabla.agregarRelacion(tab_tabla1);
		tab_tabla.setCondicion("ide_nrdtn=-1");

		tab_tabla.dibujar();


		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("NRH_DETALLE_RUBRO", "IDE_NRDER", 2);
		tab_tabla1.getColumna("IDE_NRRUB").setCombo("select RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,FCA.DETALLE_NRFOC,TRU.DETALLE_NRTIR from NRH_RUBRO RUB  " +
				"LEFT JOIN NRH_FORMA_CALCULO FCA ON FCA.IDE_NRFOC=RUB.IDE_NRFOC " +
				"LEFT JOIN NRH_TIPO_RUBRO TRU ON TRU.IDE_NRTIR=RUB.IDE_NRTIR") ;
		tab_tabla1.getColumna("IDE_GEREG").setCombo("GEN_REGION", "IDE_GEREG", "DETALLE_GEREG", "");
		tab_tabla1.getColumna("IDE_NRRUB").setAutoCompletar();
		tab_tabla1.getColumna("IDE_NRRUB").setFiltro(true);
		tab_tabla1.getColumna("IDE_NRRUB").setUnico(true);
		tab_tabla1.getColumna("IDE_NRDTN").setUnico(true);
		tab_tabla1.getColumna("FORMULA_NRDER").setUnico(true);
		tab_tabla1.getColumna("IDE_NRRUB").setRequerida(true);
		tab_tabla1.getColumna("ORDEN_NRDER").setRequerida(false);
		tab_tabla1.getColumna("ACTIVO_NRDER").setCheck();
		tab_tabla1.getColumna("ACTIVO_NRDER").setValorDefecto("true");
		tab_tabla1.getColumna("IMPRIME_NRDER").setCheck();
		tab_tabla1.getColumna("IMPRIME_NRDER").setValorDefecto("true");
		tab_tabla1.onSelect("seleccionarTabla1");
		tab_tabla1.setCampoOrden("ORDEN_NRDER ASC,ORDEN_IMPRIME_NRDER ASC");
		tab_tabla1.dibujar();

		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);


		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("RUBROS");
		pat_panel1.setPanelTabla(tab_tabla1);

		ItemMenu itm_importar_rubro =new ItemMenu();
		itm_importar_rubro.setValue("Importar");
		itm_importar_rubro.setIcon("ui-icon-arrowreturnthick-1-n");
		itm_importar_rubro.setMetodo("importarRubro");
		pat_panel1.getMenuTabla().getChildren().add(itm_importar_rubro);



		ItemMenu itm_copiar =new ItemMenu();
		itm_copiar.setValue("Copiar");
		itm_copiar.setIcon("ui-icon-copy");
		itm_copiar.setMetodo("copiar");
		pat_panel1.getMenuTabla().getChildren().add(itm_copiar);

		ItemMenu itm_pegar =new ItemMenu();
		itm_pegar.setValue("Pegar");
		itm_pegar.setIcon("ui-icon-clipboard");
		itm_pegar.setMetodo("pegar");
		pat_panel1.getMenuTabla().getChildren().add(itm_pegar);


		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel,pat_panel1 , "40%", "H");

		agregarComponente(div_division);

		Boton bot_ver_formula = new Boton();

		bot_ver_formula.setValue("Ver Formula");
		bot_ver_formula.setMetodo("verFormula");
		bar_botones.agregarBoton(bot_ver_formula);

		//Dialogo para ver la Formula
		dia_formula.setId("dia_formula");
		dia_formula.setTitle("Visualizador de Formula");
		dia_formula.setWidth("40%");
		dia_formula.setHeight("25%");
		dia_formula.setModal(false);
		Grupo gru_cuerpo = new Grupo();
		eti_mensaje.setValue("Formula del Rubro: ");
		eti_mensaje.setStyle("font-size: 14px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
		eti_formula.setStyle("font-size: 15px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
		Grid gri_clave = new Grid();
		gri_clave.setWidth("100%");
		gri_clave.setStyle("text-align: center;");
		gri_clave.getChildren().add(eti_formula);
		gru_cuerpo.getChildren().add(eti_mensaje);
		gru_cuerpo.getChildren().add(gri_clave);
		dia_formula.getBot_aceptar().setMetodo("aceptarFormula");
		dia_formula.setDialogo(gru_cuerpo);
		agregarComponente(dia_formula);

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");

		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		set_det_tip_nom.setId("set_det_tip_nom");
		set_det_tip_nom.setSeleccionTabla("select  " +
				"DTN.IDE_NRDTN," +
				"TIN.DETALLE_NRTIN, " +
				"TEM.DETALLE_GTTEM, " +
				"SUC.NOM_SUCU " +
				"from NRH_DETALLE_TIPO_NOMINA DTN " +
				"left join NRH_TIPO_NOMINA tin on TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " ,"IDE_NRDTN");
		set_det_tip_nom.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_det_tip_nom);

		set_periodo_rol.setId("set_periodo_rol");
		set_periodo_rol.setSeleccionTabla("select IDE_NRROL, " +
				"DETALLE_GTTEM ||' '|| nombre_gemes || '  ' || nom_GEANI || ' ' || TIT.DETALLE_NRTIT AS PERIODO_NOMINA " +
				"from nrh_ROL rol " +
				"inner join GEN_PERIDO_ROL PRO on PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"inner join NRH_TIPO_ROL TIT ON TIT.IDE_NRTIT=PRO.IDE_NRTIT "+
				"inner join GEN_MES MES ON MES.IDE_GEMES=PRO.IDE_GEMES " +
				"inner join GEN_ANIO ANI ON ANI.IDE_GEANI=PRO.IDE_GEANI " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"INNER JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"INNER JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"WHERE ACTIVO_GEPRO=TRUE and dtn.ide_nrdtn=-1 " +
				"AND IDE_NRESR="+utilitario.getVariable("p_nrh_estado_rol_pre_nomina"),"IDE_NRROL"); 
		set_periodo_rol.getBot_aceptar().setMetodo("aceptarImportarRubro");
		set_periodo_rol.setRadio();
		set_periodo_rol.setDynamic(false);
		agregarComponente(set_periodo_rol);


		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
	}

	public void seleccionaTipoNomina(){
		if (com_tipo_nomina.getValue()!=null){
			tab_tabla.setCondicion("ide_nrtin="+com_tipo_nomina.getValue());
			tab_tabla.ejecutarSql();
			tab_tabla1.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
		}else{
			tab_tabla.setCondicion("ide_nrtin=-1");
			tab_tabla.ejecutarSql();
			tab_tabla1.ejecutarValorForanea(tab_tabla.getValorSeleccionado());
		}
	}



	public void actualizarNomina(String ide_nrdtn_origen,String ide_nrdtn_destino){
		TablaGenerica tab_det_rub_origen=utilitario.consultar("select * from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn_origen+" and ACTIVO_NRDER=TRUE");
		System.out.println("sql tab_det_rub_origen...  "+utilitario.consultar("select * from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn_origen+" and ACTIVO_NRDER=TRUE"));
		
		TablaGenerica tab_det_rub_destino=utilitario.consultar("select * from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn_destino);
		System.out.println("sql tab_det_rub_destino...  "+utilitario.consultar("select * from NRH_DETALLE_RUBRO where IDE_NRDTN="+ide_nrdtn_destino));
		int band=0;
		for (int i = 0; i < tab_det_rub_origen.getTotalFilas(); i++) {
			String ide_nrrub_origen=tab_det_rub_origen.getValor(i, "IDE_NRRUB");
			band=0;
			for (int j = 0; j < tab_det_rub_destino.getTotalFilas(); j++) {
				if (ide_nrrub_origen.equalsIgnoreCase(tab_det_rub_destino.getValor(j, "IDE_NRRUB"))){
					utilitario.getConexion().ejecutarSql("update NRH_DETALLE_RUBRO " +
							"set FORMULA_NRDER='"+tab_det_rub_origen.getValor(i, "FORMULA_NRDER")+"',  " +
							"ACTIVO_NRDER="+tab_det_rub_origen.getValor(i, "ACTIVO_NRDER")+", "+
							"ORDEN_NRDER="+tab_det_rub_origen.getValor(i, "ORDEN_NRDER")+", "+
							"ORDEN_IMPRIME_NRDER="+tab_det_rub_origen.getValor(i, "ORDEN_IMPRIME_NRDER")+", "+
							"IMPRIME_NRDER="+tab_det_rub_origen.getValor(i, "IMPRIME_NRDER")+", "+
							"FECHA_INICIAL_NRDER='"+tab_det_rub_origen.getValor(i, "FECHA_INICIAL_NRDER")+"', "+
							"FECHA_FINAL_NRDER='"+tab_det_rub_origen.getValor(i, "FECHA_FINAL_NRDER")+"', "+
							"FECHA_PAGO_NRDER='"+tab_det_rub_origen.getValor(i, "FECHA_PAGO_NRDER")+"' "+
							"where IDE_NRDTN="+ide_nrdtn_destino+" and IDE_NRRUB="+ide_nrrub_origen);
					band=1;
					break;
				}
			}
			if (band==0){
				tab_tabla1.insertar();
				tab_tabla1.setValor("IDE_NRDTN", ide_nrdtn_destino);
				tab_tabla1.setValor("IDE_NRRUB", ide_nrrub_origen);
				tab_tabla1.setValor("FORMULA_NRDER", tab_det_rub_origen.getValor(i, "FORMULA_NRDER"));
				tab_tabla1.setValor("ACTIVO_NRDER", tab_det_rub_origen.getValor(i, "ACTIVO_NRDER"));
				tab_tabla1.setValor("ORDEN_NRDER", tab_det_rub_origen.getValor(i, "ORDEN_NRDER"));
				tab_tabla1.setValor("ORDEN_IMPRIME_NRDER", tab_det_rub_origen.getValor(i, "ORDEN_IMPRIME_NRDER"));
				tab_tabla1.setValor("IMPRIME_NRDER", tab_det_rub_origen.getValor(i, "IMPRIME_NRDER"));
				tab_tabla1.setValor("FECHA_INICIAL_NRDER", tab_det_rub_origen.getValor(i, "FECHA_INICIAL_NRDER"));
				tab_tabla1.setValor("FECHA_FINAL_NRDER", tab_det_rub_origen.getValor(i, "FECHA_FINAL_NRDER"));
				tab_tabla1.setValor("FECHA_PAGO_NRDER", tab_det_rub_origen.getValor(i, "FECHA_PAGO_NRDER"));

			}

		}
		tab_tabla1.guardar();
		utilitario.getConexion().guardarPantalla();
	}

	String str_ide_nrrol="";
	public void aceptarImportarRubro(){

		if (set_periodo_rol.getValorSeleccionado()==null || set_periodo_rol.getValorSeleccionado().isEmpty()){
			utilitario.agregarMensajeInfo("No se puede importar el rubro", "Debe seleccionar un periodo de nomina");
			return;
		}

		if (utilitario.consultar("select * from NRH_DETALLE_ROL where IDE_NRROL="+set_periodo_rol.getValorSeleccionado()+" and IDE_NRDER="+tab_tabla1.getValor(tab_tabla1.getFilaActual(), "IDE_NRDER")).getTotalFilas()==0){

			if (!con_guardar.isVisible()){
				String sql="select * from ( ";
				sql+=set_periodo_rol.getTab_seleccion().getSql();
				sql+=") a "+
						"where a.ide_nrrol="+set_periodo_rol.getValorSeleccionado();
				str_ide_nrrol=set_periodo_rol.getValorSeleccionado();
				TablaGenerica tab_rol=utilitario.consultar(sql);
				con_guardar.setHeader("CONFIRMACION IMPORTAR RUBRO A ROL");
				con_guardar.setMessage("Esta seguro de importar el rubro "+tab_tabla1.getValorArreglo("IDE_NRRUB",1) +" al rol "+tab_rol.getValor("PERIODO_NOMINA"));
				con_guardar.getBot_aceptar().setMetodo("aceptarImportarRubro");
				con_guardar.dibujar();
				set_periodo_rol.cerrar();
				utilitario.addUpdate("con_guardar");
			}else{
				con_guardar.cerrar();


				utilitario.getConexion().ejecutarSql("update NRH_DETALLE_RUBRO set ACTIVO_NRDER=TRUE where IDE_NRDER="+tab_tabla1.getValorSeleccionado());
				utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");



				String ide_gepro=ser_nomina.getRol(str_ide_nrrol).getValor("IDE_GEPRO");

				String fecha_final_gepro=ser_nomina.getPeriodoRol(ide_gepro).getValor("FECHA_FINAL_GEPRO");

				Tabla tab_det_rol=new Tabla();
				tab_det_rol.setTabla("NRH_DETALLE_ROL", "IDE_NRDRO", -1);
				tab_det_rol.setCondicion("IDE_NRDRO=-1");
				tab_det_rol.ejecutarSql();

				String str_ide_nrfoc=ser_nomina.getRubro(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "IDE_NRRUB")).getValor("IDE_NRFOC");


				String ide_nrtin=tab_tabla.getValor("IDE_NRTIN");
				if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
					utilitario.getConexion().ejecutarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set EJECUTO_LIQUIDACION_GEEDP=0 where IDE_GEEDP IN (select IDE_GEEDP from NRH_DETALLE_ROL WHERE IDE_NRROL="+str_ide_nrrol+")");
				}

				TablaGenerica tab_empleados_departamento=utilitario.consultar(ser_nomina.getSqlEmpleadosTipoNomina(tab_tabla.getValorSeleccionado(),fecha_final_gepro));

				for (int i = 0; i < tab_empleados_departamento.getTotalFilas(); i++) {

					tab_det_rol.insertar();
					tab_det_rol.setValor("IDE_NRROL",str_ide_nrrol);
					tab_det_rol.setValor("IDE_GEEDP",tab_empleados_departamento.getValor(i, "IDE_GEEDP"));
					tab_det_rol.setValor("IDE_NRDER",tab_tabla1.getValor(tab_tabla1.getFilaActual(), "IDE_NRDER"));
					if (!str_ide_nrfoc.equalsIgnoreCase(utilitario.getVariable("p_nrh_forma_calculo_formula"))){
						if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "FORMULA_NRDER")!=null && !tab_tabla1.getValor(tab_tabla1.getFilaActual(), "FORMULA_NRDER").isEmpty()){
							tab_det_rol.setValor("VALOR_NRDRO",tab_tabla1.getValor(tab_tabla1.getFilaActual(), "FORMULA_NRDER"));
						}else {
							tab_det_rol.setValor("VALOR_NRDRO",0.00+"");
						}
					}else {
						tab_det_rol.setValor("VALOR_NRDRO",0.00+"");
					}
				}
				tab_det_rol.guardar();
				TablaGenerica tab_per_rol=ser_nomina.getPeriodoRol(ide_gepro);
				String fecha_ini_gepro=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
				String fecha_fin_gepro=tab_per_rol.getValor("FECHA_FINAL_GEPRO");

				utilitario.getConexion().ejecutarSql("update  NRH_AMORTIZACION set ACTIVO_NRAMO=0 " +
						"where FECHA_VENCIMIENTO_NRAMO " +
						"BETWEEN to_date ('"+fecha_ini_gepro+"','yy-mm-dd') and to_date ('"+fecha_fin_gepro+"','yy-mm-dd') " +
						"and IDE_NRANI in (select ide_nrani from NRH_ANTICIPO_INTERES " +
						"where IDE_NRANT in (select ide_nrant from NRH_ANTICIPO " +
						"where IDE_GTEMP in (select emp.ide_gtemp from GEN_EMPLEADOS_DEPARTAMENTO_PAR edp " +
						"inner join NRH_DETALLE_TIPO_NOMINA dtn on EDP.IDE_GTTEM=DTN.IDE_GTTEM " +
						"and EDP.IDE_SUCU=DTN.IDE_SUCU " +
						"inner join GTH_EMPLEADO emp on EMP.ide_gtemp=EDP.IDE_GTEMP " +
						"WHERE DTN.IDE_NRDTN IN ("+tab_tabla.getValorSeleccionado()+"))))");

				guardarPantalla();
				String ide_nrtit=ser_nomina.getDetalleTipoNomina(tab_tabla.getValorSeleccionado()).getValor("IDE_NRTIT");
				
				if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos"))){
					ser_nomina.reCalcularNomina(tab_tabla.getValorSeleccionado(), ide_gepro,ide_nrtit,true);			
					set_periodo_rol.cerrar();
				}else{
					ser_nomina.reCalcularNomina(tab_tabla.getValorSeleccionado(), ide_gepro,ide_nrtit,false);			
					set_periodo_rol.cerrar();

				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede importar", "El rubro "+tab_tabla1.getValorArreglo("IDE_NRRUB", 1)+" ya se encuentra en la nomina seleccionada");
		}

	}

	public void importarRubro(){
		set_periodo_rol.setTitle("Seleccione el Periodo de la Nomina a Importar");
		set_periodo_rol.getTab_seleccion().setSql("select IDE_NRROL, " +
				"DETALLE_GTTEM ||' '|| nombre_gemes || '  ' || nom_geani || ' ' || TIT.DETALLE_NRTIT AS PERIODO_NOMINA " +
				"from nrh_ROL rol " +
				"inner join GEN_PERIDO_ROL PRO on PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"inner join NRH_TIPO_ROL TIT ON TIT.IDE_NRTIT=PRO.IDE_NRTIT "+
				"inner join GEN_MES MES ON MES.IDE_GEMES=PRO.IDE_GEMES " +
				"inner join GEN_ANIO ANI ON ANI.IDE_GEANI=PRO.IDE_GEANI " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"INNER JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"INNER JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"WHERE ACTIVO_GEPRO=TRUE and dtn.ide_nrdtn="+tab_tabla.getValorSeleccionado()+" " +
				"AND IDE_NRESR="+utilitario.getVariable("p_nrh_estado_rol_pre_nomina"));
		set_periodo_rol.getTab_seleccion().ejecutarSql();
		System.out.println("sql importar rubro a nomina "+set_periodo_rol.getTab_seleccion().getSql());
		if (set_periodo_rol.getTab_seleccion().getTotalFilas()>0){
			str_ide_nrrol="";
			set_periodo_rol.getBot_aceptar().setMetodo("aceptarImportarRubro");
			set_periodo_rol.dibujar();
		}else{
			utilitario.agregarMensajeInfo("No se puede importar ","No existe nominas de tipo "+tab_tabla.getValorArreglo("IDE_GTTEM", 1)+" con estado pre-nomina a las cuales agregar el rubro seleccionado ");
		}
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}


	Map p_parametros= new HashMap();

	@Override
	public void aceptarReporte() {
		
		if (rep_reporte.getReporteSelecionado().equalsIgnoreCase("Rubros Tipo Nomina")) {
					
			if (rep_reporte.isVisible()) {
			
				p_parametros= new HashMap();
				rep_reporte.cerrar();
				set_det_tip_nom.setTitle("SELECCION DETALLE TIPO NOMINA");
				set_det_tip_nom.setRadio();
				set_det_tip_nom.dibujar();

			}else if(set_det_tip_nom.isVisible()) {
							
//				if (set_det_tip_nom.getSeleccionados()!=null && !set_det_tip_nom.getSeleccionados().isEmpty()){
					if (set_det_tip_nom.getValorSeleccionado() !=null && !set_det_tip_nom.getValorSeleccionado().isEmpty()){
					System.out.println("entro a consultar la snominas comoparametros \n");
					p_parametros.put("TIPO_NOMINA",ser_nomina.getTipoNomina(ser_nomina.getDetalleTipoNomina(set_det_tip_nom.getValorSeleccionado()).getValor("ide_nrtin")).getValor("detalle_nrtin"));
					System.out.println("RepPara TIPO_NOMINA...  "+ser_nomina.getTipoNomina(ser_nomina.getDetalleTipoNomina(set_det_tip_nom.getValorSeleccionado()).getValor("ide_nrtin")).getValor("DETALLE_NRTIN"));
					p_parametros.put("TIPO_EMPLEADO",ser_nomina.getTipoEmpleado(ser_nomina.getDetalleTipoNomina(set_det_tip_nom.getValorSeleccionado()).getValor("IDE_GTTEM")).getValor("DETALLE_GTTEM"));
					System.out.println("RepPara TIPO_EMPLEADO...  "+ser_nomina.getTipoEmpleado(ser_nomina.getDetalleTipoNomina(set_det_tip_nom.getValorSeleccionado()).getValor("IDE_GTTEM")).getValor("DETALLE_GTTEM"));
					String sucursal="";
					try {
						System.out.println("Ingreso al try  ...  ");
						
						sucursal=ser_gestion.getSucursal(ser_nomina.getDetalleTipoNomina(set_det_tip_nom.getValorSeleccionado()).getValor("IDE_SUCU")).getValor("nom_sucu");
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Ingreso al Reporte catch...  ");
						
					}
					p_parametros.put("SUCURSAL",sucursal);
					System.out.println("RepPara sucursal...  "+sucursal);
					TablaGenerica tab_emp=utilitario.consultar("select * from SIS_EMPRESA");
					System.out.println("RepParaSql tab_emp...  "+tab_emp.getSql());
					
					TablaGenerica tab_usua=utilitario.consultar("select * from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("ide_usua"));
					System.out.println("RepParaSql tab_usua...  "+tab_usua.getSql());
					
					p_parametros.put("titulo", "RUBROS TIPO DE NOMINA");
					System.out.println("RepPara titulo...  "+"RUBROS TIPO DE NOMINA");
					
					p_parametros.put("nick", tab_usua.getValor("nick_usua"));
					System.out.println("RepPara nick...  "+tab_usua.getValor("nick_usua"));
					
					p_parametros.put("direccion", tab_emp.getValor("DIRECCION_EMPR"));
					System.out.println("RepPara direccion...  "+tab_emp.getValor("DIRECCION_EMPR"));
					
					p_parametros.put("telefono", tab_emp.getValor("TELEFONO_EMPR"));
					System.out.println("RepPara telefono...  "+tab_emp.getValor("TELEFONO_EMPR"));
					
					p_parametros.put("dir_logo", tab_emp.getValor("LOGO_EMPR"));
					System.out.println("RepPara dir_logo...  "+tab_emp.getValor("LOGO_EMPR"));

					
					ReporteDataSource rep = new ReporteDataSource(getTablaRubroTipoNomina( set_det_tip_nom.getValorSeleccionado()));
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath(), rep);

					set_det_tip_nom.cerrar();
					sef_reporte.dibujar();
				}else{
					System.out.println("Ingreso al Reporte dentro del else 3 if ...  ");
					
					utilitario.agregarMensajeInfo("No ha seleccionado Tipo de nomina", "");
				}
			}
		}


	}



	public void seleccionarTabla1(AjaxBehaviorEvent evt){
		tab_tabla1.seleccionarFila(evt);	
		fin_copiar();
	}

	public void seleccionarTabla1(SelectEvent evt){
		tab_tabla1.seleccionarFila(evt);	
		fin_copiar();
	}

	private void fin_copiar(){
		if(boo_iniciar_copiar==true){
			lis_copiar=new ArrayList<Fila>();
			int int_cuenta=0;			
			if(int_inicio_copiar <=tab_tabla1.getFilaActual()){
				for(int i=int_inicio_copiar;i<=tab_tabla1.getFilaActual();i++){
					int_cuenta++;
					lis_copiar.add(tab_tabla1.getFilas().get(i));
				}
			}
			else{
				for(int i=int_inicio_copiar;i>=tab_tabla1.getFilaActual();i--){
					int_cuenta++;
					lis_copiar.add(tab_tabla1.getFilas().get(i));
				}
			}			
			boo_iniciar_copiar=false;			
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


	public TablaGenerica getTablaRubroTipoNomina(String ide_nrdtn){
		TablaGenerica tab_rub_tip_nomina=utilitario.consultar("SELECT IDE_NRDER , " +
				"TIN.DETALLE_NRTIN AS TIPO_NOMINA,  " +
				"TEM.DETALLE_GTTEM AS TIPO_EMPLEADO, " +
				//"TCO.DETALLE_GTTCO AS TIPO_CONTRATO,  " +
				"SUC.NOM_SUCU AS SUCURSAL,  " +
				"RUB.DETALLE_NRRUB AS RUBRO,  " +
				"TRUB.DETALLE_NRTIR AS TIPO_RUBRO,  " +
				"FCA.DETALLE_NRFOC  AS FORMA_CALCULO,  " +
				"DER.FORMULA_NRDER  AS FORMULA, " +
				"DER.FECHA_INICIAL_NRDER AS FECHA_INICIAL, " +
				"DER.FECHA_FINAL_NRDER AS FECHA_FINAL, " +
				"DER.ACTIVO_NRDER AS ACTIVO, " +
				"DER.ORDEN_NRDER  AS ORDEN, " +
				"'' AS FORMULA_VISUAL " +
				"from NRH_DETALLE_RUBRO DER  " +
				"left join NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB  " +
				"left join NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=DER.IDE_NRDTN " +
				"left join NRH_TIPO_NOMINA TIN ON DTN.IDE_NRTIN=TIN.IDE_NRTIN  " +
				"left join GTH_TIPO_EMPLEADO TEM ON DTN.IDE_GTTEM=TEM.IDE_GTTEM " +
				"left join GTH_TIPO_CONTRATO TCO ON DTN.IDE_GTTCO=TCO.IDE_GTTCO  " +
				"left join SIS_SUCURSAL SUC ON DTN.IDE_SUCU=SUC.IDE_SUCU  " +
				"left join NRH_TIPO_RUBRO TRUB ON TRUB.IDE_NRTIR=RUB.IDE_NRTIR  " +
				"left join NRH_FORMA_CALCULO FCA ON FCA.IDE_NRFOC=RUB.IDE_NRFOC " +
				"where DER.ide_nrdtn IN ("+ide_nrdtn+") and ACTIVO_NRDER=TRUE  " +
				"ORDER BY DER.IDE_NRDTN ASC ,DER.ORDEN_NRDER ASC");

		System.out.println("rep tip nom...  "+tab_rub_tip_nomina.getSql());
		for (int i = 0; i < tab_rub_tip_nomina.getTotalFilas(); i++) {
			if(tab_rub_tip_nomina.getValor(i, "FORMULA")!=null && !tab_rub_tip_nomina.getValor(i, "FORMULA").isEmpty()){
				if(tab_rub_tip_nomina.getValor(i, "FORMULA").startsWith("=")){
					String form_visual=ser_nomina.getFormulaEnLetras(0,"","","",tab_rub_tip_nomina.getValor(i,"FORMULA"),false);
					tab_rub_tip_nomina.setValor(i, "FORMULA_VISUAL", form_visual);
				}else{
					tab_rub_tip_nomina.setValor(i, "FORMULA_VISUAL", tab_rub_tip_nomina.getValor(i,"FORMULA"));
				}
			}else{
				tab_rub_tip_nomina.setValor(i, "FORMULA_VISUAL", tab_rub_tip_nomina.getValor(i,"FORMULA"));
			}
		}

		return tab_rub_tip_nomina;
	}


	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		boo_iniciar_copiar=false;
		lis_copiar=null;
		int_inicio_copiar=-1;
	}

	public void copiar() {
		if(boo_iniciar_copiar==false){			
			boo_iniciar_copiar=true;
			int_inicio_copiar=tab_tabla1.getFilaActual();
			utilitario.agregarMensaje("Seleccione hasta que fila desea copiar los rubros", "");
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar una fila de la tabla de rubros", "");
		}
	}

	public void pegar() {
		if(lis_copiar!=null){
			try {
				tab_tabla1.setDibujo(false);				
				for(int i=0;i<lis_copiar.size();i++){
					tab_tabla1.insertar();						
					//////
					for(int j=0;j<tab_tabla1.getTotalColumnas();j++){
						if(tab_tabla1.getColumnas()[j].getNombre().equalsIgnoreCase(tab_tabla1.getCampoPrimaria())){
							continue;
						}
						if(tab_tabla1.getColumnas()[j].getNombre().equalsIgnoreCase(tab_tabla.getCampoPrimaria())){
							continue;
						}
						if(tab_tabla1.getColumnas()[j].getValorDefecto()!=null){
							continue;
						}
						if(tab_tabla1.getColumnas()[j].isVisible()){						
							if(lis_copiar.get(i).getCampos()[tab_tabla1.getNumeroColumna(tab_tabla1.getColumnas()[j].getNombre())]!=null){
								if(tab_tabla1.getColumnas()[j].getControl().equalsIgnoreCase("Autocompletar")||tab_tabla1.getColumnas()[j].getControl().equalsIgnoreCase("Combo")){
									tab_tabla1.setValor(tab_tabla1.getColumnas()[j].getNombre(),((Object[]) lis_copiar.get(i).getCampos()[tab_tabla1.getNumeroColumna(tab_tabla1.getColumnas()[j].getNombre())])[0]+"");	
								}
								else{
									tab_tabla1.setValor(tab_tabla1.getColumnas()[j].getNombre(), lis_copiar.get(i).getCampos()[tab_tabla1.getNumeroColumna(tab_tabla1.getColumnas()[j].getNombre())]+"");
								}
							}						
						}
					}
				}	
				tab_tabla1.setDibujo(true);
				utilitario.addUpdate("tab_tabla1");
			} catch (Exception e) {				
				// TODO: handle exception				
			}			
		}
		else{
			utilitario.agregarMensajeInfo("Primero debe copiar rubros ", "");
		}
		boo_iniciar_copiar=false;
	}

	public void aceptarFormula() {
		dia_formula.cerrar();
	}

	public void verFormula() {
		String formula = tab_tabla1.getValor("formula_nrder");
		if (formula != null && !formula.isEmpty()) {
			if (formula.substring(0, 1).equalsIgnoreCase("=")) {
				eti_mensaje.setValue("Rubro: " + ser_nomina.getRubro(tab_tabla1.getValor("IDE_NRRUB")).getValor("DETALLE_NRRUB")+" =");
				System.out.println("FORMULA ENTRA "+formula+" formula resultado: "+ser_nomina.getFormulaEnLetras(0,"","","",formula,false));
				eti_formula.setValue(ser_nomina.getFormulaEnLetras(0,"","","",formula,false));
				dia_formula.dibujar();
			} else {
				utilitario.agregarMensajeInfo("Atencion", "El rubro seleccionado no tiene formula de calculo");
			}
		} else {
			utilitario.agregarMensajeInfo("Atencion", "El rubro seleccionado no tiene formula de calculo");
		}
	}

	@Override
	public void insertar() {
		
		if (com_tipo_nomina.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Tipo de Nomina");
			return;
		}
		
		if (tab_tabla.isFocus()){
			tab_tabla.insertar();
			tab_tabla.setValor("IDE_NRTIN", com_tipo_nomina.getValue()+"");
		}else{
		utilitario.getTablaisFocus().insertar();
		}


	}

	public boolean verificarFormulas(){
		
		for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
			if (tab_tabla1.isFilaInsertada(i) || tab_tabla1.isFilaModificada(i)){
				String ide_nrfoc="";
				try {
					ide_nrfoc=ser_nomina.getRubro(tab_tabla1.getValor(i,"ide_nrrub")).getValor("IDE_NRFOC");	
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (ide_nrfoc.equalsIgnoreCase(utilitario.getVariable("p_nrh_forma_calculo_formula"))){
					String formula=tab_tabla1.getValor(i, "FORMULA_NRDER");
					if (formula==null || formula.isEmpty()){
						utilitario.agregarMensajeInfo("No se puede guardar", "El campo formula nrder es obligatorio en rubros de tipo formula o constante ");
						return false;
					}
					if (!formula.startsWith("=")){
						utilitario.agregarMensajeInfo("No se puede guardar", "Uno de las formulas no empieza con '=', es obligatorio en rubros de tipo formula empezar con = al inicio de la formula ");
						return false;
					}

					if (formula.indexOf("%")!=-1){
						utilitario.agregarMensajeInfo("No se puede guardar", "No se permite el caracter especial '%' ");
						return false;
					}
					if (formula.indexOf("$")!=-1){
						utilitario.agregarMensajeInfo("No se puede guardar", "No se permite el caracter especial '$' ");
						return false;
					}
					if (formula.indexOf("#")!=-1){
						utilitario.agregarMensajeInfo("No se puede guardar", "No se permite el caracter especial '#' ");
						return false;
					}
					if (formula.indexOf("@")!=-1){
						utilitario.agregarMensajeInfo("No se puede guardar", "No se permite el caracter especial '@' ");
						return false;
					}
					if (formula.indexOf("!=")==-1){
						if (formula.indexOf("!")!=-1){
							utilitario.agregarMensajeInfo("No se puede guardar", "No se permite el caracter especial '!' ");
							return false;
						}
					}
					if (formula.indexOf("&")!=-1){
						utilitario.agregarMensajeInfo("No se puede guardar", "No se permite el caracter especial '&' ");
						return false;
					}

					String formula_aux=tab_tabla1.getValor(i, "FORMULA_NRDER").toLowerCase();

					String formula_aux2=formula_aux;
					for(int j=0;j<formula_aux.length();j++){
						if(formula_aux.charAt(j)=='['){
							formula_aux2=formula_aux2.replace("[","");
						}
						else if(formula.charAt(j)==']'){
							formula_aux2=formula_aux2.replace("]","");
						}
						else if(formula.charAt(j)=='('){
							formula_aux2=formula_aux2.replace("(","");
						}
						else if(formula.charAt(j)==')'){
							formula_aux2=formula_aux2.replace(")","");
						}
						else if(formula.charAt(j)=='{'){
							formula_aux2=formula_aux2.replace("{","");
						}
						else if(formula.charAt(j)=='}'){
							formula_aux2=formula_aux2.replace("}","");
						}

						else if(formula.charAt(j)=='0'){
							formula_aux2=formula_aux2.replace("0","");
						}
						else if(formula.charAt(j)=='1'){
							formula_aux2=formula_aux2.replace("1","");
						}
						else if(formula.charAt(j)=='2'){
							formula_aux2=formula_aux2.replace("2","");
						}
						else if(formula.charAt(j)=='3'){
							formula_aux2=formula_aux2.replace("3","");
						}
						else if(formula.charAt(j)=='4'){
							formula_aux2=formula_aux2.replace("4","");
						}
						else if(formula.charAt(j)=='5'){
							formula_aux2=formula_aux2.replace("5","");
						}
						else if(formula.charAt(j)=='6'){
							formula_aux2=formula_aux2.replace("6","");
						}
						else if(formula.charAt(j)=='7'){
							formula_aux2=formula_aux2.replace("7","");
						}
						else if(formula.charAt(j)=='8'){
							formula_aux2=formula_aux2.replace("8","");
						}
						else if(formula.charAt(j)=='9'){
							formula_aux2=formula_aux2.replace("9","");
						}
						else if(formula.charAt(j)=='='){
							formula_aux2=formula_aux2.replace("=","");
						}
						else if(formula.charAt(j)=='>'){
							formula_aux2=formula_aux2.replace(">","");
						}
						else if(formula.charAt(j)=='<'){
							formula_aux2=formula_aux2.replace("<","");
						}
						else if(formula.charAt(j)=='*'){
							formula_aux2=formula_aux2.replace("*","");
						}

						else if(formula.charAt(j)=='/'){
							formula_aux2=formula_aux2.replace("/","");
						}
						else if(formula.charAt(j)=='+'){
							formula_aux2=formula_aux2.replace("+","");
						}
						else if(formula.charAt(j)=='-'){
							formula_aux2=formula_aux2.replace("-","");
						}
						else if(formula.charAt(j)=='.'){
							formula_aux2=formula_aux2.replace(".","");
						}
						else if(formula.charAt(j)==';'){
							formula_aux2=formula_aux2.replace(";","");
						}

					}


					if (formula_aux2.indexOf("sum")!=-1){
						formula_aux2=formula_aux2.replaceAll("sum","");
					} 
					if (formula_aux2.indexOf("if")!=-1){
						formula_aux2=formula_aux2.replaceAll("if","");
					} 
					if (formula_aux2.indexOf("else")!=-1){
						formula_aux2=formula_aux2.replaceAll("else","");
					}

					formula_aux2=formula_aux2.trim();
					if (!formula_aux2.isEmpty()){
						utilitario.agregarMensajeInfo("No se puede guardar ", "Error en la formula del ide: "+tab_tabla1.getValor(i, "IDE_NRDER")+", Solo se admite las palabras reservadas if, else, sum");
						return false;
					}else{

			
						String formula_reemplazada_con_1=ser_nomina.getFormulaEnLetras(0,"","","",formula,true);
						System.out.println("fromula a evaluar "+formula_reemplazada_con_1);
							if (!evaluarExpresion(tab_tabla1.getValor(i, "IDE_NRDER"),formula_reemplazada_con_1)){
								return false;
							}						
					}
				}
			}
		}


		// Valida que la formula este correcta
		//Remplaza los registros de tipo formula con los ide del nuevo tipo
		boolean boo_valido=true;
		for(int i=0;i<tab_tabla1.getTotalFilas();i++){
			//Remplazo los id de la nueva formula
			String str_formula=tab_tabla1.getValor(i,"FORMULA_NRDER");			
			if(str_formula!=null){

				if(str_formula.startsWith("=")){
					List<String> lis_ides=new ArrayList<String>();
					String str_ide="";
					boolean boo_inicia=false;
					for (int k = 0; k < str_formula.length(); k++) {
						if(str_formula.charAt(k)=='['){
							str_ide="";
							boo_inicia=true;
							continue;
						}

						if(str_formula.charAt(k)==']'){
							boo_inicia=false;
							lis_ides.add(str_ide);
						}												
						if(boo_inicia){
							str_ide+=str_formula.charAt(k);
						}
					}

					//cambio los ide
					for (int j = 0; j < lis_ides.size(); j++) {
						String str_nuevo=ser_nomina.getNuevoIdeTipoNomina(lis_ides.get(j), tab_tabla.getValor("IDE_NRDTN"));
						if(str_nuevo!=null){
							str_formula=str_formula.replace("["+lis_ides.get(j)+"]", "["+str_nuevo+"]");						
						}
						else{
							boo_inicia=false;
							utilitario.agregarMensaje("Formula no valida", "La formula  del rubro :"+tab_tabla1.getValor(i,"IDE_NRDER")+" no es correcta, verifique que el ide ["+lis_ides.get(j)+"] al que esta haciendo referencia exista");
							return false;
						}
					}	
				}
			}
		}

		return true;
	}

	/**
	 * Evalua una expresión aritm�tica, y retorna el resultado, ejemplo
	 * 2*2/(4-2) retorna 2
	 *
	 * @param expresion
	 * @return
	 */
	public boolean evaluarExpresion(String ide,String expresion) {
		//Resuleve el valor de una expresion Ejemplo: 5+3-3
		double resultado = 0;
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Object operacion;
		try {
			expresion = expresion.replace("[", "(");
			expresion = expresion.replace("]", ")");
			operacion = engine.eval(expresion);
			System.out.println("operacion "+operacion);
			try {
				if (operacion.toString().equalsIgnoreCase("Infinity")){
					utilitario.agregarMensajeInfo("No se puede guardar ", "Error en la formula del ide: "+ide+", ERROR al evaluarExpresion( " + expresion + " )  No existe division para cero ");
					return false;
				}
				if (operacion.toString().equalsIgnoreCase("NaN")){
					utilitario.agregarMensajeInfo("No se puede guardar ", "Error en la formula del ide: "+ide+", ERROR al evaluarExpresion( " + expresion + " )  Existe indeterminacion ");
					return false;
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			resultado = Double.parseDouble(operacion.toString());
			
			return true;
		} catch (ScriptException e) {
			utilitario.agregarMensajeInfo("No se puede guardar ", "Error en la formula del ide: "+ide+", ERROR al evaluarExpresion( " + expresion + " )  :" + e.toString());
			return false;
		}

	}

	@Override
	public void guardar() {
		//Valido todas la formulas insertadas
		boolean boo_valida=true;		
		

		for(int i=0;i<tab_tabla1.getInsertadas().size();i++){
			
			
			String str_key=tab_tabla1.getInsertadas().get(i);
			boo_valida=isformulaValida(String.valueOf(tab_tabla1.getFila(str_key).getCampos()[tab_tabla1.getNumeroColumna("FORMULA_NRDER")]));
			System.out.println("boo_valida dentro del 1 for...  "+boo_valida);
			if(boo_valida==false){
				
				break;
			}
		}
		for(int i=0;i<tab_tabla1.getModificadas().size();i++){
			String str_key=tab_tabla1.getModificadas().get(i);
			boo_valida=isformulaValida(String.valueOf(tab_tabla1.getFila(str_key).getCampos()[tab_tabla1.getNumeroColumna("FORMULA_NRDER")]));
		
			if(boo_valida==false){
				
				
				break;
			}
		}

		if(boo_valida){
			// TODO Auto-generated method stub
			if(tab_tabla.guardar()){
				//System.out.println("");
				System.out.println("ingresas a VERIFCAR antes  "+verificarFormulas());
				if (verificarFormulas()){
					System.out.println("ingresas a VERIFCAR  "+verificarFormulas());
					if(tab_tabla1.guardar()){
						guardarPantalla();
					}
				}
			}	
		}
		else{
			utilitario.agregarMensajeError("Formul�(s) no v�lidas", "Revise la o las formulas insertadas");
		}

	}

	public void validarFormulas(){
		
		if(cambiaFormaulaCopiados()){

		}		
		
		
		
	}


	private boolean cambiaFormaulaCopiados(){
		//Remplaza los registros de tipo formula con los ide del nuevo tipo
		
			
		
		boolean boo_valido=true;
		for(int i=0;i<tab_tabla1.getTotalFilas();i++){
			//Remplazo los id de la nueva formula
			String str_formula=tab_tabla1.getValor(i,"FORMULA_NRDER");			
			if(str_formula!=null){

				if(str_formula.startsWith("=")){
					List<String> lis_ides=new ArrayList<String>();
					String str_ide="";
					boolean boo_inicia=false;
					for (int k = 0; k < str_formula.length(); k++) {
						if(str_formula.charAt(k)=='['){
							str_ide="";
							boo_inicia=true;
							continue;
						}

						if(str_formula.charAt(k)==']'){
							boo_inicia=false;
							lis_ides.add(str_ide);
						}												
						if(boo_inicia){
							str_ide+=str_formula.charAt(k);
						}
					}

					//cambio los ide
					for (int j = 0; j < lis_ides.size(); j++) {
						String str_nuevo=ser_nomina.getNuevoIdeTipoNomina(lis_ides.get(j), tab_tabla.getValor("IDE_NRDTN"));
						System.out.println("valor dentro del FOR...  "+str_nuevo);
						if(str_nuevo!=null){
							str_formula=str_formula.replace("["+lis_ides.get(j)+"]", "["+str_nuevo+"]");						
							tab_tabla1.setValor(i,"FORMULA_NRDER",str_formula);
							tab_tabla1.modificar(i);
						}
						else{
							boo_inicia=false;
							utilitario.agregarMensaje("Formula no valida", "La formula  del rubro :"+tab_tabla1.getValor(i,"IDE_NRDER")+" no es correcta, verifique que el ide ["+lis_ides.get(j)+"] al que esta haciendo referencia exista");
						}
					}	
				}
			}
		}

		tab_tabla1.guardar();
		guardarPantalla();
		return boo_valido;

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}


	/**
	 * Valida que la expresion de una formula sea v�lida para evaluar
	 * @param formula
	 * @return
	 */
	private boolean isformulaValida(String formula){
		System.out.println("entra en isformulaV�lida...  "+ formula);
		if(formula==null){
			System.out.println("entra en formula en null...  "+ formula);
			
			return false;
		}		
		//	Valida los [] () {} que se abran y se cierren
		int int_ca=0;
		int int_cc=0;
		int int_pa=0;
		int int_pc=0;
		int int_la=0;
		int int_lc=0;		
		for(int i=0;i<formula.length();i++){
			if(formula.charAt(i)=='['){
				int_ca++;
			}
			else if(formula.charAt(i)==']'){
				int_cc++;
			}
			else if(formula.charAt(i)=='('){
				int_pa++;
			}
			else if(formula.charAt(i)==')'){
				int_pc++;
			}
			else if(formula.charAt(i)=='{'){
				int_la++;
			}
			else if(formula.charAt(i)=='}'){
				int_lc++;
			}

		}

		if(int_ca!=int_cc){
			utilitario.agregarMensajeInfo("No se puede guardar", "La formula "+formula+" es incorrecta, El numero de corchetes abiertos debe ser igual que el numero de corchetes cerrados");
			return false;
		}
		else if(int_pa!=int_pc){
			utilitario.agregarMensajeInfo("No se puede guardar", "La formula "+formula+" es incorrecta, El numero de parentesis abiertos debe ser igual que el numero de parentesis cerrados");
			return false;
		}
		else if(int_la!=int_lc){
			utilitario.agregarMensajeInfo("No se puede guardar", "La formula "+formula+" es incorrecta, El numero de llaves abiertos debe ser igual que el numero de llaves cerrados");
			return false;
		}
		return true;
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Dialogo getDia_formula() {
		return dia_formula;
	}

	public void setDia_formula(Dialogo dia_formula) {
		this.dia_formula = dia_formula;
	}

	public SeleccionTabla getSet_det_tip_nom() {
		return set_det_tip_nom;
	}

	public void setSet_det_tip_nom(SeleccionTabla set_det_tip_nom) {
		this.set_det_tip_nom = set_det_tip_nom;
	}

	public SeleccionTabla getSet_periodo_rol() {
		return set_periodo_rol;
	}

	public void setSet_periodo_rol(SeleccionTabla set_periodo_rol) {
		this.set_periodo_rol = set_periodo_rol;
	}




	public Confirmar getCon_guardar() {
		return con_guardar;
	}




	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

}
