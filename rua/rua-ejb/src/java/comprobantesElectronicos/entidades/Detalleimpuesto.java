/*
 * Autor: Diego Jácome   11-03-2015
 */
package comprobantesElectronicos.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author dfjacome
 */
public class Detalleimpuesto implements Serializable {
    
    private Long codigodetaimp;
    private int codigo;
    private int codigoporcentaje;
    private int tarifa;
    private BigDecimal baseimponible;
    private BigDecimal valordetimp;
    private Detallecomprobante codigodetalle;

    public Detalleimpuesto() {
    }

    public Detalleimpuesto(Long codigodetaimp) {
        this.codigodetaimp = codigodetaimp;
    }

    public Detalleimpuesto(Long codigodetaimp, int codigo, int codigoporcentaje, int tarifa, BigDecimal baseimponible, BigDecimal valordetimp) {
        this.codigodetaimp = codigodetaimp;
        this.codigo = codigo;
        this.codigoporcentaje = codigoporcentaje;
        this.tarifa = tarifa;
        this.baseimponible = baseimponible;
        this.valordetimp = valordetimp;
    }

    public Long getCodigodetaimp() {
        return codigodetaimp;
    }

    public void setCodigodetaimp(Long codigodetaimp) {
        this.codigodetaimp = codigodetaimp;
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

    public int getTarifa() {
        return tarifa;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    public BigDecimal getBaseimponible() {
        return baseimponible;
    }

    public void setBaseimponible(BigDecimal baseimponible) {
        this.baseimponible = baseimponible;
    }

    public BigDecimal getValordetimp() {
        return valordetimp;
    }

    public void setValordetimp(BigDecimal valordetimp) {
        this.valordetimp = valordetimp;
    }

    public Detallecomprobante getCodigodetalle() {
        return codigodetalle;
    }

    public void setCodigodetalle(Detallecomprobante codigodetalle) {
        this.codigodetalle = codigodetalle;
    }     
}
