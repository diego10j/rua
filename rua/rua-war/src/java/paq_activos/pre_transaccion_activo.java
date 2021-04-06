/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_activos;

import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import org.primefaces.event.NodeSelectEvent;
import servicios.activos.ServicioActivosFijos;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_transaccion_activo extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Arbol arb_arbol = new Arbol();
     @EJB
    private final ServicioActivosFijos ser_activos = (ServicioActivosFijos) utilitario.instanciarEJB(ServicioActivosFijos.class);


    public pre_transaccion_activo() {
        bar_botones.getBot_insertar().setRendered(false);
        bar_botones.getBot_guardar().setRendered(false);
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_activos.getSqlHistoriaAsignacionActivo("-1","-1"));
        tab_tabla1.getColumna("codigo_barras_acafi").setFiltro(true);
        tab_tabla1.getColumna("nombre_inarti").setFiltro(true);
        tab_tabla1.getColumna("observacion_acact").setFiltro(true);
        tab_tabla1.getColumna("codigo_acact").setFiltro(true);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        tab_tabla1.setRows(15);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);


        Division div_division = new Division();
        div_division.dividir1(pat_panel);
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        tab_tabla1.insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        tab_tabla1.eliminar();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(Arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }
    
}
