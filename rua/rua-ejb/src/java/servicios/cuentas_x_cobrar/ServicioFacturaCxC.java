/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless
public class ServicioFacturaCxC {

    private final Utilitario utilitario = new Utilitario();

    /**
     * Retorna la sentencia SQL con los puntos de emision de facturas x empresa
     *
     * @return
     */
    public String getSqlPuntosEmision() {
        return "select ide_ccdaf,serie_ccdaf, autorizacion_ccdaf,observacion_ccdaf from cxc_datos_fac where ide_empr=" + utilitario.getVariable("IDE_EMPR");
    }

    /**
     * Retorna el secuencial maximo de un punto de emisión
     *
     * @param ide_ccdaf
     * @return
     */
    public int getSecuencialFactura(String ide_ccdaf) {
        int max = 0;
        TablaGenerica tab_secuencia = utilitario.consultar("select ide_ccdaf,MAX(secuencial_cccfa) as num_max FROM cxc_cabece_factura where ide_ccdaf=" + ide_ccdaf + " GROUP BY ide_ccdaf ");
        if (tab_secuencia.isEmpty() == false) {
            try {
                max = Integer.parseInt(tab_secuencia.getValor("num_max"));
            } catch (Exception e) {
            }
        }
        max++;
        return max;
    }

    /**
     * Retorna todas las facturas generadas en un punto de emisión x sucursal
     *
     * @param ide_ccdaf Punto de Emisión
     * @param fechaInicio Fecha desde
     * @param fechaFin Fecha hasta
     * @return
     */
    public String getSqlFacturas(String ide_ccdaf, String fechaInicio, String fechaFin) {
        return "select a.ide_cccfa, secuencial_cccfa, a.ide_ccefa,nombre_ccefa ,fecha_emisi_cccfa,nom_geper,identificac_geper,base_grabada_cccfa as ventas12,"
                + "base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,valor_iva_cccfa,total_cccfa, "
                + "observacion_cccfa, fecha_trans_cccfa,ide_cnccc "
                + "from cxc_cabece_factura a "
                + "inner join gen_persona b on a.ide_geper=b.ide_geper "
                + "inner join cxc_estado_factura c on  a.ide_ccefa=c.ide_ccefa "
                + "where fecha_emisi_cccfa BETWEEN  '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and ide_ccdaf=" + ide_ccdaf + " "
                + " and a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "ORDER BY secuencial_cccfa desc,ide_cccfa desc";
    }

    /**
     * Retorna los pagos realizados a una Factura
     *
     * @param ide_cccfa
     * @return
     */
    public String getSqlPagosFactura(String ide_cccfa) {
        return "SELECT ide_ccdtr,fecha_trans_ccdtr,docum_relac_ccdtr,nombre_tettb ,valor_ccdtr, nombre_tecba ||' '||nombre_teban as DESTINO,observacion_ccdtr as OBSERVACION,c.ide_tecba  "
                + "FROM cxc_detall_transa a "
                + "left JOIN  cxc_tipo_transacc b on a.IDE_CCTTR =b.IDE_CCTTR "
                + "left join  tes_cab_libr_banc c on a.ide_teclb=c.ide_teclb "
                + "left join tes_cuenta_banco  d on c.ide_tecba=d.ide_tecba "
                + "left join tes_banco e on d.ide_teban=e.ide_teban "
                + "left join tes_tip_tran_banc f on c.ide_tettb=f.ide_tettb "
                + "where numero_pago_ccdtr > 0 "
                + "and ide_cccfa=" + ide_cccfa + " "
                + "order by fecha_trans_ccdtr";
    }

}
