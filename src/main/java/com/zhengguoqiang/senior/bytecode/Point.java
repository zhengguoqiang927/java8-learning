package com.zhengguoqiang.senior.bytecode;

/**
 * @author zhengguoqiang
 */
public class Point {
    private int x;
    private int y;

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
    move方法字节码
    public void move(int, int);
    Code:
       0: aload_0
       1: iload_1
       2: putfield      #2                  // Field x:I
       5: aload_0
       6: iload_2
       7: putfield      #3                  // Field y:I
      10: return

   aload_0:aload_0 instruction is loading a reference onto the stack from the local variable 0
   iload_1:iload_1 is loading an int value from the local variable 1
   putfield:putfield is setting a field x of our object. All operations are analogical for field y
     */

    public void print() {
        System.out.println("x=" + this.x + "," + "y=" + this.y);
    }
}
