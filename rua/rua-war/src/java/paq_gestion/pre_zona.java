/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;
//eds
import org.primefaces.component.tabview.TabViewRenderer;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
/**
////
 * @author Diego
 */
public class pre_zona extends Pantalla {

	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();


	public pre_zona() {
		
		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("GTH_ZONA_VIATICO", "IDE_GTZOV", 1);
		tab_tabla1.getColumna("ACTIVO_GTZOV").setCheck();
		tab_tabla1.getColumna("ACTIVO_GTZOV").setValorDefecto("FALSE");
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla1);

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir1(pat_panel1);
		agregarComponente(div_division);
	}

	@Override
	public void insertar() {
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		tab_tabla1.guardar();
		tab_tabla2.guardar();
		guardarPantalla();
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
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
}
