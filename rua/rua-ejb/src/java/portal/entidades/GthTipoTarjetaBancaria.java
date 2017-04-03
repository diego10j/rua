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
@Table(name = "gth_tipo_tarjeta_bancaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoTarjetaBancaria.findAll", query = "SELECT g FROM GthTipoTarjetaBancaria g"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByIdeGtttb", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.ideGtttb = :ideGtttb"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByDetalleGtttb", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.detalleGtttb = :detalleGtttb"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByActivoGtttb", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.activoGtttb = :activoGtttb"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByUsuarioIngre", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByFechaIngre", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByUsuarioActua", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByFechaActua", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByHoraIngre", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByHoraActua", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.horaActua = :horaActua")})
public class GthTipoTarjetaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtttb")
    private Integer ideGtttb;
    @Column(name = "detalle_gtttb")
    private String detalleGtttb;
    @Column(name = "activo_gtttb")
    private Boolean activoGtttb;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthTipoTarjetaBancaria")
    private Collection<GthTarjetaBancaria> gthTarjetaBancariaCollection;

    public GthTipoTarjetaBancaria() {
    }

    public GthTipoTarjetaBancaria(Integer ideGtttb) {
        this.ideGtttb = ideGtttb;
    }

    public Integer getIdeGtttb() {
        return ideGtttb;
    }

    public void setIdeGtttb(Integer ideGtttb) {
        this.ideGtttb = ideGtttb;
    }

    public String getDetalleGtttb() {
        return detalleGtttb;
    }

    public void setDetalleGtttb(String detalleGtttb) {
        this.detalleGtttb = detalleGtttb;
    }

    public Boolean getActivoGtttb() {
        return activoGtttb;
    }

    public void setActivoGtttb(Boolean activoGtttb) {
        this.activoGtttb = activoGtttb;
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
    public Collection<GthTarjetaBancaria> getGthTarjetaBancariaCollection() {
        return gthTarjetaBancariaCollection;
    }

    public void setGthTarjetaBancariaCollection(Collection<GthTarjetaBancaria> gthTarjetaBancariaCollection) {
        this.gthTarjetaBancariaCollection = gthTarjetaBancariaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtttb != null ? ideGtttb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoTarjetaBancaria)) {
            return false;
        }
        GthTipoTarjetaBancaria other = (GthTipoTarjetaBancaria) object;
        if ((this.ideGtttb == null && other.ideGtttb != null) || (this.ideGtttb != null && !this.ideGtttb.equals(other.ideGtttb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoTarjetaBancaria[ ideGtttb=" + ideGtttb + " ]";
    }
    
}
