package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.Entity;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.spring.JdbiUtil;

import java.io.Serializable;
import java.util.function.Function;

public abstract class AbstractRepository<T extends Entity<ID>, ID extends Serializable> implements Repository<T, ID> {
    private final Jdbi jdbi;

    public AbstractRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    protected <R> R useHandle(Function<Handle, R> function) {
        var handle = JdbiUtil.getHandle(jdbi);
        try {
            return function.apply(handle);
        } finally {
            JdbiUtil.closeIfNeeded(handle);
        }
    }
}
