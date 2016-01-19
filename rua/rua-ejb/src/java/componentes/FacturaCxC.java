/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Ajax;
import framework.componentes.AreaTexto;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.selectbooleanbutton.SelectBooleanButton;
import org.primefaces.event.SelectEvent;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class FacturaCxC extends Dialogo {

    private final Utilitario utilitario = new Utilitario();

    /////FACTURA
    @EJB
    private final ServicioFacturaCxC ser_factura = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);
    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);

    private final Tabulador tab_factura = new Tabulador();
    private Tabla tab_cab_factura = new Tabla();
    private Tabla tab_deta_factura = new Tabla();
    private SelectBooleanButton sbb_factura = new SelectBooleanButton();
    private final AreaTexto ate_observacion = new AreaTexto();
    private final Texto tex_subtotal12 = new Texto();
    private final Texto tex_subtotal0 = new Texto();
    private final Texto tex_iva = new Texto();
    private final Texto tex_total = new Texto();
    private final Combo com_pto_emision = new Combo();

    //CONTABILIDAD Asiento de Venta
    private Tabla tab_cab_conta = new Tabla();
    private Tabla tab_deta_conta = new Tabla();

    //OPCIONES
    private boolean lectura = false;
    private String ide_cccfa = "-1";
    private String ide_cnccc = "-1";

    public FacturaCxC() {
        this.setWidth("95%");
        this.setHeight("90%");
        this.setTitle("GENERAR FACTURA DE VENTA");
        this.setResizable(false);
        this.setDynamic(false);

        tab_factura.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + (getAltoPanel() - 10) + "px;overflow: auto;display: block;");
        tab_factura.setId("tab_factura");
        tab_factura.setWidgetVar("w_factura");
        tab_factura.agregarTab("FACTURA ", null);//0 
        tab_factura.agregarTab("ASIENTO DE VENTA", null);//1
        tab_factura.agregarTab("ASIENTO DE COSTO", null);//2
        tab_factura.agregarTab("INVENTARIO", null);//3
        tab_factura.agregarTab("RETENCIÓN", null);//4
        this.setDialogo(tab_factura);

    }

    public void setFacturaCxC(String titulo) {
        tab_factura.getTab(0).getChildren().clear();
        tab_factura.getTab(1).getChildren().clear();
        tab_factura.getTab(2).getChildren().clear();
        tab_factura.getTab(3).getChildren().clear();
        this.setTitle(titulo);

        tab_factura.getTab(0).getChildren().add(dibujarFactura());
        tab_factura.getTab(1).getChildren().add(dibujarAsientoVenta());
    }

    /**
     * Configuraciones para crear una factura
     */
    public void nuevaFactura() {
        tab_cab_factura.limpiar();
        tab_cab_factura.insertar();
        tab_deta_factura.limpiar();
        ate_observacion.limpiar();
        sbb_factura.setValue(false);
        sbb_factura.setDisabled(false);
        tex_iva.setValue("0,00");
        tex_subtotal0.setValue("0,00");
        tex_subtotal12.setValue("0,00");
        tex_total.setValue("0,00");
        cargarMaximoSecuencial();
        setActivarRetencion(false); //Desactiva la retención
    }

    /**
     * Configuraciones para cargar una factura
     *
     * @param ide_cccfa
     */
    public void cargarFactura(String ide_cccfa) {
        //Carga la Factura
        tab_cab_factura.setCondicion("ide_cccfa=" + ide_cccfa);
        tab_cab_factura.ejecutarSql();
        com_pto_emision.setValue(tab_cab_factura.getValor("ide_ccdaf"));
        tab_deta_factura.setValorForanea(tab_cab_factura.getValorSeleccionado());
        tab_deta_factura.ejecutarSql();
        sbb_factura.setDisabled(true);
        tex_iva.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("valor_iva_cccfa")));
        //Carga totales y observacion
        double dou_subt0 = 0;
        double dou_subtno = 0;
        try {
            dou_subt0 = Double.parseDouble(tab_cab_factura.getValor("base_tarifa0_cccfa"));
        } catch (Exception e) {
        }
        try {
            dou_subtno = Double.parseDouble(tab_cab_factura.getValor("base_no_objeto_iva_cccfa"));
        } catch (Exception e) {
        }
        tex_subtotal0.setValue(utilitario.getFormatoNumero(dou_subt0 + dou_subtno));
        tex_subtotal12.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("base_grabada_cccfa")));
        tex_total.setValue(utilitario.getFormatoNumero(tab_cab_factura.getValor("total_cccfa")));
        ate_observacion.setValue(tab_cab_factura.getValor("observacion_cccfa "));

        //Si la factura esta anulada pone de lectura
        if (tab_cab_factura.getValor("ide_ccefa").equals(utilitario.getVariable("p_cxc_estado_factura_anulada"))) {
            tab_cab_factura.getFilaSeleccionada().setLectura(true);
            ate_observacion.setDisabled(true);
            com_pto_emision.setDisabled(true);
        } else {
            ate_observacion.setDisabled(false);
            com_pto_emision.setDisabled(false);
        }
    }

    /**
     * Configuraciones para Tab de Factura
     *
     * @return
     */
    private Grupo dibujarFactura() {
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        com_pto_emision.setMetodoRuta("pre_index.clase." + getId() + ".cargarMaximoSecuencial");
        com_pto_emision.eliminarVacio();

        Grupo grupo = new Grupo();
        Grid gri_pto = new Grid();
        gri_pto.setColumns(4);
        gri_pto.getChildren().add(new Etiqueta("<strong>PUNTO DE EMISIÓN :</strong>"));
        gri_pto.getChildren().add(com_pto_emision);

        gri_pto.getChildren().add(new Etiqueta("<strong>&nbsp;SOLO GUARDAR LA FACTURA ?</strong>"));
        sbb_factura = new SelectBooleanButton();
        sbb_factura.setValue(false);
        sbb_factura.setOnLabel("Si");
        sbb_factura.setOffLabel("No");
        sbb_factura.setOnIcon("ui-icon-check");
        sbb_factura.setOffIcon("ui-icon-close");
        sbb_factura.setStyle("width:60px");
        Ajax aja_click = new Ajax();
        aja_click.setMetodoRuta("pre_index.clase." + getId() + ".soloGuarda");
        aja_click.setGlobal(false);
        sbb_factura.addClientBehavior("change", aja_click);
        gri_pto.getChildren().add(sbb_factura);

        grupo.getChildren().add(gri_pto);

        tab_cab_factura = new Tabla();
        tab_deta_factura = new Tabla();
        tab_cab_factura.setRuta("pre_index.clase." + getId());
        tab_cab_factura.setId("tab_cab_factura");
        tab_cab_factura.setIdCompleto("tab_factura:tab_cab_factura");
        tab_cab_factura.setTabla("cxc_cabece_factura", "ide_cccfa", -1);
        tab_cab_factura.setMostrarNumeroRegistros(false);
        tab_cab_factura.getColumna("ide_cnccc").setVisible(false);
        tab_cab_factura.getColumna("ide_cccfa").setVisible(false);
        tab_cab_factura.getColumna("ide_cncre").setVisible(false);
        tab_cab_factura.getColumna("ide_vgven").setCombo("ven_vendedor", "ide_vgven", "nombre_vgven", "");
        tab_cab_factura.getColumna("ide_vgven").setNombreVisual("VENDEDOR");
        tab_cab_factura.getColumna("ide_vgven").setOrden(5);
        tab_cab_factura.getColumna("ide_cntdo").setVisible(false);
        tab_cab_factura.getColumna("ide_cntdo").setValorDefecto(utilitario.getVariable("p_con_tipo_documento_factura"));
        tab_cab_factura.getColumna("ide_ccefa").setVisible(false);
        tab_cab_factura.getColumna("ide_ccefa").setValorDefecto(utilitario.getVariable("p_cxc_estado_factura_normal"));
        tab_cab_factura.getColumna("ide_geubi").setVisible(false);
        tab_cab_factura.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_factura.getColumna("ide_usua").setVisible(false);
        tab_cab_factura.getColumna("fecha_trans_cccfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_factura.getColumna("fecha_trans_cccfa").setVisible(false);
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setOrden(2);
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setValorDefecto(utilitario.getFechaActual());
        tab_cab_factura.getColumna("fecha_emisi_cccfa").setNombreVisual("FECHA EMISION");
        tab_cab_factura.getColumna("ide_ccdaf").setVisible(false);
        tab_cab_factura.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        tab_cab_factura.getColumna("ide_geper").setAutoCompletar();
        tab_cab_factura.getColumna("ide_geper").setOrden(3);
        tab_cab_factura.getColumna("ide_geper").setRequerida(true);
        tab_cab_factura.getColumna("ide_geper").setMetodoChangeRuta(tab_cab_factura.getRuta() + ".cargarDireccionCliente");
        tab_cab_factura.getColumna("ide_geper").setNombreVisual("CLIENTE");
        tab_cab_factura.getColumna("pagado_cccfa").setValorDefecto("false");
        tab_cab_factura.getColumna("pagado_cccfa").setVisible(false);
        tab_cab_factura.getColumna("total_cccfa").setEtiqueta();
        tab_cab_factura.getColumna("total_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura.getColumna("total_cccfa").setValorDefecto("0,00");
        tab_cab_factura.getColumna("secuencial_cccfa").setEstilo("font-size: 14px;font-weight: bold;text-align: right;color:red");
        tab_cab_factura.getColumna("secuencial_cccfa").setTipoJava("java.lang.Integer");
        tab_cab_factura.getColumna("secuencial_cccfa").setLongitud(10);
        tab_cab_factura.getColumna("secuencial_cccfa").setOrden(1);
        tab_cab_factura.getColumna("secuencial_cccfa").setRequerida(true);
        tab_cab_factura.getColumna("secuencial_cccfa").setNombreVisual("SECUENCIAL");
        tab_cab_factura.getColumna("secuencial_cccfa").setMascara("9999999");
        tab_cab_factura.getColumna("base_grabada_cccfa").setEtiqueta();
        tab_cab_factura.getColumna("base_grabada_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura.getColumna("base_grabada_cccfa").setValorDefecto("0");
        tab_cab_factura.getColumna("valor_iva_cccfa").setEtiqueta();
        tab_cab_factura.getColumna("valor_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura.getColumna("valor_iva_cccfa").setValorDefecto("0");
        tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setEtiqueta();
        tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setValorDefecto("0");
        tab_cab_factura.getColumna("base_tarifa0_cccfa").setEtiqueta();
        tab_cab_factura.getColumna("base_tarifa0_cccfa").setEstilo("font-size: 16px;font-weight: bold;text-decoration: underline");
        tab_cab_factura.getColumna("base_tarifa0_cccfa").setValorDefecto("0");
        tab_cab_factura.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cndfp!=" + utilitario.getVariable("p_con_for_pag_reembolso_caja"));
        tab_cab_factura.getColumna("ide_cndfp").setPermitirNullCombo(false);
        tab_cab_factura.getColumna("ide_cndfp").setOrden(4);
        tab_cab_factura.getColumna("ide_cndfp").setNombreVisual("FORMA DE PAGO");

        tab_cab_factura.getColumna("DIRECCION_CCCFA").setOrden(6);
        tab_cab_factura.getColumna("DIRECCION_CCCFA").setNombreVisual("DIRECCIÓN");
        tab_cab_factura.getColumna("OBSERVACION_CCCFA").setVisible(false);
        tab_cab_factura.getColumna("ide_cndfp").setValorDefecto(utilitario.getVariable("p_con_deta_pago_efectivo"));
        tab_cab_factura.getColumna("solo_guardar_cccfa").setValorDefecto("false");
        tab_cab_factura.getColumna("solo_guardar_cccfa").setVisible(false);

        tab_cab_factura.getColumna("ide_geubi").setVisible(false);
        tab_cab_factura.getColumna("ide_usua").setVisible(false);
        tab_cab_factura.setTipoFormulario(true);
        tab_cab_factura.getGrid().setColumns(6);
        tab_cab_factura.agregarRelacion(tab_deta_factura);
        tab_cab_factura.setCondicion("ide_cccfa=" + ide_cccfa);
        tab_cab_factura.setCondicionSucursal(true);

        tab_cab_factura.getColumna("base_grabada_cccfa").setVisible(false);
        tab_cab_factura.getColumna("base_no_objeto_iva_cccfa").setVisible(false);
        tab_cab_factura.getColumna("valor_iva_cccfa").setVisible(false);
        tab_cab_factura.getColumna("base_tarifa0_cccfa").setVisible(false);
        tab_cab_factura.getColumna("total_cccfa").setVisible(false);
        tab_cab_factura.setLectura(lectura);
        tab_cab_factura.dibujar();

        tab_deta_factura.setId("tab_deta_factura");
        tab_deta_factura.setIdCompleto("tab_factura:tab_deta_factura");
        tab_deta_factura.setRuta("pre_index.clase." + getId());
        tab_deta_factura.setTabla("cxc_deta_factura", "ide_ccdfa", -1);
        tab_deta_factura.getColumna("ide_ccdfa").setVisible(false);
        tab_deta_factura.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_deta_factura.getColumna("ide_inarti").setAutoCompletar();
        tab_deta_factura.getColumna("ide_inarti").setNombreVisual("ARTICULO");
        tab_deta_factura.getColumna("ide_inarti").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".seleccionarProducto");
        tab_deta_factura.getColumna("ide_inarti").setRequerida(true);
        tab_deta_factura.getColumna("SECUENCIAL_CCDFA").setVisible(false);
        tab_deta_factura.getColumna("ide_inarti").setOrden(1);
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setNombreVisual("CANTIDAD");
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setOrden(2);
        tab_deta_factura.getColumna("CANTIDAD_CCDFA").setRequerida(true);
        tab_deta_factura.getColumna("PRECIO_CCDFA").setNombreVisual("PRECIO");
        tab_deta_factura.getColumna("PRECIO_CCDFA").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("PRECIO_CCDFA").setOrden(3);
        tab_deta_factura.getColumna("PRECIO_CCDFA").setRequerida(true);
        List lista = new ArrayList();
        Object fila1[] = {
            "1", "SI"
        };
        Object fila2[] = {
            "-1", "NO"
        };
        Object fila3[] = {
            "0", "NO  OBJETO"
        };
        lista.add(fila1);
        lista.add(fila2);
        lista.add(fila3);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setCombo(lista);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setPermitirNullCombo(false);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setOrden(4);
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setNombreVisual("IVA");
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setMetodoChangeRuta(tab_deta_factura.getRuta() + ".cambioPrecioCantidadIva");
        tab_deta_factura.getColumna("iva_inarti_ccdfa").setLongitud(-1);
        tab_deta_factura.getColumna("total_ccdfa").setNombreVisual("TOTAL");
        tab_deta_factura.getColumna("total_ccdfa").setOrden(5);
        tab_deta_factura.getColumna("OBSERVACION_CCDFA").setNombreVisual("OBSERVACION");
        tab_deta_factura.getColumna("total_ccdfa").setEtiqueta();
        tab_deta_factura.getColumna("total_ccdfa").setEstilo("font-size:14px;font-weight: bold;");
        tab_deta_factura.getColumna("total_ccdfa").alinearDerecha();
        tab_deta_factura.getColumna("precio_promedio_ccdfa").setLectura(true);
        tab_deta_factura.setScrollable(true);
        tab_deta_factura.setScrollHeight(150);
        tab_deta_factura.setLectura(lectura);
        tab_deta_factura.setRecuperarLectura(true);
        tab_deta_factura.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_factura);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");

        grupo.getChildren().add(tab_cab_factura);
        grupo.getChildren().add(pat_panel);

        Grid gri_total = new Grid();
        gri_total.setWidth("100%");
        gri_total.setStyle("width:" + (getAnchoPanel() - 10) + "px;border:1px");

        gri_total.setColumns(2);

        Grid gri_observa = new Grid();
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong>"));
        ate_observacion.setCols(50);
        gri_observa.getChildren().add(ate_observacion);
        gri_total.getChildren().add(gri_observa);

        Grid gri_valores = new Grid();
        gri_valores.setId("gri_valores");
        gri_valores.setColumns(4);
        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTAL TARIFA 12% :<s/trong>"));
        tex_subtotal12.setDisabled(true);
        tex_subtotal12.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_subtotal12);

        gri_valores.getChildren().add(new Etiqueta("<strong>IVA 12% :<s/trong>"));
        tex_iva.setDisabled(true);
        tex_iva.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_iva);

        gri_valores.getChildren().add(new Etiqueta("<strong>SUBTAL TARIFA 0% :<s/trong>"));
        tex_subtotal0.setDisabled(true);
        tex_subtotal0.setStyle("font-size: 14px;text-align: right;width:110px");
        gri_valores.getChildren().add(tex_subtotal0);

        gri_valores.getChildren().add(new Etiqueta("<strong>TOTAL :<s/trong>"));
        tex_total.setDisabled(true);
        tex_total.setStyle("font-size: 14px;font-weight: bold;text-align: right;width:110px");

        gri_valores.getChildren().add(tex_total);

        gri_total.getChildren().add(gri_valores);

        grupo.getChildren().add(gri_total);
        return grupo;
    }

    public Grupo dibujarAsientoVenta() {
        Grupo grupo = new Grupo();

        tab_cab_conta = new Tabla();
        tab_deta_conta = new Tabla();
        tab_cab_conta.setRuta("pre_index.clase." + getId());
        tab_cab_conta.setId("tab_cab_conta");
        tab_cab_conta.setIdCompleto("tab_factura:tab_cab_conta");
        tab_cab_conta.setTabla("con_cab_comp_cont", "ide_cnccc", -1);
        tab_cab_conta.setCondicion("ide_cnccc=" + ide_cnccc);
        tab_cab_conta.getColumna("ide_cnccc").setNombreVisual("CODIGO");
        tab_cab_conta.getColumna("fecha_siste_cnccc").setVisible(false);
        tab_cab_conta.getColumna("numero_cnccc").setLectura(true);
        tab_cab_conta.getColumna("numero_cnccc").setEstilo("font-size:11px;font-weight: bold");
        tab_cab_conta.getColumna("numero_cnccc").setNombreVisual("NÚMERO");
        tab_cab_conta.getColumna("numero_cnccc").setOrden(3);
        tab_cab_conta.getColumna("fecha_siste_cnccc").setValorDefecto(utilitario.getFechaActual());
        tab_cab_conta.getColumna("fecha_trans_cnccc").setValorDefecto(utilitario.getFechaActual());
        tab_cab_conta.getColumna("fecha_trans_cnccc").setNombreVisual("FECHA");
        tab_cab_conta.getColumna("fecha_trans_cnccc").setOrden(1);
        tab_cab_conta.getColumna("hora_sistem_cnccc").setVisible(false);
        tab_cab_conta.getColumna("hora_sistem_cnccc").setValorDefecto(utilitario.getHoraActual());
        tab_cab_conta.getColumna("ide_usua").setVisible(false);
        tab_cab_conta.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_cab_conta.getColumna("ide_modu").setVisible(false);
        //tab_cab_conta.getColumna("ide_modu").setValorDefecto(""); ////////////////************
        tab_cab_conta.getColumna("ide_geper").setCombo(tab_cab_factura.getColumna("ide_geper").getListaCombo());
        tab_cab_conta.getColumna("ide_geper").setAutoCompletar();
        tab_cab_conta.getColumna("ide_geper").setLectura(true);
        tab_cab_conta.getColumna("ide_geper").setNombreVisual("BENEFICIARIO");
        tab_cab_conta.getColumna("ide_geper").setOrden(2);
        tab_cab_conta.getColumna("ide_cneco").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_normal"));
        tab_cab_conta.getColumna("ide_cneco").setVisible(false);
        tab_cab_conta.getColumna("ide_cntcm").setValorDefecto(utilitario.getVariable("p_con_tipo_comprobante_ingreso"));
        tab_cab_conta.getColumna("ide_cntcm").setVisible(false);
        tab_cab_conta.getColumna("OBSERVACION_CNCCC").setVisible(false);

        tab_cab_conta.setTipoFormulario(true);
        tab_cab_conta.getGrid().setColumns(6);
        tab_cab_conta.setMostrarNumeroRegistros(false);
        tab_cab_conta.setLectura(lectura);
        tab_cab_conta.agregarRelacion(tab_deta_conta);
        tab_cab_conta.dibujar();
        tab_cab_conta.insertar();

        tab_deta_conta.setRuta("pre_index.clase." + getId());
        tab_deta_conta.setId("tab_deta_conta");
        tab_deta_conta.setIdCompleto("tab_factura:tab_deta_conta");
        tab_deta_conta.setTabla("con_det_comp_cont", "ide_cndcc", -1);
        tab_deta_conta.getColumna("ide_cndpc").setCombo("con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc,nombre_cndpc", "ide_cncpc=0");
        tab_deta_conta.getColumna("ide_cndpc").setAutoCompletar();
        tab_deta_conta.getColumna("ide_cndpc").setNombreVisual("CUENTA");
        tab_deta_conta.getColumna("ide_cnlap").setCombo("con_lugar_aplicac", "ide_cnlap", "nombre_cnlap", "");
        tab_deta_conta.getColumna("ide_cnlap").setPermitirNullCombo(false);
        tab_deta_conta.getColumna("ide_cnlap").setNombreVisual("LUGAR APLICA");
        tab_deta_conta.getColumna("ide_cnlap").setRequerida(true);
        tab_deta_conta.getColumna("ide_cnlap").setLongitud(-1);
        tab_deta_conta.getColumna("ide_cnccc").setVisible(false);
        tab_deta_conta.getColumna("ide_cndcc").setVisible(false);
        tab_deta_conta.getColumna("ide_cndpc").setRequerida(true);
        tab_deta_conta.getColumna("valor_cndcc").setRequerida(true);
        tab_deta_conta.getColumna("valor_cndcc").setNombreVisual("VALOR");
        tab_deta_conta.getColumna("OBSERVACION_CNDCC").setNombreVisual("OBSERVACION");
        tab_deta_conta.setCampoOrden("ide_cnlap desc");
        tab_deta_conta.setScrollable(true);
        tab_deta_conta.setScrollHeight(150);
        tab_deta_conta.setLectura(lectura);
        tab_deta_conta.setRecuperarLectura(true);
        tab_deta_conta.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_deta_conta);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");

        grupo.getChildren().add(tab_cab_conta);
        grupo.getChildren().add(pat_panel);

        return grupo;
    }

    /**
     * Activa o desactiva las Tabs
     *
     * @param evt
     */
    public void soloGuarda(AjaxBehaviorEvent evt) {
        //Solo guarda
        String str_script_activa = tab_factura.getTab(1).isDisabled() == false ? "w_factura.enable(1);" : "";
        str_script_activa += tab_factura.getTab(2).isDisabled() == false ? "w_factura.enable(2);" : "";
        str_script_activa += tab_factura.getTab(3).isDisabled() == false ? "w_factura.enable(3);" : "";
        str_script_activa += tab_factura.getTab(4).isDisabled() == false ? "w_factura.enable(4);" : "";
        if (String.valueOf(sbb_factura.getValue()).equals("true")) {
            //Desactiva todas
            utilitario.ejecutarJavaScript("w_factura.disable(1);w_factura.disable(2);w_factura.disable(3);w_factura.disable(4)");
        } else {
            utilitario.ejecutarJavaScript(str_script_activa);
        }
    }

    /**
     * Carga el seccuencial maximo de un punto de emision
     */
    public void cargarMaximoSecuencial() {

        if (com_pto_emision.getValue() != null) {
            String secuencial = ser_factura.getSecuencialFactura(String.valueOf(com_pto_emision.getValue())) + "";
            String ceros = utilitario.generarCero(7 - secuencial.length());
            String num_max = ceros.concat(secuencial);
            tab_cab_factura.setValor("secuencial_cccfa", num_max);
            utilitario.addUpdateTabla(tab_cab_factura, "secuencial_cccfa", "");
        }
    }

    /**
     * Carga la dirección cuando se selecciona un cliente del Autocompletar
     *
     * @param evt
     */
    public void cargarDireccionCliente(SelectEvent evt) {
        tab_cab_factura.modificar(evt);
        if (tab_cab_factura.getValor("ide_geper") != null) {
            TablaGenerica tag_cliente = ser_cliente.getCliente(tab_cab_factura.getValor("ide_geper"));
            if (tag_cliente.isEmpty() == false) {
                tab_cab_factura.setValor("direccion_cccfa", tag_cliente.getValor("direccion_geper"));
                utilitario.addUpdateTabla(tab_cab_factura, "direccion_cccfa", "");
            }
        }
    }

    /**
     * Carga el precio de la ultima venta del producto al cliente seleccionado
     *
     * @param evt
     */
    public void seleccionarProducto(SelectEvent evt) {
        tab_deta_factura.modificar(evt);
        if (tab_deta_factura.getValor("ide_inarti") != null) {
            tab_deta_factura.setValor("precio_ccdfa", String.valueOf(ser_producto.getUltimoPrecioProductoCliente(tab_cab_factura.getValor("ide_geper"), tab_deta_factura.getValor("ide_inarti"))));
            //Carga la configuracion de iva del producto seleccionado
            tab_deta_factura.setValor("iva_inarti_ccdfa", String.valueOf(ser_producto.getIvaProducto(tab_deta_factura.getValor("ide_inarti"))));
            //Carga configuracion del producto que compra el cliente 
            String str_aux_iva = ser_producto.getUltimoIvaProductoCliente(tab_cab_factura.getValor("ide_geper"), tab_deta_factura.getValor("ide_inarti"));
            if (str_aux_iva != null && str_aux_iva.isEmpty() == false) {
                //Carga el valor del iva del producto para el cliente seleccionado
                tab_deta_factura.setValor("iva_inarti_ccdfa", str_aux_iva);
            }
        } else {
            tab_deta_factura.setValor("precio_ccdfa", "0,00");
        }
        utilitario.addUpdateTabla(tab_deta_factura, "precio_ccdfa,iva_inarti_ccdfa", "");
        calcularTotalDetalleFactura();

    }

    public void cambioPrecioCantidadIva(AjaxBehaviorEvent evt) {
        tab_deta_factura.modificar(evt);
        calcularTotalDetalleFactura();
    }

    /**
     * Calcula el valor total de cada detalle de la factura
     */
    private void calcularTotalDetalleFactura() {
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        try {
            cantidad = Double.parseDouble(tab_deta_factura.getValor("cantidad_ccdfa"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_deta_factura.getValor("precio_ccdfa"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_deta_factura.setValor("total_ccdfa", utilitario.getFormatoNumero(total));
        utilitario.addUpdateTabla(tab_deta_factura, "total_ccdfa", "");
        calcularTotalFactura();
    }

    private void calcularTotalFactura() {
        double base_grabada = 0;
        double base_no_objeto = 0;
        double base_tarifa0 = 0;
        double valor_iva = 0;

        for (int i = 0; i < tab_deta_factura.getTotalFilas(); i++) {
            String iva = tab_deta_factura.getValor(i, "iva_inarti_ccdfa");
            if (iva.equals("1")) { //SI IVA
                base_grabada = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_grabada;
                valor_iva = base_grabada * 0.12; //******** PONER VARIABLE *****///
            } else if (iva.equals("-1")) { // NO IVA
                base_tarifa0 = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_tarifa0;
            } else if (iva.equals("0")) { // NO OBJETO
                base_no_objeto = Double.parseDouble(tab_deta_factura.getValor(i, "total_ccdfa")) + base_no_objeto;
            }
        }
        tab_cab_factura.setValor("base_grabada_cccfa", utilitario.getFormatoNumero(base_grabada));
        tab_cab_factura.setValor("base_no_objeto_iva_cccfa", utilitario.getFormatoNumero(base_no_objeto));
        tab_cab_factura.setValor("valor_iva_cccfa", utilitario.getFormatoNumero(valor_iva));
        tab_cab_factura.setValor("base_tarifa0_cccfa", utilitario.getFormatoNumero(base_tarifa0));
        tab_cab_factura.setValor("total_cccfa", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));

        tex_subtotal12.setValue(utilitario.getFormatoNumero(base_grabada));
        tex_subtotal0.setValue(utilitario.getFormatoNumero(base_no_objeto + base_tarifa0));
        tex_iva.setValue(utilitario.getFormatoNumero(valor_iva));
        tex_total.setValue(utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));

        utilitario.addUpdate("tab_factura:0:gri_valores");
    }

    public void insertar() {
        if (tab_deta_factura.isFocus()) {
            if (tab_cab_factura.getValor("ide_geper") != null) {
                tab_deta_factura.insertar();
            } else {
                utilitario.agregarMensajeInfo("Seleccione Cliente", "Debe seleccionar un cliente para realizar la factura");
            }
        } else if (tab_deta_conta.isFocus()) {
            //Valida que haya valores en la factura
            if (tab_deta_factura.isEmpty() == false) {
                tab_deta_conta.insertar();
            } else {
                utilitario.agregarMensajeInfo("Crear Factura", "Debe ingresar detalles a la Factura");
            }
        }
    }

    public void eliminar() {
        if (tab_deta_factura.isFocus()) {
            tab_deta_factura.eliminar();
        } else if (tab_deta_conta.isFocus()) {
            tab_deta_conta.eliminar();
        }
    }

    public void aceptar() {
        //Asigna punto de emision seleccionado y si solo guarda la factura
        tab_cab_factura.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
        tab_cab_factura.setValor("solo_guardar_cccfa", String.valueOf(sbb_factura.getValue()));
        tab_cab_factura.setValor("OBSERVACION_CCCFA", String.valueOf(ate_observacion.getValue()));

    }

    @Override
    public void cerrar() {
        //Activa las tab
        tab_factura.getTab(1).setDisabled(false);
        tab_factura.getTab(2).setDisabled(false);
        tab_factura.getTab(3).setDisabled(false);
        tab_factura.getTab(4).setDisabled(false);
        super.cerrar(); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getTab_cab_factura() {
        return tab_cab_factura;
    }

    public void setTab_cab_factura(Tabla tab_cab_factura) {
        this.tab_cab_factura = tab_cab_factura;
    }

    public Tabla getTab_deta_factura() {
        return tab_deta_factura;
    }

    public void setTab_deta_factura(Tabla tab_deta_factura) {
        this.tab_deta_factura = tab_deta_factura;
    }

    public boolean isLectura() {
        return lectura;
    }

    public void setLectura(boolean lectura) {
        this.lectura = lectura;
    }

    /**
     * Para cargar una factura que ya existe
     *
     * @param ide_cccfa Codigo de la Factura
     */
    public void setIdeFactura(String ide_cccfa) {
        this.ide_cccfa = ide_cccfa;
    }

    public boolean isActivarAsientoVenta() {
        return tab_factura.getTab(1).isDisabled();
    }

    public void setActivarAsientoVenta(boolean activarAsientoVenta) {
        tab_factura.getTab(1).setDisabled(!activarAsientoVenta);
    }

    public boolean isActivarAsientoCosto() {
        return tab_factura.getTab(2).isDisabled();
    }

    public void setActivarAsientoCosto(boolean activarAsientoCosto) {
        tab_factura.getTab(2).setDisabled(!activarAsientoCosto);
    }

    public boolean isActivarInventario() {
        return tab_factura.getTab(3).isDisabled();
    }

    public void setActivarInventario(boolean activarInventario) {
        tab_factura.getTab(3).setDisabled(!activarInventario);
    }

    public boolean isActivarRetencion() {
        return tab_factura.getTab(4).isDisabled();
    }

    public void setActivarRetencion(boolean activarRetencion) {
        tab_factura.getTab(4).setDisabled(!activarRetencion);
    }

    public Tabla getTab_cab_conta() {
        return tab_cab_conta;
    }

    public void setTab_cab_conta(Tabla tab_cab_conta) {
        this.tab_cab_conta = tab_cab_conta;
    }

    public Tabla getTab_deta_conta() {
        return tab_deta_conta;
    }

    public void setTab_deta_conta(Tabla tab_deta_conta) {
        this.tab_deta_conta = tab_deta_conta;
    }

}
