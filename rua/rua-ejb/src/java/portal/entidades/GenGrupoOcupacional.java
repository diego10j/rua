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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "gen_grupo_ocupacional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenGrupoOcupacional.findAll", query = "SELECT g FROM GenGrupoOcupacional g"),
    @NamedQuery(name = "GenGrupoOcupacional.findByIdeGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.ideGegro = :ideGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findByDetalleGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.detalleGegro = :detalleGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findBySiglasGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.siglasGegro = :siglasGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findByActivoGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.activoGegro = :activoGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findByUsuarioIngre", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenGrupoOcupacional.findByFechaIngre", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenGrupoOcupacional.findByUsuarioActua", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenGrupoOcupacional.findByFechaActua", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenGrupoOcupacional.findByHoraIngre", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenGrupoOcupacional.findByHoraActua", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenGrupoOcupacional.findBySubrogacionGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.subrogacionGegro = :subrogacionGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findByRmuGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.rmuGegro = :rmuGegro")})
public class GenGrupoOcupacional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gegro")
    private Integer ideGegro;
    @Column(name = "detalle_gegro")
    private String detalleGegro;
    @Column(name = "siglas_gegro")
    private String siglasGegro;
    @Column(name = "activo_gegro")
    private Boolean activoGegro;
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
    @Column(name = "subrogacion_gegro")
    private Boolean subrogacionGegro;
    @Column(name = "rmu_gegro")
    private BigInteger rmuGegro;
    @JoinColumn(name = "ide_gtniv", referencedColumnName = "ide_gtniv")
    @ManyToOne
    private GthNivelViatico ideGtniv;
    @OneToMany(mappedBy = "ideGegro")
    private Collection<GthFichaValoracion> gthFichaValoracionCollection;
    @OneToMany(mappedBy = "ideGegro")
    private Collection<GthGrupoValora> gthGrupoValoraCollection;

    public GenGrupoOcupacional() {
    }

    public GenGrupoOcupacional(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public Integer getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public String getDetalleGegro() {
        return detalleGegro;
    }

    public void setDetalleGegro(String detalleGegro) {
        this.detalleGegro = detalleGegro;
    }

    public String getSiglasGegro() {
        return siglasGegro;
    }

    public void setSiglasGegro(String siglasGegro) {
        this.siglasGegro = siglasGegro;
    }

    public Boolean getActivoGegro() {
        return activoGegro;
    }

    public void setActivoGegro(Boolean activoGegro) {
        this.activoGegro = activoGegro;
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

    public Boolean getSubrogacionGegro() {
        return subrogacionGegro;
    }

    public void setSubrogacionGegro(Boolean subrogacionGegro) {
        this.subrogacionGegro = subrogacionGegro;
    }

    public BigInteger getRmuGegro() {
        return rmuGegro;
    }

    public void setRmuGegro(BigInteger rmuGegro) {
        this.rmuGegro = rmuGegro;
    }

    public GthNivelViatico getIdeGtniv() {
        return ideGtniv;
    }

    public void setIdeGtniv(GthNivelViatico ideGtniv) {
        this.ideGtniv = ideGtniv;
    }

    @XmlTransient
    public Collection<GthFichaValoracion> getGthFichaValoracionCollection() {
        return gthFichaValoracionCollection;
    }

    public void setGthFichaValoracionCollection(Collection<GthFichaValoracion> gthFichaValoracionCollection) {
        this.gthFichaValoracionCollection = gthFichaValoracionCollection;
    }

    @XmlTransient
    public Collection<GthGrupoValora> getGthGrupoValoraCollection() {
        return gthGrupoValoraCollection;
    }

    public void setGthGrupoValoraCollection(Collection<GthGrupoValora> gthGrupoValoraCollection) {
        this.gthGrupoValoraCollection = gthGrupoValoraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGegro != null ? ideGegro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGrupoOcupacional)) {
            return false;
        }
        GenGrupoOcupacional other = (GenGrupoOcupacional) object;
        if ((this.ideGegro == null && other.ideGegro != null) || (this.ideGegro != null && !this.ideGegro.equals(other.ideGegro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGrupoOcupacional[ ideGegro=" + ideGegro + " ]";
    }
    
}
