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
@Table(name = "sis_periodo_clave")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisPeriodoClave.findAll", query = "SELECT s FROM SisPeriodoClave s"),
    @NamedQuery(name = "SisPeriodoClave.findByIdePecl", query = "SELECT s FROM SisPeriodoClave s WHERE s.idePecl = :idePecl"),
    @NamedQuery(name = "SisPeriodoClave.findByIdeEmpr", query = "SELECT s FROM SisPeriodoClave s WHERE s.ideEmpr = :ideEmpr"),
    @NamedQuery(name = "SisPeriodoClave.findByNomPecl", query = "SELECT s FROM SisPeriodoClave s WHERE s.nomPecl = :nomPecl"),
    @NamedQuery(name = "SisPeriodoClave.findByNumDias", query = "SELECT s FROM SisPeriodoClave s WHERE s.numDias = :numDias")})
public class SisPeriodoClave implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_pecl")
    private Integer idePecl;
    @Column(name = "ide_empr")
    private Integer ideEmpr;
    @Basic(optional = false)
    @Column(name = "nom_pecl")
    private String nomPecl;
    @Basic(optional = false)
    @Column(name = "num_dias")
    private int numDias;

    public SisPeriodoClave() {
    }

    public SisPeriodoClave(Integer idePecl) {
        this.idePecl = idePecl;
    }

    public SisPeriodoClave(Integer idePecl, String nomPecl, int numDias) {
        this.idePecl = idePecl;
        this.nomPecl = nomPecl;
        this.numDias = numDias;
    }

    public Integer getIdePecl() {
        return idePecl;
    }

    public void setIdePecl(Integer idePecl) {
        this.idePecl = idePecl;
    }

    public Integer getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(Integer ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    public String getNomPecl() {
        return nomPecl;
    }

    public void setNomPecl(String nomPecl) {
        this.nomPecl = nomPecl;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePecl != null ? idePecl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisPeriodoClave)) {
            return false;
        }
        SisPeriodoClave other = (SisPeriodoClave) object;
        if ((this.idePecl == null && other.idePecl != null) || (this.idePecl != null && !this.idePecl.equals(other.idePecl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisPeriodoClave[ idePecl=" + idePecl + " ]";
    }
    
}
