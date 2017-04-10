/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_pagar;

import componentes.AsientoContable;
import componentes.DocumentoCxP;
import componentes.Retencion;
import framework.aplicacion.Fila;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Link;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.graficos.GraficoCartesiano;
import framework.componentes.graficos.GraficoPastel;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import servicios.cuentas_x_pagar.ServicioProveedor;
import paq_presupuesto.ejb.ServicioPresupuesto;
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class pre_documentosCxP extends Pantalla {

    private final MenuPanel mep_menu = new MenuPanel();
    private final Combo com_tipo_documento = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private DocumentoCxP dcp_documento = new DocumentoCxP();
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);
    @EJB
    private final ServicioProveedor ser_proveedor = (ServicioProveedor) utilitario.instanciarEJB(ServicioProveedor.class);
    @EJB
    private final ServicioPresupuesto ser_presupuesto = (ServicioPresupuesto) utilitario.instanciarEJB(ServicioPresupuesto.class);
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);
    @EJB
    private final ServicioComprobanteContabilidad ser_comp_conta = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    private Tabla tab_tabla1 = new Tabla();
    private Combo com_periodo;
    private GraficoCartesiano gca_facturas;
    private GraficoPastel gpa_facturas;

    private Retencion ret_retencion = new Retencion();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sel_rep = new SeleccionFormatoReporte();

    private AsientoContable asc_asiento = new AsientoContable();

    private Confirmar con_confirmar = new Confirmar();
    private AutoCompletar aut_proveedor;
    private Texto txt_nro_certificacion = new Texto();
    private Tabla tab_tabla2;
    private SeleccionTabla sel_certificacion = new SeleccionTabla();

    private Etiqueta eti1 = new Etiqueta();

    private AutoCompletar aut_anticipo;
    private Texto tex_saldo_anticipo;
    private Texto tex_suma_facturas;
    private Calendario cal_fecha_pago;
    private Radio rad_hace_asiento;
    private Texto tex_num_asiento;
    private Etiqueta eti_num_asiento;
    private Texto tex_observacion;
    private Confirmar con_confirma = new Confirmar();

    public pre_documentosCxP() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();
        bar_botones.agregarReporte();

        com_tipo_documento.setCombo(ser_cuentas_cxp.getSqlTipoDocumentosCxP());
        com_tipo_documento.setMetodo("actualizarFiltros");

        bar_botones.agregarComponente(new Etiqueta("TIPO DE DOCUMENTO :"));
        bar_botones.agregarComponente(com_tipo_documento);

        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);

        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarFiltros");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        dcp_documento.setId("dcp_documento");
        dcp_documento.setDocumentoCxP("INGRESAR DOCUMENTO POR PAGAR");
        dcp_documento.getBot_aceptar().setMetodo("guardar");
        agregarComponente(dcp_documento);

        mep_menu.setMenuPanel("OPCIONES DOCUMENTOS POR PAGAR", "20%");
        mep_menu.agregarItem("Listado de Documentos CxP ", "dibujarDocumentos", "ui-icon-note");
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarDocumentosNoContabilizadas", "ui-icon-notice");
        mep_menu.agregarItem("Generar Comprobante Retención", "dibujarDocumentosNoRetencion", "ui-icon-notice");
        mep_menu.agregarSubMenu("ANTICIPOS EMPLEADOS");
        mep_menu.agregarItem("Saldar Documentos CxP ", "dibujarSaldarAnticipo", "ui-icon-star");//8
        mep_menu.agregarSubMenu("CONSULTAS");
        mep_menu.agregarItem("Documentos Anulados", "dibujarDocumentosAnulados", "ui-icon-cancel");
        mep_menu.agregarItem("Documentos Por Pagar", "dibujarDocumentosPorPagar", "ui-icon-calculator");
        mep_menu.agregarSubMenu("INFORMES");
        mep_menu.agregarItem("Grafico de Compras", "dibujarGraficoCompras", "ui-icon-clock");
        // mep_menu.agregarItem("Estadística de Ventas", "dibujarEstadisticas", "ui-icon-bookmark");        
        mep_menu.agregarSubMenu("PRESUPUESTO");
        mep_menu.agregarItem("Compromiso Documentos CxP", "dibujarCompromiso", "ui-icon-calculator");
        mep_menu.agregarSubMenu("FACTURAS ELECTRONICAS");
        mep_menu.agregarItem("Seleccionar Factura XML", "dibujarFacturaElectronica", "ui-icon-signal-diag");
        agregarComponente(mep_menu);
        dibujarDocumentos();

        ret_retencion.setId("ret_retencion");
        ret_retencion.getBot_aceptar().setMetodo("guardar");
        agregarComponente(ret_retencion);

        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        sel_rep.setId("sel_rep");
        agregarComponente(rep_reporte);
        agregarComponente(sel_rep);

        asc_asiento.setId("asc_asiento");
        asc_asiento.getBot_aceptar().setMetodo("guardar");
        asc_asiento.getBot_cancelar().setMetodo("cerrarAsiento");
        agregarComponente(asc_asiento);

        con_confirmar.setId("con_confirmar");
        con_confirmar.setWidgetVar("con_confirmar");
        con_confirmar.setMessage("Desea Generar el Comprobante de Retención?");
        con_confirmar.getBot_aceptar().setValue("Si");
        con_confirmar.getBot_cancelar().setValue("No");
        con_confirmar.getBot_aceptar().setOncomplete("con_confirmar.hide();");
        con_confirmar.getBot_aceptar().setMetodo("abrirRetencion");
        con_confirmar.getBot_cancelar().setOnclick("con_confirmar.hide();");
        agregarComponente(con_confirmar);

        sel_certificacion.setId("sel_certificacion");
        sel_certificacion.setTitle("SELECCIONE UNA CERTIFICACION");
        sel_certificacion.setSeleccionTabla(ser_presupuesto.getCompromisoRua("-1", "1"), "ide_prpot");
        sel_certificacion.getTab_seleccion().getColumna("cod_programa_prpro").setFiltro(true);
        sel_certificacion.getTab_seleccion().getColumna("detalle_subactividad").setFiltro(true);
        sel_certificacion.getBot_aceptar().setMetodo("aceptarBusqueda");
        agregarComponente(sel_certificacion);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular el Documento CXP Seleccionado ?");
        con_confirma.setTitle("ANULAR DOCUMENTO CXP");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

    }

    public void dibujarCompromiso() {
        Grid gri = new Grid();
        Grid g1 = new Grid();
        g1.setColumns(5);
        g1.getChildren().add(new Etiqueta("<strong>PROVEEDOR : </strong>"));
        aut_proveedor = new AutoCompletar();
        aut_proveedor.setId("aut_proveedor");
        aut_proveedor.setAutoCompletar(ser_proveedor.getSqlComboProveedor());
        aut_proveedor.setAutocompletarContenido();
        aut_proveedor.setMetodoChange("cargarDocumetosProveedor");
        aut_proveedor.setSize(70);
        g1.getChildren().add(aut_proveedor);
        g1.getChildren().add(new Etiqueta("<strong>Nro. Certificación: </strong>"));
        txt_nro_certificacion.setId("txt_nro_certificacion");
        txt_nro_certificacion.setSize(20);
        g1.getChildren().add(txt_nro_certificacion);

        Boton bot_buscar_certi = new Boton();
        bot_buscar_certi.setValue("Buscar Certificación");
        bot_buscar_certi.setMetodo("buscaCertificacion");
        g1.getChildren().add(bot_buscar_certi);

        // boton guardar facturas presupuestos
        Boton bot_guardar_certi = new Boton();
        bot_guardar_certi.setValue("Guardar Certificación");
        bot_guardar_certi.setMetodo("GuardaCertificacion");
        g1.getChildren().add(bot_guardar_certi);

        gri.getChildren().add(g1);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosProveedor(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), "-1"));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("ide_cncre").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setVisible(false);
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setScrollable(true);
        tab_tabla1.setScrollHeight(utilitario.getAltoPantalla() - 350);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[5] eq '" + utilitario.getVariable("p_cxp_estado_factura_anulada") + "' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla1.onSelect("seleccionarDocumento");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);

        gri.getChildren().add(pat_panel);

        //tabla asociación
        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("pre_compromiso_factura", "ide_prcof", 7);
        tab_tabla2.setCondicion("ide_cpcfa=-1");
        tab_tabla2.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla2.getColumna("ide_prpot").setCombo(ser_presupuesto.getCompromisoRua("-1", "3"));
        tab_tabla2.getColumna("ide_prpot").setAutoCompletar();
        tab_tabla2.getColumna("valor_devengar_prcof").setMetodoChange("CalcularSuma");
        tab_tabla2.getColumna("saldo_devengar_prcof").setEtiqueta();
        tab_tabla2.getColumna("saldo_devengar_prcof").setEstilo("font-size:15px;font-weight: bold;text-decoration: underline;color:red");//Estilo
        tab_tabla2.setColumnaSuma("valor_devengar_prcof");
        tab_tabla2.setScrollable(true);
        tab_tabla2.setScrollHeight(utilitario.getAltoPantalla() - 450);
        tab_tabla2.dibujar();

        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        pat_panel2.getMenuTabla().getItem_insertar().setMetodo("insertar7");
        pat_panel2.getMenuTabla().getItem_guardar().setMetodo("guardar7");
        pat_panel2.getMenuTabla().getItem_guardar().setRendered(true);
        pat_panel2.getMenuTabla().getItem_eliminar().setMetodo("eliminar7");
        pat_panel2.getMenuTabla().getItem_eliminar().setRendered(true);

        gri.getChildren().add(pat_panel2);
        mep_menu.dibujar(7, "COMPROMISO PRESUPUESTARIO DOCUMENTOS CXP", gri);
    }

    public void dibujarSaldarAnticipo() {
        Grid gri_contenido = new Grid();

        Grid gri2 = new Grid();
        gri2.setColumns(4);
        gri2.getChildren().add(new Etiqueta("<strong>ANTICIPO EMPLEADO : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        aut_anticipo = new AutoCompletar();
        aut_anticipo.setId("aut_anticipo");
        aut_anticipo.setAutocompletarContenido();
        aut_anticipo.setAutoCompletar(ser_factura.getSqlAnticiposEmpleados());
        aut_anticipo.setMetodoChange("seleccionarAnticipoEmpleado");
        aut_anticipo.setSize(70);
        gri2.getChildren().add(aut_anticipo);
        gri2.getChildren().add(new Etiqueta("<strong>FECHA : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        cal_fecha_pago = new Calendario();
        cal_fecha_pago.setFechaActual();
        gri2.getChildren().add(cal_fecha_pago);
        gri_contenido.getChildren().add(gri2);

        Grid gri1 = new Grid();
        gri1.setColumns(4);

        Etiqueta eti_valor_cobrar = new Etiqueta();
        eti_valor_cobrar.setValue("VALOR ANTICIPO $:");
        eti_valor_cobrar.setStyle("font-weight: bold;");
        tex_saldo_anticipo = new Texto();
        tex_saldo_anticipo.setId("tex_saldo_anticipo");
        tex_saldo_anticipo.setDisabled(true);

        Etiqueta eti_diferencia = new Etiqueta();
        eti_diferencia.setStyle("font-weight: bold");
        eti_diferencia.setValue("SALDO SELECCIONADO $: ");

        gri1.getChildren().add(eti_valor_cobrar);
        gri1.getChildren().add(tex_saldo_anticipo);

        tex_suma_facturas = new Texto();
        tex_suma_facturas.setId("tex_suma_facturas");
        tex_suma_facturas.setDisabled(true);
        tex_suma_facturas.setStyle("font-weight: bold");

        gri1.getChildren().add(eti_diferencia);
        gri1.getChildren().add(tex_suma_facturas);

        tex_observacion = new Texto();
        tex_observacion.setSize(100);
        Grid gri5 = new Grid();
        gri5.setColumns(2);
        gri5.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN : </strong><span style='color:red;font-weight: bold;'>*</span>"));
        gri5.getChildren().add(tex_observacion);
        gri1.setFooter(gri5);

        gri_contenido.getChildren().add(gri1);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosPorPagar(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
        tab_tabla1.getColumna("saldo_x_pagar").alinearDerecha();
        tab_tabla1.setCampoPrimaria("ide_cpctr");
        tab_tabla1.getColumna("ide_cpctr").setVisible(false);
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        //tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
        //tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla1.getColumna("fecha").setVisible(true);
        tab_tabla1.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
        // tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_tabla1.getColumna("total_cpcfa").setNombreVisual("TOTAL");
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 13px;");
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.setLectura(true);

        tab_tabla1.onSelectCheck("seleccionaFacturaCxP");
        tab_tabla1.onUnselectCheck("deseleccionaFacturaCxP");
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setRows(10);
        tab_tabla1.dibujar();
        gri_contenido.getChildren().add(tab_tabla1);
        gri_contenido.getChildren().add(new Separator());

        Grid gri = new Grid();
        gri.setId("gri");
        gri.setColumns(2);
        gri.getChildren().add(new Etiqueta("<div style='font-size:12px;font-weight: bold;'> <img src='imagenes/im_pregunta.gif' />  GENERAR NUEVO ASIENTO CONTABLE ? </div>"));
        rad_hace_asiento = new Radio();
        rad_hace_asiento.setRadio(utilitario.getListaPregunta());
        rad_hace_asiento.setValue(true);
        rad_hace_asiento.setMetodoChange("cambiaHaceAsiento");
        gri.getChildren().add(rad_hace_asiento);
        gri_contenido.setFooter(gri);

        tex_num_asiento = new Texto();
        tex_num_asiento.setId("tex_num_asiento");
        tex_num_asiento.setSoloEnteros();
        tex_num_asiento.setRendered(false);

        eti_num_asiento = new Etiqueta();
        eti_num_asiento.setRendered(false);
        eti_num_asiento.setId("eti_num_asiento");
        eti_num_asiento.setValue("<strong>NÚMERO DE ASIENTO : :</strong>");

        gri.getChildren().add(eti_num_asiento);
        gri.getChildren().add(tex_num_asiento);

        Boton bot_aceptar = new Boton();
        bot_aceptar.setValue("Aceptar");
        bot_aceptar.setMetodo("aceptarAnticipoCxp");
        gri.setFooter(bot_aceptar);

        mep_menu.dibujar(8, "SALDAR DOCUMENTOS CXP", gri_contenido);

    }

    public void cerrarAsiento() {
        //limpia sql guardados
        utilitario.getConexion().getSqlPantalla().clear();
        asc_asiento.cerrar();
    }

    public void aceptarAnticipoCxp() {
        if (validarAnticipoEmpleado()) {
            String ide_cnccc = null;
            if (tex_num_asiento.getValue() != null) {
                if (tex_num_asiento.getValue().toString().isEmpty() == false) {
                    TablaGenerica tab_asiento = ser_comp_conta.getCabeceraComprobante(tex_num_asiento.getValue().toString());
                    if (tab_asiento.isEmpty()) {
                        utilitario.agregarMensajeError("El asiento contable Num. " + tex_num_asiento.getValue() + " no existe", "");
                        return;
                    } else {
                        ide_cnccc = tex_num_asiento.getValue().toString();
                    }
                }
            }
            for (Fila actual : tab_tabla1.getSeleccionados()) {
                TablaGenerica tab_cab_factura = utilitario.consultar("SELECT ide_cpcfa,numero_cpcfa FROM cxp_cabece_factur where ide_cpcfa=" + actual.getCampos()[1]);
                double total = Double.parseDouble(String.valueOf(actual.getCampos()[8]));
                String ide_cpdtr = ser_cuentas_cxp.generarTransaccionPago(tab_cab_factura, String.valueOf(actual.getCampos()[0]), null, total, String.valueOf(tex_observacion.getValue()), null);
                utilitario.getConexion().agregarSql("UPDATE cxp_detall_transa SET ide_cnccc=" + ide_cnccc + " where ide_cpdtr=" + ide_cpdtr);
                utilitario.getConexion().agregarSql("UPDATE cxp_cabece_factur SET ide_cnccc=" + ide_cnccc + " where ide_cpcfa=" + actual.getCampos()[1]);

            }
            String ide_ccctr = aut_anticipo.getValor();
            TablaGenerica tab_tabla_d = new TablaGenerica();
            tab_tabla_d.setTabla("cxc_detall_transa", "ide_ccdtr");
            tab_tabla_d.setCondicion("ide_ccdtr=-1");
            tab_tabla_d.ejecutarSql();
            tab_tabla_d.insertar();
            tab_tabla_d.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
            tab_tabla_d.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_tabla_d.setValor("numero_pago_ccdtr", "1");
            tab_tabla_d.setValor("ide_ccctr", ide_ccctr);
            tab_tabla_d.setValor("ide_cnccc", ide_cnccc);
            tab_tabla_d.setValor("valor_ccdtr", String.valueOf(tex_suma_facturas.getValue()));
            tab_tabla_d.setValor("observacion_ccdtr", String.valueOf(tex_observacion.getValue()));
            tab_tabla_d.setValor("ide_ccttr", "20");//CANCELA ANTICIPO
            tab_tabla_d.guardar();
            if (rad_hace_asiento.getValue().equals("true")) {
                abrirGeneraAsiento();
                if (asc_asiento.isVisible()) {
                    asc_asiento.getTab_cabe_asiento().setValor("fecha_trans_cnccc", cal_fecha_pago.getFecha());
                    asc_asiento.getTab_cabe_asiento().setValor("observacion_cnccc", tex_observacion.getValue().toString());
                }
            } else {
                if (guardarPantalla().isEmpty()) {
                    dibujarSaldarAnticipo();
                }
            }
        }
    }

    /**
     * Validaciones de la Transaccion CXC de Pago
     *
     * @return
     */
    public boolean validarAnticipoEmpleado() {
        if (aut_anticipo.getValor() == null) {
            utilitario.agregarMensajeInfo("Debe seleccionar un 'ANTICIPO EMPLEADO'", "");
            return false;
        }

        if (tab_tabla1.isEmpty()) {
            utilitario.agregarMensajeInfo("No tiene Documentos por Pagar ", "");
            return false;
        }

        if (tex_observacion.getValue() == null || tex_observacion.getValue().toString().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe ingresar una 'OBSERVACIÓN' ", "");
            return false;
        }

        if (tab_tabla1.getListaFilasSeleccionadas().isEmpty()) {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un Documento por Pagar", "");
            return false;
        }

        if (tex_suma_facturas.getValue() != null) {
            try {
                if (Double.parseDouble(tex_suma_facturas.getValue().toString()) < 0) {
                    utilitario.agregarMensajeError("El 'SALDO SELECCIONADO' no es válido", "");
                    return false;
                }
            } catch (Exception e) {
                utilitario.agregarMensajeError("El 'SALDO SELECCIONADO' no es válido", "");
                return false;
            }
        }

        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[8] + "") + total;
        }
        total = Double.parseDouble(utilitario.getFormatoNumero(total));
        double valor_a_pagar = Double.parseDouble(utilitario.getFormatoNumero(Double.parseDouble(tex_saldo_anticipo.getValue() + "")));
        valor_a_pagar = Math.abs(valor_a_pagar);
        if (total > valor_a_pagar) {
            utilitario.agregarMensajeError("El 'SALDO SELECCIONADO' es mayor que el 'VALOR ANTICIPO'", "");
            return false;
        }
        return true;
    }

    public void cambiaHaceAsiento() {
        if (rad_hace_asiento.getValue().equals("true")) {
            tex_num_asiento.setRendered(false);
            eti_num_asiento.setRendered(false);
        } else {
            tex_num_asiento.setRendered(true);
            eti_num_asiento.setRendered(true);
        }
        utilitario.addUpdate("gri");
    }

    public void seleccionarAnticipoEmpleado(SelectEvent evt) {
        aut_anticipo.onSelect(evt);
        if (aut_anticipo.getValor() != null) {
            tex_saldo_anticipo.setValue(aut_anticipo.getValorArreglo(2));
        } else {
            tex_saldo_anticipo.setValue(null);
        }
        utilitario.addUpdate("tex_saldo_anticipo");
    }

    public void deseleccionaFacturaCxP(UnselectEvent evt) {
        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[8] + "") + total;
        }
        tex_suma_facturas.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_suma_facturas");

    }

    public void seleccionaFacturaCxP(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        double total = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            total = Double.parseDouble(actual.getCampos()[8] + "") + total;
        }
        tex_suma_facturas.setValue(utilitario.getFormatoNumero(total));
        utilitario.addUpdate("tex_suma_facturas");
    }

    public void GuardaCertificacion() {
        tab_tabla2.guardar();
        guardarPantalla();
        utilitario.addUpdateTabla(tab_tabla2, "", "");
    }

    public void CalcularSuma(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt); //Siempre es la primera linea
        double valor_comprometido = 0;
        double valor_saldo = 0;

        valor_comprometido = Double.parseDouble(tab_tabla2.getValor("valor_devengar_prcof"));
        valor_saldo = Double.parseDouble(tab_tabla2.getValor("saldo_devengar_prcof"));
        if (valor_comprometido <= 0) {
            utilitario.agregarMensajeError("Ingrese un valor positivo o mayor a cero", "Ingrese un valor positivo o mayor a cero");
            tab_tabla2.setValor("valor_devengar_prcof", "0");
            utilitario.addUpdateTabla(tab_tabla2, "valor_devengar_prcof", "");
            return;
        }
        if (valor_saldo < valor_comprometido) {
            utilitario.agregarMensajeError("No puede Exceder valor asignado", "Ingrese un valor menor igual al disponible en saldo: " + valor_saldo);
            tab_tabla2.setValor("valor_devengar_prcof", "0");
            utilitario.addUpdateTabla(tab_tabla2, "valor_devengar_prcof", "");
            return;
        }

        //tab_tramite.setValor("total_compromiso_prtra",tab_poa_tramite.getSumaColumna("comprometido_prpot")+"");
        //tab_tramite.modificar(tab_tramite.getFilaActual());
        //utilitario.addUpdateTabla(tab_tramite, "total_compromiso_prtra","");	
    }

    public void buscaCertificacion() {
        if (tab_tabla1.getTotalFilas() > 0) {
            //System.out.println("valor del texto "+txt_nro_certificacion.getValue());
            if (!txt_nro_certificacion.getValue().equals("")) {

                //System.out.println("valor texto "+txt_nro_certificacion.getValue().toString());
                sel_certificacion.getTab_seleccion().setSql(ser_presupuesto.getCompromisoRua(txt_nro_certificacion.getValue().toString(), "1"));
                sel_certificacion.getTab_seleccion().ejecutarSql();
                sel_certificacion.dibujar();

            } else {
                utilitario.agregarMensajeError("Ingrese Certificación", "Por favor ingrese el nro. de certificación ");
                return;
            }
        } else {
            utilitario.agregarMensajeError("No existe facturas", "Por favor para agregar una partida presupuestaria debe existir generada una factura");
            return;
        }

    }

    public void aceptarBusqueda() {
        if (sel_certificacion.isVisible()) {
            String str_seleccionados = sel_certificacion.getSeleccionados();
            if (str_seleccionados != "") {
                //System.out.println("entre seleccionado ");                
                TablaGenerica tab_insertar = utilitario.consultar(ser_presupuesto.getCompromisoRua(sel_certificacion.getSeleccionados(), "2"));
                tab_insertar.getSql();
                //System.out.println("codigo factura "+tab_tabla1.getFilaSeleccionada().getRowKey());
                for (int i = 0; i < tab_insertar.getTotalFilas(); i++) {

                    tab_tabla2.insertar();
                    tab_tabla2.setValor("ide_prpot", tab_insertar.getValor(i, "ide_prpot"));
                    tab_tabla2.setValor("valor_devengar_prcof", tab_insertar.getValor(i, "saldoxdevengado"));
                    tab_tabla2.setValor("saldo_devengar_prcof", tab_insertar.getValor(i, "saldoxdevengado"));
                    tab_tabla2.setValor("ide_cpcfa", tab_tabla1.getFilaSeleccionada().getRowKey());
                }
                sel_certificacion.cerrar();
            } else {
                utilitario.agregarMensajeError("Seleccione un Registro", "Debe seleccionar un registro para contnuar");
                return;
            }
        }
    }

    public void seleccionarDocumento(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla2.setCondicion("ide_cpcfa=" + tab_tabla1.getValorSeleccionado());
        tab_tabla2.ejecutarSql();

    }

    public void cargarDocumetosProveedor(SelectEvent evt) {
        aut_proveedor.onSelect(evt);
        if (aut_proveedor.getValor() != null) {
            tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosProveedor(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), aut_proveedor.getValor()));
            tab_tabla1.ejecutarSql();
            tab_tabla2.setCondicion("ide_cpcfa=" + tab_tabla1.getValorSeleccionado());
            tab_tabla2.ejecutarSql();
        } else {
            tab_tabla1.limpiar();
            tab_tabla2.limpiar();
        }
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();
    }
    Map parametro = new HashMap();

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getReporteSelecionado().equals("Comprobante de Retención")) {
            if (rep_reporte.isVisible()) {
                if (tab_tabla1.getValor("ide_cncre") != null) {
                    parametro = new HashMap();
                    rep_reporte.cerrar();
                    parametro.put("ide_cncre", Long.parseLong(tab_tabla1.getValor("ide_cncre")));
                    sel_rep.setSeleccionFormatoReporte(parametro, rep_reporte.getPath());
                    sel_rep.dibujar();
                    utilitario.addUpdate("rep_reporte,sel_rep");
                } else {
                    utilitario.agregarMensajeInfo("Seleccione un Comprobante de Retención", "");
                }

            }
        }
    }

    public void dibujarDocumentos() {

        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Documento");
        bot_ver.setMetodo("verDocumento");
        bot_ver.setIcon("ui-icon-search");

        bar_menu.agregarComponente(bot_ver);

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Documento");
        bot_anular.setIcon("ui-icon-cancel");
        bot_anular.setMetodo("abrirAnularDocumento");
        bar_menu.agregarComponente(bot_anular);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("ide_cncre").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setVisible(false);
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[5] eq '" + utilitario.getVariable("p_cxp_estado_factura_anulada") + "' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(1, "LISTADO DE DOCUMENTOS POR PAGAR", gru);
    }

    public void dibujarDocumentosNoContabilizadas() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_asi = new Boton();
        bot_asi.setValue("Generar Asiento Contable");
        bot_asi.setMetodo("abrirGeneraAsiento");
        bar_menu.agregarComponente(bot_asi);
        bar_menu.agregarSeparador();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Factura");
        bot_ver.setMetodo("abrirVerFactura");
        bar_menu.agregarComponente(bot_ver);
        eti1.setId("eti1");
        eti1.setValue("NUM. SELECCIONADOS : 0  -  VALOR : 0.00");
        eti1.setEstiloCabecera("");
        bar_menu.agregarComponente(eti1);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_seleccion");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoContabilidad(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.onSelectCheck("sumarSeleccionNoConta");
        tab_tabla1.onUnselectCheck("sumarSeleccionNoConta");
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        //tab_tabla1.setLectura(true);
        tab_tabla1.setTipoSeleccion(true);
        tab_tabla1.setSeleccionTabla("multiple");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);

        mep_menu.dibujar(2, "DOCUMENTOS POR PAGAR SIN COMPROBANTE CONTABLE", gru);
    }

    public void sumarSeleccionNoConta() {
        double dou_suma = 0;
        for (Fila actual : tab_tabla1.getSeleccionados()) {
            double valor = 0;
            try {
                valor = Double.parseDouble(String.valueOf(actual.getCampos()[tab_tabla1.getNumeroColumna("total_cpcfa")]));
            } catch (Exception e) {
            }
            dou_suma += valor;
        }
        eti1.setValue("NUM. SELECCIONADOS : " + tab_tabla1.getSeleccionados().length + "  -  VALOR : " + utilitario.getFormatoNumero(dou_suma));
        utilitario.addUpdate("eti1");
    }

    public void abrirGeneraAsiento() {
        if (tab_tabla1.getFilasSeleccionadas() != null && tab_tabla1.getFilasSeleccionadas().length() > 0) {
            asc_asiento.nuevoAsiento();
            asc_asiento.dibujar();
            asc_asiento.setAsientoDocumentosCxP(tab_tabla1.getFilasSeleccionadas());
            asc_asiento.getBot_aceptar().setMetodo("guardar");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar almenos una Factura", "");
        }
    }

    public void dibujarDocumentosNoRetencion() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Generar Comprobante de Retención");
        bot_ver.setMetodo("abrirRetencion");
        bar_menu.agregarComponente(bot_ver);

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoRetencion(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(3, "DOCUMENTOS POR PAGAR SIN COMPROBANTE DE RETENCION", gru);
    }

    public void dibujarDocumentosAnulados() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosAnulados(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.setCampoPrimaria("ide_cpcfa");
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("ide_cpefa").setVisible(false);
        tab_tabla1.getColumna("nombre_cpefa").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.setRows(20);
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(4, "DOCUMENTOS POR PAGAR ANULADOS", gru);
    }

    public void dibujarDocumentosPorPagar() {
        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosPorPagar(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
        tab_tabla1.getColumna("saldo_x_pagar").setEstilo("font-size: 13px;font-weight: bold");
        tab_tabla1.getColumna("saldo_x_pagar").alinearDerecha();
        tab_tabla1.setCampoPrimaria("ide_cpctr");
        tab_tabla1.getColumna("ide_cpctr").setVisible(false);
        tab_tabla1.getColumna("ide_cpcfa").setVisible(false);
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla1.getColumna("fecha").setVisible(true);
        tab_tabla1.getColumna("numero_cpcfa").setNombreVisual("N. FACTURA");
        tab_tabla1.getColumna("numero_cpcfa").setFiltroContenido();
        tab_tabla1.getColumna("saldo_x_pagar").setNombreVisual("SALDO");
        tab_tabla1.getColumna("total_cpcfa").setNombreVisual("TOTAL");
        tab_tabla1.getColumna("total_cpcfa").setEstilo("font-size: 13px;");
        tab_tabla1.getColumna("total_cpcfa").alinearDerecha();
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("saldo_x_pagar");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(5, "DOCUMENTOS PENDIENTES DE PAGO", gru);
    }

    public void dibujarGraficoCompras() {
        Grupo grupo = new Grupo();
        gca_facturas = new GraficoCartesiano();
        gca_facturas.setId("gca_facturas");

        gpa_facturas = new GraficoPastel();
        gpa_facturas.setId("gpa_facturas");
        gpa_facturas.setShowDataLabels(true);
        gpa_facturas.setStyle("width:300px;");

        com_periodo = new Combo();
        com_periodo.setMetodo("actualizarFiltros");
        com_periodo.setCombo(ser_cuentas_cxp.getSqlAniosFacturacion());
        com_periodo.eliminarVacio();
        com_periodo.setValue(utilitario.getAnio(utilitario.getFechaActual()));

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_cuentas_cxp.getSqlTotalComprasMensuales(String.valueOf(com_periodo.getValue())));
        tab_tabla1.setLectura(true);
        tab_tabla1.setColumnaSuma("num_documentos,compras12,compras0,iva,total");
        tab_tabla1.getColumna("num_documentos").alinearDerecha();
        tab_tabla1.getColumna("compras12").alinearDerecha();
        tab_tabla1.getColumna("compras0").alinearDerecha();
        tab_tabla1.getColumna("iva").alinearDerecha();
        tab_tabla1.getColumna("total").alinearDerecha();
        tab_tabla1.dibujar();
        Grid gri_opciones = new Grid();
        gri_opciones.setColumns(2);
        gri_opciones.getChildren().add(new Etiqueta("<strong>PERÍODO :</strong>"));
        gri_opciones.getChildren().add(com_periodo);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.getChildren().add(gri_opciones);
        pat_panel.setPanelTabla(tab_tabla1);
        Grid gri = new Grid();
        gri.setWidth("100%");
        gri.setColumns(2);
        gpa_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "num_documentos");
        gri.getChildren().add(pat_panel);
        gri.getChildren().add(gpa_facturas);
        grupo.getChildren().add(gri);

        gca_facturas.setTitulo("COMPRAS MENSUALES");
        gca_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));
        grupo.getChildren().add(gca_facturas);
        mep_menu.dibujar(6, "GRAFICOS DE COMPRAS", grupo);
    }

    public void dibujarFacturaElectronica() {

    }

    public void abrirAnularDocumento() {
        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            con_confirma.getBot_aceptar().setMetodo("anularDocumento");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Documento CxP", "");
        }

    }

    public void anularDocumento() {
        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            ser_cuentas_cxp.anularDocumento(tab_tabla1.getValor("ide_cpcfa"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                tab_tabla1.actualizar();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar un Documento CxP", "");
        }

    }

    public void verDocumento() {
        if (mep_menu.getOpcion() == 2) {
            if (tab_tabla1.getSeleccionados() != null && tab_tabla1.getSeleccionados().length > 0) {
                dcp_documento.verDocumento(tab_tabla1.getSeleccionados()[0].getRowKey());
                dcp_documento.dibujar();
            } else {
                utilitario.agregarMensajeInfo("Debe seleccionar una Factura", "");
            }
        } else if (tab_tabla1.getValorSeleccionado() != null) {
            dcp_documento.verDocumento(tab_tabla1.getValorSeleccionado());
            dcp_documento.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione un Documento", "");
        }
    }

    public void actualizarFiltros() {
        switch (mep_menu.getOpcion()) {
            case 1:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentos(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 2:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoContabilidad(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 3:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosNoRetencion(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 4:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosAnulados(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 5:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlDocumentosPorPagar(cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha(), String.valueOf(com_tipo_documento.getValue())));
                tab_tabla1.ejecutarSql();
                break;
            case 6:
                tab_tabla1.setSql(ser_cuentas_cxp.getSqlTotalComprasMensuales(String.valueOf(com_periodo.getValue())));
                tab_tabla1.ejecutarSql();
                gca_facturas.limpiar();
                gca_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "total", "COMPRAS " + String.valueOf(com_periodo.getValue()));
                gpa_facturas.limpiar();
                gpa_facturas.agregarSerie(tab_tabla1, "nombre_gemes", "num_documentos");
                utilitario.addUpdate("gca_facturas,gpa_facturas");
                break;
            default:
                break;
        }

    }

    @Override
    public void insertar() {

        dcp_documento.nuevoDocumento();
        dcp_documento.dibujar();
        //Limita fecha maxima
        for (UIComponent componenteActual : dcp_documento.getTab_cab_documento().getGrid().getChildren()) {
            if (componenteActual.getRendererType() != null && componenteActual.getRendererType().equals("org.primefaces.component.CalendarRenderer")) {
                Calendario cal = (Calendario) componenteActual;
                cal.setMaxdate(new Date());
                break;
            }
        }

    }

    public void eliminar7() {
        if (mep_menu.getOpcion() == 7) {
            tab_tabla2.eliminar();
        }
    }

    public void insertar7() {
        if (mep_menu.getOpcion() == 7) {
            if (tab_tabla2.isFocus()) {
                if (tab_tabla1.isEmpty() == false) {
                    tab_tabla2.insertar();
                    tab_tabla2.setValor("ide_cpcfa", tab_tabla1.getValorSeleccionado());
                } else {
                    utilitario.agregarMensajeInfo("Seleccione un Documento por pagar", "");
                }
            }
        }
    }

    public void guardar7() {
        if (mep_menu.getOpcion() == 7) {
            tab_tabla2.guardar();
            guardarPantalla();
        }
    }

    @Override
    public void guardar() {
        if (dcp_documento.isVisible()) {
            dcp_documento.guardar();
            if (dcp_documento.isVisible() == false) {
                //actualiza el punto de emision seleccionado y la tabla                
                dibujarDocumentos();
                tab_tabla1.setFilaActual(dcp_documento.getTab_cab_documento().getValor("ide_cpcfa"));
                utilitario.addUpdate("tab_tabla1");
                con_confirmar.dibujar();
            }
        } else if (ret_retencion.isVisible()) {
            ret_retencion.guardar();
            if (ret_retencion.isVisible() == false) {
                dibujarDocumentos();
                tab_tabla1.setFilaActual(ret_retencion.getIde_cpcfa());
                utilitario.addUpdate("tab_tabla1");
            }
        } else if (asc_asiento.isVisible()) {
            asc_asiento.guardar();
            if (asc_asiento.isVisible() == false) {

                if (mep_menu.getOpcion() == 8) {
                    //asocia asiento a transacciones seleccionadas
                    String ide_cpctr = "";

                    for (Fila actual : tab_tabla1.getSeleccionados()) {
                        if (ide_cpctr.isEmpty() == false) {
                            ide_cpctr += ",";
                        }
                        ide_cpctr += actual.getCampos()[tab_tabla1.getNumeroColumna("ide_cpctr")];

                        utilitario.getConexion().ejecutarSql("UPDATE cxp_cabece_factur SET ide_cnccc=" + asc_asiento.getIde_cnccc() + " where ide_cpcfa=" + actual.getCampos()[tab_tabla1.getNumeroColumna("ide_cpcfa")] + "");
                    }
                    utilitario.getConexion().ejecutarSql("UPDATE cxp_detall_transa SET ide_cnccc=" + asc_asiento.getIde_cnccc() + " where ide_cpctr in(" + ide_cpctr + ") and ide_cnccc is null");
                    String ide_ccctr = aut_anticipo.getValor();
                    utilitario.getConexion().ejecutarSql("UPDATE cxc_detall_transa SET ide_cnccc=" + asc_asiento.getIde_cnccc() + " where ide_ccctr =" + ide_ccctr + " and ide_cnccc is null");

                }
                dibujarDocumentos();
            }
        }
    }

    public void abrirRetencion() {
        if (tab_tabla1.getValor("ide_cpcfa") != null) {
            ret_retencion.nuevaRetencionCompra(tab_tabla1.getValor("ide_cpcfa"));
            ret_retencion.dibujar();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    @Override
    public void eliminar() {

    }

    /**
     * Abre el asiento contable seleccionado
     *
     * @param evt
     */
    public void abrirAsiento(ActionEvent evt) {
        Link lin_ide_cnccc = (Link) evt.getComponent();
        asc_asiento.setAsientoContable(lin_ide_cnccc.getValue().toString());
        tab_tabla1.setFilaActual(lin_ide_cnccc.getDir());
        asc_asiento.dibujar();
    }

    public DocumentoCxP getDcp_documento() {
        return dcp_documento;
    }

    public void setDcp_documento(DocumentoCxP dcp_documento) {
        this.dcp_documento = dcp_documento;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Retencion getRet_retencion() {
        return ret_retencion;
    }

    public void setRet_retencion(Retencion ret_retencion) {
        this.ret_retencion = ret_retencion;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(SeleccionFormatoReporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public Tabla getTab_seleccion() {
        return tab_tabla1;
    }

    public void setTab_seleccion(Tabla tab_tabla) {
        this.tab_tabla1 = tab_tabla;
    }

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

    public GraficoCartesiano getGca_facturas() {
        return gca_facturas;
    }

    public void setGca_facturas(GraficoCartesiano gca_facturas) {
        this.gca_facturas = gca_facturas;
    }

    public Confirmar getCon_confirmar() {
        return con_confirmar;
    }

    public void setCon_confirmar(Confirmar con_confirmar) {
        this.con_confirmar = con_confirmar;
    }

    public AutoCompletar getAut_proveedor() {
        return aut_proveedor;
    }

    public void setAut_proveedor(AutoCompletar aut_proveedor) {
        this.aut_proveedor = aut_proveedor;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public SeleccionTabla getSel_certificacion() {
        return sel_certificacion;
    }

    public void setSel_certificacion(SeleccionTabla sel_certificacion) {
        this.sel_certificacion = sel_certificacion;
    }

    public AutoCompletar getAut_anticipo() {
        return aut_anticipo;
    }

    public void setAut_anticipo(AutoCompletar aut_anticipo) {
        this.aut_anticipo = aut_anticipo;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

}
