package com.gruppe27.fellesprosjekt.common;

public class ParticipantUser extends User {
    
    private boolean busy;

    public ParticipantUser() {
        super();
        this.busy = false;
    }

    public ParticipantUser(String username, String name, boolean busy){
        super(username, name);
        this.busy = busy;
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

}
