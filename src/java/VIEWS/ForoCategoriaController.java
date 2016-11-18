package VIEWS;

import ENTITIES.Curso;
import ENTITIES.ForoCategoria;
import VIEWS.util.JsfUtil;
import VIEWS.util.PaginationHelper;
import MODELS.ForoCategoriaFacade;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

@Named("foroCategoriaController")
@SessionScoped
public class ForoCategoriaController implements Serializable {

    private ForoCategoria current;
    private DataModel items = null;
    @EJB
    private MODELS.ForoCategoriaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int id_cat;
    private boolean verCrear = false;

    public ForoCategoriaController() {
    }
    
     public void prepararCrear()
    {
        current = null;
    }
     
        public void precarga()
    {
        List<ForoCategoria> arMe;
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
     

    public ForoCategoria getSelected() {
        if (current == null) {
            current = new ForoCategoria();
            selectedItemIndex = -1;
        }
        return current;
    }

    public int getId_cat() {
        return id_cat;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }
    
    private ForoCategoriaFacade getFacade() {
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
        current = (ForoCategoria) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ForoCategoria();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ForoCategoriaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (ForoCategoria) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ForoCategoriaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ForoCategoria) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ForoCategoriaDeleted"));
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

    public ForoCategoria getForoCategoria(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    
    public List<ForoCategoria> listaCategorias()
    {
        List<ForoCategoria> fc = new ArrayList<ForoCategoria>();
        fc = ejbFacade.findAll();
        return fc;
    }
    
    public void cprint()
    {
        System.out.println("aca el valor del select "+ this.getId_cat());
    }

    public boolean getVerCrear() {
        return verCrear;
    }

    public void setVerCrear(boolean verCrear) {
        this.verCrear = verCrear;
    }
    
    
     public void creacionFC()
    {
        System.out.println("Antes de Crear");
          
        try{
   
        
             SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                sdf.applyPattern("yyyy/mm/dd hh:mm:ss");
                Date fecha = new Date();
              current.setCateFecha(fecha);
              System.out.println("fecha publicacion "+current.getCateFecha());
              current.setIdCategoria(null);
            ejbFacade.create(current);
            current = null;
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
     
             public List<ForoCategoria> tablaForoCategoria()
         {
             return ejbFacade.findAll();
         }
         
        public void onRowEdit(RowEditEvent event) 
        {
            FacesMessage msg = new FacesMessage("Car Edited", ((ForoCategoria) event.getObject()).getIdCategoria().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //((Curso) event.getObject()).setPublicacion(current.getPublicacion());
            //((PublicacionPerfil) event.getObject()).setIdPublicacion(current.getIdPublicacion());
            System.out.println("Imprime publicacion q llega por evento: "+((ForoCategoria) event.getObject()).getIdCategoria());
            //System.out.println("Imprime publicacion q llega por evento: "+((PublicacionPerfil) event.getObject()).getIdPublicacion());
            //current = ((Curso) event.getObject());
            ejbFacade.edit(current); //REFORMULAR?????
        }
          
          
        public void eliminarForo(int id)
        {
            current.setIdCategoria(id);
            ejbFacade.remove(current);
        
        }
        
            public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((ForoCategoria) event.getObject()).getIdCategoria().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
        
    
            
    
   
    @FacesConverter(forClass = ForoCategoria.class)
    public static class ForoCategoriaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ForoCategoriaController controller = (ForoCategoriaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "foroCategoriaController");
            return controller.getForoCategoria(getKey(value));
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
            if (object instanceof ForoCategoria) {
                ForoCategoria o = (ForoCategoria) object;
                return getStringKey(o.getIdCategoria());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ForoCategoria.class.getName());
            }
        }

    }

}
