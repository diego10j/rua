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
@Table(name = "gen_cargo_funcional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenCargoFuncional.findAll", query = "SELECT g FROM GenCargoFuncional g"),
    @NamedQuery(name = "GenCargoFuncional.findByIdeGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByIdeSbcar", query = "SELECT g FROM GenCargoFuncional g WHERE g.ideSbcar = :ideSbcar"),
    @NamedQuery(name = "GenCargoFuncional.findByDetalleGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.detalleGecaf = :detalleGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findBySiglasGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.siglasGecaf = :siglasGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByUsuarioIngre", query = "SELECT g FROM GenCargoFuncional g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenCargoFuncional.findByFechaIngre", query = "SELECT g FROM GenCargoFuncional g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenCargoFuncional.findByUsuarioActua", query = "SELECT g FROM GenCargoFuncional g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenCargoFuncional.findByPrincipalSecundarioGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.principalSecundarioGecaf = :principalSecundarioGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByActivoGecaf", query = "SELECT g FROM GenCargoFuncional g WHERE g.activoGecaf = :activoGecaf"),
    @NamedQuery(name = "GenCargoFuncional.findByHoraIngre", query = "SELECT g FROM GenCargoFuncional g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenCargoFuncional.findByHoraActua", query = "SELECT g FROM GenCargoFuncional g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenCargoFuncional.findByFechaActua", query = "SELECT g FROM GenCargoFuncional g WHERE g.fechaActua = :fechaActua")})
public class GenCargoFuncional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gecaf")
    private Integer ideGecaf;
    @Column(name = "ide_sbcar")
    private Integer ideSbcar;
    @Column(name = "detalle_gecaf")
    private String detalleGecaf;
    @Column(name = "siglas_gecaf")
    private String siglasGecaf;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "principal_secundario_gecaf")
    private Boolean principalSecundarioGecaf;
    @Column(name = "activo_gecaf")
    private Boolean activoGecaf;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.TIME)
    private Date fechaActua;
    @OneToMany(mappedBy = "ideGecaf")
    private Collection<GenAreaCargoJerarquia> genAreaCargoJerarquiaCollection;

    public GenCargoFuncional() {
    }

    public GenCargoFuncional(Integer ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public Integer getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(Integer ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public Integer getIdeSbcar() {
        return ideSbcar;
    }

    public void setIdeSbcar(Integer ideSbcar) {
        this.ideSbcar = ideSbcar;
    }

    public String getDetalleGecaf() {
        return detalleGecaf;
    }

    public void setDetalleGecaf(String detalleGecaf) {
        this.detalleGecaf = detalleGecaf;
    }

    public String getSiglasGecaf() {
        return siglasGecaf;
    }

    public void setSiglasGecaf(String siglasGecaf) {
        this.siglasGecaf = siglasGecaf;
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

    public Boolean getPrincipalSecundarioGecaf() {
        return principalSecundarioGecaf;
    }

    public void setPrincipalSecundarioGecaf(Boolean principalSecundarioGecaf) {
        this.principalSecundarioGecaf = principalSecundarioGecaf;
    }

    public Boolean getActivoGecaf() {
        return activoGecaf;
    }

    public void setActivoGecaf(Boolean activoGecaf) {
        this.activoGecaf = activoGecaf;
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

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    @XmlTransient
    public Collection<GenAreaCargoJerarquia> getGenAreaCargoJerarquiaCollection() {
        return genAreaCargoJerarquiaCollection;
    }

    public void setGenAreaCargoJerarquiaCollection(Collection<GenAreaCargoJerarquia> genAreaCargoJerarquiaCollection) {
        this.genAreaCargoJerarquiaCollection = genAreaCargoJerarquiaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGecaf != null ? ideGecaf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenCargoFuncional)) {
            return false;
        }
        GenCargoFuncional other = (GenCargoFuncional) object;
        if ((this.ideGecaf == null && other.ideGecaf != null) || (this.ideGecaf != null && !this.ideGecaf.equals(other.ideGecaf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenCargoFuncional[ ideGecaf=" + ideGecaf + " ]";
    }
    
}
