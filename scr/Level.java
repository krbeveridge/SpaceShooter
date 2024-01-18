import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Level extends JFrame{
	
	//objects
	Player player = new Player();
	private Enemy enemy;
	Bullet b;
    Rectangle2D playerCollision;
    public Enemy[] enemies = new Enemy[6];
    Image scaledImage;
	SoundEffects se;
    private Timer gameTimer;
	//objects
	
	//booleans
    public boolean enemyStartedShooting = false;
    private boolean firstStart = true;
    private boolean playerDied = false;
    public boolean playShootingSound = true;
    public boolean isMoving = true;
    //booleans
    
    //integers
	int score = 0;
    private int Rounds = 1;
    private int backgroundCounter = 0;
    private int enemiesRemaining = 0;
    //integers
    
    //polygons
    private Polygon[] death = new Polygon[1];
    Polygon bullet;
    Polygon playerShape;
    //polygons

    
	public Level(){
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setFocusable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        createBufferStrategy();
        this.se = new SoundEffects();
        se.playLevelSong(true);

        //Main GameLoop Timer
		 gameTimer = new Timer(20, new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	player.Update();
	                repaint();
	            }
	        });
	        gameTimer.start();
	    //Main GameLoop Timer
	        
	        
    	
	     // Controls for Player
	        this.addKeyListener(new KeyListener() {
	        	public void keyPressed(KeyEvent e) {
	        	    int key = e.getKeyCode();
	        	    if (key == KeyEvent.VK_LEFT) {
	        	    	isMoving = true;
	        	        player.moveLeft = true;
	        	        player.moveRight = false;
	        	    } else if (key == KeyEvent.VK_RIGHT) {
	        	    	isMoving = true;
	        	        player.moveRight = true;
	        	        player.moveLeft = false;
	        	    }
	        	    if (key == KeyEvent.VK_UP) {
	        	        if (player.canShoot) {
	        	            player.shooting = true;
	        	            player.bulletX = player.xVel + 20;
	        	            repaint();
	        	        }
	        	    }
	        	    //restart game
	        	    if(key == KeyEvent.VK_SPACE) {
	        	        enemies = new Enemy[6];
	        	    	se.playLevelSong(true);
	        	    	firstStart = true;
	        	    	playerDied = false;
	        	    	player.extraLifeOnScreen = true;
	        	    	for(int i = 0; i < enemies.length - 1; i++) {
	        	    		enemies[i] = null;
	        	    	}
	        	    	Rounds = 1;
	        	    	score = 0;
	        	    	enemiesRemaining = 0;
	        	    	player.xVel = 250;
	        	    	player.bulletX = player.xVel + 20;
	        	    	repaint();
	        	    }
	        	}


	        	public void keyReleased(KeyEvent e) {
	        	    int key = e.getKeyCode();
	        	    if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
	        	        player.moveLeft = false;
	        	        player.moveRight = false;
	        	    }
	        	    if (key == KeyEvent.VK_UP) {
	        	       // player.canShoot = true;
	        	    }
	        	}

	            public void keyTyped(KeyEvent e) {
	            }
	        });
	    //Controls for Player
	}
	
	
	public void paint(Graphics g) {
		
		//check if the bufferStrategy was created
		createBufferStrategy();
		//check if the bufferStrategy was created 
		
		//for smoother rendering
		 BufferStrategy bufferStrategy = this.getBufferStrategy();
		 

		 Graphics2D g2D = (Graphics2D) bufferStrategy.getDrawGraphics();
		 
		//Drawing Background
		drawBackground(g2D);
        //Drawing the background
		
  		
		// Player Shape
		drawPlayer(g2D);
        // PlayerShape
        
		
        //start Round
		startRound(g2D);
        //start Round
        
        
        // Move the enemy
        moveEnemy(g2D);
        //Move the enemy
        
        
        // Player shoots a bullet
        shootABullet(g2D);
        // Player shoots a bullet
        
        
        //cleans up the dead enemies
        cleanEnemies();
        //cleans up the dead Enemies

        
        //enemy shoot
        enemyShoot(g2D);	
        //enemy shoot
        
        g2D.dispose();
        bufferStrategy.show();
    }
	
	
	private void Round1(Graphics2D g2D) {
		for(int i = 0; i < 6; i++) {
			if(enemies[i] == null) {
        	//creates enemy objects
        	enemy = new Enemy(50 * (i + 1) - 20, 50 * (i + 1) + 20,20,20);
        	//adds 1 to enemies Remaining in the game
        	enemiesRemaining ++;
        	//adds enemies to array
        	enemies[i] = enemy;
        	//each enemy is a square
        	enemies[i].isSquare = true;
        	//plays enter animation for squares
        	enemies[i].EntersLevel(g2D);
			}
		}
		firstStart = false;
		Rounds++;
	}
	
	
	private void Round2(Graphics2D g2D) {
		enemiesRemaining = 0;
		enemies = new Enemy[8];
	    //spawns 5 enemies
	    for(int i = 0; i < 8; i++) {
	        //checks if the enemies were already created
	        if(enemies[i] == null) {
	            //creates enemy objects
	            enemy = new Enemy(50 * (i + 1), 50 * (i + 1),20,20);
	            enemiesRemaining++;
	            //adds enemies to array
	            enemies[i] = enemy;
	            //sets the destroyed boolean to false
	            enemies[i].setDestroyed(false);
	            //creates 2 squares
	            if(i % 2 == 0) {
	                enemies[i].isSquare = true;
	                enemies[i].renderSquare(g2D);
	                enemies[i].moveSquare();
	            }
	            //creates 3 triangles
	            else {
	                enemies[i].isTriangle = true;
	                enemies[i].renderTriangle(g2D);
	                enemies[i].moveTriangle();
	            }
	            //plays enter animation for squares
	            enemies[i].EntersLevel(g2D);
	        } 
	    }
	    firstStart = false;
	    Rounds++;
	}
	
	private void Round3(Graphics2D g2D) {
		enemiesRemaining = 0;
		enemies = new Enemy[8];
		for(int i = 0; i < 8; i ++) {
			//create enemy objects
			if(enemies[i] == null) {
	            //creates enemy objects
	            enemy = new Enemy(50 * (i + 1), 50 * (i + 1),20,20);
	            enemiesRemaining++;
	            //adds enemies to array
	            enemies[i] = enemy;
	            //sets the destroyed boolean to false
	            enemies[i].setDestroyed(false);
	            enemies[i].isDiamond = true;
	            //enter level animation
	            enemies[i].EntersLevel(g2D);
			}

		}
		firstStart = false;
		Rounds ++;
	}
	
	public void Round4(Graphics2D g2D) {
		enemiesRemaining = 0;
		enemies = new Enemy[8];
		for(int i = 0; i < 8; i ++) {
			//create enemy objects
			if(enemies[i] == null) {
	            //creates enemy objects
	            enemy = new Enemy(50 * (i + 1), 50 * (i + 1),20,20);
	            enemiesRemaining++;
	            //adds enemies to array
	            enemies[i] = enemy;
	            //sets the destroyed boolean to false
	            enemies[i].setDestroyed(false);
	            //set boolean for enemy
	            enemies[i].isOctagon = true;
	            //only octagons on this level
	            enemies[i].renderOctagon(g2D);
	            //enter level animation
	            enemies[i].EntersLevel(g2D);
			}
		}
		firstStart = false;
		Rounds ++;
	}
	
	public void RandomGeneratedRound(Graphics2D g2D) {
		enemiesRemaining = 0;
		int numberOfTriangles = 0;
		enemies = new Enemy[9];
		Random random = new Random();
		for(int i = 0; i < 9; i ++) {
			if(enemies[i] == null) {
	            //creates enemy objects
	            enemy = new Enemy(50 * (i + 1), 50 * (i + 1),20,20);
	            enemiesRemaining++;
	            //adds enemies to array
	            enemies[i] = enemy;
	            //sets the destroyed boolean to false
	            enemies[i].setDestroyed(false);
	            //if number is divisible by 4 then create square
				if(random.nextInt() % 4 == 0) {
					enemies[i].isSquare = true;
					enemies[i].renderSquare(g2D);
					enemies[i].EntersLevel(g2D);
				}
				//if number is divided by 4 with remainder 1 then create triangle
				else if(random.nextInt() % 4 == 1 && numberOfTriangles < 4) {
					numberOfTriangles ++;
					enemies[i].isTriangle = true;
					enemies[i].renderTriangle(g2D);
					enemies[i].EntersLevel(g2D);
				} 
				//if number is divided by 4 with remainder 2 then create diamond
				else if(random.nextInt() % 4 == 2) {
					enemies[i].isDiamond = true;
					enemies[i].renderDiamond(g2D);
					enemies[i].EntersLevel(g2D);
				} 
				//if number is divided by 4 with remainder 3 then create octagon
				else {
					enemies[i].isOctagon = true;
					enemies[i].renderOctagon(g2D);
					enemies[i].EntersLevel(g2D);
				}
			}
		}

		firstStart = false;
		Rounds ++;
	}
	
	private void createBufferStrategy() {
		if(this.getBufferStrategy() == null) {
			 try {
	            	//use render buffering. Additional buffering while the back buffer(invisible buffer) is rendering
	                this.createBufferStrategy(3);
	                //sleep while buffer is being created
	                Thread.sleep(50);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
		 }
	}
	
	public void createBackground(Graphics2D g2D) {
		//images for the background
		 String[] imagePath = {"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space10.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space9.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space8.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space7.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space6.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space5.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space4.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space3.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space2.png",
	        		"C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\Pictures\\Space1.jpg"};
		
       try {
    	   //background counter is initialized to 0
    	   File file = new File(imagePath[backgroundCounter]);
    	   if (!file.exists()) {
    	        System.out.println("File not found: " + file.getAbsolutePath());
    	   }
		Image background = ImageIO.read(file);
		   //backgroundCounter 1,2,3,4...10 repeat
	       this.backgroundCounter = (backgroundCounter + 1) % 10;
	       g2D.drawImage(background, 0, 0, 500, 500, null);
	}catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}


		 
	}
	
	private void drawPlayer(Graphics2D g2D) {
        if(playerDied == false) {
        	death[0] = null;
            playerShape = new Polygon();
            String imageFilePath = "C:\\Users\\kyleb\\eclipse-workspace\\SpaceShooter\\PlayerSpaceShip.png";
            try {
            	BufferedImage image = ImageIO.read(new File(imageFilePath));

            // Scale the image
            int scaledWidth = 45;
            int scaledHeight = 70;
            scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            g2D.drawImage(scaledImage, this.player.xVel, 400, scaledWidth, scaledHeight, this);
            isMoving = false;
            //collision box around player
            playerCollision = new Rectangle(this.player.xVel + 5, 415, scaledWidth - 10, scaledHeight - 20);
            } catch(IOException e) {
            	e.printStackTrace();
            }

        }
        //stops the player from shooting after death
        else{
            death[0] = playerShape;
        }
	}
	
	
	private void startRound(Graphics2D g2D) {
		if(firstStart) {
			switch(Rounds) {
			case 1:
				Round1(g2D);
				break;
			case 2:
				Round2(g2D);
				break;
			case 3:
				Round3(g2D);
				break;
			case 4:
				Round4(g2D);
				break;
			case 5:
				RandomGeneratedRound(g2D);
				break;
			}
		}
	}
	

	
	private void moveEnemy(Graphics2D g2D) {
		 for(int i = 0; i < enemies.length - 1; i++) {
     		//checks if there is an enemy to move and whether or not to create the next Round
     		if(!enemies[i].isDestroyed()){
     			if(enemies[i].isSquare) {
     				enemies[i].renderSquare(g2D);
     				enemies[i].moveSquare();
     			}
     			else if(enemies[i].isTriangle) {
     				enemies[i].renderTriangle(g2D);
     				enemies[i].moveTriangle();
     			}
     			else if(enemies[i].isDiamond) {
     				enemies[i].renderDiamond(g2D);
     				enemies[i].moveDiamond();
     			}
     			else if(enemies[i].isOctagon) {
     				enemies[i].renderOctagon(g2D);
     				enemies[i].moveOctagon();
     			}
     		}
     }
	}
	
	
	
	private void shootABullet(Graphics2D g2D) {
		 if (player.shooting && death[0] == null) {
	            if (player.bulletX == 0) player.bulletX = player.xVel + 90; // for the first bullet
	            //for shooting sound effects
	            if(player.canShoot) {
	                se.ShootingSoundEffect();
	            }
	            player.canShoot = false;
	            //bullet shape
	            player.bullets[0] = new Polygon();
	            player.bullets[0].addPoint(player.bulletX + 6, player.bulletY + 450); // Right Point
	            player.bullets[0].addPoint(player.bulletX , player.bulletY + 450); // left Point
	            player.bullets[0].addPoint(player.bulletX + 3, player.bulletY + 430); // Top Point
	            //bullet shape
	            g2D.setColor(Color.RED);
	            g2D.fill(player.bullets[0]);
	            //if the bullet collides with the enemy
	            for(int i = 0; i < enemies.length - 1; i ++) {
	                if(enemies[i] != null && enemies[i].polygonsIntersect(player.bullets[0])) {
	                    // Mark the enemy as destroyed
	                    enemies[i].setDestroyed(true);
	                    //deletes 1 from the enemies remaining variable
	                    enemiesRemaining --;
	                    
	                    //gets rid of the bullet after collision
	                    player.bullets[0] = null;
	                    player.shooting = false;
	                    player.canShoot = true;
	                    player.bulletY = 0;
	                    //gets rid of the bullet after collision
	                    
	                    score++;
	                    if(enemiesRemaining == 1) {
	                    	firstStart = true;
	                    }
	                    break;
	                }
	            }
	            //gets rid of the bullet after exiting the screen
	            if(player.bulletY > 500) {
	            	player.bullets[0] = null;
	            }
	        }
	}
	
	
	private void cleanEnemies() {
		 List<Enemy> aliveEnemies = new ArrayList<>();
	        for (Enemy enemy : enemies) {
	            if (enemy != null && !enemy.isDestroyed()) {
	            	//adds all of the enemies still on screen to the array and cleans the dead ones
	                aliveEnemies.add(enemy);
	            }
	        }
	        enemies = aliveEnemies.toArray(new Enemy[0]);
	}
	
	
	private void enemyShoot(Graphics2D g2D) {
        for (int i = 0; i < enemies.length - 1; i++) {
        	//if the enemy exists and is not destroyed
        	if(enemies[i] != null && !enemies[i].isDestroyed()) {
        		if (enemies[i].canShoot(g2D, this.player.xVel, 490, 30, 490) && enemies[i].bullets[0] == null) {
        			enemies[i].bulletY = enemies[i].getY();
        			//Going from not shooting to shooting
        			if (enemies[i].shooting == false) {
        				enemies[i].bulletX = enemies[i].getX();
        				Polygon polygon = new Polygon();
        				enemies[i].bullets[0] = polygon;
        				polygon.addPoint(enemies[i].bulletX - 5, enemies[i].bulletY + 45);
        				polygon.addPoint(enemies[i].bulletX, enemies[i].bulletY + 55);
        				polygon.addPoint(enemies[i].bulletX + 5, enemies[i].bulletY + 45);
        				enemies[i].shooting = true;
        			}
        		}
        		//bullet velocity
        		if (enemies[i].shooting == true && enemies[i] != null && enemies[i].bullets[0] != null) {
        			Polygon polygon = new Polygon();
        			enemies[i].bullets[0] = polygon;
        			polygon.addPoint(enemies[i].bulletX - 5, enemies[i].bulletY + 45);
        			polygon.addPoint(enemies[i].bulletX, enemies[i].bulletY + 55);
        			polygon.addPoint(enemies[i].bulletX + 5, enemies[i].bulletY + 45);
        			enemies[i].bulletY += 10;
        			Rectangle2D enemyShot = polygon.getBounds();
        			g2D.setColor(Color.red);
        			g2D.fill(enemies[i].bullets[0]);
        			//destroys player if enemy shoots the player
        			if (enemyShot.intersects(playerCollision) && death[0] == null) {
        				//gets rid of bullet
        				enemy.trash[0] = enemies[i].bullets[0];
        				//gets rid of collision for bullet
        				enemy.shells[0] = enemyShot;
        				se.stopMusic();
        				playerDied = true;
        				se.DeathSoundEffect();
        				g2D.setColor(Color.red);
        				player.hasExtraLife = false;
        			}
        			//gets rid of bullet after it goes beyond the screen
        			if (enemies[i].bulletY + 20 > 500) {
        				enemies[i].bullets[0] = null;
        				enemies[i].bulletY = enemies[i].getY();
        				enemies[i].shooting = false;
        			}       			
        		}            	
        	}
        	// Check if the enemy is destroyed
        	else {
			    continue;
			}
        }
	}
	
	private void drawBackground(Graphics2D g2D) {
		//draws the space background
		 createBackground(g2D);
		 //draws the score for the player
		 g2D.setColor(Color.green);
       g2D.drawString(Integer.toString(score), 250, 50);
		if(death[0] != null) {
			g2D.drawString("Press space to restart", 190, 250);
			g2D.drawString("Score: " + score, 230, 275);
		}
	}
}