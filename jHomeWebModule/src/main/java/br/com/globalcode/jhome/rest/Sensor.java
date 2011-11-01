package br.com.globalcode.jhome.rest;

import java.text.DecimalFormat;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vsenger 
 */
@XmlRootElement(name = "sensor")
public class Sensor {

  String temperature;
  String light;
  String distance;

  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

  public String getLight() {
    return light;
  }

  public void setLight(String light) {
    this.light = light;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }
  public  String getTemperatureCelsius() {
    float f = Float.parseFloat(temperature);
    double t = f  * 0.00488;
    t*=100;
    DecimalFormat dc = new DecimalFormat("##.00");
    return dc.format(t);
  }
}
