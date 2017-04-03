/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "asi_detalle_vacacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsiDetalleVacacion.findAll", query = "SELECT a FROM AsiDetalleVacacion a"),
    @NamedQuery(name = "AsiDetalleVacacion.findByIdeAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.ideAsdev = :ideAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByFechaNovedadAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.fechaNovedadAsdev = :fechaNovedadAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByDiaSolicitadoAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.diaSolicitadoAsdev = :diaSolicitadoAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByDiaAcumuladoAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.diaAcumuladoAsdev = :diaAcumuladoAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByDiaAdicionalAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.diaAdicionalAsdev = :diaAdicionalAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByDiaDescontadoAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.diaDescontadoAsdev = :diaDescontadoAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByObservacionAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.observacionAsdev = :observacionAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByAnuladoAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.anuladoAsdev = :anuladoAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByActivoAsdev", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.activoAsdev = :activoAsdev"),
    @NamedQuery(name = "AsiDetalleVacacion.findByUsuarioIngre", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiDetalleVacacion.findByFechaIngre", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiDetalleVacacion.findByHoraIngre", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiDetalleVacacion.findByUsuarioActua", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiDetalleVacacion.findByFechaActua", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiDetalleVacacion.findByHoraActua", query = "SELECT a FROM AsiDetalleVacacion a WHERE a.horaActua = :horaActua")})
public class AsiDetalleVacacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_asdev")
    private Long ideAsdev;
    @Column(name = "fecha_novedad_asdev")
    @Temporal(TemporalType.DATE)
    private Date fechaNovedadAsdev;
    @Column(name = "dia_solicitado_asdev")
    private BigInteger diaSolicitadoAsdev;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "dia_acumulado_asdev")
    private BigDecimal diaAcumuladoAsdev;
    @Column(name = "dia_adicional_asdev")
    private BigDecimal diaAdicionalAsdev;
    @Column(name = "dia_descontado_asdev")
    private BigDecimal diaDescontadoAsdev;
    @Column(name = "observacion_asdev")
    private String observacionAsdev;
    @Column(name = "anulado_asdev")
    private Boolean anuladoAsdev;
    @Column(name = "activo_asdev")
    private Boolean activoAsdev;
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
    @JoinColumn(name = "ide_asvac", referencedColumnName = "ide_asvac")
    @ManyToOne
    private AsiVacacion ideAsvac;
    @JoinColumn(name = "ide_aspvh", referencedColumnName = "ide_aspvh")
    @ManyToOne
    private AsiPermisosVacacionHext ideAspvh;
    @JoinColumn(name = "ide_asnod", referencedColumnName = "ide_asnod")
    @ManyToOne
    private AsiNovedadDetalle ideAsnod;
    @JoinColumn(name = "ide_asesv", referencedColumnName = "ide_asesv")
    @ManyToOne
    private AsiEstadoVacacion ideAsesv;

    public AsiDetalleVacacion() {
    }

    public AsiDetalleVacacion(Long ideAsdev) {
        this.ideAsdev = ideAsdev;
    }

    public Long getIdeAsdev() {
        return ideAsdev;
    }

    public void setIdeAsdev(Long ideAsdev) {
        this.ideAsdev = ideAsdev;
    }

    public Date getFechaNovedadAsdev() {
        return fechaNovedadAsdev;
    }

    public void setFechaNovedadAsdev(Date fechaNovedadAsdev) {
        this.fechaNovedadAsdev = fechaNovedadAsdev;
    }

    public BigInteger getDiaSolicitadoAsdev() {
        return diaSolicitadoAsdev;
    }

    public void setDiaSolicitadoAsdev(BigInteger diaSolicitadoAsdev) {
        this.diaSolicitadoAsdev = diaSolicitadoAsdev;
    }

    public BigDecimal getDiaAcumuladoAsdev() {
        return diaAcumuladoAsdev;
    }

    public void setDiaAcumuladoAsdev(BigDecimal diaAcumuladoAsdev) {
        this.diaAcumuladoAsdev = diaAcumuladoAsdev;
    }

    public BigDecimal getDiaAdicionalAsdev() {
        return diaAdicionalAsdev;
    }

    public void setDiaAdicionalAsdev(BigDecimal diaAdicionalAsdev) {
        this.diaAdicionalAsdev = diaAdicionalAsdev;
    }

    public BigDecimal getDiaDescontadoAsdev() {
        return diaDescontadoAsdev;
    }

    public void setDiaDescontadoAsdev(BigDecimal diaDescontadoAsdev) {
        this.diaDescontadoAsdev = diaDescontadoAsdev;
    }

    public String getObservacionAsdev() {
        return observacionAsdev;
    }

    public void setObservacionAsdev(String observacionAsdev) {
        this.observacionAsdev = observacionAsdev;
    }

    public Boolean getAnuladoAsdev() {
        return anuladoAsdev;
    }

    public void setAnuladoAsdev(Boolean anuladoAsdev) {
        this.anuladoAsdev = anuladoAsdev;
    }

    public Boolean getActivoAsdev() {
        return activoAsdev;
    }

    public void setActivoAsdev(Boolean activoAsdev) {
        this.activoAsdev = activoAsdev;
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

    public AsiVacacion getIdeAsvac() {
        return ideAsvac;
    }

    public void setIdeAsvac(AsiVacacion ideAsvac) {
        this.ideAsvac = ideAsvac;
    }

    public AsiPermisosVacacionHext getIdeAspvh() {
        return ideAspvh;
    }

    public void setIdeAspvh(AsiPermisosVacacionHext ideAspvh) {
        this.ideAspvh = ideAspvh;
    }

    public AsiNovedadDetalle getIdeAsnod() {
        return ideAsnod;
    }

    public void setIdeAsnod(AsiNovedadDetalle ideAsnod) {
        this.ideAsnod = ideAsnod;
    }

    public AsiEstadoVacacion getIdeAsesv() {
        return ideAsesv;
    }

    public void setIdeAsesv(AsiEstadoVacacion ideAsesv) {
        this.ideAsesv = ideAsesv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsdev != null ? ideAsdev.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiDetalleVacacion)) {
            return false;
        }
        AsiDetalleVacacion other = (AsiDetalleVacacion) object;
        if ((this.ideAsdev == null && other.ideAsdev != null) || (this.ideAsdev != null && !this.ideAsdev.equals(other.ideAsdev))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiDetalleVacacion[ ideAsdev=" + ideAsdev + " ]";
    }
    
}
