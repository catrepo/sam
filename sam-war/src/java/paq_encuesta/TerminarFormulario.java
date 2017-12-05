/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_encuesta;

import framework.componentes.Boton;
import framework.componentes.Grid;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author p-chumana
 */
public class TerminarFormulario extends Pantalla {

    public TerminarFormulario() {
        bar_botones.quitarBotonsNavegacion();
        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();

        Grid griPri = new Grid();
        griPri.setColumns(3);
        Grid griBot = new Grid();
        griBot.setColumns(3);
        Boton botBuscar = new Boton();
        botBuscar.setValue("Buscar ");
        botBuscar.setExcluirLectura(true);
        botBuscar.setIcon("ui-icon-search");
        botBuscar.setMetodo("busquedaInfo");
        griBot.getChildren().add(botBuscar);
        Boton botSearch = new Boton();
        botSearch.setValue("Buscar General");
        botSearch.setExcluirLectura(true);
        botSearch.setIcon("ui-icon-wrench");
        botSearch.setMetodo("buscaGeneral");
        griBot.getChildren().add(botSearch);
        Boton botEnd = new Boton();
        botEnd.setValue("Finalizar");
        botEnd.setExcluirLectura(true);
        botEnd.setIcon("ui-icon-close");
        botEnd.setMetodo("cierreDoc");
        griBot.getChildren().add(botEnd);
        Boton botVer = new Boton();
        botVer.setValue("Ver");
        botVer.setExcluirLectura(true);
        botVer.setIcon("ui-icon-open");
        botVer.setMetodo("crearArchivo");
        griBot.getChildren().add(botVer);

        Boton botEditar = new Boton();
        botEditar.setValue("Editar");
        botEditar.setExcluirLectura(true);
        botEditar.setIcon("ui-icon-open");
        botEditar.setMetodo("abrirEdicion");
        griBot.getChildren().add(botEditar);

        Boton botArchivo = new Boton();
        botArchivo.setValue("Re-asignar");
        botArchivo.setExcluirLectura(true);
        botArchivo.setIcon("ui-icon-open");
        botArchivo.setMetodo("asignaDocumento");
        griBot.getChildren().add(botArchivo);

        griPri.getChildren().add(griBot);
        bar_botones.agregarComponente(griPri);
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
}
