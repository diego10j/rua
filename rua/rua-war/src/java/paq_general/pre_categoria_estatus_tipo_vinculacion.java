package paq_general;

import org.primefaces.event.SelectEvent;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;

public class pre_categoria_estatus_tipo_vinculacion extends Pantalla {
	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();
	

	public pre_categoria_estatus_tipo_vinculacion() {
		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("GEN_CATEGORIA_ESTATUS", "IDE_GECAE", 1);
		tab_tabla1.getColumna("ACTIVO_GECAE").setCheck();
		tab_tabla1.getColumna("ACTIVO_GECAE").setValorDefecto("TRUE");
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.dibujar();
		PanelTabla pat_panel1 = new PanelTabla();
		pat_panel1.setMensajeWarn("CATEGORIA ESTATUS");
		pat_panel1.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("GEN_TIPO_VINCULACION", "IDE_GETIV", 2);
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setMensajeWarn("TIPO DE VINCULAC�N");
		pat_panel2.setPanelTabla(tab_tabla2);
	    Division div_division = new Division();
		div_division.setId("div_division");
		div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");

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
		utilitario.getConexion().guardarPantalla();
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
