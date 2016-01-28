/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;

/**
 *
 * @author dfjacome
 */
public class Tipocomprobante implements Serializable {

    private Integer codigotipcomp;
    private String nombretipcomp;
    private String alternotipcomp;

    public Tipocomprobante(String alternotipcomp) {
        this.alternotipcomp = alternotipcomp;
    }

    public Tipocomprobante() {
    }

    public Tipocomprobante(Integer codigotipcomp, String nombretipcomp, String alternotipcomp) {
        alternotipcomp = alternotipcomp == null ? null : alternotipcomp.trim();
        nombretipcomp = nombretipcomp == null ? null : nombretipcomp.trim();
        this.nombretipcomp = nombretipcomp;
        this.codigotipcomp = codigotipcomp;
        this.alternotipcomp = alternotipcomp;
    }

    public Tipocomprobante(String nombretipcomp, String alternotipcomp) {
        alternotipcomp = alternotipcomp == null ? null : alternotipcomp.trim();
        nombretipcomp = nombretipcomp == null ? null : nombretipcomp.trim();
        this.nombretipcomp = nombretipcomp;

        this.alternotipcomp = alternotipcomp;
    }

    public String getNombretipcomp() {
        return nombretipcomp;
    }

    public void setNombretipcomp(String nombretipcomp) {
        this.nombretipcomp = nombretipcomp;
    }

    public String getAlternotipcomp() {
        return alternotipcomp;
    }

    public void setAlternotipcomp(String alternotipcomp) {
        this.alternotipcomp = alternotipcomp;
    }

    public Integer getCodigotipcomp() {
        return codigotipcomp;
    }

    public void setCodigotipcomp(Integer codigotipcomp) {
        this.codigotipcomp = codigotipcomp;
    }

}
