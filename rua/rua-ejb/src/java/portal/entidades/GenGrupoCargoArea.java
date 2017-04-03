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
@Table(name = "gen_grupo_cargo_area")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenGrupoCargoArea.findAll", query = "SELECT g FROM GenGrupoCargoArea g"),
    @NamedQuery(name = "GenGrupoCargoArea.findByIdeGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.ideGegca = :ideGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByIdeGedep", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.ideGedep = :ideGedep"),
    @NamedQuery(name = "GenGrupoCargoArea.findByTituloCargoGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.tituloCargoGegca = :tituloCargoGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByMisionGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.misionGegca = :misionGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByDeAcuerdoaGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.deAcuerdoaGegca = :deAcuerdoaGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByConFindeGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.conFindeGegca = :conFindeGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByActivoGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.activoGegca = :activoGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByUsuarioIngre", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenGrupoCargoArea.findByFechaIngre", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenGrupoCargoArea.findByUsuarioActua", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenGrupoCargoArea.findByFechaActua", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenGrupoCargoArea.findByHoraIngre", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenGrupoCargoArea.findByHoraActua", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.horaActua = :horaActua")})
public class GenGrupoCargoArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gegca")
    private Integer ideGegca;
    @Column(name = "ide_gedep")
    private Integer ideGedep;
    @Column(name = "titulo_cargo_gegca")
    private String tituloCargoGegca;
    @Column(name = "mision_gegca")
    private String misionGegca;
    @Column(name = "de_acuerdoa_gegca")
    private String deAcuerdoaGegca;
    @Column(name = "con_finde_gegca")
    private String conFindeGegca;
    @Column(name = "activo_gegca")
    private Boolean activoGegca;
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

    public GenGrupoCargoArea() {
    }

    public GenGrupoCargoArea(Integer ideGegca) {
        this.ideGegca = ideGegca;
    }

    public Integer getIdeGegca() {
        return ideGegca;
    }

    public void setIdeGegca(Integer ideGegca) {
        this.ideGegca = ideGegca;
    }

    public Integer getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public String getTituloCargoGegca() {
        return tituloCargoGegca;
    }

    public void setTituloCargoGegca(String tituloCargoGegca) {
        this.tituloCargoGegca = tituloCargoGegca;
    }

    public String getMisionGegca() {
        return misionGegca;
    }

    public void setMisionGegca(String misionGegca) {
        this.misionGegca = misionGegca;
    }

    public String getDeAcuerdoaGegca() {
        return deAcuerdoaGegca;
    }

    public void setDeAcuerdoaGegca(String deAcuerdoaGegca) {
        this.deAcuerdoaGegca = deAcuerdoaGegca;
    }

    public String getConFindeGegca() {
        return conFindeGegca;
    }

    public void setConFindeGegca(String conFindeGegca) {
        this.conFindeGegca = conFindeGegca;
    }

    public Boolean getActivoGegca() {
        return activoGegca;
    }

    public void setActivoGegca(Boolean activoGegca) {
        this.activoGegca = activoGegca;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGegca != null ? ideGegca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGrupoCargoArea)) {
            return false;
        }
        GenGrupoCargoArea other = (GenGrupoCargoArea) object;
        if ((this.ideGegca == null && other.ideGegca != null) || (this.ideGegca != null && !this.ideGegca.equals(other.ideGegca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGrupoCargoArea[ ideGegca=" + ideGegca + " ]";
    }
    
}
