
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
import paq_adquisicion.ejb.ServiciosAdquisiones;
import sistema.aplicacion.Pantalla;

public class EmpleadoDepartamentoAprueba extends Pantalla{
    
    private Tabla tab_emp_depar = new Tabla();
    private Tabla tab_emp_aprueba = new Tabla();
    
    @EJB
    private final  ServiciosAdquisiones ser_adquisiciones = (ServiciosAdquisiones) utilitario.instanciarEJB(ServiciosAdquisiones.class);
    
    public EmpleadoDepartamentoAprueba (){
       tab_emp_depar.setId("tab_emp_depar");   //identificador
       tab_emp_depar.setTabla("adq_empleado_departamento", "ide_ademde", 1);
       tab_emp_depar.getColumna("ide_adarad").setCombo(ser_adquisiciones.getAreaAdministrativa());
       tab_emp_depar.getColumna("ide_ademple").setCombo(ser_adquisiciones.getEmpleado());
       tab_emp_depar.getColumna("ide_adcarg").setCombo(ser_adquisiciones.getCargo());
       tab_emp_depar.getColumna("ide_adtiap").setCombo(ser_adquisiciones.getTipoAprobador());
       tab_emp_depar.agregarRelacion(tab_emp_aprueba);
       tab_emp_depar.dibujar();
       
      PanelTabla pat_emp_depar_aprue = new PanelTabla();
      pat_emp_depar_aprue.setId("pat_emp_depar_aprue");
      pat_emp_depar_aprue.setPanelTabla(tab_emp_depar);
      
      tab_emp_aprueba.setId("tab_emp_aprueba");   //identificador
       tab_emp_aprueba.setTabla("adq_empleado_aprueba", "ide_ademap", 1); 
       tab_emp_aprueba.getColumna("ide_ademple").setCombo(ser_adquisiciones.getEmpleado());
       List lista = new ArrayList();
       Object fila1[] = {"1", "APROBADO OFICIAL"};
       Object fila2[] = {"2","APROBADO ENCARGADO"};
       lista.add(fila1);
       lista.add(fila2);
       tab_emp_aprueba.getColumna("categoria_ademap").setCombo(lista); 
       tab_emp_aprueba.dibujar();
       
      PanelTabla pat_emp_aprueba = new PanelTabla();
      pat_emp_aprueba.setId("pat_emp_aprueba");
      pat_emp_aprueba.setPanelTabla(tab_emp_aprueba);
      
      Division div_emp_aprueba = new Division();
      div_emp_aprueba.setId("div_emp_aprueba");
      div_emp_aprueba.dividir2(pat_emp_depar_aprue, pat_emp_aprueba, "50%","H");
      agregarComponente(div_emp_aprueba);
    }
    @Override
    public void insertar() {
        if(tab_emp_depar.isFocus()){
                  tab_emp_depar.insertar();
        }
        else if (tab_emp_aprueba.isFocus()){
            tab_emp_aprueba.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_emp_depar.isFocus()){
                  tab_emp_depar.guardar();
        }
        else if (tab_emp_aprueba.isFocus()){
            tab_emp_aprueba.guardar();
        }
        guardarPantalla();
    }

    @Override
    public void eliminar() {
       if(tab_emp_depar.isFocus()){
                  tab_emp_depar.eliminar();
        }
        else if (tab_emp_aprueba.isFocus()){
            tab_emp_aprueba.eliminar();
        }
    }

    public Tabla getTab_emp_depar() {
        return tab_emp_depar;
    }

    public void setTab_emp_depar(Tabla tab_emp_depar) {
        this.tab_emp_depar = tab_emp_depar;
    }

    public Tabla getTab_emp_aprueba() {
        return tab_emp_aprueba;
    }

    public void setTab_emp_aprueba(Tabla tab_emp_aprueba) {
        this.tab_emp_aprueba = tab_emp_aprueba;
    }

    
}
