/*
 *********************************************************************
 Objetivo: Servicio que implementa interface NotaCreditoService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 27-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.DetalleComprobante;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoImpuestoEnum;
import dj.comprobantes.offline.enums.TipoImpuestoIvaEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author djacome
 */
@Stateless
public class NotaCreditoServiceImp implements NotaCreditoService {

    @EJB
    private EmisorService emisorService;
    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public String getXmlNotaCredito(Comprobante comprobante) throws GenericException {
        StringBuilder str_xml = new StringBuilder();
        if (comprobante != null) {
            String moneda = "DOLAR"; // *********!!!Poner variable

            double dou_base_no_objeto_iva = 0; // No aplica para RC
            double dou_base_tarifa0 = 0;
            double dou_base_grabada = 0;
            try {
                dou_base_tarifa0 = comprobante.getSubtotal0().doubleValue();
            } catch (Exception e) {
            }
            try {
                dou_base_grabada = comprobante.getSubtotal().doubleValue();
            } catch (Exception e) {
            }

            double totalSinImpuestos = dou_base_no_objeto_iva + dou_base_tarifa0 + dou_base_grabada;
            double dou_porcentaje_iva = 0;
            try {
                dou_porcentaje_iva = (comprobante.getIva().doubleValue() * 100) / totalSinImpuestos;
            } catch (Exception e) {
            }
            Emisor emisor = emisorService.getEmisor(comprobante.getOficina());
            str_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                    .append("     <notaCredito id=\"comprobante\" version=\"1.1.0\"> \n")
                    .append("		<infoTributaria> \n")
                    .append("			<ambiente>").append(emisor.getAmbiente()).append("</ambiente> \n")
                    .append("			<tipoEmision>").append(comprobante.getTipoemision()).append("</tipoEmision> \n")
                    .append("			<razonSocial>").append(emisor.getRazonsocial()).append("</razonSocial> \n")
                    .append("			<nombreComercial>").append(emisor.getNombrecomercial()).append("</nombreComercial> \n")
                    .append("			<ruc>").append(emisor.getRuc()).append("</ruc> \n")
                    .append("			<claveAcceso>").append(comprobante.getClaveacceso()).append("</claveAcceso> \n")
                    .append("			<codDoc>").append(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo()).append("</codDoc> \n")
                    .append("			<estab>").append(comprobante.getEstab()).append("</estab> \n")
                    .append("			<ptoEmi>").append(comprobante.getPtoemi()).append("</ptoEmi> \n")
                    .append("			<secuencial>").append(comprobante.getSecuencial()).append("</secuencial> \n")
                    .append("			<dirMatriz>").append(emisor.getDirmatriz()).append("</dirMatriz> \n")
                    .append("		</infoTributaria> \n")
                    .append("		<infoNotaCredito> \n")
                    .append("			<fechaEmision>").append(utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd/MM/yyyy")).append("</fechaEmision> \n")
                    .append("			<dirEstablecimiento>").append(emisor.getDirsucursal()).append("</dirEstablecimiento> \n")
                    .append("			<tipoIdentificacionComprador>").append(comprobante.getCliente().getTipoIdentificacion()).append("</tipoIdentificacionComprador> \n")
                    .append("			<razonSocialComprador>").append(comprobante.getCliente().getNombreCliente()).append("</razonSocialComprador> \n")
                    .append("			<identificacionComprador>").append(comprobante.getCliente().getIdentificacion().trim()).append("</identificacionComprador> \n")
                    .append("			<contribuyenteEspecial>").append(emisor.getContribuyenteespecial()).append("</contribuyenteEspecial> \n")
                    .append("			<obligadoContabilidad>").append(emisor.getObligadocontabilidad()).append("</obligadoContabilidad> \n")
                    .append("                   <codDocModificado>").append(comprobante.getCoddocmodificado()).append("</codDocModificado> \n")
                    .append("                   <numDocModificado>").append(comprobante.getNumdocmodificado()).append("</numDocModificado> \n")
                    .append("                   <fechaEmisionDocSustento>").append(utilitario.getFormatoFecha(comprobante.getFechaemisiondocsustento(), "dd/MM/yyyy")).append("</fechaEmisionDocSustento> \n")
                    .append("			<totalSinImpuestos>").append(utilitario.getFormatoNumero(totalSinImpuestos)).append("</totalSinImpuestos> \n")
                    .append("                   <valorModificacion>").append(utilitario.getFormatoNumero(comprobante.getImportetotal())).append("</valorModificacion> \n")
                    .append("			<moneda>").append(moneda).append("</moneda> \n")
                    .append("			<totalConImpuestos> \n")
                    .append("				<totalImpuesto> \n")
                    .append("					<codigo>").append(TipoImpuestoEnum.IVA.getCodigo()).append("</codigo> \n")
                    .append("					<codigoPorcentaje>").append(TipoImpuestoIvaEnum.getCodigo(dou_porcentaje_iva)).append("</codigoPorcentaje> \n")
                    .append("					<baseImponible>").append(utilitario.getFormatoNumero(comprobante.getSubtotal())).append("</baseImponible> \n")
                    .append("					<valor>").append(utilitario.getFormatoNumero(comprobante.getIva())).append("</valor> \n")
                    .append("				</totalImpuesto> \n")
                    .append("			</totalConImpuestos> \n")
                    .append("			<motivo>").append(comprobante.getMotivo()).append("</motivo> \n")
                    .append("		</infoNotaCredito> \n")
                    .append("		<detalles> \n");
            for (DetalleComprobante detalle : comprobante.getDetalle()) {
                str_xml.append("			<detalle> \n")
                        .append("				<codigoInterno>").append(detalle.getCodigoprincipal()).append("</codigoInterno> \n")
                        .append("				<codigoAdicional>").append((detalle.getCodigoauxiliar() == null ? detalle.getCodigoprincipal() : detalle.getCodigoauxiliar())).append("</codigoAdicional> \n")
                        .append("				<descripcion>").append(detalle.getDescripciondet()).append("</descripcion> \n")
                        .append("				<cantidad>").append(utilitario.getFormatoNumero(detalle.getCantidad(), emisor.getDecimalesCantidad())).append("</cantidad> \n")
                        .append("				<precioUnitario>").append(utilitario.getFormatoNumero(detalle.getPreciounitario(), emisor.getDecimalesPrecioUnitario())).append("</precioUnitario> \n")
                        .append("				<descuento>").append((detalle.getDescuento() == null ? utilitario.getFormatoNumero(0) : utilitario.getFormatoNumero(detalle.getDescuento(), emisor.getDecimalesPrecioUnitario()))).append("</descuento> \n")
                        .append("				<precioTotalSinImpuesto>").append(utilitario.getFormatoNumero(detalle.getPreciototalsinimpuesto())).append("</precioTotalSinImpuesto> \n")
                        .append("				<impuestos> \n")
                        .append("					<impuesto> \n")
                        .append("						<codigo>").append(TipoImpuestoEnum.IVA.getCodigo()).append("</codigo> \n")
                        .append("						<codigoPorcentaje>").append(TipoImpuestoIvaEnum.getCodigo(utilitario.getFormatoNumero(detalle.getPorcentajeiva()))).append("</codigoPorcentaje> \n")
                        .append("						<tarifa>").append(utilitario.getFormatoNumero(detalle.getPorcentajeiva())).append("</tarifa> \n")
                        .append("						<baseImponible>").append(utilitario.getFormatoNumero(detalle.getPreciototalsinimpuesto())).append("</baseImponible> \n")
                        .append("						<valor>").append(utilitario.getFormatoNumero((detalle.getPreciototalsinimpuesto().doubleValue() * (detalle.getPorcentajeiva().doubleValue() / 100)))).append("</valor> \n")
                        .append("					</impuesto>             \n")
                        .append("				</impuestos> \n")
                        .append("			</detalle> \n");
            }
            str_xml.append("		</detalles> \n");
            str_xml.append("		<infoAdicional> \n");
            if (comprobante.getCliente().getCorreo() != null && utilitario.isCorreoValido(comprobante.getCliente().getCorreo())) {
                str_xml.append("      		<campoAdicional nombre=\"Email\">").append(comprobante.getCliente().getCorreo()).append("</campoAdicional> \n");
            } else {
                str_xml.append("      		<campoAdicional nombre=\"Email\">").append("nodispone@produquimic.com.ec").append("</campoAdicional> \n");
            }
            if (comprobante.getCliente().getTelefono() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Teléfono\">").append(comprobante.getCliente().getTelefono()).append("</campoAdicional> \n");
            }
            if (comprobante.getCliente().getDireccion() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Dirección\">").append(comprobante.getCliente().getDireccion()).append("</campoAdicional> \n");
            }
            str_xml.append("		</infoAdicional> \n");
            str_xml.append("     </notaCredito>");
        }

        return str_xml.toString();
    }

}
