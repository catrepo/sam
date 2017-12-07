
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

public class EmpleadoDepartamentoAprueba extends Pantalla{
    
    private Tabla tab_emp_depar_aprue = new Tabla();
    
    public EmpleadoDepartamentoAprueba (){
        tab_emp_depar_aprue.setId("tab_emp_depar_aprue");   //identificador
       tab_emp_depar_aprue.setTabla("adq_empleado_departamento", "ide_ademde", 1); 
       tab_emp_depar_aprue.dibujar();
       
       PanelTabla pat_emp_depar_aprue = new PanelTabla();
      pat_emp_depar_aprue.setId("pat_emp_depar_aprue");
      pat_emp_depar_aprue.setPanelTabla(tab_emp_depar_aprue);
      Division div_emp_depar_aprue = new Division();
      div_emp_depar_aprue.setId("div_emp_depar_aprue");
      div_emp_depar_aprue.dividir1(pat_emp_depar_aprue);
      agregarComponente(div_emp_depar_aprue);
    }
    @Override
    public void insertar() {
        tab_emp_depar_aprue.insertar();
    }

    @Override
    public void guardar() {
        tab_emp_depar_aprue.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       tab_emp_depar_aprue.eliminar();
    }

    public Tabla getTab_emp_depar_aprue() {
        return tab_emp_depar_aprue;
    }

    public void setTab_emp_depar_aprue(Tabla tab_emp_depar_aprue) {
        this.tab_emp_depar_aprue = tab_emp_depar_aprue;
    }
    
}
