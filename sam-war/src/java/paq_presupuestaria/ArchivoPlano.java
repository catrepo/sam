/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ArchivoPlano extends Pantalla {
    /*
     * 
     */

    private Tabla tabTabla = new Tabla();
    private Combo cmbTipo = new Combo();
    private Combo cmbMes = new Combo();
    private Panel panOpcion = new Panel();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public ArchivoPlano() {

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("ARCHIVOS PLANOS");

        Grid griReporte = new Grid();
        griReporte.setColumns(2);
        griReporte.getChildren().add(new Etiqueta("TIPO : "));

        cmbTipo.setId("cmbTipo");
        List lista = new ArrayList();
        Object filat[] = {
            "1", "Cedula Inicial"
        };
        Object filau[] = {
            "2", "Cedula Presupuestaria"
        };
        lista.add(filat);;
        lista.add(filau);;
        cmbTipo.setCombo(lista);
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
        panOpcion.setHeader("SELECCIONE PARAMETROS DE REPORTE");
        panOpcion.getChildren().add(griReporte);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Imprimir");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("carpeta");
        bar_botones.agregarBoton(botBuscar);

        tabp2.getChildren().add(panOpcion);
        tabp2.getChildren().add(botBuscar);

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("presu_clasificador", "id_codigo", 1);
        tabTabla.getColumna("id_codigo").setVisible(false);
        tabTabla.setRows(15);
        tabTabla.dibujar();

        PanelTabla tpt = new PanelTabla();
        tpt.setPanelTabla(tabTabla);

        Division div = new Division();
        div.dividir2(tabp2, tpt, "50%", "h");
        agregarComponente(div);
    }

    public void carpeta() {
        String carp = "C:\\Archivo_Plano";
        File carpeta;//para manipular el archivo
        carpeta = new File(carp);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
            archivo(carp);
        } else {
            archivo(carp);
        }
    }

    public void archivo(String carpeta) {

        File archivo;//para manipular el archivo
        FileWriter escribir;//para escribir en el archivo
        PrintWriter linea;//para información del archivo
        if (cmbTipo.getValue()
                != null && !cmbTipo.getValue().toString().isEmpty()) {
            String cadena = null, valor = null, reforma = null, codificado = null, compromiso = null, devengado = null, ejecutado = null, saldoco = null, saldodev = null;
            if (cmbTipo.getValue().equals("1")) {
                cadena = ("CedInicial" + utilitario.getFechaActual() + ".txt");
            } else if (cmbTipo.getValue().equals("2")) {
                cadena = ("CedPresupestaria" + utilitario.getFechaActual() + ".txt");
            }
            archivo = new File(carpeta + "\\" + cadena);
            if (!archivo.exists()) {
                try {
                    archivo.createNewFile();
                    escribir = new FileWriter(archivo, false);
                    linea = new PrintWriter(escribir);

                    if (cmbTipo.getValue().equals("1")) {
                        TablaGenerica tabIngreso = programas.getIngresos(cmbMes.getValue() + "");
                        if (!tabIngreso.isEmpty()) {
                            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                                //escribo en el archivo
                                if (tabIngreso.getValor(i, "inicial").equals(".00")) {
                                    valor = "0";
                                } else {
                                    valor = tabIngreso.getValor(i, "inicial");
                                }
                                linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabIngreso.getValor(i, "tipo") + "|"
                                        + tabIngreso.getValor(i, "gr") + "|" + tabIngreso.getValor(i, "sub") + "|" + tabIngreso.getValor(i, "item") + "|" + valor + "");
                            }
                        }
                        TablaGenerica tabGasto = programas.getGatos(cmbMes.getValue() + "");
                        if (!tabGasto.isEmpty()) {
                            for (int i = 0; i < tabGasto.getTotalFilas(); i++) {
                                if (tabGasto.getValor(i, "inicial").equals(".00")) {
                                    valor = "0";
                                } else {
                                    valor = tabGasto.getValor(i, "inicial");
                                }
                                TablaGenerica tabDato = programas.getVerificacion(tabGasto.getValor(i, "CUENMC"), tabGasto.getValor(i, "codigo"));
                                if (!tabDato.isEmpty()) {
                                    //escribo en el archivo
                                    linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo") + "|"
                                            + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|"
                                            + tabDato.getValor("funcion") + "|" + tabDato.getValor("orientacion") + "|" + valor + "");
                                } else {
                                    linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo") + "|"
                                            + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|"
                                            + "000" + "|" + "00000000" + "|" + valor + "");
                                }
                            }
                        }
                        utilitario.agregarMensaje("Archivo Generado", null);
                    } else if (cmbTipo.getValue().equals("2")) {
                        TablaGenerica tabIngreso = programas.getIngresos(cmbMes.getValue() + "");
                        if (!tabIngreso.isEmpty()) {
                            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                                //escribo en el archivo
                                if (tabIngreso.getValor(i, "inicial").equals(".00")) {
                                    valor = "0";
                                } else {
                                    valor = tabIngreso.getValor(i, "inicial");
                                }
                                if (tabIngreso.getValor(i, "codificado").equals(".00")) {
                                    codificado = "0";
                                } else {
                                    codificado = tabIngreso.getValor(i, "codificado");
                                }
                                if (tabIngreso.getValor(i, "devengado").equals(".00")) {
                                    devengado = "0";
                                } else {
                                    devengado = tabIngreso.getValor(i, "devengado");
                                }
                                if (tabIngreso.getValor(i, "ejecutado").equals(".00")) {
                                    ejecutado = "0";
                                } else {
                                    ejecutado = tabIngreso.getValor(i, "ejecutado");
                                }
                                if (tabIngreso.getValor(i, "saldo").equals(".00")) {
                                    saldoco = "0";
                                } else {
                                    saldoco = tabIngreso.getValor(i, "saldo");
                                }
                                linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabIngreso.getValor(i, "tipo") + "|"
                                        + tabIngreso.getValor(i, "gr") + "|" + tabIngreso.getValor(i, "sub") + "|" + tabIngreso.getValor(i, "item") + "|" + valor
                                        + "|" + tabIngreso.getValor(i, "reforma") + "|" + codificado + "|" + devengado
                                        + "|" + ejecutado + "|" + saldoco + "");
                            }
                        }
                        TablaGenerica tabGasto = programas.getGatos(cmbMes.getValue() + "");
                        if (!tabGasto.isEmpty()) {
                            for (int i = 0; i < tabGasto.getTotalFilas(); i++) {
                                if (tabGasto.getValor(i, "inicial").equals(".00")) {
                                    valor = "0";
                                } else {
                                    valor = tabGasto.getValor(i, "inicial");
                                }
                                if (tabGasto.getValor(i, "codificado").equals(".00")) {
                                    codificado = "0";
                                } else {
                                    codificado = tabGasto.getValor(i, "codificado");
                                }
                                if (tabGasto.getValor(i, "compromiso").equals(".00")) {
                                    compromiso = "0";
                                } else {
                                    compromiso = tabGasto.getValor(i, "compromiso");
                                }
                                if (tabGasto.getValor(i, "devengado").equals(".00")) {
                                    devengado = "0";
                                } else {
                                    devengado = tabGasto.getValor(i, "devengado");
                                }
                                if (tabGasto.getValor(i, "ejecutado").equals(".00")) {
                                    ejecutado = "0";
                                } else {
                                    ejecutado = tabGasto.getValor(i, "ejecutado");
                                }
                                if (tabGasto.getValor(i, "saldoco").equals(".00")) {
                                    saldoco = "0";
                                } else {
                                    saldoco = tabGasto.getValor(i, "saldoco");
                                }
                                if (tabGasto.getValor(i, "saldoDE").equals(".00")) {
                                    saldodev = "0";
                                } else {
                                    saldodev = tabGasto.getValor(i, "saldoDE");
                                }

                                if (tabGasto.getValor(i, "reforma").equals(".00")) {
                                    reforma = "0";
                                } else {
                                    reforma = tabGasto.getValor(i, "reforma");
                                }

                                TablaGenerica tabDato = programas.getVerificacion(tabGasto.getValor(i, "CUENMC"), tabGasto.getValor(i, "codigo"));
                                if (!tabDato.isEmpty()) {
                                    //escribo en el archivo
                                    linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo") + "|"
                                            + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|"
                                            + tabDato.getValor("funcion") + "|" + tabDato.getValor("orientacion") + "|" + valor
                                            + "|" + reforma + "|" + codificado + "|" + compromiso
                                            + "|" + devengado + "|" + ejecutado + "|" + saldoco + "|" + saldodev + "");
                                } else {
                                    linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo") + "|"
                                            + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|"
                                            + "000" + "|" + "00000000" + "|" + valor
                                            + "|" + reforma + "|" + codificado + "|" + compromiso
                                            + "|" + devengado + "|" + ejecutado + "|" + saldoco + "|" + saldodev + "");
                                }
                            }
                        }
                        utilitario.agregarMensaje("Archivo Generado", null);
                    }
                    linea.close();
                    escribir.close();
                } catch (Exception e) {
                    utilitario.agregarMensajeError(null, "ha sucedio un error" + e);
                }
            } else {
                utilitario.agregarMensajeInfo("Archivo ya existe", null);
            }
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar tipo de archivo", null);
        }
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
