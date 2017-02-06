package paq_sri;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_beneficio_tributario_disc extends Pantalla {

	private Tabla tab_ben_trib_disc=new Tabla();
	
	public pre_beneficio_tributario_disc(){
		
		tab_ben_trib_disc.setId("tab_ben_trib_disc");
		tab_ben_trib_disc.setTabla("SRI_BENEFICIO_TRIBUTARIO_DISC", "IDE_SRBTD",1);
		tab_ben_trib_disc.dibujar();
		
		PanelTabla pat_tab1=new PanelTabla();
		pat_tab1.setPanelTabla(tab_ben_trib_disc);
		
		Division div=new Division();
		div.dividir1(pat_tab1);
		
		agregarComponente(div);
		
	}
	
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().insertar();
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		utilitario.getTablaisFocus().eliminar();
	}

	public Tabla getTab_ben_trib_disc() {
		return tab_ben_trib_disc;
	}

	public void setTab_ben_trib_disc(Tabla tab_ben_trib_disc) {
		this.tab_ben_trib_disc = tab_ben_trib_disc;
	}

	
}
