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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "gen_partida_grupo_cargo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenPartidaGrupoCargo.findAll", query = "SELECT g FROM GenPartidaGrupoCargo g"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByIdeGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.genPartidaGrupoCargoPK.ideGepgc = :ideGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByIdeGegro", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.genPartidaGrupoCargoPK.ideGegro = :ideGegro"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByIdeGecaf", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.genPartidaGrupoCargoPK.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByIdeSucu", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.genPartidaGrupoCargoPK.ideSucu = :ideSucu"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByIdeGedep", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.genPartidaGrupoCargoPK.ideGedep = :ideGedep"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByIdeGeare", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.genPartidaGrupoCargoPK.ideGeare = :ideGeare"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByIdeGepap", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.ideGepap = :ideGepap"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByTituloCargoGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.tituloCargoGepgc = :tituloCargoGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findBySalarioEncargoGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.salarioEncargoGepgc = :salarioEncargoGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByFechaActivacionGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.fechaActivacionGepgc = :fechaActivacionGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByFechaDesactivaGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.fechaDesactivaGepgc = :fechaDesactivaGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByMotivoGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.motivoGepgc = :motivoGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByActivoGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.activoGepgc = :activoGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByUsuarioIngre", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByFechaIngre", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByUsuarioActua", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByFechaActua", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByHoraIngre", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByHoraActua", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByVacanteGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.vacanteGepgc = :vacanteGepgc"),
    @NamedQuery(name = "GenPartidaGrupoCargo.findByEncargoGepgc", query = "SELECT g FROM GenPartidaGrupoCargo g WHERE g.encargoGepgc = :encargoGepgc")})
public class GenPartidaGrupoCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GenPartidaGrupoCargoPK genPartidaGrupoCargoPK;
    @Column(name = "ide_gepap")
    private Integer ideGepap;
    @Column(name = "titulo_cargo_gepgc")
    private String tituloCargoGepgc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salario_encargo_gepgc")
    private BigDecimal salarioEncargoGepgc;
    @Column(name = "fecha_activacion_gepgc")
    @Temporal(TemporalType.DATE)
    private Date fechaActivacionGepgc;
    @Column(name = "fecha_desactiva_gepgc")
    @Temporal(TemporalType.DATE)
    private Date fechaDesactivaGepgc;
    @Column(name = "motivo_gepgc")
    private String motivoGepgc;
    @Basic(optional = false)
    @Column(name = "activo_gepgc")
    private boolean activoGepgc;
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
    @Column(name = "vacante_gepgc")
    private Boolean vacanteGepgc;
    @Column(name = "encargo_gepgc")
    private Boolean encargoGepgc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genPartidaGrupoCargo")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;
    @JoinColumn(name = "ide_gttem", referencedColumnName = "ide_gttem")
    @ManyToOne(optional = false)
    private GthTipoEmpleado ideGttem;
    @JoinColumns({
        @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro", insertable = false, updatable = false),
        @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private GenGrupoCargo genGrupoCargo;
    @JoinColumns({
        @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu", insertable = false, updatable = false),
        @JoinColumn(name = "ide_gedep", referencedColumnName = "ide_gedep", insertable = false, updatable = false),
        @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private GenDepartamentoSucursal genDepartamentoSucursal;

    public GenPartidaGrupoCargo() {
    }

    public GenPartidaGrupoCargo(GenPartidaGrupoCargoPK genPartidaGrupoCargoPK) {
        this.genPartidaGrupoCargoPK = genPartidaGrupoCargoPK;
    }

    public GenPartidaGrupoCargo(GenPartidaGrupoCargoPK genPartidaGrupoCargoPK, boolean activoGepgc) {
        this.genPartidaGrupoCargoPK = genPartidaGrupoCargoPK;
        this.activoGepgc = activoGepgc;
    }

    public GenPartidaGrupoCargo(int ideGepgc, int ideGegro, int ideGecaf, int ideSucu, int ideGedep, int ideGeare) {
        this.genPartidaGrupoCargoPK = new GenPartidaGrupoCargoPK(ideGepgc, ideGegro, ideGecaf, ideSucu, ideGedep, ideGeare);
    }

    public GenPartidaGrupoCargoPK getGenPartidaGrupoCargoPK() {
        return genPartidaGrupoCargoPK;
    }

    public void setGenPartidaGrupoCargoPK(GenPartidaGrupoCargoPK genPartidaGrupoCargoPK) {
        this.genPartidaGrupoCargoPK = genPartidaGrupoCargoPK;
    }

    public Integer getIdeGepap() {
        return ideGepap;
    }

    public void setIdeGepap(Integer ideGepap) {
        this.ideGepap = ideGepap;
    }

    public String getTituloCargoGepgc() {
        return tituloCargoGepgc;
    }

    public void setTituloCargoGepgc(String tituloCargoGepgc) {
        this.tituloCargoGepgc = tituloCargoGepgc;
    }

    public BigDecimal getSalarioEncargoGepgc() {
        return salarioEncargoGepgc;
    }

    public void setSalarioEncargoGepgc(BigDecimal salarioEncargoGepgc) {
        this.salarioEncargoGepgc = salarioEncargoGepgc;
    }

    public Date getFechaActivacionGepgc() {
        return fechaActivacionGepgc;
    }

    public void setFechaActivacionGepgc(Date fechaActivacionGepgc) {
        this.fechaActivacionGepgc = fechaActivacionGepgc;
    }

    public Date getFechaDesactivaGepgc() {
        return fechaDesactivaGepgc;
    }

    public void setFechaDesactivaGepgc(Date fechaDesactivaGepgc) {
        this.fechaDesactivaGepgc = fechaDesactivaGepgc;
    }

    public String getMotivoGepgc() {
        return motivoGepgc;
    }

    public void setMotivoGepgc(String motivoGepgc) {
        this.motivoGepgc = motivoGepgc;
    }

    public boolean getActivoGepgc() {
        return activoGepgc;
    }

    public void setActivoGepgc(boolean activoGepgc) {
        this.activoGepgc = activoGepgc;
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

    public Boolean getVacanteGepgc() {
        return vacanteGepgc;
    }

    public void setVacanteGepgc(Boolean vacanteGepgc) {
        this.vacanteGepgc = vacanteGepgc;
    }

    public Boolean getEncargoGepgc() {
        return encargoGepgc;
    }

    public void setEncargoGepgc(Boolean encargoGepgc) {
        this.encargoGepgc = encargoGepgc;
    }

    @XmlTransient
    public Collection<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParCollection() {
        return genEmpleadosDepartamentoParCollection;
    }

    public void setGenEmpleadosDepartamentoParCollection(Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection) {
        this.genEmpleadosDepartamentoParCollection = genEmpleadosDepartamentoParCollection;
    }

    public GthTipoEmpleado getIdeGttem() {
        return ideGttem;
    }

    public void setIdeGttem(GthTipoEmpleado ideGttem) {
        this.ideGttem = ideGttem;
    }

    public GenGrupoCargo getGenGrupoCargo() {
        return genGrupoCargo;
    }

    public void setGenGrupoCargo(GenGrupoCargo genGrupoCargo) {
        this.genGrupoCargo = genGrupoCargo;
    }

    public GenDepartamentoSucursal getGenDepartamentoSucursal() {
        return genDepartamentoSucursal;
    }

    public void setGenDepartamentoSucursal(GenDepartamentoSucursal genDepartamentoSucursal) {
        this.genDepartamentoSucursal = genDepartamentoSucursal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genPartidaGrupoCargoPK != null ? genPartidaGrupoCargoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPartidaGrupoCargo)) {
            return false;
        }
        GenPartidaGrupoCargo other = (GenPartidaGrupoCargo) object;
        if ((this.genPartidaGrupoCargoPK == null && other.genPartidaGrupoCargoPK != null) || (this.genPartidaGrupoCargoPK != null && !this.genPartidaGrupoCargoPK.equals(other.genPartidaGrupoCargoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPartidaGrupoCargo[ genPartidaGrupoCargoPK=" + genPartidaGrupoCargoPK + " ]";
    }
    
}
