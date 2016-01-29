/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.ejb;

import comprobantesElectronicos.dao.FirmaDAOLocal;
import comprobantesElectronicos.entidades.Firma;
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
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
@Stateless
public class ejbFirmarDocumento {

    @EJB
    private FirmaDAOLocal firmaDAO;

    private final Utilitario utilitario = new Utilitario();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /**
     * Firma Electronicamente a un documento xml
     *
     * @param firma
     * @param document documneto XML
     * @return Documento firmado
     */
    public Document getDocumentoFirmado(Firma firma, Document document) {
        String idcertified = firma.getRutafirma();
        String idpassword = firma.getClavefirma();
        IPKStoreManager storeManager = null;
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new java.io.FileInputStream(idcertified), idpassword.toCharArray());
            storeManager = new KSStore(ks, new PassStoreKS(idpassword));
        } catch (Exception ex) {
            System.out.println("No se puede generar KeyStore PKCS12 :" + ex.getMessage());
        }
        if (storeManager == null) {
            System.out.println("El gestor de claves no se ha obtenido correctamente.");
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
            e.printStackTrace();
        }

        //Recupera el primero de los certificados del almacén.
        X509Certificate certificate = null;
        try {
            certificate = (X509Certificate) ks.getCertificate(alias);
        } catch (KeyStoreException ex) {
            System.out.println("Fallo obteniendo listado de certificados");
        }

        if (certificate == null) {
            System.out.println("No existe ningún certificado para firmar.");
        }

        // Obtención de la clave privada asociada al certificado
        PrivateKey privateKey = null;
        KeyStore tmpKs = ks;
        try {
            privateKey = (PrivateKey) tmpKs.getKey(alias, idpassword.toCharArray());
        } catch (UnrecoverableKeyException e) {
            System.out.println("No existe clave privada para firmar.");
        } catch (KeyStoreException e) {
            System.out.println("No existe clave privada para firmar.");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No existe clave privada para firmar.");
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
            System.out.println("Error a establecer los parametros de SRI");
        }

        FirmaXML firmaXML = new FirmaXML();
        Document docSigned = null;
        try {
            //Creación del objeto encargado de realizar la firma
            Logger.getRootLogger().setLevel(Level.OFF);
            Object[] res = firmaXML.signFile(certificate, dataToSign, privateKey, provider);
            docSigned = (Document) res[0];
        } catch (Exception ex) {
            System.out.println("Error realizando la firma :" + ex.getMessage());
        }
        return docSigned;
    }

    /**
     * Validamos el archivo de la firma digital y su clave de acceso, se valida
     * la fecha de expiración de la firma
     *
     * @param firma
     * @return retorna "" si todo valido correctamente, caso contrario retorna
     * el mensaje de error
     */
    public String validaCertificado(Firma firma) {

        String mensaje = null;
        String idcertified = firma.getRutafirma();
        String idpassword = firma.getClavefirma();

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            //  ks.load(firma.getArchivo(), idpassword.toCharArray());
            ks.load(new java.io.FileInputStream(idcertified), idpassword.toCharArray());
            IPKStoreManager storeManager;
            storeManager = new KSStore(ks, new PassStoreKS(idpassword));
            Date dat_fecha_expira = null;
            for (Enumeration e = ks.aliases(); e.hasMoreElements();) {
                String alias = (String) e.nextElement();
                if (ks.getCertificate(alias).getType().equals("X.509")) {
                    dat_fecha_expira = ((X509Certificate) ks.getCertificate(alias)).getNotAfter();
                    break;
                }
            }
            Date dat_fecha_actual = new Date();
            System.out.println(dat_fecha_expira + "   FECHA EXPIRA");
            if (utilitario.isFechaMayor(dat_fecha_actual, dat_fecha_expira)) {
                mensaje = "La firma digital expiró el " + utilitario.getFormatoFecha(dat_fecha_expira);
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException ex) {
            mensaje = ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            mensaje = "No se puede acceder a la firma digital, la ruta o la clave son incorrectas";
        }
        return mensaje;
    }

    public Firma getFirmaDisponible() {
        Firma firma = firmaDAO.getFirmaDisponible();
        if (firma == null) {
            return null;
        }
        return firma;
    }
}
