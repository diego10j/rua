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
@Table(name = "gth_capacitacion_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthCapacitacionEmpleado.findAll", query = "SELECT g FROM GthCapacitacionEmpleado g"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByIdeGtcem", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.ideGtcem = :ideGtcem"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByDetalleGtcem", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.detalleGtcem = :detalleGtcem"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByTipoGtcem", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.tipoGtcem = :tipoGtcem"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByInstructorGtcem", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.instructorGtcem = :instructorGtcem"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByFechaGtcem", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.fechaGtcem = :fechaGtcem"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByDuracionGtcem", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.duracionGtcem = :duracionGtcem"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByActivoGtcem", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.activoGtcem = :activoGtcem"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByFechaIngre", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByUsuarioActua", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByFechaActua", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByHoraIngre", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCapacitacionEmpleado.findByHoraActua", query = "SELECT g FROM GthCapacitacionEmpleado g WHERE g.horaActua = :horaActua")})
public class GthCapacitacionEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtcem")
    private Integer ideGtcem;
    @Column(name = "detalle_gtcem")
    private String detalleGtcem;
    @Column(name = "tipo_gtcem")
    private String tipoGtcem;
    @Column(name = "instructor_gtcem")
    private String instructorGtcem;
    @Column(name = "fecha_gtcem")
    @Temporal(TemporalType.DATE)
    private Date fechaGtcem;
    @Column(name = "duracion_gtcem")
    private Integer duracionGtcem;
    @Column(name = "activo_gtcem")
    private Boolean activoGtcem;
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
    @JoinColumn(name = "ide_getpr", referencedColumnName = "ide_getpr")
    @ManyToOne
    private GenTipoPeriodo ideGetpr;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public GthCapacitacionEmpleado() {
    }

    public GthCapacitacionEmpleado(Integer ideGtcem) {
        this.ideGtcem = ideGtcem;
    }

    public Integer getIdeGtcem() {
        return ideGtcem;
    }

    public void setIdeGtcem(Integer ideGtcem) {
        this.ideGtcem = ideGtcem;
    }

    public String getDetalleGtcem() {
        return detalleGtcem;
    }

    public void setDetalleGtcem(String detalleGtcem) {
        this.detalleGtcem = detalleGtcem;
    }

    public String getTipoGtcem() {
        return tipoGtcem;
    }

    public void setTipoGtcem(String tipoGtcem) {
        this.tipoGtcem = tipoGtcem;
    }

    public String getInstructorGtcem() {
        return instructorGtcem;
    }

    public void setInstructorGtcem(String instructorGtcem) {
        this.instructorGtcem = instructorGtcem;
    }

    public Date getFechaGtcem() {
        return fechaGtcem;
    }

    public void setFechaGtcem(Date fechaGtcem) {
        this.fechaGtcem = fechaGtcem;
    }

    public Integer getDuracionGtcem() {
        return duracionGtcem;
    }

    public void setDuracionGtcem(Integer duracionGtcem) {
        this.duracionGtcem = duracionGtcem;
    }

    public Boolean getActivoGtcem() {
        return activoGtcem;
    }

    public void setActivoGtcem(Boolean activoGtcem) {
        this.activoGtcem = activoGtcem;
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

    public GenTipoPeriodo getIdeGetpr() {
        return ideGetpr;
    }

    public void setIdeGetpr(GenTipoPeriodo ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcem != null ? ideGtcem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCapacitacionEmpleado)) {
            return false;
        }
        GthCapacitacionEmpleado other = (GthCapacitacionEmpleado) object;
        if ((this.ideGtcem == null && other.ideGtcem != null) || (this.ideGtcem != null && !this.ideGtcem.equals(other.ideGtcem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCapacitacionEmpleado[ ideGtcem=" + ideGtcem + " ]";
    }
    
}
