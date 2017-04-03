/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "gth_ficha_valoracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthFichaValoracion.findAll", query = "SELECT g FROM GthFichaValoracion g"),
    @NamedQuery(name = "GthFichaValoracion.findByIdeGtfiv", query = "SELECT g FROM GthFichaValoracion g WHERE g.ideGtfiv = :ideGtfiv"),
    @NamedQuery(name = "GthFichaValoracion.findByFechaValoracionGtfiv", query = "SELECT g FROM GthFichaValoracion g WHERE g.fechaValoracionGtfiv = :fechaValoracionGtfiv"),
    @NamedQuery(name = "GthFichaValoracion.findByRmuGtfiv", query = "SELECT g FROM GthFichaValoracion g WHERE g.rmuGtfiv = :rmuGtfiv"),
    @NamedQuery(name = "GthFichaValoracion.findByTotalPuntosGtfiv", query = "SELECT g FROM GthFichaValoracion g WHERE g.totalPuntosGtfiv = :totalPuntosGtfiv"),
    @NamedQuery(name = "GthFichaValoracion.findByActivoGtfiv", query = "SELECT g FROM GthFichaValoracion g WHERE g.activoGtfiv = :activoGtfiv"),
    @NamedQuery(name = "GthFichaValoracion.findByUsuarioIngre", query = "SELECT g FROM GthFichaValoracion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthFichaValoracion.findByFechaIngre", query = "SELECT g FROM GthFichaValoracion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthFichaValoracion.findByHoraIngre", query = "SELECT g FROM GthFichaValoracion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthFichaValoracion.findByUsuarioActua", query = "SELECT g FROM GthFichaValoracion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthFichaValoracion.findByFechaActua", query = "SELECT g FROM GthFichaValoracion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthFichaValoracion.findByHoraActua", query = "SELECT g FROM GthFichaValoracion g WHERE g.horaActua = :horaActua")})
public class GthFichaValoracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtfiv")
    private Long ideGtfiv;
    @Column(name = "fecha_valoracion_gtfiv")
    @Temporal(TemporalType.DATE)
    private Date fechaValoracionGtfiv;
    @Column(name = "rmu_gtfiv")
    private BigInteger rmuGtfiv;
    @Column(name = "total_puntos_gtfiv")
    private BigInteger totalPuntosGtfiv;
    @Column(name = "activo_gtfiv")
    private Boolean activoGtfiv;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro")
    @ManyToOne
    private GenGrupoOcupacional ideGegro;
    @OneToMany(mappedBy = "ideGtfiv")
    private Collection<GthDetalleValoracion> gthDetalleValoracionCollection;

    public GthFichaValoracion() {
    }

    public GthFichaValoracion(Long ideGtfiv) {
        this.ideGtfiv = ideGtfiv;
    }

    public Long getIdeGtfiv() {
        return ideGtfiv;
    }

    public void setIdeGtfiv(Long ideGtfiv) {
        this.ideGtfiv = ideGtfiv;
    }

    public Date getFechaValoracionGtfiv() {
        return fechaValoracionGtfiv;
    }

    public void setFechaValoracionGtfiv(Date fechaValoracionGtfiv) {
        this.fechaValoracionGtfiv = fechaValoracionGtfiv;
    }

    public BigInteger getRmuGtfiv() {
        return rmuGtfiv;
    }

    public void setRmuGtfiv(BigInteger rmuGtfiv) {
        this.rmuGtfiv = rmuGtfiv;
    }

    public BigInteger getTotalPuntosGtfiv() {
        return totalPuntosGtfiv;
    }

    public void setTotalPuntosGtfiv(BigInteger totalPuntosGtfiv) {
        this.totalPuntosGtfiv = totalPuntosGtfiv;
    }

    public Boolean getActivoGtfiv() {
        return activoGtfiv;
    }

    public void setActivoGtfiv(Boolean activoGtfiv) {
        this.activoGtfiv = activoGtfiv;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    public GenGrupoOcupacional getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(GenGrupoOcupacional ideGegro) {
        this.ideGegro = ideGegro;
    }

    @XmlTransient
    public Collection<GthDetalleValoracion> getGthDetalleValoracionCollection() {
        return gthDetalleValoracionCollection;
    }

    public void setGthDetalleValoracionCollection(Collection<GthDetalleValoracion> gthDetalleValoracionCollection) {
        this.gthDetalleValoracionCollection = gthDetalleValoracionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtfiv != null ? ideGtfiv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthFichaValoracion)) {
            return false;
        }
        GthFichaValoracion other = (GthFichaValoracion) object;
        if ((this.ideGtfiv == null && other.ideGtfiv != null) || (this.ideGtfiv != null && !this.ideGtfiv.equals(other.ideGtfiv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthFichaValoracion[ ideGtfiv=" + ideGtfiv + " ]";
    }
    
}
