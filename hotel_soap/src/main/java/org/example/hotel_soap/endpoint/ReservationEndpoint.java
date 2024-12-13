package org.example.hotel_soap.endpoint;

import org.example.hotel_soap.model.Client;
import org.example.hotel_soap.model.Reservation;
import org.example.hotel_soap.model.Room;
import org.example.hotel_soap.service.ClientService;
import org.example.hotel_soap.service.ReservationService;
import org.example.hotel_soap.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Endpoint
public class ReservationEndpoint {

    private static final String NAMESPACE_URI = "http://www.example.com/hotel-reservation";

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClientService clientService;

    private Document createNewDocument() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.newDocument();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createReservationRequest")
    @ResponsePayload
    public Element createReservation(@RequestPayload Element requestElement) throws Exception {
        Long clientId = Long.parseLong(requestElement.getElementsByTagName("clientId").item(0).getTextContent());
        Long roomId = Long.parseLong(requestElement.getElementsByTagName("roomId").item(0).getTextContent());
        LocalDate checkInDate = LocalDate.parse(requestElement.getElementsByTagName("checkInDate").item(0).getTextContent());
        LocalDate checkOutDate = LocalDate.parse(requestElement.getElementsByTagName("checkOutDate").item(0).getTextContent());

        Reservation reservation = reservationService.createReservation(clientId, roomId, checkInDate, checkOutDate);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "createReservationResponse");

        Element reservationIdElement = document.createElement("reservationId");
        reservationIdElement.setTextContent(reservation.getId().toString());
        responseElement.appendChild(reservationIdElement);

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getReservationRequest")
    @ResponsePayload
    public Element getReservation(@RequestPayload Element requestElement) throws Exception {
        Long reservationId = Long.parseLong(requestElement.getElementsByTagName("reservationId").item(0).getTextContent());
        Reservation reservation = reservationService.getReservation(reservationId);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "getReservationResponse");

        responseElement.appendChild(createElementWithText(document, "reservationId", reservation.getId().toString()));
        responseElement.appendChild(createElementWithText(document, "clientId", reservation.getClientId().toString()));
        responseElement.appendChild(createElementWithText(document, "roomId", reservation.getRoomId().toString()));
        responseElement.appendChild(createElementWithText(document, "checkInDate", reservation.getCheckInDate().toString()));
        responseElement.appendChild(createElementWithText(document, "checkOutDate", reservation.getCheckOutDate().toString()));

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateReservationRequest")
    @ResponsePayload
    public Element updateReservation(@RequestPayload Element requestElement) throws Exception {
        Long reservationId = Long.parseLong(requestElement.getElementsByTagName("reservationId").item(0).getTextContent());
        Long clientId = Long.parseLong(requestElement.getElementsByTagName("clientId").item(0).getTextContent());
        Long roomId = Long.parseLong(requestElement.getElementsByTagName("roomId").item(0).getTextContent());
        LocalDate checkInDate = LocalDate.parse(requestElement.getElementsByTagName("checkInDate").item(0).getTextContent());
        LocalDate checkOutDate = LocalDate.parse(requestElement.getElementsByTagName("checkOutDate").item(0).getTextContent());

        reservationService.updateReservation(reservationId, clientId, roomId, checkInDate, checkOutDate);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "updateReservationResponse");
        Element successElement = document.createElement("success");
        successElement.setTextContent("true");
        responseElement.appendChild(successElement);

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteReservationRequest")
    @ResponsePayload
    public Element deleteReservation(@RequestPayload Element requestElement) throws Exception {
        Long reservationId = Long.parseLong(requestElement.getElementsByTagName("reservationId").item(0).getTextContent());
        reservationService.deleteReservation(reservationId);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "deleteReservationResponse");
        Element successElement = document.createElement("success");
        successElement.setTextContent("true");
        responseElement.appendChild(successElement);

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createRoomRequest")
    @ResponsePayload
    public Element createRoom(@RequestPayload Element requestElement) throws Exception {
        String roomNumber = requestElement.getElementsByTagName("roomNumber").item(0).getTextContent();
        String roomType = requestElement.getElementsByTagName("roomType").item(0).getTextContent();
        BigDecimal price = new BigDecimal(requestElement.getElementsByTagName("price").item(0).getTextContent());

        Room room = new Room(roomNumber, roomType, price);
        Room createdRoom = roomService.createRoom(room);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "createRoomResponse");
        responseElement.appendChild(createElementWithText(document, "roomId", createdRoom.getId().toString()));

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRoomRequest")
    @ResponsePayload
    public Element getRoom(@RequestPayload Element requestElement) throws Exception {
        Long roomId = Long.parseLong(requestElement.getElementsByTagName("roomId").item(0).getTextContent());
        Room room = roomService.getRoom(roomId);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "getRoomResponse");

        responseElement.appendChild(createElementWithText(document, "roomId", room.getId().toString()));
        responseElement.appendChild(createElementWithText(document, "roomNumber", room.getRoomNumber()));
        responseElement.appendChild(createElementWithText(document, "roomType", room.getRoomType()));
        responseElement.appendChild(createElementWithText(document, "price", room.getPrice().toString()));

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createClientRequest")
    @ResponsePayload
    public Element createClient(@RequestPayload Element requestElement) throws Exception {
        String firstName = requestElement.getElementsByTagName("firstName").item(0).getTextContent();
        String lastName = requestElement.getElementsByTagName("lastName").item(0).getTextContent();
        String email = requestElement.getElementsByTagName("email").item(0).getTextContent();
        String phone = requestElement.getElementsByTagName("phone_number").item(0).getTextContent();

        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhone(phone);

        Client createdClient = clientService.createClient(client);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "createClientResponse");
        responseElement.appendChild(createElementWithText(document, "clientId", createdClient.getId().toString()));

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllRoomsRequest")
    @ResponsePayload
    public Element getAllRooms(@RequestPayload Element requestElement) throws Exception {
        List<Room> rooms = roomService.getAllRooms();

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "getAllRoomsResponse");

        for (Room room : rooms) {
            Element roomElement = document.createElement("room");
            roomElement.appendChild(createElementWithText(document, "roomId", room.getId().toString()));
            roomElement.appendChild(createElementWithText(document, "roomNumber", room.getRoomNumber()));
            roomElement.appendChild(createElementWithText(document, "roomType", room.getRoomType()));
            roomElement.appendChild(createElementWithText(document, "price", room.getPrice().toString()));
            responseElement.appendChild(roomElement);
        }

        return responseElement;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateRoomRequest")
    @ResponsePayload
    public Element updateRoom(@RequestPayload Element requestElement) throws Exception {
        Long roomId = Long.parseLong(requestElement.getElementsByTagName("roomId").item(0).getTextContent());
        String roomNumber = requestElement.getElementsByTagName("roomNumber").item(0).getTextContent();
        String roomType = requestElement.getElementsByTagName("roomType").item(0).getTextContent();
        BigDecimal price = new BigDecimal(requestElement.getElementsByTagName("price").item(0).getTextContent());

        Room roomDetails = new Room(roomNumber, roomType, price);
        Room updatedRoom = roomService.updateRoom(roomId, roomDetails);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "updateRoomResponse");

        responseElement.appendChild(createElementWithText(document, "roomId", updatedRoom.getId().toString()));
        responseElement.appendChild(createElementWithText(document, "roomNumber", updatedRoom.getRoomNumber()));
        responseElement.appendChild(createElementWithText(document, "roomType", updatedRoom.getRoomType()));
        responseElement.appendChild(createElementWithText(document, "price", updatedRoom.getPrice().toString()));

        return responseElement;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteRoomRequest")
    @ResponsePayload
    public Element deleteRoom(@RequestPayload Element requestElement) throws Exception {
        Long roomId = Long.parseLong(requestElement.getElementsByTagName("roomId").item(0).getTextContent());
        roomService.deleteRoom(roomId);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "deleteRoomResponse");

        Element successElement = document.createElement("success");
        successElement.setTextContent("true");
        responseElement.appendChild(successElement);

        return responseElement;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClientRequest")
    @ResponsePayload
    public Element getClient(@RequestPayload Element requestElement) throws Exception {
        Long clientId = Long.parseLong(requestElement.getElementsByTagName("clientId").item(0).getTextContent());

        // Unwrapping Optional<Client>
        Client client = clientService.getClient(clientId).orElseThrow(() -> new Exception("Client not found"));

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "getClientResponse");

        responseElement.appendChild(createElementWithText(document, "clientId", client.getId().toString()));
        responseElement.appendChild(createElementWithText(document, "firstName", client.getFirstName()));
        responseElement.appendChild(createElementWithText(document, "lastName", client.getLastName()));
        responseElement.appendChild(createElementWithText(document, "email", client.getEmail()));
        responseElement.appendChild(createElementWithText(document, "phone_number", client.getPhone()));

        return responseElement;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateClientRequest")
    @ResponsePayload
    public Element updateClient(@RequestPayload Element requestElement) throws Exception {
        Long clientId = Long.parseLong(requestElement.getElementsByTagName("clientId").item(0).getTextContent());
        String firstName = requestElement.getElementsByTagName("firstName").item(0).getTextContent();
        String lastName = requestElement.getElementsByTagName("lastName").item(0).getTextContent();
        String email = requestElement.getElementsByTagName("email").item(0).getTextContent();
        String phone = requestElement.getElementsByTagName("phone_number").item(0).getTextContent();

        Client clientDetails = new Client();
        clientDetails.setFirstName(firstName);
        clientDetails.setLastName(lastName);
        clientDetails.setEmail(email);
        clientDetails.setPhone(phone);

        // Unwrapping Optional<Client>
        Client updatedClient = clientService.updateClient(clientId, clientDetails)
                .orElseThrow(() -> new Exception("Client not found"));

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "updateClientResponse");

        responseElement.appendChild(createElementWithText(document, "clientId", updatedClient.getId().toString()));
        responseElement.appendChild(createElementWithText(document, "firstName", updatedClient.getFirstName()));
        responseElement.appendChild(createElementWithText(document, "lastName", updatedClient.getLastName()));
        responseElement.appendChild(createElementWithText(document, "email", updatedClient.getEmail()));
        responseElement.appendChild(createElementWithText(document, "phone_number", updatedClient.getPhone()));

        return responseElement;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteClientRequest")
    @ResponsePayload
    public Element deleteClient(@RequestPayload Element requestElement) throws Exception {
        Long clientId = Long.parseLong(requestElement.getElementsByTagName("clientId").item(0).getTextContent());

        // Unwrapping Optional<Client>
        clientService.getClient(clientId).orElseThrow(() -> new Exception("Client not found"));

        clientService.deleteClient(clientId);

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "deleteClientResponse");

        Element successElement = document.createElement("success");
        successElement.setTextContent("true");
        responseElement.appendChild(successElement);

        return responseElement;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllClientsRequest")
    @ResponsePayload
    public Element getAllClients(@RequestPayload Element requestElement) throws Exception {
        List<Client> clients = clientService.getAllClients();

        Document document = createNewDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, "getAllClientsResponse");

        for (Client client : clients) {
            Element clientElement = document.createElement("client");
            clientElement.appendChild(createElementWithText(document, "clientId", client.getId().toString()));
            clientElement.appendChild(createElementWithText(document, "firstName", client.getFirstName()));
            clientElement.appendChild(createElementWithText(document, "lastName", client.getLastName()));
            clientElement.appendChild(createElementWithText(document, "email", client.getEmail()));
            clientElement.appendChild(createElementWithText(document, "phone_number", client.getPhone()));
            responseElement.appendChild(clientElement);
        }

        return responseElement;
    }


    private Element createElementWithText(Document document, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        return element;
    }
}
