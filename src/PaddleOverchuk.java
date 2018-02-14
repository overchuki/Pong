public class PaddleOverchuk {

    //variables for the x and y position of the two player paddle
    private int xPos;
    private int yPos;

    //constants for the height and width of these paddles
    private static final int WIDTH = 30;
    private static final int HEIGHT = 200;

    //since it only moves up and down, there is only a variable for the y velocity
    private int yDir=0;

    //constructor setting the x and y position according to side and size of the window
    public PaddleOverchuk(int x, int height){
        xPos=x;
        yPos=(height/2)-(HEIGHT/2);
    }

    //getter for the y velocity
    public int getyDir() {
        return yDir;
    }

    //setter for the y velocity
    public void setyDir(int y) {
        yDir = y;
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

    //setter for the y position
    public void setyPos(int y) {
        yPos = y;
    }
}