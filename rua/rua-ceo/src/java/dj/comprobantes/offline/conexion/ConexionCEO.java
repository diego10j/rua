/*
 *********************************************************************
 Objetivo: Clase ConexionCEO utilizada en todo el proyecto
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.conexion;

import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author diego.jacome
 */
public class ConexionCEO {

    public Connection conCeo;

    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    public ConexionCEO() throws GenericException {
        conectar();
    }

    /**
     * Crea la conexión a la base de datos utilizando el pool de conexiones
     *
     * @throws GenericException
     */
    private void conectar() throws GenericException {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(utilitario.getConfiguraEmpresa().getRecursoJDBC());
            this.conCeo = ds.getConnection();
            //this.conCeo.setAutoCommit(true); 
        } catch (NamingException | SQLException e) {
            throw new GenericException("ERROR. al conectar ", e);
        }
    }

    /**
     * Ejecuta una consulta SQL
     *
     * @param sql
     * @return
     * @throws GenericException
     */
    public ResultSet consultar(String sql) throws GenericException {
        ResultSet resultSet = null;
        try {
            //System.out.println(sql);
            Statement sentensia = conCeo.createStatement();
            resultSet = sentensia.executeQuery(sql);
        } catch (SQLException e) {
            throw new GenericException("ERROR. al consultar sql: " + sql, e);
        }
        return resultSet;
    }

    /**
     * Ejecuta una sentencia SQL
     *
     * @param sql
     * @return
     * @throws GenericException
     */
    public String ejecutar(String sql) throws GenericException {
        Statement sentensia = null;
        try {
            sentensia = conCeo.createStatement();
            sentensia.executeUpdate(sql);
        } catch (SQLException e) {
            throw new GenericException("ERROR. al ejecutar sql: " + sql, e);
        } finally {
            if (sentensia != null) {
                try {
                    sentensia.close();
                } catch (Exception e) {
                }
            }
        }
        return "";
    }

    /**
     * Retorna un PreparedStatement de una sentencia sql
     *
     * @param sql
     * @return
     * @throws GenericException
     */
    public PreparedStatement getPreparedStatement(String sql) throws GenericException {
        try {
            return conCeo.prepareStatement(sql);
        } catch (SQLException e) {
            throw new GenericException(e);
        }
    }

    /**
     * Desconecta la base de datos
     */
    public void desconectar() {
        try {
            if (conCeo != null) {
                conCeo.close();
            }
        } catch (Exception e) {
        }
    }

    public Connection getConnection() {
        return conCeo;
    }

    public void setConnection(Connection conCeo) {
        this.conCeo = conCeo;
    }

}
