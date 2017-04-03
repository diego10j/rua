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
@Table(name = "sis_campo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SisCampo.findAll", query = "SELECT s FROM SisCampo s"),
    @NamedQuery(name = "SisCampo.findByIdeCamp", query = "SELECT s FROM SisCampo s WHERE s.ideCamp = :ideCamp"),
    @NamedQuery(name = "SisCampo.findByNomCamp", query = "SELECT s FROM SisCampo s WHERE s.nomCamp = :nomCamp"),
    @NamedQuery(name = "SisCampo.findByNomVisualCamp", query = "SELECT s FROM SisCampo s WHERE s.nomVisualCamp = :nomVisualCamp"),
    @NamedQuery(name = "SisCampo.findByOrdenCamp", query = "SELECT s FROM SisCampo s WHERE s.ordenCamp = :ordenCamp"),
    @NamedQuery(name = "SisCampo.findByVisibleCamp", query = "SELECT s FROM SisCampo s WHERE s.visibleCamp = :visibleCamp"),
    @NamedQuery(name = "SisCampo.findByLecturaCamp", query = "SELECT s FROM SisCampo s WHERE s.lecturaCamp = :lecturaCamp"),
    @NamedQuery(name = "SisCampo.findByDefectoCamp", query = "SELECT s FROM SisCampo s WHERE s.defectoCamp = :defectoCamp"),
    @NamedQuery(name = "SisCampo.findByMascaraCamp", query = "SELECT s FROM SisCampo s WHERE s.mascaraCamp = :mascaraCamp"),
    @NamedQuery(name = "SisCampo.findByFiltroCamp", query = "SELECT s FROM SisCampo s WHERE s.filtroCamp = :filtroCamp"),
    @NamedQuery(name = "SisCampo.findByComentarioCamp", query = "SELECT s FROM SisCampo s WHERE s.comentarioCamp = :comentarioCamp"),
    @NamedQuery(name = "SisCampo.findByMayusculaCamp", query = "SELECT s FROM SisCampo s WHERE s.mayusculaCamp = :mayusculaCamp"),
    @NamedQuery(name = "SisCampo.findByRequeridoCamp", query = "SELECT s FROM SisCampo s WHERE s.requeridoCamp = :requeridoCamp"),
    @NamedQuery(name = "SisCampo.findByUnicoCamp", query = "SELECT s FROM SisCampo s WHERE s.unicoCamp = :unicoCamp")})
public class SisCampo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_camp")
    private Long ideCamp;
    @Basic(optional = false)
    @Column(name = "nom_camp")
    private String nomCamp;
    @Column(name = "nom_visual_camp")
    private String nomVisualCamp;
    @Column(name = "orden_camp")
    private Integer ordenCamp;
    @Column(name = "visible_camp")
    private Boolean visibleCamp;
    @Column(name = "lectura_camp")
    private Boolean lecturaCamp;
    @Column(name = "defecto_camp")
    private String defectoCamp;
    @Column(name = "mascara_camp")
    private String mascaraCamp;
    @Column(name = "filtro_camp")
    private Boolean filtroCamp;
    @Column(name = "comentario_camp")
    private String comentarioCamp;
    @Column(name = "mayuscula_camp")
    private Boolean mayusculaCamp;
    @Column(name = "requerido_camp")
    private Boolean requeridoCamp;
    @Column(name = "unico_camp")
    private Boolean unicoCamp;
    @JoinColumn(name = "ide_tabl", referencedColumnName = "ide_tabl")
    @ManyToOne
    private SisTabla ideTabl;
    @OneToMany(mappedBy = "ideCamp")
    private Collection<SisPerfilCampo> sisPerfilCampoCollection;

    public SisCampo() {
    }

    public SisCampo(Long ideCamp) {
        this.ideCamp = ideCamp;
    }

    public SisCampo(Long ideCamp, String nomCamp) {
        this.ideCamp = ideCamp;
        this.nomCamp = nomCamp;
    }

    public Long getIdeCamp() {
        return ideCamp;
    }

    public void setIdeCamp(Long ideCamp) {
        this.ideCamp = ideCamp;
    }

    public String getNomCamp() {
        return nomCamp;
    }

    public void setNomCamp(String nomCamp) {
        this.nomCamp = nomCamp;
    }

    public String getNomVisualCamp() {
        return nomVisualCamp;
    }

    public void setNomVisualCamp(String nomVisualCamp) {
        this.nomVisualCamp = nomVisualCamp;
    }

    public Integer getOrdenCamp() {
        return ordenCamp;
    }

    public void setOrdenCamp(Integer ordenCamp) {
        this.ordenCamp = ordenCamp;
    }

    public Boolean getVisibleCamp() {
        return visibleCamp;
    }

    public void setVisibleCamp(Boolean visibleCamp) {
        this.visibleCamp = visibleCamp;
    }

    public Boolean getLecturaCamp() {
        return lecturaCamp;
    }

    public void setLecturaCamp(Boolean lecturaCamp) {
        this.lecturaCamp = lecturaCamp;
    }

    public String getDefectoCamp() {
        return defectoCamp;
    }

    public void setDefectoCamp(String defectoCamp) {
        this.defectoCamp = defectoCamp;
    }

    public String getMascaraCamp() {
        return mascaraCamp;
    }

    public void setMascaraCamp(String mascaraCamp) {
        this.mascaraCamp = mascaraCamp;
    }

    public Boolean getFiltroCamp() {
        return filtroCamp;
    }

    public void setFiltroCamp(Boolean filtroCamp) {
        this.filtroCamp = filtroCamp;
    }

    public String getComentarioCamp() {
        return comentarioCamp;
    }

    public void setComentarioCamp(String comentarioCamp) {
        this.comentarioCamp = comentarioCamp;
    }

    public Boolean getMayusculaCamp() {
        return mayusculaCamp;
    }

    public void setMayusculaCamp(Boolean mayusculaCamp) {
        this.mayusculaCamp = mayusculaCamp;
    }

    public Boolean getRequeridoCamp() {
        return requeridoCamp;
    }

    public void setRequeridoCamp(Boolean requeridoCamp) {
        this.requeridoCamp = requeridoCamp;
    }

    public Boolean getUnicoCamp() {
        return unicoCamp;
    }

    public void setUnicoCamp(Boolean unicoCamp) {
        this.unicoCamp = unicoCamp;
    }

    public SisTabla getIdeTabl() {
        return ideTabl;
    }

    public void setIdeTabl(SisTabla ideTabl) {
        this.ideTabl = ideTabl;
    }

    @XmlTransient
    public Collection<SisPerfilCampo> getSisPerfilCampoCollection() {
        return sisPerfilCampoCollection;
    }

    public void setSisPerfilCampoCollection(Collection<SisPerfilCampo> sisPerfilCampoCollection) {
        this.sisPerfilCampoCollection = sisPerfilCampoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCamp != null ? ideCamp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisCampo)) {
            return false;
        }
        SisCampo other = (SisCampo) object;
        if ((this.ideCamp == null && other.ideCamp != null) || (this.ideCamp != null && !this.ideCamp.equals(other.ideCamp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisCampo[ ideCamp=" + ideCamp + " ]";
    }
    
}
