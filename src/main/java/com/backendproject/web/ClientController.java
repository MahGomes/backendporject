package com.backendproject.web;

import com.backendproject.data.ClientRepository;
import com.backendproject.entities.Client;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {

    private final ClientRepository REPO;

    public ClientController(ClientRepository repo) {
        this.REPO = repo;
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Client save(@RequestBody Client client) {
        return this.REPO.save(client);
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public String findUser(@PathVariable long id) {
        Optional<Client> client = this.REPO.findById(id);

        return client.toString();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<String> viewClientList() {
        List<Client> clients = (List<Client>) this.REPO.findAll();
        List<String> clientsList = new ArrayList<>();

        clients.forEach(client -> clientsList.add(client.toString()));

        return clientsList;
    }

    @Transactional
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@PathVariable long id,
                           @RequestBody Client client) {

        this.REPO.updateClient(client.getName(), client.getAge(), client.getEmail(), id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable long id) {

        this.REPO.deleteById(id);
    }
}
