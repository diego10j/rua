package paq_general;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_tipo_distribucion extends Pantalla{

	private Tabla tab_tipo_distribucion=new Tabla();


	public pre_tipo_distribucion(){
		tab_tipo_distribucion.setId("tab_tipo_distribucion");
		tab_tipo_distribucion.setTabla("inst_tipo_distribucion","ide_intid", 1);
		tab_tipo_distribucion.dibujar();
		PanelTabla pat__tipo_distribucion=new PanelTabla();
		pat__tipo_distribucion.setPanelTabla(tab_tipo_distribucion);

		agregarComponente(tab_tipo_distribucion);
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_distribucion.inicializar();

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_distribucion.guardar();
		guardarPantalla();

	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_distribucion.eliminar();

	}

	public Tabla getTab_inst_tipo_distribucion() {
		return tab_tipo_distribucion;
	}

	public void setTab_inst_tipo_distribucion(Tabla tab_inst_tipo_distribucion) {
		this.tab_tipo_distribucion = tab_inst_tipo_distribucion;
	}

	public Tabla getTab_tipo_distribucion() {
		return tab_tipo_distribucion;
	}

	public void setTab_tipo_distribucion(Tabla tab_tipo_distribucion) {
		this.tab_tipo_distribucion = tab_tipo_distribucion;
	}

}
