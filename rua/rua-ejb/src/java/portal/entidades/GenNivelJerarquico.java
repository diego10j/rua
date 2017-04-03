/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "gen_nivel_jerarquico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenNivelJerarquico.findAll", query = "SELECT g FROM GenNivelJerarquico g"),
    @NamedQuery(name = "GenNivelJerarquico.findByIdeGenij", query = "SELECT g FROM GenNivelJerarquico g WHERE g.ideGenij = :ideGenij"),
    @NamedQuery(name = "GenNivelJerarquico.findByDetalleGenij", query = "SELECT g FROM GenNivelJerarquico g WHERE g.detalleGenij = :detalleGenij"),
    @NamedQuery(name = "GenNivelJerarquico.findByNivelGenij", query = "SELECT g FROM GenNivelJerarquico g WHERE g.nivelGenij = :nivelGenij"),
    @NamedQuery(name = "GenNivelJerarquico.findByActivoGenij", query = "SELECT g FROM GenNivelJerarquico g WHERE g.activoGenij = :activoGenij"),
    @NamedQuery(name = "GenNivelJerarquico.findByUsuarioIngre", query = "SELECT g FROM GenNivelJerarquico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenNivelJerarquico.findByFechaIngre", query = "SELECT g FROM GenNivelJerarquico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenNivelJerarquico.findByHoraIngre", query = "SELECT g FROM GenNivelJerarquico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenNivelJerarquico.findByUsuarioActua", query = "SELECT g FROM GenNivelJerarquico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenNivelJerarquico.findByFechaActua", query = "SELECT g FROM GenNivelJerarquico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenNivelJerarquico.findByHoraActua", query = "SELECT g FROM GenNivelJerarquico g WHERE g.horaActua = :horaActua")})
public class GenNivelJerarquico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_genij")
    private Long ideGenij;
    @Basic(optional = false)
    @Column(name = "detalle_genij")
    private String detalleGenij;
    @Column(name = "nivel_genij")
    private BigInteger nivelGenij;
    @Column(name = "activo_genij")
    private Boolean activoGenij;
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
    @OneToMany(mappedBy = "ideGenij")
    private Collection<GenAreaCargoJerarquia> genAreaCargoJerarquiaCollection;

    public GenNivelJerarquico() {
    }

    public GenNivelJerarquico(Long ideGenij) {
        this.ideGenij = ideGenij;
    }

    public GenNivelJerarquico(Long ideGenij, String detalleGenij) {
        this.ideGenij = ideGenij;
        this.detalleGenij = detalleGenij;
    }

    public Long getIdeGenij() {
        return ideGenij;
    }

    public void setIdeGenij(Long ideGenij) {
        this.ideGenij = ideGenij;
    }

    public String getDetalleGenij() {
        return detalleGenij;
    }

    public void setDetalleGenij(String detalleGenij) {
        this.detalleGenij = detalleGenij;
    }

    public BigInteger getNivelGenij() {
        return nivelGenij;
    }

    public void setNivelGenij(BigInteger nivelGenij) {
        this.nivelGenij = nivelGenij;
    }

    public Boolean getActivoGenij() {
        return activoGenij;
    }

    public void setActivoGenij(Boolean activoGenij) {
        this.activoGenij = activoGenij;
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

    @XmlTransient
    public Collection<GenAreaCargoJerarquia> getGenAreaCargoJerarquiaCollection() {
        return genAreaCargoJerarquiaCollection;
    }

    public void setGenAreaCargoJerarquiaCollection(Collection<GenAreaCargoJerarquia> genAreaCargoJerarquiaCollection) {
        this.genAreaCargoJerarquiaCollection = genAreaCargoJerarquiaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGenij != null ? ideGenij.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenNivelJerarquico)) {
            return false;
        }
        GenNivelJerarquico other = (GenNivelJerarquico) object;
        if ((this.ideGenij == null && other.ideGenij != null) || (this.ideGenij != null && !this.ideGenij.equals(other.ideGenij))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenNivelJerarquico[ ideGenij=" + ideGenij + " ]";
    }
    
}
