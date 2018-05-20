/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dao.ComprobanteDAO;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.DetalleComprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.EstadoUsuarioEnum;
import dj.comprobantes.offline.enums.ParametrosSistemaEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author djacome
 */
@Stateless
public class CPanelServiceImp implements CPanelService {

    @EJB
    private ArchivoService archivoService;

    @EJB
    private ComprobanteDAO comprobanteDAO;

    @Override
    public boolean guardarComprobanteNube(Comprobante comprobante) throws GenericException {
        boolean guardo = true;
        UtilitarioCeo utilitario = new UtilitarioCeo();
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("NOMBRE_USUARIO", comprobante.getCliente().getNombreCliente());
        params.put("IDENTIFICACION_USUARIO", comprobante.getCliente().getIdentificacion());
        params.put("CORREO_USUARIO", comprobante.getCliente().getCorreo());
        params.put("CODIGO_ESTADO", EstadoUsuarioEnum.NUEVO.getCodigo());
        params.put("DIRECCION_USUARIO", comprobante.getCliente().getDireccion());
        params.put("PK_CODIGO_COMP", comprobante.getCodigocomprobante());
        params.put("CODIGO_DOCUMENTO", comprobante.getCoddoc());
        params.put("ESTADO", EstadoComprobanteEnum.AUTORIZADO.getDescripcion());
        params.put("CLAVE_ACCESO", comprobante.getClaveacceso());
        params.put("SECUENCIAL", comprobante.getSecuencial());
        params.put("FECHA_EMISION", utilitario.getFormatoFecha(comprobante.getFechaemision()));
        params.put("NUM_AUTORIZACION", comprobante.getNumAutorizacion());
        params.put("FECHA_AUTORIZACION", utilitario.getFormatoFecha(comprobante.getFechaautoriza()));
        params.put("ESTABLECIM", comprobante.getEstab());
        params.put("PTO_EMISION", comprobante.getPtoemi());
        params.put("TOTAL", comprobante.getImportetotal());
        params.put("CODIGO_EMPR", comprobante.getOficina()); //02-02-2018  //Sucursal 
        params.put("CORREO_DOCUMENTO", comprobante.getCorreo()); //Para guardar el correo que se envio el comprobante
        byte[] bxml = archivoService.getXml(comprobante);
        StringBuilder postData = new StringBuilder();
        postData.append("{");
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 1) {
                postData.append(',');
            }
            postData.append("\"").append(param.getKey()).append("\"");
            postData.append(":\"");
            postData.append(String.valueOf(param.getValue())).append("\"");
        }
        String encodedfileXML = new String(Base64.encodeBase64(bxml));
        params.put("ARCHIVO_XML", encodedfileXML);

        //guarda en la nuebe el comprobante AUTORIZADO
        String filefield = "pdf";
        String fileMimeType = "application/pdf";
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";
        String result = "";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String fileName = comprobante.getClaveacceso() + ".pdf";
        try {
            byte[] bpdf = archivoService.getPdf(comprobante);
            File file = File.createTempFile(comprobante.getClaveacceso(), ".tmp");
            file.deleteOnExit();
            try (FileOutputStream outputStream1 = new FileOutputStream(file);) {
                outputStream1.write(bpdf);  //write the bytes and your done. 
                outputStream1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(ParametrosSistemaEnum.CPANEL_WEB_COMPROBANTE.getCodigo());
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + fileName + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = String.valueOf(params.get(key));
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            if (200 != connection.getResponseCode()) {
                throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (inputStream)));

            String output;
            while ((output = br.readLine()) != null) {
                result += output;
            }
            System.out.println(result);
            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            //CAMBIA DE ESTADO A GUARDDADO EN LA NUBE
            comprobante.setEnNube(true);
            comprobanteDAO.actualizar(comprobante);

        } catch (Exception e) {
            guardo = false;
            throw new GenericException(e);
        }
        if (guardo) {
            //Guarda el detalle de la factura
            if (comprobante.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo())) {
                for (DetalleComprobante detActual : comprobante.getDetalle()) {
                    guardarDetalleComprobanteNube(detActual);
                }
            }
        }
        return guardo;
    }

    @Override
    public void subirComprobantesPendientes() throws GenericException {
        List<Comprobante> lisCompPendientes = comprobanteDAO.getComprobantesAutorizadosNoNube();
        for (Comprobante comprobanteActual : lisCompPendientes) {
            guardarComprobanteNube(comprobanteActual);
        }
    }

    @Override
    public void reenviarComprobante(String correo, Long id) throws GenericException {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("PK_CODIGO_COMP", id);
        params.put("CORREO_USUARIO", correo);
        StringBuilder postData = new StringBuilder();
        try {
            URL url = new URL(ParametrosSistemaEnum.CPANEL_WEB_REENVIAR.getCodigo());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json;odata=verbose");
            conn.setRequestProperty("Authorization", "AccesToken");
            postData.append("{");
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 1) {
                    postData.append(',');
                }
                postData.append("\"").append(param.getKey()).append("\"");
                postData.append(":\"");
                postData.append(String.valueOf(param.getValue())).append("\"");
            }
            postData.append("}");
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(postDataBytes);
            os.flush();
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (IOException | RuntimeException e) {
            throw new GenericException(e);
        }

    }

    @Override
    public void guardarDetalleComprobanteNube(DetalleComprobante detalleComprobante) throws GenericException {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("PK_CODIGO_COMP", detalleComprobante.getComprobante().getCodigocomprobante());
        params.put("CODIGO_AUXILIAR", detalleComprobante.getCodigoauxiliar());
        params.put("CODIGO_PRINCIPAL", detalleComprobante.getCodigoprincipal());
        params.put("DESCRIPCION", detalleComprobante.getDescripciondet());
        params.put("PORCENTAJE_IVA", detalleComprobante.getPorcentajeiva());
        params.put("CANTIDAD", detalleComprobante.getCantidad());
        params.put("PRECIO", detalleComprobante.getPreciounitario());
        params.put("DESCUENTO", detalleComprobante.getDescuento());
        params.put("TOTAL", detalleComprobante.getPreciototalsinimpuesto());
        StringBuilder postData = new StringBuilder();
        try {
            URL url = new URL(ParametrosSistemaEnum.CPANEL_WEB_DETALLE_COMPROBANTE.getCodigo());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json;odata=verbose");
            conn.setRequestProperty("Authorization", "AccesToken");
            postData.append("{");
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 1) {
                    postData.append(',');
                }
                postData.append("\"").append(param.getKey()).append("\"");
                postData.append(":\"");
                postData.append(String.valueOf(param.getValue())).append("\"");
            }
            postData.append("}");
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(postDataBytes);
            os.flush();
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (IOException | RuntimeException e) {
            throw new GenericException(e);
        }
    }

}
