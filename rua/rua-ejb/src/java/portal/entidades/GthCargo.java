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
@Table(name = "gth_cargo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthCargo.findAll", query = "SELECT g FROM GthCargo g"),
    @NamedQuery(name = "GthCargo.findByIdeGtcar", query = "SELECT g FROM GthCargo g WHERE g.ideGtcar = :ideGtcar"),
    @NamedQuery(name = "GthCargo.findByDetalleGtcar", query = "SELECT g FROM GthCargo g WHERE g.detalleGtcar = :detalleGtcar"),
    @NamedQuery(name = "GthCargo.findByActivoGtcar", query = "SELECT g FROM GthCargo g WHERE g.activoGtcar = :activoGtcar"),
    @NamedQuery(name = "GthCargo.findByUsuarioIngre", query = "SELECT g FROM GthCargo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCargo.findByFechaIngre", query = "SELECT g FROM GthCargo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCargo.findByUsuarioActua", query = "SELECT g FROM GthCargo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCargo.findByFechaActua", query = "SELECT g FROM GthCargo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCargo.findByHoraIngre", query = "SELECT g FROM GthCargo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCargo.findByHoraActua", query = "SELECT g FROM GthCargo g WHERE g.horaActua = :horaActua")})
public class GthCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtcar")
    private Integer ideGtcar;
    @Basic(optional = false)
    @Column(name = "detalle_gtcar")
    private String detalleGtcar;
    @Basic(optional = false)
    @Column(name = "activo_gtcar")
    private boolean activoGtcar;
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
    @OneToMany(mappedBy = "ideGtcar")
    private Collection<GthConyuge> gthConyugeCollection;

    public GthCargo() {
    }

    public GthCargo(Integer ideGtcar) {
        this.ideGtcar = ideGtcar;
    }

    public GthCargo(Integer ideGtcar, String detalleGtcar, boolean activoGtcar) {
        this.ideGtcar = ideGtcar;
        this.detalleGtcar = detalleGtcar;
        this.activoGtcar = activoGtcar;
    }

    public Integer getIdeGtcar() {
        return ideGtcar;
    }

    public void setIdeGtcar(Integer ideGtcar) {
        this.ideGtcar = ideGtcar;
    }

    public String getDetalleGtcar() {
        return detalleGtcar;
    }

    public void setDetalleGtcar(String detalleGtcar) {
        this.detalleGtcar = detalleGtcar;
    }

    public boolean getActivoGtcar() {
        return activoGtcar;
    }

    public void setActivoGtcar(boolean activoGtcar) {
        this.activoGtcar = activoGtcar;
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
    public Collection<GthConyuge> getGthConyugeCollection() {
        return gthConyugeCollection;
    }

    public void setGthConyugeCollection(Collection<GthConyuge> gthConyugeCollection) {
        this.gthConyugeCollection = gthConyugeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcar != null ? ideGtcar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCargo)) {
            return false;
        }
        GthCargo other = (GthCargo) object;
        if ((this.ideGtcar == null && other.ideGtcar != null) || (this.ideGtcar != null && !this.ideGtcar.equals(other.ideGtcar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCargo[ ideGtcar=" + ideGtcar + " ]";
    }
    
}
