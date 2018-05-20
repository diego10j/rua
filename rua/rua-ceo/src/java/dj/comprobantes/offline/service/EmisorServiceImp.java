/*
 *********************************************************************
 Objetivo: Servicio que implementa interface EmisorService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dao.EmisorDAO;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.exception.GenericException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class EmisorServiceImp implements EmisorService {

    @EJB
    private EmisorDAO emisorDao;

    @Override
    public Emisor getEmisor(String sucursal) throws GenericException {
        return emisorDao.getEmisor(sucursal);
    }


}
