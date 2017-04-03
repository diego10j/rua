/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "gth_viaticos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthViaticos.findAll", query = "SELECT g FROM GthViaticos g"),
    @NamedQuery(name = "GthViaticos.findByIdeGtvia", query = "SELECT g FROM GthViaticos g WHERE g.ideGtvia = :ideGtvia"),
    @NamedQuery(name = "GthViaticos.findByInteriorExteriorGtvia", query = "SELECT g FROM GthViaticos g WHERE g.interiorExteriorGtvia = :interiorExteriorGtvia"),
    @NamedQuery(name = "GthViaticos.findByNroSolicitudGtvia", query = "SELECT g FROM GthViaticos g WHERE g.nroSolicitudGtvia = :nroSolicitudGtvia"),
    @NamedQuery(name = "GthViaticos.findByFechaSolicitudGtvia", query = "SELECT g FROM GthViaticos g WHERE g.fechaSolicitudGtvia = :fechaSolicitudGtvia"),
    @NamedQuery(name = "GthViaticos.findByViaticoGtvia", query = "SELECT g FROM GthViaticos g WHERE g.viaticoGtvia = :viaticoGtvia"),
    @NamedQuery(name = "GthViaticos.findByMovilizacionGtvia", query = "SELECT g FROM GthViaticos g WHERE g.movilizacionGtvia = :movilizacionGtvia"),
    @NamedQuery(name = "GthViaticos.findBySubsistenciaGtvia", query = "SELECT g FROM GthViaticos g WHERE g.subsistenciaGtvia = :subsistenciaGtvia"),
    @NamedQuery(name = "GthViaticos.findByAlimentacionGtvia", query = "SELECT g FROM GthViaticos g WHERE g.alimentacionGtvia = :alimentacionGtvia"),
    @NamedQuery(name = "GthViaticos.findByFechaSalidaGtvia", query = "SELECT g FROM GthViaticos g WHERE g.fechaSalidaGtvia = :fechaSalidaGtvia"),
    @NamedQuery(name = "GthViaticos.findByFechaLlegadaGtvia", query = "SELECT g FROM GthViaticos g WHERE g.fechaLlegadaGtvia = :fechaLlegadaGtvia"),
    @NamedQuery(name = "GthViaticos.findByDetalleActividadGtvia", query = "SELECT g FROM GthViaticos g WHERE g.detalleActividadGtvia = :detalleActividadGtvia"),
    @NamedQuery(name = "GthViaticos.findByNroCuentaGtvia", query = "SELECT g FROM GthViaticos g WHERE g.nroCuentaGtvia = :nroCuentaGtvia"),
    @NamedQuery(name = "GthViaticos.findByValorTotalGtvia", query = "SELECT g FROM GthViaticos g WHERE g.valorTotalGtvia = :valorTotalGtvia"),
    @NamedQuery(name = "GthViaticos.findByFechaLiquidacionGtvia", query = "SELECT g FROM GthViaticos g WHERE g.fechaLiquidacionGtvia = :fechaLiquidacionGtvia"),
    @NamedQuery(name = "GthViaticos.findByPartidaPresupuestariaGtvia", query = "SELECT g FROM GthViaticos g WHERE g.partidaPresupuestariaGtvia = :partidaPresupuestariaGtvia"),
    @NamedQuery(name = "GthViaticos.findByValorReliquidacionGtvia", query = "SELECT g FROM GthViaticos g WHERE g.valorReliquidacionGtvia = :valorReliquidacionGtvia"),
    @NamedQuery(name = "GthViaticos.findByActivoGtvia", query = "SELECT g FROM GthViaticos g WHERE g.activoGtvia = :activoGtvia"),
    @NamedQuery(name = "GthViaticos.findByUsuarioIngre", query = "SELECT g FROM GthViaticos g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthViaticos.findByFechaIngre", query = "SELECT g FROM GthViaticos g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthViaticos.findByUsuarioActua", query = "SELECT g FROM GthViaticos g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthViaticos.findByFechaActua", query = "SELECT g FROM GthViaticos g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthViaticos.findByHoraIngre", query = "SELECT g FROM GthViaticos g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthViaticos.findByHoraActua", query = "SELECT g FROM GthViaticos g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthViaticos.findByHoraLlegadaGtvia", query = "SELECT g FROM GthViaticos g WHERE g.horaLlegadaGtvia = :horaLlegadaGtvia"),
    @NamedQuery(name = "GthViaticos.findByHoraSalidaGtvia", query = "SELECT g FROM GthViaticos g WHERE g.horaSalidaGtvia = :horaSalidaGtvia")})
public class GthViaticos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtvia")
    private Integer ideGtvia;
    @Column(name = "interior_exterior_gtvia")
    private Integer interiorExteriorGtvia;
    @Column(name = "nro_solicitud_gtvia")
    private String nroSolicitudGtvia;
    @Column(name = "fecha_solicitud_gtvia")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitudGtvia;
    @Column(name = "viatico_gtvia")
    private Integer viaticoGtvia;
    @Column(name = "movilizacion_gtvia")
    private Integer movilizacionGtvia;
    @Column(name = "subsistencia_gtvia")
    private Integer subsistenciaGtvia;
    @Column(name = "alimentacion_gtvia")
    private Integer alimentacionGtvia;
    @Column(name = "fecha_salida_gtvia")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaGtvia;
    @Column(name = "fecha_llegada_gtvia")
    @Temporal(TemporalType.DATE)
    private Date fechaLlegadaGtvia;
    @Column(name = "detalle_actividad_gtvia")
    private String detalleActividadGtvia;
    @Column(name = "nro_cuenta_gtvia")
    private String nroCuentaGtvia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_total_gtvia")
    private BigDecimal valorTotalGtvia;
    @Column(name = "fecha_liquidacion_gtvia")
    @Temporal(TemporalType.DATE)
    private Date fechaLiquidacionGtvia;
    @Column(name = "partida_presupuestaria_gtvia")
    private String partidaPresupuestariaGtvia;
    @Column(name = "valor_reliquidacion_gtvia")
    private BigDecimal valorReliquidacionGtvia;
    @Column(name = "activo_gtvia")
    private Boolean activoGtvia;
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
    @Column(name = "hora_llegada_gtvia")
    @Temporal(TemporalType.TIME)
    private Date horaLlegadaGtvia;
    @Column(name = "hora_salida_gtvia")
    @Temporal(TemporalType.TIME)
    private Date horaSalidaGtvia;
    @OneToMany(mappedBy = "ideGtvia")
    private Collection<GthServidoresComision> gthServidoresComisionCollection;
    @JoinColumn(name = "ide_gttcb", referencedColumnName = "ide_gttcb")
    @ManyToOne
    private GthTipoCuentaBancaria ideGttcb;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp3", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp3;
    @JoinColumn(name = "gen_ide_geedp2", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp2;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public GthViaticos() {
    }

    public GthViaticos(Integer ideGtvia) {
        this.ideGtvia = ideGtvia;
    }

    public Integer getIdeGtvia() {
        return ideGtvia;
    }

    public void setIdeGtvia(Integer ideGtvia) {
        this.ideGtvia = ideGtvia;
    }

    public Integer getInteriorExteriorGtvia() {
        return interiorExteriorGtvia;
    }

    public void setInteriorExteriorGtvia(Integer interiorExteriorGtvia) {
        this.interiorExteriorGtvia = interiorExteriorGtvia;
    }

    public String getNroSolicitudGtvia() {
        return nroSolicitudGtvia;
    }

    public void setNroSolicitudGtvia(String nroSolicitudGtvia) {
        this.nroSolicitudGtvia = nroSolicitudGtvia;
    }

    public Date getFechaSolicitudGtvia() {
        return fechaSolicitudGtvia;
    }

    public void setFechaSolicitudGtvia(Date fechaSolicitudGtvia) {
        this.fechaSolicitudGtvia = fechaSolicitudGtvia;
    }

    public Integer getViaticoGtvia() {
        return viaticoGtvia;
    }

    public void setViaticoGtvia(Integer viaticoGtvia) {
        this.viaticoGtvia = viaticoGtvia;
    }

    public Integer getMovilizacionGtvia() {
        return movilizacionGtvia;
    }

    public void setMovilizacionGtvia(Integer movilizacionGtvia) {
        this.movilizacionGtvia = movilizacionGtvia;
    }

    public Integer getSubsistenciaGtvia() {
        return subsistenciaGtvia;
    }

    public void setSubsistenciaGtvia(Integer subsistenciaGtvia) {
        this.subsistenciaGtvia = subsistenciaGtvia;
    }

    public Integer getAlimentacionGtvia() {
        return alimentacionGtvia;
    }

    public void setAlimentacionGtvia(Integer alimentacionGtvia) {
        this.alimentacionGtvia = alimentacionGtvia;
    }

    public Date getFechaSalidaGtvia() {
        return fechaSalidaGtvia;
    }

    public void setFechaSalidaGtvia(Date fechaSalidaGtvia) {
        this.fechaSalidaGtvia = fechaSalidaGtvia;
    }

    public Date getFechaLlegadaGtvia() {
        return fechaLlegadaGtvia;
    }

    public void setFechaLlegadaGtvia(Date fechaLlegadaGtvia) {
        this.fechaLlegadaGtvia = fechaLlegadaGtvia;
    }

    public String getDetalleActividadGtvia() {
        return detalleActividadGtvia;
    }

    public void setDetalleActividadGtvia(String detalleActividadGtvia) {
        this.detalleActividadGtvia = detalleActividadGtvia;
    }

    public String getNroCuentaGtvia() {
        return nroCuentaGtvia;
    }

    public void setNroCuentaGtvia(String nroCuentaGtvia) {
        this.nroCuentaGtvia = nroCuentaGtvia;
    }

    public BigDecimal getValorTotalGtvia() {
        return valorTotalGtvia;
    }

    public void setValorTotalGtvia(BigDecimal valorTotalGtvia) {
        this.valorTotalGtvia = valorTotalGtvia;
    }

    public Date getFechaLiquidacionGtvia() {
        return fechaLiquidacionGtvia;
    }

    public void setFechaLiquidacionGtvia(Date fechaLiquidacionGtvia) {
        this.fechaLiquidacionGtvia = fechaLiquidacionGtvia;
    }

    public String getPartidaPresupuestariaGtvia() {
        return partidaPresupuestariaGtvia;
    }

    public void setPartidaPresupuestariaGtvia(String partidaPresupuestariaGtvia) {
        this.partidaPresupuestariaGtvia = partidaPresupuestariaGtvia;
    }

    public BigDecimal getValorReliquidacionGtvia() {
        return valorReliquidacionGtvia;
    }

    public void setValorReliquidacionGtvia(BigDecimal valorReliquidacionGtvia) {
        this.valorReliquidacionGtvia = valorReliquidacionGtvia;
    }

    public Boolean getActivoGtvia() {
        return activoGtvia;
    }

    public void setActivoGtvia(Boolean activoGtvia) {
        this.activoGtvia = activoGtvia;
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

    public Date getHoraLlegadaGtvia() {
        return horaLlegadaGtvia;
    }

    public void setHoraLlegadaGtvia(Date horaLlegadaGtvia) {
        this.horaLlegadaGtvia = horaLlegadaGtvia;
    }

    public Date getHoraSalidaGtvia() {
        return horaSalidaGtvia;
    }

    public void setHoraSalidaGtvia(Date horaSalidaGtvia) {
        this.horaSalidaGtvia = horaSalidaGtvia;
    }

    @XmlTransient
    public Collection<GthServidoresComision> getGthServidoresComisionCollection() {
        return gthServidoresComisionCollection;
    }

    public void setGthServidoresComisionCollection(Collection<GthServidoresComision> gthServidoresComisionCollection) {
        this.gthServidoresComisionCollection = gthServidoresComisionCollection;
    }

    public GthTipoCuentaBancaria getIdeGttcb() {
        return ideGttcb;
    }

    public void setIdeGttcb(GthTipoCuentaBancaria ideGttcb) {
        this.ideGttcb = ideGttcb;
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

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp3() {
        return genIdeGeedp3;
    }

    public void setGenIdeGeedp3(GenEmpleadosDepartamentoPar genIdeGeedp3) {
        this.genIdeGeedp3 = genIdeGeedp3;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp2() {
        return genIdeGeedp2;
    }

    public void setGenIdeGeedp2(GenEmpleadosDepartamentoPar genIdeGeedp2) {
        this.genIdeGeedp2 = genIdeGeedp2;
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
        hash += (ideGtvia != null ? ideGtvia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthViaticos)) {
            return false;
        }
        GthViaticos other = (GthViaticos) object;
        if ((this.ideGtvia == null && other.ideGtvia != null) || (this.ideGtvia != null && !this.ideGtvia.equals(other.ideGtvia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthViaticos[ ideGtvia=" + ideGtvia + " ]";
    }
    
}
