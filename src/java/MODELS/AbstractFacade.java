/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MODELS;

import ENTITIES.Archivo;
import ENTITIES.Ciudad;
import ENTITIES.Contenidos;
import ENTITIES.Curso;
import ENTITIES.CursoSubCat;
import ENTITIES.ForoPosteos;
import ENTITIES.ForoSubcategoria;
import ENTITIES.InscripcionCurso;
import ENTITIES.MasterComentario;
import ENTITIES.MasterRespuestas;
import ENTITIES.Menu;
import ENTITIES.Persona;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }
    
    public boolean editar(T entity) {
        getEntityManager().merge(entity);
        return true;
    }

    public void remove(T entity) {
       
        getEntityManager().remove(getEntityManager().merge(entity));
        
      
    }
    
    public void eliminar(T entity) {
    
        EntityManager em = getEntityManager();
        em.remove(getEntityManager().merge(entity));

        
      
}
    

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public void crear(T entity) {
        getEntityManager().merge(entity);
        
        
        
        
    }
//    public void borrarCurso(Curso e)
//    {
//        EntityManager em2= getEntityManager();
//        Curso  b = em2.getReference(Curso.class,e.getIdCurso());
//               
//        em2.remove(b);
//    }
    
    public void borrarArchivo(Archivo e)
    {
        EntityManager em2= getEntityManager();
        Archivo  b = em2.getReference(Archivo.class,e.getIdArchivo());
               
        em2.remove(b);
    }
    
    public void borrarCiudad(Ciudad e)
    {
        EntityManager em2= getEntityManager();
        Ciudad  b = em2.getReference(Ciudad.class,e.getIdCiudad());
               
        em2.remove(b);
    }
    
    public void borrarContenidos(Contenidos e)
    {
        EntityManager em2= getEntityManager();
        Contenidos  b = em2.getReference(Contenidos.class,e.getIdContenido());
               
        em2.remove(b);
    }
    
    public void borrarCurso(Curso e)
    {
        EntityManager em2= getEntityManager();
        Curso  b = em2.getReference(Curso.class,e.getIdCurso());
               
        em2.remove(b);
    }
    
    public void borrarCursoSubCat (CursoSubCat e)
    {
        EntityManager em2= getEntityManager();
        CursoSubCat  b = em2.getReference(CursoSubCat.class,e.getIdSubcat());
               
        em2.remove(b);
    }
    
    public void borrarForo (ForoPosteos e)
    {
        EntityManager em2= getEntityManager();
        ForoPosteos  b = em2.getReference(ForoPosteos.class,e.getIdPost());
               
        em2.remove(b);
    }
    
    public void borrarForoSubCat (ForoSubcategoria e)
    {
        EntityManager em2= getEntityManager();
        ForoSubcategoria  b = em2.getReference(ForoSubcategoria.class,e.getIdSubcategoria());
               
        em2.remove(b);
    }
    public void borrarInscripcionCurso (InscripcionCurso e)
    {
        EntityManager em2= getEntityManager();
        InscripcionCurso  b = em2.getReference(InscripcionCurso.class,e.getIdInsc());
               
        em2.remove(b);
    }
    
    public void borrarMasterComentarios (MasterComentario e)
    {
        EntityManager em2= getEntityManager();
        MasterComentario  b = em2.getReference(MasterComentario.class,e.getIdComentario());
               
        em2.remove(b);
    }
    
    public void borrarMasterRespuestas (MasterRespuestas e)
    {
        EntityManager em2= getEntityManager();
        MasterRespuestas  b = em2.getReference(MasterRespuestas.class,e.getIdRespuestas());
               
        em2.remove(b);
    }
    
    public void borrarMasterMenu (Menu e)
    {
        EntityManager em2= getEntityManager();
        Menu  b = em2.getReference(Menu.class,e.getIdMenu());
               
        em2.remove(b);
    }
    
    
    public void borrarPersona(Persona e)
    {
        EntityManager em2= getEntityManager();
        Persona  b = em2.getReference(Persona.class,e.getIdPersona());
               
        em2.remove(b);
    }
}
