/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;

/**
 *
 * @author dfjacome
 */
public class Tipodocumento implements Serializable {

    private String tipo;

    private String alternotipo;

    public Tipodocumento() {
    }

    public Tipodocumento(String tipo, String alternotipo) {
        this.tipo = tipo;
        this.alternotipo = alternotipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAlternotipo() {
        return alternotipo;
    }

    public void setAlternotipo(String alternotipo) {
        this.alternotipo = alternotipo;
    }

}
