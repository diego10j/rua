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
@Table(name = "gen_estado_civil")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenEstadoCivil.findAll", query = "SELECT g FROM GenEstadoCivil g"),
    @NamedQuery(name = "GenEstadoCivil.findByIdeGeeci", query = "SELECT g FROM GenEstadoCivil g WHERE g.ideGeeci = :ideGeeci"),
    @NamedQuery(name = "GenEstadoCivil.findByNombreGeeci", query = "SELECT g FROM GenEstadoCivil g WHERE g.nombreGeeci = :nombreGeeci")})
public class GenEstadoCivil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geeci")
    private Long ideGeeci;
    @Basic(optional = false)
    @Column(name = "nombre_geeci")
    private String nombreGeeci;
    @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu")
    @ManyToOne
    private SisSucursal ideSucu;


    public GenEstadoCivil() {
    }

    public GenEstadoCivil(Long ideGeeci) {
        this.ideGeeci = ideGeeci;
    }

    public GenEstadoCivil(Long ideGeeci, String nombreGeeci) {
        this.ideGeeci = ideGeeci;
        this.nombreGeeci = nombreGeeci;
    }

    public Long getIdeGeeci() {
        return ideGeeci;
    }

    public void setIdeGeeci(Long ideGeeci) {
        this.ideGeeci = ideGeeci;
    }

    public String getNombreGeeci() {
        return nombreGeeci;
    }

    public void setNombreGeeci(String nombreGeeci) {
        this.nombreGeeci = nombreGeeci;
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
        hash += (ideGeeci != null ? ideGeeci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenEstadoCivil)) {
            return false;
        }
        GenEstadoCivil other = (GenEstadoCivil) object;
        if ((this.ideGeeci == null && other.ideGeeci != null) || (this.ideGeeci != null && !this.ideGeeci.equals(other.ideGeeci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenEstadoCivil[ ideGeeci=" + ideGeeci + " ]";
    }
    
}
