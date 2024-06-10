package model.repository.carros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.entity.carros.Carro;

public class PesquisaCarro {

	  private static final String URL = "jdbc:mysql://localhost:3306/veiculos";
	    private static final String USERNAME = "root";
	    private static final String PASSWORD = "root";
	    
    public static List<Carro> pesquisarCarros(Map<String, String> filtros) throws SQLException {
        List<Carro> carros = new ArrayList<>();

        
        if (filtros.isEmpty()) {
            throw new IllegalArgumentException("Pelo menos um filtro deve ser preenchido.");
        }

        validaFiltrosDeAno(filtros);
        validaFiltrosDeValor(filtros);

        StringBuilder sql = new StringBuilder("SELECT * FROM carro ");
        List<String> parametros = new ArrayList<>();

        if (filtros.containsKey("marca")) {
            sql.append("WHERE marca LIKE ? ");
            parametros.add("%" + filtros.get("marca") + "%");
        }

        if (filtros.containsKey("modelo")) {
            if (sql.length() > 13) { 
                sql.append("AND ");
            } else {
                sql.append("WHERE ");
            }
            sql.append("modelo LIKE ? ");
            parametros.add("%" + filtros.get("modelo") + "%");
        }

        if (filtros.containsKey("pais_origem")) {
            if (sql.length() > 13) { 
                sql.append("AND ");
            } else {
                sql.append("WHERE ");
            }
            sql.append("pais_origem = ? ");
            parametros.add(filtros.get("pais_origem"));
        }

        if (filtros.containsKey("ano_inicial") && filtros.containsKey("ano_final")) {
            if (sql.length() > 13) { 
                sql.append("AND ");
            } else {
                sql.append("WHERE ");
            }
            sql.append("ano BETWEEN ? AND ? ");
            parametros.add(filtros.get("ano_inicial"));
            parametros.add(filtros.get("ano_final"));
        }

        if (filtros.containsKey("valor_inicial") && filtros.containsKey("valor_final")) {
            if (sql.length() > 13) { 
                sql.append("AND ");
            } else {
                sql.append("WHERE ");
            }
            sql.append("valor BETWEEN ? AND ? ");
            parametros.add(filtros.get("valor_inicial"));
            parametros.add(filtros.get("valor_final"));
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                for (int i = 0; i < parametros.size(); i++) {
                    statement.setString(i + 1, parametros.get(i));
                }
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Carro carro = new Carro(
                    );
                    carros.add(carro);
                }
            }
        }

        return carros;
    }

    private static void validaFiltrosDeAno(Map<String, String> filtros) throws IllegalArgumentException {
        if (filtros.containsKey("ano_inicial") && !filtros.containsKey("ano_final")) {
            throw new IllegalArgumentException("Ano inicial deve ser preenchido junto com ano final.");
        } else if (!filtros.containsKey("ano_inicial") && filtros.containsKey("ano_final")) {
            throw new IllegalArgumentException("Ano final deve ser preenchido junto com ano inicial.");
        }
    }

    private static void validaFiltrosDeValor(Map<String, String> filtros) throws IllegalArgumentException {
        if (filtros.containsKey("valor_inicial") && !filtros.containsKey("valor_final")) {
            throw new IllegalArgumentException("Valor inicial deve ser preenchido junto com valor final.");
        } else if (!filtros.containsKey("valor_inicial") && filtros.containsKey("valor_final")) {
            throw new IllegalArgumentException("Valor final deve ser preenchido junto com valor inicial.");
        }
    }
}