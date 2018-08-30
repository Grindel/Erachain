import api.ApiClient;
import controller.Controller;
import core.BlockChain;
import gui.AboutFrame;
import gui.Gui;
import gui.library.Issue_Confirm_Dialog;
import lang.Lang;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import settings.Settings;
import utils.SysTray;
import webserver.Status;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

// 30/03

public class Start {


    public static boolean backUP = false;
    static Logger LOGGER = Logger.getLogger(Start.class.getName());
    private static AboutFrame about_frame;
    private static String info;

    public static void main(String args[]) throws IOException {


        ////
        File log4j = new File("log4j.properties");
        if (log4j.exists()) {
            PropertyConfigurator.configure(log4j.getAbsolutePath());
        } else {
            try (InputStream resourceAsStream = ClassLoader.class.getResourceAsStream("/log4j/log4j.default");) {
                PropertyConfigurator.configure(resourceAsStream);
                LOGGER.error("log4j.properties not found, search path is " + log4j.getAbsolutePath() + " using default!");
            }
        }

        boolean cli = false;

        String pass = null;

        for (String arg : args) {
            if (arg.equals("-cli")) {
                cli = true;
            }
            // backUP data
            if (arg.equals("-backup")) {
                backUP = true;


            } else {
                if (arg.equals("-nogui")) {

                    Controller.useGui = false;

                }

                if (arg.startsWith("-pass=") && arg.length() > 6) {
                    pass = arg.substring(6);
                }

                if (arg.startsWith("-peers=") && arg.length() > 7) {
                    Settings.getInstance().setDefaultPeers(arg.substring(7).split(","));
                }

                if (arg.equals("-testnet")) {
                    Settings.getInstance().setGenesisStamp(System.currentTimeMillis());
                } else if (arg.startsWith("-testnet=") && arg.length() > 9) {
                    try {
                        long testnetstamp = Long.parseLong(arg.substring(9));

                        if (testnetstamp == 0) {
                            testnetstamp = System.currentTimeMillis();
                        }

                        Settings.getInstance().setGenesisStamp(testnetstamp);
                    } catch (Exception e) {
                        Settings.getInstance().setGenesisStamp(BlockChain.DEFAULT_MAINNET_STAMP);
                    }
                }
            }
        }

        if (Controller.useGui) {
            about_frame = AboutFrame.getInstance();
            about_frame.setUserClose(false);
        }
        if (!cli) {
            try {

                ///fff = Controller.getInstance(Settings.getInstance().isGuiEnabled(), Settings.getInstance().isGuiDynamic());

                //ONE MUST BE ENABLED
                if (!Settings.getInstance().isGuiEnabled() && !Settings.getInstance().isRpcEnabled()) {
                    throw new Exception(Lang.getInstance().translate("Both gui and rpc cannot be disabled!"));
                }

                info = Lang.getInstance().translate("Starting %app% / version: %version% / build date: %builddate% / ...")
                        .replace("%app%", Lang.getInstance().translate(controller.Controller.APP_NAME))
                        .replace("%version%", Controller.getVersion())
                        .replace("%builddate%", Controller.getBuildDateString());
                LOGGER.info(info
                );
                if (Controller.useGui) about_frame.set_console_Text(info);


                //STARTING NETWORK/BLOCKCHAIN/RPC
                Controller.getInstance().start();
                //unlock wallet

                if (pass != null && Controller.getInstance().doesWalletDatabaseExists()) {
                    if (Controller.getInstance().unlockWallet(pass))
                        Controller.getInstance().lockWallet();
                }

                Status.getinstance();

                if (!Controller.useGui) {
                    LOGGER.info("-nogui used");
                } else {

                    try {
                        Thread.sleep(100);

                        //START GUI

                        if (Gui.getInstance() != null && Settings.getInstance().isSysTrayEnabled()) {

                            SysTray.getInstance().createTrayIcon();
                            about_frame.setVisible(false);
                            about_frame.getInstance().dispose();
                        }
                    } catch (Exception e1) {
                        if (Controller.useGui) about_frame.setVisible(false);
                        if (Controller.useGui) about_frame.dispose();
                        LOGGER.error(Lang.getInstance().translate("GUI ERROR - at Start"), e1);
                    }
                }

                //Controller.getInstance().isTestnet();


            } catch (Exception e) {

                LOGGER.error(e.getMessage(), e);
                // show error dialog
                if (Controller.useGui) {
                    if (Settings.getInstance().isGuiEnabled()) {
                        Issue_Confirm_Dialog dd = new Issue_Confirm_Dialog(null, true, null,
                                Lang.getInstance().translate("STARTUP ERROR") + ": " + e.getMessage(), 600, 400, Lang.getInstance().translate(" "));
                        dd.jButton1.setVisible(false);
                        dd.jButton2.setText(Lang.getInstance().translate("Cancel"));
                        dd.setLocationRelativeTo(null);
                        dd.setVisible(true);
                    }
                }

                //USE SYSTEM STYLE
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e2) {
                    LOGGER.error(e2.getMessage(), e2);
                }

                //ERROR STARTING
                LOGGER.error(Lang.getInstance().translate("STARTUP ERROR") + ": " + e.getMessage());

                if (Gui.isGuiStarted()) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), Lang.getInstance().translate("Startup Error"), JOptionPane.ERROR_MESSAGE);


                }


                if (Controller.useGui) about_frame.setVisible(false);
                if (Controller.useGui) about_frame.dispose();
                //FORCE SHUTDOWN
                System.exit(0);
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            ApiClient client = new ApiClient();

            while (true) {

                System.out.print("[COMMAND] ");
                String command = scanner.nextLine();

                if (command.equals("quit")) {

                    if (Controller.useGui) about_frame.setVisible(false);
                    if (Controller.useGui) about_frame.dispose();
                    scanner.close();
                    System.exit(0);
                }

                String result = client.executeCommand(command);
                LOGGER.info("[RESULT] " + result);
            }
        }
    }

    public static String getManifestInfo() throws IOException {
        Enumeration<URL> resources = Thread.currentThread()
                .getContextClassLoader()
                .getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            try {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                Attributes attributes = manifest.getMainAttributes();
                String implementationTitle = attributes.getValue("Implementation-Title");
                if (implementationTitle != null) { // && implementationTitle.equals(applicationName))
                    String implementationVersion = attributes.getValue("Implementation-Version");
                    String buildTime = attributes.getValue("Build-Time");
                    return implementationVersion + " build " + buildTime;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return "Current Version";
    }
}
