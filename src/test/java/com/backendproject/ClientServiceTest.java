package com.backendproject;

import com.backendproject.data.ClientRepository;
import com.backendproject.entities.Address;
import com.backendproject.entities.Client;
import com.backendproject.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class ClientServiceTest {

    @Mock
    private ClientRepository repo;

    @InjectMocks
    private ClientService service;

    @Test
    public void givenClientObject_whenCreateClient_thenReturnSavedClient() {
        // given
        Address address = Address.builder().build();
        Client client = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .birth_date(new Date(20001001))
                .marital_status("Single")
                .address(address)
                .build();

        String id = client.getCpf();

        // when
        when(repo.findById(id)).thenReturn(Optional.of(client));
        when(repo.save(ArgumentMatchers.any(Client.class))).thenReturn(client);
        service.saveClient(client);

        // then
        assertEquals(Optional.of(client), service.getClientById(id));
        verify(repo).save(client);
    }

    @Test
    public void givenListOfClients_whenGetAllClients_thenReturnClientList() {
        // given
        List<Client> clients = new ArrayList<>();
        Address address = Address.builder().build();
        Client client = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .birth_date(new Date(20001001))
                .marital_status("Single")
                .address(address)
                .build();

        clients.add(client);

        // when
        when(repo.findAll()).thenReturn(clients);
        Iterable<Client> expectedList = service.getAllClients();

        // then
        assertEquals(expectedList, clients);
        verify(repo).findAll();
    }

    @Test
    public void givenClientId_whenDeleteClient_shouldDeleteClient() {
        // given
        Address address = Address.builder().build();
        Client client = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .birth_date(new Date(20001001))
                .marital_status("Single")
                .address(address)
                .build();

        String id = client.getCpf();
        // when
        when(repo.findById(id)).thenReturn(Optional.empty());
        service.saveClient(client);
        service.deleteClient(id);

        // then
        assertEquals(Optional.empty(), service.getClientById(id));
        verify(repo).deleteById(id);
    }

    @Test
    public void givenUpdatedClient_whenUpdateClient_thenReturnUpdateClientObject() {
        // given
        Address address = Address.builder().build();
        Client client = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .birth_date(new Date(20001001))
                .marital_status("Single")
                .address(address)
                .build();

        Client updatedClient = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .birth_date(new Date(20001001))
                .marital_status("Single")
                .address(address)
                .build();

        String id = client.getCpf();
        //when
        when(repo.save(ArgumentMatchers.any(Client.class))).thenReturn(client);
        when(repo.findById(id)).thenReturn(Optional.of(updatedClient));
        service.saveClient(client);
        service.updateClient(updatedClient);

        // then
        assertEquals(Optional.of(updatedClient), service.getClientById(id));
        verify(repo).findById(id);
        verify(repo).save(updatedClient);
    }
}
