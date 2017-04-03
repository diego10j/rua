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
@Table(name = "gth_situacion_economica_emplea")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findAll", query = "SELECT g FROM GthSituacionEconomicaEmplea g"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByIdeGtsee", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.ideGtsee = :ideGtsee"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findBySueldoConyugeGtsee", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.sueldoConyugeGtsee = :sueldoConyugeGtsee"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByOtroIngresoGtsee", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.otroIngresoGtsee = :otroIngresoGtsee"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByTotalIngresoConyugeGtsee", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.totalIngresoConyugeGtsee = :totalIngresoConyugeGtsee"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByMontoMensualGtsee", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.montoMensualGtsee = :montoMensualGtsee"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByDetalleIngresoGtsee", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.detalleIngresoGtsee = :detalleIngresoGtsee"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByActivoGtsee", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.activoGtsee = :activoGtsee"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByUsuarioIngre", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByFechaIngre", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByUsuarioActua", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByFechaActua", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByHoraIngre", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthSituacionEconomicaEmplea.findByHoraActua", query = "SELECT g FROM GthSituacionEconomicaEmplea g WHERE g.horaActua = :horaActua")})
public class GthSituacionEconomicaEmplea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtsee")
    private Integer ideGtsee;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sueldo_conyuge_gtsee")
    private BigDecimal sueldoConyugeGtsee;
    @Column(name = "otro_ingreso_gtsee")
    private BigDecimal otroIngresoGtsee;
    @Column(name = "total_ingreso_conyuge_gtsee")
    private BigDecimal totalIngresoConyugeGtsee;
    @Column(name = "monto_mensual_gtsee")
    private BigDecimal montoMensualGtsee;
    @Column(name = "detalle_ingreso_gtsee")
    private String detalleIngresoGtsee;
    @Column(name = "activo_gtsee")
    private Boolean activoGtsee;
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

    public GthSituacionEconomicaEmplea() {
    }

    public GthSituacionEconomicaEmplea(Integer ideGtsee) {
        this.ideGtsee = ideGtsee;
    }

    public Integer getIdeGtsee() {
        return ideGtsee;
    }

    public void setIdeGtsee(Integer ideGtsee) {
        this.ideGtsee = ideGtsee;
    }

    public BigDecimal getSueldoConyugeGtsee() {
        return sueldoConyugeGtsee;
    }

    public void setSueldoConyugeGtsee(BigDecimal sueldoConyugeGtsee) {
        this.sueldoConyugeGtsee = sueldoConyugeGtsee;
    }

    public BigDecimal getOtroIngresoGtsee() {
        return otroIngresoGtsee;
    }

    public void setOtroIngresoGtsee(BigDecimal otroIngresoGtsee) {
        this.otroIngresoGtsee = otroIngresoGtsee;
    }

    public BigDecimal getTotalIngresoConyugeGtsee() {
        return totalIngresoConyugeGtsee;
    }

    public void setTotalIngresoConyugeGtsee(BigDecimal totalIngresoConyugeGtsee) {
        this.totalIngresoConyugeGtsee = totalIngresoConyugeGtsee;
    }

    public BigDecimal getMontoMensualGtsee() {
        return montoMensualGtsee;
    }

    public void setMontoMensualGtsee(BigDecimal montoMensualGtsee) {
        this.montoMensualGtsee = montoMensualGtsee;
    }

    public String getDetalleIngresoGtsee() {
        return detalleIngresoGtsee;
    }

    public void setDetalleIngresoGtsee(String detalleIngresoGtsee) {
        this.detalleIngresoGtsee = detalleIngresoGtsee;
    }

    public Boolean getActivoGtsee() {
        return activoGtsee;
    }

    public void setActivoGtsee(Boolean activoGtsee) {
        this.activoGtsee = activoGtsee;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtsee != null ? ideGtsee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthSituacionEconomicaEmplea)) {
            return false;
        }
        GthSituacionEconomicaEmplea other = (GthSituacionEconomicaEmplea) object;
        if ((this.ideGtsee == null && other.ideGtsee != null) || (this.ideGtsee != null && !this.ideGtsee.equals(other.ideGtsee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthSituacionEconomicaEmplea[ ideGtsee=" + ideGtsee + " ]";
    }
    
}
