/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.aplicacion.TablaGenerica;
import framework.componentes.Arbol;
import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import paq_nomina.ejb.AntiSueldos;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class direccionResponsable extends Pantalla {

    private Tabla tabRegistro = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Arbol arbArbol = new Arbol();

    @EJB
    private AntiSueldos admin = (AntiSueldos) utilitario.instanciarEJB(AntiSueldos.class);
    
    public direccionResponsable() {
        /*
         * Permite tener acceso a información, de los datos de registro
         */
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("SELECT u.IDE_USUA,u.NOM_USUA,u.NICK_USUA,u.IDE_PERF,p.NOM_PERF,p.PERM_UTIL_PERF\n"
                + "FROM SIS_USUARIO u,SIS_PERFIL p where u.IDE_PERF = p.IDE_PERF and IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        tabRegistro.setId("tabRegistro");
        tabRegistro.setNumeroTabla(1);
        tabRegistro.agregarArbol(arbArbol);
//        tabRegistro.getColumna("ide_responsable").setMetodoChange("datosEmpleado");
        tabRegistro.dibujar();
        PanelTabla patreg = new PanelTabla();
        patreg.setMensajeWarn("Información de Direcciones y Responsables");
        patreg.setPanelTabla(tabRegistro);

        arbArbol.setId("arbArbol");
        arbArbol.dibujar();

        Division div = new Division();
        div.setId("div");
        div.dividir2(arbArbol, patreg, "21%", "V");
        agregarComponente(div);
    }

    public void datosEmpleado(){
        TablaGenerica tabDato = admin.getDatosServidor(tabRegistro.getValor("ide_responsable"));
        if (!tabDato.isEmpty()) {
            tabRegistro.setValor("ide_responsable",tabDato.getValor("nomtra"));
            utilitario.addUpdate("tabRegistro");
        }
    }
    
    @Override
    public void insertar() {
        tabRegistro.insertar();
    }

    @Override
    public void guardar() {
        if (tabRegistro.guardar()) {
            guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        tabRegistro.eliminar();
    }

    public Tabla getTabRegistro() {
        return tabRegistro;
    }

    public void setTabRegistro(Tabla tabRegistro) {
        this.tabRegistro = tabRegistro;
    }

    public Arbol getArbArbol() {
        return arbArbol;
    }

    public void setArbArbol(Arbol arbArbol) {
        this.arbArbol = arbArbol;
    }
}
