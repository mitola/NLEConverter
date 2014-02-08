/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bttstudio.nleconverter.utils;

import java.io.File;

/**
 *
 * @author Mitola
 */
public class FileFilter extends javax.swing.filechooser.FileFilter{
    
    @Override
    public boolean accept(File f) {
        return f.getName().endsWith(".lvl") || f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "Andengin LVL";
    }
    
}
