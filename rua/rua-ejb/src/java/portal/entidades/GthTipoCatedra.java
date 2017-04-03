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
@Table(name = "gth_tipo_catedra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoCatedra.findAll", query = "SELECT g FROM GthTipoCatedra g"),
    @NamedQuery(name = "GthTipoCatedra.findByIdeGttca", query = "SELECT g FROM GthTipoCatedra g WHERE g.ideGttca = :ideGttca"),
    @NamedQuery(name = "GthTipoCatedra.findByDetalleGttca", query = "SELECT g FROM GthTipoCatedra g WHERE g.detalleGttca = :detalleGttca"),
    @NamedQuery(name = "GthTipoCatedra.findByActivoGttca", query = "SELECT g FROM GthTipoCatedra g WHERE g.activoGttca = :activoGttca"),
    @NamedQuery(name = "GthTipoCatedra.findByUsuarioIngre", query = "SELECT g FROM GthTipoCatedra g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoCatedra.findByFechaIngre", query = "SELECT g FROM GthTipoCatedra g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoCatedra.findByUsuarioActua", query = "SELECT g FROM GthTipoCatedra g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoCatedra.findByFechaActua", query = "SELECT g FROM GthTipoCatedra g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoCatedra.findByHoraIngre", query = "SELECT g FROM GthTipoCatedra g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoCatedra.findByHoraActua", query = "SELECT g FROM GthTipoCatedra g WHERE g.horaActua = :horaActua")})
public class GthTipoCatedra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttca")
    private Integer ideGttca;
    @Column(name = "detalle_gttca")
    private String detalleGttca;
    @Column(name = "activo_gttca")
    private Boolean activoGttca;
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
    @OneToMany(mappedBy = "ideGttca")
    private Collection<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaCollection;

    public GthTipoCatedra() {
    }

    public GthTipoCatedra(Integer ideGttca) {
        this.ideGttca = ideGttca;
    }

    public Integer getIdeGttca() {
        return ideGttca;
    }

    public void setIdeGttca(Integer ideGttca) {
        this.ideGttca = ideGttca;
    }

    public String getDetalleGttca() {
        return detalleGttca;
    }

    public void setDetalleGttca(String detalleGttca) {
        this.detalleGttca = detalleGttca;
    }

    public Boolean getActivoGttca() {
        return activoGttca;
    }

    public void setActivoGttca(Boolean activoGttca) {
        this.activoGttca = activoGttca;
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
    public Collection<GthExperienciaDocenteEmplea> getGthExperienciaDocenteEmpleaCollection() {
        return gthExperienciaDocenteEmpleaCollection;
    }

    public void setGthExperienciaDocenteEmpleaCollection(Collection<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaCollection) {
        this.gthExperienciaDocenteEmpleaCollection = gthExperienciaDocenteEmpleaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttca != null ? ideGttca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoCatedra)) {
            return false;
        }
        GthTipoCatedra other = (GthTipoCatedra) object;
        if ((this.ideGttca == null && other.ideGttca != null) || (this.ideGttca != null && !this.ideGttca.equals(other.ideGttca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoCatedra[ ideGttca=" + ideGttca + " ]";
    }
    
}
