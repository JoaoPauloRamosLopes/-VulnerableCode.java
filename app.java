Exemplo 1 â€” Acesso a mÃ©todo de um objeto nulo
public class Exemplo1 {
    public static void main(String[] args) {
        String nome = null;

        System.out.println(nome.length());  // NullPointerException
    }
}


Motivo: a variÃ¡vel nome estÃ¡ null, entÃ£o nÃ£o existe objeto para chamar length().

Como evitar:

if (nome != null) {
    System.out.println(nome.length());
}

 Exemplo 2 â€” Atributo nÃ£o inicializado
class Pessoa {
    String nome; // nÃ£o inicializado
}

public class Exemplo2 {
    public static void main(String[] args) {
        Pessoa p = new Pessoa();
        System.out.println(p.nome.length());   // NullPointerException
    }
}


Motivo: p.nome Ã© null.

Como evitar:

p.nome = "";
System.out.println(p.nome.length());

 Exemplo 3 â€” Array com elementos nulos
public class Exemplo3 {
    public static void main(String[] args) {
        String[] lista = new String[3];
        lista[0] = "Java";
        // lista[1] e lista[2] sÃ£o null

        System.out.println(lista[1].toUpperCase()); // NullPointerException
    }
}


Como evitar:

if (lista[1] != null) {
    System.out.println(lista[1].toUpperCase());
}

 Exemplo 4 â€” Autoboxing com null
public class Exemplo4 {
    public static void main(String[] args) {
        Integer numero = null;
        int x = numero; // unboxing â†’ NullPointerException
    }
}


Como evitar:

Integer numero = null;
int x = (numero != null ? numero : 0);



/####################/

1. Exemplo de Hard-Coded (Hardcoded Credentials)

Este Ã© um erro clÃ¡ssico e extremamente comum.

 CÃ³digo vulnerÃ¡vel:
public class LoginService {
    public boolean autenticar(String usuario, String senha) {
        // hardcoded credentials (NUNCA fazer isso!)
        String userPadrao = "admin";
        String senhaPadrao = "123456";

        return usuario.equals(userPadrao) && senha.equals(senhaPadrao);
    }
}

 Por que Ã© vulnerÃ¡vel?

Segredo exposto no cÃ³digo-fonte

FÃ¡cil de vazar em GitHub

Ferramentas SAST detectam automaticamente como "Hardcoded Password"

Facilita ataques


Forma correta (boa prÃ¡tica):
public class LoginService {
    public boolean autenticar(String usuario, String senha) {

        String userPadrao = System.getenv("APP_USER");
        String senhaPadrao = System.getenv("APP_PASS");

        if (userPadrao == null || senhaPadrao == null) {
            throw new IllegalStateException("Credenciais nÃ£o configuradas.");
        }

        return usuario.equals(userPadrao) && senha.equals(senhaPadrao);
    }
}


Melhor prÃ¡tica: usar Vault (HashiCorp), AWS Secrets Manager, Azure KeyVault, GCP Secret Manager.

ðŸ’‰ 2. Exemplo de SQL Injection em Java (Statement)
 CÃ³digo vulnerÃ¡vel (SQL Injection):
public User buscarUsuario(String nome) throws Exception {
    String sql = "SELECT * FROM usuarios WHERE nome = '" + nome + "'";

    Connection conn = DriverManager.getConnection(DB_URL);
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);

    if (rs.next()) {
        return new User(rs.getInt("id"), rs.getString("nome"));
    }

    return null;
}

Como o ataque ocorre?

O atacante envia no campo â€œnomeâ€:

' OR '1'='1


A query final fica:

SELECT * FROM usuarios WHERE nome = '' OR '1'='1'


 Retorna todos os usuÃ¡rios (grave).
 Dependendo do driver, pode atÃ© permitir DROP TABLE.


 Forma correta â€” PreparedStatement (seguro)
public User buscarUsuario(String nome) throws Exception {
    String sql = "SELECT * FROM usuarios WHERE nome = ?";

    Connection conn = DriverManager.getConnection(DB_URL);
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, nome);

    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
        return new User(rs.getInt("id"), rs.getString("nome"));
    }

    return null;
}
