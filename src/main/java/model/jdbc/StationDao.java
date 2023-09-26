package model.jdbc;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.dto.StationDto;
import model.repository.Dao;
import model.exception.RepositoryException;

public class StationDao implements Dao<Integer, StationDto> {
    private Connection connexion;

    private StationDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static StationDao getInstance() throws RepositoryException {
        return StationDaoHolder.getInstance();
    }

    @Override
    public Integer insert(StationDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        Integer id = 0;
        String sql = "INSERT INTO STATIONS(id, name) values(?, ? )";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, item.getKey());
            p.setString(2, item.getName());
            p.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return id;
    }

    @Override
    public void delete(Integer key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("No key given");
        }
        String sql = "DELETE FROM STATIONS WHERE id = ?";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, key);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(StationDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        String sql = "UPDATE STUDENTS SET name=? where id=? ";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setString(1, item.getName());
            p.setInt(2, item.getKey());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<StationDto> selectAll() throws RepositoryException {
        String sql = "SELECT id, name FROM STATIONS";
        List<StationDto> dtos = new ArrayList<>();
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StationDto dto = new StationDto(rs.getInt(1), rs.getString(2));
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dtos;
    }

    @Override
    public StationDto select(Integer key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("No key given");
        }
        String sql = "SELECT id, name FROM STATIONS WHERE  id = ?";
        StationDto dto = null;
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, key);
            ResultSet rs = p.executeQuery();

            int count = 0;
            while (rs.next()) {
                dto = new StationDto(rs.getInt(1), rs.getString(2));
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

    private static class StationDaoHolder {
        private static StationDao getInstance() throws RepositoryException {
            return new StationDao();
        }
    }
}
