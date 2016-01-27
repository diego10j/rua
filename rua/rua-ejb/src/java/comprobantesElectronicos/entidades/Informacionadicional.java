/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;

/**
 *
 * @author dfjacome
 */
public class Informacionadicional implements Serializable {

    private Long codigoinfadi;
    private String nombreinfadi;
    private String descinfadi;
    private Comprobante codigocomprobante;

    public Informacionadicional() {
    }

    public Informacionadicional(Long codigoinfadi) {
        this.codigoinfadi = codigoinfadi;
    }

    public Informacionadicional(Long codigoinfadi, String nombreinfadi, String descinfadi) {
        this.codigoinfadi = codigoinfadi;
        this.nombreinfadi = nombreinfadi;
        this.descinfadi = descinfadi;
    }

    public Long getCodigoinfadi() {
        return codigoinfadi;
    }

    public void setCodigoinfadi(Long codigoinfadi) {
        this.codigoinfadi = codigoinfadi;
    }

    public String getNombreinfadi() {
        return nombreinfadi;
    }

    public void setNombreinfadi(String nombreinfadi) {
        this.nombreinfadi = nombreinfadi;
    }

    public String getDescinfadi() {
        return descinfadi;
    }

    public void setDescinfadi(String descinfadi) {
        this.descinfadi = descinfadi;
    }

    public Comprobante getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Comprobante codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }

}
