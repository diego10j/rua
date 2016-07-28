/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.spinner.Spinner;
import pkg_sri.cls_formulario103;
import pkg_sri.cls_formulario104;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_formularios extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();

    private Tabla tab_tabla1;

    private Combo com_mes = new Combo();
    private Spinner spi_anio = new Spinner();
    private Radio rad_tipo = new Radio();
    private Texto txt_sustituye = new Texto();

    public pre_formularios() {
        bar_botones.quitarBotonsNavegacion();
        //bar_botones.agregarReporte(); 

        mep_menu.setMenuPanel("FORMULARIOS SRI", "25%");
        mep_menu.agregarItem("Formulario 103 ", "dibujarF103", "ui-icon-note"); //1
        mep_menu.agregarItem("Formulario 104", "dibujarF104", "ui-icon-note");//2
        mep_menu.agregarItem("Anexo Transaccional (ATS)", "dibujarATS", "ui-icon-note"); //3
        mep_menu.agregarItem("Anexo Relacion de Dependencia (RDEP)", "dibujarRDEP", "ui-icon-note");//4
        mep_menu.agregarSubMenu("CONFIGURACIONES");
        mep_menu.agregarItem("Fecha de Pago Formularios", "dibujarFechaPagoFormularios", "ui-icon-wrench");//5
        mep_menu.agregarItem("Interes por Mora", "dibujarInteresMora", "ui-icon-wrench");//6
        mep_menu.agregarSubMenu("CATALOGOS");
        mep_menu.agregarItem("Tipo Sustento Tributario", "dibujarTipoSustento", "ui-icon-bookmark");//8
        agregarComponente(mep_menu);
    }

    public void dibujarF103() {

        Grid gri_cuerpo = new Grid();
        Etiqueta eti = new Etiqueta("FORMULARIO 103  DECLARACIÓN DE RETENCIONES EN LA FUENTE DEL IMPUESTO A LA RENTA <br/> Resolución No. NAC-DGERCGC11-00425");
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
        Boton bot_aceptar = new Boton();
        gri_cuerpo.setFooter(bot_aceptar);

        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarF103");
        bot_aceptar.setAjax(false);
        mep_menu.dibujar(1, "FORMULARIO 103", gri_cuerpo);
    }

    public void aceptarF103() {
        cls_formulario103 form103 = new cls_formulario103();
        form103.setTipoDeclaracion((String) rad_tipo.getValue());
        form103.setNumSustituye((String) txt_sustituye.getValue());
        form103.Formulario103(spi_anio.getValue() + "", com_mes.getValue() + "");
        // utilitario.crearArchivo(form103.getPath(), form103.getNombre(), "text/xml");
        utilitario.crearArchivo(form103.getPath());
    }

    public void dibujarF104() {
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
        Boton bot_aceptar = new Boton();
        gri_cuerpo.setFooter(bot_aceptar);
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarF104");
        bot_aceptar.setAjax(false);
        mep_menu.dibujar(2, "FORMULARIO 104", gri_cuerpo);
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

    public void aceptarF104() {
        cls_formulario104 form104 = new cls_formulario104();
        form104.setTipoDeclaracion((String) rad_tipo.getValue());
        form104.setNumSustituye((String) txt_sustituye.getValue());
        form104.Formulario104(spi_anio.getValue() + "", com_mes.getValue() + "");
        // utilitario.crearArchivo(form104.getPath(), form104.getNombre(), "text/xml");
        utilitario.crearArchivo(form104.getPath());
    }

    public void dibujarATS() {

    }

    public void dibujarRDEP() {

    }

    public void dibujarFechaPagoFormularios() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sri_fecha_pago_formulario", "ide_srfpf", 5);
        tab_tabla1.getColumna("ide_srfpf").setVisible(false);
        tab_tabla1.getColumna("ide_gemes").setCombo("gen_mes", "ide_gemes", "nombre_gemes", "");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(5, "FECHA DE PAGO FORMULARIOS", pat_panel);
    }

    public void dibujarInteresMora() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sri_interes_mora", "ide_srimo", 1);
        tab_tabla1.getColumna("ide_srimo").setVisible(false);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(6, "INTERES POR MORA", pat_panel);
    }

    public void dibujarImpuestoRenta() {

    }

    public void dibujarTipoSustento() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sri_tipo_sustento_tributario", "ide_srtst", 8);
        tab_tabla1.getColumna("ide_srtst").setVisible(false);
        tab_tabla1.setCampoOrden("alterno_srtst");
        tab_tabla1.getColumna("nombre_srtst").setControl("AreaTexto");
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(8, "TIPO DE SUSTENTO TRIBUTARIO", pat_panel);
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

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

}
