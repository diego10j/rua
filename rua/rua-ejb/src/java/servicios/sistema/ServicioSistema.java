/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.sistema;

import framework.aplicacion.TablaGenerica;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import javax.ejb.Stateless;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sistema.aplicacion.Utilitario;


/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioSistema {

    private final Utilitario utilitario = new Utilitario();

    /**
     * Retorna Datos de la Empresa
     *
     * @return
     */
    public TablaGenerica getEmpresa() {
        return getEmpresa(utilitario.getVariable("IDE_EMPR"));
    }

    /**
     * Retorna Datos de una Empresa
     *
     * @param ide_empr
     * @return
     */
    public TablaGenerica getEmpresa(String ide_empr) {
        return utilitario.consultar("SELECT * from sis_empresa where ide_empr=" + ide_empr);
    }

    /**
     * Retorna el Logo de la empresa en forma StreamedContent
     *
     * @return
     */
    public StreamedContent getLogoEmpresa() {
        StreamedContent stream = null;
        try {
            TablaGenerica tabEmpresa = getEmpresa();
            if (tabEmpresa.isEmpty() == false) {
                try (InputStream myInputStream = new ByteArrayInputStream((byte[]) tabEmpresa.getValorObjeto("LOGO_EMPR"))) {
                    myInputStream.mark(0);
                    String mimeType = URLConnection.guessContentTypeFromStream(myInputStream);
                    stream = new DefaultStreamedContent(myInputStream, mimeType);                   
                    myInputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * Retorna los datos de una Sucursal
     *
     * @param ide_sucu
     * @return
     */
    public TablaGenerica getSucursal(String ide_sucu) {
        return utilitario.consultar("SELECT * from sis_sucursal where ide_sucu=" + ide_sucu);
    }

    /**
     * Retorna los datos de la Sucursal
     *
     * @return
     */
    public TablaGenerica getSucursal() {
        return getSucursal(utilitario.getVariable("IDE_SUCU"));
    }

}
