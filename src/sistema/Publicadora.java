
package sistema;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import grafico.Tela;
import javax.swing.JOptionPane;

public class Publicadora {
    
    
    
    public void publicar(String matricula) {
        
        
        String aluno = null;
        String serie = null;
        String nivel = null;
        String turno = null;
        String nascimento = null;
        String mae = null;
        String pai = null;
        String sexo = null;
        String escola = null;
        String endereco = null;
        String cnpj = null;
        String responsavel = null;
        String cargo = null;
        String cidade = null;
        String estado = null;
        
        
        String consulta = "SELECT * FROM matriculas WHERE matricula = "+matricula+";";
        
        try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projeto", "postgres", "123");
            PreparedStatement ps = c.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                
                aluno = rs.getString("aluno");
                serie = rs.getString("ano");
                nivel = rs.getString("nivel");
                turno = rs.getString("turno");
                nascimento = rs.getString("nascimento");
                mae = rs.getString("mae");
                pai = rs.getString("pai");
                sexo = rs.getString("sexo");
                escola = rs.getString("instituicao");
                endereco = rs.getString("endereco");
                cnpj = rs.getString("cnpj");
                responsavel = rs.getString("responsavel");
                cargo = rs.getString("cargo");
                cidade = rs.getString("cidade");
                estado = rs.getString("estado");
                
                
                
            }
        } catch (SQLException excecao) {
            System.out.println("Error: "+excecao);
        }
        
        Document dec = new Document(PageSize.A4);
        
         
        try{
            PdfWriter.getInstance(dec, new FileOutputStream("Declaração de Vínculo de "+aluno+".pdf"));
            
            dec.open();
            
            Paragraph cabecalho = new Paragraph(escola+"\n"+cnpj+"\n"+endereco);
            cabecalho.setAlignment(1);
             cabecalho.setSpacingAfter(30);
            dec.add( cabecalho);
            
            Paragraph titulo = new Paragraph("DECLARAÇÃO");
            titulo.setAlignment(1);
            titulo.setSpacingAfter(30);
            dec.add(titulo);
            
            
                        
            Paragraph texto = new Paragraph("Declaro para os fins que se fizerem necessários que o (a) aluno (a) "+ aluno +" está matriculado (a) e cursando regularmente o "+ serie +" do (a) "+ nivel + " neste estabelecimento de ensino no ano corrente.");
            texto.setAlignment(3);
            texto.setSpacingAfter(30);
            dec.add(texto);
                        
            Paragraph dados = new Paragraph("Data de nascimento: "+ nascimento +"\nMãe: "+ mae +"\nPai: "+ pai+"\nSexo: "+sexo );
            dados.setSpacingAfter(30);
            dec.add(dados);
                        
            Paragraph assinatura = new Paragraph("____________________________\n"+responsavel+"\n"+cargo);
            assinatura.setAlignment(1);
            assinatura.setSpacingAfter(30);
            dec.add(assinatura);
                        
            String dia;
            String mes;
            String ano;
            Format formatter;
            Date date = new Date();

            formatter = new SimpleDateFormat("dd");
            dia = formatter.format(date);
            
            formatter = new SimpleDateFormat("MMMM");
            mes = formatter.format(date);
		 		  
            formatter = new SimpleDateFormat("yyyy");
            ano = formatter.format(date);
		  
            
            
            Paragraph local = new Paragraph(cidade+"-"+estado+", "+dia+" de "+mes+" de "+ano);
            local.setAlignment(2);
            //assinatura.setSpacingAfter(30);
            dec.add(local);
                       
            dec.close();
            
            
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
        try {
            Desktop.getDesktop().open(new File("Declaração de Vínculo de "+aluno+".pdf"));
            System.out.println("PDF foi aberto!");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    
    
    }
}