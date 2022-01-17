/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bodega;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Boton;
import framework.componentes.Combo;
import sistema.aplicacion.Pantalla;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionArbol;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
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
    private SeleccionCalendario sec_rango_reporte = new SeleccionCalendario();
    private Reporte rep_reporte = new Reporte();
    private SeleccionTabla sel_tab = new SeleccionTabla();
    private SeleccionArbol sel_arbol = new SeleccionArbol();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map parametro = new HashMap();

    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

    @EJB
    private final ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);

    public pre_articulos() {

        com_anio.setId("com_anio");
        com_anio.setCombo(ser_presupuesto.getAnio("true,false"));

        //com_bodegas.setId("com_bodegas");
        //com_bodegas.setCombo("select ide_inbod,nombre_inbod from inv_bodega where nivel_inbod='HIJO' and ide_sucu=" + utilitario.getVariable("ide_sucu"));
        //com_bodegas.setValue(ser_inventario.getBodegaSucursal());
        bar_botones.agregarComponente(new Etiqueta("Periodo Fiscal: "));
        bar_botones.agregarComponente(com_anio);
        /*bar_botones.agregarComponente(new Etiqueta("Bodega: "));
        bar_botones.agregarComponente(com_bodegas);*/

        Boton bot_consultar = new Boton();
        bot_consultar.setValue("CONSULTAR");
        bot_consultar.setMetodo("consultar");
        bar_botones.agregarBoton(bot_consultar);

        Boton bot_recalcular = new Boton();
        bot_recalcular.setValue("RECALCULAR");
        bot_recalcular.setMetodo("recalcular");
        bar_botones.agregarBoton(bot_recalcular);

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("bodt_articulos", "ide_boart", 1);
        tab_tabla.getColumna("ide_inarti").setCombo(ser_inventario.getMaterialInventario("'HIJO'"));
        tab_tabla.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla.getColumna("ide_inbod").setVisible(false);
        tab_tabla.getColumna("ide_geani").setVisible(false);
        tab_tabla.getColumna("ingreso_material_boart").setValorDefecto("0");
       // tab_tabla.getColumna("ingreso_material_boart").setMetodoChange("calcularExistencia");
        tab_tabla.getColumna("egreso_material_boart").setValorDefecto("0");
        //tab_tabla.getColumna("egreso_material_boart").setMetodoChange("calcularExistencia");
        tab_tabla.getColumna("existencia_inicial_boart").setValorDefecto("0");
        tab_tabla.getColumna("costo_inicial_boart").setValorDefecto("0");
        tab_tabla.getColumna("costo_anterior_boart").setValorDefecto("0");
        tab_tabla.getColumna("costo_actual_boart").setValorDefecto("0");
        //tab_tabla.getColumna("precio_venta_boart").setValorDefecto("0");
        tab_tabla.getColumna("ingreso_material_boart").setRequerida(true);
        tab_tabla.getColumna("egreso_material_boart").setRequerida(true);
        tab_tabla.getColumna("existencia_inicial_boart").setRequerida(true);
        tab_tabla.getColumna("costo_inicial_boart").setRequerida(true);
        tab_tabla.getColumna("costo_anterior_boart").setRequerida(true);
        tab_tabla.getColumna("costo_actual_boart").setRequerida(true);

        tab_tabla.setColumnaSuma("ingreso_material_boart,egreso_material_boart,existencia_inicial_boart,costo_inicial_boart");
        tab_tabla.setCondicion("ide_geani=-1 and ide_inbod=-1");
        //tab_tabla.setCondicionSucursal(true);
        tab_tabla.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir1(pat_panel);  //arbol y div3
        agregarComponente(div_division);

        rep_reporte.setId("rep_reporte");
        agregarComponente(rep_reporte);
        bar_botones.agregarReporte();

        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);

        //selec arbol
        sel_arbol.setId("sel_arbol");
        sel_arbol.setSeleccionArbol("inv_articulo", "ide_inarti", "nombre_inarti", "inv_ide_inarti");
        sel_arbol.getArb_seleccion().setOptimiza(true);
        agregarComponente(sel_arbol);
        sel_arbol.getBot_aceptar().setMetodo("aceptarReporte");

        sec_rango_reporte.setId("sec_rango_reporte");
        sec_rango_reporte.setMultiple(false);
        sec_rango_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(sec_rango_reporte);

    }

    public void recalcular() {
        if (com_anio.getValue() == null) {
            utilitario.agregarMensajeInfo("Mensaje,", "Seleccione el Periodo Fiscal y consultar");
        } else {
            /**
             * parametro aplica bodega True permite el registrar el ingreso y
             * egresos pos sucursales y empresas es decir varias bodegas False
             * permite regitrar por defecto en una solo bodega
             */
            //if (utilitario.getVariable("p_varias_bodegas").equals("true")) {
                ser_inventario.getRecalcularInventario("1", com_anio.getValue().toString(), tab_tabla.getValor(tab_tabla.getFilaActual(), "ide_inarti"), utilitario.getVariable("IDE_SUCU"), utilitario.getVariable("IDE_EMPR"));
            /*} else {
                ser_inventario.getRecalcularInventario("0", com_anio.getValue().toString(), tab_tabla.getValor(tab_tabla.getFilaActual(), "ide_inarti"), "0", "0");
            }*/
            utilitario.agregarMensaje("", "Se recalculo corecctamente");
            utilitario.addUpdateTabla(tab_tabla, "", "");
        }

    }

    public void consultar() {
        if (com_anio.getValue() == null) {
            utilitario.agregarMensajeInfo("Información", "Seleccione el periodo fiscal");
        } else {
            tab_tabla.setCondicion("ide_geani=" + com_anio.getValue().toString());
            tab_tabla.ejecutarSql();
            utilitario.addUpdate("tab_tabla");
        }
    }

    @Override
    public void abrirListaReportes() {
        if (com_anio.getValue() == null) {
            utilitario.agregarMensajeInfo("Información", "Seleccione el periodo fiscal");
        } else {
            rep_reporte.dibujar();
        }
    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Kardex")) {

            if (rep_reporte.isVisible()) {

                parametro = new HashMap();
                rep_reporte.cerrar();
                sel_arbol.dibujar();
                utilitario.addUpdate("rep_reporte,sel_arbol");
            } else if (sel_arbol.isVisible()) {
                parametro.put("ide_inarti", sel_arbol.getSeleccionados());

                sel_arbol.cerrar();
                sec_rango_reporte.setMultiple(true);
                sec_rango_reporte.dibujar();
            } else if (sec_rango_reporte.isVisible()) {
                parametro.put("nombre", utilitario.getVariable("NICK"));
                parametro.put("ide_geani", Integer.parseInt(com_anio.getValue().toString()));
                parametro.put("pide_fecha_inicio", sec_rango_reporte.getFecha1());
                parametro.put("pide_fecha_fin", sec_rango_reporte.getFecha2());
                // System.out.println("seleccion..de arbol...ing" + sel_arbol.getSeleccionados());
                sec_rango_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_formato.dibujar();
                utilitario.addUpdate("sef_formato,sel_arbol");
            }
        }
    }

    @Override
    public void insertar() {
        tab_tabla.insertar();
        tab_tabla.setValor("ide_geani", com_anio.getValue().toString());
        //tab_tabla.setValor("ide_inbod", com_bodegas.getValue().toString());
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

    public SeleccionTabla getSel_tab() {
        return sel_tab;
    }

    public void setSel_tab(SeleccionTabla sel_tab) {
        this.sel_tab = sel_tab;
    }

    public SeleccionArbol getSel_arbol() {
        return sel_arbol;
    }

    public void setSel_arbol(SeleccionArbol sel_arbol) {
        this.sel_arbol = sel_arbol;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getParametro() {
        return parametro;
    }

    public void setParametro(Map parametro) {
        this.parametro = parametro;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionCalendario getSec_rango_reporte() {
        return sec_rango_reporte;
    }

    public void setSec_rango_reporte(SeleccionCalendario sec_rango_reporte) {
        this.sec_rango_reporte = sec_rango_reporte;
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
