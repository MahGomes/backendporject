package com.backendproject.data;

import com.backendproject.entities.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Modifying
    @Query("update Client c set c.name = ?1, c.age = ?2, c.email = ?3 where c.id = ?4")
    void updateClient(String name, int age, String email, long id);
}
