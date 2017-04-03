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
@Table(name = "gen_tipo_vinculacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenTipoVinculacion.findAll", query = "SELECT g FROM GenTipoVinculacion g"),
    @NamedQuery(name = "GenTipoVinculacion.findByIdeGetiv", query = "SELECT g FROM GenTipoVinculacion g WHERE g.ideGetiv = :ideGetiv"),
    @NamedQuery(name = "GenTipoVinculacion.findByDetalleGetiv", query = "SELECT g FROM GenTipoVinculacion g WHERE g.detalleGetiv = :detalleGetiv"),
    @NamedQuery(name = "GenTipoVinculacion.findByUsuarioIngre", query = "SELECT g FROM GenTipoVinculacion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoVinculacion.findByFechaIngre", query = "SELECT g FROM GenTipoVinculacion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoVinculacion.findByUsuarioActua", query = "SELECT g FROM GenTipoVinculacion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoVinculacion.findByFechaActua", query = "SELECT g FROM GenTipoVinculacion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoVinculacion.findByHoraIngre", query = "SELECT g FROM GenTipoVinculacion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoVinculacion.findByHoraActua", query = "SELECT g FROM GenTipoVinculacion g WHERE g.horaActua = :horaActua")})
public class GenTipoVinculacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_getiv")
    private Integer ideGetiv;
    @Column(name = "detalle_getiv")
    private String detalleGetiv;
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
    @OneToMany(mappedBy = "ideGetiv")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;

    public GenTipoVinculacion() {
    }

    public GenTipoVinculacion(Integer ideGetiv) {
        this.ideGetiv = ideGetiv;
    }

    public Integer getIdeGetiv() {
        return ideGetiv;
    }

    public void setIdeGetiv(Integer ideGetiv) {
        this.ideGetiv = ideGetiv;
    }

    public String getDetalleGetiv() {
        return detalleGetiv;
    }

    public void setDetalleGetiv(String detalleGetiv) {
        this.detalleGetiv = detalleGetiv;
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
        hash += (ideGetiv != null ? ideGetiv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoVinculacion)) {
            return false;
        }
        GenTipoVinculacion other = (GenTipoVinculacion) object;
        if ((this.ideGetiv == null && other.ideGetiv != null) || (this.ideGetiv != null && !this.ideGetiv.equals(other.ideGetiv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoVinculacion[ ideGetiv=" + ideGetiv + " ]";
    }
    
}
