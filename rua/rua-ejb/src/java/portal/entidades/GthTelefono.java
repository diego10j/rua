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
@Table(name = "gth_telefono")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTelefono.findAll", query = "SELECT g FROM GthTelefono g"),
    @NamedQuery(name = "GthTelefono.findByIdeGttel", query = "SELECT g FROM GthTelefono g WHERE g.ideGttel = :ideGttel"),
    @NamedQuery(name = "GthTelefono.findByIdeNrgar", query = "SELECT g FROM GthTelefono g WHERE g.ideNrgar = :ideNrgar"),
    @NamedQuery(name = "GthTelefono.findByNumeroTelefonoGttel", query = "SELECT g FROM GthTelefono g WHERE g.numeroTelefonoGttel = :numeroTelefonoGttel"),
    @NamedQuery(name = "GthTelefono.findByActivoGttel", query = "SELECT g FROM GthTelefono g WHERE g.activoGttel = :activoGttel"),
    @NamedQuery(name = "GthTelefono.findByUsuarioIngre", query = "SELECT g FROM GthTelefono g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTelefono.findByFechaIngre", query = "SELECT g FROM GthTelefono g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTelefono.findByUsuarioActua", query = "SELECT g FROM GthTelefono g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTelefono.findByFechaActua", query = "SELECT g FROM GthTelefono g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTelefono.findByHoraIngre", query = "SELECT g FROM GthTelefono g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTelefono.findByHoraActua", query = "SELECT g FROM GthTelefono g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthTelefono.findByNotificacionGttel", query = "SELECT g FROM GthTelefono g WHERE g.notificacionGttel = :notificacionGttel")})
public class GthTelefono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttel")
    private Integer ideGttel;
    @Column(name = "ide_nrgar")
    private Integer ideNrgar;
    @Column(name = "numero_telefono_gttel")
    private String numeroTelefonoGttel;
    @Column(name = "activo_gttel")
    private Boolean activoGttel;
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
    @Column(name = "notificacion_gttel")
    private Boolean notificacionGttel;
    @JoinColumn(name = "ide_gttit", referencedColumnName = "ide_gttit")
    @ManyToOne
    private GthTipoTelefono ideGttit;
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
    @JoinColumn(name = "ide_gtcon", referencedColumnName = "ide_gtcon")
    @ManyToOne
    private GthConyuge ideGtcon;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public GthTelefono() {
    }

    public GthTelefono(Integer ideGttel) {
        this.ideGttel = ideGttel;
    }

    public Integer getIdeGttel() {
        return ideGttel;
    }

    public void setIdeGttel(Integer ideGttel) {
        this.ideGttel = ideGttel;
    }

    public Integer getIdeNrgar() {
        return ideNrgar;
    }

    public void setIdeNrgar(Integer ideNrgar) {
        this.ideNrgar = ideNrgar;
    }

    public String getNumeroTelefonoGttel() {
        return numeroTelefonoGttel;
    }

    public void setNumeroTelefonoGttel(String numeroTelefonoGttel) {
        this.numeroTelefonoGttel = numeroTelefonoGttel;
    }

    public Boolean getActivoGttel() {
        return activoGttel;
    }

    public void setActivoGttel(Boolean activoGttel) {
        this.activoGttel = activoGttel;
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

    public Boolean getNotificacionGttel() {
        return notificacionGttel;
    }

    public void setNotificacionGttel(Boolean notificacionGttel) {
        this.notificacionGttel = notificacionGttel;
    }

    public GthTipoTelefono getIdeGttit() {
        return ideGttit;
    }

    public void setIdeGttit(GthTipoTelefono ideGttit) {
        this.ideGttit = ideGttit;
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

    public GthConyuge getIdeGtcon() {
        return ideGtcon;
    }

    public void setIdeGtcon(GthConyuge ideGtcon) {
        this.ideGtcon = ideGtcon;
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
        hash += (ideGttel != null ? ideGttel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTelefono)) {
            return false;
        }
        GthTelefono other = (GthTelefono) object;
        if ((this.ideGttel == null && other.ideGttel != null) || (this.ideGttel != null && !this.ideGttel.equals(other.ideGttel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTelefono[ ideGttel=" + ideGttel + " ]";
    }
    
}
