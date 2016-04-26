package paq_nomina;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import paq_anticipos.ejb.ServicioAnticipo;
import paq_gestion.ejb.ServicioGestion;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;


public class pre_cuentas_cobrar_empl extends Pantalla {

	private Tabla tab_anticipo=new Tabla();
	private Tabla tab_amortizacion=new Tabla();
	private Tabla tab_anticipo_interes=new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioAnticipo ser_anticipo = (ServicioAnticipo) utilitario.instanciarEJB(ServicioAnticipo.class);
	private Tabulador tab_tabulador=new Tabulador();

	private Boton bot_pre_cancelacion=new Boton();

	private Confirmar con_guardar=new Confirmar();

	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();

	private SeleccionTabla set_amortizacion=new SeleccionTabla();
	private Tabla tab_precancelacion=new Tabla();
	
	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);
private Texto txt_usuario=new Texto();
	public pre_cuentas_cobrar_empl() {

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		String str_sql_emp=ser_gestion.getSqlEmpleadosAutocompletar();
		aut_empleado.setAutoCompletar(str_sql_emp);
		/*aut_empleado.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");*/
		//"WHERE EPAR.ACTIVO_GEEDP=true");
		
		aut_empleado.setMetodoChange("filtrarAnticiposEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);


		bot_pre_cancelacion.setValue("Pre-Cancelar");
		bot_pre_cancelacion.setMetodo("preCancelarAnticipo");
		bar_botones.agregarBoton(bot_pre_cancelacion);


		//  ANTICIPOS (division 1)

		tab_anticipo.setId("tab_anticipo");
		tab_anticipo.setTabla("NRH_ANTICIPO", "IDE_NRANT", 1);
		tab_anticipo.setTipoFormulario(true);
		tab_anticipo.getColumna("ACTIVO_NRANT").setCheck();
		tab_anticipo.getColumna("ACTIVO_NRANT").setValorDefecto("true");
		tab_anticipo.getColumna("FECHA_SOLICITUD_NRANT").setValorDefecto(utilitario.getFechaActual());
		tab_anticipo.getColumna("IDE_GEEDP").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP2").setVisible(false);
		tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setMetodoChange("cambiaMontoSolicitado");
		tab_anticipo.getColumna("NRO_MES_NRANT").setMetodoChange("cambiaNumeroMes");		
		tab_anticipo.getColumna("NRO_ANTICIPO_NRANT").setLectura(true);
		tab_anticipo.getColumna("MONTO_APROBADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("GEN_IDE_GEEDP3").setVisible(false);
		tab_anticipo.getColumna("FECHA_APROBACION_NRANT").setVisible(false);
		tab_anticipo.getColumna("FECHA_RRHH_NRANT").setVisible(false);
		tab_anticipo.getColumna("FECHA_RESOLUCION_NRANT").setVisible(false);
		tab_anticipo.getColumna("IDE_GTEMP").setVisible(false);
		tab_anticipo.getColumna("RESOLUCION_NRANT").setVisible(false);
		tab_anticipo.getColumna("IDE_NRMOA").setCombo("NRH_MOTIVO_ANTICIPO","IDE_NRMOA","DETALLE_NRMOA","ACTIVO_NRMOA=true");

		tab_anticipo.getColumna("CALIFICADO_NRANT").setCheck();
		tab_anticipo.getColumna("CALIFICADO_NRANT").setLectura(true);
		tab_anticipo.getColumna("CALIFICADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("CALIFICADO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("APROBADO_NRANT").setCheck();
		tab_anticipo.getColumna("APROBADO_NRANT").setLectura(true);
		tab_anticipo.getColumna("APROBADO_NRANT").setVisible(false);
		tab_anticipo.getColumna("APROBADO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("NRO_MEMO_NRANT").setVisible(false);
		tab_anticipo.getColumna("FECHA_AUTORIZA_NRANT").setVisible(false);
		tab_anticipo.getColumna("RAZON_AUTORIZA_NRANT").setVisible(false);
		tab_anticipo.getColumna("REAPROBADO_NRANT").setVisible(false);

		tab_anticipo.getColumna("REAPROBADO_NRANT").setCheck();
		tab_anticipo.getColumna("REAPROBADO_NRANT").setLectura(true);
		tab_anticipo.getColumna("REAPROBADO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("ANTICIPO_NRANT").setCheck();
		tab_anticipo.getColumna("ANTICIPO_NRANT").setVisible(false);
		tab_anticipo.getColumna("ANTICIPO_NRANT").setValorDefecto("false");

		tab_anticipo.getColumna("ABONO_NRANT").setCheck();
		tab_anticipo.getColumna("ABONO_NRANT").setLectura(true);
		tab_anticipo.getColumna("ABONO_NRANT").setValorDefecto("false");
		
		
		tab_anticipo.getGrid().setColumns(4);
		tab_anticipo.agregarRelacion(tab_anticipo_interes);



		tab_anticipo.setCondicion("ANTICIPO_NRANT=0 AND IDE_GEEDP=-1");
		tab_anticipo.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_anticipo);

		// DIVISION 2  (TABULADOR - (CAPACIDAD DE PAGO/AMORTIZACION/GARANTE))

		tab_tabulador.setId("tab_tabulador");

		// AMORTIZACION (TABULADOR OPCION 2)

		tab_anticipo_interes.setId("tab_anticipo_interes");
		tab_anticipo_interes.setIdCompleto("tab_tabulador:tab_anticipo_interes");
		tab_anticipo_interes.setTabla("NRH_ANTICIPO_INTERES", "IDE_NRANI", 7);
		tab_anticipo_interes.getColumna("ACTIVO_NRANI").setCheck();
		tab_anticipo_interes.getColumna("ACTIVO_NRANI").setValorDefecto("true");
		tab_anticipo_interes.getColumna("PLAZO_NRANI").setNombreVisual("PLAZO (Meses)");
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setNombreVisual("AMORTIZACION CADA (Dias)");
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setMetodoChange("calcularTasaEfectiva");
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setMetodoChange("calcularTasaEfectiva");
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("PLAZO_NRANI").setLectura(true);
		tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setValorDefecto("0");
		tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setVisible(false);
		tab_anticipo_interes.agregarRelacion(tab_amortizacion);
		tab_anticipo_interes.setTipoFormulario(true);
		tab_anticipo_interes.getGrid().setColumns(8);
		tab_anticipo_interes.setMostrarNumeroRegistros(false);
		tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setValorDefecto(utilitario.getVariable("p_tasa_interes"));
		tab_anticipo_interes.getColumna("TASA_EFECTIVA_NRANI").setValorDefecto(utilitario.getVariable("p_tasa_interes_efectiva"));
		tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setValorDefecto(utilitario.getVariable("p_amortizacion_cada"));
		tab_anticipo_interes.dibujar();

		PanelTabla pat_panel7=new PanelTabla();
		pat_panel7.setPanelTabla(tab_anticipo_interes);

		ItemMenu itm_tab_amort=new ItemMenu();
		itm_tab_amort.setIcon("ui-contact");
		itm_tab_amort.setMetodo("generarTablaAmortizacion");
		itm_tab_amort.setValue("Generar Tabla");

		pat_panel7.getMenuTabla().getChildren().add(itm_tab_amort);


		tab_amortizacion.setId("tab_amortizacion");
		tab_amortizacion.setIdCompleto("tab_tabulador:tab_amortizacion");
		tab_amortizacion.setTabla("NRH_AMORTIZACION", "IDE_NRAMO", 3);
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setCheck();
		tab_amortizacion.getColumna("ACTIVO_NRAMO").setValorDefecto("false");
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setCheck();
		tab_amortizacion.getColumna("PRE_CANCELADO_NRAMO").setValorDefecto("false");
//		tab_amortizacion.getColumna("IDE_NRDER").setLectura(true);
//		tab_amortizacion.getColumna("IDE_NRDER").setCombo("select IDE_NRDER,DETALLE_NRRUB from NRH_DETALLE_RUBRO dru " +
//				"left join NRH_RUBRO rub on RUB.IDE_NRRUB=DRU.IDE_NRRUB");
		tab_amortizacion.getColumna("IDE_NRRUB").setLectura(true);
		tab_amortizacion.getColumna("IDE_NRRUB").setCombo("NRH_RUBRO","IDE_NRRUB","DETALLE_NRRUB","");
		tab_amortizacion.getColumna("CAPITAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("INTERES_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("PRINCIPAL_NRAMO").setLectura(true);
		tab_amortizacion.getColumna("NRO_CUOTA_NRAMO").setLectura(true);
		tab_amortizacion.setColumnaSuma("CUOTA_NRAMO,PRINCIPAL_NRAMO");
		tab_amortizacion.setRecuperarLectura(true);
		tab_amortizacion.setCampoOrden("IDE_NRAMO DESC");
		tab_amortizacion.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_amortizacion);

		//		Division div_amor=new Division();
		//		div_amor.dividir2(pat_panel7, pat_panel3, "12%", "H");

		Grid gri_amor=new Grid();
		gri_amor.setColumns(1);
		gri_amor.setWidth("100%");
		gri_amor.getChildren().add(pat_panel7);
		gri_amor.getChildren().add(pat_panel3);

		tab_tabulador.agregarTab("TABLA DE AMORTIZACióN", gri_amor);



		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
		agregarComponente(div_division);


		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		// dialogo para precancelacion de anticipos

		set_amortizacion.setId("set_amortizacion");
//		set_amortizacion.setSeleccionTabla("select IDE_NRAMO,NRO_CUOTA_NRAMO," +
//				"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
//				"from NRH_AMORTIZACION " +
//				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT=-1) " +
//				"AND IDE_NRDER IN (SELECT IDE_NRDER FROM NRH_DETALLE_RUBRO WHERE IDE_NRRUB=48) " +
//				"and ACTIVO_NRAMO=0 " +
//				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC","IDE_NRAMO");
		set_amortizacion.setSeleccionTabla("select IDE_NRAMO,NRO_CUOTA_NRAMO, " +
				"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
				"from NRH_AMORTIZACION " +
				"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT=-1) " +
				"AND IDE_NRRUB IN (144) " +
				"and ACTIVO_NRAMO=0 " +
				"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC", "IDE_NRAMO");		
		set_amortizacion.getBot_aceptar().setMetodo("aceptarPrecancelacion");

		tab_precancelacion.setId("tab_precancelacion");
		tab_precancelacion.setTabla("NRH_PRECANCELACION", "IDE_NRPRE", 10);
		tab_precancelacion.getColumna("activo_nrpre").setCheck();
		tab_precancelacion.getColumna("activo_nrpre").setValorDefecto("true");
		tab_precancelacion.getColumna("activo_nrpre").setVisible(false);
		tab_precancelacion.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));		
		tab_precancelacion.getColumna("FECHA_PRECANCELADO_NRPRE").setValorDefecto(utilitario.getFechaActual());
		tab_precancelacion.getColumna("FECHA_DEPOSITO_NRPRE").setValorDefecto(utilitario.getFechaActual());
		tab_precancelacion.getColumna("PATH_FOTO_NRPRE").setUpload("nueva_carpeta");
		tab_precancelacion.getColumna("PATH_FOTO_NRPRE").setImagen("", "");

		tab_precancelacion.setCondicion("IDE_NRPRE=-1");
		tab_precancelacion.setTipoFormulario(false);
		tab_precancelacion.getGrid().setColumns(6);
		tab_precancelacion.dibujar();

		PanelTabla pat_precanc=new PanelTabla();
		pat_precanc.setPanelTabla(tab_precancelacion);

		set_amortizacion.getGri_cuerpo().setWidth("100%");
		set_amortizacion.getGri_cuerpo().getChildren().add(pat_precanc);

		set_amortizacion.setWidth("80%");
		set_amortizacion.setHeight("70%");
		agregarComponente(set_amortizacion);






		//UIViewRoot newView = FacesContext.getCurrentInstance().getApplication().getViewHandler().createView(FacesContext.getCurrentInstance(), "/index.jsf"); FacesContext.getCurrentInstance().setViewRoot(newView);
		//utilitario.addUpdate("@all");
	}


	public void cambiaMontoSolicitado(AjaxBehaviorEvent evt){
			tab_anticipo.modificar(evt);
			if (tab_anticipo_interes.getTotalFilas()>0){
				tab_anticipo_interes.modificar(tab_anticipo_interes.getFilaActual());
				tab_anticipo_interes.setValor("PLAZO_NRANI", tab_anticipo.getValor("NRO_MES_NRANT"));
				utilitario.addUpdateTabla(tab_anticipo_interes, "PLAZO_NRANI", "");
				calcularTablaAmortizacion();
			}

	}
	public void cambiaNumeroMes(AjaxBehaviorEvent evt){
			tab_anticipo.modificar(evt);
			if (tab_anticipo_interes.getTotalFilas()>0){
				tab_anticipo_interes.modificar(tab_anticipo_interes.getFilaActual());
				tab_anticipo_interes.setValor("PLAZO_NRANI", tab_anticipo.getValor("NRO_MES_NRANT"));
				utilitario.addUpdateTabla(tab_anticipo_interes, "PLAZO_NRANI", "");
				calcularTablaAmortizacion();
			}
	}

	public boolean validarPagosSeleccionadosPrecancelacion(){
		Fila fila=set_amortizacion.getListaSeleccionados().get(0);
		String str_fecha_venc_sel_ini=utilitario.getFormatoFecha(utilitario.getFecha(fila.getCampos()[2]+""));
		TablaGenerica tab_aux=utilitario.consultar("SELECT * FROM NRH_AMORTIZACION " +
				"where IDE_NRANI="+tab_anticipo_interes.getValorSeleccionado()+" " +
				"and ACTIVO_NRAMO = false  " +
				"order by FECHA_VENCIMIENTO_NRAMO ASC");
		int int_indice_tab_aux=0;
		for (int i = 0; i < set_amortizacion.getListaSeleccionados().size(); i++) {
			fila=set_amortizacion.getListaSeleccionados().get(i);
			String str_fecha_ven_seleccionada=utilitario.getFormatoFecha(utilitario.getFecha(fila.getCampos()[2]+""));
			String str_fecha_ven_tab_aux=tab_aux.getValor(int_indice_tab_aux, "FECHA_VENCIMIENTO_NRAMO");
			str_fecha_ven_tab_aux=utilitario.getFormatoFecha(utilitario.getFecha(str_fecha_ven_tab_aux));
			int_indice_tab_aux=int_indice_tab_aux+1;
			if (!str_fecha_ven_tab_aux.equalsIgnoreCase(str_fecha_ven_seleccionada)){
				utilitario.agregarMensajeError("No se puede precancelar", "El orden seleccionado es incorrecto no se puede saltar un pago");
				return false;
			}
		}
		if (tab_precancelacion.getValor("IDE_GEINS")==null || tab_precancelacion.getValor("IDE_GEINS").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe seleccionar una Entidad Bancaria");
			return false;
		}

		if (tab_precancelacion.getValor("FECHA_PRECANCELADO_NRPRE")==null || tab_precancelacion.getValor("FECHA_PRECANCELADO_NRPRE").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe seleccionar la fecha de pre cancelacion");
			return false;
		}

		if (tab_precancelacion.getValor("DOC_DEPOSITO_NRPRE")==null || tab_precancelacion.getValor("DOC_DEPOSITO_NRPRE").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe ingresar el Numero de Deposito");
			return false;
		}

		if (tab_precancelacion.getValor("FECHA_DEPOSITO_NRPRE")==null || tab_precancelacion.getValor("FECHA_DEPOSITO_NRPRE").isEmpty()){
			utilitario.agregarMensajeError("No se puede precancelar", "Debe seleccionar la fecha de deposito");
			return false;
		}

		return true;
	}

	public void inicializarColumnasTablaAnticipo(){
		if (ser_anticipo.isPagosRealizadosAnticipo(tab_anticipo.getValorSeleccionado())){
			tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(true);
			tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(true);
			tab_anticipo.getColumna("FECHA_SOLICITUD_NRANT").setLectura(true);
		}else {
			tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(false);
			tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(false);
			tab_anticipo.getColumna("FECHA_SOLICITUD_NRANT").setLectura(false);
		}
		utilitario.addUpdate("tab_anticipo");
	}

	public void inicializarColumnasTablaAnticipoInteres(){
		if (ser_anticipo.isPagosRealizadosAnticipo(tab_anticipo.getValorSeleccionado())){
			tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(true);
			tab_anticipo_interes.getColumna("ACTIVO_NRANI").setLectura(true);
		}else {
			tab_anticipo_interes.getColumna("TASA_INTERES_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("MES_GRACIA_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("AMORTIZACION_NRANI").setLectura(false);
			tab_anticipo_interes.getColumna("ACTIVO_NRANI").setLectura(false);
		}
	}
	
	
	public void aceptarPrecancelacion(){
		if (set_amortizacion.getListaSeleccionados().size()>0){
			if (validarPagosSeleccionadosPrecancelacion()){
				Fila fila;
				String str_cuotas=" ";
				for (int i = 0; i < set_amortizacion.getListaSeleccionados().size(); i++) {
					fila=set_amortizacion.getListaSeleccionados().get(i);
					utilitario.getConexion().agregarSql("update NRH_AMORTIZACION set ACTIVO_NRAMO=1,PRE_CANCELADO_NRAMO=1,FECHA_CANCELADO_NRAMO= TO_DATE('"+utilitario.getFechaActual()+"', 'yy-mm-dd') where IDE_NRAMO ="+fila.getRowKey());
					str_cuotas=str_cuotas+fila.getCampos()[1]+" , ";
				}
				tab_precancelacion.setValor("OBSERVACIONES_NRPRE", "pre cancelacion de las cuotas: "+(str_cuotas.substring(0, str_cuotas.length()-2)));
				utilitario.addUpdateTabla(tab_precancelacion, "OBSERVACIONES_NRPRE", "");

				tab_precancelacion.guardar();
				guardarPantalla();
				set_amortizacion.cerrar();
				utilitario.addUpdate("tab_tabulador:tab_amortizacion");

				inicializarColumnasTablaAnticipo();
				inicializarColumnasTablaAnticipoInteres();
				tab_anticipo_interes.ejecutarValorForanea(tab_anticipo.getValorSeleccionado());
				tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValorSeleccionado());
				
			}
		}else{
			utilitario.agregarMensajeInfo("No ha seleccionado ningun anticipo para precancelar", "");
		}
	}

	
	
	public void preCancelarAnticipo(){
		if (tab_amortizacion.getTotalFilas()>0){
			set_amortizacion.getTab_seleccion().setSql("select IDE_NRAMO,NRO_CUOTA_NRAMO," +
					"FECHA_VENCIMIENTO_NRAMO,PRINCIPAL_NRAMO,INTERES_NRAMO,CUOTA_NRAMO,CAPITAL_NRAMO " +
					"from NRH_AMORTIZACION " +
					"WHERE IDE_NRANI IN (SELECT IDE_NRANI FROM NRH_ANTICIPO_INTERES WHERE IDE_NRANT="+tab_anticipo.getValorSeleccionado()+") " +
					//"AND IDE_NRDER IN (SELECT IDE_NRDER FROM NRH_DETALLE_RUBRO WHERE IDE_NRRUB=48) " +
					"and ACTIVO_NRAMO=0 " +
					"ORDER BY FECHA_VENCIMIENTO_NRAMO ASC");
			set_amortizacion.getTab_seleccion().ejecutarSql();
			if (set_amortizacion.getTab_seleccion().getTotalFilas()>0){
				tab_precancelacion.limpiar();
				tab_precancelacion.insertar();
				set_amortizacion.dibujar();
			}else{
				utilitario.agregarMensajeInfo("No se puede precancelar", "No existe pagos por precancelar");
			}
		}
	}




	private boolean validarDatosAmortizacion(){
		if (tab_anticipo_interes.getValor("TASA_INTERES_NRANI")==null || tab_anticipo_interes.getValor("TASA_INTERES_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo tasa de interes es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("TASA_EFECTIVA_NRANI")==null || tab_anticipo_interes.getValor("TASA_EFECTIVA_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo tasa de Efectiva es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("PLAZO_NRANI")==null || tab_anticipo_interes.getValor("PLAZO_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo Plazo es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("AMORTIZACION_NRANI")==null || tab_anticipo_interes.getValor("AMORTIZACION_NRANI").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede continuar", "El campo Amortizacion cada es requerido");
			return false;
		}
		if (tab_anticipo_interes.getValor("MES_GRACIA_NRANI")==null || tab_anticipo_interes.getValor("MES_GRACIA_NRANI").isEmpty()){
			tab_anticipo_interes.setValor("MES_GRACIA_NRANI","0");
		}

		return true;
	}

	public void calcularTasaEfectiva(AjaxBehaviorEvent evt){
		tab_anticipo_interes.modificar(evt);
		double tasa_interes=0;
		double amortizacion_cada=0;
		double tasa_efectiva=0;
		try {
			tasa_interes=Double.parseDouble(tab_anticipo_interes.getValor("TASA_INTERES_NRANI"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			amortizacion_cada=Double.parseDouble(tab_anticipo_interes.getValor("AMORTIZACION_NRANI"));		
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (amortizacion_cada!=0){
			tasa_efectiva=(Math.pow((1+((tasa_interes/100)/(360/amortizacion_cada))), (360/amortizacion_cada))-1)*100;
		}
		tab_anticipo_interes.setValor("TASA_EFECTIVA_NRANI",utilitario.getFormatoNumero(tasa_efectiva, 4));

		utilitario.addUpdateTabla(tab_anticipo_interes,"TASA_EFECTIVA_NRANI","tab_tabulador:tex_tasa_efectiva,tab_tabulador:tex_num_periodos");


		if (aut_empleado.getValor()!=null){
			calcularTablaAmortizacion();
		}

	}


	public void generarTablaAmortizacion(){
		if (tab_anticipo.isFilaInsertada() || !ser_anticipo.isPagosRealizadosAnticipo(tab_anticipo.getValorSeleccionado())){		
			calcularTablaAmortizacion();
		}else{
			utilitario.agregarMensajeInfo("No se puede modificar", "El anticipo ya tiene pagos realizados");
		}

	}

	private void calcularTablaAmortizacion(){
		//		if (tab_anticipo.getValor("CALIFICADO_NRANT") ==null 
		//				|| tab_anticipo.getValor("CALIFICADO_NRANT").isEmpty()
		//				|| tab_anticipo.getValor("CALIFICADO_NRANT").equalsIgnoreCase("false")){
		if (validarDatosAmortizacion()){
			TablaGenerica tab_amort=ser_anticipo.getTablaAmortizacion(
					//ser_nomina.getIdeDetalleTipoNomina(aut_empleado.getValor()),
					Double.parseDouble(tab_anticipo.getValor("MONTO_SOLICITADO_NRANT")), 
					Double.parseDouble(tab_anticipo_interes.getValor("TASA_INTERES_NRANI")), 
					Integer.parseInt(tab_anticipo_interes.getValor("PLAZO_NRANI")), 
					Integer.parseInt(tab_anticipo_interes.getValor("AMORTIZACION_NRANI")), 
					Integer.parseInt(tab_anticipo_interes.getValor("MES_GRACIA_NRANI")), 
					tab_anticipo.getValor("FECHA_SOLICITUD_NRANT"),
					null);

			//						tab_capacidad.getValor("FECHA_CALCULO_NRCAP"),
			//						tab_capacidad.getValorSeleccionado());
			tab_amortizacion.setDibujo(false);
			if (!tab_amortizacion.isFilaInsertada()){
				utilitario.getConexion().ejecutarSql("delete from nrh_amortizacion where ide_nrani="+tab_anticipo_interes.getValorSeleccionado());
			}
			tab_amortizacion.limpiar();

			for (int i = 0; i < tab_amort.getTotalFilas(); i++) {
				// inserto las filas de la amortizacion
				tab_amortizacion.insertar();
				tab_amortizacion.setValor("CAPITAL_NRAMO", tab_amort.getValor(i, "CAPITAL_NRAMO"));
				tab_amortizacion.setValor("INTERES_NRAMO", tab_amort.getValor(i, "INTERES_NRAMO"));
				tab_amortizacion.setValor("CUOTA_NRAMO", tab_amort.getValor(i, "CUOTA_NRAMO"));
				tab_amortizacion.setValor("FECHA_VENCIMIENTO_NRAMO", tab_amort.getValor(i, "FECHA_VENCIMIENTO_NRAMO"));
				tab_amortizacion.setValor("PRINCIPAL_NRAMO", tab_amort.getValor(i, "PRINCIPAL_NRAMO"));
				tab_amortizacion.setValor("NRO_CUOTA_NRAMO", tab_amort.getValor(i, "NRO_CUOTA_NRAMO"));
				//tab_amortizacion.setValor("IDE_NRDER", tab_amort.getValor(i, "IDE_NRDER"));
				tab_amortizacion.setValor("IDE_NRRUB", tab_amort.getValor(i, "IDE_NRRUB"));
			}
			tab_amortizacion.setDibujo(true);
			utilitario.addUpdate("tab_tabulador:tab_amortizacion");
		}
		//		}else{
		//			utilitario.agregarMensajeInfo("No se puede generar la tabla de amortizacion", "El anticipo ya se enuentra calificado");
		//		}
	}



	/**
	 * metodo para el boton Inicio del navegador de paginas, muestra el primer registro de la tabla 
	 * 
	 */
	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
	}


	/**
	 * metodo para el boton Fin del navegador de paginas, muestra el ultimo registro de la tabla 
	 * 
	 */
	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
	}


	/**
	 * metodo para el boton Siguiente del navegador de paginas, muestra un registro posterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
	}


	/**
	 * metodo para el boton Atras del navegador de paginas, muestra un registro anterior del registro actual de la tabla 
	 * 
	 */
	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
	}



	String ide_geedp_activo="";
	public void filtrarAnticiposEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		//tab_anticipo.setCondicion("ANTICIPO_NRANT=0 AND IDE_GEEDP="+aut_empleado.getValor());
		ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());
		tab_anticipo.setCondicion("ANTICIPO_NRANT=0 AND IDE_GTEMP="+aut_empleado.getValor());
		tab_anticipo.ejecutarSql();
			inicializarColumnasTablaAnticipo();
			inicializarColumnasTablaAnticipoInteres();
			tab_anticipo_interes.ejecutarValorForanea(tab_anticipo.getValorSeleccionado());
			tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValorSeleccionado());
	}

	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_anticipo.limpiar();
		tab_anticipo_interes.limpiar();
		tab_amortizacion.limpiar();
		tab_precancelacion.limpiar();
		aut_empleado.limpiar();
		utilitario.addUpdate("aut_empleado");// limpia y refresca el autocompletar
	}


	/* (non-Javadoc)
	 * @see paq_sistema.aplicacion.Pantalla#insertar()
	 * metodo para insertar un registro en cualquier tabla de la pantalla
	 */
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_anticipo.isFocus()){
			if (aut_empleado.getValor()!=null){				
				if (ide_geedp_activo!=null && !ide_geedp_activo.isEmpty()){
					if (!ser_anticipo.isCxPActivos(aut_empleado.getValor())){						
						tab_anticipo.getColumna("MONTO_SOLICITADO_NRANT").setLectura(false);
						tab_anticipo.getColumna("NRO_MES_NRANT").setLectura(false);
						tab_anticipo.getColumna("FECHA_SOLICITUD_NRANT").setLectura(false);

						tab_anticipo.insertar();
						tab_anticipo.setValor("IDE_GEEDP",ide_geedp_activo);
						tab_anticipo.setValor("IDE_GTEMP", aut_empleado.getValor());
						tab_anticipo.setValor("NRO_ANTICIPO_NRANT", ser_anticipo.getNumeroCuentaCobrar(aut_empleado.getValor()));
						tab_anticipo_interes.limpiar();
						tab_precancelacion.limpiar();
						tab_amortizacion.limpiar();
					}else{
						utilitario.agregarMensajeInfo("No se puede realizar otro CxP", "Tiene un CxP activo");
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede solicitar un anticipo", "El empleado seleccionado no tiene un contrato activo");
				}		
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}else if (tab_anticipo_interes.isFocus()){
			if (aut_empleado.getValor()!=null){
				if (tab_anticipo_interes.getTotalFilas()==0){
					inicializarColumnasTablaAnticipoInteres();
					tab_anticipo_interes.insertar();
					tab_anticipo_interes.setValor("PLAZO_NRANI",tab_anticipo.getValor("NRO_MES_NRANT"));
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "Ya se encuentra insertada una fila");
				}

			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}
		else if (tab_amortizacion.isFocus()){
			if (aut_empleado.getValor()!=null){
				//tab_amortizacion.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado que solicita el Anticipo");
			}
		}
	}


	public boolean validarAnticipo(){
		if (tab_anticipo.getValor("MONTO_SOLICITADO_NRANT") == null || tab_anticipo.getValor("MONTO_SOLICITADO_NRANT").isEmpty()){
			utilitario.agregarMensajeInfo("Monto solicitado invalido", "No ha ingresado el monto a solicitar");
			return false;
		}
		return true;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		if (aut_empleado.getValor()!=null){
			if (validarAnticipo()){
				if (tab_anticipo.guardar()){
					if (tab_anticipo_interes.guardar()){
						if(tab_amortizacion.guardar()){
							guardarPantalla();
//							tab_anticipo.limpiar();
							inicializarColumnasTablaAnticipo();
							inicializarColumnasTablaAnticipoInteres();
							tab_anticipo_interes.ejecutarValorForanea(tab_anticipo.getValorSeleccionado());
							tab_amortizacion.ejecutarValorForanea(tab_anticipo_interes.getValorSeleccionado());
						}
					}
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede guardar el Anticipo", "Debe seleccionar un Empleado");
		}

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (utilitario.getTablaisFocus().isFilaInsertada()){
			utilitario.getTablaisFocus().eliminar();
		}
	}

	


	public Tabla getTab_anticipo() {
		return tab_anticipo;
	}

	public void setTab_anticipo(Tabla tab_anticipo) {
		this.tab_anticipo = tab_anticipo;
	}

	public Tabla getTab_amortizacion() {
		return tab_amortizacion;
	}

	public void setTab_amortizacion(Tabla tab_amortizacion) {
		this.tab_amortizacion = tab_amortizacion;
	}


	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}

	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}


	public Tabla getTab_anticipo_interes() {
		return tab_anticipo_interes;
	}

	public void setTab_anticipo_interes(Tabla tab_anticipo_interes) {
		this.tab_anticipo_interes = tab_anticipo_interes;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}


	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}


	public SeleccionTabla getSet_amortizacion() {
		return set_amortizacion;
	}

	public void setSet_amortizacion(SeleccionTabla set_amortizacion) {
		this.set_amortizacion = set_amortizacion;
	}

	public Tabla getTab_precancelacion() {
		return tab_precancelacion;
	}

	public void setTab_precancelacion(Tabla tab_precancelacion) {
		this.tab_precancelacion = tab_precancelacion;
	}


}
