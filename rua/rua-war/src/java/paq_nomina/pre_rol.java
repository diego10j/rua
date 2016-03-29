package paq_nomina;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;


import jxl.Sheet;
import jxl.Workbook;


import org.primefaces.component.blockui.BlockUI;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import sistema.aplicacion.Pantalla;
import sistema.parametros.Parametros;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Consulta;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.ItemMenu;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;
import framework.correo.EnviarCorreo;
import framework.reportes.GenerarReporte;

/**
 * 
 *HP-USER
 */
public class pre_rol extends Pantalla{

	private Combo com_periodo=new Combo();
	private Tabla tab_rol=new Tabla();
	private Tabla tab_detalle_rol=new Tabla();
	private Tabla tab_empleado_departamento=new Tabla();	
	private Texto tex_total_ingresos = new Texto();
	private Texto tex_total_egresos = new Texto();
	private Texto tex_valor_a_recibir = new Texto();
	private Confirmar con_guardar=new Confirmar();
	private SeleccionTabla set_empleado=new SeleccionTabla();



	@EJB
	private ServicioNomina ser_nomina = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);

	@EJB
	private ServicioEmpleado ser_empleado = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);

	private Dialogo dia_importar=new Dialogo();
	private AutoCompletar aut_rubros=new AutoCompletar();
	private Check che_todas_nominas=new Check();
	private Upload upl_archivo=new Upload();
	private Editor edi_mensajes=new Editor();
	private SeleccionTabla set_rubros=new SeleccionTabla();
	private List<String[]> lis_importa=null; //Guardo los empleados y el valor del rubro
	private Reporte rep_reporte=new Reporte();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private Map p_parametros=new HashMap();
	private SeleccionTabla sel_tab_periodo = new SeleccionTabla();
	private SeleccionTabla sel_tab_tipo_nomina = new SeleccionTabla();
	private SeleccionTabla sel_tab_empleados = new SeleccionTabla();
	private Etiqueta eti_num_nomina=new Etiqueta();
	private SeleccionTabla sel_tab_rubros=new SeleccionTabla();
	private SeleccionCalendario sel_cal_mes=new SeleccionCalendario();
	private SeleccionTabla sel_tab_tipo_nomina_rubros=new SeleccionTabla();
	private SeleccionTabla sel_sucursal=new SeleccionTabla();
	private SeleccionTabla set_det_tip_nomina=new SeleccionTabla();
	private SeleccionTabla set_tipo_rubro=new SeleccionTabla();


	//	private Consulta con_valida_empleado=new Consulta(); //Para mostrar los empleados q va a importar con un rubro
	private Dialogo dia_valida_empleado=new Dialogo();
	private Grid grid_tabla_emp=new Grid();

	//enviar por correo
	private Dialogo dia_resumen= new Dialogo();
	private Editor edi_msj=new Editor();

	private Dialogo dia_balance_ingresos_egresos_emp=new Dialogo();
	private Tabla tab_bal_ing_egr_emp=new Tabla();
	
	public pre_rol() {


		if (!validarParametrosSistemaImportados()){
			return;
		}



		com_periodo.setCombo(ser_nomina.getSqlComboPeriodoRol());
		com_periodo.setMetodo("cambioPeriodo");		 
		bar_botones.agregarComponente(new Etiqueta("Per�odo Rol:"));
		bar_botones.agregarComponente(com_periodo);
		bar_botones.agregarReporte();


		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);

		Boton bot_impor_valores=new Boton();
		bot_impor_valores.setValue("Importar Valores a un Rubro");
		bot_impor_valores.setIcon("ui-icon-note");
		bot_impor_valores.setMetodo("abrirDialogoImportar");
		bar_botones.agregarBoton(bot_impor_valores);


		Boton bot_calcular=new Boton();
		//bar_botones.agregarBoton(bot_calcular);
		bot_calcular.setIcon("ui-icon-calculator");
		bot_calcular.setTitle("Calcular");
		bot_calcular.setValue("Calcular");
		bot_calcular.setMetodo("calcular");
		bot_calcular.setOnstart("blo_deta_rubro.show()");
		bot_calcular.setOncomplete("blo_deta_rubro.hide()");

		Boton bot_calcular_renta=new Boton();
		bar_botones.agregarBoton(bot_calcular_renta);
		bot_calcular_renta.setIcon("ui-icon-calculator");
		bot_calcular_renta.setTitle("Calcular Renta");
		bot_calcular_renta.setValue("Calcular Renta");
		bot_calcular_renta.setMetodo("calcularRenta");
		bot_calcular_renta.setOnstart("blo_deta_rubro.show()");
		bot_calcular_renta.setOncomplete("blo_deta_rubro.hide()");


		Boton bot_cerrar_nomina=new Boton();
		bar_botones.agregarBoton(bot_cerrar_nomina);
		bot_cerrar_nomina.setIcon("ui-icon-calculator");
		bot_cerrar_nomina.setTitle("Cerrar Nomina");
		bot_cerrar_nomina.setValue("Cerrar Nomina");
		bot_cerrar_nomina.setMetodo("cerrarNomina");
		bot_cerrar_nomina.setOnstart("blo_deta_rubro.show()");
		bot_cerrar_nomina.setOncomplete("blo_deta_rubro.hide()");


		Boton bot_enviar=new Boton();
		bot_enviar.setMetodo("enviarCorreo");
		bot_enviar.setValue("Enviar E-mail");
		bot_enviar.setIcon("ui-icon-mail-closed");
		bar_botones.agregarBoton(bot_enviar);

		Boton bot_activar_nomina=new Boton();
		bot_activar_nomina.setMetodo("activarNomina");
		bot_activar_nomina.setValue("Activar Nomina");
		bot_activar_nomina.setIcon("ui-icon-gear");
		bar_botones.agregarBoton(bot_activar_nomina);

		tab_rol.setId("tab_rol");
		tab_rol.setTabla("NRH_ROL", "IDE_NRROL", 1);
		tab_rol.getColumna("IDE_USUA").setCombo("SIS_USUARIO", "IDE_USUA", "NOM_USUA","");
		tab_rol.getColumna("IDE_USUA").setValorDefecto(utilitario.getVariable("ide_usua"));
		tab_rol.getColumna("IDE_USUA").setLectura(true);
		tab_rol.getColumna("IDE_GEPRO").setVisible(false);		
		tab_rol.getColumna("IDE_NRDTN").setCombo("select DTN.IDE_NRDTN,TIN.DETALLE_NRTIN ||' '," +
				"TEM.DETALLE_GTTEM "+
				"from NRH_DETALLE_TIPO_NOMINA DTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				" WHERE ACTIVO_NRDTN=true");
		tab_rol.getColumna("IDE_NRDTN").setMetodoChange("cargarEmpleadosDepartamento");
		tab_rol.getColumna("ACTIVO_NRROL").setCheck();
		tab_rol.getColumna("ACTIVO_NRROL").setValorDefecto("true");
		tab_rol.getColumna("FECHA_NRROL").setValorDefecto(utilitario.getFechaActual());
		tab_rol.getColumna("IDE_NRESR").setCombo("NRH_ESTADO_ROL", "IDE_NRESR", "DETALLE_NRESR", "");
		tab_rol.getColumna("IDE_NRESR").setLectura(true);
		tab_rol.getColumna("IDE_NRDTN").setUnico(true);
		tab_rol.getColumna("IDE_GEPRO").setUnico(true);
		tab_rol.onSelect("seleccionaTablaRol");
		tab_rol.agregarRelacion(tab_detalle_rol);
		tab_rol.setTipoFormulario(false);
		tab_rol.setMostrarNumeroRegistros(true);
		tab_rol.setMostrarcampoSucursal(false);
		tab_rol.setRecuperarLectura(true);
		tab_rol.setCondicion("IDE_GEPRO=-1");
		tab_rol.dibujar();

		PanelTabla pat_panel1=new PanelTabla();
		pat_panel1.setMensajeWarn("CABECERA DE ROL DE PAGOS");
		pat_panel1.setPanelTabla(tab_rol);
		pat_panel1.getMenuTabla().getItem_actualizar().setMetodo("actualizarTablaRol");



		tab_empleado_departamento.setId("tab_empleado_departamento");
		String sql=ser_nomina.getSqlEmpleadosRol("-1");
		tab_empleado_departamento.setSql(sql);
		tab_empleado_departamento.getColumna("NOMBRES").setFiltro(true);
		tab_empleado_departamento.onSelect("seleccionaEmpleadoDepartamento");
		tab_empleado_departamento.getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);
		tab_empleado_departamento.setNumeroTabla(2);
		tab_empleado_departamento.setCampoPrimaria("IDE_GEEDP");
		tab_empleado_departamento.setRows(20);
		tab_empleado_departamento.setLectura(true);
		tab_empleado_departamento.dibujar();

		ItemMenu itm_eliminar_emp=new ItemMenu();
		itm_eliminar_emp.setValue("Quitar de Nomina");
		itm_eliminar_emp.setMetodo("eliminarEmpleadoNomina");

		ItemMenu itm_agregar_emp=new ItemMenu();
		itm_agregar_emp.setValue("Agregar Empleado");
		itm_agregar_emp.setMetodo("agregarEmpleadoNomina");

		ItemMenu itm_recalcular=new ItemMenu();
		itm_recalcular.setValue("Re-Calcular");
		itm_recalcular.setMetodo("moficarRubroRol");



		PanelTabla pat_panel2=new PanelTabla();
		pat_panel2.setMensajeWarn("EMPLEADOS");
		pat_panel2.setPanelTabla(tab_empleado_departamento);
		pat_panel2.getMenuTabla().getChildren().add(itm_recalcular);
		pat_panel2.getMenuTabla().getChildren().add(itm_eliminar_emp);
		pat_panel2.getMenuTabla().getChildren().add(itm_agregar_emp);


		tab_detalle_rol.setId("tab_detalle_rol");
		tab_detalle_rol.setTabla("NRH_DETALLE_ROL", "IDE_NRDRO", 3);
		tab_detalle_rol.getColumna("IDE_NRDER").setCombo("select DRU.IDE_NRDER,RUB.DETALLE_NRRUB from NRH_DETALLE_RUBRO DRU " +
				"LEFT JOIN NRH_RUBRO RUB ON RUB.IDE_NRRUB=DRU.IDE_NRRUB");
		tab_detalle_rol.getColumna("IDE_NRDER").setAutoCompletar();
		tab_detalle_rol.getColumna("IDE_NRDER").setLectura(true);
		tab_detalle_rol.getColumna("IDE_GEEDP").setVisible(false);
		tab_detalle_rol.getColumna("VALOR_NRDRO").setMetodoChange("cambiaValorRubro");
		tab_detalle_rol.getColumna("VALOR_NRDRO").getMetodoChange().setOnstart("blo_deta_rubro.show()");
		tab_detalle_rol.getColumna("VALOR_NRDRO").getMetodoChange().setOncomplete("blo_deta_rubro.hide()");
		tab_detalle_rol.getColumna("VALOR_NRDRO").setFormatoNumero(2);
		tab_detalle_rol.getColumna("ORDEN_CALCULO_NRDRO").setVisible(false);
		tab_detalle_rol.setCondicion("IDE_GEEDP="+tab_empleado_departamento.getValor("ide_geedp"));		
          System.out.printf(" ide empleado DEPARTAMENTO"+tab_empleado_departamento.getValor("ide_geedp"));
		//		tab_detalle_rol.setCondicion("IDE_GEEDP="+tab_empleado_departamento.getValor("ide_geedp")+" " +
//				"HAVING VALOR_NRDRO>0 " +
//				"GROUP by IDE_NRDRO,ide_nrrol,IDE_GEEDP,IDE_NRDER,VALOR_NRDRO,USUARIO_INGRE,FECHA_INGRE, " +
//				"HORA_INGRE,USUARIO_ACTUA,FECHA_ACTUA,HORA_ACTUA ,ORDEN_CALCULO_NRDRO ");		
		tab_detalle_rol.setCampoOrden("ORDEN_CALCULO_NRDRO,IDE_NRDER");
		tab_detalle_rol.setRecuperarLectura(false);
		tab_detalle_rol.setRows(15);
		tab_detalle_rol.dibujar();


		PanelTabla pat_panel3=new PanelTabla();
		pat_panel3.setMensajeWarn("DETALLE DE ROL DE PAGOS");
		pat_panel3.setPanelTabla(tab_detalle_rol);
		pat_panel3.getMenuTabla().getItem_actualizar().setMetodo("cargarDetallesRol");


		tex_valor_a_recibir.setId("tex_valor_a_recibir");
		tex_valor_a_recibir.setDisabled(true);
		tex_valor_a_recibir.setStyle("font-size: 14px;color: black;font-weight: bold");
		tex_total_ingresos.setId("tex_total_ingresos");
		tex_total_ingresos.setDisabled(true);
		tex_total_ingresos.setStyle("font-size: 14px;color: black;font-weight: bold");
		tex_total_egresos.setId("tex_total_egresos");
		tex_total_egresos.setDisabled(true);
		tex_total_egresos.setStyle("font-size: 14px;color: black;font-weight: bold");


		Grid gri_valores = new Grid();
		gri_valores.setColumns(4);
		gri_valores.getChildren().add(new Etiqueta("Total Ingresos"));
		gri_valores.getChildren().add(tex_total_ingresos);
		gri_valores.getChildren().add(new Etiqueta("Total Egresos"));
		gri_valores.getChildren().add(tex_total_egresos);
		gri_valores.getChildren().add(new Etiqueta("Valor a Recibir"));
		gri_valores.getChildren().add(tex_valor_a_recibir);

		Division div_panel3=new Division();
		div_panel3.dividir2(pat_panel3, gri_valores, "75%", "H");

		Division div_aux=new Division();
		div_aux.setId("div_aux"); 
		div_aux.dividir2(pat_panel2, div_panel3, "30%", "V");

		Division div=new Division();
		div.dividir2(pat_panel1,div_aux,"30%","H");

		agregarComponente(div);
		//		cargarEmpleadosDepartamento();
		//		cargarDetallesRol();


		//Dialogo para importar 
		dia_importar.setId("dia_importar");		
		dia_importar.setTitle("IMPORTAR VALORES A UN RUBRO");
		dia_importar.setPosition("left");
		dia_importar.setWidth("50%");
		dia_importar.setHeight("85%");
		dia_importar.getBot_aceptar().setRendered(false);

		Grid gri_cuerpo=new Grid();

		Grid gri_impo=new Grid();		
		gri_impo.setColumns(2);	


		gri_impo.getChildren().add(new Etiqueta("Todas las N�minas'"));

		Grid gri_tn=new Grid();
		gri_tn.setColumns(2);

		che_todas_nominas.setValue(true);
		che_todas_nominas.setMetodoChange("cambiaCheckAplicaTodasNominas");
		gri_tn.getChildren().add(che_todas_nominas);

		eti_num_nomina.setStyle("font-size:8px;");
		gri_tn.getChildren().add(eti_num_nomina);
		gri_impo.getChildren().add(gri_tn);
		gri_impo.getChildren().add(new Etiqueta("Rubro tipo Constante o Teclado: "));
		aut_rubros.setId("aut_rubros");
		aut_rubros.setAutoCompletar("select RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC from NRH_RUBRO RUB  " +
				"LEFT JOIN NRH_FORMA_CALCULO FCA ON FCA.IDE_NRFOC=RUB.IDE_NRFOC " +
				"LEFT JOIN NRH_TIPO_RUBRO TRU ON TRU.IDE_NRTIR=RUB.IDE_NRTIR " +
				"WHERE FCA.IDE_NRFOC IN ("+utilitario.getVariable("p_nrh_forma_calculo_teclado")+") "+
				"GROUP BY RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC") ;
		aut_rubros.setMetodoChange("seleccionoRubro");
		gri_impo.getChildren().add(aut_rubros);	

		gri_impo.getChildren().add(new Etiqueta("Seleccione el archivo: "));	
		upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivo");

		upl_archivo.setUpdate("gri_valida");		
		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
		upl_archivo.setUploadLabel("Validar");
		upl_archivo.setCancelLabel("Cancelar Seleccion");



		gri_impo.getChildren().add(upl_archivo);		
		gri_impo.setWidth("100%");

		Grid gri_valida = new Grid();
		gri_valida.setId("gri_valida");
		gri_valida.setColumns(3);


		Etiqueta eti_valida = new Etiqueta();
		eti_valida.setValueExpression("value", "pre_index.clase.upl_archivo.nombreReal");
		eti_valida.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
		gri_valida.getChildren().add(eti_valida);

		Imagen ima_valida = new Imagen();
		ima_valida.setWidth("22");
		ima_valida.setHeight("22");
		ima_valida.setValue("/imagenes/im_excel.gif");
		ima_valida.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
		gri_valida.getChildren().add(ima_valida);


		edi_mensajes.setControls("");
		edi_mensajes.setId("edi_mensajes");
		edi_mensajes.setStyle("overflow:auto;");		
		edi_mensajes.setWidth(dia_importar.getAnchoPanel() - 15 );
		edi_mensajes.setDisabled(true);				
		gri_valida.setFooter(edi_mensajes);	

		gri_cuerpo.setStyle("width:" + (dia_importar.getAnchoPanel() - 5) + "px;");
		gri_cuerpo.setMensajeInfo("Esta opci�n  permite subir valores a un rubro a partir de un archivo xls");
		gri_cuerpo.getChildren().add(gri_impo);
		gri_cuerpo.getChildren().add(gri_valida);
		gri_cuerpo.getChildren().add(edi_mensajes);
		gri_cuerpo.getChildren().add(new Espacio("0", "10"));


		dia_importar.setDialogo(gri_cuerpo);
		dia_importar.setDynamic(false);

		agregarComponente(dia_importar);

		//configuracion de parametros del reporte de rol de pagos

		sel_tab_periodo.setId("sel_tab_periodo");
		sel_tab_periodo.setSeleccionTabla(ser_nomina.getSqlSeleccionTablaPeriodoRol(), "IDE_GEPRO");
		sel_tab_periodo.setRadio();
		sel_tab_periodo.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
		sel_tab_periodo.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		gru_pantalla.getChildren().add(sel_tab_periodo);
		sel_tab_periodo.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_periodo);

		sel_tab_tipo_nomina.setId("sel_tab_tipo_nomina");
		sel_tab_tipo_nomina.setSeleccionTabla("select " +
				"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
				"TEM.DETALLE_GTTEM," +
				"TIC.DETALLE_GTTCO, " +
				"SUC.NOM_SUCU " +
				"from NRH_ROL ROL " +
				"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
				"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
				"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
				"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
				"where ROL.IDE_GEPRO=-1","IDE_NRROL");


		sel_tab_tipo_nomina.setRadio();
		gru_pantalla.getChildren().add(sel_tab_tipo_nomina);
		sel_tab_tipo_nomina.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_tipo_nomina);

		sel_tab_empleados.setId("sel_tab_empleados");
		String fecha_final_gepro="";

		sel_tab_empleados.setSeleccionTabla(ser_nomina.getSqlEmpleadosTipoNomina(sel_tab_tipo_nomina.getValorSeleccionado(),fecha_final_gepro), "IDE_GEEDP");

		sel_tab_empleados.getTab_seleccion().getColumna("NOMBRES").setFiltro(true);
		sel_tab_empleados.getTab_seleccion().getColumna("DOCUMENTO_IDENTIDAD_GTEMP").setFiltro(true);

		gru_pantalla.getChildren().add(sel_tab_empleados);

		sel_tab_empleados.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_empleados);

		//sel tab de rubros

		sel_tab_rubros.setId("sel_tab_rubros");
		sel_tab_rubros.setSeleccionTabla("SELECT ide_nrrub,detalle_nrrub FROM nrh_rubro order by detalle_nrrub asc ", "ide_nrrub");
		sel_tab_rubros.getTab_seleccion().getColumna("detalle_nrrub").setFiltro(true);
		sel_tab_rubros.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_rubros);

		sel_cal_mes.setId("sel_cal_mes");
		sel_cal_mes.setMultiple(true);
		sel_cal_mes.setTitle("ESCOJA EL RANGO DE FECHAS ESCOJA EL MES Y EL A�O");
		sel_cal_mes.setDynamic(false);
		agregarComponente(sel_cal_mes);

		sel_tab_tipo_nomina_rubros.setId("sel_tab_tipo_nomina_rubros");
		sel_tab_tipo_nomina_rubros.setSeleccionTabla("select a.ide_nrdtn,b.detalle_nrtin,c.detalle_gttem from NRH_DETALLE_TIPO_NOMINA a " +
				"left join NRH_TIPO_NOMINA b ON b.ide_nrtin=a.ide_nrtin " +
				"left join GTH_TIPO_EMPLEADO c ON c.ide_gttem=a.ide_gttem","ide_nrdtn");		
		sel_tab_tipo_nomina.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(sel_tab_tipo_nomina_rubros);


		//Bloque cuando calcula el rol
		BlockUI blo_deta_rubro=new BlockUI();		
		blo_deta_rubro.setBlock(div_panel3.getDivision1().getClientId());
		blo_deta_rubro.setWidgetVar("blo_deta_rubro");
		blo_deta_rubro.getChildren().add(new Etiqueta("Calculando... </br>"));
		Imagen ima_gif=new Imagen();
		ima_gif.setValue("imagenes/cargando.gif");
		blo_deta_rubro.getChildren().add(ima_gif);
		agregarComponente(blo_deta_rubro);		


		dia_valida_empleado.setId("dia_valida_empleado");
		dia_valida_empleado.getBot_aceptar().setMetodo("aceptarImportar");
		dia_valida_empleado.setModal(false);
		dia_valida_empleado.setPosition("right");
		dia_valida_empleado.setTitle("Colaboradores encontrados en el archivo");
		dia_valida_empleado.setWidth("50%");
		dia_valida_empleado.setHeight("85%");

		tab_emp.setId("tab_emp");
		tab_emp.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , 0 as VALOR_IMPORTAR FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");
		tab_emp.setCampoPrimaria("IDE_GTEMP");
		tab_emp.setNumeroTabla(15);
		tab_emp.setColumnaSuma("VALOR_IMPORTAR");
		tab_emp.getColumna("NOMBRES").setLongitud(180);
		tab_emp.getColumna("VALOR_IMPORTAR").alinearDerecha();
		tab_emp.getColumna("NOMBRES").setFiltro(true);
		tab_emp.getColumna("DOCUMENTO").setFiltro(true);

		tab_emp.setRows(15);
		tab_emp.setLectura(true);
		tab_emp.dibujar();

		grid_tabla_emp.getChildren().add(tab_emp);
		grid_tabla_emp.setStyle("width:" + (dia_valida_empleado.getAnchoPanel() - 5) + "px; height:" + dia_valida_empleado.getAltoPanel() + "px;overflow:auto;display:block;");

		//dia_valida_empleado.setModal(true);
		dia_valida_empleado.setDialogo(grid_tabla_emp);
		dia_valida_empleado.setDynamic(false);

		agregarComponente(dia_valida_empleado);

		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE ELIMINAR LA NOMINA SELECCIONADA");
		con_guardar.setTitle("CONFIRMACION ELIMINACION DE NOMINA");

		agregarComponente(con_guardar);



		dia_resumen.setId("dia_resumen");		
		dia_resumen.setTitle("RESUMEN DE ENVIO DE CORREO");
		dia_resumen.setWidth("60%");
		dia_resumen.setHeight("60%");		
		dia_resumen.setResizable(false);
		dia_resumen.setDynamic(false);		
		dia_resumen.getBot_aceptar().setRendered(false);		

		edi_msj.setWidth(490);
		edi_msj.setControls("");
		edi_msj.setDisabled(true);
		edi_msj.setStyle("overflow:auto;");
		edi_msj.setWidth(dia_resumen.getAnchoPanel() - 15 );
		edi_msj.setHeight(dia_resumen.getAltoPanel() - 10 );

		dia_resumen.setDialogo(edi_msj);		
		agregarComponente(dia_resumen);


		sel_sucursal.setId("sel_sucursal");
		sel_sucursal.setSeleccionTabla("SELECT IDE_SUCU, " +
				"NOM_SUCU " +
				"FROM SIS_SUCURSAL " +
				"ORDER BY NOM_SUCU ASC ", "IDE_SUCU");
		sel_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
		sel_sucursal.setTitle("SELECCIONE SUCURSAL");
		sel_sucursal.setRadio();
		agregarComponente(sel_sucursal);


		//1		
		set_det_tip_nomina.setId("set_det_tip_nomina");
		set_det_tip_nomina.setTitle("Seleccion de Parametros");
		set_det_tip_nomina.setSeleccionTabla("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN,e.DETALLE_nrtit " +
				"FROM NRH_DETALLE_TIPO_NOMINA a " +
				"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
				"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin " +
				"inner join NRH_TIPO_ROL e on e.ide_nrtit=a.ide_nrtit","IDE_NRDTN");
		set_det_tip_nomina.getBot_aceptar().setMetodo("aceptarReporte");
		set_det_tip_nomina.setDynamic(false);


		agregarComponente(set_det_tip_nomina);




		set_tipo_rubro.setId("set_tipo_rubro");
		set_tipo_rubro.setSeleccionTabla("select IDE_NRTIR,DETALLE_NRTIR from NRH_TIPO_RUBRO order by IDE_NRTIR", "IDE_NRTIR");
		set_tipo_rubro.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_tipo_rubro);


		set_rubros.setId("set_rubros");
		set_rubros.setSeleccionTabla("select RUB.IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO RUB " +
				"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB " +
				"WHERE DER.IDE_NRDTN IN (-1) AND IMPRIME_NRDER=TRUE " +
				"GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER "+
				"ORDER BY ORDEN_IMPRIME_NRDER ASC","IDE_NRRUB");
		set_rubros.getTab_seleccion().getColumna("DETALLE_NRRUB").setFiltro(true);
		set_rubros.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_rubros);

		dia_valida_empleado.getBot_cancelar().setMetodo("cancelarImportarEmpleados");

		
		set_empleado.setId("set_empleado");
		
		set_empleado.setSeleccionTabla("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.apellido_paterno_gtemp ||' '|| EMP.apellido_materno_gtemp ||' '|| EMP.primer_nombre_gtemp || ' ' || EMP.segundo_nombre_gtemp AS EMPLEADO " +
				"from GTH_EMPLEADO EMP  " +
				"INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
				"INNER JOIN NRH_DETALLE_ROL DRO ON DRO.IDE_GEEDP=EDP.IDE_GEEDP " +
				"inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL " +
				"INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
				"left join NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
				"WHERE   PRO.IDE_GEPRO in(-1) " +
				"and ROL.ide_nrdtn in (-1) AND EDP.ACTIVO_GEEDP=TRUE " +
				"GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
				"EMP.apellido_paterno_gtemp , EMP.apellido_materno_gtemp , EMP.primer_nombre_gtemp , EMP.segundo_nombre_gtemp  " +
				"order by EMPLEADO ASC","IDE_GTEMP");

		
		agregarComponente(set_empleado);
		
		dia_balance_ingresos_egresos_emp.setId("dia_balance_ingresos_egresos_emp");
		
		Grid gri_bie=new Grid();
		
		gri_bie.setStyle("width:" + (dia_balance_ingresos_egresos_emp.getAnchoPanel() - 5) + "px;height:" + dia_balance_ingresos_egresos_emp.getAltoPanel() + "px;overflow: auto;display: block;");
		gri_bie.setMensajeInfo("Existe un desbalance entre ingresos y egresos de los siguientes empleados, es necesario recalcular la nomina o revisar la configuracion de rubros para asientos");

		tab_bal_ing_egr_emp.setId("tab_bal_ing_egr_emp");
		tab_bal_ing_egr_emp.setSql(ser_nomina.getTablaBalanceIngresosEgresos("-1").getSql());
		tab_bal_ing_egr_emp.setCampoPrimaria("ide_contrato");
		tab_bal_ing_egr_emp.setNumeroTabla(10);
		tab_bal_ing_egr_emp.setLectura(true);
		tab_bal_ing_egr_emp.dibujar();
		
		PanelTabla pat_bie=new PanelTabla();
		pat_bie.setPanelTabla(tab_bal_ing_egr_emp);
		
		gri_bie.getChildren().add(pat_bie);
		
		dia_balance_ingresos_egresos_emp.getBot_aceptar().setRendered(false);
		dia_balance_ingresos_egresos_emp.setTitle("BALANCE INGRESOS/ EGRESOS EMPLEADOS");
		dia_balance_ingresos_egresos_emp.setDialogo(gri_bie);
		
		agregarComponente(dia_balance_ingresos_egresos_emp);
	}


	public void enviarCorreoPrueba(){
		//utilitario.EnviaEmail("alex.becerra@emgirs.gob.ec", "Escarlata1", "veronica.zambrano@emgirs.gob.ec", "prueba", "holaddd");
	}
	public void calcularRenta(){
		// valida que seleccione un periodo
		if (com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede calcular la renta","Debe seleccionar un periodo");
			return;
		}

		if (tab_rol.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No existe nomina para calcular la renta", "");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_aportes_personales")==null || utilitario.getVariable("p_nrh_rubro_aportes_personales").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual")==null || utilitario.getVariable("p_nrh_rubro_impuesto_renta_mensual").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_subtotal_iess")==null || utilitario.getVariable("p_nrh_rubro_subtotal_iess").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_remuneracion_unificada")==null || utilitario.getVariable("p_nrh_rubro_remuneracion_unificada").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado los parametros necesarios, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_factor_multiplicador_renta_discapacitados")==null || utilitario.getVariable("p_factor_multiplicador_renta_discapacitados").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado el parametro de factor multiplizador para calculo de la renta de discapcitados y tercera edad, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_rubro_seguro_social_con_rmu")==null || utilitario.getVariable("p_nrh_rubro_seguro_social_con_rmu").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado el parametro p_nrh_rubro_seguro_social_con_rmu para calculo de la renta, favor importar los parametros del sistema");
			return;
		}

		if (utilitario.getVariable("p_nrh_tipo_nomina_para_calcular_renta")==null || utilitario.getVariable("p_nrh_tipo_nomina_para_calcular_renta").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No se han importado el parametro p_nrh_tipo_nomina_para_calcular_renta para calculo de la renta, favor importar los parametros del sistema");
			return;
		}

		
		if (tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_anulada"))){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "La nomina "+tab_rol.getValorArreglo("IDE_NRDTN", 2)+" tiene estado anulado");
			return;
		}

		if (tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_cerrada"))){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "La nomina "+tab_rol.getValorArreglo("IDE_NRDTN", 2)+" se encuentra cerrada");
			return;
		}
		
		

		// valido que aun no se calcule la renta
		String estado_renta_nrrol=tab_rol.getValor(tab_rol.getFilaActual(),"ESTADO_RENTA_NRROL");
		System.out.println("estado renta "+estado_renta_nrrol);
		if (estado_renta_nrrol!=null && !estado_renta_nrrol.isEmpty() 
				&& estado_renta_nrrol.equalsIgnoreCase("1")){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "Ya se calculo la renta para la nomina "+tab_rol.getValorArreglo("IDE_NRDTN", 2)+", Recalcule la nomina, para poder calcular la renta nuevamente");
			return;
		}


		String fecha_fin_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");

		// obtenemos el ide_srimr de la tabla sri impuesto a la renta de acuerdo a la fecha de inicio de periodo
		String ide_srimr=ser_nomina.getSriImpuestoRenta(fecha_fin_gepro).getValor("IDE_SRIMR");

		if (ide_srimr==null || ide_srimr.isEmpty()){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No existe impuesto a la renta para el a�o del periodo seleccionado");
			return;
		}
		
		
		// validamos que exista configuarcion de detalles de la tabla del sri para retenciones
		if (ser_nomina.getSriDetalleImpuestoRenta(ide_srimr).getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "No existe la configuracion de la tabla de impuesto a la renta del sri para el a�o del periodo seleccionado");
			return;
		}
		String str_tip_nom_renta=utilitario.getVariable("p_nrh_tipo_nomina_para_calcular_renta");
		List lis=new ArrayList();
		String ide="";
		do {
			try {
				if (str_tip_nom_renta.indexOf(",")!=-1){
					ide=str_tip_nom_renta.substring(0, str_tip_nom_renta.indexOf(","));
					str_tip_nom_renta=str_tip_nom_renta.substring(str_tip_nom_renta.indexOf(",")+1,str_tip_nom_renta.length());
				}else{
					ide=str_tip_nom_renta;
				}
				
				lis.add(ide);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} while (str_tip_nom_renta.indexOf(",")!=-1);
		
		try {
			System.out.println("lis "+lis.size()+" "+lis.get(0));	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	
		String IDE_NRTIN=ser_nomina.getDetalleTipoNomina(tab_rol.getValor("IDE_NRDTN")).getValor("IDE_NRTIN");

		
		if (tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
			try {						
				ser_nomina.calcularRentaEmpleados(tab_rol.getValor("IDE_NRROL"));
				actualizarTablaRol();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error al calcular la renta en tipo de nomina (ide) "+tab_rol.getValorArreglo("IDE_NRDTN", 1)+" "+tab_rol.getValorArreglo("IDE_NRDTN", 2));
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede calcular la renta", "La nomina "+tab_rol.getValorArreglo("IDE_NRDTN", 1)+" "+tab_rol.getValorArreglo("IDE_NRDTN", 2)+" no tiene estado pre-nomina");
			return;
		}
	}

	public void seleccionoRubro(SelectEvent evt){
		aut_rubros.onSelect(evt);
	}

	public void cancelarImportarEmpleados(){
		dia_valida_empleado.cerrar();
		upl_archivo.limpiar();		
		utilitario.addUpdate("dia_importar");
	}

	public boolean validarParametrosSistemaImportados(){
		if (utilitario.getVariable("p_nrh_tipo_nomina_normal")==null || utilitario.getVariable("p_nrh_tipo_nomina_normal").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_tipo_nomina_normal");
			return false;
		}

		if (utilitario.getVariable("p_nrh_estado_nomina_cerrada")==null || utilitario.getVariable("p_nrh_estado_nomina_cerrada").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_estado_nomina_cerrada");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_valor_recibir")==null || utilitario.getVariable("p_nrh_rubro_valor_recibir").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_valor_recibir");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_total_ingresos")==null || utilitario.getVariable("p_nrh_rubro_total_ingresos").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_total_ingresos");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_total_egresos")==null || utilitario.getVariable("p_nrh_rubro_total_egresos").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_total_egresos");
			return false;
		}

		if (utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos")==null || utilitario.getVariable("p_nrh_tipo_nomina_pago_decimos").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_tipo_nomina_pago_decimos");
			return false;
		}

		if (utilitario.getVariable("p_nrh_tipo_nomina_liquidacion")==null || utilitario.getVariable("p_nrh_tipo_nomina_liquidacion").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_tipo_nomina_liquidacion");
			return false;
		}

		if (utilitario.getVariable("p_nrh_estado_pre_nomina")==null || utilitario.getVariable("p_nrh_estado_pre_nomina").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_estado_pre_nomina");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_region")==null || utilitario.getVariable("p_nrh_rubro_region").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_region");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar")==null || utilitario.getVariable("p_nrh_rubro_desc_valores_liquidar").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_desc_valores_liquidar");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_remuneracion_unificada")==null || utilitario.getVariable("p_nrh_rubro_remuneracion_unificada").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_remuneracion_unificada");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios")==null || utilitario.getVariable("p_nrh_rubro_remuneracion_unificada_honorarios").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_remuneracion_unificada_honorarios");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva")==null || utilitario.getVariable("p_nrh_rubro_acumula_fondos_reserva").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_acumula_fondos_reserva");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_dias_trabajados")==null || utilitario.getVariable("p_nrh_rubro_dias_trabajados").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_trabajados");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_dias_antiguedad")==null || utilitario.getVariable("p_nrh_rubro_dias_antiguedad").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_antiguedad");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva")==null || utilitario.getVariable("p_nrh_rubro_dias_fondos_reserva").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_fondos_reserva");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina")==null || utilitario.getVariable("p_nrh_rubro_dias_periodo_nomina").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_periodo_nomina");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante")==null || utilitario.getVariable("p_nrh_rubro_rmu_cargo_subrogante").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_rmu_cargo_subrogante");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_subrogados")==null || utilitario.getVariable("p_nrh_rubro_dias_subrogados").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_subrogados");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo")==null || utilitario.getVariable("p_nrh_rubro_dias_ajuste_sueldo").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_ajuste_sueldo");
			return false;
		}

		if (utilitario.getVariable("p_nrh_rubro_ajuste_sueldo")==null || utilitario.getVariable("p_nrh_rubro_ajuste_sueldo").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_ajuste_sueldo");
			return false;
		}
		if (utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion")==null || utilitario.getVariable("p_nrh_rubro_dias_pendientes_vacacion").isEmpty()){
			utilitario.agregarMensajeInfo("No se puede abrir la pagina","Debe importar el parametro de sistema p_nrh_rubro_dias_pendientes_vacacion");
			return false;
		}
		return true;
	}


	public void activarNomina(){

		if (com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede activar la nomina","Debe seleccionar un periodo");
			return;
		}

		if (tab_rol.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede activar la nomina","No existen nominas en pantalla");
			return;
		}

		if (tab_rol.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede activar la nomina","Primero debe guardar la cabecera del rol");
			return;
		}

		if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
				&& !tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){

			if (!con_guardar.isVisible()){

				con_guardar.setMessage("ESTA SEGURO DE ACTIVAR LA NOMINA SELECCIONADA");
				con_guardar.setTitle("CONFIRMACION ACTIVACION DE NOMINA");
				con_guardar.getBot_aceptar().setMetodo("activarNomina");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				con_guardar.cerrar();

				// actualiza la bandera NOMINA_ASDHE_
				utilitario.getConexion().agregarSqlPantalla("update ASI_DETALLE_HORAS_EXTRAS set NOMINA_ASDHE=false " +
						"where IDE_ASDHE in (select IDE_ASDHE from ASI_VALIDA_NOMINA where IDE_NRROL="+tab_rol.getValorSeleccionado()+")");

				
				utilitario.getConexion().agregarSqlPantalla("update NRH_AMORTIZACION set ACTIVO_NRAMO=false, " +
						"fecha_cancelado_nramo=null " +
						"where ide_nrrol in ("+tab_rol.getValorSeleccionado()+") ");

				utilitario.getConexion().agregarSqlPantalla("UPDATE NRH_ANTICIPO SET ACTIVO_NRANT=TRUE WHERE IDE_NRANT IN ( " +
						"select IDE_NRANT from NRH_ANTICIPO_INTERES where IDE_NRANI in ( " +
						"select IDE_NRANI from NRH_AMORTIZACION where IDE_NRROL="+tab_rol.getValorSeleccionado()+"))");
				
				utilitario.getConexion().agregarSqlPantalla("update NRH_ROL set IDE_NRESR="+utilitario.getVariable("p_nrh_estado_pre_nomina")+" where IDE_NRROL="+tab_rol.getValorSeleccionado());

				guardarPantalla();
				String ide_nrrol_activar=tab_rol.getValorSeleccionado();
				if(com_periodo.getValue()!=null){
					tab_rol.setCondicion("IDE_GEPRO="+com_periodo.getValue());
				}
				else{
					tab_rol.setCondicion("IDE_GEPRO=-1");	
				}
				tab_rol.ejecutarSql();	
				tab_rol.setFilaActual(ide_nrrol_activar);
				cargarEmpleadosDepartamento();
				cargarDetallesRol();

			}
		}else{
			utilitario.agregarMensajeInfo("No se puede activar la nomina", "La nomina seleccionada ya se encuentra en estado pre-nomina ");
		}
	}

	public void cerrarNomina(){

		// valida que seleccione un periodo
		if (com_periodo.getValue()==null){
			utilitario.agregarMensajeInfo("No se puede cerrar la nomina","Debe seleccionar un periodo");
			return;
		}

		// valida que la tabla roles tenga filas
		if (tab_rol.getTotalFilas()==0){
			utilitario.agregarMensajeInfo("No se puede cerrar la nomina","No existen nominas en pantalla");
			return;
		}

		// valida que la cabecera de rol este guardada
		if (tab_rol.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede cerrar la nomina","Primero debe guardar la cabecera del rol");
			return;
		}


		// valida que el estado del rol no se encuentre cerrada
		if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
				&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_cerrada"))){
			utilitario.agregarMensajeInfo("No se puede Cerrar la nomina", "La nomina seleccionada ya se encuentra cerrada");
			return;
		}

		// valida que el estado de la cabecera de rol sea pre nomina
		if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
				&& !tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
			utilitario.agregarMensajeInfo("No se puede Cerrar la nomina", "La nomina seleccionada no tiene estado de prenomina");
			return;
		}

		// valido que ya se encuentre calculado la renta
		String estado_calculado_nrrol=tab_rol.getValor(tab_rol.getFilaActual(),"ESTADO_CALCULADO_NRROL");
		if (estado_calculado_nrrol==null || estado_calculado_nrrol.isEmpty() 
				|| estado_calculado_nrrol.equalsIgnoreCase("0")
				|| estado_calculado_nrrol.equalsIgnoreCase("false")){
			utilitario.agregarMensajeInfo("No se puede Cerrar la nomina", "Aun no se re-calculan los valores importados a la nomina seleccionada");
			return;
		}

		// valido que ya se encuentre calculado la renta
		String estado_renta_nrrol=tab_rol.getValor(tab_rol.getFilaActual(),"ESTADO_RENTA_NRROL");
		System.out.println("estado renta "+estado_renta_nrrol);
		if (estado_renta_nrrol==null || estado_renta_nrrol.isEmpty() 
				|| estado_renta_nrrol.equalsIgnoreCase("0")
				|| estado_renta_nrrol.equalsIgnoreCase("false")){
			utilitario.agregarMensajeInfo("No se puede Cerrar la nomina", "Aun no se calcula la renta en la nomina seleccionada");
			return;
		}


		
		
		
		if (!ser_nomina.isTotalRecibirPermitido(tab_rol.getValorSeleccionado())){
			utilitario.agregarMensajeInfo("No se puede Cerrar la nomina", "Existen valores negativos en total a recibir");
			return;
		}
		
		TablaGenerica tab_bal_ing_egr=ser_nomina.getTablaBalanceIngresosEgresos(tab_rol.getValorSeleccionado());
		if (tab_bal_ing_egr.getTotalFilas()>0){
			
			tab_bal_ing_egr_emp.setSql(tab_bal_ing_egr.getSql());
			tab_bal_ing_egr_emp.ejecutarSql();
			dia_balance_ingresos_egresos_emp.dibujar();
			return;
//			utilitario.agregarMensajeInfo("No se puede Cerrar la nomina", "La nomina "+tab_rol.getValorArreglo("IDE_NRDTN", 2)+" requiere ser recalculada o revisada, no cuadran valores para el asiento contable ");
//			return;
		}

		if (!con_guardar.isVisible()){
			// dibuja dialogo de confirmacion de cierre de nomina
			con_guardar.setMessage("ESTA SEGURO DE CERRAR LA NOMINA SELECCIONADA, SI CIERRA LA NOMINA NO PODRA MODIFICAR NI ELIMINAR LOS DATOS");
			con_guardar.setTitle("CONFIRMACION CIERRE DE NOMINA");
			con_guardar.getBot_aceptar().setMetodo("cerrarNomina");
			con_guardar.dibujar();
			utilitario.addUpdate("con_guardar");
		}else{
			con_guardar.cerrar();

			if (ser_nomina.cerrarNomina(tab_rol.getValorSeleccionado(),tab_rol.getValor("IDE_NRDTN"), tab_rol.getValor("IDE_GEPRO"), tab_rol.getValor("FECHA_NRROL"))){
				
				
				utilitario.agregarMensaje("La nomina "+tab_rol.getValorArreglo("IDE_NRDTN",1)+" "+tab_rol.getValorArreglo("IDE_NRDTN",2), "Se cerro correctamente");

				// actualizo el estado de la nomina a cerrada
				utilitario.getConexion().ejecutarSql("update NRH_ROL set IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada")+" where IDE_NRROL="+tab_rol.getValorSeleccionado());

				String ide_nrrol_cierre=tab_rol.getValorSeleccionado();
				if(com_periodo.getValue()!=null){
					tab_rol.setCondicion("IDE_GEPRO="+com_periodo.getValue());
				}
				else{
					tab_rol.setCondicion("IDE_GEPRO=-1");	
				}
				tab_rol.ejecutarSql();
				tab_rol.setFilaActual(ide_nrrol_cierre);
				cargarEmpleadosDepartamento();
				cargarDetallesRol();
			}

		}
	}

	public void aceptarAgregarEmpleadoNomina(){
		if (sel_tab_empleados.getTab_seleccion().getListaFilasSeleccionadas().size()>0){

			String fecha_fin_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");
			
			utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
			Long ide_num_max=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
			
			TablaGenerica tab_emp;
			
			String ide_nrtin=ser_nomina.getDetalleTipoNomina(tab_rol.getValor("IDE_NRDTN")).getValor("IDE_NRTIN");
			if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
				tab_emp=utilitario.consultar(ser_nomina.getSqlEmpleadosTipoNominaLiquidacion(tab_rol.getValor("ide_nrdtn"), sel_tab_empleados.getSeleccionados(),fecha_fin_gepro));
			}else{
				tab_emp=utilitario.consultar(ser_nomina.getSqlEmpleadosTipoNomina(tab_rol.getValor("ide_nrdtn"), sel_tab_empleados.getSeleccionados(),fecha_fin_gepro));
			}
			System.out.println("tab emp "+tab_emp.getSql());

			str_ide_geedp_recalculado="";
			ser_nomina.calcularRol(tab_emp, tab_rol.getValor(tab_rol.getFilaActual(),"IDE_NRROL"), ide_num_max);
			utilitario.agregarMensaje("Se guardo correctamnete", "");
			
			String ide_nrrol_anterior=tab_rol.getValorSeleccionado();

			tab_rol.setCondicion("IDE_GEPRO="+com_periodo.getValue());
			tab_rol.ejecutarSql();

			tab_rol.setFilaActual(ide_nrrol_anterior);
			cargarEmpleadosDepartamento();
			cargarDetallesRol();
			sel_tab_empleados.cerrar();
		}else{
			utilitario.agregarMensaje("No ha seleccionado ninugun empleado", "");
		}
	}

	public void agregarEmpleadoNomina(){
		if (!tab_rol.isFilaInsertada()){
			if (tab_rol.getTotalFilas()>0){
				if (tab_rol.getValor("IDE_NRESR")!=null 
						&& !tab_rol.getValor("IDE_NRESR").isEmpty()
						&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
					
					
					
					String ide_nrtin=ser_nomina.getDetalleTipoNomina(tab_rol.getValor("IDE_NRDTN")).getValor("IDE_NRTIN");
					String fecha_ini_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_INICIAL_GEPRO");
					if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
						sel_tab_empleados.getTab_seleccion().setSql(ser_nomina.getSqlEmpleadosAgregarNominaLiquidacion(tab_rol.getValorSeleccionado(), tab_rol.getValor("ide_nrdtn"),fecha_ini_gepro));	
					}else{
						sel_tab_empleados.getTab_seleccion().setSql(ser_nomina.getSqlEmpleadosAgregarNomina(tab_rol.getValorSeleccionado(), tab_rol.getValor("ide_nrdtn")));	
					}

					
					sel_tab_empleados.getBot_aceptar().setMetodo("aceptarAgregarEmpleadoNomina");
					sel_tab_empleados.getTab_seleccion().ejecutarSql();
					if (sel_tab_empleados.getTab_seleccion().getTotalFilas()>0){
						sel_tab_empleados.dibujar();
					}else{

						
						if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
							utilitario.agregarNotificacionInfo("No se puede agregar empleados a la nomina","No existe empleados por liquidar a la fecha "+fecha_ini_gepro+" de tipo "+tab_rol.getValorArreglo("IDE_NRDTN",2));	
						}else{
							utilitario.agregarNotificacionInfo("No se puede agregar empleados a la nomina","No existe empleados por agregar");
						}
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede agregar empleados a la nomina", "No tiene estado Pre - Nomina");
				}
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede agregar un empleado a la nomina","Primero debe guardar la cabecera del Rol");
		}
	}

	public void aceptarEliminarEmpleado(){
		if (ser_nomina.eliminarEmpleadoRol(tab_rol.getValorSeleccionado(), tab_empleado_departamento.getValor(tab_empleado_departamento.getFilaActual(),"IDE_GEEDP"))){
			con_guardar.cerrar();
			utilitario.agregarMensaje("El empleado se elimino de la nomina correctamente", "");
			str_ide_geedp_recalculado="";
			cargarEmpleadosDepartamento();
			cargarDetallesRol();
		}
	}
	public void eliminarEmpleadoNomina(){
		if (tab_rol.getTotalFilas()>0){
			if (tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){

				con_guardar.setMessage("Esta seguro de eliminar al Empleado "+tab_empleado_departamento.getValor("NOMBRES")+" de la nomina seleccionada ");
				con_guardar.setTitle("CONFIRMACION ELIMINAR EMPLEADO DE NOMINA");
				con_guardar.getBot_aceptar().setMetodo("aceptarEliminarEmpleado");
				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");

			}else{
				utilitario.agregarMensajeInfo("No se puede agregar empleados a la nomina", "No tiene estado Pre - Nomina");
			}
		}
	}


	/**
	 * Cambia de periodo 
	 */
	public void cambioPeriodo(){
		str_ide_geedp_recalculado="";
		if(com_periodo.getValue()!=null){
			tab_rol.setCondicion("IDE_GEPRO="+com_periodo.getValue());
		}
		else{
			tab_rol.setCondicion("IDE_GEPRO=-1");	
		}
		tab_rol.ejecutarSql();	
		cargarEmpleadosDepartamento();
		cargarDetallesRol();
	}

	private Tabla tab_emp=new Tabla();


	private Object[] getAcumulado(String documento){		
		for(int i=0;i<tab_emp.getTotalFilas();i++){
			if(tab_emp.getValor(i,"DOCUMENTO").equalsIgnoreCase(documento)){	
				Object[] obj= new Object[3];
				obj[0]=i;
				obj[1]=tab_emp.getValor(i,"VALOR_IMPORTAR");					
				for (int k = 0; k < lis_importa.size(); k++) {						
					//busco posicion en la lista
					if(documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
						obj[2]=k;							
						break;
					}
				}

				return obj;
			}
		}
		return null;
	}

	/**
	 * Valida el archivo para que pueda importar un rubro a la nomina
	 * @param evt
	 */
	public void validarArchivo(FileUploadEvent evt){	
		if(aut_rubros.getValor()!=null){
			//Leer el archivo
			String str_msg_info="";
			String str_msg_adve="";
			String str_msg_erro="";
			double dou_tot_valor_imp=0;
			try {
				//V�lido que el rubro seleccionado este configurado en los tipo de nomina
				String tipo_nom="";
				if(che_todas_nominas.getValue().equals("true")){
					tipo_nom=tab_rol.getStringColumna("ide_nrdtn");
				}
				else{
					tipo_nom=tab_rol.getValor("ide_nrdtn");
				}

				String str[]=tipo_nom.split(",");
				for (int i = 0; i < str.length; i++) {				
					TablaGenerica tab=utilitario.consultar("select * from NRH_DETALLE_RUBRO a where a.ide_nrdtn ="+str[i]+" and IDE_NRRUB="+aut_rubros.getValor());
					if(tab.isEmpty()){
						str_msg_adve+=getFormatoAdvertencia("No existe configuraci�n del rubro "+((Object[])aut_rubros.getValue())[1]+" en el tipo de N�mina "+tab_rol.getValorArreglo(i, "ide_nrdtn", 1)+" "+tab_rol.getValorArreglo(i, "ide_nrdtn", 2));	
					}				
				}



				Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
				Sheet hoja = archivoExcel.getSheet(0);//LEE LA PRIMERA HOJA
				if (hoja == null) {
					utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
					return;
				}
				int int_fin = hoja.getRows();
				upl_archivo.setNombreReal(evt.getFile().getFileName());



				str_msg_info+=getFormatoInformacion("El archivo "+upl_archivo.getNombreReal()+" contiene "+int_fin+" filas");

				lis_importa=new ArrayList<String[]>();

				tab_emp.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , 0 as VALOR_IMPORTAR FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");
				tab_emp.ejecutarSql();

				tab_emp.setLectura(false);
				tab_emp.setDibujo(false);
				for (int i = 0; i < int_fin; i++) {
					String str_cedula = hoja.getCell(0, i).getContents();	
					str_cedula=str_cedula.trim();
					TablaGenerica tab_empleado=ser_empleado.getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP",str_cedula);

					if(tab_empleado.isEmpty()){
						//No existe el documento en la tabla de empleados
						str_msg_erro+=getFormatoError("El documento de Identidad: "+str_cedula+" no se encuentra registrado en la base de datos, fila "+(i+1));
					}
					else{
						//Valido que el documento sea correcto
						if(!ser_empleado.isDocumentoIdentidadValido(utilitario.getVariable("p_gth_tipo_documento_cedula"),str_cedula)){
							str_msg_adve+=getFormatoAdvertencia("El documento de Identidad: "+str_cedula+" no es v�lido, fila "+(i+1));
						}
						else{
							//Valido que el empleado se encuentre en  la o las nominas q va a importar
							String str_ide_gtemp=tab_empleado.getValor("IDE_GTEMP");
							TablaGenerica tabpartda= ser_empleado.getPartida(str_ide_gtemp);
							if(tabpartda.isEmpty()==false){
								//Escojo los roles seleccionados

								String ide_nrhrol=tab_rol.getStringColumna("IDE_NRROL");
								
								if(ide_nrhrol==null || ide_nrhrol.isEmpty()){
									ide_nrhrol="-1";
								}



								//															TablaGenerica tab_detarol=utilitario.consultar("SELECT a.ide_nrdro,a.ide_nrrol,a.IDE_GEEDP,a.IDE_NRDER,b.IDE_NRRUB FROM NRH_DETALLE_ROL a inner join NRH_DETALLE_RUBRO " +
								//										" b ON a.IDE_NRDER=b.IDE_NRDER " +
								//										"where IDE_NRROL in("+ide_nrhrol+") AND IDE_GEEDP="+tabpartda.getValor("IDE_GEEDP")+" and b.IDE_NRRUB="+aut_rubros.getValor());
								//										
								TablaGenerica tab_detarol=utilitario.consultar("select ide_nrdro,ide_nrder from nrh_detalle_rol where ide_nrrol IN("+ide_nrhrol+") " +
										"and ide_nrder in (select ide_nrder from nrh_detalle_rubro where ide_nrrub = "+aut_rubros.getValor()+") and ide_geedp="+tabpartda.getValor("IDE_GEEDP"));
								
								if(tab_detarol.isEmpty()){
									//No existe registro del rubro para el empleado
									str_msg_adve+=getFormatoAdvertencia("No se puede asignar valor al rubro "+((Object[])aut_rubros.getValue())[1]+" al n�mero de cedula "+str_cedula+" ya que no existe configuraci�n, fila "+(i+1));
									
									//MENSAJE Q PIDIO LA  MADRE
									if(str.length==1){
										//Si solo selecciona la nomina actual
										str_msg_adve+=getFormatoAdvertencia("El n�mero de cedula "+str_cedula+" no existe en el tipo de N�mina "+tab_rol.getValorArreglo( "ide_nrdtn", 1)+" "+tab_rol.getValorArreglo( "ide_nrdtn", 2)+", fila "+(i+1));
									}
								}
								
								
							}
							else{
								str_msg_adve+=getFormatoAdvertencia("El documento de Identidad: "+str_cedula+" no tiene partida, fila "+(i+1));
							}
						}
					}				
					String str_valor = hoja.getCell(1, i).getContents();
					str_valor=str_valor.replaceAll(",", ".");
					if(str_valor==null){
						str_valor="0.00";
					}
					double dou_valor=0;
					try {
						//Valida que sea una cantidad numerica 
						dou_valor=(Double.parseDouble(str_valor));						
					} catch (Exception e) {
						// TODO: handle exception
						str_msg_erro+=getFormatoError("Valor numerico no v�lido , fila "+(i+1));
					}
					//
					Object [] obj_cumula=getAcumulado(str_cedula);

					if(obj_cumula==null){
						tab_emp.insertar();
						tab_emp.setValor("IDE_GTEMP", tab_empleado.getValor("IDE_GTEMP"));
						tab_emp.setValor("DOCUMENTO", str_cedula);
						tab_emp.setValor("NOMBRES", new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")==null?"":tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ").
								append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP")==null?"":tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
								.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP")==null?"":tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ").
								append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")==null?"":tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());
						tab_emp.setValor("VALOR_IMPORTAR", utilitario.getFormatoNumero(dou_valor));
						lis_importa.add(new String[]{str_cedula,utilitario.getFormatoNumero(dou_valor)});
					}
					else{
						//Acumula el valor 
						try {
							int int_fila=Integer.parseInt(String.valueOf(obj_cumula[0]));
							double dou_anterior=Double.parseDouble(String.valueOf(obj_cumula[1]));							

							tab_emp.setValor(int_fila,"VALOR_IMPORTAR", utilitario.getFormatoNumero(dou_valor+dou_anterior));
							int int_indice=Integer.parseInt(String.valueOf(obj_cumula[2]));							
							lis_importa.set(int_indice,new String[]{str_cedula,utilitario.getFormatoNumero(dou_valor+dou_anterior)});
						} catch (Exception e) {
							System.out.println("ERROR "+ e.getMessage());
						}
					}

					dou_tot_valor_imp=dou_tot_valor_imp+dou_valor;



				}
				tab_emp.setLectura(true);
				tab_emp.setDibujo(true);
				archivoExcel.close();
			} catch (Exception e) {
				// TODO: handle exception
			}	
			String str_resultado="";
			if(!str_msg_info.isEmpty()){
				str_resultado="<strong><font color='#3333ff'>INFORMACION</font></strong>"+str_msg_info;
			}
			if(!str_msg_adve.isEmpty()){
				str_resultado+="<strong><font color='#ffcc33'>ADVERTENCIAS</font></strong>"+str_msg_adve;
			}
			if(!str_msg_erro.isEmpty()){
				str_resultado+="<strong><font color='#ff0000'>ERRORES</font></strong>"+str_msg_erro;
			}		

			edi_mensajes.setValue(str_resultado);


			grid_tabla_emp.getChildren().clear();

			tab_emp.getColumna("VALOR_IMPORTAR").setTotal(dou_tot_valor_imp);
			utilitario.addUpdate("edi_mensajes,eti_tot_val_imp");
			grid_tabla_emp.getChildren().add(tab_emp);

			dia_valida_empleado.dibujar();



		}
		else{
			utilitario.agregarMensajeError("Debe seleccionar un rubro", "");
		}



	}

	/**
	 * Envia el rol de pagos  de una nomina cerrada
	 */
	public void enviarCorreo(){		
		//abre el dialogo de leccion de tipo de nomina
		if(com_periodo.getValue()!=null){
			sel_tab_tipo_nomina.setHeader("Tipos de N�mina");
			sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
					"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
					"TEM.DETALLE_GTTEM," +
					"TIC.DETALLE_GTTCO, " +
					"SUC.NOM_SUCU " +
					"from NRH_ROL ROL " +
					"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
					"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
					"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
					"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
					"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
					"where ROL.IDE_GEPRO="+com_periodo.getValue() +"  AND IDE_NRESR="+utilitario.getVariable("p_nrh_estado_nomina_cerrada"));
			sel_tab_tipo_nomina.getTab_seleccion().ejecutarSql();
			if(sel_tab_tipo_nomina.getTab_seleccion().isEmpty()==false){
				sel_tab_tipo_nomina.dibujar();
				sel_tab_tipo_nomina.getBot_aceptar().setMetodo("seleccionarEnviar");
				p_parametros = new HashMap();				
				p_parametros.put("IDE_GEPRO", Integer.parseInt(com_periodo.getValue().toString()));	
			}
			else{
				utilitario.agregarMensajeInfo("No tiene n�minas cerradas en el per�odo seleccionado", "Solo se pueden enviar n�minas con estado cerradas");
			}								
		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un per�odo", "");
		}
	}



	/**
	 * Genera un mensaje de informaci�n color azul
	 * @param mensaje
	 * @return
	 */
	private String getFormatoInformacion(String mensaje){
		return "<div><font color='#3333ff'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}

	/**
	 * Genera un mensaje de Advertencia color tomate
	 * @param mensaje
	 * @return
	 */
	private String getFormatoAdvertencia(String mensaje){
		return "<div><font color='#ffcc33'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}

	/**
	 * Genera un mensaje de Error color rojo
	 * @param mensaje
	 * @return
	 */
	private String getFormatoError(String mensaje){
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>"+mensaje+"</font></div>";	
	}



	public void aceptarImportar(){

		if(upl_archivo.getNombreReal()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un archivo", "");
			return;
		}
		if(edi_mensajes.getValue()==null || edi_mensajes.getValue().toString().isEmpty()){
			utilitario.agregarMensajeInfo("Debe validar el archivo", "");
			return;
		}
		else{
			if(edi_mensajes.getValue().toString().indexOf("#ff0000")>0){
				utilitario.agregarMensajeInfo("Debe solucionar los errores del archivo", "");
				return;	
			}
		}
		if(aut_rubros.getValor()==null){
			utilitario.agregarMensajeInfo("Debe seleccionar un rubro", "");
			return;
		}

		eti_num_nomina.setValue("Si selecciona se aplicar� a las <strong>"+tab_rol.getTotalFilas()+"</strong> n�mina del periodo");


		if(che_todas_nominas.getValue().equals(true)){
			//Aplica a todas las nominas			
			for(int i=0;i<tab_rol.getTotalFilas();i++){
				TablaGenerica tab_per_rol=ser_nomina.getPeriodoRol(tab_rol.getValor(i, "IDE_GEPRO"));
				String fecha_ini_gepro=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
				String fecha_fin_gepro=tab_per_rol.getValor("FECHA_FINAL_GEPRO");
				String str_IDE_NRDER=ser_nomina.getDetalleRubro(tab_rol.getValor(i,"IDE_NRDTN"), aut_rubros.getValor()).getValor("IDE_NRDER");
			System.out.printf("detalle rubro :"+str_IDE_NRDER);
				if(str_IDE_NRDER!=null && !str_IDE_NRDER.isEmpty()){
					if (ser_nomina.getRol(tab_rol.getValor(i, "IDE_NRROL")).getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){

						if (ser_nomina.importarValoresRubro(lis_importa, tab_rol.getValor(i, "IDE_NRROL"), tab_rol.getValor(i, "IDE_NRDTN"), str_IDE_NRDER, fecha_ini_gepro, fecha_fin_gepro)){
							utilitario.agregarMensaje("Los valores se importaron correctamente", "");
						}
						
					}else{
						utilitario.agregarMensajeInfo("No se puede importar", "La nomina seleccionada no tiene estado PRE-NOMINA");
					}
				}				
			}
			dia_importar.cerrar();
			dia_valida_empleado.cerrar();
			//			tab_detalle_rol.ejecutarValorForanea(tab_rol.getValorSeleccionado());
			cargarEmpleadosDepartamento();
			cargarDetallesRol();

		}
		else{
			//Aplica solo a la nomina seleccionada
			TablaGenerica tab_per_rol=ser_nomina.getPeriodoRol(tab_rol.getValor("IDE_GEPRO"));
			String fecha_ini_gepro=tab_per_rol.getValor("FECHA_INICIAL_GEPRO");
			String fecha_fin_gepro=tab_per_rol.getValor("FECHA_FINAL_GEPRO");

			String str_IDE_NRDER=ser_nomina.getDetalleRubro(tab_rol.getValor("IDE_NRDTN"), aut_rubros.getValor()).getValor("IDE_NRDER");

			if(str_IDE_NRDER!=null && !str_IDE_NRDER.isEmpty()){
				if (ser_nomina.getRol(tab_rol.getValor("IDE_NRROL")).getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
					//solo estado pre nomina

					if (ser_nomina.importarValoresRubro(lis_importa, tab_rol.getValor("IDE_NRROL"), tab_rol.getValor("IDE_NRDTN"), str_IDE_NRDER, fecha_ini_gepro, fecha_fin_gepro)){
						utilitario.agregarMensaje("Los valores se importaron correctamente", "");
					}

					
					dia_importar.cerrar();
					dia_valida_empleado.cerrar();
					cargarEmpleadosDepartamento();
					cargarDetallesRol();
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede importar", "La nomina seleccionada no tiene estado PRE-NOMINA");
			}
		}
	}


	public void cambiaCheckAplicaTodasNominas(){
		if (che_todas_nominas.getValue()==null || che_todas_nominas.getValue().toString().isEmpty() ||che_todas_nominas.getValue().equals(false)){
			aut_rubros.setAutoCompletar("select RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC from NRH_RUBRO RUB  " +
					"LEFT JOIN NRH_FORMA_CALCULO FCA ON FCA.IDE_NRFOC=RUB.IDE_NRFOC " +
					"LEFT JOIN NRH_TIPO_RUBRO TRU ON TRU.IDE_NRTIR=RUB.IDE_NRTIR " +
					"left join NRH_DETALLE_RUBRO der on der.ide_nrrub=rub.ide_nrrub and der.activo_nrder=TRUE " +
					"left join nrh_rol rol on rol.ide_nrdtn=der.ide_nrdtn " +
					"WHERE rol.ide_gepro in ("+com_periodo.getValue()+") " +
					"and der.ide_nrdtn in ("+tab_rol.getValor("IDE_NRDTN")+") and FCA.IDE_NRFOC IN ("+utilitario.getVariable("p_nrh_forma_calculo_teclado")+") " +
					"GROUP BY RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC") ;
		}else{
			aut_rubros.setAutoCompletar("select RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC from NRH_RUBRO RUB  " +
					"LEFT JOIN NRH_FORMA_CALCULO FCA ON FCA.IDE_NRFOC=RUB.IDE_NRFOC " +
					"LEFT JOIN NRH_TIPO_RUBRO TRU ON TRU.IDE_NRTIR=RUB.IDE_NRTIR " +
					"left join NRH_DETALLE_RUBRO der on der.ide_nrrub=rub.ide_nrrub and der.activo_nrder=TRUE " +
					"left join nrh_rol rol on rol.ide_nrdtn=der.ide_nrdtn " +
					"WHERE rol.ide_gepro in ("+com_periodo.getValue()+") and FCA.IDE_NRFOC IN ("+utilitario.getVariable("p_nrh_forma_calculo_teclado")+") "+
					"GROUP BY RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC") ;

		}
		utilitario.addUpdate("aut_rubros");
	}

	public void abrirDialogoImportar(){

		if(com_periodo.getValue()!=null){

			if(tab_rol.getTotalFilas()==0){
				utilitario.agregarMensajeInfo("No existen n�minas generadas en el periodo seleccionado", "");
				return;
			}

			if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
					&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){

				aut_rubros.limpiar();
				upl_archivo.limpiar();

				aut_rubros.setAutoCompletar("select RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC from NRH_RUBRO RUB  " +
						"LEFT JOIN NRH_FORMA_CALCULO FCA ON FCA.IDE_NRFOC=RUB.IDE_NRFOC " +
						"LEFT JOIN NRH_TIPO_RUBRO TRU ON TRU.IDE_NRTIR=RUB.IDE_NRTIR " +
						"left join NRH_DETALLE_RUBRO der on der.ide_nrrub=rub.ide_nrrub and der.activo_nrder=TRUE " +
						"left join nrh_rol rol on rol.ide_nrdtn=der.ide_nrdtn " +
						"WHERE rol.ide_gepro in ("+com_periodo.getValue()+") and FCA.IDE_NRFOC IN ("+utilitario.getVariable("p_nrh_forma_calculo_teclado")+") "+
						"GROUP BY RUB.IDE_NRRUB,RUB.DETALLE_NRRUB,TRU.DETALLE_NRTIR,FCA.DETALLE_NRFOC") ;

				dia_importar.dibujar();
				edi_mensajes.setValue(null);
			}else{
				utilitario.agregarMensajeInfo("No se puede importar", "La nomina seleccionada no tiene estado de prenomina");
			}


		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Periodo ", "");
		}

	}

	public int obtener_fila_rubro_etiqueta(String IDE_NRDER) {
		int i = -1;
		if (tab_detalle_rol.getTotalFilas() > 0) {
			do {
				i = i + 1;
			} while (!tab_detalle_rol.getValor(i, "IDE_NRDER").equals(IDE_NRDER));
			return i;
		}
		return i;
	}

	public void cargarTotales() {
		//USAR PARAMETROS

		tex_total_ingresos.setValue("0");
		tex_total_egresos.setValue("0");
		tex_valor_a_recibir.setValue("0");

		try {
			if (ser_nomina.getDetalleRol(tab_rol.getValorSeleccionado(), tab_empleado_departamento.getValorSeleccionado(), utilitario.getVariable("p_nrh_rubro_valor_recibir")).getTotalFilas()>0){
				double dou_val_recibir=Double.parseDouble(ser_nomina.getDetalleRol(tab_rol.getValorSeleccionado(), tab_empleado_departamento.getValorSeleccionado(), utilitario.getVariable("p_nrh_rubro_valor_recibir")).getValor(0, "VALOR_NRDRO"));
				tex_valor_a_recibir.setValue(utilitario.getFormatoNumero(dou_val_recibir));
			}
			if (ser_nomina.getDetalleRol(tab_rol.getValorSeleccionado(), tab_empleado_departamento.getValorSeleccionado(), utilitario.getVariable("p_nrh_rubro_total_ingresos")).getTotalFilas()>0){
				double dou_tot_ingresos=Double.parseDouble(ser_nomina.getDetalleRol(tab_rol.getValorSeleccionado(), tab_empleado_departamento.getValorSeleccionado(), utilitario.getVariable("p_nrh_rubro_total_ingresos")).getValor(0, "VALOR_NRDRO"));
				tex_total_ingresos.setValue(utilitario.getFormatoNumero(dou_tot_ingresos));
			}
			if (ser_nomina.getDetalleRol(tab_rol.getValorSeleccionado(), tab_empleado_departamento.getValorSeleccionado(), utilitario.getVariable("p_nrh_rubro_total_egresos")).getTotalFilas()>0){
				double dou_tot_egresos=Double.parseDouble(ser_nomina.getDetalleRol(tab_rol.getValorSeleccionado(), tab_empleado_departamento.getValorSeleccionado(), utilitario.getVariable("p_nrh_rubro_total_egresos")).getValor(0, "VALOR_NRDRO"));
				tex_total_egresos.setValue(utilitario.getFormatoNumero(dou_tot_egresos));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		utilitario.addUpdate("tex_valor_a_recibir,tex_total_egresos,tex_total_ingresos");


	}	    

	String str_ide_geedp_recalculado="";
	public void moficarRubroRol(){

		tab_detalle_rol.guardar();
		guardarPantalla();
		TablaGenerica tab_dtn=ser_nomina.getDetalleTipoNomina(tab_rol.getValor("IDE_NRDTN"));
		String ide_nrtit=tab_dtn.getValor("IDE_NRTIT");
		String ide_nrtin=tab_dtn.getValor("IDE_NRTIN");
		if (ide_nrtin.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			utilitario.getConexion().ejecutarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set EJECUTO_LIQUIDACION_GEEDP=0 where IDE_GEEDP="+tab_empleado_departamento.getValorSeleccionado());
		}

		if (ser_nomina.getRol(tab_rol.getValor("IDE_NRDTN"), com_periodo.getValue()+"").getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
			str_ide_geedp_recalculado=tab_empleado_departamento.getValorSeleccionado();
			ser_nomina.reCalcularNomina(tab_rol.getValor("IDE_NRDTN"), com_periodo.getValue()+"",ide_nrtit,tab_empleado_departamento.getValorSeleccionado());
		}else if (ser_nomina.getRol(tab_rol.getValor("IDE_NRDTN"), com_periodo.getValue()+"").getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_nomina_cerrada"))){
			utilitario.agregarMensajeInfo("No se puede re calcular", "La nomina seleccionada ya se encuentra cerrada");
			return;
		}else {
			utilitario.agregarMensajeInfo("No se puede re calcular", "La nomina seleccionada no tiene estado pre-nomina");
			return;
		}


		String ide_nrrol_cierre=tab_rol.getValorSeleccionado();
		if(com_periodo.getValue()!=null){
			tab_rol.setCondicion("IDE_GEPRO="+com_periodo.getValue());
		}
		else{
			tab_rol.setCondicion("IDE_GEPRO=-1");	
		}
		tab_rol.ejecutarSql();
		tab_rol.setFilaActual(ide_nrrol_cierre);
		cargarEmpleadosDepartamento();
		cargarDetallesRol();

	}
	public void cambiaValorRubro(AjaxBehaviorEvent evt){
		tab_detalle_rol.modificar(evt);
		moficarRubroRol();
	}


	public void seleccionaTablaRol(AjaxBehaviorEvent evt){
		tab_rol.seleccionarFila(evt);
		str_ide_geedp_recalculado="";
		cargarEmpleadosDepartamento();
		cargarDetallesRol();

	}


	public void actualizarTablaRol(){


		String ide_nrrol=tab_rol.getValorSeleccionado();
		if(com_periodo.getValue()!=null){
			tab_rol.setCondicion("IDE_GEPRO="+com_periodo.getValue());
		}
		else{
			tab_rol.setCondicion("IDE_GEPRO=-1");	
		}
		tab_rol.ejecutarSql();
		tab_rol.setFilaActual(ide_nrrol);
		str_ide_geedp_recalculado="";
		cargarEmpleadosDepartamento();
		cargarDetallesRol();
	}

	public void seleccionaTablaRol(SelectEvent evt){
		tab_rol.seleccionarFila(evt);
		str_ide_geedp_recalculado="";
		cargarEmpleadosDepartamento();
		cargarDetallesRol();

	}

	public void seleccionaEmpleadoDepartamento(SelectEvent evt){
		tab_empleado_departamento.seleccionarFila(evt);
		str_ide_geedp_recalculado="";
		cargarDetallesRol();

	}

	public void calcular(){
		if (tab_rol.isFilaInsertada()){
			utilitario.agregarMensajeInfo("No se puede calcular la nomina","Primero debe guardar la cabecera del rol");
			return;
		}
		if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
				&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){

			if (tab_detalle_rol.getTotalFilas()>0){
				String ide_nrtit=ser_nomina.getDetalleTipoNomina(tab_rol.getValor("IDE_NRDTN")).getValor("IDE_NRTIT");
				ser_nomina.reCalcularNomina(tab_rol.getValor("IDE_NRDTN"), com_periodo.getValue()+"",ide_nrtit,false);
			}else{
				String fecha_final_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");

				utilitario.getConexion().ejecutarSql("DELETE from SIS_BLOQUEO where upper(TABLA_BLOQ) like 'NRH_DETALLE_ROL'");
				Long ide_num_max=utilitario.getConexion().getMaximo("NRH_DETALLE_ROL", "IDE_NRDRO", 1);
				TablaGenerica tab_emp=utilitario.consultar(ser_nomina.getSqlEmpleadosTipoNomina(tab_rol.getValor("ide_nrdtn"),fecha_final_gepro));
				ser_nomina.calcularRol(tab_emp, tab_rol.getValor(tab_rol.getFilaActual(),"IDE_NRROL"), ide_num_max);
				utilitario.agregarMensaje("Se guardo correctamnete", "");
			}
			cargarEmpleadosDepartamento();
			cargarDetallesRol();


		}else{
			utilitario.agregarMensajeInfo("No se puede calcular la nomina", "La nomina seleccionada no tiene estado de prenomina");
		}
	}

	public void cargarDetallesRol(){
		
		tab_detalle_rol.setCondicion("IDE_GEEDP="+tab_empleado_departamento.getValor("ide_geedp"));		

//		tab_detalle_rol.setCondicion("IDE_GEEDP="+tab_empleado_departamento.getValor("ide_geedp")+" " +
//				"HAVING VALOR_NRDRO>0 "+
//				"GROUP by IDE_NRDRO,ide_nrrol,IDE_GEEDP,IDE_NRDER,VALOR_NRDRO,USUARIO_INGRE,FECHA_INGRE, " +
//				"HORA_INGRE,USUARIO_ACTUA,FECHA_ACTUA,HORA_ACTUA ,ORDEN_CALCULO_NRDRO ");
		tab_detalle_rol.ejecutarSql();
		TablaGenerica tab_rub_teclado=ser_nomina.getRubrosTipoTeclado(tab_rol.getValor("ide_nrdtn"));

		for (int j = 0; j < tab_detalle_rol.getTotalFilas(); j++) {
			tab_detalle_rol.getFilas().get(j).setLectura(true);
		}

		if (tab_rol.getValor("IDE_NRESR")!=null 
				&& !tab_rol.getValor("IDE_NRESR").isEmpty()
				&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
			for (int i = 0; i < tab_rub_teclado.getTotalFilas(); i++) {
				for (int j = 0; j < tab_detalle_rol.getTotalFilas(); j++) {
					if (tab_rub_teclado.getValor(i,"IDE_NRDER").equalsIgnoreCase(tab_detalle_rol.getValor(j, "IDE_NRDER"))){
						tab_detalle_rol.getFilas().get(j).setLectura(false);
						break;
					}
				}
			}
		}

		utilitario.addUpdate("tab_detalle_rol");
		cargarTotales();

	}
	
	public void cargarEmpleadosDepartamento(){


		if (tab_rol.getTotalFilas()>0){
			String sql=ser_nomina.getSqlEmpleadosRol(tab_rol.getValor("ide_nrrol"));
			System.out.println("sql emp rol generado "+sql);
			tab_empleado_departamento.setSql(sql);
			tab_empleado_departamento.ejecutarSql();
			if (tab_empleado_departamento.getTotalFilas()==0){
				tab_empleado_departamento.limpiar();
				String fecha_final_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");
				sql=ser_nomina.getSqlEmpleadosTipoNomina(tab_rol.getValor("ide_nrdtn"),fecha_final_gepro);

				if (utilitario.consultar("select * from nrh_detalle_rol where ide_nrrol="+tab_rol.getValorSeleccionado()).getTotalFilas()>0){
					tab_empleado_departamento.setSql(sql);
					tab_empleado_departamento.ejecutarSql();
				}else{
					sql=ser_nomina.getSqlEmpleadosRol("-1");
					tab_empleado_departamento.setSql(sql);
					tab_empleado_departamento.ejecutarSql();
				}
			}
		}else{
			String sql=ser_nomina.getSqlEmpleadosRol("-1");
			tab_empleado_departamento.setSql(sql);
			tab_empleado_departamento.ejecutarSql();
		}

		if (str_ide_geedp_recalculado!=null && !str_ide_geedp_recalculado.isEmpty()){
			tab_empleado_departamento.setFilaActual(str_ide_geedp_recalculado);
			utilitario.addUpdate("tab_empleado_departamento");
			tab_empleado_departamento.calcularPaginaActual();
		}

	}
	public void cargarEmpleadosDepartamento(AjaxBehaviorEvent evt){
		tab_rol.modificar(evt);
		cargarEmpleadosDepartamento();
		cargarDetallesRol();
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_periodo.getValue()!=null){
			String fecha_final_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_FINAL_GEPRO");
			String fecha_inicial_gepro=ser_nomina.getPeriodoRol(com_periodo.getValue()+"").getValor("FECHA_INICIAL_GEPRO");

			if (fecha_inicial_gepro!=null && !fecha_inicial_gepro.isEmpty()){
				if (fecha_final_gepro!=null && !fecha_final_gepro.isEmpty()){
					if (tab_rol.isFocus()){
						tab_rol.getColumna("IDE_GEPRO").setValorDefecto(com_periodo.getValue().toString());
						tab_rol.insertar();
						tab_rol.setValor("IDE_NRESR", utilitario.getVariable("p_nrh_estado_pre_nomina"));

						tab_empleado_departamento.limpiar();
						cargarDetallesRol();
					}	
				}else{
					utilitario.agregarMensajeInfo("No se puede insertar", "No ha definido la fecha final del Periodo de rol ");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "No ha definido la fecha inicial del Periodo de rol ");
			}

		}
		else{
			utilitario.agregarMensajeInfo("Debe seleccionar un Periodo ", "");
		}		

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub

		if (com_periodo.getValue()!=null && !com_periodo.getValue().toString().isEmpty()){
			if (tab_rol.getValor(tab_rol.getFilaActual(),"IDE_NRDTN") != null
					&& !tab_rol.getValor(tab_rol.getFilaActual(),"IDE_NRDTN").isEmpty()){
				String IDE_NRTIN=ser_nomina.getDetalleTipoNomina(tab_rol.getValor(tab_rol.getFilaActual(),"IDE_NRDTN")).getValor("IDE_NRTIN");
				if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
					if (tab_rol.getValor("IDE_NRESR")!=null 
							&& !tab_rol.getValor("IDE_NRESR").isEmpty()
							&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
						if (tab_rol.guardar()){
							guardarPantalla();
							cargarDetallesRol();			
						}
					}
				}else{
					utilitario.agregarMensajeInfo("No se puede guardar", "Solo se admite tipo de nomina LIQUIDACION ");
				}
			}else{
				utilitario.agregarMensajeInfo("No se puede guardar", "Debe seleccionar un Tipo de Nomina ");
			}
		}


	}


	public void eliminarNominaGenerada(){

		
		String IDE_NRTIN="";
		IDE_NRTIN=ser_nomina.getDetalleTipoNomina(tab_rol.getValor("ide_nrdtn")).getValor("IDE_NRTIN");	

		if (IDE_NRTIN.equalsIgnoreCase(utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"))){
			utilitario.getConexion().agregarSql("UPDATE GEN_EMPLEADOS_DEPARTAMENTO_PAR set EJECUTO_LIQUIDACION_GEEDP=0 " +
					"where IDE_GEEDP in (select IDE_GEEDP from NRH_DETALLE_ROL where IDE_NRROL="+tab_rol.getValorSeleccionado()+")");
		}


		
		utilitario.getConexion().agregarSql("update NRH_AMORTIZACION set FECHA_CANCELADO_NRAMO=null,PRE_CANCELADO_NRAMO=false,ACTIVO_NRAMO=false where IDE_NRROL="+tab_rol.getValor("IDE_NRROL"));
		utilitario.getConexion().agregarSql("update NRH_AMORTIZACION set ide_nrrol=null where IDE_NRROL="+tab_rol.getValor("IDE_NRROL"));
		utilitario.getConexion().agregarSql("delete from NRH_DETALLE_ROL where ide_nrrol ="+tab_rol.getValor("IDE_NRROL"));
		utilitario.getConexion().agregarSql("delete from NRH_ROL where ide_nrrol ="+tab_rol.getValor("IDE_NRROL"));
		guardarPantalla();
		con_guardar.cerrar();

		tab_rol.setCondicion("IDE_GEPRO="+com_periodo.getValue());
		tab_rol.ejecutarSql();

		str_ide_geedp_recalculado="";
		cargarEmpleadosDepartamento();
		cargarDetallesRol();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		if (tab_rol.isFocus()){
			if (tab_rol.getValor("IDE_NRESR")!=null && !tab_rol.getValor("IDE_NRESR").isEmpty() 
					&& tab_rol.getValor("IDE_NRESR").equalsIgnoreCase(utilitario.getVariable("p_nrh_estado_pre_nomina"))){
				con_guardar.setMessage("ESTA SEGURO DE ELIMINAR LA NOMINA SELECCIONADA");
				con_guardar.setTitle("CONFIRMACION ELIMINACION DE NOMINA");

				con_guardar.getBot_aceptar().setMetodo("eliminarNominaGenerada");

				con_guardar.dibujar();
				utilitario.addUpdate("con_guardar");
			}else{
				utilitario.agregarMensajeInfo("No se puede eliminar la nomina", "La nomina seleccionada no tiene estado de prenomina");
			}
		}

	}

	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		sel_tab_tipo_nomina.getBot_aceptar().setMetodo("aceptarReporte");
		sel_tab_empleados.getBot_aceptar().setMetodo("aceptarReporte");
		rep_reporte.dibujar();
	}

	@Override



public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Boleta de Pago Individual")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();					
				sel_tab_periodo.dibujar();				
			}else if (sel_tab_periodo.isVisible()){		
				try {
					if (sel_tab_periodo.getValorSeleccionado()!=null && !sel_tab_periodo.getValorSeleccionado().isEmpty()){						
						p_parametros.put("IDE_GEPRO",Integer.parseInt(sel_tab_periodo.getValorSeleccionado()));				
						sel_tab_periodo.cerrar();
						sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
								"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
								"TEM.DETALLE_GTTEM," +
								"TIC.DETALLE_GTTCO, " +
								"SUC.NOM_SUCU " +
								"from NRH_ROL ROL " +
								"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
								"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
								"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
								"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
								"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
								"where ROL.IDE_GEPRO="+sel_tab_periodo.getValorSeleccionado());
						sel_tab_tipo_nomina.getTab_seleccion().ejecutarSql();
						sel_tab_tipo_nomina.dibujar();					
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
					}				
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
				}
			}else if(sel_tab_tipo_nomina.isVisible()){			
				try {
					if(sel_tab_tipo_nomina.getValorSeleccionado()!=null && !sel_tab_tipo_nomina.getValorSeleccionado().isEmpty()){						
						p_parametros.put("IDE_NRDTN",Integer.parseInt(ser_nomina.getRol(sel_tab_tipo_nomina.getValorSeleccionado()).getValor("IDE_NRDTN")));
						sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosRol(sel_tab_tipo_nomina.getValorSeleccionado()));
						sel_tab_empleados.getBot_aceptar().setMetodo("aceptarReporte");
						sel_tab_tipo_nomina.cerrar();
						sel_tab_empleados.dibujar();						
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
					}								
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
				}
			}else if(sel_tab_empleados.isVisible()){
				try {
					if (sel_tab_empleados.getSeleccionados()!=null && !sel_tab_empleados.getSeleccionados().isEmpty()) {
						p_parametros.put("IDE_GEEDP",sel_tab_empleados.getSeleccionados());
						p_parametros.put("titulo", " BOLETA DE PAGO");
						p_parametros.put("IDE_NRTIR",utilitario.getVariable("p_nrh_trubro_egreso")+","+utilitario.getVariable("p_nrh_trubro_ingreso"));
						p_parametros.put("par_total_recibir",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
						p_parametros.put("par_total_ingresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
						p_parametros.put("par_total_egresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						System.out.print("reporte parametro..."+p_parametros);
						sel_tab_empleados.cerrar();
						sef_reporte.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
					}					
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		}
		else if (rep_reporte.getReporteSelecionado().equals("Detalle Rol Periodo")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();					
				sel_tab_periodo.dibujar();				
			}else if (sel_tab_periodo.isVisible()){		
				try {
					if (sel_tab_periodo.getValorSeleccionado()!=null && !sel_tab_periodo.getValorSeleccionado().isEmpty()){						
						p_parametros.put("IDE_GEPRO",Integer.parseInt(sel_tab_periodo.getValorSeleccionado()));	
						System.out.println("parametro IDE_GEPRO...  "+Integer.parseInt(sel_tab_periodo.getValorSeleccionado()));
						
						System.out.println("periodo..."+sel_tab_periodo.getValorSeleccionado());
						sel_tab_periodo.cerrar();
						sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
								"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
								"TEM.DETALLE_GTTEM," +
								"TIC.DETALLE_GTTCO, " +
								"SUC.NOM_SUCU " +
								"from NRH_ROL ROL " +
								"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
								"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
								"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
								"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
								"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
								"where ROL.IDE_GEPRO="+sel_tab_periodo.getValorSeleccionado());
						sel_tab_tipo_nomina.getTab_seleccion().ejecutarSql();
						sel_tab_tipo_nomina.dibujar();					

						//regresa el nrtdn
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
					}				
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
				}

			}else if(sel_tab_tipo_nomina.isVisible()){			
				try {
					if(sel_tab_tipo_nomina.getValorSeleccionado()!=null && !sel_tab_tipo_nomina.getValorSeleccionado().isEmpty()){

						//						p_parametros.put("IDE_NRDTN",Long.parseLong(sel_tab_tipo_nomina.getValorSeleccionado()));						
						p_parametros.put("IDE_NRDTN",Integer.parseInt(ser_nomina.getRol(sel_tab_tipo_nomina.getValorSeleccionado()).getValor("IDE_NRDTN")));						
						System.out.println("parametro IDE_NRDTN...  "+ p_parametros);
						//						sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosTipoNomina(sel_tab_tipo_nomina.getValorSeleccionado()));
						sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosRol(sel_tab_tipo_nomina.getValorSeleccionado()));

						sel_tab_tipo_nomina.cerrar();
						sel_tab_empleados.dibujar();						
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
					}								
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
				}

			}else if(sel_tab_empleados.isVisible()){
				try {System.out.println("1empleados"+sel_tab_empleados.getSeleccionados());
				System.out.println("1periodo.."+sel_tab_periodo.getSeleccionados());
				System.out.println("1tipo..."+sel_tab_tipo_nomina.getSeleccionados());

				System.out.println("1tav.."+utilitario.getVariable("p_nrh_rubro_valor_recibir"));
				if (sel_tab_empleados.getSeleccionados()!=null && !sel_tab_empleados.getSeleccionados().isEmpty()) {
					p_parametros.put("IDE_GEEDP",Integer.parseInt(sel_tab_empleados.getSeleccionados()));

					System.out.println("IDE_GEEDP...   "+sel_tab_empleados.getSeleccionados());
					
					
					p_parametros.put("titulo", "  EMPLEADO PERIODO");
					p_parametros.put("IDE_NRRUB",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
					
					System.out.println("IDE_NRRUB...   "+utilitario.getVariable("p_nrh_rubro_valor_recibir"));
					
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					sel_tab_empleados.cerrar();
					sef_reporte.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}					
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		}
		else if (rep_reporte.getReporteSelecionado().equals("Detalle Pago Por Rubros")){



			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();					
				sel_tab_rubros.getTab_seleccion().setSql("SELECT ide_nrrub,detalle_nrrub FROM nrh_rubro order by detalle_nrrub asc ");
				sel_tab_rubros.getTab_seleccion().ejecutarSql();
				sel_tab_rubros.getBot_aceptar().setMetodo("aceptarReporte");
				sel_tab_rubros.dibujar();							
			}else if (sel_tab_rubros.isVisible()){				
				if (sel_tab_rubros.getSeleccionados()!=null && !sel_tab_rubros.getSeleccionados().isEmpty()){						
					p_parametros.put("ide_nrrub",sel_tab_rubros.getSeleccionados());
					System.out.println("valor del ide_nrrub:  "+sel_tab_rubros.getSeleccionados());
					sel_tab_rubros.cerrar();						
					sel_cal_mes.getBot_aceptar().setMetodo("aceptarReporte");	
					sel_cal_mes.setFecha1(null);
					sel_cal_mes.setFecha2(null);
					sel_cal_mes.dibujar();					
				}else{						
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Rubro");
				}		
			}else if(sel_cal_mes.isVisible()){	
				if(sel_cal_mes.getFecha1String()!=null && ! sel_cal_mes.getFecha1String().toString().isEmpty()){
					if(sel_cal_mes.getFecha2String()!=null && ! sel_cal_mes.getFecha2String().toString().isEmpty()){
						if(sel_cal_mes.isFechasValidas()){

							String str_fecha_ini=sel_cal_mes.getFecha1String();
							String str_fecha_fin=sel_cal_mes.getFecha2String();
							String ide_gepro=ser_nomina.getPeriodosRol(str_fecha_ini, str_fecha_fin);
							if (ide_gepro==null){
								ide_gepro="-1";
							}
							System.out.println("ide_gepro "+ide_gepro);
							p_parametros.put("ide_gepro", ide_gepro);
							sel_cal_mes.cerrar();
							sel_tab_tipo_nomina_rubros.setSql("select a.ide_nrdtn,b.detalle_nrtin,c.detalle_gttem from NRH_DETALLE_TIPO_NOMINA a " +
									"left join NRH_TIPO_NOMINA b ON b.ide_nrtin=a.ide_nrtin " +
									"left join GTH_TIPO_EMPLEADO c ON c.ide_gttem=a.ide_gttem");
							sel_tab_tipo_nomina_rubros.getTab_seleccion().ejecutarSql();
							sel_tab_tipo_nomina_rubros.getBot_aceptar().setMetodo("aceptarReporte");
							sel_tab_tipo_nomina_rubros.dibujar();										
						}else{
							utilitario.agregarMensajeInfo("No se puede continuar", "La fecha Final es menor a la fecha inicial");
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "No se ha selccionado la fecha Final ");
					}									
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado la fecha inicial");
				}									
			}	
			else if (sel_tab_tipo_nomina_rubros.isVisible()){				
				if (sel_tab_tipo_nomina_rubros.getSeleccionados()!=null && !sel_tab_tipo_nomina_rubros.getSeleccionados().toString().isEmpty()){						
					p_parametros.put("ide_nrdtn",sel_tab_tipo_nomina_rubros.getSeleccionados());
					p_parametros.put("titulo", " DETALLE PAGO POR RUBROS");
					System.out.println("valor del ide_nrdtn:  "+sel_tab_tipo_nomina_rubros.getSeleccionados());
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());


					sel_tab_tipo_nomina_rubros.cerrar();						
					sef_reporte.dibujar();		
				}else{						
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
				}		
			}						
		}
		else if (rep_reporte.getReporteSelecionado().equals("Detalle Rol Tipo Cuenta")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();					
				sel_tab_periodo.getTab_seleccion().setSql(ser_nomina.getSqlSeleccionTablaPeriodoRol());
				sel_tab_periodo.getTab_seleccion().ejecutarSql();
				sel_tab_periodo.dibujar();				
			}else if (sel_tab_periodo.isVisible()){		
				try {
					if (sel_tab_periodo.getValorSeleccionado()!=null && !sel_tab_periodo.getValorSeleccionado().isEmpty()){						
						p_parametros.put("IDE_GEPRO",Integer.parseInt(sel_tab_periodo.getValorSeleccionado()));				
						System.out.println("periodo..."+sel_tab_periodo.getValorSeleccionado());
						sel_tab_periodo.cerrar();
						
                        System.out.print("imprime sel_tab_periodo... "+sel_tab_periodo.getValorSeleccionado());
						
						sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
								"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
								"TEM.DETALLE_GTTEM," +
								"TIC.DETALLE_GTTCO, " +
								"SUC.NOM_SUCU " +
								"from NRH_ROL ROL " +
								"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
								"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
								"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
								"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
								"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
								"where ROL.IDE_GEPRO="+sel_tab_periodo.getValorSeleccionado()+ " and 1=1 ");
                        System.out.print("\n imprime2 sel_tab_periodo despues del sql ... "+sel_tab_periodo.getValorSeleccionado());
              		
						sel_tab_tipo_nomina.getTab_seleccion().ejecutarSql();	
						
                        System.out.print("\n imprime sql..."+sel_tab_tipo_nomina.getTab_seleccion().getSql()+"\n");
						
						sel_tab_tipo_nomina.dibujar();					
						//regresa el nrtdn
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
					}				
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
				}

			}else if(sel_tab_tipo_nomina.isVisible()){			
				try {
					if(sel_tab_tipo_nomina.getValorSeleccionado()!=null && !sel_tab_tipo_nomina.getValorSeleccionado().isEmpty()){

						//						p_parametros.put("IDE_NRDTN",Long.parseLong(sel_tab_tipo_nomina.getValorSeleccionado()));						
						p_parametros.put("IDE_NRDTN",Integer.parseInt(ser_nomina.getRol(sel_tab_tipo_nomina.getValorSeleccionado()).getValor("IDE_NRDTN")));						
						System.out.println(ser_nomina.getRol(sel_tab_tipo_nomina.getValorSeleccionado()).getValor("IDE_NRDTN"));
						//						sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosTipoNomina(sel_tab_tipo_nomina.getValorSeleccionado()));
						//sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosRol(sel_tab_tipo_nomina.getValorSeleccionado()));

						sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosRol(sel_tab_tipo_nomina.getValorSeleccionado()));
						sel_tab_empleados.getBot_aceptar().setMetodo("aceptarReporte");

						sel_tab_tipo_nomina.cerrar();						
						sel_tab_empleados.dibujar();						
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
					}								
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
				}

			}else if(sel_tab_empleados.isVisible()){
				try {System.out.println("empleados..."+sel_tab_empleados.getSeleccionados());
				//	System.out.println("periodo"+sel_tab_periodo.getSeleccionados());
				System.out.println("tipo..."+sel_tab_tipo_nomina.getSeleccionados());

				System.out.println("tav..."+utilitario.getVariable("p_nrh_rubro_valor_recibir"));
				if (sel_tab_empleados.getSeleccionados()!=null && !sel_tab_empleados.getSeleccionados().isEmpty()) {
					p_parametros.put("IDE_GEEDP",Integer.parseInt(sel_tab_empleados.getSeleccionados()));
					p_parametros.put("IDE_NRRUB",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
					p_parametros.put("titulo", " DETALLE ROL TIPO CUENTA");
  System.out.println("paso parametros "+p_parametros);
					sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
					
					sel_tab_empleados.cerrar();
					sef_reporte.dibujar();
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}					
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		}
		else if (rep_reporte.getReporteSelecionado().equals("Detalle Costo Grupo Ocupacional")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();					
				sel_cal_mes.getBot_aceptar().setMetodo("aceptarReporte");	
				sel_cal_mes.setFecha1(null);
				sel_cal_mes.setFecha2(null);
				sel_cal_mes.dibujar();					
			}		
			else if(sel_cal_mes.isVisible()){	
				if(sel_cal_mes.getFecha1String()!=null && ! sel_cal_mes.getFecha1String().toString().isEmpty()){
					if(sel_cal_mes.getFecha2String()!=null && ! sel_cal_mes.getFecha2String().toString().isEmpty()){
						if(sel_cal_mes.isFechasValidas()){
							String str_fecha_ini=sel_cal_mes.getFecha1String();
							String str_fecha_fin=sel_cal_mes.getFecha2String();
							String ide_gepro=ser_nomina.getPeriodosRol(str_fecha_ini, str_fecha_fin);
							if (ide_gepro==null){
								ide_gepro="-1";
							} 
							System.out.println("ide_gepro "+ide_gepro);
							p_parametros.put("IDE_GEPRO",ide_gepro);
							sel_cal_mes.cerrar();
							sel_sucursal.getTab_seleccion().setSql("SELECT IDE_SUCU, " +
									"NOM_SUCU " +
									"FROM SIS_SUCURSAL " +
									"ORDER BY NOM_SUCU ASC ");
							sel_sucursal.getTab_seleccion().ejecutarSql();
							sel_sucursal.dibujar();
						}else{
							utilitario.agregarMensajeInfo("No se puede continuar", "La fecha Final es menor a la fecha inicial");
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "No se ha selccionado la fecha Final ");
					}									
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado la fecha inicial");
				}				
			}
			else if(sel_sucursal.isVisible()){
				if(sel_sucursal.getValorSeleccionado()!=null && !sel_sucursal.getValorSeleccionado().isEmpty())
				{System.out.println("sucursal... "+sel_sucursal.getValorSeleccionado());
				p_parametros.put("SUCURSAL",Integer.parseInt(sel_sucursal.getValorSeleccionado()));
				p_parametros.put("titulo", "Detalle Costo Grupo Ocupacional y Tipo Empleado");
				//	p_parametros.put("IDE_NRRUB",utilitario.getVariable("p_nrh_rubro_valor_recibir"));
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				sel_sucursal.cerrar();
				sef_reporte.dibujar();

				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado una Sucursal");
				}	

			}


		}

		else if (rep_reporte.getReporteSelecionado().equals("Detalle Nomina Global")){
			String IDE_GEPRO="";
			String IDE_NRDTN="";
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();					
				sel_cal_mes.getBot_aceptar().setMetodo("aceptarReporte");	
				sel_cal_mes.setFecha1(null);
				sel_cal_mes.setFecha2(null);
				sel_cal_mes.dibujar();					
			}		
			else if(sel_cal_mes.isVisible()){	
			
				if(sel_cal_mes.getFecha1String()!=null && ! sel_cal_mes.getFecha1String().toString().isEmpty()){
					if(sel_cal_mes.getFecha2String()!=null && ! sel_cal_mes.getFecha2String().toString().isEmpty()){
						if(sel_cal_mes.isFechasValidas()){
							String str_fecha_ini=sel_cal_mes.getFecha1String();
							String str_fecha_fin=sel_cal_mes.getFecha2String();
							String ide_gepro="";				
							ide_gepro=ser_nomina.getPeriodosRol(str_fecha_ini, str_fecha_fin);
					
							if (ide_gepro==null){
								ide_gepro="-1";
							}
							
							System.out.println("IDE_GEPRO "+IDE_GEPRO);

							if (ide_gepro==null || ide_gepro.isEmpty()){
								utilitario.agregarMensajeInfo("No existe creado un periodo para las fechas seleccionadas", "Crear periodo");
								return;
							}
							p_parametros.put("IDE_GEPRO", ide_gepro);
							
							sel_cal_mes.cerrar();
							set_det_tip_nomina.getTab_seleccion().setSql("SELECT a.IDE_NRDTN,b.DETALLE_GTTEM,d.DETALLE_NRTIN,e.DETALLE_nrtit " +
									"FROM NRH_DETALLE_TIPO_NOMINA a " +
									"INNER join GTH_TIPO_EMPLEADO b on b.IDE_GTTEM=a.IDE_GTTEM " +
									"inner join NRH_TIPO_NOMINA d on d.ide_nrtin=a.ide_nrtin " +
									"inner join NRH_TIPO_ROL e on e.ide_nrtit=a.ide_nrtit");
							set_det_tip_nomina.getTab_seleccion().ejecutarSql();
							set_det_tip_nomina.dibujar();
						}else{
							utilitario.agregarMensajeInfo("No se puede continuar", "La fecha Final es menor a la fecha inicial");
						}
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "No se ha selccionado la fecha Final ");
					}									
				}else{
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado la fecha inicial");
				}				
			}else if(set_det_tip_nomina.isVisible()){
				p_parametros.put("IDE_NRDTN",set_det_tip_nomina.getSeleccionados());

String str_fecha_ini=sel_cal_mes.getFecha1String();
String str_fecha_fin=sel_cal_mes.getFecha2String();
System.out.println("str fecha ini "+str_fecha_ini);
System.out.println("str fecha fin "+str_fecha_fin);
String ide_gepro=ser_nomina.getPeriodosRol(str_fecha_ini, str_fecha_fin);

					
				System.out.println("nrtdn "+set_det_tip_nomina.getSeleccionados());
				set_det_tip_nomina.cerrar();
				set_empleado.getTab_seleccion().setSql("select EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.apellido_paterno_gtemp ||' '|| EMP.apellido_materno_gtemp ||' '|| EMP.primer_nombre_gtemp || ' ' || EMP.segundo_nombre_gtemp AS EMPLEADO " +
						"from GTH_EMPLEADO EMP  " +
						"INNER JOIN GEN_EMPLEADOS_DEPARTAMENTO_PAR EDP ON EDP.IDE_GTEMP=EMP.IDE_GTEMP " +
						"INNER JOIN NRH_DETALLE_ROL DRO ON DRO.IDE_GEEDP=EDP.IDE_GEEDP " +
						"inner join NRH_ROL rol on ROL.IDE_NRROL=DRO.IDE_NRROL " +
						"INNER JOIN GEN_PERIDO_ROL PRO ON PRO.IDE_GEPRO=ROL.IDE_GEPRO " +
						"left join NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
						"WHERE   PRO.IDE_GEPRO in("+ide_gepro+") " +
						"and ROL.ide_nrdtn in ("+set_det_tip_nomina.getSeleccionados()+") AND EDP.ACTIVO_GEEDP=TRUE " +
						"GROUP BY EMP.IDE_GTEMP,EMP.DOCUMENTO_IDENTIDAD_GTEMP, " +
						"EMP.apellido_paterno_gtemp , EMP.apellido_materno_gtemp , EMP.primer_nombre_gtemp , EMP.segundo_nombre_gtemp  " +
						"order by EMPLEADO ASC");
				set_empleado.getTab_seleccion().ejecutarSql();
				
				
				System.out.println("st em "+set_empleado.getTab_seleccion().getSql());
				set_empleado.getBot_aceptar().setMetodo("aceptarReporte");
				set_empleado.dibujar();
				utilitario.addUpdate("set_empleado");

			}	
			else if(set_empleado.isVisible()){
				System.out.println("ide_gepro "+IDE_GEPRO);
				System.out.println("nrtdn"+set_det_tip_nomina.getSeleccionados());
				System.out.println("empleados1"+set_empleado.getSeleccionados());
				//try {
					if (set_empleado.getSeleccionados()!=null && !set_empleado.getSeleccionados().isEmpty()) {
						p_parametros.put("IDE_GTEMP",set_empleado.getSeleccionados());
						set_empleado.cerrar();
						set_tipo_rubro.getTab_seleccion().setSql("select IDE_NRTIR,DETALLE_NRTIR from NRH_TIPO_RUBRO order by IDE_NRTIR");
						set_tipo_rubro.getTab_seleccion().ejecutarSql();
						set_tipo_rubro.dibujar();

											
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
					}					
			//	} catch (Exception e) {
				//	utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				//}
			
			}

			
			else if(set_tipo_rubro.isVisible()){
				p_parametros.put("IDE_NRTIR",set_tipo_rubro.getSeleccionados());
				System.out.println("nrtir"+set_tipo_rubro.getSeleccionados());

				set_tipo_rubro.cerrar();
				set_rubros.getTab_seleccion().setSql("select RUB.IDE_NRRUB,DETALLE_NRRUB from NRH_RUBRO RUB " +
						"INNER JOIN NRH_DETALLE_RUBRO DER ON DER.IDE_NRRUB=RUB.IDE_NRRUB " +
						"WHERE DER.IDE_NRDTN IN ("+set_det_tip_nomina.getSeleccionados()+") AND IMPRIME_NRDER=TRUE " +
						"GROUP BY RUB.IDE_NRRUB,DETALLE_NRRUB,ORDEN_IMPRIME_NRDER "+
						"ORDER BY ORDEN_IMPRIME_NRDER ASC");
				set_rubros.getTab_seleccion().ejecutarSql();
				set_rubros.dibujar();


			}
			else if(set_rubros.isVisible()){
				try {
				if(set_rubros.getSeleccionados()!=null && !set_rubros.getSeleccionados().isEmpty())
				{
					System.out.println("RUBROS"+set_rubros.getSeleccionados());
				p_parametros.put("IDE_NRRUB",set_rubros.getSeleccionados());
				p_parametros.put("titulo", "DETALLE NOMINA GLOBAL POR PERIODO");
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
				set_rubros.cerrar();
				sef_reporte.dibujar();
				
			}else{						
				utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
			}								
		} catch (Exception e) {
			utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
		}
		
	}
		}

		

		else if (rep_reporte.getReporteSelecionado().equals("Liquidacion de Haberes")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();					
				sel_tab_periodo.dibujar();				
			}else if (sel_tab_periodo.isVisible()){		
				try {
					if (sel_tab_periodo.getValorSeleccionado()!=null && !sel_tab_periodo.getValorSeleccionado().isEmpty()){						
										
						sel_tab_periodo.cerrar();
						sel_tab_tipo_nomina.getTab_seleccion().setSql("select " +
								"ROL.IDE_NRROL,TIN.DETALLE_NRTIN, " +
								"TEM.DETALLE_GTTEM," +
								"TIC.DETALLE_GTTCO, " +
								"SUC.NOM_SUCU " +
								"from NRH_ROL ROL " +
								"LEFT JOIN NRH_DETALLE_TIPO_NOMINA DTN ON DTN.IDE_NRDTN=ROL.IDE_NRDTN " +
								"LEFT JOIN NRH_TIPO_NOMINA TIN ON TIN.IDE_NRTIN=DTN.IDE_NRTIN " +
								"LEFT JOIN GTH_TIPO_CONTRATO TIC ON TIC.IDE_GTTCO=DTN.IDE_GTTCO " +
								"LEFT JOIN GTH_TIPO_EMPLEADO TEM ON TEM.IDE_GTTEM=DTN.IDE_GTTEM " +
								"LEFT JOIN SIS_SUCURSAL SUC ON SUC.IDE_SUCU=DTN.IDE_SUCU " +
								"where ROL.IDE_GEPRO="+sel_tab_periodo.getValorSeleccionado() +" and TIN.IDE_NRTIN="+utilitario.getVariable("p_nrh_tipo_nomina_liquidacion"));
						sel_tab_tipo_nomina.getTab_seleccion().ejecutarSql();
						sel_tab_tipo_nomina.setRadio();
						sel_tab_tipo_nomina.dibujar();					
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
					}				
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Periodo del Rol");
				}
			}else if(sel_tab_tipo_nomina.isVisible()){			
				try {
					if(sel_tab_tipo_nomina.getValorSeleccionado()!=null && !sel_tab_tipo_nomina.getValorSeleccionado().isEmpty()){						
						p_parametros.put("ide_nrrol",Integer.parseInt(sel_tab_tipo_nomina.getValorSeleccionado()));
						sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosRol(sel_tab_tipo_nomina.getValorSeleccionado()));
						sel_tab_empleados.getBot_aceptar().setMetodo("aceptarReporte");
						sel_tab_tipo_nomina.cerrar();
						sel_tab_empleados.dibujar();						
					}else{						
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
					}								
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
				}
			}else if(sel_tab_empleados.isVisible()){
				try {
					if (sel_tab_empleados.getSeleccionados()!=null && !sel_tab_empleados.getSeleccionados().isEmpty()) {
						p_parametros.put("ide_geedp",sel_tab_empleados.getSeleccionados());
						p_parametros.put("titulo", "LIQUIDACION DE HABERES");						
						p_parametros.put("p_liquidacion_elaborado",utilitario.getVariable("p_liquidacion_elaborado_por"));
						p_parametros.put("p_liquidacion_revisado",utilitario.getVariable("p_liquidacion_revisado_por"));
						p_parametros.put("p_liquidacion_aprobado",utilitario.getVariable("p_liquidacion_aprobado_por"));
						sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
						sel_tab_empleados.cerrar();
						sef_reporte.dibujar();
					}else{
						utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
					}					
				} catch (Exception e) {
					utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
				}
			}
		}
	}




	public Tabla getTab_rol() {
		return tab_rol;
	}

	public void setTab_rol(Tabla tab_rol) {
		this.tab_rol = tab_rol;
	}

	public Tabla getTab_detalle_rol() {
		return tab_detalle_rol;
	}

	public void setTab_detalle_rol(Tabla tab_detalle_rol) {
		this.tab_detalle_rol = tab_detalle_rol;
	}

	public Tabla getTab_empleado_departamento() {
		return tab_empleado_departamento;
	}

	public void setTab_empleado_departamento(Tabla tab_empleado_departamento) {
		this.tab_empleado_departamento = tab_empleado_departamento;
	}

	public Dialogo getDia_importar() {
		return dia_importar;
	}

	public void setDia_importar(Dialogo dia_importar) {
		this.dia_importar = dia_importar;
	}

	public AutoCompletar getAut_rubros() {
		return aut_rubros;
	}


	public void setAut_rubros(AutoCompletar aut_rubros) {
		this.aut_rubros = aut_rubros;
	}

	public Upload getUpl_archivo() {
		return upl_archivo;
	}

	public void setUpl_archivo(Upload upl_archivo) {
		this.upl_archivo = upl_archivo;
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

	public SeleccionTabla getSel_tab_periodo() {
		return sel_tab_periodo;
	}

	public void setSel_tab_periodo(SeleccionTabla sel_tab_periodo) {
		this.sel_tab_periodo = sel_tab_periodo;
	}

	public SeleccionTabla getSel_tab_tipo_nomina() {
		return sel_tab_tipo_nomina;
	}

	public void setSel_tab_tipo_nomina(SeleccionTabla sel_tab_tipo_nomina) {
		this.sel_tab_tipo_nomina = sel_tab_tipo_nomina;
	}

	public SeleccionTabla getSel_tab_empleados() {
		return sel_tab_empleados;
	}

	public void setSel_tab_empleados(SeleccionTabla sel_tab_empleados) {
		this.sel_tab_empleados = sel_tab_empleados;
	}


	public Dialogo getDia_valida_empleado() {
		return dia_valida_empleado;
	}


	public void setDia_valida_empleado(Dialogo dia_valida_empleado) {
		this.dia_valida_empleado = dia_valida_empleado;
	}


	public Tabla getTab_emp() {
		return tab_emp;
	}


	public void setTab_emp(Tabla tab_emp) {
		this.tab_emp = tab_emp;
	}

	public Confirmar getCon_guardar() {
		return con_guardar;
	}

	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}

	public SeleccionTabla getSel_tab_rubros() {
		return sel_tab_rubros;
	}

	public void setSel_tab_rubros(SeleccionTabla sel_tab_rubros) {
		this.sel_tab_rubros = sel_tab_rubros;
	}

	public SeleccionCalendario getSel_cal_mes() {
		return sel_cal_mes;
	}

	public void setSel_cal_mes(SeleccionCalendario sel_cal_mes) {
		this.sel_cal_mes = sel_cal_mes;
	}

	public SeleccionTabla getSel_tab_tipo_nomina_rubros() {
		return sel_tab_tipo_nomina_rubros;
	}

	public void setSel_tab_tipo_nomina_rubros(
			SeleccionTabla sel_tab_tipo_nomina_rubros) {
		this.sel_tab_tipo_nomina_rubros = sel_tab_tipo_nomina_rubros;
	}

	public Dialogo getDia_resumen() {
		return dia_resumen;
	}

	public void setDia_resumen(Dialogo dia_resumen) {
		this.dia_resumen = dia_resumen;
	}



	public SeleccionTabla getSel_sucursal() {
		return sel_sucursal;
	}



	public void setSel_sucursal(SeleccionTabla sel_sucursal) {
		this.sel_sucursal = sel_sucursal;
	}			

	 public void seleccionarEnviar(){                                
         if(sel_tab_tipo_nomina.isVisible())     {                               
                 try {
                         if(sel_tab_tipo_nomina.getValorSeleccionado()!=null && !sel_tab_tipo_nomina.getValorSeleccionado().isEmpty()){                                          
                                 p_parametros.put("IDE_NRDTN",Long.parseLong(ser_nomina.getRol(sel_tab_tipo_nomina.getValorSeleccionado()).getValor("IDE_NRDTN")));
                                 sel_tab_empleados.setSql(ser_nomina.getSqlEmpleadosRol(sel_tab_tipo_nomina.getValorSeleccionado()));
                                 sel_tab_empleados.getBot_aceptar().setMetodo("seleccionarEnviar");
                                 sel_tab_tipo_nomina.cerrar();

                                 sel_tab_empleados.dibujar();                                            
                         }else{                                          
                                 utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
                         }                                                               
                 } catch (Exception e) {
                         utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Tipo de Nomina");
                 }
         }
         else if(sel_tab_empleados.isVisible()){

                 if (sel_tab_empleados.getSeleccionados()!=null && !sel_tab_empleados.getSeleccionados().isEmpty()) {

                         //Busco los correos de los empleados seleccionados
                         StringBuilder str_ide=new StringBuilder();
                         int int_num_col_idegtemp=sel_tab_empleados.getTab_seleccion().getNumeroColumna("IDE_GTEMP");

                         for (Fila filaActual:sel_tab_empleados.getTab_seleccion().getSeleccionados()) {
                                 if(str_ide.toString().isEmpty()==false){
                                         str_ide.append(",");
                                 }
                                 str_ide.append(filaActual.getCampos()[int_num_col_idegtemp]);
                         }
                         TablaGenerica tab_correos =ser_empleado.getCorreoEmpleados(str_ide.toString());                                         
                         StringBuilder str_resultado=new StringBuilder(getFormatoInformacion("TOTAL DE EMPLEADOS PARA ENVIAR CORREO ELECTR�NICO : "+sel_tab_empleados.getTab_seleccion().getSeleccionados().length));
                         //EnviarCorreo env_enviar = new EnviarCorreo();
                         String str_asunto="NOMINA DE PAGOS";
                         //Proceso de enviar a cada empleado                             
                         GenerarReporte ger = new GenerarReporte();                                              
                         for (Fila filaActual:sel_tab_empleados.getTab_seleccion().getSeleccionados()) {

                                 for (int j = 0; j <tab_correos.getTotalFilas(); j++) {
                                         //Busca el correo
                                         if(filaActual.getCampos()[int_num_col_idegtemp].toString().equals(tab_correos.getValor(j,"IDE_GTEMP"))){
                                                 //Encontro el correo
                                                 String str_mail=tab_correos.getValor(j,"DETALLE_GTCOR");
                                                 if(str_mail==null || str_mail.isEmpty()){
                                                         //No existe correo del empleado configurado para las notificaciones
                                                         str_resultado.append(getFormatoError("El empleado : "+filaActual.getCampos()[2]+" No tiene configurado correo electr�nico para notificaciones" ));
                                                         //str_mail="hhsoul_louis@hotmail.com";
                                                         continue;
                                                 }
                                                 String str_mensaje="Fecha Generaci�n: "+utilitario.getFechaLarga(utilitario.getFechaActual())+" Hora : "+utilitario.getHoraActual()+" "+
                                                 "Funcionario(a): "+filaActual.getCampos()[2]+" Para su conocimiento, le adjuntamos un archivo pdf con el detalle del rol de pago generado por el Sistema de Gest�on de Talento Humano.";
                                                 //Mismo proceso de llamar a reporte boleta individual
                                                 
                                                 p_parametros.put("IDE_GEEDP", filaActual.getCampos()[0].toString());            
                                                 p_parametros.put("titulo", " BOLETA DE PAGO");
                                                 p_parametros.put("IDE_NRTIR",utilitario.getVariable("p_nrh_trubro_egreso")+","+utilitario.getVariable("p_nrh_trubro_ingreso"));
                                                 p_parametros.put("par_total_recibir",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
                                                 p_parametros.put("par_total_ingresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
                                                 p_parametros.put("par_total_egresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));                                                    
                                                 
                                                 File fil_rol=generar(p_parametros, "/reportes/rep_rol_de_pagos/rep_n_rol_pagos.jasper",filaActual.getCampos()[0].toString());
                                                 List<File> lis_file = new ArrayList<File>();
                                                 lis_file.add(fil_rol);
                                                // String str_msj= env_enviar.agregarCorreo(str_mail, str_asunto, str_mensaje.toString(), lis_file);
                                                 TablaGenerica tab_correo = utilitario.consultar("select ide_corr,correo_corr,clave_corr from sis_correo limit 1");
                                                 EnviaEmail(tab_correo.getValor("correo_corr"), tab_correo.getValor("clave_corr"), str_mail, str_asunto, str_mensaje,fil_rol);
                                                
                                            tab_correos.getFilas().remove(j);

                                                 break;
                                         }
                                 }


                         }
                         sel_tab_empleados.cerrar();
                         /*	
                         //String str_mensaje=env_enviar.enviarTodos();
                         System.out.println("DFJ****   "+str_mensaje);
                         if(str_mensaje.isEmpty()==false){
                                 str_resultado.append(getFormatoError(str_mensaje));
                         }
						*/
                         edi_msj.setValue(str_resultado);
                         dia_resumen.dibujar();

                 }else{
                         utilitario.agregarMensajeInfo("No se puede continuar", "No ha seleccionado ningun Empleado");
                 }                                       

         }

 }


 public File generar(Map parametros, String reporte,String IDE_EMPL) {     
         try {           
                 FacesContext fc = FacesContext.getCurrentInstance();    
                 ExternalContext ec = fc.getExternalContext();  
                 JasperPrint jasperPrint=null;
                 try {
                         
                         InputStream fis = ec.getResourceAsStream(reporte);
                         JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fis);
                         
                         try {
                         parametros.put("ide_empr", Integer.parseInt(utilitario.getVariable("ide_empr")));
                         } catch (Exception e) {
                         }
                         try {
                         parametros.put("ide_sucu", Integer.parseInt(utilitario.getVariable("ide_sucu")));
                         } catch (Exception e) {
                         }

                         try {
                         parametros.put("usuario", Integer.parseInt(utilitario.getVariable("ide_usua")));
                         } catch (Exception e) {
                         }

                         parametros.put("SUBREPORT_DIR", utilitario.getURL());
                         
                         jasperPrint = JasperFillManager.fillReport(
                         jasperReport, parametros, utilitario.getConexion().getConnection());
                         
                         } catch (Exception e) {
                         System.out.println("error ejecutar" + e.getMessage());
                         }
                 
                 JRExporter exporter = new JRPdfExporter();         
                 exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);    
                 File fil_reporte = new File(ec.getRealPath("/reportes/rol_"+IDE_EMPL+ ".pdf"));          
                 exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);        
                 exporter.exportReport();                        
                 return fil_reporte;     
                 } catch (Exception ex) {         
                         System.out.println("error" + ex.getMessage());        
                         ex.printStackTrace();     
                         }    
         return null;    
         }




	public SeleccionTabla getSet_rubros() {
		return set_rubros;
	}



	public void setSet_rubros(SeleccionTabla set_rubros) {
		this.set_rubros = set_rubros;
	}



	public SeleccionTabla getSet_det_tip_nomina() {
		return set_det_tip_nomina;
	}



	public void setSet_det_tip_nomina(SeleccionTabla set_det_tip_nomina) {
		this.set_det_tip_nomina = set_det_tip_nomina;
	}



	public SeleccionTabla getSet_tipo_rubro() {
		return set_tipo_rubro;
	}



	public void setSet_tipo_rubro(SeleccionTabla set_tipo_rubro) {
		this.set_tipo_rubro = set_tipo_rubro;
	}


	public SeleccionTabla getSet_empleado() {
		return set_empleado;
	}


	public void setSet_empleado(SeleccionTabla set_empleado) {
		this.set_empleado = set_empleado;
	}



	public Dialogo getDia_balance_ingresos_egresos_emp() {
		return dia_balance_ingresos_egresos_emp;
	}



	public void setDia_balance_ingresos_egresos_emp(
			Dialogo dia_balance_ingresos_egresos_emp) {
		this.dia_balance_ingresos_egresos_emp = dia_balance_ingresos_egresos_emp;
	}



	public Tabla getTab_bal_ing_egr_emp() {
		return tab_bal_ing_egr_emp;
	}



	public void setTab_bal_ing_egr_emp(Tabla tab_bal_ing_egr_emp) {
		this.tab_bal_ing_egr_emp = tab_bal_ing_egr_emp;
	}

        
	/////ENVIO DE EMAIL

String miCorreo;
String miContraseña;
//String servidorSMTP = "smtp.gmail.com";
//String puertoEnvio = "465";
String servidorSMTP = "mail.emgirs.gob.ec";
String puertoEnvio = "25";
String mailReceptor;
String asunto;
String cuerpo;
String filename = "C:/reporte.pdf";        
// metodo que recibe y envia el email
	public void EnviaEmail(String miCorreo, String miContraseña,
			String mailReceptor, String asunto, String cuerpo, File filearchivo) {
		this.miCorreo = miCorreo;
		this.miContraseña = miContraseña;
		this.mailReceptor = mailReceptor;
		this.asunto = asunto;
		this.cuerpo = cuerpo;

		Properties props = new Properties();// propiedades a agragar
		props.put("mail.smtp.user", this.miCorreo);// correo origen
		props.put("mail.smtp.host", servidorSMTP);// tipo de servidor
		props.put("mail.smtp.port", puertoEnvio);// puesto de salida
		props.put("mail.smtp.starttls.enableue", false);// inicializar el
														// servidor
		// props.put("mail.transport.protocol",smpt);
		props.put("mail.smtp.auth", "false");// autentificacion
		props.put("mail.smtp.socketFactory.port", puertoEnvio);// activar el
																// puerto
		// props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", false);

		SecurityManager security = System.getSecurityManager();

		try {
			Authenticator auth = new autentificadorSMTP();// autentificar el
															// correo
			Session session = Session.getInstance(props, auth);// se inica una
																// session

			// Se compone la parte del texto
			BodyPart texto = new MimeBodyPart();
			texto.setText(cuerpo);

			// Se compone el adjunto con la imagen
			BodyPart adjunto = new MimeBodyPart();
			DataSource source = new FileDataSource(filearchivo);
			adjunto.setDataHandler(new DataHandler(source));
			adjunto.setFileName(filearchivo.getName());

			// Una MultiParte para agrupar texto e imagen.
			MimeMultipart multiParte = new MimeMultipart();
			//BodyPart messageBodyPart = new MimeBodyPart();
	        //messageBodyPart.setContent(texto, "text/html");			
	        
	        multiParte.addBodyPart(texto);
			multiParte.addBodyPart(adjunto);

			// Se compone el correo, dando to, from, subject y el
			// contenido.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.miCorreo));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					this.mailReceptor));
			message.setSubject(asunto);
			message.setContent(multiParte);

			Transport.send(message);// envia el mensaje


		} catch (Exception mex) {
			System.out.println("ff" + mex);
		}// fin try-catch
	}// fin metodo enviaEmail

	private class autentificadorSMTP extends javax.mail.Authenticator {

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(miCorreo, miContraseña);
		}
	}


}
