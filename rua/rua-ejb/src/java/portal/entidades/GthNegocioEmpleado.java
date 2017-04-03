/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "gth_negocio_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthNegocioEmpleado.findAll", query = "SELECT g FROM GthNegocioEmpleado g"),
    @NamedQuery(name = "GthNegocioEmpleado.findByIdeGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.ideGtnee = :ideGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByPropioGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.propioGtnee = :propioGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByNombreComercialGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.nombreComercialGtnee = :nombreComercialGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByRucGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.rucGtnee = :rucGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByFechaVigenciaRucGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.fechaVigenciaRucGtnee = :fechaVigenciaRucGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByTotalVentaGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.totalVentaGtnee = :totalVentaGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByTotalGastoGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.totalGastoGtnee = :totalGastoGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByTotalUtilidadGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.totalUtilidadGtnee = :totalUtilidadGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findBySocioGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.socioGtnee = :socioGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByActivoGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.activoGtnee = :activoGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthNegocioEmpleado.findByFechaIngre", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthNegocioEmpleado.findByUsuarioActua", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthNegocioEmpleado.findByFechaActua", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthNegocioEmpleado.findByHoraIngre", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthNegocioEmpleado.findByHoraActua", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.horaActua = :horaActua")})
public class GthNegocioEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtnee")
    private Integer ideGtnee;
    @Column(name = "propio_gtnee")
    private Integer propioGtnee;
    @Column(name = "nombre_comercial_gtnee")
    private String nombreComercialGtnee;
    @Column(name = "ruc_gtnee")
    private String rucGtnee;
    @Column(name = "fecha_vigencia_ruc_gtnee")
    @Temporal(TemporalType.DATE)
    private Date fechaVigenciaRucGtnee;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_venta_gtnee")
    private BigDecimal totalVentaGtnee;
    @Column(name = "total_gasto_gtnee")
    private BigDecimal totalGastoGtnee;
    @Column(name = "total_utilidad_gtnee")
    private BigDecimal totalUtilidadGtnee;
    @Column(name = "socio_gtnee")
    private Integer socioGtnee;
    @Column(name = "activo_gtnee")
    private Boolean activoGtnee;
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
    @OneToMany(mappedBy = "ideGtnee")
    private Collection<GthDireccion> gthDireccionCollection;
    @OneToMany(mappedBy = "ideGtnee")
    private Collection<GthTelefono> gthTelefonoCollection;
    @OneToMany(mappedBy = "ideGtnee")
    private Collection<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaCollection;
    @OneToMany(mappedBy = "ideGtnee")
    private Collection<GthArchivoEmpleado> gthArchivoEmpleadoCollection;
    @JoinColumn(name = "ide_gttae", referencedColumnName = "ide_gttae")
    @ManyToOne
    private GthTipoActividadEconomica ideGttae;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gtacr", referencedColumnName = "ide_gtacr")
    @ManyToOne
    private GthActividadRuc ideGtacr;

    public GthNegocioEmpleado() {
    }

    public GthNegocioEmpleado(Integer ideGtnee) {
        this.ideGtnee = ideGtnee;
    }

    public Integer getIdeGtnee() {
        return ideGtnee;
    }

    public void setIdeGtnee(Integer ideGtnee) {
        this.ideGtnee = ideGtnee;
    }

    public Integer getPropioGtnee() {
        return propioGtnee;
    }

    public void setPropioGtnee(Integer propioGtnee) {
        this.propioGtnee = propioGtnee;
    }

    public String getNombreComercialGtnee() {
        return nombreComercialGtnee;
    }

    public void setNombreComercialGtnee(String nombreComercialGtnee) {
        this.nombreComercialGtnee = nombreComercialGtnee;
    }

    public String getRucGtnee() {
        return rucGtnee;
    }

    public void setRucGtnee(String rucGtnee) {
        this.rucGtnee = rucGtnee;
    }

    public Date getFechaVigenciaRucGtnee() {
        return fechaVigenciaRucGtnee;
    }

    public void setFechaVigenciaRucGtnee(Date fechaVigenciaRucGtnee) {
        this.fechaVigenciaRucGtnee = fechaVigenciaRucGtnee;
    }

    public BigDecimal getTotalVentaGtnee() {
        return totalVentaGtnee;
    }

    public void setTotalVentaGtnee(BigDecimal totalVentaGtnee) {
        this.totalVentaGtnee = totalVentaGtnee;
    }

    public BigDecimal getTotalGastoGtnee() {
        return totalGastoGtnee;
    }

    public void setTotalGastoGtnee(BigDecimal totalGastoGtnee) {
        this.totalGastoGtnee = totalGastoGtnee;
    }

    public BigDecimal getTotalUtilidadGtnee() {
        return totalUtilidadGtnee;
    }

    public void setTotalUtilidadGtnee(BigDecimal totalUtilidadGtnee) {
        this.totalUtilidadGtnee = totalUtilidadGtnee;
    }

    public Integer getSocioGtnee() {
        return socioGtnee;
    }

    public void setSocioGtnee(Integer socioGtnee) {
        this.socioGtnee = socioGtnee;
    }

    public Boolean getActivoGtnee() {
        return activoGtnee;
    }

    public void setActivoGtnee(Boolean activoGtnee) {
        this.activoGtnee = activoGtnee;
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
    public Collection<GthDireccion> getGthDireccionCollection() {
        return gthDireccionCollection;
    }

    public void setGthDireccionCollection(Collection<GthDireccion> gthDireccionCollection) {
        this.gthDireccionCollection = gthDireccionCollection;
    }

    @XmlTransient
    public Collection<GthTelefono> getGthTelefonoCollection() {
        return gthTelefonoCollection;
    }

    public void setGthTelefonoCollection(Collection<GthTelefono> gthTelefonoCollection) {
        this.gthTelefonoCollection = gthTelefonoCollection;
    }

    @XmlTransient
    public Collection<GthParticipaNegocioEmplea> getGthParticipaNegocioEmpleaCollection() {
        return gthParticipaNegocioEmpleaCollection;
    }

    public void setGthParticipaNegocioEmpleaCollection(Collection<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaCollection) {
        this.gthParticipaNegocioEmpleaCollection = gthParticipaNegocioEmpleaCollection;
    }

    @XmlTransient
    public Collection<GthArchivoEmpleado> getGthArchivoEmpleadoCollection() {
        return gthArchivoEmpleadoCollection;
    }

    public void setGthArchivoEmpleadoCollection(Collection<GthArchivoEmpleado> gthArchivoEmpleadoCollection) {
        this.gthArchivoEmpleadoCollection = gthArchivoEmpleadoCollection;
    }

    public GthTipoActividadEconomica getIdeGttae() {
        return ideGttae;
    }

    public void setIdeGttae(GthTipoActividadEconomica ideGttae) {
        this.ideGttae = ideGttae;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthActividadRuc getIdeGtacr() {
        return ideGtacr;
    }

    public void setIdeGtacr(GthActividadRuc ideGtacr) {
        this.ideGtacr = ideGtacr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtnee != null ? ideGtnee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthNegocioEmpleado)) {
            return false;
        }
        GthNegocioEmpleado other = (GthNegocioEmpleado) object;
        if ((this.ideGtnee == null && other.ideGtnee != null) || (this.ideGtnee != null && !this.ideGtnee.equals(other.ideGtnee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthNegocioEmpleado[ ideGtnee=" + ideGtnee + " ]";
    }
    
}
