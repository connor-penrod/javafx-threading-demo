/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import javafx.application.Platform;
import notifcationexamples.NotificationsUIController;
import notifcationexamples.ThreadState;

/**
 *
 * @author dalemusser
 * 
 * This example uses an object passed in with a notify()
 * method that gets called when a notification is to occur.
 * To accomplish this the Notifiable interface is needed.
 * 
 */
public class Task1 extends Thread {
    
    private int maxValue, notifyEvery, id;
    boolean exit = false;
    
    private Notifiable notificationTarget;
    private ThreadState state;
    
    
    public Task1(int maxValue, int notifyEvery, int id)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.state = ThreadState.STOPPED;
        this.id = id;
    }
    
    @Override
    public void run() {
        this.state = ThreadState.RUNNING;
                
        doNotify("Task1 start.", this.state);
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task1: " + i, this.state);
            }
            
            if (exit) {
                break;
            }
        }
        this.state = ThreadState.STOPPED;
        
        doNotify("Task1 done.", this.state);
    }
    
    public void end() {
        exit = true;
    }
    
    public void setNotificationTarget(Notifiable notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
    
    private void doNotify(String message, ThreadState threadState) {
        // this provides the notification through a method on a passed in object (notificationTarget)
        if (notificationTarget != null) {
            Platform.runLater(() -> {
                notificationTarget.notify(message, this.id, threadState);
            });
        }
    }
}
