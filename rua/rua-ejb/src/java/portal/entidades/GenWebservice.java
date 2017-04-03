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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "gen_webservice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenWebservice.findAll", query = "SELECT g FROM GenWebservice g"),
    @NamedQuery(name = "GenWebservice.findByIdeGewes", query = "SELECT g FROM GenWebservice g WHERE g.ideGewes = :ideGewes"),
    @NamedQuery(name = "GenWebservice.findByNombreGewes", query = "SELECT g FROM GenWebservice g WHERE g.nombreGewes = :nombreGewes"),
    @NamedQuery(name = "GenWebservice.findByWsdlGewes", query = "SELECT g FROM GenWebservice g WHERE g.wsdlGewes = :wsdlGewes"),
    @NamedQuery(name = "GenWebservice.findByUsuarioGewes", query = "SELECT g FROM GenWebservice g WHERE g.usuarioGewes = :usuarioGewes"),
    @NamedQuery(name = "GenWebservice.findByPasswordGewes", query = "SELECT g FROM GenWebservice g WHERE g.passwordGewes = :passwordGewes"),
    @NamedQuery(name = "GenWebservice.findByTiempomaxGewes", query = "SELECT g FROM GenWebservice g WHERE g.tiempomaxGewes = :tiempomaxGewes")})
public class GenWebservice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gewes")
    private Short ideGewes;
    @Column(name = "nombre_gewes")
    private String nombreGewes;
    @Column(name = "wsdl_gewes")
    private String wsdlGewes;
    @Column(name = "usuario_gewes")
    private String usuarioGewes;
    @Column(name = "password_gewes")
    private String passwordGewes;
    @Column(name = "tiempomax_gewes")
    private Integer tiempomaxGewes;

    public GenWebservice() {
    }

    public GenWebservice(Short ideGewes) {
        this.ideGewes = ideGewes;
    }

    public Short getIdeGewes() {
        return ideGewes;
    }

    public void setIdeGewes(Short ideGewes) {
        this.ideGewes = ideGewes;
    }

    public String getNombreGewes() {
        return nombreGewes;
    }

    public void setNombreGewes(String nombreGewes) {
        this.nombreGewes = nombreGewes;
    }

    public String getWsdlGewes() {
        return wsdlGewes;
    }

    public void setWsdlGewes(String wsdlGewes) {
        this.wsdlGewes = wsdlGewes;
    }

    public String getUsuarioGewes() {
        return usuarioGewes;
    }

    public void setUsuarioGewes(String usuarioGewes) {
        this.usuarioGewes = usuarioGewes;
    }

    public String getPasswordGewes() {
        return passwordGewes;
    }

    public void setPasswordGewes(String passwordGewes) {
        this.passwordGewes = passwordGewes;
    }

    public Integer getTiempomaxGewes() {
        return tiempomaxGewes;
    }

    public void setTiempomaxGewes(Integer tiempomaxGewes) {
        this.tiempomaxGewes = tiempomaxGewes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGewes != null ? ideGewes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenWebservice)) {
            return false;
        }
        GenWebservice other = (GenWebservice) object;
        if ((this.ideGewes == null && other.ideGewes != null) || (this.ideGewes != null && !this.ideGewes.equals(other.ideGewes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenWebservice[ ideGewes=" + ideGewes + " ]";
    }
    
}
