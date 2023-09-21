package top.fols.box.util;

import top.fols.atri.io.Streams;
import top.fols.atri.util.DoubleLinkedList;

public class DoubleLinkedListTest {
    public static void main(String[] args) throws Throwable {
        DoubleLinkedList<Object> dll = new DoubleLinkedList<>();

        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());
        dll.addLast(new DoubleLinkedList.Element<Object>("1"));
        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());
        dll.addLast(new DoubleLinkedList.Element<Object>("2"));
        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());
        dll.addLast(new DoubleLinkedList.Element<Object>("3"));
        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());


//		dll.clear();
//		System.out.println(dll.getFirst() + " / " + dll.getLast() +" | "+ dll.size());


        System.out.println(dll);

        DoubleLinkedList<Object> dll2 = new DoubleLinkedList<>();

//		while (dll.getFirst() != null) {
//			dll2.addLast(dll.getFirst());
//		}
        dll2.addAll(dll2.getLast(), dll);
        dll2.addNext(null, (new DoubleLinkedList.Element<Object>("4")));

        dll.clear(); dll.clear();

        System.out.println(dll);
        System.out.println(dll2);

        System.out.println();
        System.out.println(dll2.getLast().findFirst());



        System.out.println(dll2);
        System.out.println(Streams.ObjectTool.toObject(Streams.ObjectTool.toBytes(dll2)));



        System.out.println();

        DoubleLinkedList dll3=dll2.clone();
        System.out.println(dll3);



        DoubleLinkedList.ListIterator it = dll2.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            System.out.println(it.now());
            it.remove();
        }


        System.out.println(dll3);

    }
}