/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;

/**
 *
 * @author dfjacome
 */
public class Estadocomprobante implements Serializable {

    private String codigoestado;
    private String nombreestado;

    public Estadocomprobante() {
    }

    public Estadocomprobante(String codigoestado) {
        this.codigoestado = codigoestado;
    }

    public Estadocomprobante(String codigoestado, String nombreestado) {
        codigoestado = codigoestado == null ? codigoestado : codigoestado.trim();
        this.codigoestado = codigoestado;
        this.nombreestado = nombreestado;
    }

    public String getCodigoestado() {
        return codigoestado;
    }

    public void setCodigoestado(String codigoestado) {
        this.codigoestado = codigoestado;
    }

    public String getNombreestado() {
        return nombreestado;
    }

    public void setNombreestado(String nombreestado) {
        this.nombreestado = nombreestado;
    }

}
