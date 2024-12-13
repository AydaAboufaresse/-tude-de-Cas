import db from './db.js';  // Utilisez `import` pour importer la connexion à MySQL

const resolvers = {
  Query: {
    // Réservation
    getReservation: (parent, { id }) => {
      return new Promise((resolve, reject) => {
        db.query('SELECT * FROM reservations WHERE id = ?', [id], (err, results) => {
          if (err) reject(err);
          resolve(results[0]);
        });
      });
    },
    getReservations: () => {
      return new Promise((resolve, reject) => {
        db.query('SELECT * FROM reservations', (err, results) => {
          if (err) reject(err);
          resolve(results);
        });
      });
    },

    // Client
    getClient: (parent, { id }) => {
      return new Promise((resolve, reject) => {
        db.query('SELECT * FROM clients WHERE id = ?', [id], (err, results) => {
          if (err) reject(err);
          resolve(results[0]);
        });
      });
    },
    getClients: () => {
      return new Promise((resolve, reject) => {
        db.query('SELECT * FROM clients', (err, results) => {
          if (err) reject(err);
          resolve(results);
        });
      });
    },

    // Chambre
    getRoom: (parent, { id }) => {
      return new Promise((resolve, reject) => {
        db.query('SELECT * FROM rooms WHERE id = ?', [id], (err, results) => {
          if (err) reject(err);
          resolve(results[0]);
        });
      });
    },
    getRooms: () => {
      return new Promise((resolve, reject) => {
        db.query('SELECT * FROM rooms', (err, results) => {
          if (err) reject(err);
          resolve(results);
        });
      });
    },
  },

  Mutation: {
    // Réservation
    createReservation: (parent, { clientId, roomId, checkInDate, checkOutDate }) => {
      return new Promise((resolve, reject) => {
        db.query(
          'INSERT INTO reservations (client_id, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?)',
          [clientId, roomId, checkInDate, checkOutDate],
          (err, results) => {
            if (err) reject(err);
            resolve({
              id: results.insertId,
              clientId,
              roomId,
              checkInDate,
              checkOutDate,
            });
          }
        );
      });
    },
    updateReservation: (parent, { id, checkInDate, checkOutDate }) => {
      return new Promise((resolve, reject) => {
        const updates = [];
        const values = [];

        if (checkInDate) {
          updates.push('check_in_date = ?');
          values.push(checkInDate);
        }
        if (checkOutDate) {
          updates.push('check_out_date = ?');
          values.push(checkOutDate);
        }

        if (updates.length > 0) {
          db.query(
            `UPDATE reservations SET ${updates.join(', ')} WHERE id = ?`,
            [...values, id],
            (err) => {
              if (err) reject(err);
              resolve({
                id,
                checkInDate,
                checkOutDate,
              });
            }
          );
        } else {
          reject('No updates provided');
        }
      });
    },
    deleteReservation: (parent, { id }) => {
      return new Promise((resolve, reject) => {
        db.query('DELETE FROM reservations WHERE id = ?', [id], (err, results) => {
          if (err) reject(err);
          resolve(results.affectedRows > 0);
        });
      });
    },

    // Client
    createClient: (parent, { firstName, lastName, email, phoneNumber }) => {
      return new Promise((resolve, reject) => {
        db.query(
          'INSERT INTO clients (first_name, last_name, email, phone_number) VALUES (?, ?, ?, ?)',
          [firstName, lastName, email, phoneNumber],
          (err, results) => {
            if (err) reject(err);
            resolve({
              id: results.insertId,
              firstName,
              lastName,
              email,
              phoneNumber,
            });
          }
        );
      });
    },
    updateClient: (parent, { id, firstName, lastName, email, phoneNumber }) => {
      return new Promise((resolve, reject) => {
        const updates = [];
        const values = [];

        if (firstName) {
          updates.push('first_name = ?');
          values.push(firstName);
        }
        if (lastName) {
          updates.push('last_name = ?');
          values.push(lastName);
        }
        if (email) {
          updates.push('email = ?');
          values.push(email);
        }
        if (phoneNumber) {
          updates.push('phone_number = ?');
          values.push(phoneNumber);
        }

        if (updates.length > 0) {
          db.query(
            `UPDATE clients SET ${updates.join(', ')} WHERE id = ?`,
            [...values, id],
            (err) => {
              if (err) reject(err);
              resolve({
                id,
                firstName,
                lastName,
                email,
                phoneNumber,
              });
            }
          );
        } else {
          reject('No updates provided');
        }
      });
    },
    deleteClient: (parent, { id }) => {
      return new Promise((resolve, reject) => {
        db.query('DELETE FROM clients WHERE id = ?', [id], (err, results) => {
          if (err) reject(err);
          resolve(results.affectedRows > 0);
        });
      });
    },

    // Room
    createRoom: (parent, { roomNumber, roomType, price }) => {
      return new Promise((resolve, reject) => {
        db.query(
          'INSERT INTO rooms (room_number, room_type, price) VALUES (?, ?, ?)',
          [roomNumber, roomType, price],
          (err, results) => {
            if (err) reject(err);
            resolve({
              id: results.insertId,
              roomNumber,
              roomType,
              price,
            });
          }
        );
      });
    },
    updateRoom: (parent, { id, roomNumber, roomType, price }) => {
      return new Promise((resolve, reject) => {
        const updates = [];
        const values = [];

        if (roomNumber) {
          updates.push('room_number = ?');
          values.push(roomNumber);
        }
        if (roomType) {
          updates.push('room_type = ?');
          values.push(roomType);
        }
        if (price) {
          updates.push('price = ?');
          values.push(price);
        }

        if (updates.length > 0) {
          db.query(
            `UPDATE rooms SET ${updates.join(', ')} WHERE id = ?`,
            [...values, id],
            (err) => {
              if (err) reject(err);
              resolve({
                id,
                roomNumber,
                roomType,
                price,
              });
            }
          );
        } else {
          reject('No updates provided');
        }
      });
    },
    deleteRoom: (parent, { id }) => {
      return new Promise((resolve, reject) => {
        db.query('DELETE FROM rooms WHERE id = ?', [id], (err, results) => {
          if (err) reject(err);
          resolve(results.affectedRows > 0);
        });
      });
    },
  },
};

// Exporter les résolveurs
export default resolvers;
