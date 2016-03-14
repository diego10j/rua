/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.tesoreria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.contabilidad.ServicioContabilidad;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless

public class ServicioTesoreria {

    @EJB
    private ServicioContabilidad ser_tesoreria;

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
     * Retorna todas las cuentas : cajas y bancos
     *
     * @return
     */
    public String getSqlComboCuentas() {
        return "select ide_tecba,nombre_tecba,nombre_teban from tes_banco a "
                + "inner join tes_cuenta_banco b on a.ide_teban=b.ide_teban "
                + "where a.ide_empr=" + utilitario.getVariable("ide_empr") + " "
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
        return "select fecha_trans_teclb,numero_teclb,nombre_tettb,beneficiari_teclb,"
                + "case when signo_tettb = 1 THEN valor_teclb  end as INGRESOS,case when signo_tettb = -1 THEN valor_teclb end as EGRESOS, '' SALDO,observacion_teclb,ide_cnccc,ide_teclb "
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

    /**
     * Retorna el tipo de transaccion bancaria dependiendo la forma de pago de
     * cuentas por cobrar
     *
     * @param ide_cndfp Forma de Pago
     * @return
     */
    public String getTipoTransaccionCxC(String ide_cndfp) {
        if (ide_cndfp != null) {
            int dias = ser_tesoreria.getDiasFormaPago(ide_cndfp);
            //PAGO EFECTIVO
            if (dias == 0) {
                if (utilitario.getVariable("p_con_for_pag_efec") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_efec"))) {
                    return utilitario.getVariable("p_tes_tran_deposito_caja");
                } else if (utilitario.getVariable("p_con_for_pag_cheque") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_cheque"))) {
                    return utilitario.getVariable("p_tes_tran_deposito_caja");
                } else if (utilitario.getVariable("p_con_for_pag_transferencia") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_transferencia"))) {
                    return utilitario.getVariable("p_tes_tran_transferencia_mas");
                } else if (utilitario.getVariable("p_con_for_pag_deposito") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_deposito"))) {
                    return utilitario.getVariable("p_tes_tran_deposito");
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

    /**
     * Retorna el tipo de transaccion bancaria dependiendo la forma de pago de
     * cuentas por pagar
     *
     * @param ide_cndfp Forma de Pago
     * @return
     */
    public String getTipoTransaccionCxP(String ide_cndfp) {
        //Retorna el tipo de transaccion dependiendo la forma de pago
        if (ide_cndfp != null) {
            int dias = ser_tesoreria.getDiasFormaPago(ide_cndfp);
            //PAGO EFECTIVO
            if (dias == 0) {
                if (utilitario.getVariable("p_con_for_pag_efec") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_efec"))) {
                    return utilitario.getVariable("p_tes_tran_retiro_caja");
                } else if (utilitario.getVariable("p_con_for_pag_cheque") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_cheque"))) {
                    return utilitario.getVariable("p_tes_tran_cheque");
                } else if (utilitario.getVariable("p_con_for_pag_transferencia") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_transferencia"))) {
                    return utilitario.getVariable("p_tes_tran_transferencia_menos");
                } //else if (utilitario.getVariable("p_con_for_pag_deposito") != null && ide_cndfp.equalsIgnoreCase(utilitario.getVariable("p_con_for_pag_deposito"))) {
                //    return utilitario.getVariable("p_tes_tran_cheque");
                //} 
                else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * Genera una transaccion en bancos/caja de un pago de una factura CxC
     *
     * @param tab_cab_factura_cxc Cabecera de Factura Cxc
     * @param ide_tecba Cuenta Destino
     * @param valor Valor
     * @param observacion Observación
     * @param numero Numero de Documento Relacionado
     * @param fechaTransaccion Fecha de Pago
     * @return ide_teclb
     */
    public String generarPagoFacturaCxC(Tabla tab_cab_factura_cxc, String ide_tecba, double valor, String observacion, String numero, String fechaTransaccion) {
        if (numero == null || numero.isEmpty()) {
            numero = tab_cab_factura_cxc.getValor("secuencial_cccfa");
        }

        if (observacion == null || observacion.isEmpty()) {
            observacion = "V/. Pago Factura " + tab_cab_factura_cxc.getValor("secuencial_cccfa");
        }

        if (fechaTransaccion == null || fechaTransaccion.isEmpty()) {
            fechaTransaccion = utilitario.getFormatoFecha(tab_cab_factura_cxc.getValor("fecha_emisi_cccfa"));
        }

        long ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb", 1);
        String sql = "INSERT INTO tes_cab_libr_banc (ide_teclb, ide_tecba, "
                + "ide_sucu, ide_tettb, ide_teelb, ide_empr, ide_cnccc, valor_teclb, numero_teclb, "
                + "fecha_trans_teclb, fecha_venci_teclb, fec_cam_est_teclb, conciliado_teclb, "
                + "beneficiari_teclb, observacion_teclb) "
                + "VALUES (" + ide_teclb + ", " + ide_tecba + ", " + utilitario.getVariable("ide_empr") + ",  "
                + "" + getTipoTransaccionCxC(tab_cab_factura_cxc.getValor("ide_cndfp")) + ", " + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
                + "" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", " + tab_cab_factura_cxc.getValor("ide_cnccc") + ", "
                + "" + utilitario.getFormatoNumero(valor) + ", '" + numero + "', '" + fechaTransaccion + "', "
                + "'" + fechaTransaccion + "', "
                + "NULL, false, '" + getPersona(tab_cab_factura_cxc.getValor("ide_geper")).getValor("nom_geper") + "', '" + observacion + "')";
        utilitario.getConexion().agregarSqlPantalla(sql);
        return String.valueOf(ide_teclb);
    }

    /**
     * Genera una transaccion en bancos/caja de un pago de una factura CxC
     *
     * @param tab_cab_factura_cxc Factura
     * @param ide_tecba Cuenta Banco-Cajsa
     * @param valor Valor
     * @param numero Numero de Documento
     * @param fechaTransaccion Fecha de Transaccion
     * @param tipoTransaccion Tipo de Transaccion Tesoreria
     *
     * @return ide_teclb generado
     */
    public String generarPagoFacturaCxC(TablaGenerica tab_cab_factura_cxc, String ide_tecba, double valor, String numero, String fechaTransaccion, String tipoTransaccion) {
        //Si noy numero de documento, asigna el secuencial de la factura
        if (numero == null || numero.isEmpty()) {
            numero = tab_cab_factura_cxc.getValor("secuencial_cccfa");
        }

        String observacion = "V/. Pago Factura " + tab_cab_factura_cxc.getValor("secuencial_cccfa");
        //Si no hay fecha de pago, se asigna la fecha de la factura
        if (fechaTransaccion == null || fechaTransaccion.isEmpty()) {
            fechaTransaccion = utilitario.getFormatoFecha(tab_cab_factura_cxc.getValor("fecha_emisi_cccfa"));
        }
        //Si tipo transaccion, busca un tipo de transaccion cxc
        if (tipoTransaccion == null || tipoTransaccion.isEmpty()) {
            tipoTransaccion = getTipoTransaccionCxC(tab_cab_factura_cxc.getValor("ide_cndfp"));
        }

        long ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb", 1);
        String sql = "INSERT INTO tes_cab_libr_banc (ide_teclb, ide_tecba, "
                + "ide_sucu, ide_tettb, ide_teelb, ide_empr,  valor_teclb, numero_teclb, "
                + "fecha_trans_teclb, fecha_venci_teclb, fec_cam_est_teclb, conciliado_teclb, "
                + "beneficiari_teclb, observacion_teclb) "
                + "VALUES (" + ide_teclb + ", " + ide_tecba + ", " + utilitario.getVariable("ide_empr") + ",  "
                + "" + tipoTransaccion + ", " + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
                + "" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
                + "" + utilitario.getFormatoNumero(valor) + ", '" + numero + "', '" + fechaTransaccion + "', "
                + "'" + fechaTransaccion + "', "
                + "NULL, false, '" + getPersona(tab_cab_factura_cxc.getValor("ide_geper")).getValor("nom_geper") + "', '" + observacion + "')";
        utilitario.getConexion().agregarSqlPantalla(sql);
        return String.valueOf(ide_teclb);
    }

    /**
     * Retorna lps datos de Una Persona
     *
     * @param ide_geper
     * @return
     */
    public TablaGenerica getPersona(String ide_geper) {
        return utilitario.consultar("select * from gen_persona where ide_geper=" + ide_geper);

    }

    /**
     * Retorna las transacciones con signo positivo (aumentan libro caja bancos)
     * INGRESOS
     *
     * @return
     */
    public String getSqlTipoTransaccionPositivo() {
        return "select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "and signo_tettb=1";
    }

    /**
     * Retorna las transacciones con signo negativo (disminuyen libro caja
     * bancos) EGRESOS
     *
     * @return
     */
    public String getSqlTipoTransaccionNegativo() {
        return "select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "and signo_tettb=-1";
    }

    /**
     *
     * @param ide_tecba Cuenta
     * @param ide_tettb Tipo de transacción
     * @return
     */
    public String getNumMaximoTipoTransaccion(String ide_tecba, String ide_tettb) {
        String maximo = "";
        if (ide_tecba == null || ide_tecba.isEmpty()) {
            ide_tecba = "-1";
        }
        if (ide_tettb == null || ide_tettb.isEmpty()) {
            ide_tettb = "-1";
        }
        String str_ide_ttr_nota_debito = utilitario.getVariable("p_tes_nota_debito");
        String str_ide_ttr_nota_credito = utilitario.getVariable("p_tes_nota_credito");
        String str_ide_ttr_cheque = utilitario.getVariable("p_tes_tran_cheque");
        if (ide_tettb.equals(str_ide_ttr_nota_debito)) {
//       calculo el maximo de las notas de debito
            List lis_max = utilitario.getConexion().consultar("SELECT max(numero_teclb) from tes_cab_libr_banc where ide_tettb=" + str_ide_ttr_nota_debito + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_tecba=" + ide_tecba);
            maximo = "000000001";
            if (lis_max.get(0) != null) {
                try {
                    maximo = ((Integer.parseInt(lis_max.get(0).toString())) + 1) + "";
                    maximo = utilitario.generarCero(9 - maximo.length()) + maximo;

                } catch (Exception e) {
                }
            }
        } else if (ide_tettb.equals(str_ide_ttr_nota_credito)) {
//       calculo el maximo de las notas de credito
            List lis_max = utilitario.getConexion().consultar("SELECT max(numero_teclb) from tes_cab_libr_banc where ide_tettb=" + str_ide_ttr_nota_credito + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_tecba=" + ide_tecba);
            maximo = "000000001";
            if (lis_max.get(0) != null) {
                try {
                    maximo = ((Integer.parseInt(lis_max.get(0).toString())) + 1) + "";
                    maximo = utilitario.generarCero(9 - maximo.length()) + maximo;
                } catch (Exception e) {
                }
            }
        } else if (ide_tettb.equals(str_ide_ttr_cheque)) {
//       calculo el maximo de los cheques
            if (ide_tecba.equals("-1")) {
                maximo = "";
            }
            List lis_max = utilitario.getConexion().consultar("select MAX(numero_teclb) from tes_cab_libr_banc where ide_tettb=" + str_ide_ttr_cheque + " and ide_tecba=" + ide_tecba);

            if (lis_max.get(0) != null) {
                try {
                    maximo = ((Integer.parseInt(lis_max.get(0).toString())) + 1) + "";
                } catch (Exception e) {
                }
            }
            if (ide_tecba.equals("-1")) {
                maximo = "";
            }

        }
        return maximo;
    }

}
