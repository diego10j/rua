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
@Table(name = "gth_tipo_parentesco_relacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoParentescoRelacion.findAll", query = "SELECT g FROM GthTipoParentescoRelacion g"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByIdeGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.ideGttpr = :ideGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByDetalleGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.detalleGttpr = :detalleGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByCodigoSbsGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.codigoSbsGttpr = :codigoSbsGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByActivoGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.activoGttpr = :activoGttpr"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByUsuarioIngre", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByFechaIngre", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByUsuarioActua", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByFechaActua", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByHoraIngre", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByHoraActua", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthTipoParentescoRelacion.findByDependienteGttpr", query = "SELECT g FROM GthTipoParentescoRelacion g WHERE g.dependienteGttpr = :dependienteGttpr")})
public class GthTipoParentescoRelacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttpr")
    private Integer ideGttpr;
    @Column(name = "detalle_gttpr")
    private String detalleGttpr;
    @Column(name = "codigo_sbs_gttpr")
    private String codigoSbsGttpr;
    @Column(name = "activo_gttpr")
    private Boolean activoGttpr;
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
    @Column(name = "dependiente_gttpr")
    private Boolean dependienteGttpr;
    @OneToMany(mappedBy = "ideGttpr")
    private Collection<GthBeneficiarioSeguro> gthBeneficiarioSeguroCollection;
    @OneToMany(mappedBy = "ideGttpr")
    private Collection<GthFamiliar> gthFamiliarCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttpr")
    private Collection<GthCargasFamiliares> gthCargasFamiliaresCollection;
    @OneToMany(mappedBy = "ideGttpr")
    private Collection<GthPersonaEmergencia> gthPersonaEmergenciaCollection;
    @OneToMany(mappedBy = "ideGttpr")
    private Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection;

    public GthTipoParentescoRelacion() {
    }

    public GthTipoParentescoRelacion(Integer ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public Integer getIdeGttpr() {
        return ideGttpr;
    }

    public void setIdeGttpr(Integer ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public String getDetalleGttpr() {
        return detalleGttpr;
    }

    public void setDetalleGttpr(String detalleGttpr) {
        this.detalleGttpr = detalleGttpr;
    }

    public String getCodigoSbsGttpr() {
        return codigoSbsGttpr;
    }

    public void setCodigoSbsGttpr(String codigoSbsGttpr) {
        this.codigoSbsGttpr = codigoSbsGttpr;
    }

    public Boolean getActivoGttpr() {
        return activoGttpr;
    }

    public void setActivoGttpr(Boolean activoGttpr) {
        this.activoGttpr = activoGttpr;
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

    public Boolean getDependienteGttpr() {
        return dependienteGttpr;
    }

    public void setDependienteGttpr(Boolean dependienteGttpr) {
        this.dependienteGttpr = dependienteGttpr;
    }

    @XmlTransient
    public Collection<GthBeneficiarioSeguro> getGthBeneficiarioSeguroCollection() {
        return gthBeneficiarioSeguroCollection;
    }

    public void setGthBeneficiarioSeguroCollection(Collection<GthBeneficiarioSeguro> gthBeneficiarioSeguroCollection) {
        this.gthBeneficiarioSeguroCollection = gthBeneficiarioSeguroCollection;
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
    public Collection<GthPersonaEmergencia> getGthPersonaEmergenciaCollection() {
        return gthPersonaEmergenciaCollection;
    }

    public void setGthPersonaEmergenciaCollection(Collection<GthPersonaEmergencia> gthPersonaEmergenciaCollection) {
        this.gthPersonaEmergenciaCollection = gthPersonaEmergenciaCollection;
    }

    @XmlTransient
    public Collection<NrhRetencionJudicial> getNrhRetencionJudicialCollection() {
        return nrhRetencionJudicialCollection;
    }

    public void setNrhRetencionJudicialCollection(Collection<NrhRetencionJudicial> nrhRetencionJudicialCollection) {
        this.nrhRetencionJudicialCollection = nrhRetencionJudicialCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttpr != null ? ideGttpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoParentescoRelacion)) {
            return false;
        }
        GthTipoParentescoRelacion other = (GthTipoParentescoRelacion) object;
        if ((this.ideGttpr == null && other.ideGttpr != null) || (this.ideGttpr != null && !this.ideGttpr.equals(other.ideGttpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoParentescoRelacion[ ideGttpr=" + ideGttpr + " ]";
    }
    
}
