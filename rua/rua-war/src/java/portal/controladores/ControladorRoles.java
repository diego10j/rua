/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.reportes.GenerarReporte;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import paq_gestion.ejb.ServicioEmpleado;
import sistema.aplicacion.Utilitario;
import persistencia.Conexion;




@ManagedBean
@ViewScoped
public class ControladorRoles {

    private String strOpcion = "1";
    private List lisRolesPago;
    private Object rolSeleccionado;
    private Utilitario utilitario = new Utilitario();
    @EJB
    private ServicioEmpleado ser_empleado;
    private String strPathReporte;
    private Map parametros = new HashMap();
    
    private JasperPrint jasperPrint;
    Connection conn;
    @PostConstruct
    public void cargarDatos() {
        TablaGenerica tab_partida = ser_empleado.getPartida(utilitario.getVariable("IDE_GTEMP"));
        if (tab_partida != null) {
            lisRolesPago = ser_empleado.getRolesEmpleadoLista(tab_partida.getValor("IDE_GTEMP"));
        }
        strPathReporte = utilitario.getURL()+ "/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf";
    }

    public void visualizarRolx() {
        if (rolSeleccionado != null) {
            GenerarReporte ger = new GenerarReporte();
            
            try {
                parametros.put("IDE_GEPRO", Long.parseLong(((Object[]) rolSeleccionado)[4] + ""));
            } catch (Exception e) {
            }
            parametros.put("IDE_NRDTN", Long.parseLong(((Object[]) rolSeleccionado)[3] + ""));
            TablaGenerica tab_partida = ser_empleado.getPartidaPortal(utilitario.getVariable("IDE_GTEMP"),Long.parseLong(((Object[]) rolSeleccionado)[4] + "")+"","1");
            parametros.put("IDE_GEEDP", Long.parseLong(tab_partida.getValor("IDE_GEEDP")));
            parametros.put("titulo", " BOLETA DE PAGO");
            parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
        	parametros.put("par_total_recibir",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
			parametros.put("par_total_ingresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
			parametros.put("par_total_egresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
			ger.generar(parametros, "/reportes/rep_rol_de_pagos/rep_rol_individual.jasper");
        }        
    }


	public void reportBuilder() throws JRException {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/rua","admin","In$p3_UPS15");
        } catch (SQLException ex) {
            //Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
           // Logger.getLogger(IniciarReporte.class.getName()).log(Level.SEVERE, null, ex);
        }
		
        TablaGenerica tab_sucursal=utilitario.consultar("select ide_usua,ide_sucu from SIS_USUARIO_SUCURSAL where ide_usua="+utilitario.getVariable("ide_usua"));
        parametros.put("IDE_GEPRO", Long.parseLong(((Object[]) rolSeleccionado)[4] + ""));
        parametros.put("IDE_NRDTN", Long.parseLong(((Object[]) rolSeleccionado)[3] + ""));
        TablaGenerica tab_partida = ser_empleado.getPartidaPortal(utilitario.getVariable("IDE_GTEMP"),Long.parseLong(((Object[]) rolSeleccionado)[4] + "")+"","1");
        parametros.put("IDE_GEEDP", Long.parseLong(tab_partida.getValor("IDE_GEEDP")));
        parametros.put("titulo", " BOLETA DE PAGO");
        parametros.put("IDE_NRTIR", utilitario.getVariable("p_nrh_trubro_egreso") + "," + utilitario.getVariable("p_nrh_trubro_ingreso"));
    	parametros.put("par_total_recibir",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_valor_recibir")));
		parametros.put("par_total_ingresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_ingresos")));
		parametros.put("par_total_egresos",Integer.parseInt(utilitario.getVariable("p_nrh_rubro_total_egresos")));
        parametros.put("ide_empr", Integer.valueOf(Integer.parseInt(utilitario.getVariable("ide_empr"))));
        parametros.put("ide_sucu", Integer.valueOf(Integer.parseInt(tab_sucursal.getValor("ide_sucu"))));
        parametros.put("usuario", Integer.valueOf(Integer.parseInt(utilitario.getVariable("ide_usua"))));
	    parametros.put("SUBREPORT_DIR", getURL());
        parametros.put("REPORT_CONNECTION", conn);

		//System.out.println("enter a imprimir cc "+parametros);
		String report = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/rep_rol_de_pagos/rep_rol_individual.jasper");
		//System.out.println("report  "+report);
		//System.out.println("getURL()  "+getURL());


		jasperPrint = JasperFillManager.fillReport(report, parametros,conn);
	}

	public void visualizarRol() throws JRException,IOException {

		 try
		    {
		      FacesContext fc = FacesContext.getCurrentInstance();
		      ExternalContext ec = fc.getExternalContext();
				reportBuilder();
				//System.out.println("enter a imprimir veamos que pasa");
		      JRExporter exporter = new JRPdfExporter();
		      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		      File fil_reporte = new File(ec.getRealPath("/reportes/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf"));
		      exporter.setParameter(JRExporterParameter.OUTPUT_FILE, fil_reporte);
		      exporter.exportReport();
		      
		    }
		    catch (Exception ex)
		    {
		      System.out.println("error" + ex.getMessage());
		      ex.printStackTrace();
		    }
	}
    
	

	
    public List getLisRolesPago() {
        return lisRolesPago;
    }

    public void setLisRolesPago(List lisRolesPago) {
        this.lisRolesPago = lisRolesPago;
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public Object getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(Object rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public String getStrPathReporte() {
        return strPathReporte;
    }

    public void setStrPathReporte(String strPathReporte) {
        this.strPathReporte = strPathReporte;
    }

	public Map getParametros() {
		return parametros;
	}

	public void setParametros(Map parametros) {
		this.parametros = parametros;
	}
	  public String getURL()
	  {
	    ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
	    HttpServletRequest request = (HttpServletRequest)iecx.getRequest();
	    String path = request.getRequestURL() + "";
	    path = path.substring(0, path.lastIndexOf("/"));
	    if (path.indexOf("portal") > 0) {
	      path = path.substring(0, path.lastIndexOf("/"));
	    }
	    return path;
	  }
	  public Conexion getConexion()
	  {
	    Conexion conexion = (Conexion)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONEXION");
	    return conexion;
	  }
}
