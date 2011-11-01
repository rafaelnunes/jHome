/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.device;

/**
 *
 * @author vsenger
 */
public class DeviceUtil {

  public static void delay(long milis) {
    try {
      Thread.sleep(milis);
    } catch (InterruptedException ex) {
    }

  }
}
