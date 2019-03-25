/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.ServicioBase;
import servicios.contabilidad.ServicioConfiguracion;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ServicioCliente extends ServicioBase {

    /**
     * Codigo Padre de todos los clientes para el campo GEN_IDE_GEPER
     */
    public final static String P_PADRE_CLIENTES = "1";

    @EJB
    private ServicioConfiguracion ser_configuracion;

    @PostConstruct
    public void init() {
        //Recupera todos los parametros que se van a ocupar en el EJB
        parametros = utilitario.getVariables("p_cxc_estado_factura_normal");
    }

    /**
     * Retorna los datos de un cliente
     *
     * @param ide_geper Cliente
     * @return
     */
    public TablaGenerica getCliente(String ide_geper) {
        return utilitario.consultar("select * from gen_persona where ide_geper=" + ide_geper);
    }

    /**
     * Retorna la cuenta configurada del Cliente con el identificador CUENTA POR
     * COBRAR
     *
     * @param ide_geper Cliente
     * @return
     */
    public String getCuentaCliente(String ide_geper) {
        return ser_configuracion.getCuentaPersona("CUENTA POR COBRAR", ide_geper);
    }

    /**
     * Retorna la sentencia SQL para actualizar la configuracion de la cuenta
     * del cliente
     *
     * @param ide_geper Cliente
     * @param ide_cndpc Nueva Cuenta
     * @return
     */
    public String getSqlActualizarCuentaCliente(String ide_geper, String ide_cndpc) {
        return "update con_det_conf_asie "
                + "set ide_cndpc=" + ide_cndpc + " "
                + "where ide_geper=" + ide_geper + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR COBRAR");
    }

    /**
     * Retorna si un cliente tiene configurada una cuenta contable
     *
     * @param ide_geper
     * @return
     */
    public boolean isTieneCuentaConfiguradaCliente(String ide_geper) {
        return !utilitario.consultar("Select * from con_det_conf_asie "
                + "where ide_geper=" + ide_geper + " "
                + "and ide_cnvca =" + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR COBRAR")).isEmpty();
    }

    /**
     * Retorna la sentencia SQL para insertar la configuracion de la cuenta del
     * cliente
     *
     * @param ide_geper
     * @param ide_cndpc
     * @return
     */
    public String getSqlInsertarCuentaCliente(String ide_geper, String ide_cndpc) {
        return "insert into con_det_conf_asie (ide_cndca,ide_geper,ide_cndpc,ide_cnvca)"
                + "values (" + utilitario.getConexion().getMaximo("con_det_conf_asie", "ide_cndca", 1)
                + ", " + ide_geper + ", " + ide_cndpc + ","
                + ser_configuracion.getCodigoVigenciaIdentificador("CUENTA POR COBRAR") + " )";
    }

    /**
     * Reorna la sentecnia SQL para que se utilice en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboClientes() {
        return "select ide_geper,identificac_geper,nom_geper from gen_persona where es_cliente_geper is TRUE and identificac_geper is not null order by nom_geper ";
    }

    public String getSqlListaClientes() {
        return "select ide_geper,codigo_geper,identificac_geper,nom_geper,correo_geper,direccion_geper,telefono_geper,contacto_geper,nombre_cndfp as FORMA_PAGO from gen_persona a left join con_deta_forma_pago b on a.ide_cndfp=b.ide_cndfp where es_cliente_geper is TRUE and identificac_geper is not null order by nom_geper ";
    }

    /**
     * Asigna las configuraciones de un cliente
     *
     * @param tabla
     */
    public void configurarTablaCliente(Tabla tabla) {

        tabla.setTabla("gen_persona", "ide_geper", -1);
        tabla.setCondicion("es_cliente_geper=true and ide_geper=-1");
        tabla.getColumna("es_cliente_geper").setValorDefecto("true");
        tabla.getColumna("es_cliente_geper").setVisible(false);
        tabla.getColumna("codigo_geper").setLectura(false);
        tabla.getColumna("nivel_geper").setValorDefecto("HIJO");
        tabla.getColumna("nivel_geper").setPermitirNullCombo(true);
        tabla.getColumna("ide_rhtro").setVisible(false);
        tabla.getColumna("ide_rhcon").setVisible(false);
        tabla.getColumna("ide_teban").setVisible(false);
        tabla.getColumna("rep_ide_geper").setVisible(false);
        tabla.getColumna("ide_getid").setCombo("gen_tipo_identifi", "ide_getid", "nombre_getid", "");
        // tabla.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
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
        tabla.getColumna("NIVEL_GEPER").setVisible(false);
        tabla.getColumna("GEN_IDE_GEPER").setVisible(true);
        tabla.getColumna("GEN_IDE_GEPER").setCombo("select ide_geper,nom_geper from gen_persona where nivel_geper ='PADRE' and es_cliente_geper=true order by nom_geper");
        tabla.getColumna("GEN_IDE_GEPER").setPermitirNullCombo(false);
        tabla.getColumna("GEN_IDE_GEPER").setRequerida(true);
        tabla.getColumna("ide_tetcb").setVisible(false);
        tabla.getColumna("ide_coepr").setVisible(false);
        tabla.getColumna("es_proveedo_geper").setVisible(false);
        //tabla.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        //tabla.getColumna("ide_gegen").setCombo("gen_genero", "ide_gegen", "nombre_gegen", "");
        tabla.getColumna("ide_vgtcl").setCombo("ven_tipo_cliente", "ide_vgtcl", "nombre_vgtcl", "");
        tabla.getColumna("ide_vgecl").setCombo("ven_estado_client", "ide_vgecl", "nombre_vgecl", "");
        tabla.getColumna("ide_vgven").setVisible(true);
        tabla.getColumna("ide_vgven").setCombo("SELECT ide_vgven,nombre_vgven FROM ven_vendedor ORDER BY nombre_vgven");
        tabla.getColumna("jornada_inicio_geper").setVisible(false);
        tabla.getColumna("jornada_fin_geper").setVisible(false);
        tabla.getColumna("tipo_sangre_geper").setVisible(false);
        tabla.getColumna("tipo_sangre_geper").setVisible(false);
        tabla.getColumna("jornada_fin_geper").setVisible(false);
        tabla.getColumna("jornada_inicio_geper").setVisible(false);
        tabla.getColumna("numero_geper").setVisible(false);
        tabla.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "ide_cncfp!=3");
        tabla.getColumna("ide_cndfp").setVisible(true);
        tabla.setTipoFormulario(true);
        tabla.getGrid().setColumns(4);
    }

    /**
     * Retorna una sentencia SQL que contiene los detalles de Productos X
     * SUCURSAL que a comprado un Cliente en un rango de fechas
     *
     * @param ide_geper
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlProductosCliente(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT cdf.ide_ccdfa,cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa ,iart.nombre_inarti ,observacion_ccdfa ,cdf.cantidad_ccdfa,cdf.precio_ccdfa,cdf.total_ccdfa \n"
                + "from cxc_deta_factura cdf \n"
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=cdf.ide_cccfa \n"
                + "left join inv_articulo iart on iart.ide_inarti=cdf.ide_inarti \n"
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "where cf.ide_geper=" + ide_geper + " "
                + "and cdf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "and cf.fecha_emisi_cccfa  BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "ORDER BY cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa";
    }

    /**
     * Retorna las Transacciones del Cliente X SUCURSAL
     *
     * @param ide_geper
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlTransaccionesCliente(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT a.ide_ccdtr,FECHA_TRANS_CCDTR, IDE_CNCCC, nombre_ccttr as TRANSACCION,DOCUM_RELAC_CCDTR, case when signo_ccttr = 1 THEN VALOR_CCDTR  end as INGRESOS,case when signo_ccttr = -1 THEN VALOR_CCDTR end as EGRESOS, '' SALDO,OBSERVACION_CCDTR as OBSERVACION, NOM_USUA as USUARIO, FECHA_VENCI_CCDTR,IDE_TECLB,NUMERO_PAGO_CCDTR "
                + "FROM cxc_detall_transa a "
                + "INNER JOIN  cxc_tipo_transacc b on a.IDE_CCTTR =b.IDE_CCTTR "
                + "INNER JOIN  sis_usuario c on a.IDE_USUA =c.IDE_USUA "
                + "INNER JOIN cxc_cabece_transa d on a.ide_ccctr=d.ide_ccctr "
                + "WHERE ide_geper=" + ide_geper + " "
                + "AND a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "AND FECHA_TRANS_CCDTR BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "ORDER BY FECHA_TRANS_CCDTR,a.IDE_CCDTR";
    }

    /**
     * Retorna el saldo inicial de un Cliente X SUCURSAL a una fecha determinada
     *
     * @param ide_geper
     * @param fecha
     * @return
     */
    public double getSaldoInicialCliente(String ide_geper, String fecha) {
        double saldo = 0;
        String sql = "select ide_geper,sum(valor_ccdtr* signo_ccttr) as valor from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "where ide_geper=" + ide_geper + " "
                + "and fecha_trans_ccdtr <'" + fecha + "' "
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

    /**
     * Retorna el saldo de un Cliente X SUCURSAL
     *
     * @param ide_geper
     * @return
     */
    public double getSaldoCliente(String ide_geper) {
        double saldo = 0;
        String sql = "select ide_geper,sum(valor_ccdtr* signo_ccttr) as valor from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "where ide_geper=" + ide_geper + " "
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

    /**
     * Retorna sentencia SQL para obtener las facturas por cobrar de un cliente
     * X SUCURSAL
     *
     * @param ide_geper
     * @return
     */
    public String getSqlFacturasPorCobrar(String ide_geper) {
        return "select dt.ide_ccctr,"
                + "case when (cf.fecha_emisi_cccfa) is null then ct.fecha_trans_ccctr else cf.fecha_emisi_cccfa end as FECHA,"
                + "serie_ccdaf,"
                + "cf.secuencial_cccfa,"
                + "cf.total_cccfa,"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar,"
                + "dias_credito_cccfa as dias_credito,"
                + "to_char(fecha_emisi_cccfa + CAST(dias_credito_cccfa||' days' AS INTERVAL),'YYYY-MM-DD')as FECHA_VENCE, "
                + "case when (cf.observacion_cccfa) is NULL then ct.observacion_ccctr else cf.observacion_cccfa end as OBSERVACION "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "where ct.ide_geper=" + ide_geper + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,serie_ccdaf,cf.secuencial_cccfa, "
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa,dias_credito_cccfa "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";
    }

    public String getSqlCuentasPorCobrar(String ide_geper) {

        String str_sql_cxc = "select dt.ide_ccctr,"
                + "dt.ide_cccfa,"
                + "case when (cf.fecha_emisi_cccfa) is null then ct.fecha_trans_ccctr else cf.fecha_emisi_cccfa end,"
                + "cf.secuencial_cccfa,"
                + "cf.total_cccfa,"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cccfa) is NULL then ct.observacion_ccctr else cf.observacion_cccfa end "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "where ct.ide_geper=" + ide_geper + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,cf.secuencial_cccfa, "
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";

        return str_sql_cxc;
    }

    /**
     * Ventas Mensuales en un año de un cliente
     *
     * @param ide_geper
     * @param anio
     * @return
     */
    public String getSqlTotalVentasMensualesCliente(String ide_geper, String anio) {
        return "select nombre_gemes,"
                + "(select count(ide_cccfa) as num_facturas from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and ide_geper=" + ide_geper + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_grabada_cccfa) as ventas12 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa) in(" + anio + ") and ide_geper=" + ide_geper + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(base_tarifa0_cccfa+base_no_objeto_iva_cccfa) as ventas0 from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and ide_geper=" + ide_geper + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(valor_iva_cccfa) as iva from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and ide_geper=" + ide_geper + " and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + "),"
                + "(select sum(total_cccfa) as total from cxc_cabece_factura a where EXTRACT(MONTH FROM fecha_emisi_cccfa)=ide_gemes and EXTRACT(YEAR FROM fecha_emisi_cccfa)in(" + anio + ") and ide_geper=" + ide_geper + "  and ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + ") "
                + "from gen_mes "
                + "order by ide_gemes";
    }

    /**
     * Reorna los productos que compra el cliente, con ultima fecha de compra,
     * ultimo precio de venta,
     *
     * @param ide_geper
     * @return
     */
    public String getSqlProductosVendidos(String ide_geper) {

        return "select distinct a.ide_inarti,nombre_inarti,observacion_ccdfa,max(b.fecha_emisi_cccfa) as fecha_ultima_venta,\n"
                + "(select precio_ccdfa from cxc_deta_factura  inner join cxc_cabece_factura  on cxc_deta_factura.ide_cccfa=cxc_cabece_factura.ide_cccfa where ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " and ide_geper=" + ide_geper + " and ide_inarti=a.ide_inarti order by fecha_emisi_cccfa desc limit 1) as ultimo_precio\n"
                + "from cxc_deta_factura a \n"
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa\n"
                + "inner join inv_articulo c on a.ide_inarti=c.ide_inarti\n"
                + "where ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "and ide_geper=" + ide_geper + " "
                + "AND a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "group by a.ide_inarti,nombre_inarti ,observacion_ccdfa\n"
                + "order by nombre_inarti";
    }

    /**
     * Validaciones para crear o modificar un Cliente
     *
     * @return
     */
    public boolean validarCliente(Tabla tab_cliente) {
        if (tab_cliente.getValor("ide_getid") != null && tab_cliente.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_cedula"))) {
            if (utilitario.validarCedula(tab_cliente.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de cédula válida");
                return false;
            }
        }
        if (tab_cliente.getValor("ide_getid") != null && tab_cliente.getValor("ide_getid").equals(utilitario.getVariable("p_gen_tipo_identificacion_ruc"))) {
            if (utilitario.validarRUC(tab_cliente.getValor("identificac_geper"))) {
            } else {
                utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar el número de ruc válido");
                return false;
            }
        }
        if (tab_cliente.getValor("ide_cntco") == null || tab_cliente.getValor("ide_cntco").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe seleccionar el tipo de contribuyente");
            return false;
        }

        if (tab_cliente.getValor("DIRECCION_GEPER") == null || tab_cliente.getValor("DIRECCION_GEPER").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar la dirección");
            return false;
        }

        if (tab_cliente.getValor("TELEFONO_GEPER") == null || tab_cliente.getValor("TELEFONO_GEPER").isEmpty()) {
            utilitario.agregarMensajeError("Error no puede guardar", "Debe ingresar un número de teléfono");
            return false;
        }
        //      }
        return true;
    }

    public String getSqlComboFacturasPorCobrar(String ide_geper) {
        return "select dt.ide_ccctr,\n"
                + "coalesce(nombre_cntdo,'Cuenta por Cobrar'),coalesce(cf.secuencial_cccfa,''),\n"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_cobrar\n"
                + "from cxc_detall_transa dt \n"
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr \n"
                + "left join cxc_cabece_factura  cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr \n"
                + "left join con_tipo_document co on cf.ide_cntdo= co.ide_cntdo \n"
                + "where ct.ide_geper=" + ide_geper + " "
                + "and ct.ide_sucu=" + utilitario.getVariable("ide_sucu") + " "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,cf.secuencial_cccfa,nombre_cntdo, \n"
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa \n"
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 \n"
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";
    }

    /**
     * Retorna los datos principales de clientes
     *
     * @param ide_geper
     * @return
     */
    public String getSqlDatosClientes(String ide_geper) {
        return "select ide_geper,nom_geper as NOMBRE,nombre_getid TIPO_IDENTIFICACION,identificac_geper IDENTIFICACION,direccion_geper DIRECCION,telefono_geper TELEFONO\n"
                + "from gen_persona a\n"
                + "left join gen_tipo_identifi b on a.ide_getid=b.ide_getid\n"
                + "where ide_geper in (" + ide_geper + ") order by nom_geper";
    }

    /**
     * Retorna el ide_geper de un Cliente por Identificación
     *
     * @param identificac_geper Cédula/Ruc/Pasaporte
     * @return
     */
    public String getIdeClienteporIdentificacion(String identificac_geper) {
        return utilitario.consultar("select identificac_geper,ide_geper from gen_persona where es_cliente_geper=true  and  identificac_geper='" + identificac_geper + "'").getValor("ide_geper");
    }

    /**
     * Retorna el codigo_geper de un Cliente
     *
     * @param ide_geper
     * @return
     */
    public String getCodigoCliente(String ide_geper) {
        return utilitario.consultar("select codigo_geper,ide_geper from gen_persona where ide_geper=" + ide_geper).getValor("codigo_geper");
    }

    /**
     * Retorna el total de clientes creados
     *
     * @return
     */
    public int getTotalClientes() {
        TablaGenerica tag = utilitario.consultar("select count(1) as NUMERO, 'total' as TOTAL from gen_persona where es_cliente_geper is TRUE and identificac_geper is not null");
        int total = 0;
        try {
            total = Integer.parseInt(tag.getValor("NUMERO"));
        } catch (Exception e) {
        }
        return total;
    }

    /**
     * Retorna el número de clientes Nuevos en el año
     *
     * @return
     */
    public int getTotalClientesNuevos() {
        TablaGenerica tag = utilitario.consultar("select count(1) as NUMERO, 'total' as TOTAL from gen_persona where es_cliente_geper is TRUE and identificac_geper is not null and  EXTRACT(YEAR FROM fecha_ingre_geper) = " + utilitario.getAnio(utilitario.getFechaActual()));
        int total = 0;
        try {
            total = Integer.parseInt(tag.getValor("NUMERO"));
        } catch (Exception e) {
        }
        return total;
    }

    /**
     * Retorna el total de clientes creados
     *
     * @return
     */
    public String getSqlTotalClientesporTipoContribuyente() {
        return "select a.ide_cntco,nombre_cntco,count(1) as TOTAL\n"
                + "from gen_persona a\n"
                + "left join con_tipo_contribu b on a.ide_cntco = b.ide_cntco\n"
                + "where es_cliente_geper is TRUE and identificac_geper is not null\n"
                + "group by a.ide_cntco,nombre_cntco\n"
                + "ORDER BY 2";
    }

    public String getSqlChequesPosfechadosCliente(String ide_geper) {
        return "select distinct a.ide_teclb,fec_cam_est_teclb,nombre_tettb as TRANSACCION,numero_teclb as NUMERO,beneficiari_teclb as BENEFICIARIO,\n"
                + "valor_teclb as VALOR,fecha_trans_teclb as FECHA_TRANSACCION,'' as NUM_CUENTA_CHEQUE,(SELECT nombre_teban FROM tes_banco where a.ide_teban=ide_teban)as BANCO_CHEQUE,observacion_teclb as OBSERVACION\n"
                + "from tes_cab_libr_banc a\n"
                + "inner join tes_tip_tran_banc b on a.ide_tettb=b.ide_tettb and ide_teelb =0\n"
                + "inner join cxc_detall_transa c on a.ide_teclb=c.ide_teclb and ide_ccttr=10\n" //CH POSFECHADO
                + "inner join cxc_cabece_transa d on c.ide_ccctr=d.ide_ccctr\n"
                + "where a.ide_tettb=13 and (devuelto_teclb=false or devuelto_teclb is null)\n"
                + "and conciliado_teclb=false and a.ide_sucu=" + utilitario.getVariable("IDE_SUCU")
                + "and ide_geper= " + ide_geper + " "
                + "order by fec_cam_est_teclb, ide_teclb";
    }

    /**
     * Busca facturas por cobrar en una fecha para saldar en grupo
     *
     * @param fecha
     * @return
     */
    public String getSqlCuentasPorCobrarGrupo(String fecha) {

        String str_sql_cxc = "select dt.ide_ccctr,"
                + "dt.ide_cccfa,"
                + "case when (cf.fecha_emisi_cccfa) is null then ct.fecha_trans_ccctr else cf.fecha_emisi_cccfa end,"
                + "cf.secuencial_cccfa,"
                + "cf.total_cccfa,"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cccfa) is NULL then nom_geper || ' ' || ct.observacion_ccctr else nom_geper || ' ' || cf.observacion_cccfa end "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "left join gen_persona p on cf.ide_geper=p.ide_geper "
                + "where cf.fecha_emisi_cccfa='" + fecha + "' "
                + "and ct.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,cf.secuencial_cccfa,nom_geper, "
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";

        return str_sql_cxc;
    }

    /**
     * Busca facturas por cobrar en una fecha de pago para contabilizar por
     * grupo
     *
     * @param fecha de pago
     * @return
     */
    public String getSqlCuentasPorCobrarGrupoPension(String fecha) {

        String str_sql_cxc = "select dt.ide_ccctr,"
                + "dt.ide_cccfa,"
                + "case when (cf.fecha_emisi_cccfa) is null then ct.fecha_trans_ccctr else cf.fecha_emisi_cccfa end,"
                + "cf.secuencial_cccfa,"
                + "cf.total_cccfa,"
                + "sum (dt.valor_ccdtr*tt.signo_ccttr) as saldo_x_pagar,"
                + "case when (cf.observacion_cccfa) is NULL then nom_geper || ' ' || ct.observacion_ccctr else nom_geper || ' ' || cf.observacion_cccfa end "
                + "from cxc_detall_transa dt "
                + "left join cxc_cabece_transa ct on dt.ide_ccctr=ct.ide_ccctr "
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=ct.ide_cccfa and cf.ide_ccefa=" + parametros.get("p_cxc_estado_factura_normal") + " "
                + "left join cxc_tipo_transacc tt on tt.ide_ccttr=dt.ide_ccttr "
                + "left join gen_persona p on cf.ide_geper=p.ide_geper "
                + "where dt.ide_cccfa in ( select ide_cccfa from rec_valores where fecha_pago_recva ='" + fecha + "') "
                + "and ct.ide_sucu=" + utilitario.getVariable("IDE_SUCU") + " "
                + "GROUP BY dt.ide_cccfa,dt.ide_ccctr,cf.secuencial_cccfa,nom_geper, "
                + "cf.observacion_cccfa,ct.observacion_ccctr,cf.fecha_emisi_cccfa,ct.fecha_trans_ccctr,cf.total_cccfa "
                + "HAVING sum (dt.valor_ccdtr*tt.signo_ccttr) > 0 "
                + "ORDER BY cf.fecha_emisi_cccfa ASC ,ct.fecha_trans_ccctr ASC,dt.ide_ccctr ASC";

        return str_sql_cxc;
    }

    /**
     * SQL para importar clientes desde archivo excel
     *
     * @return
     */
    public String getSqlClientesImportarArchivo() {
        return "select "
                + "ide_geper AS CODIGO, "
                + "telefono_geper AS ESTADO,"
                + "contacto_geper AS UBICACION,"
                + "telefono_geper AS TIPO_CLIENTE,"
                + "contacto_geper AS NOMBRE_COMPLETO,"
                + "contacto_geper AS NOMBRE,"
                + "telefono_geper AS TIPO_CONTRIBUYENTE,"
                + "telefono_geper AS TIPO_IDENTIFICACION,"
                + "telefono_geper AS IDENTIFICACION,"
                + "contacto_geper AS CONTACTO,"
                + "contacto_geper AS DIRECCION,"
                + "telefono_geper AS TELEFONO1,"
                + "telefono_geper AS TELEFONO2,"
                + "telefono_geper AS FAX,"
                + "telefono_geper AS MOVIL,"
                + "correo_geper AS CORREO,"
                + "observacion_geper AS OBSERVACION,"
                + "sueldo_geper AS SALDO_INICIAL,"
                + "telefono_geper AS CUENTA_CONTABLE"
                + "from gen_persona where ide_geper=-1";
    }

}
