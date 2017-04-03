/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "gen_base_legal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenBaseLegal.findAll", query = "SELECT g FROM GenBaseLegal g"),
    @NamedQuery(name = "GenBaseLegal.findByIdeGebal", query = "SELECT g FROM GenBaseLegal g WHERE g.ideGebal = :ideGebal"),
    @NamedQuery(name = "GenBaseLegal.findByDetalleGebal", query = "SELECT g FROM GenBaseLegal g WHERE g.detalleGebal = :detalleGebal"),
    @NamedQuery(name = "GenBaseLegal.findByActivoGebal", query = "SELECT g FROM GenBaseLegal g WHERE g.activoGebal = :activoGebal"),
    @NamedQuery(name = "GenBaseLegal.findByUsuarioIngre", query = "SELECT g FROM GenBaseLegal g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenBaseLegal.findByFechaIngre", query = "SELECT g FROM GenBaseLegal g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenBaseLegal.findByHoraIngre", query = "SELECT g FROM GenBaseLegal g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenBaseLegal.findByUsuarioActua", query = "SELECT g FROM GenBaseLegal g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenBaseLegal.findByFechaActua", query = "SELECT g FROM GenBaseLegal g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenBaseLegal.findByHoraActua", query = "SELECT g FROM GenBaseLegal g WHERE g.horaActua = :horaActua")})
public class GenBaseLegal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gebal")
    private Long ideGebal;
    @Column(name = "detalle_gebal")
    private String detalleGebal;
    @Column(name = "activo_gebal")
    private Boolean activoGebal;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;

    public GenBaseLegal() {
    }

    public GenBaseLegal(Long ideGebal) {
        this.ideGebal = ideGebal;
    }

    public Long getIdeGebal() {
        return ideGebal;
    }

    public void setIdeGebal(Long ideGebal) {
        this.ideGebal = ideGebal;
    }

    public String getDetalleGebal() {
        return detalleGebal;
    }

    public void setDetalleGebal(String detalleGebal) {
        this.detalleGebal = detalleGebal;
    }

    public Boolean getActivoGebal() {
        return activoGebal;
    }

    public void setActivoGebal(Boolean activoGebal) {
        this.activoGebal = activoGebal;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGebal != null ? ideGebal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenBaseLegal)) {
            return false;
        }
        GenBaseLegal other = (GenBaseLegal) object;
        if ((this.ideGebal == null && other.ideGebal != null) || (this.ideGebal != null && !this.ideGebal.equals(other.ideGebal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenBaseLegal[ ideGebal=" + ideGebal + " ]";
    }
    
}
