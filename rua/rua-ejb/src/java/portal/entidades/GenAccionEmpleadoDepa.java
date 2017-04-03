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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "gen_accion_empleado_depa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenAccionEmpleadoDepa.findAll", query = "SELECT g FROM GenAccionEmpleadoDepa g"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByIdeGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.ideGeaed = :ideGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByDetalleGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.detalleGeaed = :detalleGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByActivoGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.activoGeaed = :activoGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByUsuarioIngre", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByFechaIngre", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByUsuarioActua", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByFechaActua", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByFiniquitoContratoGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.finiquitoContratoGeaed = :finiquitoContratoGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByHoraIngre", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByHoraActua", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.horaActua = :horaActua")})
public class GenAccionEmpleadoDepa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_geaed")
    private Integer ideGeaed;
    @Basic(optional = false)
    @Column(name = "detalle_geaed")
    private String detalleGeaed;
    @Basic(optional = false)
    @Column(name = "activo_geaed")
    private boolean activoGeaed;
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
    @Column(name = "finiquito_contrato_geaed")
    private Boolean finiquitoContratoGeaed;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideGeaed")
    private Collection<GenAccionMotivoEmpleado> genAccionMotivoEmpleadoCollection;

    public GenAccionEmpleadoDepa() {
    }

    public GenAccionEmpleadoDepa(Integer ideGeaed) {
        this.ideGeaed = ideGeaed;
    }

    public GenAccionEmpleadoDepa(Integer ideGeaed, String detalleGeaed, boolean activoGeaed) {
        this.ideGeaed = ideGeaed;
        this.detalleGeaed = detalleGeaed;
        this.activoGeaed = activoGeaed;
    }

    public Integer getIdeGeaed() {
        return ideGeaed;
    }

    public void setIdeGeaed(Integer ideGeaed) {
        this.ideGeaed = ideGeaed;
    }

    public String getDetalleGeaed() {
        return detalleGeaed;
    }

    public void setDetalleGeaed(String detalleGeaed) {
        this.detalleGeaed = detalleGeaed;
    }

    public boolean getActivoGeaed() {
        return activoGeaed;
    }

    public void setActivoGeaed(boolean activoGeaed) {
        this.activoGeaed = activoGeaed;
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

    public Boolean getFiniquitoContratoGeaed() {
        return finiquitoContratoGeaed;
    }

    public void setFiniquitoContratoGeaed(Boolean finiquitoContratoGeaed) {
        this.finiquitoContratoGeaed = finiquitoContratoGeaed;
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
    public Collection<GenAccionMotivoEmpleado> getGenAccionMotivoEmpleadoCollection() {
        return genAccionMotivoEmpleadoCollection;
    }

    public void setGenAccionMotivoEmpleadoCollection(Collection<GenAccionMotivoEmpleado> genAccionMotivoEmpleadoCollection) {
        this.genAccionMotivoEmpleadoCollection = genAccionMotivoEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeaed != null ? ideGeaed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenAccionEmpleadoDepa)) {
            return false;
        }
        GenAccionEmpleadoDepa other = (GenAccionEmpleadoDepa) object;
        if ((this.ideGeaed == null && other.ideGeaed != null) || (this.ideGeaed != null && !this.ideGeaed.equals(other.ideGeaed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenAccionEmpleadoDepa[ ideGeaed=" + ideGeaed + " ]";
    }
    
}
