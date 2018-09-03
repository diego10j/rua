/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_adquisicion.ejb;

import framework.aplicacion.TablaGenerica;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import persistencia.Conexion;
import sistema.aplicacion.Utilitario;

/**
 * /**
 *
 * @author Andres
 */
@Stateless
public class ServiciosAdquisiones {

    /**
     * Retorna los tipos de aprobaciones que existen
     *
     * @return sql de aprobado
     */
    private Conexion conSql;
    private Conexion conOracle;
    private Utilitario utilitario = new Utilitario();

    public String getAprobado() {
        String sql = "";
        sql = "SELECT IDE_ADAPRO, DETALLE_ADAPRO FROM ADQ_APROBADO order by DETALLE_ADAPRO";
        return sql;
    }

    public String getTipoArea() {
        String sql = "";
        sql = "select IDE_ADTIAR, DETALLE_ADTIAR from ADQ_TIPO_AREA";
        return sql;
    }
    
    public String getTipoAreaDatos(String IDE_ADTIAR) {
        String sql = "";
        sql = "select IDE_ADTIAR, DETALLE_ADTIAR from ADQ_TIPO_AREA  where IDE_ADTIAR in ("+IDE_ADTIAR+") order by DETALLE_ADTIAR";
        return sql;
    }

    public String getGrupoMaterial() {
        String sql = "";
        sql = "SELECT IDE_ADGRMA, CODIGO_ADGRMA, DETALLE_ADGRMA FROM ADQ_GRUPO_MATERIAL order by DETALLE_ADGRMA";
        return sql;
    }

    public String getPartidaPresupuestaria() {
        String sql = "";
        sql = "SELECT IDE_ADPAPR, CODIGO_ADPAPR, DETALLE_ADPAPR FROM ADQ_PARTIDA_PRESUPUESTARIA order by CODIGO_ADPAPR";
        return sql;
    }

    public String getAreaPartida() {
        String sql = "";
        sql = "select ide_adarpa,codigo_adarpa,detalle_adpapr from ADQ_AREA_PARTIDA a,ADQ_PARTIDA_PRESUPUESTARIA b where a.IDE_ADPAPR = b.IDE_ADPAPR order by codigo_adpapr";
        return sql;
    }

    public String getPartidaMaterial() {
        String sql = "";
        sql = "select ide_adpama,CODIGO_ADPAMA,DETALLE_ADMATE\n"
                + "from ADQ_PARTIDA_MATERIAL a, ADQ_MATERIAL b\n"
                + "where a.IDE_ADMATE=b.IDE_ADMATE\n"
                + "order by CODIGO_ADPAMA";
        return sql;
    }

    public String getMaterial(String tipo, String tipo_compra) {

        String sql = "select ide_inarti, codigo_inarti, nombre_inarti from  inv_articulo order by codigo_inarti ";
        return sql;
    }

    public String getAreaAdministrativa(String tipo, String empleado) {
        String sql = "SELECT IDE_ADARAD, CODIGO_ADARAD, DETALLE_ADARAD FROM ADQ_AREA_ADMINISTRATIVA ";
        if (tipo.equals("1")) {
            sql += " where IDE_ADARAD in (select IDE_ADARAD from ADQ_EMPLEADO_DEPARTAMENTO where IDE_ADEMDE =" + empleado + ") ";
        }
        if (tipo.equals("2")) {
            sql += "  where ide_adarad in  ( select IDE_ADARAD from ADQ_EMPLEADO_DEPARTAMENTO where IDE_ADEMDE in (select IDE_ADEMDE from ADQ_EMPLEADO_APRUEBA where IDE_ADEMAP=" + empleado + ")) ";
        }
        sql += " order by DETALLE_ADARAD ";
        return sql;
    }

    public String getEmpleado() {
        String sql = "";
        sql = "SELECT IDE_ADEMPLE, CEDULA_ADEMPLE, NOMBRES_ADEMPLE FROM ADQ_EMPLEADO ORDER BY NOMBRES_ADEMPLE";
        return sql;
    }
    
    public String getDatosEmpleadoCodigo(String ide_usua) {
        String sql = "";
        sql = "select ide_ademple, cedula_ademple, nombres_ademple from adq_empleado where ide_usua in (select ide_usua from sis_usuario where ide_usua = "+ide_usua+")";
        return sql;
    }
    public String getDatosFactura() {
        String sql = "";
        sql = "select ide_ccdaf, serie_ccdaf, autorizacion_ccdaf, observacion_ccdaf from cxc_datos_fac";
        return sql;
    }
    
    public String getTipoCaja() {
        String sql = "";
        sql = "select ide_cocaj, detalle_cocaj from cont_caja order by detalle_cocaj";
        return sql;
    }
    
    public String getCajas(String ide_emple, String activo) {
        String sql = "";
        sql = " select ide_cxcaem, detalle_cocaj from cxc_caja_empleado a\n" +
              " inner join cont_caja b on a.ide_cocaj = b.ide_cocaj\n" +
              " where a.ide_ademple = "+ide_emple+"\n" +
              " and activo_cxcaem = "+activo+" ";
        return sql;
    }
    public String getPuntoEmisionAdquisiciones(String ide_emple, String activo) {
        String sql = "";
        sql = " select a.ide_cxdaem, b.serie_ccdaf, b.autorizacion_ccdaf, b.observacion_ccdaf from cxc_dat_fac_emp a\n" +
              " inner join cxc_datos_fac b on a.ide_ccdaf = b.ide_ccdaf\n" +
              " where a.ide_ademple = "+ide_emple+"\n" +
              " and activo_cxdaem = "+activo+" ";
        return sql;
    }
    
    
        public String getEmpleadoDep(){
         String sql = "select ide_ademple, cedula_ademple, nombres_ademple from adq_empleado order by nombres_ademple";
         return sql;
    }
    public String getEmpleadoDepartamento(String tipo, String empleado, String estado, String departamento) {
        String sql = "select ide_ademde,CEDULA_ADEMPLE,NOMBRES_ADEMPLE from ADQ_EMPLEADO_DEPARTAMENTO a,ADQ_EMPLEADO b where a.IDE_ADEMPLE = b.IDE_ADEMPLE ";
        if (tipo.equals("1")) {
            sql += " and ide_ademde in (" + empleado + ")";
        }
        if (tipo.equals("2")) {
            sql += " and ACTIVO_ADEMDE in (" + estado + ") and IDE_ADEMDE =" + departamento;
        }
        sql += " order by NOMBRES_ADEMPLE";
        return sql;
    }

    public String getEmpleadoAprueba(String tipo, String empleado, String estado, String departamento) {
        String sql = "SELECT IDE_ADEMAP,CEDULA_ADEMPLE,NOMBRES_ADEMPLE FROM ADQ_EMPLEADO_APRUEBA a, ADQ_EMPLEADO b WHERE a.IDE_ADEMPLE = b.IDE_ADEMPLE ";
        if (tipo.equals("1")) {
            sql += " and IDE_ADEMAP in (" + empleado + ")";
        }
        if (tipo.equals("2")) {
            sql += " and ACTIVO_ADEMAP in (" + estado + ") and a.IDE_ADEMDE  =" + departamento;
        }
        sql += " order by NOMBRES_ADEMPLE";
        return sql;
    }

    public String getCargo() {
        String sql = "";
        sql = "SELECT IDE_ADCARG, DETALLE_ADCARG FROM ADQ_CARGO by detalle_gtcar";
        return sql;
    }

    public String getTipoAprobador() {
        String sql = "";
        sql = "SELECT IDE_ADTIAP, DETALLE_ADTIAP FROM ADQ_TIPO_APROBADOR";
        return sql;
    }

    public String getTipoDenominacion() {
        String sql = "";
        sql = "select IDE_ADTIDE, DETALLE_ADTIDE from ADQ_TIPO_DENOMINACION";
        return sql;
    }

    public String getUsuario(String activo) {
        String sql = "";
        sql = "SELECT IDE_USUA, NICK_USUA, NOM_USUA,NOM_PERF FROM SIS_USUARIO a , SIS_PERFIL b WHERE a.IDE_PERF = b.IDE_PERF and ACTIVO_USUA in (" + activo + ")";
        return sql;
    }

    public String getUsuarioSistema(String ide_usua, String activo, String tipo_aprobador) {
        String sql = "";
        sql = "select ide_ademde,NOMBRES_ADEMPLE,CEDULA_ADEMPLE from ADQ_EMPLEADO_DEPARTAMENTO a, ADQ_EMPLEADO b where a.IDE_ADEMPLE=b.IDE_ADEMPLE and ACTIVO_ADEMDE = " + activo + " and IDE_ADTIAP = " + tipo_aprobador + " and IDE_USUA =" + ide_usua;
        return sql;
    }
    
    public String getUsuarioSistemaEmpleado(String ide_usua) {
        String sql = "";
        sql = "select ide_ademple, cedula_ademple, nombres_ademple from adq_empleado where ide_usua =" + ide_usua;
        return sql;
    }
    
    public String getUsuarioCaja(String ide_usua){
        String sql="";
        sql="select a.ide_ademple, cedula_ademple, nombres_ademple, b.detalle_cocaj , c.observacion_ccdaf from adq_empleado a\n" +
"left join (select ide_cxcaem, a.ide_ademple, b.detalle_cocaj from cxc_caja_empleado a left join cont_caja b on a.ide_cocaj = b.ide_cocaj where activo_cxcaem = true) b on a.ide_ademple = b.ide_ademple\n" +
"left join(select a.ide_ccdaf,b.observacion_ccdaf,a.ide_cxdaem,a.ide_ademple from cxc_dat_fac_emp a left join cxc_datos_fac b  on a.ide_ccdaf =b.ide_ccdaf) c on a.ide_ademple = c.IDE_ADEMPLE \n" +
"where  ide_usua = "+ide_usua;
        return sql;
    }
    public String getUsuarioSistemaAprobador(String ide_usua, String activo, String tipo_aprobador) {
        String sql = "";
        sql = "select ide_ademap,NOMBRES_ADEMPLE,CEDULA_ADEMPLE from ADQ_EMPLEADO_APRUEBA a, ADQ_EMPLEADO b where a.IDE_ADEMPLE=b.IDE_ADEMPLE and ACTIVO_ADEMAP = " + activo + "  and IDE_USUA =" + ide_usua;
        return sql;
    }

    public String getMateriales(String ide_adcomp) {
        String sql = "";
        sql = "select IDE_ADCOBI, IDE_ADMATE, IDE_ADCOMP from ADQ_COMPRA_BIENES WHERE IDE_ADCOMP =" + ide_adcomp;
        return sql;
    }
    
    public String getDepartamento (){
        String sql = "";
        sql= "SELECT ide_acuba, nombre_acuba FROM act_ubicacion_activo ORDER BY nombre_acuba";
                return sql;
    }
    public String getDepartamentoDatos (String ide_acuba){
        String sql = "";
        sql= "SELECT ide_acuba, nombre_acuba , codigo_acuba FROM act_ubicacion_activo where ide_acuba in ("+ide_acuba+") ORDER BY nombre_acuba";
                return sql;
    }
    public String getDatosEmpleado (){
        String sql = "";
        sql= "select ide_gtemp, documento_identidad_gtemp, apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp ||' '||\n" +
             "(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as nombres_empleado\n" +
             "from gth_empleado order by nombres_empleado";
                return sql;
    }
    
    public String getDatosProveedor (){
        String sql = "";
        sql= "select ide_geper, identificac_geper, nom_geper from gen_persona order by nom_geper";
                return sql;
    }
    
    public String getDatosPresupuesto (String tipo,String anio, String ide){
        String sql = "";
        sql= "select c.ide_prpot,comprometido_prpot,comprometido_prpot  - (case when devengado is null then 0 else devengado end) as saldoxdevengado,fecha_tramite_prtra,cod_programa_prpro,codigo_clasificador_prcla,detalle_actividad,detalle_subactividad,detalle_prfuf,elaborado,observaciones_prtra,a.ide_geedp,empleado_responsable \n" +
"            from pre_tramite a \n" +
"            left join \n" +
"            ( \n" +
"            select apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp ||' '|| \n" +
"            (case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado_responsable,ide_geedp,apellido_paterno_gtemp||' '||primer_nombre_gtemp as elaborado \n" +
"            from gth_empleado a, gen_empleados_departamento_par b where a.ide_gtemp = b.ide_gtemp \n" +
"            ) b on a.ide_geedp = b.ide_geedp \n" +
"            left join pre_poa_tramite c on a.ide_prtra = c.ide_prtra \n" +
"            left join ( \n" +
"            select a.ide_pranu,a.ide_prpro,a.ide_geani,nom_geani,valor_reformado_pranu,valor_inicial_pranu,valor_codificado_pranu,valor_devengado_pranu,valor_eje_comprometido_pranu,a.ide_prfuf, \n" +
"            cod_programa_prpro,detalle_programa,detalle_proyecto,detalle_producto,detalle_actividad,detalle_subactividad,detalle_prfuf,codigo_clasificador_prcla,descripcion_clasificador_prcla \n" +
"            from pre_anual a, ( \n" +
"            select a.ide_prpro,cod_programa_prpro,detalle_programa,detalle_proyecto,detalle_producto,detalle_actividad,detalle_subactividad,codigo_clasificador_prcla,descripcion_clasificador_prcla \n" +
"            from pre_programa a, ( \n" +
"            select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto, \n" +
"            			proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad, \n" +
"            			 detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a , \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c, \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup \n" +
"            			 and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup \n" +
"            ) b,pre_clasificador c \n" +
"            where a.ide_prfup = b.ide_prfup \n" +
"            and a.ide_prcla = c.ide_prcla \n" +
"            ) b, pre_fuente_financiamiento c,gen_anio d \n" +
"            where a.ide_prpro = b.ide_prpro \n" +
"            and a.ide_prfuf = c.ide_prfuf \n" +
"            and a.ide_geani =d.ide_geani \n" ;
                if(tipo.equals("2")){
                    sql +="     and a.ide_geani = "+anio ;
                }
                    sql+="    order by cod_programa_prpro \n" +
"            ) d on c.ide_pranu = d.ide_pranu \n" +
"            left join (select ide_prpot ,sum(valor_adpres) as devengado \n" +
"            from adq_presupuesto group by ide_adpres ) e on c.ide_prpot = e.ide_prpot\n" +
"            where not c.ide_prpot is null";
                
                 if(tipo.equals("3")){
                    sql +=" and (comprometido_prpot  - (case when devengado is null then 0 else devengado end)) > 0 ";
                }
                 if(tipo.equals("4")){
                    sql +=" and c.ide_prpot =  "+ide;
                }
                 return sql;
    }
    
    public String getDatosEmpleadoConsulta (String ide_gtemp){
        String sql = "";
        sql= "select ide_gtemp, documento_identidad_gtemp, apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp ||' '||\n" +
             "(case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as nombres_empleado\n" +
             "from gth_empleado where ide_gtemp in ("+ide_gtemp+") order by nombres_empleado";
                return sql;
    }
    public String getDatosProveedorConsulta (String ide_geper){
        String sql = "";
        sql= " select ide_geper, identificac_geper, nom_geper from gen_persona  "
             + "where ide_geper in ("+ide_geper+") order by nom_geper";
                return sql;
    }
    
    public String getDatosPresupuestoConsulta (){
        String sql = "";
       
        sql= "select c.ide_prpot,codigo_clasificador_prcla,detalle_subactividad,detalle_prfuf\n" +
"            from pre_tramite a \n" +
"            left join \n" +
"            ( \n" +
"            select apellido_paterno_gtemp||' '||(case when apellido_materno_gtemp is null then '' else apellido_materno_gtemp end)||' '||primer_nombre_gtemp ||' '|| \n" +
"            (case when segundo_nombre_gtemp is null then '' else segundo_nombre_gtemp end) as empleado_responsable,ide_geedp,apellido_paterno_gtemp||' '||primer_nombre_gtemp as elaborado \n" +
"            from gth_empleado a, gen_empleados_departamento_par b where a.ide_gtemp = b.ide_gtemp \n" +
"            ) b on a.ide_geedp = b.ide_geedp \n" +
"            left join pre_poa_tramite c on a.ide_prtra = c.ide_prtra \n" +
"            left join ( \n" +
"            select a.ide_pranu,a.ide_prpro,a.ide_geani,nom_geani,valor_reformado_pranu,valor_inicial_pranu,valor_codificado_pranu,valor_devengado_pranu,valor_eje_comprometido_pranu,a.ide_prfuf, \n" +
"            cod_programa_prpro,detalle_programa,detalle_proyecto,detalle_producto,detalle_actividad,detalle_subactividad,detalle_prfuf,codigo_clasificador_prcla,descripcion_clasificador_prcla \n" +
"            from pre_anual a, ( \n" +
"            select a.ide_prpro,cod_programa_prpro,detalle_programa,detalle_proyecto,detalle_producto,detalle_actividad,detalle_subactividad,codigo_clasificador_prcla,descripcion_clasificador_prcla \n" +
"            from pre_programa a, ( \n" +
"            select a.ide_prfup,codigo_subactividad,detalle_subactividad,subactividad,detalle_actividad,actividad,detalle_producto,producto,detalle_proyecto, \n" +
"            			proyecto,detalle_programa ,programa from (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_subactividad,detalle_prfup as detalle_subactividad, \n" +
"            			 detalle_prnfp as subactividad from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =5) a , \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_actividad,detalle_prfup as detalle_actividad,detalle_prnfp as actividad \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =4) b, \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_producto,detalle_prfup as detalle_producto,detalle_prnfp as producto \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =3 ) c, \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_proyecto,detalle_prfup as detalle_proyecto,detalle_prnfp as proyecto \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =2) d, \n" +
"            			 (select ide_prfup ,pre_ide_prfup,codigo_prfup as codigo_programa,detalle_prfup as detalle_programa,detalle_prnfp as programa \n" +
"            			 from pre_funcion_programa a, pre_nivel_funcion_programa b where a.ide_prnfp = b.ide_prnfp and a.ide_prnfp =1) e where a.pre_ide_prfup = b.ide_prfup \n" +
"            			 and b.pre_ide_prfup = c.ide_prfup and c.pre_ide_prfup = d.ide_prfup and d.pre_ide_prfup = e.ide_prfup \n" +
"            ) b,pre_clasificador c \n" +
"            where a.ide_prfup = b.ide_prfup \n" +
"            and a.ide_prcla = c.ide_prcla \n" +
"            ) b, pre_fuente_financiamiento c,gen_anio d \n" +
"            where a.ide_prpro = b.ide_prpro \n" +
"            and a.ide_prfuf = c.ide_prfuf \n" +
"            and a.ide_geani =d.ide_geani \n" +
"            order by cod_programa_prpro \n" +
"            ) d on c.ide_pranu = d.ide_pranu \n" +
"            left join (select ide_prpot ,sum(valor_adpres) as devengado \n" +
"            from adq_presupuesto group by ide_adpres ) e on c.ide_prpot = e.ide_prpot\n" +
"            where not c.ide_prpot is null\n" ;
        
       
                return sql;
    }
    
    public void setUpdateEstadoGastos(int codigo, String dato) {
        String auSql = "UPDATE adq_compra\n"
                + "set APRUEBA_GASTO_ADCOMP= true,\n"
                + "ADQ_IDE_ADEMDE = "+dato+"\n"
                + "where ide_adcomp = " + codigo;
        conSql();
        conSql.ejecutarSql(auSql);
        desConSql();
    }

    /*
    Cargar certificaciones, partida y valor
     */
    public TablaGenerica getBuscarDatos(int anio, int cadena, String fecha) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select DISTINCT (cedtmc|| '-' ||NDOCDC|| '-' ||AUAD01) as id, NDOCDC,cedtmc,AUAD02,MONTDT,AUAD01\n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "inner join USFIMRU.PRCO01 on  CUENDT = CUENDC and AUAD02 = AUA2DC\n"
                + "inner join USFIMRU.TIGSA_GLM03 on CUENMC = CUENDT\n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=1" + cadena + "14 \n"
                + "AND SAPRDT<=1" + cadena + "15 \n"
                + "AND AUAD02 is not null \n"
                + "AND ANIODC =" + anio + "\n"
                + "AND TIPLMC= 'R'\n"
                + "AND substr(FDOCDT,1,5) <= 1" + fecha + "");
        tabFuncionario.ejecutarSql();
        desOraclesql();
        return tabFuncionario;
    }

    public TablaGenerica getMuestraDatos(String id, int anio, int cadena, String fecha) {
        conOraclesql();
        TablaGenerica tabFuncionario = new TablaGenerica();
        conOraclesql();
        tabFuncionario.setConexion(conOracle);
        tabFuncionario.setSql("select DISTINCT NDOCDC,cedtmc,AUAD02,MONTDT,AUAD01,NOLAAD \n"
                + "from USFIMRU.TIGSA_GLB01 \n"
                + "inner join USFIMRU.PRCO01 on  CUENDT = CUENDC and AUAD02 = AUA2DC\n"
                + "inner join USFIMRU.TIGSA_GLM03 on CUENMC = CUENDT\n"
                + "where STATDT='E' \n"
                + "AND CCIADT <> 'CM' and CCIADT <> 'MR' \n"
                + "AND SAPRDT>=1" + cadena + "14 \n"
                + "AND SAPRDT<=1" + cadena + "15 \n"
                + "AND AUAD02 is not null \n"
                + "AND ANIODC =" + anio + "\n"
                + "AND TIPLMC= 'R'\n"
                + "AND substr(FDOCDT,1,5) <= 1" + fecha + " \n"
                + "and CONCAT(CONCAT(CONCAT(CONCAT(cedtmc,'-'),NDOCDC),'-'),AUAD01) = '" + id + "'");
        tabFuncionario.ejecutarSql();
        tabFuncionario.imprimirSql();
        desOraclesql();
        return tabFuncionario;
    }

    private void conSql() {
        if (conSql == null) {
            conSql = new Conexion();
            conSql.setUnidad_persistencia(utilitario.getConfiguraEmpresa().getRecursoJDBC());
        }
    }

    private void desConSql() {
        if (conSql != null) {
            conSql.desconectar(true);
            conSql = null;
        }
    }

    private void conOraclesql() {
        if (conOracle == null) {
            conOracle = new Conexion();
            conOracle.setUnidad_persistencia(utilitario.getConfiguraEmpresa().getRecursoJDBC());        
        }
    }

    private void desOraclesql() {
        if (conOracle != null) {
            conOracle.desconectar(true);
            conOracle = null;
        }
    }
    
    public String getSolicitudCompra (String tipo, String solicitud){
        String sql = "";
        if (tipo.equals("1")){
        sql = "select ide_adcomp, numero_orden_adcomp, b.identificac_geper, b.nom_geper, fecha_solicitud_adcomp, valor_adcomp, detalle_adcomp\n" +
               "from adq_compra a\n" +
               "left join gen_persona b on a.ide_geper = b.ide_geper\n" +
               "where a.facturado_adcomp = false";
        }
        if (tipo.equals("2")){
            sql += " and ide_adcomp = "+solicitud+"";
        }
                return sql;
    }
    public String getdetalleSolicitudCompra (String tipo, String solicitud){
        String sql = "";
        sql += "select ide_adcobi, ide_adcomp, b.nombre_inarti, especificaciones_adcobi from adq_compra_bienes a\n" +
               "left join inv_articulo b on a.ide_inarti = b.ide_inarti";
        if (tipo.equals("2")){
        sql += " where a.ide_adcomp = "+solicitud+"";
        }
        if (tipo.equals("3")){
            sql += " and ide_adcobi in ( "+solicitud+")";
        }
                return sql;
    }
    public String getdetalleFacturaCompra (String tipo, String factura){
        String sql = "";
        sql += "select ide_cpdfa, b.nombre_inarti, cantidad_cpdfa, precio_cpdfa, valor_cpdfa as subtotal\n" +
               "from cxp_detall_factur a \n" +
               "left join inv_articulo b on a.ide_inarti = b.ide_inarti\n" +
               "where recibido_compra_cpdfa = false";
        if (tipo.equals("2")){
        sql += " and a.ide_cpcfa = "+factura+"";
        }
                return sql;
    }
public String getUnidad() {
        String sql = "";
        sql = "select ide_inuni ,nombre_inuni from inv_unidad order by nombre_inuni";
        return sql;
          }
    
    
}

