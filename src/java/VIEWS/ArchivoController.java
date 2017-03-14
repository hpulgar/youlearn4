package VIEWS;

import ENTITIES.Archivo;
import ENTITIES.Contenidos;
import ENTITIES.IdentificadorArchivo;
import ENTITIES.TipoArchivo;
import ENTITIES.Usuario;
import VIEWS.util.JsfUtil;
import VIEWS.util.PaginationHelper;
import MODELS.ArchivoFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

@Named("archivoController")
@SessionScoped
public class ArchivoController implements Serializable {

    private Archivo current;
    private DataModel items = null;
    @EJB
    private MODELS.ArchivoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private boolean verCrear = false;
    private List<Archivo> arArchivo = new ArrayList();
    private List<Archivo> arArchivo2= new ArrayList();

    public ArchivoController() {
    }
    
     public void prepararCrear()
    {
        current = null;
    }

    public Archivo getSelected() {
        if (current == null) {
            current = new Archivo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ArchivoFacade getFacade() {
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
        current = (Archivo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Archivo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ArchivoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Archivo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ArchivoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Archivo) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ArchivoDeleted"));
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
    
     public void cargaDatos(int id)
    {
        current = ejbFacade.find(id);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Archivo getArchivo(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    
    public boolean getVerCrear() {
        return verCrear;
    }

    public void setVerCrear(boolean verCrear) {
        this.verCrear = verCrear;
    }
    
    
      public void actualizaNombre(int id_archivo,String descripcion)
    {
       
          
        try{
            

            current = ejbFacade.find(id_archivo);
            current.setDescripcion(descripcion);
          
            ejbFacade.crear(current);
           
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
      
            public List<Archivo> archivosNoAprobadosTodos()
    {
        return ejbFacade.archivosNoAprobadoTodos();
    }
         
 
            public List<Archivo> archivosAprobadosTodos()
    {
        return ejbFacade.archivosAprobadoTodos();
    }
    
            
             public void desaprobarArchivo(int idArchivo)
    {
       
          
        try{
            
            System.out.println("Desaprobando "+idArchivo);
            current = ejbFacade.find(idArchivo);
            current.setAutorizado(false);
          
            ejbFacade.crear(current);
           
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
             
                    public void aprobarArchivo(int idArchivo)
    {
       
          
        try{
            
            System.out.println("Aprobando "+idArchivo);
            current = ejbFacade.find(idArchivo);
            current.setAutorizado(true);
          
            ejbFacade.crear(current);
           
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
    
     public void creacionA()
    {
        System.out.println("Antes de Crear");
          
        try{
   
        
             SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                sdf.applyPattern("yyyy/mm/dd hh:mm:ss");
                Date fecha = new Date();
              current.setFecha(fecha);
              System.out.println("Fecha "+current.getFecha());
             current.setIdArchivo(null);
            ejbFacade.create(current);
            current = null;
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
     
     public void listFiles(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            if (file.isFile()){
                System.out.println(file.getName());
            }
        }
    }
     
        public List<Archivo> verArchivos(int idIdentificador,int idAux)
    {
        arArchivo.clear();
        arArchivo2.clear();
        arArchivo = ejbFacade.findAll();        
        
        
                for(int i=0;i<arArchivo.size();i++)
                {
                    if(arArchivo.get(i).getIdIdentificadorArchivo().getIdIdentificadorArchivo()== idIdentificador && arArchivo.get(i).getAutorizado()==true && arArchivo.get(i).getIdAux()== idAux )
                    {
                        arArchivo2.add(arArchivo.get(i));
                    }
                }        
        
        
        
        return arArchivo2;
    }
        
      
        
        
     
      public void upload(FileUploadEvent event) {  
       ExternalContext extContext=FacesContext.getCurrentInstance().getExternalContext();
        //FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        //FacesContext.getCurrentInstance().addMessage(null, msg);
            
        try {
            
            
                        int idIdentificador=0;
                       
                        String tempDetalle = (String) event.getComponent().getAttributes().get("tempDetalle");
                        
                        if(tempDetalle.equalsIgnoreCase("archivo"))
                                {
                                    idIdentificador=3;
                                }
                        
                         if(tempDetalle.equalsIgnoreCase("foto_perfil"))
                                {
                                    idIdentificador=2;
                                }
                         
                           if(tempDetalle.equalsIgnoreCase("muro"))
                                {
                                    idIdentificador=1;
                                }
                        
                        int idAux =  (int) event.getComponent().getAttributes().get("idAux");
                        
                  
                         //Identificador Archivo Contenidos
                        if(idIdentificador==3){
                    
                         String nomcurso = (String) event.getComponent().getAttributes().get("nombreCurso"); 
                        String nomunidad = (String) event.getComponent().getAttributes().get("nombreUnidad");
                        
                        
                         //Path directorio .WAR de proyecto
                         File dataDir = new File(extContext.getRealPath("//files//"));
                        // File dataDir = new File(System.getProperty("jboss.server.home.url"), "uploads");
                         
                        // String directorio = ""+dataDir+"/"+nomcurso+"/"+nomunidad+"/";
                       //String con nombre de curso y unidad separados para distintos usos
                         String pathDatos = ""+nomcurso+"/"+nomunidad+"/";
                         //Con este string se hara la insercion en la DB
                         String directorioDB = "http://localhost/archivos/"+nomcurso+"/"+nomunidad+"/";
                         
                         //Configuracion con servidor apache
                         String filepath="/archivos/";
                         //Directorio root de Apache,soloa accesible por el aplicativo
                         String apacheDir ="C://Apache24//htdocs//";    
             
                        String directorio = apacheDir+filepath+pathDatos;
                         boolean existeDirectorio = new File(directorio).exists(); 
            

                         if(existeDirectorio==false)
                         {
                             System.out.println("El directorio "+directorio+" no existe, creando....");
                         //new File(directorio).mkdirs();
                         System.out.println("Prueba de creacion en directorio apache");
                         System.out.println("En el directorio C:\\Apache24\\htdocs\\archivos"+pathDatos);
                         new File(apacheDir+filepath+pathDatos).mkdirs();
                         }
                         else
                         {
                              System.out.println("El directorio "+directorio+" si existe");

                         }

                         UploadedFile tfile = event.getFile();
                         String str = tfile.getFileName();
                         //String prefijo = FilenameUtils.getBaseName(str);
                         String extension = FilenameUtils.getExtension(str);
                  
                         copyFile(nomunidad+"."+extension, event.getFile().getInputstream(),directorio,idAux,idIdentificador);
                         System.out.println("Archivos en directorio");
                       // listFiles(directorio);
                        archivoSubido(idIdentificador, extension, nomunidad, directorioDB,idAux);
                        }
                        
                        
                        
                         if(idIdentificador==2){
                    
                     
                        
                        
                         //Path directorio .WAR de proyecto
                         File dataDir = new File(extContext.getRealPath("//files//"));
                        // File dataDir = new File(System.getProperty("jboss.server.home.url"), "uploads");
                         
                        // String directorio = ""+dataDir+"/"+nomcurso+"/"+nomunidad+"/";
                       //String con nombre de curso y unidad separados para distintos usos
                         String pathDatos = ""+idAux+"/";
                         //Con este string se hara la insercion en la DB
                         String directorioDB = "http://localhost/foto_perfil/"+idAux+"/";
                         
                         //Configuracion con servidor apache
                         String filepath="/foto_perfil/";
                         //Directorio root de Apache,soloa accesible por el aplicativo
                         String apacheDir ="C://Apache24//htdocs//";    
             
                        String directorio = apacheDir+filepath+pathDatos;
                         boolean existeDirectorio = new File(directorio).exists(); 
            

                         if(existeDirectorio==false)
                         {
                             System.out.println("El directorio "+directorio+" no existe, creando....");
                         //new File(directorio).mkdirs();
                         System.out.println("Prueba de creacion en directorio apache");
                         System.out.println("En el directorio C:\\Apache24\\htdocs\\foto_perfil"+pathDatos);
                         new File(apacheDir+filepath+pathDatos).mkdirs();
                         }
                         else
                         {
                              System.out.println("El directorio "+directorio+" si existe");

                         }

                         UploadedFile tfile = event.getFile();
                         String str = tfile.getFileName();
                         //String prefijo = FilenameUtils.getBaseName(str);
                         String extension = FilenameUtils.getExtension(str);
                  
                         copyFile("."+extension, event.getFile().getInputstream(),directorio,idAux,idIdentificador);
                         System.out.println("Archivos en directorio");
                       // listFiles(directorio);
                        archivoSubido(idIdentificador, extension, "imagen", directorioDB,idAux);
                        }
                        
                         
                }       catch (IOException e) {
                        e.printStackTrace();
                }
 
    }  
 
    public void copyFile(String fileName, InputStream in,String directorioFinal,int idAux,int idIdentificadorArchivo) {
           try {
               
               if(idIdentificadorArchivo==3)
               {
                
                  
                  
                                List<Archivo> obtenerArchivos = ejbFacade.obtenerArchivos(idIdentificadorArchivo,idAux);

                                 int cantidadArchivos = obtenerArchivos.size();
                                //Necesito obtener la cantidad de archivos actuales para que no se repitan...la consulta podría ser mejor.(OBTENER CANT_ARCHIVOS DONDE UNIDAD=UNIDAD_CURSO)



                                    String nombre_final_a=cantidadArchivos+"_"+fileName;

                                 // write the inputStream to a FileOutputStream
                                 System.out.println("Creando archivo en directorio :"+directorioFinal);
                                 System.out.println("Ubicacion final archivo :"+directorioFinal+"/"+fileName);
                                 System.out.println("nombre final archivo :"+nombre_final_a);
                                 OutputStream out = new FileOutputStream(new File(directorioFinal + nombre_final_a));

                                 int read = 0;
                                 byte[] bytes = new byte[1024];

                                 while ((read = in.read(bytes)) != -1) {
                                     out.write(bytes, 0, read);
                                 }

                                 in.close();
                                 out.flush();
                                 out.close();

                                 System.out.println("Archivo creado exitosamente!");
                
               }
               
                if(idIdentificadorArchivo==2)
               {
                
                  
                  
                               

                 
                                //Necesito obtener la cantidad de archivos actuales para que no se repitan...la consulta podría ser mejor.(OBTENER CANT_ARCHIVOS DONDE UNIDAD=UNIDAD_CURSO)



                                    String nombre_final_a="imagen"+fileName;

                                 // write the inputStream to a FileOutputStream
                                 System.out.println("Creando archivo en directorio :"+directorioFinal);
                                 System.out.println("Ubicacion final archivo :"+directorioFinal+"/"+fileName);
                                 System.out.println("nombre final archivo :"+nombre_final_a);
                                 OutputStream out = new FileOutputStream(new File(directorioFinal + nombre_final_a));

                                 int read = 0;
                                 byte[] bytes = new byte[1024];

                                 while ((read = in.read(bytes)) != -1) {
                                     out.write(bytes, 0, read);
                                 }

                                 in.close();
                                 out.flush();
                                 out.close();

                                 System.out.println("Archivo creado exitosamente!");
                
               }
                } 
           
           
           catch (IOException e) {
                System.out.println(e.getMessage());
                }
    }
     
         public void archivoSubido(int idIdentificador,String extension,String nom_archivo,String ubicacion,int idAux)
    {
        System.out.println("Antes de Crear");
          
        try{
            
            
     System.out.println("Metodo Try");
            int tipoArchivo = 0;
            boolean no_autorizado = false;
            String descripcion ="Sin descripcion";
            
                List<Archivo> obtenerArchivos = ejbFacade.obtenerArchivos(idIdentificador,idAux);
               
                int cantidadArchivos = obtenerArchivos.size();
        
                   //Necesito obtener la cantidad de archivos actuales para que no se repitan...la consulta podría ser mejor.(OBTENER CANT_ARCHIVOS DONDE UNIDAD=UNIDAD_CURSO)
                  String nombre_final_a="";
                  if(idIdentificador==3)
                  {
                  nombre_final_a=cantidadArchivos+"_"+nom_archivo+"."+extension;
                  }
                  
                  if(idIdentificador==2)
                  {
                       nombre_final_a=nom_archivo+"."+extension;
                  }
            
                if(null != extension){
             switch (extension) {
                case "jpg":
                    tipoArchivo=1;
                    break;
                case "gif":
                    tipoArchivo=2;
                    break;
                case "png":
                    tipoArchivo=3;
                    break;
                case "pdf":
                    tipoArchivo=4;
                    break;
                case "ppt":
                    tipoArchivo=5;
                    break;
                case "doc":
                    tipoArchivo=6;
                    break;
                case "xls":
                    tipoArchivo=7;
                    break;
                default:
                    tipoArchivo=8;
                    break;
            }
                }
             
            SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.setTimeZone(new SimpleTimeZone(-3, "GMT"));
                sdf.applyPattern("yyyy/mm/dd");
                Date fecha = new Date();
                
           
             IdentificadorArchivo Identificador_temp = new IdentificadorArchivo();
             Identificador_temp.setIdIdentificadorArchivo(idIdentificador);
             
             TipoArchivo tip = new TipoArchivo();
             tip.setIdTipo(tipoArchivo);
             
             
        
             
             System.out.println("Comienza seteo de variables en objeto current");
 
   
             
             Archivo objArchivo = new Archivo();
             
             System.out.println("Identificador seteado OK");
             objArchivo.setIdIdentificadorArchivo(Identificador_temp);
             
             objArchivo.setIdAux(idAux);
             
            objArchivo.setIdTipoArchivo(tip);
            System.out.println("Tipo OK");
             objArchivo.setNomArchivo(nom_archivo);
             System.out.println("Nombre OK");
             objArchivo.setDescripcion(descripcion);
             System.out.println("Descripion OK"); 
             objArchivo.setUbicacion(ubicacion+nombre_final_a);
             System.out.println("Ubicacion OK");
             objArchivo.setAutorizado(no_autorizado);
             System.out.println("Autorizado OK");     
            objArchivo.setFecha(fecha);
            System.out.println("Fecha OK");
                      
           if(idIdentificador==2)
           {
               ejbFacade.updateUsuario(idAux, ""+ubicacion+nombre_final_a+"");
              
           
           }
     
            ejbFacade.create(objArchivo);
            
           
         
            
        }catch(Exception e)
        {
            System.out.println("ERRRROOORR "+e);
          
        }
    }
     
             public List<Archivo> tablaArchivo()
         {
             return ejbFacade.findAll();
         }
         
        public void onRowEdit(RowEditEvent event) 
        {
            FacesMessage msg = new FacesMessage("Car Edited", ((Archivo) event.getObject()).getIdArchivo().toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);

            //((Curso) event.getObject()).setPublicacion(current.getPublicacion());
            //((PublicacionPerfil) event.getObject()).setIdPublicacion(current.getIdPublicacion());
            System.out.println("Imprime publicacion q llega por evento: "+((Archivo) event.getObject()).getIdArchivo());
            //System.out.println("Imprime publicacion q llega por evento: "+((PublicacionPerfil) event.getObject()).getIdPublicacion());
            //current = ((Curso) event.getObject());
            ejbFacade.edit(current); //REFORMULAR?????
        }
          
          
        public void eliminarArchivo(int id)
        {
            current = null;
            current = ejbFacade.find(id);
            ejbFacade.borrarArchivo(current);
            current = null;
        }
        
            public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Archivo) event.getObject()).getIdArchivo().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
            
                public void precarga()
    {
        List<Archivo> arMe;
        arMe = ejbFacade.findAll();
       
        for(int i =0;i<arMe.size();i++)
        {
            current = arMe.get(i);
        }
                
    }
                
    
    @FacesConverter(forClass = Archivo.class)
    public static class ArchivoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ArchivoController controller = (ArchivoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "archivoController");
            return controller.getArchivo(getKey(value));
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
            if (object instanceof Archivo) {
                Archivo o = (Archivo) object;
                return getStringKey(o.getIdArchivo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Archivo.class.getName());
            }
        }

    }

}
