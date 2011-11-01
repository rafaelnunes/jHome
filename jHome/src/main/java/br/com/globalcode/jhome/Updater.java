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
public interface Updater {
    public void startUpdater(long interval);    
}
