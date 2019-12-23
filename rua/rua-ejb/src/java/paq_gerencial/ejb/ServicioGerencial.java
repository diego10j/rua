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
        String sql = "select ide_geani,nom_geani,activo_geani from gen_anio where activo_geani in ("+estado+") order by nom_geani";
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
        "from ger_obra a,ger_casa b where a.ide_gercas= b.a.ide_gercas ";
        if(tipo.equals("1")){
            sql +=" and a.ide_gerobr in ("+ide_obra+")";
        }
               sql +="order by nombre_gercas,nombre_gerobr";
               
        return sql;
    }    
}   
