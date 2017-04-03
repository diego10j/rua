/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "ing_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngDetalle.findAll", query = "SELECT i FROM IngDetalle i"),
    @NamedQuery(name = "IngDetalle.findByIdeIndet", query = "SELECT i FROM IngDetalle i WHERE i.ideIndet = :ideIndet"),
    @NamedQuery(name = "IngDetalle.findByValorIndet", query = "SELECT i FROM IngDetalle i WHERE i.valorIndet = :valorIndet"),
    @NamedQuery(name = "IngDetalle.findByDetalleIndet", query = "SELECT i FROM IngDetalle i WHERE i.detalleIndet = :detalleIndet"),
    @NamedQuery(name = "IngDetalle.findByNumeroIndet", query = "SELECT i FROM IngDetalle i WHERE i.numeroIndet = :numeroIndet")})
public class IngDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_indet")
    private Long ideIndet;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_indet")
    private BigDecimal valorIndet;
    @Column(name = "detalle_indet")
    private String detalleIndet;
    @Column(name = "numero_indet")
    private String numeroIndet;
    @JoinColumn(name = "ide_inper", referencedColumnName = "ide_inper")
    @ManyToOne
    private IngPersona ideInper;
    @JoinColumn(name = "ide_incab", referencedColumnName = "ide_incab")
    @ManyToOne
    private IngCabecera ideIncab;

    public IngDetalle() {
    }

    public IngDetalle(Long ideIndet) {
        this.ideIndet = ideIndet;
    }

    public Long getIdeIndet() {
        return ideIndet;
    }

    public void setIdeIndet(Long ideIndet) {
        this.ideIndet = ideIndet;
    }

    public BigDecimal getValorIndet() {
        return valorIndet;
    }

    public void setValorIndet(BigDecimal valorIndet) {
        this.valorIndet = valorIndet;
    }

    public String getDetalleIndet() {
        return detalleIndet;
    }

    public void setDetalleIndet(String detalleIndet) {
        this.detalleIndet = detalleIndet;
    }

    public String getNumeroIndet() {
        return numeroIndet;
    }

    public void setNumeroIndet(String numeroIndet) {
        this.numeroIndet = numeroIndet;
    }

    public IngPersona getIdeInper() {
        return ideInper;
    }

    public void setIdeInper(IngPersona ideInper) {
        this.ideInper = ideInper;
    }

    public IngCabecera getIdeIncab() {
        return ideIncab;
    }

    public void setIdeIncab(IngCabecera ideIncab) {
        this.ideIncab = ideIncab;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideIndet != null ? ideIndet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngDetalle)) {
            return false;
        }
        IngDetalle other = (IngDetalle) object;
        if ((this.ideIndet == null && other.ideIndet != null) || (this.ideIndet != null && !this.ideIndet.equals(other.ideIndet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.IngDetalle[ ideIndet=" + ideIndet + " ]";
    }
    
}
