package com.backendproject.data;

import com.backendproject.entities.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface ClientRepository extends CrudRepository<Client, Long> {

}
