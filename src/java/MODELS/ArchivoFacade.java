/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELS;

import ENTITIES.Archivo;
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
public class ArchivoFacade extends AbstractFacade<Archivo> {

    @PersistenceContext(unitName = "YoulearnPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArchivoFacade() {
        super(Archivo.class);
    }
    
       public List<Archivo> obtenerArchivos(int idContenido)
    {
        EntityManager t2= getEntityManager();
        Query q = t2.createNamedQuery("Archivo.findByIdContenido").setParameter("idContenido", idContenido);   
        
       return q.getResultList(); 
    }
    
}
