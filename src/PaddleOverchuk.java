public class PaddleOverchuk {

    private int xPos;
    private int yPos;

    private static final int WIDTH = 30;
    private static final int HEIGHT = 200;

    private int yDir=0;

    public PaddleOverchuk(int x, int height){
        xPos=x;
        yPos=(height/2)-(HEIGHT/2);
    }

    public int getyDir() {
        return yDir;
    }

    public void setyDir(int y) {
        yDir = y;
    }

    public int getHeight(){
        return HEIGHT;
    }

    public int getWidth(){
        return WIDTH;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int y) {
        yPos = y;
    }
}