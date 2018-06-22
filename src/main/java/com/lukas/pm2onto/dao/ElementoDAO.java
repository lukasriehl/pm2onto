package com.lukas.pm2onto.dao;

import com.lukas.pm2onto.model.Elemento;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author lukas
 */
public class ElementoDAO extends GenericDAO<Elemento> {

    public ElementoDAO() {
        super();
    }
    
    public void salvar(Elemento elemento) {
        save(elemento);
    }

//    public void insereElemento(String id, String nome, String descricao, String documentacao) throws Exception {
//        try {
//            Elemento elemento = new Elemento(id, nome, descricao, documentacao);
//
//            getManager().getTransaction().begin();
//            getManager().persist(elemento);
//            getManager().getTransaction().commit();
//        } catch (Exception e) {
//            getManager().getTransaction().rollback();
//            throw new Exception("Erro ao inserir elemento!".concat(e != null ? "Erro:".concat(e.getMessage()) : ""));
//        } finally {
//            getManager().close();
//            getFactory().close();
//        }
//    }
//    
//     public List<Elemento> getElementos() {
//        TypedQuery<Elemento> query = getManager().createQuery(
//                "select e from Elemento e", Elemento.class);
//        return query.getResultList();
//    }

}
