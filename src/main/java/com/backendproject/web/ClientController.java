package com.backendproject.web;

import com.backendproject.data.ClientRepository;
import com.backendproject.entities.Client;
import com.backendproject.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {

    @Autowired
    ClientService service;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Client save(@RequestBody Client client) {
        return service.createNewClient(client);
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public Optional<Client> findUser(@PathVariable long id) {
        return service.searchClient(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<String> viewClientList() {
        List<String> clients = new ArrayList<>();
        service.listClients().forEach(client -> clients.add(client.toString()));

        return clients;
    }

    @Transactional
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable long id,
                           @RequestBody Client client) {

        service.updateClient(client, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable long id) {

        service.deleteClient(id);
    }
}
