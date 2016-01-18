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
     * Retorna la sentencia SQL con los puntos de emision de facturas x sucursal
     *
     * @return
     */
    public String getSqlPuntosEmision() {
        return "select ide_ccdaf,serie_ccdaf, autorizacion_ccdaf,observacion_ccdaf from cxc_datos_fac where ide_sucu=" + utilitario.getVariable("IDE_SUCU");
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
                e.printStackTrace();
            }
        }
        max++;
        return max;
    }

}
