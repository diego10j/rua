/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "tes_conciliacion_banco")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TesConciliacionBanco.findAll", query = "SELECT t FROM TesConciliacionBanco t"),
    @NamedQuery(name = "TesConciliacionBanco.findByIdeTecob", query = "SELECT t FROM TesConciliacionBanco t WHERE t.ideTecob = :ideTecob"),
    @NamedQuery(name = "TesConciliacionBanco.findByIdeTecba", query = "SELECT t FROM TesConciliacionBanco t WHERE t.ideTecba = :ideTecba"),
    @NamedQuery(name = "TesConciliacionBanco.findByFechaTecob", query = "SELECT t FROM TesConciliacionBanco t WHERE t.fechaTecob = :fechaTecob"),
    @NamedQuery(name = "TesConciliacionBanco.findByConceptoTecob", query = "SELECT t FROM TesConciliacionBanco t WHERE t.conceptoTecob = :conceptoTecob"),
    @NamedQuery(name = "TesConciliacionBanco.findByDocumentoTecob", query = "SELECT t FROM TesConciliacionBanco t WHERE t.documentoTecob = :documentoTecob"),
    @NamedQuery(name = "TesConciliacionBanco.findByMontoTecob", query = "SELECT t FROM TesConciliacionBanco t WHERE t.montoTecob = :montoTecob"),
    @NamedQuery(name = "TesConciliacionBanco.findBySaldoTecob", query = "SELECT t FROM TesConciliacionBanco t WHERE t.saldoTecob = :saldoTecob"),
    @NamedQuery(name = "TesConciliacionBanco.findByConciliadoTecob", query = "SELECT t FROM TesConciliacionBanco t WHERE t.conciliadoTecob = :conciliadoTecob")})
public class TesConciliacionBanco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_tecob")
    private Long ideTecob;
    @Column(name = "ide_tecba")
    private BigInteger ideTecba;
    @Column(name = "fecha_tecob")
    @Temporal(TemporalType.DATE)
    private Date fechaTecob;
    @Column(name = "concepto_tecob")
    private String conceptoTecob;
    @Column(name = "documento_tecob")
    private String documentoTecob;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_tecob")
    private BigDecimal montoTecob;
    @Column(name = "saldo_tecob")
    private BigDecimal saldoTecob;
    @Column(name = "conciliado_tecob")
    private Boolean conciliadoTecob;

    public TesConciliacionBanco() {
    }

    public TesConciliacionBanco(Long ideTecob) {
        this.ideTecob = ideTecob;
    }

    public Long getIdeTecob() {
        return ideTecob;
    }

    public void setIdeTecob(Long ideTecob) {
        this.ideTecob = ideTecob;
    }

    public BigInteger getIdeTecba() {
        return ideTecba;
    }

    public void setIdeTecba(BigInteger ideTecba) {
        this.ideTecba = ideTecba;
    }

    public Date getFechaTecob() {
        return fechaTecob;
    }

    public void setFechaTecob(Date fechaTecob) {
        this.fechaTecob = fechaTecob;
    }

    public String getConceptoTecob() {
        return conceptoTecob;
    }

    public void setConceptoTecob(String conceptoTecob) {
        this.conceptoTecob = conceptoTecob;
    }

    public String getDocumentoTecob() {
        return documentoTecob;
    }

    public void setDocumentoTecob(String documentoTecob) {
        this.documentoTecob = documentoTecob;
    }

    public BigDecimal getMontoTecob() {
        return montoTecob;
    }

    public void setMontoTecob(BigDecimal montoTecob) {
        this.montoTecob = montoTecob;
    }

    public BigDecimal getSaldoTecob() {
        return saldoTecob;
    }

    public void setSaldoTecob(BigDecimal saldoTecob) {
        this.saldoTecob = saldoTecob;
    }

    public Boolean getConciliadoTecob() {
        return conciliadoTecob;
    }

    public void setConciliadoTecob(Boolean conciliadoTecob) {
        this.conciliadoTecob = conciliadoTecob;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTecob != null ? ideTecob.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesConciliacionBanco)) {
            return false;
        }
        TesConciliacionBanco other = (TesConciliacionBanco) object;
        if ((this.ideTecob == null && other.ideTecob != null) || (this.ideTecob != null && !this.ideTecob.equals(other.ideTecob))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesConciliacionBanco[ ideTecob=" + ideTecob + " ]";
    }
    
}
