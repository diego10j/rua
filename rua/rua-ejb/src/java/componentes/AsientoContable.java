/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.VisualizarPDF;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.contabilidad.TipoAsientoEnum;
import servicios.tesoreria.ServicioTesoreria;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
public class AsientoContable extends Dialogo {

    private final Utilitario utilitario = new Utilitario();
    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    @EJB
    private final ServicioComprobanteContabilidad ser_comprobante = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioTesoreria ser_tesoreria = (ServicioTesoreria) utilitario.instanciarEJB(ServicioTesoreria.class);

    private final Etiqueta eti_suma_debe = new Etiqueta();
    private final Etiqueta eti_suma_haber = new Etiqueta();
    private final Etiqueta eti_suma_diferencia = new Etiqueta();
    private final Map<String, String> parametros;

    private AutoCompletar aut_asiento_tipo;

    private Tabla tab_cabe_asiento;
    private Tabla tab_deta_asiento;
    private Tabla tab_presupuesto;

    private final VisualizarPDF vpd_asiento = new VisualizarPDF();

    //Asiento de factura ventas cxc
    private String relacion = null;
    //Tipo Asiento que se va a generar
    private String tipo = null;

    private int reporteComprobante = 1;// reporte comprobante
    //Para ver un asiento contable
    private String ide_cnccc = null;

    private Dialogo dia_asociacionPre;
    private Tabla tab_sel_aso_tab_seleccion;
    private Texto tex_valor_pre;

    public AsientoContable() {
        parametros = utilitario.getVariables("p_con_lugar_debe", "p_con_lugar_haber", "p_con_usa_presupuesto",
                "p_con_tipo_comprobante_diario", "p_con_tipo_comprobante_ingreso",
                "p_con_tipo_comprobante_egreso", "p_con_beneficiario_empresa", "p_tes_tran_cheque");
        this.setWidth("95%");
        this.setHeight("95%");
        this.setTitle("ASIENTO CONTABLE");
        this.setResizable(false);
        this.setDynamic(false);
        vpd_asiento.setId("vpd_asiento");
        utilitario.getPantalla().getChildren().add(vpd_asiento);

        if (isPresupuesto()) {
            dia_asociacionPre = new Dialogo();
            dia_asociacionPre.setId("dia_asociacionPre");
            dia_asociacionPre.setTitle("ASOCIACION PRESUPUESTARIA");
            dia_asociacionPre.setWidth("75%");
            dia_asociacionPre.setHeight("60%");
            utilitario.getPantalla().getChildren().add(dia_asociacionPre);
        }
    }

    //Asigna un asiento contable para dibujarlo
    public void setAsientoContable(String ide_cnccc) {
        this.ide_cnccc = ide_cnccc;
    }

    private void dibujarNuevoAsiento() {
        Grid contenido = new Grid();
        //Asientos tipo
        ide_cnccc = null;
        aut_asiento_tipo = new AutoCompletar();
        aut_asiento_tipo.setRuta("pre_index.clase." + getId());
        aut_asiento_tipo.setId("aut_asiento_tipo");
        aut_asiento_tipo.setAutocompletarContenido();
        aut_asiento_tipo.setAutoCompletar(ser_configuracion.getSqlAsientosTipo());
        aut_asiento_tipo.setSize(getAnchoPanel() / 7);
        aut_asiento_tipo.setMetodoChangeRuta("pre_index.clase." + getId() + ".cargarCuentasAsientoTipo");
        Grid gri_as_tipo = new Grid();
        gri_as_tipo.setColumns(2);
        gri_as_tipo.getChildren().add(new Etiqueta("<strong>ASIENTO TIPO :</strong>"));
        gri_as_tipo.getChildren().add(aut_asiento_tipo);

        contenido.getChildren().add(gri_as_tipo);

        tab_cabe_asiento = new Tabla();
        tab_deta_asiento = new Tabla();
        tab_cabe_asiento.setId("tab_cabe_asiento");
        tab_cabe_asiento.setRuta("pre_index.clase." + getId());
        ser_comprobante.configurarTablaCabeceraComprobante(tab_cabe_asiento);
        tab_cabe_asiento.agregarRelacion(tab_deta_asiento);
        tab_cabe_asiento.getGrid().setColumns(6);
        tab_cabe_asiento.getColumna("ide_cnccc").setNombreVisual("TRANSACCIÓN");
        tab_cabe_asiento.getColumna("numero_cnccc").setOrden(2);
        tab_cabe_asiento.getColumna("numero_cnccc").setLectura(true);
        tab_cabe_asiento.getColumna("numero_cnccc").setNombreVisual("NUM. COMPROBANTE");
        tab_cabe_asiento.getColumna("fecha_trans_cnccc").setOrden(4);
        tab_cabe_asiento.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_cabe_asiento.getColumna("fecha_trans_cnccc").setRequerida(true);
        tab_cabe_asiento.getColumna("ide_geper").setOrden(3);
        tab_cabe_asiento.getColumna("ide_cntcm").setOrden(5);
        tab_cabe_asiento.getColumna("ide_cntcm").setCombo(ser_comprobante.getSqlTiposComprobante());
        tab_cabe_asiento.getColumna("ide_cntcm").setNombreVisual("TIPO DE COMPROBANTE");
        tab_cabe_asiento.getColumna("ide_cntcm").setRequerida(true);
        tab_cabe_asiento.getColumna("ide_cntcm").setVisible(true);
        tab_cabe_asiento.getColumna("ide_geper").setNombreVisual("BENEFICIARIO");
        tab_cabe_asiento.getColumna("ide_usua").setVisible(false);
        tab_cabe_asiento.getColumna("observacion_cnccc").setOrden(6);
        tab_cabe_asiento.getColumna("observacion_cnccc").setRequerida(true);
        tab_cabe_asiento.getColumna("observacion_cnccc").setNombreVisual("OBSERVACIÓN");
        tab_cabe_asiento.setMostrarNumeroRegistros(false);
        tab_cabe_asiento.getColumna("ide_modu").setVisible(false);
        tab_cabe_asiento.getColumna("ide_cneco").setVisible(false);
        tab_cabe_asiento.getColumna("observacion_cnccc").setControl("Texto");
        tab_cabe_asiento.getColumna("numero_cnccc").setControl("Texto");

        tab_cabe_asiento.dibujar();
        tab_cabe_asiento.insertar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_cabe_asiento);
        pat_panel1.getMenuTabla().setRendered(false);
        contenido.getChildren().add(pat_panel1);

        tab_deta_asiento.setId("tab_deta_asiento");
        tab_deta_asiento.setRuta("pre_index.clase." + getId());
        ser_comprobante.configurarTablaDetalleComprobante(tab_deta_asiento);
        tab_deta_asiento.setTabla("con_det_comp_cont", "ide_cndcc", 2);
        tab_deta_asiento.getColumna("valor_cndcc").setMetodoChangeRuta("pre_index.clase." + getId() + ".ingresaCantidad");
        tab_deta_asiento.getColumna("ide_cnlap").setMetodoChangeRuta("pre_index.clase." + getId() + ".seleccionarLugarAplica");
        tab_deta_asiento.getColumna("ide_cndpc").setNombreVisual("CUENTA CONTABLE");
        tab_deta_asiento.getColumna("observacion_cndcc").setNombreVisual("OBSERVACIÓN");
        tab_deta_asiento.getColumna("VALOR_cndcc").setNombreVisual("VALOR");
        tab_deta_asiento.getColumna("ide_cnlap").setNombreVisual("LUGAR APLICA");

        tab_deta_asiento.setScrollable(true);
        tab_deta_asiento.setScrollWidth(getAnchoPanel() - 15);
        if (!isPresupuesto()) {
            tab_deta_asiento.setScrollHeight(getAltoPanel() - 200); //175 sin presupuesto
        } else {
            tab_deta_asiento.setScrollHeight(getAltoPanel() - 275); //300 con presupuesto                     
        }
        tab_deta_asiento.setRows(100);
        tab_deta_asiento.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_deta_asiento);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel2.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel2.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel2.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");
        pat_panel2.setStyle("width:100%;height:100%;overflow: auto;display: block;");
        contenido.getChildren().add(pat_panel2);

        PanelGrid gri_totales = new PanelGrid();
        gri_totales.setId("gri_totales");
        gri_totales.setColumns(3);
        eti_suma_debe.setValue("TOTAL DEBE : " + utilitario.getFormatoNumero("0"));
        eti_suma_debe.setStyle("font-size: 14px;font-weight: bold");
        eti_suma_haber.setValue("TOTAL HABER " + utilitario.getFormatoNumero("0"));
        eti_suma_haber.setStyle("font-size: 14px;font-weight: bold");
        eti_suma_diferencia.setValue("DIFERENCIA " + utilitario.getFormatoNumero("0"));
        eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
        gri_totales.setStyle("width:99%");
        gri_totales.getChildren().add(eti_suma_diferencia);
        gri_totales.getChildren().add(eti_suma_debe);
        gri_totales.getChildren().add(eti_suma_haber);

        pat_panel2.setFooter(gri_totales);

        if (isPresupuesto()) {

            dia_asociacionPre.getGri_cuerpo().getChildren().clear();
            dia_asociacionPre.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".aceptarAsociacion");
            dia_asociacionPre.getBot_cancelar().setMetodoRuta("pre_index.clase." + getId() + ".cancelarAsociacion");

            tab_sel_aso_tab_seleccion = new Tabla();
            tab_sel_aso_tab_seleccion.setId("tab_sel_aso_tab_seleccion");
            tab_sel_aso_tab_seleccion.setRuta("pre_index.clase." + getId());
            tab_sel_aso_tab_seleccion.setSql(ser_comprobante.getSqlAsociacionPresupuestaria("-1", "-1"));
            tab_sel_aso_tab_seleccion.setCampoPrimaria("ide_prasp");
            tab_sel_aso_tab_seleccion.setRows(100);
            tab_sel_aso_tab_seleccion.setTipoSeleccion(true);
            tab_sel_aso_tab_seleccion.setSeleccionTabla("multiple");
            tab_sel_aso_tab_seleccion.setScrollable(true);
            tab_sel_aso_tab_seleccion.setScrollHeight(dia_asociacionPre.getAltoPanel() - 140);
            tab_sel_aso_tab_seleccion.getColumna("ide_prasp").setVisible(false);
            tab_sel_aso_tab_seleccion.getColumna("ide_prcla").setVisible(false);
            tab_sel_aso_tab_seleccion.setLectura(true);
            tab_sel_aso_tab_seleccion.dibujar();
            PanelTabla pat_panel4 = new PanelTabla();
            pat_panel4.setPanelTabla(tab_sel_aso_tab_seleccion);

            dia_asociacionPre.setDialogo(pat_panel4);

            tex_valor_pre = new Texto();
            tex_valor_pre.setSoloNumeros();
            Grid gri1 = new Grid();
            gri1.setColumns(2);
            gri1.getChildren().add(new Etiqueta("VALOR :"));
            gri1.getChildren().add(tex_valor_pre);
            pat_panel4.setHeader(gri1);

            tab_presupuesto = new Tabla();
            tab_presupuesto.setId("tab_presupuesto");
            tab_presupuesto.setRuta("pre_index.clase." + getId());
            tab_presupuesto.setTabla("pre_mensual", "ide_prmen", 888);
            tab_presupuesto.setHeader("PRESUPUESTO");
            tab_presupuesto.setScrollable(true);
            tab_presupuesto.setScrollWidth(getAnchoPanel() - 15);
            tab_presupuesto.setScrollHeight(60);
            tab_presupuesto.setCondicion("ide_cndcc=-1");
            tab_presupuesto.dibujar();
            tab_presupuesto.insertar();
            PanelTabla pat_panel3 = new PanelTabla();
            pat_panel3.setPanelTabla(tab_presupuesto);
            pat_panel3.getMenuTabla().getItem_buscar().setRendered(false);
            pat_panel3.getMenuTabla().getItem_actualizar().setRendered(false);
            pat_panel3.getMenuTabla().getItem_guardar().setRendered(false);
            pat_panel3.getMenuTabla().getItem_formato().setRendered(false);
            pat_panel3.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
            pat_panel3.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");
            pat_panel3.setStyle("width:100%;height:100%;overflow: auto;display: block;");
            contenido.getChildren().add(pat_panel3);
        }
        contenido.setStyle("width:" + (getAnchoPanel() - 10) + "px; height:" + (getAltoPanel() - 5) + "px;overflow:hidden;display:block;");
        this.getGri_cuerpo().getChildren().clear();
        this.setDialogo(contenido);
        this.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardar");

    }

    public void aceptarAsociacion() {
        if (tex_valor_pre.getValue() == null || String.valueOf(tex_valor_pre.getValue()).isEmpty()) {
            utilitario.agregarMensajeError("Debe ingresar un valor", "");
        }
        if (tab_sel_aso_tab_seleccion.getSeleccionados() != null) {
            dia_asociacionPre.cerrar();
            tab_deta_asiento.setValor("valor_cndcc", utilitario.getFormatoNumero(tex_valor_pre.getValue()));
            tab_deta_asiento.getFilaSeleccionada().setLectura(true);
            utilitario.addUpdate("tab_deta_asiento");
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una asociación presupuestaria", "");
        }
    }

    public void cancelarAsociacion() {
        dia_asociacionPre.cerrar();
        tab_deta_asiento.setValor("valor_cndcc", null);
        tab_deta_asiento.setValor("ide_cnlap", null);
        utilitario.addUpdate("tab_deta_asiento");
    }

    private void dibujarVerAsiento() {
        this.setTitle("ASIENTO CONTABLE N. " + ide_cnccc);
        Grupo contenido = new Grupo();
        AreaTexto ate_observacion_conta = new AreaTexto();
        tab_cabe_asiento = new Tabla();
        tab_deta_asiento = new Tabla();
        tab_cabe_asiento.setId("tab_cabe_asiento");
        tab_cabe_asiento.setRuta("pre_index.clase." + getId());
        tab_cabe_asiento.setSql(ser_comprobante.getSqlCabeceraAsiento(ide_cnccc));
        tab_cabe_asiento.getColumna("ide_cnccc").setNombreVisual("TRANSACCIÓN");
        tab_cabe_asiento.getColumna("ide_cnccc").setEtiqueta();
        tab_cabe_asiento.getColumna("ide_cnccc").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cabe_asiento.getColumna("numero_cnccc").setEtiqueta();
        tab_cabe_asiento.getColumna("numero_cnccc").setEstilo("font-size: 14px;font-weight: bold;text-align: right;");
        tab_cabe_asiento.getColumna("numero_cnccc").setNombreVisual("NUM. COMPROBANTE");
        tab_cabe_asiento.getColumna("numero_cnccc").setOrden(5);
        tab_cabe_asiento.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_cabe_asiento.getColumna("fecha_trans_cnccc").setOrden(1);
        tab_cabe_asiento.getColumna("fecha_trans_cnccc").setEtiqueta();
        tab_cabe_asiento.getColumna("nom_usua").setVisible(false);
        tab_cabe_asiento.getColumna("fecha_siste_cnccc").setVisible(false);
        tab_cabe_asiento.getColumna("hora_sistem_cnccc").setVisible(false);
        tab_cabe_asiento.getColumna("nom_modu").setEtiqueta();
        tab_cabe_asiento.getColumna("nom_modu").setNombreVisual("MÓDULO");
        tab_cabe_asiento.getColumna("nom_modu").setOrden(4);
        tab_cabe_asiento.getColumna("nom_modu").setEstilo("width:150px");
        tab_cabe_asiento.getColumna("nom_geper").setEtiqueta();
        tab_cabe_asiento.getColumna("nom_geper").setNombreVisual("BENEFICIARIO");
        tab_cabe_asiento.getColumna("nom_geper").setOrden(2);
        tab_cabe_asiento.getColumna("nombre_cntcm").setEtiqueta();
        tab_cabe_asiento.getColumna("nombre_cntcm").setNombreVisual("TIPO COMPROBANTE");
        tab_cabe_asiento.getColumna("nombre_cntcm").setEstilo("width:100px");
        tab_cabe_asiento.getColumna("nombre_cntcm").setOrden(3);
        tab_cabe_asiento.getColumna("OBSERVACION_CNCCC").setVisible(false);
        tab_cabe_asiento.setTipoFormulario(true);
        tab_cabe_asiento.getGrid().setColumns(6);
        tab_cabe_asiento.setMostrarNumeroRegistros(false);
        tab_cabe_asiento.setLectura(true);
        tab_cabe_asiento.dibujar();
        tab_cabe_asiento.setLectura(false);

        tab_deta_asiento.setId("tab_deta_asiento");
        tab_deta_asiento.setRuta("pre_index.clase." + getId());
        tab_deta_asiento.setSql(ser_comprobante.getSqlDetalleAsiento(tab_cabe_asiento.getValorSeleccionado()));
        tab_deta_asiento.getColumna("ide_cndcc").setVisible(false);
        tab_deta_asiento.getColumna("codig_recur_cndpc").setNombreVisual("CÓDIGO CUENTA");
        tab_deta_asiento.getColumna("nombre_cndpc").setNombreVisual("CUENTA");
        tab_deta_asiento.setColumnaSuma("debe,haber");
        tab_deta_asiento.getColumna("debe").alinearDerecha();
        tab_deta_asiento.getColumna("debe").setLongitud(25);
        tab_deta_asiento.getColumna("haber").alinearDerecha();
        tab_deta_asiento.getColumna("haber").setLongitud(25);
        tab_deta_asiento.getColumna("OBSERVACION_CNDCC").setNombreVisual("OBSERVACIÓN");
        tab_deta_asiento.setScrollable(true);
        tab_deta_asiento.setScrollHeight(getAltoPanel() - 210);
        tab_deta_asiento.setScrollWidth(getAnchoPanel() - 15);
        tab_deta_asiento.setLectura(true);
        tab_deta_asiento.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_asiento);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel.setStyle("overflow:hidden;");
        contenido.getChildren().add(tab_cabe_asiento);
        contenido.getChildren().add(pat_panel);

        Grid gri_observa = new Grid();
        gri_observa.setColumns(2);
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong>"));
        gri_observa.getChildren().add(new Etiqueta(""));
        ate_observacion_conta.setValue(tab_cabe_asiento.getValor("observacion_cnccc"));
        ate_observacion_conta.setCols(90);
        ate_observacion_conta.setDisabled(true);

        Grid griaux = new Grid();
        griaux.setColumns(2);

        Boton bot_imprimir = new Boton();
        bot_imprimir.setValue("Imprimir");
        bot_imprimir.setIcon("ui-icon-print");
        bot_imprimir.setMetodoRuta("pre_index.clase." + getId() + ".imprimir");
        griaux.getChildren().add(ate_observacion_conta);
        griaux.getChildren().add(bot_imprimir);
        gri_observa.getChildren().add(griaux);
        gri_observa.getChildren().add(new Etiqueta("<table style='padding-left:10px;'><tr><td><strong>USUARIO CREADOR :</strong></td><td>" + tab_cabe_asiento.getValor("nom_usua") + " </td></tr><td><strong>FECHA SISTEMA :</strong></td><td>" + utilitario.getFormatoFecha(tab_cabe_asiento.getValor("fecha_siste_cnccc")) + " </td><tr> </tr><td><strong>HORA SISTEMA :</strong></td><td>" + utilitario.getFormatoHora(tab_cabe_asiento.getValor("hora_sistem_cnccc")) + " </td><tr> </tr></table>"));
        contenido.getChildren().add(gri_observa);
        contenido.setStyle("overflow:hidden;");

        this.getGri_cuerpo().getChildren().clear();
        this.setDialogo(contenido);
        this.getBot_aceptar().setMetodoRuta("pre_index.clase." + getId() + ".guardar");
    }

    public void cargarCuentasAsientoTipo(SelectEvent evt) {
        aut_asiento_tipo.onSelect(evt);
        if (aut_asiento_tipo.getValor() != null) {
            tab_deta_asiento.limpiar();
            TablaGenerica tab_ast = ser_configuracion.getCabeceraAsientoTipo(aut_asiento_tipo.getValor());
            tab_cabe_asiento.setValor("ide_modu", tab_ast.getValor("ide_modu"));
            tab_cabe_asiento.setValor("ide_cntcm", tab_ast.getValor("ide_cntcm"));
            utilitario.addUpdate("tab_cabe_asiento");
            TablaGenerica tab_cuentas = ser_configuracion.getCuentasAsientoTipo(aut_asiento_tipo.getValor());
            for (int i = 0; i < tab_cuentas.getTotalFilas(); i++) {
                tab_deta_asiento.insertar();
                tab_deta_asiento.setValor("ide_cndpc", tab_cuentas.getValor(i, "ide_cndpc"));
                tab_deta_asiento.setValor("ide_cnlap", tab_cuentas.getValor(i, "ide_cnlap"));
            }
        }
    }

    public void seleccionarLugarAplica(AjaxBehaviorEvent evt) {
        tab_deta_asiento.modificar(evt);
        if (isPresupuesto()) {
            //usa presupuesto
            if (tab_deta_asiento.getValor("ide_cnlap") != null) {
                //busca asociasion presupuestaria
                tab_sel_aso_tab_seleccion.setSql(ser_comprobante.getSqlAsociacionPresupuestaria(tab_deta_asiento.getValor("ide_cndpc"), tab_deta_asiento.getValor("ide_cnlap")));
                tab_sel_aso_tab_seleccion.ejecutarSql();
                if (tab_sel_aso_tab_seleccion.isEmpty() == false) {
                    dia_asociacionPre.dibujar();
                }
            }
        }
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    public void ingresaCantidad(AjaxBehaviorEvent evt) {
        tab_deta_asiento.modificar(evt);
        calcularTotal();
        utilitario.addUpdate("gri_totales");
    }

    /**
     * Calcula la sumatoria de debe vs haber
     *
     * @return true =asiento cuadrado ; false = asiento con diferencias
     */
    public boolean calcularTotal() {
        double dou_debe = 0;
        double dou_haber = 0;
        for (int i = 0; i < tab_deta_asiento.getTotalFilas(); i++) {
            try {
                if (tab_deta_asiento.getValor(i, "ide_cnlap").equals(parametros.get("p_con_lugar_debe"))) {
                    dou_debe += Double.parseDouble(tab_deta_asiento.getValor(i, "valor_cndcc"));
                } else if (tab_deta_asiento.getValor(i, "ide_cnlap").equals(parametros.get("p_con_lugar_haber"))) {
                    dou_haber += Double.parseDouble(tab_deta_asiento.getValor(i, "valor_cndcc"));
                }
            } catch (Exception e) {
            }
        }
        eti_suma_debe.setValue("TOTAL DEBE : " + utilitario.getFormatoNumero(dou_debe));
        eti_suma_haber.setValue("TOTAL HABER : " + utilitario.getFormatoNumero(dou_haber));

        double dou_diferencia = Double.parseDouble(utilitario.getFormatoNumero(dou_debe)) - Double.parseDouble(utilitario.getFormatoNumero(dou_haber));
        eti_suma_diferencia.setValue("DIFERENCIA : " + utilitario.getFormatoNumero(dou_diferencia));
        if (dou_diferencia != 0.0) {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold;color:red");
        } else {
            eti_suma_diferencia.setStyle("font-size: 14px;font-weight: bold");
            return true;
        }
        return false;
    }

    public boolean validarComprobante() {
//        for (int i = 0; i < tab_deta_asiento.getTotalFilas(); i++) {
//            if (!ser_contabilidad.isCuentaContableHija(tab_deta_asiento.getValor(i, "ide_cndpc"))) {
//                utilitario.agregarMensajeError("En uno de los detalles del asiento, hay cuentas contables que no tiene nivel HIJO", "");
//                return false;
//            }
//        }
        if (tab_deta_asiento.isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar Detalles al Comprobante de Contabilidad", "");
            return false;
        }
        if (tab_cabe_asiento.getValor("fecha_trans_cnccc") == null || tab_cabe_asiento.getValor("fecha_trans_cnccc").isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar la Fecha de la Transaccion", "");
            return false;
        }
        if (tab_cabe_asiento.getValor("observacion_cnccc") == null || tab_cabe_asiento.getValor("observacion_cnccc").isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una Observación", "");
            return false;
        }
        if (tab_cabe_asiento.getValor("ide_geper") == null || tab_cabe_asiento.getValor("ide_geper").isEmpty()) {
            utilitario.agregarMensajeInfo("Debe seleccionar un Beneficiario", "");
            return false;
        }
        if (!calcularTotal()) {
            utilitario.agregarMensajeError("La suma de los detalles del DEBE tiene que ser igual al del HABER", "");
            return false;
        }
        return true;

    }

    public void guardar() {
        if (ide_cnccc == null) {
            if (validarComprobante()) {
                if (ser_comprobante.isPeriodoValido(tab_cabe_asiento.getValor("fecha_trans_cnccc"))) {
                    if (tab_cabe_asiento.isFilaInsertada()) {
                        tab_cabe_asiento.setValor("hora_sistem_cnccc", utilitario.getHoraActual());
                        tab_cabe_asiento.setValor("numero_cnccc", ser_comprobante.getSecuencial(tab_cabe_asiento.getValor("fecha_trans_cnccc"), tab_cabe_asiento.getValor("ide_cntcm")));
                    }
                    if (tab_cabe_asiento.guardar()) {
                        if (tab_deta_asiento.guardar()) {
                            if (tab_presupuesto != null) {
                                tab_presupuesto.guardar();
                            }
                            //utilitario.getConexion().setImprimirSqlConsola(true);                            
                            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                                ide_cnccc = tab_cabe_asiento.getValor("ide_cnccc");
                                completarAsiento(tab_cabe_asiento.getValor("ide_cnccc"));
                                this.cerrar();
                                verAsientoContable(tab_cabe_asiento.getValor("ide_cnccc"));
                            }
                        }
                    }
                }
            }
        } else {
            this.cerrar();
        }

    }

    public void imprimir() {
        if (ide_cnccc != null) {
            //SI EL ASIENTO ES TIPO CHEQUE            
            TablaGenerica tab_consulta = utilitario.consultar("select ide_cnccc,ide_tettb from tes_cab_libr_banc  where ide_cnccc=" + ide_cnccc + " and ide_tettb=" + parametros.get("p_tes_tran_cheque"));
            tab_consulta.imprimirSql();
            if (tab_consulta.isEmpty() == false) {
                setReporteCheque();
            } else {
                setReporteComprobante();
            }
            verAsientoContable(ide_cnccc);
        }
    }

    public void verAsientoContable(String ide_cnccc) {
        Map parametros_rep = new HashMap();
        vpd_asiento.setTitle("ASIENTO CONTABLE N. " + ide_cnccc);
        if (reporteComprobante == 1) {
            parametros_rep.put("ide_cnccc", Long.parseLong(ide_cnccc));
            parametros_rep.put("ide_cnlap_debe", parametros.get("p_con_lugar_debe"));
            parametros_rep.put("ide_cnlap_haber", parametros.get("p_con_lugar_haber"));

            vpd_asiento.setVisualizarPDF("rep_contabilidad/rep_comprobante_contabilidad.jasper", parametros_rep);
        }
        if (reporteComprobante == 2) {//cheque
            TablaGenerica tab_lib_banc = utilitario.consultar("select * from tes_cab_libr_banc where ide_cnccc =" + ide_cnccc);
            parametros_rep.put("beneficiario", tab_lib_banc.getValor("beneficiari_teclb") + "");
            parametros_rep.put("monto", tab_lib_banc.getValor("valor_teclb") + "");
            parametros_rep.put("anio", utilitario.getAnio(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
            parametros_rep.put("mes", utilitario.getMes(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
            parametros_rep.put("dia", utilitario.getDia(tab_lib_banc.getValor("fecha_trans_teclb")) + "");
            parametros_rep.put("monto_letras", ser_tesoreria.agregarAsteriscosCheque(utilitario.getLetrasDolarNumero(tab_lib_banc.getValor("valor_teclb"))));
            parametros_rep.put("ide_cnccc", Long.parseLong(tab_lib_banc.getValor("ide_cnccc")));
            parametros_rep.put("ide_cnlap_haber", parametros.get("p_con_lugar_haber"));
            parametros_rep.put("ide_cnlap_debe", parametros.get("p_con_lugar_debe"));
            parametros_rep.put("p_num_cheque", tab_lib_banc.getValor("numero_teclb") + "");
            parametros_rep.put("p_num_trans", tab_lib_banc.getValor("ide_teclb") + "");
            TablaGenerica tab_persona = ser_tesoreria.getPersona(ser_comprobante.getCabeceraComprobante(ide_cnccc).getValor("ide_geper"));
            if (tab_persona.isEmpty() == false) {
                parametros_rep.put("p_identificacion", tab_persona.getValor("identificac_geper"));
            } else {
                parametros_rep.put("p_identificacion", "");
            }
            vpd_asiento.setVisualizarPDF("rep_bancos/rep_cheque.jasper", parametros_rep);

        }
        vpd_asiento.dibujar();
    }

    public void insertar() {
        tab_deta_asiento.insertar();
    }

    public void eliminar() {
        tab_deta_asiento.eliminar();
    }

    @Override
    public void dibujar() {
        if (ide_cnccc == null) {
            dibujarNuevoAsiento();
        } else {
            dibujarVerAsiento();
        }
        super.dibujar();

    }

    @Override
    public void cerrar() {
        super.cerrar(); //To change body of generated methods, choose Tools | Templates.      
    }

    private void completarAsiento(String ide_cnccc) {
        if (tipo != null) {
            if (tipo.equals(TipoAsientoEnum.FACTURAS_CXC.getCodigo())) {
                //Asigna el ide_cnccc a la factura y a la transaccion cxc  y comp inventario               
                utilitario.getConexion().ejecutarSql("UPDATE cxc_cabece_factura SET ide_cnccc=" + ide_cnccc + " WHERE ide_cccfa in(" + relacion + ")");
                utilitario.getConexion().ejecutarSql("UPDATE cxc_detall_transa SET ide_cnccc=" + ide_cnccc + " WHERE ide_cccfa in(" + relacion + ") and numero_pago_ccdtr=0");
                utilitario.getConexion().ejecutarSql("UPDATE inv_cab_comp_inve SET ide_cnccc=" + ide_cnccc + " WHERE ide_incci in(select ide_incci from inv_det_comp_inve where ide_cccfa in(" + relacion + ")) and ide_cnccc is null");
            } else if (tipo.equals(TipoAsientoEnum.DOCUMENTOS_CXP.getCodigo())) {
                //Asigna el ide_cnccc a la factura y a la transaccion cxc  y comp inventario                             
                utilitario.getConexion().ejecutarSql("UPDATE cxp_cabece_factur SET ide_cnccc=" + ide_cnccc + " WHERE ide_cpcfa in(" + relacion + ")");
                utilitario.getConexion().ejecutarSql("UPDATE cxp_detall_transa SET ide_cnccc=" + ide_cnccc + " WHERE ide_cpcfa in(" + relacion + ") and numero_pago_cpdtr=0");
                utilitario.getConexion().ejecutarSql("UPDATE inv_cab_comp_inve SET ide_cnccc=" + ide_cnccc + " WHERE ide_incci in(select ide_incci from inv_det_comp_inve where ide_cpcfa in(" + relacion + ")) and ide_cnccc is null");
                utilitario.getConexion().ejecutarSql("UPDATE con_cabece_retenc SET ide_cnccc=" + ide_cnccc + " WHERE ide_cncre in  (select ide_cncre from cxp_cabece_factur where ide_cpcfa in(" + relacion + ") ) and ide_cnccc is null");
            } else if (tipo.equals(TipoAsientoEnum.LIBRO_BANCOS.getCodigo())) {
                //Asigna el ide_cnccc a la  facturas, documentos cxp,transaccion cxc o cxp y al libro bancos               
                utilitario.getConexion().ejecutarSql("UPDATE tes_cab_libr_banc SET ide_cnccc=" + ide_cnccc + " WHERE ide_teclb in(" + relacion + ")");
                utilitario.getConexion().ejecutarSql("UPDATE cxc_detall_transa SET ide_cnccc=" + ide_cnccc + " WHERE ide_teclb in(" + relacion + ")");
                utilitario.getConexion().ejecutarSql("UPDATE cxp_detall_transa SET ide_cnccc=" + ide_cnccc + " WHERE ide_teclb in(" + relacion + ")");
                utilitario.getConexion().ejecutarSql("UPDATE cxp_cabece_factur set ide_cnccc =" + ide_cnccc + " where ide_cpcfa in (select ide_cpcfa from cxp_detall_transa where ide_teclb in (" + relacion + "))");
                utilitario.getConexion().ejecutarSql("UPDATE cxc_cabece_factura set ide_cnccc =" + ide_cnccc + " where ide_cccfa in (select ide_cccfa from cxc_detall_transa where ide_teclb in (" + relacion + "))");

                utilitario.getConexion().ejecutarSql("UPDATE con_cabece_retenc SET ide_cnccc=" + ide_cnccc + " WHERE ide_cncre in  (select ide_cncre from cxp_cabece_factur where ide_cpcfa in (select ide_cpcfa from cxp_detall_transa where ide_teclb in (" + relacion + ")) ) and ide_cnccc is null");

                utilitario.getConexion().ejecutarSql("UPDATE inv_cab_comp_inve SET ide_cnccc=" + ide_cnccc + " WHERE ide_incci in(select ide_incci from inv_det_comp_inve where ide_cpcfa in (select ide_cpcfa from cxp_detall_transa where ide_teclb in (" + relacion + ")) ) and ide_cnccc is null");

            }
        }
    }

    //Genera un asiento contable Factura CXC  (Se crea la cuenta por pagar)
    public void setAsientoFacturasCxC(String ide_cccfa) {
        this.relacion = ide_cccfa;
        this.tipo = TipoAsientoEnum.FACTURAS_CXC.getCodigo();
        //Consulta las facturas
        TablaGenerica tab_fac = utilitario.consultar("SELECT * FROM cxc_cabece_factura WHERE ide_cccfa in (" + ide_cccfa + ")");
        if (tab_fac.isEmpty() == false) {
            if (tab_fac.getTotalFilas() == 1) {
                //una
                tab_cabe_asiento.setValor("ide_geper", tab_fac.getValor("ide_geper"));
                tab_cabe_asiento.setValor("observacion_cnccc", "V/. FACTURA N." + tab_fac.getValor("secuencial_cccfa"));
            } else { //varias
                String str_observa = "V/. FACTURAS N.";
                boolean boo_mismo_clie = true; //si es el mismo cliente todos los seleccionados
                String str_ide_geper = "";
                for (int i = 0; i < tab_fac.getTotalFilas(); i++) {
                    if (i == 0) {
                        str_ide_geper = tab_fac.getValor(i, "ide_geper");
                    }
                    if (str_ide_geper.equals(tab_fac.getValor(i, "ide_geper")) == false) {
                        boo_mismo_clie = false;
                    }
                    if (str_observa.equals("V/. FACTURAS N.")) {
                        str_observa += " ";
                    } else {
                        str_observa += ", ";
                    }
                    str_observa += "" + tab_fac.getValor(i, "secuencial_cccfa");
                }
                if (boo_mismo_clie) {
                    tab_cabe_asiento.setValor("ide_geper", str_ide_geper);
                } else {
                    tab_cabe_asiento.setValor("ide_geper", getBeneficiarioEmpresa());//sociedad salesianos                
                }
                if (tab_cabe_asiento.getValor("observacion_cnccc") == null) {
                    tab_cabe_asiento.setValor("observacion_cnccc", str_observa);
                }
            }
        } else {
            utilitario.agregarMensajeError("Error no se puede generar el Asiento Contable", "No existe la Factura");
        }
    }

    //Genera un asiento contable de Prestamos 
    public void setAsientoPrestamo(String ide_cccfa) {
        this.relacion = ide_cccfa;
        this.tipo = TipoAsientoEnum.PRESTAMOS.getCodigo();
        //Consulta las facturas
        TablaGenerica tab_fac = utilitario.consultar("SELECT * FROM cxc_cabece_factura WHERE ide_cccfa in (" + ide_cccfa + ")");
        if (tab_fac.isEmpty() == false) {
            if (tab_fac.getTotalFilas() == 1) {
                //una
                tab_cabe_asiento.setValor("ide_geper", tab_fac.getValor("ide_geper"));
                tab_cabe_asiento.setValor("observacion_cnccc", "V/. FACTURA N." + tab_fac.getValor("secuencial_cccfa"));
            } else { //varias
                String str_observa = "V/. FACTURA N.";
                boolean boo_mismo_clie = true; //si es el mismo cliente todos los seleccionados
                String str_ide_geper = "";
                for (int i = 0; i < tab_fac.getTotalFilas(); i++) {
                    if (i == 0) {
                        str_ide_geper = tab_fac.getValor(i, "ide_geper");
                    }
                    if (str_ide_geper.equals(tab_fac.getValor(i, "ide_geper")) == false) {
                        boo_mismo_clie = false;
                    }
                    if (str_observa.equals("V/. FACTURAS N.")) {
                        str_observa += " ";
                    } else {
                        str_observa += ", ";
                    }
                    str_observa += "" + tab_fac.getValor(i, "secuencial_cccfa");
                }
                if (boo_mismo_clie) {
                    tab_cabe_asiento.setValor("ide_geper", str_ide_geper);
                } else {
                    tab_cabe_asiento.setValor("ide_geper", getBeneficiarioEmpresa());//sociedad salesianos                
                }
                if (tab_cabe_asiento.getValor("observacion_cnccc") == null) {
                    tab_cabe_asiento.setValor("observacion_cnccc", str_observa);
                }
            }
        } else {
            utilitario.agregarMensajeError("Error no se puede generar el Asiento Contable", "No existe la Factura");
        }
        Tabla tb = new Tabla();
        tb.setSql("SELECT * FROM iyp_deta_prestamo WHERE ide_cccfa in (" + ide_cccfa + ")");
        tb.ejecutarSql();
        setAsientoTipoSistema(TipoAsientoEnum.PRESTAMOS, tb);

    }

    public void setAsientoPagoFacturasCxC(String ide_cccfa) {
        this.relacion = ide_cccfa;
        this.tipo = TipoAsientoEnum.PAGO_FACTURAS_CXC.getCodigo();
    }

    public void setAsientoDocumentosCxP(String ide_cpcfa) {
        this.relacion = ide_cpcfa;
        this.tipo = TipoAsientoEnum.DOCUMENTOS_CXP.getCodigo();
        //Consulta las facturas
        TablaGenerica tab_fac = utilitario.consultar("SELECT * FROM cxp_cabece_factur WHERE ide_cpcfa in (" + ide_cpcfa + ")");
        if (tab_fac.isEmpty() == false) {
            if (tab_fac.getTotalFilas() == 1) {
                //una
                tab_cabe_asiento.setValor("ide_geper", tab_fac.getValor("ide_geper"));
                tab_cabe_asiento.setValor("observacion_cnccc", "V/. FACTURA N." + tab_fac.getValor("numero_cpcfa"));
            } else { //varias
                String str_observa = "V/. FACTURAS N.";
                boolean boo_mismo_clie = true; //si es el mismo cliente todos los seleccionados
                String str_ide_geper = "";
                for (int i = 0; i < tab_fac.getTotalFilas(); i++) {
                    if (i == 0) {
                        str_ide_geper = tab_fac.getValor(i, "ide_geper");
                    }
                    if (str_ide_geper.equals(tab_fac.getValor(i, "ide_geper")) == false) {
                        boo_mismo_clie = false;
                    }
                    if (str_observa.equals("V/. FACTURAS N.")) {
                        str_observa += " ";
                    } else {
                        str_observa += ", ";
                    }
                    str_observa += "" + tab_fac.getValor(i, "numero_cpcfa");
                }
                if (boo_mismo_clie) {
                    tab_cabe_asiento.setValor("ide_geper", str_ide_geper);//sociedad salesianos                
                } else {
                    tab_cabe_asiento.setValor("ide_geper", getBeneficiarioEmpresa());//sociedad salesianos                
                }

                if (tab_cabe_asiento.getValor("observacion_cnccc") == null) {
                    tab_cabe_asiento.setValor("observacion_cnccc", str_observa);
                }
            }
        } else {
            utilitario.agregarMensajeError("Error no se puede generar el Asiento Contable", "No existe el Documento");
        }

    }

    public void setAsientoTipoSistema(TipoAsientoEnum tipoAsientoEnum, Tabla... tablas) {
        TablaGenerica tab_ast = ser_configuracion.getCabeceraAsientoTipo(tipoAsientoEnum.getCodigo());

        aut_asiento_tipo.setValor(tipoAsientoEnum.getCodigo());
        tab_cabe_asiento.setValor("ide_modu", tab_ast.getValor("ide_modu"));
        tab_cabe_asiento.setValor("ide_cntcm", tab_ast.getValor("ide_cntcm"));
        utilitario.addUpdate("tab_cabe_asiento");
        TablaGenerica tab_cuentas = ser_configuracion.getCuentasAsientoTipo(tipoAsientoEnum.getCodigo());
        Map<String, Double> map_campos = new HashMap();
        //recupera columna configurada       
        for (int i = 0; i < tab_cuentas.getTotalFilas(); i++) {
            if (tab_cuentas.getValor(i, "columna_coast") != null) {
                //si no es un valor fijo
                double valor = -1;
                try {
                    valor = Double.parseDouble(tab_cuentas.getValor(i, "columna_coast"));
                } catch (Exception e) {
                    valor = -1;
                }
                if (valor == -1) {
                    map_campos.put(tab_cuentas.getValor(i, "columna_coast").toUpperCase(), 0.00);
                }
            }
        }

        if (map_campos.isEmpty()) {
            return;
        }

        for (Tabla tablaActual : tablas) {
            //recorre las columnas de todas las tablas
            for (int i = 0; i < tablaActual.getTotalFilas(); i++) {
                //recorre columnas
                for (int j = 0; j < tablaActual.getTotalColumnas(); j++) {
                    String nom_columna = tablaActual.getColumnas()[j].getNombre();

                    if (map_campos.get(nom_columna) != null) {
                        double valor = map_campos.get(nom_columna);
                        double valor_columna = 0;
                        try {
                            valor_columna = Double.parseDouble(tablaActual.getValor(i, nom_columna));
                        } catch (Exception e) {
                            valor_columna = 0;
                        }
                        valor += valor_columna;
                        map_campos.put(nom_columna, valor);
                    }

                }
            }
        }

        for (int i = 0; i < tab_cuentas.getTotalFilas(); i++) {
            tab_deta_asiento.insertar();
            tab_deta_asiento.setValor("ide_cndpc", tab_cuentas.getValor(i, "ide_cndpc"));
            tab_deta_asiento.setValor("ide_cnlap", tab_cuentas.getValor(i, "ide_cnlap"));

            if (tab_cuentas.getValor(i, "columna_coast") != null) {

                //si no es un valor fijo
                double valor = -1;
                try {
                    valor = Double.parseDouble(tab_cuentas.getValor(i, "columna_coast"));
                } catch (Exception e) {
                    valor = -1;
                }

                if (valor == -1) {
                    tab_deta_asiento.setValor("VALOR_cndcc", utilitario.getFormatoNumero(map_campos.get(tab_cuentas.getValor(i, "columna_coast").toUpperCase())));
                } else {
                    tab_deta_asiento.setValor("VALOR_cndcc", utilitario.getFormatoNumero(valor));
                }
            }
        }
    }

    public void setAsientoPagoDocumentosCxP(String ide_cpcfa) {
        this.relacion = ide_cpcfa;
        this.tipo = TipoAsientoEnum.PAGO_DOCUMENTOS_CXP.getCodigo();
    }

    public void setAsientoLibroBancos(String ide_teclb) {
        this.relacion = ide_teclb;
        this.tipo = TipoAsientoEnum.LIBRO_BANCOS.getCodigo();
    }

    public Tabla getTab_cabe_asiento() {
        return tab_cabe_asiento;
    }

    public void setTab_cabe_asiento(Tabla tab_cabe_asiento) {
        this.tab_cabe_asiento = tab_cabe_asiento;
    }

    public Tabla getTab_deta_asiento() {
        return tab_deta_asiento;
    }

    public void setTab_deta_asiento(Tabla tab_deta_asiento) {
        this.tab_deta_asiento = tab_deta_asiento;
    }

    public AutoCompletar getAut_asiento_tipo() {
        return aut_asiento_tipo;
    }

    public void setAut_asiento_tipo(AutoCompletar aut_asiento_tipo) {
        this.aut_asiento_tipo = aut_asiento_tipo;
    }

    public Tabla getTab_presupuesto() {
        return tab_presupuesto;
    }

    public void setTab_presupuesto(Tabla tab_presupuesto) {
        this.tab_presupuesto = tab_presupuesto;
    }

    public String getLugarAplicaDebe() {
        return parametros.get("p_con_lugar_debe");
    }

    public String getLugarAplicaHaber() {
        return parametros.get("p_con_lugar_haber");
    }

    public String getTipoComprobanteDiario() {
        return parametros.get("p_con_tipo_comprobante_diario");
    }

    public String getTipoComprobanteIngreso() {
        return parametros.get("p_con_tipo_comprobante_ingreso");
    }

    public String getTipoComprobanteEgreso() {
        return parametros.get("p_con_tipo_comprobante_egreso");
    }

    public String getBeneficiarioEmpresa() {
        return parametros.get("p_con_beneficiario_empresa"); //sociedad salesianos
    }

    public void setReporteCheque() {
        reporteComprobante = 2;
    }

    public void setReporteComprobante() {
        reporteComprobante = 1;
    }

    public String getIde_cnccc() {
        return ide_cnccc;
    }

    public void nuevoAsiento() {
        this.ide_cnccc = null;
    }

    public final boolean isPresupuesto() {
        if (parametros.get("p_con_usa_presupuesto") == null) {
            return false;
        } else if (parametros.get("p_con_usa_presupuesto").equals("false")) {
            return false;
        } else if (parametros.get("p_con_usa_presupuesto").equals("true")) {
            return true;
        }
        return false;
    }

    public Dialogo getDia_asociacionPre() {
        return dia_asociacionPre;
    }

    public void setDia_asociacionPre(Dialogo dia_asociacionPre) {
        this.dia_asociacionPre = dia_asociacionPre;
    }

    public Tabla getTab_seleccion() {
        return tab_sel_aso_tab_seleccion;
    }

    public void setTab_seleccion(Tabla tab_tabla) {
        this.tab_sel_aso_tab_seleccion = tab_tabla;
    }

    public Tabla getTab_sel_aso_tab_seleccion() {
        return tab_sel_aso_tab_seleccion;
    }

    public void setTab_sel_aso_tab_seleccion(Tabla tab_sel_aso_tab_seleccion) {
        this.tab_sel_aso_tab_seleccion = tab_sel_aso_tab_seleccion;
    }

}
