package dao;

import entity.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDaoImpl implements ReaderDao {
  private final List<Reader> readers = new ArrayList<>();

  public ReaderDaoImpl() {
    save(new Reader("Ivan"));
    save(new Reader("Yevhenii"));
    save(new Reader("Andrii"));
  }

  @Override
  public Reader save(Reader readerToSave) {
    readers.add(readerToSave);
    return readerToSave;
  }

  @Override
  public Optional<Reader> findById(long id) {
    return readers.stream().filter(reader -> reader.getId() == id).findFirst();
  }

  @Override
  public List<Reader> findAll() {
    return readers;
  }
}
