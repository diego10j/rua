package paq_gestion.ejb;

import java.util.List;

import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;



import sistema.aplicacion.Utilitario;

@Stateless
public class ServicioEmpleado {

	private Utilitario utilitario=new Utilitario();

	
	/**metodo que retorna una fila de la tabla gen_region a la que pertenece el empleado 
	 * @param ide_gtemp
	 * @return tabla
	 */
	public TablaGenerica getRegionEmpleado(String ide_gtemp){
		if (ide_gtemp!=null && !ide_gtemp.isEmpty()){
		TablaGenerica tab_reg=utilitario.consultar("select * from GEN_REGION where IDE_GEREG in ( " +
				"select IDE_GEREG from GEN_DIVISION_POLITICA where IDE_GEDIP in ( " +
				"select IDE_GEDIP from SIS_SUCURSAL where ide_sucu in ( " +
				"select IDE_SUCU from GEN_EMPLEADOS_DEPARTAMENTO_PAR where IDE_GTEMP="+ide_gtemp+" and ACTIVO_GEEDP=TRUE)))");
		return tab_reg;
		}
		return null;
		
	}
	
	public boolean isContratoActivo(String IDE_GEEDP){
		TablaGenerica tab_emp_dep_par=utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR WHERE IDE_GEEDP="+IDE_GEEDP);
		if (tab_emp_dep_par.getTotalFilas()>0){
			if (tab_emp_dep_par.getValor("ACTIVO_GEEDP")!=null && !tab_emp_dep_par.getValor("ACTIVO_GEEDP").isEmpty()){
				if (tab_emp_dep_par.getValor("ACTIVO_GEEDP").equals("TRUE")){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param ide_gttdi
	 * @param documento_identidad_gttdi
	 * @return
	 * 
	 * metodo booleano para validar el tipo de documento de identidad cedula y ruc
	 */
	public boolean isDocumentoIdentidadValido(String ide_gttdi,String documento_identidad){
		if (ide_gttdi!=null && !ide_gttdi.isEmpty()){
			if (documento_identidad!=null && !documento_identidad.isEmpty()){
				if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))){
					if (!utilitario.validarCedula(documento_identidad)){					
						return false;
					}
				}else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))){
					if (!utilitario.validarRUC(documento_identidad)){						
						return false;
					}
				}
			}else{				
				return false;
			}
		}else{			
			return false;
		}
		return true;
	}


	/**Busca un empleado por cualquier campo 
	 * @param campo Nombre del campo al que se realizara la busqueda
	 * @param valor Valor de la busqueda
	 * @return
	 */
	public TablaGenerica getEmpleado(String campo,String valor){
		return utilitario.consultar("SELECT * FROM GTH_EMPLEADO WHERE "+campo+"='"+valor+"'");
	}

	/**Busca un empleado por el campo IDE_GEPER
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public TablaGenerica getEmpleado(String IDE_GTEMP){
		return getEmpleado("IDE_GTEMP",IDE_GTEMP);		
	}

	/**Busca un empleado por el campo DOCUMENTO_IDENTIDAD_GTEMP
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public TablaGenerica getEmpleadoDocumento(String DOCUMENTO_IDENTIDAD_GTEMP){
		return getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP",DOCUMENTO_IDENTIDAD_GTEMP);	
	}

	/**Busca si existe un empleado empleado por el campo IDE_GEPER
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public boolean isExisteEmpleado (String IDE_GTEMP){
		return !getEmpleado(IDE_GTEMP).isEmpty();
	}
	/**Busca si existe un empleado empleado por el campo DOCUMENTO_IDENTIDAD_GTEMP
	 * @param IDE_GTEMP  clave primaria del empleado
	 * @return
	 */
	public boolean isExisteEmpleadoDocumento (String DOCUMENTO_IDENTIDAD_GTEMP){
		return !getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP",DOCUMENTO_IDENTIDAD_GTEMP).isEmpty();
	}

	
	public TablaGenerica getCargasFamiliares(String IDE_GTEMP){
		TablaGenerica tab_cargas_fam=utilitario.consultar("select * from GTH_CARGAS_FAMILIARES where IDE_GTEMP="+IDE_GTEMP);
		return tab_cargas_fam;
	}

	public boolean isExisteHijosEmpelado (String IDE_GTEMP){
		TablaGenerica tab_cargas_fam=getCargasFamiliares(IDE_GTEMP);
		for (int i = 0; i < tab_cargas_fam.getTotalFilas(); i++) {
			if (tab_cargas_fam.getValor(i, "IDE_GTTPR").equals("1")
					|| tab_cargas_fam.getValor(i, "IDE_GTTPR").equals("7")){
				return true;
			}
		}
		return false;
	}
	
	/**Verifica si un empleado es de la tercera edad >=65
	 * @return
	 */
	public boolean isTerceraEdad(String IDE_GTEMP){		
		TablaGenerica tab_empleado=getEmpleado(IDE_GTEMP);
		String str_fecha=tab_empleado.getValor("FECHA_NACIMIENTO_GTEMP");
		
		if(utilitario.getEdad(str_fecha)>=65){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public String getSQLEmpleadosActivos(){
		return "SELECT IDE_GTEMP,DOCUMENTO_IDENTIDAD_GTEMP, " +
				"APELLIDO_PATERNO_GTEMP || ' ' || " +
				"APELLIDO_MATERNO_GTEMP || ' ' || " +
				"PRIMER_NOMBRE_GTEMP || ' ' || " +
				"SEGUNDO_NOMBRE_GTEMP AS NOMBRES " +
				"from GTH_EMPLEADO WHERE ACTIVO_GTEMP=TRUE ORDER BY APELLIDO_PATERNO_GTEMP";
	}
	
	public double getPorcentajeDiscapacidad(String IDE_GTEMP){
		TablaGenerica tab_discapa=utilitario.consultar("SELECT * FROM GTH_DISCAPACIDAD_EMPLEADO WHERE IDE_GTEMP="+IDE_GTEMP);
		if(!tab_discapa.isEmpty()){
			if(tab_discapa.getValor("PORCENTAJE_GTDIE")!=null){
				try {
					return Double.parseDouble(tab_discapa.getValor("PORCENTAJE_GTDIE"));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}		
		return 0;
	}

    public TablaGenerica getPartida(String IDE_GTEMP){
        return utilitario.consultar("select * from GEN_EMPLEADOS_DEPARTAMENTO_PAR where ACTIVO_GEEDP=true and IDE_GTEMP="+IDE_GTEMP);
    }
    
    
    
    public TablaGenerica getRolesEmpleado(String IDE_GEEDP) {
        // (NRESR) ESTADO DE ROL ACTIVO=1 ===Poner en parametro
        return utilitario.consultar("select IDE_NRROL,nom_geani as DETALLE_GEANI,nombre_gemes,IDE_NRDTN,ROL.IDE_GEPRO,FECHA_NRROL from NRH_ROL rol "
                + "INNER JOIN GEN_PERIDO_ROL PROL ON ROL.IDE_GEPRO=PROL.IDE_GEPRO "
                + "INNER JOIN GEN_ANIO ANIO ON PROL.IDE_GEANI=ANIO.IDE_GEANI "
                + "INNER JOIN GEN_MES mes on prol.ide_gemes=mes.ide_gemes "
                + "where IDE_NRROL in (select IDE_NRROL  from NRH_DETALLE_ROL where IDE_GEEDP="+IDE_GEEDP+" GROUP BY IDE_NRROL) "
                + "and IDE_NRESR=1"
                + "ORDER BY IDE_GEPRO desc");
    }
    
      public List getRolesEmpleadoLista(String IDE_GEEDP) {
        // (NRESR) ESTADO DE ROL ACTIVO=1 ===Poner en parametro
        return utilitario.getConexion().consultar("select IDE_NRROL,nom_geani as DETALLE_GEANI,nombre_gemes,IDE_NRDTN,ROL.IDE_GEPRO,to_CHAR(FECHA_NRROL,'DD-MM-YYYY') from NRH_ROL rol "
                + "INNER JOIN GEN_PERIDO_ROL PROL ON ROL.IDE_GEPRO=PROL.IDE_GEPRO "
                + "INNER JOIN GEN_ANIO ANIO ON PROL.IDE_GEANI=ANIO.IDE_GEANI "
                + "INNER JOIN GEN_MES mes on prol.ide_gemes=mes.ide_gemes "
                + "where IDE_NRROL in (select IDE_NRROL  from NRH_DETALLE_ROL where IDE_GEEDP="+IDE_GEEDP+" GROUP BY IDE_NRROL) "
                + "and IDE_NRESR=1"
                + "ORDER BY IDE_GEPRO desc");
    }
      
  	/**
  	 * Busca los correos de los empleados
  	 * @param IDE_GTEMP
  	 * @return
  	 */
  	public TablaGenerica getCorreoEmpleados(String IDE_GTEMP){
  		return utilitario.consultar("SELECT EMP.IDE_GTEMP,DETALLE_GTCOR FROM GTH_EMPLEADO emp " +
  				"left JOIN GTH_CORREO mail on EMP.IDE_GTEMP = MAIL.IDE_GTEMP and NOTIFICACION_GTCOR=true " +
  				"where EMP.IDE_GTEMP in("+IDE_GTEMP+")");
  	}
  	/**
  	 * Busca los correos de una partida de un empleado
  	 * @param IDE_GEEDP
  	 * @return
  	 */
  	public TablaGenerica getCorreoEmpleadoPepartamentoPartida(String IDE_GEEDP){
  		return utilitario.consultar("select detalle_gtcor,apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado, " +
  				"ide_geedp from GTH_CORREO a, GEN_EMPLEADOS_DEPARTAMENTO_PAR b,GTH_EMPLEADO c " +
  				"where a.IDE_GTEMP = c.IDE_GTEMP and b.ide_gtemp = c.IDE_GTEMP " +
  				"and activo_geedp=TRUE and notificacion_gtcor=TRUE and ide_geedp in("+IDE_GEEDP+") ");
  	}
	
}
