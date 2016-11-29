/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELS;

import ENTITIES.Notificaciones;
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
public class NotificacionesFacade extends AbstractFacade<Notificaciones> {

    @PersistenceContext(unitName = "YoulearnPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificacionesFacade() {
        super(Notificaciones.class);
    }
    
    
     public List<Notificaciones> obtenerNotificaciones()
    {
        EntityManager t2= getEntityManager();
        Query q = t2.createNamedQuery("Notificaciones.findByIdusuario");   
        
       return q.getResultList(); 
    }
     
    public List esSeguidor(int idUsuario,int idCurso)
    {
     
        
        EntityManager em3 = getEntityManager();
        Query q= em3.createNamedQuery("Notificaciones.findBySeguidor").setParameter("idUsuario",idUsuario).setParameter("idCurso", idCurso);
       
          
        
       return q.getResultList();
        
    }
    
}
