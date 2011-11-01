/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author vsenger
 */
public interface Device {
  public String getName();
  public String getResourceString();
  public String getDescription();
  public void send(String s) throws IOException;
  public String receive() throws IOException;
  public void close() throws IOException;
  public void init();
  public boolean connected();
  public Map<String, Component> getComponents();
  
}
