
import java.util.Iterator;


public class SortedStringsList implements Iterable<String> {

    private Node head = new Node(null);
    private Node middle = head;
    private int size = 0;


    public void add(String string) {
        if (middle.value != null && middle.value.compareTo(string) < 0) {
            addToList(middle, string); // Start insetting fra midten
        } else {
            addToList(head, string); // Start innsetting fra start
        }
    }

    private void addToList(Node start, String string) {
        Node current = start;
        Node newEntry = new Node(string);

        int middleIndex = size/2; // Indexen til den midterse verdien i lista

        for (int i = 0; ; i++, current = current.next) {
            if (current.next == null) {
                current.next = newEntry;
                break;
            } else if (current.next.value.compareTo(string) > 0) {
                newEntry.next = current.next;
                current.next = newEntry;
                break;
            }

            if (i == middleIndex) { // Oppdater midterste verdi, hvis vi er i midten av lista
                this.middle = current;
            }
        }

        size++;
    }

    public String[] toArray() {
        int i = 0;
        String[] array = new String[size];

        for (String string : this) {
            array[i++] = string;
        }

        return array;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iter();
    }


    private class Node {

        public Node next = null;
        public String value = null;

        public Node(String value) {
            this.value = value;
        }
    }


    private class Iter implements Iterator<String> {

        private Node current = head;

        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public String next() {
            current = current.next;
            return current.value;
        }

        @Override
        public void remove() {
        }
    }
}
