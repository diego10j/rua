/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.cuentas_x_pagar;

import framework.componentes.Tabla;
import javax.ejb.Stateless;
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
    private Utilitario utilitario = new Utilitario();

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
     * Retorna una sentencia SQL que contiene los detalles de Productos que a
     * comprado un Cliente
     *
     * @param ide_geper
     * @return
     */
    public String getSqlProductosCliente(String ide_geper) {
        return "SELECT cdf.ide_ccdfa,cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa ,iart.nombre_inarti ,cdf.cantidad_ccdfa,cdf.precio_ccdfa,cdf.total_ccdfa \n"
                + "from cxc_deta_factura cdf \n"
                + "left join cxc_cabece_factura cf on cf.ide_cccfa=cdf.ide_cccfa \n"
                + "left join inv_articulo iart on iart.ide_inarti=cdf.ide_inarti \n"
                + "left join cxc_datos_fac df on cf.ide_ccdaf=df.ide_ccdaf "
                + "where cf.ide_geper=" + ide_geper + " and cdf.IDE_EMPR =" + utilitario.getVariable("IDE_EMPR") + " "
                + "ORDER BY cf.fecha_emisi_cccfa,serie_ccdaf, secuencial_cccfa";
    }

    /**
     * Retorna las Transacciones del Cliente
     *
     * @param ide_geper
     * @return
     */
    public String getSqlTransaccionesCliente(String ide_geper) {
        return "SELECT IDE_CCDTR, IDE_CNCCC, nombre_ccttr,DOCUM_RELAC_CCDTR, FECHA_TRANS_CCDTR,case when signo_ccttr = 1 THEN VALOR_CCDTR  end as INGRESOS,case when signo_ccttr = -1 THEN VALOR_CCDTR end as EGRESOS, IDE_TECLB, FECHA_VENCI_CCDTR,    OBSERVACION_CCDTR, NOM_USUA, NUMERO_PAGO_CCDTR \n"
                + "FROM cxc_detall_transa a\n"
                + "INNER JOIN  cxc_tipo_transacc b on a.IDE_CCTTR =b.IDE_CCTTR\n"
                + "INNER JOIN  sis_usuario c on a.IDE_USUA =c.IDE_USUA\n"
                + "WHERE a.IDE_EMPR =" + utilitario.getVariable("IDE_EMPR") + " \n"
                + "AND ide_ccctr in (select ide_ccctr from cxc_cabece_transa where ide_geper=" + ide_geper + ") \n"
                + "ORDER BY ide_ccdtr desc";
    }

}
