package Enums;

public enum TextComparison {

  EQUALS {
    @Override
    public boolean match(String haystack, String needle) {
      return haystack.equals(needle);
    }
  },
  CASE_INSENSITIVE_EQUALS {
    @Override
    public boolean match(String haystack, String needle) {
      return haystack.equalsIgnoreCase(needle);
    }
  },
  CONTAINS {
    @Override
    public boolean match(String haystack, String needle) {
      return haystack.contains(needle);
    }
  };

  public abstract boolean match(String haystack, String needle);
}
