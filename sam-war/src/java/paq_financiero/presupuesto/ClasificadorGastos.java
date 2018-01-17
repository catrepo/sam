/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_financiero.presupuesto;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_financiero.presupuesto.ejb.ReporteCedulas;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class ClasificadorGastos extends Pantalla {
    
    private Tabla tabTabla = new Tabla();
    private Combo cmbAnio = new Combo();
    //Creacion Calendarios
    private Calendario fechaInicio = new Calendario();
    private Calendario fechaFin = new Calendario();
    private Etiqueta txtFechaInicio = new Etiqueta("FECHA INICIAL : ");
    private Etiqueta txtFechaFin = new Etiqueta("FECHA FINAL : ");
    @EJB
    private ReporteCedulas admin = (ReporteCedulas) utilitario.instanciarEJB(ReporteCedulas.class);
    
    public ClasificadorGastos() {
        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("select ano_curso as anio, ano_curso from conc_ano where ano_curso>=2016 order by ano_curso desc ");
        bar_botones.agregarComponente(cmbAnio);
        
        Boton botMostrar = new Boton();
        botMostrar.setValue("Mostrar Cuentas");
        botMostrar.setExcluirLectura(true);
        botMostrar.setIcon("ui-icon-document-b");
        botMostrar.setMetodo("buscaDatos");
        bar_botones.agregarBoton(botMostrar);
        
        bar_botones.agregarSeparador();
        
        bar_botones.agregarComponente(txtFechaInicio);
        fechaInicio.setId("fechaInicio");
        fechaInicio.setFechaActual();
        fechaInicio.setTipoBoton(true);
        bar_botones.agregarComponente(fechaInicio);
        
        bar_botones.agregarComponente(txtFechaFin);
        fechaFin.setId("fechaFin");
        fechaFin.setFechaActual();
        fechaFin.setTipoBoton(true);
        bar_botones.agregarComponente(fechaFin);
        
        Boton botBuscar = new Boton();
        botBuscar.setValue("Extraer Cuentas");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("extraerCuentas");
        bar_botones.agregarBoton(botBuscar);
        
        bar_botones.agregarSeparador();
        
        Boton botDatos = new Boton();
        botDatos.setValue("Completar Infor.");
        botDatos.setExcluirLectura(true);
        botDatos.setIcon("ui-icon-document-b");
        botDatos.setMetodo("datosInfor");
        bar_botones.agregarBoton(botDatos);
        
        
        tabTabla.setId("tabTabla");
        tabTabla.setTabla("clas_orien_gasto_proyecto", "id_codigo", 1);
        tabTabla.getColumna("id_codigo").setVisible(false);
        tabTabla.getColumna("num_proyecto").setFiltro(true);
        tabTabla.getColumna("programa").setFiltro(true);
        tabTabla.getColumna("tipo").setFiltro(true);
        tabTabla.getColumna("nom_proyecto").setVisible(false);
        tabTabla.getColumna("auxiliar").setVisible(false);
        tabTabla.setHeader("CLASIFICADORES DE GASTOS");
        tabTabla.setRows(15);
        tabTabla.dibujar();
        
        PanelTabla tpt = new PanelTabla();
        tpt.setPanelTabla(tabTabla);
        
        Division div = new Division();
        div.dividir2(tpt, null, "95%", "h");
        agregarComponente(div);
        
        buscaDato();
    }
    
    public void extraerCuentas() {
        String mes14,
                mes15;
        if (String.valueOf((utilitario.getMes(fechaInicio.getFecha()))).length() > 1) {
            mes14 = String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
        } else {
            mes14 = "0" + String.valueOf((utilitario.getMes(fechaInicio.getFecha())));
        }
        if (String.valueOf((utilitario.getMes(fechaFin.getFecha()))).length() > 1) {
            mes15 = String.valueOf((utilitario.getMes(fechaFin.getFecha())));
        } else {
            mes15 = "0" + String.valueOf((utilitario.getMes(fechaFin.getFecha())));
        }
        Integer maximo = Integer.parseInt(admin.maxRegistro());
        TablaGenerica tabIngreso = admin.getDatosCedulasClasificador(Integer.parseInt("1" + (Integer.parseInt(fechaInicio.getFecha().toString().substring(2, 4)) - 1) + "14"), Integer.parseInt("1" + (Integer.parseInt(fechaFin.getFecha().toString().substring(2, 4)) - 1) + "15"),
                Integer.parseInt("1" + String.valueOf((utilitario.getAnio(fechaFin.getFecha()))).substring(2, 4) + "" + mes15));
        if (!tabIngreso.isEmpty()) {
            for (int i = 0; i < tabIngreso.getTotalFilas(); i++) {
                TablaGenerica tabDato = admin.getVerificaParametro(tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "TAAD01"),
                        tabIngreso.getValor(i, "AUAD01"), tabIngreso.getValor(i, "CCIADT"), tabIngreso.getValor(i, "AUAD02"));
                if (!tabDato.isEmpty()) {
                } else {
                    admin.setDatosParametrosGastos((maximo + i), tabIngreso.getValor(i, "cuendt"), tabIngreso.getValor(i, "TAAD01"), tabIngreso.getValor(i, "AUAD01"), tabIngreso.getValor(i, "CCIADT"),
                            tabIngreso.getValor(i, "proyecto"), tabIngreso.getValor(i, "dep"), tabIngreso.getValor(i, "AUAD02"), utilitario.getAnio(fechaInicio.getFecha()), tabIngreso.getValor(i, "orientacion"));
                }
            }
        } else {
        }
        utilitario.addUpdate("tabTabla");
    }
    
    public void datosInfor() {
        admin.setClasificadorGastos(utilitario.getAnio(utilitario.getFechaActual()));
    }

    /*
     * Opciones de busqueda
     */
    public void buscaDatos() {
        if (!getBuscaAnio().isEmpty()) {
            tabTabla.setCondicion(getBuscaAnio());
            tabTabla.ejecutarSql();
            utilitario.addUpdate("tabTabla");
        }
    }
    
    private String getBuscaAnio() {
        String str_filtros = "";
        str_filtros = "anio = " + cmbAnio.getValue() + "";
        return str_filtros;
    }
    
    public void buscaDato() {
        if (!getBuscaAni().isEmpty()) {
            tabTabla.setCondicion(getBuscaAni());
            tabTabla.ejecutarSql();
            utilitario.addUpdate("tabTabla");
        }
    }
    
    private String getBuscaAni() {
        String str_filtros = "";
        str_filtros = "anio = 2015";
        return str_filtros;
    }
    
    @Override
    public void insertar() {
    }
    
    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            guardarPantalla();
        }
    }
    
    @Override
    public void eliminar() {
        tabTabla.eliminar();
    }
    
    public Tabla getTabTabla() {
        return tabTabla;
    }
    
    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }
}
