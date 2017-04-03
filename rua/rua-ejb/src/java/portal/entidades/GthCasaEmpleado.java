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
@Table(name = "gth_casa_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthCasaEmpleado.findAll", query = "SELECT g FROM GthCasaEmpleado g"),
    @NamedQuery(name = "GthCasaEmpleado.findByIdeGtcse", query = "SELECT g FROM GthCasaEmpleado g WHERE g.ideGtcse = :ideGtcse"),
    @NamedQuery(name = "GthCasaEmpleado.findByPropiaGtcse", query = "SELECT g FROM GthCasaEmpleado g WHERE g.propiaGtcse = :propiaGtcse"),
    @NamedQuery(name = "GthCasaEmpleado.findByAfavorGtcse", query = "SELECT g FROM GthCasaEmpleado g WHERE g.afavorGtcse = :afavorGtcse"),
    @NamedQuery(name = "GthCasaEmpleado.findByArrendatarioGtcse", query = "SELECT g FROM GthCasaEmpleado g WHERE g.arrendatarioGtcse = :arrendatarioGtcse"),
    @NamedQuery(name = "GthCasaEmpleado.findByMontoArriendoGtcse", query = "SELECT g FROM GthCasaEmpleado g WHERE g.montoArriendoGtcse = :montoArriendoGtcse"),
    @NamedQuery(name = "GthCasaEmpleado.findByAvaluoGtcse", query = "SELECT g FROM GthCasaEmpleado g WHERE g.avaluoGtcse = :avaluoGtcse"),
    @NamedQuery(name = "GthCasaEmpleado.findByActivoGtcse", query = "SELECT g FROM GthCasaEmpleado g WHERE g.activoGtcse = :activoGtcse"),
    @NamedQuery(name = "GthCasaEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthCasaEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCasaEmpleado.findByFechaIngre", query = "SELECT g FROM GthCasaEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCasaEmpleado.findByUsuarioActua", query = "SELECT g FROM GthCasaEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCasaEmpleado.findByFechaActua", query = "SELECT g FROM GthCasaEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCasaEmpleado.findByHoraIngre", query = "SELECT g FROM GthCasaEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCasaEmpleado.findByHoraActua", query = "SELECT g FROM GthCasaEmpleado g WHERE g.horaActua = :horaActua")})
public class GthCasaEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtcse")
    private Integer ideGtcse;
    @Column(name = "propia_gtcse")
    private Integer propiaGtcse;
    @Column(name = "afavor_gtcse")
    private String afavorGtcse;
    @Column(name = "arrendatario_gtcse")
    private String arrendatarioGtcse;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_arriendo_gtcse")
    private BigDecimal montoArriendoGtcse;
    @Column(name = "avaluo_gtcse")
    private BigDecimal avaluoGtcse;
    @Column(name = "activo_gtcse")
    private Boolean activoGtcse;
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

    public GthCasaEmpleado() {
    }

    public GthCasaEmpleado(Integer ideGtcse) {
        this.ideGtcse = ideGtcse;
    }

    public Integer getIdeGtcse() {
        return ideGtcse;
    }

    public void setIdeGtcse(Integer ideGtcse) {
        this.ideGtcse = ideGtcse;
    }

    public Integer getPropiaGtcse() {
        return propiaGtcse;
    }

    public void setPropiaGtcse(Integer propiaGtcse) {
        this.propiaGtcse = propiaGtcse;
    }

    public String getAfavorGtcse() {
        return afavorGtcse;
    }

    public void setAfavorGtcse(String afavorGtcse) {
        this.afavorGtcse = afavorGtcse;
    }

    public String getArrendatarioGtcse() {
        return arrendatarioGtcse;
    }

    public void setArrendatarioGtcse(String arrendatarioGtcse) {
        this.arrendatarioGtcse = arrendatarioGtcse;
    }

    public BigDecimal getMontoArriendoGtcse() {
        return montoArriendoGtcse;
    }

    public void setMontoArriendoGtcse(BigDecimal montoArriendoGtcse) {
        this.montoArriendoGtcse = montoArriendoGtcse;
    }

    public BigDecimal getAvaluoGtcse() {
        return avaluoGtcse;
    }

    public void setAvaluoGtcse(BigDecimal avaluoGtcse) {
        this.avaluoGtcse = avaluoGtcse;
    }

    public Boolean getActivoGtcse() {
        return activoGtcse;
    }

    public void setActivoGtcse(Boolean activoGtcse) {
        this.activoGtcse = activoGtcse;
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
        hash += (ideGtcse != null ? ideGtcse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCasaEmpleado)) {
            return false;
        }
        GthCasaEmpleado other = (GthCasaEmpleado) object;
        if ((this.ideGtcse == null && other.ideGtcse != null) || (this.ideGtcse != null && !this.ideGtcse.equals(other.ideGtcse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCasaEmpleado[ ideGtcse=" + ideGtcse + " ]";
    }
    
}
