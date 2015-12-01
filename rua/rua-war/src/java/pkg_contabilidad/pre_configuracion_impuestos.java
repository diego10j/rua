/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_configuracion_impuestos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Tabla tab_tabla3 = new Tabla();
    private Division div_division = new Division();

    public pre_configuracion_impuestos() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("con_cabece_impues", "ide_cncim", 1);
        tab_tabla1.getColumna("ide_cnimp").setCombo("con_impuesto", "ide_cnimp", "nombre_cnimp", "");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("con_vigenc_impues", "ide_cnvim", 2);
        tab_tabla2.setCampoForanea("ide_cncim");
        tab_tabla2.agregarRelacion(tab_tabla3);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("con_detall_impues", "ide_cndim", 3);
        tab_tabla3.setCampoForanea("ide_cnvim");
        tab_tabla3.getColumna("ide_cntdo").setCombo("con_tipo_document", "ide_cntdo", "nombre_cntdo", "");
        tab_tabla3.getColumna("ide_cntco").setCombo("con_tipo_contribu", "ide_cntco", "nombre_cntco", "");
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);

        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel2, pat_panel3, "45%", "40%", "H");

        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla2.guardar();
        tab_tabla3.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
    }

    public void seleccionar_tabla2(SelectEvent evt) {
        tab_tabla2.seleccionarFila(evt);
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

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }
}
