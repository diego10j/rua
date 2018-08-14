/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_contabilidad;

import componentes.AsientoContable;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.Radio;
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
import servicios.contabilidad.ServicioComprobanteContabilidad;
import servicios.contabilidad.ServicioContabilidadGeneral;
import servicios.sistema.ServicioSistema;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_importa_asiento_normal extends Pantalla {

    @EJB
    private final ServicioContabilidadGeneral ser_contabilidad = (ServicioContabilidadGeneral) utilitario.instanciarEJB(ServicioContabilidadGeneral.class);
    @EJB
    private final ServicioSistema ser_sistema = (ServicioSistema) utilitario.instanciarEJB(ServicioSistema.class);
    @EJB
    private final ServicioComprobanteContabilidad ser_comp_contabilidad = (ServicioComprobanteContabilidad) utilitario.instanciarEJB(ServicioComprobanteContabilidad.class);

    private Upload upl_importa = new Upload();
    private Texto tex_columna = new Texto();
    private Texto tex_inicio = new Texto();
    private Texto tex_numero_hoja = new Texto();
    private Texto tex_fin = new Texto();

    private Tabla tab_detalle = new Tabla();

    private AsientoContable asc_asiento = new AsientoContable();

    public pre_importa_asiento_normal() {
        bar_botones.limpiar();

        Panel pan = new Panel();
        pan.setStyle("width:100%;");

        Grid gri_archivo = new Grid();
        gri_archivo.setWidth("95%");
        gri_archivo.setColumns(3);

        Etiqueta eti_sucu = new Etiqueta();
        eti_sucu.setStyle("font-size: 12px;");
        eti_sucu.setValue(ser_sistema.getSucursal().getValor("nom_sucu"));

        bar_botones.agregarComponente(eti_sucu);

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
        tab_detalle.setSql(ser_comp_contabilidad.getSqlDetalleAsiento("-1"));
        tab_detalle.getColumna("ide_cndcc").setVisible(false); //Aqui guardo el ide_cndpc
        tab_detalle.getColumna("codig_recur_cndpc").setNombreVisual("CÓDIGO CUENTA");
        tab_detalle.getColumna("nombre_cndpc").setNombreVisual("CUENTA");
        tab_detalle.setColumnaSuma("debe,haber");
        tab_detalle.getColumna("debe").alinearDerecha();
        tab_detalle.getColumna("debe").setLongitud(25);
        tab_detalle.getColumna("haber").alinearDerecha();
        tab_detalle.getColumna("haber").setLongitud(25);
        tab_detalle.getColumna("OBSERVACION_CNDCC").setVisible(false);
        tab_detalle.getColumna("OBSERVACION_CNDCC").setNombreVisual("ERROR");
        tab_detalle.setScrollable(true);
        tab_detalle.setRows(100);
        tab_detalle.setValueExpression("rowStyleClass", "fila.campos[5]  eq null ? null :  'text-red'"); //pinta de rojo la fila si hay observación
        tab_detalle.setScrollHeight(200); //300
        tab_detalle.setLectura(true);
        tab_detalle.dibujar();
        tab_detalle.setLectura(false);
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_detalle);
        agregarComponente(pat_panel);

        Grupo gri = new Grupo();
        gri.getChildren().add(new Etiqueta("<div align='center'>"));
        Boton bot_subir = new Boton();
        bot_subir.setValue("Generar Asiento Contable");
        bot_subir.setMetodo("subirAsiento");
        gri.getChildren().add(bot_subir);
        gri.getChildren().add(new Etiqueta("</div>"));
        pat_panel.setFooter(gri);
        asc_asiento.setId("asc_asiento");
        agregarComponente(asc_asiento);

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
            //  System.out.println("xx//////--- " + tex_numero_hoja.getValue());
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
                    System.out.println(obj[0] + " " + obj[1]);
                    lis_campos_excel.add(obj);
                } catch (Exception e) {
                    System.out.println("Fallo al leer columnas del archivo xls :" + e.getMessage());
                }
            }
            //Recorre filas e inserta en la tabla recorre de manera inversa el archivo
            for (int fila = (int_fin - 1); fila >= int_inicio; fila--) {
                String codig_recur_cndpc = hoja.getCell(0, fila).getContents();
                String nombre_cndpc = hoja.getCell(1, fila).getContents();
                if (nombre_cndpc == null || nombre_cndpc.isEmpty()) {
                    continue;
                }
                String debe = hoja.getCell(2, fila).getContents();
                String haber = hoja.getCell(3, fila).getContents();
                tab_detalle.insertar();
                if (codig_recur_cndpc.endsWith(".") == false) {
                    codig_recur_cndpc = codig_recur_cndpc + ".";
                }
                tab_detalle.setValor("codig_recur_cndpc", codig_recur_cndpc);
                tab_detalle.setValor("nombre_cndpc", nombre_cndpc);
                if (utilitario.getFormatoNumero(debe.replace(",", ".")) != null) {
                    if (!utilitario.getFormatoNumero(debe.replace(",", ".")).equals("0.00")) {
                        tab_detalle.setValor("debe", utilitario.getFormatoNumero(debe.replace(",", ".")));
                    }
                }
                if (utilitario.getFormatoNumero(haber.replace(",", ".")) != null) {
                    if (!utilitario.getFormatoNumero(haber.replace(",", ".")).equals("0.00")) {
                        tab_detalle.setValor("haber", utilitario.getFormatoNumero(haber.replace(",", ".")));
                    }
                }
            }
            tab_detalle.sumarColumnas();
            upl_importa.setNombreReal(event.getFile().getFileName());
            utilitario.addUpdate("gri_valida,tab_detalle");
        } catch (Exception e) {
            e.printStackTrace();
            utilitario.agregarMensajeError("No se puede abrir el archivo", e.getMessage());
        }
    }

    public void subirAsiento() {
        if (!tab_detalle.isEmpty()) {
//            if (tab_detalle.getSumaColumna("debe") != tab_detalle.getSumaColumna("haber")) {
//                utilitario.agregarMensajeError("El Asiento no esta cuadrado", "Verificar la sumatoria del DEBE y del HABER");
//                return;
//            }

            String str_codigos = tab_detalle.getStringColumna("codig_recur_cndpc");
            TablaGenerica tab_cuentas = ser_contabilidad.getCuentaporCodigo(str_codigos);
            if (tab_cuentas.getTotalFilas() > 0) {
                //Busca ide_cndpc
                boolean correcto = true;
                for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                    String codig_recur_cndpc = tab_detalle.getValor(i, "codig_recur_cndpc");
                    if (codig_recur_cndpc != null && codig_recur_cndpc.isEmpty() == false) {
                        String ide_cndpc = null;
                        for (int j = 0; j < tab_cuentas.getTotalFilas(); j++) {
                            if (codig_recur_cndpc.equals(tab_cuentas.getValor(j, "codig_recur_cndpc"))) {
                                ide_cndpc = tab_cuentas.getValor(j, "ide_cndpc");
                                break;
                            }
                        }
                        if (ide_cndpc == null) {
                            tab_detalle.setValor(i, "OBSERVACION_CNDCC", "LA CUENTA NO EXISTE");
                            correcto = false;
                        } else {
                            tab_detalle.setValor(i, "ide_cndcc", ide_cndpc);  //asigna el ide_cndcc  
                        }
                    } else {
                        tab_detalle.setValor(i, "OBSERVACION_CNDCC", "CÓDIGO DE CUENTA NO VÁLIDO");
                        correcto = false;
                    }
                }

                if (correcto) {
                    //Guarda el asiento
                    asc_asiento.dibujar();
                    //asiento nuevo
                    asc_asiento.getTab_cabe_asiento().setValor("ide_geper", asc_asiento.getBeneficiarioEmpresa());
                    //Inserta detalles al comprobante
                    for (int i = (tab_detalle.getTotalFilas() - 1); i >= 0; i--) {
                        asc_asiento.getTab_deta_asiento().insertar();
                        asc_asiento.getTab_deta_asiento().setValor("ide_cndpc", tab_detalle.getValor(i, "ide_cndcc"));
                        if (tab_detalle.getValor(i, "debe") != null) {
                            asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaDebe());
                            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", tab_detalle.getValor(i, "debe"));
                        } else if (tab_detalle.getValor(i, "haber") != null) {
                            asc_asiento.getTab_deta_asiento().setValor("ide_cnlap", asc_asiento.getLugarAplicaHaber());
                            asc_asiento.getTab_deta_asiento().setValor("valor_cndcc", tab_detalle.getValor(i, "haber"));
                        }
                        asc_asiento.getTab_deta_asiento().getFila(0).setLectura(true); //para no permitir editar

                    }
                    asc_asiento.calcularTotal();
                } else {
                    tab_detalle.getColumna("OBSERVACION_CNDCC").setVisible(true);
                    utilitario.addUpdate("tab_detalle");
                }

            } else {
                utilitario.agregarMensajeError("No existen los detalles en el actual Plan de Cuentas", "");
            }

        } else {
            utilitario.agregarMensajeError("No hay detalles para generar el Asiento Contable", "");
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

    public AsientoContable getAsc_asiento() {
        return asc_asiento;
    }

    public void setAsc_asiento(AsientoContable asc_asiento) {
        this.asc_asiento = asc_asiento;
    }

}
