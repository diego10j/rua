package pkg_cuentas_x_cobrar;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import org.primefaces.event.SelectEvent;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author user
 */
public class pre_datos_factura_cxc extends Pantalla {

    private Tabla tab_tabla = new Tabla();

    private Tabla tab_tabla1 = new Tabla();

    private Tabla tab_tabla2 = new Tabla();

    public pre_datos_factura_cxc() {

        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setTabla("con_tipo_document", "ide_cntdo", 2);
        tab_tabla1.getColumna("ide_cntdo").setVisible(false);
        tab_tabla1.getColumna("aplic_tribu_cntdo").setVisible(false);
        tab_tabla1.setRows(15);
        tab_tabla1.onSelect("seleccionarTipoDocumento");
        tab_tabla1.setLectura(true);
        tab_tabla1.dibujar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);

        tab_tabla.setId("tab_tabla");
        tab_tabla.setHeader("PUNTOS DE EMISIÓN Y ESTABLECIMIENTOS");
        tab_tabla.setTabla("cxc_datos_fac", "ide_ccdaf", 1);
        tab_tabla.getColumna("ide_georg").setCombo("gen_organigrama", "ide_georg", "nombre_georg", "");
        tab_tabla.getColumna("activo_ccdaf").setValorDefecto("true");
        tab_tabla.setCondicionSucursal(true);
        tab_tabla.setCondicion("ide_cntdoc is null ");
        tab_tabla.getColumna("ide_cntdoc").setVisible(false);
        tab_tabla.dibujar();

        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla);

        tab_tabla2.setHeader("CONFIGURAR CORREOS ADICIONALES");
        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setTabla("cxc_correo_doc", "ide_cccdo", 2);
        tab_tabla2.getColumna("ide_cntdo").setVisible(false);
        tab_tabla2.setCondicionSucursal(true);
        tab_tabla2.getColumna("activo_cccdo").setValorDefecto("true");
        tab_tabla2.setValidarInsertar(true);
        tab_tabla2.setCondicion("ide_cntdo =" + tab_tabla1.getValor("ide_cntdo"));
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(pat_panel1, pat_panel, pat_panel2, "29%", "22%", "H");
        agregarComponente(div_division);
    }

    public void seleccionarTipoDocumento(SelectEvent evt) {
        tab_tabla1.seleccionarFila(evt);
        tab_tabla.setCondicion("ide_cntdoc=" + tab_tabla1.getValorSeleccionado());
        tab_tabla.ejecutarSql();
        tab_tabla2.setCondicion("ide_cntdo=" + tab_tabla1.getValorSeleccionado());
        tab_tabla2.ejecutarSql();

    }

    @Override
    public void insertar() {
        if (tab_tabla1.getValorSeleccionado() != null) {
            if (tab_tabla.isFocus()) {
                tab_tabla.insertar();
                tab_tabla.setValor("ide_cntdoc", tab_tabla1.getValorSeleccionado());
            } else if (tab_tabla2.isFocus()) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("ide_cntdo", tab_tabla1.getValorSeleccionado());
            }
        }
    }

    @Override
    public void guardar() {
        if (tab_tabla2.isFilaInsertada() || tab_tabla2.isFilaModificada()) {
            if (utilitario.isCorreoValido(tab_tabla2.getValor("correo_cccdo")) == false) {
                utilitario.agregarMensajeInfo("El correo electrónico ingresado no es válido", "");
                return;
            }
        }

        if (tab_tabla.guardar()) {
            if (tab_tabla2.guardar()) {
                utilitario.getConexion().guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        if (tab_tabla.isFocus()) {
            tab_tabla.eliminar();
        } else if (tab_tabla2.isFocus()) {
            tab_tabla2.eliminar();
        }
    }

    public Tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(Tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

}
/*
 CREATE TABLE cxc_correo_doc (
 "ide_cccdo" int8 NOT NULL,
 "ide_cntdo" int4,
 "correo_cccdo" varchar(120),
 "activo_cccdo" bool,
 "ide_empr" int2,
 "ide_sucu" int2,
 "usuario_ingre" varchar(50) ,
 "fecha_ingre" date,
 "hora_ingre" time(6),
 "usuario_actua" varchar(50) ,
 "fecha_actua" date,
 "hora_actua" time(6),
 PRIMARY KEY ("ide_cccdo")
 )
 WITH (OIDS=FALSE)
 ;
 ALTER TABLE "public"."cxc_correo_doc"
 ADD CONSTRAINT "fk_correo_doc" FOREIGN KEY ("ide_cntdo") REFERENCES "public"."con_tipo_document" ("ide_cntdo");
 */


/*
 CREATE TABLE gen_correo_persona (
 "ide_gecop" int8 NOT NULL,
 "ide_geper" int4,
 "correo_gecop" varchar(120),
 "activo_gecop" bool,
 "descripcion_gecop" varchar(150),
 "ide_empr" int2,
 "ide_sucu" int2,
 "usuario_ingre" varchar(50) ,
 "fecha_ingre" date,
 "hora_ingre" time(6),
 "usuario_actua" varchar(50) ,
 "fecha_actua" date,
 "hora_actua" time(6),
 PRIMARY KEY ("ide_gecop")
 )
 WITH (OIDS=FALSE)
 ;
 ALTER TABLE "public"."gen_correo_persona"
 ADD CONSTRAINT "fk_correo_persona" FOREIGN KEY ("ide_geper") REFERENCES "public"."gen_persona" ("ide_geper");
 */
