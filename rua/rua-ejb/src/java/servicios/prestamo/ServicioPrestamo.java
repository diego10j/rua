/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.prestamo;

import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ServicioPrestamo extends ServicioBase {

    public String getSqlComboPrestamos() {
        return "SELECT ide_ipcpr,nom_geper,num_prestamo_ipcpr,identificac_geper,nombre_teban,nombre_tecba\n"
                + "from  iyp_cab_prestamo a\n"
                + "left join gen_persona b on a.ide_geper=b.ide_geper\n"
                + "left join tes_cuenta_banco c on a.ide_tecba=c.ide_tecba\n"
                + "left join tes_banco d on c.ide_teban=d.ide_teban\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr");
    }

    public String getSqlTablaAmortizacion(String ide_ipcpr) {
        return "select ide_ipdpr,num_ipdpr as NUMERO,fecha_ipdpr,capital_ipdpr as CAPITAL,interes_ipdpr as INTERES,cuota_ipdpr as CUOTA,pagado_ipdpr as PAGADO,doc_banco_ipdpr as DOCUMENTO,nombre_teban || ' '|| nombre_tecba as BANCO,a.ide_cnccc,a.ide_cccfa ,\n"
                + "secuencial_cccfa as FACTURA,fecha_prestamo_ipcpr,monto_ipcpr,num_dias_ipcpr,interes_ipcpr\n"
                + "from iyp_deta_prestamo  a\n"
                + "inner join iyp_cab_prestamo b on b.ide_ipcpr=a.ide_ipcpr\n"
                + "left join tes_cuenta_banco c on a.ide_tecba=c.ide_tecba\n"
                + "left join tes_banco d on c.ide_teban=d.ide_teban\n"
                + "left join cxc_cabece_factura f on a.ide_cccfa=f.ide_cccfa \n"
                + "where a.ide_ipcpr=" + ide_ipcpr + "\n"
                + "order by num_ipdpr";
    }

    public String getSqlPrestamos() {
        return "select a.ide_ipcpr,a.fecha_prestamo_ipcpr,case when es_ingreso_ipcpr = true THEN 'INGRESO' else 'EGRESO'  end as tipo,\n"
                + "num_prestamo_ipcpr,b.nom_geper,a.monto_ipcpr, interes_ipcpr,num_pagos_ipcpr,\n"
                + "(select sum(capital_ipdpr) as capital from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select sum(interes_ipdpr) as interes from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select sum(cuota_ipdpr) as cuota from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select count(pagado_ipdpr) as pagos from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true),  \n"
                + "(select coalesce(sum(cuota_ipdpr),0) from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true) as valor_pagado,\n"
                + "(select max(fecha_ipdpr) from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true) as fecha_ultimo_pago\n"
                + "from  iyp_cab_prestamo a\n"
                + "left join gen_persona b on a.ide_geper=b.ide_geper\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "order by fecha_prestamo_ipcpr desc";
    }

    public String getSqlPagosPrestamo(String ide_ipcpr) {
        return "SELECT dp.ide_ipdpr,dp.ide_ipcpr,dp.num_ipdpr,dp.fecha_ipdpr,dp.capital_ipdpr,dp.interes_ipdpr,dp.cuota_ipdpr,dp.pagado_ipdpr,cp.ide_cndpc,fecha_prestamo_ipcpr,monto_ipcpr,num_dias_ipcpr,interes_ipcpr,ide_geper "
                + "FROM iyp_deta_prestamo dp,iyp_cab_prestamo cp WHERE dp.ide_ipcpr=" + ide_ipcpr + " "
                + "AND dp.pagado_ipdpr is FALSE and dp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "and cp.ide_ipcpr=dp.ide_ipcpr "
                + "ORDER BY dp.num_ipdpr,dp.fecha_ipdpr ASC";
    }

}
