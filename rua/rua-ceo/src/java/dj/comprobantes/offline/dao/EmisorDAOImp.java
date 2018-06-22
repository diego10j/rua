/*
 *********************************************************************
 Objetivo: Servicio que implementa interface EmisorDAO
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.conexion.ConexionCEO;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.exception.GenericException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Stateless;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class EmisorDAOImp implements EmisorDAO {

    private Emisor emisor = null;

    @Override
    public Emisor getEmisor(String sucursal) throws GenericException {

        ConexionCEO conn = new ConexionCEO();
        ResultSet resultSet = null;
        try {
            resultSet = conn.consultar("select ide_sremi,identificacion_empr,nom_corto_empr,nom_empr,direccion_empr,contribuyenteespecial_empr,obligadocontabilidad_empr,direccion_sucu,\n"
                    + " tiempo_espera_sremi,ambiente_sremi,wsdl_recep_offline_sremi,wsdl_autori_offline_sremi,telefono_empr,fines_lucro_empr "
                    + " from sri_emisor a "
                    + " inner join sis_sucursal b on a.ide_sucu=b.ide_sucu "
                    + " inner join sis_empresa c on b.ide_empr=c.ide_empr "
                    + " where a.ide_sucu=" + sucursal);
            if (resultSet.next()) {
                emisor = new Emisor();
                emisor.setCodigoemisor(resultSet.getInt("ide_sremi"));
                emisor.setRuc(resultSet.getString("identificacion_empr"));
                emisor.setRazonsocial(resultSet.getString("nom_empr"));
                emisor.setNombrecomercial(resultSet.getString("nom_corto_empr"));
                emisor.setDirmatriz(resultSet.getString("direccion_empr"));
                emisor.setContribuyenteespecial(resultSet.getString("contribuyenteespecial_empr"));
                emisor.setObligadocontabilidad(resultSet.getString("obligadocontabilidad_empr"));
                emisor.setTiempomaxespera(resultSet.getInt("tiempo_espera_sremi"));
                emisor.setAmbiente(resultSet.getInt("ambiente_sremi"));
                emisor.setWsdlrecepcion(resultSet.getString("wsdl_recep_offline_sremi"));
                emisor.setWsdlautirizacion(resultSet.getString("wsdl_autori_offline_sremi"));
                emisor.setDirsucursal(resultSet.getString("direccion_sucu"));
                emisor.setTelefonos(resultSet.getString("telefono_empr"));
                if (resultSet.getString("fines_lucro_empr") != null) {
                    emisor.setSinFinesLucro(resultSet.getBoolean("fines_lucro_empr"));
                }
            }
        } catch (SQLException e) {
            throw new GenericException("ERROR. No se puede retornar el Emisor", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                }
            }
            conn.desconectar();
        }

        if (emisor == null) {
            throw new GenericException("ERROR. No existe Emisor");
        }
        return emisor;
    }
}
