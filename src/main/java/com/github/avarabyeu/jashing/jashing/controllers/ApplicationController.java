/**
 * Copyright (C) 2013 the original author or authors.
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

package com.github.avarabyeu.jashing.jashing.controllers;

import com.github.avarabyeu.jashing.jashing.subscribers.ServerSentEventHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

import java.io.IOException;


@Singleton
public class ApplicationController {


    @Inject
    private Provider<ServerSentEventHandler> serverSentEventHandler;

    public Result index() {
        return Results.html();
    }

    public Result dashboard(@PathParam("name") String name) {
        return Results.html().template("views/dashboards/" + name + ".ftl.html");
    }

    public Result widget(@PathParam("widget") String widget) {
        return Results.html().template("assets/widgets/" + widget + "/" + widget + ".html");
    }

    public Result events(final Context context) throws IOException {
        return serverSentEventHandler.get().handle(context);
    }


}
