/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_pagar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.contabilidad.ServicioConfiguracion;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ServicioProveedor {

    /**
     * Codigo Padre de todos los proveedores para el campo GEN_IDE_GEPER
     */
    public final static String P_PADRE_PROVEEDORES = "1";
    private final Utilitario utilitario = new Utilitario();

    @EJB
    private ServicioConfiguracion ser_configuracion;

    /**
     * Retorna la cuenta configurada del pROVEEDOR con el identificador CUENTA
     * POR PAGAR
     *
     * @param ide_geper Codigo del Proveedor
     * @return
     */
    public String getCuentaProveedor(String ide_geper) {
        return ser_configuracion.getCuentaPersona("CUENTA POR PAGAR", ide_geper);
    }

    /**
     * Retorna la sentencia SQL para actualizar la configuracion de la cuenta
     * del Proveedor
     *
     * @param ide_geper Proveedor
     * @param ide_cndpc Nueva Cuenta
     * @return
     */
    public String getSqlActualizarCuentaProveedor(String ide_geper, String ide_cndpc) {
        return "update con_det_conf_asie "
                + "set ide_cndpc=" + ide_cndpc + " "
                + "where ide_geper=" + ide_geper + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR PAGAR");
    }

    /**
     * Retorna si un Proveedor tiene configurada una cuenta contable
     *
     * @param ide_geper Proveedor
     * @return
     */
    public boolean isTieneCuentaConfiguradaProveedor(String ide_geper) {
        return !utilitario.consultar("Select * from con_det_conf_asie "
                + "where ide_geper=" + ide_geper + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR PAGAR")).isEmpty();
    }

    /**
     * Retorna la sentencia SQL para insertar la configuracion de la cuenta del
     * Proveedor
     *
     * @param ide_geper Proveedor
     * @param ide_cndpc Cuenta Contable
     * @return
     */
    public String getSqlInsertarCuentaProveedor(String ide_geper, String ide_cndpc) {
        return "insert into con_det_conf_asie (ide_cndca,ide_geper,ide_cndpc,ide_cnvca)"
                + "values (" + utilitario.getConexion().getMaximo("con_det_conf_asie", "ide_cndca", 1)
                + ", " + ide_geper + ", " + ide_cndpc + ","
                + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR PAGAR") + " )";
    }

    /**
     * Reorna la sentecnia SQL para obtener los Proveedor para que se utilice en
     * Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboProveedor() {
        return "select ide_geper,identificac_geper,nom_geper from gen_persona where es_proveedo_geper is TRUE and identificac_geper is not null order by nom_geper ";
    }

    /**
     * Asigna las configuraciones de un Proveedor
     *
     * @param tabla
     */
    public void configurarTablaProveedor(Tabla tabla) {

        tabla.setTabla("gen_persona", "ide_geper", -1);
        tabla.setCondicion("es_proveedo_geper=true");
        tabla.getColumna("es_proveedo_geper").setValorDefecto("true");
        tabla.getColumna("es_proveedo_geper").setVisible(false);
        tabla.getColumna("ES_CLIENTE_GEPER").setVisible(false);
        tabla.getColumna("nivel_geper").setValorDefecto("HIJO");
        tabla.getColumna("nivel_geper").setPermitirNullCombo(true);
        tabla.getColumna("ide_rhtro").setVisible(false);
        tabla.getColumna("ide_rhcon").setVisible(false);
        tabla.getColumna("ide_teban").setVisible(false);
        tabla.getColumna("IDE_VGECL").setVisible(false);
        tabla.getColumna("ide_gegen").setVisible(false);
        tabla.getColumna("IDE_VGTCL").setVisible(false);

        tabla.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "");
        tabla.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
        tabla.getColumna("identificac_geper").setUnico(true);
        tabla.getColumna("nivel_geper").setRequerida(true);
        tabla.getColumna("ide_rheem").setVisible(false);
        tabla.getColumna("factu_hasta_geper").setVisible(false);
        tabla.getColumna("ide_rhtem").setVisible(false);
        tabla.getColumna("ide_rhmse").setVisible(false);
        tabla.getColumna("ide_rhseg").setVisible(false);
        tabla.getColumna("ide_rhcsa").setVisible(false);
        tabla.getColumna("ide_cntco").setCombo("con_tipo_contribu", "ide_cntco", "nombre_cntco", "");
        tabla.getColumna("ide_cntco").setRequerida(true);
        tabla.getColumna("identificac_geper").setRequerida(true);
        tabla.getColumna("ide_getid").setRequerida(true);
        tabla.getColumna("ide_rhfpa").setVisible(false);
        tabla.getColumna("ide_rheor").setVisible(false);
        tabla.getColumna("ide_cotpr").setVisible(false);
        tabla.getColumna("ide_rhrtr").setVisible(false);
        tabla.getColumna("ide_rhtsa").setVisible(false);
        tabla.getColumna("ide_rhtco").setVisible(false);
        tabla.getColumna("ide_geeci").setVisible(false);
        tabla.getColumna("sueldo_geper").setVisible(false);
        tabla.getColumna("fecha_ingre_geper").setVisible(false);
        tabla.getColumna("ide_georg").setVisible(false);
        tabla.getColumna("fecha_ingre_geper").setValorDefecto(utilitario.getFechaActual());
        tabla.getColumna("fecha_ingre_geper").setLectura(true);
        tabla.getColumna("foto_geper").setVisible(false);
        tabla.getColumna("fecha_nacim_geper").setVisible(false);
        tabla.getColumna("fecha_salid_geper").setVisible(false);
        tabla.getColumna("es_empleado_geper").setVisible(false);
        tabla.getColumna("cuent_banco_geper").setVisible(false);
        tabla.getColumna("IDE_RHFRE").setVisible(false);
        tabla.getColumna("IDE_USUA").setVisible(false);
        tabla.getColumna("GEN_IDE_GEPER").setVisible(false);
        tabla.getColumna("GEN_IDE_GEPER").setVisible(true);
        tabla.getColumna("GEN_IDE_GEPER").setCombo("select ide_geper,nom_geper from gen_persona where nivel_geper ='PADRE' and es_proveedo_geper=true order by nom_geper");
        tabla.getColumna("ide_tetcb").setVisible(false);
        tabla.getColumna("ide_coepr").setVisible(false);
        tabla.getColumna("es_proveedo_geper").setVisible(false);
        tabla.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        tabla.getColumna("ide_gegen").setCombo("gen_genero", "ide_gegen", "nombre_gegen", "");
        tabla.getColumna("ide_cotpr").setCombo("com_tipo_proveedo", "ide_cotpr", "nombre_cotpr", "");
        tabla.getColumna("ide_coepr").setCombo("com_estado_provee", "ide_coepr", "nombre_coepr", "");
        tabla.getColumna("ide_vgven").setVisible(false);
        tabla.getColumna("jornada_inicio_geper").setVisible(false);
        tabla.getColumna("jornada_fin_geper").setVisible(false);
        tabla.getColumna("tipo_sangre_geper").setVisible(false);
        tabla.getColumna("tipo_sangre_geper").setVisible(false);
        tabla.getColumna("jornada_fin_geper").setVisible(false);
        tabla.getColumna("jornada_inicio_geper").setVisible(false);
        tabla.getColumna("numero_geper").setVisible(false);
        tabla.setTipoFormulario(true);
        tabla.getGrid().setColumns(4);
    }

    /**
     * Retorna una sentencia SQL que contiene los detalles de Productos X
     * SUCURSAL que compramos a un Proveedor en un rango de fechas
     *
     * @param ide_geper Proveedor
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlProductosProveedor(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT cdf.ide_cpdfa,cf.fecha_emisi_cpcfa, numero_cpcfa  ,iart.nombre_inarti ,cdf.cantidad_cpdfa,cdf.precio_cpdfa,cdf.valor_cpdfa "
                + "from cxp_detall_factur cdf "
                + "left join cxp_cabece_factur cf on cf.ide_cpcfa=cdf.ide_cpcfa "
                + "left join inv_articulo iart on iart.ide_inarti=cdf.ide_inarti "
                + "where cf.ide_geper=" + ide_geper + " and cdf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "and cf.fecha_emisi_cpcfa  BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and cf.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_normal")
                + " ORDER BY cf.fecha_emisi_cpcfa, numero_cpcfa";
    }

    /**
     * Retorna las Transacciones del Proveedor X SUCURSAL
     *
     * @param ide_geper Proveedor
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlTransaccionesProveedor(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT fecha_trans_cpdtr,ide_cpdtr, IDE_CNCCC, nombre_cpttr as TRANSACCION,docum_relac_cpdtr, case when signo_cpttr = 1 THEN valor_cpdtr  end as INGRESOS,case when signo_cpttr = -1 THEN valor_cpdtr end as EGRESOS, '' SALDO,IDE_TECLB,observacion_cpdtr as OBSERVACION, NOM_USUA as USUARIO, numero_pago_cpdtr ,fecha_venci_cpdtr "
                + "FROM cxp_detall_transa a "
                + "INNER JOIN  cxp_tipo_transacc b on a.ide_cpttr =b.ide_cpttr "
                + "INNER JOIN  sis_usuario c on a.IDE_USUA =c.IDE_USUA "
                + "INNER JOIN cxp_cabece_transa d on a.ide_cpctr=d.ide_cpctr "
                + "WHERE a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "AND  ide_geper=" + ide_geper + " "
                + "AND fecha_trans_cpdtr BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "ORDER BY fecha_trans_cpdtr,ide_cpdtr";
    }

    /**
     * Retorna el saldo inicial de un Proveedor X SUCURSAL a una fecha
     * determinada
     *
     * @param ide_geper Proveedor
     * @param fecha
     * @return
     */
    public double getSaldoInicialProveedor(String ide_geper, String fecha) {
        double saldo = 0;
        String sql = "select ide_geper,sum(valor_cpdtr* signo_cpttr) as valor from cxp_detall_transa dt "
                + "left join cxp_cabece_transa ct on dt.ide_cpctr=ct.ide_cpctr "
                + "left join cxp_tipo_transacc tt on tt.ide_cpttr=dt.ide_cpttr "
                + "where ide_geper=" + ide_geper + " "
                + "and fecha_trans_cpdtr <'" + fecha + "' "
                + "and dt.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "group by ide_geper";
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

    public String getSqlCuentasPorPagar(String ide_geper) {
        String str_sql_cxp = "select dt.ide_cpctr,"
                + "dt.ide_cpcfa,"
                + "case when (cf.fecha_emisi_cpcfa) is null then ct.fecha_trans_cpctr else cf.fecha_emisi_cpcfa end,"
                + "cf.numero_cpcfa,"
                + "cf.total_cpcfa,"
                + "sum (dt.valor_cpdtr*tt.signo_cpttr)-sum (dt.valor_anticipo_cpdtr) as saldo_x_pagar, "
                + "case when (cf.observacion_cpcfa) is NULL then ct.observacion_cpctr else cf.observacion_cpcfa end "
                + "from cxp_detall_transa dt "
                + "left join cxp_cabece_transa ct on dt.ide_cpctr=ct.ide_cpctr "
                + "left join cxp_cabece_factur cf on cf.ide_cpcfa=ct.ide_cpcfa and cf.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_normal") + " "
                + "left join cxp_tipo_transacc tt on tt.ide_cpttr=dt.ide_cpttr "
                + "where ct.ide_geper=" + ide_geper + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "GROUP BY dt.ide_cpcfa,dt.ide_cpctr,cf.numero_cpcfa, "
                + "cf.observacion_cpcfa,ct.observacion_cpctr,cf.fecha_emisi_cpcfa,ct.fecha_trans_cpctr,cf.total_cpcfa "
                + "HAVING sum (dt.valor_cpdtr*tt.signo_cpttr)-sum (dt.valor_anticipo_cpdtr) > 0 "
                + "ORDER BY cf.fecha_emisi_cpcfa ASC ,ct.fecha_trans_cpctr ASC,dt.ide_cpctr ASC";
        return str_sql_cxp;
    }

    /**
     * Retorna sentencia SQL para obtener las facturas por cobrar de un cliente
     * X SUCURSAL
     *
     * @param ide_geper
     * @return
     */
    public String getSqlFacturasPorPagar(String ide_geper) {
        return "select dt.ide_cpctr,"
                + "dt.ide_cpcfa,"
                + "case when (cf.fecha_emisi_cpcfa) is null then ct.fecha_trans_cpctr else cf.fecha_emisi_cpcfa end as FECHA,"
                + "nombre_cntdo,cf.numero_cpcfa,"
                + "cf.total_cpcfa,"
                + "sum (dt.valor_cpdtr*tt.signo_cpttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cpcfa) is NULL then ct.observacion_cpctr else cf.observacion_cpcfa end as OBSERVACION "
                + "from cxp_detall_transa dt "
                + "left join cxp_cabece_transa ct on dt.ide_cpctr=ct.ide_cpctr "
                + "left join cxp_cabece_factur cf on cf.ide_cpcfa=ct.ide_cpcfa and cf.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_normal") + " "
                + "left join cxp_tipo_transacc tt on tt.ide_cpttr=dt.ide_cpttr "
                + "left join con_tipo_document co on cf.ide_cntdo= co.ide_cntdo "
                + "where ct.ide_geper=" + ide_geper + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "GROUP BY dt.ide_cpcfa,dt.ide_cpctr,cf.numero_cpcfa,nombre_cntdo, "
                + "cf.observacion_cpcfa,ct.observacion_cpctr,cf.fecha_emisi_cpcfa,ct.fecha_trans_cpctr,cf.total_cpcfa "
                + "HAVING sum (dt.valor_cpdtr*tt.signo_cpttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cpcfa ASC ,ct.fecha_trans_cpctr ASC,dt.ide_cpctr ASC";
    }

    public String getSqlComboFacturasPorPagar(String ide_geper) {
        return "select dt.ide_cpctr,"
                + "coalesce(nombre_cntdo,'Cuenta por Pagar'),coalesce(cf.numero_cpcfa,''),"
                + "sum (dt.valor_cpdtr*tt.signo_cpttr) as saldo_x_pagar "
                + "from cxp_detall_transa dt "
                + "left join cxp_cabece_transa ct on dt.ide_cpctr=ct.ide_cpctr "
                + "left join cxp_cabece_factur cf on cf.ide_cpcfa=ct.ide_cpcfa and cf.ide_cpefa=" + utilitario.getVariable("p_cxp_estado_factura_normal") + " "
                + "left join cxp_tipo_transacc tt on tt.ide_cpttr=dt.ide_cpttr "
                + "left join con_tipo_document co on cf.ide_cntdo= co.ide_cntdo "
                + "where ct.ide_geper=" + ide_geper + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "GROUP BY dt.ide_cpcfa,dt.ide_cpctr,cf.numero_cpcfa,nombre_cntdo, "
                + "cf.observacion_cpcfa,ct.observacion_cpctr,cf.fecha_emisi_cpcfa,ct.fecha_trans_cpctr,cf.total_cpcfa "
                + "HAVING sum (dt.valor_cpdtr*tt.signo_cpttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cpcfa ASC ,ct.fecha_trans_cpctr ASC,dt.ide_cpctr ASC";
    }

    /**
     * Compras Mensuales en un año de un cliente
     *
     * @param ide_geper
     * @param anio
     * @return
     */
    public String getSqlTotalComprasMensualesProveedor(String ide_geper, String anio) {
        String p_cxp_estado_factura_normal = utilitario.getVariable("p_cxp_estado_factura_normal");
        return "select nombre_gemes,"
                + "(select count(ide_cpcfa) as num_facturas from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ") and ide_geper=" + ide_geper + " and ide_cpefa=" + p_cxp_estado_factura_normal + "),"
                + "(select sum(base_grabada_cpcfa) as ventas12 from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ") and ide_geper=" + ide_geper + "  and ide_cpefa=" + p_cxp_estado_factura_normal + "),"
                + "(select sum(base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa) as ventas0 from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ") and ide_geper=" + ide_geper + "  and ide_cpefa=" + p_cxp_estado_factura_normal + "),"
                + "(select sum(valor_iva_cpcfa) as iva from cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ") and ide_geper=" + ide_geper + " and ide_cpefa=" + p_cxp_estado_factura_normal + "),"
                + "(select sum(total_cpcfa) as total from  cxp_cabece_factur a where EXTRACT(MONTH FROM fecha_emisi_cpcfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cpcfa) in(" + anio + ") and ide_geper=" + ide_geper + "  and ide_cpefa=" + p_cxp_estado_factura_normal + ") "
                + "from gen_mes "
                + "order by ide_gemes";
    }

    /**
     * Reorna los productos que compramos a un proveedor, ultimo precio de
     * compra,
     *
     * @param ide_geper
     * @return
     */
    public String getSqlProductosComprados(String ide_geper) {
        String p_cxp_estado_factura_normal = utilitario.getVariable("p_cxp_estado_factura_normal");
        return "select distinct a.ide_inarti,nombre_inarti,max(b.fecha_emisi_cpcfa) as fecha_ultima_compra,\n"
                + "(select precio_cpdfa from cxp_detall_factur  inner join cxp_cabece_factur  on cxp_detall_factur.ide_cpcfa=cxp_cabece_factur.ide_cpcfa where ide_cpefa=" + p_cxp_estado_factura_normal + " and ide_geper=" + ide_geper + " and ide_inarti=a.ide_inarti order by fecha_emisi_cpcfa desc limit 1) as ultimo_precio\n"
                + "from cxp_detall_factur a \n"
                + "inner join cxp_cabece_factur b on a.ide_cpcfa=b.ide_cpcfa\n"
                + "inner join inv_articulo c on a.ide_inarti=c.ide_inarti\n"
                + "where ide_cpefa=" + p_cxp_estado_factura_normal + " "
                + "and ide_geper=" + ide_geper + " "
                + "AND a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "group by a.ide_inarti,nombre_inarti \n"
                + "order by nombre_inarti";
    }

    /**
     * Retorna el sql con los anios que exuste facturacion en la empresa
     *
     * @return
     */
    public String getSqlAniosCompras() {
        return "select distinct EXTRACT(YEAR FROM fecha_emisi_cpcfa)||'' as anio,EXTRACT(YEAR FROM fecha_emisi_cpcfa)||'' as nom_anio  "
                + "from cxp_cabece_factur where ide_empr=" + utilitario.getVariable("ide_empr") + " order by 1 desc ";
    }

    /**
     * Validaciones para crear o modificar un Proveedor
     *
     * @return
     */
    public boolean validarProveedor(Tabla tab_proveedor) {
        if (tab_proveedor.getValor("ide_getid") != null && tab_proveedor.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
            if (utilitario.validarCedula(tab_proveedor.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de cédula válida");
                return false;
            }
        }
        if (tab_proveedor.getValor("ide_getid") != null && tab_proveedor.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
            if (utilitario.validarRUC(tab_proveedor.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de ruc válido");
                return false;
            }
        }
        if (tab_proveedor.getValor("ide_cntco") == null || tab_proveedor.getValor("ide_cntco").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar el tipo de contribuyente");
            return false;
        }
        //      }
        return true;
    }
}
