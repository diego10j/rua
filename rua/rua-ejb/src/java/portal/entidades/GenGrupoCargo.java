/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "gen_grupo_cargo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenGrupoCargo.findAll", query = "SELECT g FROM GenGrupoCargo g"),
    @NamedQuery(name = "GenGrupoCargo.findByIdeGegro", query = "SELECT g FROM GenGrupoCargo g WHERE g.genGrupoCargoPK.ideGegro = :ideGegro"),
    @NamedQuery(name = "GenGrupoCargo.findByIdeGecaf", query = "SELECT g FROM GenGrupoCargo g WHERE g.genGrupoCargoPK.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "GenGrupoCargo.findByRmuGegrc", query = "SELECT g FROM GenGrupoCargo g WHERE g.rmuGegrc = :rmuGegrc"),
    @NamedQuery(name = "GenGrupoCargo.findByActivoGegrc", query = "SELECT g FROM GenGrupoCargo g WHERE g.activoGegrc = :activoGegrc"),
    @NamedQuery(name = "GenGrupoCargo.findByUsuarioIngre", query = "SELECT g FROM GenGrupoCargo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenGrupoCargo.findByFechaIngre", query = "SELECT g FROM GenGrupoCargo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenGrupoCargo.findByUsuarioActua", query = "SELECT g FROM GenGrupoCargo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenGrupoCargo.findByFechaActua", query = "SELECT g FROM GenGrupoCargo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenGrupoCargo.findByHoraIngre", query = "SELECT g FROM GenGrupoCargo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenGrupoCargo.findByHoraActua", query = "SELECT g FROM GenGrupoCargo g WHERE g.horaActua = :horaActua")})
public class GenGrupoCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GenGrupoCargoPK genGrupoCargoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rmu_gegrc")
    private BigDecimal rmuGegrc;
    @Column(name = "activo_gegrc")
    private Boolean activoGegrc;
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
    @OneToMany(mappedBy = "genGrupoCargo")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genGrupoCargo")
    private Collection<GenPartidaGrupoCargo> genPartidaGrupoCargoCollection;
    @OneToMany(mappedBy = "genGrupoCargo")
    private Collection<GenGrupoCargoSupervisa> genGrupoCargoSupervisaCollection;
    @OneToMany(mappedBy = "genGrupoCargo1")
    private Collection<GenGrupoCargoSupervisa> genGrupoCargoSupervisaCollection1;
    @OneToMany(mappedBy = "genGrupoCargo")
    private Collection<GenGrupoCargoArea> genGrupoCargoAreaCollection;

    public GenGrupoCargo() {
    }

    public GenGrupoCargo(GenGrupoCargoPK genGrupoCargoPK) {
        this.genGrupoCargoPK = genGrupoCargoPK;
    }

    public GenGrupoCargo(int ideGegro, int ideGecaf) {
        this.genGrupoCargoPK = new GenGrupoCargoPK(ideGegro, ideGecaf);
    }

    public GenGrupoCargoPK getGenGrupoCargoPK() {
        return genGrupoCargoPK;
    }

    public void setGenGrupoCargoPK(GenGrupoCargoPK genGrupoCargoPK) {
        this.genGrupoCargoPK = genGrupoCargoPK;
    }

    public BigDecimal getRmuGegrc() {
        return rmuGegrc;
    }

    public void setRmuGegrc(BigDecimal rmuGegrc) {
        this.rmuGegrc = rmuGegrc;
    }

    public Boolean getActivoGegrc() {
        return activoGegrc;
    }

    public void setActivoGegrc(Boolean activoGegrc) {
        this.activoGegrc = activoGegrc;
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

    @XmlTransient
    public Collection<GenPartidaGrupoCargo> getGenPartidaGrupoCargoCollection() {
        return genPartidaGrupoCargoCollection;
    }

    public void setGenPartidaGrupoCargoCollection(Collection<GenPartidaGrupoCargo> genPartidaGrupoCargoCollection) {
        this.genPartidaGrupoCargoCollection = genPartidaGrupoCargoCollection;
    }

    @XmlTransient
    public Collection<GenGrupoCargoSupervisa> getGenGrupoCargoSupervisaCollection() {
        return genGrupoCargoSupervisaCollection;
    }

    public void setGenGrupoCargoSupervisaCollection(Collection<GenGrupoCargoSupervisa> genGrupoCargoSupervisaCollection) {
        this.genGrupoCargoSupervisaCollection = genGrupoCargoSupervisaCollection;
    }

    @XmlTransient
    public Collection<GenGrupoCargoSupervisa> getGenGrupoCargoSupervisaCollection1() {
        return genGrupoCargoSupervisaCollection1;
    }

    public void setGenGrupoCargoSupervisaCollection1(Collection<GenGrupoCargoSupervisa> genGrupoCargoSupervisaCollection1) {
        this.genGrupoCargoSupervisaCollection1 = genGrupoCargoSupervisaCollection1;
    }

    @XmlTransient
    public Collection<GenGrupoCargoArea> getGenGrupoCargoAreaCollection() {
        return genGrupoCargoAreaCollection;
    }

    public void setGenGrupoCargoAreaCollection(Collection<GenGrupoCargoArea> genGrupoCargoAreaCollection) {
        this.genGrupoCargoAreaCollection = genGrupoCargoAreaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genGrupoCargoPK != null ? genGrupoCargoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGrupoCargo)) {
            return false;
        }
        GenGrupoCargo other = (GenGrupoCargo) object;
        if ((this.genGrupoCargoPK == null && other.genGrupoCargoPK != null) || (this.genGrupoCargoPK != null && !this.genGrupoCargoPK.equals(other.genGrupoCargoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGrupoCargo[ genGrupoCargoPK=" + genGrupoCargoPK + " ]";
    }
    
}
