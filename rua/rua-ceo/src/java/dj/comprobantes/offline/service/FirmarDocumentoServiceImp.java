/*
 *********************************************************************
 Objetivo: Servicio que implementa interface FirmarDocumentoService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dao.FirmaDAO;
import dj.comprobantes.offline.dto.Firma;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.PassStoreKS;
import dj.comprobantes.offline.util.UtilitarioCeo;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.pkstore.IPKStoreManager;
import es.mityc.javasign.pkstore.keystore.KSStore;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class FirmarDocumentoServiceImp implements FirmarDocumentoService {

    @EJB
    private FirmaDAO firmaDAO;
    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public Document getDocumentoFirmado(Document document, String sucursal) throws GenericException {
        Firma firma = firmaDAO.getFirmaDisponible(sucursal);
        Document docSigned = null;
        if (validaCertificado(firma)) {
            String idcertified = firma.getRutafirma();
            String idpassword = firma.getClavefirma();
            IPKStoreManager storeManager = null;
            KeyStore ks = null;
            try {
                ks = KeyStore.getInstance("PKCS12");
                ks.load(new java.io.FileInputStream(idcertified), idpassword.toCharArray());
                storeManager = new KSStore(ks, new PassStoreKS(idpassword));
            } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
                throw new GenericException("ERROR. No se puede generar KeyStore PKCS12", ex);
            }
            if (storeManager == null) {
                throw new GenericException("ERROR. El gestor de claves no se ha obtenido correctamente");
            }
            //lee el certificado
            String alias = null;
            Enumeration nombres;
            try {
                nombres = ks.aliases();
                while (nombres.hasMoreElements()) {
                    String tmpAlias = (String) nombres.nextElement();
                    if (ks.isKeyEntry(tmpAlias)) {
                        alias = tmpAlias;
                    }
                }
            } catch (KeyStoreException e) {
                throw new GenericException(e);
            }

            //Recupera el primero de los certificados del almacén.
            X509Certificate certificate = null;
            try {
                certificate = (X509Certificate) ks.getCertificate(alias);
            } catch (KeyStoreException ex) {
                throw new GenericException("ERROR. Fallo obteniendo listado de certificados", ex);
            }

            if (certificate == null) {
                throw new GenericException("No existe ningún certificado para firmar");
            }

            // Obtención de la clave privada asociada al certificado
            PrivateKey privateKey = null;
            KeyStore tmpKs = ks;
            try {
                privateKey = (PrivateKey) tmpKs.getKey(alias, idpassword.toCharArray());
            } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
                throw new GenericException("ERROR. No existe clave privada para firmar", e);
            }
            // Obtención del provider encargado de las labores criptográficas
            Provider provider = ks.getProvider();
            DataToSign dataToSign = new DataToSign();
            try {
                dataToSign.setXadesFormat(EnumFormatoFirma.XAdES_BES);
                dataToSign.setEsquema(XAdESSchemas.XAdES_132);
                dataToSign.setXMLEncoding("UTF-8");
                dataToSign.setEnveloped(true);
                dataToSign.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "Comprobante Electronico", null, "text/xml", null));
                dataToSign.setParentSignNode("comprobante");
                dataToSign.setDocument(document);
            } catch (Exception ex) {
                throw new GenericException("ERROR. Error a establecer los parametros de SRI", ex);
            }
            FirmaXML firmaXML = new FirmaXML();
            try {
                //Creación del objeto encargado de realizar la firma
                Logger.getRootLogger().setLevel(Level.OFF);
                Object[] res = firmaXML.signFile(certificate, dataToSign, privateKey, provider);
                docSigned = (Document) res[0];
            } catch (Exception ex) {
                throw new GenericException("ERROR. Error realizando la firma ", ex);
            }
        }
        return docSigned;
    }

    @Override
    public boolean validaCertificado(Firma firma) throws GenericException {
        String idcertified = firma.getRutafirma();
        String idpassword = firma.getClavefirma();
        Date dat_fecha_expira = null;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new java.io.FileInputStream(idcertified), idpassword.toCharArray());
            IPKStoreManager storeManager;
            storeManager = new KSStore(ks, new PassStoreKS(idpassword));
            for (Enumeration e = ks.aliases(); e.hasMoreElements();) {
                String alias = (String) e.nextElement();
                if (ks.getCertificate(alias).getType().equals("X.509")) {
                    dat_fecha_expira = ((X509Certificate) ks.getCertificate(alias)).getNotAfter();
                    break;
                }
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            throw new GenericException("ERROR. No se puede acceder a la firma digital, la ruta o la clave son incorrectas ", ex);
        }
        Date dat_fecha_actual = new Date();
        //  System.out.println("EXPIRA : " + Utilitario.getFormatoFecha(dat_fecha_expira));
        if (utilitario.isFechaMayor(dat_fecha_actual, dat_fecha_expira)) {
            throw new GenericException("ERROR. La firma Digital " + firma.getRutafirma() + " expiró el " + utilitario.getFormatoFecha(dat_fecha_expira));
        }
        return true;
    }

}
