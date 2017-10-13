package org.firstinspires.ftc.teamcode;

/**
 * Created by temp on 10/12/2017.
 */

public class TestSomething {
    static class ColorBlue implements ColorProvider {

        @Override
        public Color getColor() {
            return Color.BLUE;
        }
    }
    public static void main(String[] args) {
        BaseAuto auto = new BaseAuto(1,2,3, new ColorBlue());

        System.out.println(String.format("Color result is %d", auto.doSomethingWithColor()));
    }
}
