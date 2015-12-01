/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.List;
import javax.faces.event.AjaxBehaviorEvent;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class VistaRetencion extends Dialogo {

    private Tabla tab_cab_retencion_vretencion = new Tabla();
    private Tabla tab_det_retencion_vretencion = new Tabla();
    private Tabla tab_datos_proveedor = new Tabla();
    private String ruta = "pre_index.clase";
    private Grid gri_cuerpo_vretencion = new Grid();
    private Utilitario utilitario = new Utilitario();
    private boolean aux_tabla = false;
    private Grid gri_totales_vretencion = new Grid();
    private Etiqueta eti_total_vretencion = new Etiqueta();

    public VistaRetencion() {
        this.setTitle("COMPROBANTE DE RETENCION");
        this.setWidth("78%");
        this.setHeight("80%");

        List lis_plan = utilitario.getConexion().consultar("select ide_CNCPC from con_cab_plan_cuen where activo_cncpc=true");

        gri_totales_vretencion.setId("gri_totales_vretencion");
        gri_totales_vretencion.setColumns(3);
        eti_total_vretencion.setValue("TOTAL RETENIDO : 0");
        eti_total_vretencion.setStyle("font-size: 14px;font-weight: bold");
        gri_totales_vretencion.getChildren().add(eti_total_vretencion);
        tab_cab_retencion_vretencion.setId("tab_cab_retencion_vretencion");
        tab_cab_retencion_vretencion.setTabla("con_cabece_retenc", "ide_cncre", -1);
        tab_cab_retencion_vretencion.agregarRelacion(tab_det_retencion_vretencion);
        tab_cab_retencion_vretencion.getColumna("ide_cnccc").setVisible(false);
        tab_cab_retencion_vretencion.getColumna("ide_cnere").setVisible(false);
        tab_cab_retencion_vretencion.getColumna("ide_cnere").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_rete_normal"));
        tab_cab_retencion_vretencion.getColumna("es_venta_cncre").setValorDefecto("false");
        tab_cab_retencion_vretencion.getColumna("es_venta_cncre").setVisible(false);
        tab_cab_retencion_vretencion.getColumna("numero_cncre").setMascara("999-999-99999999");
        tab_cab_retencion_vretencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(false);
        tab_cab_retencion_vretencion.getColumna("autorizacion_cncre").setMascara("9999999999");
        tab_cab_retencion_vretencion.getColumna("autorizacion_cncre").setQuitarCaracteresEspeciales(true);
        tab_cab_retencion_vretencion.getColumna("numero_cncre").setQuitarCaracteresEspeciales(true);
        tab_cab_retencion_vretencion.setCondicion("ide_cncre=-1");
        tab_cab_retencion_vretencion.setTipoFormulario(true);
        tab_cab_retencion_vretencion.getGrid().setColumns(4);
        tab_cab_retencion_vretencion.setMostrarNumeroRegistros(false);
        utilitario.buscarNombresVisuales(tab_cab_retencion_vretencion);

        tab_det_retencion_vretencion.setId("tab_det_retencion_vretencion");
        tab_det_retencion_vretencion.setTabla("con_detall_retenc", "ide_cndre", -1);
        tab_det_retencion_vretencion.getColumna("ide_cncim").setCombo("con_cabece_impues", "ide_cncim", "nombre_cncim,casillero_cncim", "");
        tab_det_retencion_vretencion.getColumna("valor_cndre").setLectura(true);
        tab_det_retencion_vretencion.setCondicion("ide_cndre=-1");
        utilitario.buscarNombresVisuales(tab_det_retencion_vretencion);

        tab_datos_proveedor.setId("tab_datos_proveedor");
        tab_datos_proveedor.setSql("select ide_geper,nom_geper,ti.nombre_getid,direccion_geper,identificac_geper "
                + "from gen_persona gp,gen_tipo_identifi ti "
                + "where ti.ide_getid=gp.ide_getid and ide_geper=-1");
        tab_datos_proveedor.setCampoPrimaria("ide_geper");
        tab_datos_proveedor.getColumna("nombre_getid").setLectura(true);
        tab_datos_proveedor.getColumna("identificac_geper").setLectura(true);
        tab_datos_proveedor.getColumna("nom_geper").setLectura(true);
        tab_datos_proveedor.setNumeroTabla(-1);
        tab_datos_proveedor.ejecutarSql();
        utilitario.buscarNombresVisuales(tab_datos_proveedor);
        tab_datos_proveedor.setTipoFormulario(true);
        tab_datos_proveedor.getGrid().setColumns(4);
        tab_datos_proveedor.setMostrarNumeroRegistros(false);

        this.setDialogo(gri_cuerpo_vretencion);
        this.setDynamic(false);

    }

    public boolean validarComprobanteRetencion() {

        if (tab_cab_retencion_vretencion.getValor("numero_cncre") == null || tab_cab_retencion_vretencion.getValor("numero_cncre").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar el numero de retencion");
            return false;
        }

        if (tab_cab_retencion_vretencion.getValor("autorizacion_cncre") == null || tab_cab_retencion_vretencion.getValor("autorizacion_cncre").isEmpty()) {
            utilitario.agregarMensajeError("Atencion", "Debe ingresar el numero de autorizacion");
            return false;
        }
        try {
            if (Integer.parseInt(tab_cab_retencion_vretencion.getValor("numero_cncre").substring(6, tab_cab_retencion_vretencion.getValor("numero_cncre").length())) != 0) {
                List sql_num_com = utilitario.getConexion().consultar("select *from con_cabece_retenc where autorizacion_cncre='" + tab_cab_retencion_vretencion.getValor("autorizacion_cncre") + "' and numero_cncre='" + tab_cab_retencion_vretencion.getValor("numero_cncre") + "'");
                if (sql_num_com.size() > 0) {
                    utilitario.agregarMensajeError("Atencion", "El numero de comprobante de retencion ya existe");
                    return false;
                }
            }
        } catch (Exception e) {
            return true;
        }

        return true;
    }

    public void insertar() {
        tab_det_retencion_vretencion.insertar();

    }

    public void eliminar() {
        tab_det_retencion_vretencion.eliminar();

    }
    private String p_iva30 = utilitario.getVariable("p_con_impuesto_iva30");
    private String p_iva70 = utilitario.getVariable("p_con_impuesto_iva70");
    private String p_iva100 = utilitario.getVariable("p_con_impuesto_iva100");
    List porcen_iva_sql = utilitario.getConexion().consultar("select porcentaje_cnpim from con_porcen_impues where ide_cnpim=" + utilitario.getVariable("p_con_porcentaje_imp_iva"));
    private double p_porcentaje_iva = Double.parseDouble(porcen_iva_sql.get(0).toString());
    cls_retenciones retenciones = new cls_retenciones();

    public void setVistaRetencion(Tabla tab_cabecera_factura, Tabla tab_detalle_factura, boolean es_venta) {
        if (aux_tabla == false) {
            tab_cab_retencion_vretencion.setRuta(ruta + "." + this.getId());
            tab_det_retencion_vretencion.setRuta(ruta + "." + this.getId());
            tab_datos_proveedor.setRuta(ruta + "." + this.getId());
            tab_datos_proveedor.setSql("select ide_geper,nom_geper,ti.nombre_getid,direccion_geper,identificac_geper "
                    + "from gen_persona gp,gen_tipo_identifi ti "
                    + "where ti.ide_getid=gp.ide_getid and ide_geper=" + tab_cabecera_factura.getValor("ide_geper"));
            tab_datos_proveedor.ejecutarSql();
            tab_datos_proveedor.dibujar();
            tab_cab_retencion_vretencion.dibujar();

            tab_det_retencion_vretencion.getColumna("porcentaje_cndre").setMetodoChangeRuta(ruta + "." + this.getId() + ".calcularValorRetener");
            tab_det_retencion_vretencion.getColumna("base_cndre").setMetodoChangeRuta(ruta + "." + this.getId() + ".calcularValorRetener");

            tab_det_retencion_vretencion.dibujar();
            PanelTabla pat_panel8 = new PanelTabla();
            pat_panel8.setPanelTabla(tab_det_retencion_vretencion);
            pat_panel8.getMenuTabla().quitarItemGuardar();
            pat_panel8.getMenuTabla().quitarSubmenuOtros();
            gri_cuerpo_vretencion.getChildren().add(tab_cab_retencion_vretencion);
            gri_cuerpo_vretencion.getChildren().add(tab_datos_proveedor);
            gri_cuerpo_vretencion.getChildren().add(gri_totales_vretencion);
            gri_cuerpo_vretencion.getChildren().add(pat_panel8);
            aux_tabla = true;
        } else {
            tab_cab_retencion_vretencion.limpiar();
            tab_det_retencion_vretencion.limpiar();
            tab_datos_proveedor.setRuta(ruta + "." + this.getId());
            tab_datos_proveedor.setSql("select ide_geper,nom_geper,ti.nombre_getid,direccion_geper,identificac_geper "
                    + "from gen_persona gp,gen_tipo_identifi ti "
                    + "where ti.ide_getid=gp.ide_getid and ide_geper=" + tab_cabecera_factura.getValor("ide_geper"));
            tab_datos_proveedor.ejecutarSql();

        }
        retenciones.cargar_renta(tab_detalle_factura, es_venta);
        retenciones.calcula_ivas_para_retencion(tab_detalle_factura, es_venta);
        tab_cab_retencion_vretencion.insertar();
        if (es_venta) {
            tab_cab_retencion_vretencion.setValor("fecha_emisi_cncre", tab_cabecera_factura.getValor("fecha_emisi_cccfa"));
        } else {
            tab_cab_retencion_vretencion.setValor("fecha_emisi_cncre", tab_cabecera_factura.getValor("fecha_emisi_cpcfa"));
        }

        if (retenciones.getL_casillero().size() > 0) {
            for (int i = 0; i < retenciones.getL_casillero().size(); i++) {
                String porcen = retenciones.obtener_porcen(retenciones.getL_casillero().get(i).toString(), tab_cabecera_factura.getValor("ide_geper"), tab_cabecera_factura.getValor("ide_cntdo"));
                if (porcen != null) {
                    tab_det_retencion_vretencion.insertar();
                    tab_det_retencion_vretencion.setValor("ide_cncim", retenciones.getL_casillero().get(i).toString());
                    tab_det_retencion_vretencion.setValor("porcentaje_cndre", porcen);
                    tab_det_retencion_vretencion.setValor("base_cndre", utilitario.getFormatoNumero(retenciones.getL_valor_casillero().get(i)));
                    tab_det_retencion_vretencion.setValor("valor_cndre", utilitario.getFormatoNumero(((Double.parseDouble(retenciones.getL_valor_casillero().get(i).toString()) * Double.parseDouble(porcen)) / 100)));
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
        if (iva30 != 0) {
            String porcen = retenciones.obtener_porcen(p_iva30, tab_cabecera_factura.getValor("ide_geper"), tab_cabecera_factura.getValor("ide_cntdo"));
            if (porcen != null) {
                if (Double.parseDouble(porcen) != 0) {
                    tab_det_retencion_vretencion.insertar();
                    tab_det_retencion_vretencion.setValor("ide_cncim", p_iva30);
                    tab_det_retencion_vretencion.setValor("porcentaje_cndre", porcen);
                    tab_det_retencion_vretencion.setValor("base_cndre", utilitario.getFormatoNumero(iva30 * p_porcentaje_iva));
                    tab_det_retencion_vretencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva30 * p_porcentaje_iva)) / 100)));
                }
            }
        }
        if (iva70 != 0) {
            String porcen = retenciones.obtener_porcen(p_iva70, tab_cabecera_factura.getValor("ide_geper"), tab_cabecera_factura.getValor("ide_cntdo"));
            if (porcen != null) {
                if (Double.parseDouble(porcen) != 0) {
                    tab_det_retencion_vretencion.insertar();
                    tab_det_retencion_vretencion.setValor("ide_cncim", p_iva70);
                    tab_det_retencion_vretencion.setValor("porcentaje_cndre", porcen);
                    tab_det_retencion_vretencion.setValor("base_cndre", utilitario.getFormatoNumero(iva70 * p_porcentaje_iva));
                    tab_det_retencion_vretencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva70 * p_porcentaje_iva)) / 100)));
                }
            }
        }
        if (iva100 != 0) {
            String porcen = retenciones.obtener_porcen(p_iva100, tab_cabecera_factura.getValor("ide_geper"), tab_cabecera_factura.getValor("ide_cntdo"));
            System.out.println();
            if (porcen != null) {
                if (Double.parseDouble(porcen) != 0) {
                    tab_det_retencion_vretencion.insertar();
                    tab_det_retencion_vretencion.setValor("ide_cncim", p_iva100);
                    tab_det_retencion_vretencion.setValor("porcentaje_cndre", porcen);
                    tab_det_retencion_vretencion.setValor("base_cndre", utilitario.getFormatoNumero(iva100 * p_porcentaje_iva, 2));
                    tab_det_retencion_vretencion.setValor("valor_cndre", utilitario.getFormatoNumero((((Double.parseDouble(porcen)) * (iva100 * p_porcentaje_iva)) / 100), 2));
                }
            }
        }

        if (tab_det_retencion_vretencion.getTotalFilas() > 0) {
//            boo_hizo_retencion = true;
            String autorizacion = retenciones.getNumeroAutorizacion();
            String num_retencion = "";
            if (autorizacion != null) {
                num_retencion = retenciones.getNumeroRetencion(autorizacion, es_venta);
                if (num_retencion == null) {
                    num_retencion = "";
                }
            } else {
                autorizacion = "";
            }
            if (tab_det_retencion_vretencion.getSumaColumna("valor_cndre") == 0) {
                num_retencion = "00100100000000";
                autorizacion = "0000000000";
            }

            tab_cab_retencion_vretencion.setValor("numero_cncre", num_retencion);
            tab_cab_retencion_vretencion.setValor("autorizacion_cncre", autorizacion);
            Texto tex1 = new Texto();
            if (es_venta) {
                tab_cab_retencion_vretencion.setValor("es_venta_cncre", "true");
                tex1.setValue(" (N° de Comprobante de Venta: " + tab_cabecera_factura.getValor("secuencial_cccfa") + " )");
            } else {
                tab_cab_retencion_vretencion.setValor("es_venta_cncre", "false");
                tex1.setValue(" (N° de Comprobante de Compra: " + tab_cabecera_factura.getValor("numero_cpcfa") + " )");
            }
            this.setTitle("COMPROBANTE DE RETENCION" + " " + " " + tex1.getValue().toString());
            this.setStyle("width:" + (getAnchoPanel() - 5) + "px;height:" + getAltoPanel() + "px;overflow: auto;display: block;");
            calculaTotales();
        }else{
            tab_cab_retencion_vretencion.limpiar();

        }
    }

    public void calculaTotales() {
        double tot_ret = 0;
        System.out.println("total filas retencion "+tab_det_retencion_vretencion.getTotalFilas());
        for (int i = 0; i < tab_det_retencion_vretencion.getTotalFilas(); i++) {
            tot_ret = Double.parseDouble(tab_det_retencion_vretencion.getValor(i,"valor_cndre")) + tot_ret;
        }
        System.out.println("total retenido "+tot_ret);
        eti_total_vretencion.setValue("Total Retencion: " + utilitario.getFormatoNumero(tot_ret));
        utilitario.addUpdate("gri_totales_vretencion");

    }

    public void calcularValorRetener(AjaxBehaviorEvent evt) {
        tab_det_retencion_vretencion.modificar(evt);
        double dou_val_ret = 0;
        try {
            dou_val_ret = (Double.parseDouble(tab_det_retencion_vretencion.getValor("base_cndre")) * Double.parseDouble(tab_det_retencion_vretencion.getValor("porcentaje_cndre"))) / 100;
        } catch (Exception e) {
        }
        tab_det_retencion_vretencion.setValor("valor_cndre", utilitario.getFormatoNumero(dou_val_ret));
        utilitario.addUpdateTabla(tab_det_retencion_vretencion, "valor_cndre", "gri_totales_vretencion");
        calculaTotales();
    }

    @Override
    public void dibujar() {
        gri_totales_vretencion.setWidth((getAnchoPanel() - 15) + "");
        super.dibujar();
        
        
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Grid getGri_cuerpo_vretencion() {
        return gri_cuerpo_vretencion;
    }

    public void setGri_cuerpo_vretencion(Grid gri_cuerpo_vretencion) {
        this.gri_cuerpo_vretencion = gri_cuerpo_vretencion;
    }

    public Tabla getTab_cab_retencion_vretencion() {
        return tab_cab_retencion_vretencion;
    }

    public void setTab_cab_retencion_vretencion(Tabla tab_cab_retencion_vretencion) {
        this.tab_cab_retencion_vretencion = tab_cab_retencion_vretencion;
    }

    public Tabla getTab_det_retencion_vretencion() {
        return tab_det_retencion_vretencion;
    }

    public void setTab_det_retencion_vretencion(Tabla tab_det_retencion_vretencion) {
        this.tab_det_retencion_vretencion = tab_det_retencion_vretencion;
    }

    public Tabla getTab_datos_proveedor() {
        return tab_datos_proveedor;
    }

    public void setTab_datos_proveedor(Tabla tab_datos_proveedor) {
        this.tab_datos_proveedor = tab_datos_proveedor;
    }
}
