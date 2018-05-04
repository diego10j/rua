/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import componentes.FacturaCxC;
import framework.componentes.AreaTexto;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import sistema.aplicacion.Pantalla;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import framework.componentes.Texto;
import javax.ejb.EJB;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_cobrar.ServicioCliente;
import servicios.cuentas_x_cobrar.ServicioFacturaCxC;
import servicios.inventario.ServicioInventario;
import servicios.inventario.ServicioProducto;
import java.util.List;
import javax.ejb.EJB;
import servicios.sistema.ServicioSistema;
/**
 *
 * @author Diego
 */
public class pre_factura_venta extends Pantalla {
   private FacturaCxC fcc_factura = new FacturaCxC();
    @EJB
    private final ServicioSistema ser_sistema = (ServicioSistema) utilitario.instanciarEJB(ServicioSistema.class);    
  
    public pre_factura_venta() {     
        if (tienePerfilResponsable()) {
        
            
            
        } else {
            utilitario.agregarNotificacionInfo("Mensaje", "EL usuario ingresado no registra permisos para el control de Asistencia. Consulte con el Administrador");
        }        
    }
String docente = "";
    String documento="";
    String ide_docente="";
        private boolean tienePerfilResponsable() {/*
        List sql = utilitario.getConexion().consultar(ser_sistema.getUsuarioSistema(utilitario.getVariable("IDE_USUA")," and not ide_gtemp is null"));

        if (!sql.isEmpty()) {
            Object[] fila = (Object[]) sql.get(0);
                    List sql2 =  utilitario.getConexion().consultar(ser_personal.getDatoPersonalCodigo(fila[3].toString()));
            if (!sql2.isEmpty()) {
                Object[] fila2 = (Object[]) sql2.get(0);
                docente = fila2[1].toString()+" "+fila2[2].toString();
                documento = fila2[3].toString();
                ide_docente=fila2[0].toString();
                    return true;
            }  
            else{
            return false;
            }
        } else {
            return false;
        }
*/
            return true;
    }    

    @Override
    public void insertar() {
        
    }

    @Override
    public void guardar() {

    }

    @Override
    public void eliminar() {
       
    }
   
    
}
