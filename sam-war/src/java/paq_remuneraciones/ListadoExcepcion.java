/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;

/**
 *
 * @author p-chumana
 */
public class ListadoExcepcion extends Pantalla {

    private Tabla tabSolicitud = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private AutoCompletar autBusca = new AutoCompletar();
//Contiene todos los elementos de la plantilla
    private Panel panOpcion = new Panel();
    @EJB
    private BeanRemuneracion adminRemuneracion = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);
    private ClaseGenerica clase = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);

    public ListadoExcepcion() {
        //Para capturar el usuario que se encuntra utilizando la opción
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SOLICITUD DE ANTICIPOS DE SUELDOS");
        agregarComponente(panOpcion);

        //Auto busqueda para, verificar solicitud
        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT id_autoriza,ced_empleado,nom_empleado,anio_autoriza from nom_autorizacion where estado is null");
        autBusca.setMetodoChange("filtrarSolicitud");
        autBusca.setSize(80);
        bar_botones.agregarComponente(new Etiqueta("Buscador :"));
        bar_botones.agregarComponente(autBusca);

        dibujarPantalla();

    }

    public void dibujarPantalla() {
        limpiarPanel();
        tabSolicitud.setId("tabSolicitud");
        tabSolicitud.setTabla("nom_autorizacion", "id_autoriza", 1);
        if (autBusca.getValue() == null) {
            tabSolicitud.setCondicion("id_autoriza=-1");
        } else {
            tabSolicitud.setCondicion("id_autoriza=" + autBusca.getValor());
        }
        tabSolicitud.getColumna("ced_empleado").setMetodoChange("datosSolicitud");

        tabSolicitud.getColumna("ide_autoriza").setValorDefecto(utilitario.getVariable("NICK"));
        tabSolicitud.getColumna("ip_autoriza").setValorDefecto(utilitario.getIp());
        tabSolicitud.getColumna("fecha_autoriza").setValorDefecto(utilitario.getFechaActual());
        tabSolicitud.getColumna("anio_autoriza").setValorDefecto(String.valueOf(utilitario.getAnio(utilitario.getFechaActual())));

        List lista = new ArrayList();
        Object fila1[] = {"NL", "EMPLEADO"};
        Object fila2[] = {"CT", "TRABAJADOR"};
        lista.add(fila1);
        lista.add(fila2);
        tabSolicitud.getColumna("id_distributivo").setCombo(lista);

        List list2 = new ArrayList();
        Object fil2[] = {"NO", "NO"};
        list2.add(fil2);
        tabSolicitud.getColumna("porcentaje").setCombo(list2);

        List list3 = new ArrayList();
        Object fil3[] = {"SI", "SI"};
        list3.add(fil3);
        tabSolicitud.getColumna("autoriza_anticipo").setCombo(list3);

        List list4 = new ArrayList();
        Object fil4[] = {"SI", "SI"};
        list4.add(fil4);
        tabSolicitud.getColumna("pasar_rmu").setCombo(list4);

        List list5 = new ArrayList();
        Object fil5[] = {"SI", "SI"};
        list5.add(fil5);
        tabSolicitud.getColumna("acepta_fecha").setCombo(list5);
        
        List list6 = new ArrayList();
        Object fil6[] = {"SI", "SI"};
        list6.add(fil6);
        tabSolicitud.getColumna("pasar_garante").setCombo(list6);

        tabSolicitud.getColumna("ide_autoriza").setVisible(false);
        tabSolicitud.getColumna("ip_autoriza").setVisible(false);
//        tabSolicitud.getColumna("fecha_autoriza").setVisible(false);
        tabSolicitud.getColumna("anio_autoriza").setVisible(false);
        tabSolicitud.getColumna("dir_empleado").setVisible(false);
        tabSolicitud.getColumna("estado").setVisible(false);


        tabSolicitud.getColumna("id_distributivo").setLectura(true);
        tabSolicitud.getColumna("ide_empleado").setLectura(true);
        tabSolicitud.getColumna("suel_empleado").setLectura(true);
        tabSolicitud.getColumna("rmu_empleado").setLectura(true);
        tabSolicitud.getColumna("nom_empleado").setLectura(true);
        tabSolicitud.getColumna("contr_empleado").setLectura(true);
        tabSolicitud.getColumna("cargo_empleado").setLectura(true);
        tabSolicitud.getColumna("fecha_recomendada").setLectura(true);

        tabSolicitud.setTipoFormulario(true);
        tabSolicitud.getGrid().setColumns(2);
        tabSolicitud.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setMensajeWarn("AUTORIZACIONES PARA ANTICIPOS");
        tpa.setPanelTabla(tabSolicitud);

        Division divFormulario = new Division();
        divFormulario.setId("divFormulario");
        divFormulario.dividir1(tpa);
        agregarComponente(divFormulario);

        Grupo gru = new Grupo();
        gru.getChildren().add(divFormulario);
        panOpcion.getChildren().add(gru);
    }

    /*
     * Llenar datos de empleado
     */
    public void datosSolicitud() {
        TablaGenerica tabActual = adminRemuneracion.getVerificaDatos(tabSolicitud.getValor("ced_empleado"));
        if (!tabActual.isEmpty()) {
            tabSolicitud.setValor("ide_empleado", tabActual.getValor("CODTRA"));
            tabSolicitud.setValor("nom_empleado", tabActual.getValor("nomtra"));
            tabSolicitud.setValor("rmu_empleado", tabActual.getValor("SUEBAS"));
            tabSolicitud.setValor("id_distributivo", tabActual.getValor("tipctt"));
            tabSolicitud.setValor("cargo_empleado", tabActual.getValor("cargo"));
            tabSolicitud.setValor("area_empleado", tabActual.getValor("area"));
            tabSolicitud.setValor("dir_empleado", tabActual.getValor("coddep"));
            tabSolicitud.setValor("contr_empleado", tabActual.getValor("tipctt"));
            TablaGenerica tabFecha = adminRemuneracion.getVerificaFecha();
            if (!tabFecha.isEmpty()) {
                tabSolicitud.setValor("fecha_recomendada", tabFecha.getValor("fecha_ant"));
            }
            utilitario.addUpdate("tabSolicitud");
            liquidoAnterior();
        } else {
            utilitario.agregarMensaje("No se encontraron datos", null);
        }
    }

    /*
     * Saco remuneracion liquida anterior
     */
    public void liquidoAnterior() {
        TablaGenerica tabActual = adminRemuneracion.getMesActual(tabSolicitud.getValor("ide_empleado"), utilitario.getAnio(utilitario.getFechaActual()), clase.meses(utilitario.getMes(utilitario.getFechaActual()) - 1));
        if (!tabActual.isEmpty()) {
            tabSolicitud.setValor("suel_empleado", tabActual.getValor("liquido"));
            utilitario.addUpdate("tabSolicitud");
        } else {
            String an, mes;
            if (utilitario.getMes(utilitario.getFechaActual()) != 1) {
                an = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()));
                mes = clase.meses(utilitario.getMes(utilitario.getFechaActual()) - 1);
            } else {
                an = String.valueOf(utilitario.getAnio(utilitario.getFechaActual()) - 1);
                mes = clase.meses(12);
            }
            TablaGenerica tabAnAnterior = adminRemuneracion.getMesActual(tabSolicitud.getValor("ide_empleado"), Integer.parseInt(an), mes);
            if (!tabAnAnterior.isEmpty()) {
                tabSolicitud.setValor("suel_empleado", tabAnAnterior.getValor("liquido"));
                utilitario.addUpdate("tabSolicitud");
            } else {
                TablaGenerica tabAnterior1 = adminRemuneracion.getMesAnterior1(tabSolicitud.getValor("ide_empleado"), Integer.parseInt(an), mes);
                if (!tabAnterior1.isEmpty()) {
                    tabSolicitud.setValor("suel_empleado", tabAnterior1.getValor("liquido"));
                    utilitario.addUpdate("tabSolicitud");
                } else {
                    utilitario.agregarMensaje("No puede mostra sueldo anterior", null);
                    tabSolicitud.setValor("nom_empleado", null);
                    tabSolicitud.setValor("rmu_empleado", null);
                    tabSolicitud.setValor("id_distributivo", null);
                    tabSolicitud.setValor("cargo_empleado", null);
                    tabSolicitud.setValor("tipcont_empleado", null);
                    tabSolicitud.setValor("area_empleado", null);
                    tabSolicitud.setValor("dir_empleado", null);
                    tabSolicitud.setValor("contr_empleado", null);
                    tabSolicitud.setValor("tip_contrato", null);
                    tabSolicitud.setValor("fecha_ingreso", null);
                    tabSolicitud.setValor("fecha_salida", null);
                    tabSolicitud.setValor("autoriza_anticipo", null);
                    tabSolicitud.setValor("pasar_rmu", null);
                    tabSolicitud.setValor("porcentaje", null);
                    tabSolicitud.setValor("acepta_fecha", null);
                    utilitario.addUpdate("tabSolicitud");
                }
            }
        }
    }

    /*
     * Permite limpiar el formulario
     */
    //borra el contenido de la división central central
    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

    //Filtra el cliente seleccionado en el autocompletar
    public void filtrarSolicitud(SelectEvent evt) {
        limpiar();
        autBusca.onSelect(evt);
        dibujarPantalla();
    }

    @Override
    public void insertar() {
        if (tabSolicitud.isFocus()) {
            autBusca.limpiar();
            utilitario.addUpdate("autBusca");
            tabSolicitud.limpiar();
            tabSolicitud.insertar();
        }
    }

    @Override
    public void guardar() {
        if (tabSolicitud.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        if (tabSolicitud.isFocus()) {
            tabSolicitud.eliminar();
        }
    }

    public Tabla getTabSolicitud() {
        return tabSolicitud;
    }

    public void setTabSolicitud(Tabla tabSolicitud) {
        this.tabSolicitud = tabSolicitud;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}
