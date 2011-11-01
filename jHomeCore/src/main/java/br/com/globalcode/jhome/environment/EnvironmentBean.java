/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.environment;

import br.com.globalcode.jhome.Environment;
import br.com.globalcode.jhome.Updater;
import br.com.globalcode.jhome.wallsocket.RelaySchedulerBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.inject.Named;

/**
 *
 * @author vsenger
 */
@Singleton
public class EnvironmentBean implements Environment{

  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")
  private int temperature;
  private int light;
  private int distance;
  @EJB
  Updater updater;

  @Resource
  SessionContext ctx;  
  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  public int getLight() {
    return light;
  }

  public void setLight(int light) {
    this.light = light;
  }

  public int getTemperature() {
    return temperature;
  }

  public void setTemperature(int temperature) {
    this.temperature = temperature;
  }
  @Override
  public void startUpdater() {
    startUpdater(250);
  }
  
  @Override
  public void startUpdater(long interval) {
    Logger.getLogger(RelaySchedulerBean.class.getName()).log(Level.INFO,
            "Initializing environment updater.");
    updater.startUpdater(interval);
    
  }
 
}
