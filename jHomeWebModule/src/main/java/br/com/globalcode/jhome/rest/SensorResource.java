/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.rest;

import br.com.globalcode.jhome.Environment;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author vsenger
 */
@Path("sensor")
@Stateless
public class SensorResource {
  @EJB
  private Environment environment;
  @Context
  private UriInfo context;

  /** Creates a new instance of SensorResource */
  public SensorResource() {
  }

  /**
   * Retrieves representation of an instance of br.com.globalcode.javahome.rest.SensorResource
   * @return an instance of java.lang.String
   */
  @GET
  @Produces("application/json")
  public Sensor getJson() {
    //TODO return proper representation object
    Sensor s = new Sensor();
    s.setTemperature("" + environment.getTemperature());
    s.setLight("" + environment.getLight());
    s.setDistance("" + environment.getDistance());
    return s;
  }

  /**
   * PUT method for updating or creating an instance of SensorResource
   * @param content representation for the resource
   * @return an HTTP response with content of the updated or created resource.
   */
  @PUT
  @Consumes("application/json")
  public void putJson(String content) {
  }
}
