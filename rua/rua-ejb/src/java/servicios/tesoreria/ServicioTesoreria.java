/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.tesoreria;

import framework.aplicacion.TablaGenerica;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.cuentas_x_pagar.ServicioCuentasCxP;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless

public class ServicioTesoreria {

    @EJB
    private ServicioContabilidadGeneral ser_tesoreria;

    private final Utilitario utilitario = new Utilitario();
    @EJB
    private final ServicioCuentasCxP ser_cuentas_cxp = (ServicioCuentasCxP) utilitario.instanciarEJB(ServicioCuentasCxP.class);

    /**
     * Retorna sentencia SQL para obtener las cuentas bancarias de una empresa,
     * para ser usada en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboCuentasBancarias() {
        return "select ide_tecba,nombre_tecba,nombre_teban from tes_banco a "
                + "inner join tes_cuenta_banco b on a.ide_teban=b.ide_teban "
                + "where b.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
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
                + "where b.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "order by nombre_teban,nombre_tecba";
    }

    /**
     * Retorna todas los nombres de los bancos
     *
     * @return
     */
    public String getSqlNombreBancos() {
        return "select ide_teban,nombre_teban from tes_banco  "
                + "order by nombre_teban";
    }

    public String getSqlComboCuentasTodas() {
        return "select ide_tecba,nombre_tecba,nombre_teban from tes_banco a "
                + "inner join tes_cuenta_banco b on a.ide_teban=b.ide_teban "
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
                + "where b.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
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
    public String getSqlTransaccionesCuentaNoConciliado(String ide_tecba, String fechaInicio, String fechaFin) {
        return "select fecha_trans_teclb,numero_teclb,nombre_tettb,beneficiari_teclb,"
                + "case when signo_tettb = 1 THEN valor_teclb  end as INGRESOS,case when signo_tettb = -1 THEN valor_teclb end as EGRESOS,observacion_teclb,ide_cnccc,ide_teclb "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and ide_teelb =" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "and fecha_trans_teclb BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and conciliado_teclb=false "
                + "order by fecha_trans_teclb,ide_teclb";
    }

    public boolean isMovimientoConciliado(String ide_tecba, String numero, String valor) {
        boolean existe = false;
        valor = valor.replace(",", "");
        List lis = utilitario.getConexion().consultar("select ide_teclb from tes_cab_libr_banc  "
                + "where ide_tecba=" + ide_tecba + " "
                + "and ide_teelb =" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "and numero_teclb = '" + numero + "' "
                + "and valor_teclb =" + utilitario.getFormatoNumero(valor) + " "
                + "and conciliado_teclb=false ");
        if (lis != null) {
            if (lis.isEmpty() == false) {
                if (lis.get(0) != null) {
                    existe = true;
                }
            }

        }
        return existe;
    }

    public String getSqlTransaccionesConciliarCuenta(String ide_tecba, String fechaInicio, String fechaFin) {
        return "select fecha_trans_teclb,numero_teclb,ide_cnccc,beneficiari_teclb,"
                + "valor_teclb,observacion_teclb,ide_teclb,conciliado_teclb as conciliado "
                + "from tes_cab_libr_banc a "
                + "where ide_tecba=" + ide_tecba + " "
                + "and ide_teelb =" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "and fecha_trans_teclb BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                //+ "and conciliado_teclb=false  "
                + "order by fecha_trans_teclb,ide_teclb";
    }

    public String getSqlTransaccionesEncontradasConciliarCuenta(String ide_teclb) {
        return "select fecha_trans_teclb,numero_teclb,valor_teclb,beneficiari_teclb,"
                + "observacion_teclb,ide_teclb "
                + "from tes_cab_libr_banc a "
                + "where ide_teclb in(" + ide_teclb + ")"
                + "order by fecha_trans_teclb,ide_teclb";
    }

    public String getSqlTransaccionesCuenta(String ide_tecba, String fechaInicio, String fechaFin) {
        return "select fecha_trans_teclb,numero_teclb,ide_cnccc,nombre_tettb,beneficiari_teclb,"
                + "case when signo_tettb = 1 THEN valor_teclb  end as INGRESOS,case when signo_tettb = -1 THEN valor_teclb end as EGRESOS, '' SALDO,observacion_teclb,ide_teclb,conciliado_teclb as conciliado "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and ide_teelb =" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "and fecha_trans_teclb BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "order by fecha_trans_teclb,ide_teclb";
    }

    public String getSqlTransaccionesAnuladasCuenta(String ide_tecba, String fechaInicio, String fechaFin) {
        return "select fecha_trans_teclb,numero_teclb,ide_cnccc,nombre_tettb,beneficiari_teclb,"
                + "valor_teclb as VALOR,observacion_teclb,ide_teclb,conciliado_teclb as conciliado "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and ide_teelb !=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
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
            if (tab_saldo.getValor("valor") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor("valor"));
                } catch (Exception e) {
                }
            }
        }
        return saldo;
    }

    /**
     * Retorna el saldo de una cuenta
     *
     * @param ide_tecba
     * @return
     */
    public double getSaldoCuenta(String ide_tecba) {
        double saldo = 0;
        String sql = "select ide_tecba,sum(valor_teclb * signo_tettb) as valor "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "group by ide_tecba";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor("valor") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor("valor"));
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

    public String generarLibroBanco(String beneficiario, String fecha, String ide_tettb, String ide_tecba, double valor, String observacion, String numero, String ide_cnccc) {
        return generarTablaLibroBanco(beneficiario, fecha, ide_tettb, ide_tecba, valor, observacion, numero, ide_cnccc).getValor("ide_teclb");
    }

    public TablaGenerica generarTablaLibroBanco(String beneficiario, String fecha, String ide_tettb, String ide_tecba, double valor, String observacion, String numero, String ide_cnccc) {
        TablaGenerica tab_cab_libro_banco = new TablaGenerica();
        tab_cab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", -1);
        tab_cab_libro_banco.setCondicion("ide_teclb=-1");
        tab_cab_libro_banco.ejecutarSql();
        if (numero == null || numero.isEmpty()) {
            numero = "000000";
        }
        tab_cab_libro_banco.insertar();
        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_cab_libro_banco.setValor("numero_teclb", numero);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", fecha);
        tab_cab_libro_banco.setValor("fecha_venci_teclb", fecha);
        tab_cab_libro_banco.setValor("beneficiari_teclb", beneficiario);
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
        tab_cab_libro_banco.setValor("ide_tettb", ide_tettb);
        tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
        tab_cab_libro_banco.setValor("observacion_teclb", observacion);
        tab_cab_libro_banco.setValor("conciliado_teclb", "false");
        actualizarNumMaximoTipoTransaccion(ide_tecba, ide_tettb, numero);
        tab_cab_libro_banco.guardar();
        return tab_cab_libro_banco;
    }

    public String generarLibroBancoTransferir(String fecha, String ide_tettb, String ide_tecba, String ide_tecba2, double valor, String observacion, String numero) {
        String ide_geper = utilitario.getVariable("p_con_beneficiario_empresa");
        String beneficiario = getPersona(ide_geper).getValor("nom_geper");
        TablaGenerica tab_cab_libro_banco = new TablaGenerica();
        tab_cab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", -1);
        tab_cab_libro_banco.setCondicion("ide_teclb=-1");
        tab_cab_libro_banco.ejecutarSql();
        if (numero == null || numero.isEmpty()) {
            numero = "000000";
        }
        // inserto el retiro de la cuenta origen
        tab_cab_libro_banco.limpiar();
        tab_cab_libro_banco.insertar();

        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_cab_libro_banco.setValor("numero_teclb", numero);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", fecha);
        tab_cab_libro_banco.setValor("fecha_venci_teclb", fecha);
        tab_cab_libro_banco.setValor("beneficiari_teclb", beneficiario);
        tab_cab_libro_banco.setValor("conciliado_teclb", "false");
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
        tab_cab_libro_banco.setValor("ide_tettb", ide_tettb);
        tab_cab_libro_banco.setValor("observacion_teclb", observacion);

        // inserto el ingreso a la cuenta destino
        tab_cab_libro_banco.insertar();
        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_cab_libro_banco.setValor("numero_teclb", numero);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", utilitario.getFechaActual());
        tab_cab_libro_banco.setValor("fecha_venci_teclb", fecha);
        tab_cab_libro_banco.setValor("beneficiari_teclb", beneficiario);
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba2);
        //if (getParametroCuentaBanco(ide_tecba, "ide_tecba", "ide_tetcb").equals(utilitario.getVariable("p_tes_tipo_cuenta_banco_virtual"))) {
        //    tab_cab_libro_banco.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_deposito"));
        // } else {
        tab_cab_libro_banco.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_transferencia_mas"));
        //}
        tab_cab_libro_banco.setValor("observacion_teclb", observacion);
        tab_cab_libro_banco.setValor("conciliado_teclb", "false");
        actualizarNumMaximoTipoTransaccion(ide_tecba, ide_tettb, numero);
        tab_cab_libro_banco.guardar();
        return tab_cab_libro_banco.getValor(0, "ide_teclb") + "," + tab_cab_libro_banco.getValor(1, "ide_teclb");
    }

//    /**
//     * Genera una transaccion en bancos/caja de un pago de una factura CxC
//     *
//     * @param tab_cab_factura_cxc Cabecera de Factura Cxc
//     * @param ide_tecba Cuenta Destino
//     * @param valor Valor
//     * @param observacion Observación
//     * @param numero Numero de Documento Relacionado
//     * @param fechaTransaccion Fecha de Pago
//     * @return ide_teclb
//     */
//    public String generarPagoFacturaCxC(Tabla tab_cab_factura_cxc, String ide_tecba, double valor, String observacion, String numero, String fechaTransaccion) {
//        if (numero == null || numero.isEmpty()) {
//            numero = tab_cab_factura_cxc.getValor("secuencial_cccfa");
//        }
//
//        if (observacion == null || observacion.isEmpty()) {
//            observacion = "V/. PAGO FACTURA " + tab_cab_factura_cxc.getValor("secuencial_cccfa");
//        }
//
//        if (fechaTransaccion == null || fechaTransaccion.isEmpty()) {
//            fechaTransaccion = utilitario.getFormatoFecha(tab_cab_factura_cxc.getValor("fecha_emisi_cccfa"));
//        }
//
//        long ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb", 1);
//        String sql = "INSERT INTO tes_cab_libr_banc (ide_teclb, ide_tecba, "
//                + "ide_sucu, ide_tettb, ide_teelb, ide_empr, ide_cnccc, valor_teclb, numero_teclb, "
//                + "fecha_trans_teclb, fecha_venci_teclb, fec_cam_est_teclb, conciliado_teclb, "
//                + "beneficiari_teclb, observacion_teclb) "
//                + "VALUES (" + ide_teclb + ", " + ide_tecba + ", " + utilitario.getVariable("ide_empr") + ",  "
//                + "" + getTipoTransaccionCxC(tab_cab_factura_cxc.getValor("ide_cndfp")) + ", " + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
//                + "" + utilitario.getVariable("ide_empr") + ", " + tab_cab_factura_cxc.getValor("ide_cnccc") + ", "
//                + "" + utilitario.getFormatoNumero(valor) + ", '" + numero + "', '" + fechaTransaccion + "', "
//                + "'" + fechaTransaccion + "', "
//                + "NULL, false, '" + getPersona(tab_cab_factura_cxc.getValor("ide_geper")).getValor("nom_geper") + "', '" + observacion + "')";
//        utilitario.getConexion().agregarSqlPantalla(sql);
//        return String.valueOf(ide_teclb);
//    }
    public String generarTransaccion(String ide_tecba, String ide_tettb, double valor, String observacion, String numero, String fechaTransaccion, String beneficiario) {
        long ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb", 1);
        if (numero == null || numero.isEmpty()) {
            numero = "000000";
        }
        String sql = "INSERT INTO tes_cab_libr_banc (ide_teclb, ide_tecba, "
                + "ide_sucu, ide_tettb, ide_teelb, ide_empr, ide_cnccc, valor_teclb, numero_teclb, "
                + "fecha_trans_teclb, fecha_venci_teclb, fec_cam_est_teclb, conciliado_teclb, "
                + "beneficiari_teclb, observacion_teclb) "
                + "VALUES (" + ide_teclb + ", " + ide_tecba + ", " + utilitario.getVariable("ide_sucu") + ",  "
                + "" + ide_tettb + ", " + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ","
                + "" + utilitario.getVariable("ide_empr") + ", null, "
                + "" + utilitario.getFormatoNumero(valor) + ", '" + numero + "', '" + fechaTransaccion + "', "
                + "'" + fechaTransaccion + "', "
                + "NULL, false, '" + beneficiario + "', '" + observacion + "')";
        utilitario.getConexion().agregarSqlPantalla(sql);
        return String.valueOf(ide_teclb);
    }

//    /**
//     * Genera una transaccion en bancos/caja de un pago de una factura CxC
//     *
//     * @param tab_cab_factura_cxc Factura
//     * @param ide_tecba Cuenta Banco-Cajsa
//     * @param valor Valor
//     * @param numero Numero de Documento
//     * @param fechaTransaccion Fecha de Transaccion
//     * @param tipoTransaccion Tipo de Transaccion Tesoreria
//     *
//     * @return ide_teclb generado
//     */
//    public String generarPagoFacturaCxC(TablaGenerica tab_cab_factura_cxc, String ide_tecba, double valor, String numero, String fechaTransaccion, String tipoTransaccion, String observacion) {
//        //Si noy numero de documento, asigna el secuencial de la factura
//        if (numero == null || numero.isEmpty()) {
//            numero = tab_cab_factura_cxc.getValor("secuencial_cccfa");
//        }
//
//        if (observacion == null || observacion.isEmpty()) {
//            observacion = "V/. PAGO FACTURA N." + tab_cab_factura_cxc.getValor("secuencial_cccfa");
//        }
//        //Si no hay fecha de pago, se asigna la fecha de la factura
//        if (fechaTransaccion == null || fechaTransaccion.isEmpty()) {
//            fechaTransaccion = utilitario.getFormatoFecha(tab_cab_factura_cxc.getValor("fecha_emisi_cccfa"));
//        }
//        //Si tipo transaccion, busca un tipo de transaccion cxc
//        if (tipoTransaccion == null || tipoTransaccion.isEmpty()) {
//            tipoTransaccion = getTipoTransaccionCxC(tab_cab_factura_cxc.getValor("ide_cndfp"));
//        }
//
//        long ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb", 1);
//        String sql = "INSERT INTO tes_cab_libr_banc (ide_teclb, ide_tecba, "
//                + "ide_sucu, ide_tettb, ide_teelb, ide_empr,  valor_teclb, numero_teclb, "
//                + "fecha_trans_teclb, fecha_venci_teclb, fec_cam_est_teclb, conciliado_teclb, "
//                + "beneficiari_teclb, observacion_teclb) "
//                + "VALUES (" + ide_teclb + ", " + ide_tecba + ", " + utilitario.getVariable("ide_empr") + ",  "
//                + "" + tipoTransaccion + ", " + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
//                + "" + utilitario.getVariable("ide_empr") + ", "
//                + "" + utilitario.getFormatoNumero(valor) + ", '" + numero + "', '" + fechaTransaccion + "', "
//                + "'" + fechaTransaccion + "', "
//                + "NULL, false, '" + getPersona(tab_cab_factura_cxc.getValor("ide_geper")).getValor("nom_geper") + "', '" + observacion + "')";
//        utilitario.getConexion().agregarSqlPantalla(sql);
//        return String.valueOf(ide_teclb);
//    }
//    public String generarPagoFacturaCxP(TablaGenerica tab_cab_factura_cxp, String ide_tecba, double valor, String numero, String fechaTransaccion, String tipoTransaccion, String observacion) {
//        //Si noy numero de documento, asigna el secuencial de la factura
//        if (numero == null || numero.isEmpty()) {
//            numero = tab_cab_factura_cxp.getValor("numero_cpcfa");
//        }
//        if (observacion == null || observacion.isEmpty()) {
//            observacion = "V/. PAGO " + ser_cuentas_cxp.getNombreTipoDocumento(tab_cab_factura_cxp.getValor("ide_cntdo")) + " N." + tab_cab_factura_cxp.getValor("numero_cpcfa");
//        }
//
//        //Si no hay fecha de pago, se asigna la fecha de la factura
//        if (fechaTransaccion == null || fechaTransaccion.isEmpty()) {
//            fechaTransaccion = utilitario.getFormatoFecha(tab_cab_factura_cxp.getValor("fecha_emisi_cpcfa"));
//        }
//        //Si tipo transaccion, busca un tipo de transaccion cxc
//        if (tipoTransaccion == null || tipoTransaccion.isEmpty()) {
//            tipoTransaccion = getTipoTransaccionCxP(tab_cab_factura_cxp.getValor("ide_cndfp"));
//        }
//
//        long ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb", 1);
//        String sql = "INSERT INTO tes_cab_libr_banc (ide_teclb, ide_tecba, "
//                + "ide_sucu, ide_tettb, ide_teelb, ide_empr,  valor_teclb, numero_teclb, "
//                + "fecha_trans_teclb, fecha_venci_teclb, fec_cam_est_teclb, conciliado_teclb, "
//                + "beneficiari_teclb, observacion_teclb) "
//                + "VALUES (" + ide_teclb + ", " + ide_tecba + ", " + utilitario.getVariable("ide_empr") + ",  "
//                + "" + tipoTransaccion + ", " + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
//                + "" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
//                + "" + utilitario.getFormatoNumero(valor) + ", '" + numero + "', '" + fechaTransaccion + "', "
//                + "'" + fechaTransaccion + "', "
//                + "NULL, false, '" + getPersona(tab_cab_factura_cxp.getValor("ide_geper")).getValor("nom_geper") + "', '" + observacion + "')";
//        utilitario.getConexion().agregarSqlPantalla(sql);
//        return String.valueOf(ide_teclb);
//    }
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
        return "select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "and signo_tettb=1 order by nombre_tettb";
    }

    /**
     * Retorna las transacciones con signo negativo (disminuyen libro caja
     * bancos) EGRESOS
     *
     * @return
     */
    public String getSqlTipoTransaccionNegativo() {
        return "select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + "and signo_tettb=-1 order by nombre_tettb";
    }

    public String getSqlTipoTransaccion() {
        return "select ide_tettb,nombre_tettb from tes_tip_tran_banc where ide_empr=" + utilitario.getVariable("ide_empr") + " order by nombre_tettb";
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
        //       calculo el maximo y suma uno
        List lis_max = utilitario.getConexion().consultar("SELECT max(secuencial_tesec)  from tes_secuencial_trans a \n"
                + "inner join tes_tip_tran_banc b on a.ide_tettb = b.ide_tettb where a.ide_tettb =" + ide_tettb + " and ide_tecba=" + ide_tecba + " AND calculado_tettb= true ");
        if (lis_max.get(0) != null) {
            try {
                maximo = ((Integer.parseInt(lis_max.get(0).toString())) + 1) + "";
            } catch (Exception e) {
                maximo = "";
            }
        }
        return maximo;
    }

    /**
     * Sql con las cuentas que tiene la empresa
     *
     * @param fecha_inicio_periodo  corresponde a la fecha de inicio del pariodo fiscal
     * @return
     */
    public String getSqlPosicionConsolidada(String fecha_inicio_periodo) {
        return "select a.ide_tecba,nombre_teban,nombre_tecba,nombre_tetcb,ide_cndpc, \n"
                + "(Select sum(dcc.valor_cndcc*sc.signo_cnscu) as valor \n"
                + "from con_cab_comp_cont ccc \n"
                + "inner join  con_det_comp_cont dcc on ccc.ide_cnccc=dcc.ide_cnccc \n"
                + "inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc \n"
                + "inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu \n"
                + "inner  join con_signo_cuenta sc on tc.ide_cntcu=sc.ide_cntcu and dcc.ide_cnlap=sc.ide_cnlap \n"
                + "WHERE  ccc.ide_cneco in (" + utilitario.getVariable("p_con_estado_comp_inicial") + "," + utilitario.getVariable("p_con_estado_comprobante_normal") + "," + utilitario.getVariable("p_con_estado_comp_final") + ") \n"
                + "  and fecha_trans_cnccc >= '"+fecha_inicio_periodo+"' and dpc.ide_cndpc=a.ide_cndpc\n"
                + "GROUP BY dpc.ide_cndpc ) as saldo_contable, \n"
                + "(select sum(valor_teclb * signo_tettb) + valor_inicial as valor \n"
                + "from tes_cab_libr_banc aa \n"
                + "inner join tes_tip_tran_banc bb on aa.ide_tettb=bb.ide_tettb \n"
                + " inner join (select ide_tecba,sum(valor_teclb * signo_tettb)  as valor_inicial "
+ " from tes_cab_libr_banc a inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
+ " where fecha_trans_teclb < '"+fecha_inicio_periodo+"' and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " group by ide_tecba"
 + " ) cc on aa.ide_tecba=cc.ide_tecba"
+ " where aa.ide_tecba=a.ide_tecba and fecha_trans_teclb >= '"+fecha_inicio_periodo+"' and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " group by aa.ide_tecba,valor_inicial"
+ " ) as saldo_disponible \n"
                + "from tes_cuenta_banco a\n"
                + "inner join tes_banco b on a.ide_teban= b.ide_teban\n"
                + "inner join tes_tip_cuen_banc c on a.ide_tetcb = c.ide_tetcb\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "order by nombre_teban,nombre_tecba";
    }

    /**
     * Retorna el signo de un tipo transaccion
     *
     * @param ide_tettb
     * @return 1= positivo ; -1 negativo
     */
    public String getSignoTransaccion(String ide_tettb) {
        TablaGenerica tab_tipo_transacciones = utilitario.consultar("select ide_tettb,signo_tettb from tes_tip_tran_banc where ide_tettb=" + ide_tettb);
        if (tab_tipo_transacciones.getTotalFilas() > 0) {
            if (tab_tipo_transacciones.getValor(0, "signo_tettb") != null && !tab_tipo_transacciones.getValor(0, "signo_tettb").isEmpty()) {
                return tab_tipo_transacciones.getValor(0, "signo_tettb");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getCuentaContable(String ide_tecba) {
        TablaGenerica tab_cuenta_banco = utilitario.consultar("select ide_tecba,ide_cndpc from tes_cuenta_banco where ide_tecba=" + ide_tecba);
        if (tab_cuenta_banco.getTotalFilas() > 0) {
            if (tab_cuenta_banco.getValor("ide_cndpc") != null) {
                return tab_cuenta_banco.getValor("ide_cndpc");
            } else {
                return null;
            }
        } else {
            System.out.println("cuenta bancaria asiento vacia ");
            return null;
        }
    }

    public String agregarAsteriscosCheque(String monto_letras) {
        String x = "*";
        String monto_x = "";
        for (int i = 1; i < 50 - monto_letras.length(); i++) {
            x = x.concat("*");
        }
        monto_x = monto_letras.concat(x);
        return monto_x;
    }

    public void conciliarMovimientos(String ide_teclb) {
        utilitario.getConexion().ejecutarSql("update tes_cab_libr_banc set conciliado_teclb=true where ide_teclb in(" + ide_teclb + ")");
    }

    /**
     * Retorna tipos de identificacion exceto ruc
     *
     * @return
     */
    public String getSqlComboTipoIdentificacion() {
        return "select  ide_getid,nombre_getid from  gen_tipo_identifi ORDER BY nombre_getid";
    }

    public TablaGenerica getPersonaporIdentificacion(String identificac_geper) {
        return utilitario.consultar("select * from gen_persona where identificac_geper='" + identificac_geper + "'");
    }

    public String crearBeneficiario(String identificac_geper, String ide_getid, String nom_geper) {
        TablaGenerica tabla = new TablaGenerica();
        tabla.setTabla("gen_persona", "ide_geper", -1);
        tabla.setCondicion("ide_geper=-1");
        tabla.ejecutarSql();
        tabla.insertar();
        tabla.setValor("es_proveedo_geper", "true");
        tabla.setValor("es_cliente_geper", "false");
        tabla.setValor("nivel_geper", "HIJO");
        tabla.setValor("fecha_ingre_geper", utilitario.getFechaActual());
        tabla.setValor("ide_cntco", "2");
        tabla.setValor("identificac_geper", identificac_geper);
        tabla.setValor("ide_getid", ide_getid);
        tabla.setValor("nom_geper", nom_geper);
        tabla.guardar();
        return tabla.getValor("ide_geper");
    }

    /**
     * Reorna la sentecnia SQL para obtener los Proveedor para que se utilice en
     * Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboBeneficiario() {
        return "select ide_geper,identificac_geper,nom_geper from gen_persona where identificac_geper is not null order by nom_geper ";
    }

    public void anularMovimiento(String ide_teclb) {
        //Anula Movimiento 
        utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set ide_teelb =1, usuario_actua='" + utilitario.getVariable("NICK") + "',fecha_actua='" + utilitario.getFechaActual() + "',hora_actua='" + utilitario.getHoraActual() + "'  where ide_teclb=" + ide_teclb);
        //Cambia a depositado = false si es enviado a banco
        //**** utilitario.getConexion().agregarSqlPantalla("update tes_cab_libr_banc set depositado_teclb =false, devuelto_teclb= false where tes_ide_teclb=" + ide_teclb);
        //Anula Asiento
        TablaGenerica tab_busca = utilitario.consultar("SELECT * FROM con_cab_comp_cont where ide_cnccc = (select ide_cnccc from tes_cab_libr_banc where ide_teclb=" + ide_teclb + ")");
        String p_con_estado_comprobante_anulado = utilitario.getVariable("p_con_estado_comprobante_anulado");
        if (tab_busca.getTotalFilas() > 0) {
            utilitario.getConexion().agregarSqlPantalla("update con_cab_comp_cont set ide_cneco=" + p_con_estado_comprobante_anulado + " where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
            utilitario.getConexion().agregarSqlPantalla("UPDATE con_det_comp_cont set valor_cndcc=0 where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
            utilitario.getConexion().agregarSqlPantalla("UPDATE cxc_cabece_factura set ide_cnccc=Null where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
            utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_cabece_factur set ide_cnccc=Null where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
            utilitario.getConexion().agregarSqlPantalla("UPDATE cxc_detall_transa set ide_cnccc=Null where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
            utilitario.getConexion().agregarSqlPantalla("UPDATE cxp_detall_transa set ide_cnccc=Null where ide_cnccc=" + tab_busca.getValor("ide_cnccc"));
        }
        //eliminar pago y cobro sea el caso
        utilitario.getConexion().agregarSqlPantalla("delete from cxc_detall_transa where ide_teclb=" + ide_teclb + " and numero_pago_ccdtr >0 ");
        utilitario.getConexion().agregarSqlPantalla("delete from cxp_detall_transa where ide_teclb=" + ide_teclb + " and numero_pago_cpdtr >0 ");

    }

    /**
     *
     * @param ide_tecba
     * @param fecha
     * @return
     */
    public double getSaldoInicialConciliadoCuenta(String ide_tecba, String fecha) {
        double saldo = 0;
        String sql = "select ide_tecba,sum(valor_teclb * signo_tettb) as valor "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and fecha_trans_teclb  <= '" + fecha + "' " //fecha_trans_teclb antes  26/10/2018
                + "and conciliado_teclb=true "
                + "and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "group by ide_tecba";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor("valor") != null) {
                try {
                    saldo = Double.parseDouble(tab_saldo.getValor("valor"));
                } catch (Exception e) {
                }
            }
        }
        return saldo;
    }

    /**
     *
     * @param ide_tecba
     * @param fecha
     * @return
     */
    public double getSaldoInicialEstadoCuenta(String ide_tecba, String fecha) {
        double saldo_con_mes = 0;
        String sql = "select ide_tecba,sum(valor_teclb * signo_tettb) as valor "
                + "from tes_cab_libr_banc a "
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
                + "where ide_tecba=" + ide_tecba + " "
                + "and fecha_concilia_teclb  <= '" + fecha + "'  " //no considera cheques posfechados  AND  a.ide_tettb <>14  //antes fecha_trans_teclb 26/10/2018
                + "and conciliado_teclb=true  "
                + "and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
                + "group by ide_tecba";
        TablaGenerica tab_saldo = utilitario.consultar(sql);
        if (tab_saldo.getTotalFilas() > 0) {
            if (tab_saldo.getValor("valor") != null) {
                try {
                    saldo_con_mes = Double.parseDouble(tab_saldo.getValor("valor"));
                } catch (Exception e) {
                }
            }
        }

//        double saldo_ch_pos = 0;
//        sql = "select ide_tecba,sum(valor_teclb * signo_tettb) as valor "
//                + "from tes_cab_libr_banc a "
//                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb "
//                + "where ide_tecba=" + ide_tecba + " "
//                + "and fec_cam_est_teclb <= '" + fecha + "' AND  a.ide_tettb =14 " // considera cheques posfechados 
//                + "and conciliado_teclb=true  "
//                + "and ide_teelb=" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + " "
//                + "group by ide_tecba";
//        TablaGenerica tab_saldo_c = utilitario.consultar(sql);
//        if (tab_saldo_c.getTotalFilas() > 0) {
//            if (tab_saldo_c.getValor("valor") != null) {
//                try {
//                    saldo_ch_pos = Double.parseDouble(tab_saldo_c.getValor("valor"));
//                } catch (Exception e) {
//                }
//            }
//        }
//        double saldo = saldo_con_mes + saldo_ch_pos;
        return saldo_con_mes;
    }

    /**
     * Actualiza el numeromaximo en la tabla de secuenciales de tipos de
     * transaccion
     *
     * @param ide_tecba
     * @param ide_tettb
     * @param numeroIngresado
     */
    public void actualizarNumMaximoTipoTransaccion(String ide_tecba, String ide_tettb, String numeroIngresado) {
        long numIng = 0;
        try {
            //solo deja numeros
            String num = "";
            for (int i = 0; i < numeroIngresado.length(); i++) {
                if (Character.isDigit(numeroIngresado.charAt(i))) {
                    num += numeroIngresado.charAt(i);
                }
            }
            numIng = Long.parseLong(num.trim());
        } catch (Exception e) {
        }
        if (numIng > 0) {
            TablaGenerica tag = new TablaGenerica();
            tag.setTabla("tes_secuencial_trans", "ide_tesec");
            tag.setCondicion("ide_tecba=" + ide_tecba + " and ide_tettb = " + ide_tettb + " and ide_sucu = " + utilitario.getVariable("IDE_SUCU"));
            tag.ejecutarSql();
            if (tag.isEmpty()) {
                tag.insertar();
            } else {
                tag.modificar(0);
            }
            tag.setValor("ide_tettb", ide_tettb);
            tag.setValor("ide_tecba", ide_tecba);
            tag.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tag.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tag.setValor("secuencial_tesec", String.valueOf(numIng));
            tag.guardar();
        }
    }

}
