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
@Table(name = "sis_perfil_objeto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisPerfilObjeto.findAll", query = "SELECT s FROM SisPerfilObjeto s"),
    @NamedQuery(name = "SisPerfilObjeto.findByIdePeob", query = "SELECT s FROM SisPerfilObjeto s WHERE s.idePeob = :idePeob"),
    @NamedQuery(name = "SisPerfilObjeto.findByLecturaPeob", query = "SELECT s FROM SisPerfilObjeto s WHERE s.lecturaPeob = :lecturaPeob"),
    @NamedQuery(name = "SisPerfilObjeto.findByVisiblePeob", query = "SELECT s FROM SisPerfilObjeto s WHERE s.visiblePeob = :visiblePeob")})
public class SisPerfilObjeto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_peob")
    private Long idePeob;
    @Column(name = "lectura_peob")
    private Boolean lecturaPeob;
    @Column(name = "visible_peob")
    private Boolean visiblePeob;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @JoinColumn(name = "ide_obop", referencedColumnName = "ide_obop")
    @ManyToOne
    private SisObjetoOpcion ideObop;

    public SisPerfilObjeto() {
    }

    public SisPerfilObjeto(Long idePeob) {
        this.idePeob = idePeob;
    }

    public Long getIdePeob() {
        return idePeob;
    }

    public void setIdePeob(Long idePeob) {
        this.idePeob = idePeob;
    }

    public Boolean getLecturaPeob() {
        return lecturaPeob;
    }

    public void setLecturaPeob(Boolean lecturaPeob) {
        this.lecturaPeob = lecturaPeob;
    }

    public Boolean getVisiblePeob() {
        return visiblePeob;
    }

    public void setVisiblePeob(Boolean visiblePeob) {
        this.visiblePeob = visiblePeob;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
    }

    public SisObjetoOpcion getIdeObop() {
        return ideObop;
    }

    public void setIdeObop(SisObjetoOpcion ideObop) {
        this.ideObop = ideObop;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePeob != null ? idePeob.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilObjeto)) {
            return false;
        }
        SisPerfilObjeto other = (SisPerfilObjeto) object;
        if ((this.idePeob == null && other.idePeob != null) || (this.idePeob != null && !this.idePeob.equals(other.idePeob))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilObjeto[ idePeob=" + idePeob + " ]";
    }
    
}
