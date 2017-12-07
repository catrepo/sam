package paq_adquisicion;
/**
 *
 * @author Andres Redroban soy nina
 */
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class Adquisiciones extends Pantalla {
    private Tabla tab_adquisiones = new Tabla();
    private Tabla tab_certificacion = new Tabla();
    private Tabla tab_compra_bienes = new Tabla();
    
    @EJB
    private final  ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    public Adquisiciones(){
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        
      tab_adquisiones.setId("tab_adquisiones");   //identificador
      tab_adquisiones.setTabla("adq_compra", "ide_adcomp", 1);
      List lista = new ArrayList();
      List lista1 = new ArrayList();
      List lista2 = new ArrayList();
        Object fila1[] = {"1", "SI"};
        Object fila2[] = {"2","NO"};
        Object fila5[] = {"1","COMPRA EN STOCK"};
        Object fila6[] = {"2","COMPRA DE CONSUMO DIRECTO"};
        lista.add(fila1);
        lista.add(fila2);
        lista2.add(fila5);
        lista2.add(fila6);
      tab_adquisiones.getColumna("existe_adcomp").setRadio(lista, "1");
      tab_adquisiones.getColumna("tipo_compra_adcomp").setCombo(lista2); 
      tab_adquisiones.getColumna("IDE_ADAPRO").setCombo(ser_adquisiciones.getAprobado());
      tab_adquisiones.agregarRelacion(tab_certificacion);
 tab_adquisiones.agregarRelacion(tab_compra_bienes);      
      tab_adquisiones.setTipoFormulario(true);
      tab_adquisiones.getGrid().setColumns(6);
      tab_adquisiones.dibujar();
      
      PanelTabla pat_adquisiciones = new PanelTabla();
      pat_adquisiciones.setId("pat_adquisiciones");
      pat_adquisiciones.setPanelTabla(tab_adquisiones);
      
      tab_certificacion.setId("tab_certificacion");
      tab_certificacion.setTabla("ADQ_CERTIFICACION", "IDE_ADCERT", 1);
      List lista3 = new ArrayList();
      Object fila3[] = {"1", "CERTIFICACION"};
      Object fila4[] = {"2","COMPROMISO"};
      lista3.add(fila3);
      lista3.add(fila4);
      tab_certificacion.getColumna("tipo_documento_adcert").setCombo(lista3);
      tab_certificacion.dibujar();
      PanelTabla pat_panel_certificacion = new PanelTabla();
      pat_panel_certificacion.setId("pat_panel_certificacion");
      pat_panel_certificacion.setPanelTabla(tab_certificacion);
      
      tab_compra_bienes.setId("tab_compra_bienes");
      tab_compra_bienes.setTabla("ADQ_COMPRA_BIENES", "IDE_ADCOBI", 1);
      tab_compra_bienes.getColumna("IDE_ADPAMA").setCombo(ser_adquisiciones.getPartidaMaterial());
      tab_compra_bienes.dibujar();
      PanelTabla pat_panel_compra_bienes = new PanelTabla();
      pat_panel_compra_bienes.setId("pat_panel_compra_bienes");
      pat_panel_compra_bienes.setPanelTabla(tab_compra_bienes);
      
      tab_tabulador.agregarTab("CERTIFICACION", pat_panel_certificacion);
      tab_tabulador.agregarTab("COMPRA BIENES", pat_panel_compra_bienes);
      
      Division div_adquisiciones = new Division();
      div_adquisiciones.setId("div_adquisiciones");
      div_adquisiciones.dividir2(pat_adquisiciones, tab_tabulador, "70%", "H");
      agregarComponente(div_adquisiciones); 
      
      
    }
    @Override
    public void insertar() {
        if(tab_adquisiones.isFocus()){
                  tab_adquisiones.insertar();
        }
        else if (tab_certificacion.isFocus()){
            tab_certificacion.insertar();
        }
        else if(tab_compra_bienes.isFocus()){
            tab_compra_bienes.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_adquisiones.isFocus()){
                  tab_adquisiones.guardar();
        }
        else if (tab_certificacion.isFocus()){
            tab_certificacion.guardar();
        }
        else if(tab_compra_bienes.isFocus()){
            tab_compra_bienes.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       if(tab_adquisiones.isFocus()){
                  tab_adquisiones.eliminar();
        }
        else if (tab_certificacion.isFocus()){
            tab_certificacion.eliminar();
        }
        else if(tab_compra_bienes.isFocus()){
            tab_compra_bienes.eliminar();
        }
    }    

    public Tabla getTab_adquisiones() {
        return tab_adquisiones;
    }

    public void setTab_adquisiones(Tabla tab_adquisiones) {
        this.tab_adquisiones = tab_adquisiones;
    }

    public Tabla getTab_certificacion() {
        return tab_certificacion;
    }

    public void setTab_certificacion(Tabla tab_certificacion) {
        this.tab_certificacion = tab_certificacion;
    }

    public Tabla getTab_compra_bienes() {
        return tab_compra_bienes;
    }

    public void setTab_compra_bienes(Tabla tab_compra_bienes) {
        this.tab_compra_bienes = tab_compra_bienes;
    }
    
    

}
