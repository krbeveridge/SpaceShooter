import java.awt.Polygon;
import java.awt.Rectangle;
import javax.swing.JFrame;



public class Player extends JFrame {
	
	 //booleans
     public boolean moveLeft = false;
     public boolean moveRight = false;
     public boolean moveImageUp = false;
     public boolean moveImageDown = false;
     public boolean moveImageLeft = false;
     public boolean moveImageRight = false;
     public boolean extraLifeOnScreen = true;
     public boolean hasExtraLife = false;
	 public boolean shooting = false;
	 public boolean canShoot = true;
	 //booleans
	 
	 //integers
	 private int imageX = 0;
	 private int imageY = 0;
	 public int bulletY = 0;
	 public int bulletX = 0;
	 public int xVel = 250;
     public int bulletSpeed = 40;
     public int XEnemyPos = 50;
	 //integers
	 
     //objects
	 public Polygon[] bullets = new Polygon[1];
	 Rectangle extraLifeCollision;
	 Bullet b = new Bullet(50,50);
	 //objects

    public void Update() {
        if (moveLeft && xVel > 10) {
            xVel -= 10;
        } else if (moveRight && xVel < 450) {
            xVel += 10;
        }

        // Shoot a bullet
    	if (shooting) {
            bulletY -= bulletSpeed;
            if (bulletY < -400) {
                bulletY = 0;
                bullets[0] = null;
                shooting = false;
                canShoot = true;
            }
        }
        
    }
    
    
    public void moveImage() {
    	if(!moveImageUp && !moveImageDown && !moveImageLeft && !moveImageRight) {
    		moveImageRight = true;
    		moveImageDown = true;
    	}
    	if(moveImageDown && moveImageRight) {
    		imageX += 5;
    		imageY -= 5;
    	} 
    	else if(moveImageUp && moveImageRight) {
    		imageX += 5;
    		imageY += 5;
    	} else if(moveImageDown && moveImageLeft) {
    		imageX -= 5;
    		imageY -= 5;
    	} else if(moveImageUp && moveImageLeft) {
    		imageX -= 5;
    		imageY += 5;
    	}
    	
    	//conditions for changing direction
    	//at the bottom of the screen
    	if (imageY <= 0) {
    		imageY = 0;
            moveImageUp = true;
            moveImageDown = false;
        } 
    	//at the left of the screen
    	else if (imageX <= 0) {
            imageX = 0;
            moveImageLeft = false;
            moveImageRight = true;
            
        } 
    	// at the bottom of the screen
    	else if (imageY >= 500) {
    		imageY = 500;
            moveImageUp = false;
            moveImageDown = true;
        } 
    	
    	//at the right of the screen
    	else if (imageX + 45 >= 490) {
            imageX = 490 - 45;
            moveImageRight = false;
            moveImageLeft = true;
        }
    }
    

    
}
