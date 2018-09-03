package paq_produccion.ejb;

import paq_gestion.ejb.*;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;


import sistema.aplicacion.Utilitario;

@Stateless
public class ServicioProduccion {

	public String getValidezTiempo() {
        String sql = "";
        sql = "select ide_prvat ,detalle_prvat from prod_validez_tiempo order by detalle_prvat";
        return sql;
        
        
    }
        
public String getUnidad() {
        String sql = "";
        sql = "select ide_inuni ,nombre_inuni from inv_unidad order by nombre_inuni";
        return sql;
        
          }
public String getTurno() {
        String sql = "";
        sql = "select ide_prtur ,detalle_prtur from prod_turno order by detalle_prtur";
        return sql;
         }

public String getMaquina() {
        String sql = "";
        sql = "select ide_prmaq ,detalle_prmaq from prod_maquina order by detalle_prmaq";
        return sql;
         }

public String getOrdenProduccion() {
        String sql = "";
        sql = "select ide_prorp ,detalle_produccion_prorp from prod_orden_produccion order by detalle_produccion_prorp";
        return sql;
          }

public String getSecuencialModulo(String ide_gemos) {
        String sql = "";
        sql = "select a.ide_gemos,nom_geani,numero_secuencial_gemos,abreviatura_gemos,aplica_abreviatura_gemos,\n" +
"(case when aplica_abreviatura_gemos=true then abreviatura_gemos||'-'||nom_geani||'-'||numero_secuencial_gemos else \n" +
"nom_geani||'-'||numero_secuencial_gemos end) as nuevo_secuencial\n" +
"from gen_modulo_secuencial a, gen_anio b \n" +
"where a.ide_gemos= b.ide_geani\n" +
"and ide_gemos="+ide_gemos;
        return sql;
        
         }

public String getActualizarSecuencial(String ide_gemos) {
        String sql = "";
        sql = "update gen_modulo_secuencial set  numero_secuencial_gemos=numero_secuencial_gemos+1 where ide_gemos="+ide_gemos;
        return sql;
    }


}
