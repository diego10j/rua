/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.biff.drawing.ComboBox;

import org.primefaces.event.SelectEvent;


import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;

/**
 * 
 * @author Diego
 */
public class pre_grupo_cargo extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	private SeleccionTabla set_grupo = new SeleccionTabla();
	private Map p_parametros = new HashMap();
	private ListaSeleccion lis_activo = new ListaSeleccion();
	private ListaSeleccion lis_aplicar = new ListaSeleccion();
	private Dialogo dia_filtro_activo = new Dialogo();
	private Combo cmb_tipo_emp = new Combo();
	private Radio rad_grupo = new Radio();
	private SeleccionTabla set_empleado = new SeleccionTabla(); 
	private Dialogo dia_actualizar_grupo_ocupacional = new Dialogo();
	private Dialogo dia_aplica_grupo = new Dialogo();
	private Boton bot_act_grup_linea = new Boton();
	int band=0;
	private Tabla tab_grupo_minimo_seleccion=new Tabla();
	private Tabla tab_grupo_factor=new Tabla();
	private Tabla tab_factor_ponederacion=new Tabla();
	private Tabla tab_evl_grupo_factor=new Tabla();

	public pre_grupo_cargo() {

		Boton bot_actualizar_grupo_emp = new Boton();
		bot_actualizar_grupo_emp.setValue("ACTUALIZAR GRUPOxEMPLEADO");
		bot_actualizar_grupo_emp.setMetodo("actualizarGrupoEmpleado");
		bar_botones.agregarBoton(bot_actualizar_grupo_emp);

		//	Boton bot_act_grup_linea = new Boton();
		bot_act_grup_linea.setValue("ACTUALIZAR LINEA DE SUPERVISION");
		bot_act_grup_linea.setMetodo("actualizaLineaSupervision");
		bar_botones.agregarBoton(bot_act_grup_linea);

		Boton bot_actualizar_rmu=new Boton();
		bot_actualizar_rmu.setValue("ACTUALIZAR RMU");
		bot_actualizar_rmu.setMetodo("actualizarRmu");
		bar_botones.agregarBoton(bot_actualizar_rmu);



		bar_botones.agregarReporte();
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);
		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		set_grupo.setId("set_grupo");
		set_grupo.setSeleccionTabla("SELECT  IDE_GEGRO, "
				+ "DETALLE_GEGRO,SIGLAS_GEGRO from GEN_GRUPO_OCUPACIONAL "
				+ "WHERE ACTIVO_GEGRO=TRUE", "IDE_GEGRO");
		set_grupo.getBot_aceptar().setMetodo("aceptarReporte");
		set_grupo.setTitle("SELECCION GRUPO OCUPACIONAL");
		agregarComponente(set_grupo);

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", 1);
		tab_tabla1.getColumna("ACTIVO_GEGRO").setCheck();
		tab_tabla1.getColumna("ACTIVO_GEGRO").setValorDefecto("true");
		tab_tabla1.getColumna("DETALLE_GEGRO").setRequerida(true);
		tab_tabla1.getColumna("SIGLAS_GEGRO").setRequerida(true);
		tab_tabla1.onSelect("seleccionarTabla1");
		tab_tabla1.getColumna("IDE_GTNIV").setCombo("GTH_NIVEL_VIATICO","IDE_GTNIV" , "DETALLE_GTNIV", "");
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("GRUPO OCUPACIONAL");
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setGenerarPrimaria(false);
		tab_tabla2.setTabla("GEN_GRUPO_CARGO", "IDE_GEGRO,IDE_GECAF", 2);// clave
		// primaria
		// compuesta
		tab_tabla2.getColumna("ACTIVO_GEGRC").setCheck();
		tab_tabla2.getColumna("ACTIVO_GEGRC").setValorDefecto("true");
		tab_tabla2.getColumna("IDE_GEGRO").setLectura(true);
		//tab_tabla2.getColumna("IDE_GEGRO").setVisible(false);
		tab_tabla2.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL",
				"IDE_GECAF", "DETALLE_GECAF,SIGLAS_GECAF", "");
		tab_tabla2.setValidarInsertar(true);// Para que solo inserte de una en
		//tab_tabla2.onSelect("seleccionarTabla2");
		// una
		filtrarGrupoCargos();
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("GRUPO CARGO ");
		pat_panel2.setPanelTabla(tab_tabla2);

                      /*  
		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_grupo_minimo_seleccion.setId("tab_grupo_minimo_seleccion");
		tab_grupo_minimo_seleccion.setIdCompleto("tab_tabulador:tab_grupo_minimo_seleccion");
		
		tab_grupo_minimo_seleccion.setTabla("SPR_GRUPO_MINIMO_SELECCION", "IDE_SPGMS", 3);
		tab_grupo_minimo_seleccion.getColumna("IDE_GEGRO").setVisible(false);
		tab_grupo_minimo_seleccion.getColumna("ACTIVO_SPGMS").setCheck();
		tab_grupo_minimo_seleccion.getColumna("ACTIVO_SPGMS").setValorDefecto("true");
		System.out.println("tab_grupo_minimo_seleccion....  "+tab_grupo_minimo_seleccion.getSql());
		
		tab_grupo_minimo_seleccion.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_grupo_minimo_seleccion);


		tab_grupo_factor.setId("tab_grupo_factor");
		tab_grupo_factor.setIdCompleto("tab_tabulador:tab_grupo_factor");
		tab_grupo_factor.setTabla("SPR_GRUPO_FACTOR", "IDE_SPGRF", 4);
		tab_grupo_factor.getColumna("IDE_GEGRO").setVisible(false);
		tab_grupo_factor.getColumna("IDE_SPFAC").setCombo("select ide_spfac,detalle_spfac from spr_factor where spr_ide_spfac is null order by ide_spfac");
		tab_grupo_factor.getColumna("ACTIVO_SPGRF").setCheck();
		tab_grupo_factor.getColumna("ACTIVO_SPGRF").setValorDefecto("true");
		System.out.println("tab_grupo_factor....  "+tab_grupo_factor.getSql());
		
		tab_grupo_factor.dibujar();

		PanelTabla pat_panel4=new PanelTabla();
		pat_panel4.setPanelTabla(tab_grupo_factor);


		tab_factor_ponederacion.setId("tab_factor_ponederacion");
		tab_factor_ponederacion.setIdCompleto("tab_tabulador:tab_factor_ponederacion");
		tab_factor_ponederacion.setTabla("SPR_FACTOR_PONDERACION", "IDE_SPFAP", 5);
		tab_factor_ponederacion.getColumna("IDE_GEGRO").setVisible(false);
		tab_factor_ponederacion.getColumna("IDE_SPFAC").setCombo("select a.ide_spfac,detalle2||'  -  '||detalle_spfac from spr_factor a,(select ide_spfac,detalle_spfac as detalle2 from spr_factor ) b where a.spr_ide_spfac =b.ide_spfac order by spr_ide_spfac ");

		tab_factor_ponederacion.getColumna("ACTIVO_SPFAP").setCheck();
		tab_factor_ponederacion.getColumna("ACTIVO_SPFAP").setValorDefecto("true");
		
		
		tab_factor_ponederacion.dibujar();

		PanelTabla pat_panel5=new PanelTabla();
		pat_panel5.setPanelTabla(tab_factor_ponederacion);



		tab_evl_grupo_factor.setId("tab_evl_grupo_factor");
		tab_evl_grupo_factor.setIdCompleto("tab_tabulador:tab_evl_grupo_factor");
		tab_evl_grupo_factor.setTabla("EVL_GRUPO_FACTOR", "IDE_EVGRF", 6);

		tab_evl_grupo_factor.getColumna("IDE_GEGRO").setVisible(false);
		tab_evl_grupo_factor.getColumna("IDE_EVFAE").setCombo("EVL_FACTOR_EVALUACION","IDE_EVFAE", "DETALLE_EVFAE", "");

		tab_evl_grupo_factor.getColumna("ACTIVO_EVGRF").setCheck();
		tab_evl_grupo_factor.getColumna("ACTIVO_EVGRF").setValorDefecto("true");
		
		
		System.out.println("tab_factor_ponederacion....  "+tab_factor_ponederacion.getSql());
		
		tab_evl_grupo_factor.dibujar();

		PanelTabla pat_panel6=new PanelTabla();
		pat_panel6.setPanelTabla(tab_evl_grupo_factor);




		tab_tabulador.agregarTab("SELECCION MINIMOS", pat_panel3);
		tab_tabulador.agregarTab("GRUPO POR FACTOR", pat_panel4);
		tab_tabulador.agregarTab("FACTORES PONDERACION", pat_panel5);
		tab_tabulador.agregarTab("EVAL: FACTORES", pat_panel6);	

		System.out.println("tab_evl_grupo_factor....  "+tab_evl_grupo_factor.getSql());
                */
		Division div_division = new Division();

		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2,"60%", "H");

		agregarComponente(div_division);

		agregarComponente(sef_reporte);
		agregarComponente(set_grupo);

		List lista = new ArrayList();
		Object fila1[] = { "0", "INACTIVO" };
		Object fila2[] = { "1", "ACTIVO" };
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setListaSeleccion(lista);
		lis_activo.setVertical();

		cmb_tipo_emp.setId("cmb_tipo_emp");
		cmb_tipo_emp.setCombo("select IDE_GTGRE,DETALLE_GTGRE from GTH_GRUPO_EMPLEADO " +
				"order by DETALLE_GTGRE asc");

		Grid gri_grupo_ocupacional =new Grid();
		gri_grupo_ocupacional.setColumns(2);
		gri_grupo_ocupacional.getChildren().add(new Etiqueta("Seleccione Tipo Empleado"));
		gri_grupo_ocupacional.getChildren().add(cmb_tipo_emp);
		dia_actualizar_grupo_ocupacional.setId("dia_actualizar_grupo_ocupacional");
		dia_actualizar_grupo_ocupacional.setTitle("SELECCIONE TIPO EMPLEADO");
		dia_actualizar_grupo_ocupacional.setHeight("50%");
		dia_actualizar_grupo_ocupacional.setWidth("50%");
		dia_actualizar_grupo_ocupacional.setDialogo(gri_grupo_ocupacional);
		dia_actualizar_grupo_ocupacional.getBot_aceptar().setMetodo("actualizarEmpleado");
		dia_actualizar_grupo_ocupacional.getBot_cancelar().setMetodo("volver");
		dia_actualizar_grupo_ocupacional.setDynamic(false);
		agregarComponente(dia_actualizar_grupo_ocupacional);
		dia_filtro_activo.setId("dia_filtro_activo");
		dia_filtro_activo.setTitle("SELECCIONE GRUPO ACTIVO / INACTIVO");
		dia_filtro_activo.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_activo.setDialogo(lis_activo);
		agregarComponente(dia_filtro_activo);

		set_empleado.setId("set_empleado");
		set_empleado.setSeleccionTabla("select " +
				"edp.IDE_Geedp, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from   GTH_EMPLEADO EMP  " +
				"LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"left join GTH_GRUPO_EMPLEADO GEMP ON GEMP.IDE_GTGRE=EDP.IDE_GTGRE " +
				"WHERE  EMP.ACTIVO_GTEMP=TRUE AND EDP.IDE_GEGRO=0 " +
				"AND EDP.ACTIVO_GEEDP=TRUE " +
				" order by nombres asc","IDE_GEEDP");
		set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getBot_aceptar().setMetodo("actualizarGrupoOcupacional");

		set_empleado.setTitle("SELECCION DE EMPLEADOS");
		agregarComponente(set_empleado);;

		List lista1 = new ArrayList();
		Object fila11[] = { "0", "A TODOS" };
		Object fila22[] = { "1", "POR GRUPOS" };
		lista1.add(fila11);
		lista1.add(fila22);
		rad_grupo.setId("rad_grupo");
		rad_grupo.setRadio(lista1);
		rad_grupo.setVertical();

		dia_aplica_grupo.setId("dia_aplica_grupo");
		dia_aplica_grupo.setTitle("SELECCIONE UNA OPCION ");
		dia_aplica_grupo.getBot_aceptar().setMetodo("opcionLineaSupervision");
		dia_aplica_grupo.getBot_cancelar().setMetodo("volver");
		dia_aplica_grupo.setDialogo(rad_grupo);
		dia_aplica_grupo.setDynamic(false);
		agregarComponente(dia_aplica_grupo);
	}

	public void opcionLineaSupervision(){
		//cmb_tipo_emp.setValue(null);
		try {
			if(rad_grupo.getValue().equals("1")){
				dia_aplica_grupo.cerrar();
				set_empleado.getTab_seleccion().setSql("select " +
						"edp.IDE_Geedp, " +
						"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
						"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
						"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
						"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
						"from   GTH_EMPLEADO EMP  " +
						"LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
						"left join GTH_GRUPO_EMPLEADO GEMP ON GEMP.IDE_GTGRE=EDP.IDE_GTGRE " +
						"WHERE  EMP.ACTIVO_GTEMP=TRUE AND EDP.IDE_GEGRO="+tab_tabla1.getValorSeleccionado()+" " +
						"AND EDP.ACTIVO_GEEDP=TRUE " +
						"order by nombres asc");
				//set_empleado.getTab_seleccion().imprimirSql();
				set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
				set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
				set_empleado.getTab_seleccion().ejecutarSql();
				set_empleado.getBot_aceptar().setMetodo("actualizarEmpleado");
				//dia_actualizar_grupo_ocupacional.dibujar();
				set_empleado.dibujar();
			}
			else if(rad_grupo.getValue().equals("0"))	 {

				set_empleado.getTab_seleccion().setSql("select " +
						"edp.IDE_Geedp, " +
						"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
						"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
						"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
						"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
						"from    GTH_EMPLEADO EMP " +
						"LEFT JOIN   GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
						"left join GEN_GRUPO_CARGO GEMP ON GEMP.IDE_GeGRo=EDP.IDE_GeGRo " +
						"WHERE  EMP.ACTIVO_GTEMP=TRUE  and EDP.IDE_GEGRO is not null " +
						"AND EDP.ACTIVO_GEEDP=TRUE " +
						"group by edp.IDE_Geedp, " +
						"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.APELLIDO_PATERNO_GTEMP, " +
						"EMP.APELLIDO_MATERNO_GTEMP, " +
						"EMP.PRIMER_NOMBRE_GTEMP,  " +
						"EMP.SEGUNDO_NOMBRE_GTEMP " +
						" order by nombres asc");
				set_empleado.getTab_seleccion().imprimirSql();
				set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
				set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
				set_empleado.getTab_seleccion().ejecutarSql();
				set_empleado.getBot_aceptar().setMetodo("actualizarEmpleado");
				dia_aplica_grupo.cerrar();
				set_empleado.dibujar();

			}
			else {
				utilitario.agregarMensajeInfo("No ha seleccionado ninguna Opcion", "");
			}

		} catch (Exception e) {
			utilitario.agregarMensajeInfo("No ha seleccionado ninguna Opcion", "");
		}		
	}
	public void volver(){
		band=0;
		rad_grupo.setValue(null);
		cmb_tipo_emp.setValue(null);
		dia_actualizar_grupo_ocupacional.cerrar();
		dia_aplica_grupo.cerrar();
		set_empleado.cerrar();
	}
	public void  actualizaLineaSupervision(){
		band=1;
		if(tab_tabla1.getValorSeleccionado()!=null && !tab_tabla1.isEmpty()){
			rad_grupo.setValue(null);
			dia_aplica_grupo.dibujar();}
		else {
			utilitario.agregarMensajeInfo("No ha seleccionado ningun grupo", "");
		}

	}
	public void actualizarGrupoEmpleado(){
		System.out.println("lo dek combi  "+cmb_tipo_emp.getValue());
		band=0;
		if(tab_tabla1.getValorSeleccionado()!=null ){
			set_empleado.getTab_seleccion().setSql("select " +
					"edp.IDE_Geedp, " +
					"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
					"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
					"EMP.APELLIDO_MATERNO_GTEMP || ' ' ||  " +
					"EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  " +
					"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
					"from   GTH_EMPLEADO EMP  " +
					"LEFT JOIN  GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
					"left join GTH_GRUPO_EMPLEADO GEMP ON GEMP.IDE_GTGRE=EDP.IDE_GTGRE " +
					"WHERE  EMP.ACTIVO_GTEMP=TRUE AND EDP.IDE_GEGRO="+tab_tabla1.getValorSeleccionado()+" " +
					"AND EDP.ACTIVO_GEEDP=TRUE " +
					" order by nombres asc");
			set_empleado.getTab_seleccion().imprimirSql();
			set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
			set_empleado.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
			set_empleado.getTab_seleccion().ejecutarSql();
			set_empleado.getBot_aceptar().setMetodo("actualizarGrupoOcupacional");
			set_empleado.dibujar();
		}else {
			utilitario.agregarMensajeInfo("No ha seleccionado ningun grupo", "");
		}

	}

	public void actualizarGrupoOcupacional(){
		if(set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()){
			dia_actualizar_grupo_ocupacional.dibujar();
		}
		else {
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Cargo");
		}
	}

	public void actualizarEmpleado(){
		if(band==1){
			if(rad_grupo.getValue().equals("1"))	{
				System.out.println("EMPLEADOS"+set_empleado.getSeleccionados());
				utilitario.getConexion().agregarSql("update  GEN_EMPLEADOS_DEPARTAMENTO_PAR set LINEA_SUPERVICION_GEEDP=true  " +
						"WHERE IDE_GEGRO="+tab_tabla1.getValorSeleccionado()+"  " + 
						" and ACTIVO_GEEDP=true " +
						" and IDE_GEEDP in("+set_empleado.getSeleccionados()+")");
				guardarPantalla();	
				rad_grupo.setValue(null);
				set_empleado.cerrar();
				cmb_tipo_emp.setValue(null);
				dia_actualizar_grupo_ocupacional.cerrar();

			}else if (rad_grupo.getValue().equals("0")) {
				utilitario.getConexion().agregarSql("update  GEN_EMPLEADOS_DEPARTAMENTO_PAR set LINEA_SUPERVICION_GEEDP=true  " +
						"WHERE IDE_GEGRO="+tab_tabla1.getValorSeleccionado()+"  " +
						" and ACTIVO_GEEDP=true " +
						" and IDE_GEEDP in("+set_empleado.getSeleccionados()+")");
				guardarPantalla();	
				rad_grupo.setValue(null);
				cmb_tipo_emp.setValue(null);
				set_empleado.cerrar();
				dia_actualizar_grupo_ocupacional.cerrar();
			}else {
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ninguna Opcion");
			}

		}
		else if(band==0){
			try {
				if(cmb_tipo_emp.getValue()!=null ){
					System.out.println("grupos"+cmb_tipo_emp.getValue());
					System.out.println("grupos1"+tab_tabla1.getValorSeleccionado());
					System.out.println("empleados"+set_empleado.getSeleccionados());
					utilitario.getConexion().agregarSql("update  GEN_EMPLEADOS_DEPARTAMENTO_PAR set ide_gtgre="+cmb_tipo_emp.getValue()+" " +
							" WHERE IDE_GEGRO="+tab_tabla1.getValorSeleccionado()+" " +
							" and ACTIVO_GEEDP=true " +
							" and IDE_GEEDP in("+set_empleado.getSeleccionados()+")");
					guardarPantalla();	
					rad_grupo.setValue(null);
					cmb_tipo_emp.setValue(null);
					set_empleado.cerrar();
					dia_actualizar_grupo_ocupacional.cerrar();
				}	else {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo");
				}

			} catch (Exception e) {
				// TODO: handle exception
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo");
			}


		}else if(band==2){
			if(rad_grupo.getValue().equals("1")){				
				String empleado=set_empleado.getSeleccionados(); 
				String[] str_vec=empleado.split(",");
				
				for (int i = 0; i < str_vec.length; i++) {
					System.out.println("EMPLEADOS del sel tab: "+set_empleado.getSeleccionados());
					utilitario.getConexion().agregarSql("update gen_empleados_departamento_par  " +
							"set rmu_geedp=(select rmu_gegrc from gen_grupo_cargo where ide_gecaf " +
							"in (select ide_gecaf from gen_empleados_departamento_par where ide_geedp in ("+str_vec[i]+")) and ide_gegro " +
							"in (select ide_gegro from gen_empleados_departamento_par where ide_geedp in ("+str_vec[i]+"))) where ide_geedp="+str_vec[i]);
				}				
				guardarPantalla();
				rad_grupo.setValue(null);
				set_empleado.cerrar();
				cmb_tipo_emp.setValue(null);
				dia_actualizar_grupo_ocupacional.cerrar();


				
			}else if (rad_grupo.getValue().equals("0")) {
				String empleado=set_empleado.getSeleccionados(); 
				String[] str_vec=empleado.split(",");
				
				for (int i = 0; i < str_vec.length; i++) {
					System.out.println("EMPLEADOS del sel tab: "+set_empleado.getSeleccionados());
					utilitario.getConexion().agregarSql("update gen_empleados_departamento_par  " +
							"set rmu_geedp=(select rmu_gegrc from gen_grupo_cargo where ide_gecaf " +
							"in (select ide_gecaf from gen_empleados_departamento_par where ide_geedp in ("+str_vec[i]+")) and ide_gegro " +
							"in (select ide_gegro from gen_empleados_departamento_par where ide_geedp in ("+str_vec[i]+"))) where ide_geedp="+str_vec[i]);
				}				
				guardarPantalla();
				rad_grupo.setValue(null);
				cmb_tipo_emp.setValue(null);
				set_empleado.cerrar();
				dia_actualizar_grupo_ocupacional.cerrar();
			}else {
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ninguna Opcion");
			}

		}
		else {
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ninguna Opcion");
		}
	}

	public void actualizarRmu(){
		band=2;
		if(tab_tabla1.getValorSeleccionado()!=null && !tab_tabla1.isEmpty()){
			rad_grupo.setValue(null);
			dia_aplica_grupo.dibujar();}
		else {
			utilitario.agregarMensajeInfo("No ha seleccionado ningun grupo", "");
		}
	}


	@Override
	public void insertar() {
		if (tab_tabla1.isFocus()) {
			tab_tabla1.insertar();
			tab_tabla2.limpiar();
		} else if (tab_tabla2.isFocus()) {
			// Inserta solo si la fila seleccionada de la tabla de grupo
			// ocupacional y la tabla de cargo funcional tienen clave primaria
			if (!tab_tabla1.isFilaInsertada()) {
				tab_tabla2.getColumna("IDE_GEGRO").setValorDefecto(
						tab_tabla1.getValor(tab_tabla1.getFilaActual(),
								"IDE_GEGRO"));
				tab_tabla2.insertar();
			} else {
				utilitario.agregarMensajeInfo("No se puede insertar ",
						"Debe guardar la tabla de Grupo Ocupacional ");
			}
		}else if(tab_grupo_minimo_seleccion.isFocus()){
			tab_grupo_minimo_seleccion.insertar();
			if(tab_grupo_minimo_seleccion.isFilaInsertada()){
				tab_grupo_minimo_seleccion.setValor("IDE_GEGRO", tab_tabla2.getValor("IDE_GEGRO"));				
			}		
		}else if(tab_grupo_factor.isFocus()){
			tab_grupo_factor.insertar();
			if(tab_grupo_factor.isFilaInsertada()){
				tab_grupo_factor.setValor("IDE_GEGRO", tab_tabla2.getValor("IDE_GEGRO"));				
			}		
		}else if(tab_factor_ponederacion.isFocus()){
			tab_factor_ponederacion.insertar();
			if(tab_factor_ponederacion.isFilaInsertada()){
				tab_factor_ponederacion.setValor("IDE_GEGRO", tab_tabla2.getValor("IDE_GEGRO"));				
			}		
		}else if(tab_evl_grupo_factor.isFocus()){
			tab_evl_grupo_factor.insertar();
			if(tab_evl_grupo_factor.isFilaInsertada()){
				tab_evl_grupo_factor.setValor("IDE_GEGRO", tab_tabla2.getValor("IDE_GEGRO"));				
			}		
		}

	}

	@Override
	public void guardar() {
		if (tab_tabla1.isFocus()) {
			tab_tabla1.guardar();
			guardarPantalla();
		} else if (tab_tabla2.isFocus()) {
			tab_tabla2.guardar();
			guardarPantalla();
		} else if (tab_grupo_minimo_seleccion.isFocus()) {
			tab_grupo_minimo_seleccion.guardar();
			guardarPantalla();
		} else if (tab_grupo_factor.isFocus()) {
			tab_grupo_factor.guardar();
			guardarPantalla();
		} else if (tab_factor_ponederacion.isFocus()) {
			tab_factor_ponederacion.guardar();
			guardarPantalla();
		} else if (tab_evl_grupo_factor.isFocus()) {
			tab_evl_grupo_factor.guardar();
			guardarPantalla();
		}

	}

	@Override
	public void eliminar() {
		if (tab_tabla1.isFocus()) {
			if (tab_tabla1.eliminar()) {
				// si es que si elimina actualiza la nueva fila seleccionada
				filtrarGrupoCargos();
				tab_tabla2.ejecutarSql();
			}
		} else if (tab_tabla2.isFocus()) {
			tab_tabla2.eliminar();
		} else if (tab_grupo_minimo_seleccion.isFocus()) {
			tab_grupo_minimo_seleccion.eliminar();
		} else if (tab_grupo_factor.isFocus()) {
			tab_grupo_factor.eliminar();
		} else if (tab_factor_ponederacion.isFocus()) {
			tab_factor_ponederacion.eliminar();
		} else if (tab_evl_grupo_factor.isFocus()) {
			tab_evl_grupo_factor.eliminar();
		}

	}
	public void seleccionarTabla2(SelectEvent evt){
		tab_tabla2.seleccionarFila(evt);
		tab_grupo_factor.setCondicion("IDE_GEGRO="+tab_tabla2.getValor("IDE_GEGRO"));
		tab_grupo_factor.ejecutarSql();
		tab_grupo_minimo_seleccion.setCondicion("IDE_GEGRO="+tab_tabla2.getValor("IDE_GEGRO"));
		tab_grupo_minimo_seleccion.ejecutarSql();
		tab_factor_ponederacion.setCondicion("IDE_GEGRO="+tab_tabla2.getValor("IDE_GEGRO"));
		tab_factor_ponederacion.ejecutarSql();
		tab_evl_grupo_factor.setCondicion("IDE_GEGRO="+tab_tabla2.getValor("IDE_GEGRO"));
		tab_evl_grupo_factor.ejecutarSql();
	}
	/**
	 * Se ejecuta cuando se selecciona una fila de la tabla1 de Grupo
	 * Ocupacional, actualiza grupo cargos
	 * 
	 * @param evt
	 */

	public void seleccionarTabla1(SelectEvent evt) {
		tab_tabla1.seleccionarFila(evt);
		filtrarGrupoCargos();
		tab_tabla2.ejecutarSql();
	}

	/**
	 * Filtra a los grupos ocupacionales y cargos funcionales seleccionados
	 */

	public void filtrarGrupoCargos() {
		String str_grupo = tab_tabla1.getValor("IDE_GEGRO");
		if (str_grupo == null || str_grupo.isEmpty()) {
			str_grupo = "-1";
		}
		tab_tabla2.setCondicion("IDE_GEGRO =" + str_grupo);
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

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}

	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Detalle Grupo Cargo")) {
			if (rep_reporte.isVisible()) {
				p_parametros = new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();
			} else if (dia_filtro_activo.isVisible()) {
				System.out.println("ACTIVOS :" + lis_activo.getSeleccionados());
				set_grupo
				.getTab_seleccion()
				.setSql("SELECT  IDE_GEGRO, "
						+ "DETALLE_GEGRO,SIGLAS_GEGRO from GEN_GRUPO_OCUPACIONAL "
						+ "WHERE ACTIVO_GEGRO in("
						+ lis_activo.getSeleccionados() + ")");
				set_grupo.getTab_seleccion().ejecutarSql();
				set_grupo.getBot_aceptar().setMetodo("aceptarReporte");
				dia_filtro_activo.cerrar();
				set_grupo.dibujar();
			} else if (set_grupo.isVisible()) {
				if (set_grupo.getSeleccionados() != null
						&& !set_grupo.getSeleccionados().isEmpty()) {
					p_parametros.put("IDE_GEGRO", set_grupo.getSeleccionados());
					p_parametros.put("titulo", "DETALLE GRUPO OCUPACIONAL POR EMPLADO ");
					sef_reporte.setSeleccionFormatoReporte(p_parametros,
							rep_reporte.getPath());
					set_grupo.cerrar();
					sef_reporte.dibujar();
				} else {
					utilitario.agregarMensajeInfo(
							"No ha seleccionado ningun grupo", "");
				}

			}
		}

	}

	public Dialogo getDia_filtro_activo() {
		return dia_filtro_activo;
	}

	public void setDia_filtro_activo(Dialogo dia_filtro_activo) {
		this.dia_filtro_activo = dia_filtro_activo;
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

	public SeleccionTabla getSet_grupo() {
		return set_grupo;
	}

	public void setSet_grupo(SeleccionTabla set_grupo) {
		this.set_grupo = set_grupo;
	}

	public Dialogo getDia_actualizar_grupo_ocupacional() {
		return dia_actualizar_grupo_ocupacional;
	}

	public void setDia_actualizar_grupo_ocupacional(
			Dialogo dia_actualizar_grupo_ocupacional) {
		this.dia_actualizar_grupo_ocupacional = dia_actualizar_grupo_ocupacional;
	}

	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}

	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}

	public Tabla getTab_grupo_minimo_seleccion() {
		return tab_grupo_minimo_seleccion;
	}

	public void setTab_grupo_minimo_seleccion(Tabla tab_grupo_minimo_seleccion) {
		this.tab_grupo_minimo_seleccion = tab_grupo_minimo_seleccion;
	}

	public Tabla getTab_grupo_factor() {
		return tab_grupo_factor;
	}

	public void setTab_grupo_factor(Tabla tab_grupo_factor) {
		this.tab_grupo_factor = tab_grupo_factor;
	}

	public Tabla getTab_factor_ponederacion() {
		return tab_factor_ponederacion;
	}

	public void setTab_factor_ponederacion(Tabla tab_factor_ponederacion) {
		this.tab_factor_ponederacion = tab_factor_ponederacion;
	}

	public Tabla getTab_evl_grupo_factor() {
		return tab_evl_grupo_factor;
	}

	public void setTab_evl_grupo_factor(Tabla tab_evl_grupo_factor) {
		this.tab_evl_grupo_factor = tab_evl_grupo_factor;
	}


} 