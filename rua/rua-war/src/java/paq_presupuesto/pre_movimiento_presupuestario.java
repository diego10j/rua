package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_movimiento_presupuestario extends Pantalla {
	
	private Tabla tab_movimiento_presupuestario=new Tabla ();
	
	public pre_movimiento_presupuestario (){
		tab_movimiento_presupuestario.setId("tab_movimiento_presupuestario");
		tab_movimiento_presupuestario.setTabla("pre_movimiento_presupuestario", "ide_prmop", 1);
		tab_movimiento_presupuestario.setNumeroTabla(1);
		tab_movimiento_presupuestario.dibujar();
		PanelTabla pat_movimiento_presupuestario=new PanelTabla();
		pat_movimiento_presupuestario.setPanelTabla(tab_movimiento_presupuestario);
		agregarComponente(pat_movimiento_presupuestario);
		
		
		
	}
	
		
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_movimiento_presupuestario.insertar();
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_movimiento_presupuestario.guardar();
			guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_movimiento_presupuestario.eliminar();
		
	}




	public Tabla getTab_movimiento_presupuestario() {
		return tab_movimiento_presupuestario;
	}




	public void setTab_movimiento_presupuestario(Tabla tab_movimiento_presupuestario) {
		this.tab_movimiento_presupuestario = tab_movimiento_presupuestario;
	}

}
