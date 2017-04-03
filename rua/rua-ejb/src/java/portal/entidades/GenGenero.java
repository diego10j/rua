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
@Table(name = "gen_genero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenGenero.findAll", query = "SELECT g FROM GenGenero g"),
    @NamedQuery(name = "GenGenero.findByIdeGegen", query = "SELECT g FROM GenGenero g WHERE g.ideGegen = :ideGegen"),
    @NamedQuery(name = "GenGenero.findByNombreGegen", query = "SELECT g FROM GenGenero g WHERE g.nombreGegen = :nombreGegen")})
public class GenGenero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gegen")
    private Long ideGegen;
    @Basic(optional = false)
    @Column(name = "nombre_gegen")
    private String nombreGegen;
    @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu")
    @ManyToOne
    private SisSucursal ideSucu;



    public GenGenero() {
    }

    public GenGenero(Long ideGegen) {
        this.ideGegen = ideGegen;
    }

    public GenGenero(Long ideGegen, String nombreGegen) {
        this.ideGegen = ideGegen;
        this.nombreGegen = nombreGegen;
    }

    public Long getIdeGegen() {
        return ideGegen;
    }

    public void setIdeGegen(Long ideGegen) {
        this.ideGegen = ideGegen;
    }

    public String getNombreGegen() {
        return nombreGegen;
    }

    public void setNombreGegen(String nombreGegen) {
        this.nombreGegen = nombreGegen;
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
        hash += (ideGegen != null ? ideGegen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGenero)) {
            return false;
        }
        GenGenero other = (GenGenero) object;
        if ((this.ideGegen == null && other.ideGegen != null) || (this.ideGegen != null && !this.ideGegen.equals(other.ideGegen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGenero[ ideGegen=" + ideGegen + " ]";
    }
    
}
