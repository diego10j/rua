/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_bancos;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.List;
import pkg_cuentas_x_pagar.cls_cuentas_x_pagar;
import pkg_rrhh.cls_nomina;
import sistema.aplicacion.Utilitario;


/**
 *
 * @author Diego
 */
public class cls_bancos {

    private Utilitario utilitario = new Utilitario();
    private Tabla tab_cab_libro_banco = new Tabla();

    public cls_bancos() {
        tab_cab_libro_banco.setId("tab_cab_libro_banco");
        tab_cab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", -1);
        tab_cab_libro_banco.setCondicion("ide_teclb=-1");
        tab_cab_libro_banco.getColumna("ide_teclb").setExterna(false);
        tab_cab_libro_banco.ejecutarSql();
    }

    public String cargarNombreGeper(String ide_geper) {
        String provee_actual = ide_geper;
        String nombre_proveedor = "";
        List nom_prove = utilitario.getConexion().consultar("select nom_geper from gen_persona where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_geper=" + provee_actual);
        if (nom_prove != null) {
            if (!nom_prove.isEmpty()) {
                if (nom_prove.get(0) != null) {
                    nombre_proveedor = nom_prove.get(0).toString();
                }
            }
        }
        return nombre_proveedor;
    }

    public void guardarLibroBancoRolesPago(String ide_cnccc, String str_cuenta_banco, double valor, String ide_geper, String fecha, String observacion) {
        if (str_cuenta_banco != null) {
            cls_nomina nomina = new cls_nomina();
            Tabla tab_cab_libro_banco = new Tabla();
            tab_cab_libro_banco.setId("tab_cab_libro_banco");
            tab_cab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", -1);
            tab_cab_libro_banco.setCondicion("ide_teclb=-1");
            tab_cab_libro_banco.ejecutarSql();

            String str_ide_ttr_nota_debito = utilitario.getVariable("p_tes_nota_debito");

            tab_cab_libro_banco.insertar();
            tab_cab_libro_banco.setValor("ide_tecba", str_cuenta_banco);
            tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
            tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
            tab_cab_libro_banco.setValor("numero_teclb", "-1");
            tab_cab_libro_banco.setValor("fecha_trans_teclb", utilitario.getFechaActual());
            tab_cab_libro_banco.setValor("fecha_venci_teclb", utilitario.getFechaActual());
            tab_cab_libro_banco.setValor("beneficiari_teclb", cargarNombreGeper(ide_geper));
            tab_cab_libro_banco.setValor("ide_tettb", str_ide_ttr_nota_debito);
            tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
            tab_cab_libro_banco.setValor("observacion_teclb", observacion);

            tab_cab_libro_banco.guardar();
            System.out.println("se ha generado el libro banco en roles de pagos");
        }
    }

    public void guardarLibroBancoRolesPagoxEmpleado(Tabla tab_empleado_rol, String ide_cnccc, String str_cuenta_banco) {
        if (str_cuenta_banco != null) {
            cls_nomina nomina = new cls_nomina();
            Tabla tab_cab_libro_banco = new Tabla();
            tab_cab_libro_banco.setId("tab_cab_libro_banco");
            tab_cab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", -1);
            tab_cab_libro_banco.setCondicion("ide_teclb=-1");
            tab_cab_libro_banco.ejecutarSql();

            String str_ide_ttr_cheque = utilitario.getVariable("p_tes_tran_cheque");

            for (int i = 0; i < tab_empleado_rol.getTotalFilas(); i++) {
                tab_cab_libro_banco.insertar();
                tab_cab_libro_banco.setValor("ide_tecba", str_cuenta_banco);
                tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
                tab_cab_libro_banco.setValor("valor_teclb", nomina.getParametroRubrosRol("valor_rhrro", utilitario.getVariable("p_reh_rubro_valor_recibir"), tab_empleado_rol.getValor(i, "ide_rherl")));
                tab_cab_libro_banco.setValor("numero_teclb", "-1");
                tab_cab_libro_banco.setValor("fecha_trans_teclb", utilitario.getFechaActual());
                tab_cab_libro_banco.setValor("fecha_venci_teclb", utilitario.getFechaActual());
                tab_cab_libro_banco.setValor("beneficiari_teclb", cargarNombreGeper(tab_empleado_rol.getValor(i, "ide_geper")));
                tab_cab_libro_banco.setValor("ide_tettb", str_ide_ttr_cheque);
                tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
                tab_cab_libro_banco.setValor("observacion_teclb", "pago rol de" + cargarNombreGeper(tab_empleado_rol.getValor(i, "ide_geper")));

            }
            tab_cab_libro_banco.guardar();
            System.out.println("se ha generado el libro banco en roles de pagos x empleado");
        }
    }

    public String obtener_signo_transaccion(String IdeTransaccion) {
        Tabla ltab_tipo_transaccion = new Tabla();
        if (IdeTransaccion != null && !IdeTransaccion.isEmpty()) {
            ltab_tipo_transaccion.setSql("SELECT ide_tettb,signo_tettb from tes_tip_tran_banc where ide_tettb = " + IdeTransaccion + " "
                    + " and ide_empr=" + utilitario.getVariable("ide_empr"));
            ltab_tipo_transaccion.ejecutarSql();
            return ltab_tipo_transaccion.getValor("signo_tettb");
        } else {
            return null;
        }
        //1 suma -1 resta
    }

    public String obtener_saldo_cuenta(String IdeCuenta, String fecha) {
        // Devuelve el saldo de la cuenta bancaria dada a una fecha determinada
        //Parámetros:  cuenta bancaria, fecha, 0,1,2 para indicar conciliado
        // Conciliado: 0 no, 1 sí, 2 sí y no
        List lis_saldo_sql = utilitario.getConexion().consultar("select sum(clb.valor_teclb* ttb.signo_tettb) valor_cuenta from "
                + "tes_cab_libr_banc clb, tes_tip_tran_banc ttb "
                + "where clb.ide_tecba = " + IdeCuenta + " and "
                + "clb.ide_tettb = ttb.ide_tettb and "
                + "clb.fecha_trans_teclb<='" + fecha + "'");
        if (lis_saldo_sql.size() > 0) {
            return lis_saldo_sql.get(0) + "";
        } else {
            return null;
        }

    }

    public String obtenerParametroCuentaBanco(String parametro, String ide_tecba) {
        TablaGenerica tab_cuenta_banco = utilitario.consultar("select * from tes_cuenta_banco where ide_tecba=" + ide_tecba);
        if (tab_cuenta_banco.getTotalFilas() > 0) {
            System.out.println("cuenta bancaria asiento " + tab_cuenta_banco.getValor("ide_cndpc"));
            if (tab_cuenta_banco.getValor(parametro) != null) {
                return tab_cuenta_banco.getValor(parametro);
            } else {
                return null;
            }
        } else {
            System.out.println("cuenta bancaria asiento vacia ");
            return null;
        }
    }

    public String obtenerNumMaximoTran(String ide_tecba, String ide_tettb) {
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
        String str_ide_ttr_retiro_caja = utilitario.getVariable("p_tes_tran_retiro_caja");
        //if (ide_tettb.equals(str_ide_ttr_retiro_caja)) {
//       calculo el maximo de las notas de debito
//            List lis_max = utilitario.getConexion().consultar("SELECT max(numero_teclb) from tes_cab_libr_banc where ide_tettb=" + str_ide_ttr_retiro_caja + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " and ide_tecba=" + ide_tecba);
//            maximo = "000000001";
//            if (lis_max.get(0) != null) {
//                try {
//                    maximo = ((Integer.parseInt(lis_max.get(0).toString())) + 1) + "";
//                    maximo = utilitario.generarCero(9 - maximo.length()) + maximo;
//
//                } catch (Exception e) {
//                }
//            }
        //} else 
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

    public int obtenerDiasPago(String ide_cndfp) {
        List dias_sql = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
        int int_dias = 0;
        if (dias_sql != null && !dias_sql.isEmpty()) {
            int_dias = Integer.parseInt(dias_sql.get(0).toString());
        }
        return int_dias;
    }

//    public void generarLibroBancoCxC(Tabla tab_cab_factura_cxc, String ide_tecba, double valor, String observacion, String numero) {
//        if (numero == null || numero.isEmpty()) {
//            numero = "000000";
//        }
//        System.out.println("size 228 " + utilitario.getConexion().getSqlPantalla().size());
//        tab_cab_libro_banco.limpiar();
//        tab_cab_libro_banco.insertar();
//        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
//        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
//        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
//        tab_cab_libro_banco.setValor("numero_teclb", numero);
//        tab_cab_libro_banco.setValor("fecha_trans_teclb", tab_cab_factura_cxc.getValor("fecha_emisi_cccfa"));
//        tab_cab_libro_banco.setValor("fecha_venci_teclb", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura_cxc.getValor("fecha_emisi_cccfa")), obtenerDiasPago(tab_cab_factura_cxc.getValor("ide_cndfp")))));
//        System.out.println("size 237 " + utilitario.getConexion().getSqlPantalla().size());
//        tab_cab_libro_banco.setValor("beneficiari_teclb", buscarParametroPersona("nom_geper", tab_cab_factura_cxc.getValor("ide_geper")));
//        System.out.println("size 239 " + utilitario.getConexion().getSqlPantalla().size());
//        tab_cab_libro_banco.setValor("ide_tettb", buscarTransaccionCXC(tab_cab_factura_cxc.getValor("ide_cndfp")));
//        tab_cab_libro_banco.setValor("ide_cnccc", tab_cab_factura_cxc.getValor("ide_cnccc"));
//        tab_cab_libro_banco.setValor("observacion_teclb", observacion);
//        System.out.println("size 243 " + utilitario.getConexion().getSqlPantalla().size());
//        tab_cab_libro_banco.guardar();
//        System.out.println("size 245 " + utilitario.getConexion().getSqlPantalla().size());
//        System.out.println("se ha generado un libro banco cxc ");
//    }
    public synchronized Tabla generarLibroBancoCxC(Tabla tab_cab_factura_cxc, String ide_tecba, double valor, String observacion, String numero) {
        if (numero == null || numero.isEmpty()) {
            numero = tab_cab_factura_cxc.getValor("secuencial_cccfa");
        }
        long ide_teclb = utilitario.getConexion().getMaximo("tes_cab_libr_banc", "ide_teclb",1);
        String str_lib_banco = "INSERT INTO tes_cab_libr_banc (ide_teclb, ide_tecba, "
                + "ide_sucu, ide_tettb, ide_teelb, ide_empr, ide_cnccc, valor_teclb, numero_teclb, "
                + "fecha_trans_teclb, fecha_venci_teclb, fec_cam_est_teclb, conciliado_teclb, "
                + "beneficiari_teclb, observacion_teclb) "
                + "VALUES (" + ide_teclb + ", " + ide_tecba + ", " + utilitario.getVariable("ide_empr") + ",  "
                + "" + buscarTransaccionCXC(tab_cab_factura_cxc.getValor("ide_cndfp")) + ", " + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", "
                + "" + utilitario.getVariable("p_tes_estado_lib_banco_normal") + ", " + tab_cab_factura_cxc.getValor("ide_cnccc") + ", "
                + "" + utilitario.getFormatoNumero(valor) + ", '" + numero + "', '" + tab_cab_factura_cxc.getValor("fecha_emisi_cccfa") + "', "
                + "'" + utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura_cxc.getValor("fecha_emisi_cccfa")), obtenerDiasPago(tab_cab_factura_cxc.getValor("ide_cndfp")))) + "', "
                + "NULL, false, '" + buscarParametroPersona("nom_geper", tab_cab_factura_cxc.getValor("ide_geper")) + "', '" + observacion + "')";
        utilitario.getConexion().agregarSqlPantalla(str_lib_banco);
        System.out.println("size 265 " + utilitario.getConexion().getSqlPantalla().size());
        Tabla tab_libro_banco = new Tabla();
        tab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", -1);
        tab_libro_banco.setGenerarPrimaria(false);
        tab_libro_banco.getColumna("ide_teclb").setExterna(false);
        tab_libro_banco.setCondicion("ide_teclb=-1");
        tab_libro_banco.ejecutarSql();
        tab_libro_banco.insertar();
        tab_libro_banco.setValor("ide_teclb", ide_teclb + "");//Cuenta bancaria
        tab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
        tab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_libro_banco.setValor("numero_teclb", numero);
        tab_libro_banco.setValor("fecha_trans_teclb", tab_cab_factura_cxc.getValor("fecha_emisi_cccfa"));
        tab_libro_banco.setValor("fecha_venci_teclb", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(tab_cab_factura_cxc.getValor("fecha_emisi_cccfa")), obtenerDiasPago(tab_cab_factura_cxc.getValor("ide_cndfp")))));
        tab_libro_banco.setValor("beneficiari_teclb", buscarParametroPersona("nom_geper", tab_cab_factura_cxc.getValor("ide_geper")));
        tab_libro_banco.setValor("ide_tettb", buscarTransaccionCXC(tab_cab_factura_cxc.getValor("ide_cndfp")));
        tab_libro_banco.setValor("ide_cnccc", tab_cab_factura_cxc.getValor("ide_cnccc"));
        tab_libro_banco.setValor("observacion_teclb", observacion);
//        tab_libro_banco.guardar();
        System.out.println("size 285 " + utilitario.getConexion().getSqlPantalla().size());
        return tab_libro_banco;
    }

    public void generarLibroBancoCxP(Tabla tab_cab_factura_cxp, String ide_tecba, double valor, String observacion, String numero, String fecha) {
        cls_cuentas_x_pagar cxp = new cls_cuentas_x_pagar();
        if (numero == null || numero.isEmpty()) {
            numero = tab_cab_factura_cxp.getValor("numero_cpcfa").substring(6, tab_cab_factura_cxp.getValor("numero_cpcfa").length());
        }
        tab_cab_libro_banco.limpiar();
        tab_cab_libro_banco.insertar();
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_cab_libro_banco.setValor("numero_teclb", numero);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", fecha);
        tab_cab_libro_banco.setValor("fecha_venci_teclb", utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha), cxp.obtenerDiasPago(tab_cab_factura_cxp.getValor("ide_cndfp")))));
        tab_cab_libro_banco.setValor("beneficiari_teclb", buscarParametroPersona("nom_geper", tab_cab_factura_cxp.getValor("ide_geper")));
        tab_cab_libro_banco.setValor("ide_tettb", buscarTransaccionCXP(tab_cab_factura_cxp.getValor("ide_cndfp")));
        tab_cab_libro_banco.setValor("ide_cnccc", tab_cab_factura_cxp.getValor("ide_cnccc"));
        tab_cab_libro_banco.setValor("observacion_teclb", observacion);
        tab_cab_libro_banco.guardar();
    }

    public void generarLibroBanco(String ide_geper, String fecha, String ide_tettb, String ide_tecba, String ide_cnccc, double valor, String observacion, String numero) {
        if (numero == null || numero.isEmpty()) {
            numero = "0";
        }
        tab_cab_libro_banco.limpiar();
        tab_cab_libro_banco.insertar();
        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_cab_libro_banco.setValor("numero_teclb", numero);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", fecha);
        tab_cab_libro_banco.setValor("fecha_venci_teclb", fecha);
        tab_cab_libro_banco.setValor("beneficiari_teclb", buscarParametroPersona("nom_geper", ide_geper));
        tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
        tab_cab_libro_banco.setValor("ide_tettb", ide_tettb);
        tab_cab_libro_banco.setValor("observacion_teclb", observacion);
        tab_cab_libro_banco.guardar();
    }

    public void generarVariosLibrosBancos(String ide_geper, String fecha, String ide_tettb, String ide_tecba, String ide_cnccc, double valor, String observacion, String numero) {
        if (numero == null || numero.isEmpty()) {
            numero = "0";
        }
        tab_cab_libro_banco.insertar();
        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_cab_libro_banco.setValor("numero_teclb", numero);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", fecha);
        tab_cab_libro_banco.setValor("fecha_venci_teclb", fecha);
        tab_cab_libro_banco.setValor("beneficiari_teclb", buscarParametroPersona("nom_geper", ide_geper));
        tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
        tab_cab_libro_banco.setValor("ide_tettb", ide_tettb);
        tab_cab_libro_banco.setValor("observacion_teclb", observacion);
        System.out.println("una fila insertada en libro bancos");

    }

    public String getParametroCuentaBanco(String ide_busqueda, String parametro_busqueda, String parametro_retorno) {
        if (ide_busqueda != null && !ide_busqueda.isEmpty() && parametro_retorno != null && !parametro_retorno.isEmpty()) {
            TablaGenerica tab_cuenta_banco = utilitario.consultar("SELECT * from tes_cuenta_banco where " + parametro_busqueda + " = " + ide_busqueda);
            if (tab_cuenta_banco.getTotalFilas() > 0) {
                return tab_cuenta_banco.getValor(0, parametro_retorno);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public void generarLibroBancoTransferir(String ide_geper, String fecha, String ide_tettb, String ide_tecba, String ide_tecba2, String ide_cnccc, double valor, String observacion, String numero) {
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
        tab_cab_libro_banco.setValor("beneficiari_teclb", buscarParametroPersona("nom_geper", ide_geper));
        tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba);//Cuenta bancaria
        tab_cab_libro_banco.setValor("ide_tettb", ide_tettb);
        tab_cab_libro_banco.setValor("observacion_teclb", observacion + " ( Se Transfiere a la cuenta: " + obtenerParametroCuentaBanco("nombre_tecba", ide_tecba2) + " )");

        // inserto el ingreso a la cuenta destino

        tab_cab_libro_banco.insertar();
        tab_cab_libro_banco.setValor("ide_teelb", utilitario.getVariable("p_tes_estado_lib_banco_normal"));
        tab_cab_libro_banco.setValor("valor_teclb", utilitario.getFormatoNumero(valor));
        tab_cab_libro_banco.setValor("numero_teclb", numero);
        tab_cab_libro_banco.setValor("fecha_trans_teclb", utilitario.getFechaActual());
        tab_cab_libro_banco.setValor("fecha_venci_teclb", fecha);
        tab_cab_libro_banco.setValor("beneficiari_teclb", buscarParametroPersona("nom_geper", ide_geper));
        tab_cab_libro_banco.setValor("ide_cnccc", ide_cnccc);
        tab_cab_libro_banco.setValor("ide_tecba", ide_tecba2);
        if (getParametroCuentaBanco(ide_tecba, "ide_tecba", "ide_tetcb").equals(utilitario.getVariable("p_tes_tipo_cuenta_banco_virtual"))) {
            tab_cab_libro_banco.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_deposito"));
        } else {
            tab_cab_libro_banco.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_transferencia_mas"));
        }
        tab_cab_libro_banco.setValor("observacion_teclb", observacion + " ( Se Transfiere de la cuenta: " + obtenerParametroCuentaBanco("nombre_tecba", ide_tecba) + " )");
        tab_cab_libro_banco.guardar();
    }

    public String buscarParametroPersona(String parametro, String ide_geper) {
        TablaGenerica tab_persona = utilitario.consultar("select * from gen_persona where ide_empr=" + utilitario.getVariable("ide_empr") + " and ide_geper=" + ide_geper);
        if (tab_persona.getTotalFilas() > 0) {
            return tab_persona.getValor(parametro);
        } else {
            return null;
        }
    }

    public String buscarTransaccionCXC(String ide_cndfp) {
        //Retorna el tipo de transaccion dependiendo la forma de pago
        if (ide_cndfp != null) {
            String ide_tettb = "";
            List sql_forma_pago = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
            int dias = 0;
            if (!sql_forma_pago.isEmpty()) {
                dias = Integer.parseInt(sql_forma_pago.get(0).toString());
            }
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

    public String buscarTransaccionCXP(String ide_cndfp) {
        //Retorna el tipo de transaccion dependiendo la forma de pago
        if (ide_cndfp != null) {
            List sql_forma_pago = utilitario.getConexion().consultar("select dias_cndfp from con_deta_forma_pago where ide_cndfp=" + ide_cndfp);
            int dias = 0;
            if (!sql_forma_pago.isEmpty()) {
                dias = Integer.parseInt(sql_forma_pago.get(0).toString());
            }
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

    public String agregarAsteriscosCheque(String monto_letras) {
        String x = "*";
        String monto_x = "";
        for (int i = 1; i < 50 - monto_letras.length(); i++) {
            x = x.concat("*");
        }
        monto_x = monto_letras.concat(x);
        return monto_x;
    }

    public void reversar(String ide_teclb, String observacion) {

        if (ide_teclb != null && !ide_teclb.isEmpty()) {
            TablaGenerica tab_cab = utilitario.consultar("SELECT * from tes_cab_libr_banc where ide_teclb=" + ide_teclb);
            if (tab_cab.getTotalFilas() > 0) {
                if (tab_cab.getValor(0, "ide_tettb") != null && !tab_cab.getValor(0, "ide_tettb").isEmpty()) {
                    for (int i = 0; i < tab_cab.getTotalFilas(); i++) {
                        tab_cab_libro_banco.insertar();
                        tab_cab_libro_banco.setValor("ide_empr", utilitario.getVariable("ide_empr"));
                        tab_cab_libro_banco.setValor("ide_sucu", utilitario.getVariable("ide_empr"));
                        tab_cab_libro_banco.setValor("ide_tecba", tab_cab.getValor(i, "ide_tecba"));
                        tab_cab_libro_banco.setValor("ide_teelb", tab_cab.getValor(i, "ide_teelb"));
                        tab_cab_libro_banco.setValor("ide_cnccc", tab_cab.getValor(i, "ide_cnccc"));
                        tab_cab_libro_banco.setValor("valor_teclb", tab_cab.getValor(i, "valor_teclb"));
                        tab_cab_libro_banco.setValor("numero_teclb", tab_cab.getValor(i, "numero_teclb"));
                        tab_cab_libro_banco.setValor("fecha_trans_teclb", tab_cab.getValor(i, "fecha_trans_teclb"));
                        tab_cab_libro_banco.setValor("fecha_venci_teclb", tab_cab.getValor(i, "fecha_trans_teclb"));
                        tab_cab_libro_banco.setValor("conciliado_teclb", tab_cab.getValor(i, "conciliado_teclb"));
                        tab_cab_libro_banco.setValor("observacion_teclb", observacion);
                        tab_cab_libro_banco.setValor("beneficiari_teclb", tab_cab.getValor(i, "beneficiari_teclb"));
                        if (Integer.parseInt(getSignoTransaccion(tab_cab.getValor(i, "ide_tettb"))) > 0) {
                            tab_cab_libro_banco.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_reversa_menos"));// reversa -
                        } else {
                            tab_cab_libro_banco.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_reversa_mas"));// reversa +
                        }

                    }
//                    tab_cab_libro_banco.guardar();
                }
            }
        }

    }

//    public void reversar(String ide_teclb, String observacion) {
//
//        if (ide_teclb != null && !ide_teclb.isEmpty()) {
//            Tabla tab_cab = utilitario.consultar("SELECT * from tes_cab_libr_banc where ide_teclb=" + ide_teclb);
//            if (tab_cab.getTotalFilas() > 0) {
//                if (tab_cab.getValor(0, "ide_tettb") != null && !tab_cab.getValor(0, "ide_tettb").isEmpty()) {
//                    Tabla tab_libro_bancos = new Tabla();
//                    tab_libro_bancos.setTabla("tes_cab_libr_banc", "ide_teclb", -1);
//                    tab_libro_bancos.setId("ide_teclb");
//                    tab_libro_bancos.setCondicion("ide_teclb=-1");
//                    tab_libro_bancos.ejecutarSql();
//                    for (int i = 0; i < tab_cab.getTotalFilas(); i++) {
//                        tab_libro_bancos.insertar();
//                        tab_libro_bancos.setValor("ide_empr", utilitario.getVariable("ide_empr"));
//                        tab_libro_bancos.setValor("ide_sucu", utilitario.getVariable("ide_empr"));
//                        tab_libro_bancos.setValor("ide_tecba", tab_cab.getValor(i, "ide_tecba"));
//                        tab_libro_bancos.setValor("ide_teelb", tab_cab.getValor(i, "ide_teelb"));
//                        tab_libro_bancos.setValor("ide_cnccc", tab_cab.getValor(i, "ide_cnccc"));
//                        tab_libro_bancos.setValor("valor_teclb", tab_cab.getValor(i, "valor_teclb"));
//                        tab_libro_bancos.setValor("numero_teclb", tab_cab.getValor(i, "numero_teclb"));
//                        tab_libro_bancos.setValor("fecha_trans_teclb", tab_cab.getValor(i, "fecha_trans_teclb"));
//                        tab_libro_bancos.setValor("fecha_venci_teclb", tab_cab.getValor(i, "fecha_trans_teclb"));
//                        tab_libro_bancos.setValor("conciliado_teclb", tab_cab.getValor(i, "conciliado_teclb"));
//                        tab_libro_bancos.setValor("observacion_teclb", observacion);
//                        tab_libro_bancos.setValor("beneficiari_teclb", tab_cab.getValor(i, "beneficiari_teclb"));
//                        if (Integer.parseInt(getSignoTransaccion(tab_cab.getValor(i, "ide_tettb"))) > 0) {
//                            tab_libro_bancos.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_reversa_menos"));// reversa -
//                        } else {
//                            tab_libro_bancos.setValor("ide_tettb", utilitario.getVariable("p_tes_tran_reversa_mas"));// reversa +
//                        }
//                    }
//                    tab_libro_bancos.guardar();
//                }
//            }
//        }
//
//    }
    public String getSignoTransaccion(String ide_tettb) {
        TablaGenerica tab_tipo_transacciones = utilitario.consultar("select *from tes_tip_tran_banc where ide_tettb=" + ide_tettb);
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

    public Tabla getTab_cab_libro_banco(String ide_teclb) {
        tab_cab_libro_banco.setTabla("tes_cab_libr_banc", "ide_teclb", 0);
        tab_cab_libro_banco.setCondicion("ide_teclb=" + ide_teclb);
        tab_cab_libro_banco.ejecutarSql();
        return tab_cab_libro_banco;
    }

    public Tabla getTab_cab_libro_banco() {
        return tab_cab_libro_banco;
    }

    public void setTab_cab_libro_banco(Tabla tab_cab_libro_banco) {
        this.tab_cab_libro_banco = tab_cab_libro_banco;
    }
}
