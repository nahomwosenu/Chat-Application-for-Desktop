/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.application.by.nahom;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
/**
 *
 * @author Nahom
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button sendButton;
    @FXML private Button connectButton;
    @FXML private Button createButton;
    @FXML private Button clearButton;
    @FXML 
    private TextArea messageArea;
    @FXML
    private TextField messageField;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    public void connect(){
        try{
            connection=new Socket("127.0.0.1",2020);
            messageArea.setText("Connected to: "+connection.getInetAddress().getHostAddress());
            recieveMessage();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void recieve(){
        try{
            do{
            input=new ObjectInputStream(connection.getInputStream());
                String message=(String)input.readObject();
                messageArea.appendText("\n"+message);
            }while(input!=null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void send(String message){
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.writeObject(message);
            output.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void sendVideo(File file){
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.writeObject(file);
            output.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void clearMessageArea(ActionEvent e){
        messageArea.clear();
    }
    Runnable reciever=new Runnable(){
      public void run(){
          recieve();
      }  
    };
    public void recieveMessage(){
        Thread t=new Thread(reciever);
        t.start();
    }
    public void sendMessage(ActionEvent e){
        String message=messageField.getText();
        send(message);
    }
    public void startServer(){
        try{
        ServerSocket server=new ServerSocket(2020);
        while(true){
          messageArea.appendText("\nSERVER WAITING FOR NEW USER TO CONNECT\n");
          connection=server.accept();
          messageArea.appendText("\nNow Connected to: "+connection.getInetAddress().getHostName());
          recieveMessage();
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    Runnable serverRun=new Runnable(){
      public void run(){
          startServer();
      }  
    };
    public void runServer(){
        Thread t=new Thread(serverRun);
        t.setName("ServerThread");
        t.start();
    }
}
