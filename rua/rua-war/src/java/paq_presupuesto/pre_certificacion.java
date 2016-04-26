package paq_presupuesto;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;
import servicios.sistema.ServicioSeguridad;

public class pre_certificacion extends Pantalla{
	private Tabla tab_certificacion=new Tabla();
	private Tabla tab_poa_certificacion=new Tabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
	private Combo com_anio=new Combo();
	private String empleado;
	public static String par_sec_certificacion;

	///reporte
	private Map p_parametros = new HashMap();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte self_reporte = new SeleccionFormatoReporte();
	private Map map_parametros = new HashMap();


  
  @EJB
 private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
  @EJB
 private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
  @EJB
private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
  @EJB
private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
			
	
	public pre_certificacion(){
		
		par_sec_certificacion =utilitario.getVariable("p_modulo_secuencialcertificacion");
		///reporte
		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		self_reporte.setId("self_reporte"); //id
		agregarComponente(self_reporte);
		
		
		///BOTON COMBO
		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		System.out.println("empleado"+empleado);
		if(empleado==null ||empleado.isEmpty()){
			utilitario.agregarNotificacionInfo("Mensaje", "No exixte usuario registrado para el registro de compras");
			return;
		}

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		
		tab_certificacion.setId("tab_certificacion");
		tab_certificacion.setHeader("CERTIFICACION PRESUPUESTARIA");
		tab_certificacion.setTabla("pre_certificacion", "ide_prcer", 1);
		tab_certificacion.setCampoOrden("ide_prcer desc");
		tab_certificacion.getColumna("IDE_GEEDP").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_certificacion.getColumna("IDE_GEEDP").setAutoCompletar();
		tab_certificacion.getColumna("gen_ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_certificacion.getColumna("gen_ide_geedp").setAutoCompletar();
		tab_certificacion.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_certificacion.getColumna("ide_gtemp").setLectura(true);
		tab_certificacion.getColumna("ide_gtemp").setAutoCompletar();
		tab_certificacion.getColumna("activo_prcer").setValorDefecto("true");
		tab_certificacion.getColumna("activo_prcer").setLectura(true);
		tab_certificacion.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_certificacion.setCondicion("ide_geani=-1"); 
		tab_certificacion.getColumna("ide_geani").setVisible(false);
		tab_certificacion.getColumna("VALOR_LIBERADO_PRCER").setMetodoChange("calcularCertificacion");
		tab_certificacion.getColumna("VALOR_LIBERADO_PRCER").setValorDefecto("0.00");
		tab_certificacion.getColumna("VALOR_LIBERADO_PRCER").setEtiqueta();
		tab_certificacion.getColumna("VALOR_LIBERADO_PRCER").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_certificacion.getColumna("VALOR_DISPONIBLE_PRCER").setEtiqueta();
		tab_certificacion.getColumna("VALOR_DISPONIBLE_PRCER").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_certificacion.getColumna("VALOR_DISPONIBLE_PRCER").setValorDefecto("0.00");
		tab_certificacion.getColumna("VALOR_CERTIFICACION_PRCER").setEtiqueta();
		tab_certificacion.getColumna("VALOR_CERTIFICACION_PRCER").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:black");
		tab_certificacion.getColumna("VALOR_CERTIFICACION_PRCER").setValorDefecto("0.00");
		tab_certificacion.setTipoFormulario(true);
		tab_certificacion.getGrid().setColumns(4);
		tab_certificacion.agregarRelacion(tab_poa_certificacion);
		tab_certificacion.dibujar();
		PanelTabla pat_certificacion=new PanelTabla();
		pat_certificacion.setPanelTabla(tab_certificacion);
		
		////// poa certificacion
		tab_poa_certificacion.setId("tab_poa_certificacion");
		tab_poa_certificacion.setHeader("POA CERTIFICACION");
		tab_poa_certificacion.setTabla("pre_poa_certificacion", "ide_prpoc", 2);
		tab_poa_certificacion.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_poa_certificacion.getColumna("ide_prpoa").setAutoCompletar();
		tab_poa_certificacion.getColumna("ide_prpoa").setLectura(true);
		tab_poa_certificacion.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_poa_certificacion.getColumna("ide_prfuf").setAutoCompletar();
		tab_poa_certificacion.getColumna("ide_prfuf").setLectura(true);		
		tab_poa_certificacion.getColumna("activo_prpoc").setValorDefecto("true");
		tab_poa_certificacion.getColumna("activo_prpoc").setLectura(true);
		tab_poa_certificacion.getColumna("valor_certificado_prpoc").setMetodoChange("calcular");
		tab_poa_certificacion.getColumna("ide_prpoa").setAncho(50);
		tab_poa_certificacion.getColumna("saldo_certificacion_prpoc").setEtiqueta();
		tab_poa_certificacion.getColumna("saldo_certificacion_prpoc").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");

		tab_poa_certificacion.dibujar();
		PanelTabla pat_poa_certi=new PanelTabla();
		pat_poa_certi.setPanelTabla(tab_poa_certificacion);
		
		
		Division div_divi=new Division();
		div_divi.dividir2(pat_certificacion, pat_poa_certi, "50%", "H");
		agregarComponente(div_divi);
		
		
		/////boton buscar poa
		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar POA");
		bot_buscar.setMetodo("importarPoa");
		bar_botones.agregarBoton(bot_buscar);

		set_poa.setId("set_poa");
		set_poa.setSeleccionTabla(ser_presupuesto.getPoa("-1","true","true"),"ide_prpoa");
		set_poa.setTitle("Seleccione Poa");
		set_poa.getTab_seleccion().getColumna("codigo_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("descripcion_clasificador_prcla").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROGRAMA").setFiltro(true);//pone filtro
		set_poa.getTab_seleccion().getColumna("PROGRAMA").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PROYECTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("PRODUCTO").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("ACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("DETALLE_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("CODIGO_SUBACTIVIDAD").setFiltro(true);
		set_poa.getTab_seleccion().getColumna("NUM_RESOLUCION_PRPOA").setFiltro(true);
		set_poa.getBot_aceptar().setMetodo("aceptarPoa");
		agregarComponente(set_poa);

	}
	
	public int validaValorCertificado(){
		
		double valor_certificado=0;
		double saldo_certificado=0;
		int retorna=0;
		
		for (int i=0;i<tab_poa_certificacion.getTotalFilas();i++)
		{	
		valor_certificado =Double.parseDouble(tab_poa_certificacion.getValor(i,"valor_certificado_prpoc"));
		saldo_certificado=Double.parseDouble(tab_poa_certificacion.getValor(i,"saldo_certificacion_prpoc"));
		if(valor_certificado <=0)
		{
			utilitario.agregarNotificacionInfo("No se puede generar la Certificación Presupuestaria", "Ingrese un valor superior a cero o un valor positivo");
			retorna +=retorna+1;
			
		}
		if(valor_certificado>saldo_certificado){
			utilitario.agregarNotificacionInfo("No se puede generar la Certificación Presupuestaria", "Ingrese un valor igual o menor al disponible de saldo");
			retorna +=retorna+1;
			
		}
		}
		return retorna;
	}
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_certificacion.setCondicion("ide_geani="+com_anio.getValue());
			tab_certificacion.ejecutarSql();
			tab_poa_certificacion.ejecutarValorForanea(tab_certificacion.getValorSeleccionado());


		}
	}
	//////Calcular certificacion
	public void calcularCertificacion (){
		double dou_valor_certif=0;
		double dou_valor_liberado=0;
		double dou_valor_disponible=0;
		
		try {
			//Obtenemos el valor de la cantidad
			dou_valor_liberado=Double.parseDouble(tab_certificacion.getValor("VALOR_LIBERADO_PRCER"));
		} catch (Exception e) {
		}
		
        TablaGenerica saldo_poa = utilitario.consultar(ser_presupuesto.getSaldoPoa(tab_poa_certificacion.getValor("ide_prpoa")));
		String saldo_poa_str = saldo_poa.getValor("saldo_poa");
		Double dou_saldo_poa =Double.parseDouble(saldo_poa_str);
		
		////suma la los mismos valores
		String valorcert=tab_poa_certificacion.getSumaColumna("valor_certificado_prpoc")+"";
		dou_valor_certif=Double.parseDouble(valorcert);
		/*if(dou_valor_certif > dou_saldo_poa || dou_saldo_poa <=0 ){
			utilitario.agregarNotificacionInfo("Valor Excedido", "Valor de la certificacion por POA excede su saldo, es un avalor negativo o valor cero verifique");
			return;
		}*/
		///valor disponible
		dou_valor_disponible=dou_valor_certif-dou_valor_liberado;
				
		
		//Asignamos el total a la tabla detalle, con 2 decimales
		tab_certificacion.setValor("VALOR_CERTIFICACION_PRCER",utilitario.getFormatoNumero(valorcert,2));
		tab_certificacion.setValor("VALOR_DISPONIBLE_PRCER", utilitario.getFormatoNumero(dou_valor_disponible));
		tab_certificacion.modificar(tab_certificacion.getFilaActual());//para que haga el update

		utilitario.addUpdateTabla(tab_certificacion, "VALOR_CERTIFICACION_PRCER,VALOR_DISPONIBLE_PRCER", "");	
			
	}
	
	////para llamar al metodo
	public void calcular(AjaxBehaviorEvent evt) {
		tab_poa_certificacion.modificar(evt); //Siempre es la primera linea
		calcularCertificacion();

	}

	
////importar poa
	public void importarPoa(){
		//System.out.println(" ingresar al importar");
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}
		else if(tab_certificacion.isEmpty()){
			utilitario.agregarMensajeInfo("No puede buscar un POA", "Debe tener una Certificación Presupuestaria");
		}

		set_poa.getTab_seleccion().setSql(ser_presupuesto.getPoa(com_anio.getValue().toString(),"true","true"));
		set_poa.getTab_seleccion().ejecutarSql();
		set_poa.dibujar();

	}

	public  void aceptarPoa(){
		String str_seleccionados= set_poa.getSeleccionados();

		if (str_seleccionados!=null){
			TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaPoa(str_seleccionados);		
			for(int i=0;i<tab_poa.getTotalFilas();i++){
				TablaGenerica saldo_poa=utilitario.consultar(ser_presupuesto.getSaldoPoa(tab_poa.getValor( i,"ide_prpoa")));
				for(int j=0;j<saldo_poa.getTotalFilas();j++){
					
					
						tab_poa_certificacion.insertar();
						tab_poa_certificacion.setValor("ide_prpoa", tab_poa.getValor( i,"ide_prpoa"));
						tab_poa_certificacion.setValor("valor_certificado_prpoc", saldo_poa.getValor(j,"saldo_poa"));
						tab_poa_certificacion.setValor("ide_prfuf", saldo_poa.getValor(j,"ide_prfuf"));
						tab_poa_certificacion.setValor("saldo_certificacion_prpoc",saldo_poa.getValor(j,"saldo_poa"));
						calcularCertificacion ();
				}
				
			}
			set_poa.cerrar();
			utilitario.addUpdate("tab_poa_certificacion");
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
		}
	
	}

	//reporte
public void abrirListaReportes() {
	// TODO Auto-generated method stub
	rep_reporte.dibujar();
}
public void aceptarReporte(){
	if(rep_reporte.getReporteSelecionado().equals("Certificación Presupuestaria"));{
		TablaGenerica tab_reporte=utilitario.consultar("select ide_geani,nom_geani from gen_anio where ide_geani="+com_anio.getValue());
		if (rep_reporte.isVisible()){
			p_parametros=new HashMap();		
			rep_reporte.cerrar();	
			p_parametros.put("titulo","CERTIFICACION PRESUPUESTARIA");
			p_parametros.put("ide_prcer", Integer.parseInt(tab_certificacion.getValor("ide_prcer")));
			p_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
			p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
			p_parametros.put("ide_geani", Integer.parseInt("1"));

			self_reporte.setSeleccionFormatoReporte(p_parametros,rep_reporte.getPath());
			
		self_reporte.dibujar();
		
		}
		else{
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha Seleccionado Ningun Registro");

		}
	}
		
}
	



	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
		

		
		
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		
		}
		if(tab_certificacion.isFocus()){
			tab_certificacion.insertar();
			tab_certificacion.setValor("ide_gtemp",ide_gtempxx );
			tab_certificacion.setValor("ide_geedp",ide_gtempxx );
			tab_certificacion.setValor("ide_geani",com_anio.getValue()+"");
			tab_certificacion.setValor("nro_certificacion_prcer",ser_contabilidad.numeroSecuencial(par_sec_certificacion));
			tab_certificacion.setValor("fecha_prcer", utilitario.getFechaActual());
			utilitario.addUpdate("tab_certificacion");

		}
		else if(tab_poa_certificacion.isFocus()){
			utilitario.agregarMensajeInfo("No puede insertar", "Debe seleccionar POA");
		}
		

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if(tab_poa_certificacion.getTotalFilas()>0){
			
			if(validaValorCertificado()>0){
				//utilitario.agregarMensajeError("No se puede guardar", "Nos epudo guaradr");
				return;
			}
			//calcularCertificacion ();
		}
		
		
		
		if(tab_certificacion.isFilaInsertada()){
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sec_certificacion), par_sec_certificacion);
		}
		if(tab_certificacion.guardar()){

			if(tab_poa_certificacion.guardar()){
				guardarPantalla();
				if(tab_poa_certificacion.getTotalFilas()>0){
					for(int i=0;i<tab_poa_certificacion.getTotalFilas();i++){
						ser_presupuesto.trigValidaFuenteEjecucion(tab_poa_certificacion.getValor(i, "ide_prpoa"),tab_poa_certificacion.getValor(i, "ide_prfuf"));
						ser_presupuesto.trigEjecutaCertificacion(tab_poa_certificacion.getValor(i, "ide_prpoa"),tab_poa_certificacion.getValor(i, "ide_prfuf"));
						ser_presupuesto.trigActualizaCertificadoPoa(tab_poa_certificacion.getValor(i, "ide_prpoa"));
					}
					
					//Triger envia a la ejcucion presupuestaria pre_mensual
					ser_presupuesto.trigCertificacionPreMensual(tab_certificacion.getValor("ide_prcer"));
				}
								
			}
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
		utilitario.getTablaisFocus().eliminar();
		//calcularCertificacion ();
	}

	public Tabla getTab_certificacion() {
		return tab_certificacion;
	}

	public void setTab_certificacion(Tabla tab_certificacion) {
		this.tab_certificacion = tab_certificacion;
	}

	public Tabla getTab_poa_certificacion() {
		return tab_poa_certificacion;
	}

	public void setTab_poa_certificacion(Tabla tab_poa_certificacion) {
		this.tab_poa_certificacion = tab_poa_certificacion;
	}
	public SeleccionTabla getSet_poa() {
		return set_poa;
	}
	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public Map getP_parametros() {
		return p_parametros;
	}

	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}

	public Reporte getRep_reporte() {
		return rep_reporte;
	}

	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}

	public SeleccionFormatoReporte getSelf_reporte() {
		return self_reporte;
	}

	public void setSelf_reporte(SeleccionFormatoReporte self_reporte) {
		this.self_reporte = self_reporte;
	}

	public Map getMap_parametros() {
		return map_parametros;
	}

	public void setMap_parametros(Map map_parametros) {
		this.map_parametros = map_parametros;
	}
	

}
