
package paq_webservice;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the paq_webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: paq_webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BusquedaCedulaActual }
     * 
     */
    public BusquedaCedulaActual createBusquedaCedulaActual() {
        return new BusquedaCedulaActual();
    }

    /**
     * Create an instance of {@link BusquedaPorCedula }
     * 
     */
    public BusquedaPorCedula createBusquedaPorCedula() {
        return new BusquedaPorCedula();
    }

    /**
     * Create an instance of {@link BusquedaCedulaActualResponse }
     * 
     */
    public BusquedaCedulaActualResponse createBusquedaCedulaActualResponse() {
        return new BusquedaCedulaActualResponse();
    }

    /**
     * Create an instance of {@link ClassCiudadania }
     * 
     */
    public ClassCiudadania createClassCiudadania() {
        return new ClassCiudadania();
    }

    /**
     * Create an instance of {@link BusquedaPorRuc }
     * 
     */
    public BusquedaPorRuc createBusquedaPorRuc() {
        return new BusquedaPorRuc();
    }

    /**
     * Create an instance of {@link BusquedaPorCedulaResponse }
     * 
     */
    public BusquedaPorCedulaResponse createBusquedaPorCedulaResponse() {
        return new BusquedaPorCedulaResponse();
    }

    /**
     * Create an instance of {@link BusquedaPorRucResponse }
     * 
     */
    public BusquedaPorRucResponse createBusquedaPorRucResponse() {
        return new BusquedaPorRucResponse();
    }

}
