package VIEWS;

import ENTITIES.MasterComentario;
import ENTITIES.MasterRespuestas;
import ENTITIES.Usuario;
import VIEWS.util.JsfUtil;
import VIEWS.util.PaginationHelper;
import MODELS.MasterRespuestasFacade;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.event.RowEditEvent;

@Named("masterRespuestasController")
@SessionScoped
public class MasterRespuestasController implements Serializable {

    private MasterRespuestas current;
    private DataModel items = null;
    @EJB
    private MODELS.MasterRespuestasFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<MasterRespuestas> listaRespuestas = new ArrayList();
    private boolean ver = false;
     private boolean verCrear = false;
    
    

    public MasterRespuestasController() {
    }
    
    public void prepararCrear()
    {
        current = null;
    }
    
        public void precarga()
    {
        List<MasterRespuestas> arMe;
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

    public boolean getVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }

    
    public List<MasterRespuestas> getListaRespuestas() {
        return listaRespuestas;
    }

    public void setListaRespuestas(List<MasterRespuestas> listaRespuestas) {
        this.listaRespuestas = listaRespuestas;
    }
    
    public MasterRespuestas getSelected() {
        if (current == null) {
            current = new MasterRespuestas();
            selectedItemIndex = -1;
        }
        return current;
    }

    private MasterRespuestasFacade getFacade() {
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
        current = (MasterRespuestas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new MasterRespuestas();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MasterRespuestasCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (MasterRespuestas) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MasterRespuestasUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (MasterRespuestas) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MasterRespuestasDeleted"));
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

    public MasterRespuestas getMasterRespuestas(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    
    public void verInput()
    {
        this.ver=true;
        
    }
    
    private void hideInput()
    {
        this.setVer(false);
    }
    
    public void crearRespuesta(int id_comentario,int id_usuario){
        System.out.println("ENTRA AL CREAR       --------------------");
        //if(!current.getRespuesta().isEmpty())
        //{
            try
            {
                //id_respuesta
                //id_comentario
                //reespuesta
                //id_usuario
                //fecha respuesta
                
                Usuario ou = new Usuario();
                ou.setIdUsuario(id_usuario);
                MasterComentario mc= new MasterComentario();
                mc.setIdComentario(id_comentario);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
                Date date = new Date();
                String fecha = dateFormat.format(date);


                current.setIdComentario(mc);
                current.setIdUsuario(ou);
                current.setFechaRespuesta(dateFormat.parse(fecha));

                System.out.println("LA RESPUESTA A CREAR "+current.getRespuesta());
               
                
                if(current.getRespuesta() != null)
                {
                    System.out.println("ID DEL COMENTARIO A PARA RESPUESTA ->>>>>>>>>>>"+id_comentario);
                    System.out.println("LA RESPUESTA A CREAR "+current.getRespuesta());
                    getFacade().create(current);
                    current = null;
                }
                
            }catch(Exception e){
                System.out.println("Si tira error es este -----------------> "+e);
            }
    }
    
//     public List<MasterRespuestas> cargaRespuesta(int id_comentario)
//    {    
//        //listaRespuestas.clear();
//        List<MasterRespuestas> mrl = new ArrayList(); 
//        mrl.clear();   
//        
//        listaRespuestas.clear();
//        listaRespuestas = ejbFacade.findAll();                
//        for(int i=0;i<listaRespuestas.size();i++)
//        {
//            if((listaRespuestas.get(i).getIdComentario().getIdComentario())==id_comentario)
//                {
//                   
//                   
//                   mrl.add(listaRespuestas.get(i));
//                } 
//        }
//        return mrl;
//    }
    
     public List<MasterRespuestas> cargaRespuesta(int id_comentario)
    {    
       
        listaRespuestas = ejbFacade.verRespuestas(id_comentario);                
        
        return listaRespuestas;
    }
     
     
     public String crearRespuesta2(int id_comentario,int id_usuario){
    
      
            try
            {

               

                MasterRespuestas mr = new MasterRespuestas();

                Usuario ou = new Usuario();
                ou.setIdUsuario(id_usuario);

                MasterComentario mc= new MasterComentario();
                mc.setIdComentario(id_comentario);



                DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
                Date date = new Date();
                String fecha = dateFormat.format(date);


                current.setIdComentario(mc);
                current.setIdUsuario(ou);
                current.setFechaRespuesta(dateFormat.parse(fecha));

                
               
                
                if(!current.getRespuesta().isEmpty())
                {
                    System.out.println("ID DEL COMENTARIO A PARA RESPUESTA ->>>>>>>>>>>"+id_comentario);
                    System.out.println("LA RESPUESTA A CREAR "+current.getRespuesta());
                    getFacade().create(current);
                    current = null;
                }
                return "/perfil.xhtml";

            }catch(Exception e){

                System.out.println("Si tira error es este -----------------> "+e);
                return "/perfil.xhtml";
            }
           
        
        
    }
     
     public boolean getVerCrear() {
        return verCrear;
    }

    public void setVerCrear(boolean verCrear) {
        this.verCrear = verCrear;
    }
    
    
     public void creacionMR()
    {
        System.out.println("Antes de Crear");
          
        try{
   
           current.setIdRespuestas(null);
            ejbFacade.create(current);
            current = null;
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
     
             public List<MasterRespuestas> tablaMasterR()
         {
             return ejbFacade.findAll();
         }
         
        public void onRowEdit(RowEditEvent event) 
        {
            FacesMessage msg = new FacesMessage("Car Edited", ((MasterRespuestas) event.getObject()).getIdRespuestas().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //((Curso) event.getObject()).setPublicacion(current.getPublicacion());
            //((PublicacionPerfil) event.getObject()).setIdPublicacion(current.getIdPublicacion());
            System.out.println("Imprime publicacion q llega por evento: "+((MasterRespuestas) event.getObject()).getIdRespuestas());
            //System.out.println("Imprime publicacion q llega por evento: "+((PublicacionPerfil) event.getObject()).getIdPublicacion());
            //current = ((Curso) event.getObject());
            ejbFacade.edit(current); //REFORMULAR?????
        }
          
          
        public void eliminaMasterRespuesta(int id)
        {
            current.setIdRespuestas(id);
            ejbFacade.remove(current);
        
        }
        
            public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edicion cancelada", ((MasterRespuestas) event.getObject()).getIdRespuestas().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
     
     
     
     
     
     
     
     
     

    @FacesConverter(forClass = MasterRespuestas.class)
    public static class MasterRespuestasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MasterRespuestasController controller = (MasterRespuestasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "masterRespuestasController");
            return controller.getMasterRespuestas(getKey(value));
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
            if (object instanceof MasterRespuestas) {
                MasterRespuestas o = (MasterRespuestas) object;
                return getStringKey(o.getIdRespuestas());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + MasterRespuestas.class.getName());
            }
        }

    }

}
