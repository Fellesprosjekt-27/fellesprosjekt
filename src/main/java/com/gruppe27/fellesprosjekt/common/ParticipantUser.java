package com.gruppe27.fellesprosjekt.common;

import com.gruppe27.fellesprosjekt.common.Event.Status;

public class ParticipantUser extends User {

    private boolean busy;
    //Status is defined in the Event class.
    private Status status;

    public ParticipantUser() {
        super();
        this.busy = false;
        status = Status.MAYBE;
    }

    public ParticipantUser(String username, String name, boolean busy) {
        super(username, name);
        this.busy = busy;
        status = Status.MAYBE;
    }

    @Override
    public String toString() {

        return "User{" +
                "groups=" + this.getGroups() +
                ", username='" + this.getUsername() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", busy='" + busy + '\'' +
                '}';

    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Status getParticipantStatus() {
        return status;
    }
    
    public void setParticipantStatus(Status status) {
        this.status = status;
    }
    
    public void setParticipantStatus(String string){
        if(string.equals("MAYBE"))
            status = Status.MAYBE;
        else if(string.equals("ATTENDING"))
            status = Status.ATTENDING;
        else if(string.equals("NOT_ATTENDING"))
            status = Status.NOT_ATTENDING;
        else
            status = null;
    }
}
