package paq_general;

import org.primefaces.event.NodeSelectEvent;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_cargo_funcional extends Pantalla {
	private Tabla tab_tabla = new Tabla();
	

	public pre_cargo_funcional() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("GEN_CARGO_FUNCIONAL", "IDE_GECAF", 1);
        tab_tabla.getColumna("ACTIVO_GECAF").setCheck();
        tab_tabla.getColumna("ACTIVO_GECAF").setValorDefecto("true");
        tab_tabla.getColumna("DETALLE_GECAF").setRequerida(true);
        tab_tabla.getColumna("SIGLAS_GECAF").setRequerida(false);
        tab_tabla.getColumna("PRINCIPAL_SECUNDARIO_gecaf").setCheck();
        tab_tabla.getColumna("PRINCIPAL_SECUNDARIO_GECAF").setValorDefecto("false");
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setMensajeWarn("CARGO FUNCIONAL");
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

}
