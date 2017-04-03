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
@Table(name = "ing_tipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngTipo.findAll", query = "SELECT i FROM IngTipo i"),
    @NamedQuery(name = "IngTipo.findByIdeIntip", query = "SELECT i FROM IngTipo i WHERE i.ideIntip = :ideIntip"),
    @NamedQuery(name = "IngTipo.findByNombreIntip", query = "SELECT i FROM IngTipo i WHERE i.nombreIntip = :nombreIntip")})
public class IngTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_intip")
    private Long ideIntip;
    @Column(name = "nombre_intip")
    private String nombreIntip;
    @OneToMany(mappedBy = "ideIntip")
    private Collection<IngCabecera> ingCabeceraCollection;

    public IngTipo() {
    }

    public IngTipo(Long ideIntip) {
        this.ideIntip = ideIntip;
    }

    public Long getIdeIntip() {
        return ideIntip;
    }

    public void setIdeIntip(Long ideIntip) {
        this.ideIntip = ideIntip;
    }

    public String getNombreIntip() {
        return nombreIntip;
    }

    public void setNombreIntip(String nombreIntip) {
        this.nombreIntip = nombreIntip;
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
        hash += (ideIntip != null ? ideIntip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngTipo)) {
            return false;
        }
        IngTipo other = (IngTipo) object;
        if ((this.ideIntip == null && other.ideIntip != null) || (this.ideIntip != null && !this.ideIntip.equals(other.ideIntip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.IngTipo[ ideIntip=" + ideIntip + " ]";
    }
    
}
