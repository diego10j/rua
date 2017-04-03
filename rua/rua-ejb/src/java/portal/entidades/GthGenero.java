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
@Table(name = "gth_genero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthGenero.findAll", query = "SELECT g FROM GthGenero g"),
    @NamedQuery(name = "GthGenero.findByIdeGtgen", query = "SELECT g FROM GthGenero g WHERE g.ideGtgen = :ideGtgen"),
    @NamedQuery(name = "GthGenero.findByDetalleGtgen", query = "SELECT g FROM GthGenero g WHERE g.detalleGtgen = :detalleGtgen"),
    @NamedQuery(name = "GthGenero.findByCodigoSbsGtgen", query = "SELECT g FROM GthGenero g WHERE g.codigoSbsGtgen = :codigoSbsGtgen"),
    @NamedQuery(name = "GthGenero.findByActivoGtgen", query = "SELECT g FROM GthGenero g WHERE g.activoGtgen = :activoGtgen"),
    @NamedQuery(name = "GthGenero.findByUsuarioIngre", query = "SELECT g FROM GthGenero g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthGenero.findByFechaIngre", query = "SELECT g FROM GthGenero g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthGenero.findByUsuarioActua", query = "SELECT g FROM GthGenero g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthGenero.findByFechaActua", query = "SELECT g FROM GthGenero g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthGenero.findByHoraIngre", query = "SELECT g FROM GthGenero g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthGenero.findByHoraActua", query = "SELECT g FROM GthGenero g WHERE g.horaActua = :horaActua")})
public class GthGenero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtgen")
    private Integer ideGtgen;
    @Column(name = "detalle_gtgen")
    private String detalleGtgen;
    @Column(name = "codigo_sbs_gtgen")
    private String codigoSbsGtgen;
    @Column(name = "activo_gtgen")
    private Boolean activoGtgen;
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
    @OneToMany(mappedBy = "ideGtgen")
    private Collection<GthConyuge> gthConyugeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtgen")
    private Collection<GthEmpleado> gthEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtgen")
    private Collection<GthCargasFamiliares> gthCargasFamiliaresCollection;

    public GthGenero() {
    }

    public GthGenero(Integer ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public Integer getIdeGtgen() {
        return ideGtgen;
    }

    public void setIdeGtgen(Integer ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public String getDetalleGtgen() {
        return detalleGtgen;
    }

    public void setDetalleGtgen(String detalleGtgen) {
        this.detalleGtgen = detalleGtgen;
    }

    public String getCodigoSbsGtgen() {
        return codigoSbsGtgen;
    }

    public void setCodigoSbsGtgen(String codigoSbsGtgen) {
        this.codigoSbsGtgen = codigoSbsGtgen;
    }

    public Boolean getActivoGtgen() {
        return activoGtgen;
    }

    public void setActivoGtgen(Boolean activoGtgen) {
        this.activoGtgen = activoGtgen;
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
    public Collection<GthEmpleado> getGthEmpleadoCollection() {
        return gthEmpleadoCollection;
    }

    public void setGthEmpleadoCollection(Collection<GthEmpleado> gthEmpleadoCollection) {
        this.gthEmpleadoCollection = gthEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthCargasFamiliares> getGthCargasFamiliaresCollection() {
        return gthCargasFamiliaresCollection;
    }

    public void setGthCargasFamiliaresCollection(Collection<GthCargasFamiliares> gthCargasFamiliaresCollection) {
        this.gthCargasFamiliaresCollection = gthCargasFamiliaresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtgen != null ? ideGtgen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthGenero)) {
            return false;
        }
        GthGenero other = (GthGenero) object;
        if ((this.ideGtgen == null && other.ideGtgen != null) || (this.ideGtgen != null && !this.ideGtgen.equals(other.ideGtgen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthGenero[ ideGtgen=" + ideGtgen + " ]";
    }
    
}
