package VIEWS;

import ENTITIES.ForoCategoria;
import ENTITIES.ForoSubcategoria;
import VIEWS.util.JsfUtil;
import VIEWS.util.PaginationHelper;
import MODELS.ForoSubcategoriaFacade;

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

@Named("foroSubcategoriaController")
@SessionScoped
public class ForoSubcategoriaController implements Serializable {

    private ForoSubcategoria current;
    private DataModel items = null;
    @EJB
    private MODELS.ForoSubcategoriaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int id_subCat;
  private boolean verCrear = false;
    public ForoSubcategoriaController() {
    }
    
     public void prepararCrear()
    {
        current = null;
    }
     
         public void precarga()
    {
        List<ForoSubcategoria> arMe;
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
     

    public ForoSubcategoria getSelected() {
        if (current == null) {
            current = new ForoSubcategoria();
            selectedItemIndex = -1;
        }
        return current;
    }

    public int getId_subCat() {
        return id_subCat;
    }

    public void setId_subCat(int id_subCat) {
        this.id_subCat = id_subCat;
    }

    private ForoSubcategoriaFacade getFacade() {
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
        current = (ForoSubcategoria) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ForoSubcategoria();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ForoSubcategoriaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (ForoSubcategoria) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ForoSubcategoriaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ForoSubcategoria) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ForoSubcategoriaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
      public String submit()
{
   
    // blank out the value of the name property
    id_subCat = 0;

    // send the user back to the same page.
    return null;
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

    public ForoSubcategoria getForoSubcategoria(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    
    public List<ForoSubcategoria> listaCategorias(int id_categoria)
    {
        List<ForoSubcategoria> fsc = new ArrayList<ForoSubcategoria>();
        fsc = ejbFacade.findAll();
        List<ForoSubcategoria> fsc2 = new ArrayList<ForoSubcategoria>();
        
        for(int i=0;i<fsc.size();i++)
        {
            if(fsc.get(i).getIdCategoria().getIdCategoria()==id_categoria)
            {
                fsc2.add(fsc.get(i));
            }
        }
        return fsc2;
    }
    
    public boolean getVerCrear() {
        return verCrear;
    }

    public void setVerCrear(boolean verCrear) {
        this.verCrear = verCrear;
    }
    
    
     public void creacionFSC()
    {
        System.out.println("Antes de Crear");
          
        try{
   
        
             SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                sdf.applyPattern("yyyy/mm/dd hh:mm:ss");
                Date fecha = new Date();
              current.setSubcateFecha(fecha);
              System.out.println("Fecha "+current.getSubcateFecha());
              current.setIdSubcategoria(null);
            ejbFacade.create(current);
            current = null;
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
     
             public List<ForoSubcategoria> tablaForoSubCategoria()
         {
             return ejbFacade.findAll();
         }
         
        public void onRowEdit(RowEditEvent event) 
        {
            FacesMessage msg = new FacesMessage("Car Edited", ((ForoSubcategoria) event.getObject()).getIdSubcategoria().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //((Curso) event.getObject()).setPublicacion(current.getPublicacion());
            //((PublicacionPerfil) event.getObject()).setIdPublicacion(current.getIdPublicacion());
            System.out.println("Imprime publicacion q llega por evento: "+((ForoSubcategoria) event.getObject()).getIdSubcategoria());
            //System.out.println("Imprime publicacion q llega por evento: "+((PublicacionPerfil) event.getObject()).getIdPublicacion());
            //current = ((Curso) event.getObject());
            ejbFacade.edit(current); //REFORMULAR?????
        }
          
          
        public void eliminarForoSub(int id)
        {
            current.setIdSubcategoria(id);
            ejbFacade.remove(current);
        
        }
        
            public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((ForoSubcategoria) event.getObject()).getIdSubcategoria().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    
    
    
    
    
    @FacesConverter(forClass = ForoSubcategoria.class)
    public static class ForoSubcategoriaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ForoSubcategoriaController controller = (ForoSubcategoriaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "foroSubcategoriaController");
            return controller.getForoSubcategoria(getKey(value));
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
            if (object instanceof ForoSubcategoria) {
                ForoSubcategoria o = (ForoSubcategoria) object;
                return getStringKey(o.getIdSubcategoria());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ForoSubcategoria.class.getName());
            }
        }

    }

}
