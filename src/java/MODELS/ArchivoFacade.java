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
    
       public List<Archivo> obtenerArchivos(int idIdentificador,int idAux)
    {
        EntityManager t2= getEntityManager();
        Query q = t2.createNamedQuery("Archivo.findByIdContenido").setParameter("idIdentificador", idIdentificador).setParameter("idUsuario", idAux);   
        
       return q.getResultList(); 
    }
       
         public List<Archivo> archivosNoAprobadoTodos()
    {
        EntityManager m2 = getEntityManager();
        Query q=m2.createNamedQuery("Archivo.findByNoAprobadoTodos");
        
        
        return q.getResultList();
    }
         
           public List<Archivo> archivosAprobadoTodos()
    {
        EntityManager m2 = getEntityManager();
        Query q=m2.createNamedQuery("Archivo.findByAprobadoTodos");
        
        
        return q.getResultList();
    }
           
            public void updateUsuario(int idUsuario,String dir)
    {
         Query query = em.createQuery("UPDATE Usuario us SET us.imagen_foto_perfil='"+dir+"' where us.idUsuario="+idUsuario);
             query.executeUpdate();
        
        
       
    }
            
    public void updateArchivo(int idArchivo,int aux)
    {
        System.out.println("Inicio update archivo");
        System.out.println("Parametro idArchivo-> "+idArchivo+"  Parametro Aux-> "+aux);
         Query query = em.createQuery("UPDATE Archivo us SET us.idAux="+aux+" where us.idArchivo="+idArchivo);
             query.executeUpdate();
        
        
       
    }
    
    public void updateArchivoPub(int idUsuario,int idArchivo)
    {
        
        System.out.println("Inicio update archivo");
        Query ultimaid = em.createQuery("SELECT max(us.idPublicacion+1) FROM PublicacionPerfil us where us.idPublicador="+idUsuario);
        System.out.println("Ultima id "+ultimaid);
        System.out.println("ID Usuario"+idUsuario);
        System.out.println("ID idArchivo"+idArchivo);
      
        
         Query query = em.createQuery("UPDATE Archivo us SET us.idAux="+ultimaid.getSingleResult()+" where us.idArchivo="+idArchivo+" AND us.idUsuario="+idUsuario);
             query.executeUpdate();
        
        
       
    }
    
   
    
}

