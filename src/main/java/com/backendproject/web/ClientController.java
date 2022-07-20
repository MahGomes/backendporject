package com.backendproject.web;

import com.backendproject.entities.Address;
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

    @RequestMapping(value = "/search/{cpf}", method = RequestMethod.GET)
    public ResponseEntity<Client> search(@PathVariable String cpf) {
        return service.getClientById(cpf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<Client> viewList() {
        return service.getAllClients();
    }

    @RequestMapping(value = "/update/{cpf}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> update(@PathVariable String cpf,
                                             @RequestBody Client client) {

        return service.getClientById(cpf)
                .map(savedClient -> {
                    savedClient.setName(client.getName());
                    savedClient.setBirth_date(client.getBirth_date());
                    savedClient.setMarital_status(client.getMarital_status());

                    Address address = service.getClientById(cpf).get().getAddress();
                    address.setPostal_code(client.getAddress().getPostal_code());
                    address.setCity(client.getAddress().getCity());
                    address.setStreet(client.getAddress().getStreet());
                    address.setState(client.getAddress().getState());
                    address.setCountry(client.getAddress().getCountry());
                    address.setNumber(client.getAddress().getNumber());
                    savedClient.setAddress(address);

                    Client updatedClient = service.updateClient(savedClient);
                    return new ResponseEntity<>(updatedClient, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{cpf}")
    public ResponseEntity<String> delete(@PathVariable String cpf) {
        service.deleteClient(cpf);

        return new ResponseEntity<>("Client deleted!.", HttpStatus.OK);
    }


}
