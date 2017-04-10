/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema.parametros;

import framework.aplicacion.Parametro;
import java.util.ArrayList;
import java.util.List;

public class Parametros {

    public List<Parametro> getParametrosSistema() {
        List<Parametro> lis_parametros = new ArrayList<>();
//////////////////////////////////////////////////////////////////////
        /*
         * SISTEMA MODULO =0
         */
        lis_parametros.add(new Parametro("0", "P_SIS_RESETEO_CLAVE", "Indica cuando se resetea la clave de un usuario", "6", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_ACTIVA_USUARIO", "Indica cuando se pone en estado activo a un usuario", "4", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_DESACTIVA_USUARIO", "Indica cuando se pone en estado inactivo a un usuario", "3", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_INGRESO_USUARIO", "Indica cuando un usuario accede exitosamente al sistema", "0", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_FALLO_INGRESO", "Indica cuando no se produce un acceso al sistema por parte de un usuario", "1", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_CADUCO_SESSION", "Indica cuando a un usuario se le expira el tiempo de su session", "7", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_SALIO_USUARIO", "Indica cuando un usuario sale del sistema", "8", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_CAMBIO_CLAVE", "Indica cuando un usuario cambia su clave", "5", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_CREAR_USUARIO", "Indica cuando se crea un usuario en el sistema", "9", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_BLOQUEA_USUARIO", "Indica cuando se bloqueao a un usuario", "2", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));
        lis_parametros.add(new Parametro("0", "P_SIS_DESBLOQUEA_USUARIO", "Indica cuando se desbloquea a un usuario", "10", "SIS_ACCION_AUDITORIA", "IDE_ACAU", "DESCRIPCION_ACAU"));

//////////////////////////////////////////////////////////////////////
        // parametros para el cierre de periodo
        lis_parametros.add(new Parametro("0", "p_con_superavit_ejercicio_anterior", "Indica la configuracion para la cuenta de Superavit del Ejercicio Anterior ", "1496", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_deficit_ejercicio_anterior", "Indica la configuracion para la cuenta de Deficit del Ejercicio Anterior ", "1497", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_patrimonio", "Indica la configuracion para la cuenta a la que se vamos a sumar el (superavit)  o restar el (Deficit) del Ejercicio Anterior ", "1487", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_superavit_ejercicio_presente", "Indica la configuracion para la cuenta de Superavit del Ejercicio Presente ", "1500", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("0", "p_con_deficit_ejercicio_presente", "Indica la configuracion para la cuenta de Deficit del Ejercicio Presente ", "4481", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));

        lis_parametros.add(new Parametro("0", "p_con_tipo_documento_nota_credito", "Comprobante tipo nota de credito ", "0", "con_tipo_document", "ide_cntdo", "nombre_cntdo"));
        lis_parametros.add(new Parametro("0", "p_con_usa_presupuesto", "Indica si la empresa maneja el módulo de presupuesto", "false"));
        /*
         * INVENTARIO MODULO =1
         */
        lis_parametros.add(new Parametro("1", "p_inv_servicio", "Indica el servicio ", "2", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_activo_fijo", "Indica que un articulo pertenecen al grupo de activos fijos ", "53", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_bien", "Indica que un articulo pertenecen al grupo de bienes ", "46", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_servicio", "Indica que un articulo pertenecen al grupo de servicios ", "2", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_articulo_honorarios", "Indica que un articulo pertenecen al grupo de honorarios profesionales ", "47", "inv_articulo", "ide_inarti", "nombre_inarti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_compra", "Indica el tipo de transaccion compra de inventario ", "19", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_venta", "Indica el tipo de transaccion venta de inventario ", "29", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_estado_normal", "Indica el estado normal de comprobante de inventario ", "1", "inv_est_prev_inve", "ide_inepi", "nombre_inepi"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_reversa_mas", "Indica que el tipo de transaccion inversa(cuando existe una anulacion)  ", "12", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_reversa_menos", "Indica que el tipo de transaccion inversa(cuando existe una anulacion)  ", "13", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));
        lis_parametros.add(new Parametro("1", "p_inv_tipo_transaccion_consumo", "Indica que el tipo de transaccion de consumo ", "27", "inv_tip_tran_inve", "ide_intti", "nombre_intti"));


        /*
         * CUENTAS POR PAGAR MODULO =2
         */
        lis_parametros.add(new Parametro("2", "p_cxp_estado_factura_normal", "Indica el estado de la factura (Normal) ", "0", "cxp_estado_factur", "ide_cpefa", "nombre_cpefa"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_factura", "Indica que el tipo de transaccion es (Factura) ", "0", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago", "Indica que el tipo de transaccion (Pago) ", "3", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago_con_anticipo", "Indica que el tipo de transaccion es de un pago con un anticipo registrado (Pago con Anticipo) ", "17", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_reversa_mas", "Indica que el tipo de transaccion inversa(cuando existe una anulacion) ", "9", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_reversa_menos", "Indica que el tipo de transaccion inversa(cuando existe una anulacion) ", "10", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_anticipo", "Indica que el tipo de transaccion (Pago) ", "7", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_inversion", "Indica que el tipo de transaccion (Inversion) ", "15", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_inversion_interes", "Indica que el tipo de transaccion de inversion de intereses (Inversion de los interes) ", "18", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago_inversion", "Indica que el tipo de transaccion pago de inversion (pago Inversion Terminada) ", "16", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_pago_interes_inversion", "Indica que el tipo de transaccion pago de intereses de la inversion (Pago de intereses) ", "19", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_renova_interes_reinver", "Indica que el tipo de transaccion renovacion de intereses de inversion (Interes de Nueva Renovacion de Inversion) ", "20", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_tipo_trans_retencion", "Indica que el tipo de transaccion Retencion ", "21", "cxp_tipo_transacc", "ide_cpttr", "nombre_cpttr"));
        lis_parametros.add(new Parametro("2", "p_cxp_estado_factura_anulada", "Indica el estado de la factura (Anulada) ", "1", "cxp_estado_factur", "ide_cpefa", "nombre_cpefa"));

        /*
         * CUENTAS POR COBRAR MODULO =3
         */
        lis_parametros.add(new Parametro("3", "p_cxc_estado_factura_normal", "Indica el estado de la factura (Normal) ", "0", "cxc_estado_factura", "ide_ccefa", "nombre_ccefa"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_factura", "Indica que el tipo de transaccion es (Factura) ", "4", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_pago", "Indica que el tipo de transaccion es (Pago de cliente) ", "0", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_anticipo", "Indica que el tipo de transaccion es (Anticipo de cliente) ", "5", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_reversar_mas", "Indica que el tipo de transaccion es para reversar (+) una transaccion ", "15", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_reversar_menos", "Indica que el tipo de transaccion es para reversar (-)una transaccion ", "16", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));
        lis_parametros.add(new Parametro("3", "p_cxc_estado_factura_anulada", "Indica el estado de la factura (Anulada) ", "1", "cxc_estado_factura", "ide_ccefa", "nombre_ccefa"));
        lis_parametros.add(new Parametro("3", "p_cxc_tipo_trans_retencion", "Indica que el tipo de transaccion Retencion ", "8", "cxc_tipo_transacc", "ide_ccttr", "nombre_ccttr"));

        /*
         * ACTIVOS FIJOS MODULO =4
         */
        lis_parametros.add(new Parametro("4", "p_act_tipo_activo_fijo", "Indica el tipo de activo fijo ", "0", "inv_tipo_producto", "ide_intpr", "nombre_intpr"));
        lis_parametros.add(new Parametro("4", "p_act_estado_dado_de_baja", "Indica el estado dado de baja de un activo ", "4", "act_estado_activo_fijo", "ide_aceaf", "nombre_aceaf"));

        /*
         * COMPRAS MODULO =5
         *
         */
        lis_parametros.add(new Parametro("5", "p_gen_tipo_identificacion_ruc", "Indica el tipo de identificacion que sea RUC ", "1", "gen_tipo_identifi", "ide_getid", "nombre_getid"));
        lis_parametros.add(new Parametro("5", "p_gen_tipo_identificacion_cedula", "Indica el tipo de identificacion que sea CEDULA ", "0", "gen_tipo_identifi", "ide_getid", "nombre_getid"));
        lis_parametros.add(new Parametro("5", "p_com_estado_proveedor", "Indica el estado del Proveedor", "0", "com_estado_provee", "ide_coepr", "nombre_coepr"));

        /*
         * NOMINA =6
         */
        lis_parametros.add(new Parametro("6", "p_reh_dias_trabajados", "Indica el numero de dias trabajados ", "9", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_acumula_fondos_reserva", "Indica el rubro acumula fondos de reserva ", "25", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_rubro_valor_recibir", "Indica el rubro valor a recibir ", "37", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_estado_activo_empleado", "Indica el estado activo de un  empleado ", "0", "reh_estado_emplea", "ide_rheem", "nombre_rheem"));
        lis_parametros.add(new Parametro("6", "p_reh_si_acumula_fondos", "Indica que un empleado si acumula fondos de reserva", "1", "reh_fondos_reserva", "ide_rhfre", "nombre_rhfre"));
        lis_parametros.add(new Parametro("6", "p_reh_valor_acumula_fondo", "Indica el rubro acumula fondos de reserva ", "26", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_no_acumula_fondos", "Indica que un empleado no acumula fondos de reserva", "0", "reh_fondos_reserva", "ide_rhfre", "nombre_rhfre"));
        lis_parametros.add(new Parametro("6", "p_reh_rubro_valor_recibir", "Indica el rubro valor a recibir", "37", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("6", "p_reh_estado_pre_nomina", "Indica el estado pre nomina del rol ", "0", "reh_estado_rol", "ide_rhero", "nombre_rhero"));
        lis_parametros.add(new Parametro("6", "p_reh_estado_cerrada", "Indica el estado cerrado del rol ", "2", "reh_estado_rol", "ide_rhero", "nombre_rhero"));
// luis toapanta pamatros nuevos nomina desde aqui
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_garante_empl_biess", "Indica el tipo de garante Empleado del Biess ", "1", "NRH_TIPO_GARANTE", "IDE_NRTIG", "DETALLE_NRTIG"));
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_nomina_escenario", "Indica el tipo de nomina NORMAL ", "5", "NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_nomina_normal", "Indica el tipo de nomina NORMAL ", "0", "NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_nomina_liquidacion", "Indica el tipo de nomina LIQUIDACION ", "2", "NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_nomina_pago_decimos", "Indica el tipo de nomina PAGO DE DECIMOS ", "4", "NRH_TIPO_NOMINA", "IDE_NRTIN", "DETALLE_NRTIN"));
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_nomina_para_generar_rol", "Indica los tipos de nominas para generar desde rol ", "0,3"));
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_nomina_para_calcular_renta", "Indica los tipos de nominas a los cuales se calcula la renta ", "0"));
        lis_parametros.add(new Parametro("6", "p_nrh_estado_nomina_cerrada", "Indica la estado de NOMINA CERRADA, en este estado los datos son solo de lectura, no se puede modificar ni eliminar ", "1", "NRH_ESTADO_ROL", "IDE_NRESR", "DETALLE_NRESR"));
        lis_parametros.add(new Parametro("6", "p_nrh_estado_pre_nomina", "Indica la estado PRE-NOMINA, en este estado se pueden leer,modificar y eliminar datos de la nomina", "2", "NRH_ESTADO_ROL", "IDE_NRESR", "DETALLE_NRESR"));
        lis_parametros.add(new Parametro("6", "p_nrh_estado_nomina_anulada", "Indica la estado de NOMINA ANULADA, en este estado los datos son solo de lectura, y nos indica que una nomina generada se encuentra anulada ", "3", "NRH_ESTADO_ROL", "IDE_NRESR", "DETALLE_NRESR"));
        lis_parametros.add(new Parametro("6", "p_nrh_forma_calculo_formula", "Indica la forma de calculo TECLADO del Rubro ", "0", "NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("6", "p_nrh_forma_calculo_teclado", "Indica la forma de calculo TECLADO del Rubro ", "1", "NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("6", "p_nrh_forma_calculo_importado", "Indica la forma de calculo TECLADO del Rubro ", "2", "NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("6", "p_nrh_forma_calculo_constante", "Indica la forma de calculo TECLADO del Rubro ", "3", "NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_importado", "Indica la forma de calculo IMPORTADO del Rubro ", "2", "NRH_FORMA_CALCULO", "IDE_NRFOC", "DETALLE_NRFOC"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_vacaciones_liquidacion", "Indica el rubro en el cual se va a calcular el valor correspondiente a vacaciones ", "28", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_desc_valores_liquidar", "Indica el descuento de valores por liquidar ", "289", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_ajuste_sueldo", "Indica el numero de dias para calculo de rol en ajuste de sueldo ", "285", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_ajuste_sueldo", "Indica el numero de dias para calculo de rol en ajuste de sueldo ", "287", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_total_ingresos", "Indica el rubro Total Ingresos de un empleado ", "11", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_total_egresos", "Indica el rubro Total Ingresos de un empleado ", "22", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_valor_recibir", "Indica el rubro Total Ingresos de un empleado ", "131", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_subtotal_iess", "Indica el rubro Total Ingresos de un empleado ", "281", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_seguro_social_con_rmu", "Indica el rubro Seguro Social calculado solo con la RMU ", "291", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_rmu_cargo_subrogante", "Indica el rubro Total Ingresos de un empleado ", "278", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_subrogados", "Indica el rubro Total Ingresos de un empleado ", "279", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_region", "Indica el rubro Region, lo cual nos permite saber a que region pertenece el empleado para pago de decimo 4 ", "285", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_pendientes_vacacion", "Indica el numero de dias pendientes de vacacion ", "68", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_decimo_cuarto_rol", "Indica el rubro Decimo Cuarto Sueldo para el Rol  (Rol) en la fecha de pago configurada para el rubro ", "9", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_decimo_cuarto_rol_p", "Indica el rubro Decimo Cuarto Sueldo para el Rol  (Rol) en la fecha de pago configurada para el rubro ", "283", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_trabajados_d4", "Este rurbro nos indica el numero de dias para el calculo de decimo cuarto ", "65", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_proviciones_d4", "Este rurbro nos indica el numero de dias para el calculo de decimo cuarto ", "121", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_decimo_tercer_rol", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "12", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_trabajados_d3", "Este rurbro nos indica el numero de dias para el calculo de decimo tercero ", "63", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_salario_basico_unificado_vig", "Este rurbro nos indica el numero de dias para el Salario basico unificado vigente ", "276", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_proviciones_d3", "Este rurbro nos indica el numero de dias para el calculo de decimo cuarto ", "125", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_descuento_decimo_cuarto", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "273", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_descuento_decimo_tercer", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "195", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_beneficios_guarderia", "Indica el rubro Beneficios Guarderia para el (Rol) ", "144", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_aportes_personales", "Indica el rubro aporte_personal para el (Rol) nombre del rubro seguro social ", "44", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_impuesto_renta_mensual", "Indica el rubro aporte_personal para el (Rol) ", "41", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_acumula_fondos_reserva", "Indica el rubro para saber si un empleado acumula o no los fondos de reserva ", "46", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_valor_fondos_reserva", "Indica el rubro con el valor acumulado de los fondos de reserva nomina ", "29", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_valor_provision_fondos_reserva", "Indica el rubro con el valor acumulado de los fondos de reserva iess", "120", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_descuento_nomina", "Indica el rubro Decimo Tercer Sueldo para el (Rol) ", "48", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_acumula_decimos", "Indica el rubro para saber si un empleado acumula los decimos true=acumula, false=no acumula ", "330", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_base_imponible_mes_anterior", "Indica el rubro donde se va a importar el sueldo base del mes anterior ", "329", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_imponible_mes_anterior", "Indica el rubro para del mes anteriro que se toma para el nuevo rubro ", "329", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_fondreser_acum_ante", "Indica el rubro fondos de reserva acumulado a ser importado para la nomina vigente", "136", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_fondreser_nomi_ante", "Indica el rubro fondos de reserva pago en nomina a ser importado para la nomina vigente", "29", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_fondreser_acum_pago", "Indica el rubro fondos de reserva acumulados con el cual se va a pagar en la nomina vigente", "338", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_fondreser_nomi_pago", "Indica el rubro fondos de reserva en nomina con el cual se va a pagar en la nomina vigente", "339", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_trubro_egreso_informativo", "Indica el tipo de rubro Egreso Informativo (Tipo Rol) ", "2", "NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("6", "p_nrh_trubro_ingreso_informativo", "Indica el tipo de rubro Ingreso Informativo (Tipo Rol) ", "3", "NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("6", "p_nrh_trubro_informativo", "Indica el tipo de rubro Informativo (Tipo Rol) ", "4", "NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_remuneracion_unificada", "Indica el rubro Remuneracion unificada ", "24", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_remuneracion_unificada_honorarios", "Indica el rubro Remuneracion unificada ", "277", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_periodo_nomina", "Indica el rubro Decimo Cuarto Sueldo para el Rol  (Rol) en la fecha de pago configurada para el rubro ", "2", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_trabajados", "Indica los dias trabajados para la generacion del rol ", "274", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_fondos_reserva", "Indica los dias trabajados para la generacion del rol ", "69", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_dias_antiguedad", "Indica los dias trabajados para la generacion del rol ", "60", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_gerencia_general_ap", "Indica el nombre de la persona encargada de la Gerencia General para el reporte de acciones de personal", "Eco. Roberto. A. Machuca. C"));
        lis_parametros.add(new Parametro("6", "p_gerencia_administrativa_ap", "Indica el nombre de la persona encargada de la Gerencia Administrativa para el reporte de acciones de personal", "Ing. Marco V. Egas A."));
        lis_parametros.add(new Parametro("6", "p_nrh_trubro_egreso", "Indica el tipo de rubro Egreso (Tipo Rol) ", "1", "NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("6", "p_nrh_trubro_ingreso", "Indica el tipo de rubro Ingreso (Tipo Rol) ", "0", "NRH_TIPO_RUBRO", "IDE_NRTIR", "DETALLE_NRTIR"));
        lis_parametros.add(new Parametro("6", "p_cargo_gerencia_general_ap", "Indica el nombre del Cargo de la Gerencia General para el reporte de acciones de personal", "GERENTE GENERAL"));
        lis_parametros.add(new Parametro("6", "p_cargo_gerencia_administrativa_ap", "Indica el nombre del Cargo de la Gerencia Administrativa para el reporte de acciones de personal", "GERENTE ADMINISTRATIVO"));
        lis_parametros.add(new Parametro("6", "p_liquidacion_elaborado_por", "Indica el nombre de la persona encargada de Elaborar la Liquidacion para el reporte de Liquidacion de Haberes", "Ing. Nelly Fabara"));
        lis_parametros.add(new Parametro("6", "p_liquidacion_revisado_por", "Indica el nombre de la persona encargada de Revizar la Liquidacion para el reporte de Liquidacion de Haberes", "Ing. Maria Fernanda Reyes"));
        lis_parametros.add(new Parametro("6", "p_liquidacion_aprobado_por", "Indica el nombre de la persona encargada de Aprobar la Liquidacion para el reporte de Liquidacion de Haberes", "Ing. Marco Egas"));
        lis_parametros.add(new Parametro("6", "p_nrh_rubro_vacaciones_liquidacion", "Indica el rubro en el cual se va a calcular el valor correspondiente a vacaciones ", "28", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("6", "p_nrh_tipo_garante", "Indica el tipo de garante EMPLEADO DE EMGIRS", "1", "nrh_tipo_garante", "ide_nrtig", "detalle_nrtig"));
// hasta aqui lusi toapanta paramteros nomina
        /*
         * TESORERIA =7
         */
        lis_parametros.add(new Parametro("7", "p_tes_num_dias_filtro", "Indica el numero de dias para realizar el filtro ", "90"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_cheque", "Indica la transaccion se lo realiza con cheque ", "2", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_caja", "Indica que el Banco es de tipo Caja ", "3", "tes_banco", "ide_teban", "nombre_teban"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_retiro_caja", "Indica la transaccion se lo realiza con caja ", "1", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_deposito_caja", "Indica la transaccion se lo realiza con caja ", "8", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_deposito", "Indica la transaccion se lo realiza con bancos (deposito) ", "0", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_transferencia_menos", "Indica la transaccion se lo realiza con bancos (Transferencia MENOS) ", "9", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_transferencia_mas", "Indica la transaccion se lo realiza con bancos (Transferencia MAS) ", "10", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_reversa_mas", "Indica la transaccion de reversa con signo + ", "11", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tran_reversa_menos", "Indica la transaccion de reversa sin signo - ", "12", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_estado_lib_banco_normal", "Indica el estado normal de la transaccion de libro bancos del modulo Tesoreria ", "0", "tes_estado_libro_banco", "ide_teelb", "nombre_teelb"));
        lis_parametros.add(new Parametro("7", "p_tes_estado_lib_banco_anulado", "Indica el estado anulado de la transaccion de libro bancos del modulo Tesoreria ", "1", "tes_estado_libro_banco", "ide_teelb", "nombre_teelb"));
        lis_parametros.add(new Parametro("7", "p_tes_nota_debito", "Indica el tipo de la transaccion nota de debito de libro bancos del modulo Tesoreria ", "3", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_nota_credito", "Indica el tipo de la transaccion nota de credito de libro bancos del modulo Tesoreria ", "4", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tip_tran_reversar_mas", "Indica que se realiza una transaccion inversa (+) ", "10", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tip_tran_reversar_menos", "Indica que se realiza una transaccion inversa (-) ", "11", "tes_tip_tran_banc", "ide_tettb", "nombre_tettb"));
        lis_parametros.add(new Parametro("7", "p_tes_tipo_cuenta_banco_virtual", "Indica que el tipo de cuenta bancaria virtual (cajas) ", "6", "tes_tip_cuen_banc", "ide_tetcb", "nombre_tetcb"));
        lis_parametros.add(new Parametro("7", "p_tes_banco_casas", "Indica que el Banco es de tipo casas para pagar por trandsferencia de cuentas contables ", "5", "tes_banco", "ide_teban", "nombre_teban"));

        /*
         * VENTAS =8
         */
        /*
         * PRESTAMOS E INVERSIONES =9 *
         */
        lis_parametros.add(new Parametro("9", "p_iyp_activo", "Indica que se carga solo las cuentas de nivel de Activo", "0", "con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc"));
        lis_parametros.add(new Parametro("9", "p_iyp_pasivo", "Indica que se carga solo las cuentas de nivel de Pasivo  ", "4", "con_det_plan_cuen", "ide_cndpc", "codig_recur_cndpc"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_normal", "Indica el estado normal del prestamo  ", "0", "iyp_estado_prestamos", "ide_ipepr", "nombre_ipepr"));
        lis_parametros.add(new Parametro("9", "p_iyp_tipo_prestamo", "Indica que se carga el tipo de prestamo cuota fija  ", "0", "iyp_tipo_prestamo", "ide_iptpr", "nombre_iptpr"));
        lis_parametros.add(new Parametro("9", "p_iyp_serie_factura", "Indica la serie a utilizar para facturar el ineters de un pago de prestamo ", "3", "cxc_datos_fac", "ide_ccdaf", "serie_ccdaf"));
        lis_parametros.add(new Parametro("9", "p_iyp_cuenta_interes", "Indica la cuenta que afecta a los interes de los prestamos de las casas", "1982", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_activo_inversion", "Indica el estado activo de la inversión", "0", "iyp_estado_inversion", "ide_ipein", "nombre_ipein"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_cancelado", "Indica el estado cancelado de la inversion  ", "1", "iyp_estado_inversion", "ide_ipein", "nombre_ipein"));
        lis_parametros.add(new Parametro("9", "p_iyp_estado_anulada", "Indica el estado anulada de la inversion  ", "2", "iyp_estado_inversion", "ide_ipein", "nombre_ipein"));
        lis_parametros.add(new Parametro("9", "p_iyp_cuenta_interes_pagado", "Indica la cuenta que afecta a los interes de los prestamos de de ingreso", "4100", "con_det_plan_cuen", "ide_cndpc", "nombre_cndpc"));


        /*
         * SRI =10 *
         */
        lis_parametros.add(new Parametro("10", "p_sri_tip_sus_tri02", "Indica el tipo de sustento tributario de alterno 02 (COSTO O GASTO PARA DECLARACIÓN DE IMP. A LA RENTA (SERVICIOS Y BIENES DISTINTOS DE INVENTARIOS Y ACTIVOS FIJOS )) ", "1", "sri_tipo_sustento_tributario", "ide_srtst", "nombre_srtst"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_local_12%_401", "Ventas locales [excluye activos fijos] gravadas tarifa 12%", "401"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_activos_12%_402", "Ventas de activos fijos gravadas tarifa 12%", "402"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_local_no_dertri_0%_403", "Ventas locales [excluye activos fijos] gravadas tarifa 0% que no dan derecho a crédito tributario", "403"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_activos_0%_no_dertri_404", "Ventas de activos fijos gtavadas tarifa 0% que no dan derecho a crédito tributario", "404"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_local_0%_dertri_405", "Ventas locales(excluye activos fijos) gravadasde activos fijos gtavadas tarifa 0% que dan derecho a crédito tributario", "405"));
        lis_parametros.add(new Parametro("10", "p_sri_venta_activos_0%_dertri_406", "Ventas de activos fijos gravadas tarifa 0% que dan derecho a crédito tributario", "406"));
        lis_parametros.add(new Parametro("10", "p_sri_exportacion_bienes_407", "Exportaciones de bienes", "407"));
        lis_parametros.add(new Parametro("10", "p_sri_exportacion_servicios_408", "Exportaciones de servicios", "408"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_pagos_12%_dertri_501", "Adquisiciones y pagos [excluye activos fijos] gravados tarifa 12% (con derecho a crédito tributario)", "501"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_local_12%_dertri_502", "Adquisiciones locales de activos fijos gravados tarifa 12% (con derecho a crédito tributario)", "502"));
        lis_parametros.add(new Parametro("10", "p_sri_otra_adqui_pago_12%_no_dertri_503", "Otras adquisiones y pagos gravados tarifa 12% (sin derecho a crédito tributario)", "503"));
        lis_parametros.add(new Parametro("10", "p_sri_import_bienes_12%_504", "Importaciones de bienes [excluye activos fijos] gravados tarifa 12%", "504"));
        lis_parametros.add(new Parametro("10", "p_sri_import_act_fijos_12%_505", "Importaciones de activos fijos  gravados tarifa 12%", "505"));
        lis_parametros.add(new Parametro("10", "p_sri_import_bienes_0%_506", "Importaciones de bienes [incluye activos fijos] gravados tarifa 0%", "506"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_realizadas_rise_518", "Adquisiciones realizadas a contribuyente rise", "518"));
        lis_parametros.add(new Parametro("10", "p_sri_trans_no_obj_iva_431", "Transferencias no objeto o exentas de IVA", "431"));
        lis_parametros.add(new Parametro("10", "p_sri_adqui_no_obj_iva_531", "Adquisiciones no objeto de IVA", "531"));
        lis_parametros.add(new Parametro("10", "p_sri_base_renta", "Indica el rubro de la base imponible del impuesto a la renta para formulario 103", "53", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("10", "p_sri_impuesto_renta", "Indica el rubro del valor de impuesto a la renta para formulario 103", "31", "reh_cab_rubro", "ide_rhcru", "nombre_rhcru"));
        lis_parametros.add(new Parametro("10", "p_sri_activa_comp_elect", "Activa o desactiva comprobantes electronicos del SRI", "true"));
        lis_parametros.add(new Parametro("10", "p_sri_ambiente_comp_elect", "Ambiente de Comprobantes Eléctronicos del SRI", "1"));  //1 PRUEBAS //2 PRODUCCION
        lis_parametros.add(new Parametro("10", "p_sri_moneda_comp_elect", "Ambiente de Comprobantes Eléctronicos del SRI", "DOLAR"));
        lis_parametros.add(new Parametro("10", "p_sri_codigoIva_comp_elect", "Codigo de Iva para Comprobantes Eléctronicos del SRI", "2"));
        lis_parametros.add(new Parametro("10", "p_sri_codigoPorcentajeIva_comp_elect", "Codigo de Iva para Comprobantes Eléctronicos del SRI", "2"));
        lis_parametros.add(new Parametro("10", "p_sri_codigoPorcentajeIva0_comp_elect", "Codigo de Iva 0 para Comprobantes Eléctronicos del SRI", "0"));
        lis_parametros.add(new Parametro("10", "p_sri_codigoPorcentajeIvaNoObjeto_comp_elect", "Codigo de Iva no Objeto  para Comprobantes Eléctronicos del SRI", "6"));
        lis_parametros.add(new Parametro("10", "p_sri_porcentajeIva_comp_elect", "Porcentaje del Iva para Comprobantes Eléctronicos del SRI", "12.00"));
//desde aqui luis toapanta sri
        lis_parametros.add(new Parametro("10", "p_sri_tipo_moneda_spi", "Indica el codigo de moneda dolar $ para el archivo SPI", "1"));
        lis_parametros.add(new Parametro("10", "p_sri_tipo_pago_spi", "Indica el codigo de tipo de pago via internet para el archivo SPI", "2"));
        lis_parametros.add(new Parametro("10", "p_sri_concepto_spi", "Indica el codigo del concepto para el archivo SPI", "2"));
        lis_parametros.add(new Parametro("10", "p_sri_control_spi", "Indica el numero de control para el archivo SPI", "11217287915534"));
        lis_parametros.add(new Parametro("10", "p_sri_301_sueldos_y_salarios", "Indica el 301 SUELDOS Y SALARIOS para (RUBRO 24) el	SRI", "24", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_303_subrogaciones", "Indica el 303 SOBRESUELDOS, COMISIONES, BONOS Y OTROS INGRESOS GRAVADOS (RUBRO 27) para el SRI", "27", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_305_participacion_utilidades", "Indica el 307 INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES (RUBRO 76) para el SRI", "76", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_307_ing_grav_otro_emp", "Indica el 307 INGRESOS GRAVADOS GENERADOS CON OTROS EMPLEADORES (RUBRO 77) para el SRI", "77", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_311_provisiones_decimo_tercero", "Indica el 311 DECIMO TERCER SUELDO (RUBRO 125)  provisiones para el SRI", "125", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_313_provisiones_decimo_cuarto", "Indica el 313 DECIMO CUARTO SUELDO (RUBRO 121)  provisiones  provisiones para el SRI", "121", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_315_provisiones_fondo_reserva", "Indica el 315 FONDO DE RESERVA (RUBRO 120)  provisiones  provisiones para el SRI", "120", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_317_otros_ing_rel_depencia", "Indica el 317 OTROS INGRESOS EN RELACION DE DEPENDENCIA QUE NO CONSTITUYEN RENTA GRAVADA para el SRI", "283", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_351_aporte_personal", "Indica el 351 (-) APORTE PERSONAL IESS CON ESTE EMPLEADOR para el SRI", "44", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_353_aporte_personal_otros_empl", "Indica el 353 (-) APORTE PERSONAL IESS CON OTROS EMPLEADORES para el SRI", "288", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_381_imp_renta_empleador", "Indica el 381 IMPUESTO A LA RENTA ASUMIDO POR ESTE EMPLEADOR para el SRI", "257", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_403_impuesto_retenido", "Indica el 403 VALOR DEL IMPUESTO RETENIDO Y ASUMIDO POR OTROS EMPL. DURANTE EL PERIODO DECLARADO para el SRI", "258", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_405_impuesto_asumido", "Indica el 405 VALOR DEL IMPUESTO ASUMIDO POR ESTE EMPLEADOR para el SRI", "267", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_sri_407_imp_retenido_trabajador", "Indica el 407 VALOR DEL IMPUESTO RETENIDO AL TRABAJADOR POR ESTE EMPLEADOR para el SRI", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_salario_ingreso", "Indica el VALOR DEL INGRESO DEL SALARIO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_salario_egreso", "Indica el VALOR DEL EGRESO DEL SALARIO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_sobresueldo_ing", "Indica el VALOR DEL INGRESO DEL SOBRESUELDO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_sobresueldo_egr", "Indica el VALOR DEL EGRESO DEL SOBRESUELDO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_parti_utilidad_ing", "Indica el VALOR DEL INGRESO DEL PARTICIPACION DE UTILIDAD", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_parti_utilidad_egr", "Indica el VALOR DEL EGRESO DEL PARTICIPACION DE UTILIDAD", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_ingr_gravados_ing", "Indica el VALOR DEL INGRESO GRAVADO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_ingr_gravados_egr", "Indica el VALOR DEL EGRESO GRAVADO ", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_empleador_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA POR EL EMPLEADOR", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_empleador_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA POR EL EMPLEADOR ", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_decimo_tercer_sueldo_ing", "Indica el VALOR DEL INGRESO DEL DECIMO TERCER SUELDO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_decimo_tercer_sueldo_egr", "Indica el VALOR DEL EGRESO DEL DECIMO TERCER SUELDO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_decimo_cuarto_sueldo_ing", "Indica el VALOR DEL INGRESO DEL DECIMO CUARTO SUELDO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_decimo_cuarto_sueldo_egr", "Indica el VALOR DEL EGRESO DEL DECIMO CUARTO SUELDO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_fondo_reserva_ing", "Indica el VALOR DEL INGRESO DEL FONDO DE RESERVA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_fondo_reserva_egr", "Indica el VALOR DEL EGRESO DEL FONDO DE RESERVA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_otros_ingrrelaciondep_ing", "Indica el VALOR DEL INGRESO DEL INGRESO EN RELACION DE DEPENDENCIA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_otros_ingrrelaciondep_egr", "Indica el VALOR DEL EGRESO DEL INGRESO EN RELACION DE DEPENDENCIA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_ingresos_gravaempleado_ing", "Indica el VALOR DEL INGRESO DEL EGRESO GRAVADO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_ingresos_gravaempleado_egr", "Indica el VALOR DEL EGRESO DEL EGRESO GRAVADO", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_aporte_personal_iess_ing", "Indica el VALOR DEL INGRESO DEL APORTE PERSONAL IESS", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_aporte_personal_iess_egr", "Indica el VALOR DEL EGRESO DEL APORTE PERSONAL IESS", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_vivienda_ing", "Indica el VALOR DEL INGRESO DE LOS GASTOS PERSONALES POR VIVIENDA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_vivienda_egr", "Indica el VALOR DEL EGRESO DE LOS GASTOS PERSONALES POR VIVIENDA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_salud_ing", "Indica el VALOR DEL GASTO PERSONAL POR SALUD", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_salud_egr", "Indica el VALOR DEL GASTO PERSONAL POR SALUD", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_educa_ing", "Indica el VALOR DEL INGRESO DEL  GASTO PERSONAL POR EDUCACION", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_educa_egr", "Indica el VALOR DEL EGRESO DEL GASTO PERSONAL POR EDUCACION", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_alimenta_ing", "Indica el VALOR DEL INGRESO DEL  GASTO PERSONAL POR ALIMENTACION", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_alimenta_egr", "Indica el VALOR DEL EGRESO DEL GASTO PERSONAL POR ALIMENTACION", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_vestimenta_ing", "Indica el VALOR DEL INGRESO DEL  GASTO PERSONAL POR VESTIMENTA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_gas_persona_vestimenta_egr", "Indica el VALOR DEL EGRESO DEL GASTO PERSONAL POR VESTIMENTA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_baseimponible_gravada_ing", "Indica el VALOR DEL INGRESO DE LA BASE IMPONIBLE GRABADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_baseimponible_gravada_egr", "Indica el VALOR DEL EGRESO DE LA BASE IMPONIBLE GRABADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_renta_causado_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_renta_causado_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_renta_asumidootr_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_renta_asumidootr_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_renta_asumidoest_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impuesto_renta_asumidoest_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impue_retenido_ing", "Indica el VALOR DEL INGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_impue_retenido_egr", "Indica el VALOR DEL EGRESO DEL IMPUESTO A LA RENTA GRAVADA", "42", "NRH_RUBRO", "IDE_NRRUB", "DETALLE_NRRUB"));
        lis_parametros.add(new Parametro("10", "p_redep_codigo_establecimiento", "Define el codigo del establecimiento", "001"));
        lis_parametros.add(new Parametro("10", "p_redep_pais_tipo_residencia", "Define el numero del tipo de residencia", "943"));
        lis_parametros.add(new Parametro("10", "p_redep_aplica_convenio", "Define el numero del tipo de residencia", "943"));
        lis_parametros.add(new Parametro("10", "p_redep_pais_residencia", "Define el numero del tipo de residencia", "943"));
        lis_parametros.add(new Parametro("10", "p_redep_tipo_identi_discap", "Define el numero del tipo de documento de identidad de los discapcitados", "N"));
        lis_parametros.add(new Parametro("10", "p_redep_nro_docume_discap", "Define el numero del documento de identidad de los discapcitados", "999"));
        lis_parametros.add(new Parametro("10", "p_redep_tipo_sis_salario_neto", "Define el tipo de sistema de salario neto", "1"));

// hasta aqui luis toapnra sri
        /*
         * PRESUPUESTOS =11 *
         */
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_obra", "Indica el tipo de aporte Obra", "2", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_paricipante", "Indica el tipo de aporte Participante", "1", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_inspectoria", "Indica el tipo de aporte Inspectoria", "0", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_aporte_rmayor", "Indica el tipo de aporte Rector Mayor", "3", "pre_tipo_aporte_presu", "ide_prtap", "nombre_prtap"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_presu_gasto", "Indica el tipo de presupuesto de Gasto", "1", "pre_tipo_presupuesto", "ide_prtpr", "nombre_prtpr"));
        lis_parametros.add(new Parametro("11", "p_pre_tipo_presu_ingreso", "Indica el tipo de presupuesto de Ingreso", "0", "pre_tipo_presupuesto", "ide_prtpr", "nombre_prtpr"));
        lis_parametros.add(new Parametro("11", "p_pre_estado_en_tramite", "Indica el tipo de estado en tramite del presupuesto", "1", "pre_estado_plan_presu", "ide_prepp", "nombre_prepp"));
        lis_parametros.add(new Parametro("11", "p_pre_num_modificacion_presu", "Numero de Modificaciones permitidas para el presupuesto", "1"));
// desde aqui luis toapanta presupesto
        lis_parametros.add(new Parametro("11", "p_modulo_presupuesto", "Indica el modulo que pertenece a presupuesto", "10", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_modulo_tramite", "Indica el modulo que pertenece el tramite ", "11", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_modulo_tramite_alterno", "Indica el modulo que pertenece el tramite alterno ", "15", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_modulo_empleado", "Indica el modulo que pertenece el empleado ", "8", "cont_parametros_general", "ide_copag", "detalle_copag"));
        lis_parametros.add(new Parametro("11", "p_modulo_no_adjudicado", "Indica el modulo que pertenece al no adjudicado ", "10", "cont_parametros_general", "ide_copag", "detalle_copag"));
        lis_parametros.add(new Parametro("11", "p_modulo_proveedor", "Indica el modulo que pertenece el proveedor ", "9", "cont_parametros_general", "ide_copag", "detalle_copag"));
        lis_parametros.add(new Parametro("11", "p_modulo_estado_comprometido", "Indica el modulo que pertenece el comprometido ", "21", "cont_estado", "ide_coest", "detalle_coest"));
        lis_parametros.add(new Parametro("11", "p_modulo_estado_comprometido", "Indica el modulo que pertenece el comprometido ", "21", "cont_estado", "ide_coest", "detalle_coest"));
        lis_parametros.add(new Parametro("11", "p_sub_actividad", "Indica el nombre sub actividad para ", "5", "pre_nivel_funcion_programa", "ide_prnfp", "detalle_prnfp"));
        lis_parametros.add(new Parametro("11", "p_mov_devengado", "Indica el tipo de movimiento presupuestario devengado", "5", "pre_movimiento_presupuestario", "ide_prmop", "detalle_prmop"));
        lis_parametros.add(new Parametro("11", "p_cuenta_banco", "Indica el codigo de cuenta contable de bancos para generar el asiento contable", "5", "cont_catalogo_cuenta", "ide_cocac", "cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("11", "p_lugar_ejecuta_banco", "Indica el si la cuenta cuanta bancos donde se ejcuta debe o haber", "2", "gen_lugar_aplica", "ide_gelua", "detalle_gelua"));
        lis_parametros.add(new Parametro("11", "p_cuenta_banco", "Indica el codigo de cuenta contable de bancos para generar el asiento contable", "5", "cont_catalogo_cuenta", "ide_cocac", "cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("11", "p_lugar_ejecuta_banco", "Indica el si la cuenta cuanta bancos donde se ejcuta debe o haber", "2", "gen_lugar_aplica", "ide_gelua", "detalle_gelua"));
        lis_parametros.add(new Parametro("11", "p_cuenta_iva", "Indica el codigo de cuenta contable de iva para generar el asiento contable", "5", "cont_catalogo_cuenta", "ide_cocac", "cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("11", "p_lugar_ejecuta_iva", "Indica el si la cuenta cuanta iva donde se ejcuta debe o haber", "2", "gen_lugar_aplica", "ide_gelua", "detalle_gelua"));
        lis_parametros.add(new Parametro("11", "p_cuenta_iva_cierra", "Indica el codigo de cuenta contable de iva para generar el asiento contable", "5", "cont_catalogo_cuenta", "ide_cocac", "cue_codigo_cocac,cue_descripcion_cocac"));
        lis_parametros.add(new Parametro("11", "p_lugar_ejecuta_iva_cierra", "Indica el si la cuenta cuanta iva donde se ejcuta debe o haber", "2", "gen_lugar_aplica", "ide_gelua", "detalle_gelua"));
        lis_parametros.add(new Parametro("11", "p_modulo_secuencialproyecto", "Indica el modulo para generar el secuencial de proyecto", "21", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_modulo_secuencialprograma", "Indica el modulo para generar el secuencial de programa", "21", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_modulo_secuencialproducto", "Indica el modulo para generar el secuencial de producto", "21", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_modulo_secuencialfase", "Indica el modulo para generar el secuencial de fase", "21", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_modulo_secuencialsubactiv", "Indica el modulo para generar el secuencial de sub actividad", "21", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_proyecto", "Indica el proyecto", "1", "pre_nivel_funcion_programa", "ide_prnfp", "detalle_prnfp"));
        lis_parametros.add(new Parametro("11", "p_programa", "Indica el programa", "2", "pre_nivel_funcion_programa", "ide_prnfp", "detalle_prnfp"));
        lis_parametros.add(new Parametro("11", "p_producto", "Indica el producto", "3", "pre_nivel_funcion_programa", "ide_prnfp", "detalle_prnfp"));
        lis_parametros.add(new Parametro("11", "p_fase", "Indica el la fase", "4", "pre_nivel_funcion_programa", "ide_prnfp", "detalle_prnfp"));
        lis_parametros.add(new Parametro("11", "p_modulo_secuencialcertificacion", "Indica el modulo para generar el secuencial de las certificaciones", "21", "gen_modulo", "ide_gemod", "detalle_gemod"));
        lis_parametros.add(new Parametro("11", "p_nombre_coordinador_fin", "Indica el nombre del Coordinador Financiero", "Ing. Juan Flores"));
        lis_parametros.add(new Parametro("11", "p_nombre_jefe_presupuesto", "Indica el nombre del Jefe de Presupuesto", "Ing. Salasar Mishell"));
///*
        /*
         * MODULO GENERAL 12
         */
        lis_parametros.add(new Parametro("12", "p_gen_tipo_iden_ruc", "tipo de identificacion RUC", "1", "gen_tipo_identifi", "ide_getid", "nombre_getid"));
        lis_parametros.add(new Parametro("12", "p_gen_tipo_iden_cedula", "tipo de identificacion CEDULA", "0", "gen_tipo_identifi", "ide_getid", "nombre_getid"));

        lis_parametros.add(new Parametro("12", "p_gen_beneficiario_roles", "Identifica al beneficiario en el asiento de roles de pago ", "1438", "gen_persona", "ide_geper", "nom_geper"));
        // desque aqui Luis Toapanta Agregado Parametros Generales
        lis_parametros.add(new Parametro("12", "p_tipo_cobro_factura", "Permite definir el tipo de cobro inicial que se mostrará por defecto en la facturación", "4", "rec_tipo", "ide_retip", "detalle_retip"));
        lis_parametros.add(new Parametro("12", "p_gen_terminacion_encargo_posicion", "Permite al momento de hacer una terminacion de subrogacion volver a las caracteristicas del contrato que poseia antes de realizar la subrogacion", "46", "GEN_ACCION_EMPLEADO_DEPA", "IDE_GEAED", "DETALLE_GEAED"));
        lis_parametros.add(new Parametro("12", "p_gen_status_stand_by", "Indica la categoria estatus stand by", "2", "GEN_CATEGORIA_ESTATUS", "IDE_GECAE", "DETALLE_GECAE"));
        lis_parametros.add(new Parametro("12", "p_gen_accion_empl_comision", "Indica la accion de empleado Comision", "51", "GEN_ACCION_EMPLEADO_DEPA", "IDE_GEAED", "DETALLE_GEAED"));
        lis_parametros.add(new Parametro("12", "p_gen_tipo_institucion_educativa", "Indica el tipo de institucion Educativa", "4", "GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("12", "p_gen_tipo_institucion_aseguradora", "Indica el tipo de institucion Aseguradoras", "5", "GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("12", "p_gen_tipo_institucion_financiera", "Indica el tipo de institucion Financiera", "2", "GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("12", "p_gen_encargo_posicion", "Permite al momento de hacer una Subrogaciï¿½n desplegar los cargos a ser encargados", "13", "GEN_ACCION_MOTIVO_EMPLEADO", "IDE_GEAME", "IDE_GEAME"));
        lis_parametros.add(new Parametro("12", "p_gen_accion_contratacion", "Permite al momento de hacer una contratacion desplegar un dialogo para ingresar el periodo de asistencia ", "1", "GEN_ACCION_EMPLEADO_DEPA", "IDE_GEAED", "IDE_GEAED"));
        lis_parametros.add(new Parametro("12", "p_gen_lugar_aplica_haber", "Aplica al haber ", "2", "GEN_LUGAR_APLICA", "IDE_GELUA", "DETALLE_GELUA"));
        lis_parametros.add(new Parametro("12", "p_gen_lugar_aplica_debe", "aplica al debe ", "1", "GEN_LUGAR_APLICA", "IDE_GELUA", "DETALLE_GELUA"));
        lis_parametros.add(new Parametro("12", "p_gen_instituciones", "Selecciona las instituciones que requiera mostrar (Instituciones Educativas)", "41,43", "GEN_INSTITUCION", "IDE_GEINS", "DETALLE_GEINS"));
        lis_parametros.add(new Parametro("12", "p_gen_casas_comerciales", "Selecciona el tipo de institucion casas comerciales  (Casas comerciales )", "2", "GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("12", "p_gen_estado_activo", "Permite conocer tipo de estado (activo)en vacaciones ", "1", "gen_estados", "IDE_GEEST", "detalle_geest"));
        lis_parametros.add(new Parametro("12", "p_gen_estado_inactivo", "Permite conocer tipo de estado (inactivo)en vacaciones ", "2", "gen_estados", "IDE_GEEST", "detalle_geest"));
        lis_parametros.add(new Parametro("12", "p_gen_actividad_capacitador", "Permite conocer tipo de actividad economica ", "3", "GTH_TIPO_ACTIVIDAD_ECONOMICA", "IDE_GTTAE", "DETALLE_GTTAE"));
        lis_parametros.add(new Parametro("12", "p_firma_resp_solicitud_debito_sd", "Permite conocer el cargo de la persona que firma la Solicitud de Anticipo ", "GERENTE ADMINISTRATIVO"));
        lis_parametros.add(new Parametro("12", "p_gen_tipo_institucion_educativa_idiomas", "Indica el tipo de institucion Educativa de Idiomas", "4", "GEN_TIPO_INSTITUCION", "IDE_GETII", "DETALLE_GETII"));
        lis_parametros.add(new Parametro("12", "p_gen_responsable_depa_bienestar", "Indica el departamento para poder ver el responsable de la entrevista (Talento Humano)", "135", "GEN_DEPARTAMENTO", "IDE_GEDEP", "DETALLE_GEDEP"));
        lis_parametros.add(new Parametro("12", "p_valor_iva", "Indica el valor aplicado del iva", "0.12"));
        lis_parametros.add(new Parametro("12", "p_director_adminsitrativo", "Indica el titulo y nombre del Director Administrativo", "Ing. Paul Velez"));
        lis_parametros.add(new Parametro("12", "p_jefe_activos_fijos", "Indica el titulo y nombre del jefe de Activos Fijos", "Ing. Evelyn Suarez"));
        lis_parametros.add(new Parametro("12", "p_factor_multiplicador_renta_discapacitados", "Indica el factor a multiplicar si el empleado es discapacitado o tercera edad para el calculo de la renta", "2"));
        lis_parametros.add(new Parametro("12", "p_num_veces_vmgd", "Indica el numero de veces que se aplica a la fraccion basica desgravada para el maximo valor para gastos deducibles ", "1.3"));
        lis_parametros.add(new Parametro("12", "p_porcentaje_tot_ing_grab", "El porcentaje que se aplica al total de ingresos gravados del contribuyente para la deduccion total por gastos deducibles", "50"));
        lis_parametros.add(new Parametro("12", "p_minimo_dias_derecho_fondos", "Indica el numero de dias minimo para tener derecho a fondos de reserva (a partir del 13avo mes(390 dias))", "390"));
        lis_parametros.add(new Parametro("12", "p_amortizacion_cada", "Indica que la amortizacion se la realizara cada 30 dias", "30"));
        lis_parametros.add(new Parametro("12", "p_tasa_interes", "Indica la tasa de interes anticipo que se la realizara", "0"));
        lis_parametros.add(new Parametro("12", "p_tasa_interes_efectiva", "Indica la tasa  efectiva anticipo de interes que se la realizara", "0"));
        // hasta aqui luis toapanta parametros generales
        /*
         * GESTION DE TALENTO HUMANO MODULO =13
         */

        lis_parametros.add(new Parametro("13", "p_gth_tipo_sindicato", "Indica el tipo de sindicato que pertenece", "1", "gth_tipo_sindicato", "ide_gttsi", "detalle_gttsi"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_contrato_nombramiento", "Indica el tipo de documento de contrato NOMBRAMIENTO", "0", "GTH_TIPO_CONTRATO", "IDE_GTTCO", "DETALLE_GTTCO"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_sangre", "Indica el tipo de sangre del empleado", "11", "gth_tipo_sangre", "ide_gttis", "detalle_gttis"));
        lis_parametros.add(new Parametro("13", "p_gth_nacionalidad", "Indica la nacionalidad del empleado", "2", "gth_nacionalidad", "ide_gtnac", "detalle_gtnac"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_documento_cedula", "Indica el tipo de documento de identidad CEDULA", "0", "GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_documento_ruc", "Indica el tipo de documento de identidad RUC", "2", "GTH_TIPO_DOCUMENTO_IDENTIDAD", "IDE_GTTDI", "DETALLE_GTTDI"));
        lis_parametros.add(new Parametro("13", "p_gth_estado_civil_soltero", "Indica el estado civil soltero", "0", "GTH_ESTADO_CIVIL", "IDE_GTESC", "DETALLE_GTESC"));
        lis_parametros.add(new Parametro("13", "p_gth_estado_civil_union_libre", "Indica el estado civil union libre", "2", "GTH_ESTADO_CIVIL", "IDE_GTESC", "DETALLE_GTESC"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_telefono_celular", "Indica el tipo de telefono celular", "0", "GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_telefono_fijo", "Indica el tipo de telefono fijo", "1", "GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_telefono_oficina", "Indica el tipo de telefono de oficina", "3", "GTH_TIPO_TELEFONO", "IDE_GTTIT", "DETALLE_GTTIT"));
        lis_parametros.add(new Parametro("13", "p_gth_tipo_empleado_codigo", "Indica el tipo de empleado de codigo de trabajo", "1", "GTH_TIPO_EMPLEADO", "IDE_GTTEM", "DETALLE_GTTEM"));
        lis_parametros.add(new Parametro("13", "p_gth_coordinador_tthh", "Indica el nombre del coordinador de talento humano", "Ing. Tania Pantoja"));
        lis_parametros.add(new Parametro("13", "p_gth_analista_tthh", "Indica el nombre del analista de talento humano", "Ing. Paola"));

//////////////////////////////////////////////////////////////////////
        return lis_parametros;
    }
}
