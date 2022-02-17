/*
 *********************************************************************
 Objetivo: Clase RetencionService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 06-Abril-2017             D. Jácome        RFC-201704-843
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.DetalleImpuesto;
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
public class RetencionServiceImp implements RetencionService {

    @EJB
    private EmisorService emisorService;
    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public String getXmlRetencion(Comprobante comprobante) throws GenericException {
        StringBuilder str_xml = new StringBuilder();
        if (comprobante != null) {
            Emisor emisor = emisorService.getEmisor(comprobante.getOficina());
            str_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                    .append("     <comprobanteRetencion id=\"comprobante\" version=\"1.0.0\"> \n")
                    .append("		<infoTributaria> \n")
                    .append("			<ambiente>").append(emisor.getAmbiente()).append("</ambiente> \n")
                    .append("			<tipoEmision>").append(comprobante.getTipoemision()).append("</tipoEmision> \n")
                    .append("			<razonSocial>").append(emisor.getRazonsocial()).append("</razonSocial> \n")
                    .append("			<nombreComercial>").append(emisor.getNombrecomercial()).append("</nombreComercial> \n")
                    .append("			<ruc>").append(emisor.getRuc()).append("</ruc> \n")
                    .append("			<claveAcceso>").append(comprobante.getClaveacceso()).append("</claveAcceso> \n")
                    .append("			<codDoc>").append(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo()).append("</codDoc> \n")
                    .append("			<estab>").append(comprobante.getEstab()).append("</estab> \n")
                    .append("			<ptoEmi>").append(comprobante.getPtoemi()).append("</ptoEmi> \n")
                    .append("			<secuencial>").append(comprobante.getSecuencial()).append("</secuencial> \n")
                    .append("			<dirMatriz>").append(emisor.getDirmatriz()).append("</dirMatriz> \n");
//            if (comprobante.getRegMicroEmpresa() != null) {
//                str_xml.append("      		<regimenMicroempresas>").append(comprobante.getRegMicroEmpresa()).append("</regimenMicroempresas> \n");
//            }
//            if (comprobante.getAgenteRetRes() != null) {
//                str_xml.append("      		<agenteRetencion>").append(comprobante.getAgenteRetRes()).append("</agenteRetencion> \n");
//            }
            str_xml.append("		</infoTributaria> \n")
                    .append("		<infoCompRetencion> \n")
                    .append("			<fechaEmision>").append(utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd/MM/yyyy")).append("</fechaEmision> \n")
                    .append("			<dirEstablecimiento>").append(emisor.getDirsucursal()).append("</dirEstablecimiento> \n")
                    .append("			<contribuyenteEspecial>").append(emisor.getContribuyenteespecial()).append("</contribuyenteEspecial> \n")
                    .append("			<obligadoContabilidad>").append(emisor.getObligadocontabilidad()).append("</obligadoContabilidad> \n")
                    .append("			<tipoIdentificacionSujetoRetenido>").append(comprobante.getCliente().getTipoIdentificacion()).append("</tipoIdentificacionSujetoRetenido> \n")
                    .append("			<razonSocialSujetoRetenido>").append(comprobante.getCliente().getNombreCliente()).append("</razonSocialSujetoRetenido> \n")
                    .append("			<identificacionSujetoRetenido>").append(comprobante.getCliente().getIdentificacion().trim()).append("</identificacionSujetoRetenido> \n")
                    .append("			<periodoFiscal>").append(comprobante.getPeriodofiscal()).append("</periodoFiscal> \n")
                    .append("		</infoCompRetencion> \n")
                    .append("		<impuestos> \n");
            for (DetalleImpuesto impuesto : comprobante.getImpuesto()) {
                str_xml.append("					<impuesto> \n")
                        .append("						<codigo>").append(impuesto.getCodigo()).append("</codigo> \n")
                        .append("						<codigoRetencion>").append(impuesto.getCodigoRetencion()).append("</codigoRetencion> \n")
                        .append("						<baseImponible>").append(utilitario.getFormatoNumero(impuesto.getBaseImponible().doubleValue())).append("</baseImponible> \n")
                        .append("						<porcentajeRetener>").append(utilitario.getFormatoNumero(impuesto.getPorcentajeRetener().doubleValue())).append("</porcentajeRetener> \n")
                        .append("						<valorRetenido>").append(utilitario.getFormatoNumero(impuesto.getValorRetenido().doubleValue())).append("</valorRetenido> \n")
                        .append("						<codDocSustento>").append(impuesto.getCodDocSustento()).append("</codDocSustento> \n")
                        .append("						<numDocSustento>").append(impuesto.getNumDocSustento()).append("</numDocSustento> \n")
                        .append("						<fechaEmisionDocSustento>").append(utilitario.getFormatoFecha(impuesto.getFechaEmisionDocSustento(), "dd/MM/yyyy")).append("</fechaEmisionDocSustento> \n")
                        .append("					</impuesto>             \n");
            }
            str_xml.append("		</impuestos> \n")
                    .append("		<infoAdicional> \n");
            if (comprobante.getCorreo() != null && utilitario.isCorreoValido(comprobante.getCorreo())) {
                str_xml.append("      		<campoAdicional nombre=\"Email\">").append(comprobante.getCorreo()).append("</campoAdicional> \n");
            } else {
                str_xml.append("      		<campoAdicional nombre=\"Email\">").append("ventas@gmail.com").append("</campoAdicional> \n");
            }
            if (comprobante.getCliente().getTelefono() != null && comprobante.getCliente().getTelefono().isEmpty() == false) {
                str_xml.append("      		<campoAdicional nombre=\"Teléfono\">").append(comprobante.getCliente().getTelefono()).append("</campoAdicional> \n");
            }
            if (comprobante.getCliente().getDireccion() != null && comprobante.getCliente().getDireccion().isEmpty() == false) {
                str_xml.append("      		<campoAdicional nombre=\"Dirección\">").append(comprobante.getCliente().getDireccion()).append("</campoAdicional> \n");
            }
            if (utilitario.isFechaMayor(comprobante.getFechaemision(), utilitario.getFecha("2020-10-01"))) {
                if (comprobante.getAgenteRetRes() != null) {
                    str_xml.append("      		<campoAdicional nombre=\"Agente de Retención\">").append(comprobante.getAgenteRetRes()).append("</campoAdicional> \n");
                }
                if (comprobante.getRegMicroEmpresa() != null) {
                    str_xml.append("      		<campoAdicional nombre=\"Régimen Microempresa\">").append(comprobante.getRegMicroEmpresa()).append("</campoAdicional> \n");
                }
            }
            str_xml.append("		</infoAdicional> \n");
            str_xml.append("     </comprobanteRetencion>");
        }
        return str_xml.toString();
    }
}
