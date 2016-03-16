package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_grupo_economico extends Pantalla {
	
	private Tabla tab_grupo_economico=new Tabla();
	
	
	public pre_grupo_economico (){
		tab_grupo_economico.setId("tab_grupo_economico");
		tab_grupo_economico.setTabla("pre_grupo_economico", "ide_prgre", 1);
		tab_grupo_economico.setNumeroTabla(1);
		tab_grupo_economico.dibujar();
		PanelTabla pat_grupo_economico=new PanelTabla();
		pat_grupo_economico.setPanelTabla(tab_grupo_economico);
		agregarComponente(pat_grupo_economico);
		
	}
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_grupo_economico.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_grupo_economico.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_grupo_economico.eliminar();
	}


	public Tabla getTab_grupo_economico() {
		return tab_grupo_economico;
	}


	public void setTab_grupo_economico(Tabla tab_grupo_economico) {
		this.tab_grupo_economico = tab_grupo_economico;
	}

}
