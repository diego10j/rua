/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "nrh_razon_beneficio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhRazonBeneficio.findAll", query = "SELECT n FROM NrhRazonBeneficio n"),
    @NamedQuery(name = "NrhRazonBeneficio.findByIdeNrrab", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.ideNrrab = :ideNrrab"),
    @NamedQuery(name = "NrhRazonBeneficio.findByDetalleNrrab", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.detalleNrrab = :detalleNrrab"),
    @NamedQuery(name = "NrhRazonBeneficio.findByActivoNrrab", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.activoNrrab = :activoNrrab"),
    @NamedQuery(name = "NrhRazonBeneficio.findByUsuarioIngre", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRazonBeneficio.findByFechaIngre", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRazonBeneficio.findByUsuarioActua", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRazonBeneficio.findByFechaActua", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRazonBeneficio.findByHoraIngre", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRazonBeneficio.findByHoraActua", query = "SELECT n FROM NrhRazonBeneficio n WHERE n.horaActua = :horaActua")})
public class NrhRazonBeneficio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrrab")
    private Integer ideNrrab;
    @Column(name = "detalle_nrrab")
    private String detalleNrrab;
    @Column(name = "activo_nrrab")
    private Boolean activoNrrab;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrrab")
    private Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection;

    public NrhRazonBeneficio() {
    }

    public NrhRazonBeneficio(Integer ideNrrab) {
        this.ideNrrab = ideNrrab;
    }

    public Integer getIdeNrrab() {
        return ideNrrab;
    }

    public void setIdeNrrab(Integer ideNrrab) {
        this.ideNrrab = ideNrrab;
    }

    public String getDetalleNrrab() {
        return detalleNrrab;
    }

    public void setDetalleNrrab(String detalleNrrab) {
        this.detalleNrrab = detalleNrrab;
    }

    public Boolean getActivoNrrab() {
        return activoNrrab;
    }

    public void setActivoNrrab(Boolean activoNrrab) {
        this.activoNrrab = activoNrrab;
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

    @XmlTransient
    public Collection<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoCollection() {
        return nrhBeneficioEmpleadoCollection;
    }

    public void setNrhBeneficioEmpleadoCollection(Collection<NrhBeneficioEmpleado> nrhBeneficioEmpleadoCollection) {
        this.nrhBeneficioEmpleadoCollection = nrhBeneficioEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrab != null ? ideNrrab.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRazonBeneficio)) {
            return false;
        }
        NrhRazonBeneficio other = (NrhRazonBeneficio) object;
        if ((this.ideNrrab == null && other.ideNrrab != null) || (this.ideNrrab != null && !this.ideNrrab.equals(other.ideNrrab))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRazonBeneficio[ ideNrrab=" + ideNrrab + " ]";
    }
    
}
