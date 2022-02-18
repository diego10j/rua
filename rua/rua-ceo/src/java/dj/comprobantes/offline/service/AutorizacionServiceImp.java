/*
 *********************************************************************
 Objetivo: Servicio que implementa interface AutorizacionService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoAmbienteEnum;
import dj.comprobantes.offline.exception.GenericException;

import com.sun.xml.ws.client.BindingProviderProperties;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.util.UtilitarioCeo;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantesOffline;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantesOfflineService;
import ec.gob.sri.comprobantes.ws.aut.Mensaje;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class AutorizacionServiceImp implements AutorizacionService {

    @EJB
    private EmisorService emisorService;
    @EJB
    private ComprobanteService comprobanteService;
    @EJB
    private MailService mailService;

    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    private Emisor emisor = null;

    @Override
    public void enviarRecibidosOfflineSRI(Comprobante comprobateActual) throws GenericException {
        emisor = emisorService.getEmisor(comprobateActual.getOficina());
        RespuestaComprobante respuesta = enviarClaveDeAcceso(comprobateActual.getClaveacceso());
        if (respuesta != null) {
            // SRI recivio clave de acceso
            List<Autorizacion> listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
            StringBuilder mensajesAutorizacion = new StringBuilder();
            for (Autorizacion autorizacion : listaAutorizaciones) {
                // SRI recibio el comprobante
                List<Mensaje> mensajes = autorizacion.getMensajes().getMensaje();
                for (Mensaje mensaje : mensajes) {
                    if (mensajesAutorizacion.toString().isEmpty() == false) {
                        mensajesAutorizacion.append(" \n");
                    }
                    // Si tiene informacion adicional el mensaje
                    if (mensaje.getInformacionAdicional() != null) {
                        mensajesAutorizacion.append(mensaje.getTipo())
                                .append(".").append(mensaje.getIdentificador()).append(" ").append(mensaje.getMensaje()).append(": ")
                                .append(mensaje.getInformacionAdicional());
                    } else {
                        mensajesAutorizacion.append(mensaje.getTipo()).append(".").append(mensaje.getIdentificador())
                                .append(" ").append(mensaje.getMensaje());
                    }
                }
                try {
                    // Actualiza estado de respuesta
                    System.out.println("... " + autorizacion.getEstado() + "  " + mensajesAutorizacion.toString());
                    comprobateActual.setCodigoestado(EstadoComprobanteEnum.getCodigo(autorizacion.getEstado()));
                    if (autorizacion.getEstado().equals(EstadoComprobanteEnum.AUTORIZADO.getDescripcion())) {
                        comprobateActual.setFechaautoriza(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                        comprobateActual.setNumAutorizacion(autorizacion.getNumeroAutorizacion());
                        StringBuilder stb_xml = new StringBuilder();
                        stb_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                                .append("<autorizacion>\n")
                                .append("<estado>").append(autorizacion.getEstado()).append("</estado>\n")
                                .append("<numeroAutorizacion>").append(autorizacion.getNumeroAutorizacion()).append("</numeroAutorizacion>\n")
                                .append("<fechaAutorizacion>").append(autorizacion.getFechaAutorizacion()).append("</fechaAutorizacion>\n")
                                .append("<ambiente>").append(TipoAmbienteEnum.getDescripcion(emisorService.getEmisor(comprobateActual.getOficina()).getAmbiente().toString())).append("</ambiente>\n")
                                .append("<comprobante><![CDATA[").append(autorizacion.getComprobante()).append("]]></comprobante>\n")
                                .append("</autorizacion>");
                        comprobanteService.actualizarAutorizacionComprobante(stb_xml.toString(), comprobateActual, mensajesAutorizacion.toString());
                        //No envia las guias por correo
                        if (!comprobateActual.getCoddoc().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
                            mailService.agregarCorreo(comprobateActual, comprobateActual.getCorreo());
                        }
                    } else {
                        comprobanteService.actualizarAutorizacionComprobante(autorizacion.getComprobante(), comprobateActual, mensajesAutorizacion.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            mailService.enviarTodos();
        }
    }

    @Override
    public RespuestaComprobante enviarClaveDeAcceso(String claveDeAcceso) throws GenericException {
        RespuestaComprobante response = null;
        try {
            utilitario.instalarCertificados();
            int TIME_OUT = emisor.getTiempomaxespera() == null ? 30 : emisor.getTiempomaxespera();
            URL url = new URL(emisor.getWsdlautirizacion());
            QName AUTORIZACIONCOMPROBANTESSERVICE_QNAME = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService");
            AutorizacionComprobantesOfflineService service = new AutorizacionComprobantesOfflineService(url, AUTORIZACIONCOMPROBANTESSERVICE_QNAME);
            AutorizacionComprobantesOffline port = service.getAutorizacionComprobantesOfflinePort();
            Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
            requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, TIME_OUT * 1000);
            requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, TIME_OUT * 1000);
            response = port.autorizacionComprobante(claveDeAcceso);
        } catch (MalformedURLException e) {
            throw new GenericException(e);
        }
        return response;
    }

}
