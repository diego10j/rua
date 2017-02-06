package paq_nomina.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import framework.aplicacion.TablaGenerica;
import paq_gestion.ejb.ServicioEmpleado;
import sistema.aplicacion.Utilitario;

@Stateless
public class ServicioSRI {
	private Utilitario utilitario = new Utilitario();

	@EJB
	private ServicioEmpleado serv_empleado;
	private Document doc_rdep;

	public String generarAnexoRDEP(String IDE_SRIMR){
		TablaGenerica tab_imp_renta=getImpuestoRenta(IDE_SRIMR);
		String  fecha_inicio = "2013-01-01";
		String   fecha_fin ="2013-12-31";
		String anioHasta =utilitario.getAnio(fecha_fin)+"";
		
		///verificar 
		TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+anioHasta+"'");
		String str_ide_anio=tab_anio.getValor("IDE_GEANI");

		TablaGenerica tab_empleados=utilitario.consultar("select distinct a.ide_gtemp,empleado,documento_identidad_gtemp,codigo_documento_identidad, " +
				"codigo_ciudad,canton,provincia,codigo_provincia,detalle_gtdir,discapacidad,detalle_gttds,detalle_gtgrd " +
				"from ( " +
				"select a.ide_gtemp,c.ide_geedp,apellido_paterno_gtemp||' '||apellido_materno_gtemp||' '||primer_nombre_gtemp||' '||segundo_nombre_gtemp as empleado " +
				",documento_identidad_gtemp,a.ide_gttdi,a.ide_gedip,detalle_gttdi,codigo_sri_gttdi as codigo_documento_identidad, " +
				"(case when discapacitado_gtemp is null then true else discapacitado_gtemp end) as discapacidad " +
				"from gth_empleado a, gth_tipo_documento_identidad b,gen_empleados_departamento_par c " +
				"where a.ide_gttdi = b.ide_gttdi and a.ide_gtemp = c.ide_gtemp ) a " +
				"left join ( " +
				"select ide_gtemp,detalle_gtdir,ide_gtdir from gth_direccion where  activo_gtdir=true  group by ide_gtemp ,detalle_gtdir,ide_gtdir " +
				"having ide_gtdir in ( " +
				"select max(ide_gtdir) as ev from gth_direccion where  activo_gtdir=true  group by ide_gtemp " +
				") ) b on a.ide_gtemp = b.ide_gtemp " +
				"left join ( " +
				"select distinct ide_geedp from ( " +
				"select * from nrh_rol where ide_nrdtn in (  select ide_nrdtn from nrh_detalle_tipo_nomina where ide_nrtin=0) " +
				") a,( " +
				"select distinct ide_geedp,ide_nrrol from nrh_detalle_rol " +
				") b,( select ide_gepro,a.ide_gemes,a.ide_geani,detalle_gemes,detalle_geani " +
				",cast((to_char(to_date('"+fecha_inicio+"','yyyy-mm-dd'),'MM')) as Integer) as mes " +
				"from gen_perido_rol a, gen_mes b, gen_anio c where a.ide_gemes = b.ide_gemes and a.ide_geani = c.ide_geani " +
				"and a.ide_gemes between cast((to_char(to_date('"+fecha_inicio+"','yyyy-mm-dd'),'MM')) as Integer) and cast((to_char(to_date('"+fecha_fin+"','yyyy-mm-dd'),'MM')) as Integer) " +
				"and detalle_geani = to_char(to_date('2013-03-01','yyyy-mm-dd'),'yyyy') ) c " +
				"where a.ide_nrrol = b.ide_nrrol and a.ide_gepro = c.ide_gepro " +
				") c on a.ide_geedp = c.ide_geedp " +
				"left join ( select a.ide_gedip,a.detalle_gedip as ciudad,a.codigo_sri_gedip as codigo_ciudad,canton,provincia,codigo_provincia " +
				"from (select * from gen_division_politica where ide_getdp=2) a " +
				"left join ( select a.ide_gedip,a.detalle_gedip as canton,b.codigo_sri_gedip as codigo_provincia,b.detalle_gedip as provincia " +
				"from (select * from gen_division_politica where ide_getdp=3) a " +
				"left join (select * from gen_division_politica where ide_getdp=1) b on a.gen_ide_gedip = b.ide_gedip " +
				") b on a.gen_ide_gedip = b.ide_gedip order by a.detalle_gedip ) d on a.ide_gedip = d.ide_gedip " +
				"left join ( select ide_gtemp,detalle_gttds,detalle_gtgrd from gth_discapacidad_empleado a, " +
				"gth_grado_discapacidad b, gth_tipo_discapacidad c " +
				"where a.ide_gtgrd= b.ide_gtgrd and a.ide_gttds = c.ide_gttds and activo_gtdie=true " +
				"and ide_gtdie in ( select max(ide_gtdie) from gth_discapacidad_empleado where activo_gtdie=true group by ide_gtemp ) " +
				") e  on a.ide_gtemp = e.ide_gtemp order by  empleado"); 


		try {

			List lis_sql = utilitario.getConexion().consultar("SELECT identificacion_empr from sis_empresa where ide_empr=" + utilitario.getVariable("ide_empr"));
			if (lis_sql != null && !lis_sql.isEmpty()) {

				doc_rdep = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				doc_rdep.setXmlVersion("1.0");
				doc_rdep.setXmlStandalone(true);

				Element raiz = doc_rdep.createElement("rdep");
				raiz.appendChild(crearElemento("numRuc", null, lis_sql.get(0) + ""));
				raiz.appendChild(crearElemento("anio", null, anioHasta));

				//      raiz.setAttribute("version", "2.0");
				doc_rdep.appendChild(raiz);

				//CABECERA
				// Element rdep = doc_rdep.createElement("rdep");
				//  raiz.appendChild(rdep);

				//DETALLE

				Element retRelDep = doc_rdep.createElement("retRelDep");
				raiz.appendChild(retRelDep);
				
				for (int i = 0; i < tab_empleados.getTotalFilas(); i++) {
					Element datRetRelDep = doc_rdep.createElement("datRetRelDep");
					retRelDep.appendChild(datRetRelDep);

					datRetRelDep.appendChild(crearElemento("tipIdRet", null, Integer.parseInt(tab_empleados.getValor(i, "codigo_documento_identidad")) + ""));
					datRetRelDep.appendChild(crearElemento("idRet", null, tab_empleados.getValor(i, "documento_identidad_gtemp")));
					datRetRelDep.appendChild(crearElemento("dirCal", null, tab_empleados.getValor(i, "detalle_gtgrd")));
					//datRetRelDep.appendChild(crearElemento("dirNum", null, tab_roles.getValor(i, "numero_geper")));
					datRetRelDep.appendChild(crearElemento("dirCiu", null, tab_empleados.getValor(i, "codigo_ciudad")));
					datRetRelDep.appendChild(crearElemento("dirProv", null, tab_empleados.getValor(i, "codigo_provincia")));
					datRetRelDep.appendChild(crearElemento("tel", null, "9999999")); //////FALTA EL TELEFONO
					datRetRelDep.appendChild(crearElemento("sisSalNet", null, "1")); //1 NETO  , 2 PARCIAL
					String ideEmpleado = tab_empleados.getValor(i, "ide_gtemp");
					datRetRelDep.appendChild(crearElemento("suelSal", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_301_sueldos_y_salarios"), str_ide_anio)));
					datRetRelDep.appendChild(crearElemento("sobSuelComRemu", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_303_subrogaciones"), str_ide_anio)));
					datRetRelDep.appendChild(crearElemento("decimTer", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_311_provisiones_decimo_tercero"), str_ide_anio)));
					datRetRelDep.appendChild(crearElemento("decimCuar", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_313_provisiones_decimo_cuarto"), str_ide_anio)));					
					datRetRelDep.appendChild(crearElemento("fondoReserva", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_315_provisiones_fondo_reserva"), str_ide_anio)));
					datRetRelDep.appendChild(crearElemento("partUtil", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_305_participacion_utilidades"), str_ide_anio)));
					datRetRelDep.appendChild(crearElemento("desauOtras", null, getSumaRubro(ideEmpleado, "-1", str_ide_anio)));
					datRetRelDep.appendChild(crearElemento("apoPerIess", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_351_aporte_personal"), str_ide_anio)));
				//	datRetRelDep.appendChild(crearElemento("deducVivienda", null, sumarRubro(ideEmpleado, "40")));
				//	datRetRelDep.appendChild(crearElemento("deducSalud", null, sumarRubro(ideEmpleado, "42")));
				//	datRetRelDep.appendChild(crearElemento("deducEduca", null, sumarRubro(ideEmpleado, "41")));
				//	datRetRelDep.appendChild(crearElemento("deducAliement", null, sumarRubro(ideEmpleado, "43")));
				//	datRetRelDep.appendChild(crearElemento("deducVestim", null, sumarRubro(ideEmpleado, "39")));
				//	datRetRelDep.appendChild(crearElemento("rebEspDiscap", null, sumarRubro(ideEmpleado, "45")));
				//	datRetRelDep.appendChild(crearElemento("rebEspTerEd", null, sumarRubro(ideEmpleado, "46")));
					
					datRetRelDep.appendChild(crearElemento("impRentEmpl", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_403_impuesto_retenido"), str_ide_anio)));
				//	datRetRelDep.appendChild(crearElemento("subTotal", null, sumarRubro(ideEmpleado, "37")));
				//	datRetRelDep.appendChild(crearElemento("numRet", null, sumarRubro(ideEmpleado, "-11111")));
				//	datRetRelDep.appendChild(crearElemento("numMesEmplead", null, tab_roles.getValor(i, "meses")));
				//	datRetRelDep.appendChild(crearElemento("intGrabGen", null, sumarRubro(ideEmpleado, "49")));
			//		datRetRelDep.appendChild(crearElemento("deduccGastosOtrEmpl", null, sumarRubro(ideEmpleado, "50")));
					datRetRelDep.appendChild(crearElemento("otrRebjOtrEmpl", null, getSumaRubro(ideEmpleado, utilitario.getVariable("p_sri_403_impuesto_retenido"), str_ide_anio)));
			//		String baseImponimbe = sumarRubro(ideEmpleado, "53");
				//	datRetRelDep.appendChild(crearElemento("basImp", null, baseImponimbe));
				//	datRetRelDep.appendChild(crearElemento("impRentCaus", null, sumarRubro(ideEmpleado, "53")));
				//	datRetRelDep.appendChild(crearElemento("valRet", null, sumarRubro(ideEmpleado, "31")));
				//	datRetRelDep.appendChild(crearElemento("valorImpempAnter", null, sumarRubro(ideEmpleado, "59")));
					datRetRelDep.appendChild(crearElemento("anioRet", null, anioHasta));
				}
				///ESCRIBE EL DOCUMENTO
				Source source = new DOMSource(doc_rdep);
				String master = System.getProperty("user.dir");
				System.out.println(master);
				String nombre = "Rdep" + anioHasta + ".xml";
				Result result = new StreamResult(new java.io.File(master + "/" + nombre)); //nombre del archivo
				
				Result console = new StreamResult(System.out);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(source, result);
				transformer.transform(source, console);
				return master + "/" + nombre;
			}
		} catch (Exception e) {
			utilitario.agregarMensajeError("No se pudo generar el Anexo", "No hay informaci�n para generar el anexo :"+e.getMessage());
		}
return null;
	}

	private Element crearElemento(String nombre, String[] atributos, String texto) {
		Element elemento = doc_rdep.createElement(nombre);
		if (atributos != null) {
			for (int i = 0; i < atributos.length; i++) {
				elemento.setAttribute(atributos[0], atributos[1]);
			}
		}
		if (texto == null) {
			texto = "";
		}
		elemento.appendChild(doc_rdep.createTextNode(texto));
		return elemento;
	}
	private Element crearElementoCDATA(String nombre, String[] atributos, String texto) {
		Element elemento = doc_rdep.createElement(nombre);
		if (atributos != null) {
			for (int i = 0; i < atributos.length; i++) {
				elemento.setAttribute(atributos[0], atributos[1]);
			}
		}
		elemento.appendChild(doc_rdep.createCDATASection(texto));
		return elemento;
	}


	public TablaGenerica getFormulario107(String IDE_GTEMP,String IDE_SRIMR,String fecha_entrega){
		TablaGenerica tab_formulario=utilitario.consultar("select '' as numero,	'' as p199_firma,	'' as p105_firma,	'' as p199,	'' as p349,	'' as p407,	'' as p405,	'' as p403,	'' as p401,	'' as p399,	'' as p381,	'' as p373,	'' as p371,	'' as p369,	'' as p367,	'' as p365,	'' as p363,	'' as p361,	'' as p353,	'' as p351,	'' as p317,	'' as p315,	'' as p313,	'' as p311,	'' as p307,	'' as p305,	'' as p303,	'' as p301,	'' as p202,	'' as p201,	'' as p106,	'' as p105,	'' as p103,	'' as p102 from dual");
		String empleados[]=IDE_GTEMP.split(",");					
		for (int i = 0; i < empleados.length; i++) {
			String numero=utilitario.getAnio(fecha_entrega)+"-"+utilitario.getMes(fecha_entrega)+"-"+(i+1);		
			IDE_GTEMP = empleados[i];		
			TablaGenerica tab_imp_renta=getImpuestoRenta(IDE_SRIMR);
			tab_formulario.insertar();
			tab_formulario.setValor("numero",numero);
			//102 Ejercicio Fiscal
			String str_anio= String.valueOf(utilitario.getAnio(tab_imp_renta.getValor("FECHA_INICIO_SRIMR")));
			tab_formulario.setValor("p102",str_anio);
			//Fecha Entrega
			tab_formulario.setValor("p103", fecha_entrega);
			TablaGenerica tab_datos_sri=getDatosConfiguracionSRI(str_anio);
			if(tab_datos_sri!=null || !tab_datos_sri.isEmpty()){
				//RUC agente de retencion
				tab_formulario.setValor("p105", tab_datos_sri.getValor("DOCUMENTO_REPRE_SRCOG"));
				tab_formulario.setValor("p105_firma", tab_datos_sri.getValor("PATH_FIRMA_REPRE_SRCOG"));
				//RAZON SOCIAL O NOMBRES Y APELLIDOS COMPLETOS
				tab_formulario.setValor("p106", tab_datos_sri.getValor("RAZON_SOCIAL_SRCOG"));			
				//RUC agente de retencion
				tab_formulario.setValor("p199", tab_datos_sri.getValor("DOCUMENTO_CONTA_SRCOG"));
				tab_formulario.setValor("p199_firma", tab_datos_sri.getValor("PATH_FIRMA_CONTA_SRCOG"));
				TablaGenerica tab_empleado= serv_empleado.getEmpleado(IDE_GTEMP);
				//CEDULA EMPLEADO			
				tab_formulario.setValor("p201", tab_empleado.getValor("DOCUMENTO_IDENTIDAD_GTEMP"));
				//NOMBRES EMPLEADO			
				tab_formulario.setValor("p202", new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")==null?"":tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ").
						append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP")==null?"":tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
						.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP")==null?"":tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ").
						append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")==null?"":tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());

				TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+str_anio+"'");
				String str_ide_anio=tab_anio.getValor("IDE_GEANI");


				//LIQUIDACI�N IMPUESTO
				//301 SUELDOS Y SALARIOS  (RUBRO 24 ) sueldos  sri_sueldos 
				tab_formulario.setValor("p301", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_301_sueldos_y_salarios"), str_ide_anio));
				//303 SOBRESUELDOS, COMISIONES, BONOS Y OTROS INGRESOS GRAVADOS (RUBRO 27) subrogaciones
				tab_formulario.setValor("p303", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_303_subrogaciones"), str_ide_anio));
				//305 PARTICIPACI�N UTILIDADES (RUBRO 76)
				tab_formulario.setValor("p305", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_305_participacion_utilidades"), str_ide_anio));
				//307 INGRESOS GRAVApDOS GENERADOS CON OTROS EMPLEADORES (RUBRO 77)
				tab_formulario.setValor("p307", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_307_ing_grav_otro_emp"), str_ide_anio));
				//311 DECIMO TERCER SUELDO (RUBRO 125)  provisiones
				tab_formulario.setValor("p311", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_311_provisiones_decimo_tercero"), str_ide_anio));
				//313 DECIMO CUARTO SUELDO (RUBRO 121)  provisiones
				tab_formulario.setValor("p313", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_313_provisiones_decimo_cuarto"), str_ide_anio));
				//315 FONDO DE RESERVA (RUBRO 120)  provisiones
				tab_formulario.setValor("p315", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_315_provisiones_fondo_reserva"), str_ide_anio));
				//317 OTROS INGRESOS EN RELACION DE DEPENDENCIA QUE NO CONSTITUYEN RENTA GRAVADA 
				tab_formulario.setValor("p317",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_317_otros_ing_rel_depencia"), str_ide_anio));			
				//351 (-) APORTE PERSONAL IESS CON ESTE EMPLEADOR
				tab_formulario.setValor("p351",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_351_aporte_personal"), str_ide_anio));
				//353 (-) APORTE PERSONAL IESS CON OTROS EMPLEADORES
				tab_formulario.setValor("p353",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_353_aporte_personal_otros_empl"), str_ide_anio));

				TablaGenerica tab_deducibles=getDeducibles(IDE_SRIMR);
				for (int j = 0; j < tab_deducibles.getTotalFilas() ; j++) {
					if(tab_deducibles.getValor(j, "ALTERNO_SRI_SRDED")!=null){					
						tab_formulario.setValor("p"+tab_deducibles.getValor(j, "ALTERNO_SRI_SRDED"),getValorDeducibleEmpleado(IDE_GTEMP, tab_deducibles.getValor(j, "IDE_SRDED")));
					}				
				}

				//371 (-) EXONERACI�N POR DISCAPACIDAD
				tab_formulario.setValor("p371",getExoneracionDiscapacidad(IDE_GTEMP, IDE_SRIMR));

				//373 (-) EXONERACI�N POR TERCERA EDAD
				tab_formulario.setValor("p373",getExoneracionTerceraEdad(IDE_GTEMP, IDE_SRIMR));

				//381 IMPUESTO A LA RENTA ASUMIDO POR ESTE EMPLEADOR
				tab_formulario.setValor("p381", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_381_imp_renta_empleador"), str_ide_anio));

				//403 VALOR DEL IMPUESTO RETENIDO Y ASUMIDO POR OTROS EMPL. DURANTE EL PERIODO DECLARADO
				tab_formulario.setValor("p403", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_403_impuesto_retenido"), str_ide_anio));
				//405 VALOR DEL IMPUESTO ASUMIDO POR ESTE EMPLEADOR
				tab_formulario.setValor("p405", getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_405_impuesto_asumido"), str_ide_anio));
				//407 VALOR DEL IMPUESTO RETENIDO AL TRABAJADOR POR ESTE EMPLEADOR
				tab_formulario.setValor("p407",getSumaRubro(IDE_GTEMP, utilitario.getVariable("p_sri_407_imp_retenido_trabajador"), str_ide_anio));
				//349 INGRESOS GRAVADOS CON ESTE EMPLEADOR (301+303+305+381)
				double v301=0,v303=0,v305=0,v307=0,v351=0,v353=0,v361=0,v363=0,v365=0,v367=0,v369=0,v371=0,v373=0,v381=0;
				try {
					v301=Double.parseDouble(tab_formulario.getValor("p301"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v303=Double.parseDouble(tab_formulario.getValor("p303"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v305=Double.parseDouble(tab_formulario.getValor("p305"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v307=Double.parseDouble(tab_formulario.getValor("p307"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v351=Double.parseDouble(tab_formulario.getValor("p351"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v353=Double.parseDouble(tab_formulario.getValor("p353"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v361=Double.parseDouble(tab_formulario.getValor("p361"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v363=Double.parseDouble(tab_formulario.getValor("p363"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v365=Double.parseDouble(tab_formulario.getValor("p365"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v367=Double.parseDouble(tab_formulario.getValor("p367"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v369=Double.parseDouble(tab_formulario.getValor("p369"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v371=Double.parseDouble(tab_formulario.getValor("p371"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v373=Double.parseDouble(tab_formulario.getValor("p373"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}
				try {
					v381=Double.parseDouble(tab_formulario.getValor("p381"));
				}			
				catch (Exception e) {
					// TODO: handle exception
				}

				double base=v301+v303+v305+v307-v351-v353-v361-v363-v365-v367-v369-v371-v373+v381;
				String str_base="---";
				if(base>=0){
					str_base=utilitario.getFormatoNumero(base);
				}

				//399 BASE IMPONIBLE GRAVADA 301+303+305+307-351-353-361-363-365-367-369-371-373+381
				tab_formulario.setValor("p399", str_base);

				//401 IMPUESTO A LA RENTA CAUSADO
				try {
					tab_formulario.setValor("p401", String.valueOf(getValorImpuestoRentaCausado(IDE_SRIMR, Double.parseDouble(tab_formulario.getValor("p399")))));

				} catch (Exception e) {
					tab_formulario.setValor("p401", "---");

				}
				
				//349 INGRESOS GRAVADOS CON ESTE EMPLEADOR (informativo) 301+303+305+381
				tab_formulario.setValor("p349", utilitario.getFormatoNumero((v301+v303+v305+v381)));
			}
			else{
				System.out.println("NO EXISTEN CONFIGURACIONES DE SRI PARA EL PERIODO SELECCIONADO :"+str_anio);
				return null;
			}

		}
		return tab_formulario;
	}



	/**
	 * Busca la fraccion basica exenta de un impuesto a la renta
	 * @param IDE_SRIMR
	 * @return
	 */
	public double getFraccionBasicaExenta(String IDE_SRIMR){

		TablaGenerica tab_fraccion=utilitario.consultar("select * from SRI_DETALLE_IMPUESTO_RENTA WHERE IDE_SRIMR="+IDE_SRIMR+" AND FRACCION_BASICA_SRDIR=0");
		if(tab_fraccion.isEmpty()){
			try {
				return Double.parseDouble(tab_fraccion.getValor("EXCESO_HASTA_SRDIR"));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return 0;
	}


	/**
	 * Calcula la exoneracion por discapacidad >=30% 
	 * @param IDE_GTEMP
	 * @return
	 */
	public String getExoneracionDiscapacidad(String IDE_GTEMP,String IDE_SRIMR){
		double dou_porcentaje=serv_empleado.getPorcentajeDiscapacidad(IDE_GTEMP);
		if(dou_porcentaje>=30){
			//doble de la fraccion basica excenta
			return utilitario.getFormatoNumero(getFraccionBasicaExenta(IDE_SRIMR)*2);
		}		
		return "---";
	}


	/**
	 * Calcula la exoneracion por tercera edad >=65 a�os 
	 * @param IDE_GTEMP
	 * @return
	 */
	public String getExoneracionTerceraEdad(String IDE_GTEMP,String IDE_SRIMR){		
		if(serv_empleado.isTerceraEdad(IDE_GTEMP)){
			//doble de la fraccion basica excenta
			return utilitario.getFormatoNumero(getFraccionBasicaExenta(IDE_SRIMR)*2);
		}		
		return "---";
	}



	public String getValorImpuestoRentaCausado(String IDE_SRIMR,double base_imponible){
		TablaGenerica tab_det_imp_renta=utilitario.consultar("select * from SRI_DETALLE_IMPUESTO_RENTA where IDE_SRIMR="+IDE_SRIMR+" " +
				"and "+base_imponible+" BETWEEN FRACCION_BASICA_SRDIR and EXCESO_HASTA_SRDIR");
		double dou_imp_renta=0;
		if(!tab_det_imp_renta.isEmpty()){
			double dou_fraccion_basica=0;
			try {
				dou_fraccion_basica=Double.parseDouble(tab_det_imp_renta.getValor("FRACCION_BASICA_SRDIR"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			double dou_por_frac_exc=0;
			try {
				dou_por_frac_exc=Double.parseDouble(tab_det_imp_renta.getValor("IMP_FRACCION_EXCEDENTE_SRDIR"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			double dou_imp_fraccion=0;
			try {
				dou_imp_fraccion=Double.parseDouble(tab_det_imp_renta.getValor("IMP_FRACCION_SRDIR"));

			} catch (Exception e) {
				// TODO: handle exception
			}			
			double dou_excedente=base_imponible-dou_fraccion_basica;			
			double dou_imp_exc=(dou_excedente*dou_por_frac_exc)/100;			
			dou_imp_renta=dou_imp_exc+dou_imp_fraccion;

		}
		return utilitario.getFormatoNumero(dou_imp_renta); 
	}


	/**
	 * Retorna el valor de un deducible de un empleado
	 * @param IDE_GTEMP
	 * @param IDE_SRDED
	 * @return
	 */
	public String getValorDeducibleEmpleado(String IDE_GTEMP,String IDE_SRDED){		
		TablaGenerica tab_deduci=utilitario.consultar("SELECT * FROM SRI_DEDUCIBLES_EMPLEADO WHERE IDE_GTEMP="+IDE_GTEMP+" AND IDE_SRDED="+IDE_SRDED);
		try {
			if(!tab_deduci.isEmpty()){
				if(tab_deduci.getValor("VALOR_DEDUCIBLE_SRDEE")!=null){
					return utilitario.getFormatoNumero(Double.parseDouble(tab_deduci.getValor("VALOR_DEDUCIBLE_SRDEE")));	
				}				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "---";
	}


	/**
	 * Retorna los deducible para el impuesto a la renta de un determinado periodo
	 * @param IDE_SRIMR
	 * @return
	 */
	public TablaGenerica getDeducibles(String IDE_SRIMR){		
		return utilitario.consultar("SELECT * FROM SRI_DEDUCIBLES WHERE IDE_SRIMR="+IDE_SRIMR+" AND ACTIVO_SRDED=true");
	}


	/**
	 * Calcula la suma de un rubro de un empledo en un anio determinado
	 * @param IDE_GTEMP
	 * @param IDE_NRRUB
	 * @return
	 */
	public String getSumaRubro(String IDE_GTEMP,String IDE_NRRUB,String anio){		
		if(anio!=null){			
			List lis=utilitario.getConexion().consultar("SELECT SUM(DETAROL.VALOR_NRDRO) AS SUMA_RUBRO FROM NRH_DETALLE_ROL detarol INNER JOIN NRH_ROL rol on DETAROL.IDE_NRROL = ROL.IDE_NRROL INNER JOIN GEN_PERIDO_ROL peri ON  ROL.IDE_GEPRO=peri.IDE_GEPRO INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR emple on detarol.IDE_GEEDP=EMPLE.IDE_GEEDP INNER JOIN NRH_DETALLE_RUBRO detrubro on detarol.IDE_NRDER=detrubro.IDE_NRDER INNER JOIN NRH_RUBRO rubr on detrubro.IDE_NRRUB=rubr.IDE_NRRUB WHERE IDE_GEANI="+anio+" AND EMPLE.IDE_GTEMP="+IDE_GTEMP+" and detrubro.IDE_NRRUB in("+IDE_NRRUB+") GROUP BY detrubro.IDE_NRRUB");
			if(!lis.isEmpty() && lis.get(0)!=null){
				return utilitario.getFormatoNumero(Double.parseDouble(String.valueOf(lis.get(0))));
			}
		}

		return "---";
	}


	/**
	 * Retorna la cabecera del  impuesto a la renta
	 * @param IDE_SRIMR
	 * @return
	 */
	public TablaGenerica getImpuestoRenta(String IDE_SRIMR){			
		return utilitario.consultar("SELECT * FROM SRI_IMPUESTO_RENTA WHERE IDE_SRIMR=".concat(IDE_SRIMR));
	}

	/**
	 * Retorna la configuracion de datos del sri en un anio determinado
	 * @param anio
	 * @return
	 */
	public TablaGenerica getDatosConfiguracionSRI(String anio){
		TablaGenerica tab_anio= utilitario.consultar("SELECT * FROM GEN_ANIO WHERE DETALLE_GEANI='"+anio+"'");
		if(!tab_anio.isEmpty()){
			return utilitario.consultar("SELECT * FROM SRI_CONFIGURACION_GENERAL WHERE IDE_GEANI="+tab_anio.getValor("IDE_GEANI"));
		}
		return null;
	}


}
