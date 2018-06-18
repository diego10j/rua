/*
 *********************************************************************
 Objetivo: Web Service Offline para que consuma el robot que envia al 
 SRI los comprobantes 
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package webservice.ceo;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.service.ArchivoService;
import dj.comprobantes.offline.service.CPanelService;
import dj.comprobantes.offline.service.ComprobanteService;
import dj.comprobantes.offline.service.ConsultasService;
import dj.comprobantes.offline.service.EmisorService;
import dj.comprobantes.offline.util.Respuesta;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @author diego.jacome
 */
@WebService(serviceName = "ServicioOffline")
public class ServicioOffline {

    @EJB
    private EmisorService emisorService;
    @EJB
    private ComprobanteService comprobanteService;
    @EJB
    private ArchivoService archivoService;
    @EJB
    private ConsultasService consultasService;

    @EJB
    private CPanelService cPanelService;


    /**
     * Envia los comprobantes en estado PENDIENTE al WS de Recepcion del SRI
     *
     * @return
     */
    @WebMethod(operationName = "EnviarRecepcionSRI")
    @WebResult(name = "Respuesta")
    public Respuesta enviarRecepcionSRI() {
        Respuesta respuesta = new Respuesta();
        try {
            comprobanteService.enviarRecepcionSRI();
        } catch (GenericException e) {
            respuesta.setError(true);
            respuesta.setCodigoError(e.getCodigoError());
            respuesta.setMensaje(e.getMessage());
        }
        return respuesta;
    }

    /**
     * Envia los comprobantes en estado RECIBIDA al WS de Autorización del SRI,
     * envia al correo electronico de los clientes el pdf y xml RIDE
     *
     * @return
     */
    @WebMethod(operationName = "EnviarAutorizacionSRI")
    @WebResult(name = "Respuesta")
    public Respuesta enviarAutorizacionSRI() {
        Respuesta respuesta = new Respuesta();
        try {
            comprobanteService.enviarAutorizacionSRI();
        } catch (GenericException e) {
            respuesta.setError(true);
            respuesta.setCodigoError(e.getCodigoError());
            respuesta.setMensaje(e.getMessage());
        }
        return respuesta;
    }

    /**
     * Sube comprobantes autorizados a la nube
     *
     * @return
     */
    @WebMethod(operationName = "SubirNube")
    @WebResult(name = "Respuesta")
    public Respuesta subirNube() {
        Respuesta respuesta = new Respuesta();
        try {
            cPanelService.subirComprobantesPendientes();
        } catch (GenericException e) {
            respuesta.setError(true);
            respuesta.setCodigoError(e.getCodigoError());
            respuesta.setMensaje(e.getMessage());
        }
        return respuesta;
    }

    /**
     * Retorna el archivo pdf de un comprobante
     *
     * @param claveAcceso
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "GetPdf")
    @WebResult(name = "Pdf")
    public byte[] getPDF(@WebParam(name = "ClaveAcceso") String claveAcceso) throws GenericException {
        return archivoService.getPdf(comprobanteService.getComprobantePorClaveAcceso(claveAcceso));
    }

    /**
     * Retorna el archivo xml de un comprobante
     *
     * @param claveAcceso
     * @para3m claveAcceso
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "GetXml")
    @WebResult(name = "Xml")
    public byte[] getXML(@WebParam(name = "ClaveAcceso") String claveAcceso) throws GenericException {
        return archivoService.getXml(comprobanteService.getComprobantePorClaveAcceso(claveAcceso));
    }

    /**
     * Retorna las facturas autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "GetFacturasAutorizadas")
    @WebResult(name = "Facturas")
    public List<Comprobante> getFacturasAutorizadas(@WebParam(name = "Identificacion") String identificacion) throws GenericException {
        return consultasService.getFacturasAutorizadas(identificacion);
    }

    /**
     * Retorna las notas de crédito autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "GetNotCreditoAutorizadas")
    @WebResult(name = "NotasCredito")
    public List<Comprobante> getNotCreditoAutorizadas(@WebParam(name = "Identificacion") String identificacion) throws GenericException {
        return consultasService.getNotCreditoAutorizadas(identificacion);
    }

    /**
     * Retorna las notas de débito autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "GetNotDebitoAutorizadas")
    @WebResult(name = "NotasDebito")
    public List<Comprobante> getNotDebitoAutorizadas(@WebParam(name = "Identificacion") String identificacion) throws GenericException {
        return consultasService.getNotDebitoAutorizadas(identificacion);
    }

    /**
     * Retorna las guias de remisión autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "GetGuiasAutorizadas")
    @WebResult(name = "Guias")
    public List<Comprobante> getGuiasAutorizadas(@WebParam(name = "Identificacion") String identificacion) throws GenericException {
        return consultasService.getFacturasAutorizadas(identificacion);
    }

    /**
     * Retorna los comprobantes de retención autorizadas de un cliente
     *
     * @param identificacion
     * @return
     * @throws GenericException
     */
    @WebMethod(operationName = "GetRetencionesAutorizadas")
    @WebResult(name = "Retenciones")
    public List<Comprobante> getRetencionesAutorizadas(@WebParam(name = "Identificacion") String identificacion) throws GenericException {
        return consultasService.getFacturasAutorizadas(identificacion);
    }
}
