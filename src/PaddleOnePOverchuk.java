public class PaddleOnePOverchuk {

    //x and y position of the paddle for single player pong
    private int xPos;
    private int yPos;

    //constants for the paddle
    private static final int WIDTH = 200;
    private static final int HEIGHT = 30;

    //since this one only moves left and right, it only stores the x velocity
    private int xDir=0;

    //constructor that sets the x and y based off of the size of the window
    public PaddleOnePOverchuk(int y, int width){
        xPos=(width/2)-(WIDTH/2);
        yPos=y;
    }

    //getter for the x velocity
    public int getxDir() {
        return xDir;
    }

    //setter for the x velocity
    public void setxDir(int x) {
        xDir = x;
    }

    //getter for the height
    public int getHeight(){
        return HEIGHT;
    }

    //getter for the width
    public int getWidth(){
        return WIDTH;
    }

    //getter for the x position
    public int getxPos() {
        return xPos;
    }

    //getter for the y position
    public int getyPos() {
        return yPos;
    }

    //setter for the x position
    public void setxPos(int x) {
        xPos = x;
    }
}
