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
@Table(name = "gth_experiencia_docente_emplea")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findAll", query = "SELECT g FROM GthExperienciaDocenteEmplea g"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByIdeGtxde", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.ideGtxde = :ideGtxde"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByHoraDictadaGtxde", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.horaDictadaGtxde = :horaDictadaGtxde"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByFechaInicioGtxde", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.fechaInicioGtxde = :fechaInicioGtxde"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByFechaSalidaGtxde", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.fechaSalidaGtxde = :fechaSalidaGtxde"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByActivoGtxde", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.activoGtxde = :activoGtxde"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByUsuarioIngre", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByFechaIngre", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByUsuarioActua", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByFechaActua", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByHoraIngre", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthExperienciaDocenteEmplea.findByHoraActua", query = "SELECT g FROM GthExperienciaDocenteEmplea g WHERE g.horaActua = :horaActua")})
public class GthExperienciaDocenteEmplea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtxde")
    private Integer ideGtxde;
    @Column(name = "hora_dictada_gtxde")
    private Integer horaDictadaGtxde;
    @Column(name = "fecha_inicio_gtxde")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioGtxde;
    @Column(name = "fecha_salida_gtxde")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaGtxde;
    @Column(name = "activo_gtxde")
    private Boolean activoGtxde;
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
    @JoinColumn(name = "ide_gttca", referencedColumnName = "ide_gttca")
    @ManyToOne
    private GthTipoCatedra ideGttca;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public GthExperienciaDocenteEmplea() {
    }

    public GthExperienciaDocenteEmplea(Integer ideGtxde) {
        this.ideGtxde = ideGtxde;
    }

    public Integer getIdeGtxde() {
        return ideGtxde;
    }

    public void setIdeGtxde(Integer ideGtxde) {
        this.ideGtxde = ideGtxde;
    }

    public Integer getHoraDictadaGtxde() {
        return horaDictadaGtxde;
    }

    public void setHoraDictadaGtxde(Integer horaDictadaGtxde) {
        this.horaDictadaGtxde = horaDictadaGtxde;
    }

    public Date getFechaInicioGtxde() {
        return fechaInicioGtxde;
    }

    public void setFechaInicioGtxde(Date fechaInicioGtxde) {
        this.fechaInicioGtxde = fechaInicioGtxde;
    }

    public Date getFechaSalidaGtxde() {
        return fechaSalidaGtxde;
    }

    public void setFechaSalidaGtxde(Date fechaSalidaGtxde) {
        this.fechaSalidaGtxde = fechaSalidaGtxde;
    }

    public Boolean getActivoGtxde() {
        return activoGtxde;
    }

    public void setActivoGtxde(Boolean activoGtxde) {
        this.activoGtxde = activoGtxde;
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

    public GthTipoCatedra getIdeGttca() {
        return ideGttca;
    }

    public void setIdeGttca(GthTipoCatedra ideGttca) {
        this.ideGttca = ideGttca;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtxde != null ? ideGtxde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthExperienciaDocenteEmplea)) {
            return false;
        }
        GthExperienciaDocenteEmplea other = (GthExperienciaDocenteEmplea) object;
        if ((this.ideGtxde == null && other.ideGtxde != null) || (this.ideGtxde != null && !this.ideGtxde.equals(other.ideGtxde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthExperienciaDocenteEmplea[ ideGtxde=" + ideGtxde + " ]";
    }
    
}
