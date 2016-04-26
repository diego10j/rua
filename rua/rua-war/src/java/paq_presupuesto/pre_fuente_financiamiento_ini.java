package paq_presupuesto;

import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import sistema.aplicacion.Pantalla;

public class pre_fuente_financiamiento_ini extends Pantalla {
	private Tabla tab_fuente_financiamiento=new Tabla();
	private Combo com_anio=new Combo();

	 public pre_fuente_financiamiento_ini() {
		// TODO Auto-generated constructor stub
		 
			com_anio.setCombo("select ide_geani,nom_geani from gen_anio order by nom_geani");
			com_anio.setMetodo("filtrarAnio");
			//com_anio.setMetodo("seleccionaElAnio");
			bar_botones.agregarComponente(new Etiqueta("Seleccione El Año:"));
			bar_botones.agregarComponente(com_anio);
		 
		 tab_fuente_financiamiento.setId("tab_fuente_financiamiento");
		 tab_fuente_financiamiento.setTabla("pre_fuente_financiamiento_ini", "ide_prffi", 1);
		 tab_fuente_financiamiento.setCondicion("ide_prffi=-1");
		 tab_fuente_financiamiento.getColumna("ide_geani").setVisible(false);
		 tab_fuente_financiamiento.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
		 tab_fuente_financiamiento.dibujar();
		 PanelTabla pat_fuente_financiamiento=new PanelTabla();
		 pat_fuente_financiamiento.setPanelTabla(tab_fuente_financiamiento);
		 
		 agregarComponente(pat_fuente_financiamiento);
		  
		  
		
	}
		public void filtrarAnio(){
			System.out.println("entre 0");

			if(com_anio.getValue()!=null){
				tab_fuente_financiamiento.setCondicion("ide_geani="+com_anio.getValue().toString());
			}
			else{
				tab_fuente_financiamiento.setCondicion("ide_geani=-1");
			}
			tab_fuente_financiamiento.ejecutarSql();		
			tab_fuente_financiamiento.imprimirSql();
			utilitario.addUpdate("tab_fuente_financiamiento");


		}

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if(com_anio.getValue()==null){
			utilitario.agregarMensaje("No se puede insertar", "Debe Seleccionar un Año");
			return;

		}
		else if (tab_fuente_financiamiento.isFocus()){
			tab_fuente_financiamiento.insertar();
			tab_fuente_financiamiento.setValor("ide_geani", com_anio.getValue()+"");

		}
		
		
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
