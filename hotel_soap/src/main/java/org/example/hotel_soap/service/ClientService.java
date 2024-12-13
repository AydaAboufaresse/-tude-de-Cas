package org.example.hotel_soap.service;

import org.example.hotel_soap.model.Client;
import org.example.hotel_soap.repository.ClientRepository; // Import your repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> getClient(long id) {
        return clientRepository.findById(id);
    }
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    public Optional<Client> updateClient(Long clientId, Client clientDetails) {
        // Fetch the client from the database (or wherever you're storing them)
        Optional<Client> clientOptional = clientRepository.findById(clientId);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            // Update client details
            client.setFirstName(clientDetails.getFirstName());
            client.setLastName(clientDetails.getLastName());
            client.setEmail(clientDetails.getEmail());
            client.setPhone(clientDetails.getPhone());

            // Save the updated client
            clientRepository.save(client);

            return Optional.of(client);
        } else {
            // Client not found
            return Optional.empty();
        }
    }


    public boolean deleteClient(long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
