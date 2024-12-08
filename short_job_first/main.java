package org.example;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        // Create an instance of the SJF scheduler
        SJF sjfScheduler = new SJF();
        int count,i=0;
        System.out.println("enter number of processes: ");
        Scanner scanner = new Scanner(System.in);
        count = scanner.nextInt();
        System.out.println("________________________________________________________________________");
        while(count>0){
            int burst,arrival;
            System.out.println("enter arrival time for process " + i + ": ");
            arrival= scanner.nextInt();
            System.out.println("enter burst time for process " + i + ": ");
            burst = scanner.nextInt();
            count = count-1;
            i++;
            sjfScheduler.insert_process(new Process(i,arrival,burst));
            System.out.println("________________________________________________________________________");
        }
        sjfScheduler.context_switch();
    }
}
