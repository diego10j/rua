/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "ing_persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngPersona.findAll", query = "SELECT i FROM IngPersona i"),
    @NamedQuery(name = "IngPersona.findByIdeInper", query = "SELECT i FROM IngPersona i WHERE i.ideInper = :ideInper"),
    @NamedQuery(name = "IngPersona.findByNombresInper", query = "SELECT i FROM IngPersona i WHERE i.nombresInper = :nombresInper"),
    @NamedQuery(name = "IngPersona.findByObservacionInper", query = "SELECT i FROM IngPersona i WHERE i.observacionInper = :observacionInper"),
    @NamedQuery(name = "IngPersona.findByActivoInper", query = "SELECT i FROM IngPersona i WHERE i.activoInper = :activoInper")})
public class IngPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_inper")
    private Long ideInper;
    @Column(name = "nombres_inper")
    private String nombresInper;
    @Column(name = "observacion_inper")
    private String observacionInper;
    @Column(name = "activo_inper")
    private Boolean activoInper;
    @OneToMany(mappedBy = "ideInper")
    private Collection<IngDetalle> ingDetalleCollection;

    public IngPersona() {
    }

    public IngPersona(Long ideInper) {
        this.ideInper = ideInper;
    }

    public Long getIdeInper() {
        return ideInper;
    }

    public void setIdeInper(Long ideInper) {
        this.ideInper = ideInper;
    }

    public String getNombresInper() {
        return nombresInper;
    }

    public void setNombresInper(String nombresInper) {
        this.nombresInper = nombresInper;
    }

    public String getObservacionInper() {
        return observacionInper;
    }

    public void setObservacionInper(String observacionInper) {
        this.observacionInper = observacionInper;
    }

    public Boolean getActivoInper() {
        return activoInper;
    }

    public void setActivoInper(Boolean activoInper) {
        this.activoInper = activoInper;
    }

    @XmlTransient
    public Collection<IngDetalle> getIngDetalleCollection() {
        return ingDetalleCollection;
    }

    public void setIngDetalleCollection(Collection<IngDetalle> ingDetalleCollection) {
        this.ingDetalleCollection = ingDetalleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideInper != null ? ideInper.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngPersona)) {
            return false;
        }
        IngPersona other = (IngPersona) object;
        if ((this.ideInper == null && other.ideInper != null) || (this.ideInper != null && !this.ideInper.equals(other.ideInper))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.IngPersona[ ideInper=" + ideInper + " ]";
    }
    
}
