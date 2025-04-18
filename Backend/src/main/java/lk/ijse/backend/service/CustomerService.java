package lk.ijse.backend.service;

import lk.ijse.backend.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(Long id);

    Optional<Customer> getCustomerByEmail(String email);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customerDetails);

    void deleteCustomer(Long id);

}
