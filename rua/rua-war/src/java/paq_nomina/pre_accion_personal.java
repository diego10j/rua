/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;


import paq_gestion.ejb.ServicioEmpleado;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import java.util.Locale;
/**
 * 
 * @author psw
 */
public class pre_accion_personal extends Pantalla {

	private AutoCompletar aut_empleado = new AutoCompletar();

	private Tabla tab_partida_vigente = new Tabla();
	private Tabla tab_empleado_departamento = new Tabla();
	private Tabla tab_deta_empleado_depar = new Tabla();

	private SeleccionTabla set_encargo = new SeleccionTabla();
	private String p_gen_encargo_posicion = utilitario.getVariable("p_gen_encargo_posicion");
	private String p_gen_terminacion_encargo_posicion = utilitario.getVariable("p_gen_terminacion_encargo_posicion");

	private String p_gen_accion_contratacion = utilitario.getVariable("p_gen_accion_contratacion");

	//Reactivar
	private Dialogo dia_reactiva=new Dialogo();
	private Texto tex_encargo=new Texto();
	private Texto tex_anterior=new Texto();
	private Calendario cal_fecha_encargo=new Calendario();

	//configuracion de reporte
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private Dialogo dia_tipo_vinculacion=new Dialogo();
	private Radio rad_tipo_vinculacion=new Radio();
	private SeleccionTabla sel_tab_accion_motivo=new SeleccionTabla();
	private Dialogo dia_editar_motivo=new Dialogo();
	private AreaTexto are_tex_motivo=new AreaTexto();
	private Dialogo dia_terminacion=new Dialogo();
	private Calendario cal_terminacion=new Calendario();
	private AreaTexto are_tex_observacion=new AreaTexto();

	private Etiqueta eti_cargo_actual=new Etiqueta();
	private Etiqueta eti_cargo_accion=new Etiqueta();

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	@EJB
	private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	private Confirmar con_guardar=new Confirmar();
	private Dialogo dia_calendario=new Dialogo();
	private Calendario cal_fecha_ingreso_vac=new Calendario();
	private Dialogo dia_asi_vacacion=new Dialogo();



	private Dialogo dia_partida_grupo_cargo=new Dialogo();
	private AutoCompletar aut_part_gru_cargo=new AutoCompletar();
	
	public pre_accion_personal() {

		if (utilitario.getVariable("p_gen_accion_empl_comision")==null || utilitario.getVariable("p_gen_accion_empl_comision").isEmpty()){
			utilitario.agregarNotificacionInfo("No se puede abrir la pantalla","Importar parametro de sistema p_gen_accion_empl_comision");
			return;
		}

		if (utilitario.getVariable("p_gen_status_stand_by")==null || utilitario.getVariable("p_gen_status_stand_by").isEmpty()){
			utilitario.agregarNotificacionInfo("No se puede abrir la pantalla","Importar parametro de sistema p_gen_status_stand_by");
			return;
		}

		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		dia_calendario.setId("dia_calendario");
		agregarComponente(dia_calendario);


		dia_asi_vacacion.setId("dia_asi_vacacion");
		agregarComponente(dia_asi_vacacion);

		aut_empleado.setId("aut_empleado");		
		aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP from GTH_EMPLEADO");
		aut_empleado.setMetodoChange("filtrarEmpleado");

		bar_botones.quitarBotonsNavegacion();
		bar_botones.agregarComponente(new Etiqueta("Empleado:"));
		bar_botones.agregarComponente(aut_empleado);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		Boton bot_reactiva=new Boton();
		bot_reactiva.setValue("Reversa de Encargo de Posición");
		bot_reactiva.setMetodo("reactivar");
		bot_reactiva.setIcon("ui-icon-transfer-e-w");
//		bar_botones.agregarBoton(bot_reactiva);

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);
		bar_botones.agregarReporte();

		//dialogo de tipo vinculacion

		List lista1 = new ArrayList();
		Object fila1[] = {
				"0", "CONTRATO"
		};
		Object fila2[] = {
				"1", "NOMBRAMIENTO"
		};
		lista1.add(fila1);
		lista1.add(fila2);
		rad_tipo_vinculacion.setRadio(lista1);	
		rad_tipo_vinculacion.setVertical();

		Grid gri_vinculacion=new Grid();
		gri_vinculacion.setColumns(4);
		gri_vinculacion.getChildren().add(new Etiqueta("TIPO CONTRATO"));
		gri_vinculacion.getChildren().add(rad_tipo_vinculacion);

		dia_tipo_vinculacion.setId("dia_tipo_vinculacion");
		dia_tipo_vinculacion.setTitle("TIPO DE VINCULACION");
		dia_tipo_vinculacion.setWidth("30%");
		dia_tipo_vinculacion.setHeight("25%");
		dia_tipo_vinculacion.setDynamic(false);
		dia_tipo_vinculacion.setDialogo(gri_vinculacion);
		dia_tipo_vinculacion.setDynamic(false);
		dia_tipo_vinculacion.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(dia_tipo_vinculacion);

		//seleccion tabla de accion motivo
		sel_tab_accion_motivo.setId("sel_tab_accion_motivo");
		//sel_tab_accion_motivo.setSeleccionTabla("SELECT ide_gemed,detalle_gemed,detalle_reporte_gemed FROM gen_motivo_empleado_depa", "ide_gemed");
		sel_tab_accion_motivo.setSeleccionTabla("SELECT a.IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED,c.detalle_reporte_gemed FROM GEN_ACCION_MOTIVO_EMPLEADO a " +
				"LEFT JOIN ( " +
				"SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA " +
				")b ON b.IDE_GEAED=a.IDE_GEAED " +
				"LEFT JOIN ( " +
				"SELECT IDE_GEMED,DETALLE_GEMED,detalle_reporte_gemed FROM GEN_MOTIVO_EMPLEADO_DEPA " +
				")c ON c.IDE_GEMED=a.IDE_GEMED WHERE a.IDE_GEAME IN (-1) " +
				"ORDER BY  b.DETALLE_GEAED,c.DETALLE_GEMED", "IDE_GEAME");
		sel_tab_accion_motivo.setTitle("MOTIVOS DE ACCION DE PERSONAL");
		sel_tab_accion_motivo.getTab_seleccion().getColumna("detalle_gemed").setFiltro(true);
		sel_tab_accion_motivo.setRadio();		
		sel_tab_accion_motivo.setDynamic(false);
		sel_tab_accion_motivo.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_accion_motivo);

		//dialogo de editar motivo
		are_tex_motivo.setId("are_tex_motivo");
		are_tex_motivo.setValue("");


		//are_tex_motivo.set		
		Grid gri_editar_motivo=new Grid();

		gri_editar_motivo.setMensajeInfo("En esta �rea puede editar el detalle de la Acción de Personal");
		gri_editar_motivo.getChildren().add(are_tex_motivo);	


		dia_editar_motivo.setId("dia_editar_motivo");
		dia_editar_motivo.setTitle("EDITOR DE EL DETALLE DE ACCióN DE PERSONAL");
		dia_editar_motivo.setWidth("65%");
		dia_editar_motivo.setHeight("60%");
		dia_editar_motivo.setDialogo(gri_editar_motivo);
		dia_editar_motivo.setDynamic(false);
		dia_editar_motivo.getBot_aceptar().setMetodo("aceptarReporte");
		gri_editar_motivo.setStyle("width:" + (dia_editar_motivo.getAnchoPanel() - 5) + "px;overflow:auto;");
		are_tex_motivo.setStyle("width:" + (dia_editar_motivo.getAnchoPanel() - 15) + "px;overflow:auto; height:"+ (dia_editar_motivo.getAltoPanel() - 5)+"px");
		agregarComponente(dia_editar_motivo);

		Tabulador tab_tabulador = new Tabulador();
		tab_tabulador.setId("tab_tabulador");
		tab_partida_vigente.setHeader(eti_cargo_actual);
		tab_partida_vigente.setId("tab_partida_vigente");
		tab_partida_vigente.setIdCompleto("tab_tabulador:tab_partida_vigente");
		tab_partida_vigente.setTabla("GEN_EMPLEADOS_DEPARTAMENTO_PAR",
				"IDE_GEEDP", 1);		
		tab_partida_vigente.getColumna("IDE_GEGRO").setCombo(
				"GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO", "");		
		tab_partida_vigente
		.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL",
				"IDE_GECAF",
				"DETALLE_GECAF",
				"PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO=-1)");
		tab_partida_vigente.getColumna("GEN_IDE_GECAF").setCombo(
				"GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
		tab_partida_vigente.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL",
				"IDE_SUCU", "NOM_SUCU", "");
		tab_partida_vigente.getColumna("IDE_SUCU").setVisible(true);		
		tab_partida_vigente
		.getColumna("IDE_GEARE")
		.setCombo(
				"GEN_AREA",
				"IDE_GEARE",
				"DETALLE_GEARE",
				"IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1)");
		tab_partida_vigente.getColumna("IDE_GEARE").setMetodoChange(
				"cargarCargoDepartamentos");
		tab_partida_vigente.getColumna("IDE_GEARE").setBuscarenCombo(false);
		tab_partida_vigente
		.getColumna("IDE_GEDEP")
		.setCombo(
				"GEN_DEPARTAMENTO",
				"IDE_GEDEP",
				"DETALLE_GEDEP",
				"IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1 AND IDE_GEARE=-1)");		
		tab_partida_vigente.getColumna("IDE_GECAE").setCombo(
				"GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE", "");
		tab_partida_vigente.getColumna("IDE_GETIV").setCombo(
				"GEN_TIPO_VINCULACION", "IDE_GETIV", "DETALLE_GETIV", "");		
		tab_partida_vigente
		.getColumna("IDE_GEPGC")
		.setCombo(
				"SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP "
						+ "from GEN_PARTIDA_GRUPO_CARGO pgc "
						+ "left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP");
		tab_partida_vigente.getColumna("IDE_GTTEM").setCombo(
				"GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_partida_vigente.getColumna("IDE_GTTCO").setCombo(
				"GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO", "");
		tab_partida_vigente.getColumna("IDE_GTTSI").setCombo(
				"GTH_TIPO_SINDICATO", "IDE_GTTSI", "DETALLE_GTTSI", "");
		tab_partida_vigente.getColumna("IDE_GTGRE").setCombo(
				"GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
		tab_partida_vigente.getColumna("IDE_GTGRE").setCombo(
				"GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");		
		tab_partida_vigente.getColumna("ACTIVO_GEEDP").setVisible(false);
		tab_partida_vigente.getColumna("ACUMULA_FONDOS_GEEDP").setCheck();
		tab_partida_vigente.getColumna("LINEA_SUPERVICION_GEEDP").setCheck();
		tab_partida_vigente.getColumna("IDE_GTEMP").setVisible(false);
		tab_partida_vigente.getColumna("CONTROL_ASISTENCIA_GEEDP").setCheck();
		tab_partida_vigente.getColumna("CONTROL_ASISTENCIA_GEEDP").setValorDefecto("false");
		List listaliquidacion = new ArrayList();
		Object fila3[] = {
				"0", "SI"
		};
		Object fila4[] = {
				"1", "NO"
		};
		listaliquidacion.add(fila3);
		listaliquidacion.add(fila4);
		
		List listaejecutaliqui = new ArrayList();
		Object fila5[] = {
				"0", "SI"
		};
		Object fila6[] = {
				"1", "NO"
		};
		listaejecutaliqui.add(fila5);
		listaejecutaliqui.add(fila6);
		tab_partida_vigente.getColumna("LIQUIDACION_GEEDP").setRadio(listaliquidacion, "0");
		tab_partida_vigente.getColumna("LIQUIDACION_GEEDP").setRadioVertical(true);		
		tab_partida_vigente.getColumna("EJECUTO_LIQUIDACION_GEEDP").setRadio(listaejecutaliqui, "0");
		tab_partida_vigente.getColumna("EJECUTO_LIQUIDACION_GEEDP").setRadioVertical(true);
		tab_partida_vigente.setCondicion("IDE_GTEMP=-1 AND ACTIVO_GEEDP=true");
		tab_partida_vigente.setMostrarcampoSucursal(true);
		tab_partida_vigente.setTipoFormulario(true);
		tab_partida_vigente.setLectura(true);
		tab_partida_vigente.getGrid().setColumns(4);
		tab_partida_vigente.setMostrarNumeroRegistros(false);
		tab_partida_vigente.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_partida_vigente);
		
		
		

		tab_tabulador.agregarTab("Estado Actual", pat_panel);

		tab_deta_empleado_depar.setId("tab_deta_empleado_depar");
		tab_deta_empleado_depar.onSelect("seleccionarTabla1");

		tab_deta_empleado_depar
		.setIdCompleto("tab_tabulador:tab_deta_empleado_depar");
		tab_deta_empleado_depar.setTabla("GEN_DETALLE_EMPLEADO_DEPARTAME",
				"IDE_GEDED", 2);
		tab_deta_empleado_depar.getColumna("FECHA_SALIDA_GEDED").setVisible(false);
		tab_deta_empleado_depar.getColumna("GEN_IDE_GEDED").setVisible(false);

		tab_deta_empleado_depar.getColumna("IDE_GEINS").setCombo(
				"GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "");
		tab_deta_empleado_depar.getColumna("IDE_GEINS").setAutoCompletar();
		tab_deta_empleado_depar.setRecuperarLectura(true);
		tab_deta_empleado_depar
		.getColumna("IDE_GEAME")
		.setCombo(
				"SELECT a.IDE_GEAME,b.IDE_GEAED,b.DETALLE_GEAED,c.DETALLE_GEMED FROM GEN_ACCION_MOTIVO_EMPLEADO a "
						+ "LEFT JOIN ( "
						+ "SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA "
						+ ")b ON b.IDE_GEAED=a.IDE_GEAED "
						+ "LEFT JOIN ( "
						+ "SELECT IDE_GEMED,DETALLE_GEMED FROM GEN_MOTIVO_EMPLEADO_DEPA "
						+ ")c ON c.IDE_GEMED=a.IDE_GEMED "
						+ "ORDER BY  b.DETALLE_GEAED,c.DETALLE_GEMED");
		tab_deta_empleado_depar.getColumna("IDE_GEAME").setAutoCompletar();
		tab_deta_empleado_depar.getColumna("IDE_GEAME").setMetodoChange("cambioAccion");
		tab_deta_empleado_depar.getColumna("FECHA_INGRESO_GEDED").setValorDefecto(utilitario.getFechaActual());
		tab_deta_empleado_depar.getColumna("ACTIVO_GEDED").setCheck();
		tab_deta_empleado_depar.getColumna("ACTIVO_GEDED").setValorDefecto("true");
		tab_deta_empleado_depar.getColumna("ACTIVO_GEDED").setLectura(true);
		tab_deta_empleado_depar.getColumna("IDE_GTEMP").setVisible(false);
		tab_deta_empleado_depar.setCampoOrden("IDE_GEDED DESC");
		tab_deta_empleado_depar.agregarRelacion(tab_empleado_departamento);
		tab_deta_empleado_depar.setCondicion("IDE_GTEMP=-1");		
		tab_deta_empleado_depar.getColumna("GEN_IDE_GEDED").setLectura(true);	
		tab_deta_empleado_depar.setScrollable(true);
		tab_deta_empleado_depar.setScrollHeight(100);
		tab_deta_empleado_depar.setValidarInsertar(true);
		tab_deta_empleado_depar.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_deta_empleado_depar);
		tab_deta_empleado_depar.setRecuperarLectura(false);

		tab_empleado_departamento.setHeader(eti_cargo_accion);
		tab_empleado_departamento.setId("tab_empleado_departamento");
		tab_empleado_departamento.setIdCompleto("tab_tabulador:tab_empleado_departamento");
		tab_empleado_departamento.setTabla("GEN_EMPLEADOS_DEPARTAMENTO_PAR","IDE_GEEDP", 3);
		tab_empleado_departamento.onSelect("seleccionaTablaEmpleadosDepartamento");
		tab_empleado_departamento.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO", "");
		tab_empleado_departamento.getColumna("FECHA_GEEDP").setValorDefecto(utilitario.getFechaActual());
		tab_empleado_departamento.getColumna("FECHA_GEEDP").setMetodoChange("llenarFechaFinContrato");
		tab_empleado_departamento.getColumna("IDE_GEGRO").setMetodoChange("cargarCargoFuncional");
//		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL","IDE_GECAF","DETALLE_GECAF","PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO=-1)");
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL","IDE_GECAF","DETALLE_GECAF","");

		tab_empleado_departamento.getColumna("IDE_GECAF").setMetodoChange("cargarPartidaGrupoCargo");
		tab_empleado_departamento.getColumna("IDE_GECAF").setBuscarenCombo(false);
		tab_empleado_departamento.getColumna("GEN_IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
		tab_empleado_departamento.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
		tab_empleado_departamento.getColumna("IDE_SUCU").setVisible(true);
		tab_empleado_departamento.getColumna("IDE_SUCU").setMetodoChange(
				"cargarCargoAreas");
		tab_empleado_departamento
		.getColumna("IDE_GEARE")
		.setCombo(
				"GEN_AREA",
				"IDE_GEARE",
				"DETALLE_GEARE",
				"IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1)");
		tab_empleado_departamento.getColumna("IDE_GEARE").setMetodoChange(
				"cargarCargoDepartamentos");
		tab_empleado_departamento.getColumna("IDE_GEARE").setBuscarenCombo(
				false);
//		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO","IDE_GEDEP","DETALLE_GEDEP","IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1 AND IDE_GEARE=-1)");
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO","IDE_GEDEP","DETALLE_GEDEP","");

		tab_empleado_departamento.getColumna("IDE_GEDEP").setMetodoChange(
				"cargarPartidaGrupoCargo");
		tab_empleado_departamento.getColumna("IDE_GEDEP").setBuscarenCombo(
				false);
		tab_empleado_departamento.getColumna("IDE_GECAE").setCombo("GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE", "");
		tab_empleado_departamento.getColumna("IDE_GECAE").setLectura(true);

		tab_empleado_departamento.getColumna("IDE_GETIV").setCombo(
				"GEN_TIPO_VINCULACION", "IDE_GETIV", "DETALLE_GETIV", "");
		tab_empleado_departamento.getColumna("IDE_GETIV").setBuscarenCombo(
				false);


		tab_empleado_departamento.getColumna("AJUSTE_SUELDO_GEEDP").setMetodoChange("cambioAjuste");
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP,pgc.TITULO_CARGO_GEPGC "
				+"from GEN_PARTIDA_GRUPO_CARGO pgc "
				+ "left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP ");
		tab_empleado_departamento.getColumna("IDE_GEPGC").setLectura(true);
		tab_empleado_departamento.getColumna("IDE_GTTEM").setCombo(
				"GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_empleado_departamento.getColumna("IDE_GTTCO").setCombo(
				"GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO", "");
		tab_empleado_departamento.getColumna("IDE_GTTSI").setCombo(
				"GTH_TIPO_SINDICATO", "IDE_GTTSI", "DETALLE_GTTSI", "");
		tab_empleado_departamento.getColumna("IDE_GTGRE").setCombo(
				"GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
		tab_empleado_departamento.getColumna("IDE_GTGRE").setCombo(
				"GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
		tab_empleado_departamento.getColumna("ACTIVO_GEEDP").setCheck();
		tab_empleado_departamento.getColumna("ACTIVO_GEEDP").setLectura(true);
		tab_empleado_departamento.getColumna("ACTIVO_GEEDP").setValorDefecto("true");
		tab_empleado_departamento.getColumna("ACUMULA_FONDOS_GEEDP").setCheck();
		tab_empleado_departamento.getColumna("LINEA_SUPERVICION_GEEDP")
		.setCheck();
		tab_empleado_departamento.getColumna("IDE_GEPGC").setAutoCompletar();
		tab_empleado_departamento.getColumna("IDE_GEPGC").setMetodoChange("cambioPartida");
		tab_empleado_departamento.getColumna("IDE_GTEMP").setVisible(false);		
		tab_empleado_departamento.getColumna("CONTROL_ASISTENCIA_GEEDP").setCheck();
		tab_empleado_departamento.getColumna("CONTROL_ASISTENCIA_GEEDP").setValorDefecto("false");		
		tab_partida_vigente.getColumna("LIQUIDACION_GEEDP").setRadio(listaliquidacion, "0");
		tab_partida_vigente.getColumna("LIQUIDACION_GEEDP").setRadioVertical(true);		
		tab_partida_vigente.getColumna("EJECUTO_LIQUIDACION_GEEDP").setRadio(listaejecutaliqui, "0");
		tab_partida_vigente.getColumna("EJECUTO_LIQUIDACION_GEEDP").setRadioVertical(true);
		tab_empleado_departamento.setMostrarcampoSucursal(true);
		tab_empleado_departamento.setTipoFormulario(true);
		tab_empleado_departamento.getGrid().setColumns(4);
		tab_empleado_departamento.getColumna("IDE_GTTCO").setMetodoChange("cambioTipoContrato");
		tab_empleado_departamento.setRecuperarLectura(true);
		tab_empleado_departamento.setMostrarNumeroRegistros(false);

		actualizarCombosDepartamentoEmpleado();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_empleado_departamento);
		pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
		pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
		pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);


		aut_part_gru_cargo.setId("aut_part_gru_cargo");
		aut_part_gru_cargo.setAutoCompletar("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP,pgc.TITULO_CARGO_GEPGC "
				+"from GEN_PARTIDA_GRUPO_CARGO pgc "
				+ "left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP where pgc.VACANTE_GEPGC=TRUE and pgc.ACTIVO_GEPGC=TRUE ");
		
		Grid gri=new Grid();
		gri.setColumns(2);
		gri.getChildren().add(new Etiqueta("Partida Grupo Cargo"));
		gri.getChildren().add(aut_part_gru_cargo);
		dia_partida_grupo_cargo.setId("dia_partida_grupo_cargo");
		dia_partida_grupo_cargo.setWidth("30%");
		dia_partida_grupo_cargo.setHeight("20%");
		dia_partida_grupo_cargo.setDialogo(gri);
		dia_partida_grupo_cargo.setTitle("PARTIDA GRUPO CARGO");
		dia_partida_grupo_cargo.getBot_aceptar().setMetodo("aceptarPartidaGrupoCargo");
		dia_partida_grupo_cargo.setDynamic(true);
		agregarComponente(dia_partida_grupo_cargo);
		
		
		Boton bot_cambiar_pgc=new Boton();
		bot_cambiar_pgc.setValue("Registro de Partida");
		bot_cambiar_pgc.setMetodo("cambiarPartida");
		bot_cambiar_pgc.setIcon("ui-icon-transfer-e-w");
		bar_botones.agregarBoton(bot_cambiar_pgc);
		
		Grupo gri_grid = new Grupo();		
		//pat_panel2.setStyle("width:99%;height:100%;overflow: hidden;display: block;");
		gri_grid.getChildren().add(pat_panel2);
		gri_grid.getChildren().add(pat_panel1);

		tab_tabulador.agregarTab("Acciones de Personal", gri_grid);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(tab_tabulador);
		agregarComponente(div_division);

		set_encargo.setId("set_encargo");
		set_encargo.setWidth("70%");
		set_encargo.setHeight("60%");
		set_encargo.setTitle("ENCARGO DE PUESTO");
		set_encargo.setSeleccionTabla(
				"select a.ide_gepgc,IDE_GEARE,a.ide_gegro,a.ide_gecaf,a.ide_sucu,a.ide_gedep,a.ide_gttem, titulo_cargo_gepgc,c.detalle_gegro as grupo_ocuapcional,detalle_gecaf as cargo,salario_encargo_gepgc from gen_partida_grupo_cargo a, gen_cargo_funcional b,gen_grupo_ocupacional c where a.ide_gecaf = b.ide_gecaf and a.ide_gegro = c.ide_gegro and encargo_gepgc=TRUE order by titulo_cargo_gepgc ",
				"ide_gepgc");

		set_encargo.getTab_seleccion().getColumna("ide_gepgc").setVisible(false);
		set_encargo.getTab_seleccion().getColumna("IDE_GEARE").setVisible(false);
		set_encargo.getTab_seleccion().getColumna("ide_gegro").setVisible(false);
		set_encargo.getTab_seleccion().getColumna("ide_gecaf").setVisible(false);
		set_encargo.getTab_seleccion().getColumna("ide_sucu").setVisible(false);
		set_encargo.getTab_seleccion().getColumna("ide_gedep").setVisible(false);
		set_encargo.getTab_seleccion().getColumna("ide_gttem").setVisible(false);
		set_encargo.getBot_aceptar().setMetodo("aceptarEncargo");
		set_encargo.getBot_cancelar().setMetodo("cancelarEncargo");
		set_encargo.setRadio();
		agregarComponente(set_encargo);


		dia_reactiva.setId("dia_reactiva");
		dia_reactiva.setHeight("33%");
		dia_reactiva.setWidth("50%");
		dia_reactiva.setTitle("Reactivacion de Encargo de Posición");

		Grid gri_rac=new Grid();
		gri_rac.setStyle("Width:"+(dia_reactiva.getAnchoPanel()-10)+"PX;height:" + (dia_reactiva.getAltoPanel()-10) + "px;overflow: auto;display: block;");

		gri_rac.setColumns(2);
		gri_rac.getChildren().add(new Etiqueta("Posición Encargada :"));
		tex_encargo.setSize(50);
		tex_encargo.setReadonly(true);
		tex_encargo.setStyle("width:99%");
		gri_rac.getChildren().add(tex_encargo);
		gri_rac.getChildren().add(new Etiqueta("Posición Anterior :"));
		tex_anterior.setSize(50);
		tex_anterior.setReadonly(true);
		tex_anterior.setStyle("width:99%");
		gri_rac.getChildren().add(tex_anterior);
		gri_rac.getChildren().add(new Etiqueta("Fecha Encargo Posición :"));
		gri_rac.getChildren().add(cal_fecha_encargo);	

		dia_reactiva.getBot_aceptar().setMetodo("aceptarReactivar");
		dia_reactiva.setDialogo(gri_rac);
		agregarComponente(dia_reactiva);

		//dia_terminacion

		dia_terminacion.setId("dia_terminacion");
		dia_terminacion.setWidth("40%");
		dia_terminacion.setHeight("40%");

		are_tex_observacion.setId("are_tex_observacion");
		are_tex_observacion.setStyle("width:300px;");

		cal_terminacion.setId("cal_terminacion");

		Grid gri_terminacion=new Grid();
		gri_terminacion.setColumns(2);
		//		gri_terminacion.setStyle("width:" + (dia_terminacion.getAnchoPanel() - 5) + "px;overflow:auto;");
		gri_terminacion.getChildren().add(new Etiqueta("Ingrese la fecha te termino de contrato: "));
		gri_terminacion.getChildren().add(cal_terminacion);
		gri_terminacion.getChildren().add(new Etiqueta("Ingrese Motivo de Fin de Contrato: "));
		gri_terminacion.getChildren().add(are_tex_observacion);

		dia_terminacion.setTitle("TERMINO DE CONTRATO");
		dia_terminacion.setDialogo(gri_terminacion);
		dia_terminacion.getBot_aceptar().setMetodo("cargarFechaTerminacion");
		agregarComponente(dia_terminacion);



	}

	
public Dialogo getDia_partida_grupo_cargo() {
		return dia_partida_grupo_cargo;
	}


	public void setDia_partida_grupo_cargo(Dialogo dia_partida_grupo_cargo) {
		this.dia_partida_grupo_cargo = dia_partida_grupo_cargo;
	}


	public AutoCompletar getAut_part_gru_cargo() {
		return aut_part_gru_cargo;
	}


	public void setAut_part_gru_cargo(AutoCompletar aut_part_gru_cargo) {
		this.aut_part_gru_cargo = aut_part_gru_cargo;
	}


public void aceptarPartidaGrupoCargo(){
	if (aut_part_gru_cargo.getValor()==null || aut_part_gru_cargo.getValor().isEmpty()){
		utilitario.agregarMensajeInfo("No ha seleccionado ninguna partida","");
		return;
	}
	
	
		TablaGenerica tab_datos= utilitario.consultar("select ide_gepgc,ide_gegro,ide_gecaf,ide_sucu,ide_gedep,ide_geare,ide_gttem from gen_partida_grupo_cargo where IDE_GEPGC ="+aut_part_gru_cargo.getValor());

		if(tab_datos.isEmpty()==false){
			tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_datos.getValor("IDE_GEGRO")+")");
			tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_datos.getValor("ide_sucu")+")");
			tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_datos.getValor("ide_sucu")+" AND IDE_GEARE="+tab_datos.getValor("IDE_GEARE")+")");

			tab_empleado_departamento.modificar(tab_empleado_departamento.getFilaActual());
			tab_empleado_departamento.setValor("IDE_GEPGC",aut_part_gru_cargo.getValor());		

			tab_empleado_departamento.setValor("IDE_GEGRO",tab_datos.getValor("IDE_GEGRO"));		
			tab_empleado_departamento.setValor("IDE_GECAF",
					tab_datos.getValor("IDE_GECAF"));		
			tab_empleado_departamento.setValor("IDE_SUCU",				tab_datos.getValor("ide_sucu"));		
			tab_empleado_departamento.setValor("IDE_GTTEM",
					tab_datos.getValor("IDE_GTTEM"));		
			tab_empleado_departamento.setValor("IDE_GEDEP",
					tab_datos.getValor("IDE_GEDEP"));		
			tab_empleado_departamento.setValor("IDE_GEARE",tab_datos.getValor("IDE_GEARE"));
			utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GEPGC,IDE_GECAF,IDE_SUCU,IDE_GTTEM,IDE_GEDEP,IDE_GEARE", "tab_tabulador:tab_empleado_departamento");
			dia_partida_grupo_cargo.cerrar();
		}
	
}

public void cambiarPartida(){
		
		if (tab_deta_empleado_depar.getValor("activo_geded")==null || tab_deta_empleado_depar.getValor("activo_geded").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede cambiar la partida", "La accion se encuentra inactiva");
			return;
		}
		if (tab_deta_empleado_depar.getValor("activo_geded").equalsIgnoreCase("false") 
				|| tab_deta_empleado_depar.getValor("activo_geded").equalsIgnoreCase("0")){
			utilitario.agregarMensajeInfo("No se puede cambiar la partida", "La accion se encuentra inactiva");
			return;
		}

		aut_part_gru_cargo.setAutoCompletar("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP,pgc.TITULO_CARGO_GEPGC "
				+"from GEN_PARTIDA_GRUPO_CARGO pgc "
				+ "left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP where pgc.VACANTE_GEPGC=TRUE and pgc.ACTIVO_GEPGC=TRUE ");

		aut_part_gru_cargo.setValor(null);

		dia_partida_grupo_cargo.dibujar();
		
	}
	
	public void llenarFechaFinContrato(DateSelectEvent evt){
		tab_empleado_departamento.modificar(evt);
		int int_dias=0;
		String str_fecha_ini=tab_empleado_departamento.getValor("FECHA_GEEDP");

		try {
			int_dias=Integer.parseInt(ser_gestion.getTipoContrato(tab_empleado_departamento.getValor("IDE_GTTCO")).getValor("DIA_FINC_GTTCO"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		System.out.println("fecha fin "+utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(str_fecha_ini), int_dias)));
		tab_empleado_departamento.setValor("FECHA_FINCTR_GEEDP", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(str_fecha_ini), int_dias)));
		utilitario.addUpdateTabla(tab_empleado_departamento, "FECHA_FINCTR_GEEDP", "");

	}

	public void llenarFechaFinContrato(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		int int_dias=0;
		String str_fecha_ini=tab_empleado_departamento.getValor("FECHA_GEEDP");

		try {
			int_dias=Integer.parseInt(ser_gestion.getTipoContrato(tab_empleado_departamento.getValor("IDE_GTTCO")).getValor("DIA_FINC_GTTCO"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("fecha fin "+utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(str_fecha_ini), int_dias)));
		tab_empleado_departamento.setValor("FECHA_FINCTR_GEEDP", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(str_fecha_ini), int_dias)));
		utilitario.addUpdateTabla(tab_empleado_departamento, "FECHA_FINCTR_GEEDP", "");
	}

	/**
	 * Carga el nombre del cargo del estado actual
	 */
	private void cargarNombreCargoPartidaActual(){
		if(tab_partida_vigente.isEmpty()==false){			
			List lis_puesto=utilitario.getConexion().consultar("select titulo_cargo_gepgc from gen_partida_grupo_cargo where ide_gepgc="+tab_partida_vigente.getValor("ide_gepgc"));
			if(lis_puesto.isEmpty()==false){
				eti_cargo_actual.setValue(lis_puesto.get(0));
			}
		}

	}

	/**
	 * Carga el nombre del cargo de  la partida seleccionada
	 */
	private void cargarNombreCargoPartidaSeleccionada(){
		if(tab_empleado_departamento.isEmpty()==false){
			List lis_puesto=utilitario.getConexion().consultar("select titulo_cargo_gepgc from gen_partida_grupo_cargo where ide_gepgc="+tab_empleado_departamento.getValor("ide_gepgc"));
			if(lis_puesto.isEmpty()==false){
				eti_cargo_accion.setValue(lis_puesto.get(0));
			}
		}

	}


	public void aceptarReactivar(){				
		String str_GEN_IDE_GEDED= tab_deta_empleado_depar.getValor("GEN_IDE_GEDED");
		utilitario.getConexion().getSqlPantalla().clear();
		utilitario.getConexion().agregarSql("UPDATE GEN_DETALLE_EMPLEADO_DEPARTAME set ACTIVO_GEDED=false WHERE IDE_GTEMP="+aut_empleado.getValor());
		utilitario.getConexion().agregarSql("UPDATE GEN_DETALLE_EMPLEADO_DEPARTAME set ACTIVO_GEDED=true WHERE IDE_GEDED="+str_GEN_IDE_GEDED);		
		utilitario.getConexion().agregarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set ACTIVO_GEEDP=false WHERE IDE_GTEMP="+aut_empleado.getValor());
		utilitario.getConexion().agregarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set ACTIVO_GEEDP=true WHERE IDE_GEDED="+str_GEN_IDE_GEDED);
		if(guardarPantalla().isEmpty()){
			dia_reactiva.cerrar();
			tab_partida_vigente.ejecutarSql();	
			actualizarCombosDepartamentoActivoEmpleado();
			tab_deta_empleado_depar.ejecutarSql();
			seleccionarTabla1();
			//utilitario.addUpdate("tab_tabulador:tab_partida_vigente");	
		}		
	}


	private String getAccionAnterior(){
		tab_deta_empleado_depar.ejecutarSql();		
		if(tab_deta_empleado_depar.getTotalFilas()>1){			
			return tab_deta_empleado_depar.getValor(1,"IDE_GEDED");		
		}
		return null;
	}


	public void cambioTipoContrato(AjaxBehaviorEvent evt){		
		tab_empleado_departamento.modificar(evt);
		if(tab_empleado_departamento.getValor("IDE_GTTCO")!=null){
			TablaGenerica tab_tipocon=utilitario.consultar("SELECT * FROM GTH_TIPO_CONTRATO WHERE IDE_GTTCO="+tab_empleado_departamento.getValor("IDE_GTTCO"));	
			if(!tab_tipocon.isEmpty()){
				String str_dias=tab_tipocon.getValor("DIA_FINC_GTTCO");
				String str_fecha_ini=tab_empleado_departamento.getValor("FECHA_GEEDP");

				if(str_dias!=null && str_fecha_ini!=null){
					int int_dias=0;
					try {
						int_dias=Integer.parseInt(str_dias);
					} catch (Exception e) {
						// TODO: handle exception
					}
					tab_empleado_departamento.setValor("FECHA_FINCTR_GEEDP", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(str_fecha_ini), int_dias)));
					utilitario.addUpdateTabla(tab_empleado_departamento, "FECHA_FINCTR_GEEDP", "");
				}

			}
		}

	}

	public void cambioPartida(SelectEvent evt){
		tab_empleado_departamento.modificar(evt);
		if(tab_empleado_departamento.getValor("IDE_GEPGC")!=null){
			TablaGenerica tab_datos= utilitario.consultar("select ide_gepgc,ide_gegro,ide_gecaf,ide_sucu,ide_gedep,ide_geare,ide_gttem from gen_partida_grupo_cargo where IDE_GEPGC ="+tab_empleado_departamento.getValor("IDE_GEPGC"));

			if(tab_datos.isEmpty()==false){
				tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_datos.getValor("IDE_GEGRO")+")");
				tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_datos.getValor("ide_sucu")+")");
				tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_datos.getValor("ide_sucu")+" AND IDE_GEARE="+tab_datos.getValor("IDE_GEARE")+")");

				tab_empleado_departamento.setValor("IDE_GEGRO",tab_datos.getValor("IDE_GEGRO"));		
				tab_empleado_departamento.setValor("IDE_GECAF",
						tab_datos.getValor("IDE_GECAF"));		
				tab_empleado_departamento.setValor("IDE_SUCU",				tab_datos.getValor("ide_sucu"));		
				tab_empleado_departamento.setValor("IDE_GTTEM",
						tab_datos.getValor("IDE_GTTEM"));		
				tab_empleado_departamento.setValor("IDE_GEDEP",
						tab_datos.getValor("IDE_GEDEP"));		
				tab_empleado_departamento.setValor("IDE_GEARE",tab_datos.getValor("IDE_GEARE"));
				utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");
			}
		}
	}

	public void cambioAjuste(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		try {
			double  dou_ajuste= Double.parseDouble(tab_empleado_departamento.getValor("AJUSTE_SUELDO_GEEDP"));
			TablaGenerica tab_sueldo=utilitario.consultar("SELECT * FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR where ACTIVO_GEEDP=true and IDE_GTEMP="+aut_empleado.getValor());
			if(tab_sueldo.isEmpty()==false){
				double dou_sueldo= Double.parseDouble(tab_sueldo.getValor("RMU_GEEDP"));
				dou_sueldo+=dou_ajuste;
				tab_empleado_departamento.setValor("RMU_GEEDP", utilitario.getFormatoNumero(dou_sueldo));
				utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");	
			}
			else{
				utilitario.agregarMensaje("No existe una Accion Activa","");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void limpiar(){
		tab_partida_vigente.limpiar();
		tab_deta_empleado_depar.limpiar();
		aut_empleado.limpiar();		
		utilitario.addUpdate("tab_tabulador,aut_empleado");
	}

	public void reactivar(){
		if(tab_deta_empleado_depar.getValor("IDE_GEAME")!=null){
			if(tab_deta_empleado_depar.getValor("ACTIVO_GEDED")!=null && tab_deta_empleado_depar.getValor("ACTIVO_GEDED").equals("true")){
				if (tab_deta_empleado_depar.getValor("IDE_GEAME").equals(p_gen_encargo_posicion)){

					//Busco los nombres de los cargos
					TablaGenerica tab_cargo=utilitario.consultar("select ide_gepgc,titulo_cargo_gepgc " +
							"from gen_partida_grupo_cargo where ide_gepgc in (select ide_gepgc from gen_empleados_departamento_par where ide_geded in("+tab_deta_empleado_depar.getValor("IDE_GEDED")+","+tab_deta_empleado_depar.getValor("GEN_IDE_GEDED")+"))");

					if(tab_cargo.getTotalFilas()==2){
						//Carga los nombres de los puestos
						tex_encargo.setValue(tab_cargo.getValor(1,"titulo_cargo_gepgc"));				
						tex_anterior.setValue(tab_cargo.getValor(0,"titulo_cargo_gepgc"));
					}
					else{
						tex_anterior.limpiar();
						tex_anterior.limpiar();
					}

					cal_fecha_encargo.setValue(utilitario.getFecha(tab_partida_vigente.getValor("FECHA_ENCARGO_GEEDP")));
					dia_reactiva.dibujar();				
				}	
				else{
					utilitario.agregarMensajeInfo("No se puede Reactivar", "Debe seleccionar una Acción de tipo Encargo de Posicion");
				}	
			}
			else{
				utilitario.agregarMensajeInfo("No se puede Reactivar", "La Acción debe estar activa");	
			}
		}

	}


	private void actualizarCombosDepartamentoEmpleado(String ide_sucu,String IDE_GEGRO,String IDE_GEARE){
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+IDE_GEGRO+")");
		tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+ide_sucu+")");
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+ide_sucu+" AND IDE_GEARE="+IDE_GEARE+")");

		tab_empleado_departamento.setValor("IDE_GEGRO",IDE_GEGRO);		
		tab_empleado_departamento.setValor("IDE_GECAF",
				set_encargo.getTab_seleccion().getValor("IDE_GECAF"));		
		tab_empleado_departamento.setValor("IDE_SUCU",				ide_sucu);		
		tab_empleado_departamento.setValor("IDE_GTTEM",
				set_encargo.getTab_seleccion().getValor("IDE_GTTEM"));		
		tab_empleado_departamento.setValor("IDE_GEDEP",
				set_encargo.getTab_seleccion().getValor("IDE_GEDEP"));		
		tab_empleado_departamento.setValor("IDE_GEARE",IDE_GEARE);		
		cargarNombreCargoPartidaSeleccionada();
	}

	private void actualizarCombosDepartamentoEmpleado(){
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_empleado_departamento.getValor("IDE_GEGRO")+")");	
		tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("ide_sucu")+")");		
		tab_empleado_departamento.actualizarCombosFormulario();
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" AND IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+")");		
		tab_empleado_departamento.actualizarCombosFormulario();
		cargarNombreCargoPartidaSeleccionada();
	}

	private void actualizarCombosDepartamentoEmpleadoconActual(){
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_partida_vigente.getValor("IDE_GEGRO")+")");		
		tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_partida_vigente.getValor("ide_sucu")+")");		
		tab_empleado_departamento.actualizarCombosFormulario();
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_partida_vigente.getValor("IDE_SUCU")+" AND IDE_GEARE="+tab_partida_vigente.getValor("IDE_GEARE")+")");
		tab_empleado_departamento.actualizarCombosFormulario();
		cargarNombreCargoPartidaSeleccionada();
	}

	private void actualizarCombosDepartamentoActivoEmpleado(){
		tab_partida_vigente.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_partida_vigente.getValor("IDE_GEGRO")+")");
		tab_partida_vigente.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_partida_vigente.getValor("ide_sucu")+")");		
		tab_partida_vigente.actualizarCombosFormulario();
		tab_partida_vigente.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_partida_vigente.getValor("IDE_SUCU")+" AND IDE_GEARE="+tab_partida_vigente.getValor("IDE_GEARE")+")");
		tab_partida_vigente.actualizarCombosFormulario();
		cargarNombreCargoPartidaActual();
	}

	public void cargarCargoFuncional(AjaxBehaviorEvent evt){		
		tab_empleado_departamento.modificar(evt);
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_empleado_departamento.getValor("IDE_GEGRO")+")");
		tab_empleado_departamento.getColumna("GEN_IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=FALSE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_empleado_departamento.getValor("IDE_GEGRO")+")");		
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " +
				"from GEN_PARTIDA_GRUPO_CARGO pgc " +
				"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP " +
				"where ide_gegro="+tab_empleado_departamento.getValor("IDE_GEGRO")+" " +
				"and IDE_GECAF="+tab_empleado_departamento.getValor("IDE_GECAF")+" " +
				"and IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" " +
				"and ide_gedep="+tab_empleado_departamento.getValor("IDE_GEDEP")+" " +
				"and IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+" ");		
		utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");

	}

	public void cargarCargoDepartamentos(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" AND IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+")");		
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " +
				"from GEN_PARTIDA_GRUPO_CARGO pgc " +
				"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP " +
				"where ide_gegro="+tab_empleado_departamento.getValor("IDE_GEGRO")+" " +
				"and IDE_GECAF="+tab_empleado_departamento.getValor("IDE_GECAF")+" " +
				"and IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" " +
				"and ide_gedep="+tab_empleado_departamento.getValor("IDE_GEDEP")+" " +
				"and IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+" ");
		utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");		
	}

	public void cargarCargoAreas(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA", "IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("ide_sucu")+")");		
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " +
				"from GEN_PARTIDA_GRUPO_CARGO pgc " +
				"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP " +
				"where ide_gegro="+tab_empleado_departamento.getValor("IDE_GEGRO")+" " +
				"and IDE_GECAF="+tab_empleado_departamento.getValor("IDE_GECAF")+" " +
				"and IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" " +
				"and ide_gedep="+tab_empleado_departamento.getValor("IDE_GEDEP")+" " +
				"and IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+" ");
		utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");
	}
 
	public void cargarPartidaGrupoCargo(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " +
				"from GEN_PARTIDA_GRUPO_CARGO pgc " +
				"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP " +
				"where ide_gegro="+tab_empleado_departamento.getValor("IDE_GEGRO")+" " +
				"and IDE_GECAF="+tab_empleado_departamento.getValor("IDE_GECAF")+" " +
				"and IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" " +
				"and ide_gedep="+tab_empleado_departamento.getValor("IDE_GEDEP")+" " +
				"and IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+" ");
		System.out.println("imprimo metodo  tab_empleado_departamento "+tab_empleado_departamento.getSql());
		utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");
	}

	private boolean isSecuencialValido(String anio,String numero){
		if(numero!=null){
			TablaGenerica tab_valida=utilitario.consultar("SELECT  * FROM  GEN_DETALLE_EMPLEADO_DEPARTAME where  SECUENCIAL_GEDED="+numero+" and  to_char (FECHA_INGRESO_GEDED,'YYYY')  ='"+anio+"'");
			if(tab_valida.isEmpty()){
				return true;
			}
		}
		return false;
	}




	private String getMaximoSecuencial(int anio){
		String str_max="1";
		List lis_max=utilitario.getConexion().consultar("SELECT  MAX(SECUENCIAL_GEDED) FROM  GEN_DETALLE_EMPLEADO_DEPARTAME where to_char (FECHA_INGRESO_GEDED,'YYYY')  ='"+anio+"'");
		if(lis_max!=null){
			try {
				str_max= String.valueOf(Integer.parseInt(lis_max.get(0).toString())+1);
			} catch (Exception e) {
				// TODO: handle exception
				str_max="1";
			}
		}		
		return str_max;
	}

	public void filtrarEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		if(aut_empleado.getValor()!=null){		

			tab_partida_vigente.setCondicion("IDE_GTEMP=" + aut_empleado.getValor()
					+ " AND IDE_GEDED in (SELECT IDE_GEDED FROM GEN_DETALLE_EMPLEADO_DEPARTAME WHERE ACTIVO_GEDED=true and IDE_GTEMP=" + aut_empleado.getValor()+")");
			tab_partida_vigente.ejecutarSql();
			tab_deta_empleado_depar.setCondicion("IDE_GTEMP=" + aut_empleado.getValor());
			tab_deta_empleado_depar.ejecutarSql();
			tab_empleado_departamento.setValorForanea(tab_deta_empleado_depar.getValorSeleccionado());			
			tab_empleado_departamento.getColumna("IDE_GTEMP")
			.setValorDefecto(aut_empleado.getValor());
			if(!tab_deta_empleado_depar.isEmpty()){
				cargarTablaPartida(tab_deta_empleado_depar.getValor("IDE_GEAME"),false);
				utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");
			}			
			tab_deta_empleado_depar.getColumna("IDE_GTEMP")
			.setValorDefecto(aut_empleado.getValor());
			actualizarCombosDepartamentoActivoEmpleado();
			System.out.println("Imprime SQL");
			tab_deta_empleado_depar.imprimirSql();
		}
		else{
			tab_partida_vigente.limpiar();
			tab_deta_empleado_depar.limpiar();
		}
		utilitario.addUpdate("tab_tabulador:tab_partida_vigente,tab_tabulador:tab_empleado_departamento,tab_tabulador:tab_deta_empleado_depar");
	}

	private void cargarTablaPartida(String IDE_GEAME,boolean boo_terminacion_subroga) {


		if(IDE_GEAME!=null){

			TablaGenerica tab_accion = utilitario
					.consultar("SELECT * FROM GEN_ACCION_MOTIVO_EMPLEADO WHERE IDE_GEAME="
							+ IDE_GEAME);
			String str_campos_visibles = "";
			String str_campos_importa = "";

			if (!tab_accion.isEmpty()) {
				if (tab_accion.getValor("CAMPO_VISIBLE_GEAME") != null) {
					str_campos_visibles = tab_accion.getValor("CAMPO_VISIBLE_GEAME");
				}
				if (tab_accion.getValor("CAMPO_DATO_GEAME") != null) {
					str_campos_importa = tab_accion.getValor("CAMPO_DATO_GEAME");
				}
			}

			tab_empleado_departamento.getChildren().clear();

			String[] str_cv = str_campos_visibles.split(",");

			for (int i = 0; i < tab_empleado_departamento.getTotalColumnas(); i++) {
				boolean boo_encontro = false;
				if (!str_campos_visibles.isEmpty()) {
					tab_empleado_departamento.getColumnas()[i].setVisible(true);
					tab_empleado_departamento.getColumnas()[i].setLectura(false);
					for (int j = 0; j < str_cv.length; j++) {
						String str_nombre = tab_empleado_departamento.getColumnas()[i]
								.getNombre();
						String str_nombre_visual = tab_empleado_departamento
								.getColumnas()[i].getNombreVisual();

						String str_aux=str_cv[j].trim();
						if(str_aux.trim().endsWith("*") ||str_aux.trim().startsWith("*")){
							//Si empieza o termina con * se considera q es de lectura						
							str_aux=str_aux.replace("*", "").trim();							
							tab_empleado_departamento.getColumna(str_aux).setLectura(true);
						}

						if (str_aux.equalsIgnoreCase(str_nombre.trim())
								||str_aux.trim().equalsIgnoreCase(
										str_nombre_visual.trim())) {
							boo_encontro = true;
							break;
						}
					}
					tab_empleado_departamento.getColumnas()[i]
							.setVisible(boo_encontro);
				} else {
					tab_empleado_departamento.getColumnas()[i].setVisible(true);
					tab_empleado_departamento.getColumnas()[i].setLectura(false);
				}
			}

			tab_empleado_departamento.getColumna("IDE_GTEMP").setVisible(false);
			if (tab_deta_empleado_depar.isFilaInsertada() && boo_terminacion_subroga){
				tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO)");	
				tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL)");
//				tab_empleado_departamento.actualizarCombosFormulario();
				tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL )");
//				tab_empleado_departamento.actualizarCombosFormulario();
				cargarNombreCargoPartidaSeleccionada();
			}

			
			tab_empleado_departamento.dibujar();	
			
			
			if(tab_deta_empleado_depar.isFilaInsertada() && !boo_terminacion_subroga){
				actualizarCombosDepartamentoEmpleadoconActual();	
			}
			else if (!boo_terminacion_subroga) {
				//Hace que no le permita editar la partida
				//tab_empleado_departamento.getFilaSeleccionada().setLectura(true);
				actualizarCombosDepartamentoEmpleado();
			}

			if(tab_deta_empleado_depar.isFilaInsertada() && tab_empleado_departamento.isEmpty()){
				tab_empleado_departamento.limpiar();
				tab_empleado_departamento.insertar();					
				// Extrae valores de la partida activa			
				str_cv = str_campos_importa.split(",");			
				for (int i = 0; i < tab_empleado_departamento.getTotalColumnas(); i++) {			
					String str_nombre = tab_empleado_departamento.getColumnas()[i]
							.getNombre();
					String str_nombre_visual = tab_empleado_departamento
							.getColumnas()[i].getNombreVisual();
					if (!str_campos_importa.isEmpty()) {
						for (int j = 0; j < str_cv.length; j++) {
							String str_aux=str_cv[j].trim();					
							if (str_aux.equalsIgnoreCase(str_nombre.trim())
									|| str_aux.equalsIgnoreCase(
											str_nombre_visual.trim())) {
								if(!str_nombre.equalsIgnoreCase("IDE_GEEDP")){
									tab_empleado_departamento.setValor(str_nombre,
											tab_partida_vigente.getValor(str_nombre));	
								}						
								break;
							}
						}		
					} else {
						try {
							//IMporta todos los campos menos IDE_GEEDP 
							if(!str_nombre.equalsIgnoreCase("IDE_GEEDP")){
								tab_empleado_departamento.setValor(str_nombre,
										tab_partida_vigente.getValor(str_nombre));	
							}					
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				}
				cargarNombreCargoPartidaSeleccionada();
				TablaGenerica tab_accion_emp=utilitario.consultar("SELECT * FROM GEN_ACCION_MOTIVO_EMPLEADO WHERE IDE_GEAME="+IDE_GEAME);
				if (tab_accion_emp.getValor("IDE_GEAED").equalsIgnoreCase(utilitario.getVariable("p_gen_accion_empl_comision"))){

					tab_empleado_departamento.setValor("ACTIVO_GEEDP","false");
					tab_empleado_departamento.setValor("IDE_GECAE",utilitario.getVariable("p_gen_status_stand_by"));
					tab_empleado_departamento.getColumna("IDE_GECAE").setLectura(true);
				}else{
					tab_empleado_departamento.setValor("ACTIVO_GEEDP","true");
				}



				utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");	
			}	

		}

	}


	public Tabla getTab_asi_vac() {
		return tab_asi_vac;
	}


	public void setTab_asi_vac(Tabla tab_asi_vac) {
		this.tab_asi_vac = tab_asi_vac;
	}


	public void dibujarDialogoAsistenciaVacacion(TablaGenerica tab_periodo_vacacion_maximo){
		dia_asi_vacacion.setTitle("PERIODO DE VACACION EXISTENTE");
		dia_asi_vacacion.getBot_aceptar().setMetodo("aceptarPeriodoVacacion");
		Grid gri=new Grid();
		List lista1 = new ArrayList();
		Object fila1[] = {
				"0", "ACTIVAR PERIODO"
		};
		Object fila2[] = {
				"1", "CREAR NUEVO PERIODO DE VACACION"
		};
		lista1.add(fila1);
		lista1.add(fila2);
		rad_dia_asi_vacacion.setRadio(lista1);	
		rad_dia_asi_vacacion.setVertical();
		rad_dia_asi_vacacion.setValue("1");

		tab_asi_vac.setId("tab_asi_vac");
		tab_asi_vac.setSql(tab_periodo_vacacion_maximo.getSql());
		tab_asi_vac.setCampoPrimaria("IDE_ASVAC");
		tab_asi_vac.setNumeroTabla(10);
		tab_asi_vac.setLectura(true);
		tab_asi_vac.dibujar();


		gri.getChildren().add(rad_dia_asi_vacacion);
		gri.getChildren().add(tab_asi_vac);
		dia_asi_vacacion.setDialogo(gri);
		dia_asi_vacacion.dibujar();
		utilitario.addUpdate("dia_asi_vacacion");
	}

	public void dibujarContratoTerminacionSubrogacion(){

		cargarTablaPartida(tab_deta_empleado_depar.getValor("IDE_GEAME"),true);
		
		
		TablaGenerica tab_contrato_anterior=utilitario.consultar("SELECT * FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEDED in ( " +
				"SELECT IDE_GEDED FROM GEN_DETALLE_EMPLEADO_DEPARTAME WHERE IDE_GEDED IN ( " +
				"select GEN_IDE_GEDED from GEN_DETALLE_EMPLEADO_DEPARTAME " +
				"where IDE_GTEMP="+aut_empleado.getValor()+" and ACTIVO_GEDED=TRUE))");
		
		System.out.println("tab con "+tab_contrato_anterior.getSql());
		tab_empleado_departamento.setValor("IDE_GEPGC", tab_contrato_anterior.getValor("IDE_GEPGC"));

		tab_empleado_departamento.setValor("IDE_GEGRO", tab_contrato_anterior.getValor("IDE_GEGRO"));
		tab_empleado_departamento.setValor("IDE_GECAF", tab_contrato_anterior.getValor("IDE_GECAF"));

		tab_empleado_departamento.setValor("IDE_GEDEP", tab_contrato_anterior.getValor("IDE_GEDEP"));

		tab_empleado_departamento.setValor("IDE_SUCU", tab_contrato_anterior.getValor("IDE_SUCU"));
		tab_empleado_departamento.setValor("IDE_GEARE", tab_contrato_anterior.getValor("IDE_GEARE"));
		tab_empleado_departamento.setValor("IDE_GTTEM", tab_contrato_anterior.getValor("IDE_GTTEM"));
		tab_empleado_departamento.setValor("IDE_GTTSI", tab_contrato_anterior.getValor("IDE_GTTSI"));
		tab_empleado_departamento.setValor("IDE_GTTCO", tab_contrato_anterior.getValor("IDE_GTTCO"));
		tab_empleado_departamento.setValor("IDE_GTGRE", tab_contrato_anterior.getValor("IDE_GTGRE"));
		tab_empleado_departamento.setValor("IDE_GETIV", tab_contrato_anterior.getValor("IDE_GETIV"));
		tab_empleado_departamento.setValor("IDE_GECAE", tab_contrato_anterior.getValor("IDE_GECAE"));
		tab_empleado_departamento.setValor("FECHA_GEEDP", tab_contrato_anterior.getValor("FECHA_GEEDP"));
		
		
		
	}
	
	private Radio rad_dia_asi_vacacion=new Radio();
	private Tabla tab_asi_vac=new Tabla();
	public void cambioAccion(SelectEvent evt) {
		tab_deta_empleado_depar.modificar(evt);

		if (p_gen_encargo_posicion==null || p_gen_encargo_posicion.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede realizar la accion de personal", "Importar el paramtero de sistema p_gen_encargo_posicion");
			return;
		}

		if (p_gen_accion_contratacion==null || p_gen_accion_contratacion.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede realizar la accion de personal", "Importar el paramtero de sistema p_gen_accion_contratacion");
			return;
		}

		if (p_gen_terminacion_encargo_posicion==null || p_gen_terminacion_encargo_posicion.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede realizar la accion de personal", "Importar el paramtero de sistema p_gen_terminacion_encargo_posicion");
			return;
		}
		
				
		if (tab_deta_empleado_depar.getValor("IDE_GEAME") != null) {

//			cargarTablaPartida(tab_deta_empleado_depar.getValor("IDE_GEAME"));

			if (tab_deta_empleado_depar.getValor("IDE_GEAME").equals(p_gen_terminacion_encargo_posicion)) {
				
				dibujarContratoTerminacionSubrogacion();
				return;
			}else if (tab_deta_empleado_depar.getValor("IDE_GEAME").equals(p_gen_encargo_posicion)) {
				// ENCARGO POSICION
				set_encargo.getBot_aceptar().setMetodo("aceptarEncargo");
				set_encargo.getBot_cancelar().setMetodo("cancelarEncargo");
				set_encargo.dibujar();
				utilitario.addUpdate("set_encargo");
				return;
			}else if (tab_deta_empleado_depar.getValorArreglo("IDE_GEAME",1).equals(p_gen_accion_contratacion)){
				// PARA INGRESO O ACTUALIZACION DE PERIODO DE VACACION SOLO CUANDO ES UNA CONTRATACION  
			//comentado porque no controlam os asistencia
                                /*	utilitario.getConexion().getSqlPantalla().clear();
				TablaGenerica tab_periodo_vacacion_maximo=ser_asistencia.getAsiVacacionMaximoPeriodo(aut_empleado.getValor());
				if (tab_periodo_vacacion_maximo.getTotalFilas()>0){
					String activo_asvac=tab_periodo_vacacion_maximo.getValor("ACTIVO_ASVAC");
					if (activo_asvac!=null && !activo_asvac.isEmpty() 
							&& (activo_asvac.equalsIgnoreCase("1") || activo_asvac.equalsIgnoreCase("true"))){
						utilitario.agregarMensaje("Ya existe un periodo de vacacion activo", "Se usara el periodo activo");
					}else{
						dibujarDialogoAsistenciaVacacion(tab_periodo_vacacion_maximo);
						return;
					}
				}else{
					str_fecha_ingreso_asvac=ser_empleado.getEmpleado(aut_empleado.getValor()).getValor("FECHA_INGRESO_GTEMP");
					dia_calendario.setTitle("Seleccione Fecha de Ingreso de Periodo de Vacacion");
					dia_calendario.setWidth("35%");
					dia_calendario.setHeight("20%");
					cal_fecha_ingreso_vac.setValue(utilitario.getFecha(str_fecha_ingreso_asvac));
					Grid gri=new Grid();
					gri.setColumns(2);
					gri.getChildren().add(new Etiqueta("Fecha Ingreso: "));
					gri.getChildren().add(cal_fecha_ingreso_vac);
					dia_calendario.setDialogo(gri);
					dia_calendario.getBot_aceptar().setMetodo("aceptarCrearPeriodoVacacion");
					dia_calendario.dibujar();
					utilitario.addUpdate("dia_calendario");
				}
                                */
			}else {
				// PARA FINIQUITOS
				List lis_finiquito=new ArrayList();		
				lis_finiquito=utilitario.getConexion().consultar("SELECT finiquito_contrato_geaed FROM gen_accion_empleado_depa where ide_geaed in (select ide_geaed from  gen_accion_motivo_empleado where ide_geame="+tab_deta_empleado_depar.getValor("IDE_GEAME")+")");
				if(lis_finiquito.get(0)!=null && !lis_finiquito.get(0).toString().isEmpty()){
					if(lis_finiquito.get(0).toString().equals("true")){				
						cal_terminacion.setValue(null);
						are_tex_observacion.setValue(null);
						dia_terminacion.getBot_aceptar().setMetodo("cargarFechaTerminacion");
						dia_terminacion.dibujar();
						utilitario.addUpdate("dia_terminacion");
						return;
					}
				}
			}
			// aqui cambie
			cargarTablaPartida(tab_deta_empleado_depar.getValor("IDE_GEAME"),false);
		}
	}


	public Dialogo getDia_asi_vacacion() {
		return dia_asi_vacacion;
	}


	public void setDia_asi_vacacion(Dialogo dia_asi_vacacion) {
		this.dia_asi_vacacion = dia_asi_vacacion;
	}


	public void aceptarCrearPeriodoVacacion(){

		if (cal_fecha_ingreso_vac.getValue()==null || cal_fecha_ingreso_vac.getValue().toString().isEmpty()){
			utilitario.agregarMensajeInfo("Debe seleccionar una fecha de ingreso de periodo de vacacion", "");
			return;
		}


		System.out.println("date "+cal_fecha_ingreso_vac.getDate()+" value "+cal_fecha_ingreso_vac.getValue());
		System.out.println("formato fecha "+utilitario.getFormatoFecha(cal_fecha_ingreso_vac.getValue()));
		System.out.println("str fecha_ingreso "+str_fecha_ingreso_asvac);

		if (!utilitario.isFechaValida(utilitario.getFormatoFecha(cal_fecha_ingreso_vac.getValue()))){
			utilitario.agregarMensajeInfo("Fecha invalida", "");
			return;
		}

		if (str_fecha_ingreso_asvac==null || str_fecha_ingreso_asvac.isEmpty()){
			utilitario.agregarMensajeInfo("no se puede continuar ", "No existe fecha de ingreso en la ficha del empleado");
			return ;

		}	
		//if (utilitario.isFechaMenor(utilitario.getFecha(utilitario.getFormatoFecha(cal_fecha_ingreso_vac.getValue())), utilitario.getFecha(str_fecha_ingreso_asvac))){
		//	utilitario.agregarMensajeInfo("La fecha de ingreso de periodo no puede ser menor que la fecha de ingreso ", str_fecha_ingreso_asvac);
		//	return ;
		//}
		
/* comentado por la u salesiana
		ser_asistencia.crearPeriodoVacacion(aut_empleado.getValor(), utilitario.getFormatoFecha(cal_fecha_ingreso_vac.getValue()));
		dia_calendario.cerrar();
 */               
		dia_asi_vacacion.cerrar();


	}


	String str_fecha_ingreso_asvac="";
	public void aceptarPeriodoVacacion(){
		System.out.println("radio "+rad_dia_asi_vacacion.getValue());
		if (rad_dia_asi_vacacion.getValue().equals("1")){

			str_fecha_ingreso_asvac=tab_asi_vac.getValor("FECHA_INGRESO_ASVAC");

			dia_calendario.setTitle("Seleccione Fecha de Ingreso de Periodo de Vacacion");
			dia_calendario.setWidth("35%");
			dia_calendario.setHeight("20%");
			cal_fecha_ingreso_vac.setValue(utilitario.getFecha(str_fecha_ingreso_asvac));
			Grid gri=new Grid();
			gri.setColumns(2);
			gri.getChildren().add(new Etiqueta("Fecha Ingreso: "));
			gri.getChildren().add(cal_fecha_ingreso_vac);
			dia_calendario.setDialogo(gri);
			dia_calendario.getBot_aceptar().setMetodo("aceptarCrearPeriodoVacacion");
			dia_calendario.dibujar();
			utilitario.addUpdate("dia_calendario");


		}
		if (rad_dia_asi_vacacion.getValue().equals("0")){
			//			if (ser_asistencia.activarPeriodoVacacion(tab_asi_vac.getValor("IDE_ASVAC"))){
			//				utilitario.agregarMensaje("El periodo "+tab_asi_vac.getValor("IDE_ASVAC")+" se activo correctamente", "");
			/* comentado por u salesiana
                        utilitario.getConexion().agregarSqlPantalla(ser_asistencia.getSqlActivarPeriodoVacacion(tab_asi_vac.getValor("IDE_ASVAC")));
			*/
                        dia_asi_vacacion.cerrar();
			return;
			//			}
		}

	}


	public void cargarFechaTerminacion(){
		if(cal_terminacion.getValue()!=null && !cal_terminacion.getValue().toString().isEmpty()){			
			
			cargarTablaPartida(tab_deta_empleado_depar.getValor("IDE_GEAME"),false);
			tab_empleado_departamento.setValor("FECHA_LIQUIDACION_GEEDP", utilitario.getFormatoFecha(utilitario.getFecha(utilitario.getFormatoFecha(cal_terminacion.getValue()))));
			tab_empleado_departamento.setValor("LIQUIDACION_GEEDP", "1");
			tab_empleado_departamento.setValor("ACTIVO_GEEDP","false");
			tab_empleado_departamento.getColumna("LIQUIDACION_GEEDP").setLectura(true);
			tab_empleado_departamento.setValor("IDE_GECAE","1");
			tab_empleado_departamento.getColumna("IDE_GECAE").setLectura(true);
			tab_empleado_departamento.setValor("OBSERVACION_GEEDP", are_tex_observacion.getValue().toString());
			utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");			
			dia_terminacion.cerrar();
		}else{
			utilitario.agregarMensajeInfo("No se puede continuar", "Debe ingresar la fecha");
		}
	}


	public void aceptarEncargo() {
		if (set_encargo.getValorSeleccionado() != null) {
			if(tab_partida_vigente.getValor("IDE_GEDED")!=null){

				// aqui cambie
				cargarTablaPartida(tab_deta_empleado_depar.getValor("IDE_GEAME"),false);
				//
				
				tab_empleado_departamento.setValor("ide_gepgc",
						set_encargo.getValorSeleccionado());	
				set_encargo.getTab_seleccion().setFilaActual(set_encargo.getValorSeleccionado());
				//CARGO  LOS DATOS DEL PUSTO ENCARGADO
				tab_deta_empleado_depar.setValor("GEN_IDE_GEDED", tab_partida_vigente.getValor("IDE_GEDED"));
				actualizarCombosDepartamentoEmpleado(set_encargo.getTab_seleccion().getValor("ide_sucu"), set_encargo.getTab_seleccion().getValor("IDE_GEGRO"), set_encargo.getTab_seleccion().getValor("IDE_GEARE"));
				tab_empleado_departamento.setValor("FECHA_ENCARGO_GEEDP", utilitario.getFechaActual());
				tab_empleado_departamento.setValor("SUELDO_SUBROGA_GEEDP", set_encargo.getTab_seleccion().getValor("salario_encargo_gepgc"));
				double dou_ajuste=0;
				try {
					//Calcula el ajueste de
					dou_ajuste=(Double.parseDouble(tab_empleado_departamento.getValor("SUELDO_SUBROGA_GEEDP")))-(Double.parseDouble(tab_empleado_departamento.getValor("RMU_GEEDP")));
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(dou_ajuste<0){
					dou_ajuste=0;
				}
				tab_empleado_departamento.setValor("AJUSTE_SUELDO_GEEDP", utilitario.getFormatoNumero(dou_ajuste));
				utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");
				set_encargo.cerrar();				
			}
			else{
				utilitario.agregarMensajeError("No se puede Completar la Acción", "El empleado seleccionado no tiene una Acción activa");
			}
		}
	}



	public Confirmar getCon_guardar() {
		return con_guardar;
	}


	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}




	public Dialogo getDia_calendario() {
		return dia_calendario;
	}


	public void setDia_calendario(Dialogo dia_calendario) {
		this.dia_calendario = dia_calendario;
	}


	public void cancelarEncargo() {
		set_encargo.cerrar();
		tab_empleado_departamento.limpiar();
		utilitario.addUpdate("tab_tabulador:tab_empleado_departamento");
	}

	@Override
	public void insertar() {
		if(aut_empleado.getValor()!=null){
			tab_deta_empleado_depar.insertar();	
			eti_cargo_accion.setValue("");
			tab_deta_empleado_depar.setValor("SECUENCIAL_GEDED", getMaximoSecuencial(utilitario.getAnio(utilitario.getFechaActual())));
			if(!tab_partida_vigente.isEmpty()){
				//Guardo la partida Anterior
				tab_deta_empleado_depar.setValor("GEN_IDE_GEDED", tab_partida_vigente.getValor("IDE_GEDED"));	
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede Insertar", "Debe seleccionar un Empleado");
		}
	}

	@Override
	public void guardar() {		
		if(aut_empleado.getValor()==null){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe seleccionar un empleado");
			return;
		}

		//if (tab_deta_empleado_depar.getValor("FECHA_INGRESO_GEDED")==null || tab_deta_empleado_depar.getValor("FECHA_INGRESO_GEDED").isEmpty()){
		//	utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de ingreso de la accion no puede ser nula o vacia");
		//	return;
	//	}
		
	//	if (tab_deta_empleado_depar.getTotalFilas()>1){
	//	String ide_geedp_activo=utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ide_geded in ( select ide_geded from GEN_DETALLE_EMPLEADO_DEPARTAME where IDE_GTEMP="+aut_empleado.getValor()+" and ACTIVO_GEDED=TRUE )").getValor("ide_geedp");
	//	String fecha_accion=tab_deta_empleado_depar.getValor("FECHA_INGRESO_GEDED");
		
	//	String str_valida_accion=ser_nomina.validarAccionPersonalPermitida(ide_geedp_activo,fecha_accion);
	//	System.out.println("str_valida "+str_valida_accion);
	//	if (str_valida_accion.startsWith("No")){
	//		utilitario.agregarNotificacionInfo("No se puede guardar", str_valida_accion);
	//		return;
		//}
	//	}
		
		
		if (tab_deta_empleado_depar.getTotalFilas()>1){
			/*aqui se valida la fecha de contrato con acciones de personal anteriores*/
			TablaGenerica tab_fecha=utilitario.consultar("select ide_gtemp,max (FECHA_INGRESO_GEDED) as fecha_ingreso_accion " +
					"from GEN_DETALLE_EMPLEADO_DEPARTAME where ide_gtemp="+aut_empleado.getValor()+" group by ide_gtemp");

		//if(tab_fecha.getTotalFilas()>0){
		//	if(tab_fecha.getValor("fecha_ingreso_accion")!=null && !tab_fecha.getValor("fecha_ingreso_accion").isEmpty() ){
		//		if(tab_deta_empleado_depar.getValor("FECHA_INGRESO_GEDED")!=null && !tab_deta_empleado_depar.getValor("FECHA_INGRESO_GEDED").isEmpty()){
			//		if(utilitario.isFechaMayor(utilitario.getFecha(tab_fecha.getValor("fecha_ingreso_accion")), utilitario.getFecha(tab_deta_empleado_depar.getValor("FECHA_INGRESO_GEDED")))){
			//			utilitario.agregarNotificacionInfo("No se puede Guardar", "La fecha de ingreso de accion no puede ser menor que la ultima fecha de ingreso de accion "+tab_fecha.getValor("fecha_ingreso_accion"));
			//			return;
			//		}	
			//	}
		//	}
		}
		//}

		
		
		if(tab_deta_empleado_depar.isFilaInsertada()){
			if(!isSecuencialValido(tab_deta_empleado_depar.getValor("FECHA_INGRESO_GEDED"), tab_deta_empleado_depar.getValor("SECUENCIAL_GEDED"))){
				return;
			}
		}



		if(tab_empleado_departamento.getValor("fecha_finctr_geedp")!=null && !tab_empleado_departamento.getValor("fecha_finctr_geedp").isEmpty()){
			
			if(tab_empleado_departamento.getValor("fecha_geedp")!=null && !tab_empleado_departamento.getValor("fecha_geedp").isEmpty()){
			
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento.getValor("fecha_finctr_geedp")), utilitario.getFecha(tab_empleado_departamento.getValor("fecha_geedp")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha fin de contrato no puede ser menor que la fecha contrato");
				return;
			}	
			}
		}					


		if(tab_empleado_departamento.getValor("fecha_encargo_fin_geedp")!=null && !tab_empleado_departamento.getValor("fecha_encargo_fin_geedp").isEmpty()){
			if(tab_empleado_departamento.getValor("fecha_geedp")!=null && !tab_empleado_departamento.getValor("fecha_geedp").isEmpty()){

			if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento.getValor("fecha_encargo_fin_geedp")), utilitario.getFecha(tab_empleado_departamento.getValor("fecha_geedp")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha encargo fin de contrato no puede ser menor que la fecha contrato");
				return;
			}
			}
		}

		if(tab_empleado_departamento.getValor("fecha_encargo_geedp")!=null && !tab_empleado_departamento.getValor("fecha_encargo_geedp").isEmpty()){
			if(tab_empleado_departamento.getValor("fecha_geedp")!=null && !tab_empleado_departamento.getValor("fecha_geedp").isEmpty()){

			if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento.getValor("fecha_encargo_geedp")), utilitario.getFecha(tab_empleado_departamento.getValor("fecha_geedp")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha encargo inicio no puede ser menor que la fecha contrato");
				return;
			}
			}
		}					

		if(tab_empleado_departamento.getValor("fecha_encargo_geedp")!=null && !tab_empleado_departamento.getValor("fecha_encargo_geedp").isEmpty()){
			if(tab_empleado_departamento.getValor("fecha_encargo_fin_geedp")!=null && !tab_empleado_departamento.getValor("fecha_encargo_fin_geedp").isEmpty()){
				if (utilitario.isFechaMayor(utilitario.getFecha(tab_empleado_departamento.getValor("fecha_encargo_geedp")), utilitario.getFecha(tab_empleado_departamento.getValor("fecha_encargo_fin_geedp")))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de encargo inicial no puede ser mayor que la fecha encargo fin");
					return;
				}	
			}						
		}

		if(tab_empleado_departamento.getValor("fecha_ajuste_geedp")!=null && !tab_empleado_departamento.getValor("fecha_ajuste_geedp").isEmpty()){
			if(tab_empleado_departamento.getValor("fecha_geedp")!=null && !tab_empleado_departamento.getValor("fecha_geedp").isEmpty()){

			if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento.getValor("fecha_ajuste_geedp")), utilitario.getFecha(tab_empleado_departamento.getValor("fecha_geedp")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha ajuste de contrato no puede ser menor que la fecha contrato");
				return;
			}
			}
		}



		/*aqui se valida la fecha de contrato con acciones de personal anteriores*/
		TablaGenerica tab_fecha=utilitario.consultar("select ide_gtemp,max (fecha_geedp) as fecha_contrato from gen_empleados_departamento_par where ide_gtemp="+aut_empleado.getValor()+" group by ide_gtemp");
		
		if (tab_deta_empleado_depar.getTotalFilas()>1){
		System.out.println("valor"+aut_empleado.getValor());
		if(tab_fecha.getTotalFilas()>0){
			if(tab_fecha.getValor("fecha_contrato")!=null && !tab_fecha.getValor("fecha_contrato").isEmpty() ){
				if(tab_empleado_departamento.getValor("fecha_geedp")!=null && !tab_empleado_departamento.getValor("fecha_geedp").isEmpty()){
					if(utilitario.isFechaMayor(utilitario.getFecha(tab_fecha.getValor("fecha_contrato")), utilitario.getFecha(tab_empleado_departamento.getValor("fecha_geedp")))){
						utilitario.agregarMensajeInfo("No se puede Guardar", "La fecha de contrato actual no puede ser menor que la fecha de contrato del anterior accion de personal");
						return;
					}	
				}
			}
		}
		}

		TablaGenerica tab_pv=utilitario.consultar("select * from ("+tab_partida_vigente.getSql()+")a order by a.IDE_GEDED desc ");
		String ide_geded_ultimo=tab_pv.getValor("IDE_GEDED");

try {
	if (Long.parseLong(tab_empleado_departamento.getValor("IDE_GEDED"))<Long.parseLong(ide_geded_ultimo)){
		utilitario.agregarMensajeInfo("no se puede guardar", "la accion seleccionada no se encuentra vigente");
		return;
	}

} catch (Exception e) {
	// TODO: handle exception
}
		

		if(tab_empleado_departamento.isFilaInsertada()){
			//Desactiva todas las partidas y solo deja activa la nueva		    	
			utilitario.getConexion().agregarSql("UPDATE GEN_DETALLE_EMPLEADO_DEPARTAME set ACTIVO_GEDED=false WHERE IDE_GTEMP="+aut_empleado.getValor());
			utilitario.getConexion().agregarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set ACTIVO_GEEDP=false WHERE IDE_GTEMP="+aut_empleado.getValor());				
		}

		String str_ide_gepgc_ant=ser_gestion.getPartidaGrupoEmpleado(tab_empleado_departamento.getValorSeleccionado()).getValor("IDE_GEPGC");
		boolean boo_cambio_partida=ser_gestion.isCambioPartida(tab_empleado_departamento.getValorSeleccionado(), tab_empleado_departamento.getValor("IDE_GEPGC"));
		String str_gepgc_vacante=ser_gestion.getPartidaGrupo(tab_empleado_departamento.getValor("IDE_GEPGC")).getValor("VACANTE_GEPGC");




		guardarAccion();			

	}

	public void guardarAccion(){
		if(tab_deta_empleado_depar.guardar()){
			if(tab_empleado_departamento.guardar()){
				
				utilitario.getConexion().agregarSqlPantalla("update GEN_PARTIDA_GRUPO_CARGO set VACANTE_GEPGC=FALSE where IDE_GEPGC="+tab_empleado_departamento.getValor("IDE_GEPGC"));
				if(guardarPantalla().isEmpty()){

					if(ser_gestion.validarAccionFiniquito(tab_deta_empleado_depar.getValor(tab_deta_empleado_depar.getFilaActual(), "IDE_GEAME"))){
						if(ser_gestion.inactivarEmpleado(tab_empleado_departamento.getValorSeleccionado())){
/* comentado por u salesiana
							ser_asistencia.desactivarPeriodoVacacion(ser_asistencia.getAsiVacacionActiva(aut_empleado.getValor()).getValor("IDE_ASVAC"),tab_empleado_departamento.getValor("FECHA_LIQUIDACION_GEEDP"));
*/
                                                        utilitario.agregarMensajeInfo("Se guardo correctamente", "Terminación del contrato");
						}
					}

					//Para que ponga el visto en activo
					String str_anterior=tab_deta_empleado_depar.getValorSeleccionado();
					tab_deta_empleado_depar.ejecutarSql();
					tab_deta_empleado_depar.setFilasSeleccionados(str_anterior);
					seleccionarTabla1();					
					tab_partida_vigente.ejecutarSql();
					actualizarCombosDepartamentoActivoEmpleado();
				}
			}
		}
	}

	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		if (tab_empleado_departamento.isFocus()){
			actualizarCombosDepartamentoEmpleado();
		}
	}

	@Override
	public void eliminar() {		
		if(tab_deta_empleado_depar.isFilaInsertada()){
			utilitario.getConexion().getSqlPantalla().clear();
			tab_empleado_departamento.eliminar();
			tab_deta_empleado_depar.eliminar();			
			cargarNombreCargoPartidaSeleccionada();
		}
		else{

			if(tab_deta_empleado_depar.getValor("ACTIVO_GEDED")!=null && tab_deta_empleado_depar.getValor("ACTIVO_GEDED").equalsIgnoreCase("true")){
				
				if (utilitario.consultar("select * from NRH_DETALLE_ROL where IDE_GEEDP="+tab_empleado_departamento.getValor("ide_geedp")).getTotalFilas()>0){
					utilitario.agregarNotificacionInfo("No se puede eliminar el registro", "La accion tiene otros registros generados");	
					return;
				}
				
				String str_anerior=getAccionAnterior();
				if(str_anerior!=null){

					if (utilitario.consultar("select * from NRH_ANTICIPO where IDE_GEEDP="+tab_empleado_departamento.getValorSeleccionado()).getTotalFilas()>0){
						utilitario.agregarMensajeInfo("No se puede eliminar","El colaborador tiene registros generados");
						return;
					}

					utilitario.getConexion().agregarSqlPantalla("DELETE FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEEDP="+tab_empleado_departamento.getValorSeleccionado());
					utilitario.getConexion().agregarSqlPantalla("DELETE FROM GEN_DETALLE_EMPLEADO_DEPARTAME WHERE IDE_GEDED="+tab_deta_empleado_depar.getValorSeleccionado());
					utilitario.getConexion().agregarSql("UPDATE GEN_DETALLE_EMPLEADO_DEPARTAME set ACTIVO_GEDED=true WHERE IDE_GEDED="+str_anerior);
					utilitario.getConexion().agregarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set ACTIVO_GEEDP=true  WHERE IDE_GEDED="+str_anerior);

					if(guardarPantalla().isEmpty()){
						tab_deta_empleado_depar.ejecutarSql();
						seleccionarTabla1();					
						tab_partida_vigente.ejecutarSql();
						actualizarCombosDepartamentoActivoEmpleado();

						System.out.println("eliminar ide_gedep :  "+tab_empleado_departamento.getValorSeleccionado());
						ser_gestion.activarEmpleado(tab_empleado_departamento.getValorSeleccionado());

					}	
				}
				else{

					if(tab_deta_empleado_depar.getTotalFilas()==1){
						//Si solo tiene una partida creada intenta eliminar 
						utilitario.getConexion().agregarSqlPantalla("DELETE FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEEDP="+tab_empleado_departamento.getValorSeleccionado());
						utilitario.getConexion().agregarSqlPantalla("DELETE FROM GEN_DETALLE_EMPLEADO_DEPARTAME WHERE IDE_GEDED="+tab_deta_empleado_depar.getValorSeleccionado());
						if(guardarPantalla().isEmpty()){						
							tab_deta_empleado_depar.ejecutarSql();						
							tab_partida_vigente.ejecutarSql();
						}
					}
					else{
						utilitario.agregarMensajeInfo("No se puede Eliminar", "No se puede eliminar no se encontraron Acciones Anteriores");	
					}

				}
			}
			else{
				utilitario.agregarMensajeInfo("No se puede Eliminar", "Solo puede Eliminar una partida Activa");

			}
		}
	}

	public void seleccionarTabla1(SelectEvent evt){
		if(tab_deta_empleado_depar.isFilaInsertada()){
			return;
		}
		tab_deta_empleado_depar.seleccionarFila(evt);
		seleccionarTabla1();
	}

	public void seleccionarTabla1(AjaxBehaviorEvent evt){
		if(tab_deta_empleado_depar.isFilaInsertada()){
			return;
		}
		tab_deta_empleado_depar.seleccionarFila(evt);
		seleccionarTabla1();
	}

	private void seleccionarTabla1(){
		tab_empleado_departamento.ejecutarValorForanea(tab_deta_empleado_depar.getValorSeleccionado());		
		if(!tab_deta_empleado_depar.isEmpty()){			
			cargarTablaPartida(tab_deta_empleado_depar.getValor("IDE_GEAME"),false);			
		}
	}


	public Tabla getTab_partida_vigente() {
		return tab_partida_vigente;
	}

	public void setTab_partida_vigente(Tabla tab_partida_vigente) {
		this.tab_partida_vigente = tab_partida_vigente;
	}

	public Tabla getTab_empleado_departamento() {
		return tab_empleado_departamento;
	}

	public void setTab_empleado_departamento(Tabla tab_empleado_departamento) {
		this.tab_empleado_departamento = tab_empleado_departamento;
	}

	public Tabla getTab_deta_empleado_depar() {
		return tab_deta_empleado_depar;
	}

	public void setTab_deta_empleado_depar(Tabla tab_deta_empleado_depar) {
		this.tab_deta_empleado_depar = tab_deta_empleado_depar;
	}

	public SeleccionTabla getSet_encargo() {
		return set_encargo;
	}

	public void setSet_encargo(SeleccionTabla set_encargo) {
		this.set_encargo = set_encargo;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Dialogo getDia_reactiva() {
		return dia_reactiva;
	}

	public void setDia_reactiva(Dialogo dia_reactiva) {
		this.dia_reactiva = dia_reactiva;
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

	public Dialogo getDia_tipo_vinculacion() {
		return dia_tipo_vinculacion;
	}

	public void setDia_tipo_vinculacion(Dialogo dia_tipo_vinculacion) {
		this.dia_tipo_vinculacion = dia_tipo_vinculacion;
	}

	public SeleccionTabla getSel_tab_accion_motivo() {
		return sel_tab_accion_motivo;
	}

	public void setSel_tab_accion_motivo(SeleccionTabla sel_tab_accion_motivo) {
		this.sel_tab_accion_motivo = sel_tab_accion_motivo;
	}

	public Dialogo getDia_editar_motivo() {
		return dia_editar_motivo;
	}

	public void setDia_editar_motivo(Dialogo dia_editar_motivo) {
		this.dia_editar_motivo = dia_editar_motivo;
	}

	public Dialogo getDia_terminacion() {
		return dia_terminacion;
	}

	public void setDia_terminacion(Dialogo dia_terminacion) {
		this.dia_terminacion = dia_terminacion;
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		Locale locale=new Locale("es","ES");
		// TODO Auto-generated method stub
		if(rep_reporte.getReporteSelecionado().equals("Acción de Personal")){
			if(tab_deta_empleado_depar.getTotalFilas()>0){
				if(rep_reporte.isVisible()){
					p_parametros=new HashMap();
					rep_reporte.cerrar();
					rad_tipo_vinculacion.setValue(null);
					dia_tipo_vinculacion.dibujar();					
				}
				else if(dia_tipo_vinculacion.isVisible()){
					if(rad_tipo_vinculacion.getValue()!=null && !rad_tipo_vinculacion.getValue().toString().isEmpty()){
						p_parametros.put("p_tipo_vinculacion",rad_tipo_vinculacion.getValue());
						System.out.println("parametro p_tipo_vinculacion...  "+rad_tipo_vinculacion.getValue());
						String motivo="";
						motivo=tab_deta_empleado_depar.getValorSeleccionado();
						System.out.println("variable motivo...  "+motivo);
						
						if(motivo.isEmpty()){
							motivo="-1";
						}
						System.out.println("valor de tipo vinculacion: "+rad_tipo_vinculacion.getValue());						
						sel_tab_accion_motivo.getTab_seleccion().setSql("SELECT a.IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED,c.detalle_reporte_gemed FROM GEN_ACCION_MOTIVO_EMPLEADO a " +
								"LEFT JOIN ( " +
								"SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA " +
								")b ON b.IDE_GEAED=a.IDE_GEAED " +
								"LEFT JOIN ( " +
								"SELECT IDE_GEMED,DETALLE_GEMED,detalle_reporte_gemed FROM GEN_MOTIVO_EMPLEADO_DEPA " +
								")c ON c.IDE_GEMED=a.IDE_GEMED WHERE a.IDE_GEAME IN (select ide_geame from gen_detalle_empleado_departame where ide_geded= "+motivo+") " +
								"ORDER BY  b.DETALLE_GEAED,c.DETALLE_GEMED");
						sel_tab_accion_motivo.getTab_seleccion().getColumna("detalle_gemed").setFiltro(true);						
						sel_tab_accion_motivo.getTab_seleccion().ejecutarSql();
						sel_tab_accion_motivo.getTab_seleccion().setFilaActual(tab_deta_empleado_depar.getValor("IDE_GEAME"));
						sel_tab_accion_motivo.getBot_aceptar().setMetodo("aceptarReporte");
						dia_tipo_vinculacion.cerrar();
						tab_deta_empleado_depar.getStringColumna("IDE_GEAME");
						System.out.println("VALOR DED LA COMUNA IDE_GEAME: "+tab_deta_empleado_depar.getStringColumna("IDE_GEAME"));
						System.out.println("sql sel_tab_accion_motivo...   "+sel_tab_accion_motivo.getTab_seleccion().getSql());
						sel_tab_accion_motivo.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede generar el reporte de accion de personal", "Seleccione una opción");
					}
				}
				else if(sel_tab_accion_motivo.isVisible()){					
					if(sel_tab_accion_motivo.getValorSeleccionado()!=null && !sel_tab_accion_motivo.getValorSeleccionado().toString().isEmpty()){					
						p_parametros.put("p_detalle_accion",sel_tab_accion_motivo.getValorSeleccionado());	
						System.out.println("PARAMETRO p_detalle_accion...  "+sel_tab_accion_motivo.getValorSeleccionado());
						
						String a=sel_tab_accion_motivo.getTab_seleccion().getFilaSeleccionada().getCampos()[sel_tab_accion_motivo.getTab_seleccion().getNumeroColumna("detalle_reporte_gemed")]+"";
						System.out.println("valord  de...sel_tab_accion_motivo... a:  "+a);
						are_tex_motivo.setValue(a);						
						System.out.println("valor de tex area de edicion:  "+are_tex_motivo.getValue());
						sel_tab_accion_motivo.cerrar();
						dia_editar_motivo.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede generar el reporte de accion de personal", "Seleccione una opción de motivo");
					}
				}	
				else if(dia_editar_motivo.isVisible()){
					p_parametros.put("p_detalle_accion",are_tex_motivo.getValue());		
					System.out.println("PArametro p_detalle_accion...  "+are_tex_motivo.getValue());
					
					p_parametros.put("ide_geded",Long.parseLong(tab_deta_empleado_depar.getValor("ide_geded")));
					//System.out.println("PArametro ide_geded...  "+Long.parseLong(tab_deta_empleado_depar.getValor("ide_geded")));
					
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					p_parametros.put("p_gerencia_general",utilitario.getVariable("p_gerencia_general_ap"));
					//System.out.println("PArametro p_gerencia_general...  "+utilitario.getVariable("p_gerencia_general_ap"));
					
					p_parametros.put("p_gerencia_administrativa",utilitario.getVariable("p_gerencia_administrativa_ap"));
					//System.out.println("PArametro p_gerencia_administrativa...  "+utilitario.getVariable("p_gerencia_administrativa_ap"));
					
					p_parametros.put("p_cargo_gerencia_general",utilitario.getVariable("p_cargo_gerencia_general_ap"));
					//System.out.println("PArametro p_cargo_gerencia_general...  "+utilitario.getVariable("p_cargo_gerencia_general_ap"));
					
					p_parametros.put("p_cargo_gerencia_administrativa",utilitario.getVariable("p_cargo_gerencia_administrativa_ap"));
					//System.out.println("PArametro p_cargo_gerencia_administrativa...  "+utilitario.getVariable("p_cargo_gerencia_administrativa_ap"));
					
					p_parametros.put("REPORT_LOCALE", locale);
					dia_editar_motivo.cerrar();
					sef_reporte.dibujar();
					System.out.println("valor de detalle de accion: "+are_tex_motivo.getValue());
					System.out.println("valor de ide_geded: "+Long.parseLong(tab_deta_empleado_depar.getValor("ide_geded")));										
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera de la acción de personal");
			}
		}
	}	
}
