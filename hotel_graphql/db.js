import mysql from 'mysql2';

// Créez une connexion à votre base de données MySQL
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',  // Remplacez par votre utilisateur MySQL
  password: '',  // Remplacez par votre mot de passe MySQL
  database: 'hotel',  // Remplacez par le nom de votre base de données
});

connection.connect((err) => {
  if (err) {
    console.error('Erreur de connexion à la base de données:', err.stack);
    return;
  }
  console.log('Connecté à la base de données MySQL');
});

// Exporter la connexion pour l'utiliser dans d'autres fichiers
export default connection;
