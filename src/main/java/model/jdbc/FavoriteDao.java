package model.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.repository.Dao;
import model.dto.FavoriteDto;
import model.exception.RepositoryException;

public class FavoriteDao implements Dao<String, FavoriteDto> {
    private Connection connexion;

    private FavoriteDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static FavoriteDao getInstance() throws RepositoryException {
        return FavoriteDao.FavoriteDaoHolder.getInstance();
    }

    @Override
    public String insert(FavoriteDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        String nom = item.getKey();
        String sql = "INSERT INTO FAVORITE_TRIPS(nom, id_origine, id_destination) values(?, ?, ?)";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setString(1, nom);
            p.setInt(2, item.getId_origin());
            p.setInt(3, item.getId_destination());
            p.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return nom;
    }

    @Override
    public void delete(String key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("No key given");
        }
        String sql = "DELETE FROM FAVORITE_TRIPS WHERE nom = ?";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setString(1, key);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(FavoriteDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        String sql = "UPDATE FAVORITE_TRIPS SET nom=?, id_origine=?, id_destination=? where nom=?";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setString(1, item.getKey());
            p.setInt(2, item.getId_origin());
            p.setInt(3, item.getId_destination());
            p.setString(4, item.getKey());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<FavoriteDto> selectAll() throws RepositoryException {
        String sql = "SELECT nom, id_origine, id_destination FROM FAVORITE_TRIPS";
        List<FavoriteDto> dtos = new ArrayList<>();
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                FavoriteDto dto = new FavoriteDto(rs.getString(1), rs.getInt(2), rs.getInt(3));
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dtos;
    }

    @Override
    public FavoriteDto select(String key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("No key given");
        }
        String sql = "SELECT nom, id_origine, id_destination FROM FAVORITE_TRIPS WHERE nom=?";
        FavoriteDto dto = null;
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setString(1, key);

            ResultSet rs = p.executeQuery();

            int count = 0;
            while (rs.next()) {
                dto = new FavoriteDto(rs.getString(1), rs.getInt(2),  rs.getInt(3));
                count++;
            }
            if (count > 1) {
                throw new RepositoryException("Record not unique " + key);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dto;
    }

    private static class FavoriteDaoHolder {
        private static FavoriteDao getInstance() throws RepositoryException {
            return new FavoriteDao();
        }
    }
}
