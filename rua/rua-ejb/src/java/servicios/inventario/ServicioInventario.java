/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.inventario;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ServicioInventario {

    private final Utilitario utilitario = new Utilitario();

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
        tab_cab_comp_inv.setValor("ide_inbod", "1");   ///variable para bodega por defecto
        tab_cab_comp_inv.setValor("numero_incci", getSecuencialComprobanteInventario());  /// calcular numero de comprobante de inventario
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
            tab_det_comp_inv.setValor("cantidad_indci", tab_detalle.getValor(i, "cantidad_ccdfa"));
            tab_det_comp_inv.setValor("precio_indci", tab_detalle.getValor(i, "precio_ccdfa"));
            tab_det_comp_inv.setValor("valor_indci", tab_detalle.getValor(i, "total_ccdfa"));
            tab_det_comp_inv.setValor("observacion_indci", tab_detalle.getValor(i, "observacion_ccdfa"));

        }
        tab_det_comp_inv.guardar();
    }

    /**
     * Genera secuencial de comprobante
     *
     * @return
     */
    public String getSecuencialComprobanteInventario() {

        List list = utilitario.getConexion().consultar("SELECT max(NUMERO_INCCI) FROM inv_cab_comp_inve "
                + "WHERE ide_sucu=" + utilitario.getVariable("ide_sucu") + ""
                + "AND ide_empr=" + utilitario.getVariable("ide_empr"));
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

}
