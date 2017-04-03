/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.controladores;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import sistema.aplicacion.Utilitario;
import portal.entidades.GthCargasFamiliares;
import portal.entidades.GthCargo;
import portal.entidades.GthConyuge;
import portal.entidades.GthCorreo;
import portal.entidades.GthDireccion;
import portal.entidades.GthEmpleado;
import portal.entidades.GthGenero;
import portal.entidades.GthNacionalidad;
import portal.entidades.GthTelefono;
import portal.entidades.GthTipoDocumentoIdentidad;
import portal.entidades.GthTipoParentescoRelacion;
import portal.entidades.GthTipoTelefono;
import portal.entidades.GthUnionLibre;
import portal.servicios.ServicioEmpleadoJPA;

/**
 *
 * @author Diego
 */
@ManagedBean
@ViewScoped
public class ControladorEmpleado implements Serializable {

    private String strOpcion = "1";
    private Utilitario utilitario = new Utilitario();
    private String strCasado = "1";  //poner variable
    private String strUnionLibre = "2";//poner variable
    private String strSoltero = "0";//poner variable
    private GthEmpleado empleado;
    private GthDireccion direccion;
    @EJB
    private ServicioEmpleadoJPA servicioEmpleado;
    //Direccion
    private List listaPaises;
    private List listaProvincias;
    private List listaCiudades;
    private List listaParroquias;
    private String strPais = "0"; //Ecuador por defecto
    private String strProvincia;
    private String strCiudad;
    private String strParroquia;
    ////Telefonos
    private List listaTiposTelefono;
    private List<GthTelefono> listaTelefonos; //lista de telefonos del usuario
    private GthTelefono telefonoNuevo;
    private String telefonoEliminado;
    //Correos
    private GthCorreo correoPersonal;
    private GthCorreo correoInstitucional;
    //Cargas Familiares
    private List<GthCargasFamiliares> listaCargasFamiliares;
    private List listaTipoParentesco;
    private List listaTipoDocumento;
    private List listaGenero;
    private GthCargasFamiliares cargaNueva;
    private String cargaEliminada;
    //Conyugue
    private List listaEstadoCivil;
    private GthConyuge conyugue;
    private GthTelefono conyugueTelefono;
    private GthUnionLibre conyugueUnionLibre;
    private List listaActividadLaboral;
    private List listaNacionalidad;
    private List listaCargos;

    @PostConstruct
    public void cargarDatos() {
        empleado = servicioEmpleado.getEmpleado(utilitario.getVariable("IDE_GTEMP"));
    	System.out.println( "empleado: "+ empleado+"");
        //Direccion
        direccion = servicioEmpleado.getDireccionEmpleado(empleado.getIdeGtemp().toString());
        if (direccion == null) {
            direccion = new GthDireccion();
            direccion.setIdeGtemp(empleado);
        } else {
            try {
                strParroquia = direccion.getIdeGedip().getIdeGedip() + "";
                strCiudad = direccion.getIdeGedip().getGenIdeGedip() + "";
                strProvincia = direccion.getIdeGedip().getGenIdeGedip() + "";
            } catch (Exception e) {
            }

        }
        listaPaises = servicioEmpleado.getPaises();
        listaProvincias = servicioEmpleado.getProvincias(strPais);
        listaCiudades = servicioEmpleado.getCiudades(strProvincia);
        listaParroquias = servicioEmpleado.getParroquias(strCiudad);

        //telefonos
        listaTiposTelefono = servicioEmpleado.getTiposTelefono();
        listaTelefonos = servicioEmpleado.getTelefonos(empleado.getIdeGtemp().toString());
        if (listaTelefonos == null) {
            listaTelefonos = new ArrayList<GthTelefono>();
        }

        telefonoNuevo = new GthTelefono();
        telefonoNuevo.setIdeGtemp(empleado);
        telefonoNuevo.setIdeGttit(new GthTipoTelefono());

        //Correos
        correoInstitucional = servicioEmpleado.getCorreoInstitucional(empleado.getIdeGtemp().toString());
        if (correoInstitucional == null) {
            correoInstitucional = new GthCorreo();
            correoInstitucional.setNotificacionGtcor(new Boolean(true));
            correoInstitucional.setIdeGtemp(empleado);
            correoInstitucional.setActivoGtcor(new Boolean(true));
        }
        correoPersonal = servicioEmpleado.getCorreoPersonal(empleado.getIdeGtemp().toString());
        if (correoPersonal == null) {
            correoPersonal = new GthCorreo();
            correoPersonal.setNotificacionGtcor(new Boolean(false));
            correoPersonal.setIdeGtemp(empleado);
            correoPersonal.setActivoGtcor(new Boolean(true));
        }

        //Cargas familiares
        listaCargasFamiliares = servicioEmpleado.getCargasFamiliares(empleado.getIdeGtemp().toString());
        if (listaCargasFamiliares == null) {
            listaCargasFamiliares = new ArrayList<GthCargasFamiliares>();
        }
        listaTipoDocumento = servicioEmpleado.getTiposDocumentoIdentidad();
        listaTipoParentesco = servicioEmpleado.getTiposParentesco();
        listaGenero = servicioEmpleado.getGeneros();
        cargaNueva = new GthCargasFamiliares();
        cargaNueva.setIdeGtemp(empleado);
        cargaNueva.setIdeGtgen(new GthGenero());
        cargaNueva.setIdeGttpr(new GthTipoParentescoRelacion());
        cargaNueva.setIdeGttdi(new GthTipoDocumentoIdentidad());

        //conyugue
        listaEstadoCivil = servicioEmpleado.getEstadosCiviles();
        listaActividadLaboral = servicioEmpleado.getActividadesLaborales();
        listaNacionalidad = servicioEmpleado.getNacionalidades();
        listaCargos = servicioEmpleado.getCargos();

        conyugue = servicioEmpleado.getConyuque(empleado.getIdeGtemp().toString());
        if (conyugue == null) {
            conyugue = new GthConyuge();
        }
        conyugue.setIdeGtcar(conyugue.getIdeGtcar() == null ? new GthCargo() : conyugue.getIdeGtcar());
        conyugue.setIdeGtgen(conyugue.getIdeGtgen() == null ? new GthGenero() : conyugue.getIdeGtgen());
        conyugue.setIdeGtnac(conyugue.getIdeGtnac() == null ? new GthNacionalidad() : conyugue.getIdeGtnac());
        conyugue.setIdeGttdi(conyugue.getIdeGttdi() == null ? new GthTipoDocumentoIdentidad() : conyugue.getIdeGttdi());
        conyugue.setIdeGtemp(empleado);
        if (conyugue.getIdeGtcon() != null) {
            conyugueTelefono = servicioEmpleado.getTelefonoConyugue(conyugue.getIdeGtcon().toString());
        }
        if (conyugueTelefono == null) {
            conyugueTelefono = new GthTelefono();
            conyugueTelefono.setIdeGtcon(conyugue);
            conyugueTelefono.setIdeGttit(new GthTipoTelefono());
        }
        conyugueTelefono.setIdeGtcon(conyugue);
        if (conyugue.getIdeGtcon() != null) {
            conyugueUnionLibre = servicioEmpleado.getUnionLibre(conyugue.getIdeGtcon().toString());
        }
        if (conyugueUnionLibre == null) {
            conyugueUnionLibre = new GthUnionLibre();
        }
        conyugueUnionLibre.setIdeGtcon(conyugue);
    }

    public void actualizarEmpleado() {
        cargarDatos();
    }

    public void guardarConyugueUnionLibre(ActionEvent evt) {
          //Valido identificaci�n del conyuque
        if (validarDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString(), conyugue.getDocumentoIdentidadGtcon()) == false) {
            return;
        }
        
        if (empleado.getIdeGtesc() != null) {
            empleado.setIdeGtesc(servicioEmpleado.getEstadoCivil(empleado.getIdeGtesc().getIdeGtesc().toString()));
            if (empleado.getIdeGtesc().getIdeGtesc().toString().contains(strUnionLibre)) {
                conyugue.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString()));
                conyugue.setIdeGtgen(servicioEmpleado.getGenero(conyugue.getIdeGtgen().getIdeGtgen().toString()));
                conyugue.setIdeGtcar(conyugue.getIdeGtcar().getIdeGtcar() == null ? null : servicioEmpleado.getCargo(conyugue.getIdeGtcar().getIdeGtcar().toString()));
                conyugue.setIdeGtnac(conyugue.getIdeGtnac().getIdeGtnac() == null ? null : servicioEmpleado.getNacionalidad(conyugue.getIdeGtnac().getIdeGtnac().toString()));

                conyugueTelefono.setIdeGttit(servicioEmpleado.getTipoTelefono(conyugueTelefono.getIdeGttit().getIdeGttit().toString()));
                String str_mensaje = servicioEmpleado.guardarConyugueUnionLibre(conyugue, conyugueTelefono, conyugueUnionLibre);
                if (str_mensaje.isEmpty()) {
                    utilitario.agregarMensaje("Se guardo correctamente", "");
                    servicioEmpleado.guardarEmpleado(empleado);
                } else {
                    utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
                }
            }
        }
    }

    /**
     * @param ide_gttdi
     * @param documento_identidad_gttdi
     * @return
     *
     * metodo booleano para validar el tipo de documento de identidad cedula y
     * ruc
     */
    private boolean validarDocumentoIdentidad(String ide_gttdi, String documento_identidad) {
        if (ide_gttdi != null && !ide_gttdi.isEmpty()) {
            if (documento_identidad != null && !documento_identidad.isEmpty()) {
                if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_cedula"))) {
                    if (!utilitario.validarCedula(documento_identidad)) {
                        utilitario.agregarMensajeInfo("Atencion", "El n�mero de cedula ingresado no es valido");
                        return false;
                    }
                } else if (ide_gttdi.equals(utilitario.getVariable("p_gth_tipo_documento_ruc"))) {
                    if (!utilitario.validarRUC(documento_identidad)) {
                        utilitario.agregarMensajeInfo("Atencion", "El n�mero de RUC ingresado no es valido");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void guardarConyugue(ActionEvent evt) {
        //Valido identificaci�n del conyuque
        if (validarDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString(), conyugue.getDocumentoIdentidadGtcon()) == false) {
            return;
        }

        if (empleado.getIdeGtesc() != null) {
            empleado.setIdeGtesc(servicioEmpleado.getEstadoCivil(empleado.getIdeGtesc().getIdeGtesc().toString()));
            if (empleado.getIdeGtesc().getIdeGtesc().toString().contains(strSoltero)) {
                //Soltero
                String str_mensaje = servicioEmpleado.eliminarConyugue(empleado.getIdeGtemp().toString());
                if (str_mensaje.isEmpty()) {
                    servicioEmpleado.guardarEmpleado(empleado);
                } else {
                    utilitario.agregarMensajeInfo("No puede cambiar su estado civila a Soltero", str_mensaje);
                }
            } else if (empleado.getIdeGtesc().getIdeGtesc().toString().contains(strCasado)) {
                conyugue.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(conyugue.getIdeGttdi().getIdeGttdi().toString()));
                conyugue.setIdeGtgen(servicioEmpleado.getGenero(conyugue.getIdeGtgen().getIdeGtgen().toString()));
                conyugue.setIdeGtcar(conyugue.getIdeGtcar().getIdeGtcar() == null ? null : servicioEmpleado.getCargo(conyugue.getIdeGtcar().getIdeGtcar().toString()));
                conyugue.setIdeGtnac(conyugue.getIdeGtnac().getIdeGtnac() == null ? null : servicioEmpleado.getNacionalidad(conyugue.getIdeGtnac().getIdeGtnac().toString()));
                conyugueTelefono.setIdeGttit(servicioEmpleado.getTipoTelefono(conyugueTelefono.getIdeGttit().getIdeGttit().toString()));
                String str_mensaje = servicioEmpleado.guardarConyugue(conyugue, conyugueTelefono);
                if (str_mensaje.isEmpty()) {
                    utilitario.agregarMensaje("Se guardo correctamente", "");
                    servicioEmpleado.guardarEmpleado(empleado);
                } else {
                    utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
                }
            } else {
                String str_mensaje = servicioEmpleado.guardarEmpleado(empleado);
                if (str_mensaje.isEmpty()) {
                    utilitario.agregarMensaje("Se guardo correctamente", "");
                } else {
                    utilitario.agregarMensajeError("No se pudo guardar", str_mensaje);
                }
            }
        }
    }

    public void guardarDireccion(ActionEvent evt) {
        direccion.setIdeGedip(servicioEmpleado.getDivisionPolitica(strParroquia));        //Asigna la parroquia
        direccion.setNotificacionGtdir(new Boolean(true));
        String str_mensaje = servicioEmpleado.guardarDireccion(direccion);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
        } else {
            utilitario.agregarMensajeInfo("No se puede guardar", str_mensaje);
        }
    }

    public void guardarCorreo(ActionEvent evt) {
        String str_mensaje = servicioEmpleado.guardarCorreo(correoInstitucional, correoPersonal);
        if (str_mensaje.isEmpty()) {
            utilitario.agregarMensaje("Se guardo Correctamente", "");
        } else {
            utilitario.agregarMensaje("No se pudo guardar", str_mensaje);
        }
    }

    public void eliminarTelefono() {
        if (telefonoEliminado != null) {
            //Borro de la base
            String str_mensaje = servicioEmpleado.eliminarTelefono(telefonoEliminado);
            if (str_mensaje.isEmpty()) {
                listaTelefonos = servicioEmpleado.getTelefonos(empleado.getIdeGtemp().toString());
                telefonoEliminado = null;
            } else {
                utilitario.agregarMensajeError("No se puede eliminar el tel�fono", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un tel�fono ", "");
        }
    }

    public void modificarTelefono(RowEditEvent evt) {
        GthTelefono telefonoSeleccionado = (GthTelefono) evt.getObject();
        telefonoSeleccionado.setIdeGttit(servicioEmpleado.getTipoTelefono(telefonoSeleccionado.getIdeGttit().getIdeGttit().toString()));// por si modifica en la tabla                                 
        String str_mensaje = servicioEmpleado.guardarTelefono(telefonoSeleccionado);
        if (!str_mensaje.isEmpty()) {
            utilitario.agregarMensajeError("No se puede modificar el tel�fono", str_mensaje);
        }
    }

    public void modificarCarga(RowEditEvent evt) {
        GthCargasFamiliares cargaSeleccionada = (GthCargasFamiliares) evt.getObject();
        cargaSeleccionada.setIdeGtgen(servicioEmpleado.getGenero(cargaSeleccionada.getIdeGtgen().getIdeGtgen().toString()));
        cargaSeleccionada.setIdeGttpr(servicioEmpleado.getTipoParentesco(cargaSeleccionada.getIdeGttpr().getIdeGttpr().toString()));
        cargaSeleccionada.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(cargaSeleccionada.getIdeGttdi().getIdeGttdi().toString()));

        String str_mensaje = servicioEmpleado.guardarCargaFamiliar(cargaSeleccionada);
        if (!str_mensaje.isEmpty()) {
            utilitario.agregarMensajeError("No se puede modificar el tel�fono", str_mensaje);
        }
    }

    public void agregarCargaFamiliar(ActionEvent evt) {
          //Valido identificaci�n del conyuque
        if (validarDocumentoIdentidad(cargaNueva.getIdeGttdi().getIdeGttdi().toString(),cargaNueva.getDocumentoIdentidadGtcaf()) == false) {
            return;
        }
        
        cargaNueva.setIdeGtgen(servicioEmpleado.getGenero(cargaNueva.getIdeGtgen().getIdeGtgen().toString()));
        cargaNueva.setIdeGttpr(servicioEmpleado.getTipoParentesco(cargaNueva.getIdeGttpr().getIdeGttpr().toString()));
        cargaNueva.setIdeGttdi(servicioEmpleado.getTipoDocumentoIdentidad(cargaNueva.getIdeGttdi().getIdeGttdi().toString()));
        cargaNueva.setActivoGtcaf(new Boolean(true));
        String str_mensaje = servicioEmpleado.guardarCargaFamiliar(cargaNueva);
        if (str_mensaje.isEmpty()) {
            listaCargasFamiliares.add(cargaNueva);
            cargaNueva = new GthCargasFamiliares();
            cargaNueva.setIdeGtgen(new GthGenero());
            cargaNueva.setIdeGtemp(empleado);
            cargaNueva.setIdeGttpr(new GthTipoParentescoRelacion());
            cargaNueva.setIdeGttdi(new GthTipoDocumentoIdentidad());
        } else {
            utilitario.agregarMensaje("No se puede agregar", str_mensaje);
        }
    }

    public void eliminarCargaFamiliar() {
        if (cargaEliminada != null) {
            //Borro de la base
            String str_mensaje = servicioEmpleado.eliminarCargaFamiliar(cargaEliminada);
            if (str_mensaje.isEmpty()) {
                listaCargasFamiliares = servicioEmpleado.getCargasFamiliares(empleado.getIdeGtemp().toString());
                cargaEliminada = null;
            } else {
                utilitario.agregarMensajeError("No se puede eliminar la carga Familiar", str_mensaje);
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una Carga Familiar ", "");
        }
    }

    public void agregarTelefono(ActionEvent evt) {
        telefonoNuevo.setIdeGttit(servicioEmpleado.getTipoTelefono(telefonoNuevo.getIdeGttit().getIdeGttit().toString()));
        telefonoNuevo.setActivoGttel(new Boolean(true));
        String str_mensaje = servicioEmpleado.guardarTelefono(telefonoNuevo);
        if (str_mensaje.isEmpty()) {
            listaTelefonos.add(telefonoNuevo);
            telefonoNuevo = new GthTelefono();
            telefonoNuevo.setIdeGtemp(empleado);
            telefonoNuevo.setIdeGttit(new GthTipoTelefono());
        } else {
            utilitario.agregarMensaje("No se puede agregar", str_mensaje);
        }
    }

    public void filtrarProvincias(AjaxBehaviorEvent evt) {
        if (strPais != null) {
            listaProvincias = servicioEmpleado.getProvincias(strPais);
        } else {
            listaProvincias = null;
        }
        listaCiudades = null;
        listaParroquias = null;
        strCiudad = null;
        strParroquia = null;
    }

    public void filtrarCiudades(AjaxBehaviorEvent evt) {
        if (strProvincia != null) {
            listaCiudades = servicioEmpleado.getCiudades(strProvincia);
        } else {
            listaCiudades = null;
        }
        listaParroquias = null;
        strParroquia = null;
    }

    public void filtrarParroquias(AjaxBehaviorEvent evt) {
        if (strCiudad != null) {
            listaParroquias = servicioEmpleado.getParroquias(strCiudad);
        } else {
            listaParroquias = null;
        }
    }

    public void subirFoto(FileUploadEvent event) {
        try {
            String str_nombre = utilitario.getVariable("NICK") + "_foto" + event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."), event.getFile().getFileName().length());
            str_nombre = str_nombre.toLowerCase();
            String str_path = utilitario.getPropiedad("rutaUpload") + "/fotos";
            File path = new File(str_path);
            path.mkdirs();//Creo el Directorio
            File result = new File(str_path + "/" + str_nombre);
            ///Para el .war 
            ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
            str_path = extContext.getRealPath("/upload/fotos");
            //empleado.setPathFotoGtemp("/upload/fotos/" + str_nombre);
            path = new File(str_path);
            path.mkdirs();//Creo el Directorio
            File result1 = new File(str_path + "/" + str_nombre);


            int BUFFER_SIZE = 6124;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(result);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bulk;
                InputStream inputStream = event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(result1);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bulk;
                InputStream inputStream = event.getFile().getInputstream();
                while (true) {
                    bulk = inputStream.read(buffer);
                    if (bulk < 0) {
                        break;
                    }
                    fileOutputStream.write(buffer, 0, bulk);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();

                //Guardo la foto si se subio correctamente  
                String str_msj = servicioEmpleado.guardarEmpleado(empleado);
                if (str_msj.isEmpty()) {
                    utilitario.agregarMensaje("Se Guardo Correctamente", "");
                } else {
                    utilitario.agregarMensajeError("No se pudo Guardar", str_msj);
                }
                //Recargar la pagina para que se cambie la foto
                FacesContext.getCurrentInstance().getExternalContext().redirect("datosEmpleado.jsf");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public GthEmpleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(GthEmpleado empleado) {
        this.empleado = empleado;
    }

    public GthDireccion getDireccion() {
        return direccion;
    }

    public List getListaPaises() {
        return listaPaises;
    }

    public void setListaPaises(List listaPaises) {
        this.listaPaises = listaPaises;
    }

    public List getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public List getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public void setDireccion(GthDireccion direccion) {
        this.direccion = direccion;
    }

    public String getStrPais() {
        return strPais;
    }

    public void setStrPais(String strPais) {
        this.strPais = strPais;
    }

    public String getStrProvincia() {
        return strProvincia;
    }

    public void setStrProvincia(String strProvincia) {
        this.strProvincia = strProvincia;
    }

    public String getStrCiudad() {
        return strCiudad;
    }

    public void setStrCiudad(String strCiudad) {
        this.strCiudad = strCiudad;
    }

    public List getListaParroquias() {
        return listaParroquias;
    }

    public void setListaParroquias(List listaParroquias) {
        this.listaParroquias = listaParroquias;
    }

    public String getStrParroquia() {
        return strParroquia;
    }

    public void setStrParroquia(String strParroquia) {
        this.strParroquia = strParroquia;
    }

    public List getListaTiposTelefono() {
        return listaTiposTelefono;
    }

    public void setListaTiposTelefono(List listaTiposTelefono) {
        this.listaTiposTelefono = listaTiposTelefono;
    }

    public List<GthTelefono> getListaTelefonos() {
        return listaTelefonos;
    }

    public void setListaTelefonos(List<GthTelefono> listaTelefonos) {
        this.listaTelefonos = listaTelefonos;
    }

    public GthTelefono getTelefonoNuevo() {
        return telefonoNuevo;
    }

    public void setTelefonoNuevo(GthTelefono telefonoNuevo) {
        this.telefonoNuevo = telefonoNuevo;
    }

    public GthCorreo getCorreoPersonal() {
        return correoPersonal;
    }

    public void setCorreoPersonal(GthCorreo correoPersonal) {
        this.correoPersonal = correoPersonal;
    }

    public GthCorreo getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(GthCorreo correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getStrOpcion() {
        return strOpcion;
    }

    public void setStrOpcion(String strOpcion) {
        this.strOpcion = strOpcion;
    }

    public List<GthCargasFamiliares> getListaCargasFamiliares() {
        return listaCargasFamiliares;
    }

    public void setListaCargasFamiliares(List<GthCargasFamiliares> listaCargasFamiliares) {
        this.listaCargasFamiliares = listaCargasFamiliares;
    }

    public List getListaTipoParentesco() {
        return listaTipoParentesco;
    }

    public void setListaTipoParentesco(List listaTipoParentesco) {
        this.listaTipoParentesco = listaTipoParentesco;
    }

    public List getListaTipoDocumento() {
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public List getListaGenero() {
        return listaGenero;
    }

    public void setListaGenero(List listaGenero) {
        this.listaGenero = listaGenero;
    }

    public GthCargasFamiliares getCargaNueva() {
        return cargaNueva;
    }

    public void setCargaNueva(GthCargasFamiliares cargaNueva) {
        this.cargaNueva = cargaNueva;
    }

    public String getTelefonoEliminado() {
        return telefonoEliminado;
    }

    public void setTelefonoEliminado(String telefonoEliminado) {
        this.telefonoEliminado = telefonoEliminado;
    }

    public String getCargaEliminada() {
        return cargaEliminada;
    }

    public void setCargaEliminada(String cargaEliminada) {
        this.cargaEliminada = cargaEliminada;
    }

    public List getListaEstadoCivil() {
        return listaEstadoCivil;
    }

    public void setListaEstadoCivil(List listaEstadoCivil) {
        this.listaEstadoCivil = listaEstadoCivil;
    }

    public String getStrCasado() {
        return strCasado;
    }

    public void setStrCasado(String strCasado) {
        this.strCasado = strCasado;
    }

    public String getStrUnionLibre() {
        return strUnionLibre;
    }

    public void setStrUnionLibre(String strUnionLibre) {
        this.strUnionLibre = strUnionLibre;
    }

    public GthConyuge getConyugue() {
        return conyugue;
    }

    public void setConyugue(GthConyuge conyugue) {
        this.conyugue = conyugue;
    }

    public List getListaActividadLaboral() {
        return listaActividadLaboral;
    }

    public void setListaActividadLaboral(List listaActividadLaboral) {
        this.listaActividadLaboral = listaActividadLaboral;
    }

    public List getListaNacionalidad() {
        return listaNacionalidad;
    }

    public void setListaNacionalidad(List listaNacionalidad) {
        this.listaNacionalidad = listaNacionalidad;
    }

    public List getListaCargos() {
        return listaCargos;
    }

    public void setListaCargos(List listaCargos) {
        this.listaCargos = listaCargos;
    }

    public String getStrSoltero() {
        return strSoltero;
    }

    public void setStrSoltero(String strSoltero) {
        this.strSoltero = strSoltero;
    }

    public GthTelefono getConyugueTelefono() {
        return conyugueTelefono;
    }

    public void setConyugueTelefono(GthTelefono conyugueTelefono) {
        this.conyugueTelefono = conyugueTelefono;
    }

    public GthUnionLibre getConyugueUnionLibre() {
        return conyugueUnionLibre;
    }

    public void setConyugueUnionLibre(GthUnionLibre conyugueUnionLibre) {
        this.conyugueUnionLibre = conyugueUnionLibre;
    }
}
