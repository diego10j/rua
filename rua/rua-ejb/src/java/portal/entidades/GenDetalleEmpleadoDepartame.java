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
@Table(name = "gen_detalle_empleado_departame")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findAll", query = "SELECT g FROM GenDetalleEmpleadoDepartame g"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByIdeGeded", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.ideGeded = :ideGeded"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByFechaIngresoGeded", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.fechaIngresoGeded = :fechaIngresoGeded"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByFechaSalidaGeded", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.fechaSalidaGeded = :fechaSalidaGeded"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findBySecuencialGeded", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.secuencialGeded = :secuencialGeded"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByObservacionGeded", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.observacionGeded = :observacionGeded"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByActivoGeded", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.activoGeded = :activoGeded"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByUsuarioIngre", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByFechaIngre", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByUsuarioActua", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByFechaActua", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByHoraIngre", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDetalleEmpleadoDepartame.findByHoraActua", query = "SELECT g FROM GenDetalleEmpleadoDepartame g WHERE g.horaActua = :horaActua")})
public class GenDetalleEmpleadoDepartame implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geded")
    private Integer ideGeded;
    @Column(name = "fecha_ingreso_geded")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoGeded;
    @Column(name = "fecha_salida_geded")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaGeded;
    @Column(name = "secuencial_geded")
    private Integer secuencialGeded;
    @Column(name = "observacion_geded")
    private String observacionGeded;
    @Basic(optional = false)
    @Column(name = "activo_geded")
    private boolean activoGeded;
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
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @OneToMany(mappedBy = "genIdeGeded")
    private Collection<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameCollection;
    @JoinColumn(name = "gen_ide_geded", referencedColumnName = "ide_geded")
    @ManyToOne
    private GenDetalleEmpleadoDepartame genIdeGeded;
    @JoinColumn(name = "ide_geame", referencedColumnName = "ide_geame")
    @ManyToOne
    private GenAccionMotivoEmpleado ideGeame;
    @OneToMany(mappedBy = "ideGeded")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;

    public GenDetalleEmpleadoDepartame() {
    }

    public GenDetalleEmpleadoDepartame(Integer ideGeded) {
        this.ideGeded = ideGeded;
    }

    public GenDetalleEmpleadoDepartame(Integer ideGeded, boolean activoGeded) {
        this.ideGeded = ideGeded;
        this.activoGeded = activoGeded;
    }

    public Integer getIdeGeded() {
        return ideGeded;
    }

    public void setIdeGeded(Integer ideGeded) {
        this.ideGeded = ideGeded;
    }

    public Date getFechaIngresoGeded() {
        return fechaIngresoGeded;
    }

    public void setFechaIngresoGeded(Date fechaIngresoGeded) {
        this.fechaIngresoGeded = fechaIngresoGeded;
    }

    public Date getFechaSalidaGeded() {
        return fechaSalidaGeded;
    }

    public void setFechaSalidaGeded(Date fechaSalidaGeded) {
        this.fechaSalidaGeded = fechaSalidaGeded;
    }

    public Integer getSecuencialGeded() {
        return secuencialGeded;
    }

    public void setSecuencialGeded(Integer secuencialGeded) {
        this.secuencialGeded = secuencialGeded;
    }

    public String getObservacionGeded() {
        return observacionGeded;
    }

    public void setObservacionGeded(String observacionGeded) {
        this.observacionGeded = observacionGeded;
    }

    public boolean getActivoGeded() {
        return activoGeded;
    }

    public void setActivoGeded(boolean activoGeded) {
        this.activoGeded = activoGeded;
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

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    @XmlTransient
    public Collection<GenDetalleEmpleadoDepartame> getGenDetalleEmpleadoDepartameCollection() {
        return genDetalleEmpleadoDepartameCollection;
    }

    public void setGenDetalleEmpleadoDepartameCollection(Collection<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameCollection) {
        this.genDetalleEmpleadoDepartameCollection = genDetalleEmpleadoDepartameCollection;
    }

    public GenDetalleEmpleadoDepartame getGenIdeGeded() {
        return genIdeGeded;
    }

    public void setGenIdeGeded(GenDetalleEmpleadoDepartame genIdeGeded) {
        this.genIdeGeded = genIdeGeded;
    }

    public GenAccionMotivoEmpleado getIdeGeame() {
        return ideGeame;
    }

    public void setIdeGeame(GenAccionMotivoEmpleado ideGeame) {
        this.ideGeame = ideGeame;
    }

    @XmlTransient
    public Collection<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParCollection() {
        return genEmpleadosDepartamentoParCollection;
    }

    public void setGenEmpleadosDepartamentoParCollection(Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection) {
        this.genEmpleadosDepartamentoParCollection = genEmpleadosDepartamentoParCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeded != null ? ideGeded.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDetalleEmpleadoDepartame)) {
            return false;
        }
        GenDetalleEmpleadoDepartame other = (GenDetalleEmpleadoDepartame) object;
        if ((this.ideGeded == null && other.ideGeded != null) || (this.ideGeded != null && !this.ideGeded.equals(other.ideGeded))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDetalleEmpleadoDepartame[ ideGeded=" + ideGeded + " ]";
    }
    
}
