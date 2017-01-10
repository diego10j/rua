/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.activos;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioActivosFijos extends ServicioBase {

    @PostConstruct
    public void init() {
        //Recupera todos los parametros que se van a ocupar en el EJB
        //parametros = utilitario.getVariables("p_cxc_estado_factura_normal", "p_cxc_tipo_trans_factura", "p_cxc_tipo_trans_pago");
    }

    /**
     * Retorna la sentencia SQL con el Listado de Activos Fijos de la empresa
     *
     * @return
     */
    public String getSqlListaActivosFijos() {
        return "select ide_acafi,serie_acafi,nombre_inarti,nombre_acafi,descripcion1_acafi,color_acafi,nombre_aceaf ,nom_geper,nombre_acuba as UBICACION,fecha_compra_acafi \n"
                + ",ano_actual_acafi ,anos_uso_acafi,vida_util_acafi,valor_compra_acafi,valor_comercial_acafi,valor_remate_acafi,observacion_acafi\n"
                + "from act_activo_fijo a \n"
                + "left join act_estado_activo_fijo b on a.ide_aceaf=b.ide_aceaf\n"
                + "left join gen_persona c on a.ide_geper=c.ide_geper\n"
                + "left join inv_articulo arti on arti.ide_inarti = a.ide_inarti "
                + "left join act_ubicacion_activo d on a.ide_acuba=d.ide_acuba\n"
                + "where a.ide_empr=" + utilitario.getVariable("IDE_EMPR") + " "
                + "order by nombre_acafi";
    }

    /**
     * Retorna el historial de Asignación de un Activo Fijo
     *
     * @param ide_acafi
     * @return
     */
    public String getSqlHistoriaAsignacionActivo(String ide_acafi) {
        return "select ide_acaaf,ide_acafi,nom_geper,fecha_acaaf,observacion_acaaf,nom_usua from act_asignacion_activo a "
                + "left join gen_persona per on per.ide_geper=a.ide_geper "
                + "left join sis_usuario u on u.ide_usua=a.ide_usua "
                + "where a.ide_acafi=" + ide_acafi + " "
                + "order by a.fecha_acaaf DESC";
    }

    /**
     * Retorna el Transacciones de un Activo Fijo
     *
     * @param ide_acafi
     * @return
     */
    public String getSqlTransaccionesActivo(String ide_acafi) {
        return "select ide_actra,nombre_acttr,fecha_actra,acumulado_actra,valor_actra,\n"
                + "valor_activo_actra,cantidad_actra,observacion_actra,nom_usua\n"
                + "from act_transaccion a\n"
                + "inner join act_tipo_transaccion b on a.ide_acttr=b.ide_acttr\n"
                + "inner join sis_usuario c on a.ide_usua=c.ide_usua\n"
                + "where a.ide_acafi=" + ide_acafi + " "
                + "order by fecha_actra desc";
    }

    /**
     * Retorna todos los activos Datdos de Baja
     *
     * @return
     */
    public String getSqlActivosDadosdeBaja() {
        return "select af.ide_acafi as baja_ide_acafi,nombre_inarti as grupo,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo,nombre_acuba as ubicacion,nom_geper as custodio_actual from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper = af.ide_geper "
                + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                + "where ide_aceaf =" + utilitario.getVariable("p_act_estado_dado_de_baja") + "";
    }

    /**
     * Retorna los activos Fijos de un custodio
     *
     * @param ide_geper
     * @return
     */
    public String getSqlActivosAsignados(String ide_geper) {
        return "select af.ide_acafi as baja_ide_acafi,nombre_inarti as grupo,nombre_acafi as descripcion,serie_acafi as serie,modelo_acafi as modelo,nombre_acuba as ubicacion,nom_geper as custodio_actual from act_activo_fijo af "
                + "left join gen_persona per on per.ide_geper = af.ide_geper "
                + "left join inv_articulo arti on arti.ide_inarti = af.ide_inarti "
                + "left join act_ubicacion_activo acubi on acubi.ide_acuba=af.ide_acuba "
                + "where af.ide_geper =" + ide_geper + "";
    }

}
