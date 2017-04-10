/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AreaTexto;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.separator.Separator;
import org.primefaces.event.SelectEvent;
import servicios.contabilidad.ServicioRetenciones;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author djacome
 */
public class Retencion extends Dialogo {

    private final Utilitario utilitario = new Utilitario();
    //RETENCION
    private Tabla tab_cb_retencion;
    private Tabla tab_dt_retencion;
    private Tabla tab_dto_proveedor;
    private String ide_cpcfa;
    private String ide_cccfa;
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);
    @EJB
    private final ServicioRetenciones ser_retencion = (ServicioRetenciones) utilitario.instanciarEJB(ServicioRetenciones.class);
    @EJB
    private final ServicioFacturaCxC ser_cuentas_cxc = (ServicioFacturaCxC) utilitario.instanciarEJB(ServicioFacturaCxC.class);

    private final AreaTexto ate_observacion = new AreaTexto();
    private final Texto tex_total = new Texto();
    double douBaseImponibleRentaTotal = 0;
    double douBaseImponibleIvaTotal = 0;

    public Retencion() {
        this.setWidth("95%");
        this.setHeight("90%");
        this.setTitle("COMPROBANTE DE RETENCIÓN");
        this.setResizable(false);
        this.setDynamic(false);
    }

    public void nuevaRetencionCompra(String ide_cpcfa) {
        this.ide_cpcfa = ide_cpcfa;
        this.setTitle("NUEVO COMPROBANTE DE RETENCIÓN");
        this.getGri_cuerpo().getChildren().clear();
        this.setDialogo(dibujarRetencionCompra());
    }

    public void nuevaRetencionVenta(String ide_cccfa) {
        this.ide_cccfa = ide_cccfa;
        this.setTitle("NUEVO COMPROBANTE DE RETENCIÓN EN VENTAS");
        this.getGri_cuerpo().getChildren().clear();
        this.setDialogo(dibujarRetencionVenta());
    }

    private Grupo dibujarRetencionVenta() {
        TablaGenerica tab_cab_documento = utilitario.consultar("SELECT * FROM cxc_cabece_factura WHERE ide_cccfa=" + ide_cccfa);
        // TablaGenerica tab_deta_documento = utilitario.consultar("select * from cxc_deta_factura WHERE ide_cccfa=" + ide_cccfa);
        douBaseImponibleRentaTotal = 0;
        douBaseImponibleIvaTotal = 0;
        double douBaseImponible = 0;
        double douBaseTarifa0 = 0;
        double douBaseNoObjeto = 0;
        try {
            douBaseImponible = Double.parseDouble(tab_cab_documento.getValor("base_grabada_cccfa"));
        } catch (Exception e) {
        }
        try {
            douBaseTarifa0 = Double.parseDouble(tab_cab_documento.getValor("base_tarifa0_cccfa"));
        } catch (Exception e) {
        }
        try {
            douBaseNoObjeto = Double.parseDouble(tab_cab_documento.getValor("base_no_objeto_iva_cccfa"));
        } catch (Exception e) {
        }
        try {
            douBaseImponibleIvaTotal = Double.parseDouble(tab_cab_documento.getValor("valor_iva_cccfa"));
        } catch (Exception e) {
        }
        douBaseImponibleRentaTotal = douBaseImponible + douBaseTarifa0 + douBaseNoObjeto;

        Grupo grupo = new Grupo();
        tab_dto_proveedor = new Tabla();
        tab_dto_proveedor.setRuta("pre_index.clase." + getId());
        tab_dto_proveedor.setId("tab_dto_proveedor");
        tab_dto_proveedor.setSql("select ide_geper,nom_geper,direccion_geper,ti.nombre_getid,identificac_geper "
                + "from gen_persona gp,gen_tipo_identifi ti "
                + "where ti.ide_getid=gp.ide_getid and ide_geper=" + tab_cab_documento.getValor("ide_geper"));
        tab_dto_proveedor.setNumeroTabla(999);
        tab_dto_proveedor.setCampoPrimaria("ide_geper");
        tab_dto_proveedor.getColumna("nombre_getid").setLectura(true);
        tab_dto_proveedor.getColumna("nombre_getid").setNombreVisual("TIPO DE IDENTIFICACIÓN");
        tab_dto_proveedor.getColumna("nombre_getid").setEtiqueta();
        tab_dto_proveedor.getColumna("nombre_getid").setOrden(3);
        tab_dto_proveedor.getColumna("identificac_geper").setEtiqueta();
        tab_dto_proveedor.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_dto_proveedor.getColumna("identificac_geper").setOrden(4);
        tab_dto_proveedor.getColumna("nom_geper").setOrden(1);
        tab_dto_proveedor.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
        tab_dto_proveedor.getColumna("nom_geper").setLectura(true);
        tab_dto_proveedor.getColumna("direccion_geper").setNombreVisual("DIRECCIÓN");
        tab_dto_proveedor.getColumna("direccion_geper").setOrden(2);
        tab_dto_proveedor.getColumna("ide_geper").setVisible(false);

        tab_dto_proveedor.setNumeroTabla(-1);
        tab_dto_proveedor.setTipoFormulario(true);
        tab_dto_proveedor.getGrid().setColumns(4);
        tab_dto_proveedor.setMostrarNumeroRegistros(false);
        tab_dto_proveedor.dibujar();

        tab_cb_retencion = new Tabla();
        tab_dt_retencion = new Tabla();
        tab_cb_retencion.setId("tab_cb_retencion");
        tab_cb_retencion.setRuta("pre_index.clase." + getId());
        tab_cb_retencion.setTabla("con_cabece_retenc", "ide_cncre", 999);
        tab_cb_retencion.setCondicion("ide_cncre=-1");
        tab_cb_retencion.getColumna("ide_cncre").setVisible(false);
        tab_cb_retencion.getColumna("ide_cnccc").setVisible(false);
        tab_cb_retencion.getColumna("ide_cnere").setVisible(false);
        tab_cb_retencion.getColumna("ide_cnere").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_rete_normal"));
        tab_cb_retencion.getColumna("es_venta_cncre").setValorDefecto("true");
        tab_cb_retencion.getColumna("es_venta_cncre").setVisible(false);
        tab_cb_retencion.getColumna("numero_cncre").setOrden(1);
        tab_cb_retencion.getColumna("numero_cncre").setNombreVisual("NÚMERO");
        tab_cb_retencion.getColumna("numero_cncre").setMascara("999-999-99999999");
        tab_cb_retencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(false);
        tab_cb_retencion.getColumna("numero_cncre").setEstilo("font-size: 12px;font-weight: bold");
        tab_cb_retencion.getColumna("autorizacion_cncre").setMascara("9999999999");
        tab_cb_retencion.getColumna("autorizacion_cncre").setOrden(2);
        tab_cb_retencion.getColumna("autorizacion_cncre").setNombreVisual("NUM. AUTORIZACIÓN");
        tab_cb_retencion.getColumna("autorizacion_cncre").setQuitarCaracteresEspeciales(true);
        tab_cb_retencion.getColumna("autorizacion_cncre").setEstilo("font-size: 12px;font-weight: bold");
        tab_cb_retencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(true);
        tab_cb_retencion.getColumna("OBSERVACION_CNCRE").setVisible(false);
        tab_cb_retencion.getColumna("FECHA_EMISI_CNCRE").setNombreVisual("FECHA EMISIÓN");
        tab_cb_retencion.setTipoFormulario(true);
        tab_cb_retencion.getGrid().setColumns(6);
        tab_cb_retencion.setMostrarNumeroRegistros(false);
        tab_cb_retencion.dibujar();
        tab_cb_retencion.insertar();

        tab_dt_retencion.setId("tab_dt_retencion");
        tab_dt_retencion.setRuta("pre_index.clase." + getId());
        tab_dt_retencion.setTabla("con_detall_retenc", "ide_cndre", 999);
        tab_dt_retencion.setCondicion("ide_cndre=-1");
        tab_dt_retencion.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
        tab_dt_retencion.getColumna("ide_cncim").setAutoCompletar();
        tab_dt_retencion.getColumna("ide_cncim").setNombreVisual("IMPUESTO");
        tab_dt_retencion.getColumna("ide_cncim").setMetodoChangeRuta("pre_index.clase." + this.getId() + ".cambioImpuesto");
        tab_dt_retencion.getColumna("valor_cndre").setEtiqueta();
        tab_dt_retencion.getColumna("valor_cndre").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_dt_retencion.getColumna("valor_cndre").setNombreVisual("VALOR");
        tab_dt_retencion.getColumna("valor_cndre").alinearDerecha();
        tab_dt_retencion.getColumna("valor_cndre").setEstilo("font-size: 15px;font-weight: bold;");
        tab_dt_retencion.getColumna("porcentaje_cndre").setNombreVisual("% RETENCIÓN");
        tab_dt_retencion.getColumna("porcentaje_cndre").setLectura(false);
        tab_dt_retencion.getColumna("base_cndre").setNombreVisual("BASE IMPONIBLE");
        tab_dt_retencion.getColumna("base_cndre").setLongitud(50);
        tab_dt_retencion.getColumna("ide_cndre").setVisible(false);
        tab_dt_retencion.getColumna("ide_cncre").setVisible(false);
        tab_dt_retencion.setRecuperarLectura(true);
        tab_dt_retencion.setScrollable(true);
        tab_dt_retencion.setScrollWidth(getAnchoPanel() - 15);
        tab_dt_retencion.setScrollHeight(getAltoPanel() - 275);
        tab_dt_retencion.setRows(100);
        tab_dt_retencion.getColumna("porcentaje_cndre").setMetodoChangeRuta("pre_index.clase." + this.getId() + ".calclularValorRetencion");
        tab_dt_retencion.getColumna("base_cndre").setMetodoChangeRuta("pre_index.clase." + this.getId() + ".calclularValorRetencion");

        tab_dt_retencion.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_dt_retencion);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");
        pat_panel.getMenuTabla().getItem_eliminar().setValueExpression("rendered", "true");
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);

        Grid gri_total = new Grid();
        gri_total.setWidth("100%");
        gri_total.setStyle("width:" + (getAnchoPanel() - 10) + "px;border:1px");
        gri_total.setColumns(2);
        Grid gri_observa = new Grid();
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong> <span style='color:red;font-weight: bold;'> *</span>"));
        ate_observacion.setCols(70);
        gri_observa.getChildren().add(ate_observacion);
        gri_total.getChildren().add(gri_observa);
        Grid gri_valores = new Grid();
        gri_valores.setId("gri_valoresrt");
        gri_valores.setColumns(2);
        gri_total.getChildren().add(gri_valores);
        gri_valores.getChildren().add(new Etiqueta("<strong> TOTAL :<s/trong>"));
        tex_total.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_total.setValue(utilitario.getFormatoNumero("0"));
        tex_total.setDisabled(true);
        gri_valores.getChildren().add(tex_total);

////        String p_iva30 = utilitario.getVariable("p_con_impuesto_iva30");
////        String p_iva70 = utilitario.getVariable("p_con_impuesto_iva70");
////        String p_iva100 = utilitario.getVariable("p_con_impuesto_iva100");
////        List porcen_iva_sql = utilitario.getConexion().consultar("select porcentaje_cnpim from con_porcen_impues where ide_cnpim=" + utilitario.getVariable("p_con_porcentaje_imp_iva"));
////        double p_porcentaje_iva = Double.parseDouble(porcen_iva_sql.get(0).toString());
//////        cls_retenciones retenciones = new cls_retenciones();
//////
//////        retenciones.cargar_renta(tab_deta_documento, false);
//////        retenciones.calcula_ivas_para_retencion(tab_deta_documento, false);
        tab_cb_retencion.setValor("fecha_emisi_cncre", tab_cab_documento.getValor("fecha_emisi_cccfa"));

        if (tab_cab_documento.getValor("observacion_cccfa") != null) {
            ate_observacion.setValue(tab_cab_documento.getValor("observacion_cccfa"));
        }

//////        if (retenciones.getL_casillero()
//////                .size() > 0) {
//////            for (int i = 0; i < retenciones.getL_casillero().size(); i++) {
//////                String porcen = retenciones.obtener_porcen(retenciones.getL_casillero().get(i).toString(), tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));
//////                if (porcen == null) {
//////                    porcen = ser_retencion.getValorDefectoImpuesto(retenciones.getL_casillero().get(i).toString());
//////                }
//////
//////                if (porcen != null) {
//////                    tab_dt_retencion.insertar();
//////                    tab_dt_retencion.setValor("ide_cncim", retenciones.getL_casillero().get(i).toString());
//////                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
//////                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(retenciones.getL_valor_casillero().get(i)));
//////                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero(((Double.parseDouble(retenciones.getL_valor_casillero().get(i).toString()) * Double.parseDouble(porcen)) / 100)));
//////
//////                }
//////            }
//////        }
//////        double iva30 = 0;
//////        double iva70 = 0;
//////        double iva100 = 0;
//////
//////        try {
//////            iva30 = Double.parseDouble(retenciones.getLis_total_iva_retenido().get(0) + "");
//////            iva70 = Double.parseDouble(retenciones.getLis_total_iva_retenido().get(1) + "");
//////            iva100 = Double.parseDouble(retenciones.getLis_total_iva_retenido().get(2) + "");
//////        } catch (Exception e) {
//////        }
//////        if (iva30
//////                != 0) {
//////            String porcen = retenciones.obtener_porcen(p_iva30, tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));
//////            if (porcen == null) {
//////                porcen = ser_retencion.getValorDefectoImpuesto(p_iva30);
//////            }
//////
//////            if (porcen != null) {
//////                if (Double.parseDouble(porcen) != 0) {
//////                    tab_dt_retencion.insertar();
//////                    tab_dt_retencion.setValor("ide_cncim", p_iva30);
//////                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
//////                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(iva30 * p_porcentaje_iva));
//////                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva30 * p_porcentaje_iva)) / 100)));
//////                }
//////            }
//////        }
//////        if (iva70 != 0) {
//////            String porcen = retenciones.obtener_porcen(p_iva70, tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));
//////
//////            if (porcen == null) {
//////                porcen = ser_retencion.getValorDefectoImpuesto(p_iva70);
//////            }
//////            if (porcen != null) {
//////                if (Double.parseDouble(porcen) != 0) {
//////                    tab_dt_retencion.insertar();
//////                    tab_dt_retencion.setValor("ide_cncim", p_iva70);
//////                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
//////                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(iva70 * p_porcentaje_iva));
//////                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva70 * p_porcentaje_iva)) / 100)));
//////                }
//////            }
//////        }
//////        if (iva100 != 0) {
//////            String porcen = retenciones.obtener_porcen(p_iva100, tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));
//////            if (porcen == null) {
//////                porcen = ser_retencion.getValorDefectoImpuesto(p_iva100);
//////            }
//////            if (porcen != null) {
//////                if (Double.parseDouble(porcen) != 0) {
//////                    tab_dt_retencion.insertar();
//////                    tab_dt_retencion.setValor("ide_cncim", p_iva100);
//////                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
//////                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(iva100 * p_porcentaje_iva, 2));
//////                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva100 * p_porcentaje_iva)) / 100), 2));
//////                }
//////            }
//////        }
//////
////////            boo_hizo_retencion = true;
//////        String autorizacion = retenciones.getNumeroAutorizacion();
//////        String num_retencion = "";
//////        if (autorizacion != null) {
//////            num_retencion = retenciones.getNumeroRetencion(autorizacion, false);
//////            if (num_retencion == null) {
//////                num_retencion = "";
//////            }
//////        } else {
//////            autorizacion = "";
//////        }
//        if (tab_dt_retencion.getSumaColumna("valor_cndre") == 0) {
//            num_retencion = "00100100000000";
//            autorizacion = "0000000000";
//        }
//////        tab_cb_retencion.setValor("numero_cncre", num_retencion);
//////        tab_cb_retencion.setValor("autorizacion_cncre", autorizacion);
//////        calculaTotales();
        grupo.getChildren().add(tab_cb_retencion);
        grupo.getChildren().add(tab_dto_proveedor);
        grupo.getChildren().add(new Separator());

        Grid gri_td = new Grid();
        gri_td.setWidth("60%");
        gri_td.setColumns(4);
        gri_td.getChildren().add(new Etiqueta("<strong>TIPO DE COMPROBANTE :</strong>"));
        gri_td.getChildren().add(new Etiqueta("<span style='font-size: 14px;font-weight: bold'>" + ser_cuentas_cxp.getNombreTipoDocumento(tab_cab_documento.getValor("ide_cntdo")) + "</span>"));
        gri_td.getChildren().add(new Etiqueta("<strong>NÚMERO DE COMPROBANTE :</strong>"));
        gri_td.getChildren().add(new Etiqueta("<span style='font-size: 14px;font-weight: bold'>" + tab_cab_documento.getValor("secuencial_cccfa") + "</span>"));
        grupo.getChildren().add(gri_td);
        grupo.getChildren().add(new Separator());
        grupo.getChildren().add(pat_panel);
        grupo.getChildren().add(gri_total);
        return grupo;
    }

    private Grupo dibujarRetencionCompra() {
        TablaGenerica tab_cab_documento = utilitario.consultar("SELECT * FROM cxp_cabece_factur WHERE ide_cpcfa=" + ide_cpcfa);
        TablaGenerica tab_deta_documento = utilitario.consultar("SELECT * FROM cxp_detall_factur WHERE ide_cpcfa=" + ide_cpcfa);
        douBaseImponibleRentaTotal = 0;
        douBaseImponibleIvaTotal = 0;
        double douBaseImponible = 0;
        double douBaseTarifa0 = 0;
        double douBaseNoObjeto = 0;
        try {
            douBaseImponible = Double.parseDouble(tab_cab_documento.getValor("base_grabada_cpcfa"));
        } catch (Exception e) {
        }
        try {
            douBaseTarifa0 = Double.parseDouble(tab_cab_documento.getValor("base_tarifa0_cpcfa"));
        } catch (Exception e) {
        }
        try {
            douBaseNoObjeto = Double.parseDouble(tab_cab_documento.getValor("base_no_objeto_iva_cpcfa"));
        } catch (Exception e) {
        }
        try {
            douBaseImponibleIvaTotal = Double.parseDouble(tab_cab_documento.getValor("valor_iva_cpcfa"));
        } catch (Exception e) {
        }
        douBaseImponibleRentaTotal = douBaseImponible + douBaseTarifa0 + douBaseNoObjeto;

        Grupo grupo = new Grupo();
        tab_dto_proveedor = new Tabla();
        tab_dto_proveedor.setRuta("pre_index.clase." + getId());
        tab_dto_proveedor.setId("tab_dto_proveedor");
        tab_dto_proveedor.setSql("select ide_geper,nom_geper,direccion_geper,ti.nombre_getid,identificac_geper "
                + "from gen_persona gp,gen_tipo_identifi ti "
                + "where ti.ide_getid=gp.ide_getid and ide_geper=" + tab_cab_documento.getValor("ide_geper"));
        tab_dto_proveedor.setNumeroTabla(999);
        tab_dto_proveedor.setCampoPrimaria("ide_geper");
        tab_dto_proveedor.getColumna("nombre_getid").setLectura(true);
        tab_dto_proveedor.getColumna("nombre_getid").setNombreVisual("TIPO DE IDENTIFICACIÓN");
        tab_dto_proveedor.getColumna("nombre_getid").setEtiqueta();
        tab_dto_proveedor.getColumna("nombre_getid").setOrden(3);
        tab_dto_proveedor.getColumna("identificac_geper").setEtiqueta();
        tab_dto_proveedor.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_dto_proveedor.getColumna("identificac_geper").setOrden(4);
        tab_dto_proveedor.getColumna("nom_geper").setOrden(1);
        tab_dto_proveedor.getColumna("nom_geper").setNombreVisual("PROVEEDOR");
        tab_dto_proveedor.getColumna("nom_geper").setLectura(true);
        tab_dto_proveedor.getColumna("direccion_geper").setNombreVisual("DIRECCIÓN");
        tab_dto_proveedor.getColumna("direccion_geper").setOrden(2);
        tab_dto_proveedor.getColumna("ide_geper").setVisible(false);

        tab_dto_proveedor.setNumeroTabla(-1);
        tab_dto_proveedor.setTipoFormulario(true);
        tab_dto_proveedor.getGrid().setColumns(4);
        tab_dto_proveedor.setMostrarNumeroRegistros(false);
        tab_dto_proveedor.dibujar();

        tab_cb_retencion = new Tabla();
        tab_dt_retencion = new Tabla();
        tab_cb_retencion.setId("tab_cb_retencion");
        tab_cb_retencion.setRuta("pre_index.clase." + getId());
        tab_cb_retencion.setTabla("con_cabece_retenc", "ide_cncre", 999);
        tab_cb_retencion.setCondicion("ide_cncre=-1");
        tab_cb_retencion.getColumna("ide_cncre").setVisible(false);
        tab_cb_retencion.getColumna("ide_cnccc").setVisible(false);
        tab_cb_retencion.getColumna("ide_cnere").setVisible(false);
        tab_cb_retencion.getColumna("ide_cnere").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_rete_normal"));
        tab_cb_retencion.getColumna("es_venta_cncre").setValorDefecto("false");
        tab_cb_retencion.getColumna("es_venta_cncre").setVisible(false);
        tab_cb_retencion.getColumna("numero_cncre").setOrden(1);
        tab_cb_retencion.getColumna("numero_cncre").setNombreVisual("NÚMERO");
        tab_cb_retencion.getColumna("numero_cncre").setMascara("999-999-99999999");
        tab_cb_retencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(false);
        tab_cb_retencion.getColumna("numero_cncre").setEstilo("font-size: 12px;font-weight: bold");
        tab_cb_retencion.getColumna("autorizacion_cncre").setMascara("9999999999");
        tab_cb_retencion.getColumna("autorizacion_cncre").setOrden(2);
        tab_cb_retencion.getColumna("autorizacion_cncre").setNombreVisual("NUM. AUTORIZACIÓN");
        tab_cb_retencion.getColumna("autorizacion_cncre").setQuitarCaracteresEspeciales(true);
        tab_cb_retencion.getColumna("autorizacion_cncre").setEstilo("font-size: 12px;font-weight: bold");
        tab_cb_retencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(true);
        tab_cb_retencion.getColumna("OBSERVACION_CNCRE").setVisible(false);
        tab_cb_retencion.getColumna("FECHA_EMISI_CNCRE").setNombreVisual("FECHA EMISIÓN");
        tab_cb_retencion.setTipoFormulario(true);
        tab_cb_retencion.getGrid().setColumns(6);
        tab_cb_retencion.setMostrarNumeroRegistros(false);
        tab_cb_retencion.dibujar();
        tab_cb_retencion.insertar();

        tab_dt_retencion.setId("tab_dt_retencion");
        tab_dt_retencion.setRuta("pre_index.clase." + getId());
        tab_dt_retencion.setTabla("con_detall_retenc", "ide_cndre", 999);
        tab_dt_retencion.setCondicion("ide_cndre=-1");
        tab_dt_retencion.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
        tab_dt_retencion.getColumna("ide_cncim").setAutoCompletar();
        tab_dt_retencion.getColumna("ide_cncim").setNombreVisual("IMPUESTO");
        tab_dt_retencion.getColumna("ide_cncim").setMetodoChangeRuta("pre_index.clase." + this.getId() + ".cambioImpuesto");
        tab_dt_retencion.getColumna("valor_cndre").setEtiqueta();
        tab_dt_retencion.getColumna("valor_cndre").setValorDefecto(utilitario.getFormatoNumero("0"));
        tab_dt_retencion.getColumna("valor_cndre").setNombreVisual("VALOR");
        tab_dt_retencion.getColumna("valor_cndre").alinearDerecha();
        tab_dt_retencion.getColumna("valor_cndre").setEstilo("font-size: 15px;font-weight: bold;");
        tab_dt_retencion.getColumna("porcentaje_cndre").setNombreVisual("% RETENCIÓN");
        tab_dt_retencion.getColumna("porcentaje_cndre").setLectura(false);
        tab_dt_retencion.getColumna("base_cndre").setNombreVisual("BASE IMPONIBLE");
        tab_dt_retencion.getColumna("base_cndre").setLongitud(50);
        tab_dt_retencion.getColumna("ide_cndre").setVisible(false);
        tab_dt_retencion.getColumna("ide_cncre").setVisible(false);
        tab_dt_retencion.setRecuperarLectura(true);
        tab_dt_retencion.setScrollable(true);
        tab_dt_retencion.setScrollWidth(getAnchoPanel() - 15);
        tab_dt_retencion.setScrollHeight(getAltoPanel() - 275);
        tab_dt_retencion.setRows(100);
        tab_dt_retencion.getColumna("porcentaje_cndre").setMetodoChangeRuta("pre_index.clase." + this.getId() + ".calclularValorRetencion");
        tab_dt_retencion.getColumna("base_cndre").setMetodoChangeRuta("pre_index.clase." + this.getId() + ".calclularValorRetencion");

        tab_dt_retencion.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_dt_retencion);
        pat_panel.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel.getMenuTabla().getItem_guardar().setRendered(false);
        pat_panel.getMenuTabla().getItem_formato().setRendered(false);
        pat_panel.getMenuTabla().getItem_insertar().setMetodoRuta("pre_index.clase." + getId() + ".insertar");
        pat_panel.getMenuTabla().getItem_eliminar().setMetodoRuta("pre_index.clase." + getId() + ".eliminar");
        pat_panel.getMenuTabla().getItem_eliminar().setValueExpression("rendered", "true");
        pat_panel.getMenuTabla().getItem_eliminar().setRendered(true);

        Grid gri_total = new Grid();
        gri_total.setWidth("100%");
        gri_total.setStyle("width:" + (getAnchoPanel() - 10) + "px;border:1px");
        gri_total.setColumns(2);
        Grid gri_observa = new Grid();
        gri_observa.getChildren().add(new Etiqueta("<strong>OBSERVACIÓN:</strong> <span style='color:red;font-weight: bold;'> *</span>"));
        ate_observacion.setCols(70);
        gri_observa.getChildren().add(ate_observacion);
        gri_total.getChildren().add(gri_observa);
        Grid gri_valores = new Grid();
        gri_valores.setId("gri_valoresrt");
        gri_valores.setColumns(2);
        gri_total.getChildren().add(gri_valores);
        gri_valores.getChildren().add(new Etiqueta("<strong> TOTAL :<s/trong>"));
        tex_total.setStyle("font-size: 14px;text-align: right;width:110px");
        tex_total.setValue(utilitario.getFormatoNumero("0"));
        tex_total.setDisabled(true);
        gri_valores.getChildren().add(tex_total);

        String p_iva30 = utilitario.getVariable("p_con_impuesto_iva30");
        String p_iva70 = utilitario.getVariable("p_con_impuesto_iva70");
        String p_iva100 = utilitario.getVariable("p_con_impuesto_iva100");
        List porcen_iva_sql = utilitario.getConexion().consultar("select porcentaje_cnpim from con_porcen_impues where ide_cnpim=" + utilitario.getVariable("p_con_porcentaje_imp_iva"));
        double p_porcentaje_iva = Double.parseDouble(porcen_iva_sql.get(0).toString());

        cls_retenciones retenciones = new cls_retenciones();

        retenciones.cargar_renta(tab_deta_documento, false);
        retenciones.calcula_ivas_para_retencion(tab_deta_documento, false);

        tab_cb_retencion.setValor("fecha_emisi_cncre", tab_cab_documento.getValor("fecha_emisi_cpcfa"));

        if (tab_cab_documento.getValor("observacion_cpcfa") != null) {
            ate_observacion.setValue(tab_cab_documento.getValor("observacion_cpcfa"));
        }

        if (retenciones.getL_casillero()
                .size() > 0) {
            for (int i = 0; i < retenciones.getL_casillero().size(); i++) {
                String porcen = retenciones.obtener_porcen(retenciones.getL_casillero().get(i).toString(), tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));
                if (porcen == null) {
                    porcen = ser_retencion.getValorDefectoImpuesto(retenciones.getL_casillero().get(i).toString());
                }

                if (porcen != null) {
                    tab_dt_retencion.insertar();
                    tab_dt_retencion.setValor("ide_cncim", retenciones.getL_casillero().get(i).toString());
                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(retenciones.getL_valor_casillero().get(i)));
                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero(((Double.parseDouble(retenciones.getL_valor_casillero().get(i).toString()) * Double.parseDouble(porcen)) / 100)));

                }
            }
        }
        double iva30 = 0;
        double iva70 = 0;
        double iva100 = 0;

        try {
            iva30 = Double.parseDouble(retenciones.getLis_total_iva_retenido().get(0) + "");
            iva70 = Double.parseDouble(retenciones.getLis_total_iva_retenido().get(1) + "");
            iva100 = Double.parseDouble(retenciones.getLis_total_iva_retenido().get(2) + "");
        } catch (Exception e) {
        }
        if (iva30
                != 0) {
            String porcen = retenciones.obtener_porcen(p_iva30, tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));
            if (porcen == null) {
                porcen = ser_retencion.getValorDefectoImpuesto(p_iva30);
            }

            if (porcen != null) {
                if (Double.parseDouble(porcen) != 0) {
                    tab_dt_retencion.insertar();
                    tab_dt_retencion.setValor("ide_cncim", p_iva30);
                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(iva30 * p_porcentaje_iva));
                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva30 * p_porcentaje_iva)) / 100)));
                }
            }
        }
        if (iva70 != 0) {
            String porcen = retenciones.obtener_porcen(p_iva70, tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));

            if (porcen == null) {
                porcen = ser_retencion.getValorDefectoImpuesto(p_iva70);
            }
            if (porcen != null) {
                if (Double.parseDouble(porcen) != 0) {
                    tab_dt_retencion.insertar();
                    tab_dt_retencion.setValor("ide_cncim", p_iva70);
                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(iva70 * p_porcentaje_iva));
                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva70 * p_porcentaje_iva)) / 100)));
                }
            }
        }
        if (iva100 != 0) {
            String porcen = retenciones.obtener_porcen(p_iva100, tab_cab_documento.getValor("ide_geper"), tab_cab_documento.getValor("ide_cntdo"));
            if (porcen == null) {
                porcen = ser_retencion.getValorDefectoImpuesto(p_iva100);
            }
            if (porcen != null) {
                if (Double.parseDouble(porcen) != 0) {
                    tab_dt_retencion.insertar();
                    tab_dt_retencion.setValor("ide_cncim", p_iva100);
                    tab_dt_retencion.setValor("porcentaje_cndre", porcen);
                    tab_dt_retencion.setValor("base_cndre", utilitario.getFormatoNumero(iva100 * p_porcentaje_iva, 2));
                    tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva100 * p_porcentaje_iva)) / 100), 2));
                }
            }
        }

//            boo_hizo_retencion = true;
        String autorizacion = retenciones.getNumeroAutorizacion();
        String num_retencion = "";
        if (autorizacion != null) {
            num_retencion = retenciones.getNumeroRetencion(autorizacion, false);
            if (num_retencion == null) {
                num_retencion = "";
            }
        } else {
            autorizacion = "";
        }
//        if (tab_dt_retencion.getSumaColumna("valor_cndre") == 0) {
//            num_retencion = "00100100000000";
//            autorizacion = "0000000000";
//        }

        tab_cb_retencion.setValor("numero_cncre", num_retencion);
        tab_cb_retencion.setValor("autorizacion_cncre", autorizacion);
        calculaTotales();

        grupo.getChildren().add(tab_cb_retencion);
        grupo.getChildren().add(tab_dto_proveedor);
        grupo.getChildren().add(new Separator());

        Grid gri_td = new Grid();
        gri_td.setWidth("60%");
        gri_td.setColumns(4);
        gri_td.getChildren().add(new Etiqueta("<strong>TIPO DE COMPROBANTE :</strong>"));
        gri_td.getChildren().add(new Etiqueta("<span style='font-size: 14px;font-weight: bold'>" + ser_cuentas_cxp.getNombreTipoDocumento(tab_cab_documento.getValor("ide_cntdo")) + "</span>"));
        gri_td.getChildren().add(new Etiqueta("<strong>NÚMERO DE COMPROBANTE :</strong>"));
        gri_td.getChildren().add(new Etiqueta("<span style='font-size: 14px;font-weight: bold'>" + tab_cab_documento.getValor("numero_cpcfa") + "</span>"));
        grupo.getChildren().add(gri_td);
        grupo.getChildren().add(new Separator());
        grupo.getChildren().add(pat_panel);
        grupo.getChildren().add(gri_total);
        return grupo;
    }

    public void insertar() {
        if (tab_dt_retencion.isFocus()) {
            tab_dt_retencion.insertar();
        }
    }

    public void eliminar() {
        if (tab_dt_retencion.isFocus()) {
            tab_dt_retencion.eliminar();
            calculaTotales();
        }
    }

    public void guardar() {
        //utilitario.getConexion().setImprimirSqlConsola(true);
        tab_cb_retencion.setValor("OBSERVACION_CNCRE", String.valueOf(ate_observacion.getValue()));
        if (validarComprobanteRetencion()) {
            tab_dto_proveedor.guardar();
            if (tab_cb_retencion.guardar()) {
                String ide_cncre = tab_cb_retencion.getValor("ide_cncre");
                for (int i = 0; i < tab_dt_retencion.getTotalFilas(); i++) {
                    tab_dt_retencion.setValor(i, "ide_cncre", ide_cncre);
                }
                if (tab_dt_retencion.guardar()) {
                    //Generar transaccion retencion en cxp
                    double dou_retencion = 0;
                    try {
                        dou_retencion = Double.parseDouble(String.valueOf(tex_total.getValue()));
                    } catch (Exception e) {
                    }

                    if (ide_cpcfa != null) {
                        if (dou_retencion > 0) {
                            ser_cuentas_cxp.generarTransaccionRetencion(ide_cpcfa, dou_retencion);
                        }
                        //Actualiza la retencion generada a la factura cxp
                        utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_cabece_factur SET ide_cncre=" + ide_cncre + " WHERE ide_cpcfa=" + ide_cpcfa);
                    } else if (ide_cccfa != null) {
                        if (dou_retencion > 0) {
                            ser_cuentas_cxc.generarTransaccionRetencion(ide_cccfa, dou_retencion);
                        }
                        //Actualiza la retencion generada a la factura cxc
                        utilitario.getConexion().agregarSqlPantalla("UPDATE cxc_cabece_factura SET ide_cncre=" + ide_cncre + " WHERE ide_cccfa=" + ide_cccfa);
                    }
                    if (utilitario.getConexion().guardarPantalla().isEmpty()) {
                        this.cerrar();
                    }
                }
            }
        }
    }

    /**
     * Validaciones del comprobante de Retención
     *
     * @return
     */
    public boolean validarComprobanteRetencion() {

        if (tab_cb_retencion.getValor("numero_cncre") == null || tab_cb_retencion.getValor("numero_cncre").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Comprobante", "Debe ingresar el número de retención");
            return false;
        }

        if (tab_cb_retencion.getValor("autorizacion_cncre") == null || tab_cb_retencion.getValor("autorizacion_cncre").isEmpty()) {
            utilitario.agregarMensajeError("Error al guardar el Comprobante", "Debe ingresar el número de autorización");
            return false;
        }

        try {
            if (Integer.parseInt(tab_cb_retencion.getValor("numero_cncre").substring(6, tab_cb_retencion.getValor("numero_cncre").length())) != 0) {
                List sql_num_com = utilitario.getConexion().consultar("select 1 from con_cabece_retenc where autorizacion_cncre='" + tab_cb_retencion.getValor("autorizacion_cncre") + "' and numero_cncre='" + tab_cb_retencion.getValor("numero_cncre") + "'");
                if (sql_num_com.size() > 0) {
                    utilitario.agregarMensajeError("Error al guardar el Comprobante", "Debe ingresar número de retención ya existe");
                    return false;
                }
            }
        } catch (Exception e) {
            utilitario.agregarMensajeError("Error al guardar el Comprobante", "El número de retención no es válido");
            return false;
        }
        double douSumaBaseRenta = 0;
        double douSumaBaseIva = 0;
        if (tab_dt_retencion.getTotalFilas() == 0) {
            utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe ingresar Detalles al comprobante de retención");
            return false;
        } else {
            for (int i = 0; i < tab_dt_retencion.getTotalFilas(); i++) {
                if (tab_dt_retencion.getValor(i, "ide_cncim") == null || tab_dt_retencion.getValor("ide_cncim").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe seleccionar un Impuesto en los Detalles de la retención");
                    return false;
                }
                if (tab_dt_retencion.getValor(i, "porcentaje_cndre") == null || tab_dt_retencion.getValor("porcentaje_cndre").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe ingresar el porcentaje de retención en los Detalles de la retención");
                    return false;
                }
                if (tab_dt_retencion.getValor(i, "base_cndre") == null || tab_dt_retencion.getValor("base_cndre").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede guardar el Comprobante", "Debe ingresar la Base Imponible en los Detalles de la retención ");
                    return false;
                }
                if (ser_retencion.isImpuestoRenta(tab_dt_retencion.getValor(i, "ide_cncim"))) {
                    douSumaBaseRenta += Double.parseDouble(tab_dt_retencion.getValor(i, "base_cndre"));
                } else {
                    douSumaBaseIva += Double.parseDouble(tab_dt_retencion.getValor(i, "base_cndre"));
                }
            }
        }
        if (ide_cpcfa != null) {
            if (!utilitario.getFormatoNumero(douSumaBaseRenta).equals(utilitario.getFormatoNumero(douBaseImponibleRentaTotal))) {
                utilitario.agregarMensajeError("La suma de la base imponible de impuesto a la RENTA debe ser igual a " + utilitario.getFormatoNumero(douBaseImponibleRentaTotal), "");
                return false;
            }
        }

        if (douSumaBaseIva > 0) {
            if (!utilitario.getFormatoNumero(douSumaBaseIva).equals(utilitario.getFormatoNumero(douBaseImponibleIvaTotal))) {
                utilitario.agregarMensajeError("La suma de la base imponible de impuesto IVA debe ser igual a " + utilitario.getFormatoNumero(douBaseImponibleIvaTotal), "");
                return false;
            }
        }
        return true;
    }

    public void cambioImpuesto(SelectEvent evt) {
        tab_dt_retencion.modificar(evt);
        String porcen = "";
        if (tab_dt_retencion.getValor("ide_cncim") != null) {
            porcen = ser_retencion.getValorDefectoImpuesto(tab_dt_retencion.getValor("ide_cncim"));
        }
        tab_dt_retencion.setValor("porcentaje_cndre", porcen);
        //utilitario.addUpdateTabla(tab_dt_retencion, "porcentaje_cndre", "");
        calclularValorRetencion(evt);

    }

    /**
     * Multiplica la base imponible por el porcentaje de retención
     *
     * @param evt
     */
    public void calclularValorRetencion(AjaxBehaviorEvent evt) {
        tab_dt_retencion.modificar(evt);

        double dou_val_ret = 0;
        try {
            dou_val_ret = (Double.parseDouble(tab_dt_retencion.getValor("base_cndre")) * Double.parseDouble(tab_dt_retencion.getValor("porcentaje_cndre"))) / 100;
        } catch (Exception e) {
        }
        tab_dt_retencion.setValor("valor_cndre", utilitario.getFormatoNumero(dou_val_ret));
        utilitario.addUpdateTabla(tab_dt_retencion, "VALOR_CNDRE,porcentaje_cndre", "");
        calculaTotales();
    }

    public void calculaTotales() {
        double tot_ret = 0;
        for (int i = 0; i < tab_dt_retencion.getTotalFilas(); i++) {
            try {
                tot_ret += Double.parseDouble(tab_dt_retencion.getValor(i, "valor_cndre"));
            } catch (Exception e) {
            }
        }
        tex_total.setValue(utilitario.getFormatoNumero(tot_ret));
        utilitario.addUpdate("gri_valoresrt");
    }

    public Tabla getTab_cb_retencion() {
        return tab_cb_retencion;
    }

    public void setTab_cb_retencion(Tabla tab_cb_retencion) {
        this.tab_cb_retencion = tab_cb_retencion;
    }

    public Tabla getTab_dt_retencion() {
        return tab_dt_retencion;
    }

    public void setTab_dt_retencion(Tabla tab_dt_retencion) {
        this.tab_dt_retencion = tab_dt_retencion;
    }

    public Tabla getTab_dto_proveedor() {
        return tab_dto_proveedor;
    }

    public void setTab_dto_proveedor(Tabla tab_dto_proveedor) {
        this.tab_dto_proveedor = tab_dto_proveedor;
    }

    public String getIde_cpcfa() {
        return ide_cpcfa;
    }

    public String getIde_cccfa() {
        return ide_cccfa;
    }

    public void setIde_cccfa(String ide_cccfa) {
        this.ide_cccfa = ide_cccfa;
    }

}
