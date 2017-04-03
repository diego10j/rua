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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "gen_perido_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenPeridoRol.findAll", query = "SELECT g FROM GenPeridoRol g"),
    @NamedQuery(name = "GenPeridoRol.findByIdeGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.ideGepro = :ideGepro"),
    @NamedQuery(name = "GenPeridoRol.findByFechaInicialGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaInicialGepro = :fechaInicialGepro"),
    @NamedQuery(name = "GenPeridoRol.findByFechaFinalGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaFinalGepro = :fechaFinalGepro"),
    @NamedQuery(name = "GenPeridoRol.findByDetallePeriodoGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.detallePeriodoGepro = :detallePeriodoGepro"),
    @NamedQuery(name = "GenPeridoRol.findByUsuarioIngre", query = "SELECT g FROM GenPeridoRol g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenPeridoRol.findByFechaIngre", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenPeridoRol.findByUsuarioActua", query = "SELECT g FROM GenPeridoRol g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenPeridoRol.findByFechaActua", query = "SELECT g FROM GenPeridoRol g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenPeridoRol.findByHoraIngre", query = "SELECT g FROM GenPeridoRol g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenPeridoRol.findByHoraActua", query = "SELECT g FROM GenPeridoRol g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenPeridoRol.findByActivoGepro", query = "SELECT g FROM GenPeridoRol g WHERE g.activoGepro = :activoGepro")})
public class GenPeridoRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gepro")
    private Integer ideGepro;
    @Column(name = "fecha_inicial_gepro")
    @Temporal(TemporalType.DATE)
    private Date fechaInicialGepro;
    @Column(name = "fecha_final_gepro")
    @Temporal(TemporalType.DATE)
    private Date fechaFinalGepro;
    @Column(name = "detalle_periodo_gepro")
    private String detallePeriodoGepro;
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
    @Column(name = "activo_gepro")
    private Boolean activoGepro;
    @JoinColumn(name = "ide_nrtit", referencedColumnName = "ide_nrtit")
    @ManyToOne
    private NrhTipoRol ideNrtit;
    @JoinColumns({
        @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes"),
        @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")})
    @ManyToOne
    private GenPeriodo genPeriodo;
    @OneToMany(mappedBy = "genIdeGepro")
    private Collection<GenPeridoRol> genPeridoRolCollection;
    @JoinColumn(name = "gen_ide_gepro", referencedColumnName = "ide_gepro")
    @ManyToOne
    private GenPeridoRol genIdeGepro;
    @OneToMany(mappedBy = "ideGepro")
    private Collection<NrhCabeceraAsiento> nrhCabeceraAsientoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGepro")
    private Collection<NrhRol> nrhRolCollection;

    public GenPeridoRol() {
    }

    public GenPeridoRol(Integer ideGepro) {
        this.ideGepro = ideGepro;
    }

    public Integer getIdeGepro() {
        return ideGepro;
    }

    public void setIdeGepro(Integer ideGepro) {
        this.ideGepro = ideGepro;
    }

    public Date getFechaInicialGepro() {
        return fechaInicialGepro;
    }

    public void setFechaInicialGepro(Date fechaInicialGepro) {
        this.fechaInicialGepro = fechaInicialGepro;
    }

    public Date getFechaFinalGepro() {
        return fechaFinalGepro;
    }

    public void setFechaFinalGepro(Date fechaFinalGepro) {
        this.fechaFinalGepro = fechaFinalGepro;
    }

    public String getDetallePeriodoGepro() {
        return detallePeriodoGepro;
    }

    public void setDetallePeriodoGepro(String detallePeriodoGepro) {
        this.detallePeriodoGepro = detallePeriodoGepro;
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

    public Boolean getActivoGepro() {
        return activoGepro;
    }

    public void setActivoGepro(Boolean activoGepro) {
        this.activoGepro = activoGepro;
    }

    public NrhTipoRol getIdeNrtit() {
        return ideNrtit;
    }

    public void setIdeNrtit(NrhTipoRol ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public GenPeriodo getGenPeriodo() {
        return genPeriodo;
    }

    public void setGenPeriodo(GenPeriodo genPeriodo) {
        this.genPeriodo = genPeriodo;
    }

    @XmlTransient
    public Collection<GenPeridoRol> getGenPeridoRolCollection() {
        return genPeridoRolCollection;
    }

    public void setGenPeridoRolCollection(Collection<GenPeridoRol> genPeridoRolCollection) {
        this.genPeridoRolCollection = genPeridoRolCollection;
    }

    public GenPeridoRol getGenIdeGepro() {
        return genIdeGepro;
    }

    public void setGenIdeGepro(GenPeridoRol genIdeGepro) {
        this.genIdeGepro = genIdeGepro;
    }

    @XmlTransient
    public Collection<NrhCabeceraAsiento> getNrhCabeceraAsientoCollection() {
        return nrhCabeceraAsientoCollection;
    }

    public void setNrhCabeceraAsientoCollection(Collection<NrhCabeceraAsiento> nrhCabeceraAsientoCollection) {
        this.nrhCabeceraAsientoCollection = nrhCabeceraAsientoCollection;
    }

    @XmlTransient
    public Collection<NrhRol> getNrhRolCollection() {
        return nrhRolCollection;
    }

    public void setNrhRolCollection(Collection<NrhRol> nrhRolCollection) {
        this.nrhRolCollection = nrhRolCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGepro != null ? ideGepro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenPeridoRol)) {
            return false;
        }
        GenPeridoRol other = (GenPeridoRol) object;
        if ((this.ideGepro == null && other.ideGepro != null) || (this.ideGepro != null && !this.ideGepro.equals(other.ideGepro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenPeridoRol[ ideGepro=" + ideGepro + " ]";
    }
    
}
