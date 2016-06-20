package com.github.avarabyeu.jashing.http;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by avarabyeu on 11/28/15.
 */
public class StaticsHandler implements RequestHandler {
    private final Path basePath;
    private final int basePathLength;

    public StaticsHandler(Path basePath) {
        this.basePath = basePath;
        this.basePathLength = basePath.toString().length();
    }

    @Override
    public void handle(Request request, Response response) throws IOException {
        String filename = request.getRequestUri().substring(1, request.getRequestUri().length());

        Stream<Path> s = Files.find(basePath, Integer.MAX_VALUE,
                (path, basicFileAttributes) -> {
                    String filePath = path.toString();
                    String relPath = filePath
                            .substring(basePathLength, filePath.length());
                    return relPath.endsWith(filename);
                });

        Optional<Path> file = s.findFirst();
        if (file.isPresent()) {

            String contentType = Utils.resolveMimeType(filename);
            response.contentType(contentType);
            Files.copy(file.get(), response.raw().getOutputStream());
        } else {
            response.statusCode(404);
        }
    }
}
