/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioContabilidadGeneral;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 * @version 2.0
 */
public class pre_plan_cuentas extends Pantalla {

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Arbol arb_arbol = new Arbol();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_rep = new SeleccionFormatoReporte();

    public pre_plan_cuentas() {

        bar_botones.agregarReporte();

        sef_rep.setId("sef_rep");
        sef_rep.getBot_aceptar().setMetodo("aceptarReporte");

        //Configurar tabla1
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("con_cab_plan_cuen", "ide_cncpc", 1);
        tab_tabla1.onSelect("seleccionarTabla1"); // ejecuta  seleccionar_tabla1  cuando se seleciona una fila de la tabla1
        tab_tabla1.getColumna("ide_cncpc").setVisible(false);
        tab_tabla1.agregarRelacion(tab_tabla2);// crea la relación entre la 
        tab_tabla1.getColumna("activo_cncpc").setComentario("Sirve para identificar al Plan de Cuentas vigente o activo");
        tab_tabla1.getColumna("activo_cncpc").setMetodoChange("cambiarPlanActivo");
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        //Configurar tabla2
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("con_det_plan_cuen", "ide_cndpc", 2);
        tab_tabla2.getColumna("ide_cndpc").setVisible(false);
        tab_tabla2.getColumna("ide_cntcu").setCombo("con_tipo_cuenta", "ide_cntcu", "nombre_cntcu", "");
        tab_tabla2.getColumna("ide_cntcu").setLongitud(-1);
        tab_tabla2.getColumna("nivel_cndpc").setCombo(utilitario.getListaNiveles());
        tab_tabla2.getColumna("nivel_cndpc").setLongitud(-1);
        tab_tabla2.getColumna("nivel_cndpc").setRequerida(true);
        if (tab_tabla1.getValorSeleccionado() != null) {// si hay datos en la tabla1
            // entonces configura mascara en el campo codig_recur_cndpc  de la tabla 2
            tab_tabla2.getColumna("codig_recur_cndpc").setMascara(tab_tabla1.getValor("mascara_cncpc"));
            tab_tabla2.setCampoRecursivo("codig_recur_cndpc", tab_tabla1.getValor("mascara_cncpc"));
        }
        tab_tabla2.setCampoNombre("codig_recur_cndpc || ' ' || nombre_cndpc");
        tab_tabla2.setCampoPadre("con_ide_cndpc");
        tab_tabla2.getColumna("ide_cnncu").setLectura(true);
        tab_tabla2.getColumna("codig_recur_cndpc").setLectura(false);
        tab_tabla2.agregarArbol(arb_arbol);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        //Configurar arbol
        arb_arbol.setId("arb_arbol");
        arb_arbol.setCampoOrden("codig_recur_cndpc"); // para que se ordene por el codigo  el arbol
        arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado()); // filtra los datos de la tabla1 seleccionada
        arb_arbol.setOptimiza(true);
        arb_arbol.dibujar();

        Division div_vertical = new Division();
        div_vertical.dividir2(arb_arbol, pat_panel2, "45%", "V");
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, div_vertical, "11%", "H");

        agregarComponente(div_division);
        agregarComponente(rep_reporte);
        agregarComponente(sef_rep);
    }

    @Override
    public void insertar() {
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
            arb_arbol.limpiar(); //porque se tiene que crear un arbol nuevo
            utilitario.addUpdate("arb_arbol");
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.insertar();
            if (arb_arbol.getValorSeleccionado() != null) {
                TablaGenerica tab_cuentaPadre = ser_contabilidad.getCuenta(arb_arbol.getValorSeleccionado());
                int int_nivelPadre = 0;
                try {
                    int_nivelPadre = Integer.parseInt(tab_cuentaPadre.getValor("ide_cnncu"));
                } catch (Exception e) {
                }
                tab_tabla2.setValor("ide_cnncu", String.valueOf((int_nivelPadre + 1)));
                tab_tabla2.setValor("ide_cntcu", tab_cuentaPadre.getValor("ide_cntcu"));
            } else {
                tab_tabla2.setValor("ide_cnncu", String.valueOf(1));
            }
            tab_tabla2.getColumna("codig_recur_cndpc").setMascara(tab_tabla1.getValor("mascara_cncpc"));
        }

    }

    @Override
    public void eliminar() {
        if (tab_tabla1.isFocus()) {
            if (tab_tabla1.eliminar()) {
                arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado());
                arb_arbol.ejecutarSql();
                utilitario.addUpdate("arb_arbol");
            }
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        }
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        if (tab_tabla2.guardar()) {
            if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                arb_arbol.actualizar(); ///borrar
            }
        }
    }

    public void cambiarPlanActivo(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        for (int i = 0; i < tab_tabla1.getFilas().size(); i++) {  //Para recorrer una tabla        
            if (!tab_tabla1.isFilaInsertada(i)) {
                tab_tabla1.setValor(i, "activo_cncpc", "false");
                tab_tabla1.modificar(i);
            }
        }
        tab_tabla1.setValor(tab_tabla1.getFilaActual(), "activo_cncpc", "true");
        tab_tabla1.setFilaActual(tab_tabla1.getFilaActual());

        arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado());
        tab_tabla2.setValorPadre(arb_arbol.getValorSeleccionado());
        arb_arbol.ejecutarSql();

        tab_tabla2.setValorPadre(arb_arbol.getValorSeleccionado());
        tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        utilitario.addUpdate("arb_arbol");
    }

    public void seleccionarTabla1(SelectEvent evt) {
        //1 selecciona la fila de la tabla1
        tab_tabla1.seleccionarFila(evt);
        //2 crea una condición al arbol con la cabecera seleccionada
        arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado());
        //3 ejecuta el sql del arbol
        tab_tabla2.setValorPadre(arb_arbol.getValorSeleccionado());
        arb_arbol.ejecutarSql();
        utilitario.addUpdate("arb_arbol");
    }

    public void seleccionarTabla1(AjaxBehaviorEvent evt) {

    }

    @Override
    public void inicio() {
        //Para controlar  boton inicio      
        try {
            tab_tabla1.inicio();
            arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado());
            arb_arbol.ejecutarSql();
            tab_tabla2.setValorPadre(arb_arbol.getValorSeleccionado());
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } catch (Exception e) {
        }
    }

    @Override
    public void siguiente() {
        //Para controlar  boton siguiente
        try {
            tab_tabla1.siguiente();
            arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado());
            arb_arbol.ejecutarSql();
            tab_tabla2.setValorPadre(arb_arbol.getValorSeleccionado());
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } catch (Exception e) {
        }
    }

    @Override
    public void atras() {
        //Para controlar  boton atras
        try {
            tab_tabla1.atras();
            arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado());
            arb_arbol.ejecutarSql();
            tab_tabla2.setValorPadre(arb_arbol.getValorSeleccionado());
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } catch (Exception e) {
        }
    }

    @Override
    public void fin() {
        //Para controlar boton fin
        try {
            tab_tabla1.fin();
            arb_arbol.setCondicion("ide_cncpc=" + tab_tabla1.getValorSeleccionado());
            arb_arbol.ejecutarSql();
            tab_tabla2.setValorPadre(arb_arbol.getValorSeleccionado());
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
        } catch (Exception e) {
        }
    }

    @Override
    public void abrirListaReportes() {
//Se ejecuta cuando da click en el boton de Reportes de la Barra    
        rep_reporte.dibujar();
    }

    @Override
    public void aceptarReporte() {
//Se ejecuta cuando se selecciona un reporte de la lista
        Map parametro = new HashMap();
        if (rep_reporte.getReporteSelecionado().equals("Plan de cuentas")) {
            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                parametro.put("ide_cncpc", Long.parseLong(tab_tabla1.getValor("ide_cncpc")));
                sef_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                sef_rep.dibujar();
            }
        }
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
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

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_rep() {
        return sef_rep;
    }

    public void setSef_rep(SeleccionFormatoReporte sef_rep) {
        this.sef_rep = sef_rep;
    }
}
