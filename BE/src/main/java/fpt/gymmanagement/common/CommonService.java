package fpt.gymmanagement.common;

import java.util.Optional;

public interface CommonService<T, ID> {

    Optional<T> getById(ID id);

    T getOrElseThrow(ID id);

    T saveOrUpdate(T t, ID id);

    void deleteById(ID id);
}
