/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pensiones;



import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author HP
 */
public class pre_concepto extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();
    private Division div_division = new Division();

    public pre_concepto() {

        //configuracion de la tabla concepto (cabecera)
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("rec_concepto", "ide_concepto", 1);
        tab_tabla1.setCampoOrden("des_concepto");
        tab_tabla1.getColumna("ide_tipo_documento").setCombo("rec_tipo_documento", "ide_tipo_documento", "detalle", "");
        tab_tabla1.getColumna("abonos").setMetodoChange("activarCampos");
        //  tab_tabla1.getColumna("urbano").setValorDefecto("true");
        tab_tabla1.getColumna("urbano").setMetodoChange("cambiaUrbano");
        tab_tabla1.getColumna("rural").setMetodoChange("cambiaRural");
        tab_tabla1.getColumna("uso").setMetodoChange("cambiaUso");
        tab_tabla1.getColumna("exoneracion").setMetodoChange("cambiaExoneracion");
        tab_tabla1.getColumna("dependencia").setMetodoChange("cambiaDependencia");
        tab_tabla1.setRows(10);
        tab_tabla1.onSelect("seleccionar_tabla1");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();

        //configuracion de la tabla impuesto (detalle)
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("rec_forma_impuesto", "ide_forma_impuesto", 2);
        tab_tabla2.setCampoOrden("ide_impuesto");
        tab_tabla2.getColumna("ide_impuesto").setCombo("rec_impuesto", "ide_impuesto", "des_impuesto,porcentaje,valor", "");
        tab_tabla2.getColumna("ide_impuesto").setAutoCompletar();
        tab_tabla2.setCampoForanea("ide_concepto");



        tab_tabla2.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        pat_panel2.setPanelTabla(tab_tabla2);

        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");

        
        gru_pantalla.getChildren().add(div_division);
        

    }

    public void insertar() {
        if (tab_tabla1.isFocus()) {
            if (tab_tabla1.isFilaInsertada() == false) {
                tab_tabla1.insertar();
            } else {
                utilitario.agregarMensajeInfo("Debe guardar el concepto que esta registrando", "");
            }
        } else if (tab_tabla2.isFocus()) {
            String lstr_uso = tab_tabla1.getValor("uso");
            if (!lstr_uso.equalsIgnoreCase("true")) {
                String lstr_porcentaje_exonera = tab_tabla1.getValor("exoneracion");
                String lstr_porcentaje_abono = tab_tabla1.getValor("abonos");

                if (lstr_porcentaje_abono != null && lstr_porcentaje_abono.equals("true")) {
                    tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(true);
                } else {
                    tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(false);
                }

                if (lstr_porcentaje_exonera.equals("true")) {
                    tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(true);
                } else {
                    tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(false);
                }

                tab_tabla2.insertar();
            } else {
                utilitario.agregarMensajeInfo("Atención", "Al marcar USO no puede registrar detalles");
            }
        }
    }

    public boolean validarPorcentajeExoneracion() {
        double dou_exonera = tab_tabla2.getSumaColumna("porcentaje_exonera");
        if (dou_exonera == 100.0) {
            return true;
        }
        return false;
    }

    public boolean validarPorcentajeAbono() {
        double dou_abono = tab_tabla2.getSumaColumna("porcentaje_abono");
        if (dou_abono == 100.0) {
            return true;
        }
        return false;
    }

    public void guardar() {
        boolean valida = true;
        if (tab_tabla2.getColumna("PORCENTAJE_EXONERA").isVisible()) {
            valida = validarPorcentajeExoneracion();
        }
        if (valida == true && tab_tabla2.getColumna("porcentaje_abono").isVisible()) {
            valida = validarPorcentajeAbono();
        }
        if (valida) {
            tab_tabla1.guardar();
            tab_tabla2.guardar();
            utilitario.getConexion().guardarPantalla();
        } else {
            utilitario.agregarMensajeError("No se puede Guardar", "La suma de los detalles del porcentaje del detalle  debe ser igual a 100%");
        }
    }

    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public void cambiaUrbano(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "urbano").equals("true")) {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "rural", "false");
        } else {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "rural", "true");
        }
        utilitario.addUpdateTabla(tab_tabla1, "rural", "");
    }

    public void cambiaUso(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "uso").equals("true")) {
            if (tab_tabla2.getTotalFilas() == 0) {
                tab_tabla1.setValor(tab_tabla1.getFilaActual(), "exoneracion", "true");
                tab_tabla1.setValor(tab_tabla1.getFilaActual(), "dependencia", "false");
            } else {
                tab_tabla1.setValor(tab_tabla1.getFilaActual(), "uso", "false");
                utilitario.addUpdateTabla(tab_tabla1, "uso", "");
                utilitario.agregarMensajeError("No se puede cambiar a tipo uso por que ya tiene registros en el detalle", "");
            }
        } else {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "exoneracion", "false");
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "dependencia", "false");
        }
        utilitario.addUpdateTabla(tab_tabla1, "exoneracion,dependencia", "");
    }

    public void cambiaDependencia(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "dependencia").equals("true")) {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "exoneracion", "true");
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "uso", "false");
        } else {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "exoneracion", "false");
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "uso", "false");
        }

        utilitario.addUpdateTabla(tab_tabla1, "exoneracion,uso", "");
    }

    public void cambiaExoneracion(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "exoneracion").equals("true")) {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "uso", "true");
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "dependencia", "false");
        } else {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "uso", "false");
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "dependencia", "false");
        }
        utilitario.addUpdateTabla(tab_tabla1, "dependencia,uso", "");

        /////
        String lstr_porcentaje_exonera = tab_tabla1.getValor("exoneracion");
        String lstr_porcentaje_abono = tab_tabla1.getValor("abonos");

        if (lstr_porcentaje_abono.equals("true")) {
            tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(true);
        } else {
            tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(false);
        }

        if (lstr_porcentaje_exonera.equals("true")) {
            tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(true);
        } else {
            tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(false);
        }
        utilitario.addUpdate("tab_tabla2");
    }

    public void cambiaRural(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);
        if (tab_tabla1.getValor(tab_tabla1.getFilaActual(), "rural").equals("true")) {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "urbano", "false");
        } else {
            tab_tabla1.setValor(tab_tabla1.getFilaActual(), "urbano", "true");
        }

        utilitario.addUpdateTabla(tab_tabla1, "urbano", "");
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        String lstr_porcentaje_exonera = tab_tabla1.getValor("exoneracion");
        String lstr_porcentaje_abono = tab_tabla1.getValor("abonos");
        if (lstr_porcentaje_abono != null && lstr_porcentaje_abono.equals("true")) {
            tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(true);
        } else {
            tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(false);
        }

        if (lstr_porcentaje_exonera.equals("true")) {
            tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(true);
        } else {
            tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(false);
        }

    }

    public void activarCampos(AjaxBehaviorEvent evt) {
        tab_tabla1.modificar(evt);

        String lstr_porcentaje_exonera = tab_tabla1.getValor("exoneracion");
        String lstr_porcentaje_abono = tab_tabla1.getValor("abonos");

        if (lstr_porcentaje_abono.equals("true")) {
            tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(true);
        } else {
            tab_tabla2.getColumna("PORCENTAJE_ABONO").setVisible(false);
        }

        if (lstr_porcentaje_exonera.equals("true")) {
            tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(true);
        } else {
            tab_tabla2.getColumna("PORCENTAJE_EXONERA").setVisible(false);
        }
        utilitario.addUpdate("tab_tabla2");
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
