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
@Table(name = "gth_tipo_sindicato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoSindicato.findAll", query = "SELECT g FROM GthTipoSindicato g"),
    @NamedQuery(name = "GthTipoSindicato.findByIdeGttsi", query = "SELECT g FROM GthTipoSindicato g WHERE g.ideGttsi = :ideGttsi"),
    @NamedQuery(name = "GthTipoSindicato.findByDetalleGttsi", query = "SELECT g FROM GthTipoSindicato g WHERE g.detalleGttsi = :detalleGttsi"),
    @NamedQuery(name = "GthTipoSindicato.findByActivoGttsi", query = "SELECT g FROM GthTipoSindicato g WHERE g.activoGttsi = :activoGttsi"),
    @NamedQuery(name = "GthTipoSindicato.findByUsuarioIngre", query = "SELECT g FROM GthTipoSindicato g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoSindicato.findByFechaIngre", query = "SELECT g FROM GthTipoSindicato g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoSindicato.findByUsuarioActua", query = "SELECT g FROM GthTipoSindicato g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoSindicato.findByFechaActua", query = "SELECT g FROM GthTipoSindicato g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoSindicato.findByHoraIngre", query = "SELECT g FROM GthTipoSindicato g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoSindicato.findByHoraActua", query = "SELECT g FROM GthTipoSindicato g WHERE g.horaActua = :horaActua")})
public class GthTipoSindicato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttsi")
    private Integer ideGttsi;
    @Column(name = "detalle_gttsi")
    private String detalleGttsi;
    @Column(name = "activo_gttsi")
    private Boolean activoGttsi;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttsi")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;

    public GthTipoSindicato() {
    }

    public GthTipoSindicato(Integer ideGttsi) {
        this.ideGttsi = ideGttsi;
    }

    public Integer getIdeGttsi() {
        return ideGttsi;
    }

    public void setIdeGttsi(Integer ideGttsi) {
        this.ideGttsi = ideGttsi;
    }

    public String getDetalleGttsi() {
        return detalleGttsi;
    }

    public void setDetalleGttsi(String detalleGttsi) {
        this.detalleGttsi = detalleGttsi;
    }

    public Boolean getActivoGttsi() {
        return activoGttsi;
    }

    public void setActivoGttsi(Boolean activoGttsi) {
        this.activoGttsi = activoGttsi;
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
    public Collection<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParCollection() {
        return genEmpleadosDepartamentoParCollection;
    }

    public void setGenEmpleadosDepartamentoParCollection(Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection) {
        this.genEmpleadosDepartamentoParCollection = genEmpleadosDepartamentoParCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttsi != null ? ideGttsi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoSindicato)) {
            return false;
        }
        GthTipoSindicato other = (GthTipoSindicato) object;
        if ((this.ideGttsi == null && other.ideGttsi != null) || (this.ideGttsi != null && !this.ideGttsi.equals(other.ideGttsi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoSindicato[ ideGttsi=" + ideGttsi + " ]";
    }
    
}
