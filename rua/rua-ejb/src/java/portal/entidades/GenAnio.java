/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "gen_anio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenAnio.findAll", query = "SELECT g FROM GenAnio g"),
    @NamedQuery(name = "GenAnio.findByIdeGeani", query = "SELECT g FROM GenAnio g WHERE g.ideGeani = :ideGeani"),
    @NamedQuery(name = "GenAnio.findByNomGeani", query = "SELECT g FROM GenAnio g WHERE g.nomGeani = :nomGeani"),
    @NamedQuery(name = "GenAnio.findByActivoGeani", query = "SELECT g FROM GenAnio g WHERE g.activoGeani = :activoGeani")})
public class GenAnio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geani")
    private Long ideGeani;
    @Column(name = "nom_geani")
    private String nomGeani;
    @Column(name = "activo_geani")
    private Boolean activoGeani;

    @OneToMany(mappedBy = "ideGeani")
    private Collection<GthCuentaAnticipo> gthCuentaAnticipoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genAnio")
    private Collection<GenPeriodo> genPeriodoCollection;

    @OneToMany(mappedBy = "ideGeani")
    private Collection<IngCabecera> ingCabeceraCollection;


    public GenAnio() {
    }

    public GenAnio(Long ideGeani) {
        this.ideGeani = ideGeani;
    }

    public Long getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(Long ideGeani) {
        this.ideGeani = ideGeani;
    }

    public String getNomGeani() {
        return nomGeani;
    }

    public void setNomGeani(String nomGeani) {
        this.nomGeani = nomGeani;
    }

    public Boolean getActivoGeani() {
        return activoGeani;
    }

    public void setActivoGeani(Boolean activoGeani) {
        this.activoGeani = activoGeani;
    }

 
    @XmlTransient
    public Collection<GthCuentaAnticipo> getGthCuentaAnticipoCollection() {
        return gthCuentaAnticipoCollection;
    }

    public void setGthCuentaAnticipoCollection(Collection<GthCuentaAnticipo> gthCuentaAnticipoCollection) {
        this.gthCuentaAnticipoCollection = gthCuentaAnticipoCollection;
    }

    @XmlTransient
    public Collection<GenPeriodo> getGenPeriodoCollection() {
        return genPeriodoCollection;
    }

    public void setGenPeriodoCollection(Collection<GenPeriodo> genPeriodoCollection) {
        this.genPeriodoCollection = genPeriodoCollection;
    }


    @XmlTransient
    public Collection<IngCabecera> getIngCabeceraCollection() {
        return ingCabeceraCollection;
    }

    public void setIngCabeceraCollection(Collection<IngCabecera> ingCabeceraCollection) {
        this.ingCabeceraCollection = ingCabeceraCollection;
    }

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeani != null ? ideGeani.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenAnio)) {
            return false;
        }
        GenAnio other = (GenAnio) object;
        if ((this.ideGeani == null && other.ideGeani != null) || (this.ideGeani != null && !this.ideGeani.equals(other.ideGeani))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenAnio[ ideGeani=" + ideGeani + " ]";
    }
    
}
