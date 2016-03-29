package paq_general;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_tipo_distribucion_politica extends Pantalla{
	
	private Tabla tab_tipo_distribucion_politica=new Tabla();

	private Arbol arb_tipo_distribucion_politica=new Arbol();


	public pre_tipo_distribucion_politica(){



		tab_tipo_distribucion_politica.setId("tab_tipo_distribucion_politica");
		tab_tipo_distribucion_politica.setTabla("inst_tipo_distribucion_politica", "ide_indip", 1);
		
		tab_tipo_distribucion_politica.setCampoPadre("ins_ide_indip"); //necesarios para el arbol
		tab_tipo_distribucion_politica.setCampoNombre("detalle_indip"); //necesarios para el arbol
		tab_tipo_distribucion_politica.agregarArbol(arb_tipo_distribucion_politica);//necesarios para el arbol
		
		
		
		tab_tipo_distribucion_politica.dibujar();
		PanelTabla pat_ubica = new PanelTabla();
		pat_ubica.setPanelTabla(tab_tipo_distribucion_politica);

		arb_tipo_distribucion_politica.setId("arb_ubica");
		arb_tipo_distribucion_politica.dibujar();


		Division div_division=new Division();
		div_division.dividir2(arb_tipo_distribucion_politica, pat_ubica, "25%", "v");

		agregarComponente(div_division);

	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	public Tabla getTab_tipo_distribucion_politica() {
		return tab_tipo_distribucion_politica;
	}

	public void setTab_tipo_distribucion_politica(
			Tabla tab_tipo_distribucion_politica) {
		this.tab_tipo_distribucion_politica = tab_tipo_distribucion_politica;
	}

	public Arbol getArb_tipo_distribucion_politica() {
		return arb_tipo_distribucion_politica;
	}

	public void setArb_tipo_distribucion_politica(
			Arbol arb_tipo_distribucion_politica) {
		this.arb_tipo_distribucion_politica = arb_tipo_distribucion_politica;
	}

}
