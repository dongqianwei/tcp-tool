package com.dqw.tool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by dqw on 9/23/2014.
 */
public class Tool extends Application {

    public static void main(String[] args) {

        Thread mthd = new Thread(() -> {
            try {
                ServerSocket soc = new ServerSocket();
                soc.bind(new InetSocketAddress("127.0.0.1", 8080));
                while (!soc.isClosed()) {
                    Socket s = soc.accept();
                    Thread thd = new Thread(() -> {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            while (s.isConnected()) {
                                char[] cbuf = new char[1024];
                                int size = reader.read(cbuf);
                                if (size == -1) {
                                    s.close();
                                    return;
                                }
                                writer.write(new String(cbuf, 0, size) + System.lineSeparator());
                                writer.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    thd.setDaemon(true);
                    thd.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mthd.setDaemon(true);
        // test simple server
        // mthd.start();

        launch();
        System.exit(1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("demo.fxml"));
        Parent root = loader.load();
        ToolControllor controllor = loader.getController();
        primaryStage.setOnCloseRequest(e -> {
            controllor.dump();
        });
        controllor.init();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
