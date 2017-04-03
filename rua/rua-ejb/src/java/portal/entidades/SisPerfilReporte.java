/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "sis_perfil_reporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisPerfilReporte.findAll", query = "SELECT s FROM SisPerfilReporte s"),
    @NamedQuery(name = "SisPerfilReporte.findByIdePere", query = "SELECT s FROM SisPerfilReporte s WHERE s.idePere = :idePere")})
public class SisPerfilReporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_pere")
    private Long idePere;
    @JoinColumn(name = "ide_repo", referencedColumnName = "ide_repo")
    @ManyToOne
    private SisReporte ideRepo;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;

    public SisPerfilReporte() {
    }

    public SisPerfilReporte(Long idePere) {
        this.idePere = idePere;
    }

    public Long getIdePere() {
        return idePere;
    }

    public void setIdePere(Long idePere) {
        this.idePere = idePere;
    }

    public SisReporte getIdeRepo() {
        return ideRepo;
    }

    public void setIdeRepo(SisReporte ideRepo) {
        this.ideRepo = ideRepo;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePere != null ? idePere.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilReporte)) {
            return false;
        }
        SisPerfilReporte other = (SisPerfilReporte) object;
        if ((this.idePere == null && other.idePere != null) || (this.idePere != null && !this.idePere.equals(other.idePere))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilReporte[ idePere=" + idePere + " ]";
    }
    
}
