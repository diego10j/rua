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
@Table(name = "sis_reporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisReporte.findAll", query = "SELECT s FROM SisReporte s"),
    @NamedQuery(name = "SisReporte.findByIdeRepo", query = "SELECT s FROM SisReporte s WHERE s.ideRepo = :ideRepo"),
    @NamedQuery(name = "SisReporte.findByNomRepo", query = "SELECT s FROM SisReporte s WHERE s.nomRepo = :nomRepo"),
    @NamedQuery(name = "SisReporte.findByPathRepo", query = "SELECT s FROM SisReporte s WHERE s.pathRepo = :pathRepo")})
public class SisReporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_repo")
    private Long ideRepo;
    @Basic(optional = false)
    @Column(name = "nom_repo")
    private String nomRepo;
    @Column(name = "path_repo")
    private String pathRepo;
    @OneToMany(mappedBy = "ideRepo")
    private Collection<SisPerfilReporte> sisPerfilReporteCollection;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;

    public SisReporte() {
    }

    public SisReporte(Long ideRepo) {
        this.ideRepo = ideRepo;
    }

    public SisReporte(Long ideRepo, String nomRepo) {
        this.ideRepo = ideRepo;
        this.nomRepo = nomRepo;
    }

    public Long getIdeRepo() {
        return ideRepo;
    }

    public void setIdeRepo(Long ideRepo) {
        this.ideRepo = ideRepo;
    }

    public String getNomRepo() {
        return nomRepo;
    }

    public void setNomRepo(String nomRepo) {
        this.nomRepo = nomRepo;
    }

    public String getPathRepo() {
        return pathRepo;
    }

    public void setPathRepo(String pathRepo) {
        this.pathRepo = pathRepo;
    }

    @XmlTransient
    public Collection<SisPerfilReporte> getSisPerfilReporteCollection() {
        return sisPerfilReporteCollection;
    }

    public void setSisPerfilReporteCollection(Collection<SisPerfilReporte> sisPerfilReporteCollection) {
        this.sisPerfilReporteCollection = sisPerfilReporteCollection;
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
        hash += (ideRepo != null ? ideRepo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisReporte)) {
            return false;
        }
        SisReporte other = (SisReporte) object;
        if ((this.ideRepo == null && other.ideRepo != null) || (this.ideRepo != null && !this.ideRepo.equals(other.ideRepo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisReporte[ ideRepo=" + ideRepo + " ]";
    }
    
}
