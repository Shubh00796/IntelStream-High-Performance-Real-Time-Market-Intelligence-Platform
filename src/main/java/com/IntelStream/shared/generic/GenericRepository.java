package com.IntelStream.shared.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GenericRepository<T, ID> extends JpaRepository<T, ID> {

    // Common query methods that can be used across all repositories
    List<T> findByActive(boolean active);

    Page<T> findByActive(boolean active, Pageable pageable);

    Optional<T> findByIdAndActive(ID id, boolean active);

    void softDelete(ID id);

    int bulkUpdateActive(List<ID> ids, boolean active);
}