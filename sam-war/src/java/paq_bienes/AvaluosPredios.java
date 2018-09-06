/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_bienes;

import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_bienes.ejb.acfbienes;
import persistencia.Conexion;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class AvaluosPredios extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Tabla selCatastro = new Tabla();
    private Tabla selBienes = new Tabla();

    private Conexion conCayman = new Conexion();

    private Confirmar conInformacion = new Confirmar();

    private Reporte rep_reporte = new Reporte();
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    @EJB
    private acfbienes activos = (acfbienes) utilitario.instanciarEJB(acfbienes.class);

    public AvaluosPredios() {

        conCayman.setUnidad_persistencia(utilitario.getPropiedad("recursojdbcayman"));
        conCayman.NOMBRE_MARCA_BASE = "sqlserver";

        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonsNavegacion();

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Boton btnProcesar = new Boton();
        btnProcesar.setValue("PROCESAR");
        btnProcesar.setExcluirLectura(true);
        btnProcesar.setIcon("ui-icon-gear");
        btnProcesar.setMetodo("procesoConfirmacion");
        bar_botones.agregarBoton(btnProcesar);

        conInformacion.setId("conInformacion");
        agregarComponente(conInformacion);

        selCatastro.setId("selCatastro");
        selCatastro.setConexion(conCayman);
        selCatastro.setSql("select MAE_CLVACT,MAE_BARRIO,area,VAL_AVATER,VAL_OTRMEJ,VAL_AVACON,valor_total, 0 as id\n"
                + "from vw_CatastroUrbanoRural\n"
                + "inner join vw_ActivosInmuebles on MAE_CLVACT = clave\n"
                + "where ISNULL(valor_total,0)  <> isnull(act_valorcompra,0)\n"
                + "order by MAE_CLVACT");
        selCatastro.getColumna("id").setCheck();
        selCatastro.getColumna("MAE_BARRIO").setLongitud(75);
        selCatastro.getColumna("valor_total").setLectura(true);
        selCatastro.getColumna("id").setMetodoChange("buscaRegistro");
        selCatastro.setRows(25);
//        selCatastro.setLectura(true);
        selCatastro.dibujar();

        PanelTabla pntCat = new PanelTabla();
        pntCat.setMensajeWarn("INFORMACIÓN CATASTRAL");
        pntCat.setPanelTabla(selCatastro);

        selBienes.setId("selBienes");
        selBienes.setConexion(conCayman);
        selBienes.setSql("select Act_id,act_codbarras,clave,gru_nombre1,act_valorcompra, 0 as id\n"
                + "from vw_CatastroUrbanoRural\n"
                + "inner join vw_ActivosInmuebles on MAE_CLVACT = clave\n"
                + "where ISNULL(valor_total,0)  <> isnull(act_valorcompra,0)\n"
                + "order by MAE_CLVACT");
        selBienes.getColumna("id").setCheck();
        selBienes.getColumna("act_codbarras").setLongitud(25);
        selBienes.getColumna("clave").setLongitud(20);
        selBienes.getColumna("act_valorcompra").setLectura(true);
        selBienes.setRows(25);
//        selBienes.setLectura(true);
        selBienes.dibujar();

        PanelTabla pntBie = new PanelTabla();
        pntBie.setMensajeWarn("INFORMACIÓN BIENES");
        pntBie.setPanelTabla(selBienes);

        Division div = new Division();
        div.setId("div");
        div.dividir2(pntCat, pntBie, "50%", "H");
        agregarComponente(div);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conCayman);
        agregarComponente(sef_formato);
    }

    public void buscaRegistro() {
        if (selCatastro.getValor("id").equals("true")) {
            for (int i = 0; i < selBienes.getTotalFilas(); i++) {
                if (selBienes.getValor(i, "clave").contains(selCatastro.getValor("MAE_CLVACT"))) {
                    selBienes.setValor(i, "id", "true");
                    if (Double.parseDouble(selBienes.getValor(i, "act_valorcompra")) > Double.parseDouble(selCatastro.getValor("valor_total"))) {
                        utilitario.agregarMensajeInfo("CLAVE CATASTRAL " + selCatastro.getValor("MAE_CLVACT"), "VALOR DE COMPRA BIENES " + selBienes.getValor(i, "act_valorcompra") + "\n MAYOR A VALOR DE CATASTRO " + selCatastro.getValor("valor_total"));
                    } else {
                        utilitario.agregarMensaje("CLAVE CATASTRAL QUE SE VA A MODIFICAR : " + selCatastro.getValor("MAE_CLVACT"), "VALOR DE BIENES " + selBienes.getValor(i, "act_valorcompra") + "\n VALOR DE CATASTRO " + selCatastro.getValor("valor_total"));
                    }
                }
            }
            utilitario.addUpdate("selBienes");
        } else {
            for (int i = 0; i < selBienes.getTotalFilas(); i++) {
                if (selBienes.getValor(i, "clave").contains(selCatastro.getValor("MAE_CLVACT"))) {
                    selBienes.setValor(i, "id", "false");
                }
            }
            utilitario.addUpdate("selBienes");
        }
    }

    public void procesoConfirmacion() {
        conInformacion.setMessage("Esta seguro de realizar la actualización de los datos");
        conInformacion.setTitle("Confirmación Actualización");
        conInformacion.getBot_aceptar().setMetodo("procesoActulizacion");
        conInformacion.dibujar();
        utilitario.addUpdate("conInformacion");
    }

    public void procesoActulizacion() {
        for (int i = 0; i < selCatastro.getTotalFilas(); i++) {
            if (selCatastro.getValor(i, "id").equals("true")) {
                activos.set_AvaluoBienes(selCatastro.getValor(i, "MAE_CLVACT"), tabConsulta.getValor("NICK_USUA"));
//                System.err.println("-->>"+ selCatastro.getValor(i, "MAE_CLVACT"));
            }
        }
        selBienes.actualizar();
        selCatastro.actualizar();
        utilitario.agregarMensaje("Registros Actualizados", "");
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
            case "Reporte Cambios":
                mostrarReporte();
                break;
        }
    }

    public void mostrarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "Reporte Cambios":
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Tabla getSelCatastro() {
        return selCatastro;
    }

    public void setSelCatastro(Tabla selCatastro) {
        this.selCatastro = selCatastro;
    }

    public Tabla getSelBienes() {
        return selBienes;
    }

    public void setSelBienes(Tabla selBienes) {
        this.selBienes = selBienes;
    }

    public Conexion getConCayman() {
        return conCayman;
    }

    public void setConCayman(Conexion conCayman) {
        this.conCayman = conCayman;
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

}
