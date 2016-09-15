package paq_presupuesto;

import framework.aplicacion.TablaGenerica;
import javax.ejb.EJB;

import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_contabilidad.ejb.ServicioContabilidad;
import sistema.aplicacion.Pantalla;

public class pre_sub_actividad extends Pantalla {
	private Tabla tab_sub_actividad=new Tabla();
	public static String par_sub_activdad;

	
	@EJB
	private ServicioContabilidad ser_contabilidad = (ServicioContabilidad ) utilitario.instanciarEJB(ServicioContabilidad.class);

	public pre_sub_actividad(){
		par_sub_activdad=utilitario.getVariable("p_modulo_secuencialsubactiv");
		
		tab_sub_actividad.setId("tab_sub_actividad");
		tab_sub_actividad.setNumeroTabla(1);
		tab_sub_actividad.setTabla("pre_sub_actividad", "ide_prsua", 1);
		//tab_sub_actividad.getColumna("codigo_prsua").setLectura(true);
		tab_sub_actividad.dibujar();
		PanelTabla pat_sub_actividad=new PanelTabla();
		pat_sub_actividad.setPanelTabla(tab_sub_actividad);
		
		agregarComponente(pat_sub_actividad);
		
	
	}
		
	
	
	

	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		tab_sub_actividad.insertar();
                TablaGenerica tab_secuencial = utilitario.consultar("select 1 as codigo,(case when max(cast (codigo_prsua as integer)) is null then 1 else max(cast (codigo_prsua as integer)) end) +1 as secuencial from pre_sub_actividad");
		tab_sub_actividad.setValor("codigo_prsua", tab_secuencial.getValor("secuencial"));
                utilitario.addUpdateTabla(tab_sub_actividad, "codigo_prsua", "");

	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
                /*
		if(tab_sub_actividad.isFilaInsertada()){
			tab_sub_actividad.setValor("codigo_prsua", ser_contabilidad.numeroSecuencial(par_sub_activdad));
			ser_contabilidad.guardaSecuencial(ser_contabilidad.numeroSecuencial(par_sub_activdad),par_sub_activdad);

		}
*/
		tab_sub_actividad.guardar();
		guardarPantalla();
		
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
		tab_sub_actividad.eliminar();
		
	}




	public Tabla getTab_sub_actividad() {
		return tab_sub_actividad;
	}




	public void setTab_sub_actividad(Tabla tab_sub_actividad) {
		this.tab_sub_actividad = tab_sub_actividad;
	}

}
