/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.device;

import br.com.globalcode.jhome.Component;
import br.com.globalcode.jhome.Device;
import br.com.globalcode.jhome.DeviceManager;
import br.com.globalcode.jhome.device.impl.SerialDevice;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 *
 * @author vsenger
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DeviceManagerBean implements DeviceManager {

  @Resource
  TimerService timerService;
  private Collection<Device> devices;

  @Override
  public Component findComponent(String id) {
    Component component = null;
    for (Device device : devices) {
      if (device.getComponents().containsKey(id)) {
        component = device.getComponents().get(id);
      }
    }
    return component;
  }

  @Lock(LockType.WRITE)
  @Override
  public String execute(String component) throws Exception {
    Component found = findComponent(component);
    if (found != null) {
      return found.execute();
    } else {
      throw new Exception("Component " + component + " not found!");
    }
  }

  @Lock(LockType.WRITE)
  @Override
  public String execute(String component, String args) throws Exception {
    Component found = findComponent(component);
    if (found != null) {
      return found.execute(args);
    } else {
      throw new Exception("Component " + component + " not found!");
    }
  }

  @PreDestroy
  @Override
  public void close() {
    for (Device device : devices) {
      try {
        device.close();
      } catch (IOException e) {
        Logger.getLogger(DeviceManagerBean.class.getName()).log(Level.INFO, "Exception while closing " + device.getName());
      } 
    }
    Collection<Timer> timers = timerService.getTimers();
    for (Timer t : timers) {
      t.cancel();
    }


  }

  @PostConstruct
  @Override
  public void discovery() {
    devices = new ArrayList<Device>();
    Enumeration portList;
    CommPortIdentifier portId;

    portList = CommPortIdentifier.getPortIdentifiers();
    Logger.getLogger(DeviceManagerBean.class.getName()).log(Level.INFO, "Starting jHome 1.0");
    SerialPort serialPort = null;
    while (portList.hasMoreElements()) {
      portId = (CommPortIdentifier) portList.nextElement();
      Logger.getLogger(DeviceManagerBean.class.getName()).log(Level.INFO,
              "Scaning port: " + portId.getName());

      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        Logger.getLogger(DeviceManagerBean.class.getName()).log(Level.INFO,
                "Serial Device Port found " + portId.getName()
                + ". Trying to discovery this device.");
        try {
          serialPort =
                  (SerialPort) portId.open(portId.getName(), 115200);
          Device device = new SerialDevice(serialPort);

          device.init();
          if (device.connected()) {
            this.devices.add(device);
          } else {
            Logger.getLogger(DeviceManagerBean.class.getName()).log(Level.INFO,
                    "Serial Device is not jHome compatible " + portId.getName());

            device.close();
            serialPort.close();
          }

        } catch (Exception e) {
          Logger.getLogger(DeviceManagerBean.class.getName()).log(Level.SEVERE,
                  "Couldn't connect to" + portId.getName());
          e.printStackTrace();
        }

      }
    }
  }
}
