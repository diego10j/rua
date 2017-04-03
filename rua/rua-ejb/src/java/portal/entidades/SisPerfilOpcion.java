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
@Table(name = "sis_perfil_opcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisPerfilOpcion.findAll", query = "SELECT s FROM SisPerfilOpcion s"),
    @NamedQuery(name = "SisPerfilOpcion.findByIdePeop", query = "SELECT s FROM SisPerfilOpcion s WHERE s.idePeop = :idePeop"),
    @NamedQuery(name = "SisPerfilOpcion.findByLecturaPeop", query = "SELECT s FROM SisPerfilOpcion s WHERE s.lecturaPeop = :lecturaPeop")})
public class SisPerfilOpcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_peop")
    private Long idePeop;
    @Column(name = "lectura_peop")
    private Boolean lecturaPeop;
    @JoinColumn(name = "ide_perf", referencedColumnName = "ide_perf")
    @ManyToOne
    private SisPerfil idePerf;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;

    public SisPerfilOpcion() {
    }

    public SisPerfilOpcion(Long idePeop) {
        this.idePeop = idePeop;
    }

    public Long getIdePeop() {
        return idePeop;
    }

    public void setIdePeop(Long idePeop) {
        this.idePeop = idePeop;
    }

    public Boolean getLecturaPeop() {
        return lecturaPeop;
    }

    public void setLecturaPeop(Boolean lecturaPeop) {
        this.lecturaPeop = lecturaPeop;
    }

    public SisPerfil getIdePerf() {
        return idePerf;
    }

    public void setIdePerf(SisPerfil idePerf) {
        this.idePerf = idePerf;
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
        hash += (idePeop != null ? idePeop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPerfilOpcion)) {
            return false;
        }
        SisPerfilOpcion other = (SisPerfilOpcion) object;
        if ((this.idePeop == null && other.idePeop != null) || (this.idePeop != null && !this.idePeop.equals(other.idePeop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPerfilOpcion[ idePeop=" + idePeop + " ]";
    }
    
}
