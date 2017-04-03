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
@Table(name = "gth_educacion_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthEducacionEmpleado.findAll", query = "SELECT g FROM GthEducacionEmpleado g"),
    @NamedQuery(name = "GthEducacionEmpleado.findByIdeGtede", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.ideGtede = :ideGtede"),
    @NamedQuery(name = "GthEducacionEmpleado.findByAnioGtede", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.anioGtede = :anioGtede"),
    @NamedQuery(name = "GthEducacionEmpleado.findByAnioGradoGtede", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.anioGradoGtede = :anioGradoGtede"),
    @NamedQuery(name = "GthEducacionEmpleado.findByActivoGtede", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.activoGtede = :activoGtede"),
    @NamedQuery(name = "GthEducacionEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthEducacionEmpleado.findByFechaIngre", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthEducacionEmpleado.findByUsuarioActua", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthEducacionEmpleado.findByFechaActua", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthEducacionEmpleado.findByHoraIngre", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthEducacionEmpleado.findByHoraActua", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthEducacionEmpleado.findByRegistroTituloGtede", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.registroTituloGtede = :registroTituloGtede"),
    @NamedQuery(name = "GthEducacionEmpleado.findByObservacionesGtede", query = "SELECT g FROM GthEducacionEmpleado g WHERE g.observacionesGtede = :observacionesGtede")})
public class GthEducacionEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtede")
    private Integer ideGtede;
    @Column(name = "anio_gtede")
    private Integer anioGtede;
    @Column(name = "anio_grado_gtede")
    private Integer anioGradoGtede;
    @Column(name = "activo_gtede")
    private Boolean activoGtede;
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
    @Column(name = "registro_titulo_gtede")
    private Boolean registroTituloGtede;
    @Column(name = "observaciones_gtede")
    private String observacionesGtede;
    @JoinColumn(name = "ide_gtttp", referencedColumnName = "ide_gtttp")
    @ManyToOne
    private GthTipoTituloProfesional ideGtttp;
    @JoinColumn(name = "ide_gttes", referencedColumnName = "ide_gttes")
    @ManyToOne
    private GthTipoEspecialidad ideGttes;
    @JoinColumn(name = "ide_gtted", referencedColumnName = "ide_gtted")
    @ManyToOne
    private GthTipoEducacion ideGtted;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gtana", referencedColumnName = "ide_gtana")
    @ManyToOne
    private GthAnioAprobado ideGtana;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public GthEducacionEmpleado() {
    }

    public GthEducacionEmpleado(Integer ideGtede) {
        this.ideGtede = ideGtede;
    }

    public Integer getIdeGtede() {
        return ideGtede;
    }

    public void setIdeGtede(Integer ideGtede) {
        this.ideGtede = ideGtede;
    }

    public Integer getAnioGtede() {
        return anioGtede;
    }

    public void setAnioGtede(Integer anioGtede) {
        this.anioGtede = anioGtede;
    }

    public Integer getAnioGradoGtede() {
        return anioGradoGtede;
    }

    public void setAnioGradoGtede(Integer anioGradoGtede) {
        this.anioGradoGtede = anioGradoGtede;
    }

    public Boolean getActivoGtede() {
        return activoGtede;
    }

    public void setActivoGtede(Boolean activoGtede) {
        this.activoGtede = activoGtede;
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

    public Boolean getRegistroTituloGtede() {
        return registroTituloGtede;
    }

    public void setRegistroTituloGtede(Boolean registroTituloGtede) {
        this.registroTituloGtede = registroTituloGtede;
    }

    public String getObservacionesGtede() {
        return observacionesGtede;
    }

    public void setObservacionesGtede(String observacionesGtede) {
        this.observacionesGtede = observacionesGtede;
    }

    public GthTipoTituloProfesional getIdeGtttp() {
        return ideGtttp;
    }

    public void setIdeGtttp(GthTipoTituloProfesional ideGtttp) {
        this.ideGtttp = ideGtttp;
    }

    public GthTipoEspecialidad getIdeGttes() {
        return ideGttes;
    }

    public void setIdeGttes(GthTipoEspecialidad ideGttes) {
        this.ideGttes = ideGttes;
    }

    public GthTipoEducacion getIdeGtted() {
        return ideGtted;
    }

    public void setIdeGtted(GthTipoEducacion ideGtted) {
        this.ideGtted = ideGtted;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthAnioAprobado getIdeGtana() {
        return ideGtana;
    }

    public void setIdeGtana(GthAnioAprobado ideGtana) {
        this.ideGtana = ideGtana;
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
        hash += (ideGtede != null ? ideGtede.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthEducacionEmpleado)) {
            return false;
        }
        GthEducacionEmpleado other = (GthEducacionEmpleado) object;
        if ((this.ideGtede == null && other.ideGtede != null) || (this.ideGtede != null && !this.ideGtede.equals(other.ideGtede))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthEducacionEmpleado[ ideGtede=" + ideGtede + " ]";
    }
    
}
