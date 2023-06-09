package Entity;

import Main.MainPanel;
import org.joml.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Pet extends Entity implements Renderer, MouseListener, MouseMotionListener {
    BufferedImage sprite, idle1, idle2, walk1, walk2, walk3, walk4,
                    walkLeft1, walkLeft2, walkLeft3, walkLeft4,
                    idleLeft1, idleLeft2, grab, grabLeft, grab2,
                    grabLeft2, sleeping0, sleeping1, sleeping2,
                    sleepingLeft0, sleepingLeft1, sleepingLeft2,
                    stretching0, stretchingLeft0, sit1, sit2,
                    sitLeft1, sitLeft2, lick1, lick2, lickLeft1,
                    lickLeft2, jump, jumpLeft, fall, fallLeft;
    String direction, status;
    Random random = new Random();
    int speed, counter, spriteTick, mouseXOffset, mouseYOffset,
            petWidth, petHeight, xCollision, yCollision,
            fallingSpeed, xMouseGrab, yMouseGrab, sleepTimer,
            sleepTicker, randomLickTick, jumpCounter, floor;
    int randomNum = 6;
    Bowl bowl; Ball ball;
    public int lastMouseX;
    public int lastMouseY;
    public float velX;
    public float velY;
    boolean onPlatform = false;

    public Pet(MainPanel mp)
    {
        super(mp);
        setPetImages();
        setInitialConditions();
        bowl = new Bowl(this.mp, 360, (y+64));
        bowl.setBowlFull();
        mp.addMouseListener(this);
        mp.addMouseMotionListener(this);
    }

    public void setInitialConditions() {
        x = 0;
        y = mp.screenHeight - idle1.getHeight() - 40;
        speed = 1;
        status = "idle";
        spriteNum = 1;
        petWidth = idle1.getWidth();
        petHeight = idle1.getHeight();
        direction = "right";
        fallingSpeed = 7;
        sleepTimer = 3600;
        randomLickTick = 10;
        floor = mp.screenHeight - 40;
    }

    public void setPetImages()
    {
        try {
            File file = new File("src/main/java/Assets/idle/idle_1.png");
            idle1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/idle/idle_2.png");
            idle2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/idle/idle_left_1.png");
            idleLeft1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/idle/idle_left_2.png");
            idleLeft2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_1.png");
            walk1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_2.png");
            walk2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_3.png");
            walk3 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_4.png");
            walk4 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_left_1.png");
            walkLeft1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_left_2.png");
            walkLeft2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_left_3.png");
            walkLeft3 = ImageIO.read(file);
            file = new File("src/main/java/Assets/walk/walk_left_4.png");
            walkLeft4 = ImageIO.read(file);
            file = new File("src/main/java/Assets/grab/cat_grab.png");
            grab = ImageIO.read(file);
            file = new File("src/main/java/Assets/grab/cat_grab_2.png");
            grab2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/sleep/sleeping_0.png");
            sleeping0 = ImageIO.read(file);
            file = new File("src/main/java/Assets/sleep/sleeping_1.png");
            sleeping1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/sleep/sleeping_2.png");
            sleeping2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/sleep/sleeping_Left_0.png");
            sleepingLeft0 = ImageIO.read(file);
            file = new File("src/main/java/Assets/sleep/sleeping_Left_1.png");
            sleepingLeft1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/sleep/sleeping_Left_2.png");
            sleepingLeft2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/stretch/stretching_0.png");
            stretching0 = ImageIO.read(file);
            file = new File("src/main/java/Assets/stretch/stretching_Left_0.png");
            stretchingLeft0 = ImageIO.read(file);
            file = new File("src/main/java/Assets/idle/sit_1.png");
            sit1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/idle/sit_2.png");
            sit2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/idle/sit_Left_1.png");
            sitLeft1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/idle/sit_Left_2.png");
            sitLeft2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/lick/cat_lick_1.png");
            lick1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/lick/cat_lick_2.png");
            lick2 = ImageIO.read(file);
            file = new File("src/main/java/Assets/lick/cat_lick_Left_1.png");
            lickLeft1 = ImageIO.read(file);
            file = new File("src/main/java/Assets/lick/cat_lick_Left_2.png");
            lickLeft2 = ImageIO.read(file);
            //jump sprites
            file = new File("src/main/java/Assets/jump/cat_jump.png");
            jump = ImageIO.read(file);
            file = new File("src/main/java/Assets/jump/cat_jump_Left.png");
            jumpLeft = ImageIO.read(file);
            file = new File("src/main/java/Assets/jump/cat_fall.png");
            fall = ImageIO.read(file);
            file = new File("src/main/java/Assets/jump/cat_fall_Left.png");
            fallLeft = ImageIO.read(file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(float dt) {
        xCollision = x+petWidth; yCollision = y+petHeight;

        // Time and Ticks
        counter++;
        if (counter % 30 == 0) {
            tick++;
            sleepTicker++;
        }
        spriteCounter++;
        if (spriteCounter > 30) {
            if (spriteNum == 1)  spriteNum = 2;
            else if (spriteNum == 2)  spriteNum = 3;
            else if (spriteNum == 3)  spriteNum = 4;
            else if (spriteNum == 4)  spriteNum = 1;
            spriteCounter = 0;
        }

        // For debugging
        if (counter % 30 == 0) {
            System.out.println("y: "+y);
            System.out.println("platform y: "+mp.platform.y);
//            System.out.println("Sprite Num: "+spriteNum);
//            System.out.println("Tick: "+tick);
//            System.out.println("SleepTicker: "+sleepTicker);
//            System.out.println("SleepCountdown"+(sleepTimer - sleepTicker));
        }

        if (status == "walk") {
            if (yCollision <= mp.platform.y && x <= mp.platform.x-(petWidth/2)) {
                onPlatform = false;
                status = "falling";
            }
        }
        // starts walking or sleeping
        if (status == "idle" && tick == randomNum) {
            if (random.nextInt(randomLickTick) <= 3) { //test only (delete later)
                status = "sit";
                tick = 0;
                randomNum = 2;
            }
            else if (sleepTicker > sleepTimer) {
                status = "startSleeping";
                tick = 0;
                sleepTicker = 0;
            } else if (random.nextInt(8) == 2) {
                status = "sit";
                tick = 0;
                randomNum = random.nextInt(30, 360);
            } else {
                status = "walk";
                tick = 0;
                randomNum = random.nextInt(3, 7);
                randomLickTick--;
            }
        }
        // idle after walking
        if (status == "walk" && tick == randomNum) {
            status = "idle";
            tick = 0;
            randomNum = random.nextInt(10, 30);
        }
        // start eating when pet collide with a full bowl
        if (status == "walk" && direction == "right" &&
                xCollision == bowl.x && bowl.foodStatus == "full") {
            status = "eat";
            tick = 0;
            randomNum = 20;
        }
        // start sleeping or walk after eating
        if (status == "eat" && tick == randomNum) {
            randomNum = random.nextInt(3, 7);
            // will start sleeping after eating
            if (randomNum == 4) {
                status = "startSleeping";
                tick = 0;
                sleepTicker = 0;
                bowl.setBowlEmpty();
                bowl.foodStatus = "empty";
            }
            // walking after eating
            else {
                status = "walk";
                tick = 0;
                bowl.setBowlEmpty();
                bowl.foodStatus = "empty";
            }
        }
        // if falling under the floor
        if (status == "falling") {
            floor = mp.screenHeight - 40;
            if (x + (petWidth/2) > mp.platform.x && yCollision >= mp.platform.y) {
                status = "tpToPlatform";
                onPlatform = true;
            }
            else if (yCollision >= floor) {
                status = "tpToFloorY";
            }
        }
        // tp the pet back to platform
        if (status == "tpToPlatform" && onPlatform) {
            y = mp.platform.y - petHeight;
            tick = 0;
            randomNum = random.nextInt(5, 15);
            status = "idle";
        }
        // tp the pet back to floor
        if (status == "tpToFloorY") {
            y = floor - idle1.getHeight();
            tick = 0;
            randomNum = random.nextInt(5, 15);
            status = "idle";
        }
        //if pet goes beyond frame
        if (status == "idle" && x < 0) {
            direction = "right";
            status = "walk";
            tick = 0;
            randomNum = 7;
        }
        //if pet goes beyond frame
        if ((status == "idle" &&
                x+idle1.getHeight() > mp.screenWidth)) {
            direction = "left";
            status = "walk";
            randomNum = 7;
        }
        // set sleep timer and the next sleepTimer
        if (status == "startSleeping" && tick == 1) {
            status = "sleeping";
            tick = 0;
            randomNum = random.nextInt(300, 2400);
            sleepTimer = 5000;
        }
        // stop sleeping
        if (status == "sleeping" && tick == randomNum) {
            status = "stopSleeping";
            tick = 0;
        }
        // idle after stopSleeping
        if (status == "stopSleeping" && tick == 1) {
            status = "idle";
            tick = 0;
            randomNum = random.nextInt(10, 30);
        }
        // lick or getting up from sitting
        if (status == "sit" && tick == randomNum) {
            if (randomNum == 2) {
                //pls delete later
                status = "lick";
                tick = 0;
                randomNum = random.nextInt(6, 10);
                randomLickTick = 10;
            } else {
                status = "idle";
                tick = 0;
                randomNum = 2;
            }
        }
        // sit from licking
        if (status == "lick" && tick == randomNum) {
            status = "sit";
            tick = 0;
            randomNum = random.nextInt(3, 7);
        }
        if (ball != null) {
            this.ball.update(dt, mp.screenWidth, mp.screenHeight);
        }
        jump();
        jumpToFalling();
    }

    @Override
    public void draw(Graphics2D g2) {
        //BufferedImage sprite = null;

        switch (status) {
            case "idle":
                if (direction == "right") {
                    if (spriteNum == 1 || spriteNum == 3) { sprite = idle1; }
                    if (spriteNum == 2 || spriteNum == 4) { sprite = idle2; }
                } else if (direction == "left") {
                    if (spriteNum == 1 || spriteNum == 3) { sprite = idleLeft1; }
                    if (spriteNum == 2 || spriteNum == 4) { sprite = idleLeft2; }
                }
                break;
            case "walk":
                if (direction == "right") {
                    x += speed;
                    if(spriteNum == 1){ sprite = walk1; }
                    if(spriteNum == 2){ sprite = walk2; }
                    if(spriteNum == 3){ sprite = walk3; }
                    if(spriteNum == 4){ sprite = walk4; }
                    if (xCollision == mp.screenWidth) { direction = "left"; }
                } else if (direction == "left") {
                    x -= speed;
                    if(spriteNum == 1){ sprite = walkLeft1; }
                    if(spriteNum == 2){ sprite = walkLeft2; }
                    if(spriteNum == 3){ sprite = walkLeft3; }
                    if(spriteNum == 4){ sprite = walkLeft4; }
                    if (x == 0) { direction = "right"; }
                }
                break;
            case "eat":
                //sprite needs to be changed
                if (spriteNum == 1 || spriteNum == 3) { sprite = idle1; }
                if (spriteNum == 2 || spriteNum == 4) { sprite = idle2; }
                break;
            case "grab":
                //sprite needs to be changed
                x = xMouseGrab;
                y = yMouseGrab;
                if (spriteNum == 1 || spriteNum == 3) { sprite = grab; }
                if (spriteNum == 2 || spriteNum == 4) { sprite = grab2; }
                break;
            case "falling":
                y += fallingSpeed;
                if (direction == "right") {
                    sprite = fall;
                } else if (direction == "left") {
                    sprite = fallLeft;
                }
                break;
            case "startSleeping": // case "startSleeping" and
            case "stopSleeping":  // stop "stopSleeping" are merged
                if (direction == "right") {sprite = sleeping0;}
                else if (direction == "left") {sprite = sleepingLeft0;}
                break;
            case "sleeping":
                if (direction == "right") {
                    if (spriteNum == 1 || spriteNum == 2) { sprite = sleeping1; }
                    if (spriteNum == 3 || spriteNum == 4) { sprite = sleeping2; }
                } else if (direction == "left") {
                    if (spriteNum == 1 || spriteNum == 2) { sprite = sleepingLeft1; }
                    if (spriteNum == 3 || spriteNum == 4) { sprite = sleepingLeft2; }
                }
                break;
            case "sit":
                if (direction == "right") {
                    if (spriteNum == 1 || spriteNum == 3) { sprite = sit1; }
                    if (spriteNum == 2 || spriteNum == 4) { sprite = sit2; }
                } else if (direction == "left") {
                    if (spriteNum == 1 || spriteNum == 3) { sprite = sitLeft1; }
                    if (spriteNum == 2 || spriteNum == 4) { sprite = sitLeft2; }
                }
                break;
            case "lick":
                if (direction == "right") {
                    if (spriteNum == 1 || spriteNum == 3) { sprite = lick1; }
                    if (spriteNum == 2 || spriteNum == 4) { sprite = lick2; }
                } else if (direction == "left") {
                    if (spriteNum == 1 || spriteNum == 3) { sprite = lickLeft1; }
                    if (spriteNum == 2 || spriteNum == 4) { sprite = lickLeft2; }
                }
            case "jump":
                jumpAnimation();
                break;
        }
        g2.drawImage(sprite, x, y, idle1.getWidth(), idle1.getHeight(), null);
        g2.drawImage(bowl.sprite, bowl.x, bowl.y, bowl.bowl_full.getWidth(), bowl.bowl_full.getHeight(), null);
        if (ball != null) {
            ball.draw(g2);
        }
    }

    //jump function
    public void jump() {
        if (status == "walk"
                && xCollision > mp.platform.x
                && xCollision < mp.platform.x + 25
                && yCollision == mp.screenHeight - 40
                && randomNum == 5) {
            status = "jump";
            tick = 0;
            randomNum = random.nextInt(3, 7);
        }
    }
    //from jump to idle function
    public void jumpToFalling() {
        if (status == "jump" && this.y < mp.platform.y - 180) {
            status = "falling";
        }
    }
    //jump sprite animation
    public void jumpAnimation() {
        if (status == "jump") {
            if (direction == "right") {
                sprite = jump;
            } else if (direction == "left") {
                sprite = jumpLeft;
            }
            y -= fallingSpeed;
            x += fallingSpeed / 2;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        //miaw when clicked
        if (mouseX >= x && mouseX <= x + idle1.getWidth() &&
                mouseY >= y && mouseY <= y + idle1.getHeight()) {
            System.out.println("miaw");
        }
        //fill the bowl
        if (bowl.foodStatus == "empty") {
            if (mouseX >= bowl.x && mouseX <= bowl.x + bowl.bowl_empty.getWidth() &&
                    mouseY >= bowl.y && mouseY <= bowl.y + bowl.bowl_empty.getHeight()) {
                System.out.println("mmm food...");
                bowl.sprite = bowl.bowl_full;
                bowl.foodStatus = "full";
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            ball = new Ball (mp, 40,
                    new Vector2f(mouseX, mouseY),
                    new Vector2f(0, 0), 1);
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            //drag cat
            if (mouseX >= (x+32) && mouseX <= (x + idle1.getWidth()-32) &&
                    mouseY >= (y+32) && mouseY <= (y + idle1.getHeight()-32)
                    && direction == "right") {
                xMouseGrab = mouseX - 64;
                yMouseGrab = mouseY - 16;
                status = "grab";
            } else if (mouseX >= (x+32) && mouseX <= (x + idle1.getWidth()-32) &&
                    mouseY >= (y+32) && mouseY <= (y + idle1.getHeight()-32)
                    && direction == "left") {
                xMouseGrab = mouseX - 64;
                yMouseGrab = mouseY - 16;
                status = "grab";
            } else if (ball != null && (mouseX >= (ball.getPosition().x - (ball.getRadius()/2)) && mouseX <= (ball.getPosition().x + (ball.getRadius()/2)) &&
                    mouseY >= (ball.getPosition().y - (ball.getRadius()/2)) && mouseY <= (ball.getPosition().y + (ball.getRadius()/2)))) {
                ball.xMouseGrab = mouseX;
                ball.yMouseGrab = mouseY;
                ball.ballGrab = true;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1 && status == "grab") {
            if (yCollision < (mp.screenHeight - 40))
                status = "falling";
            else
                status = "tpToFloorY";
        }
        if (ball != null && e.getButton() == MouseEvent.BUTTON1 && ball.ballGrab) {
            ball.ballGrab = false;
            ball.acceleration.x = 0;
            ball.acceleration.y = 0;
            // set the ball's velocity to the calculated values
            ball.velocity.x = velX;
            ball.velocity.y = velY;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (status == "grab") {
            xMouseGrab = mouseX - 64;
            yMouseGrab = mouseY - 16;
        }
        if (ball != null && ball.ballGrab) {
            ball.xMouseGrab = mouseX;
            ball.yMouseGrab = mouseY;
            int dx = mouseX - lastMouseX;
            int dy = mouseY - lastMouseY;
            lastMouseX = mouseX;
            lastMouseY = mouseY;

            // calculate velocity based on change in mouse position
            velX = dx / 10.0f; // adjust scale as needed
            velY = dy / 10.0f;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
