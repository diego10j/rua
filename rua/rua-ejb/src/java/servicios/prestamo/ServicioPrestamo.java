/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.prestamo;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ServicioPrestamo extends ServicioBase {

    public String getSqlComboPrestamos() {
        return "SELECT ide_ipcpr,observacion_ipcpr,num_prestamo_ipcpr,identificac_geper,num_asiento_ant_sistema\n"
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
                + "num_prestamo_ipcpr,observacion_ipcpr as nom_geper,a.monto_ipcpr, interes_ipcpr,num_pagos_ipcpr,\n"
                + "(select sum(capital_ipdpr) as capital from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select sum(interes_ipdpr) as interes from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select sum(capital_ipdpr) as SALDO from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=false),\n"
                + "(select sum(cuota_ipdpr) as cuota from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select count(pagado_ipdpr) as pagos from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true),  \n"
                + "(select coalesce(sum(cuota_ipdpr),0) from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true) as valor_pagado,\n"
                + "(select max(fecha_ipdpr) from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true) as fecha_ultimo_pago\n"
                + "from  iyp_cab_prestamo a\n"
                + "left join gen_persona b on a.ide_geper=b.ide_geper\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "order by fecha_prestamo_ipcpr desc";
    }

    public String getSqlPrestamosCliente(String ide_geper) {

        return "select a.ide_ipcpr,a.fecha_prestamo_ipcpr,case when es_ingreso_ipcpr = true THEN 'INGRESO' else 'EGRESO'  end as tipo,\n"
                + "num_prestamo_ipcpr,a.monto_ipcpr, interes_ipcpr,num_pagos_ipcpr,\n"
                + "(select sum(capital_ipdpr) as capital from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select sum(interes_ipdpr) as interes from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select sum(capital_ipdpr) as SALDO from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=false),\n"
                + "(select sum(cuota_ipdpr) as cuota from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr),\n"
                + "(select count(pagado_ipdpr) as pagos from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true),  \n"
                + "(select coalesce(sum(cuota_ipdpr),0) from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true) as valor_pagado,\n"
                + "(select max(fecha_ipdpr) from iyp_deta_prestamo where ide_ipcpr=a.ide_ipcpr and pagado_ipdpr=true) as fecha_ultimo_pago\n"
                + "from  iyp_cab_prestamo a\n"
                + "WHERE a.ide_empr=" + utilitario.getVariable("ide_empr") + "\n"
                + "and ide_geper=" + ide_geper + " "
                + "order by fecha_prestamo_ipcpr desc";
    }

    public String getSqlPagosPrestamo(String ide_ipcpr) {
        return "SELECT dp.ide_ipdpr,dp.ide_ipcpr,dp.num_ipdpr,dp.fecha_ipdpr,dp.capital_ipdpr,dp.interes_ipdpr,dp.cuota_ipdpr,dp.pagado_ipdpr,cp.ide_cndpc,fecha_prestamo_ipcpr,monto_ipcpr,num_dias_ipcpr,interes_ipcpr,ide_geper "
                + "FROM iyp_deta_prestamo dp,iyp_cab_prestamo cp WHERE dp.ide_ipcpr=" + ide_ipcpr + " "
                + "AND dp.pagado_ipdpr is FALSE and dp.ide_empr=" + utilitario.getVariable("ide_empr") + " "
                + "and cp.ide_ipcpr=dp.ide_ipcpr "
                + "ORDER BY dp.num_ipdpr,dp.fecha_ipdpr ASC";
    }

    public String getSqlPagarDividendos(String ide_ipcpr, String ide_cccfa) {
        if (ide_cccfa == null || ide_cccfa.isEmpty()) {
            return "update iyp_deta_prestamo set pagado_ipdpr=true where ide_ipdpr in (" + ide_ipcpr + ")";
        } else {
            return "update iyp_deta_prestamo set pagado_ipdpr=true,ide_cccfa=" + ide_cccfa + " where ide_ipdpr in (" + ide_ipcpr + ")";
        }

    }

    public void generarTablaAmortizacion(Tabla tab_tabla1) {
        double monto = 0;
        double tasa_interes_anual = 0;
        double tasa_interes_mensual = 0;
        double num_pagos = 0;
        double num_dias = 0;
        String fecha = "";
        if (tab_tabla1.getValor("monto_ipcpr") != null && !tab_tabla1.getValor("monto_ipcpr").isEmpty()) {
            monto = Double.parseDouble(tab_tabla1.getValor("monto_ipcpr"));
        }
        if (tab_tabla1.getValor("interes_ipcpr") != null && !tab_tabla1.getValor("interes_ipcpr").isEmpty()) {
            tasa_interes_anual = Double.parseDouble(tab_tabla1.getValor("interes_ipcpr"));
        }
        if (tab_tabla1.getValor("num_pagos_ipcpr") != null && !tab_tabla1.getValor("num_pagos_ipcpr").isEmpty()) {
            num_pagos = Double.parseDouble(tab_tabla1.getValor("num_pagos_ipcpr"));
        }
        if (tab_tabla1.getValor("num_dias_ipcpr") != null && !tab_tabla1.getValor("num_dias_ipcpr").isEmpty()) {
            num_dias = Double.parseDouble(tab_tabla1.getValor("num_dias_ipcpr"));
        }
        if (tab_tabla1.getValor("fecha_prestamo_ipcpr") != null && !tab_tabla1.getValor("fecha_prestamo_ipcpr").isEmpty()) {
            fecha = tab_tabla1.getValor("fecha_prestamo_ipcpr");
        }

        System.out.println("monto " + monto);
        System.out.println("tasa " + tasa_interes_anual);
        System.out.println("num pag " + num_pagos);
        System.out.println("num dias " + num_dias);
        List lis_capital = new ArrayList();
        List lis_interes = new ArrayList();
        List lis_cuota = new ArrayList();
        List lis_fecha = new ArrayList();
        if (monto != 0) {
            if (tasa_interes_anual != 0) {
                if (num_pagos != 0) {
                    if (num_dias != 0) {
                        if (!fecha.equals("")) {
                            tasa_interes_mensual = (tasa_interes_anual / 12);
                            System.out.println("tasa de nteres mensual " + tasa_interes_mensual);
                            double aux_interes_mesual = tasa_interes_mensual / 100;
                            double aux_interes_n = 0;
                            aux_interes_n = (Math.pow((1 + aux_interes_mesual), num_pagos));
                            double cuotafija = 0;
                            cuotafija = monto * ((aux_interes_mesual * aux_interes_n) / (aux_interes_n - 1));
                            System.out.println("cuota fija " + cuotafija);
                            double plazo = num_dias / 360;
                            System.out.println("plazo " + plazo);
                            double interes100 = tasa_interes_anual / 100;
                            System.out.println("interes100 " + interes100);
                            double capital = 0;
                            double subcapital = monto;
                            double sum_capital = 0;
                            System.out.println("subcapital " + subcapital);
                            double interes = 0;

                            for (int i = 0; i < num_pagos; i++) {
                                interes = interes100 * plazo * subcapital;
                                capital = cuotafija - interes;
                                sum_capital = capital + sum_capital;
                                subcapital = monto - sum_capital;
                                fecha = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha), Integer.parseInt(tab_tabla1.getValor("num_dias_ipcpr").toString())));
                                lis_capital.add(capital);
                                lis_cuota.add(cuotafija);
                                lis_interes.add(interes);
                                lis_fecha.add(fecha);

                                System.out.println("capital:  " + capital + "   interes " + interes + "     cuota " + cuotafija);
                            }
                            TablaGenerica tab_tabla2 = new TablaGenerica();
                            tab_tabla2.setTabla("iyp_deta_prestamo", "ide_ipdpr");
                            tab_tabla2.setCondicion("ide_ipdpr=-1");
                            tab_tabla2.ejecutarSql();
                            for (int i = 0; i < lis_capital.size(); i++) {
                                tab_tabla2.insertar();
                                System.out.println("total de filas" + tab_tabla2.getTotalFilas());
                                tab_tabla2.setValor("ide_ipcpr", tab_tabla1.getValor("ide_ipcpr"));
                                tab_tabla2.setValor("capital_ipdpr", utilitario.getFormatoNumero(lis_capital.get(lis_capital.size() - i - 1)));
                                tab_tabla2.setValor("cuota_ipdpr", utilitario.getFormatoNumero(lis_cuota.get(lis_cuota.size() - i - 1)));
                                tab_tabla2.setValor("interes_ipdpr", utilitario.getFormatoNumero(lis_interes.get(lis_interes.size() - i - 1)));
                                tab_tabla2.setValor("fecha_ipdpr", lis_fecha.get(lis_fecha.size() - i - 1) + "");
                                tab_tabla2.setValor("num_ipdpr", lis_capital.size() - i + "");
                                tab_tabla2.setValor("pagado_ipdpr", "false");
                            }
                            tab_tabla2.guardar();
                        } else {
                            utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese la fecha del prestamo");
                        }
                    } else {
                        utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el numero de dias");
                    }
                } else {
                    utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el numero de pagos");
                }

            } else {
                utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el Interes ");
            }

        } else {
            utilitario.agregarMensajeError("No se puede cacular la tabla de amortización", "Ingrese el monto");
        }
    }

    public void eliminarPrestamo(String ide_ipcpr) {
        utilitario.getConexion().agregarSql("DELETE FROM iyp_deta_prestamo WHERE ide_ipcpr=" + ide_ipcpr);
        utilitario.getConexion().agregarSql("DELETE FROM iyp_cab_prestamo WHERE ide_ipcpr=" + ide_ipcpr);
    }

    /**
     * Retorna las cuotas no contabilizadas que ya se generaron factura
     *
     * @return
     */
    public String getSqCuotasNoContabilizadas() {
        return "select ide_ipdpr,a.ide_cccfa,nom_geper,secuencial_cccfa,total_cccfa,observacion_cccfa,num_prestamo_ipcpr,num_ipdpr,capital_ipdpr AS CAPITAL,interes_ipdpr AS INTERES,cuota_ipdpr AS COUTA,d.ide_geper\n"
                + "from iyp_deta_prestamo a\n"
                + "inner join iyp_cab_prestamo b on a.ide_ipcpr=b.ide_ipcpr\n"
                + "inner join cxc_cabece_factura  d on a.ide_cccfa=d.ide_cccfa\n"
                + "left join  gen_persona c on d.ide_geper=c.ide_geper\n"
                + "where a.ide_cccfa is not NULL  \n"
                + "and a.ide_cnccc is null \n"
                + "and pagado_ipdpr=true\n"
                + "order by a.ide_cccfa desc";
    }

    public String getSqMovimientosRealizados() {
        return "select ide_ipdpr,a.ide_cccfa,nom_geper,a.ide_cnccc,secuencial_cccfa,total_cccfa,observacion_cccfa,num_prestamo_ipcpr,num_ipdpr,capital_ipdpr AS CAPITAL,interes_ipdpr AS INTERES,cuota_ipdpr AS COUTA,d.ide_geper\n"
                + "from iyp_deta_prestamo a\n"
                + "inner join iyp_cab_prestamo b on a.ide_ipcpr=b.ide_ipcpr\n"
                + "inner join cxc_cabece_factura  d on a.ide_cccfa=d.ide_cccfa\n"
                + "left join  gen_persona c on d.ide_geper=c.ide_geper\n"
                + "where a.ide_cccfa is not NULL  \n"
                + "and pagado_ipdpr=true\n"
                + "order by a.ide_cccfa desc";
    }

}
