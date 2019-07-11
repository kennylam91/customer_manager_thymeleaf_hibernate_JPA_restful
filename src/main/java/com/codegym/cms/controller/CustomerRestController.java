package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    //-------------------Retrieve All Customers--------------------------------------------------------

    @RequestMapping(value = "/api/customers", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> listAllCustomers(){
        List<Customer> customers = (List<Customer>) customerService.findAll();
        if(customers.isEmpty()){
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);

        }

        return new ResponseEntity<List<Customer>>(customers,HttpStatus.OK);
    }

    //-------------------Retrieve Single Customer--------------------------------------------------------
    @RequestMapping(value = "/api/customers/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
        Customer customer= customerService.findById(id);
        if(customer==null){
            System.out.println("Customer with id "+id +" not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        else
        return new ResponseEntity<Customer>(customer,HttpStatus.OK);
    }

    //-------------------Create a Customer--------------------------------------------------------
    @RequestMapping(value = "/api/customers", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder uriComponentsBuilder){
        System.out.println("Creating Customer " + customer.getLastName());
        customerService.save(customer);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("/api/customers/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
    }

    //------------------- Update a Customer --------------------------------------------------------
    @RequestMapping(value = "/api/customers/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable("id") Long id){
        System.out.println("Update Customer "+id);
        Customer currentCustomer = customerService.findById(id);
        if(currentCustomer==null){
            System.out.println("Customer with id " +id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        else {
            currentCustomer.setFirstName(customer.getFirstName());
            currentCustomer.setLastName(customer.getLastName());
            customerService.save(currentCustomer);
            return new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK);
        }
    }


    //------------------- Delete a Customer --------------------------------------------------------

    @RequestMapping(value = "/api/customers/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Customer with id " + id);

        Customer customer = customerService.findById(id);
        if (customer == null) {
            System.out.println("Unable to delete. Customer with id " + id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }

        customerService.remove(id);
        return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
    }
}
