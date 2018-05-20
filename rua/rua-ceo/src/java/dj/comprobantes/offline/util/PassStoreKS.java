/*
 *********************************************************************
 Objetivo: Clase que implementa la seguridad de la firma electronica
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.util;

import es.mityc.javasign.pkstore.IPassStoreKS;
import java.security.cert.X509Certificate;

/**
 *
 * @author diego.jacome
 */
public class PassStoreKS implements IPassStoreKS {

    /**
     * Contraseña de acceso al almacén.
     */
    private final transient String password;

    /**
     * <p>
     * Crea una instancia con la contraseña que se utilizará con el almacén
     * relacionado.</p>
     *
     * @param pass Contraseña del almacén
     */
    public PassStoreKS(final String pass) {
        this.password = pass;
    }

    /**
     * <p>
     * Devuelve la contraseña configurada para este almacén.</p>
     *
     * @param certificate No se utiliza
     * @param alias no se utiliza
     * @return contraseña configurada para este almacén
     * @see
     * es.mityc.javasign.pkstore.IPassStoreKS#getPassword(java.security.cert.X509Certificate,
     * java.lang.String)
     */
    @Override
    public char[] getPassword(final X509Certificate certificate, final String alias) {
        return password.toCharArray();
    }
}
