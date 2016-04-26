/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_anio extends Pantalla {

	private Tabla tab_tabla = new Tabla();

	public pre_anio() {        
		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("GEN_ANIO", "IDE_GEANI", 1);
		tab_tabla.getColumna("ACTIVO_GEANI").setCheck();
		tab_tabla.getColumna("ACTIVO_GEANI").setValorDefecto("true");
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();        
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		int cont_activos=0;
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			if (tab_tabla.getValor(i, "ACTIVO_GEANI").equalsIgnoreCase("true")){
				cont_activos++;
			}
		}
		if (cont_activos==1){
			if (tab_tabla.guardar()){
				guardarPantalla();
			}
		}else{
			if (cont_activos==0){
				utilitario.agregarMensajeInfo("No se puede guardar","Debe existir un Año activo");
			}else{
				utilitario.agregarMensajeInfo("No se puede guardar","Debe existir solo un Año activo");
			}
		}
	}

	@Override
	public void eliminar() {
		tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}
}
