/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_gerencial.ejb;

import framework.aplicacion.TablaGenerica;
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
     * Retorna sentencia SQL para obtener el tipo de balance sea Inicial o
     * mensual
     *
     * @return sql
     */
    public String getTipoBalance(String tipo,String ide_mes) {
        String sql = "select ide_getiba, detalle_getiba from ger_tipo_balance ";
        if(tipo.equals("1")){
            sql+=" where ide_getiba in (select ide_getiba from  ger_tipo_balance_mes where ide_gemes="+ide_mes+" )";
        }
        if(tipo.equals("2")){
            sql+=" where ide_getiba in ("+ide_mes+" )";
        }
        return sql;
    }

    /**
     * Retorna sentencia SQL para obtener el Año
     *
     * @param estado recibe el estado activo, pasivo
     * @param tipo tipo de retirno del sql tipo 1= retorna sql con estado, tipo
     * 2 retorna sql con ide_geani
     * @param ide codigo del ide_geani
     * @return sql
     */
    public String getAnio(String estado, String tipo, String ide) {
        String sql = "select ide_geani,nom_geani,(case when activo_geani=true then 'ACTIVO' else 'INACTIVO' end) as activo_geani from gen_anio ";
        if (tipo.equals("1")) {
            sql += " where activo_geani in (" + estado + ") ";
        }
        if (tipo.equals("2")) {
            sql += " where ide_geani in (" + ide + ") ";
        }
        sql += " order by nom_geani desc";
        return sql;
    }

    //RETORNA LOS MESES DE 
    public String getMes(String tipo, String ide_mes) {
        String sql = "select ide_gemes,nombre_gemes from gen_mes";
        if (tipo.equals("0")) {
            sql += " where ide_gemes in (" + ide_mes + ")";
        }
        sql += " order by ide_gemes";

        return sql;
    }
        //RETORNA LAS OBRAS POR SUCURSAL
    public String getCasaObraScursal(String sucursal,String ide_geani,String tipo) {
        String sql = "select ide_gecobc,nombre_gercas,nombre_gerobr,codigo_gerobr,a.ide_gerest from ger_cont_balance_cabecera a,(select a.ide_gercas,nombre_gercas,ide_gerobr,nombre_gerobr,codigo_gerobr " +
                    " from ger_casa a, ger_obra b where a.ide_gercas=b.ide_gercas ) b where a.ide_gerobr=b.ide_gerobr "; 
                    if(tipo.equals("1")){
                        sql+=" and codigo_gerobr in ( select codigo_sucu from sis_sucursal where ide_sucu="+sucursal+") ";
                    }
                    sql+=" and a.ide_geani="+ide_geani;

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
    public String getCasaObraPeriodoFiscal(String estado, String anio, String tipo, String codigo) {
        String sql = "select ide_gecobc,nombre_gercas,nombre_gerobr from ger_cont_balance_cabecera a,(select a.ide_gercas,nombre_gercas,ide_gerobr,nombre_gerobr "
                + " from ger_casa a, ger_obra b where a.ide_gercas=b.ide_gercas ) b where a.ide_gerobr=b.ide_gerobr ";
        if (tipo.equals("1")) {
            sql += " and a.ide_geani=" + anio + " and a.ide_gerest in (" + estado+")";
        }
        if (tipo.equals("2")) {
            sql += " and ide_gecobc in (" + codigo + ")";
        }
        sql += " order by nombre_gercas,nombre_gerobr";
        return sql;
    }

    /**
     * Retorna sentencia SQL para obtener el nombre de Casa y la Obra
     *
     * @param tipo Si el tipo 1 requiere parametros ide obra, tipo 2 retorna lod elas casas
     * obras en periodo no vigente
     * @param ide_obra Ide de las obras salesianas
     * @return sql
     */
    public String getCasaObra(String tipo, String ide_obra,String ide_geani) {
        String sql = "";
        if(tipo.equals("2")){
            sql+="select a.ide_gerobr,ide_gercas,nombre_gercas,nombre_gerobr from (";
        }
        sql += "SELECT a.ide_gerobr,a.ide_gercas,nombre_gercas,nombre_gerobr "
                + "from ger_obra a,ger_casa b where a.ide_gercas= b.ide_gercas ";
        if (tipo.equals("1")) {
            sql += " and a.ide_gerobr in (" + ide_obra + ")";
        }
        if(tipo.equals("2")){
            sql+=" ) a  left join ger_cont_balance_cabecera b on a.ide_gerobr = b.ide_gerobr and ide_geani="+ide_geani+"  where  b.ide_gerobr is null ";
        }
        sql += " order by nombre_gercas,nombre_gerobr";

        return sql;
    }
    /**
     * Retorna sentencia SQL para obtener el las casas y obras que se encuentran en la cabecera balnace general
     *
     * @param tipo      tipo 1= lista todas las obras, tipo 2= filtra por obras seleccionadas
     * @param str_anio  recibe el paramtero ide_geani
     * @param str_mes   recibe el parametro ide_gemes
     * @param str_tipo_balance  recibe el parametro tupo balance
     * @param ide_obra parametro de obras seleccionadas
     * @return sql
     */
    public String getCasaObraMensual(String tipo,String str_anio,String str_mes,String str_tipo_balance,String ide_obra) {
        String sql = "select a.ide_gecobc,nombre_gercas,nombre_gerobr from ( " +
                    " SELECT ide_gecobc,a.ide_gerobr,a.ide_gercas,nombre_gercas,nombre_gerobr " +
                    " from ger_obra a,ger_casa b,ger_cont_balance_cabecera c " +
                    " where a.ide_gercas= b.ide_gercas and a.ide_gerobr=c.ide_gerobr and c.ide_geani=" +str_anio+
                    " ) a left join ger_balance_mensual b on a.ide_gecobc=b.ide_gecobc and ide_gemes="+str_mes+" and ide_getiba="+str_tipo_balance +
                    " where 1= 1 ";
                if(tipo.equals("1")){
                sql+= " and b.ide_gecobc is  null";
                }
                if(tipo.equals("2")){
                sql+= " and  a.ide_gecobc in ("+ide_obra+")";
                }
                sql+=" order by nombre_gercas ";
        return sql;
    }
    public String getDatoEmpleado(String ide_usua) {
        String sql = "Select a.ide_usua,b.ide_gtemp,a.nom_usua,a.nick_usua, b.documento_identidad_gtemp from Sis_usuario a\n"
                + "left join gth_empleado b on a.ide_gtemp = b.ide_gtemp\n"
                + "where ide_usua in(" + ide_usua + ")  ";
        return sql;

    }
    /**
     * Retorna sentencia SQL para obtener el plan de cuentas
     *
     * @return sql
     */
    public String getPlanCuentas() {
        String sql = "select ide_cndpc,codig_recur_cndpc,nombre_cndpc from con_det_plan_cuen order by codig_recur_cndpc";
        return sql;
    }
        /**
     * Retorna sentencia SQL para obtener el plan de cuentas presupuestarias
     *
     * @return sql
     */
    public String getPlanPresupuestario() {
        String sql = "select ide_prcla,codigo_clasificador_prcla,descripcion_clasificador_prcla from pre_clasificador order by codigo_clasificador_prcla";
        return sql;
    }
        /**
     * Retorna sentencia SQL tranferir las cuentas contables al modulo gerencial
     *
     * @return sql
     */
    public String getTranseferirAsientos(String estado,String sucursal,String fecha_inicial,String fecha_final) {
        String sql = "select dpc.ide_cndpc,dpc.nombre_cndpc,dpc.ide_cnncu,sum(dcc.debe) as debe,sum(dcc.haber) as haber,dpc.codig_recur_cndpc,con_ide_cndpc,dpc.ide_cntcu \n" +
                        " from con_cab_comp_cont ccc inner join  (select ide_cnccc,(case when ide_cnlap=1 then valor_cndcc else 0 end) as debe,(case when ide_cnlap=0 then valor_cndcc else 0 end) as haber \n" +
                        " ,ide_cndpc,ide_cnlap from con_det_comp_cont ) dcc on ccc.ide_cnccc=dcc.ide_cnccc inner join con_det_plan_cuen dpc on  dpc.ide_cndpc = dcc.ide_cndpc \n" +
                        " inner join con_tipo_cuenta tc on dpc.ide_cntcu=tc.ide_cntcu WHERE (ccc.fecha_trans_cnccc BETWEEN '"+fecha_inicial+"' and '"+fecha_final+"') \n" +
                        " and ccc.ide_cneco IN ("+estado+") and ccc.ide_sucu="+sucursal+" group by  dpc.ide_cndpc,dpc.nombre_cndpc,dpc.ide_cnncu order by dpc.ide_cndpc";
       //System.out.println("transferrir asientos "+sql);
        return sql;
    }
        /**
     * Retorna sentencia SQL insert tabla temporal balances contables
     *
     * @return sql
     */
    public String insertTempBalance(String cuenta,String debe,String haber,String usuario,String nivel) {
        String sql = "insert into ger_temp_balance (ide_cndpc,valor_debe_geteba,valor_haber_geteba,ide_usua,nivel_cndpc) values ("
                + cuenta+","+debe+","+haber+","+usuario+","+nivel+" );";
        //System.out.println(" insert "+sql);
        return sql;
    }
     /**
     * Retorna sentencia SQL delete tabla temporal balances contables
     *
     * @return sql
     */
    public String deleteTempBalance(String usuario) {
        String sql = "delete from ger_temp_balance where ide_usua="+usuario;
                        return sql;
    }
     /**
     * Retorna sentencia SQL delete tabla detalle de balances que no has sido cerradas
     *
     * @return sql
     */
    public String deleteBalanceDetalle(String ide_balance_mensual) {
        String sql = "delete from ger_balance_detalle where ide_gebame ="+ide_balance_mensual;
                        return sql;
    }  
     /**
     * Retorna sentencia SQL update tabla detalle de balances que han sido cerradas
     *
     * @return sql
     */
    public String UpdateEstadoBalanceCerrado(String ide_balance_mensual,String ide_estado) {
        String sql = "update ger_balance_mensual set ide_gerest="+ide_estado+"  where ide_gebame in ("+ide_balance_mensual+");";
                        return sql;
    }    
             /**
     * Retorna sentencia SQL calcula tabla temporal balances contables
     *
     * @return sql
     */
    public String getCalTemBalance(String usuario,String nivel) {
        String sql = "select con_ide_cndpc,sum(valor_debe_geteba) as debe,sum(valor_haber_geteba) as haber " +
                        "from ger_temp_balance a, con_det_plan_cuen b where a.ide_cndpc=b.ide_cndpc and ide_cnncu in ("+nivel+") and ide_usua="+usuario+" group by con_ide_cndpc";
        return sql;
    }
     public String getCalTemBalanceNivel5y4(String usuario,String nivel) {
        String sql = "select a.ide_cndpc,sum(valor_debe_geteba) as debe,sum(valor_haber_geteba) as haber " +
                    " from ger_temp_balance a, con_det_plan_cuen b where a.ide_cndpc=b.ide_cndpc " +
                    " and ide_cnncu in (5) " +
                    " and codig_recur_cndpc like '4%'" +
                    " and ide_usua="+usuario+" group by a.ide_cndpc" +
                    " union" +
                    " select a.ide_cndpc,sum(valor_debe_geteba) as debe,sum(valor_haber_geteba) as haber" +
                    " from ger_temp_balance a, con_det_plan_cuen b where a.ide_cndpc=b.ide_cndpc " +
                    " and ide_cnncu in (5) " +
                    " and codig_recur_cndpc like '5%'" +
                    " and ide_usua="+usuario+" group by a.ide_cndpc";
        return sql;
    }   
    /**
     * Sirve cuando para la generacción del balance general
     *
     * @param ide_cndpc rubro de amisión
     * @return ide_gebame en caso de guardar correctamente
     */
    public String getSavePatTituDetalle(String ide_cndpc, String ide_gebame, String valor_debe_gebade, String valor_haber_gebade) {
        String ide_patide = "-1";
        TablaGenerica tab_tabla1 = new TablaGenerica();
        tab_tabla1.setTabla("ger_balance_detalle", "ide_gebade", -1);
        tab_tabla1.setCondicion("ide_gebade=-1");
        tab_tabla1.ejecutarSql();
        tab_tabla1.insertar();
        tab_tabla1.setValor("ide_cndpc", ide_cndpc);
        tab_tabla1.setValor("ide_gebame", ide_gebame);
        tab_tabla1.setValor("valor_debe_gebade", valor_debe_gebade);
        tab_tabla1.setValor("valor_haber_gebade", valor_haber_gebade);
        if (tab_tabla1.guardar()) {
            ide_patide = tab_tabla1.getValor("ide_gebade");
        }
        return ide_patide;
    }
             /**
     * Retorna sentencia SQL resumen meses
     *
     * @return sql
     */
    public String getResumenMes(String mes,String valor,String condicion) {
        String sql = "select 1 as ide, "+valor+" as valor from (" ;
                sql+=getMes("0", mes);
                sql+=" ) a "+condicion;
        return sql;
    }   
     /**
     * Retorna sentencia SQL resumen de obras horizontal
     *
     * @return sql
     */
    public String getObrasHorizontal(String ide_obra) {
        String sql = "SELECT 1 as codigo,' '|| list(nombre_gerobr) as obra from ( SELECT cast(codigo_gerobr as integer) as codigo_orden,nombre_gerobr FROM ger_obra where ide_gerobr in (select ide_gerobr from ger_cont_balance_cabecera where ide_gecobc in ("+ide_obra+") ) order by cast(codigo_gerobr as integer) ) a" ;
        return sql;
    }
     /**
     * Retorna sentencia SQL valores para el caculo de valores activo, pasivo patrimonio, utilidad
     *
     * @return sql
     */
    public String getResultadoBalanceGeneral(String ide_geani,String ide_obra,String activo,String pasivo,String patrimonio) {
        String sql = "SELECT a.ide_gemes,nombre_gemes,(case when b.debe is null then 0 else b.debe end) as activo,(case when c.haber is null then 0 else c.haber end) as pasivo,(case when d.haber is null then 0 else d.haber end) as patrimonio from gen_mes a left join ( " +
                 getBalanceGeneral(ide_geani, ide_obra, "1,2","1,2,3,4,5,6,7,8,9,10,11,12", "1", "1", "1",activo ) +" ) b on a.ide_gemes=b.ide_gemes "+
                 " left join ("+
                 getBalanceGeneral(ide_geani, ide_obra, "1,2","1,2,3,4,5,6,7,8,9,10,11,12", "1", "1", "1",pasivo ) +" ) c on a.ide_gemes=c.ide_gemes "+
                  " left join ("+
                 getBalanceGeneral(ide_geani, ide_obra, "1,2","1,2,3,4,5,6,7,8,9,10,11,12", "1", "1", "1",patrimonio ) +" ) d on a.ide_gemes=d.ide_gemes "+
                 " order by ide_gemes ";
        return sql;
    }
     /**
     * Retorna sentencia SQL balance general
     *
     * @return sql
     */
    public String getBalanceGeneral(String ide_geani,String ide_obra,String tipo_balance,String mes,String nivel_inicial,String nivel_final,String tipo,String cuenta) {
        String sql = "select a.ide_cndpc,codig_recur_cndpc,nombre_cndpc,(case when apli_saldo_cndpc=1 then debe-haber else 0 end) as debe, " +
                    "(case when apli_saldo_cndpc=2 then haber-debe else 0 end) as haber " ;
                    if(tipo.equals("1")){
                         sql+=" ,ide_gemes ";
                     }
                    sql+=" from con_det_plan_cuen a ,( " +
                    "select ide_cndpc,sum(valor_debe_gebade) as debe,sum(valor_haber_gebade) as haber " ;
                    if(tipo.equals("1")){
                         sql+=" ,ide_gemes ";
                     }
                    sql+=" from ( " +
                    " select a.ide_gecobc,nombre_gercas,nombre_gerobr,codigo_gerobr,ide_geani,ide_cndpc,valor_debe_gebade,valor_haber_gebade " ;
                    if(tipo.equals("1")){
                         sql+=" ,c.ide_gemes ";
                     }
                    sql +=" from ger_cont_balance_cabecera a,(select a.ide_gercas,nombre_gercas,ide_gerobr,nombre_gerobr,codigo_gerobr " +
                    " from ger_casa a, ger_obra b where a.ide_gercas=b.ide_gercas ) b,ger_balance_mensual c ,ger_balance_detalle d " +
                    "where a.ide_gerobr=b.ide_gerobr " +
                    "and a.ide_gecobc= c.ide_gecobc " +
                    "and c.ide_gebame=d.ide_gebame " +
                    "and ide_geani in ("+ide_geani+")" +
                    "and a.ide_gecobc in ("+ide_obra+") " +
                    "and c.ide_getiba in ("+tipo_balance+") " +
                    " and c.ide_gemes in ( "+mes+") ";
                     if(tipo.equals("1")){
                         sql+=" and ide_cndpc in ("+cuenta+") ";
                     }
                    sql +=" ) a group by ide_cndpc " ;
                    if(tipo.equals("1")){
                         sql+=" ,ide_gemes ";
                     }        
                    sql+=" ) b where a.ide_cndpc= b.ide_cndpc " +
                    " and ((case when apli_saldo_cndpc=1 then debe-haber else 0 end) + (case when apli_saldo_cndpc=2 then haber-debe else 0 end)) !=0 " +
                    " and ide_cnncu between "+nivel_inicial+" and " +nivel_final+
                    " order by codig_recur_cndpc " ;               
        return sql;
    }   
     /**
     * Retorna sentencia SQL totales balance de comprobacion
     *
     * @return sql
     */
    public String getTotalBalanceComprobacion(String ide_geani,String bal_inicial,String bal_mensual,String ide_obras,String ide_mes,String tipo_balance,String mes_periodo) {
        String sql = "select  sum (debe_inicial) as tot_debe_inicial,sum(haber_inicial) as tot_haber_incial,sum(debe_periodo) as tot_debe_per,sum(haber_periodo) as tot_haber_per,\n" +
"sum(debe_acum) as tot_debe_acum,sum(haber_acum) as tot_haber_acum,sum(debe_saldo) as tot_debe_sal,sum(haber_saldo) as tot_haber_sal from (\n" +
"select a.ide_cndpc,codig_recur_cndpc,nombre_cndpc,(case when debe_inicial is null then 0 else debe_inicial end) as debe_inicial,\n" +
"(case when haber_inicial is null then 0 else haber_inicial end) as haber_inicial,\n" +
"(case when debe_periodo is null then 0 else debe_periodo end) as debe_periodo,\n" +
"(case when haber_periodo is null then 0 else haber_periodo end) as haber_periodo,\n" +
"(case when debe_acum is null then 0 else debe_acum end) as debe_acum,\n" +
"(case when haber_acum is null then 0 else haber_acum end) as haber_acum,\n" +
"(case when apli_saldo_cndpc=1 then (case when debe_acum is null then 0 else debe_acum end)-(case when haber_acum is null then 0 else haber_acum end) else 0 end) as debe_saldo,\n" +
"(case when apli_saldo_cndpc=2 then (case when haber_acum is null then 0 else haber_acum end)-(case when debe_acum is null then 0 else debe_acum end) else 0 end) as haber_saldo " +
"from con_det_plan_cuen a \n" +
"left join (\n" +
"select ide_cndpc,sum(valor_debe_gebade) as debe_inicial,sum(valor_haber_gebade) as haber_inicial \n" +
"from (\n" +
"select a.ide_gecobc,nombre_gercas,nombre_gerobr,codigo_gerobr,ide_geani,ide_cndpc,valor_debe_gebade,valor_haber_gebade \n" +
"from ger_cont_balance_cabecera a,(select a.ide_gercas,nombre_gercas,ide_gerobr,nombre_gerobr,codigo_gerobr \n" +
"from ger_casa a, ger_obra b where a.ide_gercas=b.ide_gercas ) b,ger_balance_mensual c ,ger_balance_detalle d \n" +
"where a.ide_gerobr=b.ide_gerobr \n" +
"and a.ide_gecobc= c.ide_gecobc\n" +
"and c.ide_gebame=d.ide_gebame\n" +
"and ide_geani in ("+ide_geani+")\n" +
"and a.ide_gecobc in ("+ide_obras+")\n" +
"and c.ide_getiba in ("+bal_inicial+")\n" +
") a group by ide_cndpc\n" +
") b on a.ide_cndpc = b.ide_cndpc\n" +
"left join (\n" +
"select ide_cndpc,sum(valor_debe_gebade) as debe_periodo,sum(valor_haber_gebade) as haber_periodo \n" +
"from (\n" +
"select a.ide_gecobc,nombre_gercas,nombre_gerobr,codigo_gerobr,ide_geani,ide_cndpc,valor_debe_gebade,valor_haber_gebade \n" +
"from ger_cont_balance_cabecera a,(select a.ide_gercas,nombre_gercas,ide_gerobr,nombre_gerobr,codigo_gerobr \n" +
"from ger_casa a, ger_obra b where a.ide_gercas=b.ide_gercas ) b,ger_balance_mensual c ,ger_balance_detalle d \n" +
"where a.ide_gerobr=b.ide_gerobr \n" +
"and a.ide_gecobc= c.ide_gecobc\n" +
"and c.ide_gebame=d.ide_gebame\n" +
"and ide_geani in ("+ide_geani+")\n" +
"and a.ide_gecobc in ("+ide_obras+")\n" +
"and c.ide_getiba in ("+bal_mensual+")\n" +
"and c.ide_gemes in ("+mes_periodo+")\n" +
") a group by ide_cndpc\n" +
") c on a.ide_cndpc = c.ide_cndpc\n" +
"left join (\n" +
"select ide_cndpc,sum(valor_debe_gebade) as debe_acum,sum(valor_haber_gebade) as haber_acum \n" +
"from (\n" +
"select a.ide_gecobc,nombre_gercas,nombre_gerobr,codigo_gerobr,ide_geani,ide_cndpc,valor_debe_gebade,valor_haber_gebade ,c.ide_gemes\n" +
"from ger_cont_balance_cabecera a,(select a.ide_gercas,nombre_gercas,ide_gerobr,nombre_gerobr,codigo_gerobr \n" +
"from ger_casa a, ger_obra b where a.ide_gercas=b.ide_gercas ) b,ger_balance_mensual c ,ger_balance_detalle d \n" +
"where a.ide_gerobr=b.ide_gerobr \n" +
"and a.ide_gecobc= c.ide_gecobc\n" +
"and c.ide_gebame=d.ide_gebame\n" +
"and ide_geani in ("+ide_geani+")\n" +
"and a.ide_gecobc in ("+ide_obras+")\n" +
"and c.ide_getiba in ("+tipo_balance+")\n" +
"and c.ide_gemes in ("+ide_mes+")\n" +
") a group by ide_cndpc\n" +
") d on a.ide_cndpc = d.ide_cndpc\n" +
"where ide_cnncu between 1 and 1\n" +
"order by codig_recur_cndpc\n" +
") a" ;
        //System.out.println(sql);
        return sql;
    }    
}
