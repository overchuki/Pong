public class PaddleOnePOverchuk {

    private int xPos;
    private int yPos;

    private static final int WIDTH = 200;
    private static final int HEIGHT = 30;

    private int xDir=0;

    public PaddleOnePOverchuk(int y, int width){
        xPos=(width/2)-(WIDTH/2);
        yPos=y;
    }

    public int getxDir() {
        return xDir;
    }

    public void setxDir(int x) {
        xDir = x;
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

    public void setxPos(int x) {
        xPos = x;
    }
}
