/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.inversiones;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless
public class ServicioInversiones extends ServicioBase {

    /**
     * Restorna sql de lista de certificados
     *
     * @param ide_iptin
     * @return
     */
    public String getSqlListaCertificados(String ide_iptin) {
        String condicion = "";
        if (ide_iptin != null && ide_iptin.isEmpty() == false && ide_iptin.equals("null") == false) {
            condicion = "and a.ide_iptin=" + ide_iptin + " ";
        }
        String sql = "select ide_ipcer,a.ide_iptin,nombre_iptin,num_certificado_ipcer,nombre_ipcin,observacion_ipcer,fecha_emision_ipcer,plazo_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,fecha_vence_ipcer,cod_captacion_ipcer,nombre_ipein,ide_cnccc_terminacion,ide_cnccc,ide_cnccc_interes\n"
                + "from iyp_certificado a\n"
                + "inner join iyp_tipo_inversion b on a.ide_iptin=b.ide_iptin\n"
                + "left join  iyp_clase_inversion c on a.ide_ipcin=c.ide_ipcin\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + condicion
                + "order by nombre_iptin,fecha_emision_ipcer desc";
        return sql;
    }

    public String getSqlListaCertificadosVencidos(String ide_iptin) {
        String condicion = "";
        if (ide_iptin != null && ide_iptin.isEmpty() == false && ide_iptin.equals("null") == false) {
            condicion = "and a.ide_iptin=" + ide_iptin + " ";
        }
        String sql = "select ide_ipcer,a.ide_iptin,nombre_iptin,fecha_vence_ipcer,num_certificado_ipcer,nombre_ipcin,observacion_ipcer,fecha_emision_ipcer,plazo_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,cod_captacion_ipcer,nombre_ipein,ide_cnccc_terminacion,ide_cnccc,ide_cnccc_interes\n"
                + "from iyp_certificado a\n"
                + "inner join iyp_tipo_inversion b on a.ide_iptin=b.ide_iptin\n"
                + "left join  iyp_clase_inversion c on a.ide_ipcin=c.ide_ipcin\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "and fecha_vence_ipcer<='" + utilitario.getFechaActual() + "' \n "
                + "AND es_renovacion_ipcer=false  "
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "  "
                + condicion
                + "order by nombre_iptin,fecha_emision_ipcer desc";
        return sql;
    }

    public String getSqlCertificadosSinAsiento(String ide_iptin) {
        String condicion = "";
        if (ide_iptin != null && ide_iptin.isEmpty() == false && ide_iptin.equals("null") == false) {
            condicion = "and a.ide_iptin=" + ide_iptin + " ";
        }
        String sql = "select ide_ipcer,a.ide_iptin,nombre_iptin,fecha_vence_ipcer,num_certificado_ipcer,nombre_ipcin,observacion_ipcer,fecha_emision_ipcer,plazo_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,cod_captacion_ipcer,nombre_ipein,a.ide_geper\n"
                + "from iyp_certificado a\n"
                + "inner join iyp_tipo_inversion b on a.ide_iptin=b.ide_iptin\n"
                + "left join  iyp_clase_inversion c on a.ide_ipcin=c.ide_ipcin\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "and ide_cnccc is null \n "
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "  "
                + condicion
                + "order by nombre_iptin,fecha_emision_ipcer desc";
        return sql;
    }

    public String getSqlCertificadosSinAsientoInteres(String ide_iptin) {
        String condicion = "";
        if (ide_iptin != null && ide_iptin.isEmpty() == false && ide_iptin.equals("null") == false) {
            condicion = "and a.ide_iptin=" + ide_iptin + " ";
        }
        String sql = "select ide_ipcer,a.ide_iptin,nombre_iptin,fecha_vence_ipcer,num_certificado_ipcer,nombre_ipcin,observacion_ipcer,fecha_emision_ipcer,plazo_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,cod_captacion_ipcer,nombre_ipein,a.ide_geper\n"
                + "from iyp_certificado a\n"
                + "inner join iyp_tipo_inversion b on a.ide_iptin=b.ide_iptin\n"
                + "left join  iyp_clase_inversion c on a.ide_ipcin=c.ide_ipcin\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "and ide_cnccc_interes is null \n "
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "  "
                + condicion
                + "order by nombre_iptin,fecha_emision_ipcer desc";
        return sql;
    }

    public String getSqlCertificadosSinAsientoTerminacion(String ide_iptin) {
        String condicion = "";
        if (ide_iptin != null && ide_iptin.isEmpty() == false && ide_iptin.equals("null") == false) {
            condicion = "and a.ide_iptin=" + ide_iptin + " ";
        }
        String sql = "select ide_ipcer,a.ide_iptin,nombre_iptin,fecha_vence_ipcer,num_certificado_ipcer,nombre_ipcin,observacion_ipcer,fecha_emision_ipcer,plazo_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,cod_captacion_ipcer,nombre_ipein,a.ide_geper\n"
                + "from iyp_certificado a\n"
                + "inner join iyp_tipo_inversion b on a.ide_iptin=b.ide_iptin\n"
                + "left join  iyp_clase_inversion c on a.ide_ipcin=c.ide_ipcin\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "and ide_cnccc_terminacion is null \n "
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "  "
                + condicion
                + "order by nombre_iptin,fecha_emision_ipcer desc";
        return sql;
    }

    /**
     * Retorna el secuencial de un certificado
     *
     * @param ide_iptin
     * @return
     */
    public String getSecuenciaCertificado(String ide_iptin) {
        int max = 0;
        TablaGenerica tab_secuencia = utilitario.consultar("select ide_iptin,max(CAST(coalesce(num_certificado_ipcer, '0') AS Integer)) as num_max FROM iyp_certificado WHERE ide_iptin=" + ide_iptin + " and ide_sucu=" + utilitario.getVariable("ide_sucu") + " GROUP BY ide_iptin");
        if (tab_secuencia.isEmpty() == false) {
            try {
                max = Integer.parseInt(tab_secuencia.getValor("num_max"));
            } catch (Exception e) {
            }
        }
        max++;
        String num_max = String.valueOf(max);
        //String ceros = utilitario.generarCero(10 - num_max.length());
        // str_maximo = ceros.concat(num_max);
        return num_max;
    }

    public String getSqlTipoInversionesCombo() {
        return "SELECT ide_iptin,nombre_iptin FROM iyp_tipo_inversion ORDER BY ide_iptin";
    }

}
