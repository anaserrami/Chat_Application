package err.anas.chatyou.dao;

import java.util.List;

public interface Dao <T,U>{
    void save(T o);
    void deleteById(U id);
    T getById(U id);
    List<T> getAll();
    void update(T o);

}
