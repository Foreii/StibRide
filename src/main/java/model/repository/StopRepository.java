package model.repository;

import java.util.List;
import model.dto.StopDto;
import model.dto.Composed;
import model.jdbc.StopDao;
import model.exception.RepositoryException;


public class StopRepository implements Repository<Composed, StopDto> {
    private final StopDao dao;

    public StopRepository() throws RepositoryException {
        dao = StopDao.getInstance();
    }

    @Override
    public Composed add(StopDto item) throws RepositoryException {
        Composed key = item.getKey();
        if (contains(item.getKey())) {
            dao.update(item);
        } else {
            key = dao.insert(item);
        }
        return key;
    }

    @Override
    public void remove(Composed key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<StopDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    public List<StopDto> getAllByStationId(int id_station) throws RepositoryException {
        return dao.selectByStationId(id_station);
    }

    @Override
    public StopDto get(Composed key) throws RepositoryException {
        return dao.select(key);
    }

    @Override
    public boolean contains(Composed key) throws RepositoryException {
        StopDto refreshItem = dao.select(key);
        return refreshItem != null;
    }
}
