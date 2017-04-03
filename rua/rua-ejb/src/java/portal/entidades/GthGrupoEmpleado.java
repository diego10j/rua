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
@Table(name = "gth_grupo_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthGrupoEmpleado.findAll", query = "SELECT g FROM GthGrupoEmpleado g"),
    @NamedQuery(name = "GthGrupoEmpleado.findByIdeGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.ideGtgre = :ideGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByDetalleGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.detalleGtgre = :detalleGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByMinutoAlmuerzoGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.minutoAlmuerzoGtgre = :minutoAlmuerzoGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByActivoGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.activoGtgre = :activoGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByFechaIngre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthGrupoEmpleado.findByFechaActua", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthGrupoEmpleado.findByHoraIngre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByHoraActua", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthGrupoEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtgre")
    private Integer ideGtgre;
    @Column(name = "detalle_gtgre")
    private String detalleGtgre;
    @Column(name = "minuto_almuerzo_gtgre")
    private Integer minutoAlmuerzoGtgre;
    @Column(name = "activo_gtgre")
    private Boolean activoGtgre;
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
    @OneToMany(mappedBy = "ideGtgre")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;

    public GthGrupoEmpleado() {
    }

    public GthGrupoEmpleado(Integer ideGtgre) {
        this.ideGtgre = ideGtgre;
    }

    public Integer getIdeGtgre() {
        return ideGtgre;
    }

    public void setIdeGtgre(Integer ideGtgre) {
        this.ideGtgre = ideGtgre;
    }

    public String getDetalleGtgre() {
        return detalleGtgre;
    }

    public void setDetalleGtgre(String detalleGtgre) {
        this.detalleGtgre = detalleGtgre;
    }

    public Integer getMinutoAlmuerzoGtgre() {
        return minutoAlmuerzoGtgre;
    }

    public void setMinutoAlmuerzoGtgre(Integer minutoAlmuerzoGtgre) {
        this.minutoAlmuerzoGtgre = minutoAlmuerzoGtgre;
    }

    public Boolean getActivoGtgre() {
        return activoGtgre;
    }

    public void setActivoGtgre(Boolean activoGtgre) {
        this.activoGtgre = activoGtgre;
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
        hash += (ideGtgre != null ? ideGtgre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthGrupoEmpleado)) {
            return false;
        }
        GthGrupoEmpleado other = (GthGrupoEmpleado) object;
        if ((this.ideGtgre == null && other.ideGtgre != null) || (this.ideGtgre != null && !this.ideGtgre.equals(other.ideGtgre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthGrupoEmpleado[ ideGtgre=" + ideGtgre + " ]";
    }
    
}
