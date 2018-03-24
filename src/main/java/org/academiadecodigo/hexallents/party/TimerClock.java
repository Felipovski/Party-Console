package org.academiadecodigo.hexallents.party;

import java.util.Timer;
import java.util.TimerTask;

public class TimerClock {

    private Timer timer;

    public TimerClock(Timer timer) {

        this.timer = timer;
    }

    public void timer() {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                System.out.println("Time is over");
            }
        };
        timer.schedule(timerTask, 10000);
    }
}



