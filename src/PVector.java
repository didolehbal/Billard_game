import javafx.scene.image.ImageView;

public class PVector {
    public double x;
    public double y;
    PVector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static PVector add(PVector p1, PVector p2){
        return new PVector(p1.x+p2.x,p1.y+p2.y);
    }
    public static PVector subs(PVector p1, PVector p2){
        return new PVector(p1.x-p2.x,p1.y-p2.y);
    }
    public void add(PVector p){
        x += p.x;
        y += p.y;
    }
    public void mult(double b){
        x *= b;
        y *= b;
    }
    @Override
    public String toString() {
        return "PVector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public void applyVector(ImageView img , PVector v){
        x += v.x;
        y += v.y;
        img.setX(x);
        img.setY(y);

    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double magnitude(){
        return  Math.sqrt(x*x + y*y);
    }
    public double normalize(double b){
        double mag = magnitude();
        if(mag > 1){
            x = x / mag;
            y= y / mag;
        }
        else {
            x = 0;
            y = 0;
        }
        x*=b;
        y*=b;
        return mag;
    }
    public double normalize(){
        double mag = magnitude();
        if(mag > 0){
            x = x / mag;
            y= y / mag;
        }

        return mag;
    }
}
