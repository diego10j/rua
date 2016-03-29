package paq_general.ejb;

import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;
import paq_general.ejb.ServicioGeneral;
import sistema.aplicacion.Utilitario;

@Stateless
public  class ServicioGeneral  {
	
	private Utilitario utilitario= new Utilitario();
	/**
	 * Metodo que devuelve los Tipo Persona por Modulo.
	 * @param estado recibe el estado Activo=true, Inactivo=false. puede ingresar los dos estados.
	 * @param modulo recibe el modulo para filtrar los Tipos de Persona, recibe un solo valor para modulo, ejemplo 1= ACtivos Fijos, 2=Convenis, etc.
	 * @return string SQL Tipo Persona
	 */
	public String getTipoPersona(String estado,String  modulo){
		
		String tab_tipo_persona= "select a.ide_getip,detalle_getip from gen_tipo_persona a, gen_tipo_persona_modulo b" +
				" where a.ide_getip=b.ide_getip and ide_gemod in ( "+modulo+") and activo_getpm in ("+estado+") order by detalle_getip";
		
		 return tab_tipo_persona;
		
	}
	/**
	 * Metodo que devuelve los Tipo Persona por Modulo.
	 * @param estado recibe el estado Activo=true, Inactivo=false. puede ingresar los dos estados.
	 * @param modulo recibe el modulo para filtrar los Tipos de Persona, recibe un solo valor para modulo, ejemplo 1= ACtivos Fijos, 2=Convenis, etc.
	 * @return Tabla Tipo Persona.
	 */
	public TablaGenerica getTablaTipoPersona(String estado,String modulo){
		
		TablaGenerica tab_tipo_persona=utilitario.consultar("select a.ide_getip,detalle_getip from gen_tipo_persona a, gen_tipo_persona_modulo b" +
				" where a.ide_getip=b.ide_getip and ide_gemod in ("+modulo+") and activo_getpm in ("+estado+") order by detalle_getip");
		 return tab_tipo_persona;
		 
	}

	/**
	 * Metodo que devuelve el distributivo de colaboradores.
	 * @param estado recibe el estado Activo=true, Inactivo=false. puede ingresar los dos estados.
	 * @param modulo recibe el modulo para filtrar los Tipos de Persona, recibe un solo valor para modulo, ejemplo 1= ACtivos Fijos, 2=Convenis, etc.
	 * @return string SQL Tipo Persona
	 */
	public String getDistributivoColaboradores(String fecha){
		
		String tab_distributivo= "select row_number() over(order by proceso,rmu_geedp) as num_registro, a.ide_gtemp,documento_identidad_gtemp,empleado,codigo_partida_gepap,"
+" fecha_ingreso_gtemp,'' as fecha_salida, titulo_cargo_gepgc,proceso,sub_proceso,grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,"
+" lugar_trabajo,genero,detalle_gtted as formacion,detalle_gtttp as titulo,detalle_gttes as area_conocimiento,"
+" discapacitado,detalle_gttds,fecha_nacimiento_gtemp,observacion_geedp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce, 1 as orden"
+" from ("
+" select a.ide_gtemp,documento_identidad_gtemp,"
+" apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp"
+" ||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,"
+" fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,"
+" detalle_gegro as grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,"
+" (case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp"
+" from gth_empleado a,gen_empleados_departamento_par b, gen_partida_presupuestaria c,gen_partida_grupo_cargo d,gen_area e,"
+" gen_departamento f,gen_grupo_ocupacional g,gth_tipo_empleado h,gth_tipo_contrato i,sis_sucursal j,gth_genero k"
+" where a.ide_gtemp = b.ide_gtemp"
+" and activo_geedp = true"
+" and b.ide_gepgc = d.ide_gepgc"
+" and c.ide_gepap = d.ide_gepap"
+" and d.ide_geare = e.ide_geare"
+" and d.ide_gedep = f.ide_gedep"
+" and d.ide_gegro = g.ide_gegro"
+" and d.ide_gttem =h.ide_gttem"
+" and b.ide_gttco = i.ide_gttco"
+" and a.ide_gtgen = k.ide_gtgen"
+" and d.ide_sucu = j.ide_sucu"
+" and fecha_ingreso_gtemp < '"+fecha+"'"
+" ) a"
+" left join ("
+" select ide_gtemp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce from gth_documentacion_empleado where activo_gtdce = true limit 1"
+" ) b on a.ide_gtemp = b.ide_gtemp"
+" left join ("
+" select ide_gtemp,detalle_gtted,detalle_gtttp,detalle_gttes"
+" from gth_educacion_empleado a"
+" left join gth_tipo_educacion b on a.ide_gtted = b.ide_gtted"
+" left join gth_tipo_titulo_profesional c on a.ide_gtttp = c.ide_gtttp"
+" left join gth_tipo_especialidad d on a.ide_gttes = d.ide_gttes"
+" ) c on a.ide_gtemp = c.ide_gtemp"
+" left join ("
+" select ide_gtemp,detalle_gttds"
+" from gth_discapacidad_empleado a , gth_tipo_discapacidad b where a.ide_gttds = b.ide_gttds"
+" ) d on a.ide_gtemp = d.ide_gtemp"
+" union "
+" select null as num_registro,null as ide_gtemp,null as documento_identidad, 'VACANTE' as empleado,codigo_partida_gepap,"
+" null as fecha_ingreso,null as fecha_salida,titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,"
+" detalle_gegro as grupo_ocupacional,rmu_gegro,detalle_gttem,null as detalle_gttco,nom_sucu as lugar_trabajo,null as genero,"
+" null as formacion, null as titulo,null as area_conocimiento,null as discapacitado,null as detalle_gttds, null as fecha_nacimiento,"
+" null as observacion,null as fecha_entrega,null as fecha_proxima_entrega,2 as orden"
+" from gen_partida_grupo_cargo a,gen_partida_presupuestaria b,gen_area c,"
+" gen_departamento d,gen_grupo_ocupacional e,gth_tipo_empleado f,sis_sucursal h"
+" where a.ide_gepap = b.ide_gepap and a.ide_geare = c.ide_geare"
+" and a.ide_gedep = d.ide_gedep and a.ide_gegro = e.ide_gegro"
+" and a.ide_gttem = f.ide_gttem and a.ide_sucu = h.ide_sucu "
+" and activo_gepgc = true and vacante_gepgc = true"
+" union"
+" select null as num_registro, a.ide_gtemp,documento_identidad_gtemp,empleado,codigo_partida_gepap,"
+" fecha_ingreso_gtemp,fecha_finctr_geedp||'' as fecha_salida, titulo_cargo_gepgc,proceso,sub_proceso,grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,"
+" lugar_trabajo,genero,detalle_gtted as formacion,detalle_gtttp as titulo,detalle_gttes as area_conocimiento,"
+" discapacitado,detalle_gttds,fecha_nacimiento_gtemp,observacion_geedp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce, 3 as orden"
+" from ("
+" select a.ide_gtemp,documento_identidad_gtemp,"
+" apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp"
+" ||' '||(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado,codigo_partida_gepap,"
+" fecha_ingreso_gtemp, titulo_cargo_gepgc,detalle_geare as proceso,detalle_gedep as sub_proceso,"
+" detalle_gegro as grupo_ocupacional,rmu_geedp,detalle_gttem,detalle_gttco,nom_sucu as lugar_trabajo,codigo_sbs_gtgen as genero,"
+" (case when discapacitado_gtemp = true then 'SI' else 'NO' end) as discapacitado,fecha_nacimiento_gtemp,observacion_geedp,fecha_finctr_geedp"
+" from gth_empleado a,gen_empleados_departamento_par b, gen_partida_presupuestaria c,gen_partida_grupo_cargo d,gen_area e,"
+" gen_departamento f,gen_grupo_ocupacional g,gth_tipo_empleado h,gth_tipo_contrato i,sis_sucursal j,gth_genero k"
+" where a.ide_gtemp = b.ide_gtemp"
+" and activo_geedp = true"
+" and b.ide_gepgc = d.ide_gepgc"
+" and c.ide_gepap = d.ide_gepap"
+" and d.ide_geare = e.ide_geare"
+" and d.ide_gedep = f.ide_gedep"
+" and d.ide_gegro = g.ide_gegro"
+" and d.ide_gttem =h.ide_gttem"
+" and b.ide_gttco = i.ide_gttco"
+" and a.ide_gtgen = k.ide_gtgen"
+" and d.ide_sucu = j.ide_sucu"
+" and ide_geded in ("
+" select ide_geded from gen_detalle_empleado_departame where ide_geame in ("
+" select ide_geame from gen_accion_motivo_empleado where ide_geaed in ( select ide_geaed from gen_accion_empleado_depa where finiquito_contrato_geaed = true)"
+" )"
+" )"
+" and fecha_finctr_geedp between cast(extract(year from cast('"+fecha+"' as date))||'-'||extract(month from cast('"+fecha+"' as date))||'-01' as date) and '"+fecha+"'"
+" ) a"
+" left join ("
+" select ide_gtemp,fecha_entrega_declaracion_gtdce,fecha_proxima_declaracion_gtdce from gth_documentacion_empleado where activo_gtdce = true limit 1"
+" ) b on a.ide_gtemp = b.ide_gtemp"
+" left join ("
+" select ide_gtemp,detalle_gtted,detalle_gtttp,detalle_gttes"
+" from gth_educacion_empleado a"
+" left join gth_tipo_educacion b on a.ide_gtted = b.ide_gtted"
+" left join gth_tipo_titulo_profesional c on a.ide_gtttp = c.ide_gtttp"
+" left join gth_tipo_especialidad d on a.ide_gttes = d.ide_gttes"
+" ) c on a.ide_gtemp = c.ide_gtemp"
+" left join ("
+" select ide_gtemp,detalle_gttds"
+" from gth_discapacidad_empleado a , gth_tipo_discapacidad b where a.ide_gttds = b.ide_gttds"
+" ) d on a.ide_gtemp = d.ide_gtemp"
+" order by  proceso,rmu_geedp,num_registro";
		
		 return tab_distributivo;
		
	}
	
	

}
