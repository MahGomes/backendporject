package com.backendproject.service;

import com.backendproject.data.ClientRepository;
import com.backendproject.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository repo;

    public Client createNewClient(Client client) {
        return repo.save(client);
    }

    @Transactional
    public void updateClient(Client client, long id) {

       repo.updateClient(client.getName(), client.getAge(), client.getEmail(), id);
    }

    public void deleteClient(long id) {
        repo.deleteById(id);
    }

    public Iterable<Client> listClients() {
        return this.repo.findAll();
    }

    public Optional<Client> searchClient(long id) {

        return repo.findById(id);
    }
}
