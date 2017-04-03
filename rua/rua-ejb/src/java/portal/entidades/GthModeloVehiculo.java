/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "gth_modelo_vehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthModeloVehiculo.findAll", query = "SELECT g FROM GthModeloVehiculo g"),
    @NamedQuery(name = "GthModeloVehiculo.findByIdeGtmov", query = "SELECT g FROM GthModeloVehiculo g WHERE g.gthModeloVehiculoPK.ideGtmov = :ideGtmov"),
    @NamedQuery(name = "GthModeloVehiculo.findByIdeGtmav", query = "SELECT g FROM GthModeloVehiculo g WHERE g.gthModeloVehiculoPK.ideGtmav = :ideGtmav"),
    @NamedQuery(name = "GthModeloVehiculo.findByDetalleGtmov", query = "SELECT g FROM GthModeloVehiculo g WHERE g.detalleGtmov = :detalleGtmov"),
    @NamedQuery(name = "GthModeloVehiculo.findByActivoGtmov", query = "SELECT g FROM GthModeloVehiculo g WHERE g.activoGtmov = :activoGtmov"),
    @NamedQuery(name = "GthModeloVehiculo.findByUsuarioIngre", query = "SELECT g FROM GthModeloVehiculo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthModeloVehiculo.findByFechaIngre", query = "SELECT g FROM GthModeloVehiculo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthModeloVehiculo.findByUsuarioActua", query = "SELECT g FROM GthModeloVehiculo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthModeloVehiculo.findByFechaActua", query = "SELECT g FROM GthModeloVehiculo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthModeloVehiculo.findByHoraIngre", query = "SELECT g FROM GthModeloVehiculo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthModeloVehiculo.findByHoraActua", query = "SELECT g FROM GthModeloVehiculo g WHERE g.horaActua = :horaActua")})
public class GthModeloVehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GthModeloVehiculoPK gthModeloVehiculoPK;
    @Column(name = "detalle_gtmov")
    private String detalleGtmov;
    @Column(name = "activo_gtmov")
    private Boolean activoGtmov;
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
    @JoinColumn(name = "ide_gtmav", referencedColumnName = "ide_gtmav", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GthMarcaVehiculo gthMarcaVehiculo;
    @OneToMany(mappedBy = "gthModeloVehiculo")
    private Collection<GthVehiculoEmpleado> gthVehiculoEmpleadoCollection;

    public GthModeloVehiculo() {
    }

    public GthModeloVehiculo(GthModeloVehiculoPK gthModeloVehiculoPK) {
        this.gthModeloVehiculoPK = gthModeloVehiculoPK;
    }

    public GthModeloVehiculo(int ideGtmov, int ideGtmav) {
        this.gthModeloVehiculoPK = new GthModeloVehiculoPK(ideGtmov, ideGtmav);
    }

    public GthModeloVehiculoPK getGthModeloVehiculoPK() {
        return gthModeloVehiculoPK;
    }

    public void setGthModeloVehiculoPK(GthModeloVehiculoPK gthModeloVehiculoPK) {
        this.gthModeloVehiculoPK = gthModeloVehiculoPK;
    }

    public String getDetalleGtmov() {
        return detalleGtmov;
    }

    public void setDetalleGtmov(String detalleGtmov) {
        this.detalleGtmov = detalleGtmov;
    }

    public Boolean getActivoGtmov() {
        return activoGtmov;
    }

    public void setActivoGtmov(Boolean activoGtmov) {
        this.activoGtmov = activoGtmov;
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

    public GthMarcaVehiculo getGthMarcaVehiculo() {
        return gthMarcaVehiculo;
    }

    public void setGthMarcaVehiculo(GthMarcaVehiculo gthMarcaVehiculo) {
        this.gthMarcaVehiculo = gthMarcaVehiculo;
    }

    @XmlTransient
    public Collection<GthVehiculoEmpleado> getGthVehiculoEmpleadoCollection() {
        return gthVehiculoEmpleadoCollection;
    }

    public void setGthVehiculoEmpleadoCollection(Collection<GthVehiculoEmpleado> gthVehiculoEmpleadoCollection) {
        this.gthVehiculoEmpleadoCollection = gthVehiculoEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gthModeloVehiculoPK != null ? gthModeloVehiculoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthModeloVehiculo)) {
            return false;
        }
        GthModeloVehiculo other = (GthModeloVehiculo) object;
        if ((this.gthModeloVehiculoPK == null && other.gthModeloVehiculoPK != null) || (this.gthModeloVehiculoPK != null && !this.gthModeloVehiculoPK.equals(other.gthModeloVehiculoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthModeloVehiculo[ gthModeloVehiculoPK=" + gthModeloVehiculoPK + " ]";
    }
    
}
