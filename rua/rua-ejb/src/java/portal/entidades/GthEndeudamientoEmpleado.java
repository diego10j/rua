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
@Table(name = "gth_endeudamiento_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthEndeudamientoEmpleado.findAll", query = "SELECT g FROM GthEndeudamientoEmpleado g"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByIdeGteem", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.ideGteem = :ideGteem"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByPlazoGteem", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.plazoGteem = :plazoGteem"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByMontoGteem", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.montoGteem = :montoGteem"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByCuotaMensualGteem", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.cuotaMensualGteem = :cuotaMensualGteem"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByActivoGteem", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.activoGteem = :activoGteem"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByFechaIngre", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByFechaActua", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByHoraIngre", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthEndeudamientoEmpleado.findByHoraActua", query = "SELECT g FROM GthEndeudamientoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthEndeudamientoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gteem")
    private Integer ideGteem;
    @Column(name = "plazo_gteem")
    private Integer plazoGteem;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_gteem")
    private BigDecimal montoGteem;
    @Column(name = "cuota_mensual_gteem")
    private BigDecimal cuotaMensualGteem;
    @Column(name = "activo_gteem")
    private Boolean activoGteem;
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
    @JoinColumn(name = "ide_gtten", referencedColumnName = "ide_gtten")
    @ManyToOne
    private GthTipoEndeudamiento ideGtten;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_getpr", referencedColumnName = "ide_getpr")
    @ManyToOne
    private GenTipoPeriodo ideGetpr;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;

    public GthEndeudamientoEmpleado() {
    }

    public GthEndeudamientoEmpleado(Integer ideGteem) {
        this.ideGteem = ideGteem;
    }

    public Integer getIdeGteem() {
        return ideGteem;
    }

    public void setIdeGteem(Integer ideGteem) {
        this.ideGteem = ideGteem;
    }

    public Integer getPlazoGteem() {
        return plazoGteem;
    }

    public void setPlazoGteem(Integer plazoGteem) {
        this.plazoGteem = plazoGteem;
    }

    public BigDecimal getMontoGteem() {
        return montoGteem;
    }

    public void setMontoGteem(BigDecimal montoGteem) {
        this.montoGteem = montoGteem;
    }

    public BigDecimal getCuotaMensualGteem() {
        return cuotaMensualGteem;
    }

    public void setCuotaMensualGteem(BigDecimal cuotaMensualGteem) {
        this.cuotaMensualGteem = cuotaMensualGteem;
    }

    public Boolean getActivoGteem() {
        return activoGteem;
    }

    public void setActivoGteem(Boolean activoGteem) {
        this.activoGteem = activoGteem;
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

    public GthTipoEndeudamiento getIdeGtten() {
        return ideGtten;
    }

    public void setIdeGtten(GthTipoEndeudamiento ideGtten) {
        this.ideGtten = ideGtten;
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
        hash += (ideGteem != null ? ideGteem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthEndeudamientoEmpleado)) {
            return false;
        }
        GthEndeudamientoEmpleado other = (GthEndeudamientoEmpleado) object;
        if ((this.ideGteem == null && other.ideGteem != null) || (this.ideGteem != null && !this.ideGteem.equals(other.ideGteem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthEndeudamientoEmpleado[ ideGteem=" + ideGteem + " ]";
    }
    
}
