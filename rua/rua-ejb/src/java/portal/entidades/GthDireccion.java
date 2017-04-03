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
import javax.persistence.JoinColumns;
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
@Table(name = "gth_direccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthDireccion.findAll", query = "SELECT g FROM GthDireccion g"),
    @NamedQuery(name = "GthDireccion.findByIdeGtdir", query = "SELECT g FROM GthDireccion g WHERE g.ideGtdir = :ideGtdir"),
    @NamedQuery(name = "GthDireccion.findByIdeNrgar", query = "SELECT g FROM GthDireccion g WHERE g.ideNrgar = :ideNrgar"),
    @NamedQuery(name = "GthDireccion.findByDetalleGtdir", query = "SELECT g FROM GthDireccion g WHERE g.detalleGtdir = :detalleGtdir"),
    @NamedQuery(name = "GthDireccion.findByReferenciaGtdir", query = "SELECT g FROM GthDireccion g WHERE g.referenciaGtdir = :referenciaGtdir"),
    @NamedQuery(name = "GthDireccion.findByActivoGtdir", query = "SELECT g FROM GthDireccion g WHERE g.activoGtdir = :activoGtdir"),
    @NamedQuery(name = "GthDireccion.findByUsuarioIngre", query = "SELECT g FROM GthDireccion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthDireccion.findByFechaIngre", query = "SELECT g FROM GthDireccion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthDireccion.findByUsuarioActua", query = "SELECT g FROM GthDireccion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthDireccion.findByFechaActua", query = "SELECT g FROM GthDireccion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthDireccion.findByHoraIngre", query = "SELECT g FROM GthDireccion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthDireccion.findByHoraActua", query = "SELECT g FROM GthDireccion g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthDireccion.findByNotificacionGtdir", query = "SELECT g FROM GthDireccion g WHERE g.notificacionGtdir = :notificacionGtdir")})
public class GthDireccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtdir")
    private Integer ideGtdir;
    @Column(name = "ide_nrgar")
    private Integer ideNrgar;
    @Basic(optional = false)
    @Column(name = "detalle_gtdir")
    private String detalleGtdir;
    @Column(name = "referencia_gtdir")
    private String referenciaGtdir;
    @Column(name = "activo_gtdir")
    private Boolean activoGtdir;
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
    @Column(name = "notificacion_gtdir")
    private Boolean notificacionGtdir;
    @JoinColumns({
        @JoinColumn(name = "ide_gtpee", referencedColumnName = "ide_gtpee"),
        @JoinColumn(name = "gth_ide_gtemp", referencedColumnName = "ide_gtemp")})
    @ManyToOne
    private GthPersonaEmergencia gthPersonaEmergencia;
    @JoinColumn(name = "ide_gtnee", referencedColumnName = "ide_gtnee")
    @ManyToOne
    private GthNegocioEmpleado ideGtnee;
    @JoinColumn(name = "ide_gtele", referencedColumnName = "ide_gtele")
    @ManyToOne
    private GthExperienciaLaboralEmplea ideGtele;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public GthDireccion() {
    }

    public GthDireccion(Integer ideGtdir) {
        this.ideGtdir = ideGtdir;
    }

    public GthDireccion(Integer ideGtdir, String detalleGtdir) {
        this.ideGtdir = ideGtdir;
        this.detalleGtdir = detalleGtdir;
    }

    public Integer getIdeGtdir() {
        return ideGtdir;
    }

    public void setIdeGtdir(Integer ideGtdir) {
        this.ideGtdir = ideGtdir;
    }

    public Integer getIdeNrgar() {
        return ideNrgar;
    }

    public void setIdeNrgar(Integer ideNrgar) {
        this.ideNrgar = ideNrgar;
    }

    public String getDetalleGtdir() {
        return detalleGtdir;
    }

    public void setDetalleGtdir(String detalleGtdir) {
        this.detalleGtdir = detalleGtdir;
    }

    public String getReferenciaGtdir() {
        return referenciaGtdir;
    }

    public void setReferenciaGtdir(String referenciaGtdir) {
        this.referenciaGtdir = referenciaGtdir;
    }

    public Boolean getActivoGtdir() {
        return activoGtdir;
    }

    public void setActivoGtdir(Boolean activoGtdir) {
        this.activoGtdir = activoGtdir;
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

    public Boolean getNotificacionGtdir() {
        return notificacionGtdir;
    }

    public void setNotificacionGtdir(Boolean notificacionGtdir) {
        this.notificacionGtdir = notificacionGtdir;
    }

    public GthPersonaEmergencia getGthPersonaEmergencia() {
        return gthPersonaEmergencia;
    }

    public void setGthPersonaEmergencia(GthPersonaEmergencia gthPersonaEmergencia) {
        this.gthPersonaEmergencia = gthPersonaEmergencia;
    }

    public GthNegocioEmpleado getIdeGtnee() {
        return ideGtnee;
    }

    public void setIdeGtnee(GthNegocioEmpleado ideGtnee) {
        this.ideGtnee = ideGtnee;
    }

    public GthExperienciaLaboralEmplea getIdeGtele() {
        return ideGtele;
    }

    public void setIdeGtele(GthExperienciaLaboralEmplea ideGtele) {
        this.ideGtele = ideGtele;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
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
        hash += (ideGtdir != null ? ideGtdir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthDireccion)) {
            return false;
        }
        GthDireccion other = (GthDireccion) object;
        if ((this.ideGtdir == null && other.ideGtdir != null) || (this.ideGtdir != null && !this.ideGtdir.equals(other.ideGtdir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthDireccion[ ideGtdir=" + ideGtdir + " ]";
    }
    
}
