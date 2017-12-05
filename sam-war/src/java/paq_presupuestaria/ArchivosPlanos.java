/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import paq_presupuestaria.ejb.CedulaPresupuestaria;

import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ArchivosPlanos extends Pantalla {

    private Tabla tabTabla = new Tabla();
    private Combo cmbTipo = new Combo();
    private Combo cmbMes = new Combo();
    private Combo cmbAnio = new Combo();
    private Panel panOpcion = new Panel();
    private Dialogo diaArchivo = new Dialogo();
    private Grid gridA = new Grid();
    private Grid grida = new Grid();
    private HtmlOutputText valor = new HtmlOutputText();
    private HtmlOutputLink botPrin = new HtmlOutputLink();
    private Imagen quinde = new Imagen();
    @EJB
    private CedulaPresupuestaria programas = (CedulaPresupuestaria) utilitario.instanciarEJB(CedulaPresupuestaria.class);

    public ArchivosPlanos() {
        Boton botCuentas = new Boton();
        botCuentas.setValue("Actualizar");
        botCuentas.setExcluirLectura(true);
        botCuentas.setIcon("ui-icon-document-b");
        botCuentas.setMetodo("cuentas");
        bar_botones.agregarBoton(botCuentas);

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
        griReporte.getChildren().add(new Etiqueta("AÑO : "));
        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("select ano_curso as anio, ano_curso from conc_ano order by ano_curso desc ");
        griReporte.getChildren().add(cmbAnio);

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
        bar_botones.agregarBoton(botBuscar);

        tabp2.getChildren().add(panOpcion);
        tabp2.getChildren().add(botBuscar);

        tabTabla.setId("tabTabla");
        tabTabla.setTabla("presu_clasificador", "id_codigo", 1);
        tabTabla.getColumna("partida").setFiltro(true);
        tabTabla.getColumna("id_codigo").setVisible(false);
        tabTabla.setRows(15);
        tabTabla.dibujar();

        PanelTabla tpt = new PanelTabla();
        tpt.setPanelTabla(tabTabla);

        Division div = new Division();
        div.dividir2(tabp2, tpt, "50%", "h");
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

    public void cuentas() {
        Integer maximo = Integer.parseInt(programas.listaMax());
        TablaGenerica tabIngreso = programas.getFuncionGasto();
        if (!tabIngreso.isEmpty()) {
            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                TablaGenerica tabDato = programas.getVerificaParametro(tabIngreso.getValor(i, "tipo_presupuesto"), tabIngreso.getValor(i, "cuenta"), tabIngreso.getValor(i, "programa"));
                if (!tabDato.isEmpty()) {
                } else {
                    String funcion = "";
                    String orientacion = "";
                    if (tabIngreso.getValor(i, "funcion") != null) {
                        funcion = "'" + tabIngreso.getValor(i, "funcion") + "'";
                    } else {
                        funcion = null;
                    }
                    if (tabIngreso.getValor(i, "orientacion") != null) {
                        orientacion = "'" + tabIngreso.getValor(i, "orientacion") + "'";
                    } else {
                        orientacion = null;
                    }
                    programas.setDatosParametroGasto(((maximo) + i), tabIngreso.getValor(i, "tipo_presupuesto"), tabIngreso.getValor(i, "gr") + "." + tabIngreso.getValor(i, "gr1") + "." + tabIngreso.getValor(i, "sub") + "." + tabIngreso.getValor(i, "item"), tabIngreso.getValor(i, "cuenta"),
                            funcion, orientacion, tabIngreso.getValor(i, "programa"));
                }
            }
        }
        utilitario.addUpdate("tabTabla");
    }

    public void datosGasto() {
        programas.setGastoClasificacion();
//        System.err.println(Integer.parseInt("1" + String.valueOf(utilitario.getAnio(utilitario.getFechaActual())).substring(2, 4) + "" + cmbMes.getValue() + ""));
        TablaGenerica tabIngreso = programas.getCedulaPresupuesto(cmbMes.getValue() + "", Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""),cmbAnio.getValue()+"");
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
        File carpeta = new File(FacesContext.
                getCurrentInstance().
                getExternalContext().
                getRealPath("/upload/archivo/"));

        if (!carpeta.exists()) {
            convertidor();
            carpeta.mkdirs();
            archivo(carpeta.toString());
        } else {
            convertidor();
            archivo(carpeta.toString());
        }
    }

    public String convertidor() {
        String caracter = "";
        Calendar fecha = Calendar.getInstance();
        int ano = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);
        caracter = ano + "_" + "_" + mes + "_" + dia + "_" + hora + "_" + minuto + "_" + segundo;
        return caracter;
    }

    public void archivo(String carpeta) {
        String valor = null, reforma = null, codificado = null, compromiso = null, devengado = null, ejecutado = null, saldoco = null, saldodev = null, cadena = null;
        File archivo;//para manipular el archivo
        FileWriter escribir;//para escribir en el archivo
        PrintWriter linea;//para información del archivo
        if (cmbTipo.getValue() != null && !cmbTipo.getValue().toString().isEmpty()) {
            if (cmbTipo.getValue().equals("1")) {
                cadena = ("Inicial" + cmbMes.getValue() + "_" + convertidor() + ".txt");
            } else if (cmbTipo.getValue().equals("2")) {
                cadena = ("Presupuesto" + cmbMes.getValue() + "_" + convertidor() + ".txt");
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
                    TablaGenerica tabIngreso = programas.getIngresos(cmbMes.getValue() + "", Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""),cmbAnio.getValue()+"");
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
                    System.out.println(Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""));
                    TablaGenerica tabIngreso = programas.getIngresos(cmbMes.getValue() + "", Integer.parseInt("1" + String.valueOf(cmbAnio.getValue()).substring(2, 4) + "" + cmbMes.getValue() + ""),cmbAnio.getValue()+"");
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
            descargarArchivo(archivo.toString());
        }
    }

    public void descargarArchivo(String archivo) {
        ServletContext serCon = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = serCon.getRealPath("") + "/" + "upload/auxiliar" + "/archivo_plano.txt";
        File file = new File(path);
        try (FileOutputStream out = new FileOutputStream(file.getAbsolutePath())) {
            File archivoCarga = new File(archivo);
            FileInputStream fis = new FileInputStream(archivoCarga);
            byte[] bytesIn = new byte[(int) archivoCarga.length()];
            fis.read(bytesIn);
            fis.close();
            out.write(bytesIn);
            out.flush();
            abrirMetodo("archivo_plano.txt");
        } catch (Exception e) {
        }
    }

    public void abrirMetodo(String archivo) {
        valor.clearInitialState();;
        botPrin.clearInitialState();
        quinde.clearInitialState();;

        valor.setId("valor");
        valor.setValue("Descargar Archivo");
        quinde.setId("quinde");
        quinde.setStyle("text-align:center;");
        quinde.setValue("imagenes/download-icone.png");
        botPrin.setId("botPrin");
        botPrin.setValue("./upload/auxiliar/" + archivo);
        botPrin.getChildren().add(valor);

        diaArchivo.Limpiar();
        diaArchivo.setDialogo(grida);
        gridA.getChildren().add(quinde);
        gridA.getChildren().add(botPrin);
        diaArchivo.setDialogo(gridA);
        diaArchivo.dibujar();

    }

    @Override
    public void insertar() {
//        cuentas();
//        tabTabla.insertar();
    }

    @Override
    public void guardar() {
        for (int i = 0; i < tabTabla.getTotalFilas(); i++) {
            TablaGenerica tabDato = programas.getVerificaParame(tabTabla.getValor(i, "tipo_presupuesto"), tabTabla.getValor(i, "clasificador_presu"),
                    tabTabla.getValor(i, "funcion"), tabTabla.getValor(i, "orientacion"), tabTabla.getValor(i, "programa"));
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
