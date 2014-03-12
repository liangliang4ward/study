import java.util.*;    
    
public class SimpleLRU {    
    
  private static final int MAX_ENTRIES = 50;    
    
  private Map mCache = new LinkedHashMap(MAX_ENTRIES, .75F, true) {    
    protected boolean removeEldestEntry(Map.Entry eldest) {    
      return size() > MAX_ENTRIES;    
    }    
  };    
    
  public SimpleLRU() {    
    for(int i = 0; i < 100; i++) {    
      String numberStr = String.valueOf(i);    
      mCache.put(numberStr, numberStr);    
      System.out.print("\rSize = " + mCache.size() + "\tCurrent value = " + i + "\tLast Value in cache = " + mCache.get(numberStr));    
      try {    
        Thread.sleep(10);    
      } catch(InterruptedException ex) {    
      }  
      System.out.println(mCache.get("1"));
    }    
    
    System.out.println("");   
    System.out.println(mCache.get("1"));

  }    
    
  public static void main(String[] args) {    
    new SimpleLRU();    
  }    
}   