/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.contabilidad.ServicioConfiguracion;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ServicioProducto {

    private final Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioConfiguracion ser_configuracion;

    /**
     * Retorna los datos de un Producto
     *
     * @param ide_inarti Producto
     * @return
     */
    public TablaGenerica getProducto(String ide_inarti) {
        return utilitario.consultar("SELECT * from inv_articulo where ide_inarti=" + ide_inarti + "");
    }

    /**
     * Retorna el valor que identifica la configuracion de iva para un Producto
     *
     * @param ide_inarti
     * @return "" No existe el producto; 1 SI IVA; -1 NO IVA; 0 NO OBJETO;
     */
    public String getIvaProducto(String ide_inarti) {
        String iva = "";
        TablaGenerica tab_producto = getProducto(ide_inarti);
        if (tab_producto.isEmpty() == false) {
            iva = tab_producto.getValor("iva_inarti");
        }
        return iva;
    }

    /**
     * Retorna la sentencia SQL para obtener los Productos de una empresa, para
     * ser utilizada en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlProductosCombo() {
        return "SELECT ide_inarti,nombre_inarti from inv_articulo arti "
                + "where arti.ide_empr=" + utilitario.getVariable("ide_empr") + " and nivel_inarti='HIJO' ORDER BY nombre_inarti ";
    }

    public String getSqlProductosKardexCombo() {
        return "SELECT ide_inarti,nombre_inarti from inv_articulo arti "
                + "where arti.ide_empr=" + utilitario.getVariable("ide_empr") + " and nivel_inarti='HIJO' and hace_kardex_inarti=true ORDER BY nombre_inarti ";
    }

    /**
     * Retorna la sentencia SQL para obtener las Bodegas de una empresa, para
     * ser utilizada en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlBodegasCombo() {
        return "select ide_inbod,nombre_inbod from inv_bodega where nivel_inbod='HIJO' and ide_empr=" + utilitario.getVariable("ide_empr");
    }

    /**
     * Asigna las configuraciones de un Producto
     *
     * @param tabla
     */
    public void configurarTablaProducto(Tabla tabla) {
        tabla.setTabla("inv_articulo", "ide_inarti", -1);
        tabla.getColumna("nivel_inarti").setCombo(utilitario.getListaNiveles());
        tabla.getColumna("ide_infab").setCombo("inv_fabricante", "ide_infab", "nombre_infab", "");
        tabla.getColumna("ide_inmar").setCombo("inv_marca", "ide_inmar", "nombre_invmar", "");
        tabla.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tabla.getColumna("ide_intpr").setCombo("inv_tipo_producto", "ide_intpr", "nombre_intpr", "");
        tabla.getColumna("ide_intpr").setRequerida(true);
        tabla.getColumna("ide_inepr").setCombo("inv_estado_produc", "ide_inepr", "nombre_inepr", "");
        tabla.getColumna("nivel_inarti").setValorDefecto("HIJO");
        tabla.getColumna("hace_kardex_inarti").setValorDefecto("true");
        tabla.getColumna("es_combo_inarti").setValorDefecto("false");
        tabla.getColumna("nombre_inarti").setRequerida(true);

        tabla.getColumna("nombre_inarti").setRequerida(true);

        tabla.getColumna("iva_inarti").setRadio(getListaTipoIVA(), "1");
        tabla.getColumna("iva_inarti").setRadioVertical(true);
        tabla.getColumna("INV_IDE_INARTI").setVisible(true);
        tabla.getColumna("INV_IDE_INARTI").setCombo("select ide_inarti,nombre_inarti from inv_articulo where nivel_inarti ='PADRE' order by nombre_inarti");
        tabla.setTipoFormulario(true);
        tabla.getGrid().setColumns(4);
        tabla.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");// cargar un combo de una con el ide, nombre
    }

    /**
     * Lista con los tipos de Iva que pueden tener un articulo
     *
     * @return
     */
    public List getListaTipoIVA() {
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
        return lista;
    }

    /**
     * Retorna la sentencia SQL para obtener las transacciones de un Producto en
     * una Bodega x Sucursal
     *
     * @param ide_inarti Producto
     * @param ide_inbod Bodega
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlTransaccionesProductoBodega(String ide_inarti, String ide_inbod, String fechaInicio, String fechaFin) {
        return "SELECT dci.ide_indci,cci.fecha_trans_incci,tci.signo_intci,nombre_intti,"
                + "cantidad_indci,precio_indci,valor_indci,precio_promedio_indci,"
                + "case when signo_intci = 1 THEN valor_indci  end as INGRESOS,case when signo_intci = -1 THEN valor_indci end as EGRESOS,'' as saldo_cant,'' as saldo_valor "
                + "from inv_det_comp_inve dci "
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                + "left join inv_articulo arti on dci.ide_inarti=arti.ide_inarti "
                + "where dci.ide_inarti=" + ide_inarti + " "
                + "and dci.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "and fecha_trans_incci BETWEEN '" + fechaInicio + "'  and '" + fechaFin + "' "
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " "
                + "and ide_inbod=" + ide_inbod + " "
                + "ORDER BY cci.fecha_trans_incci,dci.ide_indci asc";
    }

    /**
     * Retorna la cantidad inicial de un Producto en una Bodega X SUCURSAL a una
     * fecha determinada
     *
     * @param ide_inarti Producto
     * @param ide_inbod Bodega
     * @param fecha
     * @return
     */
    public double getCantidadInicialProductoBodega(String ide_inarti, String ide_inbod, String fecha) {
        double saldo = 0;
        String sql = "select dci.ide_inarti,sum(cantidad_indci * signo_intci) as cantidad "
                + "from inv_det_comp_inve dci "
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                + "where dci.ide_inarti=" + ide_inarti + " "
                + "and fecha_trans_incci <'" + fecha + "' "
                + "and dci.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and ide_inbod=" + ide_inbod + " "
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " "
                + "group by dci.ide_inarti";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor(0, "cantidad") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor(0, "cantidad"));
                } catch (Exception e) {
                }
            }
        }
        return saldo;
    }

    public double getCantidadProductoBodega(String ide_inarti, String ide_inbod) {
        double saldo = 0;
        String sql = "select dci.ide_inarti,sum(cantidad_indci * signo_intci) as cantidad "
                + "from inv_det_comp_inve dci "
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                + "where dci.ide_inarti=" + ide_inarti + " "
                + "and dci.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "and ide_inbod=" + ide_inbod + " "
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " "
                + "group by dci.ide_inarti";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor(0, "cantidad") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor(0, "cantidad"));
                } catch (Exception e) {
                }
            }
        }
        return saldo;
    }

    /**
     * Retorna el precio promedio de un Producto
     *
     * @param ide_inarti
     * @param ide_inbod
     * @param fecha
     * @return
     */
    public List<Double> getSaldoPromedioProductoBodega(String ide_inarti, String fecha, String ide_inbod) {
        List<Double> resultado = new ArrayList();
        ide_inbod = ide_inbod == null ? "" : ide_inbod.trim();
        String strCondicionBodega = ide_inbod.isEmpty() ? "" : "and ide_inbod in (" + ide_inbod + ") \n";

        TablaGenerica tab_kardex = utilitario.consultar("SELECT dci.ide_indci,cci.fecha_trans_incci,nom_geper,nombre_intti,\n"
                + "case when signo_intci = 1 THEN cantidad_indci  end as CANT_INGRESO,\n"
                + "case when signo_intci = 1 THEN precio_indci  end as VUNI_INGRESO,\n"
                + "case when signo_intci = 1 THEN valor_indci  end as VTOT_INGRESO,\n"
                + "case when signo_intci = -1 THEN cantidad_indci  end as CANT_EGRESO,\n"
                + "case when signo_intci = -1 THEN precio_indci  end as VUNI_EGRESO,\n"
                + "case when signo_intci = -1 THEN valor_indci  end as VTOT_EGRESO,\n"
                + "'' as CANT_SALDO,precio_promedio_indci as VUNI_SALDO ,'' VTOT_SALDO\n"
                + "from inv_det_comp_inve dci \n"
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci \n"
                + "left join gen_persona gpe on cci.ide_geper=gpe.ide_geper\n"
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti \n"
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci \n"
                + "left join inv_articulo arti on dci.ide_inarti=arti.ide_inarti\n"
                + "where dci.ide_inarti=" + ide_inarti + " \n"
                + "and fecha_trans_incci <= '" + fecha + "' \n"
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " \n" //Comprobantes en estado  normal
                + strCondicionBodega
                + "ORDER BY cci.fecha_trans_incci asc,dci.ide_indci asc,signo_intci asc");
        //CALCULA PRECIO PROMEDIO 
        double dou_cantf = 0;
        double dou_preciof = 0;
        double dou_saldof = 0;
        if (!tab_kardex.isEmpty()) {
            for (int i = 0; i < tab_kardex.getTotalFilas(); i++) {
                double dou_cant_fila = 0;
                double dou_precio_fila = 0;
                double dou_saldo_fila = 0;

                if (tab_kardex.getValor(i, "VTOT_INGRESO") != null && tab_kardex.getValor(i, "VTOT_INGRESO").isEmpty() == false) {
                    try {
                        dou_cant_fila = Double.parseDouble(tab_kardex.getValor(i, "CANT_INGRESO"));
                        dou_precio_fila = Double.parseDouble(tab_kardex.getValor(i, "VUNI_INGRESO"));
                        dou_saldo_fila = Double.parseDouble(tab_kardex.getValor(i, "VTOT_INGRESO"));
                    } catch (Exception e) {
                    }
                    if (i == 0) {
                        dou_cantf += dou_cant_fila;
                        dou_preciof = dou_precio_fila;
                        dou_saldof = dou_cant_fila * dou_precio_fila;
                    } else {
                        dou_cantf += dou_cant_fila;
                        dou_saldof += dou_saldo_fila;
                        dou_preciof = dou_saldof / dou_cantf;
                    }
                } else if (tab_kardex.getValor(i, "VTOT_EGRESO") != null && tab_kardex.getValor(i, "VTOT_EGRESO").isEmpty() == false) {
                    try {
                        dou_cant_fila = Double.parseDouble(tab_kardex.getValor(i, "CANT_EGRESO"));
                        dou_precio_fila = Double.parseDouble(tab_kardex.getValor(i, "VUNI_EGRESO"));
                        dou_saldo_fila = Double.parseDouble(tab_kardex.getValor(i, "VTOT_EGRESO"));
                    } catch (Exception e) {
                    }
                    if (i == 0) {
                        dou_cantf -= dou_cant_fila;
                        dou_preciof = dou_precio_fila;
                        dou_saldof = dou_cant_fila * dou_precio_fila;
                    } else {
                        dou_cantf -= dou_cant_fila;
                        dou_saldof -= dou_saldo_fila;
                        dou_preciof = dou_saldof / dou_cantf;
                    }
                }
            }
        }
        resultado.add(dou_cantf);//CANT INICIA
        resultado.add(dou_preciof);//PRECIO UNIT. INICIA
        resultado.add(dou_saldof);//SALDO INICIA
        return resultado;
    }

    /**
     * Retona el último precio de un Producto que compro un cliente
     *
     * @param ide_geper
     * @param ide_inarti
     * @return
     */
    public double getUltimoPrecioProductoCliente(String ide_geper, String ide_inarti) {
        double precio = 0.00;
        TablaGenerica tab_precio = utilitario.consultar("select ide_inarti,precio_ccdfa  from cxc_deta_factura a "
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa "
                + "where ide_geper=" + ide_geper + " "
                + "AND  ide_inarti=" + ide_inarti + " "
                + "order by fecha_trans_cccfa desc limit 1");
        if (tab_precio.isEmpty() == false) {
            if (tab_precio.getValor(0, "precio_ccdfa") != null) {
                try {
                    precio = Double.parseDouble(tab_precio.getValor(0, "precio_ccdfa"));
                } catch (Exception e) {
                }
            }
        }
        return precio;
    }

    /**
     * Retorna el ultimo valor de configuracion de la compra del Producto de un
     * Cliente
     *
     * @param ide_geper
     * @param ide_inarti
     * @return '' No existe configuracion; 1 SI IVA; -1 NO IVA; 0 NO OBJETO;
     */
    public String getUltimoIvaProductoCliente(String ide_geper, String ide_inarti) {
        String iva = "";
        TablaGenerica tab_precio = utilitario.consultar("select ide_inarti,iva_inarti_ccdfa  from cxc_deta_factura a "
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa "
                + "where ide_geper=" + ide_geper + " "
                + "AND  ide_inarti=" + ide_inarti + " "
                + "AND iva_inarti_ccdfa is not null "
                + "order by fecha_trans_cccfa desc limit 1");
        if (tab_precio.isEmpty() == false) {
            if (tab_precio.getValor(0, "iva_inarti_ccdfa") != null) {
                iva = tab_precio.getValor(0, "iva_inarti_ccdfa");

            }
        }
        return iva;
    }

    /**
     * Obtiene el tipo de producto recursivamente
     *
     * @param ide_arti
     * @return
     */
    public String getTipoProducto(String ide_arti) {
        String ide_art = ide_arti;
        TablaGenerica inv_ide_arti = getProducto(ide_arti);
        if (inv_ide_arti.getTotalFilas() > 0) {
            do {
                ide_art = inv_ide_arti.getValor(0, "inv_ide_inarti");
                inv_ide_arti = utilitario.consultar("select * from inv_articulo where ide_inarti=" + ide_art);
            } while (inv_ide_arti.getValor(0, "inv_ide_inarti") != null && !inv_ide_arti.getValor(0, "inv_ide_inarti").isEmpty());
        }
        return ide_art;
    }

    /**
     * Retorna si un Producto es un Bien
     *
     * @param ide_inarti
     * @return
     */
    public boolean isBien(String ide_inarti) {
        // Parametro que identifica a un 
        String p_inv_articulo_bien = utilitario.getVariable("p_inv_articulo_bien");
        String art = getTipoProducto(ide_inarti);
        return art.equals(p_inv_articulo_bien);
    }

    /**
     * Retorna Sql para obtener el kardex de un producto, con rango de fechas y
     * por bodega(s)
     *
     * @param ide_inarti
     * @param fecha_inicio
     * @param fecha_fin
     * @param ide_inbod null o vacio no filtra por bodegas
     * @return
     */
    public String getSqlKardex(String ide_inarti, String fecha_inicio, String fecha_fin, String ide_inbod) {
        ide_inbod = ide_inbod == null ? "" : ide_inbod.trim();

        String strCondicionBodega = ide_inbod.isEmpty() ? "" : " ide_inbod in (" + ide_inbod + ") \n";
        return "SELECT dci.ide_indci,cci.fecha_trans_incci,nom_geper,nombre_intti,\n"
                + "case when signo_intci = 1 THEN cantidad_indci  end as CANT_INGRESO,\n"
                + "case when signo_intci = 1 THEN precio_indci  end as VUNI_INGRESO,\n"
                + "case when signo_intci = 1 THEN valor_indci  end as VTOT_INGRESO,\n"
                + "case when signo_intci = -1 THEN cantidad_indci  end as CANT_EGRESO,\n"
                + "case when signo_intci = -1 THEN precio_indci  end as VUNI_EGRESO,\n"
                + "case when signo_intci = -1 THEN valor_indci  end as VTOT_EGRESO,\n"
                + "'' as CANT_SALDO,precio_promedio_indci as VUNI_SALDO ,'' VTOT_SALDO\n"
                + "from inv_det_comp_inve dci \n"
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci \n"
                + "left join gen_persona gpe on cci.ide_geper=gpe.ide_geper\n"
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti \n"
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci \n"
                + "left join inv_articulo arti on dci.ide_inarti=arti.ide_inarti\n"
                + "where dci.ide_inarti=" + ide_inarti + " \n"
                + "and fecha_trans_incci BETWEEN '" + fecha_inicio + "'  and '" + fecha_fin + "' \n"
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " \n" //Comprobantes en estado  normal
                + strCondicionBodega
                + "ORDER BY cci.fecha_trans_incci asc,dci.ide_indci asc,signo_intci asc";
    }

    public List<Double> getSaldosInicialesKardex(String ide_inarti, String fecha_fin, String ide_inbod) {
        List<Double> resultado = new ArrayList();
        ide_inbod = ide_inbod == null ? "" : ide_inbod.trim();
        String strCondicionBodega = ide_inbod.isEmpty() ? "" : "and ide_inbod in (" + ide_inbod + ") \n";

        TablaGenerica tab_kardex = utilitario.consultar("SELECT dci.ide_indci,cci.fecha_trans_incci,nom_geper,nombre_intti,\n"
                + "case when signo_intci = 1 THEN cantidad_indci  end as CANT_INGRESO,\n"
                + "case when signo_intci = 1 THEN precio_indci  end as VUNI_INGRESO,\n"
                + "case when signo_intci = 1 THEN valor_indci  end as VTOT_INGRESO,\n"
                + "case when signo_intci = -1 THEN cantidad_indci  end as CANT_EGRESO,\n"
                + "case when signo_intci = -1 THEN precio_indci  end as VUNI_EGRESO,\n"
                + "case when signo_intci = -1 THEN valor_indci  end as VTOT_EGRESO,\n"
                + "'' as CANT_SALDO,precio_promedio_indci as VUNI_SALDO ,'' VTOT_SALDO\n"
                + "from inv_det_comp_inve dci \n"
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci \n"
                + "left join gen_persona gpe on cci.ide_geper=gpe.ide_geper\n"
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti \n"
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci \n"
                + "left join inv_articulo arti on dci.ide_inarti=arti.ide_inarti\n"
                + "where dci.ide_inarti=" + ide_inarti + " \n"
                + "and fecha_trans_incci < '" + fecha_fin + "' \n"
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " \n" //Comprobantes en estado  normal
                + strCondicionBodega
                + "ORDER BY cci.fecha_trans_incci asc,dci.ide_indci asc,signo_intci asc");
        //CALCULA PRECIO PROMEDIO 
        double dou_cantf = 0;
        double dou_preciof = 0;
        double dou_saldof = 0;
        if (!tab_kardex.isEmpty()) {
            for (int i = 0; i < tab_kardex.getTotalFilas(); i++) {
                double dou_cant_fila = 0;
                double dou_precio_fila = 0;
                double dou_saldo_fila = 0;

                if (tab_kardex.getValor(i, "VTOT_INGRESO") != null && tab_kardex.getValor(i, "VTOT_INGRESO").isEmpty() == false) {
                    try {
                        dou_cant_fila = Double.parseDouble(tab_kardex.getValor(i, "CANT_INGRESO"));
                        dou_precio_fila = Double.parseDouble(tab_kardex.getValor(i, "VUNI_INGRESO"));
                        dou_saldo_fila = Double.parseDouble(tab_kardex.getValor(i, "VTOT_INGRESO"));
                    } catch (Exception e) {
                    }
                    if (i == 0) {
                        dou_cantf += dou_cant_fila;
                        dou_preciof = dou_precio_fila;
                        dou_saldof = dou_cant_fila * dou_precio_fila;
                    } else {
                        dou_cantf += dou_cant_fila;
                        dou_saldof += dou_saldo_fila;
                        dou_preciof = dou_saldof / dou_cantf;
                    }
                } else if (tab_kardex.getValor(i, "VTOT_EGRESO") != null && tab_kardex.getValor(i, "VTOT_EGRESO").isEmpty() == false) {
                    try {
                        dou_cant_fila = Double.parseDouble(tab_kardex.getValor(i, "CANT_EGRESO"));
                        dou_precio_fila = Double.parseDouble(tab_kardex.getValor(i, "VUNI_EGRESO"));
                        dou_saldo_fila = Double.parseDouble(tab_kardex.getValor(i, "VTOT_EGRESO"));
                    } catch (Exception e) {
                    }
                    if (i == 0) {
                        dou_cantf -= dou_cant_fila;
                        dou_preciof = dou_precio_fila;
                        dou_saldof = dou_cant_fila * dou_precio_fila;
                    } else {
                        dou_cantf -= dou_cant_fila;
                        dou_saldof -= dou_saldo_fila;
                        dou_preciof = dou_saldof / dou_cantf;
                    }
                }
            }
        }
        resultado.add(dou_cantf);//CANT INICIA
        resultado.add(dou_preciof);//PRECIO UNIT. INICIA
        resultado.add(dou_saldof);//SALDO INICIA

        return resultado;
    }

    /**
     * Retorna la cuenta configurada del Producto con el identificador
     * INVENTARIO
     *
     * @param ide_inarti Producto
     * @return
     */
    public String getCuentaProducto(String ide_inarti) {
        return ser_configuracion.getCuentaProducto("INVENTARIO-GASTO-ACTIVO", ide_inarti);
    }

    /**
     * Retorna si un Producto tiene configurada una cuenta contable
     *
     * @param ide_inarti
     * @return
     */
    public boolean isTieneCuentaConfiguradaProducto(String ide_inarti) {
        return !utilitario.consultar("Select * from con_det_conf_asie "
                + "where ide_inarti=" + ide_inarti + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("INVENTARIO-GASTO-ACTIVO")).isEmpty();
    }

    /**
     * Retorna la sentencia SQL para actualizar la configuracion de la cuenta
     * del Producto
     *
     * @param ide_inarti Producto
     * @param ide_cndpc Nueva Cuenta
     * @return
     */
    public String getSqlActualizarCuentaProducto(String ide_inarti, String ide_cndpc) {
        return "update con_det_conf_asie "
                + "set ide_cndpc=" + ide_cndpc + " "
                + "where ide_inarti=" + ide_inarti + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("INVENTARIO-GASTO-ACTIVO");
    }

    /**
     * Retorna la sentencia SQL para insertar la configuracion de la cuenta del
     * cliente
     *
     * @param ide_inarti
     * @param ide_cndpc
     * @return
     */
    public String getSqlInsertarCuentaProducto(String ide_inarti, String ide_cndpc) {
        return "insert into con_det_conf_asie (ide_cndca,ide_inarti,ide_cndpc,ide_cnvca)"
                + "values (" + utilitario.getConexion().getMaximo("con_det_conf_asie", "ide_cndca", 1)
                + ", " + ide_inarti + ", " + ide_cndpc + ","
                + ser_configuracion.getCodigoVigenciaIdentificador("INVENTARIO-GASTO-ACTIVO") + " )";
    }

    /**
     * Retorna una sentencia SQL que contiene las Ventas a Clientes que a an
     * comprado un Producto en una sucursal por rango de fechas
     *
     * @param ide_inarti
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlVentasProducto(String ide_inarti, String fechaInicio, String fechaFin) {
        return "SELECT cdf.ide_ccdfa,cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa ,p.nom_geper ,cdf.cantidad_ccdfa,cdf.precio_ccdfa,cdf.total_ccdfa \n"
                + "from cxc_deta_factura cdf \n"
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=cdf.ide_cccfa \n"
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "left join gen_persona p on cf.ide_geper=p.ide_geper "
                + "where cdf.ide_inarti=" + ide_inarti + " "
                // + "and cdf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "and cf.fecha_emisi_cccfa  BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and cf.ide_ccefa=" + utilitario.getVariable("p_cxc_estado_factura_normal") + " "
                + "ORDER BY cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa";
    }

    /**
     * Retorna una sentencia SQL que contiene los detalles de Productos X
     * SUCURSAL que compramos a un Proveedor en un rango de fechas
     *
     * @param ide_inarti Proveedor
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlComprasProducto(String ide_inarti, String fechaInicio, String fechaFin) {
        return "SELECT cdf.ide_cpdfa,cf.fecha_emisi_cpcfa, numero_cpcfa  ,nom_geper ,cdf.cantidad_cpdfa,cdf.precio_cpdfa,cdf.valor_cpdfa "
                + "from cxp_detall_factur cdf "
                + "left join cxp_cabece_factur cf on cf.ide_cpcfa=cdf.ide_cpcfa "
                + "left join inv_articulo iart on iart.ide_inarti=cdf.ide_inarti "
                + "left join gen_persona p on cf.ide_geper=p.ide_geper "
                + "where cdf.ide_inarti=" + ide_inarti + " "
                //  + "and cdf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + " and cf.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_normal")
                + " and cf.fecha_emisi_cpcfa  BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "ORDER BY cf.fecha_emisi_cpcfa, numero_cpcfa";
    }

    /**
     * Ventas Mensuales en un año de un Producto
     *
     * @param ide_inarti
     * @param anio
     * @return
     */
    public String getSqlTotalVentasMensualesProducto(String ide_inarti, String anio) {
        String p_cxc_estado_factura_normal = utilitario.getVariable("p_cxc_estado_factura_normal");
        return "select nombre_gemes,"
                + "(select count(a.ide_cccfa) as num_facturas from cxc_cabece_factura a inner join cxc_deta_factura cdf on a.ide_cccfa=cdf.ide_cccfa  where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and ide_inarti=" + ide_inarti + "  and ide_ccefa=" + p_cxc_estado_factura_normal + "),"
                + "(select sum(cantidad_ccdfa) as cantidad from cxc_cabece_factura a inner join cxc_deta_factura cdf on a.ide_cccfa=cdf.ide_cccfa  where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and ide_inarti=" + ide_inarti + "  and ide_ccefa=" + p_cxc_estado_factura_normal + "),"
                + "(select sum(total_ccdfa) as total from cxc_cabece_factura a inner join cxc_deta_factura cdf on a.ide_cccfa=cdf.ide_cccfa  where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and ide_inarti=" + ide_inarti + "  and ide_ccefa=" + p_cxc_estado_factura_normal + ") "
                + "from gen_mes "
                + "order by ide_gemes";
    }

    /**
     * Compras Mensuales en un año de un Producto
     *
     * @param ide_inarti
     * @param anio
     * @return
     */
    public String getSqlTotalComprasMensualesProducto(String ide_inarti, String anio) {
        String p_cxp_estado_factura_normal = utilitario.getVariable("p_cxp_estado_factura_normal");
        return "select nombre_gemes,"
                + "(select count(a.ide_cpcfa) as num_facturas from cxp_cabece_factur a inner join cxp_detall_factur cdf on a.ide_cpcfa=cdf.ide_cpcfa  where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in( " + anio + ") and ide_inarti=" + ide_inarti + "  and ide_cpefa=" + p_cxp_estado_factura_normal + "),"
                + "(select sum(cantidad_cpdfa) as cantidad from cxp_cabece_factur a inner join cxp_detall_factur cdf on a.ide_cpcfa=cdf.ide_cpcfa  where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in( " + anio + ") and ide_inarti=" + ide_inarti + "  and ide_cpefa=" + p_cxp_estado_factura_normal + "),"
                + "(select sum(valor_cpdfa) as total from cxp_cabece_factur a inner join cxp_detall_factur cdf on a.ide_cpcfa=cdf.ide_cpcfa  where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in( " + anio + ") and ide_inarti=" + ide_inarti + "  and ide_cpefa=" + p_cxp_estado_factura_normal + ") "
                + "from gen_mes "
                + "order by ide_gemes";
    }

    /**
     * Reorna los clientes que han comprado un producto, con ultima fecha de
     * venta, ultimo precio de venta,
     *
     * @param ide_inarti
     * @return
     */
    public String getSqlUltimosPreciosVentas(String ide_inarti) {
        String p_cxc_estado_factura_normal = utilitario.getVariable("p_cxc_estado_factura_normal");
        return "select distinct b.ide_geper,nom_geper,max(b.fecha_emisi_cccfa) as fecha_ultima_venta,\n"
                + "(select cantidad_ccdfa from cxc_deta_factura  inner join cxc_cabece_factura  on cxc_deta_factura.ide_cccfa=cxc_cabece_factura.ide_cccfa where ide_ccefa=" + p_cxc_estado_factura_normal + " and ide_geper=b.ide_geper and ide_inarti=" + ide_inarti + " order by fecha_emisi_cccfa desc limit 1) as ultima_cantidad,\n"
                + "(select precio_ccdfa from cxc_deta_factura  inner join cxc_cabece_factura  on cxc_deta_factura.ide_cccfa=cxc_cabece_factura.ide_cccfa where ide_ccefa=" + p_cxc_estado_factura_normal + " and ide_geper=b.ide_geper and ide_inarti=" + ide_inarti + " order by fecha_emisi_cccfa desc limit 1) as ultimo_precio,\n"
                + "(select total_ccdfa from cxc_deta_factura  inner join cxc_cabece_factura  on cxc_deta_factura.ide_cccfa=cxc_cabece_factura.ide_cccfa where ide_ccefa=" + p_cxc_estado_factura_normal + " and ide_geper=b.ide_geper and ide_inarti=" + ide_inarti + " order by fecha_emisi_cccfa desc limit 1) as valor_total\n"
                + "from cxc_deta_factura a \n"
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa\n"
                + "inner join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "where ide_ccefa=" + p_cxc_estado_factura_normal + "\n"
                + "and a.ide_inarti=" + ide_inarti + "\n"
                + "AND a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + "\n"
                + "group by a.ide_inarti,b.ide_geper,nom_geper\n"
                + "order by 3,nom_geper desc";
    }

    /**
     * Reorna los proveedores de un un producto, con ultima fecha de compra,
     * ultimo precio de compra,
     *
     * @param ide_inarti
     * @return
     */
    public String getSqlUltimosPreciosCompras(String ide_inarti) {
        String p_cxp_estado_factura_normal = utilitario.getVariable("p_cxp_estado_factura_normal");
        return "select distinct b.ide_geper,nom_geper,max(b.fecha_emisi_cpcfa) as fecha_ultima_venta,\n"
                + "(select cantidad_cpdfa from cxp_detall_factur  inner join cxp_cabece_factur  on cxp_detall_factur.ide_cpcfa=cxp_cabece_factur.ide_cpcfa where ide_cpefa=" + p_cxp_estado_factura_normal + " and ide_geper=b.ide_geper and ide_inarti=" + ide_inarti + " order by fecha_emisi_cpcfa desc limit 1) as ultima_cantidad,\n"
                + "(select precio_cpdfa from cxp_detall_factur  inner join cxp_cabece_factur  on cxp_detall_factur.ide_cpcfa=cxp_cabece_factur.ide_cpcfa where ide_cpefa=" + p_cxp_estado_factura_normal + " and ide_geper=b.ide_geper and ide_inarti=" + ide_inarti + " order by fecha_emisi_cpcfa desc limit 1) as ultimo_precio,\n"
                + "(select valor_cpdfa  from cxp_detall_factur  inner join cxp_cabece_factur  on cxp_detall_factur.ide_cpcfa=cxp_cabece_factur.ide_cpcfa where ide_cpefa=" + p_cxp_estado_factura_normal + " and ide_geper=b.ide_geper and ide_inarti=" + ide_inarti + " order by fecha_emisi_cpcfa desc limit 1) as valor_total\n"
                + "from cxp_detall_factur a \n"
                + "inner join cxp_cabece_factur b on a.ide_cpcfa=b.ide_cpcfa\n"
                + "inner join gen_persona c on b.ide_geper=c.ide_geper\n"
                + "where ide_cpefa=" + p_cxp_estado_factura_normal + "\n"
                + "and a.ide_inarti=" + ide_inarti + "\n"
                + "AND a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + "\n"
                + "group by a.ide_inarti,b.ide_geper,nom_geper\n"
                + "order by 3,nom_geper desc";
    }

}
