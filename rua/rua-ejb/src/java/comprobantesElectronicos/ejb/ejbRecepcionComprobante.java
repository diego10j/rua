/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.ejb;

import com.sun.xml.ws.client.BindingProviderProperties;
import comprobantesElectronicos.dao.ComprobanteDAOLocal;
import comprobantesElectronicos.dao.EmisorDAOLocal;
import comprobantesElectronicos.dao.EstadoComprobanteDAOLocal;
import comprobantesElectronicos.dao.SriComprobanteDAOLocal;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Emisor;
import comprobantesElectronicos.entidades.Estadocomprobante;
import comprobantesElectronicos.entidades.Firma;
import comprobantesElectronicos.entidades.Sricomprobante;
import ec.gob.sri.comprobantes.ws.Mensaje;
import ec.gob.sri.comprobantes.ws.RecepcionComprobantes;
import ec.gob.sri.comprobantes.ws.RecepcionComprobantesService;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ejbRecepcionComprobante {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private SriComprobanteDAOLocal sriComprobanteDAO;
    @EJB
    private EmisorDAOLocal emisorDAO;
    @EJB
    private EstadoComprobanteDAOLocal estadoComprobanteDAO;
    @EJB
    private ComprobanteDAOLocal comprobanteDAO;
    @EJB
    private ejbClaveAcceso ejbClaveAcceso;
    @EJB
    private ejbFirmarDocumento ejbFirma;
    private static RecepcionComprobantesService service;

    /**
     * Envia el documento firmado al SRI
     *
     * @param comprobante
     * @param cadenaXML
     * @return retorna mensaje de respuesta del SRI
     */
    public String eviarDocumentoFirmado(Comprobante comprobante, String cadenaXML) {

        //Valida que existan datos del Emisor
        Emisor emisor = emisorDAO.getEmisor();
        if (emisor == null) {
            return "No existe emisor creado en la base de datos";
        }
        //Asignamos el emisor al comprobante
        comprobante.setCodigoemisor(emisor);

        //Busca una firma disponible
        Firma firma = ejbFirma.getFirmaDisponible();
        if (firma == null) {
            return "No tiene firmas o certificados digitales disponibles";
        }
        //Asigna la firma al comprobante
        comprobante.setCodigofirma(firma);

        //Validaciones de la firma
        String mensajeValida = ejbFirma.validaCertificado(firma);
        if (mensajeValida != null) {
            return mensajeValida;
        }

        //Genera la clave de acceso normal
        String claveAcceso = "";
        if (cadenaXML.contains("[CLAVEACCESO]")) {
            //CLAVE NORMAL, EMISION NORMAL 1
            comprobante.setTipoemision(1);
            claveAcceso = ejbClaveAcceso.getClaveAcceso(comprobante);
            //Remplazo la clave de acceso en la estructura XML            
            cadenaXML = cadenaXML.replace("[CLAVEACCESO]", claveAcceso);
            //Asigna la clave de acceso al comprobante
            comprobante.setClaveacceso(claveAcceso);
        } else {
            //Recupera la clave que ya tiene el comprobante 
            claveAcceso = comprobante.getClaveacceso();
        }

        StringBuilder mensajesDevuelta = new StringBuilder();
        //Obtengo el tiempo maximo de espera
        int TIME_OUT = 5; //valor por defecto
        if (emisor.getTiempomaxespera() != null) {
            TIME_OUT = emisor.getTiempomaxespera();
        }
        try {
            //Convierte a Doumento la cadenaXML y lo valida que sea una estructura XML
            Document documento = getDocumento(cadenaXML);
            if (documento == null) {
                return "La cadena enviada no cumple con una estructura XML";
            }
            //Firma el Documento
            Document docFirmado = ejbFirma.getDocumentoFirmado(firma, documento);
            if (docFirmado == null) {
                return "No se puede firmar el Documento";
            }
            //Envia documento firmado al SRI
            instalarCertificados();
            URL url = new URL(emisor.getWsdlrecepcion());
            QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService");
            service = new RecepcionComprobantesService(url, qname);
            //RespuestaSolicitud response = null;//POBAR CONTINGENCIA
            RespuestaSolicitud response = enviarComprobante(docFirmado, TIME_OUT);
            Estadocomprobante estadoComprobante;
            //Generamos el intento de recepcion del comprobante
            Sricomprobante sriComprobante = new Sricomprobante();
            sriComprobante.setCodigocomprobante(comprobante);
            sriComprobante.setFecha(new Date());
            sriComprobante.setXmlcomprobante(cadenaXML);
            if (response == null) {//No hay servicio del SRI
                //Si no tiene clave de contingencia genera una
                if (comprobante.getCodigoclave() == null) {
                    //CLAVE CONTINGENCIA, EMISION CONTINGENCIA 2                
                    comprobante.setTipoemision(2);
                    String claveContingencia = ejbClaveAcceso.getClaveContingencia(comprobante);
                    if (claveContingencia.length() != 49) {
                        return claveContingencia;
                    }
                    //Actualiza el tipo de emision 2 en cadena xml
                    sriComprobante.setXmlcomprobante(sriComprobante.getXmlcomprobante().replace("<tipoEmision>1", "<tipoEmision>2"));
                    //Cambia la clave de contingencia en cadena xml
                    sriComprobante.setXmlcomprobante(sriComprobante.getXmlcomprobante().replace(claveAcceso, claveContingencia));
                    comprobante.setClaveacceso(claveContingencia);
                    System.out.println(sriComprobante.getXmlcomprobante());
                    System.out.println("CLAVE DE CONTINGENCIA :" + claveContingencia);
                }
                //Ponemos en estado de contingencia para poder enviarlo despues
                estadoComprobante = estadoComprobanteDAO.getEstadoContingencia();
                comprobanteDAO.actualizarEstado(comprobante, estadoComprobante);
                //Guardo el resultado de invocar al webservice
                sriComprobante.setMensajerecepcion("CONTINGENCIA");
                sriComprobante.setCodigocomprobante(comprobante);
                sriComprobante.setCodigoestado(estadoComprobante);
                sriComprobanteDAO.crear(sriComprobante);
                return "Servicio de Recepción del SRI no disponible";
            } else {
                System.out.println("CLAVE DE ACCESO :" + claveAcceso + "  " + response.getEstado());
                String estado = response.getEstado();
                //Busca el estado del comprobante              
                estadoComprobante = estadoComprobanteDAO.getEstadoporNombre(estado);
                sriComprobante.setCodigoestado(estadoComprobante);
                //Actualiza estado del comprobante   
                comprobanteDAO.actualizarEstado(comprobante, estadoComprobante);
                //Lee los mensajes en caso de que le comprobante este en estado DEVUELTA
                if (estado.equalsIgnoreCase("DEVUELTA")) {
                    RespuestaSolicitud.Comprobantes comprobantes = response.getComprobantes();                    
                    if (comprobantes != null) {
                        List<ec.gob.sri.comprobantes.ws.Comprobante> listaComprobantes = comprobantes.getComprobante();                        
                        if (!listaComprobantes.isEmpty()) {
                            for (ec.gob.sri.comprobantes.ws.Comprobante comprobanteActual : listaComprobantes) {                                
                                List<Mensaje> mensajes = comprobanteActual.getMensajes().getMensaje();                                
                                for (Mensaje mensaje : mensajes) {                                    
                                    mensajesDevuelta.append(mensaje.getTipo()).append(".").append(mensaje.getIdentificador()).append(" ").append(mensaje.getMensaje()).append(": ").append(mensaje.getInformacionAdicional()).append(" \n");
                                }
                            }
                        }
                    } else {
                        System.out.println("xxxx   ");
                        return "No existen comprobantes en el documento XML";
                    }
                    //Guardo el resultado de invocar al webservice
                    sriComprobante.setMensajerecepcion(mensajesDevuelta.toString());
                    sriComprobanteDAO.crear(sriComprobante);
                } else {
                    //Guardo el resultado de invocar al webservice
                    sriComprobante.setMensajerecepcion(estado);
                    sriComprobanteDAO.crear(sriComprobante);
                    return estado;
                }
            }
        } catch (Exception e) {
            return "No se envió el comprobante, " + e.getMessage();
        }
        return mensajesDevuelta.toString();
    }

    /*
     * permite hacer una istalacion a certificados  x509 para webservice
     */
    public static String instalarCertificados() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            return e.getMessage();
        }
        return null;
    }

    /*
     *envia a webService de solicitud SRI el documento XML en byte[]
     */
    public RespuestaSolicitud enviarComprobante(Document xmlFile, int TIME_OUT) {
        RespuestaSolicitud response = null;
        try {
            RecepcionComprobantes port = service.getRecepcionComprobantesPort();
            Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
            requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, TIME_OUT * 1000);
            requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, TIME_OUT * 1000);
            byte[] archivoBytes = obtenerBytesDeDocument(xmlFile);
            if (archivoBytes != null) {
                response = port.validarComprobante(archivoBytes);//invoca servicio SRI
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public byte[] obtenerBytesDeDocument(Document documentoXml) throws Exception {
        Source source = new DOMSource(documentoXml);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(out);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.transform(source, result);
        byte[] butesXml = out.toByteArray();
        return butesXml;
    }

    /**
     * Convierte de Documento a String con la opcion de indentado
     *
     * @param documentoXML
     * @param indentado
     * @return
     */
    public String getXmlDocumento(Document documentoXML, boolean indentado) {
        String texto = null;
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            if (indentado) {
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
            }
            try (Writer out = new StringWriter()) {
                tf.transform(new DOMSource(documentoXML), new StreamResult(out));
                texto = out.toString();
                out.flush();
            }
        } catch (IllegalArgumentException | TransformerException | IOException e) {
        }
        return texto;
    }

    /**
     * Convierte una estructura XML a Document
     *
     * @param cadenaXML
     * @return
     */
    public Document getDocumento(String cadenaXML) {
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
            e.printStackTrace();
        }
        return documento;
    }

    public Document getDocumentoconFirma(String cadenaXML) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(cadenaXML)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
        }
        return null;
    }
}
