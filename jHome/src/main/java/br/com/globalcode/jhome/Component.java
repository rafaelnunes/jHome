/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome;

/**
 *
 * @author vsenger
 */
public interface Component {

  String execute() throws Exception;

  String execute(String args) throws Exception;

  void setIdentifier(String identifier);

  void setLastValue(String lastValue);

  void setName(String name);
  
}
