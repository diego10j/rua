/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author dfjacome
 */
public class Impuestocomprobante implements Serializable {

    private Long codigoimpuesto;
    private int codigo;
    private int codigoporcentaje;
    private BigDecimal valorimp;
    private BigDecimal descuentoadicional;
    private String codigoretencion;
    private BigDecimal porcentajeretener;
    private String coddocsustento;
    private String numdocsustento;
    private Date fechadocsustento;
    private Integer tarifaimpuesto;
    private BigDecimal baseimponibleimp;
    private Comprobante codigocomprobante;

    public Impuestocomprobante() {
    }

    public Impuestocomprobante(Long codigoimpuesto) {
        this.codigoimpuesto = codigoimpuesto;
    }

    public Impuestocomprobante(Long codigoimpuesto, int codigo, int codigoporcentaje, BigDecimal valorimp) {
        this.codigoimpuesto = codigoimpuesto;
        this.codigo = codigo;
        this.codigoporcentaje = codigoporcentaje;
        this.valorimp = valorimp;
    }

    public Long getCodigoimpuesto() {
        return codigoimpuesto;
    }

    public void setCodigoimpuesto(Long codigoimpuesto) {
        this.codigoimpuesto = codigoimpuesto;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoporcentaje() {
        return codigoporcentaje;
    }

    public void setCodigoporcentaje(int codigoporcentaje) {
        this.codigoporcentaje = codigoporcentaje;
    }

    public BigDecimal getValorimp() {
        return valorimp;
    }

    public void setValorimp(BigDecimal valorimp) {
        this.valorimp = valorimp;
    }

    public BigDecimal getDescuentoadicional() {
        return descuentoadicional;
    }

    public void setDescuentoadicional(BigDecimal descuentoadicional) {
        this.descuentoadicional = descuentoadicional;
    }

    public String getCodigoretencion() {
        return codigoretencion;
    }

    public void setCodigoretencion(String codigoretencion) {
        this.codigoretencion = codigoretencion;
    }

    public BigDecimal getPorcentajeretener() {
        return porcentajeretener;
    }

    public void setPorcentajeretener(BigDecimal porcentajeretener) {
        this.porcentajeretener = porcentajeretener;
    }

    public String getCoddocsustento() {
        return coddocsustento;
    }

    public void setCoddocsustento(String coddocsustento) {
        this.coddocsustento = coddocsustento;
    }

    public String getNumdocsustento() {
        return numdocsustento;
    }

    public void setNumdocsustento(String numdocsustento) {
        this.numdocsustento = numdocsustento;
    }

    public Date getFechadocsustento() {
        return fechadocsustento;
    }

    public void setFechadocsustento(Date fechadocsustento) {
        this.fechadocsustento = fechadocsustento;
    }

    public Integer getTarifaimpuesto() {
        return tarifaimpuesto;
    }

    public void setTarifaimpuesto(Integer tarifaimpuesto) {
        this.tarifaimpuesto = tarifaimpuesto;
    }

    public BigDecimal getBaseimponibleimp() {
        return baseimponibleimp;
    }

    public void setBaseimponibleimp(BigDecimal baseimponibleimp) {
        this.baseimponibleimp = baseimponibleimp;
    }

    public Comprobante getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Comprobante codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }

}
