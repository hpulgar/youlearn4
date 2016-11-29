/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELS;

import ENTITIES.Archivo;
import ENTITIES.InscripcionCurso;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Felipe
 */
@Stateless
public class InscripcionCursoFacade extends AbstractFacade<InscripcionCurso> {

    @PersistenceContext(unitName = "YoulearnPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InscripcionCursoFacade() {
        super(InscripcionCurso.class);
    }
    
       public List<InscripcionCurso> obtenerCursos(int idUsuario)
    {
        EntityManager t2= getEntityManager();
        Query q = t2.createNamedQuery("InscripcionCurso.findByIdUsuario").setParameter("idUsuario", idUsuario);   
        
       return q.getResultList(); 
    }
    
}
