package top.fols.box.util;

import top.fols.box.io.XStream;

public class XDoubleLinkedListTest {
    public static void main(String[] args) throws Throwable {
        XDoubleLinkedList<Object> dll = new XDoubleLinkedList<>();

        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());
        dll.addLast(new XDoubleLinkedList.Element<Object>("1"));
        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());
        dll.addLast(new XDoubleLinkedList.Element<Object>("2"));
        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());
        dll.addLast(new XDoubleLinkedList.Element<Object>("3"));
        System.out.println(dll.getFirst() + " / " + dll.getLast() + " | " + dll.size());


//		dll.clear();
//		System.out.println(dll.getFirst() + " / " + dll.getLast() +" | "+ dll.size());


        System.out.println(dll);

        XDoubleLinkedList<Object> dll2 = new XDoubleLinkedList<>();

//		while (dll.getFirst() != null) {
//			dll2.addLast(dll.getFirst());
//		}
        dll2.addAll(dll2.getLast(), dll);
        dll2.addNext(null, (new XDoubleLinkedList.Element<Object>("4")));

        dll.clear(); dll.clear();

        System.out.println(dll);
        System.out.println(dll2);

        System.out.println();
        System.out.println(dll2.getLast().findFirst());



        System.out.println(dll2);
        System.out.println(XStream.ObjectTool.toObject(XStream.ObjectTool.toByteArray(dll2)));



        System.out.println();

        XDoubleLinkedList dll3=dll2.clone();
        System.out.println(dll3);



        XDoubleLinkedList.ListIterator it = dll2.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            System.out.println(it.now());
            it.remove();
        }


        System.out.println(dll3);

    }
}