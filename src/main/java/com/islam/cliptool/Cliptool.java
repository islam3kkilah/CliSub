/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.islam.cliptool;

import com.islam.cliptool.view.ConsoleView;
import javax.swing.SwingUtilities;

/**
 *
 * @author i3akk
 */
public class Cliptool {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
           
            ConsoleView view = new ConsoleView();
            SwingUtilities.updateComponentTreeUI(view);
            view.setVisible(true);
        });

    }
}
