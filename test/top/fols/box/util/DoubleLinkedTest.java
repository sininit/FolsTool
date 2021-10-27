package top.fols.box.util;

import top.fols.atri.util.DoubleLinked;

public class DoubleLinkedTest {
    public static void main(String[] args) {
        DoubleLinked bottom = new DoubleLinked("0");
		System.out.println("栈底: " + bottom);

		DoubleLinked need;
		need = new DoubleLinked("1");
		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(DoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		need = new DoubleLinked("2");
		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(DoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(DoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		DoubleLinked top = bottom.findFirst();
		top.addNext(need);
		System.out.println(top + " << " + need);
		System.out.println(DoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(DoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		need = new DoubleLinked("3");
		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(DoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		need.remove();
		System.out.println(" delete " + need);
		System.out.println(DoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

        DoubleLinked two = bottom.findFirst().getNext();
		bottom.findFirst().remove();
		System.out.println(" delete " + bottom.findFirst());
		System.out.println(
			DoubleLinked.toStringFromFirstStart(bottom) +
			" // " +
			DoubleLinked.toStringFromFirstStart(two));

    }
}