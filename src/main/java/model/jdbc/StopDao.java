package model.jdbc;

import java.sql.*;
import java.util.List;
import model.dto.StopDto;
import model.dto.Composed;
import java.util.ArrayList;
import model.repository.Dao;
import model.exception.RepositoryException;

public class StopDao implements Dao<Composed, StopDto> {
    private Connection connexion;

    private StopDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static StopDao getInstance() throws RepositoryException {
        return StopDao.StopDaoHolder.getInstance();
    }

    @Override
    public Composed insert(StopDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        Composed id = item.getKey();
        String sql = "INSERT INTO STOPS(id_line, id_station, id_order) values(?, ?, ?)";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, id.getFirst());
            p.setInt(2, id.getSecond());
            p.setInt(3, item.getId_order());
            p.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return id;
    }

    @Override
    public void delete(Composed key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("No key given");
        }
        String sql = "DELETE FROM STOPS WHERE id_line = ? AND id_station = ?";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, key.getFirst());
            p.setInt(2, key.getSecond());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(StopDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        String sql = "UPDATE STOPS SET id_line=?, id_station=?, id_order=? where id_line=? AND id_station=?";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, item.getKey().getFirst());
            p.setInt(2, item.getKey().getSecond());
            p.setInt(3, item.getId_order());
            p.setInt(4, item.getKey().getFirst());
            p.setInt(5, item.getKey().getSecond());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public List<StopDto> selectByStationId(int id_station) throws RepositoryException {
        List<StopDto> stopsByStation = new ArrayList<>();
        String sql = "SELECT id_line, id_station, id_order FROM STOPS WHERE id_station=?";

        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, id_station);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                StopDto dto = new StopDto(new Composed(rs.getInt(1), rs.getInt(2)), rs.getInt(3));
                stopsByStation.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return stopsByStation;
    }

    @Override
    public List<StopDto> selectAll() throws RepositoryException {
        String sql = "SELECT id_line, id_station, id_order FROM STOPS";
        List<StopDto> dtos = new ArrayList<>();
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StopDto dto = new StopDto(new Composed(rs.getInt(1), rs.getInt(2)), rs.getInt(3));
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dtos;
    }

    @Override
    public StopDto select(Composed key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("No key given");
        }
        String sql = "SELECT id_line, id_station, id_order FROM STOPS WHERE id_line=? AND id_station=?";
        StopDto dto = null;
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, key.getFirst());
            p.setInt(2, key.getSecond());

            ResultSet rs = p.executeQuery();

            int count = 0;
            while (rs.next()) {
                dto = new StopDto(new Composed(rs.getInt(1), rs.getInt(2)), rs.getInt(3));
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

    private static class StopDaoHolder {
        private static StopDao getInstance() throws RepositoryException {
            return new StopDao();
        }
    }
}
