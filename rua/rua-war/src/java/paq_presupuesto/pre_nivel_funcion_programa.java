package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_nivel_funcion_programa extends Pantalla {
	private Tabla tab_nivel_funcion_programa= new Tabla();
	
	public pre_nivel_funcion_programa(){
		tab_nivel_funcion_programa.setId("tab_nivel_funcion_programa");
		tab_nivel_funcion_programa.setNumeroTabla(1);
		tab_nivel_funcion_programa.setTabla("pre_nivel_funcion_programa", "ide_prnfp", 1);
		tab_nivel_funcion_programa.dibujar();
		PanelTabla pat_nivel_funcion_programa=new PanelTabla();
		pat_nivel_funcion_programa.setPanelTabla(tab_nivel_funcion_programa);
		agregarComponente(pat_nivel_funcion_programa);
		
		
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_nivel_funcion_programa.insertar();
			
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_nivel_funcion_programa.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_nivel_funcion_programa.eliminar();
		
	}


	public Tabla getTab_nivel_funcion_programa() {
		return tab_nivel_funcion_programa;
	}


	public void setTab_nivel_funcion_programa(Tabla tab_nivel_funcion_programa) {
		this.tab_nivel_funcion_programa = tab_nivel_funcion_programa;
	}
	
	

}
