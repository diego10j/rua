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
@Table(name = "gth_descripcion_valoracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthDescripcionValoracion.findAll", query = "SELECT g FROM GthDescripcionValoracion g"),
    @NamedQuery(name = "GthDescripcionValoracion.findByIdeGtdva", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.ideGtdva = :ideGtdva"),
    @NamedQuery(name = "GthDescripcionValoracion.findByDenominacionPuestoGtdva", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.denominacionPuestoGtdva = :denominacionPuestoGtdva"),
    @NamedQuery(name = "GthDescripcionValoracion.findByActivoGtdva", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.activoGtdva = :activoGtdva"),
    @NamedQuery(name = "GthDescripcionValoracion.findByUsuarioIngre", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthDescripcionValoracion.findByFechaIngre", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthDescripcionValoracion.findByHoraIngre", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthDescripcionValoracion.findByUsuarioActua", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthDescripcionValoracion.findByFechaActua", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthDescripcionValoracion.findByHoraActua", query = "SELECT g FROM GthDescripcionValoracion g WHERE g.horaActua = :horaActua")})
public class GthDescripcionValoracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gtdva")
    private Long ideGtdva;
    @Column(name = "denominacion_puesto_gtdva")
    private String denominacionPuestoGtdva;
    @Column(name = "activo_gtdva")
    private Boolean activoGtdva;
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
    @JoinColumn(name = "ide_gtdev", referencedColumnName = "ide_gtdev")
    @ManyToOne
    private GthDetalleValoracion ideGtdev;
    @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")
    @ManyToOne
    private GenArea ideGeare;

    public GthDescripcionValoracion() {
    }

    public GthDescripcionValoracion(Long ideGtdva) {
        this.ideGtdva = ideGtdva;
    }

    public Long getIdeGtdva() {
        return ideGtdva;
    }

    public void setIdeGtdva(Long ideGtdva) {
        this.ideGtdva = ideGtdva;
    }

    public String getDenominacionPuestoGtdva() {
        return denominacionPuestoGtdva;
    }

    public void setDenominacionPuestoGtdva(String denominacionPuestoGtdva) {
        this.denominacionPuestoGtdva = denominacionPuestoGtdva;
    }

    public Boolean getActivoGtdva() {
        return activoGtdva;
    }

    public void setActivoGtdva(Boolean activoGtdva) {
        this.activoGtdva = activoGtdva;
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

    public GthDetalleValoracion getIdeGtdev() {
        return ideGtdev;
    }

    public void setIdeGtdev(GthDetalleValoracion ideGtdev) {
        this.ideGtdev = ideGtdev;
    }

    public GenArea getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(GenArea ideGeare) {
        this.ideGeare = ideGeare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtdva != null ? ideGtdva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthDescripcionValoracion)) {
            return false;
        }
        GthDescripcionValoracion other = (GthDescripcionValoracion) object;
        if ((this.ideGtdva == null && other.ideGtdva != null) || (this.ideGtdva != null && !this.ideGtdva.equals(other.ideGtdva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthDescripcionValoracion[ ideGtdva=" + ideGtdva + " ]";
    }
    
}
