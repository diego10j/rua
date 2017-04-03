/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "gth_correo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthCorreo.findAll", query = "SELECT g FROM GthCorreo g"),
    @NamedQuery(name = "GthCorreo.findByIdeGtcor", query = "SELECT g FROM GthCorreo g WHERE g.ideGtcor = :ideGtcor"),
    @NamedQuery(name = "GthCorreo.findByDetalleGtcor", query = "SELECT g FROM GthCorreo g WHERE g.detalleGtcor = :detalleGtcor"),
    @NamedQuery(name = "GthCorreo.findByActivoGtcor", query = "SELECT g FROM GthCorreo g WHERE g.activoGtcor = :activoGtcor"),
    @NamedQuery(name = "GthCorreo.findByUsuarioIngre", query = "SELECT g FROM GthCorreo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCorreo.findByFechaIngre", query = "SELECT g FROM GthCorreo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCorreo.findByUsuarioActua", query = "SELECT g FROM GthCorreo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCorreo.findByFechaActua", query = "SELECT g FROM GthCorreo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCorreo.findByHoraIngre", query = "SELECT g FROM GthCorreo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCorreo.findByHoraActua", query = "SELECT g FROM GthCorreo g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthCorreo.findByNotificacionGtcor", query = "SELECT g FROM GthCorreo g WHERE g.notificacionGtcor = :notificacionGtcor")})
public class GthCorreo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtcor")
    private Integer ideGtcor;
    @Column(name = "detalle_gtcor")
    private String detalleGtcor;
    @Column(name = "activo_gtcor")
    private Boolean activoGtcor;
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
    @Column(name = "notificacion_gtcor")
    private Boolean notificacionGtcor;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public GthCorreo() {
    }

    public GthCorreo(Integer ideGtcor) {
        this.ideGtcor = ideGtcor;
    }

    public Integer getIdeGtcor() {
        return ideGtcor;
    }

    public void setIdeGtcor(Integer ideGtcor) {
        this.ideGtcor = ideGtcor;
    }

    public String getDetalleGtcor() {
        return detalleGtcor;
    }

    public void setDetalleGtcor(String detalleGtcor) {
        this.detalleGtcor = detalleGtcor;
    }

    public Boolean getActivoGtcor() {
        return activoGtcor;
    }

    public void setActivoGtcor(Boolean activoGtcor) {
        this.activoGtcor = activoGtcor;
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

    public Boolean getNotificacionGtcor() {
        return notificacionGtcor;
    }

    public void setNotificacionGtcor(Boolean notificacionGtcor) {
        this.notificacionGtcor = notificacionGtcor;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcor != null ? ideGtcor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCorreo)) {
            return false;
        }
        GthCorreo other = (GthCorreo) object;
        if ((this.ideGtcor == null && other.ideGtcor != null) || (this.ideGtcor != null && !this.ideGtcor.equals(other.ideGtcor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCorreo[ ideGtcor=" + ideGtcor + " ]";
    }
    
}
