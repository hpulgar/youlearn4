/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELS;

import ENTITIES.Contenidos;
import ENTITIES.Curso;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Fenix III
 */
@Stateless
public class ContenidosFacade extends AbstractFacade<Contenidos> {

    @PersistenceContext(unitName = "YoulearnPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ContenidosFacade() {
        super(Contenidos.class);
    }
    
    
    public List<Contenidos> verUnContenido(int idContenido)
    {
        EntityManager m2=getEntityManager();
        Query q = m2.createNamedQuery("Contenidos.verContenido").setParameter("idContenido", idContenido);
        return q.getResultList();
    }
    
    public Contenidos contenido(int idContenido)
    {
        Contenidos obC = new Contenidos();
        
        EntityManager m2=getEntityManager();
        Query q = m2.createNamedQuery("Contenidos.verContenido").setParameter("idContenido", idContenido);
        
        return obC = (Contenidos) q.getSingleResult();
    }
    
        public List<Contenidos> unidadesCurso(int idCurso)
    {
     
        
        EntityManager m2 = getEntityManager();
        Query q=m2.createNamedQuery("Contenidos.findByidCurso").setParameter("idCurso",idCurso);
        
         return q.getResultList();
       
    }
        
          public void borrarUnidad(Contenidos e)
  {
       EntityManager em2= getEntityManager();
      Contenidos  b = em2.getReference(Contenidos.class,e.getIdContenido());
              
       em2.remove(b);
    }
    
       
    
}
