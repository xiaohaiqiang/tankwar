import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {
    private int x;
    private int y;
    private Dir dir;
    private boolean bL, bU, bR, bD;

    public static final int SPEED = 5;

    public Tank(int x, int y, Dir dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void paint(Graphics g) {
        g.fillRect(x, y, 50, 50);
        move();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }

        setMainDir();

    }

    private void setMainDir() {
        if(!bL && !bR && !bD && !bU){
            dir = Dir.STOP;
        }
        if(bL && !bR && !bD && !bU){
            dir = Dir.L;
        }
        if(!bL && bR && !bD && !bU){
            dir = Dir.R;
        }
        if(!bL && !bR && bD && !bU){
            dir = Dir.D;
        }
        if(!bL && !bR && !bD && bU){
            dir = Dir.U;
        }
    }

    private void move() {
        switch(dir){
            case L:
                x -= SPEED;
                break;
            case R:
                x += SPEED;
                break;
            case U:
                y -= SPEED;
                break;
            case D:
                y += SPEED;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        setMainDir();
    }
}
