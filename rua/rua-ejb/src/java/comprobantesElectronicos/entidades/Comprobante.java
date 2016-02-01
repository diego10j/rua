/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import framework.aplicacion.TablaGenerica;
import java.io.Serializable;
import java.util.Date;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author dfjacome
 */
public class Comprobante implements Serializable {

    private Long codigocomprobante;
    private Integer tipoemision;
    // private Integer persona;
    private String claveacceso;
    private String coddoc;
    private String estab;
    private String ptoemi;
    private String secuencial;
    private String ide_cccfa;
    private Date fechaemision;
    private String numAutorizacion;
    private Tipocomprobante codigotipcomp;
    private Firma codigofirma;
    private Estadocomprobante codigoestado;
    private Emisor codigoemisor;
    private Clavecontingencia codigoclave;
    private String identificacion;
    private Date fechaautoriza;

    public Comprobante() {
    }

    public Comprobante(TablaGenerica comprobante) {
        asignarComprobante(comprobante, 0);
    }

    public Comprobante(TablaGenerica comprobante, int index) {
        asignarComprobante(comprobante, index);
    }

    private void asignarComprobante(TablaGenerica comprobante, int index) {
        if (comprobante.isEmpty() == false) {
            Utilitario utilitario = new Utilitario();
            this.codigocomprobante = new Long(comprobante.getValor(index, "ide_srcom"));
            this.tipoemision = new Integer(comprobante.getValor(index, "tipoemision_srcom"));
            this.claveacceso = comprobante.getValor(index, "claveacceso_srcom");
            this.coddoc = comprobante.getValor(index, "coddoc_srcom");
            this.estab = comprobante.getValor(index, "estab_srcom");
            this.ptoemi = comprobante.getValor(index, "ptoemi_srcom");
            this.secuencial = comprobante.getValor(index, "secuencial_srcom");
            this.ide_cccfa = comprobante.getValor(index, "ide_cccfa");
            this.fechaemision = utilitario.getFecha(comprobante.getValor(index, "fechaemision_srcom"));
            this.numAutorizacion = comprobante.getValor(index, "autorizacion_srcom");
            this.identificacion = comprobante.getValor(index, "identificacion_srcom");
            if (comprobante.getValor(index, "coddoc_srcom") != null) {
                this.codigotipcomp = new Tipocomprobante(comprobante.getValor(index, "coddoc_srcom"));
            }
            if (comprobante.getValor(index, "ide_srfid") != null) {
                this.codigofirma = new Firma(new Integer(comprobante.getValor(index, "ide_srfid")));
            }
            if (comprobante.getValor(index, "ide_sresc") != null) {
                this.codigoestado = new Estadocomprobante(comprobante.getValor(index, "ide_sresc"));
            }
            if (comprobante.getValor(index, "ide_srclc") != null) {
                this.codigoclave = new Clavecontingencia(new Long(comprobante.getValor(index, "ide_srclc")));
            }
            this.fechaautoriza = utilitario.getFecha(comprobante.getValor(index, "fechaautoriza_srcom"));
        }
    }

    public Long getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Long codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }

    public Integer getTipoemision() {
        return tipoemision;
    }

    public void setTipoemision(Integer tipoemision) {
        this.tipoemision = tipoemision;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public String getCoddoc() {
        return coddoc;
    }

    public void setCoddoc(String coddoc) {
        this.coddoc = coddoc;
    }

    public String getEstab() {
        return estab;
    }

    public void setEstab(String estab) {
        this.estab = estab;
    }

    public String getPtoemi() {
        return ptoemi;
    }

    public void setPtoemi(String ptoemi) {
        this.ptoemi = ptoemi;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public Date getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(Date fechaemision) {
        this.fechaemision = fechaemision;
    }

    public Tipocomprobante getCodigotipcomp() {
        return codigotipcomp;
    }

    public void setCodigotipcomp(Tipocomprobante codigotipcomp) {
        this.codigotipcomp = codigotipcomp;
    }

    public Firma getCodigofirma() {
        return codigofirma;
    }

    public void setCodigofirma(Firma codigofirma) {
        this.codigofirma = codigofirma;
    }

    public Estadocomprobante getCodigoestado() {
        return codigoestado;
    }

    public void setCodigoestado(Estadocomprobante codigoestado) {
        this.codigoestado = codigoestado;
    }

    public Emisor getCodigoemisor() {
        return codigoemisor;
    }

    public void setCodigoemisor(Emisor codigoemisor) {
        this.codigoemisor = codigoemisor;
    }

    public Clavecontingencia getCodigoclave() {
        return codigoclave;
    }

    public void setCodigoclave(Clavecontingencia codigoclave) {
        this.codigoclave = codigoclave;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public Date getFechaautoriza() {
        return fechaautoriza;
    }

    public void setFechaautoriza(Date fechaautoriza) {
        this.fechaautoriza = fechaautoriza;
    }

    public String getIde_cccfa() {
        return ide_cccfa;
    }

    public void setIde_cccfa(String ide_cccfa) {
        this.ide_cccfa = ide_cccfa;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

}
