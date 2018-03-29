/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Combo;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_presupuesto.ejb.ServicioPresupuesto;
import servicios.inventario.ServicioInventario;

/**
 *
 * @author Luis
 */
public class pre_articulos extends Pantalla {

    
    private Tabla tab_tabla = new Tabla();
    private Arbol arb_arbol = new Arbol();
    private Combo com_anio = new Combo();
    private Combo com_bodegas = new Combo();
    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

   @EJB
    private final ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
    
    public pre_articulos() {    
        
        com_anio.setId("com_anio");
        com_anio.setCombo(ser_presupuesto.getAnio("true,false"));
        
        
        com_bodegas.setId("com_bodegas");
        com_bodegas.setCombo(ser_inventario.getBodegaSucursalDatos());
        bar_botones.agregarComponente(new Etiqueta("Periodo Fiscal: "));
        bar_botones.agregarComponente(com_anio);
        bar_botones.agregarComponente(new Etiqueta("Bodega: "));
        bar_botones.agregarComponente(com_bodegas);
        
        Boton bot_consultar= new Boton();
        bot_consultar.setValue("CONSULTAR");
        bot_consultar.setMetodo("consultar");
        bar_botones.agregarBoton(bot_consultar);
        
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("bodt_articulos","ide_boart", 1);
        tab_tabla.getColumna("ide_inarti").setCombo(ser_inventario.getMaterialInventario("'HIJO'"));
        tab_tabla.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla.getColumna("ide_inbod").setVisible(false);
        tab_tabla.getColumna("ide_geani").setVisible(false);
        tab_tabla.getColumna("ingreso_materia_boartl").setValorDefecto("0");
        tab_tabla.getColumna("egreso_material_boart").setValorDefecto("0");
        tab_tabla.getColumna("existencia_inicial_boart").setValorDefecto("0");
        tab_tabla.getColumna("costo_inicial_boart").setValorDefecto("0");
        tab_tabla.getColumna("costo_anterior_boart").setValorDefecto("0");
        tab_tabla.getColumna("costo_actual_boart").setValorDefecto("0");

        tab_tabla.getColumna("ingreso_materia_boartl").setRequerida(true);
        tab_tabla.getColumna("egreso_material_boart").setRequerida(true);
        tab_tabla.getColumna("existencia_inicial_boart").setRequerida(true);
        tab_tabla.getColumna("costo_inicial_boart").setRequerida(true);
        tab_tabla.getColumna("costo_anterior_boart").setRequerida(true);
        tab_tabla.getColumna("costo_actual_boart").setRequerida(true);
        
        tab_tabla.setColumnaSuma("ingreso_materia_boartl,egreso_material_boart,existencia_inicial_boart,costo_inicial_boart");
        tab_tabla.setCondicion("ide_geani=-1 and ide_inbod=-1");
        tab_tabla.setCondicionSucursal(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);


        Division div_division = new Division();
        div_division.setId("div_division");
         div_division.dividir1( pat_panel);  //arbol y div3
        agregarComponente(div_division);
    }
    public void consultar(){
        tab_tabla.setCondicion("ide_geani="+com_anio.getValue().toString()+" and ide_inbod ="+com_bodegas.getValue().toString());
        tab_tabla.ejecutarSql();
        utilitario.addUpdate("tab_tabla");
    }
    @Override
    public void insertar() {
        tab_tabla.insertar();
        tab_tabla.setValor("ide_geani",com_anio.getValue().toString());
        tab_tabla.setValor("ide_inbod",com_bodegas.getValue().toString());
    }

    @Override
    public void guardar() {
        tab_tabla.guardar();
        guardarPantalla();
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

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }
    
}
