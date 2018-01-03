/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_remuneraciones;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_remuneraciones.ejb.BeanRemuneracion;
import sistema.aplicacion.Pantalla;
import paq_utilitario.ejb.ClaseGenerica;
import persistencia.Conexion;

/**
 *
 * @author p-chumana
 */
public class ConfidencialRol extends Pantalla {

    private Tabla tabConsulta = new Tabla();
    private Conexion conNomina = new Conexion();
    private Combo cmbAnio = new Combo();
    private Combo cmbPeriodo = new Combo();
    private Combo cmbSevidor = new Combo();
    private Etiqueta txeAnio = new Etiqueta("AÑO :");
    private Etiqueta txePeriodo = new Etiqueta("PERIODO : ");
    private Etiqueta txeCedula = new Etiqueta("CEDULA : ");
    private Texto txtCedula = new Texto();
    String selec_mes = new String();
    private Panel panOpcion = new Panel();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    @EJB
    private ClaseGenerica generico = (ClaseGenerica) utilitario.instanciarEJB(ClaseGenerica.class);
    private BeanRemuneracion adminRemuneracion = (BeanRemuneracion) utilitario.instanciarEJB(BeanRemuneracion.class);
    public ConfidencialRol() {

        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        Imagen encabeza = new Imagen();
        encabeza.setStyle("font-size:19px;color:black;text-align:center;");
        encabeza.setValue("imagenes/logo_talento.png");

        Imagen pie = new Imagen();
        pie.setStyle("font-size:19px;color:black;text-align:center;");
        pie.setValue("imagenes/advertencia.png");


        //cadena de conexión para otra base de datos
        conNomina.setUnidad_persistencia(utilitario.getPropiedad("oraclejdb"));
        conNomina.NOMBRE_MARCA_BASE = "oracle";

        Panel tabp2 = new Panel();
        tabp2.setStyle("font-size:19px;color:black;text-align:center;");
        tabp2.setHeader("CONFIDENCIAL INDIVIDUAL SERVIDORES");
        Panel tabp3 = new Panel();
        tabp3.setStyle("font-size:19px;color:black;text-align:center;");
        tabp3.getChildren().add(pie);

        cmbAnio.setId("cmbAnio");
        cmbAnio.setCombo("SELECT ano_curso,ano_curso as anio FROM conc_ano  where ano_curso>=(select case when parametro2>=2016 then 2016 end\n"
                + "from nom_parametros where ide_parametro ='VCR') order by ano_curso desc");
        cmbAnio.setMetodo("cargaMes");

        cmbPeriodo.setId("cmbPeriodo");
        cmbPeriodo.setCombo("SELECT periodo,periodo as mes\n"
                + "FROM conc_periodo \n"
                + "where cast(CONCAT (MONTH(GETDATE()), DAY(GETDATE())+(case when  DAY(GETDATE()) > (select  parametro  from nom_parametros where ide_parametro ='VCR') then +1 else 0 end)) as integer)>cast(CONCAT(periodo, DAY(GETDATE()))as integer) order by periodo desc");

        cmbSevidor.setId("cmbSevidor");
        cmbSevidor.setConexion(conNomina);
        cmbSevidor.setCombo("select cedciu,nomtra from nodattra where tipctt <> 'JUB' order by nomtra");
        //cmbSevidor.setCombo("select cedciu,nomtra from nodttrpr where tipctt <> 'JUB' order by nomtra");
        Boton botBuscar = new Boton();
        botBuscar.setValue("Imprimir");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-document-b");
        botBuscar.setMetodo("abrirListaReportes");
        bar_botones.agregarBoton(botBuscar);

        tabp2.getChildren().add(encabeza);
        tabp2.getChildren().add(panOpcion);
        tabp2.getChildren().add(botBuscar);
        tabp2.getChildren().add(tabp3);


        agregarComponente(tabp2);
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conNomina);
        agregarComponente(sef_formato);
        dibujaPantalla();
    }

    public void cargaMes() {
        cmbPeriodo.setId("cmbPeriodo");
        cmbPeriodo.setCombo("select periodo, mes \n"
                + "from (\n"
                + "select a.*, (case when a.ban = 1 then cast(CONCAT (MONTH(GETDATE()), DAY(GETDATE())+(case when  DAY(GETDATE()) > (select parametro from nom_parametros where ide_parametro ='VCR') then +1 else 0 end)) as integer) else 0 end) as ban1\n"
                + ",cast(CONCAT(periodo, DAY(GETDATE()))as integer) as ban2\n"
                + "from (\n"
                + "SELECT periodo,periodo as mes,(case when (select parametro2 from nom_parametros where ide_parametro ='VCR') = "+cmbAnio.getValue()+" then 1 \n"
                + "else 0 end ) as ban\n"
                + "FROM conc_periodo) a) b\n"
                + "where ban1>ban2 or ban1<ban2");
        utilitario.addUpdate("cmbPeriodo");
    }

    public void dibujaPantalla() {
        panOpcion.setId("panOpcion");
        panOpcion.setStyle("font-size:12px;color:black;text-align:left;");
        panOpcion.setTransient(true);
        panOpcion.setHeader("SELECCIONE PARAMETROS DE REPORTE");

        Grid griCuerpo = new Grid();
        griCuerpo.setColumns(2);
        griCuerpo.getChildren().add(txeAnio);
        griCuerpo.getChildren().add(cmbAnio);
        griCuerpo.getChildren().add(txePeriodo);
        griCuerpo.getChildren().add(cmbPeriodo);
        if (isNumeric(utilitario.getVariable("NICK"))) {
            System.out.println(utilitario.getVariable("NICK") + " es un numero");
        } else {
            griCuerpo.getChildren().add(txeCedula);
            griCuerpo.getChildren().add(cmbSevidor);
        }
        panOpcion.getChildren().add(griCuerpo);

    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
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
                dibujarReporte();
                break;
        }
    }

    public void dibujarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "CONFIDENCIAL SERVIDORES":
                Integer dia = 0,
                 mes = 0,
                 anio = 0,
                 anioa = 0,
                 mesa = 0;
                String cedula = "",
                 tabla = "";
                double ingreso = 0.0,
                 egreso = 0.0,
                 liquido = 0.0;
                dia = utilitario.getDia(utilitario.getFechaActual());
                mes = utilitario.getMes(utilitario.getFechaActual());
                anio = utilitario.getAnio(utilitario.getFechaActual());
                mesa = Integer.parseInt(cmbPeriodo.getValue().toString());
                anioa = Integer.parseInt(cmbAnio.getValue().toString());


                if (isNumeric(utilitario.getVariable("NICK"))) {
                    cedula = utilitario.getVariable("NICK");
                } else {
                    cedula = cmbSevidor.getValue() + "";
                }

                if (anio.equals(anioa)) {
                    if (mes.equals(mesa)) {
                        if (dia >= 5 && dia <= 15) {
                            tabla = "0";
                        } else {
                            tabla = "1";
                        }
                    } else {
                        TablaGenerica tabValida = adminRemuneracion.getAnteriorServidores(cedula, generico.meses(Integer.parseInt(cmbPeriodo.getValue() + "")), cmbAnio.getValue() + "", "0");
                        if (!tabValida.isEmpty()) {
                            tabla = "0";
                        } else {
                            tabla = "1";
                        }
                    }
                } else {
                     TablaGenerica tabValida = adminRemuneracion.getActualServidores(cedula, generico.meses(Integer.parseInt(cmbPeriodo.getValue() + "")), cmbAnio.getValue() + "", "1");
                        if (!tabValida.isEmpty()) {
                            tabla = "1";
                        } else {
                            tabla = "0";
                        }
                }

                TablaGenerica tabValidar = adminRemuneracion.getActualServidores(cedula, generico.meses(Integer.parseInt(cmbPeriodo.getValue() + "")), cmbAnio.getValue() + "", tabla);
                if (!tabValidar.isEmpty()) {
                    ingreso = Double.parseDouble(tabValidar.getValor("ingreso"));
                    egreso = Double.parseDouble(tabValidar.getValor("egreso"));
                    liquido = Double.parseDouble(tabValidar.getValor("liquido"));
                } else {
                    TablaGenerica tabValida = adminRemuneracion.getAnteriorServidores(cedula, generico.meses(Integer.parseInt(cmbPeriodo.getValue() + "")), cmbAnio.getValue() + "", tabla);
                    if (!tabValida.isEmpty()) {
                        ingreso = Double.parseDouble(tabValida.getValor("ingreso"));
                        egreso = Double.parseDouble(tabValida.getValor("egreso"));
                        liquido = Double.parseDouble(tabValida.getValor("liquido"));
                    }
                }
                p_parametros.put("anio", cmbAnio.getValue() + "");
                p_parametros.put("mes", generico.meses(Integer.parseInt(cmbPeriodo.getValue() + "")));
                p_parametros.put("cedula", cedula);
                p_parametros.put("mes1", meses(Integer.parseInt(cmbPeriodo.getValue() + "")));
                p_parametros.put("bandera", tabla);
                p_parametros.put("ingreso", ingreso);
                p_parametros.put("egreso", egreso);
                p_parametros.put("liquido", liquido);
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                System.out.println("->> " + p_parametros);
                sef_formato.dibujar();
                break;
        }
    }

    public String meses(Integer numero) {
        switch (numero) {
            case 12:
                selec_mes = "Diciembre";
                break;
            case 11:
                selec_mes = "Noviembre";
                break;
            case 10:
                selec_mes = "Octubre";
                break;
            case 9:
                selec_mes = "Septiembre";
                break;
            case 8:
                selec_mes = "Agosto";
                break;
            case 7:
                selec_mes = "Julio";
                break;
            case 6:
                selec_mes = "Junio";
                break;
            case 5:
                selec_mes = "Mayo";
                break;
            case 4:
                selec_mes = "Abril";
                break;
            case 3:
                selec_mes = "Marzo";
                break;
            case 2:
                selec_mes = "Febrero";
                break;
            case 1:
                selec_mes = "Enero";
                break;
        }
        return selec_mes;
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

    public Conexion getConNomina() {
        return conNomina;
    }

    public void setConNomina(Conexion conNomina) {
        this.conNomina = conNomina;
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
