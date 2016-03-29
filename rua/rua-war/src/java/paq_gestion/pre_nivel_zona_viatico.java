/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

/**
 *
 * @author Diego
 */
public class pre_nivel_zona_viatico extends Pantalla {


	private Tabla tab_tabla = new Tabla();
	private Tabla tab_nivel = new Tabla();

	public pre_nivel_zona_viatico() {

		tab_nivel.setId("tab_nivel");
		tab_nivel.setTabla("GTH_NIVEL_VIATICO", "IDE_GTNIV", 1);
		tab_nivel.getColumna("ACTIVO_GTNIV").setCheck();
		tab_nivel.getColumna("ACTIVO_GTNIV").setValorDefecto("true");
		tab_nivel.onSelect("seleccionaTablaNivel");
		tab_nivel.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_nivel);
		pat_panel.setMensajeWarn("NIVELES VIATICOS");


		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("GTH_NIVEL_ZONA_VIATICO", "IDE_GTNZV", 2);
		//tab_tabla.getColumna("IDE_GTNIV").setCombo("GTH_NIVEL_VIATICO", "IDE_GTNIV", "DETALLE_GTNIV", "");
		tab_tabla.getColumna("IDE_GTNIV").setVisible(false);
		tab_tabla.getColumna("IDE_GTZOV").setCombo("GTH_ZONA_VIATICO", "IDE_GTZOV", "DETALLE_GTZOV", "");
		tab_tabla.getColumna("ACTIVO_GTNZV").setCheck();
		tab_tabla.getColumna("ACTIVO_GTNZV").setValorDefecto("TRUE");

		List lista = new ArrayList();
		Object fila1[] = {
				"0", "INTERIOR"
		};
		Object fila2[] = {
				"1", "EXTERIOR"
		};
		lista.add(fila1);
		lista.add(fila2);

		tab_tabla.getColumna("INTERIOR_EXTERIOR_GTNZV").setRadio(lista,"");
		tab_tabla.getColumna("INTERIOR_EXTERIOR_GTNZV").setValorDefecto("0");

		tab_tabla.setCondicion("IDE_GTNIV = "+tab_nivel.getValorSeleccionado());
		tab_tabla.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setPanelTabla(tab_tabla);
		pat_panel1.setMensajeWarn("NIVEL ZONA VIATICOS");

		Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel,pat_panel1,"40%","H");
		agregarComponente(div_division);
	}


	public void seleccionaTablaNivel(SelectEvent evt){
		tab_nivel.seleccionarFila(evt);
		tab_tabla.setCondicion("IDE_GTNIV = "+tab_nivel.getValorSeleccionado());
		tab_tabla.ejecutarSql();
	}

	public void seleccionaTablaNivel(AjaxBehaviorEvent evt){
		tab_nivel.seleccionarFila(evt);
		tab_tabla.setCondicion("IDE_GTNIV = "+tab_nivel.getValorSeleccionado());
		tab_tabla.ejecutarSql();
	}

	@Override
	public void insertar() {
		if (tab_nivel.isFocus()){
			tab_nivel.insertar();
		}
		else if (tab_tabla.isFocus()){
			if (tab_nivel.getTotalFilas()>0){
				tab_tabla.insertar();
				tab_tabla.setValor("IDE_GTNIV", tab_nivel.getValorSeleccionado());
			}
		}
	}

	@Override
	public void guardar() {

		if (tab_nivel.guardar()){
			if (tab_tabla.guardar()){
				guardarPantalla();			}
		}
	}

	@Override
	public void eliminar() {
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}

	public Tabla getTab_nivel() {
		return tab_nivel;
	}

	public void setTab_nivel(Tabla tab_nivel) {
		this.tab_nivel = tab_nivel;
	}

}
