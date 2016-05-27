package paq_contabilidad.ejb;

import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import sistema.aplicacion.Utilitario;
@Stateless

public class ServicioContabilidad {
	
	private Utilitario utilitario=new Utilitario();
	
	public String getVigente (String codigo){
		
		String tab_vigente="SELECT tc.constraint_name, " +
				"tc.table_name, kcu.column_name," +
				" ccu.table_name AS foreign_table_name," +
				"ccu.column_name AS foreign_column_name " +
				"FROM information_schema.table_constraints AS tc " +
				"JOIN information_schema.key_column_usage AS kcu " +
				"ON tc.constraint_name = kcu.constraint_name " +
				"JOIN information_schema.constraint_column_usage AS ccu " +
				"ON ccu.constraint_name = tc.constraint_name " +
				"WHERE constraint_type = 'FOREIGN KEY' AND tc.table_name='cont_vigente'" +"codigo";
				
		return tab_vigente;
		
	}
public TablaGenerica getTablaVigente (String tabla){
	
	TablaGenerica tab_vigente=utilitario.consultar("SELECT tc.constraint_name, " +
			"tc.table_name, kcu.column_name, " +
			" ccu.table_name AS foreign_table_name, " +
			"ccu.column_name AS foreign_column_name " +
			"FROM information_schema.table_constraints AS tc " +
			"JOIN information_schema.key_column_usage AS kcu " +
			"ON tc.constraint_name = kcu.constraint_name " +
			"JOIN information_schema.constraint_column_usage AS ccu " +
			"ON ccu.constraint_name = tc.constraint_name " +
			"WHERE constraint_type = 'FOREIGN KEY' AND tc.table_name='"+tabla+"'");
	return tab_vigente;
	
	}

public String getAnio (String activo, String bloqueado ){
	String tab_anio= "select ide_geani,nom_geani as detalle_geani from gen_anio " +
			" where activo_geani in ("+activo+") " +
			" order by nom_geani desc";
	return tab_anio;

	}
public String getMes (String estado ){
	String tab_anio= "select ide_gemes,nombre_gemes from gen_mes where activo_gemes in ("+estado+") order by ide_gemes";
	return tab_anio;

	}
public TablaGenerica getTablaAnio (String activo, String bloqueado ){
	TablaGenerica tab_anio= utilitario.consultar("select ide_geani,nom_geani as detalle_geani from gen_anio " +
			" where activo_geani in ("+activo+") " +
			" order by nom_geani desc");
	return tab_anio;
	}
public String getAnioDetalle (String activo, String bloqueado ){
	String tab_anio= "select ide_geani,nom_geani as detalle_geani, " +
			" CASE WHEN activo_geani = true THEN 'Activo' ELSE 'Inactivo' END AS activo_geani" +
			" from gen_anio where activo_geani in("+activo+")" +
			" order by nom_geani desc" ;
			
	return tab_anio;

	}
public String getNombreAsientoContable (String modulo, String estado ){
	String tab_asiento= "select a.ide_conac,ide_gemod,detalle_conac,consolidado_conac,individual_conac,activo_conac,ide_coest from cont_nombre_asiento_contable a where ide_gemod in ( "+modulo+") and activo_conac in ("+estado+")" ;
	return tab_asiento;
}
public TablaGenerica getTablaAnioDetalle (String activo, String bloqueado ){
	TablaGenerica tab_anio= utilitario.consultar("select ide_geani,nom_geani as detalle_geani," +
			" CASE WHEN activo_geani = true THEN 'Activo' ELSE 'Inactivo' END AS activo_geani" +
			" from gen_anio where activo_geani in("+activo+")  " +
			" order by nom_geani desc");
	return tab_anio;
	}
/**
 * Metodo que devuelve los Estados clasificados por modulo 
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param modulo recibe el codigo del Modulo por el cual desea clasificar el estado 
 * @return string SQL modulo estado
 */
public TablaGenerica getTablaModuloEstados (String estado,String modulo){
	
	TablaGenerica tab_estados=utilitario.consultar("SELECT a.ide_coest,detalle_coest,detalle_gemod "
			+"FROM gen_modulo_estado a,cont_estado b, gen_modulo c "
			+"WHERE a.ide_coest= b.ide_coest AND a.ide_gemod = c.ide_gemod "
			+"AND activo_gemoe in ("+estado+") AND a.ide_gemod ="+modulo+" order by detalle_coest");
		return tab_estados;
}

public String getModuloEstados (String estado,String modulo){
	String consultaEstados="SELECT a.ide_coest,detalle_coest,detalle_gemod "
+"FROM gen_modulo_estado a,cont_estado b, gen_modulo c "
+"WHERE a.ide_coest= b.ide_coest AND a.ide_gemod = c.ide_gemod "
+"AND activo_gemoe in ("+estado+") AND a.ide_gemod ="+modulo+" order by detalle_coest";
	return consultaEstados;
}
/**
 * Metodo que devuelve los Parametros generales requeridos por modulo
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param modulo recibe el codigo del modulo 
 * @return string SQL gen_modulo
 */
public String getModuloParametros (String estado,String modulo){
	String consultaEstados="select ide_copag,detalle_copag from cont_parametros_general" 
+" where ide_copag in (select ide_copag from cont_parametro_modulo where ide_gemod in ("+modulo+") and activo_copam in ("+estado+") )";
	System.out.println("modulo parametro"+consultaEstados);
	return consultaEstados;
}
/**
 * Metodo que devuelve los tipos de convenios 
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param modulo recibe el codigo del de los tipos de convenios 
 * @return string SQL tipo de convenio
 */
public TablaGenerica getTablaTipoConvenio (String activo,String detalle){
	TablaGenerica tab_tipo_convenio= utilitario.consultar("Select ide_cotie ,detalle_cotie " +
			" from cont_tipo_convenio" +
			"  where activo_cotie in ( true, false) order by detalle_cotie");
	
		return tab_tipo_convenio;
}

public String getTipoConvenio (String activo, String detalle){
	String tipo_convenio ="Select ide_cotie ,detalle_cotie " +
			" from cont_tipo_convenio" +
			"  where activo_cotie in ( true, false) order by detalle_cotie";
		
	return tipo_convenio ;

}
public TablaGenerica getTablaInstitucion ( String activo, String detalle){
	TablaGenerica tab_institucion= utilitario.consultar("Select ide_geins,detalle_geins" +
			" from gen_institucion" +
			" where activo_geins in (true, false) order by detalle_geins");
	
	return tab_institucion;	
	
	}
public String getInstitucion (String activo, String detalle){
	String institucion ="Select ide_geins,detalle_geins "+
			" from gen_institucion" +
			" where activo_geins in (true, false)  order by detalle_geins";
	
	return institucion;
}
/**
 * Metodo que devuelve la Cuenta Cantable
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param modulo recibe las cuentas contables activas o pasivas 
 * @return TABLA GENERICA SQL Cuenta Contable 
 */

public TablaGenerica getTablaCuentaContable ( String activo){
	TablaGenerica tab_cuenta_contable= utilitario.consultar("select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac" +
			"  from cont_catalogo_cuenta " +
			"  WHERE activo_cocac IN ("+activo+")order by cue_codigo_cocac ");
	
	return tab_cuenta_contable;	
}

public String getCuentaContable (String activo){
	String cuentacontable ="select ide_cndpc as ide_cocac, codig_recur_cndpc as cue_codigo_cocac, nombre_cndpc as cue_descripcion_cocac from con_det_plan_cuen a, con_cab_plan_cuen b " 
                +" where a.ide_cncpc=b.ide_cncpc and activo_cncpc in ("+activo+")";
	return cuentacontable;
}

/**
 * Metodo que devuelve la Cuenta Cantable por ide_cocac 
 * @param activo recibe el o los estados true y false, ejemplo: true o false
 * @param ide_cuenta recibe los ide de las cuentas contables 
 * @return String SQL Cuenta Contable 
 */
public String getCuentaContableCodigo (String estado,String ide_activo){
	String cuentacontable ="select ide_cocac,cue_codigo_cocac,cue_descripcion_cocac, replace(cue_codigo_cocac,'.','') as cue_codigo_remplazado" +
			"  from cont_catalogo_cuenta " +
			"  WHERE activo_cocac IN ("+estado+") and ide_cocac in ("+ide_activo+") order by cue_codigo_cocac ";
	return cuentacontable;
}

/**
 * Metodo que devuelve la Cuenta Cantable por los Años vigentes
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param ide_geani recibe el Año de las cuentas contables 
 * @return TablaGenerica Cuenta Contable 
 */
public TablaGenerica getTablaCatalogoCuentaAnio (String estado,String ide_geani){
	
	TablaGenerica tab_catalogo_cuenta_anio=utilitario.consultar("select a.ide_cocac,cue_codigo_cocac,cue_descripcion_cocac," +
			" nom_geani as detalle_geani" +
			"  from cont_catalogo_cuenta a,cont_vigente b,gen_anio c "+
			" where a.ide_cocac = b.ide_cocac" +
			" and b.ide_geani= c.ide_geani and b.ide_geani =("+ide_geani+")" +
			" and activo_cocac in ("+estado+")order by cue_codigo_cocac");
	return tab_catalogo_cuenta_anio;
	
	
}
/**
 * Metodo que devuelve la Cuenta Cantable por los Años vigentes
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param ide_geani recibe el Año de las cuentas contables 
 * @return String Cuenta Contable 
 */
public String getCatalogoCuentaAnio (String estado,String ide_geani){
	
	String tab_catalogo_cuenta_anio="select a.ide_cocac,cue_codigo_cocac,cue_descripcion_cocac," +
			" nom_geani as detalle_geani" +
			"  from cont_catalogo_cuenta a,cont_vigente b,gen_anio c "+
			" where a.ide_cocac = b.ide_cocac" +
			" and b.ide_geani= c.ide_geani and b.ide_geani =("+ide_geani+")" +
			" and activo_cocac in ("+estado+")order by cue_codigo_cocac";
	return tab_catalogo_cuenta_anio;
	
	
}
/**
 * Metodo que devuelve la Cuenta Cantable por los Años vigentes
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param ide_geani recibe el Año de las cuentas contables 
 * @return String SQL Cuenta Contable 
 */
public String servicioCatalogoCuentaAnio (String estado, String ide_geani){
	
	String catalogo_cuenta_anio="select ide_cndpc as ide_cocac, codig_recur_cndpc as cue_codigo_cocac, nombre_cndpc as cue_descripcion_cocac from con_det_plan_cuen a, con_cab_plan_cuen b " 
                +" where a.ide_cncpc=b.ide_cncpc and activo_cncpc in ("+estado+")";
	return catalogo_cuenta_anio;
}
public String getInventario(String ide_geani,String carga,String material){
	
	String tab_inventario="SELECT ide_boinv,codigo_bomat,detalle_bomat,nom_geani as detalle_geani,ingreso_material_boinv,egreso_material_boinv,existencia_inicial_boinv,costo_anterior_boinv," +
			"costo_actual_boinv,fecha_ingr_articulo_boinv,costo_inicial_boinv,activo_boinv " +
			" FROM bodt_inventario a, gen_anio b ,bodt_material c WHERE a.ide_geani=b.ide_geani " +
			" AND a.ide_bomat = c.ide_bomat AND a.ide_geani IN ("+ide_geani+") ";
			if (carga.equals("0")){
				tab_inventario +=" and a.ide_bomat= "+material;
			}
			    tab_inventario += " ORDER by ide_boinv";	

	return tab_inventario;
}

/**
 * Metodo que devuelve la Cuenta Cantable por realizaqr transacciones filtra cuentas contables por Año fiscal vigente
 * @return String SQL Cuenta Contable para realizar transacciones
 */
public String servicioCatalogoCuentasTransaccion(String aplica_anio){
	
	String catalogo_cuenta_anio="select ide_cocac,cue_codigo_cocac,substring (cue_descripcion_cocac from 1 for 100) as cuenta from cont_catalogo_cuenta  where not ide_cocac in ("
			+" select con_ide_cocac from cont_catalogo_cuenta where not con_ide_cocac is null group by con_ide_cocac )" 
			+" and ide_cocac in ( select ide_cocac from cont_vigente where not ide_cocac is null ";
	if (aplica_anio.equals("0")){
		catalogo_cuenta_anio+= " and ide_geani in (select ide_geani from gen_anio where activo_geani = true limit 1)";
	}
	catalogo_cuenta_anio+= " )"
			+" order by cue_codigo_cocac";
	return catalogo_cuenta_anio;
}
/**
 * Metodo que devuelve el ide maximo de una tabla
 * @return String SQL Codigo maximo de los ide primarios de de las tablas
 */
public String servicioCodigoMaximo(String tabla,String ide_primario){
	
	String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
	return maximo;
}
/**
 * Metodo que devuelve el numero secuencial, por modulo
 * @param modulo recibe el modulo al cual pertenece el secuencial
 * @return String nro secuencial por modulo
 */
public String numeroSecuencial(String modulo){
	String resultado="1";
	
	TablaGenerica valor_resultado=utilitario.consultar("select ide_gemod,numero_secuencial_gemos from gen_modulo_secuencial where ide_gemod="+modulo);
	Integer nuevo_valor=Integer.parseInt(valor_resultado.getValor("numero_secuencial_gemos"))+1;
	resultado =	nuevo_valor+"";
	return resultado;
}

public String guardaSecuencial(String secuencial_vigente,String modulo){
	String mensaje="Actualizado Secuencial";
	double nuevo_valor=Double.parseDouble(secuencial_vigente);
	utilitario.getConexion().ejecutarSql("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
	System.out.println("update gen_modulo_secuencial set numero_secuencial_gemos="+nuevo_valor+" where ide_gemod="+modulo);
	return mensaje;
}
/**
 * Metodo que permite generar el balance inicial
 * @param ide_geani recibe el Año fiscal para la generación del balance inicial
 */
public void generarBalanceInicial(String ide_geani,String ide_movimiento){
	TablaGenerica anio = utilitario.consultar("select * from gen_anio where ide_geani ="+ide_geani);
	utilitario.getConexion().ejecutarSql("delete from cont_balance_inicial where ide_geani="+ide_geani);
	utilitario.getConexion().ejecutarSql("insert into cont_balance_inicial (ide_cobai,ide_cocac,ide_geani,valor_debe_cobai,valor_haber_cobai,valor_descripcion_cobai,activo_cobai)"
+" select row_number() over(order by a.ide_cocac) + (select (case when max(ide_cobai) is null then 0 else max(ide_cobai) end) as codigo from cont_balance_inicial) as codigo, a.ide_cocac,"
+" ide_geani,0 as debe,0 as haber,'BALANCE INICIAL '||"+anio.getValor("nom_geani")+" as detalle,false from cont_catalogo_cuenta a, cont_vigente b where a.ide_cocac= b.ide_cocac and ide_geani ="+ide_geani+" group by a.ide_cocac,ide_geani");
	
	TablaGenerica actualiza_cuenta=utilitario.consultar(sumaDebeHaberCuentaContable(ide_movimiento));
	for (int i=0;i<actualiza_cuenta.getTotalFilas();i++){
	utilitario.getConexion().ejecutarSql(updateBalanceInicial(actualiza_cuenta.getValor(i,"debe"),actualiza_cuenta.getValor(i,"haber"),actualiza_cuenta.getValor(i,"ide_cocac"),ide_geani));
	}
	
	TablaGenerica nivel_maximo = utilitario.consultar(nivelMaximoCuentaContable());
	Integer contador = Integer.parseInt(nivel_maximo.getValor("nivel"));
	while (contador>1){
		TablaGenerica suma_balance=utilitario.consultar("select con_ide_cocac,sum(valor_debe_cobai) as debe,sum(valor_haber_cobai) as haber from ("
				+" select  a.ide_cocac,ide_geani,valor_debe_cobai,valor_haber_cobai,con_ide_cocac" 
				+" from cont_balance_inicial a, cont_catalogo_cuenta b where a.ide_cocac= b.ide_cocac and  ide_geani="+ide_geani+" and nivel_cocac="+contador
				+" ) a group by con_ide_cocac");
		for(int i=0;i<suma_balance.getTotalFilas();i++){
			utilitario.getConexion().ejecutarSql(updateBalanceInicial(suma_balance.getValor(i,"debe"), suma_balance.getValor(i,"debe"), suma_balance.getValor(i, "con_ide_cocac"), ide_geani));
		}
	   -- contador ; 
	}
	
}

/**
 * Metodo que devuelve el sql para actualizar el balance inicial
 * @param valor_debe recibe el valor al debe que va a ser actualizado
 * @param valor_haber recibe el valor al haber que va a ser actualizado
 * @param ide_cocac recibe ide de la cuenta contable a ser actualizado
 * @param ide_geani recibe el Año fiscal
 * @return String sql para actualizar balance inicial
 */
public String updateBalanceInicial(String valor_debe,String valor_haber,String ide_cocac,String ide_geani){
	    String sql="update cont_balance_inicial"
				+" set valor_debe_cobai ="+valor_debe+","
				+" valor_haber_cobai ="+valor_haber+" where ide_cocac ="+ide_cocac+" and ide_geani="+ide_geani; 
		return sql;
}


/**
 * Metodo que devuelve el sql para actualizar el balance de comprobacion
 * @param valor_debe recibe el valor al debe que va a ser actualizado
 * @param valor_haber recibe el valor al haber que va a ser actualizado
 * @param ide_cocac recibe ide de la cuenta contable a ser actualizado
 * @param ide_geani recibe el Año fiscal
 * @param ide_gemes recibe el mes
 * @return String sql para actualizar balance comprobacion
 */
public String updateBalanceComprobacion(String valor_debe,String valor_haber,String ide_cocac,String ide_geani,String ide_gemes){
	    String sql="update cont_balance_comprobacion"
				+" set debe"+ide_gemes+"_cobac ="+valor_debe+","
				+" haber"+ide_gemes+"_cobac ="+valor_haber+" where ide_cocac ="+ide_cocac+" and ide_geani="+ide_geani; 
	System.out.println("actualizo balance "+sql);
	    return sql;
}
/**
 * Metodo que devuelve la suma del debe y el haber por cuenta contable y movimientos contables
 * @param ide_movimiento recibe el codigo del asiento contable
 * @return String suma debe y haber por cuenta contable
 */
public String sumaDebeHaberCuentaContable(String ide_movimiento){
	    String sql="select ide_cocac,sum (debe_codem) as debe,sum(haber_codem) as haber from ("
+" select ide_cocac,debe_codem,haber_codem from cont_detalle_movimiento where ide_comov in ("+ide_movimiento+")"
+" ) a group by ide_cocac"; 
	    System.out.println("imprimo para mayorizar "+sql);
		return sql;
}

/**
 * Metodo que devuelve el codigo maximo del nivel de cuenta contable
 * @return String nivel maximo
 */
public String nivelMaximoCuentaContable(){
	    String sql="select 1 as codigo, max(nivel_cocac) as nivel from cont_catalogo_cuenta"; 
		return sql;
}
/**
 * Metodo que devuelve los periodos contables por Año vigente
 * @param ide_geani recibe el codigo del Año fiscal
 * @return String sql de los movimientos contables
 */
public String getMovimientosContables(String ide_geani,String ide_cotia,String ide_cotim){
	    String sql="select ide_comov,nro_comprobante_comov,mov_fecha_comov,detalle_comov,ide_geani,ide_gemes from cont_movimiento where ide_geani ="+ide_geani+" and ide_cotia in ("+ide_cotia+") and ide_cotim in ("+ide_cotim+") order by nro_comprobante_comov desc,mov_fecha_comov desc"; 
		return sql;
}
/**
 * Metodo que devuelve los asientos contables con las respectivas sumas al debe y al haber
 * @return String sql de los movimientos contables
 */
public String getMovimientosContablesSumaDebeHaber(String ide_geani,String ide_gemes, String estado,String ide_cotia){
	    String sql="select a.ide_comov,nro_comprobante_comov,mov_fecha_comov,detalle_comov,"
+" (case when debe is  null then 0 else debe end) as debe,(case when haber is null then 0 else haber end) as haber,"
+" (case when debe is  null then 0 else debe end) - (case when haber is null then 0 else haber end) as diferencia,"
+" (case when b.ide_comov is null then 0 else 1 end ) as detalle_asiento"
+" from cont_movimiento a"
+" left join ( select sum(debe_codem) as debe, sum(haber_codem) as haber,ide_comov" 
+" from cont_detalle_movimiento group by ide_comov  ) b on  a.ide_comov = b.ide_comov"
+" where  ide_geani = "+ide_geani+" and ide_gemes in ("+ide_gemes+") and activo_comov in ("+estado+") and not ide_cotia= "+ide_cotia
+" order by nro_comprobante_comov desc,mov_fecha_comov desc"; 
	    System.out.println("cargfando para mayorizar "+sql);
		return sql;
}

/**
 * Metodo que permite generar el balance de comprobación
 * @param ide_geani recibe el Año fiscal para la generación del balance inicial
 * @param ide_geani recibe el Año fiscal para la generación del balance inicial
 */

 
public void generarBalanceComprobacion(String ide_geani,String ide_gemes,String ide_cotia){
	TablaGenerica anio = utilitario.consultar("select * from gen_anio where ide_geani ="+ide_geani);
	utilitario.getConexion().ejecutarSql("delete from cont_balance_comprobacion where ide_geani="+ide_geani+"; ");
	
	String inserta_balance_comprobacion="insert into cont_balance_comprobacion (ide_cobac,ide_cocac,ide_geani,debe1_cobac,haber1_cobac,debe2_cobac,haber2_cobac,debe3_cobac,haber3_cobac"
			+" ,debe4_cobac,haber4_cobac,debe5_cobac,haber5_cobac,debe6_cobac,haber6_cobac,debe7_cobac,haber7_cobac,debe8_cobac,haber8_cobac,debe9_cobac,haber9_cobac"
			+" ,debe10_cobac,haber10_cobac,debe11_cobac,haber11_cobac,debe12_cobac,haber12_cobac)"
			+" select row_number() over(order by a.ide_cocac) + (select (case when max(ide_cobac) is null then 0 else max(ide_cobac) end) as codigo from cont_balance_comprobacion) as codigo,"
			+" a.ide_cocac,a.ide_geani,0 as debe1, 0 as haber1,0 as debe2, 0 as haber2,0 as debe3, 0 as haber3,0 as debe4, 0 as haber4,0 as debe5, 0 as haber5"
			+" ,0 as debe6, 0 as haber6,0 as debe7, 0 as haber7,0 as debe8, 0 as haber8,0 as debe9, 0 as haber9,0 as debe10, 0 as haber10"
			+" ,0 as debe11, 0 as haber11,0 as debe12, 0 as haber12"
			+" from ( select a.ide_cocac,ide_geani "
			+" from cont_catalogo_cuenta a, cont_vigente b where a.ide_cocac= b.ide_cocac" 
			+" and ide_geani ="+ide_geani+" group by a.ide_cocac,ide_geani ) a"
			+" left join cont_balance_comprobacion b on a.ide_cocac = b.ide_cocac and a.ide_geani = b.ide_geani"
			+" where b.ide_cocac is null;";
			
	utilitario.getConexion().ejecutarSql(inserta_balance_comprobacion);
	TablaGenerica consulta_movimiento =utilitario.consultar("select * from cont_movimiento where ide_gemes ="+ide_gemes+" and ide_geani="+ide_geani+" and not ide_cotia in ("+ide_cotia+")");
	TablaGenerica consulta_cuadre_asientos=utilitario.consultar(getMovimientosContablesSumaDebeHaber(ide_geani, ide_gemes, "false", ide_cotia));
	consulta_cuadre_asientos.imprimirSql();
	double debe=0;
	double haber=0;
	
	
	for (int i=0;i<consulta_cuadre_asientos.getTotalFilas();i++){
		// tabla generica permite consultar las sumas de 

		debe=Double.parseDouble(consulta_cuadre_asientos.getValor(i,"debe"));
	    haber=Double.parseDouble(consulta_cuadre_asientos.getValor(i,"haber"));
	    
		if(debe==haber){
			System.out.println("entro actualizar los asientos "+consulta_cuadre_asientos.getValor(i, "ide_comov"));
		utilitario.getConexion().ejecutarSql("update cont_movimiento set activo_comov=true where ide_comov ="+consulta_cuadre_asientos.getValor(i,"ide_comov"));	
		}			
	}
	
	//
	TablaGenerica consulta_cuentas_actualizar_balance = utilitario.consultar(sumaDebeHaberCuentaContable("select ide_comov from cont_movimiento where ide_gemes ="+ide_gemes+" and ide_geani="+ide_geani+" and not ide_cotia in ("+ide_cotia+") and activo_comov=true "));
	consulta_cuentas_actualizar_balance.imprimirSql();
	for (int j=0;j<consulta_cuentas_actualizar_balance.getTotalFilas();j++){
		utilitario.getConexion().ejecutarSql(updateBalanceComprobacion(consulta_cuentas_actualizar_balance.getValor(j,"debe"), consulta_cuentas_actualizar_balance.getValor(j, "haber"), consulta_cuentas_actualizar_balance.getValor(j,"ide_cocac"), ide_geani, ide_gemes));
	}
	
	TablaGenerica nivel_maximo = utilitario.consultar(nivelMaximoCuentaContable());
	Integer contador = Integer.parseInt(nivel_maximo.getValor("nivel"));
	while (contador>1){
		/* TablaGenerica suma_balance=utilitario.consultar("select con_ide_cocac,sum(debe) as debe,sum(haber) as haber from ("
+" select  a.ide_cocac,ide_geani,debe"+ide_gemes+"_cobac as debe,haber"+ide_gemes+"_cobac as haber,con_ide_cocac"
+" from cont_balance_comprobacion a, cont_catalogo_cuenta b where a.ide_cocac= b.ide_cocac and  ide_geani="+ide_geani+" and nivel_cocac="+contador
+" ) a group by con_ide_cocac");
		
		System.out.println("va siguiente sistem ");
		suma_balance.imprimirSql();
		for(int k=0;k<suma_balance.getTotalFilas();k++){
			utilitario.getConexion().ejecutarSql(updateBalanceComprobacion(suma_balance.getValor(k,"debe"), suma_balance.getValor(k, "haber"), suma_balance.getValor(k,"ide_cocac"), ide_geani, ide_gemes));
		}*/
		utilitario.getConexion().ejecutarSql("update cont_balance_comprobacion"
+" set debe"+ide_gemes+"_cobac =debe,"
+" haber"+ide_gemes+"_cobac =haber "
+" from ("
+" select con_ide_cocac,sum(debe) as debe,sum(haber) as haber from ("
+" select  a.ide_cocac,ide_geani,debe"+ide_gemes+"_cobac as debe,haber"+ide_gemes+"_cobac as haber,con_ide_cocac"
+" from cont_balance_comprobacion a, cont_catalogo_cuenta b where a.ide_cocac= b.ide_cocac and  ide_geani="+ide_geani+" and nivel_cocac="+contador
+" ) a group by con_ide_cocac"
+" )  a"
+" where cont_balance_comprobacion.ide_cocac = a.con_ide_cocac");
	   -- contador ; 
	}
	
}

public void desmayorizaAsientos(String ide_geani,String ide_gemes){
	utilitario.getConexion().ejecutarSql("update cont_movimiento set activo_comov=false where ide_geani="+ide_geani+" and ide_gemes ="+ide_gemes);
}
/**
 * Metodo que devuelve el Mayor Analitico
 * @param fecha_inicial
 * @para fecha_final
 * @return String sql de los movimientos contables
 */
public String getMayorAnalitico(String fecha_inicial,String fecha_final,String ide_cocac){
	    String sql="select a.ide_comov,mov_fecha_comov,detalle_comov,nro_comprobante_comov,"
+" cue_codigo_cocac,cue_descripcion_cocac,debe_codem,haber_codem,nombre_gemes,estado"
+" from ("
+" select ide_comov,mov_fecha_comov,detalle_comov,nro_comprobante_comov,nombre_gemes,"
+" (case when activo_comov = false then 'NO MAYORIZADO' else 'MAYORIZADO' end) as estado"
+" from cont_movimiento a, gen_mes b where a.ide_gemes = b.ide_gemes"
+" ) a"
+" left join ("
+" select ide_comov,debe_codem,haber_codem,cue_codigo_cocac,cue_descripcion_cocac,b.ide_cocac"
+" from cont_detalle_movimiento a, cont_catalogo_cuenta b"
+" where a.ide_cocac = b.ide_cocac"
+" ) b on a.ide_comov = b.ide_comov"
+" where mov_fecha_comov between '"+fecha_inicial+"' and '"+fecha_final+"'"
+" and ide_cocac IN ("+ide_cocac+")"
+" order by cue_codigo_cocac,mov_fecha_comov,nro_comprobante_comov"; 
		return sql;
}
public void limpiarAcceso(String tabla){
	String sql="delete from sis_bloqueo where tabla_bloq ilike '"+tabla+"';";
	utilitario.getConexion().ejecutarSql(sql);
}
/**
 * Metodo que devuelve el sql para consultar lso anticpio pendientes de contabilizar de lso empleados
 * @return String sql para contar anticpios pendientes
 */
public String getConsultaAnticipos(){
	    String sql="select a.ide_nrant,a.ide_gtemp,a.ide_geedp,apellido_paterno_gtemp,apellido_materno_gtemp,primer_nombre_gtemp,segundo_nombre_gtemp,detalle_nrmoa,fecha_solicitud_nrant,monto_aprobado_nrant"
	    		+" from nrh_anticipo a"
	    		+" left join tes_comprobante_pago b on a.ide_nrant = b.ide_nrant"
	    		+" left join nrh_motivo_anticipo c on a.ide_nrmoa = c.ide_nrmoa"
	    		+" left join gth_empleado d on a.ide_gtemp = d.ide_gtemp"
	    		+" where aprobado_nrant =true and a.activo_nrant=true "
	    		+" and b.ide_nrant is null";
		return sql;
}
}

