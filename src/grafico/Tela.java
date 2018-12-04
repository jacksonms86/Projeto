
package grafico;


import java.sql.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sistema.Conectora;
import net.proteanit.sql.DbUtils;
import sistema.Publicadora;


public class Tela extends javax.swing.JFrame {

    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Publicadora p = new Publicadora();
    
    public Tela() {
        initComponents();
        setLocationRelativeTo(null);
        c = Conectora.conectora();
        listar();
        contar();
        exibir();
        
    }
    
    public void cadastrar() throws ParseException{
        String consulta = "INSERT INTO alunos (aluno, nascimento, mae, pai, sexo, serie) VALUES (?,?,?,?,?,?);";
        
        try {
            ps = c.prepareStatement(consulta);
            ps.setString(1,campoAluno.getText());
            ps.setString(2,campoNascimento.getText());
            ps.setString(3,campoMae.getText());
            ps.setString(4,campoPai.getText());
            ps.setString(5, sexualizar());
            ps.setInt(6,(Integer.parseInt(serializar())));
            ps.execute();
            JOptionPane.showMessageDialog(null,"Cadastrado com sucesso!");
            listar();
                        
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    } 
    
    public void listar(){
        String consulta = "SELECT matricula, aluno, nascimento, sexo, ano, nivel, turno FROM matriculas ORDER BY aluno ASC;";
        
        try{
            ps =  c.prepareStatement(consulta);
            rs = ps.executeQuery();
            tabelaLista.setModel(DbUtils.resultSetToTableModel(rs));
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void pesquisar(){
        String consulta = "SELECT matricula, aluno, nascimento, sexo, ano, nivel, turno FROM matriculas WHERE aluno ILIKE ?;";
        
        try{
            ps = c.prepareStatement(consulta);
            ps.setString(1,"%"+campoPesquisa.getText()+"%");
            rs = ps.executeQuery();
            tabelaLista.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void selecionar(){
        
        int seletor = tabelaLista.getSelectedRow();
        campoMatricula.setText(tabelaLista.getModel().getValueAt(seletor,0).toString());
                
        String selecionado = campoMatricula.getText();
        String consulta = "SELECT * FROM alunos WHERE matricula = "+selecionado+";";
        
        
        try {
            ps =  c.prepareStatement(consulta);
            rs = ps.executeQuery();
            rs.next();
            
            campoAluno.setText(rs.getString("aluno"));
            campoNascimento.setText(rs.getString("nascimento"));
            campoMae.setText(rs.getString("mae"));
            campoPai.setText(rs.getString("pai"));
                        
            String sexualizado = rs.getString("sexo");
            
            if(sexualizado.equals("Feminino")){
                radioFeminino.setSelected(true);
            }else if(sexualizado.equals("Masculino")){
                radioMasculino.setSelected(true);
            }
            
            String seriado = (rs.getString("serie"));
            
            switch(seriado){
                case "0":
                    caixaSerie.setSelectedItem("Jardim III");
                    break;
                case "1":
                    caixaSerie.setSelectedItem("1º ano");
                    break;
                case "2":
                    caixaSerie.setSelectedItem("2º ano");
                    break;
                case "3":
                    caixaSerie.setSelectedItem("3º ano");
                    break;
                case "4":
                    caixaSerie.setSelectedItem("4º ano");
                    break;
                case "5":
                    caixaSerie.setSelectedItem("5º ano");
                    break;
                default:
                    caixaSerie.setSelectedItem(null);
                    
            }
            
            rs = ps.executeQuery();
            
                        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void contar(){
        int contador = 0;
        String consulta = "SELECT COUNT(*) FROM alunos;";
        
                
        try {
            ps =  c.prepareStatement(consulta);
            
            rs = ps.executeQuery();
            rs.next();
            contador = rs.getInt(1);
            String informacao = contador+" registro(s) encontrado(s).";
            rotuloQuantidade.setText(informacao);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void alterar(){
        
        String consulta = "UPDATE alunos SET aluno = ?, nascimento = ?, mae = ?, pai = ?, sexo = ?, serie = ? WHERE matricula = ?;";
        
        try{
            ps = c.prepareStatement(consulta);
            ps.setString(1, campoAluno.getText());
            ps.setString(2, campoNascimento.getText());
            ps.setString(3, campoMae.getText());
            ps.setString(4, campoPai.getText());
            
            ps.setString(5, sexualizar());
            
            ps.setInt(6,(Integer.parseInt(serializar())));
            
            ps.setInt(7, Integer.parseInt(campoMatricula.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cadastro atualizado!");
            listar();
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void atualizar(){
        
        String consulta = "UPDATE escolas SET escola = ?, cnpj = ?, endereco = ?, responsavel = ?, cargo = ?, cidade = ?, estado = ? WHERE identificacao = 1;";
        
        try{
            ps = c.prepareStatement(consulta);
            ps.setString(1, campoEscola.getText());
            ps.setString(2, campoCNPJ.getText());
            ps.setString(3, campoEndereco.getText());
            ps.setString(4, campoResponsavel.getText());
            ps.setString(5, campoCargo.getText());
            ps.setString(6, campoCidade.getText());
            ps.setString(7, campoEstado.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Informações atualizadas!");
            exibir();
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void exibir(){
        
        String consulta = "SELECT escola, cnpj, endereco, responsavel, cargo, cidade, estado FROM escolas WHERE identificacao = 1;";
        
        
        try {
            ps =  c.prepareStatement(consulta);
            rs = ps.executeQuery();
            rs.next();
            
            campoEscola.setText(rs.getString("escola"));
            campoCNPJ.setText(rs.getString("cnpj"));
            campoEndereco.setText(rs.getString("endereco"));
            campoResponsavel.setText(rs.getString("responsavel"));
            campoCargo.setText(rs.getString("cargo"));
            campoCidade.setText(rs.getString("cidade"));
            campoEstado.setText(rs.getString("estado"));
            rs = ps.executeQuery();
                        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void apagar(){
        
        String consulta = "DELETE FROM alunos WHERE matricula = ?;";
        
        try{
            ps = c.prepareStatement(consulta);
            ps.setInt(1, Integer.parseInt(campoMatricula.getText()));
            ps.execute();
            JOptionPane.showMessageDialog(null, "Cadastro excluído!");
            listar();
            limpar();
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void limpar(){
        
        campoMatricula.setText(null);
        campoAluno.setText(null);
        campoNascimento.setText(null);
        campoMae.setText(null);
        campoPai.setText("IGNORADO");
        grupoSexo.clearSelection();
        caixaSerie.setSelectedItem("Selecione");
    }
    
    public String sexualizar(){
        
        if(radioFeminino.isSelected()){
                sexualidade = "Feminino";
            } else if (radioMasculino.isSelected()){
                sexualidade = "Masculino";
            } else {
                JOptionPane.showMessageDialog(null, "Escolha o sexo.");
            }
        return sexualidade;
    }
    
    public String serializar(){
        
        serializacao = (String)caixaSerie.getSelectedItem();
        
        switch(serializacao){
            case "Jardim III":
                serializacao = "0";
                break;
            case "1º ano":
                serializacao = "1";
                break;
            case "2º ano":
                serializacao = "2";
                break;
            case "3º ano":
                //caixaSerie.setSelectedItem("3");
                serializacao = "3";
                break;
            case "4º ano":
                serializacao = "4";
                break;
            case "5º ano":
                serializacao = "5";
                break;
            default:
                JOptionPane.showMessageDialog(null, "Escolha a série.");
            
        }
        return serializacao;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoSexo = new javax.swing.ButtonGroup();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        guiasTela = new javax.swing.JTabbedPane();
        guiaListagem = new javax.swing.JPanel();
        campoPesquisa = new javax.swing.JTextField();
        rolagemTela = new javax.swing.JScrollPane();
        tabelaLista = new javax.swing.JTable();
        rotuloQuantidade = new javax.swing.JLabel();
        guiaInformacao = new javax.swing.JPanel();
        rotuloEscola = new javax.swing.JLabel();
        campoEscola = new javax.swing.JTextField();
        rotuloCNPJ = new javax.swing.JLabel();
        rotuloEndereco = new javax.swing.JLabel();
        campoEndereco = new javax.swing.JTextField();
        rotuloResponsavel = new javax.swing.JLabel();
        campoResponsavel = new javax.swing.JTextField();
        rotuloCargo = new javax.swing.JLabel();
        campoCargo = new javax.swing.JTextField();
        rotuloCidade = new javax.swing.JLabel();
        campoCidade = new javax.swing.JTextField();
        rotuloEstado = new javax.swing.JLabel();
        campoEstado = new javax.swing.JTextField();
        botaoAtualizar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        rotuloAluno = new javax.swing.JLabel();
        campoAluno = new javax.swing.JTextField();
        rotuloNascimento = new javax.swing.JLabel();
        campoNascimento = new javax.swing.JFormattedTextField();
        rotuloMae = new javax.swing.JLabel();
        campoMae = new javax.swing.JTextField();
        rotuloPai = new javax.swing.JLabel();
        campoPai = new javax.swing.JTextField();
        rotuloSexo = new javax.swing.JLabel();
        rotuloSerie = new javax.swing.JLabel();
        botaoCadastro = new javax.swing.JButton();
        botaoAlteracao = new javax.swing.JButton();
        botaoExclusao = new javax.swing.JButton();
        botaoLimpeza = new javax.swing.JToggleButton();
        botaoDeclaracao = new javax.swing.JButton();
        campoCNPJ = new javax.swing.JFormattedTextField();
        rotuloMatricula = new javax.swing.JLabel();
        campoMatricula = new javax.swing.JTextField();
        radioFeminino = new javax.swing.JRadioButton();
        radioMasculino = new javax.swing.JRadioButton();
        caixaSerie = new javax.swing.JComboBox<>();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GEDUEER");
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        campoPesquisa.setText("Pesquisar");
        campoPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoPesquisaKeyReleased(evt);
            }
        });

        tabelaLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelaLista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaListaMouseClicked(evt);
            }
        });
        rolagemTela.setViewportView(tabelaLista);

        rotuloQuantidade.setText("Nenhum registro encontrado.");
        rotuloQuantidade.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                rotuloQuantidadeAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout guiaListagemLayout = new javax.swing.GroupLayout(guiaListagem);
        guiaListagem.setLayout(guiaListagemLayout);
        guiaListagemLayout.setHorizontalGroup(
            guiaListagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guiaListagemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(guiaListagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(campoPesquisa)
                    .addComponent(rolagemTela, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                    .addGroup(guiaListagemLayout.createSequentialGroup()
                        .addComponent(rotuloQuantidade)
                        .addGap(0, 379, Short.MAX_VALUE)))
                .addContainerGap())
        );
        guiaListagemLayout.setVerticalGroup(
            guiaListagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guiaListagemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(campoPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rolagemTela, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rotuloQuantidade)
                .addContainerGap())
        );

        guiasTela.addTab("Lista", guiaListagem);

        rotuloEscola.setText("Escola");

        campoEscola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoEscolaActionPerformed(evt);
            }
        });

        rotuloCNPJ.setText("CNPJ");

        rotuloEndereco.setText("Endereço");

        campoEndereco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoEnderecoActionPerformed(evt);
            }
        });

        rotuloResponsavel.setText("Responsável");

        campoResponsavel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoResponsavelActionPerformed(evt);
            }
        });

        rotuloCargo.setText("Cargo");

        rotuloCidade.setText("Cidade");

        rotuloEstado.setText("Estado");

        campoEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoEstadoActionPerformed(evt);
            }
        });

        botaoAtualizar.setText("Atualizar");
        botaoAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAtualizarActionPerformed(evt);
            }
        });

        rotuloAluno.setText("Nome");

        rotuloNascimento.setText("Data de nascimento");

        try {
            campoNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        rotuloMae.setText("Mãe");

        campoMae.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoMaeActionPerformed(evt);
            }
        });

        rotuloPai.setText("Pai");

        campoPai.setText("IGNORADO");

        rotuloSexo.setText("Sexo");

        rotuloSerie.setText("Ano");

        botaoCadastro.setText("Cadastrar");
        botaoCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCadastroActionPerformed(evt);
            }
        });

        botaoAlteracao.setText("Alterar");
        botaoAlteracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAlteracaoActionPerformed(evt);
            }
        });

        botaoExclusao.setText("Excluir");
        botaoExclusao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExclusaoActionPerformed(evt);
            }
        });

        botaoLimpeza.setText("Limpar");
        botaoLimpeza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLimpezaActionPerformed(evt);
            }
        });

        botaoDeclaracao.setText("Gerar Declaração");
        botaoDeclaracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoDeclaracaoActionPerformed(evt);
            }
        });

        try {
            campoCNPJ.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        rotuloMatricula.setText("Matrícula");

        campoMatricula.setEnabled(false);

        grupoSexo.add(radioFeminino);
        radioFeminino.setText("Feminino");
        radioFeminino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioFemininoActionPerformed(evt);
            }
        });

        grupoSexo.add(radioMasculino);
        radioMasculino.setText("Masculino");
        radioMasculino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioMasculinoActionPerformed(evt);
            }
        });

        caixaSerie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Jardim III", "1º ano", "2º ano", "3º ano", "4º ano", "5º ano" }));

        javax.swing.GroupLayout guiaInformacaoLayout = new javax.swing.GroupLayout(guiaInformacao);
        guiaInformacao.setLayout(guiaInformacaoLayout);
        guiaInformacaoLayout.setHorizontalGroup(
            guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(guiaInformacaoLayout.createSequentialGroup()
                        .addComponent(rotuloAluno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoAluno))
                    .addGroup(guiaInformacaoLayout.createSequentialGroup()
                        .addComponent(rotuloMae)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoMae))
                    .addGroup(guiaInformacaoLayout.createSequentialGroup()
                        .addComponent(rotuloPai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoPai))
                    .addGroup(guiaInformacaoLayout.createSequentialGroup()
                        .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoAtualizar)
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloEscola)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoEscola))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloEstado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoEstado))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloCidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoCidade))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloCNPJ)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoCNPJ, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloEndereco)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoEndereco))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloResponsavel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoResponsavel))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloCargo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                        .addComponent(botaoDeclaracao))
                    .addGroup(guiaInformacaoLayout.createSequentialGroup()
                        .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloNascimento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(botaoCadastro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botaoAlteracao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botaoExclusao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botaoLimpeza))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloMatricula)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloSexo)
                                .addGap(18, 18, 18)
                                .addComponent(radioFeminino)
                                .addGap(18, 18, 18)
                                .addComponent(radioMasculino))
                            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                                .addComponent(rotuloSerie)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(caixaSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 230, Short.MAX_VALUE)))
                .addContainerGap())
        );
        guiaInformacaoLayout.setVerticalGroup(
            guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guiaInformacaoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloMatricula)
                    .addComponent(campoMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloAluno)
                    .addComponent(campoAluno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloNascimento)
                    .addComponent(campoNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloMae)
                    .addComponent(campoMae, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloPai)
                    .addComponent(campoPai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloSexo)
                    .addComponent(radioFeminino)
                    .addComponent(radioMasculino))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloSerie)
                    .addComponent(caixaSerie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botaoCadastro)
                    .addComponent(botaoAlteracao)
                    .addComponent(botaoExclusao)
                    .addComponent(botaoLimpeza))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloEscola)
                    .addComponent(campoEscola, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rotuloCNPJ)
                    .addComponent(campoCNPJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rotuloEndereco))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rotuloResponsavel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rotuloCargo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rotuloCidade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rotuloEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(guiaInformacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botaoAtualizar)
                    .addComponent(botaoDeclaracao))
                .addContainerGap())
        );

        guiasTela.addTab("Dados", guiaInformacao);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guiasTela)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guiasTela)
        );

        guiasTela.getAccessibleContext().setAccessibleName("Aluno");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoEnderecoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoEnderecoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoEnderecoActionPerformed

    private void campoEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoEstadoActionPerformed

    private void campoEscolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoEscolaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoEscolaActionPerformed

    private void campoResponsavelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoResponsavelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoResponsavelActionPerformed

    private void campoMaeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoMaeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoMaeActionPerformed

    private void botaoCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCadastroActionPerformed
        try {
            // TODO add your handling code here:
            cadastrar();
        } catch (ParseException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botaoCadastroActionPerformed

    private void rotuloQuantidadeAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_rotuloQuantidadeAncestorAdded
        // TODO add your handling code here:
        contar();
    }//GEN-LAST:event_rotuloQuantidadeAncestorAdded

    private void campoPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisaKeyReleased
        // TODO add your handling code here:
        pesquisar();
    }//GEN-LAST:event_campoPesquisaKeyReleased

    private void tabelaListaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaListaMouseClicked
        // TODO add your handling code here:
        selecionar();
    }//GEN-LAST:event_tabelaListaMouseClicked

    private void botaoAlteracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAlteracaoActionPerformed
        // TODO add your handling code here:
        alterar();
    }//GEN-LAST:event_botaoAlteracaoActionPerformed

    private void botaoAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAtualizarActionPerformed
        // TODO add your handling code here:
        atualizar();
    }//GEN-LAST:event_botaoAtualizarActionPerformed

    private void botaoExclusaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExclusaoActionPerformed
        // TODO add your handling code here:
        apagar();
    }//GEN-LAST:event_botaoExclusaoActionPerformed

    private void botaoLimpezaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimpezaActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_botaoLimpezaActionPerformed

    private void botaoDeclaracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoDeclaracaoActionPerformed
        // TODO add your handling code here:
        p.publicar(campoMatricula.getText());
    }//GEN-LAST:event_botaoDeclaracaoActionPerformed

    private void radioFemininoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioFemininoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioFemininoActionPerformed

    private void radioMasculinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioMasculinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioMasculinoActionPerformed

    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoAlteracao;
    private javax.swing.JButton botaoAtualizar;
    private javax.swing.JButton botaoCadastro;
    private javax.swing.JButton botaoDeclaracao;
    private javax.swing.JButton botaoExclusao;
    private javax.swing.JToggleButton botaoLimpeza;
    private javax.swing.JComboBox<String> caixaSerie;
    private javax.swing.JTextField campoAluno;
    private javax.swing.JFormattedTextField campoCNPJ;
    private javax.swing.JTextField campoCargo;
    private javax.swing.JTextField campoCidade;
    private javax.swing.JTextField campoEndereco;
    private javax.swing.JTextField campoEscola;
    private javax.swing.JTextField campoEstado;
    private javax.swing.JTextField campoMae;
    public static javax.swing.JTextField campoMatricula;
    private javax.swing.JFormattedTextField campoNascimento;
    private javax.swing.JTextField campoPai;
    private javax.swing.JTextField campoPesquisa;
    private javax.swing.JTextField campoResponsavel;
    private javax.swing.ButtonGroup grupoSexo;
    private javax.swing.JPanel guiaInformacao;
    private javax.swing.JPanel guiaListagem;
    private javax.swing.JTabbedPane guiasTela;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton radioFeminino;
    private javax.swing.JRadioButton radioMasculino;
    private javax.swing.JScrollPane rolagemTela;
    private javax.swing.JLabel rotuloAluno;
    private javax.swing.JLabel rotuloCNPJ;
    private javax.swing.JLabel rotuloCargo;
    private javax.swing.JLabel rotuloCidade;
    private javax.swing.JLabel rotuloEndereco;
    private javax.swing.JLabel rotuloEscola;
    private javax.swing.JLabel rotuloEstado;
    private javax.swing.JLabel rotuloMae;
    private javax.swing.JLabel rotuloMatricula;
    private javax.swing.JLabel rotuloNascimento;
    private javax.swing.JLabel rotuloPai;
    private javax.swing.JLabel rotuloQuantidade;
    private javax.swing.JLabel rotuloResponsavel;
    private javax.swing.JLabel rotuloSerie;
    private javax.swing.JLabel rotuloSexo;
    private javax.swing.JTable tabelaLista;
    // End of variables declaration//GEN-END:variables
    private String sexualidade;
    private String serializacao;
    
}
