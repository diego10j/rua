package paq_gestion.ejb;

import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;


import sistema.aplicacion.Utilitario;

@Stateless
public class ServicioGestion {

	private Utilitario utilitario=new Utilitario();
	/**
	 * metodo que retorna el sql para cargar los combos de division politica con los campos ide_gedip- pais- provincia -ciudad -parroquia , ide_gedip de la ciudad
	 */
	public String getSqlDivisionPoliticaCiudadParroquia(){
		String str_sql="select a.ide_gedip,pais,provincia,ciudad,a.detalle_gedip as parroquia" +
				" from (select * from gen_division_politica where ide_getdp=5 ) a" +
				" left join (select a.ide_gedip,c.detalle_gedip ||' ' as pais,provincia||' ' as provincia,a.detalle_gedip as ciudad" +
				" from( select * from gen_division_politica where ide_getdp=2 ) a" +
				" left join (select a.ide_gedip,a.detalle_gedip as canton,b.ide_gedip as codigo_provincia,b.detalle_gedip as provincia," +
				" b.gen_ide_gedip as codigo_pais from (select * from gen_division_politica where ide_getdp=3) a left join" +
				" (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip) b on a.gen_ide_gedip = b.ide_gedip" +
				" left join gen_division_politica c on b.codigo_pais=c.ide_gedip" +
				" where not c.detalle_gedip is null order by provincia,a.detalle_gedip) " +
				" b on a.gen_ide_gedip= b.ide_gedip	where not pais is null" +
				" order by pais, provincia,ciudad,detalle_gedip";	
		return str_sql;
	}

	

	/**
	 * metodo que retorna el sql para cargar los combos de division politica con los campos ide_gedip- pais- provincia -ciudad , ide_gedip de la ciudad
	 */
	public String getSqlDivisionPoliticaCiudad(){
		String str_sql="select a.ide_gedip,c.detalle_gedip ||' ' as pais,provincia||' ' as provincia,a.detalle_gedip as ciudad " +
				"from " +
				"( " +
				"select * from gen_division_politica where ide_getdp=2 " +
				") a " +
				"left join " +
				"( " +
				"select a.ide_gedip,a.detalle_gedip as canton," +
				"b.ide_gedip as codigo_provincia,b.detalle_gedip as provincia, " +
				"b.gen_ide_gedip as codigo_pais " +
				"from (select * from gen_division_politica where ide_getdp=3) a " +
				"left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip " +
				") b on a.gen_ide_gedip = b.ide_gedip " +
				"left join gen_division_politica c on b.codigo_pais=c.ide_gedip " +
				"where not c.detalle_gedip is null " +
				"order by provincia,a.detalle_gedip";
		return str_sql;
	}

	
	/**
	 * metodo que retorna el sql para cargar los combos de division politica con los campos ide_gedip- pais- provincia -ciudad , ide_gedip de la provincia
	 */
	public String getSqlDivisionPoliticaProvincia(){
		String str_sql="select b.ide_gedip,c.detalle_gedip||' ' as pais,provincia||' ' as provincia,a.detalle_gedip as ciudad " +
				"from " +
				"( " +
				"select * from gen_division_politica where ide_getdp=2 " +
				") a " +
				"left join " +
				"( " +
				"select a.ide_gedip,a.detalle_gedip as canton,b.ide_gedip as codigo_provincia,b.detalle_gedip as provincia, " +
				"b.gen_ide_gedip as codigo_pais " +
				"from (select * from gen_division_politica where ide_getdp=3) a " +
				"left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip " +
				") b on a.gen_ide_gedip = b.ide_gedip " +
				"left join gen_division_politica c on b.codigo_pais=c.ide_gedip " +
				"where not c.detalle_gedip is null " +
				"order by provincia,a.detalle_gedip";
		return str_sql;
	}

	/**
	 * metodo que retorna el sql para cargar los combos de division politica con los campos ide_gedip- pais , ide_gedip del pais
	 */
	public String getSqlDivisionPoliticaSoloPais(){
		String str_sql="select  a.ide_gedip, a.pais from (select c.ide_gedip,c.detalle_gedip||' ' as pais,provincia||' ' as provincia,a.detalle_gedip as ciudad " +
				"from " +
				"( " +
				"select * from gen_division_politica where ide_getdp=2 " +
				") a " +
				"left join " +
				"( " +
				"select a.ide_gedip,a.detalle_gedip as canton,b.ide_gedip as codigo_provincia,b.detalle_gedip as provincia, " +
				"b.gen_ide_gedip as codigo_pais " +
				"from (select * from gen_division_politica where ide_getdp=3) a " +
				"left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip " +
				") b on a.gen_ide_gedip = b.ide_gedip " +
				"left join gen_division_politica c on b.codigo_pais=c.ide_gedip " +
				"where not c.detalle_gedip is null " +
				"order by provincia,a.detalle_gedip)a group by a.ide_gedip,a.pais ";
		return str_sql;
	}

	
	/**
	 * metodo que retorna el sql para cargar los combos de division politica con los campos ide_gedip- pais- provincia -ciudad , ide_gedip del pais
	 */
	public String getSqlDivisionPoliticaPais(){
		String str_sql="select c.ide_gedip,c.detalle_gedip||' ' as pais,provincia||' ' as provincia,a.detalle_gedip as ciudad " +
				"from " +
				"( " +
				"select * from gen_division_politica where ide_getdp=2 " +
				") a " +
				"left join " +
				"( " +
				"select a.ide_gedip,a.detalle_gedip as canton,b.ide_gedip as codigo_provincia,b.detalle_gedip as provincia, " +
				"b.gen_ide_gedip as codigo_pais " +
				"from (select * from gen_division_politica where ide_getdp=3) a " +
				"left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip " +
				") b on a.gen_ide_gedip = b.ide_gedip " +
				"left join gen_division_politica c on b.codigo_pais=c.ide_gedip " +
				"where not c.detalle_gedip is null " +
				"order by provincia,a.detalle_gedip";
		return str_sql;
	}
	
	public String getIdeContratoActivo(String ide_gtemp){
		if (ide_gtemp!=null && !ide_gtemp.isEmpty()){
		TablaGenerica tab_emp_dep=getEmpleadoDepartamento(ide_gtemp);
		for (int i = 0; i < tab_emp_dep.getTotalFilas(); i++) {
			if (tab_emp_dep.getValor(i, "ACTIVO_GEEDP")!=null
					&& !tab_emp_dep.getValor(i, "ACTIVO_GEEDP").isEmpty()
					&& tab_emp_dep.getValor(i, "ACTIVO_GEEDP").equalsIgnoreCase("TRUE")){
				return tab_emp_dep.getValor(i, "IDE_GEEDP");
			}
		}
		}
		return null;
	}
	
	/**
	 * Retorna el TablaGenerica EmpleadoDepartamento
	 * @param ide_geedp
	 * @return
	 */
	public TablaGenerica getEmpleadoDepartamento(String ide_gtemp) {
		if ( ide_gtemp != null && !ide_gtemp.isEmpty()) {
			TablaGenerica tab_empl = utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP=" + ide_gtemp);
			if (tab_empl.getTotalFilas() > 0) {				
				return tab_empl;				 
			} 
		} 
		return null;
	}

	
	public String getSqlEmpleadosAutocompletar(){
		String str_sql="SELECT EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP || ' ' || " +
				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"FROM GTH_EMPLEADO EMP " +
				"LEFT JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR  ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP and epar.activo_geedp= true " +
				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.APELLIDO_PATERNO_GTEMP , " +
				"EMP.APELLIDO_MATERNO_GTEMP , " +
				"EMP.PRIMER_NOMBRE_GTEMP , " +
				"EMP.SEGUNDO_NOMBRE_GTEMP , " +
				"SUCU.NOM_SUCU, AREA.DETALLE_GEARE, " +
				"DEPA.DETALLE_GEDEP " +
				"order by APELLIDO_PATERNO_GTEMP";
		return str_sql;
	}
	
	/**
	 * metodo que retorna el sql para cargar los combos de Distribucion politica con los campos ide_indip- pais- provincia -ciudad , ide_indip de la ciudad
	 */
	public String getSqlDistribucionPoliticaCiudad(){
		String str_sql="select a.ide_indip,c.detalle_indip ||' ' as pais,provincia||' ' as provincia," +
				"a.detalle_indip as ciudad from ( select * from inst_distribucion_politica where ide_intid=3 ) a " +
				" left join ( select a.ide_indip,a.detalle_indip as canton, b.ide_indip as codigo_provincia," +
				" b.detalle_indip as provincia, b.ide_indip as codigo_pais from     " +
				"(select * from inst_distribucion_politica where ide_intid=4) a left join " +
				" (select * from inst_distribucion_politica where ide_intid=2)  b " +
				" on a.ins_ide_indip = b.ide_indip ) b on a.ins_ide_indip = b.ide_indip " +
				" left join inst_distribucion_politica c on b.codigo_pais=c.ide_indip where not c.detalle_indip is null " +
				" order by provincia,a.detalle_indip ";
		return str_sql;
	}

	
	
	
	
	public boolean cambiarEstadoPartidaOcupada(String IDE_GEPGC){
		if (utilitario.getConexion().ejecutarSql("UPDATE GEN_PARTIDA_GRUPO_CARGO set VACANTE_GEPGC=0 where IDE_GEPGC ="+IDE_GEPGC).isEmpty()){
			return true;
		}
		return false;
	}

	public boolean cambiarEstadoPartidaVacante(String IDE_GEPGC){
		if (utilitario.getConexion().ejecutarSql("UPDATE GEN_PARTIDA_GRUPO_CARGO set VACANTE_GEPGC=1 where IDE_GEPGC ="+IDE_GEPGC).isEmpty()){
			return true;
		}
		return false;
	}

	public TablaGenerica getPartidaGrupoEmpleado(String ide_geedp){
		TablaGenerica tab_emp_dep=utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp);
		return tab_emp_dep;
	}

	public TablaGenerica getPartidaGrupo(String ide_gepgc){
		TablaGenerica tab_emp_dep=utilitario.consultar("select * from GEN_PARTIDA_GRUPO_CARGO where IDE_GEPGC="+ide_gepgc);
		return tab_emp_dep;
	}
	
	public boolean isCambioPartida(String ide_geedp,String IDE_GEPGC){
		TablaGenerica tab_emp_dep=utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+ide_geedp);
		for (int i = 0; i < tab_emp_dep.getTotalFilas(); i++) {
			if (tab_emp_dep.getValor(i, "IDE_GEPGC").equalsIgnoreCase(IDE_GEPGC)){
				return false;
			}
		}

		return true;
	}

	public boolean validarAccionFiniquito(String IDE_GEAME){
		TablaGenerica tab_ame=utilitario.consultar("select * from GEN_ACCION_MOTIVO_EMPLEADO where IDE_GEAED in ( " +
				"select IDE_GEAED from GEN_ACCION_EMPLEADO_DEPA where FINIQUITO_CONTRATO_GEAED=TRUE)");
		for (int i = 0; i < tab_ame.getTotalFilas(); i++) {
			if (tab_ame.getValor(i, "IDE_GEAME")!=null && !tab_ame.getValor(i, "IDE_GEAME").isEmpty()){
				if (tab_ame.getValor(i, "IDE_GEAME").equalsIgnoreCase(IDE_GEAME)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean inactivarEmpleado(String IDE_GEEDP){
		if (utilitario.getConexion().ejecutarSql("update GTH_EMPLEADO set ACTIVO_GTEMP=FALSE where IDE_GTEMP in ( " +
				"select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+IDE_GEEDP+")").isEmpty()){
			return true;
		}
		return false;
	}

	public boolean activarEmpleado(String IDE_GEEDP){
		if (utilitario.getConexion().ejecutarSql("update GTH_EMPLEADO set ACTIVO_GTEMP=TRUE where IDE_GTEMP in ( " +
				"select IDE_GTEMP from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GEEDP="+IDE_GEEDP+")").isEmpty()){
			return true;
		}
		return false;
	}

	public TablaGenerica getAnio(String IDE_GEANI){
		TablaGenerica tab_anio=utilitario.consultar("SELECT * FROM GEN_ANIO WHERE IDE_GEANI="+IDE_GEANI);
		return tab_anio;
	}


	public TablaGenerica getMes(String IDE_GEMES){
		TablaGenerica tab_mes=utilitario.consultar("SELECT * FROM GEN_MES WHERE IDE_GEMES="+IDE_GEMES);
		return tab_mes;
	}

	public String getNumeroSolicitudViatico(String IDE_GTEMP){
		TablaGenerica tab_viaticos=utilitario.consultar("select * from GTH_VIATICOS  where IDE_GTEMP="+IDE_GTEMP);
		if (tab_viaticos.getTotalFilas()>0){
			int int_num_sol=tab_viaticos.getTotalFilas()+1;
			return int_num_sol+"";
		}else{
			return "1";
		}
	}

	public TablaGenerica getTipoContrato(String IDE_GTTCO){
		if (IDE_GTTCO!=null && !IDE_GTTCO.isEmpty()){
			TablaGenerica tab_tc=utilitario.consultar("select * from GTH_TIPO_CONTRATO where IDE_GTTCO="+IDE_GTTCO);
			return tab_tc;
		}
		return null;
	}

	public TablaGenerica getTipoEmpleado(String IDE_GTTEM){
		if (IDE_GTTEM!=null && !IDE_GTTEM.isEmpty()){
			TablaGenerica tab_te=utilitario.consultar("select * from GTH_TIPO_EMPLEADO where IDE_GTTEM="+IDE_GTTEM);
			return tab_te;
		}
		return null;
	}

	public TablaGenerica getSucursal(String IDE_SUCU){
		if (IDE_SUCU!=null && !IDE_SUCU.isEmpty()){
			TablaGenerica tab_suc=utilitario.consultar("select * from SIS_SUCURSAL WHERE IDE_SUCU="+IDE_SUCU);
			return tab_suc;
		}
		return null;
	}

}
