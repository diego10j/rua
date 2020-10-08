/*
 *********************************************************************
 Objetivo: Servicio que implementa interface ArchivoService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dao.XmlComprobanteDAO;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.XmlComprobante;
import dj.comprobantes.offline.enums.ParametrosSistemaEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoImpuestoEnum;
import dj.comprobantes.offline.enums.TipoImpuestoIvaEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.reporte.DetalleReporte;
import dj.comprobantes.offline.reporte.GenerarReporte;
import dj.comprobantes.offline.reporte.ReporteDataSource;
import dj.comprobantes.offline.util.UtilitarioCeo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ArchivoServiceImp implements ArchivoService {

    @EJB
    private XmlComprobanteDAO sriComprobanteDao;
    @EJB
    private FacturaService facturaService;
    @EJB
    private NotaCreditoService notaCreditoService;
    @EJB
    private RetencionService retencionService;
    @EJB
    private GuiaRemisionService guiaRemisionService;
    @EJB
    private LiquidacionCompraService liquidacionCompraService;
    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public byte[] getXml(Comprobante comprobante) throws GenericException {
        try {
            XmlComprobante sriComprobante = sriComprobanteDao.getSriComprobanteActual(comprobante);
            if (sriComprobante == null) {
                return null;
            }
            // Crea el archivo y escribe el XML
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(sriComprobante.getXmlcomprobante()));
            Format format = Format.getPrettyFormat();
            format.setEncoding("ISO-8859-1"); // Por las tildes
            XMLOutputter serializerxml = new XMLOutputter(format);
            String xmlInvoice = serializerxml.outputString(doc);
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xmlInvoice.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            InputStream myInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
            byte[] bpdf = IOUtils.toByteArray(myInputStream);
            myInputStream.close();
            return bpdf;
        } catch (GenericException | JDOMException | IOException | IllegalArgumentException | TransformerException e) {

            throw new GenericException("ERROR. al crear XML", e);
        }

    }

    @Override
    public byte[] getPdf(Comprobante comprobante) throws GenericException {
        XmlComprobante sriComprobante = sriComprobanteDao.getSriComprobanteActual(comprobante);
        String cadenaXML = "";
        if (sriComprobante == null) {
            //AUN NO SE ENVIA AL SRI
            if (comprobante.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo())) {
                cadenaXML = facturaService.getXmlFactura(comprobante);
            } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo())) {
                cadenaXML = notaCreditoService.getXmlNotaCredito(comprobante);
            } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo())) {
                cadenaXML = retencionService.getXmlRetencion(comprobante);
            } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
                cadenaXML = guiaRemisionService.getXmlGuiaRemision(comprobante);
            } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS.getCodigo())) {
                cadenaXML = liquidacionCompraService.getXmlLiquidacionCompra(comprobante);
            }
        } else {
            cadenaXML = sriComprobante.getXmlcomprobante();
        }
        Map<String, Object> parametros = getParametrosComunes(cadenaXML, comprobante);
        List<DetalleReporte> lisDetalle = new ArrayList<>();
        // Info Adicional infoAdicional
        String cadenainfoAdicional = utilitario.getValorEtiqueta(cadenaXML, "infoAdicional");
        String strcampoAdicional[] = cadenainfoAdicional.split("</campoAdicional>");
        // Crea Collection de info Adicional
        try {
            Collection<String> col_name = null;
            for (String strcampoAdicional1 : strcampoAdicional) {
                if (col_name == null) {
                    col_name = new ArrayList<>();
                }
                String strcampo = strcampoAdicional1;
                String name = strcampo.substring((strcampo.indexOf("nombre=\"") + 8), (strcampo.lastIndexOf(">") - 1));
                String valor = strcampo.substring(strcampo.lastIndexOf(">") + 1);
                col_name.add(name + " : " + valor);
            }
            parametros.put("col_name", col_name);
        } catch (Exception e) {
            System.out.println("metodo getPdf "+e);
        }
        // Recorre todos los detalles de factura o nota de credito
        if (comprobante.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo()) || comprobante.getCoddoc().equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo()) || comprobante.getCoddoc().equals(TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS.getCodigo())) {
            // Detalles
            String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "detalles");
            // //Forma un arreglo de todos los detalles
            String strDetalles[] = cadenaDetalles.split("</detalle>");

            String columnas[] = {"codigoPrincipal", "codigoAuxiliar", "cantidad", "descripcion", "precioUnitario", "precioTotalSinImpuesto", "descuento"};
            for (String strDetalleActual : strDetalles) {
                Object valores[] = {
                    (utilitario.getValorEtiqueta(strDetalleActual, "codigoPrincipal").isEmpty() ? utilitario.getValorEtiqueta(strDetalleActual,
                    "codigoInterno") : utilitario.getValorEtiqueta(strDetalleActual, "codigoPrincipal")),
                    (utilitario.getValorEtiqueta(strDetalleActual, "codigoAuxiliar").isEmpty() ? utilitario.getValorEtiqueta(strDetalleActual,
                    "codigoAdicional") : utilitario.getValorEtiqueta(strDetalleActual, "codigoAuxiliar")),
                    utilitario.getValorEtiqueta(strDetalleActual, "cantidad"),
                    utilitario.getValorEtiqueta(strDetalleActual, "descripcion"),
                    utilitario.getValorEtiqueta(strDetalleActual, "precioUnitario"),
                    utilitario.getValorEtiqueta(strDetalleActual, "precioTotalSinImpuesto"),
                    utilitario.getValorEtiqueta(strDetalleActual, "descuento")};
                lisDetalle.add(new DetalleReporte(columnas, valores));
            }
        } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
            //Destinatario
            String cadenaDestinatario = utilitario.getValorEtiqueta(cadenaXML, "destinatario");
            String nombreComprobante = TipoComprobanteEnum.getDescripcion(utilitario.getValorEtiqueta(cadenaXML, "codDocSustento"));
            String numDocSustento = utilitario.getValorEtiqueta(cadenaDestinatario, "numDocSustento");
            String fechaEmisionSustento = utilitario.getValorEtiqueta(cadenaDestinatario, "fechaEmisionDocSustento");
            String numeroAutorizacion = utilitario.getValorEtiqueta(cadenaDestinatario, "numAutDocSustento");
            String motivoTraslado = utilitario.getValorEtiqueta(cadenaDestinatario, "motivoTraslado");
            String destino = utilitario.getValorEtiqueta(cadenaDestinatario, "dirDestinatario");
            String rucDestinatario = utilitario.getValorEtiqueta(cadenaDestinatario, "identificacionDestinatario");
            String razonSocial = utilitario.getValorEtiqueta(cadenaDestinatario, "razonSocialDestinatario");
            String docAduanero = utilitario.getValorEtiqueta(cadenaDestinatario, "impuestos");
            String codigoEstab = utilitario.getValorEtiqueta(cadenaDestinatario, "impuestos");
            String ruta = "RUTA TRANSPORTE";
            // Detalles Guia
            String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "detalles");
            // //Forma un arreglo de todos los impuestos
            String strDetalles[] = cadenaDetalles.split("</detalle>");
            String columnas[] = {"nombreComprobante", "numDocSustento", "fechaEmisionSustento", "numeroAutorizacion", "motivoTraslado", "destino", "rucDestinatario", "razonSocial", "docAduanero", "codigoEstab", "ruta", "cantidad", "descripcion", "codigoPrincipal", "codigoAuxiliar"};
            for (String strDetalleActual : strDetalles) {
                Object valores[] = {
                    nombreComprobante,
                    numDocSustento,
                    fechaEmisionSustento,
                    numeroAutorizacion,
                    motivoTraslado,
                    destino,
                    rucDestinatario,
                    razonSocial,
                    docAduanero,
                    codigoEstab,
                    ruta,
                    utilitario.getValorEtiqueta(strDetalleActual, "cantidad"),
                    utilitario.getValorEtiqueta(strDetalleActual, "descripcion"),
                    utilitario.getValorEtiqueta(strDetalleActual, "codigoInterno"),
                    utilitario.getValorEtiqueta(strDetalleActual, "codigoAdicional")
                };
                lisDetalle.add(new DetalleReporte(columnas, valores));
            }
        } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo())) {
            // Detalles impuestos
            String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "impuestos");
            // //Forma un arreglo de todos los impuestos
            String strDetalles[] = cadenaDetalles.split("</impuesto>");
            String columnas[] = {"baseImponible", "porcentajeRetener", "valorRetenido", "nombreImpuesto", "nombreComprobante", "numeroComprobante", "fechaEmisionCcompModificado", "codigoRetencion"};
            for (String strDetalleActual : strDetalles) {
                Object valores[] = {
                    utilitario.getValorEtiqueta(strDetalleActual, "baseImponible"),
                    utilitario.getValorEtiqueta(strDetalleActual, "porcentajeRetener"),
                    utilitario.getValorEtiqueta(strDetalleActual, "valorRetenido"),
                    TipoImpuestoEnum.getDescripcion(utilitario.getValorEtiqueta(strDetalleActual, "codigo")),
                    TipoComprobanteEnum.getDescripcion(utilitario.getValorEtiqueta(strDetalleActual, "codDocSustento")),
                    utilitario.getValorEtiqueta(strDetalleActual, "numDocSustento"),
                    utilitario.getValorEtiqueta(strDetalleActual, "fechaEmisionDocSustento"),
                    utilitario.getValorEtiqueta(strDetalleActual, "codigoRetencion")
                };
                lisDetalle.add(new DetalleReporte(columnas, valores));
            }
        }
        // Genera el reporte en PDF
        ReporteDataSource data = new ReporteDataSource(lisDetalle);
        GenerarReporte generarReporte = new GenerarReporte();
        generarReporte.setDataSource(data);
        File reporte = null;
        if (comprobante.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo())) {
            reporte = generarReporte.crearPDF(parametros, "factura.jasper", parametros.get("CLAVE_ACC") + "");
        } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo())) {
            reporte = generarReporte.crearPDF(parametros, "notaCredito.jasper", parametros.get("CLAVE_ACC") + "");
        } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
            reporte = generarReporte.crearPDF(parametros, "guiaRemisionFinal.jasper", parametros.get("CLAVE_ACC") + "");
        } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo())) {
            reporte = generarReporte.crearPDF(parametros, "comprobanteRetencion.jasper", parametros.get("CLAVE_ACC") + "");
        } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS.getCodigo())) {
            System.out.println(" parametro liqui "+parametros.toString());
            reporte = generarReporte.crearPDF(parametros, "liquidacionCompra.jasper", parametros.get("CLAVE_ACC") + "");
        }

        try {
            InputStream myInputStream = new FileInputStream(reporte);
            byte[] bpdf = IOUtils.toByteArray(myInputStream);
            myInputStream.close();
            // Borra el archivo cuando termina el proceso
            reporte.delete();
            return bpdf;
        } catch (Exception e) {
            throw new GenericException("ERROR. al crear PDF", e);
        }
    }

    /**
     * Construye el arreglo de parametros comunes para los reportes de los
     * comprobantes electrónicos
     *
     * @param cadenaXML
     * @param comprobante
     * @return
     */
    private Map<String, Object> getParametrosComunes(String cadenaXML, Comprobante comprobante) {
        Map<String, Object> parametros = new HashMap<>();
        try {
            parametros.put("PAGINA_CONSULTA", ParametrosSistemaEnum.PAGINA_CONSULTA.getCodigo());
            parametros.put("SUBREPORT_DIR", ParametrosSistemaEnum.RUTA_SISTEMA.getCodigo());
            parametros.put("RUC", utilitario.getValorEtiqueta(cadenaXML, "ruc"));
            parametros.put("NUM_AUT", comprobante.getNumAutorizacion());
            if (comprobante.getFechaautoriza() != null) {
                parametros.put("FECHA_AUT", utilitario.getFormatoFecha(comprobante.getFechaautoriza(), "dd 'de' MMMM 'del' yyyy  HH:mm"));
            }
            parametros.put("TIPO_EMISION", utilitario.getValorEtiqueta(cadenaXML, "tipoEmision"));
            parametros.put("CLAVE_ACC", utilitario.getValorEtiqueta(cadenaXML, "claveAcceso"));
            parametros.put("RAZON_SOCIAL", utilitario.getValorEtiqueta(cadenaXML, "razonSocial"));
            parametros.put("DIR_MATRIZ", utilitario.getValorEtiqueta(cadenaXML, "dirMatriz"));
            parametros.put("DIR_SUCURSAL", utilitario.getValorEtiqueta(cadenaXML, "dirEstablecimiento"));
            parametros.put("CONT_ESPECIAL", utilitario.getValorEtiqueta(cadenaXML, "contribuyenteEspecial"));
            parametros.put("LLEVA_CONTABILIDAD", utilitario.getValorEtiqueta(cadenaXML, "obligadoContabilidad"));
            parametros.put("RS_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "razonSocialComprador"));
            parametros.put("RUC_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "identificacionComprador"));
            parametros.put("GUIA", utilitario.getValorEtiqueta(cadenaXML, "guiaRemision"));

            parametros.put("TELEFONO", comprobante.getTelefonos());//22-06-2018
            parametros.put("SIN_FINES_LUCRO", String.valueOf(comprobante.isSinFinesLucro())); //22-06-2018
            parametros.put("CORREO_EMPRESA", comprobante.getCorreoEmpresa()); //07-07-2018
            parametros.put("RESOLUCIONSRI", comprobante.getResolucionSri()); //12-01-2019
            
            parametros.put("AGENTE_RET_RES_EMPR", comprobante.getAgenteRetRes()); //07-10-2020
            parametros.put("REG_MICRO_EMPR", comprobante.getRegMicroEmpresa()); //07-10-2020
            
            parametros.put("NUM_DOC_MODIFICADO", utilitario.getValorEtiqueta(cadenaXML, "numDocModificado"));
            parametros.put("DOC_MODIFICADO", TipoComprobanteEnum.getDescripcion(utilitario.getValorEtiqueta(cadenaXML, "codDocModificado")));
            parametros.put("FECHA_EMISION_DOC_SUSTENTO", utilitario.getValorEtiqueta(cadenaXML, "fechaEmisionDocSustento"));
            parametros.put("RAZON_MODIF", utilitario.getValorEtiqueta(cadenaXML, "motivo"));

            if (comprobante.getFechaemision() != null) {
                parametros.put("FECHA_EMISION", utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd 'de' MMMM 'del' yyyy"));
            }
            parametros.put("VALOR_TOTAL", utilitario.getFormatoNumero(comprobante.getImportetotal()));
            parametros.put("DESCUENTO", utilitario.getValorEtiqueta(cadenaXML, "totalDescuento"));
            parametros.put("IVA", utilitario.getFormatoNumero(comprobante.getIva()));
            parametros.put("IVA_12", utilitario.getFormatoNumero(comprobante.getSubtotal()));
            parametros.put("SUBTOTAL", utilitario.getValorEtiqueta(cadenaXML, "totalSinImpuestos"));
            parametros.put("NUM_FACT", utilitario.getValorEtiqueta(cadenaXML, "estab") + "-" + utilitario.getValorEtiqueta(cadenaXML, "ptoEmi") + "-"
                    + utilitario.getValorEtiqueta(cadenaXML, "secuencial"));
            parametros.put("TOTAL_DESCUENTO", utilitario.getValorEtiqueta(cadenaXML, "totalDescuento"));
            parametros.put("AMBIENTE", utilitario.getValorEtiqueta(cadenaXML, "ambiente"));
            parametros.put("NOM_COMERCIAL", utilitario.getValorEtiqueta(cadenaXML, "nombreComercial"));
            parametros.put("FORMA_PAGO", "20");//utilitario.getValorEtiqueta(cadenaXML, "formaCobro"));
            parametros.put("PLAZO", utilitario.getValorEtiqueta(cadenaXML, "plazo"));
            parametros.put("UNIDAD_TIEMPO", utilitario.getValorEtiqueta(cadenaXML, "unidadTiempo"));

            if (comprobante.getCoddoc().equals(TipoComprobanteEnum.COMPROBANTE_DE_RETENCION.getCodigo())) {
                parametros.put("EJERCICIO_FISCAL", utilitario.getValorEtiqueta(cadenaXML, "periodoFiscal"));
                parametros.put("RS_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "razonSocialSujetoRetenido"));
                parametros.put("RUC_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "identificacionSujetoRetenido"));
            }

            if (comprobante.getCoddoc().equals(TipoComprobanteEnum.GUIA_DE_REMISION.getCodigo())) {
                parametros.put("PLACA", utilitario.getValorEtiqueta(cadenaXML, "placa"));
                parametros.put("RS_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "razonSocialTransportista"));
                parametros.put("RUC_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "rucTransportista"));
                parametros.put("PUNTO_PARTIDA", utilitario.getValorEtiqueta(cadenaXML, "dirPartida"));
                parametros.put("FECHA_INI_TRANSPORTE", utilitario.getValorEtiqueta(cadenaXML, "fechaIniTransporte"));
                parametros.put("FECHA_FIN_TRANSPORTE", utilitario.getValorEtiqueta(cadenaXML, "fechaFinTransporte"));
            }

            if (comprobante.getCoddoc().equals(TipoComprobanteEnum.LIQUIDACION_DE_COMPRAS.getCodigo())) {
                parametros.put("RS_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "razonSocialProveedor"));
                parametros.put("RUC_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "identificacionProveedor"));
            }

            // Porcentaje iva
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
            double dou_porcentaje_iva = 0;
            try {
                dou_porcentaje_iva = (comprobante.getIva().doubleValue() * 100) / totalSinImpuestos;
            } catch (Exception e) {
            }
            parametros.put("TARIFA_IVA", TipoImpuestoIvaEnum.getPorcentaje(dou_porcentaje_iva));
            parametros.put("IVA_0", utilitario.getFormatoNumero(dou_base_tarifa0)); // +dou_base_no_objeto_iva  /29/08/2018
            parametros.put("IVA_NO_OBJETO", utilitario.getFormatoNumero(dou_base_no_objeto_iva)); //29/08/2018
        } catch (Exception e) {
        }
        return parametros;
    }

}
