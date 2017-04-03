/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "gth_tipo_documento_identidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findAll", query = "SELECT g FROM GthTipoDocumentoIdentidad g"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByIdeGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.ideGttdi = :ideGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByDetalleGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.detalleGttdi = :detalleGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByCodigoSbsGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.codigoSbsGttdi = :codigoSbsGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByCodigoSriGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.codigoSriGttdi = :codigoSriGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByActivoGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.activoGttdi = :activoGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByUsuarioIngre", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByFechaIngre", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByUsuarioActua", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByFechaActua", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByHoraIngre", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByHoraActua", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.horaActua = :horaActua")})
public class GthTipoDocumentoIdentidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttdi")
    private Integer ideGttdi;
    @Column(name = "detalle_gttdi")
    private String detalleGttdi;
    @Column(name = "codigo_sbs_gttdi")
    private String codigoSbsGttdi;
    @Column(name = "codigo_sri_gttdi")
    private String codigoSriGttdi;
    @Column(name = "activo_gttdi")
    private Boolean activoGttdi;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideGttdi")
    private Collection<GthConyuge> gthConyugeCollection;
    @OneToMany(mappedBy = "ideGttdi")
    private Collection<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttdi")
    private Collection<GthEmpleado> gthEmpleadoCollection;
    @OneToMany(mappedBy = "ideGttdi")
    private Collection<NrhGarante> nrhGaranteCollection;
    @OneToMany(mappedBy = "gthIdeGttdi")
    private Collection<NrhGarante> nrhGaranteCollection1;
    @OneToMany(mappedBy = "ideGttdi")
    private Collection<GthFamiliar> gthFamiliarCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttdi")
    private Collection<GthCargasFamiliares> gthCargasFamiliaresCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttdi")
    private Collection<GenBeneficiario> genBeneficiarioCollection;
    @OneToMany(mappedBy = "gthIdeGttdi")
    private Collection<GenBeneficiario> genBeneficiarioCollection1;

    public GthTipoDocumentoIdentidad() {
    }

    public GthTipoDocumentoIdentidad(Integer ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public Integer getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(Integer ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public String getDetalleGttdi() {
        return detalleGttdi;
    }

    public void setDetalleGttdi(String detalleGttdi) {
        this.detalleGttdi = detalleGttdi;
    }

    public String getCodigoSbsGttdi() {
        return codigoSbsGttdi;
    }

    public void setCodigoSbsGttdi(String codigoSbsGttdi) {
        this.codigoSbsGttdi = codigoSbsGttdi;
    }

    public String getCodigoSriGttdi() {
        return codigoSriGttdi;
    }

    public void setCodigoSriGttdi(String codigoSriGttdi) {
        this.codigoSriGttdi = codigoSriGttdi;
    }

    public Boolean getActivoGttdi() {
        return activoGttdi;
    }

    public void setActivoGttdi(Boolean activoGttdi) {
        this.activoGttdi = activoGttdi;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    @XmlTransient
    public Collection<GthConyuge> getGthConyugeCollection() {
        return gthConyugeCollection;
    }

    public void setGthConyugeCollection(Collection<GthConyuge> gthConyugeCollection) {
        this.gthConyugeCollection = gthConyugeCollection;
    }

    @XmlTransient
    public Collection<GthParticipaNegocioEmplea> getGthParticipaNegocioEmpleaCollection() {
        return gthParticipaNegocioEmpleaCollection;
    }

    public void setGthParticipaNegocioEmpleaCollection(Collection<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaCollection) {
        this.gthParticipaNegocioEmpleaCollection = gthParticipaNegocioEmpleaCollection;
    }

    @XmlTransient
    public Collection<GthEmpleado> getGthEmpleadoCollection() {
        return gthEmpleadoCollection;
    }

    public void setGthEmpleadoCollection(Collection<GthEmpleado> gthEmpleadoCollection) {
        this.gthEmpleadoCollection = gthEmpleadoCollection;
    }

    @XmlTransient
    public Collection<NrhGarante> getNrhGaranteCollection() {
        return nrhGaranteCollection;
    }

    public void setNrhGaranteCollection(Collection<NrhGarante> nrhGaranteCollection) {
        this.nrhGaranteCollection = nrhGaranteCollection;
    }

    @XmlTransient
    public Collection<NrhGarante> getNrhGaranteCollection1() {
        return nrhGaranteCollection1;
    }

    public void setNrhGaranteCollection1(Collection<NrhGarante> nrhGaranteCollection1) {
        this.nrhGaranteCollection1 = nrhGaranteCollection1;
    }

    @XmlTransient
    public Collection<GthFamiliar> getGthFamiliarCollection() {
        return gthFamiliarCollection;
    }

    public void setGthFamiliarCollection(Collection<GthFamiliar> gthFamiliarCollection) {
        this.gthFamiliarCollection = gthFamiliarCollection;
    }

    @XmlTransient
    public Collection<GthCargasFamiliares> getGthCargasFamiliaresCollection() {
        return gthCargasFamiliaresCollection;
    }

    public void setGthCargasFamiliaresCollection(Collection<GthCargasFamiliares> gthCargasFamiliaresCollection) {
        this.gthCargasFamiliaresCollection = gthCargasFamiliaresCollection;
    }

    @XmlTransient
    public Collection<GenBeneficiario> getGenBeneficiarioCollection() {
        return genBeneficiarioCollection;
    }

    public void setGenBeneficiarioCollection(Collection<GenBeneficiario> genBeneficiarioCollection) {
        this.genBeneficiarioCollection = genBeneficiarioCollection;
    }

    @XmlTransient
    public Collection<GenBeneficiario> getGenBeneficiarioCollection1() {
        return genBeneficiarioCollection1;
    }

    public void setGenBeneficiarioCollection1(Collection<GenBeneficiario> genBeneficiarioCollection1) {
        this.genBeneficiarioCollection1 = genBeneficiarioCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttdi != null ? ideGttdi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoDocumentoIdentidad)) {
            return false;
        }
        GthTipoDocumentoIdentidad other = (GthTipoDocumentoIdentidad) object;
        if ((this.ideGttdi == null && other.ideGttdi != null) || (this.ideGttdi != null && !this.ideGttdi.equals(other.ideGttdi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoDocumentoIdentidad[ ideGttdi=" + ideGttdi + " ]";
    }
    
}
