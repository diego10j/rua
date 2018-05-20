/*
 *********************************************************************
 Objetivo: Servicio que implementa interface FirmaDAO
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.conexion.ConexionCEO;
import dj.comprobantes.offline.dto.Firma;
import dj.comprobantes.offline.exception.GenericException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class FirmaDAOImp implements FirmaDAO {

    @Override
    public Firma getFirmaDisponible(String sucursal) throws GenericException {
        ConexionCEO conn = new ConexionCEO();
        ResultSet res = null;
        Firma firma = null;
        try {
            res = conn.consultar("select * from sri_firma_digital where disponible_srfid =true and ide_sucu=" + sucursal);
            if (res.next()) {
                firma = new Firma();
                firma.setCodigofirma(res.getInt("ide_srfid"));
                firma.setRutafirma(res.getString("ruta_srfid"));
                firma.setClavefirma(res.getString("password_srfid"));
                firma.setFechaingreso(res.getDate("fecha_ingreso_srfid"));
                firma.setFechacaducidad(res.getDate("fecha_caduca_srfid"));
                firma.setNombrerepresentante(res.getString("nombre_representante_srfid"));
                firma.setCorreorepresentante(res.getString("correo_representante_srfid"));
                firma.setDisponiblefirma(res.getBoolean("disponible_srfid"));
            }
        } catch (SQLException e) {
            throw new GenericException(e);
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                }
            }
            conn.desconectar();
        }
        if (firma == null) {
            throw new GenericException("ERROR. No tiene una Firma Electrónica disponible");
        }
        return firma;
    }

    @Override
    public List<Firma> getTodasFirmas() throws GenericException {
        ConexionCEO conn = new ConexionCEO();
        ResultSet res = null;
        List<Firma> lisFirmas = new ArrayList<>();
        try {
            res = conn.consultar("select * from sri_firma_digital");
            while (res.next()) {
                Firma firma = new Firma();
                firma.setCodigofirma(res.getInt("ide_srfid"));
                firma.setRutafirma(res.getString("ruta_srfid"));
                firma.setClavefirma(res.getString("password_srfid"));
                firma.setFechaingreso(res.getDate("fecha_ingreso_srfid"));
                firma.setFechacaducidad(res.getDate("fecha_caduca_srfid"));
                firma.setNombrerepresentante(res.getString("nombre_representante_srfid"));
                firma.setCorreorepresentante(res.getString("correo_representante_srfid"));
                firma.setDisponiblefirma(res.getBoolean("disponible_srfid"));
                lisFirmas.add(firma);
            }
            res.close();
        } catch (SQLException e) {
            throw new GenericException(e);
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                }
            }
            conn.desconectar();
        }
        return lisFirmas;
    }

}
