package model.repository;

import java.util.List;
import model.dto.StationDto;
import model.jdbc.StationDao;
import model.exception.RepositoryException;

public class StationRepository implements Repository<Integer, StationDto> {
    private final StationDao dao;

    public StationRepository() throws RepositoryException {
        dao = StationDao.getInstance();
    }

    @Override
    public Integer add(StationDto item) throws RepositoryException {
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
    public List<StationDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public StationDto get(Integer key) throws RepositoryException {
        return dao.select(key);
    }

    @Override
    public boolean contains(Integer key) throws RepositoryException {
        StationDto refreshItem = dao.select(key);
        return refreshItem != null;
    }
}
