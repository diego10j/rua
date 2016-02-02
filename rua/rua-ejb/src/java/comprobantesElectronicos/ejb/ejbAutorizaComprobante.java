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
import comprobantesElectronicos.entidades.Sricomprobante;

import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantes;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantesService;
import ec.gob.sri.comprobantes.ws.aut.Mensaje;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.xml.namespace.QName;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ejbAutorizaComprobante {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private SriComprobanteDAOLocal sriComprobanteDAO;
    @EJB
    private EmisorDAOLocal emisorDAO;
    @EJB
    private ComprobanteDAOLocal comprobanteDAO;
    @EJB
    private EstadoComprobanteDAOLocal estadoComprobanteDAO;

    private final Utilitario utilitario = new Utilitario();
    /**
     * enlaza webservice de SRI solicitud
     */
    private AutorizacionComprobantesService service;

    /**
     * *
     * permite solicitar la autorizacion de servicios del SRI , enviar, guardar
     * el documento xml
     *
     * @param claveDeAcceso
     * @return
     */
    public String enviarAutorizacionDocumento(String claveDeAcceso) {
        Emisor emisor = emisorDAO.getEmisor();
        if (emisor == null) {
            return "No existe emisor creado en la base de datos";
        }

        int TIME_OUT = 5; //valor por defecto
        if (emisor.getTiempomaxespera() != null) {
            TIME_OUT = emisor.getTiempomaxespera();
        }
        //Validamos que exista un comprobante con la clave de acceso 
        Comprobante comprobante = comprobanteDAO.getComprobanteporClaveAcceso(claveDeAcceso);
        if (comprobante == null) {
            return "No existe comprobante con clave de acceso: " + claveDeAcceso + " en la base de datos";
        }
        comprobante.setCodigoemisor(emisor);
        //Valida que el comprobante no este Autorizado
        if (comprobante.getCodigoestado().equals(estadoComprobanteDAO.getEstadoAutorizado())) {
            return "El comprobante con clave de acceso : " + claveDeAcceso + " ya está Autorizado";
        }

        StringBuilder mensajesAutorizacion = new StringBuilder();
        String urlEnvioroment = emisor.getWsdlautirizacion();
        RespuestaComprobante respuesta = null;
        try {
            instalarCertificados();
            service = new AutorizacionComprobantesService(new URL(urlEnvioroment),
                    new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService"));
            respuesta = llamadaWSAutorizacionInd(claveDeAcceso, TIME_OUT);
        } catch (Exception e) {
            return "Servicio de autorización no disponible, " + e.getMessage();
        }
        if (respuesta == null) {
            return ("Servicio de autorización no disponible");
        }
        //Para guardar la resúesta de la autorizacion
        Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobante);
        if (sriComprobante == null) {
            sriComprobante = new Sricomprobante();
            sriComprobante.setCodigocomprobante(comprobante);
        }
        //Valida que la clave de acceso haya sido RECIBIDA
        List<Autorizacion> listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
        if (listaAutorizaciones.isEmpty()) {
            return "No existe la clave de acceso: " + claveDeAcceso;
        }
        for (Autorizacion autorizacion : listaAutorizaciones) {
            String estado = autorizacion.getEstado();
            //System.out.println("****estado**************" + estado + " fecha " + autorizacion.getFechaAutorizacion());
            Estadocomprobante estadoComprobante = estadoComprobanteDAO.getEstadoporNombre(estado);
            //Asigna nuevo estado      
            sriComprobante.setCodigoestado(estadoComprobante);            
            if (estado.equalsIgnoreCase("AUTORIZADO")) {
                //Asigna numero de autorizacion y fecha del SRI                 
                comprobante.setNumAutorizacion(autorizacion.getNumeroAutorizacion());
                comprobante.setFechaautoriza(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
                mensajesAutorizacion.append(estado);
                sriComprobante.setMensajeautorizacion("NUM. AUTORIZACION: " + comprobante.getNumAutorizacion() + "    FECHA: " + utilitario.getFormatoFecha(comprobante.getFechaautoriza(), "dd 'de' MMMM 'del' yyyy"));
                //Guarda el xml del comprobante con la firma y la autorizacion
                sriComprobante.setXmlcomprobante(autorizacion.getComprobante());
            } else {
                List<Mensaje> mensajes = autorizacion.getMensajes().getMensaje();
                if (mensajes.isEmpty()) {
                    return (estado + ", no se pudo autorizar el documento");
                }
                for (Mensaje mensaje : mensajes) {
                    //Si tiene informacion adicional el mensaje
                    if (mensaje.getInformacionAdicional() != null) {
                        mensajesAutorizacion.append(mensaje.getTipo()).append(".").append(mensaje.getIdentificador()).append(" ").append(mensaje.getMensaje()).append(": ").append(mensaje.getInformacionAdicional()).append("   \n");
                    } else {
                        mensajesAutorizacion.append(mensaje.getTipo()).append(".").append(mensaje.getIdentificador()).append(" ").append(mensaje.getMensaje()).append("   \n");
                    }
                }
                //asigna el mensajes de autorizacion
                sriComprobante.setMensajeautorizacion(mensajesAutorizacion.toString());
            }
            //Actualiza el resultado del envio
            if (sriComprobante.getCodigocompsri() != null) {
                //Actualiza
                sriComprobanteDAO.actualizar(sriComprobante);
            } else {
                //Inserta 
                sriComprobanteDAO.crear(sriComprobante);
            }
            //Actualiza el estado del comprobante
            comprobanteDAO.actualizarEstado(comprobante, estadoComprobante);
            break; //Solo lee el ultimo comprobante 
        }
        return mensajesAutorizacion.toString();
    }

    /**
     * *
     * crea e invoca certificados SSL
     *
     * @return
     */
    public static String instalarCertificados() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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
        }};

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

    /**
     * *
     * permite la invocacion del web service de autorizacion
     *
     * @param claveDeAcceso
     * @param TIME_OUT tiempo maximo de espera en segundos
     * @return
     */
    public RespuestaComprobante llamadaWSAutorizacionInd(String claveDeAcceso, int TIME_OUT) {
        RespuestaComprobante response = null;
        try {
            AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
            Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
            requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, TIME_OUT * 1000);
            requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, TIME_OUT * 1000);
            response = port.autorizacionComprobante(claveDeAcceso);
        } catch (Exception e) {
            return response;
        }
        return response;
    }

    /**
     * Tiempo de espera recomendado por el SRI antes de enviar un comprobante
     */
    public void esperarAntesAutorizar() {
        try {
            //espera de tiempo antes del SERVICIO DE AUTORIZACIÓN 5 seg"
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
        }
    }
}
