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
@Table(name = "sis_objeto_opcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisObjetoOpcion.findAll", query = "SELECT s FROM SisObjetoOpcion s"),
    @NamedQuery(name = "SisObjetoOpcion.findByIdeObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.ideObop = :ideObop"),
    @NamedQuery(name = "SisObjetoOpcion.findByNomObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.nomObop = :nomObop"),
    @NamedQuery(name = "SisObjetoOpcion.findByIdObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.idObop = :idObop"),
    @NamedQuery(name = "SisObjetoOpcion.findByDescripcionObop", query = "SELECT s FROM SisObjetoOpcion s WHERE s.descripcionObop = :descripcionObop")})
public class SisObjetoOpcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_obop")
    private Long ideObop;
    @Column(name = "nom_obop")
    private String nomObop;
    @Column(name = "id_obop")
    private String idObop;
    @Column(name = "descripcion_obop")
    private String descripcionObop;
    @OneToMany(mappedBy = "ideObop")
    private Collection<SisPerfilObjeto> sisPerfilObjetoCollection;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;

    public SisObjetoOpcion() {
    }

    public SisObjetoOpcion(Long ideObop) {
        this.ideObop = ideObop;
    }

    public Long getIdeObop() {
        return ideObop;
    }

    public void setIdeObop(Long ideObop) {
        this.ideObop = ideObop;
    }

    public String getNomObop() {
        return nomObop;
    }

    public void setNomObop(String nomObop) {
        this.nomObop = nomObop;
    }

    public String getIdObop() {
        return idObop;
    }

    public void setIdObop(String idObop) {
        this.idObop = idObop;
    }

    public String getDescripcionObop() {
        return descripcionObop;
    }

    public void setDescripcionObop(String descripcionObop) {
        this.descripcionObop = descripcionObop;
    }

    @XmlTransient
    public Collection<SisPerfilObjeto> getSisPerfilObjetoCollection() {
        return sisPerfilObjetoCollection;
    }

    public void setSisPerfilObjetoCollection(Collection<SisPerfilObjeto> sisPerfilObjetoCollection) {
        this.sisPerfilObjetoCollection = sisPerfilObjetoCollection;
    }

    public SisOpcion getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(SisOpcion ideOpci) {
        this.ideOpci = ideOpci;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideObop != null ? ideObop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisObjetoOpcion)) {
            return false;
        }
        SisObjetoOpcion other = (SisObjetoOpcion) object;
        if ((this.ideObop == null && other.ideObop != null) || (this.ideObop != null && !this.ideObop.equals(other.ideObop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisObjetoOpcion[ ideObop=" + ideObop + " ]";
    }
    
}
