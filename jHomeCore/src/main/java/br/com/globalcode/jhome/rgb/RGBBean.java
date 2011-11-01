/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.rgb;

import br.com.globalcode.jhome.DeviceManager;
import br.com.globalcode.jhome.RGB;
import br.com.globalcode.jhome.device.DeviceUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author vsenger
 */
@Stateless
public class RGBBean implements RGB {

  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")
  @EJB
  private DeviceManager deviceManager;
  
  public void changeColor(int red, int blue, int green) {
    try {
      //Arduino.enviar("A-B");
      deviceManager.execute("RGB", "R" + red);
      DeviceUtil.delay(50);
      deviceManager.execute("RGB", "G" + green);
      DeviceUtil.delay(50);
      deviceManager.execute("RGB", "R" + blue);
      DeviceUtil.delay(50);
    } catch (Exception ex) {
      Logger.getLogger(RGBBean.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
