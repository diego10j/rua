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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "gen_mes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenMes.findAll", query = "SELECT g FROM GenMes g"),
    @NamedQuery(name = "GenMes.findByIdeGemes", query = "SELECT g FROM GenMes g WHERE g.ideGemes = :ideGemes"),
    @NamedQuery(name = "GenMes.findByNombreGemes", query = "SELECT g FROM GenMes g WHERE g.nombreGemes = :nombreGemes"),
    @NamedQuery(name = "GenMes.findByAlternoGemes", query = "SELECT g FROM GenMes g WHERE g.alternoGemes = :alternoGemes"),
    @NamedQuery(name = "GenMes.findByActivoGemes", query = "SELECT g FROM GenMes g WHERE g.activoGemes = :activoGemes")})
public class GenMes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gemes")
    private Long ideGemes;
    @Column(name = "nombre_gemes")
    private String nombreGemes;
    @Column(name = "alterno_gemes")
    private String alternoGemes;
    @Column(name = "activo_gemes")
    private Boolean activoGemes;


    @OneToMany(mappedBy = "ideGemes")
    private Collection<IngCabecera> ingCabeceraCollection;
    @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu")
    @ManyToOne
    private SisSucursal ideSucu;


    public GenMes() {
    }

    public GenMes(Long ideGemes) {
        this.ideGemes = ideGemes;
    }

    public Long getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(Long ideGemes) {
        this.ideGemes = ideGemes;
    }

    public String getNombreGemes() {
        return nombreGemes;
    }

    public void setNombreGemes(String nombreGemes) {
        this.nombreGemes = nombreGemes;
    }

    public String getAlternoGemes() {
        return alternoGemes;
    }

    public void setAlternoGemes(String alternoGemes) {
        this.alternoGemes = alternoGemes;
    }

    public Boolean getActivoGemes() {
        return activoGemes;
    }

    public void setActivoGemes(Boolean activoGemes) {
        this.activoGemes = activoGemes;
    }

 
    @XmlTransient
    public Collection<IngCabecera> getIngCabeceraCollection() {
        return ingCabeceraCollection;
    }

    public void setIngCabeceraCollection(Collection<IngCabecera> ingCabeceraCollection) {
        this.ingCabeceraCollection = ingCabeceraCollection;
    }

    public SisSucursal getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(SisSucursal ideSucu) {
        this.ideSucu = ideSucu;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemes != null ? ideGemes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenMes)) {
            return false;
        }
        GenMes other = (GenMes) object;
        if ((this.ideGemes == null && other.ideGemes != null) || (this.ideGemes != null && !this.ideGemes.equals(other.ideGemes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenMes[ ideGemes=" + ideGemes + " ]";
    }
    
}
