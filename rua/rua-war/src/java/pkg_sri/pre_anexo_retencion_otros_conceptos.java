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
import org.primefaces.component.spinner.Spinner;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_anexo_retencion_otros_conceptos extends Pantalla {

    private Division div_division = new Division();
    private Combo com_mes = new Combo();
    private Spinner spi_anio = new Spinner();
    private Boton bot_aceptar = new Boton();

    public pre_anexo_retencion_otros_conceptos() {
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        Grid gri_matriz1 = new Grid();
        gri_matriz1.setColumns(2);
        gri_matriz1.getChildren().add(new Etiqueta("Mes:"));
        gri_matriz1.getChildren().add(new Etiqueta("Año:"));

        com_mes.setCombo("SELECT ide_gemes,nombre_gemes FROM gen_mes WHERE ide_empr=" + utilitario.getVariable("ide_empr") + "  ORDER BY ide_gemes ");
        com_mes.eliminarVacio();
        com_mes.setValue(utilitario.getMes(utilitario.getFechaActual()));

        gri_matriz1.getChildren().add(com_mes);
        spi_anio.setMin(2012);
        spi_anio.setStepFactor(1);
        spi_anio.setValue(utilitario.getAnio(utilitario.getFechaActual()));

        gri_matriz1.getChildren().add(spi_anio);

        Grid gri_final = new Grid();
        gri_final.setColumns(1);
        gri_final.getChildren().add(gri_matriz1);
        gri_final.setFooter(bot_aceptar);

        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptar");
        bot_aceptar.setAjax(false);
        div_division.setId("div_division");
        div_division.dividir1(gri_final);
        agregarComponente(div_division);

    }

    public void aceptar() {
        cls_reoc anexo = new cls_reoc();
        anexo.Reoc(spi_anio.getValue() + "", com_mes.getValue() + "");
        //utilitario.crearArchivo(anexo.getPath(), anexo.getNombre(), "text/xml");
        utilitario.crearArchivo(anexo.getPath());
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
