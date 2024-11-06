package org.example;

public class car extends Thread{
    Parking_System parking_system;
    public int arrival_time,leave_time;
    public String name;
    public int Gate;
    car(String n,int a,int l,int g,Parking_System pa){
        this.name=n;
        this.arrival_time=a;
        this.leave_time=l;
        this.Gate=g;
        this.parking_system=pa;
    }

    @Override
    public void run (){
        try {
            Thread.sleep(arrival_time * 1000L);
            parking_system.park(name,arrival_time,leave_time,Gate);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
