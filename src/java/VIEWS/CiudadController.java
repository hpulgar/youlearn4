package VIEWS;

import ENTITIES.Archivo;
import ENTITIES.Ciudad;
import VIEWS.util.JsfUtil;
import VIEWS.util.PaginationHelper;
import MODELS.CiudadFacade;

import java.io.Serializable;
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

@Named("ciudadController")
@SessionScoped
public class CiudadController implements Serializable {

    private Ciudad current;
    private DataModel items = null;
    @EJB
    private MODELS.CiudadFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int idCiudad;
    private List<Ciudad> arCiudad = new ArrayList();
    private List<Ciudad> arCiudad2 = new ArrayList();
    private boolean verCrear = false;

    public CiudadController() {
    }
    
     public void prepararCrear()
    {
        current = null;
    }
     
     public void precarga()
    {
        List<Ciudad> arMe;
        arMe = ejbFacade.findAll();
       
        for(int i =0;i<arMe.size();i++)
        {
            current = arMe.get(i);
        }
                
    }

    public Ciudad getSelected() {
        if (current == null) {
            current = new Ciudad();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<Ciudad> getArCiudad2() {
        return arCiudad2;
    }

    public void setArCiudad2(List<Ciudad> arCiudad2) {
        this.arCiudad2 = arCiudad2;
    }

    
    public List<Ciudad> getArCiudad() {
        return arCiudad;
    }

    public void setArCiudad(List<Ciudad> arCiudad) {
        this.arCiudad = arCiudad;
    }
    

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    
    private CiudadFacade getFacade() {
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
        current = (Ciudad) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Ciudad();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CiudadCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Ciudad) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CiudadUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Ciudad) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CiudadDeleted"));
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

    public List<Ciudad> verCity(int idPais)
    {
       arCiudad.clear();
       arCiudad2.clear();
       arCiudad = ejbFacade.findAll();
        
        for(int i=0;i<arCiudad.size();i++)
        {
            if(arCiudad.get(i).getIdPais().getIdPais() == idPais)
            {
                arCiudad2.add(arCiudad.get(i));
            }
        }
        return arCiudad2;
    }
    
    public boolean getVerCrear() {
        return verCrear;
    }
    
    public void cargaDatos(int id)
    {
        current = ejbFacade.find(id);
    }

    public void setVerCrear(boolean verCrear) {
        this.verCrear = verCrear;
    }
    
    
     public void creacionC()
    {
        System.out.println("Antes de Crear");
          
        try{
   
            current.setIdCiudad(null);
            ejbFacade.create(current);
            current = null;
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
     
             public List<Ciudad> tablaCiudad()
         {
             return ejbFacade.findAll();
         }
         
        public void onRowEdit(RowEditEvent event) 
        {
            FacesMessage msg = new FacesMessage("Car Edited", ((Ciudad) event.getObject()).getIdCiudad().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //((Curso) event.getObject()).setPublicacion(current.getPublicacion());
            //((PublicacionPerfil) event.getObject()).setIdPublicacion(current.getIdPublicacion());
            System.out.println("Imprime publicacion q llega por evento: "+((Ciudad) event.getObject()).getIdCiudad());
            //System.out.println("Imprime publicacion q llega por evento: "+((PublicacionPerfil) event.getObject()).getIdPublicacion());
            //current = ((Curso) event.getObject());
            ejbFacade.edit(current); //REFORMULAR?????
        }
          
          
        public void eliminarCiudad(int id)
        {
            current.setIdCiudad(id);
            ejbFacade.remove(current);
        
        }
        
            public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edicion cancelada", ((Ciudad) event.getObject()).getIdCiudad().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Ciudad getCiudad(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Ciudad.class)
    public static class CiudadControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CiudadController controller = (CiudadController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ciudadController");
            return controller.getCiudad(getKey(value));
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
            if (object instanceof Ciudad) {
                Ciudad o = (Ciudad) object;
                return getStringKey(o.getIdCiudad());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Ciudad.class.getName());
            }
        }

    }

}
