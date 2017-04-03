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
import javax.persistence.CascadeType;
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
@Table(name = "sri_deducibles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SriDeducibles.findAll", query = "SELECT s FROM SriDeducibles s"),
    @NamedQuery(name = "SriDeducibles.findByIdeSrded", query = "SELECT s FROM SriDeducibles s WHERE s.ideSrded = :ideSrded"),
    @NamedQuery(name = "SriDeducibles.findByDetalleSrded", query = "SELECT s FROM SriDeducibles s WHERE s.detalleSrded = :detalleSrded"),
    @NamedQuery(name = "SriDeducibles.findByFraccionBasicaSrded", query = "SELECT s FROM SriDeducibles s WHERE s.fraccionBasicaSrded = :fraccionBasicaSrded"),
    @NamedQuery(name = "SriDeducibles.findByObservacionesSrded", query = "SELECT s FROM SriDeducibles s WHERE s.observacionesSrded = :observacionesSrded"),
    @NamedQuery(name = "SriDeducibles.findByAlternoSriSrded", query = "SELECT s FROM SriDeducibles s WHERE s.alternoSriSrded = :alternoSriSrded"),
    @NamedQuery(name = "SriDeducibles.findByActivoSrded", query = "SELECT s FROM SriDeducibles s WHERE s.activoSrded = :activoSrded"),
    @NamedQuery(name = "SriDeducibles.findByUsuarioIngre", query = "SELECT s FROM SriDeducibles s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriDeducibles.findByFechaIngre", query = "SELECT s FROM SriDeducibles s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriDeducibles.findByUsuarioActua", query = "SELECT s FROM SriDeducibles s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriDeducibles.findByFechaActua", query = "SELECT s FROM SriDeducibles s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriDeducibles.findByHoraIngre", query = "SELECT s FROM SriDeducibles s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriDeducibles.findByHoraActua", query = "SELECT s FROM SriDeducibles s WHERE s.horaActua = :horaActua")})
public class SriDeducibles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_srded")
    private Integer ideSrded;
    @Column(name = "detalle_srded")
    private String detalleSrded;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fraccion_basica_srded")
    private BigDecimal fraccionBasicaSrded;
    @Column(name = "observaciones_srded")
    private String observacionesSrded;
    @Column(name = "alterno_sri_srded")
    private Integer alternoSriSrded;
    @Basic(optional = false)
    @Column(name = "activo_srded")
    private boolean activoSrded;
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
    @JoinColumn(name = "ide_srimr", referencedColumnName = "ide_srimr")
    @ManyToOne
    private SriImpuestoRenta ideSrimr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideSrded")
    private Collection<SriDeduciblesEmpleado> sriDeduciblesEmpleadoCollection;

    public SriDeducibles() {
    }

    public SriDeducibles(Integer ideSrded) {
        this.ideSrded = ideSrded;
    }

    public SriDeducibles(Integer ideSrded, boolean activoSrded) {
        this.ideSrded = ideSrded;
        this.activoSrded = activoSrded;
    }

    public Integer getIdeSrded() {
        return ideSrded;
    }

    public void setIdeSrded(Integer ideSrded) {
        this.ideSrded = ideSrded;
    }

    public String getDetalleSrded() {
        return detalleSrded;
    }

    public void setDetalleSrded(String detalleSrded) {
        this.detalleSrded = detalleSrded;
    }

    public BigDecimal getFraccionBasicaSrded() {
        return fraccionBasicaSrded;
    }

    public void setFraccionBasicaSrded(BigDecimal fraccionBasicaSrded) {
        this.fraccionBasicaSrded = fraccionBasicaSrded;
    }

    public String getObservacionesSrded() {
        return observacionesSrded;
    }

    public void setObservacionesSrded(String observacionesSrded) {
        this.observacionesSrded = observacionesSrded;
    }

    public Integer getAlternoSriSrded() {
        return alternoSriSrded;
    }

    public void setAlternoSriSrded(Integer alternoSriSrded) {
        this.alternoSriSrded = alternoSriSrded;
    }

    public boolean getActivoSrded() {
        return activoSrded;
    }

    public void setActivoSrded(boolean activoSrded) {
        this.activoSrded = activoSrded;
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

    public SriImpuestoRenta getIdeSrimr() {
        return ideSrimr;
    }

    public void setIdeSrimr(SriImpuestoRenta ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    @XmlTransient
    public Collection<SriDeduciblesEmpleado> getSriDeduciblesEmpleadoCollection() {
        return sriDeduciblesEmpleadoCollection;
    }

    public void setSriDeduciblesEmpleadoCollection(Collection<SriDeduciblesEmpleado> sriDeduciblesEmpleadoCollection) {
        this.sriDeduciblesEmpleadoCollection = sriDeduciblesEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrded != null ? ideSrded.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriDeducibles)) {
            return false;
        }
        SriDeducibles other = (SriDeducibles) object;
        if ((this.ideSrded == null && other.ideSrded != null) || (this.ideSrded != null && !this.ideSrded.equals(other.ideSrded))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriDeducibles[ ideSrded=" + ideSrded + " ]";
    }
    
}
