package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.Entity;
import ch.szclsb.test.jdbi.model.EntityBean;
import org.jdbi.v3.core.Jdbi;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityRepository<T extends Entity<ID>, ID extends Serializable> extends AbstractRepository implements Repository<T, ID> {
    private final Class<T> tClass;

    private final String directTableName;
    private final List<String> directColumnNames;

    public AbstractEntityRepository(Jdbi jdbi,
                                    EntityMetadataService entityMetadataService,
                                    Class<T> tClass) {
        super(jdbi);
        if (!tClass.isAnnotationPresent(EntityBean.class)) {
            throw new IllegalStateException(tClass + " is not annotated with @EntityBean");
        }
        this.tClass = tClass;

        this.directTableName = entityMetadataService.getTableName(tClass);
        this.directColumnNames = entityMetadataService.getColumnNames(tClass);
    }

    public List<T> findAll() {
        return useHandle(handle -> handle.createQuery("""
                            SELECT <columns>
                            FROM <table>
                        """)
                .defineList("columns", directColumnNames)
                .define("table", directTableName)
                .mapTo(tClass)
                .collectIntoList());
    }

    public Optional<T> findById(final Long id) {
        return useHandle(handle -> handle.createQuery("""
                            SELECT <columns>
                            FROM <table>
                            WHERE id = :id
                        """)
                .defineList("columns", directColumnNames)
                .define("table", directTableName)
                .bind("id", id)
                .mapTo(tClass)
                .findOne());
    }
}
