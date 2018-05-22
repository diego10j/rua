/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.DetalleComprobante;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author djacome
 */
@Stateless
public class GuiaRemisionServiceImp implements GuiaRemisionService {

    @EJB
    private EmisorService emisorService;
    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public String getXmlGuiaRemision(Comprobante comprobante) throws GenericException {
        StringBuilder str_xml = new StringBuilder();
        if (comprobante != null) {
            Emisor emisor = emisorService.getEmisor(comprobante.getOficina());
            str_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                    .append("     <guiaRemision id=\"comprobante\" version=\"1.0.0\"> \n")
                    .append("		<infoTributaria> \n")
                    .append("			<ambiente>").append(emisor.getAmbiente()).append("</ambiente> \n")
                    .append("			<tipoEmision>").append(comprobante.getTipoemision()).append("</tipoEmision> \n")
                    .append("			<razonSocial>").append(emisor.getRazonsocial()).append("</razonSocial> \n")
                    .append("			<nombreComercial>").append(emisor.getNombrecomercial()).append("</nombreComercial> \n")
                    .append("			<ruc>").append(emisor.getRuc()).append("</ruc> \n")
                    .append("			<claveAcceso>").append(comprobante.getClaveacceso()).append("</claveAcceso> \n")
                    .append("			<codDoc>").append(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo()).append("</codDoc> \n")
                    .append("			<estab>").append(comprobante.getEstab()).append("</estab> \n")
                    .append("			<ptoEmi>").append(comprobante.getPtoemi()).append("</ptoEmi> \n")
                    .append("			<secuencial>").append(comprobante.getSecuencial()).append("</secuencial> \n")
                    .append("			<dirMatriz>").append(emisor.getDirmatriz()).append("</dirMatriz> \n")
                    .append("		</infoTributaria> \n")
                    .append("		<infoGuiaRemision> \n")
                    .append("			<dirEstablecimiento>").append(emisor.getDirsucursal()).append("</dirEstablecimiento> \n")
                    .append("			<dirPartida>").append(comprobante.getDirPartida()).append("</dirPartida> \n")
                    .append("			<razonSocialTransportista>").append(comprobante.getCliente().getNombreCliente()).append("</razonSocialTransportista> \n")
                    .append("			<tipoIdentificacionTransportista>").append(comprobante.getCliente().getTipoIdentificacion()).append("</tipoIdentificacionTransportista> \n")
                    .append("			<rucTransportista>").append(comprobante.getCliente().getIdentificacion().trim()).append("</rucTransportista> \n")
                    .append("			<obligadoContabilidad>").append(emisor.getObligadocontabilidad()).append("</obligadoContabilidad> \n")
                    .append("			<contribuyenteEspecial>").append(emisor.getContribuyenteespecial()).append("</contribuyenteEspecial> \n")
                    .append("			<fechaIniTransporte>").append(utilitario.getFormatoFecha(comprobante.getFechaIniTransporte(), "dd/MM/yyyy")).append("</fechaIniTransporte> \n")
                    .append("			<fechaFinTransporte>").append(utilitario.getFormatoFecha(comprobante.getFechaFinTransporte(), "dd/MM/yyyy")).append("</fechaFinTransporte> \n")
                    .append("			<placa>").append(comprobante.getPlaca()).append("</placa> \n")
                    .append("		</infoGuiaRemision> \n")
                    .append("		<destinatarios> \n")
                    .append("                   <destinatario> \n")
                    .append("                           <identificacionDestinatario>").append(comprobante.getDestinatario().getIdentificacionDestinatario()).append("</identificacionDestinatario> \n")
                    .append("                           <razonSocialDestinatario>").append(comprobante.getDestinatario().getRazonSocialDestinatario()).append("</razonSocialDestinatario> \n")
                    .append("                           <dirDestinatario>").append(comprobante.getDestinatario().getDirDestinatario()).append("</dirDestinatario> \n")
                    .append("                           <motivoTraslado>").append(comprobante.getDestinatario().getMotivoTraslado()).append("</motivoTraslado> \n")
                    .append("                           <docAduaneroUnico>").append(comprobante.getDestinatario().getDocAduaneroUnico()).append("</docAduaneroUnico> \n")
                    .append("                           <codEstabDestino>").append(comprobante.getDestinatario().getCodEstabDestino()).append("</codEstabDestino> \n")
                    .append("                           <ruta>").append(comprobante.getDestinatario().getRuta()).append("</ruta> \n")
                    .append("                           <codDocSustento>").append(comprobante.getDestinatario().getCodDocSustento()).append("</codDocSustento> \n")
                    .append("                           <numDocSustento>").append(comprobante.getDestinatario().getNumDocSustento()).append("</numDocSustento> \n")
                    .append("                           <numAutDocSustento>").append(comprobante.getDestinatario().getNumAutDocSustento()).append("</numAutDocSustento> \n")
                    .append("                           <fechaEmisionDocSustento>").append(utilitario.getFormatoFecha(comprobante.getDestinatario().getFechaEmisionDocSustento(), "dd/MM/yyyy")).append("</fechaEmisionDocSustento> \n")
                    .append("                           <detalles> \n");
            for (DetalleComprobante detalle : comprobante.getDetalle()) {
                str_xml.append("                                <detalle> \n")
                        .append("                                       <codigoInterno>").append(detalle.getCodigoprincipal()).append("</codigoInterno> \n")
                        .append("                                       <codigoAdicional>").append((detalle.getCodigoauxiliar() == null ? detalle.getCodigoprincipal() : detalle.getCodigoauxiliar())).append("</codigoAdicional> \n")
                        .append("                                       <descripcion>").append(detalle.getDescripciondet()).append("</descripcion> \n")
                        .append("                                       <cantidad>").append(utilitario.getFormatoNumero(detalle.getCantidad(), 3)).append("</cantidad> \n")
                        .append("                               </detalle> \n");
            }
            str_xml.append("                            </detalles> \n");
            str_xml.append("                    </destinatario> \n")
                    .append("		</destinatarios> \n")
                    .append("		<infoAdicional> \n");
            if (comprobante.getDestinatario().getCorreo() != null && utilitario.isCorreoValido(comprobante.getDestinatario().getCorreo())) {
                str_xml.append("      		<campoAdicional nombre=\"Email\">").append(comprobante.getDestinatario().getCorreo()).append("</campoAdicional> \n");
            } else {
                str_xml.append("      		<campoAdicional nombre=\"Email\">").append("nodispone@produquimic.com.ec").append("</campoAdicional> \n");
            }
            if (comprobante.getDestinatario().getTelefono() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Teléfono\">").append(comprobante.getDestinatario().getTelefono()).append("</campoAdicional> \n");
            }
            if (comprobante.getDestinatario().getDirDestinatario() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Dirección\">").append(comprobante.getDestinatario().getDirDestinatario()).append("</campoAdicional> \n");
            }
            if (comprobante.getNumOrdenCompra() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Orden de Compra\">").append(comprobante.getNumOrdenCompra()).append("</campoAdicional> \n");
            }
            str_xml.append("		</infoAdicional> \n");
            str_xml.append("     </guiaRemision>");
        }
        return str_xml.toString();
    }

}
