package com.github.avarabyeu.jashing.eventsource;

import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Set if utility method to simplify working with events-related stuff
 */
public class EventUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventUtils.class);

    /**
     * Scans whole application classpath and find events handlers. Returns result in 'event name' -> 'handler class' map
     *
     * @return
     * @throws IOException
     */
    public static Map<String, Class<?>> mapEventHandlers() throws IOException {

        /** Obtains all classpath's top level classes */
        Set<ClassPath.ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClassesRecursive("com.github.avarabyeu");
        LOGGER.info("Scanning classpath for EventHandlers.... Found {} items", classes.size());

        /* iterates over all classes, filter by HandlesEvent annotation and transforms stream to needed form */
        return classes.parallelStream().map(classInfo -> {
            try {
                return Optional.<Class<?>>of(classInfo.load());
            } catch (Throwable e) {
                return Optional.<Class<?>>empty();
            }
        }).filter(((Predicate<Optional<Class<?>>>) Optional::isPresent).and(classOptional -> classOptional.get().isAnnotationPresent(HandlesEvent.class))).map(Optional::get)
                .collect(Collectors.toMap(clazz -> clazz.getAnnotation(HandlesEvent.class).value(), clazz -> clazz));
    }
}
