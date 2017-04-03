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
@Table(name = "gth_idioma_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthIdiomaEmpleado.findAll", query = "SELECT g FROM GthIdiomaEmpleado g"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByIdeGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.ideGtide = :ideGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByPocentajeLeeGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.pocentajeLeeGtide = :pocentajeLeeGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByPorcentajeEscribeGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.porcentajeEscribeGtide = :porcentajeEscribeGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByPorcentajeEntiendeGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.porcentajeEntiendeGtide = :porcentajeEntiendeGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByAnioGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.anioGtide = :anioGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByDuracionGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.duracionGtide = :duracionGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByNivelAprobadoGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.nivelAprobadoGtide = :nivelAprobadoGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByActivoGtide", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.activoGtide = :activoGtide"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByFechaIngre", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByUsuarioActua", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByFechaActua", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByHoraIngre", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthIdiomaEmpleado.findByHoraActua", query = "SELECT g FROM GthIdiomaEmpleado g WHERE g.horaActua = :horaActua")})
public class GthIdiomaEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtide")
    private Integer ideGtide;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pocentaje_lee_gtide")
    private BigDecimal pocentajeLeeGtide;
    @Column(name = "porcentaje_escribe_gtide")
    private BigDecimal porcentajeEscribeGtide;
    @Column(name = "porcentaje_entiende_gtide")
    private BigDecimal porcentajeEntiendeGtide;
    @Column(name = "anio_gtide")
    private Integer anioGtide;
    @Column(name = "duracion_gtide")
    private Integer duracionGtide;
    @Column(name = "nivel_aprobado_gtide")
    private Integer nivelAprobadoGtide;
    @Column(name = "activo_gtide")
    private Boolean activoGtide;
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
    @JoinColumn(name = "ide_geidi", referencedColumnName = "ide_geidi")
    @ManyToOne
    private GenIdioma ideGeidi;

    public GthIdiomaEmpleado() {
    }

    public GthIdiomaEmpleado(Integer ideGtide) {
        this.ideGtide = ideGtide;
    }

    public Integer getIdeGtide() {
        return ideGtide;
    }

    public void setIdeGtide(Integer ideGtide) {
        this.ideGtide = ideGtide;
    }

    public BigDecimal getPocentajeLeeGtide() {
        return pocentajeLeeGtide;
    }

    public void setPocentajeLeeGtide(BigDecimal pocentajeLeeGtide) {
        this.pocentajeLeeGtide = pocentajeLeeGtide;
    }

    public BigDecimal getPorcentajeEscribeGtide() {
        return porcentajeEscribeGtide;
    }

    public void setPorcentajeEscribeGtide(BigDecimal porcentajeEscribeGtide) {
        this.porcentajeEscribeGtide = porcentajeEscribeGtide;
    }

    public BigDecimal getPorcentajeEntiendeGtide() {
        return porcentajeEntiendeGtide;
    }

    public void setPorcentajeEntiendeGtide(BigDecimal porcentajeEntiendeGtide) {
        this.porcentajeEntiendeGtide = porcentajeEntiendeGtide;
    }

    public Integer getAnioGtide() {
        return anioGtide;
    }

    public void setAnioGtide(Integer anioGtide) {
        this.anioGtide = anioGtide;
    }

    public Integer getDuracionGtide() {
        return duracionGtide;
    }

    public void setDuracionGtide(Integer duracionGtide) {
        this.duracionGtide = duracionGtide;
    }

    public Integer getNivelAprobadoGtide() {
        return nivelAprobadoGtide;
    }

    public void setNivelAprobadoGtide(Integer nivelAprobadoGtide) {
        this.nivelAprobadoGtide = nivelAprobadoGtide;
    }

    public Boolean getActivoGtide() {
        return activoGtide;
    }

    public void setActivoGtide(Boolean activoGtide) {
        this.activoGtide = activoGtide;
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

    public GenIdioma getIdeGeidi() {
        return ideGeidi;
    }

    public void setIdeGeidi(GenIdioma ideGeidi) {
        this.ideGeidi = ideGeidi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtide != null ? ideGtide.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthIdiomaEmpleado)) {
            return false;
        }
        GthIdiomaEmpleado other = (GthIdiomaEmpleado) object;
        if ((this.ideGtide == null && other.ideGtide != null) || (this.ideGtide != null && !this.ideGtide.equals(other.ideGtide))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthIdiomaEmpleado[ ideGtide=" + ideGtide + " ]";
    }
    
}
