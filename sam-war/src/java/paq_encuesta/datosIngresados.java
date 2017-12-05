/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.component.chart.pie.PieChart;
import org.primefaces.model.chart.PieChartModel;
import sistema.aplicacion.Pantalla;
import paq_transportes.ejb.Serviciobusqueda;
import paq_utilitario.ejb.BeanEncuestaSatisfaccion;

/**
 *
 * @author p-chumana
 */
public class datosIngresados extends Pantalla {

    private Tabla tabConteo = new Tabla();
    private Tabla tabEncuesta = new Tabla();
    private Tabla setRegistro = new Tabla();
    private AutoCompletar autCompleta = new AutoCompletar();
    private Panel panEncuesta = new Panel();
    private Panel panDatos = new Panel();
    private Dialogo diaDialogo = new Dialogo();
    private Grid gridO = new Grid();
    private Grid grid = new Grid();
    @EJB
    private Serviciobusqueda busqueda = (Serviciobusqueda) utilitario.instanciarEJB(Serviciobusqueda.class);
    private BeanEncuestaSatisfaccion datos = (BeanEncuestaSatisfaccion) utilitario.instanciarEJB(BeanEncuestaSatisfaccion.class);

    /*
     * Grafico Estadistico
     */
    private PieChartModel modeloPastel = new PieChartModel();
    private PieChart graficoPastel = new PieChart();

    public datosIngresados() {
        /*
         * Muestra del conteo de Registros al ingresar a la aplicacion
         */
        Boton botMensaje = new Boton();
        botMensaje.setValue("Conteo");
        botMensaje.setExcluirLectura(true);
        botMensaje.setIcon("ui-icon-search");
        botMensaje.setMetodo("conteo");
        bar_botones.agregarBoton(botMensaje);

        tabConteo.setId("tabConteo");
        tabConteo.setSql("select 0 as id,count(*) as ingresos from encuesta_volcan");
        tabConteo.setCampoPrimaria("id");
        tabConteo.setLectura(true);
        tabConteo.dibujar();
        utilitario.agregarNotificacion("Actualmente : " + tabConteo.getValor("ingresos"), "Registros Ingresados de Encuesta", "imagenes/volcan.png");

        autCompleta.setId("autCompleta");
        autCompleta.setAutoCompletar("SELECT id_codigo,cedula_encuestado,nombre_encuestado,tipo_vivienda,cedula_propietario,nombre_propietario FROM encuesta_volcan");
        autCompleta.setSize(70);

        diaDialogo.setId("diaDialogo");
        diaDialogo.setTitle("Lista de Propiedades"); //titulo
        diaDialogo.setWidth("46%"); //siempre en porcentajes  ancho
        diaDialogo.setHeight("40%");//siempre porcentaje   alto
        diaDialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaDialogo.getBot_aceptar().setMetodo("aceptaRegistro");
        gridO.setColumns(4);
        agregarComponente(diaDialogo);

        /*
         * Opción para mirar reporte
         */

        Division div = new Division();
        div.setId("div");
        div.dividir2(panEncuesta, panDatos, "45%", "V");
        agregarComponente(div);

        dibujaPantalla();
        dibujarGrafico();
    }
    /*
     * Encuesta de ingreso
     */

    public void dibujaPantalla() {
        tabEncuesta.setId("tabEncuesta");
        tabEncuesta.setTabla("encuesta_volcan", "id_codigo", 1);
        if (autCompleta.getValue() == null) {
            tabEncuesta.setCondicion("id_codigo=-1");
        } else {
            tabEncuesta.setCondicion("id_codigo=" + autCompleta.getValor());
        }
        tabEncuesta.getColumna("cedula_encuestado").setMetodoChange("ciudadano");
        tabEncuesta.getColumna("cedula_propietario").setMetodoChange("catastro");
        tabEncuesta.getColumna("provincia").setMetodoChange("canton");
        tabEncuesta.getColumna("canton").setMetodoChange("parroquia");
        tabEncuesta.getColumna("lugar_refugiarse").setMetodoChange("lugaRefugio");
        tabEncuesta.getColumna("tipo_refugio").setMetodoChange("tipoRefugio");
        tabEncuesta.getColumna("tipo_vivienda").setMetodoChange("tipoVivienda");
        tabEncuesta.getColumna("provincia").setCombo("SELECT ubi_codigo,ubi_descri FROM oceubica where ubi_acceso ='P'");
        tabEncuesta.getColumna("canton").setCombo("SELECT ubi_codigo,ubi_descri FROM oceubica where ubi_acceso ='C'");
        tabEncuesta.getColumna("parroquia").setCombo("SELECT ubi_codigo,ubi_descri FROM oceubica where ubi_acceso ='R'");
        tabEncuesta.getColumna("fecha_registro").setValorDefecto(utilitario.getFechaActual());
        tabEncuesta.getColumna("nombre_encuestado").setLongitud(60);
        tabEncuesta.getColumna("nombre_propietario").setLongitud(60);
        tabEncuesta.getColumna("propietario_direccion").setLongitud(90);
        tabEncuesta.getColumna("barrio").setLongitud(50);
        tabEncuesta.getColumna("numero_integrantes").setLongitud(2);
        tabEncuesta.getColumna("numero_ninos").setLongitud(2);
        tabEncuesta.getColumna("numero_adultos").setLongitud(2);
        tabEncuesta.getColumna("numero_discapacitados").setLongitud(2);
        tabEncuesta.getColumna("id_codigo").setVisible(false);

        List lista = new ArrayList();
        Object fila1[] = {
            "Propia", "Propia"
        };
        Object fila2[] = {
            "Arrendada", "Arrendada"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        tabEncuesta.getColumna("tipo_vivienda").setCombo(lista);
        List list = new ArrayList();
        Object fil1[] = {
            "1", "Si"
        };
        Object fil2[] = {
            "2", "No"
        };
        list.add(fil1);;
        list.add(fil2);;
        tabEncuesta.getColumna("lugar_refugiarse").setRadio(list, "");

        List lis = new ArrayList();
        Object fi1[] = {
            "Donde un Familiar", "Donde un Familiar"
        };
        Object fi2[] = {
            "Casa de Arriendo", "Casa de Arriendo"
        };
        Object fi3[] = {
            "Otra Casa Propia", "Otra Casa Propia"
        };
        Object fi4[] = {
            "Albergue", "Albergue"
        };
        lis.add(fi1);;
        lis.add(fi2);;
        lis.add(fi3);;
        lis.add(fi4);;
        tabEncuesta.getColumna("tipo_refugio").setCombo(lis);

        List li = new ArrayList();
        Object f1[] = {
            "Hermana/o", "Hermana/o"
        };
        Object f2[] = {
            "Tio/a", "Tio/a"
        };
        Object f3[] = {
            "Abuela/o", "Abuela/o"
        };
        Object f4[] = {
            "Primo/a", "Primo/a"
        };
        li.add(f1);;
        li.add(f2);;
        li.add(f3);;
        li.add(f4);;
        tabEncuesta.getColumna("parentesco").setCombo(li);

        tabEncuesta.setTipoFormulario(true);
        tabEncuesta.getGrid().setColumns(2);
        tabEncuesta.dibujar();
        PanelTabla tpn = new PanelTabla();
        tpn.setPanelTabla(tabEncuesta);

        Grupo gru = new Grupo();
        gru.getChildren().add(tpn);
        panEncuesta.getChildren().add(gru);
    }

    public void conteo() {
        utilitario.agregarNotificacion("Actualmente : " + tabConteo.getValor("ingresos"), "Registros Ingresados de Encuesta", "imagenes/volcan.png");
    }

    public void ciudadano() {
        if (utilitario.validarCedula(tabEncuesta.getValor("cedula_encuestado"))) {
            TablaGenerica tabDato = busqueda.getPersona(tabEncuesta.getValor("cedula_encuestado"));
            if (!tabDato.isEmpty()) {
                tabEncuesta.setValor("nombre_encuestado", tabDato.getValor("nombre"));
                utilitario.addUpdate("tabEncuesta");
            } else {
                utilitario.agregarMensajeInfo("El Número de Cédula ingresado no existe en la base de datos ciudadania del municipio", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Cedula incorrecta", null);
        }
    }

    public void tipoVivienda() {
        if (tabEncuesta.getValor("tipo_vivienda").equals("Propia")) {
            TablaGenerica tabDato = datos.busCredenciales(tabEncuesta.getValor("cedula_encuestado"), "cedula");
            if (!tabDato.isEmpty()) {
                if (tabDato.getTotalFilas() > 1) {
                    listado();
                } else {
                    tabEncuesta.setValor("cedula_propietario", tabDato.getValor("clave"));
                    tabEncuesta.setValor("nombre_propietario", tabDato.getValor("nombre"));
                    tabEncuesta.setValor("propietario_direccion", tabDato.getValor("direccion"));
                    utilitario.addUpdate("tabEncuesta");
                }
            }
        }
    }

    public void listado() {
        diaDialogo.Limpiar();
        diaDialogo.setDialogo(grid);
        gridO.getChildren().add(setRegistro);
        setRegistro.setId("setRegistro");
        setRegistro.setSql("SELECT clave,clave as catastro,direccion FROM catastrov where cedula = '" + tabEncuesta.getValor("cedula_encuestado") + "'");
        setRegistro.getColumna("direccion").setLongitud(51);
        setRegistro.setTipoSeleccion(false);
        setRegistro.setRows(10);
        setRegistro.dibujar();
        diaDialogo.setDialogo(gridO);
        diaDialogo.dibujar();
    }

    public void aceptaRegistro() {
        TablaGenerica tabDato = datos.busCredenciales(setRegistro.getValorSeleccionado(), "clave");
        if (!tabDato.isEmpty()) {
            tabEncuesta.setValor("cedula_propietario", tabDato.getValor("clave"));
            tabEncuesta.setValor("nombre_propietario", tabDato.getValor("nombre"));
            tabEncuesta.setValor("propietario_direccion", tabDato.getValor("direccion"));
            utilitario.addUpdate("tabEncuesta");
        }
        diaDialogo.cerrar();
    }

    public void catastro() {
        if (utilitario.validarCedula(tabEncuesta.getValor("cedula_propietario"))) {
            TablaGenerica tabDato = datos.busCredenciales(tabEncuesta.getValor("cedula_propietario"), "cedula");
            if (!tabDato.isEmpty()) {
                if (tabDato.getTotalFilas() > 1) {
                    listado();
                } else {
                    tabEncuesta.setValor("cedula_propietario", tabDato.getValor("clave"));
                    tabEncuesta.setValor("nombre_propietario", tabDato.getValor("nombre"));
                    tabEncuesta.setValor("propietario_direccion", tabDato.getValor("direccion"));
                    utilitario.addUpdate("tabEncuesta");
                }
            }
        } else {
            TablaGenerica tabDato = datos.busCredenciales(tabEncuesta.getValor("cedula_propietario"), "clave");
            if (!tabDato.isEmpty()) {
                tabEncuesta.setValor("nombre_propietario", tabDato.getValor("nombre"));
                tabEncuesta.setValor("propietario_direccion", tabDato.getValor("direcion"));
                utilitario.addUpdate("tabEncuesta");
            }
        }
    }

    public void lugaRefugio() {
        if (tabEncuesta.getValor("lugar_refugiarse").equals("1")) {
            tabEncuesta.getColumna("tipo_refugio").setLectura(false);
            tabEncuesta.getColumna("provincia").setLectura(false);
            utilitario.addUpdate("tabEncuesta");
        } else {
            tabEncuesta.getColumna("tipo_refugio").setLectura(true);
            tabEncuesta.getColumna("provincia").setLectura(true);
            utilitario.addUpdate("tabEncuesta");
        }
    }

    public void tipoRefugio() {
        if (tabEncuesta.getValor("tipo_refugio").equals("Donde un Familiar")) {
            tabEncuesta.getColumna("parentesco").setLectura(false);
            utilitario.addUpdate("tabEncuesta");
        } else {
            tabEncuesta.getColumna("parentesco").setLectura(true);
            utilitario.addUpdate("tabEncuesta");
        }
    }

    public void canton() {
        if (tabEncuesta.getValor("provincia").equals("19")) {
            tabEncuesta.getColumna("canton").setLectura(false);
            tabEncuesta.getColumna("parroquia").setLectura(false);
            tabEncuesta.getColumna("barrio").setLectura(false);
            utilitario.addUpdate("tabEncuesta");
            tabEncuesta.getColumna("canton").setCombo("SELECT ubi_codigo,ubi_descri FROM oceubica where oce_ubi_codigo =" + tabEncuesta.getValor("provincia"));
            utilitario.addUpdateTabla(tabEncuesta, "canton", null);
        } else {
            tabEncuesta.getColumna("canton").setLectura(true);
            tabEncuesta.getColumna("parroquia").setLectura(true);
            tabEncuesta.getColumna("barrio").setLectura(true);
            utilitario.addUpdate("tabEncuesta");
        }
    }

    public void parroquia() {
        tabEncuesta.getColumna("parroquia").setCombo("SELECT ubi_codigo,ubi_descri FROM oceubica where oce_ubi_codigo =" + tabEncuesta.getValor("canton"));
        utilitario.addUpdateTabla(tabEncuesta, "parroquia", null);
    }

    @Override
    public void insertar() {
        tabEncuesta.insertar();
    }

    @Override
    public void guardar() {
        tabEncuesta.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
    }

    /*
     * Grafico Estadistico de información ingresada
     */
    public void dibujarGrafico() {
        graficoPastel.setId("graficoPastel");
        graficoPastel.setTitle("Ingreso Por Zonas");
        graficoPastel.setLegendPosition("w");
        graficoPastel.setShowDataLabels(true);
        graficoPastel.setDiameter(245);
        graficoPastel.setSliceMargin(10);
        graficoPastel.setValue(modeloPastel);
        List lis = utilitario.getConexion().consultar("select count(*) as ingreso,a.zona from \n"
                + "(SELECT CLAVE, ZONA FROM zona_riesgos) as a\n"
                + "inner join \n"
                + "(SELECT cedula_propietario FROM encuesta_volcan) as b\n"
                + "on a.clave = b.cedula_propietario\n"
                + "group by a.zona");
        if (!lis.isEmpty()) {
            for (int i = 0; i < lis.size(); i++) {
                Object[] fila = (Object[]) lis.get(i);
                modeloPastel.set("Zona " + fila[1], Float.parseFloat(fila[0] + ""));
            }
        } else {
            utilitario.agregarNotificacionInfo("Reporte", "Error No Existe Información Ingresada");
        }
        Grupo gru = new Grupo();
        gru.getChildren().add(graficoPastel);
        panDatos.getChildren().add(gru);
    }

    public Tabla getTabEncuesta() {
        return tabEncuesta;
    }

    public void setTabEncuesta(Tabla tabEncuesta) {
        this.tabEncuesta = tabEncuesta;
    }

    public Tabla getSetRegistro() {
        return setRegistro;
    }

    public void setSetRegistro(Tabla setRegistro) {
        this.setRegistro = setRegistro;
    }

    public AutoCompletar getAutCompleta() {
        return autCompleta;
    }

    public void setAutCompleta(AutoCompletar autCompleta) {
        this.autCompleta = autCompleta;
    }

    public PieChartModel getModeloPastel() {
        return modeloPastel;
    }

    public void setModeloPastel(PieChartModel modeloPastel) {
        this.modeloPastel = modeloPastel;
    }
}
