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
import servicios.pensiones.ServicioPensiones;
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
    @EJB
    private ServicioPensiones ser_pensiones = (ServicioPensiones) utilitario.instanciarEJB(ServicioPensiones.class);

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
        //tab_det_comp_inv.ejecutarSql();
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
        tab_cab_comp_inv.setValor("numero_incci", "VENTA-"+ide_incci);  /// calcular numero de comprobante de inventario
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

                tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(precio, getDecimalesPrecioUnitario()));
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

    public String getSecuencialCompInventario(String ide_intti, String anio) {

        TablaGenerica tab_secuencial = utilitario.consultar("select 1 as codigo,count(ide_incci) as secuencial\n"
                + "from (\n"
                + "select ide_incci,abreviatura_intti,extract(year from fecha_trans_incci) anio,a.ide_intti  \n"
                + "from inv_cab_comp_inve a\n"
                + "left join inv_tip_tran_inve b on a.ide_intti=b.ide_intti \n"
                + ") a\n"
                + "where a.ide_intti=" + ide_intti + " and a.anio=" + anio + "\n"
                + "group by abreviatura_intti");
        TablaGenerica tab_tipo = utilitario.consultar("select * from inv_tip_tran_inve where ide_intti=" + ide_intti);
        String str_maximo = "0";
        if (tab_secuencial.getTotalFilas() > 0) {
            String numero = tab_secuencial.getValor("secuencial");
            str_maximo = tab_tipo.getValor("abreviatura_intti") + "-" + String.valueOf(Integer.parseInt(numero) + 1);
        } else {
            str_maximo = tab_tipo.getValor("abreviatura_intti") + "-" + String.valueOf(Integer.parseInt(str_maximo) + 1);
        }
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
            tab_det_comp_inv.setValor("precio_indci", utilitario.getFormatoNumero(precio, getDecimalesPrecioUnitario()));
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

    /**
     * Retorna las existencias del articulo, a su vez los valores costo
     *
     * @return
     */
    public String saldosArticulos(String articulos, String anio, String bodega) {
        String sql = "";
        sql += "select ide_boart,ingreso_material_boart,existencia_inicial_boart,egreso_material_boart,costo_actual_boart,precio_venta_boart, "
                + "(ingreso_material_boart + existencia_inicial_boart) - egreso_material_boart as saldo_existencia "
                + "from bodt_articulos where ide_boart in (" + articulos + ")";
        return sql;
    }

    public String bodegasProductos(String articulos, String anio, String bodega) {
        return "completar para pruebas de buscar bodega";
    }

    public String getTipoTransaccion() {
        String sql = "";
        sql += "select ide_intti, nombre_intti, nombre_intci from inv_tip_tran_inve a inner join inv_tip_comp_inve b on a.ide_intci=b.ide_intci order by nombre_intci desc, nombre_intti";
        return sql;
    }

    public String getBodtCostoArticulo(String tipo, String articulo, String sucu, String empr) {
        String sql = "";
        sql += "select ide_bocoa,ide_inarti,ide_empr,ide_sucu,tipo_aplica_bocoa,valor_bocoa,porcentaje_bocoa from bodt_costo_articulo  \n"
                + "where ide_inarti = " + articulo + " and activo_bocoa=true";
        if (tipo.equals("1")) {
            sql += " and ide_sucu=" + sucu + " and ide_empr=" + empr;
        }
        return sql;
    }

    public String getBodtArticulo(String tipo, String articulo, String anio, String sucu, String empr) {
        String sql = "";
        sql += "select ide_boart,ide_inarti,ide_geani,ide_sucu,ide_empr,ingreso_material_boart,egreso_material_boart,existencia_inicial_boart,costo_inicial_boart,costo_anterior_boart,costo_actual_boart,(ingreso_material_boart + existencia_inicial_boart - egreso_material_boart) as stock\n"
                + "from bodt_articulos \n"
                + "where ide_inarti=" + articulo + " and ide_geani = " + anio;
        if (tipo.equals("1")) {
            sql += " and ide_sucu=" + sucu + " and ide_empr=" + empr;
        }
        //System.out.println("consulta bodega getBodtArticulo >>>>>>>>> "+sql);
        return sql;
    }

    /**
     * Devuelve el stock actual de un producto de bodega
     *
     * @param articulo
     * @param anio
     * @param sucu
     * @param empr
     * @return
     */
    public double getStockArticulo(String tipo, String articulo, String anio, String sucu, String empr) {
        double stock = 0;
        if (tipo.equals("1")) {
            TablaGenerica tab_stock = utilitario.consultar("select ide_boart,ide_inarti,ide_geani,ide_sucu,ide_empr,ingreso_material_boart,egreso_material_boart,existencia_inicial_boart,costo_inicial_boart,costo_anterior_boart,costo_actual_boart,(ingreso_material_boart + existencia_inicial_boart - egreso_material_boart) as stock\n"
                    + "from bodt_articulos \n"
                    + "where ide_inarti=" + articulo + " and ide_geani = " + anio + " and ide_sucu = " + sucu + " and ide_empr = " + empr + "  ");
            //tab_stock.imprimirSql();
            try {
                stock = Double.parseDouble(utilitario.getFormatoNumero(tab_stock.getValor("stock")));
            } catch (Exception e) {
            }
        } else {
            TablaGenerica tab_stock = utilitario.consultar("select ide_boart,ide_inarti,ide_geani,ide_sucu,ide_empr,ingreso_material_boart,egreso_material_boart,existencia_inicial_boart,costo_inicial_boart,costo_anterior_boart,costo_actual_boart,(ingreso_material_boart + existencia_inicial_boart - egreso_material_boart) as stock\n"
                    + "from bodt_articulos \n"
                    + "where ide_inarti=" + articulo + " and ide_geani = " + anio);
            //tab_stock.imprimirSql();
            try {
                stock = Double.parseDouble(utilitario.getFormatoNumero(tab_stock.getValor("stock")));
            } catch (Exception e) {
            }
        }

        return stock;
    }

    public String getInventarioAnio(String anio) {
        String sql = "";
        sql += "select ide_geani,nom_geani from gen_anio \n"
                + "where nom_geani='" + anio + "'";
        return sql;
    }

    public String getExtraerAnio(String fecha) {
        String sql = "";
        sql += "select 1,cast(extract(year from date '" + fecha + "') as text) as anio";
        return sql;
    }

    public double getValorUnitario(String tipo, String articulo, String anio, String sucu, String empr) {
        double valor = 0;
        TablaGenerica tab_costo = utilitario.consultar(this.getBodtCostoArticulo(tipo, articulo, sucu, empr));
        tab_costo.imprimirSql();
        if (tab_costo.getTotalFilas() > 0) {
            if (tab_costo.getValor("tipo_aplica_bocoa").equals("1")) {
                valor = Double.parseDouble(utilitario.getFormatoNumero(tab_costo.getValor("valor_bocoa"), 4));
            } else if (tab_costo.getValor("tipo_aplica_bocoa").equals("2")) {
                //TablaGenerica tab_anio = utilitario.consultar(this.getInventarioAnio(anio));
                TablaGenerica tab_articulo = utilitario.consultar(this.getBodtArticulo(tipo, articulo, anio, sucu, empr));
                tab_articulo.imprimirSql();
                double porcentaje = Double.parseDouble(utilitario.getFormatoNumero(tab_costo.getValor("porcentaje_bocoa"), 2));
                double valor_actual = Double.parseDouble(utilitario.getFormatoNumero(tab_articulo.getValor("costo_actual_boart"), 4));
                valor = (((porcentaje * valor_actual) / 100) + valor_actual);
            }
        } else {
            valor = 0;
        }

        return valor;
    }

    public String getInventarioGrupo(String grupo) {
        String sql = "";
        sql += "select ide_inarti,codigo_inarti,nombre_inarti \n"
                + "from inv_articulo  \n"
                + "where inv_ide_inarti in (" + grupo + ") ";
        return sql;
    }

    public String getDetalleInventario(String tipo, String codigo, String sucu, String empr) {
        String sql = "";

        sql += "select ide_indci, ide_sucu, ide_inarti, ide_cpcfa, ide_empr, ide_cccfa, \n"
                + "       ide_incci, secuencial_indci, cantidad_indci, cantidad1_indci, \n"
                + "       precio_indci, valor_indci, observacion_indci, referencia_indci, \n"
                + "       referencia1_indci, precio_promedio_indci, usuario_ingre, fecha_ingre, \n"
                + "       hora_ingre, usuario_actua, fecha_actua, hora_actua \n"
                + "from inv_det_comp_inve  \n"
                + "where ide_incci=" + codigo;

        if (tipo.equals("1")) {
            sql += " and ide_sucu=" + sucu + " and ide_empr=" + empr;
        }
        //System.out.println("cargo tabla detalle "+sql);
        return sql;
    }

    public String getInsertarBodegaArticulos(String tipo, String anio, String sucu, String empr, String articulo) {
        TablaGenerica tab_maximo = utilitario.consultar(ser_pensiones.getCodigoMaximoTabla("bodt_articulos", "ide_boart"));
        String sql = "";
        if (tipo.equals("1")) {
            sql += "INSERT INTO bodt_articulos(\n"
                    + "            ide_boart, ide_geani, ide_sucu, ide_empr, ide_inarti,ingreso_material_boart, \n"
                    + "            egreso_material_boart, existencia_inicial_boart,costo_inicial_boart, costo_anterior_boart, \n"
                    + "            costo_actual_boart \n"
                    + "            )\n"
                    + "VALUES (" + tab_maximo.getValor("maximo") + ", " + anio + ", " + sucu + ", " + empr + ", " + articulo + ", 0, 0, 0, 0, 0, 0 );";
        } else {
        sql += "INSERT INTO bodt_articulos(\n"
                + "            ide_boart, ide_geani, ide_inarti,ingreso_material_boart, \n"
                + "            egreso_material_boart, existencia_inicial_boart,costo_inicial_boart, costo_anterior_boart, \n"
                + "            costo_actual_boart \n"
                + "            )\n"
                + "VALUES (" + tab_maximo.getValor("maximo") + ", " + anio + ", " + articulo + ", 0, 0, 0, 0, 0, 0 );";
        }
        return sql;
    }

    public double getPrecioPonderado(Double stock, Double costa_actual, Double cantidad, Double valor_total) {
        //System.out.println("PARAMETROS: " + stock + " " + costa_actual + " " + cantidad + " " + valor_total);
        double costo_actual = 0;
        double total = (stock * costa_actual);
        costo_actual = ((total + valor_total) / (stock + cantidad));
        return costo_actual;
    }

    public String getConsultarTipoTransaccion(String codigo) {
        String sql = "";
        sql += "select ide_intti, nombre_intti, nombre_intci,a.ide_intci from inv_tip_tran_inve a\n"
                + "inner join inv_tip_comp_inve b on a.ide_intci=b.ide_intci\n"
                + "where ide_intti=" + codigo + "\n"
                + "order by nombre_intci desc, nombre_intti";
        return sql;
    }

    public String getActualizarBodegaArticulos(String costo_actual, Double precio, String articulo, String anio) {
        String sql = "";
        sql += "UPDATE bodt_articulos   SET \n"
                + "       costo_anterior_boart=" + costo_actual + ", \n"
                + "       costo_actual_boart=" + precio + "\n"
                + " WHERE ide_inarti=" + articulo + " and ide_geani=" + anio + " ";
        return sql;
    }

    public String getActualizarIngreso(String cantidad, String articulo, String anio) {
        String sql = "";
        sql += "UPDATE bodt_articulos   SET \n"
                + "       ingreso_material_boart=( ingreso_material_boart + " + cantidad + ") \n"
                + " WHERE ide_inarti=" + articulo + " and ide_geani=" + anio + " ";
        return sql;
    }

    public String getActualizarEgreso(String cantidad, String articulo, String anio) {
        String sql = "";
        sql += "UPDATE bodt_articulos   SET \n"
                + "       egreso_material_boart=( egreso_material_boart + " + cantidad + ") \n"
                + " WHERE ide_inarti=" + articulo + " and ide_geani=" + anio + " ";
        return sql;
    }

    public String getActualizarDetalleStock(String stock, Double precio, String codigo, String articulo) {
        String sql = "";
        sql += "UPDATE inv_det_comp_inve SET \n"
                + "       cantidad1_indci= " + stock + ", \n"
                + "       precio_promedio_indci=" + precio + " \n"
                + " WHERE ide_indci=" + codigo + " and ide_inarti=" + articulo + " ";
        //System.out.println(" sql update actualiza detalle stock "+sql);
        return sql;
    }

    public String getActualizarEstadoInventario(String estado, String codigo) {
        String sql = "";
        sql += "UPDATE inv_cab_comp_inve\n"
                + "   SET ide_inepi=" + estado + "\n"
                + " WHERE ide_incci=" + codigo + " ";
        return sql;
    }

    public String getReporteOrdenado(String anio, String articulo) {
        String sql = "";
        sql += "select a.ide_incci,b.ide_intci,e.ide_geani,nom_geani,e.ide_inarti,codigo_inarti,nombre_inarti,existencia_inicial_boart,\n"
                + "	costo_inicial_boart,cantidad_indci,precio_indci,valor_indci,0 as cant_egre,0 as prec_egre,0 as valor_egre,cantidad1_indci,precio_promedio_indci,\n"
                + "	fecha_trans_incci,numero_incci,ide_indci\n"
                + "	from inv_cab_comp_inve a\n"
                + "	left join inv_tip_tran_inve b on a.ide_intti=b.ide_intti\n"
                + "	left join inv_tip_comp_inve c on b.ide_intci=c.ide_intci\n"
                + "	left join inv_det_comp_inve d on a.ide_incci=d.ide_incci\n"
                + "	left join bodt_articulos e on d.ide_inarti=e.ide_inarti\n"
                + "	left join inv_articulo f on e.ide_inarti=f.ide_inarti\n"
                + "	left join gen_anio g on e.ide_geani=g.ide_geani\n"
                + "	where b.ide_intci =cast((select valor_para from sis_parametros where nom_para = 'p_inv_tipo_ingreso')as numeric)\n"
                + "		and e.ide_geani=" + anio + " and e.ide_inarti in (" + articulo + ") and extract(year from fecha_trans_incci)||'' = (select nom_geani from gen_anio where ide_geani ="+anio+")\n"
                + "     and a.ide_inepi=cast((select valor_para from sis_parametros where nom_para = 'p_inv_estado_aprobado')as numeric)\n"
                + "union\n"
                + "	select a.ide_incci,b.ide_intci,e.ide_geani,nom_geani,e.ide_inarti,codigo_inarti,nombre_inarti,existencia_inicial_boart,costo_inicial_boart,\n"
                + "	0 as cant_egre,0 as prec_egre,0 as valor_egre,cantidad_indci,precio_indci,valor_indci,cantidad1_indci,precio_promedio_indci,\n"
                + "	fecha_trans_incci,numero_incci,ide_indci\n"
                + "	from inv_cab_comp_inve a\n"
                + "	left join inv_tip_tran_inve b on a.ide_intti=b.ide_intti\n"
                + "	left join inv_tip_comp_inve c on b.ide_intci=c.ide_intci\n"
                + "	left join inv_det_comp_inve d on a.ide_incci=d.ide_incci\n"
                + "	left join bodt_articulos e on d.ide_inarti=e.ide_inarti\n"
                + "	left join inv_articulo f on e.ide_inarti=f.ide_inarti\n"
                + "	left join gen_anio g on e.ide_geani=g.ide_geani\n"
                + "	where b.ide_intci =cast((select valor_para from sis_parametros where nom_para = 'p_inv_tipo_egreso')as numeric)\n"
                + "	and e.ide_geani=" + anio + " and e.ide_inarti in (" + articulo + ") and extract(year from fecha_trans_incci)||'' = (select nom_geani from gen_anio where ide_geani ="+anio+")\n"
                + "     and a.ide_inepi=cast((select valor_para from sis_parametros where nom_para = 'p_inv_estado_aprobado')as numeric)\n"
                + "order by ide_inarti,fecha_trans_incci,ide_indci ";
        //System.out.println("REPORTES >>>>> " + sql);
        return sql;
    }

    public String getActualizarBdt(String anio, String articulo) {
        String sql = "";
        sql += "update bodt_articulos set \n"
                + "	ingreso_material_boart=0,\n"
                + "	egreso_material_boart=0,\n"
                + "	costo_anterior_boart=0,\n"
                + "	costo_actual_boart=0\n"
                + "where ide_geani=" + anio + " and ide_inarti=" + articulo + " ";
        return sql;
    }

    public String getActualizarCostoInicial(String anio, String articulo) {
        String sql = "";
        sql += "update bodt_articulos set \n"
                + "	costo_actual_boart=costo_inicial_boart\n"
                + "where ide_geani=" + anio + " and ide_inarti=" + articulo + " ";
        return sql;
    }

    public String getAplicaKardex(String articulo) {
        String sql = "";
        sql += "select ide_inarti,codigo_inarti,nombre_inarti,hace_kardex_inarti from inv_articulo where ide_inarti=" + articulo + "";
        return sql;
    }

    public String getConsultaUnidadMedida(String articulo) {
        String sql = "";
        sql += "select ide_inarti,ide_inuni,codigo_inarti,nombre_inarti from inv_articulo  where ide_inarti=" + articulo + "";
        return sql;
    }

    /**
     * Permite registrar los ingreso y egresos al bodegas para generar el kardex
     *
     * @param tipo indica si aplica el inreso por sucursales y empresas
     * @param ide_incci
     * @param sucu
     * @param empr
     * @param anio
     */
    public void getRegistrarInventario(String tipo, String ide_incci, String ide_intti, String sucu, String empr, String anio) {
        TablaGenerica tab_anio = utilitario.consultar(getInventarioAnio(anio));
        TablaGenerica tab_detalle = utilitario.consultar(getDetalleInventario(tipo, ide_incci, sucu, empr));
        TablaGenerica tab_transaccion = utilitario.consultar(getConsultarTipoTransaccion(ide_intti));
        double costo_actual = 0;
        //System.out.println("ingrese  getRegistrarInventario");
        if (tab_transaccion.getValor("ide_intci").equals(utilitario.getVariable("p_inv_tipo_ingreso"))) {
            //System.out.println("ingrese  p_inv_tipo_ingreso");
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                TablaGenerica tab_kardex = utilitario.consultar(getAplicaKardex(tab_detalle.getValor(i, "ide_inarti")));
                if (tab_kardex.getValor("hace_kardex_inarti").equals("true")) {
                     //System.out.println("ingrese  p_inv_tipo_ingreso hace_kardex_inarti");
                    TablaGenerica tab_articulo = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                    if (tab_articulo.getTotalFilas() > 0) {
                        //System.out.println("hay stockcccc hace_kardex_inarti");
                        costo_actual = getPrecioPonderado(Double.parseDouble(tab_articulo.getValor("stock")), Double.parseDouble(tab_articulo.getValor("costo_actual_boart")), Double.parseDouble(tab_detalle.getValor(i, "cantidad_indci")), Double.parseDouble(tab_detalle.getValor(i, "valor_indci")));
                        utilitario.getConexion().ejecutarSql(getActualizarBodegaArticulos(tab_articulo.getValor("costo_actual_boart"), costo_actual, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        utilitario.getConexion().ejecutarSql(getActualizarIngreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                        utilitario.getConexion().ejecutarSql(getActualizarDetalleStock(tab_arti2.getValor("stock"), costo_actual, tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), ide_incci));
                    } else {
                         //System.out.println("no hyyhay stock hace_kardex_inarti");
                        utilitario.getConexion().ejecutarSql(getInsertarBodegaArticulos(tipo, tab_anio.getValor("ide_geani"), sucu, empr, tab_detalle.getValor(i, "ide_inarti")));
                        TablaGenerica tab_articulos = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                        costo_actual = getPrecioPonderado(Double.parseDouble(tab_articulos.getValor("stock")), Double.parseDouble(tab_articulos.getValor("costo_actual_boart")), Double.parseDouble(tab_detalle.getValor(i, "cantidad_indci")), Double.parseDouble(tab_detalle.getValor(i, "valor_indci")));
                        utilitario.getConexion().ejecutarSql(getActualizarBodegaArticulos(tab_articulos.getValor("costo_actual_boart"), costo_actual, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        utilitario.getConexion().ejecutarSql(getActualizarIngreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                        utilitario.getConexion().ejecutarSql(getActualizarDetalleStock(tab_arti2.getValor("stock"), costo_actual, tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), ide_incci));
                    }
                } else {
                        utilitario.getConexion().ejecutarSql(getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), ide_incci));
                }
            }
        } else if (tab_transaccion.getValor("ide_intci").equals(utilitario.getVariable("p_inv_tipo_egreso"))) {
            //System.out.println("ingrese  p_inv_tipo_egreso total = "+tab_detalle.getTotalFilas());
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                TablaGenerica tab_kardex = utilitario.consultar(getAplicaKardex(tab_detalle.getValor(i, "ide_inarti")));
                //System.out.println("hace hace_kardex_inartippppp "+ tab_kardex.getValor("hace_kardex_inarti"));
                if (tab_kardex.getValor("hace_kardex_inarti").equals("true")) {
                    //System.out.println("ingrese  p_inv_tipo_egreso hace_kardex_inarti");
                    TablaGenerica tab_articulo = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                    if (tab_articulo.getTotalFilas() > 0) {
                        //System.out.println("hay stock hace_kardex_inarti");
                        utilitario.getConexion().ejecutarSql(getActualizarEgreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                        utilitario.getConexion().ejecutarSql(getActualizarDetalleStock(tab_arti2.getValor("stock"), Double.parseDouble(tab_arti2.getValor("costo_actual_boart")), tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), ide_incci));

                    } else {
                        //System.out.println("nooo hay stock hace_kardex_inarti");
                        utilitario.getConexion().ejecutarSql(getInsertarBodegaArticulos(tipo, tab_anio.getValor("ide_geani"), sucu, empr, tab_detalle.getValor(i, "ide_inarti")));
                        TablaGenerica tab_articulos = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                        utilitario.getConexion().ejecutarSql(getActualizarEgreso(tab_detalle.getValor(i, "cantidad_indci"), tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani")));
                        TablaGenerica tab_arti2 = utilitario.consultar(getBodtArticulo(tipo, tab_detalle.getValor(i, "ide_inarti"), tab_anio.getValor("ide_geani"), sucu, empr));
                        utilitario.getConexion().ejecutarSql(getActualizarDetalleStock(tab_arti2.getValor("stock"), Double.parseDouble(tab_arti2.getValor("costo_actual_boart")), tab_detalle.getValor(i, "ide_indci"), tab_detalle.getValor(i, "ide_inarti")));
                        utilitario.getConexion().ejecutarSql(getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), ide_incci));
                    }
                } else {
                    utilitario.getConexion().ejecutarSql(getActualizarEstadoInventario(utilitario.getVariable("p_inv_estado_aprobado"), ide_incci));
                }
            }
        }
    }

    /**
     * Medoto que permite recalcular los ingresos y egresos de un articulo
     *
     * @param tipo 1 = todas las bodegas 0=una solo bodega
     * @param anio
     * @param ide_inarti
     * @param sucu
     * @param empr
     */
    public void getRecalcularInventario(String tipo, String anio, String ide_inarti, String sucu, String empr) {
        //System.out.println("SQL RECALCULAR ANIO>>>> " + anio + "  articulo >> " + ide_inarti);
        TablaGenerica tab_consulta = utilitario.consultar(getReporteOrdenado(anio, ide_inarti));
        double costo_actual = 0;
        utilitario.getConexion().ejecutarSql(getActualizarBdt(anio, ide_inarti));
        utilitario.getConexion().ejecutarSql(getActualizarCostoInicial(anio, ide_inarti));
        for (int j = 0; j < tab_consulta.getTotalFilas(); j++) {
            if (tab_consulta.getValor(j, "ide_intci") != null) {
                if (tab_consulta.getValor(j, "ide_intci").equals(utilitario.getVariable("p_inv_tipo_ingreso"))) {
                    TablaGenerica tab_articulo = utilitario.consultar(getBodtArticulo(tipo, tab_consulta.getValor(j, "ide_inarti"), anio, sucu, empr));
                    if (tab_articulo.getTotalFilas() > 0) {
                        costo_actual = getPrecioPonderado(Double.parseDouble(tab_articulo.getValor("stock")), Double.parseDouble(tab_articulo.getValor("costo_actual_boart")), Double.parseDouble(tab_consulta.getValor(j, "cantidad_indci")), Double.parseDouble(tab_consulta.getValor(j, "valor_indci")));
                        utilitario.getConexion().ejecutarSql(getActualizarBodegaArticulos(tab_articulo.getValor("costo_actual_boart"), costo_actual, tab_consulta.getValor(j, "ide_inarti"), anio));
                        utilitario.getConexion().ejecutarSql(getActualizarIngreso(tab_consulta.getValor(j, "cantidad_indci"), tab_consulta.getValor(j, "ide_inarti"), anio));
                        TablaGenerica tab_arti2 = utilitario.consultar(getBodtArticulo(tipo, tab_consulta.getValor(j, "ide_inarti"), anio, sucu, empr));
                        utilitario.getConexion().ejecutarSql(getActualizarDetalleStock(tab_arti2.getValor("stock"), costo_actual, tab_consulta.getValor(j, "ide_indci"), tab_consulta.getValor(j, "ide_inarti")));
                    }

                } else if (tab_consulta.getValor(j, "ide_intci").equals(utilitario.getVariable("p_inv_tipo_egreso"))) {

                    TablaGenerica tab_articulo = utilitario.consultar(getBodtArticulo(tipo, tab_consulta.getValor(j, "ide_inarti"), anio, sucu, empr));
                    if (tab_articulo.getTotalFilas() > 0) {
                        utilitario.getConexion().ejecutarSql(getActualizarEgreso(tab_consulta.getValor(j, "cant_egre"), tab_consulta.getValor(j, "ide_inarti"), anio));
                        TablaGenerica tab_arti2 = utilitario.consultar(getBodtArticulo(tipo, tab_consulta.getValor(j, "ide_inarti"), anio, sucu, empr));
                        utilitario.getConexion().ejecutarSql(getActualizarDetalleStock(tab_arti2.getValor("stock"), Double.parseDouble(tab_arti2.getValor("costo_actual_boart")), tab_consulta.getValor(j, "ide_indci"), tab_consulta.getValor(j, "ide_inarti")));
                    }
                }
            }
        }

    }

    public String getDetalleTransacciones(String fecha_inicio, String fecha_fin, String transaccion) {
        String sql = "";
        sql += "select a.ide_incci, (numero_incci) as secuencial, (referencia_incci) as num_factura, (nom_geper) as provedor_distribuidor, (fecha_trans_incci) as fecha_transaccion, apellido_paterno_gtemp || ' ' || apellido_materno_gtemp || ' ' || primer_nombre_gtemp || ' ' || segundo_nombre_gtemp as responsable,  \n"
                + "total, nombre_intti, nombre_intci, (gth_ide_gtemp) as empleado_recibe\n"
                + "from inv_cab_comp_inve a  \n"
                + "left join gen_persona b on a.ide_geper = b.ide_geper\n"
                + "left join gth_empleado c on a.ide_gtemp = c.ide_gtemp \n"
                + "left join inv_tip_tran_inve d on a.ide_intti = d.ide_intti\n"
                + "left join inv_tip_comp_inve e on d.ide_intci = e.ide_intci\n"
                + "left join (\n"
                + "select ide_incci,sum(valor_indci) as total\n"
                + " from inv_det_comp_inve\n"
                + " group by ide_incci\n"
                + ") f on a.ide_incci= f.ide_incci\n"
                + "where fecha_trans_incci between cast('" + fecha_inicio + "' as date) and cast ('" + fecha_fin + "' as date) and a.ide_intti in(" + transaccion + ")";
        return sql;
    }
    public String getSqlInventarioContabilizaFecha(String fecha_inicial,String fecha_final,String tipo){
        String sql="";
        if(tipo.equals("1")){
            sql+="select ide_cndpc,ide_gelua, sum(debe) as debe,sum(haber) as haber from (";
        }
        if(tipo.equals("2")){
            sql+="select ide_inarti as ide_cndpc,nombre_inarti, sum(debe) as debe,sum(haber) as haber from (";
        }
        sql+="select a.ide_incci,ide_indci,numero_incci,observacion_incci,b.ide_inarti,valor_indci as valor_venta,fecha_trans_incci,ide_gelua,\n" +
"(case when ide_gelua = 0 then (precio_promedio_indci*cantidad_indci) else 0 end) as haber,(case when ide_gelua = 1 then (precio_promedio_indci*cantidad_indci) else 0 end) as debe,\n" +
"nombre_inarti,c.ide_cndpc\n" +
"from inv_cab_comp_inve a,inv_det_comp_inve b,inv_asiento_inventario c,inv_articulo d\n" +
"where a.ide_incci=b.ide_incci and b.ide_inarti=c.ide_inarti and b.ide_inarti=d.ide_inarti\n" +
"and ide_intti= 29\n" +
"and fecha_trans_incci between '"+fecha_inicial+"' and '"+fecha_final+"'\n" +
"order by fecha_trans_incci desc";
        if(tipo.equals("1")){
            sql+=") a group by ide_cndpc,ide_gelua";
        }
        if(tipo.equals("2")){
            sql+=") a group by ide_inarti,nombre_inarti having (sum(debe)-sum(haber)) != 0";
        }
        return sql;
    }
    public String getSqlInventarioContabilizar(String anio,String tipo){
        String sql="";
        if(tipo.equals("2")){
            sql+=" select 1 as ide_cndpc, nombre_inarti,sum(debe) as debe,sum(haber) as haber from ( ";
        }
        sql+="select ide_cndpc, ide_gelua,nombre_inarti,sum(debe) as debe,sum(haber) as haber from ( \n" +
"                            select a.ide_gelua,a.ide_cndpc,b.ide_inarti,ide_geani, nombre_inarti,\n" +
"                            (case when a.ide_gelua = 0 then costo_actual_boart else 0 end) as haber,(case when a.ide_gelua = 1 then costo_actual_boart else 0 end) as debe \n" +
"                            from inv_asiento_inventario a,bodt_articulos b,inv_articulo c\n" +
"                            where a.ide_inarti = b.ide_inarti and b.ide_inarti=c.ide_inarti\n" +
"                           and costo_actual_boart >0\n" +
"                            and ide_geani in ("+anio+")\n" +
"                            ) a \n" +
"                            group by ide_gelua,ide_cndpc,nombre_inarti";
        if(tipo.equals("2")){
            sql+=" ) a group by nombre_inarti having (sum(debe)-sum(haber)) != 0";
        }
        //System.out.println(" consultando inventarios contable "+sql);
        return sql;
        
    }
    public String getSqlCostoVenta(String fecha_inicial,String fecha_final){
        String sql="";
        sql+="select a.ide_inarti,a.nom_geani,a.nombre_inarti,a.codigo_inarti,a.existencia_inicial_boart,a.costo_inicial_boart,precio,\n" +
"cantidad_indci,precio_indci,valor_indci,cant_egre,prec_egre,valor_egre,(case when cantidad1_indci is null then a.existencia_inicial_boart else cantidad1_indci end) as cantidad1_indci,cant_egre*a.costo_inicial_boart as costo_venta,\n" +
"(case when precio_promedio_indci is null then  a.costo_inicial_boart else precio_promedio_indci end) as precio_promedio_indci,\n" +
"	fecha_trans_incci,numero_incci\n" +
"from (\n" +
"select a.ide_inarti,a.nombre_inarti,b.existencia_inicial_boart,b.costo_inicial_boart,c.nom_geani,c.ide_geani,codigo_inarti\n" +
"from inv_articulo a,bodt_articulos b,gen_anio c\n" +
"where a.ide_inarti=b.ide_inarti\n" +
"and b.ide_geani=c.ide_geani\n" +
") a\n" +
"left join (\n" +
"select b.ide_intci,e.ide_geani,nom_geani,e.ide_inarti,codigo_inarti,nombre_inarti,existencia_inicial_boart,\n" +
"	costo_inicial_boart,0 as precio,cantidad_indci,precio_indci,valor_indci,0 as cant_egre,0 as prec_egre,0 as valor_egre,cantidad1_indci,precio_promedio_indci,\n" +
"	fecha_trans_incci,numero_incci,ide_indci\n" +
"	from inv_cab_comp_inve a\n" +
"	left join inv_tip_tran_inve b on a.ide_intti=b.ide_intti\n" +
"	left join inv_tip_comp_inve c on b.ide_intci=c.ide_intci\n" +
"	left join inv_det_comp_inve d on a.ide_incci=d.ide_incci\n" +
"	left join bodt_articulos e on d.ide_inarti=e.ide_inarti\n" +
"	left join inv_articulo f on e.ide_inarti=f.ide_inarti\n" +
"	left join gen_anio g on e.ide_geani=g.ide_geani\n" +
"	where b.ide_intci =cast((select valor_para from sis_parametros where nom_para = 'p_inv_tipo_ingreso')as numeric)\n" +
"	and fecha_trans_incci between '"+fecha_inicial+"' and '"+fecha_final+"'\n" +
"\n" +
"\n" +
"union\n" +
"	select b.ide_intci,e.ide_geani,nom_geani,e.ide_inarti,codigo_inarti,nombre_inarti,existencia_inicial_boart,costo_inicial_boart,\n" +
"	precio_promedio_indci as precio,0 as cant_egre,0 as prec_egre,0 as valor_egre,cantidad_indci,precio_indci,valor_indci,cantidad1_indci,precio_promedio_indci,\n" +
"	fecha_trans_incci,numero_incci,ide_indci\n" +
"	from inv_cab_comp_inve a\n" +
"	left join inv_tip_tran_inve b on a.ide_intti=b.ide_intti\n" +
"	left join inv_tip_comp_inve c on b.ide_intci=c.ide_intci\n" +
"	left join inv_det_comp_inve d on a.ide_incci=d.ide_incci\n" +
"	left join bodt_articulos e on d.ide_inarti=e.ide_inarti\n" +
"	left join inv_articulo f on e.ide_inarti=f.ide_inarti\n" +
"	left join gen_anio g on e.ide_geani=g.ide_geani\n" +
"	where b.ide_intci =cast((select valor_para from sis_parametros where nom_para = 'p_inv_tipo_egreso')as numeric)\n" +
"	and fecha_trans_incci between '"+fecha_inicial+"' and '"+fecha_final+"'\n" +
"\n" +
") b on a.ide_geani= b.ide_geani and a.ide_inarti = b.ide_inarti\n" +
"where a.nom_geani= extract(year from cast('"+fecha_inicial+"' as date))||''\n" +
"order by a.ide_inarti,fecha_trans_incci,ide_indci";
        return sql;
    }
}
