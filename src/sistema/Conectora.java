
package sistema;

import grafico.Tela;
import java.sql.*;
import javax.swing.JOptionPane;

public class Conectora {
    
    
    public static Connection conectora(){
        
        String endereco = "jdbc:postgresql://localhost:5432/projeto";
        String usuario = "postgres";
        String senha = "123";
        
        try {
               
            Connection conexao = DriverManager.getConnection(endereco, usuario, senha);
            return conexao;
            
        } catch (SQLException excecao) {
            JOptionPane.showMessageDialog(null, "Erro: "+excecao);
            return null;
        }
    }
    
    
    
    //criar método que retorna os dados da entidade escola para a guia correspondente
    public static String apresentar(){
        String dado = null;
        String consulta = "SELECT * FROM alunos WHERE matricula = 1;";
        
        try (Connection c = conectora();
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery(consulta)){
            
            rs.next();
            String id = rs.getString("matricula");
            String name = rs.getString("aluno");
            String bday = rs.getString("nascimento");
            
            dado = id+"\n"+name+"\n"+bday;
            
        }catch (SQLException e){
            System.out.println(e.getMessage());
            
        }
        
        return dado;
    }
  
    
    
}


