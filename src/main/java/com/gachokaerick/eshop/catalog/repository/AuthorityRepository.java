package com.gachokaerick.eshop.catalog.repository;

import com.gachokaerick.eshop.catalog.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
