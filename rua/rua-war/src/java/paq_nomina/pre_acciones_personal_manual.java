/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

/**
 *
 * Esta vlase permite realizar las subrogaciones de lso empleados
 */
public class pre_acciones_personal_manual extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();	
	private SeleccionTabla set_subroga = new SeleccionTabla();
	private Confirmar con_guardar=new Confirmar();
	public static String par_subrogacion;

	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);




	public pre_acciones_personal_manual() {
		par_subrogacion =utilitario.getVariable("p_gen_encargo_posicion");

		bar_botones.limpiar();
		aut_empleado.setId("aut_empleado");		
		aut_empleado.setAutoCompletar("select IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP,APELLIDO_PATERNO_GTEMP,APELLIDO_MATERNO_GTEMP,PRIMER_NOMBRE_GTEMP,SEGUNDO_NOMBRE_GTEMP from GTH_EMPLEADO");
		aut_empleado.setMetodoChange("filtrarEmpleado");
		bar_botones.agregarComponente(new Etiqueta("Empleado"));
		bar_botones.agregarComponente(aut_empleado);

		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);

		Boton bot_subroga = new Boton();
		bot_subroga.setValue("PUESTOS A SUBROGAR");
		bot_subroga.setTitle("PUESTOS SUBROGACION");
		bot_subroga.setIcon("ui-icon-person");
		bot_subroga.setMetodo("importarPuestos");
		bar_botones.agregarBoton(bot_subroga);
		
		
		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("GEN_DETALLE_EMPLEADO_DEPARTAME","IDE_GEDED", 1);
		tab_tabla1.setHeader("SUBROGACIONES DE LOS FUNCIONARIOS");
		tab_tabla1.getColumna("FECHA_SALIDA_GEDED").setVisible(false);
		tab_tabla1.getColumna("GEN_IDE_GEDED").setVisible(false);
		tab_tabla1.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "");
		tab_tabla1.getColumna("IDE_GEINS").setAutoCompletar();
		tab_tabla1.getColumna("IDE_GEINS").setVisible(false);
		tab_tabla1.getColumna("IDE_GEAME").setCombo("SELECT a.IDE_GEAME,b.DETALLE_GEAED,c.DETALLE_GEMED FROM GEN_ACCION_MOTIVO_EMPLEADO a "
				+ "LEFT JOIN ( "
				+ "SELECT IDE_GEAED,DETALLE_GEAED from GEN_ACCION_EMPLEADO_DEPA "
				+ ")b ON b.IDE_GEAED=a.IDE_GEAED "
				+ "LEFT JOIN ( "
				+ "SELECT IDE_GEMED,DETALLE_GEMED FROM GEN_MOTIVO_EMPLEADO_DEPA "
				+ ")c ON c.IDE_GEMED=a.IDE_GEMED "
				+ "ORDER BY  b.DETALLE_GEAED,c.DETALLE_GEMED");
		tab_tabla1.getColumna("IDE_GEAME").setAutoCompletar();
		tab_tabla1.getColumna("IDE_GEAME").setMetodoChange("cambioAccion");
		tab_tabla1.getColumna("IDE_GEAME").setLongitud(100);
		tab_tabla1.getColumna("IDE_GEAME").setNombreVisual("DENOMINACION CONTRATO");
		tab_tabla1.getColumna("fecha_ingreso_geded").setNombreVisual("FECHA CONTRATO");

		tab_tabla1.getColumna("FECHA_INGRESO_GEDED").setValorDefecto(utilitario.getFechaActual());
		tab_tabla1.getColumna("ACTIVO_GEDED").setCheck();
		tab_tabla1.getColumna("ACTIVO_GEDED").setValorDefecto("true");		
		tab_tabla1.getColumna("IDE_GTEMP").setVisible(false);
		tab_tabla1.getColumna("observacion_geded").setVisible(false);

		tab_tabla1.setCampoOrden("IDE_GEDED DESC");		
		tab_tabla1.setCondicion("IDE_GTEMP=-1 AND ACTIVO_GEDED=true");		
		tab_tabla1.getColumna("GEN_IDE_GEDED").setLectura(true);
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.setLectura(true);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);


		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("GEN_EMPLEADOS_DEPARTAMENTO_PAR","IDE_GEEDP", 3);
		tab_tabla2.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO", "");
		tab_tabla2.getColumna("FECHA_GEEDP").setValorDefecto(utilitario.getFechaActual());
		tab_tabla2.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL","IDE_GECAF","DETALLE_GECAF","");
		tab_tabla2.getColumna("GEN_IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF", "");
		tab_tabla2.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
		tab_tabla2.getColumna("IDE_SUCU").setVisible(true);
		tab_tabla2.getColumna("IDE_GEARE").setCombo("GEN_AREA","IDE_GEARE","DETALLE_GEARE","");
		tab_tabla2.getColumna("IDE_GEDEP").setCombo("GEN_DEPARTAMENTO","IDE_GEDEP","DETALLE_GEDEP","");
		tab_tabla2.getColumna("IDE_GECAE").setCombo("GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE", "");
		tab_tabla2.getColumna("IDE_GETIV").setCombo("GEN_TIPO_VINCULACION", "IDE_GETIV", "DETALLE_GETIV", "");
		tab_tabla2.getColumna("AJUSTE_SUELDO_GEEDP").setMetodoChange("cambioAjuste");
		tab_tabla2.getColumna("IDE_GEPGC").setCombo("SELECT IDE_GEPGC,PAP.CODIGO_PARTIDA_GEPAP,PAP.DETALLE_GEPAP,pgc.TITULO_CARGO_GEPGC "
				+ "from GEN_PARTIDA_GRUPO_CARGO pgc "
				+ "left join GEN_PARTIDA_PRESUPUESTARIA pap on PAP.IDE_GEPAP=PGC.IDE_GEPAP");
		tab_tabla2.getColumna("IDE_GTTEM").setCombo("GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_tabla2.getColumna("IDE_GTTCO").setCombo("GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO", "");
		tab_tabla2.getColumna("IDE_GTTSI").setCombo("GTH_TIPO_SINDICATO", "IDE_GTTSI", "DETALLE_GTTSI", "");
		tab_tabla2.getColumna("IDE_GTGRE").setCombo("GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
		tab_tabla2.getColumna("IDE_GTGRE").setCombo("GTH_GRUPO_EMPLEADO", "IDE_GTGRE", "DETALLE_GTGRE", "");
		tab_tabla2.getColumna("ACTIVO_GEEDP").setCheck();
		tab_tabla2.getColumna("ACTIVO_GEEDP").setLectura(true);
		tab_tabla2.getColumna("ACTIVO_GEEDP").setValorDefecto("true");
		tab_tabla2.getColumna("ACUMULA_FONDOS_GEEDP").setCheck();
		tab_tabla2.getColumna("LINEA_SUPERVICION_GEEDP").setCheck();
		tab_tabla2.getColumna("IDE_GEPGC").setAutoCompletar();
		tab_tabla2.getColumna("IDE_GTEMP").setVisible(false);
		tab_tabla2.getColumna("LIQUIDACION_GEEDP").setLectura(true);
		tab_tabla2.getColumna("LIQUIDACION_GEEDP").setCheck();
		tab_tabla2.getColumna("CONTROL_ASISTENCIA_GEEDP").setCheck();
		tab_tabla2.getColumna("CONTROL_ASISTENCIA_GEEDP").setValorDefecto("false");
		tab_tabla2.getColumna("LIQUIDACION_GEEDP").setCheck();
		tab_tabla2.getColumna("LIQUIDACION_GEEDP").setValorDefecto("false");
		tab_tabla2.getColumna("EJECUTO_LIQUIDACION_GEEDP").setCheck();
		tab_tabla2.getColumna("EJECUTO_LIQUIDACION_GEEDP").setValorDefecto("false");
		tab_tabla2.getColumna("GEN_IDE_GEGRO").setVisible(false);
		tab_tabla2.getColumna("GEN_IDE_GECAF").setVisible(false);
		tab_tabla2.getColumna("IDE_GETIV").setVisible(false);
		tab_tabla2.getColumna("IDE_GTGRE").setVisible(false);
		tab_tabla2.getColumna("IDE_GECAE").setVisible(false);
		tab_tabla2.getColumna("IDE_GECAF").setVisible(false);
		tab_tabla2.getColumna("IDE_GTTSI").setVisible(false);
		tab_tabla2.getColumna("LIQUIDACION_GEEDP").setVisible(false);
		tab_tabla2.getColumna("LINEA_SUPERVICION_GEEDP").setVisible(false);
		tab_tabla2.getColumna("EJECUTO_LIQUIDACION_GEEDP").setVisible(false);
		tab_tabla2.getColumna("OBSERVACION_GEEDP").setVisible(false);
		tab_tabla2.getColumna("fecha_ajuste_geedp").setVisible(false);
		tab_tabla2.getColumna("fecha_liquidacion_geedp").setVisible(false);
		tab_tabla2.getColumna("encargado_subrogado_geedp").setLectura(true);
		tab_tabla2.getColumna("ide_gepgc").setLectura(true);
		tab_tabla2.getColumna("ide_gegro").setLectura(true);
		tab_tabla2.getColumna("ide_gecaf").setLectura(true);
		tab_tabla2.getColumna("ide_sucu").setLectura(true);
		tab_tabla2.getColumna("ide_gedep").setLectura(true);
		tab_tabla2.getColumna("ide_geare").setLectura(true);
		tab_tabla2.getColumna("fecha_geedp").setLectura(true);
		tab_tabla2.getColumna("rmu_geedp").setLectura(true);
		tab_tabla2.getColumna("ajuste_sueldo_geedp").setLectura(true);
		tab_tabla2.getColumna("ide_gttco").setLectura(true);


		tab_tabla2.getColumna("IDE_GTTCO").setNombreVisual("TIPO CONTRATO");
		tab_tabla2.getColumna("IDE_GEPGC").setNombreVisual("PARTIDA PRESUPUESTARIA");
		tab_tabla2.getColumna("IDE_SUCU").setNombreVisual("LUGAR TRABAJO");
		tab_tabla2.getColumna("IDE_GTTEM").setNombreVisual("REGIMEN LABORAL");
		tab_tabla2.getColumna("IDE_GEGRO").setNombreVisual("GRUPO OCUPACIONAL");
		tab_tabla2.getColumna("IDE_GEARE").setNombreVisual("PROCESO");
		tab_tabla2.getColumna("IDE_GEDEP").setNombreVisual("SUB PROCESO");
		tab_tabla2.getColumna("RMU_GEEDP").setNombreVisual("RMU");
		tab_tabla2.getColumna("fecha_finctr_geedp").setNombreVisual("FECHA FIN GESTION");
		tab_tabla2.getColumna("fecha_geedp").setNombreVisual("FECHA INICIO GESTION");
		tab_tabla2.getColumna("ajuste_sueldo_geedp").setNombreVisual("DIFERENCIA SUBROGACION");
		tab_tabla2.getColumna("fecha_encargo_geedp").setNombreVisual("FECHA INICIO SUBROGACION");
		tab_tabla2.getColumna("fecha_encargo_fin_geedp").setNombreVisual("FECHA FIN SUBROGACION");
		tab_tabla2.getColumna("sueldo_subroga_geedp").setNombreVisual("RMU SUBROGACION");
		tab_tabla2.getColumna("acumula_fondos_geedp").setNombreVisual("ACUMULA FONDOS");
		tab_tabla2.getColumna("control_asistencia_geedp").setNombreVisual("CONTROL ASISTENCIA");
		tab_tabla2.getColumna("activo_geedp").setNombreVisual("ACTIVO");
		tab_tabla2.getColumna("encargado_subrogado_geedp").setNombreVisual("SUBROGADO");

		
		tab_tabla2.setMostrarcampoSucursal(true);
		//tab_tabla2.setLectura(true);
		tab_tabla2.setTipoFormulario(true);
		tab_tabla2.getGrid().setColumns(4);
		tab_tabla2.getColumna("IDE_GTTCO").setMetodoChange("cambioTipoContrato");		
		tab_tabla2.dibujar();

		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "20%", "H");
		agregarComponente(div_division);
		
		
		
		set_subroga.setId("set_subroga");
		set_subroga.setSeleccionTabla(ser_nomina.ideEmpleadoSubrogacion(" and 1=0 "),"ide_geedp");
		set_subroga.getTab_seleccion().getColumna("documento_identidad_gtemp").setNombreVisual("CEDULA");
		set_subroga.getTab_seleccion().getColumna("documento_identidad_gtemp").setLongitud(20);
		set_subroga.getTab_seleccion().getColumna("empleado").setNombreVisual("EMPLEADO");
		set_subroga.getTab_seleccion().getColumna("empleado").setLongitud(100);
		set_subroga.getTab_seleccion().getColumna("codigo_partida_gepap").setNombreVisual("PARTIDA PRESUP�ESTARIA");
		set_subroga.getTab_seleccion().getColumna("codigo_partida_gepap").setLongitud(20);
		set_subroga.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
		set_subroga.getTab_seleccion().getColumna("empleado").setFiltro(true);

		set_subroga.getBot_aceptar().setMetodo("aceptarCambio");
		set_subroga.getTab_seleccion().ejecutarSql();
		set_subroga.setRadio();
		agregarComponente(set_subroga);
		
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE SUBROGAR EL PUESTO SELECCIONADO");
		con_guardar.setTitle("CONFIRMACION DE INGRESO");

		agregarComponente(con_guardar);
		
	}
 public void importarPuestos(){
	 if(aut_empleado.getValor() != null){
		 set_subroga.getTab_seleccion().setSql(ser_nomina.ideEmpleadoSubrogacion(" and d.ide_gegro  in ( select ide_gegro from gen_grupo_ocupacional where subrogacion_gegro =true) and activo_geedp= true"));
		 set_subroga.getTab_seleccion().ejecutarSql();
		 set_subroga.dibujar();
		 
	 }
	 else {
		 utilitario.agregarMensajeInfo("Seleccione una opción", "Debe seleccionar un funcionario");
	 }
 }
 long ide_inicial=0;

 public void aceptarCambio(){

	String str_seleccionados=set_subroga.getValorSeleccionado();
	System.out.println(" probando el str_Selccionado "+str_seleccionados);

	 if(str_seleccionados!=null || !str_seleccionados.isEmpty()){
	/* if (!con_guardar.isVisible()){
		 set_subroga.cerrar();
			// dibuja dialogo de confirmacion de recepcion de activjvos fijos
			con_guardar.setMessage("ESTA SEGURO DE SUBROGAR EL PUESTO SELECCIONADO");
			con_guardar.setTitle("CONFIRMACION DE INGRESO");
			con_guardar.getBot_aceptar().setMetodo("aceptarCambio");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}else{
		//	con_guardar.cerrar();
			
			tab_tabla1.setValor("activo_geded", "false");
			tab_tabla2.setValor("activo_geedp", "false");
			tab_tabla1.modificar(tab_tabla1.getFilaActual());
			tab_tabla2.modificar(tab_tabla2.getFilaActual());
			utilitario.addUpdate("tab_tabla1,tab_tabla2");
			*/
			
		 utilitario.getConexion().ejecutarSql("update gen_detalle_empleado_departame set activo_geded=false where ide_geded ="+tab_tabla1.getValor("ide_geded"));
		 utilitario.getConexion().ejecutarSql("update gen_empleados_departamento_par set activo_geedp=false where ide_geedp ="+tab_tabla2.getValor("ide_geedp"));

			System.out.println(" entre a guardar ");
		
			utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'gen_detalle_empleado_departame'");
			TablaGenerica valor=utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("gen_detalle_empleado_departame", "ide_geded"));
			ide_inicial=Long.parseLong(valor.getValor("codigo"));
			utilitario.getConexion().ejecutarSql("insert into gen_detalle_empleado_departame (ide_geded,ide_geame,fecha_ingreso_geded,activo_geded,gen_ide_geded,ide_gtemp)"
					+" values ( "+ide_inicial+","+par_subrogacion+",'"+utilitario.getFechaActual()+"','true',"+tab_tabla1.getValor("ide_geded")+","+tab_tabla2.getValor("ide_gtemp")+" )");
			

			utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'gen_empleados_departamento_par'");
			TablaGenerica valor2=utilitario.consultar(ser_contabilidad.servicioCodigoMaximo("gen_empleados_departamento_par", "ide_geedp"));
			long ide_inicial2=Long.parseLong(valor.getValor("codigo"));
			TablaGenerica datos_subrogado = utilitario.consultar("select * from gen_empleados_departamento_par where ide_geedp ="+str_seleccionados);
			TablaGenerica consulta_grupo_ocupacional =utilitario.consultar("select * from gen_grupo_ocupacional where ide_gegro ="+datos_subrogado.getValor("ide_gegro"));
			double ajuste_subroga= Double.parseDouble(consulta_grupo_ocupacional.getValor("rmu_gegro")) - Double.parseDouble(tab_tabla2.getValor("rmu_geedp"));
			utilitario.getConexion().ejecutarSql("insert into gen_empleados_departamento_par (ide_geedp,ide_gtemp,ide_gepgc,ide_gegro,ide_gecaf,ide_sucu,ide_gedep,ide_geare,ide_gttem,"
					+" ide_gttco,ide_gttsi,ide_gtgre,ide_geded,fecha_geedp,rmu_geedp,sueldo_subroga_geedp,ajuste_sueldo_geedp,"
					+" linea_supervicion_geedp,acumula_fondos_geedp,control_asistencia_geedp,encargado_subrogado_geedp,activo_geedp) values ("+ide_inicial2+","+tab_tabla2.getValor("ide_gtemp")+","
					+ datos_subrogado.getValor("ide_gepgc")+","+datos_subrogado.getValor("ide_gegro")+","+datos_subrogado.getValor("ide_gecaf")+","+datos_subrogado.getValor("ide_sucu")+","
					+ datos_subrogado.getValor("ide_gedep")+","+datos_subrogado.getValor("ide_geare")+","+datos_subrogado.getValor("ide_gttem")+","+tab_tabla2.getValor("ide_gttco")+","
					+ tab_tabla2.getValor("ide_gttsi")+","+tab_tabla2.getValor("ide_gtgre")+","+ide_inicial+",'"+tab_tabla2.getValor("fecha_geedp")+"',"
					+ tab_tabla2.getValor("rmu_geedp")+","+consulta_grupo_ocupacional.getValor("rmu_gegro")+","+ajuste_subroga+","+tab_tabla2.getValor("linea_supervicion_geedp")+","
					+ tab_tabla2.getValor("acumula_fondos_geedp")+","+tab_tabla2.getValor("control_asistencia_geedp")+",true,true"+")");
			tab_tabla1.ejecutarSql();
			tab_tabla2.ejecutarSql();
			set_subroga.cerrar();
			utilitario.agregarMensaje("Ejecutado con Exito", "La subrogación se ejecuto con �xito");
	 }
	 else {
		 utilitario.agregarMensajeInfo("Seleccionar una opción", "Debe seleccionar al menos un registro");
	 return;
	 }
 }
	public void filtrarEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		if(aut_empleado.getValor()!=null){
			tab_tabla1.setCondicion("IDE_GTEMP=" + aut_empleado.getValor()+" AND ACTIVO_GEDED=true");
			tab_tabla1.ejecutarSql();
			tab_tabla2.setValorForanea(tab_tabla1.getValorSeleccionado());
		}else{
			utilitario.agregarMensajeInfo("No se puede mostrar registros", "Debe seleccionar un empleado");
		}
		utilitario.addUpdate("tab_tabla1,tab_tabla2");
	}

	public void limpiar(){		
		tab_tabla1.limpiar();
		tab_tabla2.limpiar();
		aut_empleado.limpiar();		
		utilitario.addUpdate("aut_empleado");
	}
	
	
	@Override
	public void insertar() {
		if(aut_empleado.getValor()!=null){
		if (tab_tabla1.isFocus()){
			tab_tabla1.insertar();
		tab_tabla1.setValor("IDE_GTEMP", aut_empleado.getValor());
		}
		if (tab_tabla2.isFocus()){
			tab_tabla2.insertar();
			tab_tabla2.setValor("IDE_GTEMP", aut_empleado.getValor());	
		}
		}else{
			utilitario.agregarMensajeInfo("No se puede Insertar", "Debe seleccionar un Empleado");
		}
	}

	@Override
	public void guardar() {

		if (tab_tabla1.guardar()) {
			if (tab_tabla2.guardar()) {
				if(tab_tabla2.getValor("fecha_finctr_geedp")!=null && !tab_tabla2.getValor("fecha_finctr_geedp").isEmpty()){
					if (utilitario.isFechaMenor(utilitario.getFecha(tab_tabla2.getValor("fecha_finctr_geedp")), utilitario.getFecha(tab_tabla2.getValor("fecha_geedp")))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha fin de contrato no puede ser menor que la fecha contrato");
						return;
					}	
				}					

				if(tab_tabla2.getValor("fecha_encargo_fin_geedp")!=null && !tab_tabla2.getValor("fecha_encargo_fin_geedp").isEmpty()){
					if (utilitario.isFechaMenor(utilitario.getFecha(tab_tabla2.getValor("fecha_encargo_fin_geedp")), utilitario.getFecha(tab_tabla2.getValor("fecha_geedp")))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha encargo fin de contrato no puede ser menor que la fecha contrato");
						return;
					}
				}

				if(tab_tabla2.getValor("fecha_encargo_geedp")!=null && !tab_tabla2.getValor("fecha_encargo_geedp").isEmpty()){
					if (utilitario.isFechaMenor(utilitario.getFecha(tab_tabla2.getValor("fecha_encargo_geedp")), utilitario.getFecha(tab_tabla2.getValor("fecha_geedp")))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha encargo de contrato no puede ser menor que la fecha contrato");
						return;
					}
				}					


				if(tab_tabla2.getValor("fecha_ajuste_geedp")!=null && !tab_tabla2.getValor("fecha_ajuste_geedp").isEmpty()){
					if (utilitario.isFechaMenor(utilitario.getFecha(tab_tabla2.getValor("fecha_ajuste_geedp")), utilitario.getFecha(tab_tabla2.getValor("fecha_geedp")))){
						utilitario.agregarMensajeInfo("No se puede guardar", "La fecha ajuste de contrato no puede ser menor que la fecha contrato");
						return;
					}
				}

				if(tab_tabla2.getValor("fecha_encargo_geedp")!=null && !tab_tabla2.getValor("fecha_encargo_geedp").isEmpty()){
					if(tab_tabla2.getValor("fecha_encargo_fin_geedp")!=null && !tab_tabla2.getValor("fecha_encargo_fin_geedp").isEmpty()){
						if (utilitario.isFechaMenor(utilitario.getFecha(tab_tabla2.getValor("fecha_encargo_fin_geedp")), utilitario.getFecha(tab_tabla2.getValor("fecha_encargo_geedp")))){
							utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de encargo fin  de contrato no puede ser menor que la fecha encargo contrato");
							return;
						}	
					}						
				}

				guardarPantalla();
			}
		}
	} 
	
	
	
	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
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

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}

	public SeleccionTabla getSet_subroga() {
		return set_subroga;
	}

	public void setSet_subroga(SeleccionTabla set_subroga) {
		this.set_subroga = set_subroga;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

}
