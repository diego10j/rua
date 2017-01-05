/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ServicioComprobanteContabilidad {

    @EJB
    private ServicioContabilidadGeneral ser_contabilidad;
    private final Utilitario utilitario = new Utilitario();

    public String getSqlTiposComprobante() {
        return "select ide_cntcm,nombre_cntcm from con_tipo_comproba where ide_empr=" + utilitario.getVariable("ide_empr");
    }

    public String getSqlEstadosComprobante() {
        return "select ide_cneco,nombre_cneco from con_estado_compro where ide_empr=" + utilitario.getVariable("ide_empr");
    }

    public String getSqlLugarAplica() {
        return "select ide_cnlap,nombre_cnlap from con_lugar_aplicac where ide_empr=" + utilitario.getVariable("ide_empr");

    }

    /**
     * Genera el numero de secuencial del comprobante de contabilidad x sucursal
     * a pratir de una fecha
     *
     * @param fecha_trans_cnccc
     * @param ide_cntcm Tipo de Comprobante
     * @return
     */
    public String getSecuencial(String fecha_trans_cnccc, String ide_cntcm) {
        //GENERA el número secuencial de la cabecera del comprobante
        String str_numero = null;
        String str_ano = utilitario.getAnio(fecha_trans_cnccc) + "";
        String str_mes = utilitario.getMes(fecha_trans_cnccc) + "";
        String str_ide_sucu = utilitario.getVariable("ide_sucu");
        //SELECCIONA EL MAXIMO SEGUN EL MES Y EL AÑO 
        TablaGenerica tab_max = utilitario.consultar("SELECT count(NUMERO_CNCCC) as cod,max(cast( substr(NUMERO_CNCCC,8) as NUMERIC)) AS MAXIMO FROM CON_CAB_COMP_CONT WHERE extract(year from FECHA_TRANS_CNCCC) ='" + str_ano + "' AND extract(month from FECHA_TRANS_CNCCC) ='" + str_mes + "' AND IDE_SUCU=" + str_ide_sucu + " and ide_cntcm=" + ide_cntcm);
        String str_maximo = "0";
        if (tab_max.getTotalFilas() > 0) {
            str_maximo = tab_max.getValor("MAXIMO");
            if (str_maximo == null || str_maximo.isEmpty()) {
                str_maximo = "0";
            }
            long lon_siguiente = 0;
            try {
                lon_siguiente = Long.parseLong(str_maximo) + 1;
            } catch (Exception e) {
            }
            str_maximo = lon_siguiente + "";
        }
        str_maximo = utilitario.generarCero(8 - str_maximo.length()) + str_maximo;
        str_numero = str_ano + str_mes + str_ide_sucu + str_maximo;
        return str_numero;
    }

    /**
     * Reversa un comprobante
     *
     * @param ide_cnccc
     * @param observacion
     * @return
     */
    public String reversarComprobante(String ide_cnccc, String observacion) {

        //Busca Asiento 
        TablaGenerica tab_busca_cabecera = getCabeceraComprobante(ide_cnccc);
        TablaGenerica tab_busca_detalles = getDetallesComprobante(ide_cnccc);
        System.out.println("*****reversar asiento " + ide_cnccc);
        if (tab_busca_cabecera.getTotalFilas() > 0) {
            //Genera asiento de reversa
            TablaGenerica tab_cabecera = getCabeceraComprobante();
            TablaGenerica tab_detalles = getDetallesComprobante();

            tab_cabecera.insertar();
            tab_cabecera.setValor("ide_cntcm", tab_busca_cabecera.getValor("ide_cntcm"));
            tab_cabecera.setValor("ide_cneco", utilitario.getVariable("p_con_estado_comprobante_normal"));
            tab_cabecera.setValor("ide_modu", tab_busca_cabecera.getValor("ide_modu"));
            tab_cabecera.setValor("ide_geper", tab_busca_cabecera.getValor("ide_geper"));
            if (observacion == null || observacion.isEmpty()) {
                tab_cabecera.setValor("observacion_cnccc", "Reversa Comprobante Num: " + tab_busca_cabecera.getValor("ide_cnccc") + "   obs. (" + tab_busca_cabecera.getValor("observacion_cnccc") + " )");
            } else {
                tab_cabecera.setValor("observacion_cnccc", "Reversa Comprobante Num: " + tab_busca_cabecera.getValor("ide_cnccc") + "   obs. (" + observacion + " )");
            }

            tab_cabecera.setValor("fecha_trans_cnccc", utilitario.getFechaActual());
            String lugar_aplica_debe = utilitario.getVariable("p_con_lugar_debe");
            String lugar_aplica_haber = utilitario.getVariable("p_con_lugar_haber");
            for (int i = 0; i < tab_busca_detalles.getTotalFilas(); i++) {
                tab_detalles.insertar();
                if (tab_busca_detalles.getValor(i, "ide_cnlap").equals(lugar_aplica_debe)) {
                    tab_detalles.setValor("ide_cnlap", lugar_aplica_haber);
                } else {
                    tab_detalles.setValor("ide_cnlap", lugar_aplica_debe);
                }
                tab_detalles.setValor("ide_cndpc", tab_busca_detalles.getValor(i, "ide_cndpc"));
                tab_detalles.setValor("valor_cndcc", tab_busca_detalles.getValor(i, "valor_cndcc"));
            }
            return generarAsientoContable(tab_cabecera, tab_detalles);
        }
        return null;
    }

    /**
     * Genera y valida un asiento contable
     *
     * @param tab_cabecera
     * @param tab_detalle
     * @return ide_cnccc generado ; null si fallo
     */
    public String generarAsientoContable(TablaGenerica tab_cabecera, TablaGenerica tab_detalle) {
        if (this.validarComprobane(tab_cabecera, tab_detalle)) {

            tab_detalle = this.resumirComprobante(tab_detalle);
            tab_cabecera.setValor("hora_sistem_cnccc", utilitario.getHoraActual()); //hora del sistema
            tab_cabecera.setValor("fecha_siste_cnccc", utilitario.getFechaActual());//fecha del sistema
            tab_cabecera.setValor("ide_usua", utilitario.getVariable("ide_usua")); //usuario
            if (tab_cabecera.getValor("ide_cneco") == null) {
                //Pone estado normal x defecto si es null
                tab_cabecera.setValor("ide_cneco", utilitario.getVariable("p_con_estado_comprobante_normal"));
            }
            if (tab_cabecera.getValor("fecha_trans_cnccc") == null) {
                //Pone fecha actual x defecto si es null
                tab_cabecera.setValor("fecha_trans_cnccc", utilitario.getFechaActual());
            }

            tab_cabecera.setValor("numero_cnccc", this.getSecuencial(tab_cabecera.getValor("fecha_trans_cnccc"),tab_cabecera.getValor("ide_cntcm")));
            tab_cabecera.guardar();
            String ide_cnccc = tab_cabecera.getValor("ide_cnccc");
            tab_detalle.getColumna("ide_cnccc").setValorDefecto(tab_cabecera.getValor("ide_cnccc"));
            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                tab_detalle.setValor(i, "ide_cnccc", ide_cnccc);
            }
            tab_detalle.guardar();
            System.out.println("Asiento contable generado correctamente: ide_cnccc " + tab_cabecera.getValor("ide_cnccc"));
            return tab_cabecera.getValor("ide_cnccc");
        } else {
            return null;
        }
    }

    /**
     * Valida que una fecha este en el rango del periodo contable activo
     *
     * @param fecha
     * @return
     */
    public boolean isPeriodoValido(String fecha) {
        if (fecha != null && !fecha.isEmpty()) {
            TablaGenerica tab_periodo = utilitario.consultar("SELECT  * FROM con_periodo WHERE ide_sucu=" + utilitario.getVariable("ide_sucu") + " and estado_cnper=true");
            if (tab_periodo.getTotalFilas() > 0) {
                if (tab_periodo.getValor("fecha_fin_cnper") == null || tab_periodo.getValor("fecha_fin_cnper").isEmpty()) {
                    utilitario.agregarMensajeError("No se puede generar el comprobante de Contabilidad", "No existe fecha de cierre de periodo");
                    return false;
                }
            }
            tab_periodo = utilitario.consultar("SELECT  * FROM con_periodo WHERE ide_sucu=" + utilitario.getVariable("ide_sucu") + " and estado_cnper=true AND '" + fecha + "' BETWEEN fecha_inicio_cnper and fecha_fin_cnper ");
            if (tab_periodo.getTotalFilas() > 0) {
                return true;
            } else {
                utilitario.agregarMensajeError("No se puede generar el comprobante de Contabilidad", "El periodo de la fecha del comprobante ya fue cerrado");
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Resume los detalles de los comprobantes, agrupa y acumula cuando hay
     * varias cuentas en el mismo asiento
     *
     * @param tab_detalles
     * @return
     */
    public TablaGenerica resumirComprobante(TablaGenerica tab_detalles) {
        TablaGenerica tab_resumida = getDetallesComprobante();

        for (int i = 0; i < tab_detalles.getTotalFilas(); i++) {
            double dou_acumula = Double.parseDouble(tab_detalles.getValor(i, "valor_cndcc"));;
            if (tab_detalles.getFila(i).isVisible()) {
                for (int j = (i + 1); j < tab_detalles.getTotalFilas(); j++) {
                    if (tab_detalles.getValor(i, "ide_cndpc").equals(tab_detalles.getValor(j, "ide_cndpc"))) {
                        //Misma cuenta agrupa si tienen la misma observacion y el mismo lugar aplica
                        String str_obsi = tab_detalles.getValor(i, "observacion_cndcc") + "";
                        String str_lugari = tab_detalles.getValor(i, "ide_cnlap") + "";
                        if ((str_obsi.equals(tab_detalles.getValor(j, "observacion_cndcc"))) && (str_lugari.equals(tab_detalles.getValor(j, "ide_cnlap")))) {
                            dou_acumula += Double.parseDouble(tab_detalles.getValor(j, "valor_cndcc"));
                            tab_detalles.getFila(j).setVisible(false);
                        }
                    }
                }
                tab_resumida.insertar();
                tab_resumida.setValor("valor_cndcc", utilitario.getFormatoNumero(dou_acumula));
                tab_resumida.setValor("ide_cndpc", tab_detalles.getValor(i, "ide_cndpc"));
                tab_resumida.setValor("observacion_cndcc", tab_detalles.getValor(i, "observacion_cndcc"));
                tab_resumida.setValor("ide_cnlap", tab_detalles.getValor(i, "ide_cnlap"));
                tab_resumida.setValor("ide_cnccc", tab_detalles.getValor(i, "ide_cnccc"));
            }
        }
        return tab_resumida;
    }

    /**
     * Validaciones para crear un comprobante contable
     *
     * @param tab_cabecera
     * @param tab_detalles
     * @return
     */
    public boolean validarComprobane(TablaGenerica tab_cabecera, TablaGenerica tab_detalles) {
        //Valida Perido activo
        if (!isPeriodoValido(tab_cabecera.getValor("fecha_trans_cnccc"))) {
            return false;
        }
        if (tab_detalles.isEmpty()) {
            utilitario.agregarMensajeError("Comprobante no válido", "El comprobante no tiene detales de cuentas");
            return false;
        } else {
            double total_debe = 0.0;
            double total_haber = 0.0;
            double diferencia = 0.0;
            String p_con_lugar_haber = utilitario.getVariable("p_con_lugar_haber");
            String p_con_lugar_debe = utilitario.getVariable("p_con_lugar_debe");
            for (int i = 0; i < tab_detalles.getTotalFilas(); i++) {
                if (tab_detalles.getValor(i, "ide_cnlap").equals(p_con_lugar_debe)) {
                    total_debe += Double.parseDouble(tab_detalles.getValor(i, "valor_cndcc"));
                } else if (tab_detalles.getValor(i, "ide_cnlap").equals(p_con_lugar_haber)) {
                    total_haber += Double.parseDouble(tab_detalles.getValor(i, "valor_cndcc"));
                }
            }
            diferencia = Double.parseDouble(utilitario.getFormatoNumero(total_debe)) - Double.parseDouble(utilitario.getFormatoNumero(total_haber));
            if (diferencia != 0.0) {
                utilitario.agregarMensajeError("Comprobante no válido", "Diferencia :" + diferencia + " entre debe y haber");
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Retorna cabecera comprobante en blanco
     *
     * @return
     */
    public TablaGenerica getCabeceraComprobante() {
        TablaGenerica tab_cabecera = new TablaGenerica();
        tab_cabecera.setTabla("con_cab_comp_cont", "ide_cnccc", -1);
        tab_cabecera.setCondicion("ide_cnccc=-1");
        tab_cabecera.ejecutarSql();
        return tab_cabecera;
    }

    /**
     * Retorna detalles comprobante en blanco
     *
     * @return
     */
    public TablaGenerica getDetallesComprobante() {
        TablaGenerica tab_detalles = new TablaGenerica();
        tab_detalles.setTabla("con_det_comp_cont", "ide_cndcc", -1);
        tab_detalles.setCondicion("ide_cndcc=-1");
        tab_detalles.ejecutarSql();
        return tab_detalles;
    }

    /**
     * retorna la cabecera de un comprobante
     *
     * @param ide_cnccc
     * @return
     */
    public TablaGenerica getCabeceraComprobante(String ide_cnccc) {
        TablaGenerica tab_cabecera = new TablaGenerica();
        tab_cabecera.setTabla("con_cab_comp_cont", "ide_cnccc", -1);
        tab_cabecera.setCondicion("ide_cnccc=" + ide_cnccc);
        tab_cabecera.ejecutarSql();
        return tab_cabecera;

    }

    /**
     * Retorna los detalles de un comprobante
     *
     * @param ide_cnccc
     * @return
     */
    public TablaGenerica getDetallesComprobante(String ide_cnccc) {
        TablaGenerica tab_detalles = new TablaGenerica();
        tab_detalles.setTabla("con_det_comp_cont", "ide_cndcc", -1);
        tab_detalles.setCondicion("ide_cnccc=" + ide_cnccc);
        tab_detalles.ejecutarSql();
        return tab_detalles;
    }

    /**
     * Retorna Sql para visualizar el detalle de un asiento mostrando en
     * columnas separadas el debe, haber
     *
     * @param ide_cnccc
     * @return
     */
    public String getSqlDetalleAsiento(String ide_cnccc) {
        return "select a.ide_cndcc,codig_recur_cndpc,nombre_cndpc,\n"
                + "case when ide_cnlap = 1 THEN valor_cndcc  end as DEBE, case when ide_cnlap = 0 THEN valor_cndcc end as HABER,\n"
                + "observacion_cndcc from con_det_comp_cont a\n"
                + "INNER JOIN con_det_plan_cuen b on a.ide_cndpc=b.ide_cndpc\n"
                + "where ide_cnccc=" + ide_cnccc + "\n"
                + "order by ide_cnlap desc";
    }

    /**
     * Retorna sql para visualizar cabecera de un asiento
     *
     * @param ide_cnccc
     * @return
     */
    public String getSqlCabeceraAsiento(String ide_cnccc) {
        return "select a.ide_cnccc,fecha_trans_cnccc,numero_cnccc,nom_usua,nombre_cntcm,nom_modu,nom_geper,observacion_cnccc\n"
                + ",fecha_siste_cnccc,hora_sistem_cnccc from con_cab_comp_cont a\n"
                + "inner join sis_usuario b on a.ide_usua=b.ide_usua\n"
                + "inner join con_tipo_comproba c on a.ide_cntcm=c.ide_cntcm\n"
                + "left join sis_modulo d on a.ide_modu=d.ide_modu\n"
                + "left join gen_persona e on a.ide_geper=e.ide_geper\n"
                + "where ide_cnccc=" + ide_cnccc + "";
    }

    /**
     * Asigna las configuraciones de la cabecera de un comprobante
     *
     * @param tabla
     */
    public void configurarTablaCabeceraComprobante(Tabla tabla) {
        tabla.setTabla("con_cab_comp_cont", "ide_cnccc", 999);
        tabla.setCondicionSucursal(true);
        tabla.getColumna("ide_cneco").setCombo(getSqlEstadosComprobante());
        tabla.getColumna("fecha_siste_cnccc").setVisible(false);
        tabla.getColumna("numero_cnccc").setEtiqueta();
        tabla.getColumna("numero_cnccc").setEstilo("font-size:11px;font-weight: bold");
        tabla.getColumna("fecha_siste_cnccc").setValorDefecto(utilitario.getFechaActual());
        tabla.getColumna("fecha_trans_cnccc").setValorDefecto(utilitario.getFechaActual());
        tabla.getColumna("hora_sistem_cnccc").setVisible(false);
        tabla.getColumna("hora_sistem_cnccc").setValorDefecto(utilitario.getHoraActual());
        tabla.getColumna("ide_cntcm").setVisible(false);
        //tabla.getColumna("ide_usua").setCombo("sis_usuario", "ide_usua", "nom_usua", "");
        tabla.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tabla.getColumna("ide_usua").setLectura(true);
        tabla.getColumna("ide_modu").setCombo("sis_modulo", "ide_modu", "nom_modu", "");
        tabla.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "");
        tabla.getColumna("ide_geper").setAutoCompletar();
        tabla.getColumna("ide_geper").setRequerida(true);
        tabla.setCondicion("ide_cntcm=-1");//vacio 
        tabla.setValidarInsertar(true);
        tabla.setRecuperarLectura(true);
        tabla.setTipoFormulario(true);
        tabla.getGrid().setColumns(6);
        tabla.getColumna("ide_cneco").setValorDefecto(utilitario.getVariable("p_con_estado_comprobante_normal"));
        tabla.getColumna("ide_cneco").setLectura(true);
        tabla.setCampoOrden("ide_cnccc desc");
    }

    /**
     * Asigna las configuraciones de los detalles de un comprobante
     *
     * @param tabla
     */
    public void configurarTablaDetalleComprobante(Tabla tabla) {
        tabla.setTabla("con_det_comp_cont", "ide_cndcc", -1);
        tabla.getColumna("ide_cndcc").setVisible(false);
        tabla.getColumna("ide_cndpc").setCombo(ser_contabilidad.getSqlCuentas());
        tabla.getColumna("ide_cndpc").setAutoCompletar();
        tabla.getColumna("ide_cndpc").setRequerida(true);
        tabla.getColumna("ide_cndpc").setOrden(1);
        tabla.getColumna("ide_cnlap").setCombo(getSqlLugarAplica());
        //tabla.getColumna("ide_cnlap").setPermitirNullCombo(false);
        tabla.getColumna("ide_cnlap").setMetodoChange("cambioLugarAplica");
        tabla.getColumna("ide_cnlap").setLongitud(-1);
        tabla.getColumna("ide_cnlap").setRequerida(true);
        tabla.getColumna("ide_cnlap").setOrden(2);
        tabla.setCampoOrden("ide_cnlap desc");
        tabla.getColumna("valor_cndcc").setMetodoChange("ingresaCantidad");
        tabla.setCampoOrden("ide_cnlap desc");
        tabla.setRows(10);
    }

    public boolean anularComprobante(String ide_cnccc) {
        TablaGenerica tab_busca = utilitario.consultar("SELECT * FROM con_cab_comp_cont where ide_cnccc=" + ide_cnccc);
        String p_con_estado_comprobante_anulado = utilitario.getVariable("p_con_estado_comprobante_anulado");
        if (tab_busca.getTotalFilas() > 0) {
            // if (!tab_busca.getValor("ide_cneco").equals(p_con_estado_comprobante_anulado)) {
            utilitario.getConexion().agregarSqlPantalla("update con_cab_comp_cont set ide_cneco=" + p_con_estado_comprobante_anulado + " where ide_cnccc=" + ide_cnccc);
            utilitario.getConexion().agregarSqlPantalla("UPDATE con_det_comp_cont set valor_cndcc=0 where ide_cnccc=" + ide_cnccc);
            return true;
            //  }

        }
        return false;
    }

    /**
     * Retorna la configuracion presupuestaria para una cuenta contable
     * dependiendo del lugar aplica
     *
     * @param ide_cndpc
     * @param ide_cnlap
     * @return
     */
    public String getSqlAsociacionPresupuestaria(String ide_cndpc, String ide_cnlap) {
        return "select ide_prasp,a.ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla,detalle_prmop from pre_asociacion_presupuestaria a\n"
                + "left join pre_clasificador b on a.ide_prcla=b.ide_prcla and activo_prcla= true\n"
                + "left join pre_movimiento_presupuestario c on a.ide_prmop=c.ide_prmop  and activo_prmop=true\n"
                + "where ide_cndpc=" + ide_cndpc + " and ide_cnlap =" + ide_cnlap + "";
    }

}
