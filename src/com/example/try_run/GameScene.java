package com.example.try_run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.try_gameengine.action.MAction;
import com.example.try_gameengine.action.MAction2;
import com.example.try_gameengine.action.MovementAction;
import com.example.try_gameengine.action.MovementAtionController;
import com.example.try_gameengine.framework.ALayer;
import com.example.try_gameengine.framework.ALayer.LayerParam;
import com.example.try_gameengine.framework.ButtonLayer;
import com.example.try_gameengine.framework.ButtonLayer.OnClickListener;
import com.example.try_gameengine.framework.GameView;
import com.example.try_gameengine.framework.IGameController;
import com.example.try_gameengine.framework.IGameModel;
import com.example.try_gameengine.framework.ILayer;
import com.example.try_gameengine.framework.LabelLayer;
import com.example.try_gameengine.framework.Layer;
import com.example.try_gameengine.framework.LayerManager;
import com.example.try_gameengine.framework.Sprite;
import com.example.try_gameengine.scene.EasyScene;
import com.example.try_run.model.AppleFactory;
import com.example.try_run.model.Background;
import com.example.try_run.model.Panda;
import com.example.try_run.model.Panda.Status;
import com.example.try_run.model.Platform;
import com.example.try_run.model.PlatformFactory;
import com.example.try_run.model.ProtocolMainscreen;
import com.example.try_run.utils.AudioUtil;
import com.example.try_run.utils.CommonUtil;



public class GameScene extends EasyScene implements ButtonLayer.OnClickListener, ProtocolMainscreen{
    Panda panda;
    PlatformFactory platformFactory = new PlatformFactory(0, 0, false);
//    lazy var sound = SoundManager()
    Background bg = new Background(0, 0, false);
    AppleFactory appleFactory;
    LabelLayer scoreLab = new LabelLayer(0, 0, false); 
    LabelLayer appLab  = new LabelLayer(0, 0, false); 
    LabelLayer myLabel  = new LabelLayer(0, 0, false); 
    int appleNum = 0;
    
    float moveSpeed = 15.0f;
    float maxSpeed = 50.0f;
    float distance = 0.0f;
    float lastDis = 0.0f;
    float theY = 0.0f;
    boolean isLose = false;
	boolean isReadyToJump = false;

    
	public GameScene(Context context, String id, int level) {
		super(context, id, level);
		// TODO Auto-generated constructor stub
		this.addAutoDraw(bg);
		
        int skyColor = Color.argb(255, 113, 197, 207);
        this.setBackgroundColor(skyColor);
        scoreLab.getPaint().setTextAlign(Align.LEFT);
        scoreLab.setPosition(20, 150);
        scoreLab.setText("run: 0 km");
        this.addAutoDraw(scoreLab);
        
        appLab.getPaint().setTextAlign(Align.LEFT);
        appLab.setPosition(400, 150);
        appLab.setText("eat: apple");
        this.addAutoDraw(appLab);
        
        myLabel.setText("");
        myLabel.setTextSize(100);
        myLabel.setzPosition(100);
        myLabel.setAutoHWByText();
        LayerParam layerParam = new LayerParam();
        layerParam.setPercentageX(0.5f);
        layerParam.setEnabledPercentagePositionX(true);
        myLabel.setLayerParam(new LayerParam());
        myLabel.setPosition(CommonUtil.screenWidth/2, CommonUtil.screenHeight/2);
        myLabel.setAnchorPoint(0.5f, 0);
        this.addAutoDraw(myLabel);
        
//        self.physicsWorld.contactDelegate = self
//        self.physicsWorld.gravity = CGVectorMake(0, -5)
//        self.physicsBody = SKPhysicsBody(edgeLoopFromRect: self.frame)
//        self.physicsBody!.categoryBitMask = BitMaskType.scene
//        self.physicsBody!.dynamic = false
        
        panda = new Panda(210, 100, true);
        
        appleFactory = new AppleFactory(0, 0, CommonUtil.screenWidth, false);
        this.addAutoDraw(appleFactory);
        
//        self.addChild(panda)
        this.addAutoDraw(platformFactory);
        platformFactory.setScreenWdith(CommonUtil.screenWidth);
        platformFactory.setProtocolMainscreen(this);
        platformFactory.createPlatform(3, 0, CommonUtil.screenHeight - 400);
        
//        platformFactory.createPlatformRandom();
        
        
        
//        self.addChild(sound)
        AudioUtil.playBackgroundMusic();
        
        isEnableRemoteController(false);
        
//        appleFactory.onInit(self.frame.width, y: theY)
//        self.addChild( appleFactory )
			

        
//        myImageView= new Layer(10, 10, true);
//        myImageView.setBitmapAndAutoChangeWH(BitmapUtil.common);
//        myTextView = new LabelLayer("hello",150, 350, true);
//        myTextView.setPosition(150, 150);
//        myTextView.setTextColor(Color.WHITE);
//        bath = new ButtonLayer("bath", 150, 350, true);
//        bath.setPosition(100, 550);
//        bath.setTextColor(Color.WHITE);
//        engage = new ButtonLayer("engage", 150, 350, true);
//        engage.setPosition(250, 550);
//        engage.setTextColor(Color.WHITE);
//        find = new ButtonLayer("find", 150, 350, true);
//        find.setPosition(400, 550);
//        find.setTextColor(Color.WHITE);
//        store = new ButtonLayer("find", 150, 350, true);
//        store.setPosition(550, 550);
//        store.setTextColor(Color.WHITE);
//        fight = new ButtonLayer("fight", 150, 350, false);
//        fight.setPosition(700, 550);
//        fight.setTextColor(Color.WHITE);
//        ui = new ButtonLayer("ui", 150, 350, true);
//        ui.setPosition(850, 550);
//        ui.setTextColor(Color.WHITE);
//        
//        bath.setOnClickListener(this);//�s�W��ť
//        engage.setOnClickListener(this);//�s�W��ť
//        find.setOnClickListener(this);//�s�W��ť
//        
//        store.setOnClickListener(this);
//        fight.setOnClickListener(this);
//        ui.setOnClickListener(this);
        
//        gameDog = new GameDog(this, 100, 100 , true);
//        actionThread = new ActionThread(this);
//        actionThread.start();//�Ұʫ�O�����

	}
	GameView gameView;
	
	
void checkCollistion(){
	
	for(Platform platform : platformFactory.getPlatforms()){
		
//	if(!panda.getFrame().equals(panda.getCollisionRectF()))
//		Log.e("NOT Equel", panda.getFrame() + ":" + panda.getCollisionRectF());
	
//	Log.e("checkCollistion", panda.getCollisionRectF() + ":" + platform.getFrame());
	if(platform.isEnable() && RectF.intersects(panda.getCollisionRectF(), platform.getFrame())){
//        if (contact.bodyA.categoryBitMask | contact.bodyB.categoryBitMask) == (BitMaskType.platform | BitMaskType.panda){
//		Log.e("Collistion", panda.getCollisionRectF() + ":" + platform.getFrame());
        boolean isDown = false;
        boolean canRun = false;
        
        panda.setY(platform.getY()-panda.h);
        
            if (platform.isDown) {
                isDown = true;
                platform.setEnable(false);
                platform.setHidden(true);
//                platform.physicsBody!.dynamic = true
//                platform.physicsBody!.collisionBitMask = 0
            }else if (platform.isShock) {
            	platform.isShock = false;
//                downAndUp(platform, 50, 0.2f, -100, 1, true);
            }
            if (panda.getY() < platform.getY()) {
                canRun=true;
            }
            

        
	        panda.jumpEnd = panda.getY();
//	        if (panda.jumpEnd-panda.jumpStart <= -70) {
	        if (panda.jumpEnd-panda.jumpStart >= 70) {
	            panda.roll();
	            AudioUtil.playRoll();
	            
	            if (!isDown) {
	            	downAndUp(panda,50,0.05f,-50,0.1f,false);
	                downAndUp(platform,50,0.05f,-50,0.1f,false);
	                Log.e("Run", "DownUp");
	            }
	            
	        }else{
	            if (canRun) {
	                panda.run();
	            }
	            
	        }
	        
	      //落地后jumpstart数据要设为当前位置，防止自由落地计算出错
	        panda.jumpStart = panda.getY();
	        		break;
		}
	}
	
	//熊猫和苹果碰撞
	for(Sprite apple : appleFactory.arrApple){
		if (apple.isEnable() && RectF.intersects(panda.getCollisionRectF(), apple.getFrame())){
		    AudioUtil.playEat();
		    this.appleNum++;
		    apple.setHidden(true);
		}
	}

}

public void downAndUp(final Sprite sprite,float down, float downTime, float up, float upTime, boolean isRepeat){
    MovementAction downAct = MAction.moveByY(down, (long)(downTime*1000));
//    downAct.setTimerOnTickListener(new MovementAction.TimerOnTickListener() {
//		
//		@Override
//		public void onTick(float dx, float dy) {
//			// TODO Auto-generated method stub
//			sprite.move(dx, dy);
//		}
//	});
    //moveByX(CGFloat(0), y: down, duration: downTime)
    MovementAction upAct = MAction.moveByY(up, (long)(upTime*1000));
//    upAct.setTimerOnTickListener(new MovementAction.TimerOnTickListener() {
//		
//		@Override
//		public void onTick(float dx, float dy) {
//			// TODO Auto-generated method stub
//			sprite.move(dx, dy);
//		}
//	});
    
    //MAction use threadPool it would delay during action by action.
    MovementAction downUpAct = MAction2.sequence(new MovementAction[]{downAct,upAct});
    downUpAct.setMovementActionController(new MovementAtionController());
    if (isRepeat) {
    	sprite.runMovementActionAndAppend(MAction.repeatForever(downUpAct));
    }else {
    	sprite.runMovementActionAndAppend(downUpAct);
    }
    
    
}

//	ALayer myImageView = null;//ImageView������ޥ�
//	LabelLayer myTextView = null;//TextView������ޥ�
//	ButtonLayer bath = null;//�~�����s
//	ButtonLayer engage = null;//�r�˫��s
//	ButtonLayer find = null;//�M����s 
//	ButtonLayer store, fight, ui;
//	static GameDog gameDog;//GameDog���ޥ�
//	ActionThread actionThread;//��O�����ޥ�
	
//	 public void didBeginContact(contact: SKPhysicsContact){
//	        
//	        //熊猫和苹果碰撞
//	        if (contact.bodyA.categoryBitMask | contact.bodyB.categoryBitMask) == (BitMaskType.apple | BitMaskType.panda){
//	            sound.playEat()
//	            self.appleNum++
//	            if contact.bodyA.categoryBitMask == BitMaskType.apple {
//	                contact.bodyA.node!.hidden = true
//	            }else{
//	                contact.bodyB.node!.hidden = true
//	            }
//	            
//	            
//	        }
//	        
//	        //熊猫和台子碰撞
//	        if (contact.bodyA.categoryBitMask | contact.bodyB.categoryBitMask) == (BitMaskType.platform | BitMaskType.panda){
//	            var isDown = false
//	            var canRun = false
//	            if contact.bodyA.categoryBitMask == BitMaskType.platform {
//	                if (contact.bodyA.node as! Platform).isDown {
//	                    isDown = true
//	                    contact.bodyA.node!.physicsBody!.dynamic = true
//	                    contact.bodyA.node!.physicsBody!.collisionBitMask = 0
//	                }else if (contact.bodyA.node as! Platform).isShock {
//	                    (contact.bodyA.node as! Platform).isShock = false
//	                    downAndUp(contact.bodyA.node!, down: -50, downTime: 0.2, up: 100, upTime: 1, isRepeat: true)
//	                }
//	                if contact.bodyB.node!.position.y > contact.bodyA.node!.position.y {
//	                    canRun=true
//	                }
//	                
//	            }else if contact.bodyB.categoryBitMask == BitMaskType.platform  {
//	                if (contact.bodyB.node as! Platform).isDown {
//	                    contact.bodyB.node!.physicsBody!.dynamic = true
//	                    contact.bodyB.node!.physicsBody!.collisionBitMask = 0
//	                    isDown = true
//	                }else if (contact.bodyB.node as! Platform).isShock {
//	                    (contact.bodyB.node as! Platform).isShock = false
//	                    downAndUp(contact.bodyB.node!, down: -50, downTime: 0.2, up: 100, upTime: 1, isRepeat: true)
//	                }
//	                if contact.bodyA.node!.position.y > contact.bodyB.node!.position.y {
//	                    canRun=true
//	                }
//	                
//	            }
//	            
//	            panda.jumpEnd = panda.position.y
//	            if panda.jumpEnd-panda.jumpStart <= -70 {
//	                panda.roll()
//	                sound.playRoll()
//	                
//	                if !isDown {
//	                    downAndUp(contact.bodyA.node!)
//	                    downAndUp(contact.bodyB.node!)
//	                }
//	                
//	            }else{
//	                if canRun {
//	                    panda.run()
//	                }
//	                
//	            }
//	        }
//	        
//	        if (contact.bodyA.categoryBitMask | contact.bodyB.categoryBitMask) == (BitMaskType.scene | BitMaskType.panda) {
//	            println("game over")
//	            myLabel.text = "game over";
//	            sound.playDead()
//	            isLose = true
//	            sound.stopBackgroundMusic()
//	            
//
//	        }
//	        
//	        //落地后jumpstart数据要设为当前位置，防止自由落地计算出错
//	        panda.jumpStart = panda.position.y
//	    }
//
//	 public void didEndContact(contact: SKPhysicsContact){
//	        panda.jumpStart = panda.position.y
//	        
//	    }
//	 public void downAndUp(node :SKNode,down:CGFloat = -50,downTime:CGFloat=0.05,up:CGFloat=50,upTime:CGFloat=0.1,isRepeat:Bool=false){
//	        let downAct = SKAction.moveByX(0, y: down, duration: Double(downTime))
//	        //moveByX(CGFloat(0), y: down, duration: downTime)
//	        let upAct = SKAction.moveByX(0, y: up, duration: Double(upTime))
//	        let downUpAct = SKAction.sequence([downAct,upAct])
//	        if isRepeat {
//	            node.runAction(SKAction.repeatActionForever(downUpAct))
//	        }else {
//	            node.runAction(downUpAct)
//	        }
//	        
//	        
//	    }
//	    
//	    
//	 public void touchesBegan(touches: Set<NSObject>, withEvent event: UIEvent) {
//	        if isLose {
//	            reSet()
//	        }else{
//	            if panda.status != Status.jump2 {
//	                sound.playJump()
//	            }
//	            panda.jump()
//	        }        
//	    }
//	
//    //重新开始游戏
//    func reSet(){
//        isLose = false
//        panda.position = CGPointMake(200, 400)
//        myLabel.text = ""
//        moveSpeed  = 15.0
//        distance = 0.0
//        lastDis = 0.0
//        self.appleNum = 0
//        platformFactory.reset()
//        appleFactory.reSet()
//        platformFactory.createPlatform(3, x: 0, y: 200)
//        sound.playBackgroundMusic()
//    }
//    override func update(currentTime: CFTimeInterval) {
//        if isLose {
//            
//        }else{
//            if panda.position.x < 200 {
//                var x = panda.position.x + 1
//                panda.position = CGPointMake(x, panda.position.y)
//            }
//            distance += moveSpeed
//            lastDis -= moveSpeed
//            var tempSpeed = CGFloat(5 + Int(distance/2000))
//            if tempSpeed > maxSpeed {
//                tempSpeed = maxSpeed
//            }
//            if moveSpeed < tempSpeed {
//                moveSpeed = tempSpeed
//            }
//            
//            if lastDis < 0 {
//                platformFactory.createPlatformRandom()
//            }
//            distance += moveSpeed
//            scoreLab.text = "run: \(Int(distance/1000*10)/10) km"
//            appLab.text = "eat: \(appleNum) apple"
//            platformFactory.move(moveSpeed)
//            bg.move(moveSpeed/5)
//            appleFactory.move(moveSpeed)
//        }
//        
//    }
//    
//    func onGetData(dist:CGFloat,theY:CGFloat){
//        
//        self.lastDis = dist
//        self.theY = theY
//        appleFactory.theY = theY
//    }

	@Override
	public void initGameView(Activity activity, IGameController gameController,
			IGameModel gameModel) {
		// TODO Auto-generated method stub
		gameView = new GameView(activity, gameController, gameModel);
	}

	public void action(){
//		gameDog.alone();
	}
	
	@Override
	public boolean onSceneTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if (isLose) {
	            reSet();
	        }else{
	            if (panda.status != Status.jump2) {
	            	AudioUtil.playJump();
	            }
	            isReadyToJump = true;
	        }
		}
		
		return super.onSceneTouchEvent(event);
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		if(event.getAction()==MotionEvent.ACTION_DOWN){
//			if (isLose) {
//	            reSet();
//	        }else{
//	            if (panda.status != Status.jump2) {
//	            	AudioUtil.playJump();
//	            }
//	            isReadyToJump = true;
//	        }
//		}
//		LayerManager.getInstance().onTouchLayers(event);
//		return true;
//	}
	
	private void checkGameOver(){
	    if (panda.getX() + panda.w < 0 || panda.getY() > CommonUtil.screenHeight) {
		    System.out.println("game over");
		    myLabel.setText("game over");
		    AudioUtil.playDead();
		    isLose = true;
		    AudioUtil.stopBackgroundMusic();
	    }
	}
	
	//重新开始游戏
	public void reSet(){
        isLose = false;
        panda.setPosition(200, 400);
        panda.reset();
        myLabel.setText("");
        moveSpeed  = 15.0f;
        distance = 0.0f;
        lastDis = 0.0f;
        appleNum = 0;
        platformFactory.reset();
        appleFactory.reSet();
        platformFactory.createPlatform(3, 0, 400);
        AudioUtil.playBackgroundMusic();
    }
	
	@Override
	public void process() {
		// TODO Auto-generated method stub
		if (isLose) {
			//do nothing
        }else{
        	LayerManager.getInstance().processLayers();
        	
            if (panda.getX() < 200 && !panda.isDisableAutoForward) {
                float x = panda.getX() + 1;
                panda.setX(x);
            }
            panda.isDisableAutoForward = false;
            distance += moveSpeed;
            lastDis -= moveSpeed;
            float tempSpeed = 5+(int)(distance/2000);
            if (tempSpeed > maxSpeed) {
                tempSpeed = maxSpeed;
            }
            if (moveSpeed < tempSpeed) {
                moveSpeed = tempSpeed;
            }
            
            if (lastDis < 0) {
                platformFactory.createPlatformRandom();
            }
            distance += moveSpeed;
            int runKM = (int)((distance/1000*10)/10);
            scoreLab.setText("run: " + runKM + "km");
            appLab.setText("eat: " + appleNum + "apple");
            platformFactory.move(moveSpeed, panda);
            bg.move(moveSpeed/5);
            appleFactory.move(moveSpeed);
            appleFactory.process();
            
            checkCollistion();
            
            boolean isOnGround = false;
            for(Platform platform : platformFactory.getPlatforms()){
            	if(!platform.isHidden() && panda.getY()==platform.getY()-panda.h && platform.getX() <= panda.getCollisionRectF().left + panda.getCollisionRectF().width() && platform.getX() + platform.w >= panda.getCollisionRectF().left){
            		isOnGround = true;
            		break;
            	}
            }
            if(!isOnGround)
            	panda.down();
            
            if(isReadyToJump){
            	panda.jump(platformFactory);
            	isReadyToJump = false;
            }
            
            checkGameOver();
        }
	}
	
	@Override
	public void onGetData(float dist, float theY) {
		// TODO Auto-generated method stub
		this.lastDis = dist;
		this.theY = theY;
        appleFactory.theY = theY;
	}

	@Override
	public void doDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		LayerManager.getInstance().drawLayers(canvas, null);
		
//		fight.drawSelf(canvas, null);
		
		super.doDraw(canvas);
	}

	@Override
	public void beforeGameStart() {
		// TODO Auto-generated method stub
		setWidth(gameView.getWidth());
		setHeight(gameView.getHeight());
	}

	@Override
	public void arrangeView(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActivityContentView(Activity activity) {
		// TODO Auto-generated method stub
		activity.setContentView(gameView);
	}

	@Override
	public void afterGameStart() {
		// TODO Auto-generated method stub
		Log.e("game scene", "game start");
		AudioUtil.playBackgroundMusic();
	}
	
	@Override
	protected void beforeGameStop() {
		// TODO Auto-generated method stub
		Log.e("game scene", "game stop");
		AudioUtil.stopBackgroundMusic();
	}
	
	@Override
	protected void afterGameStop() {
		// TODO Auto-generated method stub
//		AudioUtil.stopBackgroundMusic();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(ButtonLayer buttonLayer) {
		// TODO Auto-generated method stub

	}


}
