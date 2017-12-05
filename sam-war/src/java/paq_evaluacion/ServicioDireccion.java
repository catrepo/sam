/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_evaluacion;

import framework.aplicacion.TablaGenerica;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.BeanEncuestaSatisfaccion;

/**
 *
 * @author p-sistemas
 */
public class ServicioDireccion extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private List listaDir = new ArrayList();
    @EJB
    private BeanEncuestaSatisfaccion encuesta = (BeanEncuestaSatisfaccion) utilitario.instanciarEJB(BeanEncuestaSatisfaccion.class);

    public ServicioDireccion() {

        TablaGenerica tabDatos = encuesta.getDirecciones();
        for (int i = 0; i < tabDatos.getTotalFilas(); i++) {
            Object[] obj = new Object[2];
            obj[0] = tabDatos.getValor(i, "cod_direccion");
            obj[1] = tabDatos.getValor(i, "nombre_dir");
            listaDir.add(obj);
        }

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("esf_servicios", "id_servicio", 0);
        tabTabla.getColumna("id_direccion").setCombo(listaDir);
        List lista = new ArrayList();
        Object fila1[] = {
            "A", "Activo"
        };
        Object fila2[] = {
            "D", "Baja"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabTabla.getColumna("codigo").setRadio(lista, "A");
        tabTabla.getColumna("seleccion").setVisible(false);
        tabTabla.setRows(10);
        tabTabla.dibujar();

        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabTabla);
        agregarComponente(pnt);

    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}
