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
@Table(name = "gen_categoria_estatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenCategoriaEstatus.findAll", query = "SELECT g FROM GenCategoriaEstatus g"),
    @NamedQuery(name = "GenCategoriaEstatus.findByIdeGecae", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.ideGecae = :ideGecae"),
    @NamedQuery(name = "GenCategoriaEstatus.findByDetalleGecae", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.detalleGecae = :detalleGecae"),
    @NamedQuery(name = "GenCategoriaEstatus.findByActivoGecae", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.activoGecae = :activoGecae"),
    @NamedQuery(name = "GenCategoriaEstatus.findByUsuarioIngre", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenCategoriaEstatus.findByFechaIngre", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenCategoriaEstatus.findByUsuarioActua", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenCategoriaEstatus.findByFechaActua", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenCategoriaEstatus.findByHoraIngre", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenCategoriaEstatus.findByHoraActua", query = "SELECT g FROM GenCategoriaEstatus g WHERE g.horaActua = :horaActua")})
public class GenCategoriaEstatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gecae")
    private Integer ideGecae;
    @Basic(optional = false)
    @Column(name = "detalle_gecae")
    private String detalleGecae;
    @Basic(optional = false)
    @Column(name = "activo_gecae")
    private boolean activoGecae;
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
    @OneToMany(mappedBy = "ideGecae")
    private Collection<GthTipoContrato> gthTipoContratoCollection;
    @OneToMany(mappedBy = "ideGecae")
    private Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection;
    @OneToMany(mappedBy = "ideGecae")
    private Collection<GthDocumentacionEmpleado> gthDocumentacionEmpleadoCollection;

    public GenCategoriaEstatus() {
    }

    public GenCategoriaEstatus(Integer ideGecae) {
        this.ideGecae = ideGecae;
    }

    public GenCategoriaEstatus(Integer ideGecae, String detalleGecae, boolean activoGecae) {
        this.ideGecae = ideGecae;
        this.detalleGecae = detalleGecae;
        this.activoGecae = activoGecae;
    }

    public Integer getIdeGecae() {
        return ideGecae;
    }

    public void setIdeGecae(Integer ideGecae) {
        this.ideGecae = ideGecae;
    }

    public String getDetalleGecae() {
        return detalleGecae;
    }

    public void setDetalleGecae(String detalleGecae) {
        this.detalleGecae = detalleGecae;
    }

    public boolean getActivoGecae() {
        return activoGecae;
    }

    public void setActivoGecae(boolean activoGecae) {
        this.activoGecae = activoGecae;
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
    public Collection<GthTipoContrato> getGthTipoContratoCollection() {
        return gthTipoContratoCollection;
    }

    public void setGthTipoContratoCollection(Collection<GthTipoContrato> gthTipoContratoCollection) {
        this.gthTipoContratoCollection = gthTipoContratoCollection;
    }

    @XmlTransient
    public Collection<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParCollection() {
        return genEmpleadosDepartamentoParCollection;
    }

    public void setGenEmpleadosDepartamentoParCollection(Collection<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParCollection) {
        this.genEmpleadosDepartamentoParCollection = genEmpleadosDepartamentoParCollection;
    }

    @XmlTransient
    public Collection<GthDocumentacionEmpleado> getGthDocumentacionEmpleadoCollection() {
        return gthDocumentacionEmpleadoCollection;
    }

    public void setGthDocumentacionEmpleadoCollection(Collection<GthDocumentacionEmpleado> gthDocumentacionEmpleadoCollection) {
        this.gthDocumentacionEmpleadoCollection = gthDocumentacionEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGecae != null ? ideGecae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenCategoriaEstatus)) {
            return false;
        }
        GenCategoriaEstatus other = (GenCategoriaEstatus) object;
        if ((this.ideGecae == null && other.ideGecae != null) || (this.ideGecae != null && !this.ideGecae.equals(other.ideGecae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenCategoriaEstatus[ ideGecae=" + ideGecae + " ]";
    }
    
}
