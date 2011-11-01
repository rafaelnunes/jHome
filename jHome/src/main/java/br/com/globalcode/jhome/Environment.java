/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome;

import javax.ejb.Local;

/**
 *
 * @author vsenger
 */
@Local
public interface Environment {

  public void startUpdater(long interval);

  public void startUpdater();

  public int getDistance();

  public void setDistance(int distance);

  public int getLight();

  public void setLight(int light);

  public int getTemperature();

  public void setTemperature(int temperature);
}
