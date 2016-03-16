package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_tipo_compareciente extends Pantalla{
	
	private Tabla tab_tipo_compareciente= new Tabla();
	
	public pre_tipo_compareciente(){
		tab_tipo_compareciente.setId("tab_tipo_compareciente");
		tab_tipo_compareciente.setNumeroTabla(1);
		tab_tipo_compareciente.setTabla("pre_tipo_compareciente", "ide_prtio", 1);
		tab_tipo_compareciente.dibujar();
		
		PanelTabla pat_tipo_compareciente= new PanelTabla();
		pat_tipo_compareciente.setPanelTabla(tab_tipo_compareciente);
		
		agregarComponente(pat_tipo_compareciente);
		
	}
	
	public void insertar(){
		tab_tipo_compareciente.insertar();
	}
	
	public void guardar(){
		tab_tipo_compareciente.guardar();
		guardarPantalla();
	}
	
	public void eliminar(){
		tab_tipo_compareciente.eliminar();
	}

	
	public Tabla getTab_tipo_compareciente() {
		return tab_tipo_compareciente;
	}

	public void setTab_tipo_compareciente(Tabla tab_tipo_compareciente) {
		this.tab_tipo_compareciente = tab_tipo_compareciente;
	}

}
