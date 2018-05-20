/*
 *********************************************************************
 Objetivo: Servicio que implementa interface ConsultasService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 15-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dao.ComprobanteDAO;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author djacome
 */
@Stateless
public class ConsultasServiceImp implements ConsultasService {

    @EJB
    private ComprobanteDAO comprobanteDAO;

    @Override
    public List<Comprobante> getFacturasAutorizadas(String identificacion) throws GenericException {
        List<Comprobante> lisResultado = comprobanteDAO.getComprobantesCliente(identificacion, TipoComprobanteEnum.FACTURA);
        //Recorre la lista y pone null en los Detalles para que no retornen en la trama xml
        for (Comprobante comActual : lisResultado) {
            comActual.setDetalle(null);
            comActual.setCodigofirma(null);
        }
        return lisResultado;
    }

    @Override
    public List<Comprobante> getNotCreditoAutorizadas(String identificacion) throws GenericException {
        List<Comprobante> lisResultado = comprobanteDAO.getComprobantesCliente(identificacion, TipoComprobanteEnum.NOTA_DE_CREDITO);
        //Recorre la lista y pone null en los Detalles para que no retornen en la trama xml
        for (Comprobante comActual : lisResultado) {
            comActual.setDetalle(null);
            comActual.setCodigofirma(null);
        }
        return lisResultado;
    }

    @Override
    public List<Comprobante> getNotDebitoAutorizadas(String identificacion) throws GenericException {
        List<Comprobante> lisResultado = comprobanteDAO.getComprobantesCliente(identificacion, TipoComprobanteEnum.NOTA_DE_DEBITO);
        //Recorre la lista y pone null en los Detalles para que no retornen en la trama xml
        for (Comprobante comActual : lisResultado) {
            comActual.setDetalle(null);
            comActual.setCodigofirma(null);
        }
        return lisResultado;
    }

    @Override
    public List<Comprobante> getGuiasAutorizadas(String identificacion) throws GenericException {
        List<Comprobante> lisResultado = comprobanteDAO.getComprobantesCliente(identificacion, TipoComprobanteEnum.GUIA_DE_REMISION);
        //Recorre la lista y pone null en los Detalles para que no retornen en la trama xml
        for (Comprobante comActual : lisResultado) {
            comActual.setDetalle(null);
            comActual.setCodigofirma(null);
        }
        return lisResultado;
    }

    @Override
    public List<Comprobante> getRetencionesAutorizadas(String identificacion) throws GenericException {
        List<Comprobante> lisResultado = comprobanteDAO.getComprobantesCliente(identificacion, TipoComprobanteEnum.COMPROBANTE_DE_RETENCION);
        //Recorre la lista y pone null en los Detalles para que no retornen en la trama xml
        for (Comprobante comActual : lisResultado) {
            comActual.setDetalle(null);
            comActual.setCodigofirma(null);
        }
        return lisResultado;
    }
}
