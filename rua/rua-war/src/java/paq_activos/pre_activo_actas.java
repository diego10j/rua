/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import paq_general.*;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.activos.ServicioActivosFijos;
import sistema.aplicacion.Pantalla;

public class pre_activo_actas extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	//	private Tabla tab_tabla3 = new Tabla();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	private SeleccionTabla set_sucursal = new SeleccionTabla();
    @EJB
    private ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);

	public pre_activo_actas() {



		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("act_activo_fijo", "ide_acafi", 1);
                tab_tabla1.getColumna("ide_aceaf").setCombo("act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf","");
                tab_tabla1.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi","");
                tab_tabla1.getColumna("ide_geubi").setAutoCompletar();
                tab_tabla1.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti","");
                tab_tabla1.getColumna("ide_inarti").setAutoCompletar();
                tab_tabla1.getColumna("ide_inarti").setFiltroContenido();
                tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper","");
                tab_tabla1.getColumna("ide_geper").setAutoCompletar();
                tab_tabla1.getColumna("ide_geper").setFiltroContenido();
                tab_tabla1.getColumna("ide_rheor").setCombo("reh_estruc_organi", "ide_rheor", "nombre_rheor","");
                tab_tabla1.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar","");
                tab_tabla1.getColumna("ide_inmar").setAutoCompletar();
                tab_tabla1.getColumna("ide_acuba").setCombo("act_ubicacion_activo", "ide_acuba", "nombre_acuba","");
                tab_tabla1.getColumna("ide_acuba").setAutoCompletar();
                tab_tabla1.getColumna("ide_gecas").setCombo("gen_casa", "ide_gecas", "nombre_gecas","");
                tab_tabla1.getColumna("ide_gecas").setAutoCompletar();
                tab_tabla1.getColumna("ide_geobr").setCombo("gen_obra", "ide_geobr", "nombre_geobr","");
                tab_tabla1.getColumna("ide_accla").setCombo("act_clase_activo","ide_accla","nombre_accla","");
                tab_tabla1.getColumna("ide_accls").setCombo("act_clasificacion", "ide_accls", "nombre_accls",""); 
                tab_tabla1.getColumna("ide_actac").setCombo("act_tipo_adquisicion", "ide_actac", "nombre_actac","");
                tab_tabla1.getColumna("observacion_acafi").setFiltroContenido();
                tab_tabla1.getColumna("codigo_barras_acafi").setFiltroContenido();
                tab_tabla1.getColumna("serie_acafi").setFiltroContenido();
                tab_tabla1.getColumna("numero_factu_acafi").setFiltroContenido();
                tab_tabla1.getColumna("modelo_acafi").setFiltroContenido();
                tab_tabla1.getColumna("ide_gecas").setFiltroContenido();
                tab_tabla1.getColumna("ide_accla").setFiltroContenido();
                tab_tabla1.getColumna("ide_inmar").setFiltroContenido();
                tab_tabla1.getColumna("ide_acuba").setFiltroContenido();
                tab_tabla1.getColumna("descripcion1_acafi").setVisible(false);
                tab_tabla1.getColumna("color_acafi").setVisible(false);
                tab_tabla1.getColumna("cuenta_ant_sistema").setVisible(false);
                tab_tabla1.getColumna("custodio_tmp").setVisible(false);
                tab_tabla1.getColumna("mediadas_acafi").setVisible(false);
                tab_tabla1.getColumna("credi_tribu_acafi").setVisible(false);
                tab_tabla1.getColumna("alterno_acafi").setVisible(false);
                tab_tabla1.getColumna("codigo_recu_acafi").setVisible(false);
                tab_tabla1.getColumna("anos_uso_acafi").setVisible(false);
                tab_tabla1.getColumna("valor_comercial_acafi").setVisible(false);
                tab_tabla1.getColumna("fo_acafi").setVisible(false);
                tab_tabla1.getColumna("ide_usua").setVisible(false);
                tab_tabla1.getColumna("fecha_fabrica_acafi").setVisible(false);
                tab_tabla1.getColumna("fd_acafi").setVisible(false);
                tab_tabla1.getColumna("act_ide_acafi").setVisible(false);
                tab_tabla1.getColumna("sec_masivo_acafi").setVisible(false);
                tab_tabla1.getColumna("ide_actac").setVisible(false);
                tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
                tab_tabla1.getColumna("valor_remate_acafi").setVisible(false);
                tab_tabla1.getColumna("ide_rheor").setVisible(false);
                tab_tabla1.getColumna("gen_ide_geper").setVisible(false);
                tab_tabla1.getColumna("nombre_acafi").setVisible(false);
                //tab_tabla1.getColumna("ide_cpcfa").setCombo("cxp_cabece_factur", "ide_cpcfa", "observacion_cpcfa","");
		tab_tabla1.agregarRelacion(tab_tabla2);
                tab_tabla1.setLectura(true);             
		tab_tabla1.dibujar();
                tab_tabla1.setRows(10);
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setSql(ser_activos.getSqlMovimientoActas(tab_tabla1.getValor(tab_tabla1.getFilaActual(), "ide_acafi"))); //calve primaria compuesta
                tab_tabla2.setLectura(true);   
                tab_tabla2.dibujar();
                tab_tabla2.setRows(10);
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);

		
		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		agregarComponente(div_division);


	}


	@Override
	public void abrirListaReportes() {
		// TODO Auto-generated method stub
		rep_reporte.dibujar();
	}
	Map p_parametros= new HashMap();
	public void aceptarReporte(){
		if (rep_reporte.getReporteSelecionado().equals("Detalle Areas y Departamentos")){
			if (rep_reporte.isVisible()){
				p_parametros=new HashMap();				
				rep_reporte.cerrar();				
				p_parametros.put("titulo", " AREAS Y DEPARTAMENTOS ");
				utilitario.addUpdate("rep_reporte,set_sucursal");
				sef_reporte.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());						
				sef_reporte.dibujar();
				utilitario.addUpdate("sef_reporte");
				//
			}}
	}

	public void cambioTipo(AjaxBehaviorEvent evt){
		tab_tabla2.modificar(evt);
		if(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("Hijo")){
			tab_tabla2.setValor("NIVEL_ORGANICO_GEDEP", "4");
			utilitario.addUpdateTabla(tab_tabla2,"NIVEL_ORGANICO_GEDEP", "");
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

	/**
	 * Valida que los datos ingrseados en departamentos sean correctos para que se puedan graficar 
	 */
	private String validar(){
		String str_mensaje="";

		if(tab_tabla2.isEmpty()==false && tab_tabla2.getValor("NIVEL_GEDEP").equals("0")){
			TablaGenerica tab_nodos = utilitario.consultar("SELECT * FROM GEN_DEPARTAMENTO WHERE ACTIVO_GEDEP=true AND NIVEL_GEDEP=0 order by NIVEL_GEDEP,TIPO_GEDEP,ORDEN_GEDEP");
			//Valido q solo exista un solo nodo raiz			
			if(!tab_nodos.isEmpty()){
				//Ya existe un nivel raiz no puede grabar otro
				if(tab_tabla2.isFilaInsertada()){
					str_mensaje="Ya existe un nivel 0, solo puede existir un nivel 0";
					return str_mensaje;
				}
			}
			if(tab_tabla2.getValor("NIVEL_GEDEP").equalsIgnoreCase("0")){
				if(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("CENTRO")){
					if(tab_tabla2.getValor("GEN_IDE_GEDEP")==null){
					}
					else{
						str_mensaje="El nivel 0 no puede tener valor en el campo padre";	
					}
				}
				else{
					str_mensaje="El nivel 0 debe tener como tipo Centro";            		  
				}
				if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP")==null || !tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("1")){
					str_mensaje="El nivel 0 debe tener como nivel organico Gerencia";
				}		
			}
			else if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("3") && !(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("IZQUIERDA") || tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("DERECHA"))){ //validaciones apoyo
				str_mensaje="El Nivel Org�nico de Apoyo debe ser de tipo Izquierda o Derecha";

			}
			else if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("4") && !tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("Hijo")){ //validaciones Division				
				str_mensaje="El Nivel Org�nico de División debe ser de tipo Hijo";
			}
			else if(tab_tabla2.getValor("TIPO_GEDEP").equalsIgnoreCase("Hijo")  ){ //validaciones Hijo
				if(tab_tabla2.getValor("NIVEL_ORGANICO_GEDEP").equalsIgnoreCase("4")){
					if(tab_tabla2.getValor("GEN_IDE_GEDEP")==null){
						str_mensaje="Cuando es de tipo Hijo debe tener un valor en el campo padre";
					}
					else{
						if(tab_tabla2.getValorSeleccionado()!=null){
							//Valida que el padre no sea el mismo que esta seleccionado
							if(tab_tabla2.getValor("GEN_IDE_GEDEP").equals(tab_tabla2.getValorSeleccionado())){
								str_mensaje="El valor en el campo padre no puede ser el registro seleccionado";	
							}	
						}

					}

				}
				else{
					str_mensaje="El tipo Hijo debe ser de Nivel Org�nico División";
				}
			}
		}
		return str_mensaje;
	}




	@Override
	public void insertar() {
		if (tab_tabla1.isFocus()) {
			tab_tabla1.insertar();
			tab_tabla2.limpiar();
			//			tab_tabla3.limpiar();
		} else if (tab_tabla2.isFocus()) {
			//Inserta solo si la fila seleccionada de la tabla de areas tiene una clave primaria 
			if (tab_tabla1.isFilaInsertada() == false && tab_tabla1.isEmpty() == false) {
				tab_tabla2.getColumna("IDE_GEARE").setValorDefecto(tab_tabla1.getValor("IDE_GEARE"));
				tab_tabla2.insertar();
			} else {
				utilitario.agregarMensajeInfo("No se puede insertar ", "Debe guardar la tabla de �reas ");
			}
		} 
		//		else if (tab_tabla3.isFocus()) {
		//			//Inserta solo si la fila seleccionada de la tabla de departamentos tiene una clave primaria 
		//			if (tab_tabla2.isFilaInsertada() == false && tab_tabla2.isEmpty() == false) {				
		//				tab_tabla3.insertar();
		//				tab_tabla3.setValor("IDE_GEARE",tab_tabla1.getValor("IDE_GEARE"));
		//				tab_tabla3.setValor("IDE_GEDEP",tab_tabla2.getValor("IDE_GEDEP"));
		//			} else {
		//				utilitario.agregarMensajeInfo("No se puede insertar ", "Debe guardar la tabla de Departamentos ");
		//			}
		//		}
	}

	/**
	 * Filtra sucursales y departamentos del area seleccionada *
	 */
	private void filtrosTabla1() {
		tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
		filtrarSucursales();
		//		tab_tabla3.ejecutarSql();
	}

	/**
	 * Se ejecuta cuando se selecciona una fila de la tabla1 de Areas
	 *
	 * @param evt
	 */
	public void seleccionarTabla1(SelectEvent evt) {
		tab_tabla1.seleccionarFila(evt);
		filtrosTabla1();
	}

	/**
	 * Se ejecuta cuando un componente de la tabla1 de cualquier fila gana el
	 * foco
	 *
	 * @param evt
	 */
	public void seleccionarTabla1(AjaxBehaviorEvent evt) {
		tab_tabla1.seleccionarFila(evt);
		filtrosTabla1();
	}

	/**
	 * Filtra las sucursales del departamento seleccionado
	 */
	private void filtrosTabla2() {
		filtrarSucursales();
		//		tab_tabla3.ejecutarSql();
	}

	/**
	 * Se ejecuta cuando se selecciona una fila de la tabla2
	 *
	 * @param evt
	 */
	public void seleccionarTabla2(SelectEvent evt) {
		tab_tabla2.seleccionarFila(evt);
		filtrosTabla2();
	}

	/**
	 * Se ejecuta cuando un componente de la tabla2 de cualquier fila gana el
	 * foco
	 *
	 * @param evt
	 */
	public void seleccionarTabla2(AjaxBehaviorEvent evt) {
		tab_tabla2.seleccionarFila(evt);
		filtrosTabla2();
	}

	@Override
	public void guardar() {
		if (tab_tabla1.guardar()) {			
			String str_mensaje=validar();			
			if(!str_mensaje.isEmpty()){
				utilitario.agregarMensajeError("No se puede guardar", str_mensaje);
				return;
			}						
			tab_tabla2.guardar();
		}
		guardarPantalla();
}

	@Override
	public void eliminar() {
		if (tab_tabla1.isFocus()) {
			if (tab_tabla1.eliminar()) {
				//si es que si elimina actualiza la nueva fila seleccionada
				filtrarSucursales();
				//				tab_tabla3.ejecutarSql();
			}

		} else if (tab_tabla2.isFocus()) {
			if (tab_tabla2.eliminar()) {
				//si es que si elimina actualiza la nueva fila seleccionada
				filtrarSucursales();
				//				tab_tabla3.ejecutarSql();
			}
		} 
		//		else if (tab_tabla3.isFocus()) {
		//			tab_tabla3.eliminar();
		//		}

	}

	@Override
	public void inicio() {
		// TODO Auto-generated method stub
		super.inicio();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void siguiente() {
		// TODO Auto-generated method stub
		super.siguiente();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void atras() {
		// TODO Auto-generated method stub
		super.atras();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void fin() {
		// TODO Auto-generated method stub
		super.fin();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	@Override
	public void actualizar() {
		// TODO Auto-generated method stub
		super.actualizar();
		if (tab_tabla1.isFocus() || tab_tabla2.isFocus()) {
			filtrosTabla2();
		}
	}

	/**
	 * Filtra las sucursales por departamento y �rea seleccionada
	 */
	private void filtrarSucursales() {
		String str_area = tab_tabla1.getValor("IDE_GEARE");
		if (str_area == null) {
			str_area = "-1";
		}

		String str_departamento = tab_tabla2.getValor("IDE_GEDEP");
		if (str_departamento == null) {
			str_departamento = "-1";
		}

		//		tab_tabla3.setCondicion("IDE_GEARE =" + str_area + " AND IDE_GEDEP=" + str_departamento);
		tab_tabla2.imprimirSql();
	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}

	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}

	//	public Tabla getTab_tabla3() {
	//		return tab_tabla3;
	//	}
	//
	//	public void setTab_tabla3(Tabla tab_tabla3) {
	//		this.tab_tabla3 = tab_tabla3;
	//	}

	public SeleccionTabla getSet_sucursal() {
		return set_sucursal;
	}

	public void setSet_sucursal(SeleccionTabla set_sucursal) {
		this.set_sucursal = set_sucursal;
	}

}
