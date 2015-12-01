/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_activos_fijos;


import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import pkg_contabilidad.cls_cab_comp_cont;
import pkg_contabilidad.cls_contabilidad;
import pkg_contabilidad.cls_det_comp_cont;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author byron
 */

public class cls_activos_fijos {

    private Utilitario utilitario = new Utilitario();
    private Tabla tab_act_transaccion = new Tabla();
    private Tabla tab_act_activo_fijo = new Tabla();
    private Tabla tab_act_asignacion_activo = new Tabla();
    cls_contabilidad conta = new cls_contabilidad();
    cls_cab_comp_cont cab_com_con;
    List<cls_det_comp_cont> lista_detalles = new ArrayList();

    public cls_activos_fijos() {

        // Tabla act_activo_fijo contiene los activos fijos con todos sus datos de compra , de asignacion y detalles de activo
        tab_act_activo_fijo.setId("tab_act_activo_fijo");
        tab_act_activo_fijo.setTabla("act_activo_fijo", "ide_acafi", -1);
        tab_act_activo_fijo.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_act_activo_fijo.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_act_activo_fijo.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_act_activo_fijo.setCondicion("ide_acafi=-1");
        tab_act_activo_fijo.ejecutarSql();


        // tabla act_transacciones --- aqui se registran todas las transacciones que se dan en el modulo activos fijos
        // como por ejemplo la depreciacion de activos, el ingreso de un activo, dar de baja un activo, finalizacion de vida util de activo
        tab_act_transaccion.setId("tab_act_transaccion");
        tab_act_transaccion.setTabla("act_transaccion", "ide_actra", -1);
        tab_act_transaccion.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_act_transaccion.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_act_transaccion.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_act_transaccion.setCondicion("ide_actra=-1");
        tab_act_transaccion.ejecutarSql();

        // tabla act_asignacion_activo -- contiene el historial de asigancion de activos 
        tab_act_asignacion_activo.setId("tab_act_asignacion_activo");
        tab_act_asignacion_activo.setTabla("act_asignacion_activo", "ide_acaaf", -1);
        tab_act_asignacion_activo.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_act_asignacion_activo.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_act_asignacion_activo.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_act_asignacion_activo.setCondicion("ide_acaaf=-1");
        tab_act_asignacion_activo.ejecutarSql();



    }

    public void generarIngresoActivo(Tabla tab_ingreso_activo, String ide_inarti) {
        if (tab_ingreso_activo.getTotalFilas() > 0 && ide_inarti != null && !ide_inarti.isEmpty()) {
            tab_act_activo_fijo.limpiar();
            tab_act_activo_fijo.insertar();
            tab_act_activo_fijo.setValor("ide_aceaf", tab_ingreso_activo.getValor("ide_aceaf"));
            tab_act_activo_fijo.setValor("ide_inarti", ide_inarti);
            tab_act_activo_fijo.setValor("codigo_recu_acafi", tab_ingreso_activo.getValor("codigo_recu_acafi"));
            tab_act_activo_fijo.setValor("nombre_acafi", tab_ingreso_activo.getValor("nombre_acafi"));
            tab_act_activo_fijo.setValor("fecha_acafi", tab_ingreso_activo.getValor("fecha_acafi"));
            tab_act_activo_fijo.setValor("vida_util_acafi", tab_ingreso_activo.getValor("vida_util_acafi"));
            tab_act_activo_fijo.setValor("valor_compra_acafi", tab_ingreso_activo.getValor("valor_compra_acafi"));
            tab_act_activo_fijo.setValor("fecha_compra_acafi", tab_ingreso_activo.getValor("fecha_compra_acafi"));
            tab_act_activo_fijo.setValor("observacion_acafi", tab_ingreso_activo.getValor("observacion_acafi"));
            tab_act_activo_fijo.setValor("ide_acuba", tab_ingreso_activo.getValor("ide_acuba"));
            tab_act_activo_fijo.setValor("ide_inmar", tab_ingreso_activo.getValor("ide_inmar"));
            tab_act_activo_fijo.setValor("modelo_acafi", tab_ingreso_activo.getValor("modelo_acafi"));
            tab_act_activo_fijo.setValor("serie_acafi", tab_ingreso_activo.getValor("serie_acafi"));
            tab_act_activo_fijo.setValor("ide_geper", tab_ingreso_activo.getValor("ide_geper"));
            tab_act_activo_fijo.setValor("numero_factu_acafi", tab_ingreso_activo.getValor(0, "numero_factu_acafi"));            
            if (tab_ingreso_activo.getValor("valor_comercial_acafi") != null && !tab_ingreso_activo.getValor("valor_comercial_acafi").isEmpty()) {
                tab_act_activo_fijo.setValor("valor_comercial_acafi", tab_ingreso_activo.getValor("valor_comercial_acafi"));
            } else {
                tab_act_activo_fijo.setValor("valor_comercial_acafi", "0");
            }
            if (tab_ingreso_activo.getValor("recidual_acafi") != null && !tab_ingreso_activo.getValor("recidual_acafi").isEmpty()) {
                tab_act_activo_fijo.setValor("recidual_acafi", tab_ingreso_activo.getValor("recidual_acafi"));
            } else {
                tab_act_activo_fijo.setValor("recidual_acafi", "0");
            }
            tab_act_activo_fijo.guardar();
        }

    }

    public void generarIngresoActivo(Tabla tab_ingreso_activo, String ide_inarti, List lis_items) {
        if (tab_ingreso_activo.getTotalFilas() > 0 && ide_inarti != null && !ide_inarti.isEmpty() && lis_items.size() > 0) {
            tab_act_activo_fijo.limpiar();

            for (int j = 0; j < lis_items.size(); j++) {
                tab_act_activo_fijo.insertar();
                tab_act_activo_fijo.setValor("ide_aceaf", tab_ingreso_activo.getValor("ide_aceaf"));
                tab_act_activo_fijo.setValor("ide_inarti", ide_inarti);
                tab_act_activo_fijo.setValor("codigo_recu_acafi", lis_items.get(j) + "");
                tab_act_activo_fijo.setValor("nombre_acafi", tab_ingreso_activo.getValor("nombre_acafi"));
                tab_act_activo_fijo.setValor("fecha_acafi", tab_ingreso_activo.getValor("fecha_acafi"));
                tab_act_activo_fijo.setValor("vida_util_acafi", tab_ingreso_activo.getValor("vida_util_acafi"));
                tab_act_activo_fijo.setValor("valor_compra_acafi", tab_ingreso_activo.getValor("valor_compra_acafi"));
                tab_act_activo_fijo.setValor("fecha_compra_acafi", tab_ingreso_activo.getValor("fecha_compra_acafi"));
                tab_act_activo_fijo.setValor("observacion_acafi", tab_ingreso_activo.getValor("observacion_acafi"));
                tab_act_activo_fijo.setValor("ide_acuba", tab_ingreso_activo.getValor("ide_acuba"));
                tab_act_activo_fijo.setValor("ide_inmar", tab_ingreso_activo.getValor("ide_inmar"));
                tab_act_activo_fijo.setValor("modelo_acafi", tab_ingreso_activo.getValor("modelo_acafi"));
                tab_act_activo_fijo.setValor("serie_acafi", tab_ingreso_activo.getValor("serie_acafi"));
                tab_act_activo_fijo.setValor("ide_geper", tab_ingreso_activo.getValor("ide_geper"));
                if (tab_ingreso_activo.getValor("valor_comercial_acafi") != null && !tab_ingreso_activo.getValor("valor_comercial_acafi").isEmpty()) {
                    tab_act_activo_fijo.setValor("valor_comercial_acafi", tab_ingreso_activo.getValor("valor_comercial_acafi"));
                } else {
                    tab_act_activo_fijo.setValor("valor_comercial_acafi", "0");
                }
                if (tab_ingreso_activo.getValor("recidual_acafi") != null && !tab_ingreso_activo.getValor("recidual_acafi").isEmpty()) {
                    tab_act_activo_fijo.setValor("recidual_acafi", tab_ingreso_activo.getValor("recidual_acafi"));
                } else {
                    tab_act_activo_fijo.setValor("recidual_acafi", "0");
                }
            }
            tab_act_activo_fijo.guardar();
        }

    }

    public void generarAsignacionActivo(String ide_acafi, String ide_geper, String observacion, List lis_item_activo) {
        if (ide_acafi != null && !ide_acafi.isEmpty()
                && ide_geper != null && !ide_geper.isEmpty()
                && observacion != null && !observacion.isEmpty() && lis_item_activo.size() > 0) {
            tab_act_asignacion_activo.limpiar();
            for (int j = 0; j < lis_item_activo.size(); j++) {
                tab_act_asignacion_activo.insertar();
                tab_act_asignacion_activo.setValor("ide_acafi", ide_acafi);
                tab_act_asignacion_activo.setValor("ide_geper", ide_geper);
                tab_act_asignacion_activo.setValor("fecha_acaaf", utilitario.getFechaActual());
                tab_act_asignacion_activo.setValor("observacion_acaaf", observacion);
            }
            tab_act_asignacion_activo.guardar();
        }

    }

    public void generarAsignacionActivo(String ide_acafi, String ide_geper, String observacion) {
        if (ide_acafi != null && !ide_acafi.isEmpty()
                && ide_geper != null && !ide_geper.isEmpty()
                && observacion != null && !observacion.isEmpty()) {
            tab_act_asignacion_activo.limpiar();
            tab_act_asignacion_activo.insertar();
            tab_act_asignacion_activo.setValor("ide_acafi", ide_acafi);
            tab_act_asignacion_activo.setValor("ide_geper", ide_geper);
            tab_act_asignacion_activo.setValor("fecha_acaaf", utilitario.getFechaActual());
            tab_act_asignacion_activo.setValor("observacion_acaaf", observacion);
            tab_act_asignacion_activo.guardar();
        }

    }

    public void generarTransaccionActivo(String ide_acttr, String ide_cnccc, List lis_ide_acafi, double acumulado, double valor_activo, double valor_transaccion, String observacion) {
        if (lis_ide_acafi.size()>0 && ide_acttr != null && !ide_acttr.isEmpty() ) {
            tab_act_transaccion.limpiar();
            for (int j = 0; j < lis_ide_acafi.size(); j++) {
                tab_act_transaccion.insertar();
                tab_act_transaccion.setValor("ide_cnccc", ide_cnccc);
                tab_act_transaccion.setValor("ide_acafi", lis_ide_acafi.get(j)+"");
                tab_act_transaccion.setValor("ide_acttr", ide_acttr);
                tab_act_transaccion.setValor("fecha_actra", utilitario.getFechaActual());
                tab_act_transaccion.setValor("acumulado_actra", utilitario.getFormatoNumero(acumulado));
                tab_act_transaccion.setValor("valor_activo_actra", utilitario.getFormatoNumero(valor_activo));
                tab_act_transaccion.setValor("valor_actra", utilitario.getFormatoNumero(valor_transaccion));
                tab_act_transaccion.setValor("observacion_actra", observacion);
            }
            tab_act_transaccion.guardar();
        }
    }

    public void generarTransaccionActivo(String ide_acttr, String ide_cnccc, String ide_acafi, double acumulado, double valor_activo, double valor_transaccion, String observacion) {
        if (ide_acafi != null && !ide_acafi.isEmpty() && ide_acttr != null && !ide_acttr.isEmpty()) {
            tab_act_transaccion.limpiar();
            tab_act_transaccion.insertar();
            tab_act_transaccion.setValor("ide_cnccc", ide_cnccc);
            tab_act_transaccion.setValor("ide_acafi", ide_acafi);
            tab_act_transaccion.setValor("ide_acttr", ide_acttr);
            tab_act_transaccion.setValor("fecha_actra", utilitario.getFechaActual());
            tab_act_transaccion.setValor("acumulado_actra", utilitario.getFormatoNumero(acumulado));
            tab_act_transaccion.setValor("valor_activo_actra", utilitario.getFormatoNumero(valor_activo));
            tab_act_transaccion.setValor("valor_actra", utilitario.getFormatoNumero(valor_transaccion));
            tab_act_transaccion.setValor("observacion_actra", observacion);
            tab_act_transaccion.guardar();
        }
    }

    public double depreciacionAnual(String fecha_compra, int vida_util, double costo_activo, double residual, String ide_acafi) {

        if (fecha_compra != null && !fecha_compra.isEmpty()
                && vida_util > 0
                && costo_activo > 0
                && residual >= 0
                && ide_acafi != null && !ide_acafi.isEmpty()) {
            int anio_compra = utilitario.getAnio(fecha_compra);
            int mes_compra = utilitario.getMes(fecha_compra);
            int anio_actual = utilitario.getAnio(utilitario.getFechaActual());
            int mes_actual = utilitario.getMes(utilitario.getFechaActual());
            String fecha_tope_depreciacion = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha_compra), vida_util * 365));
            int diferencia_dias = utilitario.getDiferenciasDeFechas(utilitario.getFecha(utilitario.getFechaActual()), utilitario.getFecha(fecha_tope_depreciacion));
            int anio_depreciacion = anio_actual - anio_compra;
            double dp;
            double depre_acu = 0;
            double libro = 0;
            double valor_depreciar = 0;
            int anio_max_depreciado = 0;
            int num_meses_faltantes;
            boolean boo_puede_depreciar = true;
            boolean boo_termina_vida_util = false;
            int periodo = 1;
            List lis_fecha_max = utilitario.getConexion().consultar("select max(fecha_actra) from act_transaccion where ide_acttr=0 and ide_acafi=" + ide_acafi);//0 tipo transaccion depreciacion
            if (lis_fecha_max.get(0) != null) {
                anio_max_depreciado = utilitario.getAnio(lis_fecha_max.get(0).toString());
                int mes_max_depreciado = utilitario.getMes(lis_fecha_max.get(0).toString());
                if (anio_max_depreciado <= anio_actual) {
                    if (anio_max_depreciado == anio_actual && mes_actual == mes_max_depreciado) {
                        boo_puede_depreciar = false;
                    } else {
                        if (diferencia_dias <= -31) {
                            boo_puede_depreciar = false;
                        } else {
                            mes_compra = mes_max_depreciado;
                            anio_depreciacion = anio_actual - anio_max_depreciado;
                            TablaGenerica tab_depreciacion_activo = utilitario.consultar("select ide_acafi,MAX(acumulado_actra) as acumulado,min(valor_activo_actra) as valor_activo,valor_actra from act_transaccion "
                                    + "where ide_acttr=0 and ide_acafi=" + ide_acafi + " group by ide_acafi,valor_actra"); // parametro 0 tipo transaccion depreciacion
                            if (tab_depreciacion_activo.getTotalFilas() > 0) {
                                valor_depreciar = Double.parseDouble(tab_depreciacion_activo.getValor(0, "valor_actra"));
                                depre_acu = Double.parseDouble(tab_depreciacion_activo.getValor(0, "acumulado"));
                                libro = Double.parseDouble(tab_depreciacion_activo.getValor(0, "valor_activo"));
                                boo_puede_depreciar = true;
                                periodo = anio_max_depreciado - anio_compra;
                                if (periodo == 0) {
                                    periodo = 1;
                                }
                                if (mes_max_depreciado == 12) {
                                    periodo = anio_max_depreciado - anio_compra + 1;
                                    anio_max_depreciado = anio_max_depreciado + 1;
                                }
                            } else {
                                if (residual > 0) {
                                    dp = (costo_activo - residual) / vida_util;
                                } else {
                                    dp = costo_activo / vida_util;
                                }
                                valor_depreciar = dp / 12;
                                valor_depreciar = valor_depreciar;
                                libro = costo_activo - depre_acu;
                                boo_puede_depreciar = true;
                                if (diferencia_dias <= -31) {
                                    anio_depreciacion = vida_util;
                                    boo_termina_vida_util = true;
                                }
                                anio_max_depreciado = anio_compra;
                            }
                        }
                    }
                } else {
                    boo_puede_depreciar = false;
                }
            } else {
                if (residual > 0) {
                    dp = (costo_activo - residual) / vida_util;
                } else {
                    dp = costo_activo / vida_util;
                }
                valor_depreciar = dp / 12;
                valor_depreciar = valor_depreciar;
                libro = costo_activo - depre_acu;
                boo_puede_depreciar = true;
                if (diferencia_dias <= -31) {
                    anio_depreciacion = vida_util;
                    boo_termina_vida_util = true;
                }
                anio_max_depreciado = anio_compra;
            }

            if (boo_puede_depreciar) {
                calcularAniosUso(ide_acafi);
                double depre1 = 0;
                double libro1 = 0;
                if (mes_compra < mes_actual) {
                    anio_depreciacion = anio_depreciacion;
                    if (boo_termina_vida_util == false) {
                        num_meses_faltantes = mes_actual - mes_compra;
                    } else {
                        num_meses_faltantes = 0;
                    }
                } else {
                    if (mes_compra == mes_actual) {
                        anio_depreciacion = anio_depreciacion;
                        num_meses_faltantes = 0;
                    } else {
                        if (boo_termina_vida_util == false) {
                            num_meses_faltantes = (12 - mes_compra) + mes_actual;
                            if (anio_depreciacion != 0) {
                                anio_depreciacion = anio_depreciacion - 1;
                            }
                        } else {
                            num_meses_faltantes = 0;
                        }
                    }
                }
                String fecha_actra;

                int mes_depreciacion = 0;
                if (mes_compra == 12) {
                    mes_depreciacion = 1;
                } else {
                    mes_depreciacion = mes_compra + 1;
                }
                int aux_anio = 0;
                int indice_periodo = 0;

                int anio_tope = 2011;
                int anios_a_restar = 0;
                if (anio_depreciacion > 0) {

                    anios_a_restar = anio_compra + anio_depreciacion - anio_tope;
                    if (anios_a_restar > 0) {
                        if (anios_a_restar == 2 && num_meses_faltantes >= 0) {
                            anios_a_restar = anio_depreciacion;
                        } else {

                            double val_actra = depre_acu + valor_depreciar * 12;
                            depre_acu = depre_acu + valor_depreciar * 12;
                            libro = libro - depre_acu;
                            for (int i = 0; i < anio_depreciacion - anios_a_restar; i++) {
                                tab_act_transaccion.insertar();
                                fecha_actra = (anio_max_depreciado + i) + "-" + (mes_compra) + "-" + utilitario.getDia(utilitario.getFechaActual());
                                tab_act_transaccion.setValor("ide_acafi", ide_acafi);
                                tab_act_transaccion.setValor("fecha_actra", utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
                                tab_act_transaccion.setValor("acumulado_actra", depre_acu + "");
                                tab_act_transaccion.setValor("ide_acttr", "0");
                                tab_act_transaccion.setValor("valor_actra", val_actra + "");
                                tab_act_transaccion.setValor("valor_activo_actra", libro + "");
                                tab_act_transaccion.setValor("observacion_actra", getMesenLetras(mes_compra) + " - " + (anio_max_depreciado + i) + " hasta " + getMesenLetras(mes_compra) + " - " + (anio_max_depreciado + i + 1));
                                depre_acu = depre_acu + val_actra;
                                libro = libro - val_actra;
                            }
                            periodo = anio_depreciacion - anios_a_restar + 1;
                            anio_max_depreciado = anio_max_depreciado + anio_depreciacion - anios_a_restar;
                            depre_acu = depre_acu - (valor_depreciar * 12);
                            libro = libro + (valor_depreciar * 12);
                        }
                    }
                } else {
                    anios_a_restar = anio_depreciacion;
                }
                double valor_total_transaccion = 0;
                for (int i = 0; i < (anios_a_restar * 12) + num_meses_faltantes; i++) {
                    depre_acu = depre_acu + valor_depreciar;
                    libro = libro - valor_depreciar;
                    tab_act_transaccion.insertar();
                    fecha_actra = (anio_max_depreciado + aux_anio) + "-" + (mes_depreciacion) + "-" + utilitario.getDia(utilitario.getFechaActual());
                    tab_act_transaccion.setValor("ide_acafi", ide_acafi);
                    tab_act_transaccion.setValor("fecha_actra", utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
                    tab_act_transaccion.setValor("ide_acttr", "0");
                    tab_act_transaccion.setValor("acumulado_actra", depre_acu + "");
                    tab_act_transaccion.setValor("valor_actra", valor_depreciar + "");
                    tab_act_transaccion.setValor("valor_activo_actra", libro + "");
                    tab_act_transaccion.setValor("observacion_actra", getMesenLetras(mes_depreciacion) + " - " + (anio_max_depreciado + aux_anio) + " del Periodo " + (periodo));
                    if (mes_depreciacion == 12) {
                        aux_anio = aux_anio + 1;
                        mes_depreciacion = 1;
                    } else {
                        mes_depreciacion = mes_depreciacion + 1;
                    }
                    indice_periodo = indice_periodo + 1;
                    if (indice_periodo == 12) {
                        periodo = periodo + 1;
                        indice_periodo = 0;
                    }
                    valor_total_transaccion = valor_depreciar + valor_total_transaccion;
                }
                return valor_total_transaccion;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public double depreciacionMensual(String fecha_compra, int vida_util, double costo_activo, double residual, String ide_acafi) {

        double valor_total_transaccion = 0;
        if (fecha_compra != null && !fecha_compra.isEmpty()
                && vida_util > 0
                && costo_activo > 0
                && residual >= 0
                && ide_acafi != null && !ide_acafi.isEmpty()) {

            int anio_compra = utilitario.getAnio(fecha_compra);
            int mes_compra = utilitario.getMes(fecha_compra);
            int anio_actual = utilitario.getAnio(utilitario.getFechaActual());
            int mes_actual = utilitario.getMes(utilitario.getFechaActual());
            String fecha_tope_depreciacion = utilitario.getFormatoFecha(utilitario.sumarDiasFecha(utilitario.getFecha(fecha_compra), vida_util * 365));
            int diferencia_dias = utilitario.getDiferenciasDeFechas(utilitario.getFecha(utilitario.getFechaActual()), utilitario.getFecha(fecha_tope_depreciacion));
            int anio_depreciacion = anio_actual - anio_compra;
            double dp;
            double depre_acu = 0;
            double libro = 0;
            double valor_depreciar = 0;
            int anio_max_depreciado = 0;
            int num_meses_faltantes;
            boolean boo_puede_depreciar = true;
            boolean boo_termina_vida_util = false;
            int periodo = 1;
            List lis_fecha_max = utilitario.getConexion().consultar("select max(fecha_actra) from act_transaccion where ide_acttr=0 and ide_acafi=" + ide_acafi);//0 tipo transaccion depreciacion
            if (lis_fecha_max.get(0) != null) {
                anio_max_depreciado = utilitario.getAnio(lis_fecha_max.get(0).toString());
                int mes_max_depreciado = utilitario.getMes(lis_fecha_max.get(0).toString());
                if (anio_max_depreciado <= anio_actual) {
                    if (anio_max_depreciado == anio_actual && mes_actual == mes_max_depreciado) {
                        boo_puede_depreciar = false;
                    } else {
                        if (diferencia_dias <= -31) {
                            boo_puede_depreciar = false;
                        } else {
                            mes_compra = mes_max_depreciado;
                            anio_depreciacion = anio_actual - anio_max_depreciado;
                            TablaGenerica tab_depreciacion_activo = utilitario.consultar("select ide_acafi,MAX(acumulado_actra) as acumulado,min(valor_activo_actra) as valor_activo,valor_actra from act_transaccion "
                                    + "where ide_acafi=" + ide_acafi + " group by ide_acafi,valor_actra");
                            valor_depreciar = Double.parseDouble(tab_depreciacion_activo.getValor(0, "valor_actra"));
                            depre_acu = Double.parseDouble(tab_depreciacion_activo.getValor(0, "acumulado")) + valor_depreciar;
                            libro = Double.parseDouble(tab_depreciacion_activo.getValor(0, "valor_activo")) - valor_depreciar;
                            boo_puede_depreciar = true;
                            periodo = anio_max_depreciado - anio_compra;
                            if (periodo == 0) {
                                periodo = 1;
                            }
                            if (mes_max_depreciado == 12) {
                                periodo = anio_max_depreciado - anio_compra + 1;
                                anio_max_depreciado = anio_max_depreciado + 1;
                            }
                        }
                    }
                } else {
                    boo_puede_depreciar = false;
                }
            } else {
                if (residual > 0) {
                    dp = (costo_activo - residual) / vida_util;
                } else {
                    dp = costo_activo / vida_util;
                }
                valor_depreciar = dp / 12;
                valor_depreciar = valor_depreciar;
                libro = costo_activo - depre_acu;
                boo_puede_depreciar = true;
                if (diferencia_dias <= -31) {
                    anio_depreciacion = vida_util;
                    boo_termina_vida_util = true;
                }
                anio_max_depreciado = anio_compra;
            }

            if (boo_puede_depreciar) {
                calcularAniosUso(ide_acafi);
                double depre1 = 0;
                double libro1 = 0;
                if (mes_compra < mes_actual) {
                    anio_depreciacion = anio_depreciacion;
                    if (boo_termina_vida_util == false) {
                        num_meses_faltantes = mes_actual - mes_compra;
                    } else {
                        num_meses_faltantes = 0;
                    }
                } else {
                    if (mes_compra == mes_actual) {
                        anio_depreciacion = anio_depreciacion;
                        num_meses_faltantes = 0;
                    } else {
                        if (boo_termina_vida_util == false) {
                            num_meses_faltantes = (12 - mes_compra) + mes_actual;
                            if (anio_depreciacion != 0) {
                                anio_depreciacion = anio_depreciacion - 1;
                            }
                        } else {
                            num_meses_faltantes = 0;
                        }
                    }
                }
                String fecha_actra;

                int mes_depreciacion = 0;
                if (mes_compra == 12) {
                    mes_depreciacion = 1;
                } else {
                    mes_depreciacion = mes_compra + 1;
                }
                int aux_anio = 0;
                int indice_periodo = 0;

                for (int i = 0; i < (anio_depreciacion * 12) + num_meses_faltantes; i++) {
                    depre_acu = depre_acu + valor_depreciar;
                    libro = libro - valor_depreciar;
                    tab_act_transaccion.insertar();
                    fecha_actra = (anio_max_depreciado + aux_anio) + "-" + (mes_depreciacion) + "-" + utilitario.getDia(utilitario.getFechaActual());
                    tab_act_transaccion.setValor("ide_acafi", ide_acafi);
                    tab_act_transaccion.setValor("fecha_actra", utilitario.getFormatoFecha(utilitario.getFecha(fecha_actra)));
                    tab_act_transaccion.setValor("acumulado_actra", depre_acu + "");
                    tab_act_transaccion.setValor("ide_acttr", "0");
                    tab_act_transaccion.setValor("valor_actra", valor_depreciar + "");
                    tab_act_transaccion.setValor("valor_activo_actra", libro + "");
                    tab_act_transaccion.setValor("observacion_actra", getMesenLetras(mes_depreciacion) + " - " + (anio_max_depreciado + aux_anio) + " del Periodo " + (periodo));
                    valor_total_transaccion = valor_depreciar + valor_total_transaccion;
                    if (mes_depreciacion == 12) {
                        aux_anio = aux_anio + 1;
                        mes_depreciacion = 1;
                    } else {
                        mes_depreciacion = mes_depreciacion + 1;
                    }
                    indice_periodo = indice_periodo + 1;
                    if (indice_periodo == 12) {
                        periodo = periodo + 1;
                        indice_periodo = 0;
                    }

                }
                return valor_total_transaccion;
            }
        }
        return 0;
    }
    String ide_cnccc = "";

    public String depreciarActivos(TablaGenerica tab_activos_a_depreciar) {
        //realiza la depreciacion de 50 en 50
        ide_cnccc = "";
        int total_activos_a_depreciar = tab_activos_a_depreciar.getTotalFilas();
        int num_grupos_a_dividir = 0;
        int num_filas_x_grupo = 0;
        if (total_activos_a_depreciar >= 200) {
            int aux = total_activos_a_depreciar / 50;
            if ((aux * 50) >= total_activos_a_depreciar) {
                num_grupos_a_dividir = aux;
                num_filas_x_grupo = 50;
            } else {
                num_grupos_a_dividir = aux + 1;
                num_filas_x_grupo = 50;
            }
        } else {
            num_grupos_a_dividir = 1;
            num_filas_x_grupo = total_activos_a_depreciar;
        }
        int j = 0;
        int fila = 0;
        for (int i = 0; i < num_grupos_a_dividir; i++) {
            tab_act_transaccion.limpiar();
            conta.limpiar();
            lista_detalles.clear();
            cab_com_con = new cls_cab_comp_cont("0", "0", "4", "1294", utilitario.getFechaActual(), "ASIENTO DE DEPRECIACION DE ACTIVOS");
            String str_ide_acafi = "";
            List lis_ide_actra = new ArrayList();
            List lis_ide_acafi = new ArrayList();
            for (fila = j; fila < num_filas_x_grupo; fila++) {
                if (fila < total_activos_a_depreciar) {
                    if (tab_activos_a_depreciar.getValor(fila, "fecha_compra_acafi") != null && !tab_activos_a_depreciar.getValor(fila, "fecha_compra_acafi").isEmpty()) {
                        if (tab_activos_a_depreciar.getValor(fila, "vida_util_acafi") != null && !tab_activos_a_depreciar.getValor(fila, "vida_util_acafi").isEmpty()) {
                            if (tab_activos_a_depreciar.getValor(fila, "valor_compra_acafi") != null && !tab_activos_a_depreciar.getValor(fila, "valor_compra_acafi").isEmpty()) {
                                double residual;
                                if (tab_activos_a_depreciar.getValor(fila, "recidual_acafi") != null && !tab_activos_a_depreciar.getValor(fila, "recidual_acafi").isEmpty()) {
                                    residual = Double.parseDouble(tab_activos_a_depreciar.getValor(fila, "recidual_acafi"));
                                } else {
                                    residual = 0;
                                }
                                double valor_transaccion = depreciacionMensual(tab_activos_a_depreciar.getValor(fila, "fecha_compra_acafi"), Integer.parseInt(tab_activos_a_depreciar.getValor(fila, "vida_util_acafi")), Double.parseDouble(tab_activos_a_depreciar.getValor(fila, "valor_compra_acafi")), residual, tab_activos_a_depreciar.getValor(fila, "ide_acafi"));
//                                double valor_transaccion = depreciacionAnual(tab_activos_a_depreciar.getValor(fila, "fecha_compra_acafi"), Integer.parseInt(tab_activos_a_depreciar.getValor(fila, "vida_util_acafi")), Double.parseDouble(tab_activos_a_depreciar.getValor(fila, "valor_compra_acafi")), residual, tab_activos_a_depreciar.getValor(fila, "ide_acafi"));
                                if (valor_transaccion > 0) {
                                    str_ide_acafi += tab_activos_a_depreciar.getValor(fila, "ide_acafi") + ",";
                                    generarAsientoDepreciacion(tab_activos_a_depreciar.getValor(fila, "ide_inarti"), valor_transaccion, tab_activos_a_depreciar.getValor(fila, "observacion_acafi"), tab_activos_a_depreciar.getValor(fila, "ide_acafi"));
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            if (lista_detalles.size() > 0) {
                tab_act_transaccion.guardar();
                cab_com_con.setDetalles(lista_detalles);
                ide_cnccc = conta.generarAsientoContable(cab_com_con);
                if (ide_cnccc != null && !ide_cnccc.isEmpty()) {
                    do {
                        String ide_acafi = str_ide_acafi.substring(0, str_ide_acafi.indexOf(","));
                        TablaGenerica tab_transacciones = utilitario.consultar("select * from act_transaccion where ide_acafi=" + ide_acafi + " "
                                + "and ide_acttr=0 "
                                + "ORDER BY ide_actra DESC");
                        str_ide_acafi = str_ide_acafi.substring(str_ide_acafi.indexOf(",") + 1, str_ide_acafi.length());
                        if (tab_transacciones.getTotalFilas() == 0) {
                            utilitario.getConexion().agregarSqlPantalla("update act_transaccion set ide_cnccc=" + ide_cnccc + " "
                                    + "where ide_acttr=0 and ide_acafi in (" + ide_acafi + ")");
                        } else {
                            lis_ide_actra.add(tab_transacciones.getValor(0, "ide_actra"));
                            lis_ide_acafi.add(tab_transacciones.getValor(0, "ide_acafi"));
                        }

                    } while (str_ide_acafi.length() > 0);
                    utilitario.getConexion().guardarPantalla();
                    for (int k = 0; k < lis_ide_actra.size(); k++) {
                        utilitario.getConexion().agregarSqlPantalla("update act_transaccion set ide_cnccc=" + ide_cnccc + " "
                                + "where ide_actra>" + lis_ide_actra.get(k) + " and ide_acttr=0 and ide_acafi=" + lis_ide_acafi.get(k) + "");
                    }
                    utilitario.getConexion().guardarPantalla();
                }
            } else {
                utilitario.agregarMensajeInfo("No se encontraron cuentas configuradas para realizar el asiento contable de la depreciacion", "Por favor configurar el asiento para el tipo de activo seleccionado");
            }
            if (num_grupos_a_dividir > 1) {
                j = num_filas_x_grupo + 1;
                num_filas_x_grupo = num_filas_x_grupo + 50;
            }
        }
        return ide_cnccc;
    }
    int i = 1;

    public void generarAsientoDepreciacion(String ide_inarti, double valor_tran, String observacion, String ide_actra) {


        String ide_cndpc_depreciacion = conta.buscarCuentaActivo("INVENTARIO-GASTO-ACTIVO", ide_inarti, "0");// 0 tip tran depreciacion
        // CUENTA PARA DEPRECIACION AXUMULADA DE ACTIVO
        String ide_cndpc_depre_acumulada = conta.buscarCuentaActivo("INVENTARIO-GASTO-ACTIVO", ide_inarti, "3");// 3 tip tran depreciacion ACUMULADA
        System.out.println("ide depreciacion " + ide_cndpc_depreciacion);
        System.out.println("ide depreciacion acumulada " + ide_cndpc_depre_acumulada);
        if (ide_cndpc_depreciacion != null && !ide_cndpc_depreciacion.isEmpty()
                && ide_cndpc_depre_acumulada != null && !ide_cndpc_depre_acumulada.isEmpty()
                && valor_tran > 0) {
            System.out.println("ingresa_detalles ");
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_debe"), ide_cndpc_depreciacion, valor_tran, "" + observacion + " " + ide_actra));
            lista_detalles.add(new cls_det_comp_cont(utilitario.getVariable("p_con_lugar_haber"), ide_cndpc_depre_acumulada, valor_tran, "" + observacion + " " + ide_actra));
        }

    }

    public void calcularAniosUso(String ide_acafi) {
        String fecha_compra = getParametroActivo(ide_acafi, "fecha_compra_acafi");
        String fecha_actual = utilitario.getFechaActual();
        if (fecha_compra != null) {
            double anios_uso = (double) utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_compra), utilitario.getFecha(fecha_actual)) / 365;
            utilitario.getConexion().agregarSqlPantalla("update act_activo_fijo set anos_uso_acafi=" + utilitario.getFormatoNumero(anios_uso, 3) + " where ide_acafi=" + ide_acafi);
        }
    }

    public String getParametroActivo(String ide_acafi, String parametro) {
        if (ide_acafi != null && !ide_acafi.isEmpty() && parametro != null && !parametro.isEmpty()) {
            TablaGenerica tab_act_activo_fijo = utilitario.consultar("select * from act_activo_fijo where ide_acafi=" + ide_acafi);
            if (tab_act_activo_fijo.getTotalFilas() > 0) {
                if (tab_act_activo_fijo.getValor(0, parametro) != null && !tab_act_activo_fijo.getValor(0, parametro).isEmpty()) {
                    return tab_act_activo_fijo.getValor(0, parametro);
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

    public String getMesenLetras(int mes) {
        String mes1 = "";
        if (mes == 1) {
            mes1 = "ENERO";
        }
        if (mes == 2) {
            mes1 = "FEBRERO";
        }
        if (mes == 3) {
            mes1 = "MARZO";
        }
        if (mes == 4) {
            mes1 = "ABRIL";
        }
        if (mes == 5) {
            mes1 = "MAYO";
        }
        if (mes == 6) {
            mes1 = "JUNIO";
        }
        if (mes == 7) {
            mes1 = "JULIO";
        }
        if (mes == 8) {
            mes1 = "AGOSTO";
        }
        if (mes == 9) {
            mes1 = "SEPTIEMBRE";
        }
        if (mes == 10) {
            mes1 = "OCTUBRE";
        }
        if (mes == 11) {
            mes1 = "NOVIEMBRE";
        }
        if (mes == 12) {
            mes1 = "DICIEMBRE";
        }
        return mes1;
    }

    public Tabla getTab_act_transaccion() {
        return tab_act_transaccion;
    }

    public void setTab_act_transaccion(Tabla tab_act_transaccion) {
        this.tab_act_transaccion = tab_act_transaccion;
    }

    public Tabla getTab_act_activo_fijo() {
        return tab_act_activo_fijo;
    }

    public void setTab_act_activo_fijo(Tabla tab_act_activo_fijo) {
        this.tab_act_activo_fijo = tab_act_activo_fijo;
    }
}
