package simpleml.struct;

/**
 * @author rasmikun
 * @author sitfoxfly
 */
public final class IndexedValue {

  public final int index;
  public final double value;

  public IndexedValue(int index, double value) {
    this.index = index;
    this.value = value;
  }

  public int getIndex() {
    return index;
  }

  public double getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    IndexedValue that = (IndexedValue) o;

    return index == that.index && Double.compare(that.value, value) == 0;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = index;
    temp = value != 0.0d ? Double.doubleToLongBits(value) : 0L;
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "{" + index + ": " + value + "}";
  }
}
