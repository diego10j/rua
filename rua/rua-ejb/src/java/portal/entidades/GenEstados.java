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
@Table(name = "gen_estados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenEstados.findAll", query = "SELECT g FROM GenEstados g"),
    @NamedQuery(name = "GenEstados.findByIdeGeest", query = "SELECT g FROM GenEstados g WHERE g.ideGeest = :ideGeest"),
    @NamedQuery(name = "GenEstados.findByDetalleGeest", query = "SELECT g FROM GenEstados g WHERE g.detalleGeest = :detalleGeest"),
    @NamedQuery(name = "GenEstados.findByActivoGeest", query = "SELECT g FROM GenEstados g WHERE g.activoGeest = :activoGeest"),
    @NamedQuery(name = "GenEstados.findByUsuarioIngre", query = "SELECT g FROM GenEstados g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenEstados.findByFechaIngre", query = "SELECT g FROM GenEstados g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenEstados.findByUsuarioActua", query = "SELECT g FROM GenEstados g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenEstados.findByFechaActua", query = "SELECT g FROM GenEstados g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenEstados.findByHoraIngre", query = "SELECT g FROM GenEstados g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenEstados.findByHoraActua", query = "SELECT g FROM GenEstados g WHERE g.horaActua = :horaActua")})
public class GenEstados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geest")
    private Integer ideGeest;
    @Basic(optional = false)
    @Column(name = "detalle_geest")
    private String detalleGeest;
    @Basic(optional = false)
    @Column(name = "activo_geest")
    private boolean activoGeest;
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
    @OneToMany(mappedBy = "ideGeest")
    private Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection;

    public GenEstados() {
    }

    public GenEstados(Integer ideGeest) {
        this.ideGeest = ideGeest;
    }

    public GenEstados(Integer ideGeest, String detalleGeest, boolean activoGeest) {
        this.ideGeest = ideGeest;
        this.detalleGeest = detalleGeest;
        this.activoGeest = activoGeest;
    }

    public Integer getIdeGeest() {
        return ideGeest;
    }

    public void setIdeGeest(Integer ideGeest) {
        this.ideGeest = ideGeest;
    }

    public String getDetalleGeest() {
        return detalleGeest;
    }

    public void setDetalleGeest(String detalleGeest) {
        this.detalleGeest = detalleGeest;
    }

    public boolean getActivoGeest() {
        return activoGeest;
    }

    public void setActivoGeest(boolean activoGeest) {
        this.activoGeest = activoGeest;
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
    public Collection<AsiPermisosVacacionHext> getAsiPermisosVacacionHextCollection() {
        return asiPermisosVacacionHextCollection;
    }

    public void setAsiPermisosVacacionHextCollection(Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection) {
        this.asiPermisosVacacionHextCollection = asiPermisosVacacionHextCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeest != null ? ideGeest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenEstados)) {
            return false;
        }
        GenEstados other = (GenEstados) object;
        if ((this.ideGeest == null && other.ideGeest != null) || (this.ideGeest != null && !this.ideGeest.equals(other.ideGeest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenEstados[ ideGeest=" + ideGeest + " ]";
    }
    
}
