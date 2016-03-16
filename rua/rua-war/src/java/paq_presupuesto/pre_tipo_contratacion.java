package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_tipo_contratacion extends Pantalla{
	private Tabla tab_tipo_contratacion=new Tabla();
	
	public pre_tipo_contratacion(){
		tab_tipo_contratacion.setId("tab_tipo_contratacion");
		tab_tipo_contratacion.setNumeroTabla(1);
		tab_tipo_contratacion.setTabla("pre_tipo_contratacion", "ide_prtpc", 1);
		tab_tipo_contratacion.dibujar();
		PanelTabla pat_tipo_contratacion=new PanelTabla();
		pat_tipo_contratacion.setPanelTabla(tab_tipo_contratacion);
		
		agregarComponente(pat_tipo_contratacion);
		
	
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_tipo_contratacion.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_tipo_contratacion.guardar();
		guardarPantalla();
		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_tipo_contratacion.eliminar();
		
	}


	public Tabla getTab_tipo_contratacion() {
		return tab_tipo_contratacion;
	}


	public void setTab_tipo_contratacion(Tabla tab_tipo_contratacion) {
		this.tab_tipo_contratacion = tab_tipo_contratacion;
	}


}
