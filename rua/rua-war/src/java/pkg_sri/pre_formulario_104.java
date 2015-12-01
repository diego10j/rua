/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_sri;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Radio;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.spinner.Spinner;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_formulario_104 extends Pantalla {

    private Division div_division = new Division();
    private Combo com_mes = new Combo();
    private Spinner spi_anio = new Spinner();
    private Radio rad_tipo = new Radio();
    private Boton bot_aceptar = new Boton();
    private Texto txt_sustituye = new Texto();

    public pre_formulario_104() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        Grid gri_cuerpo = new Grid();
        Etiqueta eti = new Etiqueta("FORMULARIO 104  DECLARACIÓN DEL IMPUESTO A LA RENTA <br/> Resolución No. NAC-DGERCGC11-00425");
        eti.setStyle("font-size:14px;");
        gri_cuerpo.setHeader(eti);
        Grid gri_matriz1 = new Grid();

        gri_matriz1.setColumns(2);
        gri_matriz1.getChildren().add(new Etiqueta("Mes:"));
        gri_matriz1.getChildren().add(new Etiqueta("Año:"));

        com_mes.setCombo("SELECT alterno_gemes,nombre_gemes FROM gen_mes WHERE ide_empr=" + utilitario.getVariable("ide_empr") + "  ORDER BY alterno_gemes");
        com_mes.eliminarVacio();
        com_mes.setValue(utilitario.getMes(utilitario.getFechaActual()));

        gri_matriz1.getChildren().add(com_mes);
        spi_anio.setMin(2012);
        spi_anio.setStepFactor(1);
        spi_anio.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        gri_matriz1.getChildren().add(spi_anio);

        gri_cuerpo.getChildren().add(gri_matriz1);

        List lis_tipo = new ArrayList();
        Object obj1[] = {"O", "Original"};
        Object obj2[] = {"S", "Sustitutiva"};
        lis_tipo.add(obj1);
        lis_tipo.add(obj2);
        rad_tipo.setRadio(lis_tipo);
        rad_tipo.setValue("O");
        rad_tipo.setMetodoChange("cambioTipo");
        Fieldset fie_opciones = new Fieldset();
        fie_opciones.setLegend("Tipo de declaración");
        Grid gri_tipo = new Grid();
        gri_tipo.setColumns(3);
        gri_tipo.getChildren().add(rad_tipo);
        gri_tipo.getChildren().add(new Etiqueta("No. Formulario que sustituye:"));
        txt_sustituye.setId("txt_sustituye");
        txt_sustituye.setDisabled(true);
        gri_tipo.getChildren().add(txt_sustituye);
        fie_opciones.getChildren().add(gri_tipo);
        gri_cuerpo.getChildren().add(fie_opciones);
        gri_cuerpo.setFooter(bot_aceptar);

        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptar");
        bot_aceptar.setAjax(false);
        div_division.setId("div_division");
        div_division.dividir1(gri_cuerpo);
        agregarComponente(div_division);

    }

    public void cambioTipo() {
        if (rad_tipo.getValue().toString().equals("O")) {
            txt_sustituye.setDisabled(true);
            txt_sustituye.setValue(null);
        } else {
            txt_sustituye.setDisabled(false);
        }
        utilitario.addUpdate("txt_sustituye");
    }

    public void aceptar() {
        cls_formulario104 form104 = new cls_formulario104();
        form104.setTipoDeclaracion((String) rad_tipo.getValue());
        form104.setNumSustituye((String) txt_sustituye.getValue());
        form104.Formulario104(spi_anio.getValue() + "", com_mes.getValue() + "");
        // utilitario.crearArchivo(form104.getPath(), form104.getNombre(), "text/xml");
        utilitario.crearArchivo(form104.getPath());
    }

    @Override
    public void insertar() {
    }

    @Override
    public void guardar() {
    }

    @Override
    public void eliminar() {
    }
}
