package paq_general;

import org.primefaces.event.NodeSelectEvent;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_division_politica extends Pantalla {
	private Tabla tab_tabla = new Tabla();	
	private Arbol arb_arbol = new Arbol();

	public pre_division_politica() {

		tab_tabla.setId("tab_tabla");
		tab_tabla.setTabla("GEN_DIVISION_POLITICA", "IDE_GEDIP", 1);
		tab_tabla.setCampoNombre("DETALLE_GEDIP"); // Para que se configure el arbol
		tab_tabla.setCampoPadre("GEN_IDE_GEDIP"); // Para que se configure el arbol
		tab_tabla.agregarArbol(arb_arbol); // Para que se configure el arbol
		tab_tabla.getColumna("IDE_GETDP").setCombo( "GEN_TIPO_DIVISION_POLITICA", "IDE_GETDP", "DETALLE_GETDP", "");
		tab_tabla.getColumna("IDE_GEREG").setCombo( "GEN_REGION", "IDE_GEREG", "DETALLE_GEREG", "");
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);
		arb_arbol.setId("arb_arbol");
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
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		tab_tabla.guardar();
		utilitario.getConexion().guardarPantalla();

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
