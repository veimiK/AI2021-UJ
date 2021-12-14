import java.math.*;
import java.util.*;
 
class MarsLander {
    private int x, y, dx, dy, fuel, angle, power;
    private int targetL, targetR, targetY;
    
    private static final int Y_marg = 20;
    private static final int speed_marg = 5;

    private static final int maxDY = 40;
    private static final int maxDX = 20;

    private static final double grav = 3.711;

    public void init (int x, int y, int dx, int dy, int fuel, int angle, int power) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.fuel = fuel;
        this.angle = angle;
        this.power = power;
    }
    
    public void setTarget(int targetL, int targetR, int targetY) {
        this.targetL = targetL;
        this.targetR = targetR;
        this.targetY = targetY;
    }
    //cases for several conditions
    public boolean isOverTarget() {
        return targetL <= x && x <= targetR;
    }
    public boolean isFinishing() {
        return y < targetY+Y_marg;
    }
    
    public boolean safeSpeed() {
        return Math.abs(dx) <= maxDX - speed_marg &&
               Math.abs(dy) <= maxDY - speed_marg;
    }
    
    public boolean wrongDirection() {
        return (x < targetL && dx < 0) || (targetR < x && dx > 0);
    }
    
    public int angleToSlow() {
        double speed = Math.sqrt(dx*dx + dy*dy);
        return (int) Math.toDegrees(Math.asin((double)dx / speed));
    }

    public int powerToHover() {
        return (dy >= 0) ? 3 : 4;
    }
    public int angleToAimTarget() {
        int angle = (int) Math.toDegrees(Math.acos(grav / 4.0));
        if (x < targetL)
            return -angle;
        else if (targetR < x)
            return angle;
        else
            return 0;
    }
    public boolean tooFastHorizontally() {
        return Math.abs(dx) > 4*maxDX;
    }
    
    public boolean tooSlowHorizontally() {
        return Math.abs(dx) < 2*maxDX;
}
}
class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        MarsLander ship = new MarsLander();
        int N = in.nextInt();
        
        //check landing area
        int landX, landY, prevX, prevY;
        prevX = prevY = -1;
        for (int i = 0; i < N; i++) {
            landX = in.nextInt();
            landY = in.nextInt();
            if (landY == prevY) {
                ship.setTarget(prevX, landX, landY);
            } else {
                prevX = landX;
                prevY = landY;
            }
        }
        for (;;) {       
            ship.init(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
            if (!ship.isOverTarget()) {
                if (ship.wrongDirection() || ship.tooFastHorizontally()) {System.out.println(ship.angleToSlow() + " 4");}
                else if (ship.tooSlowHorizontally()) {System.out.println(ship.angleToAimTarget() + " 4");}
                else {System.out.println("0 " + ship.powerToHover());}
            }
            else {
                if (ship.isFinishing()) {System.out.println("0 3");}
                else if (ship.safeSpeed()) {System.out.println("0 2");}
                else {System.out.println(ship.angleToSlow() + " 4");}
            }
        }
    }
}