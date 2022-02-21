/*
 *********************************************************************
 Objetivo: Servicio que implementa interface ComprobanteService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dao.ComprobanteDAO;
import dj.comprobantes.offline.dao.XmlComprobanteDAO;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.dto.XmlComprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ComprobanteServiceImp implements ComprobanteService {

    @EJB
    private ComprobanteDAO comprobanteDAO;
    @EJB
    private XmlComprobanteDAO sriComprobanteDAO;
    @EJB
    private FacturaService facturaService;
    @EJB
    private EmisorService emisorService;
    @EJB
    private RecepcionService recepcionService;
    @EJB
    private AutorizacionService autorizacionService;
    @EJB
    private NotaCreditoService notaCreditoService;
    @EJB
    private RetencionService retencionService;
    @EJB
    private GuiaRemisionService guiaRemisionService;
    @EJB
    private LiquidacionCompraService liquidacionCompraService;
    @EJB
    private CPanelService cPanelService;

    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public void enviarRecepcionSRI() throws GenericException {
        try {
            //Recupera Comprobantes en estado Pendiente
            List<Comprobante> lisCompPendientes = comprobanteDAO.getComprobantesPorEstado(EstadoComprobanteEnum.PENDIENTE);
            for (Comprobante comprobanteActual : lisCompPendientes) {
                if (comprobanteActual.getClaveacceso() == null || comprobanteActual.getClaveacceso().isEmpty()) {
                    comprobanteActual.setClaveacceso(getClaveAcceso(comprobanteActual));
                }
                String xml = "";
                //genera xml de la factura 
                if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo())) {
                    xml = facturaService.getXmlFactura(comprobanteActual);
                } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo())) {
                    xml = notaCreditoService.getXmlNotaCredito(comprobanteActual);
                } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo())) {
                    xml = retencionService.getXmlRetencion(comprobanteActual);
                } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
                    xml = guiaRemisionService.getXmlGuiaRemision(comprobanteActual);
                } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS.getCodigo())) {
                    xml = liquidacionCompraService.getXmlLiquidacionCompra(comprobanteActual);
                }
                xml = utilitario.reemplazarCaracteresEspeciales(xml);
                try {
                    recepcionService.enviarRecepcionOfflineSRI(comprobanteActual, xml);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            throw new GenericException(e);
        }
    }

    @Override
    public void enviarAutorizacionSRI() throws GenericException {
        try {
            //Recupera Comprobantes en estado Pendiente
            List<Comprobante> lisCompPendientes = comprobanteDAO.getComprobantesPorEstado(EstadoComprobanteEnum.RECIBIDA);
            for (Comprobante comprobanteActual : lisCompPendientes) {
                try {
                    autorizacionService.enviarRecibidosOfflineSRI(comprobanteActual);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            throw new GenericException(e);
        }
    }

    @Override
    public String getClaveAcceso(Comprobante comprobante) throws GenericException {
        String fechaEmision = utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd/MM/yyyy");
        String tipoComprobante = comprobante.getCoddoc();
        Emisor emisor = emisorService.getEmisor(comprobante.getOficina());
        String ruc = emisor.getRuc();
        String ambiente = emisor.getAmbiente().toString();
        String serie = comprobante.getEstab() + comprobante.getPtoemi();
        String numeroComprobante = comprobante.getSecuencial();
        String tipoEmision = comprobante.getTipoemision();
        String hora = utilitario.getFormatoFecha(new Date(), "HH:mm");
        if (ruc != null && ruc.length() < 13) {
            ruc = String.format("%013d", new Object[]{Integer.parseInt(ruc)});
        }
        if (numeroComprobante != null && numeroComprobante.length() < 9) {
            numeroComprobante = String.format("%09d", new Object[]{Integer.parseInt(numeroComprobante)});
        }
        int verificador = 0;
        fechaEmision = fechaEmision.replaceAll("/", "");
        hora = hora.replaceAll(":", "");
        String codigoNumerico = fechaEmision.substring(4) + hora;
        StringBuilder clave = new StringBuilder(fechaEmision);
        clave.append(tipoComprobante);
        clave.append(ruc);
        clave.append(ambiente);
        clave.append(serie);
        clave.append(numeroComprobante);
        clave.append(codigoNumerico);
        clave.append(tipoEmision);
        verificador = getVerifierModule11(clave.toString());
        clave.append(Integer.valueOf(verificador));
        if (clave.toString().length() != 49) {
            throw new GenericException("ERROR. La cleve de acceso no tiene longitud 49");
        }
        return clave.toString();
    }

    @Override
    public void actualizarRecepcionComprobante(String xmlFirmado, Comprobante comprobante, String mensajeRespuesta) throws GenericException {
        // Actualiza comprobante            
        comprobanteDAO.actualizar(comprobante);
        XmlComprobante sriComp = sriComprobanteDAO.getSriComprobanteActual(comprobante);
        sriComp = sriComp == null ? new XmlComprobante() : sriComp;
        sriComp.setCodigocomprobante(comprobante);
        sriComp.setXmlcomprobante(xmlFirmado);
        sriComp.setFecha(new Date());
        sriComp.setMensajerecepcion(mensajeRespuesta);
        sriComp.setCodigoestado(comprobante.getCodigoestado());
        // Guarda o actualiza el SRI comporbnate
        sriComprobanteDAO.guardar(sriComp);

    }

    @Override
    public void actualizarAutorizacionComprobante(String xmlFirmado, Comprobante comprobante, String mensajeRespuesta) throws GenericException {
        // Actualiza comprobante            
        comprobanteDAO.actualizar(comprobante);
        XmlComprobante sriComp = sriComprobanteDAO.getSriComprobanteActual(comprobante);
        sriComp = sriComp == null ? new XmlComprobante() : sriComp;
        sriComp.setCodigocomprobante(comprobante);
        sriComp.setXmlcomprobante(xmlFirmado);
        sriComp.setFecha(new Date());
        sriComp.setMensajeautorizacion(mensajeRespuesta);
        sriComp.setCodigoestado(comprobante.getCodigoestado());
        // Guarda o actualiza el SRI comporbnate
        sriComprobanteDAO.guardar(sriComp);
    }

    /**
     * Obtiene el digito verificador de la clave de acceso en modulo 11 para la
     * clave de Acceso
     *
     * @param cadena
     * @return
     */
    private int getVerifierModule11(String cadena) {
        int baseMultiplicador = 7;
        int aux[] = new int[cadena.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador = 0;
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt((new StringBuilder()).append("").append(cadena.charAt(i)).toString());
            aux[i] = aux[i] * multiplicador;
            if (++multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
            total += aux[i];
        }
        // --Ya tenemos el total
        if (total == 0 || total == 1) {
            verificador = 0;
        } else {
            verificador = (11 - (total % 11)) != 11 ? 11 - total % 11 : 0;
        }
        if (verificador == 10) {
            verificador = 1;
        }
        return verificador;
    }

    @Override
    public Comprobante getComprobantePorClaveAcceso(String claveAcceso) throws GenericException {
        return comprobanteDAO.getComprobantePorClaveAcceso(claveAcceso);
    }

    @Override
    public Comprobante getComprobantePorId(Long ide_srcom) throws GenericException {
        return comprobanteDAO.getComprobantePorId(ide_srcom);
    }

    @Override
    public Comprobante getComprobanteGuia(Comprobante comprobante) throws GenericException {
        return comprobanteDAO.getComprobanteGuia(comprobante.getCodigocomprobante());
    }

    @Override
    public void enviarComprobante(String claveAcceso) throws GenericException {

        Comprobante comprobanteActual = getComprobantePorClaveAcceso(claveAcceso);
        Comprobante comprobanteGuia = null;
        boolean envioPendiente = false;
        if (comprobanteActual == null) {
            throw new GenericException("ERROR. No existe el comprobante " + claveAcceso);
        }
        if (comprobanteActual.getCodigoestado() == EstadoComprobanteEnum.PENDIENTE.getCodigo()) {
            String xml = "";
            //genera xml de la factura 
            if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo())) {
                xml = facturaService.getXmlFactura(comprobanteActual);
            } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo())) {
                xml = notaCreditoService.getXmlNotaCredito(comprobanteActual);
            } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo())) {
                xml = retencionService.getXmlRetencion(comprobanteActual);
            } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
                xml = guiaRemisionService.getXmlGuiaRemision(comprobanteActual);
            } else if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS.getCodigo())) {
                xml = liquidacionCompraService.getXmlLiquidacionCompra(comprobanteActual);
            }
            xml = utilitario.reemplazarCaracteresEspeciales(xml);
            recepcionService.enviarRecepcionOfflineSRI(comprobanteActual, xml);
            //verifica que se encuentre en estado RECIBIDA
            comprobanteActual = getComprobantePorId(comprobanteActual.getCodigocomprobante());
            if (comprobanteActual.getCodigoestado() != EstadoComprobanteEnum.RECIBIDA.getCodigo()) {
                throw new GenericException("ERROR. El Comprobante " + claveAcceso + " no pudo ser enviado al SRI");
            }
            envioPendiente = true;
        }

        //Si es factura envia la guia de remision
        if (comprobanteActual.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo())) {
            comprobanteGuia = getComprobanteGuia(comprobanteActual);
            if (comprobanteGuia != null && comprobanteGuia.getCodigoestado() == EstadoComprobanteEnum.PENDIENTE.getCodigo()) {
                //Envia la guia
                String xmlGuia = guiaRemisionService.getXmlGuiaRemision(comprobanteGuia);
                xmlGuia = utilitario.reemplazarCaracteresEspeciales(xmlGuia);
                recepcionService.enviarRecepcionOfflineSRI(comprobanteGuia, xmlGuia);
                comprobanteGuia = getComprobantePorId(comprobanteGuia.getCodigocomprobante());
            } else {
                if (envioPendiente) {
                    try {
                        //Esperamos 6 segundos por recomendacion del SRI
                        Thread.sleep(6 * 1000);
                    } catch (Exception e) {
                    }
                }

            }
        }

        if (comprobanteActual.getCodigoestado() == EstadoComprobanteEnum.RECIBIDA.getCodigo()) {
            autorizacionService.enviarRecibidosOfflineSRI(comprobanteActual);
            if (comprobanteGuia != null) {
                //Envia Guia
                if (comprobanteGuia.getCodigoestado() == EstadoComprobanteEnum.RECIBIDA.getCodigo()) {
                    comprobanteGuia.getCliente().setNombreCliente(comprobanteActual.getCliente().getNombreCliente());
                    comprobanteGuia.getCliente().setIdentificacion(comprobanteActual.getCliente().getIdentificacion());
                    autorizacionService.enviarRecibidosOfflineSRI(comprobanteGuia);
                }
            }
            //verifica si se autorizo
            comprobanteActual = getComprobantePorId(comprobanteActual.getCodigocomprobante());
            if (comprobanteActual.getCodigoestado() != EstadoComprobanteEnum.AUTORIZADO.getCodigo()) {
                throw new GenericException("ERROR. El Comprobante " + claveAcceso + " no pudo ser Autorizado por el SRI.");
            }
            try {
                cPanelService.guardarComprobanteNube(comprobanteActual);
                if (comprobanteGuia != null) {
                    comprobanteGuia.getCliente().setNombreCliente(comprobanteActual.getCliente().getNombreCliente());
                    comprobanteGuia.getCliente().setIdentificacion(comprobanteActual.getCliente().getIdentificacion());
                    cPanelService.guardarComprobanteNube(comprobanteGuia);
                }
            } catch (Exception e) {
                System.out.println("ERROR. al subir comprobante a la Nube :" + e.getMessage());
            }

        }
    }

}
