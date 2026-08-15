package ch.szclsb.test.jdbi.app.business.repo;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.spring.JdbiUtil;

import java.util.function.Function;

public abstract class AbstractRepository {
    private final Jdbi jdbi;

    public AbstractRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    protected <R> R useHandle(Function<Handle, R> function) {
        // synchronize JTA. https://jdbi.org/#spring-jta
        var handle = JdbiUtil.getHandle(jdbi);
        try {
            return function.apply(handle);
        } finally {
            JdbiUtil.closeIfNeeded(handle);
        }
    }
}
