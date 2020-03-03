/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_inventario;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import servicios.inventario.ServicioInventario;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author CRISTIAN VEGA
 */
public class pre_reportes extends Pantalla {

    private Tabla tab_tabla1 = new Tabla();
    private Tabla tab_tabla2 = new Tabla();

    private final Calendario cal_fecha1 = new Calendario();
    private final Calendario cal_fecha2 = new Calendario();
    private Combo com_reporte = new Combo();
    private VisualizarPDF vipdf_reportes = new VisualizarPDF();
    private Map p_parametro = new HashMap();

    private SeleccionTabla sel_tipo_trans = new SeleccionTabla();

    @EJB
    private final ServicioInventario ser_inventario = (ServicioInventario) utilitario.instanciarEJB(ServicioInventario.class);

    public pre_reportes() {

        cal_fecha1.setId("cal_fecha1");
        bar_botones.agregarComponente(new Etiqueta("FECHA INICIAL"));
        bar_botones.agregarComponente(cal_fecha1);

        cal_fecha2.setId("cal_fecha2");
        bar_botones.agregarComponente(new Etiqueta("FECHA FINAL"));
        bar_botones.agregarComponente(cal_fecha2);

        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);

        Boton bot_seleccion = new Boton();
        bot_seleccion.setIcon("ui-icon-print");
        bot_seleccion.setValue("SELECIONE TIPO TRANSACCIÓN");
        bot_seleccion.setMetodo("dibujaTipoTransaccion");
        bar_botones.agregarBoton(bot_seleccion);

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_inventario.getDetalleTransacciones("1-1-2000", "1-1-2000", "-1"));
        tab_tabla1.setRecuperarLectura(true);
        tab_tabla1.setCampoPrimaria("ide_incci");
        tab_tabla1.agregarRelacion(tab_tabla2);     
        tab_tabla1.imprimirSql();
        tab_tabla1.dibujar();

        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setId("pat_panel1");
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("inv_det_comp_inve", "ide_indci", 2);
        tab_tabla2.getColumna("ide_indci").setNombreVisual("CODIGO");
        tab_tabla2.getColumna("ide_inarti").setNombreVisual("COD. ARTI");
        tab_tabla2.getColumna("cantidad_indci").setNombreVisual("CANTIDAD");
        tab_tabla2.getColumna("precio_indci").setNombreVisual("PRECIO");
        tab_tabla2.getColumna("valor_indci").setNombreVisual("VALOR");

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setId("pat_panel2");
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.dividir2(pat_panel1, pat_panel2, "50%", "H");
        agregarComponente(div_division);

        sel_tipo_trans.setId("sel_tipo_trans");
        sel_tipo_trans.setTitle("TIPO TRANSACCIÓN");
        sel_tipo_trans.setSeleccionTabla("select ide_intti, nombre_intti, nombre_intci from inv_tip_tran_inve a\n"
                + "left join inv_tip_comp_inve b on a.ide_intci=b.ide_intci\n"
                + "order by nombre_intci desc, nombre_intti", "ide_intti");
        sel_tipo_trans.setWidth("80%");
        sel_tipo_trans.setHeight("70%");
        //sel_tipo_trans.setRadio();
        sel_tipo_trans.getBot_aceptar().setMetodo("selecioneTransaccion");
        agregarComponente(sel_tipo_trans);

    }

    public void limpiar() {
        cal_fecha1.limpiar();
        cal_fecha2.limpiar();
        tab_tabla1.limpiar();
    }

    public void selecioneTransaccion() {
        String seleccionados = sel_tipo_trans.getSeleccionados();
        if (seleccionados != null) {
            //String seleccionados = sel_tipo_trans.getSeleccionados();
            tab_tabla1.setSql(ser_inventario.getDetalleTransacciones(cal_fecha1.getFecha(), cal_fecha2.getFecha(), seleccionados));
            tab_tabla1.ejecutarSql();
            tab_tabla2.ejecutarValorForanea(tab_tabla1.getValorSeleccionado());
            utilitario.addUpdate("tab_tabla1, tabla2");     
            sel_tipo_trans.cerrar();
        } else {
            utilitario.agregarMensajeInfo("Advertencia", "Eliga por lo menos un registro");
        }
    }

    public void dibujaTipoTransaccion() {
        sel_tipo_trans.dibujar();
    }

    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public Combo getCom_reporte() {
        return com_reporte;
    }

    public void setCom_reporte(Combo com_reporte) {
        this.com_reporte = com_reporte;
    }

    public VisualizarPDF getVipdf_reportes() {
        return vipdf_reportes;
    }

    public void setVipdf_reportes(VisualizarPDF vipdf_reportes) {
        this.vipdf_reportes = vipdf_reportes;
    }

    public SeleccionTabla getSel_tipo_trans() {
        return sel_tipo_trans;
    }

    public void setSel_tipo_trans(SeleccionTabla sel_tipo_trans) {
        this.sel_tipo_trans = sel_tipo_trans;
    }
}
