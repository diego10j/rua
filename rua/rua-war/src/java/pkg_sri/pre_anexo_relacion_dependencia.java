/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_sri;

import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import org.primefaces.component.spinner.Spinner;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author programador2
 */
public class pre_anexo_relacion_dependencia extends Pantalla {

    private Division div_division = new Division();
    private Spinner spi_anio_desde = new Spinner();
    private Spinner spi_anio_hasta = new Spinner();
    private Boton bot_aceptar = new Boton();

    public pre_anexo_relacion_dependencia() {

        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonsNavegacion();

        Grid gri_matriz1 = new Grid();

        gri_matriz1.setColumns(2);
        gri_matriz1.getChildren().add(new Etiqueta("Año desde:"));
        gri_matriz1.getChildren().add(new Etiqueta("Año hasta:"));

        spi_anio_desde.setMin(2012);
        spi_anio_desde.setStepFactor(1);
        spi_anio_desde.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        spi_anio_hasta.setMin(2012);
        spi_anio_hasta.setStepFactor(1);
        spi_anio_hasta.setValue(utilitario.getAnio(utilitario.getFechaActual()));
        gri_matriz1.getChildren().add(spi_anio_desde);
        gri_matriz1.getChildren().add(spi_anio_hasta);
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setAjax(false);
        bot_aceptar.setMetodo("aceptar");
        gri_matriz1.setFooter(bot_aceptar);
        div_division.setId("div_division");
        div_division.dividir1(gri_matriz1);

        agregarComponente(div_division);

    }

    public void aceptar() {
        cls_rdep rdep = new cls_rdep();
        rdep.Rdep(spi_anio_desde.getValue() + "", spi_anio_hasta.getValue() + "");
       // utilitario.crearArchivo(rdep.getPath(), rdep.getNombre(), "text/xml");
        utilitario.crearArchivo(rdep.getPath());
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
