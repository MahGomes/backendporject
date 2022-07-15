package com.backendproject;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Client client = Client.builder()
                .firstName("Test")
                .lastName("Test")
                .email("Test@email.com")
                .age(1)
                .build();
        given(service.saveClient(any(Client.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(client.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(client.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(client.getEmail())))
                .andExpect(jsonPath("$.age",
                        is(client.getAge())));
    }

    @Test
    public void givenListOfClients_whenGetAllClients_thenReturnClientList() throws Exception{
        // given
        List<Client> listOfClients = new ArrayList<>();
        listOfClients.add(Client.builder().firstName("Test").lastName("Client").age(1).email("test@email.com").build());
        listOfClients.add(Client.builder().firstName("Client").lastName("Test").age(2).email("client@email.com").build());
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
        long id = 1L;
        Client client = Client.builder()
                .firstName("Test")
                .lastName("Client")
                .email("Test@email.com")
                .age(1)
                .build();
        given(service.getClientById(id)).willReturn(Optional.of(client));

        // when
        ResultActions response = mockMvc.perform(get("/search/{id}", id));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(client.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(client.getLastName())))
                .andExpect(jsonPath("$.email", is(client.getEmail())))
                .andExpect(jsonPath("$.age", is(client.getAge())));

    }

    @Test
    public void givenInvalidClientId_whenGetClientById_thenReturnEmpty() throws Exception{
        // given
        long id = 1L;
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
        long id = 1L;
        Client savedClient = Client.builder()
                .firstName("Test")
                .lastName("Client")
                .age(1)
                .email("test@gmail.com")
                .build();

        Client updatedClient = Client.builder()
                .firstName("Client")
                .lastName("Test")
                .age(2)
                .email("client@gmail.com")
                .build();
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
                .andExpect(jsonPath("$.firstName", is(updatedClient.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedClient.getLastName())))
                .andExpect(jsonPath("$.age", is(updatedClient.getAge())))
                .andExpect(jsonPath("$.email", is(updatedClient.getEmail())));
    }

    @Test
    public void givenClientId_whenDeleteClient_thenReturn200() throws Exception{
        // given
        long id = 1L;
        willDoNothing().given(service).deleteClient(id);

        // when
        ResultActions response = mockMvc.perform(delete("/delete/{id}", id));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
