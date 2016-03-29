package paq_gestion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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


public class pre_viaticos extends Pantalla {

	private AutoCompletar aut_empleado=new AutoCompletar();
	private Tabla tab_viaticos=new Tabla();
	private Tabla tab_transporte=new Tabla();
	private Tabla tab_servidores_comision=new Tabla();
	private Tabla tab_liquidacion=new Tabla();
	private Tabulador tab_tabulador=new Tabulador();
	private Confirmar con_guardar=new Confirmar();

	private Boton bot_autorizar_gasto=new Boton();
	private Boton bot_autorizar_pago=new Boton();
	private Boton bot_autorizar_liqui=new Boton();


	private Dialogo dia_autorizacion=new Dialogo();
	private AutoCompletar aut_empleado_autoriza=new AutoCompletar();
	private Etiqueta eti_empleado=new Etiqueta();


	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioGestion ser_gestion = (ServicioGestion) utilitario.instanciarEJB(ServicioGestion.class);

	public pre_viaticos() {

		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");

		// autocompletar empleado
		aut_empleado.setId("aut_empleado");
		aut_empleado.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
				"WHERE EPAR.ACTIVO_GEEDP=true");
		aut_empleado.setMetodoChange("filtrarViaticosEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);
		bar_botones.agregarBoton(bot_limpiar);

		bot_autorizar_gasto.setValue("Autorizar Gasto");
		bot_autorizar_gasto.setMetodo("autorizarGasto");
		bar_botones.agregarBoton(bot_autorizar_gasto);

		bot_autorizar_pago.setValue("Autorizar Pago");
		bot_autorizar_pago.setMetodo("autorizarPago");
		bar_botones.agregarBoton(bot_autorizar_pago);

		bot_autorizar_liqui.setValue("Autorizar Liquidacion");
		bot_autorizar_liqui.setMetodo("autorizarLiquidacion");
		bar_botones.agregarBoton(bot_autorizar_liqui);


		//  VIATICOS (division 1)

		tab_viaticos.setId("tab_viaticos");
		tab_viaticos.setTabla("GTH_VIATICOS", "IDE_GTVIA", 1);
		tab_viaticos.getColumna("IDE_GTEMP").setVisible(false);
		tab_viaticos.getColumna("IDE_GEEDP").setVisible(false);
		tab_viaticos.getColumna("GEN_IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " );
		//"WHERE EPAR.ACTIVO_GEEDP=true");
		tab_viaticos.getColumna("GEN_IDE_GEEDP").setLectura(true);
		tab_viaticos.getColumna("GEN_IDE_GEEDP").setAutoCompletar();
		tab_viaticos.getColumna("GEN_IDE_GEEDP2").setCombo(tab_viaticos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_viaticos.getColumna("GEN_IDE_GEEDP2").setLectura(true);
		tab_viaticos.getColumna("GEN_IDE_GEEDP2").setAutoCompletar();
		tab_viaticos.getColumna("GEN_IDE_GEEDP3").setCombo(tab_viaticos.getColumna("GEN_IDE_GEEDP").getListaCombo());
		tab_viaticos.getColumna("GEN_IDE_GEEDP3").setLectura(true);
		tab_viaticos.getColumna("GEN_IDE_GEEDP3").setAutoCompletar();
		List lista = new ArrayList();
		Object fila1[] = {
				"0", "INTERIOR"
		};
		Object fila2[] = {
				"1", "EXTERIOR"
		};
		lista.add(fila1);
		lista.add(fila2);

		tab_viaticos.getColumna("INTERIOR_EXTERIOR_GTVIA").setRadio(lista,"");
		tab_viaticos.getColumna("INTERIOR_EXTERIOR_GTVIA").setValorDefecto("0");
		tab_viaticos.getColumna("INTERIOR_EXTERIOR_GTVIA").setMetodoChange("cargarDivisionPolitica");
		tab_viaticos.getColumna("INTERIOR_EXTERIOR_GTVIA").setRequerida(true);
		// hasta ciudad
		tab_viaticos.getColumna("IDE_GEDIP").setCombo("select DP.IDE_GEDIP, " +
				"DP.DETALLE_GEDIP AS CIUDAD, " +
				"DP1.DETALLE_GEDIP AS PROVINCIA, " +
				"DP2.DETALLE_GEDIP AS PAIS " +
				"from GEN_DIVISION_POLITICA DP " +
				"INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP " +
				"INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP " +
				"inner join GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP " +
				"AND TDP.NIVEL_GETDP IN (3) " +
				"order by DP.DETALLE_GEDIP ASC,DP1.DETALLE_GEDIP ASC,DP2.DETALLE_GEDIP ASC");
		tab_viaticos.getColumna("IDE_GEDIP").setRequerida(true);
		tab_viaticos.getColumna("IDE_GTTCB").setCombo("GTH_TIPO_CUENTA_BANCARIA", "IDE_GTTCB", "DETALLE_GTTCB", "");
		tab_viaticos.getColumna("IDE_GTTCB").setRequerida(true);
		tab_viaticos.getColumna("IDE_GEINS").setCombo("GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS", "GEN_IDE_GEINS IS NOT NULL and IDE_GETII="+utilitario.getVariable("p_gen_tipo_institucion_financiera"));		
		tab_viaticos.getColumna("IDE_GEINS").setRequerida(true);
		tab_viaticos.getColumna("NRO_SOLICITUD_GTVIA").setLectura(true);
		tab_viaticos.getColumna("NRO_SOLICITUD_GTVIA").setRequerida(true);
		tab_viaticos.getColumna("FECHA_SOLICITUD_GTVIA").setValorDefecto(utilitario.getFechaActual());
		tab_viaticos.getColumna("FECHA_SOLICITUD_GTVIA").setRequerida(true);
		tab_viaticos.getColumna("NRO_CUENTA_GTVIA").setRequerida(true);
		tab_viaticos.getColumna("DETALLE_ACTIVIDAD_GTVIA").setRequerida(true);
		tab_viaticos.getColumna("VALOR_TOTAL_GTVIA").setRequerida(true);
		tab_viaticos.getColumna("ACTIVO_GTVIA").setCheck();
		tab_viaticos.getColumna("ACTIVO_GTVIA").setValorDefecto("true");
		tab_viaticos.agregarRelacion(tab_transporte);
		tab_viaticos.agregarRelacion(tab_servidores_comision);
		tab_viaticos.agregarRelacion(tab_liquidacion);
		tab_viaticos.setTipoFormulario(true);
		tab_viaticos.getGrid().setColumns(4);
		tab_viaticos.setValidarInsertar(true);
		tab_viaticos.setCondicion("IDE_GTVIA=-1");
		tab_viaticos.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setPanelTabla(tab_viaticos);
		pat_panel1.setMensajeWarn("VIATICOS");

		// DIVISION 2  (TABULADOR - (TRANSPORTE))

		tab_tabulador.setId("tab_tabulador");

		// TRANSPORTE (TABULADOR OPCION 1)		

		tab_transporte.setId("tab_transporte");
		tab_transporte.setIdCompleto("tab_tabulador:tab_transporte");
		tab_transporte.setTabla("GTH_TRANSPORTE_VIATICO", "IDE_GTTRV", 2);
		tab_transporte.getColumna("IDE_GTTTV").setCombo("GTH_TIPO_TRANSPORTE_VIATICO", "IDE_GTTTV", "DETALLE_GTTTV", "");
		tab_transporte.getColumna("IDE_GTTTV").setRequerida(true);
		tab_transporte.getColumna("IDE_GEINS").setCombo("select IDE_GEINS,DETALLE_GEINS from GEN_INSTITUCION GINS " +
				"LEFT JOIN GEN_TIPO_INSTITUCION GTI ON GTI.IDE_GETII=GINS.IDE_GETII " +
				"WHERE GTI.VIATICO_GETII=1 and GEN_IDE_GEINS is not NULL");
		tab_transporte.getColumna("IDE_GEINS").setRequerida(true);
		tab_transporte.getColumna("RUTA_GTTRV").setRequerida(true);
		tab_transporte.getColumna("ACTIVO_GTTRV").setCheck();
		tab_transporte.getColumna("ACTIVO_GTTRV").setValorDefecto("true");
		tab_transporte.getColumna("INSTITUCIONAL_GTTRV").setCheck();

		tab_transporte.setTipoFormulario(true);
		tab_transporte.getGrid().setColumns(4);
		tab_transporte.setCondicion("IDE_GTTRV=-1");
		tab_transporte.dibujar();

		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setPanelTabla(tab_transporte);
		pat_panel2.setMensajeWarn("TRANSPORTE VIATICOS");

		// SERVIDORES COMISION (TABULADOR OPCION 2)		

		tab_servidores_comision.setId("tab_servidores_comision");
		tab_servidores_comision.setIdCompleto("tab_tabulador:tab_servidores_comision");
		tab_servidores_comision.setTabla("GTH_SERVIDORES_COMISION", "IDE_GTSEC", 3);
		tab_servidores_comision.getColumna("ACTIVO_GTSEC").setCheck();
		tab_servidores_comision.getColumna("ACTIVO_GTSEC").setValorDefecto("true");
		tab_servidores_comision.getColumna("OBSERVACION_GTSEC").setRequerida(true);
		tab_servidores_comision.setTipoFormulario(true);
		tab_servidores_comision.getGrid().setColumns(4);
		tab_servidores_comision.setCondicion("IDE_GTSEC=-1");
		tab_servidores_comision.dibujar();

		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setPanelTabla(tab_servidores_comision);
		pat_panel3.setMensajeWarn("SERVIDORES COMISION");


		// SERVIDORES COMISION (TABULADOR OPCION 2)		

		tab_liquidacion.setId("tab_liquidacion");
		tab_liquidacion.setIdCompleto("tab_tabulador:tab_liquidacion");
		tab_liquidacion.setTabla("GTH_LIQUIDACION_VIATICO", "IDE_GTLIV", 4);
		tab_liquidacion.getColumna("ACTIVO_GTLIV").setCheck();
		tab_liquidacion.getColumna("ACTIVO_GTLIV").setValorDefecto("true");
		tab_liquidacion.getColumna("IDE_GTNZV").setCombo("SELECT IDE_GTNZV, " +
				"NIV.DETALLE_GTNIV, " +
				"ZOV.DETALLE_GTZOV, " +
				"(case when NZV.INTERIOR_EXTERIOR_GTNZV=0 then 'INTERIOR' else 'EXTERIOR' end ) AS INTERIOR, " +
				"NZV.VALOR_GTNZV " +
				"FROM GTH_NIVEL_ZONA_VIATICO NZV " +
				"LEFT JOIN GTH_NIVEL_VIATICO NIV ON NIV.IDE_GTNIV=NZV.IDE_GTNIV " +
				"LEFT JOIN GTH_ZONA_VIATICO ZOV ON ZOV.IDE_GTZOV=NZV.IDE_GTZOV"); 
		tab_liquidacion.setTipoFormulario(true);
		tab_liquidacion.getGrid().setColumns(4);
		tab_liquidacion.setCondicion("IDE_GTLIV=-1");
		tab_liquidacion.dibujar();

		PanelTabla pat_panel4=new PanelTabla();
		pat_panel4.setPanelTabla(tab_liquidacion);
		pat_panel4.setMensajeWarn("LIQUIDACION VIATICOS");

		tab_tabulador.agregarTab("TRANSPORTE VIATICOS", pat_panel2);
		tab_tabulador.agregarTab("SERVIDORES COMISION", pat_panel3);
		tab_tabulador.agregarTab("LIQUIDACION VIATICOS", pat_panel4);




		//  DIVISION DE LA PANTALLA

		Division div_division=new Division();
		div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
		agregarComponente(div_division);


		// confirmacion para guardar datos
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);

		// dialogo ingreso de autorizacion

		aut_empleado_autoriza.setId("aut_empleado_autoriza");
		aut_empleado_autoriza.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " );
		//"WHERE EPAR.ACTIVO_GEEDP=true");


		eti_empleado.setValue("Empleado Autoriza: ");

		Grid gri_memos=new Grid();
		gri_memos.setWidth("100%");
		gri_memos.setColumns(2);
		gri_memos.getChildren().add(eti_empleado);
		gri_memos.getChildren().add(aut_empleado_autoriza);

		dia_autorizacion.setId("dia_autorizacion");
		dia_autorizacion.setWidth("50%");
		dia_autorizacion.setHeight("20%");
		dia_autorizacion.setDialogo(gri_memos);
		agregarComponente(dia_autorizacion);


	}

	public void cargarDivisionPolitica(AjaxBehaviorEvent evt){
		tab_viaticos.modificar(evt);
		if (tab_viaticos.getValor("INTERIOR_EXTERIOR_GTVIA").equals("0")){
			//interior
			tab_viaticos.getColumna("IDE_GEDIP").setCombo("select DP.IDE_GEDIP, " +
					"DP.DETALLE_GEDIP AS CIUDAD, " +
					"DP1.DETALLE_GEDIP AS PROVINCIA, " +
					"DP2.DETALLE_GEDIP AS PAIS " +
					"from GEN_DIVISION_POLITICA DP " +
					"INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP " +
					"INNER JOIN GEN_DIVISION_POLITICA DP1 ON DP1.IDE_GEDIP=DP.GEN_IDE_GEDIP " +
					"inner join GEN_DIVISION_POLITICA DP2 ON DP2.IDE_GEDIP=DP1.GEN_IDE_GEDIP " +
					"AND TDP.NIVEL_GETDP IN (3) " +
					"order by DP.DETALLE_GEDIP ASC,DP1.DETALLE_GEDIP ASC,DP2.DETALLE_GEDIP ASC");
		}else{
			//exterior
			tab_viaticos.getColumna("IDE_GEDIP").setCombo("select dp.IDE_GEDIP,dp.DETALLE_GEDIP " +
					"from GEN_DIVISION_POLITICA DP " +
					"INNER JOIN GEN_TIPO_DIVISION_POLITICA TDP ON TDP.IDE_GETDP=DP.IDE_GETDP " +
					"AND TDP.NIVEL_GETDP IN (1) " +
					"order by DP.DETALLE_GEDIP ASC");
		}

		utilitario.addUpdateTabla(tab_viaticos, "IDE_GEDIP", "");
	}

	public void aceptarDatosAutorizacionLiquidacion(){
		if (aut_empleado_autoriza.getValor()!=null){
			tab_viaticos.modificar(tab_viaticos.getFilaActual());
			tab_viaticos.setValor("GEN_IDE_GEEDP3", aut_empleado_autoriza.getValor());
			utilitario.addUpdateTabla(tab_viaticos, "GEN_IDE_GEEDP3","");
			dia_autorizacion.cerrar();
		}else{
			utilitario.agregarMensajeInfo("No se puede Autorizar", "Debe seleccionar un empleado");
		}
	}

	public void autorizarLiquidacion(){
		if (aut_empleado.getValor()!=null){
			if (tab_viaticos.getTotalFilas()>0){
				if (!tab_viaticos.isFilaInsertada()){
					eti_empleado.setValue("Empleado Autoriza Liquidacion: ");
					aut_empleado_autoriza.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
							"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
							"WHERE EPAR.IDE_GEEDP!="+aut_empleado.getValor());
					aut_empleado_autoriza.setValue(null);
					dia_autorizacion.getBot_aceptar().setMetodo("aceptarDatosAutorizacionLiquidacion");
					dia_autorizacion.setTitle("DATOS AUTORIZACION LIQUIDACION ");
					dia_autorizacion.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede autorizar el gasto", "Primero de be guardar la transaccion en curso");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede autorizar el gasto", "No existen viaticos solicitados");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede insertar el Viativo", "Debe seleccionar un Empleado");
		}
	}

	public void aceptarDatosAutorizacionPago(){
		if (aut_empleado_autoriza.getValor()!=null){
			tab_viaticos.modificar(tab_viaticos.getFilaActual());
			tab_viaticos.setValor("GEN_IDE_GEEDP2", aut_empleado_autoriza.getValor());
			utilitario.addUpdateTabla(tab_viaticos, "GEN_IDE_GEEDP2","");
			dia_autorizacion.cerrar();
		}else{
			utilitario.agregarMensajeInfo("No se puede Autorizar", "Debe seleccionar un empleado");
		}
	}

	public void autorizarPago(){
		if (aut_empleado.getValor()!=null){
			if (tab_viaticos.getTotalFilas()>0){
				if (!tab_viaticos.isFilaInsertada()){
					eti_empleado.setValue("Empleado Autoriza Pago: ");
					aut_empleado_autoriza.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
							"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
							"WHERE EPAR.IDE_GEEDP!="+aut_empleado.getValor());
					aut_empleado_autoriza.setValue(null);
					dia_autorizacion.getBot_aceptar().setMetodo("aceptarDatosAutorizacionPago");
					dia_autorizacion.setTitle("DATOS AUTORIZACION PAGO ");
					dia_autorizacion.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede autorizar el gasto", "Primero de be guardar la transaccion en curso");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede autorizar el gasto", "No existen viaticos solicitados");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede insertar el Viativo", "Debe seleccionar un Empleado");
		}
	}



	public void aceptarDatosAutorizacionGasto(){
		if (aut_empleado_autoriza.getValor()!=null){
			tab_viaticos.modificar(tab_viaticos.getFilaActual());
			tab_viaticos.setValor("GEN_IDE_GEEDP", aut_empleado_autoriza.getValor());
			utilitario.addUpdateTabla(tab_viaticos, "GEN_IDE_GEEDP","");
			dia_autorizacion.cerrar();
		}else{
			utilitario.agregarMensajeInfo("No se puede Autorizar", "Debe seleccionar un empleado");
		}
	}

	public void autorizarGasto(){
		if (aut_empleado.getValor()!=null){
			if (tab_viaticos.getTotalFilas()>0){
				if (!tab_viaticos.isFilaInsertada()){
					eti_empleado.setValue("Empleado Autoriza Gasto: ");
					aut_empleado_autoriza.setAutoCompletar("SELECT EPAR.IDE_GEEDP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
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
							"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE " +
							"WHERE EPAR.IDE_GEEDP!="+aut_empleado.getValor());
					aut_empleado_autoriza.setValue(null);
					dia_autorizacion.getBot_aceptar().setMetodo("aceptarDatosAutorizacionGasto");
					dia_autorizacion.setTitle("DATOS AUTORIZACION GASTO ");
					dia_autorizacion.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede autorizar el gasto", "Primero de be guardar la transaccion en curso");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede autorizar el gasto", "No existen viaticos solicitados");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede insertar el Viativo", "Debe seleccionar un Empleado");
		}
	}

	public void filtrarViaticosEmpleado(SelectEvent evt){
		aut_empleado.onSelect(evt);
		tab_viaticos.setCondicion("IDE_GEEDP="+aut_empleado.getValor());
		tab_viaticos.ejecutarSql();
		if (tab_viaticos.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("Atencion", "El Empleado seleccionado no tiene Viaticos solicitados");
		}

		tab_transporte.setCondicion("IDE_GTVIA="+tab_viaticos.getValorSeleccionado());
		tab_transporte.ejecutarSql();
		tab_servidores_comision.setCondicion("IDE_GTVIA="+tab_viaticos.getValorSeleccionado());
		tab_servidores_comision.ejecutarSql();
		tab_liquidacion.setCondicion("IDE_GTVIA="+tab_viaticos.getValorSeleccionado());
		tab_liquidacion.ejecutarSql();



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



	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_viaticos.limpiar();
		tab_transporte.limpiar();
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
		if (aut_empleado.getValor()!=null){
			if (tab_viaticos.isFocus()){
				tab_viaticos.insertar();
				tab_viaticos.setValor("IDE_GEEDP",aut_empleado.getValor());
				String ide_gtemp=ser_nomina.getEmpleadoDepartamento(aut_empleado.getValor()).getValor("IDE_GTEMP");
				tab_viaticos.setValor("IDE_GTEMP",ide_gtemp );
				tab_viaticos.setValor("NRO_SOLICITUD_GTVIA", ser_gestion.getNumeroSolicitudViatico(ide_gtemp));
			}
			else if (tab_transporte.isFocus()){
				tab_transporte.insertar();
			}
			else if (tab_servidores_comision.isFocus()){
				tab_servidores_comision.insertar();
			}
			else if (tab_liquidacion.isFocus()){
				tab_liquidacion.insertar();
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede insertar el Viativo", "Debe seleccionar un Empleado");
		}

	}

	public boolean isHoraMayor(String hora_ini,String hora_fin){
		try {
			DateFormat dateFormat = new SimpleDateFormat ("hh:mm:ss");

			String hora1 = hora_ini;
			String hora2 = hora_fin;


			Date date1, date2, dateNueva;
			date1 = dateFormat.parse(hora1);
			date2 = dateFormat.parse(hora2);

			if (date1.compareTo(date2) > 0){ 
				return true;
			}

		} catch (ParseException parseException){
			parseException.printStackTrace();
		}
		return false;
	}

	public boolean validarSolicitudViaticos(){
		if (tab_viaticos.getTotalFilas()>0){
			if (tab_viaticos.getValor("FECHA_SALIDA_GTVIA")==null || tab_viaticos.getValor("FECHA_SALIDA_GTVIA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha de salida");
				return false;
			}
			if (tab_viaticos.getValor("FECHA_LLEGADA_GTVIA")==null || tab_viaticos.getValor("FECHA_LLEGADA_GTVIA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la fecha de llegada");
				return false;
			}
			if (tab_viaticos.getValor("HORA_SALIDA_GTVIA")==null || tab_viaticos.getValor("HORA_SALIDA_GTVIA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la hora de salida");
				return false;
			}
			if (tab_viaticos.getValor("HORA_LLEGADA_GTVIA")==null || tab_viaticos.getValor("HORA_LLEGADA_GTVIA").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe ingresar la hora de llegada");
				return false;
			}
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_viaticos.getValor("FECHA_LLEGADA_GTVIA")), utilitario.getFecha(tab_viaticos.getValor("FECHA_SALIDA_GTVIA")))){
				utilitario.agregarMensajeInfo("No se puede guardar", "La fecha de llegada no puede ser menor que la fecha de salida");
				return false;
			}

			if (tab_viaticos.getValor("FECHA_LLEGADA_GTVIA").equalsIgnoreCase(tab_viaticos.getValor("FECHA_SALIDA_GTVIA"))){
				if (!isHoraMayor(tab_viaticos.getValor("HORA_LLEGADA_GTVIA"),tab_viaticos.getValor("HORA_SALIDA_GTVIA"))){
					utilitario.agregarMensajeInfo("No se puede guardar", "La hora de llegada no puede ser menor que la hora de salida");
					return false;
				}
			}
		}

		return true;
	}

	public boolean validarTransporteViaticos(){
		if (tab_transporte.getTotalFilas()>0){
			if (tab_transporte.getValor("FECHA_SALIDA_GTTRV")==null || tab_transporte.getValor("FECHA_SALIDA_GTTRV").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar la tabla transporte", "Debe ingresar la fecha de salida");
				return false;
			}
			if (tab_transporte.getValor("FECHA_LLEGADA_GTTRV")==null || tab_transporte.getValor("FECHA_LLEGADA_GTTRV").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar la tabla transporte", "Debe ingresar la fecha de llegada");
				return false;
			}
			if (tab_transporte.getValor("HORA_SALIDA_GTTRV")==null || tab_transporte.getValor("HORA_SALIDA_GTTRV").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar la tabla transporte", "Debe ingresar la hora de salida");
				return false;
			}
			if (tab_transporte.getValor("HORA_LLEGADA_GTTRV")==null || tab_transporte.getValor("HORA_LLEGADA_GTTRV").isEmpty()){
				utilitario.agregarMensajeInfo("No se puede guardar la tabla transporte", "Debe ingresar la hora de llegada");
				return false;
			}
			if (utilitario.isFechaMenor(utilitario.getFecha(tab_transporte.getValor("FECHA_LLEGADA_GTTRV")), utilitario.getFecha(tab_transporte.getValor("FECHA_SALIDA_GTTRV")))){
				utilitario.agregarMensajeInfo("No se puede guardar la tabla transporte", "La fecha de llegada no puede ser menor que la fecha de salida");
				return false;
			}

			if (tab_transporte.getValor("FECHA_LLEGADA_GTTRV").equalsIgnoreCase(tab_transporte.getValor("FECHA_SALIDA_GTTRV"))){
				if (!isHoraMayor(tab_transporte.getValor("HORA_LLEGADA_GTTRV"),tab_transporte.getValor("HORA_SALIDA_GTTRV"))){
					utilitario.agregarMensajeInfo("No se puede guardar la tabla transporte", "La hora de llegada no puede ser menor que la hora de salida");
					return false;
				}
			}
			
			if (tab_transporte.getValor("INSTITUCIONAL_GTTRV")!=null && !tab_transporte.getValor("INSTITUCIONAL_GTTRV").isEmpty()){
				if (tab_transporte.getValor("INSTITUCIONAL_GTTRV").equals("true")){
					if (tab_transporte.getValor("PLACA_GTTRV")==null || tab_transporte.getValor("PLACA_GTTRV").isEmpty()){
						utilitario.agregarMensajeInfo("No se puede guardar la tabla transporte", "La placa de vehivulo institucional es requerida");
						return false;
					}
				}else{
					tab_transporte.setValor(tab_transporte.getFilaActual(), "PLACA_GTTRV", "");
				}
			}
		}

		return true;
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		if (aut_empleado.getValor()!=null){
			if (validarSolicitudViaticos()){
				if (tab_viaticos.guardar()){
					if (validarTransporteViaticos()){
						if(tab_transporte.guardar()){
							if (tab_servidores_comision.guardar()){
								if (tab_liquidacion.guardar()){
									guardarPantalla();
								}
							}
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

		utilitario.getTablaisFocus().eliminar();

	}



	public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}



	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}



	public Tabla getTab_viaticos() {
		return tab_viaticos;
	}



	public void setTab_viaticos(Tabla tab_viaticos) {
		this.tab_viaticos = tab_viaticos;
	}



	public Tabla getTab_transporte() {
		return tab_transporte;
	}



	public void setTab_transporte(Tabla tab_transporte) {
		this.tab_transporte = tab_transporte;
	}



	public Confirmar getCon_guardar() {
		return con_guardar;
	}



	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}



	public Dialogo getDia_autorizacion() {
		return dia_autorizacion;
	}



	public void setDia_autorizacion(Dialogo dia_autorizacion) {
		this.dia_autorizacion = dia_autorizacion;
	}



	public AutoCompletar getAut_empleado_autoriza() {
		return aut_empleado_autoriza;
	}



	public void setAut_empleado_autoriza(AutoCompletar aut_empleado_autoriza) {
		this.aut_empleado_autoriza = aut_empleado_autoriza;
	}


	public Tabla getTab_servidores_comision() {
		return tab_servidores_comision;
	}


	public void setTab_servidores_comision(Tabla tab_servidores_comision) {
		this.tab_servidores_comision = tab_servidores_comision;
	}


	public Tabla getTab_liquidacion() {
		return tab_liquidacion;
	}


	public void setTab_liquidacion(Tabla tab_liquidacion) {
		this.tab_liquidacion = tab_liquidacion;
	}

}
