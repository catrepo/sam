/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class CertificadoFondo extends Pantalla {

    private Tabla tabOrden = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Tabla setRegistro = new Tabla();
    private Panel panOpcion = new Panel();
    private AutoCompletar autBusca = new AutoCompletar();
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);

    public CertificadoFondo() {
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("CERTIFICADO DE DISPONIBILIDAD DE FONDOS ");
        agregarComponente(panOpcion);

        autBusca.setId("autBusca");
        autBusca.setAutoCompletar("SELECT c.id_codigo,c.numero_certificado,c.memorando,c.descripcion_proyecto FROM cert_fondos AS c\n"
                + "ORDER BY c.numero_certificado");
        autBusca.setSize(70);
        bar_botones.agregarComponente(new Etiqueta("Busca Registro"));
        bar_botones.agregarComponente(autBusca);

        dibujaCertificado();
    }

    public void dibujaCertificado() {
        limpiarPanel();
        tabOrden.setId("tabOrden");
        tabOrden.setTabla("cert_fondos", "id_codigo", 1);
        if (autBusca.getValue() == null) {
            tabOrden.setCondicion("id_codigo=-1");
        } else {
            tabOrden.setCondicion("id_codigo=" + autBusca.getValor());
        }
        tabOrden.getColumna("fecha_certificado").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("fecha_ingreso").setValorDefecto(utilitario.getFechaActual());
        tabOrden.getColumna("login_ingreso").setValorDefecto(tabConsulta.getValor("NICK_USUA"));
        tabOrden.getColumna("ip_ingreso").setValorDefecto(utilitario.getIp());
        List list = new ArrayList();
        Object fil1[] = {
            "Ingresado", "Ingresado"
        };
        Object fil2[] = {
            "Anulado", "Anulado"
        };
        list.add(fil1);;
        list.add(fil2);;
        tabOrden.getColumna("estado").setCombo(list);
        tabOrden.getColumna("fecha_certificado").setVisible(false);
        tabOrden.getColumna("fecha_ingreso").setVisible(false);
        tabOrden.getColumna("login_ingreso").setVisible(false);
        tabOrden.getColumna("observacion").setVisible(false);
        tabOrden.getColumna("ip_ingreso").setVisible(false);
        tabOrden.getColumna("login_anulacion").setVisible(false);
        tabOrden.getColumna("fecha_anulacion").setVisible(false);
        tabOrden.setTipoFormulario(true);
        tabOrden.getGrid().setColumns(2);
        tabOrden.dibujar();
        PanelTabla pto = new PanelTabla();
        pto.setPanelTabla(tabOrden);
        Grupo gru = new Grupo();
        gru.getChildren().add(pto);
        panOpcion.getChildren().add(gru);
    }

    private void limpiarPanel() {
        panOpcion.getChildren().clear();
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
        limpiarPanel();
        utilitario.addUpdate("panOpcion");
    }

//    public void numero() {
//        tabOrden.setValor("estado", "Ingresado");
//        String numero = programas.maxNumCertificado(utilitario.getAnio(utilitario.getFechaActual()));
//        String num;
//        Integer cantidad = 0;
//        cantidad = Integer.parseInt(numero) + 1;
//        if (numero != null) {
//            if (cantidad >= 0 && cantidad <= 9) {
//                num = "00" + String.valueOf(cantidad);
//                tabOrden.setValor("numero_certificado", num);
//            } else if (cantidad >= 10 && cantidad <= 99) {
//                num = "0" + String.valueOf(cantidad);
//                tabOrden.setValor("numero_certificado", num);
//            } else if (cantidad >= 100 && cantidad <= 999) {
//                num = String.valueOf(cantidad);
//                tabOrden.setValor("numero_certificado", num);
//            }
//        }
//        utilitario.addUpdate("tabOrden");
//    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
//        numero();
    }

    @Override
    public void guardar() {
        if (tabOrden.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Tabla getTabOrden() {
        return tabOrden;
    }

    public void setTabOrden(Tabla tabOrden) {
        this.tabOrden = tabOrden;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
    }
}
