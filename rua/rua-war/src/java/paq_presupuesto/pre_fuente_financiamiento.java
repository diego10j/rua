package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_fuente_financiamiento extends Pantalla {
	private Tabla tab_fuente_financiamiento=new Tabla();
	 public pre_fuente_financiamiento() {
		// TODO Auto-generated constructor stub
		 
		 tab_fuente_financiamiento.setId("tab_fuente_financiamiento");
		 tab_fuente_financiamiento.setNumeroTabla(1);
		 tab_fuente_financiamiento.setTabla("pre_fuente_financiamiento", "ide_prfuf", 1);
		 tab_fuente_financiamiento.dibujar();
		 PanelTabla pat_fuente_financiamiento=new PanelTabla();
		 pat_fuente_financiamiento.setPanelTabla(tab_fuente_financiamiento);
		 
		 agregarComponente(pat_fuente_financiamiento);
		  
		  
		
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_fuente_financiamiento.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_fuente_financiamiento.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_fuente_financiamiento.eliminar();
		
	}


	public Tabla getTab_fuente_financiamiento() {
		return tab_fuente_financiamiento;
	}


	public void setTab_fuente_financiamiento(Tabla tab_fuente_financiamiento) {
		this.tab_fuente_financiamiento = tab_fuente_financiamiento;
	}
	

}
