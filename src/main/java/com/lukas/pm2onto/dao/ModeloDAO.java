package com.lukas.pm2onto.dao;

import com.lukas.pm2onto.model.Modelo;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 *
 * @author lukas
 */
public class ModeloDAO extends GenericDAO<Modelo>{
    
    public ModeloDAO() {
        super();
    }
    
    public void salvar(Modelo modelo) {
        save(modelo);
    }
    
    public Long retornaProxIdModelo() {
        Long idRetorno;

        Session session = (Session) getEntityManager().getDelegate();

        Criteria criteria = session
                .createCriteria(Modelo.class)
                .setProjection(Projections.max("id"));
        idRetorno = (Long) criteria.uniqueResult();

        if (idRetorno == null) {
            idRetorno = 1L;
        } else {
            idRetorno += 1L;
        }

        return idRetorno;
    }
}
