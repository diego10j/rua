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
@Table(name = "gen_tipo_division_politica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenTipoDivisionPolitica.findAll", query = "SELECT g FROM GenTipoDivisionPolitica g"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByIdeGetdp", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.ideGetdp = :ideGetdp"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByDetalleGetdp", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.detalleGetdp = :detalleGetdp"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByNivelGetdp", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.nivelGetdp = :nivelGetdp"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByUsuarioIngre", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByFechaIngre", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByUsuarioActua", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByFechaActua", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByHoraIngre", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoDivisionPolitica.findByHoraActua", query = "SELECT g FROM GenTipoDivisionPolitica g WHERE g.horaActua = :horaActua")})
public class GenTipoDivisionPolitica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_getdp")
    private Integer ideGetdp;
    @Column(name = "detalle_getdp")
    private String detalleGetdp;
    @Column(name = "nivel_getdp")
    private Integer nivelGetdp;
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
    @OneToMany(mappedBy = "ideGetdp")
    private Collection<GenDivisionPolitica> genDivisionPoliticaCollection;

    public GenTipoDivisionPolitica() {
    }

    public GenTipoDivisionPolitica(Integer ideGetdp) {
        this.ideGetdp = ideGetdp;
    }

    public Integer getIdeGetdp() {
        return ideGetdp;
    }

    public void setIdeGetdp(Integer ideGetdp) {
        this.ideGetdp = ideGetdp;
    }

    public String getDetalleGetdp() {
        return detalleGetdp;
    }

    public void setDetalleGetdp(String detalleGetdp) {
        this.detalleGetdp = detalleGetdp;
    }

    public Integer getNivelGetdp() {
        return nivelGetdp;
    }

    public void setNivelGetdp(Integer nivelGetdp) {
        this.nivelGetdp = nivelGetdp;
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
    public Collection<GenDivisionPolitica> getGenDivisionPoliticaCollection() {
        return genDivisionPoliticaCollection;
    }

    public void setGenDivisionPoliticaCollection(Collection<GenDivisionPolitica> genDivisionPoliticaCollection) {
        this.genDivisionPoliticaCollection = genDivisionPoliticaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetdp != null ? ideGetdp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoDivisionPolitica)) {
            return false;
        }
        GenTipoDivisionPolitica other = (GenTipoDivisionPolitica) object;
        if ((this.ideGetdp == null && other.ideGetdp != null) || (this.ideGetdp != null && !this.ideGetdp.equals(other.ideGetdp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoDivisionPolitica[ ideGetdp=" + ideGetdp + " ]";
    }
    
}
