package paq_presupuesto;

import framework.componentes.Combo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_poa_financiamiento extends Pantalla {
	private Tabla tab_poa_financiamiento=new Tabla();
	private Combo com_poa=new Combo();

	public pre_poa_financiamiento(){
		tab_poa_financiamiento.setId("tab_poa_financiamiento");
		tab_poa_financiamiento.setNumeroTabla(1);
		tab_poa_financiamiento.setTabla("pre_poa_financiamiento", "ide_prpof", 1);
		tab_poa_financiamiento.getColumna("ide_coest").setCombo("cont_estado","ide_coest","detalle_coest","");
		tab_poa_financiamiento.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento", "ide_prfuf","detalle_prfuf","");
		
		
		tab_poa_financiamiento.dibujar();
		PanelTabla pat_poa_financiamiento=new PanelTabla();
		pat_poa_financiamiento.setPanelTabla(tab_poa_financiamiento);
		
		agregarComponente(pat_poa_financiamiento);
		
	}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_poa_financiamiento.insertar();
		
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_poa_financiamiento.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_poa_financiamiento.eliminar();
		
	}

	public Tabla getTab_poa_financiamiento() {
		return tab_poa_financiamiento;
	}

	public void setTab_poa_financiamiento(Tabla tab_poa_financiamiento) {
		this.tab_poa_financiamiento = tab_poa_financiamiento;
	}
	

}
