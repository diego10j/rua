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
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + " and nuevo=false \n "
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
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "   and nuevo=false  "
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
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "  and nuevo=false  "
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
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "  and nuevo=false  "
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
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_activo_inversion") + "   and nuevo=false  "
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
        TablaGenerica tab_secuencia = utilitario.consultar("select ide_iptin,max(CAST(coalesce(num_certificado_ipcer, '0') AS Integer)) as num_max FROM iyp_certificado WHERE ide_iptin=" + ide_iptin + "  and nuevo=false and ide_sucu=" + utilitario.getVariable("ide_sucu") + " GROUP BY ide_iptin");
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

    /////////////NEW VERSIÓN
    public String getSqlListaInversionesBancarias() {
        return "SELECT ide_ipcer,nombre_teban as BANCO,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES\n"
                + ",fecha_vence_ipcer AS FECHA_VENCIMIENTO,cancelado,nombre_ipein AS ESTADO,ide_cnccc_terminacion\n"
                + "FROM iyp_certificado  a\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join tes_cuenta_banco b on a.ide_tecba=b.ide_tecba\n"
                + "left join tes_banco c on a.ide_teban=c.ide_teban\n"
                + "where nuevo=true	\n"
                + "and ide_iptin=0\n"
                + "order by nombre_teban,num_certificado_ipcer,fecha_vence_ipcer";
    }
    
    public String getSqlListaInversionesFondo() {
        return "SELECT ide_ipcer,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,ide_cnccc,nom_geper AS BENEFICIARIO,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES\n"
                + ",fecha_vence_ipcer AS FECHA_VENCIMIENTO,cancelado,nombre_ipein AS ESTADO,ide_cnccc_terminacion\n"
                + "FROM iyp_certificado  a\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join gen_persona c on a.ide_geper = c.ide_geper  \n"
                + "where nuevo=true	\n"
                + "and ide_iptin=2\n"
                + "order by num_certificado_ipcer";
    }
    
    public String getSqlListaInversionesCasas() {
        return "SELECT ide_ipcer,a.ide_ipcai,c.nom_geper AS GRUPO,beneficiario_ipcai AS CASAS_OBRAS,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,fecha_vence_ipcer AS FECHA_VENCIMIENTO,\n"
                + "ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES ,cancelado,\n"
                + "nombre_ipein AS ESTADO,ide_cnccc_terminacion \n"
                + "FROM iyp_certificado a \n"
                + "left join iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join iyp_cab_inversion b on a.ide_ipcai=b.ide_ipcai\n"
                + "left join gen_persona c on b.ide_geper = c.ide_geper  \n"
                + "where nuevo=true \n"
                + "and b.ide_iptin=1\n"
                + "order by c.nom_geper,beneficiario_ipcai,fecha_emision_ipcer";
    }
    
    public String getSqlComboListaInversionesCasas() {
        return "SELECT ide_ipcer,beneficiario_ipcai AS CASAS_OBRAS,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,\n"
                + "capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES ,\n"
                + "nombre_ipein AS ESTADO \n"
                + "FROM iyp_certificado a \n"
                + "left join iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join iyp_cab_inversion b on a.ide_ipcai=b.ide_ipcai\n"
                + "where nuevo=true \n"
                + "and b.ide_iptin=1\n"
                + "order by beneficiario_ipcai,fecha_emision_ipcer";
    }
    
    public String getSqlListaInversionesCasasGrupo(String ide_geper) {
        return "SELECT ide_ipcer,b.ide_ipcai,beneficiario_ipcai AS CASAS_OBRAS,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,fecha_vence_ipcer AS FECHA_VENCIMIENTO,\n"
                + "ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES ,\n"
                + "nombre_ipein AS ESTADO \n"
                + "FROM iyp_certificado a \n"
                + "left join iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join iyp_cab_inversion b on a.ide_ipcai=b.ide_ipcai\n"
                + "where nuevo=true \n"
                + "and b.ide_iptin=1 and iyp_ide_ipcer is null \n"
                + "and b.ide_geper=" + ide_geper + "\n"
                + "order by beneficiario_ipcai,fecha_emision_ipcer";
    }
    
    public String getSqlListaRenovaciones(String iyp_ide_ipcer) {
        return "SELECT ide_ipcer,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,fecha_vence_ipcer AS FECHA_VENCIMIENTO,\n"
                + "ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES ,\n"
                + "nombre_ipein AS ESTADO \n"
                + "FROM iyp_certificado a \n"
                + "left join iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join iyp_cab_inversion b on a.ide_ipcai=b.ide_ipcai\n"
                + "where nuevo=true \n"
                + "and b.ide_iptin=1 and iyp_ide_ipcer=" + iyp_ide_ipcer + " \n"
                + "order by fecha_emision_ipcer";
    }
    
    public String getSqlListaRenovacionesBanco(String iyp_ide_ipcer) {
        return "SELECT ide_ipcer,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,fecha_vence_ipcer AS FECHA_VENCIMIENTO,\n"
                + "ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES ,\n"
                + "nombre_ipein AS ESTADO \n"
                + "FROM iyp_certificado a \n"
                + "left join iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where nuevo=true \n"
                + "and a.ide_iptin=0 and iyp_ide_ipcer=" + iyp_ide_ipcer + " \n"
                + "order by fecha_emision_ipcer";
    }
    
    public String getSqlListaInversionesPorBanco(String ide_teban) {
        return "SELECT ide_ipcer,a.ide_teban,nombre_teban,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,fecha_vence_ipcer AS FECHA_VENCIMIENTO\n"
                + ",ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES ,cancelado,\n"
                + "nombre_ipein AS ESTADO \n"
                + "FROM iyp_certificado a \n"
                + "left join iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join tes_banco e on a.ide_teban=e.ide_teban\n"
                + "where nuevo=true \n"
                + "and a.ide_iptin=0 and iyp_ide_ipcer is null                \n"
                + "and a.ide_teban=" + ide_teban + "\n"
                + "order by fecha_emision_ipcer desc";
    }

    /**
     * Reorna la sentecnia SQL para que se utilice en Combos, Autocompletar
     *
     * @return
     */
    public String getSqlComboClientes() {
        return "select ide_geper,identificac_geper,nom_geper from gen_persona where es_cliente_geper is TRUE and identificac_geper is not null order by nom_geper ";
    }
    
    public String getSqlComboGrupoBeneficiarios() {
        return "SELECT a.ide_geper,nom_geper \n"
                + "FROM iyp_cab_inversion a \n"
                + "left join gen_persona b on a.ide_geper = b.ide_geper  \n"
                + "where activo_ipcai=true\n"
                + "GROUP BY a.ide_geper,nom_geper \n"
                + "order by nom_geper";
    }
    
    public String getSecuencialNuevos(String ide_iptin) {
        int max = 0;
        TablaGenerica tab_secuencia = utilitario.consultar("select ide_iptin,max(CAST(coalesce(num_certificado_ipcer, '0') AS Integer)) as num_max FROM iyp_certificado WHERE ide_iptin=" + ide_iptin + "  and nuevo=true and ide_sucu=" + utilitario.getVariable("ide_sucu") + " GROUP BY ide_iptin");
        if (tab_secuencia.isEmpty() == false) {
            try {
                max = Integer.parseInt(tab_secuencia.getValor("num_max"));
            } catch (Exception e) {
            }
        }
        max++;
        String num_max = String.valueOf(max);
        return num_max;
    }
    
    public String getSqlListaInversionesCasasSinTerminacion() {
        return "SELECT ide_ipcer,a.ide_ipcai,c.nom_geper AS GRUPO,beneficiario_ipcai AS CASAS_OBRAS,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,fecha_vence_ipcer AS FECHA_VENCIMIENTO,\n"
                + "ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES ,\n"
                + "nombre_ipein AS ESTADO,a.ide_geper \n"
                + "FROM iyp_certificado a \n"
                + "left join iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join iyp_cab_inversion b on a.ide_ipcai=b.ide_ipcai\n"
                + "left join gen_persona c on b.ide_geper = c.ide_geper  \n"
                + "where nuevo=true \n"
                + "and b.ide_iptin=1 and ide_cnccc_terminacion is null \n"
                + "order by c.nom_geper,beneficiario_ipcai,fecha_emision_ipcer";
    }
    
    public String getSqlListaInversionesFondoSinTerminacion() {
        return "SELECT ide_ipcer,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES\n"
                + ",fecha_vence_ipcer AS FECHA_VENCIMIENTO,nombre_ipein AS ESTADO,a.ide_geper\n"
                + "FROM iyp_certificado  a\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where nuevo=true and ide_cnccc_terminacion is null\n"
                + "and ide_iptin=2\n"
                + "order by num_certificado_ipcer";
    }
    
    public String getSqlListaInversionesBancariasSinTerminacion() {
        return "SELECT ide_ipcer,nombre_teban as BANCO,num_certificado_ipcer AS NUM_CERTIFICADO,fecha_emision_ipcer AS FECHA_EMISION,ide_cnccc,capital_ipcer AS CAPITAL,interes_ipcer AS INTERES,valor_a_pagar_ipcer AS CAPITAL_MAS_INTERES\n"
                + ",fecha_vence_ipcer AS FECHA_VENCIMIENTO,nombre_ipein AS ESTADO,a.ide_geper\n"
                + "FROM iyp_certificado  a\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "left join tes_cuenta_banco b on a.ide_tecba=b.ide_tecba\n"
                + "left join tes_banco c on b.ide_teban=c.ide_teban\n"
                + "where nuevo=true and ide_cnccc_terminacion is null\n"
                + "and ide_iptin=0\n"
                + "order by nombre_teban,num_certificado_ipcer,fecha_vence_ipcer";
    }
    
    public String getSqlListaCertificadosVencidosNuevas() {
        String sql = "select ide_ipcer,a.ide_iptin,nombre_iptin,fecha_vence_ipcer,num_certificado_ipcer,nombre_ipcin,observacion_ipcer,fecha_emision_ipcer,plazo_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,cod_captacion_ipcer,nombre_ipein,ide_cnccc_terminacion,ide_cnccc,ide_cnccc_interes\n"
                + "from iyp_certificado a\n"
                + "inner join iyp_tipo_inversion b on a.ide_iptin=b.ide_iptin\n"
                + "left join  iyp_clase_inversion c on a.ide_ipcin=c.ide_ipcin\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + "and fecha_vence_ipcer<='" + utilitario.getFechaActual() + "' \n "
                + "and nuevo=true "
                + "AND a.ide_ipein !=" + utilitario.getVariable("p_iyp_estado_cancelado") + "   "
                + "order by nombre_iptin,fecha_emision_ipcer desc";
        return sql;
    }
    
    public String getSqlListaCertificadosCanceladosNuevas() {
        String sql = "select ide_ipcer,a.ide_iptin,nombre_iptin,fecha_vence_ipcer,num_certificado_ipcer,nombre_ipcin,observacion_ipcer,fecha_emision_ipcer,plazo_ipcer,capital_ipcer,interes_ipcer,valor_a_pagar_ipcer,cod_captacion_ipcer,nombre_ipein,ide_cnccc_terminacion,ide_cnccc,ide_cnccc_interes\n"
                + "from iyp_certificado a\n"
                + "inner join iyp_tipo_inversion b on a.ide_iptin=b.ide_iptin\n"
                + "left join  iyp_clase_inversion c on a.ide_ipcin=c.ide_ipcin\n"
                + "left join  iyp_estado_inversion d on a.ide_ipein=d.ide_ipein\n"
                + "where a.ide_sucu=" + utilitario.getVariable("ide_sucu") + "\n"
                + " and nuevo=true "
                + "AND a.ide_ipein=" + utilitario.getVariable("p_iyp_estado_cancelado") + "   "
                + "order by nombre_iptin,fecha_emision_ipcer desc";
        return sql;
    }

    /**
     * Retorna todas los nombres de los bancos
     *
     * @return
     */
    public String getSqlComboBancos() {
        return "select ide_teban,nombre_teban from tes_banco  "
                + "order by nombre_teban";
    }
    
    public void cancelarInversion(String ide_ipcer) {
        utilitario.getConexion().agregarSql("UPDATE iyp_certificado set cancelado=true where ide_ipcer=" + ide_ipcer);
    }
    
}
