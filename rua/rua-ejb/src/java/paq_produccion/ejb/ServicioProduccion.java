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
        sql = "select ide_prorp, numero_prorp||'  '||numero_modulo_prorp as orden_produccion from prod_orden_produccion";
        return sql;
          }

public String getSecuencialModulo(String modulo) {
        String sql = "";
        sql = "select ide_gemos, ide_gemod, abreviatura_gemos||' - '||'0'||''||numero_secuencial_gemos as nuevo_secuencial\n" +
              "from gen_modulo_secuencial \n" +
              "where ide_gemod = "+modulo+"\n" +
              "and activo_gemos = true ";
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
public String getSqlOrdenesProduccion(String numero_orden, String tipo){
    String sql="";
    sql = "select c.ide_prord, a.ide_prorp, c.ide_inarti, numero_modulo_prorp, numero_prorp, nom_geper, fecha_emision_prorp, nombre_inarti, total_prord as cantidad_produccion, total_producion_prorp\n" +
          "from  prod_orden_produccion a\n" +
          "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
          "left join prod_orden_detalle c on a.ide_prorp = c.ide_prorp\n" +
          "left join inv_articulo d on c.ide_inarti = d.ide_inarti";
      if (tipo.equals("2")){
          sql += " where c.ide_prord = "+numero_orden+""; 
      }
      sql +=" order by numero_prorp";
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
  public String getControlProdRecepcionDetalle (String tipo, String ide_control, String ide_detalle_control){
      String sql = "";
      sql = "select ide_prdecp, fecha_control_prdecp, detalle_prtur as turno, apellido_paterno_gtemp||' '||primer_nombre_gtemp as operario, producto_bueno_prdecp as total\n" +
            "from prod_detalle_control_pro a\n" +
            "left join gth_empleado b on a.ide_gtemp = b.ide_gtemp\n" +
            "left join prod_turno c on a.ide_prtur = c.ide_prtur";
       if (tipo.equals("2")){
           sql +=" where a.ide_prcop = "+ide_control+"";
       }
       else if (tipo.equals("3")){
           sql +=" and ide_prdecp = "+ide_detalle_control+"";
       }
       sql +=" order by fecha_control_prdecp";
      return sql;
  }
}
