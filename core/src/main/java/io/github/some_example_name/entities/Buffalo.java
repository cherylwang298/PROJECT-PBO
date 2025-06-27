////package io.github.some_example_name.entities;
////
////import com.badlogic.gdx.Gdx;
////import com.badlogic.gdx.graphics.Texture;
////import com.badlogic.gdx.graphics.g2d.SpriteBatch;
////import com.badlogic.gdx.math.Vector2;
////
////public class Buffalo extends Monsters {
////
////    private Texture front1, front2, right1, right2, left1, left2;
////    private Texture currentTexture;
////
////    private float directionX = 0f;
////    private float directionY = -1f;
////    private float knockbackForce = 150f;
////    private float knockbackCooldown = 1.5f;
////    private float knockbackTimer = 0f;
////
////    private float animationTimer = 0f;
////    private float animationSpeed = 0.2f;
////    private boolean useAltFrame = false;
////
////    private float exitX, exitY;
////    private float waypointX, waypointY;
////    private boolean useWaypoint = true;
////    private boolean reachedWaypoint = false;
////
////    public Buffalo(float x, float y) {
////        super(x, y);
////        this.health = 100;
////        this.speed = 80f;
////        this.damage = 10;
////        this.attackRadius = 80f;
////        this.isAggressive = true;
////
////        try {
////            front1 = new Texture(Gdx.files.internal("buffaloFront1.png"));
////            front2 = new Texture(Gdx.files.internal("buffaloFront2.png"));
////            right1 = new Texture(Gdx.files.internal("buffaloRight1.png"));
////            right2 = new Texture(Gdx.files.internal("buffaloRight2.png"));
////            left1 = new Texture(Gdx.files.internal("buffaloLeft1.png"));
////            left2 = new Texture(Gdx.files.internal("buffaloLeft2.png"));
////            currentTexture = front1;
////        } catch (Exception e) {
////            front1 = front2 = right1 = right2 = left1 = left2 = new Texture("placeholder.png");
////            currentTexture = front1;
////            Gdx.app.error("Buffalo", "Texture not found. Using placeholder.");
////        }
////
////        this.setSize(64, 64);
////        assignRandomExitWithWaypoint();
////    }
////
////    public Buffalo(float x, float y, float exitX, float exitY) {
////        this(x, y);
////        this.exitX = exitX;
////        this.exitY = exitY;
////    }
////
////    private void assignRandomExitWithWaypoint() {
////        float screenWidth = Gdx.graphics.getWidth();
////        float screenHeight = Gdx.graphics.getHeight();
////
////        exitX = screenWidth * 0.50f;
////        exitY = screenHeight * 0.08f;
////
////        int style = (int)(Math.random() * 3);
////        useWaypoint = true;
////        reachedWaypoint = false;
////
////        if (style == 0) {
////            float offsetX = (float)(Math.random() * 150 - 75);
////            waypointX = x + offsetX;
////            waypointY = y;
////        } else if (style == 1) {
////            waypointX = x;
////            waypointY = y - (float)(Math.random() * 80 + 40);
////        } else {
////            useWaypoint = false;
////            reachedWaypoint = true;
////        }
////    }
////
////    @Override
////    public void update(float deltaTime, Player player) {
////        animationTimer += deltaTime;
////        if (animationTimer >= animationSpeed) {
////            useAltFrame = !useAltFrame;
////            animationTimer = 0f;
////        }
////
////        float centerX = x + width / 2;
////        float centerY = y + height / 2;
////
////        Vector2 target = new Vector2(
////            (!useWaypoint || reachedWaypoint) ? exitX : waypointX,
////            (!useWaypoint || reachedWaypoint) ? exitY : waypointY
////        );
////
////        Vector2 move = new Vector2(target.x - centerX, target.y - centerY);
////
////        if (move.len() < 5f) {
////            if (useWaypoint && !reachedWaypoint) {
////                reachedWaypoint = true;
////            } else {
////                move.setZero();
////            }
////        } else {
////            move.nor();
////        }
////
////        x += move.x * speed * deltaTime;
////        y += move.y * speed * deltaTime;
////
////        directionX = move.x;
////        directionY = move.y;
////
////        if (isPlayerInFront(player)) {
////            knockbackTimer += deltaTime;
////            if (knockbackTimer >= knockbackCooldown) {
////                applyKnockback(player);
////                knockbackTimer = 0f;
////            }
////        }
////    }
////
////    private boolean isPlayerInFront(Player player) {
////        float px = player.getX() + player.getWidth() / 2;
////        float py = player.getY() + player.getHeight() / 2;
////
////        float bx = x + width / 2;
////        float by = y + height / 2;
////
////        Vector2 toPlayer = new Vector2(px - bx, py - by);
////        return toPlayer.len() <= attackRadius && Math.abs(toPlayer.angleDeg()) < 45;
////    }
////
////    private boolean isPlayerOnSide(Player player) {
////        float px = player.getX() + player.getWidth() / 2;
////        float bx = x + width / 2;
////        return Math.abs(px - bx) > width * 0.3f;
////    }
////
////    private void applyKnockback(Player player) {
////        Vector2 push = new Vector2(player.getX() - x, player.getY() - y).nor().scl(knockbackForce);
////        player.getPosition().add(push);
////        Gdx.app.log("Buffalo", "Knockback applied to player!");
////    }
////
////    @Override
////    public void onHit(Player player) {
////        if (isPlayerOnSide(player)) {
////            health -= 10;
////            Gdx.app.log("Buffalo", "Player hit from side. Health now: " + health);
////            if (health <= 0) kill();
////        } else {
////            Gdx.app.log("Buffalo", "Player attack from front blocked (knockback).");
////        }
////    }
////
////    @Override
////    public boolean shouldAttackPlayer(Player player) {
////        return true;
////    }
////
////    @Override
////    public void render(SpriteBatch batch) {
////        Texture currentFrame = front1;
////
////        if (directionY < 0) {
////            currentFrame = useAltFrame ? front2 : front1;
////        } else if (directionX > 0) {
////            currentFrame = useAltFrame ? right2 : right1;
////        } else if (directionX < 0) {
////            currentFrame = useAltFrame ? left2 : left1;
////        }
////
////        batch.draw(currentFrame, x, y, width, height);
////    }
////
////    @Override
////    public void dispose() {
////        front1.dispose(); front2.dispose();
////        right1.dispose(); right2.dispose();
////        left1.dispose(); left2.dispose();
////    }
////
////    @Override
////    public void setSize(float width, float height) {
////        this.width = width;
////        this.height = height;
////    }
////
////    @Override
////    public boolean hasReachedCity() {
////        return y <= 50;
////    }
////
////    @Override
////    public void kill() {
////        this.health = 0;
////    }
////}
//
//
//package io.github.some_example_name.entities;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//
//public class Buffalo extends Monsters {
//
//    private Texture front1, front2, right1, right2, left1, left2;
//    private Texture currentTexture;
//
//    private float directionX = 0f;
//    private float directionY = -1f;
//    private float knockbackForce = 150f;
//    private float knockbackCooldown = 1.5f;
//    private float knockbackTimer = 0f;
//
//    private float animationTimer = 0f;
//    private float animationSpeed = 0.2f;
//    private boolean useAltFrame = false;
//
//    private float exitX, exitY;
//    private float waypointX, waypointY;
//    private boolean useWaypoint = true;
//    private boolean reachedWaypoint = false;
//
//    public Buffalo(float x, float y) {
//        super(x, y);
//        this.health = 250;
//        this.maxHealth=250; //buat maxhealth healthbar
//        this.speed = 35f;
//        this.damageToplayer = 50; //cheryl: edit, ini belum di panggil ye
//        this.damageTocity = 1;
//        this.attackRadius = 80f;
//        this.isAggressive = true;
//
//        try {
//            front1 = new Texture(Gdx.files.internal("buffaloFront1.png"));
//            front2 = new Texture(Gdx.files.internal("buffaloFront2.png"));
//            right1 = new Texture(Gdx.files.internal("buffaloRight1.png"));
//            right2 = new Texture(Gdx.files.internal("buffaloRight2.png"));
//            left1 = new Texture(Gdx.files.internal("buffaloLeft1.png"));
//            left2 = new Texture(Gdx.files.internal("buffaloLeft2.png"));
//            currentTexture = front1;
//        } catch (Exception e) {
//            front1 = front2 = right1 = right2 = left1 = left2 = new Texture("placeholder.png");
//            currentTexture = front1;
//            Gdx.app.error("Buffalo", "Texture not found. Using placeholder.");
//        }
//
//        this.setSize(215, 215);
//    }
//
//    public Buffalo(float x, float y, float exitX, float exitY) {
//        this(x, y);
//        this.exitX = exitX;
//        this.exitY = exitY;
//        assignRandomExitWithWaypoint(); // hanya dipanggil di constructor ini
//        Gdx.app.log("Buffalo", "Spawned at (" + x + ", " + y + ") → (" + exitX + ", " + exitY + ")");
//    }
//
//    private void assignRandomExitWithWaypoint() {
//        int style = (int)(Math.random() * 3);
//        useWaypoint = true;
//        reachedWaypoint = false;
//
//        if (style == 0) {
//            float offsetX = (float)(Math.random() * 150 - 75);
//            waypointX = x + offsetX;
//            waypointY = y;
//        } else if (style == 1) {
//            waypointX = x;
//            waypointY = y - (float)(Math.random() * 80 + 40);
//        } else {
//            useWaypoint = false;
//            reachedWaypoint = true;
//        }
//    }
//
//    @Override
//    public void update(float deltaTime, Player player) {
//        animationTimer += deltaTime;
//        if (animationTimer >= animationSpeed) {
//            useAltFrame = !useAltFrame;
//            animationTimer = 0f;
//        }
//
//        float centerX = x + width / 2;
//        float centerY = y + height / 2;
//
//        Vector2 target = new Vector2(
//            (!useWaypoint || reachedWaypoint) ? exitX : waypointX,
//            (!useWaypoint || reachedWaypoint) ? exitY : waypointY
//        );
//
//        Vector2 move = new Vector2(target.x - centerX, target.y - centerY);
//
//        if (move.len() < 5f) {
//            if (useWaypoint && !reachedWaypoint) {
//                reachedWaypoint = true;
//            } else {
//                move.setZero();
//            }
//        } else {
//            move.nor();
//        }
//
//        x += move.x * speed * deltaTime;
//        y += move.y * speed * deltaTime;
//
//        directionX = move.x;
//        directionY = move.y;
//
//        if (isPlayerInFront(player)) {
//            knockbackTimer += deltaTime;
//            if (knockbackTimer >= knockbackCooldown) {
//                applyKnockback(player);
//                knockbackTimer = 0f;
//            }
//        }
//    }
//
//    private boolean isPlayerInFront(Player player) {
//        float px = player.getX() + player.getWidth() / 2;
//        float py = player.getY() + player.getHeight() / 2;
//
//        float bx = x + width / 2;
//        float by = y + height / 2;
//
//        Vector2 toPlayer = new Vector2(px - bx, py - by);
//        return toPlayer.len() <= attackRadius && Math.abs(toPlayer.angleDeg()) < 45;
//    }
//
//    private boolean isPlayerOnSide(Player player) {
//        float px = player.getX() + player.getWidth() / 2;
//        float bx = x + width / 2;
//        return Math.abs(px - bx) > width * 0.3f;
//    }
//
//    private void applyKnockback(Player player) {
//        Vector2 push = new Vector2(player.getX() - x, player.getY() - y).nor().scl(knockbackForce);
//        player.getPosition().add(push);
//        Gdx.app.log("Buffalo", "Knockback applied to player!");
//    }
//
//    @Override
//    public void onHit(Player player) {
//        if (isPlayerOnSide(player)) {
//            health -= 10;
//            Gdx.app.log("Buffalo", "Player hit from side. Health now: " + health);
//            if (health <= 0) kill();
//        } else {
//            Gdx.app.log("Buffalo", "Player attack from front blocked (knockback).");
//        }
//    }
//
//    @Override
//    public boolean shouldAttackPlayer(Player player) {
//        return true;
//    }
//
//    @Override
//    public void render(SpriteBatch batch) {
//        Texture currentFrame = front1;
//        if (directionY < 0) {
//            currentFrame = useAltFrame ? front2 : front1;
//        } else if (directionX > 0) {
//            currentFrame = useAltFrame ? right2 : right1;
//        } else if (directionX < 0) {
//            currentFrame = useAltFrame ? left2 : left1;
//        }
//
//        if (currentFrame != null) {
//            batch.draw(currentFrame, x, y, width, height);
//        }
//    }
//
//    @Override
//    public void dispose() {
//        front1.dispose(); front2.dispose();
//        right1.dispose(); right2.dispose();
//        left1.dispose(); left2.dispose();
//    }
//
//    @Override
//    public void setSize(float width, float height) {
//        this.width = width;
//        this.height = height;
//    }
//
//    @Override
//    public boolean hasReachedCity() {
//        return y <= 50;
//    }
//
//    @Override
//    public void kill() {
//        this.health = 0;
//    }
//}


package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Buffalo extends Monsters {

    /* ---------- konstanta perilaku ---------- */
    private static final float base_speed          = 35f;
    private static final float knockback_force     = 180f;   // seberapa jauh player terdorong
    private static final float knockback_cd        = 0.7f;   // cooldown antar knock-back (detik)
    private static final float attack_arc_deg      = 50f;    // sudut "depan" (semakin besar → area depan makin lebar)
    private static final float attack_radius       = 85f;    // jarak maksimal bisa knock-back

    /* ---------- asset ---------- */
    private Texture f1,f2,r1,r2,l1,l2;

    /* ---------- anim & gerak ---------- */
    private float animTimer   = 0f;
    private final float anim_frame = 0.22f;
    private boolean altFrame = false;
    private float dirX = 0, dirY = -1;

    /* ---------- knock-back ---------- */
    private float knockbackTimer = knockback_cd;   // start “siap knockback” segera

    /* ---------- jalur (way-point / exit) ---------- */
    private final Vector2 exit = new Vector2();      // pintu keluar
    private final Vector2 waypoint = new Vector2();  // titik tengah (optional)
    private boolean useWaypoint = true;
    private boolean reachedWaypoint = false;

    /* ============================================================= */
    public Buffalo(float x,float y,float exitX,float exitY){
        super(x,y);
        /* stat */
        this.health       = 250;
        this.maxHealth    = 250;
        this.speed        =base_speed;
        this.damageToplayer = 50;    // kalau suatu saat mau dipakai
        this.damageTocity  = 1;
        this.attackRadius  = attack_radius;
        this.isAggressive  = true;

        /* load texture */
        try{
            f1=new Texture("buffaloFront1.png");  f2=new Texture("buffaloFront2.png");
            r1=new Texture("buffaloRight1.png");  r2=new Texture("buffaloRight2.png");
            l1=new Texture("buffaloLeft1.png");   l2=new Texture("buffaloLeft2.png");
        }catch(Exception e){
            f1=f2=r1=r2=l1=l2=new Texture("placeholder.png");
            Gdx.app.error("Buffalo","Texture not found, using placeholder.");
        }

        setSize(215,215);   // sprite besar – sesuaikan health-bar di renderHealthBar()

        /* simpan tujuan */
        this.exit.set(exitX,exitY);
        assignRandomWaypoint();

        Gdx.app.log("Buffalo","Spawned ("+x+","+y+")");
    }

    /* ============================================================= */
    /* waypoint random kecil biar jalur bervariasi */
    private void assignRandomWaypoint(){
        int style = (int)(Math.random()*3);
        useWaypoint = style!=2;
        reachedWaypoint = !useWaypoint;

        if(style==0){
            waypoint.set(x + (float)(Math.random()*150-75), y);
        }else if(style==1){
            waypoint.set(x, y - (float)(Math.random()*80+40));
        }
    }

    /* ============================================================= */
    @Override public void update(float dt, Player player){
        /* === animasi sederhana === */
        animTimer += dt;
        if(animTimer>=anim_frame){ animTimer=0; altFrame=!altFrame; }

        /* === gerak normal (menuju waypoint / exit) === */
        Vector2 target = reachedWaypoint? exit: waypoint;
        Vector2 move   = new Vector2(target.x - (x+width/2f),
            target.y - (y+height/2f));

        if(move.len() < 4f){
            if(!reachedWaypoint && useWaypoint)  reachedWaypoint=true;
            else move.setZero();
        }else move.nor();

        x += move.x * speed * dt;
        y += move.y * speed * dt;

        dirX = move.x; dirY = move.y;

        /* === knock-back check === */
        knockbackTimer += dt;
        if(knockbackTimer >= knockback_cd && isPlayerInFront(player)){
            applyKnockback(player);
            knockbackTimer = 0;
        }
    }

    /* ============================================================= */
    /** player berada di “kerucut” depan & radius tertentu */
    private boolean isPlayerInFront(Player p){
        Vector2 toPlayer = new Vector2(
            (p.getX()+p.getWidth()/2f)  - (x+width/2f),
            (p.getY()+p.getHeight()/2f) - (y+height/2f)
        );
        float dist = toPlayer.len();
        if(dist > attack_radius) return false;

        toPlayer.nor();
        Vector2 facing = new Vector2(dirX,dirY).nor(); // arah hadap Buffalo saat ini
        float angle = facing.dot(toPlayer);            // cos(θ)
        float threshold = (float)Math.cos(Math.toRadians(attack_arc_deg/2f));
        return angle > threshold;                      // true jika sudut kecil (depan)
    }

    private void applyKnockback(Player p){
        Vector2 push = new Vector2(
            (p.getX()+p.getWidth()/2f)  - (x+width/2f),
            (p.getY()+p.getHeight()/2f) - (y+height/2f)
        ).nor().scl(knockback_force);

        /* asumsikan Player punya getPosition():Vector2 yg bisa di-mutate */
        p.getPosition().add(push);
        Gdx.app.log("Buffalo","Knock-back!");
    }

    /* ============================================================= */
    @Override public void onHit(Player p){
        /* hanya bisa diserang dari samping/belakang */
        if(!isPlayerInFront(p)){
            health -= 10;
            Gdx.app.log("Buffalo","Hit! HP = "+health);
            if(health<=0) kill();
        }else{
            Gdx.app.log("Buffalo","Front attack blocked.");
        }
    }

    /* ============================================================= */
    @Override public boolean shouldAttackPlayer(Player p){ return true; }

    @Override public void render(SpriteBatch batch){
        Texture frame = f1;      // default depan
        if(dirY<0)               frame = altFrame? f2 : f1;
        else if(dirX>0)          frame = altFrame? r2 : r1;
        else if(dirX<0)          frame = altFrame? l2 : l1;

        batch.draw(frame, x, y, width, height);
    }

    /* ===== boilerplate ===== */
    @Override public void dispose(){ f1.dispose();f2.dispose();r1.dispose();r2.dispose();l1.dispose();l2.dispose(); }
    @Override public void setSize(float w,float h){ this.width=w; this.height=h; }
    @Override public boolean hasReachedCity(){ return y<=50; }
    @Override public void kill(){ health = 0; }
}
