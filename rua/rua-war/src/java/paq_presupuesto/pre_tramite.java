package paq_presupuesto;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import java.util.HashMap;
import java.util.Map;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import paq_bodega.ejb.ServicioBodega;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_nomina.ejb.ServicioNomina;
import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;
import servicios.sistema.ServicioSeguridad;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;

public class pre_tramite extends Pantalla   {

	public static String par_tramite;
	public static String par_tramite_alterno;

	public static String par_empleado;
	public static String par_no_adjudicado;
	public static String par_proveedor;
	public static String par_estado;
	
	
	//reporte
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();


	private Tabla tab_tramite=new Tabla();
	private Tabla tab_poa_tramite=new Tabla();
	private Tabla tab_documento=new Tabla();
	private Tabla tab_archivo=new Tabla();
	private Combo com_anio=new Combo();
	private SeleccionTabla set_tramite=new SeleccionTabla();
	private SeleccionTabla set_poa=new SeleccionTabla();
	private SeleccionTabla set_tramite_alterno=new SeleccionTabla();
	private SeleccionTabla set_empleado=new SeleccionTabla();
	private SeleccionTabla set_proveedor=new SeleccionTabla();
	private SeleccionTabla set_actualiza_proveedor=new SeleccionTabla();

	private SeleccionTabla set_peticionario=new SeleccionTabla();
	private SeleccionTabla set_responsable=new SeleccionTabla();


	private String empleado;

	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);
	@EJB
	private ServicioBodega ser_bodega = (ServicioBodega ) utilitario.instanciarEJB(ServicioBodega.class);
	@EJB
	private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina ) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	public pre_tramite(){
		

		empleado=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");

		par_tramite=utilitario.getVariable("p_modulo_tramite");
		par_tramite_alterno=utilitario.getVariable("p_modulo_tramite_alterno");
		par_empleado=utilitario.getVariable("p_modulo_empleado");
		par_no_adjudicado=utilitario.getVariable("p_modulo_no_adjudicado");
		par_proveedor=utilitario.getVariable("p_modulo_proveedor");
		par_estado=utilitario.getVariable("p_modulo_estado_comprometido");

		rep_reporte.setId("rep_reporte"); //id
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");//ejecuta el metodo al aceptar reporte
		agregarComponente(rep_reporte);//agrega el componente a la pantalla
		bar_botones.agregarReporte();//aparece el boton de reportes en la barra de botones
		sef_reporte.setId("sef_reporte"); //id
		agregarComponente(sef_reporte);	

		com_anio.setCombo(ser_contabilidad.getAnioDetalle("true,false","true,false"));
		com_anio.setMetodo("seleccionaElAnio");
		bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
		bar_botones.agregarComponente(com_anio);

		tab_tramite.setId("tab_tramite");
		tab_tramite.setHeader("COMPROMISO PRESUPUESTARIO");
		tab_tramite.setTabla("pre_tramite","ide_prtra", 1);
		tab_tramite.setCampoOrden("ide_prtra desc");
		tab_tramite.getColumna("ide_geedp").setCombo(ser_nomina.servicioEmpleadoContrato("true,false"));
		tab_tramite.getColumna("ide_geedp").setLectura(true);
		tab_tramite.getColumna("ide_geedp").setAutoCompletar();
		tab_tramite.getColumna("ide_coest").setCombo("cont_estado", "ide_coest", "detalle_coest", "");
		tab_tramite.getColumna("ide_coest").setLectura(true);
		tab_tramite.getColumna("ide_coest").setAutoCompletar();
		tab_tramite.getColumna("ide_tepro").setCombo(ser_bodega.getProveedor("true,false"));
		tab_tramite.getColumna("ide_tepro").setLectura(true);
		tab_tramite.getColumna("ide_tepro").setAutoCompletar();
		tab_tramite.getColumna("ide_copag").setCombo("cont_parametros_general", "ide_copag", "detalle_copag", "");
		tab_tramite.getColumna("ide_copag").setLectura(true);
		tab_tramite.getColumna("ide_copag").setAutoCompletar();
		tab_tramite.getColumna("con_ide_copag").setCombo("cont_parametros_general", "ide_copag", "detalle_copag", "");
		tab_tramite.getColumna("con_ide_copag").setLectura(true);
		tab_tramite.getColumna("con_ide_copag").setAutoCompletar();
		tab_tramite.getColumna("ide_geani").setCombo(ser_contabilidad.getAnio("true,false","true,false"));
		tab_tramite.getColumna("ide_geani").setVisible(false);
		tab_tramite.getColumna("ide_gtemp").setCombo(ser_nomina.servicioEmpleadosActivos("true,false"));
		tab_tramite.getColumna("ide_gtemp").setLectura(true);
		tab_tramite.getColumna("ide_gtemp").setAutoCompletar();
		tab_tramite.getColumna("fecha_tramite_prtra").setValorDefecto(utilitario.getFechaActual());
		tab_tramite.setCondicion("ide_geani=-1"); 
		tab_tramite.getColumna("total_compromiso_prtra").setEtiqueta();
		tab_tramite.getColumna("total_compromiso_prtra").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		tab_tramite.getColumna("activo_prtra").setValorDefecto("true");
		tab_tramite.getColumna("activo_prtra").setLectura(true);

		tab_tramite.setTipoFormulario(true);
		tab_tramite.getGrid().setColumns(4);

		tab_tramite.agregarRelacion(tab_poa_tramite);
		tab_tramite.agregarRelacion(tab_archivo);
		tab_tramite.agregarRelacion(tab_documento);
		tab_tramite.dibujar();
		PanelTabla pat_tramite=new PanelTabla();
		pat_tramite.setPanelTabla(tab_tramite);

		///poa tramite
		tab_poa_tramite.setId("tab_poa_tramite");
		tab_poa_tramite.setHeader("POA TRAMITE");
		tab_poa_tramite.setIdCompleto("tab_tabulador:tab_poa_tramite");
		tab_poa_tramite.setTabla("pre_poa_tramite", "ide_prpot", 2);
		tab_poa_tramite.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaTodos());
		tab_poa_tramite.getColumna("ide_prpoa").setAutoCompletar();
		tab_poa_tramite.getColumna("ide_prpoa").setLectura(true);
		tab_poa_tramite.getColumna("comprometido_prpot").setMetodoChange("CalcularSuma");
		tab_poa_tramite.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		tab_poa_tramite.getColumna("ide_prfuf").setAutoCompletar();
		tab_poa_tramite.getColumna("ide_prfuf").setLectura(true);
		tab_poa_tramite.getColumna("saldo_comprometido_prpot").setEtiqueta();
		tab_poa_tramite.getColumna("saldo_comprometido_prpot").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
		
		
		tab_poa_tramite.dibujar();
		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_poa_tramite);

		//// documento habilitante
		tab_documento.setId("tab_documento");
		tab_documento.setHeader("DOCUMENTO HABILITANTE");
		tab_documento.setIdCompleto("tab_tabulador:tab_documento");
		tab_documento.setTabla("pre_documento_habilitante", "ide_prdoh", 3);
		tab_documento.dibujar();
		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_documento);

		/// ARCHIVO
		tab_archivo.setId("tab_archivo");
		tab_archivo.setHeader("ARCHIVOS ANEXOS");
		tab_archivo.setIdCompleto("tab_tabulador:tab_archivo");
		tab_archivo.setTipoFormulario(true);
		tab_archivo.setTabla("pre_archivo","ide_prarc",4);
		tab_archivo.getColumna("foto_prarc").setUpload("presupuesto");
		tab_archivo.setCondicion("ide_prtra!=null");
		tab_archivo.dibujar();
		PanelTabla pat_panel4= new PanelTabla();
		pat_panel4.setPanelTabla(tab_archivo);



		///// tabuladores
		Tabulador tab_tabulador=new Tabulador();
		tab_tabulador.setId("tab_tabulador");

		tab_tabulador.agregarTab("POA TRAMITE", pat_panel2);
		tab_tabulador.agregarTab("DOCUMENTO HABILITANTE", pat_panel3);
		tab_tabulador.agregarTab("ARCHIVOS ANEXOS", pat_panel4);

		Division div_tramite =new Division();
		div_tramite.dividir2(pat_tramite, tab_tabulador, "50%", "h");
		agregarComponente(div_tramite);

		set_tramite.setId("set_tramite");
		set_tramite.setSeleccionTabla(ser_contabilidad.getModuloParametros("true,false", par_tramite),"ide_copag");
		set_tramite.setTitle("Seleccione el Tramite");
		set_tramite.getBot_aceptar().setMetodo("aceptarTramite");
		set_tramite.setRadio();
		agregarComponente(set_tramite);

		set_tramite_alterno.setId("set_tramite_alterno");
		set_tramite_alterno.setSeleccionTabla(ser_contabilidad.getModuloParametros("true,false", par_tramite_alterno),"ide_copag");
		set_tramite_alterno.setTitle("Seleccione el Tramite Alterno");
		set_tramite_alterno.getBot_aceptar().setMetodo("aceptarTramite");
		set_tramite_alterno.setRadio();
		agregarComponente(set_tramite_alterno);

		set_empleado.setId("set_empleado");
		set_empleado.setTitle("SELECCIONE EL EMPLEADO");
		set_empleado.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_empleado.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_empleado.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		set_empleado.getBot_aceptar().setMetodo("aceptarTramite");
		set_empleado.setRadio();
		agregarComponente(set_empleado);

		set_proveedor.setId("set_proveedor");
		set_proveedor.setTitle("SELECCIONE EL PROVEEDOR");
		set_proveedor.setSeleccionTabla(ser_bodega.getProveedor("true"),"ide_tepro");
		set_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_proveedor.getBot_aceptar().setMetodo("aceptarTramite");
		set_proveedor.setRadio();
		agregarComponente(set_proveedor);

		Boton bot_buscar=new Boton();
		bot_buscar.setIcon("ui-icon-person");
		bot_buscar.setValue("Buscar Certificación Presupuestaria");
		bot_buscar.setMetodo("importarPoa");
		bar_botones.agregarBoton(bot_buscar);

		set_poa.setId("set_poa");
		set_poa.setSeleccionTabla(ser_presupuesto.cetificacion("-1"),"ide_prcer");
		set_poa.getTab_seleccion().getColumna("nro_certificacion_prcer").setFiltro(true);
		set_poa.setTitle("Seleccione Poa");
		set_poa.getBot_aceptar().setMetodo("aceptarPoa");
		agregarComponente(set_poa);
		
		Boton bot_peticionario=new Boton();
		bot_peticionario.setIcon("ui-icon-person");
		bot_peticionario.setValue("Actualizar Empleado Solicitante");
		bot_peticionario.setMetodo("importarPeticionario");
		bar_botones.agregarBoton(bot_peticionario);

		set_peticionario.setId("set_peticionario");
		set_peticionario.setTitle("SELECCIONE EL PETICIONARIO");
		set_peticionario.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true,false"),"ide_geedp");
		set_peticionario.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_peticionario.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		set_peticionario.getBot_aceptar().setMetodo("aceptarPeticionario");
		set_peticionario.setRadio();
		agregarComponente(set_peticionario);
		
		
		Boton bot_empleado=new Boton();
		bot_empleado.setIcon("ui-icon-person");
		bot_empleado.setValue("Actualizar Proveedor Solicitante");
		bot_empleado.setMetodo("actualizaProveedor");
		bar_botones.agregarBoton(bot_empleado);
		
		set_actualiza_proveedor.setId("set_actualiza_proveedor");
		set_actualiza_proveedor.setTitle("SELECCIONE EL PROVEEDOR");
		set_actualiza_proveedor.setSeleccionTabla(ser_bodega.getProveedor("true"),"ide_tepro");
		set_actualiza_proveedor.getTab_seleccion().getColumna("nombre_tepro").setFiltro(true);
		set_actualiza_proveedor.getTab_seleccion().getColumna("ruc_tepro").setFiltro(true);
		set_actualiza_proveedor.getBot_aceptar().setMetodo("aceptaProveedor");
		set_actualiza_proveedor.setRadio();
		agregarComponente(set_actualiza_proveedor);
		
		/*
		Boton bot_responsable=new Boton();
		bot_responsable.setIcon("ui-icon-person");
		bot_responsable.setValue("Agregar Responsable");
		bot_responsable.setMetodo("importarResponsable");
		bar_botones.agregarBoton(bot_responsable);
		
		set_responsable.setId("set_responsable");
		set_responsable.setTitle("SELECCIONE EL RESPONSABLE");
		set_responsable.setSeleccionTabla(ser_nomina.servicioEmpleadoContrato("true"),"ide_geedp");
		set_responsable.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		set_responsable.getTab_seleccion().getColumna("NOMBRES_APELLIDOS").setFiltro(true);
		set_responsable.getBot_aceptar().setMetodo("aceptarResponsable");
		set_responsable.setRadio();
		agregarComponente(set_responsable);
		*/
	}
public void importarPeticionario(){
		
	set_peticionario.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true,false"));
	set_peticionario.getTab_seleccion().ejecutarSql();
	set_peticionario.dibujar();
	
	}
	public void aceptarPeticionario(){

		String str_seleccionado = set_peticionario.getValorSeleccionado();
		TablaGenerica tab_empleado=ser_nomina.ideEmpleadoContrato(str_seleccionado,"true");
		if (str_seleccionado!=null){
			tab_tramite.setValor("ide_geedp",str_seleccionado);
			tab_tramite.setValor("ide_copag",par_empleado);
			tab_tramite.setValor("ide_tepro",null);

			tab_tramite.modificar(tab_tramite.getFilaActual());
			tab_tramite.guardar();
			guardarPantalla();
			set_peticionario.cerrar();
			utilitario.addUpdate("tab_tramite");
				
		}
		else {
			utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
		    }
		
	}
	public void actualizaProveedor(){
		
		set_actualiza_proveedor.getTab_seleccion().setSql(ser_bodega.getProveedor("true"));
		set_actualiza_proveedor.getTab_seleccion().ejecutarSql();
		set_actualiza_proveedor.dibujar();
		
		}
		public void aceptaProveedor(){
		
				if(set_actualiza_proveedor.getValorSeleccionado()!=null){
					tab_tramite.setValor("ide_tepro",set_actualiza_proveedor.getValorSeleccionado());
					tab_tramite.setValor("ide_copag",par_proveedor);
					tab_tramite.setValor("ide_geedp",null);

					tab_tramite.modificar(tab_tramite.getFilaActual());
					tab_tramite.guardar();
					guardarPantalla();
					set_actualiza_proveedor.cerrar();
					utilitario.addUpdate("tab_tramite");
					
				}
				   else {
					utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
				    }
				
					
			
		}
	public void importarResponsable(){
		
		set_responsable.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
		set_responsable.getTab_seleccion().ejecutarSql();
		set_responsable.dibujar();
		tab_tramite.setValor("gen_ide_geedp",set_responsable.getValorSeleccionado()); 
			utilitario.addUpdate("tab_tramite");

		}
		public void aceptarResponsable(){

			String str_seleccionado = set_responsable.getValorSeleccionado();
			TablaGenerica tab_empleado=ser_nomina.ideEmpleadoContrato(str_seleccionado,"true");
			if (str_seleccionado!=null){
				tab_tramite.setValor("gen_ide_geedp",str_seleccionado);
				tab_tramite.modificar(tab_tramite.getFilaActual());
				tab_tramite.guardar(); 
				
						
			}
			set_responsable.cerrar();
			utilitario.addUpdate("tab_tramite");
		}
	public void importarPoa(){
		//System.out.println(" ingresar al importar");
		if(com_anio.getValue()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un Año", "");
			return;
		}

		set_poa.getTab_seleccion().setSql(ser_presupuesto.cetificacion(com_anio.getValue().toString()));
		set_poa.getTab_seleccion().ejecutarSql();
		set_poa.dibujar();

	}

	public  void aceptarPoa(){
		String str_seleccionados = set_poa.getSeleccionados();

		if (str_seleccionados!=null){
			TablaGenerica tab_poa = ser_presupuesto.getTablaGenericaCert(str_seleccionados);		
			for(int i=0;i<tab_poa.getTotalFilas();i++){
				tab_poa_tramite.insertar();
				tab_poa_tramite.setValor("ide_prpoa", tab_poa.getValor(i, "ide_prpoa"));
				tab_poa_tramite.setValor("ide_prpoc", tab_poa.getValor(i, "ide_prpoc"));
				tab_poa_tramite.setValor("ide_prfuf", tab_poa.getValor(i, "ide_prfuf"));
				tab_poa_tramite.setValor("comprometido_prpot", tab_poa.getValor(i, "saldo_comprometer"));
				tab_poa_tramite.setValor("saldo_comprometido_prpot", tab_poa.getValor(i, "saldo_comprometer"));
				tab_poa_tramite.setValor("activo_prpot", "true");

			}
			tab_tramite.setValor("ide_prcer", tab_poa.getValor("ide_prcer"));
			tab_tramite.setValor("observaciones_prtra",tab_tramite.getValor("observaciones_prtra") + tab_poa.getValor("detalle_prcer"));
			tab_tramite.modificar(tab_tramite.getFilaActual());
			set_poa.cerrar();
			utilitario.addUpdate("tab_poa_tramite,tab_tramite");
		}
	}
	public void aceptarTramite(){
		if(set_tramite_alterno.isVisible()){
			if (set_tramite_alterno.getValorSeleccionado()!=null){
				tab_tramite.setValor("ide_copag", set_tramite_alterno.getValorSeleccionado());
				set_tramite_alterno.cerrar();
				if(set_tramite_alterno.getValorSeleccionado().equals(par_empleado)){

					set_empleado.getTab_seleccion().setSql(ser_nomina.servicioEmpleadoContrato("true"));
					set_empleado.getTab_seleccion().ejecutarSql();
					set_empleado.dibujar();	
					set_tramite_alterno.cerrar();


				}else if (set_tramite_alterno.getValorSeleccionado().equals(par_proveedor)){
					
					set_proveedor.getTab_seleccion().setSql(ser_bodega.getProveedor("true"));
					set_proveedor.getTab_seleccion().ejecutarSql();
					set_proveedor.dibujar();	
					set_tramite_alterno.cerrar();					
					
					
				}
				else if(set_tramite_alterno.getValorSeleccionado().equals(par_no_adjudicado)){
					tab_tramite.setValor("observaciones_prtra", "No adjudicado");
					//utilitario.agregarMensajeInfo("Seleciono ", "El no adjudicado");
				}				

			}
		}
		else if(set_proveedor.isVisible()){
			if(set_proveedor.getValorSeleccionado()!=null){
			tab_tramite.setValor("ide_tepro",set_proveedor.getValorSeleccionado());
			set_proveedor.cerrar();
			}
		   else {
			utilitario.agregarMensajeInfo("SELECCIONE OPCION", "Seleccione un registro");
		    }
		}else if(set_empleado.isVisible()){
			if(set_empleado.getValorSeleccionado()!=null){
				tab_tramite.setValor("ide_geedp",set_empleado.getValorSeleccionado());
				set_empleado.cerrar();
			}
		}
		
		utilitario.addUpdateTabla(tab_tramite, "ide_tepro,ide_geedp,observaciones_prtra,ide_copag", "");

	}
	public void seleccionaElAnio (){
		if(com_anio.getValue()!=null){
			tab_tramite.setCondicion("ide_geani="+com_anio.getValue());
			tab_tramite.ejecutarSql();
			tab_poa_tramite.ejecutarValorForanea(tab_tramite.getValorSeleccionado());
			tab_archivo.ejecutarValorForanea(tab_tramite.getValorSeleccionado());
			tab_documento.ejecutarValorForanea(tab_tramite.getValorSeleccionado());

		}
		else{
			tab_tramite.setCondicion("ide_geani=-1");
			tab_tramite.ejecutarSql();
		}
	}
	public void CalcularSuma(AjaxBehaviorEvent evt){
		tab_poa_tramite.modificar(evt); //Siempre es la primera linea
		double valor_comprometido=0;
		double valor_saldo=0;
		
		valor_comprometido= Double.parseDouble(tab_poa_tramite.getValor("comprometido_prpot"));
		valor_saldo=Double.parseDouble(tab_poa_tramite.getValor("saldo_comprometido_prpot"));
		if(valor_comprometido<=0){
			utilitario.agregarMensajeError("Ingrese un valor positivo o mayor a cero", "Ingrese un valor positivo o mayor a cero");
		    tab_poa_tramite.setValor("comprometido_prpot", "0");
		    utilitario.addUpdateTabla(tab_poa_tramite, "comprometido_prpot","");
		    return;
		}
		if(valor_saldo<valor_comprometido){
			utilitario.agregarMensajeError("No puede Exceder valor asignado", "Ingrese un valor menor igual al disponible en saldo: "+valor_saldo);
		    tab_poa_tramite.setValor("comprometido_prpot", "0");
		    utilitario.addUpdateTabla(tab_poa_tramite, "comprometido_prpot","");
		    return;
		}
		
		tab_tramite.setValor("total_compromiso_prtra",tab_poa_tramite.getSumaColumna("comprometido_prpot")+"");
		tab_tramite.modificar(tab_tramite.getFilaActual());
		utilitario.addUpdateTabla(tab_tramite, "total_compromiso_prtra","");	

	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;
		
		}
		if(tab_tramite.isFocus()){
			tab_tramite.insertar();

			String ide_gtempxx=ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
			tab_tramite.setValor("ide_coest", par_estado);
			tab_tramite.setValor("ide_geani", com_anio.getValue()+"");
			tab_tramite.setValor("ide_gtemp",ide_gtempxx );
			set_tramite_alterno.dibujar();
			utilitario.addUpdate("tab_tramite");
		}
		else if(tab_poa_tramite.isFocus()){
			tab_poa_tramite.insertar();
		}
		else if(tab_archivo.isFocus()){
			tab_archivo.insertar();
		}
		else if(tab_documento.isFocus()){
			tab_documento.insertar();
		}
	
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		if (tab_tramite.guardar()){
			if (tab_poa_tramite.guardar()){
				if (tab_documento.guardar()){
					tab_archivo.guardar();
					guardarPantalla();
					
					if(tab_poa_tramite.getTotalFilas()>0){
						for(int i=0;i<tab_poa_tramite.getTotalFilas();i++){
							ser_presupuesto.trigEjecutaCompromiso(tab_poa_tramite.getValor(i, "ide_prpoa"),tab_poa_tramite.getValor(i, "ide_prfuf"));
							ser_presupuesto.trigActualizaCompromisoPoa(tab_poa_tramite.getValor(i, "ide_prpoa"));
						}
						
						//Triger envia a la ejcucion presupuestaria pre_mensual
						ser_presupuesto.trigCompromisoPreMensual(tab_tramite.getValor("ide_prtra"));
					}
				
				}
			}
		}
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();

	}


	public Tabla getTab_tramite() {
		return tab_tramite;
	}


	public void setTab_tramite(Tabla tab_tramite) {
		this.tab_tramite = tab_tramite;
	}


	public Tabla getTab_poa_tramite() {
		return tab_poa_tramite;
	}


	public void setTab_poa_tramite(Tabla tab_poa_tramite) {
		this.tab_poa_tramite = tab_poa_tramite;
	}


	public Tabla getTab_documento() {
		return tab_documento;
	}


	public void setTab_documento(Tabla tab_documento) {
		this.tab_documento = tab_documento;
	}


	public Tabla getTab_archivo() {
		return tab_archivo;
	}


	public void setTab_archivo(Tabla tab_archivo) {
		this.tab_archivo = tab_archivo;
	}

	public Combo getCom_anio() {
		return com_anio;
	}

	public void setCom_anio(Combo com_anio) {
		this.com_anio = com_anio;
	}

	public SeleccionTabla getSet_tramite() {
		return set_tramite;
	}

	public void setSet_tramite(SeleccionTabla set_tramite) {
		this.set_tramite = set_tramite;
	}
	public SeleccionTabla getSet_poa() {
		return set_poa;
	}
	public void setSet_poa(SeleccionTabla set_poa) {
		this.set_poa = set_poa;
	}
	public SeleccionTabla getSet_tramite_alterno() {
		return set_tramite_alterno;
	}
	public void setSet_tramite_alterno(SeleccionTabla set_tramite_alterno) {
		this.set_tramite_alterno = set_tramite_alterno;
	}
	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}
	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}
	public SeleccionTabla getSet_proveedor() {
		return set_proveedor;
	}
	public void setSet_proveedor(SeleccionTabla set_proveedor) {
		this.set_proveedor = set_proveedor;
	}
	public SeleccionTabla getSet_peticionario() {
		return set_peticionario;
	}
	public void setSet_peticionario(SeleccionTabla set_peticionario) {
		this.set_peticionario = set_peticionario;
	}
	public SeleccionTabla getSet_responsable() {
		return set_responsable;
	}
	public void setSet_responsable(SeleccionTabla set_responsable) {
		this.set_responsable = set_responsable;
	}
	public void aceptarReporte() {
		  
			if (rep_reporte.getReporteSelecionado().equals("Compromiso Presupuestario")){
			
			//	if (tab_tramite.getTotalFilas()>0) {
				if (rep_reporte.isVisible()){
						p_parametros=new HashMap();				
						rep_reporte.cerrar();				
						p_parametros.put("titulo", "COMPROMISO PRESUPUESTARIO");
						p_parametros.put("ide_prcer", Integer.parseInt(tab_tramite.getValor("ide_prtra")));
						p_parametros.put("jefe_presupuesto", utilitario.getVariable("p_nombre_jefe_presupuesto"));
						p_parametros.put("coordinador_finaciero",  utilitario.getVariable("p_nombre_coordinador_fin"));
						p_parametros.put("ide_geani", Integer.parseInt("1"));
						
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
						sef_reporte.dibujar();
					//}
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun registro en la cabecera del anticipo");
				}
			}
			
		}
	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}
	public Reporte getRep_reporte() {
		return rep_reporte;
	}
	public void setRep_reporte(Reporte rep_reporte) {
		this.rep_reporte = rep_reporte;
	}
	public SeleccionFormatoReporte getSef_reporte() {
		return sef_reporte;
	}
	public void setSef_reporte(SeleccionFormatoReporte sef_reporte) {
		this.sef_reporte = sef_reporte;
	}
	public Map getP_parametros() {
		return p_parametros;
	}
	public void setP_parametros(Map p_parametros) {
		this.p_parametros = p_parametros;
	}
	public SeleccionTabla getSet_actualiza_proveedor() {
		return set_actualiza_proveedor;
	}
	public void setSet_actualiza_proveedor(SeleccionTabla set_actualiza_proveedor) {
		this.set_actualiza_proveedor = set_actualiza_proveedor;
	}

}
