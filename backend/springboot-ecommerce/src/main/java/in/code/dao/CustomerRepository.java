package in.code.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import in.code.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findByEmail(String email);
}
