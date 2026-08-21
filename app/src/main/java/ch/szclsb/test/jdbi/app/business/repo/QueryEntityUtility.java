package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.EntityBean;
import ch.szclsb.test.jdbi.model.store.Book;
import lombok.Getter;
import org.jdbi.v3.core.mapper.RowMapperFactory;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.statement.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Getter
public class QueryEntityUtility<T> {
    private final Class<T> tClass;
    private final String schemaTable;
    private final List<String> columnNames;
    private final RowMapperFactory rowMapperFactory;

    public QueryEntityUtility(final Class<T> tClass) {
        if (tClass == null || !tClass.isAnnotationPresent(EntityBean.class)) {
            throw new IllegalArgumentException("class argument is null or not annotated with @EntityBean");
        }
        this.tClass = tClass;
        // todo cache created
        // todo scan annotation at compile time
        var annotation = tClass.getAnnotation(EntityBean.class);
        this.schemaTable = annotation.schemaName().isBlank()
                ? annotation.tableName()
                : String.join(".", annotation.schemaName(), annotation.tableName());
        this.columnNames = Collections.unmodifiableList(getFieldsAsColumnsNames(tClass));
        this.rowMapperFactory = BeanMapper.factory(tClass);
    }

    private static <T> List<String> getFieldsAsColumnsNames(Class<T> tClass) {
        var columnNames = new ArrayList<String>();
        scanFields(tClass, field -> {
            var columnName = camelToSnakeCase(field.getName());
            if (field.getType().isAnnotationPresent(EntityBean.class)) {
                columnName += "_id";
            }
            columnNames.add(columnName);
        });
        return columnNames;
    }

    private static void scanFields(Class<?> entityClass, Consumer<Field> scanner) {
        var currentClass = entityClass;
        while (!Objects.equals(currentClass, Object.class)) {
            for (var field : currentClass.getDeclaredFields()) {
                scanner.accept(field);
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private static String camelToSnakeCase(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append("_").append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public void defineEntity(final Query query,
                             final String tableTemplate,
                             final String columnsTemplate) {
        defineEntity(query, tableTemplate, columnsTemplate, null);
    }

    public void defineEntity(final Query query,
                             final String tableTemplate,
                             final String columnsTemplate,
                             final String prefix) {
        var prefixed = prefix != null && !prefix.isBlank();
        // cache prefixed?
        var requiredRowMapperFactory = prefixed
                ? BeanMapper.factory(tClass, prefix)
                : rowMapperFactory;
        // cache prefixed?
        var requiredColumnNames = prefixed
                ? columnNames.stream()
                .map(columnName -> "%1$s.%2$s AS %1$s_%2$s"
                        .formatted(prefix, columnName))
                .toList()
                : columnNames;
        query
                .defineList(columnsTemplate, requiredColumnNames)
                .define(tableTemplate, schemaTable)
                .registerRowMapper(requiredRowMapperFactory);
    }

    //    public static class Flow<T> {
//        private final QueryEntityUtility<T> queryEntityUtility;
//        @Getter
//        private final Query query;
//
//        private Flow(final QueryEntityUtility<T> queryEntityUtility,
//                     final Query query) {
//            this.queryEntityUtility = queryEntityUtility;
//            this.query = query;
//        }
//
//        Flow<T> define(final String tableTemplate,
//                       final String columnsTemplate) {
//            queryEntityUtility.defineEntity(query, tableTemplate, columnsTemplate);
//            return this;
//        }
//
//        Flow<T> define(final String tableTemplate,
//                       final String columnsTemplate,
//                       final String prefix) {
//            queryEntityUtility.defineEntity(query, tableTemplate, columnsTemplate, prefix);
//            return this;
//        }
//    }
//
//    public Flow<T> flow(final Query query) {
//        return new Flow<>(this, query);
//    }
}
