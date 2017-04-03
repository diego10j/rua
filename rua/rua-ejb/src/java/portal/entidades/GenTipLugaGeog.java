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
@Table(name = "gen_tip_luga_geog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenTipLugaGeog.findAll", query = "SELECT g FROM GenTipLugaGeog g"),
    @NamedQuery(name = "GenTipLugaGeog.findByIdeGetlu", query = "SELECT g FROM GenTipLugaGeog g WHERE g.ideGetlu = :ideGetlu"),
    @NamedQuery(name = "GenTipLugaGeog.findByNombreGetlu", query = "SELECT g FROM GenTipLugaGeog g WHERE g.nombreGetlu = :nombreGetlu")})
public class GenTipLugaGeog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_getlu")
    private Long ideGetlu;
    @Basic(optional = false)
    @Column(name = "nombre_getlu")
    private String nombreGetlu;

    @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu")
    @ManyToOne
    private SisSucursal ideSucu;


    public GenTipLugaGeog() {
    }

    public GenTipLugaGeog(Long ideGetlu) {
        this.ideGetlu = ideGetlu;
    }

    public GenTipLugaGeog(Long ideGetlu, String nombreGetlu) {
        this.ideGetlu = ideGetlu;
        this.nombreGetlu = nombreGetlu;
    }

    public Long getIdeGetlu() {
        return ideGetlu;
    }

    public void setIdeGetlu(Long ideGetlu) {
        this.ideGetlu = ideGetlu;
    }

    public String getNombreGetlu() {
        return nombreGetlu;
    }

    public void setNombreGetlu(String nombreGetlu) {
        this.nombreGetlu = nombreGetlu;
    }

 
    public SisSucursal getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(SisSucursal ideSucu) {
        this.ideSucu = ideSucu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetlu != null ? ideGetlu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipLugaGeog)) {
            return false;
        }
        GenTipLugaGeog other = (GenTipLugaGeog) object;
        if ((this.ideGetlu == null && other.ideGetlu != null) || (this.ideGetlu != null && !this.ideGetlu.equals(other.ideGetlu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipLugaGeog[ ideGetlu=" + ideGetlu + " ]";
    }
    
}
