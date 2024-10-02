package org.example.demoproductservice.common.exception;

public class AtLeastOneProductRegisteredToCategory extends RuntimeException {
  public AtLeastOneProductRegisteredToCategory() {
  }

  public AtLeastOneProductRegisteredToCategory(String message) {
        super(message);
    }
}
