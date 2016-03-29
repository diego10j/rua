/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_general;

import org.primefaces.event.NodeSelectEvent;

import sistema.aplicacion.Pantalla;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_departamentos extends Pantalla {
    
    private Tabla tab_tabla = new Tabla();
    private Arbol arb_arbol=new Arbol();

    public pre_departamentos() {        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_DEPARTAMENTO", "IDE_GEDEP", 1);
        tab_tabla.getColumna("ACTIVO_GEDEP").setCheck();
        tab_tabla.getColumna("ACTIVO_GEDEP").setValorDefecto("true");
        tab_tabla.getColumna("IDE_GEARE").setVisible(false);
        tab_tabla.getColumna("TIPO_GEDEP").setVisible(false);
        tab_tabla.getColumna("NIVEL_GEDEP").setVisible(false);
        tab_tabla.getColumna("POSICION_HIJOS_GEDEP").setVisible(false);
        tab_tabla.getColumna("ORDEN_GEDEP").setVisible(false);
        tab_tabla.getColumna("NIVEL_ORGANICO_GEDEP").setVisible(false);        
        tab_tabla.setCampoNombre("DETALLE_GEDEP"); 
        tab_tabla.setCampoPadre("GEN_IDE_GEDEP"); 
        tab_tabla.agregarArbol(arb_arbol); 
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        arb_arbol.setId("arb_arbol");
        arb_arbol.onSelect("seleccionar_arbol");
        arb_arbol.dibujar();
        
        Division div_division = new Division();        
        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, pat_panel, "21%", "V");
        agregarComponente(div_division);
    }
    
    public void seleccionar_arbol(NodeSelectEvent evt) {
        arb_arbol.seleccionarNodo(evt);
        tab_tabla.setValorPadre(arb_arbol.getValorSeleccionado());
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
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

	public Arbol getArb_arbol() {
		return arb_arbol;
	}

	public void setArb_arbol(Arbol arb_arbol) {
		this.arb_arbol = arb_arbol;
	}    
}
