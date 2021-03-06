package VIEWS;

import ENTITIES.Amigos;
import ENTITIES.Archivo;
import ENTITIES.Curso;
import ENTITIES.ForoCategoria;
import ENTITIES.Notificaciones;
import ENTITIES.Persona;
import ENTITIES.Usuario;
import VIEWS.util.JsfUtil;
import VIEWS.util.PaginationHelper;
import MODELS.NotificacionesFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.event.RowEditEvent;

@Named("notificacionesController")
@SessionScoped
public class NotificacionesController implements Serializable {

    private Notificaciones current;
    private DataModel items = null;
    @EJB
    private MODELS.NotificacionesFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Notificaciones> arNotificaciones = new ArrayList();
    private List<Notificaciones> arNotificaciones2= new ArrayList();
    private List<Amigos> arAmigos= new ArrayList();
     private List<Usuario> arUsuarios= new ArrayList();

    public NotificacionesController() {
    }

      public void prepararCrear()
    {
        current = null;
    }
      
            public void precarga()
    {
        List<Notificaciones> arMe;
        arMe = ejbFacade.findAll();
       
        for(int i =0;i<arMe.size();i++)
        {
            current = arMe.get(i);
        }
                
    }

public void cargaDatos(int id)
    {
        current = ejbFacade.find(id);
    }

    public Notificaciones getSelected() {
        if (current == null) {
            current = new Notificaciones();
            selectedItemIndex = -1;
        }
        return current;
    }

    private NotificacionesFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Notificaciones) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Notificaciones();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotificacionesCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Notificaciones) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotificacionesUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Notificaciones) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NotificacionesDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    
     public void actualizaNotificacion(int id_notificacion)
    {
       
          
        try{
            

            current = ejbFacade.find(id_notificacion);
            current.setLeida(1);
          
            ejbFacade.crear(current);
           
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
    
    public String comparaFechas(Date fechaIn)
    {
        //Compara la fecha actual con fechaIn para asi devolver mensaje si X contenido fue posteado hace x cantidad de tiempo.
        
        //texto utilizado para concatenar strings y fechas para posteriormente devolver un mensaje determinado
        String textoFecha="";
        
         SimpleDateFormat sdf = new SimpleDateFormat();
           sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
            sdf.applyPattern("yyyy/mm/dd");
            Date fechaAhoraTiempo = new Date();
            
            Calendar fechaAhora = Calendar.getInstance();
            
            Calendar fechaCompara = Calendar.getInstance();
            fechaCompara.setTime(fechaIn);
            
            Date startDate = fechaIn;
            Date endDate   = fechaAhoraTiempo;

            long duration  = endDate.getTime() - startDate.getTime();

            long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
            
            
          if (fechaCompara.equals(fechaAhora))
                             {
                             
                             textoFecha="Hace un momento";
          
                             }

          if (fechaCompara.after(fechaAhora))
                             {
                            //Dias anteriores
                             
                             textoFecha="El dia..fecha hora";
                             
           
                             }

           if (fechaCompara.get(Calendar.DAY_OF_MONTH)==fechaAhora.get(Calendar.DAY_OF_MONTH))
                             {
                              //Hoy
                              
                             textoFecha="Hace "+diffInHours+" horas";
                                 
          
                             }
           
           if(fechaCompara.get(Calendar.DAY_OF_MONTH)<fechaAhora.get(Calendar.DAY_OF_MONTH))
           {
               String mes="";
               
              
                switch (fechaCompara.get(Calendar.MONTH)) {
                    case 0:
                    mes="enero";
                    break;
                    case 1:
                    mes="febrero";
                    break;   
                    case 2:
                    mes="marzo";
                    break; 
                    case 3:
                    mes="abril";
                    break; 
                    case 4:
                    mes="mayo";
                    break; 
                    case 5:
                    mes="junio";
                    break; 
                    case 6:
                    mes="julio";
                    break; 
                    case 7:
                    mes="agosto";
                    break;
                    case 8:
                    mes="septiembre";
                    break; 
                    case 9:
                    mes="octubre";
                    break; 
                    case 10:
                    mes="noviembre";
                    break; 
                    case 11:
                    mes="diciembre";
                    break;
                    
                default:
                    mes="";
                    break;
            }
                
                 
            
                
                
                
                             textoFecha="El dia "+fechaCompara.get(Calendar.DAY_OF_MONTH)+" de "+mes+" a las "+fechaCompara.get(Calendar.HOUR_OF_DAY)+":"+fechaCompara.get(Calendar.MINUTE);
                             
                               
           }
                   
           
        
        
        
        
        
        return textoFecha;
        
    }
    
    
     public List<Notificaciones> verNotificaciones(int idUsuario)
    {
        arNotificaciones.clear();
        arNotificaciones2.clear();
        arNotificaciones = ejbFacade.findAll();        
        
        for(int i=0;i<arNotificaciones.size();i++)
        {
            
    
            if(arNotificaciones.get(i).getIdUsuario().getIdUsuario() == idUsuario)
            {
                arNotificaciones2.add(arNotificaciones.get(i));
            }
        }        
        return arNotificaciones2;
    }
     
     

     
    public void retornaUsuariotest(List<Usuario> lista){
   System.out.println("Entro al metodo...");

        for(int i=0;i<lista.size();i++)
                       {
 System.out.println("Entro al metodo..."+lista.get(i).getIdUsuario());
                               
                       } 
        
       
      
    }
    
    
    public void notificacionesSys(List<Usuario> lista,int id_usuario,String nombre_usuario,int id_identificador,String nombre_identificador,String tipo)
    {
        System.out.println("Inicio creacion de notificaciones");
     
     
//        //En este string se concatenaran los valores para representar el mensaje que recibira el usuario finalmente en sus notificaciones
 
    String mensaje_final="Mensaje de prueba";
//        
//        System.out.println("Entro al try con el tipo"+tipo);
          
        try{
            
            if("Curso_suscrito".equals(tipo))
            {
              
               System.out.println("Nombre curso suscrito-> "+nombre_identificador);
                System.out.println("La lista usuarios tiene un largo de-> "+lista.size());
                mensaje_final=nombre_usuario+" se ha suscrito al curso "+nombre_identificador;
          
                           
                    for(int i=0;i<lista.size();i++)
                       {
                
                                //Metodo esSeguido compara los parametros para verificar si en la tabla inscripcionCurso existen,por ende , solo los usuarios que sean suscriptores al curso 
                                //tendran la notificacion
                           if((ejbFacade.esSuscriptor(lista.get(i).getIdUsuario(),id_identificador)).isEmpty()==false)
                           {
         
                                    Usuario objUsuario = new Usuario();
                                    objUsuario.setIdUsuario(lista.get(i).getIdUsuario());


                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                                    sdf.applyPattern("yyyy/mm/dd");
                                    Date fecha = new Date();




                                    Notificaciones objNotificacion = new Notificaciones();
                                    objNotificacion.setIdNotificacion(null);
                                    objNotificacion.setIdUsuario(objUsuario);
                                    objNotificacion.setTipo(tipo);
                                    objNotificacion.setIdUsuarioTrigger(id_usuario);
                                    objNotificacion.setDetalle(mensaje_final);
                                    objNotificacion.setFecha(fecha);    
                                    objNotificacion.setLeida(0);    
                                    objNotificacion.setIdentificador(id_identificador);

                                    System.out.println("Valores del objeto antes de crear");
                                    System.out.println("ID usuario "+objNotificacion.getIdUsuario().getIdUsuario());
                                    System.out.println("Tipo "+objNotificacion.getTipo());
                                    System.out.println("Detalle "+objNotificacion.getDetalle());
                                    System.out.println("Fecha "+objNotificacion.getFecha());
                                    System.out.println("Leida "+objNotificacion.getLeida());
                                    System.out.println("ID identificador "+objNotificacion.getIdentificador());

                                    ejbFacade.crear(objNotificacion);
                       }
                        
                       }
          
            }
            
            if("Curso_seguido".equals(tipo))
            {
 
           for(int i=0;i<lista.size();i++)
                       {
                
                                //Metodo esSeguido compara los parametros para verificar si en la tabla inscripcionCurso existen,por ende , solo los usuarios que siguen al curso 
                                //tendran la notificacion
                           if((ejbFacade.esSeguidor(lista.get(i).getIdUsuario(),id_identificador)).isEmpty()==false)
                           {
      
                                    Usuario objUsuario = new Usuario();
                                    objUsuario.setIdUsuario(lista.get(i).getIdUsuario());


                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                                    sdf.applyPattern("yyyy/mm/dd");
                                    Date fecha = new Date();




                                    Notificaciones objNotificacion = new Notificaciones();
                                    objNotificacion.setIdNotificacion(null);
                                    objNotificacion.setIdUsuario(objUsuario);
                                    objNotificacion.setTipo(tipo);
                                    objNotificacion.setIdUsuarioTrigger(id_usuario);
                                    objNotificacion.setDetalle(mensaje_final);
                                    objNotificacion.setFecha(fecha);    
                                    objNotificacion.setLeida(0);    
                                    objNotificacion.setIdentificador(id_identificador);

                                    System.out.println("Valores del objeto antes de crear");
                                    System.out.println("ID usuario "+objNotificacion.getIdUsuario().getIdUsuario());
                                    System.out.println("Tipo "+objNotificacion.getTipo());
                                    System.out.println("Detalle "+objNotificacion.getDetalle());
                                    System.out.println("Fecha "+objNotificacion.getFecha());
                                    System.out.println("Leida "+objNotificacion.getLeida());
                                    System.out.println("ID identificador "+objNotificacion.getIdentificador());

                                    ejbFacade.crear(objNotificacion);
                       }
                        
                       }
          
            }
            if("Unidad_nueva_curso".equals(tipo))
            {
              
          System.out.println("Nombre Unidad-> "+nombre_identificador);
          mensaje_final=nombre_usuario+" ha creado una nueva unidad en el curso "+nombre_identificador;
          
            for(int i=0;i<lista.size();i++)
                       {
                
                                //Metodo esSeguido compara los parametros para verificar si en la tabla inscripcionCurso existen,por ende , solo los usuarios que siguen al curso 
                                //tendran la notificacion
                           if((ejbFacade.estaEnCurso(lista.get(i).getIdUsuario(),id_identificador)).isEmpty()==false)
                           {


                                    Usuario objUsuario = new Usuario();
                                    objUsuario.setIdUsuario(lista.get(i).getIdUsuario());


                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                                    sdf.applyPattern("yyyy/mm/dd");
                                    Date fecha = new Date();




                                    Notificaciones objNotificacion = new Notificaciones();
                                    objNotificacion.setIdNotificacion(null);
                                    objNotificacion.setIdUsuario(objUsuario);
                                    objNotificacion.setTipo(tipo);
                                    objNotificacion.setIdUsuarioTrigger(id_usuario);
                                    objNotificacion.setDetalle(mensaje_final);
                                    objNotificacion.setFecha(fecha);    
                                    objNotificacion.setLeida(0);    
                                    objNotificacion.setIdentificador(id_identificador);

                                    System.out.println("Valores del objeto antes de crear");
                                    System.out.println("ID usuario "+objNotificacion.getIdUsuario().getIdUsuario());
                                    System.out.println("Tipo "+objNotificacion.getTipo());
                                    System.out.println("Detalle "+objNotificacion.getDetalle());
                                    System.out.println("Fecha "+objNotificacion.getFecha());
                                    System.out.println("Leida "+objNotificacion.getLeida());
                                    System.out.println("ID identificador "+objNotificacion.getIdentificador());

                                    ejbFacade.crear(objNotificacion);
                       }
                        
                       }
          
            }
            if("Tablero_nueva_discusion".equals(tipo))
            {
              
          System.out.println("Nombre -> "+nombre_identificador);
          mensaje_final=nombre_usuario+" ha creado una discusion en el tablero del curso "+nombre_identificador;
          
            for(int i=0;i<lista.size();i++)
                       {
                
                                //Metodo esSeguido compara los parametros para verificar si en la tabla inscripcionCurso existen,por ende , solo los usuarios que siguen al curso 
                                //tendran la notificacion
                           if((ejbFacade.estaEnCurso(lista.get(i).getIdUsuario(),id_identificador)).isEmpty()==false)
                           {

                                    Usuario objUsuario = new Usuario();
                                    objUsuario.setIdUsuario(lista.get(i).getIdUsuario());


                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                                    sdf.applyPattern("yyyy/mm/dd");
                                    Date fecha = new Date();




                                    Notificaciones objNotificacion = new Notificaciones();
                                    objNotificacion.setIdNotificacion(null);
                                    objNotificacion.setIdUsuario(objUsuario);
                                    objNotificacion.setTipo(tipo);
                                    objNotificacion.setIdUsuarioTrigger(id_usuario);
                                    objNotificacion.setDetalle(mensaje_final);
                                    objNotificacion.setFecha(fecha);    
                                    objNotificacion.setLeida(0);    
                                    objNotificacion.setIdentificador(id_identificador);

                                    System.out.println("Valores del objeto antes de crear");
                                    System.out.println("ID usuario "+objNotificacion.getIdUsuario().getIdUsuario());
                                    System.out.println("Tipo "+objNotificacion.getTipo());
                                    System.out.println("Detalle "+objNotificacion.getDetalle());
                                    System.out.println("Fecha "+objNotificacion.getFecha());
                                    System.out.println("Leida "+objNotificacion.getLeida());
                                    System.out.println("ID identificador "+objNotificacion.getIdentificador());

                                    ejbFacade.crear(objNotificacion);
                       }
                        
                       }
          
            }
            if("Respuesta_comentario_foro".equals(tipo))
            {
              
          System.out.println("Nombre -> "+nombre_identificador);
          mensaje_final=nombre_usuario+" ha respondido tu comentario en el foro "+nombre_identificador;

                                    //Tengo que obtener el ID usuario del posteo..para que solo le llegue a el la notificacion
                                            Usuario objUsuario = new Usuario();

                                    objUsuario.setIdUsuario(id_identificador);
                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                                    sdf.applyPattern("yyyy/mm/dd");
                                    Date fecha = new Date();




                                    Notificaciones objNotificacion = new Notificaciones();
                                    objNotificacion.setIdNotificacion(null);
                                    objNotificacion.setIdUsuario(objUsuario);
                                    objNotificacion.setTipo(tipo);
                                    objNotificacion.setIdUsuarioTrigger(id_usuario);
                                    objNotificacion.setDetalle(mensaje_final);
                                    objNotificacion.setFecha(fecha);    
                                    objNotificacion.setLeida(0);    
                                    objNotificacion.setIdentificador(id_identificador);

                                    System.out.println("Valores del objeto antes de crear");
                                    System.out.println("ID usuario "+objNotificacion.getIdUsuario().getIdUsuario());
                                    System.out.println("Tipo "+objNotificacion.getTipo());
                                    System.out.println("Detalle "+objNotificacion.getDetalle());
                                    System.out.println("Fecha "+objNotificacion.getFecha());
                                    System.out.println("Leida "+objNotificacion.getLeida());
                                    System.out.println("ID identificador "+objNotificacion.getIdentificador());

                                    ejbFacade.crear(objNotificacion);
                                    
            }
            
          
            
        }catch(Exception e)
        {
            System.out.println("ERROR "+e);
          
        }
    }
    
   
    
    public void creacionN()
    {
        System.out.println("Antes de Crear");
          
        try{
            current.setIdNotificacion(null);
            ejbFacade.create(current);
            current = null;
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
     
             public List<Notificaciones> tablaNotificaciones()
         {
             return ejbFacade.findAll();
         }
             
             
             
       public int cantidadNotificaciones(int idUsuario)
                   {
                        arNotificaciones.clear();
                        arNotificaciones2.clear();
                        arNotificaciones = ejbFacade.findAll();        
        
                            for(int i=0;i<arNotificaciones.size();i++)
                            {

                                if(arNotificaciones.get(i).getIdUsuario().getIdUsuario() == idUsuario && arNotificaciones.get(i).getLeida()==0)
                                {
                                    arNotificaciones2.add(arNotificaciones.get(i));
                                }
                            }     
                            
                  return arNotificaciones2.size();
                 }
       
         
        public void onRowEdit(RowEditEvent event) 
        {
            FacesMessage msg = new FacesMessage("Car Edited", ((Notificaciones) event.getObject()).getIdNotificacion().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //((Curso) event.getObject()).setPublicacion(current.getPublicacion());
            //((PublicacionPerfil) event.getObject()).setIdPublicacion(current.getIdPublicacion());
            System.out.println("Imprime publicacion q llega por evento: "+((Notificaciones) event.getObject()).getIdNotificacion());
            //System.out.println("Imprime publicacion q llega por evento: "+((PublicacionPerfil) event.getObject()).getIdPublicacion());
            //current = ((Curso) event.getObject());
            ejbFacade.edit(current); //REFORMULAR?????
        }
          
          
        public void eliminarNotificacion(int id)
        {
            current.setIdNotificacion(id);
            ejbFacade.remove(current);
        
        }
        
            public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Notificaciones) event.getObject()).getIdNotificacion().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Notificaciones getNotificaciones(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Notificaciones.class)
    public static class NotificacionesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NotificacionesController controller = (NotificacionesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "notificacionesController");
            return controller.getNotificaciones(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Notificaciones) {
                Notificaciones o = (Notificaciones) object;
                return getStringKey(o.getIdNotificacion());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Notificaciones.class.getName());
            }
        }

    }

}
