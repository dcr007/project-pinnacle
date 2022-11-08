package com.ondemand.tools.perflog.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 1:58 PM
 * @description
 * To develop an alert execution engine that executes alerts at the specified interval checks and sends notifications if alerts fire. Some engine basics:
 *
 *  - The alert engine is coded against an alerts client. We provide an alerts API interface and a corresponding fake client implementation below.
 *  - One alert execution cycle involves:
 *  - Making an API call (query) to query the value of the metric
 *  - Comparing the return value against Critical thresholds
 *  - Determining the state of the alert
 *  - Make a Notify API call if the alert is in CRITICAL state
 *  - Make a Resolve API call if the alert is transitioning to PASS state
 *  - Alert can be in different states based on the current value of the query and the thresholds
 *  - It is considered PASS if value <= critical threshold
 *  - It is considered CRITICAL if value > critical threshold
 *
 *   alert execution engine that:
 *  - Queries for alerts using the getAlerts API and execute them at the specified interval
 *  - Alerts will not change over time, so only need to be loaded once at start
 *  - The basic alert engine will send notifications whenever it sees a value that exceeds the critical threshold.
 *
 */
public class AnomalyEngine {

    static HashMap<Alert,Boolean> alertStateMap  = new HashMap<Alert, Boolean>();
    static Client client = new Client();
    public static void main(String[] args) {

        List<Alert> alerts = client.getAlerts();

        alerts.stream().forEach(alert ->  {
            scheduleAlert(alert,client);
        });
    }

    private  static void scheduleAlert(Alert alert,Client client) {

        // for the given alert interval
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(() -> {
                    double value = client.query("CPU");
                    if(alertStateMap.containsKey(alert)){
                        boolean alertState = checkIfValueExceedsAlertThreshold(alert,value,alertStateMap.get(alert));
                        alertStateMap.put(alert,alertState);
                    }else {
                        alertStateMap.put(alert,true);
                    }
                }
                ,0,alert.intervalSeconds, TimeUnit.SECONDS);
    }

    public static void defaultScheduleExecutor(Alert alert){
        System.out.println("Execution of Default executor is  for duration of \t"+alert.intervalSeconds);
        System.out.println(alert.name +"-- Threshold Value:\t"+ alert.criticalThreshold.value);
    }

    public  static boolean checkIfValueExceedsAlertThreshold(Alert alert,double value,boolean alertResolved) {

        System.out.println("Executing Data-point value "+value);

        if (value > alert.criticalThreshold.value) {
            client.notify(alert.name, "Data value received: "+value);
            alertResolved = false;
            return  false;
        } else {
            if (!(alertResolved)) {
                client.resolve(alert.name+ " resolved with value : "+value);
                alertResolved=true;
            }
        }
        return  alertResolved;
    }





}

class Alert {
    public final String name;
    public final String query;
    public final long intervalSeconds;
    public final Threshold criticalThreshold;

    public Alert(
            String name,
            String query,
            long intervalSeconds,
            Threshold criticalThreshold) {
        this.name = name;
        this.query = query;
        this.intervalSeconds = intervalSeconds;
        this.criticalThreshold = criticalThreshold;
    }
}

class Threshold {
    public final double value;
    public final String message;

    public Threshold(double value, String message) {
        this.value = value;
        this.message = message;
    }
}

class Client {
    static SimpleDateFormat formatter = new SimpleDateFormat("[HH:mm:ss] ");
    int index =0;
    List<Double>  listDataPoints = List.of(5.0,11.0,10.0,12.0);
    public List<Alert> getAlerts() {
        return List.of(new Alert(
                "high-CPU",
                "cpu-query",
                5 /* intervalSeconds */,
                new Threshold(10.0, "critical-CPU-alert"))
        );
    }

    public  double query(String query) {
        if(index<=listDataPoints.size()){
            return  listDataPoints.get(index++);
        }

        return 0;

    }


    public void notify(String alertName, String message) {
        log(String.format("notifying alert %s: %s", alertName, message));
    }

    public void resolve(String alertName) {
        log(String.format("resolving alert %s", alertName));
    }

    private void log(String msg) {
        String time = formatter.format(new Date());
        System.out.println(time + msg);
    }
}
