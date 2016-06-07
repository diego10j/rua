
select * from inv_det_comp_inve;


select dci.ide_inarti,sum(valor_indci* signo_intci) as valor 
from inv_det_comp_inve dci
left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci 
left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti 
left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci
where dci.ide_inarti=544
and fecha_trans_incci <'2013-06-01' 
and dci.ide_sucu=0
and ide_inbod=1
and ide_inepi=1
group by dci.ide_inarti;




select dci.ide_inarti,sum(cantidad_indci* signo_intci) as cantidad 
from inv_det_comp_inve dci
left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci 
left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti 
left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci
where dci.ide_inarti=544
and fecha_trans_incci <'2013-03-08' 
and dci.ide_sucu=0
and ide_inbod=1
and ide_inepi=1
group by dci.ide_inarti;


select dci.ide_inarti,fecha_trans_incci,precio_promedio_indci
from inv_det_comp_inve dci
left join inv_cab_comp_inve cci on dci.ide_incci=cci.ide_incci 
where dci.ide_inarti=544
and fecha_trans_incci <='2013-06-08' 
and dci.ide_sucu=0
and ide_inbod=1
and ide_inepi=1
order by fecha_trans_incci desc limit 1




SELECT dci.ide_indci,cci.fecha_trans_incci,nom_geper,nombre_intti,
case when signo_intci = 1 THEN cantidad_indci  end as CANT_INGRESO,
case when signo_intci = 1 THEN precio_indci  end as VUNI_INGRESO,
case when signo_intci = 1 THEN valor_indci  end as VTOT_INGRESO,
case when signo_intci = -1 THEN cantidad_indci  end as CANT_EGRESO,
case when signo_intci = -1 THEN precio_indci  end as VUNI_EGRESO,
case when signo_intci = -1 THEN valor_indci  end as VTOT_EGRESO,
'' as CANT_SALDO,precio_promedio_indci as VUNI_SALDO ,'' VTOT_SALDO
from inv_det_comp_inve dci 
left join inv_cab_comp_inve cci on cci.ide_incci=dci.ide_incci 
left join gen_persona gpe on cci.ide_geper=gpe.ide_geper
left join inv_tip_tran_inve tti on tti.ide_intti=cci.ide_intti 
left join inv_tip_comp_inve tci on tci.ide_intci=tti.ide_intci 
left join inv_articulo arti on dci.ide_inarti=arti.ide_inarti
where dci.ide_inarti=544 
and dci.ide_sucu=0
and fecha_trans_incci BETWEEN '2011-06-01'  and '2013-03-08' 
and ide_inepi=1
and ide_inbod=1
ORDER BY cci.fecha_trans_incci,dci.ide_indci asc




Factuta Ventas Para todas las facturas
1) Hace la factura normal-electronica y guardar cxc    ---OK
2) Separa pago de factura cxc --ok
3) Generar transaccion inventario de factura de venta  --ok
***
En opciones de facturas no contabilizadas 
1)Seleccionar facturas y generar comprobante de contabilidad (Componente Comp Conta Generico)
3)Opcion para ingresar comprobante y asignar num de comprobante a facturas seleccionadas

***Perfeccionar Comprobnate de Contabilidad Generico

VERIFICAR SI SE ESTA GUARDANDO EN BASE_TARIFA0, BASE NO OBJENTO EN FACTURAS CXC