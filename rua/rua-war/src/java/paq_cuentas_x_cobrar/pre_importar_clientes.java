/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import framework.componentes.Upload;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.component.panel.Panel;
import org.primefaces.event.FileUploadEvent;
import servicios.cuentas_x_cobrar.ServicioCliente;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_importar_clientes extends Pantalla {

    @EJB
    private final ServicioCliente ser_cliente = (ServicioCliente) utilitario.instanciarEJB(ServicioCliente.class);

    private Upload upl_importa = new Upload();
    private Texto tex_columna = new Texto();
    private Texto tex_inicio = new Texto();
    private Texto tex_numero_hoja = new Texto();
    private Texto tex_fin = new Texto();
    private Tabla tab_detalle = new Tabla();

    public pre_importar_clientes() {

        Panel pan = new Panel();
        pan.setStyle("width:100%;");

        Grid gri_archivo = new Grid();
        gri_archivo.setWidth("95%");
        gri_archivo.setColumns(3);

        Grid gri_matriz = new Grid();
        gri_matriz.setMensajeInfo("Seleccione un archivo Excel con extensión <strong>.xls<strong>");
        gri_matriz.setStyle("width:100%;");
        gri_matriz.setColumns(4);

        gri_matriz.getChildren().add(new Etiqueta("Número o Nombre de la Hoja"));
        tex_numero_hoja.setValue("1");
        gri_matriz.getChildren().add(tex_numero_hoja);
        gri_matriz.getChildren().add(new Etiqueta("Número de Fila con los Nombres de las Columnas"));
        tex_columna.setSize(5);
        tex_columna.setValue("1");
        tex_columna.setSoloEnteros();
        gri_matriz.getChildren().add(tex_columna);

        gri_matriz.getChildren().add(new Etiqueta("Número de la Primera Fila de Datos "));
        tex_inicio.setSize(5);
        tex_inicio.setValue("2");
        tex_inicio.setSoloEnteros();
        gri_matriz.getChildren().add(tex_inicio);
        gri_matriz.getChildren().add(new Etiqueta("Número de la Última Fila de Datos"));
        tex_fin.setId("tex_fin");
        tex_fin.setSize(5);
        tex_fin.setSoloEnteros();
        gri_matriz.getChildren().add(tex_fin);

        gri_archivo.getChildren().add(gri_matriz);

        Grid gri_valida = new Grid();
        gri_valida.setId("gri_valida");
        gri_valida.setColumns(3);
        Etiqueta eti_valida = new Etiqueta();
        eti_valida.setValueExpression("value", "pre_index.clase.upl_importa.nombreReal");
        eti_valida.setValueExpression("rendered", "pre_index.clase.upl_importa.nombreReal != null");
        gri_valida.getChildren().add(eti_valida);

        Imagen ima_valida = new Imagen();
        ima_valida.setWidth("22");
        ima_valida.setHeight("22");
        ima_valida.setValue("/imagenes/im_excel.gif");
        ima_valida.setValueExpression("rendered", "pre_index.clase.upl_importa.nombreReal != null");
        gri_valida.getChildren().add(ima_valida);
        gri_archivo.setFooter(gri_valida);

        upl_importa.setId("upl_importa");
        upl_importa.setUpdate("gri_valida");
        upl_importa.setAllowTypes("/(\\.|\\/)(xls)$/");
        upl_importa.setMetodo("seleccionarArchivo");
        upl_importa.setProcess("@all");
        upl_importa.setUploadLabel("Validar Archivo .xls");
        upl_importa.setAuto(false);

        gri_matriz.setFooter(upl_importa);
        pan.setHeader("Configurar Archivo");

        pan.getChildren().add(gri_archivo);
        agregarComponente(pan);

        tab_detalle.setId("tab_detalle");
        tab_detalle.setSql(ser_cliente.getSqlClientesImportarArchivo());
        tab_detalle.setScrollable(true);
        tab_detalle.setRows(15);
        //tab_detalle.setValueExpression("rowStyleClass", "fila.campos[5]  eq null ? null :  'text-red'"); //pinta de rojo la fila si hay observación

        tab_detalle.setLectura(true);
        tab_detalle.dibujar();
        tab_detalle.setLectura(false);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_detalle);
        Grupo gri = new Grupo();

        gri.getChildren().add(new Etiqueta("<div align='center'>"));

        Boton bot_subir = new Boton();
        bot_subir.setValue("Subir Clientes");
        bot_subir.setMetodo("crearClientes");
        gri.getChildren().add(bot_subir);
        gri.getChildren().add(new Etiqueta("</div>"));
        pat_panel.setFooter(gri);

        agregarComponente(pat_panel);

    }

    public void seleccionarArchivo(FileUploadEvent event) {
        try {
            Workbook archivoExcel = Workbook.getWorkbook(event.getFile().getInputstream());
            Sheet hoja = null;
            int int_numero_hoja = -1;
            int int_inicio = -1;
            int int_fin = -1;
            int int_numColumnas = -1;
            List lis_campos_excel = new ArrayList();
//Abro la hoja indicada
            System.out.println("xx//////--- " + tex_numero_hoja.getValue());
            try {
                int_numero_hoja = Integer.parseInt(tex_numero_hoja.getValue() + "");
            } catch (Exception e) {
                int_numero_hoja = -1;
            }
            System.out.println(".... " + int_numero_hoja);
            if (int_numero_hoja == -1) {
                String[] hojas = archivoExcel.getSheetNames();
                for (int i = 0; i < hojas.length; i++) {
                    if (hojas[i].equalsIgnoreCase(tex_numero_hoja.getValue() + "")) {
                        hoja = archivoExcel.getSheet(i);
                        break;
                    }
                }
            } else {
                hoja = archivoExcel.getSheet(int_numero_hoja - 1);
            }

            if (hoja == null) {
                utilitario.agregarMensajeError("La Hoja: " + tex_numero_hoja.getValue() + " no existe en el Archivo seleccionado", "");
                return;
            }

            int_numColumnas = hoja.getColumns();

            try {
                int_inicio = Integer.parseInt(tex_inicio.getValue() + "");
                int_inicio = int_inicio - 1;
            } catch (Exception e) {
                int_inicio = -1;
            }

            try {
                int_fin = Integer.parseInt(tex_fin.getValue() + "");
            } catch (Exception e) {
                int_fin = -1;
            }

            if (tex_fin.getValue() == null || tex_fin.getValue().toString().isEmpty()) {
                //Si no hay valor en fin le pongo a el total de filas de la Hoja
                int_fin = hoja.getRows();
                tex_fin.setValue(String.valueOf(int_fin));
                utilitario.addUpdate("tex_fin");
            }

            if ((int_inicio == -1 || int_fin == -1) || (int_inicio > int_fin)) {
                utilitario.agregarMensajeError("Los valores ingresados en Primera o Última fila de datos no son válidos", "");
                return;
            }

            int int_fila_columna = -1;

            try {
                int_fila_columna = Integer.parseInt(tex_columna.getValue() + "");
                int_fila_columna = int_fila_columna - 1;
            } catch (Exception e) {
                int_fila_columna = -1;
            }

            if (int_fila_columna == -1) {
                utilitario.agregarMensajeError("El Número de Fila con los Nombres de las Columnas no es válido", "");
                return;
            }

            //Recupera titulos de las columnas
            for (int i = 0; i < int_numColumnas; i++) {
                try {
                    Object[] obj = new Object[2];
                    obj[0] = i;
                    obj[1] = hoja.getCell(i, int_fila_columna).getContents();
                    //System.out.println(obj[0] + " " + obj[1]);
                    lis_campos_excel.add(obj);
                } catch (Exception e) {
                    System.out.println("Fallo al leer columnas del archivo xls :" + e.getMessage());
                }
            }
            //Recorre filas e inserta en la tabla recorre de manera inversa el archivo
            for (int fila = (int_fin - 1); fila >= int_inicio; fila--) {
                String CODIGO = hoja.getCell(0, fila).getContents();
                String ESTADO = hoja.getCell(1, fila).getContents();
                String UBICACION = hoja.getCell(2, fila).getContents();
                String TIPO_CLIENTE = hoja.getCell(3, fila).getContents();
                String NOMBRE_COMPLETO = hoja.getCell(4, fila).getContents();
                String NOMBRE = hoja.getCell(5, fila).getContents();
                String TIPO_CONTRIBUYENTE = hoja.getCell(6, fila).getContents();
                String TIPO_IDENTIFICACION = hoja.getCell(7, fila).getContents();
                String IDENTIFICACION = hoja.getCell(8, fila).getContents();
                String CONTACTO = hoja.getCell(9, fila).getContents();
                String DIRECCION = hoja.getCell(10, fila).getContents();
                String TELEFONO1 = hoja.getCell(11, fila).getContents();
                String TELEFONO2 = hoja.getCell(12, fila).getContents();
                String FAX = hoja.getCell(13, fila).getContents();
                String MOVIL = hoja.getCell(14, fila).getContents();
                String CORREO = hoja.getCell(15, fila).getContents();
                String OBSERVACION = hoja.getCell(16, fila).getContents();
                String SALDO_INICIAL = hoja.getCell(17, fila).getContents();
                String CUENTA_CONTABLE = hoja.getCell(18, fila).getContents();
                tab_detalle.insertar();

                tab_detalle.setValor("CODIGO", CODIGO);
                tab_detalle.setValor("ESTADO", ESTADO);
                tab_detalle.setValor("UBICACION", UBICACION);
                tab_detalle.setValor("TIPO_CLIENTE", TIPO_CLIENTE);
                tab_detalle.setValor("NOMBRE_COMPLETO", NOMBRE_COMPLETO);
                tab_detalle.setValor("NOMBRE", NOMBRE);
                tab_detalle.setValor("TIPO_CONTRIBUYENTE", TIPO_CONTRIBUYENTE);
                tab_detalle.setValor("TIPO_IDENTIFICACION", TIPO_IDENTIFICACION);
                tab_detalle.setValor("IDENTIFICACION", IDENTIFICACION);
                tab_detalle.setValor("CONTACTO", CONTACTO);
                tab_detalle.setValor("DIRECCION", DIRECCION);
                tab_detalle.setValor("TELEFONO1", TELEFONO1);
                tab_detalle.setValor("TELEFONO2", TELEFONO2);
                tab_detalle.setValor("FAX", FAX);
                tab_detalle.setValor("MOVIL", MOVIL);
                tab_detalle.setValor("CORREO", CORREO);
                tab_detalle.setValor("OBSERVACION", OBSERVACION);
                tab_detalle.setValor("SALDO_INICIAL", utilitario.getFormatoNumero(SALDO_INICIAL.replace(",", ".")));
                tab_detalle.setValor("CUENTA_CONTABLE", CUENTA_CONTABLE);

            }
            upl_importa.setNombreReal(event.getFile().getFileName());
            utilitario.addUpdate("gri_valida,tab_detalle");
        } catch (Exception e) {
            e.printStackTrace();
            utilitario.agregarMensajeError("No se puede abrir el archivo", e.getMessage());
        }
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

    public void crearClientes() {
        if (tab_detalle.isEmpty() == false) {

            TablaGenerica tab_cod = new TablaGenerica();
            tab_cod.setTabla("gen_persona", "ide_geper", -1);
            tab_cod.setCondicion("ide_geper=-1");
            tab_cod.setGenerarPrimaria(false);
            tab_cod.ejecutarSql();
            tab_cod.getColumna("ide_geper").setExterna(false);
            tab_cod.limpiar();
            //Busca si existen en la base de datos algun cliente
            String identificaciones = tab_detalle.getStringColumna("IDENTIFICACION");
            TablaGenerica tab_bus_clie = utilitario.consultar("SELECT ide_geper,identificac_geper FROM gen_persona where identificac_geper in (" + identificaciones + ")");
            long int_maximo_cliente = utilitario.getConexion().getMaximo("gen_persona", "ide_geper", tab_detalle.getTotalFilas());

            //saldo inicial
            TablaGenerica tab_cabcxc = new TablaGenerica();
            tab_cabcxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
            tab_cabcxc.setCondicion("ide_ccctr=-1");
            tab_cabcxc.setGenerarPrimaria(false);
            tab_cabcxc.getColumna("ide_ccctr").setExterna(false);
            tab_cabcxc.ejecutarSql();
            long int_maximo_cab = utilitario.getConexion().getMaximo("cxc_cabece_transa", "ide_ccctr", tab_detalle.getTotalFilas());

            TablaGenerica tab_detcxc = new TablaGenerica();
            tab_detcxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
            tab_detcxc.setCondicion("ide_ccdtr=-1");
            tab_detcxc.setGenerarPrimaria(false);
            tab_detcxc.getColumna("ide_ccdtr").setExterna(false);
            tab_detcxc.ejecutarSql();
            long int_maximo_det = utilitario.getConexion().getMaximo("cxc_detall_transa", "ide_ccdtr", tab_detalle.getTotalFilas());

            for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                String identifActual = tab_detalle.getValor(i, "IDENTIFICACION");
                boolean existe = false;
                for (int j = 0; j < tab_bus_clie.getTotalFilas(); i++) {
                    if (identifActual.equals(tab_bus_clie.getValor(j, "identificac_geper"))) {
                        existe = true;
                        break;
                    }
                }
                //Si no existe inserta el cliente
                if (existe == false) {
                    if (tab_detalle.getValor(i, "NOMBRE") == null || tab_detalle.getValor(i, "NOMBRE").isEmpty()) {
                        continue;
                    }

                    String CODIGO = tab_detalle.getValor(i, "CODIGO");
                    String ESTADO = tab_detalle.getValor(i, "ESTADO");
                    String UBICACION = tab_detalle.getValor(i, "UBICACION");
                    String TIPO_CLIENTE = tab_detalle.getValor(i, "TIPO_CLIENTE");
                    String NOMBRE_COMPLETO = tab_detalle.getValor(i, "NOMBRE_COMPLETO");
                    String NOMBRE = tab_detalle.getValor(i, "NOMBRE");
                    String TIPO_CONTRIBUYENTE = tab_detalle.getValor(i, "TIPO_CONTRIBUYENTE");
                    String TIPO_IDENTIFICACION = tab_detalle.getValor(i, "TIPO_IDENTIFICACION");
                    String IDENTIFICACION = tab_detalle.getValor(i, "IDENTIFICACION");
                    String CONTACTO = tab_detalle.getValor(i, "CONTACTO");
                    String DIRECCION = tab_detalle.getValor(i, "DIRECCION");
                    String TELEFONO1 = tab_detalle.getValor(i, "TELEFONO1");
                    String TELEFONO2 = tab_detalle.getValor(i, "TELEFONO2");
                    String FAX = tab_detalle.getValor(i, "FAX");
                    String MOVIL = tab_detalle.getValor(i, "MOVIL");
                    String CORREO = tab_detalle.getValor(i, "CORREO");
                    String OBSERVACION = tab_detalle.getValor(i, "OBSERVACION");
                    String SALDO_INICIAL = tab_detalle.getValor(i, "SALDO_INICIAL");
                    String CUENTA_CONTABLE = tab_detalle.getValor(i, "CUENTA_CONTABLE");

                    tab_cod.insertar();
                    tab_cod.setValor("ide_geper", String.valueOf(int_maximo_cliente));
                    tab_cod.setValor("nom_geper", NOMBRE);
                    tab_cod.setValor("identificac_geper", IDENTIFICACION);
                    tab_cod.setValor("nombre_compl_geper", NOMBRE_COMPLETO);
                    tab_cod.setValor("direccion_geper", DIRECCION);
                    tab_cod.setValor("telefono_geper", TELEFONO1);
                    tab_cod.setValor("fax_geper", FAX);
                    tab_cod.setValor("", MOVIL);
                    tab_cod.setValor("contacto_geper", CONTACTO);
                    tab_cod.setValor("correo_geper", CORREO);
                    tab_cod.setValor("observacion_geper", OBSERVACION);
                    tab_cod.setValor("nivel_geper", "HIJO");
                    tab_cod.setValor("es_cliente_geper", "true");
                    tab_cod.setValor("fecha_ingre_geper", utilitario.getFechaActual());
                    tab_cod.setValor("codigo_geper", CODIGO);
                    tab_cod.setValor("gen_ide_geper", "");  //?
                    tab_cod.setValor("ide_empr", utilitario.getVariable("ide_empr"));
                    tab_cod.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
                    tab_cod.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                    String ide_getid = "0"; //CEDULA
                    if (TIPO_IDENTIFICACION.equalsIgnoreCase("RUC")) {
                        ide_getid = "1";//RUC
                    } else if (TIPO_IDENTIFICACION.equalsIgnoreCase("PASAPORTE")) {
                        ide_getid = "2";//PASAPORTE    
                    }
                    tab_cod.setValor("ide_getid", ide_getid);

                    double dou_saldo_inicial = 0;
                    try {
                        dou_saldo_inicial = Double.parseDouble(SALDO_INICIAL);
                    } catch (Exception e) {
                    }
                    //saldo inicial
                    if (dou_saldo_inicial != 0) {
                        //Creo la transaccion
                        tab_cabcxc.insertar();
                        tab_cabcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
                        tab_cabcxc.setValor("ide_ccttr", "13");
                        tab_cabcxc.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
                        tab_cabcxc.setValor("ide_empr", utilitario.getVariable("ide_empr"));
                        tab_cabcxc.setValor("ide_geper", String.valueOf(int_maximo_cliente));
                        tab_cabcxc.setValor("fecha_trans_ccctr", utilitario.getFechaActual());
                        tab_cabcxc.setValor("observacion_ccctr", "Saldo inicial al " + utilitario.getFechaActual());

                        tab_detcxc.insertar();
                        tab_detcxc.setValor("ide_ccdtr", String.valueOf(int_maximo_det));
                        tab_detcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
                        tab_detcxc.setValor("ide_ccttr", "13");
                        tab_detcxc.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
                        tab_detcxc.setValor("ide_empr", utilitario.getVariable("ide_empr"));
                        tab_detcxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                        tab_detcxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
                        tab_detcxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
                        tab_detcxc.setValor("numero_pago_ccdtr", "0");
                        tab_detcxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(dou_saldo_inicial));
                        tab_detcxc.setValor("observacion_ccdtr", "Saldo inicial al " + utilitario.getFechaActual());

                        int_maximo_cab++;
                        int_maximo_det++;

                    }

                }
                int_maximo_cliente++;
            }
            tab_cod.guardar();
            tab_cabcxc.guardar();
            tab_detcxc.guardar();
            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                if (tab_cod.getTotalFilas() > 0) {
                    utilitario.agregarMensaje("Se importaron correctamente ", tab_cod.getTotalFilas() + " CLIENTES.");
                }
            }
        } else {
            utilitario.agregarMensajeInfo("No existen clientes a importar", "");
        }
    }

    public Upload getUpl_importa() {
        return upl_importa;
    }

    public void setUpl_importa(Upload upl_importa) {
        this.upl_importa = upl_importa;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

}
