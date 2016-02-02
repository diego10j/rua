/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.ejb;

import comprobantesElectronicos.dao.ComprobanteDAOLocal;
import comprobantesElectronicos.dao.SriComprobanteDAOLocal;
import comprobantesElectronicos.entidades.Comprobante;
import comprobantesElectronicos.entidades.Sricomprobante;

import framework.reportes.DetalleReporte;
import framework.reportes.GenerarReporte;
import framework.reportes.ReporteDataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;
import org.xml.sax.InputSource;
import servicios.sistema.ServicioSistema;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ejbReportes {

    private final Utilitario utilitario = new Utilitario();

    @EJB
    private ServicioSistema ser_sistema;
    @EJB
    private SriComprobanteDAOLocal sriComprobanteDAO;
    @EJB
    private ComprobanteDAOLocal comprobanteDAO;

    public void generarFacturaPDF(String ide_srcom) {
        Comprobante comprobante = comprobanteDAO.getComprobanteporCodigocomprobante(ide_srcom);
        Sricomprobante sri_comprobante = sriComprobanteDAO.getSriComprobanteActual(comprobante);
        String cadenaXML = sri_comprobante.getXmlcomprobante();
        Map parametros = getParametrosComunes(cadenaXML, comprobante);
        //Detalles Factura
        String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "detalles");
        //Forma un arreglo de todos los detalles
        String strDetalles[] = cadenaDetalles.split("</detalle>");
        List<DetalleReporte> lisDetalle = new ArrayList();

        //Info Adicional infoAdicional
        String cadenainfoAdicional = utilitario.getValorEtiqueta(cadenaXML, "infoAdicional");
        String strcampoAdicional[] = cadenainfoAdicional.split("</campoAdicional>");
        //Crea Collection de info Adicional     
        Collection col_name = null;
        for (int i = 0; i < strcampoAdicional.length; i++) {
            if (col_name == null) {
                col_name = new ArrayList();
            }
            String strcampo = strcampoAdicional[i];
            String name = strcampo.substring((strcampo.indexOf("nombre=\"") + 8), (strcampo.lastIndexOf(">") - 1));
            String valor = strcampo.substring(strcampo.lastIndexOf(">") + 1);
            //System.out.println(name + "     " + valor);
            col_name.add(name + " : " + valor);
        }
        parametros.put("col_name", col_name);
        //Recorre todos los detalles
        String columnas[] = {"codigoPrincipal", "codigoAuxiliar", "cantidad",
            "descripcion", "precioUnitario", "precioTotalSinImpuesto"};
        for (int i = 0; i < strDetalles.length; i++) {
            String strDetalleActual = strDetalles[i];
            Object valores[] = {utilitario.getValorEtiqueta(strDetalleActual, "codigoPrincipal"),
                utilitario.getValorEtiqueta(strDetalleActual, "codigoAuxiliar"),
                utilitario.getValorEtiqueta(strDetalleActual, "cantidad"),
                utilitario.getValorEtiqueta(strDetalleActual, "descripcion"),
                utilitario.getValorEtiqueta(strDetalleActual, "precioUnitario"),
                utilitario.getValorEtiqueta(strDetalleActual, "precioTotalSinImpuesto")
            };
            lisDetalle.add(new DetalleReporte(columnas, valores));
        }
        //Genera el reporte en PDF
        ReporteDataSource data = new ReporteDataSource(lisDetalle);
        GenerarReporte generarReporte = new GenerarReporte();
        generarReporte.setDataSource(data);
        // generarReporte.generarPDF(parametros, "/reportes/factura.jasper", parametros.get("CLAVE_ACC") + "");
        generarReporte.generar(parametros, "/reportes/rep_sri/comprobantes_electronicos/factura.jasper");
    }

    public void generarNotaCreditoPDF(String cadenaXML, Comprobante comprobante) {
        Map parametros = getParametrosComunes(cadenaXML, comprobante);
        parametros.put("DOC_MODIFICADO", utilitario.getValorEtiqueta(cadenaXML, "codDocModificado"));
        parametros.put("NUM_DOC_MODIFICADO", utilitario.getValorEtiqueta(cadenaXML, "numDocModificado"));
        parametros.put("FECHA_EMISION_DOC_SUSTENTO", utilitario.getValorEtiqueta(cadenaXML, "fechaEmisionDocSustento"));
        parametros.put("RAZON_MODIF", utilitario.getValorEtiqueta(cadenaXML, "NO HAY"));//no hay 

        //Detalles nota de credito
        String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "detalles");
        //Forma un arreglo de todos los detalles
        String strDetalles[] = cadenaDetalles.split("</detalle>");
        List<DetalleReporte> lisDetalle = new ArrayList();

        //Info Adicional infoAdicional
        String cadenainfoAdicional = utilitario.getValorEtiqueta(cadenaXML, "infoAdicional");
        String strcampoAdicional[] = cadenainfoAdicional.split("</campoAdicional>");
        //Crea Collection de info Adicional      
        Collection col_name = null;
        for (int i = 0; i < strcampoAdicional.length; i++) {
            if (col_name == null) {
                col_name = new ArrayList();
            }
            String strcampo = strcampoAdicional[i];
            String name = strcampo.substring((strcampo.indexOf("nombre=\"") + 8), (strcampo.lastIndexOf(">") - 1));
            String valor = strcampo.substring(strcampo.lastIndexOf(">") + 1);
            //System.out.println(name + "     " + valor);
            col_name.add(name + " : " + valor);
        }
        parametros.put("col_name", col_name);
        //Recorre todos los detalles
        String columnas[] = {"codigoPrincipal", "codigoAuxiliar", "cantidad",
            "descripcion", "precioUnitario", "precioTotalSinImpuesto"};
        for (int i = 0; i < strDetalles.length; i++) {
            String strDetalleActual = strDetalles[i];
            Object valores[] = {utilitario.getValorEtiqueta(strDetalleActual, "codigoPrincipal"),
                utilitario.getValorEtiqueta(strDetalleActual, "codigoAuxiliar"),
                utilitario.getValorEtiqueta(strDetalleActual, "cantidad"),
                utilitario.getValorEtiqueta(strDetalleActual, "descripcion"),
                utilitario.getValorEtiqueta(strDetalleActual, "precioUnitario"),
                utilitario.getValorEtiqueta(strDetalleActual, "precioTotalSinImpuesto")
            };
            lisDetalle.add(new DetalleReporte(columnas, valores));
        }
        //Genera el reporte en PDF
        ReporteDataSource data = new ReporteDataSource(lisDetalle);
        GenerarReporte generarReporte = new GenerarReporte();
        generarReporte.setDataSource(data);
        //  generarReporte.generarPDF(parametros, "/reportes/notaCredito.jasper", parametros.get("CLAVE_ACC") + "");
        generarReporte.generarPDF(parametros, "/reportes/notaCredito.jasper");
    }

    public void generarComprobanteRetencion(String cadenaXML, Comprobante comprobante) {
        Map parametros = getParametrosComunes(cadenaXML, comprobante);
        parametros.put("LLEVA_CONTABILIDAD", utilitario.getValorEtiqueta(cadenaXML, "obligadoContabilidad"));
        parametros.put("RS_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "razonSocialSujetoRetenido"));
        parametros.put("EJERCICIO_FISCAL", utilitario.getValorEtiqueta(cadenaXML, "periodoFiscal"));
        //Detalles Factura
        String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "impuestos");
        //Forma un arreglo de todos los detalles
        String strDetalles[] = cadenaDetalles.split("</impuesto>");
        List<DetalleReporte> lisDetalle = new ArrayList();

        //Info Adicional infoAdicional
        String cadenainfoAdicional = utilitario.getValorEtiqueta(cadenaXML, "infoAdicional");
        String strcampoAdicional[] = cadenainfoAdicional.split("</campoAdicional>");
        //Crea Collection de info Adicional     
        Collection col_name = null;
        for (int i = 0; i < strcampoAdicional.length; i++) {
            if (col_name == null) {
                col_name = new ArrayList();
            }
            String strcampo = strcampoAdicional[i];
            String name = strcampo.substring((strcampo.indexOf("nombre=\"") + 8), (strcampo.lastIndexOf(">") - 1));
            String valor = strcampo.substring(strcampo.lastIndexOf(">") + 1);
            //System.out.println(name + "     " + valor);
            col_name.add(name + " : " + valor);
        }
        parametros.put("col_name", col_name);
        //Recorre todos los detalles
        String columnas[] = {"baseImponible", "porcentajeRetener", "valorRetenido",
            "nombreImpuesto", "nombreComprobante", "numeroComprobante", "fechaEmisionCcompModificado"};
        for (int i = 0; i < strDetalles.length; i++) {
            String strDetalleActual = strDetalles[i];
            Object valores[] = {utilitario.getValorEtiqueta(strDetalleActual, "baseImponible"),
                utilitario.getValorEtiqueta(strDetalleActual, "porcentajeRetener"),
                utilitario.getValorEtiqueta(strDetalleActual, "valorRetenido"),
                utilitario.getValorEtiqueta(strDetalleActual, "codigoRetencion"),
                utilitario.getValorEtiqueta(strDetalleActual, "codDocSustento"),
                utilitario.getValorEtiqueta(strDetalleActual, "numDocSustento"),
                utilitario.getValorEtiqueta(strDetalleActual, "fechaEmisionDocSustento")
            };
            lisDetalle.add(new DetalleReporte(columnas, valores));
        }
        //Genera el reporte en PDF
        ReporteDataSource data = new ReporteDataSource(lisDetalle);
        GenerarReporte generarReporte = new GenerarReporte();
        generarReporte.setDataSource(data);
        // generarReporte.generarPDF(parametros, "/reportes/comprobanteRetencion.jasper", parametros.get("CLAVE_ACC") + "");
        generarReporte.generarPDF(parametros, "/reportes/comprobanteRetencion.jasper");
    }

    private Map getParametrosComunes(String cadenaXML, Comprobante comprobante) {
        Map parametros = new HashMap();
        try {
            //RECUPERA PARAMETROS DE LA CADENA XML PARA EL REPORTE
            parametros.put("RUC", utilitario.getValorEtiqueta(cadenaXML, "ruc"));
            parametros.put("NUM_AUT", comprobante.getNumAutorizacion());
            if (comprobante.getFechaautoriza() != null) {
                parametros.put("FECHA_AUT", getFormatoFecha(comprobante.getFechaautoriza()));
            }
            parametros.put("TIPO_EMISION", utilitario.getValorEtiqueta(cadenaXML, "tipoEmision"));
            parametros.put("CLAVE_ACC", utilitario.getValorEtiqueta(cadenaXML, "claveAcceso"));
//            InputStream stream = null;
//            try {
//                Emisor emisor = emisorDAO.getEmisor();
//                String path = emisor.getLogoempresa();
//                if (path != null && path.isEmpty() == false) {
//                    path = path.startsWith("/") ? path : "/" + path;
//                    stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path);
//                }
//            } catch (Exception e) {
//            }

            //Logo 
            try {

                parametros.put("LOGO", ser_sistema.getLogoEmpresa().getStream());//InputStream
            } catch (Exception e) {
                e.printStackTrace();
            }

            parametros.put("RAZON_SOCIAL", utilitario.getValorEtiqueta(cadenaXML, "razonSocial"));
            parametros.put("DIR_MATRIZ", utilitario.getValorEtiqueta(cadenaXML, "dirMatriz"));
            parametros.put("DIR_SUCURSAL", utilitario.getValorEtiqueta(cadenaXML, "dirEstablecimiento"));
            parametros.put("CONT_ESPECIAL", utilitario.getValorEtiqueta(cadenaXML, "contribuyenteEspecial"));
            parametros.put("LLEVA_CONTABILIDAD", utilitario.getValorEtiqueta(cadenaXML, "obligadoContabilidad"));
            parametros.put("RS_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "razonSocialComprador"));
            parametros.put("RUC_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "identificacionComprador"));
            if (comprobante.getFechaemision() != null) {
                parametros.put("FECHA_EMISION", getFormatoFecha(comprobante.getFechaemision()));
            }
            parametros.put("VALOR_TOTAL", utilitario.getValorEtiqueta(cadenaXML, "importeTotal"));//Object
            parametros.put("DESCUENTO", utilitario.getValorEtiqueta(cadenaXML, "totalDescuento"));
            String cadenaTotalImpuesto = utilitario.getValorEtiqueta(cadenaXML, "totalImpuesto");
            parametros.put("IVA", utilitario.getValorEtiqueta(cadenaTotalImpuesto, "valor"));
            parametros.put("IVA_0", "0,00");
            parametros.put("IVA_12", utilitario.getValorEtiqueta(cadenaTotalImpuesto, "baseImponible"));
            parametros.put("SUBTOTAL", utilitario.getValorEtiqueta(cadenaXML, "totalSinImpuestos"));
            parametros.put("NUM_FACT", utilitario.getValorEtiqueta(cadenaXML, "estab") + "-" + utilitario.getValorEtiqueta(cadenaXML, "ptoEmi") + "-" + utilitario.getValorEtiqueta(cadenaXML, "secuencial"));
            parametros.put("TOTAL_DESCUENTO", utilitario.getValorEtiqueta(cadenaXML, "totalDescuento"));
            parametros.put("AMBIENTE", utilitario.getValorEtiqueta(cadenaXML, "ambiente"));
            parametros.put("NOM_COMERCIAL", utilitario.getValorEtiqueta(cadenaXML, "nombreComercial"));

        } catch (Exception e) {
        }
        return parametros;
    }

    private String getFormatoFecha(Date fecha) {
        String fechaAut = utilitario.getFormatoFecha(fecha, "dd 'de' MMMM 'del' yyyy");
        if (fechaAut != null) {
            fechaAut = fechaAut.toUpperCase();
            fechaAut = fechaAut.replace("JANUARY", "ENERO");
            fechaAut = fechaAut.replace("FEBRUARY", "FEBRERO");
            fechaAut = fechaAut.replace("MARCH", "MARZO");
            fechaAut = fechaAut.replace("APRIL", "ABRIL");
            fechaAut = fechaAut.replace("MAY", "MAYO");
            fechaAut = fechaAut.replace("JUNE", "JUNIO");
            fechaAut = fechaAut.replace("JULY", "JULIO");
            fechaAut = fechaAut.replace("AUGUST", "AGOSTO");
            fechaAut = fechaAut.replace("SEPTEMBER", "SEPTIEMBRE");
            fechaAut = fechaAut.replace("OCTOBER", "OCTUBRE");
            fechaAut = fechaAut.replace("NOVEMBER", "NOVIEMBRE");
            fechaAut = fechaAut.replace("DECEMBER", "DICIEMBRE");
        }
        return fechaAut;
    }

    public void generarComprobanteXML(String ide_srcom) {
        try {
            Comprobante comprobante = comprobanteDAO.getComprobanteporCodigocomprobante(ide_srcom);
            Sricomprobante sri_comprobante = sriComprobanteDAO.getSriComprobanteActual(comprobante);
            String cadenaXML = sri_comprobante.getXmlcomprobante();

            //Crea el archivo y escribe el XML           
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(cadenaXML));
            Format format = Format.getPrettyFormat();
            XMLOutputter serializerxml = new XMLOutputter(format);
            String xmlInvoice = serializerxml.outputString(doc);
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xmlInvoice.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            InputStream myInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
            //Descargar archivo en el navegador
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            StreamedContent content = new DefaultStreamedContent(myInputStream, "text/xml", comprobante.getClaveacceso() + ".xml");
            externalContext.setResponseContentType(content.getContentType());
            externalContext.setResponseHeader("Content-Disposition", "attachment" + ";filename=\"" + content.getName() + "\"");
            externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
            byte[] buffer = new byte[2048];
            int length;
            InputStream inputStream = content.getStream();
            OutputStream outputStream = externalContext.getResponseOutputStream();
            while ((length = (inputStream.read(buffer))) != -1) {
                outputStream.write(buffer, 0, length);
            }
            externalContext.setResponseStatus(200);
            externalContext.responseFlushBuffer();
            content.getStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (JDOMException | IOException | IllegalArgumentException | TransformerException e) {
            System.out.println("ERROR al crear XML : " + e.getMessage());
        }
    }

//    public byte[] crearComprobanteXML(String claveAcceso) {
//        try {
//            Comprobante comprobante = comprobanteDAO.getComprobanteporClaveAcceso(claveAcceso);
//            if (comprobante == null) {
//                return null;
//            }
//            Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobante);
//            if (sriComprobante == null) {
//                return null;
//            }
//            //Crea el archivo y escribe el XML           
//            SAXBuilder builder = new SAXBuilder();
//            Document doc = builder.build(new StringReader(sriComprobante.getXmlcomprobante()));
//            Format format = Format.getPrettyFormat();
//            XMLOutputter serializerxml = new XMLOutputter(format);
//            String xmlInvoice = serializerxml.outputString(doc);
//            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
//            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xmlInvoice.getBytes())));
//            StreamResult res = new StreamResult(new ByteArrayOutputStream());
//            serializer.transform(xmlSource, res);
//            InputStream myInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
//            myInputStream.close();           
//            //System.out.println("XXXXX   :" + IOUtils.toByteArray(myInputStream));
//            return IOUtils.toByteArray(myInputStream);
//
//        } catch (Exception e) {
//            System.out.println("ERROR al crear XML : " + e.getMessage());
//        }
//        return null;
//    }
//
//    public byte[] crearComprobantePDF(String claveAcceso) {
//        Comprobante comprobante = comprobanteDAO.getComprobanteporClaveAcceso(claveAcceso);
//        if (comprobante == null) {
//            return null;
//        }
//        Sricomprobante sriComprobante = sriComprobanteDAO.getSriComprobanteActual(comprobante);
//        if (sriComprobante == null) {
//            return null;
//        }
//        String cadenaXML = sriComprobante.getXmlcomprobante();
//
//        Map parametros = getParametrosComunes(cadenaXML, comprobante);
//
//        //Detalles Factura
//        String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "detalles");
//        //Forma un arreglo de todos los detalles
//        String strDetalles[] = cadenaDetalles.split("</detalle>");
//        List<DetalleReporte> lisDetalle = new ArrayList();
//
//        //Info Adicional infoAdicional
//        String cadenainfoAdicional = utilitario.getValorEtiqueta(cadenaXML, "infoAdicional");
//        String strcampoAdicional[] = cadenainfoAdicional.split("</campoAdicional>");
//        //Crea Collection de info Adicional     
//        Collection col_name = null;
//        for (int i = 0; i < strcampoAdicional.length; i++) {
//            if (col_name == null) {
//                col_name = new ArrayList();
//            }
//            String strcampo = strcampoAdicional[i];
//            String name = strcampo.substring((strcampo.indexOf("nombre=\"") + 8), (strcampo.lastIndexOf(">") - 1));
//            String valor = strcampo.substring(strcampo.lastIndexOf(">") + 1);
//            //System.out.println(name + "     " + valor);
//            col_name.add(name + " : " + valor);
//        }
//        parametros.put("col_name", col_name);
//        //Recorre todos los detalles
//        String columnas[] = {"codigoPrincipal", "codigoAuxiliar", "cantidad",
//            "descripcion", "precioUnitario", "precioTotalSinImpuesto"};
//        for (int i = 0; i < strDetalles.length; i++) {
//            String strDetalleActual = strDetalles[i];
//            Object valores[] = {utilitario.getValorEtiqueta(strDetalleActual, "codigoPrincipal"),
//                utilitario.getValorEtiqueta(strDetalleActual, "codigoAuxiliar"),
//                utilitario.getValorEtiqueta(strDetalleActual, "cantidad"),
//                utilitario.getValorEtiqueta(strDetalleActual, "descripcion"),
//                utilitario.getValorEtiqueta(strDetalleActual, "precioUnitario"),
//                utilitario.getValorEtiqueta(strDetalleActual, "precioTotalSinImpuesto")
//            };
//            lisDetalle.add(new DetalleReporte(columnas, valores));
//        }
//        //Genera el reporte en PDF
//        ReporteDetalleDataSource data = new ReporteDetalleDataSource(lisDetalle);
//        GenerarReporte generarReporte = new GenerarReporte();
//        generarReporte.setDataSource(data);
//        File reporte = generarReporte.crearPDF(parametros, "/reportes/factura.jasper", parametros.get("CLAVE_ACC") + "");
//        try {
//            InputStream myInputStream = new FileInputStream(reporte);
//            myInputStream.close();
//            //Borra el archivo cuando termina el proceso
//            reporte.deleteOnExit();
//            return IOUtils.toByteArray(myInputStream);
//        } catch (Exception e) {
//            System.out.println("ERROR al crear PDF : " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }
}
