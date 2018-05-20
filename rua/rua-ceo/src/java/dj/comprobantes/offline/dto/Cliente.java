/*
 *********************************************************************
 Objetivo: Clase Cliente
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 *
 * @author diego.jacome
 */
public class Cliente implements Serializable {

    private String identificacion;
    private String tipoIdentificacion;
    private String nombreCliente;
    private String direccion;
    private String telefono;
    private String celular;
    private String correo;

    public Cliente(ResultSet resultado) {
        try {
            this.identificacion = resultado.getString("identificac_geper").trim();
            this.tipoIdentificacion = resultado.getString("alterno2_getid").trim();
            this.nombreCliente = resultado.getString("nom_geper").trim();
            this.direccion = resultado.getString("direccion_geper");
            this.telefono = resultado.getString("telefono_geper");
            this.celular = resultado.getString("movil_geper");
            this.correo = resultado.getString("correo_geper");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identificacion != null ? identificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.identificacion == null && other.identificacion != null) || (this.identificacion != null && !this.identificacion.equals(other.identificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "banecuador.fin.ec.dto[ pkIdentificacion=" + identificacion + " ]";
    }

}
