/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

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

import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

public class pre_area_departamento extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	//	private Tabla tab_tabla3 = new Tabla();
	private Reporte rep_reporte = new Reporte();
	private SeleccionFormatoReporte sef_reporte = new SeleccionFormatoReporte();
	private SeleccionTabla set_sucursal = new SeleccionTabla();

	public pre_area_departamento() {
		rep_reporte.setId("rep_reporte");
		rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
		bar_botones.agregarReporte();


		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("GEN_AREA", "IDE_GEARE", 1);
		tab_tabla1.getColumna("ACTIVO_GEARE").setCheck();

		tab_tabla1.onSelect("seleccionarTabla1");
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("�REAS");
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("GEN_DEPARTAMENTO", "IDE_GEDEP", 2); //calve primaria compuesta
		tab_tabla2.getColumna("IDE_GEDEP").setEtiqueta();
		tab_tabla2.getColumna("IDE_GEARE").setVisible(false);
		tab_tabla2.getColumna("ACTIVO_GEDEP").setCheck();
		tab_tabla2.getColumna("GEN_IDE_GEDEP").setCombo("GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP", "");
		tab_tabla2.getColumna("GEN_IDE_GEDEP").setLongitud(150);
		tab_tabla2.getColumna("GEN_IDE_GEDEP").setAutoCompletar();

		List< Object[]> lis_organico = new ArrayList<Object[]>();

		lis_organico.add(new Object[]{"2", "�rea"});		
		lis_organico.add(new Object[]{"4", "Apoyo"});
		lis_organico.add(new Object[]{"3", "Division"});
		lis_organico.add(new Object[]{"1", "Gerencia"});		
		tab_tabla2.getColumna("NIVEL_ORGANICO_GEDEP").setCombo(lis_organico);
		tab_tabla2.getColumna("NIVEL_ORGANICO_GEDEP").setPermitirNullCombo(false);
		tab_tabla2.getColumna("NIVEL_GEDEP").setRequerida(true);		

		tab_tabla2.setValidarInsertar(true);//Para que solo inserte de una en una        
		tab_tabla2.onSelect("seleccionarTabla2");

		List< Object[]> lis_tipos = new ArrayList<Object[]>();
		lis_tipos.add(new Object[]{"Centro", "Centro"});
		lis_tipos.add(new Object[]{"Derecha", "Derecha"});
		lis_tipos.add(new Object[]{"Izquierda", "Izquierda"});
		lis_tipos.add(new Object[]{"Nodo", "Nodo"});
		tab_tabla2.getColumna("TIPO_GEDEP").setCombo(lis_tipos);
		tab_tabla2.getColumna("TIPO_GEDEP").setPermitirNullCombo(false);
		tab_tabla2.getColumna("TIPO_GEDEP").setMetodoChange("cambioTipo");


		List< Object[]> lis_posicion = new ArrayList<Object[]>();
		lis_posicion.add(new Object[]{"Vertical", "Vertical"});
		lis_posicion.add(new Object[]{"Horizontal", "Horizontal"});
		tab_tabla2.getColumna("POSICION_HIJOS_GEDEP").setCombo(lis_posicion);
		tab_tabla2.getColumna("POSICION_HIJOS_GEDEP").setPermitirNullCombo(false);

		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("DEPARTAMENTOS POR AREAS");
		pat_panel2.setPanelTabla(tab_tabla2);

		//		tab_tabla3.setId("tab_tabla3");
		//		tab_tabla3.setGenerarPrimaria(false);
		//		tab_tabla3.setTabla("GEN_DEPARTAMENTO_SUCURSAL", "IDE_SUCU,IDE_GEDEP,IDE_GEARE", 3);//clave primaria compuesta
		//		tab_tabla3.setMostrarcampoSucursal(true);
		//		tab_tabla3.setValidarInsertar(true);//Para que solo inserte de una en una
		//		tab_tabla3.getColumna("ide_sucu").setCombo("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU", "");
		//		tab_tabla3.getColumna("ide_gedep").setVisible(false);
		//		tab_tabla3.getColumna("ide_geare").setVisible(false);
		//		filtrarSucursales();
		//		tab_tabla3.dibujar();
		//		PanelTabla pat_panel3 = new PanelTabla();
		//		pat_panel3.setMensajeWarn("SUCURSALES POR DEPARTAMENTOS ");
		//		pat_panel3.setPanelTabla(tab_tabla3);

		//		Division div_vertical = new Division();
		//		div_vertical.dividir2(pat_panel2, pat_panel3, "65%", "V");

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
		agregarComponente(div_division);


		sef_reporte.setId("sef_reporte");
		agregarComponente(rep_reporte);
		agregarComponente(sef_reporte);
		set_sucursal.setId("set_sucursal");
		set_sucursal.setSeleccionTabla("SIS_SUCURSAL", "IDE_SUCU", "NOM_SUCU");
		set_sucursal.getBot_aceptar().setMetodo("aceptarReporte");
		agregarComponente(set_sucursal);

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
		if(guardarPantalla().isEmpty()){
			tab_tabla2.actualizarCombos();
		}
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
