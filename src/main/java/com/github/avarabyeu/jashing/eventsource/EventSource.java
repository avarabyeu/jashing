package com.github.avarabyeu.jashing.eventsource;

import com.google.common.util.concurrent.Service;

/**
 * Created by andrey.vorobyov on 25/04/14.
 */
interface EventSource<T> extends Service {
    void sendEvent(T t);
}
