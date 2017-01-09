package paq_gestion;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.*;
import framework.reportes.ReporteDataSource;
import framework.componentes.Boton;

import org.primefaces.component.panelmenu.PanelMenu;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import sistema.parametros.Parametros;


public class pre_empleado extends Pantalla {

	private AutoCompletar aut_empleado = new AutoCompletar();
	private Tabla tab_cuenta_anticipo = new Tabla();
	private Tabla tab_anio = new Tabla();
	private Tabla tab_empleado = new Tabla();
	private Tabla tab_documentacion = new Tabla();
	private Tabla tab_conyuge = new Tabla();
	private Tabla tab_union_libre = new Tabla();
	private Tabla tab_persona_emergencia = new Tabla();
	private Tabla tab_familiar = new Tabla();
	private Tabla tab_cargas_familiares = new Tabla();
	private Tabla tab_amigos = new Tabla();
	private Tabla tab_telefonos = new Tabla();
	private Tabla tab_correos = new Tabla();
	private Tabla tab_seguro_vida = new Tabla();
	private Tabla tab_beneficiario_seguro = new Tabla();
	private Tabla tab_registro_militar = new Tabla();
	private Tabla tab_hobbies=new Tabla();
	private Tabla tab_educacion = new Tabla();
	private Tabla tab_idiomas = new Tabla();
	private Tabla tab_experiencia_docente = new Tabla();
	private Tabla tab_capacitacion = new Tabla();
	private Tabla tab_experiencia_laboral = new Tabla();
	private Tabla tab_situacion_economica = new Tabla();
	private Tabla tab_negocio_empl = new Tabla();
	private Tabla tab_paticipantes_negocio = new Tabla();
	private Tabla tab_cuenta_bancaria = new Tabla();
	private Tabla tab_situacion_financiera = new Tabla();
	private Tabla tab_inversion = new Tabla();
	private Tabla tab_endeudamiento = new Tabla();
	private Tabla tab_tarjeta_credito = new Tabla();
	private Tabla tab_empleado_departamento=new Tabla();
	private Tabla tab_direccion=new Tabla();
	private Tabla tab_terreno=new Tabla();
	private Tabla tab_casa=new Tabla();
	private Tabla tab_vehiculo=new Tabla();
	private Tabla tab_archivo_empleado=new Tabla();
	private Tabla tab_membresias=new Tabla();
	private Tabla tab_discapacidad = new Tabla();
	private Tabla tab_sri_proy_ing = new Tabla();
	private Tabla tab_sri_det_proy_ing = new Tabla();
	private Tabla tab_sri_impuesto_renta = new Tabla();
	private Tabla tab_sri_gastos_deducible = new Tabla();
	private Tabla tab_deta_empleado_depar=new Tabla();
	private PanelMenu pam_menu = new PanelMenu();
	private Panel pan_opcion = new Panel();
	private String str_opcion = "";// sirve para identificar la opcion que se encuentra dibujada en pantalla
	private Efecto efecto = new Efecto();
	private Division div_division = new Division();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private SeleccionTabla set_actualizar_cuenta=new SeleccionTabla();
	private SeleccionTabla set_cuenta_anticipo=new SeleccionTabla();
	private SeleccionTabla set_sucursal=new SeleccionTabla();
	private SeleccionTabla set_empleado_deducible=new SeleccionTabla();
	private SeleccionTabla set_departamento=new SeleccionTabla();
	private SeleccionTabla set_sri=new SeleccionTabla();
	private SeleccionTabla set_tipo_empleado=new SeleccionTabla();
	private Dialogo dia_filtro_activo = new Dialogo();
	private Map p_parametros=new HashMap();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	int a=0;
	int b=0;
	int c=0;
	private Consulta con_acciones_personal = new Consulta();
	private Dialogo dia_deta_emplea_depar=new Dialogo();
	private Tabla  tab_dia_detalle_empleado_depar=new Tabla();
	private SeleccionCalendario sel_cal_cumpleanios=new SeleccionCalendario();
	private SeleccionTabla sel_tab_area=new SeleccionTabla();
	private SeleccionTabla sel_tab_departamento =new SeleccionTabla();
	private SeleccionTabla sel_tab_sucursal=new SeleccionTabla();
	private String parametro1;
	private Confirmar con_guardar_cuenta=new Confirmar();

	private Confirmar con_guardar=new Confirmar();
	//DFJ
	private Dialogo dia_contrata=new Dialogo();
	private Tabla tab_empleado_departamento_dia=new Tabla();
	private final static String IDE_GAME="24";
	private Tabla tab_partida_cargo=new Tabla();
	public static String par_nacionalidad;
	public static String par_tipo_sangre;
	public static String par_tipo_sindicato; 
	public static String par_estado_civil_soltero; 
	



	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	public pre_empleado() {
		//parametros
		par_nacionalidad=utilitario.getVariable("p_gth_nacionalidad");
		par_tipo_sangre=utilitario.getVariable("p_gth_tipo_sangre");
		par_tipo_sindicato=utilitario.getVariable("p_gth_tipo_sindicato");
		par_estado_civil_soltero=utilitario.getVariable("p_gth_estado_civil_soltero");

		bar_botones.agregarReporte();

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE", "IDE_GTEMP");
		set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado.setTitle("SELECCION DE EMPLEADOS");
		set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);		
		agregarComponente(set_empleado);

		set_tipo_empleado.setId("set_tipo_empleado");
		set_tipo_empleado.setSeleccionTabla("GTH_TIPO_EMPLEADO", "IDE_GTTEM","DETALLE_GTTEM");
		set_tipo_empleado.getBot_aceptar().setMetodo("aceptarReporte");
		set_tipo_empleado.setTitle("SELECCION DE TIPO DE EMPLEADO");
		set_tipo_empleado.setCheck();
		agregarComponente(set_tipo_empleado);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		aut_empleado.setId("aut_empleado");		
		aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP, APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP  from GTH_EMPLEADO");
		aut_empleado.setMetodoChange("filtrarEmpleado");


		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);


		//con_ver_vacaciones.getBot_aceptar().setRendered(false);

		//tabla detale empleado
		tab_dia_detalle_empleado_depar.setId("tab_dia_detalle_empleado_depar");
		tab_dia_detalle_empleado_depar.setSql("SELECT IDE_GEDED as IDE_EMPLEA,IDE_GEINS,IDE_GEAME,FECHA_INGRESO_GEDED,FECHA_SALIDA_GEDED,OBSERVACION_GEDED,ACTIVO_GEDED " +
				"FROM GEN_DETALLE_EMPLEADO_DEPARTAME");
		tab_dia_detalle_empleado_depar.setCampoPrimaria("IDE_EMPLEA");
		tab_dia_detalle_empleado_depar.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "");
		tab_dia_detalle_empleado_depar.getColumna("IDE_GEINS").setAutoCompletar();
		tab_dia_detalle_empleado_depar.getColumna("IDE_GEAME").setCombo("SELECT a.IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED FROM GEN_ACCION_MOTIVO_EMPLEADO a " +
				"LEFT JOIN ( " +
				"SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA " +
				")b ON b.IDE_GEAED=a.IDE_GEAED " +
				"LEFT JOIN ( " +
				"SELECT IDE_GEMED,DETALLE_GEMED FROM GEN_MOTIVO_EMPLEADO_DEPA " +
				")c ON c.IDE_GEMED=a.IDE_GEMED " +
				"ORDER BY  b.DETALLE_GEAED,c.DETALLE_GEMED");	
		tab_dia_detalle_empleado_depar.getColumna("IDE_GEAME").setAutoCompletar();		
		tab_dia_detalle_empleado_depar.getColumna("ACTIVO_GEDED").setCheck();			
		//tab_dia_detalle_empleado_depar.setCondicion("IDE_GEEDP =" + tab_empleado_departamento.getValor("IDE_GEEDP"));
		//tab_dia_detalle_empleado_depar.setTipoFormulario(true);
		tab_dia_detalle_empleado_depar.getGrid().setColumns(4);
		tab_dia_detalle_empleado_depar.setScrollable(true);
		tab_dia_detalle_empleado_depar.setScrollHeight(60);
		tab_dia_detalle_empleado_depar.setLectura(true);


		///BOTON AGREGAR CUENTA ANTICIPO
		Boton bot_cuenta_anticipo = new Boton();
		bot_cuenta_anticipo.setValue("Agregar Cuenta Anticipo");
		bot_cuenta_anticipo.setTitle("CUENTA ANTICIPO");
		bot_cuenta_anticipo.setIcon("ui-icon-person");
		bot_cuenta_anticipo.setMetodo("importarCuentaAnticipo");
		bar_botones.agregarBoton(bot_cuenta_anticipo);


		set_cuenta_anticipo.setId("set_cuenta_anticipo");
		set_cuenta_anticipo.setSeleccionTabla(ser_contabilidad.servicioCatalogoCuentaAnio("true","-1"),"ide_cocac");
		set_cuenta_anticipo.getTab_seleccion().getColumna("cue_codigo_cocac").setFiltro(true);
		set_cuenta_anticipo.getTab_seleccion().getColumna("cue_descripcion_cocac").setFiltro(true);

		set_cuenta_anticipo.getBot_aceptar().setMetodo("aceptarCuentaAnticipo");
		set_cuenta_anticipo.setRadio();
		set_cuenta_anticipo.getTab_seleccion().ejecutarSql();
		agregarComponente(set_cuenta_anticipo);

		//ACTUALIZAR CUENTA A.		
		Boton bot_actualizar = new Boton();
		bot_actualizar.setIcon("ui-icon-person");
		bot_actualizar.setValue("Actualizar Cuenta Anticipo");
		bot_actualizar.setMetodo("actualizarCuentaAnticipo");
		bar_botones.agregarBoton(bot_actualizar);

		con_guardar_cuenta.setId("con_guardar_cuenta");
		agregarComponente(con_guardar_cuenta);

		set_actualizar_cuenta.setId("set_actualizar_cuenta");
		set_actualizar_cuenta.setSeleccionTabla(ser_contabilidad.servicioCatalogoCuentaAnio("true","-1"),"ide_cocac");
		set_actualizar_cuenta.setRadio();
		set_actualizar_cuenta.getBot_aceptar().setMetodo("modificarCuentaAnticipo");
		agregarComponente(set_actualizar_cuenta);

		//Configuro el dialogo 
		dia_deta_emplea_depar=new Dialogo();
		dia_deta_emplea_depar.setId("dia_deta_emplea_depar");
		dia_deta_emplea_depar.setTitle("EMPLEADOS DEPARTAMENTO");
		dia_deta_emplea_depar.setWidth("70");
		dia_deta_emplea_depar.setHeight("60");

		Grid gri_deta_emple_depar=new Grid();
		gri_deta_emple_depar.getChildren().add(tab_dia_detalle_empleado_depar);
		dia_deta_emplea_depar.setDialogo(gri_deta_emple_depar);
		dia_deta_emplea_depar.getBot_aceptar().setMetodo("aceptarAccionesEmpleados");


		agregarComponente(dia_deta_emplea_depar);

		contruirMenu();

		pan_opcion.setId("pan_opcion");
		pan_opcion.setTransient(true);

		efecto.setType("drop");
		efecto.setSpeed(150);
		efecto.setPropiedad("mode", "'show'");
		efecto.setEvent("load");
		pan_opcion.getChildren().add(efecto);

		dibujarDatosPersona();


		div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pam_menu, pan_opcion, "20%", "V");
		div_division.getDivision1().setCollapsible(true);
		div_division.getDivision1().setHeader("MENU DE OPCIONES");
		agregarComponente(div_division);

		List lista = new ArrayList();
		Object fila1[] = {
				"false", "INACTIVO"
		};
		Object fila2[] = {
				"true", "ACTIVO"
		};
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setListaSeleccion(lista);
		lis_activo.setVertical();
		dia_filtro_activo.setId("dia_filtro_activo");
		dia_filtro_activo.setTitle("SELECCIONE EMPLEADO ACTIVO / INACTIVO");
		dia_filtro_activo.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_activo.setDialogo(lis_activo);
		dia_filtro_activo.setHeight("40%");
		dia_filtro_activo.setWidth("40%");
		dia_filtro_activo.setDynamic(false);
		agregarComponente(dia_filtro_activo);

		// SRI	
		set_sri.setId("set_sri");
		set_sri.setSeleccionTabla("SELECT IDE_SRIMR, " +
				"DETALLE_SRIMR, " +
				"FECHA_INICIO_SRIMR, " +
				"FECHA_FIN_SRIMR " +
				"FROM SRI_IMPUESTO_RENTA  " +
				"ORDER BY DETALLE_SRIMR ASC", "IDE_SRIMR");
		set_sri.setRadio();
		set_sri.getBot_aceptar().setMetodo("aceptarReporte");
		set_sri.setTitle("SELECCIONE PERIODO SRI");
		agregarComponente(set_sri);

		//SUCURSAL
		set_sucursal.setId("set_sucursal");
		set_sucursal.setSeleccionTabla("SELECT IDE_SUCU, " +
				"NOM_SUCU " +
				"FROM SIS_SUCURSAL " +
				"ORDER BY NOM_SUCU ASC ", "IDE_SUCU");
		set_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
		set_sucursal.setTitle("SELECCIONE SUCURSAL");
		agregarComponente(set_sucursal);
		// AREA 

		set_empleado_deducible.setId("set_empleado_deducible");
		set_empleado_deducible.setSeleccionTabla("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO EMP " +
				"left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
				"WHERE EDP.IDE_GEDEP IN(0,1) " +
				"AND ACTIVO_GTEMP in(FALSE,TRUE) " +
				"ORDER BY NOMBRES ASC", "IDE_GTEMP");
		set_empleado_deducible.getBot_aceptar().setMetodo("aceptarReporte");
		set_empleado_deducible.setTitle("SELECCIONE EMPLEADO");
		agregarComponente(set_empleado_deducible);


		//departamentos
		set_departamento.setId("set_departamento");
		set_departamento.setSeleccionTabla("SELECT DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
				"FROM GEN_DEPARTAMENTO DEP " +
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEDEP=DEP.IDE_GEDEP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EDP.IDE_SUCU " +
				"WHERE DEP.ACTIVO_GEDEP IN(FALSE,TRUE) " +
				"GROUP BY DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
				"ORDER BY DEP.DETALLE_GEDEP", "IDE_GEDEP");
		set_departamento.getBot_aceptar().setMetodo("aceptarReporte");
		set_departamento.setTitle("SELECCIONE DEPARTAMENTO");
		agregarComponente(set_departamento);


		//cumpleAños
		sel_cal_cumpleanios.setId("sel_cal_cumpleanios");
		sel_cal_cumpleanios.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_cal_cumpleanios);

		sel_tab_area.setId("sel_tab_area");
		sel_tab_area.setTitle("SELECCION DE AREAS");
		sel_tab_area.setSeleccionTabla("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare=-1", "ide_geare");
		sel_tab_area.getTab_seleccion().getColumna("detalle_geare").setFiltro(true);
		sel_tab_area.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_area);
		
		sel_tab_departamento.setId("sel_tab_departamento");
		sel_tab_departamento.setTitle("SELECCION DE DEPARTAMENTOS");
		sel_tab_departamento.setSeleccionTabla("SELECT ide_gedep,detalle_gedep FROM gen_departamento where ide_geare in(-1) order by detalle_gedep", "ide_gedep");
		sel_tab_departamento.getTab_seleccion().getColumna("detalle_gedep").setFiltro(true);
		sel_tab_departamento.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_departamento);

		//localidades de trabajo		

		sel_tab_sucursal.setId("sel_tab_sucursal");
		sel_tab_sucursal.setTitle("SELECCION DE SUCURSAL");
		sel_tab_sucursal.setSeleccionTabla("SELECT ide_sucu,nom_sucu  FROM sis_sucursal where ide_sucu=-1 order by  nom_sucu", "ide_sucu");
		sel_tab_sucursal.getTab_seleccion().getColumna("nom_sucu").setFiltro(true);
		sel_tab_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_sucursal);


		//DFJ
		Boton bot_contrato=new Boton();
		bot_contrato.setValue("Contratación");
		bot_contrato.setMetodo("abrirContratacion");
		bar_botones.agregarBoton(bot_contrato);

		dia_contrata.setId("dia_contrata");
		dia_contrata.setTitle("CONTRATACióN");
		dia_contrata.setWidth("85%");
		dia_contrata.setHeight("75%");
                dia_contrata.getBot_aceptar().setMetodo("acpetarContratacion");


		tab_partida_cargo.setId("tab_partida_cargo");
		tab_partida_cargo.setTabla("GEN_PARTIDA_GRUPO_CARGO", "IDE_GEPGC", 51);
		tab_partida_cargo.setCondicion("IDE_GEPGC=-1");
		tab_partida_cargo.getColumna("IDE_GEGRO").setUnico(true);
		tab_partida_cargo.getColumna("IDE_GECAF").setUnico(true);
		tab_partida_cargo.getColumna("IDE_SUCU").setUnico(true);
		tab_partida_cargo.getColumna("IDE_GEARE").setUnico(true);
		tab_partida_cargo.getColumna("IDE_GEDEP").setUnico(true);
		tab_partida_cargo.getColumna("IDE_GEPAP").setUnico(true);
		tab_partida_cargo.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL",	"IDE_GEGRO", "DETALLE_GEGRO", "");
		tab_partida_cargo.getColumna("IDE_GEGRO").setMetodoChange("grupoOcupacional");
		tab_partida_cargo.getColumna("IDE_GECAF").setCombo("select a.IDE_GECAF,a.DETALLE_GECAF from GEN_CARGO_FUNCIONAL a");
		tab_partida_cargo.getColumna("IDE_GECAF").setBuscarenCombo(false);
		tab_partida_cargo.getColumna("IDE_GECAF").setNombreVisual("CARGO FUNCIONAL");
		//tab_partida_cargo.getColumna("IDE_GECAF").setVisible(false);
		tab_partida_cargo.setMostrarcampoSucursal(true);
		tab_partida_cargo.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU","NOM_SUCU", "");
		tab_partida_cargo.getColumna("IDE_SUCU").setBuscarenCombo(false);
		tab_partida_cargo.getColumna("IDE_SUCU").setNombreVisual("SUCURSAL");
		tab_partida_cargo.getColumna("IDE_SUCU").setMetodoChange("cargarAreaContratacion");
		tab_partida_cargo.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_DEPARTAMENTO_SUCURSAL a " +
				"inner join GEN_AREA b on a.IDE_GEARE=b.IDE_GEARE " +
				"GROUP BY b.IDE_GEARE,b.DETALLE_GEARE " +
				"ORDER BY b.DETALLE_GEARE");
		tab_partida_cargo.getColumna("IDE_GEARE").setMetodoChange("cargarDepartamentosContratacion");
		tab_partida_cargo.getColumna("IDE_GEARE").setBuscarenCombo(false);
		tab_partida_cargo.getColumna("IDE_GEDEP").setCombo("SELECT DISTINCT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEDEP=b.IDE_GEDEP");
		tab_partida_cargo.getColumna("IDE_GEDEP").setBuscarenCombo(false);
		tab_partida_cargo.getColumna("IDE_GEPAP").setCombo("GEN_PARTIDA_PRESUPUESTARIA", "IDE_GEPAP","CODIGO_PARTIDA_GEPAP,DETALLE_GEPAP", "ACTIVO_GEPAP=TRUE");
		tab_partida_cargo.getColumna("IDE_GEPAP").setAutoCompletar();
		tab_partida_cargo.setMostrarcampoSucursal(true);
		tab_partida_cargo.getColumna("ACTIVO_GEPGC").setCheck();
		tab_partida_cargo.getColumna("ACTIVO_GEPGC").setValorDefecto("true");
		tab_partida_cargo.getColumna("IDE_GTTEM").setCombo("GTH_TIPO_EMPLEADO","IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_partida_cargo.getColumna("IDE_GTTEM").setRequerida(true);
		tab_partida_cargo.getColumna("IDE_GEPAP").setRequerida(true);
		tab_partida_cargo.getColumna("VACANTE_GEPGC").setVisible(false);
		tab_partida_cargo.getColumna("VACANTE_GEPGC").setValorDefecto("true");	
		tab_partida_cargo.getColumna("FECHA_ACTIVACION_GEPGC").setVisible(false);		
		tab_partida_cargo.getColumna("FECHA_DESACTIVA_GEPGC").setVisible(false);
		tab_partida_cargo.getColumna("MOTIVO_GEPGC").setVisible(false);
		tab_partida_cargo.getColumna("ACTIVO_GEPGC").setVisible(false);
		tab_partida_cargo.getColumna("ENCARGO_GEPGC").setVisible(false);
		tab_partida_cargo.getColumna("SALARIO_ENCARGO_GEPGC").setVisible(false);
		tab_partida_cargo.getColumna("ENCARGO_GEPGC").setVisible(false);
		tab_partida_cargo.setTipoFormulario(true);
		tab_partida_cargo.getGrid().setColumns(4);
		tab_partida_cargo.setMostrarNumeroRegistros(false);
		///// muentra nombres de los campos
		tab_partida_cargo.getColumna("IDE_GEPGC").setNombreVisual("CODIGO");
		tab_partida_cargo.getColumna("IDE_GTTEM").setNombreVisual("REGIMEN LABORAL");
		tab_partida_cargo.getColumna("IDE_SUCU").setNombreVisual("LUGAR TRABAJO");
		tab_partida_cargo.getColumna("IDE_GEGRO").setNombreVisual("GRUPO OCUPACIONAL");
		tab_partida_cargo.getColumna("IDE_GEPAP").setNombreVisual("PARTIDA PRESUPUESTARIA");
		tab_partida_cargo.getColumna("IDE_GEARE").setNombreVisual("PROCESO");
		tab_partida_cargo.getColumna("IDE_GEDEP").setNombreVisual("SUB_PROCESO");
		tab_partida_cargo.getColumna("TITULO_CARGO_GEPGC").setNombreVisual("DENOMINACION PUESTO");

		///orden de los campos
		tab_partida_cargo.getColumna("IDE_GEPGC").setOrden(1);
		tab_partida_cargo.getColumna("IDE_GTTEM").setOrden(2);
		tab_partida_cargo.getColumna("IDE_SUCU").setOrden(3);
		tab_partida_cargo.getColumna("IDE_GECAF").setOrden(7);
		tab_partida_cargo.getColumna("IDE_GEGRO").setOrden(5);
		tab_partida_cargo.getColumna("IDE_GEPAP").setOrden(9);
		tab_partida_cargo.getColumna("IDE_GEARE").setOrden(4);
		tab_partida_cargo.getColumna("IDE_GEDEP").setOrden(6);
		tab_partida_cargo.getColumna("TITULO_CARGO_GEPGC").setOrden(8);

		
		tab_partida_cargo.dibujar();




		////// SEGUNDA DIVICION 

		tab_empleado_departamento_dia.setId("tab_empleado_departamento_dia");	
		tab_empleado_departamento_dia.setTabla("GEN_EMPLEADOS_DEPARTAMENTO_PAR","IDE_GEEDP", 50);		
		tab_empleado_departamento_dia.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO", "");
		//tab_empleado_departamento_dia.getColumna("FECHA_GEEDP").setValorDefecto(utilitario.getFechaActual());***
		tab_empleado_departamento_dia.getColumna("FECHA_GEEDP").setMetodoChange("mismaFecha");
		tab_empleado_departamento_dia.getColumna("FECHA_FINCTR_GEEDP").setMetodoChange("sumarFecha");
		tab_empleado_departamento_dia.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL","IDE_GECAF","DETALLE_GECAF","");	
		tab_empleado_departamento_dia.getColumna("IDE_GECAF").setBuscarenCombo(true);
		tab_empleado_departamento_dia.getColumna("IDE_GECAF").setVisible(false);
		tab_empleado_departamento_dia.getColumna("GEN_IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
		tab_empleado_departamento_dia.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
		tab_empleado_departamento_dia.getColumna("IDE_SUCU").setVisible(true);
		tab_empleado_departamento_dia.getColumna("IDE_GEARE").setCombo(
				"GEN_AREA",
				"IDE_GEARE",
				"DETALLE_GEARE",
				"IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL)");	 //DFJ QUITAR FILTRO X SUCURSAL	
		tab_empleado_departamento_dia.getColumna("IDE_GEARE").setBuscarenCombo(true);		
		tab_empleado_departamento_dia.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO","IDE_GEDEP","DETALLE_GEDEP","");
		tab_empleado_departamento_dia.getColumna("IDE_GEDEP").setBuscarenCombo(true);
		tab_empleado_departamento_dia.getColumna("IDE_GECAE").setCombo("GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE", "");
		tab_empleado_departamento_dia.getColumna("IDE_GETIV").setCombo(
				"GEN_TIPO_VINCULACION", "IDE_GETIV", "DETALLE_GETIV", "");
		tab_empleado_departamento_dia.getColumna("IDE_GETIV").setBuscarenCombo(
				true);
		tab_empleado_departamento_dia.getColumna("IDE_GTTEM").setCombo(
				"GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_empleado_departamento_dia.getColumna("IDE_GTTCO").setCombo(
				"GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO", "");
		tab_empleado_departamento_dia.getColumna("IDE_GTTSI").setCombo(
				"GTH_TIPO_SINDICATO", "IDE_GTTSI", "DETALLE_GTTSI", "");
		tab_empleado_departamento_dia.getColumna("IDE_GTTSI").setValorDefecto(par_tipo_sindicato);
		tab_empleado_departamento_dia.getColumna("IDE_GTGRE").setCombo(
				"GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
		tab_empleado_departamento_dia.getColumna("IDE_GTGRE").setCombo(
				"GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
		tab_empleado_departamento_dia.getColumna("ACTIVO_GEEDP").setCheck();
		tab_empleado_departamento_dia.getColumna("ACTIVO_GEEDP").setLectura(true);
		tab_empleado_departamento_dia.getColumna("ACTIVO_GEEDP").setValorDefecto("true");
		tab_empleado_departamento_dia.getColumna("ACUMULA_FONDOS_GEEDP").setCheck();
		tab_empleado_departamento_dia.getColumna("LINEA_SUPERVICION_GEEDP").setCheck();
		tab_empleado_departamento_dia.getColumna("IDE_GTEMP").setVisible(false);		
		tab_empleado_departamento_dia.getColumna("CONTROL_ASISTENCIA_GEEDP").setCheck();
		tab_empleado_departamento_dia.getColumna("CONTROL_ASISTENCIA_GEEDP").setValorDefecto("false");		
		tab_empleado_departamento_dia.setMostrarcampoSucursal(true);
		tab_empleado_departamento_dia.setTipoFormulario(true);
		tab_empleado_departamento_dia.getGrid().setColumns(4);
		//tab_empleado_departamento_dia.getColumna("IDE_GTTCO").setMetodoChange("cambioTipoContrato");
		tab_empleado_departamento_dia.setRecuperarLectura(true);
		tab_empleado_departamento_dia.setMostrarNumeroRegistros(false);
		tab_empleado_departamento_dia.setCondicion("IDE_GEEDP=-1");
		
		//// campos ocultos en dialogo contratación
		tab_empleado_departamento_dia.getColumna("IDE_GEGRO").setVisible(false);
		tab_empleado_departamento_dia.getColumna("encargado_subrogado_geedp").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_SUCU").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GEARE").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GEDEP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GTTEM").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GEPGC").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GEPGC").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GTTSI").setVisible(false);
		tab_empleado_departamento_dia.getColumna("GEN_IDE_GEGRO").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GECAE").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GETIV").setVisible(false);
		tab_empleado_departamento_dia.getColumna("IDE_GEDED").setVisible(false);
		tab_empleado_departamento_dia.getColumna("AJUSTE_SUELDO_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("FECHA_ENCARGO_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("FECHA_AJUSTE_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("FECHA_LIQUIDACION_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("LIQUIDACION_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("FECHA_ENCARGO_FIN_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("SUELDO_SUBROGA_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("EJECUTO_LIQUIDACION_GEEDP").setVisible(false);
		tab_empleado_departamento_dia.getColumna("GEN_IDE_GECAF").setVisible(false);
		tab_empleado_departamento_dia.getColumna("LINEA_SUPERVICION_GEEDP").setVisible(false);
		//tab_empleado_departamento_dia.getColumna("IDE_GEDED").setVisible(false);
		////NOMBRE DE CAMPOS 
		tab_empleado_departamento_dia.getColumna("IDE_GEEDP").setNombreVisual("CODIGO");
		tab_empleado_departamento_dia.getColumna("IDE_GTGRE").setNombreVisual("JORNADA LABORAL");
		tab_empleado_departamento_dia.getColumna("FECHA_GEEDP").setNombreVisual("FECHA INICIO GESTION");
		tab_empleado_departamento_dia.getColumna("FECHA_FINCTR_GEEDP").setNombreVisual("FECHA FIN GESTION");
		tab_empleado_departamento_dia.getColumna("RMU_GEEDP").setNombreVisual("RMU");
		tab_empleado_departamento_dia.getColumna("IDE_GTTCO").setNombreVisual("TIPO CONTRATO");
		tab_empleado_departamento_dia.getColumna("ACTIVO_GEEDP").setNombreVisual("ACTIVO");
		tab_empleado_departamento_dia.getColumna("ACUMULA_FONDOS_GEEDP").setNombreVisual("ACUMULA FONDOS");
		tab_empleado_departamento_dia.getColumna("CONTROL_ASISTENCIA_GEEDP").setNombreVisual("CONTROL ASISTENCIA");
		tab_empleado_departamento_dia.getColumna("OBSERVACION_GEEDP").setNombreVisual("OBSERVACION");
		tab_empleado_departamento_dia.getColumna("OBSERVACION_GEEDP").setOrden(9);// para q se me ponga en el orden #9

		tab_empleado_departamento_dia.dibujar();
		Grid grid=new Grid();
		grid.getChildren().add(tab_partida_cargo);
		grid.getChildren().add(tab_empleado_departamento_dia);
		dia_contrata.setDialogo(grid);
                
                agregarComponente(dia_contrata);


	}
	///////////sumar las fecha del contrato
	
	public void sumarFecha(DateSelectEvent evt){
		
		TablaGenerica tab_num_dias= utilitario.consultar("select ide_gttco,dia_finc_gttco,detalle_gttco from gth_tipo_contrato where ide_gttco = "+tab_empleado_departamento_dia.getValor("IDE_GTTCO")+" order by dia_finc_gttco");
		//int num_dias= Integer.parseInt(tab_num_dias.getValor("dia_finc_gttco"));
		String fechas=""; // tab_empleado_departamento_dia.getValor("FECHA_GEEDP")+"";
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	  
		//Date fechaDate = null;
	   
		//fechaDate = formato.parse(fechas);
	   
		
		//String str_fecha_fin=utilitario.sumarDiasFecha(fch, num_dias); 
		
		
		//tab_empleado_departamento_dia.setValor("FECHA_FINCTR_GEEDP", tab_empleado_departamento_dia.)
		
	}
	//////////////////SE ME CARGA EL MISMO DATO AL OTRO CAMPO 
		
	public void grupoOcupacional(AjaxBehaviorEvent evt){
		tab_partida_cargo.modificar(evt);//Siempre es la primera linea
		tab_partida_cargo.getColumna("IDE_GECAF").setCombo("select a.IDE_GECAF,a.DETALLE_GECAF from GEN_CARGO_FUNCIONAL a where ide_gecaf in (select ide_gecaf from gen_grupo_cargo where ide_gegro="+tab_partida_cargo.getValor("IDE_GEGRO")+")");		
		utilitario.addUpdateTabla(tab_partida_cargo, "IDE_GECAF", "");
	}
	
	public void cargarDepartamentosContratacion() {
		if (tab_partida_cargo.getTotalFilas()>0){
			tab_partida_cargo.getColumna("IDE_GEDEP").setCombo("SELECT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEDEP=b.IDE_GEDEP AND b.IDE_GEARE="+ tab_partida_cargo.getValor("IDE_GEARE")+ " AND IDE_SUCU="+ tab_partida_cargo.getValor("IDE_SUCU"));
			utilitario.addUpdateTabla(tab_partida_cargo, "IDE_GEDEP", "");
		}
	}


	public void cargarAreaContratacion() {
		tab_partida_cargo.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_DEPARTAMENTO_SUCURSAL a " +
				"inner join GEN_AREA b on a.IDE_GEARE=b.IDE_GEARE " +
				"where a.IDE_SUCU="+ tab_partida_cargo.getValor("IDE_SUCU")+" "+
				"GROUP BY b.IDE_GEARE,b.DETALLE_GEARE " +
				"ORDER BY b.DETALLE_GEARE" );
		utilitario.addUpdateTabla(tab_partida_cargo, "IDE_GEARE", "");
	}

	public void cargarCargoFuncionalContratacion(AjaxBehaviorEvent evt) {
		tab_partida_cargo.modificar(evt);
		tab_partida_cargo.getColumna("IDE_GECAF").setValorDefecto("select a.IDE_GECAF from GEN_CARGO_FUNCIONAL a, GEN_GRUPO_CARGO b where a.IDE_GECAF = b.IDE_GECAF AND IDE_GEGRO ="+ tab_partida_cargo.getValor("IDE_GEGRO")+" limit 1");
		utilitario.addUpdateTabla(tab_partida_cargo, "IDE_GECAF", "");
	}


	private boolean validacionesContratacion(){



		if(tab_partida_cargo.getValor("IDE_GEGRO")==null || 
				tab_partida_cargo.getValor("IDE_GECAF")	==null || 
				tab_partida_cargo.getValor("IDE_SUCU")	==null || 
				tab_partida_cargo.getValor("IDE_GEARE")	==null || 
				tab_partida_cargo.getValor("IDE_GEDEP")	==null || 
				tab_partida_cargo.getValor("IDE_GEPAP")	==null 
				){
			utilitario.agregarMensajeInfo("Debe ingresar todos los valores que son requeridos *", "");
			return false;
		}


		if(tab_empleado_departamento_dia.getValor("fecha_finctr_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_finctr_geedp").isEmpty()){

			if(tab_empleado_departamento_dia.getValor("fecha_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_geedp").isEmpty()){

				if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_finctr_geedp")), utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_geedp")))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha fin de contrato no puede ser menor que la fecha contrato");
					return false;
				}	
			}
		}					


		if(tab_empleado_departamento_dia.getValor("fecha_encargo_fin_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_encargo_fin_geedp").isEmpty()){
			if(tab_empleado_departamento_dia.getValor("fecha_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_geedp").isEmpty()){

				if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_encargo_fin_geedp")), utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_geedp")))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha encargo fin de contrato no puede ser menor que la fecha contrato");
					return false;
				}
			}
		}

		if(tab_empleado_departamento_dia.getValor("fecha_encargo_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_encargo_geedp").isEmpty()){
			if(tab_empleado_departamento_dia.getValor("fecha_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_geedp").isEmpty()){

				if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_encargo_geedp")), utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_geedp")))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha encargo inicio no puede ser menor que la fecha contrato");
					return false;
				}
			}
		}					

		if(tab_empleado_departamento_dia.getValor("fecha_encargo_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_encargo_geedp").isEmpty()){
			if(tab_empleado_departamento_dia.getValor("fecha_encargo_fin_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_encargo_fin_geedp").isEmpty()){
				if (utilitario.isFechaMayor(utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_encargo_geedp")), utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_encargo_fin_geedp")))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de encargo inicial no puede ser mayor que la fecha encargo fin");
					return false;
				}	
			}						
		}

		if(tab_empleado_departamento_dia.getValor("fecha_ajuste_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_ajuste_geedp").isEmpty()){
			if(tab_empleado_departamento_dia.getValor("fecha_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_geedp").isEmpty()){

				if (utilitario.isFechaMenor(utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_ajuste_geedp")), utilitario.getFecha(tab_empleado_departamento_dia.getValor("fecha_geedp")))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha ajuste de contrato no puede ser menor que la fecha contrato");
					return false;
				}
			}
		}



		/*aqui se valida la fecha de contrato con acciones de personal anteriores*/
		TablaGenerica tab_fecha=utilitario.consultar("select ide_gtemp,max (fecha_geedp) as fecha_contrato from gen_empleados_departamento_par where ide_gtemp="+aut_empleado.getValor()+" group by ide_gtemp");

		if (tab_deta_empleado_depar.getTotalFilas()>1){
			System.out.println("valor"+aut_empleado.getValor());
			if(tab_fecha.getTotalFilas()>0){
				if(tab_fecha.getValor("fecha_contrato")!=null && !tab_fecha.getValor("fecha_contrato").isEmpty() ){
					if(tab_empleado_departamento_dia.getValor("fecha_geedp")!=null && !tab_empleado_departamento_dia.getValor("fecha_geedp").isEmpty()){
						if(utilitario.isFechaMayor(utilitario.getFecha(tab_fecha.getValor("fecha_contrato")), utilitario.getFecha(tab_empleado_departamento.getValor("fecha_geedp")))){
							utilitario.agregarMensajeInfo("No se puede Guardar", "La fecha de contrato actual no puede ser menor que la fecha de contrato del anterior accion de personal");
							return false;
						}	
					}
				}
			}
		}

		if(tab_empleado_departamento_dia.isFilaInsertada()){
			//Desactiva todas las partidas y solo deja activa la nueva		    	
			utilitario.getConexion().agregarSql("UPDATE GEN_DETALLE_EMPLEADO_DEPARTAME set ACTIVO_GEDED=false WHERE IDE_GTEMP="+aut_empleado.getValor());
			utilitario.getConexion().agregarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set ACTIVO_GEEDP=false WHERE IDE_GTEMP="+aut_empleado.getValor());				
		}
		return true;		
	}


	public void acpetarContratacion(){
            TablaGenerica tab_contrato_activo = utilitario.consultar("select * from gen_empleados_departamento_par where ide_gtemp="+aut_empleado.getValor()+" and activo_geedp = true ");
		
            if(tab_contrato_activo.getTotalFilas()>0){
                utilitario.agregarMensajeError("Realice Accion de Personal", "El empleado posee un contrato activo utilice al opcion Acciones de Personal");
			return;
            }
            else if(tab_empleado_departamento_dia.getValor("rmu_geedp").equals("")||tab_empleado_departamento_dia.getValor("rmu_geedp").equals("null")||tab_empleado_departamento_dia.getValor("rmu_geedp").equals(null)){
			utilitario.agregarMensajeError("Ingrese Valor", "Ingrese el valor de la Remuneración");
			return;
		}
		
		if (validacionesContratacion()){
			String str_IDE_GEPGC=null;
			//Guarda partida grupo cargo
			TablaGenerica tab_con_partida=utilitario.consultar("SELECT * FROM GEN_PARTIDA_GRUPO_CARGO WHERE " +
					"IDE_GEGRO="+tab_partida_cargo.getValor("IDE_GEGRO")+" " +
					"AND IDE_GECAF="+tab_partida_cargo.getValor("IDE_GECAF")+" " +
					"AND IDE_SUCU="+tab_partida_cargo.getValor("IDE_SUCU")+" " +
					"AND IDE_GEARE="+tab_partida_cargo.getValor("IDE_GEARE")+" " +
					"AND IDE_GEDEP="+tab_partida_cargo.getValor("IDE_GEDEP")+" " +
					"AND  IDE_GEPAP="+tab_partida_cargo.getValor("IDE_GEPAP")+"");


			tab_empleado_departamento_dia.setValor("IDE_GEGRO",tab_partida_cargo.getValor("IDE_GEGRO"));
			tab_empleado_departamento_dia.setValor("IDE_GECAF", tab_partida_cargo.getValor("IDE_GECAF"));
			tab_empleado_departamento_dia.setValor("IDE_SUCU", tab_partida_cargo.getValor("IDE_SUCU"));
			tab_empleado_departamento_dia.setValor("IDE_GEARE",tab_partida_cargo.getValor("IDE_GEARE"));
			tab_empleado_departamento_dia.setValor("IDE_GEDEP",tab_partida_cargo.getValor("IDE_GEDEP"));
			tab_empleado_departamento_dia.setValor("IDE_GTTEM",tab_partida_cargo.getValor("IDE_GTTEM"));
			
			tab_empleado_departamento_dia.setValor("ACTIVO_GEEDP", "true");
			
			if(tab_con_partida.isEmpty()){
		
				tab_partida_cargo.guardar();

				str_IDE_GEPGC=tab_partida_cargo.getValor("IDE_GEPGC");	
				
			}
			else{
				str_IDE_GEPGC=tab_con_partida.getValor("IDE_GEPGC");

			}

			//Guardar registro de GEN_DETALLE_EMPLEADO_DEPARTAME
			TablaGenerica tab_ded=new TablaGenerica();
			tab_ded.setTabla("GEN_DETALLE_EMPLEADO_DEPARTAME", "IDE_GEDED", -1);
			tab_ded.setCondicion("IDE_GEDED=-1");
			tab_ded.ejecutarSql();

			tab_ded.insertar();
			tab_ded.setValor("IDE_GEINS", null);
			tab_ded.setValor("IDE_GEAME", IDE_GAME);
			tab_ded.setValor("FECHA_INGRESO_GEDED", tab_empleado_departamento_dia.getValor("fecha_geedp"));

			tab_ded.setValor("ACTIVO_GEDED", "true");
			tab_ded.setValor("IDE_GTEMP", aut_empleado.getValor());
			tab_ded.setValor("GEN_IDE_GEDED", tab_deta_empleado_depar.getValor("IDE_GEDED")); //recursivo guarda el anterior

			tab_ded.guardar();

			tab_empleado_departamento_dia.setValor("IDE_GEDED", tab_ded.getValor("IDE_GEDED"));
			tab_empleado_departamento_dia.setValor("IDE_GEPGC", str_IDE_GEPGC);
			tab_empleado_departamento_dia.guardar();

			if(guardarPantalla().isEmpty()){
				dibujarDatosEmpleadoDepartamento();
				utilitario.addUpdate("pan_opcion");
				dia_contrata.cerrar(); 	
			}			
		}
	}


	public void abrirContratacion(){
		if(aut_empleado.getValor()!=null){
			tab_empleado_departamento_dia.limpiar();
			tab_empleado_departamento_dia.insertar();
			tab_empleado_departamento_dia.setValor("IDE_GTEMP", aut_empleado.getValor());
			tab_empleado_departamento_dia.setValor("FECHA_GEEDP",tab_empleado.getValor("fecha_ingreso_gtemp"));
			tab_partida_cargo.limpiar();
			tab_partida_cargo.insertar();
			utilitario.getConexion().getSqlPantalla().clear();
			dia_contrata.dibujar();

                        

		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un empleado", "");
		}
	}
	

	public  void aceptarCuentaAnticipo(){
		String str_seleccionado=set_cuenta_anticipo.getValorSeleccionado();
		System.out.println("pruebabab f  "+str_seleccionado);
		if (str_seleccionado!=null){
			tab_cuenta_anticipo.insertar();
			//tab_cuenta_anticipo.setValor("IDE_COCAC", tab_cuenta_anticipo.getValor(i, "IDE_COCAC"));			
			tab_cuenta_anticipo.setValor("ide_cocac",str_seleccionado);
			System.out.println("entre cc f  "+str_seleccionado);
		}
		set_cuenta_anticipo.cerrar();
		utilitario.addUpdate("tab_cuenta_anticipo");
	}

	public void importarCuentaAnticipo(){
		if(aut_empleado.getValor()!= null){
			if(tab_anio.getValor("ide_geani")!=null){
				set_cuenta_anticipo.getTab_seleccion().setSql(ser_contabilidad.servicioCatalogoCuentaAnio("true",tab_anio.getValorSeleccionado()));
				set_cuenta_anticipo.getTab_seleccion().ejecutarSql();
				set_cuenta_anticipo.dibujar();
			}
			else{
				utilitario.agregarMensajeInfo("SELECCIONE UN AñO","No se encuentra seleccionado un Año para la cuenta de anticipo");
			}

		}else {
			utilitario.agregarMensajeInfo("SELECCIONE UN EMPLEADO","");

		}

	}
	public void actualizarCuentaAnticipo(){
		if(aut_empleado.getValor()!= null){
			if(tab_cuenta_anticipo.getValor("ide_cocac")!=null){
				set_actualizar_cuenta.getTab_seleccion().setSql(ser_contabilidad.servicioCatalogoCuentaAnio("true",tab_anio.getValorSeleccionado()));
				set_actualizar_cuenta.getTab_seleccion().ejecutarSql();
				set_actualizar_cuenta.dibujar();
			}
			else{
				utilitario.agregarMensajeInfo("SELECCIONE UNA CUENTA","No se encuentra seleccionado la cuenta anticipo para actualizar");
			}

		}else {
			utilitario.agregarMensajeInfo("SELECCIONE UN EMPLEADO","");
		}

	}

	public void modificarCuentaAnticipo(){
		System.out.println("entre modificar  ");
		String str_cuenta= set_actualizar_cuenta.getValorSeleccionado();
		System.out.println("q selecciona  "+str_cuenta);
		if(str_cuenta!=null){

			tab_cuenta_anticipo.modificar(tab_cuenta_anticipo.getFilaActual());
			tab_cuenta_anticipo.setValor("ide_cocac", str_cuenta);
			utilitario.addUpdate("tab_cuenta_anticipo");

			con_guardar_cuenta.setMessage("Esta Seguro de Actualizar la cuenta");
			con_guardar_cuenta.setTitle("CONFIRMCION DE ACTUALIZAR");
			con_guardar_cuenta.getBot_aceptar().setMetodo("guardarActualizarCuenta");
			con_guardar_cuenta.dibujar();
			utilitario.addUpdate("con_guardar_cuenta");
		}
	}
	public void guardarActualizarCuenta(){
		System.out.println("entre guardar  ");

		tab_cuenta_anticipo.guardar();
		con_guardar_cuenta.cerrar();
		set_actualizar_cuenta.cerrar();
		guardarPantalla();
	}


	public void aceptarAccionesEmpleado(){
		System.out.println("Si ingreso al al aceptar acciones empleado");
		con_acciones_personal.cerrar();
		System.out.println("Si ingreso anstes  del dibujar la tabla");
		//		dia_deta_emplea_depar.dibujar();
		dibujarDatosDialogoDetalleEmpleadoDepar();
		System.out.println("Si DIBUJA LA TABAL");
		utilitario.addUpdate("con_acciones_personal,dia_deta_emplea_depar");
	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}



	public TablaGenerica getTablaCargasFamiliaresLlena(String IDE_GTEMP , String ACTIVO_GTEMP){
		TablaGenerica tab_car_fam=utilitario.consultar("select " +
				"EMP.ACTIVO_GTEMP, " +
				"EMP.IDE_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || '  ' || " +
				"EMP.APELLIDO_MATERNO_GTEMP || '  ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || '  ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP  AS NOMBRES, " +
				"FAM.APELLIDO_PATERNO_GTCAF || '  ' || " +
				"FAM.APELLIDO_MATERNO_GTCAF || '  ' || " +
				"FAM.PRIMER_NOMBRE_GTCAF || '  ' || " +
				"FAM.SEGUNDO_NOMBRE_GTCAF AS NOMBRES_CARGA, " +
				"PAR.DETALLE_GTTPR AS TIPO_PARENTESCO, " +
				"DOC.DETALLE_GTTDI AS TIPO_DOC, " +
				"FAM.DOCUMENTO_IDENTIDAD_GTCAF AS DOCUMENTO, " +
				"GEN.DETALLE_GTGEN AS GENERO, " +
				"FAM.ACTIVO_GTCAF, " +
				"FECHA_NACIMIENTO_GTCAF, " +
				"'' as EDAD " +
				"from GTH_CARGAS_FAMILIARES FAM " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=FAM.IDE_GTEMP " +
				"left join  GTH_TIPO_PARENTESCO_RELACION PAR ON PAR.IDE_GTTPR=FAM.IDE_GTTPR " +
				"left join  GTH_GENERO GEN ON GEN.IDE_GTGEN=FAM.IDE_GTGEN " +
				"left join  GTH_TIPO_DOCUMENTO_IDENTIDAD DOC ON DOC.IDE_GTTDI=FAM.IDE_GTTDI " +
				"WHERE FAM.IDE_GTEMP IN ("+IDE_GTEMP+") AND " +
				"EMP.ACTIVO_GTEMP IN ("+ACTIVO_GTEMP+") " +
				"order by ACTIVO_GTEMP ASC, NOMBRES ASC, NOMBRES_CARGA ASC"); 

		for (int i = 0; i < tab_car_fam.getTotalFilas(); i++) {
			String fecha_nac=tab_car_fam.getValor(i,"FECHA_NACIMIENTO_GTCAF");
			String edad="";
			if (fecha_nac!=null && !fecha_nac.isEmpty()){
				//				edad=getAniosEdad(getEdadCompleta(utilitario.getFormatoFecha(fecha_nac)))+" A "
				//						+getMesesEdad(getEdadCompleta(utilitario.getFormatoFecha(fecha_nac)))+" M "
				//						+getDiasEdad(getEdadCompleta(utilitario.getFormatoFecha(fecha_nac)))+" D ";
				edad=getEdadCompleta(utilitario.getFormatoFecha(fecha_nac),true);
			}
			tab_car_fam.modificar(i);
			tab_car_fam.setValor(i, "EDAD", edad);
		}

		return tab_car_fam;
	}


	/*
	public TablaGenerica getTablaCargasFamiliaresLlena(String IDE_GTEMP , String ACTIVO_GTEMP){
		TablaGenerica tab_car_fam=utilitario.consultar("select " +
				"EMP.ACTIVO_GTEMP," +
				"EMP.IDE_GTEMP," +
				"SUCU.NOM_SUCU AS SUCURSAL, " +
				"EMP.APELLIDO_PATERNO_GTEMP || '  ' || " +
				"EMP.APELLIDO_MATERNO_GTEMP || '  ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || '  ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP  AS NOMBRES, " +
				"FAM.APELLIDO_PATERNO_GTCAF || '  ' || " +
				"FAM.APELLIDO_MATERNO_GTCAF || '  ' || " +
				"FAM.PRIMER_NOMBRE_GTCAF || '  ' || " +
				"FAM.SEGUNDO_NOMBRE_GTCAF AS NOMBRES_CARGA, " +
				"PAR.DETALLE_GTTPR AS TIPO_PARENTESCO, " +
				"DOC.DETALLE_GTTDI AS TIPO_DOC, " +
				"FAM.DOCUMENTO_IDENTIDAD_GTCAF AS DOCUMENTO, " +
				"GEN.DETALLE_GTGEN AS GENERO, " +
				"FAM.ACTIVO_GTCAF, " +
				"FECHA_NACIMIENTO_GTCAF, " +
				"'' as EDAD " +
				"from GTH_CARGAS_FAMILIARES FAM " +
				"left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=FAM.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU = EDP.IDE_SUCU " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=FAM.IDE_GTEMP " +
				"left join  GTH_TIPO_PARENTESCO_RELACION PAR ON PAR.IDE_GTTPR=FAM.IDE_GTTPR " +
				"left join  GTH_GENERO GEN ON GEN.IDE_GTGEN=FAM.IDE_GTGEN " +
				"left join  GTH_TIPO_DOCUMENTO_IDENTIDAD DOC ON DOC.IDE_GTTDI=FAM.IDE_GTTDI " +
				"WHERE FAM.IDE_GTEMP IN ("+IDE_GTEMP+") " +
				"AND EMP.ACTIVO_GTEMP IN ("+ACTIVO_GTEMP+") " +
				"order by ACTIVO_GTEMP ASC , " +
				"NOMBRES ASC, " +
				"NOMBRES_CARGA ASC, " +
				"SUCU.NOM_SUCU"); 

		for (int i = 0; i < tab_car_fam.getTotalFilas(); i++) {
			String fecha_nac=tab_car_fam.getValor(i,"FECHA_NACIMIENTO_GTCAF");
			String edad="";
			if (fecha_nac!=null && !fecha_nac.isEmpty()){
				//				edad=getAniosEdad(getEdadCompleta(utilitario.getFormatoFecha(fecha_nac)))+" A "
				//						+getMesesEdad(getEdadCompleta(utilitario.getFormatoFecha(fecha_nac)))+" M "
				//						+getDiasEdad(getEdadCompleta(utilitario.getFormatoFecha(fecha_nac)))+" D ";
				edad=getEdadCompleta(utilitario.getFormatoFecha(fecha_nac),true);
			}
			tab_car_fam.modificar(i);
			tab_car_fam.setValor(i, "EDAD", edad);
		}

		return tab_car_fam;
	}
	 */



	@Override
	public void aceptarReporte() {
		// TODO Auto-generated method stub
		if (rep_reporte.getReporteSelecionado().equals("Ficha del Empleado")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				set_empleado.getTab_seleccion().setSeleccionados(null);
				//utilitario.ejecutarJavaScript(set_empleado.getTab_seleccion().getId()+".clearFilters();");				
				dia_filtro_activo.dibujar();				
			}else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()!=null && !lis_activo.getSeleccionados().isEmpty()){
					System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
					set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' || " +
							"PRIMER_NOMBRE_GTEMP || ' ' || " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
							"ORDER BY NOMBRES ASC");
					set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado.getTab_seleccion().ejecutarSql();
					set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					dia_filtro_activo.cerrar();
					set_empleado.dibujar();			
				}else{
					utilitario.agregarMensajeInfo("No se puede generar la ficha del empleado", "Debe seleccionar una opción");
				}
			}	else if (set_empleado.isVisible()){
				if (set_empleado.getListaSeleccionados().size()>0){
					p_parametros.put("IDE_GTEMP",set_empleado.getSeleccionados());
					p_parametros.put("titulo", "EXPEDIENTE EMPLEADO");
					set_empleado.cerrar();
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();					
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		}

		////  para sistuacion economica

		else if (rep_reporte.getReporteSelecionado().equals("Situacion Socioeconomica del Empleado")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				set_empleado.getTab_seleccion().setSeleccionados(null);
				//utilitario.ejecutarJavaScript(set_empleado.getTab_seleccion().getId()+".clearFilters();");				
				dia_filtro_activo.dibujar();				
			}else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()!=null && !lis_activo.getSeleccionados().isEmpty()){
					System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
					set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' || " +
							"PRIMER_NOMBRE_GTEMP || ' ' || " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
							"ORDER BY NOMBRES ASC");
					set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado.getTab_seleccion().ejecutarSql();
					set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					dia_filtro_activo.cerrar();
					set_empleado.dibujar();			
				}else{
					utilitario.agregarMensajeInfo("No se puede generar la ficha del empleado", "Debe seleccionar una opción");
				}
			}	else if (set_empleado.isVisible()){
				if (set_empleado.getListaSeleccionados().size()>0){
					p_parametros.put("IDE_GTEMP",set_empleado.getSeleccionados());
					p_parametros.put("titulo", "SITUACION ECONOMICA DEL EMPLEADO");
					set_empleado.cerrar();
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();					
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		}

		//

		else if (rep_reporte.getReporteSelecionado().equals("Distributivo de Colaboradores")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();				
			}
			else if (dia_filtro_activo.isVisible()) {

				if(lis_activo.getSeleccionados()!=null && ! lis_activo.getSeleccionados().isEmpty()){
					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					dia_filtro_activo.cerrar();
					set_tipo_empleado.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}

			}
			else if (set_tipo_empleado.isVisible()){
				try {
					if (set_tipo_empleado.getSeleccionados()!=null && !set_tipo_empleado.getSeleccionados().isEmpty() ){
						p_parametros.put("ide_gttem",set_tipo_empleado.getSeleccionados());
						TablaGenerica tab_tipo_empl=utilitario.consultar("select * from GTH_TIPO_EMPLEADO where IDE_GTTEM in("+set_tipo_empleado.getSeleccionados()+") ");

						p_parametros.put("titulo", "DISTRIBUTIVO "+tab_tipo_empl.getValor(0, "DETALLE_GTTEM")+" "+utilitario.getAnio(utilitario.getFechaActual()));
						set_tipo_empleado.cerrar();
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						sef_reporte.dibujar();
					}
					else{
						utilitario.agregarMensajeInfo("No se continuar", "No ha seleccionado ningun Tipo de Empleado");
					}
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Empleado");
				}
			}
		} else if (rep_reporte.getReporteSelecionado().equals("Cuentas Bancarias")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()==null || lis_activo.getSeleccionados().isEmpty()){
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
					return;
				}

				System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
				set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
						"APELLIDO_PATERNO_GTEMP || ' ' || " +
						"APELLIDO_MATERNO_GTEMP || ' ' || " +
						"PRIMER_NOMBRE_GTEMP || ' ' || " +
						"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
						"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
						"ORDER BY NOMBRES ASC");
				set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
				set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);

				set_empleado.getTab_seleccion().ejecutarSql();
				set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
				p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
				dia_filtro_activo.cerrar();
				set_empleado.dibujar();
			}else if (set_empleado.isVisible()) {
				if (set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()) {
					p_parametros.put("IDE_GTEMP", set_empleado.getSeleccionados());
					p_parametros.put("titulo","CUENTAS BANCARIAS DEL EMPLEADO");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No ha seleccionado ningun empleado","");
				}

			}
		}else if (rep_reporte.getReporteSelecionado().equals("Conyuges Empleados")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
				if(lis_activo.getSeleccionados()==null || lis_activo.getSeleccionados().isEmpty()){
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
					return;
				}

				set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
						"APELLIDO_PATERNO_GTEMP || ' ' || " +
						"APELLIDO_MATERNO_GTEMP || ' ' || " +
						"PRIMER_NOMBRE_GTEMP || ' ' || " +
						"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
						"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
						"ORDER BY NOMBRES ASC");
				set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
				set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
				set_empleado.getTab_seleccion().ejecutarSql();
				set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
				p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
				dia_filtro_activo.cerrar();
				set_empleado.dibujar();
			}else if (set_empleado.isVisible()) {
				if (set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()) {
					p_parametros.put("IDE_GTEMP", set_empleado.getSeleccionados());
					p_parametros.put("titulo","DATOS CONYUGES POR EMPLEADO");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No ha seleccionado ningun empleado","");
				}

			}
		}else if (rep_reporte.getReporteSelecionado().equals("Datos Personales")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()==null || lis_activo.getSeleccionados().isEmpty()){
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
					return;
				}

				System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
				set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
						"APELLIDO_PATERNO_GTEMP || ' ' || " +
						"APELLIDO_MATERNO_GTEMP || ' ' || " +
						"PRIMER_NOMBRE_GTEMP || ' ' || " +
						"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
						"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
						"ORDER BY NOMBRES ASC");
				set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
				set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
				set_empleado.getTab_seleccion().ejecutarSql();
				set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
				p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
				dia_filtro_activo.cerrar();
				set_empleado.dibujar();
			}else if (set_empleado.isVisible()) {
				if (set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()) {
					p_parametros.put("IDE_GTEMP", set_empleado.getSeleccionados());
					p_parametros.put("titulo","DATOS PERSONALES EMPLEADOS");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No ha seleccionado ningun empleado","");
				}

			}
		}else if (rep_reporte.getReporteSelecionado().equals("Datos Familiares Empleados")) {
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				if (lis_activo.getSeleccionados()!=null && !lis_activo.getSeleccionados().isEmpty()) {
					System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
					set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' || " +
							"PRIMER_NOMBRE_GTEMP || ' ' || " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
							"ORDER By NOMBRES ASC");
					set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado.getTab_seleccion().ejecutarSql();
					set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					dia_filtro_activo.cerrar();
					set_empleado.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}
			}else if (set_empleado.isVisible()) {
				if (set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()) {
					p_parametros.put("IDE_GTEMP", set_empleado.getSeleccionados());
					p_parametros.put("titulo","DATOS FAMILIARES EMPLEADOS");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}

			}
		}

		else if (rep_reporte.getReporteSelecionado().equals("Datos Cargas Familiares Empleados")) {
			//String str_activos="";
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				if (lis_activo.getSeleccionados()!=null && !lis_activo.getSeleccionados().isEmpty()) {

					System.out.println("ACTIVOS :"+lis_activo.getSeleccionados());
					set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' || " +
							"PRIMER_NOMBRE_GTEMP || ' ' || " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
							"ORDER BY NOMBRES ASC");
					set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado.getTab_seleccion().ejecutarSql();
					set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
					//				str_activos=lis_activo.getSeleccionados();
					dia_filtro_activo.cerrar();
					set_empleado.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}

			}else if (set_empleado.isVisible()) {
				if (set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()) {

					TablaGenerica tab_emp=utilitario.consultar("select * from SIS_EMPRESA");
					TablaGenerica tab_usua=utilitario.consultar("select * from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("ide_usua"));
					p_parametros.put("titulo", "DATOS CARGAS FAMILIARES");
					p_parametros.put("nick", tab_usua.getValor("nick_usua"));
					p_parametros.put("direccion", tab_emp.getValor("DIRECCION_EMPR"));
					p_parametros.put("telefono", tab_emp.getValor("TELEFONO_EMPR"));
					p_parametros.put("dir_logo", tab_emp.getValor("LOGO_EMPR"));
					ReporteDataSource rep = new ReporteDataSource(getTablaCargasFamiliaresLlena(set_empleado.getSeleccionados(),lis_activo.getSeleccionados()));
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath(), rep);
					set_empleado.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}

			}
		}

		else if (rep_reporte.getReporteSelecionado().equals("Gasto Deducible Por Empleado")) {
			//String str_activos="";
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()!= null && !lis_activo.getSeleccionados().isEmpty()){


					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					set_sri.getTab_seleccion().setSql("SELECT IDE_SRIMR, " +
							"DETALLE_SRIMR, " +
							"FECHA_INICIO_SRIMR, " +
							"FECHA_FIN_SRIMR " +
							"FROM SRI_IMPUESTO_RENTA  " +
							"ORDER BY DETALLE_SRIMR ASC");
					set_sri.getTab_seleccion().getColumna("DETALLE_SRIMR").setFiltro(true);
					set_sri.getTab_seleccion().ejecutarSql();
					set_sri.getBot_aceptar().setMetodo("aceptarReporte");
					System.out.println("PE SQL set_sri... "+set_sri.getTab_seleccion().getSql());
					dia_filtro_activo.cerrar();
					set_sri.dibujar();
				}
				else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}

			}
			else if (set_sri.isVisible()) {
				if (set_sri.getValorSeleccionado()!=null &&  !set_sri.getValorSeleccionado().isEmpty() ) {


					p_parametros.put("IDE_SRIMR",set_sri.getValorSeleccionado());
					set_sucursal.getTab_seleccion().setSql("SELECT IDE_SUCU, " +
							"NOM_SUCU " +
							"FROM SIS_SUCURSAL " +
							"ORDER BY NOM_SUCU ASC ");
					set_sucursal.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);
					set_sucursal.getTab_seleccion().ejecutarSql();
					set_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
					set_sri.cerrar();
					set_sucursal.dibujar();
				}
				else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo");
				}
			}else if (set_sucursal.isVisible()) {
				if(set_sucursal.getSeleccionados()!=null && !set_sucursal.getSeleccionados().isEmpty()){

					p_parametros.put("IDE_SUC", set_sucursal.getSeleccionados());
					set_departamento.getTab_seleccion().setSql("SELECT DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
							"FROM GEN_DEPARTAMENTO DEP " +
							"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEDEP=DEP.IDE_GEDEP " +
							"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EDP.IDE_SUCU " +
							"WHERE SUCU.IDE_SUCU IN("+set_sucursal.getSeleccionados()+") AND DEP.ACTIVO_GEDEP IN(0,1) " +
							"GROUP BY DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
							"ORDER BY DEP.DETALLE_GEDEP");
					set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
					set_departamento.getTab_seleccion().ejecutarSql();
					set_departamento.getBot_aceptar().setMetodo("aceptarReporte");
					set_sucursal.cerrar();
					set_departamento.dibujar();
				}	else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Sucursal");
				}

			}else if (set_departamento.isVisible()) {

				if(set_departamento.getSeleccionados()!=null && !set_departamento.getSeleccionados().isEmpty())
				{
					p_parametros.put("IDE_GEDEP", set_departamento.getSeleccionados());
					set_empleado_deducible.getTab_seleccion().setSql("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
							"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO EMP " +
							"left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
							"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
							"WHERE EDP.IDE_GEDEP IN("+set_departamento.getSeleccionados()+") " +
							"AND ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
							"ORDER BY NOMBRES ASC");
					set_empleado_deducible.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado_deducible.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado_deducible.getTab_seleccion().ejecutarSql();
					set_empleado_deducible.getBot_aceptar().setMetodo("aceptarReporte");
					//					str_activos=lis_activo.getSeleccionados();
					set_departamento.cerrar();

					set_empleado_deducible.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Departamento");
				}

			}else if (set_empleado_deducible.isVisible()) {
				if (set_empleado_deducible.getSeleccionados()!=null && !set_empleado_deducible.getSeleccionados().isEmpty()) {
					System.out.println("EMPLEADOS..."+set_empleado_deducible.getSeleccionados());
					p_parametros.put("IDE_GTEMP", set_empleado_deducible.getSeleccionados());
					p_parametros.put("titulo","GASTO DEDUCIBLE POR EMPLEADO");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado_deducible.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}

			}
		}
		else if (rep_reporte.getReporteSelecionado().equals("Detalle Gasto Deducible Empleado")) {
			//String str_activos="";
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()!= null && !lis_activo.getSeleccionados().isEmpty()){



					set_sri.getTab_seleccion().setSql("SELECT IDE_SRIMR, " +
							"DETALLE_SRIMR, " +
							"FECHA_INICIO_SRIMR, " +
							"FECHA_FIN_SRIMR " +
							"FROM SRI_IMPUESTO_RENTA  " +
							"ORDER BY DETALLE_SRIMR ASC");
					set_sri.getTab_seleccion().getColumna("DETALLE_SRIMR").setFiltro(true);
					set_sri.getTab_seleccion().ejecutarSql();
					set_sri.getBot_aceptar().setMetodo("aceptarReporte");
					dia_filtro_activo.cerrar();
					set_sri.dibujar();
				}
				else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}

			}
			else if (set_sri.isVisible()) {
				if (set_sri.getValorSeleccionado()!=null &&  !set_sri.getValorSeleccionado().isEmpty() ) {


					p_parametros.put("IDE_SRIMR",Integer.parseInt(set_sri.getValorSeleccionado()));
					set_sucursal.getTab_seleccion().setSql("SELECT IDE_SUCU, " +
							"NOM_SUCU " +
							"FROM SIS_SUCURSAL " +
							"ORDER BY NOM_SUCU ASC ");
					set_sucursal.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);
					set_sucursal.getTab_seleccion().ejecutarSql();
					set_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
					set_sri.cerrar();
					set_sucursal.dibujar();
				}
				else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo");
				}
			}else if (set_sucursal.isVisible()) {
				if(set_sucursal.getSeleccionados()!=null && !set_sucursal.getSeleccionados().isEmpty()){


					set_departamento.getTab_seleccion().setSql("SELECT DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
							"FROM GEN_DEPARTAMENTO DEP " +
							"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEDEP=DEP.IDE_GEDEP " +
							"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EDP.IDE_SUCU " +
							"WHERE DEP.ACTIVO_GEDEP IN(FALSE,TRUE) " +
							"GROUP BY DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
							"ORDER BY DEP.DETALLE_GEDEP");
					set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
					set_departamento.getTab_seleccion().ejecutarSql();
					set_departamento.getBot_aceptar().setMetodo("aceptarReporte");
					set_sucursal.cerrar();
					set_departamento.dibujar();
				}	else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Sucursal");
				}

			}else if (set_departamento.isVisible()) {

				if(set_departamento.getSeleccionados()!=null && !set_departamento.getSeleccionados().isEmpty())
				{

					set_empleado_deducible.getTab_seleccion().setSql("select EDP.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
							"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
							"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
							"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
							"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO EMP " +
							"left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP and activo_geedp=TRUE " +
							"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
							"WHERE EDP.IDE_GEDEP IN("+set_departamento.getSeleccionados()+") " +
							"AND ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
							"ORDER BY NOMBRES ASC");

					System.out.println("pe sql ... "+set_empleado_deducible.getTab_seleccion().getSql());

					set_empleado_deducible.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado_deducible.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado_deducible.getTab_seleccion().ejecutarSql();
					set_empleado_deducible.getBot_aceptar().setMetodo("aceptarReporte");
					//					str_activos=lis_activo.getSeleccionados();
					set_departamento.cerrar();

					set_empleado_deducible.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Departamento");
				}

			}else if (set_empleado_deducible.isVisible()) {
				if (set_empleado_deducible.getSeleccionados()!=null && !set_empleado_deducible.getSeleccionados().isEmpty()) {
					System.out.println("1EMPLEADOS... "+set_empleado_deducible.getSeleccionados());
					p_parametros.put("IDE_GTEMP", Integer.parseInt(set_empleado_deducible.getSeleccionados()));
					System.out.println("pe p_parametros IDE_GTEMP ... "+p_parametros);

					p_parametros.put("titulo","DETALLE GASTO DEDUCIBLE ");

					System.out.println("pe p_parametros titulo ... "+p_parametros);

					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado_deducible.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}

			}
		}

		else if (rep_reporte.getReporteSelecionado().equals("Proyeccion Ingresos Empleado")) {
			//String str_activos="";
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			}else if (dia_filtro_activo.isVisible()) {
				if (lis_activo.getSeleccionados()!=null && !lis_activo.getSeleccionados().isEmpty()) {

					p_parametros.put("ACTIVO_GTEMP", lis_activo.getSeleccionados());
					set_sri.getTab_seleccion().setSql("SELECT IDE_SRIMR, " +
							"DETALLE_SRIMR, " +
							"FECHA_INICIO_SRIMR, " +
							"FECHA_FIN_SRIMR " +
							"FROM SRI_IMPUESTO_RENTA  " +
							"ORDER BY DETALLE_SRIMR ASC");
					set_sri.getTab_seleccion().getColumna("DETALLE_SRIMR").setFiltro(true);
					set_sri.getTab_seleccion().ejecutarSql();
					set_sri.getBot_aceptar().setMetodo("aceptarReporte");
					dia_filtro_activo.cerrar();
					set_sri.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Estado");
				}

			}else if (set_sri.isVisible()) {
				if (set_sri.getValorSeleccionado()!=null && !set_sri.getValorSeleccionado().isEmpty()) {

					p_parametros.put("IDE_SRIMR",set_sri.getValorSeleccionado());
					set_empleado.getTab_seleccion().setSql("SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
							"APELLIDO_PATERNO_GTEMP || ' ' || " +
							"APELLIDO_MATERNO_GTEMP || ' ' || " +
							"PRIMER_NOMBRE_GTEMP || ' ' || " +
							"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
							"from GTH_EMPLEADO WHERE ACTIVO_GTEMP in("+lis_activo.getSeleccionados()+") " +
							"ORDER BY NOMBRES ASC");
					set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
					set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
					set_empleado.getTab_seleccion().ejecutarSql();
					set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
					//					str_activos=lis_activo.getSeleccionados();
					set_sri.cerrar();
					set_empleado.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo");
				}


			}else if (set_empleado.isVisible()) {
				if (set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()) {

					p_parametros.put("IDE_GTEMP", set_empleado.getSeleccionados());
					p_parametros.put("titulo","PROYECCION DE INGRESOS POR PERIODO ");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}

			}
		}		
		else if(rep_reporte.getReporteSelecionado().equals("Listado de CumpleAños")){
			if(rep_reporte.isVisible()){
				p_parametros =new HashMap();
				rep_reporte.cerrar();
				sel_tab_sucursal.getTab_seleccion().setSql("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by  nom_sucu");
				sel_tab_sucursal.getTab_seleccion().ejecutarSql();
				sel_tab_sucursal.dibujar();		
			}else if(sel_tab_sucursal.isVisible()){
				if(sel_tab_sucursal.getSeleccionados()!=null && !sel_tab_sucursal.getSeleccionados().isEmpty()){
					p_parametros.put("ide_sucu_t", sel_tab_sucursal.getSeleccionados());				
					sel_tab_area.getTab_seleccion().setSql("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare in (SELECT ide_geare FROM gen_departamento_sucursal where ide_sucu in("+sel_tab_sucursal.getSeleccionados()+"))");
					sel_tab_area.getTab_seleccion().ejecutarSql();
					sel_tab_sucursal.cerrar();
					sel_tab_area.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una sucursal");
				}
			}else if(sel_tab_area.isVisible()){
				if(sel_tab_area.getSeleccionados()!=null && !sel_tab_area.getSeleccionados().isEmpty()){
					p_parametros.put("ide_geare", sel_tab_area.getSeleccionados());
					sel_tab_departamento.getTab_seleccion().setSql("SELECT ide_gedep,detalle_gedep FROM gen_departamento where ide_geare in("+sel_tab_area.getSeleccionados()+") order by detalle_gedep");
					sel_tab_departamento.getTab_seleccion().ejecutarSql();
					sel_tab_area.cerrar();
					sel_tab_departamento.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una �rea");
				}
			}else if(sel_tab_departamento.isVisible()){
				if(sel_tab_departamento.getSeleccionados()!=null && ! sel_tab_departamento.getSeleccionados().isEmpty()){
					p_parametros.put("ide_gedep", sel_tab_departamento.getSeleccionados());					
					sel_tab_departamento.cerrar();
					sel_cal_cumpleanios.setFecha1(null);
					sel_cal_cumpleanios.setFecha2(null);					
					sel_cal_cumpleanios.dibujar();							
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un Departamento");
				}
			}else if(sel_cal_cumpleanios.isVisible()){
				if(sel_cal_cumpleanios.isFechasValidas()){
					int anio_ini=utilitario.getAnio(sel_cal_cumpleanios.getFecha1String());
					int anio_fin=utilitario.getAnio(sel_cal_cumpleanios.getFecha2String());
					if (anio_ini==anio_fin){					
						p_parametros.put("fecha_inicial", sel_cal_cumpleanios.getFecha1String());
						p_parametros.put("fecha_final", sel_cal_cumpleanios.getFecha2String());
						//						p_parametros.put("fecha_actual", utilitario.getAnio(sel_cal_cumpleanios.getFecha1String())+"-1-1");
						p_parametros.put("titulo","LISTADO DE CUMPLEAñOS");
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());					
						sel_cal_cumpleanios.cerrar();
						sef_reporte.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "Debe Selecionar las fechas dentro del mismo Año");
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
				}
			}
		}else if(rep_reporte.getReporteSelecionado().equals("Localidades de Trabajo")){
			if(rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				sel_tab_sucursal.getTab_seleccion().setSql("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by  nom_sucu");
				sel_tab_sucursal.getTab_seleccion().ejecutarSql();
				sel_tab_sucursal.dibujar();
			}else if(sel_tab_sucursal.isVisible()){
				if(sel_tab_sucursal.getSeleccionados()!=null && !sel_tab_sucursal.getSeleccionados().isEmpty()){									
					p_parametros.put("ide_sucu_t", sel_tab_sucursal.getSeleccionados());
					sel_tab_area.getTab_seleccion().setSql("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare in (SELECT ide_geare FROM gen_departamento_sucursal where ide_sucu in("+sel_tab_sucursal.getSeleccionados()+"))");
					sel_tab_area.getTab_seleccion().ejecutarSql();
					sel_tab_sucursal.cerrar();
					sel_tab_area.dibujar();					
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No Debe seleccionar al menos una Sucursal");
				}
			}else if(sel_tab_area.isVisible()){
				if(sel_tab_area.getSeleccionados()!=null && !sel_tab_area.getSeleccionados().isEmpty()){
					p_parametros.put("ide_geare", sel_tab_area.getSeleccionados());
					sel_tab_departamento.getTab_seleccion().setSql("SELECT ide_gedep,detalle_gedep FROM gen_departamento where ide_geare in("+sel_tab_area.getSeleccionados()+") order by detalle_gedep");
					sel_tab_departamento.getTab_seleccion().ejecutarSql();
					sel_tab_area.cerrar();
					sel_tab_departamento.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No Debe seleccionar al menos una �rea");
				}
			}else if(sel_tab_departamento.isVisible()){
				if(sel_tab_departamento.getSeleccionados()!=null && !sel_tab_departamento.getSeleccionados().isEmpty()){
					p_parametros.put("ide_gedep", sel_tab_departamento.getSeleccionados());		
    				set_empleado.getTab_seleccion().setSql("SELECT emp.ide_gtemp,emp.documento_identidad_gtemp, " +
							"emp.apellido_paterno_gtemp ||' '|| emp.apellido_materno_gtemp ||' '|| emp.primer_nombre_gtemp ||' '|| emp.segundo_nombre_gtemp as nombres FROM gth_empleado emp " +
							"inner join gen_empleados_departamento_par edp on emp.ide_gtemp=edp.ide_gtemp " +
							"where edp.ide_gedep in ("+sel_tab_departamento.getSeleccionados()+") and edp.activo_geedp=TRUE");
					set_empleado.getTab_seleccion().ejecutarSql();
					sel_tab_departamento.cerrar();
					set_empleado.dibujar();				
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No Debe seleccionar al menos una Departamento");
				}				
			}	else if(set_empleado.isVisible()){
				if(set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()){
					p_parametros.put("ide_gtemp", set_empleado.getSeleccionados());			
					set_empleado.cerrar();
					p_parametros.put("titulo","LOCALIDADES DE TRABAJO");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();	
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No Debe seleccionar al menos un Empleado");
				}				
			}			
		}


		else if (rep_reporte.getReporteSelecionado().equals("Detalle Acumula Fondo")) {
			//String str_activos="";
			if (rep_reporte.isVisible()) {
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				set_sucursal.getTab_seleccion().setSql("SELECT IDE_SUCU, " +
						"NOM_SUCU " +
						"FROM SIS_SUCURSAL " +
						"ORDER BY NOM_SUCU ASC ");
				set_sucursal.getTab_seleccion().getColumna("NOM_SUCU").setFiltro(true);
				set_sucursal.getTab_seleccion().ejecutarSql();
				set_sucursal.getBot_aceptar().setMetodo("aceptarReporte");				
				set_sucursal.dibujar();
			}else if (set_sucursal.isVisible()) {
				if(set_sucursal.getSeleccionados()!=null && !set_sucursal.getSeleccionados().isEmpty()){
					System.out.println("IDE_SUC"+set_sucursal.getSeleccionados());
					parametro1=set_sucursal.getSeleccionados();
					p_parametros.put("IDE_SUC", set_sucursal.getSeleccionados());
					set_departamento.getTab_seleccion().setSql("SELECT DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
							"FROM GEN_DEPARTAMENTO DEP " +
							"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GEDEP=DEP.IDE_GEDEP " +
							"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EDP.IDE_SUCU " +
							"AND EDP.IDE_SUCU in("+set_sucursal.getSeleccionados()+")" +
							"GROUP BY DEP.IDE_GEDEP,DEP.DETALLE_GEDEP " +
							"ORDER BY DEP.DETALLE_GEDEP");
					set_departamento.getTab_seleccion().getColumna("DETALLE_GEDEP").setFiltro(true);
					set_departamento.getTab_seleccion().ejecutarSql();
					set_departamento.getBot_aceptar().setMetodo("aceptarReporte");
					set_sucursal.cerrar();
					set_departamento.dibujar();
				}	
				else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Sucursal");
				}
			}
			else if (set_departamento.isVisible()) {

				if(set_departamento.getSeleccionados()!=null && !set_departamento.getSeleccionados().isEmpty())
				{System.out.println("IDE_SUC primera"+set_sucursal.getSeleccionados());
				p_parametros.put("IDE_GEDEP", set_departamento.getSeleccionados());
				set_empleado_deducible.getTab_seleccion().setSql("select IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
						"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
						"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
						"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
						"from GTH_EMPLEADO EMP " +
						"left join GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
						"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
						"WHERE EDP.IDE_GEDEP IN("+set_departamento.getSeleccionados()+") " +
						"AND EDP.IDE_SUCU in("+parametro1+") " +
						"AND EDP.ACTIVO_GEEDP=TRUE " +
						"ORDER BY NOMBRES ASC");
				set_empleado_deducible.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
				set_empleado_deducible.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
				set_empleado_deducible.getTab_seleccion().ejecutarSql();
				set_empleado_deducible.getBot_aceptar().setMetodo("aceptarReporte");
				set_empleado_deducible.getTab_seleccion().imprimirSql();
				//					str_activos=lis_activo.getSeleccionados();
				set_departamento.cerrar();
				System.out.println("IDE_SUCdssd"+parametro1);
				set_empleado_deducible.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Departamento");
				}

			}else if (set_empleado_deducible.isVisible()) {
				if (set_empleado_deducible.getSeleccionados()!=null && !set_empleado_deducible.getSeleccionados().isEmpty()) {
					System.out.println("2EMPLEADOS..."+set_empleado_deducible.getSeleccionados());
					System.out.println("IDE_SUC"+set_sucursal.getSeleccionados());
					p_parametros.put("IDE_GTEMP", set_empleado_deducible.getSeleccionados());
					p_parametros.put("titulo","DETALLE ACUMULACION FONDO");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					set_empleado_deducible.cerrar();
					sef_reporte.dibujar();
				}else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}

			}
		}

		//Detalle Ingreso, Salida, Cambio Ocasional config reporte tipos de Acciones
		else if (rep_reporte.getReporteSelecionado().equals("Detalle Salida,Ingreso,Cambio Empleado")) {
			if(rep_reporte.isVisible()){
				p_parametros =new HashMap();
				rep_reporte.cerrar();
				sel_tab_sucursal.getTab_seleccion().setSql("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by  nom_sucu");
				sel_tab_sucursal.getTab_seleccion().ejecutarSql();
				sel_tab_sucursal.dibujar();		
			}else if(sel_tab_sucursal.isVisible()){
				if(sel_tab_sucursal.getSeleccionados()!=null && !sel_tab_sucursal.getSeleccionados().isEmpty()){
					System.out.println("SUCURSAL"+sel_tab_sucursal.getSeleccionados());
					p_parametros.put("IDE_SUC", sel_tab_sucursal.getSeleccionados());				
					sel_tab_area.getTab_seleccion().setSql("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare in (SELECT ide_geare FROM gen_departamento_sucursal where ide_sucu in("+sel_tab_sucursal.getSeleccionados()+"))");
					sel_tab_area.getTab_seleccion().ejecutarSql();
					sel_tab_sucursal.cerrar();
					sel_tab_area.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una sucursal");
				}
			}else if(sel_tab_area.isVisible()){
				if(sel_tab_area.getSeleccionados()!=null && !sel_tab_area.getSeleccionados().isEmpty()){
					System.out.println("GEARE"+sel_tab_area.getSeleccionados());
					p_parametros.put("IDE_GEARE", sel_tab_area.getSeleccionados());
					sel_cal_cumpleanios.setFecha1(null);
					sel_cal_cumpleanios.setFecha2(null);
					sel_tab_area.cerrar();
					sel_cal_cumpleanios.dibujar();							
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un Departamento");
				}
			}else if(sel_cal_cumpleanios.isVisible()){
				if(sel_cal_cumpleanios.isFechasValidas()){
					//if (anio_ini>=anio_fin){
					System.out.println(""+sel_cal_cumpleanios.getFecha1String());
					System.out.println(""+sel_cal_cumpleanios.getFecha2String());
					p_parametros.put("FECHA_INICIO", sel_cal_cumpleanios.getFecha1String());
					p_parametros.put("FECHA_FIN", sel_cal_cumpleanios.getFecha2String());
					p_parametros.put("titulo","DETALLE INGRESO,SALIDA,CAMBIO EMPLEADO");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());					
					sel_cal_cumpleanios.cerrar();
					sef_reporte.dibujar();

					//	}else{
					//			utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
					//		}
					//NUEVA CORRECCION
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
				}



			}
		}

		//el de accciones

		else if (rep_reporte.getReporteSelecionado().equals("Detalle Acciones Empleado x Fecha")) {
			if(rep_reporte.isVisible()){
				p_parametros =new HashMap();
				rep_reporte.cerrar();
				sel_tab_sucursal.getTab_seleccion().setSql("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by  nom_sucu");
				sel_tab_sucursal.getTab_seleccion().ejecutarSql();
				sel_tab_sucursal.dibujar();		
			}else if(sel_tab_sucursal.isVisible()){
				if(sel_tab_sucursal.getSeleccionados()!=null && !sel_tab_sucursal.getSeleccionados().isEmpty()){
					System.out.println("SUCURSAL"+sel_tab_sucursal.getSeleccionados());
					p_parametros.put("IDE_SUC", sel_tab_sucursal.getSeleccionados());				
					sel_tab_area.getTab_seleccion().setSql("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare in (SELECT ide_geare FROM gen_departamento_sucursal where ide_sucu in("+sel_tab_sucursal.getSeleccionados()+"))");
					sel_tab_area.getTab_seleccion().ejecutarSql();
					sel_tab_sucursal.cerrar();
					sel_tab_area.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una sucursal");
				}
			}else if(sel_tab_area.isVisible()){
				if(sel_tab_area.getSeleccionados()!=null && !sel_tab_area.getSeleccionados().isEmpty()){
					System.out.println("GEARE"+sel_tab_area.getSeleccionados());
					p_parametros.put("IDE_GEARE", sel_tab_area.getSeleccionados());
					sel_cal_cumpleanios.setFecha1(null);
					sel_cal_cumpleanios.setFecha2(null);
					sel_tab_area.cerrar();
					sel_cal_cumpleanios.dibujar();							
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un Departamento");
				}
			}else if(sel_cal_cumpleanios.isVisible()){
				if(sel_cal_cumpleanios.isFechasValidas()){
					//if (anio_ini>=anio_fin){
					System.out.println(""+sel_cal_cumpleanios.getFecha1String());
					System.out.println(""+sel_cal_cumpleanios.getFecha2String());
					p_parametros.put("FECHA_INICIO", sel_cal_cumpleanios.getFecha1String());
					p_parametros.put("FECHA_FIN", sel_cal_cumpleanios.getFecha2String());
					p_parametros.put("titulo","DETALLE ACCIONES EMPLEADO POR FECHA");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());					
					sel_cal_cumpleanios.cerrar();
					sef_reporte.dibujar();

					//	}else{
					//			utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
					//		}
					//NUEVA CORRECCION
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
				}



			}
		}		else if(rep_reporte.getReporteSelecionado().equals("Vencimiento de Contratos")){
			if(rep_reporte.isVisible()){
				p_parametros =new HashMap();
				rep_reporte.cerrar();
				sel_tab_sucursal.getTab_seleccion().setSql("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by  nom_sucu");
				sel_tab_sucursal.getTab_seleccion().ejecutarSql();
				sel_tab_sucursal.dibujar();		
			}else if(sel_tab_sucursal.isVisible()){
				if(sel_tab_sucursal.getSeleccionados()!=null && !sel_tab_sucursal.getSeleccionados().isEmpty()){
					p_parametros.put("ide_sucu_t", sel_tab_sucursal.getSeleccionados());				
					sel_tab_area.getTab_seleccion().setSql("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare in (SELECT ide_geare FROM gen_departamento_sucursal where ide_sucu in("+sel_tab_sucursal.getSeleccionados()+"))");
					sel_tab_area.getTab_seleccion().ejecutarSql();
					sel_tab_sucursal.cerrar();
					sel_tab_area.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una sucursal");
				}
			}else if(sel_tab_area.isVisible()){
				if(sel_tab_area.getSeleccionados()!=null && !sel_tab_area.getSeleccionados().isEmpty()){
					p_parametros.put("ide_geare", sel_tab_area.getSeleccionados());
					sel_tab_departamento.getTab_seleccion().setSql("SELECT ide_gedep,detalle_gedep FROM gen_departamento where ide_geare in("+sel_tab_area.getSeleccionados()+") order by detalle_gedep");
					sel_tab_departamento.getTab_seleccion().ejecutarSql();
					sel_tab_area.cerrar();
					sel_tab_departamento.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una �rea");
				}
			}else if(sel_tab_departamento.isVisible()){
				if(sel_tab_departamento.getSeleccionados()!=null && ! sel_tab_departamento.getSeleccionados().isEmpty()){
					p_parametros.put("ide_gedep", sel_tab_departamento.getSeleccionados());					
					sel_tab_departamento.cerrar();
					sel_cal_cumpleanios.setFecha1(null);
					sel_cal_cumpleanios.setFecha2(null);					
					sel_cal_cumpleanios.dibujar();							
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un Departamento");
				}
			}else if(sel_cal_cumpleanios.isVisible()){
				if(sel_cal_cumpleanios.isFechasValidas()){
					int anio_ini=utilitario.getAnio(sel_cal_cumpleanios.getFecha1String());
					int anio_fin=utilitario.getAnio(sel_cal_cumpleanios.getFecha2String());
					if (anio_ini==anio_fin){					
						p_parametros.put("fecha_inicial", sel_cal_cumpleanios.getFecha1String());
						p_parametros.put("fecha_final", sel_cal_cumpleanios.getFecha2String());
						p_parametros.put("titulo","VENCIMIENTO DE CONTRATOS");
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());					
						sel_cal_cumpleanios.cerrar();
						sef_reporte.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "Debe Selecionar las fechas dentro del mismo Año");
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "La fecha final es menor a la fecha inicial");
				}
			}
		}
		else if(rep_reporte.getReporteSelecionado().equals("Datos - Direccion - Correo - Tel�fono")){
			if(rep_reporte.isVisible()){
				p_parametros =new HashMap();
				rep_reporte.cerrar();
				sel_tab_sucursal.getTab_seleccion().setSql("SELECT ide_sucu,nom_sucu  FROM sis_sucursal order by  nom_sucu");
				sel_tab_sucursal.getTab_seleccion().ejecutarSql();
				sel_tab_sucursal.dibujar();		
			}else if(sel_tab_sucursal.isVisible()){
				if(sel_tab_sucursal.getSeleccionados()!=null && !sel_tab_sucursal.getSeleccionados().isEmpty()){
					p_parametros.put("ide_sucu_t", sel_tab_sucursal.getSeleccionados());				
					sel_tab_area.getTab_seleccion().setSql("SELECT ide_geare,detalle_geare FROM gen_area where ide_geare in (SELECT ide_geare FROM gen_departamento_sucursal where ide_sucu in("+sel_tab_sucursal.getSeleccionados()+"))");
					sel_tab_area.getTab_seleccion().ejecutarSql();
					sel_tab_sucursal.cerrar();
					sel_tab_area.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una sucursal");
				}
			}else if(sel_tab_area.isVisible()){
				if(sel_tab_area.getSeleccionados()!=null && !sel_tab_area.getSeleccionados().isEmpty()){
					p_parametros.put("ide_geare", sel_tab_area.getSeleccionados());
					sel_tab_departamento.getTab_seleccion().setSql("SELECT ide_gedep,detalle_gedep FROM gen_departamento where ide_geare in("+sel_tab_area.getSeleccionados()+") order by detalle_gedep");
					sel_tab_departamento.getTab_seleccion().ejecutarSql();
					sel_tab_area.cerrar();
					sel_tab_departamento.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos una �rea");
				}
			}else if(sel_tab_departamento.isVisible()){
				if(sel_tab_departamento.getSeleccionados()!=null && ! sel_tab_departamento.getSeleccionados().isEmpty()){
					p_parametros.put("ide_gedep", sel_tab_departamento.getSeleccionados());					
					sel_tab_departamento.cerrar();
					p_parametros.put("titulo","DATOS - DIRECCION - CORREO -TELEFONO");
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sef_reporte.dibujar();					
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "Debe seleccionar al menos un Departamento");
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
		}else if(tab_seguro_vida.isFocus()){
			tab_seguro_vida.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_seguro_vida.ejecutarSql();
			tab_beneficiario_seguro.setCondicion("IDE_GTSEV="+tab_seguro_vida.getValor("IDE_GTSEV"));
			tab_beneficiario_seguro.ejecutarSql();
		}
	}



	/**
	 * metodo para el boton de navegacion de paginas "inicio" , muestra el primer registro de la tabla 
	 * 
	 */

	@Override
	public void inicio(){
		super.inicio();
		if (tab_conyuge.isFocus()){
			filtrarTelefonosConyugue();
			filtrarUnionLibreConyugue();
		}else if (tab_persona_emergencia.isFocus()){
			filtrarTelefonoPersonaEmergencia();
			filtrarDireccionPersonaEmergencia();
		}else if (tab_seguro_vida.isFocus()){
			filtrarBeneficiariosSeguroVida();
		}else if (tab_negocio_empl.isFocus()){
			filtrarParticipantesNegocio();
			filtrarTelefonosNegocio();
			filtrarDireccionNegocio();
			filtrarAnexosNegocio();
		}else if (tab_empleado_departamento.isFocus()){
			actualizarCombosDepartamentoEmpleado();
		}
	}

	private void filtrarParticipantesNegocio(){
		tab_paticipantes_negocio.setCondicion("IDE_GTNEE="+tab_negocio_empl.getValor("IDE_GTNEE"));
		tab_paticipantes_negocio.ejecutarSql();
	}

	private void filtrarTelefonosNegocio(){
		tab_telefonos.setCondicion("IDE_GTNEE="+tab_negocio_empl.getValor("IDE_GTNEE"));
		tab_telefonos.ejecutarSql();
	}

	private void filtrarDireccionNegocio(){
		tab_direccion.setCondicion("IDE_GTNEE="+tab_negocio_empl.getValor("IDE_GTNEE"));
		tab_direccion.ejecutarSql();
	}

	private void filtrarAnexosNegocio(){
		tab_archivo_empleado.setCondicion("IDE_GTNEE="+tab_negocio_empl.getValor("IDE_GTNEE"));
		tab_archivo_empleado.ejecutarSql();
	}


	/**
	 * metodo para el boton de navegacion de paginas fin , muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin(){
		super.fin();
		if (tab_conyuge.isFocus()){
			filtrarTelefonosConyugue();
			filtrarUnionLibreConyugue();
		}else if (tab_persona_emergencia.isFocus()){
			filtrarTelefonoPersonaEmergencia();
			filtrarDireccionPersonaEmergencia();
		}else if (tab_seguro_vida.isFocus()){
			filtrarBeneficiariosSeguroVida();
		}else if (tab_negocio_empl.isFocus()){
			filtrarParticipantesNegocio();
			filtrarTelefonosNegocio();
			filtrarDireccionNegocio();
			filtrarAnexosNegocio();
		}else if (tab_empleado_departamento.isFocus()){
			actualizarCombosDepartamentoEmpleado();
		}
		//
	}

	/**
	 * metodo para el boton de navegacion de paginas atras , retrocede un registro de la tabla 
	 * 
	 */

	@Override
	public void atras(){
		super.atras();
		if (tab_conyuge.isFocus()){
			filtrarTelefonosConyugue();
			filtrarUnionLibreConyugue();
		}else if (tab_persona_emergencia.isFocus()){
			filtrarTelefonoPersonaEmergencia();
			filtrarDireccionPersonaEmergencia();
		}else if (tab_seguro_vida.isFocus()){
			filtrarBeneficiariosSeguroVida();
		}else if (tab_negocio_empl.isFocus()){
			filtrarParticipantesNegocio();
			filtrarTelefonosNegocio();
			filtrarDireccionNegocio();
			filtrarAnexosNegocio();
		}else if (tab_empleado_departamento.isFocus()){
			actualizarCombosDepartamentoEmpleado();
		}
	}

	/**
	 * metodo para el boton de navegacion de paginas siguiente , avanza un registro de la tabla 
	 * 
	 */
	@Override
	public void siguiente(){
		super.siguiente();
		if (tab_conyuge.isFocus()){
			filtrarTelefonosConyugue();
			filtrarUnionLibreConyugue();
		}else if (tab_persona_emergencia.isFocus()){
			filtrarTelefonoPersonaEmergencia();
			filtrarDireccionPersonaEmergencia();
		}else if (tab_seguro_vida.isFocus()){
			filtrarBeneficiariosSeguroVida();
		}else if (tab_negocio_empl.isFocus()){
			filtrarParticipantesNegocio();
			filtrarTelefonosNegocio();
			filtrarDireccionNegocio();
			filtrarAnexosNegocio();
		}else if (tab_empleado_departamento.isFocus()){
			actualizarCombosDepartamentoEmpleado();
		}
	}

	/**
	 * forma el menu de la parte derecha con las opciones de la ficha del
	 * empleado
	 */
	private void contruirMenu() {
		pam_menu.setWidgetVar("100%");

		// SUB MENU 1
		Submenu sum_empleado = new Submenu();
		sum_empleado.setLabel("DATOS PERSONALES");
		pam_menu.getChildren().add(sum_empleado);

		// ITEM 1 : OPCION 0
		ItemMenu itm_datos_empl = new ItemMenu();
		itm_datos_empl.setValue("DATOS PERSONALES");
		itm_datos_empl.setIcon("ui-icon-person");
		itm_datos_empl.setMetodo("dibujarDatosPersona");
		itm_datos_empl.setUpdate("pan_opcion");
		sum_empleado.getChildren().add(itm_datos_empl);


		// ITEM 3 : OPCION 2
		ItemMenu itm_empleado_departamento = new ItemMenu();
		itm_empleado_departamento.setValue("ACCIONES DE PERSONAL");
		itm_empleado_departamento.setIcon("ui-icon-person");
		itm_empleado_departamento.setMetodo("dibujarDatosEmpleadoDepartamento");
		itm_empleado_departamento.setUpdate("pan_opcion");
		sum_empleado.getChildren().add(itm_empleado_departamento);

		// ITEM 4 : OPCION 3
		ItemMenu itm_datos_documentacion = new ItemMenu();
		itm_datos_documentacion.setValue("DOCUMENTACION");
		itm_datos_documentacion.setIcon("ui-icon-person");
		itm_datos_documentacion.setMetodo("dibujarDatosDocumentacion");
		itm_datos_documentacion.setUpdate("pan_opcion");
		sum_empleado.getChildren().add(itm_datos_documentacion);

		// ITEM 5 : OPCION 4		
		ItemMenu itm_datos_emergencia = new ItemMenu();
		itm_datos_emergencia.setValue("EN CASO DE EMERGENCIA");
		itm_datos_emergencia.setIcon("ui-icon-person");
		itm_datos_emergencia.setMetodo("dibujarDatosEmergencia");
		itm_datos_emergencia.setUpdate("pan_opcion");
		sum_empleado.getChildren().add(itm_datos_emergencia);

		// ITEM 6 : OPCION 28		
		ItemMenu itm_cuenta_anticipo = new ItemMenu();
		itm_cuenta_anticipo.setValue("CUENTA ANTICIPO");
		itm_cuenta_anticipo.setIcon("ui-icon-person");
		itm_cuenta_anticipo.setMetodo("dibujarCuentaAnticipo");
		itm_cuenta_anticipo.setUpdate("pan_opcion");
		sum_empleado.getChildren().add(itm_cuenta_anticipo);

		// SUB MENU 6
		Submenu sum_datos_formacion_academica = new Submenu();
		sum_datos_formacion_academica.setLabel("EDUCACION");
		pam_menu.getChildren().add(sum_datos_formacion_academica);

		// ITEM 1 : OPCION 10
		ItemMenu itm_datos_educacion = new ItemMenu();
		itm_datos_educacion.setValue("EDUCACION");
		itm_datos_educacion.setIcon("ui-icon-person");
		itm_datos_educacion.setMetodo("dibujarDatosEducacion");
		itm_datos_educacion.setUpdate("pan_opcion");
		sum_datos_formacion_academica.getChildren().add(itm_datos_educacion);

		// ITEM 2 : OPCION 11		
		ItemMenu itm_datos_idiomas = new ItemMenu();
		itm_datos_idiomas.setValue("IDIOMAS");
		itm_datos_idiomas.setIcon("ui-icon-person");
		itm_datos_idiomas.setMetodo("dibujarDatosIdiomas");
		itm_datos_idiomas.setUpdate("pan_opcion");
		sum_datos_formacion_academica.getChildren().add(itm_datos_idiomas);


		// SUB MENU 7
		Submenu sum_datos_experiencia_laboral = new Submenu();
		sum_datos_experiencia_laboral.setLabel("EXPERIENCIA LABORAL Y CAPACITACION");
		pam_menu.getChildren().add(sum_datos_experiencia_laboral);

		// ITEM 1 : OPCION 12
		ItemMenu itm_datos_capacitacion = new ItemMenu();
		itm_datos_capacitacion.setValue("DATOS DE CAPACITACION");
		itm_datos_capacitacion.setIcon("ui-icon-person");
		itm_datos_capacitacion.setMetodo("dibujarDatosCapacitacion");
		itm_datos_capacitacion.setUpdate("pan_opcion");
		sum_datos_experiencia_laboral.getChildren().add(itm_datos_capacitacion);




		// ITEM 4 : OPCION 14		
		ItemMenu itm_datos_experiencia_laboral = new ItemMenu();
		itm_datos_experiencia_laboral.setValue("DATOS EXPERIENCIA LABORAL");
		itm_datos_experiencia_laboral.setIcon("ui-icon-person");
		itm_datos_experiencia_laboral.setMetodo("dibujarDatosExperienciaLaboral");
		itm_datos_experiencia_laboral.setUpdate("pan_opcion");
		sum_datos_experiencia_laboral.getChildren().add(itm_datos_experiencia_laboral);

		// SUB MENU 9
		Submenu sum_datos_situacion_financiera = new Submenu();
		sum_datos_situacion_financiera.setLabel("INFORMACION SITUACION ECONOMICA");
		pam_menu.getChildren().add(sum_datos_situacion_financiera);

		// ITEM 7 : OPCION 22
		ItemMenu itm_datos_cuenta_bancaria = new ItemMenu();
		itm_datos_cuenta_bancaria.setValue("CUENTAS BANCARIAS");
		itm_datos_cuenta_bancaria.setIcon("ui-icon-person");
		itm_datos_cuenta_bancaria.setMetodo("dibujarDatosCuentaBancaria");
		itm_datos_cuenta_bancaria.setUpdate("pan_opcion");
		sum_datos_situacion_financiera.getChildren().add(itm_datos_cuenta_bancaria);

		// SUB MENU 11
		Submenu sum_sri_proy_ing = new Submenu();
		sum_sri_proy_ing.setLabel("SRI");
		pam_menu.getChildren().add(sum_sri_proy_ing);

		// ITEM 1 : OPCION 27
		ItemMenu itm_sri_proy_ing = new ItemMenu();
		itm_sri_proy_ing.setValue("PROYECCION DE INGRESOS");
		itm_sri_proy_ing.setIcon("ui-icon-person");
		itm_sri_proy_ing.setMetodo("dibujarDatosProyeccionIngresos");
		itm_sri_proy_ing.setUpdate("pan_opcion");
		sum_sri_proy_ing.getChildren().add(itm_sri_proy_ing);

		// ITEM 1 : OPCION 29
		ItemMenu itm_sri_gasto_deduc = new ItemMenu();
		itm_sri_gasto_deduc.setValue("GASTOS DEDUCIBLES");
		itm_sri_gasto_deduc.setIcon("ui-icon-person");
		itm_sri_gasto_deduc.setMetodo("dibujarGastosDeducibles");
		itm_sri_gasto_deduc.setUpdate("pan_opcion");
		sum_sri_proy_ing.getChildren().add(itm_sri_gasto_deduc);

	}

	/**
	 * muestra en pantalla la tabla con los datos del empleado
	 */
	public void dibujarDatosPersona() {
		str_opcion = "0";
		limpiarPanel();
		tab_empleado = new Tabla();
		tab_empleado.setId("tab_empleado");
		tab_empleado.setTabla("GTH_EMPLEADO", "IDE_GTEMP", 1);
		tab_empleado.getColumna("IDE_GTGEN").setCombo("GTH_GENERO", "IDE_GTGEN", "DETALLE_GTGEN", "");
		tab_empleado.getColumna("IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI", "");
		tab_empleado.getColumna("IDE_GTESC").setCombo("GTH_ESTADO_CIVIL", "IDE_GTESC", "DETALLE_GTESC", "");
		tab_empleado.getColumna("IDE_GEDIP").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
		tab_empleado.getColumna("IDE_GEDIP").setAutoCompletar();
		//tab_empleado.getColumna("IDE_GTTIS").setCombo("GTH_TIPO_SANGRE", "IDE_GTTIS", "DETALLE_GTTIS", "");
		//tab_empleado.getColumna("IDE_GTNAC").setCombo("GTH_NACIONALIDAD", "IDE_GTNAC", "DETALLE_GTNAC", "");
		tab_empleado.getColumna("IDE_GTNAC").setValorDefecto(par_nacionalidad);
		tab_empleado.getColumna("IDE_GTTIS").setValorDefecto(par_tipo_sangre);
		tab_empleado.getColumna("ide_gtesc").setValorDefecto(par_estado_civil_soltero);		
		tab_empleado.getColumna("PRIMER_NOMBRE_GTEMP").setRequerida(true);
		tab_empleado.getColumna("APELLIDO_PATERNO_GTEMP").setRequerida(true);
		tab_empleado.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setRequerida(true);
		tab_empleado.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setUnico(true);
		tab_empleado.getColumna("IDE_GTNAC").setRequerida(true);
		tab_empleado.getColumna("IDE_GTTIS").setRequerida(true);
		//			tab_empleado.getColumna("IDE_GTTIC").setRequerida(true);
		tab_empleado.getColumna("IDE_GTGEN").setRequerida(true);
		tab_empleado.getColumna("IDE_GTESC").setRequerida(true);
		tab_empleado.getColumna("IDE_GEDIP").setRequerida(true);
		tab_empleado.getColumna("PATH_FOTO_GTEMP").setUpload();
		//tab_empleado.getColumna("PATH_FOTO_GTEMP").setValorDefecto("imagenes/im_empleado.png");
		tab_empleado.getColumna("PATH_FOTO_GTEMP").setImagen("100", "80");
		tab_empleado.getColumna("SEPARACION_BIENES_GTEMP").setCheck();
		tab_empleado.getColumna("SEPARACION_BIENES_GTEMP").setValorDefecto("false");
		tab_empleado.getColumna("fecha_ingreso_gtemp").setMetodoChange("mismaFecha");
		tab_empleado.getColumna("DISCAPACITADO_GTEMP").setCheck();
		tab_empleado.getColumna("PATH_FIRMA_GTEMP").setUpload();
		tab_empleado.getColumna("PATH_FIRMA_GTEMP").setImagen("80", "100");
                
		tab_empleado.getColumna("carnet_extranjeria_gtemp").setVisible(false);
		tab_empleado.getColumna("fecha_ingreso_pais_gtemp").setVisible(false);
		tab_empleado.getColumna("ide_gttis").setVisible(false);
		tab_empleado.getColumna("ide_gtnac").setVisible(false);
		tab_empleado.getColumna("SEPARACION_BIENES_GTEMP").setVisible(false);
		tab_empleado.getColumna("CARGO_PUBLICO_GTEMP").setVisible(false);
//		tab_empleado.getColumna("fecha_ingreso_grupo_gtemp").setVisible(false);
		tab_empleado.getColumna("ide_gtesc").setVisible(false);

		List lista = new ArrayList();
		Object fila1[] = {
				"0", "PRIVADO"
		};
		Object fila2[] = {
				"1", "PUBLICO"
		};
		lista.add(fila1);
		lista.add(fila2);
		tab_empleado.getColumna("CARGO_PUBLICO_GTEMP").setRadio(lista, "1");
		tab_empleado.getColumna("CARGO_PUBLICO_GTEMP").setRadioVertical(true);
		tab_empleado.getColumna("ACTIVO_GTEMP").setCheck();
		tab_empleado.setCondicion("IDE_GTEMP=" + aut_empleado.getValor());
		tab_empleado.setTipoFormulario(true);
		tab_empleado.getGrid().setColumns(4);
		tab_empleado.setMostrarNumeroRegistros(false);
		tab_empleado.setValidarInsertar(true);
		tab_empleado.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_empleado);

		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_direccion = new Tabla();
		tab_direccion.setId("tab_direccion");
		tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
		tab_direccion.setTabla("GTH_DIRECCION", "IDE_GTDIR", 29);
		tab_direccion.getColumna("IDE_GTPEE").setVisible(false);
		tab_direccion.getColumna("ACTIVO_GTDIR").setCheck();
		tab_direccion.getColumna("NOTIFICACION_GTDIR").setCheck();
		tab_direccion.getColumna("IDE_GTEMP").setVisible(false);
		tab_direccion.getColumna("IDE_GTNEE").setVisible(false);
		tab_direccion.getColumna("GTH_IDE_GTEMP").setVisible(false);
		tab_direccion.getColumna("IDE_GEDIP").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());

		tab_direccion.getColumna("IDE_GEDIP").setAutoCompletar();
		//tab_direccion.getColumna("IDE_GEDIP").setEstilo("width:200px");
		tab_direccion.getColumna("IDE_GEDIP").setAncho(200);
		tab_direccion.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
		tab_direccion.getColumna("IDE_GTELE").setVisible(false);
		tab_direccion.getColumna("IDE_NRGAR").setVisible(false);
		tab_direccion.getColumna("IDE_GEBEN").setVisible(false);
		tab_direccion.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
		tab_direccion.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_direccion);


		tab_telefonos = new Tabla();
		tab_telefonos.setId("tab_telefonos");
		tab_telefonos.setIdCompleto("tab_tabulador:tab_telefonos");
		tab_telefonos.setTabla("GTH_TELEFONO", "IDE_GTTEL", 7);
		tab_telefonos.getColumna("IDE_GTTIT").setCombo("GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT", "");
		tab_telefonos.getColumna("ACTIVO_GTTEL").setCheck();
		tab_telefonos.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
		tab_telefonos.getColumna("IDE_GTEMP").setVisible(false);
		tab_telefonos.getColumna("IDE_GTCON").setVisible(false);
		tab_telefonos.getColumna("IDE_GTCON").setValorDefecto(null);
		tab_telefonos.getColumna("IDE_GTPEE").setVisible(false);
		tab_telefonos.getColumna("GTH_IDE_GTEMP").setVisible(false);
		tab_telefonos.getColumna("NOTIFICACION_GTTEL").setCheck();
		tab_telefonos.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
		tab_telefonos.setTipoFormulario(false);
		tab_telefonos.setMostrarNumeroRegistros(false);
		tab_telefonos.getColumna("IDE_GTELE").setVisible(false);
		tab_telefonos.getColumna("IDE_NRGAR").setVisible(false);
		tab_telefonos.getColumna("IDE_GEBEN").setVisible(false);

		tab_telefonos.dibujar();
		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setPanelTabla(tab_telefonos);

		tab_correos = new Tabla();
		tab_correos.setId("tab_correos");
		tab_correos.setTabla("GTH_CORREO", "IDE_GTCOR", 9);
		tab_correos.setIdCompleto("tab_tabulador:tab_correos");
		tab_correos.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
		tab_correos.getColumna("IDE_GTEMP").setVisible(false);
		tab_correos.getColumna("IDE_GEBEN").setVisible(false);
		tab_correos.getColumna("DETALLE_GTCOR");



		tab_correos.getColumna("ACTIVO_GTCOR").setCheck();
		tab_correos.getColumna("NOTIFICACION_GTCOR").setCheck();
		tab_correos.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
		tab_correos.setTipoFormulario(false);
		tab_correos.setMostrarNumeroRegistros(false);
		tab_correos.dibujar();
		PanelTabla pat_panel4 = new PanelTabla();
		pat_panel4.setPanelTabla(tab_correos);

		tab_discapacidad = new Tabla();
		tab_discapacidad.setId("tab_discapacidad");

		tab_discapacidad.setTabla("GTH_DISCAPACIDAD_EMPLEADO", "IDE_GTDIE", 40);
		tab_discapacidad.setIdCompleto("tab_tabulador:tab_discapacidad");
		tab_discapacidad.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
		tab_discapacidad.getColumna("IDE_GTEMP").setVisible(false);
		tab_discapacidad.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
		tab_discapacidad.setTipoFormulario(false);
		tab_discapacidad.setMostrarNumeroRegistros(false);
		tab_discapacidad.getColumna("IDE_GTTDS").setCombo("GTH_TIPO_DISCAPACIDAD","IDE_GTTDS","DETALLE_GTTDS","");
		tab_discapacidad.getColumna("IDE_GTGRD").setCombo("GTH_GRADO_DISCAPACIDAD","IDE_GTGRD","DETALLE_GTGRD","");
		tab_discapacidad.getColumna("ACTIVO_GTDIE").setCheck();
		tab_discapacidad.getColumna("ACTIVO_GTDIE").setValorDefecto("true");
		tab_discapacidad.getColumna("PORCENTAJE_GTDIE").setVisible(true);
		tab_discapacidad.getColumna("ide_gtgrd").setVisible(false);
		tab_discapacidad.dibujar();

		PanelTabla pat_panel5 = new PanelTabla();
		pat_panel5.setPanelTabla(tab_discapacidad);



		tab_archivo_empleado.setId("tab_archivo_empleado");
		tab_archivo_empleado.setTabla("GTH_ARCHIVO_EMPLEADO", "IDE_GTAEM", 33);
		tab_archivo_empleado.getColumna("IDE_GTTAR").setCombo("GTH_TIPO_ARCHIVO", "IDE_GTTAR", "DETALLE_GTTAR", "");
		tab_archivo_empleado.getColumna("IDE_GTTAR").setEstilo("width:110px");
		tab_archivo_empleado.getColumna("ACTIVO_GTAEM").setCheck();
		tab_archivo_empleado.getColumna("ACTIVO_GTAEM").setValorDefecto("true");
		tab_archivo_empleado.getColumna("PATH_GTAEM").setUpload("archivos_empleado");
		tab_archivo_empleado.getColumna("IDE_GTNEE").setVisible(false);
		tab_archivo_empleado.getColumna("IDE_GTTDC").setCombo("GTH_TIPO_DOCUMENTO", "IDE_GTTDC", "DETALLE_GTTDC", "");
		tab_archivo_empleado.getColumna("IDE_ASPVH").setVisible(false);
		tab_archivo_empleado.getColumna("IDE_NRBEE").setVisible(false);
		tab_archivo_empleado.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
		tab_archivo_empleado.getColumna("IDE_GTEMP").setVisible(false);
		tab_archivo_empleado.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
		tab_archivo_empleado.getColumna("IDE_GTNEE").setValorDefecto(tab_negocio_empl.getValor("ide_gtnee"));
		tab_archivo_empleado.setTipoFormulario(true);
		tab_archivo_empleado.setMostrarNumeroRegistros(true);
		tab_archivo_empleado.getGrid().setColumns(9);
		tab_archivo_empleado.setIdCompleto("tab_tabulador:tab_archivo_empleado");
		tab_archivo_empleado.dibujar();
		PanelTabla pat_panel6 = new PanelTabla();
		pat_panel6.setPanelTabla(tab_archivo_empleado);




		tab_tabulador.agregarTab("DIRRECION", pat_panel2);
		tab_tabulador.agregarTab("TELEFONOS", pat_panel3);
		tab_tabulador.agregarTab("CORREOS", pat_panel4);
		tab_tabulador.agregarTab("DISCAPACIDAD", pat_panel5);
		tab_tabulador.agregarTab("DOCUMENTOS DE EMPLEADOS", pat_panel6);
		Division div1=new Division();
		div1.dividir2(pat_panel1, tab_tabulador, "65%", "H");

		pan_opcion.setTitle("DATOS EMPLEADO");
		pan_opcion.getChildren().add(div1);

	}

/////////metodo para q se me pogo la misma fecha en otro comapo
public void mismaFecha(DateSelectEvent evt){
	tab_empleado.modificar(evt); //Siempre es la primera linea
	tab_empleado.setValor("fecha_ingreso_grupo_gtemp",tab_empleado.getValor("fecha_ingreso_gtemp"));
	utilitario.addUpdateTabla(tab_empleado, "fecha_ingreso_grupo_gtemp,", "");
	//utilitario.addUpdateTabla(tab_empleado_departamento_dia, "FECHA_GEEDP", "");

}


	/**
	 * muestra en pantalla la tabla con los datos del conyugue del empleado
	 */
	public void dibujarDatosConyuge() {
		if (aut_empleado.getValor() != null) {
			str_opcion = "1";
			limpiarPanel();			
			tab_conyuge = new Tabla();
			tab_conyuge.setId("tab_conyuge");
			tab_conyuge.setTabla("GTH_CONYUGE", "IDE_GTCON", 2);
			tab_conyuge.onSelect("seleccionaTablaConyugue");
			tab_conyuge.getColumna("IDE_GTGEN").setCombo("GTH_GENERO", "IDE_GTGEN", "DETALLE_GTGEN", "");
			tab_conyuge.getColumna("IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI", "");
			tab_conyuge.getColumna("IDE_GTNAC").setCombo("GTH_NACIONALIDAD", "IDE_GTNAC", "DETALLE_GTNAC", "");
			tab_conyuge.getColumna("IDE_GTCAR").setCombo("GTH_CARGO", "IDE_GTCAR", "DETALLE_GTCAR", "");
			tab_conyuge.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_conyuge.getColumna("IDE_GTEMP").setVisible(false);
			tab_conyuge.getColumna("ACTIVO_GTCON").setCheck();
			tab_conyuge.setCondicion("IDE_GTEMP=" + aut_empleado.getValor());
			tab_conyuge.setTipoFormulario(false);
			tab_conyuge.getGrid().setColumns(4);
			tab_conyuge.setMostrarNumeroRegistros(true);
			tab_conyuge.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_conyuge);
			pan_opcion.setTitle("DATOS CONYUGUE");

			Tabulador tab_tabulador=new Tabulador();
			tab_tabulador.setId("tab_tabulador");

			tab_union_libre = new Tabla();
			tab_union_libre.setId("tab_union_libre");
			tab_union_libre.setIdCompleto("tab_tabulador:tab_union_libre");
			tab_union_libre.setTabla("GTH_UNION_LIBRE", "IDE_GTUNL", 3);
			tab_union_libre.getColumna("IDE_GTCON").setValorDefecto(tab_conyuge.getValor("IDE_GTCON"));
			tab_union_libre.getColumna("IDE_GTCON").setVisible(false);
			//tab_union_libre.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			//tab_union_libre.getColumna("IDE_GTEMP").setVisible(false);
			tab_union_libre.getColumna("ACTIVO_GTUNL").setCheck();
			tab_union_libre.setCondicion("IDE_GTCON=" + tab_conyuge.getValor("IDE_GTCON"));
			tab_union_libre.setTipoFormulario(false);
			tab_union_libre.getGrid().setColumns(4);
			tab_union_libre.setMostrarNumeroRegistros(true);
			tab_union_libre.dibujar();
			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setMensajeWarn("DATOS UNION LIBRE");
			pat_panel2.setPanelTabla(tab_union_libre);

			tab_telefonos = new Tabla();
			tab_telefonos.setId("tab_telefonos");
			tab_telefonos.setIdCompleto("tab_tabulador:tab_telefonos");
			tab_telefonos.setTabla("GTH_TELEFONO", "IDE_GTTEL", 7);
			tab_telefonos.getColumna("IDE_GTTIT").setCombo("GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT", "");
			tab_telefonos.getColumna("IDE_GTCON").setVisible(false);
			tab_telefonos.getColumna("IDE_GTPEE").setVisible(false);
			tab_telefonos.getColumna("ACTIVO_GTTEL").setCheck();
			tab_telefonos.getColumna("IDE_GTEMP").setVisible(false);
			tab_telefonos.getColumna("IDE_GTNEE").setVisible(false);
			tab_telefonos.getColumna("IDE_GTCON").setValorDefecto(tab_conyuge.getValor("ide_gtcon"));
			tab_telefonos.getColumna("GTH_IDE_GTEMP").setVisible(false);
			tab_telefonos.getColumna("NOTIFICACION_GTTEL").setCheck();
			tab_telefonos.setCondicion("IDE_GTCON =" + tab_conyuge.getValor("IDE_GTCON"));
			tab_telefonos.getColumna("IDE_GTELE").setVisible(false);
			tab_telefonos.getColumna("IDE_NRGAR").setVisible(false);
			tab_telefonos.getColumna("IDE_GEBEN").setVisible(false);
			tab_telefonos.dibujar();
			PanelTabla pat_panel3 = new PanelTabla();
			pat_panel3.setMensajeWarn("TELEFONOS");
			pat_panel3.setPanelTabla(tab_telefonos);

			tab_tabulador.agregarTab("DATOS UNION LIBRE", pat_panel2);
			tab_tabulador.agregarTab("TELEFONOS", pat_panel3);

			Division div_aux=new Division();
			div_aux.dividir2(pat_panel1, tab_tabulador, "40%", "H");
			pan_opcion.getChildren().add(div_aux);

		}else{
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}

	private void filtrarTelefonosConyugue(){
		tab_telefonos.setCondicion("IDE_GTCON="+tab_conyuge.getValor("IDE_GTCON"));
		tab_telefonos.ejecutarSql();
	}
	private void filtrarUnionLibreConyugue(){
		tab_union_libre.setCondicion("IDE_GTCON="+tab_conyuge.getValor("IDE_GTCON"));
		tab_union_libre.ejecutarSql();
	}

	public void seleccionaTablaConyugue(SelectEvent evt){
		tab_conyuge.seleccionarFila(evt);
		filtrarTelefonosConyugue();
		filtrarUnionLibreConyugue();
	}
	public void seleccionaTablaConyugue(AjaxBehaviorEvent evt){
		tab_conyuge.seleccionarFila(evt);
		filtrarTelefonosConyugue();
		filtrarUnionLibreConyugue();
	}


	/**
	 * muestra en pantalla la tabla con los datos del departamento del colaborador
	 */
	public void dibujarDatosEmpleadoDepartamento() {
		if (aut_empleado.getValor() != null) {			
			str_opcion = "2";
			limpiarPanel();

			tab_deta_empleado_depar = new Tabla();
			tab_deta_empleado_depar.setId("tab_deta_empleado_depar");
			tab_deta_empleado_depar.setSql("SELECT a.IDE_GEDED,a.IDE_GTEMP,a.IDE_GEINS,a.IDE_GEAME,b.DETALLE_GEINS,c.DETALLE_GEAED,c.DETALLE_GEMED,FECHA_INGRESO_GEDED,FECHA_SALIDA_GEDED,a.OBSERVACION_GEDED,a.ACTIVO_GEDED " +
					"FROM GEN_DETALLE_EMPLEADO_DEPARTAME a " +
					"LEFT JOIN ( " +
					"SELECT IDE_GEINS,DETALLE_GEINS FROM GEN_INSTITUCION " +
					")b ON a.IDE_GEINS=b.IDE_GEINS " +
					"LEFT JOIN ( " +
					"SELECT a.IDE_GEAME AS IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED FROM GEN_ACCION_MOTIVO_EMPLEADO a " +
					"LEFT JOIN(SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA )b ON b.IDE_GEAED=a.IDE_GEAED " +
					"LEFT JOIN ( SELECT IDE_GEMED,DETALLE_GEMED FROM GEN_MOTIVO_EMPLEADO_DEPA )c ON c.IDE_GEMED=a.IDE_GEMED " +
					"ORDER BY  b.DETALLE_GEAED,c.DETALLE_GEMED " +
					")c ON c.IDE_GEAME=a.IDE_GEAME " +
					"WHERE a.IDE_GTEMP="+aut_empleado.getValor()+" ORDER BY IDE_GEDED DESC");
			tab_deta_empleado_depar.setCampoPrimaria("IDE_GEDED");
			tab_deta_empleado_depar.getColumna("ACTIVO_GEDED").setCheck();
			tab_deta_empleado_depar.getColumna("IDE_GEINS").setVisible(false);
			tab_deta_empleado_depar.getColumna("IDE_GTEMP").setVisible(false);
			tab_deta_empleado_depar.getColumna("IDE_GEAME").setVisible(false);
			tab_deta_empleado_depar.onSelect("seleccionarAccion");
			tab_deta_empleado_depar.setLectura(true);
			tab_deta_empleado_depar.setRows(4);
			tab_deta_empleado_depar.dibujar();
			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setPanelTabla(tab_deta_empleado_depar);



			tab_empleado_departamento = new Tabla();
			tab_empleado_departamento.setId("tab_empleado_departamento");
			tab_empleado_departamento.setTabla("GEN_EMPLEADOS_DEPARTAMENTO_PAR", "IDE_GEEDP", 28);
			tab_empleado_departamento.onSelect("seleccionaTablaEmpleadosDepartamento");
			tab_empleado_departamento.setMostrarNumeroRegistros(false);
			tab_empleado_departamento.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO", "");
			tab_empleado_departamento.getColumna("IDE_GEGRO").setMetodoChange("cargarCargoFuncional");
			tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO=-1)");
			tab_empleado_departamento.getColumna("IDE_GECAF").setMetodoChange("cargarPartidaGrupoCargo");
			tab_empleado_departamento.getColumna("IDE_GECAF").setBuscarenCombo(false);
			tab_empleado_departamento.getColumna("GEN_IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF","");
			tab_empleado_departamento.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
			tab_empleado_departamento.getColumna("IDE_SUCU").setVisible(true);
			tab_empleado_departamento.getColumna("IDE_SUCU").setMetodoChange("cargarCargoAreas");
			tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1)");
			tab_empleado_departamento.getColumna("IDE_GEARE").setMetodoChange("cargarCargoDepartamentos");
			tab_empleado_departamento.getColumna("IDE_GEARE").setBuscarenCombo(false);
			tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1 AND IDE_GEARE=-1)");
			tab_empleado_departamento.getColumna("IDE_GEDEP").setMetodoChange("cargarPartidaGrupoCargo");
			tab_empleado_departamento.getColumna("IDE_GEDEP").setBuscarenCombo(false);
			tab_empleado_departamento.getColumna("IDE_GECAE").setCombo("GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE", "");			
			tab_empleado_departamento.getColumna("IDE_GETIV").setCombo("GEN_TIPO_VINCULACION", "IDE_GETIV", "DETALLE_GETIV", "");
			tab_empleado_departamento.getColumna("IDE_GETIV").setBuscarenCombo(false);
			tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " + 
					"from GEN_PARTIDA_GRUPO_CARGO pgc " +
					"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP");
			tab_empleado_departamento.getColumna("IDE_GTTEM").setCombo("GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM", "");
			tab_empleado_departamento.getColumna("IDE_GTTCO").setCombo("GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO", "");
			tab_empleado_departamento.getColumna("IDE_GTTSI").setCombo("GTH_TIPO_SINDICATO", "IDE_GTTSI", "DETALLE_GTTSI", "");
			tab_empleado_departamento.getColumna("IDE_GTGRE").setCombo("GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
			tab_empleado_departamento.getColumna("IDE_GTGRE").setCombo("GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
			tab_empleado_departamento.getColumna("ACTIVO_GEEDP").setCheck();
			tab_empleado_departamento.getColumna("ACUMULA_FONDOS_GEEDP").setCheck();
			tab_empleado_departamento.getColumna("LINEA_SUPERVICION_GEEDP").setCheck();
			tab_empleado_departamento.getColumna("IDE_GTEMP").setVisible(false);
			tab_empleado_departamento.getColumna("IDE_GEDED").setVisible(false);			
			tab_empleado_departamento.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_empleado_departamento.setCondicion("IDE_GTEMP=" + aut_empleado.getValor()+" AND IDE_GEDED="+tab_deta_empleado_depar.getValorSeleccionado());
			tab_empleado_departamento.setMostrarcampoSucursal(true);
			tab_empleado_departamento.setTipoFormulario(true);
			tab_empleado_departamento.getGrid().setColumns(4);
			tab_empleado_departamento.setMostrarNumeroRegistros(true);
			tab_empleado_departamento.setLectura(true);
			tab_empleado_departamento.getColumna("CONTROL_ASISTENCIA_GEEDP").setCheck();
			tab_empleado_departamento.getColumna("CONTROL_ASISTENCIA_GEEDP").setValorDefecto("false");
			tab_empleado_departamento.getColumna("LIQUIDACION_GEEDP").setCheck();
			tab_empleado_departamento.getColumna("LIQUIDACION_GEEDP").setValorDefecto("false");
			tab_empleado_departamento.getColumna("EJECUTO_LIQUIDACION_GEEDP").setCheck();
			tab_empleado_departamento.getColumna("EJECUTO_LIQUIDACION_GEEDP").setValorDefecto("false");
			tab_empleado_departamento.dibujar();
			actualizarCombosDepartamentoEmpleado();			
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_empleado_departamento);			

			Division div1=new Division();
			div1.dividir2(pat_panel2, pat_panel1, "30%", "H");

			pan_opcion.setTitle("DATOS DEPARTAMENTO EMPLEADO");
			pan_opcion.getChildren().add(div1);

			pan_opcion.getChildren().add(dia_deta_emplea_depar);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}

	public void seleccionarAccion(SelectEvent evt){

		tab_deta_empleado_depar.seleccionarFila(evt);
		tab_empleado_departamento.setCondicion("IDE_GTEMP=" + aut_empleado.getValor()+" AND IDE_GEDED="+tab_deta_empleado_depar.getValorSeleccionado());
		tab_empleado_departamento.ejecutarSql();
		actualizarCombosDepartamentoEmpleado();
	}


	private void actualizarCombosDepartamentoEmpleado(){
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_empleado_departamento.getValor("IDE_GEGRO")+")");
		tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("ide_sucu")+")");
		tab_empleado_departamento.actualizarCombosFormulario();
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" AND IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+")");
		tab_empleado_departamento.actualizarCombosFormulario();
	}
	private void limpiarCombosDepartamentoEmpleado(){
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO=-1)");
		tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1)");
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU=-1 AND IDE_GEARE=-1)");
		tab_empleado_departamento.getColumna("IDE_GETIV").setCombo("GEN_TIPO_VINCULACION", "IDE_GETIV", "DETALLE_GETIV", "IDE_GECAE=-1");
		tab_empleado_departamento.actualizarCombosFormulario();
	}

	public void cargarCargoFuncional(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		tab_empleado_departamento.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=TRUE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_empleado_departamento.getValor("IDE_GEGRO")+")");
		tab_empleado_departamento.getColumna("GEN_IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "PRINCIPAL_SECUNDARIO_GECAF=FALSE AND IDE_GECAF IN (SELECT IDE_GECAF FROM GEN_GRUPO_CARGO where IDE_GEGRO="+tab_empleado_departamento.getValor("IDE_GEGRO")+")");
		utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GECAF,GEN_IDE_GECAF", "");
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " +
				"from GEN_PARTIDA_GRUPO_CARGO pgc " +
				"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP " +
				"where ide_gegro="+tab_empleado_departamento.getValor("IDE_GEGRO")+" " +
				"and IDE_GECAF="+tab_empleado_departamento.getValor("IDE_GECAF")+" " +
				"and IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" " +
				"and ide_gedep="+tab_empleado_departamento.getValor("IDE_GEDEP")+" " +
				"and IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+" ");
		utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GEPGC", "");

	}

	public void cargarCargoAreas(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		tab_empleado_departamento.getColumna("IDE_GEARE").setCombo("GEN_AREA", "IDE_GEARE", "DETALLE_GEARE", "IDE_GEARE IN (SELECT IDE_GEARE from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("ide_sucu")+")");
		utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GEARE", "");
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " +
				"from GEN_PARTIDA_GRUPO_CARGO pgc " +
				"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP " +
				"where ide_gegro="+tab_empleado_departamento.getValor("IDE_GEGRO")+" " +
				"and IDE_GECAF="+tab_empleado_departamento.getValor("IDE_GECAF")+" " +
				"and IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" " +
				"and ide_gedep="+tab_empleado_departamento.getValor("IDE_GEDEP")+" " +
				"and IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+" ");
		utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GEPGC", "");

	}
	public void cargarCargoDepartamentos(AjaxBehaviorEvent evt){
		tab_empleado_departamento.modificar(evt);
		tab_empleado_departamento.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "IDE_GEDEP IN (SELECT IDE_GEDEP from GEN_DEPARTAMENTO_SUCURSAL where IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" AND IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+")");
		utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GEDEP", "");
		tab_empleado_departamento.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP " +
				"from GEN_PARTIDA_GRUPO_CARGO pgc " +
				"left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP " +
				"where ide_gegro="+tab_empleado_departamento.getValor("IDE_GEGRO")+" " +
				"and IDE_GECAF="+tab_empleado_departamento.getValor("IDE_GECAF")+" " +
				"and IDE_SUCU="+tab_empleado_departamento.getValor("IDE_SUCU")+" " +
				"and ide_gedep="+tab_empleado_departamento.getValor("IDE_GEDEP")+" " +
				"and IDE_GEARE="+tab_empleado_departamento.getValor("IDE_GEARE")+" ");
		utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GEPGC", "");

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
		utilitario.addUpdateTabla(tab_empleado_departamento, "IDE_GEPGC", "");
	}


	/**
	 * muestra en pantalla la tabla con los datos de la documentacion de l colaborador
	 */ 
	public void dibujarDatosDocumentacion() {
		if (aut_empleado.getValor() != null) {
			str_opcion = "3";
			limpiarPanel();			
			tab_documentacion = new Tabla();
			tab_documentacion.setId("tab_documentacion");
			tab_documentacion.setTabla("GTH_DOCUMENTACION_EMPLEADO", "IDE_GTDCE", 22);
			//tab_documentacion.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_aseguradora"));
			//tab_documentacion.getColumna("IDE_GEINS").setAutoCompletar();
			//tab_documentacion.getColumna("IDE_GTTCB").setCombo("GTH_TIPO_CUENTA_BANCARIA", "IDE_GTTCB", "DETALLE_GTTCB", "");
			tab_documentacion.getColumna("IDE_GTTLC").setCombo("GTH_TIPO_LICENCIA_CONDUCIR", "IDE_GTTLC", "DETALLE_GTTLC", "");
			tab_documentacion.getColumna("IDE_GTTLC").setVisible(false);
			tab_documentacion.getColumna("IDE_GECAE").setCombo("GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE", "");
			tab_documentacion.getColumna("ACTIVO_GTDCE").setCheck();
			tab_documentacion.getColumna("IDE_GTEMP").setVisible(false);
			tab_documentacion.getColumna("IDE_GECAE").setVisible(false);
			tab_documentacion.getColumna("NRO_CASILLA_GTDCE").setVisible(false);
			tab_documentacion.getColumna("PASAPORTE_GTDCE").setVisible(false);
			tab_documentacion.getColumna("fecha_afiliacion_gtdce").setVisible(false);
			tab_documentacion.getColumna("fecha_caducidad_gtdce").setVisible(false);
			tab_documentacion.getColumna("nro_iess_gtdce").setVisible(false);
			tab_documentacion.getColumna("NRO_CUENTA_GTDCE").setVisible(false);	
			tab_documentacion.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_documentacion.setCondicion("IDE_GTEMP=" + aut_empleado.getValor());
			tab_documentacion.setTipoFormulario(true);
			tab_documentacion.getGrid().setColumns(4);
			tab_documentacion.setMostrarNumeroRegistros(true);
			tab_documentacion.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_documentacion);
			pan_opcion.setTitle("DATOS DOCUMENTACION");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}


	/**
	 * muestra en pantalla la tabla con los datos EN CASO DE EMERGENCIA
	 */
	public void dibujarDatosEmergencia() {
		if (aut_empleado.getValor() != null) {
			str_opcion = "4";
			limpiarPanel();			
			tab_persona_emergencia = new Tabla();
			tab_persona_emergencia.setId("tab_persona_emergencia");
			tab_persona_emergencia.setTabla("GTH_PERSONA_EMERGENCIA", "IDE_GTPEE", 4);
			tab_persona_emergencia.onSelect("seleccionaTablaPersonaEmergencia");
			tab_persona_emergencia.getColumna("IDE_GTTPR").setCombo("GTH_TIPO_PARENTESCO_RELACION", "IDE_GTTPR", "DETALLE_GTTPR", "");
			tab_persona_emergencia.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_persona_emergencia.getColumna("IDE_GTEMP").setVisible(false);
			tab_persona_emergencia.getColumna("ACTIVO_GTPEE").setCheck();
			tab_persona_emergencia.setCondicion("IDE_GTEMP=" + aut_empleado.getValor());
			tab_persona_emergencia.setTipoFormulario(false);
			tab_persona_emergencia.getGrid().setColumns(4);
			tab_persona_emergencia.setMostrarNumeroRegistros(true);
			tab_persona_emergencia.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_persona_emergencia);
			pan_opcion.setTitle("DATOS EMERGENCIA");

			Tabulador tab_tabulador=new Tabulador();
			tab_tabulador.setId("tab_tabulador");

			tab_direccion = new Tabla();
			tab_direccion.setId("tab_direccion");
			tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
			tab_direccion.setTabla("GTH_DIRECCION", "IDE_GTDIR", 29);
			tab_direccion.getColumna("IDE_GTPEE").setVisible(false);
			tab_direccion.getColumna("ACTIVO_GTDIR").setCheck();
			tab_direccion.getColumna("NOTIFICACION_GTDIR").setCheck();
			tab_direccion.getColumna("IDE_GEDIP").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
			tab_direccion.getColumna("IDE_GEDIP").setAutoCompletar();
			tab_direccion.getColumna("IDE_GTEMP").setVisible(false);
			tab_direccion.getColumna("GTH_IDE_GTEMP").setVisible(false);
			tab_direccion.getColumna("IDE_GTNEE").setVisible(false);
			tab_direccion.getColumna("IDE_GTELE").setVisible(false);
			tab_direccion.getColumna("IDE_NRGAR").setVisible(false);
			tab_direccion.getColumna("IDE_GEBEN").setVisible(false);
			tab_direccion.getColumna("IDE_GTPEE").setValorDefecto(tab_persona_emergencia.getValor("IDE_GTPEE"));
			tab_direccion.setCondicion("IDE_GTPEE =" + tab_persona_emergencia.getValor("IDE_GTPEE"));
			tab_direccion.dibujar();
			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setMensajeWarn("DIRECCION");
			pat_panel2.setPanelTabla(tab_direccion);

			tab_telefonos = new Tabla();
			tab_telefonos.setId("tab_telefonos");
			tab_telefonos.setIdCompleto("tab_tabulador:tab_telefonos");
			tab_telefonos.setTabla("GTH_TELEFONO", "IDE_GTTEL", 7);
			tab_telefonos.getColumna("IDE_GTTIT").setCombo("GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT", "");
			tab_telefonos.getColumna("IDE_GTCON").setVisible(false);
			tab_telefonos.getColumna("IDE_GTCON").setValorDefecto(null);
			tab_telefonos.getColumna("IDE_GTPEE").setVisible(false);
			tab_telefonos.getColumna("IDE_GTNEE").setVisible(false);
			tab_telefonos.getColumna("ACTIVO_GTTEL").setCheck();
			tab_telefonos.getColumna("IDE_GTEMP").setVisible(false);
			tab_telefonos.getColumna("GTH_IDE_GTEMP").setVisible(false);
			tab_telefonos.getColumna("NOTIFICACION_GTTEL").setCheck();
			tab_telefonos.getColumna("IDE_GTELE").setVisible(false);
			tab_telefonos.getColumna("IDE_NRGAR").setVisible(false);
			tab_telefonos.getColumna("IDE_GEBEN").setVisible(false);
			tab_telefonos.getColumna("IDE_GTPEE").setValorDefecto(tab_persona_emergencia.getValor("IDE_GTPEE"));
			tab_telefonos.setCondicion("IDE_GTPEE =" + tab_persona_emergencia.getValor("IDE_GTPEE"));
			tab_telefonos.dibujar();
			PanelTabla pat_panel3 = new PanelTabla();
			pat_panel3.setMensajeWarn("TELEFONOS");
			pat_panel3.setPanelTabla(tab_telefonos);

			tab_tabulador.agregarTab("DIRECCION", pat_panel2);
			tab_tabulador.agregarTab("TELEFONOS", pat_panel3);
			Division div_aux=new Division();
			div_aux.dividir2(pat_panel1,tab_tabulador, "40%", "H");
			pan_opcion.getChildren().add(div_aux);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}

	public void dibujarCuentaAnticipo() {		
		if (aut_empleado.getValor() != null) {
			str_opcion = "28";
			limpiarPanel();	
			tab_anio= new Tabla();
			tab_anio.setId("tab_anio");
			tab_anio.setHeader("AñO ");
			tab_anio.setTabla("GEN_ANIO","IDE_GEANI",44);
			tab_anio.onSelect("seleccionarCuentaAnticipo");
			//tab_anio.agregarRelacion(tab_cuenta_anticipo);
			tab_anio.setLectura(true);
			tab_anio.dibujar();

			PanelTabla pat_panel2 = new PanelTabla();	
			pat_panel2.setPanelTabla(tab_anio);


			tab_cuenta_anticipo= new Tabla();
			tab_cuenta_anticipo.setId("tab_cuenta_anticipo");
			tab_cuenta_anticipo.setHeader("CUENTA ANTICIPO");
			tab_cuenta_anticipo.setTabla("GTH_CUENTA_ANTICIPO","IDE_GTCUA",43);
			tab_cuenta_anticipo.getColumna("ide_cocac").setCombo(ser_contabilidad.getCuentaContable("true,false"));
			tab_cuenta_anticipo.getColumna("ide_cocac").setAutoCompletar();
			tab_cuenta_anticipo.getColumna("ide_cocac").setLectura(true);
			tab_cuenta_anticipo.getColumna("ide_gtemp").setVisible(false);
			tab_cuenta_anticipo.getColumna("ide_geani").setVisible(false);
			tab_cuenta_anticipo.setCondicion("IDE_GTEMP=" + aut_empleado.getValor()+" AND IDE_GEANI="+tab_anio.getValor("ide_geani"));
			tab_cuenta_anticipo.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_cuenta_anticipo);

			Division div_anio=new Division();
			div_anio.dividir2(pat_panel2,pat_panel1, "50%","H");		
			pan_opcion.setTitle("CUENTA ANTICIPO");
			pan_opcion.getChildren().add(div_anio);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}



	private void filtrarTelefonoPersonaEmergencia(){
		tab_telefonos.setCondicion("IDE_GTPEE="+tab_persona_emergencia.getValor("IDE_GTPEE"));
		tab_telefonos.ejecutarSql();
	}
	private void filtrarDireccionPersonaEmergencia(){
		tab_direccion.setCondicion("IDE_GTPEE="+tab_persona_emergencia.getValor("IDE_GTPEE"));
		tab_direccion.ejecutarSql();
	}

	public void seleccionaTablaPersonaEmergencia(SelectEvent evt){
		tab_persona_emergencia.seleccionarFila(evt);
		filtrarTelefonoPersonaEmergencia();
		filtrarDireccionPersonaEmergencia();
	}

	public void seleccionaTablaPersonaEmergencia(AjaxBehaviorEvent evt){
		tab_persona_emergencia.seleccionarFila(evt);
		filtrarTelefonoPersonaEmergencia();
		filtrarDireccionPersonaEmergencia();
	}

	private void limpiarPanel(){		
		pan_opcion.getChildren().clear();
		pan_opcion.getChildren().add(efecto);
	}

	/**
	 * muestra en pantalla la tabla con los datos de las cargas familiares del colaborador 
	 */
	public void dibujarDatosCargasFamiliares() {
		if (aut_empleado.getValor() != null) {
			str_opcion = "5";
			limpiarPanel();			
			tab_cargas_familiares = new Tabla();
			tab_cargas_familiares.setId("tab_cargas_familiares");
			tab_cargas_familiares.setTabla("GTH_CARGAS_FAMILIARES", "IDE_GTCAF", 6);
			tab_cargas_familiares.getColumna("IDE_GTTPR").setCombo("GTH_TIPO_PARENTESCO_RELACION", "IDE_GTTPR", "DETALLE_GTTPR", "");
			tab_cargas_familiares.getColumna("IDE_GTGEN").setCombo("GTH_GENERO", "IDE_GTGEN", "DETALLE_GTGEN", "");
			tab_cargas_familiares.getColumna("IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI", "");
			tab_cargas_familiares.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_cargas_familiares.getColumna("IDE_GTEMP").setVisible(false);
			tab_cargas_familiares.getColumna("ACTIVO_GTCAF").setCheck();
			tab_cargas_familiares.getColumna("PRIMER_NOMBRE_GTCAF").setRequerida(true);
			tab_cargas_familiares.getColumna("SEGUNDO_NOMBRE_GTCAF").setRequerida(false);
			tab_cargas_familiares.getColumna("APELLIDO_MATERNO_GTCAF").setRequerida(false);
			tab_cargas_familiares.getColumna("APELLIDO_PATERNO_GTCAF").setRequerida(true);
			tab_cargas_familiares.getColumna("DOCUMENTO_IDENTIDAD_GTCAF").setRequerida(false);
			tab_cargas_familiares.getColumna("IDE_GTTDI").setRequerida(false);
			tab_cargas_familiares.getColumna("DOCUMENTO_IDENTIDAD_GTCAF").setUnico(true);
			tab_cargas_familiares.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_cargas_familiares.setTipoFormulario(false);
			tab_cargas_familiares.getGrid().setColumns(4);
			tab_cargas_familiares.setMostrarNumeroRegistros(false);
			tab_cargas_familiares.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_cargas_familiares);
			pan_opcion.setTitle("DATOS CARGAS FAMILIARES");
			pan_opcion.getChildren().add(pat_panel1);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}

	/**
	 * muestra en pantalla la tabla con los datos de familiares del colaborador 
	 */
	public void dibujarDatosFamiliares() {
		if (aut_empleado.getValor() != null) {
			str_opcion = "6";
			limpiarPanel();			
			tab_familiar = new Tabla();
			tab_familiar.setId("tab_familiar");
			tab_familiar.setTabla("GTH_FAMILIAR", "IDE_GTFAM", 5);
			tab_familiar.getColumna("IDE_GTACL").setCombo("GTH_ACTIVIDAD_LABORAL", "IDE_GTACL", "DETALLE_GTACL", "");
			tab_familiar.getColumna("IDE_GTTPR").setCombo("GTH_TIPO_PARENTESCO_RELACION", "IDE_GTTPR", "DETALLE_GTTPR", "");
			tab_familiar.getColumna("IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI", "");
			tab_familiar.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_familiar.getColumna("IDE_GTEMP").setVisible(false);
			tab_familiar.getColumna("ACTIVO_GTFAM").setCheck();
			tab_familiar.getColumna("PRIMER_NOMBRE_GTFAM").setRequerida(true);
			tab_familiar.getColumna("SEGUNDO_NOMBRE_GTFAM");
			tab_familiar.getColumna("APELLIDO_MATERNO_GTFAM");
			tab_familiar.getColumna("APELLIDO_PATERNO_GTFAM").setRequerida(true);
			tab_familiar.getColumna("DOCUMENTO_IDENTIDAD_GTFAM").setRequerida(true);
			tab_familiar.getColumna("IDE_GTTDI").setRequerida(false);
			tab_familiar.getColumna("DOCUMENTO_IDENTIDAD_GTFAM").setUnico(true);
			tab_familiar.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_familiar.setTipoFormulario(false);
			tab_familiar.getGrid().setColumns(4);
			tab_familiar.setMostrarNumeroRegistros(false);
			tab_familiar.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_familiar);
			pan_opcion.setTitle("DATOS FAMILIARES");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}
	//	/**
	//	 * muestra en pantalla la tabla con los datos DE DIRECCION del colaborador 
	//	 */
	//	public void dibujarDatosDireccion() {
	//		if (aut_empleado.getValor() != null) {
	//			str_opcion = "8";
	//			pan_opcion.getChildren().clear();
	//			pan_opcion.getChildren().add(efecto);
	//			tab_direccion = new Tabla();
	//			tab_direccion.setId("tab_direccion");
	//			tab_direccion.setTabla("GTH_DIRECCION", "IDE_GTDIR", 29);
	//			tab_direccion.getColumna("IDE_GTPEE").setVisible(false);
	//			tab_direccion.getColumna("ACTIVO_GTDIR").setCheck();
	//			tab_direccion.getColumna("IDE_GTEMP").setVisible(false);
	//			tab_direccion.getColumna("IDE_GTNEE").setVisible(false);
	//			tab_direccion.getColumna("GTH_IDE_GTEMP").setVisible(false);
	//			tab_direccion.getColumna("IDE_GEDIP").setCombo("select DP.IDE_GEDIP, " +
	//					"DP4.DETALLE_GEDIP AS PAIS_VIVE, " +
	//					"DP3.DETALLE_GEDIP AS PROVINCIA_VIVE, " +
	//					"DP2.DETALLE_GEDIP AS CANTON_VIVE, " +
	//					"DP.DETALLE_GEDIP AS PARROQUIA_VIVE " +
	//					"from GEN_DIVISION_POLITICA DP " +
	//					"INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP " +
	//					"INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP " +
	//					"inner join GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP " +
	//					"INNER join GEN_DIVISION_POLITICA DP3 ON DP3.IDE_GEDIP=DP2.GEN_IDE_GEDIP " +
	//					"INNER join GEN_DIVISION_POLITICA DP4 ON DP4.IDE_GEDIP=DP3.GEN_IDE_GEDIP " +
	//					"AND TDP.NIVEL_GETDP IN (5) " +
	//					"order by DP3.DETALLE_GEDIP ASC,DP.DETALLE_GEDIP ASC,DP2.DETALLE_GEDIP ASC");
	//			tab_direccion.getColumna("IDE_GEDIP").setAutoCompletar();
	//			tab_direccion.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
	//			tab_direccion.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
	//			tab_direccion.dibujar();
	//			PanelTabla pat_panel1 = new PanelTabla();
	//			pat_panel1.setPanelTabla(tab_direccion);
	//			pan_opcion.setTitle("DATOS TELEFONICOS");
	//			pan_opcion.getChildren().add(pat_panel1);
	//		} else {
	//			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
	//		}
	//	}
	//
	//
	//	/**
	//	 * muestra en pantalla la tabla con los datos telefonicos del colaborador 
	//	 */
	//	public void dibujarDatosTelefonicos() {
	//		if (aut_empleado.getValor() != null) {
	//			str_opcion = "9";
	//			pan_opcion.getChildren().clear();
	//			pan_opcion.getChildren().add(efecto);
	//			tab_telefonos = new Tabla();
	//			tab_telefonos.setId("tab_telefonos");
	//			tab_telefonos.setTabla("GTH_TELEFONO", "IDE_GTTEL", 8);
	//			tab_telefonos.getColumna("IDE_GTTIT").setCombo("GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT", "");
	//			//			tab_telefonos.getColumna("IDE_GTCON").setCombo("GTH_CONYUGE", "IDE_GTCON", "APELLIDO_PATERNO_GTCON,APELLIDO_MATERNO_GTCON,PRIMER_NOMBRE_GTCON,SEGUNDO_NOMBRE_GTCON", "IDE_GTEMP="+aut_empleado.getValor());
	//			//			tab_telefonos.getColumna("IDE_GTPEE").setCombo("GTH_PERSONA_EMERGENCIA", "IDE_GTPEE", "APELLIDO_PATERNO_GTPEE,APELLIDO_MATERNO_GTPEE,PRIMER_NOMBRE_GTPEE,SEGUNDO_NOMBRE_GTPEE", "IDE_GTEMP="+aut_empleado.getValor());
	//			tab_telefonos.getColumna("ACTIVO_GTTEL").setCheck();
	//			tab_telefonos.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
	//			tab_telefonos.getColumna("IDE_GTEMP").setVisible(false);
	//			tab_telefonos.getColumna("IDE_GTCON").setVisible(false);
	//			tab_telefonos.getColumna("IDE_GTPEE").setVisible(false);
	//			tab_telefonos.getColumna("GTH_IDE_GTEMP").setVisible(false);
	//			tab_telefonos.getColumna("GTCON_IDE_GTEMP").setVisible(false);
	//			tab_telefonos.getColumna("NOTIFICACION_GTTEL").setCheck();
	//			tab_telefonos.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
	//			tab_telefonos.setTipoFormulario(false);
	//			tab_telefonos.setMostrarNumeroRegistros(false);
	//			tab_telefonos.dibujar();
	//			PanelTabla pat_panel1 = new PanelTabla();
	//			pat_panel1.setPanelTabla(tab_telefonos);
	//			pan_opcion.setTitle("DATOS TELEFONICOS");
	//			pan_opcion.getChildren().add(pat_panel1);
	//		} else {
	//			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
	//		}
	//	}
	//
	//	/**
	//	 * muestra en pantalla la tabla con los datos telefonicos del colaborador 
	//	 */
	//	public void dibujarDatosCorreos() {
	//		if (aut_empleado.getValor() != null) {
	//			str_opcion = "10";
	//			pan_opcion.getChildren().clear();
	//			pan_opcion.getChildren().add(efecto);
	//			tab_correos = new Tabla();
	//			tab_correos.setId("tab_correos");
	//			tab_correos.setTabla("GTH_CORREO", "IDE_GTCOR", 9);
	//			//tab_correos.getColumna("IDE_GTTPR").setCombo("GTH_TIPO_PARENTESCO_RELACION", "IDE_GTTPR", "DETALLE_GTTPR", "");
	//			tab_correos.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
	//			tab_correos.getColumna("IDE_GTEMP").setVisible(false);
	//			tab_correos.getColumna("ACTIVO_GTCOR").setCheck();
	//			tab_correos.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
	//			tab_correos.setTipoFormulario(false);
	//			tab_correos.setMostrarNumeroRegistros(false);
	//			tab_correos.dibujar();
	//			PanelTabla pat_panel1 = new PanelTabla();
	//			pat_panel1.setPanelTabla(tab_correos);
	//			pan_opcion.setTitle("DATOS DE CORREO");
	//			pan_opcion.getChildren().add(pat_panel1);
	//		} else {
	//			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
	//		}
	//	}
	//

	/**
	 * muestra en pantalla la tabla con los datos de seguro de vida y beneficiarios del seguro del colaborador 
	 */
	public void dibujarDatosSeguroVida(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "7";
			limpiarPanel();			
			tab_seguro_vida = new Tabla();
			tab_seguro_vida.setId("tab_seguro_vida");
			tab_seguro_vida.setTabla("GTH_SEGURO_VIDA", "IDE_GTSEV", 10);
			tab_seguro_vida.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_aseguradora"));
			//tab_seguro_vida.getColumna("IDE_GEINS").setAutoCompletar();
			tab_seguro_vida.onSelect("seleccionaTablaSeguroVida");
			tab_seguro_vida.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_seguro_vida.getColumna("IDE_GTEMP").setVisible(false);
			tab_seguro_vida.getColumna("ACTIVO_GTSEV").setCheck();
			tab_seguro_vida.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_seguro_vida.setTipoFormulario(true);
			tab_seguro_vida.getGrid().setColumns(4);
			tab_seguro_vida.setMostrarNumeroRegistros(true);
			tab_seguro_vida.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_seguro_vida);
			pat_panel1.getMenuTabla().getItem_actualizar().setMetodo("actualizar");
			pan_opcion.setTitle("DATOS DE SEGURO DE VIDA");

			tab_beneficiario_seguro=new Tabla();
			tab_beneficiario_seguro.setId("tab_beneficiario_seguro");
			tab_beneficiario_seguro.setTabla("GTH_BENEFICIARIO_SEGURO", "IDE_GTBES", 11);
			tab_beneficiario_seguro.getColumna("IDE_GTSEV").setVisible(false);
			tab_beneficiario_seguro.getColumna("IDE_GTSEV").setValorDefecto(tab_seguro_vida.getValor("IDE_GTSEV"));
			//tab_beneficiario_seguro.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "GEN_IDE_GEINS IS NOT NULL");
			//tab_beneficiario_seguro.getColumna("IDE_GEINS").setAutoCompletar();
			tab_beneficiario_seguro.getColumna("IDE_GTTPR").setCombo("GTH_TIPO_PARENTESCO_RELACION", "IDE_GTTPR", "DETALLE_GTTPR", "");
			tab_beneficiario_seguro.getColumna("ACTIVO_GTBES").setCheck();
			tab_beneficiario_seguro.setCondicion("IDE_GTSEV="+tab_seguro_vida.getValor("IDE_GTSEV"));
			tab_beneficiario_seguro.setTipoFormulario(false);
			tab_beneficiario_seguro.setMostrarNumeroRegistros(false);		
			tab_beneficiario_seguro.getColumna("porcentaje_seguro_gtbes").setMetodoChange("valorPorcentajeSeguro");
			tab_beneficiario_seguro.setColumnaSuma("porcentaje_seguro_gtbes");
			tab_beneficiario_seguro.dibujar();
			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setPanelTabla(tab_beneficiario_seguro);
			pat_panel2.setMensajeWarn("BENEFICIARIOS DE SEGURO DE VIDA");
			Division div_aux=new Division();
			div_aux.dividir2(pat_panel1, pat_panel2, "35%", "H");

			pan_opcion.getChildren().add(div_aux);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	private void filtrarBeneficiariosSeguroVida(){
		tab_beneficiario_seguro.setCondicion("IDE_GTSEV="+tab_seguro_vida.getValor("IDE_GTSEV"));
		tab_beneficiario_seguro.sumarColumnas();
		tab_beneficiario_seguro.ejecutarSql();
	}
	public void seleccionaTablaSeguroVida(SelectEvent evt){
		tab_seguro_vida.seleccionarFila(evt);
		filtrarBeneficiariosSeguroVida();
	}

	public void seleccionaTablaSeguroVida(AjaxBehaviorEvent evt){
		tab_seguro_vida.seleccionarFila(evt);
		filtrarBeneficiariosSeguroVida();
	}


	/**
	 * muestra en pantalla la tabla con los datos de formacion militar del colaborador si es que lo tiene 
	 */
	public void dibujarDatosRegistroMilitar(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "8";
			limpiarPanel();			
			tab_registro_militar = new Tabla();
			tab_registro_militar.setId("tab_registro_militar");
			tab_registro_militar.setTabla("GTH_REGISTRO_MILITAR", "IDE_GTREM", 15);
			tab_registro_militar.getColumna("IDE_GTTSM").setCombo("GTH_TIPO_SITUACION_MILITAR", "IDE_GTTSM", "DETALLE_GTTSM", "");
			tab_registro_militar.getColumna("IDE_GTTRF").setCombo("GTH_TIPO_RAMAS_FFAA", "IDE_GTTRF", "DETALLE_GTTRF", "");
			tab_registro_militar.getColumna("IDE_GTTGF").setCombo("GTH_TIPO_GRADO_FFAA", "IDE_GTTGF", "DETALLE_GTTGF", "");
			tab_registro_militar.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_registro_militar.getColumna("IDE_GTEMP").setVisible(false);			
			tab_registro_militar.getColumna("ANIO_INGRESO_GTREM").setMascara("9999");
			tab_registro_militar.getColumna("ACTIVO_GTREM").setCheck();
			tab_registro_militar.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_registro_militar.setTipoFormulario(true);
			tab_registro_militar.getGrid().setColumns(4);
			tab_registro_militar.setMostrarNumeroRegistros(true);
			tab_registro_militar.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_registro_militar);
			pan_opcion.setTitle("DATOS DE FORMACION MILITAR");
			pan_opcion.getChildren().add(pat_panel1);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	/**
	 * muestra en pantalla la tabla con los datos de HOBBIES del colaborador si es que lo tiene 
	 */
	public void dibujarDatosHobbies(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "9";
			limpiarPanel();			
			tab_hobbies = new Tabla();
			tab_hobbies.setId("tab_hobbies");
			tab_hobbies.setTabla("GTH_HOBIE", "IDE_GTHOB", 34);
			tab_hobbies.getColumna("IDE_GTTIH").setCombo("GTH_TIPO_HOBIE", "IDE_GTTIH", "DETALLE_GTTIH", "");
			tab_hobbies.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_hobbies.getColumna("IDE_GTEMP").setVisible(false);
			tab_hobbies.getColumna("ACTIVO_GTHOB").setCheck();
			tab_hobbies.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_hobbies.setTipoFormulario(false);
			tab_hobbies.setMostrarNumeroRegistros(false);
			tab_hobbies.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_hobbies);
			pan_opcion.setTitle("DATOS DE HOBBIES");
			pan_opcion.getChildren().add(pat_panel1);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}


	/**
	 * muestra en pantalla la tabla con los datos de formacion militar del colaborador si es que lo tiene 
	 */
	public void dibujarDatosEducacion(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "10";
			limpiarPanel();			
			tab_educacion = new Tabla();
			tab_educacion.setId("tab_educacion");
			tab_educacion.setTabla("GTH_EDUCACION_EMPLEADO", "IDE_GTEDE", 12);
			tab_educacion.getColumna("IDE_GTEDE").setNombreVisual("CODIGO");
			tab_educacion.getColumna("IDE_GEINS").setCombo("select IDE_GEINS,DETALLE_GEINS from  GEN_INSTITUCION where ide_getii in ("+utilitario.getVariable("p_gen_tipo_institucion_educativa")+")");
			tab_educacion.getColumna("IDE_GEINS").setVisible(false);
			//tab_educacion.getColumna("IDE_GEINS").setAutoCompletar();
			tab_educacion.getColumna("IDE_GTTES").setCombo("GTH_TIPO_ESPECIALIDAD", "IDE_GTTES", "DETALLE_GTTES", "");
			tab_educacion.getColumna("IDE_GTTES").setNombreVisual("ESPECIALIDAD");
			tab_educacion.getColumna("IDE_GTTED").setCombo("GTH_TIPO_EDUCACION", "IDE_GTTED", "DETALLE_GTTED", "");
			tab_educacion.getColumna("IDE_GTTED").setNombreVisual("EDUCACION");
			tab_educacion.getColumna("IDE_GTTTP").setCombo("GTH_TIPO_TITULO_PROFESIONAL", "IDE_GTTTP", "DETALLE_GTTTP", "");
			tab_educacion.getColumna("IDE_GTTTP").setNombreVisual("TITULO");
			tab_educacion.getColumna("IDE_GTANA").setCombo("GTH_ANIO_APROBADO", "IDE_GTANA", "DETALLE_GTANA", "");
			tab_educacion.getColumna("IDE_GTANA").setNombreVisual("AñO APROBADO");
			tab_educacion.getColumna("IDE_GEDIP").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
			tab_educacion.getColumna("IDE_GEDIP").setNombreVisual("CIUDAD ");
			tab_educacion.getColumna("REGISTRO_TITULO_GTEDE").setNombreVisual("REGISTRO SENECYT");
			tab_educacion.getColumna("IDE_GEDIP").setVisible(false);
			tab_educacion.getColumna("ANIO_GRADO_GTEDE").setMascara("9999");
			tab_educacion.getColumna("ANIO_GRADO_GTEDE").setNombreVisual("AñO GRADO");
			tab_educacion.getColumna("IDE_GEDIP").setAutoCompletar();
			tab_educacion.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_educacion.getColumna("IDE_GTEMP").setVisible(false);
			tab_educacion.getColumna("ANIO_GTEDE").setVisible(false);
			tab_educacion.getColumna("ACTIVO_GTEDE").setCheck();
			tab_educacion.getColumna("ACTIVO_GTEDE").setNombreVisual("ACTIVO");
			tab_educacion.getColumna("OBSERVACIONES_GTEDE").setNombreVisual("OBSERVACIONES");
			tab_educacion.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_educacion.setTipoFormulario(true);
			tab_educacion.getGrid().setColumns(4);
			tab_educacion.setMostrarNumeroRegistros(true);
			tab_educacion.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_educacion);
			pan_opcion.setTitle("DATOS DE EDUCACION");
			pan_opcion.getChildren().add(pat_panel1);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}


	/**
	 * muestra en pantalla la tabla con los datos de formacion militar del colaborador si es que lo tiene 
	 */
	public void dibujarDatosIdiomas(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "11";
			limpiarPanel();			
			tab_idiomas = new Tabla();
			tab_idiomas.setId("tab_idiomas");
			tab_idiomas.setTabla("GTH_IDIOMA_EMPLEADO", "IDE_GTIDE", 13);
			tab_idiomas.getColumna("IDE_GEIDI").setCombo("GEN_IDIOMA", "IDE_GEIDI", "DETALLE_GEIDI", "");
			tab_idiomas.getColumna("IDE_GEINS").setCombo("select IDE_GEINS,DETALLE_GEINS from  GEN_INSTITUCION where ide_getii in ("+utilitario.getVariable("p_gen_tipo_institucion_educativa_idiomas")+")");
			//tab_idiomas.getColumna("IDE_GEINS").setAutoCompletar();
			tab_idiomas.getColumna("IDE_GETPR").setCombo("GEN_TIPO_PERIODO", "IDE_GETPR", "DETALLE_GETPR", "");
			tab_idiomas.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_idiomas.getColumna("IDE_GTEMP").setVisible(false);
			tab_idiomas.getColumna("ACTIVO_GTIDE").setCheck();
			tab_idiomas.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_idiomas.setTipoFormulario(false);
			tab_idiomas.getGrid().setColumns(4);
			tab_idiomas.setMostrarNumeroRegistros(true);
			tab_idiomas.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_idiomas);
			pan_opcion.setTitle("DATOS DE IDIOMAS");
			pan_opcion.getChildren().add(pat_panel1);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	/**
	 * muestra en pantalla la tabla con los datos de capacitacion del colaborador    
	 */
	public void dibujarDatosCapacitacion(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "12";
			limpiarPanel();			
			tab_capacitacion = new Tabla();
			tab_capacitacion.setId("tab_capacitacion");
			tab_capacitacion.setTabla("GTH_CAPACITACION_EMPLEADO", "IDE_GTCEM", 17);
			
			tab_capacitacion.getColumna("IDE_GEINS").setCombo("SELECT ide_geins,detalle_geins FROM GEN_INSTITUCION " +
					"where gen_ide_geins IN("+utilitario.getVariable("p_gen_instituciones")+")");
			//tab_capacitacion.getColumna("IDE_GEINS").setAutoCompletar();
			tab_capacitacion.getColumna("IDE_GEDIP").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
			tab_capacitacion.getColumna("IDE_GEDIP").setVisible(false);
			tab_capacitacion.getColumna("IDE_GEDIP").setAutoCompletar();
			tab_capacitacion.getColumna("IDE_GETPR").setCombo("GEN_TIPO_PERIODO", "IDE_GETPR", "DETALLE_GETPR", "");
			tab_capacitacion.getColumna("IDE_GETPR").setVisible(false);
			tab_capacitacion.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_capacitacion.getColumna("IDE_GTEMP").setVisible(false);
			tab_capacitacion.getColumna("instructor_gtcem").setVisible(false);
			List lista = new ArrayList();
			Object fila1[] = {
					"0", "EXTERNA"
			};
			Object fila2[] = {
					"1", "INTERNA"
			};
			lista.add(fila1);
			lista.add(fila2);
			tab_capacitacion.getColumna("TIPO_GTCEM").setRadio(lista, "1");
			tab_capacitacion.getColumna("TIPO_GTCEM").setRadioVertical(true);
			tab_capacitacion.getColumna("TIPO_GTCEM").setVisible(false);
			tab_capacitacion.getColumna("ACTIVO_GTCEM").setCheck();
			tab_capacitacion.getColumna("ACTIVO_GTCEM").setVisible(false);
			tab_capacitacion.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_capacitacion.getColumna("IDE_GTCEM").setNombreVisual("C�DIGO");
			tab_capacitacion.getColumna("DETALLE_GTCEM").setNombreVisual("DETALLE");
			tab_capacitacion.getColumna("DURACION_GTCEM").setNombreVisual("DURACióN");
			tab_capacitacion.getColumna("IDE_GEINS").setNombreVisual("INSTITUCióN");
			tab_capacitacion.getColumna("FECHA_GTCEM").setNombreVisual("FECHA");
			tab_capacitacion.setTipoFormulario(true);
			tab_capacitacion.getGrid().setColumns(4);
			tab_capacitacion.setMostrarNumeroRegistros(true);
			tab_capacitacion.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_capacitacion);
			pan_opcion.setTitle("DATOS DE CAPACITACION");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}



	/**
	 * muestra en pantalla la tabla con los datos de experiencia laboral del colaborador  
	 */
	public void dibujarDatosExperienciaLaboral(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "14";
			limpiarPanel();			
			tab_experiencia_laboral = new Tabla();
			tab_experiencia_laboral.setId("tab_experiencia_laboral");
			tab_experiencia_laboral.setTabla("GTH_EXPERIENCIA_LABORAL_EMPLEA", "IDE_GTELE", 16);
			tab_experiencia_laboral.getColumna("IDE_GTELE").setNombreVisual("C�DIGO");
			tab_experiencia_laboral.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "");
			//tab_experiencia_laboral.getColumna("IDE_GEINS").setAutoCompletar();
			tab_experiencia_laboral.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_experiencia_laboral.getColumna("IDE_GTEMP").setVisible(false);
			tab_experiencia_laboral.getColumna("ACTIVO_GTELE").setCheck();
			tab_experiencia_laboral.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_experiencia_laboral.getColumna("DETALLE_CARGO_GTELE").setNombreVisual("DETALLE CARGO");
			tab_experiencia_laboral.getColumna("NRO_SUBORDINADOS_GTELE").setVisible(false);
			tab_experiencia_laboral.getColumna("JEFE_INMEDIATO_GTELE").setVisible(false);
			tab_experiencia_laboral.getColumna("FUNCIONES_DESEMPENIO_GTELE").setVisible(false);
			tab_experiencia_laboral.getColumna("ACTIVO_GTELE").setVisible(false);
			tab_experiencia_laboral.getColumna("FECHA_INGRESO_GTELE").setNombreVisual("FECHA INGRESO");
			tab_experiencia_laboral.getColumna("IDE_GEINS").setNombreVisual("INSTITUCióN");
			tab_experiencia_laboral.getColumna("AREA_DESEMPENIO_GTELE").setNombreVisual("�REA DESEMPE�O");
			tab_experiencia_laboral.getColumna("MOTIVO_SALIDA_GTELE").setVisible(false);
			tab_experiencia_laboral.getColumna("CARGO_JEFE_GTELE").setNombreVisual("CARGO JEFE");
			tab_experiencia_laboral.getColumna("TELEFONO_GTELE").setNombreVisual("TEL�FONO");
			tab_experiencia_laboral.getColumna("FECHA_SALIDA_GTELE").setNombreVisual("FECHA SALIDA");
			tab_experiencia_laboral.setTipoFormulario(true);
			tab_experiencia_laboral.getGrid().setColumns(4);
			tab_experiencia_laboral.setMostrarNumeroRegistros(true);
			tab_experiencia_laboral.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_experiencia_laboral);
			pan_opcion.setTitle("DATOS DE EXPERIENCIA LABORAL");
			pan_opcion.getChildren().add(pat_panel1);


		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	/**
	 * muestra en pantalla la tabla con los datos de las cargas familiares del colaborador 
	 */
	public void dibujarDatosAmigos() {
		if (aut_empleado.getValor() != null) {
			str_opcion = "15";
			limpiarPanel();			
			tab_amigos = new Tabla();
			tab_amigos.setId("tab_amigos");
			tab_amigos.setTabla("GTH_AMIGOS_EMPRESA_EMPLEA", "IDE_GTAEE", 35);
			tab_amigos.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
			tab_amigos.getColumna("IDE_GEARE").setCombo("GEN_AREA", "IDE_GEARE", "DETALLE_GEARE", "");
			tab_amigos.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_amigos.getColumna("IDE_GTEMP").setVisible(false);
			tab_amigos.getColumna("ACTIVO_GTAEE").setCheck();
			tab_amigos.getColumna("PRIMER_NOMBRE_GTAEE").setRequerida(true);
			tab_amigos.getColumna("SEGUNDO_NOMBRE_GTAEE").setRequerida(true);
			tab_amigos.getColumna("APELLIDO_MATERNO_GTAEE").setRequerida(true);
			tab_amigos.getColumna("APELLIDO_PATERNO_GTAEE").setRequerida(true);
			tab_amigos.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_amigos.setTipoFormulario(true);
			tab_amigos.getGrid().setColumns(4);
			tab_amigos.setMostrarNumeroRegistros(true);
			tab_amigos.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_amigos);
			pan_opcion.setTitle("DATOS AMIGOS DE LA EMPRESA");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}



	/**
	 * 
	 * muestra en pantalla la tabla con los datos de la situacion economica del colaborador
	 */
	public void dibujarDatosSituacionEconomica(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "16";
			limpiarPanel();			
			tab_situacion_economica = new Tabla();
			tab_situacion_economica.setId("tab_situacion_economica");
			tab_situacion_economica.setTabla("GTH_SITUACION_ECONOMICA_EMPLEA", "IDE_GTSEE", 19);
			tab_situacion_economica.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_situacion_economica.getColumna("IDE_GTEMP").setVisible(false);
			tab_situacion_economica.getColumna("ACTIVO_GTSEE").setCheck();
			tab_situacion_economica.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_situacion_economica.getColumna("SUELDO_CONYUGE_GTSEE").setMetodoChange("calcularSueldoConyuge");
			tab_situacion_economica.getColumna("OTRO_INGRESO_GTSEE").setMetodoChange("calcularSueldoConyuge");
			tab_situacion_economica.getColumna("TOTAL_INGRESO_CONYUGE_GTSEE").setEtiqueta();
			tab_situacion_economica.setTipoFormulario(true);
			tab_situacion_economica.getGrid().setColumns(4);
			tab_situacion_economica.setMostrarNumeroRegistros(true);
			tab_situacion_economica.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_situacion_economica);
			pan_opcion.setTitle("SITUACION ECONOMICA");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}


	/**
	 * 
	 * muestra en pantalla la tabla con los datos de negocio del colaborador y participantes de negocio si lo tuviera
	 */
	public void dibujarDatosNegocio(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "17";
			limpiarPanel();			
			tab_negocio_empl = new Tabla();
			tab_negocio_empl.setId("tab_negocio_empl");
			tab_negocio_empl.setTabla("GTH_NEGOCIO_EMPLEADO", "IDE_GTNEE", 20);
			tab_negocio_empl.getColumna("IDE_GTACR").setCombo("GTH_ACTIVIDAD_RUC", "IDE_GTACR", "DETALLE_GTACR", "");
			tab_negocio_empl.getColumna("IDE_GTTAE").setCombo("GTH_TIPO_ACTIVIDAD_ECONOMICA", "IDE_GTTAE", "DETALLE_GTTAE", "");
			tab_negocio_empl.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_negocio_empl.getColumna("IDE_GTEMP").setVisible(false);
			tab_negocio_empl.getColumna("ACTIVO_GTNEE").setCheck();
			tab_negocio_empl.getColumna("PROPIO_GTNEE").setCheck();

			tab_negocio_empl.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_negocio_empl.setTipoFormulario(true);
			tab_negocio_empl.getGrid().setColumns(4);
			tab_negocio_empl.setMostrarNumeroRegistros(true);
			tab_negocio_empl.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_negocio_empl);
			pan_opcion.setTitle("DATOS DE NEGOCIO");
			pan_opcion.getChildren().add(pat_panel1);

			Tabulador tab_tabulador=new Tabulador();
			tab_tabulador.setId("tab_tabulador");

			tab_direccion = new Tabla();
			tab_direccion.setId("tab_direccion");
			tab_direccion.setTabla("GTH_DIRECCION", "IDE_GTDIR", 29);
			tab_direccion.getColumna("IDE_GTPEE").setVisible(false);
			tab_direccion.getColumna("ACTIVO_GTDIR").setCheck();
			tab_direccion.getColumna("NOTIFICACION_GTDIR").setCheck();
			tab_direccion.getColumna("IDE_GEDIP").setCombo(ser_gestion.getSqlDivisionPoliticaCiudad());
			tab_direccion.getColumna("IDE_GEDIP").setVisible(false);
			tab_direccion.getColumna("IDE_GEDIP").setAutoCompletar();
			//tab_direccion.getColumna("IDE_GEDIP").setLongitud(250);
			tab_direccion.getColumna("IDE_GTEMP").setVisible(false);
			tab_direccion.getColumna("GTH_IDE_GTEMP").setVisible(false);
			tab_direccion.getColumna("IDE_GTPEE").setVisible(false);
			tab_direccion.getColumna("IDE_GTNEE").setVisible(false);
			tab_direccion.getColumna("IDE_GTELE").setVisible(false);
			tab_direccion.getColumna("IDE_NRGAR").setVisible(false);
			tab_direccion.getColumna("IDE_GEBEN").setVisible(false);
			tab_direccion.getColumna("IDE_GTNEE").setValorDefecto(tab_negocio_empl.getValor("IDE_GTNEE"));
			tab_direccion.setCondicion("IDE_GTNEE =" + tab_negocio_empl.getValor("IDE_GTNEE"));
			tab_direccion.setIdCompleto("tab_tabulador:tab_direccion");
			utilitario.buscarNombresVisuales(tab_direccion);
			tab_direccion.dibujar();
			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setMensajeWarn("DIRECCION");
			pat_panel2.setPanelTabla(tab_direccion);

			tab_telefonos = new Tabla();
			tab_telefonos.setId("tab_telefonos");
			tab_telefonos.setTabla("GTH_TELEFONO", "IDE_GTTEL", 7);
			tab_telefonos.getColumna("IDE_GTTIT").setCombo("GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT", "");
			tab_telefonos.getColumna("IDE_GTCON").setVisible(false);
			tab_telefonos.getColumna("IDE_GTCON").setValorDefecto(null);
			tab_telefonos.getColumna("IDE_GTPEE").setVisible(false);
			tab_telefonos.getColumna("ACTIVO_GTTEL").setCheck();
			tab_telefonos.getColumna("IDE_GTEMP").setVisible(false);
			tab_telefonos.getColumna("GTH_IDE_GTEMP").setVisible(false);
			tab_telefonos.getColumna("IDE_GTPEE").setVisible(false);
			tab_telefonos.getColumna("IDE_GTNEE").setVisible(false);
			tab_telefonos.getColumna("IDE_GTELE").setVisible(false);
			tab_telefonos.getColumna("IDE_NRGAR").setVisible(false);
			tab_telefonos.getColumna("IDE_GEBEN").setVisible(false);


			tab_telefonos.getColumna("NOTIFICACION_GTTEL").setCheck();
			tab_telefonos.getColumna("IDE_GTNEE").setValorDefecto(tab_negocio_empl.getValor("IDE_GTNEE"));
			tab_telefonos.setCondicion("IDE_GTNEE =" + tab_negocio_empl.getValor("IDE_GTNEE"));
			tab_telefonos.setIdCompleto("tab_tabulador:tab_telefonos");
			tab_telefonos.dibujar();
			PanelTabla pat_panel3 = new PanelTabla();
			pat_panel3.setMensajeWarn("TELEFONOS");
			pat_panel3.setPanelTabla(tab_telefonos);

			tab_paticipantes_negocio = new Tabla();
			tab_paticipantes_negocio.setId("tab_paticipantes_negocio");
			tab_paticipantes_negocio.setTabla("GTH_PARTICIPA_NEGOCIO_EMPLEA", "IDE_GTPNE", 21);
			tab_paticipantes_negocio.getColumna("IDE_GTTDI").setCombo("GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI", "");
			tab_paticipantes_negocio.getColumna("IDE_GTNAC").setCombo("GTH_NACIONALIDAD", "IDE_GTNAC", "DETALLE_GTNAC", "");
			tab_paticipantes_negocio.getColumna("ACTIVO_GTPNE").setCheck();
			tab_paticipantes_negocio.getColumna("IDE_GTNEE").setVisible(false);
			tab_paticipantes_negocio.getColumna("IDE_GTNEE").setValorDefecto(tab_negocio_empl.getValor("ide_gtnee"));
			tab_paticipantes_negocio.setCondicion("IDE_GTNEE="+tab_negocio_empl.getValor("IDE_GTNEE"));
			tab_paticipantes_negocio.setTipoFormulario(false);
			tab_paticipantes_negocio.getGrid().setColumns(4);
			tab_paticipantes_negocio.setMostrarNumeroRegistros(false);
			tab_paticipantes_negocio.setIdCompleto("tab_tabulador:tab_paticipantes_negocio");

			tab_paticipantes_negocio.dibujar();
			PanelTabla pat_panel4 = new PanelTabla();
			pat_panel4.setMensajeWarn("PARTICIPANTES DE NEGOCIO");
			pat_panel4.setPanelTabla(tab_paticipantes_negocio); 

			tab_archivo_empleado.setId("tab_archivo_empleado");
			tab_archivo_empleado.setTabla("GTH_ARCHIVO_EMPLEADO", "IDE_GTAEM", 33);
			tab_archivo_empleado.getColumna("IDE_GTTAR").setCombo("GTH_TIPO_ARCHIVO", "IDE_GTTAR", "DETALLE_GTTAR", "");
			tab_archivo_empleado.getColumna("IDE_GTTAR").setEstilo("width:110px");
			tab_archivo_empleado.getColumna("ACTIVO_GTAEM").setCheck();
			tab_archivo_empleado.getColumna("PATH_GTAEM").setUpload("archivos_empleado");
			tab_archivo_empleado.getColumna("IDE_GTNEE").setVisible(false);
			tab_archivo_empleado.getColumna("IDE_GTTDC").setCombo("GTH_TIPO_DOCUMENTO", "IDE_GTTDC", "DETALLE_GTTDC", "");
			tab_archivo_empleado.getColumna("IDE_ASPVH").setVisible(false);
			tab_archivo_empleado.getColumna("IDE_NRBEE").setVisible(false);
			tab_archivo_empleado.getColumna("IDE_GTEMP").setVisible(false);
			tab_archivo_empleado.getColumna("IDE_GTNEE").setValorDefecto(tab_negocio_empl.getValor("ide_gtnee"));
			tab_archivo_empleado.setCondicion("IDE_GTNEE="+tab_negocio_empl.getValor("IDE_GTNEE"));
			tab_archivo_empleado.setTipoFormulario(true);
			tab_archivo_empleado.setMostrarNumeroRegistros(true);
			tab_archivo_empleado.getGrid().setColumns(6);
			tab_archivo_empleado.setIdCompleto("tab_tabulador:tab_archivo_empleado");
			tab_archivo_empleado.dibujar();
			PanelTabla pat_panel5 = new PanelTabla();
			pat_panel5.setMensajeWarn("ANEXOS DE NEGOCIO");
			pat_panel5.setPanelTabla(tab_archivo_empleado);

			tab_tabulador.agregarTab("DIRECCION", pat_panel2);
			tab_tabulador.agregarTab("TELEFONOS", pat_panel3);
			tab_tabulador.agregarTab("PARTICIPANTES NEGOCIO", pat_panel4);
			tab_tabulador.agregarTab("ANEXOS DE NEGICIO", pat_panel5);

			Division div_aux=new Division();
			div_aux.dividir2(pat_panel1, tab_tabulador, "45%", "H");
			pan_opcion.getChildren().add(div_aux);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	/**
	 * 
	 * muestra en pantalla la tabla con los datos de negocio del colaborador y participantes de negocio si lo tuviera
	 */
	public void dibujarDatosBienesInmuebles(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "18";
			limpiarPanel();			
			tab_terreno = new Tabla();
			tab_terreno.setId("tab_terreno");
			tab_terreno.setTabla("GTH_TERRENO_EMPLEADO", "IDE_GTTEE", 30);
			tab_terreno.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_terreno.getColumna("IDE_GTEMP").setVisible(false);
			tab_terreno.getColumna("ACTIVO_GTTEE").setCheck();
			tab_terreno.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_terreno.setTipoFormulario(false);
			tab_terreno.getGrid().setColumns(4);
			tab_terreno.setMostrarNumeroRegistros(false);
			tab_terreno.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_terreno);
			pan_opcion.setTitle("BIENES INMUEBLES (Terrenos)");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	/**
	 * 
	 * muestra en pantalla la tabla con los datos de negocio del colaborador y participantes de negocio si lo tuviera
	 */
	public void dibujarDatosCasa(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "19";
			limpiarPanel();			
			tab_casa = new Tabla();
			tab_casa.setId("tab_casa");
			tab_casa.setTabla("GTH_CASA_EMPLEADO", "IDE_GTCSE", 31);
			tab_casa.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_casa.getColumna("IDE_GTEMP").setVisible(false);

			List<Object> lis=new ArrayList();
			Object obj[]=new Object[2];
			obj[0]="0";
			obj[1]="Propia";
			lis.add(obj);
			Object obj1[]=new Object[2];
			obj1[0]="1";
			obj1[1]="Hipotecada";
			lis.add(obj1);
			Object obj2[]=new Object[2];
			obj2[0]="2";
			obj2[1]="Arrendada";
			lis.add(obj2);

			tab_casa.getColumna("PROPIA_GTCSE").setRadio(lis, "0");
			tab_casa.getColumna("PROPIA_GTCSE").setMetodoChange("cambiaTipoVivienda");
			tab_casa.getColumna("AFAVOR_GTCSE").setLectura(true);
			tab_casa.getColumna("ARRENDATARIO_GTCSE").setLectura(true);
			tab_casa.getColumna("MONTO_ARRIENDO_GTCSE").setLectura(true);

			tab_casa.getColumna("ACTIVO_GTCSE").setCheck();
			tab_casa.getColumna("ACTIVO_GTCSE").setValorDefecto("true");
			tab_casa.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_casa.setTipoFormulario(true);
			tab_casa.getGrid().setColumns(4);
			tab_casa.setMostrarNumeroRegistros(true);
			tab_casa.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_casa);
			pan_opcion.setTitle("CASA");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	public void cambiaTipoVivienda(AjaxBehaviorEvent evt){
		tab_casa.modificar(evt);
		if (tab_casa.getValor("PROPIA_GTCSE").equalsIgnoreCase("0")){//propia

			tab_casa.getColumna("AFAVOR_GTCSE").setLectura(true);
			tab_casa.getColumna("ARRENDATARIO_GTCSE").setLectura(true);
			tab_casa.getColumna("MONTO_ARRIENDO_GTCSE").setLectura(true);
			tab_casa.getColumna("AVALUO_GTCSE").setLectura(false);
			tab_casa.getColumna("AFAVOR_GTCSE").setRequerida(false);
			tab_casa.getColumna("ARRENDATARIO_GTCSE").setRequerida(false);
			tab_casa.getColumna("MONTO_ARRIENDO_GTCSE").setRequerida(false);
			tab_casa.getColumna("AVALUO_GTCSE").setRequerida(true);


			tab_casa.setValor("AFAVOR_GTCSE", "");
			tab_casa.setValor("ARRENDATARIO_GTCSE", "");
			tab_casa.setValor("MONTO_ARRIENDO_GTCSE", "");
			utilitario.addUpdateTabla(tab_casa, "AFAVOR_GTCSE,ARRENDATARIO_GTCSE,MONTO_ARRIENDO_GTCSE", "");

		}else if (tab_casa.getValor("PROPIA_GTCSE").equalsIgnoreCase("1")){//hipotecada
			tab_casa.getColumna("AFAVOR_GTCSE").setLectura(false);
			tab_casa.getColumna("ARRENDATARIO_GTCSE").setLectura(true);
			tab_casa.getColumna("MONTO_ARRIENDO_GTCSE").setLectura(true);
			///tab_casa.getColumna("AVALUO_GTCSE").setLectura(true);
			tab_casa.getColumna("AFAVOR_GTCSE").setRequerida(true);
			tab_casa.getColumna("ARRENDATARIO_GTCSE").setRequerida(false);
			tab_casa.getColumna("MONTO_ARRIENDO_GTCSE").setRequerida(false);
			tab_casa.getColumna("AVALUO_GTCSE").setRequerida(false);


			tab_casa.setValor("ARRENDATARIO_GTCSE", "");
			tab_casa.setValor("MONTO_ARRIENDO_GTCSE", "");
			utilitario.addUpdateTabla(tab_casa, "ARRENDATARIO_GTCSE,MONTO_ARRIENDO_GTCSE", "");

		}else if (tab_casa.getValor("PROPIA_GTCSE").equalsIgnoreCase("2")){//arrendada
			tab_casa.getColumna("AFAVOR_GTCSE").setLectura(true);
			tab_casa.getColumna("ARRENDATARIO_GTCSE").setLectura(false);
			tab_casa.getColumna("MONTO_ARRIENDO_GTCSE").setLectura(false);
			//	tab_casa.getColumna("AVALUO_GTCSE").setLectura(true);
			tab_casa.getColumna("AFAVOR_GTCSE").setRequerida(false);
			tab_casa.getColumna("ARRENDATARIO_GTCSE").setRequerida(true);
			tab_casa.getColumna("MONTO_ARRIENDO_GTCSE").setRequerida(true);
			tab_casa.getColumna("AVALUO_GTCSE").setRequerida(false);

			tab_casa.setValor("AFAVOR_GTCSE", "");

			utilitario.addUpdateTabla(tab_casa, "AFAVOR_GTCSE", "");

		}
		utilitario.addUpdate("tab_casa");
	}

	/**
	 * 
	 * muestra en pantalla la tabla con los datos de negocio del colaborador y participantes de negocio si lo tuviera
	 */
	public void dibujarDatosVehiculo(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "20";
			limpiarPanel();			
			tab_vehiculo = new Tabla();
			tab_vehiculo.setId("tab_vehiculo");
			tab_vehiculo.setTabla("GTH_VEHICULO_EMPLEADO", "IDE_GTVEE", 32);
			tab_vehiculo.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_vehiculo.getColumna("IDE_GTEMP").setVisible(false);
			tab_vehiculo.getColumna("IDE_GTMOV").setCombo("GTH_MODELO_VEHICULO", "IDE_GTMOV", "DETALLE_GTMOV","");
			tab_vehiculo.getColumna("IDE_GTMAV").setCombo("GTH_MARCA_VEHICULO", "IDE_GTMAV", "DETALLE_GTMAV","");
			tab_vehiculo.getColumna("IDE_GTMAV").setMetodoChange("cambiaMarcaVehiculo");
			tab_vehiculo.getColumna("IDE_GEINS").setCombo("select IDE_GEINS,DETALLE_GEINS from  GEN_INSTITUCION where ide_getii in ("+utilitario.getVariable("p_gen_casas_comerciales")+")");

			List<Object> lis=new ArrayList();
			Object obj[]=new Object[2];
			obj[0]="0";
			obj[1]="Prendado";
			lis.add(obj);
			Object obj1[]=new Object[2];
			obj1[0]="1";
			obj1[1]="Propio";
			lis.add(obj1);

			tab_vehiculo.getColumna("PROPIO_PRENDADO_GTVEE").setRadio(lis, "0");
			tab_vehiculo.getColumna("PROPIO_PRENDADO_GTVEE").setMetodoChange("cambiaTipoVehiculo");
			tab_vehiculo.getColumna("PRENDADO_AFAVOR_GTVEE").setLectura(false);

			tab_vehiculo.getColumna("ACTIVO_GTVEE").setCheck();
			tab_vehiculo.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_vehiculo.setTipoFormulario(true);
			tab_vehiculo.getGrid().setColumns(4);
			tab_vehiculo.setMostrarNumeroRegistros(true);
			tab_vehiculo.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_vehiculo);
			pan_opcion.setTitle("VEHICULO");
			pan_opcion.getChildren().add(pat_panel1);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	public void cambiaTipoVehiculo(AjaxBehaviorEvent evt){
		tab_vehiculo.modificar(evt);
		if (tab_vehiculo.getValor("PROPIO_PRENDADO_GTVEE").equalsIgnoreCase("0")){//prendado
			tab_vehiculo.getColumna("PRENDADO_AFAVOR_GTVEE").setLectura(false);
			tab_vehiculo.getColumna("PRENDADO_AFAVOR_GTVEE").setRequerida(true);
		}else if (tab_vehiculo.getValor("PROPIO_PRENDADO_GTVEE").equalsIgnoreCase("1")){//propio
			tab_vehiculo.setValor("PRENDADO_AFAVOR_GTVEE","");
			tab_vehiculo.getColumna("PRENDADO_AFAVOR_GTVEE").setLectura(true);
			tab_vehiculo.getColumna("PRENDADO_AFAVOR_GTVEE").setRequerida(false);
		}
		utilitario.addUpdate("tab_vehiculo");
	}
	public void cambiaMarcaVehiculo(AjaxBehaviorEvent evt){
		tab_vehiculo.modificar(evt);
		tab_vehiculo.getColumna("IDE_GTMOV").setCombo("GTH_MODELO_VEHICULO", "IDE_GTMOV", "DETALLE_GTMOV","IDE_GTMAV="+tab_vehiculo.getValor("IDE_GTMAV"));
		utilitario.addUpdateTabla(tab_vehiculo,"IDE_GTMOV", "");
	}

	/**
	 * 
	 * muestra en pantalla la tabla con los datos de Situacion Financiera del colaborador
	 */
	public void dibujarDatosSituacionFinanciera(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "21";
			limpiarPanel();			
			tab_situacion_financiera = new Tabla();
			tab_situacion_financiera.setId("tab_situacion_financiera");
			tab_situacion_financiera.setTabla("GTH_SITUACION_FINANCIERA_EMPLE", "IDE_GTSFE", 24);
			tab_situacion_financiera.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_situacion_financiera.getColumna("IDE_GTEMP").setVisible(false);
			tab_situacion_financiera.getColumna("ACTIVO_GTSFE").setCheck();
			tab_situacion_financiera.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_situacion_financiera.setTipoFormulario(true);
			tab_situacion_financiera.getGrid().setColumns(4);
			tab_situacion_financiera.setMostrarNumeroRegistros(true);
			tab_situacion_financiera.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_situacion_financiera);
			pan_opcion.setTitle("DATOS DE SITUACION FINANCIERA");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}
	
	


	/**
	 * 
	 * muestra en pantalla la tabla con los datos de Cuenta Bancaria del colaborador
	 */
	public void dibujarDatosCuentaBancaria(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "22";
			limpiarPanel();			
			tab_cuenta_bancaria = new Tabla();
			tab_cuenta_bancaria.setId("tab_cuenta_bancaria");
			tab_cuenta_bancaria.setTabla("GTH_CUENTA_BANCARIA_EMPLEADO", "IDE_GTCBE", 23);
			tab_cuenta_bancaria.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "GEN_IDE_GEINS IS NOT NULL and IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));		

			//tab_cuenta_bancaria.getColumna("IDE_GEINS").setAutoCompletar();
			tab_cuenta_bancaria.getColumna("IDE_GTTCB").setCombo("GTH_TIPO_CUENTA_BANCARIA", "IDE_GTTCB", "DETALLE_GTTCB", "");
			tab_cuenta_bancaria.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_cuenta_bancaria.getColumna("IDE_GTEMP").setVisible(false);
			tab_cuenta_bancaria.getColumna("ACREDITACION_GTCBE").setCheck();
			tab_cuenta_bancaria.getColumna("ACREDITACION_GTCBE").setMetodoChange("cambiaEstadoAcreditacion");
			tab_cuenta_bancaria.getColumna("SALDO_PROMEDIO_GTCBE").setDecimales(3);
			tab_cuenta_bancaria.getColumna("individual_conjunta_gtcbe").setVisible(false);
			tab_cuenta_bancaria.getColumna("IDE_GTCBE").setNombreVisual("C�DIGO");
			tab_cuenta_bancaria.getColumna("IDE_GTTCB").setNombreVisual("TIPO CUENTA");
			tab_cuenta_bancaria.getColumna("SALDO_PROMEDIO_GTCBE").setNombreVisual("SALDO PROMEDIO");
			tab_cuenta_bancaria.getColumna("ACREDITACION_GTCBE").setNombreVisual("ACREDITACióN");
			tab_cuenta_bancaria.getColumna("IDE_GEINS").setNombreVisual("INSTITUCióN");
			tab_cuenta_bancaria.getColumna("NUMERO_CUENTA_GTCBE").setNombreVisual("N�MERO DE CUENTA");
			tab_cuenta_bancaria.getColumna("ACTIVO_GTCBE").setNombreVisual("ACTIVO");
			tab_cuenta_bancaria.getColumna("ACTIVO_GTCBE").setCheck();
			tab_cuenta_bancaria.getColumna("ACTIVO_GTCBE").setMetodoChange("cambiaEstadoCuentaBancaria");
			tab_cuenta_bancaria.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			List<Object> lis=new ArrayList();
			Object obj[]=new Object[2];
			obj[0]="0";
			obj[1]="Conjunta";
			lis.add(obj);
			Object obj1[]=new Object[2];
			obj1[0]="1";
			obj1[1]="Individual";
			lis.add(obj1);						
			tab_cuenta_bancaria.getColumna("INDIVIDUAL_CONJUNTA_GTCBE").setRadio(lis, "0");
			tab_cuenta_bancaria.getColumna("IDE_GEBEN").setVisible(false);		
			tab_cuenta_bancaria.setTipoFormulario(true);
			tab_cuenta_bancaria.getGrid().setColumns(4);
			tab_cuenta_bancaria.setMostrarNumeroRegistros(true);
			tab_cuenta_bancaria.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_cuenta_bancaria);
			pan_opcion.setTitle("DATOS DE CUENTA BANCARIA");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}


	public void cambiaEstadoAcreditacion(AjaxBehaviorEvent evt){
		tab_cuenta_bancaria.modificar(evt);
		System.out.println("estdo acre "+tab_cuenta_bancaria.getValor("ACREDITACION_GTCBE"));
		if (tab_cuenta_bancaria.getValor("ACREDITACION_GTCBE").equalsIgnoreCase("true")){
			tab_cuenta_bancaria.setValor("ACTIVO_GTCBE", "true");
			utilitario.addUpdateTabla(tab_cuenta_bancaria, "ACTIVO_GTCBE", "");
		}

	}

	public void cambiaEstadoCuentaBancaria(AjaxBehaviorEvent evt){
		tab_cuenta_bancaria.modificar(evt);
		System.out.println("estdo "+tab_cuenta_bancaria.getValor("ACTIVO_GTCBE"));
		if (tab_cuenta_bancaria.getValor("ACTIVO_GTCBE").equalsIgnoreCase("false")){
			tab_cuenta_bancaria.setValor("ACREDITACION_GTCBE", "0");
			utilitario.addUpdateTabla(tab_cuenta_bancaria, "ACREDITACION_GTCBE", "");
		}

	}

	/**
	 * 
	 * muestra en pantalla la tabla con los datos de Inversion del colaborador
	 */
	public void dibujarDatosInversion(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "23";
			limpiarPanel();			
			tab_inversion = new Tabla();
			tab_inversion.setId("tab_inversion");
			tab_inversion.setTabla("GTH_INVERSION_EMPLEADO", "IDE_GTINE", 25);
			tab_inversion.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "GEN_IDE_GEINS IS NOT NULL");
			//tab_inversion.getColumna("IDE_GEINS").setAutoCompletar();
			tab_inversion.getColumna("IDE_GTTII").setCombo("GTH_TIPO_INVERSION", "IDE_GTTII", "DETALLE_GTTII", "");
			tab_inversion.getColumna("IDE_GETPR").setCombo("GEN_TIPO_PERIODO", "IDE_GETPR", "DETALLE_GETPR", "");
			tab_inversion.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_inversion.getColumna("IDE_GTEMP").setVisible(false);
			tab_inversion.getColumna("ACTIVO_GTINE").setCheck();
			tab_inversion.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_inversion.setTipoFormulario(false);
			tab_inversion.getGrid().setColumns(4);
			tab_inversion.setMostrarNumeroRegistros(true);
			tab_inversion.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_inversion);
			pan_opcion.setTitle("DATOS DE INVERSION");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}

	/**
	 * 
	 * muestra en pantalla la tabla con los datos de Enduedamiento del colaborador
	 */
	public void dibujarDatosEndeudamiento(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "24";
			limpiarPanel();			
			tab_endeudamiento = new Tabla();
			tab_endeudamiento.setId("tab_endeudamiento");
			tab_endeudamiento.setTabla("GTH_ENDEUDAMIENTO_EMPLEADO", "IDE_GTEEM", 26);
			tab_endeudamiento.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "GEN_IDE_GEINS IS NOT NULL");
			//tab_endeudamiento.getColumna("IDE_GEINS").setAutoCompletar();
			tab_endeudamiento.getColumna("IDE_GETPR").setCombo("GEN_TIPO_PERIODO", "IDE_GETPR", "DETALLE_GETPR", "");
			tab_endeudamiento.getColumna("IDE_GTTEN").setCombo("GTH_TIPO_ENDEUDAMIENTO", "IDE_GTTEN", "DETALLE_GTTEN", "");
			tab_endeudamiento.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_endeudamiento.getColumna("IDE_GTEMP").setVisible(false);
			tab_endeudamiento.getColumna("ACTIVO_GTEEM").setCheck();
			tab_endeudamiento.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_endeudamiento.setTipoFormulario(false);
			tab_endeudamiento.getGrid().setColumns(4);
			tab_endeudamiento.setMostrarNumeroRegistros(true);
			tab_endeudamiento.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_endeudamiento);
			pan_opcion.setTitle("DATOS DE ENDEUDAMIENTO");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}

	/**
	 * 
	 * muestra en pantalla la tabla con los datos de Tarjeta de Credito del colaborador
	 */
	public void dibujarDatosTarjetaCredito(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "25";
			limpiarPanel();			
			tab_tarjeta_credito = new Tabla();
			tab_tarjeta_credito.setId("tab_tarjeta_credito");
			tab_tarjeta_credito.setTabla("GTH_TARJETA_CREDITO_EMPLEADO", "IDE_GTTCE", 27);
			tab_tarjeta_credito.getColumna("IDE_GTTTB").setCombo("GTH_TIPO_TARJETA_BANCARIA", "IDE_GTTTB", "DETALLE_GTTTB", "");
			tab_tarjeta_credito.getColumna("IDE_GTTTB").setMetodoChange("cargarTarjetasBancarias");
			tab_tarjeta_credito.getColumna("IDE_GTTAB").setCombo("GTH_TARJETA_BANCARIA", "IDE_GTTAB", "DETALLE_GTTAB", "");
			tab_tarjeta_credito.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_tarjeta_credito.getColumna("IDE_GTEMP").setVisible(false);
			tab_tarjeta_credito.getColumna("ACTIVO_GTTCE").setCheck();			
			List<Object> lis=new ArrayList();
			Object obj[]=new Object[2];
			obj[0]="0";
			obj[1]="Adicional";
			lis.add(obj);
			Object obj1[]=new Object[2];
			obj1[0]="1";
			obj1[1]="Principal";
			lis.add(obj1);
			tab_tarjeta_credito.getColumna("PRINCIPAL_ADICIONAL_GTTCE").setRadio(lis, "0");			
			tab_tarjeta_credito.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_tarjeta_credito.setTipoFormulario(true);
			tab_tarjeta_credito.getGrid().setColumns(4);
			tab_tarjeta_credito.setMostrarNumeroRegistros(true);
			tab_tarjeta_credito.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_tarjeta_credito);
			pan_opcion.setTitle("DATOS DE TARJETA DE CREDITO");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}

	public void cargarTarjetasBancarias(AjaxBehaviorEvent evt){
		tab_tarjeta_credito.modificar(evt);
		tab_tarjeta_credito.getColumna("IDE_GTTAB").setCombo("GTH_TARJETA_BANCARIA", "IDE_GTTAB", "DETALLE_GTTAB", "IDE_GTTTB="+tab_tarjeta_credito.getValor("IDE_GTTTB"));
		utilitario.addUpdateTabla(tab_tarjeta_credito, "IDE_GTTAB", "");
	}

	/**
	 * muestra en pantalla la tabla con los datos de membresias del colaborador    
	 */
	public void dibujarDatosMembresias(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "26";
			limpiarPanel();			
			tab_membresias = new Tabla();
			tab_membresias.setId("tab_membresias");
			tab_membresias.setTabla("GTH_MEMBRESIA_EMPLEADO", "IDE_GTMEE", 36);
			tab_membresias.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "");
			//tab_membresias.getColumna("IDE_GEINS").setAutoCompletar();
			tab_membresias.getColumna("IDE_GTEMP").setValorDefecto(aut_empleado.getValor());
			tab_membresias.getColumna("IDE_GTEMP").setVisible(false);
			tab_membresias.getColumna("ACTIVO_GTMEE").setCheck();
			tab_membresias.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_membresias.setTipoFormulario(false);
			tab_membresias.dibujar();
			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_membresias);
			pan_opcion.setTitle("DATOS DE MEMBRESIAS");
			pan_opcion.getChildren().add(pat_panel1);
		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}
	/**
	 * muestra en pantalla la tabla con los datos de proyeccion de ingresos    
	 */

	public void dibujarDatosProyeccionIngresos(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "27";
			limpiarPanel();
			tab_sri_proy_ing = new Tabla();
			tab_sri_proy_ing.setId("tab_sri_proy_ing");
			tab_sri_proy_ing.setTabla("SRI_PROYECCION_INGRES", "IDE_SRPRI", 37);
			tab_sri_proy_ing.getColumna("IDE_SRIMR").setCombo("SRI_IMPUESTO_RENTA","IDE_SRIMR", "DETALLE_SRIMR", "");
			tab_sri_proy_ing.getColumna("IDE_SRIMR").setLongitud(200);
			tab_sri_proy_ing.getColumna("IDE_GTEMP").setVisible(false);
			tab_sri_proy_ing.getColumna("IDE_GEEDP").setVisible(false);
			tab_sri_proy_ing.getColumna("ACTIVO_SRPRI").setCheck();
			tab_sri_proy_ing.onSelect("seleccionaProyeccionIngreso");
			tab_sri_proy_ing.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
			tab_sri_proy_ing.setLectura(true);
			tab_sri_proy_ing.dibujar();

			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_sri_proy_ing);

			tab_sri_det_proy_ing = new Tabla();
			tab_sri_det_proy_ing.setId("tab_sri_det_proy_ing");
			tab_sri_det_proy_ing.setTabla("SRI_DETALLE_PROYECCCION_INGRES", "IDE_SRDPI", 38);
			tab_sri_det_proy_ing.getColumna("IDE_GEMES").setCombo("GEN_MES","IDE_GEMES", "nombre_gemes", "");
			tab_sri_det_proy_ing.setCondicion("IDE_SRPRI =" + tab_sri_proy_ing.getValorSeleccionado());
			tab_sri_det_proy_ing.setCampoOrden("IDE_GEMES ASC");
			tab_sri_det_proy_ing.getColumna("IDE_SRPRI").setVisible(false);
			tab_sri_det_proy_ing.getColumna("IDE_GEMES").setLongitud(100);
			tab_sri_det_proy_ing.getColumna("VALOR_SRDPI").setLongitud(100);
			tab_sri_det_proy_ing.getColumna("VALOR_SRDPI").alinearDerecha();
			tab_sri_det_proy_ing.getColumna("IDE_GEMES").alinearDerecha();
			//			tab_sri_det_proy_ing.getColumna("VALOR_SRDPI").setFormatoNumero(2);
			tab_sri_det_proy_ing.setColumnaSuma("VALOR_SRDPI");			
			tab_sri_det_proy_ing.setLectura(true);
			tab_sri_det_proy_ing.dibujar();

			PanelTabla pat_panel2 = new PanelTabla();
			pat_panel2.setPanelTabla(tab_sri_det_proy_ing);

			Division div_sri=new Division();
			div_sri.dividir2(pat_panel1, pat_panel2, "20%","H");
			pan_opcion.setTitle("RETENCIONES");
			pan_opcion.getChildren().add(div_sri);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}

	}


	public void seleccionaProyeccionIngreso(SelectEvent evt){
		tab_sri_proy_ing.seleccionarFila(evt);
		tab_sri_det_proy_ing.setCondicion("IDE_SRPRI =" + tab_sri_proy_ing.getValorSeleccionado());
		tab_sri_det_proy_ing.setCampoOrden("IDE_GEMES ASC");
		tab_sri_det_proy_ing.ejecutarSql();
	}

	/**
	 * muestra en pantalla la tabla con los datos de impuesto a la renta y gastos deducibles    
	 */

	public void dibujarGastosDeducibles(){
		if (aut_empleado.getValor() != null) {
			str_opcion = "29";
			limpiarPanel();		
			tab_sri_impuesto_renta = new Tabla();
			tab_sri_impuesto_renta.setId("tab_sri_impuesto_renta");
			tab_sri_impuesto_renta.setTabla("SRI_IMPUESTO_RENTA", "IDE_SRIMR", 41);
			tab_sri_impuesto_renta.onSelect("seleccionarImpuestoRenta");
			tab_sri_impuesto_renta.getColumna("ACTIVO_SRIMR").setCheck();			
			tab_sri_impuesto_renta.setLectura(true);
			tab_sri_impuesto_renta.dibujar();

			PanelTabla pat_panel1 = new PanelTabla();
			pat_panel1.setPanelTabla(tab_sri_impuesto_renta);
			pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);			
			pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);


			tab_sri_gastos_deducible = new Tabla();
			tab_sri_gastos_deducible.setId("tab_sri_gastos_deducible");
			tab_sri_gastos_deducible.setTabla("SRI_DEDUCIBLES_EMPLEADO", "IDE_SRDEE", 42);
			tab_sri_gastos_deducible.getColumna("IDE_GTEMP").setVisible(false);
			tab_sri_gastos_deducible.getColumna("ACTIVO_SRDEE").setCheck();			
			tab_sri_gastos_deducible.getColumna("IDE_SRDED").setCombo("select IDE_SRDED,DETALLE_SRDED from  SRI_DEDUCIBLES WHERE IDE_SRIMR="+tab_sri_impuesto_renta.getValorSeleccionado());
			tab_sri_gastos_deducible.getColumna("IDE_SRDED").setUnico(true);
			tab_sri_gastos_deducible.getColumna("IDE_GTEMP").setUnico(true);
			//tab_sri_gastos_deducible.getColumna("IDE_SRDED")
			tab_sri_gastos_deducible.getColumna("VALOR_DEDUCIBLE_SRDEE").setLectura(false);
			tab_sri_gastos_deducible.getColumna("VALOR_DEDUCIBLE_SRDEE").setMetodoChange("calcularTotalesDeducibles");
			tab_sri_gastos_deducible.getColumna("OBSERVACION_SRDEE").setLectura(false);			
			tab_sri_gastos_deducible.getColumna("OBSERVACION_SRDEE").setLongitud(100);
			tab_sri_gastos_deducible.setColumnaSuma("VALOR_DEDUCIBLE_SRDEE");
			tab_sri_gastos_deducible.setCondicion(" IDE_SRDED IN (SELECT IDE_SRDED FROM SRI_DEDUCIBLES WHERE IDE_SRIMR="+tab_sri_impuesto_renta.getValor("IDE_SRIMR")+") AND IDE_GTEMP= " +aut_empleado.getValor());						
			tab_sri_gastos_deducible.setRecuperarLectura(true);
			tab_sri_gastos_deducible.dibujar();

			PanelTabla pat_panel2 = new PanelTabla();	
			pat_panel2.setPanelTabla(tab_sri_gastos_deducible);
			pat_panel2.getMenuTabla().getItem_insertar().setRendered(true);			
			pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);

			ItemMenu itm_modificar=new ItemMenu();
			itm_modificar.setMetodo("modificarGasto");
			itm_modificar.setValue("Modificar");
			pat_panel2.getMenuTabla().getChildren().add(itm_modificar);

			Division div_sri_gasto_deduc=new Division();
			div_sri_gasto_deduc.dividir2(pat_panel1, pat_panel2, "20%","H");		
			pan_opcion.setTitle("DEDUCIBLES EMPLEADO");
			pan_opcion.getChildren().add(div_sri_gasto_deduc);

		} else {
			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
			limpiar();
		}
	}


	public void modificarGasto(){
		tab_sri_gastos_deducible.getFilas().get(tab_sri_gastos_deducible.getFilaActual()).setLectura(false);
		utilitario.addUpdate("tab_sri_gastos_deducible");

	}

	/*
	 * Calcula los totales de los valores deducibles
	 * */


	public void calcularTotalesDeducibles(AjaxBehaviorEvent evt){
		tab_sri_gastos_deducible.modificar(evt);
		tab_sri_gastos_deducible.sumarColumnas();
		utilitario.addUpdate("tab_sri_gastos_deducible");
	}

	public void seleccionarImpuestoRenta(SelectEvent evt){
		tab_sri_impuesto_renta.seleccionarFila(evt);
		tab_sri_gastos_deducible.getColumna("IDE_SRDED").setCombo("select IDE_SRDED,DETALLE_SRDED from  SRI_DEDUCIBLES WHERE IDE_SRIMR="+tab_sri_impuesto_renta.getValorSeleccionado());
		tab_sri_gastos_deducible.setCondicion(" IDE_SRDED IN (SELECT IDE_SRDED FROM SRI_DEDUCIBLES WHERE IDE_SRIMR="+tab_sri_impuesto_renta.getValor("IDE_SRIMR")+") AND IDE_GTEMP= "+aut_empleado.getValor());
		tab_sri_gastos_deducible.ejecutarSql();
		tab_sri_gastos_deducible.sumarColumnas();		
		System.out.println("sri gastos"+tab_sri_gastos_deducible.getSql());
		utilitario.addUpdate("tab_sri_gastos_deducible");
	}

	public void seleccionarCuentaAnticipo(SelectEvent evt){
		tab_anio.seleccionarFila(evt);
		tab_cuenta_anticipo.setCondicion("IDE_GTEMP=" + aut_empleado.getValor()+" AND IDE_GEANI="+tab_anio.getValor("ide_geani"));
		tab_cuenta_anticipo.ejecutarSql();
		//tab_cuenta_anticipo.getColumna("ide_gtemp").setValorDefecto(tab_anio.getValorSeleccionado());
		tab_cuenta_anticipo.getColumna("ide_gtemp").setValorDefecto(aut_empleado.getValor());
		tab_cuenta_anticipo.getColumna("ide_geani").setValorDefecto(tab_anio.getValorSeleccionado());
		System.out.println("salir"+tab_cuenta_anticipo.getSql());
		utilitario.addUpdate("tab_cuenta_anticipo");

	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_empleado.limpiar();
		tab_conyuge.limpiar();
		tab_union_libre.limpiar();
		aut_empleado.limpiar();
		tab_persona_emergencia.limpiar();
		tab_familiar.limpiar();
		tab_cargas_familiares.limpiar();
		tab_amigos.limpiar();
		tab_telefonos.limpiar();
		tab_correos.limpiar();
		tab_discapacidad.limpiar();
		tab_seguro_vida.limpiar();
		tab_educacion.limpiar();
		tab_idiomas.limpiar();
		tab_experiencia_docente.limpiar();
		tab_registro_militar.limpiar();
		tab_capacitacion.limpiar();
		tab_experiencia_laboral.limpiar();
		tab_situacion_economica.limpiar();
		tab_negocio_empl.limpiar();
		tab_paticipantes_negocio.limpiar();
		tab_documentacion.limpiar();
		tab_cuenta_anticipo.limpiar();
		tab_anio.limpiar();
		tab_cuenta_bancaria.limpiar();
		tab_situacion_financiera.limpiar();
		tab_inversion.limpiar();
		tab_endeudamiento.limpiar();
		tab_tarjeta_credito.limpiar();
		tab_empleado_departamento.limpiar();
		tab_direccion.limpiar();
		tab_terreno.limpiar();
		tab_casa.limpiar();
		tab_vehiculo.limpiar();
		tab_hobbies.limpiar();
		tab_archivo_empleado.limpiar();
		tab_telefonos.limpiar();
		tab_membresias.limpiar();
		tab_sri_det_proy_ing.limpiar();
		tab_sri_proy_ing.limpiar();
		tab_sri_impuesto_renta.limpiar();
		tab_sri_gastos_deducible.limpiar();
		tab_deta_empleado_depar.limpiar();
		dia_filtro_activo.Limpiar();
		dia_deta_emplea_depar.Limpiar();
		tab_beneficiario_seguro.limpiar();
		//str_opcion = "";
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}

	/**
	 * filtra el empleado que selecciona en el autocompletar y carga la tabla
	 * empleados
	 *
	 * @param evt
	 */
	public void filtrarEmpleado(SelectEvent evt) {

		aut_empleado.onSelect(evt);
		if (str_opcion.equals("0")){dibujarDatosPersona();}
		else if (str_opcion.equals("1")){dibujarDatosConyuge();}
		else if (str_opcion.equals("2")){dibujarDatosEmpleadoDepartamento();}
		else if(str_opcion.equals("3")){dibujarDatosDocumentacion();}
		else if(str_opcion.equals("4")){dibujarDatosEmergencia();}
		else if(str_opcion.equals("5")){dibujarDatosCargasFamiliares();}
		else if(str_opcion.equals("6")){dibujarDatosFamiliares();}
		else if(str_opcion.equals("7")){dibujarDatosSeguroVida();}
		else if(str_opcion.equals("8")){dibujarDatosRegistroMilitar();}
		else if(str_opcion.equals("9")){dibujarDatosHobbies();}
		else if(str_opcion.equals("10")){dibujarDatosEducacion();}
		else if(str_opcion.equals("11")){dibujarDatosIdiomas();}
		else if(str_opcion.equals("12")){dibujarDatosCapacitacion();}
		else if(str_opcion.equals("14")){dibujarDatosExperienciaLaboral();}
		else if(str_opcion.equals("15")){dibujarDatosAmigos();}
		else if(str_opcion.equals("16")){dibujarDatosSituacionEconomica();}
		else if(str_opcion.equals("17")){dibujarDatosNegocio();}
		else if(str_opcion.equals("18")){dibujarDatosBienesInmuebles();}
		else if(str_opcion.equals("19")){dibujarDatosCasa();}
		else if(str_opcion.equals("20")){dibujarDatosVehiculo();}
		else if(str_opcion.equals("21")){dibujarDatosSituacionFinanciera();}
		else if(str_opcion.equals("22")){dibujarDatosCuentaBancaria();}
		else if(str_opcion.equals("23")){dibujarDatosInversion();}
		else if(str_opcion.equals("24")){dibujarDatosEndeudamiento();}
		else if(str_opcion.equals("25")){dibujarDatosTarjetaCredito();}
		else if(str_opcion.equals("26")){dibujarDatosMembresias();}
		else if(str_opcion.equals("27")){dibujarDatosProyeccionIngresos();}
		else if(str_opcion.equals("28")){dibujarCuentaAnticipo();}
		else if(str_opcion.equals("29")){dibujarGastosDeducibles();};

		utilitario.addUpdate("pan_opcion");

		utilitario.addUpdate("pan_opcion");
	}

	/**
	 * @param ide_gttdi
	 * @param documento_identidad_gttdi
	 * @return
	 * 
	 * metodo booleano para validar el tipo de documento de identidad cedula y ruc
	 */
	public boolean validarDocumentoIdentidad(String ide_gttdi,String documento_identidad){
		if (ide_gttdi!=null && !ide_gttdi.isEmpty()){
			if (documento_identidad!=null && !documento_identidad.isEmpty()){
				if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))){
					if (!utilitario.validarCedula(documento_identidad)){
						utilitario.agregarMensajeInfo("Atencion", "El numero de cedula ingresado no es valido");
						return false;
					}
				}else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))){
					if (!utilitario.validarRUC(documento_identidad)){
						utilitario.agregarMensajeInfo("Atencion", "El numero de RUC ingresado no es valido");
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		// OPCION DATOS PERSONALES
		if (str_opcion.equals("0")) {
			if (tab_empleado.isFocus()){
				if (!tab_empleado.isFilaInsertada()){
					tab_empleado.setCondicion("ide_gtemp=-1");
					tab_empleado.ejecutarSql();
					tab_empleado.insertar();
					aut_empleado.limpiar();
					utilitario.addUpdate("aut_empleado");
					tab_direccion.limpiar();
					tab_telefonos.limpiar();
					tab_correos.limpiar();
					tab_discapacidad.limpiar();
					tab_archivo_empleado.limpiar();
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Debe guardar el registro que esta trabajando");
				}
			}else if(tab_telefonos.isFocus()){
				if (aut_empleado.getValor() !=null && tab_empleado.getTotalFilas()>0){
					tab_telefonos.insertar();
					tab_telefonos.setValor("IDE_GTEMP", aut_empleado.getValor());
					tab_telefonos.setValor("IDE_GTCON", null);
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Colaborador");
				}
			}else if(tab_direccion.isFocus()){
				if (aut_empleado.getValor() !=null && tab_empleado.getTotalFilas()>0){
					tab_direccion.insertar();
					tab_direccion.setValor("IDE_GTEMP", aut_empleado.getValor());
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Colaborador");
				}
			}


			else if(tab_correos.isFocus()){
				if (aut_empleado.getValor() !=null && tab_empleado.getTotalFilas()>0){
					tab_correos.insertar();
					tab_correos.setValor("IDE_GTEMP", aut_empleado.getValor());
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Colaborador");
				}
			}
			else if(tab_discapacidad.isFocus()){
				if (tab_empleado.getTotalFilas()>0){
					if (tab_empleado.getValor("DISCAPACITADO_GTEMP")==null 
							|| tab_empleado.getValor("DISCAPACITADO_GTEMP").isEmpty()
							|| tab_empleado.getValor("DISCAPACITADO_GTEMP").equalsIgnoreCase("false")
							|| tab_empleado.getValor("DISCAPACITADO_GTEMP").equalsIgnoreCase("0")){
						utilitario.agregarMensajeInfo("No se puede insertar una discapacidad ", "el campo discapacitado se encuentra desactivado");
						return;
					}

					tab_discapacidad.insertar();
					tab_discapacidad.setValor("IDE_GTEMP", aut_empleado.getValor());
					tab_discapacidad.setValor("PORCENTAJE_GTDIE","0");
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Colaborador");
				}
			}
			else if(tab_archivo_empleado.isFocus()){
				if (tab_empleado.getTotalFilas()>0){
					tab_archivo_empleado.insertar();
					tab_archivo_empleado.setValor("IDE_GTEMP", aut_empleado.getValor());
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Colaborador");
				}
			}
		} else if (str_opcion.equals("1")) {// OPCION CONYUGUE
			if (aut_empleado.getValor()!=null){
				if (tab_conyuge.isFocus()){
					tab_conyuge.insertar();
					tab_conyuge.setValor("IDE_GTEMP",aut_empleado.getValor());
					tab_telefonos.limpiar();
					tab_union_libre.limpiar();
				}else if(tab_telefonos.isFocus()){
					if (tab_conyuge.getTotalFilas()>0){
						tab_telefonos.insertar();
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Conyuge");
					}
				}else if(tab_union_libre.isFocus()){
					if (tab_conyuge.getTotalFilas()>0){
						tab_union_libre.insertar();
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Conyuge");
					}
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("2")) { // OPCION EMPLEADO DEPARTAMENTO
			if (aut_empleado.getValor()!=null){
				if(tab_deta_empleado_depar.isFocus()){
					System.out.println("si ingresa al metodo");					
					tab_deta_empleado_depar.insertar();					
				}else if(tab_empleado_departamento.isFocus()){
					if (tab_deta_empleado_depar.getTotalFilas()>0) {	
						limpiarCombosDepartamentoEmpleado();
						tab_empleado_departamento.insertar();
						tab_empleado_departamento.setValor("IDE_SUCU",null);
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del Empleado Departamento");
					}
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("3")) { // OPCION DOCUMENTACION 
			if (aut_empleado.getValor()!=null){
				tab_documentacion.insertar();
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("4")) {  // OPCION EN CASO DE EMERGENCIA
			if (aut_empleado.getValor()!=null){
				if (tab_persona_emergencia.isFocus()){
					tab_persona_emergencia.insertar();
					tab_persona_emergencia.setValor("IDE_GTEMP", aut_empleado.getValor());
					tab_telefonos.limpiar();
					tab_direccion.limpiar();
				}else if(tab_telefonos.isFocus()){
					if (tab_persona_emergencia.getTotalFilas()>0){
						tab_telefonos.insertar();
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos de emergencia");
					}
				}else if(tab_direccion.isFocus()){
					if (tab_persona_emergencia.getTotalFilas()>0){
						tab_direccion.insertar();
						tab_direccion.setValor("IDE_GTEMP", "");
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos de emergencia");
					}
				}

			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("28")) { // OPCION PARTIDA ANTICIPO
			if (aut_empleado.getValor()!=null){
				tab_cuenta_anticipo.insertar();
				tab_cuenta_anticipo.setValor("IDE_GTEMP", aut_empleado.getValor());
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("5")) {  // OPCION DEPENDIENTES (HIJOS-HERMANOS) 
			if (aut_empleado.getValor()!=null){
				tab_cargas_familiares.insertar();
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("6")) {   // OPCION FAMILIARES
			if (aut_empleado.getValor()!=null){
				tab_familiar.insertar();
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}
		//		else if (str_opcion.equals("8")){   // OPCION DIRECCION
		//			if (aut_empleado.getValor()!=null){
		//				if (tab_direccion.isFocus()){
		//					tab_direccion.insertar();
		//				}
		//			}else {
		//				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
		//			}
		//		}else if (str_opcion.equals("9")) {  // OPCION TELEFONOS
		//			if (aut_empleado.getValor()!=null){
		//				tab_telefonos.insertar();
		//			}else {
		//				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
		//			}
		//		}else if (str_opcion.equals("10")) {  // OPCION CORREOS
		//			if (aut_empleado.getValor()!=null){
		//				tab_correos.insertar();
		//			}else {
		//				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
		//			}
		//		}
		else if (str_opcion.equals("7")){  // OPCION SEGURO DE VIDA
			if (aut_empleado.getValor()!=null){
				if (tab_seguro_vida.isFocus()){
					tab_seguro_vida.insertar();
					tab_beneficiario_seguro.limpiar();
				}else if (tab_beneficiario_seguro.isFocus()){
					if (tab_seguro_vida.getTotalFilas()>0){
						tab_beneficiario_seguro.insertar();
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existen Datos de Seguro de Vida");
					}
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("8")){  // OPCION REGISTRO MILITAR
			if (aut_empleado.getValor()!=null){
				if (tab_registro_militar.isFocus()){
					tab_registro_militar.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("9")){   // OPCION HOBBIES
			if (aut_empleado.getValor()!=null){
				if (tab_hobbies.isFocus()){
					tab_hobbies.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("10")){ // OPCION EDUCACION
			if (aut_empleado.getValor()!=null){
				if (tab_educacion.isFocus()){
					tab_educacion.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("11")){  // OPCION IDIOMAS
			if (aut_empleado.getValor()!=null){
				if (tab_idiomas.isFocus()){
					tab_idiomas.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("12")){ // OPCION CAPACITACION
			if (aut_empleado.getValor()!=null){
				if (tab_capacitacion.isFocus()){
					tab_capacitacion.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("13")){   // OPCION EXPERIENCIA DOCENTE
			if (aut_empleado.getValor()!=null){
				if (tab_experiencia_docente.isFocus()){
					tab_experiencia_docente.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("14")){   // OPCION EXPERIENCIA LABORAL
			System.out.println("entre aqui primero "+str_opcion);
			if (aut_empleado.getValor()!=null){
				if (tab_experiencia_laboral.isFocus()){
					tab_experiencia_laboral.insertar();
					System.out.println("entre aqui empleado ");
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("15")) {
			if (aut_empleado.getValor()!=null){  // OPCION AMIGOS DENTRO DEL BIESS
				tab_amigos.insertar();
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("16")){  // OPCION SITUACION ECONOMICA
			if (aut_empleado.getValor()!=null){
				if (tab_situacion_economica.isFocus()){
					tab_situacion_economica.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("17")){  // OPCION NEGOCIO PROPIO
			if (aut_empleado.getValor()!=null){
				if (tab_negocio_empl.isFocus()){
					tab_negocio_empl.insertar();
					tab_paticipantes_negocio.limpiar();
				}else if (tab_paticipantes_negocio.isFocus()){
					if (tab_negocio_empl.getTotalFilas()>0){
						tab_paticipantes_negocio.insertar();
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del negocio");
					}
				}else if (tab_direccion.isFocus()){
					if (tab_negocio_empl.getTotalFilas()>0){
						tab_direccion.insertar();
						tab_direccion.setValor("IDE_GTNEE", tab_negocio_empl.getValorSeleccionado());
						tab_direccion.setValor("IDE_GTEMP", null);
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del negocio");
					}
				}else if (tab_telefonos.isFocus()){
					if (tab_negocio_empl.getTotalFilas()>0){
						tab_telefonos.insertar();
						tab_telefonos.setValor("IDE_GTNEE", tab_negocio_empl.getValorSeleccionado());
						tab_telefonos.setValor("IDE_GTEMP", null);

					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del negocio");
					}
				}else if (tab_archivo_empleado.isFocus()){
					if (tab_negocio_empl.getTotalFilas()>0){
						tab_archivo_empleado.insertar();
					}else{
						utilitario.agregarMensajeInfo("No se puede insertar", "No existe datos del negocio");
					}
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("18")){  // OPCION TERRENO
			if (aut_empleado.getValor()!=null){
				if (tab_terreno.isFocus()){
					tab_terreno.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("19")){  // OPCION CASA
			if (aut_empleado.getValor()!=null){
				if (tab_casa.isFocus()){
					tab_casa.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("20")){  // OPCION VEHICULO
			if (aut_empleado.getValor()!=null){
				if (tab_vehiculo.isFocus()){
					tab_vehiculo.getColumna("IDE_GTMOV").setCombo("GTH_MODELO_VEHICULO", "IDE_GTMOV", "DETALLE_GTMOV","IDE_GTMAV=-1");
					tab_vehiculo.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("21")){   // OPCION SITUACION FINANCIERA
			if (aut_empleado.getValor()!=null){
				if (tab_situacion_financiera.isFocus()){
					tab_situacion_financiera.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("22")){   // OPCION CUENTA BANCARIA
			if (aut_empleado.getValor()!=null){
				if (tab_cuenta_bancaria.isFocus()){
					tab_cuenta_bancaria.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("23")){   // OPCION INVERSIONES
			if (aut_empleado.getValor()!=null){
				if (tab_inversion.isFocus()){
					tab_inversion.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("24")){  // OPCION PRESTAMOS
			if (aut_empleado.getValor()!=null){
				if (tab_endeudamiento.isFocus()){
					tab_endeudamiento.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("25")){   // OPCION TARJETAS DE CREDITO
			if (aut_empleado.getValor()!=null){
				if (tab_tarjeta_credito.isFocus()){
					tab_tarjeta_credito.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}else if (str_opcion.equals("26")){ // OPCION MEMBRESIAS
			if (aut_empleado.getValor()!=null){
				if (tab_membresias.isFocus()){
					tab_membresias.insertar();
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}
		else if (str_opcion.equals("29")){ // OPCION GASTOS DEDUCIBLES
			if (aut_empleado.getValor()!=null){
				if (tab_sri_gastos_deducible.isFocus()){
					tab_sri_gastos_deducible.insertar();
					tab_sri_gastos_deducible.setValor("IDE_GTEMP", aut_empleado.getValor());
				}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar un Colaborador en el autocompletar");
			}
		}
	}



	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (str_opcion.equals("0")) {
			if (tab_empleado.isFilaInsertada()||tab_empleado.isFilaModificada()){
				if (validarDocumentoIdentidad(tab_empleado.getValor("IDE_GTTDI"),tab_empleado.getValor("DOCUMENTO_IDENTIDAD_GTEMP"))){


					// valida q la fecha de nacimiento no sea mayor a la fecha actual
					if(tab_empleado.getValor("FECHA_NACIMIENTO_GTEMP")!=null && !tab_empleado.getValor("FECHA_NACIMIENTO_GTEMP").isEmpty()){
						if(utilitario.isFechaMayor(utilitario.getFecha(tab_empleado.getValor("FECHA_NACIMIENTO_GTEMP")),utilitario.getFecha(utilitario.getFechaActual()))){
							utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de nacimiento del empleado no puede ser mayor que la fecha de actual");
							return;							
						}
					}

					if(tab_empleado.getValor("fecha_ingreso_grupo_gtemp")!=null && !tab_empleado.getValor("fecha_ingreso_grupo_gtemp").isEmpty()){
						if(utilitario.isFechaMayor(utilitario.getFecha(tab_empleado.getValor("fecha_ingreso_grupo_gtemp")),utilitario.getFecha(utilitario.getFechaActual()))){
							utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de ingreso del empleado no puede ser mayor que la fecha de actual");
							return;
						}
					}


					if(tab_empleado.getValor("fecha_ingreso_gtemp")!=null && !tab_empleado.getValor("fecha_ingreso_gtemp").isEmpty()){
						if(utilitario.isFechaMayor(utilitario.getFecha(tab_empleado.getValor("fecha_ingreso_gtemp")),utilitario.getFecha(utilitario.getFechaActual()))){
							utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de ingreso de grupo del empleado no puede ser mayor que la fecha de actual");
							return;							
						}
					}


					if(tab_empleado.getValor("fecha_ingreso_pais_gtemp")!=null && !tab_empleado.getValor("fecha_ingreso_pais_gtemp").isEmpty()){
						if(utilitario.isFechaMayor(utilitario.getFecha(tab_empleado.getValor("fecha_ingreso_pais_gtemp")),utilitario.getFecha(utilitario.getFechaActual()))){
							utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de ingreso al pais del empleado no puede ser mayor que la fecha de actual");
							return;
						}
					}



					if (tab_empleado.guardar()){
						tab_direccion.guardar();

						for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
							if (!utilitario.isEnteroPositivo(tab_telefonos.getValor(i,"numero_telefono_gttel"))){
								utilitario.agregarMensajeInfo("No se puede Guardar el telefono", "El numero es invalido");
								return ;
							}
						}

						tab_telefonos.guardar();					
						for (int i = 0; i < tab_correos.getTotalFilas(); i++) {
							if(!utilitario.isEmailValido(tab_correos.getValor(i,"DETALLE_GTCOR"))){
								utilitario.agregarMensajeInfo("No se puede Guardar el correo", "El correo Invalido");
								return;
							}	
						}

						tab_correos.guardar();

						tab_discapacidad.guardar();
						tab_archivo_empleado.guardar();
						guardarPantalla();
						//						aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP from GTH_EMPLEADO");
						aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP ||' '|| PRIMER_NOMBRE_GTEMP ||' '|| SEGUNDO_NOMBRE_GTEMP as nombres from GTH_EMPLEADO");

						aut_empleado.setValor(tab_empleado.getValor("IDE_GTEMP"));
						utilitario.addUpdate("aut_empleado");
					}
				}
			}else{
				if (tab_empleado.guardar()){
					tab_direccion.guardar();

					for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
						if (!utilitario.isEnteroPositivo(tab_telefonos.getValor(i,"numero_telefono_gttel"))){
							utilitario.agregarMensajeInfo("No se puede Guardar el telefono", "El numero es invalido");
							return ;
						}
					}

					tab_telefonos.guardar();
					for (int i = 0; i < tab_correos.getTotalFilas(); i++) {
						if(!utilitario.isEmailValido(tab_correos.getValor(i,"DETALLE_GTCOR"))){
							utilitario.agregarMensajeInfo("No se puede Guardar el correo", "El correo Invalido");
							return;
						}	
					}

					tab_correos.guardar();

					tab_discapacidad.guardar();
					tab_archivo_empleado.guardar();
					guardarPantalla();
					aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, APELLIDO_PATERNO_GTEMP ||' '|| APELLIDO_MATERNO_GTEMP ||' '|| PRIMER_NOMBRE_GTEMP ||' '|| SEGUNDO_NOMBRE_GTEMP as nombres from GTH_EMPLEADO");

					//					aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP from GTH_EMPLEADO");
					aut_empleado.setValor(tab_empleado.getValor("IDE_GTEMP"));
					utilitario.addUpdate("aut_empleado");
				}
			}
		} else if (str_opcion.equals("1")) {//item datos conyuge
			if (validarDocumentoIdentidad(tab_conyuge.getValor("IDE_GTTDI"),tab_conyuge.getValor("DOCUMENTO_IDENTIDAD_GTCON"))){
				if (tab_conyuge.guardar()){
					for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
						if (tab_telefonos.isFilaInsertada(i)){
							tab_telefonos.setValor(i,"IDE_GTCON", tab_conyuge.getValor("IDE_GTCON"));
						}
					}
					for (int i = 0; i < tab_union_libre.getTotalFilas(); i++) {
						if (tab_union_libre.isFilaInsertada(i)){
							tab_union_libre.setValor(i,"IDE_GTCON", tab_conyuge.getValor("IDE_GTCON"));
						}
					}
					for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
						if (!utilitario.isEnteroPositivo(tab_telefonos.getValor(i,"numero_telefono_gttel"))){
							utilitario.agregarMensajeInfo("No se puede Guardar el telefono", "El numero es invalido");
							return ;
						}	
					}					

					tab_telefonos.guardar();
					tab_union_libre.guardar();
					guardarPantalla();
				}
			}
		}else if (str_opcion.equals("2")) {
			if (tab_empleado_departamento.guardar()){
				for (int i = 0; i < tab_deta_empleado_depar.getTotalFilas(); i++) {
					if (tab_deta_empleado_depar.isFilaInsertada(i)){
						tab_deta_empleado_depar.setValor(i,"IDE_GEEDP", tab_empleado_departamento.getValor("IDE_GEEDP"));
					}
				}
				tab_deta_empleado_depar.guardar();
				guardarPantalla();
			}
		}else if (str_opcion.equals("3")) {
			for (int i = 0; i < tab_documentacion.getTotalFilas(); i++) {
				if(tab_documentacion.getValor(i,"fecha_afiliacion_gtdce")!=null && !tab_documentacion.getValor(i,"fecha_afiliacion_gtdce").isEmpty()){
					if(utilitario.isFechaMayor(utilitario.getFecha(tab_documentacion.getValor(i,"fecha_afiliacion_gtdce")),utilitario.getFecha(utilitario.getFechaActual()))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de afiliacion del empleado no puede ser mayor que la fecha de actual");
						return;						
					}
				}	
			}

			tab_documentacion.guardar();
			guardarPantalla();
		}else if (str_opcion.equals("4")) {
			if (tab_persona_emergencia.guardar()){
				for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
					if (tab_telefonos.isFilaInsertada(i)){
						tab_telefonos.setValor(i,"IDE_GTPEE", tab_persona_emergencia.getValor("IDE_GTPEE"));
					}
				}
				for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
					if (!utilitario.isEnteroPositivo(tab_telefonos.getValor(i,"numero_telefono_gttel"))){
						utilitario.agregarMensajeInfo("No se puede Guardar el telefono", "El numero es invalido");
						return ;
					}	
				}
				tab_telefonos.guardar();
				for (int i = 0; i < tab_direccion.getTotalFilas(); i++) {
					if (tab_direccion.isFilaInsertada(i)){
						tab_direccion.setValor(i,"IDE_GTPEE", tab_persona_emergencia.getValor("IDE_GTPEE"));
						tab_direccion.setValor(i,"IDE_GTEMP", "");
					}
				}
				tab_direccion.guardar();
				guardarPantalla();
			}//PARTIDA ANTICIPO
		}else if (str_opcion.equals("28")) {
			tab_cuenta_anticipo.guardar();
			guardarPantalla();

		}else if (str_opcion.equals("5")) {
			if(tab_cargas_familiares.getValor("IDE_GTTDI")==null || tab_cargas_familiares.getValor("IDE_GTTDI").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar un tipo de documento");
				return;
			}			
			for (int i = 0; i < tab_cargas_familiares.getTotalFilas(); i++) {
				if (!validarDocumentoIdentidad(tab_cargas_familiares.getValor(i,"IDE_GTTDI"),tab_cargas_familiares.getValor(i,"DOCUMENTO_IDENTIDAD_GTCAF"))){
					return;
				}
			}

			for (int i = 0; i < tab_cargas_familiares.getTotalFilas(); i++) {
				if(tab_cargas_familiares.getValor(i,"fecha_nacimiento_gtcaf")!=null && !tab_cargas_familiares.getValor(i,"fecha_nacimiento_gtcaf").isEmpty()){
					if(utilitario.isFechaMayor(utilitario.getFecha(tab_cargas_familiares.getValor(i,"fecha_nacimiento_gtcaf")),utilitario.getFecha(utilitario.getFechaActual()))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de nacimiento no puede ser mayor que la fecha actual");
						return;						
					}
				}	
			}

			if (tab_cargas_familiares.guardar()){
				guardarPantalla();
			}


		}else if (str_opcion.equals("6")) {
			for (int i = 0; i < tab_familiar.getTotalFilas(); i++) {
				if (!validarDocumentoIdentidad(tab_familiar.getValor(i,"IDE_GTTDI"),tab_familiar.getValor(i,"DOCUMENTO_IDENTIDAD_GTFAM"))){
					return;
				}
			}
			for (int i = 0; i < tab_familiar.getTotalFilas(); i++) {
				if(tab_familiar.getValor(i,"fecha_nacimiento_gtfam")!=null && !tab_familiar.getValor(i,"fecha_nacimiento_gtfam").isEmpty()){
					if(utilitario.isFechaMayor(utilitario.getFecha(tab_familiar.getValor(i,"fecha_nacimiento_gtfam")),utilitario.getFecha(utilitario.getFechaActual()))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de nacimiento no puede ser mayor que la fecha de actual");
						return;						
					}
				}	
			}
			if (tab_familiar.guardar()){
				guardarPantalla();
			}

		}
		//		else if (str_opcion.equals("8")) {
		//			tab_direccion.guardar();
		//			guardarPantalla();
		//		}else if (str_opcion.equals("9")) {
		//			tab_telefonos.guardar();
		//			guardarPantalla();
		//		}else if (str_opcion.equals("10")) {
		//			tab_correos.guardar();
		//			guardarPantalla();
		//		}
		else if (str_opcion.equals("7")){
			for (int i = 0; i < tab_seguro_vida.getTotalFilas(); i++) {
				if (tab_seguro_vida.getValor(i,"fecha_vigencia_inicial_gtsev")!=null && !tab_seguro_vida.getValor(i,"fecha_vigencia_inicial_gtsev").isEmpty()) {
					if(utilitario.isFechaMayor(utilitario.getFecha(tab_seguro_vida.getValor(i,"fecha_vigencia_inicial_gtsev")),utilitario.getFecha(utilitario.getFechaActual()))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de vigencia inicial no puede ser mayor que la fecha actual");
						return;
					}							
					if(tab_seguro_vida.getValor(i,"fecha_vigencia_final_gtsev")!=null && !tab_seguro_vida.getValor(i,"fecha_vigencia_final_gtsev").isEmpty()){
						if(utilitario.isFechaMayor(utilitario.getFecha(tab_seguro_vida.getValor(i,"fecha_vigencia_inicial_gtsev")),utilitario.getFecha(tab_seguro_vida.getValor(i,"fecha_vigencia_final_gtsev")))){
							utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de vigencia inicial no puede ser mayor que la fecha de vigencia final");
							return;
						}		
					}			


					if(tab_seguro_vida.getValor(i,"fecha_vigencia_final_gtsev")==null || tab_seguro_vida.getValor(i,"fecha_vigencia_final_gtsev").isEmpty()){
						utilitario.agregarMensajeInfo("No se puede guardar", "Ingrese la fecha de vigencia final");
						return;
					}					
				}

				if(tab_seguro_vida.getValor(i,"fecha_vigencia_final_gtsev")!=null && !tab_seguro_vida.getValor(i,"fecha_vigencia_final_gtsev").isEmpty()){
					if(tab_seguro_vida.getValor(i,"fecha_vigencia_inicial_gtsev")==null || tab_seguro_vida.getValor(i,"fecha_vigencia_inicial_gtsev").isEmpty()){
						utilitario.agregarMensajeInfo("No se puede guardar", "Ingrese la fecha de vigencia inicial");
						return;
					}					
				}				
			}			

			for (int i = 0; i < tab_seguro_vida.getTotalFilas(); i++) {
				if(tab_seguro_vida.getValor("monto_seguro_gtsev")!=null && !tab_seguro_vida.getValor("monto_seguro_gtsev").isEmpty()){
					if(!utilitario.isNumeroPositivo(tab_seguro_vida.getValor("monto_seguro_gtsev"))){
						utilitario.agregarMensajeInfo("No se puede Guardar el monto", "El monto es invalido");
						return;
					}	
				}
			}

			if (tab_seguro_vida.guardar()){
				for (int i = 0; i < tab_beneficiario_seguro.getTotalFilas(); i++) {
					if(tab_beneficiario_seguro.getValor(i, "porcentaje_seguro_gtbes")!=null && !tab_beneficiario_seguro.getValor(i, "porcentaje_seguro_gtbes").isEmpty()){
						if(!utilitario.isNumeroPositivo(tab_beneficiario_seguro.getValor(i, "porcentaje_seguro_gtbes"))){
							utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje de beneficiario del seguro es invalido");
							return;
						}
						double dou_porcentaje=Double.parseDouble(tab_beneficiario_seguro.getValor(i, "porcentaje_seguro_gtbes"));
						if(dou_porcentaje>100){
							utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje de beneficiario  esta fuera del rango de 1 a 100");
							return;
						}
					}
				}

				for (int i = 0; i < tab_beneficiario_seguro.getTotalFilas(); i++) {
					if(tab_beneficiario_seguro.getValor(i, "valor_seguro_gtbes")!=null && !tab_beneficiario_seguro.getValor(i, "valor_seguro_gtbes").isEmpty()){
						if(!utilitario.isNumeroPositivo(tab_beneficiario_seguro.getValor(i, "valor_seguro_gtbes"))){
							utilitario.agregarMensajeInfo("No se puede guardar", "El valor seguro es invalido");
							return;
						}
						double dou_valor_seguro=Double.parseDouble(tab_beneficiario_seguro.getValor(i, "valor_seguro_gtbes"));
						if(dou_valor_seguro==0){
							utilitario.agregarMensajeInfo("No se puede guardar", "El valor seguro debe ser mayor a cero");
							return;
						}
					}
				}				

				if (tab_beneficiario_seguro.isFilaInsertada()){
					tab_beneficiario_seguro.setValor("IDE_GTSEV", tab_seguro_vida.getValor("IDE_GTSEV"));
				}				
				tab_beneficiario_seguro.guardar();
				guardarPantalla();

				tab_seguro_vida.setCondicion("IDE_GTEMP =" + aut_empleado.getValor());
				tab_seguro_vida.ejecutarSql();
				tab_beneficiario_seguro.setCondicion("IDE_GTSEV="+tab_seguro_vida.getValor("IDE_GTSEV"));
				tab_beneficiario_seguro.ejecutarSql();
				tab_beneficiario_seguro.sumarColumnas();
				utilitario.addUpdate("tab_beneficiario_seguro");

			}
		}else if (str_opcion.equals("8")){
			//if(tab_registro_militar.getValor("ANIO_INGRESO_GTREM")!=null && !tab_registro_militar.getValor("ANIO_INGRESO_GTREM").isEmpty()){
			//if(!utilitario.isEnteroPositivo(tab_registro_militar.getValor("ANIO_INGRESO_GTREM"))){
			//utilitario.agregarMensajeInfo("No se puede guardar", "El Total de Años es invalido");
			//return;	
			//}								
			//}			
			if(tab_registro_militar.getValor("ANIO_TOTAL_GTREM")!=null && !tab_registro_militar.getValor("ANIO_TOTAL_GTREM").isEmpty()){
				if(!utilitario.isEnteroPositivo(tab_registro_militar.getValor("ANIO_TOTAL_GTREM"))){
					utilitario.agregarMensajeInfo("No se puede guardar", "El Año de ingreso es invalido");
					return;
				}

				int int_total_anios=0;
				int_total_anios=Integer.parseInt(tab_registro_militar.getValor("ANIO_TOTAL_GTREM"));				
				if(int_total_anios>50){
					utilitario.agregarMensajeInfo("No se puede guardar", "El total de Años esta fuera del rango 1 a 30");					
					return;
				}			
			}
			if (tab_registro_militar.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("9")){
			if (tab_hobbies.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("10")){
			if(tab_educacion.getValor("ANIO_GTEDE")!=null && !tab_educacion.getValor("ANIO_GTEDE").isEmpty()){
				if(!utilitario.isEnteroPositivo(tab_educacion.getValor("ANIO_GTEDE"))){
					utilitario.agregarMensajeInfo("No se puede guardar", "El Tiempo de duración (Año) es invalido");
					return;
				}				
				int int_total_anios=0;
				int_total_anios=Integer.parseInt(tab_educacion.getValor("ANIO_GTEDE"));				
				if(int_total_anios>20){
					utilitario.agregarMensajeInfo("No se puede guardar", "El tiempo de duración de Años esta fuera del rango 1 a 20");					
					return;
				}			
			}
			if (tab_educacion.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("11")){
			for (int i = 0; i < tab_idiomas.getTotalFilas(); i++) {
				try {
					if (Double.parseDouble(tab_idiomas.getValor(i,"POCENTAJE_LEE_GTIDE"))>100 ){
						utilitario.agregarMensajeInfo("No se puede guardar los idiomas", "el porcentaje que lee no puede superar el 100%");
						return;
					}
					if (Double.parseDouble(tab_idiomas.getValor(i,"POCENTAJE_LEE_GTIDE"))<0 ){
						utilitario.agregarMensajeInfo("No se puede guardar los idiomas", "el porcentaje que lee no puede ser menor a cero");
						return;
					}
					if (Double.parseDouble(tab_idiomas.getValor(i,"PORCENTAJE_ENTIENDE_GTIDE"))>100 ){
						utilitario.agregarMensajeInfo("No se puede guardar los idiomas", "el porcentaje que entiende no puede superar el 100%");
						return;
					}
					if (Double.parseDouble(tab_idiomas.getValor(i,"PORCENTAJE_ENTIENDE_GTIDE"))<0 ){
						utilitario.agregarMensajeInfo("No se puede guardar los idiomas", "el porcentaje que entiende no puede ser menor a cero");
						return;
					}
					if (Double.parseDouble(tab_idiomas.getValor(i,"PORCENTAJE_ESCRIBE_GTIDE"))>100 ){
						utilitario.agregarMensajeInfo("No se puede guardar los idiomas", "el porcentaje que escribe no puede superar el 100%");
						return;
					}
					if (Double.parseDouble(tab_idiomas.getValor(i,"PORCENTAJE_ESCRIBE_GTIDE"))<0 ){
						utilitario.agregarMensajeInfo("No se puede guardar los idiomas", "el porcentaje que escribe no puede ser menor a cero");
						return;
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			for (int i = 0; i < tab_idiomas.getTotalFilas(); i++) {
				if(tab_idiomas.getValor(i, "POCENTAJE_LEE_GTIDE")!=null && !tab_idiomas.getValor(i, "POCENTAJE_LEE_GTIDE").isEmpty()){
					if(!utilitario.isNumeroPositivo(tab_idiomas.getValor(i, "POCENTAJE_LEE_GTIDE"))){
						utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje lee es invalido");
						return;
					}					
				}
				if(tab_idiomas.getValor(i, "PORCENTAJE_ESCRIBE_GTIDE")!=null && !tab_idiomas.getValor(i, "PORCENTAJE_ESCRIBE_GTIDE").isEmpty()){
					if(!utilitario.isNumeroPositivo(tab_idiomas.getValor(i, "PORCENTAJE_ESCRIBE_GTIDE"))){
						utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje escribe es invalido");
						return;
					}					
				}
				if(tab_idiomas.getValor(i, "PORCENTAJE_ENTIENDE_GTIDE")!=null && !tab_idiomas.getValor(i, "PORCENTAJE_ENTIENDE_GTIDE").isEmpty()){
					if(!utilitario.isNumeroPositivo(tab_idiomas.getValor(i, "PORCENTAJE_ENTIENDE_GTIDE"))){
						utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje entiende es invalido");
						return;
					}					
				}
				if(tab_idiomas.getValor(i, "ANIO_GTIDE")!=null && !tab_idiomas.getValor(i, "ANIO_GTIDE").isEmpty()){
					if(!utilitario.isNumeroPositivo(tab_idiomas.getValor(i, "ANIO_GTIDE"))){
						utilitario.agregarMensajeInfo("No se puede guardar", "El Año es invalido");
						return;
					}					
				}
				if(tab_idiomas.getValor(i, "DURACION_GTIDE")!=null && !tab_idiomas.getValor(i, "DURACION_GTIDE").isEmpty()){
					if(!utilitario.isNumeroPositivo(tab_idiomas.getValor(i, "DURACION_GTIDE"))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La duracion es invalido");
						return;
					}					
				}
				if(tab_idiomas.getValor(i, "NIVEL_APROBADO_GTIDE")!=null && !tab_idiomas.getValor(i, "NIVEL_APROBADO_GTIDE").isEmpty()){
					if(!utilitario.isNumeroPositivo(tab_idiomas.getValor(i, "NIVEL_APROBADO_GTIDE"))){
						utilitario.agregarMensajeInfo("No se puede guardar", "El Nivel es invalido");
						return;
					}					
				}
			}

			if (tab_idiomas.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("12")){




			// valida q la fecha de capacitacion no sea mayor a la fecha actual
			if(tab_capacitacion.getValor("FECHA_GTCEM")!=null && !tab_capacitacion.getValor("FECHA_GTCEM").isEmpty()){
				if(utilitario.isFechaMayor(utilitario.getFecha(tab_capacitacion.getValor("FECHA_GTCEM")),utilitario.getFecha(utilitario.getFechaActual()))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de capacitacion no puede ser mayor que la fecha actual");
					return;							
				}
			}

			if(tab_capacitacion.getValor("DURACION_GTCEM")!=null && !tab_capacitacion.getValor("DURACION_GTCEM").isEmpty()){
				if(!utilitario.isNumeroPositivo(tab_capacitacion.getValor("DURACION_GTCEM"))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La duracion es invalido");
					return;
				}
			}
			if (tab_capacitacion.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("13")){


			if (tab_experiencia_docente.getValor("FECHA_INICIO_GTXDE")!=null && !tab_experiencia_docente.getValor("FECHA_INICIO_GTXDE").isEmpty()) {
				if(utilitario.isFechaMayor(utilitario.getFecha(tab_experiencia_docente.getValor("FECHA_INICIO_GTXDE")),utilitario.getFecha(utilitario.getFechaActual()))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de ingreso no puede ser mayor que la fecha actual");
					return;
				}							
			}

			if (tab_experiencia_docente.getValor("FECHA_SALIDA_GTXDE")!=null && !tab_experiencia_docente.getValor("FECHA_SALIDA_GTXDE").isEmpty()) {
				if(utilitario.isFechaMayor(utilitario.getFecha(tab_experiencia_docente.getValor("FECHA_SALIDA_GTXDE")),utilitario.getFecha(utilitario.getFechaActual()))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de salida no puede ser mayor que la fecha actual");
					return;
				}							
			}

			if(utilitario.isFechaMayor(utilitario.getFecha(tab_experiencia_docente.getValor("FECHA_INICIO_GTXDE")),utilitario.getFecha(tab_experiencia_docente.getValor("FECHA_SALIDA_GTXDE")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de ingreso no puede ser mayor que la fecha de salida");
				return;
			}		



			if (tab_experiencia_docente.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("14")){
			
			if (tab_experiencia_laboral.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("15")) {
			if (tab_amigos.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("16")){


			if (tab_situacion_economica.getValor("SUELDO_CONYUGE_GTSEE")==null || tab_situacion_economica.getValor("SUELDO_CONYUGE_GTSEE").isEmpty()) {
				utilitario.agregarMensajeInfo("No se puede Guardar el campo Sueldo Conyuge", "No Contiene Datos");
				return;
			}
			if (!utilitario.isNumeroPositivo(tab_situacion_economica.getValor("SUELDO_CONYUGE_GTSEE"))) {
				utilitario.agregarMensajeInfo("No se puede Guardar el campo Sueldo Conyuge", "Recuerde ingresar un valor valido");
				return ;
			}
			if (tab_situacion_economica.getValor("OTRO_INGRESO_GTSEE")==null || tab_situacion_economica.getValor("OTRO_INGRESO_GTSEE").isEmpty()) {
				utilitario.agregarMensajeInfo("No se puede Guardar el campo Otro Ingreso", "No Contiene Datos");
				return;
			}


			if (tab_situacion_economica.getValor("MONTO_MENSUAL_GTSEE")==null || tab_situacion_economica.getValor("MONTO_MENSUAL_GTSEE").isEmpty()) {
				utilitario.agregarMensajeInfo("No se puede Guardar el campo Monto Mensual", "No Contiene Datos");
				return;
			}
			if (!utilitario.isNumeroPositivo(tab_situacion_economica.getValor("MONTO_MENSUAL_GTSEE"))) {
				utilitario.agregarMensajeInfo("No se puede Guardar el campo Monto Mensual", "Recuerde ingresar un valor valido");
				return ;
			}

			if (tab_situacion_economica.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("17")){



			if (tab_negocio_empl.getValor("FECHA_VIGENCIA_RUC_GTNEE")!=null && !tab_negocio_empl.getValor("FECHA_VIGENCIA_RUC_GTNEE").isEmpty()) {
				if(utilitario.isFechaMayor(utilitario.getFecha(tab_negocio_empl.getValor("FECHA_VIGENCIA_RUC_GTNEE")),utilitario.getFecha(utilitario.getFechaActual()))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de inicio de RUC no puede ser mayor que la fecha actual");
					return;
				}							
			}


			if (tab_negocio_empl.getValor("RUC_GTNEE")!=null && !tab_negocio_empl.getValor("RUC_GTNEE").isEmpty()){
				if (!utilitario.isEnteroPositivoyCero(tab_negocio_empl.getValor("RUC_GTNEE"))) {
					utilitario.agregarMensajeInfo("No se puede Guardar el campo RUC", "Solo se admiten numeros enteros");
					return ;
				}
			}

			if (tab_negocio_empl.getValor("TOTAL_VENTA_GTNEE")!=null && !tab_negocio_empl.getValor("TOTAL_VENTA_GTNEE").isEmpty()){
				if (!utilitario.isEnteroPositivoyCero(tab_negocio_empl.getValor("TOTAL_VENTA_GTNEE"))) {
					utilitario.agregarMensajeInfo("No se puede Guardar el campo total venta", "Solo se admiten enteros positivos");
					return ;
				}
			}

			if (tab_negocio_empl.getValor("TOTAL_GASTO_GTNEE")!=null && !tab_negocio_empl.getValor("TOTAL_GASTO_GTNEE").isEmpty()){
				if (!utilitario.isEnteroPositivoyCero(tab_negocio_empl.getValor("TOTAL_GASTO_GTNEE"))) {
					utilitario.agregarMensajeInfo("No se puede Guardar el campo total gasto", "Solo se admiten enteros positivos");
					return ;
				}
			}


			if (tab_negocio_empl.guardar()){
				for (int i = 0; i < tab_paticipantes_negocio.getTotalFilas(); i++) {
					if (tab_paticipantes_negocio.isFilaInsertada(i)){
						tab_paticipantes_negocio.setValor(i,"IDE_GTNEE", tab_negocio_empl.getValor("IDE_GTNEE"));
					}
				}
				for (int i = 0; i < tab_direccion.getTotalFilas(); i++) {
					//					if (tab_direccion.isFilaInsertada(i)){
					tab_direccion.setValor(i,"IDE_GTNEE", tab_negocio_empl.getValor("IDE_GTNEE"));
					//					}
				}
				for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
					//					if (tab_telefonos.isFilaInsertada(i)){
					tab_telefonos.setValor(i,"IDE_GTNEE", tab_negocio_empl.getValor("IDE_GTNEE"));
					//					}
				}
				for (int i = 0; i < tab_archivo_empleado.getTotalFilas(); i++) {
					//					if (tab_archivo_empleado.isFilaInsertada(i)){
					tab_archivo_empleado.setValor(i,"IDE_GTNEE", tab_negocio_empl.getValor("IDE_GTNEE"));
					//					}
				}

				tab_paticipantes_negocio.guardar();
				tab_direccion.guardar();
				for (int i = 0; i < tab_telefonos.getTotalFilas(); i++) {
					if (!utilitario.isEnteroPositivo(tab_telefonos.getValor(i,"numero_telefono_gttel"))){
						utilitario.agregarMensajeInfo("No se puede Guardar el telefono", "El numero es invalido");
						return ;
					}	
				}
				tab_telefonos.guardar();
				tab_archivo_empleado.guardar();
				guardarPantalla();
			}
		}else if (str_opcion.equals("18")){

			for (int j = 0; j < tab_terreno.getTotalFilas(); j++) {
				if (tab_terreno.getValor(j,"AVALUO_GTTEE")!=null && !tab_terreno.getValor(j,"AVALUO_GTTEE").isEmpty()){
					if (!utilitario.isNumeroPositivo(tab_terreno.getValor(j,"AVALUO_GTTEE"))){
						utilitario.agregarMensajeInfo("No se puede Guardar", "El campo AVALUO es invalido, solo positivos");
						return ;
					}	
				}
			}

			if (tab_terreno.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("19")){

			if (tab_casa.getValor("AVALUO_GTCSE")!=null && !tab_casa.getValor("AVALUO_GTCSE").isEmpty()){
				if (!utilitario.isNumeroPositivo(tab_casa.getValor("AVALUO_GTCSE"))){
					utilitario.agregarMensajeInfo("No se puede Guardar", "El campo AVALUO es invalido, solo positivos");
					return ;
				}	
			}

			if (tab_casa.getValor("MONTO_ARRIENDO_GTCSE")!=null && !tab_casa.getValor("MONTO_ARRIENDO_GTCSE").isEmpty()){
				if (!utilitario.isNumeroPositivo(tab_casa.getValor("MONTO_ARRIENDO_GTCSE"))){
					utilitario.agregarMensajeInfo("No se puede Guardar", "El campo MONTO ARRIENDO es invalido, solo positivos");
					return ;
				}	
			}

			if (tab_casa.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("20")){


			if (tab_vehiculo.getValor("AVALUO_GTVEE")!=null && !tab_vehiculo.getValor("AVALUO_GTVEE").isEmpty()){
				if (!utilitario.isNumeroPositivo(tab_vehiculo.getValor("AVALUO_GTVEE"))){
					utilitario.agregarMensajeInfo("No se puede Guardar", "El campo AVALUO es invalido, solo positivos");
					return ;
				}	
			}

			if (tab_vehiculo.getValor("MONTO_SEGURO_GTVEE")!=null && !tab_vehiculo.getValor("MONTO_SEGURO_GTVEE").isEmpty()){
				if (!utilitario.isNumeroPositivo(tab_vehiculo.getValor("MONTO_SEGURO_GTVEE"))){
					utilitario.agregarMensajeInfo("No se puede Guardar", "El campo MONTO SEGURO es invalido, solo positivos");
					return ;
				}	
			}


			if (tab_vehiculo.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("21")){
			if (tab_situacion_financiera.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("22")){



			if (tab_cuenta_bancaria.guardar()){
				guardarPantalla();

			}
		}else if (str_opcion.equals("23")){
			if (tab_inversion.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("24")){

			for (int i = 0; i < tab_endeudamiento.getTotalFilas(); i++) {

				if (tab_endeudamiento.getValor(i,"PLAZO_GTEEM")==null || tab_endeudamiento.getValor(i,"PLAZO_GTEEM").isEmpty()) {
					utilitario.agregarMensajeInfo("No se puede Guardar el campo Plazo", "No Contiene Datos");
					return;
				}


				if (!utilitario.isNumeroPositivo(tab_endeudamiento.getValor(i,"PLAZO_GTEEM"))) {
					utilitario.agregarMensajeInfo("No se puede Guardar el Plazo", "Recuerde ingresar un valor valido");
					return ;
				}	

				if (tab_endeudamiento.getValor(i,"MONTO_GTEEM")==null || tab_endeudamiento.getValor(i,"MONTO_GTEEM").isEmpty()) {
					utilitario.agregarMensajeInfo("No se puede Guardar el campo Monto", "No Contiene Datos");
					return;
				}
				if (!utilitario.isNumeroPositivo(tab_endeudamiento.getValor(i,"MONTO_GTEEM"))) {
					utilitario.agregarMensajeInfo("No se puede Guardar el Monto", "Recuerde ingresar un valor valido");
					return ;

				}

				if (tab_endeudamiento.getValor(i,"CUOTA_MENSUAL_GTEEM")==null || tab_endeudamiento.getValor(i,"CUOTA_MENSUAL_GTEEM").isEmpty()) {
					utilitario.agregarMensajeInfo("No se puede Guardar el campo Couta Mensual", "No Contiene Datos");
					return;
				}
				if (!utilitario.isNumeroPositivo(tab_endeudamiento.getValor(i,"CUOTA_MENSUAL_GTEEM"))) {
					utilitario.agregarMensajeInfo("No se puede Guardar el Cuota Mensual", "Recuerde ingresar un valor valido");
					return ;

				}		


			}


			if (tab_endeudamiento.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("25")){

			if (tab_tarjeta_credito.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("26")){

			for (int i = 0; i < tab_membresias.getTotalFilas(); i++) {
				// valida q la fecha de capacitacion no sea mayor a la fecha actual
				if(tab_membresias.getValor(i,"FECHA_MEMBRESIA_GTMEE")!=null && !tab_membresias.getValor(i,"FECHA_MEMBRESIA_GTMEE").isEmpty()){
					if(utilitario.isFechaMayor(utilitario.getFecha(tab_membresias.getValor(i,"FECHA_MEMBRESIA_GTMEE")),utilitario.getFecha(utilitario.getFechaActual()))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de membresia no puede ser mayor que la fecha actual");
						return;							
					}
				}
			}


			if (tab_membresias.guardar()){
				guardarPantalla();
			}
		}else if (str_opcion.equals("29")){

			if (validarGastosPersonalesDeducibles()){

				if (tab_sri_gastos_deducible.guardar()){
					guardarPantalla();
					tab_sri_gastos_deducible.getFilas().get(tab_sri_gastos_deducible.getFilaActual()).setLectura(true);
					utilitario.addUpdate("tab_sri_gastos_deducible");
				}
			}
		}
	}


	public boolean validarGastosPersonalesDeducibles(){

		String ide_srimr=ser_nomina.getSriImpuestoRenta(utilitario.getFechaActual()).getValor("IDE_SRIMR");
		TablaGenerica tab_deducibles=ser_nomina.getSriDeducibles(ide_srimr);

		for (int i = 0; i < tab_sri_gastos_deducible.getTotalFilas(); i++) {
			for (int j = 0; j < tab_deducibles.getTotalFilas(); j++) {
				if (tab_sri_gastos_deducible.getValor(i,"IDE_SRDED").equalsIgnoreCase(tab_deducibles.getValor(j, "IDE_SRDED"))){
					double dou_gasto_ded=0;
					double dou_gasto_ded_max=0; 
					try {
						dou_gasto_ded=Double.parseDouble(tab_sri_gastos_deducible.getValor(i,"VALOR_DEDUCIBLE_SRDEE"));
					
					//System.out.println(" deducible gastos "+dou_gasto_ded);
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						dou_gasto_ded_max=Double.parseDouble(tab_deducibles.getValor(j, "valor_maximo"));
						//System.out.println(" deducible gastos maximo "+dou_gasto_ded_max);

					} catch (Exception e) {
						// TODO: handle exception
					}

					if (dou_gasto_ded>dou_gasto_ded_max){
						utilitario.agregarMensajeInfo("No se puede guardar los gastos deducibles", "El deducible "+tab_deducibles.getValor(j, "detalle_srded")+" sobrepasa el valor maximo deducible "+dou_gasto_ded_max);
						return false;
					}else{
						break;
					}
				}
			}
		}


		double dou_porcentaje_aplica=0;
		try {
			dou_porcentaje_aplica=Double.parseDouble(utilitario.getVariable("p_porcentaje_tot_ing_grab"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		double dou_tot_ing_gravados=ser_nomina.getTotalIngresosGravados(ide_srimr, aut_empleado.getValor());
		double dou_tot_gastos_personales=tab_sri_gastos_deducible.getSumaColumna("VALOR_DEDUCIBLE_SRDEE");
		double dou_porcentaje_total_ingresos_gravados=(dou_tot_ing_gravados*dou_porcentaje_aplica)/100;
		double dou_veces_fraccion_basica_desgravada=ser_nomina.getValorVecesFraccionBasicaDesgravada(ide_srimr);

		System.out.println("tot ing gravados "+dou_tot_ing_gravados);
		System.out.println("tot gastos personales "+dou_tot_gastos_personales);
		System.out.println("50% tot ing grav "+dou_porcentaje_total_ingresos_gravados);
		System.out.println("1.3 veces fracion basica desgravada "+dou_veces_fraccion_basica_desgravada);

		if (dou_tot_gastos_personales>dou_veces_fraccion_basica_desgravada){
			utilitario.agregarMensajeInfo("No se puede guardar los gastos deducibles", "El total de gastos deducibles "+dou_tot_gastos_personales+" sobrepasa el valor maximo deducible "+dou_veces_fraccion_basica_desgravada);
			return false;
		}

		if (dou_tot_gastos_personales>dou_porcentaje_total_ingresos_gravados){
			utilitario.agregarMensajeInfo("No se puede guardar los gastos deducibles", "El total de gastos deducibles "+dou_tot_gastos_personales+" sobrepasa al porcentaje de total ingresos gravados "+dou_porcentaje_total_ingresos_gravados);
			return false;
		}
		return true;

	}

	private boolean validarIngresoCuentasBancarias(){
		int num_cuentas_activas=0;
		int num_cuentas_acreditacion=0;
		for (int i = 0; i < tab_cuenta_bancaria.getTotalFilas(); i++) {
			if (tab_cuenta_bancaria.getValor(i,"ACTIVO_GTCBE").equals("true")){
				num_cuentas_activas++;
			}
			if (tab_cuenta_bancaria.getValor(i,"ACREDITACION_GTCBE").equals("true")){
				num_cuentas_acreditacion++;
			}
		}
		if (num_cuentas_activas==0){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe existir al menos una cuenta bancaria activa");
			return false;
		}
		if (num_cuentas_acreditacion==0){
			utilitario.agregarMensajeInfo("No se puede guardar", "No existe cuenta de acreditacion");
			return false;
		}

		if (num_cuentas_acreditacion>1){
			utilitario.agregarMensajeInfo("No se puede guardar", "Debe existir solo una cuenta de acreditacion");
			return false;
		}
		return true;
	}
	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		System.out.println("entre metodo eleiminar");

		if (str_opcion.equals("0")) {
			if (tab_empleado.isFocus()){
				if (tab_empleado.eliminar()){
					tab_direccion.limpiar();
					tab_telefonos.limpiar();
					tab_correos.limpiar();
					tab_discapacidad.limpiar();
					tab_archivo_empleado.limpiar();
				}
			}else if (tab_telefonos.isFocus()){
				tab_telefonos.eliminar();
			}else if (tab_direccion.isFocus()){
				tab_direccion.eliminar();
			}else if (tab_correos.isFocus()){
				tab_correos.eliminar();
			}
			else if (tab_discapacidad.isFocus()){
				tab_discapacidad.eliminar();
			}
			else if (tab_archivo_empleado.isFocus()){
				tab_archivo_empleado.eliminar();
			}
		} 

		else if (str_opcion.equals("1")) {
			if (tab_conyuge.isFocus()){
				tab_conyuge.eliminar();
				filtrarTelefonosConyugue();
				filtrarUnionLibreConyugue();
			}else if (tab_telefonos.isFocus()){
				tab_telefonos.eliminar();
			}else if (tab_union_libre.isFocus()){
				tab_union_libre.eliminar();
			}
		}
		else if (str_opcion.equals("2")) {
			if(tab_empleado_departamento.isFocus()){
				if (tab_empleado_departamento.eliminar()){
					actualizarCombosDepartamentoEmpleado();
				}
			}else if (tab_deta_empleado_depar.isFocus()){
				tab_deta_empleado_depar.eliminar();
			}							
		}
		else if (str_opcion.equals("3")) {
			tab_documentacion.eliminar();
		}
		else if(str_opcion.equals("28")){
			if(tab_cuenta_anticipo.isFocus()){
				tab_cuenta_anticipo.eliminar();
			}
		}
		else if (str_opcion.equals("4")) {
			if (tab_persona_emergencia.isFocus()){
				if (tab_telefonos.getTotalFilas()==0){
					tab_persona_emergencia.eliminar();
					filtrarTelefonoPersonaEmergencia();
					filtrarDireccionPersonaEmergencia();
				}else if(tab_telefonos.isFocus()){
					tab_telefonos.eliminar();
				}else if(tab_direccion.isFocus()){
					tab_direccion.eliminar();
				}
			}
		}

		else if (str_opcion.equals("5")) {
			tab_cargas_familiares.eliminar();
		}

		else if (str_opcion.equals("6")) {
			tab_familiar.eliminar();
		}
		//		else if (str_opcion.equals("8")) {
		//			tab_direccion.eliminar();
		//		}else if (str_opcion.equals("9")) {
		//			tab_telefonos.eliminar();
		//		}else if (str_opcion.equals("10")) {
		//			tab_correos.eliminar();
		//		}
		else if (str_opcion.equals("7")){
			if (tab_seguro_vida.isFocus()){
				tab_seguro_vida.eliminar();
				filtrarBeneficiariosSeguroVida();
			}else if (tab_beneficiario_seguro.isFocus()){
				tab_beneficiario_seguro.eliminar();
			}
		}else if (str_opcion.equals("8")){
			if (tab_registro_militar.isFocus()){
				tab_registro_militar.eliminar();
			}
		}else if (str_opcion.equals("9")){
			if (tab_hobbies.isFocus()){
				tab_hobbies.eliminar();
			}
		}else if (str_opcion.equals("10")){
			if (tab_educacion.isFocus()){
				tab_educacion.eliminar();
			}
		}else if (str_opcion.equals("11")){
			if (tab_idiomas.isFocus()){
				tab_idiomas.eliminar();
			}
		}else if (str_opcion.equals("12")){
			if (tab_capacitacion.isFocus()){
				tab_capacitacion.eliminar();
			}
		}

		else if (str_opcion.equals("14")){
			if(tab_experiencia_laboral.isFocus()){
				tab_experiencia_laboral.eliminar();
			}
		}else if (str_opcion.equals("15")) {
			tab_amigos.eliminar();
		}else if (str_opcion.equals("16")){
			if (tab_situacion_economica.isFocus()){
				tab_situacion_economica.eliminar();
			}
		}
		else if (str_opcion.equals("22")){ 

			System.out.println("emtere a eliminar cuenta bancaria");

			tab_cuenta_bancaria.eliminar();

		}

		else if (str_opcion.equals("29")){
			if (tab_sri_gastos_deducible.isFocus()){
				if (tab_sri_gastos_deducible.isFilaInsertada()){
					tab_sri_gastos_deducible.eliminar();
					tab_sri_gastos_deducible.sumarColumnas();
					utilitario.addUpdate("tab_sri_gastos_deducible");
				}
			}
		}


	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public Tabla getTab_empleado() {
		return tab_empleado;
	}

	public void setTab_empleado(Tabla tab_empleado) {
		this.tab_empleado = tab_empleado;
	}

	public Tabla getTab_conyuge() {
		return tab_conyuge;
	}

	public void setTab_conyuge(Tabla tab_conyuge) {
		this.tab_conyuge = tab_conyuge;
	}

	public Tabla getTab_union_libre() {
		return tab_union_libre;
	}

	public void setTab_union_libre(Tabla tab_union_libre) {
		this.tab_union_libre = tab_union_libre;
	}

	public Tabla getTab_persona_emergencia() {
		return tab_persona_emergencia;
	}

	public void setTab_persona_emergencia(Tabla tab_persona_emergencia) {
		this.tab_persona_emergencia = tab_persona_emergencia;
	}

	public Tabla getTab_familiar() {
		return tab_familiar;
	}

	public void setTab_familiar(Tabla tab_familiar) {
		this.tab_familiar = tab_familiar;
	}

	public Tabla getTab_cargas_familiares() {
		return tab_cargas_familiares;
	}

	public void setTab_cargas_familiares(Tabla tab_cargas_familiares) {
		this.tab_cargas_familiares = tab_cargas_familiares;
	}

	public Tabla getTab_telefonos() {
		return tab_telefonos;
	}

	public void setTab_telefonos(Tabla tab_telefonos) {
		this.tab_telefonos = tab_telefonos;
	}
	public SeleccionTabla getSet_actualizar_cuenta() {
		return set_actualizar_cuenta;
	}

	public void setSet_actualizar_cuenta(SeleccionTabla set_actualizar_cuenta) {
		this.set_actualizar_cuenta = set_actualizar_cuenta;
	}

	public Tabla getTab_correos() {
		return tab_correos;
	}

	public void setTab_correos(Tabla tab_correos) {
		this.tab_correos = tab_correos;
	}

	public Tabla getTab_seguro_vida() {
		return tab_seguro_vida;
	}

	public void setTab_seguro_vida(Tabla tab_seguro_vida) {
		this.tab_seguro_vida = tab_seguro_vida;
	}

	public Tabla getTab_beneficiario_seguro() {
		return tab_beneficiario_seguro;
	}

	public void setTab_beneficiario_seguro(Tabla tab_beneficiario_seguro) {
		this.tab_beneficiario_seguro = tab_beneficiario_seguro;
	}

	public Tabla getTab_registro_militar() {
		return tab_registro_militar;
	}

	public void setTab_registro_militar(Tabla tab_registro_militar) {
		this.tab_registro_militar = tab_registro_militar;
	}

	public Tabla getTab_educacion() {
		return tab_educacion;
	}

	public void setTab_educacion(Tabla tab_educacion) {
		this.tab_educacion = tab_educacion;
	}

	public Tabla getTab_idiomas() {
		return tab_idiomas;
	}

	public void setTab_idiomas(Tabla tab_idiomas) {
		this.tab_idiomas = tab_idiomas;
	}

	public Tabla getTab_experiencia_docente() {
		return tab_experiencia_docente;
	}

	public void setTab_experiencia_docente(Tabla tab_experiencia_docente) {
		this.tab_experiencia_docente = tab_experiencia_docente;
	}

	public Tabla getTab_capacitacion() {
		return tab_capacitacion;
	}

	public void setTab_capacitacion(Tabla tab_capacitacion) {
		this.tab_capacitacion = tab_capacitacion;
	}

	public Tabla getTab_amigos() {
		return tab_amigos;
	}

	public void setTab_amigos(Tabla tab_amigos) {
		this.tab_amigos = tab_amigos;
	}

	public Tabla getTab_experiencia_laboral() {
		return tab_experiencia_laboral;
	}

	public void setTab_experiencia_laboral(Tabla tab_experiencia_laboral) {
		this.tab_experiencia_laboral = tab_experiencia_laboral;
	}

	public Tabla getTab_situacion_economica() {
		return tab_situacion_economica;
	}

	public void setTab_situacion_economica(Tabla tab_situacion_economica) {
		this.tab_situacion_economica = tab_situacion_economica;
	}

	public Tabla getTab_negocio_empl() {
		return tab_negocio_empl;
	}

	public void setTab_negocio_empl(Tabla tab_negocio_empl) {
		this.tab_negocio_empl = tab_negocio_empl;
	}

	public Tabla getTab_paticipantes_negocio() {
		return tab_paticipantes_negocio;
	}

	public void setTab_paticipantes_negocio(Tabla tab_paticipantes_negocio) {
		this.tab_paticipantes_negocio = tab_paticipantes_negocio;
	}

	public Tabla getTab_documentacion() {
		return tab_documentacion;
	}

	public void setTab_documentacion(Tabla tab_documentacion) {
		this.tab_documentacion = tab_documentacion;
	}

	public Tabla getTab_cuenta_bancaria() {
		return tab_cuenta_bancaria;
	}

	public void setTab_cuenta_bancaria(Tabla tab_cuenta_bancaria) {
		this.tab_cuenta_bancaria = tab_cuenta_bancaria;
	}

	public Tabla getTab_situacion_financiera() {
		return tab_situacion_financiera;
	}

	public void setTab_situacion_financiera(Tabla tab_situacion_financiera) {
		this.tab_situacion_financiera = tab_situacion_financiera;
	}

	public Tabla getTab_inversion() {
		return tab_inversion;
	}

	public void setTab_inversion(Tabla tab_inversion) {
		this.tab_inversion = tab_inversion;
	}

	public Tabla getTab_endeudamiento() {
		return tab_endeudamiento;
	}

	public void setTab_endeudamiento(Tabla tab_endeudamiento) {
		this.tab_endeudamiento = tab_endeudamiento;
	}

	public Tabla getTab_tarjeta_credito() {
		return tab_tarjeta_credito;
	}

	public void setTab_tarjeta_credito(Tabla tab_tarjeta_credito) {
		this.tab_tarjeta_credito = tab_tarjeta_credito;
	}

	public Tabla getTab_empleado_departamento() {
		return tab_empleado_departamento;
	}

	public void setTab_empleado_departamento(Tabla tab_empleado_departamento) {
		this.tab_empleado_departamento = tab_empleado_departamento;
	}

	public Tabla getTab_direccion() {
		return tab_direccion;
	}

	public void setTab_direccion(Tabla tab_direccion) {
		this.tab_direccion = tab_direccion;
	}

	public Tabla getTab_terreno() {
		return tab_terreno;
	}

	public void setTab_terreno(Tabla tab_terreno) {
		this.tab_terreno = tab_terreno;
	}

	public Tabla getTab_casa() {
		return tab_casa;
	}

	public void setTab_casa(Tabla tab_casa) {
		this.tab_casa = tab_casa;
	}

	public Tabla getTab_vehiculo() {
		return tab_vehiculo;
	}

	public void setTab_vehiculo(Tabla tab_vehiculo) {
		this.tab_vehiculo = tab_vehiculo;
	}


	public Tabla getTab_archivo_empleado() {
		return tab_archivo_empleado;
	}

	public void setTab_archivo_empleado(Tabla tab_archivo_empleado) {
		this.tab_archivo_empleado = tab_archivo_empleado;
	}

	public Tabla getTab_hobbies() {
		return tab_hobbies;
	}

	public void setTab_hobbies(Tabla tab_hobbies) {
		this.tab_hobbies = tab_hobbies;
	}



	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}



	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
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


	public SeleccionTabla getSet_tipo_empleado() {
		return set_tipo_empleado;
	}


	public void setSet_tipo_empleado(SeleccionTabla set_tipo_empleado) {
		this.set_tipo_empleado = set_tipo_empleado;
	}


	public Tabla getTab_membresias() {
		return tab_membresias;
	}


	public void setTab_membresias(Tabla tab_membresias) {
		this.tab_membresias = tab_membresias;
	}


	public Tabla getTab_discapacidad() {
		return tab_discapacidad;
	}


	public void setTab_discapacidad(Tabla tab_discapacidad) {
		this.tab_discapacidad = tab_discapacidad;
	}


	public Dialogo getDia_filtro_activo() {
		return dia_filtro_activo;
	}


	public void setDia_filtro_activo(Dialogo dia_filtro_activo) {
		this.dia_filtro_activo = dia_filtro_activo;
	}


	public Tabla getTab_sri_proy_ing() {
		return tab_sri_proy_ing;
	}


	public void setTab_sri_proy_ing(Tabla tab_sri_proy_ing) {
		this.tab_sri_proy_ing = tab_sri_proy_ing;
	}


	public Tabla getTab_sri_det_proy_ing() {
		return tab_sri_det_proy_ing;
	}


	public void setTab_sri_det_proy_ing(Tabla tab_sri_det_proy_ing) {
		this.tab_sri_det_proy_ing = tab_sri_det_proy_ing;
	}


	public Tabla getTab_sri_gastos_deducible() {
		return tab_sri_gastos_deducible;
	}


	public void setTab_sri_gastos_deducible(Tabla tab_sri_gastos_deducible) {
		this.tab_sri_gastos_deducible = tab_sri_gastos_deducible;
	}

	public Tabla getTab_sri_impuesto_renta() {
		return tab_sri_impuesto_renta;
	}


	public void setTab_sri_impuesto_renta(Tabla tab_sri_impuesto_renta) {
		this.tab_sri_impuesto_renta = tab_sri_impuesto_renta;
	}

	public Tabla getTab_deta_empleado_depar() {
		return tab_deta_empleado_depar;
	}


	public Tabla getTab_anio() {
		return tab_anio;
	}



	public void setTab_anio(Tabla tab_anio) {
		this.tab_anio = tab_anio;
	}



	public void setTab_deta_empleado_depar(Tabla tab_deta_empleado_depar) {
		this.tab_deta_empleado_depar = tab_deta_empleado_depar;
	}


	public String getEdadCompleta(String fecha_nac,boolean en_letras){
		if (fecha_nac!=null && !fecha_nac.isEmpty()) {
			String fecha_actual=utilitario.getFechaActual();
			int mes_nac=0,dia_nac=0,anio_nac=0;
			int mes_actual=0,dia_actual=0,anio_actual=0;
			int mes_edad=0,dia_edad=0,anio_edad=0;

			anio_nac=utilitario.getAnio(utilitario.getFormatoFecha(utilitario.getFecha(fecha_nac)));
			mes_nac=utilitario.getMes(utilitario.getFormatoFecha(utilitario.getFecha(fecha_nac)));
			dia_nac=utilitario.getDia(utilitario.getFormatoFecha(utilitario.getFecha(fecha_nac)));

			anio_actual=utilitario.getAnio(fecha_actual);
			mes_actual=utilitario.getMes(fecha_actual);
			dia_actual=utilitario.getDia(fecha_actual);

			if (anio_nac<=anio_actual) {
				anio_edad=anio_actual-anio_nac;
				if (mes_nac>mes_actual) {
					anio_edad=anio_edad-1;
					mes_edad=12-mes_nac+mes_actual;
				}
				if (mes_nac==mes_actual) {
					if (dia_nac>dia_actual) {
						anio_edad=anio_edad-1;
					}
					mes_edad=mes_actual-mes_nac;
				}
				if (mes_nac<mes_actual) {
					mes_edad=mes_actual-mes_nac;
				}
				if (dia_nac>dia_actual) {
					dia_edad=30-dia_nac+dia_actual;
				}else {
					dia_edad=dia_actual-dia_nac;
				}
				//return ""+anio_edad+""+mes_edad+""+dia_edad;
				if (en_letras){
					return anio_edad+" anos "+mes_edad+" meses "+dia_edad+" dias";	
				}else{
					return anio_edad+"-"+mes_edad+"-"+dia_edad;
				}
			}
		}
		return null;
	}

	/**
	 * muestra en pantalla la tabla con los datos de dialogo de Detalle Empleado Departamento 
	 */
	public void dibujarDatosDialogoDetalleEmpleadoDepar() {
		//		if (aut_empleado.getValor() != null) {
		//			str_opcion = "5";
		//			pan_opcion.getChildren().clear();
		//			pan_opcion.getChildren().add(efecto);
		//			tab_cargas_familiares = new Tabla();

		//			tab_dia_detalle_empleado_depar.setId("tab_dia_detalle_empleado_depar");
		//			tab_dia_detalle_empleado_depar.setSql("SELECT IDE_GEDED as IDE_EMPLEA,IDE_GEEDP,IDE_GEINS,IDE_GEAME,FECHA_INGRESO_GEDED,FECHA_SALIDA_GEDED,OBSERVACION_GEDED,ACTIVO_GEDED " +
		//					"FROM GEN_DETALLE_EMPLEADO_DEPARTAME");
		//			tab_dia_detalle_empleado_depar.setCampoPrimaria("IDE_EMPLEA");
		//			tab_dia_detalle_empleado_depar.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "");
		//			tab_dia_detalle_empleado_depar.getColumna("IDE_GEINS").setAutoCompletar();
		//			tab_dia_detalle_empleado_depar.getColumna("IDE_GEAME").setCombo("SELECT a.IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED FROM GEN_ACCION_MOTIVO_EMPLEADO a " +
		//					"LEFT JOIN ( " +
		//					"SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA " +
		//					")b ON b.IDE_GEAED=a.IDE_GEAED " +
		//					"LEFT JOIN ( " +
		//					"SELECT IDE_GEMED,DETALLE_GEMED FROM GEN_MOTIVO_EMPLEADO_DEPA " +
		//					")c ON c.IDE_GEMED=a.IDE_GEMED " +
		//					"ORDER BY  b.DETALLE_GEAED,c.DETALLE_GEMED");	
		//			tab_dia_detalle_empleado_depar.getColumna("IDE_GEAME").setAutoCompletar();
		//			tab_dia_detalle_empleado_depar.getColumna("IDE_GEEDP").setVisible(false);
		//			tab_dia_detalle_empleado_depar.getColumna("ACTIVO_GEDED").setCheck();			
		//			tab_dia_detalle_empleado_depar.setCondicion("IDE_GEEDP =" + tab_empleado_departamento.getValor("IDE_GEEDP"));
		//			tab_dia_detalle_empleado_depar.setTipoFormulario(true);
		//			tab_dia_detalle_empleado_depar.getGrid().setColumns(4);
		//			tab_dia_detalle_empleado_depar.setScrollable(true);
		//			tab_dia_detalle_empleado_depar.setScrollHeight(60);
		//			tab_dia_detalle_empleado_depar.setLectura(true);
		//			tab_dia_detalle_empleado_depar.dibujar();
		//			actualizarCombosDepartamentoEmpleado();
		//			PanelTabla pat_panel2 = new PanelTabla();
		//			pat_panel2.setPanelTabla(tab_deta_empleado_depar);

		///DIALOGO ACCIONES DE EMPLEADOS



		dia_deta_emplea_depar.dibujar();


		//			pan_opcion.setTitle("DATOS CARGAS FAMILIARES");
		//			pan_opcion.getChildren().add(pat_panel1);
		//		} else {
		//			utilitario.agregarMensajeInfo("No se puede abrir el item", "Seleccione un Colaborador en el autocompletar");
		////			limpiar();
		//		}
	}

	public SeleccionTabla getSet_sucursal() {
		return set_sucursal;
	}


	public void setSet_sucursal(SeleccionTabla set_sucursal) {
		this.set_sucursal = set_sucursal;
	}





	public SeleccionTabla getSet_empleado_deducible() {
		return set_empleado_deducible;
	}



	public void setSet_empleado_deducible(SeleccionTabla set_empleado_deducible) {
		this.set_empleado_deducible = set_empleado_deducible;
	}



	public SeleccionTabla getSet_departamento() {
		return set_departamento;
	}


	public void setSet_departamento(SeleccionTabla set_departamento) {
		this.set_departamento = set_departamento;
	}


	public SeleccionTabla getSet_sri() {
		return set_sri;
	}


	public void setSet_sri(SeleccionTabla set_sri) {
		this.set_sri = set_sri;
	}


	public Consulta getCon_acciones_personal() {
		return con_acciones_personal;
	}


	public void setCon_acciones_personal(Consulta con_acciones_personal) {
		this.con_acciones_personal = con_acciones_personal;
	}

	public Dialogo getDia_deta_emplea_depar() {
		return dia_deta_emplea_depar;
	}

	public void setDia_deta_emplea_depar(Dialogo dia_deta_emplea_depar) {
		this.dia_deta_emplea_depar = dia_deta_emplea_depar;
	}

	public Tabla getTab_dia_detalle_empleado_depar() {
		return tab_dia_detalle_empleado_depar;
	}

	public void setTab_dia_detalle_empleado_depar(
			Tabla tab_dia_detalle_empleado_depar) {
		this.tab_dia_detalle_empleado_depar = tab_dia_detalle_empleado_depar;
	}

	public SeleccionCalendario getSel_cal_cumpleanios() {
		return sel_cal_cumpleanios;
	}

	public void setSel_cal_cumpleanios(SeleccionCalendario sel_cal_cumpleanios) {
		this.sel_cal_cumpleanios = sel_cal_cumpleanios;
	}

	public SeleccionTabla getSel_tab_area() {
		return sel_tab_area;
	}

	public void setSel_tab_area(SeleccionTabla sel_tab_area) {
		this.sel_tab_area = sel_tab_area;
	}

	public SeleccionTabla getSel_tab_departamento() {
		return sel_tab_departamento;
	}

	public void setSel_tab_departamento(SeleccionTabla sel_tab_departamento) {
		this.sel_tab_departamento = sel_tab_departamento;
	}

	public SeleccionTabla getSel_tab_sucursal() {
		return sel_tab_sucursal;
	}




	public Tabla getTab_cuenta_anticipo() {
		return tab_cuenta_anticipo;
	}



	public void setTab_cuenta_anticipo(Tabla tab_cuenta_anticipo) {
		this.tab_cuenta_anticipo = tab_cuenta_anticipo;
	}



	public SeleccionTabla getSet_cuenta_anticipo() {
		return set_cuenta_anticipo;
	}



	public void setSet_cuenta_anticipo(SeleccionTabla set_cuenta_anticipo) {
		this.set_cuenta_anticipo = set_cuenta_anticipo;
	}



	public void setSel_tab_sucursal(SeleccionTabla sel_tab_sucursal) {
		this.sel_tab_sucursal = sel_tab_sucursal;
	}
	public void calcularSueldoConyuge(AjaxBehaviorEvent evt){
		tab_situacion_economica.modificar(evt);
		double sueldo=0;
		double otro=0;
		double total=0;
		if(tab_situacion_economica.getValor("SUELDO_CONYUGE_GTSEE")!=null && !tab_situacion_economica.getValor("SUELDO_CONYUGE_GTSEE").isEmpty()){
			if(tab_situacion_economica.getValor("OTRO_INGRESO_GTSEE")!=null && !tab_situacion_economica.getValor("OTRO_INGRESO_GTSEE").isEmpty()){
				sueldo=Double.parseDouble(tab_situacion_economica.getValor("SUELDO_CONYUGE_GTSEE"));
				otro=Double.parseDouble(tab_situacion_economica.getValor("OTRO_INGRESO_GTSEE"));
				total=sueldo+otro;
				tab_situacion_economica.setValor("TOTAL_INGRESO_CONYUGE_GTSEE",total+"");
				utilitario.addUpdateTabla(tab_situacion_economica, "TOTAL_INGRESO_CONYUGE_GTSEE", "");
			}else{
				utilitario.agregarMensajeInfo("No se puede calcular el Total", "Ingrese otro ingreso del Conyuge");
			}

		}else{
			utilitario.agregarMensajeInfo("No se puede calcular el Total", "Ingrese el Sueldo del Conyuge");
		}
	}

	public void cargarFechaIngresoEmpleado(DateSelectEvent evt){
		tab_empleado.modificar(evt);
		if(tab_empleado.getValor("fecha_ingreso_grupo_gtemp")!=null && !tab_empleado.getValor("fecha_ingreso_grupo_gtemp").isEmpty()){
			tab_empleado.setValor("fecha_ingreso_gtemp", tab_empleado.getValor("fecha_ingreso_grupo_gtemp"));
			utilitario.addUpdateTabla(tab_empleado, "fecha_ingreso_gtemp", "");
		}
	}


	public void valorPorcentajeSeguro(AjaxBehaviorEvent evt){		
		tab_beneficiario_seguro.modificar(evt);
		if (tab_beneficiario_seguro.getTotalFilas()>0 ) {			
			tab_beneficiario_seguro.sumarColumnas();
			utilitario.addUpdate("tab_beneficiario_seguro");

		}	
	}

	public Dialogo getDia_contrata() {
		return dia_contrata;
	}

	public void setDia_contrata(Dialogo dia_contrata) {
		this.dia_contrata = dia_contrata;
	}

	public Tabla getTab_empleado_departamento_dia() {
		return tab_empleado_departamento_dia;
	}

	public void setTab_empleado_departamento_dia(Tabla tab_empleado_departamento_dia) {
		this.tab_empleado_departamento_dia = tab_empleado_departamento_dia;
	}

	public Tabla getTab_partida_cargo() {
		return tab_partida_cargo;
	}

	public void setTab_partida_cargo(Tabla tab_partida_cargo) {
		this.tab_partida_cargo = tab_partida_cargo;
	}



}
