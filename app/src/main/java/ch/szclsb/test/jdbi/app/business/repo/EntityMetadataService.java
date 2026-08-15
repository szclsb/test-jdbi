package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.Entity;
import ch.szclsb.test.jdbi.model.EntityBean;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Service
public class EntityMetadataService {  // todo to config properties
    record EntityMetadata<T>(
            String schemaName,
            String tableName,
            List<String> columnNames,
            RowMapper<T> rowMapper
    ) {
        public String table() {
            return schemaName == null
                    ? tableName
                    : schemaName + "." + tableName;
        }
    }

    private final Map<Class<?>, EntityMetadata<?>> entityMetadataMap;

    public EntityMetadataService() {
        this.entityMetadataMap = new HashMap<>();

        // todo scan annotation at compile time
        var provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(EntityBean.class));
        provider.findCandidateComponents(Entity.class.getPackageName()).forEach(beanDefinition -> {
            try {
                var entityClass = Class.forName(beanDefinition.getBeanClassName());
                var annotation = entityClass.getAnnotation(EntityBean.class);
                var columnNames = new ArrayList<String>();
                scanFields(entityClass, field -> {
                    var columnName = camelToSnakeCase(field.getName());  // todo check jdbi annotation
                    if (field.getType().isAnnotationPresent(EntityBean.class)) {
                        columnName += "_id";
                    }
                    columnNames.add(columnName);
                });

                entityMetadataMap.put(entityClass, new EntityMetadata<>(
                        annotation.schemaName().isEmpty() ? null : annotation.schemaName(),
                        annotation.tableName().isEmpty() ? null : annotation.tableName(),
                        columnNames,
                        null // fixme
                ));

                log.debug("scanned entity bean {}", entityClass.getSimpleName());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not load class", e);
            }
        });
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

    public String getTableName(Class<?> entityClass) {
        var entry = entityMetadataMap.get(entityClass);
        if (entry != null) {
            return entry.table();
        }
        return null;
    }

    public List<String> getColumnNames(Class<?> entityClass) {
        return getColumnNames(entityClass, null);
    }

    public List<String> getColumnNames(Class<?> entityClass, String prefix) {
        var entry = entityMetadataMap.get(entityClass);
        if (entry != null) {
            var stream = prefix == null
                    ? entry.columnNames().stream()
                    : entry.columnNames().stream()
                    .map(columnName -> "%1$s.%2$s AS %1$s_%2$s"
                            .formatted(prefix, columnName));
            return stream.toList();
        }
        return null;
    }
}
