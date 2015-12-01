/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author byron
 */
public class cls_retenciones {

    private Tabla tab_cabecera_retencion = new Tabla();
    private Tabla tab_detalle_retencion = new Tabla();
    private Utilitario utilitario = new Utilitario();

    public void cls_retenciones() {

        tab_cabecera_retencion.setId("tab_cabecera_retencion");
        tab_cabecera_retencion.setTabla("con_cabece_retenc", "ide_cncre", -1);
        tab_cabecera_retencion.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_cabecera_retencion.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_cabecera_retencion.getColumna("ide_cnere").setValorDefecto("0");// estado normal
        tab_cabecera_retencion.setCondicion("ide_cncre=-1");
        tab_cabecera_retencion.ejecutarSql();

        tab_detalle_retencion.setId("tab_detalle_retencion");
        tab_detalle_retencion.setTabla("con_detall_retenc", "ide_cndre", -1);
        tab_detalle_retencion.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_detalle_retencion.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_detalle_retencion.setCondicion("ide_cndre=-1");
        tab_detalle_retencion.ejecutarSql();

    }

    public void anularComprobanteRetencion(String ide_cncre){
        TablaGenerica tab_cab_retencion=utilitario.consultar("select * from con_cabece_retenc where ide_cncre="+ide_cncre);
        if (tab_cab_retencion.getTotalFilas()>0){
            utilitario.getConexion().agregarSqlPantalla("UPDATE con_cabece_retenc set ide_cnere="+utilitario.getVariable("p_con_estado_comprobante_rete_anulado") +" where ide_cncre="+ide_cncre);
        }
    }
    public void insertarCabeceraRetencion(Tabla tab_cab_retencion) {
        tab_cabecera_retencion.insertar();
        tab_cabecera_retencion.setValor("ide_cncre", tab_cab_retencion.getValor("ide_cncre"));
        tab_cabecera_retencion.setValor("ide_cnccc", tab_cab_retencion.getValor("ide_cnccc"));
        tab_cabecera_retencion.setValor("fecha_emisi_cncre", tab_cab_retencion.getValor("fecha_emisi_cncre"));
        tab_cabecera_retencion.setValor("observacion_cncre", tab_cab_retencion.getValor("observacion_cncre"));
        tab_cabecera_retencion.setValor("numero_cncre", tab_cab_retencion.getValor("numero_cncre"));
        tab_cabecera_retencion.setValor("autorizacion_cncre", tab_cab_retencion.getValor("autorizacion_cncre"));
        tab_cabecera_retencion.setValor("es_venta_cncre", tab_cab_retencion.getValor("es_venta_cncre"));
    }

    public void insertarDetalleRetencion(Tabla tab_det_retencion) {
        tab_detalle_retencion.insertar();
        tab_detalle_retencion.setValor("ide_cndre", tab_det_retencion.getValor("ide_cndre"));
        tab_detalle_retencion.setValor("ide_cncre", tab_det_retencion.getValor("ide_cncre"));
        tab_detalle_retencion.setValor("ide_cnimp", tab_det_retencion.getValor("ide_cnimp"));
        tab_detalle_retencion.setValor("porcentaje_cndre", tab_det_retencion.getValor("porcentaje_cndre"));
        tab_detalle_retencion.setValor("base_cndre", tab_det_retencion.getValor("base_cndre"));
        tab_detalle_retencion.setValor("valor_cndre", tab_det_retencion.getValor("valor_cndre"));
    }
//    public void insertarCabeceraRetencion(String ide_cnccc, String fecha, String observacion, String numero, String autorizacion, boolean es_venta) {
//        tab_cabecera_retencion.insertar();
//        tab_cabecera_retencion.setValor("ide_cnccc", ide_cnccc);
//        tab_cabecera_retencion.setValor("fecha_emisi_cncre", fecha);
//        tab_cabecera_retencion.setValor("observacion_cncre", ide_cnccc);
//        tab_cabecera_retencion.setValor("numero_cncre", ide_cnccc);
//        tab_cabecera_retencion.setValor("autorizacion_cncre", ide_cnccc);
//        if (es_venta) {
//            tab_cabecera_retencion.setValor("es_venta_cncre", "true");
//        } else {
//            tab_cabecera_retencion.setValor("es_venta_cncre", "false");
//        }
//    }
//
//    public void insertarDetalleRetencion(String ide_cncre, String ide_cnimp, double porcentaje_cndre, double base_cndre, double valor_cndre) {
//        tab_detalle_retencion.insertar();
//        tab_detalle_retencion.setValor("ide_cncre", ide_cncre);
//        tab_detalle_retencion.setValor("ide_cnimp", ide_cnimp);
//        tab_detalle_retencion.setValor("porcentaje_cndre", porcentaje_cndre + "");
//        tab_detalle_retencion.setValor("base_cndre", base_cndre + "");
//        tab_detalle_retencion.setValor("valor_cndre", valor_cndre + "");
//    }
    private List l_casillero = new ArrayList();
    private List l_valor_casillero = new ArrayList();
    cls_contabilidad conta = new cls_contabilidad();

    public void cargar_renta(Tabla tab_detalle_factura, boolean es_venta) {
        String casillero;
        l_casillero.clear();
        l_valor_casillero.clear();
        double suma = 0;
        int band = 0;
        for (int i = 0; i < tab_detalle_factura.getTotalFilas(); i++) {
            if (es_venta) {
                casillero = conta.buscarParametroProducto("ide_cncim", "RETENCION RENTA POR COBRAR", tab_detalle_factura.getValor(i, "ide_inarti"));
            } else {
                casillero = conta.buscarParametroProducto("ide_cncim", "RETENCION RENTA POR PAGAR", tab_detalle_factura.getValor(i, "ide_inarti"));
            }
            if (casillero != null) {
                for (int k = 0; k < l_casillero.size(); k++) {
                    if (casillero.equals(l_casillero.get(k))) {
                        band = 1;
                    }
                }
                if (band == 0) {
                    l_casillero.add(casillero);
                }
            }
            band = 0;
        }

        for (int i = 0; i < l_casillero.size(); i++) {
            casillero = l_casillero.get(i).toString();
            for (int j = 0; j < tab_detalle_factura.getTotalFilas(); j++) {
                if (es_venta) {
                    if (casillero.equals(conta.buscarParametroProducto("ide_cncim", "RETENCION RENTA POR COBRAR", tab_detalle_factura.getValor(j, "ide_inarti")))) {
                        suma = Double.parseDouble(tab_detalle_factura.getValor(j, "total_ccdfa")) + suma;
                    }
                } else {
                    if (casillero.equals(conta.buscarParametroProducto("ide_cncim", "RETENCION RENTA POR PAGAR", tab_detalle_factura.getValor(j, "ide_inarti")))) {
                        suma = Double.parseDouble(tab_detalle_factura.getValor(j, "valor_cpdfa")) + suma;
                    }
                }
            }
            l_valor_casillero.add(suma);
            suma = 0;
        }
    }
    private double iva30;
    private double iva70;
    private double iva100;
    private String p_bienes = utilitario.getVariable("p_inv_articulo_bien");
    private String p_servicios = utilitario.getVariable("p_inv_articulo_servicio");
    private String p_honorarios_profes = utilitario.getVariable("p_inv_articulo_honorarios");
    private String p_activos_fijos = utilitario.getVariable("p_inv_articulo_activo_fijo");
    private List lis_total_iva_retenido = new ArrayList();

    public void calcula_ivas_para_retencion(Tabla tab_detalle_factura, boolean es_venta) {
        lis_total_iva_retenido.clear();
        iva30 = 0;
        iva70 = 0;
        iva100 = 0;
        for (int i = 0; i < tab_detalle_factura.getTotalFilas(); i++) {
            if (tab_detalle_factura.getValor(i, "ide_inarti") != null) {
                String art = obtener_tipo_articulo(tab_detalle_factura.getValor(i, "ide_inarti"));
                if (es_venta) {
//                    List sql_iva = utilitario.getConexion().consultar("select iva_inarti from inv_articulo where ide_inarti=" + tab_detalle_factura.getValor(i, "ide_inarti"));
//                    int iva = Integer.parseInt(sql_iva.get(0).toString());
//                    if (iva == 1) {
//                        if (art.equals(p_bienes)) {
//                            iva30 = iva30 + Double.parseDouble(tab_detalle_factura.getValor(i, "total_ccdfa"));
//                        }
//                        if (art.equals(p_activos_fijos)) {
//                            iva30 = iva30 + Double.parseDouble(tab_detalle_factura.getValor(i, "total_ccdfa"));
//                        }
//                        if (art.equals(p_servicios)) {
//                            iva70 = iva70 + Double.parseDouble(tab_detalle_factura.getValor(i, "total_ccdfa"));
//                        }
//                        if (art.equals(p_honorarios_profes)) {
//                            iva100 = iva100 + Double.parseDouble(tab_detalle_factura.getValor(i, "total_ccdfa"));
//                        }
//                    }
                } else {
                    int iva = Integer.parseInt(tab_detalle_factura.getValor(i, "iva_inarti_cpdfa"));
                    if (iva == 1) {
                        if (art.equals(p_bienes)) {
                            iva30 = iva30 + Double.parseDouble(tab_detalle_factura.getValor(i, "valor_cpdfa"));
                        }
                        if (art.equals(p_activos_fijos)) {
                            iva30 = iva30 + Double.parseDouble(tab_detalle_factura.getValor(i, "valor_cpdfa"));
                        }
                        if (art.equals(p_servicios)) {
                            iva70 = iva70 + Double.parseDouble(tab_detalle_factura.getValor(i, "valor_cpdfa"));
                        }
                        if (art.equals(p_honorarios_profes)) {
                            iva100 = iva100 + Double.parseDouble(tab_detalle_factura.getValor(i, "valor_cpdfa"));
                        }
                    }
                }
            }
        }
        lis_total_iva_retenido.add(iva30);
        lis_total_iva_retenido.add(iva70);
        lis_total_iva_retenido.add(iva100);
    }

    public String obtener_tipo_articulo(String ide_arti) {
        // devuelve el tipo de articulo ya sea activo fijo,bien, o otro
        String ide_art = ide_arti;
        List inv_ide_arti = utilitario.getConexion().consultar("select inv_ide_inarti from inv_articulo where ide_inarti=" + ide_art);
        try {
            if (inv_ide_arti.get(0) != null) {
                do {
                    ide_art = inv_ide_arti.get(0).toString();
                    inv_ide_arti = utilitario.getConexion().consultar("select inv_ide_inarti from inv_articulo where ide_inarti=" + ide_art);
                } while (inv_ide_arti.get(0) != null);
            }

        } catch (Exception e) {
        }
        return ide_art;
    }

    public String obtener_porcen(String cabecera_impuesto, String proveedor, String tipo_documento) {
        List sql_porcentaje = utilitario.getConexion().consultar("SELECT porcentaje_cndim from con_detall_impues where ide_cnvim= "
                + "(select ide_cnvim from con_vigenc_impues where ide_cncim =" + cabecera_impuesto + " "
                + "and estado_cnvim is TRUE) "
                + "and ide_cntdo=" + tipo_documento + ""
                + "and ide_cntco=(select ide_cntco from gen_persona where ide_geper=" + proveedor + " "
                + ")");
        if (sql_porcentaje != null && !sql_porcentaje.isEmpty()) {
            System.out.println("porcentaje " + sql_porcentaje.get(0).toString());
            return sql_porcentaje.get(0).toString();
        } else {
            return null;
        }
    }

    public String getNumeroAutorizacion() {
        TablaGenerica tab_cabecera_retencion = utilitario.consultar("select * from con_cabece_retenc where ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and es_venta_cncre is FALSE "
                + "and autorizacion_cncre not like '0000000000' "
                + "order by ide_cncre desc");
        if (tab_cabecera_retencion.getTotalFilas() > 0) {
            if (tab_cabecera_retencion.getValor(0, "autorizacion_cncre") != null && !tab_cabecera_retencion.getValor(0, "autorizacion_cncre").isEmpty()) {
                return tab_cabecera_retencion.getValor(0, "autorizacion_cncre");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getNumeroRetencion(String autorizacion, boolean es_venta) {
        String str_sql = "select MAX(numero_cncre) as num_retencion from con_cabece_retenc where ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and autorizacion_cncre ='" + autorizacion + "' ";
        if (es_venta){
            str_sql+="and es_venta_cncre is true ";
        }else{
            str_sql+="and es_venta_cncre is FALSE ";
        }
        System.out.println("sql num retencion "+str_sql);
        List lis_cabecera_retencion = utilitario.getConexion().consultar(str_sql);
        if (lis_cabecera_retencion.size() > 0) {
            if (lis_cabecera_retencion.get(0) != null && !lis_cabecera_retencion.get(0).toString().isEmpty()) {
                String num_max = lis_cabecera_retencion.get(0) + "";
                String num_max_retencion = num_max.substring(0, 6);
                String aux_num = num_max.substring(6, num_max.length());
                try {
                    int num = Integer.parseInt(aux_num);
                    num = num + 1;
                    aux_num = num + "";
                    String ceros = utilitario.generarCero(8 - aux_num.length());
                    num_max_retencion = num_max_retencion.concat(ceros).concat(aux_num);
                    return num_max_retencion;
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public Tabla getTab_cabecera_retencion() {
        return tab_cabecera_retencion;
    }

    public void setTab_cabecera_retencion(Tabla tab_cabecera_retencion) {
        this.tab_cabecera_retencion = tab_cabecera_retencion;
    }

    public Tabla getTab_detalle_retencion() {
        return tab_detalle_retencion;
    }

    public void setTab_detalle_retencion(Tabla tab_detalle_retencion) {
        this.tab_detalle_retencion = tab_detalle_retencion;
    }

    public List getL_casillero() {
        return l_casillero;
    }

    public void setL_casillero(List l_casillero) {
        this.l_casillero = l_casillero;
    }

    public List getL_valor_casillero() {
        return l_valor_casillero;
    }

    public void setL_valor_casillero(List l_valor_casillero) {
        this.l_valor_casillero = l_valor_casillero;
    }

    public List getLis_total_iva_retenido() {
        return lis_total_iva_retenido;
    }

    public void setLis_total_iva_retenido(List lis_total_iva_retenido) {
        this.lis_total_iva_retenido = lis_total_iva_retenido;
    }
}
