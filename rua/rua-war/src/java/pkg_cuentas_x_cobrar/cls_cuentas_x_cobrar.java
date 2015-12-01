/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_cuentas_x_cobrar {

    private Utilitario utilitario = new Utilitario();
    private Tabla tab_cab_tran_cxc = new Tabla();
    private Tabla tab_det_tran_cxc = new Tabla();

    public cls_cuentas_x_cobrar() {
        tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
        tab_cab_tran_cxc.getColumna("ide_ccctr").setExterna(false);
        tab_cab_tran_cxc.setCondicion("ide_ccctr=-1");
        tab_cab_tran_cxc.ejecutarSql();
        tab_det_tran_cxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_det_tran_cxc.getColumna("ide_ccdtr").setExterna(false);
        tab_det_tran_cxc.setCondicion("ide_ccdtr=-1");
        tab_det_tran_cxc.ejecutarSql();
    }
    
    public String getSaldoPorCobrar(String ide_geper) {
        TablaGenerica tab_cuentas_x_cobrar = utilitario.consultar(getSqlCuentasPorCobrar(ide_geper));
        if (tab_cuentas_x_cobrar.getTotalFilas()>0) {
            return tab_cuentas_x_cobrar.getSumaColumna("saldo_x_pagar") + "";
        }
        return null;
    }

    public String getSqlCuentasPorCobrar(String ide_geper) {
        
        String str_sql_cxc = "select dt.ide_ccctr,"
                + "dt.ide_cccfa,"
                + "case when (cf.fecha_emisi_cccfa) is null then ct.fecha_trans_ccctr else cf.fecha_emisi_cccfa end,"
                + "cf.secuencial_cccfa,"
                + "cf.total_cccfa,"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cccfa) is NULL then ct.observacion_ccctr else cf.observacion_cccfa end "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa="+utilitario.getVariable("p_cxc_estado_factura_normal")+" "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "where ct.ide_geper="+ide_geper+" "
                + "and ct.ide_sucu="+utilitario.getVariable("ide_sucu") +" "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,cf.secuencial_cccfa, "
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";

        return str_sql_cxc;
    }


    public String buscarSecuencialFactura(String ide_ccdaf) {
        if (ide_ccdaf != null) {
            List secuencial_sql = utilitario.getConexion().consultar("select MAX(secuencial_cccfa) FROM cxc_cabece_factura where ide_ccdaf=" + ide_ccdaf);
            if (secuencial_sql.get(0) != null) {
                Long secuencial = Long.parseLong(secuencial_sql.get(0).toString());
                secuencial = secuencial + 1;
                String ceros = utilitario.generarCero(7 - secuencial.toString().length());
                String num_max = ceros.concat(secuencial.toString());
                return num_max;
            }
        }
        return null;
    }

    public void generarTransaccionVentaTransferenciaCasas(Tabla tab_cab_factura, double total_retenido, String num_doc) {
        String str_p_cxc_tipo_trans_factura = utilitario.getVariable("p_cxc_tipo_trans_factura");//Tipo transaccion Factura     
        tab_cab_tran_cxc.limpiar();
        tab_det_tran_cxc.limpiar();
        tab_cab_tran_cxc.insertar();
        if (tab_cab_factura != null) {
            tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_cab_tran_cxc.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxc.setValor("fecha_trans_ccctr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_cab_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            if (tab_cab_factura.getValor("observacion_cccfa") != null && !tab_cab_factura.getValor("observacion_cccfa").isEmpty()) {
                tab_cab_tran_cxc.setValor("observacion_ccctr", tab_cab_factura.getValor("observacion_cccfa"));
            }
            tab_cab_tran_cxc.guardar();
            tab_det_tran_cxc.insertar();
            // primer detalle cxc de tipo factura
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            double valor = Double.parseDouble(tab_cab_factura.getValor("total_cccfa")) - total_retenido;
            tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_factura.getValor("observacion_cccfa"));
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", 0 + "");
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cccfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_factura.getValor("secuencial_cccfa"));
            tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            // genero el pago en cxc otro detalle
            generarTransaccionPagoTransferenciaCasas(tab_cab_factura, tab_cab_tran_cxc.getValor("ide_ccctr"), num_doc);
        }
    }

    public void generarTransaccionPagoTransferenciaCasas(Tabla tab_cab_tran_cxc, String ide_ccctr, String num_doc) {
        String str_p_cxc_tipo_trans_pago = utilitario.getVariable("p_cxc_tipo_trans_pago");
        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_tran_cxc.getValor("ide_cccfa"));
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
        tab_det_tran_cxc.setValor("ide_ccctr", ide_ccctr);
        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_tran_cxc.getValor("fecha_emisi_cccfa"));
        double valor = Double.parseDouble(tab_cab_tran_cxc.getValor("total_cccfa"));
        tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_tran_cxc.getValor("observacion_cccfa") + " (Transferencia Casas)");
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", buscarMaximoPago(ide_ccctr));
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", tab_cab_tran_cxc.getValor("fecha_emisi_cccfa"));
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_tran_cxc.getValor("secuencial_cccfa"));
        tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_tran_cxc.getValor("ide_cnccc"));
        tab_det_tran_cxc.guardar();
    }

    public void generarTransaccionVentaDonacion(Tabla tab_cab_factura, double total_retenido) {
        System.out.println("cxc venta donacion");
        String str_p_cxc_tipo_trans_factura = utilitario.getVariable("p_cxc_tipo_trans_factura");//Tipo transaccion Factura     
        tab_cab_tran_cxc.limpiar();
        tab_det_tran_cxc.limpiar();
        tab_cab_tran_cxc.insertar();
        if (tab_cab_factura != null) {
            tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_cab_tran_cxc.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxc.setValor("fecha_trans_ccctr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_cab_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            if (tab_cab_factura.getValor("observacion_cccfa") != null && !tab_cab_factura.getValor("observacion_cccfa").isEmpty()) {
                tab_cab_tran_cxc.setValor("observacion_ccctr", tab_cab_factura.getValor("observacion_cccfa"));
            }
            tab_cab_tran_cxc.guardar();
            tab_det_tran_cxc.insertar();
            // primer detalle cxc de tipo factura
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            double valor = Double.parseDouble(tab_cab_factura.getValor("total_cccfa")) - total_retenido;
            tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_factura.getValor("observacion_cccfa"));
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", 0 + "");
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cccfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_factura.getValor("secuencial_cccfa"));
            tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            // genero el pago en cxc otro detalle
            generarTransaccionPagoDonacion(tab_cab_factura, tab_cab_tran_cxc.getValor("ide_ccctr"));
        }

    }

    public void generarTransaccionPagoDonacion(Tabla tab_cab_tran_cxc, String ide_ccctr) {
        System.out.println("cxc pago venta donacion");

        String str_p_cxc_tipo_trans_pago = utilitario.getVariable("p_cxc_tipo_trans_pago");
        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_tran_cxc.getValor("ide_cccfa"));
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
        tab_det_tran_cxc.setValor("ide_ccctr", ide_ccctr);
        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_tran_cxc.getValor("fecha_emisi_cccfa"));
        double valor = Double.parseDouble(tab_cab_tran_cxc.getValor("total_cccfa"));
        tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_tran_cxc.getValor("observacion_cccfa"));
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", buscarMaximoPago(ide_ccctr));
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", tab_cab_tran_cxc.getValor("fecha_emisi_cccfa"));
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_tran_cxc.getValor("secuencial_cccfa"));
        tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_tran_cxc.getValor("ide_cnccc"));
        tab_det_tran_cxc.guardar();
    }

    public void generarTransaccionVenta(Tabla tab_cab_factura, double total_retenido, Tabla tab_cab_libr_ban) {
        String str_p_cxc_tipo_trans_factura = utilitario.getVariable("p_cxc_tipo_trans_factura");//Tipo transaccion Factura     
        tab_cab_tran_cxc.limpiar();
        tab_det_tran_cxc.limpiar();
        tab_cab_tran_cxc.insertar();
        if (tab_cab_factura != null) {
            tab_cab_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_cab_tran_cxc.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxc.setValor("fecha_trans_ccctr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            tab_cab_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            if (tab_cab_factura.getValor("observacion_cccfa") != null && !tab_cab_factura.getValor("observacion_cccfa").isEmpty()) {
                tab_cab_tran_cxc.setValor("observacion_ccctr", tab_cab_factura.getValor("observacion_cccfa"));
            }
            tab_cab_tran_cxc.guardar();
            tab_det_tran_cxc.insertar();
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_factura.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_factura);
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_factura.getValor("fecha_emisi_cccfa"));
            double valor = Double.parseDouble(tab_cab_factura.getValor("total_cccfa")) - total_retenido;
            tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_factura.getValor("observacion_cccfa"));
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", 0 + "");
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cccfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_factura.getValor("secuencial_cccfa"));
            tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
        }

        if (tab_cab_libr_ban != null) {
            generarTransaccionPago(tab_cab_tran_cxc, tab_cab_libr_ban, tab_cab_tran_cxc.getValor("ide_ccctr"));
        } else {
            tab_det_tran_cxc.guardar();
        }
    }

    public void generarAnticipoCxC(Tabla tab_cab_libr_ban, String ide_geper) {
        tab_cab_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_anticipo"));
        tab_cab_tran_cxc.setValor("ide_geper", ide_geper);
        tab_cab_tran_cxc.setValor("fecha_trans_ccctr", utilitario.getFechaActual());
        tab_cab_tran_cxc.setValor("observacion_ccctr", "transaccion por anticipo");
        tab_cab_tran_cxc.guardar();
        tab_det_tran_cxc.insertar();
        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
        tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_anticipo"));// tipo transaccion anticipo
        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
        tab_det_tran_cxc.setValor("ide_teclb", tab_cab_libr_ban.getValor("ide_teclb"));
        tab_det_tran_cxc.setValor("valor_ccdtr", tab_cab_libr_ban.getValor("valor_teclb"));
        tab_det_tran_cxc.setValor("observacion_ccdtr", "transaccion por anticipo");
        tab_det_tran_cxc.setValor("numero_pago_ccdtr", 1 + "");
        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
        tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_libr_ban.getValor("numero_teclb"));
        tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_libr_ban.getValor("ide_cnccc"));

    }

    public void generarTransaccionPago(Tabla tab_cab_tran_cxc, Tabla tab_cab_libr_ban, String ide_ccctr) {

        this.tab_cab_tran_cxc = tab_cab_tran_cxc;
        String str_p_cxc_tipo_trans_pago = utilitario.getVariable("p_cxc_tipo_trans_pago");
        if (tab_cab_libr_ban != null) {
            tab_det_tran_cxc.insertar();
            tab_det_tran_cxc.setValor("ide_teclb", tab_cab_libr_ban.getValor("ide_teclb"));
            tab_det_tran_cxc.setValor("ide_cccfa", tab_cab_tran_cxc.getValor("ide_cccfa"));
            tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
            tab_det_tran_cxc.setValor("ide_ccctr", tab_cab_tran_cxc.getValor("ide_ccctr"));
            tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
            double valor = Double.parseDouble(tab_cab_libr_ban.getValor("valor_teclb"));
            tab_det_tran_cxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_libr_ban.getValor("observacion_teclb"));
            tab_det_tran_cxc.setValor("numero_pago_ccdtr", buscarMaximoPago(tab_cab_tran_cxc.getValor("ide_ccctr")));
            tab_det_tran_cxc.setValor("fecha_venci_ccdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
            tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_libr_ban.getValor("numero_teclb"));
            tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_libr_ban.getValor("ide_cnccc"));
        }

        tab_det_tran_cxc.guardar();
    }

    public void generarPagoTransaccionVentaConBancos(List lis_fac_pagadas, Tabla tab_cab_libr_ban) {

        if (lis_fac_pagadas.size() > 0) {
            if (tab_cab_libr_ban != null) {
                String str_p_cxc_tipo_trans_pago = utilitario.getVariable("p_cxc_tipo_trans_pago");
                tab_det_tran_cxc.limpiar();
                for (int i = 0; i < lis_fac_pagadas.size(); i++) {
                    Object obj_fila[] = (Object[]) lis_fac_pagadas.get(i);
                    tab_det_tran_cxc.insertar();
                    if (obj_fila[0] != null) {
                        tab_det_tran_cxc.setValor("ide_cccfa", obj_fila[0] + "");
                    }
                    tab_det_tran_cxc.setValor("ide_teclb", tab_cab_libr_ban.getValor("ide_teclb"));
                    tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                    tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
                    tab_det_tran_cxc.setValor("ide_ccctr", obj_fila[1] + "");
                    tab_det_tran_cxc.setValor("fecha_trans_ccdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
                    tab_det_tran_cxc.setValor("valor_ccdtr", obj_fila[2] + "" + "");
                    tab_det_tran_cxc.setValor("observacion_ccdtr", tab_cab_libr_ban.getValor("observacion_teclb"));
                    tab_det_tran_cxc.setValor("numero_pago_ccdtr", buscarMaximoPago(obj_fila[1] + ""));
                    tab_det_tran_cxc.setValor("fecha_venci_ccdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
                    tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_cab_libr_ban.getValor("numero_teclb"));
                    tab_det_tran_cxc.setValor("ide_cnccc", tab_cab_libr_ban.getValor("ide_cnccc"));
                }
                tab_det_tran_cxc.guardar();
            }
        }
    }

    public void generarPagoTransaccionVentaSinBancos(List lis_fac_pagadas, String ide_cnccc, String fecha, String num_doc, String observacion) {

        if (lis_fac_pagadas.size() > 0) {
            if (ide_cnccc != null && !ide_cnccc.isEmpty()) {
                String str_p_cxc_tipo_trans_pago = utilitario.getVariable("p_cxc_tipo_trans_pago");
                tab_det_tran_cxc.limpiar();

                for (int i = 0; i < lis_fac_pagadas.size(); i++) {
                    Object obj_fila[] = (Object[]) lis_fac_pagadas.get(i);
                    tab_det_tran_cxc.insertar();
                    if (obj_fila[0] != null) {
                        tab_det_tran_cxc.setValor("ide_cccfa", obj_fila[0] + "");
                    }
                    tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                    tab_det_tran_cxc.setValor("ide_ccttr", str_p_cxc_tipo_trans_pago);
                    tab_det_tran_cxc.setValor("ide_ccctr", obj_fila[1] + "");
                    tab_det_tran_cxc.setValor("fecha_trans_ccdtr", fecha);
                    tab_det_tran_cxc.setValor("valor_ccdtr", obj_fila[2] + "" + "");
                    tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
                    tab_det_tran_cxc.setValor("numero_pago_ccdtr", buscarMaximoPago(obj_fila[1] + ""));
                    tab_det_tran_cxc.setValor("fecha_venci_ccdtr", fecha);
                    tab_det_tran_cxc.setValor("docum_relac_ccdtr", num_doc);
                    tab_det_tran_cxc.setValor("ide_cnccc", ide_cnccc);
                }
                tab_det_tran_cxc.guardar();
                System.out.println("se ha realizado una transaccion de cobro sin bancos ");

            }
        }
    }

//    public void reversar(String ide_cnccc, String ide_cccfa, String observacion) {
//        if (ide_cccfa != null && !ide_cccfa.isEmpty()) {
//            Tabla tab_cab = utilitario.consultar("SELECT * from cxc_cabece_transa where ide_cccfa=" + ide_cccfa);
//            if (tab_cab.getTotalFilas() > 0) {
//                if (tab_cab.getValor("ide_ccctr") != null && !tab_cab.getValor("ide_ccctr").isEmpty()) {
//                    Tabla tab_det = utilitario.consultar("SELECT * from cxc_detall_transa where ide_ccctr=" + tab_cab.getValor("ide_ccctr"));
//                    Tabla tab_det_tran = new Tabla();
//                    tab_det_tran.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
//                    tab_det_tran.setCondicion("ide_ccdtr=-1");
//                    tab_det_tran.ejecutarSql();
//                    for (int i = 0; i < tab_det.getTotalFilas(); i++) {
//                        tab_det_tran.insertar();
//                        tab_det_tran.setValor("ide_ccctr", tab_cab.getValor("ide_ccctr"));
//                        tab_det_tran.setValor("ide_cccfa", tab_det.getValor(i, "ide_cccfa"));
//                        tab_det_tran.setValor("ide_cnccc", ide_cnccc);
//                        tab_det_tran.setValor("ide_teclb", tab_det.getValor(i, "ide_teclb"));
//                        tab_det_tran.setValor("ide_usua", utilitario.getVariable("ide_usua"));
//                        if (tab_det.getValor(i, "ide_ccttr") != null && !tab_det.getValor(i, "ide_ccttr").isEmpty()) {
//                            if (Integer.parseInt(getSignoTransaccion(tab_det.getValor(i, "ide_ccttr"))) > 0) {
//                                tab_det_tran.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_menos"));
//                            } else {
//                                tab_det_tran.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_mas"));
//                            }
//                        }
//                        tab_det_tran.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
//                        tab_det_tran.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
//                        tab_det_tran.setValor("numero_pago_ccdtr", tab_det.getValor(i, "numero_pago_ccdtr"));
//                        tab_det_tran.setValor("valor_ccdtr", tab_det.getValor(i, "valor_ccdtr"));
//                        tab_det_tran.setValor("docum_relac_ccdtr", tab_det.getValor(i, "docum_relac_ccdtr"));
//                        tab_det_tran.setValor("observacion_ccdtr", observacion);
//                    }
//                    tab_det_tran.guardar();
//                }
//            }
//        }
//
//    }

    public void reversar(String ide_cccfa, String observacion) {
        if (ide_cccfa != null && !ide_cccfa.isEmpty()) {
            TablaGenerica tab_cab = utilitario.consultar("SELECT * from cxc_cabece_transa where ide_cccfa=" + ide_cccfa);
            if (tab_cab.getTotalFilas() > 0) {
                if (tab_cab.getValor("ide_ccctr") != null && !tab_cab.getValor("ide_ccctr").isEmpty()) {
                    TablaGenerica tab_det = utilitario.consultar("SELECT * from cxc_detall_transa where ide_ccctr=" + tab_cab.getValor(0,"ide_ccctr"));
                    tab_det_tran_cxc.limpiar();
                    for (int i = 0; i < tab_det.getTotalFilas(); i++) {
                        tab_det_tran_cxc.insertar();
                        tab_det_tran_cxc.setValor("ide_ccctr", tab_cab.getValor("ide_ccctr"));
                        tab_det_tran_cxc.setValor("ide_cccfa", tab_det.getValor(i, "ide_cccfa"));
                        tab_det_tran_cxc.setValor("ide_cnccc", tab_det.getValor(i, "ide_cnccc"));
                        tab_det_tran_cxc.setValor("ide_teclb", tab_det.getValor(i, "ide_teclb"));
                        tab_det_tran_cxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                        if (tab_det.getValor(i, "ide_ccttr") != null && !tab_det.getValor(i, "ide_ccttr").isEmpty()) {
                            if (Integer.parseInt(getSignoTransaccion(tab_det.getValor(i, "ide_ccttr"))) > 0) {
                                tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_menos"));
                            } else {
                                tab_det_tran_cxc.setValor("ide_ccttr", utilitario.getVariable("p_cxc_tipo_trans_reversar_mas"));
                            }
                        }
                        tab_det_tran_cxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
                        tab_det_tran_cxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
                        tab_det_tran_cxc.setValor("numero_pago_ccdtr", tab_det.getValor(i, "numero_pago_ccdtr"));
                        tab_det_tran_cxc.setValor("valor_ccdtr", tab_det.getValor(i, "valor_ccdtr"));
                        tab_det_tran_cxc.setValor("docum_relac_ccdtr", tab_det.getValor(i, "docum_relac_ccdtr"));
                        tab_det_tran_cxc.setValor("observacion_ccdtr", observacion);
                    }
                    tab_det_tran_cxc.guardar();
                }
            }
        }
    }
    
    public String getSignoTransaccion(String ide_ccttr) {
        TablaGenerica tab_tipo_transacciones = utilitario.consultar("select *from cxc_tipo_transacc where ide_ccttr=" + ide_ccttr);
        if (tab_tipo_transacciones.getTotalFilas() > 0) {
            if (tab_tipo_transacciones.getValor(0, "signo_ccttr") != null && !tab_tipo_transacciones.getValor(0, "signo_ccttr").isEmpty()) {
                return tab_tipo_transacciones.getValor(0, "signo_ccttr");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String buscarMaximoPago(String ide_ccctr) {
        //RETORNA EL PAGO MAXIMO 
        System.out.println("select max(numero_pago_ccdtr) from cxc_detall_transa where ide_ccctr=" + ide_ccctr);
        List lis_sql = utilitario.getConexion().consultar("select max(numero_pago_ccdtr) from cxc_detall_transa where ide_ccctr=" + ide_ccctr);
        int num = 0;
        if (lis_sql.get(0) != null) {
            num = Integer.parseInt(lis_sql.get(0) + "") + 1;
        }
        return (num + 1) + "";
    }

    public int obtenerDiasPago(String ide_cndfp) {
        List dias_sql = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
        int int_dias = 0;
        if (dias_sql != null && !dias_sql.isEmpty()) {
            int_dias = Integer.parseInt(dias_sql.get(0).toString());
        }
        return int_dias;
    }

    public Tabla gettab_cab_tran_cxc(String ide_ccctr) {
        tab_cab_tran_cxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
        tab_cab_tran_cxc.setCondicion("ide_ccctr=" + ide_ccctr);
        tab_cab_tran_cxc.ejecutarSql();
        return tab_cab_tran_cxc;
    }

    public Tabla geTab_cab_tran_cxc() {
        return tab_cab_tran_cxc;
    }

    public void setTab_cab_tran_cxc(Tabla tab_cab_tran_cxc) {
        this.tab_cab_tran_cxc = tab_cab_tran_cxc;
    }

    public Tabla getTab_det_tran_cxc() {
        return tab_det_tran_cxc;
    }

    public void setTab_det_tran_cxc(Tabla tab_det_tran_cxc) {
        this.tab_det_tran_cxc = tab_det_tran_cxc;
    }
}
