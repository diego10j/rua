/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "gth_registro_militar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthRegistroMilitar.findAll", query = "SELECT g FROM GthRegistroMilitar g"),
    @NamedQuery(name = "GthRegistroMilitar.findByIdeGtrem", query = "SELECT g FROM GthRegistroMilitar g WHERE g.gthRegistroMilitarPK.ideGtrem = :ideGtrem"),
    @NamedQuery(name = "GthRegistroMilitar.findByIdeGtemp", query = "SELECT g FROM GthRegistroMilitar g WHERE g.gthRegistroMilitarPK.ideGtemp = :ideGtemp"),
    @NamedQuery(name = "GthRegistroMilitar.findByLibretaMilitarGtrem", query = "SELECT g FROM GthRegistroMilitar g WHERE g.libretaMilitarGtrem = :libretaMilitarGtrem"),
    @NamedQuery(name = "GthRegistroMilitar.findByAnioTotalGtrem", query = "SELECT g FROM GthRegistroMilitar g WHERE g.anioTotalGtrem = :anioTotalGtrem"),
    @NamedQuery(name = "GthRegistroMilitar.findByAnioIngresoGtrem", query = "SELECT g FROM GthRegistroMilitar g WHERE g.anioIngresoGtrem = :anioIngresoGtrem"),
    @NamedQuery(name = "GthRegistroMilitar.findByUnidadConscripcionGtrem", query = "SELECT g FROM GthRegistroMilitar g WHERE g.unidadConscripcionGtrem = :unidadConscripcionGtrem"),
    @NamedQuery(name = "GthRegistroMilitar.findByActivoGtrem", query = "SELECT g FROM GthRegistroMilitar g WHERE g.activoGtrem = :activoGtrem"),
    @NamedQuery(name = "GthRegistroMilitar.findByUsuarioIngre", query = "SELECT g FROM GthRegistroMilitar g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthRegistroMilitar.findByFechaIngre", query = "SELECT g FROM GthRegistroMilitar g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthRegistroMilitar.findByUsuarioActua", query = "SELECT g FROM GthRegistroMilitar g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthRegistroMilitar.findByFechaActua", query = "SELECT g FROM GthRegistroMilitar g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthRegistroMilitar.findByHoraIngre", query = "SELECT g FROM GthRegistroMilitar g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthRegistroMilitar.findByHoraActua", query = "SELECT g FROM GthRegistroMilitar g WHERE g.horaActua = :horaActua")})
public class GthRegistroMilitar implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GthRegistroMilitarPK gthRegistroMilitarPK;
    @Column(name = "libreta_militar_gtrem")
    private String libretaMilitarGtrem;
    @Column(name = "anio_total_gtrem")
    private Integer anioTotalGtrem;
    @Column(name = "anio_ingreso_gtrem")
    private Integer anioIngresoGtrem;
    @Column(name = "unidad_conscripcion_gtrem")
    private String unidadConscripcionGtrem;
    @Column(name = "activo_gtrem")
    private Boolean activoGtrem;
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
    @JoinColumn(name = "ide_gttsm", referencedColumnName = "ide_gttsm")
    @ManyToOne
    private GthTipoSituacionMilitar ideGttsm;
    @JoinColumn(name = "ide_gttrf", referencedColumnName = "ide_gttrf")
    @ManyToOne
    private GthTipoRamasFfaa ideGttrf;
    @JoinColumn(name = "ide_gttgf", referencedColumnName = "ide_gttgf")
    @ManyToOne
    private GthTipoGradoFfaa ideGttgf;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GthEmpleado gthEmpleado;

    public GthRegistroMilitar() {
    }

    public GthRegistroMilitar(GthRegistroMilitarPK gthRegistroMilitarPK) {
        this.gthRegistroMilitarPK = gthRegistroMilitarPK;
    }

    public GthRegistroMilitar(int ideGtrem, int ideGtemp) {
        this.gthRegistroMilitarPK = new GthRegistroMilitarPK(ideGtrem, ideGtemp);
    }

    public GthRegistroMilitarPK getGthRegistroMilitarPK() {
        return gthRegistroMilitarPK;
    }

    public void setGthRegistroMilitarPK(GthRegistroMilitarPK gthRegistroMilitarPK) {
        this.gthRegistroMilitarPK = gthRegistroMilitarPK;
    }

    public String getLibretaMilitarGtrem() {
        return libretaMilitarGtrem;
    }

    public void setLibretaMilitarGtrem(String libretaMilitarGtrem) {
        this.libretaMilitarGtrem = libretaMilitarGtrem;
    }

    public Integer getAnioTotalGtrem() {
        return anioTotalGtrem;
    }

    public void setAnioTotalGtrem(Integer anioTotalGtrem) {
        this.anioTotalGtrem = anioTotalGtrem;
    }

    public Integer getAnioIngresoGtrem() {
        return anioIngresoGtrem;
    }

    public void setAnioIngresoGtrem(Integer anioIngresoGtrem) {
        this.anioIngresoGtrem = anioIngresoGtrem;
    }

    public String getUnidadConscripcionGtrem() {
        return unidadConscripcionGtrem;
    }

    public void setUnidadConscripcionGtrem(String unidadConscripcionGtrem) {
        this.unidadConscripcionGtrem = unidadConscripcionGtrem;
    }

    public Boolean getActivoGtrem() {
        return activoGtrem;
    }

    public void setActivoGtrem(Boolean activoGtrem) {
        this.activoGtrem = activoGtrem;
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

    public GthTipoSituacionMilitar getIdeGttsm() {
        return ideGttsm;
    }

    public void setIdeGttsm(GthTipoSituacionMilitar ideGttsm) {
        this.ideGttsm = ideGttsm;
    }

    public GthTipoRamasFfaa getIdeGttrf() {
        return ideGttrf;
    }

    public void setIdeGttrf(GthTipoRamasFfaa ideGttrf) {
        this.ideGttrf = ideGttrf;
    }

    public GthTipoGradoFfaa getIdeGttgf() {
        return ideGttgf;
    }

    public void setIdeGttgf(GthTipoGradoFfaa ideGttgf) {
        this.ideGttgf = ideGttgf;
    }

    public GthEmpleado getGthEmpleado() {
        return gthEmpleado;
    }

    public void setGthEmpleado(GthEmpleado gthEmpleado) {
        this.gthEmpleado = gthEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gthRegistroMilitarPK != null ? gthRegistroMilitarPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthRegistroMilitar)) {
            return false;
        }
        GthRegistroMilitar other = (GthRegistroMilitar) object;
        if ((this.gthRegistroMilitarPK == null && other.gthRegistroMilitarPK != null) || (this.gthRegistroMilitarPK != null && !this.gthRegistroMilitarPK.equals(other.gthRegistroMilitarPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthRegistroMilitar[ gthRegistroMilitarPK=" + gthRegistroMilitarPK + " ]";
    }
    
}
