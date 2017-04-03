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
@Table(name = "gth_seguro_vida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthSeguroVida.findAll", query = "SELECT g FROM GthSeguroVida g"),
    @NamedQuery(name = "GthSeguroVida.findByIdeGtsev", query = "SELECT g FROM GthSeguroVida g WHERE g.ideGtsev = :ideGtsev"),
    @NamedQuery(name = "GthSeguroVida.findByObservacionGtsev", query = "SELECT g FROM GthSeguroVida g WHERE g.observacionGtsev = :observacionGtsev"),
    @NamedQuery(name = "GthSeguroVida.findByFechaVigenciaInicialGtsev", query = "SELECT g FROM GthSeguroVida g WHERE g.fechaVigenciaInicialGtsev = :fechaVigenciaInicialGtsev"),
    @NamedQuery(name = "GthSeguroVida.findByFechaVigenciaFinalGtsev", query = "SELECT g FROM GthSeguroVida g WHERE g.fechaVigenciaFinalGtsev = :fechaVigenciaFinalGtsev"),
    @NamedQuery(name = "GthSeguroVida.findByMontoSeguroGtsev", query = "SELECT g FROM GthSeguroVida g WHERE g.montoSeguroGtsev = :montoSeguroGtsev"),
    @NamedQuery(name = "GthSeguroVida.findByActivoGtsev", query = "SELECT g FROM GthSeguroVida g WHERE g.activoGtsev = :activoGtsev"),
    @NamedQuery(name = "GthSeguroVida.findByUsuarioIngre", query = "SELECT g FROM GthSeguroVida g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthSeguroVida.findByFechaIngre", query = "SELECT g FROM GthSeguroVida g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthSeguroVida.findByUsuarioActua", query = "SELECT g FROM GthSeguroVida g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthSeguroVida.findByFechaActua", query = "SELECT g FROM GthSeguroVida g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthSeguroVida.findByHoraIngre", query = "SELECT g FROM GthSeguroVida g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthSeguroVida.findByHoraActua", query = "SELECT g FROM GthSeguroVida g WHERE g.horaActua = :horaActua")})
public class GthSeguroVida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtsev")
    private Integer ideGtsev;
    @Column(name = "observacion_gtsev")
    private String observacionGtsev;
    @Column(name = "fecha_vigencia_inicial_gtsev")
    @Temporal(TemporalType.DATE)
    private Date fechaVigenciaInicialGtsev;
    @Column(name = "fecha_vigencia_final_gtsev")
    @Temporal(TemporalType.DATE)
    private Date fechaVigenciaFinalGtsev;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_seguro_gtsev")
    private BigDecimal montoSeguroGtsev;
    @Column(name = "activo_gtsev")
    private Boolean activoGtsev;
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
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne(optional = false)
    private GenInstitucion ideGeins;
    @OneToMany(mappedBy = "ideGtsev")
    private Collection<GthBeneficiarioSeguro> gthBeneficiarioSeguroCollection;

    public GthSeguroVida() {
    }

    public GthSeguroVida(Integer ideGtsev) {
        this.ideGtsev = ideGtsev;
    }

    public Integer getIdeGtsev() {
        return ideGtsev;
    }

    public void setIdeGtsev(Integer ideGtsev) {
        this.ideGtsev = ideGtsev;
    }

    public String getObservacionGtsev() {
        return observacionGtsev;
    }

    public void setObservacionGtsev(String observacionGtsev) {
        this.observacionGtsev = observacionGtsev;
    }

    public Date getFechaVigenciaInicialGtsev() {
        return fechaVigenciaInicialGtsev;
    }

    public void setFechaVigenciaInicialGtsev(Date fechaVigenciaInicialGtsev) {
        this.fechaVigenciaInicialGtsev = fechaVigenciaInicialGtsev;
    }

    public Date getFechaVigenciaFinalGtsev() {
        return fechaVigenciaFinalGtsev;
    }

    public void setFechaVigenciaFinalGtsev(Date fechaVigenciaFinalGtsev) {
        this.fechaVigenciaFinalGtsev = fechaVigenciaFinalGtsev;
    }

    public BigDecimal getMontoSeguroGtsev() {
        return montoSeguroGtsev;
    }

    public void setMontoSeguroGtsev(BigDecimal montoSeguroGtsev) {
        this.montoSeguroGtsev = montoSeguroGtsev;
    }

    public Boolean getActivoGtsev() {
        return activoGtsev;
    }

    public void setActivoGtsev(Boolean activoGtsev) {
        this.activoGtsev = activoGtsev;
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

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    @XmlTransient
    public Collection<GthBeneficiarioSeguro> getGthBeneficiarioSeguroCollection() {
        return gthBeneficiarioSeguroCollection;
    }

    public void setGthBeneficiarioSeguroCollection(Collection<GthBeneficiarioSeguro> gthBeneficiarioSeguroCollection) {
        this.gthBeneficiarioSeguroCollection = gthBeneficiarioSeguroCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtsev != null ? ideGtsev.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthSeguroVida)) {
            return false;
        }
        GthSeguroVida other = (GthSeguroVida) object;
        if ((this.ideGtsev == null && other.ideGtsev != null) || (this.ideGtsev != null && !this.ideGtsev.equals(other.ideGtsev))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthSeguroVida[ ideGtsev=" + ideGtsev + " ]";
    }
    
}
