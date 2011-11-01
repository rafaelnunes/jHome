/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.device.impl;

import br.com.globalcode.jhome.Component;
import br.com.globalcode.jhome.Device;

import br.com.globalcode.jhome.device.DeviceUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vsenger
 */
public class ComponentBean implements Component {
  private Device device;
  String identifier, name;
  String lastValue;
  @Override
  public String execute() throws Exception {
    device.send(identifier);
    DeviceUtil.delay(50);
    String r = device.receive();
    return r;
  }
  @Override
  public String execute(String args) throws Exception {
    device.send(identifier + args);
    DeviceUtil.delay(50);
    
    String r = device.receive();
    return r;
  }
  public ComponentBean(Device device, String identifier, String name) {
    this.device=device;
    Logger.getLogger(ComponentBean.class.getName()).log(Level.INFO, 
            "Creating Component " + name + " with ID " + identifier);

    this.identifier = identifier;
    this.name = name;
  }

  @Override
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public void setLastValue(String lastValue) {
    this.lastValue = lastValue;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }
}
