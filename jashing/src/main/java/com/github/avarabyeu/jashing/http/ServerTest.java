package com.github.avarabyeu.jashing.http;

import sun.nio.fs.DefaultFileSystemProvider;

import java.nio.file.FileSystems;

/**
 * Created by avarabyeu on 11/28/15.
 */
public class ServerTest {

    public static void main(String[] args) throws Exception {
        Endpoint e = new Endpoint();
        e.start(Router.builder()
                .path(Request.Method.GET, "/version", (rq, rs) -> rs.content("hello world")
                        .content("\n")
                        .content(rq.getRequestUri())
                        .content("\n")
                        .content(rq.raw().getRequestURL().toString()))
                //.statics(FileSystems.getDefault().getPath("/Users/avarabyeu/Downloads"))
                .resources("")
                .build());
    }
}
