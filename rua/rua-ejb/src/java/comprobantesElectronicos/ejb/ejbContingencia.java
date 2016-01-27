/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.ejb;

import comprobantesElectronicos.dao.ComprobanteDAOLocal;
import comprobantesElectronicos.dao.EstadoComprobanteDAOLocal;
import comprobantesElectronicos.dao.SriComprobanteDAOLocal;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Sricomprobante;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ejbContingencia {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private ComprobanteDAOLocal comprobanteDAO;
    @EJB
    private EstadoComprobanteDAOLocal estadoComprobanteDAO;
    @EJB
    private ejbRecepcionComprobante ejbRecepcionComprobnate;
    @EJB
    private ejbAutorizaComprobante ejbAutorizaComprobante;
    @EJB
    private SriComprobanteDAOLocal sriComprobanteDAO;

    /**
     * Intenta enviar todos los comprobantes que se encuentren en estado de
     * contingencia
     *
     * @return
     */
    public String enviarComprobantesContingencia() {
        //Busca todos los comprobantes en estado CONTINGENCIA para que puedan enviarlos
        Estadocomprobante estadoContingencia = estadoComprobanteDAO.getEstadoContingencia();
        List<Comprobante> lisComprobantes = comprobanteDAO.getComprobantesEstado(estadoContingencia);
        int intExitosos = 0;
        StringBuilder stbCabecera = new StringBuilder();
        if (lisComprobantes.isEmpty() == false) {
            StringBuilder stbMensajes = new StringBuilder();
            for (Comprobante comprobanteActual : lisComprobantes) {
                //Busca la cadena xml firmada
                Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobanteActual);
                if (sriComprobante != null) {
                    if (sriComprobante.getXmlcomprobante() != null && sriComprobante.getXmlcomprobante().isEmpty() == false) {
                        String mensaje = ejbRecepcionComprobnate.eviarDocumentoFirmado(comprobanteActual, sriComprobante.getXmlcomprobante());
                        stbMensajes.append("[CLAVE DE ACCESO]  ").append(comprobanteActual.getClaveacceso()).append("\n");
                        if (mensaje.equalsIgnoreCase("RECIBIDA")) {
                            //Tiempo de espera antes de autorizar
                            ejbAutorizaComprobante.esperarAntesAutorizar();
                            mensaje = ejbAutorizaComprobante.enviarAutorizacionDocumento(comprobanteActual.getClaveacceso());
                            if (mensaje.equalsIgnoreCase("AUTORIZADO")) {
                                stbMensajes.append("[OK]  ").append("COMPROBANTE AUTORIZADO").append("\n");
                                intExitosos++;
                            } else {
                                stbMensajes.append("[FALLO AUTORIZACION]  ").append(mensaje).append("\n");
                            }
                        } else {
                            stbMensajes.append("[FALLO ENVIO]  ").append(mensaje).append("\n");
                        }
                    }
                }
            }
            stbCabecera.append("[ENVIANDO] >>>>>>>> COMPROBANTES EN ESTADO DE CONTINGENCIA").append("\n");
            stbCabecera.append("NUM. DE COMPROBANTES : ").append(lisComprobantes.size()).append("\n");
            stbCabecera.append("NUM. AUTORIZADOS : ").append(intExitosos).append("\n");
            stbCabecera.append("NUM. NO AUTORIZADOS : ").append((lisComprobantes.size() - intExitosos)).append("\n");
            stbCabecera.append(stbMensajes.toString());
            return stbCabecera.toString().trim();
        }
        return null;
    }

    /**
     * Intenta enviar todos los comprobantes que se encuentren en estado de
     * contingencia
     *
     * @return
     */
    public String autorizarComprobantesRecibidos() {
        //Busca todos los comprobantes en estado RECIBIDO para que puedan ser Autorizados
        Estadocomprobante estadoRecibido = estadoComprobanteDAO.getEstadoRecibida();
        List<Comprobante> lisComprobantes = comprobanteDAO.getComprobantesEstado(estadoRecibido);
        int intExitosos = 0;

        StringBuilder stbCabecera = new StringBuilder();
        if (lisComprobantes.isEmpty() == false) {
            StringBuilder stbMensajes = new StringBuilder();
            for (Comprobante comprobanteActual : lisComprobantes) {
                Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobanteActual);
                if (sriComprobante != null) {
                    stbMensajes.append("[CLAVE DE ACCESO]  ").append(comprobanteActual.getClaveacceso()).append("\n");
                    String mensaje = ejbAutorizaComprobante.enviarAutorizacionDocumento(comprobanteActual.getClaveacceso());
                    if (mensaje.equalsIgnoreCase("AUTORIZADO")) {
                        stbMensajes.append("[OK]  ").append("COMPROBANTE AUTORIZADO").append("\n");
                        intExitosos++;
                    } else {
                        stbMensajes.append("[FALLO AUTORIZACION]  ").append(mensaje).append("\n");
                    }
                }
            }
            stbCabecera.append("[ENVIANDO] >>>>>>>> COMPROBANTES EN ESTADO RECIBIDO").append("\n");
            stbCabecera.append("NUM. DE COMPROBANTES : ").append(lisComprobantes.size()).append("\n");
            stbCabecera.append("NUM. AUTORIZADOS : ").append(intExitosos).append("\n");
            stbCabecera.append("NUM. NO AUTORIZADOS : ").append((lisComprobantes.size() - intExitosos)).append("\n");
            stbCabecera.append(stbMensajes.toString());
            return stbCabecera.toString().trim();
        }
        return null;
    }

}
