/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_adquisicion.ejb;

import framework.aplicacion.TablaGenerica;
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
        sql = "SELECT IDE_ADCARG, DETALLE_ADCARG FROM ADQ_CARGO";
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
}
