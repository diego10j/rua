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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "gen_division_politica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenDivisionPolitica.findAll", query = "SELECT g FROM GenDivisionPolitica g"),
    @NamedQuery(name = "GenDivisionPolitica.findByIdeGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.ideGedip = :ideGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByGenIdeGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.genIdeGedip = :genIdeGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByDetalleGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.detalleGedip = :detalleGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByCoeficienteGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.coeficienteGedip = :coeficienteGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByCodigoSriGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.codigoSriGedip = :codigoSriGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByUsuarioIngre", query = "SELECT g FROM GenDivisionPolitica g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDivisionPolitica.findByFechaIngre", query = "SELECT g FROM GenDivisionPolitica g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDivisionPolitica.findByUsuarioActua", query = "SELECT g FROM GenDivisionPolitica g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDivisionPolitica.findByFechaActua", query = "SELECT g FROM GenDivisionPolitica g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDivisionPolitica.findByHoraIngre", query = "SELECT g FROM GenDivisionPolitica g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDivisionPolitica.findByHoraActua", query = "SELECT g FROM GenDivisionPolitica g WHERE g.horaActua = :horaActua")})
public class GenDivisionPolitica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gedip")
    private Integer ideGedip;
    @Column(name = "gen_ide_gedip")
    private Integer genIdeGedip;
    @Column(name = "detalle_gedip")
    private String detalleGedip;
    @Column(name = "coeficiente_gedip")
    private Integer coeficienteGedip;
    @Column(name = "codigo_sri_gedip")
    private String codigoSriGedip;
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
    @OneToMany(mappedBy = "ideGedip")
    private Collection<GthDireccion> gthDireccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGedip")
    private Collection<GthEmpleado> gthEmpleadoCollection;
    @JoinColumn(name = "ide_getdp", referencedColumnName = "ide_getdp")
    @ManyToOne
    private GenTipoDivisionPolitica ideGetdp;
    @JoinColumn(name = "ide_gereg", referencedColumnName = "ide_gereg")
    @ManyToOne
    private GenRegion ideGereg;
    @OneToMany(mappedBy = "ideGedip")
    private Collection<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaCollection;
    @OneToMany(mappedBy = "ideGedip")
    private Collection<GthCapacitacionEmpleado> gthCapacitacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGedip")
    private Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection;
    @OneToMany(mappedBy = "ideGedip")
    private Collection<GthViaticos> gthViaticosCollection;
    @OneToMany(mappedBy = "ideGedip")
    private Collection<SisSucursal> sisSucursalCollection;

    public GenDivisionPolitica() {
    }

    public GenDivisionPolitica(Integer ideGedip) {
        this.ideGedip = ideGedip;
    }

    public Integer getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(Integer ideGedip) {
        this.ideGedip = ideGedip;
    }

    public Integer getGenIdeGedip() {
        return genIdeGedip;
    }

    public void setGenIdeGedip(Integer genIdeGedip) {
        this.genIdeGedip = genIdeGedip;
    }

    public String getDetalleGedip() {
        return detalleGedip;
    }

    public void setDetalleGedip(String detalleGedip) {
        this.detalleGedip = detalleGedip;
    }

    public Integer getCoeficienteGedip() {
        return coeficienteGedip;
    }

    public void setCoeficienteGedip(Integer coeficienteGedip) {
        this.coeficienteGedip = coeficienteGedip;
    }

    public String getCodigoSriGedip() {
        return codigoSriGedip;
    }

    public void setCodigoSriGedip(String codigoSriGedip) {
        this.codigoSriGedip = codigoSriGedip;
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
    public Collection<GthDireccion> getGthDireccionCollection() {
        return gthDireccionCollection;
    }

    public void setGthDireccionCollection(Collection<GthDireccion> gthDireccionCollection) {
        this.gthDireccionCollection = gthDireccionCollection;
    }

    @XmlTransient
    public Collection<GthEmpleado> getGthEmpleadoCollection() {
        return gthEmpleadoCollection;
    }

    public void setGthEmpleadoCollection(Collection<GthEmpleado> gthEmpleadoCollection) {
        this.gthEmpleadoCollection = gthEmpleadoCollection;
    }

    public GenTipoDivisionPolitica getIdeGetdp() {
        return ideGetdp;
    }

    public void setIdeGetdp(GenTipoDivisionPolitica ideGetdp) {
        this.ideGetdp = ideGetdp;
    }

    public GenRegion getIdeGereg() {
        return ideGereg;
    }

    public void setIdeGereg(GenRegion ideGereg) {
        this.ideGereg = ideGereg;
    }

    @XmlTransient
    public Collection<GthExperienciaDocenteEmplea> getGthExperienciaDocenteEmpleaCollection() {
        return gthExperienciaDocenteEmpleaCollection;
    }

    public void setGthExperienciaDocenteEmpleaCollection(Collection<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaCollection) {
        this.gthExperienciaDocenteEmpleaCollection = gthExperienciaDocenteEmpleaCollection;
    }

    @XmlTransient
    public Collection<GthCapacitacionEmpleado> getGthCapacitacionEmpleadoCollection() {
        return gthCapacitacionEmpleadoCollection;
    }

    public void setGthCapacitacionEmpleadoCollection(Collection<GthCapacitacionEmpleado> gthCapacitacionEmpleadoCollection) {
        this.gthCapacitacionEmpleadoCollection = gthCapacitacionEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthEducacionEmpleado> getGthEducacionEmpleadoCollection() {
        return gthEducacionEmpleadoCollection;
    }

    public void setGthEducacionEmpleadoCollection(Collection<GthEducacionEmpleado> gthEducacionEmpleadoCollection) {
        this.gthEducacionEmpleadoCollection = gthEducacionEmpleadoCollection;
    }

    @XmlTransient
    public Collection<GthViaticos> getGthViaticosCollection() {
        return gthViaticosCollection;
    }

    public void setGthViaticosCollection(Collection<GthViaticos> gthViaticosCollection) {
        this.gthViaticosCollection = gthViaticosCollection;
    }

    @XmlTransient
    public Collection<SisSucursal> getSisSucursalCollection() {
        return sisSucursalCollection;
    }

    public void setSisSucursalCollection(Collection<SisSucursal> sisSucursalCollection) {
        this.sisSucursalCollection = sisSucursalCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGedip != null ? ideGedip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDivisionPolitica)) {
            return false;
        }
        GenDivisionPolitica other = (GenDivisionPolitica) object;
        if ((this.ideGedip == null && other.ideGedip != null) || (this.ideGedip != null && !this.ideGedip.equals(other.ideGedip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDivisionPolitica[ ideGedip=" + ideGedip + " ]";
    }
    
}
