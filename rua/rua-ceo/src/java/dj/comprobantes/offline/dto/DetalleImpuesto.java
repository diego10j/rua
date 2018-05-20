/*
 *********************************************************************
 Objetivo: Clase DetalleImpuesto
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author djacome
 */
public class DetalleImpuesto implements Serializable {

    private Comprobante comprobante;
    private String codigo;
    private String codigoRetencion;
    private BigDecimal porcentajeRetener;
    private BigDecimal baseImponible;
    private BigDecimal valorRetenido;
    private String codDocSustento;
    private String numDocSustento;
    private Date fechaEmisionDocSustento;

    public DetalleImpuesto(ResultSet resultado) {
        try {
            this.codigo = resultado.getString("codigo_fe_cnimp");
            this.codigoRetencion = resultado.getString("codigo_fe_retencion_cncim");
            this.baseImponible = resultado.getBigDecimal("base_cndre");
            this.porcentajeRetener = resultado.getBigDecimal("porcentaje_cndre");
            this.valorRetenido = resultado.getBigDecimal("valor_cndre");
            this.codDocSustento = resultado.getString("alter_tribu_cntdo");
            this.numDocSustento = resultado.getString("numero_cpcfa");
            this.fechaEmisionDocSustento = resultado.getDate("fecha_emisi_cpcfa");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoRetencion() {
        return codigoRetencion;
    }

    public void setCodigoRetencion(String codigoRetencion) {
        this.codigoRetencion = codigoRetencion;
    }

    public BigDecimal getPorcentajeRetener() {
        return porcentajeRetener;
    }

    public void setPorcentajeRetener(BigDecimal porcentajeRetener) {
        this.porcentajeRetener = porcentajeRetener;
    }

    public BigDecimal getValorRetenido() {
        return valorRetenido;
    }

    public void setValorRetenido(BigDecimal valorRetenido) {
        this.valorRetenido = valorRetenido;
    }

    public String getCodDocSustento() {
        return codDocSustento;
    }

    public void setCodDocSustento(String codDocSustento) {
        this.codDocSustento = codDocSustento;
    }

    public String getNumDocSustento() {
        return numDocSustento;
    }

    public void setNumDocSustento(String numDocSustento) {
        this.numDocSustento = numDocSustento;
    }

    public Date getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

}
