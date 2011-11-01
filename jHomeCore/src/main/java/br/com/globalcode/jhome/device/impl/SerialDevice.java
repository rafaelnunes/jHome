/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome.device.impl;

import br.com.globalcode.jhome.Component;
import br.com.globalcode.jhome.Device;
import static br.com.globalcode.jhome.device.DeviceUtil.delay;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vsenger
 */
public class SerialDevice implements Device {

  SerialPort serialPort;
  OutputStream outputStream;
  InputStream inputStream;
  String resources;
  String name;
  String description;
  boolean connected;
  Map<String, Component> components;

  public SerialDevice(SerialPort p) {
    this.serialPort = p;
  }

  @Override
  public boolean connected() {
    return connected;
  }

  @Override
  public void close() throws IOException {
    Logger.getLogger(SerialDevice.class.getName()).log(Level.INFO,
            "Closing device on {0}", serialPort.getName());
    send("X");
    serialPort.close();
  }

  @Override
  public void init() {
    components = new Hashtable<String, Component>();
    
    try {
      outputStream = serialPort.getOutputStream();
      inputStream = serialPort.getInputStream();
      serialPort.setSerialPortParams(115200,
              SerialPort.DATABITS_8,
              SerialPort.STOPBITS_1,
              SerialPort.PARITY_NONE);
      serialPort.notifyOnOutputEmpty(true);
      Logger.getLogger(SerialDevice.class.getName()).log(Level.INFO,
              "Connection Stabilished with {0}, discovering resources...", serialPort.getName());
      discovery();
    } catch (Exception e) {
      Logger.getLogger(SerialDevice.class.getName()).log(Level.SEVERE,
              "Could not init the device on " + serialPort.getName(), e);
      serialPort.close();
    }
  } 
  
  private void discovery() throws Exception {
    //Those times are totally dependent with the kind of communication...
    delay(1500); 
    send("J");  
    delay(30);
    resources = receive();
    delay(50);    
    send("1"); 
    
    if (resources != null) {
      Logger.getLogger(SerialDevice.class.getName()).log(Level.INFO,
              "jHome Compatible device found! Resource String: {0}", resources);

      connected = true;
      try {
        StringTokenizer tokenizer = new StringTokenizer(resources, "|");
        name = tokenizer.nextToken();
        description = tokenizer.nextToken();
        int numberOfComponents = Integer.parseInt(tokenizer.nextToken());
        for (int x = 0; x < numberOfComponents; x++) {
           String identifier = tokenizer.nextToken();
           String name = tokenizer.nextToken();
           ComponentBean component=new ComponentBean(this, identifier, name);
           this.components.put(name, component);
          //this.components.add(new Co);
        }
      } catch (Exception e) {
        Logger.getLogger(SerialDevice.class.getName()).log(Level.INFO,
                "Wrong resource String. Parse error!", e);
      }
    } else {
      Logger.getLogger(SerialDevice.class.getName()).log(Level.INFO,
              "Empty Resource String - Nor a jHome device", resources);
      
    }
  }

  @Override
  public void send(String s) throws IOException {
    if (outputStream == null) {
      Logger.getLogger(SerialDevice.class.getName()).log(Level.SEVERE,
              "This device ({0}) is not working because IO objects are null. "
              + "You should restart the device!", this.getName());
    } else { 
      outputStream.write(s.getBytes());
      outputStream.flush();
    }
  }

  @Override
  public String receive() throws IOException {
    if (inputStream == null) {
      String msg = "This device (" + this.getName()
              + ") is not working because IO objects are null. "
              + "You should restart the device!";
      Logger.getLogger(SerialDevice.class.getName()).log(Level.SEVERE, msg);
      throw new IOException(msg);
    } else {
      int available = inputStream.available();
      if (available == 0) {
        return null;
      } else {
        byte chunk[] = new byte[available];
        inputStream.read(chunk, 0, available);
        String retorno = new String(chunk);
        return retorno;
      }
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getResourceString() {
    return resources;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Map<String, Component> getComponents() {
    return this.components;
  }



}
