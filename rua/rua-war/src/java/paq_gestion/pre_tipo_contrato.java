/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gestion;

import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;


public class pre_tipo_contrato extends Pantalla {

	private Division div_division = new Division();
	private Tabla tab_tabla1 = new Tabla();
	private Tabla tab_tabla2 = new Tabla();

	public pre_tipo_contrato() {        
		tab_tabla1.setId("tab_tabla1");
		tab_tabla1.setTabla("GTH_TIPO_CONTRATO", "IDE_GTTCO", 1);
		tab_tabla1.agregarRelacion(tab_tabla2);
		tab_tabla1.setCampoOrden("DETALLE_GTTCO");
		tab_tabla1.setCampoOrden("DETALLE_GTTCO");
		tab_tabla1.setValidarInsertar(true);
		tab_tabla1.getColumna("ACTIVO_GTTCO").setCheck();
		tab_tabla1.getColumna("ANTICIPO_GTTCO").setCheck();
		tab_tabla1.getColumna("ACTIVO_GTTCO").setValorDefecto("TRUE");
		
		tab_tabla1.getColumna("GARANTE_GTTCO").setCheck();
		tab_tabla1.getColumna("GARANTE_GTTCO").setValorDefecto("false");

		tab_tabla1.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla1);

		tab_tabla2.setId("tab_tabla2");
		tab_tabla2.setTabla("NRH_CONDICION_ANTICIPO", "IDE_NRCOA", 2);
		tab_tabla2.getColumna("ACTIVO_NRCOA").setCheck();
		tab_tabla2.getColumna("ACTIVO_NRCOA").setValorDefecto("TRUE");
		tab_tabla2.dibujar();
		PanelTabla pat_panel2 = new PanelTabla();
		pat_panel2.setPanelTabla(tab_tabla2);

		div_division.setId("div_division");
		div_division.dividir2(pat_panel,pat_panel2,"50%","H");
		agregarComponente(div_division);
	}

	@Override
	public void insertar() {
		if (tab_tabla1.isFocus()){
			tab_tabla1.insertar();
		}
		if (tab_tabla2.isFocus()){
			if (tab_tabla1.getValor("ANTICIPO_GTTCO").equals("true")){
				tab_tabla2.insertar();
			}else{
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe estar activo el campo anticipo del tipo de contrato");
			}
		}

	}

	@Override
	public void guardar() {
		if (tab_tabla1.guardar()){
			if (tab_tabla2.guardar()){
				int band=0;
				for (int i = 0; i <tab_tabla2.getTotalFilas(); i++) {
					if (tab_tabla2.getValor(i, "ACTIVO_NRCOA").equals("true")){
						band=band+1;
					}
				}
				if (tab_tabla2.getTotalFilas()==0){
					band=1;
				}
				if (band==1){
					guardarPantalla();
				}else{
					utilitario.agregarMensajeInfo("No se puede Guardar", "Debe existir una unica Condicion de Anticipo activa");
				}
			}
		}
	}

	@Override
	public void eliminar() {
		if (tab_tabla1.isFocus()){
			tab_tabla1.eliminar();
		}
		if (tab_tabla2.isFocus()){
			tab_tabla2.eliminar();
		}

	}

	public Tabla getTab_tabla1() {
		return tab_tabla1;
	}

	public void setTab_tabla1(Tabla tab_tabla1) {
		this.tab_tabla1 = tab_tabla1;
	}

	public Tabla getTab_tabla2() {
		return tab_tabla2;
	}

	public void setTab_tabla2(Tabla tab_tabla2) {
		this.tab_tabla2 = tab_tabla2;
	}

}
