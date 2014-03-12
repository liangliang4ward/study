public class Interleaver extends Thread implements WordHandler {

    private String[] list1 = null;
    private String[] list2 = null;
    private String[] sorted = null;


    public Interleaver(String[] list1, String[] list2) {
        this.list1 = list1;
        this.list2 = list2;

        start();
    }


    @Override
    public void run() {
        sorted = new String[list1.length + list2.length];
        int list1Pos = 0;
        int list2Pos = 0;

        for (int i = 0; i < sorted.length; i++) {
            if (list1Pos >= list1.length) {
                sorted[i] = list2[list2Pos++];
            } else if (list2Pos >= list2.length) {
                sorted[i] = list1[list1Pos++];
            } else if (list1[list1Pos].compareTo(list2[list2Pos]) < 0) {
                sorted[i] = list1[list1Pos++];
            } else {
                sorted[i] = list2[list2Pos++];
            }
        }
    }


    public String[] getWords() {
        return sorted;
    }
}
