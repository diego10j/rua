package paq_produccion.ejb;

import paq_gestion.ejb.*;
import javax.ejb.Stateless;

import framework.aplicacion.TablaGenerica;


import sistema.aplicacion.*;

@Stateless
public class ServicioProduccion {
private final Utilitario utilitario = new Utilitario();
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
        sql = "select ide_prorp, numero_prorp from prod_orden_produccion";
        return sql;
          }

public String getSecuencialModulo(String modulo) {
        String sql = "";
        sql = "select a.ide_gemos, ide_gemod, nom_geani,numero_secuencial_gemos,abreviatura_gemos,aplica_abreviatura_gemos, longitud_secuencial_gemos,\n" +
        "(case when a.ide_geani is null then \n" +
        "(case when aplica_abreviatura_gemos = true then abreviatura_gemos||'-'||numero_secuencial_gemos else \n" +
        "numero_secuencial_gemos||'' end) else\n" +
        "(case when aplica_abreviatura_gemos = true then abreviatura_gemos||'-'||nom_geani||'-'||numero_secuencial_gemos else \n" +
        "numero_secuencial_gemos||'' end)   end ) as nuevo_secuencial\n" +
        ", length(numero_secuencial_gemos||'') as tamano\n" +
        "from gen_modulo_secuencial a\n" +
        "left join gen_anio b on a.ide_geani= b.ide_geani\n" +
        "where ide_gemos = "+modulo+"";
        //System.out.printf("IMPRIMIENDO SECUENCIAL OOOOOOOOOOOO11111111000111" +sql);
        return sql;
     }
public String getSecuencialNumero(int longitud, int tamaño) {
        String acum ="";
        int tamaño_final = longitud - tamaño;
        for (int i=0; i < tamaño_final ; i++){
             acum += "0";
        }
        
        String taman = acum + tamaño;
        return acum;
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
        sql = "select ide_prorp, numero_prorp, nom_geper\n" +
              "from prod_orden_produccion a\n" +
              "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
              "order by numero_prorp";
        return sql;
    }
public String getProforma(){
    String sql = "";
    sql = "select ide_prpro, numero_prpro, fecha_prpro, b.nom_geper, total_prpro, observacion_prpro\n" +
          "from prod_proforma a\n" +
          "left join gen_persona b on a.ide_geper = b.ide_geper";
    return sql;
}
public String getComboProforma(){
    String sql = "";
    sql = "select ide_prpro, numero_prpro, nom_geper \n" +
          "from prod_proforma a\n" +
          "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
          "order by ide_prpro";
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
public String getSqlDetalleOrdenProd(String tipo, String numero_orden, String numero_detalle){
    String sql="";
    sql = "select ide_prord, nombre_inuni, nombre_inarti, detalle_prcol, bulto_paquete_prord, unidades_prord, total_prord, total_entregado_prord as por_entregar\n" +
          "from prod_orden_detalle a\n" +
          "left join inv_unidad b on a.ide_inuni = b.ide_inuni\n" +
          "left join inv_articulo c on a.ide_inarti = c.ide_inarti\n" +
          "left join prod_color d on a.ide_prcol = d.ide_prcol";
      if (tipo.equals("2")){
          sql += " where ide_prorp = "+numero_orden+""; 
      }
      if (tipo.equals("3")){
          sql += " where ide_prorp = "+numero_orden+" and ide_prord in ("+numero_detalle+")"; 
      }
    return sql;
    
}
public String getControlProduccion (String tipo, String control){
    String sql = "";
    sql = "select ide_prcop, b.ide_prorp, nom_geper, numero_modulo_prorp, numero_prorp, nombre_inarti, producto_bueno_prcop, producto_mala_calidad_prcop, total_horas_prcop, fecha_termina_prcop\n" +
          "from prod_control_produccion a\n" +
          "left join prod_orden_produccion b on a.ide_prorp = b.ide_prorp\n" +
          "left join gen_persona c on b.ide_geper = c.ide_geper\n" +
          "left join inv_articulo d on a.ide_inarti = d.ide_inarti";
     if(tipo.equals("2")){
         sql += " where ide_prcop = "+control+"";
     }
     sql += " order by numero_prorp"; 
    return sql;
    
    }
  public String getControlProdRecepcion (){
      String sql= "";
      sql = "select a.ide_prcop,  b.ide_prcop as control_prod, a.numero_prcop, nombre_inarti, numero_modulo_prorp||'  '||numero_prorp as orden, detalle_prmaq\n" +
            "from prod_control_produccion a\n" +
            "left join prod_control_produccion b on a.ide_prcop = b.ide_prcop\n" +
            "left join inv_articulo c on a.ide_inarti = c.ide_inarti\n" +
            "left join prod_orden_produccion d on a.ide_prorp = d.ide_prorp\n" +
            "left join prod_maquina e on a.ide_prmaq = e.ide_prmaq\n" +
            "order by orden";
      return sql;
  }
  public String getTipoOrden() {
        String sql = "";
        sql = "select ide_prtio, detalle_prtio from prod_tipo_orden order by detalle_prtio";
        return sql;
  }

}
