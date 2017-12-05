/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.contabilidad;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import paq_presupuestaria.ejb.Programas;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ArchivoPlano extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Combo cmbTipo = new Combo();
    private Combo cmbMes = new Combo();
    private Panel panOpcion = new Panel();
    private Dialogo diaArchivo = new Dialogo();
    private Grid gridA = new Grid();
    private Grid grida = new Grid();
    private HtmlOutputText valor = new HtmlOutputText();
    private HtmlOutputLink botPrin = new HtmlOutputLink();
    private Imagen quinde = new Imagen();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public ArchivoPlano() {

        Boton botCuentas = new Boton();
        botCuentas.setValue("Actualizar");
        botCuentas.setExcluirLectura(true);
        botCuentas.setIcon("ui-icon-gear");
        botCuentas.setMetodo("ActualizaCuenta");
        bar_botones.agregarBoton(botCuentas);

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("ARCHIVO PLANO");

        Grid griReporte = new Grid();
        griReporte.setColumns(2);
        griReporte.getChildren().add(new Etiqueta("TIPO : "));

        cmbTipo.setId("cmbTipo");
        List lista = new ArrayList();
        Object filat[] = {
            "1", "Cedula Presupuestaria"
        };
        Object filau[] = {
            "2", "Cedula Inicial"
        };
        lista.add(filat);;
        lista.add(filau);;
        cmbTipo.setCombo(lista);
        cmbTipo.eliminarVacio();
        griReporte.getChildren().add(cmbTipo);
        griReporte.getChildren().add(new Etiqueta("MES : "));
        cmbMes.setId("cmbMes");
        List meses = new ArrayList();
        Object enero[] = {
            "01", "Enero"
        };
        Object febrero[] = {
            "02", "Febrero"
        };
        Object marzo[] = {
            "03", "Marzo"
        };
        Object abril[] = {
            "04", "Abril"
        };
        Object mayo[] = {
            "05", "Mayo"
        };
        Object junio[] = {
            "06", "Junio"
        };
        Object julio[] = {
            "07", "Julio"
        };
        Object agosto[] = {
            "08", "Agosto"
        };
        Object septiembre[] = {
            "09", "Septiembre"
        };
        Object octubre[] = {
            "10", "Octubre"
        };
        Object noviembre[] = {
            "11", "Noviembre"
        };
        Object diciembre[] = {
            "12", "Diciembre"
        };
        meses.add(enero);;
        meses.add(febrero);;
        meses.add(marzo);;
        meses.add(abril);;
        meses.add(mayo);;
        meses.add(junio);;
        meses.add(julio);;
        meses.add(agosto);;
        meses.add(septiembre);;
        meses.add(octubre);;
        meses.add(noviembre);;
        meses.add(diciembre);;
        cmbMes.setCombo(meses);
        griReporte.getChildren().add(cmbMes);

        panOpcion.setId("panOpcion");
        panOpcion.setStyle("font-size:12px;color:black;text-align:left;");
        panOpcion.setTransient(true);
        panOpcion.setHeader("PARAMETRO DE REPORTE");
        panOpcion.getChildren().add(griReporte);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Imprimir");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("datosGasto");
        bar_botones.agregarBoton(botBuscar);

        tabp2.getChildren().add(panOpcion);
        tabp2.getChildren().add(botBuscar);

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("clas_orien_gasto_proyecto", "id_codigo", 1);
        tabTabla.getColumna("cuenta").setFiltro(true);
        tabTabla.getColumna("auxiliar").setVisible(false);
        tabTabla.getColumna("id_codigo").setVisible(false);
        tabTabla.getColumna("direccion").setVisible(false);
        tabTabla.setRows(15);
        tabTabla.dibujar();

        PanelTabla tpt = new PanelTabla();
        tpt.setPanelTabla(tabTabla);

        Division div = new Division();
        div.dividir2(tabp2, tpt, "35%", "h");
        agregarComponente(div);

        diaArchivo.setId("diaArchivo");
        diaArchivo.setTitle("IMPRIMIR ARCHIVO PLANO"); //titulo
        diaArchivo.setWidth("20%"); //siempre en porcentajes  ancho
        diaArchivo.setHeight("20%");//siempre porcentaje   alto
        diaArchivo.setResizable(false); //para que no se pueda cambiar el tamaño
        diaArchivo.getBot_aceptar().setDisabled(true);
        gridA.setColumns(4);
        agregarComponente(diaArchivo);

    }

    public void ActualizaCuenta() {
        Integer maximo = Integer.parseInt(programas.listaMax1());
        TablaGenerica tabIngreso = programas.getGastosProyectoClasificador();
        if (!tabIngreso.isEmpty()) {
            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                TablaGenerica tabDato = programas.getVerificaParametro(tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "taad02"),
                        tabIngreso.getValor(i, "auad02"), tabIngreso.getValor(i, "auad01"), tabIngreso.getValor(i, "postm"));
                if (!tabDato.isEmpty()) {
                } else {
                    programas.setDatosParametrosGastos((maximo + i), tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "taad02"), tabIngreso.getValor(i, "auad02"), tabIngreso.getValor(i, "auad01"),
                            tabIngreso.getValor(i, "proyecto"), tabIngreso.getValor(i, "dep"), tabIngreso.getValor(i, "postm"));
                }
            }
        }
        utilitario.addUpdate("tabTabla");
    }

    /*
     * Creación de archivo plano
     */
    
    
    
    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            String proyecto = "";
            if (tabTabla.getValor(i, "num_proyecto") != null) {
                proyecto  = "'" + tabTabla.getValor(i, "funcion") + "'";
            } else {
                proyecto  = null;
            }
            TablaGenerica tabDato = programas.getVerificaClasificador(tabTabla.getValor(i, "cuenta"), tabTabla.getValor(i, "orientacion"),
                    tabTabla.getValor(i, "tipo"), proyecto, tabTabla.getValor(i, "programa"), tabTabla.getValor(i, "funcion"));
            if (!tabDato.isEmpty()) {
            } else {
                String funcion = "";
                String orientacion = "";
                if (tabTabla.getValor(i, "funcion") != null) {
                    funcion = "'" + tabTabla.getValor(i, "funcion") + "'";
                } else {
                    funcion = null;
                }
                if (tabTabla.getValor(i, "orientacion") != null) {
                    orientacion = "'" + tabTabla.getValor(i, "orientacion") + "'";
                } else {
                    orientacion = null;
                }
                programas.setClasificadoGastos(Integer.parseInt(tabTabla.getValor(i, "id_codigo")), orientacion, funcion);
            }
        }
        utilitario.agregarMensaje("Registro Guardado", "");
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}
