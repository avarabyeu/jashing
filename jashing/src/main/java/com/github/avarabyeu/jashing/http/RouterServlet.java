package com.github.avarabyeu.jashing.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by avarabyeu on 11/28/15.
 */
public class RouterServlet extends HttpServlet {

    private final Router router;

    public RouterServlet(Router router) {
        this.router = router;
    }

    @Override
    protected void service(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        try {
            Request request = new Request(rq);

            Optional<RequestHandler> handler = router.getHandler(request);

            Response response = handler.isPresent() ? new Response(rs) :
                    new NotFoundResponse(rs);
            handler.get().handle(request, response);

        } catch (Exception t) {
            t.printStackTrace();
        } finally {

        }
    }
}
