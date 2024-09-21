/**
 * Author: Le Nguyen Minh Quy
 */
package fashion.mock.repository;

import fashion.mock.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}

