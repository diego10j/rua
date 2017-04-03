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
@Table(name = "nrh_estado_nomina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhEstadoNomina.findAll", query = "SELECT n FROM NrhEstadoNomina n"),
    @NamedQuery(name = "NrhEstadoNomina.findByIdeNresn", query = "SELECT n FROM NrhEstadoNomina n WHERE n.ideNresn = :ideNresn"),
    @NamedQuery(name = "NrhEstadoNomina.findByUsuarioIngre", query = "SELECT n FROM NrhEstadoNomina n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhEstadoNomina.findByFechaIngre", query = "SELECT n FROM NrhEstadoNomina n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhEstadoNomina.findByUsuarioActua", query = "SELECT n FROM NrhEstadoNomina n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhEstadoNomina.findByFechaActua", query = "SELECT n FROM NrhEstadoNomina n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhEstadoNomina.findByActivoNresn", query = "SELECT n FROM NrhEstadoNomina n WHERE n.activoNresn = :activoNresn"),
    @NamedQuery(name = "NrhEstadoNomina.findByDetalleNresn", query = "SELECT n FROM NrhEstadoNomina n WHERE n.detalleNresn = :detalleNresn"),
    @NamedQuery(name = "NrhEstadoNomina.findByHoraIngre", query = "SELECT n FROM NrhEstadoNomina n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhEstadoNomina.findByHoraActua", query = "SELECT n FROM NrhEstadoNomina n WHERE n.horaActua = :horaActua")})
public class NrhEstadoNomina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nresn")
    private Integer ideNresn;
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
    @Column(name = "activo_nresn")
    private Boolean activoNresn;
    @Column(name = "detalle_nresn")
    private String detalleNresn;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;

    public NrhEstadoNomina() {
    }

    public NrhEstadoNomina(Integer ideNresn) {
        this.ideNresn = ideNresn;
    }

    public Integer getIdeNresn() {
        return ideNresn;
    }

    public void setIdeNresn(Integer ideNresn) {
        this.ideNresn = ideNresn;
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

    public Boolean getActivoNresn() {
        return activoNresn;
    }

    public void setActivoNresn(Boolean activoNresn) {
        this.activoNresn = activoNresn;
    }

    public String getDetalleNresn() {
        return detalleNresn;
    }

    public void setDetalleNresn(String detalleNresn) {
        this.detalleNresn = detalleNresn;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNresn != null ? ideNresn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhEstadoNomina)) {
            return false;
        }
        NrhEstadoNomina other = (NrhEstadoNomina) object;
        if ((this.ideNresn == null && other.ideNresn != null) || (this.ideNresn != null && !this.ideNresn.equals(other.ideNresn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhEstadoNomina[ ideNresn=" + ideNresn + " ]";
    }
    
}
