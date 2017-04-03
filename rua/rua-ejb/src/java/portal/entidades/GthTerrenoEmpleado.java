/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "gth_terreno_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTerrenoEmpleado.findAll", query = "SELECT g FROM GthTerrenoEmpleado g"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByIdeGttee", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.ideGttee = :ideGttee"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByDetalleGttee", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.detalleGttee = :detalleGttee"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByDireccionGttee", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.direccionGttee = :direccionGttee"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByAvaluoGttee", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.avaluoGttee = :avaluoGttee"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByActivoGttee", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.activoGttee = :activoGttee"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByFechaIngre", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByFechaActua", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByHoraIngre", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTerrenoEmpleado.findByHoraActua", query = "SELECT g FROM GthTerrenoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthTerrenoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttee")
    private Integer ideGttee;
    @Column(name = "detalle_gttee")
    private String detalleGttee;
    @Column(name = "direccion_gttee")
    private String direccionGttee;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avaluo_gttee")
    private BigDecimal avaluoGttee;
    @Column(name = "activo_gttee")
    private Boolean activoGttee;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public GthTerrenoEmpleado() {
    }

    public GthTerrenoEmpleado(Integer ideGttee) {
        this.ideGttee = ideGttee;
    }

    public Integer getIdeGttee() {
        return ideGttee;
    }

    public void setIdeGttee(Integer ideGttee) {
        this.ideGttee = ideGttee;
    }

    public String getDetalleGttee() {
        return detalleGttee;
    }

    public void setDetalleGttee(String detalleGttee) {
        this.detalleGttee = detalleGttee;
    }

    public String getDireccionGttee() {
        return direccionGttee;
    }

    public void setDireccionGttee(String direccionGttee) {
        this.direccionGttee = direccionGttee;
    }

    public BigDecimal getAvaluoGttee() {
        return avaluoGttee;
    }

    public void setAvaluoGttee(BigDecimal avaluoGttee) {
        this.avaluoGttee = avaluoGttee;
    }

    public Boolean getActivoGttee() {
        return activoGttee;
    }

    public void setActivoGttee(Boolean activoGttee) {
        this.activoGttee = activoGttee;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttee != null ? ideGttee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTerrenoEmpleado)) {
            return false;
        }
        GthTerrenoEmpleado other = (GthTerrenoEmpleado) object;
        if ((this.ideGttee == null && other.ideGttee != null) || (this.ideGttee != null && !this.ideGttee.equals(other.ideGttee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTerrenoEmpleado[ ideGttee=" + ideGttee + " ]";
    }
    
}
