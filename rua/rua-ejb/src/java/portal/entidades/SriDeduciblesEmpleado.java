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
@Table(name = "sri_deducibles_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriDeduciblesEmpleado.findAll", query = "SELECT s FROM SriDeduciblesEmpleado s"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByIdeSrdee", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.ideSrdee = :ideSrdee"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByValorDeducibleSrdee", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.valorDeducibleSrdee = :valorDeducibleSrdee"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByObservacionSrdee", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.observacionSrdee = :observacionSrdee"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByActivoSrdee", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.activoSrdee = :activoSrdee"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByUsuarioIngre", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByFechaIngre", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByUsuarioActua", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByFechaActua", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByHoraIngre", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriDeduciblesEmpleado.findByHoraActua", query = "SELECT s FROM SriDeduciblesEmpleado s WHERE s.horaActua = :horaActua")})
public class SriDeduciblesEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_srdee")
    private Integer ideSrdee;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor_deducible_srdee")
    private BigDecimal valorDeducibleSrdee;
    @Column(name = "observacion_srdee")
    private String observacionSrdee;
    @Basic(optional = false)
    @Column(name = "activo_srdee")
    private boolean activoSrdee;
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
    @JoinColumn(name = "ide_srded", referencedColumnName = "ide_srded")
    @ManyToOne(optional = false)
    private SriDeducibles ideSrded;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;

    public SriDeduciblesEmpleado() {
    }

    public SriDeduciblesEmpleado(Integer ideSrdee) {
        this.ideSrdee = ideSrdee;
    }

    public SriDeduciblesEmpleado(Integer ideSrdee, BigDecimal valorDeducibleSrdee, boolean activoSrdee) {
        this.ideSrdee = ideSrdee;
        this.valorDeducibleSrdee = valorDeducibleSrdee;
        this.activoSrdee = activoSrdee;
    }

    public Integer getIdeSrdee() {
        return ideSrdee;
    }

    public void setIdeSrdee(Integer ideSrdee) {
        this.ideSrdee = ideSrdee;
    }

    public BigDecimal getValorDeducibleSrdee() {
        return valorDeducibleSrdee;
    }

    public void setValorDeducibleSrdee(BigDecimal valorDeducibleSrdee) {
        this.valorDeducibleSrdee = valorDeducibleSrdee;
    }

    public String getObservacionSrdee() {
        return observacionSrdee;
    }

    public void setObservacionSrdee(String observacionSrdee) {
        this.observacionSrdee = observacionSrdee;
    }

    public boolean getActivoSrdee() {
        return activoSrdee;
    }

    public void setActivoSrdee(boolean activoSrdee) {
        this.activoSrdee = activoSrdee;
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

    public SriDeducibles getIdeSrded() {
        return ideSrded;
    }

    public void setIdeSrded(SriDeducibles ideSrded) {
        this.ideSrded = ideSrded;
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
        hash += (ideSrdee != null ? ideSrdee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriDeduciblesEmpleado)) {
            return false;
        }
        SriDeduciblesEmpleado other = (SriDeduciblesEmpleado) object;
        if ((this.ideSrdee == null && other.ideSrdee != null) || (this.ideSrdee != null && !this.ideSrdee.equals(other.ideSrdee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriDeduciblesEmpleado[ ideSrdee=" + ideSrdee + " ]";
    }
    
}
