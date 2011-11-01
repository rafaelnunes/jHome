/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.environment;

import br.com.globalcode.jhome.DeviceManager;
import br.com.globalcode.jhome.Environment;
import br.com.globalcode.jhome.Updater;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 *
 * @author vsenger
 */
@Stateless
public class EnvironmentUpdater implements Updater {

  @EJB
  private DeviceManager deviceManager;
  @EJB
  private Environment environment;
  @Resource
  TimerService timerService;
  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")

  @Timeout
  private void execute(Timer timer) {
    //String command = (String) timer.getInfo();
    try {
      String sensors = deviceManager.execute("allsensors");
      StringTokenizer t = new StringTokenizer(sensors, "|");
      environment.setLight(Integer.parseInt(t.nextToken()));
      environment.setTemperature(Integer.parseInt(t.nextToken()));
    } catch (Exception ex) {
      Logger.getLogger(EnvironmentUpdater.class.getName()).log(
              Level.SEVERE,"Error updating sensors!", ex);
      /*Collection<Timer> timers = timerService.getTimers();
      for (Timer t : timers) {
        t.cancel();
      }*/
    }
  }

  @Override
  public void startUpdater(long interval) {
    Logger.getLogger(EnvironmentUpdater.class.getName()).log(Level.INFO, 
            "Initializing environment updater.");

    timerService.createTimer(0, interval, "");
  }
}
