/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.dao;

import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Sricomprobante;
import framework.aplicacion.TablaGenerica;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class SriComprobanteDAO implements SriComprobanteDAOLocal {

    private final Utilitario utilitario = new Utilitario();

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
        String sc_msg_autoriza = sriComprobante.getMensajeautorizacion() == null ? "Null" : "'" + sriComprobante.getMensajeautorizacion().replace("'", "\"") + "'";
        String sc_msg_recepcion = sriComprobante.getMensajerecepcion() == null ? "Null" : "'" + sriComprobante.getMensajerecepcion().replace("'", "\"") + "'";
        String sc_xml_comp = sriComprobante.getXmlcomprobante() == null ? "Null" : "'" + sriComprobante.getXmlcomprobante().replace("'", "\"") + "'";
        utilitario.getConexion().ejecutarSql("INSERT INTO sri_xml_comprobante "
                + "( ide_srcom,ide_sresc,fecha_hora_srxmc,xml_srxmc ,msg_recepcion_srxmc,msg_autoriza_srxmc) "
                + "values "
                + "(" + sriComprobante.getCodigocomprobante().getCodigocomprobante() + ""
                + "," + sriComprobante.getCodigoestado().getCodigoestado() + ","
                + "now()," + sc_xml_comp
                + "," + sc_msg_recepcion
                + "," + sc_msg_autoriza + ")");

    }

    @Override
    public void actualizar(Sricomprobante sriComprobante) {
        String sc_msg_autoriza = sriComprobante.getMensajeautorizacion() == null ? "Null" : "'" + sriComprobante.getMensajeautorizacion().replace("'", "\"") + "'";
        String sc_msg_recepcion = sriComprobante.getMensajerecepcion() == null ? "Null" : "'" + sriComprobante.getMensajerecepcion().replace("'", "\"") + "'";
        String sc_xml_comp = sriComprobante.getXmlcomprobante() == null ? "Null" : "'" + sriComprobante.getXmlcomprobante().replace("'", "\"") + "'";
        utilitario.getConexion().ejecutarSql("UPDATE sri_xml_comprobante set ide_srcom=" + sriComprobante.getCodigocomprobante().getCodigocomprobante()
                + ", ide_sresc=" + sriComprobante.getCodigoestado().getCodigoestado() + " ,"
                + "fecha_hora_srxmc=now(), xml_srxmc=" + sc_xml_comp
                + ", msg_recepcion_srxmc=" + sc_msg_recepcion
                + ", msg_autoriza_srxmc=" + sc_msg_autoriza + " "
                + "WHERE ide_srxmc=" + sriComprobante.getCodigocompsri());
    }

    @Override
    public List<Sricomprobante> getTodosSriComprobantes(Comprobante comprobante) {

        List<Sricomprobante> lisSriComprobante = new ArrayList();

        TablaGenerica tab_consulta = utilitario.consultar("SELECT * FROM sri_xml_comprobante WHERE ide_srcom=" + comprobante.getCodigocomprobante() + " order by fecha_hora_srxmc desc");
        for (int i = 0; i < tab_consulta.getTotalFilas(); i++) {
            Sricomprobante sriComprobante = new Sricomprobante();
            sriComprobante.setCodigocomprobante(comprobante);
            sriComprobante.setCodigocompsri(new Long(tab_consulta.getValor(i, "ide_srxmc")));
            sriComprobante.setFecha(utilitario.getFechaHora(tab_consulta.getValor(i, "fecha_hora_srxmc")));
            sriComprobante.setXmlcomprobante(tab_consulta.getValor(i, "xml_srxmc"));
            sriComprobante.setMensajerecepcion(tab_consulta.getValor(i, "msg_recepcion_srxmc"));
            sriComprobante.setMensajeautorizacion(tab_consulta.getValor(i, "msg_autoriza_srxmc"));
            if (tab_consulta.getValor(i, "ide_sresc") != null) {
                sriComprobante.setCodigoestado(new Estadocomprobante(tab_consulta.getValor(i, "ide_sresc")));
            }
            lisSriComprobante.add(sriComprobante);
        }

        return lisSriComprobante;
    }

}
