/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.behavior.ajax.AjaxBehavior;

import sistema.aplicacion.Pantalla;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Check;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ListaSeleccion;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;

public class pre_partida_grupo_cargo extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	private Dialogo dia_activacion_desactivacion = new Dialogo();	
	private Calendario cal_fecha = new Calendario();	
	private AreaTexto art_motivo = new AreaTexto();	
	private Etiqueta eti_fecha=new Etiqueta();
	private Reporte rep_reporte=new Reporte();
	private Texto text_sueldo_subrogacion = new Texto();
	private SeleccionFormatoReporte sef_reporte=new SeleccionFormatoReporte();
	private SeleccionTabla set_vacante=new SeleccionTabla();
	private Map p_parametros = new HashMap();
	private	ListaSeleccion lis_activo=new ListaSeleccion();
	private	ListaSeleccion lis_vacante=new ListaSeleccion();
	private Dialogo dia_filtro_activo = new Dialogo();
	private Dialogo dia_filtro_vacante = new Dialogo();
	private Dialogo dia_sueldo_subrogacion = new Dialogo();
	private Confirmar con_guardar=new Confirmar();
	private Confirmar con_guardar_partida=new Confirmar();
	private SeleccionTabla set_partida=new SeleccionTabla();



	public pre_partida_grupo_cargo() {
		bar_botones.agregarReporte();

		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(rep_reporte);

		sef_reporte.setId("sef_reporte");
		agregarComponente(sef_reporte);


		Boton bot_cambia_estado = new Boton();
		bar_botones.agregarBoton(bot_cambia_estado);
		
		Boton bot_activar_vacante=new Boton();
		bar_botones.agregarBoton(bot_activar_vacante);


		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("GEN_PARTIDA_GRUPO_CARGO", "IDE_GEPGC", 1);
		tab_tabla.setCampoOrden("IDE_GEPGC desc");
		tab_tabla.getColumna("IDE_GEGRO").setUnico(true);
		tab_tabla.getColumna("IDE_GECAF").setUnico(true);
		tab_tabla.getColumna("IDE_SUCU").setUnico(true);
		tab_tabla.getColumna("IDE_GEARE").setUnico(true);
		tab_tabla.getColumna("IDE_GEDEP").setUnico(true);
		tab_tabla.getColumna("IDE_GEPAP").setUnico(true);
		tab_tabla.getColumna("IDE_GEGRO").setCombo("GEN_GRUPO_OCUPACIONAL",	"IDE_GEGRO", "DETALLE_GEGRO", "");
		tab_tabla.getColumna("IDE_GEGRO").setMetodoChange("grupoOcupacional");
		tab_tabla.getColumna("IDE_GECAF").setCombo("select a.IDE_GECAF,a.DETALLE_GECAF from GEN_CARGO_FUNCIONAL a, GEN_GRUPO_CARGO b where a.IDE_GECAF = b.IDE_GECAF");
		tab_tabla.getColumna("IDE_GECAF").setBuscarenCombo(false);
		//tab_tabla.getColumna("IDE_GECAF").setVisible(false);
		tab_tabla.setMostrarcampoSucursal(true);
		tab_tabla.getColumna("IDE_SUCU").setCombo("SIS_SUCURSAL", "IDE_SUCU","NOM_SUCU", "");
		tab_tabla.getColumna("IDE_SUCU").setBuscarenCombo(false);
		tab_tabla.getColumna("IDE_SUCU").setNombreVisual("SUCURSAL");
		tab_tabla.getColumna("IDE_SUCU").setMetodoChange("cargarArea");
		tab_tabla.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_DEPARTAMENTO_SUCURSAL a " +
				"inner join GEN_AREA b on a.IDE_GEARE=b.IDE_GEARE " +
				"GROUP BY b.IDE_GEARE,b.DETALLE_GEARE " +
				"ORDER BY b.DETALLE_GEARE");
		tab_tabla.getColumna("IDE_GEARE").setMetodoChange("cargarDepartamentos");
		tab_tabla.getColumna("IDE_GEARE").setBuscarenCombo(false);
		tab_tabla.getColumna("IDE_GEDEP").setCombo("SELECT DISTINCT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEDEP=b.IDE_GEDEP");
		tab_tabla.getColumna("IDE_GEDEP").setBuscarenCombo(false);
		tab_tabla.getColumna("IDE_GEPAP").setCombo("GEN_PARTIDA_PRESUPUESTARIA", "IDE_GEPAP","CODIGO_PARTIDA_GEPAP,DETALLE_GEPAP", "ACTIVO_GEPAP=TRUE");
		tab_tabla.getColumna("IDE_GEPAP").setAutoCompletar();
		tab_tabla.getColumna("IDE_GEPAP").setLectura(true);
		tab_tabla.setMostrarcampoSucursal(true);
		tab_tabla.getColumna("ACTIVO_GEPGC").setCheck();
		tab_tabla.getColumna("ACTIVO_GEPGC").setValorDefecto("true");
		tab_tabla.getColumna("IDE_GTTEM").setCombo("GTH_TIPO_EMPLEADO","IDE_GTTEM", "DETALLE_GTTEM", "");
		tab_tabla.getColumna("IDE_GTTEM").setRequerida(true);
		tab_tabla.getColumna("IDE_GEPAP").setRequerida(true);
		tab_tabla.getColumna("VACANTE_GEPGC").setCheck();
		tab_tabla.getColumna("VACANTE_GEPGC").setValorDefecto("true");
		//		tab_tabla.getColumna("VACANTE_GEPGC").setLectura(true);
		tab_tabla.getColumna("FECHA_ACTIVACION_GEPGC").setLectura(true);		
		tab_tabla.getColumna("FECHA_DESACTIVA_GEPGC").setLectura(true);
		tab_tabla.getColumna("MOTIVO_GEPGC").setLectura(true);
		tab_tabla.getColumna("ACTIVO_GEPGC").setLectura(true);
		tab_tabla.getColumna("ENCARGO_GEPGC").setCheck();
		tab_tabla.getColumna("SALARIO_ENCARGO_GEPGC").setLectura(true);
		tab_tabla.getColumna("ENCARGO_GEPGC").setMetodoChange("salarioEncargo");
		tab_tabla.getColumna("IDE_GEPGC").setNombreVisual("CODIGO");
		tab_tabla.getColumna("IDE_GTTEM").setNombreVisual("REGIMEN LABORAL");
		tab_tabla.getColumna("IDE_SUCU").setNombreVisual("LUGAR TRABAJO");
		tab_tabla.getColumna("IDE_SUCU").setOrden(3);
		tab_tabla.getColumna("IDE_GEGRO").setNombreVisual("GRUPO OCUPACIONAL");
		tab_tabla.getColumna("IDE_GEGRO").setOrden(4);
		tab_tabla.getColumna("IDE_GEPAP").setNombreVisual("PARTIDA PRESUPUESTARIA");
		tab_tabla.getColumna("IDE_GEPAP").setOrden(7);
		tab_tabla.getColumna("IDE_GEARE").setNombreVisual("PROCESO");
		tab_tabla.getColumna("IDE_GEARE").setOrden(5);
		tab_tabla.getColumna("IDE_GEDEP").setNombreVisual("SUB_PROCESO");
		tab_tabla.getColumna("IDE_GEDEP").setOrden(6);
		tab_tabla.getColumna("TITULO_CARGO_GEPGC").setNombreVisual("DENOMINACION PUESTO");
		tab_tabla.getColumna("TITULO_CARGO_GEPGC").setOrden(2);
		tab_tabla.getColumna("FECHA_ACTIVACION_GEPGC").setNombreVisual("FECHA ACTIVACióN");
		tab_tabla.getColumna("FECHA_DESACTIVA_GEPGC ").setNombreVisual("FECHA DESACTIVA");
		tab_tabla.getColumna("MOTIVO_GEPGC").setNombreVisual("MOTIVO ACTIVACióN");
		tab_tabla.getColumna("ACTIVO_GEPGC").setNombreVisual("ACTIVO");
		tab_tabla.getColumna("VACANTE_GEPGC").setNombreVisual("VACANTE");

		//tab_tabla.getColumna("ENCARGO_GEPGC").setVisible(false);
		//tab_tabla.getColumna("SALARIO_ENCARGO_GEPGC").setVisible(false);
		tab_tabla.setTipoFormulario(true);
		tab_tabla.getGrid().setColumns(4);
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);


		bot_cambia_estado.setValue("ACTIVAR / DESACTIVAR");
		bot_cambia_estado.setMetodo("cambiaEstado");
		cal_fecha.setFechaActual();
		cal_fecha.setDisabled(true);

		art_motivo.setCols(40);
		art_motivo.setRows(6);		
		Grid gri_activar = new Grid();
		gri_activar.setWidth("100%");
		gri_activar.setColumns(2);
		gri_activar.getChildren().add(eti_fecha);
		gri_activar.getChildren().add(cal_fecha);
		gri_activar.getChildren().add(new Etiqueta("MOTIVO"));
		gri_activar.getChildren().add(art_motivo);

		dia_activacion_desactivacion.setId("dia_activacion_desactivacion");
		dia_activacion_desactivacion.setTitle("ACTIVAR ");
		dia_activacion_desactivacion.setWidth("30%");
		dia_activacion_desactivacion.setHeight("30%");
		dia_activacion_desactivacion.getBot_aceptar().setMetodo("aceptarCambiarEstado");
		dia_activacion_desactivacion.setDialogo(gri_activar);
		dia_activacion_desactivacion.setDynamic(false);
		gri_activar.setStyle("width:" + (dia_activacion_desactivacion.getAnchoPanel() - 5)+ "px;height:" + dia_activacion_desactivacion.getAltoPanel()+ "px;overflow: auto;display: block;");


		Division div_division = new Division();

		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		agregarComponente(dia_activacion_desactivacion);		
		actualizarCombos();
		agregarComponente(sef_reporte);
		agregarComponente(set_vacante);


		List lista = new ArrayList();
		Object fila1[] = {
				"0", "INACTIVO"
		};
		Object fila2[] = {
				"1", "ACTIVO"
		};
		lista.add(fila1);
		lista.add(fila2);

		lis_activo.setListaSeleccion(lista);
		lis_activo.setVertical();
		dia_filtro_activo.setId("dia_filtro_activo");
		dia_filtro_activo.setTitle("SELECCIONE GRUPO ACTIVO / INACTIVO");
		dia_filtro_activo.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_activo.setDialogo(lis_activo);
		agregarComponente(dia_filtro_activo);
		List lista1 = new ArrayList();
		Object fila11[] = {
				"1", "VACANTE"
		};
		Object fila22[] = {
				"0", "SIN VACANTE"
		};
		lista1.add(fila11);
		lista1.add(fila22);

		lis_vacante.setListaSeleccion(lista1);
		lis_vacante.setVertical();
		dia_filtro_vacante.setId("dia_filtro_vacante");
		dia_filtro_vacante.setTitle("SELECCIONE VACANTE/ SIN VACANTE");
		dia_filtro_vacante.getBot_aceptar().setMetodo("aceptarReporte");
		dia_filtro_vacante.setDialogo(lis_vacante);
		agregarComponente(dia_filtro_vacante);


		///// DIALOGO INSERTAR SUELDO SUBROGACION
		text_sueldo_subrogacion.setSize(50);
		text_sueldo_subrogacion.setSoloNumeros();
		Grid grid_sueldo_subrogacion = new Grid();
		grid_sueldo_subrogacion.setColumns(2);
		grid_sueldo_subrogacion.getChildren().add(new Etiqueta("SUELDO SUBROGACION:"));
		grid_sueldo_subrogacion.getChildren().add(text_sueldo_subrogacion);

		dia_sueldo_subrogacion.setId("dia_sueldo_subrogacion");
		dia_sueldo_subrogacion.setTitle("Ingreso del Sueldo de Subrogación");

		dia_sueldo_subrogacion.setWidth("40%");
		dia_sueldo_subrogacion.setHeight("20%");
		dia_sueldo_subrogacion.setDialogo(grid_sueldo_subrogacion);
		dia_sueldo_subrogacion.getBot_aceptar().setMetodo("aceptaEncargo");
		dia_sueldo_subrogacion.getBot_cancelar().setMetodo("cancelarEncargo");
		gru_pantalla.getChildren().add(dia_sueldo_subrogacion);
		
		bot_activar_vacante.setValue("ACTIVAR VACANTE");
		bot_activar_vacante.setMetodo("activarVacante");
		
		con_guardar.setId("con_guardar");
		con_guardar.setMessage("ESTA SEGURO DE ACTIVAR VACANTE");
		con_guardar.setTitle("CONFIRMACION DE ACTIVACION VACANTE");
		con_guardar.getBot_aceptar().setMetodo("validarVacante");

		agregarComponente(con_guardar);

		
		/////pARTIDA PRESUPUESTARIA BOTON
		Boton bot_agregar=new Boton();
		bot_agregar.setValue("AGREGAR PARTIDA PRESUPUESTARIA");
		bot_agregar.setMetodo("agregarPartida");
		bar_botones.agregarBoton(bot_agregar);
		///boton guaradar
		con_guardar_partida.setId("con_guardar_partida");
		agregarComponente(con_guardar_partida);
		
		
		set_partida.setId("set_partida");
		set_partida.setTitle("SELECCIONE UNA PARTIDA PRESUPUESTARIA");
		set_partida.setRadio(); //solo selecciona una opcion
		set_partida.setSeleccionTabla("select ide_gepap,codigo_partida_gepap,detalle_gepap from gen_partida_presupuestaria order by codigo_partida_gepap", "ide_gepap");
		set_partida.getTab_seleccion().getColumna("codigo_partida_gepap").setFiltroContenido(); //pone filtro
		set_partida.getTab_seleccion().getColumna("detalle_gepap").setFiltroContenido();//pone filtro
		set_partida.getBot_aceptar().setMetodo("modificarPartida");
		agregarComponente(set_partida);

		
	}
		//////////////////SE ME CARGA EL MISMO DATO AL OTRO CAMPO 
			
		public void grupoOcupacional(AjaxBehaviorEvent evt){
		tab_tabla.modificar(evt);//Siempre es la primera linea
		tab_tabla.setValor("IDE_GECAF", tab_tabla.getValor("IDE_GEGRO"));
		utilitario.addUpdateTabla(tab_tabla, "IDE_GECAF", "");
		}


	/////partda presupestaria
	public void agregarPartida(){
		
		//Filtrar los clasificadores del Año seleccionado
		set_partida.getTab_seleccion().setSql("select ide_gepap,codigo_partida_gepap,detalle_gepap from gen_partida_presupuestaria order by codigo_partida_gepap");
		set_partida.getTab_seleccion().ejecutarSql();
		set_partida.dibujar();
	}
	
	public void modificarPartida(){
			String str_partidaActualizado=set_partida.getValorSeleccionado();
		    tab_tabla.setValor("ide_gepap",str_partidaActualizado);			
		    tab_tabla.modificar(tab_tabla.getFilaActual());
			utilitario.addUpdate("tab_tabla");	

			con_guardar_partida.setMessage("Esta Seguro que desea Guardar ");
			con_guardar_partida.setTitle("CONFIRMCióN GUARDAR PARTIDA");
			con_guardar_partida.getBot_aceptar().setMetodo("guardarPartida");
			con_guardar_partida.dibujar();
			utilitario.addUpdate("con_guardar_partida");


		}
		public void guardarPartida(){
			System.out.println("Entra a guardar...");
			tab_tabla.guardar();
			con_guardar_partida.cerrar();
			set_partida.cerrar();


			guardarPantalla();

		}

	

	public void salarioEncargo(AjaxBehaviorEvent evt){

		tab_tabla.modificar(evt);

		if(tab_tabla.getValor("ENCARGO_GEPGC").equals("true")){		
			text_sueldo_subrogacion.limpiar();	
			dia_sueldo_subrogacion.dibujar();
		}
		else{

			tab_tabla.setValor("SALARIO_ENCARGO_GEPGC","");
			utilitario.addUpdate("tab_tabla");
		}

	}
	public void aceptaEncargo(){


		try{

			double valor = Double.parseDouble(text_sueldo_subrogacion.getValue()+"");

			if(valor <=0 ){
				utilitario.agregarMensajeError("Valor Requerido", "Ingrese Un valor Sueldo de Subrogacion");
			}
			else{
				tab_tabla.setValor("SALARIO_ENCARGO_GEPGC", text_sueldo_subrogacion.getValue() + "");
				dia_sueldo_subrogacion.cerrar();

				utilitario.addUpdate("tab_tabla");
			}
		}
		catch (Exception e){
			utilitario.agregarMensajeError("Valor Requerido", "Ingrese Un valor Sueldo de Subrogacion");
		}

	}
	public void cancelarEncargo(){
		tab_tabla.setValor("ENCARGO_GEPGC","false");
		utilitario.addUpdateTabla(tab_tabla,"ENCARGO_GEPGC", "");
		dia_sueldo_subrogacion.cerrar();
	}

	public void cambiaEstado() {
		if(tab_tabla.getValor("ACTIVO_GEPGC").equals("false")){
			dia_activacion_desactivacion.setTitle("ACTIVAR PARTIDA");
			eti_fecha.setValue("FECHA ACTIVACióN");
		}else{
			dia_activacion_desactivacion.setTitle("DESACTIVAR PARTIDA");
			eti_fecha.setValue("FECHA DESACTIVACióN");				

		}	
		art_motivo.limpiar();
		dia_activacion_desactivacion.dibujar();
	}


	public boolean validarDialogo(){		
		if (cal_fecha.getValue() == null) {
			utilitario.agregarMensajeInfo("No se puede desactivar", "Debe seleccionar una fecha de desactivación");
			return false;
		}      
		if (art_motivo.getValue() == null || art_motivo.getValue().toString().isEmpty()) {
			utilitario.agregarMensajeInfo("No se puede desactivar", "Debe un motivo");
			return false;
		}
		return true;
	}


	public void aceptarCambiarEstado(){		
		if(validarDialogo()){			
			if(tab_tabla.getValor("ACTIVO_GEPGC").equals("false")){
				tab_tabla.setValor("FECHA_ACTIVACION_GEPGC",utilitario.getFormatoFecha(cal_fecha.getFecha()));	
				tab_tabla.setValor("ACTIVO_GEPGC","true");
			}else{
				tab_tabla.setValor("FECHA_DESACTIVA_GEPGC",utilitario.getFormatoFecha(cal_fecha.getFecha()));
				tab_tabla.setValor("ACTIVO_GEPGC","false");
			}			
			tab_tabla.setValor("MOTIVO_GEPGC",art_motivo.getValue().toString());

			tab_tabla.modificar(tab_tabla.getFilaActual());
			tab_tabla.guardar();			
			guardarPantalla();
			dia_activacion_desactivacion.cerrar();			
		}		
	}


	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		actualizarCombos();
	}

	public void cargarArea() {
		
		tab_tabla.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_DEPARTAMENTO_SUCURSAL a " +
				"inner join GEN_AREA b on a.IDE_GEARE=b.IDE_GEARE " +
				"where a.IDE_SUCU="+ tab_tabla.getValor("IDE_SUCU")+" "+
				"GROUP BY b.IDE_GEARE,b.DETALLE_GEARE " +
				"ORDER BY b.DETALLE_GEARE" );
		utilitario.addUpdateTabla(tab_tabla, "IDE_GEARE", "");
		
	}

	public void cargarDepartamentos() {
		if (tab_tabla.getTotalFilas()>0){
		tab_tabla.getColumna("IDE_GEDEP").setCombo("SELECT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEDEP=b.IDE_GEDEP AND b.IDE_GEARE="+ tab_tabla.getValor("IDE_GEARE")+ " AND IDE_SUCU="+ tab_tabla.getValor("IDE_SUCU"));
		utilitario.addUpdateTabla(tab_tabla, "IDE_GEDEP", "");
		}
	}

	public void cargarArea(AjaxBehaviorEvent evt) {
		tab_tabla.modificar(evt);
		cargarArea();
	}

	public void cargarDepartamentos(AjaxBehaviorEvent evt) {
		tab_tabla.modificar(evt);
		cargarDepartamentos();	
	}

	private void actualizarCombos() {


		
		tab_tabla.getColumna("IDE_GEARE").setCombo("SELECT b.IDE_GEARE,b.DETALLE_GEARE FROM GEN_DEPARTAMENTO_SUCURSAL a " +
				"inner join GEN_AREA b on a.IDE_GEARE=b.IDE_GEARE " +
				"where a.IDE_SUCU="+ tab_tabla.getValor("IDE_SUCU")+" "+
				"GROUP BY b.IDE_GEARE,b.DETALLE_GEARE " +
				"ORDER BY b.DETALLE_GEARE" );

		
		tab_tabla.getColumna("IDE_GEDEP").setCombo("SELECT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEDEP=b.IDE_GEDEP AND b.IDE_GEARE="+ tab_tabla.getValor("IDE_GEARE")+ " AND IDE_SUCU="+ tab_tabla.getValor("IDE_SUCU"));
		tab_tabla.actualizarCombosFormulario();
	}


	@Override
	public void buscar() {
		// TODO Auto-generated method stub
		//Carga todos los valores para q funcione la busqueda x q son combos anidados
		tab_tabla.getColumna("IDE_GECAF").setCombo("select a.IDE_GECAF,a.DETALLE_GECAF from GEN_CARGO_FUNCIONAL a");
		tab_tabla.getColumna("IDE_GEARE").setCombo("SELECT a.IDE_GEARE,a.DETALLE_GEARE FROM GEN_AREA a");
		tab_tabla.getColumna("IDE_GEDEP").setCombo("SELECT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a");
		tab_tabla.getColumna("IDE_GEPAP").setCombo("GEN_PARTIDA_PRESUPUESTARIA", "IDE_GEPAP","CODIGO_PARTIDA_GEPAP,DETALLE_GEPAP","");
		
		super.buscar();
	}

	@Override
	public void aceptarBuscar() {
		// TODO Auto-generated method stub
		super.aceptarBuscar();
		if (utilitario.getBuscaTabla().isVisible() == false) {
			actualizarCombos();
		}
	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		actualizarCombos();
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		actualizarCombos();
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		actualizarCombos();
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		actualizarCombos();
	}

	@Override
	public void insertar() {
		tab_tabla.getColumna("IDE_GEARE").setCombo("SELECT a.IDE_GEARE,a.DETALLE_GEARE FROM GEN_AREA a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEARE=b.IDE_GEARE AND IDE_SUCU=-1");
		tab_tabla.getColumna("IDE_GEDEP").setCombo("SELECT a.IDE_GEDEP,a.DETALLE_GEDEP FROM GEN_DEPARTAMENTO a, GEN_DEPARTAMENTO_SUCURSAL b WHERE a.IDE_GEDEP=b.IDE_GEDEP AND b.IDE_GEARE=-1 AND IDE_SUCU=-1");
		tab_tabla.insertar();
		tab_tabla.setValor("IDE_SUCU", "");

	}

	@Override
	public void guardar() {
				if (tab_tabla.getTotalFilas() > 0) {			
					TablaGenerica tab_partida = utilitario.consultar("SELECT * FROM GEN_PARTIDA_GRUPO_CARGO WHERE IDE_GEPGC="+ tab_tabla.getValor("IDE_GEPGC")+ " AND vacante_gepgc=false");
					System.out.println("imprimo sql"+tab_partida.getSql());
					if (tab_partida.getTotalFilas() > 0) {
						utilitario.agregarMensajeInfo("No se puede guardar","Esta partida ya se encuentra en uso");
					} else {
						tab_tabla.guardar();
					}
				} else {
				}		
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		if (tab_tabla.eliminar()) {
			actualizarCombos();
		}
	}

	public void activarVacante(){
		if (tab_tabla.getTotalFilas()>0) {
			con_guardar.dibujar();
			if (con_guardar.isVisible()) {
				con_guardar.setMessage("ESTA SEGURO DE ACTIVAR VACANTE");
				con_guardar.setTitle("CONFIRMACION DE ACTIVACION VACANTE");
				con_guardar.getBot_aceptar().setMetodo("validarVacante");
				utilitario.addUpdate("con_guardar");
			}
		}else{
			utilitario.agregarMensajeInfo("No se puede Activar Vacante", "No ha seleccionado nungu registro");
		}
	}
	
	public void validarVacante(){
		con_guardar.cerrar();
		utilitario.getConexion().agregarSqlPantalla("update GEN_PARTIDA_GRUPO_CARGO set vacante_gepgc=true where ide_gepgc="+tab_tabla.getValorSeleccionado());
		guardarPantalla();
		tab_tabla.ejecutarSql();		
	}
	
	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}


	public Dialogo getDia_activacion_desactivacion() {
		return dia_activacion_desactivacion;
	}

	public void setDia_activacion_desactivacion(Dialogo dia_activacion_desactivacion) {
		this.dia_activacion_desactivacion = dia_activacion_desactivacion;
	}


	@Override
	public void abrirListaReportes() {
		rep_reporte.dibujar();
	}


	@Override
	public void aceptarReporte() {
		if (rep_reporte.getReporteSelecionado().equals("Partida Grupo Cargo")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();
				rep_reporte.cerrar();
				dia_filtro_activo.dibujar();			
			}else if (dia_filtro_activo.isVisible()) {
				if(lis_activo.getSeleccionados()!= null){


					dia_filtro_vacante.dibujar();
					p_parametros.put("ACTIVO_GEPGC",lis_activo.getSeleccionados());
					//	dia_filtro_vacante.getBot_aceptar().setMetodo("aceptarReporte");
					dia_filtro_activo.cerrar();
				}
				else{
					utilitario.agregarMensajeInfo("Escoga una opcion","please");
				}
			}else if (dia_filtro_vacante.isVisible()) {
				System.out.println("activo"+lis_activo.getSeleccionados());
				System.out.println("vacan"+lis_vacante.getSeleccionados());

				p_parametros.put("VACANTE_GEPGC",lis_vacante.getSeleccionados());
				p_parametros.put("titulo","DETALLE PARTIDA PRESUPUESTARIA");

				dia_filtro_vacante.cerrar();
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());

				sef_reporte.dibujar();



			}


		}


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


	public SeleccionTabla getSet_vacante() {
		return set_vacante;
	}


	public void setSet_vacante(SeleccionTabla set_vacante) {
		this.set_vacante = set_vacante;
	}


	public Dialogo getDia_filtro_activo() {
		return dia_filtro_activo;
	}


	public void setDia_filtro_activo(Dialogo dia_filtro_activo) {
		this.dia_filtro_activo = dia_filtro_activo;
	}


	public Dialogo getDia_filtro_vacante() {
		return dia_filtro_vacante;
	}


	public void setDia_filtro_vacante(Dialogo dia_filtro_vacante) {
		this.dia_filtro_vacante = dia_filtro_vacante;
	}

	public Dialogo getDia_sueldo_subrogacion() {
		return dia_sueldo_subrogacion;
	}

	public void setDia_sueldo_subrogacion(Dialogo dia_sueldo_subrogacion) {
		this.dia_sueldo_subrogacion = dia_sueldo_subrogacion;
	}
	public SeleccionTabla getSet_partida() {
		return set_partida;
	}
	public void setSet_partida(SeleccionTabla set_partida) {
		this.set_partida = set_partida;
	}

}
