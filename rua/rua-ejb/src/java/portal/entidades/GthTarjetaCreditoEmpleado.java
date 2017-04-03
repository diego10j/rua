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
import javax.persistence.JoinColumns;
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
@Table(name = "gth_tarjeta_credito_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findAll", query = "SELECT g FROM GthTarjetaCreditoEmpleado g"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByIdeGttce", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.ideGttce = :ideGttce"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByNumeroTargetaGttce", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.numeroTargetaGttce = :numeroTargetaGttce"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByPrincipalAdicionalGttce", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.principalAdicionalGttce = :principalAdicionalGttce"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByCupoGttce", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.cupoGttce = :cupoGttce"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByMontoPromedioMensualGttce", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.montoPromedioMensualGttce = :montoPromedioMensualGttce"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByFechaVencimientoGttce", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.fechaVencimientoGttce = :fechaVencimientoGttce"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByActivoGttce", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.activoGttce = :activoGttce"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByFechaIngre", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByFechaActua", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByHoraIngre", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTarjetaCreditoEmpleado.findByHoraActua", query = "SELECT g FROM GthTarjetaCreditoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthTarjetaCreditoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttce")
    private Integer ideGttce;
    @Column(name = "numero_targeta_gttce")
    private String numeroTargetaGttce;
    @Column(name = "principal_adicional_gttce")
    private Integer principalAdicionalGttce;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cupo_gttce")
    private BigDecimal cupoGttce;
    @Column(name = "monto_promedio_mensual_gttce")
    private BigDecimal montoPromedioMensualGttce;
    @Column(name = "fecha_vencimiento_gttce")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimientoGttce;
    @Column(name = "activo_gttce")
    private Boolean activoGttce;
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
    @JoinColumns({
        @JoinColumn(name = "ide_gttab", referencedColumnName = "ide_gttab"),
        @JoinColumn(name = "ide_gtttb", referencedColumnName = "ide_gtttb")})
    @ManyToOne
    private GthTarjetaBancaria gthTarjetaBancaria;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public GthTarjetaCreditoEmpleado() {
    }

    public GthTarjetaCreditoEmpleado(Integer ideGttce) {
        this.ideGttce = ideGttce;
    }

    public Integer getIdeGttce() {
        return ideGttce;
    }

    public void setIdeGttce(Integer ideGttce) {
        this.ideGttce = ideGttce;
    }

    public String getNumeroTargetaGttce() {
        return numeroTargetaGttce;
    }

    public void setNumeroTargetaGttce(String numeroTargetaGttce) {
        this.numeroTargetaGttce = numeroTargetaGttce;
    }

    public Integer getPrincipalAdicionalGttce() {
        return principalAdicionalGttce;
    }

    public void setPrincipalAdicionalGttce(Integer principalAdicionalGttce) {
        this.principalAdicionalGttce = principalAdicionalGttce;
    }

    public BigDecimal getCupoGttce() {
        return cupoGttce;
    }

    public void setCupoGttce(BigDecimal cupoGttce) {
        this.cupoGttce = cupoGttce;
    }

    public BigDecimal getMontoPromedioMensualGttce() {
        return montoPromedioMensualGttce;
    }

    public void setMontoPromedioMensualGttce(BigDecimal montoPromedioMensualGttce) {
        this.montoPromedioMensualGttce = montoPromedioMensualGttce;
    }

    public Date getFechaVencimientoGttce() {
        return fechaVencimientoGttce;
    }

    public void setFechaVencimientoGttce(Date fechaVencimientoGttce) {
        this.fechaVencimientoGttce = fechaVencimientoGttce;
    }

    public Boolean getActivoGttce() {
        return activoGttce;
    }

    public void setActivoGttce(Boolean activoGttce) {
        this.activoGttce = activoGttce;
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

    public GthTarjetaBancaria getGthTarjetaBancaria() {
        return gthTarjetaBancaria;
    }

    public void setGthTarjetaBancaria(GthTarjetaBancaria gthTarjetaBancaria) {
        this.gthTarjetaBancaria = gthTarjetaBancaria;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttce != null ? ideGttce.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTarjetaCreditoEmpleado)) {
            return false;
        }
        GthTarjetaCreditoEmpleado other = (GthTarjetaCreditoEmpleado) object;
        if ((this.ideGttce == null && other.ideGttce != null) || (this.ideGttce != null && !this.ideGttce.equals(other.ideGttce))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTarjetaCreditoEmpleado[ ideGttce=" + ideGttce + " ]";
    }
    
}
