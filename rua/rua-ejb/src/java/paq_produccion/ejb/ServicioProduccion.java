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
"(case when aplica_abreviatura_gemos=false then abreviatura_gemos||'-'||nom_geani||'-'||numero_secuencial_gemos else \n" +
"abreviatura_gemos||' - '||'0'||''||numero_secuencial_gemos end) as nuevo_secuencial\n" +
"from gen_modulo_secuencial a, gen_anio b \n" +
"where a.ide_geani= b.ide_geani\n" +
"and ide_gemos ="+ide_gemos;
        //System.out.printf("IMPRIMIENDO SECUENCIAL OOOOOOOOOOOO11111111000111" +sql);
        return sql;
                 }


public String getActualizarSecuencial(String ide_gemos) {
        String sql = "";
        sql = "update gen_modulo_secuencial set  numero_secuencial_gemos=numero_secuencial_gemos+1 where ide_gemos="+ide_gemos;
        return sql;
    }
public String getColor() {
        String sql = "";
        sql = "select ide_prcol, detalle_prcol from prod_color order by detalle_prcol";
        return sql;
    }
public String getOrdenPro() {
        String sql = "";
        sql = "select ide_prorp, numero_modulo_prorp||' '||numero_prorp from prod_orden_produccion as orden_produccion order by numero_prorp ";
        return sql;
    }
public String getProforma(){
    String sql = "";
    sql = "select ide_prpro, numero_prpro, fecha_prpro, b.nom_geper, total_prpro, observacion_prpro\n" +
          "from prod_proforma a\n" +
          "left join gen_persona b on a.ide_geper = b.ide_geper";
    return sql;
}
public String getNumeroSecuencial(String campo_numero, String tabla){
    String sql = "";
    sql="select ((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1) as maximo,\n" +
        "length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') as longitud,\n" +
        "(case when length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') = 1 then '0000000'||((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)\n" +
        "when length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') = 2 then '000000'||((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)\n" +
        "when length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') = 3 then '00000'||((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)\n" +
        "when length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') = 4 then '0000'||((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)\n" +
        "when length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') = 5 then '000'||((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)\n" +
        "when length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') = 6 then '00'||((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)\n" +
        "when length(((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)||'') = 7 then '0'||((case when max(cast("+campo_numero+" as integer)) is null then  0 else max(cast("+campo_numero+" as integer)) end) + 1)\n" +
        "end) as numero from "+tabla+"";
    return sql;
}
}
