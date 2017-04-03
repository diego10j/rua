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
@Table(name = "sis_perfil_campo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisPerfilCampo.findAll", query = "SELECT s FROM SisPerfilCampo s"),
    @NamedQuery(name = "SisPerfilCampo.findByIdePeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.idePeca = :idePeca"),
    @NamedQuery(name = "SisPerfilCampo.findByLecturaPeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.lecturaPeca = :lecturaPeca"),
    @NamedQuery(name = "SisPerfilCampo.findByVisiblePeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.visiblePeca = :visiblePeca"),
    @NamedQuery(name = "SisPerfilCampo.findByDefectoPeca", query = "SELECT s FROM SisPerfilCampo s WHERE s.defectoPeca = :defectoPeca")})
public class SisPerfilCampo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_peca")
    private Long idePeca;
    @Column(name = "lectura_peca")
    private Boolean lecturaPeca;
    @Column(name = "visible_peca")
    private Boolean visiblePeca;
    @Column(name = "defecto_peca")
    private String defectoPeca;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @JoinColumn(name = "ide_camp", referencedColumnName = "ide_camp")
    @ManyToOne
    private SisCampo ideCamp;

    public SisPerfilCampo() {
    }

    public SisPerfilCampo(Long idePeca) {
        this.idePeca = idePeca;
    }

    public Long getIdePeca() {
        return idePeca;
    }

    public void setIdePeca(Long idePeca) {
        this.idePeca = idePeca;
    }

    public Boolean getLecturaPeca() {
        return lecturaPeca;
    }

    public void setLecturaPeca(Boolean lecturaPeca) {
        this.lecturaPeca = lecturaPeca;
    }

    public Boolean getVisiblePeca() {
        return visiblePeca;
    }

    public void setVisiblePeca(Boolean visiblePeca) {
        this.visiblePeca = visiblePeca;
    }

    public String getDefectoPeca() {
        return defectoPeca;
    }

    public void setDefectoPeca(String defectoPeca) {
        this.defectoPeca = defectoPeca;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    public SisCampo getIdeCamp() {
        return ideCamp;
    }

    public void setIdeCamp(SisCampo ideCamp) {
        this.ideCamp = ideCamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePeca != null ? idePeca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilCampo)) {
            return false;
        }
        SisPerfilCampo other = (SisPerfilCampo) object;
        if ((this.idePeca == null && other.idePeca != null) || (this.idePeca != null && !this.idePeca.equals(other.idePeca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilCampo[ idePeca=" + idePeca + " ]";
    }
    
}
