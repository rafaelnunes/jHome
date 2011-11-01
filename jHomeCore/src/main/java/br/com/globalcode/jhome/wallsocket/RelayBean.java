/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.wallsocket;

import br.com.globalcode.jhome.DeviceManager;
import br.com.globalcode.jhome.Relay;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author vsenger
 */
@Stateless
public class RelayBean implements Relay {

  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")
  @EJB
  private DeviceManager deviceManager;
  
  public void turnOn(String name) {
    try {
      deviceManager.execute(name, "+");
    } catch (Exception ex) {
      Logger.getLogger(RelayBean.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  public void turnOff(String name) {
    try {
      deviceManager.execute(name, "-");
    } catch (Exception ex) {
      Logger.getLogger(RelayBean.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
