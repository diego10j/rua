/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;

/**
 *
 * @author dfjacome
 */
public class Clavecontingencia implements Serializable {

    private Long codigoclave;

    private String clavecont;

    private boolean disponiblecont;

    public Clavecontingencia() {
    }

    public Clavecontingencia(Long codigoclave) {
        this.codigoclave = codigoclave;
    }

    public Clavecontingencia(Long codigoclave, String clavecont, boolean disponiblecont) {
        this.codigoclave = codigoclave;
        this.clavecont = clavecont;
        this.disponiblecont = disponiblecont;
    }

    public Long getCodigoclave() {
        return codigoclave;
    }

    public void setCodigoclave(Long codigoclave) {
        this.codigoclave = codigoclave;
    }

    public String getClavecont() {
        return clavecont;
    }

    public void setClavecont(String clavecont) {
        this.clavecont = clavecont;
    }

    public boolean getDisponiblecont() {
        return disponiblecont;
    }

    public void setDisponiblecont(boolean disponiblecont) {
        this.disponiblecont = disponiblecont;
    }

}
