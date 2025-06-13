package my.project.hotel.database.entity;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable> {
    T getId();
}

