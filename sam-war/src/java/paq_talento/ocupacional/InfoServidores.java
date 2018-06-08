/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_talento.ocupacional;

import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Division;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class InfoServidores extends Pantalla {

    private Tabla setEmpleado = new Tabla();
    private Tabla tabDatos = new Tabla();
    private Tabla tabConyugue = new Tabla();
    private Tabla tabHijos = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Combo cmbTipo = new Combo();
    private Conexion conNomina = new Conexion();
    private Panel panOpcion = new Panel();

    //Declaración para reportes
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    public InfoServidores() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        List lisTipo = new ArrayList();
        Object fila1[] = {
            "NL", "Empleado"
        };
        Object fila2[] = {
            "CT", "Trabajador"
        };
        Object fila3[] = {
            "JUB", "Jubilado"
        };
        lisTipo.add(fila1);;
        lisTipo.add(fila2);;
        lisTipo.add(fila3);;

        cmbTipo.setId("cmbTipo");
        cmbTipo.setCombo(lisTipo);
        bar_botones.agregarComponente(cmbTipo);

        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar Registros");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-person");
        botBuscar.setMetodo("buscaEmpleados");
        bar_botones.agregarBoton(botBuscar);

        bar_botones.agregarSeparador();
        Boton botSearch = new Boton();
        botSearch.setValue("Muestra Información");
        botSearch.setExcluirLectura(true);
        botSearch.setIcon("ui-icon-search");
        botSearch.setMetodo("buscaRegistro");
        bar_botones.agregarBoton(botSearch);

        conNomina.setUnidad_persistencia(utilitario.getPropiedad("oraclejdb"));
        conNomina.NOMBRE_MARCA_BASE = "oracle";

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
//        panOpcion.setHeader("ABASTECIMIENTO DE COMBUSTIBLE");
        agregarComponente(panOpcion);

        /*         * CONFIGURACIÓN DE OBJETO REPORTE         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conNomina);
        agregarComponente(sef_formato);
        
        dibujarPantalla();

    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
        utilitario.addUpdate("panOpcion");
    }

    public void dibujarPantalla() {
        limpiarPanel();
        setEmpleado.setId("setEmpleado");
        setEmpleado.setConexion(conNomina);
        setEmpleado.setSql("select codtra,cedciu,nomtra from nodattra where tipctt = 'L' order by tipctt,nomtra");
        setEmpleado.getColumna("cedciu").setFiltro(true);
        setEmpleado.getColumna("nomtra").setFiltro(true);
        setEmpleado.setRows(27);
        setEmpleado.setLectura(true);
        setEmpleado.dibujar();
        PanelTabla pntt = new PanelTabla();
        pntt.setPanelTabla(setEmpleado);

        tabDatos.setId("tabDatos");
        tabDatos.setConexion(conNomina);
        tabDatos.setHeader("INFORMACIÓN PERSONAL");
        tabDatos.setSql("select codtra,nomtra,cedciu,fecnac,fecing,suebas,\n"
                + "(select noldat from nodatdat where tipdat='TC' AND CODDAT=TIPCTT) AS RELACION_LABORAL,\n"
                + "(select noldat from nodatdat where tipdat='TP' AND CODDAT=TIPAFI) AS CONTRATO,\n"
                + "(SELECT NOLCGO FROM NODATCGO WHERE NODATCGO.CODCGO=NODATTRA.CODCGO AND NODATCGO.CODCIA=NODATTRA.CODCIA) AS CARGO,\n"
                + "(SELECT NOLARE FROM NODATARE WHERE NODATARE.CODCIA=NODATTRA.CODCIA AND NODATARE.CODARE=NODATTRA.CODARE) AS AREA,tlftra, tlfpar, mailtra\n"
                + "from nodattra WHERE codtra = '00'");
        tabDatos.getColumna("codtra").setVisible(false);
        tabDatos.getColumna("nomtra").setNombreVisual("NOMBRE");
        tabDatos.getColumna("suebas").setNombreVisual("SUELDO");
        tabDatos.setTipoFormulario(true);
        tabDatos.getGrid().setColumns(6);
        tabDatos.dibujar();
        PanelTabla pnt = new PanelTabla();
        pnt.setPanelTabla(tabDatos);

        tabConyugue.setId("tabConyugue");
        tabConyugue.setConexion(conNomina);
        tabConyugue.setHeader("DATOS CONYUGUE U OTROS");
        tabConyugue.setSql("select nomcfa,apecfa,fecnac, parcfa,trunc((SYSDATE - to_date(fecnac,'dd/mm/rrrr'))/365,0) as edad\n"
                + "from nocarfam\n"
                + "where cedciu ='00'");
        tabConyugue.setRows(27);
        tabConyugue.getColumna("nomcfa").setNombreVisual("NOMBRE");
        tabConyugue.getColumna("apecfa").setNombreVisual("APELLIDO");
        tabConyugue.getColumna("parcfa").setNombreVisual("PARENTESCO");
        tabConyugue.setLectura(true);
        tabConyugue.dibujar();
        PanelTabla pno = new PanelTabla();
        pno.setPanelTabla(tabConyugue);
        
        tabHijos.setId("tabHijos");
        tabHijos.setConexion(conNomina);
        tabHijos.setHeader("INFORMACIÓN HIJOS");
        tabHijos.setSql("select nomcfa,apecfa,fecnac, parcfa,trunc((SYSDATE - to_date(fecnac,'dd/mm/rrrr'))/365,0) as edad\n"
                + "from nocarfam\n"
                + "where cedciu ='00'");
        tabHijos.setRows(27);
        tabHijos.getColumna("nomcfa").setNombreVisual("NOMBRE");
        tabHijos.getColumna("apecfa").setNombreVisual("APELLIDO");
        tabHijos.getColumna("parcfa").setNombreVisual("PARENTESCO");
        tabHijos.setLectura(true);
        tabHijos.dibujar();
        PanelTabla pnh = new PanelTabla();
        pnh.setPanelTabla(tabHijos);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pno, pnh, "50%", "V");
//        
        Division division = new Division();
        division.setId("division");
        division.dividir2(pnt, div, "40%", "H");

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(pntt, division, "28%", "V");
        Grupo gru = new Grupo();
        gru.getChildren().add(div_division);
        panOpcion.getChildren().add(gru);
    }

    public void buscaEmpleados() {
        setEmpleado.setSql("select codtra,cedciu,nomtra \n"
                + "from nodattra \n"
                + "where tipctt = '" + cmbTipo.getValue() + "' \n"
                + "order by tipctt,nomtra");
        setEmpleado.ejecutarSql();
    }

    public void buscaRegistro() {
        tabDatos.setSql("select codtra,nomtra,cedciu,fecnac,fecing,suebas,\n"
                + "(select noldat from nodatdat where tipdat='TC' AND CODDAT=TIPCTT) AS RELACION_LABORAL,\n"
                + "(select noldat from nodatdat where tipdat='TP' AND CODDAT=TIPAFI) AS CONTRATO,\n"
                + "(SELECT NOLCGO FROM NODATCGO WHERE NODATCGO.CODCGO=NODATTRA.CODCGO AND NODATCGO.CODCIA=NODATTRA.CODCIA) AS CARGO,\n"
                + "(SELECT NOLARE FROM NODATARE WHERE NODATARE.CODCIA=NODATTRA.CODCIA AND NODATARE.CODARE=NODATTRA.CODARE) AS AREA,tlftra, tlfpar, mailtra\n"
                + "from nodattra WHERE codtra = '" + setEmpleado.getValorSeleccionado() + "'");
        tabDatos.ejecutarSql();
        buscaHijos();
        buscaOtros();
    }

    public void buscaHijos() {
        tabHijos.setSql("select nomcfa,apecfa,fecnac, parcfa,trunc((SYSDATE - to_date(fecnac,'dd/mm/rrrr'))/365,0) as edad\n"
                + "from nocarfam\n"
                + "where parcfa like 'HIJ%' and cedciu =(select cedciu\n"
                + "from nodattra WHERE codtra ='" + setEmpleado.getValorSeleccionado() + "' )");
        tabHijos.ejecutarSql();
    }

    public void buscaOtros() {
        tabConyugue.setSql("select nomcfa,apecfa,fecnac, parcfa,trunc((SYSDATE - to_date(fecnac,'dd/mm/rrrr'))/365,0) as edad\n"
                + "from nocarfam\n"
                + "where parcfa not like 'HIJ%' and cedciu =(select cedciu\n"
                + "from nodattra WHERE codtra ='" + setEmpleado.getValorSeleccionado() + "' )");
        tabConyugue.ejecutarSql();
    }
    
    
    @Override
    public void insertar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "CONFIDENCIAL SERVIDORES":
                aceptoAnticipo();
                break;
        }
    }

    public void aceptoAnticipo() {
        switch (rep_reporte.getNombre()) {
            case "CONFIDENCIAL SERVIDORES":
                sef_formato.setConexion(conNomina);
                    p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                    p_parametros.put("identificacion", setEmpleado.getValorSeleccionado() + "");
                    rep_reporte.cerrar();
                    sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                    sef_formato.dibujar();
                rep_reporte.cerrar();
                System.out.println("paq_talento.ocupacional.InfoServidores.aceptoAnticipo()"+rep_reporte.getPath());
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    
    public Tabla getSetEmpleado() {
        return setEmpleado;
    }

    public void setSetEmpleado(Tabla setEmpleado) {
        this.setEmpleado = setEmpleado;
    }

    public Tabla getTabDatos() {
        return tabDatos;
    }

    public void setTabDatos(Tabla tabDatos) {
        this.tabDatos = tabDatos;
    }

    public Tabla getTabConyugue() {
        return tabConyugue;
    }

    public void setTabConyugue(Tabla tabConyugue) {
        this.tabConyugue = tabConyugue;
    }

    public Tabla getTabHijos() {
        return tabHijos;
    }

    public void setTabHijos(Tabla tabHijos) {
        this.tabHijos = tabHijos;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }

    public Conexion getConNomina() {
        return conNomina;
    }

    public void setConNomina(Conexion conNomina) {
        this.conNomina = conNomina;
    }

}
