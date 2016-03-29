package paq_gestion;

import java.awt.List;
import java.util.ArrayList;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import com.lowagie.text.pdf.AcroFields.Item;

import sistema.aplicacion.Pantalla;
public class pre_empleado_reporta extends Pantalla {


	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	private Tabla tab_tabla3 = new Tabla();

	private Combo cmb_opcion_reporta= new Combo();
	private Dialogo dia_empleado_reporta=new Dialogo();
	private AutoCompletar aut_empleado=new AutoCompletar();

	public  pre_empleado_reporta(){
		Boton btn_area = new Boton();
		Boton btn_departamento = new Boton();
		Boton btn_empleado = new Boton();

		bar_botones.agregarComponente(new Etiqueta("BUSQUEDA"));

		ArrayList  lst_opcion =new ArrayList();
		Object obj[] = {
				"1","AREA"
		};
		Object obj1[] = {
				"2","DEPARTAMENTO"
		};

		Object obj2[] = {
				"3","EMPLEADOS"
		};


		lst_opcion.add(obj);
		lst_opcion.add(obj1);
		lst_opcion.add(obj2);

		cmb_opcion_reporta.setId("cmb_opcion_reporta");
		cmb_opcion_reporta.setCombo(lst_opcion);
		cmb_opcion_reporta.setMetodo("filtrar_por_opcion");


		btn_area.setId("btn_area");
		btn_area.setValue("AREA");
		btn_area.setMetodo("por_areas");
		btn_departamento.setId("btn_departamento");
		btn_departamento.setValue("DEPARTAMENTO");
		btn_departamento.setMetodo("por_departamento");
		btn_empleado.setId("btn_empleado");
		btn_empleado.setValue("EMPLEADO");
		btn_empleado.setMetodo("por_empleados");
		//bar_botones.agregarComponente(btn_area);
		//bar_botones.agregarComponente(btn_departamento);
		//bar_botones.agregarComponente(btn_empleado);
		bar_botones.agregarComponente(cmb_opcion_reporta);

		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setSql("SELECT IDE_SUCU,NOM_SUCU FROM SIS_SUCURSAL ");
		tab_tabla1.setCampoPrimaria("IDE_SUCU");
		tab_tabla1.setNumeroTabla(1);
		tab_tabla1.onSelect("seleccionarTablaSucursal");

		tab_tabla1.setLectura(true);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("SUCURSAL");
		pat_panel1.setPanelTabla(tab_tabla1);



		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setSql(getSqlTablaAreasSucursal("-1"));
		tab_tabla2.onSelect("seleccionarTablaFiltrada");
		tab_tabla2.setCampoPrimaria("IDE_TABLA");
		tab_tabla2.setNumeroTabla(2);
		tab_tabla2.setLectura(true);
		tab_tabla2.dibujar();

		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("CAMPOS : ");
		pat_panel2.setPanelTabla(tab_tabla2);


		tab_tabla3.setId("tab_tabla3");
		tab_tabla3.setSql("select " +
				"EMP.IDE_GTEMP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || " +
				"EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP as NOMBRES, " +
				"ARE.DETALLE_GEARE, " +
				"DEP.DETALLE_GEDEP " +
				"from GTH_EMPLEADO  EMP " +
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"LEFT JOIN GEN_AREA ARE ON ARE.IDE_GEARE=EDP.IDE_GEARE " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
				"where EMP.IDE_GTEMP in (select DISTINCT(GTH_IDE_GTEMP) from GTH_EMPLEADO_REPORTA where ide_gtemp in ( " +
				"SELECT ide_gtemp FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_SUCU=-1 " +
				"AND IDE_GEARE=-1))");
		tab_tabla3.setCampoPrimaria("IDE_GTEMP");
		tab_tabla3.setLectura(true);
		tab_tabla3.setNumeroTabla(3);
		tab_tabla3.dibujar();

		PanelTabla pat_panel3 = new PanelTabla();
		pat_panel3.setMensajeWarn("REPORTA A : ");
		pat_panel3.setPanelTabla(tab_tabla3);

		ItemMenu itm_reportar_a=new ItemMenu();
		itm_reportar_a.setValue("Reportar A ");
		itm_reportar_a.setMetodo("dibujarDialogoEmpleadoReporta");
		itm_reportar_a.setIcon("ui-icon-person");

		pat_panel3.getMenuTabla().getChildren().add(itm_reportar_a);

		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar("SELECT EPAR.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				" EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true");
		aut_empleado.setMetodoChange("seleccionaEmpleadoReportarse");
		Grid gri_empl_rep=new Grid();
		gri_empl_rep.setColumns(2);
		gri_empl_rep.getChildren().add(new Etiqueta("Emplaedo a Reportarse "));
		gri_empl_rep.getChildren().add(aut_empleado);

		dia_empleado_reporta.setId("dia_empleado_reporta");
		dia_empleado_reporta.setTitle("SELECCION DE EMPELADO A REPORTARSE");
		dia_empleado_reporta.setWidth("50%");
		dia_empleado_reporta.setHeight("25%");
		dia_empleado_reporta.setDialogo(gri_empl_rep);
		dia_empleado_reporta.getBot_aceptar().setMetodo("insertarEmpleadoReportarse");

		agregarComponente(dia_empleado_reporta);




		Division div_vertical = new Division();
		div_vertical.dividir2(pat_panel2, pat_panel3, "65%", "V");

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1,div_vertical , "50%", "H");

		agregarComponente(div_division);
	}

	String ide_emp_rep="";
	public void seleccionaEmpleadoReportarse(SelectEvent evt){
		aut_empleado.onSelect(evt);
		ide_emp_rep=aut_empleado.getValor();
	}
	public void dibujarDialogoEmpleadoReporta(){

		if (tab_tabla1.getTotalFilas()>0 && tab_tabla2.getTotalFilas()>0) {
			TablaGenerica tab_emp=new TablaGenerica();
			if(cmb_opcion_reporta.getValue().equals("1")){
				tab_emp=utilitario.consultar("SELECT EDP.IDE_GTEMP,EDP.ACTIVO_GEEDP " +
						"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR  EDP " +
						"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
						"WHERE EDP.IDE_SUCU="+tab_tabla1.getValor("ide_sucu")+""  +
						"AND EDP.IDE_GEARE="+tab_tabla2.getValor("ide_tabla"));
			}
			if(cmb_opcion_reporta.getValue().equals("2")){
				tab_emp=utilitario.consultar("SELECT EDP.IDE_GTEMP,EDP.ACTIVO_GEEDP " +
						"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR  EDP " +
						"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
						"WHERE EDP.IDE_SUCU="+tab_tabla1.getValor("ide_sucu")+""  +
						"AND EDP.IDE_GEDEP="+tab_tabla2.getValor("ide_tabla"));
				tab_tabla3.ejecutarSql();
			}


			if(cmb_opcion_reporta.getValue().equals("3")){
				tab_emp=utilitario.consultar("SELECT EDP.IDE_GTEMP,EDP.ACTIVO_GEEDP " +
						"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR  EDP " +
						"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
						"WHERE EDP.IDE_SUCU="+tab_tabla1.getValor("ide_sucu")+""  +
						"AND EDP.IDE_GTEMP="+tab_tabla2.getValor("ide_tabla"));	
				tab_tabla3.ejecutarSql();
			}			
			if(tab_emp.getTotalFilas()>0){
				aut_empleado.setValue(null);
				dia_empleado_reporta.dibujar();
			}else {
				utilitario.agregarMensaje("No se puede insertar", "DEPARTAMENTO SELECCIONADO NO CONTIENE EMPLEADOS");
			}
		}
	}

	public void filtrar_por_opcion(){


		if(cmb_opcion_reporta.getValue().equals("1")){
			por_areas();
			cargarTablaEmpleadoReporta();
		}
		if(cmb_opcion_reporta.getValue().equals("2")){
			por_departamento();
			cargarTablaEmpleadoReporta();
		}
		if(cmb_opcion_reporta.getValue().equals("3")){
			por_empleados();
			cargarTablaEmpleadoReporta();
		}


	}


	public String getSqlTablaAreasSucursal(String ide_sucu){
		String sql="select " +
				"ARE.IDE_GEARE AS IDE_TABLA,"+
				"SUC.NOM_SUCU," +
				"ARE.DETALLE_GEARE," +
				"'' as DETALLE_GEDEP," +
				"''  AS NOMBRES_APELLIDOS," +
				"'' AS DOCUMENTO_IDENTIDAD_GTEMP " +
				"from GEN_DEPARTAMENTO_SUCURSAL DSU " +
				"INNER JOIN GEN_AREA ARE ON ARE.IDE_GEARE=DSU.IDE_GEARE " +
				"INNER JOIN SIS_SUCURSAL  SUC ON SUC.IDE_SUCU=DSU.IDE_SUCU " +
				"Where suc.ide_sucu="+ide_sucu+"" +
				"GROUP BY ARE.IDE_GEARE,SUC.NOM_SUCU,ARE.DETALLE_GEARE,DSU.ide_sucu " +
				"HAVING ARE.DETALLE_GEARE IS NOT NULL " +
				"order by DSU.ide_sucu";
		return sql;
	}

	public String getSqlTablaDepartamentosSucursal(String ide_sucu){
		String sql="select " +
				"DEP.IDE_GEDEP AS IDE_TABLA,"+
				"SUC.NOM_SUCU , " +
				"ARE.DETALLE_GEARE , " +
				"DEP.DETALLE_GEDEP ,"+
				"'' AS NOMBRES_APELLIDOS, " +
				"'' as DOCUMENTO_IDENTIDAD_GTEMP " +
				"from GEN_DEPARTAMENTO_SUCURSAL DSU " +
				"LEFT JOIN GEN_AREA ARE ON ARE.IDE_GEARE=DSU.IDE_GEARE " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=DSU.IDE_GEDEP "+
				"LEFT JOIN SIS_SUCURSAL  SUC ON SUC.IDE_SUCU=DSU.IDE_SUCU " +
				"where suc.ide_sucu="+ide_sucu+" " +
				"GROUP BY DEP.IDE_GEDEP,SUC.NOM_SUCU,ARE.DETALLE_GEARE,DSU.ide_sucu,DEP.DETALLE_GEDEP "+
				"HAVING ARE.DETALLE_GEARE IS NOT NULL " +
				"order by DSU.ide_sucu";
		return sql;

	}
	public String getSqlTablaEmpleadosSucursal(String ide_sucu){
		String sql="select EMP.IDE_GTEMP AS IDE_TABLA, " +
				"SUC.NOM_SUCU , " +
				"ARE.DETALLE_GEARE, " +
				"DEP.DETALLE_GEDEP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || " +
				"EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP AS NOMBRES_APELLIDOS, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP " +
				"from GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
				"LEFT JOIN GEN_AREA ARE ON ARE.IDE_GEARE=EDP.IDE_GEARE " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
				"LEFT JOIN SIS_SUCURSAL  SUC ON SUC.IDE_SUCU=EDP.IDE_SUCU " +
				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				"where suc.ide_sucu="+ide_sucu+" " +
				"GROUP BY EMP.IDE_GTEMP,SUC.NOM_SUCU,ARE.DETALLE_GEARE,EDP.ide_sucu, " +
				"EMP.APELLIDO_PATERNO_GTEMP, " +
				"EMP.APELLIDO_MATERNO_GTEMP, " +
				"EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP, " +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"DEP.DETALLE_GEDEP " +
				"HAVING ARE.DETALLE_GEARE IS NOT NULL AND EMP.APELLIDO_PATERNO_GTEMP IS NOT NULL " +
				"AND EMP.APELLIDO_MATERNO_GTEMP  IS NOT NULL " +
				"AND EMP.PRIMER_NOMBRE_GTEMP  IS NOT NULL " +
				"AND EMP.SEGUNDO_NOMBRE_GTEMP IS NOT NULL " +
				"order by EDP.ide_sucu";

		return sql;

	}	

	public void seleccionarTablaSucursal(AjaxBehaviorEvent evt) {
		tab_tabla1.seleccionarFila(evt);
		seleccionarTabla1();
	}
	public void seleccionarTablaSucursal(SelectEvent evt) {
		tab_tabla1.seleccionarFila(evt);
		seleccionarTabla1();	
	}

	public void cargarTablaEmpleadoReporta(){
		String sql="select " +
				"EMP.IDE_GTEMP," +
				"EMP.DOCUMENTO_IDENTIDAD_GTEMP," +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || " +
				"EMP.APELLIDO_MATERNO_GTEMP || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"EMP.SEGUNDO_NOMBRE_GTEMP as NOMBRES, " +
				"ARE.DETALLE_GEARE, " +
				"DEP.DETALLE_GEDEP " +
				"from GTH_EMPLEADO  EMP " +
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"LEFT JOIN GEN_AREA ARE ON ARE.IDE_GEARE=EDP.IDE_GEARE " +
				"LEFT JOIN GEN_DEPARTAMENTO DEP ON DEP.IDE_GEDEP=EDP.IDE_GEDEP " +
				"where EMP.IDE_GTEMP in (select DISTINCT(GTH_IDE_GTEMP) from GTH_EMPLEADO_REPORTA where ide_gtemp in ( " +
				"SELECT ide_gtemp FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_SUCU="+tab_tabla1.getValor("ide_sucu")+" " ;
		if (cmb_opcion_reporta.getValue().equals("1")){
			sql+="AND IDE_GEARE="+tab_tabla2.getValor("ide_tabla")+" ";
		}
		if (cmb_opcion_reporta.getValue().equals("2")){
			sql+="AND IDE_GEDEP="+tab_tabla2.getValor("ide_tabla")+" ";
		}
		if (cmb_opcion_reporta.getValue().equals("3")){
			sql+="AND IDE_GTEMP="+tab_tabla2.getValor("ide_tabla")+" ";
		}
		sql+="))";
		
				
		tab_tabla3.setSql(sql);
		tab_tabla3.ejecutarSql();
		System.out.println("sql "+sql);
	}

	private void seleccionarTabla1(){
		if(tab_tabla1.isFocus()){
			if (cmb_opcion_reporta.getValue()!=null){
				if(cmb_opcion_reporta.getValue().equals("1")){
					por_areas();
					cargarTablaEmpleadoReporta();
				}
				if(cmb_opcion_reporta.getValue().equals("2")){

					por_departamento();
					cargarTablaEmpleadoReporta();

				}
				if(cmb_opcion_reporta.getValue().equals("3")){
					por_empleados();
					cargarTablaEmpleadoReporta();
				}
			}else{
				utilitario.agregarMensajeInfo("DEBE ESCOGER UNA OPCION DE BUSQUEDA", "");
			}
		}


	}




	public void seleccionarTablaFiltrada(AjaxBehaviorEvent evt) {
		tab_tabla2.seleccionarFila(evt);
		seleccionarTabla2();
	}
	public void seleccionarTablaFiltrada(SelectEvent evt) {
		tab_tabla2.seleccionarFila(evt);
		seleccionarTabla2();

	}


	private void seleccionarTabla2(){

		if(cmb_opcion_reporta.getValue().equals("1")){
			cargarTablaEmpleadoReporta();
		}

		if(cmb_opcion_reporta.getValue().equals("2")){
			cargarTablaEmpleadoReporta();
		}

		if(cmb_opcion_reporta.getValue().equals("3")){
			cargarTablaEmpleadoReporta();
		}

	}


	public void insertarEmpleadoReportarse(){
		System.out.println("cmb "+cmb_opcion_reporta.getValue());
		if(cmb_opcion_reporta.getValue().equals("1")){
			if(tab_tabla2.getTotalFilas()>0){
				if (aut_empleado.getValor()!=null){
					TablaGenerica tab_empleados= utilitario.consultar("SELECT * FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR  EDP " +
							"WHERE EDP.IDE_SUCU="+tab_tabla1.getValor("IDE_SUCU")+" " +
							"AND EDP.IDE_GEARE="+tab_tabla2.getValor("IDE_TABLA")+"" );
					String empleado_reportarse=aut_empleado.getValor(); 
					Tabla tab_empleado_reporta=new Tabla();
					tab_empleado_reporta.setTabla("GTH_EMPLEADO_REPORTA", "IDE_GTEMR", -1);
					tab_empleado_reporta.setCondicion("IDE_GTEMR=-1");
					tab_empleado_reporta.ejecutarSql();
					for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
						tab_empleado_reporta.insertar();
						tab_empleado_reporta.setValor("IDE_GTEMP", tab_empleados.getValor(i,"IDE_GTEMP"));
						tab_empleado_reporta.setValor("GTH_IDE_GTEMP", ide_emp_rep);
						tab_empleado_reporta.setValor("ACTIVO_GTEMR", "1");
					}
					if (tab_empleado_reporta.getTotalFilas()>0){
						tab_empleado_reporta.guardar();
						dia_empleado_reporta.cerrar();
						guardarPantalla();
						if (cmb_opcion_reporta.getValue().equals("1")){
							por_areas();
							cargarTablaEmpleadoReporta();
						}
						if (cmb_opcion_reporta.getValue().equals("2")){
							por_departamento();
							cargarTablaEmpleadoReporta();
						}
						if (cmb_opcion_reporta.getValue().equals("3")){
							por_empleados();
							cargarTablaEmpleadoReporta();
						}
					}
				}else{
					utilitario.agregarMensajeInfo("Debe seleccionar un Empelado al cual se van a Reportar", "");
				}
			}
		}

		if(cmb_opcion_reporta.getValue().equals("2")){
			if(tab_tabla2.getTotalFilas()>0){

				TablaGenerica tab_empleados= utilitario.consultar("SELECT * FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR  EDP " +
						"WHERE EDP.IDE_SUCU="+tab_tabla1.getValor("IDE_SUCU")+" " +
						"AND   EDP.IDE_GEDEP="+tab_tabla2.getValor("IDE_TABLA")+"" );
				String empleado_reportarse=aut_empleado.getValor(); 
				Tabla tab_empleado_reporta=new Tabla();
				tab_empleado_reporta.setTabla("GTH_EMPLEADO_REPORTA", "IDE_GTEMR", -1);
				tab_empleado_reporta.setCondicion("IDE_GTEMR=-1");
				tab_empleado_reporta.ejecutarSql();
				for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
					tab_empleado_reporta.insertar();
					tab_empleado_reporta.setValor("IDE_GTEMP", tab_empleados.getValor(i,"IDE_GTEMP"));
					tab_empleado_reporta.setValor("GTH_IDE_GTEMP", ide_emp_rep);
					tab_empleado_reporta.setValor("ACTIVO_GTEMR", "1");
				}
				if (tab_empleado_reporta.getTotalFilas()>0){
					tab_empleado_reporta.guardar();
					dia_empleado_reporta.cerrar();
					guardarPantalla();
					if (cmb_opcion_reporta.getValue().equals("1")){
						por_areas();
						cargarTablaEmpleadoReporta();
					}
					if (cmb_opcion_reporta.getValue().equals("2")){
						por_departamento();
						cargarTablaEmpleadoReporta();
					}
					if (cmb_opcion_reporta.getValue().equals("3")){
						por_empleados();
						cargarTablaEmpleadoReporta();
					}
				}

			}

		}


		if(cmb_opcion_reporta.getValue().equals("3")){
			if(tab_tabla2.getTotalFilas()>0){

				TablaGenerica tab_empleados= utilitario.consultar("SELECT * FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR  EDP " +
						"WHERE EDP.IDE_SUCU="+tab_tabla1.getValor("IDE_SUCU")+" " +
						"AND   EDP.IDE_GTEMP="+tab_tabla2.getValor("IDE_TABLA")+"" );
				String empleado_reportarse=aut_empleado.getValor(); 
				Tabla tab_empleado_reporta=new Tabla();
				tab_empleado_reporta.setTabla("GTH_EMPLEADO_REPORTA", "IDE_GTEMR", -1);
				tab_empleado_reporta.setCondicion("IDE_GTEMR=-1");
				tab_empleado_reporta.ejecutarSql();
				for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
					tab_empleado_reporta.insertar();
					tab_empleado_reporta.setValor("IDE_GTEMP", tab_empleados.getValor(i,"IDE_GTEMP"));
					tab_empleado_reporta.setValor("GTH_IDE_GTEMP", ide_emp_rep);
					tab_empleado_reporta.setValor("ACTIVO_GTEMR", "1");
				}
				if (tab_empleado_reporta.getTotalFilas()>0){
					tab_empleado_reporta.guardar();
					dia_empleado_reporta.cerrar();
					guardarPantalla();
					if (cmb_opcion_reporta.getValue().equals("1")){
						por_areas();
						cargarTablaEmpleadoReporta();
					}
					if (cmb_opcion_reporta.getValue().equals("2")){
						por_departamento();
						cargarTablaEmpleadoReporta();
					}
					if (cmb_opcion_reporta.getValue().equals("3")){
						por_empleados();
						cargarTablaEmpleadoReporta();
					}
				}

			}
		}
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
	}


	@Override
	public void guardar() {
		// TODO Auto-generated method stub
	}


	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if(tab_tabla3.isFocus())
		{
			tab_tabla3.eliminar();

		}

	}



	public void por_areas(){


		tab_tabla2.setSql(getSqlTablaAreasSucursal(tab_tabla1.getValor("ide_sucu")));
		tab_tabla2.getColumna("NOM_SUCU").setVisible(false);
		tab_tabla2.getColumna("NOMBRES_APELLIDOS").setVisible(false);
		tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setVisible(false);
		tab_tabla2.getColumna("DETALLE_GEDEP").setVisible(false);
		tab_tabla2.getColumna("IDE_TABLA").setLectura(true);

		tab_tabla2.ejecutarSql();		



	}



	public void por_departamento(){

		tab_tabla2.setSql(getSqlTablaDepartamentosSucursal(tab_tabla1.getValor("ide_sucu")));
		tab_tabla2.getColumna("NOM_SUCU").setVisible(false);
		tab_tabla2.getColumna("NOMBRES_APELLIDOS").setVisible(false);
		tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setVisible(false);
		tab_tabla2.getColumna("DETALLE_GEDEP").setVisible(true);
		tab_tabla2.getColumna("IDE_TABLA").setLectura(true);
		tab_tabla2.ejecutarSql();





	}

	public void por_empleados(){


		tab_tabla2.setSql(getSqlTablaEmpleadosSucursal(tab_tabla1.getValor("ide_sucu")));
		tab_tabla2.getColumna("NOM_SUCU").setVisible(false);
		tab_tabla2.getColumna("NOMBRES_APELLIDOS").setVisible(true);
		tab_tabla2.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setVisible(true);
		tab_tabla2.getColumna("DETALLE_GEDEP").setVisible(true);		
		tab_tabla2.getColumna("IDE_TABLA").setLectura(true);
		tab_tabla2.ejecutarSql();



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


	public Tabla getTab_tabla3() {
		return tab_tabla3;
	}


	public void setTab_tabla3(Tabla tab_tabla3) {
		this.tab_tabla3 = tab_tabla3;
	}

	public Dialogo getDia_empleado_reporta() {
		return dia_empleado_reporta;
	}

	public void setDia_empleado_reporta(Dialogo dia_empleado_reporta) {
		this.dia_empleado_reporta = dia_empleado_reporta;
	}

	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}




}
