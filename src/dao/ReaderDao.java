package dao;

import entity.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderDao {
  Reader save(Reader reader);

  void removeReader(Reader reader);

  Optional<Reader> findById(long id);

  List<Reader> findAll();
}
