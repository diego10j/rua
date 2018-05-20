/*
 *********************************************************************
 Objetivo: Servicio que implementa interface XmlComprobanteDAO
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.conexion.ConexionCEO;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.XmlComprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Stateless;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class XmlComprobanteDAOImp implements XmlComprobanteDAO {

    @Override
    public XmlComprobante getSriComprobanteActual(Comprobante comprobante) throws GenericException {
        XmlComprobante sriComprobante = null;
        ResultSet res = null;
        ConexionCEO conn = new ConexionCEO();
        try {
            res = conn.consultar("SELECT * FROM sri_xml_comprobante WHERE ide_srcom=" + comprobante.getCodigocomprobante() + " order by fecha_hora_srxmc desc");
            if (res.next()) {
                sriComprobante = new XmlComprobante();
                sriComprobante.setCodigocomprobante(comprobante);
                sriComprobante.setCodigocompsri(res.getLong("ide_srxmc"));
                sriComprobante.setFecha(res.getDate("fecha_hora_srxmc"));
                sriComprobante.setXmlcomprobante(res.getString("xml_srxmc"));
                sriComprobante.setMensajerecepcion(res.getString("msg_recepcion_srxmc"));
                sriComprobante.setMensajeautorizacion(res.getString("msg_autoriza_srxmc"));
                if (res.getString("ide_sresc") != null) {
                    sriComprobante.setCodigoestado(EstadoComprobanteEnum.getCodigo(res.getString("ide_sresc")));
                }
            }
        } catch (GenericException | SQLException e) {
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
        return sriComprobante;
    }

    @Override
    public void guardar(XmlComprobante sriComprobante) throws GenericException {
        ConexionCEO conn = new ConexionCEO();
        PreparedStatement preparedStatement = null;
        try {
            if (sriComprobante.getCodigocompsri() == null) {
                String sc_msg_autoriza = sriComprobante.getMensajeautorizacion() == null ? null : sriComprobante.getMensajeautorizacion().replace("'", "\"");
                String sc_msg_recepcion = sriComprobante.getMensajerecepcion() == null ? null : sriComprobante.getMensajerecepcion().replace("'", "\"");
                String sc_xml_comp = sriComprobante.getXmlcomprobante() == null ? null : sriComprobante.getXmlcomprobante().replace("'", "\"");
                String sql = "INSERT INTO sri_xml_comprobante "
                        + "( ide_srcom,ide_sresc,fecha_hora_srxmc,xml_srxmc ,msg_recepcion_srxmc,msg_autoriza_srxmc) "
                        + "values (?,?,now(),?,?,?)";
                preparedStatement = conn.getPreparedStatement(sql);
                preparedStatement.setLong(1, sriComprobante.getCodigocomprobante().getCodigocomprobante());
                preparedStatement.setInt(2, sriComprobante.getCodigoestado());
                preparedStatement.setString(3, sc_xml_comp);
                preparedStatement.setString(4, sc_msg_recepcion);
                preparedStatement.setString(5, sc_msg_autoriza);
                preparedStatement.executeUpdate();
            } else {
                String sc_msg_autoriza = sriComprobante.getMensajeautorizacion() == null ? null : sriComprobante.getMensajeautorizacion().replace("'", "\"");
                String sc_msg_recepcion = sriComprobante.getMensajerecepcion() == null ? null : sriComprobante.getMensajerecepcion().replace("'", "\"");
                String sc_xml_comp = sriComprobante.getXmlcomprobante() == null ? null : sriComprobante.getXmlcomprobante().replace("'", "\"");
                String sql = "UPDATE sri_xml_comprobante "
                        + "set ide_srcom=?, "
                        + "ide_sresc=?, "
                        + "fecha_hora_srxmc=now(), "
                        + "xml_srxmc=?, "
                        + "msg_recepcion_srxmc=?, "
                        + "msg_autoriza_srxmc=? "
                        + "WHERE ide_srxmc=?";
                preparedStatement = conn.getPreparedStatement(sql);
                preparedStatement.setLong(1, sriComprobante.getCodigocomprobante().getCodigocomprobante());
                preparedStatement.setInt(2, sriComprobante.getCodigoestado());
                preparedStatement.setString(3, sc_xml_comp);
                preparedStatement.setString(4, sc_msg_recepcion);
                preparedStatement.setString(5, sc_msg_autoriza);
                preparedStatement.setLong(6, sriComprobante.getCodigocompsri());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new GenericException("ERROR. No se puede guardar el SriComprobante", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                }
            }
            conn.desconectar();
        }
    }
}
