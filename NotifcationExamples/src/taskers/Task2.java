/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.util.ArrayList;
import javafx.application.Platform;
import notifcationexamples.ThreadState;

/**
 *
 * @author dalemusser
 * 
 * This example uses a Notification functional interface.
 * This allows the use of anonymous inner classes or
 * lambda expressions to define the method that gets called
 * when a notification is to be made.
 */
public class Task2 extends Thread {
    
    private int maxValue, notifyEvery, id;
    boolean exit = false;
    
    private ArrayList<Notification> notifications = new ArrayList<>();
    private ThreadState state;
    
    public Task2(int maxValue, int notifyEvery, int id)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.state = ThreadState.STOPPED;
        this.id = id;
    }
    
    @Override
    public void run() {
        this.state = ThreadState.RUNNING;
        doNotify("Started Task2!", this.state);
        
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task2: " + i, this.state);
            }
            
            if (exit) {
                break;
            }
        }
        this.state = ThreadState.STOPPED;
        doNotify("Task2 done.", this.state);
    }
    
    public void end() {
        exit = true;
    }
    
    // this method allows a notification handler to be registered to receive notifications
    public void setOnNotification(Notification notification) {
        this.notifications.add(notification);
    }
    
    private void doNotify(String message, ThreadState threadState) {
        // this provides the notification through the registered notification handler
        for (Notification notification : notifications) {
            Platform.runLater(() -> {
                notification.handle(message, this.id, threadState);
            });
        }
    }
}