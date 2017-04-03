/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "sis_tabla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisTabla.findAll", query = "SELECT s FROM SisTabla s"),
    @NamedQuery(name = "SisTabla.findByIdeTabl", query = "SELECT s FROM SisTabla s WHERE s.ideTabl = :ideTabl"),
    @NamedQuery(name = "SisTabla.findByNumeroTabl", query = "SELECT s FROM SisTabla s WHERE s.numeroTabl = :numeroTabl"),
    @NamedQuery(name = "SisTabla.findByTablaTabl", query = "SELECT s FROM SisTabla s WHERE s.tablaTabl = :tablaTabl"),
    @NamedQuery(name = "SisTabla.findByPrimariaTabl", query = "SELECT s FROM SisTabla s WHERE s.primariaTabl = :primariaTabl"),
    @NamedQuery(name = "SisTabla.findByNombreTabl", query = "SELECT s FROM SisTabla s WHERE s.nombreTabl = :nombreTabl"),
    @NamedQuery(name = "SisTabla.findByForaneaTabl", query = "SELECT s FROM SisTabla s WHERE s.foraneaTabl = :foraneaTabl"),
    @NamedQuery(name = "SisTabla.findByPadreTabl", query = "SELECT s FROM SisTabla s WHERE s.padreTabl = :padreTabl"),
    @NamedQuery(name = "SisTabla.findByOrdenTabl", query = "SELECT s FROM SisTabla s WHERE s.ordenTabl = :ordenTabl"),
    @NamedQuery(name = "SisTabla.findByFilasTabl", query = "SELECT s FROM SisTabla s WHERE s.filasTabl = :filasTabl"),
    @NamedQuery(name = "SisTabla.findByGeneraPrimariaTabl", query = "SELECT s FROM SisTabla s WHERE s.generaPrimariaTabl = :generaPrimariaTabl"),
    @NamedQuery(name = "SisTabla.findByFormularioTabl", query = "SELECT s FROM SisTabla s WHERE s.formularioTabl = :formularioTabl")})
public class SisTabla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_tabl")
    private Long ideTabl;
    @Column(name = "numero_tabl")
    private Integer numeroTabl;
    @Basic(optional = false)
    @Column(name = "tabla_tabl")
    private String tablaTabl;
    @Basic(optional = false)
    @Column(name = "primaria_tabl")
    private String primariaTabl;
    @Column(name = "nombre_tabl")
    private String nombreTabl;
    @Column(name = "foranea_tabl")
    private String foraneaTabl;
    @Column(name = "padre_tabl")
    private String padreTabl;
    @Column(name = "orden_tabl")
    private String ordenTabl;
    @Column(name = "filas_tabl")
    private Integer filasTabl;
    @Column(name = "genera_primaria_tabl")
    private Boolean generaPrimariaTabl;
    @Column(name = "formulario_tabl")
    private Boolean formularioTabl;
    @OneToMany(mappedBy = "ideTabl")
    private Collection<SisCampo> sisCampoCollection;
    @OneToMany(mappedBy = "ideTabl")
    private Collection<SisCombo> sisComboCollection;
    @JoinColumn(name = "ide_opci", referencedColumnName = "ide_opci")
    @ManyToOne
    private SisOpcion ideOpci;

    public SisTabla() {
    }

    public SisTabla(Long ideTabl) {
        this.ideTabl = ideTabl;
    }

    public SisTabla(Long ideTabl, String tablaTabl, String primariaTabl) {
        this.ideTabl = ideTabl;
        this.tablaTabl = tablaTabl;
        this.primariaTabl = primariaTabl;
    }

    public Long getIdeTabl() {
        return ideTabl;
    }

    public void setIdeTabl(Long ideTabl) {
        this.ideTabl = ideTabl;
    }

    public Integer getNumeroTabl() {
        return numeroTabl;
    }

    public void setNumeroTabl(Integer numeroTabl) {
        this.numeroTabl = numeroTabl;
    }

    public String getTablaTabl() {
        return tablaTabl;
    }

    public void setTablaTabl(String tablaTabl) {
        this.tablaTabl = tablaTabl;
    }

    public String getPrimariaTabl() {
        return primariaTabl;
    }

    public void setPrimariaTabl(String primariaTabl) {
        this.primariaTabl = primariaTabl;
    }

    public String getNombreTabl() {
        return nombreTabl;
    }

    public void setNombreTabl(String nombreTabl) {
        this.nombreTabl = nombreTabl;
    }

    public String getForaneaTabl() {
        return foraneaTabl;
    }

    public void setForaneaTabl(String foraneaTabl) {
        this.foraneaTabl = foraneaTabl;
    }

    public String getPadreTabl() {
        return padreTabl;
    }

    public void setPadreTabl(String padreTabl) {
        this.padreTabl = padreTabl;
    }

    public String getOrdenTabl() {
        return ordenTabl;
    }

    public void setOrdenTabl(String ordenTabl) {
        this.ordenTabl = ordenTabl;
    }

    public Integer getFilasTabl() {
        return filasTabl;
    }

    public void setFilasTabl(Integer filasTabl) {
        this.filasTabl = filasTabl;
    }

    public Boolean getGeneraPrimariaTabl() {
        return generaPrimariaTabl;
    }

    public void setGeneraPrimariaTabl(Boolean generaPrimariaTabl) {
        this.generaPrimariaTabl = generaPrimariaTabl;
    }

    public Boolean getFormularioTabl() {
        return formularioTabl;
    }

    public void setFormularioTabl(Boolean formularioTabl) {
        this.formularioTabl = formularioTabl;
    }

    @XmlTransient
    public Collection<SisCampo> getSisCampoCollection() {
        return sisCampoCollection;
    }

    public void setSisCampoCollection(Collection<SisCampo> sisCampoCollection) {
        this.sisCampoCollection = sisCampoCollection;
    }

    @XmlTransient
    public Collection<SisCombo> getSisComboCollection() {
        return sisComboCollection;
    }

    public void setSisComboCollection(Collection<SisCombo> sisComboCollection) {
        this.sisComboCollection = sisComboCollection;
    }

    public SisOpcion getIdeOpci() {
        return ideOpci;
    }

    public void setIdeOpci(SisOpcion ideOpci) {
        this.ideOpci = ideOpci;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTabl != null ? ideTabl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisTabla)) {
            return false;
        }
        SisTabla other = (SisTabla) object;
        if ((this.ideTabl == null && other.ideTabl != null) || (this.ideTabl != null && !this.ideTabl.equals(other.ideTabl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisTabla[ ideTabl=" + ideTabl + " ]";
    }
    
}
