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
@Table(name = "gth_tipo_cuenta_bancaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GthTipoCuentaBancaria.findAll", query = "SELECT g FROM GthTipoCuentaBancaria g"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByIdeGttcb", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.ideGttcb = :ideGttcb"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByDetalleGttcb", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.detalleGttcb = :detalleGttcb"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByCodigoGttcb", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.codigoGttcb = :codigoGttcb"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByActivoGttcb", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.activoGttcb = :activoGttcb"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByUsuarioIngre", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByFechaIngre", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByUsuarioActua", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByFechaActua", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByHoraIngre", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoCuentaBancaria.findByHoraActua", query = "SELECT g FROM GthTipoCuentaBancaria g WHERE g.horaActua = :horaActua")})
public class GthTipoCuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gttcb")
    private Integer ideGttcb;
    @Column(name = "detalle_gttcb")
    private String detalleGttcb;
    @Column(name = "codigo_gttcb")
    private String codigoGttcb;
    @Column(name = "activo_gttcb")
    private Boolean activoGttcb;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttcb")
    private Collection<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoCollection;
    @OneToMany(mappedBy = "ideGttcb")
    private Collection<GthViaticos> gthViaticosCollection;

    public GthTipoCuentaBancaria() {
    }

    public GthTipoCuentaBancaria(Integer ideGttcb) {
        this.ideGttcb = ideGttcb;
    }

    public Integer getIdeGttcb() {
        return ideGttcb;
    }

    public void setIdeGttcb(Integer ideGttcb) {
        this.ideGttcb = ideGttcb;
    }

    public String getDetalleGttcb() {
        return detalleGttcb;
    }

    public void setDetalleGttcb(String detalleGttcb) {
        this.detalleGttcb = detalleGttcb;
    }

    public String getCodigoGttcb() {
        return codigoGttcb;
    }

    public void setCodigoGttcb(String codigoGttcb) {
        this.codigoGttcb = codigoGttcb;
    }

    public Boolean getActivoGttcb() {
        return activoGttcb;
    }

    public void setActivoGttcb(Boolean activoGttcb) {
        this.activoGttcb = activoGttcb;
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

    @XmlTransient
    public Collection<GthCuentaBancariaEmpleado> getGthCuentaBancariaEmpleadoCollection() {
        return gthCuentaBancariaEmpleadoCollection;
    }

    public void setGthCuentaBancariaEmpleadoCollection(Collection<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoCollection) {
        this.gthCuentaBancariaEmpleadoCollection = gthCuentaBancariaEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthViaticos> getGthViaticosCollection() {
        return gthViaticosCollection;
    }

    public void setGthViaticosCollection(Collection<GthViaticos> gthViaticosCollection) {
        this.gthViaticosCollection = gthViaticosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttcb != null ? ideGttcb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoCuentaBancaria)) {
            return false;
        }
        GthTipoCuentaBancaria other = (GthTipoCuentaBancaria) object;
        if ((this.ideGttcb == null && other.ideGttcb != null) || (this.ideGttcb != null && !this.ideGttcb.equals(other.ideGttcb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoCuentaBancaria[ ideGttcb=" + ideGttcb + " ]";
    }
    
}
