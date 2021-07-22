public class Enemy {

    static int teemoHealth;
    static int screenWidth;
    static int bgscrollspeed;
    static int dartDamage = 1;
    static int grompsKilled = 0;
    static int wolvesKilled = 0;
    static int raptorsKilled = 0;

    int maxHealth;
    int health;
    int x;
    int y;
    int hitboxCorrection;
    int spawnDistance;
    double moveSpeed;

    int eWidth;
    int eHeight;
    
    Enemy (int teemoHp, int screenW, int bgscroll) {

        teemoHealth = teemoHp;
        screenWidth = screenW;
        bgscrollspeed = bgscroll;

    }

    Enemy(int hp, int x, int y, int width, int height, double moveSpeed, int spawnDistance, int hitboxCorrection) {

        maxHealth = hp;
        health = maxHealth;
        this.x = x;
        this.y = y;
        eWidth = width;
        eHeight = height;
        this.moveSpeed = moveSpeed;
        this.spawnDistance = spawnDistance;
        this.hitboxCorrection = hitboxCorrection;

    }

    public void collision (int teemox,  int teemoy) {

        if (teemoHealth > 0) {
            int teemoWidth = 100;
            int leftSide = x + hitboxCorrection;
            int rightSide = x + eWidth - hitboxCorrection;

            if (teemox < leftSide && teemox + teemoWidth > leftSide || teemox < rightSide && teemox + teemoWidth > rightSide || teemox > leftSide && teemox + teemoWidth < rightSide) {
                int teemoheight = 100;
                int topSide = y + hitboxCorrection;
                int botSide = y + eHeight - hitboxCorrection;

                if (teemoy + teemoheight >= topSide && teemoy <= topSide || teemoy <= botSide && teemoy + teemoheight >= botSide || teemoy >= topSide && teemoy + teemoheight <= botSide) {
                    teemoHealth -= 1;

                    if (teemoHealth > 0) {
                        x = -200;
                    }
                }
            }
        }

    }

    public void dartCollision (int teemox, int teemoy, Dart dart) {

        if (Enemy.teemoHealth > 0) {
            int dartWidth = 64;
            int leftSide = x + hitboxCorrection;
            int rightSide = x + eWidth - hitboxCorrection;
            if (dart.x < leftSide && dart.x + dartWidth > leftSide || dart.x < rightSide && dart.x + dartWidth > rightSide || dart.x > leftSide && dart.x + dartWidth < rightSide) {
                int dartHeight = 12;
                int topSide = y + hitboxCorrection;
                int botSide = y + eHeight - hitboxCorrection;

                if (dart.y + dartHeight >= topSide && dart.y <= topSide || dart.y <= botSide && dart.y + dartHeight >= botSide || dart.y >= topSide && dart.y + dartHeight <= botSide) {
                    health -= dartDamage;
                    dart.x = teemox + 25;
                    dart.y = teemoy + 40;
                    dart.isDartMoving = false;

                    if (health <= 0) {
                        switch(maxHealth) {
                            case 10:
                                grompsKilled++;
                                break;
                            case 5: 
                                wolvesKilled++;
                                break;
                            case 1: 
                                raptorsKilled++;
                                break;
                        }

                        x = -200;
                        health = maxHealth;
                    }
                }
            }
        }

    }

    public void movement () {

        if (x <= -200) {
            health = maxHealth;
            x = screenWidth + (int) (Math.random() * spawnDistance);
        } else {
            x = (int) (x + -bgscrollspeed * moveSpeed);
        }

    }

    public void behavior(int teemox, int teemoy, Dart dart1, Dart dart2, Dart dart3) {

        movement();
        collision(teemox, teemoy);
        dartCollision(teemox, teemoy, dart1);
        dartCollision(teemox, teemoy, dart2);
        dartCollision(teemox, teemoy, dart3);
        
    }
}
