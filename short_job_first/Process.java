package org.example;

public class Process {
    int pid,arrival,burst,remaining,waiting,end,turnaround,executed;
    Process(int p,int a,int b){
        pid=p;
        arrival=a;
        burst=b;
        remaining=b;
        waiting=0;
        end=0;
        turnaround=0;
        executed=0;
    }
}
