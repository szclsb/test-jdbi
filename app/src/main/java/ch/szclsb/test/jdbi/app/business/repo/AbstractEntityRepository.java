package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.Entity;
import org.jdbi.v3.core.Jdbi;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityRepository<T extends Entity<ID>, ID extends Serializable> extends AbstractRepository implements Repository<T, ID> {
    protected final QueryEntityUtility<T> queryEntityUtility;

    public AbstractEntityRepository(Jdbi jdbi,
                                    Class<T> tClass) {
        super(jdbi);
        this.queryEntityUtility = new QueryEntityUtility<>(tClass);
    }

    public List<T> findAll() {
        return useHandle(handle -> handle.createQuery("""
                            SELECT <columns>
                            FROM <table>
                        """)
                .defineList("columns", queryEntityUtility.getColumnNames())
                .define("table", queryEntityUtility.getSchemaTable())
                .registerRowMapper(queryEntityUtility.getRowMapperFactory())
                .mapTo(queryEntityUtility.getTClass())
                .collectIntoList());
    }

    public Optional<T> findById(final Long id) {
        return useHandle(handle -> handle.createQuery("""
                            SELECT <columns>
                            FROM <table>
                            WHERE id = :id
                        """)
                .defineList("columns", queryEntityUtility.getColumnNames())
                .define("table", queryEntityUtility.getSchemaTable())
                .bind("id", id)
                .registerRowMapper(queryEntityUtility.getRowMapperFactory())
                .mapTo(queryEntityUtility.getTClass())
                .findOne());
    }
}
