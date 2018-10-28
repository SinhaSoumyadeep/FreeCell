package freecell;

public class FreecellOperationBuilderImpl implements FreecellOperationsBuilder {

  Integer cascadeSize;
  Integer openSize;


  protected FreecellOperationBuilderImpl()
  {
    openSize = 4;
    cascadeSize = 8;
  }

  @Override
  public FreecellOperationsBuilder cascades(int c) {

    cascadeSize = c;
    return this;
  }

  @Override
  public FreecellOperationsBuilder opens(int o) {
    openSize = o;
    return this;
  }

  @Override
  public <K> FreecellOperations<K> build() {
    return (FreecellOperations<K>) new FreecellModel(openSize, cascadeSize);
  }
}
