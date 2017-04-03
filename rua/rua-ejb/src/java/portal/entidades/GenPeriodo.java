/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "gen_periodo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenPeriodo.findAll", query = "SELECT g FROM GenPeriodo g"),
    @NamedQuery(name = "GenPeriodo.findByIdeGemes", query = "SELECT g FROM GenPeriodo g WHERE g.genPeriodoPK.ideGemes = :ideGemes"),
    @NamedQuery(name = "GenPeriodo.findByIdeGeani", query = "SELECT g FROM GenPeriodo g WHERE g.genPeriodoPK.ideGeani = :ideGeani"),
    @NamedQuery(name = "GenPeriodo.findByActivoGeper", query = "SELECT g FROM GenPeriodo g WHERE g.activoGeper = :activoGeper"),
    @NamedQuery(name = "GenPeriodo.findByUsuarioIngre", query = "SELECT g FROM GenPeriodo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenPeriodo.findByFechaIngre", query = "SELECT g FROM GenPeriodo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenPeriodo.findByUsuarioActua", query = "SELECT g FROM GenPeriodo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenPeriodo.findByFechaActua", query = "SELECT g FROM GenPeriodo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenPeriodo.findByHoraIngre", query = "SELECT g FROM GenPeriodo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenPeriodo.findByHoraActua", query = "SELECT g FROM GenPeriodo g WHERE g.horaActua = :horaActua")})
public class GenPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GenPeriodoPK genPeriodoPK;
    @Column(name = "activo_geper")
    private Boolean activoGeper;
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
    @OneToMany(mappedBy = "genPeriodo")
    private Collection<AsiPermisosVacacionHext> asiPermisosVacacionHextCollection;
    @OneToMany(mappedBy = "genPeriodo")
    private Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection;
    @OneToMany(mappedBy = "genPeriodo")
    private Collection<GenPeridoRol> genPeridoRolCollection;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GenAnio genAnio;

    public GenPeriodo() {
    }

    public GenPeriodo(GenPeriodoPK genPeriodoPK) {
        this.genPeriodoPK = genPeriodoPK;
    }

    public GenPeriodo(int ideGemes, int ideGeani) {
        this.genPeriodoPK = new GenPeriodoPK(ideGemes, ideGeani);
    }

    public GenPeriodoPK getGenPeriodoPK() {
        return genPeriodoPK;
    }

    public void setGenPeriodoPK(GenPeriodoPK genPeriodoPK) {
        this.genPeriodoPK = genPeriodoPK;
    }

    public Boolean getActivoGeper() {
        return activoGeper;
    }

    public void setActivoGeper(Boolean activoGeper) {
        this.activoGeper = activoGeper;
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

    @XmlTransient
    public Collection<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaCollection() {
        return nrhDetalleFacturaGuarderiaCollection;
    }

    public void setNrhDetalleFacturaGuarderiaCollection(Collection<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaCollection) {
        this.nrhDetalleFacturaGuarderiaCollection = nrhDetalleFacturaGuarderiaCollection;
    }

    @XmlTransient
    public Collection<GenPeridoRol> getGenPeridoRolCollection() {
        return genPeridoRolCollection;
    }

    public void setGenPeridoRolCollection(Collection<GenPeridoRol> genPeridoRolCollection) {
        this.genPeridoRolCollection = genPeridoRolCollection;
    }

    public GenAnio getGenAnio() {
        return genAnio;
    }

    public void setGenAnio(GenAnio genAnio) {
        this.genAnio = genAnio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genPeriodoPK != null ? genPeriodoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPeriodo)) {
            return false;
        }
        GenPeriodo other = (GenPeriodo) object;
        if ((this.genPeriodoPK == null && other.genPeriodoPK != null) || (this.genPeriodoPK != null && !this.genPeriodoPK.equals(other.genPeriodoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPeriodo[ genPeriodoPK=" + genPeriodoPK + " ]";
    }
    
}
