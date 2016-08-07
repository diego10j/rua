/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import framework.aplicacion.Fila;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import framework.reportes.ReporteDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.spinner.Spinner;
import pkg_sri.cls_anexo_transaccional;
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
    private Radio rad_opciones = new Radio();
    private VisualizarPDF vpdf_ver = new VisualizarPDF();
    private Map parametros = new HashMap();
    private cls_formulario103 form103 = new cls_formulario103();
    private cls_formulario104 form104 = new cls_formulario104();

    public pre_formularios() {
        bar_botones.quitarBotonsNavegacion();
        //bar_botones.agregarReporte(); 

        mep_menu.setMenuPanel("FORMULARIOS SRI", "25%");
        mep_menu.agregarItem("Formulario 103 ", "dibujarF103", "ui-icon-note"); //1
        mep_menu.agregarItem("Formulario 104", "dibujarF104", "ui-icon-note");//2
        mep_menu.agregarItem("Anexo Transaccional (ATS)", "dibujarATS", "ui-icon-note"); //3
        mep_menu.agregarItem("Anexo Relacion de Dependencia (RDEP)", "dibujarRDEP", "ui-icon-note");//4
        mep_menu.agregarSubMenu("CONFIGURACIONES");
        mep_menu.agregarItem("Reporte Formulario 103", "dibujarRep103", "ui-icon-wrench");//7
        mep_menu.agregarItem("Reporte Formulario 104", "dibujarRep104", "ui-icon-wrench");//9
        mep_menu.agregarItem("Fecha de Pago Formularios", "dibujarFechaPagoFormularios", "ui-icon-wrench");//5
        mep_menu.agregarItem("Interes por Mora", "dibujarInteresMora", "ui-icon-wrench");//6
        mep_menu.agregarSubMenu("CATALOGOS");
        mep_menu.agregarItem("Tipo Sustento Tributario", "dibujarTipoSustento", "ui-icon-bookmark");//8
        agregarComponente(mep_menu);
        vpdf_ver.setTitle("FORMULARIOS");
        vpdf_ver.setId("vpdf_ver");
        agregarComponente(vpdf_ver);
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
        gri_tipo.getChildren().add(bot_aceptar);
        bot_aceptar.setValue("Generar");
        bot_aceptar.setMetodo("aceptarF103");

        gri_cuerpo.getChildren().add(new Separator());
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql("select codigo_srrep as numero,nombre_srrep as descripcion, valor_srrep as value from sri_reporte where numero_srrep='-1' order by codigo_srrep");
        tab_tabla1.getColumna("value").setNombreVisual("VALOR");
        tab_tabla1.getColumna("value").alinearDerecha();
        tab_tabla1.setLectura(true);
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(200);
        tab_tabla1.setRows(500);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        gri_cuerpo.getChildren().add(pat_panel);

        Boton bot_imprimir = new Boton();
        bot_imprimir.setValue("Imprimir");
        bot_imprimir.setMetodo("generarReporte");

        Boton bot_descargar = new Boton();
        bot_descargar.setValue("Descargar");
        bot_descargar.setAjax(false);
        bot_descargar.setMetodo("descargarF103");

        Grid gri1 = new Grid();
        gri1.setColumns(2);
        gri1.getChildren().add(bot_imprimir);
        gri1.getChildren().add(bot_descargar);

        pat_panel.setFooter(gri1);

        mep_menu.dibujar(1, "FORMULARIO 103", gri_cuerpo);
    }

    public void aceptarF103() {

        form103.setTipoDeclaracion((String) rad_tipo.getValue());
        form103.setNumSustituye((String) txt_sustituye.getValue());
        String xml = form103.Formulario103(spi_anio.getValue() + "", com_mes.getValue() + "");
        parametros = new HashMap();
        parametros.put("101", getValorEtiqueta(xml, "101"));
        parametros.put("102", getValorEtiqueta(xml, "102"));
        parametros.put("201", getValorEtiqueta(xml, "201"));
        parametros.put("202", getValorEtiqueta(xml, "202"));
        parametros.put("impuesto", "IMPUESTO A LA RENTA");
        parametros.put("periodo", "MES");

        tab_tabla1.setSql("select codigo_srrep as numero,nombre_srrep as descripcion, valor_srrep as value from sri_reporte where numero_srrep='103' order by codigo_srrep");
        tab_tabla1.ejecutarSql();
        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            String codsri = tab_tabla1.getValor(i, "numero");
            String value = getValorEtiqueta(xml, codsri);
            if (value == null || value.isEmpty() || value.equals("0.00")) {
                value = null;
                tab_tabla1.getFila(i).setVisible(false);
            }
            tab_tabla1.setValor(i, "value", value);
        }

        Iterator<Fila> iter = tab_tabla1.getFilas().listIterator();
        while (iter.hasNext()) {
            String value = String.valueOf(iter.next().getCampos()[tab_tabla1.getNumeroColumna("value")]);
            if (value == null || value.isEmpty()) {
                iter.remove();
            }
        }
    }

    public void descargarF103() {
        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeInfo("Debe Generar el Formulario", "");
            return;
        }
        utilitario.crearArchivo(form103.getPath());
    }

    public void dibujarF104() {
        Grid gri_cuerpo = new Grid();
        Etiqueta eti = new Etiqueta("FORMULARIO 104  DECLARACIÓN DEL IMPUESTO AL VALOR AGREGADO <br/> Resolución N° NAC-DGERCGC16-00000210");
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
        gri_tipo.getChildren().add(bot_aceptar);
        bot_aceptar.setValue("Generar");
        bot_aceptar.setMetodo("aceptarF104");

        gri_cuerpo.getChildren().add(new Separator());
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql("select codigo_srrep as numero,nombre_srrep as descripcion, valor_srrep as value from sri_reporte where numero_srrep='-1' order by codigo_srrep");
        tab_tabla1.getColumna("value").setNombreVisual("VALOR");
        tab_tabla1.getColumna("value").alinearDerecha();
        tab_tabla1.setLectura(true);
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(200);
        tab_tabla1.setRows(500);
        tab_tabla1.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        gri_cuerpo.getChildren().add(pat_panel);

        Boton bot_imprimir = new Boton();
        bot_imprimir.setValue("Imprimir");
        bot_imprimir.setMetodo("generarReporte");

        Boton bot_descargar = new Boton();
        bot_descargar.setValue("Descargar");
        bot_descargar.setAjax(false);
        bot_descargar.setMetodo("descargarF104");

        Grid gri1 = new Grid();
        gri1.setColumns(2);
        gri1.getChildren().add(bot_imprimir);
        gri1.getChildren().add(bot_descargar);

        pat_panel.setFooter(gri1);

        mep_menu.dibujar(2, "FORMULARIO 104", gri_cuerpo);
    }

    public void aceptarF104() {

        form104.setTipoDeclaracion((String) rad_tipo.getValue());
        form104.setNumSustituye((String) txt_sustituye.getValue());
        String xml = form104.Formulario104(spi_anio.getValue() + "", com_mes.getValue() + "");

        parametros = new HashMap();
        parametros.put("101", getValorEtiqueta(xml, "101"));
        parametros.put("102", getValorEtiqueta(xml, "102"));
        parametros.put("201", getValorEtiqueta(xml, "201"));
        parametros.put("202", getValorEtiqueta(xml, "202"));
        parametros.put("impuesto", "IMPUESTO AL VALOR AGREGADO");
        parametros.put("periodo", "MES");

        tab_tabla1.setSql("select codigo_srrep as numero,nombre_srrep as descripcion, valor_srrep as value from sri_reporte where numero_srrep='104' order by codigo_srrep");
        tab_tabla1.ejecutarSql();
        for (int i = 0; i < tab_tabla1.getTotalFilas(); i++) {
            String codsri = tab_tabla1.getValor(i, "numero");
            String value = getValorEtiqueta(xml, codsri);
            if (value == null || value.isEmpty() || value.equals("0.00")) {
                value = null;
                tab_tabla1.getFila(i).setVisible(false);
            }
            tab_tabla1.setValor(i, "value", value);
        }

        Iterator<Fila> iter = tab_tabla1.getFilas().listIterator();
        while (iter.hasNext()) {
            String value = String.valueOf(iter.next().getCampos()[tab_tabla1.getNumeroColumna("value")]);
            if (value == null || value.isEmpty()) {
                iter.remove();
            }
        }
    }

    public void descargarF104() {
        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeInfo("Debe Generar el Formulario", "");
            return;
        }
        utilitario.crearArchivo(form104.getPath());
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

    public void dibujarATS() {
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

        List lista = new ArrayList();
        Object fila1[] = {
            "1", "Retenciones en Compras + Ventas + Importaciones"
        };
        Object fila2[] = {
            "2", "Retenciones en Compras"
        };
        Object fila3[] = {
            "3", "Retenciones en Ventas"
        };
        Object fila4[] = {
            "4", "Retenciones en Importaciones"
        };
        lista.add(fila1);
        lista.add(fila2);
        lista.add(fila3);
        lista.add(fila4);

        rad_opciones.setRadio(lista);
        rad_opciones.setVertical();
        rad_opciones.setValue("1");
        Fieldset fie_opciones = new Fieldset();
        fie_opciones.setLegend("Opciones de Anexo");
        fie_opciones.getChildren().add(rad_opciones);
        gri_matriz1.getChildren().add(fie_opciones);

        Grid gri_final = new Grid();
        gri_final.setColumns(1);
        gri_final.getChildren().add(gri_matriz1);
        gri_final.getChildren().add(fie_opciones);
        Boton bot_aceptar = new Boton();
        gri_final.setFooter(bot_aceptar);

        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setAjax(false);
        bot_aceptar.setMetodo("aceptarATS");
        mep_menu.dibujar(4, "ANEXO TRANSACCIONAL", gri_final);
    }

    public void aceptarATS() {
        cls_anexo_transaccional anexo = new cls_anexo_transaccional();
        anexo.setOpcionAnexo((String) rad_opciones.getValue());
        anexo.AnexoTransaccional(spi_anio.getValue() + "", com_mes.getValue() + "");
        //  utilitario.crearArchivo(anexo.getPath(), anexo.getNombre(), "text/xml");
        utilitario.crearArchivo(anexo.getPath());
    }

    public void dibujarRDEP() {
        Grupo gru_grupo = new Grupo();
        mep_menu.dibujar(4, "ANEXO RELACIÓN DE DEPENDENCIA", gru_grupo);
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
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(8, "TIPO DE SUSTENTO TRIBUTARIO", pat_panel);
    }

    public void dibujarRep103() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sri_reporte", "IDE_SRREP", 7);
        tab_tabla1.getColumna("IDE_SRREP").setVisible(false);
        tab_tabla1.getColumna("NUMERO_SRREP").setVisible(false);
        tab_tabla1.setCondicion("NUMERO_SRREP='103'");
        tab_tabla1.setCampoOrden("CODIGO_SRREP");
        tab_tabla1.setHeader("DISEÑO FORMULARIO 103");
        tab_tabla1.dibujar();
        tab_tabla1.setRows(15);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(7, "CONFIGURACIÓN DE REPORTE FORMULARIO 103", pat_panel);
    }

    public void dibujarRep104() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("sri_reporte", "IDE_SRREP", 9);
        tab_tabla1.getColumna("IDE_SRREP").setVisible(false);
        tab_tabla1.getColumna("NUMERO_SRREP").setVisible(false);
        tab_tabla1.setCondicion("NUMERO_SRREP='104'");
        tab_tabla1.setCampoOrden("CODIGO_SRREP");
        tab_tabla1.dibujar();
        tab_tabla1.setRows(15);
        tab_tabla1.setHeader("DISEÑO FORMULARIO 104");
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        mep_menu.dibujar(9, "CONFIGURACIÓN DE REPORTE FORMULARIO 104", pat_panel);
    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == 7) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.insertar();
            }
        } else if (mep_menu.getOpcion() == 9) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.insertar();
            }
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 7) {
            if (tab_tabla1.isFocus()) {
                if (tab_tabla1.guardar()) {
                    guardarPantalla();
                }
            }
        } else if (mep_menu.getOpcion() == 9) {
            if (tab_tabla1.isFocus()) {
                if (tab_tabla1.guardar()) {
                    guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 7) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.eliminar();
            }
        } else if (mep_menu.getOpcion() == 9) {
            if (tab_tabla1.isFocus()) {
                tab_tabla1.eliminar();
            }
        }
    }

    public void generarReporte() {
        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeInfo("Debe Generar el Formulario", "");
            return;
        }
        ReporteDataSource rds = new ReporteDataSource(tab_tabla1);
        vpdf_ver.setVisualizarPDF("rep_sri/rep_formularios.jasper", parametros, rds);
        vpdf_ver.dibujar();
    }

    private String getValorEtiqueta(String cadenaXML, String etiqueta) {
        String str_valor = "";
        try {
            String str_etiqueta1 = "<campo numero=\"" + etiqueta + "\">";
            String str_etiqueta2 = "</campo>";
            int int1 = cadenaXML.indexOf(str_etiqueta1);
            if (int1 != -1) {
                str_valor = cadenaXML.substring(int1 + str_etiqueta1.length());
                str_valor = str_valor.substring(0, str_valor.indexOf(str_etiqueta2));
                str_valor = str_valor.trim();
            }
        } catch (Exception e) {
        }
        return str_valor;
    }

    public VisualizarPDF getVpdf_ver() {
        return vpdf_ver;
    }

    public void setVpdf_ver(VisualizarPDF vpdf_ver) {
        this.vpdf_ver = vpdf_ver;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

}
