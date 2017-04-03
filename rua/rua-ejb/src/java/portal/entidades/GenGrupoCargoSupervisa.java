/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "gen_grupo_cargo_supervisa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenGrupoCargoSupervisa.findAll", query = "SELECT g FROM GenGrupoCargoSupervisa g"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByIdeGegcs", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.ideGegcs = :ideGegcs"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByActivoGegcs", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.activoGegcs = :activoGegcs"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByUsuarioIngre", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByFechaIngre", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByUsuarioActua", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByFechaActua", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByHoraIngre", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenGrupoCargoSupervisa.findByHoraActua", query = "SELECT g FROM GenGrupoCargoSupervisa g WHERE g.horaActua = :horaActua")})
public class GenGrupoCargoSupervisa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gegcs")
    private Integer ideGegcs;
    @Column(name = "activo_gegcs")
    private Boolean activoGegcs;
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
    @JoinColumns({
        @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf")})
    @ManyToOne
    private GenGrupoCargo genGrupoCargo;
    @JoinColumns({
        @JoinColumn(name = "gen_ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "gen_ide_gecaf", referencedColumnName = "ide_gecaf")})
    @ManyToOne
    private GenGrupoCargo genGrupoCargo1;

    public GenGrupoCargoSupervisa() {
    }

    public GenGrupoCargoSupervisa(Integer ideGegcs) {
        this.ideGegcs = ideGegcs;
    }

    public Integer getIdeGegcs() {
        return ideGegcs;
    }

    public void setIdeGegcs(Integer ideGegcs) {
        this.ideGegcs = ideGegcs;
    }

    public Boolean getActivoGegcs() {
        return activoGegcs;
    }

    public void setActivoGegcs(Boolean activoGegcs) {
        this.activoGegcs = activoGegcs;
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

    public GenGrupoCargo getGenGrupoCargo() {
        return genGrupoCargo;
    }

    public void setGenGrupoCargo(GenGrupoCargo genGrupoCargo) {
        this.genGrupoCargo = genGrupoCargo;
    }

    public GenGrupoCargo getGenGrupoCargo1() {
        return genGrupoCargo1;
    }

    public void setGenGrupoCargo1(GenGrupoCargo genGrupoCargo1) {
        this.genGrupoCargo1 = genGrupoCargo1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGegcs != null ? ideGegcs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGrupoCargoSupervisa)) {
            return false;
        }
        GenGrupoCargoSupervisa other = (GenGrupoCargoSupervisa) object;
        if ((this.ideGegcs == null && other.ideGegcs != null) || (this.ideGegcs != null && !this.ideGegcs.equals(other.ideGegcs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGrupoCargoSupervisa[ ideGegcs=" + ideGegcs + " ]";
    }
    
}
