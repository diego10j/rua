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
@Table(name = "sis_bloqueo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisBloqueo.findAll", query = "SELECT s FROM SisBloqueo s"),
    @NamedQuery(name = "SisBloqueo.findByIdeBloq", query = "SELECT s FROM SisBloqueo s WHERE s.ideBloq = :ideBloq"),
    @NamedQuery(name = "SisBloqueo.findByTablaBloq", query = "SELECT s FROM SisBloqueo s WHERE s.tablaBloq = :tablaBloq"),
    @NamedQuery(name = "SisBloqueo.findByMaximoBloq", query = "SELECT s FROM SisBloqueo s WHERE s.maximoBloq = :maximoBloq"),
    @NamedQuery(name = "SisBloqueo.findByUsuarioBloq", query = "SELECT s FROM SisBloqueo s WHERE s.usuarioBloq = :usuarioBloq")})
public class SisBloqueo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_bloq")
    private Long ideBloq;
    @Basic(optional = false)
    @Column(name = "tabla_bloq")
    private String tablaBloq;
    @Basic(optional = false)
    @Column(name = "maximo_bloq")
    private long maximoBloq;
    @Basic(optional = false)
    @Column(name = "usuario_bloq")
    private String usuarioBloq;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;

    public SisBloqueo() {
    }

    public SisBloqueo(Long ideBloq) {
        this.ideBloq = ideBloq;
    }

    public SisBloqueo(Long ideBloq, String tablaBloq, long maximoBloq, String usuarioBloq) {
        this.ideBloq = ideBloq;
        this.tablaBloq = tablaBloq;
        this.maximoBloq = maximoBloq;
        this.usuarioBloq = usuarioBloq;
    }

    public Long getIdeBloq() {
        return ideBloq;
    }

    public void setIdeBloq(Long ideBloq) {
        this.ideBloq = ideBloq;
    }

    public String getTablaBloq() {
        return tablaBloq;
    }

    public void setTablaBloq(String tablaBloq) {
        this.tablaBloq = tablaBloq;
    }

    public long getMaximoBloq() {
        return maximoBloq;
    }

    public void setMaximoBloq(long maximoBloq) {
        this.maximoBloq = maximoBloq;
    }

    public String getUsuarioBloq() {
        return usuarioBloq;
    }

    public void setUsuarioBloq(String usuarioBloq) {
        this.usuarioBloq = usuarioBloq;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBloq != null ? ideBloq.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisBloqueo)) {
            return false;
        }
        SisBloqueo other = (SisBloqueo) object;
        if ((this.ideBloq == null && other.ideBloq != null) || (this.ideBloq != null && !this.ideBloq.equals(other.ideBloq))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisBloqueo[ ideBloq=" + ideBloq + " ]";
    }
    
}
