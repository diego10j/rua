/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_cobrar;

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
public class ServicioCliente {

    /**
     * Codigo Padre de todos los clientes para el campo GEN_IDE_GEPER
     */
    public final static String P_PADRE_CLIENTES = "1";
    private final Utilitario utilitario = new Utilitario();

    @EJB
    private ServicioConfiguracion ser_configuracion;

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

    /**
     * Asigna las configuraciones de un cliente
     *
     * @param tabla
     */
    public void configurarTablaCliente(Tabla tabla) {

        tabla.setTabla("gen_persona", "ide_geper", -1);
        tabla.setCondicion("es_cliente_geper=true");
        tabla.getColumna("es_cliente_geper").setValorDefecto("true");
        tabla.getColumna("es_cliente_geper").setVisible(false);
        tabla.getColumna("nivel_geper").setValorDefecto("HIJO");
        tabla.getColumna("nivel_geper").setPermitirNullCombo(true);
        tabla.getColumna("ide_rhtro").setVisible(false);
        tabla.getColumna("ide_rhcon").setVisible(false);
        tabla.getColumna("ide_teban").setVisible(false);
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
        tabla.getColumna("NIVEL_GEPER").setVisible(false);
        tabla.getColumna("GEN_IDE_GEPER").setValorDefecto(P_PADRE_CLIENTES); //padre de todos los clientes
        tabla.getColumna("ide_tetcb").setVisible(false);
        tabla.getColumna("ide_coepr").setVisible(false);
        tabla.getColumna("es_proveedo_geper").setVisible(false);
        tabla.getColumna("ide_geubi").setCombo("gen_ubicacion", "ide_geubi", "nombre_geubi", "nivel_geubi='HIJO'");
        tabla.getColumna("ide_gegen").setCombo("gen_genero", "ide_gegen", "nombre_gegen", "");
        tabla.getColumna("ide_vgtcl").setCombo("ven_tipo_cliente", "ide_vgtcl", "nombre_vgtcl", "");
        tabla.getColumna("ide_vgecl").setCombo("ven_estado_client", "ide_vgecl", "nombre_vgecl", "");
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
     * SUCURSAL que a comprado un Cliente en un rango de fechas
     *
     * @param ide_geper
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public String getSqlProductosCliente(String ide_geper, String fechaInicio, String fechaFin) {
        return "SELECT cdf.ide_ccdfa,cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa ,iart.nombre_inarti ,cdf.cantidad_ccdfa,cdf.precio_ccdfa,cdf.total_ccdfa \n"
                + "from cxc_deta_factura cdf \n"
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=cdf.ide_cccfa \n"
                + "left join inv_articulo iart on iart.ide_inarti=cdf.ide_inarti \n"
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "where cf.ide_geper=" + ide_geper + " and cdf.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "and cf.fecha_emisi_cccfa  BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "ORDER BY cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa";
    }

    /**
     * Retorna las Transacciones del Cliente X SUCURSAL
     *
     * @param ide_geper
     * @return
     */
    public String getSqlTransaccionesCliente(String ide_geper,String fechaInicio, String fechaFin) {
        return "SELECT FECHA_TRANS_CCDTR,IDE_CCDTR, IDE_CNCCC, nombre_ccttr as TRANSACCION,DOCUM_RELAC_CCDTR, case when signo_ccttr = 1 THEN VALOR_CCDTR  end as INGRESOS,case when signo_ccttr = -1 THEN VALOR_CCDTR end as EGRESOS, '' SALDO,IDE_TECLB,OBSERVACION_CCDTR as OBSERVACION, NOM_USUA as USUARIO, NUMERO_PAGO_CCDTR ,FECHA_VENCI_CCDTR "
                + "FROM cxc_detall_transa a "
                + "INNER JOIN  cxc_tipo_transacc b on a.IDE_CCTTR =b.IDE_CCTTR "
                + "INNER JOIN  sis_usuario c on a.IDE_USUA =c.IDE_USUA "
                + "WHERE a.IDE_SUCU =" + utilitario.getVariable("IDE_SUCU") + " "
                + "AND ide_ccctr in (select ide_ccctr from cxc_cabece_transa where ide_geper=" + ide_geper + ") "
                +" AND FECHA_TRANS_CCDTR BETWEEN '" + fechaInicio + "' and '" + fechaFin + "' "
                + "ORDER BY ide_ccdtr desc";
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

}
