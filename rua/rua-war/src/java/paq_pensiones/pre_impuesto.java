/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;


import framework.componentes.Barra;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.faces.event.AjaxBehaviorEvent;
import sistema.aplicacion.Pantalla;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author HP
 */
public class pre_impuesto extends Pantalla {

    private Tabla tab_tabla = new Tabla();
    private Division div_division = new Division();

    public pre_impuesto() {
        bar_botones.getBot_insertar().setUpdate("tab_tabla");
        bar_botones.getBot_guardar().setUpdate("tab_tabla");
        bar_botones.getBot_eliminar().setUpdate("tab_tabla");

        //configuracion de la tabla impuesto
        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("rec_impuesto", "ide_impuesto_reimp", 1);
        tab_tabla.setCampoOrden("ide_impuesto_reimp");
       // tab_tabla.getColumna("ide_cuenta").setCombo("conc_catalogo_cuentas", "ide_cuenta", "ide_cuenta,cue_codigo,cue_descripcion", "");
        //tab_tabla.getColumna("ide_cuenta").setAutoCompletar();
        //tab_tabla.getColumna("ide_clasificador").setCombo("conc_clasificador", "ide_clasificador", "ide_clasificador,pre_codigo,pre_descripcion", "");
        //tab_tabla.getColumna("ide_clasificador").setAutoCompletar();
        tab_tabla.setRows(20);
        tab_tabla.getColumna("porcentaje_reimp").setMetodoChange("cambiaPorcentaje");
        tab_tabla.getColumna("valor_reimp").setMetodoChange("cambiaValor");
        //tab_tabla.getColumna("tipo_con").setVisible(false);
        tab_tabla.getColumna("ide_inarti").setCombo("SELECT ide_inarti,nombre_inarti  FROM inv_articulo  WHERE nivel_inarti='HIJO'");
        tab_tabla.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        div_division.setId("div_division");
        div_division.dividir1(pat_panel);


        gru_pantalla.getChildren().add(div_division);


    }

    public void cambiaValor(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        tab_tabla.setValor(tab_tabla.getFilaActual(), "porcentaje", "");
        utilitario.addUpdateTabla(tab_tabla, "porcentaje", "");
    }

    public void cambiaPorcentaje(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        tab_tabla.setValor(tab_tabla.getFilaActual(), "valor", "");
        utilitario.addUpdateTabla(tab_tabla, "valor", "");
    }

    public void insertar() {
        tab_tabla.insertar();
    }

    public void guardar() {
        tab_tabla.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    public void eliminar() {
        tab_tabla.eliminar();
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Barra getBar_botones() {
        return bar_botones;
    }

    public void setBar_botones(Barra bar_botones) {
        this.bar_botones = bar_botones;
    }
}
