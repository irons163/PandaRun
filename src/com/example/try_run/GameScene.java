package com.example.try_run;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.try_gameengine.action.MAction;
import com.example.try_gameengine.action.MAction2;
import com.example.try_gameengine.action.MovementAction;
import com.example.try_gameengine.action.MovementAtionController;
import com.example.try_gameengine.framework.ButtonLayer;
import com.example.try_gameengine.framework.GameView;
import com.example.try_gameengine.framework.IGameController;
import com.example.try_gameengine.framework.IGameModel;
import com.example.try_gameengine.framework.LabelLayer;
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
	GameView gameView;
    Panda panda;
    PlatformFactory platformFactory = new PlatformFactory(0, 0, false);
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
        
        panda = new Panda(210, 100, true);
        appleFactory = new AppleFactory(0, 0, CommonUtil.screenWidth, false);
        this.addAutoDraw(appleFactory);
        
        this.addAutoDraw(platformFactory);
        platformFactory.setScreenWdith(CommonUtil.screenWidth);
        platformFactory.setProtocolMainscreen(this);
        platformFactory.createPlatform(3, 0, CommonUtil.screenHeight - 400);
        
        AudioUtil.playBackgroundMusic();
        isEnableRemoteController(false);
	}
	
	void checkCollistion() {

		for (Platform platform : platformFactory.getPlatforms()) {
			if (platform.isEnable()
					&& RectF.intersects(panda.getCollisionRectF(),
							platform.getFrame())) {
				boolean isDown = false;
				boolean canRun = false;

				panda.setY(platform.getY() - panda.h);

				if (platform.isDown) {
					isDown = true;
					platform.setEnable(false);
					platform.setHidden(true);
				} else if (platform.isShock) {
					platform.isShock = false;
				}
				if (panda.getY() < platform.getY()) {
					canRun = true;
				}

				panda.jumpEnd = panda.getY();
				if (panda.jumpEnd - panda.jumpStart >= 70) {
					panda.roll();
					AudioUtil.playRoll();

					if (!isDown) {
						downAndUp(panda, 50, 0.05f, -50, 0.1f, false);
						downAndUp(platform, 50, 0.05f, -50, 0.1f, false);
						Log.e("Run", "DownUp");
					}
				} else {
					if (canRun) {
						panda.run();
					}
				}

				// 落地后jumpstart数据要设为当前位置，防止自由落地计算出错
				panda.jumpStart = panda.getY();
				break;
			}
		}

		// 熊猫和苹果碰撞
		for (Sprite apple : appleFactory.arrApple) {
			if (apple.isEnable()
					&& RectF.intersects(panda.getCollisionRectF(),
							apple.getFrame())) {
				AudioUtil.playEat();
				this.appleNum++;
				apple.setHidden(true);
			}
		}
	}

	public void downAndUp(final Sprite sprite, float down, float downTime,
			float up, float upTime, boolean isRepeat) {
		MovementAction downAct = MAction
				.moveByY(down, (long) (downTime * 1000));
		MovementAction upAct = MAction.moveByY(up, (long) (upTime * 1000));
		// MAction use threadPool it would delay during action by action.
		// MAction2 has no delay during action by action.
		MovementAction downUpAct = MAction2.sequence(new MovementAction[] {
				downAct, upAct });
		downUpAct.setMovementActionController(new MovementAtionController());
		if (isRepeat) {
			sprite.runMovementActionAndAppend(MAction.repeatForever(downUpAct));
		} else {
			sprite.runMovementActionAndAppend(downUpAct);
		}
	}

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
