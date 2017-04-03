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
@Table(name = "gth_tipo_licencia_conducir")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoLicenciaConducir.findAll", query = "SELECT g FROM GthTipoLicenciaConducir g"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByIdeGttlc", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.ideGttlc = :ideGttlc"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByDetalleGttlc", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.detalleGttlc = :detalleGttlc"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByActivoGttlc", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.activoGttlc = :activoGttlc"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByUsuarioIngre", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByFechaIngre", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByUsuarioActua", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByFechaActua", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByHoraIngre", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoLicenciaConducir.findByHoraActua", query = "SELECT g FROM GthTipoLicenciaConducir g WHERE g.horaActua = :horaActua")})
public class GthTipoLicenciaConducir implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttlc")
    private Integer ideGttlc;
    @Column(name = "detalle_gttlc")
    private String detalleGttlc;
    @Column(name = "activo_gttlc")
    private Boolean activoGttlc;
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
    @OneToMany(mappedBy = "ideGttlc")
    private Collection<GthDocumentacionEmpleado> gthDocumentacionEmpleadoCollection;

    public GthTipoLicenciaConducir() {
    }

    public GthTipoLicenciaConducir(Integer ideGttlc) {
        this.ideGttlc = ideGttlc;
    }

    public Integer getIdeGttlc() {
        return ideGttlc;
    }

    public void setIdeGttlc(Integer ideGttlc) {
        this.ideGttlc = ideGttlc;
    }

    public String getDetalleGttlc() {
        return detalleGttlc;
    }

    public void setDetalleGttlc(String detalleGttlc) {
        this.detalleGttlc = detalleGttlc;
    }

    public Boolean getActivoGttlc() {
        return activoGttlc;
    }

    public void setActivoGttlc(Boolean activoGttlc) {
        this.activoGttlc = activoGttlc;
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
    public Collection<GthDocumentacionEmpleado> getGthDocumentacionEmpleadoCollection() {
        return gthDocumentacionEmpleadoCollection;
    }

    public void setGthDocumentacionEmpleadoCollection(Collection<GthDocumentacionEmpleado> gthDocumentacionEmpleadoCollection) {
        this.gthDocumentacionEmpleadoCollection = gthDocumentacionEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttlc != null ? ideGttlc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoLicenciaConducir)) {
            return false;
        }
        GthTipoLicenciaConducir other = (GthTipoLicenciaConducir) object;
        if ((this.ideGttlc == null && other.ideGttlc != null) || (this.ideGttlc != null && !this.ideGttlc.equals(other.ideGttlc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoLicenciaConducir[ ideGttlc=" + ideGttlc + " ]";
    }
    
}
