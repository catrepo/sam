
package paq_webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-2b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ConsultaCiudadano", targetNamespace = "http://ruminahui.gob.ec/", wsdlLocation = "http://186.46.84.36/ConsultaCiudadania/ciudadania.asmx?WSDL")
public class ConsultaCiudadano
    extends Service
{

    private final static URL CONSULTACIUDADANO_WSDL_LOCATION;
    private final static WebServiceException CONSULTACIUDADANO_EXCEPTION;
    private final static QName CONSULTACIUDADANO_QNAME = new QName("http://ruminahui.gob.ec/", "ConsultaCiudadano");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://186.46.84.36/ConsultaCiudadania/ciudadania.asmx?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CONSULTACIUDADANO_WSDL_LOCATION = url;
        CONSULTACIUDADANO_EXCEPTION = e;
    }

    public ConsultaCiudadano() {
        super(__getWsdlLocation(), CONSULTACIUDADANO_QNAME);
    }

    public ConsultaCiudadano(WebServiceFeature... features) {
        super(__getWsdlLocation(), CONSULTACIUDADANO_QNAME, features);
    }

    public ConsultaCiudadano(URL wsdlLocation) {
        super(wsdlLocation, CONSULTACIUDADANO_QNAME);
    }

    public ConsultaCiudadano(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CONSULTACIUDADANO_QNAME, features);
    }

    public ConsultaCiudadano(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ConsultaCiudadano(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ConsultaCiudadanoSoap
     */
    @WebEndpoint(name = "ConsultaCiudadanoSoap")
    public ConsultaCiudadanoSoap getConsultaCiudadanoSoap() {
        return super.getPort(new QName("http://ruminahui.gob.ec/", "ConsultaCiudadanoSoap"), ConsultaCiudadanoSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ConsultaCiudadanoSoap
     */
    @WebEndpoint(name = "ConsultaCiudadanoSoap")
    public ConsultaCiudadanoSoap getConsultaCiudadanoSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://ruminahui.gob.ec/", "ConsultaCiudadanoSoap"), ConsultaCiudadanoSoap.class, features);
    }

    /**
     * 
     * @return
     *     returns ConsultaCiudadanoSoap
     */
    @WebEndpoint(name = "ConsultaCiudadanoSoap12")
    public ConsultaCiudadanoSoap getConsultaCiudadanoSoap12() {
        return super.getPort(new QName("http://ruminahui.gob.ec/", "ConsultaCiudadanoSoap12"), ConsultaCiudadanoSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ConsultaCiudadanoSoap
     */
    @WebEndpoint(name = "ConsultaCiudadanoSoap12")
    public ConsultaCiudadanoSoap getConsultaCiudadanoSoap12(WebServiceFeature... features) {
        return super.getPort(new QName("http://ruminahui.gob.ec/", "ConsultaCiudadanoSoap12"), ConsultaCiudadanoSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CONSULTACIUDADANO_EXCEPTION!= null) {
            throw CONSULTACIUDADANO_EXCEPTION;
        }
        return CONSULTACIUDADANO_WSDL_LOCATION;
    }

}
