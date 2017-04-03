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
@Table(name = "pre_tipo_compareciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreTipoCompareciente.findAll", query = "SELECT p FROM PreTipoCompareciente p"),
    @NamedQuery(name = "PreTipoCompareciente.findByIdePrtio", query = "SELECT p FROM PreTipoCompareciente p WHERE p.idePrtio = :idePrtio"),
    @NamedQuery(name = "PreTipoCompareciente.findByDetallePrtio", query = "SELECT p FROM PreTipoCompareciente p WHERE p.detallePrtio = :detallePrtio"),
    @NamedQuery(name = "PreTipoCompareciente.findByActivoPrtio", query = "SELECT p FROM PreTipoCompareciente p WHERE p.activoPrtio = :activoPrtio"),
    @NamedQuery(name = "PreTipoCompareciente.findByUsuarioIngre", query = "SELECT p FROM PreTipoCompareciente p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreTipoCompareciente.findByFechaIngre", query = "SELECT p FROM PreTipoCompareciente p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreTipoCompareciente.findByHoraIngre", query = "SELECT p FROM PreTipoCompareciente p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreTipoCompareciente.findByUsuarioActua", query = "SELECT p FROM PreTipoCompareciente p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreTipoCompareciente.findByFechaActua", query = "SELECT p FROM PreTipoCompareciente p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreTipoCompareciente.findByHoraActua", query = "SELECT p FROM PreTipoCompareciente p WHERE p.horaActua = :horaActua")})
public class PreTipoCompareciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_prtio")
    private Long idePrtio;
    @Column(name = "detalle_prtio")
    private String detallePrtio;
    @Column(name = "activo_prtio")
    private Boolean activoPrtio;
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

    public PreTipoCompareciente() {
    }

    public PreTipoCompareciente(Long idePrtio) {
        this.idePrtio = idePrtio;
    }

    public Long getIdePrtio() {
        return idePrtio;
    }

    public void setIdePrtio(Long idePrtio) {
        this.idePrtio = idePrtio;
    }

    public String getDetallePrtio() {
        return detallePrtio;
    }

    public void setDetallePrtio(String detallePrtio) {
        this.detallePrtio = detallePrtio;
    }

    public Boolean getActivoPrtio() {
        return activoPrtio;
    }

    public void setActivoPrtio(Boolean activoPrtio) {
        this.activoPrtio = activoPrtio;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrtio != null ? idePrtio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreTipoCompareciente)) {
            return false;
        }
        PreTipoCompareciente other = (PreTipoCompareciente) object;
        if ((this.idePrtio == null && other.idePrtio != null) || (this.idePrtio != null && !this.idePrtio.equals(other.idePrtio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreTipoCompareciente[ idePrtio=" + idePrtio + " ]";
    }
    
}
