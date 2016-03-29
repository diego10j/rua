package paq_nomina.ejb;



import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import sistema.aplicacion.Utilitario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;

import net.sf.jasperreports.charts.util.SvgChartRendererFactory;



/**
 * @author byron
 *
 */
@Stateless
public class ServicioEscenariosNomina {

	public final static String P_NRH_RUBRO_IMPORTADO = "2";
	public final static String P_NRH_RUBRO_FORMULA = "0";
	public final static String P_NRH_RUBRO_TECLADO = "1";
	public final static String P_NRH_RUBRO_CONSTANTE = "3";
	public static String P_NRH_RUBRO_REMUNERACION_UNIFICADA="";

	@EJB
	private ServicioEscenariosNomina serv_nomina;

	private Utilitario utilitario = new Utilitario();
	private TablaGenerica tab_deta_rubros_rol;

	public TablaGenerica getEmpleadosEscenario(String ide_gttem){
		TablaGenerica tab_emp=utilitario.consultar("select EMP.DOCUMENTO_IDENTIDAD_GTEMP AS IDE_GEEDP, " +
				"APELLIDO_PATERNO_GTEMP || ' '|| " +
				"APELLIDO_MATERNO_GTEMP || ' '|| " +
				"PRIMER_NOMBRE_GTEMP || ' '|| " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES, " +
				"EDP.RMU_GEEDP, " +
				"EDP.IDE_GEGRO, " +
				"EDP.IDE_GECAF, " +
				"EDP.IDE_GTTCO, " +
				"DTN.IDE_NRDTN, " +
				"EDP.IDE_SUCU " +
				" from GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
				"INNER JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=EDP.IDE_GTTEM " +
				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_GTTEM=TEM.IDE_GTTEM " +
				"AND DTN.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_normal")+" " +
				"WHERE EDP.IDE_GTTEM IN ("+ide_gttem+") " +
				"AND EDP.ACTIVO_GEEDP=TRUE " +
				"ORDER BY NOMBRES");
		return tab_emp;
	}

//	public TablaGenerica getEmpleadosEscenario(String ide_gttem,double porcentaje){
//		TablaGenerica tab_emp=utilitario.consultar("select EMP.DOCUMENTO_IDENTIDAD_GTEMP AS IDE_GEEDP, " +
//				"APELLIDO_PATERNO_GTEMP || ' '|| " +
//				"APELLIDO_MATERNO_GTEMP || ' '|| " +
//				"PRIMER_NOMBRE_GTEMP || ' '|| " +
//				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES, " +
//				"(EDP.RMU_GEEDP+((EDP.RMU_GEEDP * "+porcentaje+")/100)) as RMU_GEEDP, " +
//				"EDP.IDE_GEGRO, " +
//				"EDP.IDE_GECAF, " +
//				"EDP.IDE_GTTCO, " +
//				"DTN.IDE_NRDTN, " +
//				"EDP.IDE_SUCU " +
//				" from GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP " +
//				"INNER JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=EDP.IDE_GTTEM " +
//				"INNER JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EDP.IDE_GTEMP " +
//				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_GTTEM=TEM.IDE_GTTEM " +
//				"AND DTN.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_normal")+" " +
//				"WHERE EDP.IDE_GTTEM IN ("+ide_gttem+") " +
//				"AND EDP.ACTIVO_GEEDP=TRUE " +
//				"ORDER BY NOMBRES");
//		return tab_emp;
//	}
	
	public TablaGenerica getEmpleadosEscenario(String ide_nrroe,double porcentaje){
	
		TablaGenerica tab_emp=utilitario.consultar("select " +
				"	DOCUMENTO_IDENTIDAD_NRROE AS IDE_GEEDP, " +
				"	APELLIDO_NOMBRE_NRROE AS NOMBRES, " +
				"	(RMU_ANTERIOR_NRROE+((RMU_ANTERIOR_NRROE * "+porcentaje+")/100)) as RMU_GEEDP, " +
				"	IDE_NRDTN " +
				"	 from NRH_ROL_DETALLE_ESCENARIO RDOE " +
				"	where RDOE.IDE_NRROE="+ide_nrroe+" " +
				"   order by NOMBRES");
		return tab_emp;
	}
							
	/**'
	 * Retorna una tabla vacia del Detalle de Rol
	 * @return
	 */
	private TablaGenerica getDetalleRubrosRolVacia(){

		TablaGenerica tab_deta_rol_vacia=new TablaGenerica();
		//		tab_deta_rol_vacia.setTabla("NRH_DETALLE_ROL", "IDE_NRDRO", -1);
		tab_deta_rol_vacia.setSql("select IDE_NRDRO,IDE_NRROL,IDE_GEEDP,'' as IDE_NRRUB,IDE_NRDER,VALOR_NRDRO,'' AS FECHA_INICIAL_NRDER, " +
				"'' AS FECHA_FINAL_NRDER,'' AS FECHA_PAGO_NRDER " +
				"from NRH_DETALLE_ROL WHERE IDE_NRROL=-1");
		tab_deta_rol_vacia.setCampoPrimaria("IDE_NRDRO");
		tab_deta_rol_vacia.setNumeroTabla(-1);

		//		tab_deta_rol_vacia.setCondicion("IDE_NRDRO=-1");
		tab_deta_rol_vacia.ejecutarSql();
		return tab_deta_rol_vacia;
	}

	
	public TablaGenerica generarRol(String sql_emp,String ide_nrroe ){
		long inicio = System.currentTimeMillis();		


		P_NRH_RUBRO_REMUNERACION_UNIFICADA=utilitario.getVariable("p_nrh_rubro_remuneracion_unificada");
		

		
		
				
//		TablaGenerica tab_rubros = getRubrosTipoNominaSql(tab_empleados_departamento.getSql());
//		System.out.println("tab rubros "+tab_rubros.getSql());		

		String s="select * from NRH_DETALLE_TIPO_NOMINA where IDE_NRDTN in (  select a.ide_nrdtn from ( "+
				""+getRubrosTipoNominaSql(sql_emp).getSql()+" )a group by a.ide_nrdtn )";
		System.out.println("dtn "+s );
		TablaGenerica tab_det_tip_nom=utilitario.consultar(s);
		
//		
		for (int i = 0; i < tab_det_tip_nom.getTotalFilas(); i++) {
		
			String str_sql_emp="select * from ( "+
			""+sql_emp+" " +
			")a where a.ide_nrdtn in ( "+
			"select ide_nrdtn from NRH_DETALLE_TIPO_NOMINA where IDE_GTTEM in ( " +
					"select IDE_GTTEM from NRH_DETALLE_TIPO_NOMINA where IDE_NRDTN="+tab_det_tip_nom.getValor(i, "IDE_NRDTN")+") " +
					"and IDE_NRTIN=0)";
			
			TablaGenerica tab_empleados_departamento=utilitario.consultar(str_sql_emp);
			System.out.println("sql emp "+tab_empleados_departamento.getSql());
			
			int indice=0;
			tab_deta_rubros_rol=getDetalleRubrosRolVacia();
			
			
			TablaGenerica tab_rubros = getRubrosTipoNomina1(tab_det_tip_nom.getValor(i, "IDE_NRDTN"));
			System.out.println("tab rubros "+tab_rubros.getSql());		

			for (int j = 0; j < tab_empleados_departamento.getTotalFilas(); j++) {

				calcularRolIndividual(tab_rubros,tab_empleados_departamento.getValor(j, "IDE_GEEDP"),tab_empleados_departamento.getValor(j, "RMU_GEEDP"),ide_nrroe);

			}
		}
		

		long fin = System.currentTimeMillis();
		System.out.println("======== Tiempo total (TODO EL PROCESO) guardar pantalla : " + (fin - inicio));
		return tab_deta_rubros_rol;
	}
	

	
	private TablaGenerica getRubrosTipoNominaSql(String sql_emp){
		//		TablaGenerica tab_rubros=utilitario.consultar("SELECT * FROM NRH_DETALLE_RUBRO WHERE IDE_NRDTN="+IDE_NRDTN+" AND ACTIVO_NRDER=true ORDER BY ORDEN_NRDER DESC");
		TablaGenerica tab_rubros=utilitario.consultar("SELECT ide_nrder,RUB.ide_nrrub,IDE_NRFOC,dtn.IDE_NRDTN,FORMULA_NRDER,FECHA_INICIAL_NRDER, " +
				"FECHA_FINAL_NRDER,FECHA_PAGO_NRDER,ORDEN_NRDER " +
				"FROM NRH_DETALLE_RUBRO DER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DER.IDE_NRDTN=DTN.IDE_NRDTN "+
				"WHERE DTN.IDE_GTTEM in (" +
				"SELECT IDE_GTTEM FROM NRH_DETALLE_TIPO_NOMINA WHERE IDE_NRDTN IN ( "+
				"select a.IDE_NRDTN from " +
				"( "+
				""+sql_emp+"" +
				")a)) " +
				"AND DER.ACTIVO_NRDER=TRUE " +
				"AND DTN.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_escenario")+" "+
				"ORDER BY DER.ORDEN_NRDER DESC ");
		return tab_rubros;
	}
	

	

	private TablaGenerica getRubrosTipoNomina1(String IDE_NRDTN){
		//		TablaGenerica tab_rubros=utilitario.consultar("SELECT * FROM NRH_DETALLE_RUBRO WHERE IDE_NRDTN="+IDE_NRDTN+" AND ACTIVO_NRDER=true ORDER BY ORDEN_NRDER DESC");
		TablaGenerica tab_rubros=utilitario.consultar("SELECT ide_nrder,RUB.ide_nrrub,IDE_NRFOC,dtn.IDE_NRDTN,FORMULA_NRDER,FECHA_INICIAL_NRDER, " +
				"FECHA_FINAL_NRDER,FECHA_PAGO_NRDER,ORDEN_NRDER " +
				"FROM NRH_DETALLE_RUBRO DER " +
				"INNER JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DER.IDE_NRRUB " +
				"INNER JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DER.IDE_NRDTN=DTN.IDE_NRDTN "+
				"WHERE DTN.IDE_NRDTN in ("+IDE_NRDTN+") " +
				"AND DER.ACTIVO_NRDER=TRUE " +
				"AND DTN.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_escenario")+" "+
				"ORDER BY DER.ORDEN_NRDER DESC ");
		return tab_rubros;
	}
	
	public void calcularRolIndividual(TablaGenerica tab_rubros,String ide_geedp,String rmu,String ide_nrroe){


		// insertarmos todos los rubros del empleado e importamos los valores unicamente de los rubros de forma de calculo (IMPORTADO)
		insertarDetallesRolEmpleado(tab_rubros,rmu,ide_geedp);


		int indice=tab_rubros.getTotalFilas();


		despejarFormulasRol(indice, ide_geedp, "");


		String str_valor="";
		double dou_total_anual=0;
		for (int l = 0; l < tab_rubros.getTotalFilas(); l++) {
			
			if (tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO").isEmpty()){
				
				try {
				BigDecimal big_valor=new BigDecimal(Double.parseDouble(tab_deta_rubros_rol.getValor(l,"VALOR_NRDRO")));
				big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);
				str_valor=big_valor+"";
				} catch (Exception e) {
					str_valor="0.0";
				}
			}else{
				str_valor="0.0";
			}
			
			if (tab_deta_rubros_rol.getValor(l,"IDE_NRRUB").equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_remuneracion_unificada"))){
				String str_update="update NRH_ROL_DETALLE_ESCENARIO set RMU_ESCENARIO_NRROE="+str_valor+" where DOCUMENTO_IDENTIDAD_NRROE LIKE '"+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+"' and IDE_NRROE="+ide_nrroe;
				dou_total_anual=dou_total_anual+Double.parseDouble(str_valor);
				utilitario.getConexion().ejecutarSql(str_update);
			}
			if (tab_deta_rubros_rol.getValor(l,"IDE_NRRUB").equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_aportes_personales"))){
				String str_update="update NRH_ROL_DETALLE_ESCENARIO set APORTE_PATRONAL_NRROE="+str_valor+" where DOCUMENTO_IDENTIDAD_NRROE LIKE '"+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+"' and IDE_NRROE="+ide_nrroe;
				dou_total_anual=dou_total_anual+Double.parseDouble(str_valor);
				utilitario.getConexion().ejecutarSql(str_update);
			}
			if (tab_deta_rubros_rol.getValor(l,"IDE_NRRUB").equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_proviciones_d4"))){
				String str_update="update NRH_ROL_DETALLE_ESCENARIO set DECIMO_CUARTO_NRROE="+str_valor+" where DOCUMENTO_IDENTIDAD_NRROE LIKE '"+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+"' and IDE_NRROE="+ide_nrroe;
				dou_total_anual=dou_total_anual+Double.parseDouble(str_valor);
				utilitario.getConexion().ejecutarSql(str_update);
			}
			if (tab_deta_rubros_rol.getValor(l,"IDE_NRRUB").equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_proviciones_d3"))){
				String str_update="update NRH_ROL_DETALLE_ESCENARIO set DECIMO_TERCERO_NRROE="+str_valor+" where DOCUMENTO_IDENTIDAD_NRROE LIKE '"+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+"' and IDE_NRROE="+ide_nrroe;
				utilitario.getConexion().ejecutarSql(str_update);
				dou_total_anual=dou_total_anual+Double.parseDouble(str_valor);
			}
			if (tab_deta_rubros_rol.getValor(l,"IDE_NRRUB").equalsIgnoreCase(utilitario.getVariable("p_nrh_rubro_valor_fondos_reserva"))){
				String str_update="update NRH_ROL_DETALLE_ESCENARIO set FONDO_RESREVA_NRROE="+str_valor+" where DOCUMENTO_IDENTIDAD_NRROE LIKE '"+tab_deta_rubros_rol.getValor(l,"IDE_GEEDP")+"' and IDE_NRROE="+ide_nrroe;
				utilitario.getConexion().ejecutarSql(str_update);
				dou_total_anual=dou_total_anual+Double.parseDouble(str_valor);
			}
			
		}
		dou_total_anual=dou_total_anual*12;
		BigDecimal big_valor_anual=new BigDecimal(dou_total_anual);
		big_valor_anual=big_valor_anual.setScale(2, RoundingMode.HALF_UP);
		
		
		String str_update="update NRH_ROL_DETALLE_ESCENARIO set TOTAL_ANUAL_NRROE="+big_valor_anual+" where DOCUMENTO_IDENTIDAD_NRROE LIKE '"+tab_deta_rubros_rol.getValor(0,"IDE_GEEDP")+"' and IDE_NRROE="+ide_nrroe;
		utilitario.getConexion().ejecutarSql(str_update);
		
	}

	public void insertarDetallesRolEmpleado(TablaGenerica tab_rubros,String rmu,String ide_geedp){
		for (int j = 0; j < tab_rubros.getTotalFilas(); j++) {
			tab_deta_rubros_rol.insertar();
			tab_deta_rubros_rol.setValor("FECHA_INICIAL_NRDER",tab_rubros.getValor(j, "FECHA_INICIAL_NRDER"));
			tab_deta_rubros_rol.setValor("FECHA_FINAL_NRDER",tab_rubros.getValor(j, "FECHA_FINAL_NRDER"));
			tab_deta_rubros_rol.setValor("FECHA_PAGO_NRDER",tab_rubros.getValor(j, "FECHA_PAGO_NRDER"));
			tab_deta_rubros_rol.setValor("VALOR_NRDRO",tab_rubros.getValor(j, "FORMULA_NRDER"));
			tab_deta_rubros_rol.setValor("IDE_NRDER",tab_rubros.getValor(j, "IDE_NRDER"));
			tab_deta_rubros_rol.setValor("IDE_NRRUB",tab_rubros.getValor(j, "IDE_NRRUB"));
			tab_deta_rubros_rol.setValor("ide_geedp",ide_geedp);
//			tab_deta_rubros_rol.setValor("IDE_NRROL",ide_nrrol);
			if(tab_rubros.getValor(j, "IDE_NRFOC").equalsIgnoreCase(P_NRH_RUBRO_IMPORTADO)){
				String str_valor=importarRubro(tab_rubros.getValor(j, "IDE_NRRUB"),rmu);
				tab_deta_rubros_rol.setValor("VALOR_NRDRO",str_valor);				
			}
		}
	}
	
	public String importarRubro(String ide_nrrub,String RMU){

		// importacion RMU del empleado				
		if(ide_nrrub.equals(P_NRH_RUBRO_REMUNERACION_UNIFICADA)){					
			return RMU;
		}		// importacion RMU del empleado				
		return null;
	}
	

	/**
	 * Despeja las formulas del detalle del rol
	 */

	public void despejarFormulasRol(int indice_final,String ide_geedp,String fecha_rol){		
		String formula_reemplazada="";
		for (int i = 0; i < indice_final; i++) {
			if (tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO")!=null && !tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO").isEmpty()){
				if (tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO").startsWith("=")){
					formula_reemplazada = getValorFormula(indice_final,fecha_rol,ide_geedp,tab_deta_rubros_rol.getValor(i, "IDE_NRDER"),tab_deta_rubros_rol.getValor(i, "VALOR_NRDRO"),tab_deta_rubros_rol.getValor(i, "FECHA_INICIAL_NRDER"),tab_deta_rubros_rol.getValor(i, "FECHA_FINAL_NRDER"),tab_deta_rubros_rol.getValor(i, "FECHA_PAGO_NRDER"));
					try {
						tab_deta_rubros_rol.modificar(i);
						
						BigDecimal big_valor=new BigDecimal(utilitario.evaluarExpresion(formula_reemplazada));
						big_valor=big_valor.setScale(2, RoundingMode.HALF_UP);

						tab_deta_rubros_rol.setValor(i,"VALOR_NRDRO",big_valor+"");
						
					} catch (Exception e) {
						// TODO: handle exception
						tab_deta_rubros_rol.modificar(i);
						tab_deta_rubros_rol.setValor(i,"VALOR_NRDRO","0.0");
					}
				}		
			}
		}
	}

	private String getValorFormula(int total_filas_detalle_rol,String fecha_rol,String ide_geedp,String ide_nrder_formula,String formula_rhcru,String fecha_inicial_nrder,String fecha_final_nrder,String fecha_pago_nrder) {
		if (formula_rhcru!=null && !formula_rhcru.isEmpty()){
			return getFormulaReemplazada(total_filas_detalle_rol,fecha_rol,ide_geedp,ide_nrder_formula,formula_rhcru,fecha_inicial_nrder,fecha_final_nrder,fecha_pago_nrder,false);
		}
		return "0";
	}
	

	private String getFormulaReemplazada(int total_filas_detalle,String fecha_rol,String ide_geedp,String ide_nrder_formula,String formula_nrder,String fecha_inicial_nrder,String fecha_final_nrder,String fecha_pago_nrder ,boolean con_nombre_formula) {
		String formula = formula_nrder;
		String formula_reemplazada = "";
		String nueva_formula = "";
		int band=0;
		int indice = 0;
		int indicador_parentesis = 0;
		String parentesis;
		do {
			indice = formula.indexOf("[");
			if (indice != -1) {
				do {
					if (band==0){
						nueva_formula = formula.substring(1, indice + 1);
						band=1;
					}else{
						nueva_formula = formula.substring(indice, indice + 1);	
					}
					formula = formula.substring(indice + 1);
					parentesis = formula.substring(0, 1);
					if (parentesis.equals("[")) {
						indicador_parentesis = 1;
						indice = formula.indexOf("[");
						formula_reemplazada = formula_reemplazada.concat(nueva_formula);
					} else {
						indicador_parentesis = 0;
					}
				} while (indicador_parentesis == 1);

				String ide_nrder = getRecuperarNumeroFormula(formula,con_nombre_formula);


				if (!con_nombre_formula) {
					String valor_ide="";
					if (ide_nrder.length()>=3){
						if (ide_nrder.substring(0, 3).equalsIgnoreCase("sum")){
							formula=formula.substring(formula.indexOf("[")+1, formula.length());
							formula=formula.replaceFirst("]", "");
							valor_ide=getBuscarValorRubro(ide_nrder, ide_nrder_formula, ide_geedp, fecha_rol,fecha_inicial_nrder,fecha_final_nrder,fecha_pago_nrder);
						}
					}
					if (valor_ide.isEmpty()){	
						valor_ide = getBuscarValorRubro(total_filas_detalle,ide_nrder);
					}
					if (valor_ide == null || valor_ide.isEmpty()) {
						valor_ide = "0";
					}
					nueva_formula = nueva_formula.concat(valor_ide);
				} else {
					String nombre_ide="";

					if (formula.length()>=3){	
						if (formula.substring(0, 3).equalsIgnoreCase("sum")){
							nueva_formula=nueva_formula.concat(formula.substring(0, formula.indexOf("[")+1));
							formula=formula.substring(formula.indexOf("[")+1, formula.length());
						}
					}

					try {
						nombre_ide =getRubro(getDetalleRubro(ide_nrder).getValor("IDE_NRRUB")).getValor("DETALLE_NRRUB");
					} catch (Exception e) {
						// TODO: handle exception
					}
					if (nombre_ide == null || nombre_ide.isEmpty()) {
						nombre_ide = "";
					}
					nueva_formula = nueva_formula.concat(nombre_ide);

				}

				int ind = formula.indexOf("[");
				if (ind != -1) {
					nueva_formula = nueva_formula.concat(formula.substring(formula.indexOf("]"), ind));
				} else {
					nueva_formula = nueva_formula.concat(formula.substring(formula.indexOf("]")));
				}
				formula_reemplazada = formula_reemplazada.concat(nueva_formula);

			}
		} while (indice != -1);
		return formula_reemplazada;
	}

	private String getRecuperarNumeroFormula(String formula,boolean en_letras) {
		if (formula.length()>=3){	
			if (formula.substring(0, 3).equalsIgnoreCase("sum")){
				if (!en_letras){
					return formula.substring(0, (formula.indexOf("]")+1));
				}else{
					return formula.substring(formula.indexOf("[")+1, formula.indexOf("]")); 
				}
			}
		}
		return formula.substring(0, formula.indexOf("]"));

	}
	
	/** busca el Detalle de tipo nomina y retorna una tabla
	 * @param ide_nrdtn_a_buscar : ide a buscar en la tabla 
	 * 
	 * @return si no existe datos retorna null 
	 */
	public TablaGenerica getDetalleRubro(String ide_nrder){
		TablaGenerica tab_dru=utilitario.consultar("select * from NRH_DETALLE_RUBRO where IDE_NRDER="+ide_nrder+"");
		if (tab_dru.getTotalFilas()>0){			
			return tab_dru;			
		}
		return null;
	}

	/**
	 * @param parametro :recibe el parametro de retorno (nombre de la columna) de la tabla NRH_RUBROS que requerimos
	 * @param ide_nrrub :recibe el ide del rubro (ide_nrrub) a buscar en la tabla nrh_rubros   
	 * @return : retorna el valor que contiene la columna de parametro que enviamos
	 * 
	 */
	public TablaGenerica getRubro(String ide_nrrub) {
		if (ide_nrrub != null && !ide_nrrub.isEmpty()) {
			TablaGenerica tab_rubros = utilitario.consultar("SELECT DISTINCT RUB.IDE_NRRUB,IDE_NRFOC,IDE_NRTIR,DETALLE_NRRUB,ANTICIPO_NRRUB,ACTIVO_NRRUB ,ORDEN_NRDER " +
					"FROM NRH_RUBRO rub " +
					"INNER join NRH_DETALLE_RUBRO der on DER.ide_nrrub=RUB.IDE_NRRUB " +
					"WHERE der.IDE_NRRUB IN ("+ide_nrrub+") " +
					"ORDER BY DER.ORDEN_NRDER");

			if (tab_rubros.getTotalFilas() > 0) {				
				return tab_rubros;				 
			}        
		}
		return null;
	}
	
	private String getBuscarValorRubro(int total_filas_detalle,String ide_nrder) {
		for (int i = 0; i < total_filas_detalle; i++) {
			if (tab_deta_rubros_rol.getValor(i, "ide_nrder").equals(ide_nrder)) {
				return tab_deta_rubros_rol.getValor(i, "valor_nrdro");
			}
		}

		return null;
	}

	private String getBuscarValorRubro(String formula,String ide_nrder_formula,String ide_geedp,String fecha_rol,String str_fecha_inicio_nrder,String str_fecha_fin_nrder,String str_fecha_pago_nrder) {

		if (formula !=null && !formula.isEmpty() && formula.length()>=3){
			if (formula.substring(0, 3).equalsIgnoreCase("sum")){

				if (str_fecha_inicio_nrder!=null && !str_fecha_inicio_nrder.isEmpty()
						&& str_fecha_fin_nrder!=null && !str_fecha_fin_nrder.isEmpty()){

					int indice=formula.indexOf("[");
					String ide_nrder_suma=formula.substring(indice+1, formula.indexOf("]"));

					int anio_actual=utilitario.getAnio(fecha_rol);
					if (str_fecha_inicio_nrder.indexOf("/")!=-1){
						String num_anios=str_fecha_inicio_nrder.substring(str_fecha_inicio_nrder.indexOf("/")+1, str_fecha_inicio_nrder.length());
						String fecha=str_fecha_inicio_nrder.substring(0,str_fecha_inicio_nrder.indexOf("/"));								
						int int_num_anios_fec_ini=0;	
						try {
							int_num_anios_fec_ini=Integer.parseInt(num_anios);
						} catch (Exception e) {
							// TODO: handle exception
						}
						fecha=(anio_actual+int_num_anios_fec_ini)+"-"+fecha;
						str_fecha_inicio_nrder=fecha;
					}
					if (str_fecha_fin_nrder.indexOf("/")!=-1){
						String num_anios=str_fecha_fin_nrder.substring(str_fecha_fin_nrder.indexOf("/")+1, str_fecha_fin_nrder.length());
						String fecha=str_fecha_fin_nrder.substring(0,str_fecha_fin_nrder.indexOf("/"));								
						int int_num_anios_fec_fin=0;	
						try {
							int_num_anios_fec_fin=Integer.parseInt(num_anios);
						} catch (Exception e) {
							// TODO: handle exception
						}
						fecha=(anio_actual+int_num_anios_fec_fin)+"-"+fecha;
						str_fecha_fin_nrder=fecha;
					}
					if (str_fecha_pago_nrder!=null && !str_fecha_pago_nrder.isEmpty()){
						if (str_fecha_pago_nrder.indexOf("/")!=-1){
							String num_anios=str_fecha_pago_nrder.substring(str_fecha_pago_nrder.indexOf("/")+1, str_fecha_pago_nrder.length());
							String fecha=str_fecha_pago_nrder.substring(0,str_fecha_pago_nrder.indexOf("/"));								
							int int_num_anios_fec_pago=0;	
							try {
								int_num_anios_fec_pago=Integer.parseInt(num_anios);
							} catch (Exception e) {
								// TODO: handle exception
							}
							fecha=(anio_actual+int_num_anios_fec_pago)+"-"+fecha;
							str_fecha_pago_nrder=fecha;
						}else{
							String fecha=str_fecha_pago_nrder;
							fecha=anio_actual+"-"+fecha;
							str_fecha_pago_nrder=fecha;
						}
					}


					int ide_gemes_ini=utilitario.getMes(str_fecha_inicio_nrder);
					int ide_gemes_fin=utilitario.getMes(str_fecha_fin_nrder);
					int anio_ini=utilitario.getAnio(str_fecha_inicio_nrder);
					int anio_fin=utilitario.getAnio(str_fecha_fin_nrder);

					if (str_fecha_pago_nrder!=null && !str_fecha_pago_nrder.isEmpty()){
						int mes_rol=utilitario.getMes(fecha_rol);
						int mes_pago_rubro=utilitario.getMes(str_fecha_pago_nrder);
						if (mes_rol==mes_pago_rubro){
							if (utilitario.isFechaMenor(utilitario.getFecha(str_fecha_pago_nrder), utilitario.getFecha(fecha_rol))
									|| !utilitario.isFechaMayor(utilitario.getFecha(str_fecha_pago_nrder), utilitario.getFecha(fecha_rol))){
//								return serv_nomina.getSumatoriaRubro(ide_geedp, ide_nrder_suma,str_fecha_inicio_nrder,str_fecha_fin_nrder)+"";
								return "0";

							}
						}else{
							return "0";
						}
					}else{
//						return serv_nomina.getSumatoriaRubro(ide_geedp, ide_nrder_suma,str_fecha_inicio_nrder,str_fecha_fin_nrder)+"";
						return "0";

					}

				}else{
					return "0";
				}
			}
		}


		return null;
	}

	
}
