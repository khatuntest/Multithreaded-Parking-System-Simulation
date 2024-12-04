package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SJF {
    private final List<Process> processes;
    private int current,complete_processes;
    private void print(){
        System.out.println("PID\tArrival\tBurst\tEnd\tTurnaround\tWaiting\tExecuted order");
        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.arrival + "\t\t" + p.burst + "\t\t" +
                    p.end + "\t\t" + p.turnaround + "\t\t" + p.waiting+"\t\t"+ p.executed);
        }
        System.out.println("________________________________________________________________________");
    }
    public SJF(){
        processes=new ArrayList<>();
        current=0;
        complete_processes=0;
    }
    public void insert_process(Process p){
        processes.add(p);
    }
    public void context_switch(){
        processes.sort(Comparator.comparing(p->p.arrival));
        boolean[] finished=new boolean[processes.size()];
        int served=1;
        while(complete_processes<processes.size()){
            List <Process> readyQueue=new ArrayList<>();
            for(Process p:processes)
                if(p.arrival<=current && !finished[p.pid-1])
                    readyQueue.add(p);
            if(readyQueue.isEmpty()){
                current++;
                continue;
            }
            Process current_process= Collections.min(readyQueue, Comparator.comparingInt(p -> p.burst));
            System.out.println("Process "+current_process.pid+" is executed after waiting "+ (current-current_process.arrival)+" time from the moment its arrived");

            current+=current_process.remaining;
            current_process.end=current;
            current_process.executed=served;
            current_process.turnaround=current_process.end- current_process.arrival;
            current_process.waiting=current_process.turnaround-current_process.burst;
            finished[current_process.pid-1]=true;
            complete_processes++;
            served++;
            System.out.println("Process "+current_process.pid+" completed at "+current+" time");
            System.out.println("________________________________________________________________________");
        }
        processes.sort(Comparator.comparing(p->p.pid));
        print();
    }
}
