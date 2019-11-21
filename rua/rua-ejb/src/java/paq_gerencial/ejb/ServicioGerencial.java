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
     * Retorna sentencia SQL para obtener los nombres de las casas, y obras
     * salesianas para proceder a autorizar la conexión.
     *
     * @return sql
     */
    public String getSqlGerPermiso() {
        String sql = "select ide_geperm,nom_casa_geperm,nom_persistencia_geperm from ger_permiso "
                + "order by nom_casa_geperm";
        return sql;
    }

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

    public String getTipoBalance() {
        String sql = "select ide_getiba, detalle_getiba from ger_tipo_balance ";
        return sql;
    }
}   
