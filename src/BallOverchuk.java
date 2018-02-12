public class BallOverchuk {

    private int ballX;
    private int ballY;

    private static final int RADIUS=20;

    private int xDir;
    private int yDir;

    public BallOverchuk(){
        ballX=0;
        ballY=0;
    }

    public int getRadius(){
        return RADIUS;
    }

    public void invertxDir(){
        xDir=xDir*(-1);
    }

    public void invertyDir(){
        yDir=yDir*(-1);
    }

    public void increaseSpeed(boolean xVal,boolean yVal){
        if(xVal) {
            if (xDir > 0) {
                xDir+=2;
            } else {
                xDir-=2;
            }
        }
        if(yVal) {
            if (yDir > 0) {
                yDir+=2;
            } else {
                yDir-=2;
            }
        }
    }

    public int getBallX() {
        return ballX;
    }

    public void setBallX(int x) {
        ballX = x;
    }

    public int getBallY() {
        return ballY;
    }

    public void setBallY(int y) {
        ballY = y;
    }

    public int getxDir() {
        return xDir;
    }

    public void setxDir(int x) {
        xDir = x;
    }

    public int getyDir() {
        return yDir;
    }

    public void setyDir(int y) {
        yDir = y;
    }
}