package com.material.fs.filesystem;

import java.util.Optional;


public interface Traversable {
  Optional<File> findChild();
}
