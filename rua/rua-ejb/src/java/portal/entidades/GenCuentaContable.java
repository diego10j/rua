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
@Table(name = "gen_cuenta_contable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenCuentaContable.findAll", query = "SELECT g FROM GenCuentaContable g"),
    @NamedQuery(name = "GenCuentaContable.findByIdeGecuc", query = "SELECT g FROM GenCuentaContable g WHERE g.ideGecuc = :ideGecuc"),
    @NamedQuery(name = "GenCuentaContable.findByDetalleGecuc", query = "SELECT g FROM GenCuentaContable g WHERE g.detalleGecuc = :detalleGecuc"),
    @NamedQuery(name = "GenCuentaContable.findByCodigoCuentaGecuc", query = "SELECT g FROM GenCuentaContable g WHERE g.codigoCuentaGecuc = :codigoCuentaGecuc"),
    @NamedQuery(name = "GenCuentaContable.findByCodigoCoreGecuc", query = "SELECT g FROM GenCuentaContable g WHERE g.codigoCoreGecuc = :codigoCoreGecuc"),
    @NamedQuery(name = "GenCuentaContable.findByActivoGecuc", query = "SELECT g FROM GenCuentaContable g WHERE g.activoGecuc = :activoGecuc"),
    @NamedQuery(name = "GenCuentaContable.findByUsuarioIngre", query = "SELECT g FROM GenCuentaContable g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenCuentaContable.findByFechaIngre", query = "SELECT g FROM GenCuentaContable g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenCuentaContable.findByUsuarioActua", query = "SELECT g FROM GenCuentaContable g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenCuentaContable.findByFechaActua", query = "SELECT g FROM GenCuentaContable g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenCuentaContable.findByHoraIngre", query = "SELECT g FROM GenCuentaContable g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenCuentaContable.findByHoraActua", query = "SELECT g FROM GenCuentaContable g WHERE g.horaActua = :horaActua")})
public class GenCuentaContable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gecuc")
    private Integer ideGecuc;
    @Basic(optional = false)
    @Column(name = "detalle_gecuc")
    private String detalleGecuc;
    @Column(name = "codigo_cuenta_gecuc")
    private String codigoCuentaGecuc;
    @Column(name = "codigo_core_gecuc")
    private String codigoCoreGecuc;
    @Basic(optional = false)
    @Column(name = "activo_gecuc")
    private boolean activoGecuc;
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
    @OneToMany(mappedBy = "ideGecuc")
    private Collection<NrhRubroAsiento> nrhRubroAsientoCollection;

    public GenCuentaContable() {
    }

    public GenCuentaContable(Integer ideGecuc) {
        this.ideGecuc = ideGecuc;
    }

    public GenCuentaContable(Integer ideGecuc, String detalleGecuc, boolean activoGecuc) {
        this.ideGecuc = ideGecuc;
        this.detalleGecuc = detalleGecuc;
        this.activoGecuc = activoGecuc;
    }

    public Integer getIdeGecuc() {
        return ideGecuc;
    }

    public void setIdeGecuc(Integer ideGecuc) {
        this.ideGecuc = ideGecuc;
    }

    public String getDetalleGecuc() {
        return detalleGecuc;
    }

    public void setDetalleGecuc(String detalleGecuc) {
        this.detalleGecuc = detalleGecuc;
    }

    public String getCodigoCuentaGecuc() {
        return codigoCuentaGecuc;
    }

    public void setCodigoCuentaGecuc(String codigoCuentaGecuc) {
        this.codigoCuentaGecuc = codigoCuentaGecuc;
    }

    public String getCodigoCoreGecuc() {
        return codigoCoreGecuc;
    }

    public void setCodigoCoreGecuc(String codigoCoreGecuc) {
        this.codigoCoreGecuc = codigoCoreGecuc;
    }

    public boolean getActivoGecuc() {
        return activoGecuc;
    }

    public void setActivoGecuc(boolean activoGecuc) {
        this.activoGecuc = activoGecuc;
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
    public Collection<NrhRubroAsiento> getNrhRubroAsientoCollection() {
        return nrhRubroAsientoCollection;
    }

    public void setNrhRubroAsientoCollection(Collection<NrhRubroAsiento> nrhRubroAsientoCollection) {
        this.nrhRubroAsientoCollection = nrhRubroAsientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGecuc != null ? ideGecuc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenCuentaContable)) {
            return false;
        }
        GenCuentaContable other = (GenCuentaContable) object;
        if ((this.ideGecuc == null && other.ideGecuc != null) || (this.ideGecuc != null && !this.ideGecuc.equals(other.ideGecuc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenCuentaContable[ ideGecuc=" + ideGecuc + " ]";
    }
    
}
