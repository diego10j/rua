/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import paq_general.*;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.inventario.ServicioInventario;

public class pre_costo_venta extends Pantalla {

	private Tabla tab_tabla = new Tabla();
        private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
@EJB
        private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

	public pre_costo_venta() {  
            
            bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);
        
        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarInventario");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);
        
        
		tab_tabla.setId("tab_tabla");
		tab_tabla.setSql(ser_inventario.getSqlCostoVenta("1900-01-01", "1900-01-01"));
                tab_tabla.getColumna("nombre_inarti").setFiltro(true);
                tab_tabla.setLectura(true);
		tab_tabla.dibujar();
                tab_tabla.setRows(20);
		PanelTabla pat_panel = new PanelTabla();
		pat_panel.setPanelTabla(tab_tabla);

		Division div_division = new Division();        
		div_division.setId("div_division");
		div_division.dividir1(pat_panel);
		agregarComponente(div_division);
	}
public void actualizarInventario(){
    tab_tabla.setSql(ser_inventario.getSqlCostoVenta(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
    tab_tabla.ejecutarSql();
}
	@Override
	public void insertar() {
		tab_tabla.insertar();
	}

	@Override
	public void guardar() {
		int cont_activos=0;
		for (int i = 0; i < tab_tabla.getTotalFilas(); i++) {
			if (tab_tabla.getValor(i, "ACTIVO_GEANI").equalsIgnoreCase("true")){
				cont_activos++;
			}
		}
		if (cont_activos==1){
			if (tab_tabla.guardar()){
				guardarPantalla();
			}
		}else{
			if (cont_activos==0){
				utilitario.agregarMensajeInfo("No se puede guardar","Debe existir un Año activo");
			}else{
				utilitario.agregarMensajeInfo("No se puede guardar","Debe existir solo un Año activo");
			}
		}
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
}
