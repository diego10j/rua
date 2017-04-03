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
@Table(name = "gth_tipo_archivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoArchivo.findAll", query = "SELECT g FROM GthTipoArchivo g"),
    @NamedQuery(name = "GthTipoArchivo.findByIdeGttar", query = "SELECT g FROM GthTipoArchivo g WHERE g.ideGttar = :ideGttar"),
    @NamedQuery(name = "GthTipoArchivo.findByDetalleGttar", query = "SELECT g FROM GthTipoArchivo g WHERE g.detalleGttar = :detalleGttar"),
    @NamedQuery(name = "GthTipoArchivo.findByActivoGttar", query = "SELECT g FROM GthTipoArchivo g WHERE g.activoGttar = :activoGttar"),
    @NamedQuery(name = "GthTipoArchivo.findByUsuarioIngre", query = "SELECT g FROM GthTipoArchivo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoArchivo.findByFechaIngre", query = "SELECT g FROM GthTipoArchivo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoArchivo.findByUsuarioActua", query = "SELECT g FROM GthTipoArchivo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoArchivo.findByFechaActua", query = "SELECT g FROM GthTipoArchivo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoArchivo.findByHoraIngre", query = "SELECT g FROM GthTipoArchivo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoArchivo.findByHoraActua", query = "SELECT g FROM GthTipoArchivo g WHERE g.horaActua = :horaActua")})
public class GthTipoArchivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttar")
    private Integer ideGttar;
    @Column(name = "detalle_gttar")
    private String detalleGttar;
    @Column(name = "activo_gttar")
    private Boolean activoGttar;
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
    @OneToMany(mappedBy = "ideGttar")
    private Collection<GthArchivoEmpleado> gthArchivoEmpleadoCollection;

    public GthTipoArchivo() {
    }

    public GthTipoArchivo(Integer ideGttar) {
        this.ideGttar = ideGttar;
    }

    public Integer getIdeGttar() {
        return ideGttar;
    }

    public void setIdeGttar(Integer ideGttar) {
        this.ideGttar = ideGttar;
    }

    public String getDetalleGttar() {
        return detalleGttar;
    }

    public void setDetalleGttar(String detalleGttar) {
        this.detalleGttar = detalleGttar;
    }

    public Boolean getActivoGttar() {
        return activoGttar;
    }

    public void setActivoGttar(Boolean activoGttar) {
        this.activoGttar = activoGttar;
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
    public Collection<GthArchivoEmpleado> getGthArchivoEmpleadoCollection() {
        return gthArchivoEmpleadoCollection;
    }

    public void setGthArchivoEmpleadoCollection(Collection<GthArchivoEmpleado> gthArchivoEmpleadoCollection) {
        this.gthArchivoEmpleadoCollection = gthArchivoEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttar != null ? ideGttar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoArchivo)) {
            return false;
        }
        GthTipoArchivo other = (GthTipoArchivo) object;
        if ((this.ideGttar == null && other.ideGttar != null) || (this.ideGttar != null && !this.ideGttar.equals(other.ideGttar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoArchivo[ ideGttar=" + ideGttar + " ]";
    }
    
}
