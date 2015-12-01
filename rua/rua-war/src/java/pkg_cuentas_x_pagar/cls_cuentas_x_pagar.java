/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_cuentas_x_pagar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author Diego
 */
public class cls_cuentas_x_pagar {

    private Utilitario utilitario = new Utilitario();
    private Tabla tab_cab_tran_cxp = new Tabla();
    private Tabla tab_det_tran_cxp = new Tabla();

    public cls_cuentas_x_pagar() {

        tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr", -1);
        tab_cab_tran_cxp.getColumna("ide_cpctr").setExterna(false);
        tab_cab_tran_cxp.setCondicion("ide_cpctr=-1");
        tab_cab_tran_cxp.ejecutarSql();

        tab_det_tran_cxp.setTabla("cxp_detall_transa", "ide_cpdtr", -1);
        tab_det_tran_cxp.getColumna("ide_cpdtr").setExterna(false);
        tab_det_tran_cxp.setCondicion("ide_cpdtr=-1");
        tab_det_tran_cxp.ejecutarSql();
    }

    public String getSaldoPorPagar(String ide_geper) {
        TablaGenerica tab_cuentas_x_pagar = utilitario.consultar(getSqlCuentasPorPagar(ide_geper));
        if (tab_cuentas_x_pagar.getTotalFilas() > 0) {
            return tab_cuentas_x_pagar.getSumaColumna("saldo_x_pagar") + "";
        }
        return null;
    }

    public String getSqlCuentasPorPagar(String ide_geper) {
        
        utilitario.getConexion().ejecutarSql("update cxp_detall_transa set valor_anticipo_cpdtr=0 where valor_anticipo_cpdtr is null");
        //utilitario.getConexion().commit();
        
        String str_sql_cxp = "select dt.ide_cpctr,"
                + "dt.ide_cpcfa,"
                + "case when (cf.fecha_emisi_cpcfa) is null then ct.fecha_trans_cpctr else cf.fecha_emisi_cpcfa end,"
                + "cf.numero_cpcfa,"
                + "cf.total_cpcfa,"
//                + "sum (dt.valor_cpdtr*tt.signo_cpttr) as saldo_x_pagar,"
                + "sum (dt.valor_cpdtr*tt.signo_cpttr)-sum (dt.valor_anticipo_cpdtr) as saldo_x_pagar, "
                + "case when (cf.observacion_cpcfa) is NULL then ct.observacion_cpctr else cf.observacion_cpcfa end "
                + "from cxp_detall_transa dt "
                + "left join cxp_cabece_transa ct on dt.ide_cpctr=ct.ide_cpctr "
                + "left join cxp_cabece_factur cf on cf.ide_cpcfa=ct.ide_cpcfa and cf.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_normal") + " "
                + "left join cxp_tipo_transacc tt on tt.ide_cpttr=dt.ide_cpttr "
                + "where ct.ide_geper=" + ide_geper + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "GROUP BY dt.ide_cpcfa,dt.ide_cpctr,cf.numero_cpcfa, "
                + "cf.observacion_cpcfa,ct.observacion_cpctr,cf.fecha_emisi_cpcfa,ct.fecha_trans_cpctr,cf.total_cpcfa "
                + "HAVING sum (dt.valor_cpdtr*tt.signo_cpttr)-sum (dt.valor_anticipo_cpdtr) > 0 "
                + "ORDER BY cf.fecha_emisi_cpcfa ASC ,ct.fecha_trans_cpctr ASC,dt.ide_cpctr ASC";

        return str_sql_cxp;
    }

    public String getSqlReporteCompras(String fecha_ini, String fecha_fin) {
        String sql_rep_compras = "select ccr.ide_cncre, "
                + "per.identificac_geper, "
                + "ccr.fecha_emisi_cncre , "
                + "ctd.alter_tribu_cntdo, "
                + "cfa.numero_cpcfa, "
                + "cci.casillero_cncim, "
                + "cfa.base_no_objeto_iva_cpcfa, "
                + "cfa.base_tarifa0_cpcfa, "
                + "cfa.base_grabada_cpcfa, "
                + "cfa.valor_iva_cpcfa, "
                + "cim.ide_cnimp, "
                + "cdr.porcentaje_cndre as por_ret_iva, "
                + "cdr.valor_cndre as val_ret_iva, "
                + "cdr.base_cndre,"
                + "cdr.porcentaje_cndre as por_ret_renta, "
                + "cdr.valor_cndre as val_ret_renta, "
                + "tst.alterno_srtst, "
                + "'N' as devolucion, "
                + "ccr.numero_cncre  "
                + "from con_cabece_retenc ccr "
                + "left join con_detall_retenc cdr on cdr.ide_cncre=ccr.ide_cncre "
                + "left join con_cabece_impues cci on cci.ide_cncim=cdr.ide_cncim "
                + "left join con_impuesto cim on cim.ide_cnimp=cci.ide_cnimp "
                + "left join cxp_cabece_factur cfa on cfa.ide_cncre=ccr.ide_cncre "
                + "left join gen_persona per on per.ide_geper=cfa.ide_geper "
                + "left join con_tipo_document ctd on ctd.ide_cntdo=cfa.ide_cntdo "
                + "left join sri_tipo_sustento_tributario tst on tst.ide_srtst=cfa.ide_srtst "
                + "where es_venta_cncre is FALSE "
                + "and ccr.fecha_emisi_cncre BETWEEN '" + fecha_ini + "' and '" + fecha_fin + "' "
                + "order by ccr.fecha_emisi_cncre,ccr.ide_cncre ASC";
        return sql_rep_compras;
    }

    public TablaGenerica getTablaReporteCompras(String fecha_inicio, String fecha_fin) {
        TablaGenerica tab_compra = utilitario.consultar(getSqlReporteCompras(fecha_inicio, fecha_fin));
        //0 iva
        //1 renta
        System.out.println("tot " + tab_compra.getTotalFilas());
        for (int i = 0; i < tab_compra.getTotalFilas(); i++) {
            //iva y renta
            try {
                if (tab_compra.getValor(i, "ide_cnimp").equals("0")) {
                    tab_compra.setValor(i, "por_ret_renta", "");
                    tab_compra.setValor(i, "val_ret_renta", "");
                } else {
                    tab_compra.setValor(i, "por_ret_iva", "");
                    tab_compra.setValor(i, "val_ret_iva", "");
                }
                if (Double.parseDouble(tab_compra.getValor(i, "base_no_objeto_iva_cpcfa")) == 0) {
                    tab_compra.setValor(i, "base_no_objeto_iva_cpcfa", "");
                }
                if (Double.parseDouble(tab_compra.getValor(i, "base_tarifa0_cpcfa")) == 0) {
                    tab_compra.setValor(i, "base_tarifa0_cpcfa", "");
                }
                if (Double.parseDouble(tab_compra.getValor(i, "base_grabada_cpcfa")) == 0) {
                    tab_compra.setValor(i, "base_grabada_cpcfa", "");
                }
                if (Double.parseDouble(tab_compra.getValor(i, "valor_iva_cpcfa")) == 0) {
                    tab_compra.setValor(i, "valor_iva_cpcfa", "");
                }
                String ide_cncre = "";
                if (i > 0) {
                    ide_cncre = tab_compra.getValor(i - 1, "ide_cncre");
                }
                if (ide_cncre.equals(tab_compra.getValor(i, "ide_cncre"))) {
                    tab_compra.setValor(i, "identificac_geper", "");
                    tab_compra.setValor(i, "numero_cncre", "");
                }

            } catch (Exception e) {
            }
        }

        return tab_compra;
    }

    public String buscarSecuencialFactura(String ide_cpdaf) {
        if (ide_cpdaf != null) {
            List secuencial_sql = utilitario.getConexion().consultar("select MAX(numero_cpcfa) FROM cxp_cabece_factura where ide_cpdaf=" + ide_cpdaf);
            if (secuencial_sql.get(0) != null) {
                Long secuencial = Long.parseLong(secuencial_sql.get(0).toString());
                secuencial = secuencial + 1;
                String ceros = utilitario.generarCero(8 - secuencial.toString().length());
                String num_max = ceros.concat(secuencial.toString());
                return num_max;
            }
        }
        return null;
    }

    public void generarCabeceraTransaccionCxP(String ide_geper, String ide_cpttr, String fecha, String ide_cpcfa, String observacion, boolean boo_varias_cabeceras, String ide_cpctr) {
        if (!boo_varias_cabeceras) {
            tab_cab_tran_cxp.limpiar();
        }
        tab_cab_tran_cxp.insertar();
        tab_cab_tran_cxp.setValor("ide_cpttr", ide_cpttr);
        tab_cab_tran_cxp.setValor("ide_geper", ide_geper);
        tab_cab_tran_cxp.setValor("fecha_trans_cpctr", fecha);
        tab_cab_tran_cxp.setValor("ide_cpcfa", ide_cpcfa);
        tab_cab_tran_cxp.setValor("observacion_cpctr", observacion);
        if (!boo_varias_cabeceras) {
            tab_cab_tran_cxp.guardar();
        } else {
            tab_cab_tran_cxp.setValor("ide_cpctr", ide_cpctr);
        }
    }

    public void generarDetalleTransaccionCxP(String ide_cpctr, String ide_cpttr, String fecha, String ide_cpcfa, String observacion, double valor, String num_doc, String ide_cnccc, String ide_teclb) {
        tab_det_tran_cxp.insertar();
        tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxp.setValor("ide_cpctr", ide_cpctr);
        tab_det_tran_cxp.setValor("ide_cpcfa", ide_cpcfa);
        tab_det_tran_cxp.setValor("ide_cpttr", ide_cpttr);
        tab_det_tran_cxp.setValor("fecha_trans_cpdtr", fecha);
        tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxp.setValor("observacion_cpdtr", observacion);
        tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
//        tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
        tab_det_tran_cxp.setValor("docum_relac_cpdtr", num_doc);
        tab_det_tran_cxp.setValor("ide_cnccc", ide_cnccc);
        tab_det_tran_cxp.setValor("ide_teclb", ide_teclb);
    }

    public void generarCxP(String ide_geper, String ide_cpttr, String fecha, String ide_cpcfa, String observacion, double valor, String num_doc, String ide_cnccc) {
        tab_cab_tran_cxp.limpiar();
        tab_det_tran_cxp.limpiar();
        tab_cab_tran_cxp.insertar();
        tab_cab_tran_cxp.setValor("ide_cpttr", ide_cpttr);
        tab_cab_tran_cxp.setValor("ide_geper", ide_geper);
        tab_cab_tran_cxp.setValor("fecha_trans_cpctr", fecha);
        tab_cab_tran_cxp.setValor("ide_cpcfa", ide_cpcfa);
        tab_cab_tran_cxp.setValor("observacion_cpctr", observacion);
        tab_cab_tran_cxp.guardar();
        tab_det_tran_cxp.insertar();
        tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
        tab_det_tran_cxp.setValor("ide_cpcfa", ide_cpcfa);
        tab_det_tran_cxp.setValor("ide_cpttr", ide_cpttr);
        tab_det_tran_cxp.setValor("fecha_trans_cpdtr", fecha);
        tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
        tab_det_tran_cxp.setValor("observacion_cpdtr", observacion);
        tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
//        tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
        tab_det_tran_cxp.setValor("docum_relac_cpdtr", num_doc);
        tab_det_tran_cxp.setValor("ide_cnccc", ide_cnccc);
        tab_det_tran_cxp.guardar();
    }

    public void generarTransaccionCompra(Tabla tab_cab_factura, double total_retenido, Tabla tab_cab_libr_ban,double tot_anticipo) {
        String str_p_cxp_tipo_trans_factura = utilitario.getVariable("p_cxp_tipo_trans_factura");//Tipo transaccion Factura     
        if (tab_cab_factura != null) {
            tab_cab_tran_cxp.limpiar();
            tab_det_tran_cxp.limpiar();
            tab_cab_tran_cxp.insertar();
            tab_cab_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_cab_tran_cxp.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_cab_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("observacion_cpcfa") != null && !tab_cab_factura.getValor("observacion_cpcfa").isEmpty()) {
                tab_cab_tran_cxp.setValor("observacion_cpctr", tab_cab_factura.getValor("observacion_cpcfa"));
            }
            tab_cab_tran_cxp.guardar();
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            double valor = Double.parseDouble(tab_cab_factura.getValor("total_cpcfa")) - total_retenido;
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
        }

        
        if (tab_cab_libr_ban != null) {
            generarPagoTransaccionCompra(tab_cab_tran_cxp, tab_cab_libr_ban,tot_anticipo);
        } else {
            tab_det_tran_cxp.guardar();
        }
    }

    public void generarTransaccionCompra(Tabla tab_cab_factura, double total_retenido, boolean es_pago_especial) {
        String str_p_cxp_tipo_trans_factura = utilitario.getVariable("p_cxp_tipo_trans_factura");//Tipo transaccion Factura     
        double valor = 0;
        if (tab_cab_factura != null) {
            tab_cab_tran_cxp.limpiar();
            tab_det_tran_cxp.limpiar();
            tab_cab_tran_cxp.insertar();
            tab_cab_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_cab_tran_cxp.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_cab_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("observacion_cpcfa") != null && !tab_cab_factura.getValor("observacion_cpcfa").isEmpty()) {
                tab_cab_tran_cxp.setValor("observacion_cpctr", tab_cab_factura.getValor("observacion_cpcfa"));
            }
            tab_cab_tran_cxp.guardar();
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            valor = Double.parseDouble(tab_cab_factura.getValor("total_cpcfa")) - total_retenido;
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            if (es_pago_especial) {
                generarPagoTransaccionCompra(tab_cab_factura, tab_cab_tran_cxp.getValor("ide_cpctr"), valor,false);
            } else {
                tab_det_tran_cxp.guardar();
            }
        }
    }

    public void generarTransaccionCompra(Tabla tab_cab_factura, double total_retenido, double total_anticipo) {
        String str_p_cxp_tipo_trans_factura = utilitario.getVariable("p_cxp_tipo_trans_factura");//Tipo transaccion Factura     
        if (tab_cab_factura != null && total_anticipo > 0) {
            tab_cab_tran_cxp.limpiar();
            tab_det_tran_cxp.limpiar();
            tab_cab_tran_cxp.insertar();
            tab_cab_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_cab_tran_cxp.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_cab_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("observacion_cpcfa") != null && !tab_cab_factura.getValor("observacion_cpcfa").isEmpty()) {
                tab_cab_tran_cxp.setValor("observacion_cpctr", tab_cab_factura.getValor("observacion_cpcfa"));
            }
            tab_cab_tran_cxp.guardar();
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            double valor = Double.parseDouble(tab_cab_factura.getValor("total_cpcfa")) - total_retenido;
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            total_anticipo = Double.parseDouble(utilitario.getFormatoNumero(total_anticipo, 2));
            valor = Double.parseDouble(utilitario.getFormatoNumero(valor, 2));
            if (total_anticipo >= valor) {
                generarPagoTransaccionCompra(tab_cab_factura, tab_cab_tran_cxp.getValor("ide_cpctr"), valor,true);
            } else {
                generarPagoTransaccionCompra(tab_cab_factura, tab_cab_tran_cxp.getValor("ide_cpctr"), total_anticipo,true);
            }
        }
    }

    public void generarVariosPagosTransaccionCompra(Tabla tab_cab_tran_cxp, Tabla tab_cab_libr_ban, boolean es_tarjeta_credito, String ide_cpctr, double tot_retenido) {
        String str_p_cxp_tipo_trans_pago = utilitario.getVariable("p_cxp_tipo_trans_pago");
        if (tab_cab_libr_ban != null) {
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_teclb", tab_cab_libr_ban.getValor("ide_teclb"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_tran_cxp.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_pago);
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
            double valor = Double.parseDouble(tab_cab_libr_ban.getValor("valor_teclb")) - tot_retenido;
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_libr_ban.getValor("observacion_teclb"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", buscarMaximoPago(tab_cab_tran_cxp.getValor("ide_cpctr")));
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_libr_ban.getValor("numero_teclb"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_libr_ban.getValor("ide_cnccc"));
        }
        if (es_tarjeta_credito) {
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_tran_cxp.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_pago);
            tab_det_tran_cxp.setValor("ide_cpctr", ide_cpctr);
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_tran_cxp.getValor("fecha_trans_cpcfa"));
            double valor = Double.parseDouble(tab_cab_tran_cxp.getValor("total_cpcfa")) - tot_retenido;
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_tran_cxp.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", buscarMaximoPago(ide_cpctr));
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", tab_cab_tran_cxp.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_tran_cxp.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_tran_cxp.getValor("ide_cnccc"));
        }

    }

    public void generarVariasTransaccionesCompra(Tabla tab_cab_factura, double total_retenido, Tabla tab_cab_libr_ban, String ide_cpctr, String ide_cpdtr, boolean es_tarjeta_credito) {
        String str_p_cxp_tipo_trans_factura = utilitario.getVariable("p_cxp_tipo_trans_factura");//Tipo transaccion Factura     
        if (tab_cab_factura != null) {
            tab_cab_tran_cxp.insertar();
            if (ide_cpctr != null && !ide_cpctr.isEmpty()) {
                tab_cab_tran_cxp.setValor("ide_cpctr", ide_cpctr);
            }
            tab_cab_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_cab_tran_cxp.setValor("ide_geper", tab_cab_factura.getValor("ide_geper"));
            tab_cab_tran_cxp.setValor("fecha_trans_cpctr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_cab_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            if (tab_cab_factura.getValor("observacion_cpcfa") != null && !tab_cab_factura.getValor("observacion_cpcfa").isEmpty()) {
                tab_cab_tran_cxp.setValor("observacion_cpctr", tab_cab_factura.getValor("observacion_cpcfa"));
            }
            tab_det_tran_cxp.insertar();
            if (ide_cpdtr != null && !ide_cpdtr.isEmpty()) {
                tab_det_tran_cxp.setValor("ide_cpdtr", ide_cpdtr);
            }
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_factura);
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            double valor = Double.parseDouble(tab_cab_factura.getValor("total_cpcfa")) - total_retenido;
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", 0 + "");
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura.getValor("fecha_emisi_cpcfa")), obtenerDiasPago(tab_cab_factura.getValor("ide_cndfp")))));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
        }

        if (tab_cab_libr_ban != null) {
            generarVariosPagosTransaccionCompra(tab_cab_tran_cxp, tab_cab_libr_ban, false, tab_cab_tran_cxp.getValor("ide_cpctr"), total_retenido);
        } else {
            if (es_tarjeta_credito) {
                generarVariosPagosTransaccionCompra(tab_cab_factura, null, true, tab_cab_tran_cxp.getValor("ide_cpctr"), total_retenido);
            }
        }
    }

    public void generarAnticipoCxP(Tabla tab_cab_libr_ban, String ide_geper) {
        tab_cab_tran_cxp.limpiar();
        tab_det_tran_cxp.limpiar();
        tab_cab_tran_cxp.insertar();
        tab_cab_tran_cxp.setValor("ide_cpttr", utilitario.getVariable("p_cxp_tipo_trans_anticipo"));
        tab_cab_tran_cxp.setValor("ide_geper", ide_geper);
        tab_cab_tran_cxp.setValor("fecha_trans_cpctr", utilitario.getFechaActual());
        tab_cab_tran_cxp.setValor("observacion_cpctr", "transaccion por anticipo ");
        tab_cab_tran_cxp.guardar();
        tab_det_tran_cxp.insertar();
        tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
        tab_det_tran_cxp.setValor("ide_cpttr", utilitario.getVariable("p_cxp_tipo_trans_anticipo"));// tipo transaccion anticipo
        tab_det_tran_cxp.setValor("fecha_trans_cpdtr", utilitario.getFechaActual());
        tab_det_tran_cxp.setValor("ide_teclb", tab_cab_libr_ban.getValor("ide_teclb"));
        tab_det_tran_cxp.setValor("valor_cpdtr", tab_cab_libr_ban.getValor("valor_teclb"));
        tab_det_tran_cxp.setValor("observacion_cpdtr", "transaccion por anticipo");
        tab_det_tran_cxp.setValor("numero_pago_cpdtr", 1 + "");
        tab_det_tran_cxp.setValor("fecha_venci_cpdtr", utilitario.getFechaActual());
        tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_libr_ban.getValor("numero_teclb"));
        tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_libr_ban.getValor("ide_cnccc"));
        tab_det_tran_cxp.setValor("anticipo_activo", "true");
        tab_det_tran_cxp.guardar();

    }

    public void generarPagoTransaccionCompra(Tabla tab_cab_tran_cxp, Tabla tab_cab_libr_ban,double tot_anticipo) {
        this.tab_cab_tran_cxp = tab_cab_tran_cxp;
        String str_p_cxp_tipo_trans_pago = utilitario.getVariable("p_cxp_tipo_trans_pago");
        if (tab_cab_libr_ban != null) {
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_teclb", tab_cab_libr_ban.getValor("ide_teclb"));
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_tran_cxp.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_pago);
            tab_det_tran_cxp.setValor("ide_cpctr", tab_cab_tran_cxp.getValor("ide_cpctr"));
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
            double valor = Double.parseDouble(tab_cab_libr_ban.getValor("valor_teclb"));
            tab_det_tran_cxp.setValor("valor_cpdtr", utilitario.getFormatoNumero(valor));
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_libr_ban.getValor("observacion_teclb"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", buscarMaximoPago(tab_cab_tran_cxp.getValor("ide_cpctr")));
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_libr_ban.getValor("numero_teclb"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_libr_ban.getValor("ide_cnccc"));
            tab_det_tran_cxp.setValor("valor_anticipo_cpdtr",tot_anticipo+"");
            
            tab_det_tran_cxp.guardar();
        }
    }

    public void generarPagoTransaccionCompra(Tabla tab_cab_factura, String ide_cpctr, double tot_pagar, boolean es_anticipo) {
        String str_p_cxp_tipo_trans_pago ;
        if (es_anticipo) {
            str_p_cxp_tipo_trans_pago = utilitario.getVariable("p_cxp_tipo_trans_pago_con_anticipo");
        } else {
            str_p_cxp_tipo_trans_pago = utilitario.getVariable("p_cxp_tipo_trans_pago");
        }
        if (tab_cab_factura != null && ide_cpctr != null && !ide_cpctr.isEmpty() && tot_pagar > 0) {
            tab_det_tran_cxp.insertar();
            tab_det_tran_cxp.setValor("ide_cpcfa", tab_cab_factura.getValor("ide_cpcfa"));
            tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_pago);
            tab_det_tran_cxp.setValor("ide_cpctr", ide_cpctr);
            tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("valor_cpdtr", tot_pagar + "");
            tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_factura.getValor("observacion_cpcfa"));
            tab_det_tran_cxp.setValor("numero_pago_cpdtr", buscarMaximoPago(ide_cpctr));
            tab_det_tran_cxp.setValor("fecha_venci_cpdtr", tab_cab_factura.getValor("fecha_trans_cpcfa"));
            tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_factura.getValor("numero_cpcfa"));
            tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_factura.getValor("ide_cnccc"));
            tab_det_tran_cxp.guardar();
        }
    }

    public void generarPagoFacturaSinBancos(List lis_fac_pagadas, String ide_cnccc, String fecha, String num_doc, String observacion) {
        if (ide_cnccc != null && !ide_cnccc.isEmpty()) {
            String str_p_cxp_tipo_trans_pago = utilitario.getVariable("p_cxp_tipo_trans_pago");
            tab_det_tran_cxp.limpiar();
            for (int i = 0; i < lis_fac_pagadas.size(); i++) {
//                try {
                Object obj_fila[] = (Object[]) lis_fac_pagadas.get(i);
                tab_det_tran_cxp.insertar();
                if (obj_fila[0] != null) {
                    tab_det_tran_cxp.setValor("ide_cpcfa", obj_fila[0] + "");
                }
                tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_pago);
                tab_det_tran_cxp.setValor("ide_cpctr", obj_fila[1] + "");
                tab_det_tran_cxp.setValor("fecha_trans_cpdtr", fecha);
                tab_det_tran_cxp.setValor("valor_cpdtr", obj_fila[2] + "");
                tab_det_tran_cxp.setValor("observacion_cpdtr", observacion);
                tab_det_tran_cxp.setValor("numero_pago_cpdtr", buscarMaximoPago(obj_fila[1] + ""));
                tab_det_tran_cxp.setValor("fecha_venci_cpdtr", fecha);
                tab_det_tran_cxp.setValor("docum_relac_cpdtr", num_doc);
                tab_det_tran_cxp.setValor("ide_cnccc", ide_cnccc);
                //              } catch (Exception e) {
//                }
            }
            tab_det_tran_cxp.guardar();
            System.out.println("se ha realizado una transaccion de pago sin bancos ");
        }
    }

    public void generarPagoFacturaConBancos(List lis_fac_pagadas, Tabla tab_cab_libr_ban) {
        if (tab_cab_libr_ban != null) {
            String str_p_cxp_tipo_trans_pago = utilitario.getVariable("p_cxp_tipo_trans_pago");
            tab_det_tran_cxp.limpiar();
            for (int i = 0; i < lis_fac_pagadas.size(); i++) {
//                try {
                Object obj_fila[] = (Object[]) lis_fac_pagadas.get(i);
                tab_det_tran_cxp.insertar();
                if (obj_fila[0] != null) {
                    tab_det_tran_cxp.setValor("ide_cpcfa", obj_fila[0] + "");
                }
                tab_det_tran_cxp.setValor("ide_teclb", tab_cab_libr_ban.getValor("ide_teclb"));
                tab_det_tran_cxp.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                tab_det_tran_cxp.setValor("ide_cpttr", str_p_cxp_tipo_trans_pago);
                tab_det_tran_cxp.setValor("ide_cpctr", obj_fila[1] + "");
                tab_det_tran_cxp.setValor("fecha_trans_cpdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
                tab_det_tran_cxp.setValor("valor_cpdtr", obj_fila[2] + "");
                tab_det_tran_cxp.setValor("observacion_cpdtr", tab_cab_libr_ban.getValor("observacion_teclb"));
                tab_det_tran_cxp.setValor("numero_pago_cpdtr", buscarMaximoPago(obj_fila[1] + ""));
                tab_det_tran_cxp.setValor("fecha_venci_cpdtr", tab_cab_libr_ban.getValor("fecha_trans_teclb"));
                tab_det_tran_cxp.setValor("docum_relac_cpdtr", tab_cab_libr_ban.getValor("numero_teclb"));
                tab_det_tran_cxp.setValor("ide_cnccc", tab_cab_libr_ban.getValor("ide_cnccc"));
                //              } catch (Exception e) {
//                }
            }
            tab_det_tran_cxp.guardar();
            System.out.println("se ha realizado una transaccion de pago en bancos ");
        }
    }

    public void reversar(String ide_cnccc, String ide_cpcfa, String observacion, String ide_cpdtr) {

        String str_sql_cab_trans = "";
        if (ide_cpcfa != null && !ide_cpcfa.isEmpty()) {
            str_sql_cab_trans = "select * from cxp_cabece_transa where ide_cpctr in ( "
                    + "select ide_cpctr from cxp_detall_transa where ide_cpcfa=" + ide_cpcfa + " )";
        } else if (ide_cpdtr != null && !ide_cpdtr.isEmpty()) {
            str_sql_cab_trans = "select * from cxp_cabece_transa where ide_cpctr in ( "
                    + "SELECT ide_cpctr from cxp_detall_transa where ide_cpdtr=" + ide_cpdtr + ")";
        }

        TablaGenerica tab_cab = utilitario.consultar(str_sql_cab_trans);
        if (tab_cab.getTotalFilas() > 0) {
            if (tab_cab.getValor(0, "ide_cpttr") != null && !tab_cab.getValor(0, "ide_cpttr").isEmpty()) {
                if (Integer.parseInt(getSignoTransaccion(tab_cab.getValor(0, "ide_cpttr"))) > 0) {
                    reversar_menos(ide_cnccc, tab_cab, observacion);
                } else {
                    reversar_mas(ide_cnccc, tab_cab, observacion);
                }
            }
        }

    }

    private synchronized void reversar_mas(String ide_cnccc, TablaGenerica tab_cab, String observacion) {

        String p_tipo_transaccion = utilitario.getVariable("p_cxp_tipo_trans_reversa_mas");
        Tabla tab_cab_tran = new Tabla();
        tab_cab_tran.setTabla("cxp_cabece_transa", "ide_cpctr", 0);
        tab_cab_tran.setCondicion("ide_cpctr=-1");
        tab_cab_tran.ejecutarSql();
        tab_cab_tran.insertar();
        tab_cab_tran.setValor("ide_cpttr", p_tipo_transaccion);
        tab_cab_tran.setValor("ide_geper", tab_cab.getValor(0, "ide_geper"));
        tab_cab_tran.setValor("fecha_trans_cpctr", tab_cab.getValor(0, "fecha_trans_cpctr"));
        tab_cab_tran.setValor("observacion_cpctr", observacion);
        tab_cab_tran.setValor("ide_cpcfa", tab_cab.getValor(0, "ide_cpcfa"));
        //Detalles

        TablaGenerica tab_det = utilitario.consultar("SELECT * from cxp_detall_transa where ide_cpctr=" + tab_cab.getValor("ide_cpctr"));
        Tabla tab_det_tran = new Tabla();
        tab_det_tran.setTabla("cxp_detall_transa", "ide_cpctr", 0);
        tab_det_tran.setCondicion("ide_cpctr=-1");
        tab_det_tran.ejecutarSql();
        tab_cab_tran.guardar();
        for (int i = 0; i < tab_det.getTotalFilas(); i++) {
            tab_det_tran.insertar();
            tab_det_tran.setValor("ide_cpctr", tab_cab_tran.getValor("ide_cpctr"));
            tab_det_tran.setValor("ide_cpcfa", tab_det.getValor(i, "ide_cpcfa"));
            tab_det_tran.setValor("ide_cnccc", ide_cnccc);
            tab_det_tran.setValor("ide_teclb", tab_det.getValor(i, "ide_teclb"));
            tab_det_tran.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran.setValor("ide_cpttr", p_tipo_transaccion);
            tab_det_tran.setValor("fecha_trans_cpdtr", utilitario.getFechaActual());
            tab_det_tran.setValor("fecha_venci_cpdtr", utilitario.getFechaActual());
            tab_det_tran.setValor("numero_pago_cpdtr", tab_det.getValor(i, "numero_pago_cpdtr"));
            tab_det_tran.setValor("valor_cpdtr", tab_det.getValor(i, "valor_cpdtr"));
            tab_det_tran.setValor("docum_relac_cpdtr", tab_det.getValor(i, "docum_relac_cpdtr"));
            tab_det_tran.setValor("observacion_cpdtr", tab_det.getValor(i, "observacion_cpdtr"));

        }
        tab_det_tran.guardar();
        System.out.println("reversa mas cxp generado");
    }

    private synchronized void reversar_menos(String ide_cnccc, TablaGenerica tab_cab, String observacion) {
        String p_tipo_transaccion = utilitario.getVariable("p_cxp_tipo_trans_reversa_menos");
        Tabla tab_cab_tran = new Tabla();
        tab_cab_tran.setTabla("cxp_cabece_transa", "ide_cpctr", 0);
        tab_cab_tran.setCondicion("ide_cpctr=-1");
        tab_cab_tran.ejecutarSql();
        tab_cab_tran.insertar();
        tab_cab_tran.setValor("ide_cpttr", p_tipo_transaccion);
        tab_cab_tran.setValor("ide_geper", tab_cab.getValor(0, "ide_geper"));
        tab_cab_tran.setValor("fecha_trans_cpctr", tab_cab.getValor(0, "fecha_trans_cpctr"));
        tab_cab_tran.setValor("observacion_cpctr", observacion);
        tab_cab_tran.setValor("ide_cpcfa", tab_cab.getValor(0, "ide_cpcfa"));
        tab_cab_tran.guardar();
        //Detalles

        TablaGenerica tab_det = utilitario.consultar("SELECT * from cxp_detall_transa where ide_cpctr=" + tab_cab.getValor("ide_cpctr"));
        Tabla tab_det_tran = new Tabla();
        tab_det_tran.setTabla("cxp_detall_transa", "ide_cpdtr", 0);
        tab_det_tran.setCondicion("ide_cpctr=-1");
        tab_det_tran.ejecutarSql();
        for (int i = 0; i < tab_det.getTotalFilas(); i++) {
            tab_det_tran.insertar();
            tab_det_tran.setValor("ide_cpctr", tab_cab_tran.getValor("ide_cpctr"));
            tab_det_tran.setValor("ide_cpcfa", tab_det.getValor(i, "ide_cpcfa"));
            tab_det_tran.setValor("ide_cnccc", ide_cnccc);
            tab_det_tran.setValor("ide_teclb", tab_det.getValor(i, "ide_teclb"));
            tab_det_tran.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_det_tran.setValor("ide_cpttr", p_tipo_transaccion);
            tab_det_tran.setValor("fecha_trans_cpdtr", utilitario.getFechaActual());
            tab_det_tran.setValor("fecha_venci_cpdtr", utilitario.getFechaActual());
            tab_det_tran.setValor("numero_pago_cpdtr", tab_det.getValor(i, "numero_pago_cpdtr"));
            tab_det_tran.setValor("valor_cpdtr", tab_det.getValor(i, "valor_cpdtr"));
            tab_det_tran.setValor("docum_relac_cpdtr", tab_det.getValor(i, "docum_relac_cpdtr"));
            tab_det_tran.setValor("observacion_cpdtr", tab_det.getValor(i, "observacion_cpdtr"));
        }
        tab_det_tran.guardar();
        System.out.println("reversa memos cxp generado");
    }

    public String getSignoTransaccion(String ide_ccttr) {
        TablaGenerica tab_tipo_transacciones = utilitario.consultar("select *from cxp_tipo_transacc where ide_cpttr=" + ide_ccttr);
        if (tab_tipo_transacciones.getTotalFilas() > 0) {
            if (tab_tipo_transacciones.getValor(0, "signo_cpttr") != null && !tab_tipo_transacciones.getValor(0, "signo_cpttr").isEmpty()) {
                return tab_tipo_transacciones.getValor(0, "signo_cpttr");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String buscarMaximoPago(String ide_cpctr) {
        //RETORNA EL PAGO MAXIMO 
        System.out.println("select max(numero_pago_ccdtr) from cxp_detall_transa where ide_cpctr=" + ide_cpctr);
        List lis_sql = utilitario.getConexion().consultar("select max(numero_pago_cpdtr) from cxp_detall_transa where ide_cpctr=" + ide_cpctr);
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

    public Tabla getTab_cab_tran_cxp(String ide_cpctr) {
        tab_cab_tran_cxp.setTabla("cxp_cabece_transa", "ide_cpctr", -1);
        tab_cab_tran_cxp.setCondicion("ide_cpctr=" + ide_cpctr);
        tab_cab_tran_cxp.ejecutarSql();
        return tab_cab_tran_cxp;
    }

    public Tabla getTab_cab_tran_cxp() {
        return tab_cab_tran_cxp;
    }

    public void setTab_cab_tran_cxp(Tabla tab_cab_tran_cxp) {
        this.tab_cab_tran_cxp = tab_cab_tran_cxp;
    }

    public Tabla getTab_det_tran_cxp() {
        return tab_det_tran_cxp;
    }

    public void setTab_det_tran_cxp(Tabla tab_det_tran_cxp) {
        this.tab_det_tran_cxp = tab_det_tran_cxp;
    }
}
