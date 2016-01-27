/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.conexion.ConexionSybaseCentral;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Sricomprobante;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author dfjacome
 */
@Stateless
public class SriComprobanteDAO implements SriComprobanteDAOLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Sricomprobante getSriComprobanteActual(Comprobante comprobante) {
        List<Sricomprobante> lisSriComprobantes = getTodosSriComprobantes(comprobante);
        if (lisSriComprobantes != null && !lisSriComprobantes.isEmpty()) {
            return lisSriComprobantes.get(0);
        }
        return null;
    }

    @Override
    public void crear(Sricomprobante sriComprobante) {

        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        try {
            String sc_msg_autoriza = sriComprobante.getMensajeautorizacion() == null ? "Null" : "'" + sriComprobante.getMensajeautorizacion().replace("'", "\"") + "'";
            String sc_msg_recepcion = sriComprobante.getMensajerecepcion() == null ? "Null" : "'" + sriComprobante.getMensajerecepcion().replace("'", "\"") + "'";
            String sc_xml_comp = sriComprobante.getXmlcomprobante() == null ? "Null" : "'" + sriComprobante.getXmlcomprobante().replace("'", "\"") + "'";
            conn.ejecutar("INSERT INTO cob_remesas..re_sri_comprobante "
                    + "( sc_secuencial,sc_estado,sc_fecha,sc_xml_comp ,sc_msg_recepcion,sc_msg_autoriza) "
                    + "values "
                    + "(" + sriComprobante.getCodigocomprobante().getCodigocomprobante() + ""
                    + ",'" + sriComprobante.getCodigoestado().getCodigoestado() + "',"
                    + "getDate()," + sc_xml_comp
                    + "," + sc_msg_recepcion
                    + "," + sc_msg_autoriza  + ")");
        } catch (Exception e) {

        } finally {
            conn.desconectar();
        }

    }

    @Override
    public void actualizar(Sricomprobante sriComprobante) {
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        try {
            String sc_msg_autoriza = sriComprobante.getMensajeautorizacion() == null ? "Null" : "'" + sriComprobante.getMensajeautorizacion().replace("'", "\"") + "'";
            String sc_msg_recepcion = sriComprobante.getMensajerecepcion() == null ? "Null" : "'" + sriComprobante.getMensajerecepcion().replace("'", "\"") + "'";
            String sc_xml_comp = sriComprobante.getXmlcomprobante() == null ? "Null" : "'" + sriComprobante.getXmlcomprobante().replace("'", "\"") + "'";
            conn.ejecutar("UPDATE cob_remesas..re_sri_comprobante set sc_secuencial=" + sriComprobante.getCodigocomprobante().getCodigocomprobante()
                    + ", sc_estado='" + sriComprobante.getCodigoestado().getCodigoestado() + "' ,"
                    + "sc_fecha=getDate(), sc_xml_comp=" + sc_xml_comp
                    + ", sc_msg_recepcion=" + sc_msg_recepcion
                    + ", sc_msg_autoriza=" + sc_msg_autoriza + " "
                    + "WHERE sc_codigo=" + sriComprobante.getCodigocompsri());
        } catch (Exception e) {

        } finally {
            conn.desconectar();
        }
    }

    @Override
    public List<Sricomprobante> getTodosSriComprobantes(Comprobante comprobante) {
        ConexionSybaseCentral conn = new ConexionSybaseCentral();
        List<Sricomprobante> lisSriComprobante = new ArrayList();
        try {
            ResultSet res = conn.consultar("SELECT * FROM cob_remesas..re_sri_comprobante WHERE sc_secuencial=" + comprobante.getCodigocomprobante() + " order by sc_fecha desc");
            while (res.next()) {
                Sricomprobante sriComprobante = new Sricomprobante();
                sriComprobante.setCodigocomprobante(comprobante);
                sriComprobante.setCodigocompsri(res.getLong("sc_codigo"));
                sriComprobante.setFecha(res.getDate("sc_fecha"));
                sriComprobante.setXmlcomprobante(res.getString("sc_xml_comp"));
                sriComprobante.setMensajerecepcion(res.getString("sc_msg_recepcion"));
                sriComprobante.setMensajeautorizacion(res.getString("sc_msg_autoriza"));
                if (res.getString("sc_estado") != null) {
                    sriComprobante.setCodigoestado(new Estadocomprobante(res.getString("sc_estado")));
                }
                lisSriComprobante.add(sriComprobante);
            }
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.desconectar();
        }
        return lisSriComprobante;
    }

}
