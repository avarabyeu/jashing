/**
 * Copyright (C) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package conf;

import com.github.avarabyeu.jashing.jashing.controllers.JashingEventHandler;
import com.github.avarabyeu.jashing.jashing.LoggingSubscriberExceptionHandler;
import com.github.avarabyeu.jashing.jashing.subscribers.ServerSentEventHandler;
import com.github.avarabyeu.jashing.jashing.events.ShutdownEvent;
import com.github.avarabyeu.jashing.jashing.events.SynergyEvent;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.lifecycle.Dispose;
import ninja.lifecycle.Start;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Singleton
public class Module extends AbstractModule {


    protected void configure() {
        final EventBus eventBus = new EventBus(new LoggingSubscriberExceptionHandler("default"));
        binder().bind(EventBus.class).toInstance(eventBus);

        binder().bind(ServerSentEventHandler.class).to(JashingEventHandler.class);

        Service service = new AbstractScheduledService() {

            private Random r = new Random();

            @Override
            protected void runOneIteration() throws Exception {
                eventBus.post(new SynergyEvent((byte) r.nextInt(100)));
            }

            @Override
            protected Scheduler scheduler() {
                return Scheduler.newFixedRateSchedule(0, 5l, TimeUnit.SECONDS);
            }
        };

        ServiceManager manager = new ServiceManager(Lists.newArrayList(service));


        binder().bind(ServiceManager.class).toInstance(manager);
        binder().bind(Module.Bootstrap.class);


    }

    @Singleton
    public static class Bootstrap {

        @Inject
        private ServiceManager serviceManager;

        @Inject
        private EventBus eventBus;

        @Start(order = 10)
        public void start() {
            serviceManager.startAsync();
        }

        @Dispose(order = 10)
        public void shutdown() {
            eventBus.post(new ShutdownEvent());
            serviceManager.stopAsync();
        }

    }


}
