package com.xhq.nettychat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame extends Frame {

    public static final ClientFrame INSTANCE = new ClientFrame();

    private TextArea ta = new TextArea();
    private TextField tf = new TextField();

    private Client c = null;

    private ClientFrame() {
        this.setSize(300, 400);
        this.setLocation(400,20);
        this.add(ta,BorderLayout.CENTER);
        this.add(tf,BorderLayout.SOUTH);
        this.setTitle("xhq");

        tf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                c.send(tf.getText());
                tf.setText("");
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                c.closeConnection();
                System.exit(0);
            }
        });
    }

    public void connectToServer(){
        c = new Client();
        c.connect();
    }

    public static void main(String[] args) {
        ClientFrame f = ClientFrame.INSTANCE;
        f.setVisible(true);
        f.connectToServer();
    }

    public void updateText(String str) {
        ta.setText(ta.getText() + str + "\r\n");
    }
}
