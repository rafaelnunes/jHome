/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.jhome;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;

/**
 *
 * @author vsenger
 */
public interface DeviceManager {

  @PreDestroy
  void close();

  @PostConstruct
  void discovery();

  @Lock(value = LockType.WRITE)
  String execute(String component) throws Exception;

  @Lock(value = LockType.WRITE)
  String execute(String component, String args) throws Exception;

  Component findComponent(String id);
  
}
