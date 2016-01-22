/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.tesoreria;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless

public class ServicioTesoreria {

    private final Utilitario utilitario = new Utilitario();

    /**
     * Retorna sentencia SQL para obtener las cuentas bancarias de una empresa,
     * para ser usada en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboCuentasBancarias() {
        return "select ide_tecba,nombre_tecba,nombre_teban from tes_banco a "
                + "inner join tes_cuenta_banco b on a.ide_teban=b.ide_teban "
                + "where a.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "and ide_tetcb !=" + utilitario.getVariable("p_tes_tipo_cuenta_banco_virtual") + " "
                + "order by nombre_teban,nombre_tecba";
    }

    /**
     * Retorna sentencia SQL para obtener las cajas de una empresa, para ser
     * usada en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboCajas() {
        return "select ide_tecba,nombre_tecba,nombre_teban from tes_banco a "
                + "inner join tes_cuenta_banco b on a.ide_teban=b.ide_teban "
                + "where a.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "and ide_tetcb =" + utilitario.getVariable("p_tes_tipo_cuenta_banco_virtual") + " "
                + "order by nombre_teban,nombre_tecba";
    }

    /**
     * Retorna sentencia SQL para obtener las transacciones de una cuenta
     * bancaria o caja, que se encuentren en estado normal en un rango de fechas
     *
     * @param ide_tecba
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlTransaccionesCuenta(String ide_tecba, String fechaInicio, String fechaFin) {
        return "select ide_teclb,fecha_trans_teclb,ide_cnccc,nombre_tettb,beneficiari_teclb,"
                + "case when signo_tettb = 1 THEN valor_teclb  end as INGRESOS,case when signo_tettb = -1 THEN valor_teclb end as EGRESOS, '' SALDO,observacion_teclb "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and ide_teelb =" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "and fecha_trans_teclb BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "order by fecha_trans_teclb,ide_teclb";
    }

    /**
     * Retorna el saldo inicial de una cuenta bancaria/caja a una determinada
     * fecha
     *
     * @param ide_tecba
     * @param fecha
     * @return
     */
    public double getSaldoInicialCuenta(String ide_tecba, String fecha) {
        double saldo = 0;
        String sql = "select ide_tecba,sum(valor_teclb * signo_tettb) as valor "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and fecha_trans_teclb < '" + fecha + "' "
                + "and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "group by ide_tecba";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor(0, "valor") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor(0, "valor"));
                } catch (Exception e) {
                }
            }
        }
        return saldo;
    }

}
