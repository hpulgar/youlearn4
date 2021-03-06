package VIEWS;

import ENTITIES.Curso;
import ENTITIES.Tablero;
import ENTITIES.Usuario;
import VIEWS.util.JsfUtil;
import VIEWS.util.PaginationHelper;
import MODELS.TableroFacade;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import java.util.*;
import javax.faces.application.FacesMessage;
import org.primefaces.event.RowEditEvent;

@Named("tableroController")
@SessionScoped
public class TableroController implements Serializable {

    private Tablero current;
    private DataModel items = null;
    @EJB
    private MODELS.TableroFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Tablero> arTablero = new ArrayList();
    private List<Tablero> arTablero2= new ArrayList();
    private int idTablero;
    
    
    
    public TableroController() {
    }

    public List<Tablero> getArTablero() {
        return arTablero;
    }

    public List<Tablero> getArTablero2() {
        return arTablero2;
    }

    public void setArTablero2(List<Tablero> arTablero2) {
        this.arTablero2 = arTablero2;
    }

    public void setArTablero(List<Tablero> arTablero) {
        this.arTablero = arTablero;
    }

    public int getIdTablero() {
        return idTablero;
    }

    public void setIdTablero(int idTablero) {
        this.idTablero = idTablero;
    }

    
    public Tablero getSelected() {
        if (current == null) {
            current = new Tablero();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TableroFacade getFacade() {
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
        current = (Tablero) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Tablero();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TableroCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Tablero) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TableroUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Tablero) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TableroDeleted"));
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

    public Tablero getTablero(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    //////////////////////////////////////////////////////////////////////
    
    //    int id tablero
    //    int id_usuario
    //    int id_curso
    //    String titulo
    //    String escripcion        
    
      public void verUno(int id)
     {
          Tablero tp = ejbFacade.verT(id).get(0);
          current = tp;
     }
    
    public List<Tablero> verTableros(int idCurso)
    {
        arTablero.clear();
        arTablero2.clear();
        arTablero = ejbFacade.findAll();        
        
        for(int i=0;i<arTablero.size();i++)
        {
            if(arTablero.get(i).getIdCurso().getIdCurso() == idCurso)
            {
                arTablero2.add(arTablero.get(i));
            }
        }        
        return arTablero2;
    }
    
    public boolean esCreador(int idUsuario)
    {
         if(ejbFacade.esCreador(idUsuario)==true)
         {
             return true;
         }
         else
         {
             return false;
         }
     
        
    }
   
    
    public String prepareEdicionTablero(int id_tablero) {
        current = ejbFacade.find(id_tablero);
        return "tablero_editar.xhtml";
    }
    
     public String actualizaTalero() {
       
            System.out.println("Actualizar tablero...");
             try {
            getFacade().edit(current);
          
            return "tablero_detalle.xhtml";
            
        } catch (Exception e) {
           
            return null;
        }
         
            
       
    }
    
    public String verDetalle(int idTablero)
    {
        this.setIdTablero(idTablero);
        return "/tablero_detalle.xhtml";
    }
    
//    public String verCrearTab()
//    {
//        String
//    }
    
    
    public List<Tablero> unTablero(int id_tablero)
    {
        
        arTablero.clear();
        arTablero = ejbFacade.verT(id_tablero);
        
        return arTablero;
        
    }
    
    
     public String crearTablero(int id_curso,int id_usuario)
    {
        
        
        try{
        //    int id tablero auto 
        //    int id_usuario parametro
        //    int id_curso  parametro 
        //    String titulo selected
        //    String escripcion selected
        //    DATETIME fecha
        
        
        
        Usuario ou = new Usuario();
        ou.setIdUsuario(id_usuario);
      
        Curso oc = new Curso();
        oc.setIdCurso(id_curso);
 
        DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
        Date date = new Date();
        String fecha = dateFormat.format(date);
    
        current.setIdUsuario(ou);
        current.setIdCurso(oc);
        current.setFecha(dateFormat.parse(fecha));
        
        getFacade().create(current);
        current = null;
        
        return "/tablero.xhtml";
        
        }catch(Exception e)
        {
            System.out.println("EL ERRORR "+ e);
            return "/tablero_crear.xhtml";
        }
            
    }
    
    /////////////////////////////////////////////MANTENEDOR
    
    public List<Tablero> tablaTablero()
    {
        return ejbFacade.findAll();
    }    
    
    public void onRowEdit(RowEditEvent event)
    {
        FacesMessage msg = new FacesMessage("Inscripcion Editada",((Tablero) event.getObject()).getIdTablero().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        current.setIdTablero(((Tablero) event.getObject()).getIdTablero());
        ejbFacade.edit(current);
        current= null;
        
    }
    
    public void onRowCancel(RowEditEvent event)
    {
        FacesMessage msg = new FacesMessage("Edicion Cancelada",((Tablero) event.getObject()).getIdTablero().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void creacionTablero()
    {
        
        try
        {
            current.setIdTablero(null);
            ejbFacade.create(current);
            current = null;
            
        }catch(Exception e)
        {
            System.out.println("Error al crear el Permiso "+e);
        }
    }
    
    public void eliminarTablero(int id)
    {
        current.setIdTablero(id);
        ejbFacade.remove(current);
        current = null;
    }
    
    public void cargaDatos(int id)
    {
        current = ejbFacade.find(id);
    }
    
    public void prepararCrear()
    {
        current = null;
    }
    
    public void precarga()
    {
        List<Tablero> arMe;
        arMe = ejbFacade.findAll();
        for(int i =0;i<arMe.size();i++)
        {
            current = arMe.get(i);
        }
                
    }
    
    //////////////////////////////////////////
    
    
    @FacesConverter(forClass = Tablero.class)
    public static class TableroControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TableroController controller = (TableroController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tableroController");
            return controller.getTablero(getKey(value));
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
            if (object instanceof Tablero) {
                Tablero o = (Tablero) object;
                return getStringKey(o.getIdTablero());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tablero.class.getName());
            }
        }

    }

}
