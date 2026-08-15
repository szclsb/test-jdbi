package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.Entity;
import ch.szclsb.test.jdbi.model.EntityBean;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityRepository<T extends Entity<ID>, ID extends Serializable> extends AbstractRepository implements Repository<T, ID> {
    private final Class<T> tClass;

    public AbstractEntityRepository(Jdbi jdbi, Class<T> tClass) {
        super(jdbi);
        if (!tClass.isAnnotationPresent(EntityBean.class)) {
            throw new IllegalStateException(tClass + " is not annotated with @EntityBean");
        }
        this.tClass = tClass;
    }

    protected abstract Query selectAll(Handle handle);

    public List<T> findAll() {
        return useHandle(handle -> selectAll(handle)
                .mapTo(tClass)
                .collectIntoList());
    }

    protected abstract Query selectById(Handle handle, Long id);

    public Optional<T> findById(final Long id) {
        return useHandle(handle -> selectById(handle, id)
                .mapTo(tClass)
                .findOne());
    }
}
