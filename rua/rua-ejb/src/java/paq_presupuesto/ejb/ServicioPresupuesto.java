package paq_presupuesto.ejb;


import javax.ejb.Stateless;
import java.util.*;

import sistema.aplicacion.Utilitario;
import framework.aplicacion.TablaGenerica;

/**
 * Session Bean implementation class ServicioPresupuesto
 */
@Stateless

public class ServicioPresupuesto {
	private Utilitario utilitario= new Utilitario();
	/**
	 * Metodo que devuelve el Catalogo Presupuestario por los Años vigentes
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @param ide_geani recibe el Año para filtar el ctalaogo presupuestario 
         * @param tipo recibe el tipo 1 para cuentas tipo ingresos, 0 para cuentas tipo egresos 
	 * @return String SQL Clasificador Presupuestario
	 */
 public String getCatalogoPresupuestarioAnio(String estado,String ide_geani,String tipo){
	 
	 String tab_presupesto="SELECT a.ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
	 		" FROM pre_clasificador a,cont_vigente b,gen_anio c where a.ide_prcla = b.ide_prcla" +
			" and b.ide_geani= c.ide_geani and b.ide_geani ="+ide_geani +
			" and activo_prcla in ("+estado+") and tipo_prcla in ("+tipo+") order by codigo_clasificador_prcla";
	 	 return tab_presupesto;
			 
 }

	/**
	 * Metodo que devuelve el Catalogo Presupuestario para de esta manera desplegar en los autocompletar del aplicativo de esta manera no saturamos los combos de consulta
	 * @param estado recibe el o los estados true y false, ejemplo: true o false
	 * @return String SQL Clasificador Presupuestario solo para consulta de autompletar
	 */
 public String getCatalogoPresupuestario(String activo){
	 
	 String tab_presupesto="SELECT ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
	 		" FROM pre_clasificador where activo_prcla in (" +activo+")"+
				" ORDER BY codigo_clasificador_prcla";
	 return tab_presupesto;
			 
 }
 public TablaGenerica getTablaCatalogoPresupuestario(String ideClasificador){
	 
	 TablaGenerica tab_presupesto=utilitario.consultar("SELECT ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
	 		" FROM pre_clasificador where ide_prcla=" +ideClasificador+
				" ORDER BY codigo_clasificador_prcla");
	 return tab_presupesto;
			 
 } 
 public String getFuncionPrograma(){
	String tab_funcion="select ide_prfup,detalle_prfup,codigo_prfup  " +
			"from pre_funcion_programa ";
	return tab_funcion;
	
}
 public String getCatalogoContabilizar(String tramite_presupuestario,String tipo_mov_presupuestario){
	String tab_funcion=" select ide_prasp,a.ide_prcla,cue_codigo_cocac,cue_descripcion_cocac, codigo_clasificador_prcla,descripcion_clasificador_prcla ,d.ide_gelua,detalle_gelua,devengado,ide_prmop"
			+" from pre_asociacion_presupuestaria a, pre_clasificador b,cont_catalogo_cuenta c, gen_lugar_aplica d"
			+" where a.ide_prcla = b.ide_prcla and a.ide_cocac= c.ide_cocac and a.ide_gelua = d.ide_gelua"
			+" and b.ide_prcla in ("
			+" select ide_prcla from pre_poa where ide_prpoa in (select ide_prpoa from pre_poa_tramite where ide_prtra in ("+tramite_presupuestario+"))"
			+" )"
			+" and ide_prmop in ("+tipo_mov_presupuestario+")"
			+" order by cue_codigo_cocac";
	return tab_funcion;
	
}
TablaGenerica getTablaGenericaFuncionPro(String ideanio){
	TablaGenerica tab_funcion_progra=utilitario.consultar("select ide_prfup,detalle_prfup from pre_funcion_programa " +
			" where ide_prfup in (select ide_prfup from cont_vigente where ide_geani=" +ideanio+
			
			"	order by detalle_prfup");
	return tab_funcion_progra;
	
}
public String getTramite (String activo){
	String tab_tramite="select ide_prtra,ide_prtra as nro_compromiso,numero_oficio_prtra,observaciones_prtra from pre_tramite where activo_prtra in ("+activo+") " +
			" order by numero_oficio_prtra";
	return tab_tramite;
	
}

public TablaGenerica getTablaGenericaTramite (String ide_prtra ){
	TablaGenerica tab_tramite=utilitario.consultar("select ide_prtra,numero_oficio_prtra from pre_tramite where ide_prtra in ("+ide_prtra+") " +
			" order by numero_oficio_prtra");
	return tab_tramite;
}
public String getPoa (String ide_geani,String activo,String presupuesto){
	String tab_poa=("select a.ide_prpoa,detalle_subactividad,presupuesto_inicial_prpoa,codigo_clasificador_prcla,codigo_subactividad,detalle_programa,descripcion_clasificador_prcla,programa," +
			" detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad," +
			" subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa," +
			" presupuesto_codificado_prpoa,reforma_prpoa,nom_geani as detalle_geani,detalle_geare" +
			" from pre_poa a left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join " +
			" (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto," +
			" proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad," +
			" detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a , " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, " +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup" +
			" and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup ) f on a.ide_prfup = f.ide_prfup" +
			" left join gen_area g on a.ide_geare=g.ide_geare where a.ide_geani= "+ide_geani+" and activo_prpoa in ("+activo+") and ejecutado_presupuesto_prpoa in ("+presupuesto+") order by codigo_subactividad,a.ide_prpoa");
		return tab_poa;
}  
public String cetificacion(String anio){
	String sql="select ide_prcer,nro_certificacion_prcer,detalle_prcer,num_documento_prcer,valor_certificacion_prcer from pre_certificacion where ide_geani in ("+anio+") order by num_documento_prcer";
	return sql;
}
public String getPoaNombre (String ide_geani){
	String tab_poa=("select a.ide_prpoa,'Partida Presupuestaria:   '||codigo_clasificador_prcla as codigo_clasificador_prcla,'	|	Codigo SUB-ACTIVIDAD:	'||codigo_subactividad as codigo_subactividad,'		|	Programa: 	'||detalle_programa as detalle_programa,'	|	Proyecto: 	'||detalle_proyecto as detalle_proyecto,'	|	Producto: 	'||detalle_producto as detalle_producto,"
			+" '	|	Nombre Cuenta Presupuestaria: 	'||descripcion_clasificador_prcla as descripcion_clasificador_prcla,'	|	Actividad:	 '||detalle_actividad as detalle_actividad,'	|	Sub Actividad:	'||detalle_subactividad as detalle_subactividad"
			+" from pre_poa a left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join" 
			+" (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto,"
			+" proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,"
			+" detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a , "
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, "
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c, "
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, "
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup"
			+" and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup ) f on a.ide_prfup = f.ide_prfup"
			+" left join gen_area g on a.ide_geare=g.ide_geare where a.ide_geani in ("+ide_geani+") ");
		return tab_poa;
} 
public String getPoaTodos (){
	String tab_poa=("select a.ide_prpoa,codigo_clasificador_prcla,codigo_subactividad,detalle_subactividad,descripcion_clasificador_prcla from pre_poa a" +
			" left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join " +
			" (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto," +
			" proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad," +
			" detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a ," +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b," +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c," +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d," +
			" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa" +
			" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup" +
			" and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup ) f on a.ide_prfup = f.ide_prfup" +
			" left join gen_area g on a.ide_geare=g.ide_geare  order by codigo_subactividad,a.ide_prpoa");

		return tab_poa;
}
public String getPoaSaldosFuenteFinanciamiento(String ide_geani,String ide_prfuf,String tipo,String seleccionados){
	String sql="";
			if(tipo.equals("1")){
				sql+="select * from (";
			}
	      sql+="select row_number() over(order by detalle_subactividad,a.ide_prpoa,b.ide_prfuf) as codigo,a.ide_prpoa,b.ide_prfuf,detalle_subactividad,descripcion_clasificador_prcla,num_resolucion_prpoa,detalle_prfuf,valor_asignado,valor_reformado,valor_saldo_fuente,"
			+" codigo_clasificador_prcla,codigo_subactividad,"
			+" detalle_proyecto,detalle_producto,detalle_actividad,fecha_inicio_prpoa,fecha_fin_prpoa,"
			+" presupuesto_inicial_prpoa,presupuesto_codificado_prpoa,reforma_prpoa,nom_geani as detalle_geani,detalle_geare,detalle_programa"
			+" from ("
			+" select a.ide_prpoa,codigo_clasificador_prcla,codigo_subactividad,detalle_programa,detalle_subactividad,descripcion_clasificador_prcla,programa,"
			+" detalle_proyecto,proyecto,detalle_producto,producto,detalle_actividad,actividad,"
			+" subactividad,fecha_inicio_prpoa,fecha_fin_prpoa,num_resolucion_prpoa,presupuesto_inicial_prpoa,"
			+" presupuesto_codificado_prpoa,reforma_prpoa,nom_geani as detalle_geani,detalle_geare"
			+" from pre_poa a left join  gen_anio b on a.ide_geani= b.ide_geani left join pre_clasificador c on a.ide_prcla = c.ide_prcla left join" 
			+" (select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto,"
			+" proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,"
			+" detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a ," 
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, "
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c, "
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, "
			+" (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa"
			+" from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup"
			+" and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup ) f on a.ide_prfup = f.ide_prfup"
			+" left join gen_area g on a.ide_geare=g.ide_geare where a.ide_geani="+ide_geani+" order by codigo_subactividad,a.ide_prpoa" 
			+" ) a left join ("
			+" select a.ide_prpoa,a.ide_prfuf,detalle_prfuf,valor_asignado,(case when valor_reformado is null then 0 else valor_reformado end) as valor_reformado,"
			+" valor_asignado + (case when valor_reformado is null then 0 else valor_reformado end) as valor_saldo_fuente"
			+" from ("
			+" select ide_prpoa,a.ide_prfuf,detalle_prfuf,sum(valor_financiamiento_prpof ) as valor_asignado"
			+" from pre_poa_financiamiento a, pre_fuente_financiamiento b where a.ide_prfuf = b.ide_prfuf group by ide_prpoa,a.ide_prfuf,detalle_prfuf"
			+" ) a left join ("
			+" select ide_prpoa,ide_prfuf,sum(valor_reformado_prprf) as valor_reformado from pre_poa_reforma_fuente group by ide_prpoa,ide_prfuf"
			+" ) b on a.ide_prpoa = b.ide_prpoa and a.ide_prfuf = b.ide_prfuf"
			+" ) b on a.ide_prpoa = b.ide_prpoa where b.ide_prfuf ="+ide_prfuf
			+" order by detalle_subactividad,a.ide_prpoa,b.ide_prfuf";
	      if(tipo.equals("1")){
	    	  sql+=" ) a where codigo in ("+seleccionados+")";
	      }
	     // System.out.println("sql "+sql);
	return sql;	
}
public TablaGenerica getTablaGenericaPoa(String ide_prpoa) {
	TablaGenerica tab_poa=utilitario.consultar("select ide_prpoa,b.ide_prcla,b.codigo_clasificador_prcla,b.descripcion_clasificador_prcla,presupuesto_codificado_prpoa " +
			" from pre_poa a,pre_clasificador b where a.ide_prcla=b.ide_prcla " +
			" and  ide_prpoa in("+ide_prpoa+")order by descripcion_clasificador_prcla");
	return tab_poa;
	
}

public TablaGenerica getTablaGenericaCert(String ide_prpoa) {
	TablaGenerica tab_poa=utilitario.consultar("select b.ide_prpoa,b.ide_prfuf,a.ide_prcer,b.valor_certificado_prpoc,b.ide_prpoc,(case when comprometido is null then 0 else comprometido end) as comprometido,"
			+" b.valor_certificado_prpoc - (case when comprometido is null then 0 else comprometido end) as saldo_comprometer,detalle_prcer"
			+" from pre_certificacion a"
			+" left join pre_poa_certificacion b on a.ide_prcer = b.ide_prcer"
			+" left join (select sum(comprometido_prpot) as comprometido,ide_prpoc from pre_poa_tramite group by ide_prpoc) c on b.ide_prpoc = c.ide_prpoc where a.ide_prcer  in ("+ide_prpoa+")");
	return tab_poa;
	
}
public String getSaldoPoa(String ide_prpoa){
	String tab_programa=("select a.ide_prpoa,valor_financiamiento_prpof + (case when valor_reformado is null then 0 else valor_reformado end )- (case when valor_certificado_prpoc is null then 0 else valor_certificado_prpoc end) - (case when valor_liquidado is null then 0 else valor_liquidado end) as saldo_poa,"
			+" c.ide_prfuf,valor_financiamiento_prpof,valor_certificado_prpoc,valor_reformado,valor_liquidado from pre_poa a left join pre_poa_financiamiento c on a.ide_prpoa=c.ide_prpoa"
			+" left join (select (case when  sum(valor_certificado_prpoc) is null then 0 else sum(valor_certificado_prpoc) end) as valor_certificado_prpoc,ide_prpoa,ide_prfuf from pre_poa_certificacion group by ide_prpoa,ide_prfuf) b on a.ide_prpoa = b.ide_prpoa and b.ide_prfuf = c.ide_prfuf"
			+" left join ( select sum(valor_reformado_prprf) as valor_reformado,ide_prpoa,ide_prfuf from pre_poa_reforma_fuente group by ide_prpoa,ide_prfuf) d on a.ide_prpoa = d.ide_prpoa and d.ide_prfuf=c.ide_prfuf"
			+" left join (select sum(valor_liquidado_prdcl) as valor_liquidado,ide_prpoa,ide_prfuf from  pre_detalle_liquida_certif group by ide_prpoa,ide_prfuf) e on a.ide_prpoa = e.ide_prpoa and d.ide_prfuf=e.ide_prfuf"
			+" where a.ide_prpoa ="+ide_prpoa);
	return tab_programa;
	
}

public String getPrograma (String activo){
	String tab_programa=("select  ide_prpro,cod_programa_prpro,codigo_prfup,detalle_prfup,codigo_clasificador_prcla,descripcion_clasificador_prcla " +
                            " from pre_programa a, pre_funcion_programa b,pre_clasificador c" +
                            " where a.ide_prfup = b.ide_prfup and a.ide_prcla = c.ide_prcla" +
                            " and activo_prpro in ("+activo+") order by cod_programa_prpro");
	return tab_programa;
	
}
public String getTotalCertificadoPoa (String ide_prcer){
	String tab_certificacion=("select ide_prcer,ide_prpoa,sum(valor_certificado_prpoc)  as total_certificado from pre_poa_certificacion where ide_prcer in ("+ide_prcer+") group by ide_prcer,ide_prpoa");
	return tab_certificacion;
	
}
public TablaGenerica getTablaGenericaPrograma(String ide_prpro){
	TablaGenerica tab_programa=utilitario.consultar("select  ide_prpro,cod_programa_prpro from pre_programa where ide_prpro in ("+ide_prpro+")" +
			" order by cod_programa_prpro");
	return tab_programa;
}
public String getCertificacion(String activo){
	String tab_certificacion=("select ide_prcer,nro_certificacion_prcer,num_documento_prcer,detalle_prcer,fecha_prcer " +
			"from pre_certificacion where activo_prcer  in ("+activo+") order by num_documento_prcer");
	return tab_certificacion;
	
}
/**
 * Metodo que devuelve el POA a ser aprobado para la generacion del Presupuesto Inicial de Gastos
 * @param estado recibe el o los estados true y false, ejemplo: true o false
 * @param ide_geani recibe el Año para filtar el POA 
 * @return String SQL POA
 */
public String getPoaPorAprobarse(String estado,String ide_geani){
 
 String tab_presupesto="select ide_prpoa,a.ide_prcla,a.ide_prfup,presupuesto_inicial_prpoa,codigo_clasificador_prcla,codigo_subactividad,descripcion_clasificador_prcla,"
+" detalle_subactividad,detalle_actividad,d.ide_geani,nom_geani as detalle_geani"
+" from pre_poa a"
+" left join ( "
+" select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,"
+" detalle_producto,producto,detalle_proyecto,proyecto,detalle_programa ,programa"
+" from ("
+" select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad,detalle_prnfp as subactividad"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b"
+" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5"
+" ) a , ("
+" select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b"
+" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4"
+" ) b, ("
+" select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b"
+" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3"
+" ) c, ("
+" select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b"
+" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2"
+" ) d, ("
+" select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa"
+" from pre_funcion_programa a, pre_nivel_funcion_programa b"
+" where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1"
+" ) e"
+" where a.pre_ide_prfup = b.ide_prfup"
+" and b.pre_ide_prfup = c.ide_prfup"
+" and c.pre_ide_prfup = d.ide_prfup"
+" and d.pre_ide_prfup = e.ide_prfup"
 +" ) b on a.ide_prfup = b.ide_prfup"
+" left join pre_clasificador c on a.ide_prcla = c.ide_prcla" 
+" left join ( select a.ide_geani,ide_prfup,nom_geani as detalle_geani from cont_vigente a, gen_anio b where  a.ide_geani = b.ide_geani" 
+" and not ide_prfup is null order by nom_geani desc"
  +" ) d on a.ide_prfup = d.ide_prfup"
+" where activo_prpoa in ("+estado+") and ide_prpro is null and d.ide_geani ="+ide_geani
+" order by codigo_clasificador_prcla,codigo_subactividad";
 	 return tab_presupesto;
		 
}
/**
 * Metodo que devuelve el valor del financiamiento inicial por fuente de financiamiento
 * @param ide_prfuf recibe el codigo de la fuente de financiamiento
 * @param ide_geani recibe el Año para filtar el POA 
 * @return String SQL Valor inicial fuente de financiamiento
 */
public String getInicialFuenteFinanciamiento(String ide_prfuf,String ide_geani){
	String tab_certificacion=("select ide_prfuf,sum(valor_prffi) as valor from pre_fuente_financiamiento_ini where ide_geani="+ide_geani+" and ide_prfuf ="+ide_prfuf+" group by ide_prfuf");
	return tab_certificacion;
	
}
/**
 * Metodo que devuelve el valor del ejecucion por fuente de financiamiento
 * @param ide_prfuf recibe el codigo de la fuente de financiamiento
 * @param ide_geani recibe el Año para filtar el POA 
 * @return String SQL Valor ejecutado en el poa por fuente de financiamiento
 */
public String getEjecutaFuenteFinanciamiento(String ide_prfuf,String ide_geani,String codigo){
	//System.out.println("entre al codigo "+codigo);
	String tab_certificacion=("select ide_prfuf, sum (valor) as valor from ("
			+" select ide_prfuf, sum(valor_financiamiento_prpof) as valor from pre_poa_financiamiento where ide_prfuf="+ide_prfuf+" and ide_prpoa in ("
			+" 		select ide_prpoa from pre_poa where ide_geani = "+ide_geani+") ");
				if(codigo==null){
					tab_certificacion+=" and 1=1 ";
				}
								
				else{
					tab_certificacion+=" and not ide_prpof ="+codigo;
				}
			tab_certificacion+= " group by ide_prfuf"
				+" 	union"
				+" 	select ide_prfuf, sum(valor_reformado_prprf) as valor from pre_poa_reforma_fuente where ide_prfuf="+ide_prfuf+" and ide_prpoa in ("
					+" 		select ide_prpoa from pre_poa where ide_geani ="+ide_geani+") and aprobado_prprf =true group by ide_prfuf"
					+" ) a group by ide_prfuf";
			//System.out.println("ejecuon financiamiento "+tab_certificacion);
	return tab_certificacion;
	
}
public String saldoFuenteFinanciamiento(String ide_geani){
	String tab_certificacion="select a.ide_prfuf,detalle_prfuf,a.valor, (case when b.valor is null then 0 else b.valor end) as valor_ejecutado,"
			+" a.valor - (case when b.valor is null then 0 else b.valor end) as saldo"
			+" from ("
				+" 	select ide_prfuf,sum(valor_prffi) as valor from pre_fuente_financiamiento_ini where ide_geani="+ide_geani+" group by ide_prfuf"
					+" ) a"
					+" left join ("
						+" 	select ide_prfuf, (case when sum (valor) is null then 0 else sum (valor) end) as valor from ("
							+" 		select ide_prfuf, sum(valor_financiamiento_prpof) as valor from pre_poa_financiamiento where ide_prpoa in ("
								+" 			select ide_prpoa from pre_poa where ide_geani = "+ide_geani+" ) "
								+"			group by ide_prfuf"
									+" union"
									+" select ide_prfuf, sum(valor_reformado_prprf) as valor from pre_poa_reforma_fuente where  ide_prpoa in ("
										+" 	select ide_prpoa from pre_poa where ide_geani ="+ide_geani+" ) and aprobado_prprf =true group by ide_prfuf"
									+" ) a group by ide_prfuf"
							+" ) b on a.ide_prfuf = b.ide_prfuf"
							+" left join pre_fuente_financiamiento c on a.ide_prfuf = c.ide_prfuf"
							+" order by detalle_prfuf";
			return tab_certificacion;
}
public void apruebaPoa(String ide_poa,String codigo){
	String sql="insert into pre_programa(ide_prpro,ide_prfup,ide_prcla,cod_programa_prpro,activo_prpro)"
			+" select "+codigo+" as codigo,"
			+" a.ide_prfup,a.ide_prcla,codigo_clasificador_prcla||'.'||codigo_prfup as nuevo_codigo,true"
			+" from ( select a.ide_prfup,a.ide_prcla "
			+" 	from pre_poa a left join pre_programa b on a.ide_prfup = b.ide_prfup and a.ide_prcla=b.ide_prcla"
			+" where b.ide_prfup is null and b.ide_prcla is null"
			+" and ide_prpoa in ("+ide_poa+")"
			+" ) a,pre_funcion_programa b, pre_clasificador c"
			+" where a.ide_prfup = b.ide_prfup and a.ide_prcla = c.ide_prcla order by codigo_clasificador_prcla; update pre_poa set activo_prpoa=true where ide_prpoa in ("+ide_poa+");"
			+"";
	
		utilitario.getConexion().ejecutarSql(sql);
		
 }
public void insertaVigente(String codigo,String programa,String anio){
	String sql="insert into cont_vigente (ide_covig,ide_prpro,ide_geani,activo_covig)"
			+" values ("+codigo+","+programa+","+anio+",true  )";
	utilitario.getConexion().ejecutarSql(sql);

}
public void insertaFuenteEjecucion(String codigo,String ide_poa){
	String sql="insert into pre_poa_fuente_ejecucion (ide_prpfe,ide_prfuf,ide_prpoa,valor_certificado_prpfe,valor_compromiso_prpfe,valor_devengado_prpfe,activo_prpfe)"
			+" select row_number() over(order by a.ide_prfuf)+"+codigo+" as codigo,a.ide_prfuf,a.ide_prpoa,0,0,0,true "
			+" from pre_poa_financiamiento a"
			+" left join pre_poa_fuente_ejecucion b on a.ide_prfuf=b.ide_prfuf and a.ide_prpoa=b.ide_prpoa"
			+" where b.ide_prfuf is null and b.ide_prpoa is null and a.ide_prpoa ="+ide_poa;
	utilitario.getConexion().ejecutarSql(sql);

}
String sql="";
public void trigActualizaReformaFuente(String ide_prpoa){
	 sql="delete from pre_poa_reforma  where ide_prpoa= "+ide_prpoa+";"
				+" insert into pre_poa_reforma (ide_prpor,ide_prpoa,valor_reformado_prpor,resolucion_prpor,activo_prpor,fecha_prpor,saldo_actual_prpor)"
				+" select row_number()over(order by ide_prpoa,fecha_prprf) + (select (case when max(ide_prpor) is null then 0 else max(ide_prpor) end) as maximo from pre_poa_reforma) as codigo,"
				+" ide_prpoa,valor_reformado,resolucion_prprf,estado,fecha_prprf,saldo"
				+" from ("
				+" select ide_prpoa,sum(valor_reformado_prprf) as valor_reformado,resolucion_prprf,true as estado,fecha_prprf,sum(saldo_actual_prprf) as saldo"
				+" from pre_poa_reforma_fuente a where activo_prprf=true"
				+" group by ide_prpoa,resolucion_prprf,fecha_prprf having ide_prpoa="+ide_prpoa+" order by ide_prpoa" 
				+" ) a"
				+" order by ide_prpoa,fecha_prprf;";
	utilitario.getConexion().ejecutarSql(sql);

}
public void trigActualizaReforma(String ide_prpoa){
	 sql="update pre_poa set reforma_prpoa =valor_reformado"
			 +" from ( select ide_prpoa,sum(valor_reformado_prpor) as valor_reformado from pre_poa_reforma a group by ide_prpoa having ide_prpoa="+ide_prpoa
			 +" 		 ) a where a.ide_prpoa = pre_poa.ide_prpoa; update pre_poa set presupuesto_codificado_prpoa=presupuesto_inicial_prpoa+reforma_prpoa where ide_prpoa="+ide_prpoa;
	utilitario.getConexion().ejecutarSql(sql);

}
public void trigValidaFuenteEjecucion(String ide_prpoa,String ide_prfuf){
	 sql="insert into pre_poa_fuente_ejecucion (ide_prpfe,ide_prpoa,ide_prfuf,valor_certificado_prpfe,valor_compromiso_prpfe,valor_devengado_prpfe,activo_prpfe)"
			 +" select row_number()over (order by ide_prfuf) +(select ( case when max (ide_prpfe) is null then 0 else max (ide_prpfe) end) as codigo from pre_poa_fuente_ejecucion) as codigo,"
			 +" ide_prpoa,ide_prfuf,0,0,0,true from ( select a.ide_prpoa,a.ide_prfuf from pre_poa_financiamiento a left join pre_poa_fuente_ejecucion b on a.ide_prpoa = b.ide_prpoa and a.ide_prfuf = b.ide_prfuf"
			 +" where a.ide_prpoa = "+ide_prpoa+" and a.ide_prfuf= "+ide_prfuf+" and b.ide_prfuf is null ) a";
	utilitario.getConexion().ejecutarSql(sql);

}

public void trigEjecutaCertificacion(String ide_prpoa,String ide_prfuf){
	 sql="update pre_poa_fuente_ejecucion set valor_certificado_prpfe=valor from ("
			 +"  select ide_prpoa,sum(valor_certificado_prpoc) as valor,activo_prpoc,ide_prfuf from pre_poa_certificacion where activo_prpoc = true and ide_prpoa="+ide_prpoa+" and ide_prfuf ="+ide_prfuf+" group by ide_prpoa,activo_prpoc,ide_prfuf"
		 +" ) a where a.ide_prpoa = pre_poa_fuente_ejecucion.ide_prpoa  and a.ide_prfuf = pre_poa_fuente_ejecucion.ide_prfuf; ";
	utilitario.getConexion().ejecutarSql(sql);

}
public void trigActualizaCertificadoPoa(String ide_prpoa){
	 sql="update pre_poa set valor_certificado_prpoa =valor from ("
			 +" select sum(valor_certificado_prpfe) as valor,ide_prpoa from pre_poa_fuente_ejecucion where ide_prpoa="+ide_prpoa+" group by ide_prpoa) a where a.ide_prpoa=pre_poa.ide_prpoa";
	utilitario.getConexion().ejecutarSql(sql);

}
public void trigCertificacionPreMensual(String ide_prcer){
	 sql="delete from pre_mensual where ide_prcer = "+ide_prcer+";"
			 +" INSERT INTO pre_mensual(ide_prmen, ide_pranu,fecha_ejecucion_prmen, comprobante_prmen, devengado_prmen, cobrado_prmen,"
			 +" 		 cobradoc_prmen, pagado_prmen, comprometido_prmen, valor_anticipo_prmen, activo_prmen,  certificado_prmen, ide_prfuf,ide_prcer)"
	 +" select row_number() over(order by ide_pranu) + (select (case when max(ide_prmen) is null then 0 else max(ide_prmen) end) as codigo from pre_mensual ) as codigo,"
	 +" b.ide_pranu,fecha_prcer,num_documento_prcer,0,0,0,0,0,0,true,valor_certificado_prpoc,a.ide_prfuf,a.ide_prcer"
	 +" from pre_poa_certificacion a, pre_anual b, pre_certificacion c"
	 +" where a.ide_prpoa = b.ide_prpoa and a.ide_prcer = c.ide_prcer and a.ide_prcer = "+ide_prcer+";";
	utilitario.getConexion().ejecutarSql(sql);

}
public void trigEjecutaCompromiso(String ide_prpoa,String ide_prfuf){
	 sql="update pre_poa_fuente_ejecucion set valor_compromiso_prpfe=valor from ("
			 +"  select ide_prpoa,sum(comprometido_prpot) as valor,ide_prfuf from pre_poa_tramite where activo_prpot = true and ide_prpoa="+ide_prpoa+" and ide_prfuf ="+ide_prfuf+" group by ide_prpoa,ide_prfuf"
		 +" ) a where a.ide_prpoa = pre_poa_fuente_ejecucion.ide_prpoa  and a.ide_prfuf = pre_poa_fuente_ejecucion.ide_prfuf; ";
	utilitario.getConexion().ejecutarSql(sql);

}
public void trigActualizaCompromisoPoa(String ide_prpoa){
	 sql="update pre_poa set valor_compromiso_prpoa =valor from ("
			 +" select sum(valor_compromiso_prpfe) as valor,ide_prpoa from pre_poa_fuente_ejecucion where ide_prpoa="+ide_prpoa+" group by ide_prpoa) a where a.ide_prpoa=pre_poa.ide_prpoa";
	utilitario.getConexion().ejecutarSql(sql);

}
public void trigCompromisoPreMensual(String ide_prtra){
	 sql="delete from pre_mensual where ide_prtra in ("+ide_prtra+");"
			 +" INSERT INTO pre_mensual(ide_prmen, ide_pranu, ide_prtra,ide_comov, ide_codem, fecha_ejecucion_prmen, comprobante_prmen, devengado_prmen, cobrado_prmen," 
			 +"             cobradoc_prmen, pagado_prmen, comprometido_prmen, valor_anticipo_prmen, "
			 +"             activo_prmen,  certificado_prmen, ide_prfuf,ide_prcer)"
			 +" select row_number()over(order by a.ide_prtra)   +(select (case when max(ide_prmen) is null then 0 else max(ide_prmen) end) as codigo from pre_mensual)  as codigo,"
			 +" b.ide_pranu,a.ide_prtra,null,null,fecha_tramite_prtra,numero_oficio_prtra,0,0,0,0,comprometido_prpot,0,true,0,a.ide_prfuf,null"
			 +" from   pre_poa_tramite a,pre_anual b,pre_tramite c"
			 +" where a.ide_prpoa =b.ide_prpoa"
			 +" and a.ide_prtra =c.ide_prtra"
			 +" and a.ide_prtra ="+ide_prtra+";";
	utilitario.getConexion().ejecutarSql(sql);

}
public List getListaGruposNivelPresupuesto(){
		
		List lista=new ArrayList();
		Object fila1[] = {
				"1","1"
		};
		
		Object fila2[] = {
				"2","2"
					
		};
		Object fila3[]={
				"3","3"
				
		};
		
		Object fila4[]={
				"4","4"
				
				
		};
		
		Object fila5[]={
				"5","5"
				
		};
		Object fila6[]={
				"6","6"
		};
		
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		lista.add(fila4);
		lista.add(fila5);
		lista.add(fila6);
		return lista;
		}

	public List getListaGrupoCuentaPresupuesto() {
		//pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
		List lista = new ArrayList();
		Object fila1[] = {
				"G", "Cuenta General"
		};
		Object fila2[] = {
				"S", "Cuenta Grupal"
		};
		Object fila3[] = {
				"F", "Cuenta Final"
		};
		lista.add(fila1);
		lista.add(fila2);
		lista.add(fila3);
		return lista;
	}
}
