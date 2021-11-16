package com.zhengguoqiang.exception;

public class AutoCloseableResource implements AutoCloseable {
    private String name = null;
    private boolean throwExceptionOnClose = false;

    public AutoCloseableResource(String name, boolean throwExceptionOnClose) {
        this.name = name;
        this.throwExceptionOnClose = throwExceptionOnClose;
    }

    public void doOp(boolean throwException) throws Exception {
        System.out.println("Resource " + this.name + " doing operation");
        if (throwException) {
            throw new Exception("Error when calling doOp() on resource " + this.name);
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("Resource " + this.name + " close() called");
        if (this.throwExceptionOnClose) {
            throw new Exception("Error when trying to close resource " + this.name);
        }
    }

    public static void main(String[] args) {
        try {
            tryWithResourcesSingleResource();
        } catch (Exception e) {
            e.printStackTrace();
            Throwable[] suppressed = e.getSuppressed();
            System.out.println("suppressed size:" + suppressed.length);
        }
    }

    public static void tryWithResourcesSingleResource() throws Exception {
        try (AutoCloseableResource resource = new AutoCloseableResource("One", true)) {
            resource.doOp(true);
        } catch (Exception e) {
//            e.printStackTrace();
            Throwable[] suppressed = e.getSuppressed();
            System.out.println("interal suppressed size:" + suppressed.length);
            throw e;
        } finally {
            throw new Exception("Hey, an exception from the finally block");
        }
    }

    public static void tryWithResourcesTwoResource() throws Exception {
        try (AutoCloseableResource resourceOne = new AutoCloseableResource("One", true);
             AutoCloseableResource resourceTwo = new AutoCloseableResource("Two", true)) {
            resourceOne.doOp(true);
            resourceTwo.doOp(false);
        }
    }
}
