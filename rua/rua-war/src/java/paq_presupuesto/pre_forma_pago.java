package paq_presupuesto;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_forma_pago extends Pantalla{
	private Tabla tab_forma_pago=new Tabla();
	
	public pre_forma_pago(){
		tab_forma_pago.setId("tab_forma_pago");
		tab_forma_pago.setNumeroTabla(1);
		tab_forma_pago.setTabla("pre_forma_pago", "ide_prfop", 1);
		tab_forma_pago.dibujar();
		PanelTabla pat_forma_pago=new PanelTabla();
		pat_forma_pago.setPanelTabla(tab_forma_pago);
		
		agregarComponente(pat_forma_pago);
		
	
	}
	

	public Tabla getTab_forma_pago() {
		return tab_forma_pago;
	}


	public void setTab_forma_pago(Tabla tab_forma_pago) {
		this.tab_forma_pago = tab_forma_pago;
	}


	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_forma_pago.insertar();
			
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		tab_forma_pago.guardar();
		guardarPantalla();
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		tab_forma_pago.eliminar();
		
	}


}
