/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.activos;

import java.util.List;
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
        return "select ide_acafi,act_ide_acafi,nombre_accla AS CLASE,nombre_inarti as TIPO_ACTIVO,cantidad_acafi as CANTIDAD,codigo_barras_acafi,nombre_aceaf as ESTADO,nom_geper,nombre_acuba as AREA_UBICACION,fecha_compra_acafi \n"
                + " ,anos_uso_acafi,vida_util_acafi,valor_compra_acafi,valor_comercial_acafi,valor_remate_acafi,nombre_gecas as CASA,nombre_geobr as OBRA,observacion_acafi\n"
                + "from act_activo_fijo a \n"
                + "left join act_estado_activo_fijo b on a.ide_aceaf=b.ide_aceaf\n"
                + "left join gen_persona c on a.ide_geper=c.ide_geper\n"
                + "left join inv_articulo arti on arti.ide_inarti = a.ide_inarti "
                + "left join act_ubicacion_activo d on a.ide_acuba=d.ide_acuba\n"
                + "left join gen_casa e on a.ide_gecas=e.ide_gecas\n"
                + "left join gen_obra f on a.ide_geobr=f.ide_geobr\n"
                + "left join act_clase_activo g on a.ide_accla=g.ide_accla\n"
                + "where a.ide_empr=" + utilitario.getVariable("IDE_EMPR") + " "
                + "order by nombre_gecas,nombre_geobr,nombre_accla,nombre_inarti,ide_acafi";
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

    public int getSecuencialActaConstatacion() {
        int maximo = 0;
        List lis_max = utilitario.getConexion().consultar("SELECT max(secuencial_acact)+1  from act_acta_constata where "
                + " ide_empr=" + utilitario.getVariable("ide_empr"));
        if (lis_max.get(0) != null) {
            try {
                maximo = ((Integer.parseInt(lis_max.get(0).toString())));
            } catch (Exception e) {
                maximo = 1;
            }
        } else {
            maximo = 1;
        }
        return maximo;
    }
//    public String getSecuencial(String ide_gecas, String ide_geobr, String ide_acuba, String ide_accla, String ide_inarti) {
//        if (ide_gecas == null || ide_gecas.isEmpty()) {
//            return "";
//        }
//        if (ide_geobr == null || ide_geobr.isEmpty()) {
//            return "";
//        }
//        if (ide_acuba == null || ide_acuba.isEmpty()) {
//            return "";
//        }
//        if (ide_accla == null || ide_accla.isEmpty()) {
//            return "";
//        }
//        if (ide_inarti == null || ide_inarti.isEmpty()) {
//            return "";
//        }
//        int maximo = 0;
//        List lis_max = utilitario.getConexion().consultar("SELECT max(CAST(coalesce(secuencial_acafi, '0') AS BIGINT))+1  from act_activo_fijo where "
//                + " ide_gecas=" + ide_gecas
//                + " and ide_geobr=" + ide_geobr
//                + " and ide_accla=" + ide_accla
//                + " and ide_inarti=" + ide_inarti
//                + " and ide_acuba=" + ide_acuba
//                + " and ide_empr=" + utilitario.getVariable("ide_empr"));
//        if (lis_max.get(0) != null) {
//            try {
//                maximo = ((Integer.parseInt(lis_max.get(0).toString())) + 1);
//            } catch (Exception e) {
//            }
//        } else {
//            maximo = 1;
//        }
//        return String.format("%04d", new Object[]{maximo});
//    }

    public String getSqlActivosHijoMasivo(String ide_acafi) {
        return "select ide_acafi,nombre_inarti as TIPO_ACTIVO,sec_masivo_acafi AS SECUENCIAL,codigo_barras_acafi AS CODIGO_BARRAS,nombre_aceaf AS ESTADO from act_activo_fijo a "
                + "left JOIN inv_articulo b on  a.ide_inarti=b.ide_inarti\n"
                + "left join act_estado_activo_fijo c  on a.ide_aceaf=c.ide_aceaf\n"
                + "where act_ide_acafi=" + ide_acafi + "\n"
                + "order by sec_masivo_acafi";
    }

    public String getSqlListaActivosFijosSinCustodio() {
        return "select ide_acafi,act_ide_acafi,nombre_accla AS CLASE,nombre_inarti as TIPO_ACTIVO,cantidad_acafi as CANTIDAD,codigo_barras_acafi,nombre_aceaf as ESTADO,nombre_acuba as AREA_UBICACION,fecha_compra_acafi \n"
                + " ,anos_uso_acafi,vida_util_acafi,valor_compra_acafi,valor_comercial_acafi,valor_remate_acafi,nombre_gecas as CASA,nombre_geobr as OBRA,observacion_acafi\n"
                + "from act_activo_fijo a \n"
                + "left join act_estado_activo_fijo b on a.ide_aceaf=b.ide_aceaf\n"
                + "left join inv_articulo arti on arti.ide_inarti = a.ide_inarti "
                + "left join act_ubicacion_activo d on a.ide_acuba=d.ide_acuba\n"
                + "left join gen_casa e on a.ide_gecas=e.ide_gecas\n"
                + "left join gen_obra f on a.ide_geobr=f.ide_geobr\n"
                + "left join act_clase_activo g on a.ide_accla=g.ide_accla\n"
                + "where a.ide_empr=" + utilitario.getVariable("IDE_EMPR") + " and a.ide_geper is null and cantidad_acafi=1"
                + "order by nombre_gecas,nombre_geobr,nombre_accla,nombre_inarti,ide_acafi";
    }

    public String getSqlListadoActasConstatacion() {
        return "select a.ide_acact,codigo_acact AS CODIGO,fecha_asigna_acact AS FECHA_ASIGNA,nombre_gecas AS CASA,\n"
                + "nombre_geobr AS OBRA, nombre_acuba AS AREA_UBICACION ,nom_geper AS CUSTODIO,\n"
                + "observacion_acact\n"
                + "from act_acta_constata a\n"
                + "inner join act_ubicacion_activo b on a.ide_acuba=b.ide_acuba\n"
                + "inner join gen_casa e on a.ide_gecas=e.ide_gecas\n"
                + "inner join gen_OBRA f on a.ide_geobr=f.ide_geobr\n"
                + "inner join gen_persona g on a.ide_geper=g.ide_geper\n"
                + "order by codigo_acact";
    }

    public boolean isExisteCodBarras(String codigo_barras_acafi) {
        return !utilitario.consultar("select ide_acafi,codigo_barras_acafi from act_activo_fijo where codigo_barras_acafi='" + codigo_barras_acafi + "'").isEmpty();
    }

}
