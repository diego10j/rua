package paq_general;

import javax.ejb.EJB;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import paq_general.ejb.ServicioGeneral;
import sistema.aplicacion.Pantalla;

public class pre_distributivo extends Pantalla {
	
	private Tabla tab_distributivo = new Tabla();
	private Calendario cal_fecha_inicial = new Calendario();

	@EJB
	private ServicioGeneral ser_general= (ServicioGeneral ) utilitario.instanciarEJB(ServicioGeneral.class);


	public pre_distributivo(){
		
		bar_botones.limpiar();
		
		bar_botones.agregarComponente(new Etiqueta("Seleccione Fecha para el Distributivo"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);
		// boton limpiar
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		
		
		Boton bot_filtrar = new Boton();
		bot_filtrar.setValue("Consultar Distributivo");
		bot_filtrar.setMetodo("actualizarDistributivo");
		bot_filtrar.setIcon("ui-icon-refresh");
		bar_botones.agregarBoton(bot_filtrar);
		
		tab_distributivo.setId("tab_distributivo");
		tab_distributivo.setHeader("DISTRIBUTIVO DE COLABORADORES");
		tab_distributivo.setSql(ser_general.getDistributivoColaboradores("1990-01-01"));
		tab_distributivo.getColumna("ide_gtemp").setVisible(false);
		tab_distributivo.getColumna("documento_identidad_gtemp").setLongitud(20);
		tab_distributivo.getColumna("codigo_partida_gepap").setLongitud(20);
		tab_distributivo.getColumna("fecha_salida").setLongitud(20);
		tab_distributivo.getColumna("lugar_trabajo").setLongitud(20);
		tab_distributivo.getColumna("genero").setLongitud(20);
		tab_distributivo.getColumna("formacion").setLongitud(20);
		tab_distributivo.getColumna("titulo").setLongitud(20);
		tab_distributivo.getColumna("area_conocimiento").setLongitud(20);
		tab_distributivo.getColumna("discapacitado").setLongitud(20);
		tab_distributivo.getColumna("detalle_gttds").setLongitud(20);
		tab_distributivo.getColumna("fecha_nacimiento_gtemp").setLongitud(20);
		tab_distributivo.getColumna("fecha_entrega_declaracion_gtdce").setLongitud(20);
		tab_distributivo.getColumna("fecha_proxima_declaracion_gtdce").setLongitud(20);
		tab_distributivo.setValueExpression("rowStyleClass", "fila.campos[" + tab_distributivo.getNumeroColumna("num_registro") + "] le 10 ? 'red' : null ");
		tab_distributivo.getColumna("num_registro").setNombreVisual("NRO.");
		tab_distributivo.getColumna("documento_identidad_gtemp").setNombreVisual("C�DULA");
		tab_distributivo.getColumna("empleado").setNombreVisual("NOMBRE");
		tab_distributivo.getColumna("codigo_partida_gepap").setNombreVisual("PARTIDA");
		tab_distributivo.getColumna("fecha_ingreso_gtemp").setNombreVisual("FECHA DE INGRESO");
		tab_distributivo.getColumna("fecha_salida").setNombreVisual("FECHA DE SALIDA");
		tab_distributivo.getColumna("titulo_cargo_gepgc").setNombreVisual("DENOMINACION DEL PUESTO");
		tab_distributivo.getColumna("proceso").setNombreVisual("PROCESO");
		tab_distributivo.getColumna("sub_proceso").setNombreVisual("SUB PROCESO");
		tab_distributivo.getColumna("grupo_ocupacional").setNombreVisual("GRUPO OCUPACIONAL");
		tab_distributivo.getColumna("rmu_geedp").setNombreVisual("RMU");
		tab_distributivo.getColumna("detalle_gttem").setNombreVisual("REGIMEN LABORAL");
		tab_distributivo.getColumna("detalle_gttco").setNombreVisual("MODALIDAD");
		tab_distributivo.getColumna("lugar_trabajo").setNombreVisual("LUGAR DE TRABAJO");
		tab_distributivo.getColumna("genero").setNombreVisual("G�NERO");
		tab_distributivo.getColumna("formacion").setNombreVisual("FORMACióN");
		tab_distributivo.getColumna("titulo").setNombreVisual("TITULO");
		tab_distributivo.getColumna("area_conocimiento").setNombreVisual("AREA DE CONOCIMIENTO");
		tab_distributivo.getColumna("discapacitado").setNombreVisual("DISCAPACIDAD");
		tab_distributivo.getColumna("detalle_gttds").setNombreVisual("TIPO DE DISCAPACIDAD");
		tab_distributivo.getColumna("fecha_nacimiento_gtemp").setNombreVisual("FECHA DE NACIMIENTO");
		tab_distributivo.getColumna("observacion_geedp").setNombreVisual("OBSERVACIONES");
		tab_distributivo.getColumna("fecha_entrega_declaracion_gtdce").setNombreVisual("FECHA ENTREGADA DECLARACióN");
		tab_distributivo.getColumna("fecha_proxima_declaracion_gtdce").setNombreVisual("FECHA PROXIMA DECLARACióN");
		tab_distributivo.getColumna("orden").setNombreVisual("TIPO");
		tab_distributivo.getColumna("num_registro").setFiltro(true);
		tab_distributivo.getColumna("documento_identidad_gtemp").setFiltro(true);
		tab_distributivo.getColumna("empleado").setFiltro(true);
		tab_distributivo.getColumna("codigo_partida_gepap").setFiltro(true);
		tab_distributivo.getColumna("fecha_ingreso_gtemp").setFiltro(true);
		tab_distributivo.getColumna("fecha_salida").setFiltro(true);
		tab_distributivo.getColumna("titulo_cargo_gepgc").setFiltro(true);
		tab_distributivo.getColumna("proceso").setFiltro(true);
		tab_distributivo.getColumna("sub_proceso").setFiltro(true);
		tab_distributivo.getColumna("grupo_ocupacional").setFiltro(true);
		tab_distributivo.getColumna("rmu_geedp").setFiltro(true);
		tab_distributivo.getColumna("detalle_gttem").setFiltro(true);
		tab_distributivo.getColumna("detalle_gttco").setFiltro(true);
		tab_distributivo.getColumna("lugar_trabajo").setFiltro(true);
		tab_distributivo.getColumna("genero").setFiltro(true);
		tab_distributivo.getColumna("formacion").setFiltro(true);
		tab_distributivo.getColumna("titulo").setFiltro(true);
		tab_distributivo.getColumna("area_conocimiento").setFiltro(true);
		tab_distributivo.getColumna("discapacitado").setFiltro(true);
		tab_distributivo.getColumna("detalle_gttds").setFiltro(true);
		tab_distributivo.getColumna("fecha_nacimiento_gtemp").setFiltro(true);
		tab_distributivo.getColumna("observacion_geedp").setFiltro(true);
		tab_distributivo.getColumna("fecha_entrega_declaracion_gtdce").setFiltro(true);
		tab_distributivo.getColumna("fecha_proxima_declaracion_gtdce").setFiltro(true);
		tab_distributivo.getColumna("orden").setFiltro(true);

		tab_distributivo.setLectura(true);
		tab_distributivo.setNumeroTabla(1);				
		tab_distributivo.setRows(20);
		tab_distributivo.dibujar();
		PanelTabla pat_distributivo = new PanelTabla();
		pat_distributivo.setPanelTabla(tab_distributivo);
		pat_distributivo.getMenuTabla().getItem_formato().setDisabled(true);

		Division div_distributivo = new Division();
		div_distributivo.setId("div_distributivo");
		div_distributivo.dividir1(pat_distributivo);
		agregarComponente(div_distributivo);
		
		
	}
	public void actualizarDistributivo(){
		tab_distributivo.setSql(ser_general.getDistributivoColaboradores(cal_fecha_inicial.getFecha()));
		tab_distributivo.ejecutarSql();
		
	}
	/**
	 * limpia toda la pantalla incluyendo el autocompletar
	 */
	public void limpiar() {
		tab_distributivo.limpiar();	
		utilitario.addUpdate("tab_distributivo");// limpia y refresca el autocompletar
	}
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_distributivo.insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_distributivo.guardar();
		guardarPantalla();		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_distributivo.eliminar();
	}
	public Tabla getTab_distributivo() {
		return tab_distributivo;
	}
	public void setTab_distributivo(Tabla tab_distributivo) {
		this.tab_distributivo = tab_distributivo;
	}
	
}
