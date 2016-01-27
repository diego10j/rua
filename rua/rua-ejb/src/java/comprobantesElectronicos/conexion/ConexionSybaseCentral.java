/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConexionSybaseCentral {

    public Connection cobCentral;

    public ConexionSybaseCentral() {
        conectar();
    }

    private void conectar() {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("jdbc/cobisJndi");
            this.cobCentral = ds.getConnection();
            this.cobCentral.setAutoCommit(true);
        } catch (NamingException | SQLException e) {
        }

    }

    public ResultSet consultar(String sql) {

        ResultSet resultado = null;
        try {
            System.out.println(sql);
            Statement sentensia = cobCentral.createStatement();
            resultado = sentensia.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public String ejecutar(String sql) {
        try {
            System.out.println("*** " + sql);
            Statement sentensia = cobCentral.createStatement();
            sentensia.executeUpdate(sql);
            sentensia.close();
            return "";
        } catch (Exception e) {
            System.out.println("ERROR al ejecutar : " + sql);
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void desconectar() {
        try {
            cobCentral.close();
        } catch (Exception e) {
        }
    }

    public Connection getConnection() {
        return cobCentral;
    }

    public void setConnection(Connection cobCentral) {
        this.cobCentral = cobCentral;
    }

}
