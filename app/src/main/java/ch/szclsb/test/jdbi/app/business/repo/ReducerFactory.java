package ch.szclsb.test.jdbi.app.business.repo;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class ReducerFactory {
    static String idColumnName(String prefix) {
        return prefix + "_id";
    }

    public record Reducer<R, ID, E>(String idColumnName, Class<ID> idClass, Class<E> entityClass, BiConsumer<R, E> setterFunction) {
        public BiConsumer<R, RowView> asConsumer() {
            return (r, rowView) -> {
                if (rowView.getColumn(idColumnName, idClass) != null) {
                    setterFunction.accept(r, rowView.getRow(entityClass));
                }
            };
        }

        public static <R, E> Reducer<R, Long, E> ofId(Class<E> entityClass, String prefix, BiConsumer<R, E> setterFunction) {
            return new Reducer<>(ReducerFactory.idColumnName(prefix), Long.class, entityClass, setterFunction);
        }
    }

    public static <ID, E> LinkedHashMapRowReducer<ID, E> rowReducer(String rootIdColumnName, Class<ID> rootIdClass, Class<E> rootEntityClass,
                                                                    Reducer<E, ?, ?> ...entityReducers) {
        var entityFunction = Arrays.stream(entityReducers)
                .map(Reducer::asConsumer)
                .reduce(BiConsumer::andThen);
        return (container, rowView) -> {
            var root = container.computeIfAbsent(rowView.getColumn(rootIdColumnName, rootIdClass),
                    id -> rowView.getRow(rootEntityClass));
            entityFunction.ifPresent(f -> f.accept(root, rowView));
        };
    }

    public static <E> LinkedHashMapRowReducer<Long, E> rowIdReducer(Class<E> rootEntityClass, String rootPrefix,
                                                                    Reducer<E, Long, ?> ...entityReducers) {
        return rowReducer(ReducerFactory.idColumnName(rootPrefix), Long.class, rootEntityClass, entityReducers);
    }

}
