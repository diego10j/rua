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
@Table(name = "gth_inversion_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthInversionEmpleado.findAll", query = "SELECT g FROM GthInversionEmpleado g"),
    @NamedQuery(name = "GthInversionEmpleado.findByIdeGtine", query = "SELECT g FROM GthInversionEmpleado g WHERE g.ideGtine = :ideGtine"),
    @NamedQuery(name = "GthInversionEmpleado.findByPlazoGtine", query = "SELECT g FROM GthInversionEmpleado g WHERE g.plazoGtine = :plazoGtine"),
    @NamedQuery(name = "GthInversionEmpleado.findByMontoGtine", query = "SELECT g FROM GthInversionEmpleado g WHERE g.montoGtine = :montoGtine"),
    @NamedQuery(name = "GthInversionEmpleado.findByTazaGtine", query = "SELECT g FROM GthInversionEmpleado g WHERE g.tazaGtine = :tazaGtine"),
    @NamedQuery(name = "GthInversionEmpleado.findByActivoGtine", query = "SELECT g FROM GthInversionEmpleado g WHERE g.activoGtine = :activoGtine"),
    @NamedQuery(name = "GthInversionEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthInversionEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthInversionEmpleado.findByFechaIngre", query = "SELECT g FROM GthInversionEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthInversionEmpleado.findByUsuarioActua", query = "SELECT g FROM GthInversionEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthInversionEmpleado.findByFechaActua", query = "SELECT g FROM GthInversionEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthInversionEmpleado.findByHoraIngre", query = "SELECT g FROM GthInversionEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthInversionEmpleado.findByHoraActua", query = "SELECT g FROM GthInversionEmpleado g WHERE g.horaActua = :horaActua")})
public class GthInversionEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtine")
    private Integer ideGtine;
    @Column(name = "plazo_gtine")
    private Integer plazoGtine;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_gtine")
    private BigDecimal montoGtine;
    @Column(name = "taza_gtine")
    private BigDecimal tazaGtine;
    @Column(name = "activo_gtine")
    private Boolean activoGtine;
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
    @JoinColumn(name = "ide_gttii", referencedColumnName = "ide_gttii")
    @ManyToOne
    private GthTipoInversion ideGttii;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_getpr", referencedColumnName = "ide_getpr")
    @ManyToOne
    private GenTipoPeriodo ideGetpr;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;

    public GthInversionEmpleado() {
    }

    public GthInversionEmpleado(Integer ideGtine) {
        this.ideGtine = ideGtine;
    }

    public Integer getIdeGtine() {
        return ideGtine;
    }

    public void setIdeGtine(Integer ideGtine) {
        this.ideGtine = ideGtine;
    }

    public Integer getPlazoGtine() {
        return plazoGtine;
    }

    public void setPlazoGtine(Integer plazoGtine) {
        this.plazoGtine = plazoGtine;
    }

    public BigDecimal getMontoGtine() {
        return montoGtine;
    }

    public void setMontoGtine(BigDecimal montoGtine) {
        this.montoGtine = montoGtine;
    }

    public BigDecimal getTazaGtine() {
        return tazaGtine;
    }

    public void setTazaGtine(BigDecimal tazaGtine) {
        this.tazaGtine = tazaGtine;
    }

    public Boolean getActivoGtine() {
        return activoGtine;
    }

    public void setActivoGtine(Boolean activoGtine) {
        this.activoGtine = activoGtine;
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

    public GthTipoInversion getIdeGttii() {
        return ideGttii;
    }

    public void setIdeGttii(GthTipoInversion ideGttii) {
        this.ideGttii = ideGttii;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtine != null ? ideGtine.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthInversionEmpleado)) {
            return false;
        }
        GthInversionEmpleado other = (GthInversionEmpleado) object;
        if ((this.ideGtine == null && other.ideGtine != null) || (this.ideGtine != null && !this.ideGtine.equals(other.ideGtine))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthInversionEmpleado[ ideGtine=" + ideGtine + " ]";
    }
    
}
