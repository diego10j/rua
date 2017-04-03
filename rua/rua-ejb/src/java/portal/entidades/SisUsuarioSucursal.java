/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "sis_usuario_sucursal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisUsuarioSucursal.findAll", query = "SELECT s FROM SisUsuarioSucursal s"),
    @NamedQuery(name = "SisUsuarioSucursal.findByIdeUssu", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.ideUssu = :ideUssu"),
    @NamedQuery(name = "SisUsuarioSucursal.findBySisIdeSucu", query = "SELECT s FROM SisUsuarioSucursal s WHERE s.sisIdeSucu = :sisIdeSucu")})
public class SisUsuarioSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_ussu")
    private Long ideUssu;
    @Column(name = "sis_ide_sucu")
    private BigInteger sisIdeSucu;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu")
    @ManyToOne
    private SisSucursal ideSucu;

    public SisUsuarioSucursal() {
    }

    public SisUsuarioSucursal(Long ideUssu) {
        this.ideUssu = ideUssu;
    }

    public Long getIdeUssu() {
        return ideUssu;
    }

    public void setIdeUssu(Long ideUssu) {
        this.ideUssu = ideUssu;
    }

    public BigInteger getSisIdeSucu() {
        return sisIdeSucu;
    }

    public void setSisIdeSucu(BigInteger sisIdeSucu) {
        this.sisIdeSucu = sisIdeSucu;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
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
        hash += (ideUssu != null ? ideUssu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisUsuarioSucursal)) {
            return false;
        }
        SisUsuarioSucursal other = (SisUsuarioSucursal) object;
        if ((this.ideUssu == null && other.ideUssu != null) || (this.ideUssu != null && !this.ideUssu.equals(other.ideUssu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisUsuarioSucursal[ ideUssu=" + ideUssu + " ]";
    }
    
}
