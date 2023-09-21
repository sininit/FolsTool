package top.fols.atri.io.util;

import java.io.Closeable;

import top.fols.atri.lang.Objects;
import top.fols.atri.interfaces.interfaces.IReleasable;
import top.fols.atri.interfaces.annotations.NotNull;
import top.fols.atri.util.DoubleLinkedList;

public class CloseList implements IReleasable, Closeable {
	final DoubleLinkedList<Status> linkedList   = new DoubleLinkedList<>();

	public Status create() {
		return create((Runnable) null);
	}
	public Status create(final Closeable c) {
		return create(new Runnable() {
			@Override
			public void run() {
				// TODO: Implement this method
				Streams.close(c);
			}
		});
	}
    public Status create(Runnable closeCallback) {
        synchronized (linkedList) {
			if (this.released()) {
				throw new UnsupportedOperationException("released");
			}

			Status status;
			status = new Status();
			status.setCloseCallback(closeCallback);

            Status e = status;

            DoubleLinkedList.Element<Status> elementElement = new DoubleLinkedList.Element<>(e);
            status.element = elementElement;

            linkedList.addLast(elementElement);
            return status;
        }
    }

	@Override
    public void close() {
        synchronized (linkedList) {
            for (DoubleLinkedList.Element<Status> element = linkedList.getFirst();  null != element; element = (DoubleLinkedList.Element<Status>) element.getNext()) {
                Status status = element.content();

                Streams.close(status);
            }
        }
    }

    public int getRunningCount() {
        synchronized (linkedList) {
            return linkedList.size();
        }
    }

    public boolean isAlive() {
        return !(0 == getRunningCount());
    }



	boolean released;
	@Override
	public boolean release() {
		// TODO: Implement this method
		this.close();

		return released = true;
	}

	@Override
	public boolean released() {
		// TODO: Implement this method
		return released;
	}



    @SuppressWarnings("DanglingJavadoc")
    public class Status implements Closeable {
		@NotNull
        DoubleLinkedList.Element<Status> element;

		Runnable closeCallback;
		boolean  closeCallbackExecuted;
		public void setCloseCallback(Runnable callback) {
			this.closeCallback = callback;
		}






        @SuppressWarnings({"SingleStatementInBlock", "PointlessBooleanExpression"})
        /**
         * You have to start to close
         */
        public void close() {
            synchronized (linkedList) {
                if (element.superListNull() == false) {
					element.remove();
				}
            }

			synchronized (element) {
				if (closeCallbackExecuted) {
				} else {
					try {
						if (null != closeCallback) {
							closeCallback.run();
						}
					} catch (Throwable ignored) {}
					closeCallbackExecuted = true;
				}
			}
        }
        public boolean isClosed() {
            synchronized (linkedList) {
                return element.superListNull() || 
					CloseList.this.released();
            }
        }


		public void requireOpen() {
			Objects.requireTrue(!isClosed(), "closed");
		}
    }
}
