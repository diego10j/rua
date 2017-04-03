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
@Table(name = "pre_forma_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreFormaPago.findAll", query = "SELECT p FROM PreFormaPago p"),
    @NamedQuery(name = "PreFormaPago.findByIdePrfop", query = "SELECT p FROM PreFormaPago p WHERE p.idePrfop = :idePrfop"),
    @NamedQuery(name = "PreFormaPago.findByDetallePrfop", query = "SELECT p FROM PreFormaPago p WHERE p.detallePrfop = :detallePrfop"),
    @NamedQuery(name = "PreFormaPago.findByActivoPrfop", query = "SELECT p FROM PreFormaPago p WHERE p.activoPrfop = :activoPrfop"),
    @NamedQuery(name = "PreFormaPago.findByUsuarioIngre", query = "SELECT p FROM PreFormaPago p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreFormaPago.findByFechaIngre", query = "SELECT p FROM PreFormaPago p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreFormaPago.findByHoraIngre", query = "SELECT p FROM PreFormaPago p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreFormaPago.findByUsuarioActua", query = "SELECT p FROM PreFormaPago p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreFormaPago.findByFechaActua", query = "SELECT p FROM PreFormaPago p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreFormaPago.findByHoraActua", query = "SELECT p FROM PreFormaPago p WHERE p.horaActua = :horaActua")})
public class PreFormaPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_prfop")
    private Long idePrfop;
    @Column(name = "detalle_prfop")
    private String detallePrfop;
    @Column(name = "activo_prfop")
    private Boolean activoPrfop;
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

    public PreFormaPago() {
    }

    public PreFormaPago(Long idePrfop) {
        this.idePrfop = idePrfop;
    }

    public Long getIdePrfop() {
        return idePrfop;
    }

    public void setIdePrfop(Long idePrfop) {
        this.idePrfop = idePrfop;
    }

    public String getDetallePrfop() {
        return detallePrfop;
    }

    public void setDetallePrfop(String detallePrfop) {
        this.detallePrfop = detallePrfop;
    }

    public Boolean getActivoPrfop() {
        return activoPrfop;
    }

    public void setActivoPrfop(Boolean activoPrfop) {
        this.activoPrfop = activoPrfop;
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
        hash += (idePrfop != null ? idePrfop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreFormaPago)) {
            return false;
        }
        PreFormaPago other = (PreFormaPago) object;
        if ((this.idePrfop == null && other.idePrfop != null) || (this.idePrfop != null && !this.idePrfop.equals(other.idePrfop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreFormaPago[ idePrfop=" + idePrfop + " ]";
    }
    
}
