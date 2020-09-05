package top.fols.box.util;

public class XDoubleLinkedTest {
    public static void main(String[] args) {
        XDoubleLinked bottom = new XDoubleLinked("0");
		System.out.println("栈底: " + bottom);

		XDoubleLinked need;
		need = new XDoubleLinked("1");
		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(XDoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		need = new XDoubleLinked("2");
		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(XDoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(XDoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		XDoubleLinked top = bottom.findFirst();
		top.addNext(need);
		System.out.println(top + " << " + need);
		System.out.println(XDoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(XDoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		need = new XDoubleLinked("3");
		bottom.addNext(need);
		System.out.println(bottom + " << " + need);
		System.out.println(XDoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

		need.remove();
		System.out.println(" delete " + need);
		System.out.println(XDoubleLinked.toStringFromFirstStart(bottom));
        System.out.println();

        XDoubleLinked two = bottom.findFirst().getNext();
		bottom.findFirst().remove();
		System.out.println(" delete " + bottom.findFirst());
		System.out.println(
			XDoubleLinked.toStringFromFirstStart(bottom) +
			" // " +
			XDoubleLinked.toStringFromFirstStart(two));

    }
}