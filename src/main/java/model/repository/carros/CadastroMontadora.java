package model.repository.carros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CadastroMontadora {

    private static final String URL = "jdbc:mysql://localhost:3306/veiculos";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        String nome = "Fiat";
        String pais = "Itália";
        String presidente = "Sergio Marchionne";
        String dataFundacao = "1899-09-11";

        try {
            cadastrarMontadora(nome, pais, presidente, dataFundacao);
            System.out.println("Montadora cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void cadastrarMontadora(String nome, String pais, String presidente, String dataFundacao) {
        validarCamposObrigatorios(nome, pais, presidente, dataFundacao);

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            cadastrarMontadoraNoBanco(connection, nome, pais, presidente, dataFundacao);
        } catch (SQLException e) {
            System.err.println("Erro ao salvar montadora no banco de dados: " + e.getMessage());
        }
    }

    private static void cadastrarMontadoraNoBanco(Connection connection, String nome, String pais, String presidente, String dataFundacao) throws SQLException {
        String sql = "INSERT INTO montadora (nome, pais, nome_presidente, data_fundacao) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nome);
            statement.setString(2, pais);
            statement.setString(3, presidente);
            statement.setDate(4, java.sql.Date.valueOf(LocalDate.parse(dataFundacao, DateTimeFormatter.ISO_DATE)));
            statement.executeUpdate();
        }
    }

    private static void validarCamposObrigatorios(String nome, String pais, String presidente, String dataFundacao) {
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Nome da montadora é obrigatório.");
        }

        if (pais.isEmpty()) {
            throw new IllegalArgumentException("Nome do país da montadora é obrigatório.");
        }

        if (presidente.isEmpty()) {
            throw new IllegalArgumentException("Nome do presidente da montadora é obrigatório.");
        }

        try {
            LocalDate.parse(dataFundacao, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de fundação inválida. Formato correto: YYYY-MM-DD.");
        }
    }
}
