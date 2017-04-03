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
@Table(name = "nrh_capacidad_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhCapacidadPago.findAll", query = "SELECT n FROM NrhCapacidadPago n"),
    @NamedQuery(name = "NrhCapacidadPago.findByIdeNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.ideNrcap = :ideNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByFechaCalculoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.fechaCalculoNrcap = :fechaCalculoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByTotalIngresoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.totalIngresoNrcap = :totalIngresoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByTotalEgresoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.totalEgresoNrcap = :totalEgresoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByNroMesNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.nroMesNrcap = :nroMesNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByMontoRecibirNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.montoRecibirNrcap = :montoRecibirNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByCuotaMensualNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.cuotaMensualNrcap = :cuotaMensualNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByPorcentajeEndeudaNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.porcentajeEndeudaNrcap = :porcentajeEndeudaNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByCuotaLimiteNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.cuotaLimiteNrcap = :cuotaLimiteNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByActivoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.activoNrcap = :activoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByUsuarioIngre", query = "SELECT n FROM NrhCapacidadPago n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhCapacidadPago.findByFechaIngre", query = "SELECT n FROM NrhCapacidadPago n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhCapacidadPago.findByUsuarioActua", query = "SELECT n FROM NrhCapacidadPago n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhCapacidadPago.findByFechaActua", query = "SELECT n FROM NrhCapacidadPago n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhCapacidadPago.findByHoraIngre", query = "SELECT n FROM NrhCapacidadPago n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhCapacidadPago.findByHoraActua", query = "SELECT n FROM NrhCapacidadPago n WHERE n.horaActua = :horaActua")})
public class NrhCapacidadPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrcap")
    private Integer ideNrcap;
    @Basic(optional = false)
    @Column(name = "fecha_calculo_nrcap")
    @Temporal(TemporalType.DATE)
    private Date fechaCalculoNrcap;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "total_ingreso_nrcap")
    private BigDecimal totalIngresoNrcap;
    @Basic(optional = false)
    @Column(name = "total_egreso_nrcap")
    private BigDecimal totalEgresoNrcap;
    @Column(name = "nro_mes_nrcap")
    private Integer nroMesNrcap;
    @Column(name = "monto_recibir_nrcap")
    private BigDecimal montoRecibirNrcap;
    @Column(name = "cuota_mensual_nrcap")
    private BigDecimal cuotaMensualNrcap;
    @Column(name = "porcentaje_endeuda_nrcap")
    private BigDecimal porcentajeEndeudaNrcap;
    @Column(name = "cuota_limite_nrcap")
    private BigDecimal cuotaLimiteNrcap;
    @Basic(optional = false)
    @Column(name = "activo_nrcap")
    private boolean activoNrcap;
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
    @JoinColumn(name = "ide_nrant", referencedColumnName = "ide_nrant")
    @ManyToOne(optional = false)
    private NrhAnticipo ideNrant;

    public NrhCapacidadPago() {
    }

    public NrhCapacidadPago(Integer ideNrcap) {
        this.ideNrcap = ideNrcap;
    }

    public NrhCapacidadPago(Integer ideNrcap, Date fechaCalculoNrcap, BigDecimal totalIngresoNrcap, BigDecimal totalEgresoNrcap, boolean activoNrcap) {
        this.ideNrcap = ideNrcap;
        this.fechaCalculoNrcap = fechaCalculoNrcap;
        this.totalIngresoNrcap = totalIngresoNrcap;
        this.totalEgresoNrcap = totalEgresoNrcap;
        this.activoNrcap = activoNrcap;
    }

    public Integer getIdeNrcap() {
        return ideNrcap;
    }

    public void setIdeNrcap(Integer ideNrcap) {
        this.ideNrcap = ideNrcap;
    }

    public Date getFechaCalculoNrcap() {
        return fechaCalculoNrcap;
    }

    public void setFechaCalculoNrcap(Date fechaCalculoNrcap) {
        this.fechaCalculoNrcap = fechaCalculoNrcap;
    }

    public BigDecimal getTotalIngresoNrcap() {
        return totalIngresoNrcap;
    }

    public void setTotalIngresoNrcap(BigDecimal totalIngresoNrcap) {
        this.totalIngresoNrcap = totalIngresoNrcap;
    }

    public BigDecimal getTotalEgresoNrcap() {
        return totalEgresoNrcap;
    }

    public void setTotalEgresoNrcap(BigDecimal totalEgresoNrcap) {
        this.totalEgresoNrcap = totalEgresoNrcap;
    }

    public Integer getNroMesNrcap() {
        return nroMesNrcap;
    }

    public void setNroMesNrcap(Integer nroMesNrcap) {
        this.nroMesNrcap = nroMesNrcap;
    }

    public BigDecimal getMontoRecibirNrcap() {
        return montoRecibirNrcap;
    }

    public void setMontoRecibirNrcap(BigDecimal montoRecibirNrcap) {
        this.montoRecibirNrcap = montoRecibirNrcap;
    }

    public BigDecimal getCuotaMensualNrcap() {
        return cuotaMensualNrcap;
    }

    public void setCuotaMensualNrcap(BigDecimal cuotaMensualNrcap) {
        this.cuotaMensualNrcap = cuotaMensualNrcap;
    }

    public BigDecimal getPorcentajeEndeudaNrcap() {
        return porcentajeEndeudaNrcap;
    }

    public void setPorcentajeEndeudaNrcap(BigDecimal porcentajeEndeudaNrcap) {
        this.porcentajeEndeudaNrcap = porcentajeEndeudaNrcap;
    }

    public BigDecimal getCuotaLimiteNrcap() {
        return cuotaLimiteNrcap;
    }

    public void setCuotaLimiteNrcap(BigDecimal cuotaLimiteNrcap) {
        this.cuotaLimiteNrcap = cuotaLimiteNrcap;
    }

    public boolean getActivoNrcap() {
        return activoNrcap;
    }

    public void setActivoNrcap(boolean activoNrcap) {
        this.activoNrcap = activoNrcap;
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

    public NrhAnticipo getIdeNrant() {
        return ideNrant;
    }

    public void setIdeNrant(NrhAnticipo ideNrant) {
        this.ideNrant = ideNrant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrcap != null ? ideNrcap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhCapacidadPago)) {
            return false;
        }
        NrhCapacidadPago other = (NrhCapacidadPago) object;
        if ((this.ideNrcap == null && other.ideNrcap != null) || (this.ideNrcap != null && !this.ideNrcap.equals(other.ideNrcap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhCapacidadPago[ ideNrcap=" + ideNrcap + " ]";
    }
    
}
