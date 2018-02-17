/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 * (badly) modified by Connor Penrod
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    
    @FXML
    private Button btn1, btn2, btn3;
    
    private Task1 task1;
    private static ThreadState task1_state = ThreadState.STOPPED;
    private int count1 = 0;
    
    private Task2 task2;
    private static ThreadState task2_state = ThreadState.STOPPED;
    private int count2 = 0;
    
    private Task3 task3;
    private static ThreadState task3_state = ThreadState.STOPPED;
    private int count3 = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        if(count1 == 0)
        {
            System.out.println("start task 1");
            btn1.setText("End Task 1");
            if (task1 == null) {
                task1 = new Task1(214748360, 1000000, 1);
                task1.setNotificationTarget(this);
                task1.start();
                task1_state = ThreadState.RUNNING;
                count1 = 1;
            }
        }
        else
        {
            btn1.setText("Start Task 1");
            if(task1 != null)
            {
                task1_state = ThreadState.STOPPED;
                task1.end();
                count1 = 0;
            }
        }
    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        if(count2 == 0)
        {
            System.out.println("start task 2");
            btn2.setText("End Task 2");
            if (task2 == null) {
                task2 = new Task2(214748306, 1000000, 2);
                task2.setOnNotification((String message, int x, ThreadState state) -> {
                    notify(message, x, state);
                });

                task2.start();
                task2_state = ThreadState.RUNNING;
                count2 = 1;
            }
        }
        else
        {
            btn2.setText("Start Task 2");
            if(task2 != null)
            {
                task2_state = ThreadState.STOPPED;
                task2.end();
                count2 = 0;
            }
        }
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        if(count3 == 0)
        {
            System.out.println("start task 3");
            btn3.setText("End Task 3");
            if (task3 == null) {
                task3 = new Task3(2147483647, 1000000, 3);
                // this uses a property change listener to get messages
                task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                    if((String)evt.getNewValue() == "Task3 done.")
                    {
                        notify((String)evt.getNewValue(), 3, ThreadState.STOPPED);
                    }
                    else
                    {
                        textArea.appendText((String)evt.getNewValue() + "\n");
                    }
                });

                task3.start();
                task3_state = ThreadState.RUNNING;
                count3 = 1;
            }
        }
        else
        {
            btn3.setText("Start Task 3");
            if(task3 != null)
            {
                task3_state = ThreadState.STOPPED;
                task3.end();
                count3 = 0;
            }
        }
    } 
    
        
    @Override
    public void notify(String message, int id, ThreadState state) {
        if (id == 1 && state != ThreadState.RUNNING) {
            task1 = null;
            count1 = 0;
            btn1.setText("Start Task 1");
        }
        if (id == 2 && state == ThreadState.STOPPED) {
            task2 = null;
            count2 = 0;
            btn2.setText("Start Task 2");
        }
        if (id == 3 && state == ThreadState.STOPPED) {
            task3 = null;
            count3 = 0;
            btn3.setText("Start Task 3");
        }
        textArea.appendText(message + "\n");
    }
}
