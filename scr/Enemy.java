import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.Line;
import javax.swing.Timer;

class Enemy {
	
	//integers
    private int x;
    private int y;
    private int width;
    private int height;
    public int bulletX;
    public int bulletY = y;
    //integers
    
    //booleans
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    public boolean shooting = false;
    private boolean destroyed = false;
    public boolean enemyCanShoot = true;
    //booleans
    
    //Shape booleans
    public boolean isSquare = false;
    public boolean isTriangle = false;
    public boolean isDiamond = false;
    public boolean isOctagon = false;
    //Shape booleans
    
    //Object arrays
    public Polygon[] bullets = new Polygon[7];
    public Rectangle2D[] shells = new Rectangle2D[7];
    public Polygon[] trash = new Polygon[7];
    //Object arrays
    

    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void moveSquare() {
        if (moveLeft) {
            x -= 5;
        } else {
            x += 5;
        }


        if (x <= 0) {
            x = 0;
            moveLeft = false;
        } else if (x + width >= 490) {
            x = 490 - width;
            moveLeft = true;
        }
    }
    
    public void moveTriangle() {
    	if (moveDown) {
            y -= 5;
        } else {
            y += 5;
        }


        if (y <= 0) {
            y = 0;
            moveDown = false;
        } else if (y + height >= 490) {
            y = 490 - height;
            moveDown = true;
        }
    }
    
    public void moveDiamond() {
    	//moves in a sideways figure 8 pattern
    	if(!moveUp && !moveDown && !moveLeft && !moveRight) {
    		moveLeft = true;
    		moveUp = true;
    	}
    	if(moveDown && moveRight) {
    		x += 5;
    		y -= 5;
    	} 
    	else if(moveUp && moveRight) {
    		x += 5;
    		y += 5;
    	} else if(moveDown && moveLeft) {
    		x -= 5;
    		y -= 5;
    	} else if(moveUp && moveLeft) {
    		x -= 5;
    		y += 5;
    	}
    	
    	//conditions for changing direction
    	//at the bottom of the screen
    	if (y  + height <= 0) {
    		y = 0;
            moveUp = true;
            moveDown = false;
        } 
    	//at the left of the screen
    	else if (x <= 0) {
            x = 0;
            moveLeft = false;
            moveRight = true;
            
        } 
    	// at the bottom of the screen
    	else if (y + height >= 400) {
    		y = 400 - height;
            moveUp = false;
            moveDown = true;
        } 
    	
    	//at the right of the screen
    	else if (x + width >= 490) {
            x = 490 - width;
            moveRight = false;
            moveLeft = true;
        }
    		
    }
    
    public void moveOctagon() {
    	//moves in a square motion
    	int direction = 5;
    	if(moveRight) {
    		x += direction;
    	} else if(moveLeft) {
    		x -=direction;
    	} else if(moveUp) {
    		y -= direction;
    	} else if(moveDown) {
    		y += direction;
    	} else {
    		x += direction;
    	}
    	//if at the right of the screen
    	if(x + width >= 490) {
    		x = 490 - width;
    		moveRight = false;
    		moveUp = true;
    	}
    	//if at the bottom of the screen
    	if( y + height >= 400) {
    		y -= direction;
    		moveDown = false;
    		moveRight = true;
    	}
    	//if at the top of the screen
    	if( y - height * 2 <= 0) {
    		y += direction;
    		moveUp = false;
    		moveRight = false;
    		moveLeft = true;
    	}
    	//if that the left of the screen
    	if(x  - width <= 0) {
    		x += direction;
    		moveLeft = false;
    		moveDown = true;
    	}
 
    }

    public void renderSquare(Graphics2D g2D) {
    	//for Squares
    	isSquare = true;
    	Polygon enemy = new Polygon();
        enemy.addPoint(x, y);               // top-left
        enemy.addPoint(x + width, y);       //
        enemy.addPoint(x + width, y + height); // bottom-right
        enemy.addPoint(x, y + height); // bottom-left
        g2D.setColor(Color.blue);
        g2D.fill(enemy);
    }
    public void renderTriangle(Graphics2D g2D) {
    	//for triangles
    	isTriangle = true;
    	Polygon enemy = new Polygon();
    	enemy.addPoint(x, y);
    	enemy.addPoint(x + width, y);
    	enemy.addPoint(x + (width)/2, y + width);
    	g2D.setColor(Color.green);
    	g2D.fill(enemy);
    }
    public void renderDiamond(Graphics2D g2D) {
    	//for diamonds
    	isDiamond = true;
    	Polygon enemy = new Polygon();
    	enemy.addPoint(x, y + height);							//top point
    	enemy.addPoint(x - width/2, y + height/2); //left point
    	enemy.addPoint(x,  y - height);						//bottom point
    	enemy.addPoint(x + width/2, y + height/2);	//right point
    	g2D.setColor(Color.pink);
    	g2D.fill(enemy);
    }
    
    public void renderOctagon(Graphics2D g2D) {
    	//for Octagons
    	isOctagon = true;
    	Polygon Octagon = new Polygon();
    	int radius = 15;
    	for( int i = 0; i < 8; i ++) {
    		double angle = 2 * Math.PI * i/7;
            int xPos = (int) (x + radius * Math.cos(angle));
            int yPos = (int) (y + radius * Math.sin(angle));
            Octagon.addPoint(xPos, yPos);
    		
    	}
    	g2D.setColor(Color.orange);
    	g2D.fill(Octagon);
    }
    
    //for death of enemy
    public boolean polygonsIntersect(Polygon polygon) {
        Rectangle2D enemyRect = new Rectangle2D.Double(x, y, width, height);

        for (int i = 0; i < polygon.npoints; i++) {
            if (enemyRect.contains(polygon.xpoints[i], polygon.ypoints[i])) {
                return true;
            }
        }

        return false;
    }
    //for death of enemy
    
    //to check if the enemy should shoot
    public boolean canShoot(Graphics2D g2D, int x, int y, int width, int height){
    	Rectangle2D intersection = new Rectangle2D.Double();
    	
    	//draws a rectangle from the enemy down the the bottom of the screen
    	intersection.setRect(this.x, this.y, this.width, this.y+ 500);

    	//check if the rectangle intersects with the player
    	if(intersection.intersects(x, y, width, height)) {
    		return true;
    	} else{
        	return false;
    	}

    
    }
    
    
    public void setDestroyed(Boolean bool) {
    	this.destroyed = bool;
    	this.enemyCanShoot = bool;
    }
    
    
    public boolean isDestroyed() {
    	return destroyed;
    }
    
    
    public int getY() {
    	return this.y;
    }
    
    
    public int getX() {
    	return this.x;
    }
    
    
    public void EntersLevel(Graphics2D g2D) {
    	//squares enter from the left
        int xPos = this.x;
            x = -width; // Start from outside the left edge
            Timer timer = new Timer(20, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    x += 5;
                    if (x >= xPos) {
                        ((Timer) e.getSource()).stop(); // Stop the timer when reaching the desired position
                    }
                }
            });
            timer.start();
        }


}
    

