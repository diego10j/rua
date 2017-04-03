/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "sis_sucursal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisSucursal.findAll", query = "SELECT s FROM SisSucursal s"),
    @NamedQuery(name = "SisSucursal.findByIdeSucu", query = "SELECT s FROM SisSucursal s WHERE s.ideSucu = :ideSucu"),
    @NamedQuery(name = "SisSucursal.findByNomSucu", query = "SELECT s FROM SisSucursal s WHERE s.nomSucu = :nomSucu"),
    @NamedQuery(name = "SisSucursal.findByTelefonosSucu", query = "SELECT s FROM SisSucursal s WHERE s.telefonosSucu = :telefonosSucu"),
    @NamedQuery(name = "SisSucursal.findByDireccionSucu", query = "SELECT s FROM SisSucursal s WHERE s.direccionSucu = :direccionSucu"),
    @NamedQuery(name = "SisSucursal.findByContactoSuc", query = "SELECT s FROM SisSucursal s WHERE s.contactoSuc = :contactoSuc")})
public class SisSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_sucu")
    private Long ideSucu;
    @Column(name = "nom_sucu")
    private String nomSucu;
    @Column(name = "telefonos_sucu")
    private String telefonosSucu;
    @Column(name = "direccion_sucu")
    private String direccionSucu;
    @Column(name = "contacto_suc")
    private String contactoSuc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sisSucursal")
    private Collection<GenDepartamentoSucursal> genDepartamentoSucursalCollection;
    @OneToMany(mappedBy = "ideSucu")
    private Collection<SisUsuarioSucursal> sisUsuarioSucursalCollection;
   @OneToMany(mappedBy = "ideSucu")
    private Collection<GenTipLugaGeog> genTipLugaGeogCollection;
    @OneToMany(mappedBy = "ideSucu")
    private Collection<GenMes> genMesCollection;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public SisSucursal() {
    }

    public SisSucursal(Long ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Long getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Long ideSucu) {
        this.ideSucu = ideSucu;
    }

    public String getNomSucu() {
        return nomSucu;
    }

    public void setNomSucu(String nomSucu) {
        this.nomSucu = nomSucu;
    }

    public String getTelefonosSucu() {
        return telefonosSucu;
    }

    public void setTelefonosSucu(String telefonosSucu) {
        this.telefonosSucu = telefonosSucu;
    }

    public String getDireccionSucu() {
        return direccionSucu;
    }

    public void setDireccionSucu(String direccionSucu) {
        this.direccionSucu = direccionSucu;
    }

    public String getContactoSuc() {
        return contactoSuc;
    }

    public void setContactoSuc(String contactoSuc) {
        this.contactoSuc = contactoSuc;
    }



    @XmlTransient
    public Collection<GenDepartamentoSucursal> getGenDepartamentoSucursalCollection() {
        return genDepartamentoSucursalCollection;
    }

    public void setGenDepartamentoSucursalCollection(Collection<GenDepartamentoSucursal> genDepartamentoSucursalCollection) {
        this.genDepartamentoSucursalCollection = genDepartamentoSucursalCollection;
    }


    @XmlTransient
    public Collection<SisUsuarioSucursal> getSisUsuarioSucursalCollection() {
        return sisUsuarioSucursalCollection;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSucu != null ? ideSucu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisSucursal)) {
            return false;
        }
        SisSucursal other = (SisSucursal) object;
        if ((this.ideSucu == null && other.ideSucu != null) || (this.ideSucu != null && !this.ideSucu.equals(other.ideSucu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisSucursal[ ideSucu=" + ideSucu + " ]";
    }
    
}
