/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego Jacome
 */
public class pre_configuracion_impuestos extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private final TablaGenerica tab_tabla2 = new TablaGenerica();
    private Tabla tab_tabla3 = new Tabla();
    private final Combo com_impuesto = new Combo();

    public pre_configuracion_impuestos() {
        com_impuesto.setCombo("select ide_cnimp,nombre_cnimp from con_impuesto");
        com_impuesto.setMetodo("seleccionarCombo");
        bar_botones.agregarComponente(new Etiqueta("IMPUESTO :"));
        bar_botones.agregarComponente(com_impuesto);
        bar_botones.quitarBotonsNavegacion();

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("con_cabece_impues", "ide_cncim", 1);
        tab_tabla1.setCondicion("ide_cnimp=-1");
        tab_tabla1.onSelect("seleccionarTabla1");
        tab_tabla1.getColumna("ide_cnimp").setVisible(false);
        tab_tabla1.getColumna("ide_cncim").setVisible(false);
        tab_tabla1.setCampoOrden("casillero_cncim");
        tab_tabla1.setRows(25);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setTabla("con_vigenc_impues", "ide_cnvim");
        tab_tabla2.setCondicion("ide_cncim=-1");
        tab_tabla2.ejecutarSql();

        tab_tabla3.setId("tab_tabla3");
        tab_tabla3.setTabla("con_detall_impues", "ide_cndim", 3);
        tab_tabla3.setCondicion("ide_cnvim=-1");
        tab_tabla3.getColumna("ide_cndim").setVisible(false);
        tab_tabla3.getColumna("ide_cnvim").setVisible(false);
        tab_tabla3.getColumna("ide_cntdo").setCombo("con_tipo_document", "ide_cntdo", "nombre_cntdo", "");
        tab_tabla3.getColumna("ide_cntco").setCombo("con_tipo_contribu", "ide_cntco", "nombre_cntco", "");
        tab_tabla3.setRows(25);
        tab_tabla3.dibujar();
        PanelTabla pat_panel3 = new PanelTabla();
        pat_panel3.setPanelTabla(tab_tabla3);
        Division div_division = new Division();
        div_division.dividir2(pat_panel1, pat_panel3, "50%", "H");
        agregarComponente(div_division);
    }

    public void seleccionarCombo() {
        if (com_impuesto.getValue() != null) {
            tab_tabla1.setCondicion("ide_cnimp=" + com_impuesto.getValue());
            tab_tabla1.ejecutarSql();
            actualizaSeleccion();
        } else {
            tab_tabla1.limpiar();
            tab_tabla2.limpiar();
            tab_tabla3.limpiar();
        }
    }

    @Override
    public void insertar() {
        if (com_impuesto.getValue() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Impuesto", "");
            return;
        }
        if (tab_tabla1.isFocus()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_cnimp", String.valueOf(com_impuesto.getValue()));
        } else if (tab_tabla3.isFocus()) {
            tab_tabla3.insertar();
            tab_tabla3.setValor("ide_cnvim", tab_tabla2.getValor("ide_cnvim"));
        }
    }

    @Override
    public void guardar() {
        tab_tabla1.guardar();
        tab_tabla3.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void seleccionarTabla1(AjaxBehaviorEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        actualizaSeleccion();
    }

    public void seleccionarTabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        actualizaSeleccion();
    }

    private void actualizaSeleccion() {
        tab_tabla2.setCondicion("ide_cncim=" + tab_tabla1.getValor("ide_cncim"));
        tab_tabla2.ejecutarSql();
        tab_tabla3.setCondicion("ide_cnvim=" + tab_tabla2.getValor("ide_cnvim"));
        tab_tabla3.ejecutarSql();
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla3() {
        return tab_tabla3;
    }

    public void setTab_tabla3(Tabla tab_tabla3) {
        this.tab_tabla3 = tab_tabla3;
    }
}
