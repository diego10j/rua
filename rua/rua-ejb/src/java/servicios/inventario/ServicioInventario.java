/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ServicioInventario {

    private final Utilitario utilitario = new Utilitario();

    @EJB
    private ServicioProducto ser_producto;

    ///////////////////////ORDENES DE PRODUCCION
    public String getIdeOrdenProduccion(String num_orden_inorp) {
        return utilitario.consultar("select ide_inorp,ide_incci,inv_ide_incci,num_orden_inorp from inv_orden_prod where ide_sucu = " + utilitario.getVariable("IDE_SUCU") + " and num_orden_inorp='" + num_orden_inorp + "'").getValor("ide_incci");
    }

    /**
     * Consulta las ordenes de producción de una sucursal
     *
     * @return
     */
    public String getSqlOrdenesProduccion() {
        return "select a.ide_inorp,num_orden_inorp,fecha_entrega_inorp,concepto_inorp,cantidad_inorp,concepto_incfo,nombre_inarti,ide_incci,inv_ide_incci "
                + "from inv_orden_prod a "
                + " inner join inv_cab_formula b on a.ide_incfo = b.ide_incfo  inner join inv_articulo c on b.ide_inarti= c.ide_inarti   "
                + " where a.ide_sucu = " + utilitario.getVariable("IDE_SUCU") + " order by num_orden_inorp desc";
    }

    public TablaGenerica getDetalleFormula(String ide_incfo) {
        return utilitario.consultar("select * from inv_deta_formula where ide_incfo=" + ide_incfo);
    }

    public String getProductoFormula(String ide_incfo) {
        return utilitario.consultar("select ide_incfo,ide_inarti from inv_cab_formula where ide_incfo=" + ide_incfo).getValor("ide_inarti");
    }

    public String getSecuencialFormula() {
        List list = utilitario.getConexion().consultar("SELECT max(num_formula_incfo) FROM inv_cab_formula "
                + "WHERE ide_sucu=" + utilitario.getVariable("IDE_SUCU"));
        String str_maximo = "0";
        if (list != null && !list.isEmpty()) {
            if (list.get(0) != null) {
                if (!list.get(0).toString().isEmpty()) {
                    str_maximo = list.get(0) + "";
                }
            }
        }
        String num_max = String.valueOf(Integer.parseInt(str_maximo) + 1);
        String ceros = utilitario.generarCero(9 - num_max.length());
        str_maximo = ceros.concat(num_max);

        return str_maximo;
    }

    public String getSecuencialOrden() {
        List list = utilitario.getConexion().consultar("SELECT max(substring(num_orden_inorp from 3)) FROM inv_orden_prod "
                + "WHERE ide_sucu=" + utilitario.getVariable("IDE_SUCU"));
        String str_maximo = "0";
        if (list != null && !list.isEmpty()) {
            if (list.get(0) != null) {
                if (!list.get(0).toString().isEmpty()) {
                    str_maximo = list.get(0) + "";
                }
            }
        }
        String num_max = String.valueOf(Integer.parseInt(str_maximo) + 1);
        String ceros = utilitario.generarCero(7 - num_max.length());
        str_maximo = "OP" + ceros.concat(num_max);

        return str_maximo;
    }

    //////////////////////
    public String getIdeComprobanteDocCxP(String ide_cpcfa) {
        return utilitario.consultar("select ide_incci,ide_incci as ide from inv_det_comp_inve where ide_cpcfa=" + ide_cpcfa + " GROUP BY ide_incci").getValor("ide_incci");
    }

    /**
     * Genera la transaccion de inventario para ventas de Facturas CXC
     *
     * @param tab_factura_cxc
     * @param tab_detalle
     */
    public void generarComprobnateTransaccionVenta(Tabla tab_factura_cxc, Tabla tab_detalle) {
        String p_estado_normal_inv = utilitario.getVariable("p_inv_estado_normal");
        String p_tipo_transaccion_inv_venta = utilitario.getVariable("p_inv_tipo_transaccion_venta");
        TablaGenerica tab_cab_comp_inv = new TablaGenerica();
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
        tab_cab_comp_inv.setCondicion("ide_incci=-1");
        tab_cab_comp_inv.ejecutarSql();
        //Detalles
        TablaGenerica tab_det_comp_inv = new TablaGenerica();
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
        tab_det_comp_inv.setCondicion("ide_indci=-1");
        tab_det_comp_inv.ejecutarSql();

        tab_cab_comp_inv.insertar();
        tab_cab_comp_inv.setValor("ide_geper", tab_factura_cxc.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("ide_inepi", p_estado_normal_inv);  /////variable estado normal de inventario
        tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion_inv_venta);   ////variable titpo transaccion compra 
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));

        //Busca
        tab_cab_comp_inv.setValor("ide_inbod", getBodegaSucursal());
        tab_cab_comp_inv.setValor("numero_incci", getSecuencialComprobanteInventario(tab_cab_comp_inv.getValor("ide_inbod")));  /// calcular numero de comprobante de inventario
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_factura_cxc.getValor("fecha_emisi_cccfa"));
        tab_cab_comp_inv.setValor("observacion_incci", tab_factura_cxc.getValor("observacion_cccfa"));
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
//        tab_cab_comp_inv.setValor("ide_cnccc", ide_cnccc);
        tab_cab_comp_inv.guardar();

        for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
            tab_det_comp_inv.insertar();
            tab_det_comp_inv.setValor("ide_inarti", tab_detalle.getValor(i, "ide_inarti"));
            tab_det_comp_inv.setValor("ide_cccfa", tab_factura_cxc.getValor("ide_cccfa"));
            tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));

            //Si hay conversion de unidades 
            double cant_convertida = ser_producto.getConversionCantidadProducto(tab_detalle.getValor(i, "ide_inarti"), tab_detalle.getValor(i, "ide_inuni"), Double.parseDouble(tab_detalle.getValor(i, "cantidad_ccdfa")));
            double precio = Double.parseDouble(tab_detalle.getValor(i, "precio_ccdfa"));
            double total = cant_convertida * precio;

            tab_det_comp_inv.setValor("cantidad_indci", utilitario.getFormatoNumero(cant_convertida, getDecimalesCantidad()));
            tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(precio, getDecimalesPrecioUnitario()));
            tab_det_comp_inv.setValor("valor_indci", utilitario.getFormatoNumero(total));
            tab_det_comp_inv.setValor("observacion_indci", tab_detalle.getValor(i, "observacion_ccdfa"));

        }
        tab_det_comp_inv.guardar();
    }

    public void generarModificarComprobnateTransaccionVenta(Tabla tab_factura_cxc, Tabla tab_detalle) {
        String ide_incci = utilitario.consultar("select ide_incci,ide_cccfa from inv_det_comp_inve where ide_cccfa=" + tab_factura_cxc.getValor("ide_cccfa") + " limit 1").getValor("ide_incci");
        if (ide_incci == null || ide_incci.isEmpty()) {
            ide_incci = "-1";
        }

        String p_estado_normal_inv = utilitario.getVariable("p_inv_estado_normal");
        String p_tipo_transaccion_inv_venta = utilitario.getVariable("p_inv_tipo_transaccion_venta");
        TablaGenerica tab_cab_comp_inv = new TablaGenerica();
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
        tab_cab_comp_inv.setCondicion("ide_incci=" + ide_incci);
        tab_cab_comp_inv.ejecutarSql();
        //Detalles
        TablaGenerica tab_det_comp_inv = new TablaGenerica();
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
        tab_det_comp_inv.setCondicion("ide_incci=" + ide_incci);
        tab_det_comp_inv.ejecutarSql();
        if (tab_cab_comp_inv.isEmpty()) {
            tab_cab_comp_inv.insertar();
        } else {
            utilitario.getConexion().agregarSqlPantalla("delete from inv_det_comp_inve where ide_indci in (" + tab_det_comp_inv.getStringColumna("ide_indci") + ")");
            tab_cab_comp_inv.modificar(tab_cab_comp_inv.getFilaActual());
        }
        //elimina detalles existentes

        tab_cab_comp_inv.setValor("ide_geper", tab_factura_cxc.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("ide_inepi", p_estado_normal_inv);  /////variable estado normal de inventario
        tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion_inv_venta);   ////variable titpo transaccion compra 
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));

        //Busca
        tab_cab_comp_inv.setValor("ide_inbod", getBodegaSucursal());
        tab_cab_comp_inv.setValor("numero_incci", getSecuencialComprobanteInventario(tab_cab_comp_inv.getValor("ide_inbod")));  /// calcular numero de comprobante de inventario
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_factura_cxc.getValor("fecha_emisi_cccfa"));
        tab_cab_comp_inv.setValor("observacion_incci", tab_factura_cxc.getValor("observacion_cccfa"));
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
//        tab_cab_comp_inv.setValor("ide_cnccc", ide_cnccc);
        tab_cab_comp_inv.guardar();

        for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
            tab_det_comp_inv.insertar();
            tab_det_comp_inv.setValor("ide_inarti", tab_detalle.getValor(i, "ide_inarti"));
            tab_det_comp_inv.setValor("ide_cccfa", tab_factura_cxc.getValor("ide_cccfa"));
            tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));

            //Si hay conversion de unidades 
            double cant_convertida = ser_producto.getConversionCantidadProducto(tab_detalle.getValor(i, "ide_inarti"), tab_detalle.getValor(i, "ide_inuni"), Double.parseDouble(tab_detalle.getValor(i, "cantidad_ccdfa")));
            double precio = Double.parseDouble(tab_detalle.getValor(i, "precio_ccdfa"));
            double total = cant_convertida * precio;

            tab_det_comp_inv.setValor("cantidad_indci", utilitario.getFormatoNumero(cant_convertida, getDecimalesCantidad()));
            tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(precio, getDecimalesPrecioUnitario()));
            tab_det_comp_inv.setValor("valor_indci", utilitario.getFormatoNumero(total));

            tab_det_comp_inv.setValor("observacion_indci", tab_detalle.getValor(i, "observacion_ccdfa"));

        }
        tab_det_comp_inv.guardar();
    }

    public void generarModificarComprobnateTransaccionCompra(Tabla tab_factura_cxp, Tabla tab_detalle) {
        String ide_incci = utilitario.consultar("select ide_incci,ide_cpcfa from inv_det_comp_inve where ide_cpcfa=" + tab_factura_cxp.getValor("ide_cpcfa") + " limit 1").getValor("ide_incci");
        if (ide_incci == null || ide_incci.isEmpty()) {
            ide_incci = "-1";
        }

        String p_estado_normal_inv = utilitario.getVariable("p_inv_estado_normal");
        String p_tipo_transaccion_inv_compra = utilitario.getVariable("p_inv_tipo_transaccion_compra"); //factura        
        if (tab_factura_cxp.getValor("ide_cntdo").equals("0")) {
            p_tipo_transaccion_inv_compra = "13"; //nota de credito --13= reversa menos        
        }

        TablaGenerica tab_cab_comp_inv = new TablaGenerica();
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
        tab_cab_comp_inv.setCondicion("ide_incci=" + ide_incci);
        tab_cab_comp_inv.ejecutarSql();
        //Detalles
        TablaGenerica tab_det_comp_inv = new TablaGenerica();
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
        tab_det_comp_inv.setCondicion("ide_incci=" + ide_incci);
        tab_det_comp_inv.ejecutarSql();
        if (tab_cab_comp_inv.isEmpty()) {
            tab_cab_comp_inv.insertar();
        } else {
            utilitario.getConexion().agregarSqlPantalla("delete from inv_det_comp_inve where ide_indci in (" + tab_det_comp_inv.getStringColumna("ide_indci") + ")");
            tab_cab_comp_inv.modificar(tab_cab_comp_inv.getFilaActual());
        }
        //elimina detalles existentes

        tab_cab_comp_inv.setValor("ide_geper", tab_factura_cxp.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("ide_inepi", p_estado_normal_inv);  /////variable estado normal de inventario
        tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion_inv_compra);   ////variable titpo transaccion compra 
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        //Busca
        tab_cab_comp_inv.setValor("ide_inbod", getBodegaSucursal());
        tab_cab_comp_inv.setValor("numero_incci", getSecuencialComprobanteInventario(tab_cab_comp_inv.getValor("ide_inbod")));  /// calcular numero de comprobante de inventario
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_factura_cxp.getValor("fecha_emisi_cpcfa"));
        tab_cab_comp_inv.setValor("observacion_incci", tab_factura_cxp.getValor("observacion_cpcfa"));
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
        tab_cab_comp_inv.setValor("ide_cnccc", tab_factura_cxp.getValor("ide_cnccc"));
        tab_cab_comp_inv.guardar();

        for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
            tab_det_comp_inv.insertar();
            tab_det_comp_inv.setValor("ide_inarti", tab_detalle.getValor(i, "ide_inarti"));
            tab_det_comp_inv.setValor("ide_cpcfa", tab_factura_cxp.getValor("ide_cpcfa"));
            tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));
            tab_det_comp_inv.setValor("cantidad_indci", utilitario.getFormatoNumero(tab_detalle.getValor(i, "cantidad_cpdfa"), getDecimalesCantidad()));
            tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(tab_detalle.getValor(i, "precio_cpdfa"), getDecimalesPrecioUnitario()));
            tab_det_comp_inv.setValor("valor_indci", tab_detalle.getValor(i, "valor_cpdfa"));
            tab_det_comp_inv.setValor("observacion_indci", tab_detalle.getValor(i, "observacion_cpdfa"));

        }
        tab_det_comp_inv.guardar();
    }

    /**
     * Retorna la bodega asociada a la sucursal
     *
     * @return
     */
    public String getBodegaSucursal() {
        String ide_inbod = utilitario.consultar("SELECT ide_inbod,ide_sucu FROM inv_bodega  WHERE ide_sucu=" + utilitario.getVariable("ide_sucu") + " and nivel_inbod='HIJO'").getValor("ide_inbod");
        if (ide_inbod == null) {
            ide_inbod = utilitario.consultar("SELECT ide_inbod,ide_sucu FROM inv_bodega where nivel_inbod='HIJO'").getValor("ide_inbod");
        }
        return ide_inbod;
    }

    public String getMaterialInventario(String nivel) {
        String ide_inbod = "SELECT ide_inarti,nombre_inarti FROM inv_articulo  WHERE  nivel_inarti in (" + nivel + ")";
        return ide_inbod;
    }

    public void generarComprobanteTransaccionCompra(Tabla tab_factura_cxp, Tabla tab_detalle) {
        String p_estado_normal_inventario = utilitario.getVariable("p_inv_estado_normal");

        String p_tipo_transaccion_inv_compra = utilitario.getVariable("p_inv_tipo_transaccion_compra"); //factura
        String observación = tab_factura_cxp.getValor("observacion_cpcfa");
        if (tab_factura_cxp.getValor("ide_cntdo").equals("0")) {
            p_tipo_transaccion_inv_compra = "13"; //nota de credito --13= reversa menos
            observación = "V/. NOTA DE CREDITO FAC. " + tab_factura_cxp.getValor("numero_cpcfa");
        }

        //Cabecera         
        TablaGenerica tab_cab_comp_inv = new TablaGenerica();
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci");
        tab_cab_comp_inv.setCondicion("ide_incci=-1");
        tab_cab_comp_inv.ejecutarSql();
        tab_cab_comp_inv.insertar();
        tab_cab_comp_inv.setValor("ide_inepi", p_estado_normal_inventario);  /////variable estado normal de inventario
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));
        tab_cab_comp_inv.setValor("ide_inbod", getBodegaSucursal());
        tab_cab_comp_inv.setValor("numero_incci", getSecuencialComprobanteInventario(tab_cab_comp_inv.getValor("ide_inbod")));  /// calcular numero de comprobante de inventario
        tab_cab_comp_inv.setValor("ide_geper", tab_factura_cxp.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_factura_cxp.getValor("fecha_emisi_cpcfa"));
        tab_cab_comp_inv.setValor("observacion_incci", observación);
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
        tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion_inv_compra);   ////variable titpo transaccion compra 
        //tab_cab_comp_inv.setValor("ide_cnccc", ide_cnccc);
        //Detalles

        TablaGenerica tab_det_comp_inv = new TablaGenerica();
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci");
        tab_det_comp_inv.setCondicion("ide_indci=-1");
        tab_det_comp_inv.ejecutarSql();
        tab_cab_comp_inv.guardar();

        double porce_descuento = 0;
        if (tab_factura_cxp.getValor("porcen_desc_cpcfa") != null) {
            try {
                porce_descuento = Double.parseDouble(tab_factura_cxp.getValor("porcen_desc_cpcfa"));
            } catch (Exception e) {
            }

        }

        for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {

//                if (haceKardex(tab_tabla2.getValor(i, "ide_inarti"))) {
            //  if (in.esTipoCombo(tab_tabla2.getValor(i, "ide_inarti")) == false) {
            tab_det_comp_inv.insertar();
            if (tab_detalle.getValor(i, "iva_inarti_cpdfa") != null
                    && !tab_detalle.getValor(i, "iva_inarti_cpdfa").isEmpty()
                    && tab_detalle.getValor(i, "iva_inarti_cpdfa").equals("1")) {
//                    System.out.println(in.aplicaIva(tab_tabla2.getValor(i, "ide_inarti")) + "    " + tab_tabla2.getValor(i, "ide_inarti"));
//                    if (in.aplicaIva(tab_tabla2.getValor(i, "ide_inarti"))) {
                try {
                    //double precio = Double.parseDouble(tab_detalle.getValor(i, "precio_cpdfa")) + (Double.parseDouble(tab_detalle.getValor(i, "precio_cpdfa")) * p_porcentaje_iva);
                    double precio = Double.parseDouble(tab_detalle.getValor(i, "precio_cpdfa"));
                    if (porce_descuento > 0) {
                        ///aplico &  descuento                                
                        precio = Double.parseDouble(tab_detalle.getValor(i, "precio_cpdfa"));
                        double valor_descuento = precio * (porce_descuento / 100);
                        precio = precio - valor_descuento;
                        //  precio = precio + (precio * p_porcentaje_iva);
                    }
                    double valor = (Double.parseDouble(tab_detalle.getValor(i, "cantidad_cpdfa")) * precio);
                    tab_det_comp_inv.setValor("precio_indci", precio + "");
                    tab_det_comp_inv.setValor("valor_indci", valor + "");
//                    String precio_promedio = in.getPrecioPromedioTransaccionPositiva(tab_tabla2.getValor(i, "ide_inarti"), "1", valor, Double.parseDouble(tab_tabla2.getValor(i, "cantidad_cpdfa")));
//                    if (precio_promedio != null) {
//                        tab_det_comp_inv.setValor("precio_promedio_indci", precio_promedio);
//                    }
                } catch (Exception e) {
                }
            } else {

                double precio = Double.parseDouble(tab_detalle.getValor(i, "precio_cpdfa"));
                if (porce_descuento > 0) {
                    ///aplico &  descuento                                
                    precio = Double.parseDouble(tab_detalle.getValor(i, "precio_cpdfa"));
                    precio = precio - (precio * (porce_descuento / 100));
                }
                double valor = Double.parseDouble(tab_detalle.getValor(i, "cantidad_cpdfa")) * precio;

                tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(precio,getDecimalesPrecioUnitario()));
                tab_det_comp_inv.setValor("valor_indci", utilitario.getFormatoNumero(valor));
//                String precio_promedio = in.getPrecioPromedioTransaccionPositiva(tab_tabla2.getValor(i, "ide_inarti"), "1", Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdfa")), Double.parseDouble(tab_tabla2.getValor(i, "cantidad_cpdfa")));
//                if (precio_promedio != null) {
//                    tab_det_comp_inv.setValor("precio_promedio_indci", precio_promedio);
//                }
            }
            tab_det_comp_inv.setValor("ide_inarti", tab_detalle.getValor(i, "ide_inarti"));
            tab_det_comp_inv.setValor("ide_cpcfa", tab_factura_cxp.getValor("ide_cpcfa"));
            tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));
            tab_det_comp_inv.setValor("cantidad_indci", utilitario.getFormatoNumero(tab_detalle.getValor(i, "cantidad_cpdfa"), getDecimalesCantidad()));
            tab_det_comp_inv.setValor("observacion_indci", tab_detalle.getValor(i, "observacion_cpdfa"));
        }
        //}

        // }
        tab_det_comp_inv.guardar();
    }

    /**
     * Genera secuencial de comprobante por bodega
     *
     * @param ide_inbod
     * @return
     */
    public String getSecuencialComprobanteInventario(String ide_inbod) {

        List list = utilitario.getConexion().consultar("SELECT max(NUMERO_INCCI) FROM inv_cab_comp_inve "
                + "WHERE ide_inbod=" + ide_inbod);
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

    /**
     * Retorna el signo de un tipo de transaccion
     *
     * @param tipo_tran_inv
     * @return
     */
    public String getSignoTipoTransaccion(String tipo_tran_inv) {
        Tabla ltab_tipo_transaccion = new Tabla();
        ltab_tipo_transaccion.setSql("SELECT ide_intci,signo_intci from inv_tip_comp_inve where ide_intci = "
                + "(select ide_intci from inv_tip_tran_inve where ide_intti =" + tipo_tran_inv + ") "
                + "and ide_empr=" + utilitario.getVariable("ide_empr"));
        ltab_tipo_transaccion.ejecutarSql();
        return ltab_tipo_transaccion.getValor("signo_intci");
    }

    /**
     * Elimina cabeceraq y detalles de un comprobante de inventario
     *
     * @param ide_incci
     */
    public void eliminarComprobanteInventario(String ide_incci) {
        utilitario.getConexion().agregarSqlPantalla("delete from  inv_det_comp_inve where ide_incci=" + ide_incci);
        utilitario.getConexion().agregarSqlPantalla("delete from  inv_cab_comp_inve where ide_incci=" + ide_incci);

    }

    /**
     * Cambia a anulado un comprobante de inventario
     *
     * @param ide_incci
     */
    public void anularComprobanteInventario(String ide_incci) {
        String p_estado_anulado_inv = "0"; //******PONER VARIABLE
        cambiarEstadoComprobanteInventario(ide_incci, p_estado_anulado_inv);
    }

    /**
     * Cambia de estado un comprobante de inventario
     *
     * @param ide_incci
     * @param ide_inepi
     */
    public void cambiarEstadoComprobanteInventario(String ide_incci, String ide_inepi) {
        utilitario.getConexion().agregarSqlPantalla("update inv_cab_comp_inve set ide_inepi=" + ide_inepi + "where ide_incci=" + ide_incci);
    }

    public String getSqlComprobantesInventario(String ide_inbod, String fecha_inicio, String fecha_fin) {
        return "select a.ide_incci as cod,a.ide_incci,ide_inepi,a.ide_geper,fecha_trans_incci,a.ide_cnccc,numero_incci,nombre_intti,nom_geper,identificac_geper,\n"
                + "case when signo_intci = 1 THEN sum(valor_indci)  end as INGRESO,\n"
                + "case when signo_intci = -1 THEN sum(valor_indci)  end as EGRESO\n"
                + "from inv_cab_comp_inve a\n"
                + "inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + "inner join inv_tip_tran_inve c on a.ide_intti= c.ide_intti\n"
                + "inner join inv_det_comp_inve d on a.ide_incci = d.ide_incci\n"
                + "left join inv_tip_comp_inve e on c.ide_intci=e.ide_intci\n"
                // + "where a.ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + "\n"
                + "WHERE ide_inbod=" + ide_inbod + "\n"
                + "and fecha_trans_incci BETWEEN '" + fecha_inicio + "'  and '" + fecha_fin + "'\n"
                + "group by a.ide_incci,ide_inepi,a.ide_geper,numero_incci,fecha_trans_incci,nombre_intti,nom_geper,identificac_geper,signo_intci"
                + " order by fecha_trans_incci ";
    }

    public String getSqlComprobantesInventarioNoConta(String ide_inbod, String fecha_inicio, String fecha_fin) {
        return "select a.ide_incci,ide_inepi,a.ide_geper,fecha_trans_incci,numero_incci,nombre_intti,nom_geper,identificac_geper,\n"
                + "case when signo_intci = 1 THEN sum(valor_indci)  end as INGRESO,\n"
                + "case when signo_intci = -1 THEN sum(valor_indci)  end as EGRESO\n"
                + "from inv_cab_comp_inve a\n"
                + "inner join gen_persona b on a.ide_geper=b.ide_geper \n"
                + "inner join inv_tip_tran_inve c on a.ide_intti= c.ide_intti\n"
                + "inner join inv_det_comp_inve d on a.ide_incci = d.ide_incci\n"
                + "left join inv_tip_comp_inve e on c.ide_intci=e.ide_intci\n"
                + "where a.ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + "\n"
                + "AND ide_inbod=" + ide_inbod + "\n"
                + "and fecha_trans_incci BETWEEN '" + fecha_inicio + "'  and '" + fecha_fin + "'\n"
                + "and a.ide_cnccc is null\n"
                + "group by a.ide_incci,ide_inepi,a.ide_geper,numero_incci,fecha_trans_incci,nombre_intti,nom_geper,identificac_geper,signo_intci";
    }

    /**
     * Retorna el codido (ide_incci)
     *
     * @param ide_cccfa
     * @return
     */
    public String getCodigoComprobnateTransaccionVenta(String ide_cccfa) {
        String p_tipo_transaccion_inv_venta = utilitario.getVariable("p_inv_tipo_transaccion_venta");
        String sql = "SELECT ide_cccfa,ide_incci FROM inv_det_comp_inve a "
                + "inner join inv_cab_comp_inve b on a.ide_incci=b.ide_incci "
                + "where a.ide_cccfa=" + ide_cccfa + " "
                + "and ide_intti=" + p_tipo_transaccion_inv_venta + " "
                + "group by ide_cccfa,ide_incci";
        return utilitario.consultar(sql).getValor("ide_incci");
    }

    public String getSqlSaldoProductos(String ide_inbod, String fecha) {
        return "SELECT ARTICULO.ide_inarti,BODEGA.nombre_inbod AS BODEGA,ARTICULO.nombre_inarti AS ARTICULO,ARTICULO.codigo_inarti as CODIGO,nombre_invmar AS MARCA, UNIDAD.nombre_inuni AS UNIDAD, \n"
                + "sum(DETA.cantidad_indci * TIPO.signo_intci) AS EXISTENCIA, \n"
                + "round(sum(DETA.valor_indci * TIPO.signo_intci),3) AS VALOR \n"
                + "FROM inv_det_comp_inve DETA \n"
                + "LEFT JOIN inv_cab_comp_inve CAB ON DETA.ide_incci=CAB.ide_incci \n"
                + "LEFT JOIN inv_tip_tran_inve TRANS ON TRANS.ide_intti=CAB.ide_intti \n"
                + "LEFT JOIN inv_articulo ARTICULO ON DETA.ide_inarti=ARTICULO.ide_inarti --and hace_kardex_inarti=true \n"
                + "LEFT JOIN inv_unidad UNIDAD ON ARTICULO.ide_inuni=UNIDAD.ide_inuni \n"
                + "LEFT JOIN inv_bodega BODEGA ON CAB.ide_inbod=BODEGA.ide_inbod \n"
                + "LEFT JOIN inv_tip_comp_inve TIPO ON TIPO.ide_intci=TRANS.ide_intci \n"
                + "LEFT JOIN inv_marca m on ARTICULO.ide_inmar= m.ide_inmar \n"
                + "WHERE BODEGA.ide_inbod =" + ide_inbod + "\n"
                + "AND CAB.fecha_trans_incci <= '" + fecha + "'\n"
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " \n"
                + "GROUP BY ARTICULO.ide_inarti,BODEGA.nombre_inbod,UNIDAD.ide_inuni,nombre_invmar\n"
                + "ORDER BY BODEGA.nombre_inbod, ARTICULO.nombre_inarti";
    }

    public double getSaldoProducto(String ide_inbod, String ide_inarti) {

        if (ide_inbod == null) {
            //Busca sucursal x defecto
            ide_inbod = getBodegaSucursal();
        }
        ///Aumentar solo para productos que hacen kardex  ////////////////***********************
        double saldo = 0;
        TablaGenerica tag = utilitario.consultar("select dci.ide_inarti,sum (cantidad_indci * signo_intci) as saldo_cantidad from inv_det_comp_inve dci "
                + "left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci "
                + "left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti "
                + "left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci "
                + "where dci.ide_inarti=" + ide_inarti + " "
                + "and ide_inepi=" + utilitario.getVariable("p_inv_estado_normal") + " "
                + "and cci.ide_inbod=" + ide_inbod + " GROUP BY dci.ide_inarti");

        try {
            saldo = Double.parseDouble(utilitario.getFormatoNumero(tag.getValor("saldo_cantidad")));
        } catch (Exception e) {
        }
        return saldo;
    }

    /**
     * Retorna si un producto hace kardex
     *
     * @param ide_inarti
     * @return
     */
    public boolean isHaceKardex(String ide_inarti) {
        boolean hace = true;
        TablaGenerica tb = utilitario.consultar("SELECT ide_inarti,hace_kardex_inarti from inv_articulo where ide_inarti=" + ide_inarti + " and hace_kardex_inarti=true");
        if (tb.isEmpty()) {
            hace = false;
        }
        return hace;
    }

    /**
     * Genera la transaccion de inventario para ventas de Facturas CXC
     *
     * @param ide_cpcno
     */
    public void generarComprobnateTransaccionNotaCredito(String ide_cpcno) {
        TablaGenerica tab_cab_nota = utilitario.consultar("select * from cxp_cabecera_nota where ide_cpcno=" + ide_cpcno);
        TablaGenerica tab_deta_nota = utilitario.consultar("select * from cxp_detalle_nota where ide_cpcno=" + ide_cpcno);
        String p_estado_normal_inv = utilitario.getVariable("p_inv_estado_normal");
        String p_tipo_transaccion_inv_venta = utilitario.getVariable("p_inv_tipo_transaccion_reversa_mas");

        TablaGenerica tab_cab_comp_inv = new TablaGenerica();
        tab_cab_comp_inv.setTabla("inv_cab_comp_inve", "ide_incci", 0);
        tab_cab_comp_inv.setCondicion("ide_incci=-1");
        tab_cab_comp_inv.ejecutarSql();
        //Detalles
        TablaGenerica tab_det_comp_inv = new TablaGenerica();
        tab_det_comp_inv.setTabla("inv_det_comp_inve", "ide_indci", 0);
        tab_det_comp_inv.setCondicion("ide_indci=-1");
        tab_det_comp_inv.ejecutarSql();

        tab_cab_comp_inv.insertar();
        tab_cab_comp_inv.setValor("ide_geper", tab_cab_nota.getValor("ide_geper"));
        tab_cab_comp_inv.setValor("ide_inepi", p_estado_normal_inv);  /////variable estado normal de inventario
        tab_cab_comp_inv.setValor("ide_intti", p_tipo_transaccion_inv_venta);   ////variable titpo transaccion compra 
        tab_cab_comp_inv.setValor("ide_usua", utilitario.getVariable("ide_usua"));

        //Busca
        tab_cab_comp_inv.setValor("ide_inbod", getBodegaSucursal());
        tab_cab_comp_inv.setValor("numero_incci", getSecuencialComprobanteInventario(tab_cab_comp_inv.getValor("ide_inbod")));  /// calcular numero de comprobante de inventario
        tab_cab_comp_inv.setValor("fecha_trans_incci", tab_cab_nota.getValor("fecha_emisi_cpcno"));
        tab_cab_comp_inv.setValor("observacion_incci", "V/. NOTA DE CREDITO FAC. " + tab_cab_nota.getValor("num_doc_mod_cpcno") + " ");
        tab_cab_comp_inv.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_comp_inv.setValor("hora_sistem_incci", utilitario.getHoraActual());
//        tab_cab_comp_inv.setValor("ide_cnccc", ide_cnccc);
        tab_cab_comp_inv.guardar();

        for (int i = 0; i < tab_deta_nota.getTotalFilas(); i++) {
            tab_det_comp_inv.insertar();
            tab_det_comp_inv.setValor("ide_inarti", tab_deta_nota.getValor(i, "ide_inarti"));
            tab_det_comp_inv.setValor("ide_incci", tab_cab_comp_inv.getValor("ide_incci"));

            //Si hay conversion de unidades 
            double cant_convertida = ser_producto.getConversionCantidadProducto(tab_deta_nota.getValor(i, "ide_inarti"), tab_deta_nota.getValor(i, "ide_inuni"), Double.parseDouble(tab_deta_nota.getValor(i, "cantidad_cpdno")));
            double precio = Double.parseDouble(tab_deta_nota.getValor(i, "precio_cpdno"));
            double total = cant_convertida * precio;

            tab_det_comp_inv.setValor("cantidad_indci", utilitario.getFormatoNumero(cant_convertida, getDecimalesCantidad()));
            tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(precio,getDecimalesPrecioUnitario()));
            tab_det_comp_inv.setValor("valor_indci", utilitario.getFormatoNumero(total));
            tab_det_comp_inv.setValor("observacion_indci", tab_cab_comp_inv.getValor("observacion_incci"));

        }
        tab_det_comp_inv.guardar();
    }

    public String getTipoProducto(String ide_arti) {
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

    public String getSqlComboEmpleados() {
        return "select ide_geper,identificac_geper,nom_geper from gen_persona where es_empleado_geper=true order by nom_geper ";
    }

    public String getSqlComboOrganigrama() {
        return "select ide_georg,nombre_georg from gen_organigrama order by nombre_georg";
    }

    public String getSqlComboProductosKardex() {
        return "select ide_inarti,nombre_inarti from inv_articulo where  nivel_inarti='HIJO' order by nombre_inarti ";
    }
    
     public String getSqlConsultaInventario() {
        return "select ide_inarti,nombre_inarti from inv_articulo where  nivel_inarti='HIJO' order by nombre_inarti ";
    }
       

    /**
     * Retorna el numero de decimales para manejar cantidad
     *
     * @return
     */
    public int getDecimalesCantidad() {
        int numDecimales = 2;
        TablaGenerica tab_emisor = utilitario.consultar("select ide_sucu,cant_decim_sremi from sri_emisor where ide_sucu=" + utilitario.getVariable("ide_sucu"));
        if (tab_emisor.isEmpty() == false) {
            if (tab_emisor.getValor("cant_decim_sremi") != null) {
                try {
                    numDecimales = Integer.parseInt(tab_emisor.getValor("cant_decim_sremi"));
                } catch (Exception e) {
                    numDecimales = 2;
                }
            }
        }
        return numDecimales;
    }

    /**
     * Retorna el numero de decimales para manejar precio unitario
     *
     * @return
     */
    public int getDecimalesPrecioUnitario() {
        int numDecimales = 2;
        TablaGenerica tab_emisor = utilitario.consultar("select ide_sucu,preciou_decim_sremi from sri_emisor where ide_sucu=" + utilitario.getVariable("ide_sucu"));
        if (tab_emisor.isEmpty() == false) {
            if (tab_emisor.getValor("preciou_decim_sremi") != null) {
                try {
                    numDecimales = Integer.parseInt(tab_emisor.getValor("preciou_decim_sremi"));
                } catch (Exception e) {
                    numDecimales = 2;
                }
            }
        }
        return numDecimales;
    }

}
