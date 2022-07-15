package com.backendproject.web;

import com.backendproject.entities.Client;
import com.backendproject.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {

    @Autowired
    ClientService service;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Client save(@RequestBody Client client) {
        return service.saveClient(client);
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public ResponseEntity<Client> search(@PathVariable long id) {
        return service.getClientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<Client> viewList() {
        return service.getAllClients();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> update(@PathVariable long id,
                                             @RequestBody Client client) {

        return service.getClientById(id)
                .map(savedClient -> {
                    savedClient.setFirstName(client.getFirstName());
                    savedClient.setLastName(client.getLastName());
                    savedClient.setAge(client.getAge());
                    savedClient.setEmail(client.getEmail());

                    Client updatedClient = service.updateClient(savedClient);
                    return new ResponseEntity<>(updatedClient, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.deleteClient(id);

        return new ResponseEntity<>("Client deleted!.", HttpStatus.OK);
    }


}
