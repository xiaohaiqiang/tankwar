import java.awt.*;

public class Main {
    public static void main(String[] args) {
        TankFrame tf = new TankFrame();

        tf.setVisible(true);

        while(true){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tf.repaint();//repaint()----update()----paint()
        }
    }
}
