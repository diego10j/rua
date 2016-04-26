package paq_nomina;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioEscenariosNomina;
import sistema.aplicacion.Pantalla;
import sistema.aplicacion.Utilitario;

public class pre_escenarios_nomina extends Pantalla{

	private Utilitario utilitario=new Utilitario();
	private Tabla tab_rol_escenario=new Tabla();
	private Tabla tab_deta_rol_escenario=new Tabla();
	
	private Confirmar con_guardar=new Confirmar();

	@EJB
	private ServicioEscenariosNomina ser_escenario_nomina = (ServicioEscenariosNomina) utilitario.instanciarEJB(ServicioEscenariosNomina.class);

	private SeleccionTabla set_tipo_nomina=new SeleccionTabla();

	public pre_escenarios_nomina(){

		Boton bot_impor_empleados=new Boton();
		bot_impor_empleados.setValue("Importar Empleados");
		bot_impor_empleados.setIcon("ui-icon-note");
		bot_impor_empleados.setMetodo("abrirDialogoImportarEmpleados");

		bar_botones.agregarBoton(bot_impor_empleados);

		Boton bot_calcular=new Boton();
		bot_calcular.setValue("Calcular");
		bot_calcular.setIcon("ui-icon-note");
		bot_calcular.setMetodo("calcular");

		bar_botones.agregarBoton(bot_calcular);

		
		set_tipo_nomina.setId("set_tipo_nomina");
		set_tipo_nomina.setTitle("SELECCION TIPO DE NOMINA");
		set_tipo_nomina.setSeleccionTabla("select TEM.IDE_GTTEM,DETALLE_GTTEM FROM GTH_TIPO_EMPLEADO TEM " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_GTTEM=TEM.IDE_GTTEM " +
				"AND DTN.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_normal"),"IDE_GTTEM");
		set_tipo_nomina.getBot_aceptar().setMetodo("aceptarTipoEmpleado");
		set_tipo_nomina.setDynamic(false);
		agregarComponente(set_tipo_nomina);



		tab_rol_escenario.setId("tab_rol_escenario");
		tab_rol_escenario.setTabla("NRH_ROL_ESCENARIO", "IDE_NRROE", 1);
		tab_rol_escenario.getColumna("POR_VARIACION_NRROE").setMetodoChange("cambiaPorcentaje");
		tab_rol_escenario.getColumna("IDE_GEANI").setCombo("GEN_ANIO", "IDE_GEANI", "nom_geani", "");
		tab_rol_escenario.getColumna("ACTIVO_NRROE").setCheck();
		tab_rol_escenario.getColumna("ACTIVO_NRROE").setValorDefecto("true");
		tab_rol_escenario.getColumna("APROBADO_NRROE").setCheck();
		tab_rol_escenario.getColumna("APROBADO_NRROE").setValorDefecto("FALSE");
		
		tab_rol_escenario.agregarRelacion(tab_deta_rol_escenario);
		tab_rol_escenario.setTipoFormulario(true);
		tab_rol_escenario.getGrid().setColumns(4);
		tab_rol_escenario.dibujar();

		PanelTabla pat_tab1=new PanelTabla();
		pat_tab1.setPanelTabla(tab_rol_escenario);

		tab_deta_rol_escenario.setId("tab_deta_rol_escenario");
		tab_deta_rol_escenario.setTabla("NRH_ROL_DETALLE_ESCENARIO", "IDE_NRRDS", 2);
		tab_deta_rol_escenario.getColumna("IDE_NRDTN").setCombo("select dtn.IDE_NRDTN,DETALLE_GTTEM FROM GTH_TIPO_EMPLEADO TEM " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_GTTEM=TEM.IDE_GTTEM " +
				"AND DTN.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_normal"));
		tab_deta_rol_escenario.setRecuperarLectura(true);
		tab_deta_rol_escenario.getColumna("ACTIVO_NRROE").setValorDefecto("1");
		tab_deta_rol_escenario.getColumna("ACTIVO_NRROE").setVisible(false);
		tab_deta_rol_escenario.getColumna("RMU_ESCENARIO_NRROE").setRequerida(true);
		tab_deta_rol_escenario.getColumna("RMU_ANTERIOR_NRROE").setLectura(true);
		tab_deta_rol_escenario.getColumna("RMU_ESCENARIO_NRROE").setLectura(false);
		tab_deta_rol_escenario.getColumna("DECIMO_TERCERO_NRROE").setLectura(true);
		tab_deta_rol_escenario.getColumna("DECIMO_CUARTO_NRROE").setLectura(true);
		tab_deta_rol_escenario.getColumna("APORTE_PATRONAL_NRROE").setLectura(true);
		tab_deta_rol_escenario.getColumna("FONDO_RESREVA_NRROE").setLectura(true);
		tab_deta_rol_escenario.getColumna("TOTAL_ANUAL_NRROE").setLectura(true);
		tab_deta_rol_escenario.getColumna("IDE_GECAF").setCombo("GEN_CARGO_FUNCIONAL", "IDE_GECAF", "DETALLE_GECAF","");
		tab_deta_rol_escenario.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL", "IDE_GEGRO", "DETALLE_GEGRO","");
		tab_deta_rol_escenario.getColumna("IDE_GEGRO").setVisible(false);
		tab_deta_rol_escenario.getColumna("IDE_NRDTN").setRequerida(true);
		tab_deta_rol_escenario.getColumna("DOCUMENTO_IDENTIDAD_NRROE").setRequerida(true);
		tab_deta_rol_escenario.getColumna("APELLIDO_NOMBRE_NRROE").setRequerida(true);
		tab_deta_rol_escenario.getColumna("IDE_GTTCO").setCombo("GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO","");
		tab_deta_rol_escenario.setColumnaSuma("RMU_ANTERIOR_NRROE,RMU_ESCENARIO_NRROE,DECIMO_TERCERO_NRROE,DECIMO_CUARTO_NRROE,APORTE_PATRONAL_NRROE,FONDO_RESREVA_NRROE,TOTAL_ANUAL_NRROE");
		tab_deta_rol_escenario.setCampoOrden("APELLIDO_NOMBRE_NRROE");
		tab_deta_rol_escenario.dibujar();

		
		PanelTabla pat_tab2=new PanelTabla();
		pat_tab2.setPanelTabla(tab_deta_rol_escenario);
		
		
		Division div=new Division();
		div.dividir2(pat_tab1, pat_tab2, "30%", "H");

		agregarComponente(div);
		
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
	}

	public void cambiaPorcentaje(AjaxBehaviorEvent evt){
		tab_rol_escenario.modificar(evt);
		calcular();
	}
	public void calcular(){

		if (tab_deta_rol_escenario.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede calcular", "No existen empleados importados");
			return;
		}

		
		if (tab_rol_escenario.getValor("POR_VARIACION_NRROE")==null || tab_rol_escenario.getValor("POR_VARIACION_NRROE").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular", "El porcentaje de aumento es nulo o vacio");
			return;
		}
		if (Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))==0 ){
			utilitario.agregarMensajeInfo("No se puede calcular", "El porcentaje no puede ser cero");
			return;
		}

		if (Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))<0 ){
			utilitario.agregarMensajeInfo("No se puede calcular", "El porcentaje no puede ser menor a cero");
			return;
		}
		if (Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))>100 ){
			utilitario.agregarMensajeInfo("No se puede calcular", "El porcentaje no puede ser mayor a 100");
			return;
		}
		
		for (int i = 0; i < tab_deta_rol_escenario.getTotalFilas(); i++) {
			if (tab_deta_rol_escenario.isFilaInsertada(i)){
				utilitario.agregarMensajeInfo("No se puede calcular","Primero debe guardar las filas insertadas");
				return;
			}
		}
		
		tab_deta_rol_escenario.guardar();
		guardarPantalla();
		
		utilitario.getConexion().ejecutarSql("update NRH_ROL_ESCENARIO set POR_VARIACION_NRROE="+tab_rol_escenario.getValor("POR_VARIACION_NRROE")+" where IDE_NRROE="+tab_rol_escenario.getValor("IDE_NRROE"));
		
		TablaGenerica tab=utilitario.consultar("SELECT * from NRH_DETALLE_TIPO_NOMINA where IDE_NRDTN in " +
				"( select a.ide_nrdtn from ( "+
		""+tab_deta_rol_escenario.getSql()+" ) a group by a.ide_nrdtn )");
		
		String ide_gttem="";
		for (int i = 0; i < tab.getTotalFilas(); i++) {
			ide_gttem+=tab.getValor(i,"IDE_GTTEM")+",";
		}

		try {
			ide_gttem=ide_gttem.substring(0, ide_gttem.length()-1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		ser_escenario_nomina.generarRol(ser_escenario_nomina.getEmpleadosEscenario(tab_rol_escenario.getValorSeleccionado(),Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))).getSql(),tab_rol_escenario.getValorSeleccionado());
		
		tab_deta_rol_escenario.ejecutarValorForanea(tab_rol_escenario.getValorSeleccionado());
		tab_deta_rol_escenario.sumarColumnas();
		
		utilitario.addUpdate("tab_deta_rol_escenario");
	}

	public void importarEmpleados(){
		
		if (con_guardar.isVisible()){
			con_guardar.cerrar();
			utilitario.getConexion().ejecutarSql("delete from NRH_ROL_DETALLE_ESCENARIO where ide_nrroe="+tab_rol_escenario.getValorSeleccionado());
		}
		str_ide_gttem=set_tipo_nomina.getSeleccionados();
		TablaGenerica tab_emp=ser_escenario_nomina.getEmpleadosEscenario(set_tipo_nomina.getSeleccionados());

		System.out.println("tab emp "+tab_emp.getSql());
		for (int i = 0; i < tab_emp.getTotalFilas(); i++) {
			tab_deta_rol_escenario.insertar();
			tab_deta_rol_escenario.setValor("IDE_GEGRO",tab_emp.getValor(i, "IDE_GEGRO"));
			tab_deta_rol_escenario.setValor("IDE_GECAF",tab_emp.getValor(i, "IDE_GECAF"));
			tab_deta_rol_escenario.setValor("IDE_GTTCO",tab_emp.getValor(i, "IDE_GTTCO"));
			tab_deta_rol_escenario.setValor("IDE_NRDTN",tab_emp.getValor(i, "IDE_NRDTN"));
			tab_deta_rol_escenario.setValor("IDE_SUCU",tab_emp.getValor(i, "IDE_SUCU"));
			tab_deta_rol_escenario.setValor("DOCUMENTO_IDENTIDAD_NRROE",tab_emp.getValor(i, "IDE_GEEDP"));
			tab_deta_rol_escenario.setValor("APELLIDO_NOMBRE_NRROE",tab_emp.getValor(i, "NOMBRES"));
			tab_deta_rol_escenario.setValor("RMU_ANTERIOR_NRROE",tab_emp.getValor(i, "RMU_GEEDP"));

			double rmu_escenario=Double.parseDouble(tab_emp.getValor(i, "RMU_GEEDP"))+((Double.parseDouble(tab_emp.getValor(i, "RMU_GEEDP"))* Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE")))/100);
			tab_deta_rol_escenario.setValor("RMU_ESCENARIO_NRROE",utilitario.getFormatoNumero(rmu_escenario));

		}

		if (tab_deta_rol_escenario.guardar()){
			utilitario.getConexion().guardarPantalla();	
		}
		
		tab_deta_rol_escenario.ejecutarValorForanea(tab_rol_escenario.getValorSeleccionado());
		tab_deta_rol_escenario.sumarColumnas();
		utilitario.addUpdate("tab_deta_rol_escenario");
	}
	
	String str_ide_gttem="";
	public void aceptarTipoEmpleado(){
		if (set_tipo_nomina.getListaSeleccionados().size()==0){
			utilitario.agregarMensajeInfo("Debe seleccionar un tipo de empleado","");
			return;
		}

		
		if (tab_deta_rol_escenario.getTotalFilas()>0){
		con_guardar.setTitle("CONFIRMACION SOBREESCRIBIR DATOS");
		con_guardar.setMessage("YA EXISTEN EMPLEADOS IMPORTADOS, DESEA SOBREESCRIBIR LOS EMPLEADOS");
		con_guardar.getBot_aceptar().setMetodo("importarEmpleados");
		con_guardar.dibujar();
		set_tipo_nomina.cerrar();
		utilitario.addUpdate("con_guardar");
		}else{
			importarEmpleados();
		}
		

		
	}

	public void abrirDialogoImportarEmpleados(){
		if (tab_rol_escenario.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede importar los empleados","No existe escenario de nomina");
			return;
		}
		if (tab_rol_escenario.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede importar los empleados","Primero debe guardar el escenario en curso");
			return;
		}

		set_tipo_nomina.dibujar();
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_rol_escenario.isFocus()){
			tab_rol_escenario.insertar();
		}
		else{
			tab_deta_rol_escenario.insertar();
		}
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_rol_escenario.isFocus()){
			if (tab_rol_escenario.getValor("POR_VARIACION_NRROE")==null || tab_rol_escenario.getValor("POR_VARIACION_NRROE").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje de aumento es nulo o vacio");
				return;
			}
			if (Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))==0 ){
				utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje no puede ser cero");
				return;
			}

			if (Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))<0 ){
				utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje no puede ser menor a cero");
				return;
			}
			if (Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))>100 ){
				utilitario.agregarMensajeInfo("No se puede guardar", "El porcentaje no puede ser mayor a 100");
				return;
			}

			if (tab_rol_escenario.guardar()){
				guardarPantalla();
			}
		}else if (tab_deta_rol_escenario.isFocus()){
			
			double rmu_escenario=0;
			try {
				rmu_escenario=Double.parseDouble(tab_deta_rol_escenario.getValor("RMU_ESCENARIO_NRROE"))-((Double.parseDouble(tab_deta_rol_escenario.getValor("RMU_ESCENARIO_NRROE"))* Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE")))/(100+Double.parseDouble(tab_rol_escenario.getValor("POR_VARIACION_NRROE"))));
			} catch (Exception e) {
				// TODO: handle exception
			}
					
			tab_deta_rol_escenario.setValor("RMU_ANTERIOR_NRROE", utilitario.getFormatoNumero(rmu_escenario));
			utilitario.addUpdateTabla(tab_deta_rol_escenario, "RMU_ANTERIOR_NRROE", "");
			if (tab_deta_rol_escenario.guardar()){
				guardarPantalla();
			}
		}
	}
	

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_rol_escenario.isFocus()){
			tab_rol_escenario.eliminar();
		}else if (tab_deta_rol_escenario.isFocus()){
			
			
			utilitario.getConexion().agregarSqlPantalla("delete from NRH_ROL_DETALLE_ESCENARIO where ide_nrrds= "+tab_deta_rol_escenario.getValorSeleccionado());
			guardarPantalla();
			tab_deta_rol_escenario.ejecutarValorForanea(tab_rol_escenario.getValorSeleccionado());
			tab_deta_rol_escenario.sumarColumnas();
			
			utilitario.addUpdate("tab_deta_rol_escenario");
		}
	}

	public Tabla getTab_rol_escenario() {
		return tab_rol_escenario;
	}

	public void setTab_rol_escenario(Tabla tab_rol_escenario) {
		this.tab_rol_escenario = tab_rol_escenario;
	}

	public Tabla getTab_deta_rol_escenario() {
		return tab_deta_rol_escenario;
	}

	public void setTab_deta_rol_escenario(Tabla tab_deta_rol_escenario) {
		this.tab_deta_rol_escenario = tab_deta_rol_escenario;
	}

	public SeleccionTabla getSet_tipo_nomina() {
		return set_tipo_nomina;
	}

	public void setSet_tipo_nomina(SeleccionTabla set_tipo_nomina) {
		this.set_tipo_nomina = set_tipo_nomina;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}


}
