/*
 *********************************************************************
 Objetivo: Servicio que implementa interface FacturaService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.DetalleComprobante;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.dto.InfoAdicional;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoImpuestoEnum;
import dj.comprobantes.offline.enums.TipoImpuestoIvaEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class FacturaServiceImp implements FacturaService {

    @EJB
    private EmisorService emisorService;
    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public String getXmlFactura(Comprobante comprobante) throws GenericException {

        StringBuilder str_xml = new StringBuilder();
        if (comprobante != null) {

            String moneda = "DOLAR";

            double dou_base_no_objeto_iva = 0;
            double dou_base_tarifa0 = 0;
            double dou_base_grabada = 0;

            try {
                dou_base_no_objeto_iva = comprobante.getSubtotalNoObjeto().doubleValue();
            } catch (Exception e) {
            }
            try {
                dou_base_tarifa0 = comprobante.getSubtotal0().doubleValue();
            } catch (Exception e) {
            }
            try {
                dou_base_grabada = comprobante.getSubtotal().doubleValue();
            } catch (Exception e) {
            }

            double totalSinImpuestos = dou_base_no_objeto_iva + dou_base_tarifa0 + dou_base_grabada;
            Emisor emisor = emisorService.getEmisor(comprobante.getOficina());
            double dou_porcentaje_iva = 0;
            try {
                dou_porcentaje_iva = (comprobante.getIva().doubleValue() * 100) / totalSinImpuestos;
            } catch (Exception e) {
            }

            double dou_descuento = 0;
            try {
                dou_descuento = comprobante.getTotaldescuento().doubleValue();
            } catch (Exception e) {
            }

            //22-07-2019
            //Corrección visualiza subtotales en RIDE del SRI
            StringBuilder str_subtotales = new StringBuilder();
            if (comprobante.getSubtotal().doubleValue() > 0) {
                str_subtotales.append("				<totalImpuesto> \n")
                        .append("					<codigo>").append(TipoImpuestoEnum.IVA.getCodigo()).append("</codigo> \n")
                        .append("					<codigoPorcentaje>").append(TipoImpuestoIvaEnum.getCodigo(dou_porcentaje_iva)).append("</codigoPorcentaje> \n")
                        .append("					<descuentoAdicional>").append(utilitario.getFormatoNumero(0)).append("</descuentoAdicional> \n")
                        .append("					<baseImponible>").append(utilitario.getFormatoNumero(comprobante.getSubtotal())).append("</baseImponible> \n")
                        .append("					<valor>").append(utilitario.getFormatoNumero(comprobante.getIva())).append("</valor> \n")
                        .append("				</totalImpuesto> \n");
            }

            if (comprobante.getSubtotal0().doubleValue() > 0) {
                str_subtotales.append("				<totalImpuesto> \n")
                        .append("					<codigo>").append(TipoImpuestoEnum.IVA.getCodigo()).append("</codigo> \n")
                        .append("					<codigoPorcentaje>").append(TipoImpuestoIvaEnum.IVA_VENTA_0.getCodigo()).append("</codigoPorcentaje> \n")
                        .append("					<descuentoAdicional>").append(utilitario.getFormatoNumero(0)).append("</descuentoAdicional> \n")
                        .append("					<baseImponible>").append(utilitario.getFormatoNumero(comprobante.getSubtotal0())).append("</baseImponible> \n")
                        .append("					<valor>").append(utilitario.getFormatoNumero(0)).append("</valor> \n")
                        .append("				</totalImpuesto> \n");
            }

            if (comprobante.getSubtotalNoObjeto().doubleValue() > 0) {
                str_subtotales.append("				<totalImpuesto> \n")
                        .append("					<codigo>").append(TipoImpuestoEnum.IVA.getCodigo()).append("</codigo> \n")
                        .append("					<codigoPorcentaje>").append(TipoImpuestoIvaEnum.IVA_NO_OBJETO.getCodigo()).append("</codigoPorcentaje> \n")
                        .append("					<descuentoAdicional>").append(utilitario.getFormatoNumero(0)).append("</descuentoAdicional> \n")
                        .append("					<baseImponible>").append(utilitario.getFormatoNumero(comprobante.getSubtotalNoObjeto())).append("</baseImponible> \n")
                        .append("					<valor>").append(utilitario.getFormatoNumero(0)).append("</valor> \n")
                        .append("				</totalImpuesto> \n");
            }

            str_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                    .append("     <factura id=\"comprobante\" version=\"1.1.0\"> \n")
                    .append("		<infoTributaria> \n")
                    .append("			<ambiente>").append(emisor.getAmbiente()).append("</ambiente> \n")
                    .append("			<tipoEmision>").append(comprobante.getTipoemision()).append("</tipoEmision> \n")
                    .append("			<razonSocial>").append(emisor.getRazonsocial()).append("</razonSocial> \n")
                    .append("			<nombreComercial>").append(emisor.getNombrecomercial()).append("</nombreComercial> \n")
                    .append("			<ruc>").append(emisor.getRuc()).append("</ruc> \n")
                    .append("			<claveAcceso>").append(comprobante.getClaveacceso()).append("</claveAcceso> \n")
                    .append("			<codDoc>").append(TipoComprobanteEnum.FACTURA.getCodigo()).append("</codDoc> \n")
                    .append("			<estab>").append(comprobante.getEstab()).append("</estab> \n")
                    .append("			<ptoEmi>").append(comprobante.getPtoemi()).append("</ptoEmi> \n")
                    .append("			<secuencial>").append(comprobante.getSecuencial()).append("</secuencial> \n")
                    .append("			<dirMatriz>").append(emisor.getDirmatriz()).append("</dirMatriz> \n")
                    .append("		</infoTributaria> \n")
                    .append("		<infoFactura> \n")
                    .append("			<fechaEmision>").append(utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd/MM/yyyy")).append("</fechaEmision> \n")
                    .append("			<dirEstablecimiento>").append(emisor.getDirsucursal()).append("</dirEstablecimiento> \n")
                    .append("			<contribuyenteEspecial>").append(emisor.getContribuyenteespecial()).append("</contribuyenteEspecial> \n")
                    .append("			<obligadoContabilidad>").append(emisor.getObligadocontabilidad()).append("</obligadoContabilidad> \n")
                    .append("			<tipoIdentificacionComprador>").append(comprobante.getCliente().getTipoIdentificacion()).append("</tipoIdentificacionComprador> \n");
            if (comprobante.getGuiaremision() != null) {
                str_xml.append("			<guiaRemision>").append(comprobante.getGuiaremision()).append("</guiaRemision> \n");
            }
            str_xml.append("			<razonSocialComprador>").append(comprobante.getCliente().getNombreCliente()).append("</razonSocialComprador> \n")
                    .append("			<identificacionComprador>").append(comprobante.getCliente().getIdentificacion().trim()).append("</identificacionComprador> \n")
                    .append("			<direccionComprador>").append(comprobante.getCliente().getDireccion()).append("</direccionComprador> \n")
                    .append("			<totalSinImpuestos>").append(utilitario.getFormatoNumero(totalSinImpuestos)).append("</totalSinImpuestos> \n")
                    .append("			<totalDescuento>").append((utilitario.getFormatoNumero(dou_descuento))).append("</totalDescuento> \n")
                    .append("			<totalConImpuestos> \n")
                    .append(str_subtotales)
                    .append("			</totalConImpuestos> \n")
                    .append("			<propina>").append(utilitario.getFormatoNumero(0)).append("</propina> \n")
                    .append("			<importeTotal>").append(utilitario.getFormatoNumero(comprobante.getImportetotal())).append("</importeTotal> \n")
                    .append("			<moneda>").append(moneda).append("</moneda> \n")
                    .append("                   <pagos> \n")
                    .append("                           <pago> \n")
                    .append("                                    <formaPago>").append(comprobante.getFormaCobro()).append("</formaPago> \n")
                    .append("                                    <total>").append(utilitario.getFormatoNumero(comprobante.getImportetotal())).append("</total> \n")
                    .append("                                    <plazo>").append(comprobante.getDiasCredito()).append("</plazo> \n")
                    .append("                                    <unidadTiempo>").append("dias").append("</unidadTiempo> \n")
                    .append("                           </pago> \n")
                    .append("                   </pagos>\n")
                    .append("		</infoFactura> \n")
                    .append("		<detalles> \n");
            for (DetalleComprobante detalle : comprobante.getDetalle()) {
                str_xml.append("			<detalle> \n")
                        .append("				<codigoPrincipal>").append(detalle.getCodigoprincipal()).append("</codigoPrincipal> \n")
                        .append("				<codigoAuxiliar>").append((detalle.getCodigoauxiliar() == null ? detalle.getCodigoprincipal() : detalle.getCodigoauxiliar())).append("</codigoAuxiliar> \n")
                        .append("				<descripcion>").append(detalle.getDescripciondet()).append("</descripcion> \n")
                        .append("				<cantidad>").append(utilitario.getFormatoNumero(detalle.getCantidad(), emisor.getDecimalesCantidad())).append("</cantidad> \n")
                        .append("				<precioUnitario>").append(utilitario.getFormatoNumero(detalle.getPreciounitario(), emisor.getDecimalesPrecioUnitario())).append("</precioUnitario> \n")
                        .append("				<descuento>").append((detalle.getDescuento() == null ? utilitario.getFormatoNumero(0) : utilitario.getFormatoNumero(detalle.getDescuento()))).append("</descuento> \n")
                        .append("				<precioTotalSinImpuesto>").append(utilitario.getFormatoNumero(detalle.getPreciototalsinimpuesto())).append("</precioTotalSinImpuesto> \n")
                        .append("				<impuestos> \n")
                        .append("					<impuesto> \n")
                        .append("						<codigo>").append(TipoImpuestoEnum.IVA.getCodigo()).append("</codigo> \n")
                        .append("						<codigoPorcentaje>").append(detalle.getCodigoPorcentaje()).append("</codigoPorcentaje> \n")
                        .append("						<tarifa>").append(detalle.getPorcentajeiva()).append("</tarifa> \n")
                        .append("						<baseImponible>").append(utilitario.getFormatoNumero(detalle.getPreciototalsinimpuesto())).append("</baseImponible> \n")
                        .append("						<valor>").append(utilitario.getFormatoNumero((detalle.getPreciototalsinimpuesto().doubleValue() * (detalle.getPorcentajeiva().doubleValue() / 100)))).append("</valor> \n")
                        .append("					</impuesto>             \n")
                        .append("				</impuestos> \n")
                        .append("			</detalle> \n");
            }
            str_xml.append("		</detalles> \n")
                    .append("		<retenciones> \n")
                    .append("			<retencion> \n")
                    .append("				<codigo>4</codigo>             \n")
                    .append("				<codigoPorcentaje>3</codigoPorcentaje> \n")
                    .append("				<tarifa>1</tarifa> \n")
                    .append("				<valor>0.00</valor> \n")
                    .append("			</retencion> \n")
                    .append("		</retenciones> \n");
            str_xml.append("		<infoAdicional> \n");
            if (comprobante.getCliente().getCorreo() != null && utilitario.isCorreoValido(comprobante.getCliente().getCorreo())) {
                str_xml.append("      		<campoAdicional nombre=\"Email\">").append(comprobante.getCliente().getCorreo()).append("</campoAdicional> \n");
            }
            if (comprobante.getCliente().getTelefono() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Teléfono\">").append(comprobante.getCliente().getTelefono()).append("</campoAdicional> \n");
            }
            if (comprobante.getCliente().getDireccion() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Dirección\">").append(comprobante.getCliente().getDireccion()).append("</campoAdicional> \n");
            }
            if (comprobante.getNumOrdenCompra() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Referencia\">").append(comprobante.getNumOrdenCompra()).append("</campoAdicional> \n");
            }
            if (comprobante.getInfoAdicional1() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Usuario\">").append(comprobante.getInfoAdicional1()).append("</campoAdicional> \n");
            }
            if (comprobante.getInfoAdicional2() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Forma de Pago\">").append(comprobante.getInfoAdicional2()).append("</campoAdicional> \n");
            }
            if (comprobante.getInfoAdicional3() != null) {
                str_xml.append("      		<campoAdicional nombre=\"Observación\">").append(comprobante.getInfoAdicional3()).append("</campoAdicional> \n");
            }
            for (InfoAdicional inf : comprobante.getInfoAdicional()) {
                str_xml.append("      		<campoAdicional nombre=\"").append(inf.getNombre()).append("\">").append(inf.getValor()).append("</campoAdicional> \n");
            }
            str_xml.append("		</infoAdicional> \n");
            str_xml.append("     </factura>");
        }
        return str_xml.toString();
    }
}
