public class WordSorter extends Thread implements WordHandler {

    private String[] sorted = null; // Den etterhvert sorterte arrayen
    private String[] sourceStrings = null; // Kildearrayet som vi henter ordene våre fra
    private int start = 0; // Settes i konstruktør, startindex i kildearrayet
    private int end = 0; // Siste index i kildearrayet


    public WordSorter(String[] sourceList, int start, int end) {
        this.sourceStrings = sourceList;
        this.start = start;
        this.end = end;

        start();
    }


    @Override
    public void run() {
        SortedStringsList sorter = new SortedStringsList();

        for (int i = start; i < end; i++) {
            sorter.add(sourceStrings[i]);
        }

        sorted = sorter.toArray();
    }


    public String[] getWords() {
        return sorted;
    }
}

