package ch.szclsb.test.jdbi.app.business.data;

import java.time.OffsetDateTime;

public record Author(
        long id,
        int version,
        OffsetDateTime createdAt,
        String createdBy,
        OffsetDateTime modifiedAt,
        String modifiedBy,
        String lastName,
        String firstName
) {
}
