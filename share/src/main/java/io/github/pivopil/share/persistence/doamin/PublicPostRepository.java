package io.github.pivopil.share.persistence.doamin;

import io.github.pivopil.share.entities.impl.domain.PublicPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 19.10.16.
 */
@Repository
public interface PublicPostRepository extends CrudRepository<PublicPost, Long> {
    // PublicPostService
}