/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inventario;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_tipo_transaccion_inv extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();

    public pre_tipo_transaccion_inv() {
        bar_botones.getBot_insertar().setUpdate("tab_tabla1,tab_tabla2");
        bar_botones.getBot_guardar().setUpdate("tab_tabla1,tab_tabla2");
        bar_botones.getBot_eliminar().setUpdate("tab_tabla1,tab_tabla2");

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("inv_tip_comp_inve", "ide_intci", 1);
        tab_tabla1.getColumna("signo_intci").setRadio(getListaSignos(), "0");
//      tab_tabla1.getColumna("ide_cnere").setCombo("con_estado_retenc", "ide_cnere", "nombre_cnere", "");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("inv_tip_tran_inve", "ide_intti", 2);
        tab_tabla2.setCampoForanea("ide_intci");

        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);


        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "35%", "H");
        agregarComponente(div_division);

    }

    public List getListaSignos() {
        //pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "+"
        };
        Object fila2[] = {
            "-1", "-"
        };
        lista.add(fila1);
        lista.add(fila2);
        return lista;
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
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
