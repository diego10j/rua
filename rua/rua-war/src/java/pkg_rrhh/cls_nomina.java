/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg_rrhh;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author byron
 */
public class cls_nomina {

    private List<String[]> lis_rubros = new ArrayList();
    private Utilitario utilitario = new Utilitario();
    private TablaGenerica tab_rubros = new TablaGenerica();
    private TablaGenerica tab_reh_rubros_rol = new TablaGenerica();

    public cls_nomina() {
        // tabla donde se almacena los rubros de cada empleado de cada mes
        //tab_reh_rubros_rol.setId("tab_reh_rubros_rol");
        tab_reh_rubros_rol.setTabla("reh_rubros_rol", "ide_rhrro", -1);
        tab_reh_rubros_rol.getColumna("ide_usua").setValorDefecto(utilitario.getVariable("ide_usua"));
        tab_reh_rubros_rol.getColumna("ide_empr").setValorDefecto(utilitario.getVariable("ide_empr"));
        tab_reh_rubros_rol.getColumna("ide_sucu").setValorDefecto(utilitario.getVariable("ide_sucu"));
        tab_reh_rubros_rol.setCondicion("ide_rhrro=-1");
        tab_reh_rubros_rol.ejecutarSql();

    }



    public String getParametroRubrosRol(String parametro, String ide_rhcru, String ide_rherl) {
        if (parametro != null && !parametro.isEmpty()
                && ide_rhcru != null && !ide_rhcru.isEmpty()
                && ide_rherl != null && !ide_rherl.isEmpty()) {
            TablaGenerica tab_rubros_rol = utilitario.consultar("select * from reh_rubros_rol where ide_rherl=" + ide_rherl + " and ide_rhcru=" + ide_rhcru);
            if (tab_rubros_rol.getTotalFilas() > 0) {
                if (tab_rubros_rol.getValor(0, parametro) != null && !tab_rubros_rol.getValor(0, parametro).isEmpty()) {
                    return tab_rubros_rol.getValor(0, parametro);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getParametroRubros(String parametro, String ide_rhcru) {
        if (parametro != null && !parametro.isEmpty()
                && ide_rhcru != null && !ide_rhcru.isEmpty()) {
            TablaGenerica tab_rubros = utilitario.consultar("select * from reh_cab_rubro where ide_rhcru=" + ide_rhcru);
            if (tab_rubros.getTotalFilas() > 0) {
                if (tab_rubros.getValor(0, parametro) != null && !tab_rubros.getValor(0, parametro).isEmpty()) {
                    return tab_rubros.getValor(0, parametro);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void asignarRubrosTipoTeclado(Tabla tab_rubros_rol) {
        TablaGenerica tab_rubros = new TablaGenerica();
        tab_rubros = utilitario.consultar("select * from reh_cab_rubro where ide_rhfca=1");// 1 es el ide de forma de calculo por teclado
        // asignacion de rubros ingresados por teclado
        for (int i = 0; i < tab_rubros_rol.getTotalFilas(); i++) {
            for (int j = 0; j < tab_rubros.getTotalFilas(); j++) {
                if (tab_rubros_rol.getValor(i, "valor_rhrro") != null && !tab_rubros_rol.getValor(i, "valor_rhrro").isEmpty()) {
                    if (tab_rubros_rol.getValor(i, "ide_rhcru").equals(tab_rubros.getValor(j, "ide_rhcru"))) {
                        asignar_rubro(tab_rubros_rol.getValor(i, "ide_rhcru"), tab_rubros_rol.getValor(i, "valor_rhrro"));
                        break;
                    }
                }
            }
        }
    }

    public String getParametroEmpleado(String parametro, String ide_geper) {
        if (parametro != null && !parametro.isEmpty() && ide_geper != null && !ide_geper.isEmpty()) {
            TablaGenerica tab_empl = utilitario.consultar("select * FROM gen_persona where ide_geper=" + ide_geper);
            if (tab_empl.getTotalFilas() > 0) {
                if (tab_empl.getValor(0, parametro) != null && !tab_empl.getValor(0, parametro).isEmpty()) {
                    return tab_empl.getValor(0, parametro);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void cargarRubrosEmpleados(Tabla tab_empleados_rol, String fecha_fin_cab_rol) {
        tab_reh_rubros_rol.limpiar();
        for (int i = 0; i < tab_empleados_rol.getTotalFilas(); i++) {
            cargarRubrosPorEmpleado(tab_empleados_rol.getValor(i, "ide_geper"), tab_empleados_rol.getValor(i, "ide_rherl"), fecha_fin_cab_rol);
        }
        tab_reh_rubros_rol.guardar();
        utilitario.getConexion().guardarPantalla();
    }

    public boolean acumulaFondosReserva(String ide_geper) {
        String acumula = getParametroEmpleado("ide_rhfre", ide_geper);
        boolean boo_acumula_fondos;
        if (acumula != null && !acumula.isEmpty()) {
            if (utilitario.getVariable("p_reh_no_acumula_fondos").equalsIgnoreCase(acumula)) {
                return false;
            } else {
                asignar_rubro(utilitario.getVariable("p_reh_acumula_fondos_reserva"), acumula);
                return true;
            }
        } else {
            return false;
        }

    }

    public void asignarRubrosSinFormula(String ide_geper, String fecha_fin_cab_rol_pago) {
        importar_rubros();
        // importacion FECHA CALCULO  ********************************************
        asignar_rubro("0", fecha_fin_cab_rol_pago);

        String sueldo = getParametroEmpleado("sueldo_geper", ide_geper);
        String fecha_salida = getParametroEmpleado("fecha_salid_geper", ide_geper);
        String fecha_ingreso = getParametroEmpleado("fecha_ingre_geper", ide_geper);

        // importacion SUELDO
        if (sueldo != null && !sueldo.isEmpty()) {
            asignar_rubro("7", sueldo);
        }
        // asignacion de dias trabajados al mes
        System.out.println("DIAS TRABAJADOS: " + utilitario.getDia(utilitario.getUltimaFechaMes(fecha_fin_cab_rol_pago)));
        asignar_rubro("9", utilitario.getDia(utilitario.getUltimaFechaMes(fecha_fin_cab_rol_pago)) + "");
        // asignacion dias tranajados
        if (fecha_salida == null || fecha_salida.isEmpty()) {
            String fecha_fin_rol = fecha_fin_cab_rol_pago;
            if (fecha_ingreso != null && !fecha_ingreso.isEmpty()) {
                int num_dias = utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_ingreso), utilitario.getFecha(fecha_fin_rol));
                System.out.println("ide geper "+ide_geper+" diferencia de fechas "+num_dias);
               
                do {
                    if (num_dias>365){
                    num_dias = num_dias - 365;
                    }
                } while (num_dias > 365);
                asignar_rubro("55", num_dias + "");
                System.out.println(" num dias "+num_dias);
                
            } else {
                asignar_rubro("55", 0 + "");
                
            }
        } else {
            
            if (fecha_ingreso != null && !fecha_ingreso.isEmpty()) {
                int num_dias = utilitario.getDiferenciasDeFechas(utilitario.getFecha(fecha_ingreso), utilitario.getFecha(fecha_salida));
                System.out.println("fecha ingreso " + fecha_ingreso + " fecha fin rol" + fecha_salida);
                do {
                    num_dias = num_dias - 365;
                } while (num_dias > 365);
                asignar_rubro("55", num_dias + "");
                System.out.println("ide geper "+ide_geper+" num dias "+num_dias);
            }else {
                asignar_rubro("55", 0 + "");
            }
        }

    }

    public void cargarRubrosPorEmpleado(String ide_geper, String ide_rherl, String fecha_fin_cab_rol_pago) {

        asignarRubrosSinFormula(ide_geper, fecha_fin_cab_rol_pago);
        TablaGenerica tab_rubros_empl = utilitario.consultar("select *from reh_rubros_rol where ide_rherl=" + ide_rherl);
        if (tab_rubros_empl.getTotalFilas() == 0) {

            // calculo de los rubros que tienen formula
            boolean boo_acumula_fondos = acumulaFondosReserva(ide_geper);
            calcularRubrosConFormula(boo_acumula_fondos);
            tab_reh_rubros_rol.getColumna("ide_rherl").setValorDefecto(ide_rherl);
            for (int i = 0; i < lis_rubros.size(); i++) {
                tab_reh_rubros_rol.insertar();
                String[] fila = (String[]) lis_rubros.get(i);
                tab_reh_rubros_rol.setValor("ide_rhcru", fila[0]);
                tab_reh_rubros_rol.setValor("valor_rhrro", fila[1]);
                TablaGenerica tab_orden = utilitario.consultar("SELECT ide_rhcru,orden_rubro_rol_rhcru from reh_cab_rubro where ide_rhcru=" + fila[0]);
                if (tab_orden.getTotalFilas() > 0) {
                    if (tab_orden.getValor(0, "orden_rubro_rol_rhcru") != null && !tab_orden.getValor(0, "orden_rubro_rol_rhcru").isEmpty()) {
                        tab_reh_rubros_rol.setValor("orden_rhrro", tab_orden.getValor(0, "orden_rubro_rol_rhcru"));
                    }
                }
            }
        }
    }

    public void importar_rubros() {
        lis_rubros.clear();
        // ide_rheru=0 estado activo 
        tab_rubros = utilitario.consultar("select *from reh_cab_rubro where ide_rheru=0 and ide_empr=" + utilitario.getVariable("ide_empr") + " ORDER BY ide_rhcru asc");
        for (int i = 0; i < tab_rubros.getTotalFilas(); i++) {
            if (tab_rubros.getValor(i, "formula_rhcru") != null && !tab_rubros.getValor(i, "formula_rhcru").startsWith("=")) {
                String[] datos = {tab_rubros.getValor(i, "ide_rhcru"), tab_rubros.getValor(i, "formula_rhcru")};
                lis_rubros.add(datos);
            } else {
                String[] datos = {tab_rubros.getValor(i, "ide_rhcru"), null};
                lis_rubros.add(datos);
            }
        }
    }

    public String obtenerParametroRubros(String parametro, String ide_rhcru) {
        if (parametro != null && !parametro.isEmpty() && ide_rhcru != null && !ide_rhcru.isEmpty()) {
            TablaGenerica tab_rubros = utilitario.consultar("select *from reh_cab_rubro where ide_rheru=0 and ide_rhcru=" + ide_rhcru + " and ide_empr=" + utilitario.getVariable("ide_empr"));
            if (tab_rubros.getTotalFilas() > 0) {
                if (tab_rubros.getValor(0, parametro) != null && !tab_rubros.getValor(0, parametro).isEmpty()) {
                    return tab_rubros.getValor(0, parametro);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String obtenerFormulaReemplazada(String formula_rhcru, boolean con_valor) {
        String formula = formula_rhcru;
        String formula_reemplazada = "";
        String nueva_formula = "";
        int indice = 0;
        int indicador_parentesis = 0;
        String parentesis;
        do {
            indice = formula.indexOf("[");
            if (indice != -1) {
                do {
                    nueva_formula = formula.substring(indice, indice + 1);
                    formula = formula.substring(indice + 1);
                    parentesis = formula.substring(0, 1);
                    if (parentesis.equals("[")) {
                        indicador_parentesis = 1;
                        indice = formula.indexOf("[");
                        formula_reemplazada = formula_reemplazada.concat(nueva_formula);
                    } else {
                        indicador_parentesis = 0;
                    }
                } while (indicador_parentesis == 1);
                String ide_rhcru = recuperar_numero_formula(formula);
                if (con_valor) {
                    String valor_ide = buscar_valor_rubro(ide_rhcru);
                    if (valor_ide == null || valor_ide.isEmpty()) {
                        valor_ide = "0";
                    }
                    nueva_formula = nueva_formula.concat(valor_ide);
                } else {
                    String nombre_ide = obtenerParametroRubros("nombre_rhcru", ide_rhcru);
                    if (nombre_ide == null || nombre_ide.isEmpty()) {
                        nombre_ide = "";
                    }
                    nueva_formula = nueva_formula.concat(nombre_ide);
                }


                int ind = formula.indexOf("[");
                if (ind != -1) {
                    nueva_formula = nueva_formula.concat(formula.substring(formula.indexOf("]"), ind));
                } else {
                    nueva_formula = nueva_formula.concat(formula.substring(formula.indexOf("]")));
                }
                formula_reemplazada = formula_reemplazada.concat(nueva_formula);
            }
        } while (indice != -1);
        return formula_reemplazada;
    }

    public void calcularRubrosConFormula(boolean acumula_fondos) {
        if (acumula_fondos == false) {
            tab_rubros = utilitario.consultar("select *from reh_cab_rubro where ide_rheru=0 and ide_empr=" + utilitario.getVariable("ide_empr") + " ORDER BY ide_rhcru asc");
        } else {
            tab_rubros = utilitario.consultar("select *from reh_cab_rubro where ide_rhcru!=" + utilitario.getVariable("p_reh_valor_acumula_fondo") + " and ide_rheru=0 and ide_empr=" + utilitario.getVariable("ide_empr") + " ORDER BY ide_rhcru asc");
        }
        for (int i = 0; i < tab_rubros.getTotalFilas(); i++) {
            if (tab_rubros.getValor(i, "formula_rhcru") != null && tab_rubros.getValor(i, "formula_rhcru").startsWith("=")) {
                String formula_reemplazada = obtenerFormulaReemplazada(tab_rubros.getValor(i, "formula_rhcru"), true);
                asignar_rubro(tab_rubros.getValor(i, "ide_rhcru"), utilitario.getFormatoNumero(utilitario.evaluarExpresion(formula_reemplazada)));
            }
        }
    }

    public String recuperar_numero_formula(String formula) {
        String ide_a_buscar = formula.substring(0, formula.indexOf("]"));
        return ide_a_buscar;
    }

    public void asignar_rubro(String ide_rhcru, String valor) {
        int fila = buscar_rubro(ide_rhcru);
        if (fila != -1) {
            String v[] = {ide_rhcru, valor};
            lis_rubros.set(fila, v);
        }
    }

    public String buscar_valor_rubro(String ide_rhcru) {
        for (int i = 0; i < lis_rubros.size(); i++) {
            String[] fila = (String[]) lis_rubros.get(i);
            if (fila[0].equals(ide_rhcru)) {
                return fila[1];
            }
        }
        return null;
    }

    private int buscar_rubro(String ide_rhcru) {
        for (int i = 0; i < lis_rubros.size(); i++) {
            String[] fila = lis_rubros.get(i);
            if (fila[0].equals(ide_rhcru)) {
                return i;
            }
        }
        return -1;
    }

    public List getLis_rubros() {
        return lis_rubros;
    }

    public void setLis_rubros(List lis_rubros) {
        this.lis_rubros = lis_rubros;
    }

    public TablaGenerica getTab_reh_rubros_rol() {
        return tab_reh_rubros_rol;
    }

    public void setTab_reh_rubros_rol(TablaGenerica tab_reh_rubros_rol) {
        this.tab_reh_rubros_rol = tab_reh_rubros_rol;
    }
}
