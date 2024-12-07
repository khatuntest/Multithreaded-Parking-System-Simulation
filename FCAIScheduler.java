import java.util.*;

class Process {
    String name;
    int arrivalTime, burstTime, priority, remainingTime, quantum, inProgress;
    double fcaiFactor;

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        inProgress = 0;
    }

    public void updateFcaiFactor(double v1, double v2) {
        fcaiFactor = (10 - priority) + (arrivalTime / v1) + (remainingTime / v2);
    }
}

public class FCAIScheduler {
    private static int completed = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.println("Enter details for Process " + (i + 1));
            System.out.print("Name: ");
            String name = scanner.next();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority: ");
            int priority = scanner.nextInt();
            System.out.print("Quantum: ");
            int quantum = scanner.nextInt();

            processes.add(new Process(name, arrivalTime, burstTime, priority, quantum));
        }

        simulateFCAIScheduling(processes);
    }

    private static void simulateFCAIScheduling(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // Sort by arrival time
        int currentTime = 0;

        double v1 = getLastArrivalTime(processes) / 10.0;
        double v2 = getMaxBurstTime(processes) / 10.0;

        Queue<Process> readyQueue = new LinkedList<>();

        // Print table header
        System.out.printf("%-10s %-10s %-15s %-20s %-20s %-10s %-15s %s%n",
                "Time", "Process", "Executed Time", "Remaining Burst Time",
                "Updated Quantum", "Priority", "FCAI Factor", "Action - Details");

        while (completed < processes.size()) {
            addRemainingProcesses(processes, readyQueue, currentTime, v1, v2);

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process currentProcess = readyQueue.poll();
            currentTime = executeProcess(currentProcess, readyQueue, processes, currentTime, v1, v2);
        }

        System.out.println("\nProcess Execution Completed!");
    }

    private static int executeProcess(Process currentProcess, Queue<Process> readyQueue, List<Process> processes, int currentTime, double v1, double v2) {
        int startTime = currentTime;
        int quantum40 = (int) Math.ceil(0.4 * currentProcess.quantum);
        int executedTime = Math.min(quantum40, currentProcess.remainingTime);

        // Execute first 40% of quantum
        currentProcess.remainingTime -= executedTime;
        currentTime += executedTime;

        addRemainingProcesses(processes, readyQueue, currentTime, v1, v2);

        // Check for preemption
        currentProcess.updateFcaiFactor(v1, v2);
        int remainingQuantum = currentProcess.quantum - executedTime;
        String actionDetails = "";

        while (remainingQuantum > 0 && currentProcess.remainingTime > 0) {
            Process mn = currentProcess;
            for (Process process : readyQueue) {
                process.updateFcaiFactor(v1, v2);
                if (process.fcaiFactor < mn.fcaiFactor) {
                    mn = process;
                }
            }
            if (mn != currentProcess) {
                currentProcess.quantum += remainingQuantum;
                readyQueue.add(currentProcess);
                readyQueue.remove(mn);

                // Print execution details before returning
                actionDetails = String.format("%s preempts %s, runs for %d units, remaining burst = %d.",
                        mn.name, currentProcess.name, executedTime, currentProcess.remainingTime);

                System.out.printf("%-10s %-10s %-15d %-20d %-20d %-10d %-15.2f %s%n",
                        startTime + "→" + currentTime, currentProcess.name, executedTime,
                        currentProcess.remainingTime, currentProcess.quantum,
                        currentProcess.priority, Math.ceil(currentProcess.fcaiFactor), actionDetails);

                return executeProcess(mn, readyQueue, processes, currentTime, v1, v2);
            }

            currentTime++;
            currentProcess.remainingTime--;
            remainingQuantum--;
            executedTime++;
            currentProcess.updateFcaiFactor(v1, v2);

            addRemainingProcesses(processes, readyQueue, currentTime, v1, v2);

            if (currentProcess.remainingTime == 0) break;
        }

        // Log action details after process execution
        if (currentProcess.remainingTime > 0) {
            currentProcess.quantum += 2;
            readyQueue.add(currentProcess);
            actionDetails = String.format("%s runs for %d units, remaining burst = %d.",
                    currentProcess.name, executedTime, currentProcess.remainingTime);

            System.out.printf("%-10s %-10s %-15d %-20d %-20d %-10d %-15.2f %s%n",
                    startTime + "→" + currentTime, currentProcess.name, executedTime,
                    currentProcess.remainingTime, currentProcess.quantum,
                    currentProcess.priority, currentProcess.fcaiFactor, actionDetails);
        } else {
            completed++;
            actionDetails = String.format("%s completes execution.", currentProcess.name);

            System.out.printf("%-10s %-10s %-15d %-20d %-20s %-10d %-15.2f %s%n",
                    startTime + "→" + currentTime, currentProcess.name, executedTime,
                    0, "Completed",
                    currentProcess.priority, Math.ceil(currentProcess.fcaiFactor), actionDetails);
        }

        return currentTime;
    }

    private static void addRemainingProcesses(List<Process> processes, Queue<Process> readyQueue, int currentTime, double v1, double v2) {
        for (Process process : processes) {
            if (process.arrivalTime <= currentTime && process.inProgress == 0) {
                process.inProgress = 1;
                process.updateFcaiFactor(v1, v2);
                readyQueue.add(process);
            }
        }
    }

    private static int getLastArrivalTime(List<Process> processes) {
        return processes.stream().mapToInt(p -> p.arrivalTime).max().orElse(0);
    }

    private static int getMaxBurstTime(List<Process> processes) {
        return processes.stream().mapToInt(p -> p.burstTime).max().orElse(0);
    }
}
