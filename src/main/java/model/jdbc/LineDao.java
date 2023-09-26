package model.jdbc;

import java.sql.*;
import java.util.List;
import model.dto.LineDto;
import java.util.ArrayList;
import model.repository.Dao;
import model.exception.RepositoryException;

public class LineDao implements Dao<Integer, LineDto> {
    private Connection connexion;

    private LineDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static LineDao getInstance() throws RepositoryException {
        return LineDao.LineDaoHolder.getInstance();
    }

    @Override
    public Integer insert(LineDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        Integer id = 0;
        String sql = "INSERT INTO LINES(id) values(?)";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, item.getKey());
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
        String sql = "DELETE FROM LINES WHERE id = ?";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, key);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(LineDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("No item given");
        }
        String sql = "UPDATE STUDENTS SET id=? where id=? ";
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, item.getKey());
            p.setInt(2, item.getKey());
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<LineDto> selectAll() throws RepositoryException {
        String sql = "SELECT id FROM LINES";
        List<LineDto> dtos = new ArrayList<>();
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LineDto dto = new LineDto(rs.getInt(1));
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dtos;
    }

    @Override
    public LineDto select(Integer key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("No key given");
        }
        String sql = "SELECT id FROM LINES WHERE  id = ?";
        LineDto dto = null;
        try (PreparedStatement p = connexion.prepareStatement(sql)) {
            p.setInt(1, key);
            ResultSet rs = p.executeQuery();

            int count = 0;
            while (rs.next()) {
                dto = new LineDto(rs.getInt(1));
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

    private static class LineDaoHolder {
        private static LineDao getInstance() throws RepositoryException {
            return new LineDao();
        }
    }
}
