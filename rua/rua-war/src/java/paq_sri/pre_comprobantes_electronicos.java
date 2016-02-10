/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sri;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.VisualizarPDF;
import javax.ejb.EJB;
import servicios.sri.ServicioComprobatesElectronicos;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author dfjacome
 */
public class pre_comprobantes_electronicos extends Pantalla {

    @EJB
    private final ServicioComprobatesElectronicos ser_comprobante = (ServicioComprobatesElectronicos) utilitario.instanciarEJB(ServicioComprobatesElectronicos.class);

    private Tabla tab_facturas = new Tabla();

    private final Combo com_estados = new Combo();

    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();

    private VisualizarPDF vipdf_comprobante = new VisualizarPDF();

    public pre_comprobantes_electronicos() {
        bar_botones.limpiar();

        com_estados.setCombo("SELECT * FROM sri_estado_comprobante order by nombre_sresc");
        com_estados.setMetodo("actualizarConsulta");

        bar_botones.agregarComponente(new Etiqueta("ESTADO COMPROBANTE:"));
        bar_botones.agregarComponente(com_estados);

        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);
        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarConsulta");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarBoton(bot_consultar);

        bar_botones.agregarSeparador();

        Boton bot_pdf = new Boton();
        bot_pdf.setValue("Ver PDF");
        bot_pdf.setMetodo("generarPDF");
        bar_botones.agregarBoton(bot_pdf);

        Boton bot_xml = new Boton();
        bot_xml.setValue("Descargar XML");
        bot_xml.setMetodo("descargarXML");
        bot_xml.setAjax(false);
        bar_botones.agregarBoton(bot_xml);

        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_facturas.setId("tab_facturas");
        tab_facturas.setIdCompleto("tab_tabulador:tab_facturas");
        tab_facturas.setSql(ser_comprobante.getSqlFacturasElectronicas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados.getValue())));

        tab_facturas.getColumna("ide_srcom").setVisible(false);
        tab_facturas.getColumna("ide_cccfa").setVisible(false);
        tab_facturas.setLectura(true);
        tab_facturas.setRows(15);
        tab_facturas.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_facturas);
        tab_tabulador.agregarTab("FACTURAS", pat_panel);
        agregarComponente(tab_tabulador);
        tab_tabulador.agregarTab("COMPROBANTES DE RETENCION", null);
        tab_tabulador.agregarTab("NOTAS DE DEBITO", null);
        tab_tabulador.agregarTab("NOTAS DE CREDITO", null);
        tab_tabulador.agregarTab("GUIAS DE REMISIÓN", null);

        vipdf_comprobante.setId("vipdf_comprobante");
        agregarComponente(vipdf_comprobante);
    }

    public void actualizarConsulta() {
        tab_facturas.setSql(ser_comprobante.getSqlFacturasElectronicas(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_estados.getValue())));
        tab_facturas.ejecutarSql();
    }

    public void generarPDF() {
        if (tab_facturas.getValorSeleccionado() != null) {
            ser_comprobante.generarPDF(tab_facturas.getValorSeleccionado());
            vipdf_comprobante.setVisualizarPDFUsuario();
            vipdf_comprobante.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Comprobante", "");
        }
    }

    public void descargarXML() {
        if (tab_facturas.getValorSeleccionado() != null) {
            ser_comprobante.generarXML(tab_facturas.getValorSeleccionado());
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Comprobante", "");
        }
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

    public Tabla getTab_facturas() {
        return tab_facturas;
    }

    public void setTab_facturas(Tabla tab_facturas) {
        this.tab_facturas = tab_facturas;
    }

    public VisualizarPDF getVipdf_comprobante() {
        return vipdf_comprobante;
    }

    public void setVipdf_comprobante(VisualizarPDF vipdf_comprobante) {
        this.vipdf_comprobante = vipdf_comprobante;
    }

}
