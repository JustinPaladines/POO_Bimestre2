package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * CLASE: DBConnection
 * PROPÓSITO:
 * Esta clase centraliza la conexión a la base de datos (MySQL en Railway).
 * Así evito repetir el mismo código de conexión en cada formulario (Login, Register, CRUD).
 * Si mañana cambio el servidor, el usuario o la contraseña, solo lo cambio aquí.
 */
public class DBConnection {

    /*
     * Estos valores los obtuve desde Railway en MYSQL_PUBLIC_URL, MYSQLDATABASE, MYSQLUSER y MYSQLPASSWORD.
     * - HOST y PORT: son del proxy público
     * - DATABASE: es el nombre del esquema/base que Railway creó (en mi caso: railway).
     * - USER y PASSWORD: credenciales para acceder a MySQL.
     */
    private static final String HOST = "caboose.proxy.rlwy.net";
    private static final String PORT = "56651";
    private static final String DATABASE = "railway";
    private static final String USER = "root";
    private static final String PASSWORD = "CLyPVGOTKtnVXBJWCvCNofeJZQdFHfHC";

    /*
     * URL JDBC:
     * Con esto Java sabe a qué servidor conectarse y a qué base de datos entrar.
     * El formato general es: jdbc:mysql://HOST:PUERTO/BASE_DE_DATOS
     *
     * Parámetros usados:
     * - useSSL=false: desactiva SSL para evitar problemas de configuración en algunos entornos.
     * - serverTimezone=UTC: evita errores de zona horaria.
     * - allowPublicKeyRetrieval=true: evita un error común al autenticar con algunos servidores MySQL.
     */
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE  + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    /*
     * MÉTODO: getConnection()
     * Este método devuelve un objeto Connection listo para usar.
     * Lo llamo desde cualquier parte del proyecto, por ejemplo:
     * - En LoginForm para consultar si el usuario existe.
     * - En RegisterForm para insertar un nuevo usuario.
     * - En el CRUD para actualizar saldo y guardar movimientos.
     *
     * Nota: El método lanza SQLException si hay un error (credenciales, red, BD caída, etc.).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
