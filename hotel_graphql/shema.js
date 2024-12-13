import { gql } from 'apollo-server';

const typeDefs = gql`
  type Client {
  id: ID!
  firstName: String
  lastName: String
  email: String
  phoneNumber: String
  }


  type Room {
    id: ID!
    roomNumber: String
    roomType: String
    price: Float
  }

  type Reservation {
    id: ID!
    client: Client
    room: Room
    checkInDate: String
    checkOutDate: String
  }

  type Query {
    getReservation(id: ID!): Reservation
    getReservations: [Reservation]
    getClient(id: ID!): Client
    getClients: [Client]
    getRoom(id: ID!): Room
    getRooms: [Room]
  }

  type Mutation {
    # Reservations
    createReservation(clientId: ID!, roomId: ID!, checkInDate: String!, checkOutDate: String!): Reservation
    updateReservation(id: ID!, checkInDate: String, checkOutDate: String): Reservation
    deleteReservation(id: ID!): Boolean

    # Clients
    createClient(firstName: String!, lastName: String!, email: String!, phoneNumber: String!): Client
    updateClient(id: ID!, firstName: String, lastName: String, email: String, phoneNumber: String): Client
    deleteClient(id: ID!): Boolean

    # Rooms
    createRoom(roomNumber: String!, roomType: String!, price: Float!): Room
    updateRoom(id: ID!, roomNumber: String, roomType: String, price: Float): Room
    deleteRoom(id: ID!): Boolean
  }
`;

export default typeDefs;
