package model.repository;

import java.util.List;
import model.dto.LineDto;
import model.jdbc.LineDao;
import model.exception.RepositoryException;

public class LineRepository implements Repository<Integer, LineDto> {
    private final LineDao dao;

    public LineRepository() throws RepositoryException {
        dao = LineDao.getInstance();
    }

    @Override
    public Integer add(LineDto item) throws RepositoryException {
        Integer key = item.getKey();
        if (contains(item.getKey())) {
            dao.update(item);
        } else {
            key = dao.insert(item);
        }
        return key;
    }

    @Override
    public void remove(Integer key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<LineDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public LineDto get(Integer key) throws RepositoryException {
        return dao.select(key);
    }

    @Override
    public boolean contains(Integer key) throws RepositoryException {
        LineDto refreshItem = dao.select(key);
        return refreshItem != null;
    }
}
