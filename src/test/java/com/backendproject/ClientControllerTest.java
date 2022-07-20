package com.backendproject;

import com.backendproject.entities.Address;
import com.backendproject.entities.Client;
import com.backendproject.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenClientObject_whenCreateClient_thenReturnSavedClient() throws Exception {
        // given
        Address address = Address.builder()
                .postal_code("2211100")
                .city("City")
                .street("Street")
                .state("State")
                .country("Country")
                .number(1)
                .build();
        Client client = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .marital_status("Single")
                .address(address)
                .build();
        given(service.saveClient(any(Client.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)));

        // then
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.cpf", is(client.getCpf())))
                .andExpect(jsonPath("$.name", is(client.getName())))
                .andExpect(jsonPath("$.marital_status", is(client.getMarital_status())))
                .andExpect(jsonPath("$.address.postal_code", is(address.getPostal_code())))
                .andExpect(jsonPath("$.address.street", is(address.getStreet())))
                .andExpect(jsonPath("$.address.city", is(address.getCity())))
                .andExpect(jsonPath("$.address.state", is(address.getState())))
                .andExpect(jsonPath("$.address.country", is(address.getCountry())))
                .andExpect(jsonPath("$.address.number", is(address.getNumber())));

    }

    @Test
    public void givenListOfClients_whenGetAllClients_thenReturnClientList() throws Exception{
        // given
        List<Client> listOfClients = new ArrayList<>();
        Address address = Address.builder().build();
        listOfClients.add(Client.builder().cpf("11122233344").name("Test").marital_status("Single").address(address).build());
        listOfClients.add(Client.builder().cpf("11122233355").name("Client").marital_status("Single").address(address).build());
        given(service.getAllClients()).willReturn(listOfClients);

        // when
        ResultActions response = mockMvc.perform(get("/list"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfClients.size())));

    }

    @Test
    public void givenClient_whenGetClientById_thenReturnClientObject() throws Exception{
        // given
        Address address = Address.builder().build();
        Client client = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .marital_status("Single")
                .address(address)
                .build();
        String id = client.getCpf();
        given(service.getClientById(id)).willReturn(Optional.of(client));

        // when
        ResultActions response = mockMvc.perform(get("/search/{id}", id));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.cpf", is(client.getCpf())))
                .andExpect(jsonPath("$.name", is(client.getName())))
                .andExpect(jsonPath("$.marital_status", is(client.getMarital_status())))
                .andExpect(jsonPath("$.address.postal_code", is(address.getPostal_code())))
                .andExpect(jsonPath("$.address.street", is(address.getStreet())))
                .andExpect(jsonPath("$.address.city", is(address.getCity())))
                .andExpect(jsonPath("$.address.state", is(address.getState())))
                .andExpect(jsonPath("$.address.country", is(address.getCountry())))
                .andExpect(jsonPath("$.address.number", is(address.getNumber())));

    }

    @Test
    public void givenInvalidClientId_whenGetClientById_thenReturnEmpty() throws Exception{
        // given
        String id = "11122233344";
        given(service.getClientById(id)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/search/{id}", id));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedClient_whenUpdateClient_thenReturnUpdateClientObject() throws Exception{

        // given
        Address address = Address.builder()
                .postal_code("22111000")
                .city("City")
                .street("Street")
                .state("State")
                .country("Country")
                .number(1)
                .build();
        Client savedClient = Client.builder()
                .cpf("11122233344")
                .name("Test")
                .birth_date(new Date(20001001))
                .marital_status("Single")
                .address(address)
                .build();

        Address updatedAddress = Address.builder()
                .postal_code("33222111")
                .city("City c")
                .street("Street s")
                .state("State s")
                .country("Country c")
                .number(2)
                .build();
        Client updatedClient = Client.builder()
                .cpf("11122233344")
                .name("Client")
                .marital_status("Married")
                .address(updatedAddress)
                .build();

        String id = savedClient.getCpf();
        given(service.getClientById(id)).willReturn(Optional.of(savedClient));
        given(service.updateClient(any(Client.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedClient)));


        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.cpf", is(updatedClient.getCpf())))
                .andExpect(jsonPath("$.name", is(updatedClient.getName())))
                .andExpect(jsonPath("$.marital_status", is(updatedClient.getMarital_status())))
                .andExpect(jsonPath("$.address.postal_code", is(address.getPostal_code())))
                .andExpect(jsonPath("$.address.street", is(address.getStreet())))
                .andExpect(jsonPath("$.address.city", is(address.getCity())))
                .andExpect(jsonPath("$.address.state", is(address.getState())))
                .andExpect(jsonPath("$.address.country", is(address.getCountry())))
                .andExpect(jsonPath("$.address.number", is(address.getNumber())));
    }

    @Test
    public void givenClientId_whenDeleteClient_thenReturn200() throws Exception{
        // given
        String id = "11122233344";
        willDoNothing().given(service).deleteClient(id);

        // when
        ResultActions response = mockMvc.perform(delete("/delete/{id}", id));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
