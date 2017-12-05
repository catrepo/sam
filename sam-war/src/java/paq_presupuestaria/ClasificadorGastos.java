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
public class ClasificadorGastos extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Combo cmbTipo = new Combo();
    private Combo cmbMes = new Combo();
    private Panel panOpcion = new Panel();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public ClasificadorGastos() {

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("CLASIFICADOR DE GASTOS");

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

        Boton botCargar = new Boton();
        botCargar.setValue("Cuentas");
        botCargar.setExcluirLectura(true);
        botCargar.setIcon("ui-icon-document-b");
        botCargar.setMetodo("cuentas");
        bar_botones.agregarBoton(botCargar);

        panOpcion.setId("panOpcion");
        panOpcion.setStyle("font-size:12px;color:black;text-align:left;");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SELECCIONE PARAMETROS DE REPORTE");
        panOpcion.getChildren().add(griReporte);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Imprimir");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("datosGasto");

        tabp2.getChildren().add(panOpcion);
        tabp2.getChildren().add(botBuscar);

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("clas_orien_gasto_proyecto", "id_codigo", 1);
        tabTabla.getColumna("id_codigo").setVisible(false);
        tabTabla.getColumna("num_proyecto").setFiltro(true);
        tabTabla.getColumna("programa").setFiltro(true);
        tabTabla.getColumna("tipo").setFiltro(true);
        tabTabla.setRows(15);
        tabTabla.dibujar();

        PanelTabla tpt = new PanelTabla();
        tpt.setPanelTabla(tabTabla);

        Division div = new Division();
        div.dividir2(tpt, null, "95%", "h");
        agregarComponente(div);
    }

    public void cuentas() {
        Integer maximo = Integer.parseInt(programas.listaMax1());
        TablaGenerica tabIngreso = programas.getGastosProyectoClasificador();
        if (!tabIngreso.isEmpty()) {
            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                TablaGenerica tabDato = programas.getVerificaParametro(tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "taad02"),
                        tabIngreso.getValor(i, "auad02"), tabIngreso.getValor(i, "auad01"), tabIngreso.getValor(i, "postm"));
                if (!tabDato.isEmpty()) {
                } else {
                    programas.setDatosParametrosGastos((maximo+i),tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "taad02"), tabIngreso.getValor(i, "auad02"), tabIngreso.getValor(i, "auad01"),
                            tabIngreso.getValor(i, "proyecto"), tabIngreso.getValor(i, "dep"), tabIngreso.getValor(i, "postm"));
                }
            }
        }
        utilitario.addUpdate("tabTabla");
    }

    public void datosGasto() {
        programas.setGastoClasificacion();
        TablaGenerica tabIngreso = programas.getGastosProyecto(cmbMes.getValue() + "");
        if (!tabIngreso.isEmpty()) {
            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                programas.setDatosCedulaGastos(Integer.parseInt(tabIngreso.getValor(i, "numero")), tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "taad02"), tabIngreso.getValor(i, "auad02"), tabIngreso.getValor(i, "auad01"),
                        tabIngreso.getValor(i, "postm"),
                        Double.parseDouble(tabIngreso.getValor(i, "inicial")), Double.parseDouble(tabIngreso.getValor(i, "reforma")), Double.parseDouble(tabIngreso.getValor(i, "codificado")),
                        Double.parseDouble(tabIngreso.getValor(i, "compromiso")), Double.parseDouble(tabIngreso.getValor(i, "devengado")), Double.parseDouble(tabIngreso.getValor(i, "ejecutado")));
            }
        }
        carpeta();
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
        String valor = null, reforma = null, codificado = null, compromiso = null, devengado = null, ejecutado = null, saldoco = null, saldodev = null, cadena = null;
        File archivo;//para manipular el archivo
        FileWriter escribir;//para escribir en el archivo
        PrintWriter linea;//para información del archivo
        if (cmbTipo.getValue()
                != null && !cmbTipo.getValue().toString().isEmpty()) {
            if (cmbTipo.getValue().equals("1")) {
                cadena = ("CedInicialClasif_" + cmbMes.getValue() + "_" + utilitario.getFechaActual() + ".txt");
            } else if (cmbTipo.getValue().equals("2")) {
                cadena = ("CedPresupestariaClasif_" + cmbMes.getValue() + "_" + utilitario.getFechaActual() + ".txt");
            }

        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar tipo de archivo", null);
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
                    TablaGenerica tabGasto = programas.getGastoClasificacion();
                    if (!tabGasto.isEmpty()) {
                        for (int i = 0; i < tabGasto.getTotalFilas(); i++) {
                            if (tabGasto.getValor(i, "inicial").equals(".00")) {
                                valor = "0";
                            } else {
                                valor = tabGasto.getValor(i, "inicial");
                            }
                            linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo") + "|"
                                    + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|" + tabGasto.getValor(i, "funcion") + "|"
                                    + tabGasto.getValor(i, "orientacion") + "|" + valor + "");
                        }
                    }
                    utilitario.agregarMensaje("Archivo Generado", null);
                }
                if (cmbTipo.getValue().equals("2")) {
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
                    TablaGenerica tabGasto = programas.getGastoClasificacion();
                    if (!tabGasto.isEmpty()) {
                        for (int i = 0; i < tabGasto.getTotalFilas(); i++) {
                            //escribo en el archivo
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
                            if (tabGasto.getValor(i, "saldoCo").equals(".00")) {
                                saldoco = "0";
                            } else {
                                saldoco = tabGasto.getValor(i, "saldoCo");
                            }
                            if (tabGasto.getValor(i, "saldoDe").equals(".00")) {
                                saldodev = "0";
                            } else {
                                saldodev = tabGasto.getValor(i, "saldoDe");
                            }

                            if (tabGasto.getValor(i, "reforma").equals(".00")) {
                                reforma = "0";
                            } else {
                                reforma = tabGasto.getValor(i, "reforma");
                            }

                            linea.println(String.valueOf(Integer.parseInt(cmbMes.getValue().toString())) + "|" + tabGasto.getValor(i, "tipo") + "|"
                                    + tabGasto.getValor(i, "gr") + "|" + tabGasto.getValor(i, "sub") + "|" + tabGasto.getValor(i, "item") + "|" + tabGasto.getValor(i, "funcion") + "|"
                                    + tabGasto.getValor(i, "orientacion") + "|" + valor
                                    + "|" + reforma + "|" + codificado + "|" + compromiso
                                    + "|" + devengado + "|" + ejecutado + "|" + saldoco + "|" + saldodev + "");
                        }
                    }
                    utilitario.agregarMensaje("Archivo Generado", null);
                }
                linea.close();
                escribir.close();
            } catch (Exception e) {
                utilitario.agregarMensajeError(null, "ha sucedio un error" + e);
            }
        }
    }

    @Override
    public void insertar() {
        cuentas();
//        tabTabla.insertar();
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            if (tabTabla.getValor(i, "funcion") != null || tabTabla.getValor(i, "orientacion") != null) {
                programas.setClasificadoGastos(Integer.parseInt(tabTabla.getValor(i, "id_codigo")), tabTabla.getValor(i, "orientacion"), tabTabla.getValor(i, "funcion"));
            }
        }
        utilitario.agregarMensaje("Registro Guardado", "");
    }

    @Override
    public void eliminar() {
//        tabTabla.eliminar();
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}
