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
@Table(name = "nrh_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NrhRol.findAll", query = "SELECT n FROM NrhRol n"),
    @NamedQuery(name = "NrhRol.findByIdeNrrol", query = "SELECT n FROM NrhRol n WHERE n.ideNrrol = :ideNrrol"),
    @NamedQuery(name = "NrhRol.findByIdeSucu", query = "SELECT n FROM NrhRol n WHERE n.ideSucu = :ideSucu"),
    @NamedQuery(name = "NrhRol.findByIdeCnmoc", query = "SELECT n FROM NrhRol n WHERE n.ideCnmoc = :ideCnmoc"),
    @NamedQuery(name = "NrhRol.findByFechaNrrol", query = "SELECT n FROM NrhRol n WHERE n.fechaNrrol = :fechaNrrol"),
    @NamedQuery(name = "NrhRol.findByEstadoRentaNrrol", query = "SELECT n FROM NrhRol n WHERE n.estadoRentaNrrol = :estadoRentaNrrol"),
    @NamedQuery(name = "NrhRol.findByEstadoCalculadoNrrol", query = "SELECT n FROM NrhRol n WHERE n.estadoCalculadoNrrol = :estadoCalculadoNrrol"),
    @NamedQuery(name = "NrhRol.findByActivoNrrol", query = "SELECT n FROM NrhRol n WHERE n.activoNrrol = :activoNrrol"),
    @NamedQuery(name = "NrhRol.findByUsuarioIngre", query = "SELECT n FROM NrhRol n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRol.findByFechaIngre", query = "SELECT n FROM NrhRol n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRol.findByUsuarioActua", query = "SELECT n FROM NrhRol n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRol.findByFechaActua", query = "SELECT n FROM NrhRol n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRol.findByHoraIngre", query = "SELECT n FROM NrhRol n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRol.findByHoraActua", query = "SELECT n FROM NrhRol n WHERE n.horaActua = :horaActua")})
public class NrhRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_nrrol")
    private Integer ideNrrol;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "ide_cnmoc")
    private Integer ideCnmoc;
    @Basic(optional = false)
    @Column(name = "fecha_nrrol")
    @Temporal(TemporalType.DATE)
    private Date fechaNrrol;
    @Column(name = "estado_renta_nrrol")
    private Integer estadoRentaNrrol;
    @Column(name = "estado_calculado_nrrol")
    private Integer estadoCalculadoNrrol;
    @Basic(optional = false)
    @Column(name = "activo_nrrol")
    private boolean activoNrrol;
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
    @OneToMany(mappedBy = "ideNrrol")
    private Collection<AsiValidaNomina> asiValidaNominaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrrol")
    private Collection<NrhDetalleRol> nrhDetalleRolCollection;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_nresr", referencedColumnName = "ide_nresr")
    @ManyToOne
    private NrhEstadoRol ideNresr;
    @JoinColumn(name = "ide_nrdtn", referencedColumnName = "ide_nrdtn")
    @ManyToOne(optional = false)
    private NrhDetalleTipoNomina ideNrdtn;
    @JoinColumn(name = "ide_gepro", referencedColumnName = "ide_gepro")
    @ManyToOne(optional = false)
    private GenPeridoRol ideGepro;

    public NrhRol() {
    }

    public NrhRol(Integer ideNrrol) {
        this.ideNrrol = ideNrrol;
    }

    public NrhRol(Integer ideNrrol, Date fechaNrrol, boolean activoNrrol) {
        this.ideNrrol = ideNrrol;
        this.fechaNrrol = fechaNrrol;
        this.activoNrrol = activoNrrol;
    }

    public Integer getIdeNrrol() {
        return ideNrrol;
    }

    public void setIdeNrrol(Integer ideNrrol) {
        this.ideNrrol = ideNrrol;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Integer getIdeCnmoc() {
        return ideCnmoc;
    }

    public void setIdeCnmoc(Integer ideCnmoc) {
        this.ideCnmoc = ideCnmoc;
    }

    public Date getFechaNrrol() {
        return fechaNrrol;
    }

    public void setFechaNrrol(Date fechaNrrol) {
        this.fechaNrrol = fechaNrrol;
    }

    public Integer getEstadoRentaNrrol() {
        return estadoRentaNrrol;
    }

    public void setEstadoRentaNrrol(Integer estadoRentaNrrol) {
        this.estadoRentaNrrol = estadoRentaNrrol;
    }

    public Integer getEstadoCalculadoNrrol() {
        return estadoCalculadoNrrol;
    }

    public void setEstadoCalculadoNrrol(Integer estadoCalculadoNrrol) {
        this.estadoCalculadoNrrol = estadoCalculadoNrrol;
    }

    public boolean getActivoNrrol() {
        return activoNrrol;
    }

    public void setActivoNrrol(boolean activoNrrol) {
        this.activoNrrol = activoNrrol;
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
    public Collection<AsiValidaNomina> getAsiValidaNominaCollection() {
        return asiValidaNominaCollection;
    }

    public void setAsiValidaNominaCollection(Collection<AsiValidaNomina> asiValidaNominaCollection) {
        this.asiValidaNominaCollection = asiValidaNominaCollection;
    }

    @XmlTransient
    public Collection<NrhDetalleRol> getNrhDetalleRolCollection() {
        return nrhDetalleRolCollection;
    }

    public void setNrhDetalleRolCollection(Collection<NrhDetalleRol> nrhDetalleRolCollection) {
        this.nrhDetalleRolCollection = nrhDetalleRolCollection;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public NrhEstadoRol getIdeNresr() {
        return ideNresr;
    }

    public void setIdeNresr(NrhEstadoRol ideNresr) {
        this.ideNresr = ideNresr;
    }

    public NrhDetalleTipoNomina getIdeNrdtn() {
        return ideNrdtn;
    }

    public void setIdeNrdtn(NrhDetalleTipoNomina ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public GenPeridoRol getIdeGepro() {
        return ideGepro;
    }

    public void setIdeGepro(GenPeridoRol ideGepro) {
        this.ideGepro = ideGepro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrol != null ? ideNrrol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRol)) {
            return false;
        }
        NrhRol other = (NrhRol) object;
        if ((this.ideNrrol == null && other.ideNrrol != null) || (this.ideNrrol != null && !this.ideNrrol.equals(other.ideNrrol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRol[ ideNrrol=" + ideNrrol + " ]";
    }
    
}
