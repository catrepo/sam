
package paq_adquisicion;

/**
 *
 * @author Andres
 */
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import sistema.aplicacion.Pantalla;

public class Empleado extends Pantalla{
    
    private Tabla tab_empleado = new Tabla();
    
    public Empleado (){
       tab_empleado.setId("tab_empleado");   //identificador
       tab_empleado.setTabla("adq_empleado", "ide_ademple", 1); 
       tab_empleado.dibujar();
       
       PanelTabla pat_empleado = new PanelTabla();
      pat_empleado.setId("pat_empleado");
      pat_empleado.setPanelTabla(tab_empleado);
      Division div_empleado = new Division();
      div_empleado.setId("div_cargo");
      div_empleado.dividir1(pat_empleado);
      agregarComponente(div_empleado);
    }
    @Override
    public void insertar() {
        tab_empleado.insertar();
    }

    @Override
    public void guardar() {
        tab_empleado.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_empleado.eliminar();
    }

    public Tabla getTab_empleado() {
        return tab_empleado;
    }

    public void setTab_empleado(Tabla tab_empleado) {
        this.tab_empleado = tab_empleado;
    }
    
}
