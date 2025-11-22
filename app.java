import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.security.MessageDigest;

public class VulnerableCode {

    public static void main(String[] args) {

        // 🔓 Credenciais expostas diretamente no código (Hardcoded Secrets)
        String dbUser = "admin";
        String dbPassword = "123456";
        String url = "jdbc:mysql://localhost:3306/app";

        try {
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            Statement stmt = conn.createStatement();

            // ⚠ SQL Injection — concatenando diretamente o input
            String userInput = "admin' OR '1'='1";
            String query = "SELECT * FROM users WHERE username = '" + userInput + "'";

            System.out.println("Executando consulta: " + query); // 🔥 Exposição de informação sensível

            stmt.execute(query);

            // ⚠ Uso de algoritmos fracos (MD5)
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update("senhaInsegura".getBytes());
            byte[] hash = md.digest();

            System.out.println("Hash gerado (MD5): " + hash);

        } catch (Exception e) {  // ⚠ Captura de exceção genérica
            System.out.println("Erro detectado: " + e);  // 🔥 Vazamento de informações
        }
    }
}
