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
@Table(name = "gth_vehiculo_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthVehiculoEmpleado.findAll", query = "SELECT g FROM GthVehiculoEmpleado g"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByIdeGtvee", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.ideGtvee = :ideGtvee"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByPropioPrendadoGtvee", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.propioPrendadoGtvee = :propioPrendadoGtvee"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByPrendadoAfavorGtvee", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.prendadoAfavorGtvee = :prendadoAfavorGtvee"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByPlacaGtvee", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.placaGtvee = :placaGtvee"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByMontoSeguroGtvee", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.montoSeguroGtvee = :montoSeguroGtvee"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByAvaluoGtvee", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.avaluoGtvee = :avaluoGtvee"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByActivoGtvee", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.activoGtvee = :activoGtvee"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByFechaIngre", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByFechaActua", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByHoraIngre", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthVehiculoEmpleado.findByHoraActua", query = "SELECT g FROM GthVehiculoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthVehiculoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtvee")
    private Integer ideGtvee;
    @Column(name = "propio_prendado_gtvee")
    private Integer propioPrendadoGtvee;
    @Column(name = "prendado_afavor_gtvee")
    private String prendadoAfavorGtvee;
    @Column(name = "placa_gtvee")
    private String placaGtvee;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_seguro_gtvee")
    private BigDecimal montoSeguroGtvee;
    @Column(name = "avaluo_gtvee")
    private BigDecimal avaluoGtvee;
    @Column(name = "activo_gtvee")
    private Boolean activoGtvee;
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
        @JoinColumn(name = "ide_gtmov", referencedColumnName = "ide_gtmov"),
        @JoinColumn(name = "ide_gtmav", referencedColumnName = "ide_gtmav")})
    @ManyToOne
    private GthModeloVehiculo gthModeloVehiculo;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;

    public GthVehiculoEmpleado() {
    }

    public GthVehiculoEmpleado(Integer ideGtvee) {
        this.ideGtvee = ideGtvee;
    }

    public Integer getIdeGtvee() {
        return ideGtvee;
    }

    public void setIdeGtvee(Integer ideGtvee) {
        this.ideGtvee = ideGtvee;
    }

    public Integer getPropioPrendadoGtvee() {
        return propioPrendadoGtvee;
    }

    public void setPropioPrendadoGtvee(Integer propioPrendadoGtvee) {
        this.propioPrendadoGtvee = propioPrendadoGtvee;
    }

    public String getPrendadoAfavorGtvee() {
        return prendadoAfavorGtvee;
    }

    public void setPrendadoAfavorGtvee(String prendadoAfavorGtvee) {
        this.prendadoAfavorGtvee = prendadoAfavorGtvee;
    }

    public String getPlacaGtvee() {
        return placaGtvee;
    }

    public void setPlacaGtvee(String placaGtvee) {
        this.placaGtvee = placaGtvee;
    }

    public BigDecimal getMontoSeguroGtvee() {
        return montoSeguroGtvee;
    }

    public void setMontoSeguroGtvee(BigDecimal montoSeguroGtvee) {
        this.montoSeguroGtvee = montoSeguroGtvee;
    }

    public BigDecimal getAvaluoGtvee() {
        return avaluoGtvee;
    }

    public void setAvaluoGtvee(BigDecimal avaluoGtvee) {
        this.avaluoGtvee = avaluoGtvee;
    }

    public Boolean getActivoGtvee() {
        return activoGtvee;
    }

    public void setActivoGtvee(Boolean activoGtvee) {
        this.activoGtvee = activoGtvee;
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

    public GthModeloVehiculo getGthModeloVehiculo() {
        return gthModeloVehiculo;
    }

    public void setGthModeloVehiculo(GthModeloVehiculo gthModeloVehiculo) {
        this.gthModeloVehiculo = gthModeloVehiculo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtvee != null ? ideGtvee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthVehiculoEmpleado)) {
            return false;
        }
        GthVehiculoEmpleado other = (GthVehiculoEmpleado) object;
        if ((this.ideGtvee == null && other.ideGtvee != null) || (this.ideGtvee != null && !this.ideGtvee.equals(other.ideGtvee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthVehiculoEmpleado[ ideGtvee=" + ideGtvee + " ]";
    }
    
}
