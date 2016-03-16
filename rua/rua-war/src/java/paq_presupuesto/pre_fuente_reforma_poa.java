/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package paq_presupuesto;

import javax.ejb.EJB;

import paq_presupuesto.ejb.ServicioPresupuesto;
import sistema.aplicacion.Pantalla;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionCalendario;
import framework.componentes.Tabla;

public class pre_fuente_reforma_poa extends Pantalla {

	private Tabla tab_tabla = new Tabla();
	@EJB
	 private ServicioPresupuesto ser_presupuesto=(ServicioPresupuesto)utilitario.instanciarEJB(ServicioPresupuesto.class);
	private Calendario cal_fecha_inicial = new Calendario();
	private Calendario cal_fecha_final = new Calendario();
	
	
	private SeleccionCalendario sec_filtrar=new SeleccionCalendario();
	
	public pre_fuente_reforma_poa() {  
		

		
		Boton bot_importar = new Boton();
		bot_importar.setValue("Filtrar");
		bot_importar.setMetodo("filtrar");
		bot_importar.setIcon("ui-icon-clock");
			bar_botones.agregarComponente(bot_importar);
		
		bar_botones.agregarComponente(new Etiqueta("Fecha Inicial :"));
		cal_fecha_inicial.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_inicial);

		bar_botones.agregarComponente(new Etiqueta("Fecha Final :"));
		cal_fecha_final.setFechaActual();
		bar_botones.agregarComponente(cal_fecha_final);

		
		tab_tabla.setId("tab_tabla");
tab_tabla.setSql("select ide_prprf,ide_prfuf,ide_prpoa,valor_reformado_prprf," +
		"resolucion_prprf,fecha_prprf,saldo_actual_prprf,activo_prprf, " +
		"usuario_ingre as ing_usua,fecha_ingre as ing_fec,hora_ingre as ing_hora,usuario_actua as actual_usua,fecha_actua as actual_fecha, " +
		"hora_actua as actual_hora from pre_poa_reforma_fuente " +
		"where ide_prprf=-1");




tab_tabla.getColumna("ide_prprf").setVisible(true);
tab_tabla.getColumna("ide_prprf").setLectura(true);
tab_tabla.getColumna("ide_prprf").setNombreVisual("Codigo");

tab_tabla.getColumna("ide_prfuf").setVisible(true);
tab_tabla.getColumna("ide_prfuf").setCombo("pre_fuente_financiamiento","ide_prfuf","detalle_prfuf","");
tab_tabla.getColumna("ide_prfuf").setAutoCompletar();
tab_tabla.getColumna("ide_prfuf").setNombreVisual("Fuente Financiamiento");
tab_tabla.getColumna("ide_prfuf").setLectura(true);

tab_tabla.getColumna("ide_prpoa").setVisible(true);
tab_tabla.getColumna("ide_prpoa").setNombreVisual("POA");
tab_tabla.getColumna("ide_prpoa").setCombo(ser_presupuesto.getPoaNombre("select ide_geani from gen_anio"));
tab_tabla.getColumna("ide_prpoa").setAutoCompletar();
tab_tabla.getColumna("ide_prpoa").setLongitud(300);
tab_tabla.getColumna("ide_prpoa").setLectura(true);


tab_tabla.getColumna("valor_reformado_prprf").setVisible(true);
tab_tabla.getColumna("valor_reformado_prprf").setNombreVisual("Valor Reformado");
tab_tabla.getColumna("valor_reformado_prprf").setLectura(true);


tab_tabla.getColumna("resolucion_prprf").setVisible(true);
tab_tabla.getColumna("resolucion_prprf").setNombreVisual("Resolucion");
tab_tabla.getColumna("resolucion_prprf").setLectura(true);



tab_tabla.getColumna("fecha_prprf").setVisible(true);
tab_tabla.getColumna("fecha_prprf").setNombreVisual("Fecha");
tab_tabla.getColumna("fecha_prprf").setLectura(true);



tab_tabla.getColumna("saldo_actual_prprf").setVisible(true);
tab_tabla.getColumna("saldo_actual_prprf").setNombreVisual("Saldo Actual");
tab_tabla.getColumna("saldo_actual_prprf").setLectura(true);


tab_tabla.getColumna("activo_prprf").setVisible(true);
tab_tabla.getColumna("activo_prprf").setNombreVisual("Activo");
tab_tabla.getColumna("activo_prprf").setLectura(true);



		tab_tabla.getColumna("ing_usua").setVisible(true);
		tab_tabla.getColumna("ing_usua").setNombreVisual("Usuario Ingreso");
		tab_tabla.getColumna("ing_usua").setLectura(true);
		
		tab_tabla.getColumna("ing_fec").setVisible(true);
		tab_tabla.getColumna("ing_fec").setNombreVisual("Fecha Ingreso");
		tab_tabla.getColumna("ing_fec").setLectura(true);
		
		tab_tabla.getColumna("ing_hora").setVisible(true);
		tab_tabla.getColumna("ing_hora").setNombreVisual("Hora Ingreso");
		tab_tabla.getColumna("ing_hora").setLectura(true);
		
		tab_tabla.getColumna("actual_usua").setVisible(true);
		tab_tabla.getColumna("actual_usua").setNombreVisual("Usuario Actual");
		tab_tabla.getColumna("actual_usua").setLectura(true);
		
		tab_tabla.getColumna("actual_fecha").setVisible(true);
		tab_tabla.getColumna("actual_fecha").setNombreVisual("Fecha Actual");
		tab_tabla.getColumna("actual_fecha").setLectura(true);
		
		tab_tabla.getColumna("actual_hora").setVisible(true);
		tab_tabla.getColumna("actual_hora").setNombreVisual("Hora Actual");
		tab_tabla.getColumna("actual_hora").setLectura(true);
		
		
		tab_tabla.dibujar();
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();        
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
		
	
	}

	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		}

	@Override
	public void eliminar() {
		tab_tabla.eliminar();
	}

	public Tabla getTab_tabla() {
		return tab_tabla;
	}

	public void setTab_tabla(Tabla tab_tabla) {
		this.tab_tabla = tab_tabla;
	}
	
	
	public void filtrar(){
		
		
		tab_tabla.setSql("select ide_prprf,ide_prfuf,ide_prpoa,valor_reformado_prprf," +
				"resolucion_prprf,fecha_prprf,saldo_actual_prprf,activo_prprf, " +
				"usuario_ingre as ing_usua,fecha_ingre as ing_fec,hora_ingre as ing_hora,usuario_actua as actual_usua,fecha_actua as actual_fecha, " +
				"hora_actua as actual_hora from pre_poa_reforma_fuente " +
				"where fecha_prprf between '"+cal_fecha_inicial.getFecha()+"' and '"+cal_fecha_final.getFecha()+"' ");
		tab_tabla.ejecutarSql();
		
	}
	
	
	public void aceptarFiltrar(){
				tab_tabla.ejecutarSql();	
		
	}

	public Calendario getCal_fecha_inicial() {
		return cal_fecha_inicial;
	}

	public void setCal_fecha_inicial(Calendario cal_fecha_inicial) {
		this.cal_fecha_inicial = cal_fecha_inicial;
	}

	public Calendario getCal_fecha_final() {
		return cal_fecha_final;
	}

	public void setCal_fecha_final(Calendario cal_fecha_final) {
		this.cal_fecha_final = cal_fecha_final;
	}


	
	
	
	
}
