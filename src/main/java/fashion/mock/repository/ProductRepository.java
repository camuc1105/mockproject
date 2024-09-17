/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fashion.mock.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
