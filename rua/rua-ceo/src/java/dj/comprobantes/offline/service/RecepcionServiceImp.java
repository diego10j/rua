/*
 *********************************************************************
 Objetivo: Servicio que implementa interface RecepcionService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import com.sun.xml.ws.client.BindingProviderProperties;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.util.UtilitarioCeo;
import ec.gob.sri.comprobantes.ws.Mensaje;
import ec.gob.sri.comprobantes.ws.RecepcionComprobantesOffline;
import ec.gob.sri.comprobantes.ws.RecepcionComprobantesOfflineService;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class RecepcionServiceImp implements RecepcionService {

    @EJB
    private ComprobanteService comprobanteService;
    @EJB
    private EmisorService emisorService;
    @EJB
    private FirmarDocumentoService firmarService;
    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    private Emisor emisor = null;

    @Override
    public void enviarRecepcionOfflineSRI(Comprobante comprobateActual, String xml) throws GenericException {
        try {

            // Crea Documento
            Document documento = getDocument(xml);
            // Firmar Documento XML
            Document documentoFirmado = firmarService.getDocumentoFirmado(documento, comprobateActual.getOficina());
            // Envia a servicio web de Recepcion del SRI
            emisor = emisorService.getEmisor(comprobateActual.getOficina());
            RespuestaSolicitud respuesta = enviarComprobanteSRI(documentoFirmado);
            // Actualiza Estado y mensaje de respuesta
            StringBuilder mensajesDevuelta = new StringBuilder();
            RespuestaSolicitud.Comprobantes comprobantes = respuesta.getComprobantes();
            if (comprobantes != null) {
                List<ec.gob.sri.comprobantes.ws.Comprobante> listaComprobantes = comprobantes.getComprobante();
                if (!listaComprobantes.isEmpty()) {
                    for (ec.gob.sri.comprobantes.ws.Comprobante comprobanteActual1 : listaComprobantes) {
                        List<Mensaje> mensajes = comprobanteActual1.getMensajes().getMensaje();
                        for (Mensaje mensaje : mensajes) {
                            if (mensajesDevuelta.toString().isEmpty() == false) {
                                mensajesDevuelta.append(" \n");
                            }
                            mensajesDevuelta.append(mensaje.getTipo()).append(".").append(mensaje.getIdentificador())
                                    .append(" ").append(mensaje.getMensaje()).append(": ").append(mensaje.getInformacionAdicional());
                        }
                    }
                }
            }
            System.out.println("... " + respuesta.getEstado() + "  " + mensajesDevuelta.toString());
            if (mensajesDevuelta.toString().contains("ERROR.70 CLAVE DE ACCESO EN PROCESAMIENTO")) {
                comprobateActual.setCodigoestado(EstadoComprobanteEnum.PENDIENTE.getCodigo());
            } else if (mensajesDevuelta.toString().contains("ERROR.43 CLAVE ACCESO REGISTRADA: null")) {
                comprobateActual.setCodigoestado(EstadoComprobanteEnum.RECIBIDA.getCodigo());
            } else {
                comprobateActual.setCodigoestado(EstadoComprobanteEnum.getCodigo(respuesta.getEstado()));
            }
            comprobanteService.actualizarRecepcionComprobante(getStringDeDocument(documentoFirmado), comprobateActual, mensajesDevuelta.toString());//                   

        } catch (Exception e) {
            throw new GenericException("Error en el metodo enviarRecepcionOfflineSRI : " + e.getMessage());
        }
    }

    /**
     * Convierte A Documento Cadena XML
     *
     * @param cadenaXML
     * @return
     */
    private Document getDocument(String cadenaXML) {
        Document documento = null;
        try {
            InputStream is = new ByteArrayInputStream(cadenaXML.getBytes("UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            is.close();
            br.close();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            documento = dbf.newDocumentBuilder().parse(is);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            // logger.error("Error en el método  getDocument : " + e.getMessage());
        }
        return documento;
    }

    /**
     * Envia a webService de solicitud SRI el documento XML en byte[]
     *
     * @param xmlFile
     */
    private RespuestaSolicitud enviarComprobanteSRI(Document documentoFirmado) throws GenericException {
        RespuestaSolicitud response = null;
        try {
            byte[] xmlFile = getByteDeDocument(documentoFirmado);
            utilitario.instalarCertificados();
            int TIME_OUT = emisor.getTiempomaxespera() == null ? 30 : emisor.getTiempomaxespera();
            URL url = new URL(emisor.getWsdlrecepcion());
            QName RECEPCIONCOMPROBANTESOFFLINE_QNAME = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
            RecepcionComprobantesOfflineService service = new RecepcionComprobantesOfflineService(url, RECEPCIONCOMPROBANTESOFFLINE_QNAME);
            RecepcionComprobantesOffline port = service.getRecepcionComprobantesOfflinePort();
            Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
            requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, TIME_OUT * 1000);
            requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, TIME_OUT * 1000);
            response = port.validarComprobante(xmlFile);
        } catch (MalformedURLException e) {
            throw new GenericException("No se puede conectar con el servicio web de Recepción del SRI : " + e.getMessage());
        }
        //   Utilitario.borrarProxy();
        return response;
    }

    /**
     *
     * Convierte Document en Byte[]
     *
     * @param documentoXml
     * @return
     * @throws java.lang.Exception
     */
    private byte[] getByteDeDocument(Document documentoXml) {
        try {
            Source source = new DOMSource(documentoXml);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Result result = new StreamResult(out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            byte[] butesXml = out.toByteArray();
            return butesXml;
        } catch (TransformerException e) {
            // logger.error("Error en el método  getByteDeDocument : " + e.getMessage());
        }
        return null;
    }

    /**
     * Convierte de Documento a String con la opcion de indentado
     *
     * @param documentoXML
     * @param indentado
     * @return
     */
    private String getStringDeDocument(Document documentoXML) {
        String texto = null;
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // tf.setOutputProperty(OutputKeys.INDENT, "yes");
            try (Writer out = new StringWriter()) {
                tf.transform(new DOMSource(documentoXML), new StreamResult(out));
                texto = out.toString();
                out.flush();
            }
        } catch (IllegalArgumentException | IOException | TransformerException e) {
            // logger.error("Error en el método  getStringDeDocument : " + e.getMessage());
        }
        return texto;
    }

}
