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
    // private String direstablecimiento; //Direccion de la oficina
    // private String guiaremision;
    private String numAutorizacion;
    // private BigDecimal totalsinimpuestos;
    // private BigDecimal totaldescuento;
    // private BigDecimal propina;
    // private BigDecimal importetotal;
    // private String moneda;
    // private String periodofiscal;
    // private String rise;
    // private String coddocmodificado;
    // private String numdocmodificado;
    // private Date fechaemisiondocsustento;
    // private BigDecimal valormodificacion;
    private Tipocomprobante codigotipcomp;
    private Firma codigofirma;
    private Estadocomprobante codigoestado;
    private Emisor codigoemisor;
    private Clavecontingencia codigoclave;
    // private String oficina; ///!!!!!!BORRAR
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
            //   this.persona = new Integer(comprobante.getValor(index, "ide_geper"));
            this.estab = comprobante.getValor(index, "estab_srcom");
            this.ptoemi = comprobante.getValor(index, "ptoemi_srcom");
            this.secuencial = comprobante.getValor(index, "secuencial_srcom");
            this.ide_cccfa = comprobante.getValor(index, "ide_cccfa");
            this.fechaemision = utilitario.getFecha(comprobante.getValor(index, "fechaemision_srcom"));
            // this.direstablecimiento = comprobante.getValor(index, "direstablecimiento_srcom");
            // this.guiaremision = comprobante.getValor(index, "guiaremision_srcom");
            // this.totalsinimpuestos = new BigDecimal(comprobante.getValor(index, "totalsinimpuestos_srcom"));
            // this.totaldescuento = new BigDecimal(comprobante.getValor(index, "totaldescuento_srcom"));
            // this.propina = new BigDecimal(comprobante.getValor(index, "propina_srcom"));
            // this.importetotal = new BigDecimal(comprobante.getValor(index, "importetotal_srcom"));
            // this.moneda = comprobante.getValor(index, "moneda_srcom");
            //this.periodofiscal = comprobante.getValor(index, "periodofiscal_srcom");
            //this.rise = comprobante.getValor(index, "rise_srcom");
            // this.coddocmodificado = comprobante.getValor(index, "coddocmodificado_srcom");
            // this.numdocmodificado = comprobante.getValor(index, "numdocmodificado_srcom");
            //  this.fechaemisiondocsustento = utilitario.getFecha(comprobante.getValor(index, "fechaemisiondocsustento_srcom"));
            //  this.valormodificacion = new BigDecimal(comprobante.getValor(index, "valormodificacion_srcom"));
            this.numAutorizacion = comprobante.getValor(index, "autorizacion_srcom");
            // this.oficina = String.valueOf(resultado.getInt("va_oficina"));
            if (comprobante.getValor(index, "ide_cntdo") != null) {
                this.codigotipcomp = new Tipocomprobante(comprobante.getValor(index, "ide_cntdo"));
            }
            if (comprobante.getValor(index, "ide_srfid") != null) {
                this.codigofirma = new Firma(new Integer(comprobante.getValor(index, "ide_srfid")));
            }
            if (comprobante.getValor(index, "ide_sresc") != null) {
                this.codigoestado = new Estadocomprobante(comprobante.getValor(index, "ide_sresc"));
            }
            //this.codigoemisor = codigoemisor;

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

//    public String getDirestablecimiento() {
//        return direstablecimiento;
//    }
//
//    public void setDirestablecimiento(String direstablecimiento) {
//        this.direstablecimiento = direstablecimiento;
//    }
//
//    public String getGuiaremision() {
//        return guiaremision;
//    }
//
//    public void setGuiaremision(String guiaremision) {
//        this.guiaremision = guiaremision;
//    }
//
//    public BigDecimal getTotalsinimpuestos() {
//        return totalsinimpuestos;
//    }
//
//    public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
//        this.totalsinimpuestos = totalsinimpuestos;
//    }
//
//    public BigDecimal getTotaldescuento() {
//        return totaldescuento;
//    }
//
//    public void setTotaldescuento(BigDecimal totaldescuento) {
//        this.totaldescuento = totaldescuento;
//    }
//
//    public BigDecimal getPropina() {
//        return propina;
//    }
//
//    public void setPropina(BigDecimal propina) {
//        this.propina = propina;
//    }
//
//    public BigDecimal getImportetotal() {
//        return importetotal;
//    }
//
//    public void setImportetotal(BigDecimal importetotal) {
//        this.importetotal = importetotal;
//    }
//
//    public String getMoneda() {
//        return moneda;
//    }
//
//    public void setMoneda(String moneda) {
//        this.moneda = moneda;
//    }
//
//    public String getPeriodofiscal() {
//        return periodofiscal;
//    }
//
//    public void setPeriodofiscal(String periodofiscal) {
//        this.periodofiscal = periodofiscal;
//    }
//
//    public String getRise() {
//        return rise;
//    }
//
//    public void setRise(String rise) {
//        this.rise = rise;
//    }
//
//    public String getCoddocmodificado() {
//        return coddocmodificado;
//    }
//
//    public void setCoddocmodificado(String coddocmodificado) {
//        this.coddocmodificado = coddocmodificado;
//    }
//
//    public String getNumdocmodificado() {
//        return numdocmodificado;
//    }
//
//    public void setNumdocmodificado(String numdocmodificado) {
//        this.numdocmodificado = numdocmodificado;
//    }
//
//    public Date getFechaemisiondocsustento() {
//        return fechaemisiondocsustento;
//    }
//
//    public void setFechaemisiondocsustento(Date fechaemisiondocsustento) {
//        this.fechaemisiondocsustento = fechaemisiondocsustento;
//    }
//
//    public BigDecimal getValormodificacion() {
//        return valormodificacion;
//    }
//
//    public void setValormodificacion(BigDecimal valormodificacion) {
//        this.valormodificacion = valormodificacion;
//    }
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

//    public String getOficina() {
//        return oficina;
//    }
//
//    public void setOficina(String oficina) {
//        this.oficina = oficina;
//    }
    public Date getFechaautoriza() {
        return fechaautoriza;
    }

    public void setFechaautoriza(Date fechaautoriza) {
        this.fechaautoriza = fechaautoriza;
    }

//    public Integer getPersona() {
//        return persona;
//    }
//
//    public void setPersona(Integer persona) {
//        this.persona = persona;
//    }
}
