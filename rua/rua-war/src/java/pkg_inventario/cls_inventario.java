/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author byron
 */
public class cls_inventario {

    private Utilitario utilitario = new Utilitario();
    private String p_tipo_transaccion;
    private String p_bienes = utilitario.getVariable("p_inv_articulo_bien");

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

    public boolean aplicaIva(String ide_inarti) {
        // 1 si aplica
        // -1 no aplica
        // 0 no objeto
        TablaGenerica tab_articulo = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_inarti);
        if (tab_articulo.getTotalFilas() > 0) {
            if (tab_articulo.getValor(0, "iva_inarti") != null) {
                if (tab_articulo.getValor(0, "iva_inarti").equals("1")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean es_bien(String ide_inarti) {
        String art = obtener_tipo_articulo(ide_inarti);
        if (art.equals(p_bienes)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean esTipoCombo(String ide_inarti) {
        List lis_articulo = utilitario.getConexion().consultar("select es_combo_inarti from inv_articulo where ide_inarti=" + ide_inarti);
        if (lis_articulo.size() > 0) {
            if (lis_articulo.get(0) != null) {
                if (lis_articulo.get(0).toString().equalsIgnoreCase("true")) {
                    return true;
                }
            }
        }
        return false;
    }

    public String cantidad_disponible(String astr_bodega, String astr_producto, String astr_fecha_corte) {
        if (!astr_bodega.isEmpty() && !astr_producto.isEmpty()) {
            TablaGenerica ltab_saldo = utilitario.consultar("select inv_articulo.ide_inarti,inv_articulo.nombre_inarti,sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) as saldo "
                    + "from inv_cab_comp_inve, inv_det_comp_inve, inv_tip_tran_inve,inv_tip_comp_inve,inv_articulo "
                    + "where inv_det_comp_inve.ide_inarti = inv_articulo.ide_inarti "
                    + "and inv_cab_comp_inve.ide_inepi=1 "
                    + "and inv_tip_tran_inve.ide_intti= inv_cab_comp_inve.ide_intti "
                    + "and inv_tip_comp_inve.ide_intci= inv_tip_tran_inve.ide_intci "
                    + "and inv_cab_comp_inve.ide_incci=inv_det_comp_inve.ide_incci "
                    + "and inv_cab_comp_inve.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                    + "and inv_articulo.ide_inarti in (" + astr_producto + ") "
                    + "and inv_cab_comp_inve.ide_inbod in (" + astr_bodega + ") "
                    + "and inv_cab_comp_inve.fecha_trans_incci <= '" + astr_fecha_corte + "' "
                    + "group by inv_articulo.ide_inarti");
            if (ltab_saldo.getTotalFilas() > 0) {
                return ltab_saldo.getValor("saldo");
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String obtener_saldo(String astr_bodega, String astr_producto, String astr_fecha_corte) {
        String ltab_saldo = "";
        if (!astr_bodega.isEmpty() && !astr_producto.isEmpty()) {
            try {
                ltab_saldo = "select inv_articulo.ide_inarti,inv_articulo.nombre_inarti,sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) as saldo "
                        + "from inv_cab_comp_inve, inv_det_comp_inve, inv_tip_tran_inve,inv_tip_comp_inve,inv_articulo "
                        + "where inv_det_comp_inve.ide_inarti = inv_articulo.ide_inarti "
                        + "and inv_cab_comp_inve.ide_inepi=1 "
                        + "and inv_tip_tran_inve.ide_intti= inv_cab_comp_inve.ide_intti "
                        + "and inv_tip_comp_inve.ide_intci= inv_tip_tran_inve.ide_intci "
                        + "and inv_cab_comp_inve.ide_incci=inv_det_comp_inve.ide_incci "
                        + "and inv_cab_comp_inve.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "and inv_articulo.ide_inarti in (" + astr_producto + ") "
                        + "and inv_cab_comp_inve.ide_inbod in (" + astr_bodega + ") "
                        + "and inv_cab_comp_inve.fecha_trans_incci <= '" + astr_fecha_corte + "' "
                        + "group by inv_articulo.ide_inarti";

            } catch (Exception ex) {
                System.out.println("No se hizo la consulta de saldos en la clase de negocio. " + ex.toString());
            }

        }
        return ltab_saldo;
    }

    public String obtener_signo_transaccion(String astr_codigo_transaccion) {
        Tabla ltab_tipo_transaccion = new Tabla();
        ltab_tipo_transaccion.setSql("SELECT ide_intci,signo_intci from inv_tip_comp_inve where ide_intci = "
                + "(select ide_intci from inv_tip_tran_inve where ide_intti =" + astr_codigo_transaccion + ") "
                + "and ide_empr=" + utilitario.getVariable("ide_empr"));
        ltab_tipo_transaccion.ejecutarSql();

        return ltab_tipo_transaccion.getValor("signo_intci");
    }

    public void reversar_mas(String nuevo_ide_cnccc, String ide_cnccc, String observacion) {
        if (ide_cnccc != null) {
            p_tipo_transaccion = utilitario.getVariable("p_inv_tipo_transaccion_reversa_mas");
            TablaGenerica tab_cab = utilitario.consultar("SELECT * FROM inv_cab_comp_inve where ide_cnccc=" + ide_cnccc);
            Tabla tab_cab_comp_inv = new Tabla();
            tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
            tab_cab_comp_inv.setCondicion("ide_incci=-1");
            tab_cab_comp_inv.ejecutarSql();
            tab_cab_comp_inv.insertar();
            tab_cab_comp_inv.setValor("ide_geper", tab_cab.getValor("ide_geper"));
            tab_cab_comp_inv.setValor("ide_inepi", utilitario.getVariable("p_inv_estado_normal"));  /////variable estado normal de inventario
            tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_cab_comp_inv.setValor("ide_inbod", "1");   ///variable para bodega por defecto
            tab_cab_comp_inv.setValor("numero_incci", generarSecuencialComprobanteInventario(p_tipo_transaccion));  /// calcular numero de comprobante de inventario
            tab_cab_comp_inv.setValor("fecha_trans_incci", utilitario.getFechaActual());
            if (observacion != null) {
                tab_cab_comp_inv.setValor("observacion_incci", "reversa (comprobante num:" + tab_cab.getValor("numero_incci") + "), transaccion num:" + tab_cab.getValor("ide_incci") + ", " + observacion);
            } else {
                tab_cab_comp_inv.setValor("observacion_incci", "reversa (comprobante num:" + tab_cab.getValor("numero_incci") + "), transaccion num:" + tab_cab.getValor("ide_incci"));
            }
            tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
            tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
            tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion);   ////variable titpo transaccion compra 
            tab_cab_comp_inv.setValor("ide_cnccc", nuevo_ide_cnccc);
            //Detalles

            TablaGenerica tab_det = utilitario.consultar("SELECT * FROM inv_det_comp_inve where ide_incci=" + tab_cab.getValor("ide_incci"));
            Tabla tab_det_comp_inv = new Tabla();
            tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
            tab_det_comp_inv.setCondicion("ide_indci=-1");
            tab_det_comp_inv.ejecutarSql();
            tab_cab_comp_inv.guardar();
            for (int i = 0; i < tab_det.getTotalFilas(); i++) {
                tab_det_comp_inv.insertar();
                tab_det_comp_inv.setValor("ide_inarti", tab_det.getValor(i, "ide_inarti"));
                tab_det_comp_inv.setValor("ide_cpcfa", tab_det.getValor(i, "ide_cpcfa"));
                tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));
                tab_det_comp_inv.setValor("cantidad_indci", tab_det.getValor(i, "cantidad_indci"));
                tab_det_comp_inv.setValor("precio_indci", tab_det.getValor(i, "precio_indci"));
                tab_det_comp_inv.setValor("valor_indci", tab_det.getValor(i, "valor_indci"));
                tab_det_comp_inv.setValor("observacion_indci", tab_det.getValor(i, "observacion_indci"));
                tab_det_comp_inv.setValor("precio_promedio_indci", tab_det.getValor(i, "precio_promedio_indci"));
            }
            tab_det_comp_inv.guardar();
            System.out.println("reversa menos inventario generado");
        }
    }

    public void reversar_menos(String nuevo_ide_cnccc, String ide_cnccc, String observacion) {
        if (ide_cnccc != null) {
            p_tipo_transaccion = utilitario.getVariable("p_inv_tipo_transaccion_reversa_menos");
            TablaGenerica tab_cab = utilitario.consultar("SELECT * FROM inv_cab_comp_inve where ide_cnccc=" + ide_cnccc);
            Tabla tab_cab_comp_inv = new Tabla();
            tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
            tab_cab_comp_inv.setCondicion("ide_incci=-1");
            tab_cab_comp_inv.ejecutarSql();
            tab_cab_comp_inv.insertar();
            tab_cab_comp_inv.setValor("ide_geper", tab_cab.getValor("ide_geper"));
            tab_cab_comp_inv.setValor("ide_inepi", utilitario.getVariable("p_inv_estado_normal"));  /////variable estado normal de inventario
            tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            tab_cab_comp_inv.setValor("ide_inbod", "1");   ///variable para bodega por defecto
            tab_cab_comp_inv.setValor("numero_incci", generarSecuencialComprobanteInventario(p_tipo_transaccion));  /// calcular numero de comprobante de inventario
            tab_cab_comp_inv.setValor("fecha_trans_incci", utilitario.getFechaActual());
            if (observacion != null) {
                tab_cab_comp_inv.setValor("observacion_incci", "reversa (comprobante num:" + tab_cab.getValor("numero_incci") + "), transaccion num:" + tab_cab.getValor("ide_incci") + ", " + observacion);
            } else {
                tab_cab_comp_inv.setValor("observacion_incci", "reversa (comprobante num:" + tab_cab.getValor("numero_incci") + "), transaccion num:" + tab_cab.getValor("ide_incci"));
            }
            tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
            tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
            tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion);   ////variable titpo transaccion compra 
            tab_cab_comp_inv.setValor("ide_cnccc", nuevo_ide_cnccc);
            //Detalles

            TablaGenerica tab_det = utilitario.consultar("SELECT * FROM inv_det_comp_inve where ide_incci=" + tab_cab.getValor("ide_incci"));

            Tabla tab_det_comp_inv = new Tabla();
            tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
            tab_det_comp_inv.setCondicion("ide_indci=-1");
            tab_det_comp_inv.ejecutarSql();
            tab_cab_comp_inv.guardar();

            for (int i = 0; i < tab_det.getTotalFilas(); i++) {
                tab_det_comp_inv.insertar();
                tab_det_comp_inv.setValor("ide_inarti", tab_det.getValor(i, "ide_inarti"));
                tab_det_comp_inv.setValor("ide_cpcfa", tab_det.getValor(i, "ide_cpcfa"));
                tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));
                tab_det_comp_inv.setValor("cantidad_indci", tab_det.getValor(i, "cantidad_indci"));
                tab_det_comp_inv.setValor("precio_indci", tab_det.getValor(i, "precio_indci"));
                tab_det_comp_inv.setValor("valor_indci", tab_det.getValor(i, "valor_indci"));
                tab_det_comp_inv.setValor("observacion_indci", tab_det.getValor(i, "observacion_indci"));
                tab_det_comp_inv.setValor("precio_promedio_indci", tab_det.getValor(i, "precio_promedio_indci"));
            }

            tab_det_comp_inv.guardar();
            System.out.println("reversa menos inventario generado " + tab_cab.getValor("ide_incci"));

        }

    }

//    public String obtener_valor_promedio_sql(String ide_inbod, String ide_inarti) {
//        String sql_valor_promedio = ("select inv_articulo.ide_inarti,inv_articulo.nombre_inarti, "
//                + "SUM(inv_det_comp_inve.precio_indci*inv_tip_comp_inve.signo_intci*inv_det_comp_inve.cantidad_indci) as valor , "
//                + "sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) as saldo, "
//                + "case when sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) is NULL then 0 "
//                + "else case when sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) =0 then 0 "
//                + "else SUM(inv_det_comp_inve.precio_indci*inv_tip_comp_inve.signo_intci*inv_det_comp_inve.cantidad_indci) "
//                + "/ sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci)END END as valor_promedio "
//                + "from inv_cab_comp_inve, inv_det_comp_inve, inv_tip_tran_inve,inv_tip_comp_inve,inv_articulo "
//                + "where inv_det_comp_inve.ide_inarti = inv_articulo.ide_inarti "
//                + "and inv_cab_comp_inve.ide_inepi=1 "
//                + "and inv_tip_tran_inve.ide_intti= inv_cab_comp_inve.ide_intti "
//                + "and inv_tip_comp_inve.ide_intci= inv_tip_tran_inve.ide_intci "
//                + "and inv_cab_comp_inve.ide_incci=inv_det_comp_inve.ide_incci "
//                + "and inv_cab_comp_inve.ide_empr=" + utilitario.getVariable("ide_empr") + " "
//                + "and inv_articulo.ide_inarti in (" + ide_inarti + ") "
//                + "and inv_cab_comp_inve.ide_inbod in (" + ide_inbod + ") "
//                + "and inv_cab_comp_inve.fecha_trans_incci <= '" + utilitario.getFechaActual() + "' "
//                + "group by inv_articulo.ide_inarti");
//        return sql_valor_promedio;
//    }
    public String getValordeVenta(String ide_inarti) {
        TablaGenerica tab_deta_factura = utilitario.consultar("select * from cxc_deta_factura "
                + "where ide_inarti=" + ide_inarti + " "
                + "and ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "order by ide_ccdfa DESC ");
        if (tab_deta_factura.getTotalFilas() > 0) {
            return tab_deta_factura.getValor(0, "precio_ccdfa");
        } else {
            return null;
        }

    }

    public String obtener_valor_promedio(String ide_inbod, String ide_inarti) {
        TablaGenerica tab_valor_pro = utilitario.consultar("select inv_articulo.ide_inarti,inv_articulo.nombre_inarti, "
                + "SUM(inv_det_comp_inve.precio_indci*inv_tip_comp_inve.signo_intci*inv_det_comp_inve.cantidad_indci) as valor , "
                + "sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) as saldo, "
                + "case when sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) is NULL then 0 "
                + "else case when sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci) =0 then 0 "
                + "else SUM(inv_det_comp_inve.precio_indci*inv_tip_comp_inve.signo_intci*inv_det_comp_inve.cantidad_indci) "
                + "/ sum (inv_det_comp_inve.cantidad_indci * inv_tip_comp_inve.signo_intci)END END as valor_promedio "
                + "from inv_cab_comp_inve, inv_det_comp_inve, inv_tip_tran_inve,inv_tip_comp_inve,inv_articulo "
                + "where inv_det_comp_inve.ide_inarti = inv_articulo.ide_inarti "
                + "and inv_cab_comp_inve.ide_inepi=1 "
                + "and inv_tip_tran_inve.ide_intti= inv_cab_comp_inve.ide_intti "
                + "and inv_tip_comp_inve.ide_intci= inv_tip_tran_inve.ide_intci "
                + "and inv_cab_comp_inve.ide_incci=inv_det_comp_inve.ide_incci "
                + "and inv_cab_comp_inve.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "and inv_articulo.ide_inarti in (" + ide_inarti + ") "
                + "and inv_cab_comp_inve.ide_inbod in (" + ide_inbod + ") "
                + "and inv_tip_comp_inve.signo_intci=1 "// signo positivo ingreso
                + "and inv_cab_comp_inve.fecha_trans_incci <= '" + utilitario.getFechaActual() + "' "
                + "group by inv_articulo.ide_inarti");
        if (tab_valor_pro.getTotalFilas() > 0) {
            return tab_valor_pro.getValor("valor_promedio");
        } else {
            return "";
        }
    }

    public String generarSecuencialComprobanteInventario(String tipo_tran_inv) {
        String signo = obtener_signo_transaccion(tipo_tran_inv);
        List list = utilitario.getConexion().consultar("SELECT max(NUMERO_INCCI) FROM inv_cab_comp_inve,inv_tip_tran_inve,inv_tip_comp_inve "
                + "WHERE inv_cab_comp_inve.ide_intti=inv_tip_tran_inve.ide_intti "
                + "AND inv_tip_tran_inve.ide_intci=inv_tip_comp_inve.ide_intci "
                + "AND inv_tip_comp_inve.signo_intci=" + signo + " "
                + "and inv_cab_comp_inve.ide_sucu=" + utilitario.getVariable("ide_sucu") + ""
                + "AND inv_cab_comp_inve.ide_empr=" + utilitario.getVariable("ide_empr"));
        String str_maximo = "0";
        if (list != null && !list.isEmpty()) {
            if (list.get(0) != null) {
                if (!list.get(0).toString().isEmpty()) {
                    str_maximo = list.get(0) + "";
                }
            }
        }
        String num_max = String.valueOf(Integer.parseInt(str_maximo) + 1);
        String ceros = utilitario.generarCero(10 - num_max.length());
        str_maximo = ceros.concat(num_max);

        return str_maximo;
    }

    public String getParametroTablaTipoTransaccion(String ide_intti, String parametro) {
        if (ide_intti != null && !ide_intti.isEmpty()) {
            if (parametro != null && !parametro.isEmpty()) {
                TablaGenerica tab_inv_tip_tran = utilitario.consultar("SELECT * FROM inv_tip_tran_inve WHERE ide_intti=" + ide_intti);
                if (tab_inv_tip_tran.getTotalFilas() > 0) {
                    if (tab_inv_tip_tran.getValor(0, parametro) != null && !tab_inv_tip_tran.getValor(0, parametro).isEmpty()) {
                        return tab_inv_tip_tran.getValor(0, parametro);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public String getParametroTablaTipoComprobante(String ide_intci, String parametro) {
        if (ide_intci != null && !ide_intci.isEmpty()) {
            if (parametro != null && !parametro.isEmpty()) {
                TablaGenerica tab_inv_tip_compro = utilitario.consultar("SELECT * FROM inv_tip_comp_inve WHERE ide_intci=" + ide_intci);
                if (tab_inv_tip_compro.getTotalFilas() > 0) {
                    if (tab_inv_tip_compro.getValor(0, parametro) != null && !tab_inv_tip_compro.getValor(0, parametro).isEmpty()) {
                        return tab_inv_tip_compro.getValor(0, parametro);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public String getPrecioPromedioTransaccionNegativa(String ide_inarti, String ide_inbod) {
        if (ide_inarti != null && !ide_inarti.isEmpty()) {
            if (ide_inbod != null && !ide_inbod.isEmpty()) {
                TablaGenerica tab_promedio_ultimo = utilitario.consultar("select ide_indci,precio_promedio_indci from inv_det_comp_inve dci "
                        + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                        + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                        + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                        + "where dci.ide_inarti=" + ide_inarti + " "
                        + "and cci.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "and cci.ide_inbod=" + ide_inbod + " "
                        + "order by dci.ide_indci DESC");
                if (tab_promedio_ultimo.getTotalFilas() > 0) {
                    if (tab_promedio_ultimo.getValor(0, "precio_promedio_indci") != null && !tab_promedio_ultimo.getValor(0, "precio_promedio_indci").isEmpty()) {
                        return tab_promedio_ultimo.getValor(0, "precio_promedio_indci");
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getSaldoValorArticulo(String ide_inarti, String ide_inbod) {
        if (ide_inarti != null && !ide_inarti.isEmpty()) {
            if (ide_inbod != null && !ide_inbod.isEmpty()) {
                List lis_saldo_valor = utilitario.getConexion().consultar("select sum (valor_indci * signo_intci) as saldo_total_valor from inv_det_comp_inve dci "
                        + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                        + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                        + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                        + "where dci.ide_inarti=" + ide_inarti + " "
                        + "and cci.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "and cci.ide_inbod=" + ide_inbod + " ");
                if (lis_saldo_valor.size() > 0) {
                    if (lis_saldo_valor.get(0) != null && !lis_saldo_valor.get(0).toString().isEmpty()) {
                        return "" + lis_saldo_valor.get(0);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getSaldoCantidadArticulo(String ide_inarti, String ide_inbod) {

        if (ide_inarti != null && !ide_inarti.isEmpty()) {
            if (ide_inbod != null && !ide_inbod.isEmpty()) {
                List lis_saldo_cantidad = utilitario.getConexion().consultar("select sum (cantidad_indci * signo_intci) as saldo_cantidad from inv_det_comp_inve dci "
                        + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                        + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                        + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                        + "where dci.ide_inarti=" + ide_inarti + " "
                        + "and cci.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                        + "and cci.ide_inbod=" + ide_inbod + "");
                if (lis_saldo_cantidad.size() > 0) {
                    if (lis_saldo_cantidad.get(0) != null && !lis_saldo_cantidad.get(0).toString().isEmpty()) {
                        return "" + lis_saldo_cantidad.get(0);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String getPrecioPromedioTransaccionPositiva(String ide_inarti, String ide_inbod, double valor_transaccion, double cantidad_transaccion) {
        if (ide_inarti != null && !ide_inarti.isEmpty()) {
            if (ide_inbod != null && !ide_inbod.isEmpty()) {
                double saldo_valor_anterior;
                double saldo_valor_actual = 0;
                double saldo_cant_anterior;
                double saldo_cant_actual = 0;
                String precio_promedio = "";
                try {
                    if (getSaldoValorArticulo(ide_inarti, ide_inbod) != null) {
                        saldo_valor_anterior = Double.parseDouble(getSaldoValorArticulo(ide_inarti, ide_inbod));
                        saldo_valor_actual = saldo_valor_anterior + valor_transaccion;
                    } else {
                        saldo_valor_anterior = 0;
                        saldo_valor_actual = saldo_valor_anterior + valor_transaccion;
                    }
                    System.out.println("saldo valor actual " + saldo_valor_actual);
                    if (getSaldoCantidadArticulo(ide_inarti, ide_inbod) != null) {
                        saldo_cant_anterior = Double.parseDouble(getSaldoCantidadArticulo(ide_inarti, ide_inbod));
                        saldo_cant_actual = saldo_cant_anterior + cantidad_transaccion;
                    } else {
                        saldo_cant_anterior = 0;
                        saldo_cant_actual = saldo_cant_anterior + cantidad_transaccion;
                    }
                    System.out.println("saldo cantidad actual " + saldo_cant_actual);

                } catch (Exception e) {

                    return null;
                }
                if (saldo_cant_actual != 0) {
                    double precio_promed = saldo_valor_actual / saldo_cant_actual;
                    precio_promedio = String.valueOf(precio_promed);
                }
                System.out.println("precio promedio " + precio_promedio);
                return precio_promedio;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
