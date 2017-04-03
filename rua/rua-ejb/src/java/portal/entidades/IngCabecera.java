/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "ing_cabecera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngCabecera.findAll", query = "SELECT i FROM IngCabecera i"),
    @NamedQuery(name = "IngCabecera.findByIdeIncab", query = "SELECT i FROM IngCabecera i WHERE i.ideIncab = :ideIncab"),
    @NamedQuery(name = "IngCabecera.findByNombreIncab", query = "SELECT i FROM IngCabecera i WHERE i.nombreIncab = :nombreIncab"),
    @NamedQuery(name = "IngCabecera.findByObservacionIncab", query = "SELECT i FROM IngCabecera i WHERE i.observacionIncab = :observacionIncab"),
    @NamedQuery(name = "IngCabecera.findByFechaIncab", query = "SELECT i FROM IngCabecera i WHERE i.fechaIncab = :fechaIncab")})
public class IngCabecera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_incab")
    private Long ideIncab;
    @Column(name = "nombre_incab")
    private String nombreIncab;
    @Column(name = "observacion_incab")
    private String observacionIncab;
    @Column(name = "fecha_incab")
    @Temporal(TemporalType.DATE)
    private Date fechaIncab;
    @JoinColumn(name = "ide_intip", referencedColumnName = "ide_intip")
    @ManyToOne
    private IngTipo ideIntip;
    @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes")
    @ManyToOne
    private GenMes ideGemes;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @OneToMany(mappedBy = "ideIncab")
    private Collection<IngDetalle> ingDetalleCollection;

    public IngCabecera() {
    }

    public IngCabecera(Long ideIncab) {
        this.ideIncab = ideIncab;
    }

    public Long getIdeIncab() {
        return ideIncab;
    }

    public void setIdeIncab(Long ideIncab) {
        this.ideIncab = ideIncab;
    }

    public String getNombreIncab() {
        return nombreIncab;
    }

    public void setNombreIncab(String nombreIncab) {
        this.nombreIncab = nombreIncab;
    }

    public String getObservacionIncab() {
        return observacionIncab;
    }

    public void setObservacionIncab(String observacionIncab) {
        this.observacionIncab = observacionIncab;
    }

    public Date getFechaIncab() {
        return fechaIncab;
    }

    public void setFechaIncab(Date fechaIncab) {
        this.fechaIncab = fechaIncab;
    }

    public IngTipo getIdeIntip() {
        return ideIntip;
    }

    public void setIdeIntip(IngTipo ideIntip) {
        this.ideIntip = ideIntip;
    }

    public GenMes getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(GenMes ideGemes) {
        this.ideGemes = ideGemes;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
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
        hash += (ideIncab != null ? ideIncab.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngCabecera)) {
            return false;
        }
        IngCabecera other = (IngCabecera) object;
        if ((this.ideIncab == null && other.ideIncab != null) || (this.ideIncab != null && !this.ideIncab.equals(other.ideIncab))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.IngCabecera[ ideIncab=" + ideIncab + " ]";
    }
    
}
