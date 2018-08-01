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
        tab_tabla1.setTabla("rec_concepto", "ide_concepto_recon", 1);
        //tab_tabla1.setCampoOrden("des_concepto");
        //tab_tabla1.getColumna("ide_tipo_documento").setCombo("rec_tipo_documento", "ide_tipo_documento", "detalle", "");
        //tab_tabla1.getColumna("abonos").setMetodoChange("activarCampos");
        //  tab_tabla1.getColumna("urbano").setValorDefecto("true");
        //tab_tabla1.getColumna("urbano").setMetodoChange("cambiaUrbano");
       /* tab_tabla1.getColumna("rural").setMetodoChange("cambiaRural");
        tab_tabla1.getColumna("uso").setMetodoChange("cambiaUso");
        tab_tabla1.getColumna("exoneracion").setMetodoChange("cambiaExoneracion");
        tab_tabla1.getColumna("dependencia").setMetodoChange("cambiaDependencia");*/
        
     //   tab_tabla1.setRows(10);
  //      tab_tabla1.onSelect("seleccionar_tabla1");
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();

        //configuracion de la tabla impuesto (detalle)
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("rec_forma_impuesto", "ide_forma_impuesto_refim", 2);
        tab_tabla2.setCampoOrden("ide_impuesto_reimp");
        tab_tabla2.getColumna("ide_impuesto_reimp").setCombo("rec_impuesto", "ide_impuesto_reimp", "des_impuesto_reimp,porcentaje_reimp,valor_reimp", "");
        tab_tabla2.getColumna("ide_impuesto_reimp").setAutoCompletar();
        tab_tabla2.getColumna("porcentaje_abono_refim").setVisible(false);
        tab_tabla2.getColumna("porcentaje_exonera_refim").setVisible(false);
        tab_tabla2.setCampoForanea("ide_concepto_recon");



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
            tab_tabla2.insertar();
            //String lstr_uso = tab_tabla1.getValor("uso");
          /*  if (!lstr_uso.equalsIgnoreCase("true")) {
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
            }*/
        }
    }


    public void guardar() {
      if (tab_tabla1.isFocus()){
          tab_tabla1.guardar();
      }
      else if (tab_tabla2.isFocus()){
         tab_tabla2.guardar(); 
      } 
            guardarPantalla();

    }

    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
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
