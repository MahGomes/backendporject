package com.backendproject.service;

import com.backendproject.data.ClientRepository;
import com.backendproject.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repo;

    public Client saveClient(Client client) {
        return repo.save(client);
    }

    public Client updateClient(Client client) {
        return repo.save(client);
    }

    public void deleteClient(long id) {
        repo.deleteById(id);
    }

    public Iterable<Client> getAllClients() {
        return this.repo.findAll();
    }

    public Optional<Client> getClientById(long id) {
        return repo.findById(id);
    }
}
