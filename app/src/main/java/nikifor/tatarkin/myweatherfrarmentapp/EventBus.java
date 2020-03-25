package nikifor.tatarkin.myweatherfrarmentapp;

import com.squareup.otto.Bus;

class EventBus {
    private static Bus bus;
    static Bus getBus() {
        if(bus == null) {
            bus = new Bus();
        }
        return bus;
    }
}
