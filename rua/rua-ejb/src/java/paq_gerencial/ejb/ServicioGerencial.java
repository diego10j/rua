/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial.ejb;

import javax.ejb.Stateless;

/**
 *
 * @author Lusi Toapanta
 */
@Stateless

public class ServicioGerencial {

    /**
     * Retorna sentencia SQL para obtener los estados en el que se encuentra, en
     * el balance general.
     *
     * @return sql
     */
    public String getEstado() {
        String sql = "select ide_gerest, detalle_gerest from ger_estado  ";
        return sql;
    }

    /**
     * Retorna sentencia SQL para obtener el nombre y la obra que encuentra, en
     * desarrollo.
     *
     * @return sql
     */
    public String getObra() {
        String sql = "select ide_gerobr, nombre_gerobr, codigo_gerobr, abreviatura_gerobr from ger_obra  ";
        return sql;
    }
    /**
     * Retorna sentencia SQL para obtener el tipo de balance sea Inicial o mensual
     * 
     * @return sql
     */
    public String getTipoBalance() {
        String sql = "select ide_getiba, detalle_getiba from ger_tipo_balance ";
        return sql;
    }
    /**
     * Retorna sentencia SQL para obtener el Año
     * 
     * @param estado recibe el estado activo, pasivo
     * @return sql
     */
    public String getAnio(String estado) {
        String sql = "select ide_geani,nom_geani,(case when activo_geani=true then 'ACTIVO' else 'INACTIVO' end) as activo_geani from gen_anio where activo_geani in ("+estado+") order by nom_geani";
        return sql;
    }
    /**
     * Retorna sentencia SQL para obtener el Año
     * 
     * @param estado recibe el estado apertura cierre
     * @param anio recibe el anio fiscal
     * @param tipo recibe el parametro del tipo 1 si craga toda la lista del sql
     * @param codigo los ides de las casas y obras por año
     * @return sql
     */
    public String getCasaObraPeriodoFiscal(String estado,String anio,String tipo,String codigo) {
        String sql = "select ide_gecobc,nombre_gercas,nombre_gerobr from ger_cont_balance_cabecera a,(select a.ide_gercas,nombre_gercas,ide_gerobr,nombre_gerobr " +
                " from ger_casa a, ger_obra b where a.ide_gercas=b.ide_gercas ) b where a.ide_gerobr=b.ide_gerobr ";
                if(tipo.equals("1")){
                 sql+=" and a.ide_geani="+anio+" and a.ide_gerest="+estado;
                }
                if(tipo.equals("2")){
                  sql+=" and ide_gecobc in ("+codigo+")";  
                }
                sql +=" order by nombre_gercas,nombre_gerobr";
        return sql;
    }      
    /**
     * Retorna sentencia SQL para obtener el nombre de Casa y la Obra
     * 
     * @param tipo  Si el tipo 1 requiere parametros ide obra
     * @param ide_obra  Ide de las obras salesianas
     * @return sql
     */
    public String getCasaObra(String tipo,String ide_obra) {
        String sql = "SELECT a.ide_gerobr,a.ide_gercas,nombre_gercas,nombre_gerobr " +
        "from ger_obra a,ger_casa b where a.ide_gercas= b.ide_gercas ";
        if(tipo.equals("1")){                 
            sql +=" and a.ide_gerobr in ("+ide_obra+")";
        }
               sql +=" order by nombre_gercas,nombre_gerobr";            
               
        return sql;
    }

    public String getDatoEmpleado(String ide_usua) {
        String sql = "Select a.ide_usua,b.ide_gtemp,a.nom_usua,a.nick_usua, b.documento_identidad_gtemp from Sis_usuario a\n"
                + "left join gth_empleado b on a.ide_gtemp = b.ide_gtemp\n"
                + "where ide_usua in(" + ide_usua + ")  ";
        return sql;

    }
}   
