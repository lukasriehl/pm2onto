package com.lukas.pm2onto.dao;

import com.lukas.pm2onto.model.Ontologia;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 *
 * @author lukas
 */
public class OntologiaDAO extends GenericDAO<Ontologia> {

    public OntologiaDAO() {
        super();
    }

    public void salvar(Ontologia ontologia) {
        save(ontologia);
    }

    public Long retornaProxIdOntologia() {
        Long idRetorno;

        Session session = (Session) getEntityManager().getDelegate();

        Criteria criteria = session
                .createCriteria(Ontologia.class)
                .setProjection(Projections.max("idOntologia"));
        idRetorno = (Long) criteria.uniqueResult();

        if (idRetorno == null) {
            idRetorno = 1L;
        } else {
            idRetorno += 1L;
        }

        return idRetorno;
    }
    
    public void remover(Ontologia ontologia){
        delete(ontologia);
    }

}
